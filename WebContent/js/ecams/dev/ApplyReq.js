/** 체크아웃 화면 기능
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */

var userName 	      = window.top.userName;
var userId 			  = window.top.userId;
var adminYN		      = window.top.adminYN;
var userDeptName      = window.top.userDeptName;
var userDeptCd 	      = window.top.userDeptCd;
var reqCd 			  = window.top.reqCd;

var firstGrid		  = new ax5.ui.grid();
var confirmDialog  	  = new ax5.ui.dialog();
var approvalModal 	  = new ax5.ui.modal();
var befJobModal		  = new ax5.ui.modal();
var secuChkModal	  = new ax5.ui.modal();

var firstGridData  = [];
var gridSimpleData = [];
var secondGridData = [];

var confirmData = null;
var options = [];
var sysData = null;
var srData = null;
var strReqCD = "";
var qrySw = false;
var befSw = false;
var plsqlflg = false;
var strApplyDate = "";
var ingSw = false;
var acptNo = "";
var swEmg = false;
var srId = null;
//var confGridHeight = 500;

var chkListData = null;
var winDevRep        = null; //SR정보 새창

var befJobData = [];

firstGrid.setConfig({
    target: $('[data-ax5grid="first-grid"]'),
    sortable: true, 
    multiSort: false,
    showLineNumber: true,
    header: {
        align: 'center'
    },
    body: {
    	onClick: function () {
    		if ( this.colIndex == 6 && (this.item.analchk == '1' || this.item.analchk == '2') ) {
//    			window.open("http://asta.shcap.co.kr:18088/WF/NotiW/view/notiw_sso_login.jsp?user="+userId+"&enc="+this.item.encUserId+"&scnidx=11&srid="+this.item.cc_srid,'analOpen',"scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1680,height=700,left=0,top=0");
//    		    window.open("http://asta.shcap.co.kr:18088/auth/linkLogin?usrid="+userid+"&enc="+encUserid+"&viewidx="+idx+"&srid="+SR,"AnalOpen","scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1680,height=700,left=0,top=0");
    			window.open("http://asta.shcap.co.kr:18088/auth/linkLogin?usrid="+userId+"&enc="+this.item.encUserId+"&viewidx=11&srid="+this.item.cc_srid,'analOpen',"scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1680,height=700,left=0,top=0");
       		}
        },
        trStyleClass: function () {
    		if(this.item.filterData == true){
    			return 'fontStyle-cncl';
    		} else {
    			return '';
    		}
    	},
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "basename", label: "프로그램명",  width: '14%', align: 'left'},
        {key: "jobname", label: "업무명",  width: '8%', align: 'left'},
        {key: "jawon", label: "프로그램종류",  width: '7%', align: 'left'},
        {key: "prcseq", label: "컴파일순서",  width: '6%', align: 'left'},
        {key: "codename", label: "상태",  width: '5%', align: 'left'},
        {key: "sta", label: "신청구분",  width: '5%', align: 'left'},
//        {key: "analmsg", label: "영향분석조치여부",  width: '9%', align: 'left'},
        {key: 'analmsg', label: '영향분석조치여부',  width: '8%',
        	formatter: function() {
        		if (this.item.analchk == '1' || this.item.analchk == '2') {
        			var tmpStr = '<button style="width: 98%; height: 98%; border: 1px solid blue; background-color: rgba(0,0,0,0); color:blue;">' + this.item.analmsg + '</button>';
        			return tmpStr;
        		} else {
    				return this.item.analmsg;
        		}
        	}
        },
        {key: "scrver", label: "버전",  width: '4%', align: 'left'},
        {key: "cm_username", label: "수정자",  width: '5%', align: 'left'},
        {key: "lastdt", label: "수정일",  width: '6%', align: 'left'},
        {key: "cm_dirpath", label: "프로그램경로",  width: '10%', align: 'left'},
    ]
});


