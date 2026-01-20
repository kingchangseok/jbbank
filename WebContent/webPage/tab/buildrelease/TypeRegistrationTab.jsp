<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
		
<div class="az_search_wrap sm-row">
	<div class="az_in_wrap por">
		<!-- 등록구분 -->		
		<div class="width-30 dib">
			<label class="tit_60 poa">등록구분</label>
			<div class="ml_60">
				<div id="cboBldGbn" data-ax5select="cboBldGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:calc(100% - 10px);"></div>
			</div>
		</div>
	    <!-- 유형구분 -->
		<div class="width-20 dib vat">
			<label class="tit_60 poa margin-5-left">유형구분</label>
			<div class="ml_60">
				<div id="cboBldCd" data-ax5select="cboBldCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:calc(100% - 10px);"></div>
			</div>	
		</div>
		<!-- 유형제목 -->
		<div class="width-50 dib vat">
			<label class="tit_60 poa margin-5-left">유형제목</label>
			<div class="ml_60">
				<div class="dib vat" style="width:calc(100% - 165px);">
					<input id="txtBldMsg" type="text" class="width-100">
				</div>
				<div class="dib vat float-right">
					<button class="btn_basic_s dib margin-5-left" id="btnDel" style="visibility: hidden;">유형삭제</button>
					<button class="btn_basic_s dib margin-5-left" id="btnCopy" style="visibility: hidden;">새이름저장</button>
					
				</div>
			</div>
		</div>
	    <!-- 수행명령 -->
		<div class="sm-row vat">
			<label class="tit_60 poa">수행명령</label>
			<div class="ml_60">
				<input id="txtComp" type="text" class="width-100">
			</div>
		</div>
		<div class="sm-row por">
	   		<!-- 순서 -->
			<div class="vat width-30 dib">
				<label class="tit_60 poa">순서</label>
				<div class="ml_60 ">
					<input id="txtSeq" name="txtSeq" type="text" style="width:calc(100% - 150px);" class="dib margin-5-right">
					<div class="dib poa">
						<input type="checkbox" class='checkbox-pie' id='chkLocal' data-label="형상관리서버에서 실행"></input>	
					</div>
				</div>
			</div>
		    <!-- 오류MSG -->
			<div class="vat dib" style="width:calc(70% - 330px);">
				<label class="tit_60 poa margin-5-left">오류MSG</label>
				<div class="ml_60">
					<input id="txtErrMsg" type="text" class="width-100">
				</div>
			</div>
			<!-- 버튼 -->
			<div class="poa_r">
				<button id="btnScr" class="btn_basic_s">추가</button>
				<button id="btnEdit" class="btn_basic_s" style="margin-left:3px;">수정</button>
				<button id="btnDelScr" class="btn_basic_s" style="margin-left:3px;">제거</button>
				<button id="btnReq" class="btn_basic_s" style="margin-left:3px;">저장</button>
				<button id="btnScriptLoad" class="btn_basic_s" style="margin-left:3px;">엑셀로드</button>
				<button id="btnScriptSave" class="btn_basic_s" style="margin-left:3px;">엑셀저장</button>
			</div>
		</div>
	</div>
</div>

<div class="az_board_basic" style="height: 35%; min-height: 80px;">
	<div data-ax5grid="editScriptGrid" data-ax5grid-config="{lineNumberColumnWidth: 40}" style="height: 100%;"></div>
</div>	

<div class="az_search_wrap sm-row">
	<div class="az_in_wrap por">
		<!-- 등록구분 -->		
		<div class="width-30 dib">
			<label id="lbUser" class="tit_60 poa">등록구분</label>
			<div class="ml_60">
				<div id="cboBldGbnB" data-ax5select="cboBldGbnB" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 10px);"></div>
			</div>
		</div>
		<div class="width-30 dib vat">
			<label class="tit_60 poa margin-10-left">수행명령</label>
			<div class="ml_60">
				<input id="txtOrder" style="width:calc(100% - 50px);" >
				<button id="btnQry" class="btn_basic_s float-right">조회</button>
			</div>
		</div>
	</div>
</div>

<div class="az_board_basic" style="height: 40%">
	<div data-ax5grid="scriptGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
</div>

<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/buildrelease/TypeRegistrationTab.js"/>"></script>