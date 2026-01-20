/**
 * 시스템정보 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-05-16
 */

//var userName 	= window.top.userName;
var userId 			= window.parent.userId;
var selectedSystem  = window.parent.selectedSystem;

var jobGrid				= new ax5.ui.grid();
var jobModal 			= new ax5.ui.modal();
var gitJobModal 		= new ax5.ui.modal();
var jobPluginCkModal    = new ax5.ui.modal();

var cboSysGbData 	= [];	// 시스템유형콤보
var sysInfoData		= [];	// 시스템 속성 UL list
var cboSvrCdData	= [];	// 기준서버구분콤보
var jobGridData 	= [];	// 업무 그리드. 해당 시스템에 해당하는 job 리스트

var stFullDate 		= null;	// 중단시작일시
var edFullDate 		= null;	// 중단종료일시

var selSysCd        = null;
var addFg1 			= false;
var addFg2 			= false;
var closeModifyFg 	= false;
var openSw          = false;
var gitSw           = false;
var cm_typecd 		= "2";

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$('[data-ax5select="cboSysGb"]').ax5select({
    options: []
});

$('[data-ax5select="cboSvrCd"]').ax5select({
    options: []
});

/** jquery time picker 시간 포멧
 * 
 * 1. HH:mm > 01:01
 * 2. h:mm p > 1:00 AM or 1:00 PM
 */
$('#timeDeploy').timepicker({
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: true,
    scrollbar: true
});
$('#timeDeployE').timepicker({
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: true,
    scrollbar: true
});

/*$('#txtTime').timepicker({
	direction:'top',
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: false,
    scrollbar: true
});*/

$(document).ready(function(){
	if (window.parent.frmLoad1) createViewGrid();
	//createViewGrid();
	
	// 프로세스제한 숫자만 입력하도록 수정
	$("#txtPrcCnt").on("keyup", function(event) {
		$(this).val($(this).val().replace(/[^0-9]/g,""));
	});
	
	// 적용시간 숫자만 입력하도록 수정
	/*$("#txtTime").on("keyup", function(event) {
		$(this).val($(this).val().replace(/[^0-9]/g,""));
	});*/
	
	$(".checkbox-sysInfo").bind('click', function(e) {
	    clickSysinfo(this.value);
	});
	
	// 업무 엔터
	$('#txtJobname').bind('keypress', function(event) {
		var txtJobName = '';
		if(event.keyCode === 13 ) {
			//jobGrid.clearSelect();
			txtJobName = $('#txtJobname').val().trim();
			console.log('jobGridData',jobGridData);
			
			if(txtJobName.length === 0) {
				jobGrid.select(0);
			} else {
				var tmpJobList = jobGrid.getList();
				console.log('tmpJobList',tmpJobList);
				for(var i=0; i<tmpJobList.length; i++) {
					if(tmpJobList[i].cm_jobname.indexOf(txtJobName) >= 0 ) {
						//jobGrid.select(i,{selectedClear:false});
						jobGrid.focus(i);
						break;
					}
				}
			}
		}
	});
	
	// 시스템코드 엔터
	$('#txtSysCd').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			sysDataFilter();
		}
	});
	
	//삭제 버튼 클릭시
	$('#btnDel').bind('click',function() {
		if(selectedSystem == null) return;
		
		confirmDialog.confirm({
			msg: '시스템정보를 폐기처리하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				
				var infodata = new Object();
				infodata = {
					SysCd	:	selectedSystem.cm_syscd,
					UserId	:	userId,
					requestType	: 'sysInfo_Close'
				}
				ajaxAsync('/webPage/ecmm/Cmm0200Servlet', infodata, 'json',successSysClose);
				infodata = null;
				sysInfo = null;
			}
		});
	});
	
	// 등록 버튼 클릭시
	$('#btnAdd').bind('click',function() {
		sysValidationCheck();
	});
	
	// 업무등록
	$('#btnJob').bind('click', function() {
		setTimeout(function() {
			jobModal.open({
				width: 800,
				height: 650,
				iframe: {
					method: "get",
					url: "../../modal/sysinfo/JobModal.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						getSysJobInfo('');
						//$('#btnQry').trigger('click');
					}
				}
			}, function () {
			});
		}, 200);
	});
	
	// 예외업무
	$('#btnExp').bind('click', function() {
		if(selectedSystem == null) return;
		
        selSysCd = selectedSystem.cm_syscd;
		setTimeout(function() {
			jobPluginCkModal.open({
				width: 505,
				height: 615,
				iframe: {
					method: "get",
					url: "../../modal/sysinfo/JobPluginCkModal.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
					}
				}
			}, function () {
			});
		}, 200);
	});
	
	// GITLAB정보연결
	$('#btnGit').bind('click', function() {
		if(selectedSystem == null) return;
		
        selSysCd = selectedSystem.cm_syscd;
		setTimeout(function() {
			gitJobModal.open({
				width: 1000,
				height: 650,
				iframe: {
					method: "get",
					url: "../../modal/sysinfo/GitJobModal.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
					}
				}
			}, function () {
			});
		}, 200);
	});
	
	
});

