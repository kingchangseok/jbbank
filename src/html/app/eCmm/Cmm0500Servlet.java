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

import app.eCmm.Cmm0500;

@WebServlet("/webPage/ecmm/Cmm0500Servlet")
public class Cmm0500Servlet extends HttpServlet {

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
			case "getMenuList":
				response.getWriter().write(getMenuList(jsonElement));
				break;
			case "getLowMenuList":
				response.getWriter().write(getLowMenuList(jsonElement));
				break;
			case "getMenuAllList":
				response.getWriter().write(getMenuAllList(jsonElement));
				break;
			case "setMenuInfo":
				response.getWriter().write(setMenuInfo(jsonElement));
				break;
			case "setMenuList":
				response.getWriter().write(setMenuList(jsonElement));
				break;
			case "delMenuInfo":
				response.getWriter().write(delMenuInfo(jsonElement));
				break;
			case "getMenuZTree":
				response.getWriter().write(getMenuZTree(jsonElement));
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
	* String sqlGB
	*/ 
	private String getMenuList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
		    String sqlGB = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sqlGB"));
		    return ParsingCommon.toJson(cmm0500.getMenuList(sqlGB));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0500 = null;
		} 
	}
	/* Parameter 
	* String Cbo_Menu
	*/ 
	private String getLowMenuList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
		    String Cbo_Menu = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_Menu"));
		    return ParsingCommon.toJson(cmm0500.getLowMenuList(Cbo_Menu));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0500 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getMenuAllList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
		    return ParsingCommon.toJson(cmm0500.getMenuAllList());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0500 = null;
		} 
	}
	/* Parameter 
	* String Cbo_MaCode,String Txt_MaCode,String Txt_MaFile
	*/ 
	private String setMenuInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
		    String Cbo_MaCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_MaCode"));
		    String Txt_MaCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_MaCode"));
		    String Txt_MaFile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_MaFile"));
		    String reqcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "reqcd"));
		    return ParsingCommon.toJson(cmm0500.setMenuInfo(Cbo_MaCode,Txt_MaCode,Txt_MaFile,reqcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0500 = null;
		} 
	}
	/* Parameter 
	* String Cbo_selMenu,String Cbo_Menu,ArrayList<HashMap<String,String>> Lst_DevStep
	*/ 
	private String setMenuList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
		    String Cbo_selMenu = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_selMenu"));
		    String Cbo_Menu = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_Menu"));
	   	    ArrayList<HashMap<String,String>> Lst_DevStep = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "Lst_DevStep"));
		    return ParsingCommon.toJson(cmm0500.setMenuList(Cbo_selMenu,Cbo_Menu,Lst_DevStep));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0500 = null;
		} 
	}
	/* Parameter 
	* String Cbo_MaCode
	*/ 
	private String delMenuInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
		    String Cbo_MaCode = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_MaCode"));
		    return ParsingCommon.toJson(cmm0500.delMenuInfo(Cbo_MaCode));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0500 = null;
		} 
	}
	/* Parameter 
	 * 
	 */ 
	private String getMenuZTree(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0500 cmm0500 = new Cmm0500();
		try {
			return ParsingCommon.toJson(cmm0500.getMenuZTree());
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			cmm0500 = null;
		} 
	}
}
