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

import app.eCmr.Cmr3200;

@WebServlet("/webPage/ecmr/Cmr3200Servlet")
public class Cmr3200Servlet extends HttpServlet {

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
			case "get_SelectDashBoard":
				response.getWriter().write(get_SelectDashBoard(jsonElement));
				break;
			case "reqCncl":
				response.getWriter().write(reqCncl(jsonElement));
				break;
			case "cnclYn":
				response.getWriter().write(cnclYn(jsonElement));
				break;
			case "reqDelete":
				response.getWriter().write(reqDelete(jsonElement));
				break;
			case "get_SelectList2":
				response.getWriter().write(get_SelectList2(jsonElement));
				break;
			case "getAsta":
				response.getWriter().write(getAsta(jsonElement));
				break;
			case "getAsta2":
				response.getWriter().write(getAsta2(jsonElement));
				break;
			case "Cmr1015Insert":
				response.getWriter().write(Cmr1015Insert(jsonElement));
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
	* String pSysCd,String pReqCd,String pTeamCd,String pStateCd,String pReqUser,String pOrderId,String pStartDt,String pEndDt,String pUserId,String emgSw
	*/ 
	private String get_SelectList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String pSysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pSysCd"));
		    String pReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqCd"));
		    String pTeamCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pTeamCd"));
		    String pStateCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStateCd"));
		    String pReqUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqUser"));
		    String pOrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pOrderId"));
		    String pStartDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStartDt"));
		    String pEndDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEndDt"));
		    String pUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUserId"));
		    String emgSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "emgSw"));
		    return ParsingCommon.toJson(cmr3200.get_SelectList(pSysCd,pReqCd,pTeamCd,pStateCd,pReqUser,pOrderId,pStartDt,pEndDt,pUserId,emgSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String Cnt
	*/ 
	private String get_SelectDashBoard(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String Cnt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cnt"));
		    return ParsingCommon.toJson(cmr3200.get_SelectDashBoard(UserId,Cnt));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg,String ConfUsr
	*/ 
	private String reqCncl(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String ConfUsr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ConfUsr"));
		    return ParsingCommon.toJson(cmr3200.reqCncl(AcptNo,UserId,conMsg,ConfUsr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String cnclYn(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr3200.cnclYn(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String QryCd,String ConfUsr
	*/ 
	private String reqDelete(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    String ConfUsr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ConfUsr"));
		    return ParsingCommon.toJson(cmr3200.reqDelete(AcptNo,UserId,QryCd,ConfUsr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String pSysCd,String pReqCd,String pTeamCd,String pReqUser,String pUserId,String pChk,String pStD,String pEdD
	*/ 
	private String get_SelectList2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String pSysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pSysCd"));
		    String pReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqCd"));
		    String pTeamCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pTeamCd"));
		    String pReqUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqUser"));
		    String pUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUserId"));
		    String pChk = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pChk"));
		    String pStD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStD"));
		    String pEdD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEdD"));
		    return ParsingCommon.toJson(cmr3200.get_SelectList2(pSysCd,pReqCd,pTeamCd,pReqUser,pUserId,pChk,pStD,pEdD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String Acptno,String userid,String gubun
	*/ 
	private String getAsta(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    String gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gubun"));
		    return ParsingCommon.toJson(cmr3200.getAsta(Acptno,userid,gubun));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* String Acptno,String userid
	*/ 
	private String getAsta2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    return ParsingCommon.toJson(cmr3200.getAsta2(Acptno,userid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> etcData,String chkflag,String userid
	*/ 
	private String Cmr1015Insert(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3200 cmr3200 = new Cmr3200();
		try {
	   	    ArrayList<HashMap<String,String>> etcData = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    String chkflag = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkflag"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    return ParsingCommon.toJson(cmr3200.Cmr1015Insert(etcData,chkflag,userid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3200 = null;
		} 
	}
}
