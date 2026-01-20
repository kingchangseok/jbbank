var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var columnData	= 
	[ 
		{key : "plandate",label : "운영배포요청일",align : "center",width: "10%"}, 
		{key : "cm_sysmsg",label : "시스템",align : "left",width: "10%"}, 
		{key : "sta",label : "결재상태",align : "left",width: "10%"}, 
		{key : "spms",label : "SR-ID",align : "left",width: "25%"}, 
		{key : "acptno",label : "신청번호",align : "center",width: "10%"}, 
		{key : "editor",label : "신청인",align : "center",width: "5%"}, 
		{key : "qrycd",label : "신청종류",align : "left",width: "8%"}, 
		{key : "acptdate",label : "신청일시",align : "center",width: "15%"}
	];

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	sortable: true,
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            selectedItem = this.item;
        },
        onDBLClick: function(){
        	openWindow('1', 'win', this.item.cr_acptno, "");
        },
        trStyleClass: function () {
        	if (this.item.cr_congbn === "2"){
        		return "fontStyle-ing";
        	} 
        }
	},
	columns : columnData,
	contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
        	{type: 1, label: "결재요청내용확인"},
        	{type: 2, label: "결재정보"}
        ],
        popupFilter: function (item, param) {
        	mainGrid.clearSelect();
        	mainGrid.select(Number(param.dindex));
            return true;
        },
        onClick: function (item, param) {
        	mainGrid.contextMenu.close();
        	openWindow(item.type, 'win', param.item.cr_acptno, "");
        }
   }
});

$(document).ready(function() {
	getSysInfo();
	
	//조회
	$("#btnQry").bind('click', function() {
		search();
	});
	
	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("전체미결현황 " + today + ".xls");
	});
});

function search() {
	var inputData = new Object();
	inputData = {
		sysCd : $("[data-ax5select='systemSel']").ax5select("getValue")[0].value,
		userId  : userid
	}
	ajaxData = {
		data : inputData,
		requestType : "getReqList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/report/AllPendencyReport', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
	console.log(ajaxResult);
}

function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userid,
		SecuYn : "Y",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
		ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	systemSelData = data;
	
	$('[data-ax5select="systemSel"]').ax5select({
        options: injectCboDataToArr(systemSelData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function openWindow(type,reqCd,reqNo,itemId) {
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
    f.user.value 	= userid;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 1) {
		nHeight = 740;
	    nWidth  = 1270;

		cURL = "/webPage/winpop/PopRequestDetail.jsp";
	    
	} else if (type == 2) {
		nHeight = 828;
	    nWidth  = 1046;

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	}
	//console.log('+++++++++++++++++'+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}