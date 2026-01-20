/**
 * [사용자정보 > 조직도] 조직도 선택 또는 사용자선택 또는 수기조직도 관리 js
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-24
 * 
 */

var treeObj			= null;
var treeObjData		= null;
var selectedNode	= null;
var insertNode		= null;

var ulCodeData		= null;
var rMenu			= null;

var selDeptSw		= window.parent.selDeptSw;
var modiDeptSw		= window.parent.modiDeptSw;
var subSw			= window.parent.subSw;
var upDeptSw		= window.parent.upDeptSw ? true : false;
var popData			= window.parent.popData;
var dbClickSw		= false;

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},callback: {
		onRightClick: modiDeptSw ? OnRightClickTree : '',
		onDblClick	: modiDeptSw ? '' : dbClickTree
	},
	view: {
		dblClickExpand: false
	}
	
};

$(document).ready(function(){
	rMenu = $("#rMenu");
	getTreeInfo();
	if(modiDeptSw) {
		$('#titleLabel').text('[수기조직등록]');
		$('#btnReq').css('display','none');
		$('#btnExit').text('닫기');
	}
	
	if(popData != undefined && popData != null) {
		if(popData.wingbn == '1') $('#cmdDel').show();
	}
	
	// 조직 추가 (동일레벨)
	$('#addMenu').bind('click', function() {
		addMenu();
	});
	// 조직추가(하위레벨)
	$('#addSubMenu').bind('click', function() {
		addSubMenu();
	});
	// 조직 삭제
	$('#delMenu').bind('click', function() {
		delMenu();
	});
	// 조직명 변경
	$('#reMenu').bind('click', function() {
		reMenu();
	});
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
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length === 0 ) {
			dialog.alert('조직을 선택하여 주시기 바랍니다.', function(){});
			return;
		}
		
		if(subSw) {
			if(nodes[0].userId === undefined) {
				dialog.alert('사용자를 선택해 주시기 바랍니다.', function() {});
				return;
			} else {
				$('#txtUser', window.parent.document).val(nodes[0].userName);
				window.parent.txtUserId 	= nodes[0].userId;
				window.parent.txtUserName 	= nodes[0].userName;
				window.parent.deptName 		= nodes[0].deptName;
				window.parent.deptCd 		= nodes[0].pId;
			}
		} else {
			if(nodes[0].children !== undefined && !upDeptSw) {
				dialog.alert('하위 조직을 선택해 주시기 바랍니다.', function() {});
				return;
			}
			// 조직 선택시
			if(selDeptSw) {
				window.parent.selDeptCd = nodes[0].id;
				window.parent.txtOrg = nodes[0].name;
				$('#txtOrg', window.parent.document).val(nodes[0].name);
			} else {
				// 조직(겸직)선택시
				window.parent.selSubDeptCd = nodes[0].id;
				window.parent.txtOrg = nodes[0].name;
				$('#txtOrgAdd', window.parent.document).val(nodes[0].name);
			}
		}
		popClose();		
	});
	
	// 취소
	$('#btnExit').bind('click', function() {
		popClose();
	});
	
	// 삭제
	$('#cmdDel').bind('click', function() {
		window.parent.selDeptCd = '';
		window.parent.txtOrg = '';
		$('#txtOrg', window.parent.document).val('');
		popClose();
	});
});

//트리 오른쪽 클릭 이벤트
function OnRightClickTree(event, treeId, treeNode) {
	var treeId = treeNode.id;
	
	if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
		treeObj.cancelSelectedNode();
		
		showRMenu("root", event.clientX, event.clientY);
		selectedNode = treeNode;
	} else if (treeNode && !treeNode.noR) {
		treeObj.selectNode(treeNode);
		showRMenu("node", event.clientX, event.clientY);
		selectedNode = treeNode;
	}
}

//오른쪽(Context menu) 메뉴 보여주기
function showRMenu(type, x, y) {
	$("#rMenu ul").show();
	if (type=="root") {
		$("#addMenu").hide();
		$("#addSubMenu").hide();
		$("#delMenu").hide();
		$("#reMenu").hide();
	} else {
		$("#addMenu").show();
		$("#addSubMenu").show();
		$("#delMenu").show();
		$("#reMenu").show();
	}
    y += document.body.scrollTop;
    x += document.body.scrollLeft;
    rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

	$("body").bind("mousedown", onBodyMouseDown);
}

