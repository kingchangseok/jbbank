/** 변경관리종료 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-05-03
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
var confirmDialog 	= new ax5.ui.dialog();

var gyulPopUp = null;
var fileUpPop = null;
var cboEditor_dp = [];
var grdFile_dp = [];
var confirm_dp = [];
var _refAddFiles = null;
var attFile_dp = [];
var confirmData = [];
var confirmInfoData = null;

var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var strMainStatus = "";
var strSubStatus = "";
var strSubReq = "";
var popSw = false;
var strAcptNo = "";
var strUpURL = "";
var attPath = "";
var resultMSG = "";
var ingSw = false;
var acptNo = "";

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

$('#datTermSt').val(getDate('DATE', 0));
$('#datTermEd').val(getDate('DATE', 0));

picker.bind(defaultPickerInfo('botDate', 'top'));

$('[data-ax5select="cboEditor"]').ax5select({
	options : []
});

$(document).ready(function() {
	// 등록
	$('#btnReq').bind('click', function() {
		btnReq_click();
	});
	// 반려
	$('#btnCncl').bind('click', function() {
		btnCncl_click();
	});			
	// 결재정보
	$('#btnConf').bind('click', function() {
		conf_click();
	});		
	// 결재
	$('#btnOk').bind('click', function() {
		btnOk_click();
	});	
	//변경담당자 변경
	$('#cboEditor').bind('change', function() {
		cboEditor_click();
	});

	geteCAMSDir();
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
			fileClick(this.item, this.dindex, false);
		},
		onDBLClick : function() {
			fileDbClick(this.dindex, this.item);
		}
	},
	columns : [ {
		key : "cc_attfile",
		label : "파일명",
		width : '40%',
		align : 'center'
	}, {
		key : "cm_username",
		label : "첨부인",
		width : '20%',
		align : 'center'
	}, {
		key : "lastdt",
		label : "첨부일",
		width : '20%',
		align : 'center'
	} ]
});

function gridEdit() {
	grdFile.addColumn({
		key : "test",
		label : "test",
		width : '20%',
		align : 'center'
	});
	var lastIndex = grdFile.columns.length -1
	grdFile.removeColumn(lastIndex);
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
		for (var i = 0; tmpArray[0].length > i; i++) {
			if (tmpArray[i].cm_pathcd == "F2") {
				strUpURL = tmpArray[i].cm_path;
			} else if (tmpArray[i].cm_pathcd == "21") {
				attPath = tmpArray[i].cm_path;
			}
		}
	} else {
		dialog.alert("파일을 저장할 디렉토리정보를 추출 중 오류가 발생하였습니다. 관리자에게 연락하여 주십시오.");
		return;
	}
}

function screenInit(scrGbn) {
			
	if (scrGbn == "M") {
		cboEditor_dp = [];
		grdFile_dp = [];
		grdFile.setData([]);
	}
	$('#optEnd').prop('checked', true);
	
	$('#txtWriteDay').val("");
	$('#txtRealTerm').val("");
	$('#txtRealMM').val("");
	$('#txtMonitor').val("");
	$('#txtRealDay').val("");
	$('#txtEtc').val("");
	$('#txtConf').val("");
	$('#txtEndDate').val("");
	$('#txtSta').val("");
	if (popSw == false) {
		strAcptNo = "";
	}
	$('#btnAddFile').attr('disabled', true);
	$('#btnReq').attr('disabled', true);
	
	$('#txtConf').attr('readonly', true);
	$('#txtEtc').attr('readonly', true);
	$('#datTermSt').attr('disabled', true);
	$('#datTermEd').attr('disabled', true);
	$('#cal1').css("pointer-events", "visible");
	$('#cal1').css("background-color", "whitesmoke");
	$('#cal2').css("pointer-events", "visible");
	$('#cal2').css("background-color", "whitesmoke");		
	$('#hbxWeb').fadeTo(0, 0.5);
	$('#hbxWeb').css('pointer-events', 'none');	
	$('#optEnd').attr('disabled', true);
	$('#optCncl').attr('disabled', true);
	gridEdit();
}

function cboEditor_click() {
			
	screenInit("S");
	if (grdFile_dp.length > 0) {
		grdfile_filter(attFile_dp);
		grdFile.setData(grdFile_dp);
	}
	if (getSelectedIndex('cboEditor') < 0) { 
		return;
	}
	
	if (getSelectedVal('cboEditor').cc_status == "0")  {
		$('#txtSta').val("변경관리진행 중");
	} else if (getSelectedVal('cboEditor').cc_status == "1") {
		$('#txtSta').val("종료요청 결재대기 중");
	} else if (getSelectedVal('cboEditor').cc_status == "3") { 
		$('#txtSta').val("종료요청 반려처리");
	} else if (getSelectedVal('cboEditor').cc_status == "9") { 
		$('#txtSta').val("종료요청완료");
	}
	
	if (getSelectedVal('cboEditor').cc_status != "0") {
		$('#optCncl').attr('disabled', false);
		$('#optEnd').attr('disabled', false);
		if (getSelectedVal('cboEditor').cc_edgbncd == "3") { 
			$('#optCncl').prop('checked', true);
		} else {
			$('#optEnd').prop('checked', true);
		}
		if (getSelectedVal('cboEditor').edreqdt != ""){
			$('#txtWriteDay').val(getSelectedVal('cboEditor').edreqdt.substr(0,4)+"/"+getSelectedVal('cboEditor').edreqdt.substr(5,2)+"/"+
								  getSelectedVal('cboEditor').edreqdt.substr(8,2));
		}
		if (getSelectedVal('cboEditor').cc_status == "9") {
			$('#txtEndDate').val(getSelectedVal('cboEditor').eddate.substr(0,4)+"/"+getSelectedVal('cboEditor').eddate.substr(5,2)+"/"+
							  getSelectedVal('cboEditor').eddate.substr(8,2));
		}	
		$('#txtEtc').val(getSelectedVal('cboEditor').cc_jobetc);
		$('#datTermSt').val((getSelectedVal('cboEditor').cc_jobstday.substr(0,4))+'/'+
	  						(getSelectedVal('cboEditor').cc_jobstday.substr(4,2))+'/'+
	  						(getSelectedVal('cboEditor').cc_jobstday.substr(6,2)));
		$('#datTermEd').val((getSelectedVal('cboEditor').cc_jobedday.substr(0,4))+'/'+
				  			(getSelectedVal('cboEditor').cc_jobedday.substr(4,2))+'/'+
				  			(getSelectedVal('cboEditor').cc_jobedday.substr(6,2)));
		$('#btnConf').css('visibility', 'visible');
		$('#btnConf').attr('disabled', false);
	}
	
	$('#txtRealTerm').val(getSelectedVal('cboEditor').realday);
	$('#txtRealMM').val(getSelectedVal('cboEditor').realmm);
	$('#txtMonitor').val(getSelectedVal('cboEditor').monterm);
	$('#txtRealDay').val(getSelectedVal('cboEditor').prcdate);
	
	var strEdGbn = "";

	if ((getSelectedVal('cboEditor').cc_status == "0" || getSelectedVal('cboEditor').cc_status == "3") && getSelectedVal('cboEditor').value == strUserId) {
		if (strSubStatus == "49") {
			strEdGbn = "9"
		} else if (strSubStatus == "28") {
			if (getSelectedVal('cboEditor').edgbn == "9") {
				strEdGbn = "9";
			} else if (getSelectedVal('cboEditor').edgbn == "3" && getSelectedVal('cboEditor').pgmcnt == "0") {
				strEdGbn = "3";
			} else if (getSelectedVal('cboEditor').pgmcnt != "0") {
				strEdGbn = "9";
			}	
		} else if (strSubStatus == "2A") {
			if (getSelectedVal('cboEditor').pgmcnt != "0") {
				strEdGbn = "9";
			} else {
				strEdGbn = "A";
			}
		} else if (strSubStatus == '23' || strSubStatus == '24' || strSubStatus == '25' ||
				   strSubStatus == '31' || strSubStatus == '32' || strSubStatus == '34' ||
				   strSubStatus == '35' || strSubStatus =='36' || strSubStatus =='39') {
			if (getSelectedVal('cboEditor').pgmcnt == 0) strEdGbn = "3";            	
		}
		
		if (strEdGbn == "9" || strEdGbn == "A") {
			$('#optEnd').prop('checked', true);
			$('#optEnd').attr('disabled', false);
			$('#optCncl').attr('disabled', true);
		} 
		if (strEdGbn == "3" || strEdGbn == "A") {
			$('#optEnd').attr('disabled', true);
			$('#optCncl').prop('checked', true);
			$('#optCncl').attr('disabled', false);
		}

		if (strEdGbn == "3" || strEdGbn == "9" || strEdGbn == "A") { 
			$('#btnReq').attr('disabled', false);
			$('#txtEtc').attr('readonly', false);
			$('#datTermSt').attr('disabled', false);
			$('#datTermEd').attr('disabled', false);
			$('#btnAddFile').attr('disabled', false);	
			$('#cal1').css("pointer-events", "auto");
			$('#cal2').css("pointer-events", "auto");
			$('#cal1').css("background-color", "");	
			$('#cal2').css("background-color", "");	
			$('#hbxWeb').fadeTo(0, 1);
			$('#hbxWeb').css('pointer-events', 'visible');	
		}		
	} else if (getSelectedVal('cboEditor').cc_status == "1") {
		var data = {
			AcptNo : getSelectedVal('cboEditor').cc_acptno,
			UserId : strUserId,
			requestType : 'gyulChk'
		}
		ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
				successGyulChk);
	}
}

function grdfile_filter(data) {
	if (getSelectedIndex('cboEditor') < 0) { 
		return false;
	}

	for(var i = 0; data.length > i; i++) {
		if(Number(data[i].cc_subreq) == Number(getSelectedVal('cboEditor').cc_seqno)) {
			if(getSelectedVal('cboEditor').CC_STATUS == "3") {
				grdFile_dp.push(data[i]);
			}
		}
	}
}

//결재정보 체크
function successGyulChk(data) {
	if (data == "0") {
		$('#btnOk').attr('disabled', false);
		$('#btnCncl').attr('disabled', false);
		$('#btnOk').css('visibility', 'visible');
		$('#btnCncl').css('visibility', 'visible');
		
		$('#txtConf').css('visibility', 'visible');
		$('#lblConf').css('visibility', 'visible');
		$('#txtConf').attr('readonly', false);
	} else if (data != "1" && data != "2") {
		dialog.alert("결재정보 체크 중 오류가 발생하였습니다.");
	} else if (data == "2") {
		$('#btnOk').attr('disabled', false);
		$('#btnCncl').css('visibility', 'visible');
		
		$('#txtConf').css('visibility', 'visible')
		$('#lblConf').css('visibility', 'visible')
		$('#txtConf').attr('readonly', false);
	}
}
//등록
function btnReq_click() {
	if ($('#datTermSt').val() > $('#datTermEd').val()) {
		dialog.alert("작업시작일이 작업종료일이전입니다. 작업기간을 정확히 선택하여 주시기 바랍니다."); 
		return;
	}
	if (ingSw == true) {
		dialog.alert("현재 요청하신 작업을 처리 중 입니다. 잠시만 기다려 주시기 바랍니다.");
		return;
	}
	ingSw = true;
	$('#btnReq').attr('dsaibled', true);

	var confirmInfoData = new Object();
	confirmInfoData.SysCd = "99999";
	confirmInfoData.strRsrcCd = "";
	confirmInfoData.ReqCd = strReqCd;
	confirmInfoData.UserID = strUserId;
	confirmInfoData.strQry = strReqCd;

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
		$('#btnReq').attr('disabled', false);
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
//
//	var etcQryCd = [];
//	confirm_dp = [];
//	confirmInfoData = null;
//
//	etcQryCd = strReqCd.split(",");
//
//	confirmInfoData = {		
//		UserId : strUserId,
//		ReqCd  : strReqCd,
//		PrjNo : strIsrId+strIsrSub,
//		SysCd  : "99999",
//		RsrcCd : [],
//		QryCd : etcQryCd,
//		EmgSw : "N",
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
//
//	            		reqQuest(confirmData, this.state);
//	            	}
//	                mask.close();
//					ingSw = false;
//	            }
//	        }
//		});
//	} else {
//		confirmInfoData = {	
//			UserID : strUserId,
//			SysCd  : "99999",
//			ReqCD  : strReqCd,
//			Rsrccd : [],
//			QryCd : [],
//			EmgSw : "N",
//			PrjNo  : strIsrId+strIsrSub,
//			deployCd : "0",
//			JobCd : etcQryCd
//		}
//		var tmpData = {
//			confirmInfoData: 	confirmInfoData,
//			requestType: 	'Confirm_Info'
//		}
//		ajaxAsync('/webPage/request/ConfirmServlet', tmpData, 'json', successConfirm_Info);
//
//		etcQryCd = null;
//	}
//}

function reqQuest(data, state) {
	confirm_dp = data;
	if(state == 'close') {
		reqQuest_Conf();
	} else {
		$('#btnReq').attr('disabled', false);
		ingSw = false;
	}
}

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

function reqQuest_Conf() {
	var etcObj = new Object();
	
	etcObj.stday = replaceAllString($('#datTermSt').val(),"/","");
	etcObj.edday = replaceAllString($('#datTermEd').val(),"/","");
	etcObj.IsrId = strIsrId;
	etcObj.IsrSub = strIsrSub;
	etcObj.reqcd = strReqCd;
	etcObj.userid = getSelectedVal('cboEditor').value;
	etcObj.jobetc = $('#txtEtc').val().trim();
	
	if ($('#optEnd').prop('checked') == true) { 
		etcObj.endgbncd = "9";
	} else {
		etcObj.endgbncd = "3";
	}

	var confirmInfoData = {
		etcObj : etcObj,
		confList : confirm_dp,
		requestType : 'request_chgend'
	}	

	ajaxAsync('/webPage/rfc/RFCRegister', confirmInfoData, 'json',
			successRequest_chgend);		
 
 	etcObj = null;
}

function successRequest_chgend(data) {
	ingSw = false;
	if (data.substr(0,2) == "OK") {
		var findSw = false;
		if (grdFile_dp.length > 0) {					
			for (var i = 0; grdFile_dp.length > 0; i++) {
				if (grdFile_dp[i].cc_seqno == null || grdFile_dp[i].cc_seqno == "") {
					findSw = true;
					break;
				}
			}					
		} 
		strSubReq = data.substr(2);
			
		if (findSw == true) {
			fileOpen();
		} else {
			screenInit("M");
			chgEndCall();
			dialog.alert("변경관리 종료요청이 정상적으로 처리되었습니다.");	
		}			
	} else {
		dialog.alert("변경관리 종료요청 중 오류가 발생하였습니다.\n"+data.substr(2));			
	}	
}
//반려
function btnCncl_click() {
	txtConf.text = mx.utils.StringUtil.trim(txtConf.text);
	if(txtConf.text == ""){
		 mx.controls.Alert.show("반려의견을 입력하여 주십시오.");
		 return;
	}
	if (ingSw == true) {
		Alert.show("현재 요청하신 작업을 처리 중 입니다. 잠시만 기다려 주시기 바랍니다.");
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
		grdObj.AcptNo = getSelectedVal('cboEditor').cc_acptno;
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
	} else { 
		ingSw = false;
	}
}
// 결재정보
function conf_click() {
	if(getSelectedVal('cboEditor').cc_acptno == "" || getSelectedVal('cboEditor').cc_acptno == null){
		dialog.alert("신청번호가 없습니다. 관리자에게 문의해주세요.");
		return;
	}	
	var winName = "confdata";
	var f = document.popPam;   		//폼 name
    
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	f.acptno.value	= getSelectedVal('cboEditor').cc_acptno;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	nHeight = 740;
    nWidth  = 1200;

	cURL = "/webPage/winpop/PopApprovalInfo.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
// 결재
function btnOk_click() {
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
	if(state == "close") {
		
		var grdObj = new Object();
		grdObj.AcptNo = getSelectedVal('cboEditor').cc_acptno;
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
			chgEndCall();	
		}
	}else{
		dialog.alert(resultMSG+"처리에 실패하였습니다.");
	}
}

function chgEndCall() {
	getSelectList();			
	getDocList();
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
	scmSw = false;

	$('[data-ax5select="cboEditor"]').ax5select({
		options : injectCboDataToArr(cboEditor_dp, 'cc_scmuser', 'cm_username')
	});
	
	if(cboEditor_dp.length > 0){
		for (var i = 0; cboEditor_dp.length > i; i++) {
			if(strAcptNo != "" && strAcptNo != null) {
				if (cboEditor_dp[i].cc_scmuser == strUserId) {
					$('[data-ax5select="cboEditor"]').ax5select("setValue", cboEditor_dp[i].value, true);
					break;
				}
			} else {
				if (cboEditor_dp[i].cc_scmuser == strAcptNo) {
					$('[data-ax5select="cboEditor"]').ax5select("setValue", cboEditor_dp[i].value, true);
					break;
				}
			}
		}
		cboEditor_click();
	}
}

function getDocList() {
	ajaxReturnData = null;
	var docSr = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		ReqCd  : "49",
		SubReq : "00",
		requestType : 'getDocList'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/rfc/RFCRegister',
			docSr, 'json');
	if (ajaxReturnData !== 'ERR') {
		grdfile_filter(ajaxReturnData);
		grdFile_dp = ajaxReturnData;
		grdFile.setData(grdFile_dp);
	}
}
