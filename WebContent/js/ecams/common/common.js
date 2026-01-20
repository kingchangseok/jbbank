/**
 * eCAMS 공통 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문 +++
 * 	버전 : 1.1
 *  수정일 : 2019-02-07
 */
var userId;// 접속자 ID

var dialog 			= new ax5.ui.dialog({title: "확인"});
var confirmDialog 	= new ax5.ui.dialog();	//알럿,확인창
var mask 			= new ax5.ui.mask();
var picker			= new ax5.ui.picker();
var homePath 		= window.location.protocol+'//'+window.location.hostname + ':' + window.location.port;
var loading_div		= "<div class='loding-div' style='display:none;'><div class='loding-img'><img alt='' src='/img/loading_gird.gif'></div></div>";
var loading_div2	= "<div class='loding-div2' style='display:none;'><div class='loding-img'><img alt='' src='/img/loading_gird.gif'></div></div>";
var path			= null;
var closeCk 		= null;
var popUpList 		= [];


/* 취약점수정 (2025) start*/
userId = sessionStorage.getItem('id');
if(userId != null && userId != '' && userId != undefined) {
	var pathname = window.location.href;
	pathname = replaceAllString(pathname,homePath,'');
	pathname = replaceAllString(pathname,'//','/');
	/*
	if (pathname.indexOf('/webPage/winpop')<0 && pathname.indexOf('/webPage/modal') && pathname.indexOf('/webPage/tab') &&
		pathname.indexOf('/webPage/login')<0 && pathname.indexOf('/webPage/main') && pathname.indexOf('/webPage/')>=0) {
	 */

	if (pathname.indexOf('/webPage/winpop') < 0 && pathname.indexOf('/webPage/modal') < 0 && pathname.indexOf('/webPage/tab') < 0 &&
		pathname.indexOf('/webPage/login') < 0 && pathname.indexOf('/webPage/main') < 0 && pathname.indexOf('/webPage/') >= 0) {				
		var data = new Object();
		data = {
				userId : userId,
			  pathname : pathname,
		   requestType : 'CHECK_SECU'
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/main/eCAMSBaseServlet', data, 'json');
		if(ajaxReturnData != null && ajaxReturnData != '' && ajaxReturnData == 'OK') {
		} else {
			dialog.alert('로그인정보가 유효하지 않습니다.\n다시 로그인하여 주시기 바랍니다.');
			window.top.location.replace('/webPage/login/ecamsLogin.jsp');
		}
	}
}
/*  취약점수정 (2025) end */

confirmDialog.setConfig({
    title: "선택창",
    theme: "info"
});

ax5.info.weekNames = [
	{label: "일"},
	{label: "월"},
	{label: "화"},
	{label: "수"},
	{label: "목"},
	{label: "금"},
	{label: "토"}
];

function copyReferenceNone(copyArray){
	return JSON.parse(JSON.stringify(copyArray));  // 
}

function extCk(filename) {
	var ext = "";
	if(filename.lastIndexOf(".") > -1) {
		ext = filename.substring(filename.lastIndexOf(".")+1);
		
		var disabledExt = [];
		disabledExt.push("exe");
		disabledExt.push("jsp");
		disabledExt.push("dll");
		disabledExt.push("jspx");
		disabledExt.push("php");
		disabledExt.push("asp");
		disabledExt.push("aspx");
		disabledExt.push("java");
		disabledExt.push("bat");
		disabledExt.push("class");
		disabledExt.push("pdb");
		disabledExt.push("vbs");
		disabledExt.push("css");
		disabledExt.push("js");
		disabledExt.push("html");
		disabledExt.push("htm");
		disabledExt.push("cab");
		disabledExt.push("cgi");
		disabledExt.push("sh");
		
		for (var i = 0; i < disabledExt.length; i++) {
			if(disabledExt[i] == ext) {
				return "FAIL";
			}
		}
	}
	return "OK";
}

function logoutCallback(message) {
	if (message == 'TIMEOUT') {
		message = 'token정보 없음';
	} else if (message == 'EXPIRED') {
		message = 'token유효시간초과';
	} else if (message == 'DISAGREE') {
		message = '로그인정보불일치';
	} else if (message == 'FAIL') {
		message = '로그인실패';
	}
	sessionStorage.clear();
	
	dialog.alert(message+"\n다시 로그인 하시기 바랍니다.", function() {
		var localUrl = document.location.href;
		//console.log(localUrl);
		if (localUrl.indexOf('/winpop')>-1) {
			window.open("about:blank","_self").close();
			//window.opener.top.location.href='/webPage/login/ecamsLogin.jsp';
			//팝업창 여러개일때도 한번에 닫기 로직필요
			window.self.close();
			window.parent.close();
		} else {
			window.open("about:blank","_self").close();
			//window.top.location.href = '/webPage/login/ecamsLogin.jsp';
		}
	});
	
	return;
}

function ajaxCallWithJson(url, requestData, dataType) {
	var token = sessionStorage.getItem('access_token');

	var requestJson = JSON.stringify(requestData);
	var obj = JSON.parse(requestJson);
	var token_result = 'OK';
	
	var successData = null;
	try {
		if (obj.requestType != 'CODE_INFO' && obj.requestType != 'addAdminAccess' && obj.requestType != 'ISVALIDLOGIN_NEW' 
			&& obj.requestType != 'getJobInfo' && obj.requestType != 'getUserRgtCd' && obj.requestType != 'sessionCk') {
			token_result = tokenCheck(token, url, requestData);
		
			if (token_result.message != "OK") {
				logoutCallback(token_result.message);
				return;
			}
		}
		token = sessionStorage.getItem('access_token');
		var errSw = false;
		
		var jsonValue 		= null;
		var errorMessage 	= null;
		
		$.ajax({
			type 	: 'POST',
			url 	: homePath+url,
			contentType: "application/json; charset=UTF-8",
			headers: {"Authorization": 'Bearer '+token },
			data 	: requestJson,
			dataType: dataType,
			async 	: false,
			
			success : function(data) {
				if ( url == '/webPage/login/Login' && (obj.requestType == 'ISVALIDLOGIN') ) {
					if (null != token_result.token && '' != token_result.token) {
						sessionStorage.removeItem('access_token');
			        	sessionStorage.setItem( "access_token", token_result.token );
					}
				}
				successData =  copyReferenceNone(data);
			},
			error 	: function(req, stat, error) {
				errSw = true;
				
				alert(url + "\n" +requestData.requestType + " 에러 \n" + "관리자에게 문의 바랍니다.");

				$(".loding-div").remove();
				$('#btnSearch').prop('disabled', false);
				$('#btnQry').prop('disabled', false);
				$('#btnFind').prop('disabled', false);
				ingSw = false;
				qrySw = false;
				reqSw = false;
				ing_sw = false;
				
				return;
				jsonValue 		= $.parseJSON(req.responseText);	// response
				errorMessage 	= jsonValue.message;				// response error mesage
				var agent 			= navigator.userAgent.toLowerCase();// ie 援щ텇
				
				var jsonArr = req.responseJSON.message.split("\r\n\tat");
				if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
					alert("오류발생 : " + replaceAllString(req.responseJSON.message.split("\r\n\tat")[0],"java.lang.Exception: ",""));
				} else {
					alert(url + "\n" +requestData.requestType + " 에러 \n" + "관리자에게 문의 바랍니다.");
				}
				
				
				if (agent.indexOf("msie") != -1 || agent.indexOf("trident") != -1) {    
					var lineCnt = 0;
					var lineError = '';
					errorMessage.split("\n").forEach(function(line) { 
						lineCnt++;
						lineError += line;
						if(lineCnt > 7) {
							console.log(lineError);
							lineCnt = 0;
							lineError = '';
						}
					});
					console.log(lineError);
				}
				if (agent.indexOf("chrome") != -1) {
					console.log(errorMessage);
				}
			}
		});
		if(!errSw) {
			if(successData != null) return successData;
		}else {
			return 'ERROR' + errorMessage;
		}
	} finally{
		successData = null;
		token_result = null;
		obj = null;
		requestJson = null;
		token = null;
	}
};

