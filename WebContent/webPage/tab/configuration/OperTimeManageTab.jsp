<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="row half_wrap_cb">
	<div class="width-30 dib vat">
		<label class="tit_60 poa">시간구분</label>
		<div class="ml_60 por">
			<div>
				<div id="cboTimeDiv" data-ax5select="cboTimeDiv" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;" ></div>
			</div>
		</div>
	</div>
	<div class="width-40  dib vat">
          <label class="tit_80 poa tac">운영시간</label>
          <div class="ml_80 por">
           	<div class="width-30 dib vat">
				<input id="txtTimeSt" type="text" class="timepicker width-100 tac" autocomplete="off" />
           	</div>
           	<span class="margin-5-left margin-5-right vam">&#8767;</span>
           	<div class="width-30 dib vat">
           		<input id="txtTimeEd" type="text" class="timepicker width-100 tac" autocomplete="off" />
           	</div>

		</div>
	</div>
	<div class="dib float-right">
		<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
		<button id="btnDel" class="btn_basic_s">폐기</button>
	</div>
</div>
<div class="row az_board_basic" style="height:calc(100% - 50px);">
	<div data-ax5grid="operTimeGrid" style="height: 100%;" class="frameResize"></div>
</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/OperTimeManageTab.js"/>"></script>