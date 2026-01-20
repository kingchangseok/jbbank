/**
 * [결재정보 > 전체조회] 팝업 기능정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-25
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var grdAllApprovalInfo		= new ax5.ui.grid();

var grdAllApprovalInfoData 	= [];
var cboSysData				= [];
var cboReqCdData			= [];

var tmpInfo     			= new Object(); 
var tmpInfoData 			= new Object();

var insw = false;

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

grdAllApprovalInfo.setConfig({
    target: $('[data-ax5grid="grdAllApprovalInfo"]'),
    sortable: false, 
    multiSort: false,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "reqname", 		label: "결재유형",  		width: 120, align: 'left'},
        {key: "cm_sysmsg",		label: "시스템",  		width: 140, align: 'left'},
        {key: "manid", 			label: "직원구분",  		width: 80, 	align: 'center'},
        {key: "cm_seqno", 		label: "순서",  			width: 60, 	align: 'center'},
        {key: "cm_name", 		label: "단계명칭",  		width: 150, align: 'left'},
        {key: "cm_codename",	label: "결재구분",  		width: 100, align: 'center'},
        {key: "common", 		label: "정상",  			width: 100, align: 'center'},
        {key: "blank", 			label: "부재",  			width: 100, align: 'center'},
        {key: "holi", 			label: "업무후",  		width: 100, align: 'center'},
        {key: "emg", 			label: "긴급",  			width: 100, align: 'center'},
        {key: "emg2", 			label: "긴급[후]",  		width: 100, align: 'center'},
        {key: "deptcd", 		label: "결재조직",  		width: 100, align: 'left'},
        {key: "rgtcd", 			label: "결재직무",  		width: 100, align: 'left'},
        {key: "rsrccd", 		label: "프로그램종류",		width: 100, align: 'left'},
        {key: "pgmtype", 		label: "프로그램등급", 		width: 100, align: 'left'}
    ]
});

$(document).ready(function() {
	getCodeInfo();
	getSysInfo();
	
	$('#chkMember').wCheck("check", true);
	$('#chkOutsourcing').wCheck("check", false);

	// 엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		grdAllApprovalInfo.exportExcel('결재정보목록.xls');
	});
	// 조회
	$('#btnQry').bind('click', function() {
		btnQry_Click();
	});
	
	// 닫기
	$('#btnClose').bind('click', function() {
		btnClose_Click();
	});
});

function getCodeInfo(){
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('REQUEST', 'ALL','N')
	]);
	
	cboReqCdData = codeInfos.REQUEST;
	
	$('[data-ax5select="cboReqCd"]').ax5select({
        options: injectCboDataToArr(cboReqCdData, 'cm_micode' , 'cm_codename')
   	});
}

function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SelMsg : "ALL",
		CloseYn : "N",
		SysCd : "",
		requestType	: 'getSysInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSysInfo);
}

function successSysInfo(data) {
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
   	});
}

function btnQry_Click() {
	var strSys = "";
	var strReq = "";
	var strMem = "";
	
	grdAllApprovalInfoData 	= [];
	grdAllApprovalInfo.setData([]);
	
	if(!$('#chkMember').is(':checked') && !$('#chkOutsourcing').is(':checked')) {
		dialog.alert('직원/외주구분을 선택하여 주시기 바랍니다.',function(){insw=false;});
		return;
	}
	if (insw) {
		dialog.alert('조회 중 입니다. 잠시만 기다려주시기 바랍니다.',function(){});
		return;
	}
	if(getSelectedIndex('cboSys') > 0) {
		strSys = getSelectedVal('cboSys').value;
	}
	
	if(getSelectedIndex('cboReqCd') > 0) {
		strReq = getSelectedVal('cboReqCd').value;
	}
	
	if($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) {
		strMem = "9";
	}else if($('#chkMember').is(':checked')) {
		strMem = "1";
	}else {
		strMem = "2";
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
		SysCd : strSys,
		ReqCd : strReq,
		ManId : strMem,
		SeqNo : "",
		requestType	: 'getConfInfo_List'
	}

	insw = true;
	$('[data-ax5grid="grdAllApprovalInfo"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0300Servlet', tmpInfoData, 'json', successApprovalInfo);
}

function successApprovalInfo(data) {
	$(".loding-div").remove();
	grdAllApprovalInfoData = data;
	grdAllApprovalInfo.setData(grdAllApprovalInfoData);
	insw = false;
}

function btnClose_Click() {
	window.parent.allApprovalInfoModal.close();
}