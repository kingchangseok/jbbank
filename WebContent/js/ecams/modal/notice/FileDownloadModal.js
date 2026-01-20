/** File Download Javascript Page 
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */
var dialog 			= new ax5.ui.dialog({title: "확인"});
var confirmDialog 	= new ax5.ui.dialog();	//알럿,확인창
var downAcptno 		= null;
var downFileCnt 	= 0;
var noticeFolderPath= null;
var downFilelist 	= [];
var fileArr 		= null;
var fileInfo 		= null;
var uploadFileCnt 	= 0;
var uploadSucessCnt = 0;
var uploadSelectedFileLength = 0;

confirmDialog.setConfig({
    title: "파일 업로드 알림",
    theme: "info"
});


$(document).ready(function() {
	getNoticeFolderPath();
	downAcptno  = window.parent.downAcptno;
	downFileCnt = window.parent.downFileCnt; 
	if(downFileCnt > 0 ) {
		getFileList();
	}
});

// 공지사항 다운로드 경로 가져오기
function getNoticeFolderPath() {
	var data = new Object();
	data = {
		pCode:	'01',
		requestType : 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetNoticeFolderPath);
}
// 공지사항 다운로드 경로 가져오기 완료
function successGetNoticeFolderPath(data) {
	noticeFolderPath = data;
	if(typeof(noticeFolderPath) == "object"){
		noticeFolderPath = data[0].cm_path;
	}
}

// 첨부파일 리스트 가져오기
function getFileList() {
	var data = new Object();
	data = {
		requestType : 'getNoticeFileList',
		acptNo : downAcptno
	}
	ajaxAsync('/webPage/common/AttachServlet', data, 'json',successGetFileList);
}

// 첨부파일 리스트 가져오기 완료
function successGetFileList(data) {
	$('#files').empty();
	downFilelist = data;
	
	var appendStr = '';
	downFilelist.forEach(function(item,itemIndex){
		appendStr += '<li class="media">';
		appendStr += '	<div class="media-body mb-1">';
		appendStr += '		<p>';
		appendStr += '			<a href="' +homePath +'/webPage/fileupload/upload?&folderPath='+noticeFolderPath+'\\'+downAcptno+'\\'+item.orgname+'" style="font-size: 14px;">'+item.orgname+'</a>';
		appendStr += '			<button onclick="delFile(\''+item.orgname+'\')" class="btn btn-sm btn-danger" role="button" style="float: right;">삭제</button>';
		appendStr += '		</p>';
		appendStr += '		<hr class="mt-1 mb-1" />';
		appendStr += '	</div>';
		appendStr += '</li>';
	});
	$('#files').html(appendStr);
}

// 파일삭제
function delFile(item) {
	confirmDialog.confirm({
		msg: '해당 파일을 삭제 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var fileInfo 		= new Object();
			fileInfo.acptNo 	= downAcptno;
			fileInfo.fileName 	= item;
			var data = new Object();
			data = {
				requestType : 'deleteNoticeFile',
				fileData : fileInfo
			}
			ajaxAsync('/webPage/common/AttachServlet', data, 'json',successDelFile);
		}
	});
}
// 파일삭제 완료
function successDelFile(data) {
	dialog.alert('파일이 삭제 되었습니다.', function () {});
	$('#fileDownBody').empty();
	getFileList();
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
	maxFileSize: 1024*1024*1024*1, 		// 	최대 1gb 파일까지
	auto: false,						// 	파일 올렸을시 바로 업로드여부
	queue: false,						//	위에서부터 순서대로 파일 업로드 여부
	extraData: function() {				//	서블릿에 보낼 데이터
			return {
				"noticeAcptno": downAcptno
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
		fileInfo = {"noticeAcptno":downAcptno,"fileName":file.name};
		fileArr.push(fileInfo);
		
		ui_multi_add_file(id, file);
		uploadFileCnt++;
		
		if(uploadFileCnt === uploadSelectedFileLength) {
			confirmDialog.confirm({
				msg: '해당 파일을 업로드 하시겠습니까?',
			}, function(){
				var id = $(this).closest('li.media').data('file-id');
				console.log(id);
				if(this.key === 'ok') {
					$('#drag-and-drop-zone').dmUploader('start', id);
					
					//DB에 업로드 파일 정보 저장.
					window.parent.fileInfoInsert(fileArr);
				}else{
					uploadSucessCnt = 0;
				}
				
				uploadFileCnt = 0;
				fileArr = null;
				fileInfo = null;
			});
		}
		
		
	},
	onBeforeUpload: function(id){	//	업로드 되기전
	},
	onUploadProgress: function(id, percent){
		ui_multi_update_file_progress(id, percent);
	},
	onUploadSuccess: function(id, data){	// 업로드 성공
		uploadSucessCnt++;
		
		if(uploadSucessCnt === uploadSelectedFileLength) {
			dialog.alert('파일이 업로드 되었습니다.', function () {
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


