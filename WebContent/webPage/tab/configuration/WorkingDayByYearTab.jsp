<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="row half_wrap_cb">
	<div class="width-15 dib vat">
		<label class="tit_80 poa tac">연도</label>
		<div class="ml_80 por">
			<div>
				<div id="cboYear" data-ax5select="cboYear" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;" ></div>
			</div>
		</div>
	</div>
	<div class="width-40  dib vat">
          <label class="tit_80 poa tac">근무일수</label>
          <div class="ml_80 por">
           	<div class="width-30 dib vat">
				<input id="txtWorkDays" type="text" class="timepicker width-100 tac" autocomplete="off" />
           	</div>
			<label class="tit_120 dib margin-10-left">*소수점 2자리까지</label>
		</div>
	</div>
	<div class="dib float-right">
		<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
		<button id="btnChange" class="btn_basic_s">수정</button>
	</div>
</div>
<div class="row az_board_basic" style="height:calc(100% - 50px);">
	<div data-ax5grid="firstGrid" style="height: 100%;" class="frameResize"></div>
</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/WorkingDayByYearTab.js"/>"></script>