<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important; overflow: hidden;">
	
	
	<div class="pop-header">
		<div>
			<label>문서확인</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" id="btnExit">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	
	<div class="container-fluid pop_wrap">
		<div class="row">
			<div>
				<div class="az_board_basic" style="height:70%">
			    	<div data-ax5grid="donwGrid" style="height: 100%;"></div>
				</div>
			</div>
		</div>
		<div class="row tar">
			<div class="filebox" id="drag-and-drop-zone" style="display: inline-block;"> 
				<input type="file" id="ex_file"> 
			</div>
			<button id="btnAllDw" class="btn_basic_s" style="display: none;">일괄다운로드</button>
			<button id="btnClose" class="btn_basic_s">닫기</button>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/fileupload/FileDownListModal.js"/>"></script>