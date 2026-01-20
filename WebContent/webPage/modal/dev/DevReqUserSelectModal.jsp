<!-- 
	화면 명: 사용자 검색
	화면호출:
		1) 개발요청>개발요청등록 -> 주관부서-담당자 추가
		2) 개발요청>외주개발요청등록 -> 주관부서-담당자 추가
		3) 개발요청>업무지시서발행 -> 주관부서-담당자 추가
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">

<div class="pop-header">
	<div>
		<label>사용자 검색</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose(false)">
			<span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="vat">
		<input id="txtUserName" style="width:calc(100% - 50px);" type="text" placeholder="사용자명을 입력하여 조회하세요."/>
		<button class="btn_basic_s margin-5-left poa" id="btnQry">검색</button>
	</div>
	
	<div class="row">
		<label class=" title">검색결과</label>
		<div class="float-right"><label class="txt_r">[추가/제거시 더블클릭]</label></div>
	</div>
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:35%">
		    	<div data-ax5grid="firstGrid" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="row">
		<label class="poa title">추가대상</label>
	</div>
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:35%">
		    	<div data-ax5grid="secondGrid" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="row tar">
		<button class="btn_basic_s" id="btnReg">등록</button>
		<button class="btn_basic_s" id="btnCncl">취소</button>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/dev/DevReqUserSelectModal.js"/>"></script>