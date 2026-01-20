<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<head>
	<title>개발계획승인요청상세</title>
</head>

<% 
	request.setCharacterEncoding("UTF-8");
	String UserId = request.getParameter("user"); 
    String SRId = request.getParameter("srid"); 
    String AcptNo = request.getParameter("acptno");
    String codeList = request.getParameter("codeList");
%> 
<form name="getReqData">
	<input type="hidden" id="UserId" 	name="user" value=<%=UserId%>>
	<input type="hidden" id="SRId" 	name="SRId" value=<%=SRId%>>
	<input type="hidden" id="AcptNo" 	name="acptno" value=<%=AcptNo%>>
	<input type="hidden" name="codeList" value=<%=codeList%>>
</form>



<!-- contener S -->
<body style="padding: 10px !important; width: 100% !important; min-width: 0px !important;">
    <div class="content">
        <!-- history S-->
        <div id="history_wrap">SR승인 <strong>&gt; 개발계획승인요청</strong></div>
        <!-- history E-->         
	    
		<!-- 하단 S-->
		<div class="half_wrap margin-10-top" style="height: 430px;">
			<!-- tab S-->
			<div class="tab_wrap">
				<ul class="tabs">
					<li rel="SRRegister" id="tab1" class="btnTab">SR정보</li>
					<li rel="SRReceive" id="tab2" class="btnTab">SR접수</li>
					<li rel="DevPlan" id="tab3" class="btnTab on">개발계획</li>
				</ul>
			</div>
			<!-- tab E-->
				<div id="tabSRRegister" class="tab_content" style="width:100%; height: 385px;">
		       		<iframe id="frmSRRegister" src='' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabSRReceive" class="tab_content" style="width:100%; height: 385px;">
		       		<iframe id="frmSRReceive" src='' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<div id="tabDevPlan" class="tab_content on" style="width:100%; display: block;  height: 385px;">
		       		<iframe id="frmDevPlan" src='' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		   	</div>
		</div>
		<div class="width-100 float-right margin-10-top tar">
			<div class="dib" style="width: calc(100% - 312px); display:none;" id="confBox">
				<label>결재/반송의견</label>
				<input id=txtConMsg type="text" style="width: calc(100% - 88px);">
			</div>
			<div class="dib">
				<button class="btn_basic_s" id="masterCncl" disabled=true>관리자회수</button>
				<button class="btn_basic_s margin-2-left" id="btnApprovalInfo">결재정보</button>
				<button class="btn_basic_s margin-2-left" id="cmdOk" disabled=true>결재</button>
				<button class="btn_basic_s margin-2-left" id="cmdCncl" disabled=true>반려</button>
				<button class="btn_basic_s margin-2-left margin-10-right" id="btnClose">닫기</button>
			</div>
		</div>
</body>
<!-- contener E -->
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopSRDevPlanInfo.js"/>"></script>