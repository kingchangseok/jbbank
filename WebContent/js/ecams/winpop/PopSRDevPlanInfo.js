/**
 * SR정보 winPop
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2019-07-19
 */


var adminYN 		= null;
var Cnchk 		= "N";

var confirmInfoData = {};
var confirmData = [];

// 첨부파일 관련 변수
var upFiles = null;
var fileObj = null;
var docList = null;
var fileUploadModal 		= new ax5.ui.modal();
var approvalModal 		= new ax5.ui.modal();

//public
var strIsrId  = "";
var strSrTitle  = "";
var strQryGbn = "";
var strWorkD  = "";
var strAcptNo = "";
var strStatus = "";
var strIsrTitle = "";
var reloadVal = "N";
var gyulMessage = "";
var reqList = [];

var prjListData = [];

var loadSw = false;
var loadSw2 = false;
var loadSw3 = false;
var inter = null;
var inter2 = null;
var inter3 = null;
var strSysCd = null;
var gridSw = false;
var frmLoad = [false,false,false];
var inter = [null, null, null]; 

var codeList = null;
var f = document.getReqData;

var codeList = codeListJsonParse(f.codeList.value);

var strIsrId = f.SRId.value;
var strAcptNo = f.AcptNo.value; 
var userId = f.UserId.value;

var closeCk = false;
getSession();

