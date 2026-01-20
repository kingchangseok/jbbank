/** 단위테스트 Tab(eCmc0300_tab.mxml) 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 */

var userName 		= window.top.userName;
var userId 			= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName	= window.top.userDeptName;
var userDeptCd 		= window.top.userDeptCd;
var reqCd	 		= window.parent.reqCd;

var srData 			= new Object(); // from UnitTest
var grdLst 			= new ax5.ui.grid();
var grdFile 		= new ax5.ui.grid();
var picker 			= new ax5.ui.picker();
var confirmDialog 	= new ax5.ui.dialog();
var loadExcelModal 	= new ax5.ui.modal();

var myWin 			= null;

var strIsrId 		= '';
var strStatus		= '';
var attPath 		= '';
var strUpURL 		= '';
var tmpPath 		= '';

var cboTestGbnData		= [];
var cboEditorData 		= [];
var grdLstData 			= [];
var grdFileData			= [];
var grdLstSimpleData	= [];
var grdFileSimpleData	= [];
var excelArray			= [];
var loadExcelFlag		= false;

//파일첨부 변수
var fileUploadModal 	= new ax5.ui.modal();
var fileIndex			= 0;
var TotalFileSize 		= 0;

//파일업로드 변수 (ComFileUpload에서 사용)
var fileGbn 			= 'U';
var dirGbn 				= '21';
var subDocPath 			= '';
var upFiles 			= [];
var	popCloseFlag 		= false;
var completeReadyFunc 	= false;

$('[data-ax5select="cboEditor"]').ax5select({ options : [] });
$('[data-ax5select="cboTestGbn"]').ax5select({ options : [] });

$('#datTestDay').val(getDate('DATE', 0));
picker.bind(defaultPickerInfo('testdate', 'top'));

$('input:radio[name="grpRst"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-open').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

grdLst.setConfig({
	target : $('[data-ax5grid="grdLst"]'),
	sortable : true,
	multiSort : true,
	header : {
		align : "center"
	},
	body : {
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
			console.log('grdLst_click =>', this.item);
			grdLst_click(this.item);
		}	
	},
	columns : [ 
		{key : "cc_caseseq",	label : "ID",		width : '5%',	align : 'left'},
		{key : "cm_codename",	label : "구분",		width : '10%',	align : 'left'},
		{key : "cc_casename",	label : "테스트케이스",	width : '25%',	align : 'left'},
		{key : "cc_etc",		label : "입력내용",	width : '20%',	align : 'left'},
		{key : "cc_exprst",		label : "예상결과",	width : '10%',	align : 'left'},
		{key : "testday",		label : "테스트수행일",	width : '10%',	align : 'center'},
		{key : "scmuser",		label : "수행인",		width : '10%',	align : 'center'},
		{key : "testrst",		label : "테스트결과",	width : '10%',	align : 'center'}
	]
});

