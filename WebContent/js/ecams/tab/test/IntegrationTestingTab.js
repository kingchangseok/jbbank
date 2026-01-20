/** 
 * [테스트관리] 통합테스트 / [tab]통합테스트
 */

//var userName = window.top.userName;
//var userId = window.top.userId;
//var adminYN = window.top.adminYN;
//var userDeptName = window.top.userDeptName;
//var userDeptCd = window.top.userDeptCd;
//var strReqCd = window.top.reqCd;
//var codeList    = window.top.codeList;          //전체 코드 리스트

var grdLst = new ax5.ui.grid();
var grdCond = new ax5.ui.grid();
var picker = new ax5.ui.picker();
var grdChk = new ax5.ui.grid();
var grdFile = new ax5.ui.grid();
var confirmDialog 	= new ax5.ui.dialog();

var fileUpPop = null;
var casesub1_dp = [];
var casesub2_dp = [];
var grdLst_dp = [];
var grdCond_dp = [];
var grdLst_simple_dp = [];
var grdChk_dp = [];
var cboTest_dp = [];
var grdFile_dp = [];
var folderDefaultClosed = "";
var exlLoad = null;
var fileUp = null;
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var strSubStatus = "";
var strSubReq = "";
var strTstSt = "";
var strTester = "";
var Gubun = "N";
var tmpPath = "";
var attPath = "";
var strUpURL = "";
var excelFile = new Object();
var grdCondRowIndex = -1;
var grdChkRowIndex = -1;
var testSw = false;
var scmSw = false;
var tabStatus = "test";

ax5.info.weekNames = [ {
	label : "일"
}, {
	label : "월"
}, {
	label : "화"
}, {
	label : "수"
}, {
	label : "목"
}, {
	label : "금"
}, {
	label : "토"
} ];

$('[data-ax5select="cboTestCnt"]').ax5select({
	options : []
});

$('#datTestDay').val(getDate('DATE', 0));

picker.bind(defaultPickerInfo('testdate', 'top'));

$(document).ready(function() {
	// 통합테스트종료
	$('#btnEnd').bind('click', function() {
		btnEnd_click();
	});
	// 삭제 버튼
	$('#btnDel').bind('click', function() {
		btnDel_click();
	});
	// 등록 버튼
	$('#btnAdd').bind('click', function() {
		btnAdd_click();
	});
	// 추가 버튼
	$('#btngrdCondAdd').bind('click', function() {
		btnGrdAdd();
	});
	// 제거 버튼
	$('#btngrdCondDel').bind('click', function() {
		btnGrdDel();
	});		
	// 테스트Log 버튼
	$('#btnTestLog').bind('click', function() {
		fileOpen();
	});
	// 테스트케이스 복사 버튼
	$('#btnCaseCp').bind('click', function() {
		CaseCopyInfo();
	});
	// 엑셀저장 버튼
	$('#btnExlSave').bind('click', function() {
		grdLst.exportExcel("dani"+strUserId+".xls");
	});
	// 엑셀로드 버튼
	$('#btnExlLoad').bind('click', function() {
		excelFileUpLoad();
	});
	// 엑셀템플릿 버튼
	$('#btnExlTmp').bind('click', function() {
		excelTempDown();
	});				
	//테스트케이스 신규
	$('#chkOpen').bind('change', function() {
		chkOpen_click();
	})
	//해당사항없음
	$('#chkAllPass').bind('change', function() {
		AllPass();
	})
	//테스트횟수
	$('#cboTestCnt').bind('change', function() {
		cboTestCnt_click();
	});
	//하단탭 클릭
	$("ul.tabs li").bind('click', function() {
		//이미 on상태면 pass
		if($(this).hasClass("on")) {
			changeTabMenu();
			return;
		}
	
		$(".tab_content").hide();
		var activeTab = $(this).attr("rel");
		
		//tab메뉴 클릭에 따라 색상 변경
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		
		// $("#" + activeTab).show();
		$("#" + activeTab).css('display', '');
		changeTabMenu();
	})
	
	geteCAMSDir();
});

grdLst.setConfig({
    target: $('[data-ax5grid="grdLst"]'),
	sortable : true,
	multiSort : true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
			grdLst_click(this.item, this.dindex, false);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        }
    },
    columns: [
    	{key: "cc_caseseq", label: "테스트 ID",  width: 140, align: 'left'},
    	{key: "cc_casename", label: "테스트케이스",  width: 550,align: 'left'},
        {key: "scmuser",  	label: "변경담당자",  width: 140, align: 'center'},
        {key: "lastdt", 	label: "최종변경일시",  width: 180},
        {key: "tester", 	 label: "수행인",  width: 140},
        {key: "testday", 	label: "수행일",  width: 140},
        {key: "cc_testrst", label: "테스트결과",  width: 200}
    ]
});

