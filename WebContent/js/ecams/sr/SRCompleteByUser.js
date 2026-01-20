/** ISR종료 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-05-07
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
varstrIsrSub = "";
var grdLst_dp = [];
var cboQryGbn_dp = [];
var cboPart_dp = [];


var tab1 = null;
var tab2 = null;
var tab3 = null;
var tab4 = null;
var tab5 = null;

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

$('[data-ax5select="cboPart"]').ax5select({
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
	$('#btnChgInfo').bind('click', function() {
		popOpen("CHG");
	});
	
	// 테스트관리정보
	$('#btnTestInfo').bind('click', function() {
		popOpen("TEST");
	});

	strReqCd = "38";
	strUserId = userId;

	if (strUserId == "" || strUserId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}
	
	tab1 = $('#frmSRRegister').get(0).contentWindow; // ISR요청정보
	tab2 = $('#frmSRAccept').get(0).contentWindow; // 	ISR접수
	tab3 = $('#frmRFCRegister').get(0).contentWindow; //RFC발행
	tab4 = $('#frmSRComplete').get(0).contentWindow; // ISR종료
	tab5 = $('#frmSRCompleteByUser').get(0).contentWindow; // 요청자종료
	tabBase = $('#frmBaseISRInfo').get(0).contentWindow;

	$('#tabSRRegister').width($('#tabSRCompleteByUser').width()+10);
	$('#tabSRAccept').width($('#tabSRCompleteByUser').width()+10);
	$('#tabRFCRegister').width($('#tabSRCompleteByUser').width()+10);
	$('#tabSRComplete').width($('#tabSRCompleteByUser').width()+10);
	$('#tabSRCompleteByUser').show();
	
	clickTabMenu();

	document.getElementById('frmBaseISRInfo').onload = function() {
		cboSet();
	}
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
		key : "CC_ISRTITLE",
		label : "요청제목",
		width : 200,
		align : 'center'
	}, {
		key : "CC_CREATDT",
		label : "요청등록일",
		width : 150,
		align : 'center'
	}, {
		key : "CC_REQENDDT",
		label : "완료요청일",
		width : 150,
		align : 'center'
	}, {
		key : "REQGRADE",
		label : "요청등급",
		width : 100,
		align : 'center'
	}, {
		key : "CC_DOCNO",
		label : "문서번호",
		width : 150,
		align : 'center'
	}, {
		key : "BUDGETPRICE",
		label : "예산금액",
		width : 150,
		align : 'center'
	}, {
		key : "TESTERYN",
		label : "테스트참여",
		width : 250,
		align : 'center'
	}, ]
});

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

	strIsrSub = "";
	document.getElementById('frmSRCompleteByUser').onload = function() {
		tab5.screenInit('M');
	}
}

function cboSet() {
	var tmpObj = new Object();
	tmpObj.cm_micode = "00";
	tmpObj.cm_codename = "전체";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = new Object();
	tmpObj.cm_micode = "01";
	tmpObj.cm_codename = "요청종료대상";
	cboQryGbn_dp.push(tmpObj);

	tmpObj = new Object();	
	tmpObj.cm_micode = "02";
	tmpObj.cm_codename = "ISR종료건[일부]";
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

	var tmpInfo = new Object();
	tmpInfo.tmpObjData = tmpObj;
	tmpInfo.requestType = "getISRList"

	ajaxAsync('/webPage/srcommon/SRRegister', tmpInfo, 'json', successGetISRList);

	tmpObj = null;
}

function successGetISRList(data) {
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
	grdLst_dp = [];
	grdLst_dp = data;
	
	tabBase.$('#txtISRId').val(grdLst.getList('selected')[0].cc_isrid);
	tabBase.$('#txtReqDT').val(grdLst.getList('selected')[0].CC_CREATDT);
	tabBase.$('#txtTitle').val(grdLst.getList('selected')[0].CC_ISRTITLE);
	tabBase.$('#txtReqDept').val(grdLst.getList('selected')[0].CM_DEPTNAME);
	tabBase.$('#txtEditor').val(grdLst.getList('selected')[0].CM_USERNAME);
	tabBase.$('#txtComEdDt').val(grdLst.getList('selected')[0].CC_REQENDDT);

	tabBase.$('#txtStatus').val("");
	tabBase.$('#txtProStatus').val("");

	$('#btnTestInfo').attr('disabled', true);
	$('#btnReqInfo').attr('disabled', true);

	$("#srTab").fadeTo(0, 1);
	$('#srTab').css('pointer-events', 'visible');		
	tab1.strIsrId = "";
	tab1.strIsrSub = "";
	tab2.strIsrId = "";
	tab2.strIsrSub = "";
	tab3.strIsrId = "";
	tab3.strIsrSub = "";
	tab4.strIsrId = "";
	tab4.strIsrSub = "";
	tab4.strSubStatus = "";
	tab5.strIsrId = "";

	var subData = {
		IsrId : grdLst.getList('selected')[0].cc_isrid ,
		UserId : strUserId,
		requestType : 'getPrjSub',
		}	

	ajaxAsync('/webPage/srcommon/SRRegister', subData, 'json',
			successGetPrjSub);		

	//그리드 row 선택시 요청자종료
	if(!$("ul.tabs li#tab5").hasClass("on")) {
		$(".tab_content").hide();
		var activeTab = $("ul.tabs li#tab5").attr("rel");
		
		//tab메뉴 클릭에 따라 색상 변경
		$("ul.tabs li").removeClass('on');
		$("ul.tabs li#tab5").addClass("on");
		
		$("#" + activeTab).fadeIn();
		changeTabMenu();
	}
}

function successGetPrjSub(data) {
	cboPart_dp = data;

	$('[data-ax5select="cboPart"]').ax5select({
		options: injectCboDataToArr(cboPart_dp, 'cc_isrsub' , 'recvdept')
	});		
	
	if (cboPart_dp.length > 0) {
		if (cboPart_dp.length == 2) { 
			$('[data-ax5select="cboPart"]').ax5select("setValue", "01", true);
		} else {
			$('[data-ax5select="cboPart"]').ax5select("setValue", "00", true);
		}
		cboPart_click();
	}

	var grdObj = new Object();
	grdObj.IsrId = grdLst.getList('selected')[0].cc_isrid;
	grdObj.IsrSub = grdLst.getList('selected')[0].cc_isrsub;
	grdObj.ReqCd = "REQ"
	grdObj.UserId = strUserId;

	var grdInfoData = new Object();
	grdInfoData.grdData = grdObj;
	grdInfoData.requestType = "getSubTab"

	ajaxAsync('/webPage/sr/SRAccept', grdInfoData, 'json', successGetSubTab);
}

function successGetSubTab(data) {
	var tmpSubTab = data;

	if (getSelectedIndex('cboPart') == 0) {
		if (grdLst.getList('selected')[0].cc_status == "9") {
			visibleObj('on', "tab4");
			visibleObj('on', "tab1");
		} else {
			visibleObj('on', "tab1");
		}
	} else {
		if(getSelectedVal('cboPart').maintab.indexOf("C") >= 0) {
			$('#btnReqInfo').attr('disabled', false);
		}
		if(getSelectedVal('cboPart').maintab.indexOf("T") >= 0) {
			$('#btnTestInfo').attr('disabled', false);
		}		
		if(tmpSubTab.indexOf("31") >= 0) {
			visibleObj('on', "tab1");
		} else {
			visibleObj('off', "tab1");
		}
		if(tmpSubTab.indexOf("32") >= 0) {
			visibleObj('on', "tab2");
		} else {
			visibleObj('off', "tab2");
		}
		if(tmpSubTab.indexOf("33") >= 0) {
			visibleObj('on', "tab3");
		} else {
			visibleObj('off', "tab3");
		} 
		if(tmpSubTab.indexOf("39") >= 0) {
			visibleObj('on', "tab4");
		} else {
			visibleObj('off', "tab4");
		}
		if(tmpSubTab.indexOf("38") >= 0) {
			visibleObj('on', "tab5");
		} else {
			visibleObj('off', "tab5");
		}		
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

function cboPart_click() {
	$('#btnTestInfo').attr('disabled', true);
	$('#btnReqInfo').attr('disabled', true);	
	
	visibleObj('off', "tab1");
	visibleObj('off', "tab2");
	visibleObj('off', "tab3");
	visibleObj('off', "tab4");
	visibleObj('off', "tab5");
	   
	tab1.strIsrId = "";
	
	tab2.strIsrId = "";
	tab2.strIsrSub = "";
	
	tab3.strIsrId = "";
	tab3.strIsrSub = "";
	
	tab4.strIsrId = "";
	tab4.strIsrSub = "";
	   
	tab5.strIsrId = "";

	tabBase.$('#txtStatus').val("");
	tabBase.$('#txtProStatus').val("");

	if (grdLst.getList('selected')[0].endok == "OK") {
		visibleObj('on', "tab5");
		if(!$("ul.tabs li#tab5").hasClass("on")) {
			$(".tab_content").hide();
			var activeTab = $("ul.tabs li#tab5").attr("rel");
			
			$("ul.tabs li").removeClass('on');
			$("ul.tabs li#tab5").addClass("on");
			
			$("#" + activeTab).fadeIn();
		}
	} else {
		if (getSelectedIndex('cboPart') < 0) { 
			return;
		}
	}
		
	if (getSelectedIndex('cboPart') == 0) {
		strIsrSub = "";			
	} else {
		strIsrSub = getSelectedVal('cboPart').cc_isrsub;
	}
}

//tabnavi_click
function changeTabMenu() {
	if(document.getElementById("tab1").className == "on") { //ISR요청정보
		if (grdLst.getList('selected').length <= 0) {
			tab1.screenInit("M");
			return;
		}
		if (tab1.strIsrId == grdLst.getList('selected')[0].cc_isrid) {
			return;   					
		}
		tab1.screenInit("M");
		tab1.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab1.strUserId = strUserId;
		tab1.strReqCd = "31";
		tab1.isrInfoCall();
	} else if(document.getElementById("tab2").className == "on") { //ISR접수
		if (grdLst.getList('selected').length <= 0) {
			tab2.screenInit("M");
			return;
		}
		if (strIsrSub == "" || strIsrSub == null) {
			tab2.screenInit("M");
			return; 
		}  			
		if (tab2.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab2.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) { 
			return;
		}
		tab2.screenInit("M");
		tab2.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab2.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab2.strUserId = strUserId;
		tab2.strReqCd = "32";
		tab2.isrInfoCall();
	} else if(document.getElementById("tab3").className == "on") { //RFC발행
		if (grdLst.getList('selected').length <= 0) {
			tab3.screenInit("M");
			return;
		}
		if (strIsrSub == "" || strIsrSub == null) {
			tab3.screenInit("M");
			return; 
		}  	
		if (tab3.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab3.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) { 
			return;
		}
		
		tab3.screenInit("M");    
	 	tab3.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab3.strIsrSub = grdLst.getList('selected')[0].cc_isrsub;
		tab3.strUserId = strUserId;
		tab3.popSw = false;
		tab3.strReqCd = "33";
		tab3.rfcCall();
	} else if(document.getElementById("tab4").className == "on") { //ISR종료
		if (grdLst.getList('selected').length <= 0) {
			tab4.screenInit("M");
			return;
		}
		if (strIsrSub == "" || strIsrSub == null) {
			tab4.screenInit("M");
			return; 
		}  	
		if (tab4.strIsrId == grdLst.getList('selected')[0].cc_isrid && tab4.strIsrSub == grdLst.getList('selected')[0].cc_isrsub) { 
			return;
		}
		tab4.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab4.strIsrSub = strIsrSub;
		tab4.screenInit('M');
		tab4.strUserId = strUserId;
		tab4.popSw = false;
	   	tab4.strReqCd = "39";
		tab4.reqEndCall();
	} else if (document.getElementById("tab5").className == "on") {  //요청종료
		if (grdLst.getList('selected').length <= 0) {
			tab5.screenInit("M");
			return;
		}
		if (tab5.strIsrId == grdLst.getList('selected')[0].cc_isrid) { 
			return;
		}
		tab5.screenInit("M");    
	 	tab5.strIsrId = grdLst.getList('selected')[0].cc_isrid;
		tab5.strUserId = strUserId;
		tab5.strReqCd = "38";	
		tab5.isrEndCall();
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