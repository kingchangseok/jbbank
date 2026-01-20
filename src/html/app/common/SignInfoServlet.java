package html.app.common;
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

import app.common.SignInfo;

@WebServlet("/webPage/common/SignInfoServlet")
public class SignInfoServlet extends HttpServlet {

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
			case "getMyConf":
				response.getWriter().write(getMyConf(jsonElement));
				break;
			case "getSignInfo":
				response.getWriter().write(getSignInfo(jsonElement));
				break;
			case "getSignLst":
				response.getWriter().write(getSignLst(jsonElement));
				break;
			case "getDeptUser":
				response.getWriter().write(getDeptUser(jsonElement));
				break;
			case "getConfTime":
				response.getWriter().write(getConfTime(jsonElement));
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
	* String UserID,String SysCd,String JobCd
	*/ 
	private String getMyConf(JsonElement jsonElement) throws SQLException, Exception {
		SignInfo signinfo = new SignInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    return ParsingCommon.toJson(signinfo.getMyConf(UserID,SysCd,JobCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    signinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String SignNm,String RgtCd,String PosCd
	*/ 
	private String getSignInfo(JsonElement jsonElement) throws SQLException, Exception {
		SignInfo signinfo = new SignInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SignNm = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SignNm"));
		    String RgtCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RgtCd"));
		    String PosCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PosCd"));
		    return ParsingCommon.toJson(signinfo.getSignInfo(UserID,SignNm,RgtCd,PosCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    signinfo = null;
		} 
	}
	/* Parameter 
	* String UserId,String RgtCd,String PosCd
	*/ 
	private String getSignLst(JsonElement jsonElement) throws SQLException, Exception {
		SignInfo signinfo = new SignInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String RgtCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RgtCd"));
		    String PosCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PosCd"));
		    return ParsingCommon.toJson(signinfo.getSignLst(UserId,RgtCd,PosCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    signinfo = null;
		} 
	}
	/* Parameter 
	* String AddCd,String AddName,String DeptCd,String DeptName,String UserName,String SysCd,String JobCd
	*/ 
	private String getDeptUser(JsonElement jsonElement) throws SQLException, Exception {
		SignInfo signinfo = new SignInfo();
		try {
		    String AddCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AddCd"));
		    String AddName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AddName"));
		    String DeptCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeptCd"));
		    String DeptName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeptName"));
		    String UserName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserName"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    return ParsingCommon.toJson(signinfo.getDeptUser(AddCd,AddName,DeptCd,DeptName,UserName,SysCd,JobCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    signinfo = null;
		} 
	}
	/* Parameter 
	* String RgtCd
	*/ 
	private String getConfTime(JsonElement jsonElement) throws SQLException, Exception {
		SignInfo signinfo = new SignInfo();
		try {
		    String RgtCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RgtCd"));
		    return ParsingCommon.toJson(signinfo.getConfTime(RgtCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    signinfo = null;
		} 
	}
}
