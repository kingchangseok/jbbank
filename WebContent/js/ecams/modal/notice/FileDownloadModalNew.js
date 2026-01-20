/** File Download 기능 정의 수정
 *
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.1
 *  수정일 : 2019-08-06
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

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
var downGridData 	= [];
var uploadCnt		= 0;

var downGrid  		= new ax5.ui.grid();
var os				= '';
var seperator		= '';

var localUsr = window.location.protocol+'//'+window.location.hostname + ':' + window.location.port;

os = getOSInfo();
seperator = fileSeperator(os);

console.log(os, seperator);

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
        	if (this.column.key == "b") {
//        		var fullPath = encodeURIComponent(noticeFolderPath+ '/' +downAcptno.substr(0,4)+ '/' +this.item.savename);
//        		var fileName = encodeURIComponent(this.item.orgname);
//        		location.href = homePath + '/webPage/fileupload/upload?fullPath='+fullPath+'&fileName=' + encodeURIComponent(fileName);
        		fileDown(noticeFolderPath+ '/' +downAcptno.substr(0,4)+ '/' +this.item.savename, this.item.orgname);
        	}
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = downGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;

	       	//문서열기
//	       	var fullPath = encodeURIComponent(noticeFolderPath+ '/' +downAcptno.substr(0,4)+ '/' +this.item.savename);
//    		var fileName = encodeURIComponent(this.item.orgname);
//    		location.href = homePath + '/webPage/fileupload/upload?fullPath='+fullPath+'&fileName=' + encodeURIComponent(fileName);
    		fileDown(noticeFolderPath+ '/' +downAcptno.substr(0,4)+ '/' +this.item.savename, this.item.orgname);
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "orgname", label: "파일명",  width: '80%', align: "left"},
        /*{key: "fileSize", label: "사이즈",  width: '15%', align: "left",
        	formatter: function() {
        		if(this.value === undefined) return null;
        		else return byte(Number(this.value),1);
        	}
        },
        */
        {key: 'b', label: '파일다운', align: 'center',
        	formatter: function() {
        		return '<img width="16px" height="16px" src="'+localUsr+'/img/download.png" alt="file download" style="cursor:pointer"/>';
        	}
        }
    ]
});


$(document).ready(function() {
	getNoticeFolderPath();
	downAcptno  = window.parent.downAcptno + "";
	downFileCnt = window.parent.downFileCnt;

	if(window.parent.popNoticeSw !== undefined && window.parent.popNoticeSw) {
		$('.filebox').css('display', 'none');
		$('#btnDel').css('display', 'none');
	} else if(window.parent.noticeGrid.list[window.parent.noticeGrid.selectedDataIndexs].CM_EDITOR !== userId) {
		$('.filebox').css('display', 'none');
		$('#btnDel').css('display', 'none');
	}

	if(downFileCnt > 0 ) {
		getFileList();
	}

	// 닫기 클릭
	$('#btnClose, #btnExit').bind('click', function() {
		window.parent.fileDownloadModal.close();
	});
	// 파일삭제 클릭
	$('#btnDel').bind('click', function() {
		if(downGrid.selectedDataIndexs.length === 0 ) return;
		var selItem = downGrid.list[downGrid.selectedDataIndexs];

		delFile(selItem);
	});

	// 일괄다운로드
	$('#btnAllDw').bind('click', function() {
		if(downFileCnt > 0) {
			var zipName = '공지사항일괄다운';
			if(window.parent.menuNm != undefined && window.parent.menuNm == "FAQ"){
				zipName = 'FAQ일괄다운';
			}
			location.href = homePath + '/webPage/fileupload/upload?&zipPath='+noticeFolderPath+  '/' +downAcptno.substr(0,4) + '&zipName=' + encodeURIComponent(encodeURIComponent(zipName)) + '&actpno='+downAcptno+'';
			return;
		} else {
			dialog.alert('다운로드 받을 파일이 없습니다.', function() {});
		}

	});
});

function fileSeperator(str) {
	return str.substr(0,1) === 'W' ? '\\' : '/';
}