function ajaxCallWithJson_old(url, requestData, dataType) {
	var token = sessionStorage.getItem('access_token');

	var requestJson = JSON.stringify(requestData);
	var obj = JSON.parse(requestJson);
	var token_result = 'OK';
	
	var successData = null;
	try {
		if (obj.requestType != 'CODE_INFO' && obj.requestType != 'addAdminAccess'
			&& obj.requestType != 'getJobInfo' && obj.requestType != 'getUserRgtCd' && obj.requestType != 'sessionCk') {
			token_result = tokenCheck(token, url, requestData);
		
			if (token_result.message != "OK") {
				logoutCallback(token_result.message);
				return;
			}
		}
		token = sessionStorage.getItem('access_token');
		var errSw = false;
		
		var jsonValue 		= null;
		var errorMessage 	= null;
		
		$.ajax({
			type 	: 'POST',
			url 	: homePath+url,
			contentType: "application/json; charset=UTF-8",
			headers: {"Authorization": 'Bearer '+token },
			data 	: requestJson,
			dataType: dataType,
			async 	: false,
			/** 
			 * async 안쓰는게 좋다고 함
			 * [Deprecation] Synchronous XMLHttpRequest on the main thread is deprecated because of its detrimental effects to the end user's experience. 
			 * For more help, check https://xhr.spec.whatwg.org/.
			 * 메인 쓰레드에서의 동기화된 XMLHttpRequest는 사용자 경험에 안좋은 영향을 미치기 때문에 더이상 사용하지 않습니다. 더 자세한 사항은 http://xhr.spec.whatwg.org/  를 참고해 주십시오.
			 */
			success : function(data) {
				if ( url == '/webPage/login/Login' && (obj.requestType == 'ISVALIDLOGIN') ) {
			    	//local storage 에 token 저장
					if (null != token_result.token && '' != token_result.token) {
						sessionStorage.removeItem('access_token');
			        	sessionStorage.setItem( "access_token", token_result.token );
					}
				}
				successData =  copyReferenceNone(data);
			},
			error 	: function(req, stat, error) {
				errSw = true;
				
				alert(url + "\n" +requestData.requestType + " 에러 \n" + "관리자에게 문의 바랍니다.");

				$(".loding-div").remove();
				$('#btnSearch').prop('disabled', false);
				$('#btnQry').prop('disabled', false);
				$('#btnFind').prop('disabled', false);
				ingSw = false;
				qrySw = false;
				reqSw = false;
				ing_sw = false;
				
				return;
				jsonValue 		= $.parseJSON(req.responseText);	// response
				errorMessage 	= jsonValue.message;				// response error mesage
				var agent 			= navigator.userAgent.toLowerCase();// ie 구분
				
				var jsonArr = req.responseJSON.message.split("\r\n\tat");
				if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
					// java 에서 강제적으로 exception 발생시
					alert("오류발생 : " + replaceAllString(req.responseJSON.message.split("\r\n\tat")[0],"java.lang.Exception: ",""));
				} else {
					alert(url + "\n" +requestData.requestType + " 에러 \n" + "관리자에게 문의 바랍니다.");
				}
				
				// ie 브라우저일 경우 console에 나눠서 찍어주기
				if (agent.indexOf("msie") != -1 || agent.indexOf("trident") != -1) {    
					var lineCnt = 0;
					var lineError = '';
					errorMessage.split("\n").forEach(function(line) { 
						lineCnt++;
						lineError += line;
						if(lineCnt > 7) {
							console.log(lineError);
							lineCnt = 0;
							lineError = '';
						}
					});
					console.log(lineError);
				}
				// 크롬일경우 한번에 다 찍기
				if (agent.indexOf("chrome") != -1) {
					console.log(errorMessage);
				}
			}
		});
		if(!errSw) {
			if(successData != null) return successData;
		}else {
			return 'ERROR' + errorMessage;
		}
	} finally{
		successData = null;
		token_result = null;
		obj = null;
		requestJson = null;
		token = null;
	}
};

