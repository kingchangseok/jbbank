<!-- 
	화면 명: 검증사항 (eCmr0202_tab.mxml)
	화면호출:
			1) 적용요청등록 > 적용요청
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; height: 100% !important;">
<div class="pop-header">
	<div>
		<label>[검증사항]</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose('2')">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="l_wrap width-70">
		<div class="margin-5-right">
			<div class="half_wrap">
				<div class="width-100 dib vat">
					<label class="title">프로그램공통</label>
				</div>
				<div class="sm-row">
					<div class="az_board_basic az_board_basic_in" style="height: 40%">
						<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>
				</div>
			</div>
			<div class="half_wrap row">
				<div class="width-100 dib vat">
					<label class="title">프로그램개별</label>
				</div>
				<div class="sm-row">
					<div class="az_board_basic az_board_basic_in" style="height: 43.5%">
						<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="r_wrap width-30 half_wrap">
		<div class="width-100 dib vat">
			<label class="title">프로그램개별항목추가</label>
		</div>
		<div class="row">
			<input type="checkbox" class='checkbox-pie' id="chk1" style="width:60px;" data-label="대상여부"></input>
			<input type="checkbox" class='checkbox-pie' id="chk2" style="width:60px;" data-label="테스트여부" disabled></input>
			<input type="checkbox" class='checkbox-pie' id="chk3" style="width:60px;" data-label="적정여부" disabled></input>
		</div>
		<div class="row">
			<label class="tit_80 poa">검증대상항목</label>
            <div class="ml_80 vat">
				<input id="txtSubject" name="txtSubject" type="text" class="dib width-100" maxlength="20"></input>
			</div>
		</div>
		<div class="row">
			<label class="tit_80 poa">비고</label>
            <div class="ml_80 vat">
				<input id="txtBigo" name="txtBigo" type="text" class="dib width-100" maxlength="10"></input>
			</div>
		</div>
		<div class="row dib vat width-100">
 			<div class="vat float-right">
				<button id="btnDel" class="btn_basic_s tit_80">삭제</button>
				<button id="btnAdd" class="btn_basic_s tit_80">추가</button>
			</div>
		</div>
		
		<div class="dib vat width-100" style="margin-top: 220px">
			<div class="dib">
				<div style="display:none;" id="fileSave"></div>
				<button id="btnFile" class="btn_basic_s">파일첨부</button>
			</div>
			<div class="float-right">
				<button id="btnReFile" class="btn_basic_s">파일재첨부</button>
			</div>
		</div>
		
		<div class="row">
			<div class="az_board_basic az_board_basic_in" style="height: 40%">
				<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
		
		<div class="row">
			<div class="vat float-right">
				<button id="btnExit" class="btn_basic_s tit_80">취소</button>
				<button id="btnOk" class="btn_basic_s tit_80">완료</button>
			</div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ReqCheckModal.js"/>"></script>