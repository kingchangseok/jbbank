<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 1.개발환경설정 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="height: 100% !important">
	<div class="row az_search_wrap">
		<div class="az_in_wrap">
			<div class="row vat">
				<div class="width-30 dib vat">
					<div class="tit_120 poa">
						<label>시스템명</label>
					</div>
					<div class="ml_120 vat">
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary', selecteditable: true}"  style="width: 100%" class="dib"></div>
					</div>
				</div>
				<!-- <div class="width-70 dib">
					<div class="tit_120 poa margin-10-left">
						<label>Agent설치 경로</label>
					</div>
					<div class="ml_150 tal">
						<input id="txtAgentDir" type="text" style="width: calc(100% - 190px);"></input>
						<div class="tal float-right" style="display: inline;">
							<label style="float: right; color:red;">Agent 기본경로 [C:\ecamsagent]</label> 
						</div>
					</div>
				</div> -->
			</div>
			<div class="row vat">
				<!-- <div class="width-30 dib vat">
					<div class="tit_130 poa">
						<label>로컬IP</label>
					</div>
					<div class="ml_130 vat">
						<input id="txtIp" type="text" style="width: calc(100%);"></input>
					</div>
				</div> -->
				<div class="width-100 dib">
					<div class="tit_120 poa">
						<label>개발 Home Directory</label>
					</div>
					<div class="ml_120 tal">
						<input id="txtDevDir" type="text" style="width: calc(100% - 190px);"></input>
						<button id="btnDir" class="btn_basic_s ml_10" style="width:100px;">디렉토리찾기</button>
						<button id="btnReq" class="btn_basic_s margin-5-left" style="width:70px;">등록</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row vat">
		<label class="title">등록된 개발환경 목록</label>
	</div>
	<div class="row az_board_basic" style="height: calc(100% - 160px);">
		<div data-ax5grid="userConfigGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
	</div>
	<div class="tar row">
		<button id="btnSearch" class="btn_basic_s" style="width:70px;">조회</button>
		<button id="btnDel" class="btn_basic_s margin-5-left" style="width:70px;">삭제</button>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/userconfig/DevConfigTab.js"/>"></script>