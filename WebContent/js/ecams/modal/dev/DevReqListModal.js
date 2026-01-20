var userId 	  = window.parent.userId;

var firstGrid 		= new ax5.ui.grid();
var firstGridData 	= window.parent.devReqStaList;

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
        {key: "cm_deptname", label: "부서명",  width: '100%'} 
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
	window.parent.devReqListModal.close();
}