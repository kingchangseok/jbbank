var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부

var mainGrid 	= new ax5.ui.grid();
var cboJobData 	= [];
var columnData 	= [];

var picker = new ax5.ui.picker();
$('#datStD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});
$('[data-ax5select="cboJob"]').ax5select({
	options: []
});

var columnsData = [
	{key : "cm_sysmsg", 	label : "시스템", 	align : "center",	width: "9%"},
	{key : "cm_jobname", 	label : "업무", 		align : "center", 	width: "9%"},
	{key : "beforeCnt", 	label : "<span style='color: red; text-align:center'>전일보유본수</span>", 	align : "right", width: "10%", styleClass: function(){
		return "color-red";
	}},
	{key : "newCnt", 		label : "신규", 		align : "right", 	width: "9%"},
	{key : "ihCnt", 		label : "일괄이행", 	align : "right", 	width: "9%"},
	{key : "closeCnt",		label : "삭제", 		align : "right", 	width: "9%"},
	{key : "chkInCnt", 		label : "변경", 		align : "right", 	width: "9%"},
	{key : "chkOutCnt", 	label : "대여", 		align : "right", 	width: "9%"},
	{key : "chkOutCnclCnt", label : "대여취소", 	align : "right", 	width: "9%"},
	{key : "openCnt", 		label : "<span style='color: red;'>현보유본수</span>", 	align : "right", width: "9%", styleClass: function(){
		return "color-red";
	}},
	{key : "currOutCnt", 	label : "<span style='color: red;'>총대여본수</span>", 	align : "right", width: "10%", styleClass: function(){
		return "color-red";
	}}
];

$(document).ready(function() {
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		showLineNumber : false,
		showRowSelector : false,
		multipleSelect : false,
		sortable: false,
		lineNumberColumnWidth : 40,
		header: {
			align: "center"
	    },
		body : {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	        trStyleClass: function () {
	        	if(this.item.cm_sysmsg == "전체") return "color-red";
	        	if(this.item.cm_jobname == "합계") {
	        		return "color-red";
	        	} else if (this.item.cm_jobname == "소계") {
	        		return "color-red";
	        	}
	        }
		},
		columns : columnsData
	});
	
	getSysinfo();
	$("#btnExcel").hide();

	//엑셀저장
	$("#btnExcel").on('click', function() {
		var today = getDate('DATE',0);
		mainGrid.exportExcel("프로그램보유현황 " + today + ".xls");
	});
	
	//시스템
	$('#cboSys').bind('change', function() {
		cboSysCd_Change();
	});
	
	//조회 클릭 시
	$("#btnSearch").bind('click', function() {
		mainGrid.setData([]);
		getProgList();
	});
})

//시스템 콤보박스 세팅
function getSysinfo() {
	var ajaxData = new Object();
	var ajaxResult = new Object();
	
	ajaxData = {
		UserId : userid,
		SelMsg : "ALL",
		CloseYn : "N",
		SysCd : "",
		requestType	: 'getSysInfo_Rpt'
	}
	
	ajaxResult = ajaxCallWithJson('/webPage/common/SysInfoServlet', ajaxData, 'json');

	var sysInfo = '' + ajaxResult.cm_sysinfo; //substr 사용하기 위해 문자열 처리
	ajaxResult = ajaxResult.filter(function(data) {
		if(sysInfo.substr(0,1) == "1") return false;
		else return true;
	});	
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(ajaxResult, 'cm_syscd' , 'cm_sysmsg')
	});
	
	//업무콤보 사용 안함
	//cboSysCd_Change();
}

function cboSysCd_Change() {
	mainGrid.setData([]);
	selectedIndex = getSelectedIndex('cboSys');
	selectedItem = getSelectedVal('cboSys');
	
	if( selectedItem == null ) return;
	if(selectedIndex > 0) {
		getJobInfo();
	}
}

function getJobInfo() {
	var ajaxData = new Object();
	var ajaxResult = new Object();
	
	ajaxData = {
		UserID : userid,
		SysCd : getSelectedVal('cboSys').cm_syscd,
		CloseYn : "N",
		SelMsg : "ALL",
		requestType	: 'getJobInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', ajaxData, 'json', successGetJobInfo);
}

function successGetJobInfo(data) {
	cboJobData = data;
}

//그리드 데이터 세팅
function getProgList() {
	var syscd = getSelectedVal('cboSys').value;
	//시스템선택값이 전체일 시 모든 시스템 코드 통합해서 전달
	if(syscd == '00000') {
		syscd = "";
	}
	
	var ajaxData = {};
	ajaxData.UserId = userid;
	ajaxData.SysCd  = syscd;
	ajaxData.JobCd  = "";
	ajaxData.StDate = replaceAllString($("#datStD").val(), '/', '');
	ajaxData.requestType = "getProgList";
	
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp0300Servlet', ajaxData, 'json', successGetProgList);
}

function successGetProgList(data){
	$(".loding-div").remove();
	
//	$.each(columnData, function(i, value) {
//		if(i > 1) {
//			$.each(data, function(j, value2) {
//				if(value2[value.key] == undefined) {
//					value2[value.key] = "0";
//				}
//			})
//		}
//	});
	
	data.forEach(function(item) {
		item.beforeCnt = commaNum(item.beforeCnt);
		item.newCnt = commaNum(item.newCnt);
		item.ihCnt = commaNum(item.ihCnt);
		item.closeCnt = commaNum(item.closeCnt);
		item.chkInCnt = commaNum(item.chkInCnt);
		item.chkOutCnt = commaNum(item.chkOutCnt);
		item.chkOutCnclCnt = commaNum(item.chkOutCnclCnt);
		item.openCnt = commaNum(item.openCnt);
		item.currOutCnt = commaNum(item.currOutCnt);
	});
	
	if(data.length > 0) $("#btnExcel").show();
	mainGrid.setData(data);
}
