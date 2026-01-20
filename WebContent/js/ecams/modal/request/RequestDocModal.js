var acptNo 		= window.parent.pReqNo;				//신청번호
var userId 		= window.parent.pUserId;			//접속자 ID

var grdReqDoc  	= new ax5.ui.grid();

var grdReqDocData = [];
var data          = null;
var fileHomePath  = null;

var fileIndex	  = 0;
var fileIndex 	  = 0;
var fileData      = [];
var TotalFileSize = 0;
var filepath	  = "";
var filename	  = "";
var fileGb		  = '1';

grdReqDoc.setConfig({
    target: $('[data-ax5grid="grdReqDoc"]'),
    showLineNumber: true,
    multipleSelect: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	if (this.colIndex == 1) {
        		fileDown(fileHomePath+this.item.savename, this.item.orgname);
        	}
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = grdReqDoc.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	
	       	fileDown(fileHomePath+this.item.savename, this.item.orgname);
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "orgname", 	label: "파일명",  width: '85%', align: "left"},
        {key: 'b', 			label: '파일다운', align: 'center', 
        	formatter: function() {
        		return '<img width="16px" height="16px" src="/img/download.png" alt="file download" style="cursor:pointer"/>';
        	}
        }
    ]
});

$(document).ready(function() {
	filepath = acptNo.substr(0,4) + "/" +  acptNo.substr(4,2);
	filename = fileGb+"_"+acptNo.substr(6);
	
	//문서 홈 경로 가져오기
	data = new Object();
	data = {
		pCode 		: '22',
		requestType : 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json', successGetTmpDir);
	
	//X버튼 닫기
	$('#btnExit').bind('click', function() {
		popClose();
	});
	//하단 닫기버튼
	$('#btnClose').bind('click', function() {
		popClose();
	});
	//파일추가
	$('#btnAdd').bind('click', function() {
		if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
			fileIndex++;
			$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
		}
		$("#file"+fileIndex).click();
	});
	//파일제거
	$('#btnDel').bind('click', function() {
		deleteData();
	});
});

//문서 홈 경로 가져오기 완료
function successGetTmpDir(data){
	fileHomePath = data;
	if(typeof(fileHomePath) == "object"){
		fileHomePath = data[0].cm_path;
	}
	
	getFileList();
}

function getFileList() {
	//첨부문서 리스트 가져오기
	data = new Object();
	data = {
		AcptNo 		: acptNo,
		GbnCd		: fileGb,
		requestType : 'getFileList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json', successGetFileList);
}

//첨부문서 리스트 가져오기 완료
function successGetFileList(data){
	grdReqDocData = data;
	grdReqDoc.setData(grdReqDocData);
}
//모달닫기
function popClose() {
	window.parent.requestDocModal.close();
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
			var name = jqueryFiles.files[i].name
			if(jqueryFiles.files[i].size > 500*1024*1024){ // 50MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 500MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				break;
			}
			var fileCk = true;
			
			for(var j=0; j<grdReqDocData.length; j++){
				
				if(grdReqDocData[j].name == name && grdReqDocData[j].sizeReal == sizeReal){
					$("#"+file).remove();
					fileCk = false;
					break;
				}
				fileCk = true;
			}

			if(fileCk){
				var tmpObj = new Object(); // 그리드에 추가할 파일 속성
				tmpObj.name = name;
				tmpObj.size = size;
				tmpObj.sizeReal = sizeReal;
				tmpObj.fileGb = fileGb;
				tmpObj.realName = name;
				tmpObj.file = jqueryFiles.files[i];
				tmpObj.newyn = 'Y';
				
				grdReqDocData.push(tmpObj);
			}
		}
		//grdReqDoc.setData(grdReqDocData);
		fileUpload();
	}
}

