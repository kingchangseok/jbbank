<!-- 
	화면 명: 검증사항 (eCmr0230.mxml)
	화면호출:
			1) 적용요청상세 > 검증항목조회 > 검증항목수정
-->
<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
%>

<body style="width: 100% !important; height: 100% !important; padding: 10px;">

<div class="content">
	<div id="history_wrap">검증 항목 수정<strong>&gt; 검증 항목 추가/삭제</strong></div>
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
					<div class="az_board_basic az_board_basic_in" style="height: 44%">
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
				<button id="btnAdd" class="btn_basic_s tit_80 margin-5-left">추가</button>
			</div>
		</div>
		
		<div style="height: 72%;">
		</div>
		
		<div class="row">
			<div class="vat float-right">
				<button id="btnExit" class="btn_basic_s tit_80">취소</button>
				<button id="btnOk" class="btn_basic_s tit_80 margin-5-left">완료</button>
			</div>
		</div>
	</div>
</div>

<form name="getReqData">
	<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="acptno" name="acptno" value="<%=acptNo%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopReqCheck.js"/>"></script>