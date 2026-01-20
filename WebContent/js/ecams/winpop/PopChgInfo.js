/**
 * [테스트관리정보] 화면 기능정의
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
var codeList = $('#codeList').val();

var IntegrationModal = new ax5.ui.modal();
var approvalModal = new ax5.ui.modal();

var integrationData = [];
var saveObj = new Object();
var strUserId = "";
var strIsrId = "";
var strIsrSub = "";
var strSubStatus = "";
var strReqCd = "";
var strReqNo = "";
var tab1 = null;
var tab2 = null;
var tab3 = null;
var tab4 = null;
var tab5 = null;
var tab6 = null;
var tab7 = null;
var tab8 = null;
var base_dp = [];

var tabBase = null;

codeList = codeListJsonParse(codeList);

$(document).ready(function() {
	// 요구관리정보
	$('#btnReqInfo').bind('click', function() {
		popOpen("REQ");
	});		
	// 테스트관리정보
	$('#btnTestInfo').bind('click', function() {
		popOpen("TEST");
	});		

	strUserId = userId;
	strIsrId = isrId;
	strReqCd = reqCd;
	strReqCode = code;

	tab1 = $('#frmRFCAccept').get(0).contentWindow;
	tab2 = $('#frmDEVPlan').get(0).contentWindow;
	tab3 = $('#frmUnitTest').get(0).contentWindow; 
	tab4 = $('#frmIntegrationTest').get(0).contentWindow;
	tab5 = $('#frmTargetProgram').get(0).contentWindow; 
	tab6 = $('#frmTargetOutput').get(0).contentWindow; 
	tab7 = $('#frmWorkPlan').get(0).contentWindow; 
	tab8 = $('#frmRFCComplete').get(0).contentWindow; 

	if (strUserId == "" || strUserId == null) {
		dialog.alert("로그인 후 사용하시기 바랍니다.");
		return;
	}

	strIsrSub = strIsrId.substr(13);
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
		ReqCd : "CHG",
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
		strSubStatus = base_dp[0].cc_substatus;
		   
		if (base_dp[0].subtab.indexOf("41")>=0 || strReqCd == "41") {
			tab1.screenInit("M");
			visibleObj('on', "tab1");	
		} else {
			visibleObj('off', "tab1");		
		}
		if (base_dp[0].subtab.indexOf("42")>=0 || strReqCd == "42") {
			tab2.screenInit("M");
			visibleObj('on', "tab2");	
		} else {
			visibleObj('off', "tab2");		
		}
		if (base_dp[0].subtab.indexOf("43")>=0 || strReqCd == "43") {
			tab3.screenInit("M");
			visibleObj('on', "tab3");
		} else {
			visibleObj('off', "tab3");		
		}
		if (base_dp[0].subtab.indexOf("44")>=0 || strReqCd == "44") {
			tab4.screenInit("M");
			visibleObj('on', "tab4");	
		} else {
			visibleObj('off', "tab4");		
		}
		if (base_dp[0].subtab.indexOf("45")>=0 || strReqCd == "45") {
			tab5.screenInit("M");
			visibleObj('on', "tab5");	
		} else {
			visibleObj('off', "tab5");		
		}
		if (base_dp[0].subtab.indexOf("46")>=0 || strReqCd == "46") {
			tab6.screenInit("M");
			visibleObj('on', "tab6");	
		} else {
			visibleObj('off', "tab6");		
		}
		if (base_dp[0].subtab.indexOf("47")>=0 || strReqCd == "47") {
			tab7.screenInit("M");
			visibleObj('on', "tab7");	
		} else {
			visibleObj('off', "tab7");		
		}
		if (base_dp[0].subtab.indexOf("49")>=0 || strReqCd == "49") {
			tab8.screenInit("M");
			visibleObj('on', "tab8");	
		} else {
			visibleObj('off', "tab8");		
		}							

		if (strReqCd == "" || strReqCd == null) {
			if ($('#tab8').attr('style').indexOf('1') > 0) { 
				selectTab("tab8");
			} 
			if ($('#tab7').attr('style').indexOf('1') > 0) { 
				selectTab("tab7");
			} else if ($('#tab4').attr('style').indexOf('1') > 0) { 
				selectTab("tab4");
			} else if ($('#tab3').attr('style').indexOf('1') > 0) { 
				selectTab( "tab3");
			} else if ($('#tab2').attr('style').indexOf('1') > 0) { 
				selectTab("tab2");
			} else if ($('#tab1').attr('style').indexOf('1') > 0) {
				selectTab("tab1");
			} else if ($('#tab6').attr('style').indexOf('1') > 0) {
				selectTab("tab6");
			} else if ($('#tab5').attr('style').indexOf('1') > 0) {
				selectTab("tab5");
			}
		} else {
			if (strReqCd == "41") { 
				selectTab("tab1");
			} else if (strReqCd == "42") { 
				selectTab("tab2");
			} else if (strReqCd == "43") { 
				selectTab("tab3");
			} else if (strReqCd == "44") { 
				selectTab("tab4");
			} else if (strReqCd == "45") { 
				selectTab("tab5");
			} else if (strReqCd == "46") {
				selectTab("tab6");
			} else if (strReqCd == "47") { 
				selectTab("tab7");
			} else if (strReqCd == "49") { 
				selectTab("tab8");
			}
		}
		
		if (base_dp[0].maintab != null) {
			var tmpMain = base_dp[0].maintab;
			if (tmpMain.indexOf("R") >= 0 && tmpMain.indexOf("C") >= 0 && tmpMain.indexOf("T") >= 0) {
				$('#btnReqInfo').css('visibility', 'visible');
				$('#btnReqInfo').attr('disabled', false);
				$('#btnTestInfo').css('visibility', 'visible');
				$('#btnTestInfo').attr('disabled', false);	
			} else if (tmpMain.indexOf("R")>=0 && tmpMain.indexOf("C")>=0) {
				$('#btnReqInfo').css('visibility', 'visible');
				$('#btnReqInfo').attr('disabled', false);
			} else if (tmpMain.indexOf("R")>=0) {
				$('#btnReqInfo').css('visibility', 'visible');
				$('#btnReqInfo').attr('disabled', false);
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
		tab1.strSubStatus = base_dp[0].cc_substatus;   
		tab1.strUserId = strUserId;
		tab1.strReqCd = "41";
		tab1.gridEdit();			
		tab1.rfcRecvCall();	
	} else if (document.getElementById("tab2").className == "on") {		
		tab2.strIsrId = strIsrId;
		tab2.strIsrSub = strIsrSub;
		tab2.strUserId = strUserId;
		tab2.strReqCd = "42";
		tab2.strSubStatus = base_dp[0].cc_substatus;
		tab2.strMainStatus = base_dp[0].cc_mainstatus;   			
		tab2.devPlanCall();
	} else if (document.getElementById("tab3").className == "on") {
		tab3.strIsrId = strIsrId;
		tab3.strIsrSub = strIsrSub;
		tab3.strUserId = strUserId;
		tab3.strReqCd = "43";
		tab3.strSubStatus = base_dp[0].cc_substatus;
		tab3.strMainStatus = base_dp[0].cc_mainstatus;   
		tab3.gridEdit();						
		tab3.unitTestCall();
	} else if (document.getElementById("tab4").className == "on") { 
		tab4.strIsrId = strIsrId;
		tab4.strIsrSub = strIsrSub;
		tab4.strSubStatus = base_dp[0].cc_substatus;   		
		tab4.strUserId = strUserId; 
		tab4.strReqCd = "44";		
		tab4.gridEdit();
		tab4.testcntCall();
	} else if (document.getElementById("tab5").className == "on") {  
		tab5.strIsrId = strIsrId;
		tab5.strIsrSub = strIsrSub;			
		tab5.strUserId = strUserId;	
		tab5.docListCall();
	} else if (document.getElementById("tab6").className == "on") {  
		tab6.strIsrId = strIsrId;
		tab6.strIsrSub = strIsrSub;  	
		tab6.strUserId = strUserId;	
		tab6.docListCall();
	}  else if (document.getElementById("tab7").className == "on") {  
		tab7.strIsrId = strIsrId;
		tab7.strIsrSub = strIsrSub;
		tab7.strUserId = strUserId;
		tab7.strReqCd = "47";
		tab7.strAcptNo = strReqNo;
		tab7.popSw = true;
				
		tab7.$('#txtISRID').val("[" + base_dp[0].isrid + "] " + base_dp[0].isrtitle);
		tab7.$('#txtDOCNO').val(base_dp[0].cc_docno);
		tab7.$('#txtREQDT').val(base_dp[0].reqdate);
		tab7.$('#txtREQDEPT').val(base_dp[0].reqdept);
		tab7.$('#txtEDITOR').val(base_dp[0].requser);
		tab7.$('#txtSTATUS').val(base_dp[0].reqsta1);
	 	tab7.$('#txtPROSTATUS').val(base_dp[0].reqsta2);
 
		tab7.gridEdit();
		tab7.jobCall(); 
	}  else if (document.getElementById("tab8").className == "on") {  
		tab8.strIsrId = strIsrId;
		tab8.strIsrSub = strIsrSub;
		tab8.strSubStatus = base_dp[0].cc_substatus;
		tab8.strUserId = strUserId;
		tab8.strAcptNo = strReqNo;
		tab8.strReqCd = "49";
		tab8.popSw = true;
		tab8.screenInit('M');
		tab8.chgEndCall();
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

//단위테스트 탭 등록/수정 버튼 클릭
function btnAdd_Save(state, obj) {
	var saveTongTe = "";
	saveObj = obj;
	if(state != 'cancel'){
		if (state == 'ok') {
			IntegrationModal.open({
				width: 600,
				height: 400,
				iframe: {
					method: "get",
					url: "../modal/request/IntegrationTestModal.jsp",
					param: "callBack=modalCallBack"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						newDataBoxWinClose();
						mask.close();
						tab3.ingSw = false;
					}
				}
			});			
			
		} else if (state == 'cancel'){
			saveTongTe = "false";
			saveObj.saveTongTe = saveTongTe;
			tab3.setTcaseAdd();
		}
	}else if(state == 'cancel'){
		dialog.alert("단위테스트 등록이 취소되었습니다.");
	}
}
//modal close시 실행
function newDataBoxWinClose() {
	if (Object.keys(integrationData).length > 0){
		saveObj.saveTongTe = "true";
		saveObj.TongTeTest = integrationData.code;
		saveObj.TongTeTest2 = integrationData.value;
		
		tab3.setTcaseAdd();
	} else {
		dialog.alert("단위테스트 등록이 취소되었습니다.");
	}
}
//변경관리종료탭 결재클릭
function confCall(GbnCd) {

	var etcQryCd = [];
	tab8.confirm_dp = [];
	tab8.confirmInfoData = null;

	etcQryCd = tab8.strReqCd.split(",");

	confirmInfoData = {		
		UserId : tab8.strUserId,
		ReqCd  : tab8.strReqCd,
		PrjNo : tab8.strIsrId+tab8.strIsrSub,
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

	            		tab8.reqQuest(confirmData, this.state);
	            	}
	                mask.close();
					tab8.ingSw = false;
	            }
	        }
		});
	} else {
		confirmInfoData = {	
			UserID : tab8.strUserId,
			SysCd  : "99999",
			ReqCD  : tab8.strReqCd,
			Rsrccd : [],
			QryCd : [],
			EmgSw : "N",
			PrjNo  : tab8.strIsrId+tab8.strIsrSub,
			deployCd : "0",
			JobCd : etcQryCd
		}
		var tmpData = {
			confirmInfoData: 	confirmInfoData,
			requestType: 	'Confirm_Info'
		}
		ajaxAsync('/webPage/request/ConfirmServlet', tmpData, 'json', tab8.successConfirm_Info);

		etcQryCd = null;
	}
}