$(document).ready(function() {
//	screenInit();
	getSysCbo();
	getCodeInfo();
	getSrIdCbo();
	$("#btnApply").prop("disabled", true);
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
	$("#srInfo").bind('change', function() {
		$("#btnApply").prop("disabled", true);
		changeSrId();
	});
	
	$("#systemSel").bind('change', function() {
		changeSys();
	});
	
	$("#btnSearch").bind('click', function() {
		findProc();
	});
	
	$("#chkBef").bind('click', function() {
		befCheck();
	});
	
	$("#btnApply").bind('click', function() {
		cmdReq();
	});
	$("#btnSrInfo").prop('disabled', true);
	
	$("#btnSrInfo").bind('click', function() {
		cmdReqInfo_Click();
	})
	
	var today = getDate('DATE',0);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
	$("#operationDate").val(today);
});

function screenInit() {
	getSrIdCbo();
//	$('#btnReq').attr('disabled',true);
}

function getCodeInfo() {
	var tmpData = [{
		SelMsg	: "SEL",
		MACODE	: "REQPASS",
		closeYn	: "N"
	}]
	
	var ajaxData = {
		requestType	: "CODE_INFO",
		codeInfoData: tmpData
	}
	
	ajaxAsync('/webPage/common/CommonCodeInfo', ajaxData, 'json',successGetCodeInfo);
}

function successGetCodeInfo(data) {
	codeInfoData = data;
	$('[data-ax5select="processingLevel"]').ax5select({
        options: codeInfoData.REQPASS
	});
	$('[data-ax5select="processingLevel"]').ax5select("setValue", "0");
}

function getSysCbo(){
	var sysInfoData = new Object();
	sysInfoData.SelMsg = 'SEL';
	sysInfoData.UserId = userId;
	sysInfoData.SecuYn = 'y';
	sysInfoData.ReqCd = reqCd;
	sysInfoData.CloseYn = 'n';
	
	var tmpData = {
			requestType : 'getSysInfo',
			sysData : sysInfoData
	}	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpData, 'json',successGetSysCbo);
}

function successGetSysCbo(data){
	sysData = data;
	options = [];
//	console.log(data);
	for(var i=0; i<sysData.length;i++){
		options.push({value: sysData[i].cm_syscd, text: sysData[i].cm_sysmsg, cm_sysgb: sysData[i].cm_sysgb, 
			cm_sysinfo : sysData[i].cm_sysinfo, cm_systype: sysData[i].cm_systype, sysft: sysData[i].sysft});
	};
	
	$('[data-ax5select="systemSel"]').ax5select({
        options: options
	});
	
}

function getSrIdCbo(){
	var ajaxResultData = null;
	var srInfoData = new Object();
	srInfoData.userid = userId;
	srInfoData.secuyn = 'Y';
	srInfoData.reqcd = reqCd;
	srInfoData.qrygbn = '01';
	
	var tmpData = {
		prjInfoData: 		srInfoData,
		requestType: 	'getPrjInfo'
	}
	
	ajaxAsync('/webPage/common/ComPrjInfo', tmpData, 'json', successGetSrIdCbo);
}

function successGetSrIdCbo(data){
	options = [];
	options.push({value:'SR정보 선택 또는 해당없음',text:'SR정보 선택 또는 해당없음'});

	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
		} else {
			srData = data;
			$.each(srData,function(key,value) {
				if(value.setyn === 'Y') selectedSrId = value.cc_srid;
				options.push({value: value.cc_srid, text: value.title, cc_title :value.cc_title, syscd : value.cc_syscd, cc_gbncd : value.cc_gbncd, reqCd : value.ReqCd});
			});
		}
	}
	$('[data-ax5select="srInfo"]').ax5select({
        options: options
	});
}

