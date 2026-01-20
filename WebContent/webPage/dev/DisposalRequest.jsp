<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.width-250 {
	width: calc(30% + 70px);
}

.txtBox1 {
	width: calc(100% - 60px);
}

.radio, .checkbox {
	margin-top: -2px;
}
</style>
<div class="contentFrame">
	<div id="history_wrap">형상관리 <strong>&gt; 프로그램폐기신청</strong></div>
		<div class="az_search_wrap" style="min-width: 1160px;">
			<div class="az_in_wrap">
				<div class="row">
					<div class="dib vat" style="width: 280px;">
	                	<label class="tit_60">시스템</label>
		               	<div class="margin-10-left width-70 dib">
			            	<div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
		                </div>
	                </div>
	                <div class="dib vat margin-20-left" style="width: calc(45% - 150px);">
	                	<label class="">프로그램경로</label>
			            	<input id="programPath" type="text" class="" style="width: calc(100% - 80px);">
	                </div>
	                <div class="dib vat margin-20-left" style="width: calc(45% - 150px);">
	                	<label class="">프로그램명</label>
			            	<input id="programName" type="text" class="" style="width: calc(100% - 80px);">
	                </div> 
	                <div class=" dib">
						<button class="btn_basic_s margin-20-right" id="btnQry" style="width: 70px;">조회</button>
	                </div>
				</div>
			</div>
		</div>
		
	    <!-- 게시판 S-->
	    <div class="az_board_basic">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		<div class="row">
			<label class="padding-5-left">*폐기사유</label>
			<input id="reasonTxt" type="text" class="textBox" style="width: calc(100% - 450px);">
			<label class="padding-5-left" style="color: red;">*폐기신청은 운영중인 프로그램만 가능합니다.</label>
			<button class="btn_basic_s margin-10-right float-right" id="btnReq" style="width: 70px;">폐기신청</button>
		</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/DisposalRequest.js"/>"></script>