var pReqNo  = null;
var pUserId = null;

var datReqDate	= new ax5.ui.picker();	//처리일시 picker
var data        = null;

var f = document.getReqData;
pReqNo = f.acptno.value;
pUserId = f.user.value;

$('#txtAcptNo').val(pReqNo.substr(0,4)+'-'+pReqNo.substr(4,2)+'-'+pReqNo.substr(6));

function dateInit() {
	$('#txtPrcDate').val(getDate('DATE',0));
	datReqDate.bind(defaultPickerInfo('txtPrcDate', 'top'));
}

$(document).ready(function(){
	if (pUserId == null || pUserId == undefined || pUserId == '' || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pReqNo == null || pUserId == undefined || pUserId == '' || pReqNo.length != 12) {
		dialog.alert('신청번호가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	dateInit();
	
	//조회 클릭
	$('#btnSearCh').bind('click', function() {
		if (pReqNo == null) {
			return;
		}
		getSvrLog();
	});
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		window.open('about:blank','_self').self.close();
	});
	
	//최초 화면로딩 시 조회(조회버튼 로직)
	if (pReqNo.length == 12) {
		data = new Object();
		data = {
				acptNo 	: pReqNo,
			requestType : 'getReqInf'
		}
		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmm/Cmm1501Servlet', data, 'json');
		if (ajaxReturnData != null && ajaxReturnData != '' && ajaxReturnData != undefined) {
			if (typeof ajaxReturnData == 'string' && ajaxReturnData.indexOf('ERR')>=0) {
				dialog.alert(ajaxReturnData);
				return;
			} 
			$('#txtPrcDate').val(ajaxReturnData.substr(0,4)+'/'+ajaxReturnData.substr(4,2)+'/'+ajaxReturnData.substr(6));
			$('#btnSearCh').trigger('click');
		}
	} else $('#btnSearCh').trigger('click');
});

//조회클릭
function getSvrLog() {
	
	$('#txtSvrLog').val('');
	$('#txtRetLog').val('');
	
	data = new Object();
	data = {
		acptNo 		: pReqNo,
		strDate 	: replaceAllString($('#txtPrcDate').val(), '/', ''),
		requestType : 'getLogView'
	}
	ajaxAsync('/webPage/ecmm/Cmm1501Servlet', data, 'json',successGetLogView);
}

//로그가져오기  완료
function successGetLogView(data) {
	$('#txtSvrLog').val(data[0].file1);
	$('#txtRetLog').val(data[0].file2);
}