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

import app.eCmc.Cmc0100;

@WebServlet("/webPage/ecmc/Cmc0100Servlet")
public class Cmc0100Servlet extends HttpServlet {

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
			case "getISRList":
				response.getWriter().write(getISRList(jsonElement));
				break;
			case "getISRInfo_Main":
				response.getWriter().write(getISRInfo_Main(jsonElement));
				break;
			case "getISRInfo":
				response.getWriter().write(getISRInfo(jsonElement));
				break;
			case "getReqCnt":
				response.getWriter().write(getReqCnt(jsonElement));
				break;
			case "getCnclCnt":
				response.getWriter().write(getCnclCnt(jsonElement));
				break;
			case "delISRInfo":
				response.getWriter().write(delISRInfo(jsonElement));
				break;
			case "setISRInfo":
				response.getWriter().write(setISRInfo(jsonElement));
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
	* HashMap<String,String> etcData
	*/ 
	private String getISRList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmc0100.getISRList(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
	/* Parameter 
	* String IsrId
	*/ 
	private String getISRInfo_Main(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    return ParsingCommon.toJson(cmc0100.getISRInfo_Main(IsrId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
	/* Parameter 
	* String strISRID,String strISRSUBID,String UserId
	*/ 
	private String getISRInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    String strISRID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strISRID"));
		    String strISRSUBID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strISRSUBID"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmc0100.getISRInfo(strISRID,strISRSUBID,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
	/* Parameter 
	* String strISRID
	*/ 
	private String getReqCnt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    String strISRID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strISRID"));
		    return ParsingCommon.toJson(cmc0100.getReqCnt(strISRID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
	/* Parameter 
	* String strISRID
	*/ 
	private String getCnclCnt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    String strISRID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strISRID"));
		    return ParsingCommon.toJson(cmc0100.getCnclCnt(strISRID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String delISRInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmc0100.delISRInfo(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> treeDept,ArrayList<HashMap<String,Object>>ConfList
	*/ 
	private String setISRInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0100 cmc0100 = new Cmc0100();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> treeDept = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "treeDept"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    return ParsingCommon.toJson(cmc0100.setISRInfo(etcData,treeDept,ConfList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0100 = null;
		} 
	}
}
