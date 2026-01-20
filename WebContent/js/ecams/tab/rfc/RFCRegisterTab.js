/** RFC 발행 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-04-19
 */

var userName = window.top.userName;
var userId = window.top.userId;
var adminYN = window.top.adminYN;
var userDeptName = window.top.userDeptName;
var userDeptCd = window.top.userDeptCd;
var strReqCd = window.top.reqCd;
var codeList    = window.top.codeList;          //전체 코드 리스트

var grdFile = new ax5.ui.grid();
var picker = new ax5.ui.picker();
var approvalModal = new ax5.ui.modal();

var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var strSubStatus = "";
var strHandler = "";
var popSw = false;
var strAcptNo = "";
var strUpURL = "";
var attPath = "";
var strWorkD = "";
var ingSw = false;
var fileUpPop = null;

var gyulPopUp = null;
var resultMSG = "";
var attFile_dp = [];
var grdFile_dp = [];
var cboCnt_dp = [];
var getReqRFCInfo_dp = [];
var confirm_dp = [];
var confirmData = [];
var confirmInfoData = null;
// var _refAddFiles:FileReferenceList;

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

$('#datEXPDAY').val(getDate('DATE', 0));

picker.bind(defaultPickerInfo('tabDate', 'top'));

$('[data-ax5select="cboCnt"]').ax5select({
	options : []
});

$(document).ready(function() {
	// 저장
	$('#btnSave').bind('click', function() {
		btnSave_click();
	});
	// 결재상신
	$('#btnReq').bind('click', function() {
		btnReq_click();
	});
	// 수정
	$('#btnUpdate').bind('click', function() {
		btnUpdate_click();
	});			
	// 결재정보
	$('#btnConf').bind('click', function() {
		conf_Click();
	});		
	// 결재
	$('#btnUpdate').bind('click', function() {
		btnOk_click();
	});			

	geteCAMSDir();
	var strYear = "";
	var date = new Date();
	strYear = date.getFullYear().toString();
	//Cmr0020.getWorkDays(Stryear);
	getWorkDays(strYear);
})

grdFile.setConfig({
	target : $('[data-ax5grid="grdFile"]'),
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
			iSRID_Click(this.item, this.dindex, false);
		}
	},
	columns : [ {
		key : "cc_attfile",
		label : "파일명",
		width : 400,
		align : 'center'
	}, {
		key : "cm_username",
		label : "첨부인",
		width : 100,
		align : 'center'
	}, {
		key : "lastdt",
		label : "첨부일",
		width : 300,
		align : 'center'
	} ]
});

function screenInit(scrGbn) {
	if (scrGbn == "M") {
		cboCnt_dp = [];
		grdFile_dp = [];
		grdFile.setData([]);
	}	
	$('#txtCreatdt').val("");
	$('#txtRfcEndDt').val("");
	$('#txtEditor').val("");
	$('#txtSta').val("");
	$('#txtEXPRUNTIME').val("");
	$('#txtEXPMM').val("");
	$('#txtEXPDAY').val("");
	$('#txtEXPRSC').val("");
	$('#txtConf').val("");
	$('#btnUpdate').css("visibility", "hidden");

	$('#txtEXPRUNTIME').css("readonly", true);
	$('#txtEXPDAY').css("readonly", true);
	$('#txtEXPRSC').css("readonly", true);
	$('#txtConf').css("readonly", true);

	$('#datEXPDAY').css("visibility", "hidden");
	$('#cal1').css("visibility", "hidden");
	$('#txtEXPDAY').css("visibility", "visible"); 

	$('#chkEXPRSC').css("display", "none");
	// $('#lbEXPRSC').css("visibility", "visible");

	$('#btnSave').attr("disabled", true);
	$('#btnReq').attr("disabled", true);
	$('#btnConf').css("visibility", "hidden");

	$('#txtEXPRUNTIME').css("backgroundColor","0xFFFFFF");
	$('#txtEXPDAY').css("backgroundColor","0xFFFFFF");

	$('#btnOk').css("visibility", "hidden");
	$('#btnCncl').css("visibility", "hidden");
	$('#btnAddFile').attr("disabled", true);
	$('#btnSave').css("visibility", "visible");
	$('#btnReq').css("visibility", "visible");
}

function geteCAMSDir() {
	var data = {
		pcode : "21,F2",
		requestType : 'eCAMSDir'
	}
	ajaxAsync('/webPage/common/CommonSystemPath', data, 'json',
			successGeteCAMSDir);
}

