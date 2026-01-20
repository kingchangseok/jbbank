/** ISR종료 Tab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-05-07
 */

var userName = window.top.userName;
var userId = window.top.userId;
var adminYN = window.top.adminYN;
var userDeptName = window.top.userDeptName;
var userDeptCd = window.top.userDeptCd;
var strReqCd = window.top.reqCd;
var codeList    = window.top.codeList;          //전체 코드 리스트

var confirmDialog 	= new ax5.ui.dialog();

var strUserId = "";
var strIsrId = "";
var strReqCd = "";
var ingSw = false;
var listCnt = 0;
var strAcptNo = "";
var strComment = "";
var strSecuYN = "N";
var strIsrEditor = "";
var ingSt = true;

var lstDocFile_dp = [];
var getReqRFCInfo_dp = [];
var gridLst_dp = [];
var txtComment = null;
var lblName = null;
var lblCreatDt = null;
var lblUpdateDt = null;
var btnDel = null;
var btnUpd = null;
var hbTitle = null;
var hbComment = null;

$(document).ready(function() {
	// 등록
	$('#btnSave').bind('click', function() {
		btnSave_click();
	});
})

//관리자,담당자,변경관리관리자, 변경관리담당자, 테스트관리관리자, 테스트관리담당자, 릴리즈관리관리자는 Comment 등록 가능
function isrCommentCall() {
	getIsrInfo();
	getSecuInfo();
	getISRCmt();
}

