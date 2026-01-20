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
			<label>[스크립트정보확인]</label>
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
	            <label id="lbUser" class="title">스크립트실행정보</label>
			</div>
			<div class="row">
				<div class="az_board_basic az_board_basic_in" style="height: 65%">
			    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="tit_80 poa">
				<label id="lblReqGbn">디렉토리</label>
			</div>
			<div class="ml_80">
                 <input id="txtDirPath" class="width-100" type="text" style="align-content:left;" readonly />
			</div>
		</div>
		<div class="row">
			<div class="tit_80 poa">
				<label id="lblReqGbn">프로그램명</label>
			</div>
			<div class="ml_80">
                 <input id="txtRsrcName"" class="width-100" type="text" style="align-content:left;" readonly />
			</div>
		</div>
		<div class="row">
			<div class="tit_80 poa">
				<label id="lblReqGbn">컴파일방법</label>
			</div>
			<div class="ml_80">
                 <input id="txtCompBase" class="width-42" type="text" style="align-content:left;" readonly />
                 <input id="txtCompChg" class="width-30" type="text" style="align-content:left;" readonly />
				<button id="btnReq" class="btn_basic_s" disabled=true >등록</button>
			</div>
		</div>
		<div class="row">
			<div class="tit_80 poa">
				<label id="lblReqGbn">배포방법</label>
			</div>
			<div class="ml_80">
                 <input id="txtDeployBase" class="width-42" type="text" style="align-content:left;" readonly />
                 <input id="txtDeployChg"  class="width-30" type="text" style="align-content:left;" readonly />
	   				<input type="checkbox" class="checkbox-pie" id="chkExec" data-label="배포실행"></input>
				<button id="btnCncl" class="btn_basic_s margin-5-left" >배포취소</button>
				<button id="btnExit" class="btn_basic_s margin-5-left" >체크인</button>
			</div>
		</div>
		
		<div class="row">
			<div class="tal">
				<p class="txt_r" style="white-space: nowrap;">[등록방법 : 1)수정 할 대상을 목록에서 선택 2)하단의 실행방법 우측에 파라메터 입력(좌측에서 ?#PARM#으로 된 부분에 채울 내용) 3)등록버튼 클릭 ]</p>
			</div>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ScriptModal.js"/>"></script>