<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

	<div class="cb">
		<div class="l_wrap width-55">
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
	                <div class="width-67 dib vat" style="margin-left: 30px;">
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
		    	<div data-ax5grid="conInfoGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>	
			</div>									
			<div class="tar sm-row" style="margin-top: 3px;">
				 <button id="btnDell" class="btn_basic_s">삭제</button>
			</div>
			<!-- 게시판 E -->
		</div>
		<div class="float-right width-45">
			<div class="l_wrap width-50">
				<div class="margin-5-left">
					<label class="title_s">프로그램종류</label>
					<div class="poa_r dib">
						<input type="checkbox" class="checkbox-view" id="chkPrgAll" data-label="전체선택"/>
					</div>
					<div class="scrollBind sm-row" style="height: calc(55% - 70px);">
						<ul class="list-group" id="ulPrgInfo"></ul>
					</div>
				</div>
			</div>
			<div class="l_wrap width-50">
				<div class="margin-5-left">
					<label class="title_s">업무</label>
					<div class="poa_r dib">
						<input type="checkbox" class="checkbox-view" id="chkJobAll" data-label="전체선택"/>
					</div>
					<div class="scrollBind sm-row" style="height: calc(55% - 70px);" id="divJob">
						<ul class="list-group" id="ulJobInfo"></ul>
					</div>
				</div>
			</div>
			<div class="width-100">
				<div class="sm-row font_11 margin-5-left">							
	    			<label class="title_s" style="margin-top: 5px">등록순서</label>
	    			<p class="sm-row">시스템선택&gt;요청구분선택&gt;프로그램종류선택&gt;업무선택&gt;실행구분선택&gt;스크립트유형선택&gt;등록버튼클릭</p>
				</div>
			</div>
		</div>
	</div>
    <!-- 검색 S-->    
	<div class="az_search_wrap sm-row">
		<div class="az_in_wrap por cb">
			<!-- line1 -->	
			<div class="dib width-100 sm-row">	
                <div class="float-left" style="width: calc(50% - 50px)">
                    <label class="tit_60 poa">실행구분</label>
                    <div class="ml_60">
						<div id="cboPrcSys" data-ax5select="cboPrcSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<div class="width-50 vat float-left">
				 	<label class="tit_90 ml_10 poa">스크립트유형</label>
                    <div class="ml_90">
						<div id="cboBldCd" data-ax5select="cboBldCd" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<button id="btnReg" name="btnReg" class="btn_basic_s float-right">등록</button>
				<!-- <div class="width-50 vat float-left">
					<div class="dib vat float-left ml_7">
						<input type="checkbox" class="checkbox-view" id="chkLocal" data-label="형상관리서버에서 실행"/>
						<input type="checkbox" class="checkbox-view" id="chkSeq" data-label="쉘순차 실행"/>
						<input type="checkbox" class="checkbox-view" id="chkBatch" data-label="일괄쉘 실행"/>
					</div>
				</div> -->
			</div>
			<!-- line2 -->	
			<!-- <div class="dib width-100 sm-row">	
                <div class="width-50 float-left">
                    <label class="tit_80 poa">스크립트유형</label>
                    <div class="ml_80">
						<div id="cboBldCd" data-ax5select="cboBldCd" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<div class="width-50 vat float-right por">
					<button id="btnReg" name="btnReg" class="btn_basic_s float-right">등록</button>
					<div class="dib vat float-left ml_7" >
						<input id="optBefore" type="radio" name="releaseChk" value="before" checked="checked"/>
						<label for="optBefore">파일송수신 전</label>
						<input id="optAfter" type="radio" name="releaseChk" value="after"/>
						<label for="optAfter" class="margin-35-right" >파일송수신 후</label>
					</div>
				</div>
			</div> -->
		</div>
	</div>
    <!-- 검색 E-->
    <!-- 게시판 S-->
    <div class="az_board_basic" style="height: 30%; min-height: 80px;">
    	<div data-ax5grid="scriptGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%; min-height: 80px;"></div>
	</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/buildrelease/TypeConnectionTab.js"/>"></script>