<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 사용자환경설정 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<!-- history S-->
	<div id="history_wrap">기본관리 <strong>&gt; 사용자환경설정</strong></div>

    <!-- tab S-->
    <div class="tab_wrap">
		<ul class="tabs">
			<li class="on" rel="tab1" id="tab1Li">1.개발환경설정</li>
			<!-- <li rel="tab2" id="tab2Li">2.My결재선설정</li> -->
		</ul>
	</div>
	<!-- tab E-->

	<div>
		<div id="tab1" class="tab_content">
<!-- 			<iframe id="frame1" src='/webPage/tab/userconfig/DevConfigTab.jsp' width='100%' frameborder="0" style="height: calc(100% - 65px);"></iframe> -->
		</div>
		<!-- <div id="tab2" class="tab_content"> -->
<!-- 			<iframe id="frame2" src='/webPage/tab/userconfig/MyApprovalTab.jsp' width='100%' frameborder="0" style="height: calc(100% - 50px);"></iframe> -->
		<!-- </div> -->
	</div>
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/UserConfig.js"/>"></script>