package html.app.eCmr;
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

import app.eCmr.Cmr0750;

@WebServlet("/webPage/ecmr/Cmr0750Servlet")
public class Cmr0750Servlet extends HttpServlet {

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
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
				break;
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "getRstList":
				response.getWriter().write(getRstList(jsonElement));
				break;
			case "svrProc":
				response.getWriter().write(svrProc(jsonElement));
				break;
			case "progCncl":
				response.getWriter().write(progCncl(jsonElement));
				break;
			case "confLocat":
				response.getWriter().write(confLocat(jsonElement));
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
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0750 cmr0750 = new Cmr0750();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0750.getReqList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0750 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo,String chkYn,String UpdtSw
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0750 cmr0750 = new Cmr0750();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String chkYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkYn"));
		    String UpdtSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UpdtSw"));
		    return ParsingCommon.toJson(cmr0750.getProgList(UserId,AcptNo,chkYn,UpdtSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0750 = null;
		} 
	}
	/* Parameter 
	* String UserId,String AcptNo
	*/ 
	private String getRstList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0750 cmr0750 = new Cmr0750();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr0750.getRstList(UserId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0750 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String prcCd,String prcSys
	*/ 
	private String svrProc(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0750 cmr0750 = new Cmr0750();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String prcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcCd"));
		    String prcSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "prcSys"));
		    return ParsingCommon.toJson(cmr0750.svrProc(AcptNo,prcCd,prcSys));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0750 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String SerNo
	*/ 
	private String progCncl(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0750 cmr0750 = new Cmr0750();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String SerNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SerNo"));
		    return ParsingCommon.toJson(cmr0750.progCncl(AcptNo,SerNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0750 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String PrcSw
	*/ 
	private String confLocat(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0750 cmr0750 = new Cmr0750();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String PrcSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrcSw"));
		    return ParsingCommon.toJson(cmr0750.confLocat(AcptNo,PrcSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0750 = null;
		} 
	}
}
