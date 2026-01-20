/**
 * eCAMS MAIN 화면 정의
 * 
 * <pre>
 * 
 * 	작성자: 이용문
 * 	버전 : 1.1
 *  수정일 : 2019-07-16
 * 
 */


var userName 	= window.top.userName;
var userId 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;

var pieChart	= null;
var areaChart	= null;
var calendar 	= null;
var calendarEl 	= null;
var calMonthArr	= [];
var calMonthArrHoli = [];
var calHoliArr		= [];

var srListData	= [];
var timeLineArr = ['SR등록','SR접수','체크아웃/프로그램등록','체크인','개발배포','테스트배포','운영배포요청','운영배포','SR완료'];

var myWin		= null;
var springYn = false;

$(document).ready(function(){
	//$('#lblPieTitle').text('최근 한달간 신청종류');
	getCalInfo();
	getPrcLabel();
	getSrList();
	line_chart();
	
	getRequestChart();

	$('body').on('click', 'button.fc-prev-button', function() {
		getAddCalInfo();
	});

	
	$('body').on('click', 'button.fc-next-button', function() {
		getAddCalInfo();
	});
	
	$('body').on('click', 'button.fc-dayGridMonth-button', function() {
	});
	
	$(window).bind('resize', function(e) {
		/*pieChart.resize({
	        width: $('#pieAppliKinds').parent('div').width()-2,
	        height: $('#pieAppliKinds').parent('div').height()-5
		});*/
	}); 
	
	//창 크기 변경에 따른 차트 리사이즈
	var timeout;
	setTimeout(function() {		
		$(window).resize(function() {
			clearTimeout(timeout);
			timeout = setTimeout(function() {				
				getPieData('4','2');
				line_chart();		
			}, 300);
		})
	}, 500);
});

// 접속자 신청 라인 차트 정보 가져오기
function getRequestChart() {
	var data = {
			requestType	: 	'getRequestChart',
			userId		: 	userId
		}
		ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetRequestChart);
}

// 접속자 신청 라인 차트 정보 가져오기 완료
function successGetRequestChart(data) {
	var width = $('#areaDiv').width();
	var height = 247;
	
	var container = document.getElementById('chart-area');
	var data = {
	    categories: data.categories,
	    series: [
	        {
	            name: '체크아웃',
	            data: data.checkOut
	        },
	        {
	        	name: '체크인',
	        	data: data.checkIn
	        },
	        {
	        	name: '개발배포',
	        	data: data.dev
	        },
	        {
	        	name: '테스트배포',
	        	data: data.test
	        },
	        {
	        	name: '운영배포',
	        	data: data.oper
	        }
	    ]
	};
	var options = {
	    chart: {
	        width: width - 10,
	        height: height,
	    },
	    series: {
	        zoomable: false,
	        showDot: false,
	        areaOpacity: 1
	    },
	   /* yAxis: {
	        title: '신청건수',
	        pointOnColumn: true
	    },*/
	    /*xAxis: {
	        title: '주 단위'
	    },*/
	    tooltip: {
	        suffix: '건'
	    }
	};
	var theme = {
	    series: {
	        colors: [
	            '#83b14e', '#458a3f', '#295ba0', '#2a4175', '#289399',
	            '#289399', '#617178', '#8a9a9a', '#516f7d', '#dddddd'
	        ]
	    }
	};
	// For apply theme
	// tui.chart.registerTheme('myTheme', theme);
	// options.theme = 'myTheme';
	areaChart = tui.chart.areaChart(container, data, options);
	
	// 기간의 신청건별 건수 가져온후 해당 신청건 프로그램종류 가져오기 
	getPieData('4','2');
	 $('#pieLabel').html("프로그램 신청현황 [운영배포]  " + data.categories[2]);
	 
	areaChart.on('selectSeries', function(info) {
	    $('#pieLabel').html("프로그램 신청현황 [" + info.legend + "]  " + data.categories[info.index]);
	    var applyIndex = '0';
	    switch (info.legend) {
		case '체크아웃':
			applyIndex = '0';
			break;
		case '체크인':
			applyIndex = '1';
			break;
		case '개발배포':
			applyIndex = '2';
			break;
		case '테스트배포':
			applyIndex = '3';
			break;
		case '운영배포':
			applyIndex = '4';
			break;
		default:
			break;
		}
	    
	    getPieData(applyIndex, info.index);
	});
}

