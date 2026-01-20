/**
 * 공지사항-더블클릭 팝업 화면의 기능 정의
 * 
 * <pre>
 * &lt;b&gt;History&lt;/b&gt;
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-02-08
 * 
 */


var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var selectData = [];
var strUserId = "";
var strAcptNo = "";
var Kind = "";
var RepyN = "";
var SeqNo = 0;
var request =  new Request();
var memo_date 	= null;
var noticeInfo 	= null;
var dialog 		= new ax5.ui.dialog({title: "확인"});

confirmDialog.setConfig({
    title: "공지사항 확인",
    theme: "info"
});

$(document).ready(function() {
	if(adminYN){
		adminYN = 'Y'
	}else{
		adminYN = 'N'
	}

	popNoticeInit();
	
	// 파일첨부
	$('#btnFile1').bind('click', function() {
		fileUploadOpen();
	});
	$('#btnFile2').bind('click', function() {
		fileDownloadOpen();
	});
	// 공지사항 삭제
	$('#btnDel').bind('click', function() {
		del();
	});
	
	// FAQ 등록
	$('#btnReg').bind('click', function() {
		checkNoticeVal();
	});
	
	// 닫기 버튼
	$('#btnClose').bind('click', function() {
		popClose();
	});
	
//	// 답변
//	$('#btnReply').bind('click', function() {
//		reply();
//	});
//	
//	//답변등록
//	$('#btnReplyReg').bind('click', function() {
//		replyReg();
//	});
	
});


// 화면 초기화
function popNoticeInit() {
	
	$('#btnReg').css('display','none');
	$('#btnReplyReg').css('display','none');
	$('#btnFile1').css('display','none');
	$('#btnFile2').css('display','none');
	$('#btnDel').css('display','none');
	$('#btnReply').css('display','none');
	
	var dataObj = window.parent.dataObj;
	noticeInfo = window.parent.noticePopData;
	
	strUserId = dataObj.user_id;
	strAcptNo = dataObj.memo_id;
	Kind = dataObj.memo_date;
	RepyN = dataObj.repyn;
	
	if((strAcptNo == null || strAcptNo == undefined || strAcptNo == "")){
		
		$('#btnReg').css('display','inline-block');
		$('#btnReplyReg').css('display','none');
		$('#btnFile1').css('display','inline-block');
		$('#btnFile2').css('display','none');
		$('#btnDel').css('display','none');
		$('#btnReply').css('display','none');
	}else{
		var data = new Object();
		data = {
			AcptNo		: strAcptNo,
			Kind 		: Kind,
			requestType : 'get_sql_Qry'
		}
		ajaxAsync('/webPage/ecmm/Cmm2301Servlet', data, 'json',successGetNotice);
	}
}

function successGetNotice(data){
	
	selectData = data;
	
	if(Kind == '2' && RepyN == 'n'){
		$('#btnFile1').css('display','none');
		
		if(selectData[0].filecnt < 1){
			$('#btnFile2').css('display','none');
		}else{
			$('#btnFile2').css('display','inline-block');
		}
		
		if(selectData[0].CM_EDITOR == strUserId){
			$('#btnReg').css('display','inline-block');
			$('#btnReg').text('수정');
			$('#btnDel').css('display','inline-block');
			$('#btnReplyReg').css('display','none');
			$('#btnReply').css('display','none');
			
//			if(adminYN == 'Y'){
//				$('#btnReply').css('display','inline-block');
//			}
			
		}else{
			$('#btnReg').css('display','none');
			$('#btnDel').css('display','none');
			$('#btnReplyReg').css('display','none');
			$('#btnReply').css('display','none');
			
//			if(adminYN == 'Y'){
//				$('#btnReply').css('display','inline-block');
//			}
		}
		
		$("#textareaContents").val(data[0].CM_CONTENTS);
    	$("#txtTitle").val(data[0].CM_TITLE);
    	selectData[0].CM_EDITOR = strUserId;
    	selectData[0].CM_ACPTNO = strAcptNo;
	}else if((Kind == "2" && RepyN == "y") || Kind == '3'){
		$('#btnFile1').css('display','inline-block');
		
		if(RepyN == 'y'){
			$("#textareaContents").val("----본문내용----\n" + data[0].CM_CONTENTS + "\n" + "-------------------------\n");
	    	$("#txtTitle").val("[답글]" + data[0].CM_TITLE);
	    	$('#btnReplyReg').css('display','inline-block');
	    	$('#btnReg').css('display','none');
			$('#btnDel').css('display','none');
	    	$('#btnReply').css('display','none');
		}else{
			$("#textareaContents").val(data[0].CM_CONTENTS);
	    	$("#txtTitle").val(data[0].CM_TITLE);
	    	$("#btnReplyReg").text('답글수정');
	    	$('#btnDel').css('display','inline-block');
	    	$('#btnReg').css('display','none');
	    	$('#btnReply').css('display','none');
		}
		selectData[0].CM_EDITOR = strUserId;
		selectData[0].CM_ACPTNO = strAcptNo;
	}
}

