package html.app.eCmr;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

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

import app.eCmr.Cmr0020;

@WebServlet("/webPage/ecmr/Cmr0020Servlet")
public class Cmr0020Servlet extends HttpServlet {

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
			case "getSelectList":
				response.getWriter().write(getSelectList(jsonElement));
				break;
			case "get_Worktime":
				response.getWriter().write(get_Worktime(jsonElement));
				break;
			case "setInsertList":
				response.getWriter().write(setInsertList(jsonElement));
				break;
			case "setTimeInsertList":
				response.getWriter().write(setTimeInsertList(jsonElement));
				break;
			case "setTimeDeleteList":
				response.getWriter().write(setTimeDeleteList(jsonElement));
				break;
			case "getWorkDays":
				response.getWriter().write(getWorkDays(jsonElement));
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
	* String IsrId,String SubId,String UserId,String ReqCd
	*/ 
	private String getSelectList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0020 cmr0020 = new Cmr0020();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr0020.getSelectList(IsrId,SubId,UserId,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0020 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub
	*/ 
	private String get_Worktime(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0020 cmr0020 = new Cmr0020();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    return ParsingCommon.toJson(cmr0020.get_Worktime(IsrId,IsrSub));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0020 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setInsertList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0020 cmr0020 = new Cmr0020();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0020.setInsertList(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0020 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setTimeInsertList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0020 cmr0020 = new Cmr0020();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0020.setTimeInsertList(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0020 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setTimeDeleteList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0020 cmr0020 = new Cmr0020();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0020.setTimeDeleteList(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0020 = null;
		} 
	}
	/* Parameter 
	* String strYear
	*/ 
	private String getWorkDays(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0020 cmr0020 = new Cmr0020();
		try {
		    String strYear = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strYear"));
		    return ParsingCommon.toJson(cmr0020.getWorkDays(strYear));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0020 = null;
		} 
	}
}
