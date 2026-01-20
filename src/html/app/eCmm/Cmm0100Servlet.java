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

import app.eCmm.Cmm0100;

@WebServlet("/webPage/ecmm/Cmm0100Servlet")
public class Cmm0100Servlet extends HttpServlet {

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
			case "getCodeList":
				response.getWriter().write(getCodeList(jsonElement));
				break;
			case "getCodeName":
				response.getWriter().write(getCodeName(jsonElement));
				break;
			case "setCodeValue":
				response.getWriter().write(setCodeValue(jsonElement));
				break;
			case "getLangList":
				response.getWriter().write(getLangList(jsonElement));
				break;
			case "getExeName":
				response.getWriter().write(getExeName(jsonElement));
				break;
			case "setLangInfo":
				response.getWriter().write(setLangInfo(jsonElement));
				break;
			case "getJobList":
				response.getWriter().write(getJobList(jsonElement));
				break;
			case "setJobInfo":
				response.getWriter().write(setJobInfo(jsonElement));
				break;
			case "setJobInfo_individual":
				response.getWriter().write(setJobInfo_individual(jsonElement));
				break;
			case "delJobInfo":
				response.getWriter().write(delJobInfo(jsonElement));
				break;
			case "getProcInfo_Init":
				response.getWriter().write(getProcInfo_Init(jsonElement));
				break;
			case "getCodeInfo":
				response.getWriter().write(getCodeInfo(jsonElement));
				break;
			case "getCodeSelInfo":
				response.getWriter().write(getCodeSelInfo(jsonElement));
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
			case "updtProgInfo":
				response.getWriter().write(updtProgInfo(jsonElement));
				break;
			case "getProgInfoZTree":
				response.getWriter().write(getProgInfoZTree(jsonElement));
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
	* HashMap<String,String> dataObj
	*/ 
	private String getCodeList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.getCodeList(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* String txtCode0
	*/ 
	private String getCodeName(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    String txtCode0 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtCode0"));
		    return ParsingCommon.toJson(cmm0100.getCodeName(txtCode0));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String setCodeValue(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.setCodeValue(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getLangList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    return ParsingCommon.toJson(cmm0100.getLangList());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* String txtCode0
	*/ 
	private String getExeName(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    String txtCode0 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtCode0"));
		    return ParsingCommon.toJson(cmm0100.getExeName(txtCode0));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String setLangInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.setLangInfo(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getJobList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    return ParsingCommon.toJson(cmm0100.getJobList());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> JobList
	*/ 
	private String setJobInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
	   	    ArrayList<HashMap<String,String>> JobList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "JobList"));
		    return ParsingCommon.toJson(cmm0100.setJobInfo(JobList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* String cm_jobcd,String cm_jobname,String cm_deptcd
	*/ 
	private String setJobInfo_individual(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    String cm_jobcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_jobcd"));
		    String cm_jobname = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_jobname"));
		    String cm_deptcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_deptcd"));
		    return ParsingCommon.toJson(cmm0100.setJobInfo_individual(cm_jobcd,cm_jobname,cm_deptcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> jobList
	*/ 
	private String delJobInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
	   	    ArrayList<HashMap<String,String>> jobList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "jobList"));
		    return ParsingCommon.toJson(cmm0100.delJobInfo(jobList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getProcInfo_Init(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    return ParsingCommon.toJson(cmm0100.getProcInfo_Init());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getCodeInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    return ParsingCommon.toJson(cmm0100.getCodeInfo());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* String res
	*/ 
	private String getCodeSelInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    String res = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "res"));
		    return ParsingCommon.toJson(cmm0100.getCodeSelInfo(res));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subNewDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.subNewDir(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subDelDir_Check(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.subDelDir_Check(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subDelDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.subDelDir(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String subRename(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.subRename(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String updtProgInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm0100.updtProgInfo(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0100 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getProgInfoZTree(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0100 cmm0100 = new Cmm0100();
		return ParsingCommon.toJson(cmm0100.getProgInfoZTree());
	}
}
