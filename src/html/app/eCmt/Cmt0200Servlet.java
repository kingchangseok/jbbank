package html.app.eCmt;
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

import app.eCmt.Cmt0200;

@WebServlet("/webPage/ecmt/Cmt0200Servlet")
public class Cmt0200Servlet extends HttpServlet {

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
			case "getTestCase":
				response.getWriter().write(getTestCase(jsonElement));
				break;
			case "getTestCase_Sub":
				response.getWriter().write(getTestCase_Sub(jsonElement));
				break;
			case "endTestCase":
				response.getWriter().write(endTestCase(jsonElement));
				break;
			case "getTestInfo_Recv":
				response.getWriter().write(getTestInfo_Recv(jsonElement));
				break;
			case "setTestEnd":
				response.getWriter().write(setTestEnd(jsonElement));
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
	* String IsrId,String IsrSub,String UserId,String ReqCd,String TestSeq
	*/ 
	private String getTestCase(JsonElement jsonElement) throws SQLException, Exception {
		Cmt0200 cmt0200 = new Cmt0200();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String TestSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TestSeq"));
		    return ParsingCommon.toJson(cmt0200.getTestCase(IsrId,IsrSub,UserId,ReqCd,TestSeq));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmt0200 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String UserId,String ReqCd,String TestSeq,boolean scmSw,boolean testSw
	*/ 
	private String getTestCase_Sub(JsonElement jsonElement) throws SQLException, Exception {
		Cmt0200 cmt0200 = new Cmt0200();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String TestSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TestSeq"));
		    boolean scmSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "scmSw")));
		    boolean testSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "testSw")));
		    return ParsingCommon.toJson(cmt0200.getTestCase_Sub(IsrId,IsrSub,UserId,ReqCd,TestSeq,scmSw,testSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmt0200 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String TestSeq
	*/ 
	private String endTestCase(JsonElement jsonElement) throws SQLException, Exception {
		Cmt0200 cmt0200 = new Cmt0200();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String TestSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TestSeq"));
		    return ParsingCommon.toJson(cmt0200.endTestCase(IsrId,IsrSub,TestSeq));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmt0200 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,boolean secuSw,String selSeq,String dataYn
	*/ 
	private String getTestInfo_Recv(JsonElement jsonElement) throws SQLException, Exception {
		Cmt0200 cmt0200 = new Cmt0200();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    boolean secuSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "secuSw")));
		    String selSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "selSeq"));
		    String dataYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "dataYn"));
		    return ParsingCommon.toJson(cmt0200.getTestInfo_Recv(IsrId,IsrSub,secuSw,selSeq,dataYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmt0200 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String TestSeq,String UserId
	*/ 
	private String setTestEnd(JsonElement jsonElement) throws SQLException, Exception {
		Cmt0200 cmt0200 = new Cmt0200();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String TestSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TestSeq"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmt0200.setTestEnd(IsrId,IsrSub,TestSeq,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmt0200 = null;
		} 
	}
}
