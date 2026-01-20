<!--  
	* 화면명: 비밀번호 변경
	* 화면호출: 기본관리 > 기본정보 > 비밀번호 변경
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>비밀번호 변경</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<!--
    <iframe id="ifmPwdChange" src='/webPage/mypage/PwdChange.jsp' width='100%' height='80%' frameborder="0" scrolling="no"></iframe>
    -->
    <div style="width:80%;margin:auto;">
		<div class="cb">
			<div class="float-left width-100 margin-25-top">
				<p class="txt_r" id="lb2">비밀번호는 숫자/영문/특수기호를 포함하는 8-12자리 이어야 합니다.<br>(분기별 패스워드 변경)</p>
				
				<div class="sm-row">
			    	<input id="txtPw" type="text" placeholder="현재 비밀번호" style="width:100%;height:35px;font-size:15;margin-top:10px;text-security: disc; -webkit-text-security: disc;" autocomplete="off">
			    </div>
			    <div class="sm-row">
			    	<input id="txtUpdatePw1" type="text" placeholder="새 비밀번호" style="width:100%;height:35px;font-size:15;margin-top:10px;text-security: disc; -webkit-text-security: disc;" autocomplete="off">
			   	</div>
			    <div class="sm-row">
			    	<input id="txtUpdatePw2" type="text" placeholder="새 비밀번호 확인" style="width:100%;height:35px;font-size:15;text-security: disc; -webkit-text-security: disc;" autocomplete="off">
			   	</div>
			   	
				<div class="sm-row" style="text-align:right;margin-top:15px;">
					<button id="btnPw" name="btnPw" class="btn_basic_s" >변경</button>
					<button id="btnCncl" name="btnCncl" class="btn_basic_s">취소</button>
				</div>
				<div class="sm-row" style="text-align:right;margin-top:5px;">
				</div>
			</div>
		</div>
   	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/ChangePassWdModal.js"/>"></script>