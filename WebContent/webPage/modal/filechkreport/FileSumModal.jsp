<!--  
	* 화면명: 대사내용합계표
	* 화면호출: 대사기록조회 -> 대사기록리스트 더블클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<div class="pop-header">
	<div>
		<label id="lbSub">대사내용합계표</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid">
	<div class="row">
		<div class="az_board_basic" style="height: calc(100% - 78px);">
			<div data-ax5grid="fileSumGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
		</div>
	</div>
	
	
	<div class="row" style="overflow: hidden; margin-top: 15px;">
		<div class="l_wrap">
			<label>더블클릭 : 대사내용 조회</label>
		</div>
		<div class="r_wrap">
			<div style="margin-right: 5px;">
				<button id="btnExcel" class="btn_basic_s dib" style="margin-right: 5px">엑셀저장</button>
				<button id="btnClose" class="btn_basic_s dib">닫기</button>
			</div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/filechkreport/FileSumModal.js"/>"></script>