function fileUpload() {
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	var tmpData = {
		pCode: '22',
		requestType: 'getTmpDir'
	}
	tmpPath = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
	if(typeof(tmpPath) == "object"){
		tmpPath = tmpPath[0].cm_path;
	}
	for(var i=0; i<grdReqDocData.length; i++){
		var index = i + 1;
		if(grdReqDocData[i].newyn == 'Y') {
			formData.append('fullName',tmpPath);
			formData.append('fullpath',tmpPath+"/"+filepath);
			formData.append('saveName',filename+"_"+index);
			formData.append('file',grdReqDocData[i].file);
			formData.append('filegb',grdReqDocData[i].fileGb);
		}
		
		grdReqDocData[i].index = index;
	}
	// ajax
    $.ajax({
        url:'/webPage/fileupload/FileUpload.jsp',
        type:'POST',
        enctype: 'multipart/form-data',
        data:formData,
        async: true,
        cache:false,
        contentType:false,
        processData: false
        ,xhr: function() {
        	var xhr = $.ajaxSettings.xhr();
        	xhr.upload.onprogress = function(e) { //progress 이벤트 리스너 추가
	        	var percent = e.loaded * 100 / e.total;
	        	setProgress(percent);
        	};
        	return xhr;
        },
        success : function(data){
        	onUploadCompleteData(grdReqDocData);
        },
        error : function(req, stat, error){
        	var jsonValue 		= $.parseJSON(req.responseText);	// response
			var errorMessage 	= jsonValue.message;				// response error mesage
			var agent 			= navigator.userAgent.toLowerCase();// ie 구분
			
			var jsonArr = req.responseJSON.message.split("\r\n\tat");
			if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
				// java 에서 강제적으로 exception 발생시
				alert("오류발생 : " + replaceAllString(req.responseJSON.message.split("\r\n\tat")[0],"java.lang.Exception: ",""));
			}
			
        	dialog.alert('<div>오류가 발생했습니다.</div><div>재전송 버튼을 눌러 다시 등록해주시기 바랍니다.</div>',function(){});
        	$("#percent").width(0+"%");
        	$("#percentText").text(0+"%");
    		$("#btnReq").prop("disabled", false);
        	return;
        }
    });
}

function onUploadCompleteData(grdReqDocDataTmp){
	var ajaxResultData = "";
	var dbFileData = grdReqDocDataTmp;
	dbFileData = dbFileData.filter(function(item) {
		if(item.newyn == 'Y') return true;
		else return false;
	})
	for(var key in dbFileData) {
		dbFileData[key].file = null;  // 파일 데이터가 있으면 Json HashMap 변환에 에러가 뜨므로 파일 데이터를 지워서 전달
		dbFileData[key].saveName = "/"+filepath+"/"+filename+"_"+dbFileData[key].index;
		dbFileData[key].seq = dbFileData[key].index;
		dbFileData[key].acptno = acptNo;
		dbFileData[key].filegb = dbFileData[key].fileGb;
	}
	
	/*var sayuObj = {};
	sayuObj.TestPlanSayu = window.parent.MsgSayu;
	sayuObj.TestResultSayu = window.parent.MsgSayu2;*/
	
	var tmpData = {
		fileList : dbFileData,
		requestType	: 'setDocFile'
	}
	console.log('[setDocFile] ==>',tmpData)
	ajaxResultData = ajaxCallWithJson('/webPage/common/DocFileServlet', tmpData, 'json');
	if(ajaxResultData == "ok"){
		//popClose();
		dialog.alert('업로드 되었습니다.',function(){});
		getFileList();
	}
	else{
    	dialog.alert('<div>오류가 발생했습니다.</div><div> 재전송 버튼을 눌러 다시 등록해주시기 바랍니다.</div>',function(){});
    	$("#percent").width(0+"%");
    	$("#percentText").text(0+"%");
		$("#btnReq").prop("disabled", false);
    	return;
	}
}

function setProgress(percent){
	$("#percent").width(percent+"%");
	$("#percentText").text(Math.round(percent)+"%");
}

function deleteData(){
	var grdReqDocGridSeleted = grdReqDoc.getList("selected");
	if(grdReqDocGridSeleted.length < 1){
		dialog.alert("삭제할 첨부파일을 선택해 주세요");
		return;
	}
	
	confirmDialog.confirm({
		msg: '선택한 파일을 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var data = new Object();
			data = {
				fileData 	: grdReqDocGridSeleted[0],
				requestType : 'removeDocFile'
			}
			ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json', successRemoveDocFile);
		}
	});
}

function successRemoveDocFile(data) {
	dialog.alert('파일을 삭제하였습니다.',function(){});
	getFileList();
}