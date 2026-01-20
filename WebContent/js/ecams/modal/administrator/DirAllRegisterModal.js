var userId = window.parent.userId;
var sysCd = window.parent.getSelectedVal('cboSysCd').cm_syscd;

var firstGrid	= new ax5.ui.grid();

$('[data-ax5select="cboSvr"]').ax5select({
    options: []
});

var firstGridData	= [];
var cboSvrData		= [];
var treeObj			= null;
var treeObjData		= null;
var rMenu			= null;

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},callback: {
		/*onExpand: myOnExpand,
		onClick : onClickTree,*/
		onRightClick: OnRightClick
	},
	view: {
		dblClickExpand: false
	}
};

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
        	this.self.clearSelect();
            this.self.select(this.dindex);
            clickDirGrid(this.dindex);
        },
        onDBLClick: function () {
        	dblClickDirGrid(this.dindex, 'dbClick');
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_dirpath", 	label: "경로",  	width: '100%', align: 'left'}
    ]
});

$(document).ready(function() {
	$('#txtSys').val(window.parent.getSelectedVal('cboSysCd').cm_sysmsg);
	
	getSysInfo();
	
	$('#cboSvr').bind('change', function() {
		cboSvrChange();
	});
	
	//조회
	$('#btnQry').bind('click', function() {
		btnQryClick();
	});
	
	//등록
	$('#btnReg').bind('click', function() {
		btnRegClick();
	});
	
	//닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

function popClose() {
	window.parent.dirAllRegisterModal.close();
}

function getSysInfo() {
	var data = new Object();
	data = {
		UserID 		: userId,
		SysCd 		: sysCd,
		SecuYn 		: 'Y',
		SelMsg	 	: '',
		requestType	: 'getsvrInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

function successGetSysInfo(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	cboSvrData = data;
	$('[data-ax5select="cboSvr"]').ax5select({
        options: injectCboDataToArr(cboSvrData, 'cm_svrname2' , 'cm_svrname2')
	});
	
	if(cboSvrData.length > 0) {
		cboSvrChange();
	}
}

function cboSvrChange() {
	$('#txtDirBase').val('');
	if(getSelectedIndex('cboSvr') > -1) {
		$('#txtDirBase').val(getSelectedVal('cboSvr').cm_volpath);
	}
}

function btnQryClick() {
	if($('#txtDirBase').val().trim().length < 1) {
		dialog.alert('기준디렉토리를 입력하여 주십시오.');
		return;
	}
	
	var data = new Object();
	data = {
		UserID 		: userId,
		SysCd 		: sysCd,
		SvrIp 		: getSelectedVal('cboSvr').cm_svrip,
		SvrPort	 	: getSelectedVal('cboSvr').cm_portno,
		BaseDir		: $('#txtDirBase').val(),
		requestType	: 'getSvrDir_ztree'
	}
	ajaxAsync('/webPage/ecmm/Cmm1200Servlet', data, 'json',successgetSvrDir);
}

function successgetSvrDir(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	treeObjData = data;
	$.fn.zTree.init($("#treePath"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("treePath");
}

function btnRegClick() {
	var selItems = firstGrid.getList('selected');
	if(selItems.length == 0) {
		dialog.alert('등록할 디렉토리를 선택하여 주십시오.');
		return;
	}
	
	var data = new Object();
	data = {
		sysCD 		: sysCd,
		UserId 		: userId,
		saveList 	: selItems,
		requestType	: 'savePath2'
	}
	ajaxAsync('/webPage/ecmm/Cmm1200Servlet', data, 'json',successSavePath);
}

function successSavePath(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	dialog.alert(data.retMsg); 
	return;
}

/* 트리 노드 마우스 우클릭 이벤트 */
function OnRightClick(event, treeId, treeNode) { 
	if(treeNode == null || treeNode == undefined || treeNode == '')  return;
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
/* context menu 설정 */
function showRMenu(type, x, y) { 
	$("#rMenu ul").show(); 
	if (type=="root") { 
		$("#contextmenu1").hide();
		$("#contextmenu2").hide();
	} else { 
		$("#contextmenu1").show();
		$("#contextmenu2").show();
 	} 
  
     y += document.body.scrollTop; 
     x += document.body.scrollLeft; 
     $("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"});
  
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

function contextmenu_click(gbn) {
	hideRMenu();
	
	if(treeObj.getSelectedNodes()[0] == null) {
		dialog.alert('대상 디렉토리를 선택(클릭) 후에 추가 하여 주십시요.');
		return;
	}
	
	if (gbn === "1"){ //추가
		var tmpObj = new Object();
		var tmpArr = [];
		tmpObj.cm_dirpath = treeObj.getSelectedNodes()[0].cm_fullpath;
		
		var findSw = false;
		for(var i=0; i<firstGridData.length; i++) {
			if(firstGridData[i].cm_dirpath == tmpObj.cm_dirpath) {
				findSw = true;
				break;
			}
		}
		
		if(findSw) {
			dialog.alert('이미 목록에 추가된 디렉토리입니다.');
			return;
		}
		
		tmpObj.cm_syscd = sysCd;
		tmpObj.cm_userid = userId;
		tmpObj.selected = '0';
		tmpArr.push(tmpObj);
		chkDirPath(tmpArr);
		tmpObj = null;
		
	} else if (gbn === "2"){ //추가(하위폴더포함)
		var tmpObj = new Object();
		var tmpArr = [];
		tmpObj.cm_dirpath = treeObj.getSelectedNodes()[0].cm_fullpath;
		
		var i=0;
		for(i=0; i<firstGridData.length; i++) {
			if(firstGridData[i].cm_dirpath == tmpObj.cm_dirpath) {
				break;
			}
		}
		
		if(i >= firstGridData.length) {
			tmpObj.cm_syscd = sysCd;
			tmpObj.cm_userid = userId;
			tmpObj.selected = '0';
			tmpArr.push(tmpObj);
			tmpObj = null;
		}
		
		if(treeObj.getSelectedNodes()[0].children != undefined) {
			for(var j=0; j<treeObj.getSelectedNodes()[0].children.length; j++) {
				tmpObj = new Object();
				tmpObj.cm_syscd = sysCd;
				tmpObj.cm_userid = userId;
				tmpObj.selected = '0';
				tmpObj.cm_dirpath = treeObj.getSelectedNodes()[0].children[j].cm_fullpath;
				tmpArr.push(tmpObj);
				tmpObj = null;
			}
			chkDirPath(tmpArr);
		}else {
			chkDirPath(tmpArr);
		}
	}
}

function chkDirPath(tmpArr) {
	var data = new Object();
	data = {
		DirList 	: tmpArr,
		requestType	: 'chkDirPath'
	}
	console.log('[chkDirPath] ==>',data);
	ajaxAsync('/webPage/ecmm/Cmm1200Servlet', data, 'json',successChkDirPath);
}

function successChkDirPath(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data.length == 0) {
		dialog.alert('이미 등록된 디렉토리입니다.');
		return;
	}
	
	for(var i=0; i<data.length; i++) {
		for(var j=0; j<firstGridData.length; j++) {
			if(firstGridData[j].cm_dirpath == data[i].cm_dirpath) break;
		}
		if(j>=firstGridData.length) {
			firstGridData.push(data[i]);
		}
	}
	
	firstGrid.setData(firstGridData);
}