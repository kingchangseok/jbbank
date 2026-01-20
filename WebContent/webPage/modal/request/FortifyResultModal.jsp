<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>세부취약점 상세확인</label>
		</div>
	</div>
	<div class="container-fluid pop_wrap">
		<div class="row az_board_basic" style="height: 87%">
			<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<div class="float-right dib margin-10-top">
			<button id="btnClose" class="btn_basic_s">닫기</button>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/FortifyResultModal.js"/>"></script>