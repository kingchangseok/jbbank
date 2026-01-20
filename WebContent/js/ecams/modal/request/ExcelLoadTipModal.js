var userId 		= window.parent.userId;			//접속자 ID
var sysCd 		= window.parent.sysCd;
var sysMsg		= window.parent.sysMsg;

var firstGrid  	= new ax5.ui.grid();
var secondGrid  = new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];
var data          	= null;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true,
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
    },
    columns: [
        {key: "cm_jobname", label: "업무명",  width: '99%', align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true,
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
    },
    columns: [
        {key: "jawon", label: "프로그램종류",  width: '99%', align: 'left'}
    ]
});

$(document).ready(function() {

	$('#btnClose').bind('click',function() {
		popClose();
	});

	$('#txtSystem').val(sysMsg);

	var data = new Object();
	data = {
		SysCd 		: sysCd,
		requestType	: 'getJaWonList'
	}
	ajaxAsync('/webPage/ecmd/Cmd0200Servlet', data, 'json',successGetJaWonList);

	data = {
		SysCd 		: sysCd,
		requestType	: 'getJobList'
	}
	ajaxAsync('/webPage/ecmd/Cmd0200Servlet', data, 'json',successGetJobList);
	data = null;
});

function successGetJaWonList(data) {
	secondGrid.setData(data);
}
function successGetJobList(data) {
	firstGrid.setData(data);
}
function popClose(){
	window.parent.excelLoadTipModal.close();
}