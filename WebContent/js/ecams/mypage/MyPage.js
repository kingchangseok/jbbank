
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부

var firstGrid			= new ax5.ui.grid(); // 진행중인 단기SR
var secondGrid		= new ax5.ui.grid(); // 결재대기 SR
var thirdGrid			= new ax5.ui.grid(); // 진행중인 중장기SR
var fourthGrid			= new ax5.ui.grid(); // 미접수 SR

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
         	if (this.dindex < 0) return;
         	
          	 openWindow(1, this.item.qrycd2, this.item.acptno2,this.item.cc_srid);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
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
            {type: 1, label: "상세정보"},
            {type: 2, label: "결재정보"}
        ],
        popupFilter: function (item, param) {

         	firstGrid.clearSelect();
         	firstGrid.select(Number(param.dindex));
       	
	       	var selIn = firstGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	if(param.item.acptno2 == null || param.item.acptno2 == undefined){
        		return item.type == 1;
        	}else {
        		return item.type == 1 | item.type == 2;
        	}
        },
        onClick: function (item, param) {
    		/*swal({
                title: item.label+"팝업",
                text: "신청번호 ["+param.item.acptno2+"]["+param.item.qrycd2+"]"
            });*/
           	 openWindow(item.type, param.item.qrycd2, param.item.acptno2,param.item.cc_srid);
            firstGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
        {key: "cc_srid", 	label: "SR번호",  			width: 90, align: 'left'},
        {key: "cm_srjobname", 		label: "업무구분",  	width: '10%', align: 'center'},
        {key: "cc_title", 		label: "SR제목",  	width: '30%', align: 'left'},
        {key: "cc_wishdate", 		label: "완료예정일",  	width: 90, align: 'center'},
        {key: "cm_codename", 		label: "진행상태",  	width: '15%', align: 'center'},
        {key: "cm_codename2", 		label: "결재상태",  	width: '15%', align: 'center'}
    ]
});

thirdGrid.setConfig({
	target: $('[data-ax5grid="thirdGrid"]'),
	sortable: true, 
	multiSort: true,
	header: {
		align: "center"
	},
	body: {
		onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
         	
         	 openWindow(1, this.item.qrycd2, this.item.acptno2,this.item.cc_srid);
         },
		trStyleClass: function () {},
		onDataChanged: function(){
			this.self.repaint();
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
            {type: 1, label: "상세정보"},
            {type: 2, label: "결재정보"}
        ],
        popupFilter: function (item, param) {

        	thirdGrid.clearSelect();
        	thirdGrid.select(Number(param.dindex));
       	
	       	var selIn = thirdGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	if(param.item.acptno2 == null || param.item.acptno2 == undefined){
        		return item.type == 1;
        	}else {
        		return item.type == 1 | item.type == 2;
        	}
        },
        onClick: function (item, param) {
    		/*swal({
                title: item.label+"팝업",
                text: "신청번호 ["+param.item.acptno2+"]["+param.item.qrycd2+"]"
            });*/
           	 openWindow(item.type, param.item.qrycd2, param.item.acptno2,param.item.cc_srid);
           	thirdGrid.contextMenu.close();//또는 return true;
        }
    },
	columns: [
		{key: "cc_srid", 	label: "SR번호",  			width: 90, align: 'left'},
		{key: "cm_srjobname", 		label: "업무구분",  	width: '10%', align: 'center'},
		{key: "cc_title", 		label: "SR제목",  	width: '30%', align: 'left'},
		{key: "cc_wishdate", 		label: "완료예정일",  	width: 90, align: 'center'},
		{key: "cm_codename", 		label: "진행상태",  	width: '15%', align: 'center'},
        {key: "cm_codename2", 		label: "결재상태",  	width: '15%', align: 'center'}
		]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 
    multiSort: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
         	if (this.dindex < 0) return;
         	
        	 openWindow(1, this.item.cr_qrycd, this.item.cr_acptno,this.item.cc_srid);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
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
            {type: 1, label: "상세정보"},
            {type: 2, label: "결재정보"}
        ],
        popupFilter: function (item, param) {

        	secondGrid.clearSelect();
        	secondGrid.select(Number(param.dindex));
       	
	       	var selIn = secondGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	if(param.item.cr_acptno == null || param.item.cr_acptno == undefined){
        		return item.type == 1;
        	}else {
        		return item.type == 1 | item.type == 2;
        	}
        },
        onClick: function (item, param) {
    		/*swal({
                title: item.label+"팝업",
                text: "신청번호 ["+param.item.acptno2+"]["+param.item.qrycd2+"]"
            });*/
           	 openWindow(item.type, param.item.cr_qrycd, param.item.cr_acptno,param.item.cc_srid);
           	secondGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
        {key: "cc_srid", 	label: "SR번호",  			width: 90, align: 'left'},
        {key: "cm_srjobname", 		label: "업무구분",  	width: '10%', align: 'center'},
        {key: "cr_itsmtitle", 		label: "SR제목",  	width: '35%', align: 'left'},
        {key: "qrycd", 		label: "신청종류",  	width: '10%', align: 'center'},
        {key: "sta", 		label: "결재단계명",  	width: '15%', align: 'center'},
        {key: "RstName", 		label: "결재자",  	width: '10%', align: 'center'},
        {key: "editor", 		label: "신청자",  	width: '10%', align: 'center'}
    ]
});

