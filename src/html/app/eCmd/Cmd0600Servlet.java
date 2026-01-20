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

import app.eCmd.Cmd0600;

@WebServlet("/webPage/ecmd/Cmd0600Servlet")
public class Cmd0600Servlet extends HttpServlet {

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
			case "getRsrcInfo":
				response.getWriter().write(getRsrcInfo(jsonElement));
				break;
			case "getSqlQry":
				response.getWriter().write(getSqlQry(jsonElement));
				break;
			case "getDir":
				response.getWriter().write(getDir(jsonElement));
				break;
			case "setRsrcInfo":
				response.getWriter().write(setRsrcInfo(jsonElement));
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
	* String Syscd,String SelMsg
	*/ 
	private String getRsrcInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0600 cmd0600 = new Cmd0600();
		try {
		    String Syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Syscd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0600.getRsrcInfo(Syscd,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0600 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getSqlQry(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0600 cmd0600 = new Cmd0600();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmd0600.getSqlQry(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0600 = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SecuYn,String RsrcCd,String JobCd,String SelMsg
	*/ 
	private String getDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0600 cmd0600 = new Cmd0600();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0600.getDir(UserID,SysCd,SecuYn,RsrcCd,JobCd,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0600 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> dataList
	*/ 
	private String setRsrcInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0600 cmd0600 = new Cmd0600();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> dataList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "dataList"));
		    return ParsingCommon.toJson(cmd0600.setRsrcInfo(UserId,dataList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0600 = null;
		} 
	}
}