$(document).ready(function() {
	
	strIsrId = $('#SRId').val();
	strAcptNo = $('#AcptNo').val(); 
	userId = $('#UserId').val();
	

	if (userId == null || userId == '') {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	if (strIsrId == null || strIsrId == '') {
		dialog.alert('SR-ID가 없습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}

	$('#frmSRRegister').get(0).contentWindow.strReqCd = "00";
	$('#frmSRReceive').get(0).contentWindow.strReqCd = "00";
	$('#frmDevPlan').get(0).contentWindow.strReqCd = "00";
	
	document.getElementById('frmSRRegister').onload = function() {
	    loadSw = true;
	}
	
	document.getElementById('frmSRReceive').onload = function() {
	    loadSw2 = true;
	}
	
	document.getElementById('frmDevPlan').onload = function() {
	    loadSw3 = true;
	}

	var data = {
			userId : userId,
			requestType	: 'checkAdmin'
	}
	adminYN = ajaxCallWithJson('/webPage/common/CommonUserInfo', data, 'json');
	
	if(adminYN){
		adminYN = "Y";
	} else {
		adminYN = "N";
	}
	
	var data = {
			userId: 	userId,
			acptno: 	strAcptNo,
			requestType: 	'getReqList'
		}
		
	ajaxAsync('/webPage/sr/SRStatus', data, 'json',successGetReqList);
	
	$("#masterCncl").bind("click", function(){
		masterCncl_Click();
	});
	
	$("#cmdOk").bind("click", function(){
		cmdOk_Click();
	});
	
	$("#cmdCncl").bind("click", function(){
		cmdCncl_Click();
	});
	
	$("#btnClose").bind("click", function(){
		/*
		if (window.opener.getRequestList != undefined){
			//window.opener.getRequestList();
		}
		*/
		closeCk = true;
		window.close();
	});
	
	$("#btnApprovalInfo").bind("click", function(){
		openApprovalInfo();
	});

	$(".tab_wrap ul li").removeClass("disabled");
	$("ul.tabs li").click(function () {
		if(strIsrId == null || strIsrId == "" || strIsrId == undefined){
			frmLoad[$(this).index()] = false;
		}
		
		if($(this).hasClass("disabled")) {
			return;
		}

		var activeTab = $(this).attr("rel");
		//이미 on상태면 pass
		if(!$(this).hasClass("on")) {
			//tab메뉴 클릭에 따라 색상 변경
			$(".tab_content").hide();
			$(".btnTab").removeClass('on');
			$(this).addClass("on");
			$("#tab" + activeTab).show();
		}
		openFrm(activeTab, $(this).index());
	});
});

function openFrm(rel, index){
	if(!frmLoad[index]){
		$("#frm" + rel).prop("src","/webPage/tab/sr/"+rel+"Tab.jsp");
		document.getElementById('frm' + rel).onload = function() {
			$(this).get(0).contentWindow.createViewGrid();
			frmLoad[index] = true;
		}
	}
	
	if(inter[index] == null){
		inter[index] = setInterval(function(){
			if(frmLoad[index]) {
				clearInterval(inter[index]);
				inter[index] = null;
				changeTabMenu();
			}
		},100);
	}
}

function successGetReqList(data){

	if(data.length > 0){
		if (data[0].endsw == "0") {
			reqList = data;

			if (adminYN){
				$("#masterCncl").prop("disabled",false);
				$("#confBox").show();
			}
			
			gyulChk();
		}
	}
	strIsrId = data[0].cr_srid;
	strSrTitle = data[0].cr_itsmtitle;
	
	var data = {
			srId: 	strIsrId,
			requestType: 	'getSRSysInfo'
	}
	
	ajaxAsync('/webPage/sr/SRStatus', data, 'json',successGetSRSysInfo);
}

function successGetSRSysInfo(data){
	if(null != data && data.length > 0){
		strSysCd = data[0].syscd;
		openFrm($(".btnTab.on").attr("rel"), $(".btnTab.on").index());
	} else {
		dialog.alert("조회할 SR이 없습니다.");
		return;
	}
}

function gyulChk(){

	var ajaxReturnData = null;
	var data = {
			acptNo: strAcptNo,
			userId : userId,
			requestType	: 'gyulChk'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/request/ConfirmServlet', data, 'json');
	
	$("#cmdOk").prop("disabled",true);
	$("#cmdCncl").prop("disabled",true);
	$("#masterCncl").prop("disabled",true);
    
	if (ajaxReturnData == "0") {
		$("#cmdOk").prop("disabled",false);
		if (reqList.length > 0 && reqList[0].prcsw == "0") {
			$("#cmdCncl").prop("disabled",false);
		} else {
			$("#cmdCncl").prop("disabled",true);
		}
		$("#confBox").show();
	}else if (ajaxReturnData != "1") {
		dialog.alert("결재정보 체크 중 오류가 발생하였습니다.");
		return;
	}
}

function initScreen() {
	
}

//tabnavi_click
function changeTabMenu() {
	if(!frmLoad[$(".btnTab.on").index()]){
		openFrm($(".btnTab.on").attr("rel"), $(".btnTab.on").index());
		return;
	}
	
	var tmpTab = null;
	
	if(document.getElementById("tab1").className.indexOf("on") > -1) { 
		//SR정보
		tmpTab = $('#frmSRRegister').get(0).contentWindow;
		
		tmpTab.userId = userId;
		tmpTab.strIsrId = strIsrId;

		if(strIsrId != null && strIsrId != "") {
			tmpTab.strReqCd = "61";
			//tmpTab.elementInit("M"); //tab1.screenInit("M");
			
			var loadCk = setInterval(function(){
				if(undefined != tmpTab.cboPGMUserData && tmpTab.cboPGMUserData.length > 0){
					clearInterval(loadCk);
					tmpTab.firstGridClick(strIsrId);
				}
			},100);
		}
	}else if(document.getElementById("tab2").className.indexOf("on") > -1) { 
		//SR접수
		tmpTab = $('#frmSRReceive').get(0).contentWindow;
		
		tmpTab.userId = userId;
		tmpTab.strIsrId = strIsrId;
		
		if(strIsrId != null && strIsrId != "") {
			var loadCk = setInterval(function(){
				if(tmpTab.cboGradeData.length > 0){
					clearInterval(loadCk);
					tmpTab.strReqCd = "62"; 
					tmpTab.getSRInfo(); 
				}
			},100);
		}
	}else if(document.getElementById("tab3").className.indexOf("on") > -1) { 
		//개발계획
		var tmpTab = $('#frmDevPlan').get(0).contentWindow;

		tmpTab.userId = userId;
		tmpTab.AcptNo = strAcptNo;
		tmpTab.SrID = strIsrId;
		tmpTab.SrTitle = strSrTitle;
		tmpTab.SysCd = strSysCd;
		
		//tmpTab.screenInit();
		if(strIsrId != null && strIsrId != "") {
			tmpTab.strReqCd = "63"; 
			tmpTab.setSRInfo_detail_func();
		}
		
	}
}


function masterCncl_Click(){
	gyulMessage = "관리자 회수";
	
	if($("#txtConMsg").val().trim() == ""){
    	 dialog.alert("관리자 회수 의견을 입력하여 주십시오.");
    	 $("#txtConMsg").focus();
    	 return;
    }
	confirmDialog.confirm({
		title: '확인',
		msg: '관리자 회수 하시겠습니까?',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			nextConf("9", "32");
		}
	});
}

function cmdOk_Click(){
	gyulMessage = "결재";
	
	confirmDialog.confirm({
		title: '확인',
		msg: '결재 하시겠습니까?',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			nextConf("1", "32");
		}
	});
}

function cmdCncl_Click(){
	if($("#txtConMsg").val().trim() == ""){
		dialog.alert("반려의견을 입력하여 주십시오.");
		$("#txtConMsg").focus();
    	 return;
    }
	gyulMessage = "반려";
	
	confirmDialog.confirm({
		title: '확인',
		msg: '반려 하시겠습니까?',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			nextConf("3", "32");
		}
	});
}

function nextConf(cd, reqCd){
	var ajaxReturnData = null;
	var confirmInfoData = new Object();
	
	confirmInfoData = {
			acptNo : strAcptNo,
			userId : userId,
			conMsg : $("#txtConMsg").val().trim(),
			reqCd : reqCd,
			cd : cd
	}
	
	var data = {
			confirmInfoData: confirmInfoData,
			requestType	: 'nextConf'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/request/ConfirmServlet', data, 'json');
	
	if (ajaxReturnData == "0") {
		dialog.alert(gyulMessage+" 처리가  완료 되었습니다.");

		$("#confBox").hide();
		$("#masterCncl").prop("disabled",true);
		
		gyulChk();
	}else{
		dialog.alert(gyulMessage+" 처리에 실패하였습니다.");
	}
}

function openApprovalInfo() {
	var nHeight, nWidth, cURL, winName;
	var type = "01";
	var reqCd = "00";
	
	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.getReqData;   		//폼 name
    
	nHeight = 828;
    nWidth  = 1046;

	cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	
	
	//console.log('+++++++++++++++++'+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

//열려있는 ax5select 닫기
function clearSelect(frame){
	// 프레임들을 전부 찾는다
	var frameList = [];
	frameList.push($('#iFrm'));
	if($('#iFrm').contents().find("iframe").length > 0){
		frameList = $.extend({},frameList, $('#iFrm').contents().find("iframe"));
	}
	for(var i=0; i<frameList.length; i++){
		//프레임중 ax5grid 셀렉트가 열려있는 상태인 값을 찾는다
		var frameSelecte = $(frameList[i]).contents().find("[data-select-option-group-opened='true']");
		if(frameSelecte.length > 0){
			// 열려있는 상태가 있다면 그 프레임의 body에 포커스를 주기위해 클릭을 해 준다
			$(frameList[i]).contents().find("body").click();
		}
	}
}

function openApprovalModal(){
	var tmpTab = $('#frmDevPlan').get(0).contentWindow;
	setTimeout(function() {
		approvalModal.open({
			width: 820,
			height: 600,
			iframe: {
				method: "get",
				url: "../modal/request/ApprovalModal.jsp",
				param: "callBack=modalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					if(confirmData.length > 0){
						tmpTab.confirmData = confirmData;
						tmpTab.reqQuest();
					}
					mask.close();
				}
			}
		});
	}, 200);
}

function getPrjSelectIndex(){
	return 1;
}

function fileupload(){
	setTimeout(function() {
		fileUploadModal.open({
			width: 685,
			height: 420,
			iframe: {
				method: "get",
				url: "../modal/fileupload/ComFileUpload.jsp",
				param: "callBack=modalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					if(fileObj.filegb == "00"){
						var tmpTab = $('#frmSRRegister').get(0).contentWindow;
						tmpTab.fileClear();
					} else if (fileObj.filegb == "21"){
						var tmpTab = $('#frmTestResult').get(0).contentWindow;
						tmpTab.fileClear();
					} else if (fileObj.filegb == "03"){
						var tmpTab = $('#frmWorkRegister').get(0).contentWindow;
						tmpTab.fileClear();
					} else {
						var tmpTab = $('#frmDevPlan').get(0).contentWindow;
						tmpTab.fileClear();
					}
					
					mask.close();
				}
			}
		});
	}, 200);
}

