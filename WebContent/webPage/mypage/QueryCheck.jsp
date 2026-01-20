<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.mt-12{
	margin-top: 12px;
}

.width-385 {
	width: 385px;
}

textarea {
	height: 600px;
    margin: 0px;
    width: 100%;
    border: 1px solid #ddd;
    resize: none;
    padding: 5px;
}

.wCheck-theme-blue {
	margin-top: 5px;
}
}
</style>
<div class="contentFrame">
	<div id="history_wrap">기본관리 <strong>&gt; 쿼리조회</strong></div>
	<div style="padding: 5px 10px 5px 10px;">
		<div class="row">
			<label class="dib">[조회 할 쿼리문 입력]</label>
			<input id="headerCheck" name="headerCheck" tabindex="8" type="checkbox" value="optCkOut" checked="checked"/>
			<label class="dib">Header출력</label>
		</div>
		<div>
			<textarea id="queryTxt"></textarea>
		</div>
		<div class="row">		
			<button class="btn_basic_s float-right" style="width: 50px;">조회</button>
			<label class="dib float-right" style="color: #000; margin-right: 10px;">조회버튼 클릭 시 결과가 있는 경우 Excel저장까지 진행됩니다.</label>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/QueryCheck.js"/>"></script>