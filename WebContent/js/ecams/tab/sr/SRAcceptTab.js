/** SRRegisterTab 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이해성
 * 	버전 : 1.1
 *  수정일 : 2021-04-14
 */

var userName = window.top.userName;
var userId = window.top.userId;
var adminYN = window.top.adminYN;
var userDeptName = window.top.userDeptName;
var userDeptCd = window.top.userDeptCd;
var strReqCd = window.top.reqCd;
var codeList    = window.top.codeList;          //전체 코드 리스트

var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var cboCateType_dp = [];
var cboDetCate_dp = [];
var cboJobRank_dp = [];
var cboPayYn_dp = [];
var cboHandler_dp = [];
var base_dp = [];
var treeDept_dp = [];

var treeObj			= null;
var treeObjData		= [];
var selectedNode	= null;
var insertNode		= null;
var disabledNode    = null;

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	check: {
		enable: true
	}
	
};

$('[data-ax5select="cboCateType"]').ax5select({
	options : []
});

$('[data-ax5select="cboDetCate"]').ax5select({
	options : []
});

$('[data-ax5select="cboJobRank"]').ax5select({
	options : []
});

$('[data-ax5select="cboHandler"]').ax5select({
	options : []
});

$('[data-ax5select="cboPayYn"]').ax5select({
	options : []
});

$(document).ready(function(){
	//radiobutton1
	$('#rdo1').bind('click', function() {
		rdo_Click(1);
	});

	//radiobutton2
	$('#rdo2').bind('click', function() {
		rdo_Click(2);
	});
	
	//radiobutton3
	$('#rdo3').bind('click', function() {
		rdo_Click(3);
	});

	//반려
	$('#btnCncl').bind('click', function() {
		cncl_Click();
	});	

	//접수
	$('#btnReq').bind('click', function() {
		req_Click();
	});	
	
	getCodeInfo();
	getTeamInfoTree();
});

function screenInit(scrGbn) {
	if(scrGbn == 'M') {
		cboHandler_dp = [];
	}
	$('#hbxWeb').attr("disabled", true);
	$('#rdo1').prop('checked', true);
	$('#rdo2').attr("disabled", true);
	$('#rdo3').attr("disabled", true);
	rdo_Click(0);
	$('[data-ax5select="cboCateType"]').ax5select('disable');
	$('[data-ax5select="cboDetCate"]').ax5select('disable');
	$('[data-ax5select="cboJobRank"]').ax5select('disable');
	$('[data-ax5select="cboHandler"]').ax5select('disable');
	$('#txtHandler').attr("disabled", true);
	$('#txtCnclSayu').attr("readonly", true);
	$('#btnReq').attr("disabled", true);
	$('#btnCncl').attr("disabled", true);
	$('#divTree').css('pointer-events', 'none');
	$('#txtCnclSayu').val("");	
	if (cboCateType_dp.length > 0) {
		$('[data-ax5select="cboCateType"]').ax5select("setValue", "****", true);	
	}
	if (cboDetCate_dp.length > 0) {
		$('[data-ax5select="cboDetCate"]').ax5select("setValue", "****", true);
	}
	if (cboJobRank_dp.length > 0) {
		$('[data-ax5select="cboJobRank"]').ax5select("setValue", "****", true);
	}
	if(treeObj) {
		treeObj.checkAllNodes(false);
	}
}

function rdo_Click(index) {
	if (strIsrId == null || strIsrId == "") {
		return;
	}
	$('#divTree').css('pointer-events', 'none');

	if ($('#rdo1').prop('checked') == false){
		$('#divTree').css('pointer-events', 'visible');
		$('[data-ax5select="cboCateType"]').ax5select("disable");
		$('[data-ax5select="cboDetCate"]').ax5select("disable");
		$('[data-ax5select="cboJobRank"]').ax5select("disable");
		$('[data-ax5select="cboHandler"]').ax5select("disable");
		$('#txtHandler').attr("disabled", true);
		$('#txtCnclSayu').attr("readonly", true);
	
		treeObj.checkAllNodes(false);
		
		if ($('#rdo2').prop('checked') == true) { 
			$('#btnReq').text("이관");
		} else {
			$('#btnReq').text("추가");
		}
	} else {
		$('[data-ax5select="cboCateType"]').ax5select("enable");
		$('[data-ax5select="cboDetCate"]').ax5select("enable");
		$('[data-ax5select="cboJobRank"]').ax5select("enable");
		
		if (base_dp[0].usersw == "Y") {
			$('[data-ax5select="cboHandler"]').ax5select("enable");
			$('#txtHandler').attr("disabled", false);
			$('#txtCnclSayu').attr("readonly", false);
			
		}
		treeObj.checkAllNodes(false);
		$('#divTree').css('pointer-events', 'none');

		if (base_dp.length > 0) {
			if (base_dp[0].cc_substatus == "11") {
				$('#btnReq').text("접수");
			} else {
				$('#btnReq').text("수정");
			} 
		} else {
			$('#btnReq').text("접수");
		}
	}
	//treeDept.selectable = treeDept.enabled;
}

