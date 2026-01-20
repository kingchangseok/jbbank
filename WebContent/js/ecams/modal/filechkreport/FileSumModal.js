/**
 * [파일대사결과조회 > 대사기록조회 > 대사내용합계표] 팝업 화면의 기능 정의
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

var fileSumGrid		= new ax5.ui.grid();

var fileSumGridData = [];

var DaesaResult = window.parent.DaesaResult;

fileSumGrid.setConfig({
    target: $('[data-ax5grid="fileSumGrid"]'),
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
        	dblClickFileSumGrid(this.dindex);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "cm_sysmsg",		label: "시스템명",  	width: '25%'},
    	{key: "cd_svrip", 		label: "서버IP",  	width: '20%'},
        {key: "cd_svrname", 	label: "서버명",  	width: '15%'},
        {key: "cm_codename", 	label: "일치여부",  	width: '20%'},
        {key: "ResultCnt", 		label: "건수",  		width: '10%'},
    ]
});

$(document).ready(function() {
	getDaesaResult();
	
	// 닫기 클릭
	$('#btnClose').bind('click', function() {
		popClose();
	});
	
	$('#btnExcel').bind('click', function() {
		fileSumGrid.exportExcel('대사내용합계표.xls');
	});
});

// 대사내용 합계표 가져오기
function getDaesaResult() {
	var data = new Object();
	data = {
		UserId 		: DaesaResult.UserId,
		diffdt 		: DaesaResult.diffdt,
		diffseq 	: DaesaResult.diffseq,
		svrip 		: DaesaResult.svrip,
		detail 		: DaesaResult.detail,
		requestType	: 'DaesaResult'
	}
	ajaxAsync('/webPage/ecmp/Cmp2700Servlet', data, 'json',successGetDaesaResult);
}

// 대사내용 합계표 가져오기 완료
function successGetDaesaResult(data) {
	fileSumGridData = data;
	fileSumGrid.setData(fileSumGridData);
}

//대사내용 합계 표 더블클릭
function dblClickFileSumGrid(index) {
	var selItem = fileSumGrid.list[index];
	selItem.cd_diffdt 	= DaesaResult.diffdt;
	selItem.cd_diffseq 	= DaesaResult.diffseq;
	selItem.SVRIP 		= DaesaResult.svrip;
	selItem.cd_syscd 	= selItem.cm_syscd;
	selItem.cd_diffrst 	= selItem.cd_diffrst;
	window.parent.popSelItem = selItem;
	
	window.parent.getResult();
	
	window.parent.fileSumModal.close();
	window.parent.fileHisModal.close();
}


//닫기
function popClose() {
	window.parent.fileSumModal.close();
}
