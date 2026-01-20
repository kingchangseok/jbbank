/**
 * [공통 > 결재정보] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-10
 * 
 */
var pUserId = null;
var pReqNo 	= null;
var pReqCd  = null;
var adminYN = null;
var rgtList = null;
var strLIB  = 'N';

var approvalGrid	= new ax5.ui.grid();

var approvalGridData= [];
var cboBlankData 	= [];
var cboSayuData 	= [];
var cboUserData 	= [];

var reqSta 		= null;
var strPassOk 	= null;
var HoliGbn 	= null;
var strEditor 	= null;
var strNxtSign 	= null;
var strAdmin	= null;
var permitStr	= null;

var f = document.getReqData;
pReqNo = f.acptNo.value;
pReqCd = pReqNo.substr(4,2);
pUserId = f.userId.value;
adminYN = f.adminYN.value;
rgtList = f.rgtList.value;

approvalGrid.setConfig({
    target: $('[data-ax5grid="approvalGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
    },
    paging : false,
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            clickApprovalGrid(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "confname", 	label: "결재단계명",  	width: '13%', align: "left"},
        {key: "teamuser",	label: "결재자",  	width: '13%'},
        {key: "teamcd", 	label: "구분",  		width: '10%'},
        {key: "confdate", 	label: "결재일시",  	width: '15%'},
        {key: "baseuser", 	label: "원결재자",  	width: '10%'},
        {key: "conmsg", 	label: "결재의견",  	width: '39%', align: "left"}
    ]
});

$('[data-ax5select="cboBlank"]').ax5select({
    options: []
});

$('[data-ax5select="cboSayu"]').ax5select({
	options: []
});

$('[data-ax5select="cboUser"]').ax5select({
	options: []
});

$(document).ready(function() {
	if (pReqNo == undefined || pReqNo == null || pReqNo == undefined || pReqNo.length != 12) {
		dialog.alert('입력정보가 정당하지 않습니다.');
		return;
	}	
	if (pUserId == undefined || pUserId == null || pUserId == undefined) {
		dialog.alert('로그인 후 다시 진행하시기 바랍니다.');
		return;
	}	
	$('#txtAcpt').val(pReqNo.substr(0,4) + '-' + pReqNo.substr(4,2) + '-' +pReqNo.substr(6,6));
	
	if (rgtList != null && rgtList != undefined && rgtList != '') {	
		var rgtArray = rgtList.split(',');
		for (var i=0; i<rgtArray.length; i++) {
			if (rgtArray[i] == 'Q1') {
				strLIB = 'Y';
			}
		}
	}
	
	screenInit();
	
	// 변경 후 결재 변경시
	$('#cboBlank').bind('change', function() {
		if(getSelectedVal('cboBlank').cm_micode === '3' || getSelectedVal('cboBlank').cm_micode === '7') {
			$('#divUser').css('visibility', 'visible');
			selectUser();
		} else {
			$('#divUser').css('visibility', 'hidden');
		}
	});
	
	// 수정 클릭
	$('#btnUpdate').bind('click', function() {
		updateProc();
	});
	
	// 닫기 클릭
	$('#btnClose').bind('click', function() {
		window.close();
	});
});

// 정보가져오기 및 안보일 부분 숨기기
function screenInit() {
	$('#lblSayu').css('visibility', 'hidden');
	$('#divSayu').css('visibility', 'hidden');
	$('#lblBlank').css('visibility', 'hidden');
	$('#divBlank').css('visibility', 'hidden');
	$('#btnUpdate').css('visibility', 'hidden');
	$('#divUser').css('visibility', 'hidden');
	
	selectConfirm();
	selectLocat();
}

// 결재자 수정
function updateProc() {
	var findSw 	= false;
	var selItem = approvalGrid.list[approvalGrid.selectedDataIndexs[0]];
	if(getSelectedIndex('cboBlank') < 1 ) {
		dialog.alert('변경후결재를 선택하여 주십시오.', function() {});
		return;
	}
	
	if(getSelectedIndex('cboSayu') < 1 ) {
		if(getSelectedVal('cboBlank').cm_micode != '7') {
			dialog.alert('사유구분을  선택하여 주십시오.', function() {});
			return;
		}
	}
	
	var DaeUser = '';
	if(getSelectedVal('cboBlank').value === '3' || getSelectedVal('cboBlank').value === '7') {
		if(getSelectedIndex('cboUser') < 1) {
			dialog.alert('대결재자를 선택하여 주십시오.', function() {});
			return;
		}
		
		if(getSelectedVal('cboUser').userid === selItem.team2) {
			dialog.alert('현재결재자와 대결재자가 동일인입니다.', function() {});
			return;
		}
		
		DaeUser = getSelectedVal('cboUser').userid;
	}

	var data = new Object();
	data = {
		AcptNo	: pReqNo,
		Locat	: selItem.locat,
		BlankCd	: getSelectedVal('cboBlank').value,
		SayuCd	: getSelectedVal('cboSayu').value,
		UserId	: pUserId,
		DaeUser	: DaeUser,
		requestType	: 'updtConfirm'
	}
	ajaxAsync('/webPage/ecmr/Cmr6000Servlet', data, 'json',successUpdateConfirm);
}

