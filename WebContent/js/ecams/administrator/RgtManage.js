/**
 * 권한관리 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-20
 * 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var treeObj			= null;
var treeObjData		= null;

var cboDutyData		= null;
var dutyUlInfoData	= null;

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};

$('input.checkbox-rgt').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	getMenuTree();
	getCodeInfo();
	
	// 직무구분 cbo 변경 이벤트
	$('#cboDuty').bind('change', function() {
		
		$('#chkAll').wCheck('check', false);
		checkAllRgtCdList();
		
		var selRgtCd = getSelectedVal('cboDuty').cm_micode;
		$('#chkDuty' + selRgtCd).wCheck('check', true);
		
		var data = new Object();
		data = {
			rgtcd 		: selRgtCd,
			requestType	: 	'getRgtMenuList'
		}
		ajaxAsync('/webPage/ecmm/Cmm0600Servlet', data, 'json',successGetRgtMenuList);
	});
	
	// 직무리스트 전체선택
	$('#chkAll').bind('click', function() {
		checkAllRgtCdList();
	});
	
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
		var addId = null;
		var dutyList = [];
		var menuList = [];
		dutyUlInfoData.forEach(function(item, index) {
			addId = item.cm_micode;
			if($('#chkDuty'+addId).is(':checked')) {
				dutyList.push(item);
			}
		});
		
		if(dutyList.length === 0 ) {
			dialog.alert('직무구분을 선택하세요.',function(){});
			return;
		}
		
		var checkedNodes= treeObj.getCheckedNodes(true);
		if(checkedNodes.length === 0) {
			dialog.alert('부여할 메뉴를 선택한 후 등록하십시오.',function(){});
			return;
		}
		
		checkedNodes.forEach(function(item, index) {
			/*if(!item.isParent) {
				menuList.push(item);
			}*/
			if(item.pId  !== null) {
				var tmpItem = null;
				if(item.isParent) {
					tmpItem = new Object();
					tmpItem.cm_menucd = item.cm_menucd;
					menuList.push(tmpItem);
					tmpItem = null;
				} else {
					menuList.push(item);
				}
				
			}
		})
		console.log(menuList);
		var data = new Object();
		data = {
			Lst_Duty 	: dutyList,
			treeMenu 	: menuList,
			UserId		: userId,
			requestType	: 	'setRgtMenuList'
		}
		ajaxAsync('/webPage/ecmm/Cmm0600Servlet', data, 'json',successSetRgtMenuList);
		
	});
});

// 직무권한 리스트 전체 선택 또는 해제
function checkAllRgtCdList() {
	var checkSw = $('#chkAll').is(':checked');
	dutyUlInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#chkDuty'+addId).wCheck('check', true);
		} else {
			$('#chkDuty'+addId).wCheck('check', false);
		}
	});
}

// 적용 완료
function successSetRgtMenuList(data) {
	dialog.alert('적용되었습니다.', function(){});
}

// 직무구분 선택시 메뉴가져와서 세팅
function successGetRgtMenuList(data) {

	var firstNode = null;
	treeObj.checkAllNodes(false);
	data.forEach(function(item, index) {
		var node = treeObj.getNodeByParam('id', item.cm_menucd);
		if(node !== null && !node.isParent) {
			if(firstNode == null){
				firstNode = node;
			} else if (firstNode.pId > node.pId || firstNode.cm_order > node.cm_order){
				firstNode = node;
			}
			treeObj.selectNode(node);
			treeObj.checkNode(node, true, true);
		}
	});
	treeObj.selectNode(firstNode);
	treeObj.cancelSelectedNode(firstNode);
	if(data.length < 1){
		treeObj.expandAll(false);
	}
}

// 직무구분 cbo 가져오기 / 직무구분 ul 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('RGTCD','SEL','N','2',''),
		new CodeInfoOrdercd('RGTCD','SEL','N','2','')
	]);
	cboDutyData 	= codeInfos.RGTCD;
	dutyUlInfoData 	= codeInfos.RGTCD;	

	$('[data-ax5select="cboDuty"]').ax5select({
        options: injectCboDataToArr(cboDutyData, 'cm_micode' , 'cm_codename')
	});
	makeDutyUlList();
}

// 메뉴트리 정보 가져오기
function getMenuTree() {
	var data = new Object();
	data = {
		requestType	: 	'getMenuZTree'
	}
	ajaxAsync('/webPage/ecmm/Cmm0500Servlet', data, 'json',successGetMenuTree);
}

// 메뉴트리 정보 가져오기 완료
function successGetMenuTree(data) {
	treeObjData = data;
	$.fn.zTree.init($("#tvMenu"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("tvMenu");
}


//시스템 속성 ul 만들어주기
function makeDutyUlList() {
	$('#dutyUlInfo').empty();
	var liStr = null;
	var addId = null;
	liStr  = '';
	dutyUlInfoData.forEach(function(item, index) {
		if(index !== 0) {
			addId = item.cm_micode;
			liStr += '<li class="list-group-item">';
			liStr += '<div class="maring-3-top" style="padding: 5px 0;">';
			liStr += '	<input type="checkbox" class="checkbox-duty" id="chkDuty'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
			liStr += '</div>';
			liStr += '</li>';
		}
	});
	$('#dutyUlInfo').html(liStr);
	
	$('input.checkbox-duty').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

