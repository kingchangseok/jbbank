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
	
	window.parent.selCode 	= null;
	
	if(selCode !== null) {
		$('#txtCode').val(selCode);
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
	var code 	= $('#txtCode').val().trim();
	var requestType = "";
	
	if(code.length <= 0) {
		dialog.alert('영향분석 대상확장자를 입력해주세요.',function() {});
		return;
	}
	
	if(code.indexOf('.') < 0){
		dialog.alert('대상확장자 . 을 포합하여 입력해주세요.',function() {});
		return;
	}
	
	if(code.substring(0,1) != "."){
		dialog.alert('첫 문자는 . 이어야 합니다.',function() {});
		return;
	}
	
	if(code.indexOf(',') >= 0){
		dialog.alert('대상확장자에 , 는 들어갈 수 없습니다.',function() {});
		return;
	}
	
	var jobInfoData = new Object(); 
	var etcData = new Object();
	etcData.code = code;

	
	if(selCode == null){
		requestType = "inAnalExe";
	} else {
		requestType = "upAnalExe";
		etcData.delAnalExe = selCode;
		etcData.upAnalExe = code;
	}
	
	jobInfoData = {
		etcData 	: etcData,
		requestType	: requestType,
	}
	ajaxAsync('/webPage/modal/sysinfo/Job', jobInfoData, 'json',successSetJobInfo);
}

function successSetJobInfo(data) {
	console.log(data);
	if(data) {
		dialog.alert('영향분석 대상확장자를 저장했습니다.', function() {
			popClose();
			window.parent.getAnalExeList();
		});
	} else {
		dialog.alert('영향분석 대상확장자 저장을 실패했습니다.',function(){});
	}
}

function popClose(){
	window.parent.jobUpModal.close();
}



