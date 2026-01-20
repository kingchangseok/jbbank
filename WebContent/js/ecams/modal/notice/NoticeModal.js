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

var stDate = "";
var edDate = "";
var now 	= new Date();
var picker 	= new ax5.ui.picker();
var request =  new Request();
var memo_date 	= null;
var noticeInfo 	= null;
var dialog 		= new ax5.ui.dialog({title: "확인"});

confirmDialog.setConfig({
    title: "공지사항 확인",
    theme: "info"
});

$('#dateStD').val(getDate('DATE',0));
$('#dateEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'bottom'));

$('input.checkbox-pop').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	popNoticeInit();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEdD').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEdD").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	// 팝업공지 클릭
	$('#chkPop').bind('click', function() {
		if($("#chkPop").is(":checked")){
			$('#divPicker').css('display','');
		}else {
			$('#divPicker').css('display','none');
		}
	})
	
	// 파일첨부
	$('#btnFile').bind('click', function() {
		fileOpen();
	});
	
	// 공지사항 삭제
	$('#btnRem').bind('click', function() {
		del();
	});
	
	$('#btnReg').bind('click', function() {
		checkNoticeVal();
	});
	
	$('#btnClose').bind('click', function() {
		popClose();
	});
});

// 화면 초기화
function popNoticeInit() {
	$('#divPicker').css('display','none');
	$('#btnRem').css('display','none');
	$('#btnReg').css('display','none');
	$('#chkPop').wCheck('disabled',true);
	
	noticeInfo = window.parent.noticePopData;
	if(noticeInfo !== null) {
		if(noticeInfo.CM_NOTIYN === 'Y') {
			/*
			var startDate 	= replaceAllString(noticeInfo.CM_STDATE, "/", "");
			var endDate 	= replaceAllString(noticeInfo.CM_EDDATE, "/", "");
			startDate 		= ax5.util.date(startDate, {'return': 'yyyy/MM/dd', 'add': {d: 0}} );
			endDate 		= ax5.util.date(endDate, {'return': 'yyyy/MM/dd', 'add': {d: 0}} );
			*/
			
			$('#dateStD').val(noticeInfo.CM_STDATE);
			$('#dateEdD').val(noticeInfo.CM_EDDATE);
			picker.bind(defaultPickerInfoLarge('basic', 'bottom'));
			$('#divPicker').css('display','');
			$('#chkPop').wCheck('check', true);
		}
		
		$('#txtTitle').val(noticeInfo.CM_TITLE);
		$('#textareaContents').val(noticeInfo.CM_CONTENTS);
		
		if(userId !== noticeInfo.CM_EDITOR) {
			$('#btnRem').css('display','none');
			$('#btnReg').css('display','none');
			$('#chkPop').wCheck('disabled',true);
			
			if(noticeInfo.CM_NOTIYN === 'Y') {
				$('#chkPop').wCheck("disabled", true);
				
				disableCal(true, 'dateStD');
				disableCal(true, 'dateEdD');
				$('#dateStD').prop( "disabled", 	true );
				$('#dateEdD').prop( "disabled", 	true );
			}
			
			$('#txtTitle').attr('disabled',true);
			$('#textareaContents').attr('disabled',true);
		} else {
			$('#btnRem').css('display','');
			$('#btnReg').css('display','');
			$('#chkPop').wCheck('disabled',false);
		}
		
		$('#btnFile').text('첨부파일');
	} else if ( noticeInfo === null ) {
		$('#btnReg').css('display','');
	}
}

// 팝업 닫기
function popClose(){
	window.parent.fileLength = 0;
	window.parent.fileUploadModal.close();
	window.parent.popNoticeModal.close();
}

