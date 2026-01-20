/**
 * 프로그램 상세 (eCmr0170.mxml)
 * 
 * 	작성자: 진가윤
 * 	버전 : 1.0
 *  수정일 : 2022-09-00
 * 
 */

var pUserId 	= null;
var pAcptNo	    = null;
var pItemId	    = null;
var pReqCd	    = null;

var orderId	= null;

var f = document.getReqData;
pUserId = f.userId.value;
pAcptNo = f.acptno.value;
pItemId = f.itemid.value;
pReqCd = pAcptNo.substr(4,2);

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();

/* 업무지시서 추가/제거 modal */
var addOrderModal	= new ax5.ui.modal();	//업무지시서 추가/제거
var addOrderArr		= [];
var addOrderData	= [];
var addOrderFlag	= false;

$('#datReturn').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

var firstGridData		= [];
var secondGridData		= [];
var infoData			= [];
var cboSysData			= [];

var isCaller = false;
var isAbled  = false;

var returnDate = "";
var popSelItem = null;

$('[data-ax5select="cboSys"]').ax5select({
	option: []
});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multiselect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    page: false,
    header: {
        align: 'center'
    },
    body: {
    	onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            firstGrid_Click();
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        },
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'orderid', 	label: '지시번호',  	width: '45%',	align: 'center'},
        {key: 'reqsub', 	label: '업무지시제목', 	width: '55%',	align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 
    multiSort: true,
    multiselect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    page: false,
    header: {
        align: 'center'
    },
    body: {
    	onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            secondGrid_Click();
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        },
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'cc_reqid', 	label: '요청번호',  	width: '45%',	align: 'center'},
        {key: 'cc_docsubj', label: '개발요청제목', 	width: '55%',	align: 'left'}
    ]
});

$(document).ready(function(){
	//수정
	$('#btnReWrite').bind('click', function() {
		btnReWrite_Click();
	});
	
	//개발요청서보기
	$('#btnView2').bind('click', function() {
		if(reqObj.length == 0) return;
		
		if(reqObj.cc_doctype == '01' || reqObj.cc_doctype == '02') openWindow('1'); //개발요청상세(eCmc0401.mxml)
		else if(reqObj.cc_doctype == '03') openWindow('2'); //외주개발요청상세(eCmc0411.mxml)
	});
	
	//업무지시서 보기
	$('#btnView1').bind('click', function() {
		openWindow('3');
	});

	//업무지시서 추가/제거
	$('#btnOrderAdd').bind('click', function() {
		openAddOrderModal();
	});

	//닫기
	$('#btnClose').bind('click', function() {
		close();
	});
	
	getSysInfo();
	
});

