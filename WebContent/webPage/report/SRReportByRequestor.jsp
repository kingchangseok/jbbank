<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.mt-12{
	margin-top: 12px;
}

.width-385 {
	width: 385px;
}
</style>
<div class="contentFrame">
	<div id="history_wrap">보고서 <strong>&gt; 요청자별 SR현황</strong></div>
		<div class="az_search_wrap" style="min-width: 1160px;">
			<div class="az_in_wrap">
				<div class="row">
					<div class="dib vat margin-20-right margin-10-left">
	                	<label class="tit_60 poa">SR요청자</label>
	                	<div class="ml_60 vat dib">
		                	<input id="SRrequestorTxt" type="text" class="width-100">
	                	</div>
	                </div>
	                <div class="dib" style="width: 380px;">
	                	<label class="tit_60">등록일자</label>
						<div class="width-16 dib vat" style="min-width: 290px;">
							<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
								<span class="sim">∼</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>
						</div>
					</div>
	                <div class="dib">
						<button class="btn_basic_s" id="btnQry" style="width: 70px; margin-right: 10px;">조회</button>
						<button class="btn_basic_s" id="btnExcel" style="width: 70px;">엑셀저장</button>
	                </div>
				</div>
			</div>
		</div>

	    <!-- 게시판 S-->
	    <div class="az_board_basic">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;"  class="resize">
			</div>
		</div>

</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/SRReportByRequestor.js"/>"></script>