//오른쪽 메뉴 감추기
function onBodyMouseDown(event){
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
	}
}

//트리 구분 추가
function addMenu() {
	confirmDialog.setConfig({title: "XXX", theme: "danger"});
	confirmDialog.prompt({
        title: "구분명입력",
        msg: '선택한 조직과 동일한 레벨의 조직생성'
    }, function () {
        if(this.key === 'ok') {
        	if(this.input.value == "" || this.input.value == null){
        		dialog.alert('조직명을 입력하여 주십시오', function() {});
				return;
        	}
        	var dataObj = new Object();
        	dataObj.DeptUpCd = selectedNode.pId === undefined ? null : selectedNode.pId;
        	dataObj.DeptName = this.input.value;
        	insertNode = dataObj;
        	var data = new Object();
        	data = {
    			dataObj : dataObj,
        		requestType	: 'subNewDir'
        	}
        	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successInsertNewDir);
        }
    });
}

// 트리구분 추가 완료
function successInsertNewDir(data) {
	var treeId 	= data;
	var pId		= selectedNode.pId === undefined ? null : selectedNode.pId;
	var parentNode = null;
	if(pId != null) parentNode = treeObj.getNodeByParam("id", pId, null);
	var newNode = new Object();
	newNode.id 	= treeId;
	newNode.pId = pId;
	newNode.name= insertNode.DeptName;
	newNode.isParent = true;
	treeObj.addNodes(parentNode,newNode);
	
	newnode 		= null;
	selectedNode 	= null;
	insertNode 		= null;
	
	//getTreeInfo();
}

// 트리 서브구분 추가
function addSubMenu() {
	confirmDialog.setConfig({title: "XXX", theme: "danger"});
	confirmDialog.prompt({
        title: "구분명입력",
        msg: '선택한 조직의 하위레벨의 조직생성'
    }, function () {
        if(this.key === 'ok') {
        	if(this.input.value == "" || this.input.value == null){
        		dialog.alert('조직명을 입력하여 주십시오', function() {});
				return;
        	}
        	var dataObj = new Object();
        	dataObj.DeptUpCd 	= selectedNode.id === undefined ? null : selectedNode.id;
        	dataObj.DeptName 	= this.input.value;
        	insertNode = dataObj;
        	var data = new Object();
        	data = {
    			dataObj : dataObj,
        		requestType	: 'subNewDir'
        	}
        	
        	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successInsertNewSubDir);
        }
    });
}

// 트리 서브구분 추가 완료
function successInsertNewSubDir(data) {
	var treeId 	= data;
	var newNode = new Object();
	newNode.id 	= treeId;
	newNode.pId = selectedNode.id;
	newNode.name= insertNode.DeptName;
	newNode.isParent = true;
	treeObj.addNodes(selectedNode,newNode);
	
	newnode 		= null;
	selectedNode 	= null;
	insertNode 		= null;
	
	//getTreeInfo();
}

// 트리 구분 삭제 확인
function delMenu() {
	var childSw = false;
	if(selectedNode.children != undefined && selectedNode.children.length > 0) childSw = true;
	
	var confirmMsg = '';
	confirmMsg += '['+selectedNode.name+'] 해당 조직을 삭제 하시겠습니까?';
	
	if(childSw){
		confirmMsg = confirmMsg + '\n또한 하위조직이 존재하여 하위조직까지 모두 삭제됩니다. 삭제처리할까요?'; 
	}
	
	confirmDialog.confirm({
		title: "조직삭제",
		msg: confirmMsg,
	}, function(){
		if(this.key === 'ok') {
			var dataObj = new Object();
			dataObj.DeptUpCd = selectedNode.id;
			
			var data = new Object();
			data = {
				dataObj		: dataObj,
				requestType	: 'subDelDir_Check'
			}
			ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successCheckDelDir);
		} else {
			selectedNode 	= null;
			insertNode 		= null;
		}
	});
}

// 트리 구분 삭제 확인 완료
function successCheckDelDir(data) {
	if(data != '0') {
		confirmDialog.confirm({
			msg: '삭제대상 조직에 등록된 사용자가 있습니다.무시하고 삭제처리할까요?',
		}, function(){
			if(this.key === 'ok') {
				deleteDir();
			} else {
				selectedNode 	= null;
				insertNode 		= null;
			}
		});
	} else {
		deleteDir();
	}
}

