<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame" style="display: flow-root;">
	<div id="history_wrap">관리자 <strong>&gt; 시스템정보</strong></div>
	<div class="az_search_wrap">
		<div class="az_in_wrap tar">
			<input id="txtFindSys" class="margin-5-right" style="width:300px;" type="text" placeholder="시스템코드 또는 시스템명을 입력하여 조회하세요."/>
			<input type="checkbox" class="checkbox-pie" id="chkCls" data-label="폐기포함"/>
			<button class="btn_basic_s" id="btnQry">조회</button>
		</div>
	</div>
    <div class="az_board_basic"  style="height: calc(100% - 130px);">
    	<div data-ax5grid="sysInfoGrid" class="height-100 resize"></div>
	</div>	
	<div class="width-100 tar" style="right: 5px; margin-top: 5px;">
		<button id="btnReq" class="btn_basic_s">시스템신규</button>
		<button id="btnFact" class="btn_basic_s">처리펙터추가</button>
		<button id="btnCopy" class="btn_basic_s">시스템정보복사</button>
	</div>
</div>

<script type="text/javascript" src="<c:url value="/scripts/bluebird-3.5.0.min.js"/>"></script>
<c:import url="/js/ecams/common/commonscript.jsp" />

<script type="text/javascript" src="<c:url value="/js/ecams/administrator/SysInfo.js"/>"></script>