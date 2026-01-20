var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var columnData	= 
	[ 
		{key : "cm_sysmsg",label : "시스템",align : "",width: "14%"}, 
		{key : "cm_username",label : "SR요청자",align : "",width: "14%"}, 
		{key : "cnt1",label : "SR등록",align : "",width: "14%"}, 
		{key : "cnt2",label : "SR진행",align : "",width: "14%"}, 
		{key : "cnt3",label : "SR완료",align : "",width: "14%"}, 
		{key : "cnt4",label : "SR취소",align : "",width: "14%"}, 
		{key : "cm_deptname",label : "부서",align : "left",width: "14%"}
	];

picker.bind({
    target: $('[data-ax5picker="basic"]'),
    direction: "top",
    content: {
        width: 220,
        margin: 10,
        type: 'date',
        config: {
            control: {
                left: '<i class="fa fa-chevron-left"></i>',
                yearTmpl: '%s',
                monthTmpl: '%s',
                right: '<i class="fa fa-chevron-right"></i>'
            },
            dateFormat: 'yyyy/MM/dd',
            lang: {
                yearTmpl: "%s년",
                months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
                dayTmpl: "%s"
            }
        },
        formatter: {
            pattern: 'date'
        }
    },
    btns: {
        today: {
            label: "Today", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .close();
            }
        },
        thisMonth: {
            label: "This Month", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
                                + '/'
                                + ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
                        .close();
            }
        },
        ok: {label: "Close", theme: "default"}
    }
});

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : false,
	showRowSelector : false,
	multipleSelect : false,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            selectedItem = this.item;
        },
        trStyleClass: function () {
        	if (this.item.gbn === "T"){
        		return "font-red";
        	} 
        }
	},
	columns : columnData
});

//체크박스
$('input:radio[name=radioGroup]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});

//피커에 오늘 날짜 세팅
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
	$("#dateSt").val(today);
	$("#dateEd").val(today);
})

$(document).ready(function() {
	setDateEnable();
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	//조회
	$("#btnQry").bind('click', function() {
		search();
	});
	
	//엔터조회
	$("#SRrequestorTxt").bind('keypress', function() {
		if(window.event.keyCode == 13) search();
	})
	
	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("요청자별SR현황 " + today + ".xls");
	});
	
});

function search() {
	var inputData = new Object();
	var stDt;
	var edDt;
	var chkDay;
	
	stDt = replaceAllString($("#dateSt").val(), '/', '');
	edDt = replaceAllString($("#dateEd").val(), '/', '');
	inputData = {
		strDt : stDt,
		endDt : edDt,
		srRequestor : $("#SRrequestorTxt").val(),
	}
	ajaxData = {
		data : inputData,
		requestType : "getReqList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/report/SRReportByRequestor', ajaxData, 'json');
	gridDataSet(ajaxResult);
}

function gridDataSet(data) {
	mainGrid.setData(data);
}

function setDateEnable() {
	var today = getDate('DATE',0);
	var lastMonth = getDate('DATE', -7);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
	lastMonth = lastMonth.substr(0,4) + '/' + lastMonth.substr(4,2) + '/' + lastMonth.substr(6,2);
	
	$("#dateSt").val(lastMonth);
	$("#dateEd").val(today);
}