grdCond.setConfig({
    target: $('[data-ax5grid="grdCond"]'),
	sortable : true,
	multiSort : true,
    header: {
        align: "center",
    },
    body: {
		columnHeight : 35,
        onClick: function () {
			this.self.clearSelect();
        	this.self.select(this.dindex);
			grdCond_click();
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        }
    },
    columns: [
    	{key: "ITEMMSG", label: "테스트조건",  width: '100%', align: 'left', editor:{type:"text"}},
    ]
});

grdChk.setConfig({
    target: $('[data-ax5grid="grdChk"]'),
	sortable : true,
	multiSort : true,
    header: {
        align: "center",
    },
    body: {
		columnHeight : 35,
        onClick: function () {
			this.self.clearSelect();
        	this.self.select(this.dindex);
			grdChk_click();
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        }
    },
    columns: [
    	{key: "ITEMMSG", label: "확인사항",  width: '100%', align: 'left', editor:{type:"text"}}
    ]
});

grdFile.setConfig({
	target : $('[data-ax5grid="grdFile"]'),
	sortable : true,
	multiSort : true,
	header : {
		align : "center",
	},
	body : {
		columnHeight : 28,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDBLClick : function() {
			fileDbClick(this.dindex, this.item);
		}
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
			grdFile.clearSelect();
			 grdFile.select(Number(param.dindex));
			if(grdFile.getList('selected').length < 1 || 
					strReqCd == "XX" ||
					SRStatus == "1" || SRStatus == "3" || 
					SRStatus == "5" || SRStatus == "A"){
				grdFile.contextMenu.close();//또는 return true;
				return false;
			}
			 return true;
			
		},
		onClick: function (item, param) {
			deleteFile(param);
			grdFile.contextMenu.close();//또는 return true;
		}
	},
	columns : [ {
		key : "cc_attfile",
		label : "파일명",
		width : 160
	},{
		key : "cm_username",
		label : "첨부인",
		width : 140
	},{
		key : "lastdt",
		label : "첨부일",
		width : 140
	} ],
	page : {
		display : false
	}
});

function geteCAMSDir() {
	var data = {
		pcode : "21,99,F2",
		requestType : 'eCAMSDir'
	}
	ajaxAsync('/webPage/common/CommonSystemPath', data, 'json',
			successGeteCAMSDir);
}

function successGeteCAMSDir(data) {
	var tmpArray = new Array();

	tmpArray = data;
	if (tmpArray.length > 0) {
		for (var i = 0; tmpArray[0].length > i; i++) {
			if (tmpArray[i].cm_pathcd == "F2") {
				strUpURL = tmpArray[i].cm_path;
			} else if (tmpArray[i].cm_pathcd == "21") {
				attPath = tmpArray[i].cm_path;
			}
		}
	} else {
		dialog.alert("파일을 저장할 디렉토리정보를 추출 중 오류가 발생하였습니다. 관리자에게 연락하여 주십시오.");
		return;
	}
}

function screenInit(scrGbn) {
			
	if (scrGbn == "M") {
		cboTest_dp = [];
	}
	strSubReq = "";
	$('#btnAdd').attr('disabled', true);
	$('#btnAdd').attr('disabled', true);
	$('#btnDel').attr('disabled', true);
	$('#btnCaseCp').attr('disabled', true);
	$('#btnExlLoad').attr('disabled', true);
	$('#btnExlSave').attr('disabled', true);
	$('#btngrdChkAdd').attr('disabled', true);
	$('#btngrdChkDel').attr('disabled', true);
	$('#btngrdCondAdd').attr('disabled', true);
	$('#btngrdCondDel').attr('disabled', true);
	$('#btnTestLog').attr('disabled', true);
	grdLst_dp = [];
	casesub1_dp = [];
	casesub2_dp = [];
	grdCond_dp = [];
	grdChk_dp = [];
	grdLst.setData([]);
	grdCond.setData([]);
	grdChk.setData([]);
	$('#chkOpen').attr('disabled', true);
	$('#chkAllPass').attr('disabled', true);
	$('#datTestDay').css('visibility', 'hidden');
	$('#calDatTestDay').css('visibility', 'hidden');
	$('#calDatTestDay').css('pointer-event', 'none');
	$('#txtTestDay').css('visibility', 'visible');
	$('#btnEnd').css('visibility', 'hidden');
	$('#txtResult').attr('readonly', true);
	grdFile_dp = [];
	$('#txtCase').val("");
	$('#txtTestDay').val("");
	$('#txtTester').val("");
	$('#txtResult').val("");
	$('#txtTestId').val("");
	$('#chkOpen').prop('checked', false);
	$('#chkAllPass').prop('checked', false);

	if(document.getElementById("tab2").className == "") {
		// $('#tabCheck').hide();
		$("#tabCheck").css("display", "none");
	}
}

function testcntCall() {
	getTestCnt(strIsrId, strIsrSub, strUserId, strSubStatus, strReqCd);
}

function testcntCall2() {
	getTestCnt(strIsrId, strIsrSub, "", strSubStatus, strReqCd);
}

