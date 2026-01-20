/**
  [보고서] 형상관리운영현황
*/

var userId 		= window.top.userId;
var codeList	= window.top.codeList;

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var tmpInfo = {};
var options = [];
var gridData = [];
var rsrcData = [];
var columns = [];
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

$(document).ready(function() {
	
	//picker 현재 날짜
	$(function() {
		var today = getDate('DATE',0);
		today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
		$("#dateSt").val(today);
		$("#dateEd").val(today);
	});  
	
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
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("형상관리운영현황_" + today + ".xls");
	});
});

function getCodeInfo(){
	
	var codeInfos = getCodeInfoCommon([ new CodeInfo('RPTPRGGB', 'SEL','N'),new CodeInfo('RPTSTEP1', 'SEL','N')]);
	
	var cboGbnData = codeInfos.RPTPRGGB;
	var cboStepData = codeInfos.RPTSTEP1;
	
	
	options = [];

/*	$.each(cboGbnData, function(i,item){
		options[i] = {value: '0'+item.cm_micode, text: item.cm_codename};
	});
	*/
	$('[data-ax5select="cboGbn1"]').ax5select({
		options : injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
	});
	
	options = [];
	
	$.each(cboStepData, function(i,item){
		options[i] = {value: item.cm_micode, text: item.cm_codename};
	});
	
	for(var j = 1; j < 5; j++){
		$('[data-ax5select="cboStep'+j+'"]').ax5select({
			options : options
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
	
	options = [];
	
	$.each(cboSysData, function(i,item){
		options[i] = {value: item.cm_syscd, text: item.cm_sysmsg};
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options : options
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
	
	options = [];
	
	$.each(cboJobData, function(i, item){
		options[i] = {value: item.cm_jobcd, text: item.cm_jobname};
	});
	
	$('[data-ax5select="cboJob"]').ax5select({
		options : options
	});
}

function btnQry_click(){
	
	var termSw = false;
	
	$('#btnExcel').prop('disabled',true);
	
	if(getSelectedIndex('cboGbn1') < 1){
		dialog.alert('조회구분을 선택하여 주십시오.');
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
	
	tmpInfo = {
		UserId		: userId,
		SysCd		: syscd,
		requestType	: 'getRsrcCd'
	}

	ajaxAsync('/webPage/ecmp/Cmp3500Servlet', tmpInfo, 'json', successGetRsrcCd);
}

function successGetRsrcCd(data){
	
	rsrcData = data;
	
	var strSys = '';
	var strJob = '';
	var strStep1 = '';
	var strStep2 = '';
	var strStep3 = '';
	var strStep4 = '';
	var strStD = replaceAllString($('#dateSt').val(),'/','');
	var strEdD = replaceAllString($('#dateEd').val(),'/','');
	
	if(rsrcData.length > 0){
		titSet_Prog();
		
		if(getSelectedVal('cboGbn1').value == '01'){
			strStD = strStD.substr(0,6);
			strEdD = strEdD.substr(0,6);
		}
		
		if(getSelectedIndex('cboSys') > 0) strSys = getSelectedVal('cboSys').value;
		if(getSelectedIndex('cboJob') > 0) strJob = getSelectedVal('cboJob').value;
		if(getSelectedIndex('cboStep1') > 0) strStep1 = getSelectedVal('cboStep1').value;
		if(getSelectedIndex('cboStep2') > 0) strStep2 = getSelectedVal('cboStep2').value;
		if(getSelectedIndex('cboStep3') > 0) strStep3 = getSelectedVal('cboStep3').value;
		if(getSelectedIndex('cboStep4') > 0) strStep4 = getSelectedVal('cboStep4').value;
		
		tmpInfo = {
			UserId		: userId,
			Gubun		: getSelectedVal('cboGbn1').value,
			StDate		: strStD,
			EdDate		: strEdD,
			Step1		: strStep1,
			Step2		: strStep2,
			Step3		: strStep3,
			Step4		: strStep4,
			SysCd		: strSys,
			JobCd		: strJob,
			requestType	: 'getProgList'
		}
		
		gridData = ajaxCallWithJson('/webPage/ecmp/Cmp3500Servlet', tmpInfo, 'json');
		mainGrid.setData(gridData);
		if(gridData.length > 0) $('#btnExcel').prop('disabled',false);
	} else {
		gridData = [];
		mainGrid.setData(gridData);
	}
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












