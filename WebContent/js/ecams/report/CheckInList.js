/**
  [보고서] 체크인현황
*/

var userId 		= window.top.userId;
var adminYN		= window.top.adminYN;

var gridProg	= new ax5.ui.grid();
var gridList	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var tmpInfo = {};
var options = [];
var columns = [];
var cboSysData = [];
var cboJobData = [];
var cboSinData = [];
var cboDeptData = [];
var cboStepData = [];
var cboGbnData = [];
var cboQryData = [];


var gridListData = [];
var sysAll = [];
var lastChk = '';

$('[data-ax5select="cboSys"]').ax5select({
	options : []
});
$('[data-ax5select="cboJob"]').ax5select({
	options : []
});
$('[data-ax5select="cboSin"]').ax5select({
	options : []
});
$('[data-ax5select="cboDept"]').ax5select({
	options : []
});
$('[data-ax5select="cboStep"]').ax5select({
	options : []
});
$('[data-ax5select="cboGbn"]').ax5select({
	options : []
});
$('[data-ax5select="cboQry"]').ax5select({
	options : []
});

picker.bind(defaultPickerInfoLarge('basic', 'top'));

gridList.setConfig({
    target: $('[data-ax5grid="gridList"]'),
    sortable: true,
    multiSort: true,
    showLineNumber: true,
    header: {
        align: "center",
        columnHeight: 30
    },
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_sysmsg",		label: "시스템",		width: '10%',  align: "left"},
        {key: "cm_jobname",		label: "업무",		width: '10%',  align: "left"},
        {key: "spms",			label: "SPMS",		width: '5%',  align: "left"},
        {key: "cm_deptname",	label: "신청부서",		width: '10%',  align: "left"},
        {key: "acptno",			label: "신청번호",		width: '10%',  align: "left"},
        {key: "cm_codename",	label: "구분",		width: '8%',   align: "left"},
        {key: "cm_username",	label: "신청자",		width: '6%',   align: "center"},
        {key: "cr_sayu",		label: "신청근거",		width: '12%',  align: "center"},
        {key: "cr_passcd",		label: "신청사유",		width: '6%',   align: "left"},
        {key: "cr_acptdate",	label: "신청일시",		width: '9%',  align: "left"},
        {key: "cr_prcdate",		label: "완료일시",		width: '9%',  align: "left"},
        {key: "requestgb",		label: "신청구분",		width: '8%',  align: "center"},
        {key: "sta",			label: "진행상태",		width: '8%',  align: "center"},
        {key: "gbn",			label: "처리구분",		width: '8%',  align: "center"},
        {key: "cm_dirpath",		label: "디렉토리",		width: '15%',  align: "left"},
        {key: "cr_rsrcname",	label: "프로그램명",	width: '15%',  align: "left"},
    ]
});
//이 부분 지우면 영어명칭으로 바뀜
//ex) 월 -> MON
ax5.info.weekNames = [
 {label: "일"},
 {label: "월"},
 {label: "화"},
 {label: "수"},
 {label: "목"},
 {label: "금"},
 {label: "토"}
];


$('#dateSt').val(getDate('DATE',0));
$('#dateEd').val(getDate('DATE',0));

picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

$('input:radio[name=rdoDate]').wRadio({theme: 'circle-radial red', selector: 'checkmark'});

$(document).ready(function() {
	
	
	getCodeInfo();
	
	//조회
	$("#btnQry").on('click', function() {
		btnQry_click();
	});
	
	//시스템 변경
	$("#cboSys").on('change', function() {
		cboSysCd_change();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("체크인현황_" + today + ".xls");
	});
	
	$('#txtSPMS').bind('keypress',function(event){
		if(event.keyCode == 13){
			$("#btnQry").trigger('click');
		}
	});
	
	$('#txtUser').bind('keypress',function(event){
		if(event.keyCode == 13){
			$("#btnQry").trigger('click');
		}
	});
	
	$('#txtRsrc').bind('keypress',function(event){
		if(event.keyCode == 13){
			$("#btnQry").trigger('click');
		}
	});
});

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ new CodeInfo('CHECKIN', 'ALL','N'),new CodeInfo('APPROVAL', 'ALL','N'),new CodeInfo('REQCD', 'ALL','N')]);
	cboSinData = codeInfos.CHECKIN;
	
	
	cboSinData.splice(1,0, {"cm_macode":"CHECKIN","cm_micode":"99","cm_codename":"신규+수정"});
	$('[data-ax5select="cboSin"]').ax5select({
		options: injectCboDataToArr(cboSinData, 'cm_micode' , 'cm_codename')
	});
	
	cboStepData = [{"cm_micode":"0","cm_codename":"전체"},{"cm_micode":"1","cm_codename":"미완료"},{"cm_micode":"9","cm_codename":"완료"}];
	$('[data-ax5select="cboStep"]').ax5select({
		options: injectCboDataToArr(cboStepData, 'cm_micode' , 'cm_codename')
	});
	
	
	cboGbnData = [{"cm_macode":"REQPASS","cm_micode":"ALL","cm_codename":"전체"},{"cm_macode":"REQPASS","cm_micode":"4","cm_codename":"일반적용"}
					,{"cm_macode":"REQPASS","cm_micode":"0","cm_codename":"수시적용"},{"cm_macode":"REQPASS","cm_micode":"2","cm_codename":"긴급적용"}];
	$('[data-ax5select="cboGbn"]').ax5select({
		options: injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
	});
	
	cboQryData = [{"cm_macode":"REQCD","cm_micode":"00","cm_codename":"전체"},{"cm_macode":"REQCD","cm_micode":"03","cm_codename":"테스트"}
					,{"cm_macode":"REQCD","cm_micode":"04","cm_codename":"운영"}];
	$('[data-ax5select="cboQry"]').ax5select({
			options: injectCboDataToArr(cboQryData, 'cm_micode' , 'cm_codename')
	});
	
	//$('[data-ax5select="cboReq"]').ax5select('setValue', '1', true);
	getTeamInfo();
	
}