function $F(caller) { 
    var f = caller;
    if(caller) f = f.caller;
    var pat = /^function\s+([a-zA-Z0-9_]+)\s*\(/i;

    pat.exec(f);  //메서드가 일치하는 부분을 찾으면 배열변수를 반환하고, 검색 결과를 반영하도록 RegExp 개체가 업데이트된다.

    var func = new Object(); 
    func.name = RegExp.$1; 
    
    return func; 
}

/* 취약점수정 (2025) start*/
function tokenCheck(token, url, requestData) {
	//console.log('+++++++++++++++++++[protocol]',window.location.protocol);
	//console.log('+++++++++++++++++++[host]',window.location.host);
	//console.log('+++++++++++++++++++[hostname]',window.location.hostname);
	//console.log('+++++++++++++++++++[port]',window.location.port);
	//console.log('+++++++++++++++++++[url]'+window.location.protocol+'//'+window.location.hostname);
	//http://localhost:7779
	
	var paramData = JSON.stringify(requestData);
	var obj = JSON.parse(paramData);
	
	var requestJson;
	var successData = null;
	try {
		if ( obj.requestType == 'GETSESSIONUSERDATA' || url == '/webPage/dao/DaoServlet' ) {
			return JSON.parse(JSON.stringify({message:"OK"}));
		} else if ( token == null || token == '' ) {
			console.log('[2TOKEN CHECK FAIL]',obj.requestType);
			return JSON.parse(JSON.stringify({message:"TIMEOUT"}));
		} else {
			//userId = 'MASTER';
	    	userId = sessionStorage.getItem('id');
			if (null == userId || userId == '') {
				sessionStorage.removeItem('id');
				//sessionStorage.clear();
				console.log('[3TOKEN CHECK FAIL]',obj.requestType);
				return JSON.parse(JSON.stringify({message:"TIMEOUT"}));
			}
			requestJson = JSON.stringify( {user:userId, requestType:'CHECK_TOKEN', locale: 'kr'} );
			
			var ajax = $.ajax({
				type 	: 'POST',
				url 	: homePath+'/webPage/tokenCheckServlet',
				data 	: requestJson,
				dataType: 'json',
				contentType: "application/json; charset=UTF-8",
				headers: {"Authorization": 'Bearer '+token },
				/*headers: {"Authorization": sessionStorage.getItem('token')},*/
				/* headers: { Authorization: $`Bearer ${sessionStorage.getItem("token")}` }, */
				async 	: false,
				success : function(data) {
					var obj2 = JSON.parse(requestJson);
					if (null != data.token && '' != data.token) {
						sessionStorage.removeItem('access_token');
			        	sessionStorage.setItem("access_token", data.token);
						successData = copyReferenceNone(data);
					} else {
	//					logoutCallback('TIMEOUT');
						tokenCheck('', '', {requestType: 'RECREATETOKEN'});
					}
				},
				error 	: function(req, stat, error) {
					console.log(req, stat, error);
				}
			});
			
			ajax = null;
			
			if(successData != null) return successData;
			else return JSON.parse(JSON.stringify({message:"FAIL"}));
		}		
		
	} finally {
		successData = null;
		requestJson = null;
		obj = null;
		paramData = null;
	}
}

/* 취약점수정 (2025) end*/

function tokenCheck_old(token, url, requestData) {
	//console.log('+++++++++++++++++++[protocol]',window.location.protocol);
	//console.log('+++++++++++++++++++[host]',window.location.host);
	//console.log('+++++++++++++++++++[hostname]',window.location.hostname);
	//console.log('+++++++++++++++++++[port]',window.location.port);
	//console.log('+++++++++++++++++++[url]'+window.location.protocol+'//'+window.location.hostname);
	//http://localhost:7779
	
	var paramData = JSON.stringify(requestData);
	var obj = JSON.parse(paramData);
	
	var requestJson;
	var successData = null;
	try {
		if ( obj.requestType == 'GETSESSIONUSERDATA' || url == '/webPage/dao/DaoServlet' ) {
			return JSON.parse(JSON.stringify({message:"OK"}));
		}

		if ( url == '/webPage/login/Login' && (obj.requestType == 'ISVALIDLOGIN') ) {
			//console.log('userId1', obj.userId);
			if (null != obj.userId && '' != obj.userId) { //로그인하려고 하는 입력한ID
				requestJson = JSON.stringify( {user:obj.userId, requestType:'CREATE_TOKEN', locale: 'kr'} );
			} else {
				sessionStorage.removeItem('id');
				//sessionStorage.clear();
				console.log('[1TOKEN CHECK FAIL]',obj.requestType);
				return JSON.parse(JSON.stringify({message:"FAIL"}));
			}
		} else {
			if ( token == null || token == '' ) {
		    	userId = sessionStorage.getItem('id');
				if (null == userId || userId == '') {
					sessionStorage.removeItem('id');
					//sessionStorage.clear();
					console.log('[2TOKEN CHECK FAIL]',obj.requestType);
					return JSON.parse(JSON.stringify({message:"TIMEOUT"}));
				} else {
					requestJson = JSON.stringify( {user:userId, requestType:'CREATE_TOKEN', locale: 'kr'} );
				}
			} else {
				//ID 가져오는 부분 확인필요........... 파라미터로 안넘기고 로그인한 ID 가지고 있기
				//userId = 'MASTER';
		    	userId = sessionStorage.getItem('id');
				if (null == userId || userId == '') {
					sessionStorage.removeItem('id');
					//sessionStorage.clear();
					console.log('[3TOKEN CHECK FAIL]',obj.requestType);
					return JSON.parse(JSON.stringify({message:"TIMEOUT"}));
				}
				requestJson = JSON.stringify( {user:userId, requestType:'CHECK_TOKEN', locale: 'kr'} );
			}
		}
		
		var ajax = $.ajax({
			type 	: 'POST',
			url 	: homePath+'/webPage/tokenCheckServlet',
			data 	: requestJson,
			dataType: 'json',
			contentType: "application/json; charset=UTF-8",
			headers: {"Authorization": 'Bearer '+token },
			/*headers: {"Authorization": sessionStorage.getItem('token')},*/
			/* headers: { Authorization: $`Bearer ${sessionStorage.getItem("token")}` }, */
			async 	: false,
			success : function(data) {
				var obj2 = JSON.parse(requestJson);
				if (null != data.token && '' != data.token) {
					sessionStorage.removeItem('access_token');
		        	sessionStorage.setItem("access_token", data.token);
					successData = copyReferenceNone(data);
				} else {
//					logoutCallback('TIMEOUT');
					tokenCheck('', '', {requestType: 'RECREATETOKEN'});
				}
			},
			error 	: function(req, stat, error) {
				console.log(req, stat, error);
			}
		});
		
		ajax = null;
		
		if(successData != null) return successData;
		else return JSON.parse(JSON.stringify({message:"FAIL"}));
	} finally {
		successData = null;
		requestJson = null;
		obj = null;
		paramData = null;
	}
}


function ajaxAsync(url, requestData, dataType,successFunc,callbackFunc) {
	var token = sessionStorage.getItem('access_token');
	
	var requestJson = JSON.stringify(requestData);
	var obj = JSON.parse(requestJson);
	
	try {
		if (obj.requestType != 'CODE_INFO' && obj.requestType != 'addAdminAccess'
			&& obj.requestType != 'getJobInfo' && obj.requestType != 'getUserRgtCd') {
			var token_result = tokenCheck(token, url, requestData);
			
			if (token_result.message != "OK") {
				logoutCallback(token_result.message);
				return;
			}
		}
		token = sessionStorage.getItem('access_token');
	
		changeCursor(true);
		var calleeFuncName = $F(arguments.callee);
		/*
		var ajax = $.ajax({
			type 	: 'POST',
			url 	: homePath+url,
			data 	: requestJson,
			dataType: dataType,
			contentType: "application/json; charset=UTF-8",
			headers: {"Authorization": 'Bearer '+token },
			async 	: true,
			complete : function() {
				changeCursor(false);
			}
		}).then(successFunc, function(err) {
			console.log('============================에러가 발생한 호출 함수명 [' +calleeFuncName.name + ']==================');
			console.log('============================Ajax 통신중 error 발생 Error message START============================');
			console.log(err);
			console.log('============================Error message END============================');
		}).then(callbackFunc);
		*/
		
		var ajax = $.ajax({
			type 	: 'POST',
			url 	: homePath+url,
			data 	: requestJson,
			dataType: dataType,
			contentType: "application/json; charset=UTF-8",
			headers: {"Authorization": 'Bearer '+token },
			async 	: true,
			complete : function() {
				changeCursor(false);
			}
		}).fail(function($xhr,status ,error) {

			$(".loding-div").remove();
			$('#btnSearch').prop('disabled', false);
			$('#btnQry').prop('disabled', false);
			$('#btnFind').prop('disabled', false);
			ingSw = false;
			qrySw = false;
			reqSw = false;
			ing_sw = false;
			
			if(closeCk && closeCk != undefined){
				return;
			}
			alert(url + "\n" +requestData.requestType + " 에러 \n" + "관리자에게 문의 바랍니다.");
			return;
			var jsonValue 		= $.parseJSON($xhr.responseText);	// response
			var errorMessage 	= jsonValue.message;				// response error mesage
			var agent 			= navigator.userAgent.toLowerCase();// ie 구분
			
			var jsonArr = $xhr.responseJSON.message.split("\r\n\tat");
			if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
				// java 에서 강제적으로 exception 발생시
				alert("오류발생 : " + replaceAllString($xhr.responseJSON.message.split("\r\n\tat")[0],"java.lang.Exception: ",""));
			} else {
				alert(url + "\n" +requestData.requestType + " 에러 \n" + "관리자에게 문의 바랍니다.");
			}
			
			// ie 브라우저일 경우 console에 나눠서 찍어주기
			if (agent.indexOf("msie") != -1 || agent.indexOf("trident") != -1) {    
				var lineCnt = 0;
				var lineError = '';
				errorMessage.split("\n").forEach(function(line) { 
					lineCnt++;
					lineError += line;
					if(lineCnt > 7) {
						console.log(lineError);
						lineCnt = 0;
						lineError = '';
					}
				});
				console.log(lineError);
			}
			// 크롬일경우 한번에 다 찍기
			if (agent.indexOf("chrome") != -1) {
				console.log(errorMessage);
			}

		}).done(function(result){successFunc(result)}).then(callbackFunc);
		
		ajax = null;
		calleeFuncName = null;
	} finally {
		obj = null;
		requestJson = null;
		token = null;
	}
}

function changeCursor(cursorSw) {
	if(cursorSw) {
		$('html').css({'cursor':'wait'});
		$('body').css({'cursor':'wait'});
	} else {
		$('html').css({'cursor':'auto'});
		$('body').css({'cursor':'auto'});
	}
}

function Request(){
	var requestParam ="";
    this.getParameter = function(param){
    	var url = unescape(location.href); //현재 주소를 decoding
        var paramArr = (url.substring(url.indexOf("?")+1,url.length)).split("&"); //파라미터만 자르고, 다시 &그분자를 잘라서 배열에 넣는다. 

        for(var i = 0 ; i < paramArr.length ; i++){
            var temp = paramArr[i].split("="); //파라미터 변수명을 담음

            if(temp[0].toUpperCase() == param.toUpperCase()){
            	requestParam = paramArr[i].split("=")[1]; // 변수명과 일치할 경우 데이터 삽입
                break;
            }

        }
        return requestParam;
    };
}



function CodeInfoOrdercd(MACODE, SelMsg, closeYn, ordercd, NOTMICODE) {
	this.MACODE 	= MACODE;
	this.SelMsg 	= SelMsg;
	this.closeYn 	= closeYn;
	this.ordercd 	= ordercd;
	this.NOTMICODE 	= NOTMICODE;
};

function CodeInfo(MACODE, SelMsg, closeYn) {
	this.MACODE 	= MACODE;
	this.SelMsg 	= SelMsg;
	this.closeYn 	= closeYn;
};


/* 코드정보 가져오기 공통 함수 정의.
 * 한번에 여러개 혹은 하나의 코드정보를 가져옵니다.
 * 
 * EX)
 * var codeInfos = getCodeInfoCommon( [new CodeInfo('SRTYPE','ALL','N'),		>> CodeInfo 객체를    배열 형태로 파라미터 전달
										new CodeInfo('QRYGBN','ALL','N')] );
	
	cboCatTypeData 	= codeInfos.SRTYPE;	>> 리턴받은 DATA에서 MACODE값으로 해당 값을 불러서 사용하면 됩니다.
	cboQryGbnData 	= codeInfos.QRYGBN;
	SBUxMethod.refresh('cboCatType');
	SBUxMethod.refresh('cboQryGbn');
 */
