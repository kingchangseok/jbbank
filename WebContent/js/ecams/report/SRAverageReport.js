var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var columnData	= 
	[ 
		{key : "msg",label : "구분",align : "",width: "15%"}, 
		{key : "dateGbn",label : "기간",align : "",width: "18%"}, 
		{key : "dayCnt",label : "SR등록",align : "",width: "20%"}, 
		{key : "endCnt",label : "SR완료",align : "",width: "20%"}, 
		{key : "cnclCnt",label : "SR취소",align : "",width: "20%"} 
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
	
	//월별/일별 클릭
	$("input[name=radioGroup]").bind('click', function(e) {
		radioClick(e);
	})
	
	//조회
	$("#btnQry").bind('click', function() {
		search();
	});
	
	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("장기체크아웃현황 " + today + ".xls");
	});
});

function radioClick(e) {
	var division = {key : "msg",label : "구분",align : "",width: "15%"};
	mainGrid.setConfig({columns : null});
	if(e.target.id == "opt1") {
		if(columnData.length < 5) columnData.unshift(division);
		mainGrid.setConfig({
			target : $('[data-ax5grid="mainGrid"]'),
			columns : columnData
		});
	} else {
		if(columnData.length > 4) columnData.shift();		
		mainGrid.setConfig({
			target : $('[data-ax5grid="mainGrid"]'),
			columns : columnData
		});		
	}
	search();
}

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
		opt1 : $("#opt1").is(":checked") + ""
	}
	ajaxData = {
		data : inputData,
		requestType : "getReqList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/report/SRAverageReport', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
}

function setDateEnable() {
	var today = getDate('DATE',0);
	var lastMonth = getDate('DATE', -30);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
	lastMonth = lastMonth.substr(0,4) + '/' + lastMonth.substr(4,2) + '/' + lastMonth.substr(6,2);
	$("#dateSt").val(today);
	$("#dateEd").val(today);
}

