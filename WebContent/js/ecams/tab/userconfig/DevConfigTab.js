/**
 * 1. 개발환경설정
 *
 * <pre>
 * 	작성자	:	정선희
 * 	버전		:	1.0
 *  수정일 	: 	2020-12-15
 *
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드

var userConfigGrid		= new ax5.ui.grid();
var dirSelectModal      = new ax5.ui.modal();

var userConfigGridData 	= [];
var cboSysCdData		= [];

$('[data-ax5select="cboSysCd"]').ax5select({
  options: []
});

userConfigGrid.setConfig({
    target: $('[data-ax5grid="userConfigGrid"]'),
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
            clickUserConfigGrid(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_sysmsg", 		label: "시스템",  			width: '25%', align: "left"},
        {key: "cd_devhome",		label: "개발 Home Directory", width: '25%', align: "left"},
        /*{key: "cd_agentdir", 	label: "Agent설치경로",  		width: '25%', align: "left"},
        {key: "cd_userip", 		label: "로컬IP",  			width: '25%', align: "left"},*/
    ]
});

$('[data-ax5select="cboSysCdData"]').ax5select({
	options: []
});

$(document).ready(function() {

	getSysInfo();
	getUserConfigList();

	// 등록 클릭 이벤트
	$('#btnReq').bind('click', function() {
		insertUserConfig();
	});
	
	// PC경로찾기 클릭 이벤트
	$('#btnDir').bind('click', function() {
		btnDirClick();
	});

	// 삭제 클릭 이벤트
	$('#btnDel').bind('click', function() {
		deleteUserConfig();
	});

	$("#btnSearch").bind("click", function(){
		getUserConfigList();
	});

	// 시스템 변경 이벤트
	$('#cboSysCd').bind('change', function() {
		$('#txtAgentDir').val('');
		$('#txtDevDir').val('');
		$('#txtIp').val('');

		if(getSelectedIndex('cboSysCd') < 1) {
			return;
		}

		var selSysCd = getSelectedVal('cboSysCd').cm_syscd;
		for(var i = 0; i < userConfigGridData.length; i++) {
			if(userConfigGridData[i].cd_syscd === selSysCd) {
				$('#txtAgentDir').val(userConfigGridData[i].cd_agentdir);
				$('#txtDevDir').val(userConfigGridData[i].cd_devhome);
				$('#txtIp').val(userConfigGridData[i].cd_userip);
				break;
			}
		}
//		if($('#txtAgentDir').val() == '') {
//			$('#txtAgentDir').val('C:\\ecamsagent');
//		}
	});
})

// 사용자 환경설정 그리드 클릭
function clickUserConfigGrid(selIndex) {
	var selItem = userConfigGrid.list[selIndex];
	var findSw	= false;

	$('#txtAgentDir').val(selItem.cd_agentdir);
	$('#txtDevDir').val(selItem.cd_devhome);
	$('#txtIp').val(selItem.cd_userip);

	// 시스템콤보에 해당 시스템 값 있으면 세팅
	for(var i = 0; i < cboSysCdData.length; i++) {
		if(cboSysCdData[i].cm_syscd === selItem.cd_syscd) {
			findSw = true;
			$('[data-ax5select="cboSysCd"]').ax5select('setValue', selItem.cd_syscd, true);findSw
			break;
		}
	}

	// 콤보에 값 없으면 선택하세요 선택
	if(!findSw) {
		$('[data-ax5select="cboSysCd"]').ax5select('setValue', '00000', true);
	}
}

//PC경로찾기 클릭
function btnDirClick() {
	dialog.alert('ActiveX 미사용!'); return;
	
	modalData = [];
	
	if (getSelectedIndex('cboSysCd')<1) {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
	
	/*modalData.agentIp = $('#txtIp').val().trim();
	modalData.agentDir = $('#txtAgentDir').val();*/
	
	selDevHome = '';
	
	setTimeout(function() {	
		modalCloseFlag = false
		dirSelectModal.open({
			width: 570,
			height: 520,
			defaultSize: false,
			iframe: {
				method: "get",
				url: "../../modal/DirSelectModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
					
					if (modalCloseFlag) {
						selDevHome = replaceAllString(selDevHome,'/','\\');
						$('#txtDevDir').val(selDevHome);
					}
				}
			}
		}, function () {
		});
	}, 200);
}

