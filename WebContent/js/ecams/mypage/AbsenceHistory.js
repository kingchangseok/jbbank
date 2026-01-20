var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var absenteeData = null;
var columnData	= 
	[ 
		{key : "cm_userid",label : "사용자번호",align : "left",width: "9%"}, 
		{key : "cm_username",label : "사용자명",align : "left",width: "9%"}, 
		{key : "cm_deptname",label : "조직",align : "left",width: "9%"}, 
		{key : "cm_position",label : "직위",align : "left",width: "9%"}, 
		{key : "cm_blankcd",label : "부재구분",align : "left",width: "9%"}, 
		{key : "cm_blkstdate",label : "부재시작일시",align : "left",width: "9%"}, 
		{key : "cm_blkeddate",label : "부재종료일시",align : "left",width: "9%"}, 
		{key : "cm_blkcldate",label : "부재해제일시",align : "left",width: "9%"}, 
		{key : "cm_blkcont",label : "부재사유",align : "left",width: "9%"}, 
		{key : "cm_daeuser",label : "대결자",align : "left",width: "9%"}
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
	columns : columnData
});

//체크박스
$('input:radio[name=radioGroup]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	setDateEnable();
	getAbsenceInfo();
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
	$("#absenteeTxt").bind('keypress', function(e) {
		if(window.event.keyCode == 13) searchAbsentee(e.target.id);
	})
	
	$("#subApproverTxt").bind('keypress', function(e) {
		if(window.event.keyCode == 13) searchAbsentee(e.target.id);
	})
	
});

function searchAbsentee(id) {
	var target = $("#" + id);
	var userName = target.val();
	var combo = id.substr(0, id.length -3) + "Sel";
	$.each(absenteeData, function(i, value) {
		if(value.cm_username == userName) {
			$('[data-ax5select="' + combo + '"]').ax5select("setValue", value.value, true);
		}
	})
}

function search() {
	var inputData = new Object();
	var stDt;
	var edDt;
	var dateGbn = $("#radioCkOut").is(":checked") ? 0 : 1;
	
	stDt = replaceAllString($("#dateSt").val(), '/', '');
	edDt = replaceAllString($("#dateEd").val(), '/', '');
	console.log($('[data-ax5select="absenteeSel"').ax5select("getValue"));
	inputData = {
		strStD : stDt,
		strEdD : edDt,
		dateGbn : dateGbn,
		userId : $('[data-ax5select="absenteeSel"').ax5select("getValue")[0].cm_userid,
		subApproverId : $('[data-ax5select="subApproverSel"').ax5select("getValue")[0].cm_userid
	}
	ajaxData = {
		data : inputData,
		requestType : "getAbsenteeList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/mypage/AbsenceHistory', ajaxData, 'json');
	console.log(ajaxResult);
	mainGrid.setData(ajaxResult);
}

function getAbsenceInfo() {
	var inputData =  new Object();
	var data = new Object();
	inputData = {
		selMsg : "ALL"
	}
	data = {
		requestType	: 'getAbsenceInfo',
		selMsg : "ALL"
	}
		ajaxAsync('/webPage/mypage/AbsenceHistory', data, 'json', successGetAbsenceInfo);
}

function successGetAbsenceInfo(data) {
	absenteeData = data;
	console.log(absenteeData);
	$('[data-ax5select="absenteeSel"]').ax5select({
        options: injectCboDataToArr(absenteeData, 'cm_userid' , 'username')
	});
	$('[data-ax5select="subApproverSel"]').ax5select({
		options: injectCboDataToArr(absenteeData, 'cm_userid' , 'username')
	});
}

function setDateEnable() {
	var today = getDate('DATE',0);
	var lastMonth = getDate('DATE', -30);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
	lastMonth = lastMonth.substr(0,4) + '/' + lastMonth.substr(4,2) + '/' + lastMonth.substr(6,2);
	$("#dateSt").val(lastMonth);
	$("#dateEd").val(today);
}

