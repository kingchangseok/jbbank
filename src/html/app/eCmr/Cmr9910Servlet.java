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

import app.eCmr.Cmr9910;

@WebServlet("/webPage/ecmr/Cmr9910Servlet")
public class Cmr9910Servlet extends HttpServlet {

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
			case "insert_Cmr9910":
				response.getWriter().write(insert_Cmr9910(jsonElement));
				break;
			case "select_name":
				response.getWriter().write(select_name(jsonElement));
				break;
			case "get_select_grid":
				response.getWriter().write(get_select_grid(jsonElement));
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
	* String cboUser,String strUserId,String CR_TITLE,String CR_CONTENT
	*/ 
	private String insert_Cmr9910(JsonElement jsonElement) throws SQLException, Exception {
		Cmr9910 cmr9910 = new Cmr9910();
		try {
		    String cboUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cboUser"));
		    String strUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strUserId"));
		    String CR_TITLE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CR_TITLE"));
		    String CR_CONTENT = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CR_CONTENT"));
		    return ParsingCommon.toJson(cmr9910.insert_Cmr9910(cboUser,strUserId,CR_TITLE,CR_CONTENT));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr9910 = null;
		} 
	}
	/* Parameter 
	* String strUserId
	*/ 
	private String select_name(JsonElement jsonElement) throws SQLException, Exception {
		Cmr9910 cmr9910 = new Cmr9910();
		try {
		    String strUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strUserId"));
		    return ParsingCommon.toJson(cmr9910.select_name(strUserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr9910 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String get_select_grid(JsonElement jsonElement) throws SQLException, Exception {
		Cmr9910 cmr9910 = new Cmr9910();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr9910.get_select_grid(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr9910 = null;
		} 
	}
}
