/**
/webPage/common/SysInfoServlet * [보고서 > 형상관리신청현황] 
 */
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var options 	= [];
var cboSinData	= null;
var cboReqDivData	= null;
var cboPrcdData = null;

$('[data-ax5select="cboSysCd"]').ax5select({
	options: []
});
$('[data-ax5select="cboSin"]').ax5select({
	options: []
});
$('[data-ax5select="cboDept"]').ax5select({
	options: []
});
$('[data-ax5select="step_combo"]').ax5select({
	options: []
});
$('[data-ax5select="cboGbn"]').ax5select({
	options: []
});

var columnData = 
	[ 
		{key : "cm_sysmsg",		label : "시스템",		align : "left",		width: "7%"}, 
		{key : "spms",			label : "SR-ID",	align : "left",		width: "7%"},
		{key : "cm_deptname",	label : "신청부서",	align : "left",		width: "4%"}, 
		{key : "acptno",		label : "신청번호",	align : "center",	width: "7%"}, 
		{key : "cm_username",	label : "신청자",		align : "center",	width: "4%"},
		{key : "cr_passcd",		label : "신청사유",	align : "left",		width: "11%"},
		{key : "cr_acptdate",	label : "신청일시",	align : "center",	width: "8%"}, 
		{key : "cr_prcdate",	label : "완료일시",	align : "left",		width: "8%"},
		{key : "requestgb",		label : "신청구분",	align : "center",	width: "4%"}, 
		{key : "sta",			label : "진행상태",	align : "center",	width: "4%"}, 
		{key : "gbn",			label : "처리구분",	align : "center",	width: "4%"}, 
		{key : "cm_dirpath",	label : "디렉토리",	align : "left",		width: "16%"}, 
		{key : "cr_rsrcname",	label : "프로그램명",	align : "left",		width: "16%"}
	];

$('#dateSt').val(getDate('DATE',-1));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

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
	columns : columnData
	}); 

$(document).ready(function() {
	
	$('input:radio[name=radioGroup]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	$('input.checkbox-user').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
	$('#ReCk').wCheck('check', true);
	$('input:radio[name=radioStd]').wRadio({theme: 'circle-classic blue', selector: 'checkmark', highlightLabel: true});
	getSysInfo();
	search();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});

})

function getSysInfo() {
	var data = new Object();
	data = {
		UserId : userid,
		SecuYn : 'N',
		SelMsg : 'ALL',
		CloseYn : 'N',
		ReqCd : '',
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	console.log(data);
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_syscd, text : value.cm_sysmsg});
	});
	options[0].value = '';
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options: options
	});	
	getCodeInfo();
}

function getDeptInfo() {
	var ajaxData = {
		UserId : userid,
		SelMsg : 'All',
		cm_useyn : 'Y',
		gubun : 'sub',
		itYn : 'N',
		requestType : 'getTeamInfoGrid2'
	};
	ajaxAsync('/webPage/common/TeamInfoServlet', ajaxData, 'json', SuccessGetDeptInfo);
}

function SuccessGetDeptInfo(data) {
	var cboDeptData = data;
	if (cboDeptData != null && (cboDeptData.length > 0)) {
		cboOptions = [];
		$.each(cboDeptData, function(key, value) {
			cboOptions.push({
				value : value.cm_deptcd,
				text : value.cm_deptname
			});
		})

		$('[data-ax5select="cboDept"]').ax5select({
			options : cboOptions
		});
	}
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfo('CHECKIN','ALL','N'),
										new CodeInfo('REQPASS','ALL','N')]);
	cboReqDivData		= codeInfos.CHECKIN;
	cboPrcdData 		= codeInfos.REQPASS;
	
	options = [];
	$.each(cboReqDivData, function(i, value) {
		options.push({value : value.cm_micode, text : value.cm_codename});
	});
	options[0].value = '';
	options.push({value : "99", text : "신규+수정"});
	
	$('[data-ax5select="cboSin"]').ax5select({
		options: options
	})
	$('[data-ax5select="cboGbn"]').ax5select({
		options : injectCboDataToArr(cboPrcdData, 'cm_micode', 'cm_codename')
	});
	
	//진행상태박스 데이터 세팅
	$('[data-ax5select="step_combo"]').ax5select({
		options: [
			{value: "0", text: "전체"},
			{value: "1", text: "미완료"},
			{value: "9", text: "완료"}
		]
	});	
	$('[data-ax5select="step_combo"]').ax5select('setValue', '0', true);
	
	getDeptInfo();
}

//조회 클릭 시
$("#btnSearch").bind('click', function() {
	search();
})

//텍스트박스에서 엔터키 입력 시
$("#txtSpms, #txtUser, #txtPrgName").bind('keypress', function(event) {
	if(window.event.keyCode == 13) search();
})

//조회
function search() {
	var strSys = null;
	var strQry = null;
	var strDept = null;
	var strJob = null;
	var strStD	= null;
	var strEdD	= null;
	var strGbn	=null;
	var strPrc	= null;
	
	var strStD = replaceAllString($("#dateSt").val(), '/', '');
	var strEdD = replaceAllString($("#dateEd").val(), '/', '');

	if (Number(strStD) > Number(strEdD)) {
		dialog.alert("조회기간을 정확하게 선택하여 주십시오.");
		return;
	}

	if (getSelectedIndex('cboSysCd') > 0) strSys 	=	getSelectedVal('cboSysCd').value;
	if (getSelectedIndex('cboJob') > 0) strJob 		= 	getSelectedVal('cboJob').value;
	if (getSelectedIndex('cboSin') > 0) strQry 		= 	getSelectedVal('cboSin').value;
	if (getSelectedIndex('cboDept') > 0) strDept 	= 	getSelectedVal('cboDept').value;
	if (getSelectedIndex('cboGbn') > 0) strGbn 		= 	getSelectedVal('cboGbn').value;
	if (getSelectedIndex('step_combo') > 0) strPrc 	= 	getSelectedVal('step_combo').value;

	if($('#radioCkOut').is(':checked')) {
		dategbn = "0";
	} else {
		dategbn = "1";
	}
	var data = {
			StDate : replaceAllString($("#dateSt").val(), '/', ''),
			EdDate : replaceAllString($("#dateEd").val(), '/', ''),
			Txt_PrgName : $("#txtPrgName").val() == '' ? null : $("#txtPrgName").val(),
			Cbo_SysCd : strSys,
			Cbo_JobCd : strJob,
			Cbo_Steam : strDept,
			Txt_UserId : $("#txtUser").val() == '' ? null : $("#txtUser").val(),
			Cbo_Sin : strQry,
			gbn : strGbn,
			proc : strPrc,
			spms : $("#txtSpms").val() == '' ? null : $("#txtSpms").val(),
			dategbn : dategbn,			
			requestType : "get_SelectList"
	}
	console.log(data);
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp0600Servlet', data, 'json', successGetList);

}

function successGetList(data) {
	$(".loding-div").remove();
	mainGrid.setData(data);
}
