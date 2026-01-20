var userId 	  = window.parent.userId;

var firstGrid 		= new ax5.ui.grid();
var firstGridData 	= window.parent.statusListData;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    multipleSelect: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_acptno", label: "신청번호",  	width: '20%'},
        {key: "cm_username", label: "신청자",  	width: '20%'},
        {key: "cr_rsrcname", label: "프로그램명",  width: '60%'}
    ]
});

$(document).ready(function() {
	console.log('firstGridData ==>',firstGridData);
	firstGrid.setData(firstGridData);
	
	$('#btnClose').bind('click', function() {
		popClose();
	});
});

function popClose(){
	window.parent.devOrderListModal.close();
}