<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.wCheck-label{
	margin-right : 0px !important;
}
.write_wrap dl {
	font-size: 0px;
}
.row:after {
		content:""; 
		display:block; 
		clear:both;
	}
	.checkIcon { /* 툴팁박스 체크 아이콘 */
	content: '';
    width: 8.8px;
    height: 6.4px;
    position: absolute;
    top: 11px;
    /* right: 0px; */
    border: 2px solid #3492db;
    border-top: none;
    border-right: none;
    background: transparent;
    -webkit-transform: rotate(-50deg);
    -moz-transform: rotate(-50deg);
    -ms-transform: rotate(-50deg);
    -o-transform: rotate(-50deg);
    transform: rotate(-50deg);
}
.tipLabel { /* 툴팁 박스 내부 라벨 */
	line-height: 20px;
	font-size: 12px;
	font-weight: normal;
	letter-spacing: 0.2px;
	margin-left: 15px;
}
@media(max-width: 1356px) {
	#TimeLineBox{width: auto !important;}
	.thumbnail {margin-left: 10px;}
}
</style>

<!-- history S-->
<div id="history_wrap"></div>
<!-- history E-->   

<!-- 검색 S-->
<div class="az_search_wrap" style="height:4%">
	<div class="az_in_wrap sr_status" style="height:100%">
		<div class="por" style="height:10%">
			<div class="width-71 dib vat">
	            <label class="tit_60 poa">*ISR 정보</label>
	            <div class="ml_60 vat">
					<div id="cboSrId" class="dib width-100" data-ax5select="cboSrId" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
				</div>
			</div>
			<div class="r_wrap">
				<button id="btnReqInfo" class="btn_basic_s" style="width:88px;font-size:12px;margin-left: 2px; margin-right: 0px;">요구관리정보</button>
				<button id="btnChgInfo" class="btn_basic_s" style="width:88px;font-size:12px;margin-left: 2px; margin-right: 0px;">변경관리정보</button>
				<button id="btnTestInfo" class="btn_basic_s" style="width:100px;font-size:12px;margin-left: 2px; margin-right: 0px;">테스트관리정보</button>
			</div>
		</div>

	</div>
</div>
<!-- 검색 E-->

<!-- Tab S -->
<div id="srTab" class="half_wrap" style="height:85%; margin-top:10px;">
	<div class="tab_wrap">
		<ul class="tabs">
			<li rel="tabWorkPlan" id="tab8">작업계획서</li>
		</ul>
	</div>
	<div class="half_wrap" style="height:91%">
		<div id="tabWorkPlan" class="tab_content" style="width:100%">
			<iframe id="frmWorkPlan" name="frmWorkPlan" src='/webPage/tab/rfc/WorkPlanTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>								
	</div>
</div>
<!-- Tab E -->

<form name="popPam">
	<input type="hidden" name="user"/>
	<input type="hidden" name="code"/>
    <input type="hidden" name="redcd"/>
    <input type="hidden" name="isrId"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/apply/WorkPlan.js"/>"></script>
