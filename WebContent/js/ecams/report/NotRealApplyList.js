var userId = window.top.userId;

var mainGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();

var cboSysData 	  = [];
var cboReqDivData = [];

mainGrid.setConfig({

	target : $('[data-ax5grid="mainGrid"]'),
	sortable : true,
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : true,
	LineNumberColumWidth : 40,
	rowSelectorColumWidth : 27,
	header : {
		align : "center"
	},
	body : {
		columHeight : 24,
		onClick : function() {
			/*this.self.clearSelect();*/
			this.self.select(this.dindex);
			selectedItem = this.item;
		},
		onDataChanged : function() {
			this.self.repaint();
		}
	},
	columns : [
		{key : "acptno",		label : "신청번호",	width: "12%"}, 
		{key : "cm_sysmsg",		label : "시스템",		width: "8%"}, 
		{key : "cm_jobname", 	label : "업무",		width: "6%"}, 
		{key : "cm_deptname",  	label : "신청부서",	width: "8%",	align : "left"}, 
		{key : "cm_username",	label : "신청인",		width: "6%", 	align : "center"}, 
		{key : "cr_acptdate",	label : "신청일시",	width: "10%", 	align : "center"}, 
		{key : "cr_prcdate",	label : "완료일시",	width: "10%"},
		{key : "status",		label : "진행상태",	width: "5%"},
		{key : "requestgb",		label : "신청구분",	width: "5%"},
		{key : "cr_rsrcname",	label : "프로그램명",	width: "13%"}, 
		{key : "cm_dirpath",   	label : "디렉토리",	width: "17%"}
	]
});

$(document).ready(function(){
	screenInit();
	getSysInfo();
	getCodeInfo();
})

function screenInit() {
	$('[data-ax5select="cboSys"]').ax5select({
		option: []
	});
	$('[data-ax5select="cboJob"]').ax5select({
		option: []
	});
	$('[data-ax5select="cboDept"]').ax5select({
		option : []
	});
	$('[data-ax5select="cboSin"]').ax5select({
		option : []
	});
	$('input:radio[name=rdoOpt]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

	$("#btnSearch").on('click', function() {
		getFileList();
	});

	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
		});
	
	//달력 잠그기
	$("#dateEd").attr("disabled", true);
	disableCal(true, "dateEd");

	//시스템 change
	$('#cboSys').bind('change', function() {
		syscd_Change();
	});
	
	$("#btnSaveExl").on('click', function() {
		var today = getDate('DATE',0);
		mainGrid.exportExcel("EmgReport " + today + ".xls");
	});

	$('#dateSt').val(getDate('DATE',0));
	$('#dateEd').val(getDate('DATE',0));
	picker.bind(defaultPickerInfoLarge('basic', 'top'));
	
	var today = getDate('DATE',0);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
}

function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
			UserId : userId,
			SecuYn : 'Y',
			SelMsg : 'ALL',
			CloseYn : 'N',
			ReqCd : '',
		requestType : 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json',successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysData = data;
	options = [];
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd', 'cm_sysmsg')
	});
	
	$('[data-ax5select="cboSys"]').ax5select("setValue",'0000',true);
	syscd_Change();
}

function syscd_Change() {
	if(getSelectedIndex("cboSys") == 0){
		$('[data-ax5select="cboJob"]').ax5select({
			options: [	
				{value: "0000", text: "전체"},
			]
		});
	} else {
		data =  new Object();
		data = {
			UserID : userId,
			SysCd : getSelectedVal("cboSys").value,
			CloseYn : "N",
			SelMsg : "ALL",
			requestType	: 'getJobInfo_Rpt'
		}
		ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetJobInfo);
	}
}

function successGetJobInfo(data){
	cboJobData = data;
	if (cboJobData != null && (cboJobData.length > 0)) {
		options = [];
		$.each(cboJobData, function(key,value) {
			options.push({value: value.cm_jobcd, text: value.cm_jobname});
		});
		
		$('[data-ax5select="cboJob"]').ax5select({
	        options: options
		});
	}
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfo('CHECKIN','ALL','N')]);
	cboReqDivData = codeInfos.CHECKIN;
	
	options = [];
	$.each(cboReqDivData, function(i, value) {
		options.push({value : value.cm_micode, text : value.cm_codename});
	});
	options[0].value = '';
	
	$('[data-ax5select="cboSin"]').ax5select({
		options: options
	})
	
	getDeptInfo();
}

function getDeptInfo() {
	var ajaxData = {
		UserId : userId,
		SelMsg : 'All',
		cm_useyn : 'Y',
		gubun : 'sub',
		itYn : 'N',
		requestType : 'getTeamInfoGrid2'
	};
	ajaxAsync('/webPage/common/TeamInfoServlet', ajaxData, 'json', SuccessGetDeptInfo);
}

function SuccessGetDeptInfo(data) {
	var cboDeptData = data;
	if (cboDeptData != null && (cboDeptData.length > 0)) {
		cboOptions = [];
		$.each(cboDeptData, function(key, value) {
			cboOptions.push({
				value : value.cm_deptcd,
				text : value.cm_deptname
			});
		})

		$('[data-ax5select="cboDept"]').ax5select({
			options : cboOptions
		});
	}
}

function getFileList(){
	strStD = replaceAllString($("#dateSt").val(), "/", "");
	strEdD = replaceAllString($("#dateEd").val(), "/", "");
	
	var data = new Object();
	var tmpObj = new Object();
	
	tmpObj = {
			strSys : getSelectedVal("cboSys").value,
			strJob : getSelectedVal("cboJob").value,
			strQry : getSelectedVal("cboSin").value,
			strDept : getSelectedVal("cboDept").value,
			strStd : strStD,
			strEdD : strEdD,
			UserId : $("#txtUser").val().trim(),
			PrjName : $("#txtPrg").val().trim(),
			EndGbn : $('input[name="rdoOpt"]:checked').val()
	}
	
	data = {
			tmpList : tmpObj,
			requestType : "get_SelectList",
	}
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp0500Servlet', data, 'json',successGetFileList);
}

function successGetFileList(data){
	$(".loding-div").remove();
	mainGrid.setData(data);
}
