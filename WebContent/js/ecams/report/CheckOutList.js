/**
  [보고서] 장기체크아웃현황
*/

var userId 		= window.top.userId;
var adminYN		= window.top.adminYN;

var gridProg	= new ax5.ui.grid();
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var tmpInfo = {};
var options = [];
var columns = [];
var cboSysData = [];
var cboJobData = [];
var mainGridData = [];
var sysAll = [];
var lastChk = '';

$('#dateSt').val(getDate('DATE',0));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

mainGrid.setConfig({
    target: $('[data-ax5grid="mainGrid"]'),
    sortable: true,
    multiSort: true,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_sysmsg",		label: "시스템",		width: '9%',  align: "left"},
        {key: "cm_jobname",		label: "업무명",		width: '8%',  align: "left"},
        {key: "cm_dirpath",		label: "프로그램경로",	width: '11%',  align: "left"},
        {key: "cr_rsrcname",	label: "프로그램명",	width: '10%',  align: "left"},
        {key: "jawon",			label: "프로그램종류",	width: '8%',   align: "left"},
        {key: "cm_deptname",	label: "신청팀",		width: '7%',   align: "left"},
        {key: "cm_username",	label: "신청인",		width: '5%',   align: "center"},
        {key: "acptdate",		label: "신청일시",		width: '8%',  align: "center"},
        {key: "dayTerm",		label: "경과일수",		width: '5%',   align: "center"},
        {key: "statusNM",		label: "진행상태",		width: '6%',  align: "center"},
        {key: "acptno",			label: "신청번호",		width: '10%',  align: "center"},
        {key: "cr_sayu",		label: "신청사유",		width: '13%',  align: "left"}
    ],
	contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
        	{type: 1, label: "프로그램정보"}
        ],
        popupFilter: function (item, param) {
        	if(param.dindex != null){
        		mainGrid.clearSelect();
        		mainGrid.select(Number(param.dindex));
        		return true;
        	}
        },
        onClick: function (item, param) {
        	mainGrid.contextMenu.close();
        	openWindow(item.type, 'win', param.item.cr_acptno, param.item);
        }
	}
});

$('[data-ax5select="cboSysCd"]').ax5select({
	option : []
});

$(document).ready(function() {
	
	$('#dateSt').prop('disabled',true);
	$('#btnSt').prop('disabled',true);
	$('#dateEd').prop('disabled',true);
	$('#btnEd').prop('disabled',true);
	
	getSysInfo();
	
	//조회
	$("#btnQry").on('click', function() {
		btnQry_click();
	});
	
	//신청일기준 선택
	$('#chkDay').on('click', function(){
		chkDay_click();
	});
	
	//시스템 변경
	$("#cboSysCd").on('change', function() {
		cboSysCd_change();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("장기체크아웃현황_" + today + ".xls");
	});
});

function getSysInfo(){
	
	tmpInfo = {
		UserId		: userId,
		SecuYn		: 'Y',
		SelMsg		: 'ALL',
		CloseYn		: 'N',
		ReqCd		: '',
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetSysInfo);
}

function successGetSysInfo(data){
	
	cboSysData = data;
	
	options = [];
	
	cboSysData = cboSysData.filter(function(data){
		if(data.cm_sysinfo.substr(0,1) == '1'){
			return false;
		} else{
			return true;
		}
	});
	
	$.each(cboSysData, function(i,item){
		options[i] = {value: item.cm_syscd, text: item.cm_sysmsg};
	});
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options : options
	});
	
	cboSysCd_change();
}

function cboSysCd_change(){
	
	if(getSelectedIndex('cboSysCd') > 0){
		tmpInfo = {
			UserID		: userId,
			SysCd		: getSelectedVal('cboSysCd').value,
			SecuYn		: 'Y',
			CloseYn		: 'N',
			SelMsg		: 'ALL',
			sortCd		: 'NAME',
			requestType	: 'getJobInfo'
		}
		ajaxAsync('/webPage/common/SysInfoServlet', tmpInfo, 'json', successGetJobInfo);
	} else{
		$('[data-ax5select="cboJob"]').ax5select({});
	}
}


function successGetJobInfo(data){
	
	options = [];
	
	cboJobData = data;
	
	$.each(cboJobData, function(i,item){
		options[i] = {value: item.cm_jobcd, text: item.cm_jobname};
	});
	
	$('[data-ax5select="cboJob"]').ax5select({
		options : options
	});
}

function chkDay_click(){
	
	if($('#chkDay').is(':checked')){
		$('#dateSt').prop('disabled',false);
		$('#btnSt').prop('disabled',false);
		$('#dateEd').prop('disabled',false);
		$('#btnEd').prop('disabled',false);
	} else{
		$('#dateSt').prop('disabled',true);
		$('#btnSt').prop('disabled',true);
		$('#dateEd').prop('disabled',true);
		$('#btnEd').prop('disabled',true);
	}
}

function btnQry_click(){
	
	var strSys = '';
	var strJob = '';
	var strStD = '';
	var strEdD = '';
	var baseCd = '0';
	
	if(getSelectedIndex('cboSysCd') > 0) strSys = getSelectedVal('cboSysCd').value;
	if(cboJobData != null && getSelectedIndex('cboJob') > 0){
		strJob = getSelectedVal('cboJob').value;
	}
	
	if($('#chkDay').is(':checked')){
		strStD = replaceAllString($('#dateSt').val(),'/','');
		strEdD = replaceAllString($('#dateEd').val(),'/','');
		baseCd = '1';
	}
	
	tmpInfo = {
		UserId		: userId,
		SysCd		: strSys,
		JobCd		: strJob,
		txtPath		: $('#txtPath').val(),
		txtUser		: $('#txtUser').val(),
		StDate		: strStD,
		EdDate		: strEdD,
		Ilsu		: $('#txtIlsu').val(),
		baseCd		: baseCd,
		prgnm		: $('#txtPrg').val(),
		requestType	: 'getReqList'
	}
	
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp1300Servlet', tmpInfo, 'json', successGetReqList);
	
	
}

function successGetReqList(data) {
	$(".loding-div").remove();
	mainGridData = data;
	mainGrid.setData(mainGridData);
}

function openWindow(type,reqCd,reqNo,item) {
	var nHeight, nWidth, nTop, nLeft, cURL, cFeatures, winName;
	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
    winName = type+'_'+reqCd;
	nHeight = 375;
    nWidth  = 1200;
    cURL = "/webPage/winpop/PopProgramInfo.jsp";
	var winWidth  = document.body.clientWidth;  // 현재창의 너비
	var winHeight = document.body.clientHeight; // 현재창의 높이
	var winX      = window.screenX;// 현재창의 x좌표
	var winY      = window.screenY; // 현재창의 y좌표
	nLeft = winX + (winWidth - nWidth) / 2;
	nTop = winY + (winHeight - nHeight) / 2;

	
	var f = document.popPam;   		//폼 name
	f.acptno.value	= '';    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value	= item.cr_itemid;
	f.syscd.value = item.cm_syscd;
	f.rsrccd.value = item.cr_rsrccd;
	f.rsrcname.value = item.cr_rsrcname.trim();

    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
