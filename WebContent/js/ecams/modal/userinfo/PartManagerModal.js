
var treeObj			= null;
var treeObjData		= null;
var selectedNode	= null;
var insertNode		= null;

var rgtDept	= null;
var checkedDept = null;

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onCheck: function(event, treeId, treeNode) {
			treeOnCheck(treeNode);
		}
	}
};


$(document).ready(function(){
	rMenu = $("#rMenu");
	getUserRgtDept();
	getTreeInfo();
	// 트리 노드 찾기
	$('#txtFind').bind('keydown', function(evnet) { 
		if(event.keyCode === 13) {
			findNode();
		}
	});
	// 트리 노드 찾기
	$('#btnFind').bind('click', function() { 
		findNode();
	})
	
	// 트리 전체 열기(펼치기)
	$('#btnPlus').bind('click', function() {
		treeObj.expandAll(true);
	});
	
	// 트리 전체 닫기
	$('#btnMinus').bind('click', function() {
		treeObj.expandAll(false);
	});
	
	// 적용
	$('#btnReq').bind('click', function() {
		setRgtDept();	
	});
	
	// 취소
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

function treeOnCheck(node) {
	if(node.checked) {
		checkedDept = node.cm_deptcd;
	}
}

// 노드 찾기
function findNode() {
	var txtFind = $('#txtFind').val().trim();
	var node = treeObj.getNodesByParam("name", txtFind);
	treeObj.selectNode(node[0],false,false);
}

// 트리정보 가져오기
function getTreeInfo() {
	var data = new Object();
	data = {
		chkcd : 'true',
		itsw : 'true',
		requestType	: 'getTeamInfoZTree'
	}
	ajaxAsync('/webPage/common/CommonTeamInfo', data, 'json',successGetTreeInfo);
}

// 트리정보 가져오기 완료
function successGetTreeInfo(data) {
	treeObjData = data;
	$.fn.zTree.init($("#tvOrgani"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("tvOrgani");
	
	if(rgtDept != null && rgtDept.length > 0) {
		var node;
		if(window.parent.dutyCd == "T1") {
			node = treeObj.getNodesByParam("id", rgtDept[0].cm_deptcd);
			checkedDept = rgtDept[0].cm_deptcd;
		} else {
			node = treeObj.getNodesByParam("id", rgtDept[1].cm_deptcd);
			checkedDept = rgtDept[1].cm_deptcd;
		}
		treeObj.selectNode(node[0]);
		treeObj.checkNode(node[0], true, true);
	}
}

function getUserRgtDept() {
	var data = new Object();
	data = {
			userId 		: window.parent.$('#txtUserId').val().trim(),
			requestType	: 'getUserRgtDept'
	}
	rgtDept = ajaxCallWithJson('/webPage/administrator/UserInfoServlet', data, 'json');
}

function setRgtDept() {
	var data = new Object();
	data = {
			userId 		: window.parent.$('#txtUserId').val().trim(),
			dutyCode	: window.parent.dutyCd,
			deptCode	: checkedDept,
			requestType	: 'setRgtDept'
	}
	ajaxAsync('/webPage/administrator/UserInfoServlet', data, 'json', successSetRgtDept);
}

function successSetRgtDept(data) {
	if(data != null && data != "ERR") {
		dialog.alert("사용자의 테스트담당파트 등록이 완료되었습니다.", function() {popClose()});
	}
}

// 닫기
function popClose() {
	window.parent.PartManagerModal.close();
}
