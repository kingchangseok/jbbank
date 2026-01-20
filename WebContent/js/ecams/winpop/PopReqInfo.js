/**
 * [요구관리정보] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이해성
 * 	버전 		: 1.1
 *  수정일 	: 2021-05-10
 * 
 */
var userId 	= $('#userId').val();  //strUserId
var code 	= $('#code').val();  //code
var reqCd 	= $('#reqCd').val();  //strReqCd
var isrId 	= $('#isrId').val();  //strIsrId + strIsrSub
var codeList= $('#codeList').val();

var strUserId = "";
var strIsrId = "";
var strIsrSub = "";

var strReqCd = "";
var strReqCode = "";
var base_dp = [];

var tab1 = null;
var tab2 = null;
var tab3 = null;
var tab4 = null;
var tab5 = null;
var tab6 = null;

var tabBase = null;

codeList = codeListJsonParse(codeList);

$(document).ready(function() {
	// 변경관리정보
	$('#btnChgInfo').bind('click', function() {
		popOpen("CHG");
	});		
	// 테스트관리정보
	$('#btnTestInfo').bind('click', function() {
		popOpen("TEST");
	});				
	
	strUserId = userId;
	strIsrId = isrId;
	strReqCd = reqCd;
	strReqCode = code;
	//ReqCd=39&UserId=20010308&IsrId=R201103-0101-01
	
	//strReqCd = "31";
	//strUserId = "20091101";
	//strIsrId = "R201106-0309-01";

	tab1 = $('#frmSRRegister').get(0).contentWindow;
	tab2 = $('#frmSRAccept').get(0).contentWindow;
	tab3 = $('#frmRFCRegister').get(0).contentWindow; 
	tab4 = $('#frmSRComplete').get(0).contentWindow;
	tab5 = $('#frmSRCompleteByUser').get(0).contentWindow; 
	tab6 = $('#frmComment').get(0).contentWindow; 

	if (strUserId == "" || strUserId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}
	if (strIsrId == "" || strIsrId == null) {
		dialog.alert("ISR 정보가 없습니다.");
		return;
	}
	 
 	strIsrSub = "";
	if (strIsrId.length > 12) {
		strIsrSub = strIsrId.substr(13);	
	} 
	strIsrId = strIsrId.substr(0,12);		

	clickTabMenu();
});	

window.onload = function() {
	screenInit();
	getPrjInfoDetail();
}

function getPrjInfoDetail() {
	var data = {
		IsrId : strIsrId,
		IsrSub : strIsrSub,
		ReqCd : "REQ",
		UserId : strUserId,
		requestType : 'getPrjInfoDetail'
	}

	ajaxAsync('/webPage/srcommon/SRRegister', data, 'json', successGetPrjInfoDetail);
}

function screenInit() {
	tabBase = $('#frmBaseISRInfo').get(0).contentWindow;
   
	tabBase.$('#txtISRId').val("");
	tabBase.$('#txtReqDT').val("");
	tabBase.$('#txtStatus').val("");
	tabBase.$('#txtTitle').val("");
	tabBase.$('#txtReqDept').val("");
	tabBase.$('#txtProStatus').val("");
	tabBase.$('#txtEditor').val("");
	tabBase.$('#txtComEdDt').val("");
}

