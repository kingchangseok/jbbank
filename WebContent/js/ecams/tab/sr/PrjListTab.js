/** PrjList 화면 정의 (공통화면)
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이성현
 * 	버전 : 1.1
 *  수정일 : 2019-06-14
 */

var userName 		= window.top.userName;
var userId 			= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName	= window.top.userDeptName;
var userDeptCd 		= window.top.userDeptCd;

var reqCd 	  		= window.parent.reqCd;
var strIsrId		= window.parent.strIsrId;
var cboQryGbnData	= window.parent.cboQryGbnData; // 대상구분

var firstGrid 	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();

var firstGridData   = [];
var cboReqDeptData 	= [];
var cboCatTypeData 	= [];
var cboQryData		= [];

var completeReadyFunc = false;

$('[data-ax5select="cboReqDept"]').ax5select({ options: [] });
$('[data-ax5select="cboCatType"]').ax5select({ options: [] });
$('[data-ax5select="cboQryGbn"]').ax5select({ options: [] });
$('[data-ax5select="cboQry"]').ax5select({ options: [] });

$('#datStD').val(getDate('DATE',-1));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfo('basic', 'top'));

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            window.parent.isrID_click(this.item);
        },
    },
    columns: [
    	{key: "cc_srid",		label: "SR-ID",  	width: '10%', 	align: 'center'},
    	{key: "cc_reqtitle", 	label: "요청제목",  	width: '35%',	align: 'left'},
    	{key: "createdate", 	label: "등록일",  	width: '7%', 	align: 'center'},
    	{key: "reqcompdat", 	label: "완료요청일",  	width: '7%', 	align: 'center'},
    	{key: "reqdept", 		label: "요청부서",  	width: '10%', 	align: 'left'},
    	{key: "cattype", 		label: "분류유형",  	width: '8%', 	align: 'left'},
    	{key: "chgtype", 		label: "변경종류",  	width: '8%', 	align: 'left'},
    	{key: "status", 		label: "진행현황",  	width: '8%', 	align: 'left'},
    	{key: "workrank", 		label: "작업순위",  	width: '7%', 	align: 'center'}
    ]
});

$(document).ready(function() {
	console.log('############## PrjListTab.js ##############');
	
	// 조회 버튼 클릭
	$('#btnQry').bind('click', function() {
		getPrjList();
	});
	
	// 초기화 버튼 클릭
	$('#btnReset').bind('click', function() {
		resetScreen();
	});
	
	// 대상구분 변경
	$('#cboQryGbn').bind('change', function() {
		cboQryGbn_click();
	});
	
	completeReadyFunc = true;
});

// 각 SR메뉴들에서 호출하는 함수
function screenInit() {
	getTeamInfo();
	getCodeInfo();
	
	if(reqCd != 'INFO') {
		$('#divQry').hide();
		
		if(reqCd == 'CP43' || reqCd == 'CP44' || reqCd == 'CP54' || reqCd == 'CP55') {
			$('#btnExcel').show();
		}else {
			$('#btnExcel').hide();
		}
	}else {
		setCboElement();
	}
}

// 각 SR메뉴들에서 호출하는 함수
function cboQryGbn_click() {
	$('#datStD').prop("disabled", true); 
	$('#datEdD').prop("disabled", true);
	disableCal(true, 'datStD');
	disableCal(true, 'datEdD');
	if(getSelectedVal('cboQryGbn') != null && getSelectedVal('cboQryGbn').cm_micode == '00') {
		$('#datStD').prop("disabled", false); 
		$('#datEdD').prop("disabled", false);
		disableCal(false, 'datStD');
		disableCal(false, 'datEdD');
	}
	getPrjList();
}

function getTeamInfo() {
	var data =  new Object();
	data = {
		SelMsg 		: 'All',
		cm_useyn 	: 'Y',
		gubun 		: 'req',
		itYn 		: 'N',
		requestType	: 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetTeamInfoGrid2);
}

