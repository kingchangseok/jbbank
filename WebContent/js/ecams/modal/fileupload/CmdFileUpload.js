var filepath		= "";
var filename		= "";
var uploadCk  		= true;
var firstGrid  		= new ax5.ui.grid();
var firstGridData 	= []; 							//선후행목록 데이타
var data          	= null;							//json parameter
var fileIndex 		= 0;
var fileData 		= [];
var TotalFileSize 	= 0;

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
    		if (this.item.colorsw == '3'){
    			return "fontStyle-cncl";
    		} else if(this.item.colorsw == '5'){
    			return "fontStyle-eror";
    		} else if (this.item.colorsw == '0'){
    			return "fontStyle-ing";
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

	if(firstGridData != undefined && firstGridData.length > 0){
		firstGrid.setData(firstGridData);
	}
	else{
		firstGridData = [];
	}
//	if(uploadCk){
//		$("#limitByte").hide();
//		$("#btnFileUpload").hide();
//		$("#btnFileDelete").hide();
//		$("#btnReq").text("재전송");
//		$("#btnReq").prop("disabled", true);
//		$(".progressbar").css("display","inline-block");
//		fileUpload();
//	}
	
	$('#btnClose').bind('click',function() {
		popClose();
	});
	
	$('#btnReq').bind('click',function(){
		if(uploadCk){
			fileUpload();
		}
		else{
			window.parent.upFiles = firstGridData;
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
	window.parent.fileUploadModal.close();
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
				tmpObj.filegb = "1";
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
	console.log(firstGridGridSeleted.length);
	if(firstGridGridSeleted.length < 1){
		dialog.alert("삭제할 첨부파일을 선택해 주세요");
		return;
	}
	firstGrid.removeRow("selected");
	firstGridData = clone(firstGrid.list);
	firstGrid.repaint();
}

function fileUpload(){
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();
	

	var tmpData = {
		pCode:	'99',
		requestType: 	'getTmpDir'
	}
	tmpPath = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
	if(typeof(tmpPath) == "object"){
		tmpPath = tmpPath[0].cm_path;
	}
	
	//tmpPath = 'C:\\eCAMS\\webTmp\\'; // 테스트 임시경로
	for(var i=0; i<firstGridData.length; i++){
		var index = i + 1;
		formData.append('fullName',tmpPath);
		formData.append('fullpath',tmpPath+"/"+filepath);
		formData.append('saveName',firstGridData[i].name);
		
		//console.log(tmpPath+filepath+firstGridData[i].name);
		formData.append('file',firstGridData[i].file);
		firstGridData[i].index = index;
	}
	
	$("#limitByte").hide();
	$("#btnFileUpload").hide();
	$("#btnFileDelete").hide();
	$("#btnReq").text("재전송");
	$("#btnReq").prop("disabled", true);
	$(".progressbar").css("display","inline-block");

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
        	xhr.upload.onprogress = function(e) { //progress 이벤트 리스너 추가
	        	var percent = e.loaded * 100 / e.total;
	        	setProgress(percent);
        	};
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
	var ajaxResultData = "";
	var dbFileData = firstGridData;
	var cmdData = new Object();
	var dirpath = replaceAllString(response,'//','/');
	
	var tmpData = {
		cmdText : dirpath.trim(),
		requestType : 'fileAttUpdt'
	}
	ajaxCallWithJson('/webPage/ecmm/Cmm1600Servlet', tmpData, 'json');
	dialog.alert('파일송신을 완료하였습니다.\n'+response,function(){popClose()});
//	if(rst=='0'){
//		
//	} else {
//		dialog.alert('파일송신을 완료하였습니다1.\n'+dirpath,function(){popClose()});
//	}
}

function setProgress(percent){
	$("#percent").width(percent+"%");
	$("#percentText").text(Math.round(percent)+"%");
}
