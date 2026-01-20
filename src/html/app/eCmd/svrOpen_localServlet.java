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

import app.eCmd.svrOpen_local;

@WebServlet("/webPage/ecmd/svrOpen_localServlet")
public class svrOpen_localServlet extends HttpServlet {

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
			case "cmr0020_Insert_total":
				response.getWriter().write(cmr0020_Insert_total(jsonElement));
				break;
			case "dirOpenChk":
				response.getWriter().write(dirOpenChk(jsonElement));
				break;
			case "fileOpenChk":
				response.getWriter().write(fileOpenChk(jsonElement));
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
	* String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String BaseDir,String HomeDir,String SysMsg
	*/ 
	private String getSvrDir(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen_local svropen_local = new svrOpen_local();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String BufSize = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BufSize"));
		    String BaseDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseDir"));
		    String HomeDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomeDir"));
		    String SysMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysMsg"));
		    return ParsingCommon.toJson(svropen_local.getSvrDir(UserID,SysCd,SvrIp,SvrPort,BufSize,BaseDir,HomeDir,SysMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen_local = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String HomeDir,String BaseDir,String SvrCd,String GbnCd
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen_local svropen_local = new svrOpen_local();
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
		    return ParsingCommon.toJson(svropen_local.getFileList(UserID,SysCd,SvrIp,SvrPort,BufSize,HomeDir,BaseDir,SvrCd,GbnCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen_local = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,HashMap<String,String> etcData
	*/ 
	private String cmr0020_Insert_total(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen_local svropen_local = new svrOpen_local();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(svropen_local.cmr0020_Insert_total(fileList,etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen_local = null;
		} 
	}
	/* Parameter 
	* String SysCd,ArrayList<String> fileList,String DevHome
	*/ 
	private String dirOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen_local svropen_local = new svrOpen_local();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    ArrayList<String> fileList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String DevHome = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DevHome"));
		    return ParsingCommon.toJson(svropen_local.dirOpenChk(SysCd,fileList,DevHome));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen_local = null;
		} 
	}
	/* Parameter 
	* String SysCd,String fullPath,ArrayList<String> fileList,String strDevHome,String exeName
	*/ 
	private String fileOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		svrOpen_local svropen_local = new svrOpen_local();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String fullPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "fullPath"));
		    ArrayList<String> fileList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String strDevHome = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDevHome"));
		    String exeName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "exeName"));
		    return ParsingCommon.toJson(svropen_local.fileOpenChk(SysCd,fullPath,fileList,strDevHome,exeName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    svropen_local = null;
		} 
	}
}
