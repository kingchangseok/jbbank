package html.app.eCmd;
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

import app.eCmd.Cmd1300;

@WebServlet("/webPage/ecmd/Cmd1300Servlet")
public class Cmd1300Servlet extends HttpServlet {

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
			case "get_Cbo_SignNMADD":
				response.getWriter().write(get_Cbo_SignNMADD(jsonElement));
				break;
			case "get_SignNM_cnt":
				response.getWriter().write(get_SignNM_cnt(jsonElement));
				break;
			case "get_Cbo_Select":
				response.getWriter().write(get_Cbo_Select(jsonElement));
				break;
			case "getTblUpdate":
				response.getWriter().write(getTblUpdate(jsonElement));
				break;
			case "getTblDelete":
				response.getWriter().write(getTblDelete(jsonElement));
				break;
			case "get_myGrid2_insert":
				response.getWriter().write(get_myGrid2_insert(jsonElement));
				break;
			case "get_Txt_SignNM_delete":
				response.getWriter().write(get_Txt_SignNM_delete(jsonElement));
				break;
			case "getSql_Qry":
				response.getWriter().write(getSql_Qry(jsonElement));
				break;
			case "setNoti":
				response.getWriter().write(setNoti(jsonElement));
				break;
			case "getNoti":
				response.getWriter().write(getNoti(jsonElement));
				break;
			case "dirOpenChk":
				response.getWriter().write(dirOpenChk(jsonElement));
				break;
			case "getMyPage":
				response.getWriter().write(getMyPage(jsonElement));
				break;
			case "setMyPage":
				response.getWriter().write(setMyPage(jsonElement));
				break;
			case "delMyPage":
				response.getWriter().write(delMyPage(jsonElement));
				break;
			case "cmdControl":
				response.getWriter().write(cmdControl(jsonElement));
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
	* String UserID
	*/ 
	private String get_Cbo_SignNMADD(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    return ParsingCommon.toJson(cmd1300.get_Cbo_SignNMADD(UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SignNM
	*/ 
	private String get_SignNM_cnt(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SignNM = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SignNM"));
		    return ParsingCommon.toJson(cmd1300.get_SignNM_cnt(UserId,SignNM));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserID,String SignNM
	*/ 
	private String get_Cbo_Select(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SignNM = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SignNM"));
		    return ParsingCommon.toJson(cmd1300.get_Cbo_Select(UserID,SignNM));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String Lbl_Dir
	*/ 
	private String getTblUpdate(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String Lbl_Dir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Lbl_Dir"));
		    return ParsingCommon.toJson(cmd1300.getTblUpdate(UserId,SysCd,Lbl_Dir));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> rvList
	*/ 
	private String getTblDelete(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> rvList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "rvList"));
		    return ParsingCommon.toJson(cmd1300.getTblDelete(UserId,rvList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> rtList,int Index,String SignName
	*/ 
	private String get_myGrid2_insert(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> rtList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "rtList"));
		    int Index = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Index")));
		    String SignName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SignName"));
		    return ParsingCommon.toJson(cmd1300.get_myGrid2_insert(UserId,rtList,Index,SignName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String USERID,String SIGNNM
	*/ 
	private String get_Txt_SignNM_delete(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String USERID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "USERID"));
		    String SIGNNM = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SIGNNM"));
		    return ParsingCommon.toJson(cmd1300.get_Txt_SignNM_delete(USERID,SIGNNM));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getSql_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1300.getSql_Qry(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String noti,String UserId
	*/ 
	private String setNoti(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String noti = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "noti"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1300.setNoti(noti,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getNoti(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1300.getNoti(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* ArrayList<String> fileList,String DevHome
	*/ 
	private String dirOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    ArrayList<String> fileList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String DevHome = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DevHome"));
		    return ParsingCommon.toJson(cmd1300.dirOpenChk(fileList,DevHome));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getMyPage(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1300.getMyPage(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> prjList
	*/ 
	private String setMyPage(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> prjList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "prjList"));
		    return ParsingCommon.toJson(cmd1300.setMyPage(etcData,prjList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> delPrjList
	*/ 
	private String delMyPage(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> delPrjList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "delPrjList"));
		    return ParsingCommon.toJson(cmd1300.delMyPage(UserId,delPrjList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1300 = null;
		} 
	}
	private String cmdControl(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1300 cmd1300 = new Cmd1300();
		try {
			String userId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userId"));
			String targetGB = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "targetGB"));
			String Sv_GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_GbnCd"));
			String Sv_Dir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_Dir"));
			String agentIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "agentIp"));
			String agentDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "agentDir"));
			String paramPid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "paramPid"));
			return ParsingCommon.toJson(cmd1300.cmdControl(userId,targetGB,Sv_GbnCd,Sv_Dir,agentIp,agentDir,paramPid));
		} catch (IOException e) { //취약점 수정
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			cmd1300 = null;
		}
	}
}
