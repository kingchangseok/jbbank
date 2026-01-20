/**
 * [사용자정보 > 사용자직무조회] 화면 기능정의
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

var userGrid	= new ax5.ui.grid();

var userGridData 	= null;

var cboSysCdData	= null;
var cboJobData		= null;
var cboRgtCdData	= null;
var cboUserData		= null;


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
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_userid", 		label: "사번",  		width: '10%'},
        {key: "cm_username",	label: "성명",  		width: '10%'},
        {key: "cm_sysmsg", 		label: "시스템",  	width: '20%', align: 'left'},
        {key: "cm_jobname", 	label: "업무",  		width: '10%', align: 'left'},
        {key: "cm_rgtcdname", 	label: "직무",  		width: '50%', align: 'left'},
    ]
});

$('[data-ax5select="cboSysCd"]').ax5select({
	options: []
});

$('[data-ax5select="cboJob"]').ax5select({
	options: []
});

$('[data-ax5select="cboRgtCd"]').ax5select({
	options: []
});

$('[data-ax5select="cboUser"]').ax5select({
	options: []
});

$(document).ready(function() {
	getSysInfo();
	getCodeInfo();
	// 사용자 엔터
	$('#txtUser').bind('keypress', function(evnet) {
		if(event.keyCode === 13) {
			getUserId();
		}
	});
	
	// 시스템 콤보 변경
	$('#cboSysCd').bind('change', function() {
		getJobInfo();
	});
	
	// 조회 클릭
	$('#btnQry').bind('click', function() {
		getAllUserRgtCd();
	});
	// 엑셀저장 클릭
	$('#btnExcel').bind('click', function() {
		userGrid.exportExcel('사용자직무조회.xls');
	});
	// 닫기 클릭
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 조회
function getAllUserRgtCd() {
	var sysCd = getSelectedVal('cboSysCd').value;
	var jobCd = getSelectedVal('cboJob').value;
	var rgtCd = getSelectedVal('cboRgtCd').value;
	var userId = null;
	
	if(sysCd === '00000' ) { 
		sysCd = '0000';
	}
	
	if(rgtCd === '****' || rgtCd === '00') {
		rgtCd = '0000';
	}
	
	
	if(cboUserData === null || cboUserData.length === 0 ) {
		userId = '';
	} else if(cboUserData != null &&  cboUserData.length > 0 && getSelectedVal('cboUser').value == '0000'){
		userId = '';
	}else {
		userId = getSelectedVal('cboUser').value;
	}

	$('[data-ax5grid="userGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	var data = new Object();
	data = {
		sysCd 		: sysCd,
		jobCd 		: jobCd,
		rgtCd 		: rgtCd,
		userId 		: userId,
		requestType	: 'getAllUserRGTCD'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successGetAllUserRgtCd);
}

// 조회 완료
function successGetAllUserRgtCd(data) {
	$(".loding-div").remove();
	userGridData = data;
	userGrid.setData(userGridData);
}

// 사용자 정보 가져오기
function getUserId() {
	var data = new Object();
	data = {
		UserName 	: $('#txtUser').val().trim(),
		requestType	: 'getUserId'
	}
	ajaxAsync('/webPage/ecmd/Cmd0300Servlet', data, 'json',successGetUserId);
}

// 사용자 정보 가져오기 완료
function successGetUserId(data) {
	cboUserData = data;
	$('[data-ax5select="cboUser"]').ax5select({
		options: injectCboDataToArr(cboUserData, 'cm_userid' , 'viewname')
	});
}

// 업무정보 가져오기
function getJobInfo() {
	var data = new Object();
	data = {
		UserID 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').value,
		SecuYn 		: 'Y',
		CloseYn 	: 'N',
		SelMsg 		: 'ALL',
		sortCd 		: 'NAME',
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetJobInfo);
}

// 업무정보 가져오기 완료
function successGetJobInfo(data) {
	cboJobData = data;
	if(data.length === 0 ) {
		$('[data-ax5select="cboJob"]').ax5select({
			options: [{'value' : '0000' , text : '전체'}]
		});
	} else {
		$('[data-ax5select="cboJob"]').ax5select({
			options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
		});
	}
}

// 시스템정보 가져오기
function getSysInfo() {
	var data = new Object();
	data = {
			UserId : userId,
			SecuYn : "Y",
			SelMsg : "ALL",
			CloseYn : "N",
			ReqCd : "",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

// 시스템정보 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	$('[data-ax5select="cboSysCd"]').ax5select({
        options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	$('#cboSysCd').trigger('change');
}

// 직무가져오기
function getCodeInfo() {

//	cboRgtCdData 	= fixCodeList(window.top.codeList['RGTCD'], 'ALL', 'cm_micode', 'ASC', 'N');
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('RGTCD','ALL','N')
		]);
	cboRgtCdData = codeInfos.RGTCD;
	
	$('[data-ax5select="cboRgtCd"]').ax5select({
        options: injectCboDataToArr(cboRgtCdData, 'cm_micode' , 'cm_codename')
	});
};

// 모달 닫기
function popClose() {
	window.parent.rgtCdModal.close();
}