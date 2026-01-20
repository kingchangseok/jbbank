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

import app.eCmd.Cmd0300;

@WebServlet("/webPage/ecmd/Cmd0300Servlet")
public class Cmd0300Servlet extends HttpServlet {

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
			case "getMKDIR":
				response.getWriter().write(getMKDIR(jsonElement));
				break;
			case "METHCD_Set":
				response.getWriter().write(METHCD_Set(jsonElement));
				break;
			case "PrjNo_make":
				response.getWriter().write(PrjNo_make(jsonElement));
				break;
			case "Cmd0300_UPDATE":
				response.getWriter().write(Cmd0300_UPDATE(jsonElement));
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
	* String PrjNo
	*/ 
	private String getMKDIR(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0300 cmd0300 = new Cmd0300();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd0300.getMKDIR(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0300 = null;
		} 
	}
	/* Parameter 
	* String PrjNo
	*/ 
	private String METHCD_Set(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0300 cmd0300 = new Cmd0300();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd0300.METHCD_Set(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0300 = null;
		} 
	}
	/* Parameter 
	* String PrjNo
	*/ 
	private String PrjNo_make(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0300 cmd0300 = new Cmd0300();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd0300.PrjNo_make(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0300 = null;
		} 
	}
	/* Parameter 
	* String UserId,String PrjNo,int index
	*/ 
	private String Cmd0300_UPDATE(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0300 cmd0300 = new Cmd0300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    int index = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "index")));
		    return ParsingCommon.toJson(cmd0300.Cmd0300_UPDATE(UserId,PrjNo,index));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0300 = null;
		} 
	}
}
