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

import app.eCmd.Cmd1800;

@WebServlet("/webPage/ecmd/Cmd1800Servlet")
public class Cmd1800Servlet extends HttpServlet {

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
			case "genFile":
				response.getWriter().write(genFile(jsonElement));
				break;
			case "getFileList":
				response.getWriter().write(getFileList(jsonElement));
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
	* String ItemId,String Version,String RsrcName,String pathStr
	*/ 
	private String genFile(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1800 cmd1800 = new Cmd1800();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String Version = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Version"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String pathStr = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pathStr"));
		    return ParsingCommon.toJson(cmd1800.genFile(ItemId,Version,RsrcName,pathStr));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1800 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String SysGb,String DsnCd,String RsrcCd,String RsrcName,String sysAllFlag
	*/ 
	private String getFileList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd1800 cmd1800 = new Cmd1800();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SysGb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGb"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String sysAllFlag = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sysAllFlag"));
		    return ParsingCommon.toJson(cmd1800.getFileList(SysCd,SysGb,DsnCd,RsrcCd,RsrcName,sysAllFlag));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd1800 = null;
		} 
	}
}
