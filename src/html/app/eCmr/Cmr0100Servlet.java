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

import app.eCmr.Cmr0100;

@WebServlet("/webPage/ecmr/Cmr0100Servlet")
public class Cmr0100Servlet extends HttpServlet {

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
			case "getTmpDirConf":
				response.getWriter().write(getTmpDirConf(jsonElement));
				break;
			case "getDsnCD":
				response.getWriter().write(getDsnCD(jsonElement));
				break;
			case "chk_Resouce":
				response.getWriter().write(chk_Resouce(jsonElement));
				break;
			case "getDownFileList":
				response.getWriter().write(getDownFileList(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "getDelFileList":
				response.getWriter().write(getDelFileList(jsonElement));
				break;
			case "getFileList_excel":
				response.getWriter().write(getFileList_excel(jsonElement));
				break;
			case "request_Check_Out":
				response.getWriter().write(request_Check_Out(jsonElement));
				break;
			case "getLinkList":
				response.getWriter().write(getLinkList(jsonElement));
				break;
			case "svrFileMake":
				response.getWriter().write(svrFileMake(jsonElement));
				break;
			case "execShell":
				response.getWriter().write(execShell(jsonElement));
				break;
			case "getSysInfo":
				response.getWriter().write(getSysInfo(jsonElement));
				break;
			case "getProgFileList":
				response.getWriter().write(getProgFileList(jsonElement));
				break;
			case "setTranResult":
				response.getWriter().write(setTranResult(jsonElement));
				break;
			case "callCmr9900_Str":
				response.getWriter().write(callCmr9900_Str(jsonElement));
				break;
			case "getReturnInfo":
				response.getWriter().write(getReturnInfo(jsonElement));
				break;
			case "chkRechkOut":
				response.getWriter().write(chkRechkOut(jsonElement));
				break;
			case "Cmr0100_Parasearch":
				response.getWriter().write(Cmr0100_Parasearch(jsonElement));
				break;
			case "cmr0100_ParaSearch_Search":
				response.getWriter().write(cmr0100_ParaSearch_Search(jsonElement));
				break;
			case "Gitlab_Export_ChgFile":
				response.getWriter().write(Gitlab_Export_ChgFile(jsonElement));
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
	* String pCode
	*/ 
	private String getTmpDirConf(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String pCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pCode"));
		    return ParsingCommon.toJson(cmr0100.getTmpDirConf(pCode));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String sysCD,String Path
	*/ 
	private String getDsnCD(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String sysCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCD"));
		    String Path = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Path"));
		    return ParsingCommon.toJson(cmr0100.getDsnCD(sysCD,Path));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String syscd,String Rsrccd
	*/ 
	private String chk_Resouce(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    String Rsrccd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Rsrccd"));
		    return ParsingCommon.toJson(cmr0100.chk_Resouce(syscd,Rsrccd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> fileList,String ReqCd,String SysCd,String ReqSayu
	*/ 
	private String getDownFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ReqSayu = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqSayu"));
		    return ParsingCommon.toJson(cmr0100.getDownFileList(UserId,fileList,ReqCd,SysCd,ReqSayu));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String SysGb,String DsnCd,String RsrcCd,String RsrcName,String ReqCd,String JobCd,boolean UpLowSw,boolean selfSw,boolean LikeSw
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SysGb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGb"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    boolean UpLowSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UpLowSw")));
		    boolean selfSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selfSw")));
		    boolean LikeSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LikeSw")));
		    return ParsingCommon.toJson(cmr0100.getFileList(UserId,SysCd,SysGb,DsnCd,RsrcCd,RsrcName,ReqCd,JobCd,UpLowSw,selfSw,LikeSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String SysGb,String DsnCd,String RsrcCd,String RsrcName,String ReqCd,String JobCd,boolean UpLowSw,boolean selfSw,boolean LikeSw
	*/ 
	private String getDelFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SysGb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGb"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    boolean UpLowSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UpLowSw")));
		    boolean selfSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selfSw")));
		    boolean LikeSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LikeSw")));
		    return ParsingCommon.toJson(cmr0100.getDelFileList(UserId,SysCd,SysGb,DsnCd,RsrcCd,RsrcName,ReqCd,JobCd,UpLowSw,selfSw,LikeSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,String SysCd,String SysGb,String ReqCd
	*/ 
	private String getFileList_excel(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SysGb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGb"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    boolean selfSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selfSw")));
		    boolean UplowSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UplowSw")));
		    return ParsingCommon.toJson(cmr0100.getFileList_excel(fileList,SysCd,SysGb,ReqCd,UserId,selfSw, UplowSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkOutList,HashMap<String,String> etcData,ArrayList<HashMap<String,Object>>ConfList,ArrayList<HashMap<String,String>> OrderList
	*/ 
	private String request_Check_Out(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
	   	    ArrayList<HashMap<String,String>> chkOutList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkOutList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
	   	    ArrayList<HashMap<String,String>> OrderList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "OrderList"));
		    return ParsingCommon.toJson(cmr0100.request_Check_Out(chkOutList,etcData,ConfList,OrderList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo
	*/ 
	private String getLinkList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0100.getLinkList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String svrFileMake(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0100.svrFileMake(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String shFile,String parmName
	*/ 
	private String execShell(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String shFile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "shFile"));
		    String parmName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "parmName"));
		    return ParsingCommon.toJson(cmr0100.execShell(shFile,parmName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo
	*/ 
	private String getSysInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0100.getSysInfo(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String fileGb
	*/ 
	private String getProgFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String fileGb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "fileGb"));
		    return ParsingCommon.toJson(cmr0100.getProgFileList(AcptNo,fileGb));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String acptNo,String serNo,String ret
	*/ 
	private String setTranResult(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    String serNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "serNo"));
		    String ret = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ret"));
		    return ParsingCommon.toJson(cmr0100.setTranResult(acptNo,serNo,ret));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String acptNo
	*/ 
	private String callCmr9900_Str(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    return ParsingCommon.toJson(cmr0100.callCmr9900_Str(acptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo
	*/ 
	private String getReturnInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0100.getReturnInfo(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String chkRechkOut(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0100.chkRechkOut(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> Itemid,String userid,String syscod
	*/ 
	private String Cmr0100_Parasearch(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
	   	    ArrayList<HashMap<String,String>> Itemid = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "Itemid"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    String syscod = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscod"));
		    return ParsingCommon.toJson(cmr0100.Cmr0100_Parasearch(Itemid,userid,syscod));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> Itemid
	*/ 
	private String cmr0100_ParaSearch_Search(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
	   	    ArrayList<HashMap<String,String>> Itemid = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "Itemid"));
		    return ParsingCommon.toJson(cmr0100.cmr0100_ParaSearch_Search(Itemid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> gitData
	*/ 
	private String Gitlab_Export_ChgFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100 cmr0100 = new Cmr0100();
		try {
			HashMap<String,String> gitData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "gitData"));
		    return ParsingCommon.toJson(cmr0100.Gitlab_Export_ChgFile(gitData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100 = null;
		} 
	}
}