function changeSrId() {
	if(getSelectedIndex('srInfo') < 1) {
		$("#btnSrInfo").prop('disabled', true);
		return;
	} else {		
		$("#btnSrInfo").prop('disabled', false);
	}
	
	if(sysData.length > 0){
		var sysLength = sysDataFilter();
//		var sysLength = 2;

		var sysSelectIndex = 0;
		if(sysLength == 1) sysSelectIndex = 0;
		else sysSelectIndex = 1;

		var selectVal = $('select[name=systemSel] option:eq('+sysSelectIndex+')').val();
		$('[data-ax5select="systemSel"]').ax5select('setValue',selectVal,true);
		
	}
	
	strReqCD = getSelectedVal('srInfo').reqCd;
	
	$("#chkDBADiv").css({"display" : "none"});
	$("#chkFTDiv").css({"display" : "none"});
	
	if(strReqCD == "08") {
		firstGrid.prop("disabled", true);
		lblcaution.prop("disabled", false);
//		$("#btnApply").prop("disabled", false);
		$("#chkBef").prop("disabled", true);
	} else {
		$("#firstGrid").prop("disabled", false);
//		lblcaution.prop("disabled", false);
//		$("#btnApply").prop("disabled", false);
		$("#chkBef").prop("disabled", false);
		
		if(getSelectedVal('srInfo').cc_gbncd != "14") {
//			console.log("dba");
			$("#chkDBADiv").css({"display" : "inline-block"});
			$("#chkDBA").wCheck('disabled', false);
			$("#chkDBA").prop("data-label", "DBA후결처리(테스트)");
		}
	}
	
	$("#txtSayu").text(getSelectedVal('srInfo').cc_title);
//	$("#btnApply").prop("disabled", false);
	
	srId = getSelectedVal('srInfo').value;
	changeSys();
}

function sysDataFilter(){
	var sysDataLength = sysData.length;
	options = [];
	for(var i=0; i<sysDataLength ; i++){
		var data = sysData[i];
		if(data.cm_sysinfo.substr(0,1) == '1'){
			continue;
		}
		else if (data.cm_syscd =='00000'){
			options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo : data.cm_sysinfo, cm_systype: data.cm_systype});
		}
		else if(data.cm_sysinfo.substr(9,1) == '1'){
			if(getSelectedIndex('srInfo') > 0){
				continue;
			} else {
				options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo: data.cm_sysinfo, tstsw: data.TstSw, cm_systype: data.cm_systype});
			}
		}
		else{
			if(getSelectedIndex('srInfo') > 0){				
				var syscd = getSelectedVal('srInfo').syscd;
				if (syscd == undefined || null == syscd) continue;
				var arySyscd = syscd.split(",");
				for(var j=0; j<arySyscd.length; j++){
					if(arySyscd[j] == data.cm_syscd){
						options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo: data.cm_sysinfo, tstsw: data.TstSw, cm_systype: data.cm_systype});
						break;
					}
				}
				continue;
			}
			else{
				continue;
			}
		}
	}

	$('[data-ax5select="systemSel"]').ax5select({
        options: options
	});
	return options.length;
}

function changeSys(){
	
	firstGrid.setData([]);
	firstGridData = [];
	
	if (getSelectedVal('systemSel').cm_stopsw == "1") {
		dialog.alert("이관통제를 위하여 일시적으로 형상관리 사용을 중지합니다.");
		$('#btnSearch').prop('disabled',true);
		return;
	} 
	else $('#btnSearch').prop('disabled',false);
	
	if (getSelectedVal('systemSel').cm_sysinfo.substr(9,1) == "1") srSw = false;
	else srSw = true;
	
	if (srSw) {
		$('[data-ax5select="srInfo"]').ax5select("enable");
		if(srData.lenght == 2){
			var selectVal = $('select[name=srInfo] option:eq(1)').val();
			$('[data-ax5select="srInfo"]').ax5select('setValue',selectVal,true);
		}
	} 
	else { 
		var selectVal = $('select[name=srInfo] option:eq(0)').val();
		$('[data-ax5select="srInfo"]').ax5select('setValue',selectVal,true);
		$('[data-ax5select="srInfo"]').ax5select("disable");
	}
	

	if(getSelectedIndex('srInfo') < 1){
		if(reqCd != '05'){
			$('#processingLevel').prop('disabled',false);
		}
		return;
	}
//	console.log(getSelectedVal('systemSel'));
//	console.log(getSelectedVal('srInfo'));
	if(getSelectedVal('systemSel').cm_sysinfo.substr(9,1) == "1") findProc();
	else if(getSelectedIndex('srInfo') > 0) findProc();
	
	/*
	 * 정적분석확인
	if(getSelectedVal('cboSys').cm_sysinfo.substr(14,1) == '1'){
		$('#chkBefJob').show();
	}
	*/
	
}

