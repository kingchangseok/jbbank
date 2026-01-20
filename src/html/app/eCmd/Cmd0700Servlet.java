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

import app.eCmd.Cmd0700;

@WebServlet("/webPage/ecmd/Cmd0700Servlet")
public class Cmd0700Servlet extends HttpServlet {

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
			case "getRsrcBld":
				response.getWriter().write(getRsrcBld(jsonElement));
				break;
			case "getRsrcMod":
				response.getWriter().write(getRsrcMod(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "CMR0026_Update":
				response.getWriter().write(CMR0026_Update(jsonElement));
				break;
			case "CMR0026_Delete":
				response.getWriter().write(CMR0026_Delete(jsonElement));
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
	* String SysCd
	*/ 
	private String getRsrcBld(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0700 cmd0700 = new Cmd0700();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmd0700.getRsrcBld(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0700 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String SelMsg,String RsrcCd
	*/ 
	private String getRsrcMod(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0700 cmd0700 = new Cmd0700();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    return ParsingCommon.toJson(cmd0700.getRsrcMod(SysCd,SelMsg,RsrcCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0700 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String RsrcCd,String RsrcName
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0700 cmd0700 = new Cmd0700();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    return ParsingCommon.toJson(cmd0700.getFileList(UserId,SysCd,RsrcCd,RsrcName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0700 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> dataList
	*/ 
	private String CMR0026_Update(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0700 cmd0700 = new Cmd0700();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> dataList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "dataList"));
		    return ParsingCommon.toJson(cmd0700.CMR0026_Update(UserId,dataList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0700 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> dataList
	*/ 
	private String CMR0026_Delete(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0700 cmd0700 = new Cmd0700();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> dataList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "dataList"));
		    return ParsingCommon.toJson(cmd0700.CMR0026_Delete(UserId,dataList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0700 = null;
		} 
	}
}
