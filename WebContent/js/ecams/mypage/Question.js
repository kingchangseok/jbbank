/**
 * 공지사항 화면 기능 정의
 *
 * <pre>
 * 	작성자 	: 이용문
 * 	버전   	: 1.0
 *  수정일 	: 2019-06-27
 *
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var picker				= new ax5.ui.picker();		// DATE PICKER
var popNoticeModal 			= new ax5.ui.modal();		// 공지사항등록/수정 팝업
var popNoticeModal2 		= new ax5.ui.modal();		// 답글등록 모달
var fileDownloadModal 		= new ax5.ui.modal();		// 파일다운로드모달 (하나씩 파일첨부 가능)

var noticeGrid 		= new ax5.ui.grid();				// 공지사항 그리드
var mask 			= new ax5.ui.mask();
var mask2 			= new ax5.ui.mask();

var noticeGridData 	= [];
var uploadData		= [];
var dataObj 		= {memo_id:"", user_id:"", memo_date:"", repyn:""};

var menuNm = "Q/A";

mask2.setConfig({
	zIndex:2000
});

var Cbo_FindData = [];

var strStD 		= "";
var strEdD 		= "";
var fileLength 	= 0 ;

var uploadAcptno= null;
var QnaCk= true;
var downAcptno 	= null;
var downFileCnt = 0;

var fileUploadBtn 	= null;
var uploadModal 	= null;
var selectedGridItem= null;
var popNoticeSw = true;
var noticePopData = [];
var title_;
var class_;

$('[data-ax5select="CboGbn"]').ax5select({
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
        	dataObj.memo_id = noticePopData.CM_ACPTNO;
        	dataObj.memo_date = noticePopData.CM_GBNCD;
            dataObj.user_id = userId; 
            dataObj.seqno = 0;
            dataObj.repyn = "n";

            openPopNotice();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "CM_TITLE", 		label: "제목",  		width: '60%',align: 'left'},
        {key: "CM_USERNAME", 	label: "등록인",  	width: '11%'},
        {key: "CM_ACPTDATE", 	label: "등록일",  	width: '16%'},
        {key: "fileCnt", 		label: "첨부파일",  	width: '13%',
         formatter: function(){
        	 var htmlStr = this.value > 0 ? "<button class='btn-ecams-grid' onclick='openFileDownload("+this.item.CM_ACPTNO+","+this.item.fileCnt+")' ondblclick='blockDbClick(event)' >첨부파일</button>" : '';
        	 return htmlStr;
         }
        }
    ]
});

$('#start_date').val(getDate('MON',-1));
$('#end_date').val(getDate('DATE',0));

picker.bind(defaultPickerInfoLarge('basic', 'top'));

$(document).ready(function() {
	setCboElement();
	getNoticeInfo();
	Cbo_Find_Change();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#end_date').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#end_date").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	// Q/A 등록
	$("#btnReg").bind('click', function() {
		noticePopData = [];
		dataObj = [];
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
		noticeGrid.exportExcel("Question.xls");
	});

	//검색조건 변경
	$("#Cbo_Find").bind("change", function(){
		Cbo_Find_Change();
	});
	
	
	// 조회
	$('#btnQry').bind('click', function() {
		getNoticeInfo();
	});

})

// 공지사항 등록 /수정 오픈
function openPopNotice(gbn){
	if(gbn == "reply"){
		setTimeout(function() {
			popNoticeModal2.open({
				width: 620,
				height: 370,
				zIndex: 2001, //modal이 2개 이기 때문에 z-index +1
				iframe: {
					method: "get",
					url: "../modal/notice/QuestionModal.jsp",
					param: "callBack=popNoticeModalCallBack"
				},
				onStateChanged: function () {
					// mask
					if (this.state === "open") {
						mask2.open();
						$(mask2.$mask[0]).children(".ax-mask-bg").css("opacity", "0.2");
					}
					else if (this.state === "close") {
						mask2.close();
					}
				}
			}, function () {
			});
		}, 200);
	} else {
		setTimeout(function() {
			popNoticeModal.open({
				width: 620, // 다국어 페이지면  width 조절
				height: 370,
				iframe: {
					method: "get",
					url: "../modal/notice/QuestionModal.jsp",
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
}

// 첨부파일 더블 클릭시 열리는 공지사항 편집창 안열리게 막기
function blockDbClick(e) {
	e.preventDefault();
    e.stopPropagation();
}

// 공지사항 정보 가져오기
function getNoticeInfo() {
	var TxtFind_text = $('#txtFind').val().trim();
	strStD = replaceAllString($("#start_date").val(), "/", "");
	strEdD = replaceAllString($("#end_date").val(), "/", "");

	// 그리드에 loading 이미지 추가 후 보여주기
	$('[data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();

	var data = new Object();
	data = {
		Cbo_Find 	    : getSelectedVal("Cbo_Find").cm_micode,
		Txt_Find 	    : TxtFind_text,
		strStD 			: strStD,
		strEdD 			: strEdD,
		requestType   	: 'get_sql_Qry'
	}
	ajaxAsync('/webPage/ecmm/Cmm2300Servlet', data, 'json',success_getNoticeInfo);
}

//공지사항 정보 가져오기 완료
function success_getNoticeInfo(data) {
	$(".loding-div").remove();

	noticeGridData = data;
	noticeGrid.setData(noticeGridData);

	// 기존 선택 되어있던 로우 재선택
	if(selectedGridItem != null && selectedGridItem != undefined) {
		for(var i = 0; i < noticeGridData.length; i++) {
			if(noticeGridData[i].CM_ACPTNO === selectedGridItem.CM_ACPTNO) {
				noticeGrid.select(i, {selected: true});
				break;
			}
		}
	}
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
			zIndex: 2002,
			iframe: {
				method: "get",
				url: 	"../modal/notice/FileDownloadModalQnA.jsp",
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

	$(uploadData).each(function(){
		this.filegb = "2";
	});
	var data = new Object();
	data = {
		fileList    : uploadData,
		requestType	: 'setDocFile'
	}
	ajaxCallWithJson('/webPage/ecmm/Cmm2101Servlet', data, 'json');
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

	if( pageY + $("#tip").height() + 10 > $("html").height()){
		$("#tip").css({top: "", bottom: "0px", left: pageX +20});
	}
	return;
}).on('mouseleave',"[data-ax5grid-panel='body'] span",function(){
	$(this).attr("title", '');
	//$(this).attr("title", title_);
	$("#tip").remove();
});


function setCboElement() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('FIND','','N'),
	]);

	Cbo_FindData 		= codeInfos.FIND;
	
	$('[data-ax5select="Cbo_Find"]').ax5select({
		options: injectCboDataToArr(Cbo_FindData, 'cm_micode' , 'cm_codename')
	});
}

function Cbo_Find_Change(){
	
	$("#txtFind").prop("disabled", true);
	$("#divPicker").css("display","none");
	$("#txtFind").css("display","inline-block");
	
	if(getSelectedVal("Cbo_Find").cm_micode == '01' || getSelectedVal("Cbo_Find").cm_micode == '02'){
		$("#txtFind").prop("disabled", false);
	}
	
	if(getSelectedVal("Cbo_Find").cm_micode == '03'){
		$("#divPicker").css("display","inline-block");
		$("#txtFind").css("display","none");
	}
	
}