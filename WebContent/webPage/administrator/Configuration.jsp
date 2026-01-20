<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<%@ page import = "com.ecams.common.base.*"%>

	<%
		RSA rsa = RSA.getEncKey();
		request.setAttribute("publicKeyModulus", rsa.getPublicKeyModulus());
		request.setAttribute("publicKeyExponent", rsa.getPublicKeyExponent());
	%>
<style type="text/css">
.itemBox {
	/* width: calc(33% - 106px);
	max-width: 370px; */
	width: 25%;
}
</style>
<div class="contentFrame">
	<!-- history S-->
	<div id="history_wrap">관리자 <strong>&gt; 환경설정</strong></div>
	<!-- history E-->         
    
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap por">
			<!-- cell1 -->		
               <div class="itemBox dib vat">
               	<div>
                    <label class="tit_150 poa">IP Address(내부망)</label>
                    <div style="margin-left: 120px;">
						<input id="txtIpIn" type="text" style="width:calc(100% - 10px);">
					</div>
				</div>
               	<div class="sm-row">
                    <label class="tit_150 poa">IP Address(외부망)</label>
                    <div style="margin-left: 120px;">
						<input id="txtIpOut" type="text" style="width:calc(100% - 10px);">
					</div>
				</div>
               	<div class="sm-row">
                    <label class="tit_150 poa">PORT</label>
                    <div style="margin-left: 120px;">
						<input id="txtPort" type="text" style="width:calc(30% - 10px);">
						<label class="ml_10">URL</label>
	                    <div style="width: calc(70% - 45px)" class="dib">
							<input id="txtURL" type="text" class="width-100 ml_10">
						</div>
					</div>
				</div>
			</div>
			<!-- cell2 -->		
               <div class="itemBox dib vat">
               	<div class="margin-5-left">
                	<div>
	                    <label class="tit_150 poa">비밀번호변경주기</label>
	                    <div class="por" style="margin-left: 130px;">
							<input id="txtPassCycle" type="text" class="dib" style="width: 50px;">
		                    <div class="dib" style="width:calc(100% - 60px);">
								<div id="cboPassCycle" data-ax5select="cboPassCycle" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
							</div>
						</div>
					</div>
                	<div class="sm-row">
	                    <label class="tit_150 poa">비밀번호입력제한횟수</label>
	                    <div style="margin-left: 130px;">
							<input id="txtPassLimit" type="number" style="width:calc(100% - 10px);">
						</div>
					</div>
					<div class="sm-row">
						<label class="tit_150 poa">관리자용비밀번호</label>
	                    <div  style="margin-left: 130px;">
							<input id="txtAdminPass" type ="password" style="width:calc(100% - 10px);">
						</div>
					</div>
				</div>
			</div>
			<!-- cell2 -->		
               <div class="itemBox dib vat">
               	<div class="margin-5-left">
               		<div>
	                    <label class="tit_100 poa">초기비밀번호</label>
	                    <div class="ml_130">
							<input id="txtInitPass" type="password" style="width:calc(100% - 10px);">
						</div>
					</div>
					<div class="sm-row">
	                    <label class="tit_100 poa">프로세스총갯수</label>
	                    <div class="ml_130">
							<input id="txtProcTot" type="number" style="width:calc(100% - 10px);">
						</div>
					</div>
                	<div class="sm-row">
	                    <input type="checkbox" class='checkbox-pie' name='chkMGR' id='chkMGR' data-label="MGR로그">
						<input type="checkbox" class='checkbox-pie' name='chkEFF' id='chkEFF' data-label="영향분석SKIP">
					</div>
				</div>
			</div>
			<!-- cell4 -->		
            <div class="itemBox dib vat">
               	<div class="margin-5-left">
                	<div class="sm-row">
	                     <label class="tit_100 poa">소스백업위치</label>
	                    <div class="ml_110">
							<div id="cboBackUp" data-ax5select="cboBackUp" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: calc(100% - 125px);" class="dib margin-5-right"></div>
							<div class="dib vat">
								<button id="btnReq" class="btn_basic_s" style="width:110px;">환경설정등록</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 검색 E-->
    <!-- tab S-->
    <div class="tab_wrap">
		<ul class="tabs">
			<li class="on" rel="tab1" id="tab1Li">운영시간관리</li>
	        <li rel="tab2" id="tab2Li">삭제기준관리</li>
	        <li rel="tab3" id="tab3Li">디렉토리정책</li>
	        <li rel="tab4" id="tab4Li">작업서버정보</li>
		</ul>
	</div>
	<!-- tab E-->
	
	<div>
		<div id="tab1" class="tab_content">
			<iframe id="frame1" src='/webPage/tab/configuration/OperTimeManageTab.jsp' width='100%' frameborder="0" style="height: calc(100% - 209px);"></iframe>
		</div>
		<div id="tab2" class="tab_content">
			<iframe id="frame2" src='' width='100%' frameborder="0" style="height: calc(100% - 209px);"></iframe>
		</div>
		<div id="tab3" class="tab_content">
			<iframe id="frame3" src='' width='100%' frameborder="0" style="height: calc(100% - 209px);"></iframe>
		</div>
		<div id="tab4" class="tab_content">
			<iframe id="frame4" src='' width='100%' frameborder="0" style="height: calc(100% - 209px);"></iframe>
		</div>
	</div>
</div>

<form name="popPam">
    <input type="hidden" id="rsaPublicKeyModulus" value="${publicKeyModulus}">
	<input type="hidden" id="rsaPublicKeyExponent" value="${publicKeyExponent}">
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="/js/util/jsbn.js"></script>
<script type="text/javascript" src="/js/util/rsa.js"></script>
<script type="text/javascript" src="/js/util/prng4.js"></script>
<script type="text/javascript" src="/js/util/rng.js"></script>
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/Configuration.js"/>"></script>