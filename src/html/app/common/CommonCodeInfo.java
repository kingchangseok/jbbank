/**
 * CodeInfo ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½
 * <pre>
 * <b>History</b>
 * 	ï¿½Û¼ï¿½ï¿½ï¿½: ï¿½Ì¿ë¹®
 * 	ï¿½ï¿½ï¿½ï¿½ : 1.0
 *  ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ : 2019-02-07
 */

package html.app.common;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecams.util.exception.ErrorHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import app.common.CodeInfo;
import app.common.excelUtil;

@WebServlet("/webPage/common/CommonCodeInfo")
public class CommonCodeInfo extends HttpServlet {
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
				case "CODE_INFO" :
					response.getWriter().write( getCodeInfo(jsonElement) );
					break;
				case "getArrayCollection":
					response.getWriter().write(getArrayCollection(jsonElement));
					break;
				default:
					throw new Exception("Servlet Function ¹Ì Á¸Àç");
			}
		} catch(SQLException e1) {
			ErrorHandler.handleError(request, response, e1);
		} catch(Exception e2) {
			ErrorHandler.handleError(request, response, e2);
		} finally {
			requestType = null;
		}

	}//end of getSysInfo() method statement

	private String getCodeInfo(JsonElement jsonElement) throws SQLException, Exception {
		CodeInfo codeInfo = new CodeInfo();
		ArrayList<HashMap<String, String>> codeInfoArr = ParsingCommon.jsonArrToArr(ParsingCommon.jsonEtoStr(jsonElement, "codeInfoData"));
		return ParsingCommon.toJson( codeInfo.getCodeInfoWithArray( codeInfoArr ));
	}

	private String getArrayCollection(JsonElement jsonElement) throws SQLException, Exception {
		excelUtil excelUtil = new excelUtil();
		String filePath = null;
		ArrayList<String> headerDef = null;

		filePath = ParsingCommon.jsonStrToStr(ParsingCommon.jsonEtoStr(jsonElement, "filePath"));
		headerDef = ParsingCommon.jsonStrToArrStr(ParsingCommon.jsonEtoStr(jsonElement, "headerDef"));
		if(headerDef != null) {
			return ParsingCommon.toJson(excelUtil.getArrayCollection(filePath, headerDef));
		} else {
			return "ERR";
		}
	}
}
