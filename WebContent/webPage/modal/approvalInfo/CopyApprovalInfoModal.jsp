<!--  
	* 화면명: 결재정보복사
	* 화면호출: 결재정보 -> 결재정보복사 클릭
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label id="lbSub">결재정보복사</label>
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
				<label class="tit_80 poa">시스템[from]</label>
		        <div class="ml_80">
					<div id="cboFromSys" data-ax5select="cboFromSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
				    </div>
				</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-70">
	 		<div class="margin-5-left">
				<label class="tit_60 poa">결재종류</label>
		        <div class="ml_60">
					<div id="cboFromReqCd" data-ax5select="cboFromReqCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-30 dib">
					</div>
					<div class="vat dib ">
						<input type="checkbox" class="checkbox-pie" id="chkMember" data-label="직원"/>
				    	<input type="checkbox" class="checkbox-pie" id="chkOutsourcing" data-label="외주"/>
					</div>
					<button id="btnQry" class="btn_basic_s poa_r">조회</button>
				</div>
			</div>
		</div>
	</div>
	<!--line2-->				
	<div class="row">
		<div>
			<div class="az_board_basic" style="height:50%">
		    	<div data-ax5grid="grdApprovalInfo" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
	<!--line3-->	
	<div class="row">
		<label class="tit_80 poa">결재종류(To)</label>
	    <div class="ml_80">
			<div id="cboToReqCd" data-ax5select="cboToReqCd" data-ax5select-config="{size:'sm',theme:'primary',selSize:10}" class="width-20 dib">
			</div>
			<div class="vat dib">
				<input type="checkbox" class="checkbox-pie" id="chkAllCopy" data-label="결재정보전체복사"/>
			</div>
		</div>
	</div>	
	<!--line4-->	
	<div class="row">
		<label class="tit_80 poa">시스템(To)</label>
	    <div class="ml_80">
			<div class="vat dib">
				<input type="checkbox" class="checkbox-pie" id="chkNoSys" data-label="시스템관련없음"/>
				<input type="checkbox" class="checkbox-pie" id="chkAllSys" data-label="전체선택"/>
			</div>
		</div>
	</div>
	<!--line5-->	
	<div class="row">
		<div class="scrollBind" id="lstSysDiv">
 			<ul class="list-group" id="lstSys" style="height: 90%;">
  			</ul>
		</div>
	</div>
	<!--button-->
	<div class="row tac float-right">
		<button class="btn_basic_s" id="btnCopy">복사</button>
		<button class="btn_basic_s" id="btnClose">닫기</button>
	</div>
</div>
</body>
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/approvalInfo/CopyApprovalInfoModal.js"/>"></script>