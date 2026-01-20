/**
 * [테스트케이스 복사] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2022-00-00
 * 
 */

var grdCase	= new ax5.ui.grid();

var grdCaseData 	= [];
var cboEditorData 	= [];
var cboCaseData 	= [
	{cm_codename : "단위테스트",	cm_micode : "43"},
	{cm_codename : "개발검수",		cm_micode : "54"}
];
var cboQryGbnData	= [
	{cm_codename : "전체",		cm_micode : "00", cm_dateyn: "Y"},
	{cm_codename : "미완료",		cm_micode : "01", cm_dateyn: "N"}
]

var tab0			= null;
var load0Sw			= false;
var strUserId		= '';
var strScmUser 		= '';
var strBaseSR 		= '';
var strReqCd 		= '';
var strIsrId		= '';
var strTester		= '';
var reqCd			= ''; // to PrjListTab

$('[data-ax5select="cboEditor"]').ax5select({ options : [] });
$('[data-ax5select="cboCase"]').ax5select({ options : [] });

var f = document.getReqData;
strUserId 	= f.userId.value;
strScmUser	= f.scmUser.value;
strBaseSR 	= f.isrId.value;
strReqCd 	= f.reqCd.value;

grdCase.setConfig({
    target: $('[data-ax5grid="grdCase"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center"
    },
    paging : false,
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {},
    	onDataChanged: function(){}
    },
    columns: [
        {key: "cc_casename",	label: "테스트케이스명",	width: '50%'},
        {key: "cc_exprst", 		label: "예상결과",  		width: '25%'},
        {key: "cc_etc", 		label: "입력내용",  		width: '25%'}
    ]
});

$(document).ready(function() {
	// 복사 버튼
	$('#btnCopy').bind('click', function() {
		btnCopy_Click();
	});
	
	// 닫기 버튼
	$('#btnClose').bind('click', function() {
		btnClose_click();
	});
	
	// 테스트케이스구분 변경
	$('#cboCase').bind('change', function() {
		cboCase_click();
	});
	
	// 개발자 변경
	$('#cboEditor').bind('change', function() {
		cboEditor_click();
	});
	
	if(strReqCd == '54' || strReqCd == '55') {
		strTester = strUserId;
	}
	
	if(strReqCd == '43') reqCd = 'CP43';
 	else if(strReqCd == '54') reqCd = 'CP54';
 	else reqCd = 'CP44';

//	_promise(10,loadiFrame())
//	.then(function() {
//		return _promise(10,loadiFrameEnd());
//	});
	loadiFrame();
	checkFrameLoad(['frmPrjList'], loadiFrameEnd);
	
	$('#txtIsrId').val(strBaseSR);
});

function loadiFrame() {
	console.log('1: ' + load0Sw);
	document.getElementById('frmPrjList').onload = function() {
		load0Sw = true;
		tab0 = $('#frmPrjList').get(0).contentWindow;
	}
}

function loadiFrameEnd() {
	console.log('2: ' + load0Sw);
	tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.reqCd = reqCd;
		tab0.screenInit();
//		tab0.getPrjList();
		tab0.cboQryGbn_click();
	}
	setCbo();
}

function setCbo() {
	$('[data-ax5select="cboCase"]').ax5select({
        options: injectCboDataToArr(cboCaseData, "cm_micode", "cm_codename")
	});
	
	for(var i=0; i<cboCaseData.length; i++) {
		if(cboCaseData[i].cm_micode == strReqCd) {
			$('[data-ax5select="cboCase"]').ax5select('setValue', cboCaseData[i].cm_micode, true);
			break;
		}else if( (strReqCd == '54' || strReqCd == '55') && cboCaseData[i].cm_micode == '54') {
			$('[data-ax5select="cboCase"]').ax5select('setValue', cboCaseData[i].cm_micode, true);
			break;
		}
	}
	
//	tab0 = $('#frmPrjList').get(0).contentWindow;
//	if(tab0 != null && tab0 != undefined) {
//		tab0.getPrjList();
//	}
//	cboCase_click();
}

