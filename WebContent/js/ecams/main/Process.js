var userId = window.top.userId;
var userName = window.top.userName;

var secuList = null;
var contentHeight = null;
var errCnt = null;

var stDt = getDate('DATE',-1);
var edDt = getDate('DATE',0);

var approGrid  	= new ax5.ui.grid();
var orderGrid  	= new ax5.ui.grid();
var chkOutGrid 	= new ax5.ui.grid();
var chkInGrid  	= new ax5.ui.grid();

var approData  = [];
var orderData  = [];
var chkOutData = [];
var chkInData  = [];

approGrid.setConfig({
    target: $('[data-ax5grid="approGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        //	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if(approGrid.selectedDataIndexs.length < 1 ) {
        		return;
        	}
        	var item = approGrid.list[approGrid.selectedDataIndexs];
        	openWindow(1, item.cr_qrycd, item.cr_acptno, '');
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "sayu",		label: "신청내용"		, width: "14%",		align: "left"},
		{key: "acptno",		label: "신청번호"		, width: "7%", 		align: "center"},
		{key: "acptdate",	label: "신청일시"		, width: "7%", 		align: "center"},
		{key: "deptname",	label: "요청파트"		, width: "13%",		align: "left"},
		{key: "editor",		label: "요청인"		, width: "5%", 		align: "center"},
		{key: "sta",		label: "상태"			, width: "9%", 		align: "left"},
		{key: "cm_sysmsg",	label: "시스템"		, width: "13%", 	align: "left"},
		{key: "confdate",	label: "결재일시"		, width: "9%", 		align: "left"},
		{key: "qrycd",		label: "결재사유"		, width: "9%", 		align: "left"},
    ]
});

orderGrid.setConfig({
    target: $('[data-ax5grid="orderGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        //	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if(orderGrid.selectedDataIndexs.length < 1 ) {
        		return;
        	}
        	var item = orderGrid.list[orderGrid.selectedDataIndexs];
        	openWindow(2, '', this.item.CC_ORDERID, '');
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
		{key: "CC_REQID",	label: "요청번호"	, width: "15%", 	align: "left"},
		{key: "CC_ORDERID",	label: "발행번호"	, width: "15%", 	align: "left"},
		{key: "CC_DOCNUM",	label: "발행근거"	, width: "15%", 	align: "left"},
		{key: "CC_REQSUB",	label: "제목"		, width: "25%",		align: "left"},
		{key: "orderName",	label: "발행자"	, width: "5%", 		align: "center"},
		{key: "runnerName",	label: "담당자"	, width: "5%", 		align: "center"},
		{key: "STARTDT",	label: "요청등록일", width: "10%", 	align: "center"},
		{key: "CC_ENDPLAN",	label: "처리기한"	, width: "10%", 	align: "center"},
    ]
});

chkOutGrid.setConfig({
    target: $('[data-ax5grid="chkOutGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        //	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if(chkOutGrid.selectedDataIndexs.length < 1 ) {
        		return;
        	}
        	var item = chkOutGrid.list[chkOutGrid.selectedDataIndexs];
        	openWindow(1, this.item.cr_acptno.substr(4,2), this.item.cr_acptno,'')
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "sysgbname",		label: "시스템"		, width: "10%", 	align: "left"},
    	{key: "cm_jobname",		label: "업무"			, width: "10%", 	align: "left"},
    	{key: "acptdate",		label: "신청일시"		, width: "8%", 		align: "center"},
		{key: "sin",			label: "신청구분"		, width: "6%", 		align: "center"},
		{key: "acptno",			label: "신청번호"		, width: "10%", 	align: "center"},
		{key: "cm_codename",	label: "진행상태"		, width: "9%", 		align: "left"},
		{key: "rsrcnamememo",	label: "신청내용"		, width: "15%",		align: "left"},
		{key: "cr_sayu",		label: "신청사유"		, width: "16%",		align: "left"},
		{key: "prcdate",		label: "완료일시"		, width: "8%", 		align: "center"},
		{key: "prcreq",			label: "적용예정일시"	, width: "8%", 		align: "center"},
    ]
});

