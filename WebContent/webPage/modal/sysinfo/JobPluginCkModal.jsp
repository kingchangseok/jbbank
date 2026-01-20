<!--  
	* 화면명: 예외업무등록
	* 화면호출: 시스템정보 -> 예외업무 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<div class="pop-header">
	<div>
		<label>예외업무</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<div class="sm-row az_board_basic" style="height: 85%">
		<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
	<div class="sm-row cb" style="font-size: 0px">
		<div class="tac float-right">
			<button id="btnSearch" class="btn_basic_s margin-5-right">조회</button>
			<button id="btnReq" class="btn_basic_s">등록</button>
			<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
		</div>
	</div>
	<!--button-->
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/JobPluginCkModal.js"/>"></script>