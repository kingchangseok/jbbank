/**
 * [보고서 > 개발/SR기준(집계)] [보고서 > 개발/SR기준(상세)]
 */
var userid = window.top.userId;
var reqCd = window.top.reqCd;

var picker = new ax5.ui.picker();
var mainGrid = new ax5.ui.grid();

var columnData = null;
var SecuYn = null;

$('[data-ax5select="datsStd"]').ax5select({
	options : []
});
$('[data-ax5select="srStat"]').ax5select({
	options : []
});
$('[data-ax5select="reqDept"]').ax5select({
	options : []
});
$('[data-ax5select="devDept"]').ax5select({
	options : []
});

$('#datStD').val(getDate('DATE',-1));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

mainGrid.setConfig({
	header : {
		align : "center"
	},
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	body : {
		columnHeight : 24,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		trStyleClass : function() {
			if (this.item.gbn === 'T') {
				return "font-blue";
			} else if (this.item.gbn === 'S') {
				return "font-red";
			}
		}
	},
	onDataChanged : function() {
		this.self.repaint();
	},
	columns : columnData
});

$(document).ready(function() {
	// 엑셀저장
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		if (reqCd == '01' || reqCd == '03') {
			mainGrid.exportExcel("개발/SR기준(집계) " + today + ".xls");
		} else {
			mainGrid.exportExcel("개발/SR기준(상세) " + today + ".xls");
		}
	})

	if (reqCd == '01' || reqCd == '03') {
		columnData = [ 
			{key : "ownerdept",	label : "요청본부",	align : "center",	width : "10%"}, 
			{key : "dept1",		label : "요청부서",	align : "center",	width : "10%"}, 
			{key : "dept2",		label : "개발부서",	align : "center",	width : "10%"}, 
			{key : "sysmsg",	label : "시스템",		align : "center",	width : "15%"}, 
			{key : "chkDate",	label : "기간",		align : "center",	width : "15%"}, 
			{key : "CntSum",	label : "합계",		align : "right",	width : "39%"} 
		];
	} else {
		columnData = [ 
			{key : "ownerdept",	label : "요청본부",	align : "center",	width : "7%"}, 
			{key : "dept1",		label : "요청부서",	align : "center",	width : "7%"}, 
			{key : "dept3",		label : "접수부서",	align : "left",		width : "7%"}, 
			{key : "srid",		label : "SR-ID",	align : "center",	width : "8%"}, 
			{key : "reqtitle",	label : "요청제목",	align : "left",		width : "23%"}, 
			{key : "reqdate",	label : "요청일",		align : "center",	width : "7%"}, 
			{key : "srtype",	label : "분류유형",	align : "center",	width : "7%"}, 
			{key : "dept2",		label : "개발부서",	align : "center",	width : "7%"}, 
			{key : "sysmsg",	label : "시스템",		align : "center",	width : "10%"}, 
			{key : "chkDate",	label : "기간",		align : "center",	width : "7%"}, 
			{key : "CntSum",	label : "합계",		align : "right",	width : "10%"} 
		];
	}

	isAdmin();
	comboSet();
})

// 콤보 데이터 셋
function comboSet() {
	// 조회구분, SR상태
	var codeInfos = getCodeInfoCommon([ new CodeInfo('RPTPRGGB', '', 'N'),
										new CodeInfo('ISRSTA', 'ALL', 'N') ]);
	cboGbData = codeInfos.RPTPRGGB;
	cboSrData = codeInfos.ISRSTA;

	codeList = null;
	$('[data-ax5select="dateStd"]').ax5select({
		options : injectCboDataToArr(cboGbData, 'cm_micode', 'cm_codename')
	});
	$('[data-ax5select="srStat"]').ax5select({
		options : injectCboDataToArr(cboSrData, 'cm_micode', 'cm_codename')
	});

	// 개발부서
	var ajaxData = new Object();
	ajaxData = {
		selMsg		: 'ALL',
		cm_useyn 	: 'Y',
		gubun 		: 'DEPT',
		itYn 		: 'N',
		requestType : 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', ajaxData, 'json', successGetDeptCbo);

	// 요청부서
	var ajaxData = new Object();
	ajaxData = {
		selMsg 		: 'ALL',
		cm_useyn 	: 'Y',
		gubun 		: 'DEPT',
		itYn 		: 'N',
		requestType : 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', ajaxData, 'json', successGetDeptCbo2);
}

function successGetDeptCbo(data) {
	var cboDeptData = data;
	if (cboDeptData != null && (cboDeptData.length > 0)) {
		cboOptions = [];
		$.each(cboDeptData, function(key, value) {
			cboOptions.push({
				value : value.cm_deptcd,
				text  : value.cm_deptname
			});
		})

		$('[data-ax5select="dept"]').ax5select({
			options : cboOptions
		});
	}
}

function successGetDeptCbo2(data) {
	var cboDeptData = data;
	if (cboDeptData != null && (cboDeptData.length > 0)) {
		cboOptions = [];
		$.each(cboDeptData, function(key, value) {
			cboOptions.push({
				value : value.cm_deptcd,
				text  : value.cm_deptname
			});
		})

		$('[data-ax5select="dept"]').ax5select({
			options : cboOptions
		});
	}
}

function isAdmin() {
	var ajaxData = {
		requestType : 'isAdmin',
		userId 		: userid
	}
	ajaxResultData = ajaxCallWithJson('/webPage/common/UserInfoServlet', ajaxData, 'json');
	SecuYn = ajaxResultData ? "Y" : "N";
}

$("#btnSearch").bind('click', function() {
			var inputData = {
				dateGbn : getSelectedVal('dateStd').value,
				strDate : replaceAllString($("#datStD").val(), '/', ''),
				endDate : replaceAllString($("#datEdD").val(), '/', ''),
				deptGbn : "D",
				dept1	: getSelectedIndex('reqDept') > 0 ? getSelectedVal('reqDept').value : "",
				dept2 	: getSelectedIndex('devDept') > 0 ? getSelectedVal('devDept').value : "",
				Sta 	: getSelectedVal('srStat').value,
				srid 	: $("#srId").val() == '' ? null : $("#srId").val()
			}

			ajaxData = {
				inputData 	: inputData,
				userId		: userid,
				reqCd 		: reqCd,
				secuYn 		: SecuYn,
				requestType : "getProgCnt"
			}

			var ajaxResult = ajaxCallWithJson('/webPage/ecmp/Cmp1400Servlet', ajaxData, 'json');
			mainGrid.setData(ajaxResult);
})