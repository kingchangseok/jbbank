/**
 * [릴리즈관리 > 릴리즈대상현황] 
 */
var userId 		= window.top.userId;
var codeList    = window.top.codeList;          //전체 코드 리스트
var adminYN 	= window.top.adminYN;		// 관리자여부

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var qrySw	= true;
var options = [];
var tmpInfo = {};

picker.bind(defaultPickerInfo('basic', 'top'));

ax5.info.weekNames = [ 
	{label : "일"}, 
	{label : "월"}, 
	{label : "화"}, 
	{label : "수"}, 
	{label : "목"}, 
	{label : "금"}, 
	{label : "토"}
 ];

//picker에 오늘 날짜 디폴트로 세팅
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
	$("#dateEd").val(today);
	$("#dateSt").val(today);
})

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function(){
        	releaseWinOpen('G'+this.cr_qrycd, this.acptno);
        }
	},
	columns : 
		[ 
			{key : "cm_sysmsg",		label : "시스템",		align : "left",		width: "13%"}, 
			{key : "acptno",		label : "신청번호",	align : "left",		width: "10%"}, 
			{key : "acptdate",		label : "신청일시",	align : "center",	width: "13%"}, 
			{key : "cm_username",	label : "신청인",		align : "center",	width: "8%"}, 
			{key : "cm_svrname",	label : "적용서버",	align : "left",		width: "10%"}, 
			{key : "cm_codename",	label : "프로그램종류",	align : "left",		width: "10%"}, 
			{key : "cr_rsrcname",	label : "프로그램",	align : "center",	width: "13%"}, 
			{key : "cm_dirpath",	label : "프로그램경로",	align : "left",		width: "15%"}, 
			{key : "sysgbn",		label : "적용구분",	align : "left",		width: "10%"},
			{key : "cm_volpath",	label : "적용경로",	align : "left",		width: "15%"}, 
			{key : "rst",			label : "처리결과",	align : "left",		width: "7%"}, 
			{key : "prcdate",		label : "완료일시",	align : "center",	width: "10%"}
		],
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {
			'arrow': '<i class="fa fa-caret-right"></i>'
		},
//		items: [
//			{type: 1, label: "요구관리정보"},
//			{type: 2, label: "변경관리정보"},
//			{type: 3, label: "테스트관리정보"},
//			{type: 4, label: "신청상세내역"}  
//		],
		popupFilter: function (item, param) {
			mainGrid.clearSelect();
			mainGrid.select(Number(param.dindex));
        	 
        	var selIn = mainGrid.selectedDataIndexs;
        	if(selIn.length == 0) return;
        	 
         	if (param.item == undefined) return false;
         	if (param.dindex < 0) return false;
         	
         	return true;
		},
		onClick: function (item, param) {
			openWindow(item.type, param.item.CC_ISRID, param.item.CC_ISRSUB, param.item.CC_ACPTNO);
			mainGrid.contextMenu.close();
		}
	}
}); 


$(document).ready(function() {
	
	var oldVal = '';
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $('#dateEd').val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	screenInit();
	
	getSysInfo();
	
	getTmpDir();
	
	getJobCodeInfo();
	
	//진행상태/적용구분
	cboSet();
	
	$('#CboSysCd').on('change', function(){
		CboSys_Change();
	});
	
	$('#btnQry').on('click', function(){
		btnQry_Click();
	});
	
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("릴리즈대상현황_" + today + ".xls");
	});
});

function screenInit(){
	
	$('[data-ax5select="CboSysCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="CboGbnCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="CboJobCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDeploy"]').ax5select({
		options: []
	});
}

