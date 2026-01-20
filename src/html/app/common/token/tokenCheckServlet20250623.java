package html.app.common.token;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import com.ecams.common.base.Encryptor;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import html.app.common.ParsingCommon;
import html.app.common.security.JwtProperties;
import html.app.common.token.model.TokenVo;
import html.app.common.token.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//import com.auth0.jwt.JWTSigner;

/* 로컬에서 테스트시
 * has been blocked by CORS policy: Response to preflight request doesn't pass access control check:
 * No 'Access-Control-Allow-Origin' header is present on the requested resource.
 * 개발자 도구 해당 에러가 뜰 경우 기동하는 톰켓 web.xml 에서
 * <param-name>cors.allowed.headers</param-name> 에 ,Authorization 를 추가하고 재기동.
 */
@WebServlet("/webPage/tokenCheckServlet")
public class tokenCheckServlet20250623 extends HttpServlet{
	private static final long serialVersionUID = 1L;
	final Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	final Gson gson = new Gson();
	final TokenService tokenService = new TokenService();

	@Override
    @PostConstruct
	public void init() {
        //secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECURITY_SECRET_KEY.getBytes());
		//취약점 수정
		TokenSecretKey.setSecretKey(Base64.getEncoder().encodeToString(JwtProperties.SECURITY_SECRET_KEY.getBytes()));
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		doPost(req, resp);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(ParsingCommon.getJsonStr(request));
		String requestType	= ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "requestType") );

		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");

			switch (requestType) {
				case "CREATE_TOKEN" :
					response.getWriter().write( createToken(jsonElement, request, response));
					break;
				case "CHECK_TOKEN" :
					response.getWriter().write( checkToken(jsonElement, request));
					break;
				default :
					break;
			}
		} catch (SQLException e) {
			e.getStackTrace();
		}  catch (Exception e) {
			e.getStackTrace();
		} finally {
		}
	}

	//최초로그인 token 신규생성
	private String createToken(JsonElement jsonElement, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		TokenVo tokenVo = new TokenVo();
    	Date now = new Date();
    	Date validity = new Date(now.getTime() + JwtProperties.SECURITY_TOKEN_EXPIRE);

    	ecamsLogger.error("1++++++++now ["+now+"], validity ["+validity+"]");

		String userId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "user") );//로그인한 사용자ID
		ecamsLogger.error("1++++++++userId ["+userId+"]");
		
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
        Connection        conn        = null;
		ConnectionContext connectionContext = new ConnectionResource();
		ResultSet         rs          = null;
		try { 
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("SELECT a.CM_USERID			\n");
			strQuery.append("  FROM cmm0040 a			\n");
			strQuery.append(" WHERE a.CM_USERID = ?		\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()){
				//ecamsLogger.error(rs.getString("CM_CPASSWD"));
            	userId = rs.getString("CM_USERID");
            }
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## LoginManager.getUserID2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## LoginManager.getUserID2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## LoginManager.getUserID2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## LoginManager.getUserID2() Exception END ##");				
			throw exception;
		}

		String clientIp = tokenService.getClientIP(req);
    	ecamsLogger.error("++++++++clientIp ["+clientIp+"]");

    	//token 정보에 id, ip정보 관리
    	Map<String, Object> claims2 = new HashMap<String, Object>();
    	claims2.put("id", userId);
    	try {
    		/*
    		 * IPv6 -> IPv4 형태로 변경하고자 하는경우
    		 * [참고] https://jaehun2841.github.io/2018/08/10/2018-08-10-httprequest-client-ip/#httpservletrequest에서-ip-구하기
    		 *
    		 * TOMCAT catalina.bat(sh) JAVA_OPTS -Djava.net.preferIPv4Stack=true
    		 * ex) set JAVA_OPTS=%JAVA_OPTS% %LOGGING_CONFIG% -Djava.net.preferIPv4Stack=true
    		 *
    		 * Local Test환경
    		 * (spring-ap프로젝트) Run As -> Run Configurations -> Arguments -> VM arguments 에 입력
    		 * -Djava.net.preferIPv4Stack=true
    		 */

    		//ip암호화해서 저장
    		claims2.put("ip", Encryptor.instance().strGetEncrypt(clientIp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
			tokenVo.setMessage("FAIL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
			tokenVo.setMessage("FAIL");
		}

    	String token = Jwts.builder().setClaims(claims2).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, TokenSecretKey.getSecretKey()).compact(); //취약점 수정

    	/* token정보 userid만 관리할 경우
		Claims claims = Jwts.claims().setSubject(userId);

    	//jjwt 이용
        String token = Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
        				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
        */
    	ecamsLogger.error("++++++++token ["+token+"]");

        if (!StringUtils.isEmpty(token)){
	        //HttpServletResponse response = (HttpServletResponse) res;
			res.setHeader(JwtProperties.SECURITY_TOKEN_HEADER, token);
			tokenVo.setMessage("OK");
			tokenVo.setToken(token);
        } else {
			tokenVo.setMessage("FAIL");
        }

		return gson.toJson(tokenVo);
	}

	//데이타이동 token 정보 확인
	private String checkToken(JsonElement jsonElement, HttpServletRequest request) throws SQLException, Exception {
		TokenVo tokenVo = new TokenVo();

		String userId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "user") );//로그인한 사용자ID
		if (null == userId || "".equals(userId)) tokenVo.setMessage("TIMEOUT");
		else {
			//header 에서 token정보 받아오기
			String jwtStr = tokenService.getToken(request);
			if (!StringUtils.isEmpty(jwtStr)){
				try {
					//token에 포함된 userid 정보 가져오기
					try {
						String user = tokenService.getUserId(jwtStr);
						if (null == user || "".equals(user)) {
							tokenVo.setToken(tokenService.setTokenValidity(user, jwtStr, request));
							tokenVo.setMessage("TIMEOUT");
						} else {
							if (!userId.equals(user)) {
								tokenVo.setMessage("DISAGREE");
							} else {
								String tokenIp = tokenService.getUserIp(jwtStr);
								String localIp = tokenService.getClientIP(request);

								if (null == tokenIp || "".equals(tokenIp) || null == localIp || "".equals(localIp)) {
									tokenVo.setMessage("TIMEOUT");
								} else {
									if (!localIp.equals(tokenIp)) {
										tokenVo.setMessage("DISAGREE");
									} else {
										//token 유효성체크
										if (tokenService.validateToken(jwtStr)) {
											tokenVo.setMessage("OK");

											tokenVo.setToken(tokenService.setTokenValidity(user, jwtStr, request));
										} else {
											tokenVo.setMessage("EXPIRED");
										}
									}
								}
							}
						}
					} catch (RuntimeException e) { //취약점 수정
						tokenVo.setMessage("TIMEOUT");
						e.getStackTrace();
						e.getStackTrace();
					} catch (Exception e) {
						tokenVo.setMessage("TIMEOUT");
						e.getStackTrace();
						e.getStackTrace();
					}
				} catch (ExpiredJwtException e) {
					//token 유효성체크 중 expired 오류발생
					tokenVo.setMessage("EXPIRED");
					e.getStackTrace();
				} catch (Exception e) {
					//기타오류발생
					tokenVo.setMessage("FAIL");
					e.getStackTrace();
					e.getStackTrace();
				}
			} else {
				tokenVo.setMessage("TIMEOUT");
			}
		}

		return gson.toJson(tokenVo);
	}
}