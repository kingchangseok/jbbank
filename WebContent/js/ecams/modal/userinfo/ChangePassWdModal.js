
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var txtUserId 	= window.parent.txtUserIdP;		// 사용자정보화면 사원번호에 적혀있는 ID

var lastPass		= null;
var phoneNumber		= '';
var befPass			= '';
var befEncPassWd	= '';
var updtEncPassWd 	= '';

//브라우저 검사
var agent = navigator.userAgent.toLowerCase();

if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) || (agent.indexOf("edge") != -1)) {	//익스일떄
	$("#txtPw").prop("type", "password");
	$("#txtUpdatePw1").prop("type", "password");
	$("#txtUpdatePw2").prop("type", "password");
} else {
	$("#txtPw").prop("type", "text");
	$("#txtUpdatePw1").prop("type", "text");
	$("#txtUpdatePw2").prop("type", "text");
}

$(document).ready(function() {
	if (userId == null || userId == '') return;
	
	if (txtUserId != userId) {
		dialog.alert('비정상적 접근입니다. 로그인을 다시 하시기 바랍니다.', function(){window.parent.parent.location.replace('/webPage/login/ecamsLogin.jsp');popClose();});
		return;
	}
	
	//닫기,취소
	$('#btnCncl').bind('click',function() {
		popClose();
	});
	
	//비밀번호 변경
	$('#btnPw').bind('click',function() {
		checkPwdVal();
	});
	getPasswdBef();
	getLastPasswdBef();
});

// 모달 닫기
function popClose() {
	window.parent.chgPwdModal.close();
}

//변경전 비밀번호 가져오기
function getPasswdBef() {
	var data = new Object();
	data = {
		userId 		: userId,
		requestType : 'getPasswdBef'
	}
	ajaxAsync('/webPage/mypage/PwdChange', data, 'json',successGetPasswdBef);
}

// 변경전 비밀번호 가져오기 완료
function successGetPasswdBef(data) {
	befPass = data;
	if(typeof(befPass) == "object"){
		befPass = data[0].cm_cpasswd;
		if(befPass == null || befPass == undefined){
			befPass = data[0].cm_dumypw;
		}
	}
	$("#txtPw").focus();
	if (befPass.length != 64) { //기존 암호화가 안되어 있으면
		encryptPasswdBef(befPass);
	}
}

//변경전 비밀번호 암호화
function encryptPasswdBef(befPass) {
	var data = new Object();
	data = {
		userId 		: userId,
		userPwd  : befPass,
		requestType : 'encryptPassWd'
	}
	ajaxAsync('/webPage/mypage/PwdChange', data, 'json',successEncryptPassWd);
}

//변경전 비밀번호 암호화 완료
function successEncryptPassWd(data) {
	befPass = data;
}

// 마지막 변경전 비밀번호 가져오기 완료
function successGetLastPasswdBef(data) {
	lastPass = data;
}

// 마지막 변경전 비밀번호 가져오기
function getLastPasswdBef() { 
	var data = new Object();
	data = {
		userId 		: userId,
		requestType : 'getLastPasswdBef'
	}
	ajaxAsync('/webPage/mypage/PwdChange', data, 'json',successGetLastPasswdBef);
}

