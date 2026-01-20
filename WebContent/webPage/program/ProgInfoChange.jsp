<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
  	<div id="history_wrap"></div>
       <!-- 검색 S-->      
	<div class="az_search_wrap">
		<div class="az_in_wrap checkout_tit">
               <div class="row por">
                <div class="width-20 dib vat">
                    <label id="lbSystem" class="tit_50 poa">시스템</label>
                    <div class="ml_50 vat">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-95"></div>
					</div>
				</div>
				<div class="width-20 dib vat">
                    <label id="lbSystem" class="tit_90 poa">프로그램종류</label>
                    <div class="ml_90 vat">
						<div id="cboRsrccd" data-ax5select="cboRsrccd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-95"></div>
					</div>
				</div>		
				<div class="width-60 dib">
                   	<label class="tit_80 poa">프로그램명</label>
                    <div class="ml_80 tal">
						<input id="txtRsrcName" name="txtRsrcName" type="text" style="width: calc(100% - 150px);"></input>						
					</div>
					<div class="vat poa_r" id="searchBox">
						<button id="btnSearch" class="btn_basic_s" style="width:70px;">조회</button>
						<button id="btnExl" class="btn_basic_s" style="width:70px; margin-left: 5px;">엑셀저장</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div>
	    <div class="az_board_basic az_board_basic_in" style="height: 70%;">
	    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
		<!-- 게시판 E -->
	</div>
	
	<div class="row">
		<div class="az_search_wrap">
			<div class="az_in_wrap checkout_tit">
	        	<div class="row por">
	                <div class="width-20 dib vat">
	                    <div class="tit_100 dib vat">
	                    	<input type="checkbox" class="checkbox-pie" id="chkJob" name="chkJob" data-label="업무" checked></input>
	                    </div>
	                    <div class="vat dib" style="width: calc(100% - 110px);">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
						</div>
					</div>
					<div class="width-65 dib vat">
						<div class="tit_100 dib vat">
							<input type="checkbox" class="checkbox-pie" id="chkAbout" name="chkAbout" data-label="프로그램설명"></input>
	                    </div>
	                    <div class="vat dib" style="width: calc(100% - 110px);">
							<input id="txtAbout" name="txtAbout" type="text" class="dib width-100" autocomplete="off"></input>
						</div>
					</div>		
				</div>
				<div class="row por">
					<div class="width-20 dib vat">
	                    <div class="tit_100 dib vat">
	                    	<input type="checkbox" class="checkbox-pie" id="chkRsrcType" name="chkRsrcType" data-label="프로그램구분"></input>
	                    </div>
	                    <div class="vat dib" style="width: calc(100% - 110px);">
							<div id="cboRsrcType" data-ax5select="cboRsrcType" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
						</div>
					</div>
					<div class="width-30 dib vat">
						<div class="tit_100 dib vat">
							<input type="checkbox" class="checkbox-pie" id="chkTeam" name="chkTeam" data-label="관리팀"></input>
						</div>
	                    <div class="vat dib" style="width: calc(100% - 110px);">
							<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
						</div>
					</div>
					<div class="width-35 dib vat">
						<div class="tit_100 dib vat">
							<input type="checkbox" class="checkbox-pie" id="chkCompileMode" name="chkCompileMode" data-label="컴파일모드" checked></input>
						</div>
	                    <div class="vat dib" style="width: calc(100% - 110px);">
							<div id="cboCompileMode" data-ax5select="cboCompileMode" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true,selSize:2}" class="dib width-100"></div>
						</div>
					</div>
				</div>
				<div class="row por">
					<div class="width-20 dib vat">
						<div class="tit_100 dib vat">
							<input type="checkbox" class="checkbox-pie" id="chkCaller" name="chkCaller" data-label="관리담당자"></input>
						</div>
	                    <div class="vat dib" style="width: calc(100% - 110px);">
							<input id="txtEditor" type="text" class="dib width-100" readonly/>
						</div>
					</div>
					<div class="r_wrap vat poa_r">
						<label id="lbSystem" class="ml_110">**신규 또는 운영중인 프로그램에 대해서만 수정가능</label>
						<button id="btnUpdate" class="btn_basic_s" style="width:70px;">수정</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="SysCd" id="SysCd">
	<input type="hidden" name="UserId" id="UserId"> 
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="rgtList"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/program/ProgInfoChange.js"/>"></script>