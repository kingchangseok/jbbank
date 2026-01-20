<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<div class="contentFrame">  
	<div id="history_wrap">보고서<strong>&gt; 요청부서별 완료SR 등급별조회</strong></div>
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap">
				<div class="vat dib">
					<input type="checkbox" class="checkbox-file" id="chkDetail" data-label="상세보기" style="margin-top: 5px;"/> 
				</div>
				<div class="dib vat">
					<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
						<input id="dateStD" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width: 85px;">
						<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
				</div>
			</div>
			<div class="r_wrap">
				<div class="vat dib margin-5-right">
					<button id="btnQry" name="btnQry" class="btn_basic_s float-right" style="cursor: pointer;">조회</button>
				</div>
				<div class="vat dib">
					<button id="btnExcel" name="btnExlTmp" class="btn_basic_s float-right" style="cursor: pointer;">엑셀저장</button>
				</div>
			</div>
		</div>
	</div>
	<div class="az_board_basic" style="height: 85%;">
			<div data-ax5grid="mainGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/CompleteSrReport.js"/>"></script>