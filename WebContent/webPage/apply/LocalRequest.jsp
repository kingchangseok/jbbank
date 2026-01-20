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

<body>
<div class="contentFrame">
	 <div id="history_wrap"></div>
	 
	 <div class="az_search_wrap">
	 	<div class="az_in_wrap">
	 		<div class="row por">
               	<div class="dib" style="width: 400px">
                    <label class="tit_60 poa">*시스템</label>
                    <div class="ml_60 vat">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:calc(100% - 10px);" class="dib"></div>
					</div>
               	</div>
				<div class="dib" style="width: calc(100% - 410px)">
					<input id="txtLocalDir" name="txtLocalDir" type="text" class="width-100" readonly></input>
				</div>
			</div>
	 	</div>
	 </div>
	 
	 <div class="row" style="margin:0px;">
		<div class="dib vat" style="width: 400px">
			<label class="tit_60 poa">*신청구분</label>
			<div class="ml_60 vat">
				<div id="cboReq" data-ax5select="cboReq" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%" class="dib"></div>
			</div>
		</div>
		<div class="dib vat" style="width: calc(100% - 410px)">
			<label class="fontStyle-ing" id="lblLocalPath">선택한 폴더/하위디렉토리 파일추출->비교->동기화대상 목록추가->실행(형상관리에 등록되지 않은 로컬신규파일은 동기화대상제외)</label>
		</div>
	</div>
	 
	 <div class="row" style="height: 73%; position: relative;">
	 	<!-- left -->
	 	<div class="l_wrap padding-5-right" style="width: 405px; height: 100%;" id="divTree">
		 	<div class="row">
			 	<div style="overflow-y: auto; height:calc(100% - 30px); background-color: white; border: 1px solid #ddd;">
					<ul id="treeDir" class="ztree"></ul>				
				</div>
		 	</div>
		</div>
		
		<!-- right -->
		<div class="r_wrap" style="width: calc(100% - 410px); height: 100%;" id="divGrid">
			<div class="row">
				<div class="dib vat" style="width: calc(100% - 245px);">
					<label class="tit_60 poa">검색단어</label>
					<div class="ml_60 tal dib vat" style="width: calc(100% - 370px);">
						<input id="txtRsrcName" name="txtRsrcName" type="text" class="width-100" placeholder="추출할 프로그램명/확장자 입력 (구분자 콤마 ',')"></input>
					</div>
					<div class="vat dib">
						 <button id="btnSearch" class="btn_basic_s">조회</button>
						 <button id="btnDiff_top" class="btn_basic_s">비교</button>
						 <input type="checkbox" class='checkbox-pie' id='chkOk' data-label="미대상제외">
					</div>
				</div>
				<div class="dib vat float-right" style="width: 228px">
					<button class="btn_basic_s float-right margin-5-left" id="btnExl2">일괄등록양식으로저장</button>
					<button class="btn_basic_s float-right" id="btnExl">엑셀저장</button>
				</div>	
			</div>
			<div class="row">
				<div class="az_board_basic" style="height:calc(50% - 35px);">
			    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
				</div>
				<div class="por width-100 margin-5-top">
					<div>
						<label>▼비교/신청 대상목록</label>
					</div>
					<div class="poa_r">
						<div class="vat dib">
							<button id="btnAdd" class="btn_basic_s" style="width:70px;">추가</button>
						</div>
						<div class="vat dib">
							<button id="btnDel" class="btn_basic_s" style="width:70px;">제거</button>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="az_board_basic" style="height:calc(50% - 60px);">
			    	<div data-ax5grid="second-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40, showRowSelector:true}" style="height:100%"></div>
				</div>
			</div>
		</div>
	 </div>
	 
	 <div class="margin-10-top">
		 <div class="az_search_wrap">
		 	<div class="az_in_wrap" id="divReq1"> <!-- 동기화,신규등록 -->
		 		<div class="row">
					<div class="dib vat width-50">
						<label class="tit_80 dib poa">프로그램종류</label>
						<div class="ml_80">
							<div id="cboJawon" data-ax5select="cboJawon" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-30 dib tal"></div>
						    <input id="txtExeName" name="txtExeName" type="text" readonly="readonly" disabled="disabled" style="width:calc(70% - 5px);">
						</div>
					</div>
					<div class="dib vat width-50">
						<label class="tit_80 dib poa">프로그램업무</label>
						<div class="ml_80">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="div vat width-100">
						<label class="tit_80 poa">프로그램설명</label>
		                <div class="ml_80 vat">
							<input id="txtStory" name="txtStory" type="text" class="width-100"></input>
						</div>
					</div>
				</div>
				<div class="row text-right">
					<div class="dib vat">
						<button class="btn_basic_s" id="btnDiff_bottom">비교</button>
						<button class="btn_basic_s margin-5-left" id="btnCmd">실행</button>
					</div>	
				</div>
		 	</div>
		 	
		 	<div class="az_in_wrap" id="divReq2"> <!-- 신청 -->
		 		<div class="row">
	                <label class="tit_80 poa">신청사유</label>
	                <div class="ml_80 vat">
						<input id="txtSayu" name="txtSayu" type="text" class="width-100"></input>
					</div>
				</div>
				<div class="row">
					<div class="dib" style="width: calc(100% - 250px)">
						<label class="tit_80 poa">관련문서번호</label>
		                <div class="ml_80 vat">
							<input id="txtDocNo" name="txtDocNo" type="text" class="width-100"></input>
						</div>
					</div>
					<div class="dib vat text-right" style="width: 250px">
						<button id="btnPMS" class="btn_basic_s margin-10-right">PMS검색</button>
						<input type="checkbox" class='checkbox-pie' id='chkBef' data-label="선행작업">
						<input type="checkbox" class='checkbox-pie fontStyle-cncl' id='chkEmg' data-label="긴급체크인">
					</div>
				</div>
				<div class="row por">
					<div class="dib vat" style="width: calc(100% - 370px);">
						<label class="tit_80 poa">적용구분</label>
						<div class="ml_80 tal dib vat" style="width: 200px">
							<div id="cboDeploy" data-ax5select="cboDeploy" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-100"></div>
						</div>
						
						<div class="dib vat margin-20-left" id="divApplyDate">
							<label class="tit_50 poa">특정일시</label>
							<div class="input-group dib vat ml_50" data-ax5picker="txtReqDate"style=" font-size:0px;width:130px" >
					            <input id="txtReqDate" type="text" class="f-cal" placeholder="yyyy/mm/dd">
					            <button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
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
					<div class="dib vat text-right" style="width: 370px">
						<div class="dib vat" id="divSvr">
							<input type="checkbox" class='checkbox-pie' id='chkSvr' data-label="적용서버선택">
							<button id="btnSvr" class="btn_basic_s margin-5-right">적용대상서버</button>
						</div>
						<button class="btn_basic_s" id="btnFile">첨부문서</button>
						<button class="btn_basic_s margin-5-left" id="btnDiff_bottom2">비교</button>
						<button class="btn_basic_s margin-5-left" id="btnReq">신청</button>
					</div>	
				</div>
		 	</div>
		 </div>
	 </div>
</div>
</body>

<div id="rMenu">
	<ul>
		<li id="getPrgList">선택한 경로 파일추출</li>
		<li id="getPrgList2">하위폴더포함하여 파일추출</li>
	</ul>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/apply/LocalRequest.js"/>"></script>