/**
 * [프로그램 > 프로그램정보수정] 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부

var modifyGrid		= new ax5.ui.grid();

var cboSysData		= [];
var rsrcData 		= [];
var jobData 		= [];
var dirData 		= [];
var isridData		= [];
var cboReqData		= [];
var modifyGridData 	= [];
var myWin			= null;
 
modifyGrid.setConfig({
    target: $('[data-ax5grid="modifyGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
        columnHeight: 30
    },
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
//    	trStyleClass: function () {
//    		if(this.item.cr_status === 'Z'){
//    			return "fontStyle-cncl";
//     		} 
//     		if(this.item.cr_status === '0' && this.item.visible === '1'){
//    			return "fontStyle-ing";
//     		} 
//    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "rsrccd", 		label: "프로그램종류",  	width: '13%', align: "left"},
    	{key: "cr_rsrcname", 	label: "프로그램명", 	 	width: '13%', align: "left"},
    	{key: "cr_story", 		label: "프로그램설명",  	width: '20%', align: "left"},
    	{key: "cm_dirpath", 	label: "프로그램경로",  	width: '13%', align: "left"},
        {key: "sta",		 	label: "상태", 		 	width: '10%', align: "left"},
        {key: "job",	 		label: "업무",  			width: '10%', align: "left"},
        {key: "cm_username", 	label: "최종변경자", 	 	width: '10%', align: "left"},
        {key: "cr_lastdate", 	label: "최종변경일",  		width: '10%', align: "left"},
        {key: "cr_lstver", 		label: "버전", 		 	width: '10%', align: "left"}
    ]
});


$(document).ready(function() {
	screenInit();

	getSysCbo();
	
	//시스템 조회
	$('#cboSys').bind('change',function(){
		cboSys_Change();
	});

	//조회버튼 클릭
	$('#btnQry').bind('click', function() {
		btnQry_Click();
	});
	
	//프로그램명 입력 후 엔터로 조회
	$('#txtRsrcName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQry').trigger('click');
		}
	});

	//수정버튼 클릭
	$('#btnModify').bind('click',function(){
		cmdUpdata_Click();
	});
	
	//엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		modifyGrid.exportExcel("프로그램정보수정_" + userId + ".xls");
	});
	
});

function screenInit(){
	modifyGrid.setData([]);
	$('[data-ax5select="cboRsrc"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboRsrcType"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboJob"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDir"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboISRID"]').ax5select({
		options: []
	});
	$('#txtRsrcName').val('');
}

//시스템
function getSysCbo(){

	var tmpInfo = new Object();
	tmpInfo.UserID 	= userId;
	if(adminYN) {
		tmpInfo.SecuYn = 'N';
	}else {
		tmpInfo.SecuYn = 'Y';
	}
	tmpInfo.SelMsg 	= 'SEL';
	tmpInfo.CloseYn = 'N';
	tmpInfo.ReqCd	= '01';
    tmpInfoData = new Object();
    tmpInfoData = {
    	sysData		:	tmpInfo,	
    	requestType	: 	'getSysInfo'
    }
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json',successGetSysCd);
}

function successGetSysCd(data){
	console.log("SysCd= ", data);
	
	cboSysData = data;
	var options = [];
	
	for(var i=0; i<data.length;i++){
		options.push({value: cboSysData[i].cm_syscd, text: cboSysData[i].cm_sysmsg, cm_sysgb: cboSysData[i].cm_sysgb, cm_sysinfo : cboSysData[i].cm_sysinfo});
	};
	
	$('[data-ax5select="cboSys"]').ax5select({
        options: options
	});
	
	cboSys_Change();

}

//시스템 변경
function cboSys_Change(){
	if(getSelectedIndex('cboSys') > 0 && getSelectedVal('cboSys') != null){
//		Spr_Lst_dp.source = null;
//		Spr_Lst.dataProvider = Spr_Lst_dp;
		
		screenInit();

		var sysCd = getSelectedVal('cboSys').value;
		getRsrcInfo(sysCd); //프로그램 종류 조회
		getJobInfo(sysCd);  //업무 조회
		getDir(sysCd);  //경로 조회
		getISRID(getSelectedVal('cboSys'));  //srid
		
	}
}

//프로그램종류
function getRsrcInfo(sysCd) {
	
	var tmpInfo = new Object();
	tmpInfo.Syscd 	= sysCd;
	tmpInfo.SelMsg 	= 'ALL';  

	tmpInfoData = new Object();
    tmpInfoData = {
    	tmpInfo		:	tmpInfo,	
    	requestType	: 	'getRsrcInfo'
    }
    ajaxAsync('/webPage/program/ProgramModify', tmpInfoData, 'json',successGetRsrc);

	tmpInfo.SelMsg 	= 'SEL'; //Cmd06001
    
    tmpInfoData = new Object();
    tmpInfoData = {
    		tmpInfo		:	tmpInfo,	
    		requestType	: 	'getRsrcInfo'
    }
    ajaxAsync('/webPage/program/ProgramModify', tmpInfoData, 'json',successGetRsrc_1);
}
//상단 프로그램종류
function successGetRsrc(data){
	console.log("Rsrc_1= ", data);
	
	rsrcData1 = data;
	var options = [];
	
	for(var i = 0; i < rsrcData1.length; i++){
		options.push({value: rsrcData1[i].cm_micode, text: rsrcData1[i].cm_codename });
	};
	
	$('[data-ax5select="cboRsrc"]').ax5select({
		options: options
	});
}
//하단 프로그램종류
function successGetRsrc_1(data){
	console.log("Rsrc_2= ", data);
	
	rsrcData2 = data;
	var options = [];
	
	for(var i = 0; i < rsrcData2.length; i++){
		options.push({value: rsrcData2[i].cm_micode, text: rsrcData2[i].cm_codename, ID: rsrcData2[i].ID });
	};
	
	$('[data-ax5select="cboRsrcType"]').ax5select({
		options: options
	});
//    chkBox_Change(getSelectedVal('cboRsrcType').ID.substr(3));	
}

//하단 업무
function getJobInfo(sysCd) {
	
	tmpInfo = new Object();
	tmpInfo.UserID = userId;
	tmpInfo.SysCd = sysCd;
	if(adminYN) {
		tmpInfo.SecuYn = 'N';
	}else {
		tmpInfo.SecuYn = 'Y';
	}
	tmpInfo.CloseYn = 'Y';		
	tmpInfo.SelMsg = 'SEL';
	tmpInfo.sortCd = 'NAME';
	
	tmpInfoData = new Object();
	tmpInfoData = {
		jobData		: tmpInfo,
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successGetJob);
}

function successGetJob(data){
	console.log("Job= ", data);
	
	jobData = data;
	var options = [];
	
	for(var i=0; i < jobData.length; i++){
		options.push({value: jobData[i].cm_jobcd, text: jobData[i].cm_jobname , ID: jobData[i].ID});
	};
	
	$('[data-ax5select="cboJob"]').ax5select({
		options: options
	});
	
//    chkBox_Change(getSelectedVal('cboJob').ID.substr(3));	
}

//하단 프로그램경로
function getDir(sysCd) {
	
	tmpInfo = new Object();
	tmpInfo.UserID = userId;
	tmpInfo.SysCd = sysCd;
	tmpInfo.SecuYn = 'Y';
	tmpInfo.RsrcCd = '';		
	tmpInfo.JobCd = '';
	tmpInfo.SelMsg = 'SEL';
	
	tmpInfoData = new Object();
	tmpInfoData = {
			tmpInfo		: tmpInfo,
			requestType	: 'getDir' //Cmd0600
	}
	ajaxAsync('/webPage/program/ProgramModify', tmpInfoData, 'json', successGetDir);
}

function successGetDir(data){
	console.log("Dir= ", data);
	
	dirData = data;
	var options = [];
	
	for(var i=0; i<data.length;i++){
		options.push({value: dirData[i].cm_dsncd, text: dirData[i].cm_dirpath });
	};
	
	$('[data-ax5select="cboDir"]').ax5select({
		options: options
	});
	
//    chkBox_Change(getSelectedVal('cboDir').ID.substr(3));	
}

//하단 isrid
function getISRID(sysCd) {
	
	if(sysCd.cm_sysinfo.substr(3,1) == '1'){ //cm_sysinfo 버전만 관리 
		isridData.unshift({isrid: "SR정보 선택 또는 해당없음", text: "SR정보 선택 또는 해당없음"});
//		    cboISRInfo_dp.source = null;
//			cboISRInfo.dataProvider = cboISRInfo_dp;		
	} else{
		tmpInfo = new Object();
		tmpInfo.userid = userId;
		tmpInfo.reqcd = '01';
		tmpInfo.qrygbn = '01';
		tmpInfo.secuyn = 'Y';		
		
		tmpInfoData = new Object();
		tmpInfoData = {
				tmpInfo		: tmpInfo,
				requestType	: 'getPrjList_Chg' 
		}
		ajaxAsync('/webPage/program/ProgramModify', tmpInfoData, 'json', successGetISRID);
	}
}

function successGetISRID(data){
	console.log("ISRID= ", data);
	
	isridData = data;
	var options = [];
	
	for(var i=0; i<data.length;i++){
		options.push({value: isridData[i].isrid, text: isridData[i].isridtitle, cc_isrid: isridData[i].cc_isrid, isrid: isridData[i].isrid, cc_detcate: isridData[i].cc_detcate, TstSw: isridData[i].TstSw });
	};
	
	$('[data-ax5select="cboISRID"]').ax5select({
		options: options
	});
	
//    chkBox_Change(getSelectedVal('cboISRID').ID.substr(3));	
}

//조회
function btnQry_Click(){
	
	modifyGridData = [];
	modifyGrid.setData(modifyGridData);
	
	if(getSelectedIndex('cboSys') < 1){
		dialog.alert('시스템 정보가 없습니다. 확인 후 다시 조회해주세요');
		$('#cboSys').focus();
		return;
	}
	
	tmpInfo = new Object();
	tmpInfo.UserId = userId;
	tmpInfo.SecuYn = 'Y';	//SecuYn
	tmpInfo.cboSys = getSelectedVal('cboSys').value; //cm_syscd
	tmpInfo.cboRsrccd = '00';
	if (getSelectedIndex('cboRsrc') > 0){
		tmpInfo.cboRsrccd = getSelectedVal('cboRsrc').value; //cm_micode
	}
	tmpInfo.txtRsrcName = '';
	var txtRsrc = $('#txtRsrcName').val($('#txtRsrcName').val().trim());
	if($(txtRsrc.length > 0)) {
		tmpInfo.txtRsrcName = txtRsrc.val();
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
			tmpInfo		: tmpInfo,
			requestType	: 'getSqlQry' 
	}
	ajaxAsync('/webPage/program/ProgramModify', tmpInfoData, 'json', successProgInfo);
}

function successProgInfo(data){
	console.log("조회= ", data);	
	
	modifyGridData = data;
	modifyGrid.setData(modifyGridData);
	
//	modifyGrid.setColumnSort({cr_rsrcname:{seq:0, orderBy:'asc'}});
//	$('#lbTotalCnt').text('총' + modifyGridData.length + '건');
	
	if(modifyGridData.length > 0){
		$('#btnExcel').prop('disabled', false);
	}else {
		$('#btnExcel').prop('disabled', true);
	} 
}

//체크박스 사용 안함
//function chkBox_Change(chk){
//	switch (chk){
//	case "RsrcType":
//		alert("$('#rsrcCheck').val() " + $('#rsrcCheck').val());
//		if ($('#rsrcCheck').val()  && getSelectedVal('cboRsrcType') != null){
//			$('#cboRsrcType').prop('disabled', false);
//		} else{
//			$('#cboRsrcType').prop('disabled', true);
//			$('#rsrcCheck').prop('checked', false);
//		}
//		break;
//	case "Job":
//		if (getSelectedIndex('cboJob') > 0 && getSelectedVal('cboJob') != null){
//			$('#cboJob').prop('disabled', false);
//		} else{
//			$('#cboJob').prop('disabled', true);
//			$('#jobCheck').prop('checked', false);
//		}
//		break;
//	case "Dir":
//		if (getSelectedIndex('cboDir') > 0 && getSelectedVal('cboDir') != null){
//			$('#cboDir').prop('disabled', false);
//		} else{
//			$('#cboDir').prop('disabled', true);
//			$('#dirCheck').prop('checked', false);
//		}
//		break;
//	case "ISRInfo":
//		if (getSelectedIndex('cboISRID') > 0 && getSelectedVal('cboISRID') != null){
//			$('#cboISRID').prop('disabled', false);
//		} else{
//			$('#cboISRID').prop('disabled', true);
//			$('#ISRIDCheck').prop('checked', false);
//		}
//		break;
//}
//	
//}

//수정
function cmdUpdata_Click(){
	var modifySelectedIndex = modifyGrid.selectedDataIndexs;
	var modifyGridItem = modifyGrid.list[modifyGrid.selectedDataIndexs];
	
	if (getSelectedIndex('cboRsrcType')<1 || getSelectedVal('cboJob') == null) {
		dialog.alert('프로그램종류를 선택하여 주시기 바랍니다.',function(){});
		$('#cboRsrcType').focus();
		return;
	}
	if (getSelectedIndex('cboJob')<1 || getSelectedVal('cboJob') == null) {
		dialog.alert('업무를 선택하여 주시기 바랍니다.',function(){});
		$('#cboJob').focus();
		return;
	}
	if (getSelectedIndex('cboDir')<1 || getSelectedVal('cboJob') == null) {
		dialog.alert('프로그램경로를 선택하여 주시기 바랍니다.',function(){});
		$('#cboDir').focus();
		return;
	}
	//ISR정보 없어 테스트 위해 임시로 막아둠
//	if (getSelectedIndex('cboISRID')<1 || getSelectedVal('cboISRID') == null) {
//		dialog.alert('ISR 정보를 선택하여 주시기 바랍니다.',function(){});
//		$('#cboISRID').focus();
//		return;
//	}else{
//		if (getSelectedVal('cboISRID').cc_detcate.substr(0,1) == "9" && getSelectedVal('cboISRID').TstSw == "1") {
//			Alert.show("["+getSelectedVal('cboISRID').isrid+"]은(는) 테스트진행이 없는 요청입니다.\n시스템 또는 ISR정보를 변경하여 주십시오.");
//			return;
//		}
//		if (getSelectedVal('cboISRID').cc_detcate.substr(0,1) != "9" && getSelectedVal('cboISRID').TstSw != "1") {
//			Alert.show("["+getSelectedVal('cboISRID').isrid+"]은(는) 테스트진행이 있는 요청입니다.\n시스템 또는 ISR정보를 변경하여 주십시오.");
//			return;
//		}
//	}
	

	
	var i = 0;
	var chkList = true;
	for (i=0 ; modifyGridData.length>i ; i++) {
		if (modifySelectedIndex > -1) {
			chkList = false;
			break;
		}
	}
	if (chkList){
		dialog.alert("선택된 목록이 없습니다. 목록에서 수정 할 대상을 체크하여 주십시오.");
		return;
	}
//	for (i = 0 ; modifyGridData.length>i ; i++) {
//		if (modifyGridData[i].selectedDataIndexs == "1") {
		if (modifySelectedIndex > -1) {
		
			tmpInfo = new Object();

			tmpInfo.cr_syscd = modifyGridItem.cr_syscd;
			tmpInfo.cr_itemid = modifyGridItem.cr_itemid;
			tmpInfo.cr_rsrccd = getSelectedVal('cboRsrcType').value;
			tmpInfo.cr_dsncd = getSelectedVal('cboDir').value;
			tmpInfo.cr_jobcd = getSelectedVal('cboJob').value;
			tmpInfo.cr_isrid = "";
			tmpInfo.cr_isrsub = "";
			if (getSelectedVal('cboSys').cm_sysinfo.substr(3,1)=="0" && getSelectedIndex('cboISRID') > 0){
				tmpInfo.cr_isrid = getSelectedVal('cboISRID').cc_isrid;
				tmpInfo.cr_isrsub = getSelectedVal('cboISRID').cc_isrsub;
			}
			
			tmpInfoData = new Object();
			tmpInfoData = {
					UserId 		: userId, 
					progList	: [tmpInfo],  //서블릿에서 ArrayList로 받고 있음
					requestType	: 'setRsrcInfo' 
	    	}
    	}
//    	}
//	}
	ajaxAsync('/webPage/program/ProgramModify', tmpInfoData, 'json', successSetRsrcInfo);
}

function successSetRsrcInfo(data){
	console.log("수정= ", data);	
	
	if(Number(data) == 0) {
		dialog.alert('수정이 완료되었습니다.', function(){});
		$('#btnQry').trigger('click');
	}else {
		dialog.alert('수정 실패되었습니다.', function(){});
	}
}


