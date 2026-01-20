<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<body style="min-width: auto;">
	<div style="overflow-y: hidden;">
	<div class="az_search_wrap" style="padding: 10px;">
		<div style="height: 5%;">
	        <label id="lbl2" class="title">성과 및 종합판정</label>
		</div>		
	    <div class="row az_board_basic" style="width: 100%; height: 80px;">
		    <div data-ax5grid="firstGrid" style="height: 100%;"></div>
		</div>
		<div class="margin-10-top" style="height: 5%;">
	        <label id="lbl1" class="title">책임자 확인</label>
		</div>		
	    <div class="row az_board_basic" style="width: 100%; height: 80px;">
		    <div data-ax5grid="secondGrid" style="height: 100%;"></div>
		</div>
		<div class="row">
			<div class="dib vat">
				<button id="btnRewrite" class="btn_basic_s" style="top: 0;">개발기간수정</button>
			 	<button id="btnRewriteQA" class="btn_basic_s" style="top: 0;">성과/책임자수정</button> 
			</div>
		</div>
		<div class="margin-10-top" style="height: 5%;">
	        <label id="lbl1" class="title">Q.A 확인</label>
		</div>		
		<div class="row">			
			<div class="dib vat width-25">
				<label for="txtTestReqYn">테스트전담반 요청여부</label>
				<input id="txtTestReqYn" style="width:50px; text-align:center; font-weight:bold;" type="text" readonly>
			</div>		
			<div class="dib vat width-25">
				<label for="txtTestEndYn">테스트전담반 완료여부</label>
				<input id="txtTestEndYn" style="width:150px; text-align:center; font-weight:bold;" type="text" readonly>
			</div>
			<div class="dib vat width-25">
				<input type="checkbox" id="chkTestResultPass"
					class="checkbox-pie" data-label="테스트전담반결과PASS" disabled>
			</div>
		</div>
	    <div class="row az_board_basic" style="width: 100%; height: 150px;">
		    <div data-ax5grid="thirdGrid" style="height: 100%;"></div>
		</div>
	</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/request/QATab.js"/>"></script>
