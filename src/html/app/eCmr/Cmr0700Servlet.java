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

import app.eCmr.Cmr0700;

@WebServlet("/webPage/ecmr/Cmr0700Servlet")
public class Cmr0700Servlet extends HttpServlet {

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
			case "request_Check_In":
				response.getWriter().write(request_Check_In(jsonElement));
				break;
			case "pgmCheck":
				response.getWriter().write(pgmCheck(jsonElement));
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
	* ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData,ArrayList<HashMap<String,Object>>ConfList,String confFg
	*/ 
	private String request_Check_In(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0700 cmr0700 = new Cmr0700();
		try {
	   	    ArrayList<HashMap<String,String>> chkInList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkInList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    String confFg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "confFg"));
		    return ParsingCommon.toJson(cmr0700.request_Check_In(chkInList,etcData,ConfList,confFg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0700 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String RsrcName
	*/ 
	private String pgmCheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0700 cmr0700 = new Cmr0700();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    return ParsingCommon.toJson(cmr0700.pgmCheck(SysCd,RsrcName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0700 = null;
		} 
	}
}
