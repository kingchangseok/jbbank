package html.app.common;
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

import app.common.TeamInfo;

@WebServlet("/webPage/common/TeamInfoServlet")
public class TeamInfoServlet extends HttpServlet {

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
			case "getTeamInfoGrid2":
				response.getWriter().write(getTeamInfoGrid2(jsonElement));
				break;
			case "getTeamInfoGrid":
				response.getWriter().write(getTeamInfoGrid(jsonElement));
				break;
			case "getTeam":
				response.getWriter().write(getTeam(jsonElement));
				break;
			case "getTeamNodeInfo":
				response.getWriter().write(getTeamNodeInfo(jsonElement));
				break;
			case "getTeamInfoTree_zTree":
				response.getWriter().write(getTeamInfoTree_zTree(jsonElement));
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
	* String SelMsg,String cm_useyn,String gubun,String itYn
	*/ 
	private String getTeamInfoGrid2(JsonElement jsonElement) throws SQLException, Exception {
		TeamInfo teaminfo = new TeamInfo();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String cm_useyn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_useyn"));
		    String gubun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "gubun"));
		    String itYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itYn"));
		    return ParsingCommon.toJson(teaminfo.getTeamInfoGrid2(SelMsg,cm_useyn,gubun,itYn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    teaminfo = null;
		} 
	}
	/* Parameter 
	* String SelMsg,String cm_useyn
	*/ 
	private String getTeamInfoGrid(JsonElement jsonElement) throws SQLException, Exception {
		TeamInfo teaminfo = new TeamInfo();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String cm_useyn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_useyn"));
		    return ParsingCommon.toJson(teaminfo.getTeamInfoGrid(SelMsg,cm_useyn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    teaminfo = null;
		} 
	}
	/* Parameter 
	* String SelMsg,String cm_useyn
	*/ 
	private String getTeam(JsonElement jsonElement) throws SQLException, Exception {
		TeamInfo teaminfo = new TeamInfo();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String cm_useyn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_useyn"));
		    return ParsingCommon.toJson(teaminfo.getTeam(SelMsg,cm_useyn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    teaminfo = null;
		} 
	}
	/* Parameter 
	* String UserName,String DeptCd,String DeptName
	*/ 
	private String getTeamNodeInfo(JsonElement jsonElement) throws SQLException, Exception {
		TeamInfo teaminfo = new TeamInfo();
		try {
		    String UserName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserName"));
		    String DeptCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeptCd"));
		    String DeptName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DeptName"));
		    return ParsingCommon.toJson(teaminfo.getTeamNodeInfo(UserName,DeptCd,DeptName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    teaminfo = null;
		} 
	}
	/* Parameter 
	* boolean chkcd,boolean itsw
	*/ 
	private String getTeamInfoTree_zTree(JsonElement jsonElement) throws SQLException, Exception {
		TeamInfo teaminfo = new TeamInfo();
		try {
			boolean chkcd = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "chkcd")));
			boolean itsw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itsw")));
			return ParsingCommon.toJson(teaminfo.getTeamInfoTree_zTree(chkcd,itsw));
		} catch (IOException e) { 
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
			teaminfo = null;
		}
	}
}
