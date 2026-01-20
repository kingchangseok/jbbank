<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String isrId = StringHelper.evl(request.getParameter("isrid"),"");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
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
		<div id="history_wrap"><strong>SR정보</strong></div>
		
		<div class="az_search_wrap" style="height:80px">
			<div class="az_in_wrap">
				<div class="l_wrap dib" style="width: 100%;">
					<div class="por">
						<div class="width-25 dib vat">
							<label class="tit-60 dib poa">SR-ID</label>
		                	<div class="ml_60 vat">
								<input id="txtSRID" type="text" class="width-100" readonly/>
		                	</div>
						</div>
						<div class="width-25 dib vat">
							<label class="tit-60 dib poa margin-10-left">SR상태</label>
		                	<div class="ml_60 vat">
								<input id="txtSta" type="text" class="width-100" readonly/>
		                	</div>
						</div>
					</div>
					<div class="row por">
						<div class="width-100 dib vat">
							<label class="tit-60 dib poa">SR명</label>
		                	<div class="ml_60 vat">
								<input id="txtSRTitle" type="text" class="width-100" readonly/>
		                	</div>
						</div>
					</div>
				</div>
			</div>
		</div>
			
		<!-- Tab S -->
		<div id="srTab" class="half_wrap" style="height:calc(100% - 130px); margin-top:5px;">
			<div class="tab_wrap">
				<ul class="tabs">
					<li rel="tabSRRegister" id="tab1">SR등록</li>
					<li rel="tabDevPlan" id="tab2">개발계획/실적등록</li>
					<li rel="tabReqHistory" id="tab3">변경요청이력</li>
					<li rel="tabPrgList" id="tab4">프로그램목록</li>
					<li rel="tabUnitTest" id="tab5">단위테스트</li>
					<li rel="tabDevCheck" id="tab6">개발검수</li>
					<li rel="tabMonitoring" id="tab7">모니터링체크</li>
					<li rel="tabSRComplete" id="tab8">SR완료</li>
				</ul>
			</div>
		
			<div class="margin-10-top" style="height:90%">
		       	<div id="tabSRRegister" class="tab_content" style="width:100%">
		       		<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabDevPlan" class="tab_content" style="width:100%">
		       		<iframe id="frmDevPlan" name="frmDevPlan" src='/webPage/tab/sr/DevPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabReqHistory" class="tab_content" style="width:100%">
		       		<iframe id="frmReqHistory" name="frmReqHistory" src='/webPage/tab/sr/ReqHistoryTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabPrgList" class="tab_content" style="width:100%">
		       		<iframe id="frmPrgList" name="frmPrgList" src='/webPage/tab/sr/PrgListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabUnitTest" class="tab_content" style="width:100%">
		       		<iframe id="frmUnitTest" name="frmUnitTest" src='/webPage/tab/sr/UnitTestTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabDevCheck" class="tab_content" style="width:100%">
		       		<iframe id="frmDevCheck" name="frmDevCheck" src='/webPage/tab/sr/DevCheckTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabMonitoring" class="tab_content" style="width:100%">
		       		<iframe id="frmMonitoring" name="frmMonitoring" src='/webPage/tab/sr/MonitoringChkTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabSRComplete" class="tab_content" style="width:100%">
		       		<iframe id="frmSRComplete" name="frmSRComplete" src='/webPage/tab/sr/SRCompleteTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		   	</div>
		</div>
	</div> 
</body>

<form name="getReqData">
	<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="acptno" value="<%=acptNo%>"/>
	<input type="hidden" id="isrId" name="isrId"  value="<%=isrId%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopSRInfo.js"/>"></script>