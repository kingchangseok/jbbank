<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
</style>

<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">빌드/배포 <strong>&gt; 적용신청</strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
                <div class="row">
                	<!--시스템S-->
                	<div class="dib" style="width: calc(100% - 172px);">
	                	<label class="tit_60 poa">SR정보</label>
	                	<div class="ml_60">
		                    <div id="srInfo" data-ax5select="srInfo" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
					</div>
	                <div class="float-right">
						<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
	               		<button class="btn_basic_s" id="btnSrInfo" style="width: 90px; margin-left: 5px;">SR정보확인</button>
					</div>
				</div>
				<!--line2-->
				<div class="row">
					<!--신청인S-->
					<div class="dib vat width-20">
	                	<label class="tit_60 poa">시스템</label>
	                	<div class="ml_60">
		                    <div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
	                </div>
	                <div class="dib vat width-15 margin-10-left">
	                	<label class="tit_60 poa">*처리구분</label>
	                	<div class="ml_60">
		                    <div id="processingLevel" data-ax5select="processingLevel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
	                	</div>
	                </div>
					<div class="dib vat margin-10-left" style="width: 407px;">
						<label class="" style="width: 100px;">*운영배포요청일</label>
						<div id="divPicker" data-ax5picker="basic" class="az_input_group dib" style="">
							<input data-ax5picker="operationDate" id="operationDate" name="operationDate" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
<!-- 							<span class="sim">&sim;</span> -->
<!-- 							<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;"> -->
<!-- 							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> -->
						</div>
					</div>
	                <div class="float-right">
						<div class="width-5 dib vat margin-10-left" style="min-width: 110px; display: none;" id="chkFTDiv">
							<input type="checkbox" class="checkbox-pie" id="chkFT" data-label="취약점연계Skip"/>
						</div>
						<div class="width-5 dib vat margin-10-left" style="min-width: 75px;" id="chkBefDiv">
							<input type="checkbox" class="checkbox-pie" id="chkBef" data-label="선행작업"/>
						</div>			
	                </div>
				</div>
			</div>
		</div>
		<!--검색E-->		

	    <!-- 게시판 S-->
	    <div class="az_board_basic">
	    	<div id="first-grid" data-ax5grid="first-grid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		<div class="row" style="height: 25px;">
				<textarea rows="" cols="" id="txtSayu" style="display: none;"></textarea>
				<button class="btn_basic_s float-right margin-5-right" id="btnApply" style="width: 70px;">적용요청</button>
				<div class="width-5 dib vat margin-10-left float-right" style="min-width: 150px; display: none;" id="chkDBADiv">
					<input type="checkbox" class="checkbox-pie" id="chkDBA" data-label="DBA후결처리(테스트)" disabled/>
				</div>
			</div>
		<form name="popPam">
			<input type="hidden" name="acptno"/>
			<input type="hidden" name="user"/>
			<input type="hidden" name="srid"/>
		</form>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/ApplyReq.js"/>"></script>