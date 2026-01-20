var userId 	= window.parent.userId;			// 접속자 ID

var picker	 = new ax5.ui.picker();
var fileGrid = new ax5.ui.grid();

var cboSysData = [];
var cboDetCateData = [];

$('#dateSt').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

fileGrid.setConfig({
    target: $('[data-ax5grid="fileGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_micode", 	label: "변경코드",  width: '18%',	align: "left"},
        {key: "cm_codename",label: "내용",	  width: '28%', align: "left"},
        {key: "cnta",		label: "신규본수",  width: '18%',	align: "left"},
        {key: "cntb",		label: "변경본수",  width: '18%', 	align: "left"},
        {key: "sum", 		label: "총계",  	  width: '18%',	align: "left"},
    ]
});

$(document).ready(function() {
	getSysInfo();
	getCodeInfo();
	
	// 조회 
	$('#btnSearch').bind('click' , function(){
		getResult();
	});
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		fileGrid.exportExcel('프로그램 신규 및 변경코드별 내역.xls');
	});
});

function getSysInfo() {
	var data = new Object();
	data = {
		UserId : userId,
		SelMsg : "ALL",
		CloseYn : "n",
		SysCd : "",
		requestType : 'getSysInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysData = data;
	
	cboSysData = cboSysData.filter(function(cboSysData) {
		if(cboSysData.cm_sysinfo.substr(0,1) == '01') return false;
		else return true;
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfoOrdercd('DETCATE', 'SEL','N')]);
	cboDateCateData   = codeInfos.DETCATE;
	
	$('[data-ax5select="cboDetCate"]').ax5select({
		options: injectCboDataToArr(cboDateCateData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function getResult() {
	$("#btnExcel").hide();
	var strSys = "all";
	var strDetCate = "all";
	var strStD = replaceAllString($("#dateSt").val(),"/","");

	if(getSelectedIndex('cboSys') > 0) strSys = getSelectedVal('cboSys').cm_syscd;
	if(getSelectedIndex('cboDetCate') > 0) strDetCate = getSelectedVal('cboDetCate').cm_micode;
	
	var data = new Object();
	data = {
		UserId	 : userId,
		SysCd	 : strSys,
		DecaCd	 : strDetCate,
		StDate   : strStD,
		requestType	: 'getCountList'
	}
	$('[data-ax5grid="fileGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();	
	ajaxAsync('/webPage/ecmp/Cmp0300Servlet', data, 'json', successGetResult);
}

function successGetResult(data) {
	$(".loding-div").remove();
	fileGrid.setData(data);
	
	if(data.length > 0) $("#btnExcel").show();
}
