/**
 * 프로그램유형별처리속성관리 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-14
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var selectedSystem  = window.parent.selectedSystem;
var sysCd			= selectedSystem.cm_syscd;

var treeObj			= null;
var treeObjData		= null;
var selectedNode	= null;
var insertNode		= null;

var ulCodeData		= null;
var rMenu			= null;

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onRightClick: OnRightClickTree,
		onClick 	: onClickTree,
	},
	view: {
		dblClickExpand: false
	}
	
	
};

$(document).ready(function(){
	getProgInfoTree();
	getCodeSelInfo(null);
	rMenu = $("#rMenu");
	
	// 트리 전체 열기(펼치기)
	$('#btnPlus').bind('click', function() {
		treeObj.expandAll(true);
	});
	
	// 트리 전체 닫기
	$('#btnMinus').bind('click', function() {
		treeObj.expandAll(false);
	});
	
	// 처리속성 등록
	$('#btnReq').bind('click', function() {
		selectedNode = treeObj.getSelectedNodes();
		var tmpInfo	= '';
		if(selectedNode.length === 0 ) {
			dialog.alert('처리속성을 등록할 구분을 왼쪽 트리에서 선택 후 눌러주시기 바랍니다.', function(){});
			return;
		}
		selectedNode = selectedNode[0];
		
		ulCodeData.forEach(function( codeItem, index) {
			addId = Number(codeItem.cm_micode);
			var checked = $('#chkCode'+addId).is(':checked');
			if(codeItem.enable === '1' && checked) {
				if(tmpInfo.length > 0 ) tmpInfo += ',';
				tmpInfo += codeItem.cm_micode;
			};
		});
		
		var dataObj = new Object();
		dataObj.prginfcd = selectedNode.id;
		dataObj.proginfo = tmpInfo;
		
		var data = new Object();
    	data = {
			dataObj     : dataObj,
    		requestType	: 'updtProgInfo'
    	}
    	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successUpdateProgInfo);
	});
	
	// 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 처리속성 등록 완료
function successUpdateProgInfo(data) {
	if(data === '0') {
		dialog.alert('처리속성을 등록하였습니다', function(){
			getProgInfoTree();
			getCodeSelInfo(null);
			selectedNode = null;
		});
	} else {
		dialog.alert('처리속성 등록에 실패하였습니다.', function(){});
	}
}

// 닫기
function popClose() {
	window.parent.prgSeqModal.close();
}

// 트리 클릭 이벤트
function onClickTree(event, treeId, treeNode) {
	var treeId = treeNode.id;
	var infoSw = false;
	
	if(treeNode.children == undefined || treeNode.children.length === 0) infoSw = true;
	if(treeId.length <= 9) {
		infoSw = true;
		getCodeSelInfo(treeId);
	}
	if(infoSw) return; 	// 코드 사용가능
	else return;		// 코드 사용불가 
};

// 트리 오른쪽 클릭 이벤트
function OnRightClickTree(event, treeId, treeNode) {
	var treeId = treeNode.id;
	
	if(treeId.length > 9 ) return;
	
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

// 오른쪽(Context menu) 메뉴 보여주기
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

// 오른쪽 메뉴 감추기
function onBodyMouseDown(event){
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
	}
}

// 트리 구분 추가
function addMenu() {
	confirmDialog.setConfig({title: "XXX", theme: "danger"});
	confirmDialog.prompt({
        title: "구분명입력",
        msg: '선택한 구분과 동일한 레벨의 트리생성'
    }, function () {
        if(this.key === 'ok') {
        	var dataObj = new Object();
        	dataObj.upprginfcd = selectedNode.pId === undefined ? null : selectedNode.pId;
        	dataObj.prginfname = this.input.value;
        	dataObj.gbncd = '2';
        	insertNode = dataObj;
        	var data = new Object();
        	data = {
    			dataObj     : dataObj,
        		requestType	: 'subNewDir'
        	}
        	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successInsertNewDir);
        }
    });
}

// 트리구분 추가 완료
function successInsertNewDir(data) {
	var treeId 	= data.substr(0,1);
	var pId		= selectedNode.pId === undefined ? null : selectedNode.pId;
	var parentNode = null;
	if(pId != null) parentNode = treeObj.getNodeByParam("id", pId, null);
	var newNode = new Object();
	newNode.id 	= treeId;
	newNode.pId = pId;
	newNode.name= insertNode.prginfname;
	newNode.isParent = true;
	treeObj.addNodes(parentNode,newNode);
	
	newnode 		= null;
	selectedNode 	= null;
	insertNode 		= null;
}

// 트리 서브구분 추가
function addSubMenu() {
	confirmDialog.setConfig({title: "XXX", theme: "danger"});
	confirmDialog.prompt({
        title: "구분명입력",
        msg: '선택한 구분의 하위레벨의 트리생성'
    }, function () {
        if(this.key === 'ok') {
        	var dataObj = new Object();
        	dataObj.upprginfcd 	= selectedNode.id === undefined ? null : selectedNode.id;
        	dataObj.prginfname 	= this.input.value;
        	dataObj.gbncd 		= '1';
        	insertNode = dataObj;
        	var data = new Object();
        	data = {
        			dataObj : dataObj,
        		requestType	: 'subNewDir'
        	}
        	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successInsertNewSubDir);
        }
    });
}

// 트리 서브구분 추가 완료
function successInsertNewSubDir(data) {
	var treeId 	= data.substr(0,1);
	var newNode = new Object();
	newNode.id 	= treeId;
	newNode.pId = selectedNode.id;
	newNode.name= insertNode.prginfname;
	newNode.isParent = true;
	treeObj.addNodes(selectedNode,newNode);
	
	newnode 		= null;
	selectedNode 	= null;
	insertNode 		= null;
}

// 트리 구분 삭제 확인
function delMenu() {
	var childSw = false;
	if(selectedNode.children != undefined && selectedNode.children.length > 0) childSw = true;
	
	var confirmMsg = '';
	confirmMsg += '['+selectedNode.name+'] 해당 구분을 삭제 하시겠습니까?';
	confirmMsg += childSw ? '\n또한 하위구분이 존재하여 하위구분까지 모두 삭제됩니다. 삭제처리할까요?':'';
	
	confirmDialog.confirm({
		msg: confirmMsg,
	}, function(){
		if(this.key === 'ok') {
			var dataObj = new Object();
			console.log(selectedNode);
			dataObj.upprginfcd = selectedNode.id;
			dataObj.prginfname = selectedNode.name;
			
			var data = new Object();
			data = {
				dataObj		: dataObj,
				requestType	: 'subDelDir_Check'
			}
			ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successCheckDelDir);
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
			msg: '삭제대상 구분에 등록된 처리속성이 있습니다.무시하고 삭제처리할까요?',
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
	dataObj.upprginfcd = selectedNode.id;
	dataObj.prginfname = selectedNode.name;
	
	var data = new Object();
	data = {
		dataObj		: dataObj,
		requestType	: 'subDelDir'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successDelDir);
}

// 트리 구분 삭제 완료
function successDelDir(data) {
	dialog.alert('['+selectedNode.name+'] 해당 구분 삭제 처리 완료.',function() {
		treeObj.removeNode(selectedNode);
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
        	dataObj.prginfcd 	= selectedNode.id;
        	dataObj.prginfname 	= this.input.value;
        	var data = new Object();
        	insertNode = dataObj;
        	data = {
    			dataObj     : dataObj,
        		requestType	: 'subRename'
        	}
        	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successReNameDir);
        }
    });
}

function successReNameDir(data) {
	selectedNode.name = insertNode.prginfname; 
	treeObj.updateNode(selectedNode);
}

// 처리속성 트리정보 가져오기
function getProgInfoTree() {
	var data = new Object();
	data = {
		requestType	: 'getProgInfoZTree'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successGetProgInfoTree);
	
}

// 처리속성 트리정보 가져오기 완료
function successGetProgInfoTree(data) {
	treeObjData = data;
	$.fn.zTree.init($("#tvPrgSeq"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("tvPrgSeq");
}

// 처리속성 리스트 가져오기
function getCodeSelInfo(res) {
	var data = new Object();
	data = {
		res			: res,
		requestType	: 'getCodeSelInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successGetCodeSelInfo);
}

// 처리속성 리스트 가져오기 완료
function successGetCodeSelInfo(data) {
	ulCodeData = data;
	$('#ulCode').empty();
	var liStr = '';
	var addId = null;
	var checkSw = false;
	var focusId	= null;
	ulCodeData.sort(function(a,b) {
		return a.cm_codename < b.cm_codename ? -1 : a.cm_codename > b.cm_codename ? 1: 0;
	});
	
	ulCodeData.forEach(function(codeItem, sysInfoIndex) {
		addId = Number(codeItem.cm_micode);
		checkSw = false;
		if(codeItem.checkbox === '1') checkSw = true;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-code" id="chkCode'+addId+'" data-label="'+codeItem.cm_codename+'"  value="'+codeItem.cm_micode+'"';
		if(checkSw)	liStr += ' checked="checked" ';
		liStr += '/>';
		liStr += '</li>';
	});
	$('#ulCode').html(liStr);

	$('input.checkbox-code').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
	ulCodeData.forEach(function(codeItem, sysInfoIndex) {
		addId = Number(codeItem.cm_micode);
		if(codeItem.enable === '0' ) {
			$('#chkCode' + addId).wCheck('disabled',true);
		} else {
			if(codeItem.checkbox === '1' && focusId === null) focusId = addId;
		}
	});
	$('#chkCode' + focusId).focus();
}


