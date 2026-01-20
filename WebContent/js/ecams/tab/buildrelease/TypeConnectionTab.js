/**
 * 빌드/릴리즈유형등록 화면 기능정의
 *
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-18
 *
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var conInfoGrid			= new ax5.ui.grid();
var scriptGrid			= new ax5.ui.grid();

var conInfoGridData 	= null;
var conInfoAnPrgData	= null;
var scriptGridData		= null;

var cboSysCdData	= null;
var cboQryData		= null;
var cboPrcSysData	= null;
var qryAnPrcData	= null;
var cboBldCdData	= null;

var cboScript1Data = [];
var cboScript2Data = [];

var ulPrgInfoData	= null;
var ulJobInfoData	= null;

var selBldCdVal		= null;
var selScipt1		= null;
var selScipt2		= null;

var cboOptions		= null;

conInfoGrid.setConfig({
    target: $('[data-ax5grid="conInfoGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: true,
    //multipleSelect: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            //this.self.select(this.dindex);
            clickConInfoGrid(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_jobname", 	label: "업무명",			width: '10%'},
        {key: "cm_codename", 	label: "프로그램종류", 		width: '20%', align: 'left'},
        {key: "prcsys", 		label: "처리단계",  		width: '15%', align: 'left'},
        {key: "bldname", 		label: "스크립트유형", 		width: '15%', align: 'left'}
        /*{key: "bldcd3", 		label: "스크립트[묶음배포1]",	width: '20%', align: "center"},
        {key: "bldcd4", 		label: "스크립트[묵음배포2]",	width: '20%', align: "center"},
        {key: "RUNGBN", 		label: "실행시점",  			width: '10%', align: "center"},
        {key: "RUNPOS", 		label: "쉘실행위치",  			width: '10%', align: "center"},
        {key: "SEQYN", 			label: "쉘순차실행",  			width: '10%', align: "center"},
        {key: "TOTYN", 			label: "일괄쉘실행",  			width: '10%', align: "center"},*/
    ]
});

scriptGrid.setConfig({
    target: $('[data-ax5grid="scriptGrid"]'),
    sortable: false,
    multiSort: true,
    showRowSelector: false,
    page : false,
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
        /*{key: "gbncd", 	label: "구분",  		width: '10%' , align: "center"},*/
        {key: "cm_seq", 	label: "순서",  		width: '5%' , align: "center"},
        {key: "cm_cmdname", label: "수행명령",  	width: '85%' , align: "left"}
    ]
});

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});
$('[data-ax5select="cboQry"]').ax5select({
	options: []
});
$('[data-ax5select="cboPrcSys"]').ax5select({
	options: []
});
$('[data-ax5select="cboBldCd"]').ax5select({
	options: []
});

$('input.checkbox-view').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
$('input:radio[name=releaseChk]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name=grpTotYN]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function() {
	getBldCd();
	getSysInfo();

	//일괄쉘실행 구분 비활성화
	$('#optAcpt').wRadio("disabled", true);
	$('#optRsrc').wRadio("disabled", true);
	$('#optDir').wRadio("disabled", true);
	$('#optJob').wRadio("disabled", true);

	//시스템조회
	$('#txtSyscd').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			var sysCd = $('#txtSyscd').val().trim();

			for (var i=0; i<cboSysCdData.length; i++) {
				if(cboSysCdData[i].cm_syscd == sysCd || cboSysCdData[i].cm_sysmsg.indexOf(sysCd)>=0){
					$('[data-ax5select="cboSysCd"]').ax5select('setValue', cboSysCdData[i].cm_syscd, true);
					$('#cboSysCd').trigger('change');
					break;
				}
			}
		}
	});
	// 시스템 cbo 변경이벤트
	$('#cboSysCd').bind('change', function() {
		cboSysCd_Change();
	});

	// 유형구분 cbo 변경 이벤트
	$('#cboQry').bind('change', function() {
		cboQry_Change();
	});

	// 실행구분 cbo 변경 이벤트
	$('#cboPrcSys').bind('change', function() {
		cboPrcSys_Change();
	});

	// 스크립트유형 cbo 변경 이벤트
	$('#cboBldCd').bind('change', function() {
		cboBldCd_Change();
	});

	// 프로그램종류 전체선택
	$('#chkPrgAll').bind('click', function() {
		chkPrgAll_Click();
	});

	// 업무종류 전체선택
	$('#chkJobAll').bind('click', function() {
		chkJobAll_Click();
	});

	// 등록 버튼 클릭
	$('#btnReg').bind('click', function() {
		insertBldList();
	});

	// 삭제 버튼 클릭
	$('#btnDell').bind('click', function() {
		deleteBldList();
	});

	//일괄쉘실행 체크박스 이벤트
	$('#chkBatch').bind('change', function() {
		chkBatch_change();
	});
});