function successGeteCAMSDir(data) {
	var tmpArray = new Array();

	tmpArray = data;
	if (tmpArray.length > 0) {
		for (var i = 0; tmpArray.length > i; i++) {
			if (tmpArray[0].cm_pathcd == "F2") {
				strUpURL = tmpArray[0].cm_path;
			} else if (tmpArray[0].cm_pathcd == "21") {
				attPath = tmpArray[0].cm_path;
			}
		}
	} else {
		dialog.alert("파일을 저장할 디렉토리정보를 추출 중 오류가 발생하였습니다. 관리자에게 연락하여 주십시오.");
		return;
	}
}

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

function rfcCall() {
	// Cmc0300.getRfcCnt(strIsrId,strIsrSub,strUserId,strReqCd);
	// Cmc0010.getDocList(strIsrId,strIsrSub,strReqCd,"00");
	getRfcCnt();
	getDocList();
}

function getRfcCnt() {
	var grdObj = new Object();
	grdObj.IsrId = strIsrId;
	grdObj.IsrSub = strIsrSub;
	grdObj.UserId = strUserId
	grdObj.ReqCd = strReqCd;

	var grdInfoData = new Object();
	grdInfoData.grdData = grdObj;
	grdInfoData.requestType = 'getRfcCnt';
	ajaxAsync('/webPage/rfc/RFCRegister', grdInfoData, 'json',
			successGetRfcCnt);
}

function successGetRfcCnt(data) {
	cboCnt_dp = data;

	$('[data-ax5select="cboCnt"]').ax5select({
		options : injectCboDataToArr(cboCnt_dp, 'cc_seqno', 'rfcseq')
	});

	if(cboCnt_dp.length > 0) {
		$('[data-ax5select="cboCnt"]').ax5select("setValue", '1', true);
		cboCnt_click();
	}	
}

function cboCnt_click() {
	screenInit("S");
	if (attFile_dp.length > 0) {	
		grdFile_dp = attFile_dp.source;
	}
	
	if (getSelectedIndex('cboCnt') < 0) {
		return;
	}
	
	$('#txtSta').val(getSelectedVal('cboCnt').sta);
	if (getSelectedVal('cboCnt').cc_status != "X") {			
		$('#txtCreatdt').val(getSelectedVal('cboCnt').rfcreqdt);
		$('#txtRfcEndDt').val(getSelectedVal('cboCnt').rfceddate);
		$('#txtEditor').val(getSelectedVal('cboCnt').cm_username);
		$('#txtEXPRUNTIME').val(getSelectedVal('cboCnt').cc_expruntime);			
		$('#txtEXPDAY').val(getSelectedVal('cboCnt').cc_expday.substr(0,4)+"/"+getSelectedVal('cboCnt').cc_expday.substr(4,2)+"/"+getSelectedVal('cboCnt').cc_expday.substr(6,2));
		$('#datEXPDAY').val(getSelectedVal('cboCnt').cc_expday.substr(0,4)+"/"+getSelectedVal('cboCnt').cc_expday.substr(4,2)+"/"+getSelectedVal('cboCnt').cc_expday.substr(6,2));  
		$('#txtEXPMM').val(getSelectedVal('cboCnt').cc_expmm);
		if (getSelectedVal('cboCnt').cc_exprsc != null) {
			$('#chkEXPRSC').prop('checked', true);
		}
		$('#txtEXPRSC').val(getSelectedVal('cboCnt').cc_exprsc);
		
		if (getSelectedVal('cboCnt').cc_acptno != "" && getSelectedVal('cboCnt').cc_status != "0") {
			$('#btnConf').css("visibility", "visible");
			$('#btnConf').attr("disabled", false);
			$('#txtEXPRUNTIME').css("backgroundColor","0xEEEEEE");
			$('#txtEXPDAY').css("backgroundColor","0xEEEEEE");
			
		}
	} else if (getSelectedVal('cboCnt').length > 1) {
		$('#txtEXPRUNTIME').val(getSelectedVal('cboCnt').cc_expruntime);			
		$('#txtEXPDAY').val(getSelectedVal('cboCnt').cc_expday.substr(0,4)+"/"+getSelectedVal('cboCnt').cc_expday.substr(4,2)+"/"+getSelectedVal('cboCnt').cc_expday.substr(6,2));
		$('#datEXPDAY').val(getSelectedVal('cboCnt').cc_expday.substr(0,4)+"/"+getSelectedVal('cboCnt').cc_expday.substr(4,2)+"/"+getSelectedVal('cboCnt').cc_expday.substr(6,2));  
		$('#txtEXPMM').val(getSelectedVal('cboCnt').cc_expmm);
		if (getSelectedVal('cboCnt').cc_exprsc != null) {
			$('#chkEXPRSC').prop('checked', true);
		}
		$('#txtEXPRSC').val(getSelectedVal('cboCnt').cc_exprsc);	
	}
	if (getSelectedVal('cboCnt').secusw == "Y") {

		$('#txtEXPRUNTIME').attr('readonly', false);		

		$('#txtEXPRSC').attr('readonly', false);
		$('#chkEXPRSC').css('display', '');
		$('#lbEXPRSC').css('margin-left', '20px');

		$('#btnAddFile').attr('disabled', false);
		$('#btnReq').attr('disabled', false);
		$('#txtEXPDAY').css('visibility', 'hidden'); 
		// $('#lbEXPRSC').css('visibility', 'hidden');
		$('#datEXPDAY').css('visibility', 'visible');
		$('#cal1').css('visibility', 'visible');
		$('#btnSave').attr('disabled', false);	
	}
	else{
		if (getSelectedVal('cboCnt').cc_substatus !='1B' && getSelectedVal('cboCnt').cc_substatus !='19'){
			$('#txtEXPDAY').css('visibility', 'hidden');
			// $('#lbEXPRSC').css('visibility', 'hidden');
			$('#datEXPDAY').css('visibility', 'visible');
			$('#cal1').css('visibility', 'visible');
			$('#btnUpdate').css('visibility', 'visible');
			$('#btnUpdate').attr('disabled', false);
		}	
	}
	chkEXPRSC_click();

	if (getSelectedVal('cboCnt').cc_status == "1") {
		//Cmr3100.gyulChk(getSelectedVal('cboCnt').cc_acptno,strUserId);
		var data = {
			AcptNo : getSelectedVal('cboCnt').cc_acptno,
			UserId : strUserId,
			requestType : 'gyulChk'
		}
		ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
				successGyulChk);

	}
}
//결재정보 체크
function successGyulChk(data) {
	if (data == "0") {
		$('#btnOk').attr('disabled', false);
		$('#btnCncl').attr('disabled', false);
		$('#btnOk').css('visibility', 'visible')
		$('#btnCncl').css('visibility', 'visible')
		$('#btnSave').css('visibility', 'hidden')
		$('#btnReq').css('visibility', 'hidden')
		
		$('#txtConf').css('visibility', 'visible')
		$('#lblConf').css('visibility', 'visible')
		$('#txtConf').attr('readonly', false);
	} else if (data != "1" && data != "2") {
		dialog.alert("결재정보 체크 중 오류가 발생하였습니다.");
	} 
}

