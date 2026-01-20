package html.app.eCmr;
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

import app.eCmr.Cmr6000;

@WebServlet("/webPage/ecmr/Cmr6000Servlet")
public class Cmr6000Servlet extends HttpServlet {

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
			case "selectLocat":
				response.getWriter().write(selectLocat(jsonElement));
				break;
			case "selectConfirm":
				response.getWriter().write(selectConfirm(jsonElement));
				break;
			case "selectDaegyulETC3":
				response.getWriter().write(selectDaegyulETC3(jsonElement));
				break;
			case "selectDaegyulETC2":
				response.getWriter().write(selectDaegyulETC2(jsonElement));
				break;
			case "selectDaegyulETC":
				response.getWriter().write(selectDaegyulETC(jsonElement));
				break;
			case "selectDaegyul":
				response.getWriter().write(selectDaegyul(jsonElement));
				break;
			case "updtConfirm":
				response.getWriter().write(updtConfirm(jsonElement));
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
	* String UserId,String AcptNo
	*/ 
	private String selectLocat(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr6000.selectLocat(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String selectConfirm(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr6000.selectConfirm(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
	/* Parameter 
	* String UserId,String BaseUser
	*/ 
	private String selectDaegyulETC3(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String BaseUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseUser"));
		    return ParsingCommon.toJson(cmr6000.selectDaegyulETC3(UserId,BaseUser));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
	/* Parameter 
	* String UserId,String BaseUser
	*/ 
	private String selectDaegyulETC2(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String BaseUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseUser"));
		    return ParsingCommon.toJson(cmr6000.selectDaegyulETC2(UserId,BaseUser));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
	/* Parameter 
	* String UserId,String BaseUser
	*/ 
	private String selectDaegyulETC(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String BaseUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseUser"));
		    return ParsingCommon.toJson(cmr6000.selectDaegyulETC(UserId,BaseUser));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
	/* Parameter 
	* String UserId,String BaseUser
	*/ 
	private String selectDaegyul(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String BaseUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseUser"));
		    return ParsingCommon.toJson(cmr6000.selectDaegyul(UserId,BaseUser));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String Locat,String BlankCd,String SayuCd,String UserId,String DaeUser
	*/ 
	private String updtConfirm(JsonElement jsonElement) throws SQLException, Exception {
		Cmr6000 cmr6000 = new Cmr6000();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String Locat = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Locat"));
		    String BlankCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BlankCd"));
		    String SayuCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SayuCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String DaeUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DaeUser"));
		    return ParsingCommon.toJson(cmr6000.updtConfirm(AcptNo,Locat,BlankCd,SayuCd,UserId,DaeUser));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr6000 = null;
		} 
	}
}
