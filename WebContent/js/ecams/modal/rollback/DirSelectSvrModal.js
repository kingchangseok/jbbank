
var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var strReqCd = window.parent.reqCd;
var strSysCd = window.parent.selSysCd;
var strSysMsg = window.parent.selSysMsg;
var rootSw = false;
var webSw = window.parent.webSw;
var treeData  = null;
var treeObj	= null;

//경로트리
var setting = {
	data: {
		simpleData: {
			enable: true,
		}
	},
	callback:{
		onExpand: myOnExpand,
		onClick : onClickTree,
		onDblClick : onDblClickTree,
	}
};

$(document).ready(function(){

	$('input.checkbox').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
		
	$('#txtSys').val(window.parent.selSysMsg);
		
	rootSw = true;
	
	treeData = [];
	ztree = $.fn.zTree.init($('#treeDir'), treeData);
	
	getRsrcPath();
	
	$("#cmdExit").bind('click', function() {
		window.parent.modalCloseFlag = false;
		popClose();
	});

});
function getRsrcPath() {
	
	var tmpData = {
		UserID : userId,
		SysCd :	strSysCd,
		SecuYn : 'y',
		SinCd :	strReqCd,
		ReqCd :	'',
		requestType : 'getRsrcPath_ztree'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpData, 'json',successGetFileTree);
}
function successGetFileTree(data) {
	console.log('successGetFileTree ==>', data);
	treeData = data;
	
	if(treeData !== 'ERR') {
		var obj = treeData;
		
		/*for(var i in treeData){
			if(obj[i].cm_codename =='' ){
				delete obj[i]
				continue;
			}
			obj[i].name = obj[i].cm_dirpath;
			//delete obj[i].cm_codename;
			//obj[i].isParent = true;
		}*/
		obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
		obj = JSON.parse(obj);
		treeData = obj;
		
		ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
	}
}

//폴더 펼칠때 실행하는 함수
function myOnExpand(event, treeId, treeNode) {
	console.log('treeNode=',treeNode);
	if(treeNode.children != undefined) {
		return false;
	}
	
	if(treeNode.cm_upseq != '0'){
		return false;
	};
	
	//로딩중 icon class 추가
	$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){
		var tmpData = new Object();
		tmpData = {
			UserId : userId,
			SysCd : strSysCd,
			RsrcCd : treeNode.cr_rsrccd,
			Info : treeNode.cm_info,
			seqNo : treeNode.cm_upseq,
			requestType : "getDirPath3_ztree"
		}
		console.log('[getDirPath3_ztree] ==>',tmpData);
		ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
		
		if (ajaxReturnData == undefined || ajaxReturnData == null) return;
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
		} else {
			var obj = ajaxReturnData;
				
			/*for(var i in ajaxReturnData){
				if(obj[i].cm_dirpath =='' ){
					delete obj[i]
					continue;
				}
				obj[i].name = obj[i].cm_dirpath;
				//delete obj[i].cm_dirpath;
				obj[i].isParent = true;
			}*/
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			ztree.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
};

function onDblClickTree(event, treeId, treeNode) {
		
	var toDsnCd = treeNode.cm_fullpath;
	if (toDsnCd.substr(0,1) != '/') return;
	
	if ($('#chksubdir').prop('checked')) window.parent.selDirPath = toDsnCd;
	else if (treeNode.cr_dsncd != undefined && treeNode.cr_dsncd != null && treeNode.cr_dsncd != '') window.parent.selDsnCd = treeNode.cr_dsncd;
	
	if ((window.parent.selDirPath != undefined && window.parent.selDirPath != null && window.parent.selDirPath != '') || 
		(window.parent.selDsnCd != undefined && window.parent.selDsnCd != null && window.parent.selDsnCd != '')) {
		window.parent.selDirPath = treeNode.cm_fullpath;
		window.parent.selProg = $('#txtProg').val().trim();
		window.parent.modalCloseFlag = true;
		popClose();
	}
} 

function onClickTree(event, treeId, treeNode) {
	var fullPath = treeNode.cm_fullpath;
	var dir = '';
	var dirSplit = fullPath.split('/');
	
	
	if(dirSplit.length < 2) {
		$('#lblDir').text('');
	}
	for(var i=1; i<dirSplit.length; i++) {
		dir = dir + '/' + dirSplit[i];
	}
	
	if(dir.length > 0) {
		$('#lblDir').show();
		$('#lblDir').text(dir);
	}
}

function popClose() {
	window.parent.dirSelectSvrModal.close();
}
