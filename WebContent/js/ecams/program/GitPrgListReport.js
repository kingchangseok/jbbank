var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;

var mainGrid	= new ax5.ui.grid();
var cboStaData	= null;	
var orgProgData = [];
var progData = [];
var cboJobData = [];

var SecuYn = adminYN ? 'Y' : 'N';

var sysCd = "";

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});
$('[data-ax5select="cboJob"]').ax5select({
    options: []
});
$('[data-ax5select="cboSta"]').ax5select({
    options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

var columnData = 
	[ 
		{key : "cm_dirpath",	label : "프로그램경로",		align : "left",		width: "25%"}, 
		{key : "cr_rsrcname",	label : "프로그램명",		align : "left",		width: "15%"}, 
		{key : "sta",        	label : "프로그램상태",		align : "left",	    width: "10%"}, 
		{key : "progstaname",	label : "점검결과",		align : "left",	    width: "10%"},
		{key : "cm_jobname",    label : "업무",			align : "left",		width: "10%"}, 
		{key : "cr_story",   	label : "프로그램설명",		align : "left",		width: "10%"},
		{key : "rsrccd",     	label : "프로그램구분",		align : "left",		width: "10%"}, 
		{key : "language",		label : "언어",			align : "center",	width: "10%"},
	];

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	sortable : true,
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	header : {align: "center"},
	body : {
		onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
			selectedItem = this.item;
		},
		onDBLClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
			
			if (this.progsta != '1') openWindow('PROGINFO', this.item.cr_itemid,this.item.cm_info);
		}
	},
	columns : columnData,
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {
			'arrow': '<i class="fa fa-caret-right"></i>'
		},
		items: [
			{type: 1, label: "프로그램정보"},
			{type: 2, label: "소스보기"},
			{type: 3, label: "소스비교"}
			],
			popupFilter: function (item, param) {
				var selIn = mainGrid.selectedDataIndexs;
	        	if(selIn.length === 0) return;
	        	 
	         	if (param.item == undefined) return false;
	         	if (param.dindex < 0) return false;
	         	
	         	if (param.item.progsta == '1') return false;
	         	
				if(((selectedItem.cm_info.substr(11, 1) == "1" && selectedItem.cm_info.substr(9, 1) == "0") ||
						selectedItem.cm_info.substr(26, 1) == "1") && parseInt(selectedItem.cr_lstver) > 0) {
					if(parseInt(selectedItem.cr_lstver) > 1) {
						return true;
					}
					return item.type == 1 || item.type == 2;
				}
				return item.type == 1;
			},
			onClick: function (item, param) {
				mainGrid.contextMenu.close();
				if(item.type === 1) {
					openWindow('PROGINFO', param.item.cr_itemid,param.item.cm_info);
				} else if(item.type === 2) {
					openWindow('SRCVIEW', param.item.cr_itemid,param.item.cm_info);                	
				} else if (item.type === 3) {	
					openWindow('SRCDIFF', param.item.cr_itemid,param.item.cm_info);                	
				}
				//또는 return true;
			}
	}
});

