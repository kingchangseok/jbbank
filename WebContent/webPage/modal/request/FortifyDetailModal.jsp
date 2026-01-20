<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>세부취약점 조치방법</label>
		</div>
	</div>
	<div style="overflow-y: hidden; padding: 5px;">
		<div class="az_search_wrap" style="padding: 10px; height: 590px;">
			<div class="row">
				<div class="width-100 dib vat">
					<label class="title tit_80 poa">취약점명</label>
					<input class="vat" id="txtName" type="text" autocomplete="off" readonly="readonly" style="width: 85%;">
				</div>
			</div>
			<div class="row">
				<div class="width-100 dib vat margin-5-top">
					<label class="title tit_80 poa">취약점 설명</label>
					<div class="dib vat" style="width: 85%">
						<textarea id="txtDetail" name="txtSayu" class="width-100" rows="14" cols="88" style="margin-top: 5px; padding:5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;" readonly></textarea>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-100 dib vat margin-5-top">
					<label class="title tit_80 poa">조치 방법</label>
					<div class="dib vat" style="width: 85%">
						<textarea id="txtStory" name="txtSayu" class="width-100" rows="14" cols="88" style="margin-top: 5px; padding:5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;" readonly></textarea>
					</div>
				</div>
			</div>
			<div class="float-right dib">
				<button id="btnClose" class="btn_basic_s" style="margin: 15px 5px;">닫기</button>
			</div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/FortifyDetailModal.js"/>"></script>