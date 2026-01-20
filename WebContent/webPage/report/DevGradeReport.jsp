<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
.color-red {
	color: red;
	font-weight: bold;
}

.year-btn {
	height: 12px; 
	width: 15px; 
	font-size: 7pt;
	background: white;
}
.month-btn {
	height: 25px;
	width: 25px; 
	background: white;
	border: 1px;
	border-radius: 50%;
}

.dateBtn {
	background: inherit;
}

.dateBtn:hover {
	text-shadow: 0 0 2px red;
}

.dateBtn:active {
	padding-top : 2px;
	color: #fff;
}

.az_board_basic label {
	margin-top: 5px;
	margin-bottom: 5px;
}
</style>

<c:import url="/webPage/common/common.jsp" />

<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">보고서 <strong>&gt; 개발실적등급별보고서</strong></div>
		<div class="az_search_wrap" style="margin-bottom: 0px;">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
					<div id="searchBar">
						<div class="width-43 dib por"></div>

						<div class="width-30 dib por">
							<button type="button" class="month-btn dateBtn" id="month-prev">◀</button>
							<label style="margin-right: 10px; margin-left: 10px; width: 26px;" id="month"></label>
							<label id="year"></label>
							<div class="por width-4 dib">
	                        	<button type="button" class="dib year-btn dateBtn" id="year-next">▲</button>
	                        	<button type="button" class="dib year-btn dateBtn" id="year-prev">▼</button>
	                        </div>
							<button type="button" class="month-btn dateBtn" id="month-next">▶</button>
						</div>
						<div class="width-27 dib vat">
							<div class="vat dib margin-10-right float-right" id="btnBox"><!--수정-->
								<button class="btn_basic_s" data-grid-control="excel-export" style="width: 70px;" id="btnSearch">조 회</button>
							</div>
						</div>
					</div>					
				</div>						
			</div>
		</div>	
	</div>
	
	<div class="az_board_basic margin-15-left" style="width: 98%;">
		<label >*부서별 의뢰 현황</label>
		<div id="mainGrid1" data-ax5grid="mainGrid1" data-ax5grid-config="{}" style="width:100%; height: 180px;">
		
		</div>
		<label>*업무 등급별 처리현황(S:4주~, A:3~4주, B:2~3주, C:1주, D:단순업무)</label>
		<div id="mainGrid2" data-ax5grid="mainGrid2" data-ax5grid-config="{}" style="width:100%; height: 180px;">
		
		</div>
		<label>*연구소 실 별 처리 현황</label>
		<div id="mainGrid3" data-ax5grid="mainGrid3" data-ax5grid-config="{}" style="width:100%; height: 180px;">
		
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/DevGradeReport.js"/>"></script>