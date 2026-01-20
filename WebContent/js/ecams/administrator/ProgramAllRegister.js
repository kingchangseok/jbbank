/**
 * 일괄등록 화면 기능정의
 *
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 *
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var strReqCD 	= window.top.reqCd;				// reqCd
var localUsr = window.location.protocol+'//'+window.location.hostname + ':' + window.location.port;

var batchGrid	= new ax5.ui.grid();
var mask = new ax5.ui.mask();

//------- 결재정보 모달 -------------
var approvalModal	= new ax5.ui.modal();
//결재정보
var confirmInfoData = new Object();
var confirmData = [];
var reqCd = strReqCD;

//------- 작성요령 모달 --------------
var excelLoadTipModal = new ax5.ui.modal();
var sysCd 		= '';
var sysMsg 		= '';

var chkInList       = [];
var batchGridData	= [];
var fBatchGridData	= [];
var cboSysCdData	= [];
var cboSvrCdData	= [];

var templetPath		= '';
var tmpPath			= '';
var uploadJspFile 	= '';
var errSw			= false;

var BatchMapping	= null;	// 맵핑 새창
var serviceSw = false;

batchGrid.setConfig({
    target: $('[data-ax5grid="batchGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {

       		if (this.item.errsta == "체크아웃"){//오류
       			return "fontStyle-cncl"; //빨강
       		}else if (this.item.errsta == "테스트적용완료"){//오류
       			return "fontStyle-cncl"; //빨강
       		}else if(this.item.errsw === '1'){
    			return "fontStyle-cncl";
    		} else if(this.item.errsw === '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "errmsg", 	label: "체크결과",  		width: '10%', align: "left"},
        {key: "errsta", 		label: "프로그램상태",  	width: '10%', align: "left"},
        {key: "sysmsg", 	label: "시스템명",  		width: '10%', align: "left"},
        {key: "jobcd",		label: "업무명",  		width: '8%', align: "left"},
        {key: "userid", 	label: "신규등록인",  		width: '7%'},
        {key: "rsrcname", 	label: "프로그램명",  		width: '15%', align: "left"},
        {key: "story", 		label: "프로그램설명",  	width: '15%', align: "left"},
        {key: "dirpath", 	label: "프로그램경로",  	width: '15%', align: "left"},
        {key: "jawon", 		label: "프로그램종류",  	width: '10%', align: "left"},
    ]
});

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});

$('[data-ax5select="cboSvrCd"]').ax5select({
	options: []
});

$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-batch').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {

	getSysInfo();
	getTmpDir();
	getDocPath();

	$('[data-ax5select="cboSvrCd"]').ax5select("disable");
	$('#btnReq').prop( "disabled", 	true);
	$('#btnDel').prop('disabled', true);

	if (adminYN){
		$("#hbxBase").css('display', 'inline-block');
	} else {
		$("#hbxBase").css('display', 'none');
	}
	$("#optBase1").wCheck("check", true);

	// 정산건, 오류건, 전체 라이도 버튼 클릭
	$('input:radio[name^="radio"]').bind('click', function() {
		gridDataFilter();
	});
	// 정상건만 등록 클릭
	$('#chkOk').bind('click', function() {
		var checkSw = $('#chkOk').is(':checked');

		if(fBatchGridData.length > 0 && checkSw) {
			$('#btnReq').prop( "disabled", 	false);
		} else {
			if(errSw) {
				$('#btnReq').prop( "disabled", 	true);
			}
		}
	});
	$('#cboSysCd').bind('change', function() {
		batchGrid.setData([]); // grid 초기화
		getSvrInfo();
	});
	// 엑셀파일
	$('#btnLoadExl').bind('click', function() {
		if(getSelectedIndex('cboSysCd') < 1) {
			dialog.alert('시스템 선택 후 사용해 주시기 바랍니다.', function() {});
			return;
		}
		if(getSelectedIndex('cboSvrCd') < 0) {
			dialog.alert('대상서버 선택 후 사용해 주시기 바랍니다.', function() {});
			return;
		}
		$('#excelFile').trigger('click');
	});
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
	// 일괄등록
	$('#btnReq').bind('click', function() {
		requestCheckIn();
	});
	// 시스템별 작성요령 모달팝업
	$('#btnSmm').bind('click', function() {
		excelLoadTip();
	});
	// 엑셀저장
	$('#btnSaveExl').bind('click', function() {
		batchGrid.exportExcel('일괄등록리스트.xls');
	});
	// 엑셀템플릿
	// fileUploadServlet 사용.
	$('#btnExlTmp').bind('click', function() {
		//location.href = tmpPath + '/ecamsap/webPage/fileupload/upload?fileName=excel_templet.xlsx&fullPath='+templetPath + '/excel_templet.xlsx';
		batchGrid.config.columns[0].excel = false;
		batchGrid.config.columns[1].excel = false;
		batchGrid.setData([]);
		batchGrid.exportExcel('excel_templet_'+userId+'.xlsx');
		batchGrid.config.columns[0].excel = true;
		batchGrid.config.columns[1].excel = true;
	});
	// 삭제
	$('#btnDel').bind('click', function() {
		deleteGridData();
	});
});

// 시스템별 작성요령 모달 팝업
function excelLoadTip() {
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주세요.' , function() {});
		return;
	}
	excelLoadTipModal.open({
        width: 1000,
        height: 800,
        iframe: {
            method: "get",
            url: "../modal/request/ExcelLoadTipModal.jsp",
            param: "callBack=modalCallBack"
	    },
        onStateChanged: function () {
            if (this.state === "open") {
            	sysCd  = getSelectedVal('cboSysCd').cm_syscd;
            	sysMsg = getSelectedVal('cboSysCd').cm_sysmsg;
                mask.open();
            } else if (this.state === "close") {
                mask.close();
            }
        }
	});
}

// 선택된 데이터 그리드에서만 삭제
function deleteGridData() {
	var selIn = batchGrid.selectedDataIndexs;

	if(selIn.length === 0 ) {
		dialog.alert('삭제 할 그리드를 선택해주세요.', function() {});
		return;
	}

	var delInArr = [];
	selIn.forEach(function (selIndex, index) {
		delInArr.push(batchGrid.list[selIndex].NO);
	});

	delInArr.sort(function(a,b) {
		return b-a;
	});
	delInArr.forEach(function (delIndex, index) {
		for(var i=0; i<batchGridData.length; i++) {
			if(batchGridData[i].NO === delIndex) {
				batchGridData.splice(i,1);
			}
		}

	});
	gridDataFilter();
}

// 일괄등록 신청
function requestCheckIn() {
	chkInList = [];
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주세요.' , function() {});
		return;
	}
	if(getSelectedIndex('cboSvrCd') < 0) {
		dialog.alert('대상서버를 선택하여 주세요.' , function() {});
		return;
	}
	if(fBatchGridData.length === 0 ) {
		dialog.alert('엑셀 DATA가 없습니다.' , function() {});
		return;
	}

	if(!$('#optBase1').is(':checked') && !$('#optBase2').is(':checked') && !$('#optBase3').is(':checked')) {
		dialog.alert('등록기준을 선택하여 주시기 바랍니다.' , function() {});
		return;
	}

	batchGridData.forEach(function(item, index) {
		if(item.errsw === '0') {
			chkInList.push(item);
		}
	});

	if(chkInList.length === 0) {
		dialog.alert('등록할 DATA가 없습니다.' , function() {});
		return;
	}


	$('#btnLoadExl').prop('disabled', true);
	var tmpData = {
		requestType : 'confSelect',
		SysCd : getSelectedVal('cboSysCd').value,
		ReqCd : strReqCD,
		RsrcCd : '',
		UserId : userId,
		QryCd : strReqCD
	}
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');

	if (ajaxReturnData == "C") {
		mask.open();
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			mask.close();
			if(this.key === 'ok') {
				confCall("Y");
			} else {
				confCall("N");
			}
		});
	} else if (ajaxReturnData == "Y") {
		confCall("Y");
    } else if (ajaxReturnData != "N") {
    	dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
    } else {
		confCall("N");
    }
	tmpData = null;
}

function confCall(GbnCd) {
	confirmInfoData = new Object();
	confirmInfoData.UserId = userId;
	confirmInfoData.ReqCd  = strReqCD;
	confirmInfoData.PrjNo  = "";
	confirmInfoData.SysCd  = getSelectedVal('cboSysCd').cm_syscd;
	confirmInfoData.JobCd  = [];
	confirmInfoData.RsrcCd = [];
	confirmInfoData.QryCd = [];
	confirmInfoData.Pos    = [];
	confirmInfoData.web = "N";
	confirmInfoData.EmgSw = "N";
	confirmInfoData.modCnt = 0;
	confirmInfoData.deployCd = "0";

	if (GbnCd == "Y") {

		approvalModal.open({
	        width: 820,
	        height: 500,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ApprovalModal.jsp",
	            param: "callBack=approvalModalCallBack"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	mask.close();
	            	if(confirmData.length > 0){
	            		request_Check_In();
	            	}
	            }
	        }
		});

	} else if (GbnCd == "N") {
		var etcData = {};
		etcData.deployCd = "";

		var tmpData = {
			requestType : 'Confirm_Info',
			UserId : confirmInfoData.UserId,
			SysCd : confirmInfoData.SysCd,
			ReqCd : confirmInfoData.ReqCd,
			RsrcCd : [],
			JobCd : "",
			Pos : "",
			EmgSw : confirmInfoData.EmgSw,
			PrjNo : "",
			webYn : confirmInfoData.web,
			QryCd : confirmInfoData.QryCd,
			modCnt : confirmInfoData.modCnt,
			etcData : etcData
		}

		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json');

		if (ajaxReturnData != undefined && ajaxReturnData != null) {
			if (ajaxReturnData.indexOf('ERR')>-1) {
				dialog.alert(ajaxReturnData);
			} else {
				confirmData = ajaxReturnData;
				confirmData = confirmData.filter(function(item){
					if(item.arysv[0].SvUser == undefined || item.arysv[0].SvUser == null || item.arysv[0].SvUser == ""){
						return false;
					} else {
						return true;
					}
				});
				request_Check_In();
			}
		}
		etcData = null;
	}
}
function request_Check_In() {
	var dataObj = {};
	dataObj.cm_syscd = getSelectedVal('cboSysCd').cm_syscd;
	dataObj.ReqCD = strReqCD;
	dataObj.UserID = userId;
	dataObj.sysgb = getSelectedVal('cboSysCd').cm_sysgb;
	dataObj.svrcd = getSelectedVal('cboSvrCd').cm_svrcd;
	dataObj.svrseq = getSelectedVal('cboSvrCd').cm_seqno;
//	dataObj.svrseq = ""; //시스템설정정보 읽어서 하게 서버코드만 가지고 정보입력함

	if ($('#optBase1').is(':checked')) dataObj.base = "1";
	else if ($('#optBase2').is(':checked')) dataObj.base = "2";
	else dataObj.base = "3";

	var data = new Object();
	data = {
		chkInList 		: chkInList,
		etcData 		: dataObj,
		ConfList		: confirmData,
		requestType		: 'request_Check_In'
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', data, 'json',successRequestCheckIn);
}

// 일괄등록 신청 완료
function successRequestCheckIn(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}

	if (data.substr(0,5) == "ERROR") {
		dialog.alert(tmpMsg.substr(5));
		$('#btnLoadExl').prop('disabled', false);
		$('#btnReq').prop('disabled', false);
		return;
	}
	dialog.alert('일괄체크인 신청완료');

	batchGridData = [];
	gridDataFilter();
	$('#btnLoadExl').prop('disabled', false);
	$('#btnReq').prop('disabled', false);
}

// 라디오 버튼에 따른 그리드 필터
function gridDataFilter() {
	batchGrid.clearSelect();
	fBatchGridData = [];
	var checkVal = $('input[name="radio"]:checked').val();
	//var checkVal = $(':input:radio[name=radio]:checked').val();
	if(checkVal === undefined) {
		fBatchGridData = batchGridData;
	} else {
		batchGridData.forEach(function(item, index) {
			if(checkVal === 'normal' && item.errsw === '0') {
				fBatchGridData.push(item);
			} else if (checkVal === 'err' && item.errsw === '1') {
				fBatchGridData.push(item);
			}else if(checkVal === 'all'){
				fBatchGridData.push(item);
			}
		});
	}
	batchGrid.setData(fBatchGridData);
}

// 엑셀 템플릿이 있는 경로 가져오기
function getDocPath() {
	var data = new Object();
	data = {
		pCode 		: '02',	// 나중에 서버 경로로 바꿔주기
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetDocPath);
}
// 엑셀 템플릿이 있는 경로 가져오기 완료
function successGetDocPath(data) {
	 templetPath = data;
	if(typeof(templetPath) == "object"){
		templetPath = data[0].cm_path;
	}
}

// 엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir);
}
// 엑셀 파일 업로드시 파일 올릴 경로 가져오기 완료 AND 업로드할때 사용할 JSP 파일명 가져오기
function successGetTmpDir(data) {
	tmpPath = data;
	if(typeof(tmpPath) == "object"){
		tmpPath = data[0].cm_path;
	}

	var data = new Object();
	data = {
		pCode 		: 'F2',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetUploadJspName);
}
// 업로드할때 사용할 JSP 파일명 가져오기 완료
function successGetUploadJspName(data) {
	uploadJspFile = data;
	if(typeof(uploadJspFile) == "object"){
		uploadJspFile = data[0].cm_path;
	}
}

// 엑셀 파일 업로드시 파일타입 체크
function fileTypeCheck(obj) {
	var pathpoint = obj.value.lastIndexOf('.');
	var filepoint = obj.value.substring(pathpoint+1,obj.length);
	filetype = filepoint.toLowerCase();
	if(filetype=='xls' || filetype=='xlsx') {
		startUpload();
	} else {
		dialog.alert('엑셀 파일만 업로드 가능합니다.', function() {});
		parentObj  = obj.parentNode
		node = parentObj.replaceChild(obj.cloneNode(true),obj);
		return false;
	}
}
// 엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;

	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	// 테스트 임시경로
	//tmpPath = 'C:\\fileupload\\tmp\\';
	formData.append('fullName',tmpPath+userId+"_excel_eCmm1600.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);

    $.ajax({
		url: localUsr+'/webPage/fileupload/'+uploadJspFile,
        type:'POST',
        data:formData,
        async:false,
        cache:false,
        contentType:false,
        processData:false
    }).done(function(response){
    	onUploadCompleteData(response);
    }).fail(function(xhr,status,errorThrown){
    	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
    }).always(function(){
    	// file 초기화
    	var agent = navigator.userAgent.toLowerCase();
    	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
    	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
    	} else {
    	    $("#excelFile").val("");
    	}
    });
}
// 엑셀파일 완료 후
function onUploadCompleteData(filePath){
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");

	headerDef.push("sysmsg");
	headerDef.push("jobcd");
	headerDef.push("userid");
	headerDef.push("rsrcname");
	headerDef.push("story");
	headerDef.push("dirpath");
	headerDef.push("jawon");
//	headerDef.push("lang");


	tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

// 읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}

	var findSw = false;
	batchGridData = data;
	batchGridData.splice(0,1);
	batchGrid.setData(batchGridData);

	/*for(var i=0; i<batchGridData.length; i++) {
		if(batchGridData[i].sysmsg !== getSelectedVal('cboSysCd').cm_sysmsg) {
			dialog.alert('시스템명이 선택한 시스템명과 일치하지 않습니다.', function() {});
			findSw = true;
			break;
		}
	}

	if(findSw) {
		return;
	}*/

	if (batchGridData.length == 0){
		$('#btnReq').prop('disabled', true);
		return;
	}
	if (batchGridData[0].sysmsg != getSelectedVal('cboSysCd').cm_sysmsg) {
		dialog.alert("시스템명이 선택한 시스템명과 일치하지 않습니다.");
		return;
	}
	if (!$('#optBase1').is(':checked') && !$('#optBase2').is(':checked') && !$('#optBase3').is(':checked')) {
		dialog.alert("등록 기준을 선택하여 주시기 바랍니다.");
		return;
	}

	batchGrid.setData(batchGridData);
	getFileListExcel();
}

