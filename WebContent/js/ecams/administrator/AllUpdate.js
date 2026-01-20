/**
 * 일괄업데이트 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2019-08-23
 * 
 */

var userName 	= window.parent.userName;		//접속자 Name
var userId 		= window.parent.userId;			//접속자 ID
var adminYN 	= window.parent.adminYN;		//관리자여부
var userDeptName= window.parent.userDeptName;	//부서명
var userDeptCd 	= window.parent.userDeptCd;		//부서코드

var grdProgList = new ax5.ui.grid();    		//프로그램그리드

var cboSysCdData 	= []; //시스템 데이터
var cboJobData		= []; //업무 데이터
var cboJawonData	= []; //프로그램종류 데이터

var cboAftJawonData = []; //(변경후)프로그램종류 데이터
var cboAftJobData	= []; //(변경후)업무 데이터
var cboAftOwnerData	= []; //(변경후)담당자 데이터
var cboAftDirData 	= []; //(변경후)경로 데이터
var cboAftSRIDData	= []; //(변경후)SRID 데이터

var grdProgListData = null; //프로그램그리드 데이터

var tmpInfo     = new Object();
var tmpInfoData = new Object();
var reqSw 		= false;
var selOptions = [];
var selectedIndex;		//select 선택 index
var selectedItem;		//select 선택 item
var gridSelectedIndex;  //그리드 선택 index
var selectedGridItem;	//그리드 선택 item
var gridSelectedLen;    //그리드 선택 item length


grdProgList.setConfig({
    target: $('[data-ax5grid="grdProgList"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    showRowSelector: true,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: 'cm_sysmsg',   	label: '시스템',  		width: '10%',	align: 'left'},
        {key: 'cm_jobname', 	label: '업무',  			width: '7%',	align: 'left'},
        {key: 'cm_dirpath', 	label: '프로그램경로',   	width: '27%',	align: 'left'},
        {key: 'cm_codename', 	label: '프로그램종류',    	width: '10%',	align: 'left'},
        {key: 'cr_rsrcname', 	label: '프로그램명',	  	width: '14%',	align: 'left'},
        {key: 'sta', 	        label: '프로그램상태',  	width: '8%',	align: 'left'},
        {key: 'sr', 			label: 'SR-ID',	  		width: '13%',	align: 'left'},
        {key: 'owner', 			label: '담당자',	  		width: '13%',	align: 'left'}
    ]
});

$(document).ready(function() {
	getSysInfo(); //시스템조회
	getSRID(); //SR조회
	
	cboAftDirData.push({cm_dsncd: '0', cm_dirpath: '업무와 프로그램종류를 선택하세요'});
	$('[data-ax5select="cboAftDir"]').ax5select({
        options: injectCboDataToArr(cboAftDirData, 'cm_dsncd', 'cm_dirpath')
   	});
	
	
	//시스템 변경 이벤트
	$('#cboSysCd').bind('change', function() {
		cboSysCd_Change();
	});
	
	//조회버튼 클릭 이벤트
	$('#btnQry').bind('click', function() {
		btnQry_Click();
	});
	
	//프로그램명 엔터 이벤트
	$('#txtRsrcName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQry').trigger('click');
		}
	});

	//프로그램경로명 엔터 이벤트
	$('#txtDirPath').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQry').trigger('click');
		}
	});
	
	//(Aft)프로그램종류 변경 이벤트
	$('#cboAftJawon').bind('change', function() {
		cboAftJawon_Change();
	});
	
	//(Aft)업무 변경 이벤트
	$('#cboAftJob').bind('change', function() {
		cboAftJob_Change();
	});
	
	//수정버튼 클릭 이벤트
	$('#btnUpdt').bind('click', function() {
		btnUpdt_Click();
	});
});

//시스템조회
function getSysInfo() {
	tmpInfo = new Object();
	tmpInfo.UserId = userId;
	tmpInfo.SelMsg = '';
	tmpInfo.CloseYn = 'N';		
	tmpInfo.SysCd = '';
	if(adminYN) {
		tmpInfo.SecuYn = 'N';
	}else {
		tmpInfo.SecuYn = 'Y';
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
		sysData		: tmpInfo,
		requestType	: 'getSysInfoRpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSystem);
}

function successSystem(data) {
	cboSysCdData = data;
	
	cboSysCdData = cboSysCdData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	});
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options: injectCboDataToArr(cboSysCdData, 'cm_syscd', 'cm_sysmsg')
	});

	if(cboSysCdData.length > 0) {
		$('[data-ax5select="cboSysCd"]').ax5select('setValue', cboSysCdData[0].cm_syscd, true);
		cboSysCd_Change();
	}
}

function cboSysCd_Change() {
	selectedIndex = getSelectedIndex('cboSysCd');
	selectedItem = getSelectedVal('cboSysCd');
	
	if(selectedIndex < 0) return;	
	
	getJobInfo(selectedItem.cm_syscd);   //업무 재조회
	getRsrcInfo(selectedItem.cm_syscd);  //프로그램유형 재조회
	getOwnerList(selectedItem.cm_syscd); //담당자 재조회
}

