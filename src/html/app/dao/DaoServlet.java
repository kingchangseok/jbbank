package html.app.dao;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecams.service.bbs.dao.BbsDAO;
import com.ecams.service.main.dao.MainDAO;
import com.ecams.service.member.dao.MemberDAO;
import com.ecams.service.passwd.dao.PassWdDAO;
import com.ecams.util.exception.ErrorHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import html.app.common.ParsingCommon;

@WebServlet("/webPage/dao/DaoServlet")
public class DaoServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(ParsingCommon.getJsonStr(request));
		String requestType	= ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "requestType") );

		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");

			switch (requestType) {
				case "getUserName" :
					response.getWriter().write(getUserName(jsonElement));
					break;
				case "getPasswdBef":
					response.getWriter().write( selectPassWd(jsonElement) );
					break;
				case "setPassWd":
					response.getWriter().write( updtPassWd(jsonElement) );
					break;
				case "BbsDAO":
					response.getWriter().write( getBbsDAO(jsonElement) );
					break;
				case "selectAprvCnt":
					response.getWriter().write( selectAprvCnt(jsonElement) );
					break;
				default:
					throw new Exception("Servlet Function");
			}
		} catch(SQLException e1) {
			ErrorHandler.handleError(request, response, e1);
		} catch(Exception e2) {
			ErrorHandler.handleError(request, response, e2);
		} finally {
	         requestType = null;
		}
	}

	private String getUserName(JsonElement jsonElement) throws SQLException, Exception {
		MemberDAO memberdao = new MemberDAO();
		String UserId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userId") );
		return ParsingCommon.toJson(memberdao.selectUserName(UserId));
	}
	private String selectPassWd(JsonElement jsonElement) throws SQLException, Exception {
		PassWdDAO passwddao = new PassWdDAO();
		String UserId = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "userId") );
		return ParsingCommon.toJson(passwddao.selectPassWd(UserId));
	}
	private String updtPassWd(JsonElement jsonElement) throws SQLException, Exception {
		PassWdDAO passwddao = new PassWdDAO();
		String user_id = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "user_id") );
		String usr_passwd = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "usr_passwd") );
		String bef_passwd = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "bef_passwd") );
		return ParsingCommon.toJson(passwddao.updtPassWd(user_id,usr_passwd,bef_passwd));
	}
	private String getBbsDAO(JsonElement jsonElement) throws SQLException, Exception {
		BbsDAO bbsdao = new BbsDAO();
		return ParsingCommon.toJson(bbsdao.NoticePOP("1", 7));
	}
	private String selectAprvCnt(JsonElement jsonElement) throws SQLException, Exception {
		MainDAO maindao = new MainDAO();
		String user_id = ParsingCommon.jsonStrToStr( ParsingCommon.jsonEtoStr(jsonElement, "user_id") );
		return ParsingCommon.toJson(maindao.selectAprvCnt(user_id));
	}
}
