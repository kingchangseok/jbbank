var userId 	  = window.parent.userId;
var parentvar = window.parent.devReqUserSelectGbn;
var parentArr = window.parent.devReqUserSelectArr;

var firstGrid 	= new ax5.ui.grid();
var secondGrid 	= new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];

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
        	if (this.dindex < 0) return;
        	
        	for(var i=0; i<secondGridData.length; i++) {
        		if(secondGridData[i].cm_userid == this.item.cm_userid) {
        			dialog.alert('동일한 인물을 중복 등록할 수 없습니다.');
        			return;
        		}
        	}
        	
        	var addData = new Object();
        	addData = this.item;
        	addData.newUser = 'true';
        	secondGridData = secondGrid.getList();
        	secondGridData.push(addData);
        	secondGrid.setData(secondGridData);
        	addData = null;
        	
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_userid",      label: "ID",   	width: '25%'},
        {key: "cm_posname",     label: "직무",  	width: '25%'},
        {key: "cm_username",	label: "성명",  	width: '25%'},
        {key: "cm_deptname",    label: "부서",	width: '25%'} 
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
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
        	if (this.dindex < 0) return;
        	secondGrid.removeRow(secondGrid.selectedDataIndexs[0]);
        	secondGridData = secondGrid.getList();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_userid",      label: "ID",   	width: '25%'},
        {key: "cm_posname",     label: "직무",  	width: '25%'},
        {key: "cm_username",	label: "성명",  	width: '25%'},
        {key: "cm_deptname",    label: "부서",	width: '25%'} 
    ]
});

$(document).ready(function() {
	
	if(parentArr != null && parentArr != undefined && parentArr.length > 0) {
		secondGridData = clone(parentArr);
		secondGrid.setData(secondGridData);
		secondGridData = [];
	}

	//검색
	$('#btnQry').bind('click', function() {
		btnQryClick();
	});
	
	//등록
	$('#btnReg').bind('click', function() {
		window.parent.devReqUserSelectData = clone(secondGrid.getList());
		popClose(true);
	});
	
	//취소
	$('#btnCncl').bind('click', function() {
		$('#txtUserName').val('');
		popClose(false);
	});
	
	$('#txtUserName').bind('keypress',function(event){
		if(event.keyCode == 13){
			btnQryClick();
		}
	});
	
});

function popClose(flag){
	window.parent.devReqUserSelectFlag = flag;
	window.parent.devReqUserSelectModal.close();
}

function btnQryClick() {
	firstGridData = [];
	firstGrid.setData([]);
	
	var reqType = '';
	var deptCd = '';
	if(parentvar == 'Other') {
		reqType = 'OtherUserList';
	}else {
		reqType = 'getUserList';
		deptCd = parentvar;
	}
	
	var data = new Object();
	data = {
		srcWord : $('#txtUserName').val(),
		deptCd : deptCd,
		requestType	: reqType
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	console.log('[getUserList] ==>', data);
	ajaxAsync('/webPage/ecmc/UserselectServlet', data, 'json',successGetUserList);
}

function successGetUserList(data) {
	$(".loding-div").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
	
	if(firstGridData.length < 1) {
		dialog.alert('검색결과가 없습니다.');
		return;
	}
}