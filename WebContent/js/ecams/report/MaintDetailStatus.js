var userId 		= window.top.userId;
var dataGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var cboSysData = [];
var dataGridData = null;

dataGrid.setConfig({
	target : $('[data-ax5grid="dataGrid"]'),
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
            selectedItem = this.item;
        },
	},
	columns : 
		[ 
			{key : "cr_itsmid",    label : "프로젝트번호",   width: "10%"}, 
			{key : "cm_sysmsg",    label : "단위업무",      width: "10%", align:'left'}, 
			{key : "gbn",          label : "업무구분",      width: "10%", align:'left'}, 
			{key : "cr_rsrcname",  label : "프로그램명",     width: "15%", align:'left'}, 
			{key : "cm_codename",  label : "프로그램종류",    width: "10%", align:'left'}, 
			{key : "cm_dirpath",   label : "프로그램경로",    width: "15%", align:'left'}, 
			{key : "cr_lastdate",  label : "수정일",        width: "8%"}, 
			{key : "cr_lstver",    label : "버전",         width: "6%"}, 
			{key : "cm_username",  label : "담당자",        width: "6%"}, 
			{key : "cr_acptno",    label : "신청번호",       width: "10%"},
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
        	mainGrid.clearSelect();
        	mainGrid.select(Number(param.dindex));
            return true;
        }
   }
});
ax5.info.weekNames = [
    {label: "일"},
    {label: "월"},
    {label: "화"},
    {label: "수"},
    {label: "목"},
    {label: "금"},
    {label: "토"}
];


$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));

picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

$(document).ready(function() {
	getSysInfo();
	
	//조회
	$("#btnQry").bind('click', function() {
		btnQryClick();
	});
		
	//엑셀
	$("#btnExcel").on('click', function() {
  	    excelExport(dataGridData,userId+"MaintDetailStatus.xls");
	});
	
});

function btnQryClick() {
	var stDate = replaceAllString($("#datStD").val(), '/', '');
	var edDate = replaceAllString($("#datEdD").val(), '/', '');
	
	if (stDate > edDate) {
		dialog.alert('조회기간을 정확하게 선택하여 주십시오.');
		return;
	}
	var data =  new Object();
	data = {
		  	  syscd : getSelectedVal('cboSys').value,
		     StDate : replaceAllString($("#datStD").val(), '/', ''),
		     EdDate : replaceAllString($("#datEdD").val(), '/', ''),
		requestType : 'getDetailList'
	}
	
	$('[data-ax5grid="dataGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp3100Servlet', data, 'json',successGetSelectList);
}
function successGetSelectList(data) {

	$(".loding-div").remove();
	if (typeof(data) == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	dataGridData = data;
	dataGrid.setData(dataGridData);
}
function getSysInfo(){
	var data = new Object();
	data = {
	 	     UserId : userId,
		     SecuYn : "Y",
		     SelMsg : "ALL",
		    CloseYn : "N",
		      ReqCd : "",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysData = data;
	cboSysData = cboSysData.filter(function(data) {
		if (data.cm_sysinfo.substr(0,1) == '1') return false;
		else return true;
	});
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
}