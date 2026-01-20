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

import app.eCmr.Cmr5300;

@WebServlet("/webPage/ecmr/Cmr5300Servlet")
public class Cmr5300Servlet extends HttpServlet {

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
			case "getFileText":
				response.getWriter().write(getFileText(jsonElement));
				break;
			case "getFileVer":
				response.getWriter().write(getFileVer(jsonElement));
				break;
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
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
	* String ItemId,String Version,String AcptNo,boolean ynDocFile,boolean gubun
	*/ 
	private String getFileText(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5300 cmr5300 = new Cmr5300();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String Version = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Version"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    boolean ynDocFile = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ynDocFile")));
		    boolean gubun = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gubun")));
		    return ParsingCommon.toJson(cmr5300.getFileText(ItemId,Version,AcptNo,ynDocFile,gubun));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5300 = null;
		} 
	}
	/* Parameter 
	* String ItemID,String viewGbn
	*/ 
	private String getFileVer(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5300 cmr5300 = new Cmr5300();
		try {
		    String ItemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID"));
		    String viewGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "viewGbn"));
		    return ParsingCommon.toJson(cmr5300.getFileVer(ItemID,viewGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5300 = null;
		} 
	}
	/* Parameter 
	* String AcptNo,String UserId
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5300 cmr5300 = new Cmr5300();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr5300.getReqList(AcptNo,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5300 = null;
		} 
	}
}
