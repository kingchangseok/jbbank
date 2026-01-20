var userId 		= window.parent.userId;			// 접속자 ID

var dialog 			= new ax5.ui.dialog({title: "확인"});
var confirmDialog 	= new ax5.ui.dialog();	//알럿,확인창
var acptno 			= null;
var downFilelist 	= [];
var tmpPath			= null;

var downGrid  		= new ax5.ui.grid();

confirmDialog.setConfig({
    title: "파일다운로드창 알림",
    theme: "info"
});

downGrid.setConfig({
    target: $('[data-ax5grid="donwGrid"]'),
    //sortable: true, 
    //multiSort: true,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	if (this.colIndex == 1) {
        		var fileName = this.item.orgname;
        		var fullPath = tmpPath + '/' + this.item.savename;
        		fileDown(fullPath, fileName);
        	}
        },
        onDBLClick: function () {
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "orgname", label: "파일명",  width: '70%', align: "left"},
        /*{key: "fileSize", label: "사이즈",  width: '15%', align: "left",
        	formatter: function() {
        		if(this.value === undefined) return null;
        		else return byte(Number(this.value),1);
        	}
        },
        */
        {key: 'b', label: '파일다운', align: 'center', 
        	formatter: function() {
        		return '<img width="16px" height="16px" src="/img/download.png" alt="file download" style="cursor:pointer"/>';
        	}
        }
    ]
});


$(document).ready(function() {
	getNoticeFolderPath();
	acptNo  = window.parent.pReqNo;
	getFileList();
	
	// 닫기 클릭
	$('#btnClose, #btnExit').bind('click', function() {
		window.parent.fileDownListModal.close();
	});
	// 일괄다운로드
	$('#btnAllDw').bind('click', function() {
		if(acptNo.toString().substring(0,4) < 2020){
			dialog.alert("2020년도 전의 공지사항 첨부파일은\n일괄 다운로드 할 수 없습니다.");
			return;
		}
		if(downFileCnt > 0) {
			var zipName = '공지사항일괄다운';
			location.href = homePath + '/webPage/fileupload/upload?&zipPath='+noticeFolderPath+  '/' +acptNo.toString().substring(0,4) + '/' +acptNo+ '&zipName=' + encodeURIComponent(zipName);
			return;
		} else {
			dialog.alert('다운로드 받을 파일이 없습니다.', function() {});
		}
		
	});
});

function fileSeperator(str) {
	return str.substring(0,1) === 'W' ? '\\' : '/';
}


// 공지사항 다운로드 경로 가져오기
function getNoticeFolderPath() {
	var data = new Object();
	data = {
			pcode:	'22',
			requestType : 'getSystemPath'
	}
	ajaxAsync('/webPage/common/CommonSystemPath', data, 'json',successGetNoticeFolderPath);
}
// 공지사항 다운로드 경로 가져오기 완료
function successGetNoticeFolderPath(data) {
	tmpPath = data;
	if(typeof(noticeFolderPath) == "object"){
		tmpPath = data[0].cm_path;
	}
}

// 첨부파일 리스트 가져오기
function getFileList() {
	var data = new Object();
	data = {
		requestType : 'getFileList',
		acptNo : acptNo,
		gbnCd : '1'
	}
	ajaxAsync('/webPage/winpop/PopRequestDetailServlet', data, 'json',successGetFileList);
}

// 첨부파일 리스트 가져오기 완료
function successGetFileList(data) {
	console.log(data);
	downGrid.setData(data);
}

/*
 * For the sake keeping the code clean and the examples simple this file
 * contains only the plugin configuration & callbacks.
 * 
 * UI functions ui_* can be located in:
 *   - assets/demo/uploader/js/ui-main.js
 *   - assets/demo/uploader/js/ui-multiple.js
 *   - assets/demo/uploader/js/ui-single.js
 */
