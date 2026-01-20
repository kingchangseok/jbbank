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

import app.eCmr.Confirm_select;

@WebServlet("/webPage/ecmr/Confirm_selectServlet")
public class Confirm_selectServlet extends HttpServlet {

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
			case "confSelect_doc":
				response.getWriter().write(confSelect_doc(jsonElement));
				break;
			case "confirmYN":
				response.getWriter().write(confirmYN(jsonElement));
				break;
			case "Confirm_Info":
				response.getWriter().write(Confirm_Info(jsonElement));
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
	* HashMap<String,String> dataObj
	*/ 
	private String confSelect_doc(JsonElement jsonElement) throws SQLException, Exception {
		Confirm_select confirm_select = new Confirm_select();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(confirm_select.confSelect_doc(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    confirm_select = null;
		} 
	}
	/* Parameter 
	* String ReqCd,String SysCd,String UserId
	*/ 
	private String confirmYN(JsonElement jsonElement) throws SQLException, Exception {
		Confirm_select confirm_select = new Confirm_select();
		try {
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(confirm_select.confirmYN(ReqCd,SysCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    confirm_select = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String ReqCd,ArrayList<String> RsrcCd,ArrayList<String> PgmType,String EmgSw,String PrjNo,String deployCd,ArrayList<String> QryCd,String passok,String gyulcheck
	*/ 
	private String Confirm_Info(JsonElement jsonElement) throws SQLException, Exception {
		Confirm_select confirm_select = new Confirm_select();
		try {
		    /*String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    ArrayList<String> RsrcCd = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    ArrayList<String> PgmType = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "PgmType"));
		    String EmgSw = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EmgSw"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String deployCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "deployCd"));
		    ArrayList<String> QryCd = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    String passok = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "passok"));
		    String gyulcheck = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gyulcheck"));*/
		    HashMap<String,String> paramMap = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "paramMap"));
		    return ParsingCommon.toJson(confirm_select.Confirm_Info(paramMap));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    confirm_select = null;
		} 
	}
}
