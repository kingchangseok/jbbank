var userId   = window.top.userId;
var adminYN  = window.top.adminYN;
var strReqCd = window.top.reqCd;

var mainGrid = new ax5.ui.grid();
var picker 	 = new ax5.ui.picker();

var confirmDialog  = new ax5.ui.dialog();	// 확인,취소 창
var confirmDialog2 = new ax5.ui.dialog();   // 확인 창

var cboData = [];
var columns = [];

var strConfUser = "";

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	sortable : true,
	showRowSelector : false,
	showLineNumber :true,
	multipleSelect : true,
	LineNumberColumWidth : 40,
	rowSelectorColumWidth : 27,
	header : {
		align : "center"
	},
	body : {
		onClick : function() {
			/*this.self.clearSelect();*/
			this.self.select(this.dindex);
			selectedItem = this.item;
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
 			openWindow(1, this.item.cr_qrycd, this.item.cr_acptno,'');
         },
     	trStyleClass: function () {
     		if (this.item.errflag != '0'){
     			return "fontStyle-error";   
     		} else {
	     		if (this.item.cr_status === '3'){
	     			return "fontStyle-cncl";
	     		} else if (this.item.cr_status === '0'){
	     			return "fontStyle-ing";
	     		}
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
             {type: 1, label: "변경신청상세"},
             {type: 2, label: "결재정보"},
             {type: 3, label: "체크인회수"}
         ],
         popupFilter: function (item, param) {
         	/** 
         	 * return 값에 따른 context menu filter
         	 * 
         	 * return true; -> 모든 context menu 보기
         	 * return item.type == 1; --> type이 1인 context menu만 보기
         	 * return item.type == 1 | item.type == 2; --> type 이 1,2인 context menu만 보기
         	 * 
         	 * ex)
	            	if(param.item.qrycd === '01'){
	            		return item.type == 1 | item.type == 2;
	            	}
         	 */
        	mainGrid.clearSelect();
        	mainGrid.select(Number(param.dindex));

         	var selIn = mainGrid.selectedDataIndexs;
         	if(selIn.length === 0) return;
         	 
          	if (param.item == undefined) return false;
          	if (param.dindex < 0) return false;
          	
 			strConfUser = "";
 			if (param.item.cr_status != "0") {
	          	if (param.item.endyn != "0") {
	          		return item.type == 1 | item.type == 2;
	     		} else if (param.item.cr_qrycd == "04" && param.item.cr_editor == userId) {
	          		var ajaxData = {
	      				AcptNo : param.item.cr_acptno,
	      				requestType : "cnclYn"
	          		}
	          		strConfUser = ajaxCallWithJson('/webPage/ecmr/Cmr3200Servlet', ajaxData, 'json');
	     			if (strConfUser != null && strConfUser != "") {
	 					return item.type == 1 | item.type == 2 | item.type == 3;
	     			} else {
	              		return item.type == 1 | item.type == 2;
	     			}
	     	    } else {
	     	    	return item.type == 1 | item.type == 2;
	     	    }
 		    }     
         },
         onClick: function (item, param) {
        	if(item.type == 3) {
  			   reqCncl(param.item); 
  			} else {            	 
  				openWindow(item.type, param.item.cr_qrycd, param.item.cr_acptno,'');
  			}
     		mainGrid.contextMenu.close();//또는 return true;
         }
     },
	columns : [
		{key : "sysgbname",		label : "시스템",		width: "12%",	align : "left"}, 
		{key : "cm_jobname",	label : "업무",		width: "9%",	align : "left"}, 
		{key : "cm_dirpath",	label : "디렉토리",	width: "20%",	align : "left"}, 
		{key : "cr_rsrcname",	label : "프로그램",	width: "10%",	align : "left"}, 
		{key : "acptdate",  	label : "신청일시",	width: "8%", 	align : "center"}, 
		{key : "sin",			label : "신청구분",	width: "8%", 	align : "center"}, 
		{key : "passok",		label : "적용구분",	width: "8%", 	align : "center"}, 
		{key : "acptno",		label : "신청번호",	width: "8%", 	align : "center"}, 
		{key : "cm_codename",	label : "진행상태",	width: "10%", 	align : "left"}, 
		{key : "rsrcnamememo",	label : "신청내용",	width: "20%",	align : "left"},
		{key : "prcdate",		label : "완료일시",	width: "8%", 	align : "center"}, 
	]
});

