<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />
<!-- 검색 S-->
<style type="text/css">
	/* .row:after {
		content:""; 
		display:block; 
		clear:both;
	} */
	
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

<div class="az_search_wrap">
	<div class="az_in_wrap sr_status">
		<div class="row">
			<div class="l_wrap dib vat" style="width:calc(100% - 565px);">
				<div class="dib vat" style="width:20%;">
					<label class="margin-10-left tit_60 poa">요청부서</label>
					<div class="ml_60">
						<div id="cboReqDept" data-ax5select="cboReqDept" data-ax5select-config="{size:'sm',theme:'primary',selecteditable:true,selSize:8}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
				</div>
				<div class="dib vat margin-10-left" style="width:20%;">
					<label class="margin-10-left tit_60 poa">분류유형</label>
					<div class="ml_60">
						<div id="cboCatType" data-ax5select="cboCatType" data-ax5select-config="{size:'sm',theme:'primary',selecteditable:true}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
				</div>
				<div class="dib vat margin-10-left" style="width:20%;">
					<label class="margin-10-left tit_60 poa">대상구분</label>
					<div class="ml_60">
						<div id="cboQryGbn" data-ax5select="cboQryGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable:true}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
				</div>
				<div class="dib vat margin-10-left" style="width:30%;" id="divQry">
					<label class="margin-10-left tit_60 poa">검색조건</label>
					<div class="ml_60 vat">
						<div id="cboQry" data-ax5select="cboQry" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-30"></div>
						<input id="txtQry" type="text" class="width-70"/>
					</div>
				</div>
			</div>
			<div class="r_wrap dib vat" style="width:565px; text-align: left;" id="acptDate">
				<div class="dib" style="float:left;">
					<label class="tit_40 poa">등록일</label>
					<div class="ml_40 dib">
						<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
							<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							<span class="sim">∼</span>
							<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
					</div>
				</div>
				<div class="vat dib float-right">
					<button id="btnQry" name="btnQry" class="btn_basic_s" style="width: 70px;">조회</button>
					<button id="btnReset" name="btnClear" class="btn_basic_s margin-5-left" style="width: 70px;">초기화</button>
					<button id="btnReset" name="btnExcel" class="btn_basic_s margin-5-left" style="width: 70px;">엑셀저장</button>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="az_board_basic" style="height: calc(100% - 60px);">
	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/PrjListTab.js"/>"></script>