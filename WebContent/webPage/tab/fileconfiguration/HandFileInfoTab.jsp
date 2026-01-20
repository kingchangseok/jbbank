<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="az_search_wrap">
	<div class="az_in_wrap">
		<div class="row vat">
			<div class="width-20 dib">
				<div class="tit_80 poa">
					<label>작업일자</label>
				</div>
				<div class="ml_80">
					<div id="divPicker" data-ax5picker="dateDeploy" class="width-100">
			            <input id="dateDeploy" name="dateDeploy" type="text" class="f-cal" placeholder="yyyy/mm/dd">
			            <button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
				</div>
			</div>
			<div class="width-20 dib vat" style="margin-left: 2px;">
				<div class="width-100">
					<input id="txtDeploy" name="txtDeploy" type="text" class="f-cal" autocomplete="off"><span class="btn_calendar"><i class="fa fa-clock-o"></i></span>
				</div>
			</div>
			<div class="dib vat margin-10-left">
				<input type="checkbox" class="checkbox-file" id="chkTime" data-label="즉시실행"/>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-20 dib">
				<div class="tit_80 poa">
					<label>시스템</label>
				</div>
				<div class="ml_80 vat">
					<input id="txtSys" name="txtSys" type="text" class="width-100 vat">
				</div>
			</div>
			<div class="width-20 dib vat">
				<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width: 100%"></div> 
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-40 dib vat">
				<div class="tit_80 poa">
					<label>서버</label>
				</div>
				<div class="ml_80 vat">
					<input id="txtSvr" name="txtSvr" type="text" class="width-100 vat">
				</div>
			</div>
			<div class="width-20 dib vat">
				<div class="vat dib">
					<button id="btnRun" name="btnRun" class="btn_basic_s vat margin-10-left">등록</button>
					<button id="btnDel" name="btnDel" class="btn_basic_s vat margin-10-left">삭제</button>
				</div>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-100 vat dib">
				<!-- <div class="az_board_basic margin-10-top" style="height: calc(68% + 37px);"> -->
					<div class="az_board_basic margin-10-top" style="height: 69%;">
					<div data-ax5grid="handGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="frameResize"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/fileconfiguration/HandFileInfoTab.js"/>"></script>