package html.app.eCmm;
import java.io.IOException;
import java.sql.SQLException;
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

import app.eCmm.Cmm1100;

@WebServlet("/webPage/ecmm/Cmm1100Servlet")
public class Cmm1100Servlet extends HttpServlet {

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
			case "get_grid_select":
				response.getWriter().write(get_grid_select(jsonElement));
				break;
			case "get_Update":
				response.getWriter().write(get_Update(jsonElement));
				break;
			case "Check_Confirm":
				response.getWriter().write(Check_Confirm(jsonElement));
				break;
			case "getCbo_User":
				response.getWriter().write(getCbo_User(jsonElement));
				break;
			case "getDaegyulList":
				response.getWriter().write(getDaegyulList(jsonElement));
				break;
			case "getDaegyulState":
				response.getWriter().write(getDaegyulState(jsonElement));
				break;
			case "getCbo_User_Click":
				response.getWriter().write(getCbo_User_Click(jsonElement));
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
	* String user_id
	*/ 
	private String get_grid_select(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    String user_id = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "user_id"));
		    return ParsingCommon.toJson(cmm1100.get_grid_select(user_id));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String get_Update(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmm1100.get_Update(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
	/* Parameter 
	* String UserId,boolean daeSw
	*/ 
	private String Check_Confirm(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    boolean daeSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "daeSw")));
		    return ParsingCommon.toJson(cmm1100.Check_Confirm(UserId,daeSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String Sv_Admin
	*/ 
	private String getCbo_User(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String Sv_Admin = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_Admin"));
		    return ParsingCommon.toJson(cmm1100.getCbo_User(UserId,Sv_Admin));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getDaegyulList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm1100.getDaegyulList(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getDaegyulState(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmm1100.getDaegyulState(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String cm_manid
	*/ 
	private String getCbo_User_Click(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1100 cmm1100 = new Cmm1100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String cm_manid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_manid"));
		    return ParsingCommon.toJson(cmm1100.getCbo_User_Click(UserId,cm_manid));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1100 = null;
		} 
	}
}
