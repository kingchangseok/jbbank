/**
 * [보고서 > 기간별변경추이] 
 */
var userId 		= window.top.userId;
var codeList    = window.top.codeList;  //전체 코드 리스트
var adminYN 	= window.top.adminYN;	//'N' 관리자여부

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var options 	= [];
var columnData 	= [];
var titleData	= [];
var ajaxData	= [];
var tmpInfo 	= {};
var lblDaygbn = '';

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
        },
	},
	columns : columnData
}); 

function column_chart(ctgData,seriesData){
	
	var container = document.getElementById('column-chart');
	var data = {
	    categories: ctgData,
	    series: [{
	    	  name: 'a',
	    	  data: seriesData
	    }]
	};
	var options = {
	    chart: {
	        width: 1160,
	        height: 540,
	        title: lblDaygbn+'변경추이',
	        format: '10'
	    },
//	    yAxis: {
//	        title: '변경건수',
//	        min: 0,
//	        max: 100
//	    },
//	    xAxis: {
//	        title: '변경기간'
//	    },
	    legend: {
	        align: 'top'
	    }
	};
	var theme = {
	    series: {
	        colors: [
	            '#83b14e', '#458a3f', '#295ba0', '#2a4175', '#289399'
	            ,'#289399', '#617178', '#8a9a9a', '#516f7d', '#dddddd'
	        ]
	    }
	};
	
	// 원하는 색상으로 변경
	// tui.chart.registerTheme('myTheme', theme);
	// options.theme = 'myTheme';
	tui.chart.columnChart(container, data, options);
}

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

$(document).ready(function() {
//	column_chart();
	
	screenInit();

	//경로
	getTmpDir('99');

	//요청구분
	getCodeInfo();
	
	//시스템
	getSysInfo();
	
	//요청팀
	getTeamInfo();
	
	//조회구분
	setFind();

	//시스템 변경에 따른 프로그램종류
	$("#cboSys").bind("change", function(){
		cboSys_Change();	
	});
	
	$("#btnSearch").bind("click", function(){
		btnSearch_Click();	
	});
	
	$("#btnGraph").bind("click", function(){
		if($(this).html() == '그래프보기'){
			$("#combo #graph").css('display', '');
			$("#combo #grid").css('display', 'none');
			$(this).html('Sheet보기');
			
		}else {
			$("#combo #grid").css('display', '');
			$("#btnSearch").trigger("click");
			$("#combo #graph").css('display', 'none');
			$(this).html('그래프보기');
		}
	});

	$("#btnExcel").bind('click', function() {
		mainGrid.exportExcel("개인별변경추이.xls");
	});
	
});


function screenInit(){
	
	$('input:radio[name=radio]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	$('#btnExcel').prop('disabled', true);
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboSel"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboProg"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboReq"]').ax5select({
		options: []
	});
	$('#lblTeam').val('');
	$('[data-ax5select="cboTeam"]').ax5select({
		options: []
	});
	
	//picker에 오늘 날짜 디폴트로 세팅
	$(function() {
		var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
		$("#dateEd").val(today);
		$("#dateSt").val(today);
	})
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
}

function getTmpDir(pCode){
	
	var data = new Object();
	data = {
		pcode 		: pCode,
		requestType	: 'getSystemPath'
	}
	var strTmpDir = ajaxCallWithJson('/webPage/common/CommonSystemPath', data, 'json');
	strTmpDir = strTmpDir + '/';
	
}

function getCodeInfo() {
	
//	var reqCd = fixCodeList(codeList['REQCD'], 'ALL', '', '', 'N');
	var request = fixCodeList(codeList['REQUEST'], 'ALL', '', '', 'N');
	var checkIn = fixCodeList(codeList['CHECKIN'], 'ALL', '', '', 'N');
	
	options = [];
	
	$.each(checkIn, function(i, item) {
		options.push({value : 'C'+item.cm_micode, text : '체크인['+item.cm_codename+']'});
	});
	
	$.each(request, function(i, item) {
		options.push({value : item.cm_micode, text : item.cm_codename});
	});

	$('[data-ax5select="cboReq"]').ax5select({
		options: options
	});
	
}

