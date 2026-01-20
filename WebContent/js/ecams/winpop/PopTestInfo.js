/** 
 * [팝업/테스트관리]
 */

var userId 	= $('#userId').val();  //strUserId
var code 	= $('#code').val();  //code
var reqCd 	= $('#reqCd').val();  //strReqCd
var isrId 	= $('#isrId').val();  //strIsrId + strIsrSub
var codeList = $('#codeList').val();

//var testGrid = new ax5.ui.grid();
//var picker = new ax5.ui.picker();

//var gridData = [];
//var gridIndex = '';
//var selectedGridItem = [];

var strIsrSub = isrId.substr(13);
var strIsrId = isrId.substr(0,12);
var strReqCd = "";

var tab1 = '';
var tab2 = '';
var tab3 = '';
var tabBase	= '';
var strSubStatus = '';
var tmpInfo	= {};
var prjData = [];

codeList = codeListJsonParse(codeList);

$(document).ready(function() {
	
	tab1 = $('#frmTestAccept').get(0).contentWindow;
	tab2 = $('#frmIntegrationTesting').get(0).contentWindow;
	tab3 = $('#frmTestComplete').get(0).contentWindow;
	tabBase = $('#frmBaseTestInfo').get(0).contentWindow;
	
	strUserId = userId;
	strIsrId = isrId;
	strReqCd = reqCd;
	strReqCode = code;
	
	strIsrSub = strIsrId.substr(13);
	strIsrId = strIsrId.substr(0,12);	
	
	if (userId == null || userId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.', function(){
			window.close();	
		});
	}
	// 요구관리정보
	$('#btnReqInfo').on('click', function() {
		popOpen("REQ");
	});

	// 변경관리정보
	$('#btnChgInfo').on('click', function() {
		popOpen("CHG");
	});
	clickTabMenu();
});

window.onload = function() {
	screenInit();
	getPrjInfo();
}

function screenInit(){
	tabBase = $('#frmBaseTestInfo').get(0).contentWindow;
	
	tabBase.$('#txtISRId').val('');
	tabBase.$('#txtReqDT').val('');
	tabBase.$('#txtStatus').val('');
	tabBase.$('#txtTitle').val('');
	tabBase.$('#txtReqDept').val('');
	tabBase.$('#txtProStatus').val('');
	tabBase.$('#txtEditor').val('');
	tabBase.$('#txtComEdDt').val('');
}

function getPrjInfo(){
	
	tmpInfo = {
		IsrId		: strIsrId,
		IsrSub		: strIsrSub,
		ReqCd		: 'TST',
		UserId		: userId,
		requestType	: 'getPrjInfoDetail'
	}
	
	ajaxAsync('/webPage/srcommon/SRRegister', tmpInfo, 'json', successGetPrjInfo);
}

function successGetPrjInfo(data){
	
	prjData = data;
	
	tabBase.$('#txtISRId').val(prjData[0].isrid);
	tabBase.$('#txtReqDT').val(prjData[0].reqdate);
	tabBase.$('#txtStatus').val(prjData[0].reqsta1);
	tabBase.$('#txtTitle').val(prjData[0].isrtitle);
	tabBase.$('#txtReqDept').val(prjData[0].reqdept);
	tabBase.$('#txtProStatus').val(prjData[0].reqsta2);
	tabBase.$('#txtEditor').val(prjData[0].requser);
	tabBase.$('#txtComEdDt').val(prjData[0].reqenddt);
	strSubStatus = prjData[0].cc_substatus;
	
	if (prjData[0].subtab.indexOf('51')>=0 || strReqCd == '51') {
		tab1.screenInit("M");
		visibleObj('on', "tab1");	
	} else {
		visibleObj('off', "tab1");		
	}
   	if (prjData[0].subtab.indexOf('52')>=0 || strReqCd == '52') {
   		tab2.screenInit("M");
		visibleObj('on', "tab2");	
	} else {
		visibleObj('off', "tab2");		
	}
   	if (prjData[0].subtab.indexOf('59')>=0 || strReqCd == '59') {
   		tab3.screenInit("M");
		visibleObj('on', "tab3");	
	} else {
		visibleObj('off', "tab3");		
	}
   	
   	if (strReqCd == '' || strReqCd == null) {
   		if ($('#tab3').attr('style').indexOf('1') > 0) { 
   			selectTab("tab3");
   		} else if ($('#tab2').attr('style').indexOf('1') > 0) { 
   			selectTab("tab2");
   		} else if ($('#tab1').attr('style').indexOf('1') > 0) { 
   			selectTab("tab1");
   		}
   	} else {
   		if (strReqCd == "51") { 
   			selectTab("tab1");
   		} else if (strReqCd == "52") { 
   			selectTab("tab2");
   		} else if (strReqCd == "59") { 
   			selectTab("tab3");
   		}
   	}
   	
   	if (prjData[0].maintab != null) {
	    var tmpMain = data[0].maintab;
	    if (tmpMain.indexOf('R')>=0 && tmpMain.indexOf('C')>=0 && tmpMain.indexOf('T')>=0) {
	    	$('#btnReqInfo').prop('disabled', false);
	    	$('#btnChgInfo').prop('disabled', false);
	    } else if (tmpMain.indexOf('R')>=0 && tmpMain.indexOf('C')>=0) {
	    	$('#btnReqInfo').prop('disabled', false);
	    	$('#btnChgInfo').prop('disabled', false);
	    } else if (tmpMain.indexOf('R')>=0) {
	    	$('#btnReqInfo').prop('disabled', false);
    	}
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


function changeTabMenu(){
	
	if(document.getElementById("tab1").className == "on"){
		
		tab1.strIsrId = strIsrId;
		tab1.strIsrSub = strIsrSub;
		tab1.strSubStatus = strSubStatus;
		tab1.strReqCd = '51';
		tab1.strUserId = userId;
		
		tab1.$('#txtTestYn').val('YES'); 
		
		tab1.$('#btnReg').prop('disabled', true);
		
		tab1.testcntCall();
	} else if(document.getElementById("tab2").className == "on"){
		
		tab2.strIsrId = strIsrId;
		tab2.strIsrSub = strIsrSub;
		tab2.strSubStatus = strSubStatus;
		tab2.strReqCd = '52';
		tab2.strUserId = userId;
		
		tab2.testcntCall();
	} else if(document.getElementById("tab3").className == "on"){
		
		tab3.strIsrId = strIsrId;
		tab3.strIsrSub = strIsrSub;
		tab3.strSubStatus = strSubStatus;
		tab3.strReqCd = '59';
		tab3.strUserId = userId;
		
		tab3.testcntCall();
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
	f.isrId.value   = prjData[0].isrid;
    
	nHeight = 768;
    nWidth  = 1200;

	cURL = "/webPage/winpop/" + popName + "Info.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
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

function visibleObj(state, tab) {
	if(state == 'on') {
		$('li[id^='+tab+']').fadeTo(0, 1);
		$('li[id^='+tab+']').css('pointer-events', 'visible');			
	} else {
		$('li[id^='+tab+']').fadeTo(0, 0.5);
		$('li[id^='+tab+']').css('pointer-events', 'none');
	}
}










