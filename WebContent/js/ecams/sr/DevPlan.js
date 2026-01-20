/**
 * 개발계획/실적등록 화면 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2019-06-26
 */

var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;
var strReqCd	 	= window.top.reqCd;
var reqCd			= "42";

//public
var strIsrId 		= "";
var strQryGbn 		= "";
var srData 			= new Object(); // to SRRegisterTab
var cboQryGbnData 	= [
	{cm_codename : "전체",			cm_micode : "00", cm_dateyn: "Y"},
	{cm_codename : "개발계획등록대상",	cm_micode : "01", cm_dateyn: "N"},
	{cm_codename : "개발실적등록대상",	cm_micode : "02", cm_dateyn: "N"}
];

var loadSrReg = false;

$(document).ready(function(){
	//tab메뉴
	$('#tabSRRegister').width($('#tabSRRegister').width()+10);
	$('#tabPrgList').width($('#tabPrgList').width()+10);
	$('#tabDevPlan').width($('#tabDevPlan').width()+10);

	$('#datStD').val(getDate('DATE',-1));
	$('#datEdD').val(getDate('DATE',0));

	$("#datStD, #datEdD").bind('change', function(){
		var dataId = $(this).prop("id");
		var dataStd = $('#frmPrjList').contents().find("#"+dataId).val($(this).val());
	});

	$(document).on("focusout",function(){
		setTimeout(function(){
			if(document.activeElement instanceof HTMLIFrameElement){
				$('[data-picker-btn="ok"]').click();
			}
		},0);
	});
	
	$('.btn_calendar').bind('click', function(e) {
		e.preventDefault();
	    e.stopPropagation();
		if($(this).css('background-color') === 'rgb(255, 255, 255)') {
			var inputs = $(this).siblings().prevAll('input');
			$(inputs.prevObject[0]).trigger('click');
		}
	});
	
	$(document).on("click",".ax5-ui-picker",function(){
		$("#btnStD").focus();
	});
	
	strQryGbn = "01";
	if(strReqCd != null && strReqCd != "") {
		if(strReqCd.length > 3) strReqGbn = strReqCd.substr(0,2);
	}
	
	strReqCd = "42";
	
	initScreen();
	
//	document.getElementById('frmSRRegister').onload = function() {
//		setSRRegData();
//	};
//	
//	document.getElementById('frmDevPlan').onload = function() {
//		loadSrReg = true;
//	}
//	
//	document.getElementById('frmPrjList').onload = function() {
//		load1Sw = true;
//		
//		var tab0 = $('#frmPrjList').get(0).contentWindow;
//		if(tab0 != null && tab0 != undefined) {
//			tab0.screenInit();
//			tab0.cboQryGbn_click();
//		}
//	}
	
	checkFrameLoad(["frmSRRegister","frmDevPlan","frmPrgList","frmPrjList"], frameReadyFunction);
	
	$("#tabDevPlan").show(); //개발계획실적탭
	clickTabMenu();
});

function frameReadyFunction() {
	setSRRegData();
	
	loadSrReg = true;
	var tab0 = $('#frmPrjList').get(0).contentWindow;
	if(tab0 != null && tab0 != undefined) {
		tab0.screenInit();
		tab0.cboQryGbn_click();
	}
}

//페이지 로딩 완료시 다음 진행 
var inter = null;

function callSRRegister(data) {
   inter = setInterval(function(){
      if(loadSrReg) {
         iSRID_Click(data);
         clearInterval(inter);
      }
   },100);
}

function setSRRegData() {
	var tmpTab = $('#frmSRRegister').get(0).contentWindow;
//	tmpTab.createViewGrid();
	tmpTab.strReqCd = "XX";
}

function initScreen() {
	$("#tab1").unbind("click");
	$("#tab1_2").unbind("click");
	$("#tab2").unbind("click");
	$("#tab1").addClass("tab_disabled");
	$("#tab1_2").addClass("tab_disabled");
	$("#tab2").addClass("tab_disabled");
}

//자식(PrjListTab)에서 호출하는 함수 subScreenInit()
function subScreenInit() {
	var tmpTab1 = $('#frmSRRegister').get(0).contentWindow;
	var tmpTab2 = $('#frmDevPlan').get(0).contentWindow;
	var tmpTab1_2 = $('#frmPrgList').get(0).contentWindow;
	setTimeout(function () {
		tmpTab1.screenInit("M"); 
//		tmpTab1_2.elementInit("M"); 
//		tmpTab2.screenInit();
		tmpTab1.strIsrId = "";
		tmpTab1_2.strIsrId = "";
//		tmpTab2.strIsrId = "";
	}, 500);
}

