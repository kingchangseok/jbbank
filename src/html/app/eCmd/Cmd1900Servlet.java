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

import app.eCmd.Cmd1900;

@WebServlet("/webPage/ecmd/Cmd1900Servlet")
public class Cmd1900Servlet extends HttpServlet {

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
			case "getSvrDir":
				response.getWriter().write(getSvrDir(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "dirOpenChk":
				response.getWriter().write(dirOpenChk(jsonElement));
				break;
			case "fileOpenChk":
				response.getWriter().write(fileOpenChk(jsonElement));
				break;
			case "request_Check_In":
				response.getWriter().write(request_Check_In(jsonElement));
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
	* String UserID,String SvrIp,String SvrPort,String BufSize,String AgentDir,String SysOs,String HomeDir
	*/ 
	private String getSvrDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1900 cmd1900 = new Cmd1900();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String BufSize = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BufSize"));
		    String AgentDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AgentDir"));
		    String SysOs = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysOs"));
		    String HomeDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomeDir"));
		    return ParsingCommon.toJson(cmd1900.getSvrDir(UserID,SvrIp,SvrPort,BufSize,AgentDir,SysOs,HomeDir));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1900 = null;
		} 
	}
	/* Parameter 
	* String UserID,ArrayList<HashMap<String,String>> dirList,String SvrIp,String SvrPort,String HomeDir,String SvrCd,String AgentDir,String SysOs
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1900 cmd1900 = new Cmd1900();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
	   	    ArrayList<HashMap<String,String>> dirList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "dirList"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String HomeDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomeDir"));
		    String SvrCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrCd"));
		    String AgentDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AgentDir"));
		    String SysOs = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysOs"));
		    return ParsingCommon.toJson(cmd1900.getFileList(UserID,dirList,SvrIp,SvrPort,HomeDir,SvrCd,AgentDir,SysOs));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1900 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String devHome,ArrayList<String> dirList
	*/ 
	private String dirOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1900 cmd1900 = new Cmd1900();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String devHome = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "devHome"));
		    ArrayList<String> dirList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "dirList"));
		    return ParsingCommon.toJson(cmd1900.dirOpenChk(PrjNo,devHome,dirList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1900 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,ArrayList<HashMap<String,String>> fileList,String strDevHome,String UserId
	*/ 
	private String fileOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1900 cmd1900 = new Cmd1900();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String strDevHome = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDevHome"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1900.fileOpenChk(PrjNo,fileList,strDevHome,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1900 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> dataObj,ArrayList<HashMap<String,Object>>ConfList
	*/ 
	private String request_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1900 cmd1900 = new Cmd1900();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    return ParsingCommon.toJson(cmd1900.request_Check_In(chkInList,dataObj,ConfList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1900 = null;
		} 
	}
}
