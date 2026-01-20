<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<body style="width: 100% !important; min-width: 0px !important;">
	<div class="pop-header">
		<div>
			<label>파트별테스트담당자</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="">
		<!--line1-->					
		<div class="half_wrap_cb">
		</div>
		<!--line2-->
		<div class="row az_board_basic" style="height: 86%">
			<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<!--button-->
		<div class="row tar">
			<button id="btnDel" class="btn_basic_s margin-5-left">삭제</button>
			<button id="btnExit" class="btn_basic_s margin-5-left">닫기</button>
		</div>
	</div>

</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/TestManagerListModal.js"/>"></script>