function cboCase_click() {
	grdCaseData = [];
	grdCase.setData([]);
	
	if(getSelectedIndex('cboCase') < 0) return;
	if(getSelectedVal('cboCase').cm_micode == '43') {
		grdCase.updateColumn({key: "cc_exprst", 	label: "예상결과",  		width: '25%'}, 1);
		grdCase.updateColumn({key: "cc_etc", 		label: "입력내용",  		width: '25%'}, 2);
	}else {
		grdCase.updateColumn({key: "cc_exprst", 	label: "예상결과",  		width: '25%', hidden: 'true'}, 1);
		grdCase.updateColumn({key: "cc_etc", 		label: "입력내용",  		width: '25%', hidden: 'true'}, 2);
	}
	
	setTimeout(function() {
		tab0 = $('#frmPrjList').get(0).contentWindow;
		if(tab0.firstGrid.selectedDataIndexs.length == 0) return;
		if(getSelectedIndex('cboEditor') < 1) return;
	}, 10);
	
	if(getSelectedVal('cboCase').cm_micode == '54') {
		if (strReqCd == '54') {
			getTestCase(getSelectedVal('cboCase').cm_micode);
		}else {
			getTestCase(strReqCd);
		}
	}else {
		getTestCase(strReqCd);
	}
}

function getTestCase(reqcd) {
	tab0 = $('#frmPrjList').get(0).contentWindow;
	
	var data = {
		IsrId : tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_srid,
		scmUser : getSelectedVal('cboEditor').cc_scmuser,
		ReqCd : reqcd,
		requestType	: 'getTestCase'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json',successGetTestCase);
}

function successGetTestCase(data) {
	grdCaseData = data;
	grdCase.setData(grdCaseData);
}

// PrjListTab에서 호출
function isrID_click(data) {
	subScreenInit();
	strIsrId = data.cc_srid;
	
	if(strReqCd == '54') getScmuserList('44');
	else getScmuserList(strReqCd);
}

function subScreenInit() {
	grdCaseData = [];
	grdCase.setData([]);
	cboEditorData = [];
	$('[data-ax5select="cboEditor"]').ax5select({ options : [] });
}

function getScmuserList(reqcd) {
	var data = {
		cc_srid : strIsrId,
		reqCD : reqcd,
		userID : strUserId,
		requestType	: 'getScmuserList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json',successGetScmuserList);
}

function successGetScmuserList(data) {
	cboEditorData = data;
	$('[data-ax5select="cboEditor"]').ax5select({
        options: injectCboDataToArr(cboEditorData, "cc_scmuser", "scmuser")
	});
	
	if(cboEditorData.length > 0) {
		$('[data-ax5select="cboEditor"]').ax5select('setValue', cboEditorData[1].cc_scmuser, true);
		cboEditor_click();
	}
}

function cboEditor_click() {
	grdCaseData = [];
	grdCase.setData([]);
	
	if(getSelectedIndex('cboEditor') < 1) return;
	
	if(cboEditorData.length > 0) {
		cboCase_click();
	}
}

function btnCopy_Click() {
	var tmpArray = grdCase.getList('selected');
	if(tmpArray.length == 0) {
		dialog.alert('복사 할 테스트케이스를 선택한 후 처리하시기 바랍니다.');
		return;
	}
	
	tab0 = $('#frmPrjList').get(0).contentWindow;
	
	var tmpObj = new Object();
	tmpObj.UserId = strUserId;
	tmpObj.tosrid = strBaseSR;
	tmpObj.toscmuser = strUserId;
	tmpObj.fromsrid = tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_srid;
	tmpObj.fromscmuser = getSelectedVal('cboEditor').cc_scmuser;
	tmpObj.reqcd = getSelectedVal('cboCase').cm_micode;
	tmpObj.tester = strTester;
	if(strReqCd == '54' || strReqCd == '55') {
		if (getSelectedVal('cboCase').cm_micode == '54') {
			tmpObj.UserId = strUserId;
			tmpObj.toscmuser = strScmUser;
			tmpObj.tester = strTester;
			tmpObj.reqcd = strReqCd;
		}	
	}
	
	var data = {
		etcData : tmpObj,
		CopyList : tmpArray,
		requestType	: 'setCaseCopy'
	}
	
	console.log('[PopCopyTestCase.js] setCaseCopy ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json',successSetCaseCopy);
}

function successSetCaseCopy(data) {
	if(data != '0') {
		dialog.alert(data);
	}else {
		dialog.alert('테스트케이스 복사처리를 완료하였습니다.', function() {
			try {
				if(window.opener.PopCopyTestCaseCallBack != undefined) {
					window.opener.PopCopyTestCaseCallBack();
				}
				window.open("about:blank","_self").close();
			}catch(e) {
			}
		});
	}
}

function btnClose_click() {
	window.open("about:blank","_self").close();
}