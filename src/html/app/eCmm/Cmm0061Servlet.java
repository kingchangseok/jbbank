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

import app.eCmm.Cmm0061;

@WebServlet("/webPage/ecmm/Cmm0061Servlet")
public class Cmm0061Servlet extends HttpServlet {

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
			case "get_SelectTimeSch":
				response.getWriter().write(get_SelectTimeSch(jsonElement));
				break;
			case "get_Update_time":
				response.getWriter().write(get_Update_time(jsonElement));
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
	* String rgtcd
	*/ 
	private String get_SelectTimeSch(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0061 cmm0061 = new Cmm0061();
		try {
		    String rgtcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rgtcd"));
		    return ParsingCommon.toJson(cmm0061.get_SelectTimeSch(rgtcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0061 = null;
		} 
	}
	/* Parameter 
	* String ComSTTime,String ComEDTime,String WedSTTime,String WedEDTime,String HolSTTime,String HolEDTime,String rgtcd
	*/ 
	private String get_Update_time(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0061 cmm0061 = new Cmm0061();
		try {
		    String ComSTTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ComSTTime"));
		    String ComEDTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ComEDTime"));
		    String WedSTTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "WedSTTime"));
		    String WedEDTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "WedEDTime"));
		    String HolSTTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HolSTTime"));
		    String HolEDTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HolEDTime"));
		    String rgtcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rgtcd"));
		    return ParsingCommon.toJson(cmm0061.get_Update_time(ComSTTime,ComEDTime,WedSTTime,WedEDTime,HolSTTime,HolEDTime,rgtcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0061 = null;
		} 
	}
}