function createViewGrid() {
	jobGrid = new ax5.ui.grid();
	jobGrid.setConfig({
		target: $('[data-ax5grid="jobGrid"]'),
		sortable: true, 
		multiSort: true,
		showRowSelector: true,
		multipleSelect : true,
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
			{key: "cm_jobcd", 	label: "업무코드",  	width: '40%', align: 'left'},
			{key: "cm_jobname",	label: "업무명",		width: '60%', align: 'left'}
		]
	});
	screenLoad();
}

function screenLoad() {
	getSysCodeInfo();
	screenInit();
	baseReload();
	dateInit();
}

function baseReload() {
	selectedSystem = window.parent.selectedSystem;
	gitSw = false;
	$('#btnGit').css('display','none');
	var selSysCd = "";
	if (selectedSystem != null) {
		selSysCd = selectedSystem.cm_syscd;
		setSysInfo();
		if (selectedSystem.cm_sysinfo.substr(18,1)=='1') {
			gitSw = true;
			$('#btnGit').css('display','inline-block');
		}
	} else {
		openSw = true;
		var nowDate = getDate('DATE',0);
		$("#datSysOpen").val(nowDate);
		$("#datScmOpen").val(nowDate);
		$("#btnJobRule").hide();
	}
	getSysJobInfo(selSysCd);
}

// datepicker 날짜 오늘날짜로 초기화
function dateInit() {
	var datStDate 		= new ax5.ui.picker();
	var datEdDate 		= new ax5.ui.picker();
	var datSysOpen 		= new ax5.ui.picker();
	var datScmOpen 		= new ax5.ui.picker();
	datStDate.bind(defaultPickerInfo('datStDate', 'top'));
	datEdDate.bind(defaultPickerInfo('datEdDate', 'top'));
	datSysOpen.bind(defaultPickerInfo('datSysOpen','top'));
	datScmOpen.bind(defaultPickerInfo('datScmOpen','top'));

	datScmOpen = null;
	datSysOpen = null;
	datEdDate = null;
	datStDat = null;
}

