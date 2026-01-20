var userName 	= window.top.userName;
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;
var strReqCD 	= window.top.reqCd;

var confirmDialog  = new ax5.ui.dialog();	// 확인,취소 창
var confirmDialog2 = new ax5.ui.dialog();   // 확인 창

var firstGrid 	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();

var strRgtCd 		= "";
var strDeptCd		= "";
var strConfUsr 		= "";
var myWin 			= null;;
var isAdmin 		= false;

var options 		= [];
var firstGridData 	= [];
var cboSinData 		= [];
var cboStatData 	= [];
var cboSysData 		= [];
var cboDeptData		= [];

var columnData = [
	{key : "syscd", 	label : "시스템", 	align : "left", 	width: "10%"},    
	{key : "deptname",	label : "신청부서", 	align : "left", 	width: "7%"},
	{key : "editor",	label : "신청자", 	align : "center", 	width: "5%"},
	{key : "qrycd", 	label : "신청구분", 	align : "left", 	width: "7%"},
	{key : "passok", 	label : "적용구분", 	align : "center", 	width: "6%"},
	{key : "sayu", 		label : "신청사유", 	align : "left",		width: "24%"},
	{key : "acptdate", 	label : "신청일시", 	align : "center", 	width: "8%"}, 
	{key : "sta", 		label : "진행상태", 	align : "left", 	width: "10%"},
	{key : "pgmid", 	label : "프로그램명", 	align : "left", 	width: "15%"},
//	{key : "prcreq", 	label : "적용예정일시",	align : "center", 	width: "8%"},
	{key : "acptno", 	label : "신청번호", 	align : "center", 	width: "8%"},
]

$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));

$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$('[data-ax5select="cboSys"]').ax5select({
	options: []
});
$('[data-ax5select="cboDept"]').ax5select({
	options: []
});
$('[data-ax5select="cboSin"]').ax5select({
	options: []
});
$('[data-ax5select="cboStat"]').ax5select({
	options: []
});

firstGrid.setConfig({
     target: $('[data-ax5grid="firstGrid"]'),
     sortable: true, 
     multiSort: false,
     header: {
         align: "center",
     },
     body: {
         onClick: function () {
         	this.self.clearSelect();
            this.self.select(this.dindex);
            
            strConfUsr = "";
            
            if(this.dindex < 0) return;
            
            if (this.item.cr_qrycd == "04" && (adminYN || this.item.cr_editor == userid)){
	            var tmpData = {
	    			treeInfoData: this.item.cr_acptno,
	    			requestType:  'cnclYn'
	    		}
	    		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr3200Servlet', tmpData, 'json');
	    		if (ajaxReturnData != null && ajaxReturnData != "") {
	    			strConfUsr = ajaxReturnData;
	    		}
            }
         },
         onDBLClick: function () {
         	if (this.dindex < 0) return;
         	openWindow(1, this.item.acptno2.substr(4,2), this.item.acptno2,'');
         },
     	trStyleClass: function () {
     		//처리 상태에 따른 컬러지정
     		if(this.item.colorsw === '5'){
     			return "fontStyle-error";
     		} else if (this.item.colorsw === '3'){
     			return "fontStyle-cncl";
     		} else if (this.item.colorsw === '0'){
     			return "fontStyle-ing";
     		}
     	},
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
             {type: 1, label: "변경신청상세"},
             {type: 2, label: "결재정보"},
             {type: 3, label: "체크인회수"},
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
          	firstGrid.clearSelect();
          	firstGrid.select(Number(param.dindex));
        	 
        	var selIn = firstGrid.selectedDataIndexs;
        	if (selIn.length === 0) return;
        	 
         	if (param.item == undefined) return false;
         	if (param.dindex < 0) return false;
         	
         	strConfUsr = "";
         	
         	if (param.item.cr_status != "0") {
         		return item.type == 1 | item.type == 2;
    		} else if (param.item.cr_qrycd == "04" && (adminYN || param.item.cr_editor == userid)) {
         		var ajaxData = {
     				AcptNo : param.item.cr_acptno,
     				requestType : "cnclYn"
         		}
         		strConfUsr = ajaxCallWithJson('/webPage/ecmr/Cmr3200Servlet', ajaxData, 'json');
    			if (strConfUsr != null && strConfUsr != "") {
					return item.type == 1 | item.type == 2 | item.type == 3;
    			} else {
             		return item.type == 1 | item.type == 2;
    			}
    	    } else if (param.item.endyn == "0" && adminYN) {
         		return item.type == 1 | item.type == 2;
    	    }
         },
         onClick: function (item, param) {
        	 if (item.type == "3") {
         		confirmDialog.confirm({
     				msg: "체크인번호 ["+param.item.acptno+"]를 체크인 회수할까요?",
     			}, function(){
     				if (this.key === 'ok') {
     					confirmDialog.prompt({
     				        title: "전체회수",
     				        msg: '전체회수 사유를 입력하시기 바랍니다.'
     				    }, function () {
     				        if (this.key === 'ok') {
     				        	if (this.input.value.trim() == '' || this.input.value.length == 0) {
     				        		confirmDialog2.alert('전체회수 사유를 입력하시기 바랍니다.');
     				        	} else {
     				        		allCncl(this.input.value, param.item.acptno);
     				        	}
     				        }
     				    });
     				}
     			});
         	 } else {
         		openWindow(item.type, param.item.qrycd2, param.item.acptno2,'');
         	 }
         	 firstGrid.contextMenu.close();//또는 return true;
          }
      },
     columns: columnData
});

