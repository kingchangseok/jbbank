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

import app.eCmr.Cmr3800;

@WebServlet("/webPage/ecmr/Cmr3800Servlet")
public class Cmr3800Servlet extends HttpServlet {

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
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "getFileList2":
				response.getWriter().write(getFileList2(jsonElement));
				break;
			case "BePoplay":
				response.getWriter().write(BePoplay(jsonElement));
				break;
			case "BePoplay1":
				response.getWriter().write(BePoplay1(jsonElement));
				break;
			case "get_node":
				response.getWriter().write(get_node(jsonElement));
				break;
			case "get_Instance":
				response.getWriter().write(get_Instance(jsonElement));
				break;
			case "cmd7000_INSERT":
				response.getWriter().write(cmd7000_INSERT(jsonElement));
				break;
			case "cmr7000_INSERT":
				response.getWriter().write(cmr7000_INSERT(jsonElement));
				break;
			case "cmd7000_SELECT":
				response.getWriter().write(cmd7000_SELECT(jsonElement));
				break;
			case "cmd7000_SELECT1":
				response.getWriter().write(cmd7000_SELECT1(jsonElement));
				break;
			case "cmd7000_SELECT2":
				response.getWriter().write(cmd7000_SELECT2(jsonElement));
				break;
			case "cmd7000_DELETE":
				response.getWriter().write(cmd7000_DELETE(jsonElement));
				break;
			case "cmr7100_search":
				response.getWriter().write(cmr7100_search(jsonElement));
				break;
			case "cmd7000_CHK":
				response.getWriter().write(cmd7000_CHK(jsonElement));
				break;
			case "cmr7010_result":
				response.getWriter().write(cmr7010_result(jsonElement));
				break;
			case "cmd7000_SELECTT":
				response.getWriter().write(cmd7000_SELECTT(jsonElement));
				break;
			case "cmr7000_INSERTT":
				response.getWriter().write(cmr7000_INSERTT(jsonElement));
				break;
			case "trcontrol_call":
				response.getWriter().write(trcontrol_call(jsonElement));
				break;
			case "HealthChk":
				response.getWriter().write(HealthChk(jsonElement));
				break;
			case "getFileList_Emg":
				response.getWriter().write(getFileList_Emg(jsonElement));
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
	* String strUserId,String strSys,String strDept,String cboGbn,String Admin
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String strUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strUserId"));
		    String strSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strSys"));
		    String strDept = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDept"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    String Admin = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Admin"));
		    return ParsingCommon.toJson(cmr3800.getFileList(strUserId,strSys,strDept,cboGbn,Admin));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String acptno,String cboGbn
	*/ 
	private String getFileList2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String acptno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptno"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    return ParsingCommon.toJson(cmr3800.getFileList2(acptno,cboGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,String cboGbn
	*/ 
	private String BePoplay(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    return ParsingCommon.toJson(cmr3800.BePoplay(chkInList,cboGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,String cboGbn
	*/ 
	private String BePoplay1(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    return ParsingCommon.toJson(cmr3800.BePoplay1(chkInList,cboGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String get_node(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmr3800.get_node(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String get_Instance(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    return ParsingCommon.toJson(cmr3800.get_Instance());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String node,String svrip,String portno,String svrstart,String svrstop,String stopck,String startck,String instancecd,String instanceid,String reqcd,String runcd,String userid,String strchk,String health
	*/ 
	private String cmd7000_INSERT(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String svrip = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "svrip"));
		    String portno = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "portno"));
		    String svrstart = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "svrstart"));
		    String svrstop = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "svrstop"));
		    String stopck = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "stopck"));
		    String startck = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "startck"));
		    String instancecd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instancecd"));
		    String instanceid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instanceid"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    String runcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "runcd"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    String strchk = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strchk"));
		    String health = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "health"));
		    
		    cmr3800.cmd7000_INSERT(node,svrip,portno,svrstart,svrstop,stopck,startck,instancecd,instanceid,reqcd,runcd,userid,strchk,health);
		    return "0";
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCm7000List
	*/ 
	private String cmr7000_INSERT(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
	   	    ArrayList<HashMap<String,String>> getCm7000List = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCm7000List"));
		    return ParsingCommon.toJson(cmr3800.cmr7000_INSERT(getCm7000List));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String node,String instancecd,String instanceid,String reqcd,String runcd
	*/ 
	private String cmd7000_SELECT(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String instancecd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instancecd"));
		    String instanceid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instanceid"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    String runcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "runcd"));
		    return ParsingCommon.toJson(cmr3800.cmd7000_SELECT(node,instancecd,instanceid,reqcd,runcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String acptdate,String node,String instance
	*/ 
	private String cmd7000_SELECT1(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String acptdate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptdate"));
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String instance = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instance"));
		    return ParsingCommon.toJson(cmr3800.cmd7000_SELECT1(acptdate,node,instance));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String acptdate
	*/ 
	private String cmd7000_SELECT2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String acptdate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptdate"));
		    return ParsingCommon.toJson(cmr3800.cmd7000_SELECT2(acptdate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String node,String reqcd,String runcd,String instancecd,String instanceid
	*/ 
	private String cmd7000_DELETE(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    String runcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "runcd"));
		    String instancecd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instancecd"));
		    String instanceid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instanceid"));
		    cmr3800.cmd7000_DELETE(node,reqcd,runcd,instancecd,instanceid);
		    return "0";
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String cmr7100_search(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    return ParsingCommon.toJson(cmr3800.cmr7100_search());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String acptdate
	*/ 
	private String cmd7000_CHK(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String acptdate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptdate"));
		    return ParsingCommon.toJson(cmr3800.cmd7000_CHK(acptdate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String rstfile
	*/ 
	private String cmr7010_result(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String rstfile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rstfile"));
		    return ParsingCommon.toJson(cmr3800.cmr7010_result(rstfile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String acptdate,String node,String instance,String status,String Healthck
	*/ 
	private String cmd7000_SELECTT(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String acptdate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptdate"));
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String instance = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instance"));
		    String status = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "status"));
		    String Healthck = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Healthck"));
		    return ParsingCommon.toJson(cmr3800.cmd7000_SELECTT(acptdate,node,instance,status,Healthck));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCm7000List
	*/ 
	private String cmr7000_INSERTT(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
	   	    ArrayList<HashMap<String,String>> getCm7000List = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCm7000List"));
		    return ParsingCommon.toJson(cmr3800.cmr7000_INSERTT(getCm7000List));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String node,String instance,String userid
	*/ 
	private String trcontrol_call(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String instance = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instance"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    return ParsingCommon.toJson(cmr3800.trcontrol_call(node,instance,userid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String node,String instance,String userid
	*/ 
	private String HealthChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String node = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "node"));
		    String instance = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "instance"));
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    return ParsingCommon.toJson(cmr3800.HealthChk(node,instance,userid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
	/* Parameter 
	* String strUserId,String strSys,String strDept,String cboGbn,String Admin,String emgSw
	*/ 
	private String getFileList_Emg(JsonElement jsonElement) throws SQLException, Exception {
		Cmr3800 cmr3800 = new Cmr3800();
		try {
		    String strUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strUserId"));
		    String strSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strSys"));
		    String strDept = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strDept"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    String Admin = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Admin"));
		    String emgSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "emgSw"));
		    return ParsingCommon.toJson(cmr3800.getFileList_Emg(strUserId,strSys,strDept,cboGbn,Admin,emgSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr3800 = null;
		} 
	}
}
