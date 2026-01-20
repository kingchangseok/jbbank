package html.app.eCmr;
import java.io.IOException;
import java.sql.SQLException;

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

import app.eCmr.Cmr5400;

@WebServlet("/webPage/ecmr/Cmr5400Servlet")
public class Cmr5400Servlet extends HttpServlet {

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
			case "getFileVer":
				response.getWriter().write(getFileVer(jsonElement));
				break;
			case "fileDiffInf":
				response.getWriter().write(fileDiffInf(jsonElement));
				break;
			case "getDiffSingle":
				response.getWriter().write(getDiffSingle(jsonElement));
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
	* String ItemID,String AcptNo
	*/ 
	private String getFileVer(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5400 cmr5400 = new Cmr5400();
		try {
		    String ItemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmr5400.getFileVer(ItemID,AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5400 = null;
		} 
	}
	/* Parameter 
	* String ItemID,String befAcpt,String befQry,String aftAcpt,String aftQry,boolean chkFg
	*/ 
	private String fileDiffInf(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5400 cmr5400 = new Cmr5400();
		try {
		    String ItemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemID"));
		    String befAcpt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "befAcpt"));
		    String befQry = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "befQry"));
		    String aftAcpt = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "aftAcpt"));
		    String aftQry = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "aftQry"));
		    boolean chkFg = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkFg")));
		    return ParsingCommon.toJson(cmr5400.fileDiffInf(ItemID,befAcpt,befQry,aftAcpt,aftQry,chkFg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5400 = null;
		} 
	}
	/* Parameter 
	* String ItemId,String Version,String AcptNo,String GbnCd
	*/ 
	private String getDiffSingle(JsonElement jsonElement) throws SQLException, Exception {
		Cmr5400 cmr5400 = new Cmr5400();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String Version = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Version"));
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    String GbnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "GbnCd"));
		    return ParsingCommon.toJson(cmr5400.getDiffSingle(ItemId,Version,AcptNo,GbnCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmr5400 = null;
		} 
	}
}
