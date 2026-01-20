package html.app.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ecams.common.base.Encryptor;
import com.ecams.service.list.User;
import com.ecams.service.msg.dao.MsgDAO;
import com.ecams.util.exception.ErrorHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import app.common.MenuList;
import app.common.UserInfo;
import html.app.common.ParsingCommon;
import html.app.common.security.JwtProperties;
import html.app.common.token.TokenSecretKey;
import html.app.common.token.model.TokenVo;
import html.app.common.token.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@WebServlet("/webPage/main/eCAMSBaseServlet")
public class eCAMSBaseServlet20250623 extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

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

		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");

			switch (requestType) {
			case "MenuList":
				response.getWriter().write( getMenu(jsonElement) );
				break;
			case "UserInfo":
				response.getWriter().write( getUserInfo(jsonElement) );
				break;
			case "GETSESSIONUSERDATA":
				response.getWriter().write( getSessionUserData(jsonElement, request) );
				break;
			case "LOG_OUT":
				response.getWriter().write( logOut(jsonElement) );
				break;
			default:
				throw new Exception("Servlet Function 미 존재");
			}
		} catch(SQLException e1) {
			ErrorHandler.handleError(request, response, e1);
		} catch(Exception e2) {
			ErrorHandler.handleError(request, response, e2);
		} finally {
			requestType = null;
		}
	}

	private String getSessionUserData(JsonElement jsonElement, HttpServletRequest request) throws SQLException, Exception {
		TokenService tokenService = new TokenService();
		//LoginManager loginManager = LoginManager.getInstance();
		UserInfo userinfo = new UserInfo();
		//String sessionID = ecMASMainDto.getSessionID();

		HashMap<String, String> userInfoMap = new HashMap<String, String>();

		String UserID = "";
		String jwtStr = tokenService.getToken(request);

		if (null == jwtStr || "".equals(jwtStr)) {
			String userId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "user") );
			if (userId == null || "".equals(userId) ) {
				userInfoMap.put("userId", 	"undefined");
			} else {
				TokenVo tokenVo = new TokenVo();
		    	Date now = new Date();
		    	Date validity = new Date(now.getTime() + JwtProperties.SECURITY_TOKEN_EXPIRE);
		    	
				String clientIp = tokenService.getClientIP(request);
		    	Map<String, Object> claims2 = new HashMap<String, Object>();
		    	claims2.put("id", userId);
		    	try {
		    		claims2.put("ip", Encryptor.instance().strGetEncrypt(clientIp));
				} catch (IOException e) { //�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕
					// TODO Auto-generated catch block
					e.getStackTrace();
					tokenVo.setMessage("FAIL");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.getStackTrace();
					tokenVo.setMessage("FAIL");
				}
		    	TokenSecretKey.setSecretKey(Base64.getEncoder().encodeToString(JwtProperties.SECURITY_SECRET_KEY.getBytes()));
		    	String token = Jwts.builder().setClaims(claims2).setIssuedAt(now).setExpiration(validity)
						.signWith(SignatureAlgorithm.HS256, TokenSecretKey.getSecretKey()).compact(); //�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕

		        if (!StringUtils.isEmpty(token)){
		        	jwtStr = token;
		        	UserID = userId;
					userInfoMap.put("userId", 	userId);
		        }
			}
		} else {
			try {
				if (tokenService.validateToken(jwtStr)) {
					UserID = tokenService.getUserId(jwtStr);
					if (null == UserID || "".equals(UserID)) {
						userInfoMap.put("userId", 	"undefined");
					} else {
						userInfoMap.put("userId", 	UserID);
					}
				} else {
					userInfoMap.put("userId", 	"undefined");
				}
			} catch (ExpiredJwtException e) {
				userInfoMap.put("userId", 	"undefined");
			}
		}

		if (null != jwtStr && !"".equals(jwtStr) && null != UserID && !"".equals(UserID)) {
	    	userInfoMap.put("token", jwtStr);
			HashMap<String,String> userData = (HashMap<String, String>) userinfo.getUserInfo(UserID)[0];
		    if (userData != null) {
		    	userInfoMap.put("position", userData.get("cm_position"));//吏곸쐞�젙蹂�
				userInfoMap.put("userName", userData.get("cm_username"));
				userInfoMap.put("deptName",userData.get("teamname"));
				userInfoMap.put("deptCd",userData.get("cm_project"));

				if ("1".equals(userData.get("cm_admin"))) {
					userInfoMap.put("adminYN",  "true");
				} else {
					userInfoMap.put("adminYN",  "false");
				}
				
				if (null == userData.get("cm_manid") || "".equals(userData.get("cm_manid"))) {
					userInfoMap.put("manYn",  "N");
				} else {
					userInfoMap.put("manYn",  userData.get("cm_manid"));
				}
				userInfoMap.put("rgtcd",userData.get("rgtcd"));
		    } else {
		    	userInfoMap.put("position", "");
		    }
		    userData = null;
		}
		return ParsingCommon.toJson(userInfoMap);
	}

	private String getMenu(JsonElement jsonElement) throws SQLException, Exception {
		MenuList menuList = new MenuList();
		String userId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userId") );
		//String gbn = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "gbn") );

		//return ParsingCommon.toJson(menuList.SelectMenuList(userId));
		return ParsingCommon.toJson(menuList.secuMenuList_html(userId));
	}

	private String getUserInfo(JsonElement jsonElement) throws SQLException, Exception {
		UserInfo userinfo = new UserInfo();
		String userId = null;
		userId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "UserId") );
		return ParsingCommon.toJson(userinfo.isAdmin(userId));
	}

	private String logOut(JsonElement jsonElement) throws SQLException, Exception {
		User user = new User();
		MsgDAO msgDao = new MsgDAO();
		String userId = null;
		userId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "UserId") );
		//if(msgDao.updateLogOut(userId) == 1) user.setDelUser(userId);
		user.setDelUser(userId);
		return ParsingCommon.toJson( "LOG_OUT" );
	}
}