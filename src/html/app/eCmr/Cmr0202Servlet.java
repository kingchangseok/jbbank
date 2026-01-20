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

import app.eCmr.Cmr0202;

@WebServlet("/webPage/ecmr/Cmr0202Servlet")
public class Cmr0202Servlet extends HttpServlet {

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
			case "Confirm_Info":
				response.getWriter().write(Confirm_Info(jsonElement));
				break;
			case "Cmr9900Tmp_Ins":
				response.getWriter().write(Cmr9900Tmp_Ins(jsonElement));
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
	* String UserId,String SysCd,String ReqCd,ArrayList<String> RsrcCd,ArrayList<String> JobCd,ArrayList<String> Pos,String EmgSw,String PrjNo
	*/ 
	private String Confirm_Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0202 cmr0202 = new Cmr0202();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    ArrayList<String> RsrcCd = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    ArrayList<String> JobCd = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    ArrayList<String> Pos = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "Pos"));
		    String EmgSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EmgSw"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmr0202.Confirm_Info(UserId,SysCd,ReqCd,RsrcCd,JobCd,Pos,EmgSw,PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0202 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,Object>>ConfList,HashMap<String,String> etcData
	*/ 
	private String Cmr9900Tmp_Ins(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0202 cmr0202 = new Cmr0202();
		try {
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0202.Cmr9900Tmp_Ins(ConfList,etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0202 = null;
		} 
	}
}
