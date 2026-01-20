package html.app.common;
import java.io.IOException;
import java.sql.SQLException;
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

import app.common.PrjInfo;

@WebServlet("/webPage/common/PrjInfoServlet")
public class PrjInfoServlet extends HttpServlet {

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
			case "getPrjInfo":
				response.getWriter().write(getPrjInfo(jsonElement));
				break;
			case "getPrjList_Recv":
				response.getWriter().write(getPrjList_Recv(jsonElement));
				break;
			case "getPrjList_Isr":
				response.getWriter().write(getPrjList_Isr(jsonElement));
				break;
			case "getPrjList_Chg":
				response.getWriter().write(getPrjList_Chg(jsonElement));
				break;
			case "getPrjList_Test":
				response.getWriter().write(getPrjList_Test(jsonElement));
				break;
			case "getPrjSub":
				response.getWriter().write(getPrjSub(jsonElement));
				break;
			case "getPrjInfoDetail":
				response.getWriter().write(getPrjInfoDetail(jsonElement));
				break;
			case "getPrjInfoJIKMOO":
				response.getWriter().write(getPrjInfoJIKMOO(jsonElement));
				break;
			case "getFlowCnts":
				response.getWriter().write(getFlowCnts(jsonElement));
				break;
			case "getPrjJoiner":
				response.getWriter().write(getPrjJoiner(jsonElement));
				break;
			case "getPrjJoinerInfo":
				response.getWriter().write(getPrjJoinerInfo(jsonElement));
				break;
			case "getHomeDir":
				response.getWriter().write(getHomeDir(jsonElement));
				break;
			case "getSubTab":
				response.getWriter().write(getSubTab(jsonElement));
				break;
			case "getISRList":
				response.getWriter().write(getISRList(jsonElement));
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
	* String UserId,String SelMsg,String CloseYn
	*/ 
	private String getPrjInfo(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String CloseYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CloseYn"));
		    return ParsingCommon.toJson(prjinfo.getPrjInfo(UserId,SelMsg,CloseYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getPrjList_Recv(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(prjinfo.getPrjList_Recv(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getPrjList_Isr(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(prjinfo.getPrjList_Isr(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getPrjList_Chg(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(prjinfo.getPrjList_Chg(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getPrjList_Test(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(prjinfo.getPrjList_Test(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String IsrId,String UserId
	*/ 
	private String getPrjSub(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(prjinfo.getPrjSub(IsrId,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String ReqCd,String UserId
	*/ 
	private String getPrjInfoDetail(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(prjinfo.getPrjInfoDetail(IsrId,IsrSub,ReqCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String PrjNo
	*/ 
	private String getPrjInfoJIKMOO(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(prjinfo.getPrjInfoJIKMOO(UserID,PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getFlowCnts(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(prjinfo.getFlowCnts(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String Gubun
	*/ 
	private String getPrjJoiner(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String Gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Gubun"));
		    return ParsingCommon.toJson(prjinfo.getPrjJoiner(PrjNo,Gubun));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String UserId,String PrjNo
	*/ 
	private String getPrjJoinerInfo(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(prjinfo.getPrjJoinerInfo(UserId,PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getHomeDir(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(prjinfo.getHomeDir(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String ReqCd,String UserId
	*/ 
	private String getSubTab(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(prjinfo.getSubTab(IsrId,IsrSub,ReqCd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
	/* Parameter 
	* String UserId,String ReqCD
	*/ 
	private String getISRList(JsonElement jsonElement) throws SQLException, Exception {
		PrjInfo prjinfo = new PrjInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ReqCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCD"));
		    return ParsingCommon.toJson(prjinfo.getISRList(UserId,ReqCD));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    prjinfo = null;
		} 
	}
}
