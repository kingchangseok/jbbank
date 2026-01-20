<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<link rel="stylesheet" href="<c:url value="/styles/bootstrap-timepicker.css"/>" />

<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
%>

<body style="padding: 10px;">
    <div class="content">
        <!-- history S-->
        <div id="history_wrap">	모니터링 <strong>&gt; 로그확인</strong></div>
        
        <div class="az_search_wrap" style="padding-top: 0px; padding-bottom: 5px; margin-bottom: 0px;">
			<div class="az_in_wrap" style="margin-top: 5px;">			
				<div class="row vat cb">
                    <div class="width-25 float-left">
						<div class="margin-5-right">
	                    	<label class="tit_60 poa">처리일자</label>
	                        <div class="ml_60">
	                        	<div class="input-group" data-ax5picker="txtPrcDate" style="display:inline-block;" >
						            <input id="txtPrcDate" type="text" class="f-cal" placeholder="yyyy/mm/dd" style="width:100px;">
						            <span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						        </div>
							</div>
						</div>
					</div>
                    <div class="width-55 float-left">
						<div>
	                    	<label class="tit_60 poa">&nbsp;&nbsp;신청번호</label>
	                        <div class="ml_60 width-30">
								<input id="txtAcptNo" class="width-100" type="text" readonly>
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
            	<textarea id="txtSvrLog" class="width-100" style="height: 45%; resize:none; overflow-y:auto; border: 1px solid #ddd;" readonly></textarea>
			</div>
            <div class="width-100 margin-5-top">
            	<textarea id="txtRetLog" class="width-100" style="height: 44%; resize:none; overflow-y:auto; border: 1px solid #ddd;" readonly></textarea>
			</div>
		</div>
    </div>
</body>

<form name="getReqData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script src="<c:url value="/scripts/bootstrap-timepicker.min.js"/>" type="text/javascript"></script>
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopServerLog.js"/>"></script>