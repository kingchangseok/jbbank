<!--  
	* 화면명: 조직도
	* 화면호출: 사용자정보 -> 조직정보등록 클릭/ 소속조직 더블클릭/ 소속(겸직) 더블클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label id="titleLabel">[테스트담당자담당파트]</label>
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
				<button id="btnPlus" class="btn_basic_s"><i class="fas fa-plus"></i></button>
				<button id="btnMinus" class="btn_basic_s margin-5-left"><i class="fas fa-minus"></i></button>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-70">
		 		<div class="tar">
		 			<input id="txtFind" type="text" class="width-70 dib" /> 
		 			<button id="btnFind" class="btn_basic_s margin-5-left">조회</button>
				</div>
			</div>
		</div>
		<!--line2-->				
		<div class="row">
			<div class="row scrollBind" style="height: 80%">
				<ul id="tvOrgani" class="ztree"></ul>
			</div>
		</div>
		<!--button-->
		<div class="row tar">
			<button class="btn_basic_s" id="btnReq">적용</button>
			<button class="btn_basic_s margin-5-left" id="btnExit">취소</button>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/PartManagerModal.js"/>"></script>