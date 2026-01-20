<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.width-250 {
	width: 250px;
}
</style>
<div class="contentFrame">
	<div id="history_wrap">보고서 <strong>&gt; 전체 미결 현황 조회</strong></div>
		<div class="az_search_wrap" style="min-width: 1160px;">
			<div class="az_in_wrap">
				<div class="row">
					<div class="dib vat width-250 ">
	                	<label class="tit_60">시스템</label>
		               	<div class="margin-10-left width-70 dib">
			            	<div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
		                </div>
	                </div>
	                <div class="dib vat width-250  margin-20-left">
		                <div class="row thumbnail dib" style="margin-top: 5px !important;">
							<span class="g_nail">처리완료(후결)</span>
							<span class="b_nail">진행중</span>
						</div>
	                </div>
	                <div class="float-right dib">
						<button class="btn_basic_s margin-5-right" id="btnQry" style="width: 70px;">조회</button>
						<button class="btn_basic_s" id="btnExcel" style="width: 70px;">엑셀저장</button>
	                </div>
				</div>
			</div>
		</div>

	    <!-- 게시판 S-->
	    <div class="az_board_basic">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		
		<form name="popPam">
			<input type="hidden" name="acptno"/>
			<input type="hidden" name="user"/>
		</form>

</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/AllPendencyReport.js"/>"></script>