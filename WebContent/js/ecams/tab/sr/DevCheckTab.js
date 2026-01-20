/** 개발검수 Tab(eCmc0400_tab.mxml) 화면 정의
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
var QCYN			= window.parent.QCYN;

var srData 			= new Object(); // from UnitTest
var grdLst 			= new ax5.ui.grid();
var grdFile 		= new ax5.ui.grid();
var grdCond 		= new ax5.ui.grid();
var grdChk 			= new ax5.ui.grid();
var picker 			= new ax5.ui.picker();
var confirmDialog 	= new ax5.ui.dialog();
var loadExcelModal 	= new ax5.ui.modal();

var myWin 			= null;

var booReject		= false;
var strIsrId 		= '';
var strStatus		= '';
var attPath 		= '';
var strUpURL 		= '';
var tmpPath 		= '';

var userInfoData		= [];
var cboEditorData 		= [];
var grdLstData 			= [];
var grdFileData			= [];
var grdCondData			= [];
var grdChkData			= [];
var grdLstSimpleData	= [];
var grdFileSimpleData	= [];
var grdCondSimpleData	= [];
var grdChkSimpleData	= [];
var excelArray			= [];
var loadExcelFlag		= false;
var completeReadyFunc 	= false;

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

$('[data-ax5select="cboEditor"]').ax5select({ options : [] });

$('#datTestDay').val(getDate('DATE',0));
picker.bind(defaultPickerInfo('testdate', 'top'));

$('input:radio[name="grpRst"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-open').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

var grdChkCol1 = [ 
	{key : "cc_itemmsg",	label : "확인사항",	width : '60%',	align : 'left', editor: {type: "text"}},
	{label: "결과",  width: '40%', align: 'left', columns : [	
		 {key: "selected1", label: "정상",  width: '20%', align: 'center',
			 editor:{
				   type:"checkbox" , config:{trueValue: "1", falseValue: "0", label: "selected1"}
			   },
		 },
		 {key: "selected2", label: "비정상",  width: '20%', align: 'center',
			 editor:{
				 type:"checkbox" , config:{trueValue: "1", falseValue: "0", label: "selected2"}
			 },
		 }
	]}
];

var grdChkCol2 = [ 
	{key : "cc_itemmsg",	label : "확인사항",	width : '60%',	align : 'left', editor: {type: "text"}}
];

var gridSw1 = false;
var gridSw2 = false;
createViewGrid1();
createViewGrid2();

function createViewGrid1() {
	grdCond.setConfig({
		target : $('[data-ax5grid="grdCond"]'),
		sortable : true,
		multiSort : true,
		multipleSelect : false,
		header : {
			align : "center"
		},
		body : {
			onClick : function() {
				this.self.clearSelect();
				this.self.select(this.dindex);
				
				$('#btnCondDel').prop('disabled', true);
				if(!$('#btnCondAdd').is(':disabled')) {
					$('#btnCondDel').prop('disabled', false);
				}
			}	
		},
		columns : [ 
			{key : "cc_itemmsg",	label : "테스트조건",	width : '100%',	align : 'left', editor: {type: "text"}}
		]
	});	
}

function createViewGrid2() {
	grdChk.setConfig({
		target : $('[data-ax5grid="grdChk"]'),
		sortable : true,
		multiSort : true,
		multipleSelect : false,
		header : {
			align : "center"
		},
		body : {
			onClick : function() {
				if (this.colIndex == 1 || this.colIndex == 2) {
					console.log('selected1==>', this.item.selected1);
					console.log('selected2==>', this.item.selected2);
					if(this.colIndex == 1 && this.item.selected1 == '1') this.item.selected2 = '0';
					if(this.colIndex == 1 && this.item.selected1 == '0') this.item.selected2 = '1';
					if(this.colIndex == 2 && this.item.selected2 == '1') this.item.selected1 = '0';
					if(this.colIndex == 2 && this.item.selected2 == '0') this.item.selected1 = '1';
					this.self.repaint();
	        		return;
				}
				this.self.clearSelect();
				this.self.select(this.dindex);
				
				$('#btnChkDel').prop('disabled', true);
				if(!$('#btnChkAdd').is(':disabled')) {
					$('#btnChkDel').prop('disabled', false);
				}
			}	
		},
		columns : grdChkCol1
	});
}

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
			console.log('grdLst_click =>', this.item, grdChkSimpleData);			
			grdLst_click(this.item);
			changeTabMenu();
		}	
	},
	columns : [ 
		{key : "cc_caseseq",	label : "테스트ID",		width : '5%',	align : 'center'},
		{key : "cc_casename",	label : "테스트케이스",		width : '25%',	align : 'left'},
		{key : "lastdt",		label : "최종등록일",		width : '10%',	align : 'center'},
		{key : "username",		label : "등록인",			width : '10%',	align : 'center'},
		{key : "testuser",		label : "테스트수행인",		width : '10%',	align : 'center'},
		{key : "testday",		label : "테스트수행일",		width : '10%',	align : 'center'},
		{key : "cc_worktime",	label : "투입시간",		width : '10%',	align : 'center'},
		{key : "cc_testrst",	label : "최종결과의견",		width : '10%',	align : 'left'},
		{key : "testrst",		label : "결과",			width : '10%',	align : 'left'}
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

	$('#tab1').width($('#tab1').width()+10);
	$('#tab2').width($('#tab2').width()+10);
	setTabMenu();
	
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
	
	// 개발검수완료 버튼
	$('#btnTestEndding').bind('click', function() {
		btnTestEndding_click();
	});
	
	// 검수반려 버튼
	$('#btnReject').bind('click', function() {
		btnReject_click();
	});
	
	// 증빙첨부 버튼
	$('#btnTestLog').bind('click', function() {
		if($('#btnAdd').is(':disabled')) return;
		if(!$('#chkOpen').is(':checked') && grdLst.getList('selected').length < 1) return;
		
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
		fileDown(attPath+'/eCmc0400_excel_templet.xls', 'sysTest_templet.xls');
	});
	
	// 테스트조건 추가 버튼
	$('#btnCondAdd').bind('click', function() {
		var tmpObj = new Object();
		tmpObj.cc_itemmsg = '';
		if($('#chkOpen').is(':checked')) {
			tmpObj.cc_caseseq = '999';
		}else {
			var selItem = grdLst.getList('selected')[0];
			tmpObj.cc_caseseq = selItem.cc_caseseq;
			if(reqCd != '44') {
				tmpObj.cc_testuser = getList.cc_testuser;
				tmpObj.cc_testgbn = getList.cc_testgbn;
			}
		}
		
		tmpObj.cc_gbncd = 'C';
		tmpObj.editable = '1';
		tmpObj.focusable = '1';
		tmpObj.cc_seqno = '999';
		tmpObj.cc_scmuser = getSelectedVal('cboEditor').cc_scmuser;
		grdCondData.push(tmpObj);
		grdCondSimpleData = clone(grdCondData);
		console.log('grdCondSimpleData=',grdCondSimpleData);
		grdCond.setData(grdCondSimpleData);
		grdCond.select(grdCond.getList().length-1);
		
		if(grdCondSimpleData.length > 0) $('#btnCondDel').prop('disabled', false);
		else $('#btnCondDel').prop('disabled', true);
	});
	
	// 테스트조건 제거 버튼
	$('#btnCondDel').bind('click', function() {
		if(grdCond.selectedDataIndexs < 0) return;
		grdCond.removeRow("selected");
		grdCondData = clone(grdCond.getList());
		grdCondSimpleData = clone(grdCondData);
		grdCond.setData(grdCondSimpleData);
	});
	
	// 확인사항 추가 버튼
	$('#btnChkAdd').bind('click', function() {
		var tmpObj = new Object();
		tmpObj.cc_itemmsg = '';
		if($('#chkOpen').is(':checked')) {
			tmpObj.cc_caseseq = '999';
		}else {
			var selItem = grdLst.getList('selected')[0];
			tmpObj.cc_caseseq = selItem.cc_caseseq;
			if(reqCd != '44') {
				tmpObj.cc_testuser = getList.cc_testuser;
				tmpObj.cc_testgbn = getList.cc_testgbn;
			}
		}
		
		tmpObj.cc_gbncd = 'R';
		tmpObj.editable = '1';
		tmpObj.focusable = '1';
		tmpObj.selected1 = '0'; //정상
		tmpObj.selected2 = '1';	//비정상
		tmpObj.cc_seqno = '999';
		tmpObj.cc_scmuser = getSelectedVal('cboEditor').cc_scmuser;
		grdChkData.push(tmpObj);
		grdChkSimpleData = clone(grdChkData);
		grdChk.setData(grdChkSimpleData);
		grdChk.select(grdChk.getList().length-1);
		
		if(grdChkSimpleData.length > 0) $('#btnChkDel').prop('disabled', false);
		else $('#btnChkDel').prop('disabled', true);
	});
	
	// 확인사항 제거 버튼
	$('#btnChkDel').bind('click', function() {
		if(grdChk.selectedDataIndexs < 0) return;
		grdChk.removeRow("selected");
		grdChkData = clone(grdChk.getList());
		grdChkSimpleData = clone(grdChkData);
		grdChk.setData(grdChkSimpleData);
	});
	
	getData();
	geteCAMSDir();
	resizeSub();
	
	completeReadyFunc = true;
});

function setTabMenu() {
	$("#tab2").show();
	$("#tab2").hide();
	$("#tab1").show();
	
	if (!gridSw1) {
		createViewGrid1();
		gridSw1 = true;
	}
	
	$("ul.tabs li").click(function () {
		changeTabMenu();
		
		$(".tab_content").hide();
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		$("#" + activeTab).fadeIn();

		if(this.id === 'tab1Li') {
			grdCond.setConfig();
			grdCond.align();
			grdCond.repaint();
		}else if(this.id === 'tab2Li') {
			setTimeout(function() {
				if (!gridSw2) {
					createViewGrid2();
					gridSw2 = true;
				}
			}, 10);
			
			grdChk.setConfig();
			grdChk.align();
			setTimeout(function() {
				grdChk.repaint();
			}, 10);		
		}
	});
}

function changeTabMenu() {
	if(document.getElementById("tab1").className == "on") {	
		grdCond.setConfig();
		grdCond.align();
		grdCond.repaint();
	}else if(document.getElementById("tab2").className == "on") {
		setTimeout(function() {
			if (!gridSw2) {
				createViewGrid2();
				gridSw2 = true;
			}
		}, 10);
		
		grdChk.setConfig();
		grdChk.align();
		setTimeout(function() {
			grdChk.repaint();
		}, 10);	
	}
}

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

function getData() {
	if (window.parent.srData != null && window.parent.srData != '' && window.parent.srData != undefined) {
		srData = window.parent.srData;
		strIsrId = window.parent.srData.strIsrId;
		strStatus = window.parent.srData.strStatus;
	}
}

function resizeSub() {
	if(reqCd == '54' || reqCd == '55' || reqCd == '55') {
		grdChk.config.columns = grdChkCol1;
		grdChk.setConfig();
	}else {
		grdChk.config.columns = grdChkCol2;
		grdChk.setConfig();
	}
	
	if(document.getElementById("tab1").className == "on") {
	}else if(document.getElementById("tab2").className == "on") {
	}
}

// DevCheck에서 호출
function screenInit(scrGbn) {
	getData();
	if (scrGbn == "M") {
		changeTabMenu();
			
		grdLstData = [];
		grdLst.setData([]);
		
		grdFileData = [];
		grdFile.setData([]);
		
		grdCondData = [];
		grdCond.setData([]);
		
		grdChkData = [];
		grdChk.setData([]);
		
		cboEditorData = [];
		$('[data-ax5select="cboEditor"]').ax5select({ options : [] });
		
		$('#txtTestId').val('자동부여');
		$('#chkOpen').wCheck('disabled', true);
		$('#chkOpen').wCheck('check', false);
		
		$('#btnCaseCp').hide();
	}
	
	$('#btnCaseCp').prop('disabled', true);
	$('#btnExlSave').prop('disabled', true);
	$('#btnExlLoad').prop('disabled', true);
	$('#btnExlTmp').prop('disabled', true);
	$('#btnTestLog').prop('disabled', true);
	
	$('#txtExpTime').val('');
	$('#txtTestId').val('');
	$('#txtCase').val('');
	$('#txtTester').val('');
	$('#txtResult').val('');
	$('#btnTestEndding').prop('disabled', true);
	
	$('#chkOpen').wCheck('disabled', true);
	$('#btnDel').prop('disabled', true);
	$('#btnAdd').prop('disabled', true);
	$('#btnReject').prop('disabled', true);
	booReject = false;
	
	$('#btnCondAdd').prop('disabled', true);
	$('#btnCondDel').prop('disabled', true);
	$('#btnChkAdd').prop('disabled', true);
	$('#btnChkDel').prop('disabled', true);
	
	$('#txtResult').prop('readonly', true);
	$('#txtResult').prop('disalbed', true);
	$('#datTestDay').prop("disabled", true); 
	disableCal(true, 'datTestDay');
	$('#txtExpTime').prop('readonly', true);
	$('#txtExpTime').prop('disalbed', true);
	
	console.log('1. cboEditor =>'+getSelectedVal('cboEditor') );
	if(grdLstSimpleData.length > 0) $('#btnExlSave').prop('disabled', false);
	if(getSelectedVal('cboEditor') == null || getSelectedVal('cboEditor') == undefined) return;
	
	console.log('1. reqCd=>'+reqCd+','+getSelectedIndex('cboEditor'));
	QCYN = window.parent.QCYN;
	if(reqCd == '54' || reqCd == '55') { // 54:개발검수, 55:사후통테
		if(getSelectedIndex('cboEditor') > 0 && getSelectedVal('cboEditor').secuyn == 'OK' && getSelectedVal('cboEditor').devChkEnabled == 'OK') {
			if(grdLstData.length > 0 && grdLst.getList('selected').length > 0 && grdLst.getList('selected')[0].cc_testuser == userId) {
				if(QCYN) {
					console.log('1. QCYN=>'+QCYN);
					$('#chkOpen').wCheck('disabled', false);
					$('#btnCaseCp').show();
					$('#btnCaseCp').prop('disabled', false);
					$('#btnExlLoad').prop('disabled', false);
					$('#btnExlTmp').prop('disabled', false);
					if(!$('#chkOpen').is(':checked')) {
						booReject = true;
					}
					$('#txtResult').prop('readonly', false);
					$('#txtResult').prop('disalbed', false);
					$('#datTestDay').prop("disabled", false);
					disableCal(false, 'datTestDay');
					$('#txtExpTime').prop('readonly', false);
					$('#txtExpTime').prop('disalbed', false);
				}
				
				if(getSelectedVal('cboEditor').devChkEnabled == 'OK') $('#btnAdd').prop('disabled', false);
				if(getSelectedVal('cboEditor').pgmyn == 'OK') $('#btnTestEndding').prop('disabled', false);
				if($('#chkOpen').is(':checked')) {
					$('#btnDel').prop('disabled', true);
					$('#btnCondAdd').prop('disabled', false);
					$('#btnChkAdd').prop('disabled', false);
					$('#btnTestLog').prop('disabled', false);
				}
			}else if(QCYN) {
				console.log('2. QCYN=>'+QCYN);
				$('#chkOpen').wCheck('disabled', false);
				if(getSelectedVal('cboEditor').pgmyn == 'OK') $('#btnTestEndding').prop('disabled', false);
				$('#btnCaseCp').show();
				$('#btnCaseCp').prop('disabled', false);
				$('#btnExlLoad').prop('disabled', false);
				$('#btnExlTmp').prop('disabled', false);
				if($('#chkOpen').is(':checked')) {
					$('#btnAdd').prop('disabled', false);
					$('#btnCondAdd').prop('disabled', false);
					$('#btnChkAdd').prop('disabled', false);
				}else {
					booReject = true;
				}
				$('#btnTestLog').prop('disabled', false);
				$('#txtResult').prop('readonly', false);
				$('#txtResult').prop('disalbed', false);
				$('#datTestDay').prop("disabled", false);
				disableCal(false, 'datTestDay');
				$('#txtExpTime').prop('readonly', false);
				$('#txtExpTime').prop('disalbed', false);
			}
		}
	}
}

function sysTestCall() {
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
		getTestCaseList_call(strIsrId, reqCd);
		
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
	
	//cboEditor_Change();
}

function getTestCaseList_call(srid, reqcd) {
	var data = {
		cc_srid : srid,
		reqCD : reqcd,
		requestType : 'getTestCaseList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successGetTestCaseList);
}

function successGetTestCaseList(data) {
	grdLstData = data;
	grdLstFilter(data);
	
	if(grdLstData.length > 0) { 
		var data = {
			cc_srid : strIsrId,
			UserId : userId,
			reqCD : reqCd,
			requestType : 'getTestCase_sub'
		}
		ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successGetTestCaseSub);
		
		data = {
			cc_srid : strIsrId,
			requestType : 'getTestCase_sub_tester'
		}
		ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successGetTestCaseSubTester);
		
		if(reqCd != '44') {
			data = {
				cc_srid : strIsrId,
				reqCd : reqCd,
				requestType : 'getDocList'
			}
			ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successGetDocList);
		}
	}else {
		cboEditor_Change();
	}
}

function successGetTestCaseSub(data) {
	grdCondData = data;
	grdCondFilter(grdCondData);
	
	grdChkData = data;
	grdChkFilter(grdChkData);
	
	if(getSelectedIndex('cboEditor') > 0) {
		cboEditor_Change();
	}
}

function successGetTestCaseSubTester(data) {
	//flex에 주석처리 되어있음
}

function successGetDocList(data) {
	grdFileData = data;
	grdFileFilter(grdFileData);
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

function grdFileFilter(oriData) {
	grdFileSimpleData = [];
	if(oriData.length > 0) {
		grdFileSimpleData = oriData.filter(function(data) {
			if(getSelectedIndex('cboEditor') < 1) return false;
			if($('#chkOpen').is(':checked') && data.cc_caseseq == '999') return true;
			else if($('#chkOpen').is(':checked')) return false;
			if(grdLst.getList('selected').length < 1) return false;
			if(!$('#chkOpen').is(':checked') && grdLst.getList('selected').length < 1) return false;
			if(data.cc_caseseq == grdLst.getList('selected')[0].cc_caseseq) {
				if(data.cc_division == reqCd) return true;
				else if(reqCd == 'XX') {
					if(data.cc_division == '54' && grdLst.getList('selected')[0].cc_testgbn != 'A') return true;
					else if(data.cc_division == '55' && grdLst.getList('selected')[0].cc_testgbn != 'B') return true;
					else return false;
				}else return false;
			}else return false;
		});
	}
	
	grdFile.setData(grdFileSimpleData);
	grdFile.setConfig();
	grdFile.repaint();
}

function grdCondFilter(oriData) {
	grdCondSimpleData = [];
	if(oriData.length > 0) {
		grdCondSimpleData = oriData.filter(function(data) {
			if(getSelectedIndex('cboEditor') < 1) return false;
			if(!$('#chkOpen').is(':checked') && grdLst.getList('selected').length < 1) return false;
			if(data.cc_gbncd != 'C') return false;
			if($('#chkOpen').is(':checked') && data.cc_caseseq != '999') return true;
			else if($('#chkOpen').is(':checked')) return false;
			if(grdLst.getList('selected')[0] != undefined && data.cc_caseseq != grdLst.getList('selected')[0].cc_caseseq) return false;
			if(reqCd == '54') return true;
			
			if(data.cc_testuser == grdLst.getList('selected')[0].cc_testuser) {
				if(data.cc_testgbn == grdLst.getList('selected')[0].cc_testgbn) return true;
				else return false;
			}else {
				return false;
			}
		});
	}
	
	grdCond.setData(grdCondSimpleData);
	grdCond.setConfig();
	grdCond.repaint();
}

function grdChkFilter(oriData) {
	console.log('oriData=>',oriData);
	grdChkSimpleData = [];
	if(oriData.length > 0) {
		grdChkSimpleData = oriData.filter(function(data) {
			if(getSelectedIndex('cboEditor') < 1) return false;
			if(!$('#chkOpen').is(':checked') && grdLst.getList('selected').length < 1) return false;
			if(data.cc_gbncd != 'R') return false;
			if($('#chkOpen').is(':checked') && data.cc_caseseq != '999') return true;
			else if($('#chkOpen').is(':checked')) return false;
			if(data.cc_caseseq != grdLst.getList('selected')[0].cc_caseseq) return false;
			if(reqCd == '44') return true;
			
			if(data.cc_testuser == grdLst.getList('selected')[0].cc_testuser) {
				if(data.cc_testgbn == grdLst.getList('selected')[0].cc_testgbn) return true;
				else return false;
			}else {
				return false;
			}
		});
	}
	
	grdChk.setConfig();
	grdChk.setData(grdChkSimpleData);
}

function cboEditor_Change() {
	var findSw = false;
	
	grdLstFilter(grdLstData);
	grdCondFilter(grdCondData);
	grdChkFilter(grdChkData);
	
	if(reqCd != '44') grdFileFilter(grdFileData);
	
	screenInit('S');
	
	if(getSelectedIndex('cboEditor') < 1) return;
	if(reqCd == 'XX') {
		for(var i=0; i<grdLstSimpleData.length; i++) {
			if(grdLstSimpleData[i].cc_scmuser == getSelectedVal('cboEditor').cc_scmuser) {
				if(grdLstSimpleData[i].cc_testday != null && grdLstSimpleData[i].cc_testday != undefined && grdLstSimpleData[i].cc_testday != '') {
					findSw = true;
					break;
				}
			}
		}
		
		if(findSw) {
			grdChk.config.columns = grdChkCol1;
			grdChk.setConfig();
		}else {
			grdChk.config.columns = grdChkCol2;
			grdChk.setConfig();
		}
	}else if(reqCd != '44') {
		findSw = true;
	}
	
	if(findSw) {
	}else {
		if(grdList.getList().length > 0) {
			grdLstFilter(grdListData);
			grdLst.select(grdLstSimpleData.length-1);
			grdLst.focus(grdLstSimpleData.length-1);
			grdLst_click(null);
		}
	}
}

function grdLst_click(selItem) {
	if(selItem == null) {
		selItem = grdLst.getList('selected')[0];
	}
	
	if(reqCd != '44') {
		grdChk.config.columns = grdChkCol1;
		grdChk.setConfig();
		grdFileFilter(grdFileData);
	}
	
	$('#chkOpen').wCheck('check', false);
	grdCondFilter(grdCondData);
	grdChkFilter(grdChkData);
	
	if(grdLst.getList('selected')[0] == undefined) return;
	if(selItem == null || selItem == undefined) return;
	
	screenInit('S');
	
	$('#txtTestId').val(selItem.cc_caseseq);
	$('#txtCase').val(selItem.cc_casename);
	$('#txtTester').val(selItem.testuser);
	$('#datTestDay').val('');
	if(selItem.cc_testday != null && selItem.cc_testday != undefined && selItem.cc_testday != '') {
		$('#datTestDay').val(selItem.testday);
		$('#txtExpTime').val(selItem.cc_worktime);
	}
	picker.bind(defaultPickerInfo('testdate', 'top'));
	$('#txtResult').val(selItem.cc_testrst)
	
	// 2:반려, 3:삭제
	if(selItem.cc_status == '2' || selItem.cc_status == '3') {
		$('#btnAdd').prop('disabled', true);
		return;
	}
	
	if(reqCd == '54' && !$('#btnAdd').is(':disabled')) {
		$('#btnTestLog').prop('disabled', false);
		$('#btnDel').prop('disabled', false);
	}
	
	if(reqCd == '54') {
		$('#btnReject').prop('disabled', booReject);
	}
}

// 첨부파일 삭제
function deleteDoc(data) {
	var data = {
		etcObj : data,
		requestType : 'deleteDoc'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successDeleteDoc);
}

function successDeleteDoc(data) {
	if(data == 'OK') {
		grdFile.repaint();
	}
}

// 테스트케이스 신규
function chkOpen_click() {
	//신규거나, 수정이면서 그리드선택안되어 있으면
	if($('#chkOpen').is(':checked') || (!$('#chkOpen').is(':checked') && grdLst.getList('selected').length < 1)) {
		for(var i=0; i<grdCondData.length; i++) {
			if(grdCondData[i].cc_gbncd == 'C' && grdCondData[i].cc_caseseq == '999') {
				grdCondData.splice(i,1);
				i--;
			}
		}
		grdCondFilter(grdCondData);
		grdCondData = [];
		grdCondFilter(grdCondData);
		
		for(var i=0; i<grdChkData.length; i++) {
			if(grdChkData[i].cc_gbncd == 'R' && grdChkData[i].cc_caseseq == '999') {
				grdChkData.splice(i,1);
				i--;
			}
		}
		grdChkFilter(grdChkData);
		grdChkData = [];
		grdChkFilter(grdChkData);
		
		if(reqCd != '44') {
			$('#txtTester').val('');
			
			grdChk.config.columns = grdChkCol1;
			grdChk.setConfig();
			
//			grdFileData = [];
			grdFileSimpleData = [];
			grdFileFilter([]);
		}else {
			$('#txtTester').prop('readonly', false);
			$('#txtTester').prop('disalbed', false);
		}
		
		screenInit('S');
	}else {
		grdLst_click(null);
	}
}

// 등록/수정 버튼
function btnAdd_Click() {
	if(strIsrId == null || strIsrId == '' || strIsrId == undefined) {
		dialog.alert('SR이 선택되지 않았습니다.');
		return;
	}
	
	if(getSelectedIndex('cboEditor') < 1) {
		dialog.alert('개발자를 선택하여 주시기 바랍니다.');
		return;
	}
	
	if($('#txtCase').val().trim().length == 0) {
		dialog.alert('테스트케이스를 입력하여 주시기 바랍니다.');
		return;
	}
	
	if(grdCondSimpleData.length == 0) {
		dialog.alert('테스트조건을 등록 해 주시기 바랍니다.');
		return;
	}
	
	if(!$('#chkOpen').is(':checked') && grdChkSimpleData.length == 0) {
		dialog.alert('확인사항을 등록하여 주시기 바랍니다.');
		return;
	}
	
	if(userId == null || userId == undefined || userId == '') {
		dialog.alert('테스트 등록자 정보가 없습니다.\n다시 로그인 후 등록해 주시기 바랍니다.');
		return;
	}
	
	var i = 0;
	var caseSeq = '';
	var findSw = false;
	var selItem = grdLst.getList('selected')[0];
	
	if($('#chkOpen').is(':checked')) caseSeq = '999';
	else caseSeq = selItem.cc_caseseq;
	
	findSw = false;
	
	for(i=0; i<grdCondSimpleData.length; i++) {
		if(grdCondSimpleData[i].cc_caseseq == caseSeq) {
			if(grdCondSimpleData[i].cc_itemmsg.trim().length == 0
					|| grdCondSimpleData[i].cc_itemmsg == null || grdCondSimpleData[i].cc_itemmsg == '') {
				dialog.alert('테스트조건을 입력하여 주시기 바랍니다.');
				grdCond.select(i);
				return;
			}
			
			findSw = true;
		}
	}
	
	if(!findSw) {
		dialog.alert('테스트조건을 입력하여 주시기 바랍니다.');
		return;
	}
	
	findSw = false;
	
	for(i=0; i<grdChkSimpleData.length; i++) {
		if(grdChkSimpleData[i].cc_caseseq == caseSeq) {
			if(grdChkSimpleData[i].cc_itemmsg.trim().length == 0
					|| grdChkSimpleData[i].cc_itemmsg == null || grdChkSimpleData[i].cc_itemmsg == '') {
				dialog.alert('확인사항을 입력하여 주시기 바랍니다.');
				grdChk.select(i);
				return;
			}
			
			if(!$('#chkOpen').is(':checked') && reqCd != '44') {
				if(grdChkSimpleData[i].selected1 == '0' && grdChkSimpleData[i].selected2 == '0') {
					dialog.alert('확인결과를 선택하여 주시기 바랍니다.');
					grdChk.select(i);
					return;
				}
			}
			
			findSw = true;
		}
	}
	
	if(!findSw) {
		dialog.alert('확인사항을 입력하여 주시기 바랍니다.');
		return;
	}
	
	if(reqCd == '54' || reqCd == '55') {
		if($('#txtResult').val().trim().length == 0) {
			dialog.alert('최종결과의견을 입력하여 주시기 바랍니다.');
			return;
		}
		
		if($('#txtExpTime').val().trim().length == 0) {
			dialog.alert('투입시간을 입력하여 주시기 바랍니다.');
			return;
		}
	}
	
	var tmpObj = new Object();
	tmpObj.cc_srid = strIsrId;
	if($('#chkOpen').is(':checked')) {
		tmpObj.cc_caseseq = '999';
	}else {
		tmpObj.cc_caseseq = $('#txtTestId').val();
	}
	tmpObj.cc_testuser = userId;
	tmpObj.cc_scmuser = getSelectedVal('cboEditor').cc_scmuser;
	tmpObj.cc_username = userName;
	tmpObj.cc_deptname = userDeptName;
	userInfoData.push(tmpObj);
	tmpObj = null;
	
	confirmDialog.confirm({
		title: '확인',
		msg: '테스트케이스를 등록/수정을 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var etcObj = new Object();
			etcObj.cc_srid = strIsrId;
			etcObj.reqcd = reqCd;
			if($('#chkOpen').is(':checked')) {
				etcObj.cc_caseseq = '999';
			}else {
				etcObj.cc_caseseq = $('#txtTestId').val();
			}
			etcObj.cc_casename = $('#txtCase').val();
			etcObj.cc_userid = userId;
			etcObj.cc_testgbn = 'B'; //B:사전
			etcObj.cc_scmuser = getSelectedVal('cboEditor').cc_scmuser;
			if(reqCd == '44') {
				etcObj.edityn = 'Y';
			}else if(reqCd == '54' || reqCd == '55') {
				etcObj.cc_testday = replaceAllString($("#datTestDay").val(), '/', '');
				etcObj.cc_testrst = $('#txtResult').val().trim();
				etcObj.cc_tester = userId;
				if($('#chkOpen').is(':checked')) etcObj.edityn = 'Y';
				else if (grdLst.getList('selected')[0].cc_userid == userId) etcObj.edityn = 'Y';
				else etcObj.edityn = 'N';
				etcObj.cc_exptime = $('#txtExpTime').val().trim();
				etcObj.itemid = '';
			}
			
			var data = {
				caseInfo : etcObj,
				condList : grdCondSimpleData,
				checkList : grdChkSimpleData,
				testerList : userInfoData,
				requestType : 'set_testCase'
			}
			
			console.log('[DevCheckTab.js] set_testCase ==>', data);
			ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successSetTestCase);
		}
	});
}

function successSetTestCase(data) {
	if(data.substr(0,2) != 'OK') {
		dialog.alert(data.substr(2));
	}else {
		if(reqCd != '44') {
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
		}else {
			caseFileEnd(true);
		}
	}
}

function caseFileEnd(ret) {
	if(ret) dialog.alert('개발검수 등록처리가 정상적으로 처리되었습니다.');
	
	screenInit('S');
	window.parent.cmdQry_click();
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

// 삭제 버튼
function btnDel_click() {
	if(grdLst.getList('selected').length < 1) {
		dialog.alert('삭제 할 테스트케이스를 선택 해 주시기 바랍니다.');
		return;
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: '테스트케이스를 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var etcObj = new Object();
			etcObj.cc_srid = strIsrId;
			etcObj.cc_caseseq = $('#txtTestId').val();
			etcObj.cc_testgbn = 'B'; //B:사전
			etcObj.cc_testuser = grdLst.getList('selected')[0].cc_testuser;
			
			var data = {
				caseInfo : etcObj,
				requestType : 'del_testCase'
			}
			
			console.log('[DevCheckTab.js] del_testCase ==>', data);
			ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successDelCaseInfo);
		}
	});
}

function successDelCaseInfo(data) {
	if(data.substring(0,2) == 'OK') {
		dialog.alert('삭제가 정상 처리 되었습니다.');
	}
	
	screenInit('S');
	window.parent.cmdQry_click();
}

// 개발검수완료
function btnTestEndding_click() {
	if(reqCd == '54') {
		//SR에 등록된 테스트케이스가 있는지 체크
		if(grdLstSimpleData.length < 1) {
			dalog.alert('등록 된 테스트케이스가 없습니다.\n테스트케이스 등록 후 [검수요청] 하시기 바랍니다.');
			return;
		}
		
		//테스트케이스 중 테스트결과가 Fail 난게 있는지 체크
		var i=0;
		for(i=0; i<grdLstSimpleData.length; i++) {
			if (grdLstSimpleData[i].caseend == 'NO' && grdLstSimpleData[i].cc_status == '0'){
				break;
			}
		}
		
		if (i<grdLstSimpleData.length) {
			dalog.alert('테스트케이스['+grdLstSimpleData[i].cc_casename+'] *확인사항 결과가 [비정상] 입니다.\n테스트결과를 확인해 주시기 바랍니다.');
			return;
		}
		
		if(getSelectedIndex('cboEditor') > 0 && getSelectedVal('cboEditor').pgmyn == 'OK') {
			var data = {
				srid : strIsrId,
				userid : getSelectedVal('cboEditor').cc_scmuser,
				status : 'B',
				requestType : 'setStaCmc0110'
			}
			
			console.log('[DevCheckTab.js] setStaCmc0110 ==>', data);
			ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successSetStaCmc0110);
		}
	}
}

function successSetStaCmc0110(data) {
	if(data > 0) {
		dialog.alert('개발검수를 완료했습니다.');
		
		screenInit('S');
		window.parent.cmdQry_click();
	}else {
		dialog.alert('개발검수 완료를 실패했습니다.');
	}
}

// 검수반려
function btnReject_click() {
	//검수반려는 확인사항이 1건이라도 있어야 하며, 최종결과의견에 반려의견이 있어야 함.
	if(getSelectedIndex('cboEditor') < 1) {
		dialog.alert('반려 할 개발자를 선택 해 주시기 바랍니다.');
		return;
	}
	
	if($('#txtCase').val().trim().length == 0) {
		dialog.alert('테스트케이스를 입력하여 주시기 바랍니다.');
		return;
	}
	
	if(grdCondSimpleData.length == 0) {
		dialog.alert('테스트조건을 등록 해 주시기 바랍니다.');
		return;
	}
	
	if(!$('#chkOpen').is(':checked') && grdChkSimpleData.length == 0) {
		dialog.alert('확인사항을 등록하여 주시기 바랍니다.');
		return;
	}
	
	if(userId == null || userId == undefined || userId == '') {
		dialog.alert('테스트 등록자 정보가 없습니다.\n다시 로그인 후 등록해 주시기 바랍니다.');
		return;
	}
	
	var i = 0;
	var caseSeq = '';
	var findSw = false;
	var selItem = grdLst.getList('selected')[0];
	
	if($('#chkOpen').is(':checked')) caseSeq = '999';
	else caseSeq = selItem.cc_caseseq;
	
	findSw = false;
	
	for(i=0; i<grdCondSimpleData.length; i++) {
		if(grdCondSimpleData[i].cc_caseseq == caseSeq) {
			if(grdCondSimpleData[i].cc_itemmsg.trim().length == 0
					|| grdCondSimpleData[i].cc_itemmsg == null || grdCondSimpleData[i].cc_itemmsg == '') {
				dialog.alert('테스트조건을 입력하여 주시기 바랍니다.');
				grdCond.select(i);
				return;
			}
			
			findSw = true;
		}
	}
	
	if(!findSw) {
		dialog.alert('테스트조건을 입력하여 주시기 바랍니다.');
		return;
	}
	
	findSw = false;
	
	for(i=0; i<grdChkSimpleData.length; i++) {
		if(grdChkSimpleData[i].cc_caseseq == caseSeq) {
			if(grdChkSimpleData[i].cc_itemmsg.trim().length == 0
					|| grdChkSimpleData[i].cc_itemmsg == null || grdChkSimpleData[i].cc_itemmsg == '') {
				dialog.alert('확인사항을 입력하여 주시기 바랍니다.');
				grdChk.select(i);
				return;
			}
			
			if(!$('#chkOpen').is(':checked') && reqCd != '44') {
				if(grdChkSimpleData[i].selected1 == '0' && grdChkSimpleData[i].selected2 == '0') {
					dialog.alert('확인결과를 선택하여 주시기 바랍니다.');
					grdChk.select(i);
					return;
				}
			}
			
			findSw = true;
		}
	}
	
	if(!findSw) {
		dialog.alert('확인사항을 입력하여 주시기 바랍니다.');
		return;
	}
	
	if(reqCd == '54' || reqCd == '55') {
		if($('#txtResult').val().trim().length == 0) {
			dialog.alert('최종결과의견을 입력하여 주시기 바랍니다.');
			return;
		}
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: '개발자[' + getSelectedVal('cboEditor').scmuser + '] 개발 반려 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			//개발검수 케이스 삭제 및 개발자상태(CMC0110)값 변경
			var etcObj = new Object();
			etcObj.cc_srid = strIsrId;
			etcObj.cc_editor = userId;									//검수자
			etcObj.cc_scmuser = getSelectedVal('cboEditor').cc_scmuser;	//개발자
			
			var data = {
				caseInfo : etcObj,
				requestType : 'rejectDevCase'
			}
			
			console.log('[DevCheckTab.js] rejectDevCase ==>', data);
			ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json', successRejectDevCase);
		}
	});
}

function successRejectDevCase(data) {
	if(data == 'OK') {
		dialog.alert('반려가 정상 처리 되었습니다.');
	}else {
		dialog.alert('반려가 실패 처리 되었습니다.\n확인 후 다시 처리해 주시기 바랍니다.');
	}
	window.parent.getQry_click();
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
		
		grdFileFilter(grdFileData);
//		grdFile.setData(grdFileData);
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
	f.user.value 	= getSelectedVal('cboEditor').cc_scmuser;
	f.scmuser.value = getSelectedVal('cboEditor').cc_scmuser;
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
	grdLstData = [];
	grdLstSimpleData = [];
	grdLst.setData([]);
	
	grdFileData = [];
	grdFileSimpleData = [];
	grdFile.setData([]);
	
	grdCondData = [];
	grdCondSimpleData = [];
	grdCond.setData([]);
	
	grdChkData = [];
	grdChkSimpleData = [];
	grdChk.setData([]);
	
	if(!$('#btnTestEndding').is(':disabled')) {
		$('#btnTestEndding').prop('disabled', true);
		sysTestCall();
	}
	
	getTestCaseList_call(strIsrId, reqCd);
}