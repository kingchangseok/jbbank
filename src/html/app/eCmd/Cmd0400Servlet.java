package html.app.eCmd;
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

import app.eCmd.Cmd0400;

@WebServlet("/webPage/ecmd/Cmd0400Servlet")
public class Cmd0400Servlet extends HttpServlet {

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
			case "Cbo_DevStep_Set":
				response.getWriter().write(Cbo_DevStep_Set(jsonElement));
				break;
			case "Sql_Qry_Hist":
				response.getWriter().write(Sql_Qry_Hist(jsonElement));
				break;
			case "Sql_Qry_Sub":
				response.getWriter().write(Sql_Qry_Sub(jsonElement));
				break;
			case "Sql_Qry_DocCd":
				response.getWriter().write(Sql_Qry_DocCd(jsonElement));
				break;
			case "Sql_Qry_DocFile":
				response.getWriter().write(Sql_Qry_DocFile(jsonElement));
				break;
			case "getDocFile":
				response.getWriter().write(getDocFile(jsonElement));
				break;
			case "Cmd0304_Update":
				response.getWriter().write(Cmd0304_Update(jsonElement));
				break;
			case "Cmr0030_Update":
				response.getWriter().write(Cmr0030_Update(jsonElement));
				break;
			case "Cmr0035_Insert":
				response.getWriter().write(Cmr0035_Insert(jsonElement));
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
	* String PrjNo
	*/ 
	private String Cbo_DevStep_Set(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd0400.Cbo_DevStep_Set(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String Sql_Qry_Hist(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd0400.Sql_Qry_Hist(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* String PrjNo
	*/ 
	private String Sql_Qry_Sub(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd0400.Sql_Qry_Sub(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* String PrjNo
	*/ 
	private String Sql_Qry_DocCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    return ParsingCommon.toJson(cmd0400.Sql_Qry_DocCd(PrjNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> dataObj
	*/ 
	private String Sql_Qry_DocFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    HashMap<String,String> dataObj = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "dataObj"));
		    return ParsingCommon.toJson(cmd0400.Sql_Qry_DocFile(dataObj));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String ItemId,String PrjNo,String DocSeq
	*/ 
	private String getDocFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String DocSeq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DocSeq"));
		    return ParsingCommon.toJson(cmd0400.getDocFile(SysCd,ItemId,PrjNo,DocSeq));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String UserId,ArrayList<HashMap<String,String>> PRJUSER,String Gubun
	*/ 
	private String Cmd0304_Update(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> PRJUSER = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "PRJUSER"));
		    String Gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Gubun"));
		    return ParsingCommon.toJson(cmd0400.Cmd0304_Update(PrjNo,UserId,PRJUSER,Gubun));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> Spr_Lst
	*/ 
	private String Cmr0030_Update(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> Spr_Lst = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "Spr_Lst"));
		    return ParsingCommon.toJson(cmd0400.Cmr0030_Update(UserId,Spr_Lst));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String flag,String UserId
	*/ 
	private String Cmr0035_Insert(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0400 cmd0400 = new Cmd0400();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String flag = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "flag"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmd0400.Cmr0035_Insert(PrjNo,flag,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0400 = null;
		} 
	}
}
