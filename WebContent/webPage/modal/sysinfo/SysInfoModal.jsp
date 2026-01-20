<!--  
	* 화면명: 시스템속성일괄등록
	* 화면호출: 시스템정보 -> 시스템속성일괄등록 클릭
-->
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
		if (!$('#hourTxt1').is(":focus")) {
			if ($('#hourTxt1').val() != '' && $('#hourTxt1').val().length < 2) {
				$('#hourTxt1').val('0'+$('#hourTxt1').val());
			} else if ($('#hourTxt1').val() == '') {
				$('#hourTxt1').val('00');
			}
		}
		if (!$('#minTxt1').is(":focus")) {
			if ($('#minTxt1').val() != '' && $('#minTxt1').val().length < 2) {
				$('#minTxt1').val('0'+$('#minTxt1').val());
			} else if ($('#minTxt1').val() == '') {
				$('#minTxt1').val('00');
			}
		}
		
		if (!$('#hourTxt2').is(":focus")) {
			if ($('#hourTxt2').val() != '' && $('#hourTxt2').val().length < 2) {
				$('#hourTxt2').val('0'+$('#hourTxt2').val());
			} else if ($('#hourTxt2').val() == '') {
				$('#hourTxt2').val('00');
			}
		}
		if (!$('#minTxt2').is(":focus")) {
			if ($('#minTxt2').val() != '' && $('#minTxt2').val().length < 2) {
				$('#minTxt2').val('0'+$('#minTxt2').val());
			} else if ($('#minTxt2').val() == '') {
				$('#minTxt2').val('00');
			}
		}
	}
</script>

<body style="width: 100% !important; min-width: 0px !important" onmouseup="changeTimeTxt()">
<div class="pop-header">
	<div>
		<label>시스템속성 일괄등록/해제</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<div class="sm-row az_board_basic" style="height: 75%">
		<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
	
	<div class="sm-row cb" style="font-size: 0px">
		<div class="l_wrap" style="border: 1px solid #DDD;">
			<input id="optCheck" type="radio" name="infoChk" value="optCheck"/>
			<label for="optCheck">설정</label>
			<input id="optUnCheck" type="radio" name="infoChk" value="optUnCheck"/>
			<label for="optUnCheck">해제</label>
		</div>
		<div class="dib vat" style="width: calc(100% - 200px)">
			<label class="tit_80 poa margin-20-left">시스템속성</label>
			<div class="ml_90 dib width-100">
				<div id="cboSysInfo" data-ax5select="cboSysInfo" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true,selSize:5}" class="dib width-100"></div>
			</div>
		</div>
	</div>
	
	<div class="sm-row cb" style="font-size: 0px">
		<div class="width-50 float-left margin-5-bottom">
			<label class="tit_60 dib poa">중단시작</label>
			<div class="ml_60">
				<div id="divPicker" data-ax5picker="datStDate" class="dib" style="width: calc(100% - 100px)">
					<input id="datStDate" type="text" class="f-cal" autocomplete="off">
					<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
				</div>
				<!-- <input id="txtStTime" type="text" class="timepicker tit_80" autocomplete="off" maxlength="5"/> -->
				<div class='dib vat' style="font-size:0px;width:100px;">
					<div class="dib vat timePickerDiv">
				        <input class="numberTxt" type="number" id="hourTxt1" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)">
						<label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:&nbsp;</label>
						<input class="numberTxt" type="number" id="minTxt1" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)">
					</div>
				</div>
			</div>
		</div>
		<div class="width-50 float-left margin-5-bottom">
			<label class="tit_60 dib poa">중단종료</label>
 			<div class="ml_60">
				<div id="divPicker" data-ax5picker="datEdDate" class="dib" style="width: calc(100% - 100px)">
					<input id="datEdDate" type="text" class="f-cal" autocomplete="off">
					<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
				</div>
				<!-- <input id="txtEdTime" type="text" class="timepicker tit_80" autocomplete="off" maxlength="5"/> -->
				<div class='dib vat' style="font-size:0px;width:100px;">
					<div class="dib vat timePickerDiv">
				        <input class="numberTxt" type="number" id="hourTxt2" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)">
						<label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:&nbsp;</label>
						<input class="numberTxt" type="number" id="minTxt2" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)">
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="sm-row cb" style="font-size: 0px">
		<div class="width-50 dib vat">
			<label class="tit_60 dib poa">배포시간</label>
			<div class="ml_60 dib vat" style="width: calc(100% - 73px)">
				<input id="txtReleaseTime" type="text" class="width-100" autocomplete="off" />
			</div>
		</div>
		<div class="tac float-right">
			<button id="btnSearch" class="btn_basic_s margin-5-right">조회</button>
			<button id="btnReq" class="btn_basic_s">등록</button>
			<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
		</div>
	</div>
</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/SysInfoModal.js"/>"></script>