function backSRRegister(){
	mask.open();
	dialog.alert("선택하신 SR은 필수 정보가 없습니다. \nSR정보/등록 화면에서 필수 정보를\n입력해 주세요.", function(){
		$("ul.tabs li").removeClass('on');
		$(".tab_content").hide();
		$("#tab1").addClass("on");
		$("#tabSRRegister").show();
		changeTabMenu();
		mask.close();
	});
} 

function getPrjList(newCk){
	if(newCk){
		return;
	}
	openFrm($(".btnTab.on").attr("rel"), $(".btnTab.on").index());
}

//세션 가져오기
function getSession() {
	var data = new Object();
	data = {
		requestType : 'GETSESSIONUSERDATA'
	}
	ajaxAsync('/webPage/main/eCAMSBaseServlet', data, 'json',successGetSession);
}

//세션 가져오기 완료
function successGetSession(data) {
	userName		= data.userName;
	userId 			= data.userId;
	adminYN 		= data.adminYN === 'true' ? true : false;
	userDeptCd 		= data.deptCd;
	userDeptName	= data.deptName;

	if(userId == undefined || userId == 'undefined') {
		dialog.alert('로그인정보가 유효하지않습니다.\n다시 로그인하시기 바랍니다.', function() {
			window.location.replace('/webPage/login/ecamsLogin.jsp');
			return;
		});
	}
	
	Cnchk = getRGTCHK(); 
}

//SR 취소 권한 체크
function getRGTCHK(){
 
	var data = {
			userId : userId,
			rgtCd : "CN",
		requestType : 'getRGTCHK'
	}

	return ajaxCallWithJson('/webPage/common/CommonUserInfo',data, 'json');	
}