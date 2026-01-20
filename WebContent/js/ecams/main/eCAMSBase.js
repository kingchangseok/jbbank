/**
 * 메인화면 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */
var userId 			= null;
var userName		= null;
var adminYN 		= null;
var userDeptName	= null;
var userDeptCd 		= null;
var request 		= new Request();
var reqCd			= null;
var userRgtcd       = null;
var manYn           = null;
var menuData		= null;
var resizeInterval 	= null;
var folding 		= null;
var resizeDelay 	= null;
var noticeWin		= null;
var contentHistory 	= null;
var focusFrame = null;
var Cnchk = "N";

var token 			= null;		//token정보
var inter 			= null;		//inter val
var timer 			= 0;   		//세션만료 timer 120분

var contentHeight 	= 0;
var iframeHeight	= 0;

var today 			= getDate('DATE',0);

var codeList = null;	//전체 코드정보
var rgtList  = null;    //로그인한 사용자가 가진 담당직무
var attPath	 = '';		//첨부파일경로(pathcd=21)

//마우스 움직일때 이벤트
function doMove() {
	timer = 0;
	clearInterval(inter);//STOP
	inter = null;
	startInterval();
}
function startInterval() {
	inter = setInterval(function(){//START
	   timer++;
	   //console.log(timer);
		if (timer==6*120){//120분
			timer=0;
			funcSetScript();
		}
	},1000*10);//10초마다
}
function funcSetScript() {
	clearInterval(inter);//STOP
	inter = null;
	sessionStorage.clear();
	dialog.alert('세션이 만료되었습니다.\n다시 로그인하시기 바랍니다.', function() {
		logOut();
	});
	return false;
}

$(document).ready(function(){
	startInterval();
	$("body").bind("mousemove", doMove);
	
	codeList = [];
	//contentHeight = window.innerHeight - $('#header').height() - $('#footer').height() - 20;
	contentHeight = window.innerHeight - $('#topMenu').height() - 15;

//	setTimeout(function() {
//		folding = $(window).width() < 1270 ? false : true;
//		$(window).trigger('resize');
//	}, 300);
	
	$(window).resize(function(){		
		clearTimeout(resizeDelay);
		resizeDelay = setTimeout(function() {
			resize();
		}, 200);
	});

	screenInit();
	
	// azsoft 로고 클릭시 메인 페이지 이동
	$('#logo').bind('click', function() {
		loadEcamsMain();
	});
	
	// 로그아웃 클릭
	$('#logOut, #imgLogOut').bind('click', function() {
		confirmDialog.confirm({
			title: '로그아웃',
			msg: '로그아웃 하시겠습니까?'
		}, function(){
			if(this.key === 'ok') {
				logOut();
			}
		});
	});
	
	$('#errCnt, #srCnt').bind('click', function() {
		changePage(this.id);
	});
	
});

// 열려있는 ax5select 닫기
function clearSelect(frame){
	// 프레임들을 전부 찾는다
	var frameList = [];
	frameList.push($('#iFrm'));
	if($('#iFrm').contents().find("iframe").length > 0){
		frameList = $.extend({},frameList, $('#iFrm').contents().find("iframe"));
	}
	for(var i=0; i<frameList.length; i++){
		//프레임중 ax5grid 셀렉트가 열려있는 상태인 값을 찾는다
		var frameSelecte = $(frameList[i]).contents().find("[data-select-option-group-opened='true']");
		if(frameSelecte.length > 0){
			// 열려있는 상태가 있다면 그 프레임의 body에 포커스를 주기위해 클릭을 해 준다
			$(frameList[i]).contents().find("body").click();
		}
	}
}

function screenInit() {
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
	}
	ajaxAsync('/webPage/main/eCAMSBaseServlet', data, 'json',successGetSession);
}

//세션 가져오기 완료
function successGetSession(data) {
	userName		= data.userName;
	userId 			= data.userId;
	adminYN 		= data.adminYN === 'true' ? true : false;
	userDeptCd 		= data.deptCd;
	userDeptName	= data.deptName;
	userRgtcd	    = data.rgtcd;
	manYn	        = data.manYn;
//	console.log('successGetSession:', data);

	if(userId == undefined || userId == 'undefined') {
		dialog.alert('로그인정보가 유효하지않습니다.\n다시 로그인하시기 바랍니다.', function() {
			window.location.replace('/webPage/login/ecamsLogin.jsp');
			return;
		});
	} else {
		sessionStorage.removeItem('id');
    	sessionStorage.setItem('id', data.userId);
		loadEcamsMain();
	}
	
	$('#loginUser0').css('display', 'inline-block');
	$('#loginUser').css('display', 'inline-block');
	$('.line').css('display', 'inline-block');
	$('#loginUser0').html(htmlFilter(userName));
	//$('#loginUser').html(userName + '님 로그인');
	
	if (adminYN) {
		$(".dIcon.adminMenu").css("display", "inline-block");
	}
	meneSet();
	
	rgtList = getMyRgtCd(userId);
	attPath = getDocPath();
}

