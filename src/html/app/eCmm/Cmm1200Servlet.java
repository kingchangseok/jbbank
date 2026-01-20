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

import app.eCmm.Cmm1200;

@WebServlet("/webPage/ecmm/Cmm1200Servlet")
public class Cmm1200Servlet extends HttpServlet {

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
			case "getPathList":
				response.getWriter().write(getPathList(jsonElement));
				break;
			case "getBaseInfo":
				response.getWriter().write(getBaseInfo(jsonElement));
				break;
			case "removePath":
				response.getWriter().write(removePath(jsonElement));
				break;
			case "savePath":
				response.getWriter().write(savePath(jsonElement));
				break;
			case "savePath2":
				response.getWriter().write(savePath2(jsonElement));
				break;
			case "getSvrDir_ztree":
				response.getWriter().write(getSvrDir_ztree(jsonElement));
				break;
			case "chkDirPath":
				response.getWriter().write(chkDirPath(jsonElement));
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
	* String sysCD,String spath
	*/ 
	private String getPathList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
		    String sysCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCD"));
		    String spath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "spath"));
		    return ParsingCommon.toJson(cmm1200.getPathList(sysCD,spath));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
	/* Parameter 
	* String sysCD,String baseCD
	*/ 
	private String getBaseInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
		    String sysCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCD"));
		    String baseCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "baseCD"));
		    return ParsingCommon.toJson(cmm1200.getBaseInfo(sysCD,baseCD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
	/* Parameter 
	* String sysCD,String spath,String dsnCD
	*/ 
	private String removePath(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
		    String sysCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCD"));
		    String spath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "spath"));
		    String dsnCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "dsnCD"));
		    return ParsingCommon.toJson(cmm1200.removePath(sysCD,spath,dsnCD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
	/* Parameter 
	* String sysCD,String UserId,ArrayList<HashMap<String,String>> saveList
	*/ 
	private String savePath(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
		    String sysCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCD"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> saveList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "saveList"));
		    return ParsingCommon.toJson(cmm1200.savePath(sysCD,UserId,saveList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
	/* Parameter 
	* String sysCD,String UserId,ArrayList<HashMap<String,String>> saveList
	*/ 
	private String savePath2(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
		    String sysCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCD"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> saveList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "saveList"));
		    return ParsingCommon.toJson(cmm1200.savePath2(sysCD,UserId,saveList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SvrIp,String SvrPort,String BaseDir
	*/ 
	private String getSvrDir_ztree(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrIp = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrIp"));
		    String SvrPort = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrPort"));
		    String BaseDir = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseDir"));
		    return ParsingCommon.toJson(cmm1200.getSvrDir_ztree(UserID,SysCd,SvrIp,SvrPort,BaseDir));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> DirListr
	*/ 
	private String chkDirPath(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1200 cmm1200 = new Cmm1200();
		try {
			ArrayList<HashMap<String,String>> DirList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "DirList"));
		    return ParsingCommon.toJson(cmm1200.chkDirPath(DirList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1200 = null;
		} 
	}
}
