/**
 * [보고서 > 형상관리운영현황]
 */
var userid = window.top.userId;
var codeList = window.top.codeList; // 전체 코드 리스트
var adminYN = window.top.adminYN; // 관리자여부

var picker = new ax5.ui.picker();
var mainGrid = new ax5.ui.grid();
var dialog = new ax5.ui.dialog({
	title : "경고"
});

var columnData = [];
var strDate = '';
var endDate = '';
var SecuYn = null;
var cboGbData = null;
var cboStepData = null;
var cboDocGbData = null;

$('[data-ax5select="step1"]').ax5select({
	options : []
});
$('[data-ax5select="step3"]').ax5select({
	options : []
});
$('[data-ax5select="systemSel"]').ax5select({
	options : []
});
$('[data-ax5select="cboJob"]').ax5select({
	options: []
});
$('[data-ax5select="dateStd"]').ax5select({
	options : []
});
$('[data-ax5select="step2"]').ax5select({
	options : []
});
$('[data-ax5select="step4"]').ax5select({
	options : []
});

$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

mainGrid.setConfig({
	header : {
		align : "center"
	},
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : false,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	paging : false,
	body : {
		columnHeight : 24,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		trStyleClass : function() {
			if (this.item.step2name === undefined) {
				return "font-red";
			}
		}
	},
	onDataChanged : function() {
		this.self.repaint();
	},
});

$(document).ready(function() {

	isAdmin();
	comboSet();

	// 엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));

		mainGrid.exportExcel("형상관리운영현황 " + today + ".xls");
	});

	// 조회 클릭 시
	$("#btnSearch").bind('click', function() {
		columnSet();
	});

	$("#systemSel").bind("change", function() {
		getJobInfo(getSelectedVal("systemSel").value);
	});
})

function isAdmin() {
	var ajaxData = {
		requestType : 'isAdmin',
		userId : userid
	}
	var ajaxResultData = ajaxCallWithJson('/webPage/common/UserInfoServlet', ajaxData, 'json');
	SecuYn = ajaxResultData ? "Y" : "N";
}

function comboSet() {

	var ajaxResult = new Array();
	var comboData = new Array([], [], [], [], [], []);
	var ajaxData = new Object();
	var selectMenu = [ "systemSel", "dateStd", "step1", "step2", "step3", "step4" ];

	// 시스템
	var data = new Object();
	data = {
		UserId : userid,
		SecuYn : SecuYn == "Y" ? "N" : "Y",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
		requestType : 'getSysInfo'
	}
	ajaxResult.push(ajaxCallWithJson('/webPage/common/SysInfoServlet', data, 'json'));

	// 조회구분, 1~4단계
	var codeInfos = getCodeInfoCommon([ new CodeInfo('RPTPRGGB', 'SEL', 'N'),
										new CodeInfo('RPTSTEP1', 'SEL', 'N') ]);

	cboGbData = codeInfos.RPTPRGGB;
	cboStepData = codeInfos.RPTSTEP1;

	codeList = null;
	$('[data-ax5select="dateStd"]').ax5select({
		options : injectCboDataToArr(cboGbData, 'cm_micode', 'cm_codename')
	});

	$('[data-ax5select="step1"]').ax5select({
		options : injectCboDataToArr(cboStepData, 'cm_micode', 'cm_codename')
	});
	$('[data-ax5select="step2"]').ax5select({
		options : injectCboDataToArr(cboStepData, 'cm_micode', 'cm_codename')
	});
	$('[data-ax5select="step3"]').ax5select({
		options : injectCboDataToArr(cboStepData, 'cm_micode', 'cm_codename')
	});
	$('[data-ax5select="step4"]').ax5select({
		options : injectCboDataToArr(cboStepData, 'cm_micode', 'cm_codename')
	});

	// 콤보박스에 들어갈 데이터 세팅
	$.each(ajaxResult, function(index, value) {
		$.each(value, function(key, value2) {
			if (index == 0)
				comboData[index].push({
					value : value2.cm_syscd,
					text : value2.cm_sysmsg
				});
		})
		comboData[index][0].value = ''; // "전체"값은 공백으로 세팅

		$('[data-ax5select="' + selectMenu[index] + '"]').ax5select({
			options : comboData[index]
		});
	})

	// 단계콤보박스 디폴트 값
	$('[data-ax5select="step1"]').ax5select("setValue", '0', true);
	$('[data-ax5select="step2"]').ax5select("setValue", '0', true);
	$('[data-ax5select="step3"]').ax5select("setValue", '0', true);
	$('[data-ax5select="step4"]').ax5select("setValue", '0', true);
	$('[data-ax5select="dateStd"]').ax5select("setValue", '0', true);
}

