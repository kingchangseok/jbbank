package html.app.eCmp;
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

import app.eCmp.Cmp0600;

@WebServlet("/webPage/ecmp/Cmp0600Servlet")
public class Cmp0600Servlet extends HttpServlet {

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
			case "get_SelectList":
				response.getWriter().write(get_SelectList(jsonElement));
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
	* String pSysCd,String pReqCd,String pTeamCd,String pEmgCd,String pReqUser,String pAcptN,String pProgN,String pReal,String pTest,String pStartDt,String pEndDt,String pUserId
	*/ 
	private String get_SelectList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0600 cmp0600 = new Cmp0600();
		try {
		    String pSysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pSysCd"));
		    String pReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqCd"));
		    String pTeamCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pTeamCd"));
		    String pEmgCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEmgCd"));
		    String pReqUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqUser"));
		    String pAcptN = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pAcptN"));
		    String pProgN = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pProgN"));
		    String pReal = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReal"));
		    String pTest = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pTest"));
		    String pStartDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStartDt"));
		    String pEndDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEndDt"));
		    String pUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUserId"));
		    return ParsingCommon.toJson(cmp0600.get_SelectList(pSysCd,pReqCd,pTeamCd,pEmgCd,pReqUser,pAcptN,pProgN,pReal,pTest,pStartDt,pEndDt,pUserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0600 = null;
		} 
	}
}
