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

import app.eCmd.Cmd1600;

@WebServlet("/webPage/ecmd/Cmd1600Servlet")
public class Cmd1600Servlet extends HttpServlet {

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
			case "dirAllList":
				response.getWriter().write(dirAllList(jsonElement));
				break;
			case "dirFullList":
				response.getWriter().write(dirFullList(jsonElement));
				break;
			case "fileOpenChk":
				response.getWriter().write(fileOpenChk(jsonElement));
				break;
			case "dirOpenChk":
				response.getWriter().write(dirOpenChk(jsonElement));
				break;
			case "subNewDir":
				response.getWriter().write(subNewDir(jsonElement));
				break;
			case "subDelDir":
				response.getWriter().write(subDelDir(jsonElement));
				break;
			case "subRename":
				response.getWriter().write(subRename(jsonElement));
				break;
			case "getNodeInfo":
				response.getWriter().write(getNodeInfo(jsonElement));
				break;
			case "confSelect":
				response.getWriter().write(confSelect(jsonElement));
				break;
			case "request_Check_In":
				response.getWriter().write(request_Check_In(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
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
	* String PrjNo,String DocSeq,String HomePath
	*/ 
	private String dirAllList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String DocSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocSeq"));
		    String HomePath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomePath"));
		    return ParsingCommon.toJson(cmd1600.dirAllList(PrjNo,DocSeq,HomePath));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String DocSeq,String HomePath,String selPath
	*/ 
	private String dirFullList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String DocSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocSeq"));
		    String HomePath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HomePath"));
		    String selPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selPath"));
		    return ParsingCommon.toJson(cmd1600.dirFullList(PrjNo,DocSeq,HomePath,selPath));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String fullPath,String localPath,String DocSeq,ArrayList<String> fileList,String UserId
	*/ 
	private String fileOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String fullPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "fullPath"));
		    String localPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "localPath"));
		    String DocSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocSeq"));
		    ArrayList<String> fileList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1600.fileOpenChk(PrjNo,fullPath,localPath,DocSeq,fileList,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String fullPath,String DocSeq,ArrayList<String> fileList
	*/ 
	private String dirOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String fullPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "fullPath"));
		    String DocSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocSeq"));
		    ArrayList<String> fileList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmd1600.dirOpenChk(PrjNo,fullPath,DocSeq,fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subNewDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd1600.subNewDir(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subDelDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd1600.subDelDir(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subRename(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd1600.subRename(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String docseq
	*/ 
	private String getNodeInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String docseq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "docseq"));
		    return ParsingCommon.toJson(cmd1600.getNodeInfo(PrjNo,docseq));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String confSelect(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd1600.confSelect(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> dataObj,ArrayList<HashMap<String,Object>>gyulData,ArrayList<HashMap<String,String>> DocList
	*/ 
	private String request_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    ArrayList<HashMap<String,Object>> gyulData = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "gyulData"));
	   	    ArrayList<HashMap<String,String>> DocList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "DocList"));
		    return ParsingCommon.toJson(cmd1600.request_Check_In(chkInList,dataObj,gyulData,DocList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String SysGb,String DsnCd,String RsrcCd,String RsrcName,String ReqCd,String JobCd,boolean UpLowSw,boolean selfSw,boolean LikeSw
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1600 cmd1600 = new Cmd1600();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SysGb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGb"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    boolean UpLowSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UpLowSw")));
		    boolean selfSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selfSw")));
		    boolean LikeSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LikeSw")));
		    return ParsingCommon.toJson(cmd1600.getFileList(UserId,SysCd,SysGb,DsnCd,RsrcCd,RsrcName,ReqCd,JobCd,UpLowSw,selfSw,LikeSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1600 = null;
		} 
	}
}
