/**
 * [보고서 > 주간ISR처리현황/관리자] 
 */
var userId 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
	

picker.bind({
	target: $('[data-ax5picker="picker"]'),
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
	onStateChanged: function () {
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
    target: $('[data-ax5grid="mainGrid"]'),
    sortable: true,
    multiSort: true,
    showLineNumber: true,
    header: {
        align: "center",
        columnHeight: 30
    },
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "cc_isrid", 			label: "ISR-ID",  		width: '10%', 	align: "left"},
    	{key: "deptname", 			label: "요청부서", 	 	width: '10%', 	align: "left"},
    	{key: "cc_isrtitle", 		label: "문서제목",  		width: '20%', 	align: "left"},
    	{key: "cc_createdt", 		label: "수신일",  		width: '10%', 	align: "left"},
    	{key: "cc_reqenddt", 		label: "요청일자",  		width: '10%', 	align: "left"}, 
        {key: "cc_expday",			label: "완료예정", 		width: '10%',	align: "left"},
        {key: "end_date",	 		label: "완료일자",  		width: '10%', 	align: "left"},
        {key: "AA", 				label: "승인계", 	 		width: '10%', 	align: "left"},
        {key: "BB", 				label: "정보계",  		width: '10%', 	align: "left"},
        {key: "DD", 				label: "POS", 			width: '10%', 	align: "left"},
        {key: "CC", 				label: "PG/포인트", 		width: '10%', 	align: "left"},
        {key: "cc_expruntime", 		label: "예상소요시간", 		width: '10%', 	align: "left"},
        {key: "bigo", 				label: "비고", 			width: '17%', 	align: "left"}
    ]
});

mainGrid.setData([]);


$(document).ready(function() {
	
	var oldVal = '';
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#date').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#date").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//조회
	$("#btnSearch").bind('click', function() {
		search();
	});
	
	//엑셀저장
	$("#btnExcel").bind('click', function() {
		approvalGrid.exportExcel("주간ISR처리현황_" + today + ".xls");
	});
	
});


//picker 현재 날짜
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, '/');
	$("#date").val(today);
});

function search() {

	var strStD = replaceAllString($("#date").val(), '/', '');
	var title = $('#history_wrap > strong').text();
	
	if(title.substr(-3, 3) != '관리자'){
		ajaxData = {
				strStD 		: strStD,
				requestType : 'getReqList'
		}
		
		var ajaxResult = ajaxCallWithJson('/webPage/report/WeeklySRReport', ajaxData, 'json');  //일반

		console.log("ISR주간처리현황_일반= ", ajaxResult);
		mainGrid.setData(ajaxResult);
		
	}else {
		ajaxData2 = {
				strStD 		: strStD,
				requestType : 'getReqList_Cmp4010'
		}
		var ajaxResult2 = ajaxCallWithJson('/webPage/report/WeeklySRReport', ajaxData2, 'json'); //관리자
		
		console.log("ISR주간처리현황_관리자= ", ajaxResult2);
		mainGrid.setData(ajaxResult2);
	}
	
}