$(document).ready(function(){
	screenInit();
	prcdSet();
	getCodeInfo();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){	
			picker.close();
		}
		oldVal = currentVal;
	});

	$("#btnQry").on('click', function() {
		btnQry_click();
	});

	$("#btnExcel").on('click', function() {
		var today = getDate('DATE',0);
		mainGrid.exportExcel("대여현황_" + today + ".xls");
	});
	
	$("#cboSta").bind("change", function(){
		cboSta_Change();
	});
	
	$("#chkDetail").bind("click", function(){
		chkDetailClick();
	});
	
	$('#txtPrgName').bind('keypress', function(event){
		if(event.keyCode==13) {
			$('#btnQry').trigger('click');
		}
	});
});

function screenInit() {
	$('#dateSt').val(getDate('DATE',-1));
	$('#dateEd').val(getDate('DATE',0));
	picker.bind(defaultPickerInfoLarge('basic', 'top'));

	$('[data-ax5select="cboSta"]').ax5select({
		option: []
	});
	$('[data-ax5select="cboSin"]').ax5select({
		option: []
	})
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	$("#chkProg").wCheck("disabled", true);
	$("#txtPrgName").prop("disabled", true);
	
	columns = mainGrid.config.columns;
	columns[2].hidden = true;
	columns[3].hidden = true;
	mainGrid.config.columns = columns;
	mainGrid.setConfig();
}

