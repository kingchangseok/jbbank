/**
 * [환경설정 > 알림기준정보 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-25
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var codeList    = window.top.codeList;          //전체 코드 리스트

var organizationModal 	= new ax5.ui.modal();	// 조직도 팝업

var notiGrid		= new ax5.ui.grid();

var notiGridData 	= [];
var cboUserData		= [];
var cboCommonData	= [];
var cboHoliData		= [];
var ulReqInfoData	= [];
var ulNotiInfoData	= [];

var modiDeptSw 	= false;
var subSw		= true;
var txtUserId	= null;	// 조직도에서 선택된 유저아이디
var txtUserName	= null; // 조직도에서 선택된 유저명

$('[data-ax5select="cboUser"]').ax5select({
    options: []
});

$('[data-ax5select="cboCommon"]').ax5select({
	options: []
});
$('[data-ax5select="cboHoli"]').ax5select({
	options: []
});


$('input.checkbox-noti').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getCodeInfo();
	getNotiList();
	getNotiInfo();
	
	// 발신사용자 더블 클릭
	$('#txtUser').bind('dblclick', function() {
		openOranizationModal();
	});
	
	// 발신 사용자 등록
	$('#btnReqUser').bind('click', function() {
		insertNotiInfo();
	});
	
	// 요청 리스트 전체선택
	$('#chkAll').bind('click', function() {
		var chkSw = $('#chkAll').is(':checked');
		reqAllCheck(chkSw);
	});
	// 등록
	$('#btnReq').bind('click', function() {
		insertNotiList();
	});
	// 폐기
	$('#btnDel').bind('click', function() {
		delNotiList();
	});
	createGridView();
});

function createGridView() {
	
	notiGrid		= new ax5.ui.grid();
	notiGrid.setConfig({
	    target: $('[data-ax5grid="notiGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: true,
	    showLineNumber: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	            //this.self.select(this.dindex);
	            clickNotiGrid(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "qrycd",   label: "신청구분",  	width: '25%', align: 'left'},
	        {key: "notigbn", label: "알림구분",  	width: '25%', align: 'left'},
	        {key: "common",  label: "업무중",  	width: '25%', align: 'left'},
	        {key: "holiday", label: "업무후",  	width: '25%', align: 'left'}
	    ]
	});

	if (notiGridData != null && notiGridData.length > 0) {
		notiGrid.setData(notiGridData);
	}
	
}
// 알림 기준정보 폐기
function delNotiList() {
	
	var selIn = notiGrid.selectedDataIndexs;
	var delList = [];
	if(selIn.length === 0 ) {
		dialog.alert('하단 목록에서 삭제대상을 선택한 후 진행하시기 바랍니다.', function(){});
		return;
	}
	
	selIn.forEach(function(selIn, index) {
		delList.push(notiGrid.list[selIn]);
	});
	
	var data = new Object();
	data = {
		userId 		: userId,
		delList 	: delList,
		requestType	: 'delNotiInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successDelNotiList);
}

// 알림 기준정보 폐기 완료
function successDelNotiList(data) {
	dialog.alert('삭제처리가 정상적으로 종료되었습니다.', function(){});
	getNotiList();
}

// 알림 기준정보 등록
function insertNotiList() {
	
	var addId = null;
	var qrycd = '';
	var notigbn = '';
	
	ulReqInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if($('#chkReq'+addId).is(':checked')) {
			if(qrycd.length > 0 ) qrycd += ',';
			qrycd += item.cm_micode;
		}
	});
	
	ulNotiInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if($('#chkNotis'+addId).is(':checked')) {
			if(notigbn.length > 0 ) notigbn += ',';
			notigbn += item.cm_micode;
		}
	});
	
	if(qrycd.length === 0 ) {
		dialog.alert('신청구분을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	if(notigbn.length === 0 ) {
		dialog.alert('알림구분을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	if(getSelectedIndex('cboCommon') < 1) {
		dialog.alert('업무중 알림을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	if(getSelectedIndex('cboHoli') < 1) {
		dialog.alert('업무후 알림을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	var data = new Object();
	data = {
		userId 		: userId,
		qrycd 		: qrycd,
		notigbn 	: notigbn,
		common 		: getSelectedVal('cboCommon').value,
		holiday 	: getSelectedVal('cboHoli').value,
		requestType	: 'addNotiInfo2'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successInsertNotiList);
}

// 알림기준 등록 완료
function successInsertNotiList(data) {
	dialog.alert('등록처리가 정상적으로 종료되었습니다.', function(){});
	getNotiList();
}

// 알림 유저 선택 창 오픈
function openOranizationModal() {
	setTimeout(function() {
		organizationModal.open({
	        width: 400,
	        height: 700,
	        iframe: {
	            method: "get",
	            url: "../../modal/OrganizationModal.jsp"
	        },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	                mask.close();
	                if(txtUserId !== null) {
	                	var userObj = new Object();
	                	userObj.cm_userid = txtUserId;
	                	userObj.cm_idname = txtUserName + '[' + txtUserId + ']';
	                	
	                	cboUserData = [];
	                	cboUserData.push(userObj);
	                	
	                	$('[data-ax5select="cboUser"]').ax5select({
	                        options: injectCboDataToArr(cboUserData, 'cm_userid' , 'cm_idname')
	                	});
	                	
	                	$('#txtUser').val(txtUserName);
	                }
	                txtUserId = null;
	            }
	        }
	    }, function () {
	    });
	}, 200);
}

// 알림 사용자 정보 입력
function insertNotiInfo() {
	var txtSMSSend = $('#txtSMSSend').val().trim();
	
	if(txtSMSSend.length === 0 ) {
		dialog.alert('SMS발송전화번호를 입력하여 주시기 바랍니다.', function() {});
		return;
	}
	
	if(cboUserData.length === 0 ) {
		dialog.alert('알림발송 사용자를 선택하여 주시기 바랍니다.', function() {});
		return;
	}
	
	var data = new Object();
	data = {
		userId 		: '',
		smssend 	: txtSMSSend,
		notiuser 	: getSelectedVal('cboUser').value,
		requestType	: 'addNotiInfo1'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successInsertNotiInfo);
}
// 알림 사용자 정보 입력 완료
function successInsertNotiInfo(data) {
	if(data === '0') {
		dialog.alert('등록처리가 정상적으로 되었습니다.', function() {});
	} else {
		dialog.alert('등록처리중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.', function() {});
	}
}


// 알림기준정보 그리드 클릭
function clickNotiGrid(index) {
	var selItem = notiGrid.list[index];
	if(ulReqInfoData.length > 0 ) {
		reqAllCheck(false);
		
		ulReqInfoData.forEach(function(item, index) {
			if(selItem.cm_qrycd === item.cm_micode) {
				$('#chkReq'+item.cm_micode).wCheck('check', true);
				$('#chkReq'+item.cm_micode).focus();
			}
		});
	}
	
	if(ulNotiInfoData.length > 0 ) {
		notiAllCheck(false);
		
		ulNotiInfoData.forEach(function(item, index) {
			if(selItem.cm_notigbn === item.cm_micode) {
				$('#chkNotis'+item.cm_micode).wCheck('check', true);
			}
		});
	}
	
	cboCommonData.forEach(function(item, index) {
		if(selItem.cm_common === item.cm_micode) {
			$('[data-ax5select="cboCommon"]').ax5select('setValue', item.cm_micode, true);
		}
	});
	
	cboHoliData.forEach(function(item, index) {
		if(selItem.cm_holiday === item.cm_micode) {
			$('[data-ax5select="cboHoli"]').ax5select('setValue', item.cm_micode, true);
		}
	});
	
}

// 신청구분 리스트 전체 선택
function reqAllCheck(checkSw) {
	var addId = null;
	ulReqInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#chkReq'+addId).wCheck('check', true);
		} else {
			$('#chkReq'+addId).wCheck('check', false);
		}
	});
}

// 알림구분 리스트 전체 선택
function notiAllCheck(checkSw) {
	var addId = null;
	ulNotiInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#chkNotis'+addId).wCheck('check', true);
		} else {
			$('#chkNotis'+addId).wCheck('check', false);
		}
	});
}


// 알림 관련 정보들 가져오기
function getNotiInfo() {
	var data = new Object();
	data = {
		requestType	: 'getNotiInfo1'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetNotiInfo);
}
// 알림 관련 정보들 가져오기 완료
function successGetNotiInfo(data) {
	cboUserData = data;
	if(cboUserData[0].gbncd === 'SMS') {
		
		$('#txtSMSSend').val(cboUserData.shift().smstelno);
		//cboUserData.splice(0,1);
	}
	
	$('[data-ax5select="cboUser"]').ax5select({
        options: injectCboDataToArr(cboUserData, 'cm_userid' , 'cm_idname')
	});
}

// 알림 정보 리스트 가져오기
function getNotiList() {
	var data = new Object();
	data = {
		requestType	: 'getNotiInfo2'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successgetNotiList);
}
// 알림 정보 리스트 가져오기 완료
function successgetNotiList(data) {
	notiGridData = data;
	notiGrid.setData(notiGridData);
}


// 업무 중, 후 구분가져오기
function getCodeInfo() {
	
	var codeInfos = getCodeInfoCommon([
										new CodeInfo('NOTICD','SEL','N'),
										new CodeInfo('REQUEST','','N'),
										new CodeInfo('NOTIGBN','','N'),
										]);
	cboCommonData 	= codeInfos.NOTICD;
	cboHoliData 	= codeInfos.NOTICD;
	ulReqInfoData	= codeInfos.REQUEST;
	ulNotiInfoData	= codeInfos.NOTIGBN;
	
//	cboHoliData   = fixCodeList(codeList['REQUEST'], '', '', '', 'N');
//	ulReqInfoData = fixCodeList(codeList['REQUEST'], '', '', '', 'N');
//	ulNotiInfoData = fixCodeList(codeList['NOTIGBN'], '', '', '', 'N');
//	cboCommonData = fixCodeList(codeList['NOTICD'], 'SEL', '', '', 'N');
//	cboHoliData   = cboCommonData;
//	codeList = null;
	
	$('[data-ax5select="cboCommon"]').ax5select({
        options: injectCboDataToArr(cboCommonData, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboHoli"]').ax5select({
		options: injectCboDataToArr(cboHoliData, 'cm_micode' , 'cm_codename')
	});
	
	makeReqInfoUlList();
	makeNotiInfoUlList();
}

// 알림종류
function makeNotiInfoUlList() {
	$('#ulNotiInfo').empty();
	var liStr = '';
	var addId = null;
	ulNotiInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-notis" id="chkNotis'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulNotiInfo').html(liStr);
	
	$('input.checkbox-notis').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 요청 리스트
function makeReqInfoUlList() {
	$('#ulReqInfo').empty();
	var liStr = '';
	var addId = null;
	ulReqInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-req" id="chkReq'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulReqInfo').html(liStr);
	
	$('input.checkbox-req').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}