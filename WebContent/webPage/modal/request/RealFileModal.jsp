<!--  
	* 화면명: 스크립트정보확인
	* 화면호출: 신청화면 -> 스크립드 배포 프로그램 신청
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[동일한 바이너리 사용 개발목록]</label>
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
			<div class="width-100 dib vat">
				<div class="tal">
					<p class="txt_r" id="txt_tit" style="white-space: nowrap;">현재 운영에 하려는 프로그램과 동일한 실행모듈을 생성하는 프로그램이 개발 중 인 목록입니다.</p>
				</div>
			</div>
			<div class="row">
				<div class="az_board_basic az_board_basic_in" style="height: 80%">
			    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
			<div class="row">
				<div style="text-align:center;">
					<button id="btnReg" class="btn_basic_s margin-5-left" >확인</button>
					<button id="btnCnl" class="btn_basic_s margin-5-left" >취소</button>
				</div>
			</div>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/RealFileModal.js"/>"></script>