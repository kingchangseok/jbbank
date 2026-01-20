var userId 		= window.parent.userId;
var reqCd 		= window.parent.reqCd;
var excelArray	= window.parent.excelArray;
var strSrid		= window.parent.strIsrId;
var strScmUser	= window.parent.strScmUser;

var firstGrid = new ax5.ui.grid();

var firstGridData	= [];
var firstGridOkData	= [];
var firstGridErrData= [];
var testColumns 	= [];
var unitColumns 	= [];
var baseColumns		= [
	{key: "casename", 	label: "테스트케이스명",  	width: '33%', align: 'center'},
    {key: "errmsg", 	label: "체크결과",  		width: '33%', align: 'center'}
];

firstGrid.setConfig({
	target : $('[data-ax5grid="firstGrid"]'),
	sortable : true,
	multiSort : true,
	multiselect: false,
	header : {
		align : "center"
	},
	page : false,
	body : {
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		trStyleClass: function () {
     		if(this.item.errsw === 'Y'){
     			return "fontStyle-cncl";
     		} else if (this.item.errsw === 'N'){
     			return "fontStyle-ing";
     		}
     	}
	},
	columns : baseColumns
});

$('input:radio[name="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-batch').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	$('#btnQry').prop('disabled',true);
	
	for(var i=0; i<excelArray.length; i++) {
		if(excelArray[i].casename == null || excelArray[i].casename == '' || excelArray[i].casename == undefined) {
			excelArray.splice(i,1);
			i--;
		}
	}
	
	if(reqCd != '43') {
		intTest_layout();
		$('#txtMsg').val('개발검수케이스 엑셀 Data Load');
	}else {
		intUnit_layout();
		$('#txtMsg').val('단위테스트케이스 엑셀 Data Load');
	}
	
	// 등록
	$('#btnQry').bind('click',function() {
		btnQry_click();
	});
	
	// 닫기
	$('#btnExit').bind('click',function() {
		popClose(false);
	});
	
	// 정상건만 등록
	$('#chkOk').bind('click',function() {
		$('#btnQry').prop('disabled',false);
		filterGrid(firstGridData);
		firstGrid.setData(firstGridOkData);
	});
	
	// 정산건, 오류건, 전체 라이도 버튼 클릭
	$('input:radio[name="radio"]').bind('click', function() {
		filterGrid(firstGridData);
		
		var checkVal = $('input[name="radio"]:checked').val();
		if(checkVal === 'normal') {
			firstGrid.setData(firstGridOkData);
		}else if (checkVal === 'err') {
			firstGrid.setData(firstGridErrData);
		}else {
			firstGrid.setData(firstGridData);
		}
	});
});

function intTest_layout() {
	var tmpObj = new Object();
	
	testColumns = [];
	testColumns = baseColumns;
	
	tmpObj = new Object();
	tmpObj = { key : 'testid', label : '테스트ID', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'casename', label : '테스트케이스명', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'testcomdate', label : '최종등록일', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'testcomuser', label : '등록인', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'tester', label : '테스트수행인', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'testdate', label : '테스트수행일', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'testtime', label : '투입시간', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'testrstop', label : '최종결과의견', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'testrst', label : '결과', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'scmuser', label : '개발자', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'casecond1', label : '테스트조건', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'casechk1', label : '확인사항', width : '8%' }
	testColumns.push(tmpObj);
	
	firstGrid.config.columns = testColumns;
	firstGrid.setConfig();
	
	chkExcelList();
}

function intUnit_layout() {
	var tmpObj = new Object();
	
	unitColumns = [];
	unitColumns = baseColumns;
	
	tmpObj = new Object();
	tmpObj = { key : 'caseid', label : 'ID', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'casegbn', label : '구분', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'casename', label : '테스트케이스', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'caseetc', label : '입력내용', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'caserst', label : '예상결과', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'casedate', label : '테스트수행일', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'caseuser', label : '수행인', width : '8%' }
	testColumns.push(tmpObj);
	
	tmpObj = new Object();
	tmpObj = { key : 'caseresult', label : '테스트결과', width : '8%' }
	testColumns.push(tmpObj);
	
	firstGrid.config.columns = testColumns;
	firstGrid.setConfig();
	
	chkExcelList();
}

function chkExcelList() {
	var tmpData = {
		FileList : excelArray,
		ReqCd : reqCd,
		UserId : userId,
		requestType: 'chkExcelList'
	}
	
	ajaxAsync('/webPage/ecmc/Cmc0400Servlet', tmpData, 'json',successChkExcelList);
}

function successChkExcelList(data) {
	firstGridData = data;
	
	var errSw = false;
	$('#divChkOk').hide();
	for(var i=0; i<firstGridData.length; i++) {
		if(firstGridData[i].errsw == 'Y') {
			errSw = true;
			$('#divChkOk').show();
			break;
		}
	}
	
	if(!errSw) {
		$('#btnQry').prop('disabled',false);
	}
	firstGrid.setData(firstGridData);
}

function filterGrid(data) {
	firstGridOkData = data.filter(function(item) {
		if(item.errsw == null || item.errsw == '' || item.errsw == undefined) return false;
		
		if(item.errsw == 'N') return true;
		else return false;
	});
	
	firstGridErrData = data.filter(function(item) {
		if(item.errsw == null || item.errsw == '' || item.errsw == undefined) return false;
		
		if(item.errsw == 'Y') return true;
		else return false;
	});
	
}

function btnQry_click() {
	filterGrid(firstGridData);
	firstGrid.setData(firstGridOkData);
	
	if(firstGridOkData.length == 0) {
		dialog.alert('요청 할 DATA가 없습니다.');
		return;
	}
	
	var servlet = '';
	if(reqCd != '43') servlet = '/webPage/ecmc/Cmc0400Servlet';
	else servlet = '/webPage/ecmc/Cmc0300Servlet';
	
	var tmpData = {
		strSrid : strSrid,
		userid : userId,
		reqcd : reqCd,
		scmuser : strScmUser
	}	
	
	var data = {
		FileList : firstGridOkData,
		etcData : tmpData,
		requestType : 'setExcelList',
	}	
	
	ajaxAsync(servlet, data, 'json',successSetExcelList);
}

function successSetExcelList(data) {
	if(data != '0') {
		dialog.alert(data);
		$('#btnQry').prop('disabled',false);
		return;
	}
	
	dialog.alert('엑셀파일을 이용하여 테스트케이스 등록처리를 완료하였습니다.');
	popClose(true);
}

function popClose(flag) {
	window.parent.loadExcelFlag = flag;
	window.parent.loadExcelModal.close();
}