function chkEXPRSC_click() {
	if ($('#chkEXPRSC').prop('checked') == true) {
		$('#txtEXPRSC').attr('disabled', false);
	} else {
		$('#txtEXPRSC').attr('disabled', true);
		$('#txtEXPRSC').val("");
	}
}

$('#chkEXPRSC').bind('change', function() {
	if ($('#chkEXPRSC').prop('checked') == true) {
		$('#txtEXPRSC').attr('disabled', false);
	} else {
		$('#txtEXPRSC').attr('disabled', true);
		$('#txtEXPRSC').val("");
	}
})

//파일리스트 조회
function getDocList() {
	var data = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		ReqCd : strReqCd,
		SubReq : "00",
		requestType : 'getDocList'
	}
	ajaxAsync('/webPage/srcommon/SRRegister', data, 'json',
			successGetDocList);
}

function successGetDocList(data) {
	if (data !== 'ERR') {
		grdFile_dp = data;
		grdFile.setData(grdFile_dp);
	}
}
//예상투입공수 계산
$('#txtEXPRUNTIME').bind('change', function() {
	var gap = 0;
	var StrGap = "";
	if($('#txtEXPRUNTIME').val().trim().length == 0) {
		$('#txtEXPMM').val("");
	} else {
		gap = ($('#txtEXPRUNTIME').val()/8)/strWorkD
		StrGap = String(gap);
		if (StrGap.indexOf(".") > 0) {
				StrGap = StrGap.substring(0,StrGap.indexOf(".")+3);
		   }
		   $('#txtEXPMM').val(StrGap);
	}
})

