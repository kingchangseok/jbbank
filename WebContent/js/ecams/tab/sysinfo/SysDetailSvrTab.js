/**
 * 시스템상세정보 팝업 [서버정보] 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-31
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var sysCd 	= null;	// 시스템정보 선택 코드
var sysInfo = null;	// 시스템 속성
var dirBase = null; // 기준서버

var svrInfoGrid		= new ax5.ui.grid();
var svrInfoGridData = null;

var cboSvrData 		= null;
var cboOsData 		= null;
var cboBufferData 	= null;
var svrInfoData		= null;
var cboOptions		= null;
var selectedSystem  = window.parent.selectedSystem;

var selSw			= false;

///////////////////// 서버정보 화면 세팅 start////////////////////////////////////////////////
$('[data-ax5select="cboSvr"]').ax5select({
    options: []
});

$('[data-ax5select="cboSvrUsr"]').ax5select({
    options: []
});

$('[data-ax5select="cboOs"]').ax5select({
    options: []
});

$('[data-ax5select="cboBuffer"]').ax5select({
    options: []
});

$('input.checkbox-IP').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
$('input.checkbox-IPC').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
/////////////////// 서버정보 화면 세팅 end////////////////////////////////////////////////

$(document).ready(function(){
	if (window.parent.frmLoad3) createViewGrid();
});

function createViewGrid() {
	svrInfoGrid	= new ax5.ui.grid();
	svrInfoGrid.setConfig({
	    target: $('[data-ax5grid="svrInfoGrid"]'),
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
	            clickSvrList(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_codename", 	label: "서버구분",		width: '10%' , align: "left"},
	        {key: "cm_svrname", 	label: "서버명",  	width: '8%' , align: "left"},
	        {key: "sysos", 			label: "OS",  		width: '5%'},
	        {key: "cm_svrip", 		label: "IP Address",width: '8%'},
	        {key: "cm_portno", 		label: "Port",  	width: '5%'},
	        {key: "cm_svrusr", 		label: "계정",  		width: '5%'},
	        {key: "cm_volpath", 	label: "Home_Dir",  width: '15%', align: "left" },
	        {key: "cm_dir", 		label: "Agent_Dir", width: '15%', align: "left" },
	        {key: "ab", 			label: "버퍼사이즈", 	width: '6%' , align: "left" },
	        {key: "agent", 			label: "장애",  		width: '4%'},
	        {key: "svrstop", 		label: "정지",  		width: '4%'},
	        {key: "svrinfo", 		label: "속성",  		width: '10%', align: "left"},
	    ]
	});
	screenLoad();
	if (svrInfoGridData != null && svrInfoGridData.length > 0) {
		svrInfoGrid.setData(svrInfoGridData);
	}
}
function screenLoad() {
//	$('#chkBase').wCheck('check',true);
	
//	if(selectedSystem.cm_sysinfo.substr(20,1) != '1') {
//		$('#txtSeq').prop( "disabled", true );
//		$('#txtSeq').css('display', 'none');
//	} else {
//		$('#txtSeq').prop( "disabled", false );
//	}
	
	selectedSystem  = window.parent.selectedSystem;
	if (selectedSystem != null) {
		sysCd = selectedSystem.cm_syscd;
		sysInfo = selectedSystem.cm_sysinfo;
		dirBase = selectedSystem.cm_dirbase;
		
		$('#dibChkBase').css('display', 'none');
		$('#lblAftIp').css('visibility','hidden');
		$('#txtAftIp').css('visibility','hidden');
		$('#lblSysMsg').text('시스템 : ' + selectedSystem.cm_syscd + ' ' + selectedSystem.cm_sysmsg);
		
		_promise(50,getCodeInfo())
			.then(function(){
				return _promise(50,getSvrInfoList());
			});
		
		
		$('#txtPort').keyup(function (event) { 
			var v = $(this).val();
			$(this).val(v.replace(/[^0-9]/g,""));
		});
		
//		$('#txtSeq').keyup(function (event) { 
//			var v = $(this).val();
//			$(this).val(v.replace(/[^0-9]/g,""));
//		});
		
		$('#chkAllSvr').bind('click',function() {
			clickChkAllSvr($('#chkAllSvr').is(':checked'));
		});

		$('#chkIp').bind('click',function() {
			$('#chkIpC').wCheck("check", false);
			if($('#chkIp').is(':checked')) {
				$('#lblAftIp').css('visibility','visible');
				$('#txtAftIp').css('visibility','visible');
			} 
			
			if(!$('#chkIp').is(':checked')){
				$('#lblAftIp').css('visibility','hidden');
				$('#txtAftIp').css('visibility','hidden');
			}
		});
		
		$('#chkIpC').bind('click',function() {
			$('#chkIp').wCheck("check", false);
			if($('#chkIpC').is(':checked')) {
				$('#lblAftIp').css('visibility','visible');
				$('#txtAftIp').css('visibility','visible');
			} 
			
			if(!$('#chkIpC').is(':checked')){
				$('#lblAftIp').css('visibility','hidden');
				$('#txtAftIp').css('visibility','hidden');
			}
		});
		
		// 등록 클릭
		$('#btnReq').bind('click', function() {
			checkSvrVal(1);
		});
		
		// 수정 클릭
		$('#btnUpdt').bind('click', function() {
			checkSvrVal(2);
		});
		
		// 폐기 클릭
		$('#btnCls').bind('click', function() {
			var selIndex = svrInfoGrid.selectedDataIndexs;
			if(selIndex.length < 1) {
				dialog.alert('폐기 그리드를 선택해주세요.', function(){});
				return;
			}
			
			closeSvr();
		});
		
		// 조회 클릭
		$('#btnQry').bind('click', function() {
			getSvrInfoList();
		});
		
		// 엑셀저장 클릭
		$('#btnExl').bind('click',function () {
			svrInfoGrid.exportExcel('['+selectedSystem.cm_sysmsg+']시스템상세정보.xls');
		});
	}
}

/////////////////////// 서버정보 버튼 function start////////////////////////////////////////////////
function closeSvr() {
	var selIndex 	= svrInfoGrid.selectedDataIndexs;
	var selSvrItem 	= svrInfoGrid.list[selIndex];
	var txtSvrIp 	= $('#txtSvrIp').val().trim();
	var txtUser 	= $('#txtUser').val().trim();
	var tmpSeq		= '';
	
	if(selIndex.length < 1) {
		dialog.alert('수정할 그리드를 선택해주세요.', function(){});
		return;
	}
	
	if(txtSvrIp.length === 0 ) {
		dialog.alert('서버IP를 입력하여 주시기 바랍니다.', function(){});
		return;
	}
	
	if(txtUser.length === 0 ) {
		dialog.alert('계정을 입력하여 주시기 바랍니다.', function(){});
		return;
	}
	
	if(selSvrItem.cm_svrcd == getSelectedVal('cboSvr').value 
			&& selSvrItem.cm_svrip == txtSvrIp
			&& selSvrItem.cm_svrusr == txtUser) {
		tmpSeq = selSvrItem.cm_seqno;
	}
	
	if(tmpSeq.length <= 0 ) {
		dialog.alert('선택한 그리드의 정보와 현재 정보가 다릅니다. 다시 선택 후 눌러주세요.',function(){});
		return;
	}
	
	var svrCloseData = new Object();

	svrCloseData = {
		SysCd : sysCd,
		UserId : userId,
		SvrCd : getSelectedVal('cboSvr').value,
		SeqNo : tmpSeq,
		requestType	: 'svrInfo_Close'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_SvrServlet', svrCloseData, 'json',successSvrClose);
	etcData = null;
	svrCloseData = null;
}

function successSvrClose(data) {
	dialog.alert('시스템상세정보 폐기처리를 완료하였습니다.',function(){getSvrInfoList();});
}

function checkSvrVal(gbnCd) {
	if(gbnCd === 2 || $('#chkIpC').is(':checked')) {
		var selIndex = svrInfoGrid.selectedDataIndexs;
		if(selIndex.length < 1) {
			dialog.alert('수정할 그리드를 선택하여 주시기 바랍니다.', function(){});
			return;
		}
		if ($('#chkIpC').is(':checked')) gbnCd = 1;
	}
	var txtSvrName 	= $('#txtSvrName').val().trim();
	var txtSvrIp 	= $('#txtSvrIp').val().trim();
	var txtPort 	= $('#txtPort').val().trim();
//	var txtSeq 		= $('#txtSeq').val().trim();
	var txtHome 	= $('#txtHome').val().trim();
	var txtDir 		= $('#txtDir').val().trim();
	var txtUser 	= $('#txtUser').val().trim();
	var txtAftIp	= $('#txtAftIp').val().trim();
	var rquestType	= '';
	
	if(txtSvrName.length === 0 ) {
		dialog.alert('서버명을 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	if(txtSvrIp.length === 0 ) {
		dialog.alert('서버IP를 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	if(txtPort.length === 0 ) {
		dialog.alert('Port를 입력하여 주시기 바랍니다.',function(){});
		return;
	}

//	if(selectedSystem.cm_sysinfo.substr(20,1) === '1') {
//		if(txtSeq.length === 0 ) {
//			dialog.alert('그룹을 입력하여 주시기 바랍니다.',function(){});
//			return;
//		}
//	} else {
		txtSeq = "1";
//	}
	
	if(txtHome.length === 0 ) {
		dialog.alert('Home-Dir을 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	if(txtDir.length === 0 ) {
		dialog.alert('형상관리설치디렉토리(Agent-Dir)을 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	if(txtUser.length === 0 ) {
		dialog.alert('계정을 입력하여 주시기 바랍니다.',function(){});
		return;
	}

	if ($('#chkIp').is(':checked') && $('#chkIpC').is(':checked')) {
		dialog.alert("IP변경과 IP변경하여 복사 두가지 다 체크할 수 없습니다.");
		return;
	}
	
	if($('#chkIpC').is(':checked') || $('#chkIp').is(':checked')) {
		if(txtAftIp.length === 0) {
			dialog.alert('변경할 IP를 입력하여 주시기 바랍니다.',function(){});
			return;
		}
		
		if(txtAftIp === txtSvrIp) {
			dialog.alert('변경전/후 IP가 동일합니다.',function(){});
			return;
		}
	}
	
	var svrReqInfoData 	= new Object();
	var svrReqInfo 		= new Object();
	var svrUser			= '';
	if(gbnCd != 1 || $('#chkIpC').is(':checked')) {
		var selIndex = svrInfoGrid.selectedDataIndexs;
		var selSvrItem = svrInfoGrid.list[selIndex];
		svrReqInfo.cm_svrcd = selSvrItem.cm_svrcd;
		svrReqInfo.cm_seqno = selSvrItem.cm_seqno;
	} else {
		svrReqInfo.cm_svrcd = getSelectedVal('cboSvr').value;
	}
	
	svrReqInfo.cm_sysos 	= getSelectedVal('cboOs').value;
	svrReqInfo.cm_buffsize 	= getSelectedVal('cboBuffer').value;
	
	svrReqInfo.gbncd 		= String(gbnCd);
	svrReqInfo.cm_syscd 	= sysCd;
	svrReqInfo.cm_svrname 	= txtSvrName;
	svrReqInfo.cm_svrip 	= txtSvrIp;
	svrReqInfo.cm_portno 	= txtPort;
//	svrReqInfo.cm_prcseq	= txtSeq;
	svrReqInfo.cm_svrusr 	= txtUser;
	svrReqInfo.cm_volpath 	= txtHome;
	svrReqInfo.cm_dir 		= txtDir;
	svrReqInfo.basesvr 		= dirBase;
	svrReqInfo.cm_ftppass 	= $('#txtPass').val().trim();
	//svrReqInfo.cm_backdir 	= $('#txtTmp').val().trim();
	
	if($('#chkErr').is(':checked'))  svrReqInfo.cm_agent = 'ER';
	else svrReqInfo.cm_agent = '';
	if($('#chkStop').is(':checked')) svrReqInfo.cm_svrstop = 'Y';
	else svrReqInfo.cm_svrstop = 'N';
	
	
	for(var i=0; i<svrInfoData.length; i++) {
		svrUser += $('#chkSvrName' + Number(svrInfoData[i].cm_micode)).is(':checked') ? '1' : '0';
	}
	
//	if(svrReqInfo.cm_svrcd === dirBase) {
//		if($('#chkBase').is(':checked')) svrReqInfo.cm_newflag = 'Y';
//		else svrReqInfo.cm_newflag = 'N';
//	} else {
//		svrReqInfo.cm_newflag = 'N';
//	}
	
//	if($('#chkBase').is(':checked')) svrReqInfo.cm_newflag = 'Y';
//	else svrReqInfo.cm_newflag = 'N';
	
	if( $('#chkIpC').is(':checked') || $('#chkIp').is(':checked')) {
		svrReqInfo.aftip = txtAftIp;
		svrReqInfo.gbncd = '3';
	}
	if($('#chkIpC').is(':checked')) {
		rquestType = 'svrInfo_Ins_copy';
	} else if ($('#chkIp').is(':checked')){
		rquestType = 'svrInfo_Ins_updt';
	}else {
		rquestType = 'svrInfo_Ins';
	}
	svrReqInfo.cm_svruse = svrUser;
	svrReqInfo.cm_editor = userId;
	
	svrReqInfoData = {
			etcData	: svrReqInfo,
		requestType	: rquestType
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_SvrServlet', svrReqInfoData, 'json',successSvrIn);
	svrReqInfo = null;
	svrReqInfoData = null;
};
/*
function successSvrCopy(data) {
	if(data != 'OK') {
		dialog.alert(data,function(){});
	} else {
		dialog.alert('서버 IP변경하여 복사처리를 완료하였습니다.',function(){getSvrInfoList();});
	}
	
}*/
function successSvrIn(data) {
	if(data != 'OK') {
		dialog.alert(data,function(){});
	} else {
		dialog.alert('서버정보 등록처리를 완료하였습니다.',function(){getSvrInfoList();});
	}
}


