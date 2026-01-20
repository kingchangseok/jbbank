<%
/*****************************************************************************************
	1. program ID	: HandyCount1.jsp
	2. create date	: 2011. 09. 02
	3. auth		    : 최 병 남
	4. update date	: 
	5. auth		    : 
	6. description	: 그룹웨어와 인터페이스 (결재대기건수)
*****************************************************************************************/
%>
<%@ page language="java" contentType="text/html;charset=euc-kr" pageEncoding="euc-kr" %>
<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.service.list.LoginManager"%>
<%@ page import = "com.ecams.service.main.dao.MainDAO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	LoginManager loginManager = LoginManager.getInstance();
%>
<style type = text/css>
font {
	color: #6C3734;
	font-size: 9pt;
	height: 40px;
	border-width: 1px;
}
span {
	color: #003399;
	font-size: 9pt;
	height: 40px;
    border-top:1px;
	border-width: 1px;
    font-weight:bold;
}
</style>

<body id="auth_session">
	<form name='user_auth'>
<%
	String strUsr_UserId  = StringHelper.evl(request.getParameter("UserID"),"");;
    String strRet = ""; 
  	
	strRet = loginManager.selectAprvCnt(strUsr_UserId);
%>
	<span><%=strRet%></span>
	</form>
</body>
