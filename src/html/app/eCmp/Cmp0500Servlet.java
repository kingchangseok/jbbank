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

import app.eCmp.Cmp0500;

@WebServlet("/webPage/ecmp/Cmp0500Servlet")
public class Cmp0500Servlet extends HttpServlet {

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
			case "getProgList":
				response.getWriter().write(getProgList(jsonElement));
				break;
			case "getPrjStep":
				response.getWriter().write(getPrjStep(jsonElement));
				break;
			case "getPrjInfo":
				response.getWriter().write(getPrjInfo(jsonElement));
				break;
			case "excelDataMake":
				response.getWriter().write(excelDataMake(jsonElement));
				break;
			case "excelDataMake_Prog":
				response.getWriter().write(excelDataMake_Prog(jsonElement));
				break;
			case "getRsrcCd":
				response.getWriter().write(getRsrcCd(jsonElement));
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
	* String UserId,String Gubun,String StDate,String EdDate,String PrjNo,String PrjName
	*/ 
	private String getReqList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String Gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Gubun"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String PrjName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjName"));
		    return ParsingCommon.toJson(cmp0500.getReqList(UserId,Gubun,StDate,EdDate,PrjNo,PrjName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String Gubun,String StDate,String EdDate,String Step1,String Step2,String Step3,String Step4,String SysCd,String JobCd
	*/ 
	private String getProgList(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String Gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Gubun"));
		    String StDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StDate"));
		    String EdDate = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdDate"));
		    String Step1 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Step1"));
		    String Step2 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Step2"));
		    String Step3 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Step3"));
		    String Step4 = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Step4"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    return ParsingCommon.toJson(cmp0500.getProgList(UserId,Gubun,StDate,EdDate,Step1,Step2,Step3,Step4,SysCd,JobCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getPrjStep(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
		    return ParsingCommon.toJson(cmp0500.getPrjStep());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
	/* Parameter 
	* String UserId
	*/ 
	private String getPrjInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmp0500.getPrjInfo(UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,ArrayList<HashMap<String,String>> prjStep,String UserId,String QryCd,String exlName
	*/ 
	private String excelDataMake(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
	   	    ArrayList<HashMap<String,String>> prjStep = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "prjStep"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    String exlName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "exlName"));
		    return ParsingCommon.toJson(cmp0500.excelDataMake(fileList,prjStep,UserId,QryCd,exlName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> fileList,ArrayList<HashMap<String,String>> prjStep,String UserId,String QryCd,HashMap<String,String> etcData,String exlName
	*/ 
	private String excelDataMake_Prog(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
	   	    ArrayList<HashMap<String,String>> fileList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "fileList"));
	   	    ArrayList<HashMap<String,String>> prjStep = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "prjStep"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    String exlName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "exlName"));
		    return ParsingCommon.toJson(cmp0500.excelDataMake_Prog(fileList,prjStep,UserId,QryCd,etcData,exlName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd
	*/ 
	private String getRsrcCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmp0500 cmp0500 = new Cmp0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmp0500.getRsrcCd(UserId,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmp0500 = null;
		} 
	}
}
