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

import app.eCmr.Cmr0030;

@WebServlet("/webPage/ecmr/Cmr0030Servlet")
public class Cmr0030Servlet extends HttpServlet {

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
			case "getUnitTest":
				response.getWriter().write(getUnitTest(jsonElement));
				break;
			case "getHisList":
				response.getWriter().write(getHisList(jsonElement));
				break;
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "getRelatList":
				response.getWriter().write(getRelatList(jsonElement));
				break;
			case "getProgIsr":
				response.getWriter().write(getProgIsr(jsonElement));
				break;
			case "setProgList":
				response.getWriter().write(setProgList(jsonElement));
				break;
			case "delProg":
				response.getWriter().write(delProg(jsonElement));
				break;
			case "getHandlerList":
				response.getWriter().write(getHandlerList(jsonElement));
				break;
			case "setTcaseAdd":
				response.getWriter().write(setTcaseAdd(jsonElement));
				break;
			case "TcaseDel":
				response.getWriter().write(TcaseDel(jsonElement));
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
	* String IsrId,String SubId,String UserId
	*/ 
	private String getUnitTest(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0030.getUnitTest(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId,String UserId,String qryGbn
	*/ 
	private String getHisList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String qryGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "qryGbn"));
		    return ParsingCommon.toJson(cmr0030.getHisList(IsrId,SubId,UserId,qryGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId,String UserId
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0030.getProgList(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId,String UserId
	*/ 
	private String getRelatList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0030.getRelatList(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId,String UserId
	*/ 
	private String getProgIsr(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0030.getProgIsr(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData,ArrayList<HashMap<String,String>> CopyList
	*/ 
	private String setProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
	   	    ArrayList<HashMap<String,String>> CopyList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "CopyList"));
		    return ParsingCommon.toJson(cmr0030.setProgList(etcData,CopyList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String UserId,String ItemId
	*/ 
	private String delProg(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmr0030.delProg(IsrId,IsrSub,UserId,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String SubId,String UserId
	*/ 
	private String getHandlerList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String SubId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr0030.getHandlerList(IsrId,SubId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String setTcaseAdd(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0030.setTcaseAdd(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String TcaseDel(JsonElement jsonElement) throws SQLException, Exception {
		Cmr0030 cmr0030 = new Cmr0030();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmr0030.TcaseDel(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr0030 = null;
		} 
	}
}
