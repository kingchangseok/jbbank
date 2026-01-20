<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.common.base.StringHelper"%>
<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
	String cr_ver = StringHelper.evl(request.getParameter("cr_ver"),"");
	String rsrcname = StringHelper.evl(request.getParameter("rsrcname"),"");
	String rsrccd = StringHelper.evl(request.getParameter("rsrccd"),"");
    String codeList = request.getParameter("codeList");
%>
<c:import url="/webPage/common/common.jsp" />

<style>
textarea{ border: 1px solid #ddd; }
</style>

<!-- contener S -->
<div id="wrapper" style="overflow:auto">
    <div class="content">
        <!-- line1 S-->   
        <div id="history_wrap">	요청상세 <strong>&gt; 소스보기 PL/SQL</strong></div> 
        <!-- line2 s-->
		
		<div class="row" style="font-size:0px;">
			<label class="tit_80 poa">프로그램명 : </label>
			<div class="ml_80">
				<input id="txtRsrcName" type="text" readonly=""/>
				<button class="btn_basic_s margin-2-left" id="btnClose" style="float: right;">닫기</button>
			</div>
		</div>
		
		<div class="row" style="font-size:0px;">
			<textarea id="sourceCode" name="sourceCode" style="align-content:flex-start; width:100%; height: calc(100% - 80px); resize: none; overflow-y:auto; padding:5px;" readonly></textarea>
		</div>
		<!-- line4 e-->
		
	</div>
</div>

<form name="getSrcData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
	<input type="hidden" name="cr_ver" value="<%=cr_ver%>"/>
	<input type="hidden" name="rsrcname" value="<%=rsrcname%>"/>
	<input type="hidden" name="rsrccd" value="<%=rsrccd%>"/>
	<input type="hidden" name="codeList" value="<%=codeList%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopSourceViewRequest.js"/>"></script>