// 트리 구분 삭제 
function deleteDir() {
	var dataObj = new Object();
	dataObj.DeptUpCd = selectedNode.id;
	
	var data = new Object();
	data = {
		dataObj		: dataObj,
		requestType	: 'subDelDir'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json', successDelDir);
}

// 트리 구분 삭제 완료
function successDelDir(data) {
	dialog.alert('['+selectedNode.name+'] 해당 구분 삭제 처리 완료.',function() {
		treeObj.removeNode(selectedNode);
		
		// 트리 삭제 후 자식트리가 없을 경우 상위 트리가 저절로 아이콘이 파일로 변경됨 그래서 상위 트리의 속성 값 변경!
		if (null != selectedNode.parentTId) {
			var nodes =  treeObj.getNodeByTId(selectedNode.parentTId);
			nodes.isParent = true;
			treeObj.updateNode(nodes);
		}
		
		selectedNode 	= null;
		insertNode 		= null;
	});
}

// 트리 구분명 변경
function reMenu() {
	confirmDialog.setConfig({title: "XXX", theme: "danger"});
	confirmDialog.prompt({
        title: "구분명입력",
        msg: '선택한 구분의 구분명 변경'
    }, function () {
        if(this.key === 'ok') {
        	var dataObj = new Object();
        	dataObj.DeptUpCd 	= selectedNode.id;
        	dataObj.dirname 	= this.input.value;
        	var data = new Object();
        	insertNode = dataObj;
        	data = {
        		dataObj : dataObj,
        		requestType	: 'subRename'
        	}
        	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successReNameDir);
        }
    });
}
// 트리 구분명 변경 완료
function successReNameDir(data) {
	selectedNode.name = insertNode.dirname; 
	treeObj.updateNode(selectedNode);
}

// 트리 더블 클릭
function dbClickTree(event, treeId, treeNode) {
	dbClickSw = true;
	
	// 사용자 선택시
	if(subSw) {
		if(treeNode.userId === undefined) {
			dialog.alert('사용자를 선택해 주시기 바랍니다.', function() {});
			return;
		} else {
			window.parent.txtUserId 	= treeNode.userId;
			window.parent.txtUserName 	= treeNode.userName;
			window.parent.deptName 		= treeNode.deptName;
			window.parent.deptCd 		= treeNode.pId;
		}
	} else {
		if(treeNode === null) {
			return;
		}
		
		if(treeNode.children !== undefined && !upDeptSw) {
			dialog.alert('하위 조직을 선택해 주시기 바랍니다.', function() {});
			return;
		}
		
		// 조직 선택시
		if(selDeptSw) {
			window.parent.selDeptCd = treeNode.id;
			window.parent.txtOrg = treeNode.name;
			$('#txtOrg', window.parent.document).val(treeNode.name);
		} else {
			// 조직(겸직)선택시
			window.parent.selSubDeptCd = treeNode.id;
			$('#txtOrgAdd', window.parent.document).val(treeNode.name);
		}
	}
	popClose();
}

// 노드 찾기
function findNode() {
	var txtFind = $('#txtFind').val().trim().toUpperCase();
	var node = treeObj.getNodesByParam("name", txtFind);
	treeObj.selectNode(node[0],false,false);
}

// 트리정보 가져오기
function getTreeInfo() {
	
	var chkcd = true;
	var itsw = true;
	if(popData.wingbn != '2' && popData.wingbn != '3' ) {
		chkcd = false;
		itsw = false;
	}
	
	var data = new Object();
	data = {
		chkcd : chkcd,
		itsw : itsw,
		requestType	: 'getTeamInfoTree_zTree'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetTreeInfo);
}

// 트리정보 가져오기 완료
function successGetTreeInfo(data) {
	treeObjData = data;
	$.fn.zTree.init($("#tvOrgani"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("tvOrgani");
	
	if(popData != undefined && popData != null) {
		if(popData.dutycode != undefined && popData.dutycode != null && popData.dutycode != '') {
			$('#txtFind').val(popData.dutyname);
			findNode();
		} 
	}
}

// 닫기
function popClose() {
	window.parent.organizationModal.close();
}