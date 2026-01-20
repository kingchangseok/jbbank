/**
 * [환경설정 > 디렉토리정책 TAB] 화면 기능정의
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

var dirGrid		= new ax5.ui.grid();

var dirGridData	 	= [];
var cboPathDivData	= [];

$('[data-ax5select="cboPathDiv"]').ax5select({
    options: []
});

$(document).ready(function() {
	getCodeInfo();
	getDirList();
	// 등록
	$('#btnReq').bind('click', function() {
		insertDirList();
	});
	// 폐기
	$('#btnDel').bind('click', function() {
		delDirList();
	});
	createGridView();
});

function createGridView() {
	dirGrid	= new ax5.ui.grid();

	dirGrid.setConfig({
	    target: $('[data-ax5grid="dirGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    showLineNumber: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	            clickDirGrid(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_codename", 	label: "디렉토리구분",  	width: '20%', align: 'left'},
	        {key: "cm_path", 		label: "디렉토리명",  		width: '40%', align: 'left' },
	        {key: "cm_downip", 		label: "배포서버IP",  		width: '20%' },
	        {key: "cm_downport", 	label: "PORT",  		width: '20%' },
	    ]
	});
	if (dirGridData != null && dirGridData.length > 0) {
		dirGrid.setData(dirGridData);
	}
	
}
// 디렉토리정책 리스트 가져오기
function getDirList() {
	var data = new Object();
	data = {
		requestType	: 'getTab3Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetDirList);
}

// 디렉토리정책 리스트 가져오기 완료
function successGetDirList(data) {
	dirGridData = data;
	dirGrid.setData(dirGridData);
	//그리드 정렬
	dirGrid.setColumnSort({cm_codename:{seq:0, orderBy:"asc"}});
}

// 등록
function insertDirList() {
	var txtPathName = $('#txtPathName').val().trim();
	var txtIp 		= $('#txtIp').val().trim();
	var txtPort 	= $('#txtPort').val().trim();
	
	if(getSelectedIndex('cboPathDiv') < 1) {
		dialog.alert('디렉토리구분을 선택하여 주십시오.', function(){});
		return;
	}

	if(txtPathName.length === 0 ) {
		dialog.alert('디렉토리를 입력하여 주십시오.', function(){});
		return;
	}
	var data = new Object();
	data = {
		pathcd 		: getSelectedVal('cboPathDiv').value,
		path 		: txtPathName,
		tip 		: txtIp,
		tport 		: txtPort,
		requestType	: 'addTab3Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successInsertDirList);
}
// 등록 완료
function successInsertDirList(data) {
	dialog.alert(data.retmsg, function(){});
	getDirList();
}

// 폐기
function delDirList() {
	var selIn = dirGrid.selectedDataIndexs;
	if(selIn.length === 0 ) {
		dialog.alert('폐기 대상을 선택해 주시기 바랍니다.');
		return;
	}
	
	var data = new Object();
	data = {
		pathcd 		: getSelectedVal('cboPathDiv').value,
		requestType	: 'delTab3Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successDeleteDelCycle);
}

// 폐기 완료
function successDeleteDelCycle(data) {
	dialog.alert(data.retmsg, function(){});
	getDirList();
}

// 디렉토리정책 리스트 클릭
function clickDirGrid(index) {
	var selItem = dirGrid.list[index];
	$('#txtIp').val(selItem.cm_downip);
	$('#txtPort').val(selItem.cm_downport);
	$('#txtPathName').val(selItem.cm_path);
	$('[data-ax5select="cboPathDiv"]').ax5select('setValue', selItem.cm_pathcd, true);
}

//디렉토리 구분 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfo('ECAMSDIR','SEL','N')]);
	cboPathDivData 	= codeInfos.ECAMSDIR;
	
	$('[data-ax5select="cboPathDiv"]').ax5select({
        options: injectCboDataToArr(cboPathDivData, 'cm_micode' , 'cm_codename')
	});
}