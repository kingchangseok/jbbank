
var rootSw = false;
var treeData  = null;
var treeObj	= null;

var maxSeq = 0;

//경로트리
var setting = {
	data: {
		simpleData: {
			enable: true,
		}
	},
	callback:{
		onExpand: myOnExpand,
		onDblClick : onDblClickTree,
	}
};

$('[data-ax5select="cboDrive"]').ax5select({
    options: []
});

$(document).ready(function(){
	rootSw = true;
	
	treeData = [];
	ztree = $.fn.zTree.init($('#treeDir'), treeData);
	
	setTimeout(function(){
		DIRControl('DRIVE',0,'');
	}, 100);

	$('#cboDrive').bind('change', function(){
		cboDriveChange();
	});
	
	$("#cmdExit").bind('click', function() {
		window.parent.modalCloseFlag = false;
		popClose();
	});

});

function DIRControl(Sv_JobGub,Sv_Cnt,Sv_LocalF){
	//console.log(Sv_JobGub,Sv_Cnt,Sv_LocalF);
	var ret = null;
	var clickPath = '';
	
	var data = new Object();
	data = {
		userId : window.parent.userId,
		targetGB : Sv_JobGub,
		Sv_GbnCd : '',
		Sv_Dir : Sv_LocalF,
		agentDir : window.parent.modalData.agentDir,
		agentIp : window.parent.modalData.agentIp,
		paramPid   : '0',
		requestType	: 'cmdControl'
	}
	//console.log('cmdControl => ', data);
	//ajaxAsync('/webPage/ecmd/Cmd1300Servlet', data, 'json',successDeleteUserConfig);
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmd/Cmd1300Servlet', data, 'json'); 
	
	if (ajaxReturnData.indexOf('ERROR')>-1) {
		dialog.alert(ajaxReturnData);
		return;
	}else {
		var obj = ajaxReturnData;
		obj = JSON.stringify(obj).replace(/\0/g,'');
		obj = JSON.parse(obj);
		
		ajaxReturnData = obj;
		
		//console.log('Sv_JobGub >>>>>>>> ' + Sv_JobGub);
		//console.log('ajaxReturnData => ', ajaxReturnData);
		if(Sv_JobGub == 'DRIVE') {
			$('[data-ax5select="cboDrive"]').ax5select({
		        options: ajaxReturnData
			});
			
			DIRControl('DIRLIST',0,getSelectedVal('cboDrive').value+'\\');
		}else {
			treeData = clone(ajaxReturnData);
			
			var obj = treeData;
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			treeData = obj;
			
			ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
		}
	}
}

function cboDriveChange() {
	
	if (getSelectedIndex('cboDrive')<0) return;
	
	treeData = [];
	ztree = $.fn.zTree.init($('#treeDir'), treeData);
	
	maxSeq = 1;
	
	//DIRControl('DIRLIST',0,getSelectedVal('cboDrive').value);
	DIRControl('DIRLIST',0,getSelectedVal('cboDrive').value+'\\');
	
}

function makeDirPath(treeNode) {
	//console.log('totalTree' , ztree.getNodes());
	var ingSw = true;
	var driveName = getSelectedVal('cboDrive').value+'\\';
	var makedDirPath = '';
	var parentNode = null;
	var firstSw = true;
	
	if(treeNode.pId == null || treeNode.pId == undefined) {
		return driveName + treeNode.name;
	}
	
	//console.log('check ',ztree.getNodeByTId("1_4"));
	
	while(ingSw) {
		if(firstSw) {
			//console.log('pid > ', treeNode.pId);
			for(var i=0;i<treeData.length; i++) {
				if(treeData[i].id == treeNode.pId) {
					parentNode = copyReferenceNone(treeData[i]);
					break;
				}
			}
			firstSw = false;
		} else {
			for(var i=0;i<treeData.length; i++) {
				if(treeData[i].id == parentNode.pId) {
					parentNode = copyReferenceNone(treeData[i]);
					break;
				}
			}
		}
		if(makedDirPath == '') {
			makedDirPath = parentNode.name;
		} else {
			makedDirPath = parentNode.name + '\\' + makedDirPath;
		}
		var id = parentNode.id;
		if(id.indexOf('_') < 0) {
			ingSw = false;
		}
	}
	
	makedDirPath += '\\' +treeNode.name;
	
	return driveName + makedDirPath;
}

// 폴더 펼칠때 실행하는 함수
function myOnExpand(event, treeId, treeNode) {
	if(treeNode == null || treeNode == undefined) return;
	
	if(treeNode.check_Child_State == -1) {
		
		var makedDirPath = makeDirPath(treeNode);
		//console.log('makedDirPath', makedDirPath);
		
		var data = new Object();
		data = {
			userId : window.parent.userId,
			targetGB : 'DIRLIST2',
			Sv_GbnCd : '',
			Sv_Dir : makedDirPath,
			agentDir : window.parent.modalData.agentDir,
			agentIp : window.parent.modalData.agentIp,
			paramPid   : treeNode.id,
			requestType	: 'cmdControl'
		}
		//console.log('cmdControl => ', data);
		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmd/Cmd1300Servlet', data, 'json'); 
		//console.log('ajaxReturnData => ', ajaxReturnData);
		
		var obj = null;
		obj = ajaxReturnData;
		if(obj != null) {
			obj = JSON.stringify(obj).replace(/null,/gi,'');
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			treeData = treeData.concat(ajaxReturnData);
			ztree.addNodes(treeNode,ajaxReturnData);
			
			//console.log(treeData);
			
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
		
	}
};

function onDblClickTree(event, treeId, treeNode) {
	window.parent.selDevHome = makeDirPath(treeNode);	
	window.parent.modalCloseFlag = true;
	popClose();
	
} 
function popClose() {
	window.parent.dirSelectModal.close();
}

function changeLanguage() {
	$("#lbSub").html("[" + json_prop['DIRSE'] + "]");
	$('label[for="chksubdir"]').text(json_prop['SUBDIR']);
}