grdFile.setConfig({
	target : $('[data-ax5grid="grdFile"]'),
	sortable : true,
	multiSort : true,
	header : {
		align : "center"
	},
	body : {
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
         	if (this.item.cc_savename == null || this.item.cc_savename == '' || this.item.cc_savename == undefined) {
            	confirmDialog.confirm({
        			title: '삭제확인',
        			msg: '[' + this.item.name +'] 파일을 삭제할까요?',
        		}, function(){
        			if(this.key === 'ok') {
        				grdFile.removeRow(grdFile.selectedDataIndexs[0]);
        				grdFileData = grdFile.getList();
        			}
        		});
         	}else {
         		fileDown(attPath+this.item.cc_savename, this.item.name);
         	}
		},
	},
	contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "파일삭제"}
        ],
        popupFilter: function (item, param) {
        	/** 
        	 * return 값에 따른 context menu filter
        	 * 
        	 * return true; -> 모든 context menu 보기
        	 * return item.type == 1; --> type이 1인 context menu만 보기
        	 * return item.type == 1 | item.type == 2; --> type 이 1,2인 context menu만 보기
        	 * 
        	 * ex)
	            	if(param.item.qrycd2 === '01'){
	            		return item.type == 1 | item.type == 2;
	            	}
        	 */
        	grdFile.clearSelect();
        	grdFile.select(Number(param.dindex));

	       	var selIn = grdFile.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
	        return true;
        },
        onClick: function (item, param) {
        	confirmDialog.confirm({
    			title: '삭제확인',
    			msg: '[' + param.item.name +'] 파일을 삭제할까요?',
    		}, function(){
    			if(this.key === 'ok') {
    				var tmpObj = new Object();
    				if(param.item.cc_srid == null || param.item.cc_division == null || param.item.cc_seq == null) {
    					tmpObj.cc_srid = '';
    					tmpObj.cc_division = '';
    					tmpObj.cc_seq = '';
    				}else {
    					tmpObj.cc_srid = param.item.cc_srid;
    					tmpObj.cc_division = param.item.cc_division;
    					tmpObj.cc_seq = param.item.cc_seq;
    				}
    				
    				grdFileData = [];
    				grdFile.setData([]);
    				deleteDoc(tmpObj);
    			}
    		});
        	grdFile.contextMenu.close();
		}
	},
	columns : [ 
		{key : "name",			label : "파일명",		width : '50%',	align : 'left'},
		{key : "createuser",	label : "첨부인",		width : '25%',	align : 'center'},
		{key : "createdate",	label : "첨부일",		width : '25%',	align : 'center'}
	]
});

$(document).ready(function() {
	
	reqCd = "43";
	
	// 개발자 변경
	$('#cboEditor').bind('change', function() {
		cboEditor_Change();
	});
	
	// 테스트케이스 신규
	$('#chkOpen').bind('click', function() {
		chkOpen_click();
	});
	
	// 등록/수정 버튼
	$('#btnAdd').bind('click', function() {
		btnAdd_Click();
	});
	
	// 삭제 버튼
	$('#btnDel').bind('click', function() {
		btnDel_click();
	});
	
	// 증빙첨부 버튼
	$('#btnTestLog').bind('click', function() {
		if($('#btnAdd').is(':disabled')) return;
		if(!$('#chkOpen').is(':checked') || grdLst.selectedDataIndexs < 0) return;
		
		fileOpen();
	});
	
	// 테스트케이스 복사 버튼
	$('#btnCaseCp').bind('click', function() {
		btnCaseCp_click();
	});
	
	// 엑셀로드 버튼
	$('#btnExlLoad').bind('click', function() {
		$('#excelFile').trigger('click');
	});
	
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		startUpload();
	})
	
	// 엑셀저장 버튼
	$('#btnExlSave').bind('click', function() {
		grdLst.exportExcel("unitTest_"+userId+".xls");
	});
	
	// 엑셀템플릿 버튼
	$('#btnExlTmp').bind('click', function() {
		fileDown(attPath+'/eCmc0300_excel_templet.xls', 'unitTest_templet.xls');
	});

	// 검수요청 버튼
	$('#btnTestRequest').bind('click', function() {
		btnTestRequest_click();
	});
	
	getData();
	geteCAMSDir();
	getCodeInfo();
	
	completeReadyFunc = true;
});

function geteCAMSDir() {
	var data = {
		pCode : '21,99,F2',
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json', successGeteCAMSDir);
}

function successGeteCAMSDir(data) {
	var tmpArray = new Array();

	tmpArray = data;
	if (tmpArray.length > 0) {
		for (var i = 0; tmpArray.length > i; i++) {
			if (tmpArray[i].cm_pathcd == 'F2') strUpURL = tmpArray[i].cm_path;
			else if (tmpArray[i].cm_pathcd == '21') attPath = tmpArray[i].cm_path;
			else tmpPath = tmpArray[i].cm_path;
		}
	} else {
		dialog.alert('파일을 저장할 디렉토리정보를 추출 중 오류가 발생하였습니다. 관리자에게 연락하여 주십시오.');
		return;
	}
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('UNITTSTGBN','SEL','N')
	]);
	
	cboTestGbnData = codeInfos.UNITTSTGBN;
	$('[data-ax5select="cboTestGbn"]').ax5select({
        options: injectCboDataToArr(cboTestGbnData, "cm_micode", "cm_codename")
	});
}

