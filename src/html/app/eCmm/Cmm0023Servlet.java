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

import app.eCmm.Cmm0023;

@WebServlet("/webPage/ecmm/Cmm0023Servlet")
public class Cmm0023Servlet extends HttpServlet {

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
			case "getRsrcList":
				response.getWriter().write(getRsrcList(jsonElement));
				break;
			case "getCboRsrc":
				response.getWriter().write(getCboRsrc(jsonElement));
				break;
			case "setRsrc":
				response.getWriter().write(setRsrc(jsonElement));
				break;
			case "delRsrc":
				response.getWriter().write(delRsrc(jsonElement));
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
	* HashMap<String,String> rsrcList
	*/ 
	private String getRsrcList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0023 cmm0023 = new Cmm0023();
		try {
		    HashMap<String,String> rsrcList = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "rsrcList"));
		    return ParsingCommon.toJson(cmm0023.getRsrcList(rsrcList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0023 = null;
		} 
	}
	/* Parameter 
	* String SelMsg
	*/ 
	private String getCboRsrc(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0023 cmm0023 = new Cmm0023();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmm0023.getCboRsrc(SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0023 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> rsrc
	*/ 
	private String setRsrc(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0023 cmm0023 = new Cmm0023();
		try {
		    HashMap<String,String> rsrc = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "rsrc"));
		    return ParsingCommon.toJson(cmm0023.setRsrc(rsrc));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0023 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> rsrcDel
	*/ 
	private String delRsrc(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0023 cmm0023 = new Cmm0023();
		try {
		    HashMap<String,String> rsrcDel = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "rsrcDel"));
		    return ParsingCommon.toJson(cmm0023.delRsrc(rsrcDel));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0023 = null;
		} 
	}
}
