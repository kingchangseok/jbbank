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

var upFiles = [];
var upFiles2 = [];

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

	$("#cmdResultTest").bind("click",function(){
		$("#doc").click();
	});
	
	$("#doc").bind("change",function(){
		if($("#doc").get(0).files && $("#doc").get(0).files[0]){
			var fileObj = new Object();
			fileObj = {
				srid : SrID,
				filegb : "21",
				editor : userId,
				testergbn : ""
			}
			window.parent.docList = grdDoc.getList();
			window.parent.upFiles = $("#doc").get(0); 
			window.parent.fileObj = fileObj; 
			window.parent.fileupload();
		}
	});
	
});

function setSRInfo_detail_func() {
	if(SrID == "" || SrID == null) return;
	
	$("#txtsrid").val(SrID);
	$("#txttitle").val(SrTitle);
	$("#txtcomment").val(Endmsg);
	
	if(strReqCD == "67" && (SRStatus == "45" || SRStatus == "10")){
		$("#txtcomment").prop("disabled",false);
		$("#cmdReq").prop("disabled",false);
	}else{
		$("#txtcomment").prop("disabled",true);
		$("#cmdReq").prop("disabled",true);
	}
	
}

function getSRInfo(){
	var ajaxReturnData = null;
	var prjData = new Object();
	prjData.deptCd = "ALL";
	prjData.gbnCd = "ALL";
	prjData.strDate = "";
	prjData.endDate = "";
	prjData.selCd = "SEL";
	prjData.selTxt = "";
	prjData.srId = SrID;
	
	prjData.userId 	= userId;
	prjData.reqCd 	= strReqCD;
	
	var prjInfo = new Object();
	prjInfo.prjInfoData =	prjData;
	prjInfo.requestType = 'getSRInfo';
	
	ajaxReturnData = ajaxCallWithJson('/webPage/common/ComPrjInfo', prjInfo, 'json');

	if(ajaxReturnData.length>0){
		SrTitle = ajaxReturnData[0].cc_title;
		SRStatus = ajaxReturnData[0].cc_status;
		Endmsg = ajaxReturnData[0].cc_procmsg;
		setSRInfo_detail_func();
	}
}

function cmdReq_click(){
	if(SrID == "" || SrID == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert($("#cmdReq").text()+"할 SR을 선택하십시오.");
		return;
	}else if($("#txtcomment").val().trim().length == 0){
		dialog.alert("완료의견을 입력하십시오.");
		return;
	}else if($("#txtcomment").val().length >199){
		dialog.alert("완료의견을 2000자 이내로 입력하십시오.");
		return;
	}

	confirmDialog.confirm({
		title: '확인',
		msg: '정말로 SR완료하시겠습니까?',
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
		tmpObj.UserId = UserId;
		tmpObj.Srid = SrID;
		tmpObj.txtcomment = $("#txtcomment").val();
		
		var data = new Objcet();
		data = {
			tmpObj :	tmpObj,
			requestType : 'updateSREndMsg'
		}
		
		ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
		
		if(ajaxReturnData == "OK"){
			dialog.alert("SR이 처리완료되었습니다.");
			window.parent.getPrjList();
		}else{
			dialog.alert(ajaxReturnData);
		}
		
		tmpObj = null;
}
