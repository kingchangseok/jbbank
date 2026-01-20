/**
 * [기본관리/사용자환경설정] SVN_INFO
 */


var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var codeList    = window.top.codeList;          //전체 코드 리스트

var tmpInfo = {};

$(document).ready(function() {
	
	//svn 정보 가져오기
	getSvnInfo();
	
	//저장
	$("#btnSave").bind("click", function(){
		btnSave_Click();	
	});
	
})

function getSvnInfo(){
	
	tmpInfo = {
		UserID		: userId,
		requestType	: 'getSvnInfo'
	}
	
	var svnData = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');
	
	if(svnData.length != 0){
		$('#UpdateId').val(svnData[0].cm_svnid);
		$('#UpdatePw').val(svnData[0].cm_svnpasswd);
	}
}


function btnSave_Click(){
	
	var idText = $('#UpdateId').val().trim();
	var pwText = $('#UpdatePw').val().trim();

	if (idText == '' || idText == null){
		dialog.alert("SVN ID를 입력하세요");
		$('#UpdateId').focus();
		return;
	}
	if (pwText == '' || pwText == null){
		dialog.alert("SVN PASSWORD를 입력하세요");
		$('#UpdatePw').focus();
		return;
	}
	
	tmpInfo = {
		svnId	: idText,
		svnPw	: pwText,
		userId	: userId,
		requestType	: 'SvnInfoUpdt'
	}
	
	var svnResult = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');

	if(svnResult == 'OK'){
		dialog.alert('등록처리가 완료되었습니다.');
	}
}


