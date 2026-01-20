/** SR정보 (eCmc0900.mxml)
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 */

var strWorkD	= '';
var strUserId 	= '';
var strIsrId 	= '';
var strAcptNo 	= '';
var reqCd 		= '';
var userId		= '';
var prjData		= [];
var srData 		= new Object(); // to Tab
var winSw		= true;

var f = document.getReqData;
strUserId 	= f.userId.value;
userId		= f.userId.value;
strIsrId	= f.isrId.value;
strAcptNo 	= f.acptno.value;

var tab1 	= null;
var tab2 	= null;
var tab3 	= null;
var tab4 	= null;
var tab5 	= null;
var tab6 	= null;
var tab7 	= null;
var tab8 	= null;

var load1Sw	= false;
var load2Sw	= false;
var load3Sw	= false;
var load4Sw	= false;
var load5Sw	= false;
var load6Sw	= false;
var load7Sw	= false;
var load8Sw	= false;

$(document).ready(function() {
	$('#tabSRRegister').width($('#tabSRRegister').width()+10);
	$("#tabDevPlan").width($('#tabDevPlan').width()+10);
	$('#tabReqHistory').width($('tabReqHistory').width()+10);
	$('#tabPrgList').width($('#tabPrgList').width()+10);
	$('#tabUnitTest').width($('#tabUnitTest').width()+10);
	$('#tabMonitoring').width($('#tabDevCheck').width()+10);
	$('#tabDevCheck').width($('#tabDevCheck').width()+10);
	$('#tabSRComplete').width($('#tabSRComplete').width()+10);
	
	//막지마세요!
	reqCd = '99';

	if (strIsrId == '' || strIsrId == null) {
		dialog.alert('SR-ID를 선택하지 않았습니다.');
		return;
	}
	
	if (strUserId == '' || strUserId == null) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	
	screenInit();
	
//	_promise(10,loadiFrame())
//	.then(function() {
//		return _promise(10,loadiFrameEnd(),clickTabMenu());
//	});
	
	loadiFrame();
	checkFrameLoad(['frmReqHistory','frmSRRegister','frmPrgList','frmDevPlan',
					'frmUnitTest','frmDevCheck','frmMonitoring','frmSRComplete'], loadiFrameEnd);

	
	/*$("#tabSRComplete").show();
	clickTabMenu();*/
	//clickTabMenu();
});

function loadiFrame() {
	document.getElementById('frmSRRegister').onload = function() {
		load1Sw = true;
		tab1 = $('#frmSRRegister').get(0).contentWindow;
	}
	
	document.getElementById('frmDevPlan').onload = function() {
		load2Sw = true;
		tab2 = $('#frmDevPlan').get(0).contentWindow;
	}
	
	document.getElementById('frmReqHistory').onload = function() {
		load3Sw = true;
		tab3 = $('#frmReqHistory').get(0).contentWindow;
	}
	
	document.getElementById('frmPrgList').onload = function() {
		load4Sw = true;
		tab4 = $('#frmPrgList').get(0).contentWindow;
	}
	
	document.getElementById('frmUnitTest').onload = function() {
		load5Sw = true;
		tab5 = $('#frmUnitTest').get(0).contentWindow;
	}
	
	document.getElementById('frmDevCheck').onload = function() {
		load6Sw = true;
		tab6 = $('#frmDevCheck').get(0).contentWindow;
	}
	
	document.getElementById('frmMonitoring').onload = function() {
		load7Sw = true;
		tab7 = $('#frmMonitoring').get(0).contentWindow;
	}
	
	document.getElementById('frmSRComplete').onload = function() {
		load8Sw = true;
		tab8 = $('#frmSRComplete').get(0).contentWindow;
	}
}