// 파이차트 데이터 가져오기
function getPieData(applyIndex, dateIndex) {
	var data = {
		requestType	: 	'getMainPie',
		userId		: 	userId,
		applyIndex	:   applyIndex,
		dateIndex	:   dateIndex
	}
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetPieData);
}

// 파이차트 데이터 가져오기 완료
function successGetPieData(data) {
	if(data.length === 0 ){
		data = makeFakeData('PIE');
	}
	
	var calHeight = parseInt($('#divCal').height());
	var width = $('#pieDiv').width();
	var pieChartHeight = 300;
		
	if($('#pieAppliKinds').length > 0) $('#pieAppliKinds').empty();
	
	var container = document.getElementById('pieAppliKinds');
	var data = {
	    categories: ['신청종류'],
	    series: data
	};
	var visible = $(window).width() < 1300 ? false : true;
	var options = {
	    chart: {
	    	title: {text: ""},
	        width: 	 width - 10,
	        height:  pieChartHeight,
	    },
	    series: {
	        labelAlign: 'center',
	        radiusRange: ['70%', '100%']
	    },
	    legend: {
	        visible: visible
	    },
	    chartExportMenu: {
	    	visible: false
	    }
	};
	
	pieChart = tui.chart.pieChart(container, data, options);	
	getPieprogressSw = false;
}

// 파이 데이터 없을시
function  makeFakeData(chartKin) {
	var chartData = null;
	if(chartKin === 'PIE') chartData = [{name: '데이터가 없습니다.', data: 1}];
	return chartData;
}

// SR리스트 가져오기
function getSrList() {
	var data = new Object();
	data = {
		userId		: 	userId,
		requestType	: 	'getSrList'
	}
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetSrList);
}

// SR리스트 가져오기 완료
function successGetSrList(data) {
	
	 if(data.length <= 0 ) {
        return;
	}
	if(data[0].springYn == undefined) {
	        springYn = false;
	}else {
	        springYn = true;
	}
	
	srListData = data;
	var liStr = null;
	var width = 0;
	var colIn = -1;
	var title = '';
	var colorArr = ['org','green','blue'];
	
	$('#divSrlist').empty();
	
	srListData.forEach(function(item, index){
		title = item.reqTitle.length > 10 ? item.reqTitle.substr(0,10)+'...' : item.reqTitle;
		colIn++;
		if(colIn >= colorArr.length) {
			colIn = 0;
		}
		if(springYn) {
			width = makeSrWidth(item.dataMap.step);
		} else {
			width = makeSrWidth(item.step);
		}
		liStr = '';
		liStr += '<dl class="srdl" flow="down" tooltip="'+item.reqTitle+'['+item.srId+']">';
		liStr += '	<dt>'+title+'</dt>';
		liStr += '	<dd id="'+item.srId+'" style="cursor: pointer;"><span class="'+colorArr[colIn]+' width-'+width+'">'+width+'%'+'('+ item.stepLabel +')</span></dd>';
		liStr += '</dl>';
		$('#divSrList').append(liStr);
	});
	
	if(srListData.length > 0 ) {
		makeTimeLine(srListData[0].srId);
	}
	
	$("dd[id^='R']").bind('click', function(event) {
		makeTimeLine($(this).attr('id'));
	});
}

// 타임라인 만들기
function makeTimeLine(srId) {
	var item = null;
	var step = null;
	var detailData = null;
	var liStr = '';
	var approvalStr = '';
	for(var i=0; i<srListData.length; i++) {
		if(srId === srListData[i].srId) {
			item = srListData[i];
			break;
		}
	}
	
	if(springYn) {
		step		= item.dataMap.step;
		detailData 	= item.dataMap;
	} else {
		step		= item.step;
		detailData 	= item;
	}
	
	$('#divTimeLine').empty();
	liStr = '<h4>SR 요청제목 ['+item.reqTitle+']</h4>';
	for(var i = 0; i<Number(step); i++) {
		var detail = makeTimeLineDetail( (i+1) , detailData) ;
		if(detail.length > 0 ) {
			liStr += '<div class="item">';
			liStr += '	<i class="fas fa-clock"></i>';
			liStr += '	<div class="item_info">';
			liStr += '		<div>'+detail+'</div>';
			liStr += '		<p>'+timeLineArr[i]+'</p>';
			liStr += '	</div>';
			liStr += '</div>';
		}
		
		// 결재자 타임라인 추가
		if(springYn) {
			approvalStr = makeApprovalLine(  (i+1) , item.approvalMap );
		} else {
			approvalStr = makeApprovalLine(  (i+1) , item );
		}
		
		liStr += approvalStr;
	}
	$('#divTimeLine').append(liStr);
}

