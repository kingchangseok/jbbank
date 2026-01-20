
var acptNo 		= window.parent.strAcptNo;				//신청번호
var gubun     	= "1";
var filepath	= "";
var filename	= "";

var uploadCk  	= window.parent.uploadCk;
var firstGrid  	= new ax5.ui.grid();

var gubunName 	= window.parent.gubunName;
var fileGb 		= window.parent.fileGb;

var firstGridData = [];
var data          = null;
var fileIndex = 0;
var fileData = [];
var TotalFileSize = 0;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : true,
    showRowSelector: true,
    page : {
    	display:false
    }
    ,
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if (this.item.sendflag){
    			return "fontStyle-cncl";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        }
    },
    columns: [
        {key: "name", label: "File",  width: '70%', align: 'left'},
        {key: "size", label: "Size",  width: '30%'}
    ]
});

$(document).ready(function() {
	$("#divSayu").css("display","block");
	if (gubunName == "testFile") {
		if (window.parent.MsgSayu != undefined && window.parent.MsgSayu != "") {
			$("#txtSayu").val(window.parent.MsgSayu);
		}
		firstGridData = window.parent.testResultFiles;
	} else if (gubunName == "attachDoc") {
		if (window.parent.MsgSayu2 != undefined && window.parent.MsgSayu2 != "") {
			$("#txtSayu").val(window.parent.MsgSayu2);
		}
		firstGridData = window.parent.testPlanFiles;
	} else {
		//$("#divSayu").css("display","none");
		$("#divSayu").css("display","block");	//상세내역 visible은 되어있지만 입력내용 사용안함
		firstGridData = window.parent.upFiles;
	}
	acptNo 	= window.parent.strAcptNo;
	filepath = acptNo.substr(0,4) + "/" +  acptNo.substr(4,2);
	filename = fileGb+"_"+acptNo.substr(6);
	
	if(firstGridData != undefined && firstGridData.length > 0){
		firstGrid.setData(firstGridData);
	}
	else{
		firstGridData = [];
	}
	if(uploadCk){
		$("#limitByte").hide();
		$("#btnFileUpload").hide();
		$("#btnFileDelete").hide();
		$("#btnReq").text("재전송");
		$("#btnReq").prop("disabled", true);
		$(".progressbar").css("display","inline-block");
		fileUpload();
	}
	
	$('#btnClose').bind('click',function() {
		//window.parent.upFiles = [];
		popClose();
	});
	
	$('#btnReq').bind('click',function(){
		if(uploadCk){
			fileUpload();
		}
		else{
			if (gubunName == "testFile") {
				window.parent.MsgSayu = $("#txtSayu").val();
				window.parent.testResultFiles = firstGridData;
			} else if (gubunName == "attachDoc") {
				window.parent.MsgSayu2 = $("#txtSayu").val();
				window.parent.testPlanFiles = firstGridData;
			} else {
				window.parent.docFiles = firstGridData;
			}
			popClose();
		}
	});
	
	$('#btnFileDelete').bind('click',function(){
		deleteData();
	});

	$('#btnFileUpload').bind('click',function(){
		if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
			fileIndex++;
			$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
		}
		$("#file"+fileIndex).click();
	});
});

function popClose(){
	window.parent.fileUploadModal2.close();
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
			
			for(var j=0; j<firstGridData.length; j++){
				
				if(firstGridData[j].name == name && firstGridData[j].sizeReal == sizeReal){
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
				//tmpObj.filegb = "1";
				tmpObj.gubunName = gubunName;
				tmpObj.fileGb = fileGb;
				tmpObj.realName = name;
				tmpObj.file = jqueryFiles.files[i];
				
				firstGridData.push(tmpObj);
			}
		}
		firstGrid.setData(firstGridData);
	}
}

function deleteData(){
	var firstGridGridSeleted = firstGrid.getList("selected");
	if(firstGridGridSeleted.length < 1){
		dialog.alert("삭제할 첨부파일을 선택해 주세요");
		return;
	}
	firstGrid.removeRow("selected");
	firstGridData = clone(firstGrid.list);
	firstGrid.repaint();
	
	if (gubunName == "testFile") {
		window.parent.MsgSayu = $("#txtSayu").val();
		window.parent.testResultFiles = firstGridData;
	} else if (gubunName == "attachDoc") {
		window.parent.MsgSayu2 = $("#txtSayu").val();
		window.parent.testPlanFiles = firstGridData;
	} else {
		window.parent.docFiles = firstGridData;
	}
}

function fileUpload(){
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	var tmpData = {
		pCode: 	'22',
		requestType: 	'getTmpDir'
	}
	tmpPath = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
	if(typeof(tmpPath) == "object"){
		tmpPath = tmpPath[0].cm_path;
	}
	for(var i=0; i<firstGridData.length; i++){
		var index = i + 1;
		formData.append('fullName',tmpPath);
		formData.append('fullpath',tmpPath+"/"+filepath);
		formData.append('saveName',filename+"_"+index);
		formData.append('file',firstGridData[i].file);
		formData.append('gubunName',firstGridData[i].gubunName);
		formData.append('fileGb',firstGridData[i].fileGb);
		
		firstGridData[i].index = index;
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
        	onUploadCompleteData(firstGridData);
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

function onUploadCompleteData(firstGridDataTmp){
	var ajaxResultData = "";
	var dbFileData = firstGridDataTmp;
	for(var key in dbFileData){ // 파일 데이터가 있으면 Json HashMap 변환에 에러가 뜨므로 파일 데이터를 지워서 전달
		dbFileData[key].file = null;
		dbFileData[key].saveName = "/"+filepath+"/"+filename+"_"+dbFileData[key].index;
		dbFileData[key].seq = dbFileData[key].index;
		dbFileData[key].acptno = acptNo;
		dbFileData[key].filegb = dbFileData[key].fileGb;
		dbFileData[key].gubunName = dbFileData[key].gubunName;
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
		popClose();
		//dialog.alert('업로드 되었습니다.',function(){});
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