function initScreen() {
	$('[data-ax5select="cboQry"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboPrcSys"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboBldCd"]').ax5select({
		options: []
	});
	
	conInfoGrid.setData([]);
	scriptGrid.setData([]);
	$('#ulJobInfo').empty();
	$('#ulPrgInfo').empty();
}

//시스템 cbo 변경이벤트
function cboSysCd_Change() {
	var selSysInfo = null;

	if(getSelectedIndex('cboSysCd') < 1 ) {
		return;
	}
	ulJobInfoData	= null;
	$('#ulJobInfo').empty();
	selSysInfo = getSelectedVal('cboSysCd').cm_sysinfo;

	if(selSysInfo.substr(7,1) === '1') {
		$('#chkJobAll').wCheck('disabled',false);
		$('#divJob').removeClass('mask_wrap');
		getJobInfo();
	} else {
		$('#chkJobAll').wCheck('disabled',true);
		$('#divJob').addClass('mask_wrap');
	}
	getQryCd();
}

//유형구분 cbo 변경 이벤트
function cboQry_Change() {
	cboPrcSysData = [];
	qryAnPrcData.forEach(function(item, index) {
		if(item.cm_reqcd === getSelectedVal('cboQry').cm_reqcd) {
			cboPrcSysData.push(item);
		}
	});
	$('[data-ax5select="cboPrcSys"]').ax5select({
        options: injectCboDataToArr(cboPrcSysData, 'cm_jobcd' , 'prcsys')
	});
	$('#cboPrcSys').trigger('change');
	getBldList();
}

//실행구분 cbo 변경 이벤트
function cboPrcSys_Change() {
	var bldCdData = cboBldCdFilter();

	$('[data-ax5select="cboBldCd"]').ax5select({
        options: injectCboDataToArr(bldCdData, 'cm_micode' , 'cm_codename')
	});
	if(selBldCdVal != null && selBldCdVal != '') {
		$('[data-ax5select="cboBldCd"]').ax5select('setValue', selBldCdVal, true);
	}

	if(bldCdData.length > 0 ) {
		if(selBldCdVal != null && selBldCdVal != '') selBldCdVal = null;
		if (selScipt1 != null || selScipt2 != null) {
			cboScript_Change();
			selScipt1 = null;
			selScipt2 = null;
		} else {
			$('#cboBldCd').trigger('change');
		}
	}
}

//스크립트유형 cbo 변경 이벤트
function cboBldCd_Change() {
	var selIn = scriptGrid.selectedDataIndexs;
	var bldcd3 = null;
	var bldcd4 = null;
	if(selIn.length > 0 ) {
		var selItem = scriptGrid.list[selIn];
		bldcd3 = selItem.bldcd3;
		bldcd4 = selItem.bldcd4;
	}

	if (cboScript1Data.length >0 && (bldcd3 != null || bldcd4 != null)) {
		if (getSelectedIndex('cboScript1') > 0 && getSelectedIndex('cboScript2') > 0) {
			getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode+getSelectedVal('cboScript1').cm_micode+getSelectedVal('cboScript2').cm_micode);
		} else if (getSelectedIndex('cboScript1') > 0) {
			getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode+getSelectedVal('cboScript1').cm_micode);
		} else if (getSelectedIndex('cboScript2') > 0) {
			getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode+getSelectedVal('cboScript2').cm_micode);
		}
	} else {
		getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode);
	}
}

