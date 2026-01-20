<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">프로그램 <strong>&gt; 빌드/릴리즈정보</strong></div>

	<div class="tab_wrap">
		<ul class="tabs">
			<li rel="tab1" id="tab1Li" class="on">1.내 기본정보 보기</li>
        	<li rel="tab2" id="tab2Li">2.개발환경설정</li>
        	<li rel="tab3" id="tab3Li">3.My결재선설정</li>
        	<li rel="tab4" id="tab4Li">4.SVN INFO</li>
		</ul>
	</div>
	<div>
      	<!-- 내 기본정보 보기 -->
       	<div id="tab1" class="tab_content" style="padding-top: 0px;">
       		<iframe src='/webPage/tab/mypage/MyInfoView.jsp' width='100%' style="height: calc(100% - 60px)" frameborder="0"></iframe>
       	</div>
       	
       	<!-- 개발환경설정 -->
       	<div id="tab2" class="tab_content" style="padding-top: 0px;">
       		<iframe src='/webPage/tab/mypage/DevelopEnvionment.jsp' width='100%' style="height: calc(100% - 60px)" frameborder="0"></iframe>
       	</div>
       	
       	<!-- My결재선설정 -->
       	<div id="tab3" class="tab_content" style="padding-top: 0px;">
       		<iframe src='/webPage/tab/mypage/MyApproval.jsp' width='100%' style="height: calc(100% - 60px)" frameborder="0"></iframe>
       	</div>
       	
       	<!-- SVN INFO -->
       	<div id="tab4" class="tab_content" style="padding-top: 0px;">
       		<iframe src='/webPage/tab/mypage/SVN_INFO.jsp' width='100%' style="height: calc(100% - 60px)" frameborder="0"></iframe>
       	</div>
       	
	</div>
</div>
       
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/MyInfo.js"/>"></script>