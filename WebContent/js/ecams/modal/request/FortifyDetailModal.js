var popSelItem	= window.parent.popSelItem;

$(document).ready(function() {
	$('#txtName').val(popSelItem.name);
	$('#txtDetail').val(popSelItem.detail);
	$('#txtStory').val(popSelItem.story);

	// 닫기
	$('#btnClose').bind('click', function() {
		popClose();
	});
});


// 모달 닫기
function popClose() {
	window.parent.fortifyDetailModal.close();
}