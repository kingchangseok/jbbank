<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<link rel="stylesheet" href="<c:url value="/styles/bootstrap-timepicker.css"/>" />

<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
%>

<body style="padding: 10px;">
    <div class="content">
        <div id="history_wrap">	배포 <strong>&gt; 거래제어확인</strong></div>
        
		<div class="row vat cb">
            <div class="width-100">
            	<textarea id="txtResult" class="width-100" style="height: 95%; resize:none; overflow-y:auto; border: 1px solid #DDD;" readonly></textarea>
			</div>
		</div>
		<div class="row vat cb float-right">
			<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
		</div>
    </div>
</body>

<form name="getReqData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script src="<c:url value="/scripts/bootstrap-timepicker.min.js"/>" type="text/javascript"></script>
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopTransactionResult.js"/>"></script>