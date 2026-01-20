<!-- 
	화면 명: 업무지시서 미처리건
	화면호출:
		1) 개발요청>업무지시서발행 -> 개발종료
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label>업무지시서 미처리건이 있습니다.</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<label class=" title">미처리건 리스트</label>
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
<script type="text/javascript" src="<c:url value="/js/ecams/modal/dev/DevOrderListModal.js"/>"></script>