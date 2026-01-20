/**
 * 실행모듈정보 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 * 
 */


var parentvar = window.parent.docObj;

var UserId = parentvar.UserId;
var AcptNo = parentvar.AcptNo;
var InSpmsNo = parentvar.InSpmsNo;
var OutSpmsNo = parentvar.OutSpmsNo;
var ReqCd = parentvar.ReqCd;
var SysCd = parentvar.SysCd;
var editYN = parentvar.editYN;//로그인사용자 = 신청자
var QAYN = parentvar.QAYN;//로그인사용자 = QA

var gbn = "";
var tmpPath = "";
var _strURL = "";

var docGird		= new ax5.ui.grid();
var docGirdData = [];
var grdDoc_dp = [];

var DefineDoc = {};

var tmpFile = "";
var selDocType = "";
var fileIndex 		= 0;

docGird.setConfig({
    target: $('[data-ax5grid="resultGrid"]'),
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
        onDBLClick: function () {
        	dblClickDirGrid(this.dindex);
        },
        trStyleClass: function () {
    		if(this.item.status === 'ER'){
    			return "text-danger";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_docname", 	label: "산출물명",  		width: '30%', align: "left"},
        {key: "txtResultMSG",	label: "점검결과/미비사유",  width: '70%', align: "left"},
    ]
});

$('[data-ax5select="cboJob"]').ax5select({
    options: []
});


$(document).ready(function() {
	
	// 다운로드
	$('#txtDefineDoc').bind('dblclick', function() {
		if(DefineDoc == null){
			dialog.alert("산출물 정보가 없습니다. 관리자에게 문의해주세요.");
		} else{
			fileDown(tmpPath+ "/"+ ReqCd + "/" + UserId,DefineDoc.name);
		}
	})
	
	// 추가
	$('#btnDocAdd').bind('click', function() {
		selDocType = 'JOB';
		if (selDocType == "JOB"){
			console.log(DefineDoc, $.isEmptyObject(DefineDoc));
			
			if (!$.isEmptyObject(DefineDoc) && DefineDoc !=""){
				dialog.alert("삭제처리 부터 진행 후 첨부 가능합니다.[1]");
			} else{
				/*JOBfileUp.addEventListener(Event.SELECT, onSelectFile);
				//JOBfileUp.browse([excelTypes]); //필터없이 진행
				JOBfileUp.browse();*/
				
				if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
					fileIndex++;
					$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
				}
				$("#file"+fileIndex).click();
			}
		}
		
	});
	
	// 삭제
	$('#btnDocDel').bind('click', function() {
		
		confirmDialog.setConfig({
            title: "산출물삭제",
            theme: "info"
        });
		
		if (AcptNo != null && AcptNo !=""){//재등록시  기존파일 삭제 처리 로직
			if (!$.isEmptyObject(DefineDoc) && DefineDoc != "") {
				confirmDialog.confirm({
					
					msg: "등록되어 있는 산출물["+$('$txtDefineDoc').val()+"]를 삭제 할까요?",
					btns :{
						ok: {
		                    label:'ok'
		                },
		                cancel: {
		                    label:'cancel'
		                }
					}
				}, function(){
					if(this.key === 'ok') {
						var data = {
							etcData : DefineDoc,			
							UserId 	: UserId,			
							AcptNo 	: AcptNo,			
							requestType	: 'delDocFile'
						}
						ajaxAsync('/webPage/ecmr/Cmr0260Servlet', data, 'json',successDelDocFile);
					}else{
					}
		            mask.close();
				});
			}
		} else{
			if (!$.isEmptyObject(DefineDoc) && DefineDoc != "") {
				confirmDialog.confirm({
					msg: "등록되어 있는 산출물["+$('#txtDefineDoc').text()+"]를 삭제 할까요?",
					btns :{
						ok: {
		                    label:'ok'
		                },
		                cancel: {
		                    label:'cancel'
		                }
					}
				}, function(){
					if(this.key === 'ok') {
						DefineDoc = {};
						$('#txtDefineDoc').text('');
						$('#btnDefineDocAdd').prop('disabled', '');
						dialog.alert("업무단위산출물 삭제 처리 완료.");
					}else{
					}
		            mask.close();
				});
			}
		}
	});
	// 표준양식
	$('#btnDefineDoc').bind('click', function() {
		fileDown(tmpPath+"/DG41.xls","DG41_업무변경테스트결과서.xls");
	});
	
	// 위에꺼 저장
	$('#btnQa').bind('click', function() {
		var tmpArr = [];
		var tmpObj = {};
		
		for(var i=0 ; i<grdDoc_dp.length ; i++){
			tmpObj = {};
			tmpObj = grdDoc_dp[i];
			if(grdDoc_dp[i].cr_doctype == "JOB"){
				tmpObj.cr_qamsg = $('#txtDefineQA').val();
			}else{
				tmpObj.cr_qamsg = $('#txtEtcQA').val();
			}
			tmpArr.push(tmpObj);
			tmpObj = null;
		}
		
		var data = {
			QaMSG 	: tmpArr,			
			AcptNo 	: AcptNo,			
			requestType	: 'setQaMSG'
		}
		
		ajaxAsync('/webPage/ecmr/Cmr0260Servlet', data, 'json',successSetQaMSG);
	});
	// 아래꺼 저장
	$('#btnResultMSG').bind('click', function() {
		docGirdData = clone(docGrid.list);
		
		var data = {
			ResultMSG 	: docGirdData,			
			AcptNo 	: AcptNo,			
			requestType	: 'setResultMSG'
		}
		
		ajaxAsync('/webPage/ecmr/Cmr0260Servlet', data, 'json',successSetResultMSG);
	});
	
	// 최신정보가져오기
	$('#btnRefresh').bind('click', function() {
		cmdRefresh_click();
	});
	// 재등록완료
	$('#btnRetryEnd').bind('click', function() {
		setDocStatus_click('0');
	})
	// 재등록통보
	$('#btnRetry').bind('click', function() {
		setDocStatus_click('3');
	})
	// 점검완료
	$('#btnReq').bind('click', function() {
		setDocStatus_click('9');
	})
	// 신청
	$('#btnConf').bind('click', function() {
		cmdConf_click();
	})
	
	// 닫기
	$('#btnClose').bind('click', function() {
		window.parent.updateDocFlag = false;
		popClose();
	});
	
	$('#btnQa').prop('disabled', 'disabled');
	$('#btnResultMSG').prop('disabled', 'disabled');
	$('#btnRetry').prop('disabled', 'disabled');
	$('#btnReq').prop('disabled', 'disabled');
	$('#btnRetryEnd').prop('disabled', 'disabled');
	
	//$('#btnDocAdd').prop('disabled', 'disabled');
	//$('#btnDocDel').prop('disabled', 'disabled');
	
	console.log(parentvar);
	
	if (AcptNo != null && AcptNo !="") {
		$('#btnClose').text('닫기');
		$('#btnConf').prop('disabled', 'disabled');
		$('#lblAcpt').text($('#lblAcpt').text() + ' ' + AcptNo.substr(0,4)+'-'+AcptNo.substr(4,2)+'-'+AcptNo.substr(6));
		
		
		cmdRefresh_click();
		
		$('#btnDocAdd').css('visibility','visible');
		$('#btnDocDel').css('visibility','visible');
		
		if (editYN == "Y"){//신청자일때
			$('#btnDocAdd').prop('disabled', '');
			$('#btnDocDel').prop('disabled', '');
		}else{
			$('#btnDocAdd').css('visibility','hidden');
			$('#btnDocDel').css('visibility','hidden');
		}
		
		if (QAYN == "Y"){
			$('#btnQa').prop('disabled', '');
			$('#btnRetry').prop('disabled', '');
			$('#btnReq').prop('disabled', '');
		}
	}else{
		$('#btnClose').text('취소');
	}
	if (InSpmsNo != null && InSpmsNo !=""){
		$('#lblInPrj').text(InSpmsNo);
	}
	if (OutSpmsNo != null && OutSpmsNo !=""){
		$('#lblOutPrj').text(OutSpmsNo);
	}
	getTmpDir("22");
});

