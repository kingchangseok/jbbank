var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var userGrid		= new ax5.ui.grid();
var userGridData 	= [];

var otherUser = "N";

userGrid.setConfig({
    target: $('[data-ax5grid="userGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	btnConfirm_Click();
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_userid", 		label: "ID",  		width: '20%'},
        {key: "cm_posname",		label: "직무",  		width: '20%'},
        {key: "cm_username",	label: "성명",  		width: '20%'},
        {key: "cm_deptname",	label: "부서",  		width: '40%'}
    ]
});

$(document).ready(function() {
	// 검색
	$('#btnQry').bind('click', function() {
		getUserInfo();
	});
	// 등록
	$('#btnConfirm').bind('click', function() {
		btnConfirm_Click();
	});
	// 취소
	$('#btnCncl').bind('click', function() {
		popClose(false);
	});
	
	$("#txtSearch").bind("keypress", function(e){
		if(e.keyCode == 13){
			$('#btnQry').trigger('click');
		}
	});
});

// 전체 사용자 조회
function getUserInfo() {
	var data;
	
	if(otherUser == "N") {
		data = new Object();
		data = {
			srcWord : $("#txtSearch").val().trim(),
			deptCd  : window.parent.deptCd,
			requestType : 'getUserList'
		}
		ajaxAsync('/webPage/ecmc/UserselectServlet', data, 'json',successGetUserInfo);
	}else {
		data = new Object();
		data = {
			srcWord : $("#txtSearch").val().trim(),
			deptCd  : window.parent.deptCd,
			requestType : 'OhterUserList'
		}
		ajaxAsync('/webPage/ecmc/UserselectServlet', data, 'json',successGetUserInfo);
	}
}

// 전체 사용자 조회 완료
function successGetUserInfo(data) {
	$(".loding-div").remove();
	userGridData = data;
	userGrid.setData(userGridData);
	
	if(userGridData.length < 1) {
		dialog.alert("검색 결과가 없습니다.",function(){});
	}
}

function btnConfirm_Click() {
	var gridSelectedIndex = userGrid.selectedDataIndexs;
	var selectedGridItem = userGrid.list[userGrid.selectedDataIndexs];
	if(gridSelectedIndex  < 0) {
		dialog.alert("대상자를 지정하셔야 합니다.",function(){});
		return;
	} else {
		selectedGridItem = userGrid.list[userGrid.selectedDataIndexs];
		if (selectedGridItem < 0) return;
		window.parent.popSelItem = selectedGridItem;
	}
	popClose(true);
}

// 모달 닫기
function popClose(flag) {
	window.parent.userModalCloseFlag = flag;
	window.parent.userModal.close();
}