function successGetPrjInfoDetail(data) {
	base_dp = data;
		
	if (base_dp.length > 0){
		tabBase.$('#txtISRId').val(base_dp[0].isrid);
		tabBase.$('#txtReqDT').val(base_dp[0].reqdate);
		tabBase.$('#txtStatus').val(base_dp[0].reqsta1);
		tabBase.$('#txtTitle').val(base_dp[0].isrtitle);
		tabBase.$('#txtReqDept').val(base_dp[0].reqdept);
		tabBase.$('#txtProStatus').val(base_dp[0].reqsta2);
		tabBase.$('#txtEditor').val(base_dp[0].requser);
		tabBase.$('#txtComEdDt').val(base_dp[0].reqenddt);		
		   
		if (base_dp[0].subtab.indexOf("31")>=0 || strReqCd == "31") {
			tab1.screenInit("M");
			visibleObj('on', "tab1");	
		} else {
			visibleObj('off', "tab1");		
		}
		if (base_dp[0].subtab.indexOf("32")>=0 || strReqCd == "32") {
			tab2.screenInit("M");
			visibleObj('on', "tab2");	
		} else {
			visibleObj('off', "tab2");		
		}
		if (base_dp[0].subtab.indexOf("33")>=0 || strReqCd == "33") {
			tab3.screenInit("M");
			visibleObj('on', "tab3");
		} else {
			visibleObj('off', "tab3");		
		}
		if (base_dp[0].subtab.indexOf("39")>=0 || strReqCd == "39") {
			tab4.screenInit("M");
			visibleObj('on', "tab4");	
		} else {
			visibleObj('off', "tab4");		
		}
		if (base_dp[0].subtab.indexOf("38")>=0 || strReqCd == "38") {
			tab5.screenInit("M");
			visibleObj('on', "tab5");	
		} else {
			visibleObj('off', "tab5");		
		}
		visibleObj('on', "tab6");	

		if (strReqCd == "" || strReqCd == null) {
			if ($('#tab5').attr('style').indexOf('1') > 0) { 
				selectTab("tab5");	
			} else if ($('#tab4').attr('style').indexOf('1') > 0) { 
				selectTab("tab4");	
			} else if ($('#tab3').attr('style').indexOf('1') > 0) { 
				selectTab("tab3");	
			} else if ($('#tab2').attr('style').indexOf('1') > 0) { 
				selectTab("tab2");	
			} else if ($('#tab1').attr('style').indexOf('1') > 0) { 
				selectTab("tab1");	
			} else if ($('#tab6').attr('style').indexOf('1') > 0) {
				selectTab("tab6");	
			}
		} else {
			if (strReqCd == "31") { 
				selectTab("tab1");
			} else if (strReqCd == "32") { 
				selectTab("tab2");
			} else if (strReqCd == "33") { 
				selectTab("tab3");
			} else if (strReqCd == "39") { 
				selectTab("tab4");
			} else if (strReqCd == "38") { 
				selectTab("tab5");
			} else {
				selectTab("tab1");
			}
		}
		
		if (strReqCode == "G31") { 
			selectTab("tab1");	
		}
		
		if (base_dp[0].maintab != null) {
			var tmpMain = base_dp[0].maintab;
			if (tmpMain.indexOf("R") >= 0 && tmpMain.indexOf("C") >= 0 && tmpMain.indexOf("T") >= 0) {
				$('#btnChgInfo').css('visibility', 'visible');
				$('#btnChgInfo').attr('disabled', false);
				$('#btnTestInfo').css('visibility', 'visible');
				$('#btnTestInfo').attr('disabled', false);	
			} else if (tmpMain.indexOf("R")>=0 && tmpMain.indexOf("C")>=0) {
				$('#btnChgInfo').css('visibility', 'visible');
				$('#btnChgInfo').attr('disabled', false);
			}
		}
	}
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

function selectTab(tab) { 
	if($('#' + tab).hasClass("on")) {
		changeTabMenu();
		return;
	}
	$(".tab_content").hide();
	var activeTab = $('#' + tab).attr("rel");
	$("ul.tabs li").removeClass('on');
	$('#' + tab).addClass("on");
	
	$("#" + activeTab).fadeIn();
	changeTabMenu();
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

function changeTabMenu() {
	if (document.getElementById("tab1").className == "on") {	   					
		tab1.strIsrId = strIsrId;
		tab1.strIsrSub = strIsrSub;		
		tab1.strUserId = strUserId;
		tab1.popSw = true;
		tab1.strReqCd = "31";
		tab1.isrInfoCall();	
	} else if (document.getElementById("tab2").className == "on") {		
		tab2.strIsrId = strIsrId;
		tab2.strIsrSub = strIsrSub;		
		tab2.strUserId = strUserId;
		tab2.strReqCd = "32";
		tab2.isrInfoCall();
	} else if (document.getElementById("tab3").className == "on") {
		tab3.strIsrId = strIsrId;
		tab3.strIsrSub = strIsrSub;	
		tab3.strUserId = strUserId;
		tab3.popSw = true;
		tab3.strReqCd = "33";	
		tab3.rfcCall();
	} else if (document.getElementById("tab4").className == "on") { 
		tab4.strIsrId = strIsrId;
		tab4.strIsrSub = strIsrSub;		
		tab4.strUserId = strUserId;
		tab4.popSw = true;
		tab4.strReqCd = "39";	
		tab4.reqEndCall();
	} else if (document.getElementById("tab5").className == "on") {  
		tab5.strIsrId = strIsrId;
		tab5.strUserId = strUserId;
		tab5.strReqCd = "38";
		tab5.isrEndCall();
	} else if (document.getElementById("tab6").className == "on") {  
		tab6.strIsrId = strIsrId;
		tab6.strUserId = strUserId;
		tab6.strIsrId = strIsrId.substr(0,12);
		tab6.isrCommentCall();
	} 
}

//팝업
function popOpen(status){
	var popName = null;
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
	f.isrId.value   = base_dp[0].isrid;
    
	nHeight = 768;
    nWidth  = 1200;

	cURL = "/webPage/winpop/" + popName + "Info.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}