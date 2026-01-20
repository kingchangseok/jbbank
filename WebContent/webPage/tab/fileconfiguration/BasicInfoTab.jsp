<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<div class="az_search_wrap">
	<div class="az_in_wrap">
		<div class="row vat">
			<div class="dib width-40">
				<div class="tit_100 poa">
					<label>작업구분</label>
				</div>
				<div class="ml_100">
					<div id="cboJobDiv" data-ax5select="cboJobDiv" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%"></div> 
				</div>
			</div>
			
			<div class="vat dib margin-5-left">
				<button id="btnSave" name="btnSave" class="btn_basic_s">등록</button>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-20 dib">
				<div class="tit_100 poa">
					<label>작업주기</label>
				</div>
				<div class="ml_100">
					<input id="txtCycle" type="text" class="width-100"> 
				</div>
			</div>
			<div class="width-20 dib vat">
				<div id="cboCycle" data-ax5select="cboCycle" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%"></div> 
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-40 dib">
				<div class="tit_100 poa">
					<label>작업시간</label>
				</div>
				<div class="ml_100 vat">
					<input id="txtRunTime" name="txtRunTime" type="text" class="vat" autocomplete="off">
					<span class="btn_calendar vat"><i class="fa fa-clock-o"></i></span>
				</div>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-20 dib">
				<div class="tit_100 poa">
					<label>최종작업일자</label>
				</div>
				<div class="ml_100 vat">
					<input id="txtRundate" type="text" class="width-100"> 
				</div>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-20 dib">
				<div class="tit_100 poa">
					<label>삭제주기</label>
				</div>
				<div class="ml_100 vat">
					<input id="txtDelCycle" type="text" class="width-100"> 
				</div>
			</div>
			<div class="width-20 dib vat">
				<div id="cboDelCycle" data-ax5select="cboDelCycle" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%"></div> 
			</div>
		</div>
		
		<div class="row vat" style="height: calc(100% - 183px)">
			<div class="width-100 dib">
				<div class="tit_100 poa">
					<label>예외시스템</label>
					<div class="vat">
						<input type="checkbox" class="checkbox-file" id="chkAllSys" data-label="전체선택"/>
					</div>
				</div>
				<div class="ml_100 vat" style="height: 100%; border: 1px dotted gray;; background-color: white; overflow-y: auto;">
					<ul class="list-group" id="ulSysInfo">
	    			</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/fileconfiguration/BasicInfoTab.js"/>"></script>