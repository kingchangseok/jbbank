package html.app.common;
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

import app.common.SysInfo;

@WebServlet("/webPage/common/SysInfoServlet")
public class SysInfoServlet extends HttpServlet {

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
			case "getSysInfo":
				response.getWriter().write(getSysInfo(jsonElement));
				break;
			case "getSysInfo_bld":
				response.getWriter().write(getSysInfo_bld(jsonElement));
				break;
			case "getLocalYn":
				response.getWriter().write(getLocalYn(jsonElement));
				break;
			case "getSysInfo_Rpt":
				response.getWriter().write(getSysInfo_Rpt(jsonElement));
				break;
			case "getSysInfo_Tmax":
				response.getWriter().write(getSysInfo_Tmax(jsonElement));
				break;
			case "getTstSys":
				response.getWriter().write(getTstSys(jsonElement));
				break;
			case "getJobInfo":
				response.getWriter().write(getJobInfo(jsonElement));
				break;
			case "getJobInfo_Rpt":
				response.getWriter().write(getJobInfo_Rpt(jsonElement));
				break;
			case "getRsrcInfo":
				response.getWriter().write(getRsrcInfo(jsonElement));
				break;
			case "getsvrInfo":
				response.getWriter().write(getsvrInfo(jsonElement));
				break;
			case "getsvrInfo_doc":
				response.getWriter().write(getsvrInfo_doc(jsonElement));
				break;
			case "getJobInfo2":
				response.getWriter().write(getJobInfo2(jsonElement));
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
	* String UserId,String SecuYn,String SelMsg,String CloseYn,String ReqCd
	*/ 
	private String getSysInfo(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String CloseYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CloseYn"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(sysinfo.getSysInfo(UserId,SecuYn,SelMsg,CloseYn,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserId,String SecuYn,String SelMsg
	*/ 
	private String getSysInfo_bld(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(sysinfo.getSysInfo_bld(UserId,SecuYn,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getLocalYn(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(sysinfo.getLocalYn(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserId,String SelMsg,String CloseYn,String SysCd
	*/ 
	private String getSysInfo_Rpt(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String CloseYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CloseYn"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(sysinfo.getSysInfo_Rpt(UserId,SelMsg,CloseYn,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserId,String SecuYn,String SelMsg,String CloseYn,String ReqCd
	*/ 
	private String getSysInfo_Tmax(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String CloseYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CloseYn"));
		    String ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ReqCd"));
		    return ParsingCommon.toJson(sysinfo.getSysInfo_Tmax(UserId,SecuYn,SelMsg,CloseYn,ReqCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getTstSys(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(sysinfo.getTstSys(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SecuYn,String CloseYn,String SelMsg,String sortCd
	*/ 
	private String getJobInfo(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String CloseYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CloseYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String sortCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "sortCd"));
		    return ParsingCommon.toJson(sysinfo.getJobInfo(UserID,SysCd,SecuYn,CloseYn,SelMsg,sortCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String CloseYn,String SelMsg
	*/ 
	private String getJobInfo_Rpt(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String CloseYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CloseYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(sysinfo.getJobInfo_Rpt(UserID,SysCd,CloseYn,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SecuYn,String SelMsg
	*/ 
	private String getRsrcInfo(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(sysinfo.getRsrcInfo(UserID,SysCd,SecuYn,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String SecuYn,String SelMsg
	*/ 
	private String getsvrInfo(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(sysinfo.getsvrInfo(UserID,SysCd,SecuYn,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String UserID,String SelMsg
	*/ 
	private String getsvrInfo_doc(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(sysinfo.getsvrInfo_doc(UserID,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
	/* Parameter 
	* String SysCd
	*/ 
	private String getJobInfo2(JsonElement jsonElement) throws SQLException, Exception {
		SysInfo sysinfo = new SysInfo();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(sysinfo.getJobInfo2(SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    sysinfo = null;
		} 
	}
}
