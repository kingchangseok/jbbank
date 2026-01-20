/**
 * 빌드/릴리즈 화면 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-06-17
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var urlArr = [];

$(document).ready(function(){
	$('#tab1Li').width($('#tab1Li').width()+10);
	$('#tab2Li').width($('#tab2Li').width()+10);
	setTabMenu();
});

function setTabMenu(){
	$(".tab_content:first").show();
	
	$("ul.tabs li").click(function () {
		$(".tab_content").hide();
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		
		$("#" + activeTab + " iframe").attr('src', $("#" + activeTab + " iframe").attr('src'));
		$("#" + activeTab).fadeIn();
	});
}
