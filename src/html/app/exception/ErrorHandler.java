package html.app.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorHandler  {
	public static void handleError(HttpServletRequest req, HttpServletResponse response, Exception ex) throws IOException {
		StringWriter errStr = new StringWriter();
//    	ex.printStackTrace(new PrintWriter(errStr)); 취약점 수정
		response.setContentType("application/json; charset=UTF-8");
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errStr.toString());
		ex.getStackTrace();
	}
}
