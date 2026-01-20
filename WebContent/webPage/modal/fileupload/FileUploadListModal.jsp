<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 685px !important">
<div class="pop-header">
	<div>
		<label>첨부파일</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<!--line1-->
	<div class="az_board_basic az_board_basic_in" style="height:78%">
		<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
	</div>
	<!--button-->
	<div style="display:none;" id="fileSave"></div>
	<div class="row">
		<div class="progressbar dib" style="display:inline-block; width:calc(100% - 80px); border:none; display:none; height: 25px;">
			<dl class="srdl" style="margin:0; ">
				<dd style="cursor: pointer; text-align: left; margin-left:0px; height: 25px;">
					<span class="blue" style="width:0%; display: block; height: 25px;" id="percent"></span>
					<span style="height:25px; text-align: center; width: 100%; display: inline-block; position: absolute; top: 0px; line-height: 25px;" id="percentText">0%</span>
				</dd>
			</dl>
		</div>
		<label class="poa" style="left: 10px; bottom: -25px;" id="limitByte">용량제한 : 500MB</label>
		<div class="float-right">
			<button class="btn_basic_s" id="btnAdd">추가</button>
			<button class="btn_basic_s margin-5-left"  id="btnDel">삭제</button>
			<button class="btn_basic_s margin-5-left"  id="btnComplete">선택완료</button>
		</div>
		<!-- <button class="btn_basic margin-5-left"  id="btnClose">닫기</button> -->
	</div>
</div>

<!-- contener E -->
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/fileupload/FileUploadListModal.js"/>"></script>