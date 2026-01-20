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

import app.eCmm.Cmm0200;

@WebServlet("/webPage/ecmm/Cmm0200Servlet")
public class Cmm0200Servlet extends HttpServlet {

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
			case "getSysInfo_List":
				response.getWriter().write(getSysInfo_List(jsonElement));
				break;
			case "getSvrInfo":
				response.getWriter().write(getSvrInfo(jsonElement));
				break;
			case "getScriptList":
				response.getWriter().write(getScriptList(jsonElement));
				break;
			case "factUpdt":
				response.getWriter().write(factUpdt(jsonElement));
				break;
			case "sysInfo_Updt":
				response.getWriter().write(sysInfo_Updt(jsonElement));
				break;
			case "sysInfo_Close":
				response.getWriter().write(sysInfo_Close(jsonElement));
				break;
			case "delScriptInfo":
				response.getWriter().write(delScriptInfo(jsonElement));
				break;
			case "setScriptcode":
				response.getWriter().write(setScriptcode(jsonElement));
				break;
			case "cmd0200_Excption":
				response.getWriter().write(cmd0200_Excption(jsonElement));
				break;
			case "getSysJobInfo":
				response.getWriter().write(getSysJobInfo(jsonElement));
				break;
			case "setGitJobInfo":
				response.getWriter().write(setGitJobInfo(jsonElement));
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
	* boolean clsSw,String SysCd
	*/ 
	private String getSysInfo_List(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    boolean clsSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "clsSw")));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200.getSysInfo_List(clsSw,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getSvrInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200.getSvrInfo(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getScriptList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200.getScriptList(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String factUpdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    return ParsingCommon.toJson(cmm0200.factUpdt());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String sysInfo_Updt(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0200.sysInfo_Updt(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String UserId
	*/ 
	private String sysInfo_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm0200.sysInfo_Close(SysCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String CompCd
	*/ 
	private String delScriptInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String CompCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CompCd"));
		    String gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gubun"));
		    return ParsingCommon.toJson(cmm0200.delScriptInfo(SysCd,CompCd,gubun));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> Data
	*/ 
	private String setScriptcode(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    HashMap<String,String> Data = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "Data"));
		    return ParsingCommon.toJson(cmm0200.setScriptcode(Data));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCmd0200List
	*/ 
	private String cmd0200_Excption(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
	   	    ArrayList<HashMap<String,String>> getCmd0200List = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCmd0200List"));
		    return ParsingCommon.toJson(cmm0200.cmd0200_Excption(getCmd0200List));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCmd0200List
	*/ 
	private String getSysJobInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200.getSysJobInfo(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setGitJobInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200 cmm0200 = new Cmm0200();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "gitData"));
		    return ParsingCommon.toJson(cmm0200.setGitJobInfo(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200 = null;
		} 
	}
}
