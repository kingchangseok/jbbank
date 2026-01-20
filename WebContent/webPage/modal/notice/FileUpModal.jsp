<!--  
	* 화면명: 공지사항 파일첨부
	* 화면호출: 공지사항등록 -> 파일첨부 클릭
			   공지사항-> 첨부파일 클릭 
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="<c:url value="/css/ecams/common/ecamsStyle.css" />">
<!-- Bootstrap core CSS -->
<link href="<c:url value="/styles/bootstrap4.0.css"/>" rel="stylesheet">

<!-- Custom styles -->
<link href="<c:url value="/styles/fileupload/jquery.dm-uploader.min.css"/>" rel="stylesheet">
<link href="<c:url value="/styles/fileupload/styles.css"/>" rel="stylesheet">
<link rel="stylesheet" href="<c:url value="/styles/ax5/ax5dialog.css"/>">
<style>

</style>


<body style="width: 100% !important; min-width: 0px !important; overflow: hidden;">
	<div class="row">
		<div class="col-md-6 col-sm-12">
			<div class="card">
				<ul class="list-unstyled p-2 d-flex flex-column col" id="files" style="height: 150px; overflow: scroll;">
					<li class="text-muted text-center empty">No files uploaded.</li>
				</ul>
			</div>
		</div>
	</div><!-- /file list -->
	
	<div class="row">
		<div class="col-md-6 col-sm-12 ">
			<div class="float-right">
				<div class="filebox" id="drag-and-drop-zone" style="display: inline-block;"> 
					<label for="ex_file">파일첨부</label> 
					<input type="file" id="ex_file"> 
				</div>
				<button id="selectBtn" class="btn_basic_s" style="padding-top: 5px;">선택완료</button>
			</div>
		</div>
		
		<div class="col-sm-3">
			<button  id="btnStartUpload" class="btn btn-sm btn-primary start" role="button" style="display:none;">Start</button>
		</div>
	</div>	
</body>

<!-- <button id="btnStartUpload" type="button" style="display: none;">start</button> -->


<!-- File item template -->
<script type="text/html" id="files-template">
<li class="media">
	<div class="media-body mb-1">
		<p>
			<strong>%%filename%%</strong> - Status: <span class="text-muted status">Waiting</span> - Size: <span class="text-muted">%%filesize%%</span>
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

<%-- 
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/FileUpModal.js"/>"></script>

 --%>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script src="<c:url value="/scripts/fileupload/jquery.dm-uploader.min.js"/>"></script>
<script src="<c:url value="/scripts/fileupload/demo-ui.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/FileUpModal.js"/>"></script>