<!--  
	* 화면명: 부재등록
	* 화면호출: 기본관리 > 기본정보 > 부재설정하기
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>부재 등록/해제</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	
    <iframe id="ifmDaeGyul" src='/webPage/mypage/AbsenceRegister.jsp' width='100%' height='90%' frameborder="0" scrolling="no" onload="frameOnload()"></iframe>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/SetDaeGyulModal.js"/>"></script>