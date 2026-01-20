var userId 		= window.top.userId;
var strReqCD 	= window.top.reqCd;
var adminYN 	= window.top.adminYN;		// 관리자여부
var codeList    = window.top.codeList;      //전체 코드 리스트

var picker		= new ax5.ui.picker();
var requestGrid	= new ax5.ui.grid();

var option		 = [];
var options1 	 = [];
var cboDeptData	 = [];
var cboGubunData = [];

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
	header : {
		align: "center"
	},
	body : {
		onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDBLClick: function () {
	       	if (this.dindex < 0) return;
         	openWindow(1, this.item.cr_qrycd, this.item.cr_acptno,'');
        },
	},
	columns : [
		{key : "cm_deptname",	label : "해당팀",		width: "8%",	align: "left"	}, 
		{key : "cm_codename",	label : "구분",		width: "8%",	align: "left"	}, 
		{key : "cr_rsrcname",	label : "프로그램명",	width: "25%",	align: "left"	}, 
		{key : "cr_prcdate",	label : "해당일자",	width: "6%",	align: "center"	},
		{key : "cm_username",	label : "요청자",		width: "6%",	align: "center"	},
		{key : "cr_sayu",		label : "신청사유",	width: "30%",	align: "left"	}
	]
});


$(document).ready(function() {
	screenInit();
	getCodeInfo();

	//조회 클릭 시
	$("#btnQry").bind('click', function() {
		search();
	})
	// 엑셀저장 클릭 시
	$('#btnExcel').bind('click', function() {
  	    excelExport(requestGrid, "대여 목록조회.xls");
	});
	
	$('#cboGubun').bind('change', function() {
		cboGubun_Change();
	});
	
	$('#txtProg').bind('keypress', function(event){
		if(event.keyCode==13) {
			event.keyCode = 0;
			$('#btnQry').trigger('click');
		}
	});
	
	$('#txtUser').bind('keypress', function(event){
		if(event.keyCode==13) {
			event.keyCode = 0;
			$('#btnQry').trigger('click');
		}
	});
	
	geteCAMSDir();
});

function screenInit(){
	$('[data-ax5select="cboGubun"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDept"]').ax5select({
		options: []
	});
	$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	$('#btnSt').prop("disabled", false);
	$('#btnEd').prop("disabled", false);
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ new CodeInfoOrdercd('SEARCH', 'ALL','N') ]);
	cboGubunData = codeInfos.SEARCH;
	
	$('[data-ax5select="cboGubun"]').ax5select({
		options: cboGubunData
	});
	
	getTeamInfo();
}

function getTeamInfo() {
	var data = new Object();
	data = {
		SelMsg : "ALL",
		itYn   : "Y",
		requestType : 'getTeamInfo'
	}
	ajaxAsync('/webPage/ecmr/Cmr3400Servlet', data, 'json', successGetTeamInfo);
}

function successGetTeamInfo(data) {
	$('#cboDept').ax5select({
		options : injectCboDataToArr(data, 'cm_deptcd', 'cm_deptname')
	});
	
	search();
}

