/**
  [보고서] 형상관리운영현황
*/

var userId 		= window.top.userId;

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var tmpInfo = {};
var options = [];
var gridData = [];
var rsrcData = [];
var columns = [];


mainGrid.setConfig({
    target: $('[data-ax5grid="mainGrid"]'),
    sortable: true,
    multiSort: true,
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
    columns: columns
});

$('[data-ax5select="cboJob"]').ax5select({});

ax5.info.weekNames = [
    {label: "일"},
    {label: "월"},
    {label: "화"},
    {label: "수"},
    {label: "목"},
    {label: "금"},
    {label: "토"}
];


$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));

picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

$(document).ready(function() {
	
	
	$('#btnExcel').prop('disabled',true);
	
	getCodeInfo();
	
	getSysInfo_Rpt();
	
	//조회
	$("#btnQry1").on('click', function() {
		btnQry_click();
	});
	
	//시스템 변경
	$("#cboSys").on('change', function() {
		if(getSelectedIndex('cboSys') > 0){
			getJobInfo_Rpt();
		}
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		btnExcelClick();
	});
});

function getCodeInfo(){
	
	var codeInfos = getCodeInfoCommon([ new CodeInfo('RPTPRGGB', 'SEL','N'),
		                                new CodeInfo('REQPASS',  '','N'),
		                                new CodeInfo('RPTSTEP1', 'SEL','N')]);
	
	var cboGbnData = codeInfos.RPTPRGGB;
	var cboStepData = codeInfos.RPTSTEP1;
	rsrcData = codeInfos.REQPASS;
		

	$('[data-ax5select="cboGbn1"]').ax5select({
		options : injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
	});
	
	
	$.each(cboStepData, function(i,item){
		options : injectCboDataToArr(cboStepData, 'cm_micode' , 'cm_codename')
	});
	
	for(var j = 1; j < 5; j++){
		$('[data-ax5select="cboStep'+j+'"]').ax5select({
			options : cboStepData
		});
	}
	
	$('[data-ax5select="cboStep1"]').ax5select('setValue', cboStepData[1].cm_micode, true);
	$('[data-ax5select="cboStep2"]').ax5select('setValue', cboStepData[2].cm_micode, true);
	$('[data-ax5select="cboStep3"]').ax5select('setValue', cboStepData[3].cm_micode, true);
	$('[data-ax5select="cboStep4"]').ax5select('setValue', cboStepData[4].cm_micode, true);
}

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
	
	var cboSysData = data;
	
	cboSysData = cboSysData.filter(function(data){
		if(data.cm_sysinfo.substr(0,1) == '1'){
			return false;
		} else{
			return true;
		}
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options : injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if(getSelectedIndex('cboSys') > 0){
		getJobInfo_Rpt();
	}
}

function getJobInfo_Rpt(){
	
	tmpInfo = {
		UserID		: userId,
		SysCd		: getSelectedVal('cboSys').value,
		CloseYn		: 'N',
		SelMsg		: 'ALL',
		requestType	: 'getJobInfo_Rpt'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetJobInfo);
}

function successGetJobInfo(data){
	
	var cboJobData = data;
	
	
	$('[data-ax5select="cboJob"]').ax5select({
		options : injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
	});
}

function btnQry_click(){
	
	var termSw = false;
	
	$('#btnExcel').prop('disabled',true);
	gridData = [];
	mainGrid.setData([]);
	
	if(getSelectedIndex('cboGbn1') < 1){
		dialog.alert('조회구분을 선택하여 주십시오.');
		return;
	}
	var strStD = replaceAllString($("#datStD").val(), '/', '');
	var strEdD = replaceAllString($("#datEdD").val(), '/', '');
	
	if (strStD == null || strStD == '' || strStD == undefined) {
		dialog.alert('조회시작일을 선택하여 주십시오.');
		return;
	}
	if (strEdD == null || strEdD == '' || strEdD == undefined) {
		dialog.alert('조회종료일을 선택하여 주십시오.');
		return;
	}
	if (strStD > strEdD) {
		dialog.alert('조회기간을 정확하게 선택하여 주십시오.');
		return;
	}
	if(getSelectedIndex('cboStep1') < 1){
		dialog.alert('출력할 단계를 선택하여 주십시오.');
		return;
	} else{
		if(getSelectedVal('cboStep1').value == '3') termSw = true;
	}
	
	for(var j = 2; j < 5; j++){
		for(var i = 1; i < j; i++){
			if(getSelectedIndex('cboStep'+j) > 0){
				if(getSelectedVal('cboStep'+i).value == getSelectedVal('cboStep'+j).value){
					dialog.alert(i+'단계와 '+j+'단계 구분이 동일합니다.\n 다르게 선택하여 주십시오.');
					return;
				}
				if(getSelectedVal('cboStep'+j).value == '3') termSw = true;
			}
		}
	}
	
	if(getSelectedVal('cboGbn1').value != '02' && termSw == false){
		dialog.alert('단계 중 기간을 선택하여 주십시오.');
		return;
	}
	
	for(var k = 2; k < 4; k++){
		for(var l = k+1; l < 5; l++){
			if(getSelectedIndex('cboStep'+k) < 1 && getSelectedIndex('cboStep'+l) > 0){
				dialog.alert('출력할 단계를 차례대로 선택하여 주십시오.');
				return;
			}
		}
	}
	
	var syscd = '';
	if(getSelectedIndex('cboSys') > 0){
		syscd = getSelectedVal('cboSys').value;
	}
	titSet_Prog();
	
	if(getSelectedVal('cboGbn1').value == '01'){
		strStD = strStD.substr(0,6);
		strEdD = strEdD.substr(0,6);
	}
	
	
	tmpInfo = {
		UserId		: userId,
		Gubun		: getSelectedIndex('cboGbn1') > 0 ? getSelectedVal('cboGbn1').value : '',
		StDate		: strStD,
		EdDate		: strEdD,
		Step1		: getSelectedIndex('cboStep1') > 0 ? getSelectedVal('cboStep1').value : '',
		Step2		: getSelectedIndex('cboStep2') > 0 ? getSelectedVal('cboStep2').value : '',
		Step3		: getSelectedIndex('cboStep3') > 0 ? getSelectedVal('cboStep3').value : '',
		Step4		: getSelectedIndex('cboStep4') > 0 ? getSelectedVal('cboStep4').value : '',
		SysCd		: getSelectedIndex('cboSys') > 0 ? getSelectedVal('cboSys').value : '',
		JobCd		: getSelectedIndex('cboJob') > 0 ? getSelectedVal('cboJob').value : '',
		requestType	: 'getProgList'
	}

	$('[data-ax5grid="dataGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	console.log('getProgList',tmpInfo);
	gridData = ajaxCallWithJson('/webPage/ecmp/Cmp3200Servlet', tmpInfo, 'json');
	mainGrid.setData(gridData);
	$(".loding-div").remove();
	if(gridData.length > 0) $('#btnExcel').prop('disabled',false);
		
}
function btnExcelClick() {

	var tmpData = {
			step1name : getSelectedIndex('cboStep1') > 0 ? getSelectedVal('cboStep1').text : '',
			step2name : getSelectedIndex('cboStep2') > 0 ? getSelectedVal('cboStep2').text : '',
			step3name : getSelectedIndex('cboStep3') > 0 ? getSelectedVal('cboStep3').text : '',
			step4name : getSelectedIndex('cboStep4') > 0 ? getSelectedVal('cboStep4').text : '',
	}
	tmpInfo = {
		UserId		: userId,
		   fileList	: gridData,
		    prjStep	: rsrcData,
		      QryCd	: getSelectedVal('cboGbn1').value,
		    etcData	: tmpData,
		    exlName	: userId + '_TermDeployList.xls',
		requestType	: 'excelDataMake_Prog'
	}
	
	var ajaxResult = ajaxCallWithJson('/webPage/ecmp/Cmp3500Servlet', tmpInfo, 'json');
	if (ajaxResult == 'ERR') {
		dialog.alert('엑셀저장작업에 실패하였습니다.');
		return;
	}
	successExcelExport(ajaxResult,userId + '_TermDeployList.xls');
	tmpData = null;
}
function titSet_Prog(){
	
	columns = [];
	
	columns.push({key: "step1name",	label: getSelectedVal('cboStep1').text, width: '12%', align: "center"});
	
	if(getSelectedIndex('cboStep2') > 0){
		columns.push({key: "step2name",	label: getSelectedVal('cboStep2').text, width: '12%', align: "center"});
	}
	if(getSelectedIndex('cboStep3') > 0){
		columns.push({key: "step3name",	label: getSelectedVal('cboStep3').text, width: '12%', align: "center"});
	}
	if(getSelectedIndex('cboStep4') > 0){
		columns.push({key: "step4name",	label: getSelectedVal('cboStep4').text, width: '12%', align: "center"});
	}
	
	columns.push({key: "rowhap", label: "<span style='color: red;'>합계</span>", width: '7%', align: "right", 
		styleClass: function () {
            return 'fontStyle-cncl';
        }});
	
	for(var i = 0; i < rsrcData.length; i++){
		columns.push({key: "col"+rsrcData[i].cm_micode, label: rsrcData[i].cm_codename, width: '12%', align: "right"});
	}
	
	mainGrid.setConfig({columns : columns});
}