//저장버튼 클릭 
function btnSave_click() {
	var etcObj = new Object();
	var tmpWork1 =  "";
	
	if($('#txtEXPRUNTIME').val().length == 0){
		dialog.alert("예상소요시간을 입력하여 주십시오.");
		return;
	}
	if ($('#txtEXPMM').val() === Infinity) {
			dialog.alert("월근무일수가 등록되지 않았습니다.");
		return;
	}
	if ($('#txtEXPMM').val().length == 0) {
		dialog.alert("예상투입공수를 입력하여 주시기 바랍니다.");
		$('#txtEXPRUNTIME').focus();
		return;
	} else if ($('#txtEXPMM').val().trim() == "0.00") {
		dialog.alert("예상소요시간이 너무 작아서 예상투입공수를 산정할 수 없습니다. 예상소요시간을 조정하여 주시기 바랍니다.");
		$('#txtEXPRUNTIME').focus();
		return;
	}
	if ($('#datEXPDAY').val().length == 0){
		dialog.alert("완료예정일를 선택하여 주십시오.");
		return;
	}

	var NowDate = getDate('DATE',0);
	
	if (replaceAllString($('#datEXPDAY').val(),"/","") < NowDate){
		dialog.alert("완료예정일이 현재일 이전입니다. 확인하여 주십시오.");
		$('#datEXPDAY').focus();
		return;
	} 
	if ($('#chkEXPRSC').prop('checked') == true && $('#txtEXPRSC').val().length == 0){
		dialog.alert("예상소요자원를 입력하여 주십시오.");
		return;
	}
	if (ingSw == true) {
		dialog.alert("현재 요청하신 작업을 수행 중입니다. 잠시만 기다려 주시기 바랍니다.");
		return;
	}

	ingSw = true;
	etcObj.userid = strUserId;
	etcObj.CC_EXPRUNTIME = $('#txtEXPRUNTIME').val();
	etcObj.CC_EXPMM = $('#txtEXPMM').val();
	etcObj.CC_EXPDAY = replaceAllString($('#datEXPDAY').val(),"/","");
	etcObj.CC_EXPRSC = $('#txtEXPRSC').val();
	etcObj.isrid = strIsrId;
	etcObj.isrsub = strIsrSub;
	etcObj.gbncd = "SAVE";
	etcObj.seqno = getSelectedVal('cboCnt').cc_seqno;
	etcObj.reqcd = strReqCd;
	//Cmc0300.setRFCConfirm(etcObj,confirm_dp.toArray());

	var data = {
		etcObj : etcObj,
		confirm : confirm_dp,
		requestType : 'setRFCConfirm'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successSetRFCConfirm);

	etcObj = null;
}

function successSetRFCConfirm(data) {
	var findSw = false;
	ingSw = false;
	if (data.toString() == "0") {
		dialog.alert("RFC발행요청 등록이 정상적으로 처리되었습니다.");	
		
		if (grdFile_dp.length > 0) {					
			for (var i = 0; grdFile_dp.length > i; i++) {
				if (grdFile_dp[0].cc_seqno == null || grdFile_dp[0].cc_seqno == "") {
					findSw = true;
					break;
				}
			}					
		} 
		
		if (findSw == true) {
			fileOpen();
		} else {
			screenInit("M");
			rfcCall();	
		}		
	} else {
		dialog.alert("RFC발행요청 등록 중 오류가 발생하였습니다.\n"+ data.toString());
	}
}
//파일
function fileOpen() {

}
//결재상신 버튼 클릭
function btnReq_click() {
	var tmpWork1 = "";
			
	if($('#txtEXPRUNTIME').val().length == 0){
		dialog.alert("예상소요시간을 입력하여 주십시오.");
		return;
	}
	if ($('#txtEXPMM').val() === Infinity) {
			dialog.alert("월근무일수가 등록되지 않았습니다.");
		return;
	}
	if ($('#txtEXPMM').val().length == 0) {
		dialog.alert("예상투입공수를 입력하여 주시기 바랍니다.");
		$('#txtEXPRUNTIME').focus();
		return;
	} else if ($('#txtEXPMM').val().trim() == "0.00") {
		dialog.alert("예상소요시간이 너무 작아서 예상투입공수를 산정할 수 없습니다. 예상소요시간을 조정하여 주시기 바랍니다.");
		$('#txtEXPRUNTIME').focus();
		return;
	}
	if ($('#datEXPDAY').val().length == 0){
		dialog.alert("완료예정일를 선택하여 주십시오.");
		return;
	}

	var NowDate = getDate('DATE',0);
	
	if (replaceAllString($('#datEXPDAY').val(),"/","") < NowDate){
		dialog.alert("완료예정일이 현재일 이전입니다. 확인하여 주십시오.");
		$('#datEXPDAY').focus();
		return;
	} 
	if ($('#chkEXPRSC').prop('checked') == true && $('#txtEXPRSC').val().length == 0){
		dialog.alert("예상소요자원를 입력하여 주십시오.");
		return;
	}
	if (ingSw == true) {
		dialog.alert("현재 요청하신 작업을 수행 중입니다. 잠시만 기다려 주시기 바랍니다.");
		return;
	}

	ingSw = true;

	var confirmInfoData = new Object();
	confirmInfoData.SysCd = "99999";
	confirmInfoData.strRsrcCd = "";
	confirmInfoData.ReqCd = strReqCd;
	confirmInfoData.UserID = strUserId;
	confirmInfoData.strQry = "";

	var confirmInfoData = {
		requestType : 'confSelect',
		confirmInfoData : confirmInfoData
	}	

	ajaxAsync('/webPage/request/ConfirmServlet', confirmInfoData, 'json',
			successConfSelect);	
	confirmInfoData = null;
}

