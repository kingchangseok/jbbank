<!--  
	* 화면명: [영향분석대상확장자정보]
	* 화면호출: 환경설정 -> 영향분석대상확장자정보 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[모니터링 체크리스트]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1 S-->
		<div>					
			<div class="row">
				<div class="scrollBind" style="margin-top:3px;height: calc(100% - 80px);">
					<ul id="chkList" class="ztree"></ul>
				</div>
			</div>
		</div>
		<div class="row tar">
			<span class="fa_wrap" id="btnPlus"><i class="fas fa-plus"></i></span> 
			<span class="fa_wrap margin-5-left" id="btnMinus"><i class="fas fa-minus"></i></span>
			<button id="btnSearch" class="btn_basic_s margin-5-left">조회</button>
			<button id="btnSave" class="btn_basic_s margin-5-left">저장</button>
			<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
		</div>
	</div>
	
	<div id="inputDiv" class="poa" style="display: none; width: 360px; height: 200px; top: calc(50% - 100px); left: calc(50% - 180px); border: 1px solid black; padding: 10px; border-radius: 5px; z-index: 1001; background: #fff;">
		<div class="row">
			<label>항목명입력</label>
		</div>
		<div class="row">
			<textarea id="txtInp" style="align-content:flex-start; border: 1px solid #ddd; width:100%; height:95px; resize: none; overflow-y:auto; padding: 5px; " readonly></textarea>
		</div>
		<div class="row">
			<input type="checkbox" class='checkbox-pie' id ='import' data-label="필수여부" checked>
			<div class="dib float-right">
				<label>(<span id="inputCount">0</span>/200)</label>
			</div>
		</div>
		<div>
			<div class="dib" style="margin-left: calc(50% - 45px);">
				<button id="btnSaveInp" class="btn_basic_s">확인</button>
				<button id="btnCloseInp" class="btn_basic_s">취소</button>
			</div>
		</div>
	</div>
	
</body>

<div id="rMenu"> 
 	<ul> 
 		<li id="contextmenu1" onclick="contextmenu_click('1');">필수여부수정</li> 
 	</ul> 
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/MonitoringCkListModal.js"/>"></script>