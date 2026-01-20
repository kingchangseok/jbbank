/**
 * [파일대사결과조회 > 대사기록조회] 팝업 화면의 기능 정의
 * 
 * <pre>
 * &lt;b&gt;History&lt;/b&gt;
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-02-08
 * 
 */


var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var fileHisGrid		= new ax5.ui.grid();
var fileSumModal 	= new ax5.ui.modal();

var fileHisGridData = [];

var stDate	= window.parent.stDate;
var edDate	= window.parent.edDate;
var detail	= window.parent.detail;
var popSelItem = window.parent.popSelItem;

var DaesaResult = null;

fileHisGrid.setConfig({
    target: $('[data-ax5grid="fileHisGrid"]'),
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
        	dblClickFileHisGrid(this.dindex);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "대사내용합계표"},
        ],
        popupFilter: function (item, param) {

        	if(fileHisGrid.getList('selected').length < 1){
        		return false;
        	}
        	
        	fileHisGrid.clearSelect();
        	fileHisGrid.select(Number(param.dindex));
        	
        	var selItem = fileHisGrid.list[param.dindex];
        	/*if(selItem.cd_diffrst === '00') {
        		return true;
        	} else {
        		return false;
        	}*/
        	return true;
        },
        onClick: function (item, param) {
        	var selItem = fileHisGrid.list[param.dindex];
        	window.parent.openFileSumModal(selItem);
        	fileHisGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
        {key: "diffdt", 	label: "대사일자",  		width: '18%'},
        {key: "cm_sysmsg",	label: "시스템명",  		width: '15%'},
        {key: "SVRIP", 		label: "서버명",  		width: '16%'},
        {key: "CD_SVRNAME",	label: "서버IP",  		width: '16%'},
        {key: "stdate", 	label: "작업시작시간",  	width: '25%'},
        {key: "eddate", 	label: "작업종료시간",  	width: '25%'}
    ]
});

$(document).ready(function() {
	
	getDaesa();
	
	// 닫기 클릭
	$('#btnClose').bind('click', function() {
		popClose();
	});
});

// 대사 기록 조회
function getDaesa() {
	if(detail === 'Y') {
		fileHisGrid.removeColumn(1);
	}
	
	var data = new Object();
	data = {
		datStD 		: stDate,
		datEdD 		: edDate,
		UserId 		: userId,
		requestType	: detail === 'Y' ? 'getDaesa2' : 'getDaesa'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmp/Cmp2700Servlet', data, 'json',successGetDaesa);
}

// 대사 기록 조회 완료
function successGetDaesa(data) {
	$(".loding-div").remove();
	fileHisGridData = data;
	fileHisGrid.setData(fileHisGridData);
}

// 대사기록조회 그리드 더블클릭
function dblClickFileHisGrid(index) {
	var selItem = fileHisGrid.list[index];
	if (selItem.__index < 0) return;
	
	selItem.cd_syscd 	= 'ALL';
	selItem.cd_diffrst 	= 'ALL';
	selItem.cd_diffdt 	= selItem.cd_diffdt;
	selItem.cd_diffseq 	= selItem.cd_diffseq;
	selItem.SVRIP	 	= selItem.CD_SVRNAME;
	window.parent.popSelItem = selItem;
	
	popClose();
}

// 팝업 닫기
function popClose(){
	window.parent.fileHisModal.close();
}