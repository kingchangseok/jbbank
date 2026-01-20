<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<div class="contentFrame">
	<div id="history_wrap"></div>
    
    <!-- PrjListTab.jsp -->
    <div class="az_board_basic" style="height:30%">
		<div class="l_wrap width-48 dib vat" style="width:100%; height:100%">
			<!-- frmPrjList -->
   			<iframe id="frmPrjList" name="frmPrjList" src='/webPage/tab/sr/PrjListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
   		</div>
    </div>
    
	<!-- 하단 S-->
	<div class="half_wrap" style="height: 67%">
		<!-- tab S-->
		<div class="tab_wrap">
			<ul class="tabs">
				<li rel="tabSRRegister" id="tab1">SR등록</li>
				<li rel="tabPrgList" id="tab2">프로그램목록</li>
				<li rel="tabDevPlan" id="tab3">개발계획/실적등록</li>
				<li rel="tabUnitTest" id="tab4">단위테스트</li>
				<li rel="tabDevCheck" id="tab5">개발검수</li>
				<li rel="tabMonitoring" id="tab6" class="on">모니터링체크</li>
			</ul>
		</div>
		<!-- tab E-->
		
		<div class="half_wrap margin-10-top" style="height:91%"> <!--  tab_container -->
	       	<!-- SR등록 -->
	       	<div id="tabSRRegister" class="tab_content" style="width:100%">
	       		<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	
	       	<!-- 프로그램목록 -->
	       	<div id="tabPrgList" class="tab_content" style="width:100%">
	       		<iframe id="frmPrgList" name="frmSRComplete" src='/webPage/tab/sr/PrgListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	
	       	<!-- 개발계획/실적등록  -->
	       	<div id="tabDevPlan" class="tab_content" style="width:100%">
	       		<iframe id="frmDevPlan" name="frmDevPlan" src='/webPage/tab/sr/DevPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	
	       	<!-- 단위테스트 -->
	       	<div id="tabUnitTest" class="tab_content" style="width:100%">
	       		<iframe id="frmUnitTest" name="frmSRComplete" src='/webPage/tab/sr/UnitTestTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	
	       	<!-- 개발검수 -->
	       	<div id="tabDevCheck" class="tab_content" style="width:100%">
	       		<iframe id="frmDevCheck" name="frmSRComplete" src='/webPage/tab/sr/DevCheckTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	
	       	<!-- 모니터링체크 -->
	       	<div id="tabMonitoring" class="tab_content" style="width:100%">
	       		<iframe id="frmMonitoring" name="frmSRComplete" src='/webPage/tab/sr/MonitoringChkTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	   	</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/sr/MonitoringCheck.js"/>"></script>