function getScript(bldgbn, micode){
	var data = new Object();
	data = {
		Cbo_BldGbn_code : bldgbn,
		Cbo_BldCd0_code : micode,
		requestType		: 'getScript'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetScript);
}

//프로그램종류 전체선택
function chkPrgAll_Click() {
	if(ulPrgInfoData === null) return;

	var addId = null;
	ulPrgInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if($('#chkPrgAll').is(':checked')) {
			$('#chkPrg'+addId).wCheck('check', true);
		} else {
			$('#chkPrg'+addId).wCheck('check', false);
		}
	});
}

//업무종류 전체선택
function chkJobAll_Click() {
	if(ulJobInfoData === null) return;
	var addId = null;
	ulJobInfoData.forEach(function(item, index) {
		addId = item.cm_jobcd
		if($('#chkJobAll').is(':checked')) {
			$('#chkJob'+addId).wCheck('check', true);
		} else {
			$('#chkJob'+addId).wCheck('check', false);
		}
	});
}

// 등록
function insertBldList() {
	var sysInfo = getSelectedVal('cboSysCd').cm_sysinfo;
	var jobSw	= false;
	var prgSw	= false;
	var addId	= null;
	var jobCds	= '';
	var rsrcCds	= '';
	var etcData = new Object();

	if(getSelectedIndex('cboSysCd') === 0 ) {
		dialog.alert('시스템을 선택한 후 등록하십시오.', function(){});
		return;
	}

	if(sysInfo.substr(7,1) === '1') {
		ulJobInfoData.forEach(function(item, index) {
			addId = item.cm_jobcd;
			if($('#chkJob'+addId).is(':checked')) {
				if(jobCds.length > 0 ) jobCds += ',';
				jobCds += addId;
				jobSw = true;
			}
		});

		if(!jobSw) {
			dialog.alert('업무를 선택한 후 등록하십시오.', function(){});
			return;
		}
	} else {
		jobCds = '****';
	}

	if(ulPrgInfoData.length > 0 ) {
		ulPrgInfoData.forEach(function(item, index) {
			addId = item.cm_micode;
			if($('#chkPrg'+addId).is(':checked')) {
				if(rsrcCds.length > 0 ) rsrcCds += ',';
				rsrcCds += item.cm_micode;
				prgSw = true;
			}
		})

		if(!prgSw) {
			dialog.alert('프로그램종류를 선택한 후 등록하십시오.', function(){});
			return;
		}
	} else {
		dialog.alert('프로그램종류를 선택한 후 등록하십시오.', function(){});
		return;
	}

	if($('#chkLocal').is(':checked')) {
		etcData.CM_RUNPOS = 'L'
	} else {
		etcData.CM_RUNPOS = 'R'
	}
	if($('#chkSeq').is(':checked')) {
		etcData.CM_SEQYN = 'Y'
	} else {
		etcData.CM_SEQYN = 'N'
	}
	if($('#chkBatch').is(':checked')) {
		etcData.CM_TOTYN = 'Y'
	} else {
		etcData.CM_TOTYN = 'N'
	}
	if($('#optBefore').is(':checked')) {
		etcData.CM_RUNGBN = 'B'
	} else {
		etcData.CM_RUNGBN = 'A'
	}

	etcData.cm_syscd 	= getSelectedVal('cboSysCd').cm_syscd;
	etcData.cm_qrycd 	= getSelectedVal('cboQry').cm_reqcd;
	etcData.cm_prcsys 	= getSelectedVal('cboPrcSys').cm_jobcd;
	etcData.cm_bldcd 	= getSelectedVal('cboBldCd').cm_micode;
	etcData.cm_bldgbn 	= getSelectedVal('cboBldCd').cm_bldgbn;
	etcData.cm_jobcd 	= jobCds;
	etcData.cm_rsrccd 	= rsrcCds;

	if($('#divScript').is(":visible")) {
		etcData.cm_bldcd2 = getSelectedVal('cboScript1').cm_micode;
		etcData.cm_bldcd3 = getSelectedVal('cboScript2').cm_micode;
	}

	var data = new Object();
	data = {
		etcData 	: etcData,
		requestType	: 'getCmm0033_DBProc'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successInsertBldList);
}

