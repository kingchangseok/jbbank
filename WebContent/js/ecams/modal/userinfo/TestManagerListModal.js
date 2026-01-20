var firstGrid = new ax5.ui.grid();

var firstGridData = [];

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_deptname", 	label: "담당파트",  	width: '40%'},
        {key: "cm_codename",	label: "권한",  		width: '40%'},
        {key: "cm_username", 	label: "사용자",  	width: '20%'}
    ]
});

$(document).ready(function() {
	getUserRgtDept_All();
	
	$("#btnDel").bind('click', function() {
		delRgtDept();
	})
	// 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
	
});

function getUserRgtDept_All() {
	var data = {
		requestType	: 'getUserRgtDept_All'
	}
	ajaxAsync('/webPage/modal/userinfo/TestManagerListServlet', data, 'json',successGetUserRgtDept_All);
}

function successGetUserRgtDept_All(data) {
	firstGrid.setData(data);
}

function delRgtDept() {
	var rgtList = firstGrid.getList("selected");
	var data = {
		rgtList : rgtList,
		requestType	: 'delRgtDept'
	}
	ajaxAsync('/webPage/modal/userinfo/TestManagerListServlet', data, 'json',successDelRgtDept);
}

function successDelRgtDept(data) {
	if(data == "0") {
		dialog.alert("선택한 목록에 대한 삭제처리를 완료했습니다.");
		getUserRgtDept_All();
	} else {
		dialog.alert("선택한 목록에 대한 삭제처리 중 오류가 발생하였습니다.");
	}
}

function popClose() {
	window.parent.TestManagerListModal.close();
}