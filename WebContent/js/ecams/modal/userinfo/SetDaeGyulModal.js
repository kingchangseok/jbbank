var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var txtUserId 	= window.parent.txtUserIdP;		// 사용자정보화면 사원번호에 적혀있는 ID
var popupYN = "Y";
$(document).ready(function() {
	if (userId == null || userId == '') return;
	
	if (txtUserId != userId) {
		dialog.alert('비정상적 접근입니다. 로그인을 다시 하시기 바랍니다.', function(){window.parent.parent.location.replace('/webPage/login/ecamsLogin.jsp');popClose();});
		return;
	}
	
	//닫기,취소
	$('#btnCncl').bind('click',function() {
		popClose();
	});
});
function frameOnload() {
	$("#ifmDaeGyul").contents().find("#history_wrap").css("display","none");
	$("#ifmDaeGyul").contents().find(".contentFrame").css("width",580);
	$("#ifmDaeGyul").contents().find(".contentFrame").css("margin-left",10);
	$("#ifmDaeGyul").contents().find(".padding-40-top").css("padding-top",10);
	$("#ifmDaeGyul").contents().find(".padding-40-top").css("padding-bottom",0);
	$("#ifmDaeGyul").contents().find("#btnClose").css("display","inline-block");
}

// 모달 닫기
function popClose() {
	window.parent.setDaeGyulModal.close();
}