// 사용자환경설정 삭제
function deleteUserConfig() {
	var delList = [];

	if(userConfigGrid.getList().length ==0){
		dialog.alert('삭제할 항목이 없습니다.', function() {});
		return;
	}


	var selIndex = userConfigGrid.selectedDataIndexs;
	if(selIndex.length === 0 ) {
		dialog.alert('삭제할 항목을 선택한 후 처리하십시오.', function() {});
		return;
	}

	for(var i=0; i<selIndex.length; i++){
		userConfigGrid.removeRow(selIndex[i]);
	}

	var data = new Object();
	data = {
		UserId 		: userId,
		rvList 	: userConfigGrid.getList(),
		requestType	: 'getTblDelete'
	}
	ajaxAsync('/webPage/ecmd/Cmd1300Servlet', data, 'json',successDeleteUserConfig);
}

// 사용자환경설정 삭제 완료
function successDeleteUserConfig(data) {
	if(data > 0 ) {
		dialog.alert('선택한 Home Directory가 삭제되었습니다.', function() {
			getUserConfigList();
		});
	}
}

// 사용자환경설정 등록
function insertUserConfig() {
//	var agentDir 	= $('#txtAgentDir').val().trim();
	var devDir 		= $('#txtDevDir').val().trim();
//	var userIp		= $('#txtIp').val().trim();


	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}

	if(devDir.length === 0) {
		dialog.alert('개발 Home Directory를 입력하여 주십시오.' , function() {});
		return;
	}
	
//	if(agentDir.length === 0) {
//		dialog.alert('Agent설치 경로를 입력하여 주십시오.' , function() {});
//		return;
//	}
//	
//	if(userIp.length === 0) {
//		dialog.alert('로컬IP를 입력하여 주십시오.' , function() {});
//		return;
//	}

	var data = new Object();
	data = {
		UserId 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').cm_syscd,
		Lbl_Dir 	: devDir,
//		agentDir 	: agentDir,
//		localIp		: userIp,
		requestType	: 'getTblUpdate'
	}
	ajaxAsync('/webPage/ecmd/Cmd1300Servlet', data, 'json',successInsertUserConfig);
}

// 사용자 환경설정 등록 완료
function successInsertUserConfig(data) {
	if(data > 0 ) {
		dialog.alert('Home Directory가 등록되었습니다.', function() {
			getUserConfigList();
		});
	}
}

// 사용자 환경설정 리스트 가져오기
function getUserConfigList() {
	$('[data-ax5grid="userConfigGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();

	var data = new Object();
	data = {
			UserId 	: userId,
		requestType	: 'getSql_Qry'
	}
	ajaxAsync('/webPage/ecmd/Cmd1300Servlet', data, 'json',successGetUserConfigList);
}

// 사용자 환경설정 리스트 가져오기 완료
function successGetUserConfigList(data) {
	$(".loding-div").remove();
	userConfigGridData = data;
	userConfigGrid.setData(userConfigGridData);
	
	if(cboSysCdData.length > 0) {
		$('#cboSysCd').trigger('change');
	}
}

// 시스템 정보 가져오기
function getSysInfo() {
	var sysInfoData = new Object();
	sysListInfoData = {
			UserId	: userId,
			SecuYn 	: adminYN ? 'Y' : 'N',
			SelMsg  : "SEL",
			CloseYn : "N",
			ReqCd   :  '04',
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', sysListInfoData, 'json',successGetSysInfo);
}

// 시스템 정보 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	cboSysCdData = cboSysCdData.filter(function(data) {
		if (data.cm_sysinfo.substr(0,1) == "1")
			return false;
		else
			return true;
	});

	$('[data-ax5select="cboSysCd"]').ax5select({
		options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
}
