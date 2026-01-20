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

import app.eCmr.Cmr0101;

@WebServlet("/webPage/ecmr/Cmr0101Servlet")
public class Cmr0101Servlet extends HttpServlet {

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
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
				break;
			case "getDownFileList":
				response.getWriter().write(getDownFileList(jsonElement));
				break;
			case "request_Check_Out_Cancel":
				response.getWriter().write(request_Check_Out_Cancel(jsonElement));
				break;
			case "dbchk":
				response.getWriter().write(dbchk(jsonElement));
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
	* HashMap<String,String> etcData
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0101 cmr0101 = new Cmr0101();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0101.getFileList(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0101 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList
	*/ 
	private String getDownFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0101 cmr0101 = new Cmr0101();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmr0101.getDownFileList(fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0101 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> chkOutList,HashMap<String,String> etcData,ArrayList<HashMap<String,Object>>ConfList
	*/ 
	private String request_Check_Out_Cancel(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0101 cmr0101 = new Cmr0101();
		try {
	   	    ArrayList<HashMap<String,String>> chkOutList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "chkOutList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    ArrayList<HashMap<String,Object>> ConfList = ParsingCommon.jsonStrToArrObj(ParsingCommon.jsonEtoStr(jsonElement, "ConfList"));
		    return ParsingCommon.toJson(cmr0101.request_Check_Out_Cancel(chkOutList,etcData,ConfList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0101 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList
	*/ 
	private String dbchk(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0101 cmr0101 = new Cmr0101();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
		    return ParsingCommon.toJson(cmr0101.dbchk(fileList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0101 = null;
		} 
	}
}
