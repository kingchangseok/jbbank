/**
 * 환경설정 화면 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-06-21
 */

//var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;

var adminYN 	= window.parent.adminYN;		// 관리자
var jobModal 	= new ax5.ui.modal();
var jobModal2 	= new ax5.ui.modal();

var urlArr 			 = [];
var cboPassCycleData = [];
var cboBackUpData	 = [];

var grid1Sw = false;
var grid2Sw = false;
var grid3Sw = false;
var grid4Sw = false;
var tmpTab = null;

$('[data-ax5select="cboPassCycle"]').ax5select({
    options: []
});
$('input.checkbox-dir').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$('input:radio[name^="stopRadio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	$('#tab1Li').width($('#tab1Li').width()+10);
	$('#tab2Li').width($('#tab2Li').width()+10);
	$('#tab3Li').width($('#tab3Li').width()+10);
	$('#tab4Li').width($('#tab4Li').width()+10);
	setTabMenu();
	
	getCodeInfo();
	getAgentInfo();
	
	// 환경설정 등록
	$('#btnReq').bind('click', function() {
		checkConfigVal();
	});
});

// 환경 설정 등록시 유효성 검사 
function checkConfigVal() {
	var txtIpIn 		= $('#txtIpIn').val().trim();
	var txtPassCycle 	= $('#txtPassCycle').val().trim();
	var txtInitPass 	= $('#txtInitPass').val().trim();
	var txtIpOut 		= $('#txtIpOut').val().trim();
	var txtPassLimit 	= $('#txtPassLimit').val().trim();
	var txtAdminPass 	= $('#txtAdminPass').val().trim();
	var txtPort 		= $('#txtPort').val().trim();
	var txtProcTot 		= $('#txtProcTot').val().trim();
	var txtUrl	 		= $('#txtURL').val().trim();
	
	if(txtIpIn.length === 0 ) {
		dialog.alert('서버 IP Address(내부망)를 입력하여 주십시오.', function() {});
		return;
	}

	if(txtIpOut.length === 0 ) {
		dialog.alert('서버 IP Address(외부망)를 입력하여 주십시오.', function() {});
		return;
	}

	if(txtPort.length === 0 ) {
		dialog.alert('서버 Port 를 입력하여 주십시오.', function() {});
		return;
	}
	
	if(txtUrl.length === 0 ) {
		dialog.alert('URL을 입력하여 주십시오.', function() {});
		return;
	}
	
	if(txtPassCycle.length === 0) {
		dialog.alert('변경주기를 입력하여 주십시오.', function() {});
		return;
	}
	
	if(txtPassCycle === '0') {
		dialog.alert('변경주기를 0 이외의 숫자로 입력하여 주십시오.', function() {});
		return;
	}
	
	if(getSelectedIndex('cboPassCycle') < 1) {
		dialog.alert('변경주기구분을 선택하여 주십시오.', function() {});
		return;
	}
	
	if (txtInitPass.length === 0){
		dialog.alert('초기비밀번호를 입력하여 주십시오', function() {});
		return;
	}

	if (txtAdminPass.length === 0){
		dialog.alert('관리자비밀번호를 입력하여 주십시오.', function() {});
		return;
	}
	
	if (txtPassLimit.length === 0){
		dialog.alert('비밀번호입력제한횟수를 입력하여 주십시오.', function() {});
		return;
	}
	
	if(txtPassLimit === '0') {
		dialog.alert('비밀번호입력제한횟수를 0 이외의 숫자로 입력하여 주십시오.', function() {});
		return;
	}
	
	if (txtProcTot.length === 0){
		dialog.alert('프로세스 총갯수를 숫자로 정확하게 입력하여 주십시오.', function() {});
		return;
	}
	
	if(txtProcTot === '0') {
		dialog.alert('프로세스 총갯수를 0 이외의 숫자로 입력하여 주십시오.', function() {});
		return;
	}
	
	if(getSelectedIndex('cboBackUp') < 1) {
		dialog.alert('소스저장위치를 선택하여 주십시오.', function() {});
		return;
	}

	var objData				= new Object();
	objData.cm_ipaddr 		= txtIpIn;
	objData.cm_ipaddr2 		= txtIpOut;
	objData.cm_port 		= txtPort;
	objData.cm_passwd 		= txtAdminPass;
	objData.cm_initpwd 		= txtInitPass;
	objData.cm_pwdterm 		= txtPassCycle;
	objData.cm_pwdcnt  		= txtPassLimit;
	objData.cm_proctot 		= txtProcTot;
	objData.cm_pwdcd  		= getSelectedVal('cboPassCycle').value;
	objData.cm_srcloc  		= getSelectedVal('cboBackUp').value;
	objData.cm_url 			= txtUrl;
	if($("#chkMGR").is(":checked")){
		objData.cm_mgrlog       = "Y";
	} else {
		objData.cm_mgrlog       = "N";
	}
	
	if($("#chkEFF").is(":checked")){
		objData.cm_effskip      = "Y";
	} else {
		objData.cm_effskip      = "N";
	}
	
	var data = new Object();
	data = {
		objData 	: objData,
		requestType	: 'setAgentInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successSetAgentInfo);
	data = null;
	objData = null;
}

// 환경설증 등록 완료
function successSetAgentInfo(data) {
	if(data.retval !== '0') {
		dialog.alert(data.retmsg, function(){});
	} else {
		dialog.alert('환경설정 등록이 완료 되었습니다.', function(){
			getAgentInfo();
		});
	}
}

// 환경설정 정보 가져오기
function getAgentInfo() {
	var data = new Object();
	data = {
		requestType	: 'getAgentInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetAgentInfo);
	data = null;
}

// 환경설정 정보 가져오기 완료
function successGetAgentInfo(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	$('#txtIpIn').val(data.cm_ipaddr);
	$('#txtIpOut').val(data.cm_ipaddr2);
	$('#txtPort').val(data.cm_port);
	$('#txtURL').val(data.cm_url);
	$('#txtAdminPass').val(data.cm_passwd);
	$('#txtInitPass').val(data.cm_initpwd);
	$('#txtPassCycle').val(data.cm_pwdterm);
	$('#txtPassLimit').val(data.cm_pwdcnt);
	$('#txtProcTot').val(data.cm_proctot);
	if(data.cm_mgrlog == "Y"){
		$('#chkMGR').wCheck('check',true);
	} else {
		$('#chkMGR').wCheck('check',false);
	}
	
	if(data.cm_effskip == "Y"){
		$('#chkEFF').wCheck('check',true);
	} else {
		$('#chkEFF').wCheck('check',false);
	}
	

	
	if(data.hasOwnProperty('cm_pwdcd')) {
		$('[data-ax5select="cboPassCycle"]').ax5select('setValue', data.cm_pwdcd, true);
	}
	
	if(data.hasOwnProperty('cm_srcloc')) {
		$('[data-ax5select="cboBackUp"]').ax5select('setValue', data.cm_srcloc, true);
	}
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('DBTERM','SEL','N'),
		new CodeInfo('SRCLOC','SEL','N'),
	]);
	cboPassCycleData = codeInfos.DBTERM;
	cboBackUpData = codeInfos.SRCLOC;

	$('[data-ax5select="cboPassCycle"]').ax5select({
        options: injectCboDataToArr(cboPassCycleData, 'cm_micode' , 'cm_codename')
	});
	
	$('[data-ax5select="cboBackUp"]').ax5select({
        options: injectCboDataToArr(cboBackUpData, 'cm_micode' , 'cm_codename')
	});
};

// 탭 메뉴 셋
function setTabMenu(){
	$(".tab_content:first").show();
	
	$("ul.tabs li").click(function () {
		$(".tab_content").css('display','none');
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		$('#'+activeTab).css('display','block');
		
		if(this.id === 'tab2Li') {
			if (!grid1Sw) {
				$("#frame2").attr('src','/webPage/tab/configuration/DelCriteriaManageTab.jsp');
				grid1Sw = true;
			}
		} else if(this.id === 'tab3Li') {
			if (!grid2Sw) {
				$("#frame3").attr('src','/webPage/tab/configuration/DirectoryPolicyTab.jsp');
				grid2Sw = true;
			}
		} else if(this.id === 'tab4Li') {
			if (!grid3Sw) {
				$("#frame4").attr('src','/webPage/tab/configuration/JobServerInfoTab.jsp');
				grid3Sw = true;
			}
		}

		window.top.resize();
		
		$('#'+activeTab).find("iframe").bind("load",function(){
			window.top.resize();
		});
	});
}

function fnRsaEnc(value, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
    var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

    var encValue = rsa.encrypt(value);     // 비밀번호를 RSA로 암호화한다.
    return encValue;
}