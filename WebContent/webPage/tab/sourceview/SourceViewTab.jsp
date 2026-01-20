<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<%@ page import="com.ecams.common.base.StringHelper"%>
<link type="text/css" rel="stylesheet" href="/vendor/highlight/styles/vs.css" />
<script type="text/javascript" src="/vendor/highlight/highlight.pack.js"></script>
<style>
body{min-width: 0;}
.text-red{color: red;}
</style>

<div id="L_wrap width-70" style="overflow-y: hidden;">
	<div class="row por" id="divInfo">
		<div class="dib vat" style="width:38%">
			<label class="poa">&nbsp;&nbsp;프로그램명</label>
			<div class="ml_80">
				<input id="txtProgId" name="txtProgId" type="text" class="width-100" readonly="readonly">
			</div>
		</div>
		<div class="dib vat margin-10-left" style="width:38%">
			<label class="poa">기준프로그램</label>
			<div class="ml_80">
				<input id="txtBaseId" name="txtBaseId" type="text" class="width-100" readonly="readonly">
			</div>
		</div>
		<div class="dib vat margin-10-left" style="width:20.8%">
			<label class="poa">버전</label>
			<div class="margin-30-left">
				<input id="txtVer" name="txtVer" type="text" class="width-100" readonly="readonly" style="text-align:center;">
			</div>
		</div>
		<div class="dib vat margin-10-left" style="width:70%">
			<label class="poa">경로</label>
			<div class="margin-30-left">
				<input id="txtDirPath" name=txtDirPath type="text" readonly="readonly" style="text-align:left; width:-webkit-fill-available;">
			</div>
		</div>
				
<!-- 		<div class="dib vat margin-10-left" style="width:10%">
			<div id="divCharacter" >
				<div id="cboCharacter" data-ax5select="cboCharacter" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:70px;" ></div>
			</div>
		</div> -->
	</div>
	<div class="row">
		<div class="row" style="align-content:left;width:100%;height:94%;overflow:auto;border: 1px solid #ddd;" id="htmlView">
			<div id="sourceDiv" style="height: 100%; width: 100%">
			    <%-- <pre style="width:100%;height:100%;"><code id="htmlSrc1"></code></pre> --%>
	    	</div>
		</div>
	</div>			
</div>		

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sourceview/SourceViewTab.js"/>"></script>
