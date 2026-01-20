/**
 * 빌드/릴리즈유형연결 화면 기능정의
 *
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-19
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

var selBldCdVal		= null;
var selScipt1		= null;
var selScipt2		= null;

var cboOptions		= null;

conInfoGrid.setConfig({
    target: $('[data-ax5grid="conInfoGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
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
        {key: "cm_codename", 	label: "프로그램종류", 		width: '10%'},
        {key: "cr_rsrcname", 	label: "프로그램명", 		width: '10%'},
        {key: "prcsys", 		label: "처리단계",  		width: '15%'},
        {key: "bldcd", 			label: "스크립트유형", 		width: '15%'},
//        {key: "bldcd3", 		label: "스크립트[묶음배포1]",	width: '20%', align: "center"},
//        {key: "bldcd4", 		label: "스크립트[묵음배포2]",	width: '20%', align: "center"},
        {key: "RUNGBN", 		label: "실행시점",  		width: '10%', align: "center"},
        {key: "RUNPOS", 		label: "쉘실행위치",  		width: '10%', align: "center"},
        {key: "SEQYN", 			label: "쉘순차실행",  		width: '10%', align: "center"},
        {key: "TOTYN", 			label: "일괄쉘실행",  		width: '10%', align: "center"},
        {key: "USERYN", 		label: "실행여부선택",  	width: '10%', align: "center"},
        {key: "NOEXECYN", 		label: "스크립트미실행",  	width: '10%', align: "center"}
    ]
});

scriptGrid.setConfig({
    target: $('[data-ax5grid="scriptGrid"]'),
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
//        {key: "gbncd", 		label: "구분",  		width: '10%' , align: "center"},
        {key: "cm_seq", 	label: "순서",  		width: '5%' ,	align: "center"},
        {key: "cm_cmdname", label: "수행명령",  	width: '85%' ,	align: "left"}
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
	
	$("#prg_txt").bind("keydown keypress keyup", function(e){
		if(e.keyCode == "13"){
			getPrgCd();
		}
		
		prg_change();
	});
	
	$("#prg_btn").bind("click", function(){
		getPrgCd();
	});

	//묶음1조회
	$('#txtScript1').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			var txtScript1 = $('#txtScript1').val().trim();

			for (var i=0; i<cboScript1Data.length; i++) {
				if(cboScript1Data[i].cm_micode == txtScript1 || cboScript1Data[i].cm_codename.indexOf(txtScript1)>=0){
					$('[data-ax5select="cboScript1"]').ax5select('setValue', cboScript1Data[i].cm_micode, true);
					cboScript_Change();
					break;
				}
			}
		}
	});
	//묶음1조회
	$('#txtScript2').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			var txtScript1 = $('#txtScript2').val().trim();

			for (var i=0; i<cboScript2Data.length; i++) {
				if(cboScript2Data[i].cm_micode == txtScript1 || cboScript2Data[i].cm_codename.indexOf(txtScript1)>=0){
					$('[data-ax5select="cboScript2"]').ax5select('setValue', cboScript2Data[i].cm_micode, true);
					cboScript_Change();
					break;
				}
			}
		}
	});

	$('#cboScript1').bind('change',function() {
		cboScript_Change();
	});
	$('#cboScript2').bind('change',function() {
		cboScript_Change();
	});
});

//시스템 cbo 변경이벤트
function cboSysCd_Change() {
	var selSysInfo = null;

	if(getSelectedIndex('cboSysCd') < 1 ) {
		return;
	}
	selSysInfo = getSelectedVal('cboSysCd').cm_sysinfo;

	getQryCd();
}

//유형구분 cbo 변경 이벤트
function cboQry_Change() {
	//Cmd1200.getQryCd 결재정보, 처리속성 조인 필요 SYSGBN 셋팅
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
	getPrgList();
	getPrgCd();
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

	$('#divScript').css('display','none');
	if(getSelectedVal('cboPrcSys').cm_jobcd == 'SYSRC'){
		var addId = null;
		ulPrgInfoData.forEach(function(item, index) {
			addId = item.cm_micode;
			if($('#chkPrg'+addId).is(':checked') && item.cm_info.substr(45,1) == '1') {
				$('#divScript').css('display','block');
				$('#scriptGridDiv').css('height','30%');

				cboScript1Data = clone(bldCdData);
				$('[data-ax5select="cboScript1"]').ax5select({
			        options: injectCboDataToArr(cboScript1Data, 'cm_micode' , 'cm_codename')
				});
				if (selScipt1 != null && selScipt1 != '') {
					$('[data-ax5select="cboScript1"]').ax5select('setValue', selScipt1, true);
				}
				cboScript2Data = clone(bldCdData);
				$('[data-ax5select="cboScript2"]').ax5select({
			        options: injectCboDataToArr(cboScript2Data, 'cm_micode' , 'cm_codename')
				});
				if (selScipt2 != null && selScipt2 != '') {
					$('[data-ax5select="cboScript2"]').ax5select('setValue', selScipt2, true);
					selScipt2 = null;
				}
			}

		});
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
//묶음 선택
function cboScript_Change() {
	if (getSelectedIndex('cboScript1') > 0 && getSelectedIndex('cboScript2') > 0) {
		getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode+getSelectedVal('cboScript1').cm_micode+getSelectedVal('cboScript2').cm_micode);
	} else if (getSelectedIndex('cboScript1') > 0) {
		getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode+getSelectedVal('cboScript1').cm_micode);
	} else if (getSelectedIndex('cboScript2') > 0) {
		getScript(getSelectedVal('cboBldCd').cm_bldgbn, getSelectedVal('cboBldCd').cm_micode+getSelectedVal('cboScript2').cm_micode);
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
		addId = item.cr_itemid;
		if($('#chkPrgAll').is(':checked')) {
			$('#chkPrg'+addId).wCheck('check', true);
		} else {
			$('#chkPrg'+addId).wCheck('check', false);
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

	var prgData = [];
	if(ulPrgInfoData.length > 0 ) {
		ulPrgInfoData.forEach(function(item, index) {
			addId = item.cr_itemid;
			if($('#chkPrg'+addId).is(':checked')) {
				prgData.push(item);
				prgSw = true;
			}
		})

		if(!prgSw) {
			dialog.alert('프로그램명을 선택한 후 등록하십시오.', function(){});
			return;
		}
	} else {
		dialog.alert('프로그램명을 선택한 후 등록하십시오.', function(){});
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
	if($('#chkExe').is(':checked')) {
		etcData.CM_USERYN = 'Y'
	} else {
		etcData.CM_USERYN = 'N'
	}
	if($('#chkNoExec').is(':checked')) {
		etcData.CM_NOEXECYN = 'Y'
	} else {
		etcData.CM_NOEXECYN = 'N'
	}

	etcData.cm_syscd 	= getSelectedVal('cboSysCd').cm_syscd;
	etcData.cm_qrycd 	= getSelectedVal('cboQry').cm_reqcd;
	etcData.cm_prcsys 	= getSelectedVal('cboPrcSys').cm_jobcd;
	etcData.cm_bldcd 	= getSelectedVal('cboBldCd').cm_micode;
	etcData.cm_bldgbn 	= getSelectedVal('cboBldCd').cm_bldgbn;
	etcData.cm_jobcd 	= jobCds;

	if($('#divScript').is(":visible")) {
		etcData.cm_bldcd2 = getSelectedVal('cboScript1').cm_micode;
		etcData.cm_bldcd3 = getSelectedVal('cboScript2').cm_micode;
	}

	var data = new Object();
	data = {
		etcData 		: etcData,
		delList 		: prgData,
		requestType	: 'getCmm0033_DBProc2'
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
		delObj.cm_itemid = delObj.cm_itemid;
		delObj.cm_rungbn = delObj.CM_RUNGBN;
		delList.push(delObj);
	});
	var data = new Object();
	data = {
		delList 	: delList,
		requestType	: 'getCmm0033_Del2'
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
			addId = item.cr_itemid;
			if (addId == selItem.cm_itemid) {
				$('#chkPrg'+selItem.cm_itemid).wCheck('check',true);
			} else {
				$('#chkPrg'+addId).wCheck('check', false);
			}
		});
	}
	
	

	$('#chkExe').wCheck('check',false);
	$('#chkLocal').wCheck('check',false);
	$('#chkSeq').wCheck('check',false);
	$('#chkSysSeq').wCheck('check',false);
	$('#chkBatch').wCheck('check',false);
	$('#optAfter').wCheck('check',true);
	$('#chkNoExec').wCheck('check',false);

	if(selItem.CM_RUNPOS === 'L') {
		$('#chkLocal').wCheck('check',true);
	}
	if(selItem.CM_USERYN === 'Y') {
		$('#chkExe').wCheck('check',true);
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
	if(selItem.CM_NOEXECYN === 'Y') {
		$('#chkNoExec').wCheck('check',true);
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
			} else if (prcSysVal === 'SYSUP' ) {
				if(bldGbn === '5') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if ((prcSysVal === 'SYSCB' || prcSysVal === 'SYSRCB')) {
				if(bldGbn === '2') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if ((prcSysVal === 'SYSED' || prcSysVal === 'SYSRED' || prcSysVal === 'SYSBED')) {
				if(bldGbn === '3') {
					bldCdArr.push(item);
				} else {
					continue;
				}
			} else if (prcSysVal === 'SYSRC' || prcSysVal === 'SYSRRC') {
				if(bldGbn === '6') {
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

function getPrgCd(pgmname){
	var data = new Object();
	data = {
		syscd 		: getSelectedVal('cboSysCd').cm_syscd,
		prgname 		: "",
		requestType	: 'getPrgCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetPrgCd);
}

function successGetPrgCd(data){
	ulPrgInfoData = data;
	
	makePrgInfoUlList();
}

function prg_change(){

	var newulPrgInfoData = [];
	if($("#prg_txt").val().trim().length < 1){
		$('#ulPrgInfo li').show();
	}
	
	ulPrgInfoData.forEach(function(item, index) {
		addId = item.rsrcname;
		
		if(addId.toUpperCase().indexOf($("#prg_txt").val().trim().toUpperCase()) < 0){
			$('#chkPrg'+item.cr_itemid).closest("li").hide();
		} else {
			$('#chkPrg'+item.cr_itemid).closest("li").show();
		}

	});
	
}

// 상단 연결 정보 리스트/프로그램종류 가져오기
function getPrgList() {
	var data = new Object();
	data = {
		SysCd 		: getSelectedVal('cboSysCd').cm_syscd,
		qrycd 		: getSelectedVal('cboQry').cm_reqcd,
		requestType	: 'getPrgList'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetPrgList);
}

// 상단 연결 정보 리스트/프로그램종류 가져오기 완료
function successGetPrgList(data) {
	conInfoAnPrgData = [];
	conInfoGridData = data;
	ulPrgInfoData	= [];
	conInfoGrid.setData(conInfoGridData);
}

// 프로그램종류 ul만들어주기
function makePrgInfoUlList() {
	$('#ulPrgInfo').empty();
	var liStr = null;
	var addId = null;
	ulPrgInfoData.forEach(function(prgItem, index) {
		addId = prgItem.cr_itemid;
		liStr  = '';
		liStr += '<li class="list-group-item dib width-100" style="min-width: 150px;">';
		liStr += '	<input type="checkbox" class="checkbox-prg" id="chkPrg'+addId+'" data-label="'+prgItem.rsrcname+'"  value="'+prgItem.cm_micode+'" />';
		liStr += '</li>';
		$('#ulPrgInfo').append(liStr);
	});

	$('input.checkbox-prg').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

}


// 시스템 cbo 가져오기
function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userId,
		SecuYn : "Y",
		SelMsg : "SEL",
		CloseYn : "N",
		ReqCd : "04",
		requestType	: 'getSysInfo',
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', sysData, 'json',successGetSysInfo);
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
		return;
	}

	qryAnPrcData = data;
	cboQryData = [];
	qryAnPrcData.forEach(function(item, index) {
		if (item.ID == 'QRYCD') cboQryData.push(item);
		/*if(item.ID === 'QRYCD') {
			findSw = false;
			if (item.cm_reqcd == '07' || item.cm_reqcd == '05') findSw = true; //체크인,개발폐기
			else if (item.cm_reqcd == '08' || item.cm_reqcd == '05' || item.cm_reqcd == '13') {
				//개발적용,개발폐기,개발복원
				if (getSelectedVal('cboSysCd').cm_systype == 'B' || getSelectedVal('cboSysCd').cm_systype == 'C' ||
					getSelectedVal('cboSysCd').cm_systype == 'D' || getSelectedVal('cboSysCd').cm_systype == 'E') {
					findSw = true;
				}
			} else if (item.cm_reqcd == '03' || item.cm_reqcd == '09' || item.cm_reqcd == '12') {
				//테스트적용,테스트폐기,테스트복원
				if (getSelectedVal('cboSysCd').cm_systype == 'C' || getSelectedVal('cboSysCd').cm_systype == 'D' ||
					getSelectedVal('cboSysCd').cm_systype == 'F' || getSelectedVal('cboSysCd').cm_systype == 'G') {
					findSw = true;
				}
			} else if (item.cm_reqcd == '04' || item.cm_reqcd == '10' || item.cm_reqcd == '06') {
				//운영적용,운영폐기,운영복원
				if (getSelectedVal('cboSysCd').cm_systype == 'D' || getSelectedVal('cboSysCd').cm_systype == 'E' ||
					getSelectedVal('cboSysCd').cm_systype == 'G' || getSelectedVal('cboSysCd').cm_systype == 'H') {
					findSw = true;
				}
			}
			cboQryData.push(item);
		}
		*/
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