var userId 	= window.parent.userId;			// 접속자 ID

var picker	 = new ax5.ui.picker();
var fileGrid = new ax5.ui.grid();


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
        {key: "cm_codename",    label: "내용",	  width: '28%', align: "left"},
        {key: "opencnt",		label: "신규",     width: '12%',	align: "right"},
        {key: "updtcnt",		label: "변경",     width: '12%', 	align: "right"},
        {key: "allreq", 		label: "일괄이행",  width: '12%',	align: "right"},
        {key: "delreq", 		label: "삭제",  	  width: '12%',	align: "right"},
        {key: "ckout", 		    label: "대여",  	  width: '12%',	align: "right"},
        {key: "ckoutcncl", 	    label: "대여취소",  width: '12%',	align: "right"},
    ]
});

$(document).ready(function() {
	getCodeInfo();
	
	// 조회 
	$('#btnSearch').bind('click' , function(){
		getResult();
	});
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		fileGrid.exportExcel(userId+'_PrgChgMemoReport.xls');
	});
});

function getResult() {
	$("#btnExcel").hide();
	
	var tmpData = new Object();
	tmpData.userid = userId;
	tmpData.baseday = replaceAllString($("#dateSt").val(),"/","");
	var data = new Object();
	data = {
		etcData	 : tmpData,
		requestType	: 'getCountList_vertical'
	}
	$('[data-ax5grid="fileGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();	
	ajaxAsync('/webPage/ecmp/Cmp0300Servlet', data, 'json', successGetResult);
	
	tmpData = null;
}

function successGetResult(data) {
	$(".loding-div").remove();
	fileGrid.setData(data);
	
	if(data.length > 0) $("#btnExcel").show();
}


function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfoOrdercd('DETCATE', 'SEL','N')]);
	cboDateCateData   = codeInfos.DETCATE;
	
	$('[data-ax5select="cboDetCate"]').ax5select({
		options: injectCboDataToArr(cboDateCateData, 'cm_syscd' , 'cm_sysmsg')
	});
}