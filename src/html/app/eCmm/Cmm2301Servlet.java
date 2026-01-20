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

import app.eCmm.Cmm2301;

@WebServlet("/webPage/ecmm/Cmm2301Servlet")
public class Cmm2301Servlet extends HttpServlet {

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
			case "get_sql_Qry":
				response.getWriter().write(get_sql_Qry(jsonElement));
				break;
			case "get_update_Qry":
				response.getWriter().write(get_update_Qry(jsonElement));
				break;
			case "get_reply_Qry":
				response.getWriter().write(get_reply_Qry(jsonElement));
				break;
			case "get_delete_Qry":
				response.getWriter().write(get_delete_Qry(jsonElement));
				break;
			case "setDocFile":
				response.getWriter().write(setDocFile(jsonElement));
				break;
			case "removeDocFile":
				response.getWriter().write(removeDocFile(jsonElement));
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
	* String AcptNo,String Kind
	*/ 
	private String get_sql_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2301 cmm2301 = new Cmm2301();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String Kind = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Kind"));
		    return ParsingCommon.toJson(cmm2301.get_sql_Qry(AcptNo,Kind));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2301 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserID,String Title,String Txt_Body,String Kind
	*/ 
	private String get_update_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2301 cmm2301 = new Cmm2301();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String Title = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Title"));
		    String Txt_Body = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_Body"));
		    String Kind = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Kind"));
		    return ParsingCommon.toJson(cmm2301.get_update_Qry(AcptNo,UserID,Title,Txt_Body,Kind));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2301 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,int SeqNo,String UserID,String Title,String Txt_Body
	*/ 
	private String get_reply_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2301 cmm2301 = new Cmm2301();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    int SeqNo = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SeqNo")));
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String Title = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Title"));
		    String Txt_Body = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_Body"));
		    return ParsingCommon.toJson(cmm2301.get_reply_Qry(AcptNo,SeqNo,UserID,Title,Txt_Body));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2301 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String Kind
	*/ 
	private String get_delete_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2301 cmm2301 = new Cmm2301();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String Kind = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Kind"));
		    return ParsingCommon.toJson(cmm2301.get_delete_Qry(AcptNo,Kind));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2301 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList
	*/ 
	private String setDocFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2301 cmm2301 = new Cmm2301();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmm2301.setDocFile(fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2301 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> fileData
	*/ 
	private String removeDocFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmm2301 cmm2301 = new Cmm2301();
		try {
		    HashMap<String,String> fileData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "fileData"));
		    return ParsingCommon.toJson(cmm2301.removeDocFile(fileData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm2301 = null;
		} 
	}
}