function getSysInfo() {
	
	tmpInfo = {
		UserId : userId,
		SecuYn : 'N',
		SelMsg : 'ALL',
		CloseYn : 'N',
		ReqCd : '',
	}
	var data = {
		sysData : tmpInfo,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/CommonSysInfo', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	
	var sysData = data;
	options = [];
	
	sysData.filter(function(data){
		if (data.cm_sysinfo.substr(0,1) == '1') return false;
		else return true;
	})
	
	$.each(sysData, function(i, value) {
		options.push({value : value.cm_syscd, text : value.cm_sysmsg});
	});
	
	$('[data-ax5select="CboSysCd"]').ax5select({
		options: options
	});	
	
	CboSys_Change();
}

function CboSys_Change(){
	
	if(getSelectedIndex('CboSysCd') > 0){
		tmpInfo = {
				UserId : userId,
				SysCd : getSelectedVal('CboSysCd').value,
				SecuYn : 'Y',
				CloseYn : 'N',
				SelMsg : 'ALL',
				sortCd : 'NAME',
			}
			var data = {
				jobData : tmpInfo,
				requestType	: 'getJobInfo'
			}
			
			ajaxAsync('/webPage/common/CommonSysInfo', data, 'json', SuccessGetJobInfo);
	} else if(getSelectedIndex('CboSysCd') == 0){
		getJobCodeInfo();
	}
}

function SuccessGetJobInfo(data) {
	
	options = [];
	$('[data-ax5select="CboJobCd"]').ax5select({
		options: options
	});
	
	$.each(data, function(i, value) {
		options.push({value : value.cm_jobcd, text : value.cm_jobname});
	});
	
	$('[data-ax5select="CboJobCd"]').ax5select({
		options: options
	});	
}

function getJobCodeInfo(){
	
	tmpInfo = {
			SelMsg : 'ALL',
			closeYn : 'N'
		}
		var data = {
			etcData : tmpInfo,
			requestType	: 'getJobList'
		}
		
		ajaxAsync('/webPage/administrator/SysInfoServlet', data, 'json', SuccessGetJobCodeInfo);
}

function SuccessGetJobCodeInfo(data) {
	
	options = [];
	$('[data-ax5select="CboJobCd"]').ax5select({
		options: options
	});	
	
	$.each(data, function(i, value) {
		options.push({value : value.cm_jobcd, text : value.cm_jobname});
	});
	
	$('[data-ax5select="CboJobCd"]').ax5select({
		options: options
	});	
}

function getTmpDir(){
	
	tmpInfo = {
		pcode		: '99', 
		requestType	: 'getSystemPath'
	}
	
	var strTmpDir = ajaxCallWithJson('/webPage/common/CommonSystemPath', tmpInfo, 'json');
	strTmpDir += '/';
}

function cboSet(){
	
	$('[data-ax5select="cboDeploy"]').ax5select({
	options: [
		{value: "ALL", text: "전체"}, //cm_micode, cm_codename
		{value: "TEST", text: "테스트적용"},
		{value: "REAL", text: "운영적용"}
		]
    });
	
	$('[data-ax5select="CboGbnCd"]').ax5select({
		options: [
			{value: "ALL", text: "전체"}, //cm_micode, cm_codename
			{value: "01", text: "릴리즈진행"},
			{value: "02", text: "릴리즈대기"},
			{value: "04", text: "릴리즈완료"}
			]
	});
}

function btnQry_Click(){
	
	var strSta = '00';
	var txtUserId = $('#TxtUserId').val().trim();
	var txtPgmId = $('#TxtPgmID').val().trim();
	
	if (qrySw == true) {
		if(getSelectedIndex('Cbo_GbnCd') > 0) strSta = getSelectedVal('Cbo_GbnCd').value;
		
		tmpInfo = {};
		tmpInfo.pStateCd = strSta;
		tmpInfo.pStartDt = replaceAllString($("#dateSt").val(), '/', '');
		tmpInfo.pEndDt = replaceAllString($("#dateEd").val(), '/', '');
		tmpInfo.pUserId = userId;
		tmpInfo.txtUserId = txtUserId;
		tmpInfo.txtPgmId = txtPgmId;
		
		var listData = {
			tmpInfo		: tmpInfo,
			requestType : 'get_SelectList'
		}
		
		var gridData = ajaxCallWithJson('/webPage/approval/ReleaseStatus', listData, 'json');
		
		gridData = gridData.filter(function(data){
			if (getSelectedIndex('CboSysCd') > 0){
				if (data.cr_syscd != getSelectedVal('CboSysCd').value) return false;
			}
			if (getSelectedIndex('CboJobCd') > 0){
				if (data.cr_jobcd != getSelectedVal('CboJobCd').value) return false;
			}
			if (getSelectedIndex('cboDeploy') > 0){
				if (getSelectedVal('cboDeploy').value == 'TEST' && data.cr_qrycd != '03') return false;
				else if (getSelectedVal('cboDeploy').value == 'REAL' && data.cr_qrycd != '04') return false;
			}
			if (txtUserId != '') {
				if (data.username.indexOf(txtUserId) < 0) return false; 
			}
			if (txtPgmId != '') {
				if (data.cr_rsrcname.indexOf(txtPgmId) < 0) return false; 
			}
			return true;
		})
		
		mainGrid.setData(gridData);
//		qrySw = false;
			
	} 
}

//$('#CboGbnCd').on('change', function(){
//	qrySw = true;
//});
//
//$('#divPicker').on('change', function(){
//	qrySw = true;
//});

function releaseWinOpen(qryCd,acptNo) {
    
	var cURL = '';
	var nHeight = 740;
    var nWidth  = 1500;
    var winName = 'popUp' + qryCd;
    
	if (qryCd == '') {
		cURL = '';
	} else if (qryCd == '') {
		cURL = '';
	}
	
	var f = document.popPam;   		
    
	f.user.value 	= userId;    	
    f.qrycd.value	= qryCd;    	
    f.acptno.value	= acptNo;    	
}
