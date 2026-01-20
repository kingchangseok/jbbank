<!-- 
화면 명: 적용대상서버
화면호출:
1) 체크인 > 적용대상서버 선택
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label>대상서버 선택</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="width-100 dib vat">
		<label>시스템</label>
		<div class="dib vat" style="width: calc(100% - 45px)">
	          <input id="txtSysMsg" name="txtSysMsg" type="text" style="width:100%;" readonly>
		</div>
	</div>
	<div class="row">
		<div class="az_board_basic" style="height:80%">
	    	<div data-ax5grid="firstGrid" style="height: 100%;"></div>
		</div>
	</div>
	<div class="row tar">
		<button class="btn_basic_s" id="btnInit">초기화</button>
		<button class="btn_basic_s margin-5-left" id="btnReg">등록</button>
		<button class="btn_basic_s margin-5-left" id="btnClose">닫기</button>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/selSvrModal.js"/>"></script>