function getTestCnt(IsrId, IsrSub, UserId, subStatus, Reqcd) {
	var tcObj = new Object();
	tcObj.IsrId = IsrId;
	tcObj.IsrSub = IsrSub;
	tcObj.UserId = UserId;
	tcObj.subStatus = subStatus;
	tcObj.ReqCd = Reqcd;

	var tmpInfo = new Object();
	tmpInfo.tcObjData = tcObj;
	tmpInfo.requestType = "getTestCnt"

	ajaxAsync('/webPage/rfc/RFCRegister', tmpInfo, 'json', successGetTestCnt);

	tcObj = null;	
}

function successGetTestCnt(data) {
	cboTest_dp =data

	options = [];
	$.each(cboTest_dp, function(i, value) {
		options.push({
			value : value.cc_testseq,
			text : value.testseq,
			editor : value.cc_editor,
			secusw : value.secusw,
			data : value.data,
			endyn : value.endyn,
			cc_testseq : value.cc_testseq
		});
	});

	$('[data-ax5select="cboTestCnt"]').ax5select({
		options : options
	});
	
		
	if (cboTest_dp.length > 0 ) {
		var index = cboTest_dp.length;
		$('[data-ax5select="cboTestCnt"]').ax5select("setValue", index, true);
		cboTestCnt_click();
	}
}

function cboTestCnt_click() {
	var scmFg = false;
			
	screenInit('S');
	testSw = false;
	strSubReq = getSelectedVal('cboTestCnt').cc_testseq;
	if (strSubReq.length < 2) { 
		strSubReq = "0" + strSubReq;
	}
	
	if (strReqCd == "52") {
		if (getSelectedVal('cboTestCnt').secusw == "Y" && getSelectedVal('cboTestCnt').endyn == "N") {
			if (strSubStatus == "31" || strSubStatus == "32" || strSubStatus == "35") {
				$('#btnAdd').attr('disabled', false);
				testSw = true;		
				$('#chkOpen').attr('disabled', false);
				$('#btnTestLog').attr('disabled', false);	
			}
		} 
	} else if (strReqCd == "44") {
		if (getSelectedVal('cboTestCnt').secusw == "Y") {
			if (strSubStatus == "21" || strSubStatus == "23" || strSubStatus == "36") {
				$('#btnAdd').attr('disabled', false);
				scmFg = true;		
				$('#chkOpen').attr('disabled', false);
				$('#btnExlLoad').attr('disabled', false);
				$('#btnCaseCp').attr('disabled', false);
			}
		} 
	}
	
	getTestCase();
	getDocList();
	getTestCase_Sub(scmFg);

	if (testSw == true) {
		endTestCase();
	}
}

function getTestCase() {
	var tcObj = new Object();
	tcObj.IsrId = strIsrId;
	tcObj.IsrSub = strIsrSub;
	tcObj.UserId = strUserId;
	tcObj.ReqCd  = strReqCd;
	tcObj.TestSeq = getSelectedVal('cboTestCnt').cc_testseq;
 
	var tcInfo = new Object();
	tcInfo.tcObjData = tcObj;
	tcInfo.requestType = 'getTestCase'
	ajaxAsync('/webPage/rfc/RFCRegister', tcInfo, 'json',
			successGetTestCase);
}

function getDocList() {
	ajaxReturnData = null;
	var docSr = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		ReqCd  : "52",
		SubReq : strSubReq,
		requestType : 'getDocList'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/rfc/RFCRegister',
			docSr, 'json');
	if (ajaxReturnData !== 'ERR') {
		grdFile_dp = ajaxReturnData;
		grdFile.setData(grdFile_dp);
	}
}

function getTestCase_Sub(scmFg) {
	var tcObj = new Object();
	tcObj.IsrId = strIsrId;
	tcObj.IsrSub = strIsrSub;
	tcObj.UserId = strUserId;
	tcObj.ReqCd  = strReqCd;
	tcObj.TestSeq = getSelectedVal('cboTestCnt').cc_testseq;
	tcObj.scmSw   = scmFg;
	tcObj.testSw  = testSw;
 
	var tcInfo = new Object();
	tcInfo.tcObjData = tcObj;
	tcInfo.requestType = 'getTestCase_Sub'
	ajaxAsync('/webPage/rfc/RFCRegister', tcInfo, 'json',
			successGetTestCase_Sub);	
}

function endTestCase() {
	var tcObj = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		TestSeq : getSelectedVal('cboTestCnt').cc_testseq,
		requestType : 'endTestCase'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', tcObj, 'json',
			successEndTestCase);
}

function successEndTestCase(data) {
	if (data == "Y") {
		$('#btnEnd').css('visibility', 'visible');
		$('#btnEnd').attr('disabled', false);
	}
}

