<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.timePickerDiv{
	width: 95px;
	height: 25px;
	text-align: center;
	background-color: #fff;
	border: 1px solid #ccc;
}
.timePickerDiv:hover, .timePickerDiv:focus{
	width: 95px;
	height: 25px;
	text-align: center;
	background-color: #fff;
	border: 1px solid #326aab;
}
.numberTxt, .numberTxt:hover, .numberTxt:focus {
	padding: 0px;
	margin: 0px;
	width: 35px;
	height: 22px;
	vertical-align: middle;
    text-align: right;
    border: 0px;
}
input[type="number"] {
    position: relative;
}
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { 
      /*-webkit-appearance: none;*/
      opacity: .5;
      background-color: #fff;
}
</style>

<script type="text/javascript">
	function maxLengthCheck(object) {
		if (object.value.length > object.maxLength){
			object.value = object.value.slice(0, object.maxLength);
		}
		if (object.value < Number(object.min) || object.value > Number(object.max)) {
			object.value = '';
		}
	}
	//커서 아웃될때
	function changeTimeTxt() {
		if (!$('#hourTxt').is(":focus")) {
			if ($('#hourTxt').val() != '' && $('#hourTxt').val().length < 2) {
				$('#hourTxt').val('0'+$('#hourTxt').val());
			} else if ($('#hourTxt').val() == '') {
				$('#hourTxt').val('00');
			}
		}
		if (!$('#minTxt').is(":focus")) {
			if ($('#minTxt').val() != '' && $('#minTxt').val().length < 2) {
				$('#minTxt').val('0'+$('#minTxt').val());
			} else if ($('#minTxt').val() == '') {
				$('#minTxt').val('00');
			}
		}
	}
</script>
<div class="contentFrame">
 <!-- history S-->
        <div id="history_wrap"></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
                <div class="row por">
                	<div class="tit_60 poa">
                    	<label>*SR-ID</label>
                    </div>
                    <div class="ml_60 tal">
						<div id="cboSrId" data-ax5select="cboSrId" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc( 100% - 80px);"></div>						
					</div>
					<div class="vat poa_r">
						<button id="btnSR" class="btn_basic_s margin-5-left" disabled=true style="width:70px;">SR정보</button>
					</div>
				</div>
				<div class="row por">	
					<!-- 시스템 -->		
	                <div class="width-33 dib vat">
	                    <label id="lbSystem" class="tit_60 poa">*시스템</label>
	                    <div class="ml_60 vat">
							<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 10px);" class="dib width-100"></div>
						</div>
					</div>
				    <!-- 프로그램종류 -->
	                <div class="width-33 dib vat">
	                    <label id="lbJawon" class="tit_100 poa" style="text-align:center;">프로그램유형</label>
	                    <div class="ml_100">
							<div id="cboRsrccd" data-ax5select="cboRsrccd" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 10px);" class="dib"></div>
						</div>
					</div>						
					<div class="width-34 dib vat" id="cboReqDiv" >
	                    <label id="lblProg" class="tit_120 poa" style="text-align:center;">*프로그램명/설명</label>
	                    <div class="ml_120 tal">
							<input id="txtRsrcName" name="txtRsrcName" type="text" style="width: calc(100% - 80px);"></input>
						</div>						
						<div class="vat poa_r" id="searchBox">
							 <button id="btnFind" class="btn_basic_s" style="width:70px;">검색</button>
						</div>
					</div>
				</div>	
			</div>
		</div>
		<!--검색E-->
	    <!-- 게시판 S-->
		<div>
		    <div class="az_board_basic az_board_basic_in" style="height:36%" id="grdLst1">
		    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%;"></div>
			</div>	
			<!-- 게시판 E -->
			<div class="por margin-5-top margin-5-bottom">
				<div class=""><input type="checkbox" class="checkbox-pie" id="chkDetail" data-label="항목상세보기" checked></input></div>
				<div class="poa_r">
					<div class="vat dib">
						<button id="btnAdd" class="btn_basic_s">추가</button>
					</div>
					<div class="vat dib">
						<button id="btnDel" class="btn_basic_s">제거</button>
					</div>
				</div>
			</div>
		</div>
	
		    <!-- 게시판 S-->
		<div>
		    <div class="az_board_basic az_board_basic_in" style="height:38%" id="grdLst2">
		    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
			</div>
		</div>	
	    
	    <div class="row" id="sayuBox" >
			<!-- 요청부서 -->
			<div class="tit_60 poa">
				<label>*신청사유</label>
			</div>
			<div class="ml_60" id="sayuInputBox" style="width:100%;">
                  <input id="txtSayu" name="txtSayu" type="text" class="width-100" style="align-content:left;" ></input>
			</div>
	    </div>
		<div class="row floatRow">
			<div class="width-30 dib float-left">
				<div class="tit_60 poa">
					<label id="lblReqGbn">*처리구분</label>
				</div>
				<div class="ml_60">
					<div id="cboReqGbn" class="width-100" data-ax5select="cboReqGbn" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
				</div>
	
			</div>
			<div class="width-37 dib float-left" id="panCal" style="display:none;; margin-left:50px;">
				<div class="tit_80 poa">
					<label>*적용일시</label>
				</div>
				<div class="ml_80 dib">
					<div class="input-group width-43 dib vat" data-ax5picker="txtReqDate"style=" font-size:0px;" >
			            <input id="txtReqDate" type="text" class="f-cal" placeholder="yyyy/mm/dd">
			            <button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
			        </div>
			        <div style="font-size:0px; " class='width-40 dib vat'>
						<div class="dib vat timePickerDiv">
					        <input class="numberTxt" type="number" id="hourTxt" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)">
							<label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:&nbsp;</label>
							<input class="numberTxt" type="number" id="minTxt" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)">
						</div>
					</div>
				</div>
			</div>
   			<div class='ml150 tal float-right' style="display:inline;">
				<div style="display:inline-block;">
	   				<input type="checkbox" class="checkbox-pie" id="chkBefJob" data-label="선행작업" style="display:none;"></input>
				</div>
				<button id="btnRequest" class="btn_basic_s" disabled=true>체크인신청</button>
			</div>
		</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="srid"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/apply/CloseRequest.js"/>"></script>