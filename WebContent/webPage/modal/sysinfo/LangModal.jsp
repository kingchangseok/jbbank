<!--  
	* 화면명: 언어등록
	* 화면호출: 시스템정보 -> 언어등록 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[언어등록]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row vat cb">
					<label class="tit_60 poa">언어코드</label>
					<div class="ml_60">
						<input id="txtLangcd" type="text" class="dib" style="width: calc(30% - 5px); margin-right: 5px"/>
						<div id="cboLang" data-ax5select="cboLang" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-70 dib"></div>
					</div>
				</div>
				<div class="row vat cb">
					<label class="tit_60 poa">언어명</label>
					<div class="ml_60">
						<input id="txtLangname" type="text" class="dib" style="width: calc(100% - 141px);margin-right:5px;"/>
						<input type="checkbox" class='checkbox-pie' id='chkExename' data-label="확장자없는파일포함"></input>
					</div>
				</div>
				<div class="row vat cb">
					<label class="tit_60 poa">확장자</label>
					<div class="ml_60">
						<input id="txtExename" type="text" class="dib" style="width: calc(100% - 144px);"/>
						<button id="btnRegist" class="btn_basic_s margin-3-left" style="width: 45px;">등록</button>
						<button id="btnDelete" class="btn_basic_s margin-3-left" style="width: 45px;">폐기</button>
						<button id="btnSearch" class="btn_basic_s margin-3-left" style="width: 45px;">조회</button>
					</div>
				</div>
			</div>
		</div>
		<div class="az_board_basic az_board_basic_in" style="height: 75%">
	    	<div data-ax5grid="langGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/LangModal.js"/>"></script>