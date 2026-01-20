var userId 		= window.top.userId;
var adminYN 	= window.top.adminYN;	

var picker		= new ax5.ui.picker();
var requestGrid	= new ax5.ui.grid();

var option		= [];
var options1 	= [];
var cboDeptData	= [];
var cboSysData	= [];
var columnData	= [];
var requestGridData = [];

var tmpInfo;
var tmpInfoData;

$('#dateSt').val(getDate('DATE',0));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

requestGrid.setConfig({
	target : $('[data-ax5grid="requestGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {
		align: "center"
	},
	body : {
		columnHeight: 24,
		onClick : function() {
			/*this.self.clearSelect();*/
			this.self.select(this.dindex);
			selectedItem = this.item;
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
 			openWindow(1, this.item.qrycd2, this.item.acptno2,'');
         },
         trStyleClass: function () {
        	 if(this.item.colorsw === '5'){
      			return "fontStyle-error";
      		} else if (this.item.colorsw === '3'){
      			return "fontStyle-cncl";
      		} else if (this.item.colorsw === '0'){
      			return "fontStyle-ing";
      		}
      	},
 		onDataChanged : function() {
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
            {type: 1, label: "상세조회"}
        ],
        popupFilter: function (item, param) {
        	requestGrid.clearSelect();
        	requestGrid.select(Number(param.dindex));
        	
        	var selIn = requestGrid.selectedDataIndexs;
         	if(selIn.length === 0) return;
         	 
          	if (param.item == undefined) return false;
          	if (param.dindex < 0) return false;
          	
 			strConfUser = "";
          	if (param.item.endyn != "0") {
          		//return item.type == 1;
     		} else if (param.item.cr_qrycd == "04" && (adminYN || param.item.editor2 == userId)) {
          		var ajaxData = {
      				AcptNo : param.item.acptno2,
      				requestType : "cnclYn"
          		}
          		strConfUser = ajaxCallWithJson('/webPage/ecmr/Cmr3200Servlet', ajaxData, 'json');
     	    } else {
     	    	return item.type == 1;
     	    }
        },
        onClick: function (item, param) {
   			openWindow(item.type, param.item.qrycd2, param.item.acptno2,'');
   			requestGrid.contextMenu.close();
        }
    },
	columns : [
		{key : "syscd",		label : "시스템",		width: "14%",	align: "left"	}, 
		{key : "deptname",	label : "신청부서",	width: "14%",	align: "left"	}, 
		{key : "editor",	label : "신청자",		width: "8%",	align: "center"	},
		{key : "sayu",		label : "신청사유",	width: "20%",	align: "left"	},
		{key : "acptdate",	label : "신청일시",	width: "17%",	align: "center"	},
		{key : "chksta",	label : "확인상태",	width: "10%",	align: "center"	},
		{key : "pgmid",		label : "프로그램명",	width: "17%",	align: "left"	} 
	]
});


$(document).ready(function() {
	screenInit();
	search();
	getSysInfo();

	//조회
	$("#btnQry").bind('click', function() {
		search();
	})
	//엑셀저장
	$('#btnExcel').bind('click', function() {
  	    excelExport(requestGrid, "영향분석확인.xls");
	});
	//시스템 변경
	$('#cboSys').bind('change', function() {
		requestGrid.setData([]);
	});
});

function screenInit(){
	$('[data-ax5select="cboSys"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDept"]').ax5select({
		options: []
	});

	$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

function getSysInfo(){
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SecuYn : 'N',
		SelMsg : 'ALL',	
		CloseYn : 'N',
		ReqCd : '',
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	var sysInfoData = [];
	for (var i = 0; i < data.length; i++) {
		if(data[i].cm_sysinfo.substr(0,1) != '1') {
			sysInfoData.push(data[i]);
		}
	}
	$('#cboSys').ax5select({
		options: injectCboDataToArr(sysInfoData, 'cm_syscd', 'cm_sysmsg')
	});
	getCboDept();
}

//신청팀
function getCboDept(){
	tmpInfoData = new Object();
	tmpInfoData= {
		SelMsg 	 : "ALL",
		cm_useyn : "Y",
		gubun 	 : "sub",
		itYn 	 : "Y",
		requestType : "getTeamInfoGrid2"
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', tmpInfoData, 'json',successgetCboDept);
}

function successgetCboDept(data){
	$('#cboDept').ax5select({
		options : injectCboDataToArr(data, 'cm_deptcd', 'cm_deptname')
	});
}

function search(){
	var strSys = "";
	var strQry = "";
	var strDept = "";
	var strStD = "";
	var strEdD = "";
	var strChk = 0;
	
	if ($("#dateSt").val() > $("#dateEd").val()) {
		dialog.alert("조회기간을 정확하게 선택하여 주십시오."); 
		return;
	}
	
	strStD = replaceAllString($("#dateSt").val(), "/", "");
	strEdD = replaceAllString($("#dateEd").val(), "/", "");
//	if ($("#chkAll").is(":checked")) strChk = "1";
//	else strChk = "0";
	strChk = "1";
	
	if (getSelectedIndex("cboSys") > 0) strSys = getSelectedVal("cboSys").cm_syscd;
	if (getSelectedIndex("cboDept") > 0) strDept = getSelectedVal("cboDept").cm_deptcd;

	tmpInfo = {
		pSysCd	: strSys,	
		pReqCd	: strQry,	
		pTeamCd	: strDept,	
		pReqUser: $("#txtUser").val().trim(),	
		pUserId	: userId,		
		pChk	: strChk,
		pStD	: strStD,	
		pEdD	: strEdD,	
		requestType : 'get_SelectList2'
	}
	$('[data-ax5grid="requestGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loading-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3200Servlet', tmpInfo, 'json', successGetReqList);
}

function successGetUserInfo(data){
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SecuYn : data == "Ad" ? 'n' : 'y',
		SelMsg : 'ALL',	
		CloseYn : 'n',
		ReqCd : '',
		requestType	: 'getSysInfo'
	}
	$('[data-ax5grid="requestGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loading-div").show();
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successGetSysInfo);
}

function successGetReqList(data){
	$(".loading-div").remove();
	requestGridData = data;
	requestGrid.setData(requestGridData);

	if(requestGridData.length < 1) {
		dialog.alert("검색결과가 없습니다."); 
	}
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
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 1) {
		nHeight = 740;
	    nWidth  = 1300;
	    cURL = "/webPage/winpop/PopAnalysisDetail.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	console.log('openWindow reqNo='+reqNo+', cURL='+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}