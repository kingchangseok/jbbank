<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="sm-row half_wrap_cb">	
	<div class="dib" style="width: calc(100% - 100px)">
		<div class="l_wrap width-25">
	           <label class="tit_60 poa">작업구분</label>
	           <div class="ml_60">
				<div id="cboJobDiv" data-ax5select="cboJobDiv" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 10px);"></div>
			</div>
		</div>		
		<div class="l_wrap width-20">
	           <label class="tit_80 poa tac">IP Address</label>
	           <div class="ml_80 por">
	           	<input id="txtIp" type="text" class="dib" style="width:calc(100% - 10px);">
			</div>
		</div>		
		<div class="l_wrap width-15">
	           <label class="tit_60 poa tac">Port</label>
	           <div class="ml_60 por">
	           	<input id="txtPort" type="text" class="dib" style="width:calc(100% - 10px);">
			</div>
		</div>							
		<div class="l_wrap width-20">
	           <label class="tit_80 poa tac">계정</label>
	           <div class="ml_80 por">
	           	<input id="txtUser" type="text" class="dib" style="width:calc(100% - 10px);">
			</div>
		</div>				
		<div class="l_wrap width-20">
	           <label class="tit_80 poa tac">비밀번호</label>
	           <div class="ml_80 por">
	           	<input id="txtPass" type="password" class="width-100 dib">
			</div>
		</div>	
	</div>
	<div class="dib float-right">
		<button id="btnReq" class="btn_basic_s">등록</button>
		<button id="btnDel" class="btn_basic_s">폐기</button>
	</div>
</div>
<!-- <div class="sm-row half_wrap_cb">
	<div class="l_wrap width-60">
          <label class="tit_80 poa margin-5-left">기타</label>
          <div class="ml_80 por">
          	<input id="txtEtc" type="text" class="dib" style="width:calc(100% - 100px);">
          	<label class="margin-5-left tar"><input type="checkbox" id="chkStop"/>장애</label>
          </div>
	</div>
</div> -->

<div class="sm-row az_board_basic" style="height: 100%;">
   	<div data-ax5grid="workGrid" style="height: 100%;" class="frameResize"></div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/JobServerInfoTab.js"/>"></script>