/**
 * [사용자정보 > 권한복사] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-24
 * 
 */


var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var userData	= window.parent.popData;

var jobGrid			= new ax5.ui.grid();

var jobGridData 	= [];
var cboFromUserData	= [];
var cboToUserData	= [];
var ulDutyInfoData	= [];


jobGrid.setConfig({
    target: $('[data-ax5grid="jobGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "jobgrp",		label: "시스템",  		width: '50%', align: 'left'},
        {key: "job", 		label: "업무명(업무코드)",  width: '50%', align: 'left'}
    ]
});

$('[data-ax5select="cboFromUser"]').ax5select({
	options: []
});

$('[data-ax5select="cboToUser"]').ax5select({
	options: []
});

$('input.checkbox-jobcopy').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getAllUser();
	getCodeInfo();
	
	// 사용자 엔터
	$('#txtFromUser').bind('keypress', function(evnet) {
		if(event.keyCode === 13) {
			$('#txtFromUserId').val('');
			setFromUser();
		}
	});
	// 사용자 엔터
	$('#txtToUser').bind('keypress', function(evnet) {
		if(event.keyCode === 13) {
			setToUser();
		}
	});
	
	// 시스템 콤보 변경
	$('#cboFromUser').bind('change', function() {
		if(getSelectedVal('cboFromUser').cm_username !== '0000') {
			$('#txtFromUser').val(getSelectedVal('cboFromUser').cm_username);
			$('#txtFromUserId').val(getSelectedVal('cboFromUser').value);
		}
		setFromUser();
	});
	
	// 시스템 콤보 변경
	$('#cboToUser').bind('change', function() {
		if(getSelectedVal('cboToUser').cm_username !== '0000') {
			$('#txtToUser').val(getSelectedVal('cboToUser').cm_username);
			$('#txtToUserId').val(getSelectedVal('cboToUser').value);
		}
		setToUser();
	});
	
	// 직무 전체선택
	$('#chkAllDuty').bind('click', function() {
		var checkSw = $('#chkAllDuty').is(':checked');
		var addId = null;
		ulDutyInfoData.forEach(function(item, index) {
			addId = item.cm_micode;
			if(checkSw) {
				$('#chkDuty'+addId).wCheck('check', true);
			} else {
				$('#chkDuty'+addId).wCheck('check', false);
			}
		});
	});
	
	// 조회 클릭
	$('#btnCopy').bind('click', function() {
		copyJob();
	});
	// 닫기 클릭
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 권한 복사
function copyJob() {
	var addId 			= null;
	var jobSelIndexs 	= null;
	var etcData			= new Object();
	var JobList 		= [];
	var RgtList 		= [];
	
	if(getSelectedIndex('cboFromUser') < 1) {
		dialog.alert('복사대상사용자 (From)을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	if(getSelectedIndex('cboToUser') < 1) {
		dialog.alert('복사대상사용자 (TO)을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	ulDutyInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if($('#chkDuty'+addId).is(':checked')) {
			RgtList.push(item);
		}
	})
	
	if(RgtList.length === 0 ) {
		dialog.alert('담당직무를 선택하여 주십시오.', function(){});
		return;
	}
	
	jobSelIndexs = jobGrid.selectedDataIndexs;
	
	if(jobSelIndexs.length === 0 ) {
		dialog.alert('담당업무를 선택하여 주십시오.', function(){});
		return;
	}
	
	jobSelIndexs.forEach(function(item, index) {
		JobList.push(jobGrid.list[item]);
	});
	
	etcData.userid0 = getSelectedVal('cboFromUser').value;
	etcData.userid1 = getSelectedVal('cboToUser').value;
	
	var data = new Object();
	data = {
		etcData 	: etcData,
		JobList 	: JobList,
		RgtList 	: RgtList,
		requestType	: 'userCopy'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successCopyJob);
}

function successCopyJob(data) {
	dialog.alert('사용자 권한정보 복사처리를 완료하였습니다.', function(){});
}

