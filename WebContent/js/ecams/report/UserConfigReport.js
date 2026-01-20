var userId = window.top.userId;
var mainGrid = new ax5.ui.grid();

$('[data-ax5select="cboFind"]').ax5select({
	option: []
});
$('[data-ax5select="cboDetail"]').ax5select({
	option: []
})

$(document).ready(function(){
	setGrid();
	CboFind();
	
	$("#cboDetail").ax5select("disable");
	
	$('#cboFind').bind('change', function() {
		mainGrid.setData([]);
		getCodeInfo();
	});
	$("#btnSearch").on('click', function() {
		cboFind_click();
	});
	
	$("#btnExcel").on('click', function() {
		var today = getDate('DATE',0);
		mainGrid.exportExcel("사용자환경_ " + today + ".xls");
	});
});

function getCodeInfo(){
	
	var cboData = getSelectedVal("cboFind").cm_micode;
	if(cboData == 0){
		$("#cboDetail").ax5select("disable");
	}
	else if(cboData == 1){
		$("#cboDetail").ax5select("enable");
		getDetail();
	}else if(cboData == 2){
		$("#cboDetail").ax5select("enable");
		getDetail();
	}
	else if(cboData == 3){
		$("#cboDetail").ax5select("enable");
		getSysInfo();
	}
	else if(cboData == 4){
		$("#cboDetail").ax5select("enable");
		getCboFind();
	}
	
}

function getDetail(){
	tmpInfoData = new Object();
	tmpInfoData = {
			GubunCd : getSelectedVal("cboFind").cm_micode,
		requestType : 'getDetail'
	}
	ajaxAsync('/webPage/ecmp/Cmp1800Servlet', tmpInfoData, 'json',successGetDetail);
}
function successGetDetail(data){
	$('#cboDetail').ax5select({
		options : injectCboDataToArr(data, 'cm_micode', 'cm_codename')
	});
}

function CboFind(){
	
	var cboData = [
		{cm_micode : "0", cm_codename : "전체"},
		{cm_micode : "1", cm_codename : "부서"},
		{cm_micode : "2", cm_codename : "소속조직"},
		{cm_micode : "3", cm_codename : "시스템"},
		{cm_micode : "4", cm_codename : "직위"},
	] 
	$('#cboFind').ax5select({
		options : injectCboDataToArr(cboData, 'cm_micode', 'cm_codename')
		
	});
}

//시스템
function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SecuYn : 'y',
		SelMsg : 'ALL',
		CloseYn : 'n',
		ReqCd : '',
		requestType : 'getSysInfo'
	}

	ajaxAsync('/webPage/ecmp/Cmp1800Servlet', tmpInfoData, 'json',successGetSysInfo);
}
function successGetSysInfo(data) {
console.log(data);
	$('#cboDetail').ax5select({
		options : injectCboDataToArr(data, 'cm_micode', 'cm_codename')
		
	});
}

function getCboFind(data){
	var codeInfos = getCodeInfoCommon( [new CodeInfo('POSITION','ALL','N') ]);
	var data = codeInfos.POSITION;

	$('#cboDetail').ax5select({
		options : injectCboDataToArr(data, 'cm_micode', 'cm_codename')
	});
}

function cboFind_click(){
	data = new Object();
	data = {
		gubun: getSelectedVal("cboFind").cm_micode,
		detail: getSelectedVal("cboFind").cm_micode == "0"? "": getSelectedVal("cboDetail").cm_micode,
		requestType : 'getReqList'
	}

	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
		
	ajaxAsync('/webPage/ecmp/Cmp1800Servlet', data, 'json',successCboFindClick);
	
}

function successCboFindClick(data){

	$(".loding-div").remove();
	mainGrid.setData(data);
}

function setGrid(){
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		sortable : true,
		showLineNumber : true,
		showRowSelector : false,
		multipleSelect : true,
		LineNumberColumWidth : 40,
		rowSelectorColumWidth : 27,
		header : {align: "center"},
		body : {
			onClick: function(){
				this.self.select(this.dindex); // 클릭한 row 선택
				selectedItem = this.item;
	        },
		},
		columns : [
			{key : "cm_username",	label : "사용자명",	width: "10%"}, 
			{key : "cm_userid",     label : "사용자번호",  width: "10%",	align : "center"}, 
			{key : "cm_deptname",   label : "부서",      	width: "8%",	align : "left"}, 
			{key : "sysmsg",        label : "시스템",    	width: "25%",	align : "left"}, 
			{key : "position",      label : "직위",      	width: "8%"}, 
			{key : "rgtname",       label : "담당직무",   	width: "15%",	align : "left"}, 
			{key : "cm_active",     label : "활성화여부",  width: "10%",	align : "left"}, 
			{key : "cm_retireyn",   label : "퇴사여부",   	width: "10%"}, 
		]
	});
}