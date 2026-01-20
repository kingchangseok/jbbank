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
</style>

<div class="az_search_wrap" style="height:30%">
	<div class="az_in_wrap sr_status" style="height:100%">
		<div class="row por" style="height:10%">
			<div class="l_wrap dib vat" style="width:calc(50% + 10px);">
				<div class="dib vat" id="sys">
					<label id="lblTit" style="margin-right: 17px;">대상목록</label>
				</div>			
				<div class="dib width-40"></div>
				<div class="dib width-40" id="status">
					<label id="lblQryGbn" style="margin-right: 28px;">대상구분</label>
					
					<div id="cboQryGbn" data-ax5select="cboQryGbn"
							data-ax5select-config="{
							size:'sm',
							theme:'primary'
							}" 
							class="width-15 dib" style="width: calc(100% - 99px);"></div>
				</div>
			</div>
			<div class="dib r_wrap">		
				<div class="dib vat">
					<label id="lblDay">등록일</label>
				</div>
				<div class="dib">
					<div id="divPicker" class="az_input_group dib" data-ax5picker="topDate">
						<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
						<span id="cal1" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						<label id="lblFrom" style="margin: 0 7px">부터</label>
						<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
						<span id="cal2" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						<label id="lblTo" style="margin: 0 7px">까지</label>
					</div>
				</div>
				<div class="dib vat">
					<div class="vat dib margin-5-left">
						<button class="btn_basic_s" id="btnQry" style="margin-left: 0px; margin-right: 0px;">조회</button>
					</div>
				</div>
			</div>		
		</div>
		<div class="row por" style="height:80%">
			<div class="az_board_basic" style="height: 100%;">
			<div data-ax5grid="grdLst"
				data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"
				style="height: 100%;"></div>
			</div>	
		</div>
	</div>
</div>
<!-- 검색 E-->

<!-- basieISRInfo S-->
<div class="half_wrap" style="height:14%;">
		<iframe id="frmBaseISRInfo" name="frmBaseISRInfo" src='/webPage/sr/baseISRInfo.jsp' width='100%' height='100%' frameborder="0"></iframe>
</div>
<!-- basieISRInfo E -->

<!-- Tab S -->
<div id="srTab" class="half_wrap" style="height:54%; margin-top:10px;">
	<div class="tab_wrap">
		<ul class="tabs">
			<li rel="tabSRRegister" id="tab1">ISR요청정보</li>
			<li rel="tabSRAccept" id="tab2">ISR접수</li>
			<li rel="tabRFCRegister" id="tab3">RFC발행</li>
			<li rel="tabSRComplete" id="tab4">ISR종료</li>
			<li rel="tabSRCompleteByUser" id="tab5" class="on">요청자종료</li>
			<div class="r_wrap" style="margin-left:10px;">
				<button class="btn_basic_s" id="btnChgInfo" style="margin-left: 0px;margin-right: 0px;">변경관리정보</button>
				<button class="btn_basic_s" id="btnTestInfo" style="margin-left: 0px;margin-right: 0px;">테스트관리정보</button>
			</div>	
			<div class="dib width-20 r_wrap">
				<label class="dib poa" id="cboPart">&nbsp;&nbsp;수신파트</label>
				<div class="ml_80">
					<div id="cboPart" data-ax5select="cboPart"
							data-ax5select-config="{
							size:'sm',
							theme:'primary'
							}">
					</div>
				</div>
			</div>														
		</ul>
	</div>

	<div class="half_wrap" style="height:91%">
		<div id="tabSRRegister" class="tab_content" style="width:100%">
			<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>
		<div id="tabSRAccept" class="tab_content" style="width:100%">
			<iframe id="frmSRAccept" name="frmSRAccept" src='/webPage/tab/sr/SRAcceptTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>	
		<div id="tabRFCRegister" class="tab_content" style="width:100%">
			<iframe id="frmRFCRegister" name="frmRFCRegister" src='/webPage/tab/rfc/RFCRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>	
		<div id="tabSRComplete" class="tab_content" style="width:100%">
			<iframe id="frmSRComplete" name="frmSRComplete" src='/webPage/tab/sr/SRCompleteTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		</div>
		<div id="tabSRCompleteByUser" class="tab_content" style="width:100%">
			<iframe id="frmSRCompleteByUser" name="frmSRCompleteByUser" src='/webPage/tab/sr/SRCompleteByUserTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
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
<script type="text/javascript"
	src="<c:url value="/js/ecams/sr/SRCompleteByUser.js"/>"></script>
