package html.app.eCmr;
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

import app.eCmr.Cmr3400;

@WebServlet("/webPage/ecmr/Cmr3400Servlet")
public class Cmr3400Servlet extends HttpServlet {

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
			case "getReqList_Emg":
				response.getWriter().write(getReqList_Emg(jsonElement));
				break;
			case "getTeamInfo":
				response.getWriter().write(getTeamInfo(jsonElement));
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
	* String strQryCd,String UserId,String sDate,String eDate,String UserNo,String strDept,String ProgName
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3400 cmr3400 = new Cmr3400();
		try {
		    String strQryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strQryCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String sDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sDate"));
		    String eDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "eDate"));
		    String UserNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserNo"));
		    String strDept = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDept"));
		    String ProgName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgName"));
		    return ParsingCommon.toJson(cmr3400.getReqList(strQryCd,UserId,sDate,eDate,UserNo,strDept,ProgName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3400 = null;
		} 
	}
	/* Parameter 
	 * String strQryCd,String UserId,String sDate,String eDate,String UserNo,String strDept,String ProgName,String emgSw
	 */ 
	private String getReqList_Emg(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3400 cmr3400 = new Cmr3400();
		try {
			String strQryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strQryCd"));
			String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
			String sDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sDate"));
			String eDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "eDate"));
			String UserNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserNo"));
			String strDept = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDept"));
			String ProgName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgName"));
			String emgSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "emgSw"));
			return ParsingCommon.toJson(cmr3400.getReqList_Emg(strQryCd,UserId,sDate,eDate,UserNo,strDept,ProgName,emgSw));
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			cmr3400 = null;
		} 
	}
	/* Parameter 
	 * String strQryCd,String UserId,String sDate,String eDate,String UserNo,String strDept,String ProgName
	 */ 
	private String getTeamInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3400 cmr3400 = new Cmr3400();
		try {
			String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
			String itYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itYn"));
			return ParsingCommon.toJson(cmr3400.getTeamInfo(SelMsg, itYn));
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			cmr3400 = null;
		} 
	}
}
