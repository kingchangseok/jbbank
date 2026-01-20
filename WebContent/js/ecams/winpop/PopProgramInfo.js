var tmpTab1	= null;
var tmpTab2 = null;
var tmpTab3 = null;

var load1Sw = false;
var load2Sw = false;
var load3Sw = false;
var gridSw  = false;

var tmpInfoData = null;
var progData = null;
var progInfoData = null;

var f = document.getSrcData;

var pItemId  = f.itemid.value;
var pUserId  = f.user.value;
var syscd 	 = f.syscd.value;
var sysmsg 	 = f.sysmsg.value;
var jobcd 	 = f.jobcd.value;
var rsrccd 	 = f.rsrccd.value;
var rsrcname = f.rsrcname.value;
var dirpath  = f.dirpath.value;
var rgtList  = f.rgtList.value;

var adminYN = false;
var SecuYn = 'N';

$(document).ready(function(){
	if (pUserId == null || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pItemId == null || pItemId.length != 12) {
		dialog.alert('프로그램ID가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	var data = {
		UserID  : pUserId,
		requestType  : 'isAdmin'
	}
	adminYN = ajaxCallWithJson('/webPage/common/UserInfoServlet', data, 'json');
	data = null;
	
	if (adminYN) {
		SecuYn = 'Y';
	} else {
		SecuYn = 'N';
	}
		
	document.getElementById('frmProgBase').onload = function() {
		load1Sw = true;
//		screenInit_prog('I');
	}
	document.getElementById('frmProgHistory').onload = function() {
		load2Sw = true;
//		screenInit_prog('I');
	}
	
	checkFrameLoad(["frmProgBase","frmProgHistory"], screenInit_prog, 'I');
	
	clickTabMenu();
	//닫기클릭
	$('#btnExit').bind('click', function() {
		close();
	});
});

function clickTabMenu() {
	$(".tab_content:first").show();
	
	$('ul.tabs li').click(function () {
		if($(this).hasClass('on')) {
			return;
		}
		$('.tab_content').hide();
		var activeTab = $(this).attr('rel');
		
		//tab메뉴 클릭에 따라 색상 변경
		$('ul.tabs li').removeClass('on');
		$(this).addClass('on');
		$('#' + activeTab).fadeIn();

		if(this.id === 'tab22') {
			setTimeout(function() {
				tmpTab2 = $('#frmProgHistory').get(0).contentWindow;
				tmpTab2.upFocus();
			}, 10);
		}
		
		if (!gridSw) {
			setTimeout(function() {
				tmpTab2 = $('#frmProgHistory').get(0).contentWindow;
				tmpTab2.createViewGrid();
				tmpTab2.getCodeInfo();
			}, 10);
			gridSw = true;
		}
	});

}

function screenInit_prog(gbn) {
	if (gbn == 'I') {
		console.log('loda1Sw, load2Sw',load1Sw , load2Sw);
//		if (load1Sw && load2Sw) {
			getJobInfo( syscd );  //업무 리로딩
//		} else {
//			return;
//		}
	}
	tmpTab1 = $('#frmProgBase').get(0).contentWindow;
	tmpTab1.screenInit(gbn,pUserId);
	
	tmpTab2 = $('#frmProgHistory').get(0).contentWindow;
	tmpTab2.screenInit(gbn,pUserId);	
	
	$("#tab1").show(); //기본정보
	$('#tab22').removeClass('on');
	$('#tab11').addClass('on');
	$('#tab11').fadeIn();
}

function getJobInfo(sysCd) {
	tmpInfoData = new Object();
	tmpInfoData = {
		L_Syscd : sysCd,
		SecuYn  : 'N',
		UserId  : pUserId,
		ItemId  : '',
		requestType	: 'Cmd0500_Cbo_Set'
	}
	
	tmpTab1 = $('#frmProgBase').get(0).contentWindow;
	tmpTab1.getJobInfo(tmpInfoData);
	getProgInfo();
}

function getProgInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId   : pUserId,
		SecuYn   : adminYN ? 'Y' : 'N',
		L_SysCd  : syscd,
		L_JobCd  : jobcd,
		L_ItemId : pItemId,
		requestType	: 'Cmd0500_Lv_File_ItemClick'
	}
	
	console.log('[Cmd0500_Lv_File_ItemClick] ==>', tmpInfoData);
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json', successProgInfo);
}

function successProgInfo(data) {
	progInfoData = data;
	
	var data = new Object();
	data = {
		Syscd  : syscd,
		SelMsg : "",
		requestType : "getCompile"
	}
	tmpTab1 = $('#frmProgBase').get(0).contentWindow;
	tmpTab1.getCodeInfo(data);
	
	if (progInfoData != null && progInfoData != '' && progInfoData != undefined) {
		progInfoData[0].cm_sysmsg = sysmsg;
		progInfoData[0].cr_rsrcname = rsrcname;
		progInfoData[0].cr_itemid = pItemId;
		progInfoData[0].cm_dirpath = dirpath;
		progInfoData[0].cr_status = progInfoData[0].WkSta;
		
		tmpTab1 = $('#frmProgBase').get(0).contentWindow;
		tmpTab1.successProgInfo(progInfoData);
		tmpTab1.pUserId = pUserId;
		tmpTab1.SecuYn = SecuYn;

		tmpTab2 = $('#frmProgHistory').get(0).contentWindow;
		tmpTab2.successProgInfo(progInfoData);
		tmpTab2.pUserId = pUserId;
		
		$('#tab11').trigger('click');
	}
}