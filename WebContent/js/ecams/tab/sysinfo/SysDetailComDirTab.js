/**
 * 시스템상세정보 팝업 [공통디렉토리] 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-14
 * 
 */

var userId 			= window.parent.userId;			// 접속자 ID
var selectedSystem  = window.parent.selectedSystem;

var dirGrid			= new ax5.ui.grid();

$('[data-ax5select="cboDir"]').ax5select({
    options: []
});

$(document).ready(function(){
	if (window.parent.frmLoad6) createViewGrid();
});

function createViewGrid() {
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
	        {key: "cm_codename", 	label: "디렉토리구분", 	width: '15%' , 	align: "left"	},
	        {key: "cm_dirpath", 	label: "디렉토리",  	width: '20%', 	align: "left"	},
	        {key: "cm_shell", 		label: "실행파일",  	width: '20%', 	align: "left"	},
	        {key: "cm_svrip", 		label: "서버IP",  	width: '15%', 	align: "center"},
	        {key: "cm_port", 		label: "Port",  	width: '10%', 	align: "center"},
	        {key: "cm_svrusr", 		label: "계정",  		width: '10%', 	align: "center"},
	        {key: "cm_svrpass", 	label: "비밀번호",  	width: '10%', 	align: "center"},
	    ]
	});
	screenLoad();
}

function screenLoad() {

	getCodeInfo();
	
	if (selectedSystem != null) {
		$('#txtSysMsg').val(selectedSystem.cm_syscd + ' ' + selectedSystem.cm_sysmsg);
		$('#txtSysMsg').prop('disabled', true);
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
	} 
	
}
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
	data = {
		SysCd 		: selectedSystem.cm_syscd,
		DirCd		: selItem.cm_dircd,
		userId		: userId,
		requestType	: 'dirInfo_Close'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_DirServlet', data, 'json',successCloseDir);
	data = null;
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
	etcData.userId		= userId;
	etcData.cm_syscd 	= selectedSystem.cm_syscd;
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
		requestType	: 'dirInfo_Ins'
	}
	etcData = null;
	ajaxAsync('/webPage/ecmm/Cmm0200_DirServlet', data, 'json',successInsertDir);
	data = null;
	
	txtDir = null;
	txtPort = null;
	txtSvrIp = null;
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
	$('[data-ax5select="cboDir"]').ax5select("setValue", selItem.cm_dircd, true);
	selItem = null;
}

// 공통 디렉토리 리스트 가져오기
function getDirList() {
	var data = new Object();
	data = {
		SysCd 		: selectedSystem.cm_syscd,
		requestType	: 'getDirList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_DirServlet', data, 'json',successGetDirlist);
	data = null;
}

// 공통 디렉토리 리스트 가져오기 완료
function successGetDirlist(data) {
	dirGrid.setData(data);
}

// 디렉토리 콤보 박스 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('SYSDIR','','N')
	]);
	
	cboDirData 	= codeInfos.SYSDIR;
	
	$('[data-ax5select="cboDir"]').ax5select({
		options: injectCboDataToArr(cboDirData, 'cm_micode', 'cm_codename')
	});
}

// 닫기
function popClose(){
	window.parent.comDirModal.close();
}