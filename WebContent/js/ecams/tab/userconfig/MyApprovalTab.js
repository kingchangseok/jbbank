var userName 	= window.top.userName;
var userId 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;

var findGrid		= new ax5.ui.grid();
var approvalGrid	= new ax5.ui.grid();

var combo_dp1 = [];

$('[data-ax5select="cboGbn"]').ax5select({
	options: []
});

//ztree 옵션 세팅
var setting = {
	view: {
		dblClickExpand: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},callback: {
		onClick : nodeClick
	}
};

findGrid.setConfig({
    target: $('[data-ax5grid="findGrid"]'),
    sortable: true,
    multiSort: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            addDataRow();
        },
        trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "TEAMNAME", 	label: "부서명", 	width: '33%', align: "left"},
        {key: "CM_USERNAME",label: "성명",  	width: '33%', align: "center"},
        {key: "POSITION", 	label: "직급",  	width: '33%', align: "center"}
    ]
});

approvalGrid.setConfig({
    target: $('[data-ax5grid="approvalGrid"]'),
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
        onDBLClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);

        	deleteDataRow();
        },
        trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: '결재(순차)'},
            {type: 2, label: '참조'},
            {type: 3, label: '결재(후열)'}
        ],
        popupFilter: function (item, param) {

        	approvalGrid.clearSelect();
        	approvalGrid.select(Number(param.dindex));

	       	var selIn = approvalGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;

        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;

        	var retType = '';
        	if(param.item.ITEM7 != '1') retType += '1';
        	if(param.item.ITEM7 != '2') retType += '2';
       		if(param.item.ITEM7 != '3') retType += '3';

       		var retString;

		    if (retType.indexOf('1')>-1){
		    	retString = (item.type == 1);
		    }
		    if (retType.indexOf('2')>-1){
		    	retString = retString | (item.type == 2);
		    }
		    if (retType.indexOf('3')>-1){
		    	retString = retString | (item.type == 3);
		    }

		    return retString;
        },
        onClick: function (item, param) {

        	contextMenuClick(item.type, param.item);
    		approvalGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
        {key: "ITEM1", 			label: "유형",  	width: '30%', align: "center"},
        {key: "TEAMNAME",		label: "소속",  	width: '20%', align: "left"},
        {key: "CM_USERNAME", 	label: "사용자명",	width: '20%', align: "center"},
        {key: "POSITION", 		label: "직급",  	width: '20%', align: "center"}
    ]
});

$('input:radio[name=rdoFind]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$(document).ready(function(){
	get_Cbo_SignNMADD();
	getUserTeamInfoTree();
//	getapprovalGridData();

	$("#upBtn").bind("click", function(){
		addDataRow();
	});

	$("#downBtn").bind("click", function(){
		deleteDataRow();
	});

	$("#btnReg").bind("click", function(){
		setUserApproval();
	});

	$("#btnDel").bind("click", function(){
		delUserApproval();
	});

	$("#upBtn2").bind("click", function(){

		var selIndex = approvalGrid.selectedDataIndexs;
		selIndex = selIndex[0];
		var selItem	 = approvalGrid.list[selIndex];
		if(selIndex === 0 ) return;

		// 그리드 포커스 이동시 index 0이면 가장아래로 가는 오류? 발생 하므로 HOME으로 이동
		var focusIn = selIndex -1 === 0 ? 'HOME' : selIndex -1;

		approvalGrid.addRow(selItem, selIndex-1, {focus: focusIn});
		approvalGrid.removeRow(selIndex+1);
	});

	$("#downBtn2").bind("click", function(){
		var selIndex = approvalGrid.selectedDataIndexs;
		selIndex = selIndex[0];
		var selItem	 = approvalGrid.list[selIndex];

		if(selIndex === approvalGrid.list.length-1 ) return;

		approvalGrid.addRow(selItem, selIndex+2, {focus: selIndex+1});
		approvalGrid.removeRow(selIndex);
	});

	$("#btnSearch").bind("click", function(){
		btnSearchClick();
	});

	$("#txtFind").bind("keypress", function(e){
		if(e.keyCode == "13"){
			$("#btnSearch").trigger("click");
		}
	});

	$("[data-ax5select='Cbo_SignNM']").bind("change", function(){
		Combo1_Click();
	});

});

