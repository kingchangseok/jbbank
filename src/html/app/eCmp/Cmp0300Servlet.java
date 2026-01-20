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

import app.eCmp.Cmp0300;

@WebServlet("/webPage/ecmp/Cmp0300Servlet")
public class Cmp0300Servlet extends HttpServlet {

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
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "excelDataMake":
				response.getWriter().write(excelDataMake(jsonElement));
				break;
			case "getRsrcCd":
				response.getWriter().write(getRsrcCd(jsonElement));
				break;
			case "getCountList":
				response.getWriter().write(getCountList(jsonElement));
				break;
			case "getBeforeToConvertList":
				response.getWriter().write(getBeforeToConvertList(jsonElement));
				break;
			case "getCountList_vertical":
				response.getWriter().write(getCountList_vertical(jsonElement));
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
	* String UserId,String SysCd,String JobCd,String StDate
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0300 cmp0300 = new Cmp0300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    return ParsingCommon.toJson(cmp0300.getProgList(UserId,SysCd,JobCd,StDate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0300 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,ArrayList<HashMap<String,String>> prjStep,String UserId,String exlName
	*/ 
	private String excelDataMake(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0300 cmp0300 = new Cmp0300();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
	   	    ArrayList<HashMap<String,String>> prjStep = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "prjStep"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String exlName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "exlName"));
		    return ParsingCommon.toJson(cmp0300.excelDataMake(fileList,prjStep,UserId,exlName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0300 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd
	*/ 
	private String getRsrcCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0300 cmp0300 = new Cmp0300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmp0300.getRsrcCd(UserId,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0300 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String DecaCd,String StDate
	*/ 
	private String getCountList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0300 cmp0300 = new Cmp0300();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String DecaCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DecaCd"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    return ParsingCommon.toJson(cmp0300.getCountList(UserId,SysCd,DecaCd,StDate));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0300 = null;
		} 
	}
	/* Parameter 
	* String StDate,String EdDate,String HandlerId,String DeptCd,String ProgName,String QryCd
	*/ 
	private String getBeforeToConvertList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0300 cmp0300 = new Cmp0300();
		try {
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    String HandlerId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "HandlerId"));
		    String DeptCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeptCd"));
		    String ProgName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgName"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    return ParsingCommon.toJson(cmp0300.getBeforeToConvertList(StDate,EdDate,HandlerId,DeptCd,ProgName,QryCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0300 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String DecaCd,String StDate
	*/ 
	private String getCountList_vertical(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0300 cmp0300 = new Cmp0300();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmp0300.getCountList_vertical(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0300 = null;
		} 
	}
}
