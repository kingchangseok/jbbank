package html.app.eCmc;
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

import app.eCmc.Cmc0500;

@WebServlet("/webPage/ecmc/Cmc0500Servlet")
public class Cmc0500Servlet extends HttpServlet {

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
			case "setORDERInfo":
				response.getWriter().write(setORDERInfo(jsonElement));
				break;
			case "getREQList":
				response.getWriter().write(getREQList(jsonElement));
				break;
			case "setORDERupdt":
				response.getWriter().write(setORDERupdt(jsonElement));
				break;
			case "getORDERListSearch":
				response.getWriter().write(getORDERListSearch(jsonElement));
				break;
			case "getORDERList":
				response.getWriter().write(getORDERList(jsonElement));
				break;
			case "delREQinfo":
				response.getWriter().write(delREQinfo(jsonElement));
				break;
			case "getAttfileInfo":
				response.getWriter().write(getAttfileInfo(jsonElement));
				break;
			case "getReqInfo":
				response.getWriter().write(getReqInfo(jsonElement));
				break;
			case "OrderSelect":
				response.getWriter().write(OrderSelect(jsonElement));
				break;
			case "OrderSelect2":
				response.getWriter().write(OrderSelect2(jsonElement));
				break;
			case "getOrderInfo":
				response.getWriter().write(getOrderInfo(jsonElement));
				break;
			case "getOrderRunners":
				response.getWriter().write(getOrderRunners(jsonElement));
				break;
			case "getOrderThirds":
				response.getWriter().write(getOrderThirds(jsonElement));
				break;
			case "statusChk":
				response.getWriter().write(statusChk(jsonElement));
				break;
			case "statusUpdt":
				response.getWriter().write(statusUpdt(jsonElement));
				break;
			case "getMainOrderList":
				response.getWriter().write(getMainOrderList(jsonElement));
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
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> RunnerList,ArrayList<HashMap<String,String>> tmpRunnerList,ArrayList<HashMap<String,String>> ThirdList
	*/ 
	private String setORDERInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> RunnerList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "RunnerList"));
	   	    ArrayList<HashMap<String,String>> tmpRunnerList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "tmpRunnerList"));
	   	    ArrayList<HashMap<String,String>> ThirdList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "ThirdList"));
		    return ParsingCommon.toJson(cmc0500.setORDERInfo(etcData,RunnerList,tmpRunnerList,ThirdList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String status,String UserID,String cboGbn,String datStD,String datEdD,String Gubun
	*/ 
	private String getREQList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String status = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "status"));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String cboGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboGbn"));
		    String datStD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "datStD"));
		    String datEdD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "datEdD"));
		    String Gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Gubun"));
		    return ParsingCommon.toJson(cmc0500.getREQList(status,UserID,cboGbn,datStD,datEdD,Gubun));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> RunnerList,ArrayList<HashMap<String,String>> tmpRunnerList,ArrayList<HashMap<String,String>> ThirdList
	*/ 
	private String setORDERupdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> RunnerList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "RunnerList"));
	   	    ArrayList<HashMap<String,String>> tmpRunnerList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "tmpRunnerList"));
	   	    ArrayList<HashMap<String,String>> ThirdList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "ThirdList"));
		    return ParsingCommon.toJson(cmc0500.setORDERupdt(etcData,RunnerList,tmpRunnerList,ThirdList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String Status,String Stdt,String Eddt,String UserId
	*/ 
	private String getORDERListSearch(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String Status = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Status"));
		    String Stdt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Stdt"));
		    String Eddt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Eddt"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmc0500.getORDERListSearch(Status,Stdt,Eddt,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String Userid,String Status,String ReqID,String SubID,String Stdt,String Eddt,String Selfsw
	*/ 
	private String getORDERList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String Userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Userid"));
		    String Status = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Status"));
		    String ReqID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqID"));
		    String SubID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubID"));
		    String Stdt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Stdt"));
		    String Eddt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Eddt"));
		    String Selfsw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Selfsw"));
		    return ParsingCommon.toJson(cmc0500.getORDERList(Userid,Status,ReqID,SubID,Stdt,Eddt,Selfsw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> delREQ
	*/ 
	private String delREQinfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    HashMap<String,String> delREQ = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "delREQ"));
		    return ParsingCommon.toJson(cmc0500.delREQinfo(delREQ));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String Userid,String Reqid,String Subid
	*/ 
	private String getAttfileInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String Userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Userid"));
		    String Reqid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Reqid"));
		    String Subid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Subid"));
		    return ParsingCommon.toJson(cmc0500.getAttfileInfo(Userid,Reqid,Subid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String Userid,String OrderId
	*/ 
	private String getReqInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String Userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Userid"));
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    return ParsingCommon.toJson(cmc0500.getReqInfo(Userid,OrderId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String Userid,String SelMsg
	*/ 
	private String OrderSelect(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String Userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Userid"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmc0500.OrderSelect(Userid,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String Userid
	*/ 
	private String OrderSelect2(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String Userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Userid"));
		    return ParsingCommon.toJson(cmc0500.OrderSelect2(Userid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String OrderId
	*/ 
	private String getOrderInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    return ParsingCommon.toJson(cmc0500.getOrderInfo(OrderId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String OrderId
	*/ 
	private String getOrderRunners(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    return ParsingCommon.toJson(cmc0500.getOrderRunners(OrderId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String OrderId
	*/ 
	private String getOrderThirds(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    return ParsingCommon.toJson(cmc0500.getOrderThirds(OrderId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String userID,String orderID,String reqID
	*/ 
	private String statusChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String userID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userID"));
		    String orderID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "orderID"));
		    String reqID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqID"));
		    return ParsingCommon.toJson(cmc0500.statusChk(userID,orderID,reqID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String userID,String orderID,String reqID
	*/ 
	private String statusUpdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String userID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userID"));
		    String orderID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "orderID"));
		    String reqID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqID"));
		    return ParsingCommon.toJson(cmc0500.statusUpdt(userID,orderID,reqID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getMainOrderList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0500 cmc0500 = new Cmc0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmc0500.getMainOrderList(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0500 = null;
		} 
	}
}