function getUserTeamInfoTree(){
	tmpInfoData = new Object();
	tmpInfoData = {
		chkcd : 'false',
		itsw : 'false,',
		requestType	: 'getTeamInfoTree_zTree'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet',tmpInfoData, 'json', seccessGetUserTeamInfoTree);
}

//트리정보 가져오기 완료
function seccessGetUserTeamInfoTree(data) {
	treeObjData = data;
	$.fn.zTree.init($("#treeMenu"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("treeMenu");
}

// zTree nodeClick 이벤트
function nodeClick(event, treeId, treeNode){
	getTeamNodeInfo("",treeNode,"");
}

function getTeamNodeInfo(userName, treeNode, deptName){
	findGrid.setData([]);

	if(treeNode != "" && treeNode.id == undefined){
		return;
	}

	var data = {
		UserName : "",
		DeptCd : "",
		DeptName : "",
		requestType : 'getTeamNodeInfo'
	}

	if(userName != ""){
		data.UserName = userName;
	}

	if(treeNode != ""){
		data.DeptCd = treeNode.id;
	}

	if(deptName != ""){
		data.DeptName = deptName;
	}

	ajaxAsync('/webPage/common/TeamInfoServlet',data, 'json', seccessGetTeamNodeInfo);
}

function seccessGetTeamNodeInfo(data){
	findGrid.setData(data);
}

function addDataRow(){
	approvalGrid.clearSelect();
	var findGridSelected = findGrid.getList("selected");
	if(findGridSelected.length == 0){
		return;
	}
	var approvalGridList = approvalGrid.getList();
	var addGridData = [];
	var ckAready = true;
	$(findGridSelected).each(function(){
		var gridData = this;
		ckAready = true;
		$(approvalGridList).each(function(i){
			if(gridData.CM_USERID == this.CM_USERID){
				approvalGrid.select(i);
				ckAready = false;
				return true;
			}
		});
		if(ckAready){
			gridData.ITEM1 = "결재(순차)";
			gridData.ITEM7 = "1";
			addGridData.push($.extend({}, clone(gridData), {__index: undefined}));
		}
	});

	findGrid.clearSelect();
	approvalGrid.addRow(addGridData);
	approvalGrid.select(approvalGrid.list.length);
}

function deleteDataRow(){

	approvalGrid.removeRow("selected");
}

//등록 버튼 click
function setUserApproval(){
	if (getSelectedIndex("Cbo_SignNM") == 0 && $("#txtApprovalName").val().trim() == ""){
		dialog.alert("등록할 결재선 명칭을 입력하십시오.");
		return;
	} else if(approvalGrid.getList().length < 1){
		dialog.alert("등록할 결재자를 선택/추가한 후 등록하십시오.");
		return;
	} else if (getSelectedIndex("Cbo_SignNM") == 0){
		Tbl_Update_Sign();
	}else {
		var data = {
			UserId: userId,
			rtList : approvalGrid.getList(),
			Index : "1",
			SignName : getSelectedVal("Cbo_SignNM").text,
			requestType : 'get_myGrid2_insert'
		}

		ajaxAsync('/webPage/ecmd/Cmd1300Servlet',data, 'json', seccessGet_myGrid2_insert);
	}
}

function seccessGet_myGrid2_insert(data){
	if(data == 0){
		dialog.alert("결재선 등록이 실패 되었습니다.");
	} else {
		if(getSelectedIndex("Cbo_SignNM") == 0){

			combo_dp1.push({text:$("#txtApprovalName").val(), value:""});

			$('[data-ax5select="Cbo_SignNM"]').ax5select({
		        options: combo_dp1
		   	});
		}

		dialog.alert("결재선 등록이 완료 되었습니다.");
	}
}

// 삭제
function delUserApproval(){
		if(getSelectedIndex("Cbo_SignNM") < 1){
			dialog.alert("삭제할 결재선을 선택한 후 처리하십시오.");
			return;
		} else {
			var data = {
				USERID: userId,
				SIGNNM: getSelectedVal("Cbo_SignNM").text,
				requestType : 'get_Txt_SignNM_delete'
			}

			ajaxAsync('/webPage/ecmd/Cmd1300Servlet',data, 'json', seccessDelUserApproval);
		}
}

function seccessDelUserApproval(data){
	if (data == 0) {
		dialog.alert("삭제처리 실패!");
	}else{
		combo_dp1.splice(getSelectedIndex("Cbo_SignNM"), 1);
		$("[data-ax5select='Cbo_SignNM']").ax5select({
			options: combo_dp1
		});

		$("#signNmBox").show();
		$("#txtApprovalName").val("");
		approvalGrid.setData([]);
		$("[data-ax5select='Cbo_SignNM']").ax5select("setValue", combo_dp1[0].value);
		dialog.alert("삭제처리가 완료 되었습니다.");
//		getSecondGridData();
	}
}

function Combo1_Click(){
	$("#signNmBox").hide();
	$("#txtApprovalName").val("");

	if (getSelectedIndex("Cbo_SignNM") == 0) {
		$("#signNmBox").show();
		approvalGrid.setData([]);
	}else {
		var data = {
			UserID: userId,
			SignNM: getSelectedVal("Cbo_SignNM").text,
			requestType : 'get_Cbo_Select'
		}

		ajaxAsync('/webPage/ecmd/Cmd1300Servlet',data, 'json', seccessGet_Cbo_Selectl);
	}
}

function seccessGet_Cbo_Selectl(data){
	approvalGrid.setData(data);
}

function btnSearchClick(){

	//myGrid1.dataProvider = null;
	var DeptName;
	var UserName;

	//if (txtFind.length < 1) return;
	if ($("#txtFind").val().trim() == "" || $("#txtFind").val().trim() == null){
		dialog.alert("검색할 내용을 입력해주십시오.", function(){
			$("#txtFind").focus();
		});
	}
	else if ($("#rdoName").is(":checked"))
	{
		UserName = $("#txtFind").val().trim();
		getTeamNodeInfo(UserName,"","");
	}
	else
	{
		DeptName = $("#txtFind").val().trim();
		getTeamNodeInfo("","",DeptName);
	}
}

function Tbl_Update_Sign(){
	var data = {
		UserId: userId,
		SignNM: $("#txtApprovalName").val(),
		requestType : 'get_SignNM_cnt'
	}

	ajaxAsync('/webPage/ecmd/Cmd1300Servlet',data, 'json', seccessTbl_Update_Sign);

}

function seccessTbl_Update_Sign(data){
	if (data > 0){
		dialog.alert("결재선 명칭이 이미 존재합니다.");
		return;
	}else{
		var data = {
			UserId: userId,
			rtList: approvalGrid.getList(),
			Index: "2",
			SignName: $("#txtApprovalName").val(),
			requestType : 'get_myGrid2_insert'
		}

		ajaxAsync('/webPage/ecmd/Cmd1300Servlet',data, 'json', seccessGet_myGrid2_insert);
	}
}

function get_Cbo_SignNMADD(){
	var data = {
		UserID: userId,
		requestType : 'get_Cbo_SignNMADD'
	}

	ajaxAsync('/webPage/ecmd/Cmd1300Servlet',data, 'json', seccessGet_Cbo_SignNMADD);
}

function seccessGet_Cbo_SignNMADD(data){
	combo_dp1 = injectCboDataToArr(data, 'CM_SIGNNM' , 'CM_SIGNNM');

	$('[data-ax5select="Cbo_SignNM"]').ax5select({
        options: combo_dp1
   	});
}

function contextMenuClick(type, item){
	if(item == null || item == undefined) return;
	var objtmp = approvalGrid.list[approvalGrid.selectedDataIndexs];

	if(type == "1"){
		objtmp.ITEM1 = "결재(순차)";
		objtmp.ITEM7 = "1";

	}else if (type == "2"){
		objtmp.ITEM1 = "참조";
		objtmp.ITEM7 = "2";

	}else if (type == "3"){
		objtmp.ITEM1 = "결재(후열)";
		objtmp.ITEM7 = "3";
	}

	approvalGrid.repaint();
}