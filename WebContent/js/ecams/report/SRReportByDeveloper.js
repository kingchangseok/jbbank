var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var columnData	= 
	[ 
		{key : "cm_sysmsg",label : "시스템",align : "",width: "8%"}, 
		{key : "cm_username",label : "개발담당자",align : "",width: "8%"}, 
		{key : "cnt1",label : "개발계획서 작성대기",align : "",width: "8%"}, 
		{key : "cnt2",label : "개발계획서 결재대기",align : "",width: "8%"}, 
		{key : "cnt3",label : "개발계획서 작성완료",align : "",width: "8%"}, 
		{key : "cnt4",label : "개발중",align : "",width: "8%"}, 
		{key : "cnt5",label : "개발완료",align : "",width: "8%"}, 
		{key : "cnt6",label : "SR완료",align : "",width: "8%"}, 
		{key : "endsum",label : "SR완료(시간)",align : "",width: "8%"}, 
		{key : "endfile",label : "SR완료(이관본수)",align : "",width: "8%"}, 
		{key : "cnt7",label : "SR취소",align : "",width: "8%"}, 
		{key : "cm_deptname",label : "부서",align : "left",width: "8%"}, 

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
	$("#developerTxt").bind('keypress', function() {
		if(window.event.keyCode == 13) search();
	})
	
	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("개발담당자별SR현황 " + today + ".xls");
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
		developer : $("#developerTxt").val(),
		userId  : userid
	}
	ajaxData = {
		data : inputData,
		requestType : "getReqList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/report/SRReportByDeveloper', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
}

function setDateEnable() {
	var today = getDate('DATE',0);
	var lastMonth = getDate('DATE', -7);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
	lastMonth = lastMonth.substr(0,4) + '/' + lastMonth.substr(4,2) + '/' + lastMonth.substr(6,2);
	
	$("#dateSt").val(lastMonth);
	$("#dateEd").val(today);
}

