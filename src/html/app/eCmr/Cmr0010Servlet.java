package html.app.eCmr;
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

import app.eCmr.Cmr0010;

@WebServlet("/webPage/ecmr/Cmr0010Servlet")
public class Cmr0010Servlet extends HttpServlet {

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
			case "getRFCInfo":
				response.getWriter().write(getRFCInfo(jsonElement));
				break;
			case "getChgUser":
				response.getWriter().write(getChgUser(jsonElement));
				break;
			case "setAcceptRFCInfo":
				response.getWriter().write(setAcceptRFCInfo(jsonElement));
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
	* String IsrId,String SubId,String UserId
	*/ 
	private String getRFCInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0010 cmr0010 = new Cmr0010();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0010.getRFCInfo(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0010 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId
	*/ 
	private String getChgUser(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0010 cmr0010 = new Cmr0010();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    return ParsingCommon.toJson(cmr0010.getChgUser(IsrId,SubId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0010 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setAcceptRFCInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0010 cmr0010 = new Cmr0010();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0010.setAcceptRFCInfo(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0010 = null;
		} 
	}
}
