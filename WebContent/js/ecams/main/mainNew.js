var userId = "MASTER";
var timeLineArr = ['SR등록','SR접수','체크아웃/프로그램등록','체크인','개발배포','테스트배포','운영배포요청','운영배포','SR완료'];

$(document).ready(function(){
	
	getPrcLabel();
	getSrList();
	screenInit();
	
	$(".leftBar").bind("click",function(){
		console.log($(this).index());
	});

	// 로그아웃 클릭
	$('#logOut').bind('click', function() {
		confirmDialog.confirm({
			title: '로그아웃',
			msg: '로그아웃 하시겠습니까?'
		}, function(){
			if(this.key === 'ok') {
				logOut();
			}
		});
		
	});
});

//SR리스트 가져오기
function getSrList() {
	var data = new Object();
	data = {
		userId		: 	userId,
		requestType	: 	'getSrList'
	}
	ajaxAsync('/webPage/main/eCAMSMainServlet', data, 'json',successGetSrList);
}

//SR리스트 가져오기 완료
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
	
	$('#leftGraph').empty();
	
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
		liStr = '<div class="srStatus ' + (index > 0 ? "":"first") + '" >';
		liStr += '<div class="srBasicDiv" flow="down" tooltip="'+item.reqTitle+'['+item.srId+']" id="'+item.srId+'">';
		liStr += '<div style="width: '+width+'%">';
		liStr += '	<label class="srBasicText">'+title+'</label>';
		liStr += '	</div>';
		liStr += '</div>';
		liStr += '<div class="srText">';
		liStr += '<label>'+width+'%</label>';
		liStr += '</div>';
		liStr += '</div>';
		
		$('#leftGraph').append(liStr);
	});
	
	$("#btnListAll").bind("click",function(){
		$('.timeLineBox').empty();
		if(srListData.length > 0 ) {
			for(var i=0; i<srListData.length; i++){
				makeTimeLine(srListData[i].srId,true);
			}
		}
	});
	
	$("#btnListAll").click();
	
	$("div[id^='R']").bind('click', function(event) {
		makeTimeLine($(this).attr('id'));
	});
}

//SR리스트의 퍼센테이지 만들어주기
function makeSrWidth(step) {
	var width = 0;
	
	if(step === '9') {
		width = 100;
	} else {
		width = Number(step) * 10;
	}
	return width;
}

//타임라인 만들기
function makeTimeLine(srId, first) {
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
	if(!first){
		$('.timeLineBox').empty();
	}
	
	liStr = '<div class="srDetail">';
	liStr += '		<div style="width:100%; margin-top:24px;">';
	liStr += '			<img src="zeplin_img/timeline-check.svg" class="timeline-check">';
	liStr += '			<label class="timeLineText">'+item.reqTitle+'</label>';
	liStr += '		</div>';
	for(var i = 0; i<Number(step); i++) {
		var detail = makeTimeLineDetail( (i+1) , detailData) ;
		if(detail.length > 0 ) {
			liStr += '<div class="timeLine">';
			liStr += '		<div class="oval1"></div>';
			liStr += '		<div style="width:210px;">';
			liStr += '			<label class="timeLineBase">'+timeLineArr[i]+'</label>';
			liStr += '		</div>';
			liStr += '		<div style="width: 109px;">';
			liStr += '			<label class="timeLineBase2">'+detailData['step'+(i+1)]+'</label>';
			liStr += '		</div>';
			liStr += '		<div style="width: 78px;">';
			liStr += '			<label class="timeLineBase2">'+detail+'</label>';
			liStr += '		</div>';
			liStr += '		<div class="oval2">';
			liStr += '			<img src="zeplin_img/3-circle-icon.svg" class="circle-icon">';
			liStr += '		</div>';
			liStr += '	</div>';
		}
		
		// 결재자 타임라인 추가
		if(springYn) {
			approvalStr = makeApprovalLine(  (i+1) , item.approvalMap );
		} else {
			approvalStr = makeApprovalLine(  (i+1) , item );
		}
		
		liStr += approvalStr;
	}
	liStr += '</div>';
	$('.timeLineBox').append(liStr);
}

