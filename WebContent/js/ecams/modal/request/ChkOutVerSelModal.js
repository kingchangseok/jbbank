
var itemId 		= window.parent.pItemId;			//프로그램id: itemid
var userId 		= window.parent.pUserId;			//접속자 ID
var reqCd		= window.parent.reqCd;				//신청구분

var grdReqVersion  = new ax5.ui.grid();
var confirmDialog  = new ax5.ui.dialog();   		//확인 창

var grdReqVersionData = null; 						//버전목록 데이타
var data              = null;						//json parameter

var selectVer     = ''; //선택버전
var selectViewVer = 'sel'; //선택버전
var selectAcptno  = '';    //선택한신청버전

confirmDialog.setConfig({
	Title: "확인",
    theme: "info",
    width: 500
});

grdReqVersion.setConfig({
    target: $('[data-ax5grid="grdReqVersion"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) {
    			confirmDialog.alert('체크아웃 할 버전의 데이터를 선택하여 주십시요.');
    			return;
        	}
        	
			var gridSelectedIndex = grdReqVersion.selectedDataIndexs;
			if (gridSelectedIndex.length == 0) {
	       		confirmDialog.alert('체크아웃 할 버전의 데이터를 선택하여 주십시요.');
				return;
			}

			selectViewVer = this.item.cr_ver;
			selectVer = this.item.cr_ver;
			selectAcptno = this.item.cr_acptno;
			popClose(true);
			
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{key: "prcdate", 		label: "적용일시",  	width: '25%'},
        {key: "cm_username", 	label: "신청자",  	width: '10%'},
        {key: "cr_ver", 		label: "버전",  		width: '10%'},
        {key: "acptdate", 		label: "신청일시",  	width: '25%'},
        {key: "acptno", 		label: "신청번호",  	width: '20%'}
    ]
});

$(document).ready(function() {
	//버전리스트 가져오기
	data = new Object();
	data = {
			 itemID	: itemId,
			 ReqCD	: reqCd,
		requestType : 'getVerList'
	}
	console.log('getVerList',data);
	ajaxAsync('/webPage/ecmr/Cmr0100_selectVerServlet', data, 'json', successGetVerList);
	
	//X버튼 닫기
	$('#btnExit').bind('click', function() {
		popClose(false);
	});
	//하단 닫기버튼
	$('#btnClose').bind('click', function() {
		popClose(false);
	});
	//선택 버튼
	$('#btnSel').bind('click', function() {
		
		var gridSelectedIndex = grdReqVersion.selectedDataIndexs;
		if (gridSelectedIndex.length == 0) {
       		confirmDialog.alert('선택 할 버전의 데이터를 선택하여 주십시요.');
			return;
		}
		
		var selectedGridItem = grdReqVersion.list[grdReqVersion.selectedDataIndexs];
		selectVer = selectedGridItem.cr_ver;
		selectViewVer = selectedGridItem.cr_ver;
		selectAcptno = selectedGridItem.cr_acptno;
		console.log(selectedGridItem);
		popClose(true);
	});
	
	//선택버전초기화 버튼
	$('#btnReset').bind('click', function() {
		selectViewVer = 'sel';
		selectVer = '';
		selectAcptno = '';
		popClose(true);
	});
});
//버전리스트 가져오기 완료
function successGetVerList(data){
	console.log(data);
	grdReqVersionData = data;
	grdReqVersion.setData(grdReqVersionData);
}
//모달닫기
function popClose(updateflag) {
	window.parent.updateFlag = updateflag;
	window.parent.selectVer = selectVer;
	window.parent.selectViewVer = selectViewVer;
	window.parent.selectAcptno = selectAcptno;
	window.parent.ChkOutVerSelModal.close();
}