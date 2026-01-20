/**
 * [보고서 > 주간보고자료] 
 */
var userId 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();



picker.bind(defaultPickerInfo('basic', 'top'));

ax5.info.weekNames = [ 
	{label : "일"}, 
	{label : "월"}, 
	{label : "화"}, 
	{label : "수"}, 
	{label : "목"}, 
	{label : "금"}, 
	{label : "토"}
	];

//picker 현재 날짜
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, '/');
	$("#dateEd").val(today);
	$("#dateSt").val(today);
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
    	{key: "CC_ISRID", 			label: "ISR-ID",  	width: '10%', 	align: "left"},
    	{key: "CC_REQDEPT_NAME", 	label: "요청부서", 	width: '10%', 	align: "left"},
    	{key: "CC_ISRTITLE", 		label: "문서제목",  	width: '13%', 	align: "left"},
//    	{key: "CC_CREATEDT", 		label: "작성일자",  	width: '10%', 	align: "left"},
    	{key: "CC_RECVDATE", 		label: "접수일자",  	width: '10%', 	align: "left"},
    	{key: "CC_REQENDDT", 		label: "요청일자",  	width: '20%', 	align: "left"}, 
        {key: "CC_EXPDAY",			label: "완료예정", 	width: '10%',	align: "left"},
        {key: "CLOSE_DT",	 		label: "완료일자",  	width: '10%', 	align: "left"},
        {key: "ETIME1", 			label: "예상시간", 	width: '13%', 	align: "left"},
        {key: "ETIME2", 			label: "비고",  		width: '13%', 	align: "left"}
    ]
});

mainGrid.setData([]);


$(document).ready(function() {
	
	var oldVal = '';
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').on('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//조회
	$("#btnQry").on('click', function() {
		search();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("주간보고자료" + today + ".xls");
	});
	
});



function search() {
	
	var strStD = replaceAllString($("#dateSt").val(), '/', '');
	var strEnD = replaceAllString($("#dateEd").val(), '/', '');
	
	var tmpInfo = {
		strStD		: strStD,
		strEnD		: strEnD,
		requestType	: 'getReqList'
	}
	
	var ajaxResult = ajaxCallWithJson('/webPage/report/WeeklyReport', tmpInfo, 'json');  
	
	mainGrid.setData(ajaxResult);
	
}





