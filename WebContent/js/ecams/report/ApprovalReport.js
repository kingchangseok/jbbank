/**
 * [보고서 > 대결처리내역/후열미처리내역] 
 */
var userId 		= window.top.userId;
var approvalGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
	

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

approvalGrid.setConfig({
    target: $('[data-ax5grid="approvalGrid"]'),
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
    	{key: "acptno", 			label: "신청번호",  		width: '10%', 	align: "left"},
    	{key: "cm_codename", 		label: "신청구분", 	 	width: '10%', 	align: "left"},
    	{key: "cm_username", 		label: "신청자",  		width: '13%', 	align: "left"},
    	{key: "cr_acptdate", 		label: "신청일시",  		width: '10%', 	align: "left"},
    	{key: "team", 				label: "소속팀",  		width: '20%', 	align: "left"}, //후열 미처리내역  cm_deptname과 동일
        {key: "cr_acptdate",		label: "처리요청일시", 		width: '10%',	align: "left"},
        {key: "cr_prcdate",	 		label: "처리완료일시",  	width: '10%', 	align: "left"},
        {key: "baseusr", 			label: "원결제", 	 		width: '13%', 	align: "left"},
        {key: "confusr", 			label: "대결제",  		width: '13%', 	align: "left"},
        {key: "cr_confdate", 		label: "결제일시", 		width: '13%', 	align: "left"},
        {key: "cr_confname", 		label: "후결팀1", 		width: '13%', 	align: "left"},
        {key: "cr_confname1", 		label: "후결팀2", 		width: '13%', 	align: "left"},
        {key: "cr_confname2", 		label: "후결팀3", 		width: '13%', 	align: "left"}
    ]
});

approvalGrid.setData([]);


$(document).ready(function() {
	
	var oldVal = '';
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
	
	//엑셀저장
	$("#btnExcel").bind('click', function() {
		approvalGrid.exportExcel("대결처리현황_" + today + ".xls");
	});
	
});


//picker 현재 날짜
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, '/');
	$("#dateEd").val(today);
	$("#dateSt").val(today);
});

function search() {
	
	var stDt = replaceAllString($("#dateSt").val(), '/', '');
	var edDt = replaceAllString($("#dateEd").val(), '/', '');
	
	var inputData = new Object();
	inputData.UserId = userId;
	inputData.StDate = stDt;
	inputData.EdDate = edDt;

	var title = $('#history_wrap > strong').text();
	
	if(title.substr(-6, 2) == '대결'){ //대결처리내역
		ajaxData = {
				data 		: inputData,
				requestType : 'getApprovalList'
		}
		
		var ajaxResult = ajaxCallWithJson('/webPage/report/ApprovalReport', ajaxData, 'json');  
		
		console.log("대결처리내역= ", ajaxResult);
		approvalGrid.setData(ajaxResult);
		
	}else { //후열미처리내역
		ajaxData2 = {
				data 		: inputData,
				requestType : 'getApprovalList_2'
		}
		var ajaxResult2 = ajaxCallWithJson('/webPage/report/ApprovalReport', ajaxData2, 'json'); 
		
		console.log("후열미처리내역= ", ajaxResult2);
		approvalGrid.setData(ajaxResult2);
	}
	
}





