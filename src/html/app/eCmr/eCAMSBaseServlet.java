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

import app.eCmr.eCAMSBase;

@WebServlet("/webPage/ecmr/eCAMSBaseServlet")
public class eCAMSBaseServlet extends HttpServlet {

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
			case "getCmr0100":
				response.getWriter().write(getCmr0100(jsonElement));
				break;
			case "getSecuList":
				response.getWriter().write(getSecuList(jsonElement));
				break;
			case "getSysInfo_CkOut":
				response.getWriter().write(getSysInfo_CkOut(jsonElement));
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
	* String UserID
	*/ 
	private String getCmr0100(JsonElement jsonElement) throws SQLException, Exception {
		eCAMSBase ecamsbase = new eCAMSBase();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    return ParsingCommon.toJson(ecamsbase.getCmr0100(UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    ecamsbase = null;
		} 
	}
	/* Parameter 
	* String Sv_UserID
	*/ 
	private String getSecuList(JsonElement jsonElement) throws SQLException, Exception {
		eCAMSBase ecamsbase = new eCAMSBase();
		try {
		    String Sv_UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Sv_UserID"));
		    return ParsingCommon.toJson(ecamsbase.getSecuList(Sv_UserID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    ecamsbase = null;
		} 
	}
	/* Parameter 
	* String gbnName,String gbnCd
	*/ 
	private String getSysInfo_CkOut(JsonElement jsonElement) throws SQLException, Exception {
		eCAMSBase ecamsbase = new eCAMSBase();
		try {
		    String gbnName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gbnName"));
		    String gbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gbnCd"));
		    return ParsingCommon.toJson(ecamsbase.getSysInfo_CkOut(gbnName,gbnCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    ecamsbase = null;
		} 
	}
}
