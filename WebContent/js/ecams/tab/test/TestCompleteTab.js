/** 
 * [테스트관리] 통합테스트 / [tab]테스트종료
 */

var strIsrId = '';
var strIsrSub = '';
var strUserId = '';
var strSubStatus = '';
var strReqCd = '';

var testEndGrid = new ax5.ui.grid();

var tmpInfo = {};
var tmpObj = {};
var cboTestData = [];

$('[data-ax5select="cboTest"]').ax5select({
	options : []
});

$(document).ready(function() {
	
	//결재
	$('#btnOk').on('click', function() {
		btnOk_Click();
	});
	
	//반려
	$('#btnCncl').on('click', function() {
		btnCncl_Click();
	});
	
	//테스트회차
	$('#cboTest').on('change', function() {
		cboTest_Change();
	});
});

testEndGrid.setConfig({
    target: $('[data-ax5grid="testEndGrid"]'),
    showLineNumber : true,
	sortable : true,
	multiSort : true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
//			grdLst_click(this.item, this.dindex, false);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        }
    },
    columns: [
    	{key: "cm_deptname",	label: "소속팀", 		width: 140,align: 'center'},
    	{key: "cm_username",	label: "담당자명",		width: 140,align: 'center'},
        {key: "totrsts",  		label: "테스트결과",	width: 140,align: 'center'}
    ]
});

function screenInit(scrGbn) {

	if (scrGbn == 'M') {
		cboTestData = [];
		$('[data-ax5select="cboTest"]').ax5select({
			options : []
		});
		$('#txtSta').val('');
	}

	$('#btnOk').prop('disabled', true);
	$('#btnCncl').prop('disabled', true);
	$('#txtConf').prop('disabled', true);
	$('#optOk').prop('disabled', true);
	$('#optErr').prop('disabled', true);
	$('#txtEdReqDt').prop('disabled', true);
	$('#txtEdReqUser').prop('disabled', true);
	$('#txtEndDt').prop('disabled', true);
	$('#txtEndGbn').prop('disabled', true);
	
	$('#txtConf').val('');
	$('#txtManager').val('');
	$('#txtEdReqDt').val('');
	$('#txtEdReqUser').val('');
	$('#txtEndDt').val('');
	$('#txtEndGbn').val('');
	
	testEndGrid.setData([]);
	
}

function testcntCall(){
	
	tmpInfo = {
		IsrId		: strIsrId,
		IsrSub		: strIsrSub,
		UserId		: strUserId,
		subStatus	: strSubStatus,
		ReqCd		: strReqCd,
		requestType	: 'getTestCnt'
	}
	
	ajaxAsync('/webpage/test/TestAdministration', tmpInfo, 'json', successGetTestCnt);
}

function successGetTestCnt(data){
	
	cboTestData = data;
	
	option = [];
	$('[data-ax5select="cboTest"]').ax5select({
		options: injectCboDataToArr(cboTestData, 'cc_testseq' , 'testseq')
	});
	
	if (cboTestData.length > 0 ) {
		cboTest_Change();
	}
}

