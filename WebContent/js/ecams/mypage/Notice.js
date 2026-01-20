/**
 * 공지사항 화면 기능 정의
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var picker				= new ax5.ui.picker();		// DATE PICKER
var popNoticeModal 		= new ax5.ui.modal();		// 공지사항등록/수정 팝업
var fileDownloadModal 	= new ax5.ui.modal();		// 파일다운로드모달 (하나씩 파일첨부 가능)

var noticeGrid = new ax5.ui.grid();				// 공지사항 그리드

var noticeGridData 	= [];
var uploadData		= [];
var noticePopData 	= null;

var strStD 		= "";
var strEdD 		= "";
var fileLength 	= 0 ;

var uploadAcptno= null;
var downAcptno 	= null;
var downFileCnt = 0;

var fileUploadBtn 	= null;
var uploadModal 	= null;
var selectedGridItem= null;
var popNoticeSw = true;

var cboFindData = [];

var title_;
var class_;

$('#start_date').val(getDate('MON',-1));
$('#end_date').val(getDate('DATE',0));

picker.bind(defaultPickerInfoLarge('basic', 'top'));

$('[data-ax5select="cboFind"]').ax5select({
    options: []
});

var fileUploadModal = new ax5.ui.modal({
	theme: "warning",
    header: {
        title: '<i class="glyphicon glyphicon-file" aria-hidden="true"></i> [첨부파일]',
        btns: {
            minimize: {
                label: '<i class="fa fa-minus-circle" aria-hidden="true"></i>', onClick: function(){
                	showAndHideUploadModal('hide');
                }
            },
            close: {
                label: '<i class="fa fa-times-circle" aria-hidden="true"></i>', onClick: function(){
                	showAndHideUploadModal('hide');
                }
            }
        }
    }
});

noticeGrid.setConfig({
    target: $('[data-ax5grid="noticeGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	noticePopData = noticeGrid.list[noticeGrid.selectedDataIndexs];
        	openPopNotice();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "CM_TITLE", 		label: "제목",  		width: '52%',	align: 'left'},
        {key: "CM_USERNAME", 	label: "등록자",  	width: '8%'},
        {key: "CM_ACPTDATE", 	label: "등록일",  	width: '8%'},
        {key: "CM_STDATE", 		label: "팝업시작일",  	width: '8%'},
        {key: "CM_EDDATE", 		label: "팝업마감일",  	width: '8%'},
        {key: "CM_NOTIYN", 		label: "팝업",  		width: '8%'},
        {key: "fileCnt", 		label: "첨부파일",  	width: '8%',
         formatter: function(){
        	 var htmlStr = this.value > 0 ? "<button class='btn-ecams-grid' onclick='openFileDownload("+this.item.CM_ACPTNO+","+this.item.fileCnt+")' ondblclick='blockDbClick(event)' >첨부파일</button>" : '';
        	 return htmlStr;
         }
        }
    ]
});

$(document).ready(function() {
	getCodeInfo();
	getNoticeInfo();
	cboFind_Change();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#end_date').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#end_date").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	console.log(adminYN);
	if(!adminYN) {
		$('#btnReg').attr('disabled', 'disabled');
	} else{
		popNoticeSw = false;
	}
	
	$("#btnReg").bind('click', function() {	
		noticePopData = null;
		openPopNotice();
	})
	
	// 엔터 이벤트
	$('#txtFind').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQry').trigger('click');
		}
	});
	
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		noticeGrid.exportExcel("공지사항.xls");
	});
	
	// 조회
	$('#btnQry').bind('click', function() {
		getNoticeInfo();
	});
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#end_date').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#end_date").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});

	// 검색조건 변경
	$('#cboFind').bind('change', function() {
		cboFind_Change();
	});
})

// 첨부파일 더블 클릭시 열리는 공지사항 편집창 안열리게 막기
function blockDbClick(e) {
	e.preventDefault();
    e.stopPropagation();
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfo('FIND','','N')]);

	cboFindData = codeInfos.FIND;
	
	$('[data-ax5select="cboFind"]').ax5select({
		options: injectCboDataToArr(cboFindData, 'cm_micode' , 'cm_codename')
	});
}

// 공지사항 정보 가져오기
function getNoticeInfo() {
	var TxtFind_text = $('#txtFind').val().trim();
	strStD = replaceAllString($("#start_date").val(), "/", "");
	strEdD = replaceAllString($("#end_date").val(), "/", "");

	var data = new Object();
	data = {
		Cbo_Find      	: getSelectedVal("cboFind").cm_micode,
		Txt_Find 		: TxtFind_text,
		strStD 			: strStD,
		strEdD 			: strEdD,
		requestType   	: 'get_sql_Qry'
	}
	
	$('[data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm2100Servlet', data, 'json',successGetNoticeInfo);
}

//공지사항 정보 가져오기 완료
function successGetNoticeInfo(data) {
	$(".loding-div").remove();
	noticeGridData = data;
	noticeGrid.setData(noticeGridData);
	
	// 기존 선택 되어있던 로우 재선택
	if(selectedGridItem !== null) {
		for(var i = 0; i < noticeGridData.length; i++) {
			if(noticeGridData[i].cm_acptno === selectedGridItem.cm_acptno) {
				noticeGrid.select(i, {selected: true});
				break;
			}
		}
	}
}

function cboFind_Change(){
	$("#txtFind").prop("disabled", true);
	$("#divPicker").css("display","none");
	$("#txtFind").css("display","inline-block");
	
	 if (getSelectedVal("cboFind").cm_micode == "01" || getSelectedVal("cboFind").cm_micode == "02"){
		$("#txtFind").prop("disabled",false);
	}else if(getSelectedVal("cboFind").cm_micode == "03") {
		$("#divPicker").css("display","inline-block");
		$("#txtFind").css("display","none");
	}
}

// 공지사항 등록 /수정 오픈
function openPopNotice(){
	setTimeout(function() {
		popNoticeModal.open({
			width: 620,
			height: 373,
			iframe: {
				method: "get",
				url: "../modal/notice/NoticeModal.jsp",
				param: "callBack=popNoticeModalCallBack"
			},
			onStateChanged: function () {
				// mask
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
					$('#btnQry').trigger('click');
				}
			}
		}, function () {
		});
	}, 200);
}

var popNoticeModalCallBack = function(){
	popNoticeModal.close();
};

var fileUploadModalCallBack = function() {
	fileLength = 0;
	fileUploadModal.close();
}

var fileDownloadModalCallBack = function() {
	fileDownloadModal.close();
}

// 첨부파일 업로드 모달을 숨겼다가 보여줬다가
function showAndHideUploadModal(showAndHide) {
	if(showAndHide === 'show') {
		$('#' + uploadModal.config.id).css('display', 'block');
	}
	if(showAndHide === 'hide') {
		$('#' + uploadModal.config.id).css('display', 'none');
	}
}

// 첨부파일 모달 오픈
function openFileUpload() {
	setTimeout(function() {
		uploadModal = fileUploadModal.open({
			width: 520,
			height: 325,
			iframe: {
				method: "get",
				url: 	"../modal/notice/FileUpModal.jsp",
				param: "callBack=fileUploadModalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
				}
				else if (this.state === "close") {
					$('#btnQry').trigger('click');
				}
			}
		}, function () {
		});
	}, 200);
	
}

// 다운로드 모달 오픈
function openFileDownload(acptno,fileCnt) {
	
	if(acptno !== '') {
		downAcptno = acptno;
		downFileCnt = fileCnt;
	}
	setTimeout(function() {
		fileDownloadModal.open({
			width: 520,
			height: 315,
			iframe: {
				method: "get",
				url: 	"../modal/notice/FileDownloadModalNew.jsp",
				param: "callBack=fileDownloadModalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
				}
				else if (this.state === "close") {
					selectedGridItem = noticeGrid.getList('selected')[0];
					$('#btnQry').trigger('click');
				}
			}
		}, function () {
		});
	}, 200);
}

// 공지사항 등록시 파입 업로드
function fileInfoInsert(data) {
	if(data.length == 0){
		return;
	}
	uploadData = data;
	
	for(var i=0; i<uploadData.lenth; i++) {
		//uploadData[i].seq = String(i +1);
		if(uploadData[i].acptno == undefined || uploadData[i].acptno == null || uploadData[i].acptno == '') {
			uploadData[i].acptno = uploadData[i].noticeAcptno;
		}
		if(uploadData[i].realName == undefined || uploadData[i].realName == null || uploadData[i].realName == '') {
			uploadData[i].realName = uploadData[i].fileName;
		}
		uploadData[i].seq = Number(uploadData[i].saveName.substr(uploadData[i].saveName.lastindexOf('.')))
	}
	
	var data = new Object();
	data = {
		fileList    : uploadData,
		requestType	: 'setDocFile'
	}
	console.log('[fileInfoInsert] ==>', data);
	var result = ajaxCallWithJson('/webPage/ecmm/Cmm2101Servlet', data, 'json');
	if(result != "ok") {
		dialog.alert("파일 업로드에 실패하였습니다.");
	}
}

function checkModalLength() {
	return $("div[id*='modal']").length;
}

function returnFileModal() {
	return $("div[id*='modal-15']");
}

//그리드에 마우스 툴팁 달기
$(document).on("mouseenter","[data-ax5grid-panel='body'] span",function(event){
	if(this.innerHTML == ""){
		return;
	}
	//첫번째 컬럼에만 툴팁 달기
	if($(this).closest("td").index() > 0) return;
	//그리드 정보 가져오기
	var gridRowInfo = noticeGrid.list[parseInt($(this).closest("td").closest("tr").attr("data-ax5grid-tr-data-index"))];
	var contents = gridRowInfo.CM_CONTENTS;	
	
	if(contents.length > 500 ) {
		contents = contents.substring(0,500) + '....';
	}
	$(this).attr("title",contents);
	
	// title을 변수에 저장
	title_ = $(this).attr("title");
	// class를 변수에 저장
	class_ = $(this).attr("class");
	// title 속성 삭제 ( 기본 툴팁 기능 방지 )
	$(this).attr("title","");
	
	$("body").append("<div id='tip'></div>");
	if (class_ == "img") {
		$("#tip").html(imgTag);
		$("#tip").css("width","100px");
	} else {
		$("#tip").css("display","inline-block");
		$("#tip").html('<pre>'+htmlFilter(title_)+'</pre>');
	}
	
	var pageX = event.pageX;
	var pageY = event.pageY;
	
	$("#tip").css({left : pageX + "px", top : pageY + "px"}).fadeIn(100);
	return;
}).on('mouseleave',"[data-ax5grid-panel='body'] span",function(){
	$(this).attr("title", '');
	//$(this).attr("title", title_);
	$("#tip").remove();	
});
