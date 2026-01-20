<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.year-btn {
	height: 11.5px; 
	width: 15px; 
	font-size: 7pt;
	background: white;
}
.month-btn {
	height: 23px;
	width: 25px; 
	background: white;
	border: 1px;
	border-radius: 50%;
}

.dateBtn:hover {
	text-shadow: 0 0 2px red;
}

.dateBtn:active {
	padding-top : 2px;
	color: #fff;
}
.date-div {
	background: #fff;
	border: 1px solid lightgray;
	text-align: center;
}
.mb-div {
	padding-bottom: 3px;
}
.calWid {
	width: calc(100% - 120px);
}
.calWid2 {
	width: calc(100% - 100px);
}
.calWid3 {
	width: calc(100% - 140px);
}

</style>

<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">보고서 <strong>&gt; 개발자별현황</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
					<div>
						<div class="rowItem dib" style="width:calc(100% - 465px)">
							<!-- 연구소 실 별 -->
							<div class="width-27 dib por">
								<label class="tit_80 poa">연구소 실 별</label>
							    <div id="dept" data-ax5select="dept" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" onchange="" class="dib ml_80" style="width:calc(100% - 80px);">
							    </div>
							</div>
							<!-- SR등급 -->
							<div class="width-33 dib por">
								<label class="margin-10-right margin-10-left tit_60 poa">SR등급</label>
								<div id="rate" data-ax5select="rate" data-ax5select-config="{size:'sm',theme:'primary'}" onchange="" class="dib ml_60" style="width:calc(100% - 60px);">
							    </div>						    
							</div>
							<!-- 개발자ID/개발자명 -->
							<div class="width-40 dib por vat">
								<label class="margin-10-right margin-10-left tit_80 poa">개발자ID/명</label>
								<input class="ml_80" id="developerId" type="text" placeholder="" style="width:calc(100% - 80px);">
							</div>
							
						</div>
						<div class="dib vat margin-10-right">
							<label class="margin-10-right margin-10-left tit_60 poa">월 별</label>
							<div class="dib">
								<div class="dib date-div ml_60" style="width:calc(100% - 60px);">
									<button type="button" class="month-btn dateBtn" id="month-prev"><div class="mb-div">◀</div></button>
									<label style="margin-right: 10px; margin-left: 10px; width: 26px; line-height: 23px;" id="month"></label>
									<label id="year" style="line-height: 23px;"></label>
									<div class="por width-10 dib">
			                        	<button type="button" class="dib year-btn dateBtn" id="year-next">▲</button>
			                        	<button type="button" class="dib year-btn dateBtn" id="year-prev">▼</button>
			                        </div>
									<button type="button" class="month-btn dateBtn" id="month-next"><div class="mb-div">▶</div></button>
								</div>
							</div>
						</div>
						<div class="vat dib">
							<button class="btn_basic_s" data-grid-control="excel-export" style="width: 70px;" id="btnSearch">조회</button>
								<button class="btn_basic_s margin-5-left" data-grid-control="excel-export" style="width: 70px; " id="btnExcel">엑셀저장</button>
							<button class="btn_basic_s margin-5-left" data-grid-control="excel-export" style="width: 70px;" id="reset">초기화</button>
						</div>
					</div>
				</div>						
			</div>
		</div>	
	</div>
	
	<div class="az_board_basic">
		<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 600px;">
		
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/ProgrammerReport.js"/>"></script>