function successDelDocFile(data) {
	DefineDoc = null;
	$('#txtDefineDoc').val('');
	$('#btnDefineDocAdd').prop('disabled', '');
	dialog.alert("업무단위산출물 삭제 처리 완료.");
	
	cmdRefresh_click();
}

function successSetResultMSG(data) {
	dialog.alert("점검항목별 점검결과 저장 완료!");
}
function successSetQaMSG(data) {
	dialog.alert("QA검증의견 저장 완료!");
}


function fileChange(file){
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 

		for(var i=0; i<jqueryFiles.files.length; i++){
			var sizeCount = 0;
			var size = jqueryFiles.files[i].size/1024; // Byte, KB, MB, GB
			while(size > 1024 && sizeCount < 2){
				size = size/1024;
				sizeCount ++;
			}
			size = Math.round(size*10)/10.0 + fileSizeArr[sizeCount];
			var sizeReal = jqueryFiles.files[i].size;
			var name = jqueryFiles.files[i].name;
			if(jqueryFiles.files[i].size > 500*1024*1024){ // 50MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 500MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				continue;
			}
			/*TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				break;
			}*/
			
			DefineDoc = {};
			DefineDoc.name = name;
			DefineDoc.size = size;
			DefineDoc.sizeReal = sizeReal;
			DefineDoc.filegb = "1";
			DefineDoc.realName = name;
			DefineDoc.file = jqueryFiles.files[i];
			DefineDoc.type = "JOB";
			DefineDoc.serno = "1";
			DefineDoc.text = name;
			
			$('#txtDefineDoc').text(name);
		}
		//firstGrid.setData(firstGridData);
	} 
}


