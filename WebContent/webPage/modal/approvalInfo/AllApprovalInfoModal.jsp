<!--  
	* 화면명: 전체조회
	* 화면호출: 결재정보 -> 전체조회 클릭
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label id="lbSub">결재정보전체조회</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="btnClose_Click()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<!--line1-->					
	<div class="row half_wrap_cb">
		<!--left wrap-->
		<div class="l_wrap width-30">
			<div class="margin-5-right">
				<label class="tit_40 poa">시스템</label>
		        <div class="ml_40">
					<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
					</div>
				</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-70">
	 		<div class="margin-5-left">
				<label class="tit_60 poa">결재종류</label>
		        <div class="ml_60">
					<div id="cboReqCd" data-ax5select="cboReqCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-40 dib">
				    </div>
					<div class="vat dib">
						<input type="checkbox" class="checkbox-pie" id="chkMember" data-label="직원"/>
					    <input type="checkbox" class="checkbox-pie" id="chkOutsourcing" data-label="외주"/>
					</div>
					<div class="tac float-right">
						<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel" style="margin-left: 5px; margin-right: 0px;">엑셀저장</button>
						<button id="btnQry" class="btn_basic_s">조회</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--line2-->				
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:85%">
				<div data-ax5grid="grdAllApprovalInfo" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<!--button-->
	<div class="row tac float-right">
		<!-- <button id="btnQry" class="btn_basic_s">조회</button>  -->
		<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
	</div>
</div>
</body>
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/approvalInfo/AllApprovalInfoModal.js"/>"></script>