function successGetTeamInfoGrid2(data) {
	cboReqDeptData = data;
	$('[data-ax5select="cboReqDept"]').ax5select({
        options: injectCboDataToArr(cboReqDeptData, "cm_deptcd", "cm_deptname")
	});
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('CATTYPE','ALL','N')
	]);
	
	cboCatTypeData = codeInfos.CATTYPE;
	$('[data-ax5select="cboCatType"]').ax5select({
        options: injectCboDataToArr(cboCatTypeData, "cm_micode", "cm_codename")
	});
	
	if(cboQryGbnData == null || cboQryGbnData == undefined) cboQryGbnData = window.parent.cboQryGbnData; 
	$('[data-ax5select="cboQryGbn"]').ax5select({
        options: injectCboDataToArr(cboQryGbnData, "cm_micode", "cm_codename")
	});
	$('[data-ax5select="cboQryGbn"]').ax5select('setValue', cboQryGbnData[1].cm_micode, true);
}

function setCboElement() {
	var tmpObj = [
		{cm_codename : "전체",	cm_micode : "00"},
		{cm_codename : "개발자",	cm_micode : "01"},
		{cm_codename : "요청제목",	cm_micode : "02"}
	]
	
	$('[data-ax5select="cboQry"]').ax5select({
        options: injectCboDataToArr(tmpObj, "cm_micode", "cm_codename")
	});
}

// cmdQry_click
function getPrjList() {
	firstGridData = [];
	firstGrid.setData([]);
	
	window.parent.subScreenInit();
	
	console.log('[PrjListTab] getSelectedIndex ==> ', getSelectedIndex('cboQryGbn'));
	if(getSelectedIndex('cboQryGbn') < 0) return;
	
	var strStd = replaceAllString($('#datStD').val(), '/', '');
	var strEdd = replaceAllString($('#datEdD').val(), '/', '');
	
	if(getSelectedVal('cboQryGbn').cm_micode == '00') {
		if(strStd > strEdd) {
			dialog.alert('조회기간을 정확하게 선택하여 주십시오.');
			return;
		}
	}
	
	var tmpObj = new Object();
	tmpObj.userid = userId;
	tmpObj.reqcd = reqCd;
	tmpObj.qrygbn = getSelectedVal('cboQryGbn').cm_micode;
	tmpObj.secuyn = 'Y';
	if(getSelectedVal('cboQryGbn').cm_dateyn == 'Y') {
		tmpObj.stday = strStd;
		tmpObj.edday = strEdd;
		
		if(strStd > getDate('DATE',0)) {
			dialog.alert('신청일자 시작일이 오늘보다 이후일 수 없습니다.');
			return;
		}
	}
	
	if(getSelectedIndex('cboReqDept') > 0) tmpObj.reqdept = getSelectedVal('cboReqDept').cm_deptcd;
	if(getSelectedIndex('cboCatType') > 0) tmpObj.cattype = getSelectedVal('cboCatType').cm_micode;
	
	tmpObj.admin = adminYN;
	
	if(reqCd == '99' || reqCd == 'LINK' || reqCd == 'CP43' || reqCd == 'CP44') {
		tmpObj.isrid = strIsrId;
		tmpObj.secuyn = 'N';
	}
	
	if(getSelectedIndex('cboQry') > 0) {
		if($('#txtQry').val().trim().length == 0) {
			dialog.alert('검색조건을 입력하여 주시기 바랍니다.');
			return;
		}
		
		tmpObj.qrycd = getSelectedVal('cboQry').cm_micode;
		tmpObj.qrytxt = $('#txtQry').val().trim();
	}
	
	
	var prjInfo = new Object();
	prjInfo.etcData = tmpObj;
	prjInfo.requestType = 'getPrjList';
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/common/PrjInfoServlet', prjInfo, 'json', successGetPrjList);
	
}

function successGetPrjList(data){
	$(".loding-div").remove();
	
	if (typeof data == 'string' && data.indexOf('ERROR')>=0) {
		dialog.alert(data.substr(5));
		return;
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
	
	if(firstGridData.length > 0) {
		firstGrid.select(0);
		window.parent.isrID_click(firstGridData[0]);
	}
}

// prjListTab 화면 초기화 버튼 클릭
function resetScreen() {
	$('[data-ax5select="cboCatType"]').ax5select("setValue", cboCatTypeData[0].cm_micode, true); //분류유형
	$('[data-ax5select="cboQryGbn"]').ax5select('setValue', cboQryGbnData[1].cm_micode, true); //대상구분
	$('[data-ax5select="cboReqDept"]').ax5select('setValue', cboReqDeptData[0].cm_deptcd, true); //요청부서
	$('#datStD').prop("disabled", true); 
	$('#datEdD').prop("disabled", true);
}