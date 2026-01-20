var userName 	= window.top.userName;
var userId 		= window.top.userId;
var reqCd 		= window.top.reqCd;
var rgtList		= window.top.rgtList; 
var adminYN		= window.top.adminYN;

var mainGrid 	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();

var mainGridData = [];

picker.bind(defaultPickerInfoLarge('basic', 'top'));

var columnData1 = 
	[
		{key: "DEPTNAME",	label : "요청부서명",		align : "left", 	width : "15%"},
		{key: "RATENAME",	label : "개발실적등급",		align : "left", 	width : "15%"},
		{key: "RATECNT",	label : "SR 총 개수",		align : "left", 	width : "15%"}
	]

var columnData2 =
	[
		{key: "DEPTNAME",	label : "요청부서명",		align : "left", 	width : "14%"},
		{key: "RATENAME",	label : "개발실적등급",		align : "left", 	width : "14%"},
		{key: "RATECNT",	label : "SR 총 개수",		align : "left", 	width : "14%"},
		{key: "DEVDEPT",	label : "개발부서",		align : "left", 	width : "14%"},
		{key: "DOCID",		label : "GENIE번호",		align : "left", 	width : "15%"},
		{key: "SRID",		label : "SR-ID",		align : "left", 	width : "15%"},
		{key: "SRTITLE",	label : "SR제목",			align : "left", 	width : "14%"}
	]

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
        },
	},
	columns : columnData1
}); 

$('#chkDetail').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	
	$("#dateStD").val(getDate('DATE', 0));
	
	// 엑셀
	$("#btnExcel").on('click', function() {
		var today = getDate('DATE', 0);
		
		if($('#chkDetail').is(":checked")) {
			mainGrid.exportExcel("요청부서별_처리완료SR_등급별_조회_상세" + today + ".xls");
		}else{
			mainGrid.exportExcel("요청부서별_처리완료SR_등급별_조회_" + today + ".xls");
		}
	});
	
	// 조회 버튼 클릭
	$('#btnQry').bind('click', function() {
		getDeptCnt();
	});
	
});

function getDeptCnt() {
	var qryDate = '';
	qryDate = replaceAllString($("#dateStD").val(), '/', '').substr(0,6);
	
	if($('#chkDetail').is(":checked")) {
		var data = new Object();
		data = {
			Month : qryDate,
			requestType : 'getDeptCntDetail_new'
		}
		ajaxAsync('/webPage/ecmp/Cmp1400Servlet', data, 'json', successGetDeptCntDetail);
	}else {
		var data = new Object();
		data = {
			Month : qryDate,
			requestType : 'getDeptCnt_new'
		}
		ajaxAsync('/webPage/ecmp/Cmp1400Servlet', data, 'json', successGetDeptCnt);
	}
}

function successGetDeptCntDetail(data) {
	mainGridData = data;

	mainGrid.setConfig({
		columns : null
	});
	mainGrid.setConfig({
		columns : columnData2
	});
	mainGrid.setData(mainGridData);
}

function successGetDeptCnt(data) {
	mainGridData = data;
	mainGrid.setConfig({
		columns : null
	});
	mainGrid.setConfig({
		columns : columnData1
	});
	
	mainGrid.setData(mainGridData);
}

