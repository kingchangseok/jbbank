/**
 * 시스템상세정보 팝업 [Compile Script 등록] 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2022-08-00
 * 
 */

var userId 			= window.parent.userId;
var selectedSystem  = window.parent.selectedSystem;

var scriptGrid		= new ax5.ui.grid();
var scriptGridData	= [];
var gubun			= '01';

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	if (window.parent.frmLoad9) createViewGrid();
});

function createViewGrid() {
	scriptGrid.setConfig({
	    target: $('[data-ax5grid="scriptGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    header: {
	        align: "center",
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	            clickScriptGrid(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_compscript", 	label: "스크립트명", 	width: '100%', 	align: "left"}
	    ]
	});
	
	screenLoad();
}

function screenLoad() {
	if (selectedSystem != null) {
		getScriptList(selectedSystem.cm_syscd);
		
		// 조회
		$('#btnQry').bind('click', function() {
			getScriptList(selectedSystem.cm_syscd);
		});
		
		// 등록
		$('#btnReq').bind('click', function() {
			btnReqClick();
		});
		
		// 폐기
		$('#btnDel').bind('click', function() {
			if(scriptGrid.getList('selected').length == 0) {
				dialog.alert('폐기하실 스크립트를 선택하여 주십시오.');
				return;
			}
			
			var selectedGridItem = scriptGrid.list[scriptGrid.selectedDataIndexs];
			
			confirmDialog.confirm({
				title: "폐기 확인",
				msg: "선택하신 스크립트를 폐기하시겠습니까?"
			}, function() {
				if(this.key == "ok") {
					var data = {
						SysCd : selectedSystem.cm_syscd,
						CompCd : selectedGridItem.cm_compcd,
						gubun : selectedGridItem.cm_gubun,
						requestType	: 'delScriptInfo'
					}
					ajaxAsync('/webPage/ecmm/Cmm0200Servlet', data, 'json', successDelScriptInfo);
				}
			});
		});
		
		//신규
		$('#chkNew').bind('click',function(){
			$('#txtCode').val('');
		});
	}
}

function getScriptList(syscd) {
	$('#txtCode').val('');
	$('#txtShell').val('');
	
	var data = new Object();
	data = {
		SysCd : syscd,
		requestType : 'getScriptList'
	}
	
	$('[data-ax5grid="scriptGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', data, 'json', successGetScriptList);
	data = null;
}

function successGetScriptList(data) {
	$(".loding-div").remove();
	
	data = data.filter(function(item) {
		if(item.cm_gubun == gubun) return true;
		else return false;
	});
	scriptGridData = data;
	scriptGrid.setData(scriptGridData);
}

function clickScriptGrid(index) {
	var selItem = scriptGrid.list[index];

	if (selItem == null || selItem == undefined) return;
	
	$('#txtCode').val(selItem.cm_compcd);
	$('#txtShell').val(selItem.cm_compscript);
}

function successDelScriptInfo(data) {
	if(data) {
		getScriptList(selectedSystem.cm_syscd);
	}else {
		dialog.alert('Make스크립트 폐기 실패');
		return;
	}
}

function btnReqClick() {
	if($('#txtShell').val().length == 0) {
		dialog.alert('Script명을 입력하여 주십시오.');
		return;
	}
	
	var tmpData = new Object();
	tmpData.syscd = selectedSystem.cm_syscd;
	tmpData.gubun = gubun;
	tmpData.value = $('#txtShell').val();
	if($('#txtCode').val().length == 0) tmpData.code = '';
	else tmpData.code = $('#txtCode').val();
	
	var data = new Object();
	data = {
		Data : tmpData,
		requestType : 'setScriptcode'
	}
	
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', data, 'json', successSetScriptcode);
	data = null;
}

function successSetScriptcode(data) {
	if(data) {
		$('#chkNew').wCheck("check", false);
		getScriptList(selectedSystem.cm_syscd);
	}else {
		dialog.alert('Make스크립트 등록 실패');
		return;
	}
}