function getSvrInfoList() {
	var svrInfoStr = '';
	svrInfoData.forEach(function(svrItem,index) {
		if(svrInfoStr.length > 0) svrInfoStr += ',';
		svrInfoStr += svrItem.cm_codename;
	});
	var svrInfo = new Object();
	svrInfo = {
		SysCd	: sysCd,
		SvrInfo	: svrInfoStr,
		requestType	: 'getSvrList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_SvrServlet', svrInfo, 'json',successGetSvrInfoList);
	svrInfo = null;
}

function successGetSvrInfoList(data) {
	svrInfoGridData = data;
	svrInfoGrid.setData(svrInfoGridData);
}

function popClose(){
	window.parent.parent.sysDetailModal.close();
}

function clickSvrList(selIndex) {
	var selItem = svrInfoGrid.list[selIndex];
	var svruse 	= selItem.cm_svruse;
	
	// 서버 콤보 채인지 이벤트 추가 해야함
	$('[data-ax5select="cboSvr"]').ax5select('setValue',selItem.cm_svrcd,true);
	$('[data-ax5select="cboOs"]').ax5select('setValue',selItem.cm_sysos,true);
	var i=0;
	for(i=0; i<cboBufferData.length; i++) {
		if(cboBufferData[i].cm_codename === selItem.ab) {
			$('[data-ax5select="cboBuffer"]').ax5select('setValue', cboBufferData[i].cm_micode, true);
			break;
		}
	}
	
//	if(selItem.cm_svrcd == dirBase && selItem.cm_cmpsvr == 'Y') {
//		$('#chkBase').wCheck('check',true);
//	} else {
//		$('#chkBase').wCheck('check',false);
//	}

//	if(selItem.cm_svrcd === '01') {
//		$('#dibChkBase').css('display', '');
//	} else {
//		$('#dibChkBase').css('display', 'none');
//	}
	
//	if(selItem.cm_newflag == 'Y') {
//		$('#chkBase').wCheck('check',true);
//	} else {
//		$('#chkBase').wCheck('check',false);
//	}
	
	if(selItem.agent === '정상') {
		$('#chkErr').wCheck('check', false);
	} else {
		$('#chkErr').wCheck('check', true);
	}
	
	if(selItem.svrstop === 'NO') {
		$('#chkStop').wCheck('check', false);
	} else {
		$('#chkStop').wCheck('check', true);
	}
	
	$('#txtSvrName').val(selItem.cm_svrname);
	$('#txtSvrIp').val(selItem.cm_svrip);
	$('#txtPort').val(selItem.cm_portno);
	
//	$('#txtSeq').val(selItem.cm_prcseq);
	
	/*
	if(selectedSystem.cm_sysinfo.substr(20,1) === '1') {
	} else {
		$('#txtSeq').val(selItem.cm_grpno);
		$('#txtSeq').prop( "disabled", true );
		$('#txtSeq').css('display', 'none');
	}*/
	
	$('#txtUser').val(selItem.cm_svrusr);
	$('#txtPass').val(selItem.cm_ftppass);
	$('#txtHome').val(selItem.cm_volpath);
	$('#txtDir').val(selItem.cm_dir);
	//$('#txtTmp').val(selItem.cm_backdir);
	
	clickChkAllSvr(false);
	
	for(i=0; i<svruse.length; i++) {
		if(svruse.substr(i,1) === '1') {
			$('#chkSvrName'+ (i+1) ).wCheck('check',true);
		}
	}
	selItem = null;
}

