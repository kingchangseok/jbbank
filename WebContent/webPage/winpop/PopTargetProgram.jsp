<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
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
		<div id="history_wrap"><strong>대상프로그램등록</strong></div>

		<div class="az_search_wrap" style="height:25%">
			<div class="az_in_wrap sr_status" style="height:100%">
				<div class="row por" style="height:16%">
					<div class="l_wrap dib vat" style="width:calc(40% + 10px);">
						<div class="dib vat" style="width:50%;" id="sys">
							<label id="lblTit" style="margin-right: 17px;">대상목록</label>
							<div id="" data-ax5select=""
								data-ax5select-config="{size:'sm',theme:'primary'}"
								class="width-15 dib vat" style="width: calc(100% - 99px);visibility: hidden;"></div>
							</div>
						<div class="dib" style="width:50%; " id="status">
							<label id="lblPart" style="margin-right: 28px;">변경파트</label>
							<div id="cboTeam" data-ax5select="cboTeam"
									data-ax5select-config="{
									size:'sm',
									theme:'primary'
									}" 
									class="width-15 dib" style="width: calc(100% - 99px);"></div>
						</div>
					</div>
					<div class="dib vat">
						<div class="vat dib margin-5-left">
							<button class="btn_basic_s" id="btnQry" style="margin-left: 0px; margin-right: 0px;">조회</button>
						</div>
					</div>
				</div>
				<div class="row por">
					<div class="az_board_basic" style="height: 80%;">
					<div data-ax5grid="grdLst"
						data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"
						style="height: 100%;"></div>
					</div>	
				</div>
			</div>
		</div>		
		     
		<div class="az_search_wrap" style="height:15%">
			<div class="az_in_wrap">
				<div class="row">
					<div class="l_wrap dib vat" style="width:100%;">
						<div class="dib vat width-25">
							<label class="dib poa" id="lblISRId">&nbsp;&nbsp;&nbsp;ISR ID</label>
							<div class="ml_100">
								<input id="txtISRId" type="text" class="width-100" readonly="readonly">
							</div>
						</div>
						<div class="dib vat width-75">
							<label class="dib poa" id="lblTitle">&nbsp;&nbsp;&nbsp;요청제목</label>
							<div class="ml_100">
								<input id="txtTitle" type="text" class="width-100" readonly="readonly">
							</div>
						</div>				
					</div>
				</div>
				<div class="row">
					<div class="l_wrap dib vat" style="width:100%">
						<div class="dib vat width-25">
							<label class="dib poa" id="lblReqDT">&nbsp;&nbsp;&nbsp;요청등록일</label>
							<div class="ml_100">
								<input id="txtReqDT" type="text" class="width-100" readonly="readonly">
							</div>
						</div>
						<div class="dib vat width-25">
							<label class="dib poa" id="lblReqDept">&nbsp;&nbsp;&nbsp;요청부서</label>
							<div class="ml_100">
								<input id="txtReqDept" type="text" class="width-100" readonly="readonly">
							</div>
						</div>	
						<div class="dib vat width-25">
							<label class="dib poa" id="lblEditor">&nbsp;&nbsp;&nbsp;요청인</label>
							<div class="ml_100">
								<input id="txtEditor" type="text" class="width-100" readonly="readonly">
							</div>
						</div>	
					</div>
				</div>
				<div class="row">
					<div class="l_wrap dib vat" style="width:100%">
						<div class="dib vat width-25">
							<label class="dib poa" id="lblStatus">&nbsp;&nbsp;&nbsp;진행단계</label>
							<div class="ml_100">
								<input id="txtStatus" type="text" class="width-100" readonly="readonly">
							</div>
						</div>
						<div class="dib vat width-25">
							<label class="dib poa" id="lblProStatus">&nbsp;&nbsp;&nbsp;상세진행현황</label>
							<div class="ml_100">
								<input id="txtProStatus" type="text" class="width-100" readonly="readonly">
							</div>
						</div>	
						<div class="dib vat width-25">
							<label class="dib poa" id="lblComEdDt">&nbsp;&nbsp;&nbsp;완료요청일</label>
							<div class="ml_100">
								<input id="txtComEdDt" type="text" class="width-100" readonly="readonly">
							</div>
						</div>	
					</div>
				</div>	
			</div>
		</div>

		<div class="az_search_wrap" style="height:90%">
			<div class="az_in_wrap">
				<div class="row">
					<div class="dib vat" style="width:100%;">
						<div class="dib vat width-15">
							<input class="" id="chkAll" tabindex="8" type="checkbox" value="" name="chkAll" style="margin: 5 0 0;">     
							<label class="dib" id="lbChkAll">&nbsp;&nbsp;전체선택</label>
						</div>
						<div class="dib vat width-85">
							<label class="dib poa" id="lblIsrId">&nbsp;&nbsp;ISR-ID</label>
							<div class="ml_80">
								<input id="txtIsrId" type="text" class="width-100"> 
							</div>
						</div>				
					</div>
				</div>	
				<div class="row vat">
					<div class="width-100 dib">
						<div class="az_board_basic" style="height: 50%">
							<div data-ax5grid="grdProg" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
						</div>
					</div>
				</div>
				<div class="row vat">
					<div class="width-100 dib vat margin-10-bottom">
						<div class="vat dib" style="float: right;">
							<button class="btn_basic_s" id="btnReq">등록</button>
							<button class="btn_basic_s" id="btnClose">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div> 
</body>

<input type="hidden" id="userId" name="userId" 	value="<%=userId%>"/>
<input type="hidden" id="code" name="code"  value="<%=code%>"/>
<input type="hidden" id="reqCd" name="reqCd"  value="<%=reqCd%>"/>
<input type="hidden" id="isrId" name="isrId"  value="<%=isrId%>"/>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopTargetProgram.js"/>"></script>