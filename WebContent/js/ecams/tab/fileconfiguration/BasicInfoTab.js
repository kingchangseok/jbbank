/**
 * [파일대사환경설정 > 기본관리 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-01
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
//var codeList    = window.top.codeList;          //전체 코드 리스트

var cboJobDivData	= [];
var cboCycleData	= [];
var cboWeekData	= [];
var cboDelCycleData	= [];
var ulSysInfoData	= [];

var secuYn	= 'Y';

$('[data-ax5select="cboJobDiv"]').ax5select({
    options: []
});
$('[data-ax5select="cboCycle"]').ax5select({
	options: []
});
$('[data-ax5select="cboDelCycle"]').ax5select({
	options: []
});

$('#txtRunTime').timepicker({
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: true,
    scrollbar: true
 });

$('input.checkbox-file').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	$('#txtRundate').prop('disabled', true);
	getCodeInfo();
	getSysInfo();
	
	// 작업구분 변경
	$('#cboJobDiv').bind('change', function() {
		if(getSelectedIndex('cboJobDiv') < 1) {
			dataClear();
			return;
		}
		getFileInf();
	});
	// 시스템 정보 리스트  전체선택/해체
	$('#chkAllSys').bind('click', function() {
		var checkSw = $('#chkAllSys').is(':checked');
		checkAllSys(checkSw);
	});
	// 기본정보 등록
	$('#btnSave').bind('click', function() {
		setFileInf();
	});
	
	// 작업주기 숫자만입력
	$('#txtCycle').keyup(function (event) { 
		var v = $(this).val();
		$(this).val(v.replace(/[^a-z0-9]/gi,''));
	});
	// 삭제주기 숫자만입력
	$('#txtDelCycle').keyup(function (event) { 
		var v = $(this).val();
		$(this).val(v.replace(/[^a-z0-9]/gi,''));
	});

});

// 기본정보 등록
function setFileInf() {
	var txtCycle 	=  $('#txtCycle').val();
	var txtRunTime	=  $('#txtRunTime').val();
	var txtDelCycle =  $('#txtDelCycle').val();
	var sysList		= '';
	var cm_weekcd = "";
	var etcData		= new Object();
	txtRunTime = replaceAllString(txtRunTime, ':', '');
	txtRunTime = txtRunTime.length === 3 ? '0' + txtRunTime : txtRunTime;
	if(getSelectedIndex('cboJobDiv') < 1) {
		dialog.alert('작업구분을 선택하여 주십시오.', function() {});
		return;
	}
	if(txtCycle.length === 0 ) {
		dialog.alert('작업주기를 입력하여  주십시오.', function() {});
		return;
	}
	if(getSelectedIndex('cboCycle') < 1) {
		dialog.alert('작업주기를 선택하여 주십시오.', function() {});
		return;
	}
	if(txtRunTime.length === 0 ) {
		dialog.alert('작업시간을 입력하여  주십시오.', function() {});
		return;
	}
	if(txtDelCycle.length === 0 ) {
		dialog.alert('삭제주기를 입력하여  주십시오.', function() {});
		return;
	}
	if(getSelectedIndex('cboDelCycle') < 1) {
		dialog.alert('삭제주기를 선택하여 주십시오.', function() {});
		return;
	}
	
	ulSysInfoData.forEach(function(item, index) {
		if($('#chkSys'+item.cm_syscd).is(':checked')) {
			if(sysList.length !== 0 ) sysList += ',';
			sysList += item.cm_syscd;
		}
	});
	
	cboWeekData.forEach(function(item, index) {
		if($('#chkWeek'+index).is(':checked')) {
			cm_weekcd += "1";
		} else {
			cm_weekcd += "0";
		}
	});
	
	if(sysList.length === 0 ) sysList += ',';
	
	etcData.cm_runcd 	 = getSelectedVal('cboJobDiv').value;
	etcData.cm_runterm 	 = txtCycle;
	etcData.cm_runtermcd = getSelectedVal('cboCycle').value;
	etcData.cm_runtime 	 = txtRunTime;
	etcData.cm_delterm 	 = txtDelCycle;
	etcData.cm_deltermcd = getSelectedVal('cboDelCycle').value;
	etcData.cm_syslist	 = sysList;
	etcData.cm_weekcd = cm_weekcd;
	
	var data = new Object();
	data = {
		UserId 		: userId,
		SecuYn 		: secuYn,
		etcData 	: etcData,
		requestType	: 'setFileInf'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json', successSetFileInf);
}

// 기본정보 등록 완료
function successSetFileInf(data) {
	if(data) {
		dialog.alert('파일대사 기본설정을 저장하였습니다.', function() {});
	} else {
		dialog.alert('파일대사 기본설정 저장을 실패했습니다. 관리자에게 문의하시기 바랍니다.', function() {});
	}
}

// 시스템 정보 리스트 전체 선택/해제
function checkAllSys(checkSw) {
	ulSysInfoData.forEach(function(item, index) {
		if(checkSw) {
			$('#chkSys'+item.cm_syscd).wCheck('check', true);
		} else {
			$('#chkSys'+item.cm_syscd).wCheck('check', false);
		}
	});
}


// 기본정보 화면 클리어
function dataClear() {
	$('#txtCycle').val('');
	$('#txtRunTime').val('');
	$('#txtRundate').val('');
	$('#txtDelCycle').val('');
	$('[data-ax5select="cboCycle"]').ax5select('setValue', cboCycleData[0].cm_micode, true);
	$('[data-ax5select="cboDelCycle"]').ax5select('setValue', cboDelCycleData[0].cm_micode, true);
	checkAllSys(false);
}

// 파일대사 기본 정보가져오기
function getFileInf() {
	var data = new Object();
	data = {
		UserId 		: userId,
		SecuYn 		: secuYn,
		cboJobRuncd : getSelectedVal('cboJobDiv').value,
		requestType	: 'getFileInf'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json', successGetFileInf);
}
// 파일대사 기본정보 가져오기 완료
function successGetFileInf(data) {
	var fileInfos = [];
	var sysStr	  = '';
	fileInfos 	  = data[0];
	console.log(fileInfos);
	if(fileInfos == undefined || fileInfos.length === 0 ) {
		return;
	}
	
	if(fileInfos.cm_syslist.length > 0 ) {
		sysStr = fileInfos.cm_syslist;
	}
	
	var runTime = fileInfos.cm_runtime;
	
	runTime = runTime.substr(0,2) + ':' + runTime.substr(2,2);
	
	$('#txtCycle').val(fileInfos.cm_runterm);
	$('#txtRunTime').val(runTime);
	$('#txtRundate').val(fileInfos.cm_rundate);
	$('#txtDelCycle').val(fileInfos.cm_delterm);
	$('[data-ax5select="cboCycle"]').ax5select('setValue', fileInfos.cm_runtermcd, true);
	$('[data-ax5select="cboDelCycle"]').ax5select('setValue', fileInfos.cm_deltermcd, true);
	
	
	ulSysInfoData.forEach(function(item, index) {
		if(sysStr.indexOf(item.cm_syscd) >= 0 ) {
			$('#chkSys'+item.cm_syscd).wCheck('check', true);
		}
	});
/*
	var weekStr = fileInfos.cm_weekcd;
	
	for(var i=0; i<fileInfos.cm_weekcd.length; i++){
		console.log(weekStr.substr(i,1));
		if(weekStr.substr(i,1) == "1" ) {
			$('#chkWeek'+ i).wCheck('check', true);
		}
	}
*/
}

