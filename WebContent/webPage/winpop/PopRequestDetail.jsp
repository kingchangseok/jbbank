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
	String rsrcName = StringHelper.evl(request.getParameter("rsrcname"),"");
	String chkManager = StringHelper.evl(request.getParameter("chkmanager"),"");
%>

<style>
	.timePickerDiv{
		width: 95px;
		height: 25px;
		text-align: center;
		background-color: #fff;
		border: 1px solid #ccc;
	}
	.timePickerDivDisable {
		pointer-events: none;
    	background: #eee;
	}
	.timePickerDiv:hover, .timePickerDiv:focus{
		width: 95px;
		height: 25px;
		text-align: center;
		background-color: #fff;
		border: 1px solid #326aab;
	}
	.numberTxt, .numberTxt:hover, .numberTxt:focus {
		padding: 0px;
		margin: 0px;
		width: 35px;
		height: 22px;
		vertical-align: middle;
	    text-align: right;
	    border: 0px;
	}
	input[type="number"] {
	    position: relative;
	}
	input[type=number]::-webkit-inner-spin-button, 
	input[type=number]::-webkit-outer-spin-button { 
	      /*-webkit-appearance: none;*/
	      opacity: .5;
	      background-color: #fff;
	}
	
	.numberTxt {
		background: inherit;
	}
</style>

