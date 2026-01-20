/**
 * 시스템상세정보 팝업 [공통디렉토리] 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-14
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var dirGrid		= new ax5.ui.grid();
var detailGrid	= new ax5.ui.grid();
var dirGridData 	= null;
var detailGridData 	= null;

var cboSysData 		= null;	// From Cbo 데이터
var cboOptions		= null;

var ulPropData		= null;	// 시스템 속성 데이터
var ulToSysData		= null;	// To 시스템 데이터
var ulPrgData		= null;	// To 시스템 데이터

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});

detailGrid.setConfig({
    target: $('[data-ax5grid="detailGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
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
        {key: "cm_codename", 	label: "서버종류", 		width: '20%', align: "center"},
        {key: "cm_svrname", 	label: "서버명",  		width: '20%', align: "center"},
        {key: "cm_svrip", 		label: "IP Address",  	width: '20%', align: "center"},
        {key: "cm_portno", 		label: "Port",  		width: '10%', align: "center"},
        {key: "cm_volpath", 	label: "홈경로",  		width: '30%', align: "left" },
    ]
});

dirGrid.setConfig({
	target: $('[data-ax5grid="dirGrid"]'),
	sortable: true, 
	multiSort: true,
	showRowSelector: true,
	header: {
		align: "center",
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
		{key: "cm_codename", 	label: "구분", 		width: '20%', align: 'left'},
		{key: "cm_dirpath", 	label: "디렉토리",  	width: '80%', align: 'left'},
	]
});

$('input.checkbox-all').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	
	getSysCbo();
	getCodeInfo();
	
	// From system cbo 체인지 이벤트
	$('#cboSys').bind('change', function() {
		
		var selVal = getSelectedVal('cboSys').value;
		
		if(selVal === '00000') return;
		console.log(getSelectedVal('cboSys').cm_sysinfo);
		var sysinfo = getSelectedVal('cboSys').cm_sysinfo;
		
		$('#chkAllToSys').wCheck('check',false);
		$('#chkAllProp').wCheck('check',false);
		$('#chkAllPrg').wCheck('check',false);
		
		ulPropData.forEach(function(sysInfoItem, index) {
			addId = Number(sysInfoItem.cm_micode);
			$('#chkProp'+addId).wCheck('check',false);
		});
		
		if(sysinfo !== undefined) {
			for(var i=0; i<sysinfo.length; i++) {
				if(sysinfo.substr(i,1) === '1') $('#chkProp'+(i+1)).wCheck('check',true);
			}
		}
		
		getProgInfo(selVal);
		getDetailSvrInfo(selVal);
		getDirInfo(selVal);
		
	});
	
	// 시스템 To 전체선택
	$('#chkAllToSys').bind('click',function() {
		var checkSw = $('#chkAllToSys').is(':checked');
		var addId = null;
		
		ulToSysData.forEach(function(item,index) {
			 addId =  item.cm_syscd;
			 if(checkSw) $('#chkToSys'+addId).wCheck('check',checkSw);
			 else $('#chkToSys'+addId).wCheck('check',checkSw);
		});
	});
	
	// 시스템속성 전체선택
	$('#chkAllProp').bind('click',function() {
		var checkSw = $('#chkAllProp').is(':checked');
		var addId = null;
		
		ulPropData.forEach(function(item,index) {
			 addId =  Number(item.cm_micode);
			 if(checkSw) $('#chkProp'+addId).wCheck('check',checkSw);
			 else $('#chkProp'+addId).wCheck('check',checkSw);
		});
	});
	
	// 프로그램종류 전체선택
	$('#chkAllPrg').bind('click',function() {
		var checkSw = $('#chkAllPrg').is(':checked');
		var addId = null;
		
		ulPrgData.forEach(function(item,index) {
			 addId =  item.cm_micode;
			 if(checkSw) $('#chkPrg'+addId).wCheck('check',checkSw);
			 else $('#chkPrg'+addId).wCheck('check',checkSw);
		});
	});
	
	// 등록
	$('#btnReq').bind('click', function() {
		checkVal();
	});
	
	// 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
	
});

// 복사 유효성 체크
function checkVal() {
	var selSysCd 	= getSelectedVal('cboSys').value;
	var addId	= null;	
	var selToSys = false;
	if(selSysCd === '00000') {
		dialog.alert('복사대상시스템 (FROM)을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	for (var i=0;ulToSysData.length>i;i++) {
		 addId =  ulToSysData[i].cm_syscd;
		 if($('#chkToSys'+addId).is(':checked')) {
			 if (selSysCd != addId) {
				 selToSys = true;
				 break;
			 }
		 }
	}
	
	if(!selToSys) {
		dialog.alert('복사대상시스템 (TO)을 선택하여 주시기 바랍니다.', function(){});
		return;
	}
	
	var etcData		= new Object();
	var tmpSys 		= '';
	var tmpInfo		= '';
	var tmpProg 	= '';
	var svrList		= [];
	var dirList		= [];
	var selSvrIndexs= [];
	var selDirindexs= [];
	
	ulToSysData.forEach(function(item,index) {
		 addId =  item.cm_syscd;
		 if($('#chkToSys'+addId).is(':checked') && selSysCd !== item.cm_syscd) {
			 if(tmpSys.length > 0 ) tmpSys += ',';
			 tmpSys += item.cm_syscd;
		 }
	});
	
	if($('#chkCopy').is(':checked')) {
		ulPropData.forEach(function(item,index) {
			addId =  Number(item.cm_micode);
			if($('#chkProp'+addId).is(':checked')) tmpInfo += '1';
			else tmpInfo += '0';
		});
	}
	
	ulPrgData.forEach(function(item, index) {
		addId =  item.cm_micode;
		 if($('#chkPrg'+addId).is(':checked')) {
			 if(tmpProg.length > 0 ) tmpProg += ',';
			 tmpProg += item.cm_micode;
		 }
	});
	
	etcData.cm_syscd = selSysCd;
	etcData.copysys = tmpSys;
	etcData.info 	= tmpInfo;
	etcData.prog 	= tmpProg;
	etcData.userid 	= userId;
	
	if ($('#chkItem').is(':checked')) 	etcData.item = "Y";
	else etcData.item = "N";
	/*if ($('#chkMonitor').is(':checked'))etcData.monitor = "Y";
	else etcData.monitor = "N";
	etcData.monitor = "N";*/
	if ($('#chkCopy').is(':checked')) 	etcData.copy = "Y";
	else etcData.copy = "N";
	
	selSvrIndexs = detailGrid.selectedDataIndexs;
	selDirindexs = dirGrid.selectedDataIndexs;
	
	selSvrIndexs.forEach(function(detailIndex, index) {
		svrList.push(detailGrid.list[detailIndex]);
	});
	selDirindexs.forEach(function(dirIndex, index) {
		dirList.push(dirGrid.list[dirIndex]);
	});
	
	var data = new Object();
	data = {
		etcData 	: etcData, 
		svrList 	: svrList,
		dirList 	: dirList,
		requestType	: 'sysCopy'
	}
	console.log('[SysCopyModal.js] sysCopy ==>', data);
	ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', data, 'json',successCopySys);
}

