<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />

<div class="az_search_wrap">
	<div class="az_in_wrap sr_status">
		<div class="row">
			<div class="l_wrap dib vat" style="width:100%;">
				<div class="dib vat width-25">
					<label class="dib poa" id="lblISRId">&nbsp;&nbsp;&nbsp;ISR ID</label>
					<div class="ml_100">
						<input id="txtISRId" type="text" class="width-100" readonly="readonly">
					</div>
				</div>
				<div class="dib vat width-75">
					<label class="dib poa" id="lblTitle" style="margin-left: 10px;">&nbsp;&nbsp;&nbsp;요청제목</label>
					<div class="ml_100">
						<input id="txtTitle" type="text" class="width-100" readonly="readonly">
					</div>
				</div>				
			</div>
		</div>
		<div class="row">
			<div class="l_wrap dib vat" style="width:100%; margin-top: 5px;">
				<div class="dib vat width-25">
					<label class="dib poa" id="lblReqDT">&nbsp;&nbsp;&nbsp;요청등록일</label>
					<div class="ml_100">
						<input id="txtReqDT" type="text" class="width-100" readonly="readonly">
					</div>
				</div>
				<div class="dib vat width-25">
					<label class="dib poa" id="lblReqDept" style="margin-left: 10px;">&nbsp;&nbsp;&nbsp;요청부서</label>
					<div class="ml_100">
						<input id="txtReqDept" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
				<div class="dib vat width-25">
					<label class="dib poa" id="lblEditor" style="margin-left: 10px;">&nbsp;&nbsp;&nbsp;요청인</label>
					<div class="ml_100">
						<input id="txtEditor" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
			</div>
		</div>
		<div class="row">
			<div class="l_wrap dib vat" style="width:100%; margin-top: 5px;">
				<div class="dib vat width-25">
					<label class="dib poa" id="lblStatus">&nbsp;&nbsp;&nbsp;진행단계</label>
					<div class="ml_100">
						<input id="txtStatus" type="text" class="width-100" readonly="readonly">
					</div>
				</div>
				<div class="dib vat width-25">
					<label class="dib poa" id="lblProStatus" style="margin-left: 10px;">&nbsp;&nbsp;&nbsp;상세진행현황</label>
					<div class="ml_100">
						<input id="txtProStatus" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
				<div class="dib vat width-25">
					<label class="dib poa" id="lblComEdDt" style="margin-left: 10px;">&nbsp;&nbsp;&nbsp;완료요청일</label>
					<div class="ml_100">
						<input id="txtComEdDt" type="text" class="width-100" readonly="readonly">
					</div>
				</div>	
			</div>
		</div>		
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/test/baseTestInfo.js"/>"></script>