function setSysInfo() {
	$('#txtSysCd').val(selectedSystem.cm_syscd);
	$('#txtSysMsg').val(selectedSystem.cm_sysmsg);
	
	var i=0;
	for(i=0; i<cboSysGbData.length; i++) {
		if(cboSysGbData[i].cm_micode == selectedSystem.cm_sysgb) {
			$('[data-ax5select="cboSysGb"]').ax5select('setValue',selectedSystem.cm_sysgb,true);
			break;
		}
	}
	
	for(i=0; i<sysInfoData.length; i++) {
		$('#chkSysInfo'+ (i+1) ).wCheck('check',false);
	}
	
	if(selectedSystem.cm_sysinfo !== undefined && selectedSystem.cm_sysinfo !== null) {
		sysInfoStr = selectedSystem.cm_sysinfo;
		for(i=0; i<sysInfoStr.length; i++) {
			if(sysInfoStr.substr(i,1) === '1') {
				$('#chkSysInfo'+ (i+1) ).wCheck('check',true);
			}
		}
	}

	if(selectedSystem !== undefined) {
		$('#txtPrcCnt').val(selectedSystem.cm_prccnt);
		$('#datSysOpen').val(selectedSystem.sysopen);
		$('#datScmOpen').val(selectedSystem.scmopen);
//		$('#txtTime').val('');
		$("#txtAcptCnt").val(selectedSystem.cm_acptcnt);
		
		for(i=0; i<cboSvrCdData.length; i++) {
			if(cboSvrCdData[i].cm_micode == selectedSystem.base) {
				$('[data-ax5select="cboSvrCd"]').ax5select('setValue',selectedSystem.base,true);
				break;
			}
		}

		if(sysInfoStr.substr(3,1) === '1' &&  selectedSystem.hasOwnProperty('cm_stdate') && selectedSystem.hasOwnProperty('cm_eddate')) {
			disableCal(false, 'datStDate');
			disableCal(false, 'datEdDate');
			
			$('#datStDate').prop( "disabled", 	false );
			$('#timeDeploy').prop( "disabled", 	false );
			$('#datEdDate').prop( "disabled", 	false );
			$('#timeDeployE').prop( "disabled", false );
			
			$('#datStDateDiv').css('pointer-events','auto');
			$('#datEdDateDiv').css('pointer-events','auto');
			
			
			var stDate = selectedSystem.cm_stdate;
			var edDate = selectedSystem.cm_eddate;
			
			var strTime = stDate.substr(0,4) + '/' + stDate.substr(4,2) + '/' + stDate.substr(6,2);
			$('#datStDate').val(strTime);
			
			strTime = stDate.substr(8,2) + ':' + stDate.substr(10,2);
			$('#timeDeploy').val(strTime);
			
			strTime = edDate.substr(0,4) + '/' + edDate.substr(4,2) + '/' + edDate.substr(6,2);
			$('#datEdDate').val(strTime);
			
			strTime = edDate.substr(8,2) + ':' + edDate.substr(10,2);
			$('#timeDeployE').val(strTime);
			
		} else {
			disableCal(true, 'datStDate');
			disableCal(true, 'datEdDate');
			
			$('#datStDate').prop( "disabled", true );
			$('#timeDeploy').prop( "disabled", true );
			$('#datEdDate').prop( "disabled", true );
			$('#timeDeployE').prop( "disabled", true );
			
			
			$('#datStDate').val('');
			$('#timeDeploy').val('');
			$('#datEdDate').val('');
			$('#timeDeployE').val('');

			$('#datStDateDiv').css('pointer-events','none');
			$('#datEdDateDiv').css('pointer-events','none');
		}
		
		//이클립스사용(13)
		if(sysInfoStr.substr(12,1) == '1' && selectedSystem.hasOwnProperty('cm_prjname')) {
			$('#txtPrjName').val(selectedSystem.cm_prjname);
			$('#txtPrjName').prop( "disabled", false );
		} else {
			$('#txtPrjName').val('');
			$('#txtPrjName').prop( "disabled", true );
		}
		
		/*if(sysInfoStr.substr(5,1) === '1' && selectedSystem.hasOwnProperty('cm_systime')) {
			if(selectedSystem.cm_systime.length == 4){
				selectedSystem.cm_systime = selectedSystem.cm_systime.substr(0,2) + ":" + selectedSystem.cm_systime.substr(2,2); 
			}
				$('#txtTime').val(selectedSystem.cm_systime);
				$('#txtTime').prop( "disabled", false );
		} else {
			$('#txtTime').val('');
			$('#txtTime').prop( "disabled", true );
		}*/
	}
}

