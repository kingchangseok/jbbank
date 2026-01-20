var dialog = new ax5.ui.dialog({title: "확인"});
var uploadCnt = 0;
var fileArr 	= [];
var fileGrid 	= false;

var srSw = window.parent.srSw;

$(document).ready(function(){
	window.parent.fileUploadBtn = $('#btnStartUpload');
	
	if(window.parent.fileGrid){
		fileGrid = true;
	}
	
	// 선택완료
	$('#selectBtn').bind('click', function() {
		if(srSw) {
			choiceClick();
			window.parent.fileUploadModal.minimize('bottom-right');
		} else {
			window.parent.showAndHideUploadModal('hide');
		}
	});
});

function choiceClick(){
	var tmpFile = new Object();
	// filelist 그리드의 내용을 보여주는 콘솔로 모달이 close되면 데이터가 삭제되서 콘솔확인 불가능 그래서 위에 하단으로 내리는걸로 해놈
	if(fileGrid){
		var tmp =  $('#files').find('li.media');
		var allText = "";
		$(tmp).each(function(){
			var tmp2 = $(this).find('strong').html();
			tmpFile.fileName = tmp2;
			window.parent.grid_fileList.addRow($.extend({}, tmpFile, {__index: undefined}));
			window.parent.grid_fileList.repaint();
		});
	}
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
	maxFileSize: 1024*1024*1024*1024*1, 	// 	최대 1gb 파일까지
	auto: false,						// 	파일 올렸을시 바로 업로드여부
	queue: false,						//	위에서부터 순서대로 파일 업로드 여부
	extraData: function() {				//	서블릿에 보낼 데이터
			return {
				"noticeAcptno": window.parent.uploadAcptno,
				"uploadCnt": uploadCnt
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
		console.log('New file added #' + id);
		// When a new file is added using the file selector or the DnD area
		ui_add_log('New file added #' + id);
		ui_multi_add_file(id, file);
	},
	onBeforeUpload: function(id){	//	업로드 되기전
		console.log(id, 'uploading', 'Uploading...');
		// about tho start uploading a file
		ui_add_log('Starting the upload2 of #' + id);
		ui_multi_update_file_status(id, 'uploading', 'Uploading...');
		ui_multi_update_file_progress(id, 0, '', true);
		ui_multi_update_file_controls(id, false, true);  // change control buttons status
		++uploadCnt;
	},
	onUploadProgress: function(id, percent){
		// Updating file progress
		ui_multi_update_file_progress(id, percent);
	},
	onUploadSuccess: function(id, data){	
		// 업로드 성공
		console.log('Server Response for file #' + id + ': ' + JSON.stringify(data));
		ui_add_log('Server Response for file #' + id + ': ' + JSON.stringify(data));
		ui_add_log('Upload of file #' + id + ' COMPLETED', 'success');
		ui_multi_update_file_status(id, 'success', 'Upload Complete');
		ui_multi_update_file_progress(id, 100, 'success', false);
		ui_multi_update_file_controls(id, false, false);  // change control buttons status
		--uploadCnt;

		data[0].acptno = data[0].noticeAcptno; 
		data[0].realName = data[0].fileName; 
		data[0].seq = data[0].saveName.split(".")[1];

		fileArr.push(data[0]);
		if(uploadCnt === 0 ) {
			dialog.alert('파일 업로드 완료.', function () {
				window.parent.fileUploadModal.close();
				//DB에 업로드 파일 정보 저장.
				window.parent.fileInfoInsert(fileArr);
	    		window.parent.popNoticeModal.close();
	    		window.parent.getNoticeInfo();
			});
		}
	},
	onUploadCanceled: function(id) {	// 업로드 취소시
		// Happens when a file is directly canceled by the user.
		ui_multi_update_file_status(id, 'warning', 'Canceled by User');
		ui_multi_update_file_progress(id, 0, 'warning', false);
		ui_multi_update_file_controls(id, true, false);
	},
	onUploadError: function(id, xhr, status, message){	// 업로드시 에러
		// Happens when an upload error happens
		ui_multi_update_file_status(id, 'danger', message);
		ui_multi_update_file_progress(id, 0, 'danger', false);  
		ui_multi_update_file_controls(id, true, false, true); // change control buttons status
		
		var jsonValue 		= $.parseJSON(xhr.responseText);	// response
		
		var jsonArr = jsonValue.message.split("\n");
		if (jsonArr.length > 0 && jsonArr[0].indexOf("java.lang.Exception") > -1){
			// java 에서 강제적으로 exception 발생시
			alert("오류발생 : " + replaceAllString(jsonArr[0],"java.lang.Exception: ",""));
			window.parent.fileUploadModalCallBack();
		} else {
			alert("첨부파일 오류발생! 첨부파일을 다시 업로드 해주세요.");
			window.parent.fileUploadModalCallBack();
		}
		
	},
	onFallbackMode: function(){
		// When the browser doesn't support this plugin :(
		ui_add_log('Plugin cant be used here, running Fallback callback', 'danger');
	},
	onFileSizeError: function(file){	// 파일사이즈 초과시
		ui_add_log('File \'' + file.name + '\' cannot be added: size excess limit', 'danger');
	}
});

/*
	전체파일 업로드 시작 또는 취소시
*/
$('#btnApiStart').on('click', function(evt){
	evt.preventDefault();
	$('#drag-and-drop-zone').dmUploader('start');
});