function getIsrInfo() {
	var data = {
		IsrId : strIsrId,
		requestType : 'getIsrInfo'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json', successGetIsrInfo);	
}

function successGetIsrInfo(data) {
	strIsrEditor = data;
	if(strUserId == strIsrEditor){
		strSecuYN = "Y";
		$('#btnSave').attr('disabled', false);
	}
}

function getSecuInfo() {
	var data = {
		Sv_UserID : userId,
		RgtCd : 'R1,R2,C1,C2,T1,T2,D1',
		requestType : 'getSecuInfo'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json', succesGetSecuInfo);	
}

function succesGetSecuInfo(data) {
	if (data){
		strSecuYN = "Y";
		$('#btnSave').attr('disabled', false);
	}    		
}

function getISRCmt() {
	var data = {
		IsrId : strIsrId,
		userId : strUserId,
		requestType : 'getISRCmt'
	}
	ajaxAsync('/webPage/rfc/RFCRegister', data, 'json', succesGetISRCmt);	
}

function succesGetISRCmt(data) {
	gridLst_dp = data;
	listCnt = gridLst_dp.length;
	if($('#mainContent').children('#addDiv').length === 0) {
		for(var i = 0; gridLst_dp.length > i; i++) {
			var $div = $('<div id="addDiv" class="l_wrap dib vat half_wrap" style="width:100%;">' +
						 '  <div class="row">' +
						 '  	<label class="dib" id="lblName'+[i]+'"></label>' +
						 '  	<label class="dib" id="lblCreatDt'+[i]+'"></label>' +
						 '  	<label class="dib" id="lblUpdateDt'+[i]+'"></label>' +
						 '	</div> ' +
						 ' 	<div class="row">' +
						 '		<input id="acptNo'+[i]+'" type="text" style="display:none;">' +
						 '		<div class="dib vat width-80" style="vertical-align: middle;">' +
						 '			<textarea id="txtCommentNew'+[i]+'" name="txtCommentNew'+[i]+'" style="align-content:flex-start; width:100%; height:100px; resize: none; overflow-y:auto;font-weight:bold;" readonly></textarea>' +
						 '		</div>' +
						 '		<div id="btnDiv'+[i]+'" class="dib width-20" style="vertical-align: middle;">' +
						 '		</div>' +
						 '	</div>' +
						 '</div>')

			$('#mainContent').append($div);
			$div.appendTo($('#mainContent'));

			$('#lblName' + [i]).text("작성자: " + gridLst_dp[i].CC_CREATUSER);
			$('#lblCreatDt' + [i]).text("작성일: " + gridLst_dp[i].CC_CREATDT);
			$('#lblUpdateDt' + [i]).text("수정일: " + gridLst_dp[i].CC_LASTUPDT);
			$('#txtCommentNew' + [i]).val(gridLst_dp[i].CC_COMMENT);

			if(userId == gridLst_dp[i].CC_CREATUSER && strSecuYN == "Y") {
				var $btn = 	$('<button class="btn_basic_s" id="btnUpd'+[i]+'" style="margin-left: 10px;margin-right: 0px;width:70px;">수정</button>' +
							'<button class="btn_basic_s" id="btnDel'+[i]+'" style="margin:10 10 0 10;width:70px;display:block;">삭제</button>')

				$('#btnDiv'+[i]).append($btn);
				$btn.appendTo($('#btnDiv'+[i]));

				$('#txtCommentNew' + [i]).attr('readonly', false);
			}	
		}	
	}
}

function btnSave_click() {
	if($('#txtCommentNew').val()== null || $('#txtCommentNew').val()== "" || $('#txtCommentNew').val().length < 1 ){
		dialog.alert("Comment 내용을 입력하십시오.");
		return;
	}
	if(Number(byteChk($('#txtCommentNew').val()) > 1000)) {
		dialog.alert()("Comment는 1000byte 이하로 입력해주시기 바랍니다.\n(입력하신 Comment는 "+byteChk($('#txtCommentNew').val()).toString()+"byte 입니다.");
		return;
	}
	confirmDialog.confirm({
		title: "등록확인",
		msg: "Comment를 등록하시겠습니까?"
	}, function() {
		if(this.key == "ok") {
			cmtChk();
		}
	});
}

function btnUpd_click(objNum) {
	var acptNo = gridLst_dp[objNum].CC_ACPTNO;
	strAcptNo = acptNo;
	strComment = $('#txtCommentNew' + objNum).val();
	if(strComment == null || strComment == "" || strComment.length < 1 ){
		dialog.alert("Comment 내용을 입력하십시오.");
		return;
	}
	confirmDialog.confirm({
		title: "수정확인",
		msg: "Comment를 수정하시겠습니까?"
	}, function() {
		if(this.key == "ok") {
			cmtUpd();
		}
	});	
}

function cmtUpd() {
	var etcObj = new Object();
	etcObj.isrId = strIsrId;
	etcObj.userId = userId;
	etcObj.acptNo = strAcptNo;
	etcObj.comment = strComment;

	var dataObjInfo = new Object();
	dataObjInfo.dataObj = etcObj;
	dataObjInfo.requestType = "updComment";
	ajaxAsync('/webPage/rfc/RFCRegister', dataObjInfo, 'json', successUpdComment);

	dataObjInfo = null;
	etcObj = null;
}

function successUpdComment(data) {
	if (data == "0") {
		dialog.alert("Comment 가 정상적으로 수정되었습니다.");
		$('#mainContent').children('#addDiv').remove();
		isrCommentCall();
	} else {
		dialog.alert("Comment 수정 중 오류가 발생하였습니다.");	
		return;		
	}
}

function btnDel_click(objNum) {
	var acptNo = gridLst_dp[objNum].CC_ACPTNO;
	strAcptNo = acptNo;

	confirmDialog.confirm({
		title: "삭제확인",
		msg: "Comment를 삭제하시겠습니까?"
	}, function() {
		if(this.key == "ok") {
			cmtDel();
			ingSt = false;
		}
	});	
}

function cmtDel() {
	var etcObj = new Object();
	etcObj.isrId = strIsrId;
	etcObj.userId = userId;
	etcObj.acptNo = strAcptNo;	

	var dataObjInfo = new Object();
	dataObjInfo.dataObj = etcObj;
	dataObjInfo.requestType = "delComment";
	ajaxAsync('/webPage/rfc/RFCRegister', dataObjInfo, 'json', successDelComment);

	dataObjInfo = null;	
	etcObj = null;	
}

function successDelComment(data) {
	if (data == "0") {
		dialog.alert("Comment 가 정상적으로 삭제되었습니다.");
		$('#mainContent').children('#addDiv').remove();
		isrCommentCall();
	} else {
		dialog.alert("Comment 삭제 중 오류가 발생하였습니다.");	
		return;		
	}
}

function byteChk(obj){
    var codeByte = 0;
    for (var idx = 0; idx < obj.length; idx++) {
        var oneChar = escape(obj.charAt(idx));
        if ( oneChar.length == 1 ) {
            codeByte ++;
        } else if (oneChar.indexOf("%u") != -1) {
            codeByte += 2;
        } else if (oneChar.indexOf("%") != -1) {
            codeByte ++;
        }
    }
    return codeByte;
}

function cmtChk() {
	var etcObj = new Object();
	etcObj.isrId = strIsrId;
	etcObj.userId = userId;
	etcObj.comment = $('#txtCommentNew').val();

	var dataObjInfo = new Object();
	dataObjInfo.dataObj = etcObj;
	dataObjInfo.requestType = "setComment";
	ajaxAsync('/webPage/rfc/RFCRegister', dataObjInfo, 'json', successSetComment);

	dataObjInfo = null;
	etcObj = null;
	$('#txtCommentNew').val("");
}

function successSetComment(data) {
	if (data == "0") {
		dialog.alert("Comment 가 정상적으로 등록되었습니다.");	
		$('#mainContent').children('#addDiv').remove();
		isrCommentCall();
	} else {
		dialog.alert("Comment 등록 중 오류가 발생하였습니다.");	
		return;		
	}
}


$(document).on("click",'button[id^="btnUpd"]' ,function(){
	btnUpd_click($(this).attr('id').toString().replace(/[^0-9]/g,''));
});
$(document).on("click",'button[id^="btnDel"]' ,function(){
	btnDel_click($(this).attr('id').toString().replace(/[^0-9]/g,''));
});	