function getOSInfo() {
   var ua = window.navigator.userAgent;

   if(ua.indexOf("NT 6.0") != -1) return "Windows";
   else if(ua.indexOf("NT 5.2") != -1) return "Windows Server 2003";
   else if(ua.indexOf("NT 5.1") != -1) return "Windows XP";
   else if(ua.indexOf("NT 5.0") != -1) return "Windows 2000";
   else if(ua.indexOf("NT") != -1) return "Windows NT";
   else if(ua.indexOf("9x 4.90") != -1) return "Windows Me";
   else if(ua.indexOf("98") != -1) return "Windows 98";
   else if(ua.indexOf("95") != -1) return "Windows 95";
   else if(ua.indexOf("Win16") != -1) return "Windows 3.x";
   else if(ua.indexOf("Windows") != -1) return "Windows";
   else if(ua.indexOf("Linux") != -1) return "Linux";
   else if(ua.indexOf("Macintosh") != -1) return "Macintosh";
   else return "";
}

// 공지사항 다운로드 경로 가져오기
function getNoticeFolderPath() {
	var data = new Object();
	data = {
			pCode:	'23',
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
		requestType : 'getFileList',
		AcptNo : downAcptno
	}
	ajaxAsync('/webPage/ecmm/Cmm2100Servlet', data, 'json',successGetFileList);
}

// 첨부파일 리스트 가져오기 완료
function successGetFileList(data) {
	downGridData = data;
	downGrid.setData(downGridData);
}

// 파일삭제
function delFile(item) {
	confirmDialog.confirm({
		msg: '해당 파일을 삭제 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var fileInfo 		= new Object();
			fileInfo.cm_acptno 	= downAcptno;
			fileInfo.savename 	= item.savename;
			fileInfo.cm_seqno = item.cm_seqno;
			var data = new Object();
			data = {
				requestType : 'removeDocFile',
				fileData : fileInfo
			}
			ajaxAsync('/webPage/ecmm/Cmm2101Servlet', data, 'json',successDelFile);
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
	maxFileSize: 1024*1024*1024*10, 	// 	최대 1gb 파일까지
	auto: false,						// 	파일 올렸을시 바로 업로드여부
	queue: false,						//	위에서부터 순서대로 파일 업로드 여부
	extraData: function() {				//	서블릿에 보낼 데이터
			return {
				"noticeAcptno": downAcptno,
				"uploadCnt": uploadCnt,
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

		var findSw = false;
		for(var i=0; i<downGridData.length; i++) {
			if(downGridData[i].orgname == file.name) {
				findSw = true;
				break;
			}
		}

		if(findSw) {
			dialog.alert('동일한 파일명이 존재합니다.');
			return false;
		}

		if(uploadFileCnt === 0) {
			fileArr = null;
			fileArr = [];

			var x = document.getElementById("ex_file");
			console.log( x.files);
			if ('files' in x) {
				if (x.files.length > 0) {
					uploadSelectedFileLength = x.files.length;
				}
			}
		}

		fileInfo = null;
		fileInfo = {"noticeAcptno":downAcptno,"filegb":"1","fileName":file.name, "saveName":file.saveName, "fileSize": file.size};
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

					//DB에 업로드 파일 정보 저장.
//					window.parent.fileInfoInsert(fileArr);
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
		/*
		 * 파일 올리는 동안 파일 첨부, 삭제, 닫기 막기
		 */
		changeCursor(true);
		$('.filebox').css('pointer-events', 'none');
		$('#lblAdd').css('background-color', '#ddd');
		$('#lblAdd').css('color', '#ccc');
		$('#btnDel').prop('disabled', true);
		$('#btnClose').prop('disabled', true);
		++uploadCnt;
	},
	onUploadProgress: function(id, percent){
		ui_multi_update_file_progress(id, percent);
	},
	onUploadSuccess: function(id, data){	// 업로드 성공
		uploadSucessCnt++;
		--uploadCnt;

		fileArr = [];
		fileArr.push(data[0]);
		//DB에 업로드 파일 정보 저장.
		window.parent.fileInfoInsert(fileArr);
		
		if(uploadSucessCnt === uploadSelectedFileLength) {
			dialog.alert('파일이 업로드 되었습니다.', function () {
				/*
				 * 파일 올리는 동안 파일 첨부, 삭제, 닫기 막은것 풀기
				 */
				changeCursor(false);
				$('.filebox').css('pointer-events', '');
				$('#lblAdd').css('background-color', '#fff');
				$('#lblAdd').css('color', '#103300');
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


