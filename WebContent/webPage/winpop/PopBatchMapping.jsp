<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<%
	request.setCharacterEncoding("UTF-8");
	String userId 	= StringHelper.evl(request.getParameter("userId"),"");
	String userName = StringHelper.evl(request.getParameter("userName"),"");
	String adminYN 	= StringHelper.evl(request.getParameter("adminYN"),"");
	String strReqCD = StringHelper.evl(request.getParameter("strReqCD"),"");
%>

<div id="wrapper" style="padding: 10px;">
	<div id="history_wrap">관리자<strong>&gt; 소스모듈맵핑</strong></div>
        
	<div class="half_wrap">
		<div class="l_wrap dib width-70 vat">
			<div class="width-30 dib vat">
				<label class="tit_60">시스템</label>
				<div class="dib vat" style="width: calc(100% - 80px)">
					<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
				</div> 
			</div>
			<div class="width-40 dib vat">
				<input id="optNomal" type="radio" name="radio" value="normal"/>
				<label for="optNomal">정상건</label>
				<input id="optErr" type="radio" name="radio" value="err"/>
				<label for="optErr">오류건</label>
				<input id="optAll" type="radio" name="radio" value="all"/>
				<label for="optAll">전체</label>
				<input type="checkbox" class="checkbox-batch" id="chkOk" data-label="정상건만등록"/>
			</div>
		</div>
			
		<div class="r_wrap dib width-30 vat">
			<div class="vat dib" style="float: right;">
				<button id="btnLoadExl" name="btnLoadExl" class="btn_basic_s" style="cursor: pointer;">엑셀파일</button>
				<button id="btnReq" name="btnReq" class="btn_basic_s" style="cursor: pointer;">일괄등록</button>
				<button id="btnSaveExl" name="btnSaveExl" class="btn_basic_s" style="cursor: pointer;">엑셀저장</button>
				<button id="btnExlTmp" name="btnExlTmp" class="btn_basic_s" style="cursor: pointer;">엑셀템플릿</button>
			</div>
		</div>
	</div>
        
	<div class="az_board_basic margin-10-top" style="height: 80%;">
		<div data-ax5grid="mapGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
	</div>
</div>

<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<input type="hidden" id="userId" name="userId" 		value="<%=userId%>"/>
<input type="hidden" id="userName" name="userName"  value="<%=userName%>"/>
<input type="hidden" id="adminYN" name="adminYN" 	value="<%=adminYN%>"/>
<input type="hidden" id="strReqCD" name="strReqCD"  value="<%=strReqCD%>"/>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopBatchMapping.js"/>"></script>