function successGetTestCase(data) {
	grdLst_dp = data;
	grdLst.setData(grdLst_dp); 			
				 
	if (strReqCd == "44" && $('#chkOpen').prop('disabled') == false) {
		$('#chkOpen').prop('checked', true);
		chkOpen_click();
	}
	if (grdLst_dp.length>0) {
		$('#btnExlSave').attr('disabled', false);
		$('#chkAllPass').attr('disabled', true);
		$('#chkAllPass').prop('checked', false);
	}
	else {
		$('#btnExlSave').attr('disabled', true);
		$('#btnDel').attr('disabled', true);
	}
}

function successGetTestCase_Sub(data) {

	casesub1_filter(data);
	casesub2_filter(data);
	
	casecond_filter(casesub1_dp);
	grdCond.setData(grdCond_dp);
	
	casechk_filter(casesub2_dp);
	grdChk.setData(grdChk_dp);
}

function casesub1_filter(data) {
	casesub1_dp = [];
	for(var i = 0; data.length > i; i++) {
		if(data[i].cc_gbncd == "C"){
			casesub1_dp.push(data[i]);
		}
	}
}

function casesub2_filter(data) {
	casesub2_dp = [];
	for(var i = 0; data.length > i; i++) {
		if(data[i].cc_gbncd == "R"){
			casesub2_dp.push(data[i]);
		}
	}
}

function casecond_filter(data) {
	grdCond_dp = [];
	for(var i = 0; data.length > i; i++) {
		if(data[i].cc_gbncd == "C"){
			if($('#chkOpen').prop('checked') == true && data[i].cc_caseseq == "999"){
				grdCond_dp.push(data[i]);
			} else if(grdLst.getList('selected').length <= 0) {
				continue;
			} else if($('#chkOpen').prop('checked') == false && data[i].cc_caseseq == grdLst.getList('selected')[0].cc_caseseq) {
				grdCond_dp.push(data[i]);
			}
		}
	}
}

function casechk_filter(data) {
	grdChk_dp = [];
	for(var i = 0; data.length > i; i++) {
		if(data[i].cc_gbncd == "R"){
			if($('#chkOpen').prop('checked') == true && data[i].cc_caseseq == "999"){
				grdChk_dp.push(data[i]);
			} else if(grdLst.getList('selected').length <= 0) {
				continue;
			} else if($('#chkOpen').prop('checked') == false && data[i].cc_caseseq == grdLst.getList('selected')[0].cc_caseseq) {
				grdChk_dp.push(data[i]);
			}
		}
	}
}

//테스트케이스복사
function CaseCopyInfo() {
	if (strIsrId == null || strIsrId == "") { 
		return;
	}
	var winName = "test";
	var f = document.popPam;   		//폼 name
	
	f.user.value 	= strUserId;    	
	f.code.value	= "CP";   
	f.redcd.value   = strReqCd;	
	f.isrId.value   = strIsrId + strIsrSub;
	
	nHeight = 740;
	nWidth  = 1200;

	cURL = "/webPage/winpop/PopCopyTestCase.jsp";
	myWin = winOpen(f, winName, cURL, nHeight, nWidth);

	var interval = null;
    interval = window.setInterval(function() {
        try {
            if( myWin == null || myWin.closed ) {
                window.clearInterval(interval);
                myWin = null;
				getTestCase();
            }
        } catch (e) {}
    }, 500);	
}

function chkOpen_click() {
	var findSw = false;
			
	if ($('#chkOpen').prop('checked') == true) {
		scmSw = true;
		$('#txtTestId').val("자동부여");
		$('#txtCase').val("");
		$('#txtCase').attr('readonly', false);
		grdInit();
		screenInit_scm();
		
		casecond_filter(casesub1_dp);
		grdCond.setData(grdCond_dp);
		
		casechk_filter(casesub2_dp);
		grdChk.setData(grdChk_dp);

		if (grdLst_dp.length == 0) { 
			$('#chkAllPass').attr('disabled', false);
		} else {
			$('#chkAllPass').attr('disabled', true);
		}
	} else {
		$("#txtTestId").val();
		$("#txtCase").attr('readonly', true);
		$("#txtCase").attr('disabled', true);
		$('#testTab').fadeTo(0, 0.5);
		$('#testTab').css('pointer-events', 'none');	
		$("#chkAllPass").attr('disabled', true);

		$("#txtResult").attr('disabled', true);
		$("#txtResult").attr('readonly', true);
		$("#txtTester").attr('disabled', true);
		$("#txtTestDay").attr('disabled', true);
		$("#btnAdd").attr('disabled', true);
		$("#btnDel").attr('disabled', true);
	}	
}

