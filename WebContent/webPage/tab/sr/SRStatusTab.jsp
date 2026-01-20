<!--  
	* 화면명: 개발계획/실적 등록
	* 화면호출:
	 1) 개발계획/실적 -> 개발계획/실적 탭
	 2) 체크아웃, 체크인 등 SR정보 버튼 클릭 -> 개발계획/실적 탭 
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>
<style>
[data-ax5select] .ax5select-display.input-sm, [data-ax5select] .input-group-sm>.ax5select-display.form-control, [data-ax5select] .input-group-sm>.ax5select-display.input-group-addon, [data-ax5select] .input-group-sm>.input-group-btn>.ax5select-display.btn
{
	min-width : 0px !important;
}

.timeBox{
	padding: 6px;
    border: 1px solid #3473c8;
}

.write_wrap dl dd input[type="radio"]{
	width: 15px;
	height: 15px;
}

#opt.disabled {
	background : #ebebe4;
}

#opt.disabled input[type="radio"] {
	cursor: not-allowed;
	border: 1px solid #ddd;
    border-radius: 0px;
    box-shadow: none;
    opacity: 0.5;
}
</style>
<!-- contener S -->
<body style="padding: 10px !important; width: 100% !important; min-width: 0px !important;">
    <!-- <div class="content"> -->
		<!-- 하단 S-->
	<div class="l_wrap width-100 vat write_wrap" style="-ms-overflow-y: hidden;"><!--ver2-->
		<div class="row">
			<dl>
				<dt><label>SR-ID</label></dt>
				<dd>
					<div style="width:30%;" class="dib vat">
						<input id="txtSRID" type="text" style="width: 100%;" disabled="disabled">
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<dl>
				<dt><label>SR제목</label></dt>
				<dd>
					<div style="width:100%;" class="dib vat">
						<input id="txtSRTitle" type="text" style="width: 100%;" disabled="disabled">
					</div>
				</dd>
			</dl>
		</div>
		<div class="row" style="overflow: hidden;">
			<dl>
				<dt><label>이관신청내역</label></dt>
				<dd>
					<div class="row float-right">
						<button id="btnSaveExcel1" class="btn_basic_s" disabled=true>엑셀저장</button>
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<div class="az_board_basic scroll_h az_board_basic_in" style="height: 114px;">
		    	<div data-ax5grid="grdList1" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		    </div>
		</div>
		<div class="row" style="overflow: hidden;">
			<dl>
				<dt style="width: 140px;"><label>이관신청프로그램목록</label></dt>
				<dd style="margin-left: 140px;">
					<div id="rbgResource" style="height: 25px; border: 1px solid #ddd; margin-left: 10px; width: 175px;" class="dib vat">
						<input id="rb1" name="rbgResource"  type="radio" value="N" style="height: 100%;" checked="checked"/>
						<label for="rb1" style="line-height: 20px;">전체</label>
						<input id="rb2" name="rbgResource" type="radio" value="T" style="height: 100%;"/>
						<label for="rb2" style="line-height: 20px;">이관신청건별</label>
					</div>
					<div class="float-right vat">
						<button id="btnSaveExcel2" class="btn_basic_s" disabled=true>엑셀저장</button>
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<div class="az_board_basic scroll_h az_board_basic_in" style="height: 119px;">
		    	<div data-ax5grid="grdList2" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		    </div>
		</div>
	</div>
</body>
	
<form name="popPam">
	<input type="hidden" name="acptno" />
	<input type="hidden" name="user" />
</form>
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/SRStatusTab.js"/>"></script>