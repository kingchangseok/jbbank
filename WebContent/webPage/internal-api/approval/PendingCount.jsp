<%
/*****************************************************************************************
	1. program ID	: PendingCount.jsp
	2. create date	: 2025. 12. 11
	3. auth		    : 이 동 준
	4. update date	: 
	5. auth		    : 
	6. description	: 결재대기건수 JSON API
*****************************************************************************************/
%>
<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.http.client.HttpResponseException"%>
<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.service.list.LoginManager"%>
<%@ page import = "com.ecams.service.main.dao.MainDAO"%>

<%
	request.setCharacterEncoding("UTF-8");
	String strUsr_UserId  = StringHelper.evl(request.getParameter("UserID"),"");
	
	// 행번이 없거나 6자가 아닐 경우 400 에러
	if("".equals(strUsr_UserId) || strUsr_UserId.length() != 6) {
		throw new HttpResponseException(400, "Invalid Parameter: UserID");
	}
	
	LoginManager loginManager = LoginManager.getInstance();
    String cnt = loginManager.selectAprvCnt(strUsr_UserId);
    
    response.setContentType("application/json; charset=UTF-8");
    response.setStatus(200);
    
    out.println("{ \"Count\":"+ cnt + "}");
%>