function grdInit() {
	for (var i=0; casesub1_dp.length > i; i++) {
		if (casesub1_dp[i].cc_gbncd == "C") {
			if (casesub1_dp[i].ITEMMSG == null || casesub1_dp[i].ITEMMSG == "") {
				if (casesub1_dp[i].cc_caseseq != "999" || casesub1_dp[i].cc_seqno != "000") {
					casesub1_dp.splice(i, 1);
					i--;
				}
			}	
		}
	}		
	for (var i=0; casesub2_dp.length > i; i++) {
		if (casesub2_dp[i].cc_gbncd == "C") {
			if (casesub2_dp[i].ITEMMSG == null || casesub2_dp[i].ITEMMSG == "") {
				if (casesub2_dp[i].cc_caseseq != "999" || casesub2_dp[i].cc_seqno != "000") {
					casesub2_dp.splice(i, 1);
					i--;
				}
			}
		}	
	}
}

function screenInit_scm() {			
			
	$('#btnAdd').attr('disabled', false);
	$('#btnDel').attr('disabled', false);
	$('#btngrdChkAdd').attr('disabled', false);
	$('#btngrdCondAdd').attr('disabled', false);
	$('#txtCase').attr('readonly', false);
	$('#chkOpen').attr('disabled', false);
	$('#chkAllPass').attr('disabled', true);
	$('#testTab').fadeTo(0, 1);
	$('#testTab').css('pointer-events', 'visible');
	$('#txtCase').attr('disabled', false);
	
	if (testSw == true) {
		screenInit_test();
	}
}

function screenInit_test() {
	$('#txtResult').attr('readonly', false);
	$('#datTestDay').css('visibility', 'visible');
	$('#calDatTestDay').css('visibility', 'visible');
	$('#calDatTestDay').css('pointer-event', 'visible');
	$('#txtTestDay').css('visibility', 'hidden');
	$('#txtTester').val(cboTestCnt.getList('selected')[0].cm_username);
	if(window.parent.strReqCd == "49") {
		var columns  = [
			{key: "ITEMMSG", label: "테스트조건",  width: 400, align: 'left'},
			{key: "testrst", 	label: "결과",  	width: 100, align: "left"}
			];
		grdChk.setConfig({columns: null});
		grdChk.setConfig({columns: columns});
	}
	$('#btnTestLog').attr('disabled', false);
}

function grdLst_click() {
	var tmpObj = new Object();
	scmSw = false;
	if (grdLst_dp.length > 0){
		grdInit();
		
		$('#txtCase').attr('disabled', false);
		
		$('#txtTestId').val(grdLst.getList('selected')[0].cc_caseseq);
		$('#txtCase').val(grdLst.getList('selected')[0].cc_casename);
		$('#txtCase').attr('readonly', true);
		if(grdChk.columns.length > 1 && window.parent.strReqCd == "49") {
			grdChk.removeColumn();
		}
		$('#chkOpen').prop('checked', false);
		   
		if (grdLst.getList('selected')[0].cc_scmuser == strUserId && $('#chkOpen').prop('disabled') == false) {
			scmSw = true;
			if (grdLst.getList('selected')[0].cc_nothing == "Y") {
				$('#chkAllPass').prop('checked', true);
				$('#btnDel').attr('disabled', false);
			} else {
				$('#chkAllPass').prop('checked', false);
				$('#txtCase').attr('readonly', false);
				$('#btnAdd').attr('disabled', false);
				$('#btnDel').attr('disabled', false);
			}
			AllPass();
			screenInit_scm();		   		
		} else if(grdLst.getList('selected')[0].cc_scmuser != strUserId){
			$('#btnAdd').attr('disabled', true);
			$('#btnDel').attr('disabled', true);
			$('#btngrdCondAdd').attr('disabled', true);
			$('#btngrdCondDel').attr('disabled', true);
			$('#btngrdChkAdd').attr('disabled', true);
			$('#btngrdChkDel').attr('disabled', true);
			
		}
		if (strReqCd == "52") {
			if (getSelectedVal('cboTestCnt').secusw == "Y" && getSelectedVal('cboTestCnt').endyn == "N") {
				if (strSubStatus == "31" || strSubStatus == "32" || strSubStatus == "35") {
					testSw = true;		
					$('#chkOpen').attr('disabled', false);
					$('#btnTestLog').attr('disabled', false);
				}
			} 
		}
		if (grdLst.getList('selected')[0].scmuser != null && grdLst.getList('selected')[0].scmuser != "") {
			$('#txtTester').val(grdLst.getList('selected')[0].tester);
			$('#txtTestDay').val(grdLst.getList('selected')[0].testday);
			$('#txtResult').val(grdLst.getList('selected')[0].cc_testrst);
			$('#datTestDay').val(new Date(Number($('#txtTestDay').val().substr(0,4)),
									(Number($('#txtTestDay').val().substr(5,2)) - 1),
									Number($('#txtTestDay').val().substr(8,2))));
			if (testSw ==false) {
				if(window.parent.strReqCd == "49") {
					var columns  = [
						{key: "ITEMMSG", label: "테스트조건",  width: 400, align: 'left'},
						{key: "testrst", 	label: "결과",  	width: 100, align: "left"}
						];
					grdChk.setConfig({columns: null});
					grdChk.setConfig({columns: columns});
				}
			}
				//$('#txtTester').attr('disabled', false);
				$('#txtTestDay').attr('disabled', false);
				$('#txtResult').attr('disabled', false);				    
		}
		if (testSw == true) {
			$('#datTestDay').css('visibility', 'visible');
			$('#calDatTestDay').css('visibility', 'visible');
			$('#calDatTestDay').css('pointer-event', 'visible');
			$('#txtTestDay').css('visibility', 'hidden');
			$('#txtResult').attr('readonly', false);
			//grdtstrst2.visible =true;
			$('#txtResult').attr('disabled', false);
			$('#btnAdd').attr('disabled', false);
			if ($('#txtTester').val() == "" || $('#txtTester').val() == null) {
				$('#txtTester').val(getSelectedVal('cboTestCnt').cm_username);
			}
		} else {
			$('#datTestDay').css('visibility', 'hidden');
			$('#calDatTestDay').css('visibility', 'hidden');
			$('#calDatTestDay').css('pointer-event', 'none');
			$('#txtTestDay').css('visibility', 'visible');
			$('#txtResult').attr('readonly', true);
		}
		
		casecond_filter(casesub1_dp);
		
		if (grdCond_dp.length == 0 && scmSw == true && $('#chkAllPass').prop('checked') == false) {
			tmpObj = new Object();
			tmpObj.cc_caseseq = grdLst.getList('selected')[0].cc_caseseq;
			tmpObj.ITEMMSG = "";
			tmpObj.cc_gbncd = "C";
			tmpObj.cc_seqno = "999";
			tmpObj.editable = "1";			   		
			grdCond_dp.push(tmpObj);			   			
		}
		grdCond.setData(grdCond_dp);
		
		casechk_filter(casesub2_dp)

		if (grdChk_dp.length == 0 && scmSw == true && $('#chkAllPass').prop('checked') == false) {
			tmpObj = new Object();
			tmpObj.cc_caseseq = grdLst.getList('selected')[0].cc_caseseq;
			tmpObj.ITEMMSG = "";
			tmpObj.cc_gbncd = "R";
			tmpObj.cc_seqno = "999";
			tmpObj.editable = "1";
			tmpObj.selected1 = "0";
			tmpObj.selected2 = "0";
			grdChk_dp.push(tmpObj);			   			
		}
		
		grdChk.setData(grdChk_dp);	   	
		if(grdCond_dp.length == 0){
			grdCondAdd(); 
			if(grdChk_dp.length == 0){
				grdChkAdd(); 
			}
		}
	}		
}