// 시스템 등록/업데이트시 유효성 체크
function sysValidationCheck() {
	//var gridSelectedIndex 	= sysInfoGrid.selectedDataIndexs;
	var stDate 		= null;
	var edDate 		= null;
	var nowDate 	= null;
	var stTime 		= null;
	var edTime 		= null;
	var nowTime 	= null;
	var nowFullDate = null;
	var isNew		= true;
	var closeSw     = true;
	
	if( selectedSystem == null ) {
		if($('#txtSysCd').val().length === 0){
			dialog.alert('시스템코드를 입력하여 주시기 바랍니다.',function(){});
			return;
		}
		if( $('#txtSysCd').val().length !== 5 ) {
			dialog.alert('시스템코드는 5자리로 숫자/영어(대문자)로 만들어주시기 바랍니다.',function(){});
			return;
		}
	}
	
	if( $('#txtSysMsg').val().length === 0 ) {
		dialog.alert('시스템명을 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	
	
	if( selectedSystem != null && $('#txtSysCd').val().length === 0 ) {
		dialog.alert('수정할 시스템을 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(getSelectedIndex('cboSysGb') < 1) {
		dialog.alert("시스템유형을 선택하여 주시기 바랍니다.", function() {});
		return;
	}
	
	if( $('#txtPrcCnt').val().length === 0 ) {
		dialog.alert('프로세스제한갯수를 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if( $('#datSysOpen').val().length === 0 ) {
		dialog.alert('시스템오픈일를 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if( $('#datScmOpen').val().length === 0 ) {
		dialog.alert('형상관리오픈일를 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	for(var i=0; i<sysInfoData.length; i++) {
		if(i === 4 && $('#chkSysInfo'+ (i+1) ).is(':checked') ) {
			stDate = replaceAllString($('#datStDate').val(), '/', '');  
			edDate = replaceAllString($('#datEdDate').val(), '/', '');  
			nowDate = getDate('DATE',0);
			stTime = replaceAllString($('#timeDeploy').val(), ':', '');  
			edTime = replaceAllString($('#timeDeployE').val(), ':', '');  
			nowTime = getTime();
			stFullDate = stDate + stTime;
			edFullDate = edDate + edTime;
			nowFullDate = nowDate + nowTime;
			
			if(stDate.length === 0 || stTime.length === 0 
					|| edDate.length === 0 || edTime.length === 0) {
				dialog.alert('중단일시 및 시간을 입력해 주시기 바랍니다.',function(){});
				return;
			}
			if( nowFullDate > stFullDate) {
				dialog.alert('중단시작일시가 현재일 이전입니다. 정확히 선택하여 주십시오.',function(){});
				return;
			}
			
			if( stFullDate > edFullDate ) {
				dialog.alert('중단종료시작일시가 중단시작일시 이전입니다. 정확히 선택하여 주십시오',function(){});
				return;
			}
			
			if( nowFullDate > edFullDate  ) {
				dialog.alert('중단종료시작일시가 현재일 이전입니다. 정확히 선택하여 주십시오',function(){});
				return;
			}
		}
		/*if(i === 5 && $('#chkSysInfo'+ (i+1) ).is(':checked')) {
			var txtTime = replaceAllString($('#txtTime').val().trim(),":","");
			if (txtTime.length === 0 ) {
				dialog.alert('적용시간을 입력하시기 바랍니다.',function(){
									$('#txtTime').focus();
								});
				return;
			} else if (txtTime.length !== 4) {
				dialog.alert('적용시간을 4자리 숫자로 입력하시기 바랍니다. (hhmm)',function(){
									$('#txtTime').focus();
								});
				return;
			} else {				
				var numSw = txtTime.search(/[0-9]/g);
				
				if(numSw !== 0 ) {
					dialog.alert('적용시간을 4자리 숫자로 입력하시기 바랍니다. (hhmm)',function(){
										$('#txtTime').focus();
									});
					return;
				}
			}
		}

		// 이클립스사용(13)
		if(i === 12 && $('#chkSysInfo'+ (i+1) ).is(':checked') && $('#txtPrjName').val().length === 0 ) {
			dialog.alert('프로젝트명 규칙을 입력해 주십시오. ex)JBB_MODEL_',function(){
				$('#txtPrjName').focus();}
			);
			return;
		}*/
	}
	
	/*if(jobGrid.getList("selected").length === 0){
		dialog.alert('업무를 선택하시기 바랍니다.');
		return;
	}*/
	
	closeSw = false;
	window.parent.sysInfoGridData.forEach(function(item,index) {
		if(item.cm_syscd === $('#txtSysCd').val()) {
			isNew = false; 
			if (item.closeyn == 'Y') closeSw = true;
		}
	});
	if (openSw && !isNew && !closeSw) {
		dialog.alert('기 사용 중인 시스템코드입니다. 다른코드로 입력하여 주시기 바랍니다.');
		return;
	}
	if ( selectedSystem == null  && isNew) {
		confirmDialog.confirm({
			msg: '시스템을 신규로 등록하시겠습니까?',
		}, function(){
			if(this.key === 'ok') { 
				updateSystem(isNew);
				closeModifyFg = false;
			}
		});
	} else if (closeSw){ 
		confirmDialog.confirm({
			msg: '폐기된 시스템입니다. 정보를 변경하여 재 등록 하시겠습니까?',
		}, function(){
			if(this.key === 'ok') { 
				updateSystem(isNew);
				closeModifyFg = true;
			}
		});
	} else {
		confirmDialog.confirm({
			msg: '시스템정보를 수정 등록하시겠습니까?',
		}, function(){
			if(this.key === 'ok') { 
				updateSystem(isNew);
				closeModifyFg = true;
			}
		});
	}
	stDate = null;
	edDate = null;
	nowDate = null;
	stTime = null;
	edTime = null;
	nowTime = null;
	nowFullDate = null;
}

function updateSystem(isNew) {
	var tmpJob = '';
	var tmpInfo = '';
	var tmpDate = '';
	var tmpMon = 0;
	var tmpSysGb 	= getSelectedVal('cboSysGb').value;
	var tmpDirBase 	= getSelectedVal('cboSvrCd').value;
	
	var selectedJobIndexs = jobGrid.getList("selected");
	var systemInfo = new Object();
	systemInfo.cm_syscd 	= $('#txtSysCd').val();
	systemInfo.cm_sysmsg 	= $('#txtSysMsg').val();
	systemInfo.cm_sysgb 	= tmpSysGb;
	systemInfo.cm_prccnt 	= $('#txtPrcCnt').val();
	systemInfo.prjname 		= $('#txtPrjName').val().trim();
	systemInfo.base 		= tmpDirBase;
	systemInfo.cm_stdate	= null;
	systemInfo.cm_eddate 	= null;
	
	$('#txtFindSys').val('');
	
	for(var i=0; i<selectedJobIndexs.length; i++) {
		var jobItem = selectedJobIndexs[i];
		if(tmpJob.length > 0 ) tmpJob += ',';
		tmpJob += jobItem.cm_jobcd;
		jobItem = null;
	}
	
	for(var i=0; i<sysInfoData.length; i++) {
		if($('#chkSysInfo'+ Number(sysInfoData[i].cm_micode)).is(":checked")) tmpChar = '1'; 
		else tmpChar = '0';
		
		var j = Number(sysInfoData[i].cm_micode);
		if (j == 0) tmpInfo = tmpChar;
		else if (j > tmpInfo.length) {
			if (j == tmpInfo.length + 1) tmpInfo = tmpInfo + tmpChar;
			else {
				l = j - tmpInfo.length - 1;
				for (k=0;l>k;k++) {
					tmpInfo = tmpInfo + "0";
				}	
				tmpInfo = tmpInfo + tmpChar;
			}
		} else if (j == tmpInfo.length) tmpInfo = tmpInfo.substr(0,j) + tmpChar;
		else tmpInfo = tmpInfo.substr(0,j-1) + tmpChar + tmpInfo.substr(j);
		
		if($('#chkSysInfo'+ (i+1) ).parent().hasClass('wCheck-on')) {
			if (i===4) { // 사용중지 
				systemInfo.cm_stdate = stFullDate;
				systemInfo.cm_eddate = edFullDate;
			}/* else if (i===5) { // 정기배포사용
				systemInfo.cm_systime = replaceAllString($('#txtTime').val().trim(),":","");
			}*/
		}
	}
	
	systemInfo.cm_jobcd 	= tmpJob;
	systemInfo.cm_sysinfo 	= tmpInfo;
	systemInfo.cm_editor 	= userId;
	systemInfo.sysopen 		= replaceAllString($('#datSysOpen').val(), '/', ''); 
	systemInfo.scmopen 		= replaceAllString($('#datScmOpen').val(), '/', '');
	
	
	if(selectedSystem != null ) {
		if(selectedSystem.closeSw === 'Y') systemInfo.closesw = "true";
		else systemInfo.closesw = "false";
	} else {
		systemInfo.closesw = "false";
	}
	var systemInfoDta = new Object(); 
	systemInfoDta = {
		etcData	: systemInfo,
		requestType	: 'sysInfo_Updt'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', systemInfoDta, 'json',successUpdateSysetm);
	systemInfoDta = null;

	tmpJob  = null;
	tmpInfo = null;
	tmpDate = null;
	tmpSysGb = null;
	systemInfo = null;
}

function successUpdateSysetm(data) {
	if( data === 'failed') {
		dialog.alert('폐기된 시스템코드는 사용하실 수 없습니다.',function(){});
	} else {
		dialog.alert('시스템정보 등록처리가 완료되었습니다.',
				function(){
					$('#txtSysCd').val(data);
					if(selectedSystem == null) {
						window.parent.popClose();
						// 신규등록일시
					}
				});
	}
}

function successSysClose(data) {
	if(data === 'OK')	dialog.alert('시스템정보 폐기처리가 완료되었습니다.',function(){});
	if(data !== 'OK')	dialog.alert('시스템정보 폐기중 오류가 발생했습니다. 관리자에게 문의하세요.',function(){});
}

//	화면초기화
function screenInit() {
	disableCal(true, 'datStDate');
	disableCal(true, 'datEdDate');
	
	$('#datStDate').prop( "disabled", 	true );
	$('#timeDeploy').prop( "disabled", 	true );
	$('#datEdDate').prop( "disabled", 	true );
	$('#timeDeployE').prop( "disabled", true );
	$('#txtPrjName').prop( "disabled",  true );
//	$('#txtTime').prop( "disabled",  true );
	$('#datStDateDiv').css('pointer-events','none');
	$('#datEdDateDiv').css('pointer-events','none');
}

//	선택된 시스템 JOB
function getSysJobInfo(sysCd) {
	jobGrid.clearSelect();
	var ajaxReturnData = null;
	
	
	var sysJobInfoData = {
		SelMsg	:	'',
		closeYn	:	'N',
		requestType	: 'getJobCd'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/common/CodeInfoServlet', sysJobInfoData, 'json');
	jobGrid.setData(ajaxReturnData);
	
	if (sysCd != null && sysCd != '' && sysCd != undefined) {
		
		var sysJobInfoData = {
			UserID	:	userId,
			SysCd	:	sysCd,
			selSys	:	sysCd,
			SecuYn	:	'N',
			CloseYn	:	'N',
			SelMsg	:	'',
			sortCd	:	'CD',
			requestType	: 'getJobInfo'
		}
		ajaxAsync('/webPage/common/SysInfoServlet', sysJobInfoData, 'json', successGetSysJobInfo);
		sysJobInfoData = null;
		sysJobInfo = null;
	}
}

//	선택된 시스템 JOB
function successGetSysJobInfo(data) {
	jobGridData = data;
	var jobGridList = jobGrid.getList();
	var count =0;
	
	var selJobCnt = 0;
	
	for(var i=jobGridList.length-1; i>=selJobCnt; i--){
		var selectedData = null;
		for(var j=0; j<jobGridData.length; j++) {
			if(jobGridList[i].cm_jobcd == jobGridData[j].cm_jobcd){
				selectedData = jobGridList[i];
				selectedData.__selected__ = true;
				jobGridList.splice(i,1);
				jobGridList.splice(0,0,selectedData);
				selJobCnt++;
				i++;
				break;
			}
		}
		count++;
		if(count >= jobGridList.length){
			break;
		}
	}
	jobGrid.setData(jobGridList);
}

// 시스템 유형 CBO
// 기준 서버구분 CBO
function getSysCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('SYSGB',    'SEL','N','2',''),
		new CodeInfoOrdercd('SERVERCD',	'SEL','N','3',''),
		new CodeInfoOrdercd('SYSINFO',	   '','N','3','')
	]);
	cboSysGbData	= codeInfos.SYSGB;
	cboSvrCdData 	= codeInfos.SERVERCD;	
	sysInfoData 	= codeInfos.SYSINFO;
	
	$('[data-ax5select="cboSysGb"]').ax5select({
		options: injectCboDataToArr(cboSysGbData, 'cm_micode', 'cm_codename')
	});	
	
	$('[data-ax5select="cboSvrCd"]').ax5select({
		options: injectCboDataToArr(cboSvrCdData, 'cm_micode', 'cm_codename')
	});	
	
	makeSysInfoUlList();
}

// 시스템 속성 ul 만들어주기
function makeSysInfoUlList() {
	$('#ulSysInfo').empty();
//	var liStr = null;
//	var addId = null;
	
	var $test1 = $('#ulSysInfo');
	var $frag = "";

	var sysInfoDataLength = sysInfoData.length;
	for(var i = 0; i<sysInfoDataLength; i++){
		$frag += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		$frag += '<div class="margin-3-top">';
		$frag += '	<input type="checkbox" class="checkbox-sysInfo" id="chkSysInfo'+Number(sysInfoData[i].cm_micode)+'" data-label="'+sysInfoData[i].cm_codename + " [ " +sysInfoData[i].cm_micode+ " ]"+'"  value="'+sysInfoData[i].cm_micode+'" />';
		$frag += '</div>';
		$frag += '</li>';
	}
	$test1.html($frag);
	$frag = null;
	$test1 = null;
	
	$('input.checkbox-sysInfo').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
	// 시스템 속성 클릭시
	$('input.checkbox-sysInfo').bind('click', function(e) {
		
		var selectedSysInfo = Number(this.value);
		console.log(selectedSysInfo);
		if( !(selectedSysInfo === 5 || selectedSysInfo === 6 || selectedSysInfo === 13) ) return;
		
		if(selectedSysInfo === 5 && $(this).is(':checked')) {
			disableCal(false, 'datStDate');
			disableCal(false, 'datEdDate');
			
			var nowDate = getDate('DATE',0);
			$("#timeDeployE").val("00:00");
			$("#timeDeploy").val("00:00");
			nowDate = nowDate.substr(0,4) + "/" + nowDate.substr(4,2) + "/" + nowDate.substr(6,2);
			$("#datStDate").val(nowDate);
			$("#datEdDate").val(nowDate);
			
			$('#datStDate').prop( "disabled",   false );
			$('#timeDeploy').prop( "disabled", 	false );
			$('#datEdDate').prop( "disabled", 	false );
			$('#timeDeployE').prop( "disabled", false );
			
			$('#datStDateDiv').css('pointer-events','auto');
			$('#datEdDateDiv').css('pointer-events','auto');
		}
		
		if(selectedSysInfo === 5 && !($(this).is(':checked')) ) {
			disableCal(true, 'datStDate');
			disableCal(true, 'datEdDate');
			
			$('#datStDate').prop( "disabled", 	true );
			$('#timeDeploy').prop( "disabled", 	true );
			$('#datEdDate').prop( "disabled", 	true );
			$('#timeDeployE').prop( "disabled", true );
			
			$('#datStDate').val('');
			$('#timeDeploy').val('');
			$('#datEdDate').val('');
			$('#timeDeployE').val('');
			
			$('#datStDateDiv').css('pointer-events','none');
			$('#datEdDateDiv').css('pointer-events','none');
		}
		/*if(selectedSysInfo === 6 && $(this).is(':checked') ) {
			$('#txtTime').prop( "disabled", false );
		}*/
		
		/*if(selectedSysInfo === 6 && !($(this).is(':checked')) ) {
			$('#txtTime').prop( "disabled", true );
			$('#txtTime').val('');
		}*/

		if(selectedSysInfo === 13 && $(this).is(':checked') ) {
			$('#txtPrjName').prop( "disabled", false );
			
			if(selectedSystem != null && selectedSystem.hasOwnProperty('cm_prjname')){
				$('#txtPrjName').val(selectedSystem.cm_prjname);
			} else {
				$('#txtPrjName').val('');
			}
		}
		
		if(selectedSysInfo === 13 && !($(this).is(':checked')) ) {
			$('#txtPrjName').prop( "disabled", true );
			$('#txtPrjName').val('');
		}
	});
}

function clear() {
	$('#txtSysCd').val('');		//시스템코드
	$('#txtSysMsg').val('');	//시스템명
	$('#txtPrcCnt').val('');	//프로세스갯수
	$('#datSysOpen').val('');	//시스템오픈
	$('#datScmOpen').val('');	//형상오픈
	$('#txtPrjName').val('');	//프로젝트명
	
	$('[data-ax5select="cboSysGb"]').ax5select('setValue',cboSysGbData[0].cm_micode,true);
	$('[data-ax5select="cboSvrCd"]').ax5select('setValue',cboSvrCdData[0].cm_micode,true);
	
	for(var i=0; i<sysInfoData.length; i++) {
		$('#chkSysInfo'+ (i+1) ).wCheck('check',false);
	}
	
	jobGrid.clearSelect();
	
	disableCal(true, 'datStDate');
	disableCal(true, 'datEdDate');
	
	$('#datStDate').prop( "disabled", 	true );
	$('#timeDeploy').prop( "disabled", 	true );
	$('#datEdDate').prop( "disabled", 	true );
	$('#timeDeployE').prop( "disabled", true );
	$('#txtPrjName').prop( "disabled", true );
	
	$('#datStDateDiv').css('pointer-events','none');
	$('#datEdDateDiv').css('pointer-events','none');
}

function clickSysinfo(value) {
	if(value == "05") { // 사용중지(5)
		if($("#chkSysInfo5").is(":checked")) {
			disableCal(false, 'datStDate');
			disableCal(false, 'datEdDate');
			$("#datStDate").prop("disabled", false);
			$("#datEdDate").prop("disabled", false);
			$("#timeDeploy").prop("disabled", false);
			$("#timeDeployE").prop("disabled", false);
		} else {
			disableCal(true, 'datStDate');
			disableCal(true, 'datEdDate');
			$("#datStDate").prop("disabled", true);
			$("#datEdDate").prop("disabled", true);
			$("#timeDeploy").prop("disabled", true);
			$("#timeDeployE").prop("disabled", true);
		}
	} else if(value == "13") { // 이클립스사용(13)
		if($("#chkSysInfo13").is(":checked")) {
			$("#txtPrjName").prop("disabled", false);
		} else {
			$("#txtPrjName").prop("disabled", true);
		}
	}
}