// 등록 완료
function successInsertBldList(data) {
	if(data > 0) {
		dialog.alert('등록처리를 완료하였습니다.', function(){
			$('#cboQry').trigger('change');
		});
	} else {
		dialog.alert('등록처리중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.', function(){});
	}
}

// 연결리스트 삭제
function deleteBldList() {
	var selIn 	= conInfoGrid.selectedDataIndexs;
	var delObj	= null;
	var delList	= [];
	var sysCd	= getSelectedVal('cboSysCd').cm_syscd;
	var qryCd	= getSelectedVal('cboQry').cm_reqcd;

	if(selIn.length === 0 ) {
		dialog.alert('삭제할 스크립트 연결 정보를 선택후 눌러주세요.', function(){});
		return;
	}

	selIn.forEach(function(selIndex, index) {
		delObj = conInfoGrid.list[selIndex];
		//delObj.cm_rungbn = delObj.CM_RUNGBN;
		delObj.cm_syscd	 = sysCd;
		delObj.cm_qrycd	 = qryCd;
		delObj.cm_prcsys = delObj.cm_prcsys;
		delObj.cm_rsrccd = delObj.cm_rsrccd;
		delObj.cm_jobcd = delObj.cm_jobcd;
		delObj.cm_rungbn = delObj.CM_RUNGBN;
		delList.push(delObj);
	});
	var data = new Object();
	data = {
		delList 	: delList,
		requestType	: 'getCmm0033_Del'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successDeleteBldList);
}

// 연결리스트 삭제 완료
function successDeleteBldList(data) {
	if(data == 0) {
		dialog.alert('삭제처리를 완료하였습니다.', function() {
			var selIn 	= conInfoGrid.selectedDataIndexs;
			selIn.sort(function(a,b) {
				return b - a;
			});

			selIn.forEach(function(selIndex, index) {
				conInfoGridData.splice(selIndex,1);
			})

			conInfoGrid.setData(conInfoGridData);
		});
	}
}

// 연결 스크립트 리스트 클릭
function clickConInfoGrid(index) {
	var selItem = conInfoGrid.list[index];
	var sysInfo	= getSelectedVal('cboSysCd').cm_sysinfo;

	var addId = null;
	if(ulPrgInfoData != null) {
		ulPrgInfoData.forEach(function(item, index) {
			addId = item.cm_micode;
			if (addId == selItem.cm_rsrccd) {
				$('#chkPrg'+selItem.cm_rsrccd).wCheck('check',true);
			} else {
				$('#chkPrg'+addId).wCheck('check', false);
			}
		});
	}

	if(ulJobInfoData != null) {
		ulJobInfoData.forEach(function(item, index) {
			addId = item.cm_jobcd
			$('#chkJob'+addId).wCheck('check', false);
		});
	}
	if(sysInfo.substr(7,1) === '1') {
		$('#chkJob'+selItem.cm_jobcd).wCheck('check',true);
	}

	$('#chkLocal').wCheck('check',false);
	$('#chkSeq').wCheck('check',false);
	$('#chkSysSeq').wCheck('check',false);
	$('#chkBatch').wCheck('check',false);
	$('#optAfter').wCheck('check',true);

	if(selItem.CM_RUNPOS === 'L') {
		$('#chkLocal').wCheck('check',true);
	}
	if(selItem.CM_SEQYN === 'Y') {
		$('#chkSeq').wCheck('check',true);
	}
	if(selItem.CM_TOTYN === 'Y') {
		$('#chkBatch').wCheck('check',true);
	}
	if(selItem.CM_RUNGBN === 'B') {
		$('#optBefore').wCheck('check',true);
	}
	
    if (selItem.bldcd3 != null && selItem.bldcd3 != "") {
    	selScipt1 = selItem.cm_bldcd3;
    }
    if (selItem.bldcd4 != null && selItem.bldcd4 != "") {
    	selScipt2 = selItem.cm_bldcd4;
    }

	selBldCdVal = selItem.cm_bldcd;
	$('[data-ax5select="cboPrcSys"]').ax5select('setValue', selItem.cm_prcsys ,true);
	$('#cboPrcSys').trigger('change');
}

