package html.app.eCmc;
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

import app.eCmc.Cmc1300;

@WebServlet("/webPage/ecmc/Cmc1300Servlet")
public class Cmc1300Servlet extends HttpServlet {

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
	* String pSta,String pTeam,String pStD,String pEdD,String pUser1,String pUser2
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc1300 cmc1300 = new Cmc1300();
		try {
		    String pSta = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pSta"));
		    String pTeam = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pTeam"));
		    String pStD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStD"));
		    String pEdD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEdD"));
		    String pUser1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUser1"));
		    String pUser2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUser2"));
		    return ParsingCommon.toJson(cmc1300.getReqList(pSta,pTeam,pStD,pEdD,pUser1,pUser2));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc1300 = null;
		} 
	}
}
