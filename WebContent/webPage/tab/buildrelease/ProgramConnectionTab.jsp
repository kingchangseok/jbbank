<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

	<div class="cb">
		<div class="l_wrap width-60">
		    <!-- 검색 S-->    
			<div class="az_search_wrap sm-row">
				<div class="az_in_wrap por">
					<!-- 시스템 -->		
	                <div class="width-30 dib vat">
	                    <label class="tit_50 poa" style="width: 50px;">시스템</label>
	                    <div class="ml_50" style="margin-left: 50px;">
							<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
						</div>
					</div>
				    <!-- 요청구분 -->
	                <div class="width-64 dib vat" style="margin-left: 30px;">
	                    <label class="tit_60 poa dib vat" style="width: 60px;">요청구분</label>
	                    <div class="ml_60 vat" style="margin-left: 60px;">
							<div id="cboQry" data-ax5select="cboQry" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
						</div>
					</div>
				</div>
			</div>
		    <!-- 검색 E-->
		    <!-- 게시판 S-->
		    <div class="az_board_basic" style="height: 45%">
		    	<div data-ax5grid="conInfoGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;" class="frameResize"></div>	
			</div>									
			<div class="tar sm-row" style="margin-top: 3px;">
				 <button id="btnDell" class="btn_basic_s">삭제</button>
			</div>
			<!-- 게시판 E -->
		</div>
		<div class="r_wrap width-40 sm-row"  style="height: calc(45% + 35px);">
			<div class="l_wrap width-100" style="height: 100%;">
				<div class="margin-5-left" style="height: 100%;">
					<label class="title_s">프로그램명</label>
					<div class="row">
						<input id="prg_txt" name="prg_txt" type="text" style="width: calc(100% - 45px);" autocomplete="off">
						<button id="prg_btn" name="prg_btn" class="btn_basic_s float-right">검색</button>
						<div class="poa_r dib">
							<input type="checkbox" class="checkbox-view" id="chkPrgAll" data-label="전체선택"/>
						</div>
					</div>
					<div class="scrollBind row" style="height: calc(100% - 65px);">
						<ul class="list-group" id="ulPrgInfo"></ul>
					</div>
				</div>
			</div>
			<div class="width-100">
				<div class="sm-row font_11 margin-5-left">							
	    			<label class="title_s">등록순서</label>
	    			<p>시스템선택&gt;요청구분선택&gt;프로그램명선택&gt;실행구분선택&gt;스크립트유형선택&gt;등록버튼클릭</p>
				</div>
			</div>
		</div>
	</div>
    <!-- 검색 S-->    
	<div class="az_search_wrap sm-row">
		<div class="az_in_wrap por cb">
			<!-- line1 -->	
			<div class="dib width-100 sm-row">	
                <div class="width-50 float-left">
                    <label class="tit_80 poa">실행구분</label>
                    <div class="ml_80">
						<div id="cboPrcSys" data-ax5select="cboPrcSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<div class="width-50 vat float-left">
					<div class="dib vat float-left ml_7">
						<input type="checkbox" class="checkbox-view" id="chkExe" data-label="실행여부선택"/>
						<input type="checkbox" class="checkbox-view" id="chkLocal" data-label="형상관리서버에서 실행"/>
						<input type="checkbox" class="checkbox-view" id="chkSeq" data-label="쉘순차 실행"/>
						<!-- <input type="checkbox" class="checkbox-view" id="chkSysSeq" data-label="시스탬내 순차 실행"/> -->
						
						<input type="checkbox" class="checkbox-view" id="chkBatch" data-label="일괄쉘 실행"/>
						<!-- <div class="dib vat float-right">
							<input id="optAcpt" type="radio" name="grpTotYN" value="optAcpt"/><label for="optAcpt">신청건별</label>
							<input id="optRsrc" type="radio" name="grpTotYN" value="optRsrc"/><label for="optRsrc">프로그램유형별</label>
							<input id="optDir" type="radio" name="grpTotYN" value="optDir"/><label for="optDir">디렉토리별</label>
							<input id="optJob" type="radio" name="grpTotYN" value="optJob"/><label for="optJob">업무별</label>
						</div> -->
					</div>
				</div>
			</div>
			<!-- line2 -->	
			<div class="dib width-100 sm-row">	
                <div class="width-50 float-left">
                    <label class="tit_80 poa">스크립트유형</label>
                    <div class="ml_80">
						<div id="cboBldCd" data-ax5select="cboBldCd" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<div class="width-50 vat float-right por">
					<button id="btnReg" name="btnReg" class="btn_basic_s float-right">등록</button>
					<div class="dib vat float-left ml_7" >
						<input id="optBefore"  type="radio" name="releaseChk" value="before" checked="checked"/>
						<label for="optBefore" >파일송수신 전</label>
						<input id="optAfter" type="radio" name="releaseChk" value="after" />
						<label for="optAfter" class="margin-35-right" >파일송수신 후</label>
					</div>
					<input type="checkbox" class="checkbox-view" id="chkNoExec" data-label="스크립트미실행"/>
				</div>
			</div>
		</div>
	</div>
    <!-- 검색 E-->
    <!-- 게시판 S-->
    <div class="az_board_basic" style="height: 30%">
    	<div data-ax5grid="scriptGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;" class="frameResize"></div>
	</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/buildrelease/ProgramConnectionTab.js"/>"></script>