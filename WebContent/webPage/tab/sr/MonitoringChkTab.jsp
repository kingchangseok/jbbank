<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<c:import url="/js/ecams/common/commonscript.jsp" />

<body style="padding: 10px !important; width: 100% !important; min-width: 0px !important;">
	<!-- line1 S -->
	<div class="row por">					
	    <div class="dib vat width-25">
	    	<label class="tit_50 poa">개발자</label>
		    <div class="ml_50">
				<div id="cboUser" data-ax5select="cboUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
			</div>
		</div>
		<div class="dib vat width-25 margin-10-left">
	    	<label class="tit_50 poa">시스템</label>
		    <div class="ml_50">
				<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
			</div>
		</div>
	</div>
	<!-- line2 S -->
	<div class="row az_board_basic"  style="height: calc(100% - 70px);">
		<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
	<div class="row">
		<div class="vat width-100">
			<button id="cmdReq" name="cmdReq" class="btn_basic_s r_wrap" style="margin-left:5px;">등록/수정</button>
			<button id="cmdQry" name="cmdSea" class="btn_basic_s r_wrap" style="margin-left:5px;">조회</button>
		</div>
	</div>
</body>
<form name="setReqData">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="itemid"/>
	<input type="hidden" name="srid"/>
	<input type="hidden" name="etcinfo"/>
</form>
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/MonitoringChkTab.js"/>"></script>