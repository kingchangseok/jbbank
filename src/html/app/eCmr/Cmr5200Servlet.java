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

import app.eCmr.Cmr5200;

@WebServlet("/webPage/ecmr/Cmr5200Servlet")
public class Cmr5200Servlet extends HttpServlet {

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
			case "getDiffAry":
				response.getWriter().write(getDiffAry(jsonElement));
				break;
			case "getSvrList":
				response.getWriter().write(getSvrList(jsonElement));
				break;
			case "getCheckInAcptNo":
				response.getWriter().write(getCheckInAcptNo(jsonElement));
				break;
			case "getFileVer":
				response.getWriter().write(getFileVer(jsonElement));
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
	* String UserID,String ItemID1,String ver1,String ItemID2,String ver2
	*/ 
	private String getDiffAry(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5200 cmr5200 = new Cmr5200();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String ItemID1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID1"));
		    String ver1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ver1"));
		    String ItemID2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID2"));
		    String ver2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ver2"));
		    return ParsingCommon.toJson(cmr5200.getDiffAry(UserID,ItemID1,ver1,ItemID2,ver2));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5200 = null;
		} 
	}
	/* Parameter 
	* String sysCd,String rsrcCd
	*/ 
	private String getSvrList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5200 cmr5200 = new Cmr5200();
		try {
		    String sysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysCd"));
		    String rsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "rsrcCd"));
		    return ParsingCommon.toJson(cmr5200.getSvrList(sysCd,rsrcCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5200 = null;
		} 
	}
	/* Parameter 
	* String ItemID
	*/ 
	private String getCheckInAcptNo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5200 cmr5200 = new Cmr5200();
		try {
		    String ItemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID"));
		    return ParsingCommon.toJson(cmr5200.getCheckInAcptNo(ItemID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5200 = null;
		} 
	}
	/* Parameter 
	* String ItemID,String AcptNo
	*/ 
	private String getFileVer(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5200 cmr5200 = new Cmr5200();
		try {
		    String ItemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr5200.getFileVer(ItemID,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5200 = null;
		} 
	}
}
