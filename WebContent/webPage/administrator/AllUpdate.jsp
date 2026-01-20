<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
<div class="contentFrame">
    <!-- history S-->
    <div id="history_wrap"></div>
    <!-- history E-->   
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap por">
			<div class="l_wrap width-100 vat">
				<div class="row">
					<!-- 시스템 -->		
		            <div class="width-33 dib vat">
						<label class="tit_80 poa">시스템</label>
						<div class="ml_80 vat">
							<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
						</div>
					</div>
					<!-- 업무 -->
	               	<div class="width-33 dib vat">
	                   	<label class="tit_80 poa">업무</label>
	                   	<div class="ml_80">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
						</div>
					</div>
				    <!-- 프로그램종류 -->
					<div class="width-33 dib vat">
						<label id="lbJawon" class="tit_80 poa">프로그램종류</label>
						<div class="ml_80">
							<div id="cboJawon" data-ax5select="cboJawon" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 60px);"></div>
						</div>
					</div>
				</div>
				
				<div class="row">
					<!-- 프로그램경로 -->
	                <div class="width-66 dib vat">
	                    <label class="tit_80 poa">프로그램경로</label>
						<div class="ml_80 vat">
							<input id="txtDirPath" name="txtDirPath" type="text" class="dib" style="width: 95.4%">
						</div>
					</div>
					
					<div class="width-33 dib vat">
						<!-- 프로그램명 -->
						<label class="tit_80 poa">프로그램명</label>
		                <div class="ml_80">
							<input id="txtRsrcName" name="txtRsrcName" type="text" class="dib"  style="width:calc(100% - 60px);">
						</div>						
					</div>
					<button id="btnQry" name="btnQry" class="btn_basic_s poa margin-0-right" style="right:0; margin-right:0;">조 회</button>
				</div>
			</div>
		</div>
	</div>
    <!-- 검색 E-->
    
    <!-- 게시판 S-->
    <div class="az_board_basic" style="height:75%;">
    	<div data-ax5grid="grdProgList" style="height: 100%;"></div>
	</div>	
	<!-- 게시판 E -->
	
	<!-- 하단 S-->
	<div class="az_search_wrap margin-10-top">
		<div class="az_in_wrap por">
			<div class="l_wrap width-100 vat">
				<div class="row">
					<!-- 업무  -->
			        <div class="width-33 dib vat">
			            <label class="tit_80 poa">업무</label>
			            <div class="ml_80">
							<div id="cboAftJob" data-ax5select="cboAftJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
						</div>
					</div>
					
					<!-- 프로그램 종류 -->
			        <div class="width-33 dib vat">
			            <label class="tit_80 poa">프로그램종류</label>
			            <div class="ml_80">
							<div id="cboAftJawon" data-ax5select="cboAftJawon" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
						</div>
					</div>
					
					<!-- 담당자 -->
					<div class="width-33 dib vat">
			            <label class="tit_80 poa">담당자</label>
			            <div class="ml_80">
							<div id="cboAftOwner" data-ax5select="cboAftOwner" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 60px);"></div>
						</div>
					</div>	
				</div>
			
				<div class="row">
					<!-- SR-ID -->	
			        <div class="width-33 dib vat">
			            <label class="tit_80 poa">SR-ID</label>
			            <div class="ml_80">
							<div id="cboAftSRID" data-ax5select="cboAftSRID" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
						</div>
					</div>
					
					<!-- 경로 -->
					<div class="width-66 dib vat">
			            <label class="tit_80 poa">경로</label>
			            <div class="ml_80">
							<div id="cboAftDir" data-ax5select="cboAftDir" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 60px);"></div>
						</div>
					</div>
					<button id="btnUpdt" name="btnUpdt" class="btn_basic_s poa margin-0-right" style="right:0; margin-right:0;">수정</button>
				</div>
			</div>
	<!-- 하단 E-->
		</div>
	</div>
</div>
<!-- contener E -->
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/AllUpdate.js"/>"></script>