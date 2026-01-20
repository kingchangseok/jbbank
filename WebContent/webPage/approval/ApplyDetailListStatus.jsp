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
</style>



<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">결재확인 <strong>&gt; 적용대상명세</strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="l_wrap dib"  style="width: 100%;">
	                <div class="por">
	                	<!--시스템S-->
	                	<div class="width-20 dib">
		                	<label class="dib" style="width:49px;">시스템</label>
		                    <div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
						    </div>
						</div>
					 	<div class="width-20 dib">
							<label style="width:49px;">적용구분</label>
							<div id="cboJobSel" data-ax5select="cboJobSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
						    </div>
						</div>
						
						<div class="width-20 dib vat">
	                		<label class="dib poa" style="width:80px;">신청인</label>
							<div class="ml_80 vat">
								<input id="reqUserId" data-ax-path="reqUserId" type="text" class="width-80"/>
							</div>	
						</div>
						
						<div class="width-20 dib">
							<label class="dib poa" style="width:49px;">조회일</label>
							<div id="divPicker" data-ax5picker="basic" class="ml_80 az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
								<span class="sim">&sim;</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>
						</div>
					</div>
					
					<div class="por">
						<div class="width-20 dib">
							<label style="width:49px;">업무</label>
							<div id="cboJobSel" data-ax5select="cboJobSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
						    </div>
						</div>
						<div class="width-20 dib">
		                	<label style="width:49px;">진행상태</label>
							<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
						    </div>
						</div>	
						
						<div class="width-20 dib vat">
	                		<label class="dib poa" style="width:80px;">프로그램ID</label>
							<div class="ml_80 vat">
								<input id="reqUserId" data-ax-path="reqUserId" type="text" class="width-80"/>
							</div>	
						</div>
					
						<div class="tar dib vat" style="float:right;">
							<div class="vat dib margin-5-left">
								<button class="btn_basic_s" id="btnExcel">엑셀저장</button>
							</div>
							<div class="vat dib margin-5-left">
								<button class="btn_basic_s" id="btnSearch">조회</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/approval/ApplyDetailListStatus.js"/>"></script>