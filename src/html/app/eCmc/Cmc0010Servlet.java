package html.app.eCmc;
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

import app.eCmc.Cmc0010;

@WebServlet("/webPage/ecmc/Cmc0010Servlet")
public class Cmc0010Servlet extends HttpServlet {

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
			case "ISRSta_common_Cmc0100":
				response.getWriter().write(ISRSta_common_Cmc0100(jsonElement));
				break;
			case "ISRSta_common_Cmc0110":
				response.getWriter().write(ISRSta_common_Cmc0110(jsonElement));
				break;
			case "maxFileSeq":
				response.getWriter().write(maxFileSeq(jsonElement));
				break;
			case "setDocFile":
				response.getWriter().write(setDocFile(jsonElement));
				break;
			case "DelDocFile":
				response.getWriter().write(DelDocFile(jsonElement));
				break;
			case "DelDocFile2":
				response.getWriter().write(DelDocFile2(jsonElement));
				break;
			case "maxDocId":
				response.getWriter().write(maxDocId(jsonElement));
				break;
			case "setDocInfo":
				response.getWriter().write(setDocInfo(jsonElement));
				break;
			case "delFile":
				response.getWriter().write(delFile(jsonElement));
				break;
			case "getDocList":
				response.getWriter().write(getDocList(jsonElement));
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
	* String IsrId
	*/ 
	private String ISRSta_common_Cmc0100(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    return ParsingCommon.toJson(cmc0010.ISRSta_common_Cmc0100(IsrId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* String IsrId,String IsrSub,String ReqCd
	*/ 
	private String ISRSta_common_Cmc0110(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    String IsrId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrId"));
		    String IsrSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "IsrSub"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmc0010.ISRSta_common_Cmc0110(IsrId,IsrSub,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* String ReqId,String ReqCd
	*/ 
	private String maxFileSeq(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    String ReqId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqId"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(cmc0010.maxFileSeq(ReqId,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> docFile
	*/ 
	private String setDocFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    HashMap<String,String> docFile = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "docFile"));
		    return ParsingCommon.toJson(cmc0010.setDocFile(docFile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> docFile
	*/ 
	private String DelDocFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    HashMap<String,String> docFile = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "docFile"));
		    return ParsingCommon.toJson(cmc0010.DelDocFile(docFile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> docFile
	*/ 
	private String DelDocFile2(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    HashMap<String,String> docFile = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "docFile"));
		    return ParsingCommon.toJson(cmc0010.DelDocFile2(docFile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String maxDocId(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    return ParsingCommon.toJson(cmc0010.maxDocId());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> docFile
	*/ 
	private String setDocInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    HashMap<String,String> docFile = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "docFile"));
		    return ParsingCommon.toJson(cmc0010.setDocInfo(docFile));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* String Id,String ReqCd,String seqNo
	*/ 
	private String delFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    String Id = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Id"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String seqNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "seqNo"));
		    return ParsingCommon.toJson(cmc0010.delFile(Id,ReqCd,seqNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
	/* Parameter 
	* String ReqId,String ReqCd,String SubReq
	*/ 
	private String getDocList(JsonElement jsonElement) throws SQLException, Exception {
		Cmc0010 cmc0010 = new Cmc0010();
		try {
		    String ReqId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqId"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    String SubReq = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SubReq"));
		    return ParsingCommon.toJson(cmc0010.getDocList(ReqId,ReqCd,SubReq));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmc0010 = null;
		} 
	}
}
