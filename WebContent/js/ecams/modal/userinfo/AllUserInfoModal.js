/**
 * [사용자정보 > 전체사용자조회] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-26
 * 
 */


var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var userGrid		= new ax5.ui.grid();

var userGridData 	= [];
var cboTeamData		= [];
var cboSysCdData	= []; //시스템 데이터
var cboJobCdData	= []; //업무 데이터
var cboRgtCdData	= []; //직무 데이터

userGrid.setConfig({
    target: $('[data-ax5grid="userGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
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
        {key: "cm_userid", 		label: "사용자번호",  		width: '8%'},
        {key: "cm_username",	label: "사용자명",  		width: '6%'},
        {key: "position", 		label: "직위",  			width: '6%'},
        {key: "cm_sysmsg", 		label: "시스템",  		width: '15%', align: 'left'},
        {key: "deptname", 		label: "소속조직",  		width: '7%'},
        {key: "rgtname", 		label: "담당직무",  		width: '15%', align: 'left'},
        {key: "cm_ipaddress", 	label: "IP Address",  	width: '8%',  align: 'left'},
        {key: "cm_telno1", 		label: "전화번호1",  		width: '8%'},
        {key: "cm_telno2", 		label: "전화번호2",  		width: '8%'},
        {key: "cm_logindt", 	label: "최종로그인",  		width: '8%'},
        {key: "cm_active", 		label: "활성화여부",  		width: '5%'}
    ]
});

$('[data-ax5select="cboTeam"]').ax5select({
	options: []
});
$('[data-ax5select="cboRgtCd"]').ax5select({
	options: []
});

$('input:radio[name^="userRadio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function() {
	$('#btnExcel').prop('disabled', true)
	getTeamList();
	
	$('#cboTeam').bind('change', function() {
		$('#btnQry').trigger('click');
	});
	$('#cboRgtCd').bind('change', function() {
		$('#btnQry').trigger('click');
	});
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		userGrid.exportExcel('형상관리시스템전체사용자정보.xls');
	});
	// 조회
	$('#btnQry').bind('click', function() {
		getAllUserInfo();
	});
	// 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
	
	$("#txtSearch").bind("keypress", function(e){
		if(e.keyCode == 13){
			$('#btnQry').trigger('click');
		}
	});
	
//	getSysInfo();
//	getUserRGTCD402();
});

// 전체 사용자 조회
function getAllUserInfo() {
	var Option = '2';
	if($('#optAll').is(':checked')) {
		Option = 0;
	} else if($('#optActive').is(':checked')) {
		Option = 1;
	} 

	var selCboTeam = '00';
	var selCboRgtCd = '00';
	if(getSelectedIndex('cboTeam') > 0) selCboTeam = getSelectedVal('cboTeam').value;
	if(getSelectedIndex('cboRgtCd') > 0) selCboRgtCd = getSelectedVal('cboRgtCd').value;

	var data;
	data = new Object();
	data = {
		/*UserTxt 	: $("#txtSearch").val(),
		Cbo_Sys 	: '',*/
		Cbo_Team 	: selCboTeam,
		Cbo_Rgtcd 	: selCboRgtCd,
		Option		: Option,
		requestType	: 'getAllUserInfo'
	}
	
	$('[data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successGetAllUserInfo);
}

// 전체 사용자 조회 완료
function successGetAllUserInfo(data) {
	$(".loding-div").remove();
	userGridData = data;
	userGrid.setData(userGridData);
	$('#btnExcel').prop('disabled', false);
}

// 팀 리스트 가져오기
function getTeamList() {
	var data;
	data = new Object();
	data = {
		requestType	: 'getTeamList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successGetTeamList);
}

// 팀 리스트 가져오기 완료
function successGetTeamList(data) {
	cboTeamData = data;
	$('[data-ax5select="cboTeam"]').ax5select({
        options: injectCboDataToArr(cboTeamData, 'cm_deptcd' , 'cm_deptname')
	});
	
	getRgtCdList();
//	getAllUserInfo();
}

function getRgtCdList() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('RGTCD','ALL','N','2','')
	]);
	cboRgtCdData	= codeInfos.RGTCD;
	
	$('[data-ax5select="cboRgtCd"]').ax5select({
        options: injectCboDataToArr(cboRgtCdData, 'cm_micode' , 'cm_codename')
	});
}

// 모달 닫기
function popClose() {
	window.parent.allUserInfoModal.close();
}

//시스템 콤보 가져오기
function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userId,
		SecuYn : "N",
		SelMsg : "SEL",
		CloseYn : "N",
		ReqCd : ""
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

//시스템 콤보 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
}

//function getUserRGTCD402() {
//	var data = new Object();
//	data = {
//		requestType	: 'getUserRGTCD402',
//		UserId : userId
//	}
//	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successGetUserRGTCD402);
//}
//
//function successGetUserRGTCD402(data) {
//	cboSysCdData = data;
//	
//	$('[data-ax5select="cboDuty"]').ax5select({
//		options: injectCboDataToArr(cboSysCdData, 'cm_micode' , 'cm_codename')
//	});
//}