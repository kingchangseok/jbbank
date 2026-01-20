/**
 * 개발계획/완료 탭 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 허정규
 * 	버전 : 1.0
 *  수정일 : 2020-04-23
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var codeList        = window.top.codeList;      //전체코드리스트

//public
var strStatus		= window.parent.strStatus;
var strIsrId		= window.parent.strIsrId;  

var picker = new ax5.ui.picker();

var strReqCD = "";
var SrID = "";
var SysCd = "";
var SrTitle = "";
var SRStatus = "";
var GradeCd = "";
var AcptNo = "";
var Endmsg = "";
var Cnchk = window.top.Cnchk;


ax5.info.weekNames = [
    {label: "일"},
    {label: "월"},
    {label: "화"},
    {label: "수"},
    {label: "목"},
    {label: "금"},
    {label: "토"}
];

$(document).ready(function(){
	
	$("#cmdReq").bind("click",function(){
		cmdReq_click();
	});
	
});


function setSRInfo_detail_func() {
	if(SrID == "" || SrID == null) return;
	$("#lblComment").hide();
	$("#lblComment").text("");
	
	$("#txtsrid").val(SrID);
	$("#txttitle").val(SrTitle);
	$("#txtcomment").val(Endmsg);
	
	if(strReqCd != "65" || SRStatus == "03" || SRStatus == "04" || SRStatus == "09" || SRStatus == "47"){
		$("#txtcomment").prop("disabled",true);
		$("#cmdReq").prop("disabled",true);
		$("#lblComment").show();
		$("#lblComment").text("");
		
		if(SRStatus == "03"){
			$("#lblComment").text("[SR취소불가] 관리자취소됨");
		}else if(SRStatus == "04"){
			$("#lblComment").text("[SR취소불가] SR취소상태");
		}else if(SRStatus == "09"){
			$("#lblComment").text("[SR취소불가] SR처리완료상태");
		}else if(SRStatus == "47"){
			$("#lblComment").text("[SR취소불가] SR취소상태");
		}
	}else{
		//if(SRStatus == "02" || devstatus == "0" || devstatus == "4"){
		if(SRStatus == "02" || SRStatus == "11"){
			// 2016.02.16 SR취소권한					START
			if(Cnchk == "Y"){
				$("#txtcomment").prop("disabled",false);
				$("#cmdReq").prop("disabled",false);
			}else {
				$("#txtcomment").prop("disabled",true);
				$("#cmdReq").prop("disabled",true);
			}
			// 2016.02.16 SR취소권한					END
		}else{
			$("#txtcomment").prop("disabled",true);
			$("#cmdReq").prop("disabled",true);
			$("#lblComment").show();
			$("#lblComment").text("");
			
			if(SRStatus == "01"){
				$("#lblComment").text("[SR취소불가] SR등록상태 (SR등록자가 취소가능)");
			}if(SRStatus == "03"){
				$("#lblComment").text("[SR취소불가] 관리자취소됨");
			}else if(SRStatus == "04"){
				$("#lblComment").text("[SR취소불가] SR취소상태");
			}else if(SRStatus == "09"){
				$("#lblComment").text("[SR취소불가] SR처리완료상태");
			}else if(SRStatus == "47"){
				$("#lblComment").text("[SR취소불가] SR취소상태");
			}else if(SRStatus == "12"){
				$("#lblComment").text("[SR취소불가] 개발중 (체크아웃취소 후 SR취소 재시도)");	
				if(RsrcCnt == "0" && Cnchk == "Y") {
					$("#txtcomment").prop("disabled",false);
					$("#cmdReq").prop("disabled",false);
					$("#lblComment").hide();
					$("#lblComment").text("");
				}						
			}else if(SRStatus == "13"){
				$("#lblComment").text("[SR취소불가] 개발완료 (체크아웃취소 후 SR취소 재시도)");
			}else{
				$("#lblComment").text("[SR취소불가] 적용중 (반려/회수 -> 체크아웃취소 후 SR취소 재시도)");
			}
		}
	}
}

function cmdReq_click(){
	if(SrID == "" || SrID == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert($("#cmdReq").text()+"할 SR을 선택하십시오.");
		return;
	}else if($("#txtcomment").val().length == 0){
		dialog.alert("검토의견을 입력하십시오.");
		return;
	}else if($("#txtcomment").val().length >199){
		dialog.alert("검토의견을 200자 이내로 입력하십시오.");
		return;
	}

	confirmDialog.confirm({
		title: '확인',
		msg: '정말로 SR취소하시겠습니까? 취소한 SR은 다시 사용할 수 없습니다.',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			cnclFun();
		}
	});
}

function cnclFun(){

	var ajaxReturnData = null;
	var tmpObj= new Object();
	tmpObj.UserId = userId;
	tmpObj.Srid = SrID;
	tmpObj.txtcomment = $("#txtcomment").val();
	
	var data = {
		tmpObj :	tmpObj,
		requestType : 'updateSRCnclMsg'
	}
	
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
	
	if(ajaxReturnData == "OK"){
		dialog.alert("SR취소를 완료했습니다.");
		window.parent.getPrjList();
	}else{
		dialog.alert(ajaxReturnData);
	}
	
	tmpObj = null;
}