// 스크립트 그리드 정보 가져오기 완료
function successGetScript(data) {
	scriptGridData = data;
	scriptGrid.setData(scriptGridData);
}

// 스크립트 유형 콤보 필터
function cboBldCdFilter() {
	var bldCdArr 	= [];
	var prcSysVal 	= null;
	var bldGbn		= null;
	var item		= null;

	for(var i=0; i<cboBldCdData.length; i++) {
		prcSysVal 	= getSelectedVal('cboPrcSys').cm_jobcd;
		item		= cboBldCdData[i];
		bldGbn 		= item.cm_bldgbn;
		if(item.cm_codename !== '유형신규등록') {
			if (prcSysVal === 'SYSDN') {
				if(bldGbn === '1') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if (prcSysVal === 'SYSUP') {
				if(bldGbn === '5') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if (prcSysVal === 'SYSCB') {
				if(bldGbn === '2') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if ((prcSysVal === 'SYSED' || prcSysVal === 'SYSCED')) {
				if(bldGbn === '3') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if (prcSysVal === 'SYSRC') {
				if(bldGbn === '6') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if (prcSysVal === 'SYSAR') {
				if(bldGbn === '7') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if(bldGbn === '4'){
				bldCdArr.push(item);
			}
		}
	}
	return bldCdArr;
}

// 상단 연결 정보 리스트/프로그램종류 가져오기
function getBldList() {
	var data = new Object();
	data = {
		SysCd 		: getSelectedVal('cboSysCd').cm_syscd,
		TstSw 		: getSelectedVal('cboSysCd').TstSw,
		QryCd 		: getSelectedVal('cboQry').cm_reqcd,
		SysInfo 	: getSelectedVal('cboSysCd').cm_sysinfo,
		requestType	: 'getBldList'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetBldList);
}

// 상단 연결 정보 리스트/프로그램종류 가져오기 완료
function successGetBldList(data) {
	conInfoAnPrgData = data;
	conInfoGridData = [];
	ulPrgInfoData	= [];
	conInfoAnPrgData.forEach(function(item, index) {
		if(item.ID === 'BLDLIST') {
			conInfoGridData.push(item);
		}

		if(item.ID === 'RSRCCD') {
			ulPrgInfoData.push(item);
		}
	})
	conInfoGrid.setData(conInfoGridData);
	makePrgInfoUlList();
}

// 프로그램종류 ul만들어주기
function makePrgInfoUlList() {
	$('#ulPrgInfo').empty();
	var liStr = null;
	var addId = null;
	ulPrgInfoData.forEach(function(prgItem, index) {
		addId = prgItem.cm_micode;
		liStr  = '';
		liStr += '<li class="list-group-item dib width-50" style="min-width: 150px;">';
		liStr += '	<input type="checkbox" class="checkbox-prg" id="chkPrg'+addId+'" data-label="'+prgItem.cm_codename+'"  value="'+prgItem.cm_micode+'" />';
		liStr += '</li>';
		$('#ulPrgInfo').append(liStr);
	});

	$('input.checkbox-prg').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 업무종류 가져오기
function getJobInfo() {
	var data = new Object();
	data = {
		UserID 	: userId,
		SysCd 	: getSelectedVal('cboSysCd').cm_syscd,
		SecuYn 	: 'Y',
		CloseYn : 'N',
		SelMsg 	: '',
		sortCd 	: 'NAME',
		requestType	: 'getJobInfo'
	}

	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetJobInfo);
}

// 업무종류 가져오기 완료
function successGetJobInfo(data) {
	ulJobInfoData = data;
	$('#ulJobInfo').empty();
	var liStr = "";
	var addId = null;
	ulJobInfoData.forEach(function(jobItem, index) {
		addId = jobItem.cm_jobcd;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 150px;">';
		liStr += '	<input type="checkbox" class="checkbox-job" id="chkJob'+addId+'" data-label="'+jobItem.cm_jobname+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulJobInfo').append(liStr);
	$('input.checkbox-job').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 시스템 cbo 가져오기
function getSysInfo() {
	var sysInfoData = new Object();
	sysListInfoData = {
			UserId	: userId,
			SecuYn  : "Y",
			SelMsg  : "SEL",
			CloseYn : "N",
			ReqCd   :  "D12",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', sysListInfoData, 'json',successGetSysInfo);
};

// 시스템 cbo 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	$('[data-ax5select="cboSysCd"]').ax5select({
        options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
	// 첫번째 시스템 기본 세팅
	$('[data-ax5select="cboSysCd"]').ax5select('setValue', cboSysCdData[0].cm_syscd, true);
	$('#cboSysCd').trigger('change');
}

// 유형구분/실행구분 가져오기
function getQryCd() {
	var data = new Object();
	data = {
		SysCd 		: getSelectedVal('cboSysCd').cm_syscd,
		TstSw 		: getSelectedVal('cboSysCd').TstSw,
		requestType	: 'getQryCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetQryCd);
}

// 유형구분/실행구분 가져오기 완료
function successGetQryCd(data) {
	var findSw = false;

	if(data.length === 0 ) {
		dialog.alert('해당 시스템에 결재정보가 등록되어있지않습니다.\n등록후 사용가능합니다.', function(){});
		initScreen();
		return;
	}

	qryAnPrcData = data;
	cboQryData = [];
	qryAnPrcData.forEach(function(item, index) {
		if (item.ID == 'QRYCD') cboQryData.push(item);
	});
	$('[data-ax5select="cboQry"]').ax5select({
        options: injectCboDataToArr(cboQryData, 'cm_reqcd' , 'cm_codename')
	});
	$('#cboQry').trigger('change');
}
// 스크립트 유형 가져오기
function getBldCd() {
	var data = new Object();
	data = {
		requestType	: 'getBldCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetBldCd);
}

// 스크립트 유형 가져오기 완료
function successGetBldCd(data) {
	cboBldCdData = data;
}

// 일괄쉘실행 체크박스 체크이벤트
function chkBatch_change() {
	if($("#chkBatch").is(":checked")) {
		$('#optAcpt').wRadio("disabled", false);
		$('#optRsrc').wRadio("disabled", false);
		$('#optDir').wRadio("disabled", false);
		$('#optJob').wRadio("disabled", false);
	}else {
		$('#optAcpt').wRadio("disabled", true);
		$('#optRsrc').wRadio("disabled", true);
		$('#optDir').wRadio("disabled", true);
		$('#optJob').wRadio("disabled", true);

		$('#optAcpt').wRadio("check", false);
		$('#optRsrc').wRadio("check", false);
		$('#optDir').wRadio("check", false);
		$('#optJob').wRadio("check", false);
	}
}