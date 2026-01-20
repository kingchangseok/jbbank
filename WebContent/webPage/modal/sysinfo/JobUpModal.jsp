<!--  
	* 화면명: 업무정보 편집
	* 화면호출: 업무정보 -> 새로만들기/편집 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label id="lblTitle">[업무정보편집]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1 S-->
		<div class="row">					
			<div class="width-30 dib vat"> 
	            <label class="title">업무코드</label>
			</div>
			<div class="width-68 dib vat">
	            <label class="title">업무명</label>
			</div>
		</div>
		
		<div class="row">					
			<div class="width-30 dib vat"> 
	           <input id="txtCode" name="txtCode" class="width-100" type="text" maxlength="6"></input>
			</div>
			<div class="width-68 dib vat">
	            <input id="txtVal" name="txtVal" class="width-100" type="text"></input>
			</div>
		</div>
		
		<div class="row tar">
			<button id="btnJobUp" class="btn_basic_s">적용</button>
			<button id="btnJobUpClose" class="btn_basic_s margin-5-right">취소</button>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/JobUpModal.js"/>"></script>