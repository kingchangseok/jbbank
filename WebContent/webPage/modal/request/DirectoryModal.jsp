<!--
	* 화면명: 디렉토리
	* 화면호출: 체크인-디렉토리
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.ztree li {
	font-size: 12px;
}
</style>
<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>디렉토리 선택</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose(false)">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>
	</div>
	<div class="container-fluid pop_wrap">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row vat cb">
					<label class="tit_50 poa">시스템</label>
					<div class="ml_50">
						<input id="txtSys" type="text" style="width: 100%" readonly/>
					</div>
				</div>
				<div class="row vat cb">
						<input type="checkbox" class='checkbox-pie' id="chkDir" data-label="하위폴더포함조회"></input>
				</div>
				<div class="row vat cb">
					<div class="scrollBind dib" style="height:70%; width: 100%;">
						<ul id="tvOrgani" class="ztree"></ul>
					</div>
				</div>
				<div class="row vat cb">
					<label class="dib" id="lbPath">/</label>
				</div>
				<div class="row vat cb">
					<label class="dib">디렉토리선택 후 더블클릭하면 선택이 완료됩니다.</label>
					<button id="btnClose" class="btn_basic_s dib" style="width: 70px;float: right;">닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/DirectoryModal.js"/>"></script>