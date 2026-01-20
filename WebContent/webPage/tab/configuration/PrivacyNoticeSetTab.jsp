<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
textarea {
	height: calc(100% - 70px);
    margin: 0px;
    width: 100%;
    border: 1px solid #ddd;
    resize: none;
    padding: 5px;
}
</style>

<div style="padding: 10px 20px 10px 20px;">
	<div class="row">
		<label class="dib">개인정보처리 유의사항</label>
		<label class="dib float-right" id="byteInfo">/2000 Byte</label>
	</div>
	<div>
		<textarea id="txtInfonotiMsg"></textarea>
	</div>
	<div class="dib float-right margin-5-top">
		<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/PrivacyNoticeSetTab.js"/>"></script>