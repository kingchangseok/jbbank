<!--  
	* 화면명: 프로그램정보
	* 화면호출:
	 1) 프로그램정보 -> 변경내역 탭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<!-- contener S -->
<body style="min-width: auto;">
	<div style="overflow-y: hidden;">
	<!-- 하단 입력 S-->
	<div class="margin-5-top">
	    <!-- 프로그램명 -->
        <div class="width-40 dib vat">
            <label id="lbProgId2" class="tit_100 poa">프로그램명</label>
            <div class="ml_80">
				<input id="txtProgId2" name="" type="text" class="width-100" readonly>
			</div>
		</div>	
	    <!-- 프로그램한글명 -->
        <div class="dib vat" style="margin-left: 10px;width:calc(60% - 15px);">
            <label id="lbStory2" class="tit_100 poa">프로그램설명</label>
            <div class="ml_100">
				<input id="txtStory2" name="txtStory2" type="text" class="width-100" readonly>
			</div>
		</div>				
	</div>		
	<div class="margin-5-top por">
        <div class="width-40 dib vat">
            <label id="lbUser" class="tit_80 poa">의뢰구분</label>
            <div class="ml_80">
			    <div id="cboReq" data-ax5select="cboReq" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-100"></div>
		    </div>
		</div>
		<button id="btnQry2" class="btn_basic_s margin-5-left poa" style="top: 0;">조회</button>
		<button id="btnExcel" class="btn_basic_s ml_50 poa" data-grid-control="excel-export" style="top: 0;">엑셀저장</button>
	</div>
	<!-- 하단 입력 E-->			
    <!-- 테이블 S-->
    <div class="row az_board_basic" style="height: calc(100% - 100px);">
	    <div data-ax5grid="grdProgHistory" style="height: 100%;"></div>
	</div>	
	<!-- 테이블  E -->
	</div>
</body>
<!-- contener E -->

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="itemid"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/programinfo/ProgHistoryTab.js"/>"></script>
