/**
 * 시스템 - SR업무 추가 페이지
 * 
 * <pre>
 * 	작성자	: 허정규
 * 	버전 		: 1.0
 *  수정일 	: 2020-04-13
 * 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var srGrid = new ax5.ui.grid();

var options = null;
var cboSysData = null;
var cboBaUserData = null;
var cboPGMUserData = null;
var cboDevMasterData = [];

var devMaster = "";

$('[data-ax5select="cboSys"]').ax5select({
	options: []
});

$('[data-ax5select="cboBA"]').ax5select({
	options: []
});

$('[data-ax5select="cboPGM"]').ax5select({
	options: []
});

$('[data-ax5select="cboDevMaster"]').ax5select({
	options: []
});


srGrid.setConfig({
	target : $('[data-ax5grid="srListGrid"]'),
	sortable : true,
	multiSort : true,
    multipleSelect: true,
    showRowSelector: true, //checkbox option
	header : {
		align : "center",
		columnHeight : 30
	},
	body : {
		columnHeight : 28,
		onClick : function() {
			//this.self.clearSelect();
			this.self.select(this.dindex);
			$('#txtSrTitle').val(this.item.cm_srjobname);
			$('[data-ax5select="cboSys"]').ax5select("setValue",this.item.cm_syscd, true);
			$('[data-ax5select="cboBA"]').ax5select("setValue",this.item.cm_bauserid, true);
			//$('[data-ax5select="cboPGM"]').ax5select("setValue",this.item.cm_pgmuserid, true);
			devMaster = this.item.cm_devuserid;
			//setDevMaster();
		}
	},
	columns : [ {
		key : "cm_sysmsg",
		label : "시스템",
		width : '33%'
	},{
		key : "cm_srjobname",
		label : "SR업무명",
		width : '33%'
	},  {
		key : "baUserName",
		label : "BA",
		width : '33%'
	}
	/* width: 25, 25, 16, 16, 16*/
	/*, {
		key : "pgmUserName",
		label : "PGM담당자",
		width : '16%'
	}, {
		key : "devUserName",
		label : "개발책임자",
		width : '16%'
	}
	*/
	],
	page : {
		display : false
	}
});

$(document).ready(function(){	
	setBaUserInfo();
	
	$('#btnExit').bind('click',function() {
		popClose();
	});
	
	$('#btnClear').bind('click',function() {
		btnClearClick();
	});
	
	$('#cboSys').bind('change',function() {
		//setDevMaster();
	});
	
	$('#btnQry').bind('click',function(){
		getSrJobList();
	});

	$('#btnReq').bind('click',function(){
		insertSrJob();
	});
	
	$('#btnDel').bind('click',function(){
		deleteSrJob();
	});
});

function setBaUserInfo(){
	var ajaxReturnData = null;
	var userInfo = {
		requestType : 'getBaUserInfo'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/srcommon/SRRegister',
			userInfo, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboBaUserData = ajaxReturnData;
		options = [];

		$('[data-ax5select="cboBA"]').ax5select(
				{
					options : injectCboDataToArr(cboBaUserData, 'cm_userid', 'cm_hap')
				});
	}
	setPGMUserInfo();
}

function setPGMUserInfo(){
	var ajaxReturnData = null;
	var userInfo = {
		requestType : 'getPGMUserInfo'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/srcommon/SRRegister',
			userInfo, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboPGMUserData = ajaxReturnData;
		options = [];

		$('[data-ax5select="cboPGM"]').ax5select(
				{
					options : injectCboDataToArr(cboPGMUserData, 'cm_userid', 'cm_hap')
				});
	}
	setSys();
}

function setSys(){
	devMaster = "";
	var ajaxReturnData = null;
	var sysData = new Object();
	
	sysData.UserId = userId;
	sysData.SecuYn = "Y";
	sysData.SelMsg = "SEL";
	sysData.CloseYn = "N";
	sysData.ReqCd = "";
	
	var data = {
			sysData : sysData,
		requestType : 'getSysInfo'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/common/SysInfoServlet',data, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboSysData = ajaxReturnData;
		options = [];

		$('[data-ax5select="cboSys"]').ax5select(
				{
					options : injectCboDataToArr(cboSysData, 'cm_syscd',
							'cm_sysmsg')
				});
	}
	getSrJobList();
}