function getCodeInfoCommon(codeInfoArr) {
	codeInfoArr.forEach(function(item, index) {
		if(item.ordercd == undefined) {
			item.ordercd = '2';
		}
		
	});
	var returnCodeInfo = {};
	var ajaxReturnData = null;
	var codeInfo = {};
	var divisionMacode = '';
	codeInfo = {
		codeInfoData: 	codeInfoArr,
		requestType: 	'CODE_INFO'
	};
	ajaxReturnData = ajaxCallWithJson('/webPage/common/CommonCodeInfo', codeInfo, 'json');
	if(ajaxReturnData !== 'ERR') {
		return ajaxReturnData;
	} else {
		return null;
	}
}

//내 직무권한 가져오기(cmm0043)
function getMyRgtCd(userId) {
	var ajaxReturnData = null;
	var data = new Object();
	var rgtList = '';
	try {
		data = {
				 UserID	: userId,
				  RGTCD : '',
				closeYn : 'N',  
			requestType	: 'getUserRGTCD'
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/common/UserInfoServlet', data, 'json');
		//console.log('getUserRGTCD='+ajaxReturnData);
		if(ajaxReturnData != null && ajaxReturnData != '' && ajaxReturnData != undefined) {
			if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>=0) {
				dialog.alert(ajaxReturnData);
				return null;
			} 
			return ajaxReturnData;
		} else {
			return null;
		}
	} finally{
		rgtList = null;
		data = null;
		ajaxReturnData = null;
	}
}
/*
 * dataList: 코드리스트
 * selMsg: ''-****제외하고모두, 'SEL'-선택하세요, 'ALL'-전체
 * orderCol: 정렬기준 컬럼(default: cm_micode)
 * orderCd:  정렬구분 -> ASC(오름차순:desult), DESC(내림차순)
 * closeYN: Y-미사용코드만, N-사용중이코드만, ''-모두
 */
function fixCodeList(dataList, selMsg, orderCol, orderCd, closeYN) {
	var codeList = [];
	var codeObj = null;
	try{
		for (var i=0; i<dataList.length; i++) {
			if (closeYN == 'Y' && dataList[i].cm_useyn != 'N') continue;
			if (closeYN == 'N' && dataList[i].cm_useyn != 'Y') continue;
			
			codeObj = new Object();
			codeObj = dataList[i];
			if (selMsg != 'ALL') {
				if (selMsg == 'SEL') {//선택하세요
					if (codeObj.cm_micode == '****') {
						codeObj.cm_codename = '선택하세요';
						codeObj.text = '선택하세요';
					}
				} else {//cm_micode='****' 만 제외하고 모두 
					if (codeObj.cm_micode == '****') continue;
				}
			}
			codeList.push(codeObj);
			codeObj = null;
		}
		codeObj = null;
		
		if (codeList != null && codeList.length > 0) {
			if (null != orderCol && '' != orderCol) {
				codeList.sort(compareValues(orderCol, orderCd));
			} else {
				codeList.sort(compareValues('cm_micode', 'ASC'));
			}
		}
		
		//20200417 neo. micode=****이게 중간에 들어가 있는 경우 제일 앞으로 이동
		var idx = 0;
		var found = codeList.some(function(item, index){idx = index; return item.cm_micode == '****'});
		
		//const idx = codeList.findIndex(function(item) {return item.cm_micode == '****'} )//배열에서 **** 위치 찾기
		//console.log('idx:',idx,',selMsg:',selMsg);
		if ( idx > 0 && found) {//**** 의 위치가 첫번째가 아닐때, 제일 앞으로 이동
			codeObj = new Object();
			codeObj = codeList[idx];
			codeList.splice(idx,1);//idx 위치에 있는 항목 제거
			codeList.unshift(codeObj);//0번째에 항목 추가
			codeObj = null;
		}
		
		return codeList;
		
	} finally{
		codeObj = null;
		codeList = null;
	}
}

/*
 * 데이터정렬
 * key: 정렬할 컬럼명
 * order: 정렬방식
 */
function compareValues(key, order) {
	return function innerSort(a, b) {
		if (!a.hasOwnProperty(key) || !b.hasOwnProperty(key)) {
			// property doesn't exist on either object
			return 0;
	    }

	    const varA = (typeof a[key] === 'string') ? a[key].toLowerCase() : a[key];
	    const varB = (typeof b[key] === 'string') ? b[key].toLowerCase() : b[key];
	    const varC = a['cm_micode'];
	    
	    let comparison = 0;
	    if (varA > varB) {
	    	comparison = 1;
	    } else if (varA < varB) {
	    	comparison = -1;
	    }
	    
	    //전체,선택하세요 이면 무조건 맨처음으로 
	    if (varC == '****') comparison = -5;
	    return ((order === 'DESC' && varC != '****') ? (comparison * -1) : comparison);
	};
}

/* 현재 날짜를 기준으로 
 *  dateSeparator = ['MON' , 'DATE', 'LASTDATE' , 'FIRSTDATE'] 중 하나 선택사용 
 *  increaseDecreaseNumber = 양수 혹은 음수
 *  
 *  EX)
 *  getDate('DATE',-1));  	>> 현재 날짜에서 하루전날짜
 *  getDate('DATE',0));		>> 현재 날짜
 *  getDate('MON',-1));		>> 한달 전
 *  getDate('MON',1)); 		>> 한달 후
 *  getDate('LASTDATE',0));	>> 현재달의 마지막 날짜
 *  getDate('FIRSTDATE',0));>> 현재달의 첫 날짜		 		
 * 
 */
function getDate(dateSeparator, increaseDecreaseNumber) {
	
	var calcuDate = new Date( new Date().getFullYear() 
						, new Date().getMonth().length === 1 ?  '0'+ new Date().getMonth() : new Date().getMonth()
						, new Date().getDate().length === 1 ? '0'+new Date().getDate() : new Date().getDate() );
	
	if(dateSeparator === 'MON'){
		if(calcuDate.getMonth() === '0' && increaseDecreaseNumber === -1){
			calcuDate.setFullYear(calcuDate.getFullYear() - 1);
			calcuDate.setMonth(11);
		}else if(calcuDate.getMonth() === '11' && increaseDecreaseNumber === 1){
			calcuDate.setFullYear(calcuDate.getFullYear() + 1);
            calcuDate.setMonth(0);
		}else {
        	calcuDate.setMonth(calcuDate.getMonth() + increaseDecreaseNumber);
        }
	}
	if(dateSeparator === 'DATE') calcuDate.setDate(calcuDate.getDate() + increaseDecreaseNumber);
	if(dateSeparator === 'LASTDATE') calcuDate.setDate(0);
	if(dateSeparator === 'FIRSTDATE') calcuDate.setDate(1);
	
	return changeDateToYYYYMMDD(calcuDate);
}
/* 지정한 날짜를 기준으로 
 *  baseDay       = yyyy/mm/dd
 *  dateSeparator = ['MON' , 'DATE'] 중 하나 선택사용 
 *  increaseDecreaseNumber = 양수 혹은 음수
 *  
 *  EX)
 *  getDate('DATE',-1));  	>> 현재 날짜에서 하루전날짜
 *  getDate('DATE',0));		>> 현재 날짜
 *  getDate('MON',-1));		>> 한달 전
 *  getDate('MON',1)); 		>> 한달 후
 *  getDate('LASTDATE',0));	>> 현재달의 마지막 날짜
 *  getDate('FIRSTDATE',0));>> 현재달의 첫 날짜		 		
 * 
 */
