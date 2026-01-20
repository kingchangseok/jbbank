<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.common.base.StringHelper"%>
<%
	request.setCharacterEncoding("UTF-8");

	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
	String syscd = StringHelper.evl(request.getParameter("syscd"),"");
	String sysmsg = StringHelper.evl(request.getParameter("sysmsg"),"");
	String jobcd = StringHelper.evl(request.getParameter("jobcd"),"");
 	String rsrccd = StringHelper.evl(request.getParameter("rsrccd"),"");
	String rsrcname = StringHelper.evl(request.getParameter("rsrcname"),"");
	String dirpath = StringHelper.evl(request.getParameter("dirpath"),"");
	String rgtList = StringHelper.evl(request.getParameter("rgtList"),"");
%>
<c:import url="/webPage/common/common.jsp" />


<body style="width: 100% !important; padding: 10px;">
    	<div class="content"> 
        <div id="history_wrap">프로그램<strong>&gt;프로그램정보</strong></div>
		<!-- 하단 S-->
		<div class="az_search_wrap" style="height:500px;">
			<div class="az_in_wrap" style="height:100%;">
				<!-- tab_기본정보 S-->
				<div class="tab_wrap">
					<ul class="tabs">
						<li rel="tab1" id="tab11" class="on" style="vertical-align: middle;">기본정보</li>
						<li rel="tab2" id="tab22" style="vertical-align: middle;">변경내역</li>
						<div class="r_wrap">
							<button id="btnExit" name="btnExit" class=btn_basic_s style="float:right" data-lang="EXIT">닫기</button>
						</div>
					</ul>
				</div>
				<!-- tab E-->			
				<div> <!--  tab_container -->
			       	<!-- 프로그램기본정보 -->
			       	<div id="tab1" class="tab_content" style="width:100%; height: calc(100% - 33px);">
			       		<iframe id="frmProgBase" name="frmProgBase" src='/webPage/tab/programinfo/ProgBaseTab.jsp' width='100%' height='100%' scrolling="no" ></iframe> <!-- /webPage/tab/programinfo/ProgBaseTab.jsp -->
			       	</div>
			       	<!-- 프로그램기본정보  END -->
		       		<div id="tab2" class="tab_content" style="width:100%; height: calc(100% - 33px);">
			       		<iframe id="frmProgHistory" name="frmProgHistory" src='/webPage/tab/programinfo/ProgHistoryTab.jsp' width='100%' style="height: 100%;" scrolling="no"></iframe> <!-- /webPage/tab/programinfo/ProgBaseTab.jsp -->
			       	</div>
			   	</div>
			</div>
		</div>
    </div>
</body>
<form name="getSrcData">
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
	<input type="hidden" name="syscd" value="<%=syscd%>"/>
	<input type="hidden" name="sysmsg" value="<%=sysmsg%>"/>
	<input type="hidden" name="jobcd" value="<%=jobcd%>"/>
	<input type="hidden" name="rsrccd" value="<%=rsrccd%>"/>
	<input type="hidden" name="rsrcname" value="<%=rsrcname%>"/>
	<input type="hidden" name="dirpath" value="<%=dirpath%>"/>
	<input type="hidden" name="rgtList" value="<%=rgtList%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopProgramInfo.js"/>"></script>