function cboTest_Change(){
	
	screenInit('S');
	
	if (getSelectedIndex('cboTest') < 0) return;
	
	$('#txtManager').val(getSelectedVal('cboTest').testmanager);
	if (getSelectedVal('cboTest').secusw == 'N'  && strSubStatus == '35') { //테스트(임시)
//		if (getSelectedVal('cboTest').secusw == 'Y' && getSelectedVal('cboTest').cc_editor == strUserId && strSubStatus == '34') { 
		$('#btnOk').prop('disabled', false);
		$('#btnCncl').prop('disabled', false);
	}
	if (getSelectedVal('cboTest').text != null && getSelectedVal('cboTest').text != '') {
		$('#txtConf').val(getSelectedVal('cboTest').text);
 	}
	$('#txtSta').val(getSelectedVal('cboTest').status);
 	
	if (getSelectedVal('cboTest').endyn == 'Y') {
		$('#txtEndDt').prop('disabled', false);
		$('#txtEndGbn').prop('disabled', false);
		
		$('#txtEndDt').val(getSelectedVal('cboTest').enddate);
		$('#txtEndGbn').val(getSelectedVal('cboTest').endgbn);
	} 
	if (getSelectedVal('cboTest').endreqdt != null && getSelectedVal('cboTest').endreqdt != '') {
		$('#txtEdReqDt').prop('disabled', false);
		$('#txtEdReqUser').prop('disabled', false);
		
		$('#txtEdReqDt').val(getSelectedVal('cboTest').endreqdt);
		$('#txtEdReqUser').val(getSelectedVal('cboTest').endrequser);
	} 
	
 	if ($('#btnOk').is(':enabled')) {
 		$('#txtConf').prop("disabled", false);
 		$('#optOk').prop("disabled", false);
 		$('#optErr').prop("disabled", false);
 	}
 	
 	tmpInfo = {
 		IsrId		: strIsrId,
 		IsrSub		: strIsrSub,
 		secuSw		: false,
 		selSeq		: getSelectedVal('cboTest').value,
 		dataYn		: getSelectedVal('cboTest').data,
 		ReqCd		: strReqCd,
 		requestType	: 'getTestInfo_Recv'
 	}
 	
 	gridtabData = ajaxCallWithJson('/webpage/test/TestAdministration', tmpInfo, 'json'); 
 	
 	testEndGrid.setData(gridtabData);
}

function btnOk_Click(){
	
    if (!$('#optOk').is(':checked') && !$('#optErr').is(':checked') ) {
	    dialog.alert('완료구분을 선택한 후 처리하시기 바랍니다.');
	    return;
    }

    var confText = $('#txtConf').val().trim();
    if ($('#optErr').is(':checked') && confText == '') {
    	dialog.alert('비정상인 경우 결재의견을 입력하여야 합니다.');
    	$('#txtConf').focus();
	    return;
    }
    
    tmpObj = {};
    tmpObj.isrid = strIsrId;
    tmpObj.isrsub = strIsrSub;
    tmpObj.testseq = getSelectedVal('cboTest').value;
    tmpObj.userid = strUserId;
    if ($('#optOk').is(':checked')) tmpObj.endgbn = 'Y';
    else tmpObj.endgbn = 'N';
    tmpObj.endmsg = confText;

    tmpInfo = {
    	tmpInfo		: tmpObj,
    	requestType	: 'setTestEnd_OK'
    }
    
 	var ajaxResult = ajaxCallWithJson('/webpage/test/TestAdministration', tmpInfo, 'json'); 
    
    if (ajaxResult == 'OK') {
    	dialog.alert("테스트종료가 정상적으로 처리되었습니다.");
    	window.parent.btnQry_Click();
    } else {
    	dialog.alert("테스트종료 처리 중 오류가 발생하였습니다.");
    }
}

function btnCncl_Click(){
	
    var confText = $('#txtConf').val().trim();
	if (confText == '') {
		dialog.alert('반려의견을 입력한 후 처리하시기 바랍니다.');
    	$('#txtConf').focus();
		return;
	}
	
	tmpObj = {};
	tmpObj.isrid  = strIsrId;
	tmpObj.isrsub = strIsrSub;
	tmpObj.testseq= getSelectedVal('cboTest').value;
	tmpObj.userid = strUserId;
	tmpObj.endmsg = confText;
    tmpInfo = {
		tmpInfo		: tmpObj,
		requestType	: 'setTestEnd_Cncl'
    }
	
 	var ajaxResult = ajaxCallWithJson('/webpage/test/TestAdministration', tmpInfo, 'json'); 
    
    if (ajaxResult == 'OK') {
    	dialog.alert("테스트반려가 정상적으로 처리되었습니다.");
    	window.parent.btnQry_Click();
    } else {
    	dialog.alert("테스트반려 처리 중 오류가 발생하였습니다.");
    }
	
}