//메뉴데이터 가져오기
function meneSet() {
	var data = new Object();
	data = {
		userId 		: userId,
		requestType	: 'MenuList'
	}
	ajaxAsync('/webPage/main/eCAMSBaseServlet', data, 'json',successGetMenuData);
}

//메뉴데이터 가져오기 완료
function successGetMenuData(data) {
	menuData = data;
//	console.log('successGetMenuData:',data);
	
	$('#ulMenu').empty();
	var menuIdx = 1;
	var menuHtmlStr = '';
	menuData.forEach(function(menuItem, menuItemIndex) {
		if(menuItem.link === undefined || menuItem.link === null) {
			if(menuHtmlStr.length > 1) {
				menuHtmlStr += '	    </div">\n';
				menuHtmlStr += '    </div>\n';
				menuHtmlStr += '</li>\n';
			}
			menuIdx = menuItem.id;
			/*if (menuItem.id == 10) {
				menuIdx = menuIdx+menuCnt;
			}*/
			menuHtmlStr += '<li class="lang_open">\n';
			menuHtmlStr += '	<div class="dIcon">\n';
			/* 20220307 neo. 메뉴에서 이미지 삭제*/
			menuHtmlStr += '	    <div id="menu'+(menuIdx)+'" class="menuIcon menu'+(menuIdx)+'">\n';
			/*if(menuItem.id == "2"){//접수
				menuHtmlStr += '<i class="fas fa-file-text"></i>';
			}*/
			menuHtmlStr += '	    </div>\n';
			menuHtmlStr += '	    <a href="#" link=" " style="padding-top: 7px; text-align: center;">'+htmlFilter(menuItem.text)+'</a>\n';
			
//			if (menuItem.id == 3) {//개발이면
//				menuHtmlStr += '	    <div class="menu_box" style="min-width: 270px;">\n';
//			} else {
				menuHtmlStr += '	    <div class="menu_box">\n';
//			}
			//menuIdx++;
		} else if(menuItem.link !== null) {
			menuHtmlStr += '            <p onclick="clickSideMenu(event)" link="'+menuItem.link+'">'+htmlFilter(menuItem.text)+'</p>\n';
		}
		if((menuItemIndex+1) === menuData.length) {
			menuHtmlStr += '	    </div">\n';
			menuHtmlStr += '    </div>\n';
			menuHtmlStr += '</li>\n';
		}
	});

	$('#ulMenu').html(menuHtmlStr);
	
	/*if (!adminYN) {
		$($("#menu9").parents("div")[0]).css({"display" : "none"});
	}*/
	getPopNoticeInfo();
	lang_open();
}

//20210927 팝업공지 체크 및 공지팝업 띄워주기
function getPopNoticeInfo(){
	var data = new Object();
	
	data = {
		requestType : 'BbsDAO'
	}
	
	ajaxAsync('/webPage/dao/DaoServlet', data, 'json',successGetPopNoticeInfo);
}

function successGetPopNoticeInfo(data){
	if(typeof data === 'undefined' && typeof data === null && typeof data === "") return;
	
	if(data.length === 0) return;
	
	data.forEach(function(item, index){
		if(getCookie('notice' + item.CM_ACPTNO + '_' + today) == "false"){
			return;
		} else {
			openPopNotice(item.CM_ACPTNO);
		}
	});
}

//공지사항 팝업 오픈
function openPopNotice(acptno){
	var nHeight, nWidth, cURL, winName, resizableYn,ratioSw, wRatio,hRatio;
	
	if(('popNotice_'+acptno) == winName){
		if(noticeWin != null){
			if(!noticeWin.closed){
				noticeWin.close();
			}
		}
	}
	winName = 'popNotice_'+acptno;
	
	var form = document.popPam;
	form.cm_acptno.value = acptno;
	form.method.value = 'post';
	nHeight = 430;
	nWidth = 600;
	cURL = '/webPage/winpop/PopNotice.jsp';
	ratioSw = true;
	wRatio = 0.3;
	hRatio = 0.4;
	resizableYn = 'no';
	console.log(acptno);
	noticeWin = winOpen(form, winName, cURL, nHeight, nWidth);
}

