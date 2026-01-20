<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<script>
function onlyNumber(event){
    event = event || window.event;
    var keyID = (event.which) ? event.which : event.keyCode;
    if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ) 
        return;
    else
        return false;
}
 
function removeChar(event) {
    event = event || window.event;
    var keyID = (event.which) ? event.which : event.keyCode;
    if ( keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ) 
        return;
    else
        event.target.value = event.target.value.replace(/[^0-9]/g, "");
}
</script>

<body style="height: 95% !important">

<div class="row half_wrap_cb">			
	<div class="width-20  dib vat">
        <label class="tit_100 poa tac">SMS발신번호</label>
        <div class="ml_100 por">
          	<input id="txtSMSSend" type="text" class="width-100 dib" onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)'>
		</div>
	</div>
	<div class="width-40  dib vat">
           <label class="tit_100 poa tac">발신사용자</label>
           <div class="ml_100 por">
			<input id="txtUser" type="text" class="dib" style="width:80px;">
            <div class="dib vat" style="width:calc(100% - 160px);">
                <div>
					<div id="cboUser" data-ax5select="cboUser" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;" ></div>
				</div>
			</div>	
			<div class="dib poa tar">
				<button id="btnReqUser" class="btn_basic_s margin-5-left">등록</button>
			</div>
		</div>
	</div>
</div>
<div class="az_search_wrap row" style="margin-bottom:0px;">	
	<div class="az_in_wrap por">
		<!-- cell1 -->		
		<div class="width-35 dib vat">
			<div class="por">
				<label class="tac">신청구분</label>
				<div class="poa_r">
					<input type="checkbox" class="checkbox-noti float-right" id="chkAll" data-label="전체선택"/>
				</div>
			</div>
			<div class="row scrollBind" style="height:150px;">
				<ul class="list-group" id="ulReqInfo"></ul>
			</div>
		</div>
		<!-- cell2 -->		
		<div class="width-35 dib vat">
              	<div class="margin-10-left">
               	<div class="por">
                    <label class="tac">알림구분</label>
				</div>
				<div class="row scrollBind" style="height:150px;">
					<ul class="list-group" id="ulNotiInfo"></ul>
				</div>
			</div>
		</div>
		<!-- cell3 -->		
		<div class="width-30 dib vat">
           	<div class="row">
				<label class="tit_80 poa tac">업무중</label>
				<div class="ml_80">
					<div id="cboCommon" data-ax5select="cboCommon" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
				</div>
			</div>
			<div class="row">
				<label class="tit_80 poa tac">업무후</label>
				<div class="ml_80">
					<div id="cboHoli" data-ax5select="cboHoli" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
				</div>
			</div>
			<div class="row tar" style="vertical-align:bottom;height:120px;">
				<button id="btnReq" class="btn_basic_s">등록</button>
				<button id="btnDel" class="btn_basic_s margin-5-left">폐기</button>
			</div>
		</div>
	</div>
</div>
    <div class="row az_board_basic" style="height: calc(100% - 256px);">
    	<div data-ax5grid="notiGrid" style="height: 100%;"></div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/configuration/AlertCriteriaTab.js"/>"></script>