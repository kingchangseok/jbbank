<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
	.borderbox {
		padding: 10px; 
		border: 3px solid #4c98cd;
		border-radius : 10px;
		background-color : #f8f8f8;
	}
	
	.cnt {
		color: #245490;
		cursor: pointer;
	}
	
	.cnt:hover {
		color: #FF0000;
	}
</style>

<div class="contentFrame">
	<div class="l_wrap borderbox ml_40" style="width: 200px; height: 190px; min-height: 190px; margin: 20 20 20 20;">
		<div style="margin-top: 40px;">
			<div class="tac"><label id="lbName" style="color: #245490;"></label></div>
			<div class="tac"><label id="lbLogin">님이 로그인 하셨습니다</label></div>
			<div class="tac">
				<label id="lbNot">미결</label>
				<label id="lbAprv" class="cnt"></label>
				<label id="lbCnt">건</label>
			</div>
		</div>
	</div>
	
	<div class="r_wrap" style="width: calc(100% - 240px)">
		<div><label id="lbAppro" class="tit_80">결재현황</label></div>
	    <div class="az_board_basic az_board_basic_in" style="height: 20%; margin-bottom: 10px;">
	    	<div data-ax5grid="approGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%; class="resize"></div>
		</div>	
		<div><label id="lbOrder" class="tit_80">업무지시서목록</label></div>
	    <div class="az_board_basic az_board_basic_in" style="height: 20%; margin-bottom: 10px;">
	    	<div data-ax5grid="orderGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
		<div><label id="lbChkOut" class="tit_80">대여목록</label></div>
	    <div class="az_board_basic az_board_basic_in" style="height: 20%; margin-bottom: 10px;">
	    	<div data-ax5grid="chkOutGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
		<div><label id="lbChkIn" class="tit_80">적용목록</label></div>
	    <div class="az_board_basic az_board_basic_in" style="height: 20%; margin-bottom: 10px;">
	    	<div data-ax5grid="chkInGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
	</div>
</div>

<form name="popPam" accept-charset="utf-8">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="orderId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/main/Process.js"/>"></script>