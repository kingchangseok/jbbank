/** RFC 발행 접수 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-04-21
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.parent.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var strAcptNo 		= window.parent.strAcptNo;
var strStatus		= window.parent.strStatus; 
var strIsrId		= window.parent.strIsrId; 

var grdLst = new ax5.ui.grid();
var picker = new ax5.ui.picker();

var strUserId = "";
var strReqCd = "";
var strISRSTAMAIN = "";
var strISRSTASUB = "";
var strSTATUS = "";
var cboTeam_dp = [];
var grdLst_dp = [];
var cboQryGbn_dp = [];
var userInfo_dp = [];

var tab1 = null;
var tabBase = null;

var loadSrReg = false;

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

$('[data-ax5select="cboQryGbn"]').ax5select({
	options : []
});

$('[data-ax5select="cboTeam"]').ax5select({
	options : []
});

$('#datStD').val(getDate('DATE', 0));
$('#datEdD').val(getDate('DATE', 0));

$('#datStD').attr("disabled", true);
$('#datEdD').attr("disabled", true);
$('#cal1').css("pointer-events", "none");
$('#cal2').css("pointer-events", "none");
$('#cal1').css("background-color", "whitesmoke");
$('#cal2').css("background-color", "whitesmoke");

picker.bind(defaultPickerInfo('topDate', 'top'));

$(document).ready(function() {
	// 조회
	$('#btnQry').bind('click', function() {
		getISRList();
	});

	// 변경관리정보
	$('#btnReqInfo').bind('click', function() {
		popOpen("REQ");
	});
	
	// 테스트관리정보
	$('#btnTestInfo').bind('click', function() {
		popOpen("TEST");
	});

	strReqCd = "41";
	strUserId = userId;

	if (strUserId == "" || strUserId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}
	
	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	tabBase = $('#frmBaseISRInfo').get(0).contentWindow;

	tab1.strUserId = strUserId;
	tab1.strReqCd = "41";
	
	document.getElementById('frmRFCAccept').onload = function() {
		getTeamInfoGrid2();
	}
	
	clickTabMenu();
	$("#tabRFCAccept").show();	
});

grdLst.setConfig({
	target : $('[data-ax5grid="grdLst"]'),
	sortable : true,
	multiSort : true,
	header : {
		align : "center",
		columnHeight : 30
	},
	body : {
		columnHeight : 28,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
			iSRID_Click(this.item, this.dindex, false);
		}
	},
	columns : [ {
		key : "cc_isrid",
		label : "ISR ID",
		width : 150,
		align : 'center'
	}, {
		key : "cc_isrsub",
		label : "#SUB",
		width : 70,
		align : 'center'
	}, {
		key : "isrtitle",
		label : "요청제목",
		width : 200,
		align : 'center'
	}, {
		key : "reqday",
		label : "요청등록일",
		width : 150,
		align : 'center'
	}, {
		key : "reqenddt",
		label : "완료요청일",
		width : 150,
		align : 'center'
	}, {
		key : "requser",
		label : "요청인",
		width : 100,
		align : 'center'
	}, {
		key : "reqdept",
		label : "요청부서",
		width : 150,
		align : 'center'
	}, {
		key : "recvdept",
		label : "접수파트",
		width : 150,
		align : 'center'
	}, {
		key : "status",
		label : "진행현황",
		width : 250,
		align : 'center'
	}, ]
});

function getTeamInfoGrid2() {
	var userInfo = {
		selMsg : "All",
		cm_useyn : "Y",
		gubun : "sub",
		itYn : "N",
		requestType : 'getTeamInfo'
	}
	ajaxAsync('/webPage/common/CommonTeamInfo', userInfo, 'json',
			successGetTeamInfoGrid2);
}

function successGetTeamInfoGrid2(data) {
	data[0].cm_deptname = "전체"
	cboTeam_dp = data;

	$('[data-ax5select="cboTeam"]').ax5select({
		options: injectCboDataToArr(cboTeam_dp, 'cm_deptcd' , 'cm_deptname')
	});	

	cboSet();
}

function screenInit() {
	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	tabBase = $('#frmBaseISRInfo').get(0).contentWindow;
	
	$('#btnTestInfo').attr('disabled', true);
	$('#btnReqInfo').attr('disabled', true);
	$("#srTab").fadeTo(0, 0.5);
	$('#srTab').css('pointer-events', 'none');	
	   
	grdLst_dp = [];
	tabBase.$('#txtISRId').val("");
	tabBase.$('#txtReqDT').val("");
	tabBase.$('#txtStatus').val("");
	tabBase.$('#txtTitle').val("");
	tabBase.$('#txtReqDept').val("");
	tabBase.$('#txtProStatus').val("");
	tabBase.$('#txtEditor').val("");
	tabBase.$('#txtComEdDt').val("");
						   
	tab1.screenInit('M');
}

function cboSet() {
	var tmpObj = new Object();
	tmpObj.cm_micode = "00";
	tmpObj.cm_codename = "전체";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = {};
	tmpObj.cm_micode = "01";
	tmpObj.cm_codename = "접수대상목록";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = {};
	tmpObj.cm_micode = "02";
	tmpObj.cm_codename = "담당자지정대상";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = {};
	tmpObj.cm_micode = "03";
	tmpObj.cm_codename = "기 접수목록";
	cboQryGbn_dp.push(tmpObj);	

	$('[data-ax5select="cboQryGbn"]').ax5select({
		options : injectCboDataToArr(cboQryGbn_dp, 'cm_micode', 'cm_codename')
	});

	$('[data-ax5select="cboQryGbn"]').ax5select("setValue", "01", true);

	getISRList();
}

//isr 조회
function getISRList() {
	if (getSelectedVal('cboQryGbn').value === '00') {
		if ($("#datStD").val() > $("#datEdD").val()) {
			dialog.alert("조회기간을 정확하게 선택하여 주십시오.");
			return;
		}
	}

	grdLst.setData([]);
	screenInit();	
	
	var tmpObj = new Object();
	tmpObj.userid = strUserId;
	tmpObj.reqcd = strReqCd;
	tmpObj.qrygbn = getSelectedVal('cboQryGbn').cm_micode;
	if(getSelectedIndex('cboTeam') > 0) {
		tmpObj.reqdept = getSelectedVal('cboTeam').cm_deptcd;
	}
	tmpObj.secuyn = "Y";
	if (tmpObj.qrygbn == "00" || tmpObj.qrygbn == "02") {
		tmpObj.stday = replaceAllString($('#datStD').val(),"/","");
		tmpObj.edday = replaceAllString($('#datEdD').val(),"/","");
	}

	var tmpInfo = new Object();
	tmpInfo.tmpObjData = tmpObj;
	tmpInfo.requestType = "getPrjList_Chg"

	ajaxAsync('/webPage/rfc/RFCRegister', tmpInfo, 'json', successGetPrjList_Chg);

	tmpObj = null;
}

function successGetPrjList_Chg(data) {
	if (data !== 'ERR') {

		grdLstData = clone(data);

		grdLst.setData(grdLstData);
		if (window.parent.strIsrId != null
				&& window.parent.strIsrId != undefined
				&& window.parent.strIsrId != "" && GlobalSelectCk) {
			var selecteCk = false;
			$(grdLst.list)
					.each(
							function(i) {
								if (this.cc_srid == window.parent.strIsrId) {
									selecteCk = true;
									grdLst.select(i);
									window.parent
											.iSRID_Click(
													grdLst.list[grdLst.selectedDataIndexs],
													grdLst.selectedDataIndexs,
													true);
								}
							});
			if (!selecteCk) {
				// 조회시 첫번재 선택 안되게 수정
				// grdLst.select(0);
			}
		} else {
			window.parent.selectPage = null;
			window.parent.selectedIndex = -1;
		}
	}
}

$("#cboQryGbn").bind('change', function() {
	$('#datStD').attr("disabled", true);
	$('#datEdD').attr("disabled", true);
	$('#cal1').css("pointer-events", "visible");
	$('#cal1').css("background-color", "whitesmoke");
	$('#cal2').css("pointer-events", "visible");
	$('#cal2').css("background-color", "whitesmoke");		
	if (getSelectedVal('cboQryGbn').cm_micode == '00') {
		$('#datStD').attr("disabled", false);
		$('#datEdD').attr("disabled", false);
		$('#cal1').css("pointer-events", "visible");
		$('#cal2').css("pointer-events", "visible");
		$('#cal1').css("background-color", "");	
		$('#cal2').css("background-color", "");			
	}
});

//isr 클릭 
function iSRID_Click(data) {
	grdLst_dp = [];
	grdLst_dp = data;

	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	
	tabBase.$('#txtISRId').val(grdLst.getList('selected')[0].isrid);
	tabBase.$('#txtReqDT').val(grdLst.getList('selected')[0].reqdate);
	tabBase.$('#txtStatus').val(grdLst.getList('selected')[0].reqsta1);
	tabBase.$('#txtTitle').val(grdLst.getList('selected')[0].isrtitle);
	tabBase.$('#txtReqDept').val(grdLst.getList('selected')[0].reqdept);
	tabBase.$('#txtProStatus').val(grdLst.getList('selected')[0].reqsta2);
	tabBase.$('#txtEditor').val(grdLst.getList('selected')[0].requser);
	tabBase.$('#txtComEdDt').val(grdLst.getList('selected')[0].reqenddt);

	$('#btnTestInfo').attr('disabled', true);
	$('#btnReqInfo').attr('disabled', true);

	if(grdLst.getList('selected')[0].maintab.indexOf("R") >= 0) {
		$('#btnReqInfo').attr('disabled', false);
	}
	if(grdLst.getList('selected')[0].maintab.indexOf("T") >= 0) {
		$('#btnTestInfo').attr('disabled', false);
	}

	$("#srTab").fadeTo(0, 1);
	$('#srTab').css('pointer-events', 'visible');		
	tab1.strIsrId = "";
	tab1.strIsrSub = "";

	var grdObj = new Object();
	grdObj.IsrId = grdLst.getList('selected')[0].cc_isrid;
	grdObj.IsrSub = grdLst.getList('selected')[0].cc_isrsub;
	grdObj.ReqCd = "CHG"
	grdObj.UserId = strUserId;

	var grdInfoData = new Object();
	grdInfoData.grdData = grdObj;
	grdInfoData.requestType = "getSubTab"

	ajaxAsync('/webPage/sr/SRAccept', grdInfoData, 'json', successGetSubTab);
}

function successGetSubTab(data) {
	var tmpSubTab = data;

	if(tmpSubTab.indexOf("41") >= 0) {
		//tab1.enabled = true;
		$("#srTab").fadeTo(0, 1);
		$('#srTab').css('pointer-events', 'visible');	
	}
	changeTabMenu();
}

//tabnavi_click
function changeTabMenu() {
	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	
	if(document.getElementById("tab1").className == "on") {
		if(grdLst.getList('selected').length <= 0) {
			tab1.screenInit("M");
			return;
		}
		if (tab1.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab1.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab1.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab1.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab1.strSubStatus = grdLst.getList('selected')[0].cc_substatus;		
		tab1.strUserId = strUserId;
		tab1.strReqCd = "41";
		tab1.screenInit("M");
		tab1.rfcRecvCall();
	}
}

//탭메뉴 클릭 이벤트
function clickTabMenu() {
	$("ul.tabs li").click(function () {
		
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
		
		$("#" + activeTab).fadeIn();
		changeTabMenu();
	});
}

//팝업
function popOpen(status){
	var popName = null;
	if(grdLst.list.length < 0) { 
		return;
	}
	if(status === 'REQ') { 
		popName = 'PopReq'
	} else if(status ==='CHG') {
		popName = 'PopChg'
	} else {
		popName = 'PopTest'
	}
	var winName = popName + 'Info';
	var f = document.popPam;   		
    
	f.user.value 	= strUserId;    	
	f.code.value	= status;  
	f.redcd.value   = "";	
	f.isrId.value   = grdLst.getList('selected')[0].cc_isrid + '-' + grdLst.getList('selected')[0].cc_isrsub;
    
	nHeight = 740;
    nWidth  = 1200;

	cURL = "/webPage/winpop/" + popName + "Info.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