function findProc() {
//	console.log("qwe");
	if(qrySw) return;
	if(getSelectedVal('systemSel').cm_sysinfo.substr(9,1) == "0" && getSelectedIndex('srInfo') < 1) return;
	
	qrySw = true;
	
	var tmpObj = new Object();	
	tmpObj.UserId = userId;
	tmpObj.SysCd = getSelectedVal('systemSel').value;
	tmpObj.SRID = getSelectedVal('srInfo').value;
	tmpObj.ReqCd = strReqCD;
	tmpObj.ReqCD = strReqCD;
	tmpObj.TstSw = getSelectedVal('systemSel').tstsw;
	tmpObj.SysInfo = getSelectedVal('systemSel').cm_sysinfo;
	
	var paramData = new Object();
	paramData = {
		baseInfoData	: 	tmpObj,
		requestType	    : 	'getDeployList'
	}
	tmpObj = null;
	
	ajaxAsync('/webPage/apply/ApplyRequest', paramData, 'json', successGetProgramList);
	
}

//신청목록조회
function successGetProgramList(data) {
	if (data.length == 0 || (data[0].ERROR != undefined && data[0].ERROR != null)) {
		if(data.length == 0){
			dialog.alert("신청 목록이 없습니다.");
		} else {
			dialog.alert(data[0].ERROR);
		}
		qrySw = false;
		return;
	}
	firstGridData = data;
	if(firstGridData.length == 0 ){
		//dialog.alert('검색 결과가 없습니다.');
		qrySw = false;
		return;		
	}
	else if (firstGridData.length > 0){
		if(firstGridData[0].ID =='MAX'){
			dialog.alert('검색결과가 너무 많으니 검색조건을 입력하여 검색하여 주시기 바랍니다.');
			qrySw = false;
			return;
		}
	}
	var chk = true;
	if(firstGridData.length > 0) {
		for(var i = 0; i < firstGridData.length; i++) {
			if(firstGridData[i].analchk != "0" && firstGridData[i].analchk != "99") {
				chk = false;
				break;
			}
		}
	}
	
	if( chk ) {
		$("#btnApply").prop("disabled", false);
	} else {
		dialog.alert("영향분석 조치를 모두 완료한 후 진행하시기 바랍니다.");
	}
	
	if(getSelectedVal('systemSel').sysft == "Y") {
		$.each(firstGridData, function(i, val) {
			if(val.cr_rsrccd == "03") {
				$("#chkFTDiv").css({"display" : "inline-block"});
				$("#chkFT").prop("disabled", false);
			}
		})
	}
	
	plsqlflg = false;
	$.each(firstGridData, function(i, val) {
		if(val.cr_rsrccd == "07") {
			plsqlflg = true;
		}
	});
	
	if(plsqlflg) {
		$("#chkDBADiv").css({"display" : "inline-block"});
		$("#chkDBA").wCheck('disabled', false);
		$("#chkDBA").prop("data-label", "DBA후결처리(테스트)");
	}
	
	firstGrid.setData(firstGridData);
	
//	if(firstGridData.length > 0 && reqCd == '04'){
//		firstGrid.selectAll({selected:true, filter:function(){
//			
//				return this['selected_flag'] != '1';
//			
//			}
//		});
//		//addDataRow();
//	}
	
	qrySw = false;
}

function befCheck() {
//	console.log(befJobData);
	if($("#chkBef").is(":checked")) {
		if(!befSw) {
			openBefJobSetModal();
		}
	} else if(befJobData.length > 0) {
		mask.open();
        confirmDialog.confirm({
	        title: '선행작업확인',
			msg: '기 선택된 선행작업이 있습니다. 체크해제 시 선행작업이 무시됩니다. 계속 진행할까요?',
		}, function(){
			if(this.key === 'ok') {
				befSw = false;
				befJobData = null;
			} else {
				befSw = true;
				$("#chkBef").wCheck('check', true)
			}
		});
		mask.close();
	}
}

function openBefJobSetModal() {
	setTimeout(function() {
		befJobModal.open({
			width: 915,
			height: 600,
			iframe: {
				method: "get",
				url: "../modal/request/BefJobSetModal.jsp",
				param: "calbefJobModaltModalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					befJobData = [];
					mask.open();
				}
				else if (this.state === "close") {
					if(befJobData == null || befJobData < 1){
						$("#chkBef").wCheck('check', false);
					}
					mask.close();
				}
			}
		}, function () {
		});
	}, 200);
}

