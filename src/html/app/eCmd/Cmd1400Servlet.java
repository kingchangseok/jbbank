package html.app.eCmd;
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

import app.eCmd.Cmd1400;

@WebServlet("/webPage/ecmd/Cmd1400Servlet")
public class Cmd1400Servlet extends HttpServlet {

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
			case "getProgInfo":
				response.getWriter().write(getProgInfo(jsonElement));
				break;
			case "getPrcSysInfo":
				response.getWriter().write(getPrcSysInfo(jsonElement));
				break;
			case "getBldList":
				response.getWriter().write(getBldList(jsonElement));
				break;
			case "getBldScript":
				response.getWriter().write(getBldScript(jsonElement));
				break;
			case "getHomeDir":
				response.getWriter().write(getHomeDir(jsonElement));
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
	* String ItemId
	*/ 
	private String getProgInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1400 cmd1400 = new Cmd1400();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmd1400.getProgInfo(ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1400 = null;
		} 
	}
	/* Parameter 
	* String AcptNo
	*/ 
	private String getPrcSysInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1400 cmd1400 = new Cmd1400();
		try {
		    String AcptNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "AcptNo"));
		    return ParsingCommon.toJson(cmd1400.getPrcSysInfo(AcptNo));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1400 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String RsrcCd,String JobCd,String QryCd
	*/ 
	private String getBldList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1400 cmd1400 = new Cmd1400();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String QryCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "QryCd"));
		    return ParsingCommon.toJson(cmd1400.getBldList(SysCd,RsrcCd,JobCd,QryCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1400 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String RsrcCd,String JobCd,String RsrcName,String DirPath,String BldGbn,String BldCd
	*/ 
	private String getBldScript(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1400 cmd1400 = new Cmd1400();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    String BldGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BldGbn"));
		    String BldCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BldCd"));
		    return ParsingCommon.toJson(cmd1400.getBldScript(SysCd,RsrcCd,JobCd,RsrcName,DirPath,BldGbn,BldCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1400 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String RsrcCd,String BldGbn
	*/ 
	private String getHomeDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1400 cmd1400 = new Cmd1400();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String BldGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BldGbn"));
		    return ParsingCommon.toJson(cmd1400.getHomeDir(SysCd,RsrcCd,BldGbn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1400 = null;
		} 
	}
}
