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
			{key : "cm_sysmsg",    label : "시스템명",    width: "15%", align:'left'}, 
			{key : "cm_jobname",   label : "업무명",     width: "15%", align:'left'}, 
			{key : "cm_userid",    label : "사용자ID",   width: "10%"}, 
			{key : "cm_username",  label : "사용자명",    width: "15%"}, 
			{key : "cm_deptname",  label : "팀명",       width: "15%", align:'left'}, 
			{key : "duty",         label : "직위",       width: "15%"}, 
			{key : "position",     label : "직급",       width: "15%"}, 
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
        },
        onClick: function (item, param) {
        	mainGrid.contextMenu.close();
        	openWindow(item.type, 'win', '', param.item);
        }
   }
});

$(document).ready(function() {
	getSysInfo();
	
	//조회
	$("#btnQry").bind('click', function() {
		btnQryClick();
	});
		
	//엑셀
	$("#btnExcel").on('click', function() {
  	    excelExport(dataGrid,userId+"SysUserList.xls");
	});
	
});

function btnQryClick() {
	var data =  new Object();
	data = {
			  Syscd : getSelectedVal('cboSys').value,
		requestType : 'getUser'
	}
	
	$('[data-ax5grid="dataGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp3300Servlet', data, 'json',successGetSelectList);
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