// 팝업 닫기
function popClose(){
	if(RepyN == "y"){
		window.parent.popNoticeModal2.close();
	} else {
		window.parent.fileLength = 0;
		window.parent.fileUploadModal.close();
		window.parent.popNoticeModal.close();
	}
}

//공지사항 등록 및 수정시 유효성 체크
function checkNoticeVal(){
	
	if( $('#txtTitle').val().length === 0) {
		dialog.alert('제목을 입력하십시오.', function () {});
		return;
	}
	
	if($('#textareaContents').val().length === 0 ) {
		dialog.alert('내용을 입력하십시오.', function () {});
		return;
	}
	
	confirmDialog.confirm({
		msg:  $("#btnReg").text() +'하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			updateNotice();
		}
	});
}
function reply(){
	window.parent.dataObj.memo_id = strAcptNo;
	window.parent.dataObj.memo_date = Kind;
	window.parent.dataObj.user_id = strUserId;
	window.parent.dataObj.seqno = 0;
	window.parent.dataObj.repyn = "y";
	window.parent.openPopNotice("reply");
}

function replyReg(){
	if( $('#txtTitle').val().length === 0) {
		dialog.alert('제목을 입력하십시오.', function () {});
		return;
	}
	if($('#textareaContents').val().length === 0 ) {
		dialog.alert('내용을 입력하십시오.', function () {});
		return;
	}
    SeqNo = 0;
	var i = SeqNo;
	
	var data = new Object();
	data = {
		AcptNo		: strAcptNo,
		SeqNo		: i,
		UserID 		: strUserId,
		Title 		: $('#txtTitle').val().trim(),
		Txt_Body	: $('#textareaContents').val().trim(),
		requestType : 'get_reply_Qry'
	}
	ajaxAsync('/webPage/ecmm/Cmm2301Servlet', data, 'json',successReplyNotice);
	
}

function successReplyNotice(data){
	
	confirmDialog.confirm({
		msg: '답글을 등록하셨습니다.',
	}, function(){
		if(this.key === 'ok') {
			window.parent.fileLength = 0;
			window.parent.fileUploadModal.close();
			window.parent.popNoticeModal.close();
			window.parent.popNoticeModal2.close();
		}
	});
}

// 공지사항 수정 및 등록
function updateNotice(){

	var strAcptNo = ""; 
	var Kind = '2';
	
	if (noticeInfo != null && noticeInfo != undefined){
		strAcptNo = noticeInfo.CM_ACPTNO;
		Kind = noticeInfo.CM_GBNCD;
	}
	
	var data = new Object();
	data = {
		AcptNo		: strAcptNo,
		UserID 		: userId,
		Title 		: $('#txtTitle').val().trim(),
		Txt_Body	: $('#textareaContents').val().trim(),
		Kind 		: Kind,
		requestType : 'get_update_Qry'
	}
	ajaxAsync('/webPage/ecmm/Cmm2301Servlet', data, 'json',successUpdateNotice);
	
}

// 공지사항 수정 및 등록 완료
function successUpdateNotice(data) {
	// 첨부파일 존재시
	if(window.parent.fileLength > 1) {
		window.parent.uploadAcptno = data;
		window.parent.showAndHideUploadModal('show');
		$(window.parent.fileUploadBtn).trigger('click');
	}
	dialog.alert( $("#btnReg").text() + ' 되었습니다.', function () {
		window.parent.fileLength = 0;
		window.parent.fileUploadModal.close();
		window.parent.popNoticeModal.close();
	});
}

//파일첨부
function fileUploadOpen() {
	
	if(noticeInfo != null && noticeInfo != undefined && noticeInfo != "") {
		window.parent.downAcptno = noticeInfo.CM_ACPTNO;
		window.parent.downFileCnt = noticeInfo.fileCnt;
		window.parent.openFileDownload('','');
	} else {
		if(window.parent.checkModalLength() > 1) {
			window.parent.showAndHideUploadModal('show');
		}
		else window.parent.openFileUpload();
	}
}

function fileDownloadOpen(){
	window.parent.downAcptno = noticeInfo.CM_ACPTNO;
	window.parent.downFileCnt = noticeInfo.fileCnt;
	window.parent.openFileDownload(noticeInfo.CM_ACPTNO,noticeInfo.fileCnt);
}

// 공지사항 삭제
function del() {
	confirmDialog.confirm({
		msg: "삭제하시겠습니까?",
	}, function(){
		if(this.key === 'ok') {

			var data = new Object();

			data = {
				requestType : 'get_delete_Qry',
				AcptNo : strAcptNo,
				Kind : Kind
			}
			ajaxAsync('/webPage/ecmm/Cmm2301Servlet', data, 'json',successDel);
		}
	});
}

// 공지사항 삭제 완료
function successDel(data) {
	dialog.alert('공지사항이 삭제되었습니다.', function () {
		window.parent.fileLength = 0;
		window.parent.fileUploadModal.close();
		window.parent.popNoticeModal.close();
	});
}