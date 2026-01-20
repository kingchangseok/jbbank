<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap"></div>
        <!-- history E-->         
	    
	    <!-- PrjListTab.jsp -->
	    <div class="az_board_basic" style="height:28%">
			<div class="l_wrap width-48 dib vat" style="width:100%; height:100%">
				<div id="divPicker" data-ax5picker="basic" class="az_input_group dib poa" style=" right:119px; top: 11px;">
						<input id="datStD" name="datStD" type="text"
							placeholder="yyyy/mm/dd" style="width: 100px;"> <button
							id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button> <span
							class="sim">∼</span> <input id="datEdD" name="datEdD" type="text"
							placeholder="yyyy/mm/dd" style="width: 100px;"> <button
							id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
					<!-- frmPrjList -->
	    			<iframe id="frmPrjList" name="frmPrjList" src='/webPage/tab/sr/PrjListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
    		</div>
	    </div>
	    
		<!-- 하단 S-->
		<div class="half_wrap margin-10-top" style="height: 68%">
			<!-- tab S-->
			<div class="tab_wrap">
				<ul class="tabs">
					<li rel="tabSRRegister" id="tab1">SR등록/접수</li><li rel="tabDevPlan" id="tab2" class="on">개발계획/실적등록</li>
				</ul>
			</div>
			<!-- tab E-->
			
			<div class="half_wrap margin-10-top" style="height:90%"> <!--  tab_container -->
		       	<!-- SR등록/접수 START -->
		       	<div id="tabSRRegister" class="tab_content" style="width:100%">
		       		<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<!-- SR등록/접수  END -->
		       	
		       	<!-- 개발계획/실적등록 START -->
		       	<div id="tabDevPlan" class="tab_content" style="width:100%">
		       		<iframe id="frmDevPlan" name="frmDevPlan" src='/webPage/tab/sr/DevPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<!-- 개발계획/실적등록 END -->
		   	</div>
		   	
		</div>
    </div>
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/DevPlan.js"/>"></script>