function lang_open(){
	  var $gnb = jQuery(".topMenu.lang_menu > ul > li > div");
	  //메뉴명 마우스 액션
	  $gnb.mouseenter(function(){
	      var $this = jQuery(this).children('a');
	      $this.css('color','#2471c8');
	      $this.siblings('div:eq(1)').addClass("active");
	      $('.menu_box').hide();
	      $this.siblings('div:eq(1)').show();
	   }),
	  $gnb.mouseleave(function(){
	      var $this = jQuery(this);
	      $this.children('a').css('color','');
	      $this.children('div').removeClass("active");
	  });
}
function clickSideMenu(event) {
	clearInterval(resizeInterval);
	
	event.preventDefault();
	var $iFrm = '';
	var pathName = event.target.getAttribute('link');
	if(pathName == null) pathName = event.target.value;
	
	// 접속 메뉴 request code 가져오기 수정.
	var findReqCd = false;
	for(var i=0; i<menuData.length; i++) {
		if(menuData[i].link === pathName && event.target.innerText === menuData[i].text) {
			reqCd = menuData[i].reqcd;
			findReqCd = true;
			break;
		}
	} 
	if(!findReqCd) reqCd = null;
	
	// 하위 메뉴일시만 이동
	if( pathName.indexOf('doneMove') < 0) {
		// 메뉴 이동시 열려있는 메뉴 닫아주기 수정
		$('.menu_box').removeClass('active');
		
		//IFRAME 지워준후 다시그리기
		$('#eCAMSFrame').empty();
		$iFrm = $('<IFRAME id="iFrm" frameBorder="0" name="iFrm" scrolling="yes" src="'+pathName+'" style="width:100%;  height:'+contentHeight+'px; min-width:1600px; min-height: 703px;" marginwidth="0" marginheight="0" onload="frameLoad()"></IFRAME>');
		$iFrm.appendTo('#eCAMSFrame');
		//mouseMove();

		//상위 TITLE TEXT SET
		var selectePtag = $(event.target);
		contentHistory = htmlFilter(selectePtag.parent('div.menu_box').siblings('a').text()) + "<strong> &gt; "+ htmlFilter(selectePtag.text())+"</strong>";
	}
}
function frameLoad(){
	$('#iFrm').contents().find('#history_wrap').html(contentHistory);
	
	if($('#iFrm').contents().find(".contentFrame").length == 0){
		return;
	} else {
		// iframe 내에서 드래그 막음 ( 셀렉트박스 의미없는 값 드래그 되어서 막음)
		$('#iFrm').contents().find(".contentFrame").attr('ondragstart','return false');
		
		// tab, iframe 으로 들어가는 body 에도 추가
		if($('#iFrm').contents().find("iframe").length > 0){
			var iframe = $('#iFrm').contents().find("iframe");
			$(iframe).each(function(){
				$(this).contents().find("body").attr('ondragstart','return false');
			});
		}
	}
	
	$('#iFrm').contents().find("html").css('overflow', 'hidden');
	var frameHeight = 0;
	var addHeight = 0;
	var check = 1;
	//contentHeight = window.innerHeight - $('#header').height() - $('#footer').height() - 20;
	contentHeight = window.innerHeight - $('#topMenu').height() - 15;

	resizeInterval = setInterval(function(){
		var resizeReturn = resize();	
		if(contentHeight <= 500){
			 frameHeight = 500;
		 } else {
			 frameHeight = contentHeight;
		 }

		 if($('#iFrm').height() == frameHeight || resizeReturn){
			 clearInterval(resizeInterval);
			 return;
		 }
	 },100);
}
//세로 스크롤바
(function($) {
    $.fn.hasVerticalScrollBar = function() {
        return this.get(0) ? this.get(0).scrollHeight > this.innerHeight() : false;
    }
})(jQuery);

