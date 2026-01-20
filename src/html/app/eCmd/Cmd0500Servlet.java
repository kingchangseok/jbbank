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

import app.eCmd.Cmd0500;

@WebServlet("/webPage/ecmd/Cmd0500Servlet")
public class Cmd0500Servlet extends HttpServlet {

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
			case "Cmd0500_Cbo_Set":
				response.getWriter().write(Cmd0500_Cbo_Set(jsonElement));
				break;
			case "getCbo_SysCd":
				response.getWriter().write(getCbo_SysCd(jsonElement));
				break;
			case "getCbo_LangCd":
				response.getWriter().write(getCbo_LangCd(jsonElement));
				break;
			case "getDir_Check":
				response.getWriter().write(getDir_Check(jsonElement));
				break;
			case "getSql_Qry":
				response.getWriter().write(getSql_Qry(jsonElement));
				break;
			case "getSql_Qry_Sub":
				response.getWriter().write(getSql_Qry_Sub(jsonElement));
				break;
			case "getProgInfo":
				response.getWriter().write(getProgInfo(jsonElement));
				break;
			case "getItemId":
				response.getWriter().write(getItemId(jsonElement));
				break;
			case "Cmd0500_Lv_File_ItemClick":
				response.getWriter().write(Cmd0500_Lv_File_ItemClick(jsonElement));
				break;
			case "getTeamInfoGrid":
				response.getWriter().write(getTeamInfoGrid(jsonElement));
				break;
			case "getSql_Qry_Hist":
				response.getWriter().write(getSql_Qry_Hist(jsonElement));
				break;
			case "getSql_Info":
				response.getWriter().write(getSql_Info(jsonElement));
				break;
			case "getCbo_ReqCd_Add":
				response.getWriter().write(getCbo_ReqCd_Add(jsonElement));
				break;
			case "getTbl_Update":
				response.getWriter().write(getTbl_Update(jsonElement));
				break;
			case "getTbl_Delete":
				response.getWriter().write(getTbl_Delete(jsonElement));
				break;
			case "getAllTbl_Delete":
				response.getWriter().write(getAllTbl_Delete(jsonElement));
				break;
			case "getTbl_Close":
				response.getWriter().write(getTbl_Close(jsonElement));
				break;
			case "getTbl_alive":
				response.getWriter().write(getTbl_alive(jsonElement));
				break;
			case "setTbl_Close":
				response.getWriter().write(setTbl_Close(jsonElement));
				break;
			case "getCbo_Editor_Add":
				response.getWriter().write(getCbo_Editor_Add(jsonElement));
				break;
			case "verSync":
				response.getWriter().write(verSync(jsonElement));
				break;
			case "adminUser":
				response.getWriter().write(adminUser(jsonElement));
				break;
			case "insertProject":
				response.getWriter().write(insertProject(jsonElement));
				break;
			case "getCheckConn":
				response.getWriter().write(getCheckConn(jsonElement));
				break;
			case "getDoc":
				response.getWriter().write(getDoc(jsonElement));
				break;
			case "btnRPA_Click":
				response.getWriter().write(btnRPA_Click(jsonElement));
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
	* String L_Syscd,String SecuYn,String UserId,String ItemId
	*/ 
	private String Cmd0500_Cbo_Set(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String L_Syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_Syscd"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmd0500.Cmd0500_Cbo_Set(L_Syscd,SecuYn,UserId,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SecuYn,String WkSys
	*/ 
	private String getCbo_SysCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String WkSys = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "WkSys"));
		    return ParsingCommon.toJson(cmd0500.getCbo_SysCd(UserId,SecuYn,WkSys));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String L_SysCd,String RsrcCd
	*/ 
	private String getCbo_LangCd(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String L_SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_SysCd"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    return ParsingCommon.toJson(cmd0500.getCbo_LangCd(L_SysCd,RsrcCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SecuYn,String L_Syscd,String L_ItemId,String RsrcCd,String L_DsnCd,String FindFg
	*/ 
	private String getDir_Check(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String L_Syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_Syscd"));
		    String L_ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_ItemId"));
		    String RsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "RsrcCd"));
		    String L_DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_DsnCd"));
		    String FindFg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "FindFg"));
		    return ParsingCommon.toJson(cmd0500.getDir_Check(UserId,SecuYn,L_Syscd,L_ItemId,RsrcCd,L_DsnCd,FindFg));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SecuYn,String ViewFg,String L_Syscd,String Txt_ProgId,String DsnCd,String DirPath,boolean LikeSw
	*/ 
	private String getSql_Qry(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String ViewFg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ViewFg"));
		    String L_Syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_Syscd"));
		    String Txt_ProgId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Txt_ProgId"));
		    String DsnCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DsnCd"));
		    String DirPath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "DirPath"));
		    boolean LikeSw = Boolean.parseBoolean(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "LikeSw")));
		    return ParsingCommon.toJson(cmd0500.getSql_Qry(UserId,SecuYn,ViewFg,L_Syscd,Txt_ProgId,DsnCd,DirPath,LikeSw));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String ItemId,String SysCd
	*/ 
	private String getSql_Qry_Sub(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SysCd"));
		    return ParsingCommon.toJson(cmd0500.getSql_Qry_Sub(UserId,ItemId,SysCd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String ItemId
	*/ 
	private String getProgInfo(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmd0500.getProgInfo(ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String ItemId
	*/ 
	private String getItemId(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmd0500.getItemId(ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String SecuYn,String L_SysCd,String L_JobCd,String L_ItemId
	*/ 
	private String Cmd0500_Lv_File_ItemClick(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String SecuYn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SecuYn"));
		    String L_SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_SysCd"));
		    String L_JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_JobCd"));
		    String L_ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_ItemId"));
		    return ParsingCommon.toJson(cmd0500.Cmd0500_Lv_File_ItemClick(UserId,SecuYn,L_SysCd,L_JobCd,L_ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String SelMsg,String cm_useyn
	*/ 
	private String getTeamInfoGrid(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String SelMsg = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "SelMsg"));
		    String cm_useyn = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "cm_useyn"));
		    return ParsingCommon.toJson(cmd0500.getTeamInfoGrid(SelMsg,cm_useyn));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String UserId,String L_SysCd,String L_JobCd,String Cbo_ReqCd,String L_ItemId
	*/ 
	private String getSql_Qry_Hist(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String UserId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "UserId"));
		    String L_SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_SysCd"));
		    String L_JobCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_JobCd"));
		    String Cbo_ReqCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Cbo_ReqCd"));
		    String L_ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_ItemId"));
		    return ParsingCommon.toJson(cmd0500.getSql_Qry_Hist(UserId,L_SysCd,L_JobCd,Cbo_ReqCd,L_ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String L_SysCd,String WkRsrcCd,String WkVer,int Lv_Src_Cnt
	*/ 
	private String getSql_Info(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String L_SysCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_SysCd"));
		    String WkRsrcCd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "WkRsrcCd"));
		    String WkVer = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "WkVer"));
		    int Lv_Src_Cnt = Integer.parseInt(ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Lv_Src_Cnt")));
		    return ParsingCommon.toJson(cmd0500.getSql_Info(L_SysCd,WkRsrcCd,WkVer,Lv_Src_Cnt));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* 
	*/ 
	private String getCbo_ReqCd_Add(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    return ParsingCommon.toJson(cmd0500.getCbo_ReqCd_Add());
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* HashMap<String,String> etcData
	*/ 
	private String getTbl_Update(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    HashMap<String,String> etcData = ParsingCommon.jsonStrToMap(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmd0500.getTbl_Update(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String L_ItemId
	*/ 
	private String getTbl_Delete(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String L_ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_ItemId"));
		    return ParsingCommon.toJson(cmd0500.getTbl_Delete(L_ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String itemId
	*/ 
	private String getAllTbl_Delete(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String itemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemId"));
		    return ParsingCommon.toJson(cmd0500.getAllTbl_Delete(itemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String L_ItemId,String editor
	*/ 
	private String getTbl_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String L_ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_ItemId"));
		    String editor = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "editor"));
		    return ParsingCommon.toJson(cmd0500.getTbl_Close(L_ItemId,editor));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String L_ItemId,String editor
	*/ 
	private String getTbl_alive(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String L_ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "L_ItemId"));
		    String editor = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "editor"));
		    return ParsingCommon.toJson(cmd0500.getTbl_alive(L_ItemId,editor));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String itemId,String editor
	*/ 
	private String setTbl_Close(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String itemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemId"));
		    String editor = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "editor"));
		    return ParsingCommon.toJson(cmd0500.setTbl_Close(itemId,editor));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String ItemId,String Editor
	*/ 
	private String getCbo_Editor_Add(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    String Editor = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Editor"));
		    return ParsingCommon.toJson(cmd0500.getCbo_Editor_Add(ItemId,Editor));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String itemID,String userID,String Syscd
	*/ 
	private String verSync(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String itemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemID"));
		    String userID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userID"));
		    String Syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "Syscd"));
		    return ParsingCommon.toJson(cmd0500.verSync(itemID,userID,Syscd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String userID
	*/ 
	private String adminUser(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String userID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "userID"));
		    return ParsingCommon.toJson(cmd0500.adminUser(userID));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* ArrayList<HashMap<String,String>> etcData
	*/ 
	private String insertProject(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
	   	    ArrayList<HashMap<String,String>> etcData = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "etcData"));
		    return ParsingCommon.toJson(cmd0500.insertProject(etcData));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String itemID,String syscd
	*/ 
	private String getCheckConn(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String itemID = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemID"));
		    String syscd = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "syscd"));
		    return ParsingCommon.toJson(cmd0500.getCheckConn(itemID,syscd));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	/* Parameter 
	* String PrjNo,String ItemId
	*/ 
	private String getDoc(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String PrjNo = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "PrjNo"));
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "ItemId"));
		    return ParsingCommon.toJson(cmd0500.getDoc(PrjNo,ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
	
	private String btnRPA_Click(JsonElement jsonElement) throws SQLException, Exception {
		Cmd0500 cmd0500 = new Cmd0500();
		try {
		    String ItemId = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "itemID"));
		    return ParsingCommon.toJson(cmd0500.btnRPA_Click(ItemId));
		} catch (IOException e) {
		    return ParsingCommon.toJson("ERROR"+e.getMessage());
		} catch (Exception e) {
			return ParsingCommon.toJson("ERROR"+e.getMessage());
		} finally {
		    cmd0500 = null;
		} 
	}
}
