<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<%
	request.setCharacterEncoding("UTF-8");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String winSw = StringHelper.evl(request.getParameter("winSw"),"");
%>

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
<div class="contentFrame" id="divFrame">
	<div id="history_wrap">적용 <strong>&gt; 적용요청등록</strong></div> 
	<div class="az_search_wrap" style="height: 77px">
		<div class="az_in_wrap">
			<div class="row">	
                <div class="dib vat" style="width:505px;margin-right:5px;">
                    <label class="tit_60 poa">*시스템</label>
                    <div class="ml_60 vat">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
					</div>
				</div>
				<div class="dib vat" style="width:505px;margin-right:5px;">
                    <label class="tit_80 poa">*업무</label>
                    <div class="ml_80 vat">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
					</div>
				</div>
				<div class="dib vat float-right" style="width:calc(70% - 480px);">
                    <label class="tit_80 poa">프로그램</label>
                    <div class="ml_80 vat">
						<input id="txtProg" name="txtProg" type="text" class="dib margin-5-right" style="width:calc(100% - 323px);" placeholder="프로그램명을 입력하세요."></input>
						<input type="checkbox" class='checkbox-pie' id="chkUpLow" style="width:90px;" data-label="대소문자구분"></input>
						<input type="checkbox" class='checkbox-pie' id="chkLike" style="width:60px;" data-label="Like검색" checked></input>
						<input type="checkbox" class='checkbox-pie' id="chkSelf" style="width:60px;" data-label="본인건" checked></input>
						<button id="btnFind" class="btn_basic_s margin-5-left" style="width:80px;">검색</button>
                    </div>
				</div>
			</div>
			
			<div class="row">	
                <div class="dib vat" style="width:505px;margin-right:5px;">
                    <label class="tit_60 poa">*신청구분</label>
                    <div class="ml_60 vat">
						<div id="cboReq" data-ax5select="cboReq" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
					</div>
				</div>
				<div class="dib vat float-right" style="width:calc(100% - 515px);">
                    <label class="tit_80 poa">주관부서결재</label>
                    <div class="ml_80 vat">
						<div id="cboGyulCheck" data-ax5select="cboGyulCheck" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib tit_100"></div>
						
						<label class="tit_50 ml_10">개발기간</label>	
						<input class="numberTxt" type="number" id="txtDevTime" style="width: 50px; height: 25px; border: 1px solid #ddd" value="0" min="0"><label class="dib vat">일</label>
						
						<label class="tit_60 ml_10">적용근거</label>
						<div class="dib vat">
							<div id="cboReqSayu" data-ax5select="cboReqSayu" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib" style="width: 300px"></div>
						</div>
						
						<div class="dib float-right">
							<button id="btnExcel" class="btn_basic_s margin-5-left" style="width:80px;">엑셀저장</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
		
	
    <div class="az_board_basic az_board_basic_in" style="height:30%" id="grdLst1">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%;" class="resize"></div>
	</div>	
		
	<div style="height:20%; min-height: 195px" class="margin-5-top margin-5-bottom">
		<div class="az_search_wrap" style="height: 100%">
			<div class="l_wrap width-70">
				<div class="l_wrap width-50">
					<div class="margin-10-left">
						<label id="txtOrderNum">발행번호</label>
						<div class="sm-row" style="height:calc(100% - 40px);">
							<div class="az_board_basic az_board_basic_in" style="height:100%; min-height: 131px;">
							   	<div data-ax5grid="thirdGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="thirdGrid" style="height:100%"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="r_wrap width-50">
					<div class="margin-10-left">
						<label>변경사유</label><label id="lblSayu" style="font-weight: normal; float: right;">총 (0자)</label>
						<div class="sm-row">
							<textarea id="txtSayu" name="txtSayu" class="width-100" rows="6" cols="75" style="padding:5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;'" placeholder="변경사유를 공백제외 10자이상 입력하여 주십시오."></textarea>
						</div>
						<div class="sm-row">
							<label class="tit_60 poa">거래코드</label>
                    		<div class="ml_60 vat">
								<input id="txtCode" name="txtCode" type="text" class="dib width-100" placeholder="거래코드를 입력하세요(최대10자)." maxlength="10"></input>
							</div>	
						</div>
					</div>
				</div>
			</div>
			<div class="r_wrap width-30">
				<div class="margin-10-left margin-10-right">
					<label class="tit_100 poa">적용구분</label>
                    <div class="ml_100 vat">
						<div id="cboDeploy" data-ax5select="cboDeploy" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width: calc(100% - 238px)"></div>
						
						<div class="dib vat tar" id="divApplyDate" style="visibility: hidden;">
							<div class="input-group dib vat" data-ax5picker="basic2"style=" font-size:0px;width:130px" >
					            <input id="txtReqDate" type="text" class="f-cal" placeholder="yyyy/mm/dd">
					            <button id="btnReqD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					        </div>
					        <div class='dib vat' style="font-size:0px;width:100px;">
								<div class="dib vat timePickerDiv">
							        <input class="numberTxt" type="number" id="hourTxt" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)">
									<label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:&nbsp;</label>
									<input class="numberTxt" type="number" id="minTxt" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<label class="tit_100 poa">테스트대상</label>
	                    <div class="ml_100 vat">
							<div id="cboTestYN" data-ax5select="cboTestYN" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-100"></div>
						</div>
					</div>
					<div class="row">
						<label class="tit_100 poa">신상품/공통여부</label>
	                    <div class="ml_100 vat">
							<div id="cboNewGoods" data-ax5select="cboNewGoods" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-100"></div>
						</div>
					</div>
					<div class="row">
						<label class="tit_100 poa">중요도</label>
	                    <div class="ml_100 vat">
							<div id="cboImportant" data-ax5select="cboImportant" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-100"></div>
						</div>
					</div>
					
					<div class="row" style="display: none;">
						<div class="vat float-right">
							<input type="checkbox" class='checkbox-pie' id="chkComp" style="width:90px;" data-label="특정일시컴파일"></input>
							<div class="input-group dib vat" data-ax5picker="basic" style=" font-size:0px;width:130px" >
					            <input id="txtCompDate" type="text" class="f-cal" placeholder="yyyy/mm/dd">
					            <button id="btnDate" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					        </div>
					        <div class="dib vat" style="width:100px;">
								<input id="txtTime" name="txtTime" type="text" maxlength="4" class="timepicker dib width-100"></input>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="vat float-right">
							<button id="btnLoadExl" class="btn_basic_s">엑셀 업로드</button>
							<button id="btnExlTmp" class="btn_basic_s">엑셀템플릿</button>
							<button id="btnAdd" class="btn_basic_s" style="width:40px;">▼</button>
							<button id="btnDel" class="btn_basic_s" style="width:40px;">▲</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
		
    <div class="az_board_basic az_board_basic_in" style="height:calc(50% - 240px)" id="grdLst2">
    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%" class="resize"></div>
	</div>
	
	<div class="row" style="height: 100px">
		<div class="l_wrap" style="width: calc(100% - 90px);">
			<div class="width-100 float-left dib az_search_wrap" id="divTest" style="display: none;">
				<div class="float-left dib width-100">
					<label class="tit_50 poa margin-5-left">Test일자</label>
                    <div class="ml_60">
                    	<div class="input-group dib vat" data-ax5picker="basic3" style=" font-size:0px;width:130px" >
				            <input id="txtTestDate" type="text" class="f-cal" placeholder="yyyy/mm/dd">
				            <button id="btnTestDate" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
				        </div>
				        <label class="tit_70 poa tac">신규 및 변경 요구사항</label>
				        <div class="ml_80 dib" style="width: calc(100% - 220px)">
				        	<textarea id="txtTestSayu" name="txtTestSayu" class="width-100" rows="2" cols="75" style="padding:5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;'"></textarea>
				        </div>
					</div>
				</div>
			</div>
		</div>
		<div class="r_wrap" style="width: 90px;">
			<div class="vat float-right">
				<button id="cmdReq" class="btn_basic_s tit_80" disabled>적용요청</button>
			</div>
		</div>
	</div>
</div>

<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
<input type="hidden" id="acptNo" name="acptNo" value="<%=acptNo%>"/>
<input type="hidden" id="winSw" name="winSw" value="<%=winSw%>"/>

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
<script type="text/javascript"	src="<c:url value="/js/ecams/apply/ApplyRequest.js"/>"></script>