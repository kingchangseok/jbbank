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

import app.eCmr.Cmr0508;

@WebServlet("/webPage/ecmr/Cmr0508Servlet")
public class Cmr0508Servlet extends HttpServlet {

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
			case "getSysInfo":
				response.getWriter().write(getSysInfo(jsonElement));
				break;
			case "getJobInfo":
				response.getWriter().write(getJobInfo(jsonElement));
				break;
			case "request_Check_In":
				response.getWriter().write(request_Check_In(jsonElement));
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
	* String UserId
	*/ 
	private String getSysInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0508 cmr0508 = new Cmr0508();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0508.getSysInfo(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0508 = null;
		} 
	}
	/* Parameter 
	* String SelMsg
	*/ 
	private String getJobInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0508 cmr0508 = new Cmr0508();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmr0508.getJobInfo(SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0508 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkInList,ArrayList<HashMap<String,String>> SinList,ArrayList<HashMap<String,String>> SinList2,ArrayList<HashMap<String,String>> SinList3,HashMap<String,String> etcData,ArrayList<HashMap<String,String>> befJob,ArrayList<HashMap<String,Object>>ConfList,String confFg
	*/ 
	private String request_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0508 cmr0508 = new Cmr0508();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
	   	    ArrayList<HashMap<String,String>> SinList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "SinList"));
	   	    ArrayList<HashMap<String,String>> SinList2 = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "SinList2"));
	   	    ArrayList<HashMap<String,String>> SinList3 = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "SinList3"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> befJob = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "befJob"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    String confFg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "confFg"));
		    return ParsingCommon.toJson(cmr0508.request_Check_In(chkInList,SinList,SinList2,SinList3,etcData,befJob,ConfList,confFg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0508 = null;
		} 
	}
}