fourthGrid.setConfig({
	target: $('[data-ax5grid="fourthGrid"]'),
	sortable: true, 
	multiSort: true,
	header: {
		align: "center"
	},
	body: {
		onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
         	
        	 openWindow(1, this.item.qrycd2, this.item.acptno2,this.item.cc_srid);
        },
		trStyleClass: function () {},
		onDataChanged: function(){
			this.self.repaint();
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
            {type: 1, label: "상세정보"},
            {type: 2, label: "결재정보"}
        ],
        popupFilter: function (item, param) {

        	fourthGrid.clearSelect();
        	fourthGrid.select(Number(param.dindex));
       	
	       	var selIn = fourthGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	if(param.item.acptno2 == null || param.item.acptno2 == undefined){
        		return item.type == 1;
        	}else {
        		return item.type == 1 | item.type == 2;
        	}
        },
        onClick: function (item, param) {
    		/*swal({
                title: item.label+"팝업",
                text: "신청번호 ["+param.item.acptno2+"]["+param.item.qrycd2+"]"
            });*/
           	 openWindow(item.type, param.item.qrycd2, param.item.acptno2,param.item.cc_srid);
           	fourthGrid.contextMenu.close();//또는 return true;
        }
    },
	columns: [
		{key: "cc_srid", 	label: "SR번호",  			width: 90, align: 'left'},
		{key: "cm_srjobname", 		label: "업무구분",  	width: '10%', align: 'center'},
		{key: "cc_title", 		label: "SR제목",  	width: '40%', align: 'left'},
		{key: "cm_codename", 		label: "기간구분",  	width: 70, align: 'center'},
		{key: "cc_timeline", 		label: "추진일정",  	width: '15%', align: 'center'}
		]
});
$(document).ready(function() {
	if (userId == null || userId == '') return;
	
	//사용자조회
	getMySrList();
	getMyApplyList();
	getMySrListRegister();
	
	$("#btnReq").bind("click", function(){
		getMySrList();
		getMyApplyList();
		getMySrListRegister();
	});
});

function getMySrList() {
	//사용자정보 조회
	var data = new Object();
	data = {
		userId 		: userId,
		shortYn 	: 'Y',
		requestType	: 'getMySrList'
	}
	//진행중 SR 단기
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetMySrListShort);
	
	// 진행중 SR 중장기
	data.shortYn = "N";
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetMySrListLong);
}

function successGetMySrListShort(data){
	firstGrid.setData([]);
	firstGrid.setData(data);
	$("#shortCnt").text(data.length);
}

function successGetMySrListLong(data){
	thirdGrid.setData([]);
	thirdGrid.setData(data);
	$("#lognCnt").text(data.length);
}

function getMyApplyList(){
	var data = new Object();
	data = {
		userId 		: userId,
		requestType	: 'getMyApplyReqList'
//		2020.06.17 형상관리 고도화 : 본인이 결재해야 하는 SR 목록 -> 본인이 신청한 결재 대기 SR
//		requestType	: 'getMyApplyList'
	}
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetMyApplyList);
}

function successGetMyApplyList(data){
	secondGrid.setData([]);
	secondGrid.setData(data);
	$("#approvalCnt").text(data.length);
}

function getMySrListRegister(){
	var data = new Object();
	data = {
			userId 		: userId,
			requestType	: 'getMySrListRegister'
	}
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetMySrListRegister);
}

function successGetMySrListRegister(data){
	fourthGrid.setData([]);
	fourthGrid.setData(data);
	$("#registerSRCnt").text(data.length);
}

function openWindow(type,reqCd,reqNo,srId) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
    if(reqNo != null && reqNo != undefined && reqNo != ""){
    	if (type == 1) {
    		nHeight = 740;
    	    nWidth  = 1300;
    	    if(reqCd != "32") {
    	    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
    		} else {
    	        f.srid.value	= srId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    			nHeight = 515;
    		    nWidth  = 1300;
    			cURL = "/webPage/winpop/PopSRDevPlanInfo.jsp";
    		}
    	} else if (type == 2) {
    		nHeight = 828;
    	    nWidth  = 1046;

    		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
    	}
    } else {
        f.srid.value	= srId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
		nHeight = 510;
		nWidth = 1100;
		cURL = "/webPage/winpop/PopSRInfo.jsp";
    }
	
	//console.log('+++++++++++++++++'+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

// 팝업 닫기 감지
function getRequestList(){
	/*
	getMySrList();
	getMyApplyList();
	getMySrListRegister();
	*/
}