//타임 라인 신청건/ SR 등록 접수 등의 일시 표시
function makeApprovalLine(stepIndex, item) {
	var liStr = '';
	var dateKey		= 'step' + stepIndex + "_1_CONFDATE";
	var confNameKey		= 'step' + stepIndex + "_1_CR_CONFNAME";
	var confUserKey		= 'step' + stepIndex + "_1_CONFUSERNAME";
	var lineCnt = 0;
	
	while(true) {
		lineCnt++;
		
		dateKey		= 'step' + stepIndex + "_"+lineCnt+"_CONFDATE";
		confNameKey		= 'step' + stepIndex + "_"+lineCnt+"_CR_CONFNAME";
		confUserKey		= 'step' + stepIndex + "_"+lineCnt+"_CONFUSERNAME";
		
		if(item[dateKey] === undefined) {
			break;
		}
		
		liStr += '<div class="item">';
		liStr += '	<i class="fas fa-clock"></i>';
		liStr += '	<div class="item_info">';
		switch (stepIndex) {
			case 3:
				liStr += '		<div>체크아웃/프로그램등록  결재 ['+item[dateKey]+']</div>';
				liStr += '		<p>['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</p>';
				break;
			case 4:
				liStr += '		<div>체크인  결재 ['+item[dateKey]+']</div>';
				liStr += '		<p>['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</p>';
				break;
			case 5:
				liStr += '		<div>개발배포  결재 ['+item[dateKey]+']</div>';
				liStr += '		<p>['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</p>';
				break;
			case 6:
				liStr += '		<div>테스트배포  결재 ['+item[dateKey]+']</div>';
				liStr += '		<p>['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</p>';
				break;
			case 7:
				liStr += '		<div>운영배포  결재 ['+item[dateKey]+']</div>';
				liStr += '		<p>['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</p>';
				break;
			case 9:
				liStr += '		<div>SR완료  결재 ['+item[dateKey]+']</div>';
				liStr += '		<p>['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</p>';
				break;
		}
		liStr += '	</div>';
		liStr += '</div>';
		
	}
	return liStr;
}

// 타임 라인 신청건/ SR 등록 접수 등의 일시 표시
function makeTimeLineDetail(stepIndex, item) {
	var rtDetail = '';
	var key		= 'step' + stepIndex;
	
	if(item[key] === undefined) {
		return '';
	}
	
	switch (stepIndex) {
		case 1:
			rtDetail = 'SR등록 일시 ['+ item[key] +']';
			break;
		case 2:
			rtDetail = '개발자 접수 일시 ['+ item[key] +']';
			break;
		case 3:
			rtDetail = '체크아웃 신청 일시 ['+ item[key] +']';
			break;
		case 4:
			rtDetail = '체크인 신청 일시 ['+ item[key] +']';
			break;
		case 5:
			rtDetail = '개발배포 신청  일시 ['+ item[key] +']';
			break;
		case 6:
			rtDetail = '테스트배포 신청 일시 ['+ item[key] +']';
			break;
		case 7:
			rtDetail = '운영배포 신청 일시 ['+ item[key] +']';
			break;
		case 8:
			rtDetail = '운영배포 완료 일시 ['+ item[key] +']';
			break;
		case 9:
			rtDetail = 'SR완료 일시 ['+ item[key] +']';
			break;
	}
	
	return rtDetail;
}

// SR리스트의 퍼센테이지 만들어주기
function makeSrWidth(step) {
	var width = 0;
	
	if(step === '9') {
		width = 100;
	} else {
		width = Number(step) * 10;
	}
	return width;
}

