/** 단위테스트(eCmc0300.mxml) 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 */

var userName 		= window.top.userName;
var userId 			= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName	= window.top.userDeptName;
var userDeptCd 		= window.top.userDeptCd;
var reqCd 			= window.top.reqCd;

var tab0 			= null;
var tab1 			= null;
var tab2 			= null;
var tab3 			= null;
var tab4 			= null;

var load1Sw			= false;
var load2Sw			= false;
var load3Sw			= false;
var load4Sw			= false;
var srData 			= new Object(); // to UnitTestTab

var strQryGbn 		= '';
var cboQryGbnData	= [
	{cm_codename : "전체",			cm_micode : "00", cm_dateyn: "Y"},
	{cm_codename : "단위테스트대상",	cm_micode : "01", cm_dateyn: "N"},
	{cm_codename : "개발검수요청대상",	cm_micode : "02", cm_dateyn: "N"}
]

$(document).ready(function() {
	if(reqCd.length > 2) strQryGbn = reqCd.substr(0,2);
	else strQryGbn = '01';
	
	reqCd = '43';

	if (userId == '' || userId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}
	
	$('#tabSRRegister').width($('#tabSRRegister').width()+10);
	$('#tabPrgList').width($('#tabPrgList').width()+10);
	$("#tabDevPlan").width($('#tabDevPlan').width()+10);
	$('#tabUnitTest').width($('#tabUnitTest').width()+10);
	
//	document.getElementById('frmPrjList').onload = function() {
//		tab0 = $('#frmPrjList').get(0).contentWindow;
//		if(tab0 != null && tab0 != undefined) {
//			tab0.screenInit();
//			tab0.cboQryGbn_click();
//		}
//	}
	
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
	
	checkFrameLoad(['frmPrjList','frmSRRegister','frmPrgList','frmDevPlan',
					'frmUnitTest'], frameReadyFunction);
	
	$("#tabUnitTest").show();
	clickTabMenu();
});

function frameReadyFunction() {
	tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.screenInit();
		tab0.cboQryGbn_click();
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
		
		tab0 = $('#frmPrjList').get(0).contentWindow;
		
		if(!load1Sw) {
			setTimeout(function() {
				document.getElementById('frmSRRegister').onload = function() {
					load1Sw = true;
				}
			}, 10);
		}
		
		tab1 = $('#frmSRRegister').get(0).contentWindow;
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab1.screenInit('M');
			return;
		}
		
		console.log('[UnitTest.js] tab1 strIsrId ==>', tab1.srData.strIsrId, srData.strIsrId);
		//if(tab1.srData.strIsrId == srData.strIsrId) return;
		
		tab1.srData.strStatus = tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_status;
		tab1.screenInit('M');
		tab1.grdPrj_click(srData.strIsrId);
		
	}else if(document.getElementById("tab2").className == "on") {		// 프로그램목록
		if(!load2Sw) {
			setTimeout(function() {
				document.getElementById('frmPrgList').onload = function() {
					load2Sw = true;
				}
			}, 10);
		}
		
		tab2 = $('#frmPrgList').get(0).contentWindow;
		
		console.log('[UnitTest.js] tab2 strIsrId ==>', tab2.srData.strIsrId, srData.strIsrId);
		//if(tab2.srData.strIsrId == srData.strIsrId) return;
		
		tab2.srData.strIsrId = srData.strIsrId;
		tab2.screenInit();
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab2.docListCall();
		}
		
	}else if(document.getElementById("tab3").className == "on") {		// 개발계획/실적등록
		reqCd = '42';
		
		tab0 = $('#frmPrjList').get(0).contentWindow;
		
		if(!load3Sw) {
			setTimeout(function() {
				document.getElementById('frmDevPlan').onload = function() {
					load3Sw = true;
				}
			}, 10);
		}
		
		tab3 = $('#frmDevPlan').get(0).contentWindow;
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab3.screenInit('M');
			return;
		}
		
		console.log('[UnitTest.js] tab3 strIsrId ==>', tab3.srData.strIsrId, srData.strIsrId);
		//if(tab3.srData.strIsrId == srData.strIsrId) return;
		
		tab3.srData.strIsrId = srData.strIsrId;
		tab3.srData.strStatus = tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_status;
		tab3.screenInit('M');
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab3.initDevPlan();
		}
		
	}else if(document.getElementById("tab4").className == "on") {		// 단위테스트
		tab0 = $('#frmPrjList').get(0).contentWindow;
		
		if(!load4Sw) {
			setTimeout(function() {
				document.getElementById('frmUnitTest').onload = function() {
					load4Sw = true;
				}
			}, 10);
		}
		
		tab4 = $('#frmUnitTest').get(0).contentWindow;
		if(tab0.firstGrid.selectedDataIndexs.length == 0) {
			tab4.screenInit('M');
			return;
		}
		
		console.log('[UnitTest.js] tab4 strIsrId ==>', tab4.srData.strIsrId, srData.strIsrId);
		//if(tab4.srData.strIsrId == srData.strIsrId) return;
		
		tab4.srData.strIsrId = srData.strIsrId;
		tab4.srData.strStatus = tab0.firstGrid.list[tab0.firstGrid.selectedDataIndexs].cc_status;
		tab4.screenInit('M');
		if(srData.strIsrId != null && srData.strIsrId != '' && srData.strIsrId != undefined) {
			tab4.unitTestCall();
		}
	}
}

// PrjListTab에서 호출
function subScreenInit() {
	if(!load1Sw) {
		setTimeout(function() {
			document.getElementById('frmSRRegister').onload = function() {
				load1Sw = true;
			}
		}, 10);
	}
	
	if(load1Sw) {
		tab1 = $('#frmSRRegister').get(0).contentWindow;
		if(tab1 != null && tab1 != undefined) {
			tab1.screenInit('M');
		}
	}
	
	if(!load3Sw) {
		setTimeout(function() {
			document.getElementById('frmDevPlan').onload = function() {
				load3Sw = true;
			}
		}, 10);
	}
	
	if(load3Sw) {
		tab3 = $('#frmDevPlan').get(0).contentWindow;
		if(tab3 != null && tab1 != undefined) {
			tab3.screenInit('M');
		}
	}
	
	if(!load4Sw) {
		setTimeout(function() {
			document.getElementById('frmUnitTest').onload = function() {
				load4Sw = true;
			}
		}, 10);
	}
	
	if(load4Sw) {
		tab4 = $('#frmUnitTest').get(0).contentWindow;
		if(tab4 != null && tab1 != undefined) {
			tab4.screenInit('M');
		}
	}
}

// PrjListTab에서 호출
function isrID_click(data) {
	console.log('[UnitTest.js] isrID_click ==>', data);
	srData = new Object();
	visibleObj('off', 'tab1');
	visibleObj('off', 'tab3');
	visibleObj('off', 'tab4');
	
	if(data == null || data == undefined) return;
	
	srData.strIsrId = data.cc_srid;
	srData.strStatus = data.cc_status;
	
	if(data.isrproc.indexOf('41') >= 0) visibleObj('on', 'tab1');
	if(data.isrproc.indexOf('42') >= 0) visibleObj('on', 'tab3');
	if(data.isrproc.indexOf('43') >= 0) visibleObj('on', 'tab4');
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

// UnitTestTab에서 호출
function cmdQry_click() {
	tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.getPrjList();
	}
}

//UnitTestTab에서 호출
function getQry_click() {
	tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.cboQryGbn_click();
	}
}