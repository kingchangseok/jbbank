/**
  [보고서] 파일대사불일치현황
*/

var userId 		= window.top.userId;
var adminYN		= window.top.adminYN;
var rgtList	    = window.top.rgtList;

var fileGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var fileGridData	 = [];
var cboDiffSayuData	 = [];

$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

fileGrid.setConfig({
    target: $('[data-ax5grid="fileGrid"]'),
    sortable: true,
    multiSort: true,
    showLineNumber: true,
    showRowSelector: true,
    header: {
        align: "center",
    	},
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            
            $('#txtSys').val(this.item.sysnm);
        	$('#txtPrg').val(this.item.cd_rsrcname);
        	$('#txtIp').val(this.item.cd_svrip);
        	$('#txtErrDate').val(this.item.cd_diffdt);
        	$('#txtDir').val(this.item.cm_dirpath);
        	$('#txtDiffSayu').val(this.item.cd_sayu);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "owner_nm",		label: "담당자",			width: '6%',  	align: "center"},
        {key: "sysnm",			label: "시스템",			width: '9%',  	align: "left"},
        {key: "cd_rsrcname",	label: "프로그램명",		width: '10%', 	align: "left"},
        {key: "cm_dirpath",		label: "프로그램경로",		width: '14%', 	align: "left"},
        {key: "cd_sayu",		label: "불일치사유",		width: '15%', 	align: "left"},
        {key: "cm_codename",	label: "대사결과내용",		width: '13%',	align: "left"},
        {key: "cd_svrip",		label: "서버주소",			width: '8%',  	align: "left"},
        {key: "cd_svrname",		label: "서버명",			width: '8%',  	align: "left"},
        {key: "cd_diffdt",		label: "불일치일자",		width: '6%',  	align: "center"},
        {key: "cd_errclsdt",	label: "정리일자",			width: '5%',  	align: "center"},
        {key: "cd_errdaycnt",	label: "불일치연속일수",	width: '6%', 	align: "center"}
    ]
});

$('#datStD').val(getDate('DATE',-1));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

$(document).ready(function() {
	$('#txtErr').val('1');
	$('#txtDiffSayu').hide();
	
	if(adminYN){
		$('#btnDel').css('display','inline-block');
	} else{
		$('#btnDel').prop('disabled',true);
	}
	
	getCodeInfo();	
	
	//조회
	$("#btnQry").on('click', function() {
		btnQry_click();
	});
	
	//삭제
	$("#btnDel").on('click', function() {
		btnDel_click();
	});
	
	//등록
	$("#btnReq").on('click', function() {
		btnInsert_click();
	});
	
	//불일치 발생사유 변경
	$("#cboDiffSayu").on('change', function() {
		cboDiffSayu_change();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		excelExport(fileGrid,"파일대사불일치목록_" + userId + ".xls");
	});
	
	$("#chkDate").on('click', function() {
		chkDate_click();
	});
	
	//메일보내기 Cmp2900.CmdMailSend(tmpArr.toArray(), strUserId); 사용안함	
});

function getCodeInfo(){
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('DIFFSAYU','','N','3','')
	]);
	
	cboDiffSayuData = codeInfos.DIFFSAYU;
	
	$('[data-ax5select="cboDiffSayu"]').ax5select({
		options : injectCboDataToArr(cboDiffSayuData, 'cm_micode' , 'cm_codename')
	});
}

function cboDiffSayu_change() {
	if(getSelectedVal('cboDiffSayu').cm_micode == '10') {
		$('#txtDiffSayu').show();
	}else {
		$('#txtDiffSayu').hide();
	}
}

// 조회
function btnQry_click() {
	var stDate = replaceAllString($("#datStD").val(), '/', '');
	var edDate = replaceAllString($("#datEdD").val(), '/', '');
	
	if (stDate > edDate) {
		dialog.alert('조회기간을 정확하게 선택하여 주십시오.');
		return;
	}
	
	if($('#txtErr').val().length == 0) {
		dialog.alert('불연치연속일수를 입력해 주세요.');
		$('#txtErr').focus();
		return;
	}
	
	
	if($('#chkDate').is(':checked')) chk = '1';
	else chk = '0';
	
	var data =  new Object();
	data = {
		chk : chk,
		strstd : stDate,
		stredd : edDate,
		errday : $('#txtErr').val(),
		userid : userId,
		requestType : 'CmdQry'
	}
	
	$('[data-ax5grid="fileGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp2900Servlet', data, 'json', successCmdQry);
}

function successCmdQry(data) {
	$(".loding-div").remove();
	fileGridData = data;
	fileGrid.setData(fileGridData);
}

// 등록
function btnInsert_click() {
	var selItems = fileGrid.getList('selected');
	if(selItems.length == 0) {
		dialog.alert('파일을 선택해 주세요.');
		return;
	}
	
	var tmpSayu = '';
	if(getSelectedVal('cboDiffSayu').cm_micode == '10') {
		if($('#txtDiffSayu').val().length == 0) {
			dialog.alert('사유를 입력하여 주십시요.');
			$('#txtDiffSayu').focus();
			return;
		}
		tmpSayu = $('#txtDiffSayu').val();
	}else {
		tmpSayu = getSelectedVal('cboDiffSayu').cm_codename;
	}
	
	
	selItems.forEach(function(item, index) {
		item.cd_sayu = tmpSayu;
	});
	
	console.log('[FileMismatchReport.js] selItems ==>',selItems);
	
	var data =  new Object();
	data = {
		dataList : selItems,
		requestType : 'CmdInsert'
	}
	
	ajaxAsync('/webPage/ecmp/Cmp2900Servlet', data, 'json', successCmdInsert);
}

function successCmdInsert(data) {
	dialog.alert('사유등록되었습니다.');
	btnQry_click();
}

// 삭제
function btnDel_click() {
	var selItems = fileGrid.getList('selected');
	if(selItems.length == 0) {
		dialog.alert('삭제할 대사 결과를 선택하여 주십시요.');
		return;
	}
	
	var data =  new Object();
	data = {
		dataList : selItems,
		requestType : 'CmdDelete'
	}
	
	ajaxAsync('/webPage/ecmp/Cmp2900Servlet', data, 'json', successCmdDelete);
}

function successCmdDelete(data) {
	if(data == 'OK') {
		dialog.alert('정상적으로 삭제 되었습니다.');
	}else {
		dialog.alert('삭제중 오류가 발생하였습니다.');
	}
	
	btnQry_click();
}

function chkDate_click() {
	if($('#chkDate').is(':checked')) {
		$('#datEdD').prop("disabled", true);
		$('#btnEdD').prop("disabled", true);
	}else {
		$('#datEdD').prop("disabled", false);
		$('#btnEdD').prop("disabled", false);
	}
}