function getSysInfo() {
	
	var sysData = {
		UserId 	: userId,
		SecuYn 	: 'N',
		SelMsg 	: 'ALL',
		CloseYn : 'N',
		ReqCd	: '',
	}
	var data = {
		sysData : sysData,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/CommonSysInfo', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	
	options = [];
	$.each(data, function(i, item) {
		options.push({value : item.cm_syscd, text : item.cm_sysmsg});
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: options
	});	
	
	cboSys_Change();
}

function cboSys_Change(){
	
	if (getSelectedIndex('cboSys') > 0){
		var data = {
				SysCd 		: getSelectedVal('cboSys').value,
				selMsg 		: 'ALL',
				requestType	: 'getJawon'
			}
			ajaxAsync('/webPage/report/RevisionReport', data, 'json', SuccessGetJawon);
    }
}

function SuccessGetJawon(data){
	
	options = [];
	$.each(data, function(i, item) {
		options.push({value : item.cm_micode, text : item.cm_codename});
	});
	
	$('[data-ax5select="cboProg"]').ax5select({
		options: options
	});	
}

function getTeamInfo(){
	
	var data = {
        selMsg	: 'All',
        cm_useyn: 'Y',
        gubun	: 'sub',
        itYn	: 'Y',
		requestType	: 'getTeamInfo'
	}
	
	ajaxAsync('/webPage/common/CommonTeamInfo', data, 'json', SuccessGetTeam);
}

function SuccessGetTeam(data){
	
	options = [];
	$.each(data, function(i, item) {
		options.push({value : item.cm_deptcd, text : item.cm_deptname});
	});
	
	$('[data-ax5select="cboTeam"]').ax5select({
		options: options
	});	
}

function setFind(){
	
	$('[data-ax5select="cboSel"]').ax5select({
		options: [
			{value: "0", text: "전체"},
			{value: "1", text: "시스템별"},
			{value: "2", text: "팀별"},
			{value: "3", text: "프로그램종류별"}
			]
	});
}

function getInfo(){
	
	tmpInfo.qrygbn = getSelectedVal('cboSel').value;
	tmpInfo.deptcd = getSelectedVal('cboTeam').value;
	tmpInfo.syscd = getSelectedVal('cboSys').value;
	tmpInfo.qrycd = getSelectedVal('cboReq').value;
	if(getSelectedIndex('cboProg') > 0) tmpInfo.rsrccd =  getSelectedVal('cboProg').value;
	tmpInfo.stday = replaceAllString($("#dateSt").val(), '/', '');
	tmpInfo.edday = replaceAllString($("#dateEd").val(), '/', '');
	if ($("#optDay").is(":checked")) tmpInfo.term = 'D';
	else if ($("#optWeek").is(":checked")) tmpInfo.term = 'W';
	else tmpInfo.term = 'Y';
}

function btnSearch_Click(){
	
	$("#column-chart").remove();
	var chartHtml = '<div id="column-chart"></div>';
	$("#graph .row").append(chartHtml);
	
	getInfo(); //getProgList()와 같은 값 사용
		
	var data = {
		tmpInfo 	: tmpInfo,
		requestType	: 'getTitle'
	}
		
	ajaxAsync('/webPage/report/RevisionReport', data, 'json', SuccessGetTitle);
}


function SuccessGetTitle(data){

	var ctgData 	= [];
	var seriesData	= [];
	titleData = data;
	console.log("titleData= ", data);
	
	if (titleData.length > 0) {
		if (titleData[0].errmsg != null && titleData[0].errmsg != '') {
			dialog.alert(titleData[0].errmsg);
			return;
		}
	}	

	if ($("#optDay").is(":checked")) lblDaygbn = '일별';
	else if ($("#optWeek").is(":checked")) lblDaygbn = '주간별';
	else lblDaygbn = '월별';
	columnData.push({key : "daygbn", label : lblDaygbn, align : "center", width: "10%"});
	
	var lblPrccnt = '';
	if(getSelectedVal('cboSel').value == '1') lblPrccnt = '처리건수';
	else lblPrccnt = '합계';
	columnData.push({key : "prccnt", label : lblPrccnt, align : "center", width: "90%", styleClass: "color-red"});
	
    getInfo();
	
	var progList = {
			tmpInfo		: tmpInfo,
			titleData	: titleData,
			requestType	: 'getSelect_Period'
	}
	
	var ajaxData = ajaxCallWithJson('/webPage/report/RevisionReport', progList, 'json');
	
    if (ajaxData.length > 0) $('#btnExcel').prop('disabled', false);
	
	if(getSelectedVal('cboSel').value != '0' && getSelectedVal('cboSel').value != '4'){
		if(titleData.length > 0){
			$.each(titleData, function(i, item) {
				columnData.push({key : "gbn" + item.cm_micode , label : item.cm_codename, align: "center", width : 85 / titleData.length + "%"});
					seriesData.push("gbn" + item.cm_micode); 
			});
		} else{
			seriesData.push('0'); 
		}
		$.each(ajaxData, function(i, item) {
			ctgData.push(item.qday);
		});
	} else if(getSelectedVal('cboSel').value == '0' || getSelectedVal('cboSel').value == '4'){
		$.each(ajaxData, function(i, item) {
			ctgData.push(item.qday);
			seriesData.push(item.prccnt);  //{name : item.qday, }
		});
	}
	
	
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		columns : columnData
	});
	
	columnData = [];
	
	mainGrid.setData(ajaxData);
	column_chart(ctgData,seriesData);
}










