<!-- 형상관리운영현황 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">보고서 <strong>&gt; 기간별운영현황</strong></div>
	<div class="az_search_wrap sm-row">
		<div class="az_in_wrap por">
		
			<div class="por">
				<div class="width-20 dib vat">
					<label class="tit_60 poa">조회구분</label> 
				</div>
				<div class="width-20 dib">
					<label class="tit_60 poa">1단계</label> 
					<div class="ml_60">
						<div id="cboStep1" data-ax5select="cboStep1" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
					</div>
				</div>
				<div class="width-20 dib vat">
					<label class="tit_60  poa">3단계</label> 
					<div class="ml_60">
						<div id="cboStep3" data-ax5select="cboStep3" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
					</div>
				</div>
				
				<div class="width-20 dib vat">
					<label class="tit_60  poa">시스템</label>
					<div class="ml_60">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
					</div> 
				</div>
				<div class="width-20 dib vat tar">
					<label class="tit_60 text-right dib vat">조회기간</label> 
					<div data-ax5picker="basic" class="az_input_group dib vat">
						<input id="dateSt" name="start_date" type="text" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
					<span class="sim">∼</span>
					<div data-ax5picker="basic2" class="az_input_group dib vat">
						<input id="dateEd" name="end_date" type="text" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
				</div>
				
				<!--  -->
			</div>
			
			<div class="row">
				<div class="width-20 dib">
					<div id="cboGbn1" data-ax5select="cboGbn1" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
				</div>
				<div class="width-20 dib vat">
					<label class="tit_60  poa">2단계</label> 
					<div class="ml_60">
						<div id="cboStep2" data-ax5select="cboStep2" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
					</div>
				</div>
				<div class="width-20 dib vat">
					<label class="tit_60  poa">4단계</label>
					<div class="ml_60">
						<div id="cboStep4" data-ax5select="cboStep4" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
					</div> 
				</div>
				<div class="width-20 dib vat">
					<label class="tit_60 poa">업무</label>
					<div class="ml_60">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib tal"></div>
					</div> 
				</div>
				<div class="width-20 dib vat tar">
					<button id="btnQry1" class="btn_basic_s" style="width: 70px;">조회</button>
					<button id="btnExcel" class="btn_basic_s" style="margin-left: 3px; float: right;">엑셀저장</button>
				</div>
			</div>

		</div>
	</div>

	<div class="az_board_basic" style="height: 85%;">
		<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="height: 100%;" class="frameResize"></div>
	</div>
	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/ManagementCount.js"/>"></script>
