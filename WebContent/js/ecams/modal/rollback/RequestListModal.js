var userId 		= window.parent.userId;		// 접속자 ID
var adminYN 	= window.parent.adminYN;	// 관리자여부

var firstGrid	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();

var data        = new Object();
var tmpData     = new Object();

var selSysCd    = window.parent.selSysCd;
var firstSw     = true;

firstGrid.setConfig({
    target: $('[data-ax5grid="first-Grid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: false,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = firstGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
        	
	       	OkPopUpClose();
        },
    	trStyleClass: function () {

    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "acptno", 	 label: '요청번호',  	width: "15%", align: "center"},
    	{key: "acptdate", 	 label: '요청일시',  	width: "15%", align: "center"},
    	/*{key: "isrid",		 label: 'ISR-ID',  	width: "15%", align: "center"},
    	{key: "isrtitle", 	 label: '요청제목',  	width: "20%", align: "center"},*/
    	{key: "cr_sayu", 	 label: '수정내용',  	width: "20%"}
    ]
});
$(document).ready(function(){
	
	$('#cmdReq').prop('disabled',true);
	
	dateInit('M');
	
	$("#cmdFind").bind('click', function() {
		cmdFind_click();
	});

	$("#cmdReq").bind('click', function() {
		OkPopUpClose();
	});
	$("#cmdExit").bind('click', function() {
		window.parent.modalCloseFlag = false;
		popClose();
	});

	var oldVal = "";
	$('#datEdD').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#datEdD").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
});

function cmdFind_click() {
	
	if (selSysCd == null || selSysCd == '' || selSysCd == undefined) return;
	
	data =  new Object();
	var strStD = replaceAllString($('#datStD').val(), '/', '');
	var strEdD = replaceAllString($('#datEdD').val(), '/', '');
	
	if ($('#datStD').prop('disabled')) {
		strStD = '';
		strEdD = '';
	}
	

	// 그리드에 loading 이미지 추가 후 보여주기
	$('[data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	data = {
			UserId      : userId,
			QryCd       : '0',
			stDate      : strStD,
			edDate      : strEdD,
			SysCd       : selSysCd,
		requestType		: 'getBefList'
	}
	console.log('getBefList=',data);
	ajaxAsync('/webPage/ecmr/Cmr0600Servlet', data, 'json', successGetBefList);
}
//조회완료
function successGetBefList(data) {
	$(".loding-div").remove();
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
	
	if (firstGridData.length == 0) return;
	if (firstGridData.length == 1) {
		if (firstGridData[0].acptno == null || firstGridData[0].acptno == '' || firstGridData[0].acptno == undefined) {
			firstGridData = [];
			firstGrid.setData([]);
		}
	}
	
	if ($('#datStD').prop('disabled')) {
		dateInit('S');
	}
	if (firstGridData.length > 0) $('#cmdReq').prop('disabled',false);
	
}
function dateInit(gbn) {
	if (gbn == 'M') {
		$('#datStD').val(getDate('DATE',0));
		$('#datEdD').val(getDate('DATE',0));
		
		$('#datStD').prop('disabled',true);
		$('#btnStD').prop('disabled',true);
		$('#datEdD').prop('disabled',true);
		$('#btnEdD').prop('disabled',true);
	} else {
		$('#datStD').val(getDate('DATE',-1));
		$('#datEdD').val(getDate('DATE',0));
		
		$('#datStD').prop('disabled',false);
		$('#btnStD').prop('disabled',false);
		$('#datEdD').prop('disabled',false);
		$('#btnEdD').prop('disabled',false);
	}
	picker.bind(defaultPickerInfoLarge('basic', 'top'));
	picker.bind(defaultPickerInfoLarge('basic2', 'top'));
	
	if (gbn == 'M') cmdFind_click();
}
function OkPopUpClose() {
	if (firstGridData.length == 0) return;

	var selIndex = firstGrid.selectedDataIndexs;
	if (selIndex.length == 0) return;
	var selIndex2 = selIndex[0];
	
	var selItem = firstGrid.list[selIndex2];
	
	window.parent.befAcptNo = selItem.cr_acptno;
	window.parent.confirmInfoData = new Object();
	window.parent.confirmInfoData.cr_sysgb = selItem.cr_sysgb;
	window.parent.confirmInfoData.sysgb = selItem.sysgb;
	window.parent.confirmInfoData.cr_syscd = selItem.cr_syscd;
	
	window.parent.modalCloseFlag = true;
	
	popClose();
	
}
function popClose() {
	
	window.parent.requestListModal.close();
	
}