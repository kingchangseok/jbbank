<!--  
	* 화면명: 공지사항등록/수정
	* 화면호출: 공지사항 -> 공지사항등록 클릭 또는 공지사항 리스트 더블클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub">FAQ 등록</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<div class="row">
				<div>
					<label id="lbSub" class="tit_60 poa">제목</label>
					<div class="ml_60">
						<input id="txtTitle" name="txtTitle" type="text" class="width-100"></input>
					</div>
				</div>
			</div>
			<!--CM_TITLE -->
			<div class="row">
					<label id="lbSub" class="tit_60 poa margin-15-top">내용</label>
				<div class="write_wrap ml_60">
					<textarea id="textareaContents" name="textareaContents" class=" width-100 margin-15-top" style="height: 255px; overflow-y:auto; padding: 5px;"></textarea>
				</div>
			</div>
			<div class="row">
				<div class="r_wrap" style="margin-top:2px;">
					<button id="btnFile1" name="btnFile" class="btn_basic_s">파일첨부</button>
					<button id="btnFile2" name="btnFile" class="btn_basic_s">첨부파일</button>
					<button id="btnReplyReg" name="btnReplyReg" class="btn_basic_s hide">답글등록</button>
					<button id="btnReg" name="btnReg" class="btn_basic_s">등록</button>
					<button id="btnReply" name="btnReply" class="btn_basic_s dib hide">답글</button>
					<button id="btnDel" name="btnDel" class="btn_basic_s dib">삭제</button>
					<button id="btnClose" name="btnClose" class="btn_basic_s dib">닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/notice/QuestionModal.js"/>"></script>