// 수정완료
function successUpdateConfirm(data) {
	if (typeof data == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	if(data === 'OK') {
		dialog.alert('결재정보 수정이 완료되었습니다.', function() {
			$('[data-ax5select="cboBlank"]').ax5select('setValue',cboBlankData[0].cm_micode,true);
			$('[data-ax5select="cboSayu"]').ax5select('setValue',cboSayuData[0].cm_micode,true);
		});
		
		screenInit();
	} else {
		dialog.alert(data, function() {});
	}
}

// 대결재자 정보 가져오기
function selectUser() {
	var selItem = approvalGrid.list[approvalGrid.selectedDataIndexs[0]];
	var reqType = '';
	if(selItem.confname == '3자확인' || selItem.confname == '주관부서확인') {
		reqType = 'selectDaegyulETC';
	}else if(selItem.confname == '책임자') {
		reqType = 'selectDaegyulETC2';
	}else {
		reqType = 'selectDaegyul';
	}
	
	var data = new Object();
	data = {
			 UserId	: selItem.team2,
		   BaseUser	: selItem.baseuser2,
		requestType	: reqType
	}
	ajaxAsync('/webPage/ecmr/Cmr6000Servlet', data, 'json',successSelectUser);
}

// 대결재자 정보 가져오기 완료
function successSelectUser(data) {
	cboUserData = data;
	
	$('[data-ax5select="cboUser"]').ax5select({
		options: injectCboDataToArr(cboUserData, 'userid' , 'username')
	});
	
	if (cboUserData.length>0) $('[data-ax5select="cboUser"]').ax5select('setValue','00',true);
}

// 결재 정보 그리드 클릭
function clickApprovalGrid(index) {
	$('#lblSayu').css('visibility', 'hidden');
	$('#divSayu').css('visibility', 'hidden');
	$('#lblBlank').css('visibility', 'hidden');
	$('#divBlank').css('visibility', 'hidden');
	$('#btnUpdate').css('visibility', 'hidden');
	$('#divUser').css('visibility', 'hidden');
	
	var WkSgn 	= '';
	var ConGbn 	= '';
	var BlankFg = false;
	var ConFg	= false;
	var selItem = approvalGrid.list[index];
	
	if(reqSta != "0" ) return;
	
	if(selItem.teamcd2 !== '3' && selItem.teamcd2 !== '4' &&
	   selItem.teamcd2 !== '6' && selItem.teamcd2 !== '7' && 
	   selItem.teamcd2 !== '8') {
		return;
	}
	
	if(selItem.congbn == '4') return;
	
	if(selItem.confdate != '') return;
			
	if (selItem.confdate != null && selItem.confdate != '' && selItem.confdate != undefined && selItem.confdate.length > 0) return;
	
	switch (HoliGbn) {
	   case '1':
	        WkSgn = selItem.emgaft;
	        break;
	   case '2':
	        WkSgn = selItem.holi;
	        break;
	   case '3':
	        WkSgn = selItem.emger;
	        break;
	   default:
	        WkSgn = selItem.blank;
	}
		
    if (WkSgn  === '2' || WkSgn === '3' || WkSgn === '5' || WkSgn === '6') BlankFg = true;
    
    ConGbn = selItem.congbn;
    ConFg = false;
    
    if (strEditor !== pUserId && strAdmin !== 'Y') {
    	if (selItem.teamcd2 !== '1' && selItem.teamcd2 != '2' && strNxtSign == pUserId) {
    		ConFg = true;
    	}
    } else {
    	if (strAdmin === 'Y' && selItem.teamcd2 !== '1' && selItem.teamcd2 != '2')
    	   ConFg = true;
    }
    
    if (ConFg) {
    	showModiDiv(selItem);
   	}
}

// 결재 수정할수 있는 div 보여주기
function showModiDiv(selItem) {
	if(cboBlankData.length === 0 ) {
		getCodeInfo(selItem);
	} else {	
		blankProc(selItem);
	}
}

// 콤보정보 가져오기
function getCodeInfo(selItem) {
	var cboOptions = [];
	var codeInfos = getCodeInfoCommon([
			new CodeInfoOrdercd('SGNCD',  'SEL','N', '3', ''),
			new CodeInfoOrdercd('DAEGYUL','SEL','N', '1', ''),
	]);
	cboBlankData 	= codeInfos.SGNCD;
	cboSayuData 	= codeInfos.DAEGYUL;
	
	$('[data-ax5select="cboSayu"]').ax5select({
		options: injectCboDataToArr(cboSayuData, 'cm_micode' , 'cm_codename')
	});
	
	cboBlankFilter(selItem);
}

function blankProc(selItem) {
	if (cboSayuData.length>0) $('[data-ax5select="cboSayu"]').ax5select('setValue','00',true);
	
	cboBlankFilter(selItem);
	
//	if (cboBlankData.length > 0) {
//		$('[data-ax5select="cboBlank"]').ax5select('setValue','00',true);
//		$('#cboBlank').trigger('change');
//	}
}

function cboBlankFilter(selItem) {
	var fcboBlankData = [];
	
	if(adminYN == null || adminYN == undefined || adminYN == '') adminYN = strAdmin=='Y'?true:false;
	
	if(selItem.rgtcd == '41') {
		if(BadminYN || strLIB == 'Y') {
			fcboBlankData = cboBlankData.filter(function(item) {
				if(item.cm_micode == '00' || item.cm_micode == '3' || item.cm_micode == '7') return true;
				else return false;
			});
		}else {
			fcboBlankData = cboBlankData.filter(function(item) {
				if(item.cm_micode == '00' || item.cm_micode == '3') return true;
				else return false;
			});
		}
	}else if(selItem.team2 == 'Q1') {
		fcboBlankData = cboBlankData.filter(function(item) {
			if(item.cm_micode == '00' || item.cm_micode == '4') return true;
			else return false;
		});
	}else {
		if(adminYN || strLIB == 'Y') {
			fcboBlankData = cboBlankData.filter(function(item) {
				if(item.cm_micode == '00' || item.cm_micode == '4' || item.cm_micode == '7') return true;
				else return false;
			});
		}else {
			fcboBlankData = cboBlankData.filter(function(item) {
				if(item.cm_micode == '00' || item.cm_micode == '4') return true;
				else return false;
			});
		}
	}
    
	$('[data-ax5select="cboBlank"]').ax5select({
		options: injectCboDataToArr(fcboBlankData, 'cm_micode' , 'cm_codename')
	});
	
	$('[data-ax5select="cboBlank"]').ax5select('setValue','00',true);
	$('#cboBlank').trigger('change');
	
	$('#lblSayu').css('visibility', 'visible');
	$('#divSayu').css('visibility', 'visible');
	$('#lblBlank').css('visibility', 'visible');
	$('#divBlank').css('visibility', 'visible');
	$('#btnUpdate').css('visibility', 'visible');
}

// 결재 정보 리스트 가져오기
function selectConfirm() {
	var data = new Object();
	data = {
			AcptNo	: pReqNo,
		requestType	: 'selectConfirm'
	}
	ajaxAsync('/webPage/ecmr/Cmr6000Servlet', data, 'json',successSelectConfirm);
}

// 결재 정보 리스트 가져오기 완료
function successSelectConfirm(data) {
	
	if (typeof data == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	approvalGridData= data;
	approvalGrid.setData(approvalGridData);
}

// 현재 상황 가져오기
function selectLocat() {
	var data = new Object();
	data = {
			 UserId	: pUserId,
		     AcptNo	: pReqNo,
		requestType	: 'selectLocat'
	}
	ajaxAsync('/webPage/ecmr/Cmr6000Servlet', data, 'json',successSelectLocat);
}
// 현재 상황 가져오기완료
function successSelectLocat(data) {

	if (typeof data == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	var confLocat = data[0];
	
	reqSta 		= confLocat.sta;
	strPassOk 	= confLocat.passok;
	HoliGbn 	= confLocat.holigbn;
	strEditor 	= confLocat.editor;
	strNxtSign 	= confLocat.team;
	strAdmin 	= confLocat.admin;
	$('#txtLocat').val(confLocat.confname);
	
	if (confLocat.msg !== undefined && confLocat.msg !== null && confLocat.msg !== '' && confLocat.msg.length !== 0) {
		$('#txtLocatCncl').val(confLocat.msg);
		$('#txtLocatCncl').css('display', 'inline-block');
	} else {
		$('#txtLocatCncl').css('display', 'none');
	}
	
	if (reqSta === '0' && (strAdmin === 'Y' || strEditor === pUserId)) {
		selectTimeSch();
	}
	$('#txtQryCd').val(confLocat.qrycd);
}

// 요일별 결재시간을 조회
function selectTimeSch() {
	var data = new Object();
	data = {
		requestType	: 'SelectTimeSch'
	}
	ajaxAsync('/webPage/common/TimeSchServlet', data, 'json',successSelectTimeSch);
}

// 요일별 결재시간을 조회 완료
function successSelectTimeSch(data) {
	if (HoliGbn === '1') {
		HoliGbn = '2';
	} else {
	   if (data === '1') {
		   if (strPassOk === '2') {
			   HoliGbn = '3';
		   } else {
			   HoliGbn = '2';
		   }
	   } else if (strPassOk === '2')  {
		   HoliGbn = '2';
	   }
	}
}