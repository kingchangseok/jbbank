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

import app.eCmr.Cmr9998;

@WebServlet("/webPage/ecmr/Cmr9998Servlet")
public class Cmr9998Servlet extends HttpServlet {

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
	* String Cbo_Find,String UserId,String strFind,String strFind1,String strFind2,String strStD,String EndDt
	*/ 
	private String get_select_grid(JsonElement jsonElement) throws SQLException, Exception {
		Cmr9998 cmr9998 = new Cmr9998();
		try {
		    String Cbo_Find = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_Find"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String strFind = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strFind"));
		    String strFind1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strFind1"));
		    String strFind2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strFind2"));
		    String strStD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strStD"));
		    String EndDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EndDt"));
		    return ParsingCommon.toJson(cmr9998.get_select_grid(Cbo_Find,UserId,strFind,strFind1,strFind2,strStD,EndDt));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr9998 = null;
		} 
	}
}
