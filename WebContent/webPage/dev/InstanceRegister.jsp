<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
    <div class="contentFrame">
        <div id="history_wrap"></div>
		<!-- 검색 S-->    
		<div class="az_search_wrap" style="padding-left: 10px; padding-right: 10px;">
			<div class="row">
				<div class="width-25 dib vat">
	               	<label class="tit-150 dib poa">노드명</label>
	               	<div class="ml_100">
	                    <div id="cboSystem" data-ax5select="cboSystem" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="width-25 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;작업구분</label>
					<div class="ml_100">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<div class="width-25 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;인스턴스구분</label>
					<div class="ml_100">
						<div id="cboInsGbn" data-ax5select="cboInsGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-25 dib vat">
					<label class="tit-150 dib poa">IP/PORT</label>
					<div class="ml_100">
						<input id="txtIp" type="text" class="width-50 dib" />
						<input id="txtPort" type="text" class="width-45 dib" style="margin-left: 14px;"/>
					</div>
				</div>
				<div class="width-25 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;구분</label>
					<div class="ml_100">
						<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
				<div class="width-25 dib vat">
               		<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;인스턴스명</label>
	               	<div class="ml_100">
						<div id="cboInsName" data-ax5select="cboInsName" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
	               	</div>
				</div>
			</div>
			<div class="row">
				<div class="dib vat" style="width: 75.4%;">
               		<label class="tit-150 dib poa">STOP명령</label>
	               	<div class="ml_100">
						<input id="txtStop" type="text" class="width-100" />
	               	</div>
				</div>
			</div>
			<div class="row">
				<div class="dib vat" style="width: 75.4%;">
               		<label class="tit-150 dib poa">START명령</label>
	               	<div class="ml_100">
						<input id="txtStart" type="text" class="width-100" />
	               	</div>
				</div>
				<div class="dib vat ml_10">
					<div class="vat dib">
						<input type="checkbox" class="checkbox-pie" id="chkRun" data-label="형상관리에서 실행">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="dib vat" style="width: 75.4%;">
               		<label class="tit-150 dib poa">STOP조회명령</label>
	               	<div class="ml_100">
						<input id="txtStatus" type="text" class="width-100" />
	               	</div>
				</div>
				<div class="width-20 vat dib ml_10">
					<button id="btnReg" name="btnReg" class="btn_basic_s" style="width:80px;">등록</button>
					<button id="btnDel" name="btnDel" class="btn_basic_s" style="width:80px;">삭제</button>
					<button id="btnQry" name="btnQry" class="btn_basic_s" style="width:80px;">조회</button>
				</div>
			</div>
			<div class="row">
				<div class="dib vat" style="width: 75.4%;">
               		<label class="tit-150 dib poa">START조회명령</label>
	               	<div class="ml_100">
						<input id="txtStatus2" type="text" class="width-100" />
	               	</div>
				</div>
			</div>
			<div class="row">
				<div class="dib vat" style="width: 75.4%;">
               		<label class="tit-150 dib poa">Health체크명령</label>
	               	<div class="ml_100">
						<input id="txtHealth" type="text" class="width-100" />
	               	</div>
				</div>
			</div>
			<div class="row">
			</div>
		</div>
		<!-- 게시판 S-->
	    <div class="az_board_basic" style="height:60%;">
	    	<div data-ax5grid="instanceGrid" data-ax5grid-config="{lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>
	</div>
<!-- contener E -->

<form name="popPam" id="popPam" method="post">
	<INPUT type="hidden" name="UserId" id="UserId"> 
	<INPUT type="hidden" name="SysCd" id="SysCd">
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/InstanceRegister.js"/>"></script>