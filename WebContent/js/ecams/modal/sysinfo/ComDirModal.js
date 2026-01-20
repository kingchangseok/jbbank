/**
 * 시스템상세정보 팝업 [공통디렉토리] 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-14
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var selectedSystem  = window.parent.selectedSystem;

var sysCd 	= selectedSystem.cm_syscd;	// 시스템정보 선택 코드

var dirGrid		= new ax5.ui.grid();
var dirGridData = null;

var cboDirData 		= null;
var cboOptions		= null;

$('[data-ax5select="cboDir"]').ax5select({
    options: []
});

dirGrid.setConfig({
    target: $('[data-ax5grid="dirGrid"]'),
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
            clickDirGrid(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_codename", 	label: "디렉토리구분", width: 100 , align: "left"	},
        {key: "cm_dirpath", 	label: "디렉토리",  	width: 220, align: "left"	},
        {key: "cm_shell", 		label: "실행파일",  	width: 80 , align: "left"	},
        {key: "cm_svrip", 		label: "서버IP",  	width: 120, align: "center"},
        {key: "cm_port", 		label: "Port",  	width: 70, 	align: "center"},
        {key: "cm_svrusr", 		label: "계정",  		width: 70, 	align: "center"},
        {key: "cm_svrpass", 	label: "비밀번호",  	width: 70, 	align: "center"},
    ]
});


$(document).ready(function(){
	sysCd = selectedSystem.cm_syscd;
	$('#txtSysMsg').val(sysCd + ' ' + selectedSystem.cm_sysmsg);
	$('#txtSysMsg').prop('disabled', true);
	getCodeInfo();
	getDirList();
	
	// PORT 숫자만 입력하도록 수정
	$("#txtPort").on("keyup", function(event) {
		$(this).val($(this).val().replace(/[^0-9]/g,""));
	});
	
	
	// 등록
	$('#btnReq').bind('click', function() {
		checkVal();
	});
	
	// 폐기
	$('#btnCls').bind('click', function() {
		closeDir();
	});
	
	// 조회
	$('#btnQry').bind('click', function() {
		getDirList();
	});
	
	// 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
	
});

// 공통 디렉토리 삭제
function closeDir() {
	var selIndex 	= dirGrid.selectedDataIndexs;
	var selItem		= null;
	
	if(selIndex.length === 0 ) {
		dialog.alert('삭제할 목록을 선택 후 눌러주시기 바랍니다.', function(){});
		return;
	}
	
	selItem		= dirGrid.list[selIndex];
	
	var data = new Object();
	var etcData = new Object();
	etcData.SysCd = sysCd;
	etcData.DirCd = selItem.cm_dircd;
	data = {
		etcData 	: etcData,
		requestType	: 'closeDir'
	}
	ajaxAsync('/webPage/modal/sysinfo/ComDirServlet', data, 'json',successCloseDir);
}

function successCloseDir(data) {
	dialog.alert('공통디렉토리정보 폐기처리를 완료하였습니다.', function(){
		$('#btnQry').trigger('click');
	});
}

// 공통디렉토리 등록
function checkVal(){
	var txtSvrIp 	= $('#txtSvrIp').val().trim();
	var txtPort 	= $('#txtPort').val().trim();
	var txtDir 		= $('#txtDir').val().trim();
	
	
	if(txtSvrIp.length === 0) {
		dialog.alert('서버IP를 입력하여 주시기 바랍니다.', function() {});
		return;
	}
	
	if(!checkIP(txtSvrIp)) {
		dialog.alert('정상적인 IP를 입력하여 주시기 바랍니다.', function() {});
		return;
	}
	
	if(txtPort.length === 0) {
		dialog.alert('Port를 입력하여 주시기 바랍니다.', function() {});
		return;
	}
	if(txtDir.length === 0) {
		dialog.alert('디렉토리를 입력하여 주시기 바랍니다.', function() {});
		return;
	}
	
	var etcData = new Object();
	etcData.cm_syscd 	= sysCd;
	etcData.cm_dircd 	= getSelectedVal('cboDir').value;
	etcData.cm_svrip 	= txtSvrIp;
	etcData.cm_port 	= txtPort;
	etcData.cm_dirpath 	= txtDir;
	etcData.cm_shell 	= $('#txtShell').val().trim();
	etcData.cm_svrusr 	= $('#txtUser').val().trim();
	etcData.cm_svrpass 	= $('#txtPass').val().trim();
	
	var data = new Object();
	data = {
		etcData 	: etcData,
		requestType	: 'insertDir'
	}
	ajaxAsync('/webPage/modal/sysinfo/ComDirServlet', data, 'json',successInsertDir);
 }

// 등록 완료
function successInsertDir(data) {
	if(data === 'OK') {
		dialog.alert('공통디렉토리정보 등록처리를 완료하였습니다.', function() {
			$('#btnQry').trigger('click');
		});
	}
}

// 공통 디렉토리 리스트 클릭
function clickDirGrid(index) {
	var selItem = dirGrid.list[index];
	
	$('#txtSvrIp').val(selItem.cm_svrip);
	$('#txtPort').val(selItem.cm_port);
	$('#txtUser').val(selItem.cm_svrusr);
	$('#txtPass').val(selItem.cm_svrpass);
	$('#txtShell').val(selItem.cm_shell);
	$('#txtDir').val(selItem.cm_dirpath);
}

// 공통 디렉토리 리스트 가져오기
function getDirList() {
	var data = new Object();
	data = {
		SysCd 		: sysCd,
		requestType	: 'getDirList'
	}
	ajaxAsync('/webPage/modal/sysinfo/ComDirServlet', data, 'json',successGetDirlist);
}

// 공통 디렉토리 리스트 가져오기 완료
function successGetDirlist(data) {
	dirGridData = data;
	dirGrid.setData(dirGridData);
}

// 디렉토리 콤보 박스 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('SYSDIR','','N')
		]);
	
	cboDirData 	= codeInfos.SYSDIR;
	
	cboOptions = [];
	$.each(cboDirData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	$('[data-ax5select="cboDir"]').ax5select({
        options: cboOptions
	});
}

// 닫기
function popClose(){
	window.parent.comDirModal.close();
}
