<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />

<style>
.width-75 {
	width: calc(97% - 100px);
}

.wrap_width {
	width: calc(98% - 365px);
}
</style>



<!-- contener S -->
<div id="wrapper">
	<div class="contentFrame">
		<!-- history S-->
		<div id="history_wrap">
			보고서 <strong>&gt; 형상관리신청현황</strong>
		</div>
		<!-- history E-->
		<!-- 검색 S-->
		<div class="az_search_wrap">
			<div class="az_in_wrap">
					<div class="row">
						<!--시스템S-->
						<div class="width-20 dib">
							<label class="tit_60">시스템</label>
							<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-75 dib tal"></div>
						</div>
						<div class="width-20 dib vat" style=""></div>
						<div class="width-20 dib vat">
							<label class="tit_80" style="margin-left: 20px;">SR-ID/SR명</label>
							<input id="txtSpms" data-ax-path="txtSpms" type="text"
								class="width-75" />
						</div>
						<div class="dib vat ml_40" style="calc(width: 100% - 80px);">
							<div class="margin-right-50">
								<input id="radioCkOut" name="radioGroup" tabindex="8" type="radio" value="optCkOut" checked="checked" /> 
								<label for="radioCkOut" style="margin-right: 10px;">신청일기준</label> 
								<input id="radioCkIn" name="radioGroup" tabindex="8" type="radio" value="optCkIn" /> 
								<label for="enddate">완료일기준</label>
							</div>
						</div>
						<div class="float-right">
							<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
						</div>
					</div>
					<!--line2-->
					<div class="row">
						<div class="width-20 dib vat">
							<label class="tit_60">신청구분</label>
							<div id="cboSin" data-ax5select="cboSin" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal"></div>
						</div>
						<div class="width-20 dib vat">
							<label class="tit_60">신청부서</label>
							<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal"></div>
						</div>
						<div class="width-20 dib vat">
							<label class="tit_60 ml_40">신청자</label> 
							<input id="txtUser" data-ax-path="txtUser" type="text" class="width-75" />
						</div>
						<div class="dib vat width-25 ml_40">
							<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" class="width-25"> 
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> 
								<span class="sim">&sim;</span> 
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" class="width-25">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>
						</div>
						<div class="float-right">
							<button class="btn_basic_s" id="btnExcel" style="width: 70px;">엑셀저장</button>
						</div>
					</div>
					<!--line3-->
					<div class="row">
						<div class="width-20 dib">
							<label class="tit_60">진행상태</label>
							<div id="step_combo" data-ax5select="step_combo" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal"></div>
						</div>
						<div class="width-20 dib">
							<label class="tit_60">처리구분</label>
							<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal"></div>
						</div>
						<div class="width-20 dib vat">
							<label class="tit_100">프로그램명/설명</label> <input id="txtPrgName" name="txtPrgName" type="text" class="width-75" />
						</div>
					</div>
			</div>
		</div>
		<!--검색E-->

		<!-- 게시판 S-->
		<div class="az_board_basic" style="height: 100%">
			<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}"
				style="width: 100%; height: 100%;" class="resize"></div>
		</div>
	</div>
</div>
<form name="popPam">
	<input type="hidden" name="acptno" /> <input type="hidden" name="user" />
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"
	src="<c:url value="/js/ecams/report/ConfigReqReport.js"/>"></script>