function setDevMaster(){
	sysCd = getSelectedVal("cboSys").value;
	var data = {
			sysCd : sysCd,
		requestType : 'getUserSys2'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/common/CommonUserInfo',data, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboDevMasterData = ajaxReturnData;
		options = [];
		for(var i=0; i < cboDevMasterData.length ; i++){
			if(cboDevMasterData[i].cm_rgtcd != "90"){
				cboDevMasterData.splice(i,1);
				i--;
			}
		};
		cboDevMasterData.splice(0,0,{"cc_editor": "", "cm_username" : "선택하세요"})
		$('[data-ax5select="cboDevMaster"]').ax5select(
				{
					options : injectCboDataToArr(cboDevMasterData, 'cc_editor',
							'cm_username')
				});
	}
	if(devMaster != ""){
		$('[data-ax5select="cboDevMaster"]').ax5select("setValue", devMaster, true);
	}
}

function btnClearClick(){
	$("#txtSrTitle").val('');
	$('[data-ax5select="cboSys"]').ax5select("setValue",cboSysData[0].cm_syscd,"true");
	$('[data-ax5select="cboBA"]').ax5select("setValue",cboBaUserData[0].cm_userid,"true");
	$('[data-ax5select="cboPGM"]').ax5select("setValue",cboPGMUserData[0].cm_userid,"true");
	
	options = [];
	$('[data-ax5select="cboDevMaster"]').ax5select({options : options});
}

function getSrJobList(){
	var ajaxReturnData = null;
	var srJobData = new Object();
	
	srJobData.srJobName = $("#txtSrTitle").val().trim();
	if(getSelectedIndex("cboSys") > 0) srJobData.sysCd = getSelectedVal("cboSys").value;
	if(getSelectedIndex("cboBA") > 0) srJobData.srBaUserId = getSelectedVal("cboBA").value;
	if(getSelectedIndex("cboPGM") > 0) srJobData.srPgmUserId = getSelectedVal("cboPGM").value;
	if(cboDevMasterData.length > 0 && getSelectedIndex("cboDevMaster") > 0){
		srJobData.srDevUserId = getSelectedVal("cboDevMaster").value;
	}
		
	var data = {
			srJobData : srJobData,
		requestType : 'getSrJobList'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/administrator/SysInfoServlet',
			data, 'json');
	if (ajaxReturnData !== 'ERR') {
		srGrid.setData(ajaxReturnData);
	}
}

function insertSrJob(){
	var ajaxReturnData = null;
	var srJobData = new Object();
	
	srJobData.userId = userId;
	srJobData.srJobName = $("#txtSrTitle").val().trim();
	if(getSelectedIndex("cboSys") > 0) srJobData.sysCd = getSelectedVal("cboSys").value;
	if(getSelectedIndex("cboBA") > 0) srJobData.srBaUserId = getSelectedVal("cboBA").value;
	if(getSelectedIndex("cboPGM") > 0) srJobData.srPgmUserId = getSelectedVal("cboPGM").value;
	if(cboDevMasterData.length > 0 && getSelectedIndex("cboDevMaster") > 0){
		srJobData.srDevUserId = getSelectedVal("cboDevMaster").value;
	}
	
	if(srJobData.srJobName.length < 1){
		dialog.alert("SR업무명을 입력해 주세요");
		return;
	}
	
	/*
	if(getSelectedIndex("cboBA") < 1){
		dialog.alert("BA 를 선택해 주세요");
		return;
	}
	*/
	/*
	if(getSelectedIndex("cboPGM") < 1){
		dialog.alert("PGM담당자 를 선택해 주세요");
		return;
	}
	*/
	
	var data = {
			srJobData : srJobData,
		requestType : 'insertSrJob'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/administrator/SysInfoServlet',
			data, 'json');
	if (ajaxReturnData !== 'ERR') {
		if(ajaxReturnData == "1"){
			dialog.alert("SR업무 등록이 완료되었습니다.");
		} else {
			dialog.alert("SR업무 수정이 완료되었습니다.");
		}
		btnClearClick();
		getSrJobList();
	}
}

function deleteSrJob(){
	var srJobDataArray = srGrid.getList('selected');
	if(srJobDataArray.length < 1){
		dialog.alert("삭제할 SR업무를 선택해 주세요.");
		return;
	}

	var data = {
			srJobData : srJobDataArray,
		requestType : 'deleteSrJob'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/administrator/SysInfoServlet',
			data, 'json');
	if (ajaxReturnData !== 'ERR') {
		if(ajaxReturnData == "1"){
			dialog.alert("SR업무 삭제가 완료되었습니다.");
		} 
		btnClearClick();
		getSrJobList();
	}
	
	
}

function popClose() {
	window.parent.sysSrJobModal.close();
}