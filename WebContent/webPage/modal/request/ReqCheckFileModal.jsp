<!-- 
	화면 명: 파일재첨부 (AttFilesPop.mxml)
	화면호출:
			1) 적용요청등록 > 적용요청 > 검증사항 > 파일재첨부
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; height: 100% !important;">
<div class="pop-header">
	<div>
		<label>[파일 재첨부]</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose(true)">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="vat az_search_wrap">
		<label class="ml_10">조회일자</label>
		<div class="dib vat">
			<div id="divPicker" data-ax5picker="basic" class="az_input_group dib vat">
				<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
				<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
				<span class="sim">∼</span>
				<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
				<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
			</div>
		</div>
		<button class="btn_basic_s margin-5-left poa" id="btnQry">조회</button>
	</div>
	
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:35%">
		    	<div data-ax5grid="firstGrid" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<label class="poa title">첨부파일상세</label>
	</div>
	
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:35%">
		    	<div data-ax5grid="secondGrid" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	
	<div class="row tar">
		<button class="btn_basic_s" id="btnReg" disabled>등록</button>
		<button class="btn_basic_s" id="btnCncl">취소</button>
	</div>
</div>

</body>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ReqCheckFileModal.js"/>"></script>