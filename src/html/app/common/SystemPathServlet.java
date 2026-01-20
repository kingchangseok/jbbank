package html.app.common;
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

import app.common.SystemPath;
import app.common.excelUtil;

@WebServlet("/webPage/common/SystemPathServlet")
public class SystemPathServlet extends HttpServlet {

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
			case "geteCAMSDir":
				response.getWriter().write(geteCAMSDir(jsonElement));
				break;
			case "getDevHome":
				response.getWriter().write(getDevHome(jsonElement));
				break;
			case "getTmpDir":
				response.getWriter().write(getTmpDir(jsonElement));
				break;
			case "getSysPath":
				response.getWriter().write(getSysPath(jsonElement));
				break;
			case "getDirPath":
				response.getWriter().write(getDirPath(jsonElement));
				break;
			case "getDirPath3":
				response.getWriter().write(getDirPath3(jsonElement));
				break;
			case "getDirPath3_ztree":
				response.getWriter().write(getDirPath3_ztree(jsonElement));
				break;
//			case "getRsrcPath":
//				response.getWriter().write(getRsrcPath(jsonElement));
//				break;
			case "setExcel" :
				response.getWriter().write( setExcel(jsonElement) );
				break;
			case "getRsrcPath_ztree":
				response.getWriter().write(getRsrcPath_ztree(jsonElement));
				break;
			case "getTopSysPath":
				response.getWriter().write(getTopSysPath(jsonElement));
				break;
			case "getDynamicPath":
				response.getWriter().write(getDynamicPath(jsonElement));
				break;
			case "getDirPath_File":
				response.getWriter().write(getDirPath_File(jsonElement));
				break;
			case "getTopSysPath_ztree":
				response.getWriter().write(getTopSysPath_ztree(jsonElement));
				break;
			case "getDirPathFile_ztree":
				response.getWriter().write(getDirPathFile_ztree(jsonElement));
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
	* String pCode
	*/ 
	private String geteCAMSDir(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String pCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pCode"));
		    return ParsingCommon.toJson(systempath.geteCAMSDir(pCode));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd
	*/ 
	private String getDevHome(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(systempath.getDevHome(UserId,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String pCode
	*/ 
	private String getTmpDir(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String pCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pCode"));
		    return ParsingCommon.toJson(systempath.getTmpDir(pCode));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String UserID,String syscd,String jobcd,String reqcd,String adminCk
	*/ 
	private String getSysPath(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String jobcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "jobcd"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    String adminCk = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "adminCk"));
		    return ParsingCommon.toJson(systempath.getSysPath(UserID,syscd,jobcd,reqcd,adminCk));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String UserID,String syscd,String jobcd,String reqcd,String adminCk
	*/ 
	private String getDirPath(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String jobcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "jobcd"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    String adminCk = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "adminCk"));
		    return ParsingCommon.toJson(systempath.getDirPath(UserID,syscd,jobcd,reqcd,adminCk));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	 * String UserID,String syscd,String jobcd,String reqcd,String adminCk
	 */ 
	private String getDirPath3(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
			String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
			String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
			String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
			String Info = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Info"));
			String seqNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "seqNo"));
			return ParsingCommon.toJson(systempath.getDirPath3(UserID,SysCd,RsrcCd,Info,seqNo));
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			systempath = null;
		} 
	}
	/* Parameter 
	 * String UserID,String syscd,String jobcd,String reqcd,String adminCk
	 */ 
	private String getDirPath3_ztree(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
			String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
			String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
			String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
			String Info = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Info"));
			String seqNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "seqNo"));
			return ParsingCommon.toJson(systempath.getDirPath3_ztree(UserId,SysCd,RsrcCd,Info,seqNo));
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			systempath = null;
		} 
	}
	/*
	 * String UserID,String SysCd,String SecuYn,String SinCd,String ReqCd
	 */
//	private String getRsrcPath(JsonElement jsonElement) throws SQLException, Exception {
//		SystemPath systempath = new SystemPath();
//		try {
//			String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
//			String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
//			String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
//			String SinCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SinCd"));
//			String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
//			return ParsingCommon.toJson( systempath.getRsrcPath(UserID,SysCd,SecuYn,SinCd,ReqCd));
//		} catch (IOException e) {
//			return ParsingCommon.toJson("ERROR"+e.getMessage());
//		} catch (Exception e) {
//			return ParsingCommon.toJson("ERROR"+e.getMessage());
//		} finally {
//			systempath = null;
//		} 
//	}
	private String setExcel(JsonElement jsonElement) throws SQLException, Exception {
		excelUtil excelUtil = new excelUtil();
		try {
			String filePath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "filePath"));
			ArrayList<String> headerDef = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "headerDef"));
			ArrayList<HashMap<String, String>> excelData = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "excelData"));
			return ParsingCommon.toJson( excelUtil.setExcel(filePath, headerDef, excelData));
		} catch (IOException e) { 
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			excelUtil = null;
		}
	}
	/*
	 * String UserID,String SysCd,String SecuYn,String SinCd,String ReqCd
	 */
	private String getRsrcPath_ztree(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		String SinCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SinCd"));
		String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		return ParsingCommon.toJson( systempath.getRsrcPath_ztree(UserID,SysCd,SecuYn,SinCd,ReqCd));
	}
	/* Parameter 
	* String UserID,String progName,boolean upSw
	*/ 
	private String getTopSysPath(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String progName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "progName"));
		    boolean upSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "upSw")));
		    return ParsingCommon.toJson(systempath.getTopSysPath(UserID,progName,upSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String UserID,String syscd,String rsrcCD,String rootPath,int pathlevel,String adminCk
	*/ 
	private String getDynamicPath(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String rsrcCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rsrcCD"));
		    String rootPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rootPath"));
		    int pathlevel = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pathlevel")));
		    String adminCk = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "adminCk"));
		    return ParsingCommon.toJson(systempath.getDynamicPath(UserID,syscd,rsrcCD,rootPath,pathlevel,adminCk));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String RsrcCd,String seqNo,String maxCnt,String progName,boolean upSw
	*/ 
	private String getDirPath_File(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String seqNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "seqNo"));
		    String maxCnt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "maxCnt"));
		    String progName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "progName"));
		    boolean upSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "upSw")));
		    return ParsingCommon.toJson(systempath.getDirPath_File(UserId,SysCd,RsrcCd,seqNo,maxCnt,progName,upSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	/* Parameter 
	* String UserID,String progName,boolean upSw
	*/ 
	private String getTopSysPath_ztree(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String progName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "progName"));
		    boolean upSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "upSw")));
		    return ParsingCommon.toJson(systempath.getTopSysPath_ztree(UserID,progName,upSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
	
	/* Parameter 
	* String UserId,String SysCd,String RsrcCd,String seqNo,String maxCnt,String progName,boolean upSw
	*/ 
	private String getDirPathFile_ztree(JsonElement jsonElement) throws SQLException, Exception {
		SystemPath systempath = new SystemPath();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String seqNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "seqNo"));
		    String maxCnt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "maxCnt"));
		    String progName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "progName"));
		    boolean upSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "upSw")));
		    return ParsingCommon.toJson(systempath.getDirPathFile_ztree(UserId,SysCd,RsrcCd,seqNo,maxCnt,progName,upSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    systempath = null;
		} 
	}
}