//코드 정보 조회
function getCodeInfo() {
	cboCateType_dp = fixCodeList(codeList['CATETYPE'],'SEL','','','N');
	cboDetCate_dp = fixCodeList(codeList['DETCATE'],'SEL','','','N');
	cboJobRank_dp = fixCodeList(codeList['JOBRANK'],'SEL','','','N');
	codeList = null;

	$('[data-ax5select="cboCateType"]').ax5select({
        options: injectCboDataToArr(cboCateType_dp, 'cm_micode' , 'cm_codename')
   	});	
	$('[data-ax5select="cboDetCate"]').ax5select({
        options: injectCboDataToArr(cboDetCate_dp, 'cm_micode' , 'cm_codename')
   	});	
	$('[data-ax5select="cboJobRank"]').ax5select({
        options: injectCboDataToArr(cboJobRank_dp, 'cm_micode' , 'cm_codename')
   	});
	   
	var tmpObj = new Object();
	tmpObj.cm_micode = "00";
	tmpObj.cm_codename = "Y";
	cboPayYn_dp.push(tmpObj);

	tmpObj = {};
	tmpObj.cm_micode = "01";
	tmpObj.cm_codename = "N";
	cboPayYn_dp.push(tmpObj);

	$('[data-ax5select="cboPayYn"]').ax5select({
		options : injectCboDataToArr(cboPayYn_dp, 'cm_micode', 'cm_codename')
	});
	$('[data-ax5select="cboPayYn"]').ax5select("setValue", "00", true);	   
}

//부서 정보 조회
function getTeamInfoTree() {
	var data = new Object();
	data = {
			treeInfoData: false,
			requestType	: 'getTeamInfoTree'
		}
		ajaxAsync('/webPage/common/CommonTeamInfo', data, 'json',
				successgetTeamInfoTree);
}

function successgetTeamInfoTree(data) {
	for(i = 0; i < data.length; i++) {
		if(data[i].name == "IT본부" || data[i].pId == "F10000" || data[i].pId == "F1A100" || data[i].pId == "F1B100"){
			treeObjData.push(data[i]);
		}    
	}

	$.fn.zTree.init($("#treeDept"), setting, treeObjData);
	treeObj = $.fn.zTree.getZTreeObj("treeDept");
	treeObj.expandAll(true);
}

function isrInfoCall() {
	var grdObj = new Object();
	grdObj.IsrId = strIsrId;
	grdObj.IsrSub = strIsrSub;
	grdObj.UserId = strUserId
	grdObj.ReqCd = strReqCd;

	var grdInfoData = new Object();
	grdInfoData.grdData = grdObj;
	grdInfoData.requestType = "getIsrInfo"

	ajaxAsync('/webPage/sr/SRAccept', grdInfoData, 'json', successGetIsrInfo);
}

