<!-- 
	화면 명: 형상관리최종소스와 불일치목록 (progFileUpDown_Agent.mxml, eCmr0100_relat.mxml)
	화면호출: 체크아웃 > 파일비교 (단, 로컬사용 프로그램만)
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label>형상관리최종소스와 불일치목록</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" id="btnExit">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="row" id="divTitle" style="display: none;">
		<p class="txt_r font_12">현재 체크아웃하려는 프로그램 중 형상관리서버와 불일치하는 건이 존재합니다.</p>
		<p class="txt_r font_12">목록을 확인 후 진행구분을 선택하여 주시기 바랍니다.</p>
		<p class="txt_r font_12">[1.목록에서 대상선택-->2.진행구분선택-->3.적용버튼클릭]</p>
	</div>
	<div class="row">
		<div class="az_board_basic" style="height: calc(100% - 135px);">
	    	<div data-ax5grid="firstGrid" id="firstGrid" style="height: 100%;"></div>
		</div>
	</div>
	<div class="row tar por">
		<div class="l_wrap dib tal" style="width: calc(100% - 250px); display: none;" id="divRadio">
			<div class="dib" style="border: 1px dotted gray; width: 280px">
				<input id="optBase1"  type="radio" name="optradio" value="M"/>
				<label for="optBase1">Merge</label>
				<input id="optBase2" type="radio"  name="optradio" value="C"/>
				<label for="optBase2">Local Ignore</label>
				<input id="optBase3" type="radio"  name="optradio" value="L"/>
				<label for="optBase3">eCAMS Ignore</label>
			</div>
			<button class="dib btn_basic_s" id="btnAdd">적용</button>
		</div>
		<div class="dib vat tar" style="display: none;" id="divDiffBtn">
			<button class="btn_basic_s" id="btnReplay">재처리</button>
			<button class="btn_basic_s" id="btnReq">확인</button>
			<button class="btn_basic_s" id="btnCancel">취소</button>
		</div>
		<button class="btn_basic_s" id="btnClose">닫기</button>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/FileDiffModal.js"/>"></script>