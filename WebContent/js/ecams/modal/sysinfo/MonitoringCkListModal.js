/**
 * 영향분석대상확장자정보 화면 기능 정의
 * 
 * <pre>
 * 작성자: 이성현
 * 버전: 1.0
 * 수정일: 2021 - 04 - 26
 * 
 */

var cm_typecd = window.parent.cm_typecd;
var syscd = window.parent.selectedSystem.cm_syscd;

var treeObj = null;
var treeObjData = null;

var setting = {
	check : {
		enable : true
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : clickTree,
		onRightClick: OnRightClick
	}

};

function popClose() {
	window.parent.monitorCkModal.close();
}

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	// 닫기
	$('#btnClose').bind('click', function() {
		popClose();
	});

	// 트리 전체 열기(펼치기)
	$('#btnPlus').bind('click', function() {
		treeObj.expandAll(true);
	});

	// 트리 전체 닫기
	$('#btnMinus').bind('click', function() {
		treeObj.expandAll(false);
	});

	$("#btnSearch").bind("click", function() {
		getItemInfoTree();
	});
	
	$("#btnCloseInp").bind("click", function(){
		$("#inputDiv").hide();
		mask.close();
	});
	
	$("#txtInp").bind("keyup", function(e){
		getTxtInpSize();
	});
	
	$("#btnSaveInp").bind("click", function(e){
		subUpdateItemInfo();
	});

	$("#btnSave").bind("click", function(e){
		setMonitorValues();
	});
	
	getItemInfoTree();
});

function getItemInfoTree() {

	var data = {
		cm_typecd : cm_typecd,
		requestType : 'getItemInfoTree_Ztree',
	}

	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',
			successGetItemInfoTree_Ztree);
}

function successGetItemInfoTree_Ztree(data) {
	$.fn.zTree.init($("#chkList"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("chkList");

	getMonitorValues();
}

var jobUpModalCallBack = function() {
	jobUpModal.close();
}

// 트리노드 클릭시 check box에 선택 되도록
function clickTree(event, treeId, treeNode) {
	var node = treeObj.getNodeByParam('id', treeNode.id);
	var checkStatus = node.getCheckStatus();

	if (checkStatus.checked) {
		treeObj.selectNode(node);
		treeObj.checkNode(node, false, true);
	} else {
		treeObj.selectNode(node);
		treeObj.checkNode(node, true, true);
	}
}

function getMonitorValues() {
	var dataObj = {
		cm_syscd : syscd
	}
	var data = {
		dataObj : dataObj,
		requestType : 'getMonitorValues',
	}

	ajaxAsync('/webPage/ecmm/Cmm0130Servlet', data, 'json', successGetMonitorValues);
}

function successGetMonitorValues(data) {

	var firstNode = null;
	treeObj.checkAllNodes(false);
	data.forEach(function(item, index) {
		var node = treeObj.getNodeByParam('id', item.cm_gbncd);
		if (node !== null && !node.isParent) {
			if(item.cm_reqyn == "Y"){
				node.cm_viewname = "[필수]" + node.cm_gbnname;
				node.name = node.cm_viewname;
				node.cm_reqyn = "Y";
			} else {
				node.cm_viewname = node.cm_gbnname;
				node.name = node.cm_viewname;
				node.cm_reqyn = "N";
			}
			treeObj.updateNode(node);
			
			if (firstNode == null) {
				firstNode = node;
			} else if (firstNode.pId > node.pId
					|| firstNode.cm_order > node.cm_order) {
				firstNode = node;
			}
			treeObj.selectNode(node);
			treeObj.checkNode(node, true, true);
		}
	});
	treeObj.selectNode(firstNode);
	treeObj.cancelSelectedNode(firstNode);
	if (data.length < 1) {
		treeObj.expandAll(false);
	}
}

/* 트리 노드 마우스 우클릭 이벤트 */
function OnRightClick(event, treeId, treeNode) {
	var treeId = treeNode.id;

	if (!treeNode && event.target.tagName.toLowerCase() != "button"
			&& $(event.target).parents("a").length == 0) {
		treeObj.cancelSelectedNode();

		showRMenu("root", event.clientX, event.clientY);
	} else if (!treeNode.isParent) {
		treeObj.selectNode(treeNode);
		showRMenu("node", event.clientX, event.clientY);
	}
}

/* context menu 설정 */
function showRMenu(type, x, y) {
	$("#rMenu ul").show();
	if (type == "root") {
		$("#contextmenu1").hide();
	} else {
		$("#contextmenu1").show();
	}

	y += document.body.scrollTop;
	x += document.body.scrollLeft;
	$("#rMenu").css({
		"top" : y + "px",
		"left" : x + "px",
		"visibility" : "visible"
	});

	$("body").bind("mousedown", onBodyMouseDown);
}

/* 노드 이외 영역 클릭시 context menu 숨기기 */
function onBodyMouseDown(event){ 
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		$("#rMenu").css({"visibility" : "hidden"});
	}
} 

/* context menu 숨기기 */
function hideRMenu() { 
	if ($("#rMenu")) $("#rMenu").css({"visibility": "hidden"}); 
	$("body").unbind("mousedown", onBodyMouseDown); 
}

/* 파일추출 클릭 이벤트 */
function contextmenu_click(gbn) {
	hideRMenu();
	
	if(treeObj.getSelectedNodes()[0].id == null) return;
	
	var selTreeObj = treeObj.getSelectedNodes()[0];
	
	mask.open();
	$("#inputDiv").show();
	
	$("#txtInp").val(selTreeObj.cm_gbnname);
	
	if(selTreeObj.editNameFlag){
		$("#txtInp").removeAttr("readonly");
	} else {
		$("#txtInp").prop("readonly", "readonly");
	}
	
	if(selTreeObj.cm_reqyn == "Y"){
		$("#import").wCheck("check", true);
	} else {
		$("#import").wCheck("check", false);
	}
	getTxtInpSize();
}


function getTxtInpSize(){
	var byte = getByteLength($("#txtInp").val());
	
	$("#inputCount").text(byte);
	
}

//글자수 바이트 세기
function getByteLength(s,b,i,c){
    for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?2:c>>7?2:1);
    return b;
}

