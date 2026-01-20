var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;
var rgtList	= window.top.rgtList;

var mainGrid 	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();

var mainGridData	= [];
var cboGbnData 		= [];

$('[data-ax5select="cboGbn"]').ax5select({
    options: []
});

$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('picker', 'top'));

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : false,
	showRowSelector : false,
	multipleSelect : false,
    header: {
    	align: "center"
    },
	body : {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        trStyleClass: function () {
        }
	},
	columns: [
        {key: "keyword",	label: "구분",		width: '10%',	align: 'left'},
        {key: "editor", 	label: "작업자",  	width: '10%', 	align: 'left'},
        {key: "jobdate", 	label: "작업일",  	width: '10%', 	align: 'center'},
        {key: "info", 		label: "작업내용",  	width: '70%', 	align: 'left'}
    ]
});

$(document).ready(function() {
	if (userId == null || userId == '' || userId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	
	//조회
	$("#btnSearch").bind('click', function() {	
		getLogHistory();
	});
	
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
  	    excelExport(mainGrid,"AdminLogHistory.xls");
	});
	
	getCodeInfo();
});

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('LOGMGR','ALL','N')
	]);
	
	cboGbnData = codeInfos.LOGMGR;
	$('[data-ax5select="cboGbn"]').ax5select({
		options: injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
	});
}

function getLogHistory() {
	var data =  new Object();
	data = {
		gbnCd   	: getSelectedVal('cboGbn').cm_micode,
		stDate   	: replaceAllString($("#datStD").val(), '/', ''),
		edDate  	: replaceAllString($("#datEdD").val(), '/', ''),
		requestType : 'getLogHistory'
	}
	
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	ajaxAsync('/webPage/common/eCAMSInfoServlet', data, 'json',successGetLogHistory);	
}

function successGetLogHistory(data) {
	$(".loding-div").remove();
	
	if(data != null && data != '' && data != undefined) {
		if(typeof data == 'string' && data.indexOf("ERROR") > -1) {
			dialog.alert(data);
			mainGridData = [];
			mainGrid.setData([]);
			return;
		}
	}
	
	mainGridData = data;
	mainGrid.setData(mainGridData);
}
