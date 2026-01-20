/**
 * [기본관리/사용자환경설정] 내 기본정보 보기
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부

var jobGrid			= new ax5.ui.grid();
var chgPwdModal 	= new ax5.ui.modal();		//비밀번호 변경 모달
var setDaeGyulModal = new ax5.ui.modal();		//부재설정 모달

var jobGridData		= null;
var myWin 			= null;
var txtUserIdP      = null;

jobGrid.setConfig({
    target: $('[data-ax5grid="jobGrid"]'),
    sortable: true, 
    multiSort: true,
    header: {
        align: "center"
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
        {key: "jobgrp", 	label: "시스템",  			width: '40%', align: 'left'},
        {key: "job", 		label: "업무명 (업무코드)",  	width: '60%', align: 'left'}
    ]
});

$('input:radio[name^="userRadio"]').wRadio({theme: 'circle-radial red', selector: 'checkmark_black'});
$('input.checkbox-user').wCheck({theme: 'square-classic red', selector: 'checkmark_black', highlightLabel: true});


$(document).ready(function() {
	if (userId == null || userId == '') return;
	
	$('.contentFrame').contents().find('#txtUserId').html(userId);
	$('.contentFrame').contents().find('#txtUserName').html(userName);
	
	if (adminYN) {
		//· 시스템관리자 / 동기화제외사용자
		$('.contentFrame').contents().find('#basicInfo').html('· 시스템관리자');
	} else {
		$('.contentFrame').contents().find('#basicInfo').html('');
	}
	
	//비밀번호 변경하기
	$('#txtPassWd').bind('click',function(){
		txtUserIdP = $('#txtUserId').val().trim();
		setTimeout(function() {
			chgPwdModal.open({
				width: 600,
				height: 278,
				iframe: {
					method: "get",
					url: "../../modal/userinfo/ChangePassWdModal.jsp",
					param: "callBack=changePassWdModalCallBack"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						txtUserIdP = '';
					}
				}
			}, function () {
			});
		}, 200);
	});
	//부재설정하기
	$('#btnDaeGyul').bind('click', function(){
		txtUserIdP = $('#txtUserId').val().trim();
		setTimeout(function() {
			setDaeGyulModal.open({
				width: 600,
				height: 650,
				iframe: {
					method: "get",
					url: "../../modal/userinfo/SetDaeGyulModal.jsp",
					param: "callBack=setDaeGyulModalCallBack"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						getUserInfo();	// 부재등록, 해제시 사용자정보 다시 조회
						txtUserIdP = '';
					}
				}
			}, function () {
			});
		}, 200);
	});
	
	//사용자조회
	getUserInfo();
});
var changePassWdModalCallBack = function(){
	chgPwdModal.close();
};
var setDaeGyulModalCallBack = function(){
	setDaeGyulModal.close();
};


function getUserInfo() {
	//사용자정보 조회
	var data = new Object();
	data = {
		userId 		: userId,
		userName 	: '',
		requestType	: 'getUserInfo'
	}
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetUserInfo);
}

//사용자정보가져오기 완료
function successGetUserInfo(data){
	var userInfo = data[0];

	if(userInfo.ID === 'ERROR') {
		dialog.alert('등록되지 않은 사용자입니다.', function() {});
		return;
	}
	
	$('#txtUserId').val(userInfo.cm_userid);
	
	var etcInfo = '';
	if(userInfo.cm_manid === 'Y') {
		etcInfo = ' / 직원';
	} else {
		etcInfo = ' / 외주직원';
	}
	$('.contentFrame').contents().find('#txtUserName').html(userInfo.cm_username+etcInfo);
	
	
	etcInfo = '';
	if (adminYN) {
		etcInfo = '· 시스템관리자';
	}
	if(userInfo.cm_handrun === 'Y') {
		if (etcInfo == '') etcInfo = '· 동기화제외사용자';
		else etcInfo = etcInfo + ' / 동기화제외사용자';
	}
	$('.contentFrame').contents().find('#basicInfo').html(etcInfo);

	$('.contentFrame').contents().find('#txtIp').html(userInfo.cm_ipaddress);
	$('.contentFrame').contents().find('#txtTel1').html(userInfo.cm_telno1);
	$('.contentFrame').contents().find('#txtTel2').html(userInfo.cm_telno2);
	$('.contentFrame').contents().find('#txtEMail').html(userInfo.cm_email);
	
	// 부재등록 내용이 있을 때와 없을때 값 초기화 설정
	if(typeof userInfo.Txt_DaeGyul !== "undefined"){
		$('.contentFrame').contents().find('#txtDaeGyul').html(userInfo.Txt_DaeGyul);
	} else {
		$('.contentFrame').contents().find('#txtDaeGyul').html("");
	}
	
	if(typeof userInfo.Txt_BlankTerm !== "undefined"){
		$('.contentFrame').contents().find('#txtBlankTerm').html(userInfo.Txt_BlankTerm);
	} else {
		$('.contentFrame').contents().find('#txtBlankTerm').html("");
	}
	
	if(typeof userInfo.Txt_BlankSayu !== "undefined"){
		$('.contentFrame').contents().find('#txtBlankSayu').html(userInfo.Txt_BlankSayu);
	} else {
		$('.contentFrame').contents().find('#txtBlankSayu').html("");
	}
	
	$('.contentFrame').contents().find('#txtPosition').html(userInfo.cm_position);
	$('.contentFrame').contents().find('#txtDuty').html(userInfo.cm_duty);
	$('.contentFrame').contents().find('#txtOrg').html(userInfo.deptname1);
	$('.contentFrame').contents().find('#txtOrgAdd').html(userInfo.deptname2);
	
	getUserRgtCd();
}
// 사용자 권한 가져오기
function getUserRgtCd() {
	var data = new Object();
	data = {
		userId 		: $('#txtUserId').val().trim(),
		requestType	: 'getUserRgtCd'
	}
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetUserRgtCd);
}
// 사용자 권한 가져오기 완료
function successGetUserRgtCd(data) {
	ulDutyInfoData = data;
	
	var rgtlist = '';
	ulDutyInfoData.forEach(function(item, index) {
		if(item.checkbox !== undefined && item.checkbox === 'true') {
			if (rgtlist == '') {
				rgtlist = item.cm_codename;
			} else {
				rgtlist = rgtlist + ' / ' + item.cm_codename;
			}
		}
	});
	$('.contentFrame').contents().find('#txtRgtCd').html(rgtlist);
	
	getUserJobList();
}
// 사용자 업무 리스트 가져오기
function getUserJobList() {
	var data = new Object();
	data = {
		gbnCd 		: 'USER',
		userId 		: $('#txtUserId').val().trim(),
		requestType	: 'getUserJobList'
	}
	ajaxAsync('/webPage/mypage/MyInfoServlet', data, 'json',successGetUserJobList);
}
//담당업무 가져오기 완료
function successGetUserJobList(data) {
	jobGridData = data;
	jobGrid.setData(jobGridData);
}

function openWindow(type, popinfo) {
	var nHeight, nWidth, cURL, winName;
	if ( (type+'_'+popinfo.substr(5,2)) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
    winName = type+'_'+popinfo.substr(5,2);
    
	var f = document.popPam;
    f.user.value 	= userId;
    
    if (type == '1') {
        f.acptno.value	= popinfo;
        
		nHeight = 740;
	    nWidth  = 1200;
		cURL = "/webPage/winpop/PopRequestDetail.jsp";
    } else {
        f.itemid.value	= popinfo;
        
    	nHeight = 350;
    	nWidth  = 1200;
		cURL = "/webPage/winpop/PopProgramInfo.jsp";
    }
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}