// 미결/SR/오류 라벨 건수 가져오기
function getPrcLabel() {
	var data = new Object();
	data = {
		userId		: 	userId,
		requestType	: 	'getPrcLabel'
	}
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetPrcLabel);
}

// 미결/SR/오류 라벨 건수 가져오기 완료
function successGetPrcLabel(data) {
	
	// 미결 / SR / 오류 라벨 세팅
	$('#approvalCnt').html(data.approvalCnt);
	$('#srCnt', parent.document).html('SR['+data.srCnt+']');
	$('#errCnt', parent.document).html('오류['+data.errCnt+']');
	
	$('#lblApproval').html('['+data.approvalCnt+']');
	$('#lblSr').html('['+data.srCnt+']');
	$('#lblErr').html('['+data.errCnt+']');
	
	// 등록 / 개발 / 테스트 / 적용 라벨 세팅
	$('#srRegCnt').html(data.srRegCnt);
	$('#devSrCnt').html(data.devSrCnt + '/' + data.devPrgCnt);
	$('#testSrCnt').html(data.testSrCnt + '/' + data.testPrgCnt);
	$('#appySrCnt').html(data.appySrCnt + '/' + data.appyPrgCnt);
	
	window.parent.inner = $("#msrDiv", parent.document)[0];
}


//처음 캘린더 인포 가져오기
function getCalInfo() {
	var data = new Object();
	data = {
		userId		: 	userId,
		month		: 	getDate('DATE',0).substr(0,6),
		requestType	: 	'getCalendarInfo'
	}
	
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetCalInfo);
}

//처음 캘린더 인포 가져오기 완료
function successGetCalInfo(data) {
	if(!checkCalInfo(getDate('DATE',0).substr(0,6)) ) {
		return;
	}
	var defaultDate = getDate('DATE',0).substr(0,4) + '-' + getDate('DATE',0).substr(4,2) + '-' + getDate('DATE',0).substr(6,2);
	
	calendarEl= document.getElementById('calendar');
	calendar =  new FullCalendar.Calendar(calendarEl, {
	    plugins: [ 'interaction', 'dayGrid', 'timeGrid', 'list', 'rrule' ],
	    defaultDate: defaultDate,
	    editable: false,
	    eventLimit: 1,
	    height: $('#calendar').height(),
	    header: {
	        left: 'prev,next today',
	        center: 'title',
	        right: 'dayGridMonth,listMonth'
	    },
	    locale: 'ko', 
	    events:  data,
	    eventClick: function(arg) {
	    	if( arg.event._def.extendedProps.cr_acptno !== undefined ) {
	    		openApprovalInfo(arg.event._def.extendedProps.cr_acptno, arg.event._def.extendedProps.cr_qrycd);
	    	}
	    }
	});
	calendar.render();
}

//캘린더 인포 추가
function getAddCalInfo() {
	if(!checkCalInfo(getCalFullDate()) ) {
		return;
	}
	var data = new Object();
	data = {
		userId		: 	userId,
		month		: 	getCalFullDate(),
		requestType	: 	'getCalendarInfo'
	}
	
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetAddCalInfo);
}

// 캘린더 인포 추가 가져오기 완료
function successGetAddCalInfo(data) {
	calendar.addEventSource(data);
}

// 이미 추가되어있는 캘린더 정보 인지 확인 후 추가 가능 또는 불가 판단 리턴
function checkCalInfo(month) {
	if(calMonthArr.includes(month)) {
		return false;
	} else {
		calMonthArr.push(month);
		return true;
	}
}


// 이미 추가되어있는 캘린더 휴일인지 확인
function checkCalInfoHoli(month) {
	if(calMonthArrHoli.includes(month)) {
		return false;
	} else {
		calMonthArrHoli.push(month);
		return true;
	}
}
// 달력 휴일정보 가져오기
function getHoliday() {
	if(!checkCalInfoHoli(getCalFullDate()) ) {
		addHoliCss();
		return;
	}
	
	var data = new Object();
	data = {
		month		: 	getCalFullDate(),
		requestType	: 	'getHoliday'
	}
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetHoliday);
}

function addHoliCss() {
	calHoliArr.forEach(function(item,index) {
		$('[data-date="' + item.start + '"]').addClass('holiday');
	});
}

