<!--  
	* 화면명: 실행모듈정보
	* 화면호출:
	 1) 프로그램 -> 실행모듈정보(ModuleInfo.jsp)
	 2) 프로그램등록 -> 실행모듈정보(ModuleInfoModal.jsp)
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/webPage/common/common.jsp" />

<style>
.width-50 {
	width: calc(50% - 5px) !important;
}
</style>
<div id="wrapper">
	<div class="contentFrame">
		<!-- history S-->
		<div id="history_wrap">프로그램 <strong>&gt; 실행모듈정보</strong></div>
		<div class="l_wrap width-50">
			<!-- 프로그램목록 -->
			<div class="az_search_wrap sm-row">
				<div class="az_in_wrap">
					<div class="por">
						<div class="width-30 dib vat">
							<label class="title">[프로그램목록]</label>
						</div>
						<div class="dib vat" style="width: calc(60% - 80px);">
							<label class="tit_80 dib poa">시스템</label>
							<div class="ml_80">
								<div id="cboSysCd" data-ax5select="cboSysCd"data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"class="width-100 dib"></div>
							</div>
						</div>
					</div>
					<div class="sm-row por vat">
						<div class="width-30 dib">
<!-- 							<label>시스템</label> -->
<!-- 							<div id="cboSysCd" data-ax5select="cboSysCd"data-ax5select-config="{size:'sm',theme:'primary'}"class="width-70 dib"></div> -->
						</div>
						<div class=" dib vat" style="width: calc(60% - 80px);">
							<label class="tit_80 dib poa">프로그램명</label>
							<div class="ml_80">
								<input id="txtPrgName" type="text" class="width-100 dib">
							</div>
							<div class="vat poa_r">
								<button id="btnFind" class="btn_basic_s" style="width: 50px;">검색</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="az_board_basic" style="height: 25%">
					<div data-ax5grid="firstGrid"data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"style="height: 100%;" class="frameResize"></div>
				</div>
			</div>
		</div>
		<div class="r_wrap width-50">
			<!-- 모듈목록 -->
			<div class="az_search_wrap sm-row">
				<div class="az_in_wrap por">
					<div class="row">
						<div class="width-40 dib vat">
							<label id="lbUser" class="title">[모듈목록]</label>
						</div>
					</div>
					<div class="sm-row por vat">
						<div class="vat"style="margin-left: 80px;">
							<label>모듈명</label> <input id="txtPrgName2" type="text"class="width-60" style="width: calc(65% - 200px);">
							<div class="vat dib margin-5-left">
								<button id="btnFind2" class="btn_basic_s"style="width: 50px;">검색</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="az_board_basic" style="height: 25%">
					<div data-ax5grid="secondGrid"data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"style="height: 100%;" class="frameResize"></div>
				</div>
			</div>
		</div>
	
		<div class="az_search_wrap margin-10-top">
			<div class="az_in_wrap por cb">
				<div class="row">
					<div class="dib vat" style="width: 120px;">
						<label class="title">[연관등록목록]</label>
					</div>
					<div class="dib vat width-100" style="width: calc(100% - 300px);">
						<input id="optAll" type="radio" name="radio" value="A" />
						<label for="optAll">전체</label> 
						<input id="optPrg" type="radio"name="radio" value="P" checked="checked" />
						<label for="optPrg">프로그램명</label>
						<input id="optMod" type="radio" name="radio" value="M" />
						<label for="optMod">모듈명</label> <input id="txtPrgName3" type="text" style="width: 350px"></input>
						<button id="btnFind3" class="btn_basic_s margin-5-left"style="width: 50px;">검색</button>
					</div>
					<div class="dib vat r_wrap">
						<button class="btn_basic_s margin-5-left" id="btnExl">엑셀저장</button>
						<button class="btn_basic_s margin-5-left r_wrap" id="btnReg"style="width: 50px;">등록</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="az_board_basic" style="height: 50%">
				<div data-ax5grid="thirdGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"style="height: 100%;" class="frameResize"></div>
			</div>
		</div>
		<div class="row width-100 dib vat">
			<div class="r_wrap">
				<button class="btn_basic_s" id="btnDel"style="width: 50px;">폐기</button>
				<!--<button class="btn_basic_s margin-5-left" id="btnExit">닫기</button> -->
			</div>
		</div>
	</div>
</div>


<form name="popPam" id="popPam" method="post">
	<input type="hidden" name="acptno" id="acptno" /> 
	<input type="hidden" name="user" id="user" /> 
	<input type="hidden" name="itemid" id="itemid" /> 
	<input type="hidden" name="rgtList" id="rgtList" /> 
	<input type="hidden" name="adminYN" id="adminYN" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"src="<c:url value="/js/ecams/program/ModuleInfo.js"/>"></script>