function successConfSelect(data) {
	var retMsg = data;
	if(retMsg == "C") {
		dialog.alert({
			msg: "결재자를 지정하시겠습니까?",
			onStateChanged: function() {
				if(this.state === "close") {
					confChkl(this.state);					
				}
			}
		})
	} else if (retMsg == "Y") {
		window.parent.confCall("Y");
	} else  if(retMsg != "N") {
		ingSw = false;
		dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
	} else {
		window.parent.confCall("N");
	}
}

function confChkl(state) {
	if(state == "close") {
		window.parent.confCall("Y") 
	} else {
		window.parent.confCall("N");
	}
}

//function confCall(GbnCd) {
//	confirmInfoData = null;
//
//	confirmInfoData = {		
//		UserId : strUserId,
//		ReqCd  : strReqCd,
//		SysCd  : "99999",
//		RsrcCd : [],
//		EmgSw : "N",//긴급여부
//		PrjNo : strIsrId+strIsrSub,
//		QryCd : [],
//		PgmType : "",
//		deployCd : ""
//	}
//
//	if(GbnCd == "Y") {
//			approvalModal.open({
//	        width: 820,
//	        height: 400,
//	        iframe: {
//	            method: "get",
//	            url: "../../modal/request/ApprovalModal.jsp",
//	            param: "callBack=modalCallBack"
//		    },
//	        onStateChanged: function () {
//	            if (this.state === "open") {
//	                mask.open();
//	            }
//	            else if (this.state === "close") {
//	            	if(confirmData.length > 0){
//	            		reqQuest(confirmData, this.state);
//	            	}
//	                mask.close();
//					ingSw = false;
//	            }
//	        }
//		});
//	} else {
//		confirmInfoData = {		
//				UserID : strUserId,
//				SysCd  : "99999",
//				ReqCD  : strReqCd,
//				Rsrccd : [],
//				QryCd : [],
//				EmgSw : "N",
//				PrjNo  : strIsrId+strIsrSub,
//				deployCd : "0",
//				JobCd : ""
//		}
//		
//		var tmpData = {
//			confirmInfoData: 	confirmInfoData,
//			requestType: 	'Confirm_Info'
//		}
//		ajaxAsync('/webPage/request/ConfirmServlet', tmpData, 'json', successConfirm_Info);
//	}
//}

function successConfirm_Info(data) {
	confirm_dp = data;
	var i = 0;

	for(var i = 0; confirm_dp.length > i; i++) {
		if(confirm_dp[i].arysv[0].svUser == null && confirm_dp[i].arysv[0].SvUser == "") {
			var removeIndex = confirm_dp.map(function(item) { return item.cm_baseuser; }).indexOf(confirm_dp[i].cm_baseuser);
			confirm_dp.splice(removeIndex, 1);
			--i;
		}
	}	
	reqQuest_Conf();	
}

function reqQuest(data, state) {
	if(state == 'close') {
		confirm_dp = data;
		reqQuest_Conf();
	} else {
		ingSw = false;
	}
}

