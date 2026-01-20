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

import app.eCmr.Cmr0300;

@WebServlet("/webPage/ecmr/Cmr0300Servlet")
public class Cmr0300Servlet extends HttpServlet {

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
			case "getDocList":
				response.getWriter().write(getDocList(jsonElement));
				break;
			case "getDocListCond":
				response.getWriter().write(getDocListCond(jsonElement));
				break;
			case "fileDownChk":
				response.getWriter().write(fileDownChk(jsonElement));
				break;
			case "gyulCheck":
				response.getWriter().write(gyulCheck(jsonElement));
				break;
			case "acptNoChk":
				response.getWriter().write(acptNoChk(jsonElement));
				break;
			case "request_Check_Out":
				response.getWriter().write(request_Check_Out(jsonElement));
				break;
			case "confSelect":
				response.getWriter().write(confSelect(jsonElement));
				break;
			case "getDocFileList":
				response.getWriter().write(getDocFileList(jsonElement));
				break;
			case "getTmpDirConf":
				response.getWriter().write(getTmpDirConf(jsonElement));
				break;
			case "setTranResult":
				response.getWriter().write(setTranResult(jsonElement));
				break;
			case "callCmr9900_Str":
				response.getWriter().write(callCmr9900_Str(jsonElement));
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
	* String prjNo,String docSeq,String UserId,String ChildYn
	*/ 
	private String getDocList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String prjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prjNo"));
		    String docSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "docSeq"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ChildYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ChildYn"));
		    return ParsingCommon.toJson(cmr0300.getDocList(prjNo,docSeq,UserId,ChildYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String prjNo,String gbnCD,String UserId,String keyStr
	*/ 
	private String getDocListCond(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String prjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prjNo"));
		    String gbnCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gbnCD"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String keyStr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "keyStr"));
		    return ParsingCommon.toJson(cmr0300.getDocListCond(prjNo,gbnCD,UserId,keyStr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String fileDownChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0300.fileDownChk(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String syscd,String reqcd,String ccbyn,String UserID
	*/ 
	private String gyulCheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    String ccbyn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ccbyn"));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    return ParsingCommon.toJson(cmr0300.gyulCheck(syscd,reqcd,ccbyn,UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String acptNo
	*/ 
	private String acptNoChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    return ParsingCommon.toJson(cmr0300.acptNoChk(acptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkOutList,HashMap<String,String> etcData,ArrayList<HashMap<String,Object>>gyulData,HashMap<String,String> ccbynData
	*/ 
	private String request_Check_Out(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
	   	    ArrayList<HashMap<String,String>> chkOutList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkOutList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    ArrayList<HashMap<String,Object>> gyulData = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "gyulData"));
		    HashMap<String,String> ccbynData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "ccbynData"));
		    return ParsingCommon.toJson(cmr0300.request_Check_Out(chkOutList,etcData,gyulData,ccbynData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String ReqCd,String RsrcCd,String UserId
	*/ 
	private String confSelect(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0300.confSelect(SysCd,ReqCd,RsrcCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getDocFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0300.getDocFileList(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String pCode
	*/ 
	private String getTmpDirConf(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String pCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pCode"));
		    return ParsingCommon.toJson(cmr0300.getTmpDirConf(pCode));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String acptNo,String serNo,String ret
	*/ 
	private String setTranResult(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    String serNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "serNo"));
		    String ret = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ret"));
		    return ParsingCommon.toJson(cmr0300.setTranResult(acptNo,serNo,ret));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
	/* Parameter 
	* String acptNo
	*/ 
	private String callCmr9900_Str(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0300 cmr0300 = new Cmr0300();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    return ParsingCommon.toJson(cmr0300.callCmr9900_Str(acptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0300 = null;
		} 
	}
}
