/**
 * [사용자정보 > 업무권한 일괄등록] 화면 기능정의
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

var jobGrid			= new ax5.ui.grid();

var jobGridData 	= [];
var cboUserDivData	= [];
var cboUserData		= [];
var cboSysCdData	= [];

var ulJobInfoData	= [];
var ulUserInfoData	= [];

var teamSw			= false;

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
        {key: "cm_userid", 		label: "사용자번호",  		width: '10%'},
        {key: "cm_username",	label: "사용자명",  		width: '10%'},
        {key: "jobgrp", 		label: "시스템명",  		width: '35%', align: 'left'},
        {key: "job", 			label: "업무명(업무코드)",  width: '45%', align: 'left'}
    ]
});

$('[data-ax5select="cboUserDiv"]').ax5select({
	options: []
});

$('[data-ax5select="cboUser"]').ax5select({
	options: []
});

$('[data-ax5select="cboSysCd"]').ax5select({
	options: []
});

$('[data-ax5select="cboUser"]').ax5select("disable");
$('#btnSel').prop("disabled", true);

$('input.checkbox-setjob').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	$('#btnQry').prop('disabled', true);
	getSysInfo();
	getCodeInfo();
	
	// 사용자 전체 선택
	$('#chkAllUser').bind('click', function() {
		var checkSw = $('#chkAllUser').is(':checked');
		var addId = null;
		ulUserInfoData.forEach(function(item, index) {
			if(teamSw) {
				addId = item.cm_userid;
			} else {
				addId = item.code;
			}
			
			if(checkSw) {
				$('#chkUser'+addId).wCheck('check', true);
			} else {
				$('#chkUser'+addId).wCheck('check', false);
			}
		});
	});
	
	// 업무 전체 선택
	$('#chkAllJob').bind('click', function() {
		var checkSw = $('#chkAllJob').is(':checked');
		var addId = null;
		ulJobInfoData.forEach(function(item, index) {
			addId = item.cm_jobcd;
			
			if(checkSw) {
				$('#chkJob'+addId).wCheck('check', true);
			} else {
				$('#chkJob'+addId).wCheck('check', false);
			}
		});
	});
	
	// 사용자/팀명 입력창 엔터
	$('#txtUser').bind('keydown', function(evnet) {
		if(event.keyCode === 13) {
			getUserList();
			ulUserInfoData = [];
			$('#ulUserInfo').empty();
		}
	});
	
	// 사용자 구분
	$('#cboUserDiv').bind('change', function() {
		getUserList();
	});
	
	// 시스템
	$('#cboSysCd').bind('change', function() {
		getJobInfo();
	});
	
	// 사용자선택
	$('#btnSel').bind('click', function() {
		getUserOrTeamList();
	});
	// 등록
	$('#btnReq').bind('click', function() {
		setAllUserJob();
	});
	// 폐기
	$('#btnDel').bind('click', function() {
		delUserJob();
	});
	// 조회
	$('#btnQry').bind('click', function() {
		getUserJobList();
	});
	
	// 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 업무 삭제
function delUserJob() {
	var selIndex = jobGrid.selectedDataIndexs;
	var USerId	= '';
	var JobList	= [];
	if(selIndex.length === 0 ){
		dialog.alert('삭제할 업무를 하단 그리드에서 선택한 후 처리하십시오.', function(){});
		return;
	}
	
	confirmDialog.confirm({
		msg: '선택한 업무를 폐기하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			selIndex.forEach(function(selIndex, index) {
				JobList.push(jobGrid.list[selIndex]);
			});
			var data = new Object();
			data = {
				UserId 		: '',
				JobList 	: JobList,
				requestType	: 'delUserJob'
			}
			ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successDelUserJob);
		}
	});
}

// 업무 삭제 완료
function successDelUserJob(data) {
	if(data === 0) {
		dialog.alert('선택한 업무에 대한 폐기처리를 완료하였습니다.', function() {
			$('#btnQry').trigger('click');
		})
	}
}

// 업무권한 일괄 등록
function setAllUserJob() {
	
	var SysCd = '';
	var UserList 	= [];
	var JobList 	= [];
	var addId		= null;
	
	ulUserInfoData.forEach(function(item, index) {
		item.cm_userid = item.value;
		if(teamSw) {
			addId = item.cm_userid;
		} else {
			addId = item.code;
		}
		 
		if($('#chkUser'+addId).is(':checked')) {
			if(teamSw) {
				item.code = item.cm_userid;
			}
			UserList.push(item);
		}
	});
	
	if(UserList.length === 0) { 
		dialog.alert('사용자를 선택하여 주십시오.', function(){});
		return;
	}
	
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function(){});
		return;
	}
	
	ulJobInfoData.forEach(function(item, index) {
		addId = item.cm_jobcd;
		 
		if($('#chkJob'+addId).is(':checked')) {
			JobList.push(item);
		}
	});
	
	if(JobList.length === 0 ) {
		dialog.alert('업무를 선택하여 주십시오.', function(){});
		return;
	}
	SysCd = getSelectedVal('cboSysCd').value;
	
	var data = new Object();
	data = {
		SysCd 		: SysCd,
		UserList 	: UserList,
		JobList 	: JobList,
		requestType	: 'setAllUserJob'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successSetAllUserJob);
}

// 업무권한 일괄등록 완료
function successSetAllUserJob(data) {
	dialog.alert('사용자별 업무권한 등록처리를 완료하였습니다.', function() {
		$('#btnQry').trigger('click');
	});
}

// 사용자/팀의 업무정보 리스트 가져오기
function getUserJobList(){
	var data = new Object();
	data = {
		gbnCd 	: getSelectedVal('cboUser').ID,
		UserId 	: getSelectedVal('cboUser').code,
		requestType	: 'getUserJobList'
	}
	$('[data-ax5grid="jobGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successGetUserJobList);
}

// 사용자/팀의 업무정보 리스트 가져오기 완료
function successGetUserJobList(data) {
	$(".loding-div").remove();
	jobGridData = data;
	jobGrid.setData(jobGridData);
}

// 시스템 업무 정보가져오기
function getJobInfo() {
	var data = new Object();
	var etcData = new Object();

	data = {
		UserId 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').value,
		SecuYn 		: 'N',
		CloseYn 	: 'N',
		SelMsg 		: '',
		sortCd 		: 'NAME',
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetJobInfo);
}

// 시스템 업무정보 가져오기 완료
function successGetJobInfo(data) {
	ulJobInfoData = data;
	makeJobInfoUlList();
}

// 유저 리스트 가져오기
function getUserList() {
	var UserName = $('#txtUser').val().trim();
	if(UserName.length === 0 ) {
		return;
	}
	var data = new Object();
	data = {
		selectedIndex 	: getSelectedVal('cboUserDiv').value,
		UserName 		: UserName,
		requestType		: 'getUserList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successGetUserList);
}

//유저 리스트 가져오기 완료
function successGetUserList(data) {
	cboUserData = [];
	cboUserData = data;
	
	if(cboUserData.length > 0 ) {
		$('[data-ax5select="cboUser"]').ax5select("enable");
		$('#btnSel').prop("disabled", false);
		$('#btnQry').prop("disabled", false);
	} else {
		$('[data-ax5select="cboUser"]').ax5select("disable");
		$('#btnSel').prop("disabled", true);
		$('#btnQry').prop("disabled", true);
	}
	
	$('[data-ax5select="cboUser"]').ax5select({
        options: injectCboDataToArr(cboUserData, 'code' , 'labelField')
	});
}

// 선택 클릭시 해당 유저 또는 팀의 유저 리스트
function getUserOrTeamList() {
	if(getSelectedVal('cboUser').ID === 'USER') {
		if(getSelectedVal('cboUser').code === '00') {
			return;
		}
		if(ulUserInfoData.indexOf(getSelectedVal('cboUser')) > -1) {
			return;
		}
		
		var checkSameUserSw = false;
		
		for(var i=0; i<ulUserInfoData.length; i++) {
			if(ulUserInfoData[i].value === getSelectedVal('cboUser').value)  {
				checkSameUserSw = true;
				dialog.alert('이미 목록에 추가 되어 있는 사용자 입니다.');
				break;
			}
		}
		if(!checkSameUserSw) {
			ulUserInfoData.push(getSelectedVal('cboUser'));
			makeUserInfoUlList();
		}
	} else {
		getTeamUserList();
	}
}

// 팀의 유저리스트 가져오기
function getTeamUserList() {
	var data = new Object();
	data = {
		Cbo_Sign 	: getSelectedVal('cboUser').code,
		requestType	: 'getTeamUserList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successGetTeamUserList);
}

// 팀의 유저리스트 가져오기 완료
function successGetTeamUserList(data) {
	ulUserInfoData = data;
	makeUserInfoUlList();
}

// 시스템정보가져오기
function getSysInfo() {
	var data = new Object();
	data = {
		requestType	: 'getSysInfo',
		UserId : userId,
		SecuYn : "Y",
		SelMsg : "SEL",
		CloseYn : "N",
		ReqCd : ""
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

// 시스템정보가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	$('[data-ax5select="cboSysCd"]').ax5select({
        options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function getCodeInfo() {
	cboUserDivData = [{
		cm_codename : "사용자명",
		cm_micode : "0"
	},{
		cm_codename : "팀명",
		cm_micode : "1"
	}];
	
	$('[data-ax5select="cboUserDiv"]').ax5select({
        options: injectCboDataToArr(cboUserDivData, 'cm_micode' , 'cm_codename')
	});
};

// 유저 리스트 만들기
function makeUserInfoUlList() {
	if(ulUserInfoData.length === 0 ) {
		return;
	}
	var ulObj = ulUserInfoData[0];
	teamSw	= ulObj.hasOwnProperty('cm_userid'); 
	$('#ulUserInfo').empty();
	var liStr = '';
	var addId = null;
	ulUserInfoData.forEach(function(item, index) {
		if(teamSw) {
			addId = item.cm_userid;
		} else {
			addId = item.code;
		}
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-user" id="chkUser'+addId+'" data-label="'+item.labelField+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulUserInfo').html(liStr);
	
	$('input.checkbox-user').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

//담당직무 리스트
function makeJobInfoUlList() {
	$('#ulJobInfo').empty();
	var liStr = '';
	var addId = null;
	ulJobInfoData.forEach(function(item, index) {
		addId = item.cm_jobcd;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-job" id="chkJob'+addId+'" data-label="'+item.cm_jobname+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulJobInfo').html(liStr);
	
	$('input.checkbox-job').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 모달 닫기
function popClose() {
	window.parent.setUserJobModal.close();
}