function cmdReq() {
	var tmpSayu = getSelectedVal("srInfo").cc_title;
	var strNow = "";
	var strDate = "";
	var strDate2 = "";
	var strTime = "";
	var strNum = "0123456789";
	var tempMon = 0;
	var strMsg = "";
	var now = "";
	
	if(getSelectedVal("systemSel").value == "00000" || getSelectedVal("systemSel") == null) {
		dialog.alert("시스템정보가 없습니다. 확인 후 다시 신청해주세요.");
		return;
	}
	
	if(getSelectedVal("srInfo") == null || getSelectedIndex("srInfo") == 0) {
		dialog.alert("SR정보가 없습니다. 확인 후 다시 신청해주세요.");
		return;
	}
	
//	timeSw = true;
	strApplyDate = "";
	
	if(getSelectedVal("processingLevel").cm_micode == "4") {
		
	}
	
	if(ingSw) {
		dialog.alert("현재 신청하신 건을 처리중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	cmdReq_sub();
}

function cmdReq_sub() {
	var strQry = "";
	var strRsrcCd = "";
	
	ingSw = true;
	$.each(firstGridData, function(i, val) {
		if(strQry.length > 0) {
			if(strQry.indexOf(firstGridData[i].reqcd) < 0) {
				strQry = strQry + ",";
				strQry = strQry + firstGridData[i].reqcd;
			}
		} else {
			strQry = firstGridData[i].reqcd;
		}
		
		if(strRsrcCd.length > 0) {
			if(strRsrcCd.indexOf(firstGridData[i].reqcd) < 0) {
				strRsrcCd = strRsrcCd + ",";
				strRsrcCd = strRsrcCd + firstGridData[i].reqcd;
			}
		} else {
			strRsrcCd = firstGridData[i].cr_rsrccd;
		}
	});
	
//	console.log(getSelectedVal("systemSel"));
//	console.log($('[data-ax5select="systemSel"]').ax5select("getValue"));
	var confirmInfoData = new Object();
	confirmInfoData.SysCd = getSelectedVal("systemSel").value;
	confirmInfoData.ReqCd = strReqCD;
	confirmInfoData.strRsrcCd = strRsrcCd;
	confirmInfoData.UserID = userId;
	confirmInfoData.strQry = strQry;
	
//	console.log(confirmInfoData);
	var tmpData = {
		requestType : 'confSelect',
		confirmInfoData : confirmInfoData
	}	
	ajaxAsync('/webPage/request/ConfirmServlet', tmpData, 'json', confChk);
}

function confChk(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			ingSw = false;
			return;
		}
	}
	if(data == "X") {
		ingSw = false;
		dialog.alert("로컬PC에서 파일을 전송하는 결재단계가 지정되지 않았습니다. 형상관리시스템 담당자에게 연락하여 주시기 바랍니다.");
	} else if (data == "C") {
	    confirmDialog.setConfig({
	        title: "확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				confCall("Y");
			}
			else{
				confCall("N");
			}
		});
	} else if (data == "Y") {
		confCall("Y");
    } else if (data != "N") {
    	if(strReqCD == "08") {
    		confCall("N");
    	} else {    		
    		dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
    		ingSw = false;
    	}
    } else {
		confCall("N");
    }
}