<body id="reqBody" style="padding: 10px;">
    <div class="content" id="closeSelBox">
        <div id="history_wrap">	변경신청 <strong>&gt; 요청상세</strong></div>
        
		<div class="az_search_wrap">
			<div class="az_in_wrap">			
				<div class="l_wrap width-25">
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
				<div class="l_wrap width-25">
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
					<div class="row vat cb" id="divReqPass" style="display:none;">
						<label class="tit_80 poa">&nbsp;&nbsp;적용구분</label>
	                   	<div class="ml_80">
	                  	 	<input id="txtDeploy" class="width-100" type="text" readonly>
						</div>
					</div>
				</div>
				<div class="r_wrap width-50" style="height: 115px">
					<div class="sm-row margin-10-left" style="height:100%;">
						<div class="az_board_basic az_board_basic_in" style="width:calc(100% - 85px); height:100%;">
						   	<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="fileGrid" style="height:100%"></div>
						</div>
						<div class="dib float-right">
							<div style="display:none;" id="fileSave"></div>
							<button id="btnFileAdd" class="btn_basic_s" style="width:80px;">파일첨부</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--tab-->
	<div class="row vat cb">
        <div class="tab_wrap" style="height: 59px;">
		<div class="r_wrap" id="btnBox" style="display: none; margin-bottom: 3px;">
			<button class="btn_basic_s margin-2-left" id="btnStepEnd" >단계완료처리</button>
			<button class="btn_basic_s margin-2-left" id="btnAstaResult">영향분석조회</button>
			<button class="btn_basic_s margin-2-left" id="btnFortify">FORTIFY단계완료</button>
			<button class="btn_basic_s margin-2-left" id="btnGetSrc">소스Sync</button>
			<button class="btn_basic_s margin-2-left" id="btnReturn">재요청</button>
			<button class="btn_basic_s" id="btnSrcView">소스보기</button>
			<button class="btn_basic_s margin-2-left" id="btnSrcDiff">소스비교</button>
			<button class="btn_basic_s margin-2-left" id="btnAllCncl">전체회수</button>
			<button class="btn_basic_s margin-2-left" id="btnRetry">전체재처리</button>
			<button class="btn_basic_s margin-2-left" id="btnLog">로그확인</button>
		</div>
			<ul class="tabs">
				<li rel="tab1" id="tab1Li" class="on">신청목록</li>
				<li rel="tab2" id="tab2Li">처리결과확인</li>
				<li rel="tab3" id="tab3Li">검증항목조회</li>
				<li rel="tab4" id="tab4Li">책임자/Q.A.확인</li>
				<li rel="tab5" id="tab5Li">프로그램 TEST</li>
				<li rel="tab6" id="tab6Li">소스코드분석</li>
				<div class="r_wrap" id="btnBox2" style="display:none;">
					<button class="btn_basic_s margin-2-right" id="btnCodeResult">소스코드분석확인</button>
				</div>
			</ul>
		</div>
	  	
	  	<div class="tab_container" style="height: 60%;">
	      	<div id="tab1" class="tab_content" style="height: 100%;">
				<div class="row half_wrap_cb" style="height: 100%;">
					<div class="l_wrap">
						<label><input type="checkbox" id="chkDetail" class="checkbox-detail" data-label="항목상세보기"/></label>
						<label style="display: none;"><input type="checkbox" id="chkAll" class="checkbox-detail" data-label="전체선택"/></label>
						<label style="display: none;"><input type="checkbox" id="chkDeptManager" class="checkbox-detail" data-label="부장결재" /></label>
						<button class="btn_basic_s" id="btnSelCncl">선택건회수</button>
						<button class="btn_basic_s" id="btnErrSkip" style="display: none;">오류건제외</button>
						<button class="btn_basic_s" id="btnProgDetail">프로그램 상세</button>
			    	</div>
					<div class="r_wrap">
						<button class="btn_basic_s" id="btnExcel">엑셀저장</button>
						<button class="btn_basic_s" id="btnPriorityOrder">우선순위적용</button>
		    		</div>
		    		<div class="l_wrap width-100 margin-10-top" style="height: 92%;">
					    <div class="panel-body text-center" id="gridDiv1" style="height: 100%;">
					    	<div data-ax5grid="reqGrid" style="height: 100%;"></div>
					    </div>
				    </div>
			    </div>
	       	</div>
	       	<div id="tab2" class="tab_content" style="height: 100%;">
				<div class="row half_wrap_cb" style="height: 100%;">
					<div class="l_wrap width-100">
	    				<label class="tit_60 poa">배포구분</label>
	    				<div class="ml_60 width-20 dib vat">
							<div id="cboPrcSys" data-ax5select="cboPrcSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%;"></div>
	    				</div>
		    		</div>
		    		<div class="l_wrap width-100 margin-10-top" style="height: 92%;">
					    <div class="panel-body text-center" id="gridDiv2" style="height: 100%;">
					    	<div data-ax5grid="resultGrid"  style="height: 100%;"></div>
					    </div>
				    </div>
				</div>
			</div>
			<div id="tab3" class="tab_content" style="height: 100%;">
				<iframe id="frmReqCheck" name="frmReqCheck" src='/webPage/tab/request/ReqCheckTab.jsp' width='100%' height='100%' scrolling="no" frameborder="0"></iframe>
			</div>
			<div id="tab4" class="tab_content" style="height: 100%;">
				<iframe id="frmQA" name="frmQA" src='/webPage/tab/request/QATab.jsp' width='100%' height='100%' scrolling="no" frameborder="0"></iframe>
			</div>
			<div id="tab5" class="tab_content" style="height: 100%;">
				<iframe id="frmProgramTest" name="frmProgramTest" src='/webPage/tab/request/ProgramTestTab.jsp' width='100%' height='100%' scrolling="no" frameborder="0"></iframe>
			</div>
			<div id="tab6" class="tab_content" style="height: 100%;">
				<div class="row half_wrap_cb" style="height: 100%;">
		    		<div class="l_wrap width-100" style="height: 100%;">
					    <div class="panel-body text-center" id="gridDiv3" style="height: 100%;">
					    	<div data-ax5grid="srcGrid"  style="height: 100%;"></div>
					    </div>
				    </div>
				</div>
			</div>
	   	</div>
		<!--tab-->
		
		<div class="row vat cb">
			<div class="l_wrap margin-5-top" style="width: calc(100% - 300px);">
				<div class="width-100 float-left dib" id="divConf" > <!-- style="display: none;" -->
					<div class="float-left dib width-100">
						<label id="lblApprovalMsg" class="tit_80 poa">결재/반송의견</label>
	                    <div class="ml_80">
	    					<input id="txtApprovalMsg" class="width-100" type="text">
						</div>
					</div>
				</div>
			</div>
			<div class="r_wrap margin-5-top" style="width: 300px;">
				<div class="float-right">
					<button class="btn_basic_s" id="btnPriority" style="display: none;">우선적용</button>
					<button class="btn_basic_s" id="btnAsta" style="display: none;">영향분석확인</button>
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
	<input type="hidden" name="rsrcname" value="<%=rsrcName%>"/>
	<input type="hidden" name="chkmanager" value="<%=chkManager%>"/>
</form>

<form name="setReqData">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="itemid"/>
	<input type="hidden" name="orderId"/>
	<input type="hidden" name="isrid"/>
	<input type="hidden" name="etcinfo"/>
	<input type="hidden" name="seqno"/>
	<input type="hidden" name="syscd"/>
	<input type="hidden" name="sysmsg"/>
	<input type="hidden" name="dirpath"/>
	<input type="hidden" name="rsrcname"/>
	<input type="hidden" name="rsrccd"/>
	<input type="hidden" name="jobcd"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="logdate"/>
	<input type="hidden" name="winSw"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script src="<c:url value="/scripts/bootstrap-timepicker.min.js"/>" type="text/javascript"></script>
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopRequestDetail.js"/>"></script>