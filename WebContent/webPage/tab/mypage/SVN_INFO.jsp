<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame" style="padding-top: 170px;">

	<div id="divContent" >
		<div class="row">
			<label class="tit_100 dib poa" style="margin-left: 50px">SVN ID</label>
			<div class="ml_60">
				<input id="UpdateId" name="Update_id" class="width-60" style="margin-left: 120px">
			</div>
		</div>
		<div class="row">
			<label class="tit_100 dib poa" style="margin-left: 50px">SVN PASSWORD</label>
			<div class="ml_60">
				<input type="password" id="UpdatePw" name="Update_pw" class="width-60" style="margin-left: 120px">
			</div>
		</div>
	</div>
	    
	<div id="divBtn" style="text-align:right; width:580px; margin: 0 auto; margin-top:15px;">
		<button id="btnSave" name="btnSave" class="btn_basic_s">저장</button>
	</div>

</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/mypage/SVN_INFO.js"/>"></script>