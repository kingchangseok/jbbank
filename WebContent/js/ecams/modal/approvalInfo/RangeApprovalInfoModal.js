/**
 * [결재정보 > 대결가능범위등록] 팝업 기능정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-05
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var grdRangeList		= new ax5.ui.grid();

var grdRangeListData 		= [];
var cboPosData				= []; //직위 콤보박스 데이터
var cboRgtData				= []; //직무 콤보박스 데이터
var lstPosData				= []; //대결가능범위 체크리스트 데이터
var lstRgtData				= []; //대결가능범위 체크리스트 데이터
var addId 					= [];

var tmpInfo     			= new Object(); 
var tmpInfoData 			= new Object();

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

grdRangeList.setConfig({
    target: $('[data-ax5grid="grdRangeList"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showRowSelector: false,
    showLineNumber: false,
    header: {
        align: "center",
        selector: false
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "gbncd", 		label: "구분",  		width: 70},
        {key: "cm_teamck",	label: "동일팀",  	width: 100},
        {key: "cm_rpos", 	label: "직위",  		width: 100},
        {key: "cm_spos", 	label: "대결직위", 	width: 100}
    ]
});

$(document).ready(function() {
	// 직위,직무 콤보박스 변경이벤트
	$('#cboPos').bind('change', function() {
		cboPos_Change();
	});
	
	// 직위 클릭이벤트
	$('#rdoPos').bind('click', function() {
		rdoPos_Click();
	});
	
	// 직무 클릭이벤트
	$('#rdoRgt').bind('click', function() {
		rdoRgt_Click();
	});
	
	// 직위 전체선택
	$('#chkAll').bind('click', function() {
		chkAll_Click();
	});
	
	// 조회
	$('#btnQry').bind('click', function() {
		btnQry_Click();
	});
	
	// 닫기
	$('#btnClose').bind('click', function() {
		btnClose_Click();
	});
	
	// 등록
	$('#btnReg').bind('click', function() {
		btnReg_Click();
	});
	
	// 폐기
	$('#btnDel').bind('click', function() {
		btnDel_Click();
	});
	
	$('#rdoRgt').wRadio("check", true);
	$('#chkTeam').wCheck("check", true);
	
	getCodeInfo();
	btnQry_Click();
});

function getCodeInfo(){

//	cboPosData = fixCodeList(window.top.codeList['POSITION'], '', 'cm_micode', 'ASC', 'N');
//	cboRgtData = fixCodeList(window.top.codeList['RGTCD'], '', 'cm_micode', 'ASC', 'N');
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('POSITION', '','N'),
		new CodeInfo('RGTCD', '','N')
	]);
	
	cboPosData = codeInfos.POSITION;
	cboRgtData = codeInfos.RGTCD;

	lstPosData = cboPosData;
	lstRgtData = cboRgtData;
	
	if($('#rdoRgt').is(':checked')) { //직무
		$('[data-ax5select="cboPos"]').ax5select({
	        options: injectCboDataToArr(cboRgtData, 'cm_micode' , 'cm_codename')
	   	});
		
		setLstPos(lstRgtData);
		rdoRgt_Click();
	}else { //직위
		$('[data-ax5select="cboPos"]').ax5select({
	        options: injectCboDataToArr(cboPosData, 'cm_micode' , 'cm_codename')
	   	});
		
		setLstPos(cboPosData);
		rdoPos_click();
	}	
}

function cboPos_Change() {
	if(getSelectedIndex('cboPos') < 0) return;
	
	var tmpGbn = "";
	var i = 0;
	
	if($('#rdoPos').is(':checked')) tmpGbn = "0";
	else tmpGbn = "1";
	
	if($('#rdoPos').is(':checked')) {
		lstPosData.forEach(function(item, index) {
			addId = item.cm_macode+"_"+item.cm_micode;
			$('#lstPos'+addId).wCheck('check', false);
		});
	}else {
		lstRgtData.forEach(function(item, index) {
			addId = item.cm_macode+"_"+item.cm_micode;
			$('#lstPos'+addId).wCheck('check', false);
		});
	}
	
	//Cmm0300_Blank.getBlankList(tmpGbn,cboPos.selectedItem.cm_micode);
	
	
	tmpInfoData = new Object();
	tmpInfoData = {
		GbnCd : tmpGbn,
		PosCd : getSelectedVal('cboPos').value,
		requestType	: 'getBlankList'
	}
	
	ajaxAsync('/webPage/ecmm/Cmm0300_BlankServlet', tmpInfoData, 'json', successGetBlankList);
}

function successGetBlankList(data) {
	var tmpData = data;
	var i = 0;
	var j = 0;
	var k = 0;
	
	//초기화
	for(j=0; j<tmpData.length; j++) {
		if($('#rdoPos').is(':checked')) { //직위
			for(i=0; i<lstPosData.length; i++) {
				addId = lstPosData[i].cm_macode + "_" + lstPosData[i].cm_micode;
				$('#lstPos'+addId).wCheck('check', false);
			}
		}else { //직무
			for(i=0; i<lstRgtData.length; i++) {
				addId = lstRgtData[i].cm_macode + "_" + lstRgtData[i].cm_micode;
				$('#lstPos'+addId).wCheck('check', false);
			}
		}
	}
	
	for(j=0; j<tmpData.length; j++) {
		if($('#rdoPos').is(':checked')) { //직위
			for(i=0; i<lstPosData.length; i++) {
				if(tmpData[j].cm_sposition == lstPosData[i].cm_micode) {
					addId = lstPosData[i].cm_macode + "_" + lstPosData[i].cm_micode;
					$('#lstPos'+addId).wCheck('check', true);
				}
			}
		}else { //직무
			for(i=0; i<lstRgtData.length; i++) {
				if(tmpData[j].cm_sposition == lstRgtData[i].cm_micode) {
					addId = lstRgtData[i].cm_macode + "_" + lstRgtData[i].cm_micode;
					$('#lstPos'+addId).wCheck('check', true);
				}
			}
		}
	}
}

function rdoPos_Click() {
	$('[data-ax5select="cboPos"]').ax5select({
        options: injectCboDataToArr(cboPosData, 'cm_micode' , 'cm_codename')
   	});
	
	setLstPos(lstPosData);
	$('#chkAll').wCheck("check", false);
	$('#chkAll').wCheck("disabled", false);
	btnQry_Click();
}

function rdoRgt_Click() {
	$('[data-ax5select="cboPos"]').ax5select({
        options: injectCboDataToArr(cboRgtData, 'cm_micode' , 'cm_codename')
   	});
	
	setLstPos(lstRgtData);
	$('#chkAll').wCheck("check", false);
	$('#chkAll').wCheck("disabled", true);
	
	if(getSelectedIndex('cboPos') > -1) {
		cboPos_Change();
	}
	
	btnQry_Click();
}

function btnQry_Click() {
	grdRangeList.setData([]);
	var GbnCd = "";
	if($('#rdoPos').is(':checked')) GbnCd = "0";
	else  GbnCd = "1";
	
	tmpInfoData = new Object();
	tmpInfoData = {
			PosCd : "",
			GbnCd : GbnCd,
		requestType	: 'getBlankList'
	}
	
	$('[data-ax5grid="grdRangeList"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0300_BlankServlet', tmpInfoData, 'json', successGetBlankList2);
}

function successGetBlankList2(data) {
	$(".loding-div").remove();
	grdRangeListData = data;
	grdRangeList.setData(grdRangeListData);
	
	cboPos_Change();
}

function btnClose_Click() {
	window.parent.rangeApprovalInfoModal.close();
}

//등록버튼 클릭 이벤트
function btnReg_Click() {
	var strGbn = "";
	var strTeam = "";
	var strPos = "";
	var i = 0;
	
	if(!$('#rdoRgt').is(':checked') && !$('#rdoPos').is(':checked')) {
		dialog.alert('직위/직무여부를 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(getSelectedIndex('cboPos') < 0) {
		dialog.alert('직위/직무구분을 선택하여 주시기 바랍니다.',function(){});
		$('#cboPos').focus();
		return;
	}
	
	if($('#rdoRgt').is(':checked')) {
		for(i=0; i<lstRgtData.length; i++) {
			if($('#lstPos'+lstRgtData[i].cm_macode+"_"+lstRgtData[i].cm_micode).is(':checked')) break;
		}
		
		if(i>=lstRgtData.length) {
			dialog.alert('대결가능범위를 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}else {
		for(i=0; i<lstPosData.length; i++) {
			if($('#lstPos'+lstPosData[i].cm_macode+"_"+lstPosData[i].cm_micode).is(':checked')) break;
		}
		
		if(i>=lstPosData.length) {
			dialog.alert('대결가능범위를 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	if($('#rdoRgt').is(':checked')) strGbn = "1";
	else strGbn = "0";
	
	if($('#chkTeam').is(':checked')) strTeam = "Y";
	else strTeam = "N";
	
	tmpPos = "";
	if($('#rdoRgt').is(':checked')) {
		for(i=0; i<lstRgtData.length; i++) {
			if($('#lstPos'+lstRgtData[i].cm_macode+"_"+lstRgtData[i].cm_micode).is(':checked')) {
				if(tmpPos.length > 0) tmpPos = tmpPos + ",";
				tmpPos = tmpPos + lstRgtData[i].cm_micode;
			}
		}
	}else {
		for(i=0; i<lstPosData.length; i++) {
			if($('#lstPos'+lstPosData[i].cm_macode+"_"+lstPosData[i].cm_micode).is(':checked')) {
				if(tmpPos.length > 0) tmpPos = tmpPos + ",";
				tmpPos = tmpPos + lstPosData[i].cm_micode;
			}
		}
	}

	//Cmm0300_Blank.blankUpdt(tmpObj);
	tmpInfo = new Object();
	tmpInfo.cm_gbncd = strGbn;
	tmpInfo.cm_teamck = strTeam;
	tmpInfo.userid = userId;
	tmpInfo.cm_rposition = getSelectedVal('cboPos').value;
	tmpInfo.cm_sposition = tmpPos;
	
	tmpInfoData = new Object();
	tmpInfoData = {
		etcData		: tmpInfo,
		requestType	: 'blankUpdt'
	}
	
	ajaxAsync('/webPage/ecmm/Cmm0300_BlankServlet', tmpInfoData, 'json', successUpdateBlankList);
}

function successUpdateBlankList(data) {
	dialog.alert('대결범위정보 등록처리를 완료하였습니다.',function(){
		btnQry_Click();
		cboPos_Change();
	});
}

//폐기이벤트 클릭 이벤트
function btnDel_Click() {
	var strGbn = "";
	var i = 0;
	
	var gridSelectedIndex = grdRangeList.selectedDataIndexs;
	var selectedGridItem = grdRangeList.list[grdRangeList.selectedDataIndexs];
	var j = 0;
	
	if(gridSelectedIndex == "" || gridSelectedIndex < 0) {
		dialog.alert('폐기할 대결범위를 그리드에서 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(selectedGridItem.gbncd == "직무") strGbn = "1";
	else strGbn = "0";
	
	//Cmm0300_Blank.blankClose(tmpObj);
	tmpInfo = new Object();
	tmpInfo.cm_gbncd = strGbn;
	tmpInfo.userid = userId;
	tmpInfo.cm_rposition = selectedGridItem.cm_rposition;
	tmpInfo.cm_sposition = selectedGridItem.cm_sposition;
	
	tmpInfoData = new Object();
	tmpInfoData = {
		etcData		: tmpInfo,
		requestType	: 'blankClose'
	}
	
	ajaxAsync('/webPage/ecmm/Cmm0300_BlankServlet', tmpInfoData, 'json', successDELETEBlankList);
}

function successDELETEBlankList(data) {
	dialog.alert('대결범위정보에 대한 폐기처리를 완료하였습니다.',function(){
		btnQry_Click();
		cboPos_Change();
	});
}

function setLstPos(data) {
	$('#lstPos').empty();
	
	var liStr  = '';
	data.forEach(function(data, Index) {
		addId = data.cm_macode + "_" + data.cm_micode;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		liStr += '<div class="margin-3-top">';
		liStr += '	<input type="checkbox" class="checkbox-cm_micode" id="lstPos'+addId+'" data-label="'+data.cm_codename+'"  value="'+data.cm_micode+'" />';
		liStr += '</div>';
		liStr += '</li>';
		
	});
	$('#lstPos').html(liStr);
	
	$('input.checkbox-cm_micode').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

function chkAll_Click() {
	lstPosData.forEach(function(item, index) {
		addId = item.cm_macode + "_" + item.cm_micode;
		if($('#chkAll').is(':checked')) {
			$('#lstPos'+addId).wCheck('check', true);
		} else {
			$('#lstPos'+addId).wCheck('check', false);
		}
	});
}