function getDate_baseDay(baseDay, dateSeparator, increaseDecreaseNumber) {
	
	var base1 = baseDay.split('/');
	var calcuDate = new Date(base1[0], base1[1] - 1, base1[2]);
		
	if(dateSeparator === 'MON'){
		if(calcuDate.getMonth() === '0' && increaseDecreaseNumber === -1){
			calcuDate.setFullYear(calcuDate.getFullYear() - 1);
			calcuDate.setMonth(11);
		}else if(calcuDate.getMonth() === '11' && increaseDecreaseNumber === 1){
			calcuDate.setFullYear(calcuDate.getFullYear() + 1);
            calcuDate.setMonth(0);
		}else {
        	calcuDate.setMonth(calcuDate.getMonth() + increaseDecreaseNumber);
        }
	}
	if(dateSeparator === 'DATE') calcuDate.setDate(calcuDate.getDate() + increaseDecreaseNumber);
	if(dateSeparator === 'LASTDATE') calcuDate.setDate(0);
	if(dateSeparator === 'FIRSTDATE') calcuDate.setDate(1);
	
	return changeDateToYYYYMMDD(calcuDate);
}
function dayDiff(startDate, endDate) {
	
	var v1 = startDate.split('/');
	var v2 = endDate.split('/');
	
	var a1 = new Date(v1[0], v1[1] - 1, v1[2]).getTime();
	var a2 = new Date(v2[0], v2[1] - 1, v2[2]).getTime();
	
	return (a2 - a1) / (1000 * 60 * 60 * 24);
}
function getTime() {
	var d = new Date();
	var hour = '';
	var min = '';
	hour = d.getHours() < 10 ? '0' + d.getHours() : d.getHours();
	min  = d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes();
	var currentTime = hour + '' + min;
	return currentTime;
}

function getTime2() {
	var d = new Date();
	var hour = '';
	var min = '';
	var sec = '';
	hour = d.getHours() < 10 ? '0' + d.getHours() : d.getHours();
	min  = d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes();
	sec  = d.getSeconds().length < 2 ? '0' + d.getSeconds() : d.getSeconds();
	var currentTime = hour + '' + min + '' + sec;
	return currentTime;
}

/*
 * Javascript Date형식을 YYYYMMDD의 String으로 형 변환
 */
function changeDateToYYYYMMDD(date){
	
	var year 	= date.getFullYear();
	var month 	= (date.getMonth() + 1) <  10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
	var date 	= date.getDate() <  10 ? '0' + date.getDate() : date.getDate();
	return year+''+month+''+date;
}

function defaultPickerInfoLarge(dataAx5picker, direction) {
	if (direction == '' || direction == null) direction = "bottom";
	return {
		target: $('[data-ax5picker="'+dataAx5picker+'"]'),
		direction: direction,
		content: {
			width: 220,
			margin: 10,
			type: 'date',
			config: {
				control: {
					left: '<i class="fa fa-chevron-left"></i>',
					yearTmpl: '%s',
					monthTmpl: '%s',
					right: '<i class="fa fa-chevron-right"></i>'
				},
				dateFormat: 'yyyy/MM/dd',
				lang: {
					yearTmpl: "%s",
					months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
					dayTmpl: "%s"
				}
			},
			formatter: {
				pattern: 'date'
			}
		},
		onStateChanged: function () {
            if (this.state == "open") {
                var selectedValue = this.self.getContentValue(this.item["$target"]);
                if (!selectedValue) {
                    this.item.pickerCalendar[0].ax5uiInstance.setSelection([ax5.util.date(new Date(), {'add': {d: 0}})]);
                }
            }
        },
		btns: {
			today: {
				label: "Today", onClick: function () {
					var today = new Date();
					this.self
					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
					.close();
				}
			},
			thisMonth: {
				label: "This Month", onClick: function () {
					var today = new Date();
					this.self
					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
							+ '/'
							+ ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
							.close();
				}
			},
			ok: {label: "Close", theme: "default"}
		}
		
	};
}


// StringReplaceAll
// ex) replaceAllString($("#dateStD").val(), "/", "");
function replaceAllString(source, find, replacement){
	return source.split( find ).join( replacement );
}
/*
function getSelectedIndex(id) {
	if ($('[data-ax5select="'+id+'"]').ax5select("getValue")[0] == undefined) return -1;
	return $('[data-ax5select="'+id+'"]').ax5select("getValue")[0]['@index'];
}

function getSelectedVal(id) {
	if ($('[data-ax5select="'+id+'"]').ax5select("getValue")[0] == undefined) return null;
	return $('[data-ax5select="'+id+'"]').ax5select("getValue")[0];
}
*/

function getSelectedIndex(id) {
//	if ($('[data-ax5select="'+id+'"]').ax5select("getValue")[0] == undefined) return -1;
//	return $('[data-ax5select="'+id+'"]').ax5select("getValue")[0]['@index'];

	return $('[data-ax5select="'+id+'"]').ax5select("getIndex");
}

function getSelectedVal(id) {
//	if ($('[data-ax5select="'+id+'"]').ax5select("getValue")[0] == undefined) return null;
//	return $('[data-ax5select="'+id+'"]').ax5select("getValue")[0];

	var value = $('[data-ax5select="'+id+'"]').ax5select("getValue");
	if (value == undefined) return null;
	else return value;
}

// window load 되기 전까지 마우스 커서 wait 로 나타나게 처리
$('html').css({'cursor':'wait'});
$('body').css({'cursor':'wait'});
$(window).on('load',function(){
	$('html').css({'cursor':'auto'});
	$('body').css({'cursor':'auto'});
	
});


/*
 * promise 입니다.
 * 데이터의 처리 순서를 비동기로 처리해야 할시 사용하세요.
 */
var _promise = function(ms,action){
	return new Promise(function(resolve,reject){
		setTimeout(function(){
			resolve(action);
		},ms)
	});
}

function beForAndAfterDataLoading(beForAndAfter,msg){
	if(beForAndAfter === 'BEFORE'){
		$('html').css({'cursor':'wait'});
		$('body').css({'cursor':'wait'});
	}
	
	if(beForAndAfter === 'AFTER'){
		$('html').css({'cursor':'auto'});
		$('body').css({'cursor':'auto'});
	}
	
}

function defaultPickerInfo(dataAx5picker, direction) {
	if (direction == '' || direction == null) direction = "bottom";
	return {
		target: $('[data-ax5picker="'+dataAx5picker+'"]'),
		direction: direction,
		content: {
			width: 220,
			margin: 10,
			type: 'date',
			config: {
				control: {
					left: '<i class="fa fa-chevron-left"></i>',
					yearTmpl: '%s',
					monthTmpl: '%s',
					right: '<i class="fa fa-chevron-right"></i>'
				},
				dateFormat: 'yyyy/MM/dd',
				lang: {
					yearTmpl: "%s년",
					months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
					dayTmpl: "%s"
				},dimensions: {
					height: 140,
					width : 75,
					colHeadHeight: 11,
					controlHeight: 25,
				}
			},
			formatter: {
				pattern: 'date'
			}
		},
		onStateChanged: function () {
            if (this.state == "open") {
                var selectedValue = this.self.getContentValue(this.item["$target"]);
                if (!selectedValue) {
                    this.item.pickerCalendar[0].ax5uiInstance.setSelection([ax5.util.date(new Date(), {'add': {d: 0}})]);
                }
            }
        },
		btns: {
			today: {
				label: "Today", onClick: function () {
					var today = new Date();
					this.self
					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
					.close();
				}
			},
			thisMonth: {
				label: "This Month", onClick: function () {
					var today = new Date();
					this.self
					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
							+ '/'
							+ ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
							.close();
				}
			},
			ok: {label: "Close", theme: "default"}
		}
		
	};
}

function defaultPickerInfoMonth(dataAx5picker, direction) {
	if (direction == '' || direction == null) direction = "bottom";
	return {
		target: $('[data-ax5picker="'+dataAx5picker+'"]'),
		direction: direction,
		content: {
			width: 220,
			margin: 10,
			type: 'date',
			config: {
				mode:"month",
				selectMode: "month",
				control: {
					left: '<i class="fa fa-chevron-left"></i>',
					yearTmpl: '%s',
					monthTmpl: '%s',
					right: '<i class="fa fa-chevron-right"></i>'
				},
				dateFormat: 'yyyy/MM',
				lang: {
					yearTmpl: "%s년",
					months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
				},dimensions: {
					height: 140,
					width : 75,
					colHeadHeight: 11,
					controlHeight: 25,
				}
			},
			formatter: {
				pattern: 'date(month)'
			}
		},
		onStateChanged: function () {
            if (this.state == "open") {
                var selectedValue = this.self.getContentValue(this.item["$target"]);
                if (!selectedValue) {
                    this.item.pickerCalendar[0].ax5uiInstance.setSelection([ax5.util.date(new Date(), {'add': {d: 0}})]);
                }
            }
        },
		btns: {
			thisMonth: {
				label: "This Month", onClick: function () {
					var today = new Date();
					this.self
					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM"}))
					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
							+ '/'
							+ ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
							.close();
				}
			},
			ok: {label: "Close", theme: "default"}
		}
		
	};
}
/**
 *	cbo 데이터 세팅시 자바에서 받아온 모든 데이터 편하게 넣어주기
 *
 *  !!!!기존 사용법
 *  cboBldCdData = data;
 *  cboOptions = [];
	$.each(cboBldCdData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	
	$('[data-ax5select="cboBldCd"]').ax5select({
        options: cboOptions
    });
    
    
    !!!!injectCboDataToArr function 사용시
    cboSysCdData = data;
	$('[data-ax5select="cboSysCd"]').ax5select({
        options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
 *   
 */
