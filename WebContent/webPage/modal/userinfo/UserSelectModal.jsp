<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>사용자 검색</label>
		</div>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose(false)">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
	<div class="container-fluid pop_wrap">
		<div class="vat">
			<input id="txtSearch" style="width:calc(100% - 50px);" type="text" placeholder="사용자명을 입력하여 조회하세요."/>
			<button class="btn_basic_s margin-5-left poa" id="btnQry">검색</button>
		</div>
		
		<div class="row az_board_basic" style="height: 65%">
			<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<div class="float-right dib" style="margin-top: 8px;">
			<button id="btnConfirm" class="btn_basic_s">등록</button>
			<button id="btnCncl" class="btn_basic_s">취소</button>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/UserSelectModal.js"/>"></script>