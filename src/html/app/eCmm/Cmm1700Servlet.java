package html.app.eCmm;
import java.io.IOException;
import java.sql.SQLException;

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

import app.eCmm.Cmm1700;

@WebServlet("/webPage/ecmm/Cmm1700Servlet")
public class Cmm1700Servlet extends HttpServlet {

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
			case "PassWd_reset":
				response.getWriter().write(PassWd_reset(jsonElement));
				break;
			case "PassWd_reset_initPWD":
				response.getWriter().write(PassWd_reset_initPWD(jsonElement));
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
	* String user_id,String JuMinNUM
	*/ 
	private String PassWd_reset(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1700 cmm1700 = new Cmm1700();
		try {
		    String user_id = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "user_id"));
		    String JuMinNUM = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JuMinNUM"));
		    return ParsingCommon.toJson(cmm1700.PassWd_reset(user_id,JuMinNUM));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1700 = null;
		} 
	}
	/* Parameter 
	* String user_id,String initPWD
	*/ 
	private String PassWd_reset_initPWD(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1700 cmm1700 = new Cmm1700();
		try {
		    String user_id = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "user_id"));
		    String initPWD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "initPWD"));
		    return ParsingCommon.toJson(cmm1700.PassWd_reset_initPWD(user_id,initPWD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1700 = null;
		} 
	}
}
