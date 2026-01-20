var pReqNo  = null;
var pUserId = null;

var f = document.getReqData;
pReqNo = f.acptno.value;
pUserId = f.user.value;

$(document).ready(function(){
	if (pUserId == null || pUserId == undefined || pUserId == '' || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pReqNo == null || pReqNo == undefined || pReqNo == '') {
		dialog.alert('결과파일이 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	//닫기
	$('#btnClose').bind('click', function() {
		window.open('about:blank','_self').self.close();
	});
	
	var data =  new Object();
	data = {
		rstfile : pReqNo,
		requestType	: 'cmr7010_result'
	}
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successGetCmr7010);
});

function successGetCmr7010(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	$('#txtResult').val(data);
}