function subUpdateItemInfo(){
	if(Number($("#inputCount").val()) > 200){
		dialog.alert("항목명을 200byte 이하로 입력해 주시기 바랍니다.");
		return;
	}
	
	if($("#txtInp").val().trim().length == 0){
		return;
	}

	if(treeObj.getSelectedNodes()[0].id == null) return;

	var selTreeObj = treeObj.getSelectedNodes()[0];
	
	var dataObj = {
			cm_gbncd : selTreeObj.cm_gbncd,
			cm_uppgbncd : selTreeObj.cm_uppgbncd,
			cm_seq : selTreeObj.cm_seq
	}
	
	if($("#import").is(":checked")){
		dataObj.cm_reqyn = "Y";
	} else {
		dataObj.cm_reqyn = "N";
	}
	dataObj.cm_syscd = syscd;

	var data = {
		dataObj : dataObj,
		requestType : 'updateItemInfo',
	}

	ajaxAsync('/webPage/ecmm/Cmm0130Servlet', data, 'json', successUpdateItemInfo);
	
}

function successUpdateItemInfo(data){
	dialog.alert("항목 정보가 업데이트 되였습니다.");
	$("#inputDiv").hide();
	mask.close();
	getItemInfoTree();
}

function setMonitorValues(){

	var dataObj = {
		cm_syscd : syscd
	}

	var checkedNodes= treeObj.getCheckedNodes(true);
	var chkList = [];
	
	checkedNodes.forEach(function(item, index) {
		/*if(!item.isParent) {
			menuList.push(item);
		}*/
		if(item.pId  !== null) {
			var tmpItem = null;
			if(item.isParent) {
//				tmpItem = new Object();
//				tmpItem.cm_menucd = item.cm_menucd;
//				menuList.push(tmpItem);
				tmpItem = null;
			} else {
				chkList.push(item);
			}
			
		}
	})
	
	var data = {
		chkList : chkList,
		dataObj : dataObj,
		requestType : 'setMonitorValues',
	}

	ajaxAsync('/webPage/ecmm/Cmm0130Servlet', data, 'json', successSetMonitorValues);
}

function successSetMonitorValues(data){
	dialog.alert("모니터링 체크리스트가 저장 되였습니다.");
	getItemInfoTree();
}
