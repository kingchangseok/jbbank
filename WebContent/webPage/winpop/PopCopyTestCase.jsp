<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String scmuser = StringHelper.evl(request.getParameter("scmuser"),"");
	String code = StringHelper.evl(request.getParameter("code"),"");
	String reqCd = StringHelper.evl(request.getParameter("redcd"),"");
	String isrId = StringHelper.evl(request.getParameter("isrId"),"");
%>
<style type="text/css">
	.row:after {
		content:""; 
		display:block; 
		clear:both;
	}
</style>

<body style="width: 100% !important; padding: 10px;">
	<div class="content">    	
		<div id="history_wrap"><strong>테스트케이스 복사</strong></div>
		
   		<div class="az_board_basic" style="height:28%">
			<div class="l_wrap width-48 dib vat" style="width:100%; height:100%">
	   			<iframe id="frmPrjList" name="frmPrjList" src='/webPage/tab/sr/PrjListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	  		</div>
   		</div>
   		
		<div class="az_search_wrap" style="height:68%">
			<div class="az_in_wrap">
				<div class="row">
					<div class="l_wrap dib vat" style="width:100%;">
						<div class="dib vat width-20">
							<label class="dib poa">&nbsp;&nbsp;개발자</label>
							<div class="ml_50">
								<div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
							</div>
						</div>
						<div class="dib vat width-20">
							<label class="dib poa">&nbsp;&nbsp;테스트케이스구분</label>
							<div class="ml_120">
								<div id="cboCase" data-ax5select="cboCase" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
							</div>
						</div>
						<div class="dib vat width-60">
							<label class="dib poa">&nbsp;&nbsp;SR-ID</label>
							<div class="ml_50">
								<input id="txtIsrId" type="text" class="width-100"> 
							</div>
						</div>				
					</div>
				</div>

				<div class="row vat">
					<div class="width-100 dib">
						<div class="az_board_basic" style="height: 85%">
							<div data-ax5grid="grdCase" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
						</div>
					</div>
				</div>
				<div class="row vat">
					<div class="width-100 dib vat margin-10-bottom">
						<div class="vat dib" style="float: right;">
							<button class="btn_basic_s" id="btnCopy">복사</button>
							<button class="btn_basic_s" id="btnClose" style="margin-left:5px;">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div> 
</body>

<form name="getReqData">
	<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="scmUser" name="scmUser" value="<%=scmuser%>"/>
	<input type="hidden" id="code" name="code" value="<%=code%>"/>
	<input type="hidden" id="reqCd" name="reqCd" value="<%=reqCd%>"/>
	<input type="hidden" id="isrId" name="isrId" value="<%=isrId%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopCopyTestCase.js"/>"></script>