function loadiFrameEnd() {
	var etcData = new Object();
	etcData.reqcd = reqCd;
	etcData.qrygbn = reqCd;
	etcData.secuyn = 'N';
	etcData.srid = strIsrId;
 	
	var data =  new Object();
	data = {
		etcData : etcData,
		requestType	: 'getPrjList'
	}
	ajaxAsync('/webPage/common/PrjInfoServlet', data, 'json',successGetPrjList);
}

function successGetPrjList(data) {
	prjData = data;
	if(prjData.length > 0) {
		srData.strStatus = prjData[0].cc_status;
		srData.strIsrId = prjData[0].cc_srid;
		$('#txtSRID').val(strIsrId);
		$('#txtSta').val(prjData[0].status);
		$('#txtSRTitle').val(prjData[0].cc_reqtitle);
		isrID_click(prjData[0]);
	}
	clickTabMenu();
}

function getQry_click() {
	isrID_click(prjData[0]);
}

function cmdQry_click() {
	isrID_click(prjData[0]);
}

function isrID_click(data) {
	if(data == null || data == undefined) return;
	
	var tabIdx = 0;
	if(data.isrproc.indexOf('69') >= 0) {
		visibleObj('on', 'tab8');
		if(strAcptNo != null && strAcptNo != '' && strAcptNo != undefined) {
			if(strAcptNo.substr(4,2) == '69') {
				tabIdx = 7;
			}
		}
		
		tabIdx = 7;
	}
	
	if(data.isrproc.indexOf('63') >= 0) {
		visibleObj('on', 'tab7');
		if(tabIdx == 0) tabIdx = 6;
	}
	
	if(data.isrproc.indexOf('44') >= 0 || data.isrproc.indexOf('54') >= 0 || data.isrproc.indexOf('55') >= 0) {
		visibleObj('on', 'tab6');
		if(tabIdx == 0) tabIdx = 5;
	}
	
	if(data.isrproc.indexOf('43') >= 0) {
		visibleObj('on', 'tab5');
		if(tabIdx == 0) tabIdx = 4;
	}
	
	if(data.isrproc.indexOf('42') >= 0) {
		visibleObj('on', 'tab2');
		if(tabIdx == 0) tabIdx = 1;
	}
	
	if(data.isrproc.indexOf('92') >= 0) {
		visibleObj('on', 'tab3');
	}
	
	if(data.isrproc.indexOf('93') >= 0) {
		visibleObj('on', 'tab4');
	}
	
	if(data.isrproc.indexOf('41') >= 0) {
		visibleObj('on', 'tab1');
		if(strAcptNo != null && strAcptNo != '' && strAcptNo != undefined) {
			if(strAcptNo.substr(4,2) == '41') {
				tabIdx = 0;
			}
		}
	}
	
	tabIdx++;
	console.log('tabIdx==', tabIdx);
	//$('#tab'+tabIdx+'Li').click();
	//changeTabMenu();
	
	var activeTab = $('#tab' + tabIdx).attr("rel");
	$('#tab' + tabIdx).addClass("on");
	$("#" + activeTab).fadeIn();
	changeTabMenu();
}

