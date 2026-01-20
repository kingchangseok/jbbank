<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">관리자<strong>&gt; 파일대사환경설정</strong></div>
	<div class="margin-10-top">
		<div class="tab_wrap">
			<ul class="tabs">
				<li rel="tabDefault" id="tab1Li" class="on">기본정보</li>
				<li rel="tabEct" id="tab2Li">예외디렉토리정보</li>
				<li rel="tabHand" id="tab3Li">수기파일대사</li>
			</ul>
		</div>
		
		<div class="margin-10-top">
	       	<div id="tabDefault" class="tab_content" style="width:100%">
	       		<iframe id="frmTabDefault" src='/webPage/tab/fileconfiguration/BasicInfoTab.jsp' width='100%' frameborder="0" style="height: calc(100% - 70px);"></iframe>
	       	</div>
	       	<div id="tabEct" class="tab_content" style="width:100%">
	       		<iframe id="frmTabEct" src='/webPage/tab/fileconfiguration/ExceptionDirInfoTab.jsp' width='100%' frameborder="0" style="height: calc(100% - 70px);"></iframe>
	       	</div>
	       	<div id="tabHand" class="tab_content" style="width:100%">
	       		<iframe id="frmTabHand" src='/webPage/tab/fileconfiguration/HandFileInfoTab.jsp' width='100%' frameborder="0" style="height: calc(100% - 70px);"></iframe>
	       	</div>
	   	</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/FileConfiguration.js"/>"></script>
	