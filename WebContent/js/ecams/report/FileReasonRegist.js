/**
  [보고서] 파일대사사유등록
*/

var userId 		= window.top.userId;
var adminYN		= window.top.adminYN;
var rgtList	    = window.top.rgtList;

var mainGrid	= new ax5.ui.grid();

var tmpInfo = {};
var options = [];
var gridData = [];
var cboSysData = [];
var cboJobData = [];
var cboGubunData = [];
var cboSayuData = [];
var cboRsrcData = [];
var cboDirData = [];
var admin = '';
var intGubun = '';

mainGrid.setConfig({
    target: $('[data-ax5grid="mainGrid"]'),
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
            gridList_click();
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "scmgubun",		label: "적용구분",		width: '6%',  align: "left"},
        {key: "cm_sysmsg",		label: "시스템",		width: '7%',  align: "left"},
        {key: "cm_jobname",		label: "업무",		width: '7%', align: "left"},
        {key: "cm_dirpath",		label: "프로그램경로",	width: '15%', align: "left"},
        {key: "cm_codename",	label: "프로그램종류",	width: '8%',  align: "left"},
        {key: "cd_rsrcname",	label: "프로그램명",	width: '12%', align: "left"},
        {key: "cd_opendate",	label: "최초등록일",	width: '5%',  align: "center"},
        {key: "cd_lastdate",	label: "최종등록일",	width: '5%',  align: "center"},
        {key: "editor",			label: "등록자",		width: '4%',  align: "center"},
        {key: "cd_clsdate",		label: "삭제일",		width: '5%',  align: "center"},
        {key: "clseditor",		label: "삭제자",		width: '4%',  align: "center"},
        {key: "diffre",			label: "사유구분",		width: '10%', align: "left"},
        {key: "cd_reqdoc",		label: "등록사유",		width: '15%', align: "left"}
    ]
});

$(document).ready(function() {
	
	$('#divDirCbo').css('display','none');
	
	getSysInfo_Rpt();	
	getUserInfo();
	getCodeInfo();
	
	//삭제 제외 체크
	$("#delCheck").on('click', function() {
		btnQry_click();
	});
	
	//등록
	$("#btnInsert").on('click', function() {
		btnInsert_click();
	});
	
	//삭제
	$("#btnDel").on('click', function() {
		btnDel_click();
	});
	
	//조회
	$("#btnQry").on('click', function() {
		btnQry_click();
	});
	
	//시스템 변경
	$("#Cbo_SysCd").on('change', function() {
		cboSysCd_change();
	});
	
	//적용구분 변경
	$("#Cbo_gubun").on('change', function() {
		cboGubun_change();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		//mainGrid.exportExcel("파일대사사유등록_" + userId + ".xls");
		excelExport(mainGrid,"파일대사사유등록_" + userId + ".xls");
	});
});

function getSysInfo_Rpt(){
	
	tmpInfo = {
		UserId		: userId,
		SelMsg		: 'ALL',
		CloseYn		: 'N',
		SysCd		: '',
		requestType	: 'getSysInfo_Rpt'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetSysInfo);
}

function successGetSysInfo(data){
	
	cboSysData = data;
	
	cboSysData = cboSysData.filter(function(data){
		if(data.cm_sysinfo.substr(0,1) == '1'){
			return false;
		} else{
			return true;
		}
	});
		
	$('[data-ax5select="Cbo_SysCd"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function getCodeInfo(){
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('DIFFREJECT','SEL','Y'),
		new CodeInfo('CMD0028','','Y')
		]);
	
	cboSayuData = codeInfos.DIFFREJECT;
	cboGubunData = codeInfos.CMD0028;
	
	
	$('[data-ax5select="Cbo_Sayu"]').ax5select({
		options : injectCboDataToArr(cboSayuData, 'cm_micode' , 'cm_codename')
	});
	
	$('[data-ax5select="Cbo_gubun"]').ax5select({
		options : injectCboDataToArr(cboGubunData, 'cm_micode' , 'cm_codename')
	});
}


function getUserInfo(){
	
	if(adminYN == 'true'){
		admin = '1';
	} else{
		admin = '0';
	}
}

function btnQry_click(){
	
	var delCheck = '';
	
	if($('#delCheck').is(':checked')){
		delCheck = 'true';
	}
	
	tmpInfo = {
		strSys		: getSelectedVal('Cbo_SysCd').value,
		delCheck	: delCheck,
		requestType	: 'get_Result'
	}
	
	gridData = ajaxCallWithJson('/webPage/ecmp/Cmp2800Servlet', tmpInfo, 'json');
	mainGrid.setData(gridData);
}

