<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<body style="width: 100% !important; min-width: 0px !important;">
	<div class="pop-header">
		<div>
			<label>[사용자업무이력조회]</label>
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
					<label class="tit_80 poa">조회조건</label>
			        <div class="ml_80">
						<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90 dib"></div>
					</div>
				</div>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-70">
		 		<div class="margin-5-left">
					<div class="margin-5-left">
						<label class="tit_80 poa">사용자</label>
				        <div class="ml_80">
				        	<div class="width-80">
					        	<input id="txtUser" type="text" class="dib width-50">
				        	</div>
						</div>
					</div>
			        <div class="ml_80">
						<div class="vat dib poa_r">
							<button id="btnQry" class="btn_basic_s">조회</button>
							<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
							<button id="btnExit" class="btn_basic_s margin-5-left" >닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
					
		<div class="row">
			<div class="az_board_basic" style="height: 82%;">
		    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/RgtChgModal.js"/>"></script>