/** 변경관리종료 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-05-03
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
var approvalModal = new ax5.ui.modal();

var strUserId = "";
var strReqCd = "";
var cboTeam_dp = [];
var grdLst_dp = [];
var cboQryGbn_dp = [];
var TextSave = "";
var TextCheck = "";

var tab1 = null;
var tab2 = null;
var tab3 = null;
var tab4 = null;
var tab5 = null;
var tab6 = null;
var tab7 = null;
var tab8 = null;
var tabBase = null;

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

	// 요구관리정보
	$('#btnReqInfo').bind('click', function() {
		popOpen("REQ");
	});
	
	// 테스트관리정보
	$('#btnTestInfo').bind('click', function() {
		popOpen("TEST");
	});

	strReqCd = "49";
	strUserId = userId;

	if (strUserId == "" || strUserId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}
	
	tab1 = $('#frmRFCAccept').get(0).contentWindow; // rfc접수
	tab2 = $('#frmDEVPlan').get(0).contentWindow; // 개발계획/실적등록
	tab3 = $('#frmUnitTest').get(0).contentWindow; // 단위테스트
	tab4 = $('#frmIntegrationTest').get(0).contentWindow; // 통합테스트
	tab5 = $('#frmTargetProgram').get(0).contentWindow; // 대상 프로그램
	tab6 = $('#frmTargetOutput').get(0).contentWindow; // 대상 산출물
	tab7 = $('#frmRFCComplete').get(0).contentWindow; // 변경관리종료
	tab8 = $('#frmWorkPlan').get(0).contentWindow; // 작업계획서
	
	clickTabMenu();
	
	document.getElementById('frmBaseISRInfo').onload = function() {
		getTeamInfoGrid2();
	}
						
	$('#tabTargetProgram').width($('#tabRFCAccept').width()+10);
	$('#tabTargetOutput').width($('#tabRFCAccept').width()+10);
	$("#tabDEVPlan").width($('#tabRFCAccept').width()+10);
	$('#tabRFCAccept').width($('#tabRFCAccept').width()+10);
	$('#tabUnitTest').width($('#tabRFCAccept').width()+10);
	$('#tabIntegrationTest').width($('#tabRFCAccept').width()+10);
	$('#tabWorkPlan').width($('#tabWorkPlan').width()+10);
	$('#tabRFCComplete').show();
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
		key : "chguser",
		label : "변경관리자",
		width : 100,
		align : 'center'
	},{
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
		itYn : "Y",
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
	tabBase = $('#frmBaseISRInfo').get(0).contentWindow;

	$('#btnTestInfo').attr('disabled', true);
	$('#btnReqInfo').attr('disabled', true);
	$("#srTab").fadeTo(0, 0.5);
	$('#srTab').css('pointer-events', 'none');
	$("ul.tabs li").fadeTo(0, 0.5);	
	$("ul.tabs li").css('pointer-events', 'none');
	   
	grdLst_dp = [];
	tabBase.$('#txtISRId').val("");
	tabBase.$('#txtReqDT').val("");
	tabBase.$('#txtStatus').val("");
	tabBase.$('#txtTitle').val("");
	tabBase.$('#txtReqDept').val("");
	tabBase.$('#txtProStatus').val("");
	tabBase.$('#txtEditor').val("");
	tabBase.$('#txtComEdDt').val("");
}

function cboSet() {
	var tmpObj = new Object();
	tmpObj.cm_micode = "00";
	tmpObj.cm_codename = "전체";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = new Object();
	tmpObj.cm_micode = "01";
	tmpObj.cm_codename = "정상종료대상";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = new Object();
	tmpObj.cm_micode = "02";
	tmpObj.cm_codename = "진행중단대상";
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
	tmpObj.secuyn = "Y";
	if (getSelectedVal('cboQryGbn').cm_micode == "00") {
		tmpObj.stday = replaceAllString($('#datStD').val(),"/","");
		tmpObj.edday = replaceAllString($('#datEdD').val(),"/","");
	}
	if(getSelectedIndex('cboTeam') > 0) {
		tmpObj.reqdept = getSelectedVal('cboTeam').cm_deptcd;
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
		$('#cal1').css("pointer-events", "auto");
		$('#cal2').css("pointer-events", "auto");
		$('#cal1').css("background-color", "");	
		$('#cal2').css("background-color", "");			
	}
});

//isr 클릭 
function iSRID_Click(data) {
	if(grdLst.getList('selected').length <= 0) {
		return;
	}

	grdLst_dp = [];
	grdLst_dp = data;

	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	tab2 = $('#frmDEVPlan').get(0).contentWindow;
	tab3 = $('#frmUnitTest').get(0).contentWindow; 
	tab5 = $('#frmTargetProgram').get(0).contentWindow;
	tab6 = $('#frmTargetOutput').get(0).contentWindow; 
	tab7 = $('#frmRFCComplete').get(0).contentWindow; 
	tab8 = $('#frmWorkPlan').get(0).contentWindow;
	
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

	if(grdLst.getList('selected')[0].maintab.indexOf("C") >= 0) {
		$('#btnReqInfo').attr('disabled', false);
	}
	if(grdLst.getList('selected')[0].maintab.indexOf("T") >= 0) {
		$('#btnTestInfo').attr('disabled', false);
	}

	$("#srTab").fadeTo(0, 1);
	$('#srTab').css('pointer-events', 'visible');		
	tab1.strIsrId = "";
	tab1.strIsrSub = "";
	tab2.strIsrId = "";
	tab2.strIsrSub = "";
	tab3.strIsrId = "";
	tab3.strIsrSub = "";
	tab5.strIsrId = "";
	tab5.strIsrSub = "";
	tab6.strIsrId = "";
	tab6.strIsrSub = "";
	tab7.strIsrId = "";
	tab7.strIsrSub = "";
	tab8.strIsrId = "";
	tab8.strIsrSub = "";

	var grdObj = new Object();
	grdObj.IsrId = grdLst.getList('selected')[0].cc_isrid;
	grdObj.IsrSub = grdLst.getList('selected')[0].cc_isrsub;
	grdObj.ReqCd = "CHG"
	grdObj.UserId = strUserId;

	var grdInfoData = new Object();
	grdInfoData.grdData = grdObj;
	grdInfoData.requestType = "getSubTab"

	ajaxAsync('/webPage/sr/SRAccept', grdInfoData, 'json', successGetSubTab);

	//그리드 row 선택시 변경관리종료탭으로 변경
	if(!$("ul.tabs li#tab7").hasClass("on")) {
		$(".tab_content").hide();
		var activeTab = $("ul.tabs li#tab7").attr("rel");
		
		//tab메뉴 클릭에 따라 색상 변경
		$("ul.tabs li").removeClass('on');
		$("ul.tabs li#tab7").addClass("on");
		
		$("#" + activeTab).fadeIn();
		changeTabMenu();
	}
}

function successGetSubTab(data) {
	var tmpSubTab = data;

	if(tmpSubTab.indexOf("41") >= 0) {
		visibleObj('on', "tab1");
	} else {
		visibleObj('off', "tab1");
	}
	if(tmpSubTab.indexOf("42") >= 0) {
		visibleObj('on', "tab2");
	} else {
		visibleObj('off', "tab2");
	}
	if(tmpSubTab.indexOf("43") >= 0) {
		visibleObj('on', "tab3");
	} else {
		visibleObj('off', "tab3");
	} 
	if(tmpSubTab.indexOf("44") >= 0) {
		visibleObj('on', "tab4");
	} else {
		visibleObj('off', "tab4");
	}
	if(tmpSubTab.indexOf("45") >= 0) {
		visibleObj('on', "tab5");
	} else {
		visibleObj('off', "tab5");
	}
	if(tmpSubTab.indexOf("46") >= 0) {
		visibleObj('on', "tab6");
	} else {
		visibleObj('off', "tab6");
	}
	if(tmpSubTab.indexOf("49") >= 0) {
		visibleObj('on', "tab7");
	} else {
		visibleObj('off', "tab7");
	}
	if(tmpSubTab.indexOf("47") >= 0) {
		visibleObj('on', "tab8");
	} else {
		visibleObj('off', "tab8");
	}

	$("#srTab").fadeTo(0, 1);
	$('#srTab').css('pointer-events', 'visible');	
	changeTabMenu();
}

function visibleObj(state, tab) {
	if(state == 'on') {
		$('li[id^='+tab+']').fadeTo(0, 1);
		$('li[id^='+tab+']').css('pointer-events', 'visible');
	} else {
		$('li[id^='+tab+']').fadeTo(0, 0.5);
		$('li[id^='+tab+']').css('pointer-events', 'none');	
	}
}

//tabnavi_click
function changeTabMenu() {
	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	
	if(document.getElementById("tab1").className == "on") { //RFC접수
		if(grdLst.getList('selected').length <= 0) {
			tab1.screenInit("M");
			return;
		}
		if (tab1.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab1.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab1.screenInit("M");
		tab1.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab1.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab1.strSubStatus = grdLst.getList('selected')[0].cc_substatus;		
		tab1.strUserId = strUserId;
		tab1.strReqCd = "41";
		tab1.rfcRecvCall();
	} else if(document.getElementById("tab2").className == "on") { //개발계획서
		if (grdLst.getList('selected').length <= 0) {
			tab2.screenInit("M");
			return;
		}
		
		if (tab2.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab2.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab2.screenInit("M");
		tab2.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab2.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab2.strSubStatus = grdLst.getList('selected')[0].cc_substatus;
		tab2.strMainStatus = grdLst.getList('selected')[0].cc_mainstatus;
		tab2.strUserId = strUserId;
		tab2.strReqCd = "42";
		tab2.devPlanCall();
	} else if(document.getElementById("tab3").className == "on") { //단위테스트
		tab3.reSearch();
		if (grdLst.getList('selected').length <= 0) {
			tab3.screenInit("M");
			return;
		}
		
		if (tab3.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab3.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab3.screenInit("M");
		tab3.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab3.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab3.strSubStatus = grdLst.getList('selected')[0].cc_substatus;
		tab3.strMainStatus = grdLst.getList('selected')[0].cc_mainstatus;
		tab3.strUserId = strUserId; 
		tab3.strReqCd = "43";
		tab3.unitTestCall();
	} else if(document.getElementById("tab5").className == "on") { //대상프로그램
		if (grdLst.getList('selected').length <= 0) {
			tab5.screenInit("M");
			return;
		}
		if (tab5.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab5.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab5.screenInit("M");
		tab5.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab5.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab5.strUserId = strUserId; 
		tab5.docListCall();
	} else if(document.getElementById("tab6").className == "on") { //대상산출물
		if (grdLst.getList('selected').length <= 0) {
			tab6.screenInit("M");
			return;
		}
		
		if (tab6.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab6.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab6.screenInit("M");
		tab6.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab6.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab6.strUserId = strUserId;
		tab6.docListCall();
	} else if (document.getElementById("tab4").className == "on") {  //통합테스트
		if (grdLst.getList('selected').length <= 0) {
			tab4.screenInit('M');		
			return;
		}
		if (tab4.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab4.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) {
			return;
		}
		tab4.screenInit('M');					
		tab4.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab4.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab4.strSubStatus = grdLst.getList('selected')[0].cc_substatus;
		tab4.strUserId = strUserId; 
		tab4.strReqCd = "44";
		tab4.testcntCall();					
	} else if (document.getElementById("tab8").className == "on") {	//작업계획서
		tab8.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab8.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab8.strUserId = strUserId;
		tab8.popsw = false;
		tab8.strReqCd = "47";
		tab8.$('#txtISRID').val(grdLst.getList('selected')[0].isridtitle);
		tab8.$('#txtDOCNO').val(grdLst.getList('selected')[0].cc_docno);
		tab8.$('#txtREQDT').val(grdLst.getList('selected')[0].reqdate);
		tab8.$('#txtREQDEPT').val(grdLst.getList('selected')[0].reqdept);
		tab8.$('#txtEDITOR').val(grdLst.getList('selected')[0].requser);
		tab8.$('#txtSTATUS').val(grdLst.getList('selected')[0].reqsta1);
		tab8.$('#txtPROSTATUS').val(grdLst.getList('selected')[0].reqsta2);
		tab8.jobCall();
	} else if (document.getElementById("tab7").className == "on") {	//변경관리종료
		if (grdLst.getList('selected').length <= 0) {
			tab7.screenInit('M');
			return;
		}
		if (tab7.strIsrId == grdLst.getList('selected')[0].cc_isrid &&
			tab7.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) return;
		tab7.screenInit('M');	
		tab7.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab7.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab7.strSubStatus = grdLst.getList('selected')[0].cc_substatus;
		tab7.strUserId = strUserId;
		tab7.popsw = false;
		tab7.strReqCd = "49";
		tab7.chgEndCall();
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

function confCall(GbnCd) {

	var etcQryCd = [];
	tab7.confirm_dp = [];
	tab7.confirmInfoData = null;

	etcQryCd = tab7.strReqCd.split(",");

	confirmInfoData = {		
		UserId : tab7.strUserId,
		ReqCd  : tab7.strReqCd,
		PrjNo : tab7.strIsrId+tab7.strIsrSub,
		SysCd  : "99999",
		RsrcCd : [],
		QryCd : etcQryCd,
		EmgSw : "N",
		PgmType : "",
		deployCd : ""
	}

	if(GbnCd == "Y") {
			approvalModal.open({
	        width: 900,
	        height: 550,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ApprovalModal.jsp",
	            param: "callBack=modalCallBack"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	if(confirmData.length > 0){

	            		tab7.reqQuest(confirmData, this.state);
	            	}
	                mask.close();
	                tab7.ingSw = false;
	            }
	        }
		});
	} else {
		confirmInfoData = {	
			UserID : tab7.strUserId,
			ReqCD  : tab7.strReqCd,
			PrjNo  : tab7.strIsrId+tab7.strIsrSub,
			SysCd  : "99999",
			Rsrccd : [],
			QryCd : [],
			EmgSw : "N",
			PgmType : etcQryCd,
			deployCd : ""
		}
		var tmpData = {
			confirmInfoData: 	confirmInfoData,
			requestType: 	'Confirm_Info'
		}
		ajaxAsync('/webPage/request/ConfirmServlet', tmpData, 'json', tab7.successConfirm_Info);

		etcQryCd = null;
	}
}