<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

	 <div class="contentFrame">
	 	<div id="history_wrap">결재확인 <strong>&gt; 종합모니터링</strong></div>
			<div class="az_in_wrap">
				<div class="float-right margin-10-bottom">
                	<div class="dib">
					<div id="divPicker" data-ax5picker="basic"class="az_input_group dib">
						<label class="tit-80 dib poa">변경시작일</label>
						<div class="ml_70 dib margin-20-right">
							<input id="dateSt" name="dateSt" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
							<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<label class="tit-80 dib poa">변경종료일</label>
						<div class="ml_70 dib margin-10-right">
							<input id="dateEd" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
							<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
					</div>
					</div>
					<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
				</div>
			</div>
			
			<div class="width-100 dib por">
				<div class="az_search_wrap l_wrap" style="width: 49.5%; padding: 3px 0;">
					<div class="float-left margin-10-left">
						<!-- <img src="/img/simbol_tit.gif" style="position: absolute; top: -0.3%; left: 2.7%;"> -->
						<label class="tit-80 dib poa">미결재현황</label>
					</div>
					<div class="float-right margin-10-right">
						<button class="btn_basic_s" id="btnExcel1" style="width: 70px;">엑셀저장</button>
					</div>
				</div>
				<div class="az_search_wrap r_wrap" style="width: 49.5%; padding: 3px 0;">
					<div class="float-left margin-10-left">
						<label class="tit-80 dib poa">오류현황</label>
					</div>
					<div class="float-right margin-10-right">
						<button class="btn_basic_s" id="btnExcel2" style="width: 70px;">엑셀저장</button>
					</div>
				</div>
			</div>
		
		<div class="az_board_basic por" style="height: calc(100% - 120px)">
			<div class="l_wrap" style="width: 49.5%">
				<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
			</div>
			<div class="r_wrap" style="width: 49.5%">
				<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
			</div>
		</div>
	 </div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/approval/TotalMonitoring.js"/>"></script>
