<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.common.base.StringHelper"%>
 
<link type="text/css" rel="stylesheet" href="/vendor/highlight/styles/vs.css" />
<script type="text/javascript" src="/vendor/highlight/highlight.pack.js"></script>

<style>
	.text-red {
		color : red;
	}
</style>

<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
%>
<c:import url="/webPage/common/common.jsp" />

<!-- contener S -->
<body id="wrapper" style="padding: 10px;">
   <div class="content">
        <!-- history S-->
        <div id="history_wrap">프로그램<strong>&gt; 소스보기</strong></div>
        <!-- history E-->
        
        <!-- line1 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap por">
				<!-- 시스템 -->
                <div class="width-20 dib vat">
                    <label id="lbSystem" style="width:50px;" class=" poa">시스템</label>
                    <div style="margin-left: 50px;">
						<input id="txtSysMsg" name="txtSysMsg" type="text" class="width-95 dib" readonly >
					</div>
				</div>
			    <!-- 프로그램명 -->
                <div class="width-25 dib vat">
                    <label id="lbRsrcName" style="width:70px;" class="poa">프로그램명</label>
                    <div style="margin-left: 70px;">
						<input id="txtProgId" name="txtProgId" type="text" class="width-95 dib" readonly>
					</div>
				</div>
<!-- 				<div class="width-12 dib vat">
					<label id="lbCharacter" style="width:73px;" class="poa">Character Set</label>
					<div class="ml_80" id="divCharacter">
						<div id="cboCharacter" data-ax5select="cboCharacter" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 20px);" class="dib" ></div>
					</div>
				</div> -->
				<div class="width-55 dib vat">
				    <!-- 프로그램경로 -->
                    <div class="vat">
                        <label id="lbDirPath" class="tit_80 poa">프로그램경로</label>
						<div class="ml_80">
							<input id="txtDir" name="txtDir" type="text" class="width-100" readonly>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--line1 E-->
		<!-- line2 S-->
		<div>
			<div class="l_wrap width-100">
			    <div class="az_board_basic" style="height:150px;">
			    	<div data-ax5grid="grdProgHistory" style="height: 100%;"></div>
				</div>	
			</div>
		</div>
		<!-- line2 E-->
		<!-- line3 S-->
		<div class="tar width-100">
			<div class="dib vat margin-5-top margin-5-bottom float-left">
				<input type="checkbox" class='checkbox-pie' id="chkLine" style="width:90px;" data-label="Line표시" checked></input>
			</div>
			<div class="dib vat margin-5-top margin-5-bottom float-right" style="width: calc(100% - 100px)">
				<input id="optWord" type="radio" name="optradio" value="W" onchange="optradio_change();"/>
				<label for="optWord">단어검색</label>
				<input id="optLine" type="radio"  name="optradio" value="L" onchange="optradio_change();" checked/>
				<label for="optLine" id="lblLine">라인검색</label>
				<input id="txtSearch" name="txtSearch" type="text" style="width:200px;" onchange="txtSearch_change();">
				
				<button id="btnSearch" name="btnSearch" class="btn_basic_s margin-5-left">찾기</button>
				<!-- <button id="btnSrcCopy" name="btnSrcCopy" class="btn_basic_s margin-5-left">소스복사</button> -->
				<button id="btnSrcDown" name="btnSrcDown" class="btn_basic_s margin-5-left">소스다운</button>
				<button id="btnExit" name="btnExit" class="btn_basic_s margin-5-left">닫기</button>
			</div>
		</div>
		<!-- line4 S -->
		<div class="scrollBind width-100 margin-5-top" style="height:calc(100% - 260px);" id="htmlView" >
	    	<div id="sourceDiv">
			    <%-- <pre style="width:100%;height:100%;"><code id="htmlSrc1"></code></pre> --%>
	    	</div>
		</div>
		<!-- line4 E -->
	</div>
</body>
<form name="getSrcData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopSourceView.js"/>"></script>