package html.app.eCmr;
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

import app.eCmr.Cmr9999;

@WebServlet("/webPage/ecmr/Cmr9999Servlet")
public class Cmr9999Servlet extends HttpServlet {

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
	* String CR_TITLE,String CR_CONTENT,String strUserId,ArrayList<HashMap<String,String>> CM_USERID
	*/ 
	private String insert_Cmr9910(JsonElement jsonElement) throws SQLException, Exception {
		Cmr9999 cmr9999 = new Cmr9999();
		try {
		    String CR_TITLE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CR_TITLE"));
		    String CR_CONTENT = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CR_CONTENT"));
		    String strUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "strUserId"));
	   	    ArrayList<HashMap<String,String>> CM_USERID = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "CM_USERID"));
		    return ParsingCommon.toJson(cmr9999.insert_Cmr9910(CR_TITLE,CR_CONTENT,strUserId,CM_USERID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr9999 = null;
		} 
	}
}
