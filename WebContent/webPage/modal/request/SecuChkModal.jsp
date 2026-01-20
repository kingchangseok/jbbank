<!--  
	* 화면명: 이행점검체크리스트
	* 화면호출: 적용신청 > 적용요청 > 이행점검체크리스트
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
[data-ax5grid-cellholder="multiLine"] {
	height: 100% !important;
	white-space: pre-wrap !important;
}

[data-ax5grid-column-colindex="4"] > [data-ax5grid-cellholder="multiLine"] {
	padding: 0px !important;
}

[data-ax5grid-panel="body"] {
	overflow: auto !important;
	height: 100%;
}

input[type="radio"] {
	margin: 0 21px;
}

.reasonTxt {
	height: 100%;
	width: 100%;
	border: 0px;
	background: rgba(255, 255, 255, 0);
}
.reasonTxt:hover {
	border: 0px;
}
.reasonTxt:focus {
	border: 0px;
	background: white;
}
.reasonTxt::-ms-clear {
	display: none;
}
[data-ax5grid] [data-ax5grid-container='root'] [data-ax5grid-container='body'] [data-ax5grid-panel] table tr td[data-ax5grid-column-col="4"] [data-ax5grid-cellHolder] [data-ax5grid-editor] {
	height: 100% !important;
	width: 100% !important;
	position: absolute !important;
}
</style>
<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub">&nbsp;이행점검체크리스트</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="az_board_basic" style="height: 90%; padding: 5px;">
   		<div data-ax5grid="firstGrid" style="height: 100%;"></div>
	</div>
	<label id="infoText" class="margin-10-left"></label>
	<div class="row float-right">
		<button type="button" class="btn_basic_s" id="btnQry">등록</button>
		<button type="button" class="btn_basic_s" id="close">닫기</button>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/SecuChkModal.js"/>"></script>