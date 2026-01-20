<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">관리자<strong>&gt; 코드정보</strong></div>
	
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="vat">
				<div class="width-25 dib">
					<div class="tit_80 poa">
						<label>구분명</label>
					</div>
					<div class="ml_80 tit_80 poa">
						<label>코드값</label>
					</div>
				</div>
				<div class="width-45 dib margin-5-left">
					<div class="tit_80 poa">
						<label>코드설명</label>
					</div>
				</div>
				
				<div class="width-100 dib vat">
					<div class="vat dib float-right">
						<button id="btnExl" class="btn_basic_s mw-80">엑셀저장</button>
						<!-- <button id="btnJob" class="btn_basic_s mw-80">업무정보</button> -->
					</div>
				</div>
				<div class="row">
					<div class="width-25 dib">
						<div class="tit_80 poa">
							<label>대구분</label>
						</div>
						<div class="ml_80 vat">
							<input id="txtMaCode" name="txtMaCode" type="text" class="width-100">
						</div>
					</div>
					<div class="width-43 dib margin-5-left">
						<div class="width-100 dib vat">
							<input id="txtMaName" name="txtMaName" type="text" class="width-100">
						</div>
					</div>
					
					<div class="dib vat tar" style="width:calc(30% - 165px);">
						<div class="tit_80 poa" style="text-align: left; padding-left:14px;">
							<label>검색조건</label>
						</div>
						<div class="ml_80 vat tal" style="width:calc(100% - 150px)">
							<div id="cboQry" data-ax5select="cboQry" data-ax5select-config="{size:'sm',theme:'primary'}"></div> 
						</div>
					</div>
					<div class="dib float-right">
						<button id="btnQry" class="btn_basic_s mw-80 margin-5-right">조회</button>
						<button id="btnJob" class="btn_basic_s mw-80">업무정보</button>
					</div>
				</div>
				<div class="row">
					<div class="width-25 dib">
						<div class="tit_80 poa">
							<label>소구분</label>
						</div>
						<div class="ml_80 vat">
							<input id="txtMiCode" name="txtMaCode" type="text" class="width-100">
						</div>
					</div>
					<div class="width-43 dib margin-5-left">
						<div class="width-100 dib vat">
							<input id="txtMiName" name="txtMaName" type="text" class="width-100">
						</div>
					</div>
					
					<div class="width-11 dib vat tar">
						<div class="tit_80 poa" style="text-align: left; padding-left:14px;">
							<label>소구분순서</label>
						</div>
						<div class="ml_80 vat tal">
							<input id="txtSeq" name="txtSeq" type="text" class="vat dib" style="width:calc(100% - 5px); min-width:25px;">
						</div>
					</div>
					<div class="dib vat" style="width:116px;">
						<input id="optUse" type="radio" name="radio" value="use"/>
						<label for="optUse">사용</label>
						<input id="optNotUse" type="radio" name="radio" value="notUse"/>
						<label for="optNotUse">미사용</label>
					</div>
					
					<div class="dib vat float-right" style="width: 165px">
						<div class="vat dib">
							<button id="btnReq" class="btn_basic_s mw-80 margin-5-right">적용</button>
							<button id="btnClear" class="btn_basic_s mw-80">Clear</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic margin-10-top" style="height: 80%;">
		<div data-ax5grid="codeGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/CodeInfo.js"/>"></script>