//타임 라인 신청건/ SR 등록 접수 등의 일시 표시
function makeTimeLineDetail(stepIndex, item) {
	var rtDetail = '';
	var key		= 'step' + stepIndex;
	
	if(item[key] === undefined) {
		return '';
	}
	
	switch (stepIndex) {
		case 1:
			rtDetail = 'SR등록';
			break;
		case 2:
			rtDetail = '개발자 접수';
			break;
		case 3:
			rtDetail = '체크아웃 신청';
			break;
		case 4:
			rtDetail = '체크인 신청';
			break;
		case 5:
			rtDetail = '개발배포 신청';
			break;
		case 6:
			rtDetail = '테스트배포 신청';
			break;
		case 7:
			rtDetail = '운영배포 신청';
			break;
		case 8:
			rtDetail = '운영배포 완료';
			break;
		case 9:
			rtDetail = 'SR완료';
			break;
	}
	
	return rtDetail;
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
		
		liStr += '<div class="timeLine">';
		liStr += '		<div class="oval1"></div>';
		liStr += '		<div style="width:210px;">';
		var step="";
		switch (stepIndex) {
			case 3:
				step = "체크아웃/프로그램등록";
				break;
			case 4:
				step = "체크인";
				break;
			case 5:
				step = "개발배포";
				break;
			case 6:
				step = "테스트배포";
				break;
			case 7:
				step = "운영배포";
				break;
			case 9:
				step = "SR완료";
				break;
		}

		liStr += '			<label class="timeLineBase">['+ item[confNameKey]+'] - '+ item[confUserKey] + ' 님 결재' +'</label>';
		liStr += '		</div>';
		liStr += '		<div style="width: 109px;">';
		liStr += '			<label class="timeLineBase2">'+item[dateKey]+'</label>';
		liStr += '		</div>';
		liStr += '		<div style="width: 78px;">';
		liStr += '			<label class="timeLineBase2">'+step+' 결재</label>';
		
		liStr += '		</div>';
		liStr += '		<div class="oval2">';
		liStr += '			<img src="zeplin_img/3-circle-icon.svg" class="circle-icon">';
		liStr += '		</div>';
		liStr += '	</div>';
		
	}
	return liStr;
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
	$('#srCnt').html(data.srCnt);
	$('#errCnt').html(data.errCnt);
	$('#approvalCnt').html(data.approvalCnt);
	
	// 등록 / 개발 / 테스트 / 적용 라벨 세팅
	$('#srRegCnt').html(data.srRegCnt);
	$('#devSrCnt').html(data.devSrCnt);
	$('#devSrCntTotal').html('/' + data.devPrgCnt);
	$('#testSrCnt').html(data.testSrCnt);
	$('#testSrCntTotal').html('/' + data.testPrgCnt);
	$('#appySrCnt').html(data.appySrCnt);
	$('#appySrCntTotal').html('/' + data.appyPrgCnt);
	
	window.parent.inner = $("#msrDiv", parent.document)[0];
}

function screenInit() {
	/*
	if( sessionID === null ) sessionID =$('#txtSessionID').val();
	if( sessionID === null || sessionID === '' || sessionID === 'undefinded' ) {
		window.location.replace('/webPage/login/ecamsLogin.jsp');
		return;
	}
	*/
	
	//shjung 20200223 token방식으로 변경
	token = sessionStorage.getItem('access_token');
	
	if( token === null || token === '' || token === 'undefinded' ) {
		dialog.alert('로그인정보가 유효하지않습니다.\n다시 로그인하시기 바랍니다.', function() {
			window.location.replace('/webPage/login/ecamsLogin.jsp');
		});
		return;
	}
	
	getSession();
}

// 세션 가져오기
function getSession() {
	var data = new Object();
	data = {
		requestType : 'GETSESSIONUSERDATA'
		/* ,sessionID 	: sessionID */
	}
	ajaxAsync('/webPage/main/eCAMSBaseServlet', data, 'json',successGetSession);
}

//세션 가져오기 완료
function successGetSession(data) {
	//console.log(data);
	userName		= data.userName;
	userId 			= data.userId;
	adminYN 		= data.adminYN === 'true' ? true : false;
	userDeptCd 		= data.deptCd;
	userDeptName	= data.deptName;

	if(userId == undefined || userId == 'undefined') {
		dialog.alert('로그인정보가 유효하지않습니다.\n다시 로그인하시기 바랍니다.', function() {
			window.location.replace('/webPage/login/ecamsLogin.jsp');
			return;
		});
		//window.location.replace('/webPage/login/ecamsLogin.jsp');
		//return;
	} else {
		sessionStorage.removeItem('id');
    	sessionStorage.setItem('id', data.userId);
		loadEcamsMain();
	}
	$('#loginUser').html(userName + '님 로그인');
	meneSet();
	
	rgtList = getMyRgtCd(userId);
	//console.log(rgtList);
}

function loadEcamsMain() {
	/*
	$('#eCAMSFrame').empty();
	$iFrm = $('<IFRAME id="iFrm" frameBorder="0" name="iFrm" scrolling="yes" src="/webPage/main/eCAMSMainNew.jsp" style=" width:100%; height:'+contentHeight+'px; min-width:1024px;" marginwidth="0" marginheight="0" onload="frameLoad()"></IFRAME>');
	$iFrm.appendTo('#eCAMSFrame');
	*/
	//mouseMove();
}

function logOut() {
	//local storage clear
	
	sessionStorage.clear();
	/*
	//session all remove
	var data = {
		requestType	: 	'removeSession'
	}
	ajaxCallWithJson('/webPage/login/Login', data);
	*/
	window.location.replace('/webPage/login/ecamsLogin.jsp');
}