package html.app.eCmp;
import java.io.IOException;
import java.sql.SQLException;

import com.ecams.util.exception.ErrorHandler;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import com.ecams.common.logger.EcamsLogger;

import html.app.common.ParsingCommon;
import html.app.common.token.service.TokenService;

import app.eCmp.Cmp3400;

@WebServlet("/webPage/ecmp/Cmp3400Servlet")
public class Cmp3400Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		doPost(req, resp);
 }

 @Override
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(ParsingCommon.getJsonStr(request));
		String requestType = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "requestType"));
		TokenService tokenService = new TokenService();
		ecamsLogger.error("requestType="+requestType);

		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");

			String jwtStr = tokenService.getToken(request);		
			if(StringUtils.isEmpty(jwtStr)) {                   
				throw new Exception("ERROR:Not Accessible."); 
			}                                                   

			switch (requestType) {
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
				break;
			default:
				throw new Exception("Servlet Function Not Exists");
			}
		} catch(SQLException e1) {
			ErrorHandler.handleError(request, response, e1);
		} catch(Exception e2) {
			ErrorHandler.handleError(request, response, e2);
		} finally {
			requestType = null;
		}
	}
	/* Parameter 
	* String UserId,String FindCd,String FindWord,String StDate,String EdDate,String ccbYn,String PrjNo,String PrjName,String Ilsu,String baseCd
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp3400 cmp3400 = new Cmp3400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String FindCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "FindCd"));
		    String FindWord = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "FindWord"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    String ccbYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ccbYn"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String PrjName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjName"));
		    String Ilsu = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Ilsu"));
		    String baseCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "baseCd"));
		    return ParsingCommon.toJson(cmp3400.getReqList(UserId,FindCd,FindWord,StDate,EdDate,ccbYn,PrjNo,PrjName,Ilsu,baseCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp3400 = null;
		} 
	}
}
