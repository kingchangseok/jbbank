/** 단위테스트 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-04-26
 */

var userName = window.top.userName;
var userId = window.top.userId;
var adminYN = window.top.adminYN;
var userDeptName = window.top.userDeptName;
var userDeptCd = window.top.userDeptCd;
var strReqCd = window.top.reqCd;
var codeList    = window.top.codeList;          //전체 코드 리스트

var grdLst = new ax5.ui.grid();

var grdLst_dp = [];
var grdLst_simple_dp = [];
var cboEditor_dp = [];
var folderDefaultClosed = "";

var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strSubStatus = "";

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

$('[data-ax5select="cboEditor"]').ax5select({
	options : []
});

$(document).ready(function() {

})

function docListCall() {
	createViewGrid();
	getHisList();
	getSelectList();
}

function createViewGrid() {
	grdLst.setConfig({
		target: $('[data-ax5grid="grdLst"]'),
		sortable: true, 
		multiSort: true,
		showRowSelector: false,
		header: {
			align: "center",
			columnHeight: 30
		},
		paging : false,
		body: {
			columnHeight: 25,
			onClick: function () {
				this.self.clearSelect();
				this.self.select(this.dindex);
			},
			onDBLClick: function () {},
			trStyleClass: function () {},
			onDataChanged: function(){}
		},
		contextMenu: {
			iconWidth: 20,
			acceleratorWidth: 100,
			itemClickAndClose: false,
			icons: {
				'arrow': '<i class="fa fa-caret-right"></i>'
			}
		},		
		columns: [
			{key: "qrycd", 			label: "구분",  	width: 100, align: "left"},
			{key: "sysmsg",			label: "시스템",  	width: 120, align: "left"},
			{key: "rsrcname",		label: "프로그램명",   width: 100 , align: "left"},
			{key: "sta",   			label: "상태",	  width: 100	, align: "left"},
			{key: "username", 		label: "최종변경인",  width: 100,	alingn: "center"},
			{key: "lastdate", 		label: "최종변경일",    width: 100, align: "center"},
			{key: "dirpath", 		label: "프로그램경로",   width:120, align: "left"},
			{key: "story",			label: "프로그램설명",   width:120, align: "left"},
			{key: "isrid",  		label: "ISR-ID", 	  width: 100, align: "left"},
			{key: "isrtitle",  		label: "요청제목", 	  width: 100, align: "left"},
		]
	});
}

function screenInit() {
	cboEditor_dp = [];
	grdLst_dp = [];
	grdLst_simple_dp = [];
}

function getSelectList() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.SubId = strIsrSub;
	obj.UserId = "";
	obj.ReqCd = "03";
	
	var data = new Object();
	data.objData = obj;
	data.requestType = 'getSelectList';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetSelectList);
}

function successGetSelectList(data) {
	cboEditor_dp = data;

	options = [];
	$.each(cboEditor_dp, function(i, value) {
		options.push({
			value : value.cc_scmuser,
			text : value.cm_username,
			cc_status: value.cc_status, 
			cc_substatus: value.cc_substatus,
			cnt: value.cnt,
			creatdt: value.creatdt,
			devedday : value.devedday,
			devstday : value.devstday,
			devtime : value.devtime,
			eddate : value.eddate,
			devmm : value.devmm,
			lastdt: value.lastdt,
			pgmsw: value.pgmsw
		});
	});

	$('[data-ax5select="cboEditor"]').ax5select({
		options : options
	});
	
	if(cboEditor_dp.length > 0){
		for (var i = 0; cboEditor_dp.length > i; i++) {
			if (cboEditor_dp[i].cc_scmuser == strUserId) {
				$('[data-ax5select="cboEditor"]').ax5select("setValue", cboEditor_dp[i].cc_scmuser, true);
				break;
			}
		}
		cboEditor_click();
	}
}

function cboEditor_click() {

	if(getSelectedIndex('cboEditor') < 0) {
		return;
	}

	grdLst_simple_dp = [];
	if (grdLst_dp.length > 0) {
		grdLst_simple_dp = grdLst_dp;
		grdLst.setData(grdLst_simple_dp);
	}
}

function getHisList() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.SubId = strIsrSub;
	obj.UserId = strUserId;
	obj.qryGbn = "D";
	
	var data = new Object();
	data.etcObj = obj;
	data.requestType = 'getHisList';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetHisList);	
}

function successGetHisList(data) {
	grdLst.setData(data);
}