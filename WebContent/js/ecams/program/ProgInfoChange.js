var userName 	    = window.top.userName;
var userId 			= window.top.userId;
var adminYN		    = window.top.adminYN;
var userDeptName    = window.top.userDeptName;
var userDeptCd 	    = window.top.userDeptCd;
var reqCd 			= window.top.reqCd;
var rgtList         = window.top.rgtList;

//grid 생성
var firstGrid 	 	= new ax5.ui.grid();
var userModal 		= new ax5.ui.modal();
var popSelItem		= null;

var firstGridData 	= [];
var sysData 	  	= [];
var RsrccdData      = [];
var cboTeamData		= [];
var cboCompileModeData = [];
var cboRsrcTypeData    = [];

var gridSelectedIndex;
var selectedGridItem;

var chk1  			= "T";
var chk2  			= "T";
var chk3  			= "F";
var chk4  			= "T";
var chk5  			= "F";

$('[data-ax5select="cboJob"]').ax5select({options: []});
$('[data-ax5select="cboRsrcType"]').ax5select({options: []});
$('[data-ax5select="cboTeam"]').ax5select({options: []});
$('[data-ax5select="cboCompileMode"]').ax5select({options: []});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: true, 
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	//this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: 'rsrccd', 		label: '프로그램종류',	width: '6%'},
        {key: 'cr_rsrcname',	label: '프로그램명',  	width: '12%',  	align: 'left'},
        {key: 'cr_story', 		label: '프로그램설명',	width: '12%',	align: 'left'},
        {key: 'cm_dirpath', 	label: '프로그램경로',	width: '25%', 	align: 'left'},
        {key: 'teamN', 			label: '관리팀',  	width: '6%', 	align: 'left'},
        {key: 'masname', 		label: '관리담당자',  	width: '6%'},
        {key: 'compN', 			label: '컴파일모드',  	width: '6%'},
        {key: 'sta', 			label: '상태',  		width: '6%'},
        {key: 'job', 			label: '업무',  		width: '6%'},
        {key: 'cm_username', 	label: '최종변경자',  	width: '6%'},
        {key: 'cr_lastdate', 	label: '최종변경일',  	width: '6%'},
        {key: 'cr_lstver', 		label: '버전',  		width: '6%'},
    ]
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getSysCbo();
	screenInit();
	getCodeInfo();
	
	$('#cboSys').bind('change',function(){
		changeSys();
		changeSys2();
		getJobInfo(getSelectedVal("cboSys").value);
//		$("#chkJob").trigger('click'); 
//		$("#chkCompileMode").trigger('click'); 
		screenInit();
	});
	//조회
	$('#btnSearch').bind('click',function(){
		findProc();
	});
	//수정
	$('#btnUpdate').bind('click',function(){
		updateProc();
	});
	
	$('#txtRsrcName').bind('keypress',function(event){
		if(event.keyCode == 13){
			findProc();
		}
	});
	
	$('#btnExl').bind('click', function() {
		firstGrid.exportExcel("ProgramInfoChange.xls");
	});
	
	$('#chkJob').bind('click',function(){
		if(chk1 == "T"){
			$('#chkJob').wCheck('check', false);
			$('[data-ax5select="cboJob"]').ax5select("disable");
			chk1 = "F";
		}else{
			$('#chkJob').wCheck('check', true);
			$('[data-ax5select="cboJob"]').ax5select("enable");
			chk1 = "T";
		}
	});
	
	$('#chkAbout').bind('click',function(){
		if($("#txtAbout").is(":disabled")){
			$('#txtAbout').prop('disabled',false);
		}else{
			$('#txtAbout').prop('disabled',true);
		}
	});
	
	$('#chkRsrcType').bind('click',function(){
		if(chk2 == "T"){
			$('#chkRsrcType').wCheck('check',false);
			$('[data-ax5select="cboRsrcType"]').ax5select("disable");
			chk2 = "F";
		}else{
			$('#chkRsrcType').wCheck('check',true);
			$('[data-ax5select="cboRsrcType"]').ax5select("enable");
			chk2 = "T";
		}
	});
	
	$('#chkTeam').bind('click',function(){
		if(chk3 == "T"){
			$('[data-ax5select="cboTeam"]').ax5select("disable");
			chk3 = "F";
		}else{
			$('[data-ax5select="cboTeam"]').ax5select("enable");
			chk3 = "T";
		}
	});
	
	$('#chkCompileMode').bind('click',function(){
		if(chk4 == "T"){
			$('#chkCompileMode').wCheck('check',false);
			$('[data-ax5select="cboCompileMode"]').ax5select("disable");
			chk4 = "F";
		}else{
			$('#chkCompileMode').wCheck('check', true);
			$('[data-ax5select="cboCompileMode"]').ax5select("enable");
			chk4 = "T";
		}
	});
	
	$('#chkCaller').bind('click',function(){
		if(chk5 == "T"){
			$('#chkCaller').wCheck('check',false);
			$('#txtEditor').prop('disabled', true);
			chk5 = "F";
		}else{
			$('#chkCaller').wCheck('check',true);
			$('#txtEditor').prop('disabled', false);
			chk5 = "T";
		}
	});
	//관리담당자
	$('#txtEditor').bind('click',function() {
		txtEditor_Click();
	});
});

