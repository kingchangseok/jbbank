<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap"></div>
	
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="row por">
				<div class="width-25 dib vat">
					<label class="tit_50 poa">시스템</label>
					<div class="ml_50">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:95%;"></div>
					</div>
				</div>
				<div class="width-25 dib vat">
					<label class="tit_40 poa">업무</label>
					<div class="ml_40">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:95%;"></div>
					</div>
				</div>
				<div class="dib vat" style="width: calc(50% - 150px)">
					<label class="tit_60 poa" id="lbl1">검색단어</label>
					<div class="ml_60">
						<input id=txtSearch type="text" class="width-100" placeholder="검색단어를 입력하세요.">
					</div>
				</div>
				
				<div class="dib vat float-right">
					<div class="dib">
						<button class="btn_basic_s" id="btnList" style="margin-left: 5px; margin-right: 0px;">목록추출</button>
					</div>
					<div class="vat dib margin-5-left">
						<button class="btn_basic_s" id="btnExcel" data-grid-control="excel-export" style="margin-left: 0px; margin-right: 0px;">엑셀저장</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic" style="height: 85%;">
		<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
	</div>
	
	<div class="row por">
		<div class="dib vat float-right">
			<div class="dib">
				<button class="btn_basic_s" id="btnReq" style="margin-left: 5px; margin-right: 0px;">신규등록</button>
			</div>
		</div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="srid"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/program/DevTool.js"/>"></script>