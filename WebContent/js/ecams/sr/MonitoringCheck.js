/**
 * 모니터링체크(eCmc0600.mxml) 화면 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 */

var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;
var reqCd	 		= window.top.reqCd;

var srData 			= new Object();
var	strIsrId		= '';

var cboQryGbnData	= [
	{cm_codename : "전체",		cm_micode : "00", cm_dateyn: "Y"},
	{cm_codename : "모니터링대상",	cm_micode : "01", cm_dateyn: "N"}
];

var tab0 			= null;
var tab1 			= null;
var tab2 			= null;
var tab3 			= null;
var tab4 			= null;
var tab5 			= null;
var tab6 			= null;

var load0Sw			= false;
var load1Sw			= false;
var load2Sw			= false;
var load3Sw			= false;
var load4Sw			= false;
var load5Sw			= false;
var load6Sw			= false;

$(document).ready(function(){
	if (userId == '' || userId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}
	
	//tab메뉴
	$('#tabSRRegister').width($('#tabSRRegister').width()+10);
	$('#tabPrgList').width($('#tabPrgList').width()+10);
	$('#tabDevPlan').width($('#tabDevPlan').width()+10);
	$('#tabUnitTest').width($('#tabUnitTest').width()+10);
	$('#tabDevCheck').width($('#tabDevCheck').width()+10);
	$('#tabMonitoring').width($('#tabMonitoring').width()+10);
	
//	_promise(10,loadiFrame())
//	.then(function() {
//		return _promise(10,loadiFrameEnd());
//	});
	
	loadiFrame();
	checkFrameLoad(['frmPrjList','frmSRRegister','frmPrgList','frmDevPlan',
					'frmUnitTest','frmDevCheck','frmMonitoring'], loadiFrameEnd);
	
	$("#tabMonitoring").show();
	clickTabMenu();
	
});


function loadiFrame() {
	console.log('1: ' + load0Sw);
	document.getElementById('frmPrjList').onload = function() {
		load0Sw = true;
		tab0 = $('#frmPrjList').get(0).contentWindow;
		console.log('1-1: ' + load0Sw);
	}
	
	document.getElementById('frmSRRegister').onload = function() {
		load1Sw = true;
		tab1 = $('#frmSRRegister').get(0).contentWindow;
	}
	
	document.getElementById('frmPrgList').onload = function() {
		load2Sw = true;
		tab2 = $('#frmPrgList').get(0).contentWindow;
	}
	
	document.getElementById('frmDevPlan').onload = function() {
		load3Sw = true;
		tab3 = $('#frmDevPlan').get(0).contentWindow;
	}
	
	document.getElementById('frmUnitTest').onload = function() {
		load4Sw = true;
		tab4 = $('#frmUnitTest').get(0).contentWindow;
	}
	
	document.getElementById('frmDevCheck').onload = function() {
		load5Sw = true;
		tab5 = $('#frmDevCheck').get(0).contentWindow;
	}
	
	document.getElementById('frmMonitoring').onload = function() {
		load6Sw = true;
		tab6 = $('#frmMonitoring').get(0).contentWindow;
	}
}

