/**
 * 시스템상세정보 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-31
 * 
 */

var userName 	= window.parent.userName;			// 접속자 Name
var userId 		= window.parent.userId;				// 접속자 ID
var adminYN 	= window.parent.adminYN;			// 관리자여부
var userDeptName= window.parent.userDeptName;		// 부서명
var userDeptCd 	= window.parent.userDeptCd;			// 부서코드
var selectedSystem  = window.parent.selectedSystem;

var sysCd 	= null;	// 시스템정보 선택 코드
var sysInfo = null;	// 시스템 속성
var dirBase = null; // 기준서버
var sysInfoData     = null;   //시스템정보
var svrUsrGrid		= new ax5.ui.grid();
var accGrid			= new ax5.ui.grid();
var svrUsrGridData 	= null;
var accGridData 	= null;
var cboSvrData 		= null;
var cboOptions		= null;
var ulSvrInfoData	= [];

var selSw			= false;

/////////////////// 계정정보 화면 세팅 start////////////////////////////////////////////////
$('[data-ax5select="cboSvrUsr"]').ax5select({
    options: []
});


$('input.checkbox-usr').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

/////////////////// 계정정보 화면 세팅 end////////////////////////////////////////////////


$(document).ready(function(){
	if (window.parent.frmLoad4) createViewGrid();
});

function createViewGrid() {
	svrUsrGrid		= new ax5.ui.grid();
	accGrid			= new ax5.ui.grid();
	svrUsrGrid.setConfig({
	    target: $('[data-ax5grid="svrUsrGrid"]'),
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
	        {key: "cm_codename", 	label: "서버종류",  		width: '30%'},
	        {key: "cm_svrname", 	label: "서버명/OS",  		width: '30%'},
	        {key: "cm_svrip", 		label: "IP Address",  	width: '30%'},
	        {key: "cm_portno", 		label: "Port",  		width: '10%' }
	    ]
	});

	accGrid.setConfig({ 
	    target: $('[data-ax5grid="accGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	            clickAccGrid(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_codename", 	label: "서버구분",  	width: '15%', align: "center"},
	        {key: "cm_svrname", 	label: "서버명",  	width: '10%', align: "center"},
	        {key: "cm_svrip", 		label: "IP Address",width: '10%', align: "center"},
	        {key: "cm_portno", 		label: "Port",  	width: '8%',  align: "center"},
	        {key: "cm_jobname", 	label: "업무명",  	width: '15%', align: "center"},
	        {key: "cm_svrusr", 		label: "계정",  		width: '8%',  align: "center"},
	        {key: "cm_grpid", 		label: "그룹",  		width: '8%',  align: "center"},
	        {key: "cm_permission", 	label: "권한",  		width: '8%',  align: "center"},
	        {key: "cm_dbuser", 		label: "DB계정",  	width: '8%',  align: "center"},
	        {key: "cm_dbconn", 		label: "DB연결자", 	width: '10%', align: "center"}
	    ]
	});
	screenLoad();
	if (svrUsrGridData != null && svrUsrGridData.length > 0) {
		svrUsrGrid.setData(svrUsrGridData);
	}
	if (accGridData != null && accGridData.length > 0) {
		accGrid.setData(accGridData);
	}
}
function screenLoad() {
	selectedSystem  = window.parent.selectedSystem;
	if (selectedSystem != null) {
		var sysListInfoData;
		sysListInfoData = new Object();
		sysListInfoData = {
				clsSw : false,
				SysCd : selectedSystem.cm_syscd,
			requestType	: 'getSysInfo_List'
		}
		
		ajaxAsync('/webPage/ecmm/Cmm0200Servlet', sysListInfoData, 'json',successGetSysInfoList);
	}
}