// 시스템 리스트 가져오기
function getSysInfo() {
	var data = new Object();
	data = {
		UserId : userId,
		SecuYn : "N",
		SelMsg : "",
		CloseYn : "N",
		ReqCd : "",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

// 시스템 리스트 가져오기 완료
function successGetSysInfo(data) {
	ulSysInfoData = data;
	makeSysInfoUlList();
}

// 콤보 박스 정보 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
										new CodeInfo('JOBRUNCD','SEL','N'),
										new CodeInfo('DBTERM','SEL','N')
										]);
	
	cboJobDivData 	= codeInfos.JOBRUNCD;
	cboCycleData	= codeInfos.DBTERM;
	cboDelCycleData	= codeInfos.DBTERM;
//	cboJobDivData   = fixCodeList(codeList['JOBRUNCD'], 'SEL', '', '', 'N');
//	cboCycleData    = fixCodeList(codeList['DBTERM'], 'SEL', '', '', 'N');
//	cboWeekData    = fixCodeList(codeList['WEEKDAY'], '', '', '', 'N');
	cboDelCycleData = cboCycleData;
//	codeList = null;
	
	$('[data-ax5select="cboJobDiv"]').ax5select({
        options: injectCboDataToArr(cboJobDivData, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboCycle"]').ax5select({
        options: injectCboDataToArr(cboCycleData, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboDelCycle"]').ax5select({
        options: injectCboDataToArr(cboDelCycleData, 'cm_micode' , 'cm_codename')
	});
	
//	makeWeekInfoUlList();
}

//시스템  ul 만들어주기
function makeSysInfoUlList() {
	$('#ulSysInfo').empty();
	var liStr = '';
	var addId = null;
	ulSysInfoData.forEach(function(sysInfoItem, sysInfoIndex) {
		addId = sysInfoItem.cm_syscd;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-sysInfo" id="chkSys'+addId+'" data-label="'+sysInfoItem.cm_sysmsg+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulSysInfo').html(liStr);
	
	$('input.checkbox-sysInfo').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
}

function makeWeekInfoUlList(){
	// 요일 체크박스
	$('#weekdayInfo').empty();
	var liStr = '';
	var addId = null;
	cboWeekData.forEach(function(sysInfoItem, sysInfoIndex) {
		addId = sysInfoItem.cm_micode - 1;
		liStr += '<li class="list-group-item" style="width:auto; display:inline-block;">';
		liStr += '	<input type="checkbox" class="checkbox-sysInfo" id="chkWeek'+addId+'" data-label="'+sysInfoItem.cm_codename+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#weekdayInfo').html(liStr);
	
	$('input.checkbox-sysInfo').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