function injectCboDataToArr(data, value, text) {
	var cboOptionObj	= null;
	var cboOptions 	= [];
	var keyArr		= [];
	data.forEach(function(item, index) {
		cboOptionObj = new Object();
		cboOptionObj = item;
		cboOptionObj.value = item[value];
		cboOptionObj.text = item[text];
		cboOptions.push(cboOptionObj);
		cboOptionObj = null;
	});
	return cboOptions; 
}

String.prototype.trim = function() { 
	return this.replace(/^\s+|\s+$/g,""); 
}

function winOpen(form, winName, cURL, nHeight, nWidth, ratioSw, wRatio, hRatio, resizableYn) {
	changeCursor(true);
	var cFeatures 	= '';
	var tmpWindow 	= '';
	var nTop	 	= '';
	var nLeft 		= '';
	var fMethod     = 'post';
	if(winName.indexOf("undefined") > -1){
		winName = "newPop";
	}
	if(ratioSw !== undefined && ratioSw && hRatio !== undefined && wRatio !== undefined) {
		nHeight = window.outerHeight * hRatio;
		nWidth = window.outerWidth * wRatio;
	}
	
    //cURL = 'http://'+location.host + cURL;
    nTop  = parseInt((window.screen.availHeight/2) - (nHeight/2));
	nLeft = parseInt((window.screen.availWidth/2) - (nWidth/2));
	
    if (cURL.substr(0,7) != 'http://' && cURL.substr(0,8) != 'https://') {
    	var strRgtList = null;
		try {
			strRgtList = window.top.rgtList;
		} catch (err) {
			strRgtList = '';
		}
		if(form.querySelector('[name="rgtList"]') == null) {
			var rgtListInput  = document.createElement('input');
		    rgtListInput.type = 'hidden';
		    rgtListInput.name = 'rgtList';
		    rgtListInput.value= strRgtList;
		    form.appendChild(rgtListInput);
		}else if (form.rgtList.value == null || form.rgtList.value == '' || form.rgtList.value == undefined) {
			form.rgtList.value = strRgtList;
		} 
		//console.log('common.js='+form.rgtList.value);
		
		nHeight = window.screen.availHeight - 200;
	    nWidth  = window.screen.availWidth - 300;
	    if (cURL == '/webPage/winpop/PopApprovalInfo.jsp') {
			nHeight = 550;
			nWidth = 1000;
		} else if (cURL == '/webPage/winpop/PopProgramInfo.jsp') {
	    	nHeight = 550;
		    nWidth  = 1300;
		} else if (cURL == '/webPage/winpop/PopRequestDetail.jsp'){
	    	nHeight = 850;
		    nWidth  = 1500;
		} else if (cURL == '/webPage/winpop/PopRequestDetailCkOut.jsp'){
	    	nHeight = 700;
		    nWidth  = 1200;
		} else if (cURL== '/webPage/winpop/PopPrcResultLog.jsp'){
	    	nHeight = 800;
		    nWidth  = 1200;
		} else if (cURL== '/webPage/winpop/PopServerLog.jsp'){
	    	nHeight = 800;
		    nWidth  = 1200;
		} else if (cURL == '/webPage/winpop/PopDevRepository.jsp'){
	    	nHeight = 880;
		    nWidth  = 1200;
		} else if (cURL.indexOf('SourceView')>0 || cURL.indexOf('SourceDiff')>0) {
	    	nHeight = 900;
		    nWidth  = 1300;
		} else if (cURL== '/webPage/winpop/PopNotice.jsp'){
			fMethod = "post";
			nHeight = 370;
		    nWidth  = 615;
		} else if (cURL== '/webPage/winpop/PopScript.jsp'){
	    	nHeight = 500;
		    nWidth  = 1200;
		} else if (cURL== '/webPage/winpop/PopSRInfo.jsp'){
			nHeight = 800;
		    nWidth  = 1200;
		} else if (cURL== '/webPage/winpop/PopOrderListDetail.jsp'){
			nHeight = 630;
		    nWidth  = 1200;
		} else if (cURL== '/webPage/winpop/PopDevRequestDetail.jsp'){
			nHeight = 630;
		    nWidth  = 1300;
		} else if (cURL== '/webPage/winpop/PopOutSourcingDetail.jsp'){
			nHeight = 630;
			nWidth  = 1300;
		} else if (cURL== '/webPage/winpop/PopProgDetailCkOut.jsp'){
			nHeight = 630;
			nWidth  = 1200;
		} else if (cURL== '/webPage/winpop/PopProgDetail.jsp'){
			nHeight = 630;
			nWidth  = 1500;
		} else if (cURL == '/webPage/winpop/PopRequestSourceView.jsp'){
	    	nHeight = 900;
		    nWidth  = 1046;
		} else if (cURL== '/webPage/apply/ApplyRequest.jsp'){
			nHeight = screen.height - 20;
		    nWidth  = screen.width - 10;
		} else if (cURL== '/webPage/dev/CheckOut.jsp'){
			nHeight = screen.height - 20;
		    nWidth  = screen.width - 10;
		}  
		cURL = 'http://'+location.host + cURL;
	} else if (cURL.indexOf('?')>0) fMethod = "get";
    
    form.action	= cURL; 		//이동할 페이지
    form.target	= winName;    	//폼의 타겟 지정(위의 새창을 지정함)
    form.method	= fMethod; 		//POST방식
    
    if(resizableYn !== undefined && resizableYn !== null && resizableYn !== '') {
    	cFeatures = "top=" + nTop + ",left=" + nLeft + ",height=" + nHeight + ",width=" + nWidth + ",help=no,menubar=no,status=no,resizable="+resizableYn+",scroll=no";
    } else {
    	cFeatures = "top=" + nTop + ",left=" + nLeft + ",height=" + nHeight + ",width=" + nWidth + ",help=no,menubar=no,status=no,resizable=yes,scroll=no";
    }
    if(fMethod == "get"){
//    	if (infSw) {
//    		tmpWindow = window.open(cURL,winName,cFeatures);
//    	} else {
	        var getParam = "";
	        var elements = form.elements;
	        for(var i=0; i< elements.length ; i++){
	        	getParam += encodeURIComponent(elements[i].name, 'utf-8') + "=" + encodeURIComponent(elements[i].value, 'utf-8') + "&";
	        }
	    	tmpWindow = window.open(cURL+"?"+ getParam,winName,cFeatures);
//    	}
    	changeCursor(false);
    	return tmpWindow;
    } 

//	var popForm = form.cloneNode(true);
//	popForm.name = winName;
//	popForm.id = winName;
//	popForm.target = winName;
//	document.body.appendChild(popForm);

//	for(var i=0; i< popUpList.length; i++){
//		if ( popUpList[i].name == winName && !popUpList[i].pop.closed ) {
//			popUpList[i].pop.close();
//			popUpList.splice(i--,1);
//   		} else if(popUpList[i].pop == null || popUpList[i].pop.closed ){
//			popUpList.splice(i--,1);
//   		}
//	};
	
	tmpWindow = window.open("/loading.html",winName,cFeatures);
	
//	var newPop = {
//			pop : tmpWindow,
//			name : winName
//	}
//	popUpList.push(newPop);
	
	form.submit();
	
	if(tmpWindow != null && tmpWindow != undefined){
		tmpWindow.focus();
	}
	
	changeCursor(false);
	return tmpWindow;
}

