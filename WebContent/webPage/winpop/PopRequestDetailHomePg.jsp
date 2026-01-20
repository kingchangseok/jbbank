<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>신청상세</title>
<c:import url="/webPage/common/common.jsp" />
<link rel="stylesheet" href="<c:url value="/styles/bootstrap-timepicker.css"/>" />

<%
	request.setCharacterEncoding("UTF-8");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String adminYN = StringHelper.evl(request.getParameter("adminYN"),"");
%>

<body id="reqBody" style="padding: 10px;">
    <div class="content" id="closeSelBox">
        <div id="history_wrap">	변경신청 <strong>&gt; 요청상세</strong></div>
        
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="l_wrap width-30">
					<label class="tit_80 poa">&nbsp;&nbsp;신청번호</label>
                       <div class="ml_80">
						<input id="txtAcptNo" class="width-100" type="text" readonly>
					</div>
					<div class="row vat cb">
						<label class="tit_80 poa">&nbsp;&nbsp;시스템</label>
                        <div class="ml_80">
							<input id="txtSyscd" class="width-100" type="text" readonly>
						</div>
					</div>
					<div class="row vat cb">
						<label class="tit_80 poa">&nbsp;&nbsp;신청자</label>
                        <div class="ml_80">
							<input id="txtEditor" class="width-100" type="text" readonly>
						</div>
					</div>
					<div class="row vat cb">
						<label class="tit_80 poa">&nbsp;&nbsp;진행상태</label>
                        <div class="ml_80">
							<input id="txtStatus" class="width-100" type="text" readonly>
						</div>
					</div>
				</div>
				<div class="l_wrap width-30">
					<label class="tit_80 poa">&nbsp;&nbsp;신청일시</label>
                       <div class="ml_80">
						<input id="txtAcptDate" class="width-100" type="text" readonly>
					</div>
					<div class="row vat cb">
						<label class="tit_80 poa">&nbsp;&nbsp;완료일시</label>
                        <div class="ml_80">
							<input id="txtPrcDate" class="width-100" type="text" readonly>
						</div>
					</div>
					<div class="row vat cb">
						<label class="tit_80 poa">&nbsp;&nbsp;적용구분</label>
                        <div class="ml_80">
							<input id="txtDeploy" class="width-100" type="text" readonly>
						</div>
					</div>
					<div class="row vat cb">
						<label class="tit_80 poa">&nbsp;&nbsp;개발기간</label>
                        <div class="ml_80">
							<input id="txtDevp" class="tar" type="text" style="width: calc(100% - 20px)" readonly><label class="dib vat" style="margin-left: 2px">일</label>
						</div>
					</div>
				</div>
				<div class="r_wrap width-40" style="height: 115px">
					<div class="sm-row margin-10-left" style="height:100%;">
						<div class="az_board_basic az_board_basic_in" style="height:100%;">
						   	<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="fileGrid" style="height:100%"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--tab-->
	<div class="row vat cb">
        <div class="tab_wrap margin-5-top">
			<ul class="tabs">
				<li rel="tab1" id="tab1Li" class="on">체크인목록</li><li rel="tab2" id="tab2Li">처리결과확인</li>
				<div class="r_wrap" id="btnBox">
					<button class="btn_basic_s" id="btnAllCncl">전체회수</button>
					<button class="btn_basic_s" id="btnRetry">전체재처리</button>
					<button class="btn_basic_s" id="btnLog">로그확인</button>
				</div>
			</ul>
		</div>
	  	
	  	<div class="tab_container" style="height: 65%;">
	      	<div id="tab1" class="tab_content" style="height: 100%;">
				<div class="row half_wrap_cb" style="height: 100%;">
					<div class="l_wrap">
						<!-- <label><input type="checkbox" id="chkDetail" class="checkbox-detail" data-label="항목상세보기"/></label> -->
						<button class="btn_basic_s" id="btnSelCncl">선택건회수</button>
			    	</div>
		    		<div class="l_wrap width-100 margin-10-top" style="height: 98%;">
					    <div class="panel-body text-center" id="gridDiv1" style="height: 100%;">
					    	<div data-ax5grid="reqGrid" style="height: 100%;"></div>
					    </div>
				    </div>
			    </div>
	       	</div>
	       	<div id="tab2" class="tab_content" style="height: 100%;">
				<div class="row half_wrap_cb" style="height: 100%;">
					<div class="l_wrap width-100">
	    				<label class="tit_80 poa">배포구분</label>
	    				<div class="ml_80 width-20 dib vat">
							<div id="cboPrcSys" data-ax5select="cboPrcSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%;"></div>
	    				</div>
		    		</div>
		    		<div class="l_wrap width-100 margin-10-top" style="height: 92%;">
					    <div class="panel-body text-center" id="gridDiv2" style="height: 100%;">
					    	<div data-ax5grid="resultGrid" style="height: 100%;"></div>
					    </div>
				    </div>
				</div>
			</div>
	   	</div>
		<!--tab-->
		
		<div class="row vat cb">
			<div class="l_wrap margin-5-top" style="width: calc(100% - 300px);">
				<div class="width-100 float-left dib" id="divConf"> <!-- style="display: none;" -->
					<div class="float-left dib width-100">
						<label id="lblApprovalMsg" class="tit_100 poa">결재/반송의견</label>
	                    <div class="ml_100">
	    					<input id="txtApprovalMsg" class="width-100" type="text">
						</div>
					</div>
				</div>
				<div class="dib" id="divErr">
                   	<label id="lblErMsg" class="txt_r font_12"></label>
				</div>
			</div>
			
			<div class="r_wrap margin-5-top" style="width: 300px;">
				<div class="float-right">
					<button class="btn_basic_s" id="btnQry">새로고침</button>
					<button class="btn_basic_s" id="btnApprovalInfo">결재정보</button>
					<button class="btn_basic_s" id="btnApproval">결재</button>
					<button class="btn_basic_s" id="btnCncl">반려</button>
					<button class="btn_basic_s" id="btnClose">닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>

<form name="getReqData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="adminYN" value="<%=adminYN%>"/>
	<input type="hidden" name="rgtList"/>
</form>

<form name="setReqData">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="reqno"/>
	<input type="hidden" name="reqcd"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="itemid"/>
	<input type="hidden" name="srid"/>
	<input type="hidden" name="etcinfo"/>
	<input type="hidden" name="seqno"/>
	<input type="hidden" name="logdate"/> <!-- logDate -->
	<input type="hidden" name="scnidx"/>
	<input type="hidden" name="param1"/>
	<input type="hidden" name="prjno"/>
	<input type="hidden" name="syscd"/>
	<input type="hidden" name="userid"/>
	<input type="hidden" name="name"/>
	<input type="hidden" name="pid"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script src="<c:url value="/scripts/bootstrap-timepicker.min.js"/>" type="text/javascript"></script>
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopRequestDetailHomePg.js"/>"></script>