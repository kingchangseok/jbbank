<!--  
	* 화면명: 사용자직무조회
	* 화면호출: 사용자정보 -> 사용자직무조회 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body>
	<div class="pop-header">
		<div>
			<label>[사용자직무조회]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1-->					
		<div class="half_wrap_cb">
			<!--left wrap-->
			<div class="l_wrap width-30">
				<div class="margin-5-right">
					<label class="tit_80 poa">시스템</label>
			        <div class="ml_80">
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
					</div>
				</div>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-70">
		 		<div class="margin-5-left">
					<label class="tit_80 poa">업무</label>
			        <div class="ml_80">
			        	<div class="width-80">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 1px);"></div>
			        	</div>
						<!--button-->
						<div class="vat dib poa_r">
							<button id="btnQry" class="btn_basic_s">조회</button>
							<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--line2-->					
		<div class="row half_wrap_cb">
			<!--left wrap-->
			<div class="l_wrap width-30">
				<div class="margin-5-right">
					<label class="tit_80 poa">직무</label>
			        <div class="ml_80">
						<div id="cboRgtCd" data-ax5select="cboRgtCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
					</div>
				</div>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-70">
		 		<div class="margin-5-left">
					<label class="tit_80 poa">사용자</label>
			        <div class="ml_80">
			        	<div class="width-80">
				        	<input id="txtUser" type="text" class="dib width-50">
							<div id="cboUser" data-ax5select="cboUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(50% - 5px);"></div>
			        	</div>
						<div class="vat dib poa_r">
							<button id="btnExit" class="btn_basic_s margin-5-left" >닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--line2-->				
		<div class="row">
			<div class="az_board_basic" style="height: 82%;">
		    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
</body>	
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/RgtCdModal.js"/>"></script>