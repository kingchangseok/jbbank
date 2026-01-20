<!-- 
	화면 명: 미완료 부서
	화면호출:
		1) 개발요청>개발요청등록 -> 개발종료
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label>관련된 업무지시서가 모두 종료되지 않았습니다.</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<label class=" title">미완료 부서</label>
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:80%">
		    	<div data-ax5grid="firstGrid" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="row tar">
		<button class="btn_basic_s" id="btnClose">닫기</button>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/dev/DevReqListModal.js"/>"></script>