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
	<div id="history_wrap"></div> 
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="row">	
                <div class="dib vat" style="width:calc(100% - 210px);">
                    <label class="tit_60 poa">*시스템</label>
                    <div class="ml_60 vat">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib margin-5-right" style="width: 450px"></div>
						<input type="checkbox" class='checkbox-pie' id="chkEmg" style="width:50px;" data-label="긴급"></input>
						<label class="tit_50 ml_10">개발기간</label>
						<input class="numberTxt" type="number" id="txtDevTime" style="width: 50px; height: 25px; border: 1px solid #ddd" value="0" min="0"><label class="dib vat">일</label>
					</div>
				</div>
				<div class="dib vat float-right" style="width:210px;">
                    <button id="btnExcel" class="btn_basic_s" style="width:100px;">엑셀파일</button>
                    <button id="btnTemp" class="btn_basic_s margin-5-left" style="width:100px;">엑셀템플릿</button>
				</div>
			</div>
		</div>
	</div>
		
    <div class="az_board_basic az_board_basic_in" style="height:30%" id="grdLst1">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%;" class="resize"></div>
	</div>	
		
	<div style="height:20%; min-height: 195px" class="margin-5-top margin-5-bottom">
		<div class="az_search_wrap" style="height: 100%">
			<div class="l_wrap width-60">
				<div class="margin-10-left">
					<label>변경사유</label>
					<div class="sm-row" style="height:calc(100% - 40px);">
						<textarea id="txtSayu" name="txtSayu" class="width-100" rows="6" cols="75" style="padding:5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto; height:100%;"></textarea>
					</div>
				</div>
			</div>
			<div class="r_wrap width-40">
				<div class="dib margin-10-left margin-5-right" style="width: calc(100% - 120px)">
					<div style="display:none;" id="fileSave"></div>
					<button id="btnFileAdd" class="btn_basic_s tit_80">파일첨부</button>
					<button id="btnFileDel" class="btn_basic_s tit_80">파일삭제</button>
					<div class="sm-row" style="height:calc(100% - 40px);">
						<div class="az_board_basic az_board_basic_in" style="height:100%; min-height: 131px;">
						   	<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="fileGrid" style="height:100%"></div>
						</div>
					</div>
				</div>
				<div class="dib poa" style="width: 90px; height: 100%; margin-top: 138px">
					<div class="vat float-right">
						<button id="btnAdd" class="btn_basic_s" style="width:40px;">▼</button>
						<button id="btnDel" class="btn_basic_s" style="width:40px;">▲</button>
					</div>
				</div>
			</div>
		</div>
	</div>
		
    <div class="az_board_basic az_board_basic_in" style="height:50%" id="grdLst2">
    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%" class="resize"></div>
	</div>
	
	<div class="row">
		<div class="r_wrap" style="width: 90px;">
			<div class="vat float-right">
				<button id="cmdReq" class="btn_basic_s tit_80" disabled>적용요청</button>
			</div>
		</div>
	</div>
</div>

<form name="popPam" accept-charset="utf-8">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="orderId"/>
</form>

<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/apply/ApplyHomePage.js"/>"></script>