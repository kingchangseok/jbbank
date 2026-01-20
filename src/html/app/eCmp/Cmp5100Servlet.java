package html.app.eCmp;
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

import app.eCmp.Cmp5100;

@WebServlet("/webPage/ecmp/Cmp5100Servlet")
public class Cmp5100Servlet extends HttpServlet {

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
			case "getReqList":
				response.getWriter().write(getReqList(jsonElement));
				break;
			case "getBefReq":
				response.getWriter().write(getBefReq(jsonElement));
				break;
			case "getSvrList":
				response.getWriter().write(getSvrList(jsonElement));
				break;
			case "diffReq_Ins":
				response.getWriter().write(diffReq_Ins(jsonElement));
				break;
			case "getDiffList":
				response.getWriter().write(getDiffList(jsonElement));
				break;
			case "diffRun":
				response.getWriter().write(diffRun(jsonElement));
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
	* String AcptNo
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp5100 cmp5100 = new Cmp5100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmp5100.getReqList(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp5100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String StDate,String EdDate
	*/ 
	private String getBefReq(JsonElement jsonElement) throws SQLException, Exception {
		Cmp5100 cmp5100 = new Cmp5100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    return ParsingCommon.toJson(cmp5100.getBefReq(UserId,StDate,EdDate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp5100 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String DirPath
	*/ 
	private String getSvrList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp5100 cmp5100 = new Cmp5100();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    return ParsingCommon.toJson(cmp5100.getSvrList(SysCd,DirPath));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp5100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> svrList,HashMap<String,String> etcData
	*/ 
	private String diffReq_Ins(JsonElement jsonElement) throws SQLException, Exception {
		Cmp5100 cmp5100 = new Cmp5100();
		try {
	   	    ArrayList<HashMap<String,String>> svrList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "svrList"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmp5100.diffReq_Ins(svrList,etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp5100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getDiffList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp5100 cmp5100 = new Cmp5100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmp5100.getDiffList(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp5100 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String diffRun(JsonElement jsonElement) throws SQLException, Exception {
		Cmp5100 cmp5100 = new Cmp5100();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmp5100.diffRun(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp5100 = null;
		} 
	}
}