function screenInit() {
	visibleObj('off', 'tab1');
	visibleObj('off', 'tab2');
	visibleObj('off', 'tab3');
	visibleObj('off', 'tab4');
	visibleObj('off', 'tab5');
	visibleObj('off', 'tab6');
	visibleObj('off', 'tab7');
	visibleObj('off', 'tab8');
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

// 탭메뉴 클릭 이벤트
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

// 탭메뉴 변경 이벤트 (tabnavi_click)
function changeTabMenu() {
	if(document.getElementById("tab1").className == "on") {				// SR등록
		reqCd = 'XX';
		
		srData = new Object();
		srData.strEditor = prjData[0].cc_createuser;
		srData.strStatus = prjData[0].cc_status;
		srData.strIsrId = prjData[0].cc_srid;
		srData.strAcptNo = strAcptNo;
		
		tab1 = $('#frmSRRegister').get(0).contentWindow;
		//tab1.screenInit('M');
		tab1.grdPrj_click(prjData[0].cc_srid);
		tab1.gyulChk();
		
	}else if(document.getElementById("tab2").className == "on") {		// 개발계획/실적등록
		reqCd = '42';
		
		tab2 = $('#frmDevPlan').get(0).contentWindow;
		
		tab2.strIsrId = strIsrId;
		tab2.strStatus = prjData[0].cc_status;
		tab2.screenInit('M');
		if(strIsrId != '' && strIsrId != null && strIsrId != undefined) {
			tab2.initDevPlan(); //devPlanCall();
		}
		
	}else if(document.getElementById("tab3").className == "on") {		// 변경요청이력
		tab3 = $('#frmReqHistory').get(0).contentWindow;
		
		//if(tab3.strIsrId == strIsrId) return;
		
		tab3.strIsrId = strIsrId;
		tab3.screenInit();
		if(strIsrId != '' && strIsrId != null && strIsrId != undefined) {
			tab3.docListCall();
		}
		
	}else if(document.getElementById("tab4").className == "on") {		// 프로그램목록
		tab4 = $('#frmPrgList').get(0).contentWindow;
		
		//if(tab4.strIsrId == strIsrId) return;
		
		tab4.strIsrId = strIsrId;
		tab4.screenInit();
		if(strIsrId != '' && strIsrId != null && strIsrId != undefined) {
			tab4.docListCall();
		}
		
	}else if(document.getElementById("tab5").className == "on") {		// 단위테스트
		reqCd = '43';
		
		tab5 = $('#frmUnitTest').get(0).contentWindow;
		//if(tab5.strIsrId == strIsrId) return;
		
		srData = new Object();
		srData.strIsrId = strIsrId;
		srData.strAcptNo = strAcptNo;
		srData.strStatus = prjData[0].cc_status;
		
		tab5.screenInit('M');
		if(strIsrId != '' && strIsrId != null && strIsrId != undefined) {
			tab5.unitTestCall();
		}
		
	}else if(document.getElementById("tab6").className == "on") {		// 개발검수
		reqCd = 'XX';
		
		tab6 = $('#frmDevCheck').get(0).contentWindow;
		//if(tab6.strIsrId == strIsrId) return;
		
		srData = new Object();
		srData.strIsrId = strIsrId;
		srData.strAcptNo = strAcptNo;
		
		tab6.screenInit('M');
		if(strIsrId != '' && strIsrId != null && strIsrId != undefined) {
			tab6.sysTestCall();
		}
		
	}else if(document.getElementById("tab7").className == "on") {		// 모니터링체크
		reqCd = 'XX';
		
		tab7 = $('#frmMonitoring').get(0).contentWindow;
		//if(tab7.strIsrId == strIsrId) return;
		
		tab7.strIsrId = strIsrId;
		tab7.reqCd = reqCd;
		
		tab7.screenInit('M');
		if(strIsrId != '' && strIsrId != null && strIsrId != undefined) {
			tab7.initCboSystem();
		}
		
	}else if(document.getElementById("tab8").className == "on") {		// SR완료
		reqCd = '69';
		
		tab8 = $('#frmSRComplete').get(0).contentWindow;
		//if(tab8.strIsrId == strIsrId) return;
		
		if(!load8Sw) {
			setTimeout(function(){
				changeTabMenu();
			}, 200);
			return;
		}
		tab8.strIsrId = strIsrId;
		tab8.strStatus = prjData[0].cc_status;
		tab8.createViewGrid();
		tab8.screenInit('M');
		tab8.strIsrTitle = prjData[0].cc_reqtitle;
		tab8.strEditor = prjData[0].cc_lastupuser;
		tab8.strQryGbn = '00';
		tab8.srendInfoCall();
	}
}

function popClose() {
	try {
		if (window.opener.getRequestList != undefined){
			window.opener.getRequestList();
		}
		window.open("about:blank","_self").close();
	}catch(e) {
	}
}