//선택한 시스템에 대한 업무 조회
function getJobInfo(sysCd) {
	tmpInfo = new Object();
	tmpInfo.UserId = userId;
	tmpInfo.SelMsg = 'ALL';
	tmpInfo.CloseYn = 'N';		
	tmpInfo.SysCd = sysCd;
	if(adminYN) {
		tmpInfo.SecuYn = 'N';
	}else {
		tmpInfo.SecuYn = 'Y';
	}
	tmpInfo.sortCd = 'NAME';
	
	tmpInfoData = new Object();
	tmpInfoData = {
		jobData		: tmpInfo,
		requestType	: 'getJobInfo'
	}

	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successJob);
}

function successJob(data) {
	cboJobData = data;
	cboAftJobData = data;
	
	if (cboJobData != null && (cboJobData.length > 0)) {
		$('[data-ax5select="cboJob"]').ax5select({
	        options: injectCboDataToArr(cboJobData, 'cm_jobcd', 'cm_jobname')
	   	});
		
		$('[data-ax5select="cboJob"]').ax5select('setValue', cboJobData[0].cm_jobcd, true);
		
		
		cboAftJobData.splice(0, 1);
		cboAftJobData.unshift({cm_jobcd: '0', cm_jobname: '선택하세요'});
		$('[data-ax5select="cboAftJob"]').ax5select({
	        options: injectCboDataToArr(cboAftJobData, 'cm_jobcd', 'cm_jobname')
	   	});
		
		$('[data-ax5select="cboAftJob"]').ax5select('setValue', cboAftJobData[0].cm_jobcd, true);
	}
}