function clickChkAllSvr(checked){
	var i=0;
	var data_len = svrInfoData.length;
//	if(checked) {
//		for(i=0; i<data_len; i++) {
//			$('#chkSvrName'+ (i+1) ).wCheck('check',true);
//		}
//	} else {
//		for(i=0; i<data_len; i++) {
//			$('#chkSvrName'+ (i+1) ).wCheck('check',false);
//		}
//	}
	for(i=0; i<data_len; i++) {
		$('#chkSvrName'+ (i+1) ).wCheck('check',checked);
	}
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('SERVERCD','','N'),
		new CodeInfoOrdercd('SYSOS','','N', '3', ''),
		new CodeInfoOrdercd('SVRINFO','','N', '3', ''),
		new CodeInfoOrdercd('BUFFSIZE','','N', '3', ''),
	]);
	cboSvrData 		= codeInfos.SERVERCD;
	cboOsData		= codeInfos.SYSOS;
	svrInfoData 	= codeInfos.SVRINFO;
	cboBufferData 	= codeInfos.BUFFSIZE;
	
	cboOptions = [];
	$.each(cboSvrData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	$('[data-ax5select="cboSvr"]').ax5select({
        options: cboOptions
	});
	
	$('[data-ax5select="cboSvrUsr"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
	$.each(cboOsData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	$('[data-ax5select="cboOs"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
	$.each(cboBufferData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	$('[data-ax5select="cboBuffer"]').ax5select({
        options: cboOptions
	});
	
	
	$('#ulSyrInfo').empty();
	var liStr = '';
	var addId = null;
	var $test1 = $('#ulSyrInfo');
	var $frag = "";
	
	svrInfoData.forEach(function(svrInfoItem, sysInfoIndex) {
		addId = Number(svrInfoItem.cm_micode);
		$frag += '<li class="list-group-item dib width-50">';
		$frag += '	<input type="checkbox" class="checkbox-svrInfo" id="chkSvrName'+addId+'" data-label="'+svrInfoItem.cm_codename+ " [ " +svrInfoItem.cm_micode+ " ]" + '"  value="'+svrInfoItem.cm_micode+'" />';
		$frag += '</li>';
	});
	$test1.html($frag);
	$frag = null;
	$test1 = null;
	
	$('input.checkbox-svrInfo').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	addId = null;
	liStr = null;
}
///////////////////// 서버정보 버튼 function end////////////////////////////////////////////////
