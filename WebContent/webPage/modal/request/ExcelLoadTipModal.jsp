<!--
화면 명: 프로그램종류정보
화면호출:
1) 체크인-프로그램일괄등록 -> 작성요령 모달
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
textarea {
	box-sizing: border-box;
	border: 1px solid #ddd;
	font-size: 12px;
	resize: none;
}
</style>
<body style="width: 100% !important; min-width: 820px !important">
<div class="pop-header">
	<div>
		<label id="lbSub">[ 시스템에 따른 일괄등록 연관정보 ]</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div>
</div>
<div class="container-fluid pop_wrap">
	<div class="row">
		<label class="tit_50 poa">시스템</label>
	    <div class="ml_50"><input type="text" id="txtSystem" style="width:100%;" readonly/></div>
	</div>
	<div class="row">
		<div class="l_wrap width-50">
			<div class="margin-5-right">
<!--                 <label class="tit_80" style="color:#ff0000;font-size: 15px;">항목설명</label> -->
<!--                 <textarea id="txtItem" style="width:100%;height:370px;padding-left: 10px;" readonly></textarea> -->
				<div class="az_board_basic" style="height: 85%;">
					<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%;margin-top:5px;"></div>
				</div>
			</div>
		</div>
		<div class="r_wrap width-50" >
			<div class="margin-5-left">
<!--                 <label class="tit_100" style="color:#ff0000;font-size: 15px;">체크결과 설명</label> -->
<!--                 <textarea id="txtChkRst" style="width:100%;height:370px;padding-left: 10px;" readonly></textarea> -->
				<div class="az_board_basic" style="height: 85%;">
	    			<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%;margin-top:5px;"></div>
				</div>
			</div>
		</div>
	</div>
	<div class="row tar">
		<button id="btnClose" class="btn_basic_s" style="margin-top:10px;">닫기</button>
	</div>
</div>
<!-- contener E -->
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ExcelLoadTipModal.js"/>"></script>