function getData() {
	if (window.parent.srData != null && window.parent.srData != '' && window.parent.srData != undefined) {
		srData = window.parent.srData;
		strIsrId = window.parent.srData.strIsrId;
		strStatus = window.parent.srData.strStatus;
	}
}

// UnitTest에서 호출
function screenInit(scrGbn) {
	getData();
	if (scrGbn == "M") {
		grdLstData = [];
		grdLst.setData([]);
		
		grdFileData = [];
		grdFile.setData([]);
		
		cboEditorData = [];
		$('[data-ax5select="cboEditor"]').ax5select({ options : [] });
		
		$('#txtTestId').val('자동부여');
		$('#chkOpen').wCheck('check', false);
	}
	
	$('#optFail').wRadio('check', true);
	grdFileSimpleData = [];
	
	$('#btnCaseCp').prop('disabled', true);
	$('#btnExlSave').prop('disabled', true);
	$('#btnExlLoad').prop('disabled', true);
	
	$('#txtTestId').val('');
	$('#txtCase').val('');
	$('#chkOpen').wCheck('disabled', true);
	
	$('#btnDel').prop('disabled', true);
	$('#btnAdd').prop('disabled', true);
	$('#btnTestRequest').hide();
	$('#btnTestRequest').prop('disabled', true);
	$('#txtTestDay').show();
	$('#datTestDay').prop("disabled", true); 
	disableCal(true, 'datTestDay');
	
	if(cboTestGbnData.length > 0) $('[data-ax5select="cboTestGbn"]').ax5select('setValue', cboTestGbnData[0].cm_micode, true);
	
	if(getSelectedIndex('cboEditor') > 0 && getSelectedVal('cboEditor').cc_scmuser == userId) {
		if(getSelectedVal('cboEditor').secuyn == 'OK' && (strStatus == '2' || strStatus == '4')) { // 2022.04.12 상태조건 주가
			$('#chkOpen').wCheck('disabled', false);
			if($('#chkOpen').is(':checked') || grdLst.selectedDataIndexs.length > 0) {
				$('#btnAdd').prop('disabled', false);
				if(!$('#chkOpen').is(':checked')) {
					$('#btnDel').prop('disabled', false);
				}
			}
			
			$('#btnCaseCp').prop('disabled', false);
			$('#btnExlLoad').prop('disabled', false);
		}
		
		if(grdLstSimpleData.length > 0) $('#btnExlSave').prop('disabled', false);
		
		//검수요청 버튼 활성화 = 등록/수정 가능하면서  해당시스템에 개발검수 SYSINFO 값이 1
		//테스트완료 버튼 활성화 = 등록/수정 가능하면서, 검수대상이 아니면서, CHECKINYN 함수 값이  OK 일때!!! ==> 체크인된 프로그램이 1건이라도 있는거.
		if(getSelectedVal('cboEditor').devChkEnabled == 'OK') {
			$('#btnTestRequest').show();
			$('#btnTestRequest').prop('disabled', false);
		}
	}
	
	screenInit_sub();
}

