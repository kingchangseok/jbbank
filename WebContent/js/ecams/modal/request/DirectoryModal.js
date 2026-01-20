/**
 * [체크인 - 디렉토리 modal] 화면 기능정의
 *
 * <pre>
 * 	작성자	: 정선희
 * 	버전 		: 1.0
 *  수정일 	: 2021-06-11
 *
 */

var userId 		= window.parent.userId;
var sysMsg 		= window.parent.sysMsg;
var sysCd 		= window.parent.sysCd;
var sysGb 		= window.parent.sysGb;
var reqCd 		= window.parent.reqCd;

var selectedNode	= null;
var treeObj			= null;
var treeData    	= [];

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},callback: {
		onClick : onClickTree,
		onDblClick : dbClickTree,
		onExpand : myOnExpand
	},
	view: {
		dblClickExpand: false
	}
};

$(document).ready(function() {
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

	$('#txtSys').val(sysMsg);
	
	//닫기
	$('#btnClose').bind('click', function() {
		popClose(false);
	});
	
	getTreeInfo();
});

//트리정보 가져오기
function getTreeInfo() {
	var data = new Object();
	data = {
		UserID: userId,
		SysCd: sysCd,
		SecuYn: "y",
		SinCd: reqCd,
		ReqCd : "",
		requestType	: 'getRsrcPath_ztree'
	}
	
	console.log('[getRsrcPath_ztree] ==>', data);
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetDirPath);
}

//트리정보 가져오기 완료
function successGetDirPath(data) {
	/*treeObjData = data;
	ztree = $.fn.zTree.init($('#tvOrgani'), setting, treeObjData); //초기화
	treeObj = $.fn.zTree.getZTreeObj("tvOrgani");*/
	treeData = data;
	if(treeData !== 'ERR') {
		var obj = treeData;
		
		for(var i in treeData){
			if(obj[i].cm_codename =='' ){
				delete obj[i]
				continue;
			}
			obj[i].name = obj[i].cm_codename;
			delete obj[i].cm_codename;
			obj[i].isParent = true;
		}
		obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
		obj = JSON.parse(obj);
		treeData = obj;
		
		ztree = $.fn.zTree.init($('#tvOrgani'), setting, treeData);
	}
}

//폴더 펼칠때 실행하는 함수
function myOnExpand(event, treeId, treeNode) {
	//root node 만 비동기 방식으로 뽑아오는 조건
	if(treeNode.pId != null || treeNode.children != undefined){ //treeNode.pid != -1 ||
		return false;
	};
	//로딩중 icon class 추가
	$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){

		var ajaxReturnData = null;
		var reqyestType = "";
		var tmpData = {
			UserId : userId,
			SysCd : sysCd,
			RsrcCd : treeNode.cr_rsrccd,
			Info : treeNode.cm_info,
			seqNo : treeNode.cm_upseq,
			requestType:  "getDirPath3_ztree"
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
		
		console.log('[getDirPath3_ztree] ==>', ajaxReturnData);
		if (ajaxReturnData == undefined || ajaxReturnData == null) return;
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
		} else {
			var obj = ajaxReturnData;
				
			for(var i in ajaxReturnData){
				if(obj[i].cm_dirpath =='' ){
					delete obj[i]
					continue;
				}
				obj[i].name = obj[i].cm_dirpath;
				delete obj[i].cm_dirpath;
				obj[i].isParent = true;
			}
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			ztree.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
};

function onClickTree(event, treeId, treeNode) {
	var fullpath = null;
	
	if(treeNode.cm_volpath == null){
		fullpath = treeNode.cm_fullpath;
	}else{
		fullpath = treeNode.cm_volpath;
	}
	
	var lb6split = fullpath.split('/');
	var lb6String1 	= '';
	
	if (lb6split != null && lb6split != undefined && lb6split.length < 2){
		$('#lbPath').text('');
	}
	
	for (var i=0; i<lb6split.length; i++){
		if (lb6split[i].length>0) {
			if (i==0 && lb6split[i].indexOf(':')>0) {
				lb6String1 = lb6split[i];
			} else {
				lb6String1 = lb6String1+ '/'+ lb6split[i];
			}
		}
	}
	
	if (lb6String1.length > 0){
		$('#lbPath').text(lb6String1);
	}
}

//트리 더블 클릭
function dbClickTree(event, treeId, treeNode) {
	dbClickSw = true;
	
	if(treeNode === null) {
		return;
	}
	//if (treeNode.children !== undefined) return;

	var fullpath = treeNode.cm_fullpath;
	var dsncd = treeNode.cm_dsncd;
	var rsrcCd = treeNode.cr_rsrccd;
	var strDirPath = "";
	var strDsnCd = "";
	
	if (fullpath.substr(0,1) != "/") return;
	
	if ($("#chkDir").is(":checked")) strDirPath = fullpath;
	else {
		if (dsncd != null && dsncd.length > 0) strDsnCd = dsncd;
	}
	
	if (strDirPath != "" || strDsnCd != ""){
		window.parent.strDirPath = strDirPath;
		window.parent.strDsnCd = strDsnCd;
		popClose(true);
	}
}

function popClose(closeflg) {
	if (!closeflg) {
		window.parent.strDirPath = "";
		window.parent.strDsnCd = "";
	} 
	window.parent.directoryModal.close();
}