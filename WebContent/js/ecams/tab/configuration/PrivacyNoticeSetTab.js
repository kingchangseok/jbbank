/**
 * [환경설정 > 개인정보처리 유의사항설정 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 정호윤
 * 	버전 		: 1.0
 *  수정일 	: 2020-04-20
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var InfonotiMsg	= null;
var maxByte     = 2000;

$(document).ready(function() {
	
	$('#byteInfo').text("0/2000 Byte");
	
	//유의사항 내용 조회
	getInfonotiMsg();
	
	// 등록
	$('#btnReq').bind('click', function() {
		insertInfonotiMsg();
	});
	
	//textarea에 입력 byte 체크
	$('#txtInfonotiMsg').keyup( function() {
		fnChkByte();
	})
});

//textarea에 입력 byte 체크
function fnChkByte(){
	var str = $('#txtInfonotiMsg').val().trim();
	var str_len = str.length;//현재 입력된 byte값
	
	var rbyte = 0;
	var rlen = 0;
	var one_char = "";
	var str2 = "";
	var i = 0;
	
	for ( i=0 ; i<str_len ; i++ ) {
		one_char = str.charAt(i);
		if ( escape(one_char).length > 4 ) rbyte += 2 //한글입력 2byte
		else rbyte++; //나머지는 1byte
		
		if ( rbyte <= maxByte ) rlen = i+1; //return 할 문자열 개수
	}
//	console.log('rbyte:',rbyte);
	
	if ( rbyte > maxByte ){
		dialog.alert("개인정보 유의사항 내용을 "+maxByte+"Byte 이내로 입력하시기 바랍니다.");
		str2 = str.substr(0, rlen); //초과 문자열 자르기
		$('#txtInfonotiMsg').val(str2);
		fnChkByte();
	} else {
		var tmpStr = rbyte + '/2000 byte';
		$('#byteInfo').text(tmpStr);
		var tmpstr2 = $('#byteInfo').val();
	}
}

//유의사항 내용 조회
function getInfonotiMsg() {
	var data = new Object();
	data = {
		requestType	: 'selectINFONOTIMSG'
	}
	ajaxAsync('/webPage/administrator/Configuration', data, 'json',successSelectINFONOTIMSG);
	data = null;
}
// 작업서버정보 리스트 가져오기 완료
function successSelectINFONOTIMSG(data) {
	$('#txtInfonotiMsg').val(data);
	var tmpStr = $('#txtInfonotiMsg').val().trim();
	fnChkByte();
}

//등록
function insertInfonotiMsg() {
	var txtInfonotiMsg 	= $('#txtInfonotiMsg').val().trim();
	
	if(txtInfonotiMsg.length === 0 ) {
		dialog.alert('개인정보처리 유의사항설정 값을 입력하여 주십시오.', function(){});
		return;
	}
	
	var data = new Object();
	data = {
		infonotimsg : txtInfonotiMsg,
		requestType	: 'updateINFONOTIMSG'
	}
	ajaxAsync('/webPage/administrator/Configuration', data, 'json',successUpdateINFONOTIMSG);
	data = null;
}
// 등록 완료
function successUpdateINFONOTIMSG(data) {
//	console.log('successUpdateINFONOTIMSG:',data);
	if(data !== 'OK') {
		dialog.alert(data, function(){});
	} else {
		dialog.alert('개인정보처리 유의사항이 등록 되었습니다.', function(){
		});
	}
}