function resize(){
	if($('#iFrm').contents().find(".contentFrame").length == 0){
		return;
	}
	 
	var addHeight = 0;
	var contentFrameHeight = 0;
	var frameHeight = 0;
	var limitSize = 0;
	 
	//contentHeight = window.innerHeight - $('#header').height() - $('#footer').height() - 20;
	contentHeight = window.innerHeight - $('#topMenu').height() - 15;
	
	
	if(contentHeight <= 500) {
		frameHeight = 500;
	} else {
		frameHeight = contentHeight;
	}
	
	if($('#iFrm').height() != frameHeight ){
		$('#iFrm').css("height", frameHeight + "px");
	}
	
	// 프레임 내부에 컨텐츠 div 높이 구해오기
	contentFrameHeight = Math.round($('#iFrm').contents().find(".contentFrame").height()) + 15; 

	
	// 프레임 내부 컨텐츠 높이 - 페이지 내부 높이 (스크롤 생기는 높이 구하기)
	limitSize = contentFrameHeight - frameHeight;
	
	// 프레임 내부에서 resize 클래스를 가진 그리드를 찾는다
	var ax5gridArr = $('#iFrm').contents().find("div[data-ax5grid].resize");
	
	if(typeof(ax5gridArr.length) == "undefined"){
		ax5gridArr = {};
	}
	var frameGridArr = {};
	
	// 내부 프레임 > 프레임 구조를 찾는다 (tab 등등..)
	if($('#iFrm').contents().find("iframe").length > 0) {
		$($('#iFrm').contents().find("iframe")).each(function() {
			if(!$(this).is(":visible")){
				return true;
			}
			frameGridArr = $(this).contents().find("div[data-ax5grid].frameResize");
			if(frameGridArr.length > 0) {
				ax5gridArr = $.extend({},ax5gridArr, frameGridArr);
			}
		});
	}
	
	// 그리드 수에 맞게 스크롤 생기는 높이를 나누어준다
	if(ax5gridArr.length > 1) {
		limitSize = limitSize / ax5gridArr.length;
	}
	
	// 그리드 리사이징 체크
	var gridResizeCk = false;
	
	// 내부 프레임 리사이징 체크
	var frameResizeCk = false;
	
	for(var i = 0; i < ax5gridArr.length; i++) {
		var ax5grid = $(ax5gridArr[i]);
		
		if(!$(ax5grid).is(":visible")){
			continue;
		}
		
		// 프레임 내부 > 프레임 구조 높이 수정
		if(ax5grid.hasClass("frameResize")) {
			// 스크롤이 생겼는지 확인
			if(ax5grid.parents("body").prop("scrollHeight") > ax5grid.parents("body").prop("clientHeight")) {
				
				// 내부 프레임이 여러개 있을 경우 첫번째 루프에서 limitSize를 구함
				if(!frameResizeCk){
					limitSize = ax5grid.parents("body").prop("scrollHeight") - ax5grid.parents("body").height();
				}
				
				// 내부 프레임내에 그리드가 여러개 있을 경우 첫번째 그리드에서 그리드 개수 만큼 limitSize를 나눔
				if(frameGridArr.length > 1 && !frameResizeCk) {
					limitSize = limitSize / frameGridArr.length;
				}
				frameResizeCk= true;
			} else {
				// 스크롤이 없다면 그리드에 전체 높이를 넣어주고 다시 돌린다.
				limitSize = ax5grid.parents("body").height() * -1;
				limitSize = limitSize / frameGridArr.length;
				$(frameGridArr).each(function(i){
					var ax5grid = $(frameGridArr[i]);
					var height = ax5grid.css("height");
					height = height.replace("px", "") - limitSize + "px";
					
					ax5grid.find("div[data-ax5grid-container='root']").css("height", height);
					
					var azBoardBasic = ax5grid.parent(".az_board_basic");
					azBoardBasic.css("height", height);
					
				});
				i--;
				continue;
			}
		}
		
		var height = ax5grid.css("height");
		height = height.replace("px", "") - limitSize + "px";
		
		ax5grid.find("div[data-ax5grid-container='root']").css("height", height);
		
		var azBoardBasic = ax5grid.parent(".az_board_basic");
		azBoardBasic.css("height", height);
		
		var floatCk = azBoardBasic.parent("div");
		
		// float로 들어간 형제 div 높이까지 바꾸어 준다(내부는 100%로 고정)
		if(floatCk.hasClass("r_wrap")) {
			floatCk.siblings(".l_wrap").css("height", height);
		}
		
		if(floatCk.hasClass("l_wrap")) {
			floatCk.siblings(".r_wrap").css("height", height);
		}
		gridResizeCk = true;
	}
	
	if(gridResizeCk) {
		contentFrameHeight = Math.round($('#iFrm').contents().find(".contentFrame").height()); 
	}
	
	if(contentFrameHeight > frameHeight && gridResizeCk) {
		if($('iFrm').height() != contentFrameHeight) {
			$('#iFrm').css("height", contentFrameHeight + "px");
			return true;
		}
	}
}
function loadEcamsMain() {
	contentHistory = "형상관리프로세스";
	$('#eCAMSFrame').empty();
	$iFrm = $('<IFRAME id="iFrm" frameBorder="0" name="iFrm" scrolling="yes" src="/webPage/main/Process.jsp" style="width:100%; height:'+contentHeight +'px; min-width:1004px; min-height: 703px;" marginwidth="0" marginheight="0" onload="frameLoad()"></IFRAME>');
	$iFrm.appendTo('#eCAMSFrame');
	//mouseMove();
}