function cboGubun_Change() {
	var gubun = getSelectedVal('cboGubun').cm_micode;
	
	if(gubun == "01") {
		$('#txtProg').val("");
		$('#txtUser').val("");
		$('#txtProg').prop("disabled", true);
		$('#txtUser').prop("disabled", true);
		$("#cboDept").ax5select("disable");
	}else if(gubun == "02") {
		$('#txtProg').val("");
		$('#txtUser').val("");
		$('#txtProg').prop("disabled", true);
		$('#txtUser').prop("disabled", true);
		$("#cboDept").ax5select('setValue',"0",true);
		$("#cboDept").ax5select("enable");
		$('#dateSt').prop("disabled", false);
		$('#dateEd').prop("disabled", false);
		$('#btnSt').prop("disabled", false);
		$('#btnEd').prop("disabled", false);	
	}else if(gubun == "03") {
		$('#txtProg').val("");
		$('#txtUser').val("");
		$('#txtProg').prop("disabled", true);
		$('#txtUser').prop("disabled", false);
		$("#cboDept").ax5select('setValue',"0",true);
		$("#cboDept").ax5select("disable");
		$('#dateSt').prop("disabled", true);
		$('#dateEd').prop("disabled", true);
		$('#btnSt').prop("disabled", true);
		$('#btnEd').prop("disabled", true);	
	}else if(gubun == "04") {
		$('#txtProg').val("");
		$('#txtUser').val("");
		$('#txtProg').prop("disabled", false);
		$('#txtUser').prop("disabled", true);
		$("#cboDept").ax5select('setValue',"0",true);
		$("#cboDept").ax5select("disable");
		$('#dateSt').prop("disabled", true);
		$('#dateEd').prop("disabled", true);
		$('#btnSt').prop("disabled", true);
		$('#btnEd').prop("disabled", true);
	}else if(gubun == "00") {
		$('#txtProg').prop("disabled", false);
		$('#txtUser').prop("disabled", false);
		$("#cboDept").ax5select('setValue',"0",true);
		$("#cboDept").ax5select("enable");
		$('#dateSt').prop("disabled", false);
		$('#dateEd').prop("disabled", false);
		$('#btnSt').prop("disabled", false);
		$('#btnEd').prop("disabled", false);
	}
	return;
}

function search(){
	var userNum = "";
	var sDate = $("#dateSt").val();
	var eDate = $("#dateEd").val();
	var strDept = "";

	if (getSelectedIndex("cboDept") > 0) strDept = getSelectedVal("cboDept").cm_deptcd;
	if ($("#chkSelf").is(":checked")) {
		userNum = userId;
	} else if(!$("#chkSelf").is(":checked")) {
		if($("#txtUser").val().length > 0) userNum = $("#txtUser").val().trim();
		else userNum = "";
	}
	
	tmpInfo = {
		strQryCd : strReqCD,
		UserId	 : userId,
		sDate 	 : sDate,
		eDate 	 : eDate,
		UserNo 	 : userNum,
		strDept  : strDept,
		ProgName : $("#txtProg").val().trim(),
		requestType : 'getReqList'
	}
	$('[data-ax5grid="requestGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3400Servlet', tmpInfo, 'json', successGetReqList);
}

function successGetReqList(data){
	$(".loding-div").remove();
	requestGridData = data;
	requestGrid.setData(requestGridData);
	
	if(requestGridData.length < 1) {
		dialog.alert("검색결과가 없습니다.");
		return;
	}
	
	for(var i = 0; i < requestGridData.length; i++) {
		var text = "";
		
		if(requestGridData[i].qrycd == "04") text += "적용";
		else if(requestGridData[i].cr_qrycd == "11") text += "대여취소";
		else {
			text ="미신청";
			requestGridData[i].currstatus = text;
			continue;
		}
		
		if(requestGridData[i].status == "0") text += "중";
		else if(requestGridData[i].status == "8" || requestGridData[i].status == "9") text += "완료";
		
		requestGridData[i].currstatus = text;
	}
}

function geteCAMSDir() {
	var data = {
		pCode : "99",
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json', successGeteCAMSDir);
}

function successGeteCAMSDir(data) {
	var tmpArray = new Array();

	tmpArray = data;
	if (tmpArray.length > 0) {
		for(var i = 0; i < tmpArray.length; i++) {
			tmpPath = tmpArray[i].cm_path;
		}
	} else {
		dialog.alert("파일을 저장할 디렉토리정보를 추출 중 오류가 발생하였습니다. 관리자에게 연락하여 주십시오.");
		return;
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
    
    f.acptno.value	= reqNo;						    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value 	= reqNo;    							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.adminYN.value = adminYN;
    
    if (type == 1) {
		nHeight = 740;
	    nWidth  = 1300;
    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
    } else {
    	dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
    	return;
    }
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
