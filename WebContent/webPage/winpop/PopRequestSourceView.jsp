<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecams.common.base.StringHelper"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
 
<link type="text/css" rel="stylesheet" href="/vendor/highlight/styles/vs.css" />
<script type="text/javascript" src="/vendor/highlight/highlight.pack.js"></script>
<style id="style">
	.context-menu{
		 position: absolute;
		 visibility:hidden;
		 border: 1px solid #888888;
		 border-radius: 3px;
		 background: white;
		 box-shadow: 5px 5px 5px #888888;
	}
	.context-menu ul{
		list-style: none;
		padding: 2px;
	}
	.context-menu ul li{
		padding: 8px 5px;
		margin-bottom: 3px;
		color: black;
		font-weight: bold;
		font-size: 12px;
	}
	.context-menu ul li:hover{
		cursor: pointer;
		background: #90E4FF;
	}
	ul.tabs li{
		text-overflow: ellipsis !important;
	    white-space: nowrap !important;
	    overflow: hidden !important; 
	}
}
</style>
<%
	request.setCharacterEncoding("UTF-8");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
    String codeList = StringHelper.evl(request.getParameter("codeList"),"");
%>
<c:import url="/webPage/common/common.jsp" />

<!-- contener S 
<div id="wrapper">
    <div class="content">-->
<body style="padding: 10px !important; width: 100% !important; min-width: 1150px !important;">
    <div class="content">
    	<div id="history_wrap">	프로그램 <strong>&gt; 신청건별 소스View</strong></div>
        <!-- line1 S-->    
		<!--  -->
		<div class="l_wrap width-30">
			<div class="margin-5-right">
				<div class="az_search_wrap">
					<div class="az_in_wrap">
						<div class="row">
							<label id="lbAcptno" class="tit_60 poa">요청번호</label>
							<div class="ml_60">
								<input id="Txt_Acptno" name="Txt_Acptno" type="text" class="width-100" readonly>
							</div>
						</div>
					</div>
				</div>
				<div class="half_wrap" style="margin-top: 0px;">
					<div class="l_wrap width-100">
					    <div class="az_board_basic" style="height:86%;">
					    	<div data-ax5grid="grdProgHistory" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
						</div>	
					</div>
				</div>
			</div>
		</div>
		<div class="l_wrap width-70">
			<div class="margin-5-left">
				<div class="margin-5-top tar dib">
					<input id="optWord"  type="radio" name="optradio" value="W" onchange="optradio_change();"/>
					<label for="optWord">단어검색</label>
					<input id="optLine" type="radio" name="optradio" value="L" onchange="optradio_change();" checked/>
					<label for="optLine">라인검색</label>
					<input id="txtSearch" name="txtSearch" type="text" style="width:200px;" placeholder="검색할 라인번호를 입력하세요.">
					<button id="btnSearch" name="btnSearch" class="btn_basic margin-5-left por" style="height:25px;padding: 0 7px 2px;" >찾기</button>
				</div>
				
				<div class="margin-5-top poa_r dib">
					<button id="btnClose" name="btnClose" class="btn_basic margin-5-left por" style="height:25px;padding: 0 7px 2px;" >닫기</button>
				</div>
			</div>
			
			<div class="margin-5-left">
				<!-- tab S-->
				<div class="margin-5-top">
					<div class="tab_wrap">
						<ul class="tabs" id="tabs">
							
						</ul>
					</div>
					<!-- tab E-->
					<div style="height:88%" id="content"> <!--  tab_container -->

				   	</div>
				</div>
			</div>
		</div>
	</div>
</body>
<!-- </div> --> 
<form name="getSrcData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
	<input type="hidden" name="codeList" value=<%=codeList%>>
</form>

<div id="context-menu" class="context-menu"> 
 	<ul> 
 		<li id="contextmenu1" onclick="contextmenu_click('1');">탭삭제</li> 
 		<li id="contextmenu2" onclick="contextmenu_click('2');">소스복사</li> 
 		<li id="contextmenu3" onclick="contextmenu_click('9');">소스저장</li>
 	</ul> 
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopRequestSourceView.js"/>"></script>