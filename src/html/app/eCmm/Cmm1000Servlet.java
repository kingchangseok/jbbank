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

import app.eCmm.Cmm1000;

@WebServlet("/webPage/ecmm/Cmm1000Servlet")
public class Cmm1000Servlet extends HttpServlet {

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
			case "getHoliDay":
				response.getWriter().write(getHoliDay(jsonElement));
				break;
			case "getHoliDayAll":
				response.getWriter().write(getHoliDayAll(jsonElement));
				break;
			case "chkHoliDay":
				response.getWriter().write(chkHoliDay(jsonElement));
				break;
			case "delHoliday":
				response.getWriter().write(delHoliday(jsonElement));
				break;
			case "addHoliday":
				response.getWriter().write(addHoliday(jsonElement));
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
	* String nyear
	*/ 
	private String getHoliDay(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1000 cmm1000 = new Cmm1000();
		try {
		    String nyear = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "nyear"));
		    return ParsingCommon.toJson(cmm1000.getHoliDay(nyear));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1000 = null;
		} 
	}
	/* Parameter 
	* String startDay,String endDay
	*/ 
	private String getHoliDayAll(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1000 cmm1000 = new Cmm1000();
		try {
		    String startDay = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "startDay"));
		    String endDay = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "endDay"));
		    return ParsingCommon.toJson(cmm1000.getHoliDayAll(startDay,endDay));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1000 = null;
		} 
	}
	/* Parameter 
	* String nDate
	*/ 
	private String chkHoliDay(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1000 cmm1000 = new Cmm1000();
		try {
		    String nDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "nDate"));
		    return ParsingCommon.toJson(cmm1000.chkHoliDay(nDate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1000 = null;
		} 
	}
	/* Parameter 
	* String nDate
	*/ 
	private String delHoliday(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1000 cmm1000 = new Cmm1000();
		try {
		    String nDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "nDate"));
		    return ParsingCommon.toJson(cmm1000.delHoliday(nDate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1000 = null;
		} 
	}
	/* Parameter 
	* String nDate,String holigb,String holi,int ntype
	*/ 
	private String addHoliday(JsonElement jsonElement) throws SQLException, Exception {
		Cmm1000 cmm1000 = new Cmm1000();
		try {
		    String nDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "nDate"));
		    String holigb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "holigb"));
		    String holi = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "holi"));
		    int ntype = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ntype")));
		    return ParsingCommon.toJson(cmm1000.addHoliday(nDate,holigb,holi,ntype));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm1000 = null;
		} 
	}
}
