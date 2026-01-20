/**
 * [사용자정보 > 사용자직무조회] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-24
 * 
 */
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var RGTCDcheckList = window.parent.jobArr2;
var userGrid	= new ax5.ui.grid();

var userGridData 	= null;

userGrid.setConfig({
    target: $('[data-ax5grid="userGrid"]'),
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
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_dutyName", 		label: "담당직무",  		width: '30%'},
        {key: "cm_userName",	label: "사용자",  		width: '30%'},
        {key: "cm_userId", 		label: "사용자번호",  	width: '35%', align: 'left'}
    ]
});

$(document).ready(function() {
	getRGTCDuserList();
	// 엑셀저장 클릭
	$('#btnExcel').bind('click', function() {
		userGrid.exportExcel('담당직무사용자조회.xls');

	});
	// 닫기 클릭
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 조회
function getRGTCDuserList() {
	console.log(RGTCDcheckList);
	var data = new Object();
	data = {
		RGTCDcheckList  : RGTCDcheckList,
		requestType	: 'getRGTCDuserList'
	}
	ajaxAsync('/webPage/modal/userinfo/RgtChgServlet', data, 'json',successgetAllJobCD);
}

// 조회 완료
function successgetAllJobCD(data) {
	userGridData = data;
	userGrid.setData(userGridData);
}


// 모달 닫기
function popClose() {
	window.parent.RgtCdUserListModal.close();
}