// 달력 휴일정보 가져오기 완료
function successGetHoliday(data) {
	calendar.addEventSource(data);
	data.forEach(function(item, index) {
		if(!calHoliArr.includes(item))  {
			calHoliArr.push(item);
		}
		
		if(item.holiday !== undefined && item.holiday === 'Y') {
			$('[data-date="' + item.start + '"]').addClass('holiday');
		}
	});
}



// 캘린더 현재 월 구해오기 YYYYMM 까지
function getCalFullDate() {
	var calMon 		= '';
	var calYear 	= '';
	var fullDate 	= '';
	calYear = calendar.getDate().getFullYear();
	calMon = calendar.getDate().getMonth();
	calMon += 1;
	calMon = (calMon < 10 ? '0' : '') + calMon; 
	fullDate = calYear + calMon;
	
	return fullDate; 
}

function line_chart(){
	
	if($('#pieAppliKinds').length > 0) $('#line-chart').empty();
	var container = document.getElementById('line-chart');
	var data = {
	    categories: ['01/01/2016', '02/01/2016', '03/01/2016', '04/01/2016', '05/01/2016', '06/01/2016', '07/01/2016', '08/01/2016', '09/01/2016', '10/01/2016', '11/01/2016', '12/01/2016'],
	    series: [
	        {
	            name: 'Seoul',
	            data: [-3.5, -1.1, 4.0, 11.3, 17.5, 21.5, 24.9, 25.2, 20.4, 13.9, 6.6, -0.6]
	        },
	        {
	            name: 'Seattle',
	            data: [3.8, 5.6, 7.0, 9.1, 12.4, 15.3, 17.5, 17.8, 15.0, 10.6, 6.4, 3.7]
	        },
	        {
	            name: 'Sydney',
	            data: [22.1, 22.0, 20.9, 18.3, 15.2, 12.8, 11.8, 13.0, 15.2, 17.6, 19.4, 21.2]
	        },
	        {
	            name: 'Moskva',
	            data: [-10.3, -9.1, -4.1, 4.4, 12.2, 16.3, 18.5, 16.7, 10.9, 4.2, -2.0, -7.5]
	        },
	        {
	            name: 'Jungfrau',
	            data: [-13.2, -13.7, -13.1, -10.3, -6.1, -3.2, 0.0, -0.1, -1.8, -4.5, -9.0, -10.9]
	        }
	    ]
	};
	var options = {
	    chart: {
	        width: $("#line-chart").width() - 15,
	        height: 410
//	        title: '24-hr Average Temperature'
	    },
	    yAxis: {
	        title: 'Temperature (Celsius)'
	    },
	    xAxis: {
	        title: 'Month',
	        pointOnColumn: true,
	        dateFormat: 'MMM',
	        tickInterval: 'auto'
	    },
	    series: {
	        showDot: false,
	        zoomable: true
	    },
	    tooltip: {
	        suffix: '°C'
	    },
	    plot: {
	        bands: [
	            {
	                range: ['03/01/2016', '05/01/2016'],
	                color: 'gray',
	                opacity: 0.2
	            }
	        ],
	        lines: [
	            {
	                value: '03/01/2016',
	                color: '#fa2828'
	            },{
	            	value: '05/01/2016',
	                color: '#fa2828'
	            }
	        ]
	    }
	};
	var theme = {
	    series: {
	        colors: [
	            '#83b14e', '#458a3f', '#295ba0', '#2a4175', '#289399',
	            '#289399', '#617178', '#8a9a9a', '#516f7d', '#dddddd'
	        ]
	    }
	};
	
	// 원하는 색상으로 가능
	// tui.chart.registerTheme('myTheme', theme);
	// options.theme = 'myTheme';
	var chart = tui.chart.lineChart(container, data, options);
}

//결재 정보 창 띄우기
function openApprovalInfo(acptNo, reqCd) {
	var nHeight, nWidth, cURL, winName;
	
	if ( reqCd == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
	
    winName = reqCd;
    
	var form = document.popPam;   		//폼 name
    
	form.acptno.value	= acptNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	
	nHeight = screen.height - 300;
    nWidth  = screen.width - 400;
    cURL = "/webPage/winpop/PopRequestDetail.jsp";
    
	myWin = winOpen(form, winName, cURL, nHeight, nWidth);
}

