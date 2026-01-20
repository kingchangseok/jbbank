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

import app.eCmr.Cmr0200;

@WebServlet("/webPage/ecmr/Cmr0200Servlet")
public class Cmr0200Servlet extends HttpServlet {

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
			case "getFileListIn_excel":
				response.getWriter().write(getFileListIn_excel(jsonElement));
				break;
			case "getFileList_excel":
				response.getWriter().write(getFileList_excel(jsonElement));
				break;
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
				break;
			case "pgmCheck":
				response.getWriter().write(pgmCheck(jsonElement));
				break;
			case "moduleChk":
				response.getWriter().write(moduleChk(jsonElement));
				break;
			case "moduleChk_new":
				response.getWriter().write(moduleChk_new(jsonElement));
				break;
			case "cmr0020_Insert":
				response.getWriter().write(cmr0020_Insert(jsonElement));
				break;
			case "cmr0020_Delete":
				response.getWriter().write(cmr0020_Delete(jsonElement));
				break;
			case "checkTestCase":
				response.getWriter().write(checkTestCase(jsonElement));
				break;
			case "checkReqYN":
				response.getWriter().write(checkReqYN(jsonElement));
				break;
			case "setJobPlanInfo":
				response.getWriter().write(setJobPlanInfo(jsonElement));
				break;
			case "getJobPlanInfo":
				response.getWriter().write(getJobPlanInfo(jsonElement));
				break;
			case "fileOpenChk":
				response.getWriter().write(fileOpenChk(jsonElement));
				break;
			case "getProgOrders":
				response.getWriter().write(getProgOrders(jsonElement));
				break;
			case "setPlanConfirm":
				response.getWriter().write(setPlanConfirm(jsonElement));
				break;
			case "getComProgLst":
				response.getWriter().write(getComProgLst(jsonElement));
				break;
			case "getprivProgLst":
				response.getWriter().write(getprivProgLst(jsonElement));
				break;
			case "getProgSubStat":
				response.getWriter().write(getProgSubStat(jsonElement));
				break;
			case "getProgListInfo":
				response.getWriter().write(getProgListInfo(jsonElement));
				break;
			case "chkRechkIn":
				response.getWriter().write(chkRechkIn(jsonElement));
				break;
			case "setConfList":
				response.getWriter().write(setConfList(jsonElement));
				break;
			case "request_homep_Check_In":
				response.getWriter().write(request_homep_Check_In(jsonElement));
				break;
			case "copyDoc2":
				response.getWriter().write(copyDoc2(jsonElement));
				break;
			case "copyDoc":
				response.getWriter().write(copyDoc(jsonElement));
				break;
			case "filechk":
				response.getWriter().write(filechk(jsonElement));
				break;
			case "getRsaCheck_Dev":
				response.getWriter().write(getRsaCheck_Dev(jsonElement));
				break;
			case "request_Check_In":
				response.getWriter().write(request_Check_In(jsonElement));
				break;
			case "confSelect":
				response.getWriter().write(confSelect(jsonElement));
				break;
			case "getReturnInfo":
				response.getWriter().write(getReturnInfo(jsonElement));
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
	* ArrayList<HashMap<String,String>> fileList,HashMap<String,String> dataObj
	*/ 
	private String getFileListIn_excel(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmr0200.getFileListIn_excel(fileList,dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,String UserId,String SysCd,String SinCd,String TstSw,String SysInfo,String selectISRID
	*/ 
	private String getFileList_excel(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
	   	    HashMap<String,String> paramMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "paramMap"));
		    return ParsingCommon.toJson(cmr0200.getFileList_excel(fileList,paramMap));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> paramMap
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    HashMap<String,String> paramMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "paramMap"));
		    return ParsingCommon.toJson(cmr0200.getReqList(paramMap));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String LangCd,String ProgTit,String DirPath
	*/ 
	private String pgmCheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String LangCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LangCd"));
		    String ProgTit = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgTit"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    return ParsingCommon.toJson(cmr0200.pgmCheck(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd,ProgTit,DirPath));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String LangCd,String ProgTit,String DirPath,String BaseItem
	*/ 
	private String moduleChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String LangCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LangCd"));
		    String ProgTit = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgTit"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    String BaseItem = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseItem"));
		    return ParsingCommon.toJson(cmr0200.moduleChk(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd,ProgTit,DirPath,BaseItem));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList
	*/ 
	private String moduleChk_new(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmr0200.moduleChk_new(fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String LangCd,String ProgTit,String BaseItem
	*/ 
	private String cmr0020_Insert(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String LangCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LangCd"));
		    String ProgTit = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgTit"));
		    String BaseItem = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseItem"));
		    return ParsingCommon.toJson(cmr0200.cmr0020_Insert(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd,ProgTit,BaseItem));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String ItemId
	*/ 
	private String cmr0020_Delete(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0200.cmr0020_Delete(UserId,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String ItemId
	*/ 
	private String checkTestCase(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0200.checkTestCase(UserId,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String selectORDERID,String SysCd,ArrayList<HashMap<String,String>> chkInList,String UserId,String strReqCD
	*/ 
	private String checkReqYN(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String selectORDERID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selectORDERID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String strReqCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strReqCD"));
		    return ParsingCommon.toJson(cmr0200.checkReqYN(selectORDERID,SysCd,chkInList,UserId,strReqCD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setJobPlanInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0200.setJobPlanInfo(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getJobPlanInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0200.getJobPlanInfo(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<String> fileList,HashMap<String,String> baseFile
	*/ 
	private String fileOpenChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    ArrayList<String> fileList = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    HashMap<String,String> baseFile = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "baseFile"));
		    return ParsingCommon.toJson(cmr0200.fileOpenChk(UserId,fileList,baseFile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getProgOrders(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0200.getProgOrders(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,Object>>ConfList
	*/ 
	private String setPlanConfirm(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    return ParsingCommon.toJson(cmr0200.setPlanConfirm(etcData,ConfList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getComProgLst(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    return ParsingCommon.toJson(cmr0200.getComProgLst());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String acptNO
	*/ 
	private String getprivProgLst(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String acptNO = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNO"));
		    return ParsingCommon.toJson(cmr0200.getprivProgLst(acptNO));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String getProgSubStat(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0200.getProgSubStat(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getProgListInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0200.getProgListInfo(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String chkRechkIn(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0200.chkRechkIn(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,ArrayList<HashMap<String,String>> ConfList
	*/ 
	private String setConfList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
	   	    ArrayList<HashMap<String,String>> ConfList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    return ParsingCommon.toJson(cmr0200.setConfList(AcptNo,ConfList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData,ArrayList<HashMap<String,String>> befJob,ArrayList<HashMap<String,Object>>ConfList,ArrayList<HashMap<String,String>> TestList,ArrayList<HashMap<String,String>> OrderList,String confFg
	*/ 
	private String request_homep_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> befJob = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "befJob"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
	   	    ArrayList<HashMap<String,String>> TestList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "TestList"));
	   	    ArrayList<HashMap<String,String>> OrderList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "OrderList"));
		    String confFg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "confFg"));
		    return ParsingCommon.toJson(cmr0200.request_homep_Check_In(chkInList,etcData,befJob,ConfList,TestList,OrderList,confFg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,ArrayList<HashMap<String,String>> FileList
	*/ 
	private String copyDoc2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
	   	    ArrayList<HashMap<String,String>> FileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "FileList"));
		    return ParsingCommon.toJson(cmr0200.copyDoc2(AcptNo,FileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String befAcptNo,ArrayList<HashMap<String,String>> FileList
	*/ 
	private String copyDoc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String befAcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "befAcptNo"));
	   	    ArrayList<HashMap<String,String>> FileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "FileList"));
		    return ParsingCommon.toJson(cmr0200.copyDoc(AcptNo,befAcptNo,FileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList
	*/ 
	private String filechk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmr0200.filechk(fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> rsaList
	*/ 
	private String getRsaCheck_Dev(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> rsaList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "rsaList"));
		    return ParsingCommon.toJson(cmr0200.getRsaCheck_Dev(rsaList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData,ArrayList<HashMap<String,String>> befJob,ArrayList<HashMap<String,Object>> ConfList,ArrayList<HashMap<String,String>> TestList,ArrayList<HashMap<String,String>> OrderList,String confFg
	*/ 
	private String request_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
	   	    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> befJob = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "befJob"));
	   	    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		   	ArrayList<HashMap<String,String>> TestList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "TestList"));
		   	ArrayList<HashMap<String,String>> OrderList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "OrderList"));
		   	String confFg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "confFg"));
		    return ParsingCommon.toJson(cmr0200.request_Check_In(chkInList,etcData,befJob,ConfList,TestList,OrderList,confFg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String ReqCd,String RsrcCd,String UserId,String QryCd
	*/ 
	private String confSelect(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    return ParsingCommon.toJson(cmr0200.confSelect(SysCd,ReqCd,RsrcCd,UserId,QryCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo
	*/ 
	private String getReturnInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0200 cmr0200 = new Cmr0200();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0200.getReturnInfo(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0200 = null;
		} 
	}
}
