<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<c:import url="/js/ecams/common/commonscript.jsp" />

<body style="padding: 10px !important; width: 60% !important; min-width: 0px !important;">
<div class="row half_wrap_cb">
	<!--left-->
	<div class="l_wrap width-40">
		<div class="margin-5-right">
			<div>
				<label class="tit_100 poa">SR번호</label>
				<div class="ml_100">
					<input id="txtSRID" type="text" class="width-100" readonly/>
				</div>
			</div>
			<div class="row" style="display: none;">
				<input id="rdoOpt1" tabindex="8" type="radio" name="rdoSrStatus" checked="checked" style="margin-top:6px;"/>
				<label for="rdoOpt1">SR완료</label>
				<input id="rdoOpt2" tabindex="8" type="radio" name="rdoSrStatus" style="margin-top:6px;"/>
				<label for="rdoOpt2">SR중단</label>
			</div>
		</div>
	</div>
	<!--right-->
	<div class="r_wrap width-60">
		<div class="margin-5-left">
			<div>
				<label class="tit_50 poa">SR제목</label>
				<div class="ml_50" >
					<input id="txtSRTitle" class="width-100" type="text" readonly/>
				</div>
			</div>
			<div class="row por" style="display: none;" id="applyUserDate">
				<div class="width-50 dib">
					<label class="tit_50 poa">확인자</label>
					<div class="ml_50">
						<input id="txtApplyUser" class="width-100" type="text" readonly/>
					</div>	
				</div>
				<div class="width-50 dib poa_r">
					<label class="tit_80 poa" style="padding-left:25px;">확인일시</label>
					<div class="ml_80">
						<input id="txtApplyDate" class="width-100" type="text" readonly/>
					</div>				
				</div>	
			</div>
		</div>
	</div>
</div>		
<div class="row">
	<label class="tit_100 poa">완료의견</label>
	<div class="ml_100" >
		<form>
			<textarea id="txtReqContent" name="txtReqContent" style="align-content:left;width:100%;height:50px;resize: none; overflow-y:auto; padding: 5px;"  readonly></textarea>
		</form>
	</div>
</div>

<div class="row">
	<label id="lbTxt" class="tit_100 poa" style="display: none;">결재/반송의견</label>
	<div id="divTxt" class="ml_100" style="display: none;">
		<form>
			<textarea id="txtConMsg" name="txtConMsg" style="align-content:left;width:100%;height:50px;resize: none; overflow-y:auto; padding: 5px;"></textarea>
		</form>
	</div>
</div>
<!--버튼 S-->
<div class="row tar">
	<button id="btnOk" class="btn_basic_s" style="display: none;">결재</button>
	<button id="btnCncl" class="btn_basic_s" style="display: none;">반려</button>
	<button id="btnConf2" class="btn_basic_s" style="display: none;">결재정보</button>
	<button id="btnReg" class="btn_basic_s" disabled>등록/수정</button>
</div>
<!--게시판 S-->
<div class="tit" style="margin-bottom: -5px;">
	<h3>▪ 개발자 및 테스트 담당자 별 실 투입시간</h3>
</div>
<div class="row az_board_basic" style="height: 40%">
   	<div data-ax5grid="devListGrid"
		data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"
		style="height: 100%;"></div>
</div>	
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<body style="padding: 10px !important; width: 100% !important; min-width: 0px !important;">

<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/SRCompleteTab.js"/>"></script>

