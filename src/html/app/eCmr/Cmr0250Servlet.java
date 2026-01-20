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

import app.eCmr.Cmr0250;

@WebServlet("/webPage/ecmr/Cmr0250Servlet")
public class Cmr0250Servlet extends HttpServlet {

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
			case "setQACheck":
				response.getWriter().write(setQACheck(jsonElement));
				break;
			case "getQACheckbeforeGyul":
				response.getWriter().write(getQACheckbeforeGyul(jsonElement));
				break;
			case "getQACheck":
				response.getWriter().write(getQACheck(jsonElement));
				break;
			case "getDevTime":
				response.getWriter().write(getDevTime(jsonElement));
				break;
			case "setDevTime":
				response.getWriter().write(setDevTime(jsonElement));
				break;
			case "getPoc":
				response.getWriter().write(getPoc(jsonElement));
				break;
			case "getPrjInfo":
				response.getWriter().write(getPrjInfo(jsonElement));
				break;
			case "getRstList":
				response.getWriter().write(getRstList(jsonElement));
				break;
			case "svrProc":
				response.getWriter().write(svrProc(jsonElement));
				break;
			case "delReq":
				response.getWriter().write(delReq(jsonElement));
				break;
			case "getPrcSys":
				response.getWriter().write(getPrcSys(jsonElement));
				break;
			case "updtSeq":
				response.getWriter().write(updtSeq(jsonElement));
				break;
			case "updtDeploy":
				response.getWriter().write(updtDeploy(jsonElement));
				break;
			case "updtDeploy_2":
				response.getWriter().write(updtDeploy_2(jsonElement));
				break;
			case "updtPrjInfo":
				response.getWriter().write(updtPrjInfo(jsonElement));
				break;
			case "updtTemp":
				response.getWriter().write(updtTemp(jsonElement));
				break;
			case "progCncl":
				response.getWriter().write(progCncl(jsonElement));
				break;
			case "progCncl_sel":
				response.getWriter().write(progCncl_sel(jsonElement));
				break;
			case "progCncl_Mapping":
				response.getWriter().write(progCncl_Mapping(jsonElement));
				break;
			case "getProgDetail":
				response.getWriter().write(getProgDetail(jsonElement));
				break;
			case "setProgDetail":
				response.getWriter().write(setProgDetail(jsonElement));
				break;
			case "getOrders":
				response.getWriter().write(getOrders(jsonElement));
				break;
			case "getOrders2":
				response.getWriter().write(getOrders2(jsonElement));
				break;
			case "getTeamCD":
				response.getWriter().write(getTeamCD(jsonElement));
				break;
			case "getReqs":
				response.getWriter().write(getReqs(jsonElement));
				break;
			case "getFiles":
				response.getWriter().write(getFiles(jsonElement));
				break;
			case "getStatus":
				response.getWriter().write(getStatus(jsonElement));
				break;
			case "getSrc":
				response.getWriter().write(getSrc(jsonElement));
				break;
			case "getTestData":
				response.getWriter().write(getTestData(jsonElement));
				break;
			case "setTestData":
				response.getWriter().write(setTestData(jsonElement));
				break;
			case "getBeforeFiles":
				response.getWriter().write(getBeforeFiles(jsonElement));
				break;
			case "getReqRunners":
				response.getWriter().write(getReqRunners(jsonElement));
				break;
			case "getFortify":
				response.getWriter().write(getFortify(jsonElement));
				break;
			case "getFortify_Result":
				response.getWriter().write(getFortify_Result(jsonElement));
				break;
			case "getSRReqList":
				response.getWriter().write(getSRReqList(jsonElement));
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
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getReqList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo,String chkYn,boolean qrySw
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String chkYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkYn"));
		    boolean qrySw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "qrySw")));
		    return ParsingCommon.toJson(cmr0250.getProgList(UserId,AcptNo,chkYn,qrySw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String Acptno,HashMap<String,String> etcData
	*/ 
	private String setQACheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0250.setQACheck(Acptno,etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String Acptno
	*/ 
	private String getQACheckbeforeGyul(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    return ParsingCommon.toJson(cmr0250.getQACheckbeforeGyul(Acptno));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String Acptno
	*/ 
	private String getQACheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    return ParsingCommon.toJson(cmr0250.getQACheck(Acptno));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String Acptno
	*/ 
	private String getDevTime(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    return ParsingCommon.toJson(cmr0250.getDevTime(Acptno));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String Acptno,String time
	*/ 
	private String setDevTime(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String Acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Acptno"));
		    String time = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "time"));
		    return ParsingCommon.toJson(cmr0250.setDevTime(Acptno,time));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String getPoc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0250.getPoc(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getPrjInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getPrjInfo(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo,String prcSys
	*/ 
	private String getRstList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String prcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcSys"));
		    return ParsingCommon.toJson(cmr0250.getRstList(UserId,AcptNo,prcSys));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String prcCd,String prcSys,String UserId
	*/ 
	private String svrProc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String prcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcCd"));
		    String prcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcSys"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0250.svrProc(AcptNo,prcCd,prcSys,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId,String SignTeam,String ReqCd
	*/ 
	private String delReq(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String SignTeam = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SignTeam"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr0250.delReq(AcptNo,ItemId,SignTeam,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getPrcSys(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getPrcSys(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,ArrayList<HashMap<String,String>> fileList
	*/ 
	private String updtSeq(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmr0250.updtSeq(AcptNo,fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ReqPass,String DeployDate,String PassCd
	*/ 
	private String updtDeploy(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ReqPass = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqPass"));
		    String DeployDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeployDate"));
		    String PassCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PassCd"));
		    return ParsingCommon.toJson(cmr0250.updtDeploy(AcptNo,ReqPass,DeployDate,PassCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String CD
	*/ 
	private String updtDeploy_2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String CD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CD"));
		    return ParsingCommon.toJson(cmr0250.updtDeploy_2(AcptNo,CD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String updtPrjInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0250.updtPrjInfo(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId
	*/ 
	private String updtTemp(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0250.updtTemp(AcptNo,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId,String PrcSys
	*/ 
	private String progCncl(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String PrcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrcSys"));
		    return ParsingCommon.toJson(cmr0250.progCncl(AcptNo,ItemId,PrcSys));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,ArrayList<HashMap<String,String>> fileList,String PrcSys
	*/ 
	private String progCncl_sel(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String PrcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrcSys"));
		    return ParsingCommon.toJson(cmr0250.progCncl_sel(AcptNo,fileList,PrcSys));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String progCncl_Mapping(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.progCncl_Mapping(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId
	*/ 
	private String getProgDetail(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0250.getProgDetail(AcptNo,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId,HashMap<String,String> etcData1,ArrayList<HashMap<String,String>> etcData2
	*/ 
	private String setProgDetail(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    HashMap<String,String> etcData1 = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData1"));
	   	    ArrayList<HashMap<String,String>> etcData2 = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "etcData2"));
		    return ParsingCommon.toJson(cmr0250.setProgDetail(AcptNo,ItemId,etcData1,etcData2));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemId
	*/ 
	private String getOrders(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0250.getOrders(AcptNo,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getOrders2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
			 String orderName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "orderName"));
		    return ParsingCommon.toJson(cmr0250.getOrders2(orderName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getTeamCD(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getTeamCD(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String OrderId
	*/ 
	private String getReqs(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    return ParsingCommon.toJson(cmr0250.getReqs(OrderId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String Id
	*/ 
	private String getFiles(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String Id = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Id"));
		    return ParsingCommon.toJson(cmr0250.getFiles(Id));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getStatus(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getStatus(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemAll
	*/ 
	private String getSrc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemAll = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemAll"));
		    return ParsingCommon.toJson(cmr0250.getSrc(AcptNo,ItemAll));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getTestData(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getTestData(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,HashMap<String,String> Data
	*/ 
	private String setTestData(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    HashMap<String,String> Data = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "Data"));
		    return ParsingCommon.toJson(cmr0250.setTestData(AcptNo,Data));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String UserId,String StDate,String EdDate
	*/ 
	private String getBeforeFiles(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    return ParsingCommon.toJson(cmr0250.getBeforeFiles(UserId,StDate,EdDate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getReqRunners(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getReqRunners(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getFortify(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0250.getFortify(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String ItemID
	*/ 
	private String getFortify_Result(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String ItemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID"));
		    return ParsingCommon.toJson(cmr0250.getFortify_Result(AcptNo,ItemID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String>
	*/ 
	private String getSRReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0250 cmr0250 = new Cmr0250();
		try {
		    HashMap<String,String> srData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "srData"));
		    return ParsingCommon.toJson(cmr0250.getSRReqList(srData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0250 = null;
		} 
	}
}
