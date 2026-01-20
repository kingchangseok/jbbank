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

import app.eCmr.Cmr5700;

@WebServlet("/webPage/ecmr/Cmr5700Servlet")
public class Cmr5700Servlet extends HttpServlet {

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
			case "getReqPrj":
				response.getWriter().write(getReqPrj(jsonElement));
				break;
			case "getPrjList":
				response.getWriter().write(getPrjList(jsonElement));
				break;
			case "setPrjInfo":
				response.getWriter().write(setPrjInfo(jsonElement));
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
	* String txtPrjId,boolean chkEnd
	*/ 
	private String getPrjInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5700 cmr5700 = new Cmr5700();
		try {
		    String txtPrjId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtPrjId"));
		    boolean chkEnd = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkEnd")));
		    return ParsingCommon.toJson(cmr5700.getPrjInfo(txtPrjId,chkEnd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5700 = null;
		} 
	}
	/* Parameter 
	* String txtPrjId,String AcptNo
	*/ 
	private String getReqPrj(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5700 cmr5700 = new Cmr5700();
		try {
		    String txtPrjId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtPrjId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr5700.getReqPrj(txtPrjId,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5700 = null;
		} 
	}
	/* Parameter 
	* String txtPrjId,String AcptNo,boolean chkEnd,String UserId
	*/ 
	private String getPrjList(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5700 cmr5700 = new Cmr5700();
		try {
		    String txtPrjId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtPrjId"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    boolean chkEnd = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkEnd")));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    return ParsingCommon.toJson(cmr5700.getPrjList(txtPrjId,AcptNo,chkEnd,UserId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5700 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> projectList,String txtPrjId
	*/ 
	private String setPrjInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5700 cmr5700 = new Cmr5700();
		try {
	   	    ArrayList<HashMap<String,String>> projectList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "projectList"));
		    String txtPrjId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "txtPrjId"));
		    return ParsingCommon.toJson(cmr5700.setPrjInfo(projectList,txtPrjId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5700 = null;
		} 
	}
}
