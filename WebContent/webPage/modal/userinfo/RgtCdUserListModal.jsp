<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<body style="width: 100% !important; min-width: 0px !important;">
	<div class="pop-header">
		<div>
			<label>[담당직무사용자조회]</label>
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
			<div class="row">
				<div class="az_board_basic" style="height: 82%;">
			    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
		</div>		
		
		<div class="row">
			<div class="ml_80">
				<div style="float: right;">
					<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
					<button id="btnExit" class="btn_basic_s margin-5-left" >닫기</button>
				</div>
			</div>
		</div>		
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/RgtCdUserListModal.js"/>"></script>