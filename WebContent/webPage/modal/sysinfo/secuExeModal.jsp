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
			<label>[취약점대상확장자정보]</label>
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
	            <label id="lbUser" class="title">취약점대상확장자리스트</label>
			</div>
			<div class="row">
				<div class="az_board_basic az_board_basic_in" style="height: 85%">
			    	<div data-ax5grid="jobGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
		</div>
		<div class="row tar">
			<button id="btnIn" class="btn_basic_s">새로만들기</button>
			<button id="btnUp" class="btn_basic_s margin-5-left">편집</button>
			<button id="btnDel" class="btn_basic_s margin-5-left">삭제</button>
			<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/secuExeModal.js"/>"></script>