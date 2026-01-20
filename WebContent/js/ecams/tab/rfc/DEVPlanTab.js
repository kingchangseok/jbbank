/** 개발계획서 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-04-21
 */

var userName = window.top.userName;
var userId = window.top.userId;
var adminYN = window.top.adminYN;
var userDeptName = window.top.userDeptName;
var userDeptCd = window.top.userDeptCd;
var strReqCd = window.top.reqCd;
var codeList    = window.top.codeList;          //전체 코드 리스트

var grdWork = new ax5.ui.grid();
var picker = new ax5.ui.picker();

var cboEditor_dp = [];
var grdWork_dp = [];
var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var strMainStatus = "";
var strSubStatus = "";
var strNow = "";
var strWorkD = "";
var slideData = [];
var now = new Date();

ax5.info.weekNames = [ {
	label : "일"
}, {
	label : "월"
}, {
	label : "화"
}, {
	label : "수"
}, {
	label : "목"
}, {
	label : "금"
}, {
	label : "토"
} ];

$('[data-ax5select="cboEditor"]').ax5select({
	options : []
});

picker.bind(defaultPickerInfo('devst', 'top'));
picker.bind(defaultPickerInfo('deved', 'bottom'));
picker.bind(defaultPickerInfo('devdate', 'top'));

$(document).ready(function() {
	// 등록/수정
	$('#btnAdd').bind('click', function() {
		btnAdd_Click();
	});
	// 수정
	$('#btnDel').bind('click', function() {
		btnDel_Click();
	});
	// 등록
	$('#btnReq').bind('click', function() {
		btnReq_click();
	});		
	
	$('#cboEditor').bind('change', function() {
		cboEditor_click();
	})

	var strYear = "";
	strYear = now.getFullYear().toString();
	getWorkDays(strYear);
})

grdWork.setConfig({
	target : $('[data-ax5grid="grdWork"]'),
	sortable : true,
	multiSort : true,
	header : {
		align : "center",
		columnHeight : 25
	},
	body : {
		columnHeight : 25,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
			grdWork_ONECLICK();
		}	
	},
	columns : [ {
		key : "CC_JOBDAY",
		label : "등록일",
		width : 200,
		align : 'center'
	}, {
		key : "CC_JOBTIME",
		label : "시간",
		width : 100,
		align : 'center'
	}, {
		key : "cm_username",
		label : "담당자",
		width : 300,
		align : 'center'
	} ]
});

function getWorkDays(year) {
	var data = {
		year : year,
		requestType : 'getWorkDays'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetWorkDays);
}

function successGetWorkDays(data) {
	strWorkD = data;
	if(strWorkD == "NO") {
		dialog.alert("월근무일수가 등록되지 않았습니다.");
		return;
	}
}

function screenInit(scrGbn) {
	if(scrGbn == "M") {
		cboEditor_dp = [];
		grdWork.setData([]);
	}
	$('#optPlan').prop('checked', true);
	$('#optResult').attr('disabled', false);
	optPlan_click();
	
	$('#txtWriter').val();
	$('#txtWriteDay').val();
	
	$('#txtExpDev').val();
	$('#txtExpTime').val();
	
	$('#btnReq').attr('disabled', true);

	$('#txtReDevTime').val();
	$('#btnAdd').attr('disabled', true);
	$('#btnDel').attr('disabled', true);
	$('#chkAll').prop('checked', false);
}

$('input[name=rdogp]').bind('change', function() {
	optPlan_click();
});