function fileUpload(){
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();
    DefineDoc.fileNM = tmpPath+ "/"+ ReqCd + "/" + UserId + "/" + tmpFile + "." + SysCd +".definedoc";
	formData.append('fullName',tmpPath+ "/"+ ReqCd + "/" + UserId + "/" + tmpFile + "." + SysCd +".definedoc");
	formData.append('fullpath',tmpPath+ "/"+ ReqCd + "/" + UserId);
	formData.append('saveName',tmpFile + "." + SysCd +".definedoc");
	formData.append('file',DefineDoc.file);
	
	console.log(tmpPath);
	
	// ajax
    $.ajax({
        url:'/webPage/fileupload/FileUpload.jsp',
        type:'POST',
        enctype: 'multipart/form-data',
        data:formData,
        async: true,
        cache:false,
        contentType:false,
        processData: false,
        xhr: function() { //XMLHttpRequest 재정의 가능
        	var xhr = $.ajaxSettings.xhr();
        	/*xhr.upload.onprogress = function(e) { //progress 이벤트 리스너 추가
	        	var percent = e.loaded * 100 / e.total;
	        	setProgress(percent);
        	};*/
        	return xhr;
        },
        success : function(data){
        	console.log(data);
        	onUploadCompleteData(data);
        },
        error : function(){
        	dialog.alert('<div>오류가 발생했습니다.</div><div>재전송 버튼을 눌러 다시 등록해주시기 바랍니다.</div>',function(){});
        	$("#percent").width(0+"%");
        	$("#percentText").text(0+"%");
    		$("#btnReq").prop("disabled", false);
        	return;
        }
    });
    	
}

function onUploadCompleteData(response){
	window.parent.updateDocFlag = true;
	window.parent.DefineDoc = DefineDoc;
	popClose();
}



// 신청
function cmdConf_click() {
	
	if ($('#txtDefineDoc').text().length == 0) {
		dialog.alert("업무단위산출물이 첨부되지 않았습니다. 첨부 후 다시 신청해 주세요.");
		return;
	}
	
	tmpFile = getDate('DATE', 0) + getTime2();
	if (DefineDoc.file != null ){
	    fileUpload();
	}
}

