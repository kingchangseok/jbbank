package html.app.eCmc;
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

import app.eCmc.Cmc0300;

@WebServlet("/webPage/ecmc/Cmc0300Servlet")
public class Cmc0300Servlet extends HttpServlet {

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
			case "getRfcCnt":
				response.getWriter().write(getRfcCnt(jsonElement));
				break;
			case "setRFCConfirm":
				response.getWriter().write(setRFCConfirm(jsonElement));
				break;
			case "updateRFC":
				response.getWriter().write(updateRFC(jsonElement));
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
	* String IsrId,String IsrSub,String UserId,String ReqCd
	*/ 
	private String getRfcCnt(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0300 cmc0300 = new Cmc0300();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmc0300.getRfcCnt(IsrId,IsrSub,UserId,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0300 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,Object>>ConfList
	*/ 
	private String setRFCConfirm(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0300 cmc0300 = new Cmc0300();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    return ParsingCommon.toJson(cmc0300.setRFCConfirm(etcData,ConfList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0300 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String updateRFC(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0300 cmc0300 = new Cmc0300();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmc0300.updateRFC(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0300 = null;
		} 
	}
}
