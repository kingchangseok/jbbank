<!-- 
화면 명: 이전버전선택
화면호출:
1) 개발 -> 이전버전체크아웃 -> 버전선택 모달 팝업
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label>버전선택</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" id="btnExit">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="row" >
		<div>
			<div class="az_board_basic" style="height: calc(100% - 80px);">
		    	<div data-ax5grid="grdReqVersion" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="row tar por">
		<button class="btn_basic_s" id="btnReset">선택버전초기화</button>
		<button class="btn_basic_s" id="btnSel">선택</button>
		<button class="btn_basic_s" id="btnClose">닫기</button>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ChkOutVerSelModal.js"/>"></script>