function setDocStatus_click(status) {
	var data = {
		UserId 	: UserId,			
		AcptNo 	: AcptNo,			
		Status 	: status,			
		requestType	: 'setDocStatus'
	}
	ajaxAsync('/webPage/ecmr/Cmr0260Servlet', data, 'json',successSetDocStatus);
}

function successSetDocStatus(data) {
	if (data == "0"){
		dialog.alert("상태 업데이트 완료.");
	}
	cmdRefresh_click();
}

function cmdRefresh_click() {
	if (AcptNo != null && AcptNo !="") {
		var tmpObj = {};
		var data = {
			etcData 	: tmpObj,			
			requestType	: 'getDocList'
		}
		ajaxAsync('/webPage/ecmr/Cmr0260Servlet', data, 'json',successGetDocList);
		
		var data = {
			AcptNo 	: AcptNo,			
			requestType	: 'getDocStatus'
		}
		ajaxAsync('/webPage/ecmr/Cmr0260Servlet', data, 'json',successGetDocStatus);
	}
}
function successGetDocStatus(data) {
	//cmdRetry  //재등록 버튼
	//cmdReq //점검완료 버튼
	//0:진행중 9:점검완료 8:삭제된상태 3:재등록요청상태
	if (QAYN == "Y"){
		var tmpList = data.split(",");
		var i=0;
		for (i=0 ; i<tmpList.length ; i++) {
			
			$('#btnRetry').prop('disabled', 'disabled');
			$('#btnReq').prop('disabled', 'disabled');
			
			if(tmpList[i] == "3"){
				break;
			} else if (tmpList[i] == "8"){
				break;
			} else if (tmpList[i] == "9"){
				break;
			} else {
				$('#btnRetry').prop('disabled', '');
				$('#btnReq').prop('disabled', '');
			}
		}
		tmpList = null;
	}
}

function successGetDocList(data) {
	grdDoc_dp = data;
	docGirdData = data;
	if (grdDoc_dp.length > 0){//cr_status,cr_doctype,cr_docname,cr_docfilenm,cr_qamsg,cr_resultmsg
		//버튼활성화 체크
		//dialog.alert(editYN + "," + QAYN + "," + grdDoc_dp[0].cr_status + "," + grdDoc_dp[1].cr_status);
		if(grdDoc_dp[0].cr_status == "9" && grdDoc_dp[1].cr_status == "9"){
			
			$('#btnRetryEnd').prop('disabled', 'disabled');
			$('#btnResultMSG').prop('disabled', 'disabled');
			$('#btnQa').prop('disabled', 'disabled');
			$('#btnRetry').prop('disabled', 'disabled');
			$('#btnReq').prop('disabled', 'disabled');
			$('#btnDocAdd').css('visibility', 'hidden');
			$('#btnDocDel').css('visibility', 'hidden');
			
		}else if(editYN == "Y" && grdDoc_dp[0].cr_status == "3" && grdDoc_dp[1].cr_status == "3"){//신청자이면서 상태가 3
			$('#btnRetryEnd').prop('disabled', '');
			$('#btnResultMSG').prop('disabled', '');
		}else if(editYN == "Y" && grdDoc_dp[0].cr_status == "0" && grdDoc_dp[1].cr_status == "0"){//신청자이면서 상태가 0
			$('#btnRetryEnd').prop('disabled', '');
			$('#btnResultMSG').prop('disabled', '');
		}else if (QAYN == "Y" && grdDoc_dp[0].cr_status == "3" && grdDoc_dp[1].cr_status == "3"){//QA이면서 상태가 3
			$('#btnQa').prop('disabled', 'disabled');
			$('#btnRetry').prop('disabled', 'disabled');
			$('#btnReq').prop('disabled', 'disabled');
			
		}else if (QAYN == "Y" && grdDoc_dp[0].cr_status == "0" && grdDoc_dp[1].cr_status == "0"){//QA이면서 상태가 0
			$('#btnQa').prop('disabled', '');
			$('#btnRetry').prop('disabled', '');
			$('#btnReq').prop('disabled', '');
		}

		for(var i=0 ; i<grdDoc_dp.length ; i++){
			if(grdDoc_dp[i].cr_doctype == "JOB"){
				$('#txtDefineDoc').val(grdDoc_dp[i].cr_docname);
				$('#txtDefineQA').val(grdDoc_dp[i].cr_qamsg);

				if(editYN == "Y") {
					$('#btnDocAdd').prop('disabled', '');
				}
				
				$('#btnDocDel').prop('disabled', 'disabled');
				
				if ($('#txtDefineDoc').text().length >0){
					$('#btnDocAdd').prop('disabled', 'disabled');
					if(editYN == "Y") {
						$('#btnDocDel').prop('disabled', '');
					}
				}

				if (grdDoc_dp[i].cr_docname != null && grdDoc_dp[i].cr_docname != ""){
					DefineDoc = {};
					DefineDoc.name = grdDoc_dp[i].cr_docname;
					DefineDoc.type = grdDoc_dp[i].cr_doctype;
					DefineDoc.fileNM = grdDoc_dp[i].cr_docfilenm;
					DefineDoc.serno = "1";
				}
				
				$('#txtDefineDoc').css('font-color', 'black');
				if(grdDoc_dp[i].cr_status == "3"){//산출물 상태값이 3 일때
					$('#txtDefineDoc').css('font-color', 'red');
				}
			}else{
				$('#txtEtcQA').val(grdDoc_dp[i].cr_qamsg);
			}
		}
	}
	docGird.setData(docGirdData);
}

