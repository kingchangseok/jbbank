<!--  
	* 화면명: 개인정보처리 유의사항 팝업
	* 화면호출: 신청상세 > 테스트결과서첨부/확인 에서 [개인정보처리 유의사항]버튼 클릭 시 팝업
-->
<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub" class="margin-10-left">개인정보처리 유의사항</label>
		</div>
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<!--CM_TITLE -->
			<div class="row">
				<div class="write_wrap">
					<textarea id="textareaContents" name="textareaContents" class=" width-100 margin-15-top" style="height: 255px;" readonly></textarea>
				</div>
			</div>
		</div>
	</div>
	<div class="row notice-footer" style="bottom: 0;">
		<button id="btnClose" name="btnClose" class="btn_basic_s dib float-right margin-3-top margin-5-right">닫기</button>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<%-- <script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/FileUpModal.js"/>"></script> --%>
<script type="text/javascript" src="<c:url value="/js/ecams/modal/requestDetail/NotiMsgModal.js"/>"></script>