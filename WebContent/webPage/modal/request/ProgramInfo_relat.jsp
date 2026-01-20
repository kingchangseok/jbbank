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
		<label id="lbSub">프로그램과 실행모듈 연결정보</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<div class="l_wrap width-100 dib margin-10-bottom">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="sm-row por vat">					
					<div class="width-50 dib">
						<label class="tit_80 dib poa">시스템</label>
						<div class="ml_80">
							<input id="txtSysCd" type="text" class="width-100 dib">
						</div> 
					</div>
					
					<div class="width-50 dib vat">
						<label class="tit_80 dib poa">&nbsp;&nbsp;프로그램명</label>
						<div class="ml_80">
							<input id="txtPrg" type="text" class="width-100 dib">
						</div>										
					</div>
				</div>	
				<div class="sm-row por vat">
					<div class="width-100 dib">
						<label class="tit_80 dib poa">디렉토리</label>
						<div class="ml_80">
							<input id="txtDir" type="text" class="width-100 dib">
						</div>
					</div>		
				</div>
			</div>
		</div>
		<div>
			<label>실행모듈목록</label>
			<div class="tar dib" style="margin-bottom: 5px; float: right;">
				<button id="btnReq" class="btn_basic_s">등록</button>
				<button id="btnClose" class="btn_basic_s">닫기</button>
			</div>
		</div>
		<div class="az_board_basic" style="height:calc(100% - 170px);">
			<div data-ax5grid="prgGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
		</div>	
	</div>	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ProgramInfo_relat.js"/>"></script>
	