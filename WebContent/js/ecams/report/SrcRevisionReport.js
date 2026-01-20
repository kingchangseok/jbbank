/**
 * [보고서 > 프로그램 변경내역] 
 */
var userId 		= window.top.userId;

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var options 	= [];
var tmpInfo 	= {};

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
	},
	columns : [
		{key: "CC_ISRID",	 		label: "ISR-ID",  	width: '10%', 	align: "center"},
		{key: "CC_ISRSUB",	 		label: "SUB-ID",  	width: '7%', 	align: "center"},
		{key: "CC_ISRTITLE",	 	label: "제목",  		width: '20%', 	align: "left"},
		{key: "CC_REQDEPT_NAME",	label: "요청팀",  	width: '10%', 	align: "center"},
		{key: "CC_EDITOR_NAME",	 	label: "요청자",  	width: '7%', 	align: "center"},
		{key: "CC_RECVPART_NAME",	label: "접수파트",  	width: '10%', 	align: "center"},
		{key: "CC_SCMUSER_NAME",	label: "변경담당자",  	width: '7%', 	align: "center"},
		{key: "CC_RECVDATE",	 	label: "접수일",  	width: '7%', 	align: "center"},
		{key: "CC_JOBDATE",	 		label: "예정일",  	width: '7%', 	align: "center"},
		{key: "CC_JOBTIME",	 		label: "작업일",  	width: '7%', 	align: "center"},
		{key: "CC_REALEDDAY",	 	label: "완료일",  	width: '7%', 	align: "center"}
//      {key: "link", 				label: "링크",  		width: '13%', 	align: "left"}
	],
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {
			'arrow': '<i class="fa fa-caret-right"></i>'
		},
		items: [
			{type: 1, label: "요구관리정보"},
			{type: 2, label: "변경관리정보"},
			{type: 3, label: "테스트관리정보"},
			{type: 4, label: "신청상세내역"}  
		],
		popupFilter: function (item, param) {
			mainGrid.clearSelect();
			mainGrid.select(Number(param.dindex));
        	 
        	var selIn = mainGrid.selectedDataIndexs;
        	if(selIn.length == 0) return;
        	 
         	if (param.item == undefined) return false;
         	if (param.dindex < 0) return false;
         	
         	return true;
		},
		onClick: function (item, param) {
			openWindow(item.type, param.item.CC_ISRID, param.item.CC_ISRSUB, param.item.CC_ACPTNO);
			mainGrid.contextMenu.close();
		}
	}
}); 

picker.bind(defaultPickerInfo('basic', 'top'));

ax5.info.weekNames = [ 
	{label : "일"}, 
	{label : "월"}, 
	{label : "화"}, 
	{label : "수"}, 
	{label : "목"}, 
	{label : "금"}, 
	{label : "토"}
 ];

//picker 현재 날짜
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, '/');
	$("#dateSt").val(today);
	$("#dateEd").val(today);
});

var oldVal = "";
// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
$('#dateEd').bind('propertychange change keyup paste input', function() {
	var currentVal =  $("#dateEd").val();
	if(currentVal != oldVal){
		picker.close();
	}
	oldVal = currentVal;
});


$(document).ready(function() {
	
	screenInit();
	
	getSysInfo();
	
	getTeamInfo();

	$("#btnQry").on("click", function(){
		search();	
	});
	
	$("#btnExcel").on('click', function() {
		mainGrid.exportExcel("개인별변경추이.xls");
	});
	
});

function screenInit(){
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDept"]').ax5select({
		options: []
	});
	
}

function getSysInfo(){
	
	var sysData = {
		UserId		: userId,
		SecuYn		: 'N',
		SelMsg		: 'ALL',
		CloseYn		: 'N',
		ReqCd		: ''
	}
	
	tmpInfo = {
		sysData		: sysData,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/CommonSysInfo', tmpInfo, 'json', successGetSysInfo);
}

function successGetSysInfo(data){
	
	options = [];
	
	$.each(data, function(i, item) {
	    options.push({value : item.cm_syscd, text : item.cm_sysmsg});
    });

    $('[data-ax5select="cboSysCd"]').ax5select({
	    options: options
    });
}

function getTeamInfo(){
	
	tmpInfo = {
		selMsg		: 'ALL',
		cm_useyn	: 'Y',
		gubun		: 'sub',
		itYn		: 'Y',
		requestType	: 'getTeamInfo'
	}
	
	ajaxAsync('/webPage/common/CommonTeamInfo', tmpInfo, 'json', successGetTeamInfo);
}

function successGetTeamInfo(data){
	
	options = [];
	
	$.each(data, function(i, item) {
	    options.push({value : item.cm_deptcd, text : item.cm_deptname});
    });

    $('[data-ax5select="cboDept"]').ax5select({
	    options: options
    });
}

function search(){
	
	var strStD = replaceAllString($("#dateSt").val(), '/', '');
	var strEnD = replaceAllString($("#dateEd").val(), '/', '');
	var strSys = '';
	var strTeam = '';
	var opt1 = '';
	
	if($('#optSel').is(':checked')){
		opt1 = '1';
	} else {
		opt1 = '0';
	}
	
	if(getSelectedIndex('cboSysCd') > 0) strSys = getSelectedVal('cboSysCd').value;
	if(getSelectedIndex('cboDept') > 0) strTeam = getSelectedVal('cboDept').value;
	
	tmpInfo = {
		strStD		: strStD,
		strEnD		: strEnD,
		isrId		: $('#txtIsr').val(),
		opt1		: opt1,
		strSys		: strSys,
		strTeam		: strTeam,
		requestType	: 'getReqList'
	}

	var ajaxResult = ajaxCallWithJson('/webPage/report/SrcRevisionReport', tmpInfo, 'json');  
	
	mainGrid.setData(ajaxResult);
}

function openWindow(type, isrId, isrSub, acptNo){
	
	var cURL = '';
	var nHeight = 740;
    var nWidth  = 1500;
    var isr = isrId + '-' + isrSub;
    var winName = '';
    
	if (type == 1) {
		winName = 'REQ';
		cURL = "/webPage/winpop/PopReqInfo.jsp";
	} else if (type == 2) {
		winName = 'CHG';
		cURL = "/webPage/winpop/PopChgInfo.jsp";
	} else if (type == 3) {
		winName = 'TEST';
		cURL = "/webPage/winpop/PopTestInfo.jsp";
	} else if (type == 4) {
		winName = 'G04';
		cURL = "/webPage/winpop/PopRequestDetail.jsp";
	}
	
	var f = document.popPam;   		
    
    f.acptNo.value	= acptNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.reqCd.value	= reqCd;    	
    f.isrId.value	= isr;    	
    f.user.value 	= userId;    	

    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}