function screenInit() {
	$('[data-ax5select="cboJob"]').ax5select("disable");
	$('[data-ax5select="cboRsrcType"]').ax5select("disable");
	$('[data-ax5select="cboCompileMode"]').ax5select("disable");
	$('[data-ax5select="cboTeam"]').ax5select("disable");
	
	$('[data-ax5select="cboJob"]').ax5select('setValue','0000',true);
 	$('[data-ax5select="cboRsrcType"]').ax5select('setValue','00',true);
 	$('[data-ax5select="cboCompileMode"]').ax5select('setValue','00',true);
 	
	$('#chkJob').wCheck('check', false);
	$('#chkAbout').wCheck('check', false);
	$('#chkRsrcType').wCheck('check', false);
	$('#chkTeam').wCheck('check', false);
	$('#chkCompileMode').wCheck('check', false);
	$('#chkCaller').wCheck('check', false);
	
	$('#txtAbout').val('');
	$('#txtEditor').val('');
	$('#txtAbout').prop('disabled', true);
	$('#txtEditor').prop('disabled', true);
	$('#btnExl').prop('disabled',true);
}

function updateProc(){
	
	if(!$('#chkRsrcType').is(':checked') && !$('#chkJob').is(':checked') && !$('#chkAbout').is(':checked') && !$('#chkCompileMode').is(':checked') && !$('#chkCaller').is(':checked') && !$('#chkTeam').is(':checked')){
		dialog.alert("수정 할 내용을 체크해주십시오.");
		return;
	}
	
	if($('#chkJob').is(':checked') && getSelectedIndex('cboJob') == 0){
		dialog.alert("업무를 선택하여 주십시오.");
		return;
	}
	
	if($('#cboCompileMode').is(':checked') && getSelectedIndex('cboCompileMode') == 0){
		dialog.alert("컴파일 모드를 선택하여 주십시오.");
		return;
	}
	if($('#cboRsrcType').is(':checked') && getSelectedIndex('cboRsrcType') == 0){
		dialog.alert("프로그램구분을 선택하여 주십시오.");
		return;
	}
	if($('#chkAbout').is(':checked') && $('#txtAbout').val().trim() == ""){
		dialog.alert("프로그램설명을 입력하여 주십시오.");
		return;
	}
	if($('#chkCaller').is(':checked') && $('#txtEditor').val().trim() == ""){
		dialog.alert("관리담당자를 입력하여 주십시오.");
		return;
	}
	
	firstGridSel = firstGrid.getList("selected");
	var i = 0;
	var chkList = true;
	for (i=0 ; firstGridData.length>i ; i++) {
		if (firstGridSel > -1) {
			chkList = false;
			break;
		}
	}
	if (!chkList){
		dialog.alert("선택된 목록이 없습니다. \n목록에서 수정 할 대상을 체크하여 주십시오.");
		return;
	}
	
	var firstGridSelected = [];
	var tmpObj = new Object();
	var tmpArr = new Array();
	
	for (i=0;firstGridSel.length>i;i++) {				
		firstGridSelected.push(firstGridSel[i]);
	}
	for (i=0;firstGridSelected.length>i;i++) {
		tmpObj = new Object();
		tmpObj.cr_syscd = firstGridSelected[i].cr_syscd,
		tmpObj.cr_itemid = firstGridSelected[i].cr_itemid;
		
		tmpObj.cr_rsrccd = "";
		if($('#chkRsrcType').is(':checked')){
			tmpObj.cr_rsrccd = getSelectedVal('cboRsrcType').cm_micode;
		}
		
		tmpObj.cr_story = "";
		if($('#chkAbout').is(':checked')){
			tmpObj.cr_story = $('#txtAbout').val();
		}
		
		tmpObj.cr_jobcd = "";
		if($('#chkJob').is(':checked')){
			tmpObj.cr_jobcd = getSelectedVal('cboJob').value;
		}
		
		tmpObj.cr_compile = "";
		if($('#chkCompileMode').is(':checked')){
			tmpObj.cr_compile = getSelectedVal('cboCompileMode').value;
		}
		
		tmpObj.cr_team = "";
		if($('#chkTeam').is(':checked')){
			tmpObj.cr_team = getSelectedVal('cboTeam').cm_micode;
		}
		
		tmpObj.cr_master = "";
		if($('#chkCaller').is(':checked')){
			tmpObj.cr_master = popSelItem.cm_userid;
		}
		
		tmpArr.push(tmpObj);
		tmpObj = null;
	}
	tmpData = new Object();
	tmpData = {
		UserId : userId,
		dataList : tmpArr,
		requestType : 'setRsrcInfo'
	}	
	ajaxAsync('/webPage/ecmd/Cmd0600Servlet', tmpData, 'json',successUpdateProc);
}	
function successUpdateProc(data) {
	console.log("HSHSHSS",data)
	if(data == "0") {
		dialog.alert('수정이 완료되었습니다.',function(){});
		$('#btnSearch').trigger('click');
		screenInit();
	}else { 
		dialog.alert('수정이 실패되었습니다.',function(){});
	}
}
//SysInfo.getSysInfo(strUserId,"y","","n",strReqCD);
function getSysCbo(){
	var tmpData = {
		UserId : userId,
		SecuYn : 'Y',
		SelMsg : 'SEL',
		CloseYn : 'N',
		ReqCd : '01',
		requestType : 'getSysInfo'
	}	
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpData, 'json',successGetSysCbo);
}

