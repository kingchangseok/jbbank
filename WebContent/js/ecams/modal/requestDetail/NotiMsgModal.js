
$(document).ready(function() {
	getNotiMsg();
	$("#btnClose").bind('click', function() {
		popClose();
	})
})

function getNotiMsg() {	
	var ajaxData = {
		requestType: "getNotiMsg"
	};
	ajaxAsync('/webPage/winpop/PopRequestDetailServlet', ajaxData, 'json', successGetNotiMsg);
}

function successGetNotiMsg(data) {
	console.log(data);
	$("#textareaContents").text(data);
}

function popClose() {
	window.parent.notiMsgModal.close();
}