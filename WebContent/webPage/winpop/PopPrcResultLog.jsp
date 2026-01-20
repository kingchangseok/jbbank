<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String seqNo = StringHelper.evl(request.getParameter("seqno"),"");
%>

<body style="padding: 10px;">
    <div class="content">
        <div id="history_wrap">	모니터링 <strong>&gt; 처리결과확인</strong></div>
        
        <div class="az_search_wrap" style="padding-top: 5px;margin:0px;">
			<div class="az_in_wrap" style="margin-top: 5px;">			
				<div class="row vat cb">
                    <div class="width-25 float-left">
						<div class="margin-5-right">
	                    	<label class="tit_60 poa">신청번호</label>
	                        <div class="ml_60">
								<input id="txtAcptNo" class="width-100" type="text" readonly>
							</div>
						</div>
					</div>
                    <div class="width-55 float-left">
						<div>
	                    	<label class="tit_60 poa">&nbsp;&nbsp;처리구분</label>
	                        <div class="ml_60 width-30">
								<div id="cboReqPass" data-ax5select="cboReqPass" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
							</div>
						</div>
					</div>
					<div class="width-20 float-left por">
						<div class="dib poa_r">
							<button id="btnSearCh" class="btn_basic_s">조회</button><button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row vat cb">
            <div class="width-100">
			    <div class="panel-body text-center" style="height: 25%;">
			    	<div data-ax5grid="retGrid" style="height: 100%;"></div>
			    </div>
			</div>
            <div class="width-100 margin-5-top">
            	<textarea id="txtRetLog" class="width-100" style="height: 60%; resize:none; border: 1px solid #ddd;" readonly></textarea>
			</div>
		</div>
    </div>
</body>
<form name="getReqData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="seqno" value="<%=seqNo%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopPrcResultLog.js"/>"></script>