function screenInit_sub() {
	$('#txtRef').val('');
	$('#txtCase').val('');
	$('#txtTestDay').val('');
	$('#txtWriter').val('');
	$('#txtWriteDay').val('');
	if($('#chkOpen').is(':checked')) $('#txtTestId').val("자동부여");
	else $('#txtTestId').val('');
	$('#txtExpResult').val('');
	
	if($('#btnAdd').is(':disabled')) {
		$('[data-ax5select="cboTestGbn"]').ax5select("disable");
		$('#txtRef').prop("readonly", true);
		$('#txtCase').prop("readonly", true);
		$('#txtExpResult').prop("readonly", true);
		$('#optPass').wRadio("disabled", true);
		$('#optFail').wRadio("disabled", true);
		$('#divTestDay').show();
		$('#divPicker').hide();
	}else {
		$('[data-ax5select="cboTestGbn"]').ax5select("enable");
		$('#txtRef').prop("readonly", false);
		$('#txtCase').prop("readonly", false);
		$('#txtExpResult').prop("readonly", false);
		$('#optPass').wRadio("disabled", false);
		$('#optFail').wRadio("disabled", false);
		$('#divTestDay').hide();
		$('#divPicker').show();
		
		$('#btnTestLog').prop("disabled", false);
		if(getSelectedVal('cboEditor').pgmyn != 'OK') {
			$('#optPass').wRadio("disabled", true);
			$('#optFail').wRadio("disabled", true);
			$('#divTestDay').show();
			$('#divPicker').hide();
			$('#btnTestLog').prop("disabled", true);
		}
	}
}

function unitTestCall() {
	var data = {
		cc_srid : strIsrId,
		reqCD : reqCd,
		userID : userId,
		requestType : 'getScmuserList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json', successGetScmuserList);
}

function successGetScmuserList(data) {
	cboEditorData = data;
	$('[data-ax5select="cboEditor"]').ax5select({
        options: injectCboDataToArr(cboEditorData, "cc_scmuser", "scmuser")
	});
	
	if(cboEditorData.length > 0) {
		getCaseList(strIsrId, getSelectedVal('cboEditor').cc_scmuser);
		getDocList(strIsrId, reqCd);
		
		for(var i=0; i<cboEditorData.length; i++) {
			if(cboEditorData[i].cc_scmuser == userId) {
				$('[data-ax5select="cboEditor"]').ax5select('setValue', userId, true);
				break;
			}
		}
		
		if(getSelectedIndex('cboEditor') < 1 && cboEditorData.length == 2) {
			$('[data-ax5select="cboEditor"]').ax5select('setValue', cboEditorData[1].cc_scmuser, true);
		}
	}
	
	cboEditor_Change();
}

function getCaseList(srid, user) {
	var data = {
		cc_srid : srid,
		scmuser : user,
		requestType : 'getCaseList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json', successGetCaseList);
}

function successGetCaseList(data) {
	grdLstData = data;
	grdLstFilter(data); //grdLst.validateNow();
	
	if(grdLstData.length > 0) { 
		if(grdLstSimpleData.length > 0) {
			grdLst.select(grdLstSimpleData.length-1);
			grdLst.focus(grdLstSimpleData.length-1);
		}
		
		grdLst_click(null);
	}
	
	if(getSelectedIndex('cboEditor') > 0) {
		if(grdLstSimpleData.length == 0) {
			cboEditor_Change();
		}
	}
}

function grdLstFilter(oriData) {
	grdLstSimpleData = [];
	if(oriData.length > 0) {
		grdLstSimpleData = oriData.filter(function(data) {
			if(getSelectedIndex('cboEditor') > 0) {
				if(data.cc_scmuser != getSelectedVal('cboEditor').cc_scmuser) return false;
			}else {
				return false;
			}
			
			return true;
		});
	}
	
	grdLst.setData(grdLstSimpleData);
	grdLst.setConfig();
	grdLst.repaint();
}

