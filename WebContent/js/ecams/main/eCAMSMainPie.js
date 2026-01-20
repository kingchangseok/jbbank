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
userId			= "MASTER";
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;

var calendar 	= null;
var calendarEl 	= null;
var calMonthArr	= [];
var calMonthArrHoli = [];
var calHoliArr		= [];

var srListData	= [];
var timeLineArr = ['SR등록','SR접수','체크아웃/프로그램등록','체크인','개발배포','테스트배포','운영배포요청','운영배포','SR완료'];

var myWin		= null;

$(document).ready(function(){
	$('#lblPieTitle').text(userName + '님의 최근 한달간 운영 신청 프로그램 종류');
	getCalInfo();
	getPrcLabel();
	getSrList();
	line_chart();

	$('body').on('click', 'button.fc-prev-button', function() {
		getAddCalInfo();
		//getHoliday();
	});

	$('body').on('click', 'button.fc-next-button', function() {
		getAddCalInfo();
		//getHoliday();
	});
	
	$('body').on('click', 'button.fc-dayGridMonth-button', function() {
		//getHoliday();
	});
	
	$(window).bind('resize', function() {
		/*pieChart.resize({
			width: $('#pieDiv').width(),
			height: $('#pieDiv').height() - 50
		});*/
	});
});

// 파이차트 데이터 가져오기
function getPieData() {
	var data = {
			requestType	: 	'getMainPie',
			data : {				
				userId		: 	userId
			}
		}
	ajaxAsync('/webPage/main/eCAMSMainPieServlet', data, 'json', successGetPieData);
}

// 파이차트 데이터 가져오기 완료
function successGetPieData(data) {
	
	if(data.length === 0 ){
		data = makeFakeData('PIE');
	}
	
	var calHeight = parseInt($('#divCal').height());
	var width = $('#pieDiv').width();
//	var pieChartHeight = 221;
	var pieChartHeight = 251;
		
	if($('#pieAppliKinds').length > 0) $('#pieAppliKinds').empty();
	
	var container = document.getElementById('pieAppliKinds');
	var data = {
	    categories: ['신청종류'],
	    series: data
	};
	
	var options = {
	    chart: {
	    	title: {
	    		visible: false,
	    		text: "",
	    		align: "left",
	    		offsetX: 20,
	    		offsetY: 20
	    	},
	        width: 	 width - 10,
	        height:  pieChartHeight,
	    },
	    series: {
	        labelAlign: 'center',
	        radiusRange: ['70%', '100%']
	    },
	    legend: {
	        visible: true,
	        
	    },
	    chartExportMenu: {
	    	visible: false
	    }
	};
	
	pieChart = tui.chart.pieChart(container, data, options);	
	getPieprogressSw = false;
}

function successGetPieData2(data) {
	
	if(data.length === 0 ){
		data = makeFakeData('PIE');
	}
	
	var calHeight = parseInt($('#divCal').height());
	var width = $('#pieDiv2').width();
	var pieChartHeight = 220;
	
	if($('#pieAppliKinds2').length > 0) $('#pieAppliKinds2').empty();
	
	var container = document.getElementById('pieAppliKinds2');
	var data = {
			categories: ['신청종류'],
			series: data
	};
	
	var options = {
			chart: {
				width: 	 width - 10,
				height:  pieChartHeight,
			},
			series: {
				labelAlign: 'center',
				radiusRange: ['70%', '100%']
			},
			legend: {
				visible: true,
				
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
	console.log(data);
	
	srListData = data;
	var liStr = null;
	var width = 0;
	var colIn = -1;
	var title = '';
	var colorArr = ['org','green','blue'];
	
	$('#divSrlist').empty();
	srListData.forEach(function(item, index){
		title = item.reqTitle.length > 12 ? item.reqTitle.substr(0,12)+'...' : item.reqTitle;
		colIn++;
		if(colIn >= colorArr.length) {
			colIn = 0;
		}
		width = makeSrWidth(item.step);
		liStr = '';
		liStr += '<dl class="srdl" flow="down" tooltip="'+item.reqTitle+'['+item.srId+']">';
		liStr += '	<dt>'+title+'</dt>';
		liStr += '	<dd id="'+item.srId+'" style="cursor: pointer;"><span class="'+colorArr[colIn]+' width-'+width+'">'+width+'%'+'('+ item.stepLabel +')</span></dd>';
		liStr += '</dl>';
		$('#divSrList').append(liStr);
	});
	
	makeTimeLine(srListData[0].srId);
	
	$("dd[id^='R']").bind('click', function(event) {
		makeTimeLine($(this).attr('id'));
	});
}

// 타임라인 만들기
function makeTimeLine(srId) {
	var item = null;
	for(var i=0; i<srListData.length; i++) {
		if(srId === srListData[i].srId) {
			item = srListData[i];
			break;
		}
	}
	
	var liStr = null;
	$('#divTimeLine').empty();
	liStr = '<h4>SR 요청제목 ['+item.reqTitle+']</h4>';
	for(var i = 0; i<Number(item.step); i++) {
		var detail = makeTimeLineDetail( (i+1) , item) ;
		liStr += '<div class="item">';
		liStr += '	<i class="fas fa-clock"></i>';
		liStr += '	<div class="item_info">';
		liStr += '		<div>'+detail+'</div>';
		liStr += '		<p>'+timeLineArr[i]+'</p>';
		liStr += '	</div>';
		liStr += '</div>';
	}
	$('#divTimeLine').append(liStr);
}

// 타임 라인 신청건/ SR 등록 접수 등의 일시 표시
function makeTimeLineDetail(stepIndex, item) {
	var rtDetail = '';
	var key		= 'step' + stepIndex;
	
	if(item[key] === undefined) {
		return 'yyyy/mm/dd hh:mm';
	}
	
	switch (stepIndex) {
		case 1:
			rtDetail = item[key];
			break;
		case 2:
			rtDetail = item[key];
			break;
		case 3:
			rtDetail = item[key];
			break;
		case 4:
			rtDetail = item[key];
			break;
		case 5:
			rtDetail = item[key];
			break;
		case 6:
			rtDetail = item[key];
			break;
		case 7:
			rtDetail = item[key];
			break;
		case 8:
			rtDetail = item[key];
			break;
		case 9:
			rtDetail = item[key];
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
	$('#lblApproval').html('['+data.approvalCnt+']');
	$('#lblSr').html('['+data.srCnt+']');
	$('#lblErr').html('['+data.errCnt+']');
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
	    height: 390,
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
	
	getPieData();
	var test = [
		{data : 5, name : "test1"},
		{data : 2, name : "test2"},
		{data : 2, name : "test3"},
		{data : 3, name : "test4"}
	]
	successGetPieData2(test);
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

function line_chart(){
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