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

import app.common.UserInfo;

@WebServlet("/webPage/common/UserInfoServlet")
public class UserInfoServlet extends HttpServlet {

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
			case "getUserInfo":
				response.getWriter().write(getUserInfo(jsonElement));
				break;
			case "getPMOInfo":
				response.getWriter().write(getPMOInfo(jsonElement));
				break;
			case "getCheckInfo":
				response.getWriter().write(getCheckInfo(jsonElement));
				break;
			case "getRGTCDInfo":
				response.getWriter().write(getRGTCDInfo(jsonElement));
				break;
			case "getSecuInfo":
				response.getWriter().write(getSecuInfo(jsonElement));
				break;
			case "getSecuList":
				response.getWriter().write(getSecuList(jsonElement));
				break;
			case "isAdmin":
				response.getWriter().write(isAdmin(jsonElement));
				break;
			case "getUserRGTCD":
				response.getWriter().write(getUserRGTCD(jsonElement));
				break;
			case "getUserRGTCDList":
				response.getWriter().write(getUserRGTCDList(jsonElement));
				break;
			case "getUserB":
				response.getWriter().write(getUserB(jsonElement));
				break;
			case "getOtherUser":
				response.getWriter().write(getOtherUser(jsonElement));
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
	* String UserID
	*/ 
	private String getUserInfo(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    return ParsingCommon.toJson(userinfo.getUserInfo(UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String Sv_UserID
	*/ 
	private String getPMOInfo(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String Sv_UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_UserID"));
		    return ParsingCommon.toJson(userinfo.getPMOInfo(Sv_UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String Sv_UserID
	*/ 
	private String getCheckInfo(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String Sv_UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_UserID"));
		    return ParsingCommon.toJson(userinfo.getCheckInfo(Sv_UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String Sv_UserID
	*/ 
	private String getRGTCDInfo(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String Sv_UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_UserID"));
		    return ParsingCommon.toJson(userinfo.getRGTCDInfo(Sv_UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String Sv_UserID
	*/ 
	private String getSecuInfo(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String Sv_UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_UserID"));
		    return ParsingCommon.toJson(userinfo.getSecuInfo(Sv_UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String Sv_UserID
	*/ 
	private String getSecuList(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String Sv_UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_UserID"));
		    return ParsingCommon.toJson(userinfo.getSecuList(Sv_UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String UserID
	*/ 
	private String isAdmin(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    return ParsingCommon.toJson(userinfo.isAdmin(UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String RGTCD,String closeYn
	*/ 
	private String getUserRGTCD(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String RGTCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RGTCD"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    return ParsingCommon.toJson(userinfo.getUserRGTCD(UserID,RGTCD,closeYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String closeYn
	*/ 
	private String getUserRGTCDList(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    return ParsingCommon.toJson(userinfo.getUserRGTCDList(UserID,closeYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    userinfo = null;
		} 
	}
	/* Parameter 
	 * String UserID,String closeYn
	 */ 
	private String getUserB(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
			String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
			return ParsingCommon.toJson(userinfo.getUserB(UserID));
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			userinfo = null;
		} 
	}
	/* Parameter 
	 */ 
	private String getOtherUser(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		try {
			return ParsingCommon.toJson(userinfo.getOtherUser());
		} catch (IOException e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			userinfo = null;
		} 
	}
}