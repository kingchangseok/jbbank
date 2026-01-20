<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<body style="min-width: auto;">
	<div style="overflow-y: hidden;">
		<div class="az_search_wrap" style="padding: 10px;">
			<div style="height: 5%;">
		        <label id="lbProgSubView1" class="title">프로그램공통</label>
		        <div class="float-right margin-5-right">
					<button id="btnSubPop" class="btn_basic_s" style="top: 0;">검증 항목 수정</button>
		        </div>
			</div>		
		    <div class="row az_board_basic" style="width: 100%; height: 40%;">
			    <div data-ax5grid="progGrid" style="height: 100%;"></div>
			</div>
			<div class="margin-20-top" style="height: 5%;">
		        <label id="lbProgSubView2" class="title">프로그램개별</label>
			</div>		
		    <div class="row az_board_basic" style="width: 100%; height: 40%;">
			    <div data-ax5grid="progGrid2" style="height: 100%;"></div>
			</div>	
		</div>
	</div>
</body>

<form name = "setReqData">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/request/ReqCheckTab.js"/>"></script>