function successGetSysCbo(data){
	sysData = data;
	sysData = sysData.filter(function(item) {
		if(item.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	})
	
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(sysData, 'cm_syscd' , 'cm_sysmsg')
	});
}

//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(sysCd) {
	var data = new Object();
	data = {
		UserID		: userId,
		SysCd		: sysCd,
		SecuYn		: adminYN ? 'N' : 'Y',
		CloseYn		: 'Y',
		SelMsg		: 'SEL',
		sortCd		: 'NAME',
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successJob);
	data = null;
}

function successJob(data) {
	cboJobData = data;	
	if (cboJobData != null && (cboJobData.length > 0)) {
		options = [];
		$.each(cboJobData,function(key,value) {
			options.push({value: value.cm_jobcd, text: value.cm_jobname});
		});
		
		$('[data-ax5select="cboJob"]').ax5select({
	        options: options
		});
	}
}
function changeSys(){
	firstGrid.setData([]);
	firstGridaData = [];
	
	if(getSelectedIndex('cboSys') < 0) return;
	
	var tmpData = {
		Syscd : getSelectedVal('cboSys').cm_syscd,
		SelMsg : "SEL",
		requestType : 'getRsrcInfo'
	}
	ajaxAsync('/webPage/ecmd/Cmd0600Servlet', tmpData, 'json',successChangeSys);
}