$('#btnApiCancel').on('click', function(evt){
	evt.preventDefault();
	$('#drag-and-drop-zone').dmUploader('cancel');
});

/*
  	각 파일 업로드 시작 및 취소 및 삭제시
 */
$('#files').on('click', 'button.start', function(evt){
	evt.preventDefault();
	var id = $(this).closest('li.media').data('file-id');
  	$('#drag-and-drop-zone').dmUploader('start', id);
});

// 파일 삭제
$('#files').on('click', 'button.cancel', function(evt){
	evt.preventDefault();
	var id = $(this).closest('li.media').data('file-id');
	$('#drag-and-drop-zone').dmUploader('cancel', id);
	
	$(this).closest('li.media').remove();
	if(checkFileLiLength() === 1) $('#files').find('li.empty').fadeIn();
	window.parent.fileLength = checkFileLiLength();
});

// 파일 업로드 스타트
$('#btnStartUpload').on('click',function(evt) {
	evt.preventDefault();
	console.log($(this).closest('li.media'));
	var id = $(this).closest('li.media').data('file-id');
	$('#drag-and-drop-zone').dmUploader('start', id);
});

// 업로드 될 파일 개수
function checkFileLiLength() {
	return $('#files li').length;
}

//Creates a new file and add it to our list
function ui_multi_add_file(id, file)
{
	var template = $('#files-template').text();
	template = template.replace('%%filename%%', file.name);
	template = template.replace('%%filesize%%', byte(file.size,1));
	
	template = $(template);
	template.prop('id', 'uploaderFile' + id);
	template.data('file-id', id);

	$('#files').find('li.empty').fadeOut(); // remove the 'no files yet'
	$('#files').prepend(template);
	
	window.parent.fileLength = checkFileLiLength();
}

// Changes the status messages on our list
function ui_multi_update_file_status(id, status, message)
{
  $('#uploaderFile' + id).find('span.status').html(htmlFilter(message)).prop('class', 'status text-' + status);
}

// Updates a file progress, depending on the parameters it may animate it or change the color.
function ui_multi_update_file_progress(id, percent, color, active)
{
	color = (typeof color === 'undefined' ? false : color);
	active = (typeof active === 'undefined' ? true : active);

	var bar = $('#uploaderFile' + id).find('div.progress-bar');

	bar.width(percent + '%').attr('aria-valuenow', percent);
	bar.toggleClass('progress-bar-striped progress-bar-animated', active);

	if (percent === 0){
		bar.html('');
	} else {
		bar.html(htmlFilter(percent + '%'));
	}

	if (color !== false){
		bar.removeClass('bg-success bg-info bg-warning bg-danger');
    	bar.addClass('bg-' + color);
	}
}

// Toggles the disabled status of Star/Cancel buttons on one particual file
function ui_multi_update_file_controls(id, start, cancel, wasError)
{
	wasError = (typeof wasError === 'undefined' ? false : wasError);

	$('#uploaderFile' + id).find('button.start').prop('disabled', !start);
	$('#uploaderFile' + id).find('button.cancel').prop('disabled', !cancel);

	if (!start && !cancel) {
		$('#uploaderFile' + id).find('.controls').fadeOut();
	} else {
		$('#uploaderFile' + id).find('.controls').fadeIn();
	}

	if (wasError) {
		$('#uploaderFile' + id).find('button.start').html('Retry');
	}
}


/*
 * 기본 베이직 버전 
 $(function(){
  
   * For the sake keeping the code clean and the examples simple this file
   * contains only the plugin configuration & callbacks.
   * 
   * UI functions ui_* can be located in: demo-ui.js
   
  $('#drag-and-drop-zone').dmUploader({ //
    //url: 'backend/upload.php',
	url:  '/webPage/fileupload/upload',
    maxFileSize: 3000000, // 3 Megs 
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
      // When a new file is added using the file selector or the DnD area
      ui_add_log('New file added #' + id);
      ui_multi_add_file(id, file);
    },
    onBeforeUpload: function(id){
      // about tho start uploading a file
      ui_add_log('Starting the upload of #' + id);
      ui_multi_update_file_status(id, 'uploading', 'Uploading...');
      ui_multi_update_file_progress(id, 0, '', true);
    },
    onUploadCanceled: function(id) {
      // Happens when a file is directly canceled by the user.
      ui_multi_update_file_status(id, 'warning', 'Canceled by User');
      ui_multi_update_file_progress(id, 0, 'warning', false);
    },
    onUploadProgress: function(id, percent){
      // Updating file progress
      ui_multi_update_file_progress(id, percent);
    },
    onUploadSuccess: function(id, data){
      // A file was successfully uploaded
      ui_add_log('Server Response for file #' + id + ': ' + JSON.stringify(data));
      ui_add_log('Upload of file #' + id + ' COMPLETED', 'success');
      ui_multi_update_file_status(id, 'success', 'Upload Complete');
      ui_multi_update_file_progress(id, 100, 'success', false);
    },
    onUploadError: function(id, xhr, status, message){
      ui_multi_update_file_status(id, 'danger', message);
      ui_multi_update_file_progress(id, 0, 'danger', false);  
    },
    onFallbackMode: function(){
      // When the browser doesn't support this plugin :(
      ui_add_log('Plugin cant be used here, running Fallback callback', 'danger');
    },
    onFileSizeError: function(file){
      ui_add_log('File \'' + file.name + '\' cannot be added: size excess limit', 'danger');
    }
  });
});*/