function getTmpDir(gbn) {
	var data = {
		pCode 	: gbn,			
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir);
}

function successGetTmpDir(data) {
	tmpPath = data;
	getTmpDir2("F2");
	
}

function getTmpDir2(gbn) {
	var data = {
		pCode 	: gbn,			
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir2);
}

function successGetTmpDir2(data) {
	_strURL = data;
}

function successGetSubList(data) {
	lstSubprogData = data;
	makeSubProg();
}

// 등록
function updtRelat() {
	var selInPrg = prgGrid.selectedDataIndexs;
	var selInMod = modGrid.selectedDataIndexs;
	var progList = [];
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}
	
	if(selInPrg.length === 0 ){
		dialog.alert('선택된 프로그램목로기 없습ㅂ니다. 프로그램을 선택한 후 처리하십시오.', function() {});
		return;
	}
	
	if(selInMod.length === 0 ){
		dialog.alert('선택된 맵핑프로그램목록이 없습니다. 맵핑프로그램을 선택한 후 처리하십시오.', function() {});
		return;
	}
	
	selInPrg.forEach(function(selInP, index) {
		selInMod.forEach(function(selInM, index) {
			var prgItem = prgGrid.list[selInP];
			var modItem = modGrid.list[selInM];
			var tmpItem = new Object();
			tmpItem.cr_itemid 	= prgItem.cr_itemid;
			tmpItem.cr_prcitem 	= modItem.cr_itemid;
			tmpItem.check 		= 'true';
			progList.push(tmpItem);
			tmpItem = null;
			prgItem = null;
			modItem = null;
		});
	});
	
	var data = new Object();
	data = {
		tmpInfo		: {
			UserId 		: userId,
			SysCd 		: getSelectedVal('cboSysCd').value,
		},
		progList 	: progList,			
		requestType	: 'updtRelat'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successUpdtRelat);
}
// 등록 완료
function successUpdtRelat(data) {
	if(data === '0' ) {
		dialog.alert('프로그램과 맵핑프로그램의 연결등록을 완료하였습니다.', function(){});
	} else {
		dialog.alert('프로그램과 맵핑프로그램의  연결등록처리에 실패하였습니다.', function(){});
	}
	
	$('#btnQryPrg').trigger('click');
	$('#btnQryMod').trigger('click');
}

