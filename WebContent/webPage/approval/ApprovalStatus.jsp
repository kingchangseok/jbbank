<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecams.common.base.StringHelper"%>

<c:import url="/webPage/common/common.jsp" />

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
%>

<div class="contentFrame">
	<div id="history_wrap">결재확인<strong>&gt; 결재현황</strong></div>
	
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap dib" style="width: 100%;">
                <div class="por">
					<div class="width-25 dib vat">
						<label class="tit_90 poa">결재사유</label>
						<div class="ml_90">
							<div id="cboSin" data-ax5select="cboSin" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
						</div>
					</div>
					<div class="width-25 dib vat">
						<label class="tit_90 poa">&nbsp;&nbsp;&nbsp;신청파트</label>
						<div class="ml_90">
							<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
						</div>
					</div>
					<div class="width-30 dib vat">
						<label class="tit_90 dib poa ml_10">조회시작일</label>
						<div id="divPicker" class="ml_90 az_input_group dib" data-ax5picker="basic">
							<input id="datStD" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
							<span id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						</div>
					</div>	
					<div class="dib tar vat float-right">
						<button class="btn_basic_s" data-grid-control="excel-export" id="btnGyul" style="margin-left: 5px; margin-right: 0px;">일괄결재</button>
						<button class="btn_basic_s" data-grid-control="excel-export" id="btnCncl" style="margin-left: 5px; margin-right: 0px;">일괄반려</button>
					</div>
				</div>
                <div class="row por">
					<div class="width-25 dib vat">
						<label class="tit_90 poa">진행상태</label>
						<div class="ml_90 tal">
							<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
						</div>
					</div>
					<div class="width-25 dib vat">
						<label class="tit_90 poa">&nbsp;&nbsp;&nbsp;신청인</label>
						<div class="ml_90">
							<input id=txtUser type="text" class="width-100" placeholder="신청인을 입력하세요.">
							<input id=txtPrjnm type="text" class="width-100" style="display: none;">
						</div>
					</div>
					<div class="width-30 dib vat">
						<label class="tit_90 dib poa ml_10">조회종료일</label>
						<div id="divPicker" class="ml_90 az_input_group dib" data-ax5picker="basic2">
							<input id="datEdD" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
							<span id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						</div>
					</div>	
					<div class="dib tar vat float-right">
						<button class="btn_basic_s" id="btnQry" style="width: 70px; margin-left: 0px; margin-right: 0px;">조회</button>
						<button class="btn_basic_s" id="btnExcel" style="width: 70px; margin-left: 5px; margin-right: 0px;">엑셀저장</button>
					</div>
				</div>
				<div class="row por">
					<div class="width-50 dib vat">
						<label class="tit_90 dib poa" id="lblConf">결재/반려의견</label>
						<div class="ml_90">
							<input id=txtConMsg type="text" class="width-100" placeholder="결재/반려의견을 입력하세요.">
						</div>
					</div>
					<div class="width-10 dib vat">
						<label class="dib margin-10-left">( 기준 : 결재일 )</label>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="az_board_basic" style="height: 80%;">
		<div data-ax5grid="approGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="chkmanager"/>
</form>

<form name="getReqData">
	<input type="hidden" id="userId" name="user" value="<%=userId%>"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/approval/ApprovalStatus.js"/>"></script>