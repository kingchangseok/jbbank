package html.app.login;
import java.io.IOException;
import java.security.PrivateKey;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;

import com.ecams.common.base.RSA;
import com.ecams.common.logger.EcamsLogger;
import com.ecams.service.list.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import html.app.common.ParsingCommon;

@WebServlet("/webPage/login/Login")
public class LoginServlet20250623 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final Gson gson = new Gson();
	final LoginManager loginManager = LoginManager.getInstance();
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		doPost(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*HashMap paramMap = ParsingCommon.reqParamToMap(request); 
		String requestType = (String)paramMap.get("requestType");*/
		
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(ParsingCommon.getJsonStr(request));
		String requestType	= ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "requestType") );
		ecamsLogger.error("requestType="+requestType);
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			
			switch (requestType) {
				case "ISVALIDLOGIN" :
					response.getWriter().write( isValidLoginUser(jsonElement, request) );
					break;
				case "SETSESSION" :
					response.getWriter().write( getUserName(jsonElement, request)  );
					break;	
				case "UPDATELOGINIP" : 
					updateLoginIp(jsonElement, request);
					break;
				case "sessionCk" :
					response.getWriter().write( sessionCk(jsonElement, request) );
					break;
				default:
					break;
			}
		} catch(SQLException e1) {
			e1.getStackTrace();
		} catch(Exception e2) {
			e2.getStackTrace();			
		} finally {
	         requestType = null;
		}
		
	}
	
	private String isValidLoginUser(JsonElement jsonElement, HttpServletRequest request) throws Exception {
		try {
			String userId  = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userId") );
			String userPwd = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userPwd") );
			String userPwd2 = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userPwd2") );
			String gnb = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "gnb") );
			String sso = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "sso") );
			
			String decPw = "";
			if("dev".equals(gnb)) {
				decPw = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userPwdDev") );
			} else if( "true".equals(sso) ) {
				decPw = "SSO";
			} else {
				//decPw = RSA.decryptRsa((PrivateKey) request.getSession().getAttribute("_ecams_encry_key_"), userPwd);//�븫�샇�솕�맂 鍮꾨�踰덊샇瑜� 蹂듯샇�솕�븳�떎.
				try {
					decPw = RSA.decryptRsa((PrivateKey) request.getSession().getAttribute("_ecams_encry_key2_"), userPwd2);
				}catch(Exception e) {
					decPw = RSA.decryptRsa((PrivateKey) request.getSession().getAttribute("_ecams_encry_key1_"), userPwd);
				}
			}
			
			if (null == decPw || "".equals(decPw)) {
				return "ENCERROR2";
			}
			ecamsLogger.error("isValidLoginUser="+userId+","+decPw);
			
			return gson.toJson(loginManager.isValid(userId, decPw));
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## LoginServlet.isValidLoginUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## LoginServlet.isValidLoginUser() Exception END ##");				
			return "ERROR";
		}finally{
		}
	}
	
	private String getUserName(JsonElement jsonElement, HttpServletRequest request) throws SQLException, Exception {
		String userId  = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userId") );
		HttpSession session = request.getSession();
		
		session.setAttribute("userId", userId);
		session.setAttribute("userName", loginManager.getUserName(userId));
		loginManager.setSession(session, userId);
		
		return gson.toJson( session.getId() );
	}
	
	private void updateLoginIp(JsonElement jsonElement, HttpServletRequest request) throws SQLException, Exception {
		String userId  = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userId") );
		String IpAddr  = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "IpAddr") );
		String Url	   = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "Url") );
		
		loginManager.updateLoginIp(userId, IpAddr, Url);
	}
	
	private String sessionCk(JsonElement jsonElement, HttpServletRequest request) throws SQLException, Exception {
		HttpSession session = request.getSession();
		RSA rsa = RSA.getEncKey();
		session.setAttribute("publicKeyModulus", rsa.getPublicKeyModulus());
		session.setAttribute("publicKeyExponent", rsa.getPublicKeyExponent());
		session.setAttribute("_ecams_encry_key2_", rsa.getPrivateKey());
		return gson.toJson(session.getAttribute("publicKeyModulus")+","+session.getAttribute("publicKeyExponent"));
	}
}
