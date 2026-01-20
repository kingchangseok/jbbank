<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />
<!-- 검색 S-->
<style type="text/css">
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

input:read-only{
  background-color: #fff;
}
</style>

<div class="az_search_wrap">
	<div class="az_in_wrap sr_status">
		<div class="row">
			<div class="l_wrap dib vat" style="width:100%;">
				<div class="dib vat width-25">
					<label class="dib poa" id="lblISRId">&nbsp;&nbsp;&nbsp;ISR ID</label>
					<div class="ml_100">
						<input id="txtISRId" type="text" class="width-100" readonly="readonly">
					</div>
				</div>
				<div class="dib vat width-75">
					<label class="dib poa" id="lblTitle">&nbsp;&nbsp;&nbsp;요청제목</label>
					<div class="ml_100">
						<input id="txtTitle" type="text" class="width-100" readonly="readonly">
					</div>
				</div>				
			</div>
		</div>
		<div class="row">
			<div class="l_wrap dib vat" style="width:100%">
				<div class="dib vat width-25">
					<label class="dib poa" id="lblReqDT">&nbsp;&nbsp;&nbsp;요청등록일</label>
					<div class="ml_100">
						<input id="txtReqDT" type="text" class="width-100" readonly="readonly">
					</div>
				</div>
				<div class="dib vat width-25">
					<label class="dib poa" id="lblReqDept">&nbsp;&nbsp;&nbsp;요청부서</label>
					<div class="ml_100">
						<input id="txtReqDept" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
				<div class="dib vat width-25">
					<label class="dib poa" id="lblEditor">&nbsp;&nbsp;&nbsp;요청인</label>
					<div class="ml_100">
						<input id="txtEditor" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
			</div>
		</div>
		<div class="row">
			<div class="l_wrap dib vat" style="width:100%">
				<div class="dib vat width-25">
					<label class="dib poa" id="lblStatus">&nbsp;&nbsp;&nbsp;진행단계</label>
					<div class="ml_100">
						<input id="txtStatus" type="text" class="width-100" readonly="readonly">
					</div>
				</div>
				<div class="dib vat width-25">
					<label class="dib poa" id="lblProStatus">&nbsp;&nbsp;&nbsp;상세진행현황</label>
					<div class="ml_100">
						<input id="txtProStatus" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
				<div class="dib vat width-25">
					<label class="dib poa" id="lblComEdDt">&nbsp;&nbsp;&nbsp;완료요청일</label>
					<div class="ml_100">
						<input id="txtComEdDt" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
			</div>
		</div>		
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"
	src="<c:url value="/js/ecams/sr/baseISRInfo.js"/>"></script>