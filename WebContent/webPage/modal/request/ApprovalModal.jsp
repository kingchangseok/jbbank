<!-- 
화면 명: 결재팝업
화면호출:
1) 운영배포화면 -> 운영배포신청버튼 클릭
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 820px !important">
<div class="pop-header">
	<div>
		<label id="lbSub">결재절차확정</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap" style="padding: 5px">
	<div class="row half_wrap_cb">
		<!--등록된결재선-->
		<div class="l_wrap width-50">
			<!-- <label class="tit_80 poa">등록된결재선</label>
			<div class="ml_80 vat">
				<div id="cboConf" class="dib width-97" data-ax5select="cboConf" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
			</div> -->
			<!-- <label class="poa tit_50 title">결재자</label>
			<div class="dib vat" style="display: none;">
               	<input id="optBase1"  type="radio" name="optBase" value="변경" checked/>
				<label for="optBase1">변경</label>
				<input id="optBase2" type="radio" name="optBase" value="추가"/>
				<label for="optBase2">추가</label>
               	<div class="poa_r" id="AddArea" style="display:none;">
               		<input type="text" id="txtName"/><button class="btn_basic_s margin-5-left" id="btnSearch">검색</button> 
               	</div>
               </div> -->
               
			<div class="width-100 dib vat">
				<input type="text" id="txtName" style="width: calc(100% - 113px)"/>
				<button class="btn_basic_s margin-5-left tit_50" id="btnSearch">검색</button>
				<button class="btn_basic_s tit_50" id="btnSel">선택</button>
			</div>
			
			<div class="row">
				<div class="az_board_basic az_board_basic_in" style="height:80%">
					<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
				</div>
			</div>
			<!-- 
			<div class="float-right">
				<button id="cmdDown" class="btn_basic" style="width:80px;font-size:12px;height: 29px;margin-top: 2px;margin-left: 2px; margin-right: 0px;">선택</button>
			</div>
			 -->
		</div>
		<!--결재절차-->
		<div class="r_wrap width-50">
			<div class="margin-5-left">
				<div class="width-100" style="height:25px;">
                    <label id="" class="tit_60 poa title">결재절차</label>
                    <div class="ml_60 tar" id="lblDel" style="display: none;"><span class="txt_r">[제외방법 : 목록에서 선택 후  결재삭제버튼 클릭]</span></div>
                    <!-- 
               		<button class="btn_basic_s margin-5-left float-right btnUpdate" id="btnDown" style="display:none;">단계 ▼</button> 
               		<button class="btn_basic_s margin-5-left float-right btnUpdate" id="btnUp" style="display:none;">단계 ▲</button>
               		--> 
				</div>				
				<div class="row">
					<div class="az_board_basic az_board_basic_in" style="height:80%">
		    			<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height:100%"></div>
					</div>
				</div>
			</div>
		</div>	
		
		<div class="row">
			<div class="width-100 dib vat" style="margin-top: 5px">
				<!-- <div class="l_wrap width-50">
					<input type="text" id="txtName" style="width: calc(100% - 113px)"/>
					<button class="btn_basic_s margin-5-left tit_50" id="btnSearch">검색</button>
					<button class="btn_basic_s tit_50" id="btnSel">선택</button>
				</div> -->
				<!-- <div class="width-50"> -->
					<div class="margin-5-left float-right">
						<!-- <div class="dib float-left">
							<p class="txt_r text-left" style="white-space: nowrap;">[추가방법 : 1)결재절차목록에서 추가 할 위치를 선택 2)좌측에서 결재자를 검색 3)목록에서 더블클릭 ]</p>
							<p class="txt_r text-left" style="white-space: nowrap;">[변경방법 : 1)결재절차목록에서 변경 할 대상을 선택 2)좌측의 목록에서 결재자를 더블클릭 ]</p>
						</div> -->
						<input type="checkbox" class='checkbox-pie' id="chkAll" style="width:90px;" data-label="전체선택(삭제대상)"></input>
						<div class="vat dib float-right">
							<button id="btnDel" class="btn_basic_s margin-5-left">결재삭제</button>
							<button id="btnReq" class="btn_basic_s">등록</button>
							<button id="btnClose" class="btn_basic_s">취소</button>
						</div>
					</div>
				<!-- </div> -->
			</div>
		</div>
	</div>
</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ApprovalModal.js"/>"></script>