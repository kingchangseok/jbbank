var mainGrid = new ax5.ui.grid();

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	sortable : true,
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            selectedItem = this.item;
//            console.log('mainGrid selectedItem:',selectedItem);
        },
	},
	columns : [
		{key : "CM_USERID",label : "사번",align : "center", width: "14%"}, 
		{key : "CM_USERNAME",label : "성명",align : "center", width: "14%"}, 
		{key : "CM_DEPTNAME",label : "팀명",align : "center", width: "14%"}, 
		{key : "POSITION",label : "직책",align : "center", width: "14%"}, 
		{key : "CM_LOGINDT",label : "로그인일시",align : "center", width: "14%"}, 
		{key : "CM_IPADDRESS",label : "접속IP",align : "center", width: "14%"}, 
		{key : "CM_TELNO1",label : "전화번호",align : "center", width: "14%"},
	]
});

$(document).ready(function() {
	getLoginUser();
	
	$("#btnSearch").bind(function() {
		getLoginUser();
	})
});

function getLoginUser() {
	var data = {
		requestType : "getLoginUser"
	};
	ajaxAsync('/webPage/administrator/WebConnectorList', data, 'json', successGetLoginUser);
}

function successGetLoginUser(data) {
	mainGrid.setData(data);
}