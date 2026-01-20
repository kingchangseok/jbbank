/**
 * 시스템상세정보 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-31
 * 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var selectedSystem  = window.parent.selectedSystem;
var selectedGbnCd	= window.parent.selectedGbnCd;
var sysInfoGridData = window.parent.sysInfoGridData;
var jobInfoData		= window.parent.jobInfoData;

var sysCd 	  = '';
var urlArr	  = [];
var loadSw 	  = false;
var frmLoad1  = false;
var frmLoad2  = false;
var frmLoad3  = false;
var frmLoad4  = false;
var frmLoad5  = false;
var frmLoad6  = false;
var frmLoad7  = false;
var frmLoad8  = false;
var frmLoad9  = false;

$(document).ready(function(){	
	$('#tab1Li').width($('#tab1Li').width()+10);
	$('#tab2Li').width($('#tab2Li').width()+10);
	$('#tab3Li').width($('#tab3Li').width()+10);
	$('#tab4Li').width($('#tab4Li').width()+10);
	$('#tab5Li').width($('#tab5Li').width()+10);
	$('#tab6Li').width($('#tab6Li').width()+10);
	$('#tab7Li').width($('#tab7Li').width()+10);
	$('#tab8Li').width($('#tab8Li').width()+10);
	$('#tab9Li').width($('#tab9Li').width()+10);

	$('#tab1').attr('disabled', true);
	$('#tab2').attr('disabled', true);
	$('#tab3').attr('disabled', true);
	$('#tab4').attr('disabled', true);
	$('#tab5').attr('disabled', true);
	$('#tab6').attr('disabled', true);
	$('#tab7').attr('disabled', true);
	$('#tab8').attr('disabled', true);
	$('#tab9').attr('disabled', true);
	$("ul.tabs li").addClass('tab_disabled');
	callSysDetail();
});

document.getElementById('frmBaseTab').onload = function() {
    loadSw = true;
}

//페이지 로딩 완료시 다음 진행 
function callSysDetail() {
   var inter = setInterval(function(){
      if(loadSw) {
    	 $('#frmBaseTab').get(0).contentWindow.createViewGrid();
		 clearInterval(inter);
         loadSw = false
      }
   },100);
   inter = null;
   
   initScreen();
}

function initScreen() {
	setTabMenu();
	scrLoad();
	//닫기 버튼 클릭시
	$('#btnExit').bind('click',function() {
		popClose();
	});
}
function scrLoad() {
	if (selectedSystem != null) {
		$('#txtSysMsg').val('[' + selectedSystem.cm_syscd + '] ' + selectedSystem.cm_sysmsg);
		sysCd = selectedSystem.cm_syscd;
		
		$('#tab1Li').attr('disabled', false);
		$('#tab1Li').removeClass('tab_disabled');
		
		$('#tab2Li').attr('disabled', false);
		$('#tab2Li').removeClass('tab_disabled');
		
		$('#tab3Li').attr('disabled', false);
		$('#tab3Li').removeClass('tab_disabled');
		
		$('#tab4Li').attr('disabled', false);
		$('#tab4Li').removeClass('tab_disabled');
		
		$('#tab5Li').attr('disabled', false);
		$('#tab5Li').removeClass('tab_disabled');
		
		$('#tab6Li').attr('disabled', false);
		$('#tab6Li').removeClass('tab_disabled');
		
		$('#tab7Li').attr('disabled', false);
		$('#tab7Li').removeClass('tab_disabled');
		
		$('#tab8Li').attr('disabled', false);
		$('#tab8Li').removeClass('tab_disabled');
		
		$('#tab9Li').attr('disabled', false);
		$('#tab9Li').removeClass('tab_disabled');
	} else {
		$('#txtSysMsg').val('시스템신규등록');
		
		$('#tab1Li').attr('disabled', false);
		$('#tab1Li').removeClass('tab_disabled');
	}
}
function setTabMenu(){
	$(".tab_content:first").show();
	$("ul.tabs li").click(function () {
		$(".tab_content").hide();
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		$("#" + activeTab + " iframe").attr('src', $("#" + activeTab + " iframe").attr('src'));
		$("#" + activeTab).fadeIn();
		
		if (activeTab == "baseTab") {
			frmLoad1 = true;
		} else if (activeTab == "progTab") {
			if (!frmLoad2) {
				$("#frmPrgTab").attr('src','/webPage/tab/sysinfo/SysDetailPrgTab.jsp');
			}
			frmLoad2 = true;
		} else if (activeTab == "svrTab") {
			if (!frmLoad3) {
				$("#frmSvrTab").attr('src','/webPage/tab/sysinfo/SysDetailSvrTab.jsp');
			}
			frmLoad3 = true;
		} else if (activeTab == "usrTab") {
			if (!frmLoad4) {
				$("#frmUsrTab").attr('src','/webPage/tab/sysinfo/SysDetailUsrTab.jsp');
			}
			frmLoad4 = true;
		} else if (activeTab == "svrprgTab") {
			if (!frmLoad5) {
				$("#frmSvrPrgTab").attr('src','/webPage/tab/sysinfo/SysDetailSvrPrgTab.jsp');
			}
			frmLoad5 = true;
		} else if (activeTab == "dirTab") {
			if (!frmLoad6) {
				$("#frmDirTab").attr('src','/webPage/tab/sysinfo/SysDetailComDirTab.jsp');
			}
			frmLoad6 = true;
		} else if (activeTab == "langTab") {
			if (!frmLoad7) {
				$("#frmLangTab").attr('src','/webPage/tab/sysinfo/SysDetailLangTab.jsp');
			}
			frmLoad7 = true;
		} else if (activeTab == "makeTab") {
			if (!frmLoad8) {
				$("#frmMakeTab").attr('src','/webPage/tab/sysinfo/SysDetailMakeTab.jsp');
			}
			frmLoad8 = true;
		} else if (activeTab == "compTab") {
			if (!frmLoad9) {
				$("#frmCompTab").attr('src','/webPage/tab/sysinfo/SysDetailCompTab.jsp');
			}
			frmLoad9 = true;
		}
	});
}
function popClose() {
	window.parent.sysDetailModal.close();
}
