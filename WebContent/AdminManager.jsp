<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>    
<%@ page import="java.sql.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>형상...</title>
</head>
<body>

<form name="form1" action="NewFile.jsp" method="post">
<h2>SYSCB - 컴파일</h2>
<h2>SYSGB - 배포파일수신</h2>
<h2>SYSED - 운영배포</h2>
<h2>SYSTS - 검증배포</h2>
<h2>SYSUP - 형상관리저장</h2>
<h2>SYSJAR - JAR생성</h2>
신청번호 <input type="text" name="reqNo" />
<select name="reqType">
	<option name=""></option>
	<option name="SYSCB">SYSCB</option>
	<option name="SYSGB">SYSGB</option>
	<option name="SYSED">SYSED</option>
	<option name="SYSTS">SYSTS</option>
	<option name="SYSUP">SYSUP</option>
	<option name="SYSJAR">SYSJAR</option>
</select>
<input type="submit" value="전송" />
</form>
<%

String reqNo   = request.getParameter("reqNo");
String reqType = request.getParameter("reqType");

Connection conn = null;
CallableStatement cstmt = null;

if (reqNo != null && reqType != null) {
	
	if (reqNo.length() != 12) {
		out.println("신청번호 자릿수 이상합니당.(12자리):"+reqNo);		
		return;
	} 
		
	try {
		String O_driverClassName="oracle.jdbc.driver.OracleDriver";
		String O_url="jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.15.145)(PORT=9260)))(CONNECT_DATA=(SRVR=DEDICATED)(SERVICE_NAME=oraonl)))";
		String O_username="scm";
		String O_password="scm123";

		
		//CMR9900_STR('201604004653', 'SYSTS', 'eCAMS 자동처리', '9', '', '1');
		String query = "{call CMR9900_STR(?, ?, ?, ?, ?, ?)}";
		
		Class.forName(O_driverClassName);
		conn = DriverManager.getConnection(O_url, O_username, O_password);
		
		cstmt = conn.prepareCall(query);
				
		cstmt.setString(1, reqNo);//형상신청번호
		cstmt.setString(2, reqType);//처리타입
		cstmt.setString(3, "eCAMS 자동처리");
		cstmt.setString(4, "9");
		cstmt.setString(5, "");
		cstmt.setString(6, "1");	
		
		//실행
		cstmt.executeQuery();
		out.println("정상처리되었습니다.." + reqNo);
		
	} catch(Exception e) {
			
		e.printStackTrace();		
	} finally {
		if (cstmt != null) try{cstmt.close();} catch (SQLException se){}
		if (conn != null) try{conn.close();} catch (SQLException se){}
	}
}
%>
	 

</body>
</html>
