var userId	 = window.parent.userId;
var fileList = window.parent.fileList;

var firstGrid  = new ax5.ui.grid();

var firstGridData 	= [];

var diffCol = [
	{key: "cm_dirpath", 	label: "디렉토리",		width: '30%', 	align: 'left'},
	{key: "cr_rsrcname",	label: "프로그램",  	width: '15%',	align: 'left'},
	{key: "cm_username",	label: "최종수정인",	width: '7%', 	align: 'left'},
	{key: "lastdt",		 	label: "최종수정일",	width: '8%', 	align: 'left'},
	{key: "chkrst",		 	label: "확인결과",		width: '10%', 	align: 'left'},
	{key: "view_dirpath",	label: "로컬경로",		width: '30%', 	align: 'left'}
]

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: false, 
    multiSort: false,
    showRowSelector: true, 
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{key: "cm_dirpath", 	label: "로컬경로",		width: '40%', align: 'left'},
    	{key: "cr_rsrcname",	label: "파일명",  	width: '30%', align: 'left'},
    	{key: "error",		 	label: "결과",		width: '30%', align: 'left'}
    ]
});

$('input:radio[name="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function() {
	$('#btnReplay').prop("disabled", true);
	fileCompare_NEW(fileList);
	
	// 재처리
	$('#btnReplay').bind('click',function(){
		fileCompare_NEW(firstGridData);
	});
	
	// 적용
	$('#btnAdd').bind('click',function(){
		btnAdd_click();
	});
	
	// 확인
	$('#btnReq').bind('click',function(){
		btnReq_click();
	});
	
	// 취소
	$('#btnCancel').bind('click',function(){
		btnCancel_click();
	});
	
	// 닫기
	$('#btnClose').bind('click',function(){
		popClose(false);
	});
	
});

function fileCompare_NEW(list) {
	if(list.length > 0) {
		var etcData = new Object();
		etcData.userId = userId;
		
		var tmpData = {
			etcData : etcData,
			fileList: list,
			requestType: 'fileCompare_NEW'
		}
		
		$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		console.log('[fileCompare_NEW] ==>', tmpData);
		ajaxAsync('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json',successFileCompate_NEW);
	}
}

function successFileCompate_NEW(data) {
	
	$(".loding-div").remove();
	if(typeof data === 'string' && data.indexOf('ERROR')>-1) {
		dialog.alert('파일비교 중 오류가 발생하였습니다.');
		return;
	}
	
	console.log('[successFileCompate_NEW] ==>', data);
	firstGridData = data;
	firstGrid.setData(firstGridData);
	
	var errSw = false;
	
	for(var i=0; i<firstGridData.length; i++) {
		if(firstGridData[i].errflag == 'ERROR') {
			errSw = true;
			break;
		}
	}
	
	if(errSw) {
		$('#btnReplay').prop("disabled", false);
		dialog.alert('형상관리서버와 불일치하는 파일이 있습니다.\n목록을 확인 후 처리하시기 바랍니다.');
		
		firstGrid.config.columns = diffCol;
		firstGrid.setConfig({
			showRowSelector: true,
			multipleSelect: true
		});
		firstGrid.setConfig();
		
		$('#divTitle').show();
		$('#divRadio').show();
		$('#divDiffBtn').show();
		
		firstGridData = data;
		firstGridData.forEach(function(item, index) {
			item.__disable_selection__ = '';
			item.__original_index = Number(item.__index);
			item.__selected__ = 'false';
			if(item.errflag == '0') {
				item.__disable_selection__ = 'true';
				item.chkrst = '정상';
				item.chkrstcd = '0';
			}
		});
		firstGrid.setData(firstGridData);
		firstGrid.clearSelect();
		
		return;
	}else {
		$('#btnClose').hide();
		window.parent.fileDiffList = [];
		window.parent.fileDiffModalClose = true;
		window.parent.fileDiffModal.close();
	}
}

function btnAdd_click() {
	var gridSelIdx = 0;
	var checkVal = $('input[name="optradio"]:checked').val();
	var checkText = '';
	
	if(firstGrid.getList().legnth == 0) {
		dialog.alert('목록에서 대상을 선택한 후 진행하시기 바랍니다.');
		return;
	}
	
	if(checkVal == 'M') checkText = 'Merge';
	else if(checkVal == 'C') checkText = 'Local Ignore';
	else checkText = 'eCAMS Ignore';
	
	var selItems = firstGrid.getList('selected');
	for(var i=0; i<selItems.length; i++) {
		gridSelIdx = selItems[i].__index;
		firstGrid.setValue(gridSelIdx, 'chkrstcd', checkVal);
		firstGrid.setValue(gridSelIdx, 'chkrst', checkText);
	}
	
	firstGrid.repaint();
}

// 확인
function btnReq_click() {
	var findSw = false;
	for(var i=0; i<firstGridData.length; i++) {
		if( firstGridData[i].errflag != '0' &&
				(firstGridData[i].chkrst == null || firstGridData[i].chkrst == undefined || firstGridData[i].chkrst == '') ) {
			findSw = true;
			break;
		}
	}
	
	if(findSw) {
		dialog.alert('구분을 선택하신 후 진행하시기 바랍니다.');
		return;
	}
	
	popClose(true);
}

// 취소
function btnCancel_click() {
	popClose(false);
}

function popClose(closeGbn) {
	window.parent.fileDiffList = clone(firstGridData);
	window.parent.fileDiffModalClose = closeGbn;
	window.parent.fileDiffModal.close();
}