
var acptNo 		= window.parent.pReqNo;				//신청번호
var userId 		= window.parent.pUserId;			//접속자 ID

var grdBefJob  	   = new ax5.ui.grid();
var confirmDialog2 = new ax5.ui.dialog();   //확인 창

var grdBefJobData = null; 							//선후행목록 데이타
var data          = null;							//json parameter
var updtSw        = false;

confirmDialog2.setConfig({
	Title: "확인",
    theme: "info",
    width: 500
});

grdBefJob.setConfig({
    target: $('[data-ax5grid="grdBefJob"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    multipleSelect: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
           this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = grdBefJob.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	if (!updtSw) return;
	       	
	       	var befacpt = this.item.cr_befact;
	       	
	       	mask.open();
	        confirmDialog.confirm({
		        title: '선행작업해제확인',
				msg: '선택하신 신청 건을 선행작업에서 해제하시겠습니까?',
			}, function(){
				if(this.key === 'ok') {
					delBefJob(befacpt);
				}
			});
			mask.close();
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
        {key: "befgbn",      label: "작업구분",  width: '10%'},
        {key: "linkgbn",     label: "연결",     width: '10%'},
        {key: "cr_acptno",   label: "신청번호",  width: '15%'},
        {key: "acptdate",    label: "신청일시",  width: '15%'},
        {key: "cm_sysmsg",   label: "시스템",    width: '10%',align: 'left'},
        {key: "cm_username", label: "신청자",    width: '10%'},
        {key: "cm_codename", label: "진행상태",  width: '10%'},
        {key: "cr_sayu",     label: "신청사유",  width: '20%',align: 'left'} 
    ]
});

$(document).ready(function() {
	reqData	= clone(window.parent.reqInfoData);
	
	console.log('reqData',reqData);
	if (reqData[0].prcsw == '0' && reqData[0].updtsw2 == '1') {
		$("#btnBefJob").css('display', 'inline-block');
		updtSw = true;
	} else {
		$("#btnBefJob").css('display', 'none');
		updtSw = false;
	}
	
	//X버튼 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
	//하단 닫기버튼
	$('#btnClose').bind('click', function() {
		popClose();
	});
	//선행작업연결
	$('#btnBefJob').bind('click', function() {
		window.parent.befJobListModal.close();//자기창 닫고
		window.parent.openBefJobSetModal();//연결창 팝업
	});
	
	//선후행작업연결 내용조회
	befJob_List();
});
//선후행작업연결 내용조회
function befJob_List() {
	data = new Object();
	data = {
			 AcptNo : acptNo,
		requestType : 'befJob_List'
	}
	ajaxAsync('/webPage/ecmr/Cmr0200_BefJobServlet', data, 'json', successBefJob_List);
}
//선후행작업연결 내용조회 완료
function successBefJob_List(data){
	grdBefJobData = data;
	grdBefJob.setData(grdBefJobData);
}
//선행작업해제 (그리드 더블클릭)
function delBefJob(befacptno) {
	data = new Object();
	data = {
			 AcptNo	: acptNo,
			befAcpt	: befacptno,
		requestType : 'delBefJob'
	}
	ajaxAsync('/webPage/ecmr/Cmr0200_BefJobServlet', data, 'json', successDelBefJob);
}
//선행작업해제완료
function successDelBefJob(data){
	if (data == "0") {
		confirmDialog2.alert('선행작업 연결을 해제했습니다.');
	} else {
		confirmDialog2.alert('선행작업 연결해제를 실패했습니다.');
	}
	befJob_List();
}
// 팝업 닫기
function popClose(){
	window.parent.befJobListModal.close();
}
