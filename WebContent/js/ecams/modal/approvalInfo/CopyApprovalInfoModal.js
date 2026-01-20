/**
 * [결재정보 > 결재정보복사] 팝업 기능정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var grdApprovalInfo		= new ax5.ui.grid();

var grdApprovalInfoData 	= [];
var cboFromSysData			= [];
var cboFromReqCdData		= [];
var cboToReqCdData			= [];
var lstSysData				= []; //To시스템 리스트 데이터

var tmpInfo     			= new Object(); 
var tmpInfoData 			= new Object();

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

grdApprovalInfo.setConfig({
    target: $('[data-ax5grid="grdApprovalInfo"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    showLineNumber: false,
    header: {
        align: "center",
        selector: true
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
        {key: "manid", 			label: "구분",  		width: 70,  align: 'center'},
        {key: "cm_seqno",		label: "순서",  		width: 60,  align: 'center'},
        {key: "cm_name", 		label: "단계명칭",  	width: 150, align: 'left'},
        {key: "cm_codename", 	label: "결재구분",  	width: 100, align: 'left'},
        {key: "common", 		label: "정상",  		width: 100, align: 'center'},
        {key: "blank",			label: "부재",  		width: 100, align: 'center'},
        {key: "holi", 			label: "업무후",  	width: 100, align: 'center'},
        {key: "emg", 			label: "긴급",  		width: 100, align: 'center'},
        {key: "emg2", 			label: "긴급[후]",  	width: 100, align: 'center'},
        {key: "deptcd", 		label: "결재조직",  	width: 100, align: 'left'},
        {key: "rgtcd", 			label: "결재직무",  	width: 100, align: 'left'},
        {key: "rsrccd", 		label: "프로그램종류",	width: 100, align: 'left'},
    ]
});

$(document).ready(function() {
	getCodeInfo();
	getSysInfo();
	
	$('#chkMember').wCheck("check", false);
	$('#chkOutsourcing').wCheck("check", false);
	$('#chkAllSys').wCheck("check", false);
	
	// 조회
	$('#btnQry').bind('click', function() {
		btnQry_Click();
	});
	
	// 닫기
	$('#btnClose').bind('click', function() {
		btnClose_Click();
	});
	
	// 복사
	$('#btnCopy').bind('click', function() {
		btnCopy_Click();
	});
	
	// 시스템전체선택
	$('#chkAllSys').bind('click', function() {
		chkAllSys_Click();
	});
	
	// 시스관련없음
	$('#chkNoSys').bind('click', function() {
		chkNoSys_Click();
	});
	
	// 결재정보전체복사
	$('#chkAllCopy').bind('click', function() {
		chkAllCopy_Click();
	});
});

function getCodeInfo(){
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('REQUEST', 'SEL','N')
	]);
	
	cboFromReqCdData = codeInfos.REQUEST;
	cboToReqCdData   = codeInfos.REQUEST;
	
	$('[data-ax5select="cboFromReqCd"]').ax5select({
        options: injectCboDataToArr(cboFromReqCdData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboToReqCd"]').ax5select({
        options: injectCboDataToArr(cboToReqCdData, 'cm_micode' , 'cm_codename')
   	});
}

function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SelMsg : "SEL",
		CloseYn : "N",
		SysCd : "",
		requestType	: 'getSysInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSysInfo);
}


function successSysInfo(data) {
	cboFromSysData = data;
	//cboFromSysData.unshift({cm_syscd: '99999', cm_sysmsg: '시스템관련없음'});
	var tmpItem = {
		cm_syscd: '99999',
		cm_sysmsg: '시스템관련없음'
	}
	cboFromSysData.splice(1,0,tmpItem);
	
	$('[data-ax5select="cboFromSys"]').ax5select({
        options: injectCboDataToArr(cboFromSysData, 'cm_syscd' , 'cm_sysmsg')
   	});
	
	lstSysData = data;
	lstSysData = lstSysData.filter(function(data) {
		if(data.cm_syscd == "00000" || data.cm_syscd == "99999") return false;
		else return true;
	});
	
	$('#lstSys').empty(); //리스트 초기화
	var addId = null;
	var liStr = '';
	
	if(lstSysData == null || lstSysData.length < 0) return;
	
	lstSysData.forEach(function(lstSysData, Index) {
		addId = lstSysData.cm_syscd;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		liStr += '<div class="margin-3-top">';
		liStr += '	<input type="checkbox" class="checkbox-syscd" id="lstSys'+addId+'" data-label="'+lstSysData.cm_sysmsg+'"  value="'+lstSysData.cm_syscd+'" />';
		liStr += '</div>';
		liStr += '</li>';
		
	});
	$('#lstSys').html(liStr);
	
	$('input.checkbox-syscd').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

function btnQry_Click() {
	var strMem = "";
	
	if(!$('#chkMember').is(':checked') && !$('#chkOutsourcing').is(':checked')) {
		dialog.alert('직원/외주구분을 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(getSelectedIndex('cboFromSys') < 0) {
		dialog.alert('복사대상시스템(FROM)을 선택하여 주시기 바랍니다.',function(){});
		$('#cboFromSys').focus();
		return;
	}
	
	if(getSelectedIndex('cboFromReqCd') < 1) {
		dialog.alert('복사할 결재종류(FROM)을 선택하여 주시기 바랍니다.',function(){});
		$('#cboFromReqCd').focus();
		return;
	}
	
	grdApprovalInfoData = [];
	grdApprovalInfo.setData([]);
	
	if($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) {
		strMem = "9";
	}else if($('#chkMember').is(':checked')) {
		strMem = "1";
	}else {
		strMem = "2";
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
		SysCd : getSelectedVal('cboFromSys').value,
		ReqCd : getSelectedVal('cboFromReqCd').value,
		ManId : strMem,
		SeqNo : "",
		requestType	: 'getConfInfo_List'
	}
	ajaxAsync('/webPage/ecmm/Cmm0300Servlet', tmpInfoData, 'json', successApprovalInfo);
}

function successApprovalInfo(data) {
	grdApprovalInfoData = data;
	grdApprovalInfo.setData(grdApprovalInfoData);
}

//시스템전체선택
function chkAllSys_Click() {
	var addId = null;
	
	lstSysData.forEach(function(item, index) {
		addId = item.cm_syscd;
		if($('#chkAllSys').is(':checked')) {
			$('#lstSys'+addId).wCheck('check', true);
		} else {
			$('#lstSys'+addId).wCheck('check', false);
		}
	});
}

// 시스관련없음
function chkNoSys_Click() {
	if($('#chkNoSys').is(':checked')) {
		$('#chkAllSys').wCheck("disabled", true);
		$('#chkAllSys').wCheck('check', false);
		
		chkAllSys_Click();
		
		//lstSys.enabled = false;
		document.getElementById("lstSysDiv").style.backgroundColor = "#E2E2E2";
		var addId = null;
		lstSysData.forEach(function(item, index) {
			addId = item.cm_syscd;
			$('#lstSys'+addId).wCheck('disabled', true);
		});
	}else {
		$('#chkAllSys').wCheck("disabled", false);
		
		//lstSys.enabled = true;
		document.getElementById("lstSysDiv").style.backgroundColor = "#FFFFFF";
		var addId = null;
		lstSysData.forEach(function(item, index) {
			addId = item.cm_syscd;
			$('#lstSys'+addId).wCheck('disabled', false);
		});
	}
}

// 결재정보전체복사
function chkAllCopy_Click() {
	if($('#chkAllCopy').is(':checked')) {
		$('[data-ax5select="cboFromReqCd"]').ax5select("disable");
		$('[data-ax5select="cboFromReqCd"]').ax5select('setValue',cboFromReqCdData[0].value,true); //value값으로
		$('[data-ax5select="cboToReqCd"]').ax5select("disable");
		$('[data-ax5select="cboToReqCd"]').ax5select('setValue',cboToReqCdData[0].value,true); //value값으로
		$('#chkNoSys').wCheck("disabled", true);
		$('#chkNoSys').wCheck('check', false);
	}else {
		$('[data-ax5select="cboFromReqCd"]').ax5select("enable");
		$('[data-ax5select="cboToReqCd"]').ax5select("enable");
		$('#chkNoSys').wCheck("disabled", false);
	}
	
	chkNoSys_Click();
}

function btnCopy_Click() {
	var tmpSys = "";
	var tmpMem = "";
	var tmpReq = "";
	
	if(getSelectedIndex('cboFromSys') < 1) {
		dialog.alert('복사대상시스템(FROM)을 선택하여 주시기 바랍니다.',function(){});
		$('#cboFromSys').focus();
		return;
	}
	
	if(!$('#chkNoSys').is(':checked')) {
		for(var i=0; i<lstSysData.length; i++) {
			if($('#lstSys'+lstSysData[i].cm_syscd).is(':checked')) break;
		}
		
		if(i >= lstSysData.length) {
			dialog.alert('복사대상시스템 (TO)을 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	
	if($('#chkAllCopy').is(':checked')) {
		confirmDialog.confirm({
			msg: '[' + getSelectedVal('cboFromSys').cm_sysmsg + ']에 등록된 결재정보전체를 복사하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				AllCopy();
			}
		});
	}else {
		var checkedGridItem = grdApprovalInfo.getList("selected");

		if(grdApprovalInfoData.length == 0 || checkedGridItem.length <= 0) {
			dialog.alert('복사할 결재단계를 선택하여 주시기 바랍니다.',function(){});
			return;
		}
		
		if(getSelectedIndex('cboFromReqCd') < 1) {
			dialog.alert('복사할 결재종류[From]을 선택하여 주시기 바랍니다.',function(){});
			$('#cboFromReqCd').focus();
			return;
		}
		
		if(getSelectedIndex('cboToReqCd') < 1) {
			dialog.alert('복사할 결재종류[To]을 선택하여 주시기 바랍니다.',function(){});
			$('#cboToReqCd').focus();
			return;
		}
		
		tmpSys = "";
		tmpReq = "";
		
		if(!$('#chkNoSys').is(':checked')) {
			for(var i=0; i<lstSysData.length; i++) {
				if($('#lstSys' + lstSysData[i].cm_syscd).is(':checked') &&
					(getSelectedVal('cboFromReqCd').cm_micode != getSelectedVal('cboToReqCd').cm_micode || 
					getSelectedVal('cboFromSys').cm_syscd != lstSysData[i].cm_syscd)) {

					if(tmpSys.length > 0) tmpSys = tmpSys + ",";
					tmpSys = tmpSys + lstSysData[i].cm_syscd;
				}
			}
		}else {
			tmpSys = "99999";
		}
		
		if(tmpSys.length == 0) {
			dialog.alert('복사시스템[TO], 결재종류[TO]을 정확히  선택하여 주시기 바랍니다.',function(){});
			$('#cboToReqCd').focus();
			return;
		}
		
		if($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) {
			tmpMem = "9";
		}else if($('#chkMember').is(':checked')) {
			tmpMem = "1";
		}else {
			tmpMem = "2";
		}
		
		tmpInfo = new Object();
		tmpInfo.cm_syscd = getSelectedVal('cboFromSys').value;
		tmpInfo.cm_reqcd = getSelectedVal('cboFromReqCd').value;
		tmpInfo.cm_toreq = getSelectedVal('cboToReqCd').value;
		tmpInfo.copysys = tmpSys;
		tmpInfo.cm_manid = tmpMem;
		tmpInfo.userid = userId;
		
		tmpInfoData = new Object();
		tmpInfoData = {
			etcData		: tmpInfo,
			confList	: checkedGridItem,
			requestType	: 'confCopy'
		}
		ajaxAsync('/webPage/ecmm/Cmm0300_CopyServlet', tmpInfoData, 'json', successCopyApprovalInfo);
	}
}

//결재정보전체복사
function AllCopy() {
	var tmpSys = "";
	var tmpMem = "";
	var i = 0;
	
	var checkedGridItem = [];

	if(!$('#chkNoSys').is(':checked')) {
	      for(var i=0; i<lstSysData.length; i++) {
	         if($('#lstSys' + lstSysData[i].cm_syscd).is(':checked') &&
	            (getSelectedVal('cboFromReqCd').cm_micode != getSelectedVal('cboToReqCd').cm_micode || 
	            getSelectedVal('cboFromSys').cm_syscd != lstSysData[i].cm_syscd)) {

	            if(tmpSys.length > 0) tmpSys = tmpSys + ","+ lstSysData[i].cm_syscd;
	            else tmpSys = tmpSys + lstSysData[i].cm_syscd;
	         }
	      }
	   }
	
	if(tmpSys.length == 0) {
		dialog.alert('복사시스템[TO]을 정확히  선택하여 주시기 바랍니다.',function(){});
		$('#cboToSys').focus();
		return;
	}
	
	if($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) {
		tmpMem = "9";
	}else if($('#chkMember').is(':checked')) {
		tmpMem = "1";
	}else {
		tmpMem = "2";
	}
	
	tmpInfo = new Object();
	tmpInfo.cm_syscd = getSelectedVal('cboFromSys').value;
	tmpInfo.cm_reqcd = "ALL";
	tmpInfo.cm_toreq = "ALL";
	tmpInfo.copysys = tmpSys;
	tmpInfo.cm_manid = tmpMem;
	tmpInfo.userid = userId;
	
	tmpInfoData = new Object();
	tmpInfoData = {
		etcData		: tmpInfo,
		confList	: checkedGridItem,
		requestType	: 'confCopy'
	}
	
	console.log(tmpInfoData);
	ajaxAsync('/webPage/ecmm/Cmm0300_CopyServlet', tmpInfoData, 'json', successCopyApprovalInfo);
}

function successCopyApprovalInfo(data) {
	dialog.alert('결재정보 복사처리를 완료하였습니다.',function(){});
}

function btnClose_Click() {
	window.parent.copyApprovalInfoModal.close();
}