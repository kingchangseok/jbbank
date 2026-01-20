<!--  
	* 화면명: 첨부파일
	* 화면호출: 공지사항 -> 첨부파일클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important; overflow: hidden;">
	
	
	<div class="pop-header">
		<div>
			<label>공지사항 첨부파일</label>
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
				<label id="lblAdd" for="ex_file">파일첨부</label> 
				<input type="file" id="ex_file"> 
			</div>
			<button id="btnDel" class="btn_basic_s" style="margin-left: -6px;">파일삭제</button>
			<button id="btnAllDw" class="btn_basic_s" style="display:none;">일괄다운로드</button>
			<button id="btnClose" class="btn_basic_s">닫기</button>
		</div>
	</div>
</body>
<!-- File item template -->
<script type="text/html" id="files-template">
<li class="media">
	<div class="media-body mb-1">
		<p>
			<strong>%%filename%%</strong> - Status: <span class="text-muted">Waiting</span>
			<button href="#" class="btn btn-sm btn-danger cancel" role="button" style="float: right;">삭제</button>
		</p>
		<div class="progress mb-2">
        	<div class="progress-bar progress-bar-striped progress-bar-animated bg-primary" role="progressbar" style="width: 0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
        	</div>
      	</div>
		<hr class="mt-1 mb-1" />
	</div>
</li>
</script>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script src="<c:url value="/scripts/fileupload/jquery.dm-uploader.min.js"/>"></script>
<script src="<c:url value="/scripts/fileupload/demo-ui.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/FileDownloadModalQnA.js"/>"></script>