package html.app.eCmm;
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

import app.eCmm.Cmm2201;

@WebServlet("/webPage/ecmm/Cmm2201Servlet")
public class Cmm2201Servlet extends HttpServlet {

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
			case "sql_Qry":
				response.getWriter().write(sql_Qry(jsonElement));
				break;
			case "update_Qry":
				response.getWriter().write(update_Qry(jsonElement));
				break;
			case "delete_Qry":
				response.getWriter().write(delete_Qry(jsonElement));
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
	* String AcptNo,String RsrcName
	*/ 
	private String sql_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2201 cmm2201 = new Cmm2201();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    return ParsingCommon.toJson(cmm2201.sql_Qry(AcptNo,RsrcName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2201 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserID,String Title,String Txt_Body,String attfile
	*/ 
	private String update_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2201 cmm2201 = new Cmm2201();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String Title = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Title"));
		    String Txt_Body = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_Body"));
		    String attfile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "attfile"));
		    return ParsingCommon.toJson(cmm2201.update_Qry(AcptNo,UserID,Title,Txt_Body,attfile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2201 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserID,String Title,String Txt_Body,String attfile
	*/ 
	private String delete_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2201 cmm2201 = new Cmm2201();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String Title = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Title"));
		    String Txt_Body = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_Body"));
		    String attfile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "attfile"));
		    return ParsingCommon.toJson(cmm2201.delete_Qry(AcptNo,UserID,Title,Txt_Body,attfile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2201 = null;
		} 
	}
}
