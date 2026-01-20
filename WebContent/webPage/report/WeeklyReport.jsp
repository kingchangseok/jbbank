<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.btn_calendar{
	padding-left: 9px !important;
    padding-right: 9px !important;
}
</style>

<div id="wrapper">
    <div class="contentFrame">
        <div id="history_wrap">보고서 <strong>&gt; 주간보고자료</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
			  <div class="l_wrap">
				<div class="row">
					<div class="dib vat margin-10-left">
						<div class="dib vat">
					    <label>조회기간 &nbsp;&nbsp;&nbsp;</label>
							<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
								<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
								<span class="sim">&sim;</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
								<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
				</div>
			  </div>
			</div>
			<div class="r_wrap">
				<div class="vat dib">
					<div class="vat dib margin-5-left">
						<button id="btnQry" name="btnQry" class="btn_basic_s">조회</button>
					</div>
					<div class="vat dib margin-5-left">
						<button id="btnExcel" name="btnExcel" class="btn_basic_s">엑셀저장</button>
					</div>
				</div>
			</div>
		</div>

	    <div class="az_board_basic">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/WeeklyReport.js"/>"></script>