chkInGrid.setConfig({
    target: $('[data-ax5grid="chkInGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        //	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if(chkInGrid.selectedDataIndexs.length < 1 ) {
        		return;
        	}
        	var item = chkInGrid.list[chkInGrid.selectedDataIndexs];
        	openWindow(1, this.item.cr_acptno.substr(4,2), this.item.cr_acptno,'')
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "sysgbname",		label: "시스템"		, width: "10%", 	align: "left"},
    	{key: "cm_jobname",		label: "업무"			, width: "10%", 	align: "left"},
    	{key: "acptdate",		label: "신청일시"		, width: "7%", 		align: "center"},
		{key: "sin",			label: "신청구분"		, width: "6%", 		align: "center"},
		{key: "acptno",			label: "신청번호"		, width: "8%", 		align: "center"},
		{key: "cr_prjno",		label: "변경ID"		, width: "8%",		align: "center"},
		{key: "cm_codename",	label: "진행상태"		, width: "9%", 		align: "left"},
		{key: "rsrcnamememo",	label: "신청내용"		, width: "13%",		align: "left"},
		{key: "cr_sayu",		label: "신청사유"		, width: "15%",		align: "left"},
		{key: "prcdate",		label: "완료일시"		, width: "7%", 		align: "center"},
		{key: "prcreq",			label: "적용예정일시"	, width: "7%", 		align: "center"},
    ]
});

$(document).ready(function() {
	$("#lbName").text(userName);
	$("#lbAprv").text('');
	
	//미결 조회
	selectAprvCnt();
	
	//결재현황
	get_SelectList();
	
	//업무지시서목록
	getMainOrderList();
	
	//대여목록
	getCkOutList();
	
	//적용목록
	getCkInList();
	
	$('#lbAprv').bind('click' , function(){
		window.top.changeScreen("결재<strong> &gt; "+"결재현황</strong>",'/webPage/approval/ApprovalStatus.jsp','');
		return;
	});
});

function selectAprvCnt() {
	var data = {
		user_id  : userId,
		requestType : "selectAprvCnt"
	}
	ajaxAsync('/webPage/dao/DaoServlet', data, 'json',successSelectAprvCnt);
}

function successSelectAprvCnt(data) {
	$('#lbAprv').text(data);
}

function get_SelectList() {
	var data = {
		pReqCd 	 : "0",
		pTeamCd  : "0",
		pStateCd : "01",
		pReqUser : "",
		pStartDt : "",
		pEndDt 	 : "",
		pUserId  : userId,
		pDeptManager : false,
		requestType : "get_SelectList"
	}
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json',successGetSelectList);
}

function successGetSelectList(data) {
	approData = data;
	approGrid.setData(approData);
}

function getMainOrderList() {
	var data = {
		UserId : userId,
		requestType : "getMainOrderList"
	}
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetMainOrderList);
}

function successGetMainOrderList(data) {
	orderData = data;
	orderGrid.setData(orderData);
}

function getCkOutList() {
	var data = new Object();
	data = {
		stepcd : "01",
		reqcd  : "01",
		req    : "3",
		UserID : userId,
		stDt   : stDt,
		edDt   : edDt,
		emgSw  : "0"
	}
	
	var etcData = new Object();
	etcData = {
		etcData : data,
		requestType : 'getFileList'
	}
	ajaxAsync('/webPage/ecmr/Cmr1100Servlet', etcData, 'json', successGetCkOutList);
}

function successGetCkOutList(data) {
	chkOutData = data;
	chkOutGrid.setData(chkOutData);
	chkOutGrid.setColumnSort({prcdate:{seq:0, orderBy:'asc'}});
}

function getCkInList() {
	var data = new Object();
	data = {
		stepcd : "1",
		reqcd  : "04",
		req    : "4",
		UserID : userId,
		stDt   : stDt,
		edDt   : edDt,
		emgSw  : "0"
	}
	
	var etcData = new Object();
	etcData = {
		etcData : data,
		requestType : 'getFileList'
	}
	ajaxAsync('/webPage/ecmr/Cmr1100Servlet', etcData, 'json', successGetCkInList);
}

function successGetCkInList(data) {
	chkInData = data;
	chkInGrid.setData(chkInData);
}

function openWindow(type,reqCd,reqNo,rsrcName) {
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
    
    f.acptno.value	= replaceAllString(reqNo, "-", "");    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
    if (type == 1) {
		nHeight = 740;
	    nWidth  = 1300;

	    if(reqCd == "01" || reqCd == "02" || reqCd == "11") {
	    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	    }else if(reqCd == "60") {
	    	cURL = "/webPage/winpop/PopRequestDetailHomePg.jsp";
		}else {
	    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
		}
	} else if (type == 2) {
		nHeight = 828;
	    nWidth  = 1046;

	    f.orderId.value = reqNo;
	    cURL = "/webPage/winpop/PopOrderListDetail.jsp";
	} else if (type == 4) {
		nHeight = 370;
	    nWidth  = 1200;
		cURL = "/webPage/winpop/PopProgramInfo.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}