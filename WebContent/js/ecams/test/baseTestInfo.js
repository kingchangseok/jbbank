/** baseISRInfo 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-04-14
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.parent.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var strAcptNo 		= window.parent.strAcptNo;
var strStatus		= window.parent.strStatus; //SR상태 "2";
var strIsrId		= window.parent.strIsrId; //"R201906-0003";  

var ISRInfo_dp = [];

$(document).ready(function() {
	//getBaseISRInfo();
});

function getBaseISRInfo() {
	var data = {
		isrid : window.parent.ISRID,
		isrsub : window.parent.ISRSUB,
		userid : window.parent.UserId,
		requestType : 'getBaseISRInfo'
	}
	ajaxAsync('', data, 'json',
			successgetBaseISRInfo);
}

function getBaseISRInfo(data) {
	ISRInfo_dp = data;

	$('#txtISRId').val(ISRInfo_dp.txtISRId);
	$('#txtReqDT').val(ISRInfo_dp.txtReqDT);
	$('#txtStatus').val(ISRInfo_dp.txtStatus);
	$('#txtTitle').val(ISRInfo_dp.txtTitle);
	$('#txtReqDept').val(ISRInfo_dp.txtReqDept);
	$('#txtProStatus').val(ISRInfo_dp.txtProStatus);
	$('#txtEditor').val(ISRInfo_dp.txtEditor);
}