function btnInsert_click(){

	$('#Txt_pgmname').val($('#Txt_pgmname').val().trim());
	$('#Txt_Sayu').val($('#Txt_Sayu').val().trim());
	$('#Txt_dir').val($('#Txt_dir').val().trim());
	
	if($('#Txt_pgmname').val() == ''){
		dialog.alert('프로그램명을 입력해주세요.');
		$('#Txt_pgmname').focus();
		return;
	}
	if(getSelectedIndex('Cbo_SysCd') == 0){
		dialog.alert('시스템을 선택해주세요.');
		return;
	}
	if(getSelectedIndex('Cbo_JobCd') == 0){
		dialog.alert('업무를 선택해주세요.');
		return;
	}
	if(getSelectedIndex('Cbo_rsrc') == 0){
		dialog.alert('프로그램종류를 선택해주세요.');
		return;
	}
	if(getSelectedIndex('Cbo_gubun') == 1 && $('#Txt_dir').val() == ''){
		dialog.alert('프로그램경로를 입력해주세요.');
		$('#Txt_dir').focus();
		return;
	}
	if(getSelectedIndex('Cbo_Sayu') == 0){
		dialog.alert('사유구분을 입력해주세요.');
		return;
	}
	if($('#Txt_Sayu').val() == ''){
		dialog.alert('등록사유를 입력해주세요.');
		$('#Txt_Sayu').focus();
		return;
	}
	
	tmpInfo = {
		gubun		: getSelectedVal('Cbo_gubun').value,
		syscd		: getSelectedVal('Cbo_SysCd').value,
		jobcd		: getSelectedVal('Cbo_JobCd').value,
		rsrccd		: getSelectedVal('Cbo_rsrc').value,
		dirpath		: $('#Txt_dir').val(),
		sayu		: getSelectedVal('Cbo_Sayu').value,
		pgmname		: $('#Txt_pgmname').val(),
		txtsayu		: $('#Txt_Sayu').val(),
		strUserId	: userId,
		requestType	: 'InsertData'
	}
	
	ajaxAsync('/webPage/ecmp/Cmp2800Servlet', tmpInfo, 'json', successInsertData);
}

function successInsertData(chk){
	
	if(chk == '1'){
		dialog.alert('등록되지 않은 프로그램입니다.\n프로그램, 디렉토리를 다시 확인하여 주십시오.');
		return;
	} else if(chk == '0'){
		dialog.alert('프로그램이 변경되었습니다.');
	} else if(chk == '2'){
		dialog.alert('프로그램이 추가되었습니다.');
	}
	
	btnQry_click();
}

function btnDel_click(){
	
	$('#Txt_pgmname').val($('#Txt_pgmname').val().trim());
	$('#Txt_Sayu').val($('#Txt_Sayu').val().trim());
	$('#Txt_dir').val($('#Txt_dir').val().trim());
	
	if($('#Txt_pgmname').val() == ''){
		dialog.alert('프로그램명을 입력해주세요.');
		$('#Txt_pgmname').focus();
		return;
	}
	if(getSelectedIndex('Cbo_SysCd') == 0){
		dialog.alert('시스템을 선택해주세요.');
		return;
	}
	if($('#Txt_Sayu').val() == ''){
		dialog.alert('등록사유를 입력해주세요.');
		$('#Txt_Sayu').focus();
		return;
	}
	
	tmpInfo = {
		gubun		: getSelectedVal('Cbo_gubun').value,
		syscd		: getSelectedVal('Cbo_SysCd').value,
		dirpath		: $('#Txt_dir').val(),
		pgmname		: $('#Txt_pgmname').val(),
		strUserId	: userId,
		txtsayu		: $('#Txt_Sayu').val(),
		requestType	: 'DelData'
	}

	ajaxAsync('/webPage/ecmp/Cmp2800Servlet', tmpInfo, 'json', successDelData);
}

function successDelData(chk){
	
	if(chk == '1'){
		dialog.alert('등록되지 않은 프로그램입니다.\n프로그램명, 디렉토리를 다시 확인하여 주십시오.');
		return;
	}
	if(chk == '0' || chk == '2'){
		dialog.alert('프로그램이 삭제되었습니다.');
	}
	if(chk == '3'){
		dialog.alert('등록되지 않은 프로그램입니다.');
	}
	
	btnQry_click();
}

