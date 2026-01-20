<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>프로그램 상세</title>
<c:import url="/webPage/common/common.jsp"/>

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
%>

<body id="reqBody" style="width: 100% !important; padding: 10px;">
	<div class="content">
		<div id="history_wrap">프로그램 적용 상세<strong>&gt; 프로그램 상세 조회</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<!--좌측-->
				<div class="l_wrap width-60 vat write_wrap">
					<div class="row">
						<dl>
							<dt><label>시스템</label></dt>
							<dd><div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 45%;"></div></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>프로그램</label></dt>
							<dd><input id="txtProg" type="text" autocomplete="off" readonly="readonly" style="width: 98%;"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>반환예정일</label></dt>
							<dd>
								<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
									<input id="datReturn" name="datReturn" type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
									<button id="btnReturn" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
								</div>
							</dd>
						</dl>
					</div>
				</div>
				<!--우측-->
				<div class="r_wrap width-40 vat write_wrap">
					<div class="row">
						<dl>
							<dt><label>변경사유</label></dt>
							<dd>
								<form>
									<textarea id="txtSayu" style="align-content:flex-start;width:100%;height:180px;resize: none; overflow-y:auto; padding:5px;" readonly="readonly"></textarea>
								</form>
							</dd>
						</dl>
					</div>
				</div>
		</div>
	</div>
	<div class="l_wrap width-49">
		<div class="row">
			<div class="float-right">
				<button id="btnOrderAdd" name="btnOrderAdd" class="btn_basic_s" disabled="disabled">업무지시서 추가/제거</button>
				<button id="btnView1" name="btnView1" class="btn_basic_s margin-5-left" disabled="disabled">업무지시서보기</button>
			</div>
		</div>
		<div class="row">
			<div class="az_board_basic scroll_h az_board_basic_in" style="height: 50%; margin-top: 10px;">
			 	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="r_wrap width-49">
		<div class="row">
			<div class="float-right">
				<button id="btnView2" name="btnView2" class="btn_basic_s" disabled="disabled">개발요청서보기</button>
			</div>
		</div>
		<div class="row">
			<div class="az_board_basic scroll_h az_board_basic_in" style="height: 50%; margin-top: 10px;">
			 	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="float-right margin-10-top">
		<button id="btnReWrite" name="btnReWrite" class="btn_basic_s">수정</button>
		<button id="btnClose" name="btnClose" class="btn_basic_s margin-5-left">닫기</button>
	</div>
</div>
</body>

<form name="getReqData">
	<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="acptno" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" id="itemid" name="itemid" value="<%=itemId%>"/>
</form>

<form name="setReqData" accept-charset="utf-8">
	<input type="hidden" name="user"/>
	<input type="hidden" name="orderId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopProgDetailCkOut.js"/>"></script>