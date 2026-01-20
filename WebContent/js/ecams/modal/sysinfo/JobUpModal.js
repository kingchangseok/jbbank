/**
 * 업무등록편집 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-31
 * 
 */

var selCode = null;
var selVal	= null;
var selPro	= null;
$(document).ready(function(){
	
	selCode = window.parent.selCode;
	selVal 	= window.parent.selVal;
	selPro = window.parent.selPro;
	
	window.parent.selCode 	= null;
	window.parent.selVal 	= null;
	window.parent.selPro 	= null;
	
	if(selCode !== null) {
		$('#txtCode').val(selCode);
		$('#txtVal').val(selVal);
//		$('#txtPro').val(selPro);
	} else {
		$('#lblTitle').text('[업무정보 만들기]');
	}
	
	$('#btnJobUp').bind('click',function() {
		jobValCheck();
	});
	
	// 닫기
	$('#btnJobUpClose').bind('click',function() {
		popClose();
	});
	
});

function jobValCheck() {
	var code 	= $('#txtCode').val();
	var value	= $('#txtVal').val();
	
	if(code.length <= 0) {
		dialog.alert('업무코드를 입력해주세요.',function() {});
		return;
	}
	if(value.length <= 0) {
		dialog.alert('업무명을 입력해주세요.',function() {});
		return;
	}
	
	var jobInfoData = new Object(); 
	var etcData = new Object();
	
	jobInfoData = {
		cm_jobcd : code,
		cm_jobname : value,
//		cm_deptcd : $("#txtPro").val().trim(),
		requestType	: 'setJobInfo_individual',
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', jobInfoData, 'json',successSetJobInfo);
}

function successSetJobInfo(data) {
	if(data) {
		dialog.alert('업무정보를 저장했습니다.', function() {
			popClose();
			window.parent.getJobList();
		});
	} else {
		dialog.alert('업무정보 저장을 실패했습니다.',function(){});
	}
}

function popClose(){
	window.parent.jobUpModal.close();
}