function AllPass() {
	if ($('#chkAllPass').prop('checked') == true) {
		$('#txtCase').val("");
		$('#txtTester').val("");
		$('#txtTestDay').val("");
		$('#txtResult').val("");
		$('#btnCaseCp').attr('disabled', true);
	}else{
		$('#btnCaseCp').attr('disabled', false);
	}
	if (scmSw == true) {
		if($('#chkAllPass').prop('checked')){
			$('#txtTestId').attr('disabled', true);
			$('#txtCase').attr('disabled', true);
			$('#txtTester').attr('disabled', true);
			$('#txtTestDay').attr('disabled', true);
			$('#txtResult').attr('disabled', true);
			$('#testTab').fadeTo(0, 0.5);
			$('#testTab').css('pointer-events', 'none');
			$('#btnCaseCp').attr('disabled', true);
		}else{
			//$('#txtTestId').attr('disabled', false);
			$('#txtCase').attr('disabled', false);
			//$('#txtTester').attr('disabled', false);
			$('#txtTestDay').attr('disabled', false);
			$('#txtResult').attr('disabled', false);
			$('#testTab').fadeTo(0, 1);
			$('#testTab').css('pointer-events', 'visible');
			$('#btnCaseCp').attr('disabled', false);
		}
	}	
}

function changeTabMenu() {
	if(document.getElementById("tab1").className == "on") { 
		$('#tabCheck').css('display', 'none');
		tabStatus = "test";
	} else if(document.getElementById("tab2").className == "on") { 
		$('#tabTest').css('display', 'none');
		grdChk.align();
		tabStatus = "chk"
	} 
}
//하단 탭 추가 버튼 클릭
function btnGrdAdd() {
	if(tabStatus == "test") {
		var tmpObj = new Object();
			
		tmpObj.ITEMMSG="";	
		if ($('#chkOpen').prop('checked') == true) {
			tmpObj.cc_caseseq = "999";	
		} else {
			tmpObj.cc_caseseq = grdLst.getList('selected')[0].cc_caseseq;
		}
		tmpObj.cc_gbncd = "C";	
		tmpObj.cc_seqno = "999";	
		tmpObj.editable = "1";				
		grdCond_dp.push(tmpObj);
		tmpObj = null;
		grdCond.setData(grdCond_dp);
	} else {
		var tmpObj = new Object();
			
		tmpObj.ITEMMSG="";	
		if ($('#chkOpen').prop('checked') == true) {
			tmpObj.cc_caseseq = "999";	
		} else {
			tmpObj.cc_caseseq = grdLst.getList('selected')[0].cc_caseseq;
		}
		tmpObj.cc_gbncd = "R";	
		tmpObj.cc_seqno = "999";	
		tmpObj.editable = "1";	
		tmpObj.selected1 = "0";
		tmpObj.selected2 = "0";				
		grdChk_dp.push(tmpObj);
		tmpObj = null;
		grdChk.setData(grdChk_dp);
	}
}
//하단 탭 제거 버튼 클릭
function btnGrdDel(tab) {
	if(tabStatus == "test") {
		if (grdCond.getList('selected').length < 0) { 
			return;
		}
	
		grdCond_dp.splice(grdCond.getList('selected')[0].__original_index, 1);
		grdCond.removeRow(grdCond.getList('selected')[0].__original_index);
	
		grdCond.setData(grdCond_dp);
	} else {
		if (grdChk.getList('selected').length < 0) { 
			return;
		}
		grdChk_dp.splice(grdChk.getList('selected')[0].__original_index, 1);
		grdChk.removeRow(grdChk.getList('selected')[0].__original_index)	
		
		grdChk.setData(grdChk_dp);
	}
}
//테스트조건 탭 row클릭
function grdCond_click() {
	if (grdCond.getList('selected').length > -1 || $('#chkOpen').prop('checked') == true) {

		$('#btngrdCondDel').attr('disabled', true);
		if ($('#btngrdCondAdd').prop('disabled') == false) {
			if ($('#chkOpen').prop('checked') == true) {
				return;
			} else {
				$('#btngrdCondDel').attr('disabled', false);
			}
		}
	}
}
//확인사항 탭 row클릭
function grdChk_click() {
	if(grdChk.getList('selected').length > -1 || $('#chkOpen').prop('checked') == true){

		$('#btngrdChkDel').attr('disabled', true);
		if ($('#btngrdCondDel').prop('disabled') == false) {
			if ($('#chkOpen').prop('checked') == true) { 
				return;
			} else {
				$('#btngrdCondDel').attr('disabled', false);
			}
		}
	}
}

