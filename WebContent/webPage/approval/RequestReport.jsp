<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />

<style>
.wrap_width {
	width: calc(98% - 280px);
}
</style>

<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">
			결재 <strong>&gt; 영향분석확인 목록조히</strong>
		</div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row por">
					<!--시스템S-->
					<div class="width-20 dib">
						<label class="tit_80 poa">시스템</label>
						<div class="ml_80">
							<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<div class="width-20 dib vat">
						<label class="tit_80 poa margin-10-left">신청부서</label>
						<div class="ml_80">
							<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
						</div>
					</div>
					<div class="width-20 dib vat">
						<label class="tit_80 poa margin-10-left">신청인</label>
						<div class="ml_80">
						<input id="txtUser" type="text" class="width-100" />
	               	</div>
					</div>
					<div class="width-23 dib vat">
						<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
							<label class="tit_80 poa margin-10-left">조회시작일</label>
							<div class="ml_80 vat">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
								<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
					<div class="float-right dib">
						<button class="btn_basic_s" id="btnExcel" style="width: 70px; margin-right: 5px;">엑셀저장</button>
						<button class="btn_basic_s" id="btnQry" style="width: 70px;">조회</button>
					</div>
				</div>
				<div class="row">
					<div class="width-60 dib vat" style="visibility: hidden;">
						<input type="checkbox" class="checkbox-file" id="chkAll" data-label="전체조회" />
					</div>
					<div class="width-23 dib vat">
						<div id="divPicker" data-ax5picker="basic2" class="az_input_group dib">
							<label class="tit_80 poa margin-10-left">조회종료일</label>
							<div class="ml_80 vat">
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
								<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="az_board_basic" style="height: 100%">
			<div id="requestGrid" data-ax5grid="requestGrid" data-ax5grid-config="{}" style="width: 100%; height: 100%;" class="resize"></div>
		</div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno" /> <input type="hidden" name="user" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"
	src="<c:url value="/js/ecams/approval/RequestReport.js"/>"></script>
