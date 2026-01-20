var userid 		= window.top.userId;
var codeList    = window.top.codeList;          //전체 코드 리스트
var adminYN 	= window.top.adminYN;		// 관리자여부

var mainGrid	= new ax5.ui.grid();
//var picker	= [new ax5.ui.picker(), new ax5.ui.picker()];
var picker		= new ax5.ui.picker();

var options 	= [];
var cboSinData	= null;
var cboStaData	= null;
var cboApprovalData	= null;

$('[data-ax5select="systemSel"]').ax5select({
	options: []
});
$('[data-ax5select="cboJobSel"]').ax5select({
	options: []
});
$('[data-ax5select="cboSta"]').ax5select({
	options: []
});

//$('[data-ax5select="prcdDivSel"]').ax5select({
//	options: []
//});
var columnData = 
	[ 
		{key : "cm_sysmsg",		label : "시스템",		align : "left",		width: "8%"}, 
		{key : "acptno",			label : "신청번호",	align : "center",	width: "8%"}, 
		{key : "acptdate",		label : "신청일시",	align : "left",		width: "8%"}, 
		{key : "cm_username",		label : "신청인",		align : "center",	width: "8%"}, 
		{key : "cm_svrname",		label : "적용서버",		align : "center",	width: "8%"}, 
		{key : "cm_codename",		label : "프로그램종류",	align : "left",		width: "8%"}, 
		{key : "cr_rsrcname",		label : "프로그램",	align : "left",		width: "8%"},
		{key : "cm_dirpath",		label : "프로그램경로",	align : "left",		width: "8%"}, 
		{key : "sysgbn",		label : "적용구분",	align : "left",		width: "8%"}, 
		{key : "cm_volpath",		label : "적용경로",	align : "left",		width: "8%"}, 
		{key : "rst",		label : "처리결과",	align : "left",		width: "8%"}, 
		{key : "prcdate",	label : "완료일시",	align : "left"}
	];

picker.bind({
    target: $('[data-ax5picker="basic"]'),
    direction: "top",
    content: {
        width: 220,
        margin: 10,
        type: 'date',
        config: {
            control: {
                left: '<i class="fa fa-chevron-left"></i>',
                yearTmpl: '%s',
                monthTmpl: '%s',
                right: '<i class="fa fa-chevron-right"></i>'
            },
            dateFormat: 'yyyy/MM/dd',
            lang: {
                yearTmpl: "%s년",
                months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
                dayTmpl: "%s"
            }
        },
        formatter: {
            pattern: 'date'
        }
    },
    btns: {
        today: {
            label: "Today", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .close();
            }
        },
        thisMonth: {
            label: "This Month", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
                                + '/'
                                + ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
                        .close();
            }
        },
        ok: {label: "Close", theme: "default"}
    }
});

$(document).ready(function() {
	
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
	        onDBLClick: function () {
	         	if (this.dindex < 0) return;
	 			//openWindow(1, this.item.acptno.substr(5,2), this.item.acptno,'');
	        },
		},
		columns : columnData,
//		contextMenu: {
//			iconWidth: 20,
//			acceleratorWidth: 100,
//			itemClickAndClose: false,
//			icons: {
//				'arrow': '<i class="fa fa-caret-right"></i>'
//			},
//			items: [
//				{type: 1, label: "변경신청상세"}
//			],
//			popupFilter: function (item, param) {
//				mainGrid.clearSelect();
//				mainGrid.select(Number(param.dindex));
//	        	 
//	        	var selIn = mainGrid.selectedDataIndexs;
//	        	if(selIn.length === 0) return;
//	        	 
//	         	if (param.item == undefined) return false;
//	         	if (param.dindex < 0) return false;
//	         	
//	         	return item.type == 1;
//			},
//			onClick: function (item, param) {
//				//openWindow(item.type, param.item.cr_qrycd, param.item.acptno,'');
//				openWindow(1, this.item.acptno.substr(5,2), this.item.acptno,'');
//				mainGrid.contextMenu.close();
//			}
//		}
	}); 
	
	//진행상태박스 데이터 세팅
//	$('[data-ax5select="statusSel"]').ax5select({
//		options: [
//			{value: "0", text: "전체"},
//			{value: "1", text: "미완료"},
//			{value: "9", text: "완료"}
//			]
//	});
	
	$('input:radio[name=radioStd]').wRadio({theme: 'circle-classic blue', selector: 'checkmark', highlightLabel: true});
	getSysInfo();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	$("#systemSel").bind("change", function(){
		if($("[data-ax5select='systemSel']").ax5select("getValue")[0].value !== ""){
			getJobCd();
		} else {
			getJobCd2();
		}
	});
})

function getSysInfo() {
	
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userid,
		SecuYn : "N",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_syscd, text : value.cm_sysmsg});
	});
	options[0].value = '';
	
	$('[data-ax5select="systemSel"]').ax5select({
		options: options
	});	
	getDeptInfo();
}

function getDeptInfo() {
	var ajaxData = {
			UserId : userid,
			selMsg			: 'All',
			cm_useyn		: 'Y',
			gubun			: 'main',
			itYn			: 'Y',
			requestType	: 'getTeamInfo'
	};
	ajaxAsync('/webPage/common/CommonTeamInfo', ajaxData, 'json', SuccessGetDeptInfo);
}