function btnAdd_click(){
	if (strIsrId == null || strIsrId == "") return;
	
	var findSw = false;
	
	if ($('#chkOpen').prop('checked') == true) {
		if ($('#txtTestId').val() != "자동부여") {
			dialog.alert("신규등록여부를 정확히 선택한 후 등록하시기 바랍니다.");
			return;
		}
	}
	if (scmSw == true) {				
		if ($('#txtCase').val().length == 0) {
			dialog.alert("테스트케이스를 선택한 후 등록하시기 바랍니다.");
			return;
		}
		if ($('#chkAllPass').prop('checked') == false) { 
			if ($('#txtCase').val().trim().length == 0) {
				dialog.alert("테스트케이스를 입력한 후 등록하시기 바랍니다.");
				$('#txtCase').focus();
				return;
			}
			
			for (var i = 0; grdCond_dp.length > i; i++) {
				if (grdCond_dp[i].ITEMMSG.trim() != "" && grdCond_dp[i].ITEMMSG.trim() != null) {
					findSw = true;
					break;							
				}
			}
			if (findSw == false) {
				dialog.alert("테스트조건을 입력한 후 등록하시기 바랍니다.");
				$('#tab1').click();
				return;
			}
			findSw = false;
			for (var i = 0; grdChk_dp.length > i; i++) {
				if (grdChk_dp[i].ITEMMSG.trim() != "" && grdChk_dp[i].ITEMMSG.trim() != null) {
					findSw = true;
					break;							
				}
			}
			if (findSw == false) {
				dialog.alert("확인사항을  입력한 후 등록하시기 바랍니다.");
				$('#tab2').click();
				return;
			}
		}
	}
	if (testSw == true) {
		if ($('#chkAllPass').prop('checked') == false) {
			for (var i = 0; grdChk_dp.length>i; i++) {
				if (grdChk_dp[i].selected1 == null && grdChk_dp[i].selected2 == null) {
					dialog.alert("확인사항 ["+ grdChk_dp[i].ITEMMSG+"]에 대한 결과를 선택한 후 등록하시기 바랍니다.");
					return		
				}
				if (grdChk_dp[i].selected1 == null && grdChk_dp[i].selected2 == "0") {
					dialog.alert("확인사항 ["+ grdChk_dp[i].ITEMMSG+"]에 대한 결과를 선택한 후 등록하시기 바랍니다.");
					return		
				}
				if (grdChk_dp[i].selected1 == "0" && grdChk_dp[i].selected2 == null) {
					dialog.alert("확인사항 ["+ grdChk_dp[i].ITEMMSG+"]에 대한 결과를 선택한 후 등록하시기 바랍니다.");
					return		
				}
				if (grdChk_dp[i].selected1 == "0" && grdChk_dp[i].selected2 == "0") {
					dialog.alert("확인사항 ["+ grdChk_dp[i].ITEMMSG+"]에 대한 결과를 선택한 후 등록하시기 바랍니다.");
					return	
				}
			}	
			if ($('#txtResult').val().length == 0) {
				dialog.alert("테스트결과를 입력한 후 등록하시기 바랍니다.");
				$('#txtResult').focus();
				return;	
			}	
		
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth()+1
			var day = date.getDate();
			if(month < 10){
				month = "0"+month;
			}
			if(day < 10){
				day = "0"+day;
			}
		 
			var today = year+month+day;
		
			if (today < replaceAllString($('#datTestDay').val(),"/","")) {
				dialog.alert("테스트일자가 현재일 이후입니다. 정확히 선택하여 주십시오");
				$('#datTestDay').focus();
				return;		
			}
		}
		
	}
	   var etcObj = new Object();
					  
	   etcObj.IsrId = strIsrId;
	   etcObj.IsrSub = strIsrSub;
	   etcObj.UserId = strUserId;
	   if ($('#chkOpen').prop('checked') == true) { 
		   etcObj.testid = null;
	   } else {
		   etcObj.testid = grdLst.getList('selected')[0].cc_caseseq;
	   }

	   etcObj.reqcd = strReqCd;
	   if (scmSw == true) {
		   etcObj.scmsw = "1";
		   etcObj.Tcase = $('#txtCase').val();
		   if ($('#chkAllPass').prop('checked') == true) {
			   etcObj.passsw = "1";
		   } else {
			   etcObj.passsw = "0";	   					
		   }
	   } else {
		   etcObj.scmsw = "0";
	   }
	   
	   if (testSw == true) {
		   etcObj.testseq = getSelectedVal('cboTestCnt').cc_testseq;
		   etcObj.testsw = "1";
		   etcObj.tester = strUserId;
		   if (chkAllPass.selected == true) {
			   etcObj.passsw = "1";
		   } else {
			   etcObj.passsw = "0";
		   }  
		   
		   etcObj.testday = replaceAllString($('#datTestDay').val(),"/","");
		   etcObj.testrst = $('#txtResult').val().trim();
	   } else {
		   etcObj.testsw = "0";
	   }

	   var tcInfo = new Object();
	   tcInfo.etcObj = etcObj;
	   tcInfo.testList = grdCond_dp;
	   tcInfo.checkList = grdChk_dp;
	   tcInfo.requestType = 'setTcaseAdd2'
	   ajaxAsync('/webPage/rfc/RFCRegister', tcInfo, 'json',
			   successSetTcaseAdd);
	   
}

