<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="row half_wrap_cb">			
	<div class="width-25 dib vat">
           <label class="tit_80 poa">디렉토리구분</label>
           <div class="ml_80 por">
               <div>
				<div id="cboPathDiv" data-ax5select="cboPathDiv" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 10px);" ></div>
			</div>
		</div>
	</div>	
	<div class="width-30 dib vat">
           <label class="tit_80 poa tac">디렉토리명</label>
           <div class="ml_80 por">
               <input id="txtPathName" type="text" class="dib" style="width:calc(100% - 10px);" >
		</div>
	</div>	
	<div class="width-20 dib vat">
           <label class="tit_100 poa tac">서버 IP Address</label>
           <div class="ml_100 por">
               <input id="txtIp" type="text" class="dib" style="width:calc(100% - 10px);" >
		</div>
	</div>
	<div class="width-24 dib vat poa_r">
           <label class="tit_80 poa tac">서버 Port</label>
           <div class="ml_80 por">
               <div>
				<input id="txtPort" type="text" class="dib" style="width:calc(100% - 95px);">
			</div>
			<div class="dib poa_r">
				<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
				<button id="btnDel" class="btn_basic_s">폐기</button>
			</div>
		</div>
	</div>
</div>

   <div class="row az_board_basic" style="height: 100%;">
   	<div data-ax5grid="dirGrid" style="height: 100%;" class="frameResize"></div>	
</div>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/DirectoryPolicyTab.js"/>"></script>