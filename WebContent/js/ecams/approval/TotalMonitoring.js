var userId 		= window.parent.userId;	

var firstGrid	= new ax5.ui.grid();
var secondGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var firstGridData 	= [];
var secondGridData  = [];

var curQryCd = "";
var curAcptNo = "";

$('#dateSt').val(getDate('DATE',0));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

$(document).ready(function() {
	setGrid();
	
	var oldVal = "";
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//조회
	$("#btnSearch").bind('click', function() {
		search();
	});
	
	//엑셀저장
	$("#btnExcel1").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		firstGrid.exportExcel("미결재현황_" + today + ".xls");
	});
	$("#btnExcel2").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		secondGrid.exportExcel("오류현황_" + today + ".xls");
	});
});

function search() {
	var strDate = replaceAllString($("#dateSt").val(), '/', '');
	var endDate = replaceAllString($("#dateEd").val(), '/', '');
	
	var confData = new Object();
	var diffData = new Object();
	var errData	 = new Object();
	
	curQryCd = "";
	curAcptNo = "";

	if (strDate > endDate) {
		dialog.alert("조회기간을 정확히 입력해주십시오.");
		return;
	}
	
	confData = {
		UserId : userId,
		stDay : strDate,
		edDay : endDate,
		requestType : 'getConfList'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	firstGridData = ajaxCallWithJson('/webPage/ecmr/CAMSBaseServlet', confData, 'json');
	$(".loding-div").remove();
	firstGrid.setData(firstGridData);
	
	errData = {
		UserId : userId,
		requestType : 'getErrList'
	}
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	secondGridData = ajaxCallWithJson('/webPage/ecmr/CAMSBaseServlet', errData, 'json');
	$(".loding-div").remove();
	secondGrid.setData(secondGridData);
	
	diffData = {
		UserId : userId,
		stDay : strDate,
		edDay : endDate,
		requestType : 'getDiffList'
	}
}

function setGrid() {
	firstGrid.setConfig({
	    target: $('[data-ax5grid="firstGrid"]'),
	    sortable: true,
	    multiSort: true,
	    showLineNumber: true,
	    header: {
	        align: "center",
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "syscd",		label: "시스템",		width: '11%',  	align: "left"},
	        {key: "deptname",	label: "신청팀",		width: '8%',  	align: "left"},
	        {key: "editor",		label: "신청인",		width: '7%',  	align: "center"},
	        {key: "qrycd",		label: "신청구분",		width: '9%',  	align: "left"},
	        {key: "acptno",		label: "신청번호",		width: '11%', 	align: "center"},
	        {key: "acptdate",	label: "신청일시",		width: '10%',  	align: "center"},
	        {key: "sta",		label: "진행상태",		width: '7%',  	align: "center"},
	        {key: "pgmid",		label: "신청내용",		width: '13%', 	align: "left"},
	        {key: "sayu",		label: "신청사유",		width: '14%', 	align: "left"},
	        {key: "prcreq",		label: "적용예정일시",	width: '14%', 	align: "left"}
	    ],
		contextMenu: {
	        iconWidth: 20,
	        acceleratorWidth: 100,
	        itemClickAndClose: false,
	        icons: {
	            'arrow': '<i class="fa fa-caret-right"></i>'
	        },
	        items: [
	        	{type: 1, label: "변경신청상세"},
	        	{type: 2, label: "결재정보"}
	        ],
	        popupFilter: function (item, param) {
	        	if(param.dindex != null){
	        		firstGrid.clearSelect();
	        		firstGrid.select(Number(param.dindex));
	        		return true;
	        	}
	         },
	         onClick: function (item, param) {
	     		openWindow(item.type, param.item.cr_qrycd, param.item.cr_acptno,'');
	     		firstGrid.contextMenu.close();//또는 return true;
	         }
		}
	});

	secondGrid.setConfig({
	    target: $('[data-ax5grid="secondGrid"]'),
	    sortable: true,
	    multiSort: true,
	    showLineNumber: true,
	    header: {
	        align: "center",
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	    	{key: "syscd",		label: "시스템",		width: '11%',  	align: "left"},
	        {key: "deptname",	label: "신청팀",		width: '8%',  	align: "left"},
	        {key: "editor",		label: "신청인",		width: '7%',  	align: "center"},
	        {key: "qrycd",		label: "신청구분",		width: '9%',  	align: "left"},
	        {key: "acptno",		label: "신청번호",		width: '11%', 	align: "center"},
	        {key: "acptdate",	label: "신청일시",		width: '10%',  	align: "center"},
	        {key: "sta",		label: "진행상태",		width: '7%',  	align: "center"},
	        {key: "pgmid",		label: "신청내용",		width: '13%', 	align: "left"},
	        {key: "sayu",		label: "신청사유",		width: '14%', 	align: "left"},
	        {key: "prcreq",		label: "적용예정일시",	width: '14%', 	align: "left"}
	    ],
		contextMenu: {
	        iconWidth: 20,
	        acceleratorWidth: 100,
	        itemClickAndClose: false,
	        icons: {
	            'arrow': '<i class="fa fa-caret-right"></i>'
	        },
	        items: [
	        	{type: 1, label: "변경신청상세"},
	        	{type: 2, label: "결재정보"}
	        ],
	        popupFilter: function (item, param) {
	        	if(param.dindex != null){
	        		secondGrid.clearSelect();
	        		secondGrid.select(Number(param.dindex));
	        		return true;
	        	}
	        },
	        onClick: function (item, param) {
	        	openWindow(item.type, param.item.qrycd, param.item.acptno, '');
	        	secondGrid.contextMenu.close();
	        }
		}
	});
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

	    if(reqCd == "01" || reqCd == "02" || reqCd == "11"){
	    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	    }else {
	    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
		}
	} else if (type == 2) {
		nHeight = 828;
	    nWidth  = 1046;

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}