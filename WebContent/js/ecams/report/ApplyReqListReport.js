var userId 		= window.parent.userId;			// 접속자 ID

var picker			= new ax5.ui.picker();
var fileGrid		= new ax5.ui.grid();

$('#dateSt').val(getDate('DATE',0));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

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
        {key: "cm_sysmsg", 		label: "시스템",  		width: '12%',	align: "left"},
        {key: "cm_deptname",	label: "해당팀",  		width: '8%', 	align: "left"},
        {key: "cm_codename",	label: "구분",  			width: '10%',	align: "left"},
        {key: "cr_rsrcname",	label: "프로그램명",  		width: '23%', 	align: "left"},
        {key: "cr_prcdate", 	label: "해당일자",  		width: '6%',	align: "center"},
        {key: "cm_username",	label: "요청자",  		width: '6%', 	align: "center"},
        {key: "cr_sayu",  		label: "프로그램 변경 사유",	width: '19%', 	align: "left"},
        {key: "checkin",  		label: "신청구분",			width: '7%', 	align: "center"},
        {key: "passok",  		label: "변경구분",			width: '7%', 	align: "center"},
    ]
});

$(document).ready(function() {
	// 조회 
	$('#btnSearch').bind('click' , function(){
		getResult();
	});
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		fileGrid.exportExcel('적용신청내역.xls');
	});
	
	var oldVal = "";
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	geteCAMSDir();
});

function getResult() {
	var strStD = $("#dateSt").val();
	var strEdD = $("#dateEd").val();
	var emgSw = "";
	
	if($("#chkEmg").is(":checked")) emgSw = "2";
	else emgSw = "0";
	
	var data = new Object();
	data = {
		strQryCd : "04",
		UserId	 : userId,
		sDate  	 : strStD,
		eDate 	 : strEdD,
		UserNo 	 : "",
		strDept  : "",
		ProgName : "",
		emgSw 	 : emgSw, 
		requestType	: 'getReqList_Emg'
	}
	$('[data-ax5grid="fileGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();	
	ajaxAsync('/webPage/ecmr/Cmr3400Servlet', data, 'json', successGetResult);
}

function successGetResult(data) {
	$(".loding-div").remove();
	fileGrid.setData(data);
}

function geteCAMSDir() {
	var data = {
		pCode : "99",
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json', successGeteCAMSDir);
}

function successGeteCAMSDir(data) {
	var tmpArray = new Array();
	var tmpPath = data; 
	
	tmpArray = data;
	if (tmpArray.length > 0) {
		for(var i = 0; i < tmpArray.length; i++) {
			if(tmpArray[i].cm_pathcd == "F2") {
				var strUpUrl = tmpArray[i].cm_path;
			} else if (tmpArray[i].cm_pathcd == "21") {
				var attPath = tmpArray[i].cm_path;
			} else {
				tmpPath = tmpArray[i].cm_path;
			}
		}
	} 
}