function optPlan_click() {
	if (getSelectedIndex('cboEditor') < 0) {
		return;
	} 
	$('#btnAdd').attr('disabled', true);
	$('#btnDel').attr('disabled', true);
	$('#btnReq').attr('disabled', true);
	
	if (getSelectedVal('cboEditor').value == strUserId) {
		if ($('#optPlan').prop('checked') == true && getSelectedVal('cboEditor').cnt == 0 ) {
			$('#btnReq').attr('disabled', false);
		} else if ($('#optResult').prop('checked') == true && strSubStatus != "29" 
				   && getSelectedVal('cboEditor').devmm != "" && getSelectedVal('cboEditor').devmm != null) {
			$('#btnAdd').attr('disabled', false);
		}
	} else {
		return;
	}		
	if ($('#optPlan').prop('checked') == true && $('#btnReq').prop('disabled') === false) {
		$('#txtExpTime').attr('readonly', false);
		$('#datExpDevSt').attr('disabled', false);
		$('#datExpDevEd').attr('disabled', false);
		$('#calDatExpDevSt').css("pointer-events", "visible");
		$('#calDatExpDevEd').css("pointer-events", "visible");
		$('#calDatExpDevSt').css("background-color", "");
		$('#calDatExpDevEd').css("background-color", "");						
		$('#txtReDevTime').attr('readonly', true);
		$('#datReDevDate').attr('disabled', true);
		$('#calDatReDevDate').css("pointer-events", "none");
		$('#calDatReDevDate').css("background-color", "whitesmoke");			
	} else if ($('#optResult').prop('checked') == true && $('#btnAdd').prop('disabled') === false) {
		$('#txtExpTime').attr('readonly', true);
		$('#datExpDevSt').attr('disabled', true);
		$('#datExpDevEd').attr('disabled', true);
		$('#calDatExpDevSt').css("pointer-events", "none");
		$('#calDatExpDevEd').css("pointer-events", "none");
		$('#calDatExpDevSt').css("background-color", "whitesmoke");
		$('#calDatExpDevEd').css("background-color", "whitesmoke");				
		$('#txtReDevTime').attr('readonly', false);
		$('#datReDevDate').attr('disabled', false);
		$('#calDatReDevDate').css("pointer-events", "visible");
		$('#calDatReDevDate').css("background-color", "");			
	}	
}

function grdWork_ONECLICK() {
	if (grdWork.getList('selected').length != 0){
		$('#datReDevDate').val(grdWork.getList('selected')[0].CC_JOBDAY);
		$('#txtReDevTime').val(grdWork.getList('selected')[0].CC_JOBTIME);
		$('#btnDel').attr('disabled', false);
	}
}

function devPlanCall() {
	getSelectList();
	getWorktime();
}

function getSelectList() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.SubId = strIsrSub;
	obj.UserId = "";
	obj.ReqCd = strReqCd;
	
	var data = new Object();
	data.objData = obj;
	data.requestType = 'getSelectList';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetSelectList);
}

function successGetSelectList(data) {
	cboEditor_dp = data;

	options = [];
	$.each(cboEditor_dp, function(i, value) {
		options.push({
			value : value.cc_scmuser,
			text : value.cm_username,
			cc_status: value.cc_status, 
			cc_substatus: value.cc_substatus,
			cnt: value.cnt,
			creatdt: value.creatdt,
			devedday : value.devedday,
			devstday : value.devstday,
			devtime : value.devtime,
			eddate : value.eddate,
			devmm : value.devmm,
			lastdt: value.lastdt,
			pgmsw: value.pgmsw
		});
	});

	$('[data-ax5select="cboEditor"]').ax5select({
		options : options
	});
	
	if(cboEditor_dp.length > 0){
		for (var i = 0; cboEditor_dp.length > i; i++) {
			if (cboEditor_dp[i].cc_scmuser == strUserId) {
				$('[data-ax5select="cboEditor"]').ax5select("setValue", cboEditor_dp[i].cc_scmuser, true);
				break;
			}
		}
		cboEditor_click();
	}
}

function cboEditor_click() {
	var Wd = "";
	
	screenInit("S");
	if (getSelectedIndex('cboEditor') < 0) {
		return;
	}
	
	$('#txtExpDev').val(getSelectedVal('cboEditor').devmm);
	Wd = getSelectedVal('cboEditor').creatdt.substr(0,4)+"/"+getSelectedVal('cboEditor').creatdt.substr(5,2)+"/"+
	   getSelectedVal('cboEditor').creatdt.substr(8,2);
	$('#txtWriteDay').val(Wd);
	$('#txtWriter').val(getSelectedVal('cboEditor').text);
	$('#txtExpTime').val(getSelectedVal('cboEditor').devtime);
	
	if(getSelectedVal('cboEditor').devstday != null && getSelectedVal('cboEditor').devstday != ""){
		$('#datExpDevSt').val(getSelectedVal('cboEditor').devedday.substr(0,4) + "/" +
							  getSelectedVal('cboEditor').devedday.substr(4,2) + "/" +
							  getSelectedVal('cboEditor').devedday.substr(6,2))				   		   
		$('#datExpDevEd').val(getSelectedVal('cboEditor').devedday.substr(0,4) + "/" +
							  getSelectedVal('cboEditor').devedday.substr(4,2) + "/" +
							  getSelectedVal('cboEditor').devedday.substr(6,2))	
	} else {
		$('#datExpDevSt').val(getDate('DATE', 0));
		$('#datExpDevEd').val(getDate('DATE', 0));
		picker.bind(defaultPickerInfo('devst', 'top'));
		picker.bind(defaultPickerInfo('deved', 'top'));
	}

	$('#datReDevDate').val(getDate('DATE', 0));
    picker.bind(defaultPickerInfo('devdate', 'top'));
	$('#txtReDevTime').val("");

	if (getSelectedVal('cboEditor').cnt == 0) {
		if (getSelectedVal('cboEditor').devmm == null && getSelectedVal('cboEditor').devmm == "") {
			$('#optResult').attr('disabled', true);
		}
		$('#optPlan').prop('checked', true)
	} else {
		$('#optResult').prop('checked', true)
	}
	
	if (grdWork_dp.length > 0) {
		worktimeSetting();
	}
	optPlan_click();
		
}

