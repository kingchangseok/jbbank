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

import app.eCmr.Cmr0150;

@WebServlet("/webPage/ecmr/Cmr0150Servlet")
public class Cmr0150Servlet extends HttpServlet {

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
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
				break;
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "svrProc":
				response.getWriter().write(svrProc(jsonElement));
				break;
			case "confLocat":
				response.getWriter().write(confLocat(jsonElement));
				break;
			case "updtYn":
				response.getWriter().write(updtYn(jsonElement));
				break;
			case "updtEditor":
				response.getWriter().write(updtEditor(jsonElement));
				break;
			case "delReq":
				response.getWriter().write(delReq(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "getOrderList":
				response.getWriter().write(getOrderList(jsonElement));
				break;
			case "setProgDetail":
				response.getWriter().write(setProgDetail(jsonElement));
				break;
			case "getProgDetail":
				response.getWriter().write(getProgDetail(jsonElement));
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
	* String UserId,String AcptNo
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0150.getReqList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo,String chkYn
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String chkYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkYn"));
		    return ParsingCommon.toJson(cmr0150.getProgList(UserId,AcptNo,chkYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String prcCd,String prcSys
	*/ 
	private String svrProc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String prcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcCd"));
		    String prcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcSys"));
		    return ParsingCommon.toJson(cmr0150.svrProc(AcptNo,prcCd,prcSys));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String PrcSw,String CnclStep,String CnclDat,String RetryYn
	*/ 
	private String confLocat(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String PrcSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrcSw"));
		    String CnclStep = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CnclStep"));
		    String CnclDat = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CnclDat"));
		    String RetryYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RetryYn"));
		    return ParsingCommon.toJson(cmr0150.confLocat(AcptNo,PrcSw,CnclStep,CnclDat,RetryYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String updtYn(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0150.updtYn(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String editUser,String UserId
	*/ 
	private String updtEditor(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String editUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "editUser"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0150.updtEditor(AcptNo,editUser,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId,String SignTeam,String ReqCd
	*/ 
	private String delReq(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String SignTeam = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SignTeam"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr0150.delReq(AcptNo,ItemId,SignTeam,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String GbnCd
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    return ParsingCommon.toJson(cmr0150.getFileList(AcptNo,GbnCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId
	*/ 
	private String getOrderList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0150.getOrderList(AcptNo,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId,HashMap<String,String> etcData,ArrayList<HashMap<String,String>> etcData2
	*/ 
	private String setProgDetail(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> etcData2 = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "etcData2"));
		    return ParsingCommon.toJson(cmr0150.setProgDetail(AcptNo,ItemId,etcData,etcData2));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId
	*/ 
	private String getProgDetail(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0150 cmr0150 = new Cmr0150();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0150.getProgDetail(AcptNo,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0150 = null;
		} 
	}
}