//공지사항 등록 및 수정시 유효성 체크
function checkNoticeVal(){
	var TODATE 		= "";
	var monthStr 	= "";
	var dayStr 		= "";
	
	var txtTitle 			= $('#txtTitle').val().trim();
	var textareaContents	= $('#textareaContents').val().trim();
	
	if($("#chkPop").is(":checked")){
		stDate = replaceAllString($("#dateStD").val(), "/", "");
		edDate = replaceAllString($("#dateEdD").val(), "/", "");
	}
	
	if(txtTitle.length === 0) {
		dialog.alert('제목을 입력하십시오.', function () {});
		return;
	}
	
	if(textareaContents.length === 0 ) {
		dialog.alert('내용을 입력하십시오.', function () {});
		return;
	}
	
	if($("#chkPop").is(":checked") && (edDate<stDate)) {
		dialog.alert('날짜 등록이 잘못되었습니다.', function () {});
		return;
	}
	
	confirmDialog.confirm({
		msg: '등록하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			updateNotice();
		}
	});
}

// 공지사항 수정 및 등록
function updateNotice(){

	var updateData = {};
	if(noticeInfo === null) {
		updateData.memo_id = '';
		updateData.user_id = userId;
	} else {
		updateData.memo_id = noticeInfo.CM_ACPTNO;
		updateData.user_id = noticeInfo.CM_EDITOR;
	}
	
	updateData.txtTitle 		= $("#txtTitle").val().trim();
	updateData.textareaContents = $("#textareaContents").val().trim();

	if($("#chkPop").is(":checked")){
		updateData.chkNotice = "true";
	} else {
		updateData.chkNotice = "false";
	}
	updateData.stDate = stDate;
	updateData.edDate = edDate;
	
	var data = new Object();
	data = {
		requestType : 'get_update_Qry',
		UserID : userId,
		AcptNo : updateData.memo_id,
		Title : updateData.txtTitle,
		Txt_Body : updateData.textareaContents,
		NotiYN : updateData.chkNotice,
		CM_STDATE : updateData.stDate,
		CM_EDDATE : updateData.edDate
//		dataObj : updateData
	}
	ajaxAsync('/webPage/ecmm/Cmm2101Servlet', data, 'json',successUpdateNotice);
	
}

// 공지사항 수정 및 등록 완료
function successUpdateNotice(data) {
	// 첨부파일 존재시
	if(window.parent.fileLength > 1) {
		window.parent.uploadAcptno = data;
		window.parent.showAndHideUploadModal('show');
		$(window.parent.fileUploadBtn).trigger('click');
	}
	dialog.alert('등록 되었습니다.', function () {
		window.parent.fileLength = 0;
		window.parent.fileUploadModal.close();
		window.parent.popNoticeModal.close();
	});
}


//파일첨부
function fileOpen() {
	if(noticeInfo !== null) {
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

// 공지사항 삭제
function del() {
	confirmDialog.confirm({
		msg: '공지사항을 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			
			stDate = replaceAllString($("#dateStD").val(), "/", "");
			edDate = replaceAllString($("#dateEdD").val(), "/", "");
			
			// var delData = {};
			// delData.memo_id = noticeInfo.CM_ACPTNO;
			// delData.user_id = noticeInfo.CM_EDITOR;
			// delData.txtTitle = document.getElementById("txtTitle").value;
			// delData.textareaContents = $("#textareaContents").val();
			// delData.chkNotice = $('#chkPop').prop("checked").toString();
			// delData.stDate = stDate;
			// delData.edDate = edDate;
			var notiyn = "";

			if($('#chkPop').prop("checked").toString()) {
				notiyn = 'true';
			} else {
				notiyn = 'false';
			}

			var data = new Object();
			data = {
				requestType : 'get_delete_Qry',
				AcptNo : noticeInfo ? noticeInfo.CM_ACPTNO : '',
				UserID : noticeInfo ? noticeInfo.CM_EDITOR : '',
				Title : $("#txtTitle").val().trim(),
				Txt_Body : $("#textareaContents").val().trim(),
				NotiYn : notiyn,
				CM_STDATE : stDate,
				CM_EDDATE : edDate,
			}
			ajaxAsync('/webPage/ecmm/Cmm2101Servlet', data, 'json',successDel);
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