function successChangeSys(data){
	RsrccdData = data;
	$('[data-ax5select="cboRsrccd"]').ax5select({
        options: injectCboDataToArr(data, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboRsrcType"]').ax5select({
        options: injectCboDataToArr(data, 'cm_micode' , 'cm_codename')
	});
}
function changeSys2(){
	firstGrid.setData([]);
	firstGridaData = [];
	
	if(getSelectedIndex('cboSys') < 0) return;
	
	var tmpData = {
		Syscd : getSelectedVal('cboSys').cm_syscd,
		SelMsg : "SEL",
		requestType : 'getCompile'
	}
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpData, 'json',successChangeSys2);
}

function successChangeSys2(data){
	cboCompileModeData = data;
	$('[data-ax5select="cboCompileMode"]').ax5select({
        options: injectCboDataToArr(cboCompileModeData,'cm_gubun' , 'cm_compscript')
	});
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('ETCTEAM','SEL','N','3',''),
	]);
	cboTeamData	= codeInfos.ETCTEAM;
	
	$('[data-ax5select="cboTeam"]').ax5select({
        options: injectCboDataToArr(cboTeamData, 'cm_micode' , 'cm_codename')
	});
};

function findProc() {
	firstGrid.setData([]);
	firstGridData = [];
	
	if (getSelectedIndex('cboSys') < 0){
		dialog.alert("시스템 정보가 없습니다. 확인 후 다시 조회해주세요");
		return;
	}
	if(getSelectedIndex('cboRsrccd') < 1) {
		dialog.alert('프로그램종류를 선택하여 주십시오.',function(){});
		$('#cboRsrccd').focus();
		return;
	}
	var etcData = new Object();
	etcData.UserId = userId;
	etcData.SecuYn = adminYN = "true" ? "Y" : "N";
	etcData.cboSys  = getSelectedVal('cboSys').cm_syscd;
	etcData.cboRsrccd  = RsrccdData.length > 0 ? getSelectedVal('cboRsrccd').cm_micode : "";
	etcData.txtRsrcName = $('#txtRsrcName').val().trim();
	
	var tmpData = {
		etcData : etcData,
		requestType : 'getSqlQry'
	}	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0600Servlet', tmpData, 'json',successFindProc);
}

function successFindProc(data){
	console.log('successFindProc ==>', data);
	$(".loding-div").remove();
	
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	
	firstGridData = data;
	
	firstGridData.forEach(function(item) {
		item.cr_lstver = Number(item.cr_lstver);
	});
	
	$(firstGridData).each(function(i){
		if(firstGridData[i].cr_status == '0' || firstGridData[i].cr_status == '3'){
			firstGridData[i].filterData = false;
			firstGridData[i].__disable_selection__ = '';
		}else{
			firstGridData[i].filterData = true;
			firstGridData[i].__disable_selection__ = true;
		}
	});
	
	firstGrid.setData(firstGridData);
	firstGrid.repaint();
	
	if (data.length < 1){
		dialog.alert("검색결과가 없습니다.");
		$('#btnExl').prop('disabled',true);
	}else{
		$('#btnExl').prop('disabled',false);
	}
}
function txtEditor_Click() {
	setTimeout(function() {
		userModal.open({
			width: 500,
			height: 375,
			iframe: {
				method: "get",
				url: "../modal/userinfo/UserSelectModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
					if(popSelItem != null) {
						$("#txtEditor").val(popSelItem.cm_username);
						//$("#txtEditor").toolTip(popSelItem.cm_userid);
					}
				}
			}
		}, function () {
			
		});
	}, 200);
}