function SuccessGetDeptInfo(data) {
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_deptcd, text : value.cm_deptname});
	});
	options[0].value = '';
	
	$('[data-ax5select="cboSta"]').ax5select({
		options: options
	});	
	//getCodeInfo1();
	getJobCd2();
}

function getJobCd2(){
	var sysJobInfo		= new Object();
	sysJobInfo.SelMsg = "ALL";
	sysJobInfo.SecuYn 	= "N";
	
	var sysJobInfoData = {
			sysData	: sysJobInfo,
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/CommonCodeInfo', sysJobInfoData, 'json', successGetSysJobInfo2);
	sysJobInfoData = null;
	sysJobInfo = null;
}

function successGetSysJobInfo2(data){
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_jobcd, text : value.cm_jobname});
	});
	options[0].value = '';
	
	$('[data-ax5select="cboJobSel"]').ax5select({
		options: options
	});	
	
	getCodeInfo1();
}

function getCodeInfo1() {
	/*
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('REQPASS','ALL','N'),
		new CodeInfo('CHECKIN','ALL','N'),
		]);
	cboSinData 		= codeInfos.REQPASS;
	cboStaData		= codeInfos.CHECKIN;
	*/
//	cboSinData = fixCodeList(codeList['REQCD'], 'ALL', 'cm_codename', '', 'N');
//	cboStaData = fixCodeList(codeList['CHECKIN'], 'ALL', 'cm_codename', '', 'N');
//	
//	options = [];
//	options.push({value : "", text : "전체"});
//	$.each(cboStaData, function(i, value) {
//		if (value.cm_micode == '****') {
//		//	options.push({value : "99", text : "신규+수정"});
//		} else {
//			options.push({value : value.cm_micode, text : value.cm_codename});
//		}
//	});
//	//options[0].value = '';
//	//options.splice(1,0, {"text" : "신규+수정", "value" : "99"});
//	$('[data-ax5select="reqDivSel"]').ax5select({
//		options: options
//	});
	
	cboApprovalData = fixCodeList(codeList['APPROVAL'], 'ALL', 'cm_codename', '', 'N');
	options = [];
	options.push({value : "00", text : "전체"});
	$.each(cboApprovalData, function(i, value) {
		if (value.cm_micode == '****') {
		} else {
			options.push({value : value.cm_micode, text : value.cm_codename});
		}
	});
	
	$('[data-ax5select="cboSta"]').ax5select({
        options: options
	});
}

function getJobCd(){
	var sysJobInfo		= new Object();
	sysJobInfo.UserID = userId;
	sysJobInfo.SysCd 	= $("[data-ax5select='systemSel']").ax5select("getValue")[0].value;
	sysJobInfo.selSys 	= $("[data-ax5select='systemSel']").ax5select("getValue")[0].value;
	sysJobInfo.SecuYn 	= 'Y';
	sysJobInfo.CloseYn 	= 'N';
	sysJobInfo.SelMsg 	= 'ALL';
	sysJobInfo.sortCd 	= 'NAME';
	
	var sysJobInfoData = {
		etcData	: sysJobInfo,
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/administrator/SysInfoServlet', sysJobInfoData, 'json', successGetSysJobInfo);
	sysJobInfoData = null;
	sysJobInfo = null;
}

//선택된 시스템 JOB
function successGetSysJobInfo(data) {
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_jobcd, text : value.cm_jobname});
	});
	options[0].value = '';
	
	$('[data-ax5select="cboJobSel"]').ax5select({
		options: options
	});	
}

//picker에 오늘 날짜 디폴트로 세팅
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
	$("#dateEd").val(today);
	$("#dateSt").val(today);
})

//조회 클릭 시
$("#btnSearch").bind('click', function() {
	search();
})

//텍스트박스에서 엔터키 입력 시
$("#reqUserId, #acptNo").bind('keypress', function(event) {
	if(window.event.keyCode == 13) search();
})

//조회
function search() {
	var inputData = new Object();
	var requestType = "";
	
	inputData = {			
		strSys : $("[data-ax5select='systemSel']").ax5select("getValue")[0].value,
		strJob : $("[data-ax5select='cboJobSel']").ax5select("getValue")[0].value,
		strSta : $("[data-ax5select='cboSta']").ax5select("getValue")[0].value,
		strStD : replaceAllString($("#dateSt").val(), '/', ''),
		strEdD : replaceAllString($("#dateEd").val(), '/', ''),
		strReqUsr : $("#reqUserId").val(),
	}
	
	ajaxData = {
			inputData : inputData,
			UserId : userid,
			requestType : "getApplyListStatus"
	}
	
	var ajaxResult = ajaxCallWithJson('/webPage/report/ConfigReqReport', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
}

//엑셀
$("#btnExcel").on('click', function() {
	var st_date = new Date().toLocaleString();
	var today = st_date.substr(0, st_date.indexOf("오"));
	
	mainGrid.exportExcel("적용대상명세 " + today + ".xls");
})

//contextMenu 화면 호출
function openWindow(type,reqCd,reqNo,rsrcName) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userid;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 1) {
		nHeight = 740;
	    nWidth  = 1200;

		cURL = "/webPage/winpop/PopRequestDetail.jsp";
	    
	} else if (type == 2) {
		nHeight = 828;
	    nWidth  = 1046;

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
