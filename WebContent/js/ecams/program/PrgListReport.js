var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;

var mainGrid	= new ax5.ui.grid();
var cboStaData	= null;	
var cboRsrcData = null;
var SecuYn = adminYN ? 'Y' : 'N';

var sysCd = "";

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});
$('[data-ax5select="conditionSel1"]').ax5select({
    options: []
});
$('[data-ax5select="conditionSel2"]').ax5select({
    options: []
});
$('[data-ax5select="rangeSel"]').ax5select({
    options: []
});
$('[data-ax5select="prgStatusSel"]').ax5select({
    options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$('input:radio[name=rdoGbn1]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name=rdoGbn2]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

var columnData = 
	[ 
		{key : "opendate",		label : "등록일자",		align : "center",	width: "6%"}, 
		{key : "cr_rsrcname",	label : "프로그램명",		align : "left",		width: "13%"}, 
		{key : "sta",        	label : "상태",			align : "center",	width: "6%"}, 
		{key : "deptname",      label : "관리팀",			align : "center",	width: "6%"}, 
		{key : "job",        	label : "업무",			align : "left",		width: "8%"}, 
		{key : "cr_story",   	label : "프로그램설명",		align : "left",		width: "13%"},
		{key : "rsrccd",     	label : "프로그램구분",		align : "left",		width: "6%"}, 
		{key : "compile",		label : "컴파일모드",		align : "left",		width: "5%"}, 
		{key : "creator",		label : "등록텔러",		align : "center",	width: "5%"}, 
		{key : "creatorname",	label : "등록텔러성명",		align : "center",	width: "6%"},
		{key : "cr_lastdate",	label : "최근적용일자",		align : "center",	width: "8%"},
		{key : "cr_editor",		label : "최근적용텔러",		align : "center",	width: "6%"},
		{key : "cm_username",	label : "최종적용텔러성명",	align : "center",	width: "6%"},
		{key : "language",		label : "언어",			align : "center",	width: "6%"},
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
			openWindow('PROGINFO', this.item.cr_itemid,this.item.cm_info);
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
	$('#conditionSel1').bind('change', function() {
		conditionSel1_Change();
	});

	//조건선택1 하위
	$('#prgStatusSel').bind('change', function() {
		btnSearch_click();
	});
	
	//조건선택2
	$('#conditionSel2').bind('change', function() {
		conditionSel2_Change();
	});
	
	//조회 이벤트 시
	$("#btnSearch").bind('click', function() {
		btnSearch_click();
	});

	//엑셀저장
	$("#btnExcel").on('click', function() {
		mainGrid.exportExcel(userId+'_PrgListReport.xls');
	});

	//엔터키
	$("#conditionText").bind('keypress', function(event) {
		if(window.event.keyCode == 13) btnSearch_click();
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
		if (data.cm_sysinfo.substr(0,1) == '1') return false;
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
	$('[data-ax5select="prgStatusSel"]').ax5select("disable");
	//$("#conditionText").prop("disabled", true);
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('CMR0020',  'ALL','N','1','')
	]);	
	cboStaData = codeInfos.CMR0020;
	
	//조건선택1
	$('[data-ax5select="conditionSel1"]').ax5select({
		options: [
			{value: "0", text: "선택하세요"},
			{value: "1", text: "프로그램종류"},
			{value: "2", text: "프로그램상태"}
		]
	});
	$('[data-ax5select="conditionSel1"]').ax5select('setValue','0',true);
	conditionSel1_Change();
	
	//조건선택2
	$('[data-ax5select="conditionSel2"]').ax5select({
		options: [
			{value: "0", text: "선택하세요"},
			{value: "1", text: "프로그램명"},
			{value: "2", text: "프로그램설명"},
			{value: "3", text: "최종변경자"},
			{value: "4", text: "프로그램경로"}
		]
	});
	$('[data-ax5select="conditionSel2"]').ax5select('setValue','1',true);
	$('#conditionLabel').text(getSelectedVal('conditionSel2').text);
	
	//범위
	$('[data-ax5select="rangeSel"]').ax5select({
		options: [
			{value: "0", cm_macode: "OPTION", text: "전체"},
			{value: "1", cm_macode: "OPTION", text: "신규중 제외"},
			{value: "2", cm_macode: "OPTION", text: "신규중"},
			{value: "3", cm_macode: "OPTION", text: "폐기분 제외"},
			{value: "4", cm_macode: "OPTION", text: "폐기분"}
		]
	});
	$('[data-ax5select="rangeSel"]').ax5select('setValue','0',true);
}

function cboSysCd_Change() {
	cboRsrcData = [];
	mainGrid.setData([]);
	selectedIndex = getSelectedIndex('cboSysCd');
	selectedItem = getSelectedVal('cboSysCd');

	if (selectedIndex < 0) return;
	if(selectedIndex < 1) {
		$('[data-ax5select="cboJob"]').ax5select({
			options: []
		});
		$("#chkDetail").wCheck("check", true);
	} else {
		if( selectedItem == null ) return;
		getJobInfo(selectedItem.cm_syscd);  //업무 리로딩
		$("#chkDetail").wCheck("check", false);
	}
	
	if (getSelectedVal('conditionSel1') == '1') {
		$('[data-ax5select="conditionSel1"]').ax5select({
	        options: []
		});
	}
	
	//프로그램유형 가져오기
	ajaxData = {
		L_Syscd 	: getSelectedVal('cboSysCd').value,
		Index 		: '1',
		requestType	: 'getCode'
	}
	ajaxAsync('/webPage/ecmd/Cmd3100Servlet', ajaxData, 'json',successGetCode);
}

function successGetCode(data) {
	cboRsrcData = data;
	conditionSel1_Change();
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

function conditionSel1_Change() {
	if (getSelectedIndex('conditionSel1')<1) {
		$('[data-ax5select="prgStatusSel"]').ax5select("disable");
		$("#prgStatusLabel").text("");
	} else {
		$('[data-ax5select="prgStatusSel"]').ax5select("enable");
		
		cboOptions = [];
		if (getSelectedVal('conditionSel1').value=='2') {
			$.each(cboStaData,function(key,value) {
				cboOptions.push({value: value.cm_micode, text: value.cm_codename});
			});
			$("#prgStatusLabel").text("프로그램상태");
		} else {
			$.each(cboRsrcData,function(key,value) {
				cboOptions.push({value: value.cm_micode, text: value.cm_codename});
			});
			$("#prgStatusLabel").text("프로그램종류");
		}
		$('[data-ax5select="prgStatusSel"]').ax5select({
	        options: cboOptions
		});
		if (getSelectedIndex('conditionSel1')==2) $('[data-ax5select="prgStatusSel"]').ax5select('setValue','5',true);
		else $('[data-ax5select="prgStatusSel"]').ax5select('setValue','ALL',true);
		
		if (getSelectedIndex('cboSysCd')<0) return;
		
		//btnSearch_click();
	}
}

function conditionSel2_Change() {
	$('#conditionText').val('');
	if (getSelectedIndex('conditionSel2') < 0) {
		$('#conditionText').prop('disabled',true);
	}else {
		$('#conditionText').prop('disabled',false);
		$('#conditionLabel').text(getSelectedVal('conditionSel2').text);
	}
}

function btnSearch_click() {
	if (getSelectedIndex('cboSysCd') < 1) {
		setTimeout(function() {
			dialog.alert('시스템을 선택하여 주시기 바랍니다.',function(){});
		}, 10);
		return;
	} else if (getSelectedIndex('cboSysCd') == 0) {
		if(getSelectedIndex('conditionSel2') == 0) {
			dialog.alert('전체를 조회하실 때는 조건선택2를 선택하시고 검색어를 3자리이상 입력하셔야 합니다.');
			$('#conditionText').focus();
			return;
		} else if ($('#conditionText').val().length < 3) {
			dialog.alert('전체를 조회하실 때는 검색어를 3자리이상 입력하셔야 합니다.');
			$('#conditionText').focus();
			return;
		}
	}
	
	mainGrid.setData([]);
	var inputData = new Object();
	var tmpObj = {};
		tmpObj.UserId  = userId;
		tmpObj.SecuYn  = SecuYn;
		tmpObj.L_SysCd = getSelectedVal('cboSysCd').cm_syscd;
		tmpObj.L_JobCd = getSelectedVal('cboJob').value;
		tmpObj.Cbo_Cond10_code = getSelectedVal('conditionSel1').value;
		tmpObj.Cbo_Cond11_code = getSelectedIndex('conditionSel2') > 0 ? getSelectedVal('conditionSel2').value : "";
		tmpObj.Cbo_Cond2_code = getSelectedIndex('prgStatusSel') > 0 ? getSelectedVal('prgStatusSel').value : "";
		tmpObj.Txt_Cond = $("#conditionText").val().trim().length > 0 ? $("#conditionText").val().trim() : "";
		tmpObj.Opt_Qry4 = $("#Opt_Qry4").is(':checked');
		tmpObj.Opt_Qry5 = $("#Opt_Qry5").is(':checked');
		tmpObj.Opt_Qry1 = $("#Opt_Qry1").is(':checked');
		tmpObj.Opt_Qry2 = $("#Opt_Qry2").is(':checked');
		tmpObj.Chk_Aply = $("#chkDetail").is(':checked');
	
	ajaxData = {
		etcData 	: tmpObj,
		requestType : "getSql_Qry"
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
	mainGrid.setData(data);
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