function successGetIsrInfo(data) {
	base_dp = data;

	if(base_dp.length == 0) {
		return;
	}

	$('#txtSta').val(base_dp[0].sta);
	$('#txtEditor').val(base_dp[0].cm_username);
	$('#txtCreatdt').val(base_dp[0].recvdate);
	$('#txtHandler').val(base_dp[0].handler);
	$('#txtCnclSayu').val(base_dp[0].cnclsayu);

	if (base_dp[0].cc_catetype != null) {
		$('[data-ax5select="cboCateType"]').ax5select("setValue", base_dp[0].cc_catetype, true);
	}
	if (base_dp[0].cc_detcate != null) {
		$('[data-ax5select="cboDetCate"]').ax5select("setValue", base_dp[0].cc_detcate, true);
	}
	if (base_dp[0].cc_jobrank != null) {
		$('[data-ax5select="cboJobRank"]').ax5select("setValue", base_dp[0].cc_jobrank, true);
	}	
	//20150630 neo. 결제관련 값 셋
	if (base_dp[0].cc_payyn != null) {
		var payyn = "";
		if(base_dp[0].cc_payyn === 'Y'){
			payyn = '00';
		} else if(base_dp[0].cc_payyn === 'N'){
			payyn = '01';
		}
		$('[data-ax5select="cboPayYn"]').ax5select("setValue", payyn, true);
	}	
	//20150630 neo. 요청기관 값 셋
	$('#txtReqOrg').val(base_dp[0].reqorg);
	$('#txtCnclYn').val(base_dp[0].cnclyn);

	if (base_dp[0].recvgbn != "" && base_dp[0].recvgbn != null) {  
		if (base_dp[0].recvgbn == "3") {
			$('#rdo2').prop('checked', true);
			$('#rdo2').attr("disabled", false);
			$('#rdo1').attr("disabled", true);
		} 
	}	
	
	if (base_dp[0].secusw == "Y") {
		$('#rdo3').attr("disabled", false);
		$('[data-ax5select="cboCateType"]').ax5select("enable");
		$('[data-ax5select="cboDetCate"]').ax5select("enable");
		$('[data-ax5select="cboJobRank"]').ax5select("enable");
		$('#btnReq').attr("disabled", false);

		if (base_dp[0].cc_substatus == "11") {
			$('#btnReq').text('접수');
			$('#rdo2').attr("disabled", false);
			$('#btnCncl').attr("disabled", false);
		} else { 
			$('#btnReq').text('수정');
		}

		if (base_dp[0].usersw == "Y") {
			$('#txtHandler').attr("disabled", false);
			$('[data-ax5select="cboHandler"]').ax5select('enable');
			$('#txtCnclSayu').attr("readonly", false);

			var data = new Object();
			data = {
					userId : strUserId,
					reqCd : strReqCd,
					requestType	: 'getHandlerList'
				}
				ajaxAsync('/webPage/sr/SRAccept', data, 'json',
						successGetHandlerList);			
		}
		$('#hbxWeb').attr("readonly", false);
	}	
}
//처리담당자 콤보박스 세팅
function successGetHandlerList(data) {
	cboHandler_dp = data;

	$('[data-ax5select="cboHandler"]').ax5select({
		options : injectCboDataToArr(cboHandler_dp, 'cm_userid', 'userid')
	});

	if (base_dp[0].cc_handler != null) {
		for (var i=0 ; cboHandler_dp.length > i; i++) {
			if (cboHandler_dp[i].cm_userid == base_dp[0].cc_handler) {
				$('[data-ax5select="cboHandler"]').ax5select("setValue", cboHandler_dp[i].cm_userid, true);
				cboHandler_change();
				break;
			}
		}
	}
}

function cboHandler_change() {
	$('#txtHandler').val("");
	if(getSelectedIndex('cboHandler') > 0) {
		$('#txtHandler').val(getSelectedVal('cboHandler').cm_username);
	}
}

$('#cboHandler').bind('change', function() {
	if(getSelectedIndex('cboHandler') == 0) {
		$('#txtHandler').val("");
	} else {
		$('#txtHandler').val(getSelectedVal('cboHandler').cm_username);
	}
})

function buttonFindClick() {
	if(treeObjData.length == 0) {
		return;
	}
}
//반려 버튼 클릭
function cncl_Click() {
	if ($('#txtCnclSayu').val().length == 0){
		dialog.alert("반려사유를 입력하여 주십시오.");
		$('#txtCnclSayu').focus();
		return;
	}
	
	dialog.alert({
		msg: "반려처리 하시겠습니까?",
		onStateChanged: function() {
			if(this.state === "close") {
				cnclChk(this.state);					
			}
		}
	})	
}

function cnclChk(state) {
	if(state == 'close') {
		var etcObj = new Object();
				
		etcObj.isrid = strIsrId;
		etcObj.isrsub = strIsrSub;
		etcObj.cnclSayu = $('#txtCnclSayu').val();
		etcObj.userid = strUserId;
		$('#btnReq').attr('disabled', true);
		$('#btnCncl').attr('disabled', true);
		
		//Cmc0200.cnclIsr(etcObj);
		var data = {
			cnclChkData: etcObj,
			requestType	: 'cnclIsr'
		}
		ajaxAsync('/webPage/sr/SRAccept', data, 'json',
				successCnclIsr);		
		
		etcObj = null;
	}
}

function successCnclIsr(data) {
	dialog.alert("반려처리를 완료하였습니다.");
	screenInit("M");
	isrInfoCall();
}

