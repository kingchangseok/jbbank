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

import app.common.CodeInfo;

@WebServlet("/webPage/common/CodeInfoServlet")
public class CodeInfoServlet extends HttpServlet {

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
			case "getCodeInfo":
				response.getWriter().write(getCodeInfo(jsonElement));
				break;
			case "getCodeInfo2":
				response.getWriter().write(getCodeInfo2(jsonElement));
				break;
			case "getCodeInfo_Sort":
				response.getWriter().write(getCodeInfo_Sort(jsonElement));
				break;
			case "getJobCd":
				response.getWriter().write(getJobCd(jsonElement));
				break;
			case "getSrOpen":
				response.getWriter().write(getSrOpen(jsonElement));
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
	* String MACODE,String SelMsg,String closeYn
	*/ 
	private String getCodeInfo(JsonElement jsonElement) throws SQLException, Exception {
		CodeInfo codeinfo = new CodeInfo();
		try {
		    String MACODE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MACODE"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    return ParsingCommon.toJson(codeinfo.getCodeInfo(MACODE,SelMsg,closeYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    codeinfo = null;
		} 
	}
	/* Parameter 
	* String MACODE,String SelMsg,String closeYn
	*/ 
	private String getCodeInfo2(JsonElement jsonElement) throws SQLException, Exception {
		CodeInfo codeinfo = new CodeInfo();
		try {
		    String MACODE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MACODE"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    return ParsingCommon.toJson(codeinfo.getCodeInfo2(MACODE,SelMsg,closeYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    codeinfo = null;
		} 
	}
	/* Parameter 
	* String MACODE,String SelMsg,String closeYn,int sortCd
	*/ 
	private String getCodeInfo_Sort(JsonElement jsonElement) throws SQLException, Exception {
		CodeInfo codeinfo = new CodeInfo();
		try {
		    String MACODE = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MACODE"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    int sortCd = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sortCd")));
		    return ParsingCommon.toJson(codeinfo.getCodeInfo_Sort(MACODE,SelMsg,closeYn,sortCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    codeinfo = null;
		} 
	}
	/* Parameter 
	* String SelMsg,String closeYn
	*/ 
	private String getJobCd(JsonElement jsonElement) throws SQLException, Exception {
		CodeInfo codeinfo = new CodeInfo();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    return ParsingCommon.toJson(codeinfo.getJobCd(SelMsg,closeYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    codeinfo = null;
		} 
	}
	/* Parameter 
	* String SelMsg,String closeYn
	*/ 
	private String getSrOpen(JsonElement jsonElement) throws SQLException, Exception {
		CodeInfo codeinfo = new CodeInfo();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String closeYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "closeYn"));
		    return ParsingCommon.toJson(codeinfo.getSrOpen(SelMsg,closeYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    codeinfo = null;
		} 
	}
}
