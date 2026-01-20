/** 
 * [테스트관리] 테스트접수 / [tab]테스트접수
 */


var strIsrId = '';
var strIsrSub = '';
var strUserId = '';
var strSubStatus = '';
var strReqCd = '';
var today = '';
var tmpInfo = {};
var option = [];
var cboTestData = [];
var gridtabData = [];

var picker = new ax5.ui.picker();
var testTabGrid = new ax5.ui.grid();

ax5.info.weekNames = [ 
	{label : "일"}, 
	{label : "월"}, 
	{label : "화"}, 
	{label : "수"}, 
	{label : "목"}, 
	{label : "금"}, 
	{label : "토"}
 ];

picker.bind(defaultPickerInfo('picker', 'bottom'));

//picker 현재 날짜
$(function() {
	today = new Date().toISOString().substring(0,10).replace(/-/gi, '/');
	$("#date").val(today);
});


$(document).ready(function(){
	
	var oldVal = '';
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#date').on('propertychange change keyup paste input', function() {
		var currentVal =  $("#date").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//등록
	$('#btnReg').on('click', function() {
		register();
	});
	
	//테스트회차
	$('#cboTest').on('change', function() {
		cboTest_Change();
	});
	
});

testTabGrid.setConfig({
	target : $('[data-ax5grid="testTabGrid"]'),
	sortable : true,
	multiSort : true,
	showLineNumber : true,
	header : {
		align : "center",
		columnHeight : 30
	},
	body : {
		columnHeight : 28,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		}
	},
	columns : 
	  [ 
		{key : "cm_deptname",	label : "소속팀",		width: 140,align : 'center'}, 
		{key : "cm_username",	label : "담당자명",	width: 140,align : 'center'}, 
		{key : "totrst",		label : "테스트결과",	width: 140,align : 'center'}
	  ]
});

function screenInit(scrGbn) {

	if (scrGbn == 'M') {
		$('[data-ax5select="cboTest"]').ax5select({
			options : []
		});
		$('#txtTestYn').val('')
	}
	$('#date').val('');
	$('#date').prop('disabled', true);
	$('#txtTime').val('');
	$('#txtTime').prop('disabled', true);
	$('#btnDate').prop('disabled', true);
	$('#btnReg').prop('disabled', true);
	gridtabData = [];
	testTabGrid.setData([]);
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
	$.each(cboTestData,function(i,item) {
		option.push({value: item.cc_testseq, text: item.testseq, secusw: item.secusw, endyn: item.endyn, cc_testterm: item.cc_testterm, cc_testseq: item.cc_testseq, data: item.data});
	});
	
	$('[data-ax5select="cboTest"]').ax5select({
		options : option
	});
	
	if (cboTestData.length > 0 ) {
		cboTest_Change();
	}
}

function cboTest_Change(){

	screenInit('S');
	$("#date").val(today);
	
	if (getSelectedIndex('cboTest') < 0) return;
	
	if (getSelectedVal('cboTest').secusw == 'Y' && getSelectedVal('cboTest').endyn == 'N' && strReqCd == '51') {
		$('#btnReg').prop('disabled', false);
	}
	if (getSelectedVal('cboTest').cc_testterm != null && getSelectedVal('cboTest').cc_testterm != '') {
		var date = getSelectedVal('cboTest').cc_testterm.substr(0,4) +'/'+ getSelectedVal('cboTest').cc_testterm.substr(4,2) +'/'+ getSelectedVal('cboTest').cc_testterm.substr(6,2);
		$("#date").val(date);
		document.getElementById("txtTime").value = getSelectedVal('cboTest').cc_testterm.substr(8,2) +':'+ getSelectedVal('cboTest').cc_testterm.substr(10,2);
 	}
 	
 	if ($('#btnReg').is(':enabled')) {
 		$('#date').prop("disabled", false);
 		$('#btnDate').prop("disabled", false);
 		$('#txtTime').prop("disabled", false);
 	}
 	
 	var secuSw = true;
 	if($('#btnReg').is(':disabled')) secuSw = false;
 	
 	tmpInfo = {
 		IsrId		: strIsrId,
 		IsrSub		: strIsrSub,
 		secuSw		: secuSw,
 		selSeq		: getSelectedVal('cboTest').cc_testseq,
 		dataYn		: getSelectedVal('cboTest').data,
 		ReqCd		: strReqCd,
 		requestType	: 'getTestInfo_Recv'
 	}
 	
 	gridtabData = ajaxCallWithJson('/webpage/test/TestAdministration', tmpInfo, 'json');
 	 
 	testTabGrid.setData(gridtabData);
}

function register(){
	
	var tabGridIndex = testTabGrid.selectedDataIndexs;
	var selectedTabGridItem = testTabGrid.list[tabGridIndex];
	var strUser = '';
	
	if (selectedTabGridItem == undefined) {
		dialog.alert("테스트담당자를 선택한 후 처리하시기 바랍니다.");
		return;
	}
    if (strUser.length > 0) strUser = strUser + ",";
    strUser = strUser + selectedTabGridItem.cm_userid;	
    
    var now = replaceAllString(today, '/', '');
    var date = replaceAllString($('#date').val(), '/', '');

    if (now > date) {
	    dialog.alert("테스트기한이 현재일 이전입니다. 정확히 선택하여 주십시오");
	    return;		
    }

    var tmpObj = {};
    var hm = $('#txtTime').val().replace(':','');
    
    tmpObj.testuser = strUser;
    tmpObj.isrid = strIsrId;
    tmpObj.isrsub = strIsrSub;
    tmpObj.testseq = getSelectedVal('cboTest').cc_testseq;
    tmpObj.userid = strUserId;
    tmpObj.testterm = date + hm;
    
    tmpInfo = {
    	tmpInfo		: tmpObj,
    	requestType	: 'insTest_Recv'
    }
    
 	var ajaxResult = ajaxCallWithJson('/webpage/test/TestAdministration', tmpInfo, 'json'); 
    
    if (ajaxResult == 'OK') {
    	dialog.alert("테스트접수가 정상적으로 처리되었습니다.");
    } else {
    	dialog.alert("테스트접수 처리 중 오류가 발생하였습니다.");
    }
}



















