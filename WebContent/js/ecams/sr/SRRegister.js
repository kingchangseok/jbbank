/**
 * SR접수(eCmc0100.mxml) 화면 기능 정의
 * 
 * <pre>
 * 	작성자:
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 * 
 */

var userName 		= window.top.userName;
var userId 			= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName	= window.top.userDeptName;
var userDeptCd 		= window.top.userDeptCd;
var reqCd 			= window.top.reqCd;

var load1Sw			= false;
var load2Sw			= false;
var testFlag		= false;
var srData 			= new Object(); // to SRRegisterTab
var cboQryGbnData 	= [
	{cm_codename : "전체",		cm_micode : "00", cm_dateyn: "Y"},
	{cm_codename : "SR수정대상",	cm_micode : "01", cm_dateyn: "N"}
]

$(document).ready(function() {
	reqCd = '41';
	
	document.getElementById('frmPrjList').onload = function() {
		load1Sw = true;
		
		var tab0 = $('#frmPrjList').get(0).contentWindow;
		if(tab0 != null && tab0 != undefined) {
			tab0.screenInit();
			tab0.cboQryGbn_click();
		}
	}
	
	document.getElementById('frmSRRegister').onload = function() {
		load2Sw = true;
	}
});

// PrjListTab에서 호출
function subScreenInit() {
	if(!load2Sw) {
		setTimeout(function() {
			document.getElementById('frmSRRegister').onload = function() {
				load2Sw = true;
			}
		}, 10);
	}
	
	if(load2Sw) {
		var tab1 = $('#frmSRRegister').get(0).contentWindow;
		if(tab1 != null && tab1 != undefined) {
			tab1.screenInit('NEW');
		}
	}
}

// PrjListTab에서 호출
function isrID_click(data) {
	console.log('[SRRegister.js] isrID_click ==>', data);
	srData = new Object();
	srData.strEditor = data.cc_createuser;
	srData.strStatus = data.cc_status;
	srData.strIsrId = data.cc_srid;
	
	if(!load2Sw) {
		setTimeout(function() {
			document.getElementById('frmSRRegister').onload = function() {
				load2Sw = true;
			}
		}, 10);
	}
	
	if(load2Sw) {
		var tab1 = $('#frmSRRegister').get(0).contentWindow;
		
		tab1.strEditor = data.cc_createuser;
		tab1.strStatus = data.cc_status;
		tab1.strIsrId = data.cc_srid;
		tab1.screenInit('NEW');
		
		if(testFlag) {
			tab1.grdPrj_click(data.cc_srid);
		}else {
			tab1.$("#button_update").prop("disabled", true);
			tab1.$("#button_register").prop("disabled", false);
		}
		
		testFlag = true;
	}
}

// SRRegisterTab에서 호출
function clearIdx() {
	var tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.firstGrid.clearSelect();
	}
}

//SRRegisterTab에서 호출
function subCmdQry_Click() {
	var tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.getPrjList();
	}
}