// cboToUser 콤보 세팅
function setToUser() {
	var txtToUser = $('#txtToUser').val().trim();
	var txtToUserId = $('#txtToUserId').val().trim();
	if(txtToUser.length !== 0 ) {
		for(var i=0; i<cboToUserData.length; i++) {
			if(cboToUserData[i].cm_username === txtToUser) {
				if(txtToUserId.length > 0) {
					if(cboToUserData[i].cm_userid === txtToUserId) {
						$('[data-ax5select="cboToUser"]').ax5select('setValue', cboToUserData[i].cm_userid, true);
						break;
					}
				} else {
					$('[data-ax5select="cboToUser"]').ax5select('setValue', cboToUserData[i].cm_userid, true);
					break;
				}
			}
		}
	}
}

// cboFromUser 콤보 세팅
function setFromUser() {
	var txtFromUser = $('#txtFromUser').val().trim();
	var txtFromUserId = $('#txtFromUserId').val().trim();
	if(txtFromUser.length !== 0 ) {
		for(var i=0; i<cboFromUserData.length; i++) {
			if(cboFromUserData[i].cm_username === txtFromUser) {
				if(txtFromUserId.length > 0) {
					if(cboFromUserData[i].cm_userid === txtFromUserId) {
						$('[data-ax5select="cboFromUser"]').ax5select('setValue', cboFromUserData[i].cm_userid, true);
						break;
					}
				} else {
					$('[data-ax5select="cboFromUser"]').ax5select('setValue', cboFromUserData[i].cm_userid, true);
					break;
				}
			}
		}
	}
	if(getSelectedIndex('cboFromUser') < 1) {
		return;
	}
 	getUserRgtCd();
}

// 사용자 권한 가져오기
function getUserRgtCd() {
	var data = new Object();
	data = {
		UserId 		: getSelectedVal('cboFromUser').value,
		requestType	: 'getUserRGTCD'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successGetUserRgtCd, getUserJobList);
}

// 사용자 권한 가져오기 완료
function successGetUserRgtCd(data) {
	ulDutyInfoData = data;
	makeDutyInfoUlList();
}

// 사용자 업무리스트 가져오기
function getUserJobList() {
	var data = new Object();
	//gbnCd, UserId
	data = {
		gbnCd		: 'USER',
		UserId 		: getSelectedVal('cboFromUser').value,
		requestType	: 'getUserJobList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successGetUserJobList);
}

// 사용자 업무리스트 가져오기 완료
function successGetUserJobList(data) {
	jobGridData = data;
	jobGrid.setData(jobGridData);
}

// 모든 사용자 가져오기
function getAllUser() {
	var data = new Object();
	data = {
		requestType	: 'getAllUser'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successGetAllUser);
}

// 직무가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('RGTCD', '', 'N')
	]);
	ulDutyInfoData 	= codeInfos.RGTCD;
	
	makeDutyInfoUlList();
};

// 모든 사용자 가져오기 완료
function successGetAllUser(data) {
	cboFromUserData = data;
	cboToUserData	= data;
	$('[data-ax5select="cboFromUser"]').ax5select({
        options: injectCboDataToArr(cboFromUserData, 'cm_userid' , 'cm_userinfo')
	});
	
	$('[data-ax5select="cboToUser"]').ax5select({
        options: injectCboDataToArr(cboToUserData, 'cm_userid' , 'cm_userinfo')
	});
	
	$('#txtFromUser').val(userData.username);
	$('#txtFromUserId').val(userData.userid);
	setFromUser();
}

//담당직무 리스트
function makeDutyInfoUlList() {
	$('#ulDutyInfo').empty();
	var liStr = '';
	var addId = null;
	ulDutyInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		liStr += '<li class="list-group-item">';
		if(item.checkbox !== undefined && item.checkbox === 'true') {
			liStr += '	<input type="checkbox" class="checkbox-duty" id="chkDuty'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" checked="checked"/>';
		} else {
			liStr += '	<input type="checkbox" class="checkbox-duty" id="chkDuty'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
		}
		liStr += '</li>';
	});
	$('#ulDutyInfo').html(liStr);
	
	$('input.checkbox-duty').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 모달 닫기
function popClose() {
	window.parent.jobCopyModal.close();
}