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

import app.eCmr.Cmr0550;

@WebServlet("/webPage/ecmr/Cmr0550Servlet")
public class Cmr0550Servlet extends HttpServlet {

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
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
				break;
			case "JoinUserBace":
				response.getWriter().write(JoinUserBace(jsonElement));
				break;
			case "JoinUserJob":
				response.getWriter().write(JoinUserJob(jsonElement));
				break;
			case "updtConfirm":
				response.getWriter().write(updtConfirm(jsonElement));
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
	* String UserId,String AcptNo
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0550 cmr0550 = new Cmr0550();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0550.getReqList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0550 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String JoinUserBace(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0550 cmr0550 = new Cmr0550();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0550.JoinUserBace(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0550 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String JoinUserJob(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0550 cmr0550 = new Cmr0550();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0550.JoinUserJob(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0550 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg
	*/ 
	private String updtConfirm(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0550 cmr0550 = new Cmr0550();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    return ParsingCommon.toJson(cmr0550.updtConfirm(AcptNo,UserId,conMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0550 = null;
		} 
	}
}