//시스템 리스트
function successGetSysInfoList(data) {
	sysInfoData = data;
	//console.log(sysInfoData.length);
	if (sysInfoData.length != 1) {
		dialog.alert('시스템정보가 정확하지 않습니다. [닫기]를 클릭하여 이전화면에서 다시 진행하여 주시기 바랍니다.', function() {});
	} else {
		selectedSystem = sysInfoData[0];
		sysCd = selectedSystem.cm_syscd;
		sysInfo = selectedSystem.cm_sysinfo;
		dirBase = selectedSystem.cm_dirbase;
		// 업무별 서버관리
		if(sysInfo.substr(8,1) === '1') {
			getUlSvrInfo();
			$('#chkAllSvrJob').wCheck('disabled', false);
		} else  {
			$('#chkAllSvrJob').wCheck('disabled', true);
		}
		
		_promise(50,getCodeInfo())
			.then(function(){
				return _promise(50,$('#cboSvrUsr').trigger('change'));
			})
			.then(function() {
				return _promise(50,getSecuList());
			});
		
		
		$('#txtMode').keyup(function (event) { 
			var v = $(this).val();
			$(this).val(v.replace(/[^a-z0-9]/gi,''));
		});
		
		/////////////////////// 계정정보 버튼 event end////////////////////////////////////////////////
		$('#cboSvrUsr').bind('change',function(){
			
			if(svrUsrGridData !== null) $('#chkAllUsr').wCheck('check',false);
			
			if(sysInfo.substr(8,1) === '1') {
				// 사용업무 사용가능하게
			} else {
				// 사용업무 사용 불가능하게
			}
			
			var svrUsrInfoData = new Object();
			svrUsrInfoData = {
				SysCd	: sysCd,
				SvrCd 	: getSelectedVal('cboSvrUsr').value ,
				requestType	: 'getSvrInfo'
			}
			ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', svrUsrInfoData, 'json',successGetSvrUsrInfo);
	
		});
		
		// 계정정보 닫기 버튼 클릭 이벤트
		$('#btnExitUsr').bind('click', function() {
			//popClose();
		});
		
		// 계정정보 등록 버튼 클릭 이벤트
		$('#btnReqUsr').bind('click', function() {
			checkValUsr();
		});
		
		// 계정정보 폐기 버튼 클릭 이벤트
		$('#btnUsrClose').bind('click',function() {
			
			var selSvrIn= svrUsrGrid.selectedDataIndexs;
			var svrItem = null;
			var tmpJob 	= '';
			var svrArr	= [];
			if(selSvrIn.length === 0 ) {
				dialog.alert('폐기할 서버를 선택한 후 처리하시기 바랍니다.', function() {});
				return;
			}
			
			if(sysInfo.substr(8,1) === '1') {
				var checkSw = false;
				ulSvrInfoData.forEach(function( ulItem, index) {
					var id = ulItem.cm_jobcd;
					if($('#chkJob'+id).is(':checked')) {
						checkSw = true;
					}
				});
				
				if(!checkSw) {
					dialog.alert('폐기할 업무를 선택한 후 처리하시기 바랍니다.',function(){});
					return;
				}
			}
			
			selSvrIn.forEach(function(selIn, index) {
				svrItem = svrUsrGrid.list[selIn];
				svrItem.userId = userId;
				svrArr.push(svrItem);
			});
			
			if(sysInfo.substr(8,1) === '1') {
				ulSvrInfoData.forEach(function(jobItem, index) {
					var id = jobItem.cm_jobcd;
					if( $('#chkJob'+id).is(':checked')) {
						if(tmpJob.length > 0 ) tmpJob += ',';
						tmpJob +=  $('#chkJob'+id).val();
					}
				});
				
			} else {
				tmpJob = '****';
			}
			
			var svrUsrCloseData = new Object();
			svrUsrCloseData = {
				svrList		: svrArr,
				SysCd 		: sysCd,
				JobCd 		: tmpJob,
				requestType	: 'secuInfo_Close'
			}
			ajaxAsync('/webPage/ecmm/Cmm0200_SvrServlet', svrUsrCloseData, 'json',successCloseSvrUsr);
			
		});
		
		function successCloseSvrUsr(data) {
			dialog.alert('업무별 계정권한에 대한 폐기처리를 완료하였습니다.', function() {
				getSecuList();
			});
		}
		
		// 계정정보 조회버튼 클릭 이벤트
		$('#btnQryUsr').bind('click', function() {
			getSecuList();
		});
		
		$('#chkAllUsr').bind('change',function() {
			var checkSw = false;
			if($('#chkAllUsr').is(':checked')) checkSw = true;
			
			if(checkSw) {
				svrUsrGridData.forEach(function(svrUsrItem, index) {
					svrUsrGrid.select(index);
				});
			} else {
				svrUsrGridData.forEach(function(svrUsrItem, index) {
					svrUsrGrid.clearSelect(index);
				});
			}
		});
		
		// 사용업무 전체선택 클릭 이벤트
		$('#chkAllSvrJob').bind('change', function(e) {
			var id = '';
			var checkSw = false;
			if($('#chkAllSvrJob').is(':checked')) checkSw = true;
			
		});
	}
}

