<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.fontStyle-notaccess {
	color: #d12832;
}
.fontStyle-module {
	color: #FF8080;
}
.borderBox{
	padding: 0 5px; 
	border: 1px solid #ddd;
}
</style>

<div class="contentFrame">
	<!-- history S-->
	<div id="history_wrap">적용 <strong>&gt; 원복요청</strong></div>
	<!-- 검색 S-->
	<div class="az_search_wrap" style="margin: 0px;">
		<div class="az_in_wrap">
		    <div class="row">
				<div class="width-25 dib">
					<!--시스템S-->
                	<label class="tit_60 dib poa">*시스템</label>
                	<div class="ml_60">
	                    <div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
                	</div>
				</div>
				<div class="width-25 dib vat margin-5-left">	
					<div class="dib vac borderBox" id="divopt">
						<input id="optReq" type="radio" name="radio" value="L" checked/>
						<label for="optReq">신청건</label>
						<input id="optVer" type="radio"  name="radio" value="V"/>
						<label for="optVer">이전버전</label>
					</div>	
					<div class="vat dib margin-5-left">
						<button class="btn_basic_s" id="btnQry">검색</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="width-100 dib vat">
			<input id="txtDir" type="text" class="width-60 dib vat" readonly></input>
			<input id="txtProg" name="txtProg" type="text" style="width: calc(40% - 55px);" placeholder="프로그램명을 입력하세요."></input>
			<div class="vat dib float-right">
				 <button id="btnSearch" class="btn_basic_s">검색</button>
			</div>
		</div>
	</div>
	
   	<!-- 롤백가능 운영적용신청 목록 S-->
    <div class="az_board_basic" style="height:calc(60% - 200px); padding-top:5px;">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
		
	<!-- 설명2 -->
	<div class="width-100 margin-5-top margin-5-bottom">
		<div class="dib" style="float:right">
			<button id="btnAdd" class="btn_basic_s">추가</button>
			<button id="btnDel" class="btn_basic_s">제거</button>
		</div>
	</div>
	
    <!-- 선택한 운영적용신청 프로그램목록 S-->
    <div class="az_board_basic margin-5-top" style="height:42%">
    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
	
	<!-- 신청사유 S -->
	<div class="row">
	    <div class="width-100 dib vat">
			<label class="tit_60 poa">*신청사유</label>
	           <div class="ml_60 tal">
				<input id="txtSayu" name="txtSayu" type="text" style="width: calc(100% - 85px);" placeholder="신청사유를 입력하세요."></input>
				<div class="vat dib">
					 <button id="btnReq" class="btn_basic_s" style="width:80px;">신청</button>
				</div>
			</div>
		</div>
	</div>
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/apply/RollBackReq.js"/>"></script>