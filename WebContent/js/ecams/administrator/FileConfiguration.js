/**
 * 파일대사환경설정 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-01
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var frmLoad1  = false;
var frmLoad2  = false;
var frmLoad3  = false;

var urlArr		= [];
$(document).ready(function(){
	$('#tab1Li').width($('#tab1Li').width()+10);
	$('#tab2Li').width($('#tab2Li').width()+10);
	$('#tab3Li').width($('#tab3Li').width()+10);
	setTabMenu();
})

function setTabMenu(){
	$(".tab_content:first").show();
	
	$("ul.tabs li").click(function () {
		$(".tab_content").hide();
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		
		$("#" + activeTab + " iframe").attr('src', $("#" + activeTab + " iframe").attr('src'));
		$("#" + activeTab).fadeIn();
//		$("#" + activeTab).show();
		
		//20200420 neo.탭 클릭시 활성화 시키도록 개선
		if (activeTab == "tabDefault") {
			frmLoad1 = true;
		} else if (activeTab == "tabEct") {
			if (!frmLoad2) {
				$("#frmPrgTab").attr('src','/webPage/tab/sysinfo/SysDetailPrgTab.jsp');
			}
			frmLoad2 = true;
		} else if (activeTab == "tabHand") {
			if (!frmLoad3) {
				$("#frmSvrTab").attr('src','/webPage/tab/sysinfo/SysDetailSvrTab.jsp');
			}
			frmLoad3 = true;
		}
		
		$("#" + activeTab + " iframe").bind("load",function(){
			window.top.resize();
		});
		window.top.resize();
	});
}

