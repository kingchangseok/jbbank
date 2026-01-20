<!--  
	* 화면명: 개발계획/실적 등록
	* 화면호출:
	 1) 개발계획/실적 -> 개발계획/실적 탭
	 2) 체크아웃, 체크인 등 SR정보 버튼 클릭 -> 개발계획/실적 탭 
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>
<style>
[data-ax5select] .ax5select-display.input-sm, [data-ax5select] .input-group-sm>.ax5select-display.form-control, [data-ax5select] .input-group-sm>.ax5select-display.input-group-addon, [data-ax5select] .input-group-sm>.input-group-btn>.ax5select-display.btn
{
	min-width : 0px !important;
}
</style>
<!-- contener S -->
<body style="padding: 10px !important; width: 100% !important; min-width: 0px !important;">
    <!-- <div class="content"> -->
		<!-- 하단 S-->
			<!-- 게시판 S-->
		    <div class="az_board_basic az_board_basic_in margin-10-bottom" style="height: 30%; min-height: 80px;">
		    	<div data-ax5grid="grdWorker" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%; min-height: 80px;" class="frameResize"></div>
			</div>	
			<!-- 게시판 E -->
			<!--하단 좌측-->
			<div class="l_wrap width-25 vat write_wrap write_wrap_100">
				<div class="tit">
					<!-- <h3>&#9642;개발계획</h3> -->
					<h3><input type="radio" class="radio-pie" id="rdoPlan" name="group" data-label="개발계획"></h3>
				</div>
				<div class="row">
					<dl>
						<dt><label>작성일</label></dt>
						<dd><input id="txtWriteDay" name="txtWriteDay" type="text" disabled="disabled"></dd>
					</dl>
				</div>	
				<div class="row">
					<dl>				
						<dt><label>작성인</label></dt>
						<dd><input id="txtWriter" name="txtWriter" type="text" disabled="disabled"></dd>
					</dl>
				</div>
				<div class="row">
					<dl>
						<dt><label>*예상소요시간</label></dt>
						<dd><input id="txtExpTime" name="txtExpTime" type="text" class="width-30" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" autocomplete="off"><em>*시간단위 입력</em></dd>
					</dl>
				</div>
				<div class="row">
					<dl>
						<dt><label>*예상개발시작일</label></dt>
						<dd><input id="txtExpStdate" name="txtExpStdate" type="text" class="f-cal" data-ax5picker="txtExpStdate"><span class="btn_calendar poa_r"><i class="fa fa-calendar-o"></i></span></dd>
					</dl>
					
				</div>
				<div class="row">
					<dl>
						<dt><label>*예상개발종료일</label></dt>
						<dd><input id="txtExpEnddate" name="txtExpEnddate" type="text" class="f-cal" data-ax5picker="txtExpEnddate"><span class="btn_calendar poa_r"><i class="fa fa-calendar-o"></i></span></dd>
					</dl>
				</div>
				<div class="row">
				    <dl>
				    	<dt><label id="lbRate">*기능점수등급</label></dt>
					    <dd>
							<div id="cboRate" data-ax5select="cboRate" data-ax5select-config="{size:'sm',theme:'primary'}" style="">
						    </div>
						</dd>
					</dl>
				</div>
				<div class="row tar">
					<button id="btnRegPlan" class="btn_basic_s" data-grid-control="excel-export">등록</button>
				</div>
			</div>
			<!--하단 우측-->
			<div class="r_wrap width-70 vat write_wrap">
				<div class="tit">
					<!-- <h3>&#9642;개발실적</h3> -->
					<h3><input type="radio" class="radio-pie" id="rdoResult" name="group" data-label="개발실적"></h3>
				</div>				
				<div class="bg_white pd_10">						
					<dl class="dib vat">
						<dt><label>작업일</label></dt>
						<dd><input id="txtDevDate" name="txtDevDate" type="text" disabled="disabled" class="width-70" data-ax5picker="txtDevDate" style="width:70%"><span class="btn_calendar"><i class="fa fa-calendar-o"></i></span></dd>
					</dl>
					<dl class="dib vat">				
						<dt><label>작업시간</label></dt>
						<dd><input id="txtDevTime" name="txtDevTime" type="text" class="width-70" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" style="width:70%" autocomplete="off"><button class="btn_basic_s margin-5-left" id="btnRegResult" data-grid-control="excel-export">등록</button></dd>
					</dl>
					<p class="margin-3-top">*일별 투입시간을 시간단위로 입력[개발계획] 등록 후[개발실적]등록가능</p>
				</div>
				<div class="row">
					<div class="row"><label>작업시간내역</label></div>
					<div class="az_board_basic scroll_h az_board_basic_in" style="height: 40%">
				    	<div data-ax5grid="grdWorkTime" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				    </div>
				</div>
			</div>
    <!-- </div> -->
</body>
	
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/DevPlanTab.js"/>"></script>