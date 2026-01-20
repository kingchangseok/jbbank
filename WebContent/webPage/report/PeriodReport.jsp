<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.block1 {
	width: calc(100% - 80%);
}
.centerDiv {
	width: calc( (100% - 20%) - 70px );
}

.btn_calendar{
	padding-left: 9px !important;
    padding-right: 9px !important;
}
</style>

<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">보고서 <strong>&gt; 개인별변경추이 </strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
                <div class="row">
                	<!--시스템S-->
                	<div class="width-20 dib">
		                	<input id="optDay" name="radioGroup" tabindex="8" type="radio" value="optCkOut" checked="checked"/>
							<label for="optDay" style="margin-right: 10px;">일별</label>
							<input id="optWeek" name="radioGroup" tabindex="8" type="radio" value="optCkIn"/>
							<label for="optWeek">주간</label>
							<input id="optYear" name="radioGroup" tabindex="8" type="radio" value="optCkIn"/>
							<label for="optYear">월간</label>
							<input type="checkbox" class="checkbox-user" id="chkDel" data-label="폐기포함"/>
					</div>
                	
                	<div class="dib vat width-15 margin-20-left">
	                	<label class="tit_80 poa">시스템</label>
	                	<div class="ml_100">
		                    <div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
					</div>
					<div class="width-25 dib vat margin-20-left">
						<label class="tit_100 poa ">사용자명</label>
	                	<div class="ml_100 vat">
		                	<input id="txtUser" type="text" style="width:49%;"/>
		                	<div id="cboUser" data-ax5select="cboUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-40 dib vat margin-20-left"></div>
	                	</div>
	                </div>
                    <div class="float-right">
						<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
					</div>
				</div>
				<!--line2-->
				<div class="row">
					<!--업무-->
					<div class="dib width-20">
	                	<label class="tit_80 poa">조회기준</label>
	                	<div class="ml_60 vat">
		                	<div id="cboSel" data-ax5select="cboSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
	                </div>
					<!--신청인S-->
					<div class="dib vat width-15 margin-20-left">
	                	<label class="tit_80 poa">프로그램기준</label>
	                	<div class="ml_100 vat">
		                	<div id="cboReq" data-ax5select="cboReq" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
	                </div>
	                
	                <div class="dib vat width-15 margin-20-left">
	                	<label class="tit_80 poa">변경기준</label>
	                	<div class="ml_100 vat">
		                	<div id="cboReqCd" data-ax5select="cboReqCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
	                </div>
	                
					<div class="dib vat  margin-20-left">
						<div class="dib vat">
							<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
								<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
								<span class="sim">&sim;</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
								<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
	                <div class="float-right">
						<button class="btn_basic_s" id="btnExcel" style="width: 70px;">엑셀저장</button>
	                </div>
				</div>
			</div>
		</div>
		<!--검색E-->		

	    <!-- 게시판 S-->
	    <div class="az_board_basic">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		<form name="popPam">
			<input type="hidden" name="acptno"/>
			<input type="hidden" name="user"/>
			<input type="hidden" name="itemid"/> 
			<input type="hidden" name="syscd"/>
			<input type="hidden" name="rsrccd"/>
			<input type="hidden" name="rsrcname"/>
		</form>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/PeriodReport.js"/>"></script>