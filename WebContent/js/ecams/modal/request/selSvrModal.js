var userId = window.parent.pUserId;
var selSvrModalData	= window.parent.selSvrModalData;
	
var firstGrid = new ax5.ui.grid();
var firstGridData = [];

var strSysCd = selSvrModalData.syscd;
var strRsrcCd = selSvrModalData.rsrccd;
var strItemId = selSvrModalData.itemid;
$('#txtSysMsg').val(selSvrModalData.sysmsg)

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    showLineNumber: true,
    multipleSelect: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
           this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = grdBefJob.selectedDataIndexs;
	       	if(selIn.length === 0) return;
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_svrname",	label: "서버명",	width: '50%'},
        {key: "cm_svrip",	label: "서버IP",	width: '30%'},
        {key: "cm_portno",	label: "Port",	width: '20%'} 
    ]
});

$(document).ready(function(){
	// 닫기
	$('#btnClose').bind('click', function() {
		popClose(false);
	});
	
	// 초기화
	$('#btnInit').bind('click', function() {
		window.parent.svrList = [];
		popClose(false);
	});
	
	// 등록
	$('#btnReg').bind('click', function() {
		btnReg_Click();
	});
	
	getSvrList();
});

function getSvrList() {
	var data = new Object();
	data = {
		SysCd : strSysCd,
		RsrcCd : strRsrcCd,
		ItemId : strItemId,
		requestType : 'getSvrList'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successGetSvrList);
}

function successGetSvrList(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function btnReg_Click() {
	var selectedItems = firstGrid.getList('selected');
	if(selectedItems.length == 0) {
		dialog.alert('적용할 서버를 선택하여 주시기 바랍니다.');
		return;
	}
	console.log('selectedItems ==>',selectedItems);
	window.parent.svrList = clone(selectedItems);
	popClose(true);
}

function popClose(flag){
	window.parent.selSvrModalFlag = flag;
	window.parent.selSvrModal.close();
}
