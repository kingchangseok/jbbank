package html.app.eCmd;
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

import app.eCmd.Cmd0900;

@WebServlet("/webPage/ecmd/Cmd0900Servlet")
public class Cmd0900Servlet extends HttpServlet {

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
			case "getSysInfo":
				response.getWriter().write(getSysInfo(jsonElement));
				break;
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "getModList":
				response.getWriter().write(getModList(jsonElement));
				break;
			case "getRelatList":
				response.getWriter().write(getRelatList(jsonElement));
				break;
			case "getProgRelat":
				response.getWriter().write(getProgRelat(jsonElement));
				break;
			case "updtRelat":
				response.getWriter().write(updtRelat(jsonElement));
				break;
			case "delRelat":
				response.getWriter().write(delRelat(jsonElement));
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
	* String UserId,String SecuYn,String SelMsg
	*/ 
	private String getSysInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0900.getSysInfo(UserId,SecuYn,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String ProgId
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ProgId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgId"));
		    return ParsingCommon.toJson(cmd0900.getProgList(UserId,SysCd,ProgId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String ProgId
	*/ 
	private String getModList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ProgId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgId"));
		    return ParsingCommon.toJson(cmd0900.getModList(UserId,SysCd,ProgId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String GbnCd,String ProgId
	*/ 
	private String getRelatList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    String ProgId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgId"));
		    return ParsingCommon.toJson(cmd0900.getRelatList(UserId,SysCd,GbnCd,ProgId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,ArrayList<HashMap<String,String>> progList
	*/ 
	private String getProgRelat(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
	   	    ArrayList<HashMap<String,String>> progList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "progList"));
		    return ParsingCommon.toJson(cmd0900.getProgRelat(UserId,SysCd,progList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,ArrayList<HashMap<String,String>> progList
	*/ 
	private String updtRelat(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
	   	    ArrayList<HashMap<String,String>> progList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "progList"));
		    return ParsingCommon.toJson(cmd0900.updtRelat(UserId,SysCd,progList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> progList
	*/ 
	private String delRelat(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0900 cmd0900 = new Cmd0900();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> progList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "progList"));
		    return ParsingCommon.toJson(cmd0900.delRelat(UserId,progList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0900 = null;
		} 
	}
}
