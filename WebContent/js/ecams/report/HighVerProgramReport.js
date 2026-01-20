var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var columnData	= 
	[ 
		{key : "cm_sysmsg",label : "시스템",align : "",width: "12%"}, 
		{key : "version",label : "버전",align : "",width: "12%"}, 
		{key : "cm_username",label : "최종수정자",align : "",width: "12%"}, 
		{key : "cr_rsrcname",label : "프로그램명",align : "left",width: "25%"}, 
		{key : "cm_dirpath",label : "프로그램경로",align : "left",width: "25%"}
	];

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
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
        	{type: 1, label: "프로그램정보"}
        ],
        popupFilter: function (item, param) {
        	mainGrid.clearSelect();
        	mainGrid.select(Number(param.dindex));
            return true;
        },
        onClick: function (item, param) {
        	mainGrid.contextMenu.close();
        	openWindow(item.type, 'win', '', param.item);
        }
   }
});

$(document).ready(function() {
	getSysInfo();
	var amountList = [
		{text: "50", value: 50},
		{text: "100", value: 100},
		{text: "200", value: 200},
		{text: "500", value: 500}
	]
	$('[data-ax5select="amountSel"]').ax5select({
		options: amountList
	});
	
	//조회
	$("#btnQry").bind('click', function() {
		search();
	});
		
	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("시스템별상위버전프로그램현황 " + today + ".xls");
	});
	
});

function search() {
	var inputData = new Object();
	inputData = {
		sysCd : $("[data-ax5select='systemSel']").ax5select("getValue")[0].value,
		cnt : $("[data-ax5select='amountSel']").ax5select("getValue")[0].value,
		userId  : userid
	}
	ajaxData = {
		data : inputData,
		requestType : "getReqList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/report/HighVerProgramReport', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
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

function openWindow(type,reqCd,reqNo,item) {
	var nHeight, nWidth, nTop, nLeft, cURL, cFeatures, winName;
	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
    winName = type+'_'+reqCd;
	nHeight = 375;//screen.height - 300;
    nWidth  = 1200;//screen.width - 400;
    cURL = "/webPage/winpop/PopProgramInfo.jsp";
	var winWidth  = document.body.clientWidth;  // 현재창의 너비
	var winHeight = document.body.clientHeight; // 현재창의 높이
	var winX      = window.screenX;// 현재창의 x좌표
	var winY      = window.screenY; // 현재창의 y좌표
	nLeft = winX + (winWidth - nWidth) / 2;
	nTop = winY + (winHeight - nHeight) / 2;

	//cFeatures = "top=" + nTop + ",left=" + nLeft + ",height=" + nHeight + ",width=" + nWidth + ",help=no,menubar=no,status=yes,resizable=yes,scroll=no";
	var f = document.popPam;   		//폼 name
	f.acptno.value	= '';    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userid;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value	= item.cr_itemid;
	f.syscd.value = item.cm_syscd;
	f.rsrccd.value = item.cr_rsrccd;
	f.rsrcname.value = item.cr_rsrcname.trim();
	/*
	var jsonString 	= JSON.stringify(window.top.codeList);
	var codeListInput  = document.createElement('input');
	if(f.querySelector('[name="codeList"]') == null) {
		// 2020 03 10 새창 띄울시 ecamsBase의 전체 코드 리스트 폼에 넣어주기 (" > % 로 공백 > $ 로 변환시켜 넘겨주기) 
	    jsonString = replaceAllString(replaceAllString(jsonString,'"','%'),' ','$');
	    codeListInput.type   = 'hidden';
	    codeListInput.name  = 'codeList';
	    codeListInput.value  = jsonString;
	    f.appendChild(codeListInput);
	}
*/
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    /*
    myWin = window.open(cURL,winName,cFeatures);
    f.action		= cURL; 		//이동할 페이지
    f.target		= winName;    	//폼의 타겟 지정(위의 새창을 지정함)
    f.method		= "post"; 		//POST방식
    f.submit();
    */
}