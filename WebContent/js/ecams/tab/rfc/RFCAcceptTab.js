/** RFC 접수 Tab 화면 정의
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

var grdFile = new ax5.ui.grid();
var grdCHGUSER = new ax5.ui.grid();

var fileUpPop = null;		
var cboCHGTYPE_dp = [];
var cboCHGUSER_dp = [];
var _refAddFiles = "";
var grdFile_dp = [];
var grdCHGUSER_dp = [];

var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var strSubStatus = "";
var scmSw = false;
var popSw = false;
var recvSw = false;
var strUpURL = "";
var attPath = "";
var tmpPath = "";

$('[data-ax5select="cboCHGTYPE"]').ax5select({
	options : []
});

$('[data-ax5select="cboCHGUSER"]').ax5select({
	options : []
});

$(document).ready(function() {
	// 추가
	$('#btnAddCHGUSER').bind('click', function() {
		btnAddCHGUSER_click();
	});
	// 파일첨부
	$('#btnAddFile').bind('click', function() {
		fileOpen();
	});
	// 등록
	$('#btnReq').bind('click', function() {
		btnReq_click();
	});		

	geteCAMSDir();
	getCodeInfo();
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

grdCHGUSER.setConfig({
	target : $('[data-ax5grid="grdCHGUSER"]'),
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
		},
		onDBLClick: function () {
			//grdCHGUSER.removeRow(this.dindex);
			grdCHGUSERDBClick(this.dindex);
		}		
	},
	columns : [ {
		key : "cm_deptname",
		label : "소속팀",
		width : '40%',
		align : 'center'
	}, {
		key : "cm_username",
		label : "담당자",
		width : '40%',
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
	
	grdCHGUSER.addColumn({
		key : "test",
		label : "test",
		width : '20%',
		align : 'center'
	});
	var lastIndex = grdCHGUSER.columns.length -1
	grdCHGUSER.removeColumn(lastIndex);	
}

function screenInit(scrGbn) {
	grdCHGUSER.setData([]);
	grdFile.setData([]);
	$('#rfcPass').prop('checked', true);
	if (cboCHGTYPE_dp.length > 0) { 
		$('[data-ax5select="cboCHGTYPE"]').ax5select("setValue", "00", true);
	}
	$('#txtCHGUSER').val("");
	$('#txtCHGUSER').attr('readonly', true);
	$('[data-ax5select="cboCHGUSER"]').ax5select("disable");
	$('[data-ax5select="cboCHGTYPE"]').ax5select("disable");
	$('#btnAddCHGUSER').attr('disabled', true);
	$('#btnAddFile').attr('disabled', true);
	$('#txtSayu').attr('readonly', true);
	$('#btnReq').attr('disabled', true);
	scmSw = false;
	recvSw = false;
}

function geteCAMSDir() {
	var data = {
		pcode : "21,99,F2",
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

//공통코드 조회
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ new CodeInfo('CHGTYPE', 'SEL', 'n'), ]);

	cboCHGTYPE_dp = codeInfos.CHGTYPE;

	options = [];
	$.each(cboCHGTYPE_dp, function(i, value) {
		options.push({
			value : value.cm_micode,
			text : value.cm_codename
		});
	});

	$('[data-ax5select="cboCHGTYPE"]').ax5select({
		options : options
	});
}

function rfcRecvCall() {
	getRFCInfo();
	getDocList();
}
//rfc 리스트 조회
function getRFCInfo() {
	var data = {
		IsrId : strIsrId,
		SubId : strIsrSub,
		UserId : strUserId,
		requestType : 'getRFCInfo'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetRFCInfo);
}

function successGetRFCInfo(data) {
	var tmpArray = [];
	tmpArray = data;
	
	if (tmpArray.length > 0) {	 			
		if (cboCHGUSER_dp.length == 0) {
		   getHandlerList();
	    }
		if (tmpArray[0].secusw == "Y") { 
			scmSw = true;
		}
		if (tmpArray[0].cm_username != "" && tmpArray[0].cm_username != null) {
			getChgUser();
			$('#txtRecvUser').val(tmpArray[0].cm_username);
			$('#txtRecvDt').val(tmpArray[0].recvdt);	 				
			$('#txtRecvGbn').val(tmpArray[0].recvgbn);
			$('#txtSayu').val(tmpArray[0].cc_recvmsg);
			if (tmpArray[0].cc_rfcrecvcd == "Y") {
				$('#rfcPass').prop('checked', true);
			} else {
				$('#rfcCanc').prop('checked', true);
			} 
			$("#hbxWeb").fadeTo(0, 0.5);
			$('#hbxWeb').css('pointer-events', 'none');				
			
			for (var i = 0; cboCHGTYPE_dp.length > i; i++) {
				if (cboCHGTYPE_dp[i].cm_micode == tmpArray[0].cc_chgtype) {
					$('[data-ax5select="cboCHGTYPE"]').ax5select("setValue", cboCHGTYPE_dp[i].cm_micode, true);
					break;
				}
			}				
		} else if (scmSw == true) {
			$('#rfcPass').attr('disabled', false);
			$('#rfcCanc').attr('disabled', false);
			$("#hbxWeb").fadeTo(0, 1);
			$('#hbxWeb').css('pointer-events', 'visible');							
			$('#txtSayu').attr('readonly', false);
			$('[data-ax5select="cboCHGTYPE"]').ax5select("enable");			
			$('#btnAddFile').attr('disabled', false);
			if (scmSw == true) { 
				recvSw = true;			
			}
		} 
		if (scmSw == true) {
			$('#txtCHGUSER').attr('readonly', false);
			$('#txtCHGUSER').attr('disabled', false);
			$('[data-ax5select="cboCHGUSER"]').ax5select("enable");			
			$('#btnAddCHGUSER').attr('disabled', false);	
			$('#btnReq').attr('disabled', false);
		}
	}	
}

//파일리스트 조회
function getDocList() {
	var data = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		ReqCd : "41",
		SubReq : "00",
		requestType : 'getDocList'
	}
	ajaxAsync('/webPage/srcommon/SRRegister', data, 'json',
			successGetDocList);
}

function successGetDocList(data) {
	if (data !== 'ERR') {
		grdFile_dp = clone(data);
		grdFile.setData(grdFile_dp);
	}
}

function getHandlerList() {
	var data = {
		UserId : strUserId,
		ReqCd : "41",
		requestType : 'getHandlerList'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetHandlerList);
}

function successGetHandlerList(data) {
	cboCHGUSER_dp = data;

	options = [];
	$.each(cboCHGUSER_dp, function(i, value) {
		options.push({
			value : value.cm_userid,
			text : value.userid,
			cm_deptcd : value.cm_deptcd,
			cm_deptname : value.cm_deptname,
			cm_username : value.cm_username
		});
	});

	$('[data-ax5select="cboCHGUSER"]').ax5select({
		options : options
	});
}

function getChgUser() {
	var data = {
		IsrId : strIsrId,
		SubId : strIsrSub,
		requestType : 'getChgUser'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetChgUser);
}

function successGetChgUser(data) {
	if (data !== 'ERR') {
		grdCHGUSER_dp = clone(data);
		grdCHGUSER.setData(grdCHGUSER_dp);
	}
}
//변경담당자 콤보박스 변경
$('#cboCHGUSER').bind('change', function() {
	$('#txtCHGUSER').val("");
	if(getSelectedIndex('cboCHGUSER') > 0) {
		$('#txtCHGUSER').val(getSelectedVal('cboCHGUSER').cm_username)
	}
});
//변경담당자 텍스트 입력
$('#txtCHGUSER').bind('change', function() {
	$('#txtCHGUSER').val().trim();
	if($('#txtCHGUSER').val().length == 0) {
		return;
	}
	for (var i = 0; i < cboCHGUSER_dp.length; i++) {
		if (cboCHGUSER_dp[i].cm_userid == $('#txtCHGUSER').val() ||
			cboCHGUSER_dp[i].cm_username == $('#txtCHGUSER').val()) {
			$('[data-ax5select="cboCHGUSER"]').ax5select("setValue", cboCHGUSER_dp[i].cm_userid, true);	
			break;
		}
	}
});
//추가 버튼 클릭
function btnAddCHGUSER_click() {
	if (getSelectedIndex('cboCHGUSER') < 1){
		dialog.alert("추가할 담당자를 선택한 후 처리하시기 바랍니다.");
		return;
	}

	 for (var i = 0; i < grdCHGUSER.getList().length; i++) {
		 if (grdCHGUSER.getList()[i].cm_userid == getSelectedVal('cboCHGUSER').value) {
			 dialog.alert("이미 담당자로 지정된 사용자입니다.");
			 return;
		 }
	 }
	 
	var tmpObj = new Object();
	tmpObj.cm_deptct = getSelectedVal('cboCHGUSER').cm_deptcd;
	tmpObj.cm_deptname = getSelectedVal('cboCHGUSER').cm_deptname;
	tmpObj.cm_username = getSelectedVal('cboCHGUSER').cm_username;
	tmpObj.cm_userid = getSelectedVal('cboCHGUSER').value;
	grdCHGUSER_dp.push(tmpObj);
	grdCHGUSER.setData(grdCHGUSER_dp);
	//grdCHGUSER_dp.refresh();
	//grdCHGUSER_dp = [];
	tmpObj = null;
}
//변경담당자 grid dbclick
function grdCHGUSERDBClick() {
	if (scmSw == false) {
		return;
	}
			
	if (getSelectedVal('cboCHGUSER') != null){
		if (getSelectedVal('cboCHGUSER').pgmcnt != "0" && getSelectedVal('cboCHGUSER').pgmcnt != null) {
			dialog.alert("이미 체크아웃 또는 신규등록한 프로그램이 존재하여 삭제할 수 없습니다.");
			return;
		}
		if (getSelectedVal('cboCHGUSER').devcnt != "0" && getSelectedVal('cboCHGUSER').devcnt != null) {
			dialog.alert({
				msg: "개발계획작성 후 단위테스트진행 중 입니다. 삭제를 계속 진행할까요?",
				onStateChanged: function() {
					if(this.state === "close") {
						devUser_delete();					
					}
				}
			})			
			return;
		}
		 devUser_delete();
	}
}
//변경담당자 삭제
function devUser_delete(index) {
	grdCHGUSER.removeRow(index);
}
//등록 버튼 클릭
function btnReq_click() {
	if (recvSw == true) {
		if ($('#rfcCanc').prop('checked') == false && $('#rfcPass').prop('checked') == false) {
			dialog.alert("접수/반려 구분을 선택하여 주시기 바랍니다.");
			return;	
		}
		
		if($('#rfcCanc').prop('checked')){
			$('#txtSayu').val($('#txtSayu').val().trim())
			if($('#txtSayu').val().length == 0){
				dialog.alert("접수／반려의견를 입력하여 주시기 바랍니다.");
				return;
			}
		}
		if ($('#rfcPass').prop('checked') == true) {
			if(getSelectedIndex('cboCHGTYPE') == 0){
				dialog.alert("변경종류을 선택하여 주시기 바랍니다.");
				return;
			}
			if (grdCHGUSER.getList().length == 0){
				dialog.alert("변경담당자를 추가하여 주시기 바랍니다.");
				return;
			}
		}
		
	} else {
		if (grdCHGUSER.getList().length == 0){
			dialog.alert("변경담당자를 추가하여 주시기 바랍니다.");
			return;
		}
	}
	
	var etcObj = new Object();
	if (recvSw == true) {
		etcObj.recvgbn = "1";
		if ($('#rfcPass').prop('checked')){
			etcObj.CMC0210_STATUS = "21";//9:접수
			etcObj.CC_CHGTYPE = getSelectedVal('cboCHGTYPE').value;
		}else {
			etcObj.CMC0210_STATUS = "22";//0:미접수
		}
		if($('#rfcCanc').prop('checked')){
			 etcObj.CC_RECVMSG = $('#txtSayu').val();
		   }
		 etcObj.CC_CHGUSER = strUserId;     		
	  } else {
		  etcObj.recvgbn = "9";
	  }
	 var strSCMUSER = "";

	 for (i=0 ; i < grdCHGUSER.getList().length ; i++){
		 if(strSCMUSER.length == 0){
			 strSCMUSER = grdCHGUSER.getList()[i].cm_userid
		 }else{
			 strSCMUSER = strSCMUSER + "," + grdCHGUSER.getList()[i].cm_userid;
		 }
	 }
	 etcObj.CC_ISRID = strIsrId;
	 etcObj.CC_ISRSUB = strIsrSub;
	 etcObj.CC_SCMUSER = strSCMUSER;
	 etcObj.CC_CHGUSER = strUserId;
	 
	var data = {
		etcObj : etcObj,
		requestType : 'setAcceptRFCInfo'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successSetAcceptRFCInfo);	
	
	etcObj = null;
}

function successSetAcceptRFCInfo(data) {
	$('#btnReq').attr('disabled', true);
	if (data != "0"){
		dialog.alert($('#btnReq').text() + "을 실패하였습니다.\n" + data);
		return;
	}
	dialog.alert({
		msg: $('#btnReq').text() + "을 완료하였습니다.",
	}, function() {
		if(this.key === 'ok') {
			var findSw = false;
			if (grdFile.getList().length > 0) {
				for (var i = 0 ; i < grdFile.getList().length; i++) {
					if (grdFile.getList()[i].cc_seqno == null || grdFile.getList()[i].cc_seqno == "") {
						findSw = true;
						break;
					}
				}					
			} 
			if (findSw == true) {
				fileOpen();
			} else {
				screenInit();
			}	
		}
		window.parent.getISRList();
	})	
}
//파일 첨부
function fileOpen() {}