//선택한 시스템에 대한 프로그램종류 조회 
function getRsrcInfo(sysCd) {
	tmpInfo = new Object();
	tmpInfo.userId = userId;
	tmpInfo.selMsg = 'ALL';
	tmpInfo.closeYn = 'N';		
	tmpInfo.sysCd = sysCd;
	if(adminYN) {
		tmpInfo.secuYn = 'N';
	}else {
		tmpInfo.secuYn = 'Y';
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
		sysData		: tmpInfo,
		requestType	: 'getRsrcInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successJawon);
}

function successJawon(data) {
	cboJawonData = data;
	cboAftJawonData = data;
	
	if (cboJawonData != null && (cboJawonData.length > 0)) {
		$('[data-ax5select="cboJawon"]').ax5select({
	        options: injectCboDataToArr(cboJawonData, 'cm_micode' , 'cm_codename')
	   	});
		$('[data-ax5select="cboJawon"]').ax5select('setValue', cboJawonData[0].cm_micode, true);
		
		
		cboAftJawonData.splice(0, 1);
		cboAftJawonData.unshift({cm_micode: '0', cm_codename: '선택하세요'});
		$('[data-ax5select="cboAftJawon"]').ax5select({
	        options: injectCboDataToArr(cboAftJawonData, 'cm_micode' , 'cm_codename')
	   	});
		$('[data-ax5select="cboAftJawon"]').ax5select('setValue', cboAftJawonData[0].cm_micode, true);
	}
}

function getOwnerList(syscd){
	tmpInfoData = new Object();
	tmpInfoData = {
		sysCd		: syscd,
		requestType	: 'GETOWNERLIST'
	}
	ajaxAsync('/webPage/administrator/AllUpdateServlet', tmpInfoData, 'json', successOwnerList);
}

function successOwnerList(data) {
	cboAftOwnerData = data;

	cboAftOwnerData.unshift({cm_userid: '0', cm_username: '선택하세요'});
	
	if(cboAftOwnerData.length > 0) {
		$('[data-ax5select="cboAftOwner"]').ax5select({
	        options: injectCboDataToArr(cboAftOwnerData, 'cm_userid' , 'cm_username')
	   	});
	}
}

function getSRID() {
	tmpInfo = new Object();
	tmpInfo.userid = userId;
	tmpInfo.reqcd = "01";
	tmpInfo.secuyn = "Y";
	tmpInfo.qrygbn = "01";		
	
	tmpInfoData = new Object();
	tmpInfoData = {
		srData		: tmpInfo,
		requestType	: 'GETSRID'
	}
	ajaxAsync('/webPage/dev/ProgRegisterServlet', tmpInfoData, 'json', successSRID);
}

function successSRID(data) {
	cboSRIData = data;	

	cboSRIData.unshift({cc_srid: '0', srid: 'SR정보 선택 또는 해당없음'});
	
	if(cboSRIData.length > 0) {
		$('[data-ax5select="cboAftSRID"]').ax5select({
	        options: injectCboDataToArr(cboSRIData, 'cc_srid' , 'srid')
	   	});
	}
}

function cboAftJawon_Change() {
	if(getSelectedIndex('cboAftJawon') > 0) {
		getDirList();
	}
}

function cboAftJob_Change() {
	getDirList();
}

function getDirList() {
	if (getSelectedIndex('cboAftJob') === 0){
		cboAftDirData 	= []; 
		cboAftDirData.push({cm_dsncd: '0', cm_dirpath: '업무와 프로그램종류를 선택하세요'});
		$('[data-ax5select="cboAftDir"]').ax5select({
	        options: injectCboDataToArr(cboAftDirData, 'cm_dsncd', 'cm_dirpath')
	   	});
	} else {
		tmpInfo = new Object();
		tmpInfo.userId = userId;
		tmpInfo.sysCd  = getSelectedVal('cboSysCd').cm_syscd;
		tmpInfo.secuYn = "Y";
		tmpInfo.rsrcCd = getSelectedVal('cboAftJawon').cm_micode;
		tmpInfo.jobCd  = getSelectedVal('cboAftJob').cm_jobcd;
		tmpInfo.selMsg = "SEL";
		
		tmpInfoData = new Object();
		tmpInfoData = {
			dirData		: tmpInfo,
			requestType	: 'GETDIR'
		}
		ajaxAsync('/webPage/dev/ProgRegisterServlet', tmpInfoData, 'json', successDirList);
	}
	
}

function successDirList(data) {
	cboAftDirData = data;
	
	var tmpRsrccd = getSelectedVal('cboAftJawon').cm_micode;
	var tmpJob = getSelectedVal('cboAftJob').cm_jobcd;
	
	if(cboAftDirData.length > 0) {
		$('[data-ax5select="cboAftDir"]').ax5select({
	        options: injectCboDataToArr(cboAftDirData, 'cm_dsncd' , 'cm_dirpath')
	   	});
	}
}

function btnQry_Click() {
	if (reqSw == true) {
		dialog.alert("현재 처리중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	reqSw = true;
	
	grdProgList.setData([]);
	
	tmpInfo = new Object();
	tmpInfo.UserId = userId;
	tmpInfo.L_Syscd  = getSelectedVal('cboSysCd').cm_syscd;
	if(getSelectedIndex('cboJawon') > 0) tmpInfo.Rsrccd = getSelectedVal('cboJawon').cm_micode;
	else tmpInfo.Rsrccd = "";
	if(getSelectedIndex('cboJob') > 0) tmpInfo.JobCd = getSelectedVal('cboJob').cm_jobcd;
	else tmpInfo.JobCd = "";
	
	tmpInfo.DirPath = $('#txtDirPath').val();
	tmpInfo.Txt_ProgId = $('#txtRsrcName').val().trim();
	 
	tmpInfoData = new Object();
	tmpInfoData = {
		tmpInfo			: tmpInfo,
		requestType	: 'getProgList'
	}
	
	ajaxAsync('/webPage/program/ProgramInfoServlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	grdProgListData = data;
	grdProgList.setData(grdProgListData);
	reqSw = false;
}

function btnUpdt_Click() {
	gridSelectedLen = 0;
	
	if(getSelectedIndex('cboAftJob') < 1 && 	//업무
		getSelectedIndex('cboAftJawon') < 1 &&  //프로그램종류 
		getSelectedIndex('cboAftOwner') < 1 &&  //담당자
		getSelectedIndex('cboAftSRID') < 1 &&   //SR-ID
		getSelectedIndex('cboAftDir') < 1)      //경로
	{ 
		dialog.alert('업무/프로그램종류/담당자/SR-ID/경로를 선택하여 주십시오.',function(){});
		return;
	}
	
	var findSW = false;
	var checkedGridItem = grdProgList.getList("selected");
	
	if(checkedGridItem.length > 0) {
		findSW = true;
	}
	
	if(!findSW) {
		dialog.alert('선택된 목록이 없습니다. 목록에서 수정할 대상을 선택한 후 처리하십시오.',function(){});
		return;
	}
	
	checkedGridItem = grdProgList.getList("selected");
	gridSelectedLen = checkedGridItem.length;
	
	tmpInfo = new Object();
	tmpInfo.aftjob = getSelectedVal('cboAftJob').cm_jobcd;
	tmpInfo.aftrsrccd = getSelectedVal('cboAftJawon').cm_micode;
	tmpInfo.aftowner = getSelectedVal('cboAftOwner').cm_userid;
	tmpInfo.aftsrid	= getSelectedVal('cboAftSRID').cc_srid;
	if(cboAftDirData != null && getSelectedIndex('cboAftDir') > 0) tmpInfo.aftdir = getSelectedVal('cboAftDir').cm_dsncd;
	else tmpInfo.aftdir = "0";
	
	tmpInfoData = new Object();
	tmpInfoData = {
		progList	: checkedGridItem,
		progData	: tmpInfo,
		requestType	: 'UPDATEPROGINFO'
	}
	ajaxAsync('/webPage/administrator/AllUpdateServlet', tmpInfoData, 'json', successUpdate);
}

function successUpdate(data) {
	dialog.alert('수정이 완료되었습니다.',function(){
		btnQry_Click();
	});
}