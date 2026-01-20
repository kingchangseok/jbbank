<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>프로그램 상세</title>
<c:import url="/webPage/common/common.jsp"/>

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
%>

<style>
	.timePickerDiv{
		width: 95px;
		height: 25px;
		text-align: center;
		background-color: #fff;
		border: 1px solid #ccc;
	}
	.timePickerDivDisable {
		pointer-events: none;
    	background: #eee;
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
	
	.numberTxt {
		background: inherit;
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

<body id="reqBody" style="width: 100% !important; padding: 10px;" onmouseup="changeTimeTxt()">
	<div class="content">
		<div id="history_wrap">프로그램 적용 상세<strong>&gt; 프로그램 상세 조회</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<!--좌측-->
				<div class="l_wrap width-55 vat write_wrap">
					<dl>
						<dt><label>프로그램</label></dt>
						<dd><input class="ml_10" id="txtProg" type="text" autocomplete="off" readonly="readonly" style="width: 90%;"></dd>
					</dl>
					<div class="row">
						<dl>
							<dt><label>적용구분</label></dt>
							<dd>
								<div class="width-50 dib ml_10" id="cboDeploy" data-ax5select="cboDeploy" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 45%;"></div>
								<div id="reqGbnDiv" class="dib ml_40" >
								<!--
								<div class="input-group dib" data-ax5picker="txtReqDate" style="display:inline-block;" >
							           <input id="txtReqDate" type="text" class="f-cal" placeholder="yyyy/mm/dd" style="width:90px;">
							           <span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							       </div>
							       -->
							       <div id="datDeploy" class="input-group dib" data-ax5picker="txtReqDate">
										<input id="txtReqDate" type="text" class="f-cal" placeholder="yyyy/mm/dd" style="width:90px;">
										<button id="btnReqDate" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
									</div>
							       	<div id="timeDeploy" class="dib vat timePickerDiv">
							       	<!-- style="text-align: center; background-color: #fff; width:90px; height: 25px; border: 1px; border-style: inset;" -->
							       	<!--  
									<input id="txtReqTime" type="text" class="f-cal" required="required" style="width:55px;">
									<span class="btn_calendar"><i class="fa fa-clock-o"></i></span>
									-->
										<input class="numberTxt" type="number" id="hourTxt" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)" style="width: 35px;">
										<label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:&nbsp;</label>
										<input class="numberTxt" type="number" id="minTxt" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)" style="width: 35px;">
									</div>
								</div>
							</dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<label class="tit_100 poa">신상품/공통여부</label>
							<dd><div class="ml_10" id="cboNewGoods" data-ax5select="cboNewGoods" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 90%;"></div></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>테스트대상</label></dt>
							<dd><div class="ml_10" id="cboTestYn" data-ax5select="cboTestYn" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 90%;"></div></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>중요도</label></dt>
							<dd><div class="ml_10" id="cboImportance" data-ax5select="cboImportance" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 90%;"></div></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>거래코드</label></dt>
							<dd><input class="ml_10" id="txtInputCode" name="txtInputCode" type="text" style="width:90%;"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label></label></dt>
							<dd>
								<div id="reqGbnDiv2" class="por dib ml_30" >
									<input type="checkbox" id="chkComp" class="checkbox-detail" data-label="특정일시컴파일"/>
									<div class="dib vat">
										<div id="datDeploy2" class="input-group dib margin-5-right" data-ax5picker="txtReqDate2">
											<input id="txtReqDate2" type="text" class="f-cal" placeholder="yyyy/mm/dd" style="width:90px;">
											<button id="btnReqDate2" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
										</div>
								       	<div id="timeDeploy2" class="dib vat timePickerDiv">
											<input class="numberTxt" type="number" id="hourTxt2" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)" style="width: 35px;">
											<label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:&nbsp;</label>
											<input class="numberTxt" type="number" id="minTxt2" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)" style="width: 35px;">
										</div>
									</div>
								</div>
							</dd>
						</dl>
					</div> 
				</div>
				<!--우측-->
				<div class="r_wrap width-45 vat write_wrap">
					<div class="row">
						<dl>
							<dt><label>변경사유</label></dt>
							<dd>
								<form>
									<textarea id="txtSayu" style="align-content:flex-start;width:100%;height:180px;resize: none; overflow-y:auto; padding:5px;" readonly="readonly"></textarea>
								</form>
							</dd>
						</dl>
					</div>
				</div>
		</div>
	</div>
	<div class="l_wrap width-49">
		<div class="row">
			<div class="float-right">
				<button id="btnOrderAdd" name="btnOrderAdd" class="btn_basic_s" disabled="disabled">업무지시서 추가/제거</button>
				<button id="btnView1" name="btnView1" class="btn_basic_s margin-5-left">업무지시서보기</button>
			</div>
		</div>
		<div class="row">
			<div class="az_board_basic scroll_h az_board_basic_in" style="height: 50%; margin-top: 10px;">
			 	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="r_wrap width-49">
		<div class="row">
			<div class="float-right">
				<button id="btnView2" name="btnView2" class="btn_basic_s" disabled="disabled">개발요청서보기</button>
			</div>
		</div>
		<div class="row">
			<div class="az_board_basic scroll_h az_board_basic_in" style="height: 50%; margin-top: 10px;">
			 	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<div class="float-right margin-10-top">
		<button id="btnSrApi" name="btnSrApi" class="btn_basic_s margin-5-left">SR요청서보기</button>
		<button id="btnReWrite" name="btnReWrite" class="btn_basic_s" disabled="disabled">수정</button>
		<button id="btnClose" name="btnClose" class="btn_basic_s margin-5-left">닫기</button>
	</div>
</div>
</body>

<form name="getReqData">
	<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="acptno" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" id="itemid" name="itemid" value="<%=itemId%>"/>
</form>

<form name="setReqData" accept-charset="utf-8">
	<input type="hidden" name="user"/>
	<input type="hidden" name="orderId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopProgDetail.js"/>"></script>