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

import app.eCmr.Cmr0040;

@WebServlet("/webPage/ecmr/Cmr0040Servlet")
public class Cmr0040Servlet extends HttpServlet {

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
			case "SearchTerm":
				response.getWriter().write(SearchTerm(jsonElement));
				break;
			case "SearchCheck":
				response.getWriter().write(SearchCheck(jsonElement));
				break;
			case "getUnitTest":
				response.getWriter().write(getUnitTest(jsonElement));
				break;
			case "setTcaseAdd":
				response.getWriter().write(setTcaseAdd(jsonElement));
				break;
			case "delCase":
				response.getWriter().write(delCase(jsonElement));
				break;
			case "chkExcelList":
				response.getWriter().write(chkExcelList(jsonElement));
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
	private String SearchTerm(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0040 cmr0040 = new Cmr0040();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0040.SearchTerm(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0040 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String SearchCheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0040 cmr0040 = new Cmr0040();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0040.SearchCheck(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0040 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId,String UserId
	*/ 
	private String getUnitTest(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0040 cmr0040 = new Cmr0040();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0040.getUnitTest(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0040 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> TestList,ArrayList<HashMap<String,String>> CheckList
	*/ 
	private String setTcaseAdd(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0040 cmr0040 = new Cmr0040();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> TestList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "TestList"));
	   	    ArrayList<HashMap<String,String>> CheckList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "CheckList"));
		    return ParsingCommon.toJson(cmr0040.setTcaseAdd(etcData,TestList,CheckList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0040 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String delCase(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0040 cmr0040 = new Cmr0040();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0040.delCase(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0040 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> FileList,String ReqCd
	*/ 
	private String chkExcelList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0040 cmr0040 = new Cmr0040();
		try {
	   	    ArrayList<HashMap<String,String>> FileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "FileList"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmr0040.chkExcelList(FileList,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0040 = null;
		} 
	}
}
