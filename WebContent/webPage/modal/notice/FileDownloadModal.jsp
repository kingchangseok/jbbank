<!--  
	* 화면명: 첨부파일
	* 화면호출: 공지사항 -> 첨부파일클릭
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

<body style="width: 100% !important; min-width: 0px !important; overflow: hidden;">
	<div class="row">
		<div class="col-md-12 col-sm-12">
			<div style="position: relative; height: 300px; overflow: auto; display: block;">
				<div class="card">
					<ul class="list-unstyled p-2 d-flex flex-column col" id="files" style="height: 150px; overflow: scroll;">
						<li class="text-muted text-center empty">No files uploaded.</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12 col-sm-12 margin-5-top" >
			<button class="btn_basic_s" 
					style="float: right; background-color: #fff; border-color: #e4e5e7; color: #6a6c6f; margin-right: 10px;" 
					onclick="window.parent.fileDownloadModal.close();">
				닫기
			</button>
			<div class="filebox" id="drag-and-drop-zone" style="display: inline-block; float: right;"> 
				<label for="ex_file">파일첨부</label> 
				<input type="file" id="ex_file"> 
			</div>
		</div>
	</div>
	
	<ul id="files" style="display: none;">
	</ul>
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
<script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/FileDownloadModal.js"/>"></script>