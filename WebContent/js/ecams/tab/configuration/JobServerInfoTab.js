/**
 * [환경설정 > 작업서버정보 TAB] 화면 기능정의
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

var workGrid		= new ax5.ui.grid();

var workGridData 	= [];
var cboJobDivData	= [];

$('[data-ax5select="cboJobDiv"]').ax5select({
    options: []
});

$('input.checkbox-workserver').wCheck({theme: 'square-classic red', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getCodeInfo();
	getWorkServerList();
	// 등록
	$('#btnReq').bind('click', function() {
		insertWorkServerList();
	});
	// 폐기
	$('#btnDel').bind('click', function() {
		delWorkServerList();
	});
	createGridView();
});

function createGridView() {
	
	workGrid = new ax5.ui.grid();
	
	workGrid.setConfig({
	    target: $('[data-ax5grid="workGrid"]'),
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
	            clickWorkServerGrid(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_codename", 	label: "작업구분",  		width: '25%', align: "left"},
	        {key: "cm_svrip", 		label: "IP Address",  	width: '25%', align: "left"},
	        {key: "cm_portno", 		label: "Port",  		width: '25%', align: "left"},
	        {key: "cm_svrusr", 		label: "계정",  			width: '25%', align: "left"},
	        /*{key: "cm_svrpass", 	label: "비밀번호",  		width: '20%', align: "left"}*/
	    ]
	});
	if (workGridData != null && workGridData.length > 0) {
		workGrid.setData(workGridData);
	}
}
// 작업서버정보 리스트 가져오기
function getWorkServerList() {
	var data = new Object();
	data = {
		requestType	: 'getTab4Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetDirList);
}

// 작업서버정보 리스트 가져오기 완료
function successGetDirList(data) {
	workGridData = data;
	workGrid.setData(workGridData);
}

// 등록
function insertWorkServerList() {
	var txtUser 	= $('#txtUser').val().trim();
	var txtPass 	= $('#txtPass').val().trim();
	var txtIp 		= $('#txtIp').val().trim();
	var txtPort 	= $('#txtPort').val().trim();
	
	if(getSelectedIndex('cboJobDiv') < 1) {
		dialog.alert('작업구분을 선택하여 주십시오.', function(){});
		return;
	}
	if(txtIp.length === 0 ) {
		dialog.alert('IP Address를 입력하여 주십시오.', function(){});
		return;
	}
	if(txtPort.length === 0 ) {
		dialog.alert('Port를 입력하여 주십시오.', function(){});
		return;
	}
	var data = new Object();
	data = {
		jobgb 		: getSelectedVal('cboJobDiv').value,
		tip 		: txtIp,
		tport 		: txtPort,
		tuserid 	: txtUser,
		tpwd 		: txtPass,
		stopyn		: $('#chkStop').is(':checked') ? 'Y' : 'N',
		requestType	: 'addTab4Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successInsertWorkServerList);
	data = null;
}

// 등록 완료
function successInsertWorkServerList(data) {
	dialog.alert(data.retmsg, function(){});
	getWorkServerList();
}

// 폐기
function delWorkServerList() {
	var selIn = workGrid.selectedDataIndexs;
	if(selIn.length === 0 ) {
		dialog.alert('폐기 대상을 선택해 주시기 바랍니다.');
		return;
	}
	
	var data = new Object();
	data = {
		jobgb 		: getSelectedVal('cboJobDiv').value,
		requestType	: 'delTab4Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successDelWorkServerList);
}

// 폐기 완료
function successDelWorkServerList(data) {
	dialog.alert(data.retmsg, function(){});
	getWorkServerList();
}

// 디렉토리정책 리스트 클릭
function clickWorkServerGrid(index) {
	var selItem = workGrid.list[index];
	
	$('#txtIp').val(selItem.cm_svrip);
	$('#txtPort').val(selItem.cm_portno);
	$('#txtUser').val(selItem.cm_svrusr);
	$('#txtPass').val(selItem.cm_svrpass);
	
	if(selItem.cm_stopmicode === 'Y') {
		$('#chkStop').wCheck('check', true);
	} else {
		$('#chkStop').wCheck('check', false);
	}
	
	$('[data-ax5select="cboJobDiv"]').ax5select('setValue', selItem.cm_gbncd, true);
}

//디렉토리 구분 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('SVPROC','SEL','N')
	]);
	cboJobDivData = codeInfos.SVPROC;
	
	$('[data-ax5select="cboJobDiv"]').ax5select({
        options: injectCboDataToArr(cboJobDivData, 'cm_micode' , 'cm_codename')
	});
}