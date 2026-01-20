var userId 		= window.top.userId;
var reqCd		= window.top.reqCd;

var mainGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();

var cboApproDeData = [];
var mainGridData = [];
var selectedItem = null;

$("[data-ax5select]").ax5select({
	options : []
});

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	sortable : true,
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            selectedItem = this.item;
            console.log(selectedItem);
        },
	},
	columns : [
		{key : "isrid",label : "ISR ID",align : "center", width: "10%"}, 
		{key : "isrsub",label : "SUB#",align : "center", width: "5%"}, 
		{key : "isrtitle",label : "요청제목",align : "center", width: "25%"}, 
		{key : "reqdate",label : "요청등록일",align : "center", width: "7%"}, 
		{key : "reqenddt",label : "완료요청일",align : "center", width: "7%"}, 
		{key : "requser",label : "요청인",align : "center", width: "5%"}, 
		{key : "reqdept",label : "요청부서",align : "center", width: "7%"}, 
		{key : "recvpart",label : "접수파트",align : "center", width: "7%"}, 
		{key : "statusName",label : "ISR진행상태",align : "center", width: "7%"}, 
		{key : "subStatus",label : "ISR진행상태코드",align : "center", width: "8%"}, 
		{key : "reqsta",label : "진행현황",align : "center", width: "10%"}
	]
});

$(document).ready(function() {
	picker.bind(defaultPickerInfo('basic', 'top'));
	getTeamInfo();
	
	$("#datStD, #datEdD").val(setDateFormat(getDate('DATE', 0)));
	
	$("#btnSearch").bind('click', function() {
		get_ISRList();
	});
	
	$("#btnSession").bind('click', function() {
		endISRCheck();
	});
})

// 부서 정보 가져오기
function getTeamInfo() {
	var data = new Object();
	data = {
		selMsg 		: 'ALL',
		cm_useyn 	: 'Y',
		gubun 		: 'sub',
		itYn 		: 'N',
		requestType	: 'getTeamInfo'
	}
	ajaxAsync('/webPage/common/CommonTeamInfo', data, 'json',successGetTeamInfo);
}

// 부서 정보 가져오기 완료
function successGetTeamInfo(data) {
	cboApproDeData = data;
	$('[data-ax5select="cboTeam"]').ax5select({
		options: injectCboDataToArr(cboApproDeData, 'cm_deptcd' , 'cm_deptname')
	});
}

function get_ISRList() {
	var data = new Object();
	var etcData = new Object();
	etcData.userid = userId;
	etcData.reqcd = reqCd;  
	etcData.secuyn = "Y";
	etcData.isrid = $("#txtSr").val();
	etcData.stday = setDateFormat($("#datStD").val());
	etcData.edday = setDateFormat($("#datEdD").val()); 
	if ( getSelectedIndex('cboTeam') > 0) etcData.reqdept = getSelectedVal('cboTeam').cm_deptcd;
	data = {
		etcData : etcData,
		requestType : 'get_ISRList'
	}
	
	ajaxAsync('/webPage/administrator/SRCompleteByForce', data, 'json',successGet_ISRList);
}

function successGet_ISRList(data) {
	mainGridData = data;
	mainGrid.setData(mainGridData);
}

function endISRCheck() {
	var isrData = {};
	isrData.isrId = selectedItem.isrid;
	isrData.isrSub = selectedItem.isrsub;
	
	data = {
		etcData : isrData,
		requestType : 'check_ISR'
	}
	ajaxAsync('/webPage/administrator/SRCompleteByForce', data, 'json',successEndISRCheck);
}

function successEndISRCheck(data) {
	console.log(data);
	
	rtnLst_dp = data;
	if (rtnLst_dp.length==0){
		dialog.alert("ISR정보 찾을 수 없음. 다시 조회해서 시도해주세요.");
		return;
	} 
	endISRCode = rtnLst_dp[0].CC_SUBSTATUS; //상태코드
	endISRName = rtnLst_dp[0].CM_CODENAME;  //상태명
	
	if ( endISRCode == '11' || endISRCode == '14' || endISRCode == '18' || endISRCode == '24' ||
		 endISRCode == '25' || endISRCode == '26' || endISRCode == '27' || endISRCode == '28' ||
		 endISRCode == '31' || endISRCode == '32' || endISRCode == '34' || endISRCode == '35' ||
		 endISRCode == '36' || endISRCode == '39' || endISRCode == '41' || endISRCode == '49') {
		 	
		dialog.alert("["+endISRName+"]상태의 ISR은 강제종료할 수 없습니다. 결재 승인이나 반려 후, 프로그램에 영향이 없을 때 진행이 가능합니다.");
		return;
	}
	var etcData = {};
	etcData.userid = userId;
	etcData.reqcd = reqCd;
	etcData.secuyn = "Y";
	etcData.isrid = selectedItem.isrid;
	etcData.isrsub = selectedItem.isrsub;
	
	data = {
		etcData : etcData,
		requestType : 'endISR'
	}
	ajaxAsync('/webPage/administrator/SRCompleteByForce', data, 'json',successEndISR);
}

function successEndISR(data) {
	
	if ( data == "OK"){
		dialog.alert("ISR["+selectedItem.isrid+"]이(가) 강제종료 되었습니다.");
	} else if ( data.substr(0,2) == "OK" &&  data.length > 2 ){
		dialog.alert("ISR["+selectedItem.isrid+"] SUB["+selectedItem.isrsub+"]이(가) 강제종료 되었습니다.\r\n남아 있는 SUB SR항목을 확인하시기 바랍니다.");
	} else {
		dialog.alert(data);
	}
	get_ISRList();
	
}