function getDocList(srid, reqcd) {
	var data = {
		cc_srid : srid,
		reqCd : reqcd,
		requestType : 'getDocList'
	}
	console.log('[getDocList]==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successGetDocList);
}

function successGetDocList(data) {
	grdFileData = data;
	grdFileFilter(data);
}

function grdFileFilter(oriData) {
	grdFileSimpleData = [];
	if(oriData.length > 0) {
		grdFileSimpleData = oriData.filter(function(data) {
			if(getSelectedIndex('cboEditor') < 1) return false;
			
			if(!$('#chkOpen').is(':checked') && grdLst.selectedDataIndexs.length < 1) return false;
			
			if($('#chkOpen').is(':checked') && data.cc_caseseq == '999') return true;
			else if($('#chkOpen').is(':checked')) return false;
			
			if(data.cc_caseseq == grdLst.list[grdLst.selectedDataIndexs].cc_caseseq) return true;
			else return false;
		});
	}
	
	grdFile.setData(grdFileSimpleData);
	grdFile.setConfig();
	grdFile.repaint();
}

function cboEditor_Change() {
	grdLstFilter(grdLstData);
	
	grdFileData = [];
	grdFileSimpleData = [];
	grdFile.setData(grdFileData);
	
	getDocList(strIsrId, reqCd);
	initDate();
	screenInit('S');
}


function initDate() {
	$('#datTestDay').val(getDate('DATE', 0));
	picker.bind(defaultPickerInfo('testdate', 'top'));
}

function grdLst_click(selItem) {
	if(selItem == null) {
		selItem = grdLst.getList('selected')[0];
	}
	
	grdFileData = [];
	grdFileSimpleData = [];
	grdFile.setData(grdFileData);
	
	getDocList(strIsrId, reqCd);
	screenInit('S');
	
	$('#chkOpen').wCheck('check', false);
	
	$('#txtWriter').val(selItem.createuser);
	$('#txtWriteDay').val(selItem.createdate);
	
	if(selItem.testday != null && selItem.testday != undefined && selItem.testday != '') {
		$('#txtTestDay').val(selItem.testday);
		$('#datTestDay').val(selItem.testday);
		picker.bind(defaultPickerInfo('testdate', 'top'));
	}else {
		initDate();
	}
	
	$('#txtTestId').val(selItem.cc_caseseq);
	$('[data-ax5select="cboTestGbn"]').ax5select("setValue", selItem.cc_testgbn, true);
	
	$('#txtCase').val(selItem.cc_casename);
	$('#txtExpResult').val(selItem.cc_exprst);
	$('#txtRef').val(selItem.cc_etc);
	
	$('#optFail').wRadio('check', true);
	if(selItem.cc_testrst != null && selItem.cc_testrst != undefined && selItem.cc_testrst != '') {
		if(selItem.cc_testrst == 'P') {
			$('#optPass').wRadio('check', true);
		}
	}
}

function chkOpen_click() {
	console.log('chkOpen_click');
	//신규거나, 수정이면서 그리드선택안되어 있으면
	if($('#chkOpen').is(':checked') || (!$('#chkOpen').is(':checked') && grdLst.selectedDataIndexs < 0)) {
		console.log('1');
		var findSw = false;
		
		grdFileFilter([]);
		initDate();
		screenInit('S');
	}else {
		console.log('2');
		grdLst_click(null);
	}
}

//등록/수정 버튼
function btnAdd_Click() {
	if(strIsrId == null || strIsrId == '' || strIsrId == undefined) {
		dialog.alert('SR이 선택되지 않았습니다.');
		return;
	}
	
	if(getSelectedIndex('cboEditor') < 1) {
		dialog.alert('개발자를 선택하여 주시기 바랍니다.');
		return;
	}
	
	if(getSelectedIndex('cboTestGbn') < 1) {
		dialog.alert('구분을 선택하여 주시기 바랍니다.');
		return;
	}
	
	if($('#txtCase').val().trim().length == 0) {
		dialog.alert('테스트케이스를 입력하여 주시기 바랍니다.');
		return;
	}
	
	if($('#txtRef').val().trim().length == 0) {
		dialog.alert('입력내용을 입력하여 주시기 바랍니다.');
		return;
	}
	
	if($('#txtExpResult').val().trim().length == 0) {
		dialog.alert('예상결과를 입력하여 주시기 바랍니다.');
		return;
	}
	
	var etcObj = new Object();
	etcObj.testrst = '';
	if($('#optPass').is(':checked') || $('#optFail').is(':checked')) {
		if(getSelectedVal('cboEditor').pgmyn != 'OK') {
			dialog.alert('선택한 SR-ID로 체크인한 프로그램이 없습니다.\n프로그램에 대한 체크인 후 결과를 등록하여 주시기 바랍니다.');
			return;
		}
	
		var nowDate = getDate('DATE',0);
		var strDate = replaceAllString($("#datTestDay").val(), '/', '');
		if(nowDate < strDate) {
			dialog.alert("테스트일자가 현재일 이후입니다. 정확히 선택하여 주십시오.");
			return;
		}
		
		etcObj.testday = strDate;
		if($('#optPass').is(':checked')) etcObj.testrst = 'P';
		else etcObj.testrst = 'F';
	}	
	
	etcObj.srId = strIsrId;
	etcObj.UserId = userId;
	if($('#chkOpen').is(':checked')) etcObj.testid = null;
	else etcObj.testid = grdLst.getList('selected')[0].cc_caseseq;
	etcObj.testgbn = getSelectedVal('cboTestGbn').cm_micode;
	etcObj.Tcase = $('#txtCase').val().trim();
	etcObj.txtRef = $('#txtRef').val().trim();
	etcObj.ExpResult = $('#txtExpResult').val().trim();
	
	var data = {
		etcData : etcObj,
		requestType : 'setCaseInfo'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json', successSetCaseInfo);
	
	etcObj = null;
}

function successSetCaseInfo(data) {
	if(data.substring(0,2) == 'OK') {
		var caseSeq = '';
		var upFiles = [];
		var tmpObj = new Object();
		
		if($('#chkOpen').is(':checked')) caseSeq = '999';
		else caseSeq = grdLst.getList('selected')[0].cc_caseseq;
		
		for(var i=0; i<grdFileSimpleData.length; i++) {
			if(grdFileSimpleData[i].cc_savename == null || grdFileSimpleData[i].cc_savename == '' || grdFileSimpleData[i].cc_savename == undefined) {
				if(caseSeq == grdFileSimpleData[i].cc_caseseq) {
					tmpObj = new Object();
					tmpObj = grdFileSimpleData[i];
					tmpObj.cc_caseseq = data.substr(2);
					upFiles.push(tmpObj);
					tmpObj = null;
				}
			}
		}
		
		if(upFiles.length > 0) {
			startFileupload();
		}else {
			caseFileEnd(true);
		}
	}
}

function startFileupload() {
var fileseq = 0;
	
	fileGbn = 'U';
	dirGbn = '21';
	popCloseFlag = false;
	
	//attPath = 'C:\\html5\\temp\\';
	
	for(var i=0; i<grdFileData.length; i++) {
		grdFileData[i].sendflag = false;
		
		if(grdFileData[i].cc_seq == null || grdFileData[i].cc_seq == '' || grdFileData[i].cc_seq == undefined) {
			if(fileseq < 10) {
				subDocPath = '/SR/' + strIsrId.substr(1,6);
				grdFileData[i].savefile = subDocPath + '/' + strIsrId + '_' + reqCd + '_0' + fileseq;
				grdFileData[i].saveFileName = strIsrId + '_' + reqCd + '_0' + fileseq;
				grdFileData[i].fileseq = '0' + fileseq;
			}else {
				subDocPath = '/SR/' + strIsrId.substr(1,6);
				grdFileData[i].savefile = subDocPath + '/' + strIsrId + '_' + reqCd + '_' + fileseq;
				grdFileData[i].saveFileName = strIsrId + '_' + reqCd + '_' + fileseq;
				grdFileData[i].fileseq = '' + fileseq;
			}
		}else {
			fileseq = grdFileData[i].cc_seq;
			grdFileData[i].fileseq = grdFileData[i].cc_seq;
		}
		
		grdFileData[i].attfile = grdFileData[i].name;
		grdFileData[i].fullName = attPath+subDocPath;
		grdFileData[i].fullpath = attPath;
		++fileseq;
	}
	
	upFiles = clone(grdFileData);
	console.log('[UnitTestTab.js] upFiles==>',upFiles);
	
	setTimeout(function() {
		fileUploadModal.open({
			width: 685,
			height: 420,
			iframe: {
				method: "get",
				url: "../../modal/fileupload/ComFileUpload.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
				}
			}
		});
	}, 200);
}

function caseFileEnd(ret) {
	if(ret) dialog.alert('단위테스트 등록처리가 정상적으로 처리되었습니다.');
	
	initDate();
	screenInit('S');
	window.parent.cmdQry_click();
}

// 삭제 버튼
function btnDel_click() {
	if(strIsrId == null || strIsrId == '' || strIsrId == undefined) return; 
	if($('#chkOpen').is(':checked')) return;
	if(grdLst.selectedDataIndexs < 0) {
		dialog.alert('테스트케이스를 선택한 후 처리하시기 바랍니다.');
		return;
	}
	
	var etcObj = new Object();
	etcObj.IsrId = strIsrId;
	etcObj.UserId = userId;
	etcObj.testid = grdLst.getList('selected')[0].cc_caseseq;

	var data = {
		etcData : etcObj,
		requestType : 'delCaseInfo'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json', successDelCaseInfo);
	etcObj = null;	
}

function successDelCaseInfo(data) {
	if(data.substring(0,2) == 'OK') {
		dialog.alert('단위테스트 삭제처리가 정상적으로 처리되었습니다.');
		
		initDate();
		screenInit('S');
	}else {
		dialog.alert('단위테스트 삭제 중 오류가 발생하였습니다.');
	}
	
	window.parent.cmdQry_click();
}

// 증빙첨부 버튼
function fileOpen(){
	if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
		fileIndex++;
		$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
	}
	$("#file"+fileIndex).click();
}

function fileChange(file) {
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	var spcChar = "{}<>?|~`!@#$%^&*+\"'\\/";
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 
		var fileCk = true;
		
		for(var i=0; i<jqueryFiles.files.length; i++){
			var sizeCount = 0;
			var size = jqueryFiles.files[i].size/1024; // Byte, KB, MB, GB
			while(size > 1024 && sizeCount < 2){
				size = size/1024;
				sizeCount ++;
			}
			size = Math.round(size*10)/10.0 + fileSizeArr[sizeCount];
			var sizeReal = jqueryFiles.files[i].size;
			var name = jqueryFiles.files[i].name
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				fileCk = false;
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				fileCk = false;
				break;
			}
			
			for(var j=0; j<grdFileData.length; j++){

				for (k=0;spcChar.length>k;k++) {
					if (name.indexOf(spcChar.substr(k,1))>=0) {
						dialog.alert("첨부파일명에 특수문자를 입력하실 수 없습니다.\n특수문자를 제외하고 첨부하여 주시기 바랍니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
				}
				if(!fileCk){
					break;
				}
				
				if(grdFileData[j].name == name){
					dialog.alert("파일명 : " + name +"\n 은 이미 추가 되어 있는 파일명 입니다.\n확인 후 다시 등록해 주세요");
					$("#"+file).remove();
					fileCk = false;
					break;
				}
				fileCk = true;
			}

			if(fileCk){
				var tmpObj = new Object(); // 그리드에 추가할 파일 속성
				tmpObj.name = name;
				tmpObj.size = size;
				tmpObj.sizeReal = sizeReal;
				tmpObj.newFile = true;
				tmpObj.realName = name;
				tmpObj.file = jqueryFiles.files[i];
				
				if($('#chkOpen').is(':checked')) tmpObj.cc_caseseq = '999';
				else tmpObj.cc_caseseq = grdLst.getList('selected')[0].cc_caseseq;
				
				grdFileData.push(tmpObj);
			}
			else{
				break;
			}
		}
//		grdFile.setData(grdFileSimpleData);
		grdFileFilter(grdFileData);
		
		if(fileCk){
			dialog.alert("파일첨부 시 등록/수정 버튼을 클릭해야 파일이 저장됩니다.");
		}
	}
}

// 테스트케이스복사
function btnCaseCp_click() {
	if(strIsrId == null || strIsrId == '' || strIsrId == undefined) return; 
	
	var nHeight, nWidth, cURL, winName;
	
	if ( ('pop_'+strIsrId) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
	
	winName = 'pop_'+strIsrId;
	
	var f = document.popPam;
	f.user.value 	= userId;
	f.scmuser.value = userId;
	f.code.value	= 'CP';   
	f.redcd.value   = reqCd;	
	f.isrId.value   = strIsrId;
	
	nHeight = 740;
	nWidth  = 1200;

	cURL = "/webPage/winpop/PopCopyTestCase.jsp"; //eCmc0050.jsp
	myWin = winOpen(f, winName, cURL, nHeight, nWidth);

	/*var interval = null;
    interval = window.setInterval(function() {
        try {
            if( myWin == null || myWin.closed ) {
                window.clearInterval(interval);
                myWin = null;
				getUnitTest();
				getCaseList(strUserId, uuserId);
            }
        } catch (e) {}
    }, 500);*/
}

function PopCopyTestCaseCallBack() {
	copyAftCall();
}

// 엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;

	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	// 테스트 임시경로
	formData.append('fullName',tmpPath+userId+"_excel_eCmc0300.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);

    $.ajax({
		url: homePath+'/webPage/fileupload/'+strUpURL,
        type:'POST',
        data:formData,
        async:false,
        cache:false,
        contentType:false,
        processData:false
    }).done(function(response){
    	onUploadCompleteData(response);
    }).fail(function(xhr,status,errorThrown){
    	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
    }).always(function(){
    	// file 초기화
    	var agent = navigator.userAgent.toLowerCase();
    	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
    	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
    	} else {
    	    $("#excelFile").val("");
    	}
    });
}

//엑셀파일 완료 후
function onUploadCompleteData(filePath){
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");

	headerDef.push("caseid");
	headerDef.push("casegbn");
	headerDef.push("casename");
	headerDef.push("caseetc");
	headerDef.push("caserst");
	headerDef.push("casedate");
	headerDef.push("caseuser");
	headerDef.push("caseresult");

	var tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

//읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (typeof tmpPath === 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	excelArray = data;
	
	setTimeout(function() {			
		loadExcelModal.open({
			width: 400,
			height: 700,
			iframe: {
				method: "get",
				url: "../../modal/LoadExcelModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
					if(loadExcelFlag) {
						copyAftCall();
					}
				}
			}
		}, function () {
		});
	}, 200);
}

