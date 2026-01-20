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
		<label id="lbSub">[sub프로그램등록]</label>
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
						<label class="title">[기준프로그램]</label>
					</div>
					<div class="dib vat" style="width:calc(60%);">
						<input id="txtAcptno" type="text" class="width-100 dib">
					</div>
				</div>	
			</div>
		</div>
		
		<div class="scrollBind sm-row" style="height:35%">
			<ul class="list-group" id="lstModule"></ul>
		</div>
	</div>	
	<div class="r_wrap width-50 dib margin-10-bottom">
		<div class="margin-5-left">
			<div class="az_search_wrap">
				<div class="az_in_wrap">
					<div class="por">
						<div class="width-40 dib vat">
							<label id="lbUser" class="title">[sub프로그램]</label>
						</div>
						<div class="dib vat width-60">
							<label class="tit_80 poa">업무</label>
							<div class="ml_80">
								<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div> 
							</div>
							
						</div>
					</div>
				</div>
			</div>
			<div class="scrollBind sm-row" style="height:35%">
				<ul class="list-group" id="lstSubprog"></ul>
			</div>
		</div>
	</div>		
	<div class="row width-100">
			<label id="lbUser" class="title">[맵핑정보]</label>
			
			<div class="vat dib" style="float: right;">
				<button id="btnAdd" class="btn_basic_s">추가</button>
			</div>
	</div>	
	<div class="row width-100">
		<div class="az_board_basic" style="height:35%">
			<div data-ax5grid="mappingGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
		</div>	
	</div>
	<div class="margin-5-top tar">
		<button id="btnDel" class="btn_basic_s">삭제</button>
		<button id="btnReq" class="btn_basic_s">등록</button>
		<button id="btnClose" class="btn_basic_s">취소</button>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/SubPrgModal.js"/>"></script>
	