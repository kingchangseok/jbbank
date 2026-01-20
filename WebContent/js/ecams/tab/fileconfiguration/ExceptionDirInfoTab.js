/**
 * [환경설정 > 예외디렉토리정보 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-01
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var dirGrid = new ax5.ui.grid();
var dirEtcGrid = new ax5.ui.grid();

var dirGridData 	= [];
var dirEtcGridData 	= [];

dirGrid.setConfig({
    target: $('[data-ax5grid="dirGrid"]'),
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
        {key: "cm_sysmsg", 	label: "시스템",   width: '25%', align: "left"},
        {key: "cm_dirpath",	label: "디렉토리",  width: '75%', align: "left"},
    ]
});

dirEtcGrid.setConfig({
    target: $('[data-ax5grid="dirEtcGrid"]'),
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
        {key: "cm_sysmsg", 	label: "시스템",   width: '25%', align: "left"},
        {key: "cm_dirpath",	label: "디렉토리",  width: '75%', align: "left"},
    ]
});




$(document).ready(function() {
	getEtcDir();
	
	// 디렉토리 엔터
	$('#txtEtcDir').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			getSysDir();
		}
	});
	// 예외디렉토리 등록
	$('#btEtcReq').bind('click', function() {
		setEtcDir();
	});
	// 예외디렉토리 삭제
	$('#btEtcDel').bind('click', function() {
		delEtcDir();
	});
});

// 예외디렉토리 삭제
function delEtcDir() {
	var selIn = dirEtcGrid.selectedDataIndexs;
	var delList	= [];
	if(selIn.length === 0 ) {
		dialog.alert('삭제할 대상을 선택한 후 진행하시기 바랍니다.', function() {});
		return;
	}
	
	selIn.forEach(function(item, index) {
		delList.push(dirEtcGrid.list[item]);
	});
	
	var data = new Object();
	data = {
		UserId 		: userId,
		gbnCd 		: 'D',
		dirList 	: delList,
		requestType	: 'setEtcDir'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json',successDelEtcDir);
}
//예외디렉토리 삭제 완료
function successDelEtcDir(data) {
	if(data !== 'OK') {
		dialog.alert('예외 디렉토리 삭제중 오류가 발생하였습니다.', function() {});
	} else {
		dialog.alert('예외 디렉토리 [삭제] 정상적으로 완료되었습니다.', function() {});
	}
	getSysDir();
	getEtcDir();
	
}

// 예외 디렉토리 등록
function setEtcDir() {
	var selIn = dirGrid.selectedDataIndexs;
	var dirList	= [];
	if(selIn.length === 0 ) {
		dialog.alert('등록할 대상을 선택한 후 진행하시기 바랍니다.', function() {});
		return;
	}
	
	selIn.forEach(function(item, index) {
		dirList.push(dirGrid.list[item]);
	});
	
	var data = new Object();
	data = {
		UserId 		: userId,
		gbnCd 		: 'I',
		dirList 	: dirList,
		requestType	: 'setEtcDir'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json',successSetEtcDir);
}
// 예외디렉토리 등록 완료
function successSetEtcDir(data) {
	if(data !== 'OK') {
		dialog.alert('예외 디렉토리 등록중 오류가 발생하였습니다.', function() {});
	} else {
		dialog.alert('예외 디렉토리 [등록] 정상적으로 완료되었습니다.', function() {});
	}
	getSysDir();
	getEtcDir();
}

// 예외디렉토리 리스트 가져오기
function getEtcDir() {
	var data = new Object();
	data = {
		UserId 		: userId,
		requestType	: 'getEtcDir'
	}
	ajaxAsync('//webPage/ecmm/Cmm2900Servlet', data, 'json',successGetEtcDir);
}
// 예외 디렉토리 리스트 가져오기 완료
function successGetEtcDir(data) {
	dirEtcGridData = data;
	dirEtcGrid.setData(dirEtcGridData);
}
// 디렉토리 조회
function getSysDir() {
	var txtEtcDir = $('#txtEtcDir').val().trim();
	
	if(txtEtcDir.length === 0 ) {
		dialog.alert('조회 할 디렉토리를 입력후 엔터를 눌러주세요.', function() {});
		return;
	}
	var data = new Object();
	data = {
		UserId 		: userId,
		dirPath 	: txtEtcDir,
		requestType	: 'getSysDir'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json',successGetSysDir);
}

// 디렉토리 조회 완료
function successGetSysDir(data) {
	dirGridData = data;
	dirGrid.setData(dirGridData);
}
