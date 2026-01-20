<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="row half_wrap_cb">
	<div class="dib">
        <label class="dib">금지단어</label>
		<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
	</div>
</div>
<div class="row" style="height:calc(100% - 50px);">
	<textarea id="words" style="width: 100%; height: 75%; border: 1px solid #ddd"></textarea>
    <label class="dib font-red">*금지단어는 ,로 구분합니다.</label>
</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/PasswordProhibitWords.js"/>"></script>