function cboSysCd_change(){
	
	var syscd = getSelectedVal('Cbo_SysCd').value;
	var disable = false;
	
	if(getSelectedIndex('Cbo_SysCd') < 1){
		syscd = '';
		disable = true;
		$('[data-ax5select="Cbo_dir"]').ax5select({});
	}
	$('#Cbo_rsrc').prop('disabled',disable);
	$('#Cbo_JobCd').prop('disabled',disable);
	$('#Cbo_dir').prop('disabled',disable);
	
	tmpInfo = {
		UserID		: userId,
		SysCd		: getSelectedIndex('Cbo_SysCd') > 0 ? getSelectedVal('Cbo_SysCd').value : '',
		SecuYn		: 'Y',
		CloseYn		: 'N',
		SelMsg		: 'SEL',
		sortCd		: 'NAME',
		requestType	: 'getJobInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetJobInfo);	
	
}

function getRsrcInfo(syscd){
	
	tmpInfo = {
			syscd		: syscd,
			requestType	: 'get_rsrcInfo'
		}

		ajaxAsync('/webPage/ecmp/Cmp2800Servlet', tmpInfo, 'json', successGetRsrcInfo);
}

function successGetJobInfo(data){
	
	cboJobData = data;
		
	$('[data-ax5select="Cbo_JobCd"]').ax5select({
		options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
	});
	
	if(cboJobData.length > 0 && mainGrid.getList('selected')[0]){ // && mainGrid.getList('selected')[0] 조건 추가
		$('[data-ax5select="Cbo_JobCd"]').ax5select('setValue', mainGrid.getList('selected')[0].cd_jobcd, true);
		getRsrcInfo(getSelectedVal('Cbo_SysCd').value);
	} else if(getSelectedIndex('Cbo_SysCd') > 0) getRsrcInfo(getSelectedVal('Cbo_SysCd').valu);
}

function successGetRsrcInfo(data){
	
	cboRsrcData = data;
	
	$('[data-ax5select="Cbo_rsrc"]').ax5select({
		options : injectCboDataToArr(cboRsrcData, 'cm_micode' , 'cm_codename')
	});
	
	if(cboRsrcData.length > 0 && mainGrid.getList('selected')[0]){ // && mainGrid.getList('selected')[0] 조건 추가
		$('[data-ax5select="Cbo_rsrc"]').ax5select('setValue', mainGrid.getList('selected')[0].cd_rsrccd, true);
	}
	//cboRsrc_change();
}

function cboRsrc_change(){
	
	if(cboSysData.length > 0 && cboJobData.length > 0 && cboRsrcData.length > 0){
		tmpInfo = {
			syscd		: getSelectedVal('Cbo_SysCd').value,
			jobcd		: getSelectedVal('Cbo_JobCd').value,
			rsrccd		: getSelectedVal('Cbo_rsrc').value,
			admin		: admin,
			strId		: userId,
			requestType	: 'get_dirInfo'
		}
		//cbo_dir 사용하지 않음
		ajaxAsync('/webPage/ecmp/Cmp2800Servlet', tmpInfo, 'json', successGetDirInfo);
	}
}
/*
function successGetDirInfo(data){
	
	cboDirData = data;
		
	$('[data-ax5select="Cbo_dir"]').ax5select({
		options : injectCboDataToArr(cboDirData, 'cm_dsncd' , 'cm_dirpath')
	});
	
	if(cboDirData.length > 0 && mainGrid.getList('selected')[0]){ // && mainGrid.getList('selected')[0] 조건 추가
		$('#Txt_dir').val(mainGrid.getList('selected')[0].cm_dirpath);
	}
}
*/
function cboGubun_change(){
	
	if(getSelectedIndex('Cbo_gubun') == 0){
		intGubun = 0;
	} else {
		intGubun = 1;
	}
}

function gridList_click(){
	
	var selectItem = mainGrid.getList('selected')[0];
	
	$('#Txt_pgmname').val(selectItem.cd_rsrcname);
	$('#Txt_Sayu').val(selectItem.cd_reqdoc);
	$('#Txt_dir').val(selectItem.cm_dirpath);
	
	if (cboGubunData.length>0) {
		$('[data-ax5select="Cbo_gubun"]').ax5select('setValue',selectItem.cd_gubun,true);
		cboGubun_change();
	}
	if (cboSayuData.length>0) {
		$('[data-ax5select="Cbo_Sayu"]').ax5select('setValue',selectItem.cd_sayucd,true);
	}
	if (cboSysData.length>0) {
		$('[data-ax5select="Cbo_SysCd"]').ax5select('setValue',selectItem.cd_syscd,true);
		cboSysCd_change();
	}
	$('#Txt_dir').val(selectItem.cm_dirpath);
	
}