function clone(obj){
	if(Array.isArray(obj)){
		return obj.slice();
	}
	
	var output = {};
	for(var i in obj){
		output[i] = obj[i];
	}
	return output
	
}
/**
 * IP 유효성 체크
 * ---------------------------------------------
 * 정상적인 IP인지 체크
 * 정상 예1) 222.107.254.169
 * 정상 예2) 222.7.54.69
 * 비정상 예1) 022.107.254.169
 * 비정상 예2) 222107.254.169
 * 비정상 예3) 222.107.254.1699
 * 비정상 예4) 222.107.254.
 * 비정상 예5) 222.107.254
 * 비정상 예6) 222.107.254.169:80
 * @param strIP
 * @returns
 */
function checkIP(strIP) {
    var expUrl = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;
    return expUrl.test(strIP);
}

/**
 *	캘린더 아이콘의 배경색을 바꿔줍니다. 
 */
function disableCal(sw, id) {
	var cal = $('#'+id).next();
	if(sw) {
		$(cal[0]).css('background-color', 'rgb(235, 235, 228)');
	} else {
		$(cal[0]).css('background-color', '#fff');
	}
}

/**
 * 캘린더 선택시 앞의 input 창에 클릭 이벤트를 주어 달력이 켜지도록
 * @returns
 */
$('.btn_calendar').bind('click', function(e) {
	e.preventDefault();
    e.stopPropagation();
	if($(this).css('background-color') === 'rgb(255, 255, 255)') {
		var inputs = $(this).siblings().prevAll('input');
		$(inputs.prevObject[0]).trigger('click');
		$(inputs.prevObject[0]).focus();
	}
});

/**
 * 타임피커 선택시 앞의 input 창에 포커스 이벤트를 주어 켜지도록
 * @returns
 */
$('.btn_time').bind('click', function(e) {
	e.preventDefault();
    e.stopPropagation();
	if($(this).css('background-color') === 'rgb(255, 255, 255)') {
		var inputs = $(this).siblings("input");
		$(inputs).focus();
	}
});
/**
 * byte 용량을 환산하여 반환
 * 용량의 크기에 따라 MB, KB, byte 단위로 환산함
 * ex) byte(102020, 1) > 102.0 KB
 * @param fileSize  byte 값
 * @param fixed     환산된 용량의 소수점 자릿수
 * @returns {String}
 */
function byte(fileSize, fixed) {
    var str = null;
    
    //GB 단위 이상일때 GB 단위로 환산
    if (fileSize >= 1024 * 1024 * 1024) {
        fileSize = fileSize / (1024 * 1024 * 1024);
        fileSize = (fixed === undefined) ? fileSize : fileSize.toFixed(fixed);
        str = commaNum(fileSize) + ' GB';
    }
    //MB 단위 이상일때 MB 단위로 환산
    else if (fileSize >= 1024 * 1024) {
        fileSize = fileSize / (1024 * 1024);
        fileSize = (fixed === undefined) ? fileSize : fileSize.toFixed(fixed);
        str = commaNum(fileSize) + ' MB';
    }
    //KB 단위 이상일때 KB 단위로 환산
    else if (fileSize >= 1024) {
        fileSize = fileSize / 1024;
        fileSize = (fixed === undefined) ? fileSize : fileSize.toFixed(fixed);
        str = commaNum(fileSize) + ' KB';
    }
    //KB 단위보다 작을때 byte 단위로 환산
    else {
        fileSize = (fixed === undefined) ? fileSize : fileSize.toFixed(fixed);
        str = commaNum(fileSize) + ' byte';
    }
    return str;
}

// 숫자 3자리마다 콤마 삽입
function commaNum(n) {
    var txtNumber = '' + n;
    var rxSplit = new RegExp('([0-9])([0-9][0-9][0-9][,.])');
    var arrNumber = txtNumber.split('.');
    arrNumber[0] += '.';
    do {
        arrNumber[0] = arrNumber[0].replace(rxSplit, '$1,$2');
    }
    while (rxSplit.test(arrNumber[0]));
    if(arrNumber.length > 1) {
        return arrNumber.join('');
    } else {
        return arrNumber[0].split('.')[0];
    }
}

/**
 * 긴 한줄의 string을 여러줄로 바꿔주는 함수
 * @param contents  줄바꿈 할 string
 * @param strNum    한줄에 몇글자를 넣을지 숫자
 * @returns {String}
 */
function lineBreak(contents, strNum) {
	
	if(contents.length <= strNum) {
		return contents;
	}
	
	var lineBreakedContents = '';
	var totalLine = parseInt(contents.length / strNum);
	
	
	if((strNum * totalLine) < contents.length) {
		totalLine = totalLine + 1;
	}
	
	for(var i=0; i<totalLine; i++) {
		if(i === (totalLine - 1) ) {
			lineBreakedContents += contents.substr(i*strNum, contents.length - (i*strNum) );
		} else {
			lineBreakedContents += contents.substr(i*strNum, strNum) + '\n';
		}
	}
	return lineBreakedContents;
}

function setCookie(name, value) {
	var cookeValue = escape(value);
	var nowDate = new Date();
	var cookieExpires = null;
	nowDate.setMonth(nowDate.getMonth() + 6);
	cookieExpires = nowDate.toGMTString();
	
	//크롬 8 버전부터 타 쿠키 미지원으로 옵션추가
    //document.cookie = 'cross-site-cookie=bar; SameSite=None; Secure';
	//https 프로토콜에서만 전송가능: SameSite=None; Secure
	//default: SameSite=Lax
	document.cookie = name + '=' + cookeValue + ";expires=" + cookieExpires + ';path=/; SameSite=Lax';
}

function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for(var i = 0; i <ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

/*
function getRegexp(type) {
	if( type === 'NUM')
		return /[^0-9]/g;
	if( type === 'KOR')
		return /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
	if( type === 'NUM')
		return /[^0-9]/g;
	if( type === 'NUM')
		return /[^0-9]/g;
	if( type === 'NUM')
		return /[^0-9]/g;
}

*/

// 셀렉트박스 열림 감지
$(document).ready(function(){
	$(document).on("click",function(e){
		//클릭한 값이 셀렉트박스 리스트라면 return
		if($(e.target).hasClass("ax-select-option-item-cell") || $(e.target).hasClass("ax-select-option-item")){
			return;
		}
		// 새창에서는 상위 프레임을 안거치도록 return
		if(window.top.document == this && window.top.location.pathname.indexOf("eCAMSBase.jsp") < 0){
			return;
		}
		if(window.top.clearSelect != null) {
			window.top.clearSelect(this);	
		}
	});
});


function htmlFilter(data){
	data = replaceAllString(
			replaceAllString(
					replaceAllString(
							replaceAllString(
									replaceAllString(data,"&","&amp;"),
							">","&gt;"),
					"\"","&quot;"),
			"\'","&#29;"),
		   "<","&lt;");
	return data;
}

function excelExport(gridData, xlsName) {
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	var ajaxReturnData = null;
	ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) {
		dialog.alert('Temp경로 추출 실패');
		return;
	}
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		return;
	}
	path = ajaxReturnData;

	data = null;

	if(path == null) {
		dialog.alert("Excel 저장 실패");
		return;
	} else {
		var col = gridData.config.columns;
		var headerDef = [];
		//var headerAlign = [];
		var header = new Object();
		for(var i = 0; i < col.length; i++) {
			if(col[i].columns != undefined || col[i].columns != null ){
				for(var j=0; j< col[i].columns.length; j++){
					headerDef.push(col[i].columns[j].key);
					//headerAlign.push(col[i].columns[j].align);
					var label = col[i].columns[j].label;

					if(label.indexOf("<span") > -1 || label.indexOf("<div") > -1|| label.indexOf("<font") > -1){
						label = label.substring(label.indexOf(">")+1, label.indexOf("</"));
					}

					header[col[i].columns[j].key] = replaceAllString(label, "<br/>", "\n");
				}
			} else if(col[i].hidden != true && col[i].excel != false){//  컬럼 option hidden = true, excel = false 시 에는 컬럼 추가 X
				headerDef.push(col[i].key);
				//headerAlign.push(col[i].align);
				var label = col[i].label;

				if(label.indexOf("<span") > -1 || label.indexOf("<div") > -1|| label.indexOf("<font") > -1){
					label = label.substring(label.indexOf(">")+1, label.indexOf("</"));
				}

				header[col[i].key] = replaceAllString(label, "<br/>", "\n");
			}
		}
		var excelData = [];
		$.each(gridData.getList(), function(i, val) {
			var excelRow = new Object();
			$.each(headerDef, function(j, val2) {
				if(val[val2] == undefined || val[val2] == null){
					val[val2] = "";
				}
				$.each(gridData.config.columns, function(x, val3){
					if (val3.key == val2){
						if(val3.formatter == undefined){
							return false;
						} else {
							val3.item = val;
							val3.value = val[val2];
							var value;
							if (xlsName.indexOf('SourceDiff')>=0) value = val3.value;
							else value = val3.formatter();
							if(value.indexOf("<span") > -1 || value.indexOf("<div") > -1 || value.indexOf("<button") > -1|| value.indexOf("<font") > -1){
								value = value.substring(value.indexOf(">")+1, value.indexOf("</"));
							}
							val[val2] = value;
							return false;
						}
					}
				});

				excelRow[val2] = val[val2];
			});
			excelData.push(excelRow);
		});

		excelData.unshift(header);
		//console.log(excelData);
	}
	if(xlsName.indexOf(".xls") > 0){
		xlsName = replaceAllString(xlsName,".xlsx", "");
		xlsName = replaceAllString(xlsName,".xls", "");
	}
	var filePath = path + "/" + xlsName + ".xlsx";
	data = {
		headerDef	: headerDef,
		//headerAlign	: headerAlign,
		filePath	: filePath,
		excelData	: excelData,
		requestType	: 'setExcel'
	}
	ajaxReturnData = null;
	ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) return;
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		return;
	}
	successExcelExport(ajaxReturnData, xlsName);
}