function getSysInfo() {
	var data = new Object();
	data = {
		UserId : pUserId,
		SecuYn : "Y",
		SelMsg : "",
		CloseYn : "n",
		ReqCd : pReqCd,
		requestType : 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysData = data;
	if(cboSysData.length == 0) {
		dialog.alert("시스템이 존재하지 않습니다.");
		return;
	}
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	getProgDetail();
}

function getProgDetail() {
	var data = new Object();
	data = {
		AcptNo : pAcptNo,
		ItemId : pItemId,
		requestType : 'getProgDetail'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json', successGetProgDetail);
}

function successGetProgDetail(data) {
	infoData = data;
	if (infoData <= 0) {
		dialog.alert("상세 정보 받기 실패");
		return;
	}
	
	var data = new Object();
	data = {
		AcptNo : pAcptNo,
		ItemId : infoData[0].cr_itemid,
		requestType : 'getOrders'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetOrders);
	
	var tmpObj = infoData[0];
	if (tmpObj.cr_expday != undefined) returnDate = tmpObj.cr_expday;
	
	if (returnDate != "" || tmpObj.returnDate != null) $('#datReturn').val(returnDate);
	
	$('#txtProg').val(tmpObj.cr_rsrcname);
	$('#txtSayu').val(tmpObj.cr_editcon);
	pItemId = tmpObj.cr_itemid;
	
	for(var i = 0; cboSysData.length; i++) {
        if(cboSysData[i].cm_syscd == tmpObj.cr_syscd){
        	$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[i].cm_syscd,true);
        	break;
        }
    }
	
	if(tmpObj.cr_status != "3" && tmpObj.cr_status != "9") isAbled = true;
	
	if(tmpObj.cr_editor == pUserId) isCaller = true;
	
	if(isCaller && isAbled) enableComp(true);
	else enableComp(false);
}

function successGetOrders(data) {
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function firstGrid_Click() {
	selectedGridItem = firstGrid.list[firstGrid.selectedDataIndexs];
	// 개발요청서 보기
	$('#btnView2').prop("disabled", true);
	
	if (firstGrid.selectedDataIndexs < 0 || firstGridData.length <= 0) {
		// 업무지시서 보기
		$('#btnView1').prop("disabled", true);
		return;
	} else {
		orderId = selectedGridItem.orderid;
		$('#btnView1').prop("disabled", false);
	}
	
	var data = new Object();
	data = {
		OrderId : selectedGridItem.orderid,
		requestType : 'getReqs'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0250Servlet', data, 'json');
	secondGridData = ajaxReturnData;
	
	secondGrid.setData(secondGridData);
	
	if(secondGridData.length > 0) reqObj = secondGridData[0];
}

function secondGrid_Click() {
	gridSelectedIndex = secondGrid.selectedDataIndexs;
	selectedGridItem = secondGrid.list[secondGrid.selectedDataIndexs];
	
	if (gridSelectedIndex < 0 || secondGridData.length <= 0) {
		$('#btnView2').prop("disabled", true);
		return;
	}
	orderId = selectedGridItem.cc_orderid;
	$('#btnView2').prop("disabled", false);
}

function btnReWrite_Click() {
	var tmpArc = new Array();
    if (firstGridData.length > 0) {
        for (var i = 0; i < firstGridData.length; i++) {
            var tmpObj = new Object();
            tmpObj.seq = i + 1;
            tmpObj.acptno = pAcptNo;
            tmpObj.itemid = pItemId;
            tmpObj.orderid = firstGridData[i].orderid;
            tmpObj.reqsub = firstGridData[i].reqsub;
                 
            tmpArc.push(tmpObj);
            tmpObj = null;
        }
    }
    
    var strDate = replaceAllString($("#datReturn").val(), "/", "");
    var today = getDate('DATE',0);

        if (strDate < today) {
           	dialog.alert("반환예정일이 현재일 이전입니다. 정확히 선택하여 주십시오");
           	return;
        }
        
        var tempObj = new Object();
        tempObj.cr_expday = replaceAllString($("#datReturn").val(), "/", "");
        tempObj.cr_editcon = $('#txtSayu').val().trim();
        
        data = {
        	AcptNo : pAcptNo,
        	ItemId : pItemId,
        	etcData : tempObj,
        	etcData2 : tmpArc,
        	requestType : 'setProgDetail'
        }
        ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json', successSetProgDeatil);
}

function successSetProgDeatil(data) {
	if(data) dialog.alert("수정 완료");
	else dialog.alert("수정 실패");
}

function enableComp(isTrue) {
	$('[data-ax5select="cboSys"]').ax5select("disable");
	$('#txtProg').attr("readonly", true);
	
	if (isTrue) {
		$("#datReturn").prop("disabled", false);
		$("#btnReturn").prop("disabled", false);
		$('#txtSayu').prop("disabled", false);
		$('#txtSayu').attr("readonly", false);
		$('#btnOrderAdd').prop("disabled", false);
		$('#btnReWrite').show();
		$('#btnReWrite').prop("disabled", false);
	} else {
		$("#datReturn").prop("disabled", true);
		$("#btnReturn").prop("disabled", true);
		$('#txtSayu').prop("disabled", true);
		$('#txtSayu').attr("readonly", true);
		$('#btnOrderAdd').prop("disabled", true);
		$('#btnReWrite').hide();
		$('#btnReWrite').prop("disabled", true);
	}
}

function openAddOrderModal() {
	addOrderArr = clone(firstGridData);
	setTimeout(function() {
		addOrderModal.open({
			width: 800,
			height: 600,
			iframe: {
				method: "get",
				url: "../modal/AddOrderModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					if (addOrderFlag) {
						firstGridData = clone(addOrderData);
					}
					firstGrid.setData(firstGridData);
					mask.close();
				}
			}
		}, function () {
			
		});
	}, 200);
}

function openWindow(type) {
	var nHeight, nWidth, cURL, winName;

	if ( ('pop_'+type) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
	
	winName = type+'_pop_'+pReqCd;

    nWidth  = 1046;
	nHeight = 700;
	
	selectedGridItem = firstGrid.list[firstGrid.selectedDataIndexs];
	var f = document.setReqData;
	f.user.value = pUserId;
	f.orderId.value = selectedGridItem.orderid;
	
	if (type === '1') {//개발요청상세(eCmc0401.mxml)
		cURL = "/webPage/winpop/PopDevRequestDetail.jsp";
	} else if (type === '2') {//외주개발요청상세(eCmc0411.mxml)
		cURL = "/webPage/winpop/PopOutSourcingDetail.jsp";
	} else if (type === '3') {
		cURL = "/webPage/winpop/PopOrderListDetail.jsp";
	} else {
		confirmDialog2.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}