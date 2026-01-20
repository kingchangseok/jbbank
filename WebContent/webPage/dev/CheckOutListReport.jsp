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
			대여<strong>&gt; 대여 목록조회</strong>
		</div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row por">
					<!--시스템S-->
					<div class="width-25 dib">
						<label class="tit_50 poa">구분</label>
						<div class="ml_50">
							<div id="cboGubun" data-ax5select="cboGubun" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<div class="width-25 dib vat">
						<label class="tit_80 poa margin-10-left">담당자 행번</label>
						<div class="ml_80">
							<input id="txtUser" data-ax-path="txtUser" type="text" class="width-100" />
						</div>
					</div>
					<div class="width-15 dib vat">
						<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
							<label class="tit_80 poa margin-10-left">조회시작일</label>
							<div class="ml_80 vat">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
								<button id="btnSt" class="btn_calendar"id="btnSt" disabled="disabled"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
					<div class="float-right">
						<button class="btn_basic_s" id="btnQry" style="width: 70px;">조회</button>
					</div>
				</div>
				<!-- line2 -->
				<div class="row por">
					<div class="dib vat width-25">
						<label class="tit_50 poa">해당팀</label>
						<div class="ml_50">
							<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<div class="width-25 dib vat">
						<label class="tit_80 poa margin-10-left">프로그램명</label>
						<div class="ml_80">
							<input id="txtProg" data-ax-path="txtProg" type="text" class="width-100" />
						</div>
					</div>
					<div class="dib vat">
						<div id="divPicker" data-ax5picker="basic2" class="az_input_group dib">
							<label class="tit_80 poa margin-10-left">조회종료일</label>
							<div class="ml_80 vat">
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
								<button id="btnEd" class="btn_calendar"id="btnEd" disabled="disabled"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
					<div class="ml_10 dib vat">
						<input type="checkbox" class="checkbox-file" id="chkSelf" data-label="내건만 조회" checked="checked" />
					</div>
					<div class="float-right">
						<button class="btn_basic_s" id="btnExcel" style="width: 70px;">엑셀저장</button>
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
	<input type="hidden" name="itemid" /> <input type="hidden" name="adminYN" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/CheckOutListReport.js"/>"></script>
