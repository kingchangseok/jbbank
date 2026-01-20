<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[업무지시서 추가/제거]</label>
		</div>
	</div>
	<div class="row">
		<div class="dib vat margin-10-left" style="width: 91.5%;">
			<input id="txtOrderName" name="txtOrderName" type="text" class="width-100" placeholder="업무지시제목을 입력하세요.">
		</div>
		<div class="float-right">
			<button id="btnSearch" class="btn_basic_s margin-10-right">검색</button>
		</div>
	</div>
	<div class="container-fluid pop_wrap">
		<div class="row az_board_basic" style="height: 37%">
			<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<div class="float-right">
			<label class="fontStyle-cncl">[추가/제거시 더블클릭]</label>
		</div>
	</div>
	<div class="container-fluid pop_wrap">
		<label class="title">추가대상</label>
		<div class="row az_board_basic" style="height: 37%">
			<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<div class="float-right dib margin-5-top">
			<button id="btnConfirm" class="btn_basic_s">등록</button>
			<button id="btnCncl" class="btn_basic_s">취소</button>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/AddOrderModal.js"/>"></script>