function columnSet() {

	// 조회구분 월별일 경우 일자 자르기
	if (getSelectedVal('dateStd').value === '01') {
		strDate = replaceAllString($("#datStD").val(), '/', '').substr(0, 6);
		endDate = replaceAllString($("#datEdD").val(), '/', '').substr(0, 6);
	} else {
		strDate = replaceAllString($("#datStD").val(), '/', '');
		endDate = replaceAllString($("#datEdD").val(), '/', '');
	}

	// 유효성검사
	var stepCnt = 1;
	if (getSelectedIndex('dateStd') < 1) {
		dialog.alert("조회구분을 선택해주세요.");
		return;
	}
	if (getSelectedIndex('step1') < 1) {
		dialog.alert("1단계 조건을 선택해주세요.");
		return;
	}
	if (getSelectedIndex('step2') < 1) {
		dialog.alert("2단계 조건을 선택해주세요.");
		return;
	}
	if (getSelectedIndex('step2') > 0) {
		stepCnt = 2;
		if(getSelectedVal('step1').value == getSelectedVal('step2').value) {
			dialog.alert("1단계와 2단계 구분이 동일합니다. 다르게 선택하여 주십시오.")
			return;
		}
	}
	if (getSelectedIndex('step3') < 1) {
		dialog.alert("3단계 조건을 선택해주세요.");
		return;
	}
	if (getSelectedIndex('step3') > 0) {
		stepCnt = 3;
		if(getSelectedVal('step1').value == getSelectedVal('step3').value) {
			dialog.alert("1단계와 3단계 구분이 동일합니다. 다르게 선택하여 주십시오.")
			return;
		}
		if(getSelectedVal('step2').value == getSelectedVal('step3').value) {
			dialog.alert("2단계와 3단계 구분이 동일합니다. 다르게 선택하여 주십시오.")
			return;
		}
	}
	if (getSelectedIndex('step4') < 1) {
		dialog.alert("4단계 조건을 선택해주세요.");
		return;
	}
	if (getSelectedIndex('step3') > 0) {
		stepCnt = 4;
		if(getSelectedVal('step1').value == getSelectedVal('step4').value) {
			dialog.alert("1단계와 4단계 구분이 동일합니다. 다르게 선택하여 주십시오.")
			return;
		}
		if(getSelectedVal('step2').value == getSelectedVal('step4').value) {
			dialog.alert("2단계와 4단계 구분이 동일합니다. 다르게 선택하여 주십시오.")
			return;
		}
		if(getSelectedVal('step3').value == getSelectedVal('step4').value) {
			dialog.alert("3단계와 4단계 구분이 동일합니다. 다르게 선택하여 주십시오.")
			return;
		}
	}
	if (strDate > endDate) {
		dialog.alert("조회기간을 정확히 입력해주십시오.");
		return;
	}

	var ajaxData = {
		UserId : userid,
		SysCd : getSelectedVal('systemSel').value > 0 ? getSelectedVal('systemSel').value : "" ,
		requestType : "getRsrcCd"
	}
	
	// 컬럼에 표시할 데이터 불러오기
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp3500Servlet', ajaxData, 'json', successColumnSet);
}

function successColumnSet(ajaxResult) {
	if (ajaxResult.length <= 0) {
		dialog.alert("해당 조건에 해당하는 데이터가 존재하지 않습니다.");
		mainGrid.setConfig({});
		return;
	}
	columnData = [];
	// step선택 값에 따른 컬럼 데이터 세팅
	for (var i = 1; i <= 4; i++) {
		var stepText = getSelectedVal('step'+i).text;
		columnData.push({
			key : "step" + i + "name",
			label : stepText,
			align : "center",
			width : "7%"
		});
	}
	columnData.push({
		key : "rowhap",
		label : "합계",
		align : "right",
		width : "5%",
		styleClass : "font-red"
	});

	// 존재하는 컬럼 데이터 추가
	$.each(ajaxResult, function(index, value) {
		columnData.push({
			key : "col" + value.cm_micode,
			label : value.cm_codename,
			align : "right",
			width : "5%"
		});
	})
	gridSet();
}

function gridSet() {
	
	var ajaxData = {
		UserId : userid,
		SysCd : getSelectedVal('systemSel').value,
		Gubun : getSelectedVal('dateStd').value,
		JobCd : getSelectedIndex('cboJob') > 0 ? getSelectedVal('cboJob').value : "",
		Step1 : getSelectedVal('step1').value,
		Step2 : getSelectedVal('step2').value,
		Step3 : getSelectedVal('step3').value,
		Step4 : getSelectedVal('step4').value,
		StDate : strDate,
		EdDate : endDate,
		requestType : "getProgList"
	}

	ajaxAsync('/webPage/ecmp/Cmp3500Servlet', ajaxData, 'json', successGridSet);
}

function successGridSet(ajaxResult) {
	mainGrid.setConfig({
		footSum : null,
		columns : null
	});
	if (ajaxResult.length > 0) {
		var footSumData = [ [] ];
		var gridData = [];
		$('#btnExcel').show();

		$.each(ajaxResult, function(i, value) {
			gridData.push(value);
		});

		footSumData[0].push({
			label : "총계",
			colspan : 4,
			align : "center"
		});
		$.each(columnData, function(i, value) {
			if (i > 3) {
				footSumData[0].push({
					key : value.key,
					collector : function() {
						return ajaxResult[ajaxResult.length - 1][value.key];
					},
					align : "right"
				});
			}
		});
		// 컬럼 세팅
		mainGrid.setConfig({
			footSum : footSumData,
			columns : columnData
		});

		gridData.pop();
		// 그리드 세팅
		mainGrid.setData(gridData);
	}
}

//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(sysCd) {
	var data = new Object();
	data = {
		UserID : userid,
		SysCd : getSelectedVal('systemSel').value,
		CloseYn : 'N',
		SelMsg : 'ALL',
		requestType	: 'getJobInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successJob);
}

function successJob(data) {
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
