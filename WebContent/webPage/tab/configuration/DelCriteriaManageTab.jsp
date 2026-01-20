<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="row half_wrap_cb">			
	<div class="width-30  dib vat">
		<label class="tit_130 poa">삭제대상디렉토리구분</label>
		<div class="ml_130 por">
			<div>
				<div id="cboPathDiv" data-ax5select="cboPathDiv" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;" ></div>
			</div>
		</div>
	</div>
	<div class="width-30 dib vat">
           <label class="tit_60 poa tac">삭제주기</label>
           <div class="ml_60 por">
			<input id="txtDelCycle" type="text" class="dib" style="width:50px;">
            <div class="dib vat" style="width:34%;">
				<div id="cboDelCycle" data-ax5select="cboDelCycle" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;" ></div>
			</div>
		</div>
	</div>	
	<div class="dib float-right">
		<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
		<button id="btnDel" class="btn_basic_s">폐기</button>
	</div>
</div>

<div class="row az_board_basic"  style="height: 100%;">
   	<div data-ax5grid="delCycleGrid" style="height: 100%;" class="frameResize"></div>
</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/DelCriteriaManageTab.js"/>"></script>