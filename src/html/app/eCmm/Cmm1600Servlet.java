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

import app.eCmm.Cmm1600;

@WebServlet("/webPage/ecmm/Cmm1600Servlet")
public class Cmm1600Servlet extends HttpServlet {

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
			case "getSvrInfo":
				response.getWriter().write(getSvrInfo(jsonElement));
				break;
			case "getFileList_excel":
				response.getWriter().write(getFileList_excel(jsonElement));
				break;
			case "request_Check_In":
				response.getWriter().write(request_Check_In(jsonElement));
				break;
			case "execShell":
				response.getWriter().write(execShell(jsonElement));
				break;
			case "execCmd":
				response.getWriter().write(execCmd(jsonElement));
				break;
			case "getExecCmd":
				response.getWriter().write(getExecCmd(jsonElement));
				break;
			case "getExecQry":
				response.getWriter().write(getExecQry(jsonElement));
				break;
			case "getRemoteUrl":
				response.getWriter().write(getRemoteUrl(jsonElement));
				break;
			case "getEncrypt":
				response.getWriter().write(getEncrypt(jsonElement));
				break;
			case "getUnicode":
				response.getWriter().write(getUnicode(jsonElement));
				break;
			case "getFileView":
				response.getWriter().write(getFileView(jsonElement));
				break;
			case "fileAttUpdt":
				response.getWriter().write(fileAttUpdt(jsonElement));
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
	* String SysCd
	*/ 
	private String getSvrInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm1600.getSvrInfo(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1600 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,HashMap<String,String> dataObj
	*/ 
	private String getFileList_excel(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm1600.getFileList_excel(fileList,dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1600 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData
	*/ 
	private String request_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm1600.request_Check_In(chkInList,etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1600 = null;
		} 
	}
	/* Parameter 
	* String shFile,String parmName,boolean viewSw
	*/ 
	private String execShell(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		String shFile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "shFile"));
		String parmName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "parmName"));
		boolean viewSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "viewSw")));
		return ParsingCommon.toJson(cmm1600.execShell(shFile,parmName,viewSw));
	}
	/* Parameter 
	* String cmdText,String UserId,String gbnCd,boolean viewSw
	*/ 
	private String execCmd(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		String cmdText = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cmdText"));
		String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		String gbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gbnCd"));
		boolean viewSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "viewSw")));
		return ParsingCommon.toJson(cmm1600.execCmd(cmdText,UserId,gbnCd,viewSw));
	}

	private String getExecCmd(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		boolean view = false;
		HashMap<String, String> cmdDataInfoMap = null;
		cmdDataInfoMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "cmdData"));
		if (cmdDataInfoMap.get("view").equals("ok")) {
			view = true;
		} else {
			view = false;
		}
		return ParsingCommon.toJson(cmm1600.execCmd(cmdDataInfoMap.get("txtcmd"), cmdDataInfoMap.get("userid"),
				cmdDataInfoMap.get("gbnCd"), view));
	}

	private String getExecQry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		HashMap<String, String> cmdDataInfoMap = null;
		cmdDataInfoMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "cmdData"));
		return ParsingCommon.toJson(cmm1600.get_SqlList(cmdDataInfoMap.get("txtcmd"),cmdDataInfoMap));
	}

	private String getRemoteUrl(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		HashMap<String, String> cmdDataInfoMap = null;
		cmdDataInfoMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "cmdData"));
		return ParsingCommon.toJson(cmm1600.getRemoteUrl(cmdDataInfoMap.get("txtcmd"), cmdDataInfoMap.get("userid"),
				cmdDataInfoMap.get("gbnCd"), cmdDataInfoMap.get("savePath")));
	}

	private String getEncrypt(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		HashMap<String, String> cmdDataInfoMap = null;
		cmdDataInfoMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "cmdData"));
		return ParsingCommon.toJson(cmm1600.getEncrypt(cmdDataInfoMap));
	}

	private String getUnicode(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		HashMap<String, String> cmdDataInfoMap = null;
		cmdDataInfoMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "cmdData"));
		return ParsingCommon.toJson(cmm1600.getUnicode(cmdDataInfoMap));
	}

	private String getFileView(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		try {
		    String saveFile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "saveFile"));
		    return ParsingCommon.toJson(cmm1600.getFileView(saveFile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1600 = null;
		} 
	}
	private String fileAttUpdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1600 cmm1600 = new Cmm1600();
		try {
		    String cmdText = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cmdText"));
		    return ParsingCommon.toJson(cmm1600.fileAttUpdt(cmdText));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1600 = null;
		} 
	}
}
