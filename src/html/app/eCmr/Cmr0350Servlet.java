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

import app.eCmr.Cmr0350;

@WebServlet("/webPage/ecmr/Cmr0350Servlet")
public class Cmr0350Servlet extends HttpServlet {

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
			case "getAcptinfo":
				response.getWriter().write(getAcptinfo(jsonElement));
				break;
			case "getRstList":
				response.getWriter().write(getRstList(jsonElement));
				break;
			case "delReq":
				response.getWriter().write(delReq(jsonElement));
				break;
			case "getGridLst1":
				response.getWriter().write(getGridLst1(jsonElement));
				break;
			case "get_CCBSet":
				response.getWriter().write(get_CCBSet(jsonElement));
				break;
			case "get_CmdCncl":
				response.getWriter().write(get_CmdCncl(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "sysGyulCheck":
				response.getWriter().write(sysGyulCheck(jsonElement));
				break;
			case "clearErrFlag":
				response.getWriter().write(clearErrFlag(jsonElement));
				break;
			case "updtYn":
				response.getWriter().write(updtYn(jsonElement));
				break;
			case "updtEditor":
				response.getWriter().write(updtEditor(jsonElement));
				break;
			case "svrProc":
				response.getWriter().write(svrProc(jsonElement));
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
	* String strAcptno,String UserId
	*/ 
	private String getAcptinfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String strAcptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strAcptno"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0350.getAcptinfo(strAcptno,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo
	*/ 
	private String getRstList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0350.getRstList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo,String serno,String DocId,String QryCd,String ReqCd,String ConfUsr
	*/ 
	private String delReq(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String serno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "serno"));
		    String DocId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocId"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String ConfUsr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ConfUsr"));
		    return ParsingCommon.toJson(cmr0350.delReq(UserId,AcptNo,serno,DocId,QryCd,ReqCd,ConfUsr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String strAcptno
	*/ 
	private String getGridLst1(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String strAcptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strAcptno"));
		    return ParsingCommon.toJson(cmr0350.getGridLst1(strAcptno));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String strAcptno
	*/ 
	private String get_CCBSet(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String strAcptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strAcptno"));
		    return ParsingCommon.toJson(cmr0350.get_CCBSet(strAcptno));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String UserId,String strAcptno,ArrayList<HashMap<String,String>> fileList,String ConfUsr
	*/ 
	private String get_CmdCncl(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String strAcptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strAcptno"));
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String ConfUsr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ConfUsr"));
		    return ParsingCommon.toJson(cmr0350.get_CmdCncl(UserId,strAcptno,fileList,ConfUsr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String GbnCd
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    return ParsingCommon.toJson(cmr0350.getFileList(AcptNo,GbnCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String sysGyulCheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0350.sysGyulCheck(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String clearErrFlag(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0350.clearErrFlag(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String updtYn(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0350.updtYn(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String editUser,String UserId
	*/ 
	private String updtEditor(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String editUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "editUser"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0350.updtEditor(AcptNo,editUser,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String prcSys,String UserId
	*/ 
	private String svrProc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0350 cmr0350 = new Cmr0350();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String prcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcSys"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0350.svrProc(AcptNo,prcSys,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0350 = null;
		} 
	}
}
