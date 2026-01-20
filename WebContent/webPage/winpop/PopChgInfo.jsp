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
		<div id="history_wrap"><strong>변경관리정보</strong></div>

		<div class="half_wrap" style="height:17%;">
			<iframe id="frmBaseISRInfo" name="frmBaseISRInfo" src='/webPage/sr/baseISRInfo.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>
	
		<div id="srTab" class="half_wrap" style="height:79%; margin-top:10px;">
			<div class="tab_wrap">
				<ul class="tabs">
					<li rel="tabRFCAccept" id="tab1">RFC접수</li>
					<li rel="tabDEVPlan" id="tab2">개발계획/실적등록</li>
					<li rel="tabUnitTest" id="tab3">단위테스트</li>
					<li rel="tabIntegrationTest" id="tab4">통합테스트</li>
					<li rel="tabTargetProgram" id="tab5">대상프로그램</li>
					<li rel="tabTargetOutput" id="tab6">대상산출물</li>
					<li rel="tabWorkPlan" id="tab7">작업계획서</li>
					<li rel="tabRFCComplete" id="tab8">변경관리종료</li>
					<div class="r_wrap">
						<button class="btn_basic_s" id="btnReqInfo" style="margin-left: 0px;margin-right: 0px;" disabled>요구관리정보</button>
						<button class="btn_basic_s" id="btnTestInfo" style="margin-left: 0px;margin-right: 0px;" disabled>테스트관리정보</button>
					</div>			
				</ul>
			</div>
		
			<div class="half_wrap" style="height:91%">
				<div id="tabRFCAccept" class="tab_content" style="width:100%">
					<iframe id="frmRFCAccept" name="frmRFCAccept" src='/webPage/tab/rfc/RFCAcceptTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>			
				<div id="tabDEVPlan" class="tab_content" style="width:100%">
					<iframe id="frmDEVPlan" name="frmDEVPlan" src='/webPage/tab/rfc/DEVPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>
				<div id="tabUnitTest" class="tab_content" style="width:100%">
					<iframe id="frmUnitTest" name="frmUnitTest" src='/webPage/tab/rfc/UnitTestTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>	
				<div id="tabTargetProgram" class="tab_content" style="width:100%">
					<iframe id="frmTargetProgram" name="frmTargetProgram" src='/webPage/tab/rfc/TargetProgramTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>	
				<div id="tabTargetOutput" class="tab_content" style="width:100%">
					<iframe id="frmTargetOutput" name="frmTargetOutput" src='/webPage/tab/rfc/TargetOutputTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>
				<div id="tabIntegrationTest" class="tab_content" style="width:100%">
					<iframe id="frmIntegrationTest" name="frmIntegrationTest" src='/webPage/tab/rfc/IntegrationTestTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>
				<div id="tabWorkPlan" class="tab_content" style="width:100%">
					<iframe id="frmWorkPlan" name="frmWorkPlan" src='/webPage/tab/rfc/WorkPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
				</div>								
				<div id="tabRFCComplete" class="tab_content" style="width:100%">
					<iframe id="frmRFCComplete" name="frmRFCComplete" src='/webPage/tab/rfc/RFCCompleteTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
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
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopChgInfo.js"/>"></script>