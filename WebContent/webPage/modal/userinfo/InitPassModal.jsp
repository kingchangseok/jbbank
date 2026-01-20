<!--  
	* 화면명: 비밀번호 초기화
	* 화면호출: 사용자정보 -> 비밀번호 초기화 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[비밀번호 초기화]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1-->					
		<div class="row">
			<label>사용자ID</label>
	        <div>
	        	<input id="txtUserId" type="text" class="width-100" />
			</div>
		</div>
		<!--line2-->					
		<div class="row">
			<label>비밀번호 4자리</label>
	        <div>
	        	<input id="txtPasswd" type="text" class="width-100" style="text-security: disc; -webkit-text-security: disc;" value="1234" readonly/>
			</div>
		</div>
		<!--line3-->					
		<div class="row txt_r">초기화시 비밀번호 1234로 세팅됩니다.</div>
		<!--button-->
		<div class="row tac float-right">
			<button id="btnReq" class="btn_basic_s">등록</button>
			<button id="btnExit" class="btn_basic_s">닫기</button>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/InitPassModal.js"/>"></script>