function getTeamInfo(){
	tmpInfo = {
		SelMsg		: 'ALL',
		cm_useyn	: 'Y',
		gubun		: 'sub',
		itYn		: 'N',
		requestType	: 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', tmpInfo, 'json', successGetTeamInfo);
}

function successGetTeamInfo(data) {
	cboDeptData = data;
	$('[data-ax5select="cboDept"]').ax5select({
		options: injectCboDataToArr(cboDeptData, 'cm_deptcd' , 'cm_deptname')
	});
	getSysInfo();
}

function getSysInfo(){
	
	tmpInfo = {
			UserId		: userId,
			SecuYn		: 'N',
			SelMsg		: 'ALL',
			CloseYn		: 'N',
			ReqCd		: '',
			requestType	: 'getSysInfo'
	}
	
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetSysInfo);
}

function successGetSysInfo(data){
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	$('#cboSys').trigger('change');
	
}

function cboSysCd_change(){
	if(getSelectedIndex('cboSys') > 0){
		tmpInfo = {
			UserID		: userId,
			SysCd		: getSelectedVal('cboSys').value,
			CloseYn		: 'n',
			SelMsg		: 'ALL',
			requestType	: 'getJobInfo_Rpt'
		}
		
		ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetJobInfo);
	} else{
		cboJobData = [{"cm_jobname":"전체","cm_jobcd" : "0000"}];
		$('[data-ax5select="cboJob"]').ax5select({
			options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
		});
	}
	
	$("#btnQry").trigger('click');
}

function successGetJobInfo(data){
	cboJobData = data;
	$('[data-ax5select="cboJob"]').ax5select({
		options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
	});
}

function btnQry_click(){
	
	var strSys = "";
	var strQry = "";
	var strDept = "";
	var strJob = "";
	var strStD = "";
	var strEdD = "";
	var strGbn = "";
	var strPrc = "";
	var strQrycd = "";
	
	if (getSelectedIndex('cboSys') > 0) strSys = getSelectedVal('cboSys').value;
    if (getSelectedIndex('cboJob') > 0) strJob = getSelectedVal('cboJob').value;
    if (getSelectedIndex('cboSin') > 0) strQry = getSelectedVal('cboSin').value;
    if (getSelectedIndex('cboDept') > 0) strDept = getSelectedVal('cboDept').value;
    if (getSelectedIndex('cboGbn') > 0) strGbn = getSelectedVal('cboGbn').value;
    if (getSelectedIndex('cboStep') > 0) strPrc = getSelectedVal('cboStep').value;
	if (getSelectedIndex('cboQry') > 0) strQrycd = getSelectedVal('cboQry').value;
	strStD = replaceAllString($('#dateSt').val(),'/','');
	strEdD = replaceAllString($('#dateEd').val(),'/','');
	
	tmpInfo = {
		StDate		: strStD,
		EdDate		: strEdD,
		Txt_PrgName		: $('#txtRsrc').val().trim(),
		Cbo_SysCd		: strSys,
		Cbo_JobCd		: strJob,
		Cbo_Steam		: strDept,
		Txt_UserId		: $('#txtUser').val().trim(),
		Cbo_Sin		: strQry,
		gbn		: strGbn,
		proc	: strPrc,
		spms	: $('#txtSPMS').val().trim(),
		dategbn	: $('input:radio[name=rdoDate]:checked').val(),
		QryCd	: strQrycd,
		requestType	: 'get_SelectList'
	}
	
	gridListData = ajaxCallWithJson('/webPage/ecmp/Cmp0600Servlet', tmpInfo, 'json');
	gridList.setData(gridListData);
}

