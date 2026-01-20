package html.app.eCmc;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import app.eCmc.Cmc0400_20251028;

@WebServlet("/webPage/ecmc/Cmc0400Servlet")
public class Cmc0400Servlet extends HttpServlet {

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
			case "setREQInfo":
				response.getWriter().write(setREQInfo(jsonElement));
				break;
			case "setREQupdt":
				response.getWriter().write(setREQupdt(jsonElement));
				break;
			case "getREQList":
				response.getWriter().write(getREQList(jsonElement));
				break;
			case "delREQinfo":
				response.getWriter().write(delREQinfo(jsonElement));
				break;
			case "getReqInfo":
				response.getWriter().write(getReqInfo(jsonElement));
				break;
			case "getRequest":
				response.getWriter().write(getRequest(jsonElement));
				break;
			case "getReqRunners":
				response.getWriter().write(getReqRunners(jsonElement));
				break;
			case "statusUpdt":
				response.getWriter().write(statusUpdt(jsonElement));
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
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> TeamList,ArrayList<HashMap<String,String>> UserList,ArrayList<HashMap<String,String>> tmpRunnerList
	*/ 
	private String setREQInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> TeamList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "TeamList"));
	   	    ArrayList<HashMap<String,String>> UserList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "UserList"));
	   	    ArrayList<HashMap<String,String>> tmpRunnerList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "tmpRunnerList"));
		    return ParsingCommon.toJson(cmc0400.setREQInfo(etcData,TeamList,UserList,tmpRunnerList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> TeamList,ArrayList<HashMap<String,String>> UserList,ArrayList<HashMap<String,String>> tmpRunnerList
	*/ 
	private String setREQupdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> TeamList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "TeamList"));
	   	    ArrayList<HashMap<String,String>> UserList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "UserList"));
	   	    ArrayList<HashMap<String,String>> tmpRunnerList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "tmpRunnerList"));
		    return ParsingCommon.toJson(cmc0400.setREQupdt(etcData,TeamList,UserList,tmpRunnerList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* String status,String UserID,String cboGbn,String datStD,String datEdD,String DocType
	*/ 
	private String getREQList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    String status = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "status"));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String editorName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "editorName"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    String datStD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "datStD"));
		    String datEdD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "datEdD"));
		    String DocType = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocType"));
		    return ParsingCommon.toJson(cmc0400.getREQList(status,UserID,editorName,cboGbn,datStD,datEdD,DocType));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> delREQ
	*/ 
	private String delREQinfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    HashMap<String,String> delREQ = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "delREQ"));
		    return ParsingCommon.toJson(cmc0400.delREQinfo(delREQ));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* String UserId,String Reqid
	*/ 
	private String getReqInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String Reqid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Reqid"));
		    return ParsingCommon.toJson(cmc0400.getReqInfo(UserId,Reqid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* String OrderId
	*/ 
	private String getRequest(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    return ParsingCommon.toJson(cmc0400.getRequest(OrderId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* String ReqId
	*/ 
	private String getReqRunners(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    String ReqId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqId"));
		    return ParsingCommon.toJson(cmc0400.getReqRunners(ReqId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
	/* Parameter 
	* String userID,String reqID
	*/ 
	private String statusUpdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0400_20251028 cmc0400 = new Cmc0400_20251028();
		try {
		    String userID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userID"));
		    String reqID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqID"));
		    return ParsingCommon.toJson(cmc0400.statusUpdt(userID,reqID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0400 = null;
		} 
	}
}
