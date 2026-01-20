
$(document).ready(function() {
	getWords();
	
	$("#btnReq").bind('click', function() {
		addWords();
	});
});

function addWords() {
	var data = new Object();
	data = {
		words : $("#words").val(),
		requestType	: 'addTab8Info'
	}
	ajaxAsync('/webPage/administrator/Configuration', data, 'json',successAddWords);
}

function successAddWords(data) {
	dialog.alert("등록이 완료되었습니다.");
}

function getWords() {
	var data = new Object();
	data = {
		requestType	: 'getTab8Info'
	}
	ajaxAsync('/webPage/administrator/Configuration', data, 'json',successGetWords);
}

function successGetWords(data) {
	$("#words").val(data);
}