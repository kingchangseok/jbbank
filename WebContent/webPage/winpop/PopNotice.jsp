<!--  
	* 화면명: 공지사항 새창
	* 화면호출: 메인화면 접속시 자동으로 뜸
-->
<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<%
	request.setCharacterEncoding("UTF-8");
	String cm_acptno = StringHelper.evl(request.getParameter("cm_acptno"),"");
%>
<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub">공지사항</label>
		</div>
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<div class="row">
				<div>
					<label id="lbSub" class="tit_40 poa">제목</label>
					<div class="ml_40">
						<input id="txtTitle" name="txtTitle" type="text" class="width-100" readonly></input>
					</div>
				</div>
			</div>
			<!--CM_TITLE -->
			<div class="row">
				<div class="write_wrap">
					<textarea id="textareaContents" name="textareaContents" class=" width-100" style="height: 265px;" readonly></textarea>
				</div>
			</div>
			
			<div class="row">
				<button id="btnFile" name="btnFile" class="btn_basic_s">첨부파일</button>
			</div>
		</div>
	</div>
	<div class="row notice-footer" style="bottom: 0;">
		<div class="dib">
			<input type="checkbox" class="checkbox-pop" id="chkPopYn" data-label="오늘 하루 이창을 열지 않음 "/>
		</div>
		<button id="btnClose" name="btnClose" class="btn_basic_s dib float-right margin-3-top margin-5-right">닫기</button>
	</div>
</body>

<input type="hidden" id="cm_acptno" name="cm_acptno" 	value="<%=cm_acptno%>"/>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<%-- <script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/FileUpModal.js"/>"></script> --%>
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopNotice.js"/>"></script>