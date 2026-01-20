package html.app.common.token.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import com.ecams.common.base.Encryptor;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.google.gson.JsonElement;

import html.app.common.ParsingCommon;
import html.app.common.security.JwtProperties;
import html.app.common.token.TokenSecretKey;
import html.app.common.token.model.TokenVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenService {

	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	private String secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECURITY_SECRET_KEY.getBytes());

	/*
	 * �뜝�룞�삕�깘
    @PostConstruct
    protected void init() {
	    secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECURITY_SECRET_KEY.getBytes());
    }
    */

	public String getToken(HttpServletRequest req){
	    String bearerToken = req.getHeader(JwtProperties.SECURITY_TOKEN_HEADER);
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        return bearerToken.substring(7, bearerToken.length());
	    }
	    return null;
	}

    public String getUserId(String token) {
    	try {
	    	Map<String, Object> getClaims2 = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	    	String id = getClaims2.get("id").toString();
	    	//id = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	    	return id;
    	} catch (RuntimeException e) { //�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕
    		e.getStackTrace();
    		e.getCause();
    		return null;
    	} catch (Exception e) {
    		e.getStackTrace();
    		e.getCause();
    		return null;
    	}
    }

    public String getUserIp(String token) {
    	Map<String, Object> getClaims2 = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    	String ip = "";
    	try {
    		ip = Encryptor.instance().strGetDecrypt(getClaims2.get("ip").toString());
		} catch (RuntimeException e) { //�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕
			// TODO Auto-generated catch block
			e.getStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
    	return ip;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            //ecamsLogger.error("3++++++++now ["+(new Date())+"], validity ["+claims.getBody().getExpiration()+"]");

            if (claims.getBody().getExpiration().before(new Date())) {
	        	Date now = new Date();
	        	Date validity = new Date(now.getTime() + JwtProperties.SECURITY_TOKEN_EXPIRE);
	        	Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().setIssuedAt(now).setExpiration(validity);

	        	//�꽭�뀡 �옄�룞�뿰�옣 �빐�떖�씪怨� �븿.
	        	//return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String setTokenValidity(String userId, String token, HttpServletRequest req) {
    	Date now = new Date();
    	Date validity = new Date(now.getTime() + JwtProperties.SECURITY_TOKEN_EXPIRE);
    	//ecamsLogger.error("2++++++++now ["+now+"], validity ["+validity+"]");
    	Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().setIssuedAt(now).setExpiration(validity);
    	/*
    	Claims claims = Jwts.claims().setSubject(userId);
    	return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    	*/

    	String clientIp = getClientIP(req);

    	//token �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 id, ip�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕
    	Map<String, Object> claims = new HashMap<String, Object>();
    	claims.put("id", userId);
    	try {
    		//ip�뜝�룞�삕�샇�솕�뜝�뙏�눦�삕 �뜝�룞�삕�뜝�룞�삕
    		claims.put("ip", Encryptor.instance().strGetEncrypt(clientIp));
		} catch (RuntimeException e) { //�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕
			// TODO Auto-generated catch block
			e.getStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}

    	return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity).signWith(SignatureAlgorithm.HS256, secretKey).compact();

    }

    public String getClientIP(HttpServletRequest request){

		String clientIp = request.getHeader("HTTP_X_FORWARED_FOR");

		if (StringUtils.isEmpty(clientIp)|| "unknown".equalsIgnoreCase(clientIp)) {
			//Proxy �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝占�
	        clientIp = request.getHeader("Proxy-Client-IP");
	    }
	    if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
	    	//Weblogic �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝占�
	        clientIp = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
	        clientIp = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
	        clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
	        clientIp = request.getRemoteAddr();
	    }

		return clientIp;
	}
    
	//최초로그인 token 신규생성
	//private String createToken(JsonElement jsonElement, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
    public String createToken(String userId, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {		
    	Date now = new Date();
    	Date validity = new Date(now.getTime() + JwtProperties.SECURITY_TOKEN_EXPIRE);
    	String retToken = "";
    	ecamsLogger.error("1++++++++now ["+now+"], validity ["+validity+"]");

		String clientIp = getClientIP(req);
    	ecamsLogger.error("++++++++clientIp ["+clientIp+"]");

    	//token 정보에 id, ip정보 관리
    	Map<String, Object> claims2 = new HashMap<String, Object>();
    	claims2.put("id", userId);
    	try {
    		claims2.put("ip", Encryptor.instance().strGetEncrypt(clientIp));
    		
    		TokenSecretKey.setSecretKey(Base64.getEncoder().encodeToString(JwtProperties.SECURITY_SECRET_KEY.getBytes()));
        	String token = Jwts.builder().setClaims(claims2).setIssuedAt(now).setExpiration(validity)
    				.signWith(SignatureAlgorithm.HS256, TokenSecretKey.getSecretKey()).compact(); //취약점 수정

//        	ecamsLogger.error("TokenService createToken() ["+token+"]");
            if (!StringUtils.isEmpty(token)){
    	        //HttpServletResponse response = (HttpServletResponse) res;
    			res.setHeader(JwtProperties.SECURITY_TOKEN_HEADER, token);
    			retToken = token;
            } else {
    			retToken = "FAIL";
            	ecamsLogger.error("TokenService createToken() FAIL1");
            }
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
			retToken = "FAIL";
        	ecamsLogger.error("TokenService createToken() FAIL2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
			retToken = "FAIL";
        	ecamsLogger.error("TokenService createToken() FAIL3");
		}
    	return retToken;
	}
}
