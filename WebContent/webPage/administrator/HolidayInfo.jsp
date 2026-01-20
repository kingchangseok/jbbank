<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">	
    <div id="history_wrap">관리자 <strong>&gt; 휴일정보</strong></div>
	<div class="padding-40-top">
		<div id="divContent">
			<div class="row-fluid">
				<div class="row">
					<label class="tit_40 dib poa">년도</label>
					<div class="ml_40">
						<div id="divPicker" data-picker-date="year" class="dib width-100 por">
							<input id="txtYear" type="text" class="f-cal" autocomplete="off"  placeholder="yyyy">
				            <span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
			            </div>
					</div>
				</div>
				<div class="row">
					<div class="az_board_basic" style="height: 55%;">
						<div data-ax5grid="holidayGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%; padding-top: 20px;"></div>
					</div>
				</div>
				
				<div class="row">
					<label class="tit_60 dib poa">휴일</label>
					<div class="ml_60">
				    	<div id="divPicker" data-ax5picker="basic2" class="dib width-100 por">
							<input id="txtHoliDate" type="text" class="f-cal" autocomplete="off">
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						</div>
					</div>
				</div>
				
				<div class="row">
					<label class="tit_60 dib poa">휴일종류</label>
					<div class="ml_60">
						<div id="cboHoli" data-ax5select="cboHoli" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width: 100%"></div>
					</div>
				</div>
				
				<div class="row">
					<label class="tit_60 dib poa">휴일구분</label>
					<div class="ml_60">
						<div id="cboHoliDiv" data-ax5select="cboHoliDiv" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%"></div>
					</div>
				</div>
				
				<div class="row" style="overflow:auto;">
					<div class="tac float-right">
						<button class="btn_basic_s" id="btnReg">등록</button>
    					<button class="btn_basic_s" id="btnDel">삭제</button>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</div>



<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/HolidayInfo.js"/>"></script>