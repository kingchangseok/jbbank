/**
 * 시스템속성일괄등록 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2022-06-14
 * 
 */
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부

var datStDate 		= new ax5.ui.picker();
var datEdDate 		= new ax5.ui.picker();

var firstGrid 		= new ax5.ui.grid();
var firstGridData 	= [];
var cboSysInfoData	= [];

$('[data-ax5select="cboSysInfo"]').ax5select({
    options: []
});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: false, 
    multiSort: false,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
            this.self.clearSelect();
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "cm_syscd", 	label: "시스템코드",  	width: '17%'},
        {key: "cm_sysmsg", 	label: "시스템명",  	width: '33%', align: 'left'},
        {key: "stoptime", 	label: "일시정지", 	width: '25%'},
        {key: "systime", 	label: "적용시간", 	width: '25%'}
    ]
});

$('input:radio[name=infoChk]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

datStDate.bind(defaultPickerInfo('datStDate', 'bottom'));
datEdDate.bind(defaultPickerInfo('datEdDate', 'bottom'));

$("#hourTxt1").val("00");
$("#minTxt1").val("00");
$("#hourTxt2").val("00");
$("#minTxt2").val("00");

$(document).ready(function(){
	$('#optCheck').wRadio('check', true);
	
	getCodeInfo();
	getSysInfo();
	
	// 숫자만 입력
	$("#txtReleaseTime").on("keyup", function(event) {
		if(event.keyCode === 13) {
			$("#txtReleaseTime").focusout();
		} else {
			$(this).val($(this).val().replace(/[^0-9]/g,""));
		}
	});

	// 설정, 해제
	$('input:radio[name=infoChk]').bind('click', function() {
		infoChk_Click();		
	});
	
	// 시스템속성 변경
	$('#cboSysInfo').bind('change',function() {
		infoChk_Click();
	});
	
	// 조회
	$('#btnSearch').bind('click',function() {
		getSysInfo();
	});
	
	// 등록
	$('#btnReq').bind('click',function() {
		btnReq_Click();
	});
	
	// 닫기
	$('#btnClose').bind('click',function() {
		window.parent.sysInfoModal.close();
	});
});

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('SYSINFO','SEL','N'),
	]);
	cboSysInfoData = codeInfos.SYSINFO;
	
	$('[data-ax5select="cboSysInfo"]').ax5select({
		options: injectCboDataToArr(cboSysInfoData, 'cm_micode' , 'cm_codename')
	});
	
	infoChk_Click()
}