function excelExportJqGrid(gridData, xlsName){

	var data = new Object();
	data = {
			pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	var ajaxReturnData = null;
	var path = null;
	ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxReturnData.indexOf('ERROR')>-1 || ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR') {
		dialog.alert(ajaxReturnData.substr(5));
		return;
	}
	path = ajaxReturnData;
	data = null;

	if(path == null) {
		dialog.alert("Excel 저장 실패");
		return;
	} else {
		var col = gridData.getGridParam("colModel");
		var headerDef = [];
		//var headerAlign = [];
		var header = new Object();
		for(var i = 0; i < col.length; i++) {
			if(col[i].name == "rn"){
				continue;
			}
			headerDef.push(col[i].name);
			header[col[i].name] = col[i].label;
			//headerAlign.push(col[i].align);
		}
		var excelData = [];
		$.each(gridData.getGridParam("data"), function(i, val) {
			var excelRow = new Object();
			$.each(headerDef, function(j, val2) {
				if(val[val2] == undefined || val[val2] == null){
					val[val2] = "";
				}
				excelRow[val2] = val[val2];
			});
			excelData.push(excelRow);
		});

		excelData.unshift(header);
	}
	if(xlsName.indexOf(".xls") > 0){
		xlsName = replaceAllString(xlsName,".xls", "");
	}
	var filePath = path + "/" + xlsName + ".xls";
	data = {
		headerDef	: headerDef,
		//headerAlign	: headerAlign,
		filePath	: filePath,
		excelData	: excelData,
		requestType	: 'setExcel'
	}
	var ajaxResult = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxResult == 'ERR') {
		//console.log('setExcel ERROR');
		//return;
		ajaxResult = filePath;
	}
	successExcelExport(ajaxResult, xlsName);
}

function successExcelExport(data, xlsName) {
	xlsName = xlsName + "_" + userId + ".xlsx";
	fileDown(data, xlsName);
}

function fileDown(fullPath, name){
	var name2;
	// ajax 통신으로 에러가 뜨는지 확인
	fullPath = decodeURIComponent(fullPath);
	name2= fullPath;
	name = decodeURIComponent(name);

	fullPath = encodeURIComponent(fullPath, 'utf-8');
	fullPath = encodeURIComponent(fullPath, 'utf-8');

	// utf-8로 두번 인코딩
	name = encodeURIComponent(name,'utf-8');
	name = encodeURIComponent(name,'utf-8');
	var ajax = $.ajax({
		type 	: 'GET',
		url 	: homePath+'/webPage/fileupload/upload?fullPath='+fullPath+'&fileName='+name,
		contentType: "application/json; charset=UTF-8",
		async 	: false
	}).fail(function($xhr,status ,error) {
		dialog.alert("첨부파일 다운로드 오류 발생!!");
		return;


		if($xhr != undefined && $xhr != null && $xhr.responseText != ""){
			var jsonValue 		= $.parseJSON(($xhr.responseText).replace(/^\s+/,""));	// response
			var errorMessage 	= jsonValue.message;				// response error mesage
			
			if(errorMessage.indexOf("지정된 파일을 찾을 수 없습니다") > 0 || errorMessage.indexOf("해당 파일 또는 디렉토리가 없습니다") > 0){
				dialog.alert("파일명 : " + name2 + "\n파일이 존재하지 않습니다.");
				return;
			} else {
				dialog.alert("첨부파일 다운로드 오류 발생!!");
				return;
			}
		} else {
			dialog.alert("첨부파일 다운로드 오류 발생!!");
			return;
		}
	}).done(function(result){
		location.href=homePath+'/webPage/fileupload/upload?fullPath='+fullPath+'&fileName='+name;
	});

	ajax = null;
}

//Backspace 뒤로가기 막기
$(document).keydown(function(e){
	if(e.target.nodeName != "INPUT" && e.target.nodeName != "TEXTAREA"){
		if(e.keyCode == 8){
			return false;
		}
	}
	// readonly 상태일때도 뒤로가기 막음
	if(e.target.readOnly){
		if(e.keyCode == 8){
			return false;
		}
	}
});
function gridSort(targetGrid) {
	var sortInfo = targetGrid.getColumnSortInfo();
	if(sortInfo.length > 0) {	
		var sort = [];
		for (var i = 0; i < sortInfo.length; i++) {
			var tmpInfo = {};
			tmpInfo[sortInfo[i].key] = sortInfo[i];
			sort.push(tmpInfo);
		}
		console.log(sort);
		targetGrid.setColumnSort(sort);
		console.log("complete sort");
	}
}

//글자수 바이트 세기
function getByteLength(s,b,i,c){
	for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?2:c>>7?2:1);
	return b;
}
if(!Math.trunc){
	Math.trunc = function(v){
		return v<0 ? Math.ceil(v) : Math.floor(v);
	};
}

/**
 *  tab frame 내의 요소들이 로딩 완료됐는지 체크하는 함수.
 *  각 tab의 ready function 마지막 부분에 completeReadyFunc = true; 를 넣어줘야 정상작동
 *  
 * @param frames -> tab frame id배열
 * @param func 	 -> 로딩 완료 후 실행시킬 함수명
 * @param param	 -> 실행시킬 함수의 파라미터
 * @returns
 * * 사용 예 : checkFrameLoad(["frmProgBase","frmProgHistory"], screenInit_prog, 'I');
 */
function checkFrameLoad(frames, func, param) {
	var frameInter;
	var framesContent = [];
	var readyCnt = 0;
	for(i in frames) {
		var frame = $("#" + frames[i]).get(0).contentWindow;
		framesContent.push(frame);
	}
	frameInter = setInterval(function() {
//		console.log("exec inter...");
		for(var i = framesContent.length-1; i >= 0; i--) {
//			console.log(framesContent[i].name, framesContent[i].completeReadyFunc);
			if(framesContent[i].completeReadyFunc == true) {
				readyCnt++;
				framesContent.splice(i,1);
			}
		}
		if(readyCnt >= frames.length) {
			console.log("프레임 준비완료");
			clearInterval(frameInter);
			func(param);
		}
	}, 200);
}

function getDocPath(){
	var data = new Object();
	data = {
		pCode 		: '21',
		requestType	: 'getTmpDir'
	}
	var ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxReturnData.indexOf('ERROR')>-1 || ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR') {
		dialog.alert(ajaxReturnData.substr(5));
		return '';
	}
	return ajaxReturnData;
}

//index 값을 character으로 변경
//ex. '123'.replaceAt(1,'a') => 1a3
String.prototype.replaceAt=function(index, character) {
    return this.substr(0, index) + character + this.substr(index+character.length);
}

//자리수만큼 남는 앞부분을 0으로 채움
//ex. fillZero(4,'a'); => 000a
function fillZero(width, str){
    return str.length >= width ? str:new Array(width-str.length+1).join('0')+str;
}