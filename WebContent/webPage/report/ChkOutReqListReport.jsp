<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<div class="contentFrame">
	<div id="history_wrap"></div>
      
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="width-30 dib"></div>
			<div class="dib vat">
				<label class="dib margin-5-right">조회시작일</label>
				<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
					<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
					<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					<label class="dib margin-5-right margin-20-left">조회종료일</label>
					<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
					<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
				</div>
			</div>
			<div class="vat dib ml_40">
				<button id="btnSearch" name="btnSearch" class="btn_basic_s" style="cursor: pointer;">조회</button>
				<button id="btnExcel" name="btnExcel" class="btn_basic_s margin-5-left" style="cursor: pointer;">엑셀저장</button>
			</div>
		</div>
	</div>
	<div class="az_board_basic margin-10-top" style="height: 88%;">
		<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/ChkOutReqListReport.js"/>"></script>