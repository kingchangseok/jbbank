var userId 			= window.parent.userId;

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var picker 			= new ax5.ui.picker();

var firstGridDataOri= [];
var firstGridData 	= [];
var exportData		= []; //to ReqCheckModal

var selectedAcptNo	= '';

$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    page: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.select(this.dindex);
        	firstGridClick();
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	btnRegClick();
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    },
    columns: [
    	{key: "cm_sysmsg",   	label: "시스템",			width: '20%',  	align: 'left'},
    	{key: "cr_acptno",    	label: "신청번호",			width: '15%',  	align: 'center'},
    	{key: "cr_acptdate",    label: "신청일",			width: '15%',  	align: 'center'},
    	{key: "cr_sayu",    	label: "해당 신청건 사유",	width: '50%',  	align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    page: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    },
    columns: [
    	{key: "cc_attfile",   	label: "파일명",		width: '33%',  	align: 'left'},
    	{key: "cm_username",    label: "첨부인",		width: '33%',  	align: 'center'},
    	{key: "cr_acptdate",    label: "첨부날짜",		width: '33%',  	align: 'center'}
    ]
});

$(document).ready(function(){
	//조회
	$('#btnQry').bind('click',function(){
		btnQryClick();
	});
	
	//등록
	$('#btnReg').bind('click',function(){
		btnRegClick();
	});
	
	//취소
	$('#btnCncl').bind('click',function(){
		popClose(false);
	});
});

function btnQryClick() {
	var paramData = new Object();
	paramData = {
			UserId : userId,
			StDate : $('#datStD').val(),
			EdDate : $('#datEdD').val(),
		requestType :'getBeforeFiles'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', paramData, 'json', successGetBeforeFiles);
}

function successGetBeforeFiles(data) {
	$(".loding-div").remove();
	
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data.length == 0) {
		dialog.alert('조회 결과가 없습니다.');
		return;
	}
	
	firstGridDataOri = data;
	firstGridData = [];
	var currentObj = [];
	var beforeObj = [];
	
	for(var i=1; i<firstGridDataOri.length; i++) {
		currentObj = firstGridDataOri[i];
		beforeObj = firstGridDataOri[i-1];
		if(beforeObj.cr_acptno != currentObj.cr_acptno) {
			firstGridData.push(beforeObj);
			if(i+1 == firstGridDataOri.length) {
				firstGridData.push(currentObj);
			}
		}
	}
	
	firstGrid.setData(firstGridData);
}

function btnRegClick() {
	if(firstGrid.getList('selected').length < 1) return;
	
	var selItem = firstGrid.getList('selected')[0];
	for(var i=0; i<firstGridDataOri.length; i++) {
		if(selItem.cr_acptno == firstGridDataOri[i].cr_acptno) {
			firstGridDataOri[i].isCopy = 'true';
			exportData.push(firstGridDataOri[i]);
		}
	}
	
	popClose(true);
}

function firstGridClick() {
	if(firstGrid.getList('selected').length < 1) {
		$('#btnReg').prop('disabled',true);
		return;
	}
	
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	$('#btnReg').prop('disabled',false);
	selectedAcptNo = firstGrid.getList('selected')[0].cr_acptno;
	setFilesData();
}

function setFilesData() {
	$(".loding-div").remove();
	
	var tmpArr = clone(firstGridDataOri);
	tmpArr = tmpArr.filter(function(item) {
		if(item.cr_acptno == selectedAcptNo) return true;
		else return false;
	});
	secondGrid.setData(tmpArr);
}

function popClose(gbn){
	window.parent.reqCheckFileModalFlag = gbn;
	if(gbn) window.parent.reqCheckFileModalFiles = clone(exportData);
	window.parent.reqCheckFileModal.close();
}