function loadiFrameEnd() {
	tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.screenInit();
		tab0.cboQryGbn_click();
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

//탭메뉴 변경 이벤트 (tabnavi_click)
function changeTabMenu() {
	if(document.getElementById("tab1").className == "on") {				// SR등록
		reqCd = 'XX';
		
		tab0 = $('#frmPrjList').get(0).contentWindow;
		tab1 = $('#frmSRRegister').get(0).contentWindow;
		
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab1.screenInit('M');
			return;
		}
		
		console.log('[MoniteringCheck.js] tab1 strIsrId ==>', tab1.srData.strIsrId, srData.strIsrId);
		//if(tab1.srData.strIsrId == srData.strIsrId) return;
		
		tab1.srData.strStatus = tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_status;
		tab1.screenInit('M');
		tab1.grdPrj_click(srData.strIsrId);
		
	}else if(document.getElementById("tab2").className == "on") {		// 프로그램목록
		tab2 = $('#frmPrgList').get(0).contentWindow;
		
		console.log('[MoniteringCheck.js] tab2 strIsrId ==>', tab2.srData.strIsrId, srData.strIsrId);
		//if(tab2.srData.strIsrId == srData.strIsrId) return;
		
		tab2.srData.strIsrId = srData.strIsrId;
		tab2.screenInit();
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab2.docListCall();
		}
		
	}else if(document.getElementById("tab3").className == "on") {		// 개발계획/실적등록
		reqCd = '42';
		
		tab0 = $('#frmPrjList').get(0).contentWindow;
		tab3 = $('#frmDevPlan').get(0).contentWindow;
		
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab3.screenInit('M');
			return;
		}
		
		console.log('[MoniteringCheck.js] tab3 strIsrId ==>', tab3.srData.strIsrId, srData.strIsrId);
		//if(tab3.srData.strIsrId == srData.strIsrId) return;
		
		tab3.srData.strIsrId = srData.strIsrId;
		tab3.srData.strStatus = tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_status;
		tab3.screenInit('M');
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab3.initDevPlan();
		}
		
	}else if(document.getElementById("tab4").className == "on") {		// 단위테스트
		reqCd = '43';
		
		tab0 = $('#frmPrjList').get(0).contentWindow;
		tab4 = $('#frmUnitTest').get(0).contentWindow;
		
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab4.screenInit('M');
			return;
		}
		
		console.log('[MoniteringCheck.js] tab4 strIsrId ==>', tab4.srData.strIsrId, srData.strIsrId);
		//if(tab4.srData.strIsrId == srData.strIsrId) return;
		
		tab4.srData.strIsrId = srData.strIsrId;
		tab4.screenInit('M');
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab4.unitTestCall();
		}
	}else if(document.getElementById("tab5").className == "on") {		// 개발검수
		reqCd = 'XX';
		
		tab0 = $('#frmPrjList').get(0).contentWindow;
		tab5 = $('#frmDevCheck').get(0).contentWindow;
		
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab5.screenInit('M');
			return;
		}
		
		console.log('[MoniteringCheck.js] tab5 strIsrId ==>', tab5.srData, srData);
		//if(tab4.srData.strIsrId == srData.strIsrId) return;
		
		tab5.srData.strIsrId = srData.strIsrId;
		tab5.screenInit('M');
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab5.sysTestCall();
		}
	}else if(document.getElementById("tab6").className == "on") {		// 모니터링체크
		tab0 = $('#frmPrjList').get(0).contentWindow;
		tab6 = $('#frmMonitoring').get(0).contentWindow;
		
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab6.screenInit('M');
			return;
		}
		
		//if(tab6.srData.strIsrId == srData.strIsrId) return;
		
		tab6.strIsrId = srData.strIsrId;
		tab6.screenInit('M');
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab6.initCboSystem();
		}
	}
}

//PrjListTab에서 호출
function subScreenInit() {
	if(load1Sw) {
		tab1 = $('#frmSRRegister').get(0).contentWindow;
		if(tab1 != null && tab1 != undefined) {
			tab1.srData.strIsrId = '';
			tab1.screenInit('M');
		}
	}
	
	if(load3Sw) {
		tab3 = $('#frmDevPlan').get(0).contentWindow;
		if(tab3 != null && tab3 != undefined) {
			tab3.strIsrId = '';
			tab3.screenInit('M');
		}
	}
	
	if(load4Sw) {
		tab4 = $('#frmUnitTest').get(0).contentWindow;
		if(tab4 != null && tab4 != undefined) {
			tab4.srData.strIsrId = '';
			tab4.screenInit('M');
		}
	}
	
	if(load5Sw) {
		tab5 = $('#frmDevCheck').get(0).contentWindow;
		if(tab5 != null && tab5 != undefined) {
			tab5.srData.strIsrId = '';
			tab5.screenInit('M');
		}
	}
	
	if(load6Sw) {
		tab6 = $('#frmMonitoring').get(0).contentWindow;
		if(tab6 != null && tab6 != undefined) {
			tab6.strIsrId = '';
			tab6.screenInit('M');
		}
	}
}


//PrjListTab에서 호출
function isrID_click(data) {
	console.log('[MoniteringCheck.js] isrID_click ==>', data);
	srData = new Object();
	visibleObj('off', 'tab1');
	visibleObj('off', 'tab3');
	visibleObj('off', 'tab4');
	visibleObj('off', 'tab5');
	
	if(data == null || data == undefined) return;
	
	srData.strIsrId = data.cc_srid;
	strIsrId = data.cc_srid;
	
	if(data.isrproc.indexOf('41') >= 0) visibleObj('on', 'tab1');
	if(data.isrproc.indexOf('42') >= 0) visibleObj('on', 'tab3');
	if(data.isrproc.indexOf('43') >= 0) visibleObj('on', 'tab4');
	if(data.isrproc.indexOf('54') >= 0) visibleObj('on', 'tab5');
	if(data.isrproc.indexOf('63') >= 0) visibleObj('on', 'tab6');
	if(data.isrproc.indexOf('93') >= 0) visibleObj('on', 'tab2');
	
	setTimeout(function() {			
		changeTabMenu();
	}, 10);
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
