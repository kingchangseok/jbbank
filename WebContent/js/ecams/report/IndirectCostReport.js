/**
 * [보고서 > 간접비 배부자료] 
 */
var userId 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
	
ax5.info.weekNames = [ 
	{label : "일"}, 
	{label : "월"}, 
	{label : "화"}, 
	{label : "수"}, 
	{label : "목"}, 
	{label : "금"}, 
	{label : "토"}
 ];

picker.bind({
	target: $('[data-ax5picker="picker"]'),
	direction: "top",
	content: {
		width: 220,
		margin: 10,
		type: 'date',
		config: {
			mode:"year",
			selectMode: "year",
			control: {
				left: '<i class="fa fa-chevron-left"></i>',
				yearTmpl: '%s',
				monthTmpl: '%s',
				right: '<i class="fa fa-chevron-right"></i>'
			},
			dateFormat: 'yyyy',
			lang: {
				yearTmpl: "%s년"
//				months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
			},dimensions: {
				height: 140,
				width : 75,
				colHeadHeight: 11,
				controlHeight: 25,
			}
		},
		formatter: {
			pattern: 'date(year)'
		}
	},
	onStateChanged: function () {
        if (this.state == "open") {
            var selectedValue = this.self.getContentValue(this.item["$target"]);
            if (!selectedValue) {
                this.item.pickerCalendar[0].ax5uiInstance.setSelection([ax5.util.date(new Date(), {'add': {d: 0}})]);
            }
        }
    },
	btns: {
		thisMonth: {
			label: "This Years", onClick: function () {
				var today = new Date();
				this.self
				.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy"}))
				.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy"})
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
    	{key: "cc_isrsub", 			label: "SUB-ID", 	 	width: '10%', 	align: "left"},
    	{key: "cc_isrtitle", 		label: "ISR제목",  		width: '20%', 	align: "left"},
    	{key: "cc_reqdept", 		label: "요청팀",  		width: '10%', 	align: "left"},
    	{key: "cc_reqdept_name", 	label: "요청팀명",  		width: '10%', 	align: "left"}, 
        {key: "cc_reqpart",			label: "요청파트", 		width: '10%',	align: "left"},
        {key: "cc_reqpart_name",	label: "요청파트명",  		width: '10%', 	align: "left"},
        {key: "cc_editor", 			label: "요청자", 	 		width: '10%', 	align: "left"},
        {key: "cc_editor_name", 	label: "요청자명",  		width: '10%', 	align: "left"},
        {key: "cc_creatdt", 		label: "등록일", 			width: '10%', 	align: "left"},
        {key: "cc_reqenddt", 		label: "요청일", 			width: '10%', 	align: "left"},
        {key: "cc_recvdate", 		label: "접수일", 			width: '10%', 	align: "left"},
        {key: "cc_expday", 			label: "예정일", 			width: '10%', 	align: "left"},
        {key: "cc_jobdate", 		label: "작업일", 			width: '10%', 	align: "left"},
        {key: "cc_realedday", 		label: "완료일", 			width: '10%', 	align: "left"},
        {key: "cc_edcomdt", 		label: "종료일", 			width: '10%', 	align: "left"},
        {key: "cc_jobtime", 		label: "실작업일", 		width: '10%', 	align: "left"},
        {key: "cc_satiscd", 		label: "만족도코드", 		width: '10%', 	align: "left"},
        {key: "cc_satiscd_name", 	label: "사용자만족도",		width: '10%', 	align: "left"},
        {key: "cc_recvpart", 		label: "접수처", 			width: '10%', 	align: "left"},
        {key: "cc_recvpart_name", 	label: "접수처명", 		width: '10%', 	align: "left"},
        {key: "cc_rfcuser", 		label: "변경관리자", 		width: '10%', 	align: "left"},
        {key: "cc_rfcuser_name", 	label: "변경관리자명", 		width: '10%', 	align: "left"},
        {key: "cc_scmuser", 		label: "변경담당자", 		width: '10%', 	align: "left"},
        {key: "cc_scmuser_name", 	label: "변경담당자명", 		width: '10%', 	align: "left"},
        {key: "cc_catetype", 		label: "작업유형코드", 		width: '10%', 	align: "left"},
        {key: "cc_catetype_name", 	label: "작업유형명", 		width: '10%', 	align: "left"},
        {key: "cc_detcate", 		label: "작업종류코드", 		width: '10%', 	align: "left"},
        {key: "cc_datecate_name", 	label: "작업종류명", 		width: '10%', 	align: "left"},
        {key: "cc_expruntime", 		label: "예상시간", 		width: '10%', 	align: "left"},
        {key: "cc_expmm", 			label: "예샹시간공수", 		width: '10%', 	align: "left"},
        {key: "cc_realtime", 		label: "투입시간", 		width: '10%', 	align: "left"},
        {key: "cc_realmm", 			label: "투입시간공수", 		width: '10%', 	align: "left"},
        {key: "cc_scmuser_cnt", 	label: "변경담당자수", 		width: '10%', 	align: "left"},
        {key: "cc_dev_time", 		label: "담당자별 개별시간", 	width: '17%', 	align: "left"}
    ]
});

mainGrid.setData([]);


$(document).ready(function() {
	
	var oldVal = '';
	$('#date').on('propertychange change keyup paste input', function() {
		var currentVal =  $("#date").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//조회
	$("#btnSearch").on('click', function() {
		search();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		approvalGrid.exportExcel("간접비배부자료_" + today + ".xls");
	});
	
});


//picker 현재 날짜
$(function() {
	var today = new Date().toISOString().substring(0,4);
	$("#date").val(today);
});

function search() {

	var chkDay = '';
	if($('#opt1').is(':checked')){
		chkDay = '1';
	} else if($('#opt2').is(':checked')){
		chkDay = '2';
	}
	
	var tmpInfo = {
			qryDt 		: $('#date').val(),
			chkDay		: chkDay,
			requestType : 'getReqList'
	}
	
	var ajaxResult = ajaxCallWithJson('/webPage/report/IndirectCostReport', tmpInfo, 'json');  
	
	mainGrid.setData(ajaxResult);
	
}





