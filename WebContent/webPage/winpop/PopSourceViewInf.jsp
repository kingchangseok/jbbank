<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.util.StringHelper"%>
 
<link type="text/css" rel="stylesheet" href="/vendor/highlight/styles/vs.css" />
<script type="text/javascript" src="/vendor/highlight/highlight.pack.js"></script>

<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
%>
<c:import url="/webPage/common/common.jsp" />

<head>
	<title>소스보기</title>
	
<style type="text/css">
</style>
</head>
<!-- contener S -->
<body style="padding: 10px;">
   <div class="content" style="height: 100%;">
        <!-- history S-->
        <!-- <div id="history_wrap"></div> -->
        <div id="history_wrap"><span>프로그램</span><strong> &gt; </strong><strong>소스보기</strong></div>
        <!-- history E-->
        <!-- line1 S-->
        <div style="overflow: hidden;">
	        <div class="float-left width-55">
				<div class="az_search_wrap" style="margin-bottom: 0px;">
					<div class="az_in_wrap por" style="overflow: hidden;">
						<div class="width-100">
							<!-- 시스템 -->
			                <div class="width-100 vat">
			                	<div class="dib width-45">
				                    <label id="lbSystem" class=" poa">시스템</label>
				                    <div class="ml_80">
										<input id="txtSysMsg" name="txtSysMsg" type="text" class="width-95 dib" readonly >
									</div>
			                	</div>
			                	
							    <!-- 프로그램명 -->
				                <div class="width-55 vat dib">
				                    <label id="lbRsrcName" class="poa">프로그램명</label>
				                    <div class="ml_80">
										<input id="txtProgId" name="txtProgId" type="text" class="width-100 dib" readonly>
									</div>
								</div>
							</div>
							
							<!-- 프로그램경로 -->
		                    <div class="vat row width-100">
		                        <label id="lbDirPath" class="tit_80 poa">프로그램경로</label>
								<div class="ml_80">
									<input id="txtDir" name="txtDir" type="text" class="width-100" readonly>
								</div>
							</div>
							
						    <!-- 버전 -->
			                <div class="width-100 vat row">
			                    <label id="lbRsrcName" class="poa">버전</label>
			                    <div class="ml_80">
									<div id="cboVersion" data-ax5select="cboVersion" class="width-100" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
								</div>
							</div>
							
							<div class="width-100 row vat">
			                   <div class="vat dib width-100">
			                       <label id="lbDirPath" class="tit_80 poa" >요청사유</label>
									<div class="ml_80">
										<textarea id="txtSayu" style="border: 1px solid #ddd; align-content:flex-start; width:100%; height:85px; resize: none; overflow-y:auto; padding:5px;" readonly="readonly"></textarea>
									</div>
								</div>
								
							</div>
						</div>
					</div>
				</div>
				<!-- 버전 -->
		        <div class="width-100 vat row">
		           <div class="width-100" style="font-size: 0px;" >
						<input id="optWord"  type="radio" name="optradio" value="W" onchange="optradio_change();"/>
						<label for="optWord">단어검색</label>
						<input id="optLine" type="radio"  name="optradio" value="L" onchange="optradio_change();" checked/>
						<label for="optLine">라인검색</label>
						<input id="txtSearch" name="txtSearch" type="text" style="width:calc(100% - 198px);" >
						<button id="btnSearch" name="btnSearch" class="btn_basic_s margin-5-left">찾기</button>
					</div>
				</div>
			</div>
			<div class="float-right width-45 padding-5-left">
				<div>
					<label class="poa">상세프로그램내역</label>
					<div class="ml_100 tar">
						<button id="btnSrcCopy" name="btnSrcCopy" class="btn_basic_s " style="width: 70px;">소스복사</button>
						<button id="btnSrcDown" name="btnSrcDown" class="btn_basic_s margin-5-left" style="width: 70px;">소스다운</button>
						<button id="btnExit" name="btnExit" class="btn_basic_s margin-5-left" style="width: 70px; ">닫기</button>
					</div>
				</div>
			    <div class="az_board_basic row" style="height:196px;">
			    	<div data-ax5grid="first-Grid" style="height: 100%;"></div>
				</div>
			</div>
		</div>
		<!-- line4 S -->
		<div class="scrollBind" style="height:calc(100% - 280px); overflow:auto; margin-top: 12px;" id="htmlView"  >
	    	<div id="sourceDiv">
			    <pre style="width:100%;height:100%;"><code id="htmlSrc1"></code></pre>
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
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopSourceViewInf.js"/>"></script>