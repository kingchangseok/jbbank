var firstGrid  	= new ax5.ui.grid();

var fileIndex = 0;
var fileData = [];
var acptNo = null;
var fileGb = "1";

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    showRowSelector: false,
    paging : false,
    page : {
    	display:false
    }
    ,
    showLineNumber: true,
    header: {
        align: "center",
        columnHeight: 30
    },
    body: {
        columnHeight: 28,
        onClick: function () {
        	//this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if(!this.item.newFile){
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
        {key: "name", label: "File",  width: '70%'},
        {key: "size", label: "Size",  width: '30%'}
    ]
});

$(document).ready(function() {
	
	if(window.parent.docList.length > 0) {
		firstGrid.setData(window.parent.docList);
	}
	
	if(window.parent.reqSw) {
		$(".progressbar").show();
		$("#btnAdd").hide();
		$("#btnDel").hide();
		$("#btnComplete").text("닫기");
		acptNo = window.parent.acptNo;
		fileData = window.parent.docList;
		fileUpload();
		
	}
	
	$("#btnAdd").bind('click', function() {
		fileOpen();
	});
	
	$("#btnDel").bind('click', function() {
		
	});
	
	$("#btnComplete").bind('click', function() {
		popClose();
	});
	
	
});

//파일첨부
function fileOpen() {
	if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
		fileIndex++;
		$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
	}
	$("#file"+fileIndex).click();
}

function fileChange(file){
	fileData = firstGrid.getList();
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	var spcChar = "{}<>?|~`!@#$%^&*+\"'\\/";
	var TotalFileSize = 0;
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 
		var fileCk = true;
		
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
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				fileCk = false;
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				fileCk = false;
				break;
			}
			
			for(var j=0; j<fileData.length; j++){

				for (k=0;spcChar.length>k;k++) {
					if (name.indexOf(spcChar.substr(k,1))>=0) {
						dialog.alert("첨부파일명에 특수문자를 입력하실 수 없습니다. 특수문자를 제외하고 첨부하여 주시기 바랍니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
				}
				if(!fileCk){
					break;
				}
				
				if(fileData[j].name == name){
					dialog.alert("파일명 : " + name +"\n 은 이미 등록된 파일입니다.\n확인 후 다시 등록해 주세요");
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
				tmpObj.newFile = true;
				tmpObj.realName = name;
				tmpObj.file = jqueryFiles.files[i];
				
				fileData.push(tmpObj);
			}
			else{
				break;
			}
		}
		firstGrid.setData(fileData);
		window.parent.docList = fileData;
	} 
}


function fileUpload() {
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	var tmpData = {
		pcode: 	'22',
		requestType: 	'getSystemPath'
	}
	tmpPath = ajaxCallWithJson('/webPage/common/CommonSystemPath', tmpData, 'json');

//	tmpPath = 'C:/ecamstmp/'; // 테스트 임시경로
	if(typeof(tmpPath) == "object"){
		tmpPath = tmpPath[0].cm_path;
	}
	
	filepath = acptNo.substr(0,4) + "/" + acptNo.substr(4,2);
	
	for(var i = 0; i < fileData.length; i++){
		var saveFileName = fileGb + acptNo.substr(6,6);
		for (var j = 0; j < 4-((i+1).toString().length); j++){
			saveFileName = saveFileName + "0";		
		}
		saveFileName = saveFileName + (i+1).toString();
		
		var fullName = tmpPath+ "/" + filepath + "/" + saveFileName;
		
		tmpObj = fileData[i];
		tmpObj.fullName = fullName;
		tmpObj.saveFileName = saveFileName;
		tmpObj.pathcode = "22";
		tmpObj.filepath = filepath;
		tmpObj.fullpath = tmpPath+ "/" + filepath;
		
		fileData[i] = tmpObj;
	}
	
	for(var i=0; i<fileData.length; i++){
		if(fileData[i].newFile == true){
			var index = i + 1;
			formData.append('fullName',tmpPath);
			formData.append('fullpath',tmpPath+"/"+filepath);
			formData.append('saveName',fileData[i].saveFileName);
			formData.append('file',fileData[i].file);
			console.log(formData);
			fileData[i].index = index;
		}
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
        	onUploadCompleteData(fileData);
        },
        error : function(req, stat, error){
        	var jsonValue 		= $.parseJSON(req.responseText);	// response
			var errorMessage 	= jsonValue.message;				// response error mesage
			var agent 			= navigator.userAgent.toLowerCase();// ie 구분
			
			var jsonArr = jsonValue.message.split("\n");
			if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
				// java 에서 강제적으로 exception 발생시
				alert("오류발생 : " + replaceAllString(jsonArr[0],"java.lang.Exception: ",""));
			}else {
				dialog.alert('<div>오류가 발생했습니다.</div><div>재전송 버튼을 눌러 다시 등록해주시기 바랍니다.</div>',function(){});
			}
        	$("#percent").width(0+"%");
        	$("#percentText").text(0+"%");
    		$("#btnReq").prop("disabled", false);
        	return;
        }
    });
    	
}




function onUploadCompleteData(fileDataTmp){
	var ajaxResultData = "";
	var oldDocList = window.parent.docList;
	var dbFileData = fileDataTmp;
	var lastSeqNo = 0;
	
	$.each(oldDocList, function(i ,val) {
		if(val.cc_seqno > lastSeqNo) lastSeqNo = parseInt(lastSeqNo);
	});
	
	for(var key = 0; key < dbFileData.length; key ++){ // 파일 데이터가 있으면 Json HashMap 변환에 에러가 뜨므로 파일 데이터를 지워서 전달
		if(!dbFileData[key].newFile == true){
			dbFileData.splice(key,1);
			key --;
			continue;
		}
		dbFileData[key].acptno = acptNo;
		dbFileData[key].saveName = dbFileData[key].filepath + "/" + dbFileData[key].saveFileName;
		dbFileData[key].filegb = fileGb;
		dbFileData[key].file = null;
		dbFileData[key].seq = key+lastSeqNo+1;
	}
	
	var tmpData = {
			fileList   :   dbFileData,
			requestType	: 	'setDocFile'
		}
	ajaxResultData = ajaxCallWithJson('/webPage/dev/CheckOutServlet', tmpData, 'json');
	if(ajaxResultData == "ok"){
		dialog.alert('업로드 되었습니다.',function(){popClose();});
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

function popClose() {
	window.parent.fileUpModal.close();
}