// 계정정보 등록 유효성 체크
function checkValUsr() {
	var jobSw 		= false;
	var dbSw		= false;
	var fileSw		= false;
	var svrSelIndex = svrUsrGrid.selectedDataIndexs;
	var txtSvrUsr 	= $('#txtSvrUsr').val().trim();
	var txtGroup 	= $('#txtGroup').val().trim();
	var txtMode 	= $('#txtMode').val().trim();
	var txtDbUsr 	= $('#txtDbUsr').val().trim();
	var txtDbPass 	= $('#txtDbPass').val().trim();
	var txtDbConn 	= $('#txtDbConn').val().trim();
	var svrArry		= [];
	var tmpJob		= '';
	
	if(svrSelIndex.length < 1) {
		dialog.alert('등록할 서버를 선택한 후 처리하시기 바랍니다.', function(){});
		return;
	}
	if (sysInfo.substr(8,1) == "1") {
		ulSvrInfoData.forEach(function(jobItem, index) {
			var id = jobItem.cm_jobcd;
			if( $('#chkJob'+id).is(':checked')) jobSw = true;
			console.log($('#chkJob'+id).is(':checked'));
			console.log('#chkJob'+id);
		});
		
		if(!jobSw) {
			dialog.alert('등록할 업무를 선택한 후 처리하시기 바랍니다.', function(){});
			return;
		}
	}
	
	svrUsrGrid.getList("selected").forEach(function(svrItem, index) {
		if(svrUsrGrid.getList("selected")[index].cm_svruse.substr(0,1) == '1')fileSw = true;
		if (svrUsrGrid.getList("selected")[index].cm_svruse.substr(2,1) == '1')dbSw = true;
	});
	
	if( fileSw &&  txtMode.length === 0) {
		dialog.alert('파일의 권한을 입력하여 주시기 바랍니다.', function(){});
		return;
	}
	
	if(dbSw) {
		if(txtDbUsr.length === 0) {
			dialog.alert('DB계정을 입력하여 주시기 바랍니다.', 	function(){});
			return;
		}
		if(txtDbPass.length === 0) {
			dialog.alert('DB비밀번호를 입력하여 주시기 바랍니다.', function(){});
			return;
		}
		if(txtDbConn.length === 0) {
			dialog.alert('DB연결자를 입력하여 주시기 바랍니다.', 	function(){});
			return;
		}
	}
	
	svrSelIndex.forEach(function(indexItem, index) {
		var selSvrItem = svrUsrGrid.list[indexItem];
		svrArry.push(selSvrItem);
	});
	
	if (sysInfo.substr(8,1) == "1") {
		ulSvrInfoData.forEach(function(jobItem, index) {
			var id = jobItem.cm_jobcd;
			if( $('#chkJob'+id).is(':checked')) {
				if(tmpJob.length > 0 ) tmpJob += ',';
				tmpJob +=  $('#chkJob'+id).val();
			}
		});
	} else {
		tmpJob = '****';
	}
	var etcData = new Object();
	etcData.cm_syscd 	= sysCd;
	etcData.cm_svrusr 	= txtSvrUsr;
	etcData.cm_grpid 	= txtGroup;
	etcData.cm_permission = txtMode;
	etcData.dbuser 	= txtDbUsr;
	etcData.dbpass 	= txtDbPass;
	etcData.dbconn 	= txtDbConn;
	etcData.jobcd 	= tmpJob;
	etcData.userId 	= userId;
	var usrReqData = new Object();
	usrReqData = {
		etcData		: etcData,
		svrList 	: svrArry,
		requestType	: 'secuInfo_Ins'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_SvrServlet', usrReqData, 'json',successUsrReq);
	etcData = null;
	usrReqData = null;

	txtSvrUsr 	= null;
	txtGroup 	= null;
	txtMode 	= null;
	txtDbUsr 	= null;
	txtDbPass 	= null;
	txtDbConn 	= null;
	svrArry		= null;
	tmpJob		= null;
}

function successUsrReq(data) {
	if(data === 'OK') {
		dialog.alert('계정권한에 대한 등록처리를 완료하였습니다.',function(){});
		getSecuList();
	} else {
		dialog.alert('계정정보 등록중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.\n' + data , function(){});
	}
}

function getSecuList() {
	var secuInfoData = new Object();
	secuInfoData = {
		SysCd		: sysCd,
		sysInfo 	: sysInfo,
		requestType	: 'getSecuList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_SvrServlet', secuInfoData, 'json',successGetSecuList);
	secuInfoData = null;
}

function successGetSecuList(data) {
	accGridData = data;
	accGrid.setData(accGridData);
}

/////////////////// 계정정보 버튼 function start////////////////////////////////////////////////
function popClose(){
	window.parent.parent.sysDetailModal.close();
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('SERVERCD','','N','3',''),
	]);
	cboSvrData = codeInfos.SERVERCD;
	
	cboOptions = [];
	$.each(cboSvrData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	
	$('[data-ax5select="cboSvrUsr"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
}

function successGetSvrUsrInfo(data) {
	svrUsrGridData = data;
	svrUsrGrid.setData(svrUsrGridData);
	var accSelIndex = accGrid.selectedDataIndexs;
	var accSelItem = accGrid.list[accSelIndex];
	if(selSw && accSelIndex.length !== 0) {
		svrUsrGridData.forEach(function(svrUsrItem, index) {
			if(svrUsrItem.cm_micode === accSelItem.cm_svrcd 
					&& svrUsrItem.cm_seqno === accSelItem.cm_seqno) {
				svrUsrGrid.select(index);
				selSw = false;
			}
		});
	}
	accSelItem = null;
}

// 사용업무 
function getUlSvrInfo() {
	var ulSvrInfoData = new Object();
	ulSvrInfoData = {
		UserId : userId,
		SelMsg : '',
		CloseYn :'N',
		SecuYn : 'N',
		sortCd : 'NAME',
		SysCd : sysCd,
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', ulSvrInfoData, 'json',successGetJobInfo_Rpt);
	ulSvrInfoData = null;
	etcData = null;
}

function successGetJobInfo_Rpt(data) {
	
	console.log('successGetJobInfo_Rpt',data);
	ulSvrInfoData = data;
	
//	$('#ulSvrInfo').empty();
//	var liStr = null;
//	var addId = null;
//	for(var i=0; i< ulSvrInfoData.length; i++) {
//		var svrinfoItem = ulSvrInfoData[i];
//		addId = svrinfoItem.cm_jobcd;
//		liStr  = '';
//		liStr += '<li class="list-group-item">';
//		liStr += '	<input type="checkbox" class="checkbox-svrinfo" id="chkJob'+addId+'" data-label="'+svrinfoItem.cm_jobname+'"  value="'+addId+'" />';
//		liStr += '</li>';
//		$('#ulSvrInfo').append(liStr);
//	}
	
	var $test1 = $('#ulSvrInfo');
	$test1.empty();
	var $frag = "";
	for(var i=0; i< ulSvrInfoData.length; i++) {
		var svrinfoItem = ulSvrInfoData[i];
		$frag += '<li class="list-group-item">';
		$frag += '	<input type="checkbox" class="checkbox-svrinfo" id="chkJob'+svrinfoItem.cm_jobcd+'" data-label="'+svrinfoItem.cm_jobname+'"  value="'+svrinfoItem.cm_jobcd+'" />';
		$frag += '</li>';
	}
	$test1.html($frag);
	$frag = null;
	$test1 = null;
	
	$('input.checkbox-svrinfo').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	addId = null;
	liStr = null;
}

function clickAccGrid(selIndex) {
	var selItem = accGrid.list[selIndex];
	selSw = true;
	
	$('#txtSvrUsr').val(selItem.cm_svrusr);
	$('#txtGroup').val(selItem.cm_grpid);
	$('#txtMode').val(selItem.cm_permission);
	$('#txtDbUsr').val(selItem.cm_dbuser);
	$('#txtDbConn').val(selItem.cm_dbconn);
	$('#txtDbPass').val(selItem.cm_dbpass);
	
	
	$('[data-ax5select="cboSvrUsr"]').ax5select('setValue',selItem.cm_svrcd,true);
	$('#cboSvrUsr').trigger('change');
	
	$('#chkAllSvrJob').wCheck('check',false);
	
	for(var i=0; i<ulSvrInfoData.length; i++) {
		if(selItem.cm_jobcd === ulSvrInfoData[i].cm_jobcd){
			var id = ulSvrInfoData[i].cm_jobcd;
			$('#chkJob'+id).wCheck('check',true);
			break;
		}
	}
	
	/*
	_promise(50, $('#chkAllSvrJob').trigger('change'))
		.then(function() {
			for(var i=0; i<ulSvrInfoData.length; i++) {
				if(selItem.cm_jobcd === ulSvrInfoData[i].cm_jobcd){
					var id = ulSvrInfoData[i].cm_jobcd;
					$('#chkJob'+id).wCheck('check',true);
					break;
				}
			}
		});
	*/
	selItem = null;
}
/////////////////// 계정정보 버튼 function end////////////////////////////////////////////////