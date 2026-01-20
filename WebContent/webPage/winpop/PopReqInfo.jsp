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
<style type="text/css">
	.row:after {
			content:""; 
			display:block; 
			clear:both;
		}
</style>

<body style="width: 100% !important; padding: 10px;">
	<div class="content">    	
		<div id="history_wrap"><strong>요구관리정보</strong></div>

		<div class="half_wrap" style="height:17%;">
			<iframe id="frmBaseISRInfo" name="frmBaseISRInfo" src='/webPage/sr/baseISRInfo.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>
	
		<div id="srTab" class="half_wrap" style="height:79%; margin-top:10px;">
			<div class="tab_wrap">
				<ul class="tabs">
					<li rel="tabSRRegister" id="tab1">ISR요청정보</li>
					<li rel="tabSRAccept" id="tab2">ISR접수</li>
					<li rel="tabRFCRegister" id="tab3">RFC발행</li>
					<li rel="tabSRComplete" id="tab4">ISR종료</li>
					<li rel="tabSRCompleteByUser" id="tab5">요청자종료</li>
					<li rel="tabComment" id="tab6">COMMENT</li>
					<div class="r_wrap" style="margin-left:10px;">
						<button class="btn_basic_s" id="btnChgInfo" style="margin-left: 0px;margin-right: 0px;" disabled>변경관리정보</button>
						<button class="btn_basic_s" id="btnTestInfo" style="margin-left: 0px;margin-right: 0px;" disabled>테스트관리정보</button>
					</div>														
				</ul>
			</div>
		
			<div class="half_wrap" style="height:91%">
				<div id="tabSRRegister" class="tab_content" style="width:100%">
					<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>
				<div id="tabSRAccept" class="tab_content" style="width:100%">
					<iframe id="frmSRAccept" name="frmSRAccept" src='/webPage/tab/sr/SRAcceptTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>	
				<div id="tabRFCRegister" class="tab_content" style="width:100%">
					<iframe id="frmRFCRegister" name="frmRFCRegister" src='/webPage/tab/rfc/RFCRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>	
				<div id="tabSRComplete" class="tab_content" style="width:100%">
					<iframe id="frmSRComplete" name="frmSRComplete" src='/webPage/tab/sr/SRCompleteTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>
				<div id="tabSRCompleteByUser" class="tab_content" style="width:100%">
					<iframe id="frmSRCompleteByUser" name="frmSRCompleteByUser" src='/webPage/tab/sr/SRCompleteByUserTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>
				<div id="tabComment" class="tab_content" style="width:100%">
					<iframe id="frmComment" name="frmComment" src='/webPage/tab/sr/CommentTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
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
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopReqInfo.js"/>"></script>