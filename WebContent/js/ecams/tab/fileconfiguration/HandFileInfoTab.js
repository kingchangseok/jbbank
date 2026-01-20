/**
 * [환경설정 > 수기파일대사 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-01
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var handGrid 	= new ax5.ui.grid();

var handGridData 	= [];
var cboSysData		= [];
//var cboSvrData		= [];

picker.bind(defaultPickerInfoLarge('dateDeploy', 'top'));

handGrid.setConfig({
    target: $('[data-ax5grid="handGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "lastdt", 	label: "등록일시",		width: '10%', align: "center"},
        {key: "diffday",	label: "작업일시",  	width: '10%', align: "center"},
        {key: "sysmsg",		label: "대상시스템",  	width: '25%', align: "left"},
        {key: "svrip",		label: "대상서버",   	width: '30%', align: "left"},
    ]
});

$('#txtDeploy').val('00:00');
$('[data-ax5select="cboSys"]').ax5select({
    options: []
});
//$('[data-ax5select="cboSvr"]').ax5select({
//	options: [{value:'ALL',text:'전체'}]
//});


$('#txtDeploy').timepicker({
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: true,
    scrollbar: true
 });


$('input.checkbox-file').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getSysInfo();
	getHandrunDiff();
	
	// 시스템 INPUT ENTER
	$('#txtSys').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			var txtSys 	= $('#txtSys').val().trim();
			var item 	= null;
			if(txtSys.length === 0 ) {
				$('[data-ax5select="cboSys"]').ax5select('setValue', cboSysData[0].cm_syscd, true);
			}
			
			for(var i=0; i<cboSysData.length; i++) {
				item = cboSysData[i];
				if(item.cm_sysmsg.indexOf(txtSys) >= 0) {
					$('[data-ax5select="cboSys"]').ax5select('setValue', item.cm_syscd, true);
					break;
				}
			}
		}
	});
	
	// 시스템 선택시 서버 콤보 세팅
	$('#cboSys').bind('change', function() {
//		getSvrList();
		$("#txtSys").val(getSelectedVal("cboSys").cm_sysmsg);
	});
	
	// 등록 버튼 클릭
	$('#btnRun').bind('click', function() {
		setHandrunDiff();
	});
	// 삭제 클릭
	$('#btnDel').bind('click', function() {
		delHandrunDiff();
	});
	
	// 즉시 실행 클릭
	$('#chkTime').bind('click', function() {
		if($('#chkTime').is(':checked')) {
			$('#dateDeploy').prop('disabled', true);
			$('#txtDeploy').prop('disabled', true);
			$('#dateDeploy').siblings().css('background-color','rgb(235, 235, 228)');
			$('#txtDeploy').siblings().css('background-color','rgb(235, 235, 228)');
		} else {
			$('#dateDeploy').prop('disabled', false);
			$('#txtDeploy').prop('disabled', false);
			$('#dateDeploy').siblings().css('background-color','#fff');
			$('#txtDeploy').siblings().css('background-color','#fff');
		}
	});
});

//// 서버 콤보 정보 가져오기
//function getSvrList() {
//	var data = new Object();
//	data = {
//		userId 	: userId,
//		SysCd 	: getSelectedVal('cboSys').value,
//		SecuYn 	: 'Y',
//		SelMsg 	: 'ALL',
//		requestType	: 'getSvrList'
//	}
//	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSvrList);
//}
//
//// 서버 콤보 정보 가져오기 완료
//function successGetSvrList(data) {
//	cboSvrData = data;
//	$('[data-ax5select="cboSvr"]').ax5select({
//        options: injectCboDataToArr(cboSvrData, 'cboId' , 'svrinfo')
//	});
//}

// 수기파일대사 삭제
function delHandrunDiff() {
	
	var selIn = handGrid.selectedDataIndexs;
	var dirList = [];
	if(selIn.length === 0 ) {
		dialog.alert('삭제 할 대상을 선택 한 후 진행하시기 바랍니다.', function() {});
		return;
	}
	
	selIn.forEach(function(selIndex, index) {
		dirList.push(handGrid.list[selIndex]);
	});
	
	var data = new Object();
	data = {
		UserId 		: userId,
		dirList 	: dirList,
		requestType	: 'delHandrun_diff'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json', successDelHandrunDiff);
}

// 수기파일대사 삭제 완료
function successDelHandrunDiff(data) {
	if(data === 'OK') {
		dialog.alert('수기파일대사 기록을 삭제하였습니다.', function() {
			getHandrunDiff();
		});
	} else {
		dialog.alert('수기파일대사 기록 삭제 중 오류가 발생하였습니다.', function() {});
	}
}

// 수기파일대사 등록
function setHandrunDiff() {
	var etcData = new Object();
	
	var txtDeploy = replaceAllString($('#txtDeploy').val().trim(),':','');
	var dateDeploy = replaceAllString($('#dateDeploy').val().trim(),'/','');
	txtDeploy = txtDeploy.length === 3 ? '0' + txtDeploy : txtDeploy;
	
	if(!$('#chkTime').is(':checked')) {
		if(dateDeploy.length === 0 ) {
			dialog.alert('작업일시를 선택하여 주시기 바랍니다.', function() {});
			return;
		}
		if(txtDeploy.length === 0 ) {
			dialog.alert('작업시간을 선택하여 주시기 바랍니다.', function() {});
			return;
		}
		etcData.diffday = dateDeploy + txtDeploy;
	} else {
		etcData.diffday = '';
	}
	
	if($("#txtSvr").val().trim().length == 0){
		etcData.svrip = 'ALL';
	} else {
		etcData.svrip = $("#txtSvr").val().trim();
	}
	
//	if(getSelectedVal('cboSvr').value === 'ALL') {
//		etcData.svrip = 'ALL';
//	} else {
//		etcData.svrip = getSelectedVal('cboSvr').cm_svrip;
//	}
	
	
	if(getSelectedVal('cboSys').value === '00000') {
		etcData.syscd = 'ALL';
	} else {
		etcData.syscd = getSelectedVal('cboSys').value;
	}
	
	var data = new Object();
	data = {
		UserId 		: userId,
		gbnCd 		: 'I',
		etcData		: etcData,
		requestType	: 'setHandrun_diff'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json', successSetHandrunDiff);
}

// 수기파일대사 등록 완료
function successSetHandrunDiff(data) {
	if(data === 'OK') {
		dialog.alert('수기파일대사를 등록하였습니다.', function() {
			getHandrunDiff();
		});
	} else {
		dialog.alert('수기파일대사 등록 중 오류가 발생하였습니다.', function() {});
	}
}


// 수기파일대사 리스트 가져오기
function getHandrunDiff() {
	var data = new Object();
	data = {
		UserId 		: userId,
		requestType	: 'getHandrun_diff'
	}
	ajaxAsync('/webPage/ecmm/Cmm2900Servlet', data, 'json', successGetHandrunDiff);
}
// 파일대사 기본정보 가져오기 완료
function successGetHandrunDiff(data) {
	handGridData = data;
//	console.log('data:',data);
	handGrid.setData(handGridData);
//	console.log(handGrid);
}

// 시스템 리스트 가져오기
function getSysInfo() {
	var data = new Object();
	data = {
		UserId : userId,
		SecuYn : "N",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

// 시스템 리스트 가져오기 완료
function successGetSysInfo(data) {
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
}

