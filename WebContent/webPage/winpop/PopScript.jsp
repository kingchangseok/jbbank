<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
%>

<body style="padding: 10px;">
    <div class="content">
        <!-- history S-->
        <div id="history_wrap">	프로그램 <strong>&gt; 스크립트확인</strong></div>
        <div class="az_search_wrap" style="padding-top: 0px;padding-bottom: 0px;margin-bottom: 0px;">
			<div class="az_in_wrap" style="margin-bottom: 10px;">
				<div class="row vat cb" style="margin-top: 5px;">
                    <div class="width-30 float-left">
						<div class="margin-5-right">
	                    	<label class="tit_80 poa">시스템</label>
	                        <div class="ml_80">
								<input id="txtSysMsg" class="width-100" type="text" readonly>
							</div>
						</div>
					</div>
                    <div class="width-70 float-left">
						<div>
	                    	<label class="tit_80 poa">&nbsp;&nbsp;프로그램명</label>
	                        <div class="ml_80">
								<input id="txtRsrcnName" class="width-100" type="text" readonly>
							</div>
						</div>
					</div>
				</div>
				<div class="row vat cb">
                    <div class="width-100 float-left">
						<div>
	                    	<label class="tit_80 poa">경로</label>
	                        <div class="ml_80">
								<input id="txtDirPath" class="width-100" type="text" readonly>
							</div>
						</div>
					</div>
				</div>
				<div class="row vat cb">
                    <div class="width-100 float-left por">
						<div class="dib width-30">
	                    	<label class="tit_80 poa">스크립트유형</label>
	                        <div class="ml_80">
								<div id="cboScript" class="margin-5-right" data-ax5select="cboScript" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
							</div>
						</div>
						<div style="width:70%; display: inline-block; vertical-align: top;">
							<!-- <input id="txtScriptName" type="text" style="width: 100%;" class="margin-5-right" readonly> -->
					    </div>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 10;">
            <div class="width-100">
			    <div class="panel-body text-center" style="height: 50%;">
			    	<div data-ax5grid="scriptGrid" style="height: 100%;"></div>
			    </div>
			</div>
		</div>
		<div class="row"><!--  vat cb -->
			<div class="width-35 float-right tar">
					<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
			</div>
		</div>
        <!-- history E-->
    </div>
</body>

<form name="getReqData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopScript.js"/>"></script>