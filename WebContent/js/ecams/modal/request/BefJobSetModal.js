var acptNo 		= window.parent.pReqNo;				
var userId 		= window.parent.pUserId;			
var reqCd 		= window.parent.reqCd;			

var firstGrid  	= new ax5.ui.grid();
var secondGrid  = new ax5.ui.grid();
var thirdGrid  	= new ax5.ui.grid();

var confirmDialog  = new ax5.ui.dialog();	//확인,취소 창
var confirmDialog2 = new ax5.ui.dialog();   //확인 창

var firstGridData 	= [];
var secondGridData 	= [];
var thirdGridData 	= [];
var data          	= [];
var prgData			= [];
var thirdGridSelect = false;

confirmDialog.setConfig({
    lang:{
        "ok": "확인", "cancel": "취소"
    },
    width: 500
});

function successDelBefJob(data) {
	if (data>0) {
		confirmDialog2.alert('선후행작업을 해제하였습니다.');
	} else {
		confirmDialog2.alert('선후행작업을 해제하지 못했습니다.');
	}
	return;
}

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
           this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if (this.item.colorsw == '3'){
    			return "fontStyle-cncl";
    		} else if(this.item.colorsw == '5'){
    			return "fontStyle-eror";
    		} else if (this.item.colorsw == '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        	addDataRow(this.item);
        	
        	if (this.dindex < 0) return;
        	
	       	var selIn = firstGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;   
        },
        onClick: function () {
        	getReqPgmList(this.item);
        }
    },
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "선택"},
            {type: 2, label: "신청프로그램목록"}
        ],
        popupFilter: function (item, param) {
        	if(param.dindex == undefined || param.dindex == null || param.dindex < 0){
        		return false;
        	}
         	return true;
       	 
        },
        onClick: function (item, param) {
        	if(item.type == '1'){
	        	addDataRow(param.item);
	        }else{
	        	getReqPgmList(param.item);
	        }
	        firstGrid.contextMenu.close();//또는 return true;
        	
        },
    },
    columns: [
        {key: "cr_acptno",	label: "신청번호",  	width: '15%'},
        {key: "acptdate",	label: "신청일시",  	width: '15%'},
        {key: "cm_sysmsg", 	label: "시스템",  	width: '20%',	align: 'left'},
        {key: "cm_username",label: "신청자",  	width: '10%'},
        {key: "cm_codename",label: "진행상태",		width: '15%'},
        {key: "cr_sayu",	label: "신청사유",		width: '25%', 	align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
           this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if (this.item.colorsw == '3'){
    			return "fontStyle-cncl";
    		} else if(this.item.colorsw == '5'){
    			return "fontStyle-eror";
    		} else if (this.item.colorsw == '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_dirpath", 	label: "프로그램경로",	width: '70%',align: 'left'},
        {key: "cr_rsrcname", 	label: "프로그램",  	width: '30%',align: 'left'}
    ]
});

thirdGrid.setConfig({
    target: $('[data-ax5grid="thirdGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
           this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if (this.item.colorsw == '3'){
    			return "fontStyle-cncl";
    		} else if(this.item.colorsw == '5'){
    			return "fontStyle-eror";
    		} else if (this.item.colorsw == '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = thirdGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	
	       	var befAcptNo = this.item.cr_acptno;
	       	
	    	/*
        	if(acptNo != null && acptNo != "") {
        		mask.open();
    	        confirmDialog.confirm({
    				title: '선행작업해제확인',
    				msg: '선택하신 신청 건을 선행작업에서 해제하시겠습니까?',
    			}, function(){
    				if(this.key === 'ok') {
    					var tmpData = {
    						acptNo		: 	acptNo,
    						befAcptNo  	:   befAcptNo,
    						requestType	: 	'deleteBefJob'
    					}
    					
    					ajaxAsync('/webPage/modal/request/BefJobListModalServlet', tmpData, 'json',successDelBefJob);
    				}
    				mask.close();
    			});
    			
        	}else {
        	*/
        		deleteDataRow(this);
        		secondGrid.setData([]);
        	//}
        }
    },
    columns: [
        {key: "cr_acptno", 	label: "신청번호",  	width: '30%'},
        {key: "acptdate",	label: "신청일시",  	width: '35%'},
        {key: "cm_username",label: "신청자",  	width: '35%'}
    ]
});

$(document).ready(function() {
	if(acptNo == null || acptNo == undefined){
		acptNo = "";
	}
	
	if(window.parent.befJobData != null || window.parent.befJobData != undefined){
		thirdGridData = window.parent.befJobData;
		thirdGrid.setData(thirdGridData);
		if(thirdGridData.length > 0){
			getReqPgmList(thirdGridData[0]);
			thirdGridSelect = true;
		}
	}
	
	$('#btnClose').bind('click',function() {
		if(window.parent.befJobData == null || window.parent.befJobData == undefined){
			window.parent.befJobData = [];
		}
		popClose();
	});
	
	$('#btnReq').bind('click',function(){
		window.parent.befJobData = thirdGrid.list;
		popClose();
	});
	
	getBefJob();
});

function popClose(){
	window.parent.befJobModal.close();
}

function getBefJob(){
	
	if(reqCd == null || reqCd == undefined || reqCd == null){
		reqCd = acptNo.substr(4,2);
	}
	var tmpData = {
		AcptNo		: acptNo,
		ReqCd   	: reqCd,
		requestType	: 'reqList_Select'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr0200_BefJobServlet', tmpData, 'json',successGetBefJob);
}

function successGetBefJob(data){
	firstGridData = data;
	firstGrid.setData(firstGridData);
	if(thirdGridSelect){
		firstGrid.select(0, {selected:true});
	}
}

//선행작업 추가
function addDataRow(data) {
	if(data == null){
		return;
	}
	
	var befCnt = 0;
	var i=0;
	befCnt = thirdGrid.list.length;
	
	for (i=0 ; befCnt>i ; i++) {
		if (thirdGrid.list[i].cr_befact == data.cr_befact) {
			break;
		}
	}
	
	var copyData = clone(data);
	
	if (i>=befCnt) {
		thirdGrid.addRow($.extend({}, copyData, {__index: undefined}));
	}
}

function deleteDataRow(data){
	thirdGrid.removeRow(data.dindex);
}

function getReqPgmList(data){
	secondGrid.setData([]);
	
	var tmpData = {
		AcptNo 		: data.cr_befact,
		requestType	: 'reqList_Prog'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr0200_BefJobServlet', tmpData, 'json',successGetReqPgmList);
}

function successGetReqPgmList(data){
	if(typeof data == 'string' && data.indexOf('ERROR') > -1){
		dialog.alert(data.substr(5));
		return;
	}
	secondGrid.setData(data);
}