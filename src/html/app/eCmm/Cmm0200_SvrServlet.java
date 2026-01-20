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

import app.eCmm.Cmm0200_Svr;

@WebServlet("/webPage/ecmm/Cmm0200_SvrServlet")
public class Cmm0200_SvrServlet extends HttpServlet {

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
			case "getSvrList":
				response.getWriter().write(getSvrList(jsonElement));
				break;
			case "getSecuList":
				response.getWriter().write(getSecuList(jsonElement));
				break;
			case "svrInfo_Ins":
				response.getWriter().write(svrInfo_Ins(jsonElement));
				break;
			case "svrInfo_Ins2":
				response.getWriter().write(svrInfo_Ins2(jsonElement));
				break;
			case "svrInfo_Close":
				response.getWriter().write(svrInfo_Close(jsonElement));
				break;
			case "secuInfo_Ins":
				response.getWriter().write(secuInfo_Ins(jsonElement));
				break;
			case "secuInfo_Close":
				response.getWriter().write(secuInfo_Close(jsonElement));
				break;
			case "svrInfo_Ins_copy":
				response.getWriter().write(svrInfo_Ins_copy(jsonElement));
				break;
			case "svrInfo_Ins_updt":
				response.getWriter().write(svrInfo_Ins_updt(jsonElement));
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
	* String SysCd,String SvrInfo
	*/ 
	private String getSvrList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SvrInfo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrInfo"));
		    return ParsingCommon.toJson(cmm0200_svr.getSvrList(SysCd,SvrInfo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* String SysCd,String sysInfo
	*/ 
	private String getSecuList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String sysInfo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysInfo"));
		    return ParsingCommon.toJson(cmm0200_svr.getSecuList(SysCd,sysInfo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String svrInfo_Ins(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0200_svr.svrInfo_Ins(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String svrInfo_Ins2(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0200_svr.svrInfo_Ins2(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* String SysCd,String UserId,String SvrCd,String SeqNo
	*/ 
	private String svrInfo_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SvrCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SvrCd"));
		    String SeqNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SeqNo"));
		    return ParsingCommon.toJson(cmm0200_svr.svrInfo_Close(SysCd,UserId,SvrCd,SeqNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> svrList
	*/ 
	private String secuInfo_Ins(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> svrList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "svrList"));
		    return ParsingCommon.toJson(cmm0200_svr.secuInfo_Ins(etcData,svrList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* String SysCd,String JobCd,ArrayList<HashMap<String,String>> svrList
	*/ 
	private String secuInfo_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
	   	    ArrayList<HashMap<String,String>> svrList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "svrList"));
		    return ParsingCommon.toJson(cmm0200_svr.secuInfo_Close(SysCd,JobCd,svrList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String svrInfo_Ins_copy(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0200_svr.svrInfo_Ins_copy(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String svrInfo_Ins_updt(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Svr cmm0200_svr = new Cmm0200_Svr();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0200_svr.svrInfo_Ins_updt(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_svr = null;
		} 
	}
}
