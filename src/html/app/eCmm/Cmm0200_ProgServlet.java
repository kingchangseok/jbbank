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

import app.eCmm.Cmm0200_Prog;

@WebServlet("/webPage/ecmm/Cmm0200_ProgServlet")
public class Cmm0200_ProgServlet extends HttpServlet {

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
			case "codeInfo":
				response.getWriter().write(codeInfo(jsonElement));
				break;
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "getSameList":
				response.getWriter().write(getSameList(jsonElement));
				break;
			case "rsrcInfo_Ins":
				response.getWriter().write(rsrcInfo_Ins(jsonElement));
				break;
			case "rangInfo_ins":
				response.getWriter().write(rangInfo_ins(jsonElement));
				break;
			case "rsrcInfo_Close":
				response.getWriter().write(rsrcInfo_Close(jsonElement));
				break;
			case "getLangcd":
				response.getWriter().write(getLangcd(jsonElement));
				break;
			case "rsrcInfo_seq":
				response.getWriter().write(rsrcInfo_seq(jsonElement));
				break;
			case "exeInfo":
				response.getWriter().write(exeInfo(jsonElement));
				break;
			case "getRsrcInfo":
				response.getWriter().write(getRsrcInfo(jsonElement));
				break;
			case "getLangInfo":
				response.getWriter().write(getLangInfo(jsonElement));
				break;
			case "getChkLangInfo":
				response.getWriter().write(getChkLangInfo(jsonElement));
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
	* String MACODE
	*/ 
	private String codeInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String MACODE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MACODE"));
		    return ParsingCommon.toJson(cmm0200_prog.codeInfo(MACODE));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200_prog.getProgList(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getSameList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0200_prog.getSameList(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> sameList,ArrayList<HashMap<String,String>> langInfoList
	*/ 
	private String rsrcInfo_Ins(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> sameList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "sameList"));
	   	    ArrayList<HashMap<String,String>> langInfoList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "langInfoList"));
		    return ParsingCommon.toJson(cmm0200_prog.rsrcInfo_Ins(etcData,sameList,langInfoList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String syscd,ArrayList<HashMap<String,String>> jawonInfo,ArrayList<HashMap<String,String>> langInfo
	*/ 
	private String rangInfo_ins(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
	   	    ArrayList<HashMap<String,String>> jawonInfo = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "jawonInfo"));
	   	    ArrayList<HashMap<String,String>> langInfo = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "langInfo"));
		    return ParsingCommon.toJson(cmm0200_prog.rangInfo_ins(syscd,jawonInfo,langInfo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String SysCd,String UserId,String RsrcCd
	*/ 
	private String rsrcInfo_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    return ParsingCommon.toJson(cmm0200_prog.rsrcInfo_Close(SysCd,UserId,RsrcCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String syscd,String rsrccd
	*/ 
	private String getLangcd(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String rsrccd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rsrccd"));
		    return ParsingCommon.toJson(cmm0200_prog.getLangcd(syscd,rsrccd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String SysCd,String SecuCd
	*/ 
	private String rsrcInfo_seq(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SecuCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuCd"));
		    return ParsingCommon.toJson(cmm0200_prog.rsrcInfo_seq(SysCd,SecuCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String exeInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    return ParsingCommon.toJson(cmm0200_prog.exeInfo());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String MACODE,int sortCd,String useyn
	*/ 
	private String getRsrcInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String MACODE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MACODE"));
		    int sortCd = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sortCd")));
		    String useyn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "useyn"));
		    return ParsingCommon.toJson(cmm0200_prog.getRsrcInfo(MACODE,sortCd,useyn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String syscd,String rsrccd
	*/ 
	private String getLangInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String rsrccd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rsrccd"));
		    return ParsingCommon.toJson(cmm0200_prog.getLangInfo(syscd,rsrccd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
	/* Parameter 
	* String syscd
	*/ 
	private String getChkLangInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0200_Prog cmm0200_prog = new Cmm0200_Prog();
		try {
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    return ParsingCommon.toJson(cmm0200_prog.getChkLangInfo(syscd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0200_prog = null;
		} 
	}
}
