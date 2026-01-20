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

import app.eCmd.Cmd1200;

@WebServlet("/webPage/ecmd/Cmd1200Servlet")
public class Cmd1200Servlet extends HttpServlet {

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
			case "getBldCd":
				response.getWriter().write(getBldCd(jsonElement));
				break;
			case "getScript":
				response.getWriter().write(getScript(jsonElement));
				break;
			case "getQryCd":
				response.getWriter().write(getQryCd(jsonElement));
				break;
			case "getBldList":
				response.getWriter().write(getBldList(jsonElement));
				break;
			case "getSql_Qry_Sub_Tab2":
				response.getWriter().write(getSql_Qry_Sub_Tab2(jsonElement));
				break;
			case "getSql_Qry":
				response.getWriter().write(getSql_Qry(jsonElement));
				break;
			case "getCmm0022_Del":
				response.getWriter().write(getCmm0022_Del(jsonElement));
				break;
			case "getCmm0022_Copy":
				response.getWriter().write(getCmm0022_Copy(jsonElement));
				break;
			case "getCmm0022_DBProc":
				response.getWriter().write(getCmm0022_DBProc(jsonElement));
				break;
			case "getCmm0033_DBProc":
				response.getWriter().write(getCmm0033_DBProc(jsonElement));
				break;
			case "getCmm0033_Del":
				response.getWriter().write(getCmm0033_Del(jsonElement));
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
	* 
	*/ 
	private String getBldCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    return ParsingCommon.toJson(cmd1200.getBldCd());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String Cbo_BldGbn_code,String Cbo_BldCd0_code
	*/ 
	private String getScript(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String Cbo_BldGbn_code = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldGbn_code"));
		    String Cbo_BldCd0_code = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldCd0_code"));
		    return ParsingCommon.toJson(cmd1200.getScript(Cbo_BldGbn_code,Cbo_BldCd0_code));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String TstSw
	*/ 
	private String getQryCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String TstSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TstSw"));
		    return ParsingCommon.toJson(cmd1200.getQryCd(SysCd,TstSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String TstSw,String QryCd,String SysInfo
	*/ 
	private String getBldList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String TstSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TstSw"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    String SysInfo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysInfo"));
		    return ParsingCommon.toJson(cmd1200.getBldList(SysCd,TstSw,QryCd,SysInfo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String Cbo_BldCd_code,String index
	*/ 
	private String getSql_Qry_Sub_Tab2(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String Cbo_BldCd_code = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldCd_code"));
		    String index = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "index"));
		    return ParsingCommon.toJson(cmd1200.getSql_Qry_Sub_Tab2(Cbo_BldCd_code,index));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String Cbo_BldGbn
	*/ 
	private String getSql_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String Cbo_BldGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldGbn"));
		    return ParsingCommon.toJson(cmd1200.getSql_Qry(Cbo_BldGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String Cbo_BldGbn,String Cbo_BldCd0
	*/ 
	private String getCmm0022_Del(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String Cbo_BldGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldGbn"));
		    String Cbo_BldCd0 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldCd0"));
		    return ParsingCommon.toJson(cmd1200.getCmm0022_Del(Cbo_BldGbn,Cbo_BldCd0));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String Cbo_BldGbn,String Cbo_BldCd0,String NewBld
	*/ 
	private String getCmm0022_Copy(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String Cbo_BldGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldGbn"));
		    String Cbo_BldCd0 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldCd0"));
		    String NewBld = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "NewBld"));
		    String NewBldMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "NewBldMsg"));
		    return ParsingCommon.toJson(cmd1200.getCmm0022_Copy(Cbo_BldGbn,Cbo_BldCd0,NewBld,NewBldMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* String Cbo_BldGbn,String Cbo_BldCd0,String codename,String Txt_Comp2,String runType,ArrayList<HashMap<String,String>> Lv_File0_dp
	*/ 
	private String getCmm0022_DBProc(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    String Cbo_BldGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldGbn"));
		    String Cbo_BldCd0 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_BldCd0"));
		    String codename = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "codename"));
		    String Txt_Comp2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_Comp2"));
		    String runType = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "runType"));
	   	    ArrayList<HashMap<String,String>> Lv_File0_dp = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "Lv_File0_dp"));
		    return ParsingCommon.toJson(cmd1200.getCmm0022_DBProc(Cbo_BldGbn,Cbo_BldCd0,codename,Txt_Comp2,runType,Lv_File0_dp));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getCmm0033_DBProc(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmd1200.getCmm0033_DBProc(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> delList
	*/ 
	private String getCmm0033_Del(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1200 cmd1200 = new Cmd1200();
		try {
	   	    ArrayList<HashMap<String,String>> delList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "delList"));
		    return ParsingCommon.toJson(cmd1200.getCmm0033_Del(delList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1200 = null;
		} 
	}
}
