package html.app.eCmd;
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

import app.eCmd.Cmd1500;

@WebServlet("/webPage/ecmd/Cmd1500Servlet")
public class Cmd1500Servlet extends HttpServlet {

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
			case "getPrjNo":
				response.getWriter().write(getPrjNo(jsonElement));
				break;
			case "getHistList":
				response.getWriter().write(getHistList(jsonElement));
				break;
			case "getDocList":
				response.getWriter().write(getDocList(jsonElement));
				break;
			case "getEditor":
				response.getWriter().write(getEditor(jsonElement));
				break;
			case "cmr0030Updt":
				response.getWriter().write(cmr0030Updt(jsonElement));
				break;
			case "cmr0030Dlet":
				response.getWriter().write(cmr0030Dlet(jsonElement));
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
	* String DocId,String UserId
	*/ 
	private String getPrjNo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1500 cmd1500 = new Cmd1500();
		try {
		    String DocId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1500.getPrjNo(DocId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1500 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String getHistList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1500 cmd1500 = new Cmd1500();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd1500.getHistList(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1500 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String getDocList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1500 cmd1500 = new Cmd1500();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd1500.getDocList(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1500 = null;
		} 
	}
	/* Parameter 
	* String PrjNo
	*/ 
	private String getEditor(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1500 cmd1500 = new Cmd1500();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd1500.getEditor(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1500 = null;
		} 
	}
	/* Parameter 
	* String DocId,String Editor,String ccbYn,String docSta,String UserId
	*/ 
	private String cmr0030Updt(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1500 cmd1500 = new Cmd1500();
		try {
		    String DocId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocId"));
		    String Editor = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Editor"));
		    String ccbYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ccbYn"));
		    String docSta = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "docSta"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd1500.cmr0030Updt(DocId,Editor,ccbYn,docSta,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1500 = null;
		} 
	}
	/* Parameter 
	* String DocId
	*/ 
	private String cmr0030Dlet(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1500 cmd1500 = new Cmd1500();
		try {
		    String DocId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocId"));
		    return ParsingCommon.toJson(cmd1500.cmr0030Dlet(DocId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1500 = null;
		} 
	}
}
