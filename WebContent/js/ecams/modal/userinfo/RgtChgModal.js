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
        {key: "cm_userid", 		label: "사용자ID",  		width: '10%'},
        {key: "cm_username",	label: "사용자이름",  		width: '10%'},
        {key: "cm_sysmsg", 		label: "시스템",  	width: '20%', align: 'left'},
        {key: "cm_jobname", 	label: "업무",  		width: '10%', align: 'left'},
        {key: "cm_creatdt", 	label: "등록일",  		width: '25%', align: 'left'},
        {key: "cm_closedt", 	label: "폐기일",  		width: '25%', align: 'left'}
    ]
});

$(document).ready(function() {
	searchGBN();
	
	// 사용자 엔터
	$('#txtUser').bind('keypress', function(evnet) {
		if(event.keyCode === 13) {
			getAllUserRgtCd();
		}
	});
	
	// 조회 클릭
	$('#btnQry').bind('click', function() {
		getAllUserRgtCd();
	});
	// 엑셀저장 클릭
	$('#btnExcel').bind('click', function() {
		userGrid.exportExcel('사용자업무이력조회.xls');
	});
	// 닫기 클릭
	$('#btnExit').bind('click', function() {
		popClose();
	});
});

// 조회
function getAllUserRgtCd() {
	var searchGBN = getSelectedVal('cboGbn').value;
	var userId = null;
	
	if(searchGBN == 0){
		userId = "";
	} else {
		userId = $('#txtUser').val().trim();
	}
	
	var data = new Object();
	data = {
		userId 		: userId,
		requestType	: 'getAllJobCD'
	}
	ajaxAsync('/webPage/modal/userinfo/RgtChgServlet', data, 'json',successgetAllJobCD);
}

// 조회 완료
function successgetAllJobCD(data) {
	userGridData = data;
	userGrid.setData(userGridData);
}

function searchGBN() {
	cboSysCdData = [
		{cm_micode: "0", cm_codename: "전체조회"},
		{cm_micode: "1", cm_codename: "사용자선택"}
	]
	$('[data-ax5select="cboGbn"]').ax5select({
        options: injectCboDataToArr(cboSysCdData, 'cm_micode' , 'cm_codename')
	});
}

// 모달 닫기
function popClose() {
	window.parent.RgtChgModal.close();
}