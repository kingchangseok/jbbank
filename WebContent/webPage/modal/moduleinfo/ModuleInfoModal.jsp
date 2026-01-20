<!-- 
화면 명: 실행모듈맵핑
화면호출:
1) 실행모듈정보 -> 맵핑등록버튼 클릭
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; height: 100% !important; min-height: 630px !important ">
<div class="pop-header">
	<div>
		<label id="lbSub">프로그램Vs실행모듈 맵핑</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<div class="l_wrap width-50 dib margin-10-bottom">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="por">					
					<div class="width-40 dib vat">
						<label class="title">[프로그램목록]</label>
					</div>
					<!-- <div class="dib vat" style="width:calc(60% - 80px);">
						<label class="tit_80 dib poa">프로그램종류</label>
						<div class="ml_80">
							<div id="cboRsrc" data-ax5select="cboRsrc" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div> 
							<div class="vat dib poa_r">
								<input type="checkbox" class="checkbox-module" id="chkNoPrg" data-label="미연결건"/>
							</div>
						</div>
					</div> -->
				</div>	
				
				<div class="sm-row por vat">					
					<div class="width-40 dib">
						<label>시스템</label>
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-70 dib ml_10"></div> 
					</div>
					
					<div class=" dib vat" style="width:calc(60% - 80px);">
						<label class="tit_70 dib poa">프로그램명</label>
						<div class="ml_70">
							<input id="txtPrg" type="text" class="width-100 dib">
						</div>										
						<div class="vat poa_r">
							<button id="btnQryPrg" class="btn_basic_s" style="width:75px;">검색</button>
						</div>
					</div>
				</div>	
			</div>
		</div>
		
		<div class="az_board_basic" style="height:calc(100% - 170px);">
			<div data-ax5grid="prgGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
		</div>	
	</div>	
	<div class="r_wrap width-50 dib margin-10-bottom">
		<div class="margin-5-left">
			<div class="az_search_wrap">
				<div class="az_in_wrap">
					<div class="por">					
						<div class="width-40 dib vat">
							<label id="lbUser" class="title">[모듈목록]</label>
						</div>
					</div>	
					<div class="sm-row por vat">
						<div class="vat">
							<label>모듈명</label>
							<input id="txtMod" type="text" class="width-60 ml_10" style="width:calc(100% - 200px);">									
							<div class="vat dib margin-5-left">
								<button id="btnQryMod" class="btn_basic_s">검색</button>
							</div>
							<div class="vat poa_r">
								<input type="checkbox" class="checkbox-module " id="chkNoMod" data-label="미연결건"/>
							</div>
						</div>
					</div>	
				</div>
			</div>
			<div class="az_board_basic" style="height:calc(100% - 170px);">
				<div data-ax5grid="modGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
			</div>	
		</div>
	</div>			
	<div class="margin-5-top tar">
		<button id="btnReq" class="btn_basic_s">등록</button>
		<button id="btnClose" class="btn_basic_s">닫기</button>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/moduleinfo/ModuleInfoModal.js"/>"></script>
	