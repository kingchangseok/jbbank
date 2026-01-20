<!-- 
화면 명: 파일업로드
화면호출:
1) 운영배포화면 -> 운영배포신청버튼 클릭
 -->
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
	<div class="row tac">
		<div class="progressbar" style="display:inline-block; width:calc(100% - 95px); border:none; display:none;">
			<dl class="srdl" style="margin:0; ">
				<dd style="cursor: pointer; text-align: left; margin-left:0px; margin-top: 3px;">
					<span class="blue" style="width:0%; display: block;" id="percent"></span>
					<span style="height:30px; text-align: center; width: 100%; display: inline-block; position: absolute; top: 0px;" id="percentText">0%</span>
				</dd>
			</dl>
		</div>
		<label class="poa" style="left: 10px; bottom: -25px;" id="limitByte">용량제한 : 500MB</label>
		<div class="float-right">
			<button class="btn_basic" id="btnFileUpload">추가</button>
			<button class="btn_basic margin-5-left"  id="btnFileDelete">삭제</button>
			<button class="btn_basic margin-5-left"  id="btnReq">선택완료</button>
		</div>
		<!-- <button class="btn_basic margin-5-left"  id="btnClose">닫기</button> -->
	</div>
</div>

<!-- contener E -->
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/fileupload/FileUpload.js"/>"></script>