function logOut() {
	//local storage clear
	sessionStorage.clear();
//	window.close();
//	window.open("about:blank","_self").close();
	window.location.replace('/webPage/login/ecamsLogin.jsp');
}

//미결/SR/오류 건수 클릭시 페이지 이동
function changePage(division) {
	var pathName = '';
	var mainTitle = '';
	var subTitle = '';
	
	if(division == 'approval_mb') {
		pathName = '/webPage/approval/ApprovalStatus.jsp';
		mainTitle = '결재확인';
		subTitle = '결재현황';
	} else if(division == 'srCnt' || division == 'srReg_mb') {
		pathName = '/webPage/register/SRStatus.jsp';
		mainTitle = '등록';
		subTitle = 'SR진행현황(등록)';
	} else if(division == 'errCnt') {
		pathName = '/webPage/approval/RequestStatus.jsp';
		mainTitle = '결재확인';
		subTitle = '신청현황';
	} else if(division == 'devSr_mb') {
		pathName = '/webPage/apply/ApplyRequest.jsp';
		mainTitle = '개발';
		subTitle = '체크인';
	} else if(division == 'testSr_mb') {
		pathName = '/webPage/apply/ApplyRequest.jsp';
		mainTitle = '테스트';
		subTitle = '테스트배포';
	} else if(division == 'appySr_mb') {
		pathName = '/webPage/apply/ApplyRequest.jsp';
		mainTitle = '운영';
		subTitle = '운영배포';
	} else {
		return;
	}
	
	$('#eCAMSFrame').empty();
	$iFrm = $('<IFRAME id="iFrm" frameBorder="0" name="iFrm" scrolling="yes" src="'+pathName+'" style="width:100%; height:'+contentHeight+'px; min-width:1004px; min-height: 703px;" marginwidth="0" marginheight="0"  onload="frameLoad()"></IFRAME>');
	$iFrm.appendTo('#eCAMSFrame');
	//mouseMove();
	
	//상위 TITLE TEXT SET
	contentHistory = mainTitle + "<strong> &gt; "+ subTitle+"</strong>";
}

function changeScreen(titleName, fileName, reqcd) {
	
	reqCd = reqcd
	
	$('#eCAMSFrame').empty();
	$iFrm = $('<IFRAME id="iFrm" frameBorder="0" name="iFrm" scrolling="yes" src="'+fileName+'" style="width:100%; height:'+contentHeight+'px; min-width:1004px; min-height: 703px;" marginwidth="0" marginheight="0"  onload="frameLoad()"></IFRAME>');
	$iFrm.appendTo('#eCAMSFrame');
	contentHistory = titleName;
}

//세션 유지 마우스 이동 함수
function mouseMove(){
	var child = document.getElementById('iFrm'); //iframe
	child.contentWindow.addEventListener('mousemove', function(event) {
		var agent = navigator.userAgent.toLowerCase();
		if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
		     // ie일 경우
		    var params = params || { bubbles: true, cancelable: false, detail: undefined };
		    var evt = document.createEvent( 'CustomEvent' );
		    evt.initCustomEvent( event, params.bubbles, params.cancelable, params.detail );
		    CustomEvent.prototype = window.Event.prototype;
		    timer = 0;
		    window.CustomEvent = CustomEvent;
		}else{
		     // ie가 아닐 경우
			var evt = new CustomEvent('mousemove', {bubbles: true, cancelable: false});
			timer = 0;
			child.dispatchEvent(evt);
		}
	});
}