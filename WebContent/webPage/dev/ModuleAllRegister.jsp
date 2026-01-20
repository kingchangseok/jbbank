<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />

<style>
.wrap_width {
	width: calc(98% - 280px);
}
</style>

<div class="contentFrame">
	<div id="history_wrap">등록<strong>&gt; 실행모듈일괄등록</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row por">
					<!--시스템S-->
					<div class="width-30 dib">
						<label class="tit_50 poa">시스템</label>
						<div class="ml_50">
							<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
						</div>
					</div>
					<div class="width-40 dib vat">
						<div class="ml_10">
							<input id="optNomal" type="radio" name="radio" value="normal"/>
							<label for="optNomal">정상건</label>
							<input id="optErr" type="radio" name="radio" value="err"/>
							<label for="optErr">오류건</label>
							<input id="optAll" type="radio" name="radio" value="all"/>
							<label for="optAll">전체</label>
							<input type="checkbox" class="checkbox-batch" id="chkOk" data-label="정상건만등록"/>
						</div>
					</div>
					<div class="float-right">
						<button id="btnLoadExl" class="btn_basic_s mw-85">엑셀파일</button>
						<button id="btnReq" class="btn_basic_s mw-85 margin-5-left">일괄등록</button>
						<button id="btnSmm" class="btn_basic_s mw-85 margin-5-left">참고사항</button>
					</div>
				</div>
				<!-- line2 -->
				<div class="row por">
					<div class="float-right">
						<button id="btnSaveExl" class="btn_basic_s vat mw-85">엑셀저장</button>
						<button id="btnExlTmp" class="btn_basic_s vat mw-85 margin-5-left">엑셀템플릿</button>
						<button id="btnDel" class="btn_basic_s vat mw-85 margin-5-left"style="visibility: hidden;">삭제</button>
					</div>
				</div>
			</div>
		</div>
	<div class="sm-row vat">
		<div class="width-100 dib">
			<div class="az_board_basic" style="height: 82%;">
				<div data-ax5grid="batchGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
			</div>
		</div>
	</div>
</div>
<form name="popPam">
	<input type="hidden" name="userId"/>
	<input type="hidden" name="userName"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="strReqCD"/>
</form>
<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/ModuleAllRegister.js"/>"></script>