// 변경 비밀번호 유효성 체크
function checkPwdVal() {
	var pw 			= $('#txtPw').val().trim();
	var updatePw1 	= $('#txtUpdatePw1').val().trim();
	var updatePw2 	= $('#txtUpdatePw2').val().trim();
	
	// 영어,숫자,특수문자 1개이상씩 확인
	var patternBase = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{6,}$/; 
	var temp 	= null;
	var samenum = false;
	var connum 	= false;
	
	if (pw.length === 0 ) {
		dialog.alert('변경전 비밀번호를 입력해주세요.', function(){});
		return;
	}
	if (updatePw1.length === 0 ) {
		dialog.alert('설정하실 비밀번호를 입력해주십시요.', function(){});
		return;
	}
	
	
	if (updatePw1.length < 8 || updatePw2.length > 12) {
		dialog.alert('비밀번호는 8-12자리로 설정하여 주십시요.', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw1").focus();
		return;
	} 
	/*
	for (i = 0; i < updatePw1.length; i++) {
		if (temp == updatePw1.charAt(i) && temp >= '0' && temp <= '9') {
			samenum = true;
		} else if (temp != updatePw1.charAt(i) && temp != null
				&& temp >= '0' && temp <= '9') {
			temp1 = Number(temp) + 1;
			temp2 = Number(updatePw1.charAt(i));

			if (temp1 == temp2) {
				connum = true;
			}
		}
		temp = updatePw1.charAt(i);
	}
	*/
	for (i = 0; i < updatePw1.length; i++) {
		if (temp == updatePw1.charAt(i) && temp >= '0' && temp <= '9') {
			samenum = true;
			break;
		}
		temp = updatePw1.charAt(i);
	}
	
	
	temp = '123456789';
	for (i = 0; i < updatePw1.length-4; i++) {
		//console.log(updatePw1.substr(i,4), temp.indexOf(updatePw1.charAt(i,4)));
		if (temp.indexOf(updatePw1.substr(i,4))>-1) {
			connum = true;
			break;
		}
	}
	if(!patternBase.test(updatePw1)) {
		dialog.alert('영문/숫자/특수문자 조합으로 등록이 가능합니다.', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw1").focus();
		return;
	}
	if (samenum) {
		dialog.alert('비밀번호에 연속된 동일숫자가 있어 불가합니다.', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw1").focus();
		return;
	}
	if (connum) {
		dialog.alert('비밀번호에 연속숫자가 있어 사용불가합니다', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw1").focus();
		return;
	}
	if (updatePw1 == userId) {
		dialog.alert('변경비밀번호와 사번이 일치합니다.', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw1").focus();
		return;
	}
	if (updatePw1.indexOf(phoneNumber) > 0) {
		dialog.alert('전화번호 사용불가합니다.', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw1").focus();
		return;
	}
	if (updatePw1 !== updatePw2) {
		dialog.alert('변경비밀번호와 확인비밀번호가 일치하지 않습니다.', function(){});
		$("#txtUpdatePw2").val('');
		$("#txtUpdatePw2").focus();
		return;
	}
	
	encryptTxtPw();
}

// 입력변경전비밀번호 암호화
function encryptTxtPw() { 
	var pw = $('#txtPw').val().trim();
	var data = new Object();
	data = {
		userId 		: userId,
		userPwd	: pw,
		requestType : 'encryptPassWd'
	}
	ajaxAsync('/webPage/mypage/PwdChange', data, 'json',successEncryptTxtPw);
}

// 입력변경전비밀번호 암호화 완료
function successEncryptTxtPw(data) {
	befEncPassWd = data;
	checkBefPass();
}

//변경후 비밀번호 암호화하기
function checkBefPass() {
	var updatePw1 = $('#txtUpdatePw1').val().trim();
	var data = new Object();
	data = {
		userId 		: userId,
		userPwd	: updatePw1,
		requestType : 'encryptPassWd'
	}
	ajaxAsync('/webPage/mypage/PwdChange', data, 'json',successCheckBefPass);
}

// 변경후 비밀번호 암호화하기 완료
function successCheckBefPass(data) {
	updtEncPassWd = data;
	if (befPass !== befEncPassWd) {
		dialog.alert('변경전 비밀번호가 일치하지 않습니다.', function(){});
		$("#txtPw").val('');
		$("#txtPw").focus();
		return;
	}
	if (befPass === updtEncPassWd) {
		dialog.alert('변경전 비밀번호와 변경후 비밀번호가 일치합니다.', function(){});
		$("#txtUpdatePw1").val('');
		$("#txtUpdatePw1").focus();
		return;
	}
	
	for (var i = 0; i < lastPass.length; i++) {
		if (lastPass[i].lst_passwd === updtEncPassWd) {
			dialog.alert('변경후 비밀번호가 이전에 변경된 비밀번호와 일치합니다.', function(){});
			$("#txtUpdatePw1").val('');
			$("#txtUpdatePw2").val('');
			$("#txtUpdatePw1").focus();
			return;
		}
	}
	setPassWd();
}

// 비밀번호 변경
function setPassWd() {
	var updatePw1 = $('#txtUpdatePw1').val().trim();
	var data = new Object();
	data = {
		userId 		: userId,
		userPwd	: updatePw1,
		requestType : 'setPassWd'
	}
	ajaxAsync('/webPage/mypage/PwdChange', data, 'json',successSetPassWd);
}

//비밀번호 변경 완료
function successSetPassWd(data) {
	if (data > 0) {
		dialog.alert('비밀번호 변경이 완료되었습니다. 다시 로그인해주시기 바랍니다.', function(){window.parent.parent.location.replace('/webPage/login/ecamsLogin.jsp');popClose();});
	} else {
		dialog.alert('비밀번호 변경 실패', function() {});
	}
}