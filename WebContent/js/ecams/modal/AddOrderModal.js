var userId 		= window.parent.userId;			
var parentArr	= window.parent.addOrderArr;

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];

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
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        	
        	for(var i = 0; i < secondGridData.length; i++) {
        		if(this.item.orderid == secondGridData[i].orderid) {
        			dialog.alert("동일한 업무지시서를 중복으로 등록할 수 없습니다.");
        			return;
        		}
        	}
        	
        	var addData = new Object();
        	addData = this.item;
        	secondGridData = secondGrid.getList();
        	secondGridData.push(addData);
        	secondGrid.setData(secondGridData);
        	addData = null;
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "orderid",	label: "발행번호",  	width: '45%',	align: 'left'},
        {key: "reqsub",		label: "업무지시제목", 	width: '55%',	align: 'left'}
    ]
});

secondGrid.setConfig({
	target: $('[data-ax5grid="secondGrid"]'),
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
        	secondGrid.removeRow(secondGrid.selectedDataIndexs[0]);
        	secondGridData = secondGrid.getList();
		},
		trStyleClass: function () {},
		onDataChanged: function(){
			this.self.repaint();
		}
	},
	columns: [
		{key: "orderid", 	label: "발행번호",  	width: '45%',	align: 'left'},
		{key: "reqsub",		label: "업무지시제목", 	width: '55%',	align: 'left'}
	]
});

$(document).ready(function() {
	
	if(parentArr != null && parentArr != undefined && parentArr.length > 0) {
		secondGridData = clone(parentArr);
		secondGrid.setData(secondGridData);
	}
	
	getOrders2();
	
	// 등록
	$('#btnConfirm').bind('click', function() {
		window.parent.addOrderData = clone(secondGridData);
		popClose(true);
	});
	
	// 취소
	$('#btnCncl').bind('click', function() {
		popClose(false);
	});

	// 검색
	$('#btnSearch').bind('click', function() {
		getOrders2();
	});

	// 업무지시서명 엔터
	$('#txtOrderName').bind('keypress', function(event) {
		if(event.keyCode == 13){
			getOrders2();
		}
	});
});

function getOrders2() {
	var orderName = "";
	if($('#txtOrderName').val().trim().length > 0) orderName =  $('#txtOrderName').val().trim();
	
	data = new Object();
	data = {
		orderName	: orderName,
		requestType : 'getOrders2'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetOrders2);
}

function successGetOrders2(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

// 모달 닫기
function popClose(flag) {
	window.parent.addOrderFlag = flag;
	window.parent.addOrderModal.close();
}