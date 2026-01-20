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

import app.eCmr.Cmr5100;

@WebServlet("/webPage/ecmr/Cmr5100Servlet")
public class Cmr5100Servlet extends HttpServlet {

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
			case "getResultList":
				response.getWriter().write(getResultList(jsonElement));
				break;
			case "getResultList_Tmax":
				response.getWriter().write(getResultList_Tmax(jsonElement));
				break;
			case "getResultGbn":
				response.getWriter().write(getResultGbn(jsonElement));
				break;
			case "getFileText":
				response.getWriter().write(getFileText(jsonElement));
				break;
			case "getResultList_Doc":
				response.getWriter().write(getResultList_Doc(jsonElement));
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
	* String acptNo,String UserId
	*/ 
	private String getResultList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5100 cmr5100 = new Cmr5100();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr5100.getResultList(acptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5100 = null;
		} 
	}
	/* Parameter 
	* String acptNo,String UserId
	*/ 
	private String getResultList_Tmax(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5100 cmr5100 = new Cmr5100();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr5100.getResultList_Tmax(acptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5100 = null;
		} 
	}
	/* Parameter 
	* String acptNo
	*/ 
	private String getResultGbn(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5100 cmr5100 = new Cmr5100();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    return ParsingCommon.toJson(cmr5100.getResultGbn(acptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5100 = null;
		} 
	}
	/* Parameter 
	* String rstfile
	*/ 
	private String getFileText(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5100 cmr5100 = new Cmr5100();
		try {
		    String rstfile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rstfile"));
		    return ParsingCommon.toJson(cmr5100.getFileText(rstfile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5100 = null;
		} 
	}
	/* Parameter 
	* String acptNo,String UserId
	*/ 
	private String getResultList_Doc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5100 cmr5100 = new Cmr5100();
		try {
		    String acptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "acptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr5100.getResultList_Doc(acptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5100 = null;
		} 
	}
}
