/**
 * 사용자환경설정 화면 기능정의
 * 	작성자: 정선희
 * 	버전 : 1.0
 *  수정일 : 2020-12-15
 */

var userId 		= window.top.userId;		// 접속자 ID

var grid1Sw = false;
var grid2Sw = false;
var grid3Sw = false;
var grid4Sw = false;

var notiData = null;


$(document).ready(function(){
	$('#tab1Li').width($('#tab1Li').width()+10);
	setTabMenu();
});

// 탭 메뉴 셋
function setTabMenu(){
	$(".tab_content:first").show();

	$("ul.tabs li").click(function () {
		$(".tab_content").css('display','none');
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		$('#'+activeTab).css('display','block');
	});
}

