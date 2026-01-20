<!--  
	* 화면명: 대결범위등록
	* 화면호출: 결재정보 -> 대결범위등록 클릭
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
	<div>
		<label id="lbSub">대결가능범위등록</label>
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
		<div class="l_wrap width-35">
			<div class="margin-5-right">
				<div>
					<input id="rdoPos" type="radio" name="radio"/>
					<label for="rdoPos">직위</label>
					<input id="rdoRgt" type="radio"  name="radio"/>
					<label for="rdoRgt">직무</label>
					<input type="checkbox" class="checkbox-pie" id="chkTeam" data-label="동일팀내에서만 가능"/>
				</div>
				<div class="row">
					<label class="tit_80 poa">직위/직무</label>
			        <div class="ml_80">
						<div id="cboPos" data-ax5select="cboPos" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100">
					    </div>
					</div>
				</div>	
				<div class="row por">
					<label class="title_s">대결가능범위</label><input type="checkbox" class="poa_r checkbox-pie" id="chkAll" data-label="전체선택"/>
					<div class="row scrollBind" style="width:100%; height:544px">
				     	<ul class="list-group" id="lstPos" style="height: 100%;"></ul>
					</div>
				</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-65">
	 		<div class="margin-5-left">
				<label class="title_s">등록리스트</label>
				<div class="row az_board_basic" style="width:100%; height:614px">
					<div data-ax5grid="grdRangeList" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="width:100%; height: 100%;"></div>
				</div>
			</div>
		</div>
	</div>
	<!--button-->
	<div class="row tac float-right">
		<button class="btn_basic_s" id="btnReg">등록</button>
		<button class="btn_basic_s" id="btnDel">폐기</button>
		<button class="btn_basic_s" id="btnQry">조회</button>
		<button class="btn_basic_s" id="btnClose">닫기</button>
	</div>
</div>
</body>
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/approvalInfo/RangeApprovalInfoModal.js"/>"></script>