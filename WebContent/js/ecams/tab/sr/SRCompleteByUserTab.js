/** ISR종료 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-05-07
 */

var userName = window.top.userName;
var userId = window.top.userId;
var adminYN = window.top.adminYN;
var userDeptName = window.top.userDeptName;
var userDeptCd = window.top.userDeptCd;
var strReqCd = window.top.reqCd;
var codeList    = window.top.codeList;          //전체 코드 리스트

var grdFile = new ax5.ui.grid();

var strUserId = "";
var strReqCd = "";
var strIsrId = "";

var lstDocFile_dp = [];
var getReqRFCInfo_dp = [];

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

$(document).ready(function() {
	// 종료등록
	$('#btnReq').bind('click', function() {
		btnReq_click();
	});
});

function screenInit(scrGbn) {
	$('#lblEndDt').css('visibility', 'hidden');
	$('#txtEndDt').css('visibility', 'hidden');
	$('#btnReq').attr('disabled', true);
	$('#btnReq').css('visibility', 'hidden');
	$('#txtConf').attr('readonly', true);
	$("#hbxWeb").fadeTo(0, 0.5);
	$('#hbxWeb').css('pointer-events', 'none');		
	$('#rdo1').prop('checked', true);
	$('#txtConf').val("");
	$('#txtEndDt').val("");		
}

function isrEndCall() {
	var data = {
		IsrId : strIsrId,
		requestType : 'getISRInfo_End'
	}
	ajaxAsync('/webPage/srcommon/SRRegister', data, 'json', successgetISRInfo_End);
}

function successgetISRInfo_End(data) {
	var tmpArray = [];
			
	tmpArray = data;			
	
	if (tmpArray.length > 0) {
		if (tmpArray[0].cc_satiscd != "" && tmpArray[0].cc_satiscd != null) { 
			if (tmpArray[0].cc_satiscd == "1")  {
				$('#rdo1').prop('checked', true);
			} else if (tmpArray[0].cc_satiscd == "2") {
				$('#rdo2').prop('checked', true);
			} else if (tmpArray[0].cc_satiscd == "3") { 
				$('#rdo3').prop('checked', true);
			} else if (tmpArray[0].cc_satiscd == "4") { 
				$('#rdo4').prop('checked', true);
			} else if (tmpArray[0].cc_satiscd == "5") { 
				$('#rdo5').prop('checked', true);
			}
			
			$('#txtEndDt').val(tmpArray[0].enddt);
			$('#txtConf').val(tmpArray[0].cc_edcomsayu);
			$('#txtEndDt').css('visibility', 'visible');
			$('#lblEndDt').css('visibility', 'visible');
			
		}
		if (tmpArray[0].cc_editor == strUserId && tmpArray[0].endok == "OK") {
			$('#btnReq').css('visibility', 'visible');
			$('#btnReq').attr('disabled', false);
			$('#txtConf').attr('readonly', false);
			$("#hbxWeb").fadeTo(0, 1);
			$('#hbxWeb').css('pointer-events', 'visible');		
		}
			
	} else {
		dialog.alert("ISR정보를 찾을 수 없습니다. [" + strIsrId + "]");
	}
}
//종료등록 클릭
function btnReq_click() {
	if($('#txtConf').val().trim().length == 0){
		dialog.alert("종료의견을 입력하여 주십시오.");
		$('#txtConf').focus();
		return;
	}
	
	if ($('#rdo1').prop('checked') == false && $('#rdo2').prop('checked') == false && 
		$('#rdo3').prop('checked') == false && $('#rdo4').prop('checked') == false && 
		$('#rdo5').prop('checked') == false) {
		dialog.alert("만족도를 선택하여 주시기 바랍니다.");
		return;
	} 
	var etcObj = new Object();
	etcObj.strUserId = strUserId;
	etcObj.reqcd = strReqCd;
	etcObj.Confirm = $('#txtConf').val().trim();
	if ($('#rdo1').prop('checked') == true) { 
		etcObj.grpRdo = "1";
	} else if ($('#rdo2').prop('checked') == true) { 
		etcObj.grpRdo = "2";
	} else if ($('#rdo3').prop('checked') == true) { 
		etcObj.grpRdo = "3";
	} else if ($('#rdo4').prop('checked') == true) { 
		etcObj.grpRdo = "4";
	} else if ($('#rdo5').prop('checked') == true) { 
		etcObj.grpRdo = "5";
	}
	 
	etcObj.isrid = strIsrId;
	
	screenInit("M");
	
	var etcData = new Object();
	etcData.etcObj = etcObj;
	etcData.requestType = "setISRInfo2"

	ajaxAsync('/webPage/srcommon/SRRegister', etcData, 'json', successSetISRInfo);
	
	etcObj = null;
}

function successSetISRInfo(data) {
	if (data != "0"){
		dialog.alert("요청종료 처리 중 오류가 발생하였습니다.\n" + data);
		return;
	}
	dialog.alert("요청종료가 정상적으로 처리되었습니다.");
	
	isrEndCall();
}