function copyAftCall() {
	grdLstSimpleData = [];
	if(getSelectedVal('cboEditor') == null || getSelectedVal('cboEditor') == undefined) return;
	
	getCaseList(strIsrId, getSelectedVal('cboEditor').cc_scmuser);
}

function btnTestRequest_click() {
	if(reqCd == '43') { //단위테스트
		//개발검수요청 상태로 업데이트  ISRSTAUSR [7]:개발검수요청
		var data = {
			srid : strIsrId,
			userid : userId,
			status : '7',
			requestType: 'setStaCmc0110'
		}
		
		ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successSetStaCmc0110);
	}
}

function successSetStaCmc0110(data) {
	if(data > 0) {
		dialog.alert('개발검수요청을 완료했습니다.');
		window.parent.getQry_click();
	}else {
		dialog.alert('요청을 실패했습니다.\n다시 로그인 후 이용해 주시기 바랍니다.');
	}
}

//첨부파일 삭제
function deleteDoc(data) {
	var data = {
		tmp_obj : data,
		requestType : 'deleteDoc'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successDeleteDoc);
}

function successDeleteDoc(data) {
	if(data == 'OK') {
		grdFile.repaint();
	}
}

function setFile(fileList) {
	var data = {
		srid : strIsrId,
		reqcd : reqCd,
		userid : userId,
		doc_list: fileList,
		requestType : 'insertDocList'
	}
	console.log('[UnitTestTab.js] insertDocList ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', caseFileEnd);
}