function getSysInfo() {
	data = new Object();
	data = {
		UserId  : userId,
		SecuYn  : "N",
		SelMsg  : "ALL",
		CloseYn : "N",
		ReqCd   : "",
		requestType : 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysData = data;
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function cboSta_Change() {
	if(getSelectedVal('cboSta').value != "1") {
		$('#datStD').prop("disabled", false);
		$('#datEdD').prop("disabled", false);
		$('#btnStD').prop("disabled", false);
		$('#btnEdD').prop("disabled", false);
	} else {
		$('#datStD').prop("disabled", true);
		$('#datEdD').prop("disabled", true);
		$('#btnStD').prop("disabled", true);
		$('#btnEdD').prop("disabled", true);
	}
}

function chkDetailClick() {
	if($("#chkDetail").is(":checked")){
		$("#chkProg").wCheck("disabled", false);
		$("#chkProg").wCheck("check", true);
		$("#txtPrgName").prop("disabled", false);
	} else {
		$('#txtPrgName').val('');
		$("#chkProg").wCheck('check', false);
		$("#chkProg").wCheck("disabled", true);
		$("#txtPrgName").prop("disabled", true);
	}
}

//요청구분
function getCodeInfo(){
	var codeInfos = getCodeInfoCommon([ new CodeInfoOrdercd('REQUEST', 'ALL','N') ]);
	var options = codeInfos.REQUEST;
	
	options = options.filter(function(options) {
		if(strReqCd == "1") {
			if(options.cm_micode == "00" || options.cm_micode == "01" || options.cm_micode == "02" || options.cm_micode == "11") return true;
			else return false;
		}else {
			if(options.cm_micode == "00" || options.cm_micode == "04" || options.cm_micode == "06" || options.cm_micode == "16" || options.cm_micode == "60") return true;
			else return false;
		}
	});
	
	$('#cboSin').ax5select({
		options : options
	});
}

//진행상태 세팅
function prcdSet() {
	$('[data-ax5select="cboSta"]').ax5select({
		options : [
			{value: 0, text: "전체"},
			{value: 1, text: "미완료"},
			{value: 9, text: "완료"}			
		]
	})
}

function btnQry_click(){
	var emgSw = "";
	var syscd = '';
	
	var strStD = replaceAllString($("#dateSt").val(),"/","");
	var strEdD = replaceAllString($("#dateEd").val(),"/","");
	
	columns = mainGrid.config.columns;
	var etcData = new Object();
	if($("#chkDetail").is(":checked")) {
		columns[2].hidden = false;
		columns[3].hidden = false;
		columns[9].hidden = true;
		
		etcData.stepcd = getSelectedVal('cboSta').value;
		etcData.reqcd = getSelectedVal('cboSin').cm_micode;
		etcData.req = strReqCd;
		etcData.UserID = userId;
		etcData.stDt = strStD;
		etcData.edDt = strEdD;
		etcData.prgname = $("#txtPrgName").val().trim();
		
		if($("#chkProg").is(":checked")) etcData.qrycd = "P";
		else etcData.qrycd = "A";
		
		if($("#chkEmg").is(":checked")) etcData.emgSw = "2";
		else etcData.emgSw = "0";
		
		requestType = 'getFileList_detail';
	} else {
		columns[2].hidden = true;
		columns[3].hidden = true;
		
		etcData.stepcd = getSelectedVal('cboSta').value;
		etcData.reqcd = getSelectedVal('cboSin').cm_micode;
		etcData.req = strReqCd;
		etcData.UserID = userId;
		etcData.stDt = strStD;
		etcData.edDt = strEdD;
		
		if($("#chkEmg").is(":checked")) etcData.emgSw = "2";
		else etcData.emgSw = "0";
		
		requestType = 'getFileList';
	}
	mainGrid.config.columns = columns;
	mainGrid.setConfig();
	$('[data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	data = {
		requestType : requestType,
		etcData: etcData
	}
	ajaxAsync('/webPage/ecmr/Cmr1100Servlet', data, 'json',successGetFileList);
}

function successGetFileList(data) {
	$(".loding-div").remove();
	mainGrid.setData(data);
	
	if(data.length < 1) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	mainGrid.setColumnSort({acptdate:{seq:0, orderBy:'desc'}});
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

	    if(reqCd == "01" || reqCd == "02" || reqCd == "11"){
	    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	    }else if(reqCd != "32") {
	    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
		} else {
			nHeight = 515;
		    nWidth  = 1300;
			cURL = "/webPage/winpop/PopSRDevPlanInfo.jsp";
		}
	} else if (type == 2) {
		nHeight = 828;
	    nWidth  = 1046;

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	console.log('openWindow reqNo='+reqNo+', cURL='+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

function reqCncl(data) {
	if (ingSw) {
		confirmDialog2.alert('현재 신청내용 처리 중입니다. 잠시 후 이용해 주세요.');
	} else {
		if (data.befsw == 'Y') {
			confirmDialog2.alert("다른 사용자가 선행작업으로 지정한 신청 건이 있습니다. \n"+
			                     "해당 신청 건 사용자에게 선행작업 해제 요청 후 \n" +
			                     "선행작업으로 지정한 신청 건이 없는 상태에서 진행하시기 바랍니다.");
			return;
		}
		mask.open();
        confirmDialog.confirm({
			title: '전체회수',
			msg: '체크인번호 [' + data.acptno +'] 를 체크인 회수할까요?',
		}, function(){
			if(this.key === 'ok') {
				confirmDialog.prompt({
			        title: "전체회수",
			        msg: '전체회수 사유를 입력하시기 바랍니다.'
			    }, function () {
			        if(this.key === 'ok') {
			        	if (this.input.value.trim() == '' || this.input.value.length == 0) {
			        		confirmDialog2.alert('전체회수 사유를 입력하시기 바랍니다.');
			        	} else {
			        		allCncl(data, this.input.value);
			        	}
			        }
			    });
			}
			mask.close();
		});
	}
}

//전체회수 처리시작
function allCncl(data, inputMsg) {
	var ajaxData = {
		AcptNo		: data.cr_acptno,
		UserId		: userId,
		conMsg		: inputMsg,
		ConfUsr		: strConfUser,
		requestType	: 'reqCncl'
	}
	ajaxAsync('/webPage/ecmr/Cmr3200Servlet', ajaxData, 'json',successReqCncl);
}

//전체회수 처리완료
function successReqCncl(data) {
	if (data == '0') {
		confirmDialog2.alert('전체회수 처리가 완료되었습니다.');
	} else {
		confirmDialog2.alert("체크인 회수처리 중 오류가 발생하였습니다. - " + data);
	}
}