///---------------------------------2020.04.28 여기부터-------------------------------------
function confCall(GbnCd){
	var strQry = "";
	var emgSw = "0";
	var tmpRsrc = "";
	var tmpJobCd = "";
	var tmpPrjNo = "";
	var strDeploy = "0";
	strQry = "";
	for (var x=0;x<firstGridData.length;x++) {
		if (tmpRsrc.length > 0) {
			if (tmpRsrc.indexOf(firstGridData[x].cr_rsrccd) < 0)
	            tmpRsrc = tmpRsrc + "," + firstGridData[x].cr_rsrccd;
		} else tmpRsrc = firstGridData[x].cr_rsrccd;
	
		if (tmpJobCd.length > 0) {
			if (tmpJobCd.indexOf(firstGridData[x].cr_jobcd) < 0)
	            tmpJobCd = tmpJobCd + "," + firstGridData[x].cr_jobcd;
		} else tmpJobCd = firstGridData[x].cr_jobcd;
	
		if (strQry.length > 0) {
			if (strQry.indexOf(firstGridData[x].reqcd) < 0) {
				strQry = strQry + "," + firstGridData[x].reqcd;
			}
		} else strQry = firstGridData[x].reqcd;
	}
	if (swEmg) {
		emgSw = "2"
	} else {
		emgSw = "0";
	}
	if (getSelectedIndex('srInfo')>0) tmpPrjNo = getSelectedVal('srInfo').value;

	strDeploy = "Y";
	var dataFlg = "N";
//	console.log(getSelectedVal('srInfo'));
	if(getSelectedVal('srInfo').cc_gbncd != "14") {
		if(!$("#chkDBA").is(":checked")) {
			dataFlg = "Y";
		}
	} else {
		if(plsqlflg) {
			if(!$("#chkDBA").is(":checked")) {
				dataFlg = "Y";
			}
		}
	}
	
	confirmInfoData = {		
		UserId : userId,
		ReqCd  : reqCd,
		SysCd  : getSelectedVal('systemSel').value,
		RsrcCd : [tmpRsrc],
		QryCd : [strQry],
		EmgSw : emgSw,
		JobCd : "Y",
		deployCd : strDeploy,
		PrjNo : tmpPrjNo,
		dataFlg : dataFlg,
		svChkDev : "false"
	}
	
	if(reqCd == '05'){
		confirmInfoData.OutPos = outpos;
		if ($('#chkSvr').is(':checked')) confirmInfoData.svryn = "N";
		else confirmInfoData.svryn = "Y";
	}
	
	if (GbnCd == "Y") {
		approvalModal.open({
	        width: 850,
	        height: 600,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ApprovalModal.jsp",
	            param: "callBack=modalCallBack"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	if(confirmData != null && confirmData.length > 0){
	            		cmdSecuChkList();
	            	}
	            	ingSw = false;
	                mask.close();
	            }
	        }
		});
	} else if (GbnCd == "N") {

		var tmpData = {
			confirmInfoData: 	confirmInfoData,
			requestType: 	'Confirm_Info'
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/request/ConfirmServlet', tmpData, 'json');
		if (ajaxReturnData != undefined && ajaxReturnData != null) {
			if (ajaxReturnData.indexOf('ERROR')>-1) {
				dialog.alert(ajaxReturnData);
			} else {
				confirmData = ajaxReturnData;
				for(var i=0; i<confirmData.length ; i++){
					if (confirmData[i].arysv[0].SvUser == null || confirmData[i].arysv[0].SvUser == "") {
						confirmData.splice(i,1);
						i--;
					}
				}
//				reqQuestConf();
				cmdSecuChkList();
			}
		}
	}
}

function cmdSecuChkList() {
//	console.log(srId);
	setTimeout(function() {
		secuChkModal.open({
			width: 1250,
			height: 660,
			iframe: {
				method: "get",
				url: "../modal/request/SecuChkModal.jsp",
				param: "callBack=modalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					if(chkListData != null && chkListData.length > 0){
						reqQuestConf();
					}
					ingSw = false;
					mask.close();
				}
			}
		});
	}, 200);
}

