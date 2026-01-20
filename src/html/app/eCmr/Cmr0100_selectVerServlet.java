package html.app.eCmr;
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

import app.eCmr.Cmr0100_selectVer;

@WebServlet("/webPage/ecmr/Cmr0100_selectVerServlet")
public class Cmr0100_selectVerServlet extends HttpServlet {

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
			case "getVerList":
				response.getWriter().write(getVerList(jsonElement));
				break;
			case "getRollVerList":
				response.getWriter().write(getRollVerList(jsonElement));
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
	* String itemID
	*/ 
	private String getVerList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100_selectVer cmr0100_selectver = new Cmr0100_selectVer();
		try {
		    String itemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemID"));
		    return ParsingCommon.toJson(cmr0100_selectver.getVerList(itemID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100_selectver = null;
		} 
	}
	/* Parameter 
	* String itemID
	*/ 
	private String getRollVerList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0100_selectVer cmr0100_selectver = new Cmr0100_selectVer();
		try {
		    String itemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemID"));
		    return ParsingCommon.toJson(cmr0100_selectver.getRollVerList(itemID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0100_selectver = null;
		} 
	}
}
