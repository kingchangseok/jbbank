var userId 		= window.parent.userId;			//접속자 ID
var reqCd 		= window.parent.reqCd;			//접속자 ID
var sysCd 		= window.parent.sysCd;
var jobCd		= window.parent.jobCd;	

var popData = [];


$(document).ready(function() {
	// 등록 버튼
	$('#btnSave').bind('click', function() {
		btnAdd_Click();
	});
	// 취소 버튼
	$('#btnCancel').bind('click', function() {
		btnCancel_click();
	});
});

function btnAdd_Click() {
	$('#txtTest').val($('#txtTest').val().toUpperCase());
	
	if ($('#txtTest').val().length == 0){
		dialog.alert("테스트조건을 입력해주세요.");
		return;
	}
	if ($('#txtCheckList').val().length == 0){
		dialog.alert("확인사항을 입력해주세요.");
		return;
	}
	popData.code = $('#txtTest').val()
	popData.value = $('#txtCheckList').val()
	window.parent.integrationData = popData;
	
	popClose();
}

function popClose(){
	window.parent.IntegrationModal.close();
}

function btnCancel_click() {
	popClose();
}