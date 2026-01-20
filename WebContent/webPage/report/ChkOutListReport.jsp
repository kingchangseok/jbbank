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
        <div id="history_wrap">보고서 <strong>&gt; 체크아웃현황</strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
                <div class="row">
                	<!--시스템S-->
                	<div class="width-20 dib">
	                	<label class="tit_60 poa">시스템</label>
	                	<div class="ml_60">
		                    <div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
					</div>
                	<div class="centerDiv dib vat">
	                	<label class="tit_100 poa margin-10-left">프로그램경로</label>
	                	<div class="ml_100">
		                    <input id="txtPath" data-ax-path="txtPath" type="text" class="width-98"/>
	                	</div>
					</div>
	                <div class="float-right">
						<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
					</div>
				</div>
				<!--line2-->
				<div class="row">
					<!--업무-->
					<div class="dib vat width-20">
	                	<label class="tit_60 poa">업무</label>
	                	<div class="ml_60 vat">
		                	<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
	                </div>
					<!--신청인S-->
					<div class="dib vat width-18">
	                	<label class="tit_100 poa margin-10-left">신청자</label>
	                	<div class="ml_100 vat">
		                	<input id="txtUser" type="text" class="width-100"/>
	                	</div>
	                </div>
	                <div class="dib vat width-10">
	                	<label class="tit_60 poa margin-10-left">경과일수</label>
	                	<div class="ml_60 vat">
		                	<input  id="dayTerm" type="number" class="width-100" value="0"/>
	                	</div>
	                </div>
<!-- 					<div class="dib vat margin-10-left">
						<input type="checkbox" class="checkbox-pie" id="chkNonActive" data-label="비활성자" title="체크시 비활성화 된 사용자 체크아웃 현황을 조회 합니다."/>
					</div> -->
					<div class="dib vat margin-10-left">
						<div class="dib vat" style="min-width: 94px;">
							<input type="checkbox" class="checkbox-pie" id="chkDay" data-label="신청일 기준"/>
						</div>
						<div class="dib vat">
							<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:77px;">
								<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
								<span class="sim">&sim;</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:77px;">
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
<script type="text/javascript" src="<c:url value="/js/ecams/report/ChkOutListReport.js"/>"></script>