//PrjListTab 에서 그리드 클릭 이벤트
function isrID_click(data) {
	strIsrId = "";
	
	//$("#tab1").prop('disabled',true);
	$("#tab1").unbind("click");
	$("#tab1_2").unbind("click");
	//$("#tab2").prop('disabled',true);
	$("#tab2").unbind("click");
	
	if(data == null) return;
	
	strIsrId = data.cc_srid;
	srData.strStatus = data.cc_status;
	srData.strIsrId = data.cc_srid;
	
	if(data.isrproc.indexOf("41") >= 0) {
		//$("#tab1").prop('disabled',false);
		$("#tab1").removeClass("tab_disabled");
		$("#tab1").bind("click", clickTabMenu());
	}
	if(data.isrproc.indexOf("93") >= 0) {
		//$("#tab2").prop('disabled',false);
		$("#tab1_2").removeClass("tab_disabled");
		$("#tab1_2").bind("click", clickTabMenu());
	}
	if(data.isrproc.indexOf("42") >= 0) {
		//$("#tab2").prop('disabled',false);
		$("#tab2").removeClass("tab_disabled");
		$("#tab2").bind("click", clickTabMenu());
	}
	
	data = null;
	changeTabMenu();
	//clickTabMenu();
	
	//$('#tab2').trigger('click');
}

//tabnavi_click
function changeTabMenu() {
	if(document.getElementById("tab1").className == "on") { //$('#tab1').is(':enabled')
		var tmpGrid = $('#frmPrjList').get(0).contentWindow.firstGrid;
		var tmpGridSelectedIndex = tmpGrid.selectedDataIndexs;
		var tmpSelectedGridItem = tmpGrid.list[tmpGrid.selectedDataIndexs];
		var tmpTab = $('#frmSRRegister').get(0).contentWindow;
//		tmpTab.createViewGrid();
		console.log("tmpGridSelectedIndex: " + tmpGridSelectedIndex);
		
		if(tmpGridSelectedIndex < 0) {
			tmpTab.screenInit("M");
			return;
		}
		
		if(tmpTab.strIsrId == strIsrId) return;
		
		reqCd = 'XX';
		
		srData = new Object();
		srData.strStatus = tmpSelectedGridItem.cc_status;
		srData.strIsrId = strIsrId;
		
		tmpTab.strStatus = tmpSelectedGridItem.cc_status;
		tmpTab.strIsrId = strIsrId;
		tmpTab.grdPrj_click(strIsrId); //tab1.grdPrj_click(strIsrId)
		tmpTab.screenInit("M"); //tab1.screenInit("M");
		
	}else if(document.getElementById("tab2").className == "on") { //$('#tab2').is(':enabled')
		var tmpGrid = $('#frmPrjList').get(0).contentWindow.firstGrid;
		var tmpGridSelectedIndex = tmpGrid.selectedDataIndexs;
		var tmpSelectedGridItem = tmpGrid.list[tmpGrid.selectedDataIndexs];
		var tmpTab = $('#frmDevPlan').get(0).contentWindow;
		
		if(tmpGridSelectedIndex < 0) {
			tmpTab.screenInit("M"); //tab2.screenInit("M");
			return;
		}
		
		reqCd = '42';
		
		//if(tmpTab.strIsrId == strIsrId) return;
		
		tmpTab.strIsrId = strIsrId; 
		tmpTab.strStatus = tmpSelectedGridItem.cc_status;
		
//		tmpTab.screenInit("M");
		
		if(strIsrId != null && strIsrId != "") {
			tmpTab.initDevPlan(); //tab2.devPlanCall();
		}
	}else if(document.getElementById("tab1_2").className == "on") { //$('#tab2').is(':enabled')
		var tmpGrid = $('#frmPrjList').get(0).contentWindow.firstGrid;
		var tmpGridSelectedIndex = tmpGrid.selectedDataIndexs;
		var tmpSelectedGridItem = tmpGrid.list[tmpGrid.selectedDataIndexs];
		var tmpTab = $('#frmPrgList').get(0).contentWindow;
		
		reqCd = '42';
		
		if(strIsrId != null && strIsrId != '' && strIsrId != undefined) {
			tmpTab.docListCall();
		}
	
		if(tmpGridSelectedIndex < 0) {
			tmpTab.screenInit("M"); //tab2.screenInit("M");
			return;
		}
		
		tmpTab.strIsrId = strIsrId; 
		tmpTab.strStatus = tmpSelectedGridItem.cc_status;
		//if(tmpTab.strIsrId == strIsrId) return;
		
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