// 모듈 미연결건 필터
function modFilter() {
	var checkSw = $('#chkNoMod').is(':checked');
	fModGridData = [];
	if(checkSw) {
		modGridData.forEach(function(item, index) {
			if(item.srccnt === '0') {
				fModGridData.push(item);
			}
		});
	} else {
		fModGridData = modGridData;
	}
	
	modGrid.setData(fModGridData);
}

// 프로그램 미연결건 필터
function progFilter() {
	var checkSw = $('#chkNoPrg').is(':checked');
	fPrgGridData = [];
	
	if(checkSw) {
		prgGridData.forEach(function(item, index) {
			if(item.modcnt === '0') {
				fPrgGridData.push(item);
			}
		});
	} else {
		fPrgGridData = prgGridData;
	}
	
	prgGrid.setData(fPrgGridData);
}

// 프로그램 목록 가져오기
function getProgList() {

	prgGridData	= [];
	prgGrid.setData([]);	
	fPrgGridData 	= [];
	
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}
	
	if(getSelectedIndex('cboRsrc') < 1) {
		dialog.alert('프로그램 종류를 선택하여 주십시오.', function() {});
		return;
	}
	
	var data = new Object();
	data = {
		tmpInfo		: {			
			UserId 		: userId,
			SysCd 		: getSelectedVal('cboSysCd').value,
			ProgId 		: $('#txtPrg').val().trim()
//			subSw 		: false,
//			rsrcCd 		: getSelectedVal('cboRsrc').value
		},
		requestType	: 'getProgList'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successGetProgList);
}

// 모듈 목록 가져오기
function getModList() {
	
	modGridData	= [];
	modGrid.setData([]);

	fModGridData	= [];
	
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}
	
	if(getSelectedIndex('cboRsrc') < 1) {
		dialog.alert('프로그램 종류를 선택하여 주십시오.', function() {});
		return;
	}
	
	var data = new Object();
	data = {
		tmpInfo		: {			
			UserId 		: userId,
			SysCd 		: getSelectedVal('cboSysCd').value,
			ProgId 		: $('#txtMod').val().trim()
//			subSw 		: false,
//			rsrcCd 		: getSelectedVal('cboRsrc').samersrc
		},
		requestType	: 'getModList'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successGetModList);
}
// 모듈 목록 가져오기 완료
function successGetModList(data) {
	modGridData = data;
	modFilter();
}

// 프로그램 목록 가져오기 완료
function successGetProgList(data) {
	prgGridData =data;
	progFilter();
}

// 프로그램 종류 리스트 가져오기
function getRsrcList() {
	var data = new Object();
	data = {
		tmpInfo		: {			
			SysCd 		: getSelectedVal('cboSysCd').value,
			SelMsg 		: 'SEL'
		},
		requestType	: 'getRsrcList'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successGetRsrcList);
}
// 프로그램 종류 리스트 가져오기 완료
function successGetRsrcList(data) {
	cboRsrcData = data;
	
	$('[data-ax5select="cboRsrc"]').ax5select({
      options: injectCboDataToArr(cboRsrcData, 'cm_micode' , 'cm_codename')
	});
	
	if(cboRsrcData.length === 2) {
		$('[data-ax5select="cboRsrc"]').ax5select('setValue', cboRsrcData[1].cm_micode, true);
		$('#cboRsrc').trigger('change');
	}
}

//업무정보 콤보 가져오기
function getJobInfo() {
	
	var data = {
		UserID : UserId,
		SysCd : SysCd,
		SecuYn : "Y",
		CloseYn : "N",
		SelMsg : "",
		sortCd : "",
		requestType	: 'getJobInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetJobInfo);
}

// 업무정보
function successGetJobInfo(data) {
	cboJobData = data;
	$('[data-ax5select="cboJob"]').ax5select({
      options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'jobcdname')
	});
	$('#cboJob').trigger('change');
}


function popClose(){
	window.parent.docModal.close();
}