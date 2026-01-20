var userid	= window.top.userId;
var selectedData = window.parent.popData;
var gbn = window.parent.popupGbn;
var cboDefaultData;

$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(this).ready(function() {
//	var codeInfos = getCodeInfoCommon([
//		new CodeInfo('CHKDEFAULT', 'SEL','N')
//	]);
//	cboDefaultData    = codeInfos.CHKDEFAULT;
//	codeList = null;
//	$('[data-ax5select="cboDefault"]').ax5select({
//        options: injectCboDataToArr(cboDefaultData, 'cm_micode' , 'cm_codename')
//   	});
	$('#optaftYN').wRadio('check', true);
	
	$('input:checkBox[name=checkEssential]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	$('input.checkbox-user').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
	
	$("#btnQry").bind('click', function() {
		submit();
	})
	
	$("#btnCancel").bind('click', function() {
		btnClose_Click();
	})
	
	if(gbn === "RENAME") {
//		if( selectedData.cm_befyn === "Y" ) {
//			$('#optbefYN').wRadio('check', true);
//		}
//		if( selectedData.cm_default === "Y" ) {
//			$('[data-ax5select="cboDefault"]').ax5select('setValue', 'Y', true);
//		} else if( selectedData.cm_default === "N" ) {
//			$('[data-ax5select="cboDefault"]').ax5select('setValue', 'N', true);
//		} else if( selectedData.cm_default === "P" ) {
//			$('[data-ax5select="cboDefault"]').ax5select('setValue', 'P', true);
//		} else {
//			$('[data-ax5select="cboDefault"]').ax5select('setValue', 'X', true);
//		}
		$("#content").val(selectedData.name);
	}
	
})

function btnClose_Click() {
	window.parent.checklistModal.close();
}


//확인 클릭 시
function submit() {
	
//	if(getSelectedIndex('cboDefault') < 1) {
//		dialog.alert('기본값을 선택하여 주십시오.',function(){});
//		return;
//	}
	
	if(gbn === "EQUAL" || gbn === "LOW") {
		newItemInfo();
	} else if(gbn === "RENAME") {
		updateItemInfo();
	}
}

//항목 등록
function newItemInfo() {
	
	var dataObj = new Object();
	var ajaxData = new Object();
	dataObj.cm_uppgbncd = (gbn === "EQUAL" ? selectedData.pId : selectedData.id);
	dataObj.cm_gbnname = $("#content").val();
	dataObj.cm_lstusr = userid;
	
//	dataObj.cm_befyn = $("#optbefYN").is(":checked") ? "Y" : "N";
//	dataObj.cm_aftyn = $("#optaftYN").is(":checked") ? "Y" : "N";
//	dataObj.cm_default = getSelectedVal('cboDefault').value;
	dataObj.cm_gbncd = selectedData.cm_gbncd;
	dataObj.cm_reqyn = $("#chkReq").is(":checked") ? "Y" : "N";
	dataObj.cm_typecd = selectedData.cm_typecd;
	ajaxData = {
		dataObj : dataObj,
		requestType : "newItemInfo"
	}
//	console.log('[1]:',dataObj);
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', ajaxData, 'json', successNewItemInfo);	
}

//항목 등록 성공
function successNewItemInfo(data) {
	if(data === "OK") {		
		dialog.alert({
			msg: "정상적으로 등록되었습니다.",
			onStateChanged: function() {
				if(this.state === "close") {
					btnClose_Click();
					window.parent.getTree();					
				}
			}
		})
	} else {
		dialog.alert("등록에 실패하였습니다.")
		btnClose_Click();
	}	
}

//항목명 변경
function updateItemInfo() {
	
	var dataObj = new Object();
	var ajaxData = new Object();
//	dataObj.cm_gbncd = selectedData.id;
	dataObj.cm_gbnname = $("#content").val();
	dataObj.cm_lstusr = userid;

//	dataObj.cm_befyn = $("#optbefYN").is(":checked") ? "Y" : "N";
//	dataObj.cm_aftyn = $("#optaftYN").is(":checked") ? "Y" : "N";
//	dataObj.cm_default = getSelectedVal('cboDefault').value;
	dataObj.cm_gbncd = selectedData.cm_gbncd;
	dataObj.cm_reqyn = $("#chkReq").is(":checked") ? "Y" : "N";
	dataObj.cm_typecd = selectedData.cm_typecd;
	ajaxData = {
		dataObj : dataObj,		
		requestType : "updateItemInfo"
	}
//	console.log('[2]:',ajaxData);
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', ajaxData, 'json', successUpdateItemInfo);	
}

//항목명 변경 성공
function successUpdateItemInfo(data) {
	if(data === "OK") {		
		window.parent.dialog.alert("정상적으로 수정되었습니다.")
		btnClose_Click();
		window.parent.getTree();
	} else {
		window.parent.dialog.alert("수정에 실패하였습니다.")
		btnClose_Click();
	}
}
