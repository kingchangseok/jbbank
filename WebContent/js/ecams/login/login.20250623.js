/**
 * 로그인 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */
var pwdChangeWin = null;
var isSSO = false;

$(document).ready(function() {
	//로그인 화면 이미지 랜덤 세팅
	var ranInt = Math.ceil(Math.random() * 16);
	$("#loginImg").attr("src", "/img/login/login_img" + ranInt + ".jpg");
	
	//$('body').css('background','#f8f8f8');
	$('input:checkBox[name=saveInfo]').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	//$('input:checkBox[name=savePwd]').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	screenInit();
});

function screenInit() {
	$('#ecamsLoginForm').bind('submit', loginSubmitAction);
	var ssoId = sessionStorage.getItem('strUsr_UserId');
	if(ssoId != null && ssoId != undefined) {	
		isSSO = true;
		$('#idx_input_id').val(ssoId);
		$('#idx_input_pwd').val('SSO');
		$('#saveInfo').wCheck('check', false);
		$("#ecamsLoginForm").submit();
	} else {		
		$(".login-form").css({"display" : "inline-block", "width" : "100%"});
		setInput();
	}
}

function setInput() {
	if( getCookie('remember') === 'true'){
		$('#idx_input_id').val(getCookie('ecams_id'));
		$('#saveInfo').wCheck("check",true);
	}else {
		$('#idx_input_id', 	'');
	}
	
	/*if( getCookie('rememberpwd') === 'true'){
		$('#idx_input_pwd').val(getCookie('ecams_pwd'));
		$('#savePwd').wCheck("check",true);
	}else {
		$('#idx_input_pwd', '');
	}*/
}

function fnRsaEnc(value, rsaPublicKeyModulus, rsaPublicKeyExponent) {
    var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);

    var encValue = rsa.encrypt(value);     // 사용자ID와 비밀번호를 RSA로 암호화한다.
    return encValue;
}

var loginSubmitAction = function(e) {
	sessionStorage.clear();
	
	e.preventDefault();
    e.stopPropagation();
    
    var validationCheckFlag = checkValidation(); //false
    var selectedRemember 	= $('#saveInfo').is(":checked");
    //var selectedPassWd 		= $('#savePwd').is(":checked");
    var loginValidReturnStr = null;
    var authCode 			= null;
    var userId 				= null;
    var sessionID			= null;
    
    if( ! validationCheckFlag ) return;

    if(selectedRemember) {
		setCookie('ecams_id', 	$('#idx_input_id').val());
    	setCookie('remember', 	true);
	} else {
		setCookie('ecams_id', 	'');
    	setCookie('remember', 	false);
	}
    
    /*if(selectedPassWd) {
    	setCookie('ecams_pwd', 		$('#idx_input_pwd').val());
    	setCookie('rememberpwd', 	true);
	} else {
    	setCookie('ecams_pwd', 		'');
    	setCookie('rememberpwd', 	false);
	}*/
    
    try {
    	var encPassword = fnRsaEnc($('#idx_input_pwd').val(), $('#rsaPublicKeyModulus').val(), $('#rsaPublicKeyExponent').val());
    	if (null == encPassword || encPassword == '' || encPassword == undefined || encPassword == 'undefined') {
    		dialog.alert('아이디 또는 비밀번호를 다시 확인하세요.', function() {
    			return;
    		});
    	}
	} catch (err) {
		dialog.alert(err);
		return;
	} finally {
		//$('#idx_input_pwd').val(''); //개발 상태에서 값을 넘겨야함으로 주석처리
	}
	
    loginValidReturnStr = isValidLogin(encPassword, sessionCk());

    loginValidReturnStr = String(loginValidReturnStr);
    console.log('loginValidReturnStr='+loginValidReturnStr);
    if (loginValidReturnStr == undefined || loginValidReturnStr == 'undefined' || loginValidReturnStr == 'null'
    	|| loginValidReturnStr == null || loginValidReturnStr == '') return;
    var i =0;
    while(loginValidReturnStr.indexOf("나누기") > 0 && i<5){
    	loginValidReturnStr = isValidLogin(encPassword, sessionCk());
    	loginValidReturnStr = String(loginValidReturnStr);
    	i++;
    }
    if (loginValidReturnStr.indexOf('ERROR') > -1) {
		location.reload();
    }
    if (loginValidReturnStr.indexOf('ENCERROR')>-1) {
		dialog.alert('비정상접근입니다. 다시 로그인 하시기 바랍니다.', function() {
			$('#idx_input_id').val('');
			$('#idx_input_pwd').val('');
			return;
		});
    }
    
    authCode= loginValidReturnStr.substring(0,1);
    
    if (authCode != '7') userId	= loginValidReturnStr.substring(1);
    
    console.log('### authCode:',authCode);
    
    /*
  	auth_rtn==0:정상적인 로그인
	auth_rtn==3:비번초기화 후 입력비번이랑 주민번호랑 일치 할때
	auth_rtn==9:형상관리시스템 관리자 정상적인로그인
	*/  
   
    if ( authCode === '0' || authCode === '3' || authCode === '9' || authCode === '6') {   
    	if(authCode === '3') {
    		dialog.alert('비밀번호가 초기화 되었습니다.\n비밀번호 재설정 후 이용해 주시기 바랍니다.', function() {
    			openPwdChange(userId);
    		});
    		return;
    	}
    	
    	//비번변경 주기 초과 했을 경우
    	if(authCode === '6') {
    		confirmDialog.confirm({
    			title: '확인',
    			msg: "비밀번호 유효 기간이 초과되었습니다.\n비밀번호 재설정 후 이용해 주시기 바랍니다."
    		}, function(){
    			if(this.key === 'ok') {
    				openPwdChange(userId);
    			}
    		});
    		return;
    	}

		sessionStorage.removeItem('id');
    	sessionStorage.setItem('id', userId);
    	
    	updateLoginIp(userId);
    	
	    var form = document.createElement("form");
        form.action = '/webPage/main/eCAMSBase.jsp';
        form.method = "post";
        document.body.appendChild(form);
        form.submit();
        
    } else {
    	sessionStorage.clear();
    	
    	//에러카운드 초과 했을때
    	if (authCode === '2') {
    		dialog.alert('비밀번호 오류입력횟수를 초과하였습니다. 비밀번호초기화 또는 관리자에게 문의하여 주십시오.', function() {});
    	}
    	//비밀번호 틀렸을경우
    	if (authCode === '4') {
    		dialog.alert('개인번호와 비밀번호가 일치하지 않습니다. 확인 후 다시 로그인하십시오.', function() {
    			$('#idx_input_pwd').val('');
    		});
    	}
    	//DB 사용자정보가 없을때
    	if (authCode === '7') {
    		dialog.alert('[' + $('#idx_input_id').val() + '] 개인번호가 존재하지 않습니다. 확인 후 다시 로그인하십시오.', function() {});
    	}
    	//cm_active == 0 일때
    	if (authCode === '1') {
    		dialog.alert('입력하신 개인번호가 유효하지 않습니다. 관리자에게 문의하여 주십시오.', function() {});
    	}
    	//CM_JUMINNUM 주민번호가 null 일때
    	if (authCode === '5') {
    		dialog.alert('비밀번호 초기화 후 로그인하십시오.', function() {});
    	}
    }
};

