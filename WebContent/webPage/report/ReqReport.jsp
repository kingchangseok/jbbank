<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.width-75 {
	width: calc(97% - 100px);
}

.wrap_width {
	width: calc(98% - 280px);
}

[data-ax5grid-cellholder] {
	line-height: 12px !important;
	vertical-align: middle !important;
}
</style>



<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">보고서 <strong>&gt; 전체 승인내역</strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="l_wrap wrap_width dib">
	                <div class="por">
	                	<!--시스템S-->
	                	<div class="width-15 dib">
		                	<input id="optAcpt" name="radioGroup" tabindex="8" type="radio" value="optCkOut" checked="checked"/>
							<label for="optAcpt" style="margin-right: 10px;">림스_신청일기준</label>
							<input id="optEnd" name="radioGroup" tabindex="8" type="radio" value="optCkIn"/>
							<label for="optEnd">림스_배포일기준</label>
						</div>
						<div class="width-20 dib vat">
		                	<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
								<span class="sim">&sim;</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>
						</div>
						
						<div class="width-15 dib vat"">
							<div class="sm-row">
								<input type="checkbox" class="checkbox-user" id="chkJungi" data-label="정기작업포함"/>
								<input type="checkbox" class="checkbox-user" id="chkDepYn" data-label="운영배포건포함"/>
							</div>
						</div>
						
						<div class="width-20 dib vat"">
		                	<label style="margin-right:30px;">신청부서</label>
							<div id="reqDeptSel" data-ax5select="reqDeptSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal"></div>
						</div>
						
						<div class="width-20 dib vat"">
							<div class="vat dib margin-5-left">
								<button class="btn_basic_s" id="btnSearch">조회</button>
							</div>
							<div class="vat dib margin-5-left">
								<button class="btn_basic_s" id="btnExcel">엑셀저장</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--검색E-->		

	    <!-- 게시판 S-->
	    <div class="az_board_basic" style="height:100%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
		
			</div>
		</div>	
	</div>
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/ReqReport.js"/>"></script>