function getSysInfo() {
	//Cmm0200.getSysInfo_List(false,"");
	var sysListInfoData = new Object();
	sysListInfoData = {
		clsSw : false,
		SysCd : "",
		requestType	: 'getSysInfo_List'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', sysListInfoData, 'json',successGetSysInfoList);
}

function successGetSysInfoList(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function infoChk_Click() {
	$('#txtReleaseTime').prop('disabled', true);
	$('#datStDate').prop('disabled', true);
	disableCal(true, 'datStDate');
	$('#hourTxt1').prop('disabled', true);
	$('#minTxt1').prop('disabled', true);
	$('#datEdDate').prop('disabled', true);
	disableCal(true, 'datEdDate');
	$('#hourTxt2').prop('disabled', true);
	$('#minTxt2').prop('disabled', true);
	
	if($("input:radio[name=infoChk]:checked").val() == "optCheck"){
		if(getSelectedIndex('cboSysInfo') > 0) {
			if(getSelectedVal('cboSysInfo').cm_micode == '06') { //정기배포
				$('#txtReleaseTime').prop('disabled', false);
				$('#datStDate').val('');
				$('#hourTxt1').val('');
				$('#minTxt1').val('');
				$('#datEdDate').val('');
				$('#hourTxt2').val('');
				$('#minTxt2').val('');
			}else if(getSelectedVal('cboSysInfo').cm_micode == '05') { //일시정지
				$('#txtReleaseTime').val('');
				$('#datStDate').prop('disabled', false);
				disableCal(false, 'datStDate');
				$('#hourTxt1').prop('disabled', false);
				$('#minTxt1').prop('disabled', false);
				$('#datEdDate').prop('disabled', false);
				disableCal(false, 'datEdDate');
				$('#hourTxt2').prop('disabled', false);
				$('#minTxt2').prop('disabled', false);
			}
		}
	}else {
		if(getSelectedIndex('cboSysInfo') > 0) {
			if(getSelectedVal('cboSysInfo').cm_micode == '06') { //정기배포
				$('#txtReleaseTime').val('');
			}else if(getSelectedVal('cboSysInfo').cm_micode == '05') { //일시정지
				$('#datStDate').val('');
				$('#hourTxt1').val('');
				$('#minTxt1').val('');
				$('#datEdDate').val('');
				$('#hourTxt2').val('');
				$('#minTxt2').val('');
			}
		}
	}
}

function btnReq_Click() {
	if(getSelectedIndex('cboSysInfo') < 1) {
		dialog.alert('설정한 시스템속성을 선택하여 주시기 바랍니다.');
		return;
	}
	
	var txtStTime = $("#hourTxt1").val() + $("#minTxt1").val();
	var txtEdTime = $("#hourTxt2").val() + $("#minTxt2").val();
	
	if($("#optCheck").is(":checked")) {
		if(getSelectedVal('cboSysInfo').cm_micode == '05') { //일시정지
			if(txtStTime == '' || txtStTime == null
					|| $('#datStDate').val() == '' || $('#datStDate').val == null) {
				dialog.alert('중단시작일시를 입력하여 주시기 바랍니다.');
				return;
			}
			
			if(txtEdTime == '' || txtEdTime== null
					|| $('#datEdDate').val() == '' || $('#datEdDate').val == null) {
				dialog.alert('중단종료일시를 입력하여 주시기 바랍니다.');
				return;
			}
			
			if(txtStTime.length != 4) {
				dialog.alert('중단시작시간을 4자리 숫자로 입력하여 주시기 바랍니다.');
				return;
			}
			
			if(txtEdTime.length != 4) {
				dialog.alert('중단종료시간을 4자리 숫자로 입력하여 주시기 바랍니다.');
				return;
			}
			
			var today = getDate('DATE',0);
			var strNow = getTime();
			var stDate = replaceAllString($('#datStDate').val(),'/','');
			var edDate = replaceAllString($('#datEdDate').val(),'/','');
			if(today > stDate) {
				dialog.alert("중단시작일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
				return;
			}else if(today == stDate) {
				if (txtStTime < strNow) {
					dialog.alert("중단시작일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
					return;
				}
			}
			
			if(stDate > edDate) {
				dialog.alert("중단종료시작일시가 중단시작일시 이전입니다. 정확히 선택하여 주십시오.");
				return;
			}else if(stDate == edDate) {
				if (txtEdTime < txtStTime) {
					dialog.alert("중단종료시작일시가 중단시작일시 이전입니다. 정확히 선택하여 주십시오.");
					return;
				}
			}
			
		}else if(getSelectedVal('cboSysInfo').cm_micode == '06') { //정기배포
			if($('#txtReleaseTime').val().trim().length == 0) {
				dialog.alert("정기적용시간을 입력하여 주십시오.");
				return;
			}
			
			if($('#txtReleaseTime').val().trim().length != 4) {
				dialog.alert("정기적용시간을 4자리숫자로 입력하여 주십시오. (hhmm)");
				return;
			}
		}
	}
	
	var strSubInfo = '0';
	var strAftInfo = '0';
	var strInfo    = '';
	var checkedGridItem = firstGrid.getList("selected");
	
	if (checkedGridItem.length == 0) {
		dialog.alert('설정할 시스템을 선택하여 주시기 바랍니다.');
		return;
	}
	
	for(var i=0; i<checkedGridItem.length; i++) {
		checkedGridItem[i].cm_micode = getSelectedVal('cboSysInfo').cm_micode;
		if($("#optCheck").is(":checked")) {
			checkedGridItem[i].gbncd = '1';
			strSubInfo = '1';
			if(getSelectedVal('cboSysInfo').cm_micode == '05') {
				checkedGridItem[i].cm_sttime = stDate + txtStTime;
				checkedGridItem[i].cm_edtime = edDate + txtEdTime;
			}else if(getSelectedVal('cboSysInfo').cm_micode == '06') {
				checkedGridItem[i].cm_systime = $('#txtReleaseTime').val().trim();
			}
		}else {
			checkedGridItem[i].gbncd = '9';
			strSubInfo = '9';
		}
		
		strInfo = checkedGridItem[i].cm_sysinfo;
		if(getSelectedVal('cboSysInfo').cm_micode == '01') {
			strAftInfo = strSubInfo + strInfo.substr(1);
		}else if(strInfo.length <= Number(getSelectedVal('cboSysInfo').cm_micode)) {
			strAftInfo = strInfo.substr(0,Number(getSelectedVal('cboSysInfo').cm_micode) -1) + strSubInfo;
		}else {
			strAftInfo = strInfo.substr(0,Number(getSelectedVal('cboSysInfo').cm_micode) -1) + strSubInfo + strInfo.substr(Number(getSelectedVal('cboSysInfo').cm_micode));
		}
		
		checkedGridItem[i].cm_sysinfo = strAftInfo; 
	}

	var systemInfoDta = new Object(); 
	systemInfoDta = {
		sysList : checkedGridItem,
		requestType	: 'setSysInfo',
	}
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', systemInfoDta, 'json',successSetSysInfo);
}

function successSetSysInfo(data) {
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data);
	}else {
		dialog.alert('시스템속성에 대한 설정/해제 처리를 완료하였습니다.');
	}
	
	getSysInfo();
}

function popClose(){
	window.parent.sysInfoModal.close();
}