function openPwdChange(userId) {
	var nHeight, nWidth, nTop, nLeft, cURL, cFeatures, winName;
	if (pwdChangeWin != null 
			&& !pwdChangeWin.closed ) {
		pwdChangeWin.close();
	}

    winName = 'pwdChangeWin';
	nHeight = 590;
    nWidth  = 1026;
    cURL = "../../webPage/mypage/PwdChange.jsp";
	
	nTop  = parseInt((window.screen.availHeight/2) - (nHeight/2));
	nLeft = parseInt((window.screen.availWidth/2) - (nWidth/2));
	cFeatures = "top=" + nTop + ",left=" + nLeft + ",height=" + nHeight + ",width=" + nWidth + ",help=no,menubar=no,status=yes,resizable=yes,scroll=no";

	var f = document.popPam;   		//폼 name
	pwdChangeWin = window.open('',winName,cFeatures);
    
    
    f.userId.value	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.winPopSw.value= 'true';    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.action		= cURL; 		//이동할 페이지
    f.target		= winName;    	//폼의 타겟 지정(위의 새창을 지정함)
    f.method		= "post"; 		//POST방식
    f.submit();
}

/* token 방식으로 변경 세션저장 사용안함
function setSessionLoginUser(userId) {
	var userInfo = {
		userId		: 	userId,
		requestType	: 	'SETSESSION'
	}
	return ajaxCallWithJson('/webPage/login/Login', userInfo, 'json');
}
*/
function updateLoginIp(userId) {
	var form = document.popPam;
	var IpAddr = form.custIP.value;
	var Url = form.Url.value;
	
	var userInfo = {
		userId		: userId,
		IpAddr		: IpAddr,
		Url 		: Url, 
		requestType	: 'UPDATELOGINIP'
	}
	console.log('userInfo:',userInfo);
	return ajaxCallWithJson('/webPage/login/Login', userInfo);
}

function isValidLogin(encPassword, encPassword2) {
	
	var ajaxReturnData = null;
	
	var form = document.popPam;
	var ipAddr = form.custIP.value;
	var url = form.Url.value;

	var userInfo = {
		userId		 : 	$('#idx_input_id').val(),
		userPwd		 : 	encPassword,//$('#idx_input_pwd').val()
		userPwd2	 : 	encPassword2,//$('#idx_input_pwd').val()
		userPwdDev   : 	$('#idx_input_pwd').val(),
		gnb			 : 	"Real", // 개발 상태확인 (세션 공유가 안되므로 암호화 안함)
		ipAddr		 : ipAddr,
		url 		 : url,
		sso			 : isSSO,
		requestType	 : 'ISVALIDLOGIN'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/login/Login', userInfo, 'json');
	
	return ajaxReturnData;
}

function checkValidation() {
	var cookieId = $('#idx_input_id').val();
	//var cookiePwd = $('#idx_input_pwd').val();
	var validationFlag = false;
	if( cookieId !== undefined && cookieId !== '') validationFlag = true; //&& cookiePwd !== undefined && cookiePwd !== ''
	else validationFlag = false;
	
	return validationFlag;
}

function sessionCk(){
	var data = {
		requestType	 : 'sessionCk'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/login/Login', data, 'json');

	var rsaKey = ajaxReturnData.split(",");
	return fnRsaEnc($('#idx_input_pwd').val(), rsaKey[0], rsaKey[1])
}