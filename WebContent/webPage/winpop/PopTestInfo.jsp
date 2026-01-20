<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String code = StringHelper.evl(request.getParameter("code"),"");
	String reqCd = StringHelper.evl(request.getParameter("redcd"),"");
	String isrId = StringHelper.evl(request.getParameter("isrId"),"");
	String codeList = request.getParameter("codeList");
%>

<body style="width: 100% !important; padding: 10px;">
<div class="content">    	
<div id="history_wrap">	테스트관리 <strong>&gt; 테스트관리정보</strong></div>

   <div class="half_wrap" style="height:17%;margin-top: 15px;">
	   <iframe id="frmBaseTestInfo" name="frmBaseTestInfo" src='/webPage/test/baseTestInfo.jsp' width='100%' height='100%' ></iframe>
   </div>

   <div id="test_tab" class="half_wrap" style="height:77%; margin-top:10px;">
	   <div class="tab_wrap">
		   <ul class="tabs">
		       <li rel="tabTestAccept" id="tab1">테스트접수</li>
			   <li rel="tabIntegrationTesting" id="tab2">통합테스트</li>
			   <li rel="tabTestComplete" id="tab3"  class="on">테스트종료</li>
			   <div class="r_wrap">
				   <button class="btn_basic_s" id="btnReqInfo" >요구관리정보</button>
				   <button class="btn_basic_s" id="btnChgInfo" >변경관리정보</button>
			   </div>			
		   </ul>
	   </div>

	   <div class="half_wrap" style="height:90%">
		   <div id="tabTestAccept" class="tab_content" style="width:100%">
			   <iframe id="frmTestAccept" name="frmTestAccept" src='/webPage/tab/test/TestAcceptTab.jsp' width='100%' height='100%'></iframe>
		   </div>
		   <div id="tabIntegrationTesting" class="tab_content" style="width:100%">
			   <iframe id="frmIntegrationTesting" name="frmIntegrationTesting" src='/webPage/tab/rfc/IntegrationTestTab.jsp' width='100%' height='100%'></iframe>
		   </div>
		   <div id="tabTestComplete" class="tab_content" style="width:100%">
			   <iframe id="frmTestComplete" name="frmTestComplete" src='/webPage/tab/test/TestCompleteTab.jsp' width='100%' height='100%'></iframe>
		   </div>
	   </div>
	
   </div>
			
</div>
</body>

<input type="hidden" id="userId" name="userId" 	value="<%=userId%>"/>
<input type="hidden" id="code" name="code"  value="<%=code%>"/>
<input type="hidden" id="reqCd" name="reqCd"  value="<%=reqCd%>"/>
<input type="hidden" id="isrId" name="isrId"  value="<%=isrId%>"/>
<input type="hidden" id="codeList" name="codeList" value="<%=codeList%>"/>

<form name="popPam">
	<input type="hidden" name="user"/>
	<input type="hidden" name="code"/>
    <input type="hidden" name="redcd"/>
    <input type="hidden" name="isrId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopTestInfo.js"/>"></script>