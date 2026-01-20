/**
  [보고서] 파일대사 전체조회
*/

var userId 		= window.top.userId;
var adminYN		= window.top.adminYN;
var rgtList	    = window.top.rgtList;

var mainGrid	= new ax5.ui.grid();

var tmpInfo = {};
var options = [];
var gridData = [];
var columns = [];
var diffrst = [];

mainGrid.setConfig({
    target: $('[data-ax5grid="mainGrid"]'),
    sortable: true,
    multiSort: true,
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
    	onDataChanged: function(){
    		this.self.repaint();
    	},
    	trStyleClass: function(){
    		if(this.item.cm_sysmsg == '전 업무 확인필요'){
    			return 'fontStyle-ing';
    		}
    	}
    },
    columns: columns
});

$(document).ready(function() {
	getCodeInfo();	
	//조회
	$("#btnQry").on('click', function() {
		btnQry_click();
	});
	
	//엑셀저장
	$("#btnExcel").on('click', function() {
		console.log('gridData',mainGrid.list.length);
		if (mainGrid.list.length == 0) return;
		excelExport(mainGrid,"파일대사전체조회_" + userId + ".xls");
	});
});


function btnQry_click() {
	
	tmpInfo = {
		UserId		: userId,
		syscd		: '',
		requestType	: 'DaesaResult2'
	}

	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp2700Servlet', tmpInfo, 'json', successGetDaesa);
}

function successGetDaesa(data){
	$(".loding-div").remove();
	console.log('successGetDaesa',data);
	gridData = data;
	
	
	var tmpObj = {};
	var tmpArr = [];
	var col = '';
	
	for(var i = 0; i < gridData.length; i++){
		tmpObj = {};
		for(var j = 0; j < mainGrid.columns[0].length; j++){
			col = mainGrid.columns[j].label;
			if(col != 'cd_diffrst' && col != 'cm_codename'){
				tmpObj[col] = gridData[i].col;
			}
		}
		tmpArr.push(tmpObj);
	}
	
	gridData = tmpArr;
	mainGrid.setData(gridData);
}
function getCodeInfo(){
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('DIFFRST','','N')
		]);
	
	diffrst = codeInfos.DIFFRST;
		
	if(diffrst.length > 0){
		titSet();
	}
}

function titSet(){
	
	columns = [
		{key: "cd_svrip",		label: "서버",		width: '10%',  align: "left"},
		{key: "cd_svrname",		label: "서버명",		width: '8%',   align: "left"},
		{key: "cm_sysmsg",		label: "시스템",		width: '15%',  align: "left"}
	];
	
	for(var i = 0; i < diffrst.length; i++){
		columns.push({key: "ResultCnt"+diffrst[i].cm_micode, label: diffrst[i].cm_codename, width: '8%', align: "left"});
	}
	
	mainGrid.setConfig({columns : columns});
	
	btnQry_click();
}