function reqQuest_Conf() {
	var etcObj = new Object();

	etcObj.userid = strUserId;
	etcObj.reqcd = strReqCd;
	etcObj.CC_EXPRUNTIME = $('#txtEXPRUNTIME').val();
	etcObj.CC_EXPMM = $('#txtEXPMM').val();
	etcObj.CC_EXPDAY = replaceAllString($('#datEXPDAY').val(),"/","");
	etcObj.CC_EXPRSC = $('#txtEXPRSC').val();
	etcObj.isrid = strIsrId;
	etcObj.isrsub = strIsrSub;
	etcObj.gbncd = "REQ";
	etcObj.seqno = getSelectedVal('cboCnt').cc_seqno;

	var data = {
		etcObj : etcObj,
		confirm : confirm_dp,
		requestType : 'setRFCConfirm'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successSetRFCConfirm);	
	
	etcObj = null;
}
//수정 버튼 클릭
function btnUpdate_click() {
	var etcObj = new Object();
			
	if ($('#datEXPDAY').val().length == 0){
		dialog.alert("완료예정일를 선택하여 주십시오.");
		return;
	}
	etcObj.isrid = strIsrId;
	etcObj.isrsub = strIsrSub;
	etcObj.seqno = getSelectedVal('cboCnt').cc_seqno;
	etcObj.CC_EXPDAY = replaceAllString($('#datEXPDAY').val(),"/","");
	// Cmc0300.updateRFC(etcObj);
	var data = {
		etcObj : etcObj,
		requestType : 'updateRFC'
	}	
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successUpdateRFC);		
}

function successUpdateRFC(data) {
	if (data == "0") {
		dialog.alert("완료예정일이 정상적으로 수정되었습니다.");	
	}
}
//결재 버튼 클릭
function btnOk_click(){
	if (ingSw == true) {
		dialog.alert("현재 요청하신 작업을 처리 중 입니다. 잠시만 기다려 주시기 바랍니다.");
		return;
	}
	ingSw = true;
	dialog.alert({
		msg: "결재처리하시겠습니까?",
		onStateChanged: function() {
			if(this.state === "close") {
				confOk(this.state);					
			}
		}
	})	
}

function confOk(state) {
	if (state == 'close') {
		   var grdObj = new Object();
		   grdObj.AcptNo = getSelectedVal('cboCnt').cc_acptno;
		   grdObj.UserId = strUserId;
		   grdObj.conMsg = $('#txtConf').val().trim();
		   grdObj.Cd = "1";
		   grdObj.ReqCd = strReqCd;
	   
		   var grdInfoData = new Object();
		   grdInfoData.grdData = grdObj;
		   grdInfoData.requestType = 'nextConf_ISR';
		   ajaxAsync('/webPage/rfc/RFCRegister', grdInfoData, 'json',
				   successNextConf_ISR);		   
		   resultMSG = "결재";
	} else {
		ingSw = false;
	}
}

function successNextConf_ISR(data) {
	ingSw = false;
	if (data == "0") {
		dialog.alert(resultMSG+"처리가  완료 되었습니다.");
		
		if (popSw == true) {
			//ExternalInterface.call("winclose","OK");
		} else {
			screenInit("M");
			rfcCall();
			window.parent.getISRList();
		}
	}else{
		dialog.alert(resultMSG+"처리에 실패하였습니다.");
	}
}
//결재정보 클릭
function conf_Click(){

	var winName = "checkInEnd";
	var f = document.popPam;   		//폼 name
    
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	f.acptno.value	= getSelectedVal('cboCnt').cc_acptno;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	nHeight = 740;
    nWidth  = 1200;

	cURL = "/webPage/winpop/PopApprovalInfo.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

function cmdCncl_click() {
	if(txtConf.text == ""){
		 dialog.alert("반려의견을 입력하여 주십시오.");
		 return;
	}
	if (ingSw == true) {
		dialog.alert("현재 요청하신 작업을 처리 중 입니다. 잠시만 기다려 주시기 바랍니다.");
		return;
	}
	ingSw = true;

	dialog.alert({
		msg: "반려처리하시겠습니까?",
		onStateChanged: function() {
			if(this.state === "close") {
				cnclChk(this.state);					
			}
		}
	})		
}

function cnclChk(state) {
	if (state == 'close') {
		var grdObj = new Object();
		grdObj.AcptNo = getSelectedVal('cboCnt').cc_acptno;
		grdObj.UserId = strUserId;
		grdObj.conMsg = $('#txtConf').val().trim();
		grdObj.Cd = "3";
		grdObj.ReqCd = strReqCd;
	
		var grdInfoData = new Object();
		grdInfoData.grdData = grdObj;
		grdInfoData.requestType = 'nextConf_ISR';
		ajaxAsync('/webPage/rfc/RFCRegister', grdInfoData, 'json',
				successNextConf_ISR);			
		resultMSG = "반려";
	} else ingSw = false;
}

//파일 첨부
function fileopen() {}
