var acptNo 		= window.parent.acptNo;
var upFiles  	= window.parent.upFiles;
var reqCd    	= window.parent.reqCd;
var userId   	= window.parent.userId;
var gubun    	= window.parent.fileGbn;
var dirGbn   	= window.parent.dirGbn;
var subDocPath 	= window.parent.subDocPath;

var firstGrid	= new ax5.ui.grid();

var filepath		= "";
var filename		= "";
var firstGridData 	= [];
var data          	= null;
var fileIndex 		= 0;
var fileData 		= [];
var TotalFileSize 	= 0;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : true,
    showRowSelector: true,
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
        {key: "name", label: "File",  width: '70%', align : 'left'},
        {key: "size", label: "Size",  width: '30%'}
    ]
});

$(document).ready(function() {
	firstGridData = upFiles;
	if (gubun == 'U') {   //파일첨부
		if (firstGridData == null || firstGridData == '' || firstGridData == undefined)  {
			dialog.alert('선택된 파일이 없습니다. 확인 후 진행하여 주시기 바랍니다.');
			popClose();
			return;
		}
		$('#btnReq').css('display','inline-block');
		$("#btnReq").text("재전송");
		$("#btnReq").prop("disabled", true);
		$('#btnFileUpload').css('display','none');
		$('#btnFileDelete').css('display','none');
		$('#btnCnl').css('display','none');
		$('#btnEnd').css('display','none');
		$(".progressbar").css("display","inline-block");
		
		firstGrid.setData(firstGridData);
		if(firstGridData.length > 0){
			fileUpload();
		}
	} else {
		$('#btnReq').css('display','none');
		$('#btnFileUpload').css('display','inline-block');
		$('#btnFileDelete').css('display','inline-block');
		$('#btnCnl').css('display','inline-block');
		$('#btnEnd').css('display','inline-block');
	}
	if(firstGridData != undefined && firstGridData.length > 0){
		firstGrid.setData(firstGridData);
	}
	else{
		firstGridData = [];
	}
	
	$("#limitByte").hide();
	
	$('#btnCnl').bind('click',function() {
		window.parent.upFiles = null;
		popClose();
	});

	$('#btnFileUpload').bind('click',function() {
		$("#docFile").click();
	});
	$('#btnEnd').bind('click',function() {
		window.parent.addFile(firstGridData);
		
		popClose();
	});
	$("#docFile").bind("click",function(){
		if($("#docFile").get(0).files && $("#docFile").get(0).files[0]){
			fileChange($("#docFile").get(0));
		}
	});

	$('#btnFileDelete').bind('click',function() {
		btnFileDeleteClick();
	});
	
	$('#btnReq').bind('click',function(){
		fileUpload();
	});
	
});

function popClose(){
	window.parent.fileUploadModal.close();
}

function fileChange(file){
	var jqueryFiles = file;
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
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){popClose();});
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){popClose();});
				TotalFileSize = TotalFileSize - sizeReal;
				break;
			}
			var fileCk = true;
			
			for(var j=0; j<firstGridData.length; j++){				
				if(firstGridData[j].name == name){
					dialog.alert('파일명 : ' + name +'\n 은 이미 등록된 파일입니다.\n확인 후 다시 등록해 주세요',function(){popClose();});
					//$("#"+file).remove();
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
				
				firstGridData.push(tmpObj);
			}
		}
		firstGrid.setData(firstGridData);
	}
}

function fileUpload(){
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	var tmpData = {
		pCode 		: dirGbn,
		requestType	: 'getTmpDir'
	}
	tmpPath = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');

	//tmpPath = 'C:\\html5\\temp\\'; //테스트중
	
	for(var i=0; i<firstGridData.length; i++){
		if(firstGridData[i].newFile == true){
			var index = i + 1;
			formData.append('fullName',tmpPath);
			formData.append('fullpath',tmpPath+"/"+subDocPath);
			formData.append('saveName',firstGridData[i].saveFileName);
			formData.append('file',firstGridData[i].file);
			
			firstGridData[i].index = index;
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
        	onUploadCompleteData(firstGridData);
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
    		popCloseFlag = false;
        	return;
        }
    });
    	
}

function onUploadCompleteData(firstGridDataTmp){
	// 파일 데이터가 있으면 Json HashMap 변환에 에러가 뜨므로 파일 데이터를 지워서 전달
	//console.log('onUploadCompleteData ==>', firstGridDataTmp);
	var dbFileData = firstGridDataTmp;
	for(var key = 0; key < dbFileData.length; key ++){ 
		dbFileData[key].file = null;
	}
	
	window.parent.popCloseFlag = true;
	window.parent.setFile(dbFileData);
	popClose();
}

function setProgress(percent){
	$("#percent").width(percent+"%");
	$("#percentText").text(Math.round(percent)+"%");
}
function btnFileDeleteClick() {
	
	var selList = firstGrid.getList('selected');
	for (var i=0;firstGridData.length>i;i++) {
		if (firstGridData[i].__selected__ == true) {
			firstGridData.splice(i,1);
			i--;
		}
	}
	firstGrid.setData(firstGridData);
}