//접수 버튼 클릭
function req_Click() {
	var etcObj= new Object();
	var strPart = "";
	var nodeList = new Object();
	
	if ($('#rdo1').prop('checked')) {	     		
		if(getSelectedIndex('cboCateType') == 0){
			dialog.alert("분류유형을 선택하여 주십시오.");
			return;
		}
		if (getSelectedIndex('cboDetCate') == 0){
			dialog.alert("상세분류를 선택하여 주십시오.");
			return;
		}
		if (getSelectedIndex('cboJobRank') == 0){
			dialog.alert("작업순위를 선택하여 주십시오.");
			return;
		}
		if (base_dp[0].usersw == "Y") {
			if (getSelectedIndex('cboHandler') == 0){
				dialog.alert("처리담당자를 선택하여 주십시오.");
				return;
			}
		}
	}else if ($('#rdo2').prop('checked') == true || $('#rdo3').prop('checked') == true){
		if (treeObjData.length == 0){
			dialog.alert("부서 정보가 없습니다.");
			return;
		}
	
		nodeList = treeObj.getCheckedNodes(true);
		for(var i = 0; nodeList.length > i; i++) {
			if(nodeList[i].check_Child_State == -1) {
				if(nodeList[i].pId == base_dp[0].cc_recvpart) {
					treeObj.checkNode(nodeList[i], false, true);
				} else {
					if (strPart.length > 0) { 
						strPart = strPart + ",";
					}
					strPart = strPart + nodeList[i].pId;
				}
			}
		}
		nodeList = null;

		if(strPart.length == 0){
			dialog.alert("추가된 부서가 없습니다.");
			return;
		}
	}
	
	etcObj.isrid = strIsrId;
	etcObj.isrsub = strIsrSub;
	if ($('#rdo1').prop('checked')){
		etcObj.CC_RECVGBN = "9";//9:접수
		etcObj.CC_CATETYPE = getSelectedVal('cboCateType').cm_micode;
		etcObj.CC_DETCATE = getSelectedVal('cboDetCate').cm_micode;
		etcObj.CC_JOBRANK = getSelectedVal('cboJobRank').cm_micode;
		etcObj.recvuser = strUserId;
		if (base_dp[0].usersw == "Y") {
			etcObj.handler = getSelectedVal('cboHandler').cm_userid;
		} else {
			etcObj.handler = base_dp[0].cc_handler;
		}
	}else if ($('#rdo2').prop('checked')){
		etcObj.recvuser = strUserId;
		etcObj.CC_RECVGBN = "3";//3:이관
		etcObj.addpart = strPart;
	}else {
		etcObj.CC_RECVGBN = "0";//0:미접수
		etcObj.addpart = strPart;
	}      
	 
	etcObj.userid = strUserId;
	etcObj.CC_PAYYN = getSelectedVal('cboPayYn').cm_codename;
	$('#btnReq').attr('disabled', true);
	$('#btnCncl').attr('disabled', true);

	//Cmc0200.setISRSUBInfo(etcObj);
	var data = {
		isrSubInfoData : etcObj,
		requestType	: 'setISRSUBInfo'
	}
	ajaxAsync('/webPage/sr/SRAccept', data, 'json',
			successSetISRSUBInfo);	
	
	etcObj = null;	
}

function successSetISRSUBInfo(data) {
	$('#btnReq').attr('disabled', false);

	if (data == "NO1"){
		dialog.alert("[테스트있음] -> [테스트없음] 변경\n\n\n테스트서버가 존재하는 시스템에서\n체크아웃 또는 신규등록한 프로그램이 있습니다\n체크아웃 취소 또는 신규등록 삭제 후\n[테스트없음] 으로 수정가능합니다.");
		return;
	}
	
	if (data == "NO2"){
		dialog.alert("[테스트없음] -> [테스트있음] 변경\n\n\n테스트서버가 존재하지 않는 시스템에서\n체크아웃 또는 신규등록한 프로그램이 있습니다\n체크아웃 취소 또는 신규등록 삭제 후\n[테스트있음] 으로 수정가능합니다.");
		return;
	}
	
	if (data == "NO3"){
		dialog.alert("[요구관리만으로 변경]\n\n\n해당 ISR로 체크아웃 또는 신규등록한 프로그램이 있습니다\n체크아웃 취소 또는 신규등록 삭제 후\n[요구관리만] 으로 수정가능합니다.");
		return;
	}
	
	if (data != "0"){
		dialog.alert($('#btnReq').text() + "을 실패하였습니다.\n" + data );
		return;
	}
	dialog.alert($('#btnReq').text() + "을 완료하였습니다.");
	screenInit("M");
	isrInfoCall();
}