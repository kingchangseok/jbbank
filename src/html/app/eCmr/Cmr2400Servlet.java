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

import app.eCmr.Cmr2400;

@WebServlet("/webPage/ecmr/Cmr2400Servlet")
public class Cmr2400Servlet extends HttpServlet {

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
			case "get_SelectList":
				response.getWriter().write(get_SelectList(jsonElement));
				break;
			case "reqCncl":
				response.getWriter().write(reqCncl(jsonElement));
				break;
			case "cnclYn":
				response.getWriter().write(cnclYn(jsonElement));
				break;
			case "reqDelete":
				response.getWriter().write(reqDelete(jsonElement));
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
	* String pSysCd,String pJobCd,String pStateCd,String pStartDt,String pEndDt,String pReqUser,String ResultCk,String pUserId
	*/ 
	private String get_SelectList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr2400 cmr2400 = new Cmr2400();
		try {
		    String pSysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pSysCd"));
		    String pJobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pJobCd"));
		    String pStateCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStateCd"));
		    String pStartDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pStartDt"));
		    String pEndDt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pEndDt"));
		    String pReqUser = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pReqUser"));
		    String ResultCk = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ResultCk"));
		    String pUserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pUserId"));
		    return ParsingCommon.toJson(cmr2400.get_SelectList(pSysCd,pJobCd,pStateCd,pStartDt,pEndDt,pReqUser,ResultCk,pUserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr2400 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String conMsg,String ConfUsr
	*/ 
	private String reqCncl(JsonElement jsonElement) throws SQLException, Exception {
		Cmr2400 cmr2400 = new Cmr2400();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String conMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "conMsg"));
		    String ConfUsr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ConfUsr"));
		    return ParsingCommon.toJson(cmr2400.reqCncl(AcptNo,UserId,conMsg,ConfUsr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr2400 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String cnclYn(JsonElement jsonElement) throws SQLException, Exception {
		Cmr2400 cmr2400 = new Cmr2400();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr2400.cnclYn(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr2400 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId,String QryCd,String ConfUsr
	*/ 
	private String reqDelete(JsonElement jsonElement) throws SQLException, Exception {
		Cmr2400 cmr2400 = new Cmr2400();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    String ConfUsr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ConfUsr"));
		    return ParsingCommon.toJson(cmr2400.reqDelete(AcptNo,UserId,QryCd,ConfUsr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr2400 = null;
		} 
	}
}
