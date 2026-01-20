var forObj = window.parent.tmpObj;	

var firstGrid			= new ax5.ui.grid();
var fortifyDetailModal	= new ax5.ui.modal();

var firstGridData 	= [];
var popSelItem		= [];

var strAcptNo = forObj.AcptNo;
var strItemId = forObj.ItemId;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
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
        onDBLClick : function () {
        	if(this.dindex < 0) return;
        	var name = this.item.SCA_CATEGORY_NAME;
        	var story = this.item.SCA_CATEGORY_EXPLANATION;
        	var detail = this.item.SCA_CATEGORY_RECOMMEND;
        	fortifyDetailModal.open({
    	        width: 750,
    	        height: 640,
    	        iframe: {
    	            method: "get",
    	            url: "../../modal/request/FortifyDetailModal.jsp",
    	        },
    	        onStateChanged: function () {
    	            if (this.state === "open") {
    	            	popSelItem.name = name;
    	            	popSelItem.story = story;
    	            	popSelItem.detail = detail;
    	                mask.open();
    	            }
    	            else if (this.state === "close") {
    	                mask.close();
    	            }
    	        }
    	    }, function () {
    	    });
        	
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "PRO_FILENAME", 			label: "파일명",  	width: '30%', align: 'left'},
        {key: "SCA_CATEGORY_NAME",		label: "취약점명",  	width: '40%', align: 'left'},
        {key: "SCA_ISSUE_LEVEL",		label: "중요도",  	width: '10%'},
        {key: "SCA_ISSUE_LINE_NUMBER",	label: "LINE",  	width: '20%', align: 'right'},
    ]
});

$(document).ready(function() {
	getFortifyResult();

	// 닫기
	$('#btnClose').bind('click', function() {
		popClose();
	});
});

function getFortifyResult() {
	var data = new Object();
	data = {
		AcptNo : strAcptNo,
		ItemID : strItemId,
		requestType : 'getFortify_Result'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json', successGetFortifyResult);
}

function successGetFortifyResult(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

// 모달 닫기
function popClose() {
	window.parent.fortifyResultModal.close();
}