$(document).ready(function(){
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#datEdD').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#datEdD").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	//콤보박스 데이터 세팅
	checkAdmin();
	getSysInfo();
	
	// 엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		firstGrid.exportExcel("RequestList.xls");
	});
	
	// 조회 버튼 클릭
	$('#btnSearch').bind('click', function() {
		getRequestList();
	});
	
	// 신청인 엔터
	$('#txtUser').bind('keypress', function(event){
		if(event.keyCode==13) {
			$('#btnSearch').trigger('click');
		}
	});
	
	// 작업지시 제목 엔터
	$('#txtIsr').bind('keypress', function(event){
		if(event.keyCode==13) {
			$('#btnSearch').trigger('click');
		}
	});
	
	//진행상태 change
	$('#cboStat').bind('change', function() {
		data_setEnable();
	});
});

//어드민 여부 확인
function checkAdmin(){
	data =  new Object();
	data = {
		Sv_UserID	: userid,
		requestType	: 'getCheckInfo'
	}
	ajaxAsync('/webPage/common/UserInfoServlet', data, 'json',successGetUserInfo);
}

//어드민 여부 확인 완료
function successGetUserInfo(data) {
	strRgtCd  = data;
	if(strRgtCd.length > 1) {
		strDeptCd = strRgtCd.substr(1);
		strRgtCd = strRgtCd.substr(0,1);
	}
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
	
	getTeamInfo();
}

