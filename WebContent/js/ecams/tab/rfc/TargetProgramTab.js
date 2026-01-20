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
var picker = new ax5.ui.picker();

var grdLst_dp = [];
var grdLst_simple_dp = [];
var cboEditor_dp = [];
var folderDefaultClosed = "";

var strIsrId = "";
var strIsrSub = "";
var strUserId = "";

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
	//대상프로그램추가
	$('#btnReq').bind('click', function() {
		btnReq_Click();
	});
	createViewGrid();
})

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
			},
			items: [
				{type: 1, label: "파일삭제"}
			],
			popupFilter: function (item, param) {
				if(grdLst.getList('selected').length < 1){
					return false;
				}
				 return true;
				
			},
			onClick: function (item, param) {
				if(grdLst.getList('selected').length < 0) {
					return;
				}
				delProg();
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
			{key: "isrsta",  		label: "진행상태", 	  width: 100, align: "left"},
			{key: "isrtitle",  		label: "요청제목", 	  width: 100, align: "left"},
		]
	});
}

function screenInit() {
	cboEditor_dp = [];
	grdLst_dp = [];
	grdLst_simple_dp = [];
}

function docListCall() {
	getHisList();
	getSelectList();
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
	scmSw = false;

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
	var findSw = false;
	
	$('#btnReq').css('visibility','hidden');
	$('#btnReq').attr('disabled', true);
	if (getSelectedIndex('cboEditor') < 0) {
		return;
	}
	grdLst_simple_dp = [];
	if (grdLst_dp.length > 0) {
		grdLst_simple_dp = grdLst_dp;
		grdLst.setData(grdLst_simple_dp);
	}
	if (getSelectedVal('cboEditor').value == strUserId) {
		if (getSelectedVal('cboEditor').cc_substatus == "23" ||
			getSelectedVal('cboEditor').cc_substatus == "24" ||
			getSelectedVal('cboEditor').cc_substatus == "35") {
			findSw = true;	
		}
	}
	if (findSw == true) {
		for (var i = 0; grdLst_simple_dp.length > i; i++) {
			if (grdLst_simple_dp[i].selfsw == "Y") {
				findSw = false		;
				break;
			}	
		}
	}
	if (findSw == true) {
		btnReq.visible = true;
		btnReq.enabled = true;   
		$('#btnReq').css('visibility','visible');
		$('#btnReq').attr('disabled', false);		
	}
}

function getHisList() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.SubId = strIsrSub;
	obj.UserId = strUserId;
	obj.qryGbn = "P";
	
	var data = new Object();
	data.etcObj = obj;
	data.requestType = 'getHisList';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successGetHisList);	
}

function successGetHisList(data) {
	grdLst.setData(data);
}

function btnReq_Click() {
	var winName = "poptargetprogram";
	var f = document.popPam;   		//폼 name
	
	f.user.value 	= strUserId;    	
	f.code.value	= "RP";   
	f.redcd.value   = strReqCd;	
	f.isrId.value   = strIsrId + strIsrSub;
	
	nHeight = 740;
	nWidth  = 1200;

	cURL = "/webPage/winpop/PopTargetProgram.jsp";
	myWin = winOpen(f, winName, cURL, nHeight, nWidth);

	var interval = null;
    interval = window.setInterval(function() {
        try {
            if( myWin == null || myWin.closed ) {
                window.clearInterval(interval);
                myWin = null;
				getHisList();
            }
        } catch (e) {}
    }, 500);
}

function delProg() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.IsrSub = strIsrSub;
	obj.UserId = getSelectedVal('cboEditor').value;
	obj.ItemId = grdLst.getList('selected')[0].itemid;
	
	var data = new Object();
	data.etcObj = obj;
	data.requestType = 'delProg';
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json',
			successDelProg);	
}

function successDelProg(data) {
	var retMsg = data;
	if (retMsg != "0") {
		dialog.alert(retMsg);
	} else {
		grdLst.removeRow("selected");
		grdLst.contextMenu.close();
	}

}