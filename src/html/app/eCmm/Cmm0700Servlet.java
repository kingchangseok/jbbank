package html.app.eCmm;
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

import app.eCmm.Cmm0700;

@WebServlet("/webPage/ecmm/Cmm0700Servlet")
public class Cmm0700Servlet extends HttpServlet {

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
			case "getAgentInfo":
				response.getWriter().write(getAgentInfo(jsonElement));
				break;
			case "setAgentInfo":
				response.getWriter().write(setAgentInfo(jsonElement));
				break;
			case "getTab1Info":
				response.getWriter().write(getTab1Info(jsonElement));
				break;
			case "delTab1Info":
				response.getWriter().write(delTab1Info(jsonElement));
				break;
			case "addTab1Info":
				response.getWriter().write(addTab1Info(jsonElement));
				break;
			case "getTab2Info":
				response.getWriter().write(getTab2Info(jsonElement));
				break;
			case "delTab2Info":
				response.getWriter().write(delTab2Info(jsonElement));
				break;
			case "addTab2Info":
				response.getWriter().write(addTab2Info(jsonElement));
				break;
			case "getTab3Info":
				response.getWriter().write(getTab3Info(jsonElement));
				break;
			case "delTab3Info":
				response.getWriter().write(delTab3Info(jsonElement));
				break;
			case "addTab3Info":
				response.getWriter().write(addTab3Info(jsonElement));
				break;
			case "getTab4Info":
				response.getWriter().write(getTab4Info(jsonElement));
				break;
			case "delTab4Info":
				response.getWriter().write(delTab4Info(jsonElement));
				break;
			case "addTab4Info":
				response.getWriter().write(addTab4Info(jsonElement));
				break;
			case "getTab5Info":
				response.getWriter().write(getTab5Info(jsonElement));
				break;
			case "getJawon":
				response.getWriter().write(getJawon(jsonElement));
				break;
			case "delTab5Info":
				response.getWriter().write(delTab5Info(jsonElement));
				break;
			case "addTab5Info":
				response.getWriter().write(addTab5Info(jsonElement));
				break;
			case "getTab6Info":
				response.getWriter().write(getTab6Info(jsonElement));
				break;
			case "addTab6Info":
				response.getWriter().write(addTab6Info(jsonElement));
				break;
			case "delTab6Info":
				response.getWriter().write(delTab6Info(jsonElement));
				break;
			case "addTab7Info":
				response.getWriter().write(addTab7Info(jsonElement));
				break;
			case "getTab7Info":
				response.getWriter().write(getTab7Info(jsonElement));
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
	* 
	*/ 
	private String getAgentInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getAgentInfo());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> objData
	*/ 
	private String setAgentInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    HashMap<String,String> objData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "objData"));
		    return ParsingCommon.toJson(cmm0700.setAgentInfo(objData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab1Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab1Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String timegb
	*/ 
	private String delTab1Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String timegb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "timegb"));
		    return ParsingCommon.toJson(cmm0700.delTab1Info(timegb));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String timegb,String stTime,String edTime
	*/ 
	private String addTab1Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String timegb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "timegb"));
		    String stTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "stTime"));
		    String edTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "edTime"));
		    return ParsingCommon.toJson(cmm0700.addTab1Info(timegb,stTime,edTime));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab2Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab2Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String delgb
	*/ 
	private String delTab2Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String delgb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "delgb"));
		    return ParsingCommon.toJson(cmm0700.delTab2Info(delgb));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String delgb,String deljugi,String jugigb
	*/ 
	private String addTab2Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String delgb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "delgb"));
		    String deljugi = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "deljugi"));
		    String jugigb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "jugigb"));
		    return ParsingCommon.toJson(cmm0700.addTab2Info(delgb,deljugi,jugigb));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab3Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab3Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String pathcd
	*/ 
	private String delTab3Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String pathcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pathcd"));
		    return ParsingCommon.toJson(cmm0700.delTab3Info(pathcd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String pathcd,String path,String tip,String tport
	*/ 
	private String addTab3Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String pathcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "pathcd"));
		    String path = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "path"));
		    String tip = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "tip"));
		    String tport = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "tport"));
		    return ParsingCommon.toJson(cmm0700.addTab3Info(pathcd,path,tip,tport));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab4Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab4Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String jobgb
	*/ 
	private String delTab4Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String jobgb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "jobgb"));
		    return ParsingCommon.toJson(cmm0700.delTab4Info(jobgb));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String jobgb,String tip,String tport,String tuserid,String tpwd
	*/ 
	private String addTab4Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String jobgb = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "jobgb"));
		    String tip = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "tip"));
		    String tport = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "tport"));
		    String tuserid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "tuserid"));
		    String tpwd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "tpwd"));
		    return ParsingCommon.toJson(cmm0700.addTab4Info(jobgb,tip,tport,tuserid,tpwd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab5Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab5Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getJawon(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getJawon());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String SysGbn,String Jawon
	*/ 
	private String delTab5Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String SysGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGbn"));
		    String Jawon = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Jawon"));
		    return ParsingCommon.toJson(cmm0700.delTab5Info(SysGbn,Jawon));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String SysGbn,String Jawon
	*/ 
	private String addTab5Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String SysGbn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysGbn"));
		    String Jawon = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Jawon"));
		    return ParsingCommon.toJson(cmm0700.addTab5Info(SysGbn,Jawon));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab6Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab6Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String TermCd,String StTime,String EdTime,String TermSub,String SysCd
	*/ 
	private String addTab6Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String TermCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TermCd"));
		    String StTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "StTime"));
		    String EdTime = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EdTime"));
		    String TermSub = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "TermSub"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0700.addTab6Info(TermCd,StTime,EdTime,TermSub,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String delTab6Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmm0700.delTab6Info(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* String Year,String Day
	*/ 
	private String addTab7Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    String Year = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Year"));
		    String Day = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Day"));
		    return ParsingCommon.toJson(cmm0700.addTab7Info(Year,Day));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getTab7Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmm0700 cmm0700 = new Cmm0700();
		try {
		    return ParsingCommon.toJson(cmm0700.getTab7Info());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmm0700 = null;
		} 
	}
}
