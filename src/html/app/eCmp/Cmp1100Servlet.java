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

import app.eCmp.Cmp1100;

@WebServlet("/webPage/ecmp/Cmp1100Servlet")
public class Cmp1100Servlet extends HttpServlet {

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
	* String UserId,String SysCd,String DeptCd,String QryCd1,String QryCd2,String FindCd,String FindWord,String StDate,String EdDate,String strDayCd,String approval,boolean subItem
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp1100 cmp1100 = new Cmp1100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String DeptCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeptCd"));
		    String QryCd1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd1"));
		    String QryCd2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd2"));
		    String FindCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "FindCd"));
		    String FindWord = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "FindWord"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    String strDayCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDayCd"));
		    String approval = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "approval"));
		    boolean subItem = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "subItem")));
		    return ParsingCommon.toJson(cmp1100.getReqList(UserId,SysCd,DeptCd,QryCd1,QryCd2,FindCd,FindWord,StDate,EdDate,strDayCd,approval,subItem));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp1100 = null;
		} 
	}
}
