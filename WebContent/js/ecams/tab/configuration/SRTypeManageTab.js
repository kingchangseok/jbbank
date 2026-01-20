/**
 * [환경설정 > 운영시간관리 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-24
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var codeList    = window.top.codeList;      	//전체 코드 리스트

var cboCattypeData		= null;
var ulReqInfoData		= null;


$('[data-ax5select="cboCattype"]').ax5select({
    options: []
});

$(document).ready(function() {
	getCodeInfo();
	getSrCatType();
	$('#cboCattype').bind('change', function() {
		getSrCatType();
	});
	
	$('#btnReq').bind('click', function() {
		setSrCatType();
	});
});


// SR 유형관리 세팅
function setSrCatType() {
	var typeList = [];
	var typeObj = null;
	if(getSelectedIndex('cboCattype') < 1) {
		dialog.alert('분류유형을 선택하십시오.', function() {});
		return;
	}
	
	ulReqInfoData.forEach(function(item, index) {
		typeObj = new Object();
		typeObj.cm_cattype 	= getSelectedVal('cboCattype').value;
		typeObj.cm_qrycd 	= item.cm_micode;
		
		if($('#chkReq'+item.cm_micode).is(':checked')){
			typeObj.cm_useyn = "Y";
		}else{
			typeObj.cm_useyn = "N";
		}
		typeList.push(typeObj);
	});
	
	var data = new Object();
	data = {
		typeList 	: typeList,
		requestType	: 'setSrCattype'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successSetSrCatType);
}

// SR 유형관리 세팅 완료
function successSetSrCatType(data) {
	if(data > 0 ) {
		dialog.alert('분류유형이 등록되었습니다.', function() {});
	} else {
		dialog.alert('분류유형이 등록을 실패하였습니다.', function() {});
	}
	$('#cboCattype').trigger('change');
}

// 신청구분 리스트 초기화
function initReqUlList(checkSw) {
	ulReqInfoData.forEach(function(reqItem, reqIndex){
		if(checkSw) {
			$('#chkReq'+reqItem.cm_micode).wCheck('check', true);
		} else {
			$('#chkReq'+reqItem.cm_micode).wCheck('check', false);
		}
	});
}

// 분류유형에 따른 신청구분 가져오기
function successGetSrCatType(data) {
	initReqUlList(false); 
	data.forEach(function(item, index) {
		ulReqInfoData.forEach(function(reqItem, reqIndex){
			if(item.cm_qrycd === reqItem.cm_micode && item.cm_useyn === 'Y') {
				$('#chkReq'+reqItem.cm_micode).wCheck('check', true);
			}
		});
	});
}

function getSrCatType() {
	if(getSelectedIndex('cboCattype') < 1) {
		return;
	}
	
	var data = new Object();
	data = {
		cattype 	: getSelectedVal('cboCattype').value,
		requestType	: 'getSrCattype'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetSrCatType);
}

//업무 중, 후 구분가져오기
function getCodeInfo() {
	
	var codeInfos = getCodeInfoCommon([
										new CodeInfo('CATTYPE','SEL','N'),
										new CodeInfo('REQUEST','','N')
										]);
	cboCattypeData 	= codeInfos.CATTYPE;
	ulReqInfoData	= codeInfos.REQUEST;
	
//	cboCattypeData   = fixCodeList(codeList['SRTYPE'], 'SEL', '', '', 'N');
//	ulReqInfoData   = fixCodeList(codeList['REQUEST'], '', '', '', 'N');
//	codeList = null;
	
	$('[data-ax5select="cboCattype"]').ax5select({
        options: injectCboDataToArr(cboCattypeData, 'cm_micode' , 'cm_codename')
	});
	
	makeReqInfoUlList();
}

//요청 리스트
function makeReqInfoUlList() {
	$('#ulReqInfo').empty();
	var liStr = '';
	var addId = null;
	ulReqInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		liStr += '<li class="list-group-item">';
		liStr += '	<input type="checkbox" class="checkbox-req" id="chkReq'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#ulReqInfo').html(liStr);
	
	$('input.checkbox-req').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}