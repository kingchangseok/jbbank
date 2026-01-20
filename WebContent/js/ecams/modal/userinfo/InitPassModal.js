/**
 * [사용자정보 > 비밀번호초기화] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-25
 * 
 */
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var txtUserId 	= window.parent.txtUserIdP;		// 사용자정보화면 사원번호에 적혀있는 ID

$(document).ready(function() {
	
	console.log(escape('ecams12'));
	
	if(txtUserId !== undefined && txtUserId.length > 0) {
		$('#txtUserId').attr('disabled', true);
		$('#txtUserId').val(txtUserId);
	}
	
	// 등록 클릭
	$('#btnReq').bind('click', function() {
		var txtUserId = $('#txtUserId').val().trim();
		var txtPasswd = $('#txtPasswd').val().trim();
		var numSw	  = null;
		if(txtPasswd.length !== 4) {
			dialog.alert('초기화비밀번호는 4자리로 입력해주세요.', function(){});
			return;
		}
		numSw = txtPasswd.search(/[0-9]/g);
		
		if(numSw !== 0 ) {
			dialog.alert('초기화비밀번호는 숫자로만 입력하셔야 합니다.', function(){});
			return;
		}
		var data = new Object();
		data = {
			user_id	 	: txtUserId,
			JuMinNUM	: txtPasswd,
			requestType	: 'PassWd_reset'
		}
		ajaxAsync('/webPage/ecmm/Cmm1700Servlet', data, 'json',successResetPassWord);
	});
	// 닫기 클릭
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 비밀번호 초기화 성공
function successResetPassWord(data) {
	if(Number(data) > 0) {
		dialog.alert('비밀번호 초기화가 완료되었습니다.',function(){
			popClose();
		});
	}else {
		dialog.alert('비밀번호 초기화에 실패하였습니다.',function(){});
		$('#txtPasswd').val('');
		return;
	}
}

// 모달 닫기
function popClose() {
	window.parent.initPassModal.close();
}