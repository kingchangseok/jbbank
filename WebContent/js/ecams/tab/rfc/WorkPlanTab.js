/** 작업계획서 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-05
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

var strUserId = "";
var strReqCd = "";
var strIsrId = "";
var strIsrSub = "";
var strAcptNo = "";
var strConfirm = "";
var strUpURL = "";
var attPath = "";
var resultMSG = "";
var popSw = false;
var gyulPopUp = null;
var cboJOBGBN_dp = [];
var cboMONTERM_dp = [];

var fileUpPop = null;
var isrinfo_dp = [];
var grdFile_dp = [];
var confirm_dp = [];
var jobPlanInfo = [];
var cboEditor_dp = [];
var confirmData = [];
var confirmInfoData = null;

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

$('#dfJOBDATE').val(getDate('DATE', 0));
$('#dfENDRPTDAY').val(getDate('DATE', 0));

picker.bind(defaultPickerInfo('jobDate', 'top'));
picker.bind(defaultPickerInfo('rptDate', 'top'));

$('[data-ax5select="cboEditor"]').ax5select({
	options : []
});

$('[data-ax5select="cboJOBGBN"]').ax5select({
	options : []
});


$('[data-ax5select="cboMONTERM"]').ax5select({
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
	getCodeInfo();
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

//공통코드 조회
function getCodeInfo() {
	cboJOBGBN_dp = fixCodeList(codeList['JOBGBN'], 'SEL', '', '', 'N');
	cboMONTERM_dp = fixCodeList(codeList['MONTERM'], 'SEL', '', '', 'N');

	$('[data-ax5select="cboJOBGBN"]').ax5select({
		options: injectCboDataToArr(cboJOBGBN_dp, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboMONTERM"]').ax5select({
		options: injectCboDataToArr(cboMONTERM_dp, 'cm_micode' , 'cm_codename')
	});	
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
		grdFile_dp = [];
	}
	$('#txtJOBRECORD').val("");
	$('#txtJOBDETAIL').val("");
	$('#txtREFMSG').val("");
	$('#dfJOBDATE').val("");

	$('#txtENDRPTDAY').val("");
	$('#txtJOBDATE').val("");
	if (popSw == false) { 
		strAcptNo = "";
	}
		
	$('#btnSave').attr('disabled', true);
	$('#btnReq').attr('disabled', true);
	$('#btnConf').css('visibility', 'hidden');
	$('#lblConf').css('display', 'none');
	$('#btnOk').css('visibility', 'hidden');
	$('#btnCncl').css('visibility', 'hidden');
	$('#txtConf').css('display', 'none');
	$('#btnAddFile').attr('disabled', true);
	$('#btnSave').css('visibility', 'hidden');
	$('#btnAddFile').css('visibility', 'hidden');
	$('#btnReq').css('visibility', 'hidden');
	
}

function jobCall() {
		
	if (cboEditor_dp.length == 0) {					
		getSelectList();
	} else {
		cboEditor_click();
	}
}

function getSelectList() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.SubId = strIsrSub;
	obj.UserId = "";
	obj.ReqCd = "47";
	
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
			cc_edgbncd : value.cc_edgbncd,
			cc_jobacpt : value.cc_jobacpt,
			cc_jobedday : value.cc_jobedday,
			cc_jobetc : value.cc_jobetc,
			cc_jobstday : value.cc_jobstday,
			cc_seqno : value.cc_seqno,
			cc_status : value.cc_status,
			cc_substatus : value.cc_substatus,
			creatdt : value.creatdt,
			devedday : value.devedday,
			devmm : value.devmm,
			devstday : value.devstday,
			devtime : value.devtime,
			eddate : value.eddate,
			edreqdt : value.edreqdt,
			lastdt : value.lastdt,
			monterm : value.monterm,
			pgmcnt : value.pgmcnt,
			pgmsw : value.pgmsw,
			prcdate : value.prcdate,
			realday : value.realday,
			realmm : value.realmm
		});
	});

	$('[data-ax5select="cboEditor"]').ax5select({
		options : options
	});
	
	if(cboEditor_dp.length > 0){
		for (var i = 0; cboEditor_dp.length > i; i++) {
			if (strAcptNo != "" && strAcptNo != "" && popSw == true) {
				if (cboEditor_dp[i].cc_jobacpt == strAcptNo) {
					$('[data-ax5select="cboEditor"]').ax5select("setValue", cboEditor_dp[i].cc_scmuser, true);
					break;
				}
			} else {
				if (cboEditor_dp[i].cc_scmuser == strUserId) {
					$('[data-ax5select="cboEditor"]').ax5select("setValue", cboEditor_dp[i].cc_scmuser, true);						
					break;
				}
			}
		}
		cboEditor_click();
	}
	if(popSw == true) {
		$('[data-ax5select="cboEditor"]').ax5select('enable');
	} else {
		$('[data-ax5select="cboEditor"]').ax5select('disable');
	}
}

function cboEditor_click() {
	screenInit("S");
	if (getSelectedIndex('cboEditor') < 0) { 
		return;
	}

	getJobPlanInfo();
	getDocList();
	tmpObj = null;
}

//파일리스트 조회
function getDocList() {
	var data = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		ReqCd : "04",
		SubReq : "00",
		requestType : 'getDocList'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetDocList);
}

function successGetDocList(data) {
	if (data !== 'ERR') {
		grdFile_dp = data;
		grdFile.setData(grdFile_dp);
		gridEdit();
	}
}

function getJobPlanInfo() {
	var tmpObj = new Object();
	tmpObj.CC_ISRID = strIsrId;
	tmpObj.CC_ISRSUB = strIsrSub;
	tmpObj.CC_SCMUSER = getSelectedVal('cboEditor').value;
	tmpObj.REQCD = strReqCd;

	var data = new Object();
	data.objData = tmpObj;
	data.requestType = 'getJobPlanInfo';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetJobPlanInfo);
}

function successGetJobPlanInfo(data) {
	jobPlanInfo = data;

	if (jobPlanInfo.length > 0){
		var i = 0;
		var strJobSta = "";
		
		if (jobPlanInfo[0].CC_SCMUSER == "JOBSTA") {
			strJobSta = jobPlanInfo[0].jobsta;
			jobPlanInfo.splice(0, 1);
		}
		if (jobPlanInfo.length > 0) {
			$('#txtJOBRECORD').attr('disabled', true);
			if (strJobSta == "CA" || strJobSta == "CB"){
				$('#txtJOBRECORD').attr('disabled', false);
			}
			chkStatus('cboJOBGBN', 'enabled');
			chkStatus('cboMONTERM', 'enabled');
			chkStatus('dfENDRPTDAY', 'enabled');
			chkStatus('dfJOBDATE', 'enabled');
			chkStatus('txtJOBTIME', 'enabled');
			chkStatus('hourTxt', 'enabled');
			chkStatus('minTxt', 'enabled');

			if($('#txtJOBRECORD').prop('disabled')){
				chkStatus('txtJOBDETAIL', 'editable');
				$('#txtJOBDETAIL').css("background-color", '#EFEFF7');
			}
			chkStatus('txtREFMSG', 'enabled');
			
			$('#txtJOBRECORD').val(jobPlanInfo[0].CC_JOBTITLE);
			for (i = 0 ; i < cboJOBGBN_dp.length; i++){
				if (cboJOBGBN_dp[i].cm_micode == jobPlanInfo[0].CC_JOBGBN){
					$('[data-ax5select="cboJOBGBN"]').ax5select("setValue", cboJOBGBN_dp[i].cm_micode, true);
					break;
				}
			}
			for (i = 0 ; i < cboMONTERM_dp.length; i++){
				if (cboMONTERM_dp[i].cm_micode == jobPlanInfo[0].CC_MONTERM){
					$('[data-ax5select="cboMONTERM"]').ax5select("setValue", cboMONTERM_dp[i].cm_micode, true);
					break;
				}
			}
			if (!$('#dfENDRPTDAY').prop('disabled')){
				$('#dfENDRPTDAY').css('display', '');
				$('#txtENDRPTDAY').val("");
				$('#txtENDRPTDAY').css('display', 'none');
			}else{
				$('#txtENDRPTDAY').css('display', '');
				$('#dfENDRPTDAY').val("");
				$('#dfENDRPTDAY').css('display', 'none');
			}
			$('#dfENDRPTDAY').val(jobPlanInfo[0].CC_ENDRPTDAY);
			$('#txtENDRPTDAY').val(jobPlanInfo[0].CC_ENDRPTDAY);

			if (!$('#dfJOBDATE').prop('disabled')){
				$('#dfJOBDATE').css('display', '');
				$('#txtJOBDATE').val("");
				$('#txtJOBDATE').css('display', 'none');
			}else{
				$('#txtJOBDATE').css('display', '');
				$('#dfJOBDATE').val("");
				$('#dfJOBDATE').css('display', 'none');
			}
			if(jobPlanInfo[0].CC_JOBDATE == null){
				$('#dfJOBDATE').val("");
			}else{
				$('#dfJOBDATE').val(jobPlanInfo[0].CC_JOBDATE);
			}
			$('#txtJOBDATE').val(jobPlanInfo[0].CC_JOBDATE);
			$('#hourTxt').val(jobPlanInfo[0].CC_JOBTIME.substr(0,2))
			$('#minTxt').val(jobPlanInfo[0].CC_JOBTIME.substr(2,2))
				
			$('#txtJOBDETAIL').val(jobPlanInfo[0].CC_JOBDETAIL);
			$('#txtREFMSG').val(jobPlanInfo[0].CC_REFMSG);
			
			if (jobPlanInfo[0].CC_STATUS == "1") {
				var data = {
					AcptNo : jobPlanInfo[0].CC_ACPTNO,
					UserId : strUserId,
					requestType : 'gyulChk'
				}
				ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
						successGyulChk);
			}
		}
		if (strJobSta == "CA") {
			$('#btnReq').attr('disabled', false);
			$('#btnSave').attr('disabled', false);
			$('#btnReq').css('visibility', 'visible');
			$('#btnSave').css('visibility', 'visible');
		} else if (strJobSta == "CB") {
			$('#btnSave').attr('disabled', false);
			$('#btnSave').css('visibility', 'visible');
		} else if (strJobSta == "CC") {
			$('#btnConf').attr('disabled', false);
			$('#btnConf').css('visibility', 'visible');
			strAcptNo = jobPlanInfo[0].CC_ACPTNO;
		}
		
		$('#txtJOBRECORD').attr('disabled', true);
		if (strJobSta == "CA" || strJobSta == "CB"){
			$('#txtJOBRECORD').attr('disabled', false);
		}
		chkStatus('cboJOBGBN', 'enabled');
		chkStatus('cboMONTERM', 'enabled');
		chkStatus('dfENDRPTDAY', 'visible');
		chkStatus('dfENDRPTDAY', 'enabled');
		chkStatus('txtENDRPTDAY', 'enabled');
		chkStatus('dfJOBDATE', 'visible');
		chkStatus('dfJOBDATE', 'enabled');
		chkStatus('txtJOBDATE', 'enabled');
		chkStatus('txtJOBTIME', 'enabled');
		chkStatus('hourTxt', 'enabled');
		chkStatus('minTxt', 'enabled');

		if($('#txtJOBRECORD').prop('disabled')){
			chkStatus('txtJOBDETAIL', 'editable');
			$('#txtJOBDETAIL').css("background-color", '#EFEFF7');
		}
		chkStatus('txtREFMSG', 'enabled');
	}else{
		$('#txtENDRPTDAY').val("");
		$('#txtJOBDATE').val("");
		$('#txtJOBDETAIL').val("");
		$('#txtREFMSG').val("");
		strAcptNo = "";
	}
	chkStatus('btnAddFile', 'enabled');
	chkStatus('btnAddFile', 'visible');
}

function chkStatus(lId, type) {
	var state = $('#txtJOBRECORD').prop('disabled');
	if(state == true) {
		if(type == 'enabled') {
			if(lId.indexOf('cbo') != -1) {
				$('[data-ax5select='+ lId +']').ax5select('disable');
			}
			$('#' + lId).attr('disabled', true);
		} else if(type == 'editable') {
			$('#' + lId).attr('readonly', true);
		} else if(type == 'visible') {
			if(lId === 'txtENDRPTDAY' || lId === 'txtJOBDATE') {
				$('#' + lId).css('visibility', 'visible');
			} else {
				$('#' + lId).css('visibility', 'hidden');
			}
		}
	} else {
		if(type == 'enabled') {
			if(lId.indexOf('cbo') != -1) {
				$('[data-ax5select='+ lId +']').ax5select('enable');
			}
			$('#' + lId).attr('disabled', false);
		} else if(type == 'editable') {
			$('#' + lId).attr('readonly', false);
		} else if(type == 'visible') {
			if(lId === 'txtENDRPTDAY' || lId === 'txtJOBDATE') {
				$('#' + lId).css('visibility', 'hidden');
			} else {
				$('#' + lId).css('visibility', 'visible');
			}
		}
	}
}
//결재정보체크
function successGyulChk(data) {
	if (data == "0") {
		$('#btnOk').attr('disabled', false);
		$('#btnCncl').attr('disabled', false);
		$('#btnOk').css('visibility', 'visible');
		$('#btnCncl').css('visibility', 'visible');
		
		$('#txtConf').css('display', '');
		$('#lblConf').css('display', '');
		$('#txtConf').attr('readonly', false);
	} else if (data != "1" && data != "2") {
		dialog.alert("결재정보 체크 중 오류가 발생하였습니다.");
	} 
}
//탭 선택시 호출함수
function jobCall() {
	if (cboEditor_dp.length == 0) {					
		getSelectList();
	} else {
		cboEditor_click();
	}
}

function fileOpen() {}
//저장 버튼 클릭
function btnSave_Click() {
	if ($('#txtISRID').val() == "" && $('#txtISRID').val().length<14){
		dialog.alert("ISR정보가 없습니다. ISR정보를 선택하여 주십시오.");
		return;
	}
	
	$('#txtJOBRECORD').val($('#txtJOBRECORD').val().trim());//작업내역
	$('#txtJOBDETAIL').val($('#txtJOBDETAIL').val().trim());//작업내용
	$('#txtREFMSG').val($('#txtREFMSG').val().trim());//협조사항
	
	var tmpObj = new Object();
	tmpObj.CC_ISRID = $('#txtISRID').val().substr(1,12);
	tmpObj.CC_ISRSUB = $('#txtISRID').val().substr(14,2);
	tmpObj.CC_SCMUSER = strUserId;
	tmpObj.CC_JOBTITLE = $('#txtJOBRECORD').val();
	tmpObj.CC_JOBGBN = getSelectedVal('cboJOBGBN').cm_micode;
	tmpObj.CC_MONTERM = getSelectedVal('cboMONTERM').cm_micode;
	tmpObj.CC_ENDRPTDAY = replaceAllString($('#dfENDRPTDAY').val(),"/","");
	tmpObj.CC_JOBDETAIL = $('#txtJOBDETAIL').val();
	tmpObj.CC_REFMSG = $('#txtREFMSG').val();
	tmpObj.CC_JOBDATE = replaceAllString($('#dfJOBDATE').val(),"/","");
	tmpObj.CC_JOBTIME = $('#hourTxt').val() + $('#minTxt').val();
	tmpObj.CC_ACPTNO = "";
	tmpObj.savecd = "0";

	var data = new Object();
	data.objData = tmpObj;
	data.requestType = 'setJobPlanInfo';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successSetJobPlanInfo);
	
	tmpObj = null;	
}
//결재 버튼 클릭
function btnOk_click() {
	dialog.alert({
		msg: "결재처리하시겠습니까?",
		onStateChanged: function() {
			if(this.state === "close") {
				confOk(this.state);					
			}
		}
	});
}

function confOk(state) {
	if (state == "close") {
		$('#txtConf').val($('#txtConf').val().trim());
		nextConf_ISR(strAcptNo, strUserId, $('#txtConf').val(), "1", "47");
		resultMSG = "결재";
	}
}

function nextConf_ISR(AcptNo, UserId, conMsg, Cd, ReqCd) {
	var confObj = new Object();
	confObj.AcptNo = AcptNo;
	confObj.UserId = UserId;
	confObj.conMsg = conMsg;
	confObj.Cd = Cd;
	confObj.ReqCd = ReqCd;

	var grdInfoData = new Object();
	grdInfoData.grdData = confObj;
	grdInfoData.requestType = 'nextConf_ISR';
	ajaxAsync('/webPage/rfc/RFCRegister', grdInfoData, 'json', successNextConf_ISR);		
}

function successNextConf_ISR(data) {
	if (data == "0") {
		dialog.alert(resultMSG+"처리가  완료 되었습니다.");
		
		screenInit("M");
		jobCall();
	}else{
		dialog.alert(resultMSG+"처리에 실패하였습니다.");
	}	
}

//반려버튼 클릭
function btnCncl_click() {
	$('#txtConf').val($('#txtConf').val().trim());
	if($('#txtConf').val() == ""){
		 dialog.alert("반려의견을 입력하여 주십시오.");
		 return;
	}
	dialog.alert({
		msg: "반려처리하시겠습니까?",
		onStateChanged: function() {
			if(this.state === "close") {
				cnclChk(this.state);					
			}
		}
	});
}

function cnclChk(state) {
	if (state == 'close') {
		nextConf_ISR(strAcptNo, strUserId, $('#txtConf').val(), "3", "47");
		resultMSG = "반려";
	}
}

//결재정보 버튼 클릭
function btnConf_click() {

	var winName = "confdata";
	var f = document.popPam;   		
    
    f.user.value 	= userId;    	
	f.acptno.value	= strAcptNo;    	
    
	nHeight = 740;
    nWidth  = 1200;

	cURL = "/webPage/winpop/PopApprovalInfo.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

//결재상신 버튼 클릭
function btnReq_Click() {
	if ($('#txtJOBRECORD').val() == ""){
	 dialog.alert("[작업계획서]작업내역를 입력하여 주십시오.");
	 return;
	}
	if (getSelectedIndex('cboJOBGBN') < 0){
		dialog.alert("[작업계획서]작업구분을 선택하여 주십시오.");
		return;
	}
	if (replaceAllString($('#dfJOBDATE').val(),"/","") == ""){
		dialog.alert("[작업계획서]작업예정일 날짜를 선택하여 주십시오.");
		return;
	}

	if ($('#hourTxt').val() == ""){
		dialog.alert("[작업계획서]작업예정일 시간을 입력하여 주십시오.");
		return;
	}
	if ($('#minTxt').val() == ""){
		dialog.alert("[작업계획서]작업예정일 시간을 입력하여 주십시오.");
		return;
	}
	if (getSelectedIndex('cboMONTERM') < 0){
		dialog.alert("[작업계획서]모니터링기간을 선택하여 주십시오.");
		return;
		}
	if (replaceAllString($('#dfENDRPTDAY').val(),"/","") == ""){
		dialog.alert("[작업계획서]완료보고서제출예정일 선택하여 주십시오.");
		return;
	}
	$('#txtJOBDETAIL').val($('#txtJOBDETAIL').val().trim())//작업내용
	if ($('#txtJOBDETAIL').val() == ""){
		dialog.alert("[작업계획서]작업내용을 입력하여 주십시오.");
		return;
	}

	var confirmInfoData = new Object();
	confirmInfoData.SysCd = "99999";
	confirmInfoData.strRsrcCd = "47";
	confirmInfoData.ReqCd = "";
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
	if (retMsg == "C") {
		dialog.alert({
			msg: "결재자를 지정하시겠습니까??",
			onStateChanged: function() {
				if(this.state === "close") {
					confChk1(this.state);					
				}
			}
		});			
	} else if (retMsg == "Y") {	
		confCall("Y");
	} else if (retMsg != "N") {
		dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
	} else {
		confCall("N");
	}
}

function confChk1(state) {
	if (state == 'close') {
		confCall("Y");
	} else {
		confCall("N");
	}
}

function confCall(GbnCd) {
	confirm_dp = [];
	confirmInfoData = null;

	confirmInfoData = {		
		UserID : strUserId,
		SysCd  : "99999",
		ReqCD  : "47",
		Rsrccd : [],
		QryCd : [],
		EmgSw : "N",
		PrjNo  : strIsrId+strIsrSub,
		deployCd : "",
		JobCd : ""
	}

	if(GbnCd == "Y") {
			approvalModal.open({
	        width: 820,
	        height: 400,
	        iframe: {
	            method: "get",
	            url: "../../modal/request/ApprovalModal.jsp",
	            param: "callBack=modalCallBack"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	if(confirmData.length > 0){
	            		reqQuest(confirmData, this.state);
	            	}
	                mask.close();
					ingSw = false;
	            }
	        }
		});
	} else {
		confirmInfoData = {		
			UserID : strUserId,
			SysCd  : "99999",
			ReqCD  : "47",
			Rsrccd : [],
			QryCd : [],
			EmgSw : "N",
			PrjNo  : strIsrId+strIsrSub,
			deployCd : "0",
			JobCd : ""
		}

		var tmpData = {
			confirmInfoData: 	confirmInfoData,
			requestType: 	'Confirm_Info'
		}
		ajaxAsync('/webPage/request/ConfirmServlet', tmpData, 'json', successConfirm_Info);
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

function successSetJobPlanInfo(data) {
	if (data == "0") {
		dialog.alert("작업계획서 저장이 완료되었습니다.");
		
		screenInit("M");
		jobCall();
	} else {
		dialog.alert("작업계획서 저장 중 오류가 발생하셨습니다.");
	}
}

function reqQuest(data, state) {
	if(state == 'close') {
		confirm_dp = data;
		reqQuest_Conf();
	}
}

function reqQuest_Conf() {
	$('#txtJOBRECORD').val($('#txtJOBRECORD').val().trim());//작업내역
	$('#txtJOBDETAIL').val($('#txtJOBDETAIL').val().trim());//작업내용
	$('#txtREFMSG').val($('#txtREFMSG').val().trim());//협조사항
	
	var etcObj = new Object();
	etcObj.CC_ISRID = $('#txtISRID').val().substr(1,12);
	etcObj.CC_ISRSUB = $('#txtISRID').val().substr(14,2);
	etcObj.CC_SCMUSER = strUserId;
	etcObj.CC_JOBTITLE = $('#txtJOBRECORD').val();
	etcObj.CC_JOBGBN = cboJOBGBN.selectedItem.cm_micode;
	etcObj.CC_MONTERM = cboMONTERM.selectedItem.cm_micode;
	etcObj.CC_ENDRPTDAY = replaceAllString($('#dfENDRPTDAY').val(),"/","");
	etcObj.CC_JOBDETAIL = $('#txtJOBDETAIL').val();
	etcObj.CC_REFMSG = $('#txtREFMSG').val();
	etcObj.CC_JOBDATE = replaceAllString($('#dfJOBDATE').val(),"/","");
	etcObj.CC_JOBTIME = $('#hourTxt').val() + $('#minTxt').val();
	etcObj.CC_ACPTNO = "";
	etcObj.savecd = "0";
	etcObj.reqcd = "47";

	var confirmInfoData = {
		etcObj : etcObj,
		confList : confirm_dp,
		requestType : 'setPlanConfirm'
	}	

	ajaxAsync('/webPage/rfc/RFCRegister', confirmInfoData, 'json',
			successSetPlanConfirm);		
 
 	etcObj = null;	
	confirmInfoData = null;
}

function successSetPlanConfirm(data) {
	if (data == "0") {
		dialog.alert("작업계획서 결재상신이 정상적으로 처리되었습니다.");
		
		screenInit("M");
		jobCall();				
	} else {
		dialog.alert("작업계획서 결재상신 중 오류가 발생하였습니다.\n"+data);
	}	
}

function getISRInfo_resultHandler(data){
	   		
	isrinfo_dp.source = data;
	if (isrinfo_dp.length==0){
		dialog.alert("ISR 정보가 없습니다. 관리자에게 문의해주세요.");
		return;
	}
	$('#txtISRID').val(isrinfo_dp[0].ISRIDTITLE);
	$('#txtDOCNO').val(isrinfo_dp[0].CC_DOCNO);
	$('#txtREQDT').val(isrinfo_dp[0].CC_REQENDDT);
	$('#txtREQDEPT').val(isrinfo_dp[0].CM_DEPTNAME);
	$('#txtEDITOR').val(isrinfo_dp[0].CM_USERNAME);
	$('#txtSTATUS').val(isrinfo_dp[0].STATUS);
	$('#txtPROSTATUS').val(isrinfo_dp[0].SUBSTATUS);
	screenInit("M");
	jobCall();
}
