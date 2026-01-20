<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<div class="contentFrame">
    <!-- history S-->
    <div id="history_wrap">결재확인 <strong>&gt; 신청현황</strong></div>
    <!-- history E-->         
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap dib" style="width: 100%;">
                <div class="por">
                	<!--신청부서S-->
                	<div class="width-20 dib vat">
	                	<label class="tit_80 dib poa">요청부서</label>
	                	<div class="ml_80">
		                    <div id="cboDept1" data-ax5select="cboDept1" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
	                	</div>
					</div>
					<!--시스템S-->
					<div class="width-20 dib vat">
						<label class="tit_80 dib poa">&nbsp;&nbsp;&nbsp;SR상태</label>
						<div class="ml_80">
							<div id="cboSta1" data-ax5select="cboSta1" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
						</div>
					</div>	
					<div class="dib vat r_wrap">
						<input id="optSub1" type="radio" name="rdoGbn" value="0"/>
						<label for="optSub1">전체대상</label>
						<input id="optSub2" type="radio" name="rdoGbn" value="1"/>
						<label for="optSub2">팀내진행건</label>
						<input id="optSub3" type="radio" name="rdoGbn" value="2"/>
						<label for="optSub3">내진행건만</label>
					</div>		
				</div>
                <div class="row por">
                	<!--신청종류S-->
                	<div class="width-20 dib vat">
	                	<label class="tit_80 dib poa">등록부서</label>
	                	<div class="ml_80">
		                    <div id="cboDept2" data-ax5select="cboDept2" data-ax5select-config="{size:'sm', theme:'primary',selecteditable: true}" style="width:100%;"></div> 	
	                	</div>
					</div>
					<!--처리상태S-->
					<div class="width-20 dib vat">
						<label class="tit_80 dib poa">&nbsp;&nbsp;&nbsp;개발자상태</label>
						<div class="ml_80">
							<div id="cboSta2" data-ax5select="cboSta2" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
	                	</div>
					</div>
					<!--SR-ID/SR명 S-->
					<div class="width-30 dib vat">
	                	<label class="tit_140 dib poa">&nbsp;&nbsp;&nbsp;SR-ID/SR명/GENIE번호</label>
	                	<div class="ml_140 vat">
							<input id="txtTitle" type="text" class="width-100" />
	                	</div>
					</div>
					<div class="dib r_wrap">
						<label class="tit_40 dib poa">등록일</label>
						<div id="divPicker" data-ax5picker="basic" class="az_input_group dib ml_50">
							<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							<span class="sim">∼</span>
							<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
					</div>	
				</div>
				<div class="row por">
					<div class="row thumbnail dib" style="margin-top: 5px !important;">
						<span class="o_nail">접수</span>
						<span class="gr_nail">개발</span>
						<span class="pu_nail">테스트</span>
						<span class="b_nail">적용</span>
						<span class="g_nail">처리완료</span>
						<span class="r_nail">반려 또는 취소</span>
					</div>
					<div class="dib r_wrap">		
						<div class="vat dib margin-5-left">
							<button class="btn_basic_s" id="btnQry" style="margin-left: 0px; margin-right: 0px;">조회</button>
						</div>
						<div class="vat dib margin-5-left">
							<button class="btn_basic_s" id="btnReset" style="margin-left: 0px; margin-right: 0px;">초기화</button>
						</div>
						<div class="dib">
							<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel" style="margin-left: 5px; margin-right: 0px;">엑셀저장</button>
						</div>
					</div>
				</div>		
			</div>
		</div>
	</div>
	<!--검색E-->		
    <!-- 게시판 S-->
    <div class="az_board_basic" style="height: 80%">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
	</div>	
</div>
<form name="popPam">
	<input type="hidden" name="isrid"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/sr/SRStatus.js"/>"></script>