package html.app.eCmm;
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

import app.eCmm.Cmm0600;

@WebServlet("/webPage/ecmm/Cmm0600Servlet")
public class Cmm0600Servlet extends HttpServlet {

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
			case "getRgtMenuList":
				response.getWriter().write(getRgtMenuList(jsonElement));
				break;
			case "setRgtMenuList":
				response.getWriter().write(setRgtMenuList(jsonElement));
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
	* String rgtcd
	*/ 
	private String getRgtMenuList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0600 cmm0600 = new Cmm0600();
		try {
		    String rgtcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rgtcd"));
		    return ParsingCommon.toJson(cmm0600.getRgtMenuList(rgtcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0600 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> Lst_Duty,ArrayList<HashMap<String,String>> treeMenu
	*/ 
	private String setRgtMenuList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0600 cmm0600 = new Cmm0600();
		try {
	   	    ArrayList<HashMap<String,String>> Lst_Duty = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "Lst_Duty"));
	   	    ArrayList<HashMap<String,String>> treeMenu = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "treeMenu"));
		    return ParsingCommon.toJson(cmm0600.setRgtMenuList(Lst_Duty,treeMenu));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0600 = null;
		} 
	}
}