function successSetTcaseAdd(data) {
	if (testSw == true) {
		dialog.alert("통합테스트 결과 등록처리가 정상적으로 처리되었습니다.");
	} else {
		dialog.alert("통합테스트케이스 등록처리가 정상적으로 처리되었습니다.");
	}
	
	cboTestCnt_click();
}

function btnDel_click() {
	
	if (strIsrId == null || strIsrId == "") { 
		return;
	}
	if ($('#chkOpen').prop('checked') == true) {
		return;
	}
	if (scmSw == false) { 
		return;
	}
	if (grdLst.getList('selected').length <= 0) {
		dialog.alert("테스트케이스를 선택한 후 처리하시기 바랍니다.");
		return;
	}

	   var tcObj = new Object();
	   tcObj.IsrId = strIsrId;
	   tcObj.IsrSub = strIsrSub;
	   tcObj.UserId = strUserId;
	   tcObj.testid = grdLst.getList('selected')[0].cc_caseseq;
   
	   var tcInfo = new Object();
	   tcInfo.etcObj = tcObj;
	   tcInfo.requestType = "delCase"
   
	   ajaxAsync('/webPage/rfc/RFCRegister', tcInfo, 'json', successDelCase);
   
	   tcObj = null;		   
}

function successDelCase(data) {
	dialog.alert("통합테스트케이스 삭제처리가 정상적으로 처리되었습니다.");
		
	cboTestCnt_click();
}

function btnEnd_click() {

	var tcObj = new Object();
	tcObj.IsrId = strIsrId;
	tcObj.IsrSub = strIsrSub;
	tcObj.TestSeq = getSelectedVal('cboTestCnt').cc_testseq
	tcObj.UserId = strUserId;

	var tcInfo = new Object();
	tcInfo.etcObj = tcObj;
	tcInfo.requestType = "setTestEnd"

	ajaxAsync('/webPage/rfc/RFCRegister', tcInfo, 'json', successSetTestEnd);
}

function successSetTestEnd(data) {
	var retMsg = data;
	if (retMsg != "OK") {
		dialog.alert(retMsg);
		return
	}
	dialog.alert("통합테스트 종료요청이 정상적으로 처리되었습니다.");
	
	window.parent.getISRList(); 
}