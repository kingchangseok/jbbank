<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="height: 95% !important">
	<div>
		<div class="row">
			<label class="tit_80 poa">분류유형</label>
			<div class="ml_80 vat">
				<div id="cboCattype" data-ax5select="cboCattype" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-30 dib" ></div>
				<button id="btnReq" class="btn_basic_s margin-15-left">등록</button>
			</div>
		</div>
	</div>
	<!-- cell2 -->
	<div class="row">
		<label class="tit_80 poa">신청구분</label>
		<div class="ml_80 dib scrollBind" style="height:calc(100% - 50px); width:calc(100% - 86px);">
			<ul class="list-group" id="ulReqInfo"></ul>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/SRTypeManageTab.js"/>"></script>