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

import app.eCmd.Cmd0100;

@WebServlet("/webPage/ecmd/Cmd0100Servlet")
public class Cmd0100Servlet extends HttpServlet {

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
			case "getDir":
				response.getWriter().write(getDir(jsonElement));
				break;
			case "getDir_Tmax":
				response.getWriter().write(getDir_Tmax(jsonElement));
				break;
			case "getDir_Tmax2":
				response.getWriter().write(getDir_Tmax2(jsonElement));
				break;
			case "getRsrcOpen":
				response.getWriter().write(getRsrcOpen(jsonElement));
				break;
			case "getLang":
				response.getWriter().write(getLang(jsonElement));
				break;
			case "getOpenList":
				response.getWriter().write(getOpenList(jsonElement));
				break;
			case "pgmCheck":
				response.getWriter().write(pgmCheck(jsonElement));
				break;
			case "moduleChk":
				response.getWriter().write(moduleChk(jsonElement));
				break;
			case "cmr0020_Delete":
				response.getWriter().write(cmr0020_Delete(jsonElement));
				break;
			case "getRsrcOpen2":
				response.getWriter().write(getRsrcOpen2(jsonElement));
				break;
			case "getCompile":
				response.getWriter().write(getCompile(jsonElement));
				break;
			case "getPrjList_Chg":
				response.getWriter().write(getPrjList_Chg(jsonElement));
				break;
			case "Cmd0100_search":
				response.getWriter().write(Cmd0100_search(jsonElement));
				break;
			case "Cmd0100_search2":
				response.getWriter().write(Cmd0100_search2(jsonElement));
				break;
			case "cmd0100_Insert":
				response.getWriter().write(cmd0100_Insert(jsonElement));
				break;
			case "cmd0100_Insert2":
				response.getWriter().write(cmd0100_Insert2(jsonElement));
				break;
			case "cmd0100_Insert3":
				response.getWriter().write(cmd0100_Insert3(jsonElement));
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
	* String UserID,String SysCd,String SecuYn,String RsrcCd,String JobCd,String SelMsg
	*/ 
	private String getDir(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0100.getDir(UserID,SysCd,SecuYn,RsrcCd,JobCd,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String RsrcCd
	*/ 
	private String getDir_Tmax(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    return ParsingCommon.toJson(cmd0100.getDir_Tmax(UserID,SysCd,RsrcCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String UserID,String SysCd,String RsrcCd
	*/ 
	private String getDir_Tmax2(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserID"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    return ParsingCommon.toJson(cmd0100.getDir_Tmax2(UserID,SysCd,RsrcCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String SelMsg
	*/ 
	private String getRsrcOpen(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0100.getRsrcOpen(SysCd,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String SysCd,String RsrcCd,String SelMsg
	*/ 
	private String getLang(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0100.getLang(SysCd,RsrcCd,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String RsrcCd,String Isrid,String RsrcName,boolean SecuSw
	*/ 
	private String getOpenList(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String Isrid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Isrid"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    boolean SecuSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuSw")));
		    return ParsingCommon.toJson(cmd0100.getOpenList(UserId,SysCd,RsrcCd,Isrid,RsrcName,SecuSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String OrderId,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String DirPath,String CM_info,boolean dirSw,String RsRcGuBun,String ComPile,String MakeComPile,String Team,String DOCCD,String ExeName,String MasterID,String EtcDsn,String EtcDsnHome
	*/ 
	private String pgmCheck(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String ProgTit = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgTit"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    String CM_info = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CM_info"));
		    boolean dirSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "dirSw")));
		    String RsRcGuBun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsRcGuBun"));
		    String ComPile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ComPile"));
		    String MakeComPile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MakeComPile"));
		    String Team = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Team"));
		    String DOCCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DOCCD"));
		    String ExeName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ExeName"));
		    String MasterID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "MasterID"));
		    String EtcDsn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EtcDsn"));
		    String EtcDsnHome = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "EtcDsnHome"));
		    return ParsingCommon.toJson(cmd0100.pgmCheck(UserId,SysCd,OrderId,DsnCd,RsrcName,RsrcCd,JobCd,ProgTit,DirPath,CM_info,dirSw,RsRcGuBun,ComPile,MakeComPile,Team,DOCCD,ExeName,MasterID,EtcDsn,EtcDsnHome));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SysCd,String OrderId,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String DirPath,String BaseItem,String CM_info,String RsRcGuBun,String ComPile,String Team,String SQLCD,String DOCCD,String ExeName
	*/ 
	private String moduleChk(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    String OrderId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "OrderId"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String RsrcName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcName"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "JobCd"));
		    String ProgTit = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ProgTit"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    String BaseItem = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "BaseItem"));
		    String CM_info = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "CM_info"));
		    String RsRcGuBun = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsRcGuBun"));
		    String ComPile = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ComPile"));
		    String Team = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Team"));
		    String SQLCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SQLCD"));
		    String DOCCD = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DOCCD"));
		    String ExeName = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ExeName"));
		    return ParsingCommon.toJson(cmd0100.moduleChk(UserId,SysCd,OrderId,DsnCd,RsrcName,RsrcCd,JobCd,ProgTit,DirPath,BaseItem,CM_info,RsRcGuBun,ComPile,Team,SQLCD,DOCCD,ExeName));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String UserId,ArrayList<HashMap<String,String>> dataList
	*/ 
	private String cmr0020_Delete(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
	   	    ArrayList<HashMap<String,String>> dataList = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "dataList"));
		    return ParsingCommon.toJson(cmd0100.cmr0020_Delete(UserId,dataList));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String SelMsg
	*/ 
	private String getRsrcOpen2(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0100.getRsrcOpen2(SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String Syscd,String SelMsg
	*/ 
	private String getCompile(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String Syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Syscd"));
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    return ParsingCommon.toJson(cmd0100.getCompile(Syscd,SelMsg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String SelMsg,String User
	*/ 
	private String getPrjList_Chg(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String User = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "User"));
		    return ParsingCommon.toJson(cmd0100.getPrjList_Chg(SelMsg,User));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String userid,String syscod
	*/ 
	private String Cmd0100_search(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    String syscod = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscod"));
		    return ParsingCommon.toJson(cmd0100.Cmd0100_search(userid,syscod));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* String userid,String syscod
	*/ 
	private String Cmd0100_search2(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
		    String userid = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userid"));
		    String syscod = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscod"));
		    String jobcd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "jobcd"));
		    String progname = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "progname"));
		    return ParsingCommon.toJson(cmd0100.Cmd0100_search2(userid,syscod,jobcd,progname));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCmd0100List
	*/ 
	private String cmd0100_Insert(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
	   	    ArrayList<HashMap<String,String>> getCmd0100List = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCmd0100List"));
		    return ParsingCommon.toJson(cmd0100.cmd0100_Insert(getCmd0100List));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCmd0100List,ArrayList<HashMap<String,String>> getCmd0100List2
	*/ 
	private String cmd0100_Insert2(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
	   	    ArrayList<HashMap<String,String>> getCmd0100List = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCmd0100List"));
	   	    ArrayList<HashMap<String,String>> getCmd0100List2 = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCmd0100List2"));
		    return ParsingCommon.toJson(cmd0100.cmd0100_Insert2(getCmd0100List,getCmd0100List2));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> getCmd0100List
	*/ 
	private String cmd0100_Insert3(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0100 cmd0100 = new Cmd0100();
		try {
	   	    ArrayList<HashMap<String,String>> getCmd0100List = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "getCmd0100List"));
		    return ParsingCommon.toJson(cmd0100.cmd0100_Insert3(getCmd0100List));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0100 = null;
		} 
	}
}
