package html.app.eCmm;
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

import app.eCmm.Cmm0300_Blank;

@WebServlet("/webPage/ecmm/Cmm0300_BlankServlet")
public class Cmm0300_BlankServlet extends HttpServlet {

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
			case "getBlankList":
				response.getWriter().write(getBlankList(jsonElement));
				break;
			case "blankUpdt":
				response.getWriter().write(blankUpdt(jsonElement));
				break;
			case "blankClose":
				response.getWriter().write(blankClose(jsonElement));
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
	* String GbnCd,String PosCd
	*/ 
	private String getBlankList(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0300_Blank cmm0300_blank = new Cmm0300_Blank();
		try {
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    String PosCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PosCd"));
		    return ParsingCommon.toJson(cmm0300_blank.getBlankList(GbnCd,PosCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0300_blank = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String blankUpdt(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0300_Blank cmm0300_blank = new Cmm0300_Blank();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0300_blank.blankUpdt(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0300_blank = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String blankClose(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0300_Blank cmm0300_blank = new Cmm0300_Blank();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmm0300_blank.blankClose(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0300_blank = null;
		} 
	}
}
