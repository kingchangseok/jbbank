<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<div class="contentFrame">

	<div id="history_wrap"></div>
    
    <!-- PrjListTab.jsp -->
    <div class="az_board_basic" style="height:28%">
		<div class="l_wrap width-48 dib vat" style="width:100%; height:100%">
    		<iframe id="frmPrjList" name="frmPrjList" src='/webPage/tab/sr/PrjListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
   		</div>
    </div>
    
	<!-- 하단 S-->
	<div class="half_wrap margin-10-top" style="height: 68%">
		<!-- tab S-->
		<div class="tab_wrap">
			<ul class="tabs">
				<li rel="tabSRRegister" id="tab1">SR등록</li>
				<li rel="tabPrgList" id="tab2">프로그램목록</li>
				<li rel="tabDevPlan" id="tab3">개발계획/실적등록</li>
				<li rel="tabUnitTest" id="tab4" class="on">단위테스트</li>
			</ul>
		</div>
		<!-- tab E-->
		
		<div class="half_wrap margin-10-top" style="height:90%">
	       	<div id="tabSRRegister" class="tab_content" style="width:100%">
	       		<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	<div id="tabPrgList" class="tab_content" style="width:100%">
	       		<iframe id="frmPrgList" name="frmPrgList" src='/webPage/tab/sr/PrgListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	<div id="tabDevPlan" class="tab_content" style="width:100%">
	       		<iframe id="frmDevPlan" name="frmDevPlan" src='/webPage/tab/sr/DevPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	       	<div id="tabUnitTest" class="tab_content" style="width:100%">
	       		<iframe id="frmUnitTest" name="frmUnitTest" src='/webPage/tab/sr/UnitTestTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	       	</div>
	   	</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/sr/UnitTest.js"/>"></script>