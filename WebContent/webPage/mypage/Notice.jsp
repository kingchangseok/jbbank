<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
    <!-- history S-->
	<div id="history_wrap">
		기본관리 <strong>&gt; 공지사항</strong>
	</div>
	
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="dib ">
				<div class="dib vat" id="cboFind" data-ax5select="cboFind" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
				<div id="divPicker" class="az_input_group dib vat" data-ax5picker="basic" style="margin-left: 20px; display:none;">
					<input id="start_date" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
					<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
					<span class="sim">&sim;</span>
					<input id="end_date" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
					<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
				</div>
				<input 	id="txtFind" name="txtFind" type="text" placeholder="제목/내용 입력후 조회" style="width: 250px; margin-left: 20px;"></input>
			</div>
			<div class="r_wrap">
				<div class="vat dib">
					<button id="btnQry" name="btnQry" class="btn_basic_s">조 회</button>
				</div>
				<div class="vat dib margin-5-left">
					<button id="btnReg" name="btnReg" class="btn_basic_s">공지사항등록</button>
				</div>
				<div class="vat dib margin-5-left">
					<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel">엑셀저장</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic" style="height: 90%">
		<div data-ax5grid="noticeGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="frameResize"></div>
	</div>
	
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/Notice.js"/>"></script>