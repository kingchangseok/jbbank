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

import app.eCmd.svrOpen;

@WebServlet("/webPage/ecmd/svrOpenServlet")
public class svrOpenServlet extends HttpServlet {

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
			case "getFileList_thread":
				response.getWriter().write(getFileList_thread(jsonElement));
				break;
			case "cmr0020_Insert_thread":
				response.getWriter().write(cmr0020_Insert_thread(jsonElement));
				break;
			case "getHomeDirList":
				response.getWriter().write(getHomeDirList(jsonElement));
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
	* String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String BaseDir,String AgentDir,String SysOs,String HomeDir,String svrName
	*/ 
	private String getSvrDir(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen svropen = new svrOpen();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String BufSize = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BufSize"));
		    String BaseDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseDir"));
		    String AgentDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AgentDir"));
		    String SysOs = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysOs"));
		    String HomeDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomeDir"));
		    String svrName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "svrName"));
		    return ParsingCommon.toJson(svropen.getSvrDir(UserID,SysCd,SvrIp,SvrPort,BufSize,BaseDir,AgentDir,SysOs,HomeDir,svrName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String BaseDir,String SvrCd,String SvrSeq,String GbnCd
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen svropen = new svrOpen();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String BufSize = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BufSize"));
		    String BaseDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseDir"));
		    String SvrCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrCd"));
		    String SvrSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrSeq"));
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    return ParsingCommon.toJson(svropen.getFileList(UserID,SysCd,SvrIp,SvrPort,BufSize,BaseDir,SvrCd,SvrSeq,GbnCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String HomeDir,String BaseDir,String SvrCd,String GbnCd,String exeName1,String exeName2,String SysInfo,String AgentDir,String SysOs
	*/ 
	private String getFileList_thread(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen svropen = new svrOpen();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String BufSize = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BufSize"));
		    String HomeDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomeDir"));
		    String BaseDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseDir"));
		    String SvrCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrCd"));
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    String exeName1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "exeName1"));
		    String exeName2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "exeName2"));
		    String SysInfo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysInfo"));
		    String AgentDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AgentDir"));
		    String SysOs = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysOs"));
		    return ParsingCommon.toJson(svropen.getFileList_thread(UserID,SysCd,SvrIp,SvrPort,BufSize,HomeDir,BaseDir,SvrCd,GbnCd,exeName1,exeName2,SysInfo,AgentDir,SysOs));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,HashMap<String,String> etcData
	*/ 
	private String cmr0020_Insert_thread(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen svropen = new svrOpen();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(svropen.cmr0020_Insert_thread(fileList,etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getHomeDirList(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen svropen = new svrOpen();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(svropen.getHomeDirList(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen = null;
		} 
	}
}