$(document).ready(function() {
	if (userId == null || userId == '' || userId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	
	checkAdmin();
	comboSet();	
	$('#btnExcel').prop('disabled', true);
	
	//시스템
	$('#cboSysCd').bind('change', function() {
		cboSysCd_Change();
	});

	//조건선택1
	$('#cboSta').bind('change', function() {
		cboSta_Change();
	});

	
	//조회 이벤트 시
	$("#btnSearch").bind('click', function() {
		btnSearch_click();
	});

	//엑셀저장
	$("#btnExcel").on('click', function() {
		mainGrid.exportExcel(userId+'_GitPrgListReport.xls');
	});

	//엔터키
	$("#txtProg").bind('keypress', function(event) {
		if(window.event.keyCode == 13) {
			if (orgProgData.length>0) {
				progFilter();
			}
		}
	});
})

//어드민 여부 확인
function checkAdmin(){
	data =  new Object();
	data = {
		Sv_UserID	: userId,
		requestType	: 'getRGTCDInfo'
	}
	ajaxAsync('/webPage/common/UserInfoServlet', data, 'json',successGetUserInfo);
}

//어드민 여부 확인 완료
function successGetUserInfo(data) {
	var rgtCd = data;
	SecuYn = "N";
	if(rgtCd.indexOf("A") > -1 || rgtCd.indexOf("M") > -1) {
		SecuYn = "Y";
		getSysInfo();
	} else {
		getSysInfo();
	}
}

function getSysInfo() {
	var sysData =  new Object();
	sysData = {
		UserId 		: userId,
		SecuYn 		: "N",
		SelMsg 		: "SEL",
		CloseYn 	: "N",
		ReqCd 		: "",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', sysData, 'json',successGetSysCbo);
}

//시스템 리스트
function successGetSysCbo(data) {
	var cboSystemData = data;

	cboSystemData = cboSystemData.filter(function(data) {
		if (data.cm_syscd == '00000') return true;
		if (data.cm_sysinfo.substr(0,1) == '1') return false;
		if (data.cm_sysinfo.substr(18,1) == '0') return false;
		else return true;
	});
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options: injectCboDataToArr(cboSystemData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if(cboSystemData.length > 0) {
		$("#cboSysCd").ax5select('setValue',"00000",true);
		sysCd = getSelectedVal('cboSysCd').cm_syscd;
	}
	
	cboSysCd_Change();
}

//콤보 데이터 셋
function comboSet() {
		
	//조건선택1
	$('[data-ax5select="cboSta"]').ax5select({
		options: [
			{value: "0", text: "전체"},
			{value: "1", text: "미등록프로그램"},
			{value: "2", text: "프로그램변경[대여대상]"},
			{value: "3", text: "적용요청대상[신규]"},
			{value: "4", text: "적용요청대상[수정]"},
			{value: "5", text: "적용요청중[신규]"},
			{value: "6", text: "적용요청중[수정]"},
		]
	});
	$('[data-ax5select="cboSta"]').ax5select('setValue','0',true);
	cboSta_Change();
}

function cboSysCd_Change() {
	cboJobData = [];
	mainGrid.setData([]);
	selectedIndex = getSelectedIndex('cboSysCd');
	selectedItem = getSelectedVal('cboSysCd');

	if (selectedIndex < 0) return;
	if(selectedIndex < 1) {
		$('[data-ax5select="cboJob"]').ax5select({
			options: []
		});
	} else {
		if( selectedItem == null ) return;
		getJobInfo(selectedItem.cm_syscd);  //업무 리로딩
	}
}
//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(sysCd) {
	var data = new Object();
	data = {
		UserID 		: userId,
		SysCd 		: sysCd,
		SecuYn	 	: SecuYn,		
		CloseYn	 	: 'Y',		
		SelMsg 		: 'ALL',
		sortCd 		: 'NAME',
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successJob);
}

function successJob(data) {
	cboJobData = data;	
	if (cboJobData != null && (cboJobData.length > 0)) {
		options = [];
		$.each(cboJobData,function(key,value) {
			options.push({value: value.cm_jobcd, text: value.cm_jobname});
		});
		
		$('[data-ax5select="cboJob"]').ax5select({
	        options: options
		});
	} else {
		options = [];
		options.push({value: "0000", text: "전체"});
		$('[data-ax5select="cboJob"]').ax5select({
	        options: options
		});
	}
}
function cboSta_Change() {
		
	if (orgProgData.length>0) {
		progFilter();
	}
	
}

function btnSearch_click() {
	if (getSelectedIndex('cboSysCd') < 1) {
		setTimeout(function() {
			dialog.alert('시스템을 선택하여 주시기 바랍니다.',function(){});
		}, 10);
		return;
	} 
	if (getSelectedIndex('cboJob') < 1) {
		setTimeout(function() {
			dialog.alert('업무를 선택하여 주시기 바랍니다.',function(){});
		}, 10);
		return;
	}
	orgProgData = [];
	progData = [];
	mainGrid.setData([]);
	var inputData = new Object();
	var tmpObj = {};
		tmpObj.userid  = userId;
		tmpObj.SecuYn  = SecuYn;
		tmpObj.syscd = getSelectedVal('cboSysCd').cm_syscd;
		tmpObj.jobcd = getSelectedVal('cboJob').value;
	
	ajaxData = {
		etcData 	: tmpObj,
		requestType : "gitProgList"
	}

	$('[data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	$("#btnSearch").prop('disabled', true);
	ajaxAsync('/webPage/ecmd/Cmd3100Servlet', ajaxData, 'json',successGetSql_Qry);
}

function successGetSql_Qry(data) {
	$(".loding-div").remove();
	if(data.length < 1) dialog.alert('검색 결과가 없습니다.');
	if(data.length > 0) {		
		$('#btnExcel').prop('disabled', false);
	}else {
		$('#btnExcel').prop('disabled', true);
	}
	$("#btnSearch").prop('disabled', false);
	orgProgData = data;
	if (orgProgData[0].errmsg != null && orgProgData[0].errmsg != '' && orgProgData[0].errmsg != undefined) {
		dialog.alert(orgProgData[0].errmsg);
	}
	progFilter();
}

function progFilter() {
	
	mainGrid.setData([]);
	
	progData = orgProgData;
	var findProg = $('#txtProg').val().trim();
	var txtProg = '';
	progData = progData.filter(function(data) {		
		if (getSelectedIndex('cboSta')>0) {
			if (data.progsta != getSelectedVal('cboSta').value) return false;
		} 
		if (findProg.length>0) {
			txtProg = data.cr_rsrcname;
			if (findProg.length>0 && txtProg.indexOf(findProg)<0) return false;
		}
		return true;
	});
	mainGrid.setData(progData);
}
function openWindow(reqCd,itemId,etcinfo) {
	var nHeight, nWidth, url, winName;
	var simpleSw = true;
	
	if ( ('proglist_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = 'proglist_'+reqCd; 

	var f = document.popPam;   		

	if (reqCd == 'PROGINFO') {
		selectedGridItem = mainGrid.list[mainGrid.selectedDataIndexs];
		f.syscd.value = getSelectedVal('cboSysCd').value;
		f.sysmsg.value = getSelectedVal('cboSysCd').cm_sysmsg;
		f.rsrccd.value = selectedGridItem.cr_rsrccd;
		f.rsrcname.value = selectedGridItem.cr_rsrcname;
		f.dirpath.value = selectedGridItem.cm_dirpath;
		f.jobcd.value = selectedGridItem.cr_jobcd;
		cURL = "/webPage/winpop/PopProgramInfo.jsp";
	} else if (reqCd == 'SRCVIEW') {
		if (etcinfo != null && etcinfo != '' && etcinfo != undefined) {
			if (etcinfo.substr(26,1) == '1') simpleSw = false; 
		}
		if (simpleSw) cURL = "/webPage/winpop/PopSourceView.jsp";	
		else cURL = "/webPage/winpop/PopSourceViewInf.jsp";
	} else if (reqCd == 'SRCDIFF') {
		if (etcinfo != null && etcinfo != '' && etcinfo != undefined) {
			if (etcinfo.substr(26,1) == '1') simpleSw = false; 
		}
		if (simpleSw) cURL = "/webPage/winpop/PopSourceDiff.jsp";
		else cURL = "/webPage/winpop/PopSourceDiffInf.jsp";
	} else {
		dialog.alert('window open - popup: invalid type ['+reqCd+'] error', function(){return;});
	}
	
	f.acptno.value	= '';    	    
	f.user.value 	= userId;    	
	f.itemid.value	= itemId;    	

    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}