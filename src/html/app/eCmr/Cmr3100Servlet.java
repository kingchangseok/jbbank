package html.app.eCmr;
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

import app.eCmr.Cmr3100;

@WebServlet("/webPage/ecmr/Cmr3100Servlet")
public class Cmr3100Servlet extends HttpServlet {

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
			case "get_SelectList":
				response.getWriter().write(get_SelectList(jsonElement));
				break;
			case "gyulChk":
				response.getWriter().write(gyulChk(jsonElement));
				break;
			case "gyulChk_bujang":
				response.getWriter().write(gyulChk_bujang(jsonElement));
				break;
			case "nextConf2":
				response.getWriter().write(nextConf2(jsonElement));
				break;
			case "packageConf":
				response.getWriter().write(packageConf(jsonElement));
				break;
			case "bujangConf":
				response.getWriter().write(bujangConf(jsonElement));
				break;
			case "nextConf":
				response.getWriter().write(nextConf(jsonElement));
				break;
			case "nextConf_ISR":
				response.getWriter().write(nextConf_ISR(jsonElement));
				break;
			case "nextConf3":
				response.getWriter().write(nextConf3(jsonElement));
				break;
			case "fortifyUpdate":
				response.getWriter().write(fortifyUpdate(jsonElement));
				break;
			case "ecams_verfile" : 
				response.getWriter().write(ecams_verfile(jsonElement));
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
	* String pReqCd,String pTeamCd,String pStateCd,String pReqUser,String pStartDt,String pEndDt,String pUserId,boolean pDeptManager
	*/ 
	private String get_SelectList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String pReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqCd"));
		    String pTeamCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pTeamCd"));
		    String pStateCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStateCd"));
		    String pReqUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqUser"));
		    String pStartDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStartDt"));
		    String pEndDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEndDt"));
		    String pUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUserId"));
		    boolean pDeptManager = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pDeptManager")));
		    return ParsingCommon.toJson(cmr3100.get_SelectList(pReqCd,pTeamCd,pStateCd,pReqUser,pStartDt,pEndDt,pUserId,pDeptManager));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String gyulChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr3100.gyulChk(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String gyulChk_bujang(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr3100.gyulChk_bujang(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg,String Cd,String ReqCd
	*/ 
	private String nextConf2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String Cd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr3100.nextConf2(AcptNo,UserId,conMsg,Cd,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> value,String UserId,String conMsg,String Cd
	*/ 
	private String packageConf(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
	   	    ArrayList<HashMap<String,String>> value = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "value"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String Cd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cd"));
		    return ParsingCommon.toJson(cmr3100.packageConf(value,UserId,conMsg,Cd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> value,String UserId,String conMsg,String Cd
	*/ 
	private String bujangConf(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
	   	    ArrayList<HashMap<String,String>> value = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "value"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String Cd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cd"));
		    return ParsingCommon.toJson(cmr3100.bujangConf(value,UserId,conMsg,Cd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg,String Cd,String ReqCd
	*/ 
	private String nextConf(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String Cd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr3100.nextConf(AcptNo,UserId,conMsg,Cd,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg,String Cd,String ReqCd
	*/ 
	private String nextConf_ISR(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String Cd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr3100.nextConf_ISR(AcptNo,UserId,conMsg,Cd,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg,String Cd,String ReqCd
	*/ 
	private String nextConf3(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String Cd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr3100.nextConf3(AcptNo,UserId,conMsg,Cd,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String resultMSG,String txtConMsg
	*/ 
	private String fortifyUpdate(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String resultMSG = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "resultMSG"));
		    String txtConMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtConMsg"));
		    return ParsingCommon.toJson(cmr3100.fortifyUpdate(AcptNo,resultMSG,txtConMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
	
	// 20230223
	private String ecams_verfile(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3100 cmr3100 = new Cmr3100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr3100.ecams_verfile(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3100 = null;
		} 
	}
}