function getWorktime() {
	var data = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		requestType : 'get_Worktime'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetWorktime);
}

function successGetWorktime(data) {
	grdWork_dp = data;
	worktimeSetting();
}

function worktimeSetting() {
	if($('#chkAll').prop('checked')){
		grdWork.setData([]);
		grdWork.setData(grdWork_dp);
	}else{
		worktime_filter();
		grdWork.setData([]);
		grdWork.setData(grdWork_dp);
	} 
	var workTimeSum = 0;
	if(grdWork_dp.length > 0){
		for (var i = 0; grdWork_dp.length > i; i++) {
			workTimeSum = workTimeSum + Number(grdWork_dp[i].CC_JOBTIME);
		}
	  }
	$('#lbWorkTimeSum').text("작업시간내역 [합계시간 : "+workTimeSum+" 시간]");
	workTimeSum = 0;
}

function worktime_filter() {
	if (getSelectedIndex('cboEditor') < 0) { 
		return false;
	}
	if(grdWork_dp.length > 0) {
		if (grdWork_dp[0].CC_SCMUSER == getSelectedVal('cboEditor').value){
			return true;
		}
		else{
			return false;
		}
	}
}

$('#chkAll').bind('change', function() {
	worktimeSetting();
})

//예상투입공수 계산
$('#txtExpTime').bind('change', function() {
	var gap = 0;
	var StrGap = "";
	if($('#txtExpTime').val().trim().length == 0) {
		$('#txtExpDev').val("");
	} else {
		gap = (Number($('#txtExpTime').val()/8))/Number(strWorkD)
		StrGap = String(gap);
		if (StrGap.indexOf(".") > 0) {
				StrGap = StrGap.substring(0,StrGap.indexOf(".")+3);
		   }
		   $('#txtExpDev').val(StrGap);
	}
})
//등록 버튼 클릭
function btnReq_click() {
	var etcObj = new Object();
	
	if ($('#txtExpDev').val() == "Infinity") {
		 dialog.alert("월근무일수가 등록되지 않았습니다.");
	 return;
	} 
	if ($('#txtExpTime').val().trim().length == 0) {
		dialog.alert("예상소요시간을 입력하여 주시기 바랍니다.");
		$('#txtExpTime').focus();
		return;
	}
	
	if ($('#txtExpDev').val().trim().length == 0) {
		dialog.alert("예상개발공수를 입력하여 주시기 바랍니다.");
		$('#txtExpDev').focus();
		return;
	} else if ($('#txtExpDev').val().trim() == "0.00") {
		dialog.alert("예상소요시간이 너무 작아서 예상개발공수를 산정할 수 없습니다. 예상개발공수를 조정하여 주시기 바랍니다.");
		$('#txtExpTime').focus();
		return;
	}
	if ($('#datExpDevSt').val() > $('#datExpDevEd').val()) {
		dialog.alert("개발시작일이 개발종료일이전입니다. 개발기간을 정확히 선택하여 주시기 바랍니다."); 
		return;
 }
	etcObj.ExpDevSt = replaceAllString($('#datExpDevSt').val(),"/","");
	etcObj.ExpDevEd = replaceAllString($('#datExpDevEd').val(),"/","");
	etcObj.IsrId = strIsrId;
	etcObj.SubId = strIsrSub;
	etcObj.ExpDev = $('#txtExpDev').val();
	etcObj.WritDay = $('#txtWriteDay').val();
	etcObj.Writer = strUserId;
	etcObj.ExpTime = $('#txtExpTime').val();

	var data = new Object();
	data.etcObj = etcObj;
	data.requestType = 'setInsertList';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successSetInsertList);	
}