function reqQuestConf(){

	ajaxReturnData = null;
	var ajaxReturnStr = null;
	var requestData = new Object();
	var deploy = getSelectedVal('processingLevel').value;
	var emgCd = '0';
	var applyDate = '';

	//취약점 점검 SKIP
	if($("#chkFT").is(":checked")) {
		requestData.sysft = "X";
	} else {
		requestData.sysft = "N";
	}
	
	if(swEmg) emgCd = '2';
	else emgCd = '0';
	if(getSelectedVal('processingLevel').value =='4') {
		applyDate = strApplyDate;
	} else {
		applyDate = strApplyDate;
	}
	requestData.closeyn = 'N';
	
	requestData.PlanDate = replaceAllString($("#operationDate").val(), "/", "");
	
	requestData.UserID = userId;
	requestData.ReqCD  = reqCd;
	requestData.ReqCd  = reqCd;
	requestData.Sayu  = $('#txtSayu').val().trim();
	requestData.Deploy = deploy;
	requestData.EmgCd = emgCd;
	requestData.AplyDate = applyDate;
	requestData.TstSw = getSelectedVal('systemSel').TstSw;
	requestData.syscd = getSelectedVal('systemSel').value;
	requestData.sysgb = getSelectedVal('systemSel').cm_sysgb;
	requestData.jobcd = "DBJOB";
	requestData.emrgb = "0";
	requestData.emrcd = "";
	requestData.emrmsg = "";
	requestData.reqday = "";
	requestData.reqdept = "";
	requestData.reqdoc = "";
	requestData.reqtit = "";
	requestData.ReqSayu = "9";
	requestData.txtSayu = $('#txtSayu').val().trim();
	requestData.reqetc = "";
	requestData.sysgb = getSelectedVal('systemSel').cm_sysgb;
	if (getSelectedIndex('srInfo')>0){
		requestData.cc_srid = getSelectedVal('srInfo').value;
		requestData.cc_title = getSelectedVal('srInfo').cc_title;
		requestData.cc_gbncd = getSelectedVal('srInfo').cc_gbncd;
	} else {
		requestData.cc_title = "";
		requestData.cc_srid = "";
		requestData.cc_gbncd = "";
	}
	
	var tmpData = {
		fileList: firstGrid.getList(),
		baseInfoData: 	requestData,
		requestConfirmList:	confirmData,
		befJobData 	: 	befJobData,
		secuChkList : chkListData,
		requestType: 	'requestConf'
	}
		
	if(strReqCD == "08") {
//		console.log("request_Deploy_DB");
	} else {
		var dataFlg = "N";
		if(getSelectedVal('srInfo').cc_gbncd != "14") {
			dataFlg = "Y";
		}
		tmpData.dataFlg = dataFlg;
//		console.log(tmpData);
		ajaxReturnData = ajaxCallWithJson('/webPage/apply/ApplyRequest', tmpData, 'json');
		
	}
	

	if (ajaxReturnData != undefined && ajaxReturnData != null) {
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
			ingSw = false;
			return;
		}
		if(ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR'){
			dialog.alert('에러가 발생하였습니다. 다시 신청해주세요.');
			ingSw = false;
			return;
		}
		acptNo = ajaxReturnData;
		
		requestEnd();
	}
}

function requestEnd(){
	
	ingSw = false;
    confirmDialog.setConfig({
        title: "확인",
        theme: "info"
    });
	confirmDialog.confirm({
		msg: $('#btnApply').val()+' 신청완료! 상세 정보를 확인하시겠습니까?',
	}, function(){
		upFiles = [];

		if(this.key === 'ok') {
			cmdDetail();
		}
		else{
			getSysCbo();
		}
		mask.close();
	});
	
	firstGrid.setData([]);
	firstGridData = [];
	//선행작업 체크 해제
	$("#chkBef").wCheck('check', false);
	$('#btnApply').prop('disabled',true);
	$('[data-ax5select="systemSel"]').ax5select('enable');
	$('[data-ax5select="srInfo"]').ax5select('enable');
	
	if(!swEmg) $('[data-ax5select="processingLevel"]').ax5select('enable');
	if(getSelectedIndex('processingLevel') > -1) {
		if(getSelectedVal('processingLevel').cm_micode == "4") {
			
		}
	}
	getSrIdCbo();
}

function cmdDetail(){

	var winName = "OperationRequestEnd";
	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= acptNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	nHeight = 740;
    nWidth  = 1300;

	cURL = "/webPage/winpop/PopRequestDetail.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);

	getSysCbo();
}

function cmdReqInfo_Click() {
	if (getSelectedIndex('srInfo') < 1) {
		dialog.alert('SR정보를 확인 할 SR-ID를 선택하십시오.',function(){});
		return;
	}
	
	//ExternalInterface.call("winopen",userId,"SRINFO",cboIsrId.selectedItem.cc_srid);
	var nHeight, nWidth;
	if(winDevRep != null
			&& !winDevRep.closed) {
		winDevRep.close();
	}	
	
	var form = document.popPam;   						  //폼 name
	form.user.value = userId; 	 						  //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.srid.value = getSelectedVal('srInfo').value;    //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)	
	form.acptno.value = '';
	
	nHeight = 510;
	nWidth = 1100;
    
    winDevRep = winOpen(form, 'devRep', '/webPage/winpop/PopSRInfo.jsp', nHeight, nWidth);
}