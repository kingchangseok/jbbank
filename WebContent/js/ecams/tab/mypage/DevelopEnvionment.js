/**
 * [기본관리/사용자환경설정] 개발환경설정
 */
var userId 		= window.top.userId;
var userName 	= window.top.userName;
var codeList    = window.top.codeList;  //전체 코드 리스트
var adminYN 	= window.top.adminYN;	//'N' 관리자여부

var SecuYn 		= '';	
var strUserIp 	= '';	
//var SysCd		= '';

var mainGrid			= new ax5.ui.grid();
var gridSelectedIndex	= '';
var selectedGridItem	= [];

var options 	= [];
var sysData 	= [];
var mainGridData= [];
var tmpInfo 	= {};

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : true,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            grid_Click();
        },
	},
	columns : [
        {key: "NO", 		label: "순서",				width: '10%',  align: 'left'},
        {key: "cm_sysmsg", 	label: "시스템",				width: '20%',  align: 'left'},
        {key: "cd_devhome", label: "개발 Home Directory",	width: '70%',  align: 'left'}
	]
}); 


$(document).ready(function() {
	
	screenInit();

	//사용자 정보
	getUserInfo(userId);

	//시스템
	$("#cboSys").bind("change", function(){
		cboSys_Change();	
	});
	
	//등록
	$("#btnReg").bind("click", function(){
		btnReg_Click();	
	});
	
	//조회
	$("#btnQry").bind("click", function(){
		btnQry_Click();	
	});

	//삭제
	$("#btnDel").bind("click", function(){
		btnDel_Click();	
	});
	
	//엑셀
	$("#btnExcel").bind('click', function() {
		mainGrid.exportExcel("개인별변경추이.xls");
	});
	
});

function screenInit(){
	
	$('#btnReg').prop('disabled', true);
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: []
	});
	
	btnQry_Click();	
}

function getUserInfo(userId){
	
	tmpInfo = {
		userId	: userId,
		userName: userName,
		requestType	: 'getUserInfo'		
	}
	
	var userData = ajaxCallWithJson('/webPage/common/CommonUserInfo', tmpInfo, 'json');

	if(userData.length == 0){
		dialog.alert("사용자정보가 없습니다. 관리자에게 문의 후 사용해주십시오.");
		return;
	} else if(userData[0].cm_ipaddress == null || userData[0].cm_ipaddress == ''){
		dialog.alert("사용자 IP정보가 없습니다. 관리자에게 문의 후 사용해주십시오.");
		return;
	}
	SecuYn = 'N';
	if(userData[0].cm_admin == '1') SecuYn = 'Y';
	
	strUserIp = userData[0].cm_ipaddress;
	getSysInfo(SecuYn);
	
}

function getSysInfo(SecuYn){
	
	tmpInfo = {
		UserId	: userId,
		SecuYn	: SecuYn,
		SelMsg	: 'SEL',
		CloseYn	: 'N',
		ReqCd	: ''
	}
	
	var data = {
		sysData	: tmpInfo,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/CommonSysInfo', data, 'json', SuccessGetCboSys);
}

function SuccessGetCboSys(data){
	
	options = [];

	sysData = data.filter(function(data) {
		if(data.cm_sysinfo.substr(14,1) == '1') return false;
		if (data.localyn == 'A' || data.localyn == 'L' || data.localyn == 'S' || data.cm_syscd == '00000') return true;
		else return false;
	});	

	$.each(sysData, function(i, item) {
		options.push({value : item.cm_syscd, text : item.cm_sysmsg});
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: options
	});
	
	var selectVal = '';
	
//    if (SysCd != null && SysCd != '') {    //eCmd1300 line:163
//    	for (var i = 0; sysData.length > i; i++) {
//    		if (getSelectedVal('cboSys').value == SysCd) {
//    			selectVal = $('select[id=cboSys] option:eq(i)').val();
//    			$('[data-ax5select="cboSys"]').ax5select('setValue',selectVal,true);
//    			cboSys_Change();
//    			break;
//    		}
//    	}
//    }
	
	if(getSelectedIndex('cboSys') < 1 && sysData.length == 2){
		selectVal = $('select[id=cboSys] option:eq(1)').val();
		$('[data-ax5select="cboSys"]').ax5select('setValue',selectVal,true);
		cboSys_Change();
	}
	
}

function cboSys_Change(){
	
	$('#btnReg').prop('disabled',true);
	$('#txtDir').val('');
	
	if (getSelectedIndex('cboSys') < 1) return;
	$('#btnReg').prop('disabled',false);
	
	for (var i = 0; mainGridData.length > i; i++) {
		if (mainGridData[i].cd_syscd == getSelectedVal('cboSys').value) { 
			var dirText = selectedGridItem.cd_devhome;
			$('#txtDir').val(dirText);
			break;					
		}
	}
}

function btnQry_Click(){
	
	tmpInfo = {
		userId 		: userId,
		requestType	: 'getUserConfigList'
	}
	
	mainGridData = ajaxCallWithJson('/webPage/mypage/UserConfig', tmpInfo, 'json');
	console.log("mainGridData= ",mainGridData);
	
	if(getSelectedIndex('cboSys') > 0){
		if(mainGridData.length == 0){
			mainGrid.setData([]);
		} else{
			for (var i = 0; mainGridData.length > i; i++) {
				if (getSelectedVal('cboSys').value == mainGridData[i].cd_syscd) {
					mainGrid.setData([mainGridData[i]]);
				} else{
					mainGrid.setData([]);
				}
			}
		}
	} else{
		mainGrid.setData([]);
	}
	
	
}

function grid_Click() {
	
	gridSelectedIndex = mainGrid.selectedDataIndexs;
	selectedGridItem = mainGrid.list[mainGrid.selectedDataIndexs];
	
	if (gridSelectedIndex < 0) return;
	
	$('#txtDir').val('');
	
	for (var i = 0; sysData.length > i; i++) {
		if (sysData[i].cm_syscd == selectedGridItem.cd_syscd) {
			selectVal = sysData[i].cm_syscd;
			$('[data-ax5select="cboSys"]').ax5select('setValue',selectVal,true);
			var dirText = selectedGridItem.cd_devhome;
			$('#txtDir').val(dirText);
			break;					
		}
	}
}

function btnReg_Click(){
	
    if (getSelectedIndex('cboSys') < 1){
        dialog.alert("시스템을 선택하여 주십시오.");
        return;
    }else if ($('#txtDir').val().trim() == ""){
        dialog.alert("폴더를 입력하여 주십시오.");
        return;
    }else{
        tblUpdate();
    }
}

function tblUpdate(){
	
	tmpInfo = {
		UserId 		: userId,
		SysCd 		: getSelectedVal('cboSys').value,
		Lbl_Dir 	: $('#txtDir').val().trim(),
		requestType	: 'getTblUpdate'
	}
	
	result = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');
	
	if (result > 0) dialog.alert("Home Directory가 등록되었습니다.");
	btnQry_Click();
	
	
}

function btnDel_Click(){
	if (selectedGridItem == 0){
		dialog.alert("삭제할 항목이 없습니다.");
		return;
	} else{
		tblDelete();
	}
}

function tblDelete(){
	
	tmpInfo = {
			UserId 		: userId,
			rvList 		: [selectedGridItem],
			requestType	: 'getTblDelete'
		}
		
	result = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');
		
	if (result > 0) dialog.alert("선택한 Home Directory가 삭제되었습니다.");
	selectedGridItem = [];
	btnQry_Click();
}










