<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 820px !important">
<div class="pop-header">
	<div>
		<label>[동기화 처리로그확인]</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>

<div class="container-fluid pop_wrap">
	<div class="az_search_wrap" style="padding-top: 5px;margin:0px;">
		<div class="az_in_wrap" style="margin-top: 5px;">			
			<div class="row vat cb">
				<div class="width-100">
					<label class="poa fontStyle-ing">이 화면을 닫으면 다시 확인 할 수 없습니다.</label>
					<div class='tal dib float-right'>
						<label id="lblCnt"></label>
						<button id="btnRefresh" class="btn_basic_s margin-5-left">새로고침</button>
						<button id="cmdQry_a" class="btn_basic_s margin-5-left">자동고침</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row vat cb">
           <div class="width-100">
		    <div class="panel-body text-center" style="height: 25%;">
		    	<div data-ax5grid="firstGrid" style="height: 100%;"></div>
		    </div>
		</div>
           <div class="width-100 margin-5-top">
           	<textarea id="txtLogView" class="width-100" style="height: 55%; resize:none; border: 1px solid #ddd;background-color: #fff;" readonly></textarea>
		</div>
	</div>
	
	<div class="row">
		<div class="dib float-right">
			<button id="btnExit" class="btn_basic_s">닫기</button>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/LocalSyncResultModal.js"/>"></script>