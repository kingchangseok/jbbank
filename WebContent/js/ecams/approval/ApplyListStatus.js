var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

$('[data-ax5select="systemSel"]').ax5select({
	options: []
});

$('#dateSt').val(getDate('MON',-1));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	header : {align: "center"},
	body : {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
         	if (this.dindex < 0) return;
 			openWindow(1, this.item.cr_acptno.substr(5,2), this.item.cr_acptno);
        },
	},
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
         	mainGrid.clearSelect();
         	mainGrid.select(Number(param.dindex));
       	 
         	var selIn = mainGrid.selectedDataIndexs;
         	if (selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	return item.type == 1 | item.type == 2;
        },
        onClick: function (item, param) {
    		openWindow(item.type, param.item.cr_acptno.substr(5,2), param.item.cr_acptno);
    		mainGrid.contextMenu.close();
        }
     },
	columns : [ 
		{key : "acptno",		label : "신청번호",	align : "center",	width: "12%"}, 
		{key : "acptdate",		label : "신청일시",	align : "center",	width: "12%"}, 
		{key : "username",		label : "신청자",		align : "center",	width: "8%"}, 
		{key : "Sysmsg",		label : "시스템",		align : "left",		width: "12%"}, 
		{key : "sta",			label : "상태",		align : "center",	width: "8%"}, 
		{key : "sayu",			label : "신청사유",	align : "left",		width: "14%"}
	]
}); 

$(document).ready(function() {
	$('input:radio[name=radioGroup]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	getSysInfo();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//조회 클릭 시
	$("#btnSearch").bind('click', function() {
		search();
	})

	//텍스트박스에서 엔터키 입력 시
	$("#reqUserId").bind('keypress', function(event) {
		if(window.event.keyCode == 13) search();
	})

	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		
		mainGrid.exportExcel("적용대상현황 " + today + ".xls");
	});
	
})

function getSysInfo() {
	var data = new Object();
	data = {
		UserId : userid,
		SecuYn : "N",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
 	$('[data-ax5select="systemSel"]').ax5select({
        options: injectCboDataToArr(data, 'cm_syscd' , 'cm_sysmsg')
   	});
}

//조회
function search() {
	if($('#radioCkOut').is(':checked')){
		ResultCk = "0";
	} else {
		ResultCk = "1";
	}
	ajaxData = {
		pSysCd : getSelectedIndex('systemSel') > 0 ? getSelectedVal('systemSel').value : '',
		pJobCd : '',
		pStateCd : "01",
		pStartDt : replaceAllString($("#dateSt").val(), '/', ''),
		pEndDt : replaceAllString($("#dateEd").val(), '/', ''),
		pReqUser : $("#reqUserId").val(),
		ResultCk : ResultCk,
		pUserId : userid,
		requestType : "get_SelectList"
	}
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	console.log('[get_SelectList] ==>', ajaxData);
	ajaxAsync('/webPage/ecmr/Cmr2400Servlet', ajaxData, 'json', successGet_SelectList);
}

function successGet_SelectList(data) {
	$(".loding-div").remove();
	
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	mainGrid.setData(data);
	if (data.length < 1 ) {
		dialog.alert("검색결과가 없습니다.");
		mainGrid.setConfig({});
		return;
	}
}

//contextMenu 화면 호출
function openWindow(type,reqCd,reqNo) {
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

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