function getTeamInfo() {
	var data = new Object();
	data = {
		SelMsg 	 : "ALL",
		cm_useyn : "Y",
		gubun 	 : "sub",
		itYn 	 : "Y",
		requestType : "getTeamInfoGrid2"
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json', successTeam);
}

function successTeam(data) {
	cboDeptData = data;
	
	$('[data-ax5select="cboDept"]').ax5select({
		options: injectCboDataToArr(cboDeptData, 'cm_deptcd' , 'cm_deptname')
	});
	
	if(cboDeptData.length > 0) {
		if(strRgtCd == "C" || strRgtCd == "A" || strRgtCd == "P") {
			$("#cboDept").ax5select('setValue','0',true);
		} else {
			for(var i = 0; cboDeptData.length > i; i++) {
				if(cboDeptData[i].cm_deptcd == strDeptCd) {
					$("#cboDept").ax5select('setValue',cboDeptData[i].value,true);
				}
			}
		}
		
		if(strRgtCd == "S") $("#txtUser").val(userId);
		
		/*if(getSelectedIndex('cboStat') > 0) {
			getRequestList();
		}*/
	}
	
	getCodeInfo();
}

//신청종류 가져오기
function getCodeInfo(){
	options = [];

	var codeInfos = getCodeInfoCommon([ 
		new CodeInfoOrdercd('REQUEST', 'ALL','N'),
		new CodeInfoOrdercd('R3200STA', 'ALL','N'),
	]);
	cboSinData   = codeInfos.REQUEST;
	cboStatData  = codeInfos.R3200STA;
	
	cboSinData = cboSinData.filter(function(cboSinData) {
		if(cboSinData.cm_macode == 'REQUEST' && (cboSinData.cm_micode != "31" && cboSinData.cm_micode != "34" && cboSinData.cm_micode != "39")) 
			return true;
		else 
			return false;
	});
	
	$('[data-ax5select="cboSin"]').ax5select({
		options: injectCboDataToArr(cboSinData, 'cm_micode' , 'cm_codename')
	});
	
	$('[data-ax5select="cboStat"]').ax5select({
		options: injectCboDataToArr(cboStatData, 'cm_micode' , 'cm_codename')
	});

	getRequestList();
}

function data_setEnable() {
	if (getSelectedIndex('cboStat') < 0) return;
	if (getSelectedVal('cboStat').cm_micode == "00" || getSelectedVal('cboStat').cm_micode == "3" || getSelectedVal('cboStat').cm_micode == "9") return;	

	$('#datStD').prop("disabled", false);
	$('#datEdD').prop("disabled", false);
	$('#btnStD').prop("disabled", false);
	$('#btnEdD').prop("disabled", false);
}

//신청현황 리스트 가져오기
function getRequestList(){
	columnData[9].hidden = true;
	
	var data =  new Object();
	if($("#chkDetail").is(":checked")){
		columnData[6].hidden = true;
	}else {
		columnData[6].hidden = false;
	}
	
	if ($("#datStD").val() > $("#datEdD").val()) {
		dialog.alert("조회기간을 정확하게 선택하여 주십시오."); 
		return;
	}
	
	var strSys = "";
	var strQry = "";
	var strStat = "";
	var strStD = "";
	var strEdD = "";
	var strTeam = "";
	var emgSw = "";
	
	strStD = replaceAllString($("#datStD").val(),"/","");
	strEdD = replaceAllString($("#datEdD").val(),"/","");
	
	if(getSelectedIndex('cboSys') > 0) strSys = getSelectedVal('cboSys').cm_syscd;
	if(getSelectedIndex('cboSin') > 0) strQry = getSelectedVal('cboSin').cm_micode;
	if(getSelectedIndex('cboStat') > 0) strStat = getSelectedVal('cboStat').cm_micode;
	if(getSelectedIndex('cboDept') > 0) strTeam = getSelectedVal('cboDept').cm_deptcd;
	
	if($("#chkEmg").is(":checked")) emgSw = "2";
	else emgSw = "0";
	
	data = new Object();
	data = {
		pSysCd 	 : strSys,
		pReqCd 	 : strQry,
		pTeamCd  : strTeam,
		pStateCd : strStat,
		pReqUser : $("#txtUser").val().trim(),
		pOrderId : $("#txtIsr").val().trim(),
		pStartDt : strStD,
		pEndDt 	 : strEdD,
		pUserId  : userId,
		emgSw 	 : emgSw,
		requestType : 'get_SelectList'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	console.log('[get_SelectList] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr3200Servlet', data, 'json',successGetFileList);
}

function successGetFileList(data) {
	$(".loding-div").remove();
	firstGridData = data;
	
	if(firstGridData.length < 1) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	firstGrid.setData(firstGridData);
	firstGrid.setColumnSort({acptdate:{seq:0, orderBy:'desc'}});
}

//전체회수 처리시작
function allCncl(inputMsg, pReqNo) {
	data =  new Object();
	data = {
		AcptNo		: pReqNo,
		UserId		: userid,
		conMsg		: inputMsg,
		ConfUsr		: strConfUsr,
		requestType	: 'reqCncl'
	}
	ajaxAsync('/webPage/ecmr/Cmr3200Servlet', data, 'json',successReqCncl);
}

//전체회수 처리완료
function successReqCncl(data) {
	if (data == '0') {
		confirmDialog2.alert('전체회수 처리가 완료되었습니다.');
	} else {
		confirmDialog2.alert("체크인 회수처리 중 오류가 발생하였습니다. - " + data);
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
    
    f.acptno.value	= replaceAllString(reqNo, "-", "");    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userid;    							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value 	= reqNo;    							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.adminYN.value = adminYN;
    
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

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}