// 세팅한 정보 유효성 검사
function getFileListExcel() {
	var dataObj = new Object();
	dataObj.cm_syscd = getSelectedVal('cboSysCd').cm_syscd;
	dataObj.cm_sysmsg = getSelectedVal('cboSysCd').cm_sysmsg;
	/*
	dataObj.sysgb 	= getSelectedVal('cboSysCd').cm_sysgb;
	dataObj.svrcd 	= getSelectedVal('cboSvrCd').cm_svrcd;
	dataObj.svrseq 	= getSelectedVal('cboSvrCd').cm_seqno;
	*/

	if ($('#optBase1').is(':checked')) dataObj.base = "1";
	else if ($('#optBase2').is(':checked')) dataObj.base = "2";
	else dataObj.base = "3";

	var tmpData = {
		fileList 	: batchGridData,
		dataObj		: dataObj,
		requestType	: 'getFileList_excel'
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', tmpData, 'json',successGetFileListExcel);
}

// 세팅한 정보 유효성 검사 완료
function successGetFileListExcel(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	batchGridData = data;

	if (null != batchGridData) {
		errSw = false;
		//$('#chkOk').wCheck('disabled', true);
		for(var i=0; i<batchGridData.length; i++) {
			if(batchGridData[i].errsw === '1') {
				dialog.alert('입력체크 중 오류가 발생한 항목이 있습니다. 확인하여 조치 후 다시 처리하시기 바랍니다.',  function() {});
				//$('#chkOk').wCheck('disabled', false);
				errSw = true;
				break;
			}
		}
	} else {
		dialog.alert('입력체크 중 오류가 발생한 항목이 있습니다. 확인하여 조치 후 다시 처리하시기 바랍니다.',  function() {});
		errSw = true;
	}
	if(batchGridData.length > 0 ) {
		$('#btnDel').prop('disabled', false);
	}
	$('#chkOk').wCheck("check", false);
	$('#btnReq').prop('disabled', errSw);
	gridDataFilter();
}

// 서버정보 가져오기
function getSvrInfo() {
	$('[data-ax5select="cboSvrCd"]').ax5select({
		options: []
	});
	$('#btnReq').prop('disabled', true);

	if (getSelectedIndex('cboSysCd') < 0) return;

	$('#btnLoadExl').prop('disabled', false);

	var data = new Object();
	data = {
		SysCd 		: getSelectedVal('cboSysCd').value,
		SelMsg 		: '',
		requestType	: 'getRsrcOpen'
	}
//	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json',successGetRsrcOpen);

	data = {
		UserID 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').value,
		SecuYn 		: 'N',
		SelMsg 		: 'SEL',
		requestType	: 'getsvrInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSvrInfo);
}
function successGetRsrcOpen(data) {
	if (data == null) return;
	batchGrid.repaint();
}
// 서버정보 가져오기 완료
function successGetSvrInfo(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	cboSvrCdData = data;

	if(cboSvrCdData.length > 0 ) {
		$('[data-ax5select="cboSvrCd"]').ax5select("enable");
		cboSvrCdData = cboSvrCdData.filter(function(data) {
			//if(data.cm_svrcd == "01" || data.cm_svrcd == "13" || data.cm_svrcd == "03") return true;
			if(data.cm_svrcd == "01") return true;
			else return false;
		});

		var options = [];
		var cboOptionObj = null;
		//data.forEach(function(item, index) {
		cboSvrCdData.forEach(function(item, index) {
			cboOptionObj = new Object();
			cboOptionObj = item;
			cboOptionObj.value =  index == 0?"00":item["cm_svrcd"] + item["cm_seqno"];
			cboOptionObj.text = item["cm_svrname"];
			options.push(cboOptionObj);
			cboOptionObj = null;
		});

		$('[data-ax5select="cboSvrCd"]').ax5select({
			options: options
		});
		$('[data-ax5select="cboSvrCd"]').ax5select('setValue', cboSvrCdData[0].cm_svrcd+cboSvrCdData[0].cm_seqno, true);
	}
}

//시스템 콤보 가져오기
function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userId,
		SecuYn : "y",
		SelMsg : "SEL",
		CloseYn : "N",
		ReqCd : "04"
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

// 시스템 콤보 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;

	$('[data-ax5select="cboSysCd"]').ax5select({
      options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});

	if(cboSysCdData.length === 0 ) {
		$('#btnLoadExl').prop( "disabled", 	true);
	}
}