$('#drag-and-drop-zone').dmUploader({
	url: homePath + '/webPage/fileupload/upload',	// 	서블릿 주소
	maxFileSize: 1024*1024*1024*10, 	// 	최대 1gb 파일까지
	auto: false,						// 	파일 올렸을시 바로 업로드여부
	queue: false,						//	위에서부터 순서대로 파일 업로드 여부
	extraData: function() {				//	서블릿에 보낼 데이터
			return {
				"noticeAcptno": acptNo
			};
	},
	onDragEnter: function(){
	    // Happens when dragging something over the DnD area
	    this.addClass('active');
	},
	onDragLeave: function(){
	    // Happens when dragging something OUT of the DnD area
	    this.removeClass('active');
	},
	onInit: function(){
		// Plugin is ready to use
	    ui_add_log('Penguin initialized :)', 'info');
	},
	onComplete: function(){
		// All files in the queue are processed (success or error)
		ui_add_log('All pending tranfers finished');
	},
	onNewFile: function(id, file){
		
		if(uploadFileCnt === 0) {
			fileArr = null;
			fileArr = [];
			
			var x = document.getElementById("ex_file");
			if ('files' in x) {
				if (x.files.length > 0) {
					uploadSelectedFileLength = x.files.length;
				}
			}
		}
		
		fileInfo = null;
		fileInfo = {"noticeAcptno":acptNo,"fileName":file.name, "fileSize": file.size};
		fileArr.push(fileInfo);
		
		ui_multi_add_file(id, file);
		uploadFileCnt++;
		
		if(uploadFileCnt === uploadSelectedFileLength) {
			confirmDialog.confirm({
				msg: '해당 파일을 업로드 하시겠습니까?',
			}, function(){
				var id = $(this).closest('li.media').data('file-id');
				if(this.key === 'ok') {
					$('#drag-and-drop-zone').dmUploader('start', id);
					
				}else{
					uploadSucessCnt = 0;
				}
			});
		}
	},
	onBeforeUpload: function(id){	//	업로드 되기전
		/*
		 * 파일 올리는 동안 파일 첨부, 삭제, 닫기 막기
		 */
		changeCursor(true);
		$('.filebox').css('pointer-events', 'none');
		$('#lblAdd').css('background-color', '#ddd');
		$('#lblAdd').css('color', '#ccc');
		$('#btnDel').prop('disabled', true);
		$('#btnClose').prop('disabled', true);
	},
	onUploadProgress: function(id, percent){
		ui_multi_update_file_progress(id, percent);
	},
	onUploadSuccess: function(id, data){	// 업로드 성공
		uploadSucessCnt++;
		if(uploadSucessCnt === uploadSelectedFileLength) {
			dialog.alert('파일이 업로드 되었습니다.', function () {
				//DB에 업로드 파일 정보 저장.
				window.parent.fileInfoInsert(fileArr);
				uploadFileCnt = 0;
				fileArr = null;
				fileInfo = null;
				/*
				 * 파일 올리는 동안 파일 첨부, 삭제, 닫기 막은것 풀기
				 */
				changeCursor(false);
				$('.filebox').css('pointer-events', '');
				$('#lblAdd').css('background-color', '#fff');
				$('#lblAdd').css('color', '#333');
				$('#btnDel').prop('disabled', false);
				$('#btnClose').prop('disabled', false);
				
				$('#fileDownBody').empty();
				getFileList();
				uploadSucessCnt = 0;
				uploadSucessCnt = 0;

			});
		}
	},
	onUploadCanceled: function(id) {	// 업로드 취소시
	},
	onUploadError: function(id, xhr, status, message){	// 업로드시 에러
		// Happens when an upload error happens
		var jsonValue 		= $.parseJSON(xhr.responseText);	// response
		
		var jsonArr = jsonValue.message.split("\n");
		if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
			// java 에서 강제적으로 exception 발생시
			alert("오류발생 : " + replaceAllString(jsonArr[0],"java.lang.Exception: ",""));
		} else {
			alert("첨부파일 오류발생! 첨부파일을 다시 업로드 해주세요.");
		}
		
		location.reload();
		
	},
	onFallbackMode: function(){
	},
	onFileSizeError: function(file){	// 파일사이즈 초과시
		ui_add_log('File \'' + file.name + '\' cannot be added: size excess limit', 'danger');
	}
});

//파일 삭제
$('#files').on('click', 'button.cancel', function(evt){
	evt.preventDefault();
	var id = $(this).closest('li.media').data('file-id');
	$('#drag-and-drop-zone').dmUploader('cancel', id);
	
	$(this).closest('li.media').remove();
	if(checkFileLiLength() === 1) $('#files').find('li.empty').fadeIn();
	window.parent.fileLength = checkFileLiLength();
});

//Creates a new file and add it to our list
function ui_multi_add_file(id, file)
{
	var template = $('#files-template').text();
	template = template.replace('%%filename%%', file.name);

	template = $(template);
	template.prop('id', 'uploaderFile' + id);
	template.data('file-id', id);

	$('#files').find('li.empty').fadeOut(); // remove the 'no files yet'
	$('#files').prepend(template);
}


