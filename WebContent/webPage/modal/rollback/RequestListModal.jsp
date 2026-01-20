<!--  
	* 화면명: 체크리스트항목등록 모달
	* 화면호출: 체크리스트 메뉴트리 우클릭 -> 항목등록/수정
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub" class="margin-5-left">[신청건 선택]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose();">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<!--CM_TITLE -->
			<div class="row" style="text-align: right;">
				<label class="poa" style="left: 5;">[검색조건]</label>
				<div class="dib">
					<label class="tit_80 poa text-right">조회기간</label>
					<div class="dib ml_90" id="divPicker">
						<div data-ax5picker="basic" class="az_input_group dib vat">
							<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:80px;" class="f-cal" autocomplete="off">
							<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<span class="sim">∼</span>
						<div data-ax5picker="basic2" class="az_input_group dib vat">
							<div class="dib vat">
								<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:80px;" class="f-cal" autocomplete="off">
								<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
					</div>
					<button class="btn_basic_s" id="cmdFind">검색</button>
				</div>
			</div>
			<div class="row">
				<div>
					<div data-ax5grid="first-Grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:calc(100% - 100px);"></div>
				</div>
			</div>
		</div>
		<div class="margin-5-top">
			<div class="margin-5-left">
				<div class="tar">
					<button class="btn_basic_s" id="cmdReq">선택</button>
					<button class="btn_basic_s" id="cmdExit">닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/rollback/RequestListModal.js"/>"></script>