function successSetInsertList(data) {
	if (data == "0") {
		dialog.alert("개발계획등록이 정상적으로 처리되었습니다.");	
		screenInit("M");
		devPlanCall();			
	} else {
		dialog.alert("개발계획등록 중 오류가 발생하였습니다.");			
	}
}
//등록/수정 버튼 클릭
function btnAdd_Click() {
	   		
	var etcObj = new Object();
	var strRealDate = "";
	
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1
    var day = date.getDate();
    if(month < 10){
        month = "0"+month;
    }
    if(day < 10){
        day = "0"+day;
    }
 
    var today = year+month+day;

	if (today < replaceAllString($('#datReDevDate').val(),"/","")){
		dialog.alert("작업일자가 현재일자 이후입니다. 정확히 선택하여 주시기 바랍니다.");
		$('#datReDevDate').focus();
		return;
	}
	strRealDate = replaceAllString($('#datReDevDate').val(),"/","")
	
	$('#txtReDevTime').val($('#txtReDevTime').val().trim());
	if ($('#txtReDevTime').val().length < 1) {
		dialog.alert("작업시간을 입력하여 주십시오."); 
		return;
	}
	if(Number($('#txtReDevTime').val()) < 1){
		dialog.alert("작업시간은 1시간 이상 등록 가능합니다."); 
		return;
	}
	if (Number($('#txtReDevTime').val()) > 24) {
		dialog.alert("작업시간은 24시간 이상을 입력할 수 없습니다."); 
		return;
	}
	etcObj.IsrId = strIsrId;
	etcObj.SubId = strIsrSub;
	etcObj.Writer = strUserId;
	etcObj.ReDevDate = strRealDate;
	etcObj.ReDevTime = $('#txtReDevTime').val();

	var data = new Object();
	data.etcObj = etcObj;
	data.requestType = 'setTimeInsertList';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successSetTimeInsertList);		
	etcObj = null;
}

function successSetTimeInsertList(data) {
	if (data == "0") {
		dialog.alert("개발실적등록이 정상적으로 처리되었습니다.");	
		screenInit("M");
		devPlanCall();			
	} else {
		dialog.alert("개발실적등록 중 오류가 발생하였습니다.");			
	}
}
//삭제 버튼 클릭
function btnDel_Click() {
	if(grdWork_dp.length > 0){
		for (var i = 0; grdWork_dp.length > i; i++) {
			var gridJobDay = grdWork.list[0].CC_JOBDAY;
			var delYN = "N";
			if($('#datReDevDate').val() == gridJobDay){
				delYN = "Y";
				break;
			}
		}
		if(delYN == "Y"){
			dialog.alert({
				msg: "["+$('#datReDevDate').val()+"] 작업시간을 삭제 하시겠습니까?",
			}, function() {
				if(this.key === 'ok') {
					timeDeleteChk(this.key);
				}
			})				
		}else{
			dialog.alert("["+$('#datReDevDate').val()+"] 작업시간이 존재하지 않습니다.");
		}
	}else{
			dialog.alert("["+$('#datReDevDate').val()+"] 작업시간이 존재하지 않습니다.");
	}
}

function timeDeleteChk(state) {
	if (state == 'ok') {
		var etcObj = new Object();
		etcObj.IsrId = strIsrId;
		etcObj.SubId = strIsrSub;
		etcObj.Writer = grdWork.list[0].CC_SCMUSER;
		etcObj.ReDevDate = replaceAllString($('#datReDevDate').val(),"/","");

		var data = new Object();
		data.etcObj = etcObj;
		data.requestType = 'setTimeDeleteList';
		ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
				successSetTimeDeleteList);	
		etcObj = null;
	}
}

function successSetTimeDeleteList(data) {
	if (data == "0") {
		dialog.alert("작업시간이 삭제 되었습니다.");
		screenInit("M");
		devPlanCall();			
	} else {
		dialog.alert("작업시간 삭제 중 오류가 발생하였습니다.");
	}
}