// 복사 완료
function successCopySys(data) {
	if(data === 'OK') {
		dialog.alert('시스템정보 복사처리를 완료하였습니다.', function(){});
	} else {
		dialog.alert('시스템정보 복사처리중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.', function(){});
	}
}

// 프로그램종류 가져오기
function getProgInfo(sysCd){
	var data = new Object();
	data = {
			SysCd 	: sysCd, 
		requestType	: 'getProgInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', data, 'json',successGetProgInfo);
}

// 프로그램종류 가져오기 완료
function successGetProgInfo(data) {
	ulPrgData = data;
	$('#ulPrg').empty();
	var liStr = '';
	var addId = null;
	ulPrgData.forEach(function(prgItem, sysInfoIndex) {
		addId = prgItem.cm_micode;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-prg" id="chkPrg'+addId+'" data-label="'+prgItem.cm_codename+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulPrg').html(liStr);
	
	$('input.checkbox-prg').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 시스템상세정보 가져오기
function getDetailSvrInfo(sysCd) {
	var data = new Object();
	data = {
		SysCd 	: sysCd, 
		SvrCd	: null,
		requestType	: 'getSvrInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', data, 'json',successGetDetailSvrInfo);
}

// 시스템상세정보 가져오기 완료
function successGetDetailSvrInfo(data) {
	detailGridData = data;
	detailGrid.setData(detailGridData);
}

// 공통디렉토리정보 가져오기
function getDirInfo(sysCd) {
	var data = new Object();
	data = {
			SysCd 	: sysCd, 
		requestType	: 'getDirInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', data, 'json',successGetDirInfo);
}

// 공통디렉토리정보 가져오기 완료
function successGetDirInfo(data) {
	dirGridData = data;
	dirGrid.setData(dirGridData);
}

// From system cbo 가져오기
function getSysCbo() {
	var etcData = new Object();
	
	etcData.UserId 	= userId;
	etcData.SelMsg 	= 'SEL';
	etcData.CloseYn ='N';
	etcData.SysCd 	= null;
	
	var data = new Object();
	data = {
			UserId 	: userId,
			SelMsg 	: 'SEL',
			CloseYn : 'N',
			SysCd 	: null,
		requestType	: 'getSysInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysCbo);
}

// From system cbo 가져오기 완료
function successGetSysCbo(data) {
	cboSysData = data;
	cboOptions = [];
	$.each(cboSysData,function(key,value) {
		if(value.cm_syscd == '00000') {
			cboOptions.push({value: value.cm_syscd, text: value.cm_sysmsg, cm_sysgb: value.cm_sysgb, cm_sysinfo: value.cm_sysinfo});
		}else {
			cboOptions.push({value: value.cm_syscd, text: value.cm_sysmsg+ " [ " +value.cm_syscd + " ] ", cm_sysgb: value.cm_sysgb, cm_sysinfo: value.cm_sysinfo});
		}
	});
	$('[data-ax5select="cboSys"]').ax5select({
        options: cboOptions
	});
	
	ulToSysData = [];
	cboSysData.forEach(function(item, index) {
		if(item.cm_syscd !== '00000') ulToSysData.push(item);
	});
	makeToSysUlList();
}

function makeToSysUlList() {
	$('#ulToSys').empty();
	var liStr = '';
	var addId = null;
	ulToSysData.forEach(function(sysInfoItem, sysInfoIndex) {
		addId = sysInfoItem.cm_syscd;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-tosys" id="chkToSys'+addId+'" data-label="'+sysInfoItem.cm_sysmsg+" [ "+addId+" ]"+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulToSys').html(liStr);
	
	$('input.checkbox-tosys').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 시스템 속성 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('SYSINFO','','N','3','')
	]);
	ulPropData 	= codeInfos.SYSINFO;
	makeSysInfoUlList();
}

// 시스템 속성 ul 만들어주기
function makeSysInfoUlList() {
	$('#ulProp').empty();
	var liStr = '';
	var addId = null;
	ulPropData.forEach(function(sysInfoItem, sysInfoIndex) {
		addId = Number(sysInfoItem.cm_micode);
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-sysprop" id="chkProp'+addId+'" data-label="'+sysInfoItem.cm_codename+" [ "+sysInfoItem.cm_micode+" ]"+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulProp').html(liStr);
	
	$('input.checkbox-sysprop').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

// 닫기
function popClose(){
	window.parent.sysCopyModal.close();
}