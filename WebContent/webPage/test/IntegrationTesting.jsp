<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />

<div id="history_wrap">테스트관리 <strong>&gt; 통합테스트</strong></div>
<div class="az_search_wrap" style="height:30%">
	<div class="az_in_wrap sr_status" style="height:100%">
		<div class="row por" style="height:16%">
			<div class="l_wrap dib vat" style="width:calc(55% + 10px);">
				<div class="dib vat" style="width:26%;  margin-top: 10px;" id="sys">
					<label id="lblTit">테스트대상목록</label>
				</div>
				<div class="dib" style="width:37%; " id="status">
					<label id="lblPart" style="margin-left: 16px;">변경파트</label>
					<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm', theme:'primary'}" class="width-15 dib" style="width: calc(100% - 99px); margin-left: 10px;"></div>
				</div>								
				<div class="dib" style="width:37%;" id="status">
					<label id="lblQryGbn" style="margin-left: 15px;">대상구분</label>
					<div id="cboQryGbn" data-ax5select="cboQryGbn" data-ax5select-config="{size:'sm', theme:'primary'}" class="width-15 dib" style="width: calc(100% - 99px); margin-left: 10px;"></div>
				</div>
			</div>
			<div class="dib r_wrap">		
				<div class="dib vat">
					<label id="lblDay">등록일</label>
				</div>
				<div class="dib">
					<div id="divPicker" class="az_input_group dib" data-ax5picker="topDate">
						<input id="dateSt" name="datSt" type="text" placeholder="yyyy/mm/dd" style="width:80px;">
						<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						<label id="lblFrom" style="margin: 0 7px">부터</label>
						<input id="dateEd" name="datEd" type="text" placeholder="yyyy/mm/dd" style="width:80px;">
						<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						<label id="lblTo" style="margin: 0 7px">까지</label>
					</div>
				</div>
				<div class="dib vat">
					<div class="vat dib margin-5-left">c
						<button class="btn_basic_s" id="btnQry" style="margin-left: 0px; margin-right: 0px;">조회</button>
					</div>
				</div>
			</div>		
		</div>
		<div class="row por" style="height: 80%;">
			<div class="az_board_basic" style="height: 100%;">
			<div data-ax5grid="testGrid"data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>	
		</div>
	</div>
</div>

<div class="half_wrap" style="height:16%;">
	<iframe id="frmBaseTestInfo" name="frmBaseTestInfo" src='/webPage/test/baseTestInfo.jsp' width='100%' height='100%' ></iframe>
</div>

<div id="test_tab" class="half_wrap" style="height:50%; margin-top:10px;">
	<div class="tab_wrap">
		<ul class="tabs">
			<li rel="tabTestAccept" id="tab1" >테스트접수</li>
			<li rel="tabIntegrationTesting" id="tab2" class="on">통합테스트</li>
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
	</div>
	
</div>


<form name="popPam1">
	<input type="hidden" id="isrId" name="isrId"/>
	<input type="hidden" id="user" name="user"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/test/IntegrationTesting.js"/>"></script>
