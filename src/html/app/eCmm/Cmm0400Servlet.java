package html.app.eCmm;
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

import app.eCmm.Cmm0400;

@WebServlet("/webPage/ecmm/Cmm0400Servlet")
public class Cmm0400Servlet extends HttpServlet {

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
			case "getUserInfo":
				response.getWriter().write(getUserInfo(jsonElement));
				break;
			case "getUserRGTCD":
				response.getWriter().write(getUserRGTCD(jsonElement));
				break;
			case "getUserJobList":
				response.getWriter().write(getUserJobList(jsonElement));
				break;
			case "getUserRgtDept":
				response.getWriter().write(getUserRgtDept(jsonElement));
				break;
			case "getUserRgtDept_All":
				response.getWriter().write(getUserRgtDept_All(jsonElement));
				break;
			case "setUserInfo":
				response.getWriter().write(setUserInfo(jsonElement));
				break;
			case "delUserInfo":
				response.getWriter().write(delUserInfo(jsonElement));
				break;
			case "delUserJob":
				response.getWriter().write(delUserJob(jsonElement));
				break;
			case "getTeamList":
				response.getWriter().write(getTeamList(jsonElement));
				break;
			case "getAllUserInfo":
				response.getWriter().write(getAllUserInfo(jsonElement));
				break;
			case "getUserList":
				response.getWriter().write(getUserList(jsonElement));
				break;
			case "getTeamUserList":
				response.getWriter().write(getTeamUserList(jsonElement));
				break;
			case "setAllUserJob":
				response.getWriter().write(setAllUserJob(jsonElement));
				break;
			case "getAllUser":
				response.getWriter().write(getAllUser(jsonElement));
				break;
			case "userCopy":
				response.getWriter().write(userCopy(jsonElement));
				break;
			case "subNewDir":
				response.getWriter().write(subNewDir(jsonElement));
				break;
			case "subDelDir_Check":
				response.getWriter().write(subDelDir_Check(jsonElement));
				break;
			case "subDelDir":
				response.getWriter().write(subDelDir(jsonElement));
				break;
			case "subRename":
				response.getWriter().write(subRename(jsonElement));
				break;
			case "setRgtDept":
				response.getWriter().write(setRgtDept(jsonElement));
				break;
			case "delRgtDept":
				response.getWriter().write(delRgtDept(jsonElement));
				break;
			case "setSHA256":
				response.getWriter().write(setSHA256(jsonElement));
				break;
			case "setSHA256_userid":
				response.getWriter().write(setSHA256_userid(jsonElement));
				break;
			case "setUpdateClose":
				response.getWriter().write(setUpdateClose(jsonElement));
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
	* String UserId,String UserName
	*/ 
	private String getUserInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String UserName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserName"));
		    return ParsingCommon.toJson(cmm0400.getUserInfo(UserId,UserName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getUserRGTCD(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm0400.getUserRGTCD(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String gbnCd,String UserId
	*/ 
	private String getUserJobList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String gbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gbnCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm0400.getUserJobList(gbnCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getUserRgtDept(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm0400.getUserRgtDept(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getUserRgtDept_All(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    return ParsingCommon.toJson(cmm0400.getUserRgtDept_All());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj,ArrayList<HashMap<String,String>> DutyList,ArrayList<HashMap<String,String>> JobList
	*/ 
	private String setUserInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
	   	    ArrayList<HashMap<String,String>> DutyList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "DutyList"));
	   	    ArrayList<HashMap<String,String>> JobList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "JobList"));
		    return ParsingCommon.toJson(cmm0400.setUserInfo(dataObj,DutyList,JobList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String delUserInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm0400.delUserInfo(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> JobList
	*/ 
	private String delUserJob(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> JobList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "JobList"));
		    return ParsingCommon.toJson(cmm0400.delUserJob(UserId,JobList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTeamList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    return ParsingCommon.toJson(cmm0400.getTeamList());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String Cbo_Team,int Option
	*/ 
	private String getAllUserInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String Cbo_Team = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_Team"));
		    String Cbo_Rgtcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_Rgtcd"));
		    int Option = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Option")));
		    return ParsingCommon.toJson(cmm0400.getAllUserInfo(Cbo_Team,Cbo_Rgtcd,Option));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* int selectedIndex,String UserName
	*/ 
	private String getUserList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    int selectedIndex = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selectedIndex")));
		    String UserName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserName"));
		    return ParsingCommon.toJson(cmm0400.getUserList(selectedIndex,UserName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String Cbo_Sign
	*/ 
	private String getTeamUserList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String Cbo_Sign = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_Sign"));
		    return ParsingCommon.toJson(cmm0400.getTeamUserList(Cbo_Sign));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String SysCd,ArrayList<HashMap<String,String>> UserList,ArrayList<HashMap<String,String>> JobList
	*/ 
	private String setAllUserJob(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
	   	    ArrayList<HashMap<String,String>> UserList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "UserList"));
	   	    ArrayList<HashMap<String,String>> JobList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "JobList"));
		    return ParsingCommon.toJson(cmm0400.setAllUserJob(SysCd,UserList,JobList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getAllUser(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    return ParsingCommon.toJson(cmm0400.getAllUser());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> JobList,ArrayList<HashMap<String,String>> RgtList
	*/ 
	private String userCopy(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> JobList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "JobList"));
	   	    ArrayList<HashMap<String,String>> RgtList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "RgtList"));
		    return ParsingCommon.toJson(cmm0400.userCopy(etcData,JobList,RgtList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subNewDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0400.subNewDir(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subDelDir_Check(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0400.subDelDir_Check(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subDelDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0400.subDelDir(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subRename(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0400.subRename(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String UserId,String dutyCd,String deptCd
	*/ 
	private String setRgtDept(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String dutyCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "dutyCd"));
		    String deptCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "deptCd"));
		    return ParsingCommon.toJson(cmm0400.setRgtDept(UserId,dutyCd,deptCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> rgtList
	*/ 
	private String delRgtDept(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
	   	    ArrayList<HashMap<String,String>> rgtList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "rgtList"));
		    return ParsingCommon.toJson(cmm0400.delRgtDept(rgtList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String setSHA256(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    return ParsingCommon.toJson(cmm0400.setSHA256());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String userid
	*/ 
	private String setSHA256_userid(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    return ParsingCommon.toJson(cmm0400.setSHA256(userid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
	/* Parameter 
	* String userId
	*/ 
	private String setUpdateClose(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0400 cmm0400 = new Cmm0400();
		try {
		    String userId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userId"));
		    return ParsingCommon.toJson(cmm0400.setUpdateClose(userId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0400 = null;
		} 
	}
}
