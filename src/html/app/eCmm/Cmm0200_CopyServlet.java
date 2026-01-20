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

import app.eCmm.Cmm0200_Copy;

@WebServlet("/webPage/ecmm/Cmm0200_CopyServlet")
public class Cmm0200_CopyServlet extends HttpServlet {

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
			case "getProgInfo":
				response.getWriter().write(getProgInfo(jsonElement));
				break;
			case "getDirInfo":
				response.getWriter().write(getDirInfo(jsonElement));
				break;
			case "sysCopy":
				response.getWriter().write(sysCopy(jsonElement));
				break;
			case "sysInfo_Close":
				response.getWriter().write(sysInfo_Close(jsonElement));
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
	* String SysCd,String SvrCd
	*/ 
	private String getSvrInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Copy cmm0200_copy = new Cmm0200_Copy();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrCd"));
		    return ParsingCommon.toJson(cmm0200_copy.getSvrInfo(SysCd,SvrCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_copy = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getProgInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Copy cmm0200_copy = new Cmm0200_Copy();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200_copy.getProgInfo(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_copy = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getDirInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Copy cmm0200_copy = new Cmm0200_Copy();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200_copy.getDirInfo(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_copy = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> svrList,ArrayList<HashMap<String,String>> dirList
	*/ 
	private String sysCopy(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Copy cmm0200_copy = new Cmm0200_Copy();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> svrList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "svrList"));
	   	    ArrayList<HashMap<String,String>> dirList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "dirList"));
		    return ParsingCommon.toJson(cmm0200_copy.sysCopy(etcData,svrList,dirList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_copy = null;
		} 
	}
	/* Parameter 
	* String SysCd,String UserId
	*/ 
	private String sysInfo_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Copy cmm0200_copy = new Cmm0200_Copy();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm0200_copy.sysInfo_Close(SysCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_copy = null;
		} 
	}
}
