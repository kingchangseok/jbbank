/**
 * [보고서 > 개발자별현황]
 */
var mainGrid = new ax5.ui.grid();

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
	},
	columns : [ 
		{key : "userid",		label : "개발자ID",		align : "center",	width: "9%"}, 
		{key : "username",		label : "개발자명",		align : "center",	width: "9%"}, 
		{key : "deptname",		label : "연구소 실",		align : "center",	width: "9%"}, 
		{key : "Q03",			label : "신규프로그램",		align : "center",	width: "8%"}, 
		{key : "Q04",			label : "변경프로그램",		align : "center",	width: "8%"}, 
		{key : "programcnt",	label : "프로그램 총 개수",	align : "center",	width: "9%"}, 
		{key : "01",			label : "S등급",			align : "center",	width: "8%"}, 
		{key : "02",			label : "A등급",			align : "center",	width: "8%"}, 
		{key : "03",			label : "B등급",			align : "center",	width: "8%"}, 
		{key : "04",			label : "C등급",			align : "center",	width: "8%"}, 
		{key : "05",			label : "D등급",			align : "center",	width: "8%"}, 
		{key : "ratecnt",		label : "SR총 개수",		align : "center",	width: "8%"}
	]
}); 

$('[data-ax5select="dept"]').ax5select({
	options : []
});

$('[data-ax5select="rate"]').ax5select({
	options : []
});

$(document).ready(function() {
	
	comboSet();
	
	//엔터키입력시
	$("#developerId").bind('keypress', function(event) {	
		if(window.event.keyCode == 13) $("#btnSearch").trigger("click");
	});

	//엑셀저장
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		
		mainGrid.exportExcel("개발자별현황 " + today + ".xls");
	})

	//날짜버튼 클릭 시
	$(".dateBtn").bind('click', function() {
		var clicked = $(this).attr('id');
		dateSet(clicked);
	});
});

function comboSet() {
	//연구소
	var ajaxData = new Object();
	ajaxData = {
		SelMsg : 'ALL',
		cm_useyn : 'Y',
		gubun : 'DEPT',
		itYn : 'N',
		requestType : 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', ajaxData, 'json', successGetDeptCbo);
	
	//SR등급
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('DEVRATE', 'ALL', 'N')
	]);
	cboRateData = codeInfos.DEVRATE;
	
	codeList = null;
	$('[data-ax5select="rate"]').ax5select({
		options : injectCboDataToArr(cboRateData, 'cm_micode', 'cm_codename')
	});
}

function successGetDeptCbo(data) {
	console.log(data);
	var cboDeptData = data;
	if (cboDeptData != null && (cboDeptData.length > 0)) {
		cboOptions = [];
		$.each(cboDeptData, function(key, value) {
			cboOptions.push({value : value.cm_deptcd, text : value.cm_deptname});
		})
		
		$('[data-ax5select="dept"]').ax5select({
	        options: cboOptions
		});
	}
}

$("#btnSearch, #reset").bind('click', function(event) {
	var month = $("#month").text().slice(0, -1);
	var year = $("#year").text().slice(0, -1);
	if(month.length <= 1) month = '0' + month;
	
	if(this.id === "reset") {		
		$('[data-ax5select="dept"]').ax5select("setValue", '', true);
		$('[data-ax5select="rate"]').ax5select("setValue", '00', true);
		$("#developerId").val("");
	}
	
	var ajaxData = {
		requestType : 'getRowList',
		inputData : {
			date : year + month,
			dept : getSelectedVal('dept').value,
			rate : getSelectedVal('rate').value,
			devId : $("#developerId").val().trim()
		} 
	}
	
	var ajaxResult = ajaxCallWithJson('/webPage/ecmp/Cmp6100Servlet', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
});

//picker에 오늘 날짜 디폴트로 세팅
$(function() {
	var today = new Date().toISOString().substring(0,7);
	$("#year").text(today.split('-')[0] + "년");	
	$("#month").text(parseInt(today.split('-')[1]) + "월");	
})


function dateSet(value) {
	var month = parseInt($("#month").text().slice(0, -1));
	var year = parseInt($("#year").text().slice(0, -1));
	
	switch(value) {
	case 'year-prev' :
		year--;
		break;
	case 'year-next' :
		year++;
		break;
	case 'month-prev' :
		if(month == 1) {
			month = 12;
			year--;
		} else {
			month--; 			
		}
		break;
	case 'month-next' :
		if(month == 12) {
			month = 1;
			year++;
		} else {
			month++; 			
		}
		break;
	}
	$("#month").text(month + "월");
	$("#year").text(year + "년");
}