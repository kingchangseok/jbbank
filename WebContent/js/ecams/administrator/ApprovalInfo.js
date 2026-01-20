/**
 * [결재정보] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-25
 * 
 */

var userName 	= window.top.userName;
var userId 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;

var grdSign			= new ax5.ui.grid(); //결재정보 그리드

var selOptions 		= [];

var grdSignData 	= []; //그리드
var cboReqCdData 	= []; //결재종류
var cboSysData 		= []; //시스템
var cboCommonData	= []; //정상결재
var cboSignStepData	= []; //결재단계
var cboBlankData	= []; //부재처리
var cboComAftData	= []; //정상(업무후)
var cboEmgAftData	= []; //긴급(업무후)
var cboSgnGbnData	= []; //결재구분
var cboSysGbnData	= []; //자동처리
var cboEmgData		= []; //긴급(업무중)

var lstRgtData		= []; //직무 리스트 데이터
var lstGradeData	= []; //프로그램등급 리스트 데이터
var lstJawonData	= []; //프로그램종류 리스트 데이터

var treeObj			= [];
var treeDeptData	= []; //조직트리 데이터
var tmpDeptCd		= "";

var allApprovalInfoModal 	= new ax5.ui.modal(); //전체조회 팝업
var copyApprovalInfoModal	= new ax5.ui.modal(); //결재정보복사 팝업
var rangeApprovalInfoModal	= new ax5.ui.modal(); //대결범위등록 팝업

var tmpInfo     	= new Object(); 
var tmpInfoData 	= new Object(); 

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

grdSign.setConfig({
    target: $('[data-ax5grid="grdSign"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
	multipleSelect : false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            grdSign_Click();
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "manid", 		label: "구분",		width: '4%',  align: 'center'},
        {key: "cm_seqno", 	label: "순서",  		width: '4%',  align: 'center'},
        {key: "cm_name", 	label: "단계명칭",		width: '10%', align: 'left'},
        {key: "cm_codename",label: "결재구분",		width: '7%',  align: 'left'},
        {key: "common", 	label: "정상", 		width: '7%',  align: 'center'},
        {key: "blank", 		label: "부재",  		width: '7%',  align: 'center'},
        {key: "holi", 		label: "업무후", 		width: '7%',  align: 'center'},
        {key: "emg", 		label: "긴급",  		width: '7%',  align: 'center'},
        {key: "emg2", 		label: "긴급[후]",	width: '7%',  align: 'center'},
        {key: "deptcd", 	label: "자동처리",  	width: '8%', align: 'left'},
        {key: "rgtcd", 		label: "결재직무",  	width: '8%', align: 'left'},
        {key: "cm_orgstep", label: "삭제가능",  	width: '5%',  align: 'center'},
        {key: "rsrccd", 	label: "프로그램종류", 	width: '8%', align: 'left'},
        {key: "pgmtype", 	label: "프로그램속성", 	width: '8%', align: 'left'}
    ]
});

var treeSetting = {
	check: {
		enable: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: dept_Click
	}
};

$(document).ready(function(){
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	getCodeInfo();
	getSysInfo();
	getTeamInfo();
	
	$('#chkSys').wCheck("check", false);
	$('#chkAllRgt').wCheck("check", false);
	$('#chkAllGrade').wCheck("check", false);
	$('#chkAllJawon').wCheck("check", false);
	$('#chkJawon').wCheck("disabled", true);
	$('#chkAllJawon').wCheck("disabled", true);
	$('#txtDept').prop("readonly", true);
	
	$('#chkMember').wCheck("check", true);
	$('#chkOutsourcing').wCheck("check", true);
	
	//조직 toolTip
	$("#txtDept").mouseenter(function(){
		//console.log("in");
	});
	$("#txtDept").mouseleave(function(){
		//console.log("out");
	});
	
	//시스템 변경이벤트
	$("#cboSys").bind('change', function() {
		cboSys_Change();
	});
	
	//결재구분 변경이벤트
	$("#cboSgnGbn").bind('change', function() {
		cboSgnGbn_Change();
	});

	//프로그램등급 선택
//	document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
	$("#chkGrade").bind('click', function() {
		chkGrade_Click();
	});
	
	//프로그램종류 선택
//	document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
	$("#chkJawon").bind('click', function() {
		chkJawon_Click();
	});
	
	//시스템과관련없음
	$("#chkSys").bind('click', function() {
		chkSys_Click();
	});
	
	//직무전체선택
	$("#chkAllRgt").bind('click', function() {
		chkAllRgt_Click();
	});
	
	//프로그램등급 전체선택
	$("#chkAllGrade").bind('click', function() {
		chkAllGrade_Click();
	});
	
	//프로그램종류 전체선택
	$("#chkAllJawon").bind('click', function() {
		chkAllJawon_Click();
	});
	
	//결재정보복사
	$("#btnCopy").bind('click', function() {
		btnCopy_Click();
	});
	
	//대결범위등록
	$("#btnBlank").bind('click', function() {
		btnBlank_Click();
	});
	
	//조회
	$("#btnQry").bind('click', function() {
		btnQry_Click();
	});
	
	//전체조회
	$("#btnAllQry").bind('click', function() {
		btnAllQry_Click();
	});
	
	//등록
	$("#btnReq").bind('click', function() {
		btnReq_Click();
	});
	
	//폐기
	$("#btnDel").bind('click', function() {
		btnDel_Click();
	});
	
	$("#btnExl").bind("click", function(){
		grdSign.exportExcel("결재정보.xls");
	});
});

function getCodeInfo(){
	//CodeInfo.getCodeInfo("REQUEST,SGNGBN,SGNSTEP,SGNCD,SYSGBN,RGTCD,PGMTYPE","SEL","n");
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('REQUEST','SEL','N','3',''),
		new CodeInfoOrdercd('SGNGBN','SEL','N','3',''),
		new CodeInfo('SGNSTEP', 'SEL','N'),
		new CodeInfo('SGNCD'  , 'SEL','N'),
		new CodeInfo('SYSGBN' , 'SEL','N'),
		new CodeInfo('RGTCD'  , 'SEL','N'),
		new CodeInfoOrdercd('PGMTYPE','SEL','N','3','')
	]);
	
	cboReqCdData 		= codeInfos.REQUEST;
	cboSignStepData		= codeInfos.SGNSTEP;
	cboSgnGbnData		= codeInfos.SGNGBN;
	cboSysGbnData		= codeInfos.SYSGBN;
	lstRgtData			= codeInfos.RGTCD;
	lstGradeData		= codeInfos.PGMTYPE;
	
	cboCommonData		= codeInfos.SGNCD;
	cboBlankData		= codeInfos.SGNCD;
	cboComAftData		= codeInfos.SGNCD;
	cboEmgAftData		= codeInfos.SGNCD;
	cboEmgData			= codeInfos.SGNCD;

	$('[data-ax5select="cboReqCd"]').ax5select({
        options: injectCboDataToArr(cboReqCdData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboCommon"]').ax5select({
        options: injectCboDataToArr(cboCommonData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboSignStep"]').ax5select({
        options: injectCboDataToArr(cboSignStepData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboBlank"]').ax5select({
        options: injectCboDataToArr(cboBlankData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboComAft"]').ax5select({
        options: injectCboDataToArr(cboComAftData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboEmgAft"]').ax5select({
        options: injectCboDataToArr(cboEmgAftData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboSgnGbn"]').ax5select({
        options: injectCboDataToArr(cboSgnGbnData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboSysGbn"]').ax5select({
        options: injectCboDataToArr(cboSysGbnData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboSysGbn"]').ax5select('setValue',cboSysGbnData[0].cm_micode,true);
	$('[data-ax5select="cboSysGbn"]').ax5select("disable");
	
	$('[data-ax5select="cboEmg"]').ax5select({
        options: injectCboDataToArr(cboEmgData, 'cm_micode' , 'cm_codename')
   	});

	lstRgtData = lstRgtData.filter(function(data) {
		if( data.cm_micode != "00" && data.cm_micode != "****" ) return true;
		else return false;
	});
	//setLstRgt();
	
	lstGradeData = lstGradeData.filter(function(data) {
		if(data.cm_micode != "00") return true;
		else return false;
	});
	//setLstGrade();
	
	if(cboSgnGbnData.length > 0) {
		$('[data-ax5select="cboSgnGbn"]').ax5select('setValue',cboSgnGbnData[0].value,true); //value값으로
		cboSgnGbn_Change();
	}
}

function getSysInfo() {
	//SysInfo_cbo.getSysInfo_Rpt(strUserId,"SEL","N",""); 
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SelMsg : "SEL",
		CloseYn : "N",
		SysCd : "",
		requestType	: 'getSysInfo_Rpt'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSysInfo);
}

function successSysInfo(data) {
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
   	});
	
	cboSys_Change();
}

function getTeamInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		chkcd : 'false',
		itsw : 'false,',
		requestType	: 'getTeamInfoTree_zTree'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', tmpInfoData, 'json', successTeamInfo);
}

function successTeamInfo(data) {
	treeDeptData = data;
	$.fn.zTree.init($("#treeDept"), treeSetting, data); //초기화
	treeObj = $.fn.zTree.getZTreeObj("treeDept");
} 

/* 조직 트리 클릭 이벤트 */
function dept_Click(event, treeId, treeNode) {
	$('#txtDept').val(treeObj.getSelectedNodes()[0].name);
	//txtDeptName.toolTip = SelectedCd.toXMLString();
	tmpDeptCd = treeObj.getSelectedNodes()[0].id;
} 

//결재구분 변경 이벤트 (cboGubun_Click)
function cboSgnGbn_Change() {
	var i = 0;
	var findSw = false;
	
//	console.log("getSelectedIndex('cboSgnGbn') :" + getSelectedIndex('cboSgnGbn') );
//	if(getSelectedIndex('cboSgnGbn') <= 0) {
		$('#chkAllRgt').wCheck("disabled", true);
		$('#chkDel').wCheck('check',false);
//		document.getElementById("lstRgtDiv").style.backgroundColor = "#E2E2E2"; //lstRgtCd.enabled = false;
		$('#lstRgt').empty();
//		document.getElementById("treeDeptDiv").style.backgroundColor = "#E2E2E2"; //treeDept.enabled = false;  
		$.fn.zTree.init($("#treeDept"), treeSetting, []);
		$('#txtDept').val("");
		tmpDeptCd = "";
		$('#txtDept').prop("readonly", true);
//		return;
//	}
	
	$('#chkDel').wCheck("disabled", true);
	$('#chkDel').wCheck('check',false);
	$('#chkDelDiv').css('visibility','hidden'); //visible
	
	switch (getSelectedVal('cboSgnGbn').value) {
	    case "1":
	    	$('[data-ax5select="cboCommon"]').ax5select('setValue','2',true);
	    	$('[data-ax5select="cboBlank"]').ax5select('setValue','2',true);
	    	$('[data-ax5select="cboComAft"]').ax5select('setValue','2',true); 
	    	$('[data-ax5select="cboEmg"]').ax5select('setValue','2',true);
	    	$('[data-ax5select="cboEmgAft"]').ax5select('setValue','2',true);
	    	
	    	$('[data-ax5select="cboCommon"]').ax5select("disable");
	    	$('[data-ax5select="cboBlank"]').ax5select("disable");
	    	$('[data-ax5select="cboComAft"]').ax5select("disable");
	    	$('[data-ax5select="cboEmg"]').ax5select("disable");
	    	$('[data-ax5select="cboEmgAft"]').ax5select("disable");
	    	
	    	break;
	    case "2":
	    	$('[data-ax5select="cboCommon"]').ax5select('setValue','2',true);
	    	$('[data-ax5select="cboBlank"]').ax5select('setValue','2',true);
	    	$('[data-ax5select="cboComAft"]').ax5select('setValue','2',true); 
	    	$('[data-ax5select="cboEmg"]').ax5select('setValue','2',true);
	    	$('[data-ax5select="cboEmgAft"]').ax5select('setValue','2',true);
	    	
	    	$('[data-ax5select="cboCommon"]').ax5select("enable");
	    	$('[data-ax5select="cboBlank"]').ax5select("enable");
	    	$('[data-ax5select="cboComAft"]').ax5select("enable");
	    	$('[data-ax5select="cboEmg"]').ax5select("enable");
	    	$('[data-ax5select="cboEmgAft"]').ax5select("enable");
	    	
	    	$('#chkAllRgt').wCheck("disabled", true);	//결재직무
	    	$('#chkAllRgt').wCheck("check", false);
	    	$('#lstRgt').empty();
	    	
	    	$('#chkGrade').wCheck("disabled", true);	//프로그램등급선택
	    	$('#chkGrade').wCheck("check", false);
	    	$('#lstGrade').empty();
	    	$('#chkAllGrade').wCheck("disabled", true);	//프로그램등급 전체선택
	    	$('#chkAllGrade').wCheck("check", false);
	    	
			$.fn.zTree.init($("#treeDept"), treeSetting, []);
			$('#txtDept').val("");
			tmpDeptCd = "";
			$('#txtDept').prop("readonly", true);
	    	
	    	break;
	    case "C": //결재추가
	    	//////////////////////// 정상결재시 ////////////////////////
	    	findSw = false;
	    	
	    	if(cboCommonData.length > 0 && getSelectedIndex('cboCommon') > -1) {
	    		if(getSelectedVal('cboCommon').value == "2") findSw = true;
	    	}
	    	
	    	if(!findSw) {
	    		$('[data-ax5select="cboCommon"]').ax5select('setValue',"2",true);
	    	}
	    	
	    	//////////////////////// 부재시 ////////////////////////
	    	findSw = false;
	    	
	    	if(cboBlankData.length > 0 && getSelectedIndex('cboBlank') > -1) {
	    		if(getSelectedVal('cboBlank').value == "2") findSw = true;
	    	}
	    	
	    	if(!findSw) {
	    		$('[data-ax5select="cboBlank"]').ax5select('setValue',"2",true);
	    	}
	    	
	    	//////////////////////// 정상(업무후) ////////////////////////
	    	findSw = false;
	    	
	    	if(cboComAftData.length > 0 && getSelectedIndex('cboComAft') > -1) {
	    		if(getSelectedVal('cboComAft').value == "2") findSw = true;
	    	}
	    	
	    	if(!findSw) {
	    		$('[data-ax5select="cboComAft"]').ax5select('setValue',"2",true);
	    	}
	    	
	    	//////////////////////// 긴급(업무중) ////////////////////////
	    	findSw = false;
	    	
	    	if(cboEmgData.length > 0 && getSelectedIndex('cboEmg') > -1) {
	    		if(getSelectedVal('cboEmg').value == "2") findSw = true;
	    	}
	    	
	    	if(!findSw) {
	    		$('[data-ax5select="cboEmg"]').ax5select('setValue',"2",true);
	    	}
	    	
	    	//////////////////////// 긴급(업무후) ////////////////////////
	    	findSw = false;
	    	
	    	if(cboEmgAftData.length > 0 && getSelectedIndex('cboEmgAft') > -1) {
	    		if(getSelectedVal('cboEmgAft').value == "2") findSw = true;
	    	}
	    	
	    	if(!findSw) {
	    		$('[data-ax5select="cboEmgAft"]').ax5select('setValue',"2",true);
	    	}
	    	
	    	$('[data-ax5select="cboCommon"]').ax5select("disable");
	    	$('[data-ax5select="cboBlank"]').ax5select("disable");
	    	$('[data-ax5select="cboComAft"]').ax5select("disable");
	    	$('[data-ax5select="cboEmg"]').ax5select("disable");
	    	$('[data-ax5select="cboEmgAft"]').ax5select("disable");
//	    	document.getElementById("treeDeptDiv").style.backgroundColor = "#E2E2E2"; //treeDept.enabled = false;
	    	//treeDept.setStyle("alternatingItemColors", [0xDDDDDD, 0xDDDDDD] as Array );
	    	$.fn.zTree.init($("#treeDept"), treeSetting, []);
	    	$('#txtDept').val("");
	    	tmpDeptCd = "";
			$('#txtDept').prop("readonly", true);
//			document.getElementById("lstRgtDiv").style.backgroundColor = "#E2E2E2"; //lstRgtCd.enabled = false;
			$('#lstRgt').empty();
			$('#chkAllRgt').wCheck("disabled", true);
	    	$('#chkAllRgt').wCheck("check", false);
	    	$('#chkGrade').wCheck("check", false);
			chkGrade_Click();
//			document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
			$('#chkAllGrade').wCheck("disabled", true);
			$('#chkAllGrade').wCheck("check", false);
			$('#chkAllJawon').wCheck("disabled", true);
			$('#chkAllJawon').wCheck("check", false);
//			document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
			$('#chkJawon').wCheck("check", false);
			chkJawon_Click();
			
	    	break;
	    case "3":
	    case "6":
	    case "7":
	    case "P":
	    case "B": // 20221226 내부직원 결재시 직무선택 
	    case "8": //협조
	    	$('[data-ax5select="cboCommon"]').ax5select("enable");
	    	$('[data-ax5select="cboBlank"]').ax5select("enable");
	    	$('[data-ax5select="cboComAft"]').ax5select("enable");
	    	$('[data-ax5select="cboEmg"]').ax5select("enable");
	    	$('[data-ax5select="cboEmgAft"]').ax5select("enable");
//	    	document.getElementById("treeDeptDiv").style.backgroundColor = "#E2E2E2"; //treeDept.enabled = false;
			//treeDept.setStyle("alternatingItemColors", [0xDDDDDD, 0xDDDDDD] as Array );
			$.fn.zTree.init($("#treeDept"), treeSetting, []);
			$('#txtDept').val("");
			tmpDeptCd = "";
			$('#txtDept').prop("readonly", true);
//			document.getElementById("lstRgtDiv").style.backgroundColor = "#FFFFFF"; //lstRgtCd.enabled = true;
			setLstRgt();
			$('#chkAllRgt').wCheck("disabled", false);
			$('#chkGrade').wCheck("check", false);
			chkGrade_Click();
//			document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
			$('#chkAllGrade').wCheck("disabled", true);
			$('#chkAllGrade').wCheck("check", false);
			$('#chkAllJawon').wCheck("disabled", true);
			$('#chkAllJawon').wCheck("check", false);
//			document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
			$('#chkJawon').wCheck("check", false);
			chkJawon_Click();
			$('#chkEnd').wCheck("check", false);
			$('#chkAllJawon').wCheck("disabled", true);
			$('#chkAllJawon').wCheck("check", false);
			$('#chkDel').wCheck("disabled", false);
			$('#chkDel').wCheck("check", false);
			$('#chkDelDiv').css('visibility','visible');
			
			break;
	    case "4":
	    case "5": //처리팀
	    	$('[data-ax5select="cboCommon"]').ax5select("enable");
	    	$('[data-ax5select="cboBlank"]').ax5select("enable");
	    	$('[data-ax5select="cboComAft"]').ax5select("enable");
	    	$('[data-ax5select="cboEmg"]').ax5select("enable");
	    	$('[data-ax5select="cboEmgAft"]').ax5select("enable");
//	    	document.getElementById("treeDeptDiv").style.backgroundColor = "#E2E2E2"; //treeDept.enabled = false;
	    	//treeDept.setStyle("alternatingItemColors", [0xDDDDDD, 0xDDDDDD] as Array );
	    	$.fn.zTree.init($("#treeDept"), treeSetting, []);
	    	$('#txtDept').val("");
	    	tmpDeptCd = "";
			$('#txtDept').prop("readonly", true);
//			document.getElementById("lstRgtDiv").style.backgroundColor = "#FFFFFF"; //lstRgtCd.enabled = true;
			setLstRgt();
			$('#chkAllRgt').wCheck("disabled", false);
			$('#chkGrade').wCheck('check', false);
			chkGrade_Click();
//			document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
			$('#chkAllGrade').wCheck("disabled", true);
			$('#chkAllGrade').wCheck("check", false);
			$('#chkAllJawon').wCheck("disabled", true);
			$('#chkAllJawon').wCheck("check", false);
//			document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
			$('#chkJawon').wCheck("check", false);
			chkJawon_Click();
			$('#chkEnd').wCheck("check", false);
			$('#chkDel').wCheck("disabled", false);
			$('#chkDel').wCheck("check", false);
			$('#chkDelDiv').css('visibility','visible');
			
			break;
	    case "9": //특정팀/특정권한
	    	$('[data-ax5select="cboCommon"]').ax5select("enable");
	    	$('[data-ax5select="cboBlank"]').ax5select("enable");
	    	$('[data-ax5select="cboComAft"]').ax5select("enable");
	    	$('[data-ax5select="cboEmg"]').ax5select("enable");
	    	$('[data-ax5select="cboEmgAft"]').ax5select("enable");
//	    	document.getElementById("treeDeptDiv").style.backgroundColor = "#FFFFFF"; //treeDept.enabled = true;
			//treeDept.setStyle("alternatingItemColors", [0xFFFFFF, 0xFFFFFF] as Array );
	    	$.fn.zTree.init($("#treeDept"), treeSetting, treeDeptData);
	    	$('#txtDept').val("");
	    	tmpDeptCd = "";
			$('#txtDept').prop("readonly", false);
//			document.getElementById("lstRgtDiv").style.backgroundColor = "#FFFFFF"; //lstRgtCd.enabled = true;
			setLstRgt();
			$('#chkAllRgt').wCheck("disabled", false);
			$('#chkGrade').wCheck("check", false);
			chkGrade_Click();
//			document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
//			document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
			$('#chkJawon').wCheck("check", false);
			chkJawon_Click();
			$('#chkEnd').wCheck("check", false);
			$('#chkAllGrade').wCheck("disabled", true);
			$('#chkAllGrade').wCheck("check", false);
			$('#chkAllJawon').wCheck("disabled", true);
			$('#chkAllJawon').wCheck("check", false);
			
			break;
	    default:
//	    	console.log("default");
	    	$('#chkAllRgt').wCheck("disabled", true);
	    	$('#chkAllRgt').wCheck("check", false);
//	    	document.getElementById("lstRgtDiv").style.backgroundColor = "#E2E2E2"; //lstRgtCd.enabled = false;
			$('#lstRgt').empty();
//			document.getElementById("treeDeptDiv").style.backgroundColor = "#E2E2E2"; //treeDept.enabled = false;
			//treeDept.setStyle("alternatingItemColors", [0xDDDDDD, 0xDDDDDD] as Array );
			$.fn.zTree.init($("#treeDept"), treeSetting, []);
			$('#txtDept').val("");
			tmpDeptCd = "";
			$('#txtDept').prop("readonly", true);
			break;
	}
	
	if(getSelectedVal('cboSgnGbn').value == "1") {
		$('[data-ax5select="cboSysGbn"]').ax5select("enable");
	}else {
		$('[data-ax5select="cboSysGbn"]').ax5select('setValue',cboSysGbnData[0].cm_micode,true);
		$('[data-ax5select="cboSysGbn"]').ax5select("disable");
	}
}

//시스템 변경 이벤트 (cboSys_Click) 
function cboSys_Change() {
	$('#lstJawon').empty();
	
	if(getSelectedIndex('cboSys') < 1) {
		$('#chkJawon').wCheck("disabled", true);
		$('#chkAllJawon').wCheck("disabled", true);
		return;
	}
	$('#chkJawon').wCheck("disabled", false);
	$('#chkAllJawon').wCheck("disabled", false);
	$('#chkSys').wCheck("check", false);
	
	getProgInfo();
}

function getProgInfo() {
	//sysrsrc.getProgInfo(cboSys.selectedItem.cm_syscd);
	tmpInfoData = new Object();
	tmpInfoData = {
		SysCd		: getSelectedVal('cboSys').value,
		requestType	: 'getProgInfo'
	}
	
	ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', tmpInfoData, 'json', successProgInfo);
}

function successProgInfo(data) {
	lstJawonData = data;
	setLstJawon();
}

function setLstJawon() {
	$('#lstJawon').empty(); //리스트 초기화
	var addId = null;
	var liStr = null;
	
	if(lstJawonData == null || lstJawonData.length < 0) return;
	
	liStr  = '';
	if($('#chkJawon').is(':checked')) {
		lstJawonData.forEach(function(lstJawonData, Index) {
			addId = lstJawonData.cm_micode;
			liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
			liStr += '<div class="margin-3-top">';
			liStr += '	<input type="checkbox" class="checkbox-jawon" id="lstJawon'+addId+'" data-label="'+lstJawonData.cm_codename+'"  value="'+lstJawonData.cm_micode+'" />';
			liStr += '</div>';
			liStr += '</li>';
		});
		$('#lstJawon').html(liStr);
	}
	
	$('input.checkbox-jawon').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

function chkGrade_Click() {
	if($('#chkGrade').is(':checked')) {
//		document.getElementById("lstGradeDiv").style.backgroundColor = "#FFFFFF"; //lstType.enabled = true;
		setLstGrade();
		$('#chkAllGrade').wCheck("disabled", false);
	}else {
//		document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
		$('#lstGrade').empty();
		$('#chkAllGrade').wCheck("check", false);
		$('#chkAllGrade').wCheck("disabled", true);
	}
}

function chkJawon_Click() {
	if($('#chkJawon').is(':enabled') && $('#chkJawon').is(':checked') &&
			(getSelectedVal('cboReqCd').value.substr(0,1) == "0" || getSelectedVal('cboReqCd').value.substr(0,1) == "1")) {
		setLstJawon(); //getProgInfo();
//		document.getElementById("lstJawonDiv").style.backgroundColor = "#FFFFFF"; //lstProg.enabled = true;
		$('#chkAllJawon').wCheck("disabled", false);
	}else {
//		document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
		$('#lstJawon').empty();
		$('#chkAllJawon').wCheck("check", false);
		$('#chkAllJawon').wCheck("disabled", true);
	}
}

//직무리스트
function setLstRgt() {
	$('#lstRgt').empty(); //리스트 초기화
	var addId = null;
	var liStr = null;
	
	if(lstRgtData == null || lstRgtData.length < 0) return;
	
	liStr  = '';
	lstRgtData.forEach(function(lstRgtData, Index) {
		addId = lstRgtData.cm_micode;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		liStr += '<div class="margin-3-top">';
		liStr += '	<input type="checkbox" class="checkbox-rgtcd" id="lstRgt'+addId+'" data-label="'+lstRgtData.cm_codename+'"  value="'+lstRgtData.cm_micode+'" />';
		liStr += '</div>';
		liStr += '</li>';
	});
	$('#lstRgt').html(liStr);
	
	$('input.checkbox-rgtcd').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

//프로그램등급리스트
function setLstGrade() {
	$('#lstGrade').empty(); //리스트 초기화
	var addId = null;
	var liStr = null;
	
	if(lstGradeData == null || lstGradeData.length < 0) return;
	
	liStr  = '';
	lstGradeData.forEach(function(lstGradeData, Index) {
		addId = lstGradeData.cm_micode;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		liStr += '<div class="margin-3-top">';
		liStr += '	<input type="checkbox" class="checkbox-grade" id="lstGrade'+addId+'" data-label="'+lstGradeData.cm_codename+'"  value="'+lstGradeData.cm_micode+'" />';
		liStr += '</div>';
		liStr += '</li>';
	});
	$('#lstGrade').html(liStr);
	
	$('input.checkbox-grade').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

function grdSign_Click() {
	var i = 0;
	var j = 0;
	var findSw = false;
	var tmpWork = "";
	var tmpObj = null;
	
	var gridSelectedIndex = grdSign.selectedDataIndexs;
	
	var selectedGridItem = grdSign.list[grdSign.selectedDataIndexs];
	
	if(gridSelectedIndex < 0) return;
	
	if(getSelectedIndex('cboReqCd') < 0) findSw = true;
	else if(getSelectedVal('cboReqCd').value != selectedGridItem.cm_reqcd) findSw = true;
	
	if(findSw) {
		for(i=0; i<cboReqCdData.length; i++) {
			if(cboReqCdData[i].cm_micode == selectedGridItem.cm_reqcd) {
				$('[data-ax5select="cboReqCd"]').ax5select('setValue',cboReqCdData[i].value,true); //value값으로
				break;
			}
		}
	}
	
	if(selectedGridItem.cm_syscd == "99999") {
		$('#chkSys').wCheck("check", true);
		$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[0].value,true); //value값으로
		$('[data-ax5select="cboSys"]').ax5select("disable");
	}else {
		$('#chkSys').wCheck("check", false);
		findSw = false;
		
		if(getSelectedIndex('cboSys') < 1) findSw = true;
		else if(getSelectedVal('cboSys').value != selectedGridItem.cm_syscd) findSw = true;
		
		if(findSw) {
			for(i=0; i<cboSysData.length; i++) {
				if(cboSysData[i].cm_syscd == selectedGridItem.cm_syscd) {
					$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[i].value,true); //value값으로
					break;
				}
			}
		}
	}
	
	for(i=0; i<cboSignStepData.length; i++) {
		if(Number(cboSignStepData[i].cm_micode) == Number(selectedGridItem.cm_seqno)) {
			$('[data-ax5select="cboSignStep"]').ax5select('setValue',cboSignStepData[i].value,true); //value값으로
			break;
		}
	}
	
	$('#txtStepName').val(selectedGridItem.cm_name);
	
	for(i=0; i<cboSgnGbnData.length; i++) {
		if(cboSgnGbnData[i].cm_micode == selectedGridItem.cm_gubun) {
			$('[data-ax5select="cboSgnGbn"]').ax5select('setValue',cboSgnGbnData[i].value,true); //value값으로
			break;
		}
	}
	cboSgnGbn_Change();
	
	$('#chkMember').wCheck("check", false);
	$('#chkOutsourcing').wCheck("check", false);
	if(selectedGridItem.cm_manid == "1") $('#chkMember').wCheck("check", true); 
	else if(selectedGridItem.cm_manid == "2") $('#chkOutsourcing').wCheck("check", true);
	
	for(i=0; i<cboCommonData.length; i++) {
		if(cboCommonData[i].cm_micode == selectedGridItem.cm_common) {
			$('[data-ax5select="cboCommon"]').ax5select('setValue',cboCommonData[i].value,true); //value값으로
			break;
		}
	}
	
	for(i=0; i<cboBlankData.length; i++) {
		if(cboBlankData[i].cm_micode == selectedGridItem.cm_blank) {
			$('[data-ax5select="cboBlank"]').ax5select('setValue',cboBlankData[i].value,true); //value값으로
			break;
		}
	}
	
	for(i=0; i<cboComAftData.length; i++) {
		if(cboComAftData[i].cm_micode == selectedGridItem.cm_holiday) {
			$('[data-ax5select="cboComAft"]').ax5select('setValue',cboComAftData[i].value,true); //value값으로
			break;
		}
	}
	
	for(i=0; i<cboEmgData.length; i++) {
		if(cboEmgData[i].cm_micode == selectedGridItem.cm_emg) {
			$('[data-ax5select="cboEmg"]').ax5select('setValue',cboEmgData[i].value,true); //value값으로
			break;
		}
	}
	
	for(i=0; i<cboEmgAftData.length; i++) {
		if(cboEmgAftData[i].cm_micode == selectedGridItem.cm_emg2) {
			$('[data-ax5select="cboEmgAft"]').ax5select('setValue',cboEmgAftData[i].value,true); //value값으로
			break;
		}
	}
	
	if(selectedGridItem.cm_gubun == "1") {
		for(i=0; i<cboSysGbnData.length; i++) {
			if(cboSysGbnData[i].cm_micode == selectedGridItem.cm_jobcd) {
				$('[data-ax5select="cboSysGbn"]').ax5select('setValue',cboSysGbnData[i].value,true); //value값으로
				break;
			}
		}
	}
	
	if(selectedGridItem.cm_orgstep == "Y" && $('#chkDelDiv').css('visibility') == "visible") {
		$('#chkDel').wCheck("check", true);
	}
	
	$('#chkAllJawon').wCheck("disabled", true);
	$('#chkAllJawon').wCheck("check", false);
	
	if(selectedGridItem.cm_rsrccd != null && selectedGridItem.cm_rsrccd != "") {
		$('#chkJawon').wCheck("check", true);
		setLstJawon();
//		document.getElementById("lstJawonDiv").style.backgroundColor = "#FFFFFF"; //lstProg.enabled = true;
		$('#chkAllJawon').wCheck("disabled", false);
		
		tmpWork = selectedGridItem.cm_rsrccd;
		j = 0;
		for(i=0; i<lstJawonData.length; i++) {
			if(tmpWork.indexOf(lstJawonData[i].cm_micode) > -1) {
				$('#lstJawon'+lstJawonData[i].cm_micode).wCheck('check', true);
				
				if(i > j) {
					tmpObj = lstJawonData[i];
					//배열객체명.splice(시작위치, 삭제요소갯수, 추가할 문자열)
					lstJawonData.splice(i--,1);
					lstJawonData.splice(j,0,tmpObj);
				}else j++;
			}else $('#lstJawon'+lstJawonData[i].cm_micode).wCheck('check', false);
		}
	}else {
		$('#chkJawon').wCheck("check", false);
//		document.getElementById("lstJawonDiv").style.backgroundColor = "#E2E2E2"; //lstProg.enabled = false;
		$('#lstJawon').empty();
	}
	
	$('#chkGrade').wCheck("check", false);
//	document.getElementById("lstGradeDiv").style.backgroundColor = "#E2E2E2"; //lstType.enabled = false;
	$('#lstGrade').empty();
	$('#chkAllGrade').wCheck("disabled", true);
	$('#chkAllGrade').wCheck("check", false);
	
	if(selectedGridItem.cm_pgmtype != null && selectedGridItem.cm_pgmtype != "") {
		$('#chkGrade').wCheck("check", true);
//		document.getElementById("lstGradeDiv").style.backgroundColor = "#FFFFFF"; //lstType.enabled = true;
		setLstGrade();
		$('#chkAllGrade').wCheck("disabled", false);
		
		tmpWork = selectedGridItem.cm_pgmtype;
		j = 0;
		for(i=0; i<lstGradeData.length; i++) {
			if(tmpWork.indexOf(lstGradeData[i].cm_micode) > -1) {
				$('#lstGrade'+lstGradeData[i].cm_micode).wCheck('check', true);
				
				if(i > j) {
					tmpObj = lstGradeData[i];
					//배열객체명.splice(시작위치, 삭제요소갯수, 추가할 문자열)
					lstGradeData.splice(i--,1);
					lstGradeData.splice(j,0,tmpObj);
				}else j++;
			}else $('#lstGrade'+lstGradeData[i].cm_micode).wCheck('check', false);
		}
	}

//	document.getElementById("lstRgtDiv").style.backgroundColor = "#E2E2E2"; //lstRgtCd.enabled = false;
	$('#lstRgt').empty();
	$('#chkAllRgt').wCheck("disabled", true);
	
	//결재직무 값이 있는 경우 list에서 해당 값에 체크하고, list의 제일 위로 올려리는 조직
	if( selectedGridItem.cm_position != null && selectedGridItem.cm_position != "" ) {
//		document.getElementById("lstRgtDiv").style.backgroundColor = "#FFFFFF"; //lstRgtCd.enabled = true;
//		setLstRgt();
		$('#chkAllRgt').wCheck("disabled", false);
		
		tmpWork = selectedGridItem.cm_position;
		j = 0;
		for ( i=0; i<lstRgtData.length; i++ ) {
			if( tmpWork.indexOf(lstRgtData[i].cm_micode) > -1 ) {
				if(i > j) {
					tmpObj = lstRgtData[i];
					//배열객체명.splice(시작위치, 삭제요소갯수, 추가할 문자열)
					lstRgtData.splice(i--,1);
					lstRgtData.splice(j,0,tmpObj);
				} else j++;
			}
		}
		setLstRgt();
		for ( i=0; i<lstRgtData.length; i++ ) {
			if( tmpWork.indexOf(lstRgtData[i].cm_micode) > -1 ) {
				$('#lstRgt'+lstRgtData[i].cm_micode).wCheck('check', true);
			} else $('#lstRgtData'+lstRgtData[i].cm_micode).wCheck('check', false);
		}
		
	}
	
	if(selectedGridItem.cm_gubun != "1" && selectedGridItem.cm_jobcd != "" && selectedGridItem.cm_jobcd != null) {
		var node = treeObj.getNodeByParam('id', selectedGridItem.cm_jobcd);
		if(node !== null) {
			treeObj.selectNode(node);
		}
		$('#txtDept').val(treeObj.getSelectedNodes()[0].name);
		tmpDeptCd = treeObj.getSelectedNodes()[0].id;
		
		//console.log("node: " , node);
		//console.log("cm_jobcd: " + selectedGridItem.cm_jobcd);
	}
	
	if(selectedGridItem.cm_prcsw == "Y") $('#chkEnd').wCheck("check", true);
	else $('#chkEnd').wCheck("check", false);
}

function chkSys_Click() {
	if($('#chkSys').is(':checked')) {
		if(cboSysData.length > 0) {
			$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[0].value,true); //value값으로
			$('#chkJawon').wCheck("check", false);
			$('#chkAllJawon').wCheck("check", false);
			cboSys_Change();
		}
		$('[data-ax5select="cboSys"]').ax5select("disable");
	}else {
		$('[data-ax5select="cboSys"]').ax5select("enable");
	}
}

function chkAllRgt_Click() {
	//if (lstRgtCd.enabled == false) return;
	
	var checkSw = $('#chkAllRgt').is(':checked');
	var addId = null;
	
	lstRgtData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#lstRgt'+addId).wCheck('check', true);
		} else {
			$('#lstRgt'+addId).wCheck('check', false);
		}
	});
}

function chkAllGrade_Click() {
	//if (lstType.enabled == false) return;
	
	if(!$('#chkGrade').is(':checked')) return;
	
	var checkSw = $('#chkAllGrade').is(':checked');
	var addId = null;
	
	lstGradeData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#lstGrade'+addId).wCheck('check', true);
		} else {
			$('#lstGrade'+addId).wCheck('check', false);
		}
	});
}

function chkAllJawon_Click() {
	//if (lstProg.enabled == false) return;
	
	if(!$('#chkJawon').is(':checked')) return;
	
	if(getSelectedVal('cboReqCd').value.substr(0,1) != "0" && getSelectedVal('cboReqCd').value.substr(0,1) != "1") return;
	
	var checkSw = $('#chkAllJawon').is(':checked');
	var addId = null;
	
	lstJawonData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#lstJawon'+addId).wCheck('check', true);
		} else {
			$('#lstJawon'+addId).wCheck('check', false);
		}
	});
}

function btnQry_Click() {
	grdSign.setData([]);
	
	if(getSelectedIndex('cboReqCd') < 1) {
		dialog.alert('결재종류를 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(!$('#chkSys').is(':checked')) {
		if(getSelectedIndex('cboSys') < 1) {
			dialog.alert('시스템을 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	if(!$('#chkMember').is(':checked') && !$('#chkOutsourcing').is(':checked')) {
		dialog.alert('직원/외주 구분을 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	var tmpSys = "";
	var tmpMem = "";
	
	if(!$('#chkSys').is(':checked')) tmpSys = getSelectedVal('cboSys').value;
	else tmpSys = "99999";
	
	if(!$('#chkMember').is(':checked') && !$('#chkOutsourcing').is(':checked')) tmpMem = "9";
	else if ($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) tmpMem = "9";
	else if ($('#chkMember').is(':checked')) tmpMem = "1";
	else tmpMem = "2";
	
	//Cmm0300.getConfInfo_List(tmpSys,cboReq.selectedItem.cm_micode,tmpMan,"");

	$('[data-ax5grid="grdSign"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	tmpInfoData = new Object();
	tmpInfoData = {
		SysCd : tmpSys,
		ReqCd : getSelectedVal('cboReqCd').value,
		ManId : tmpMem,
		SeqNo : "",
		requestType	: 'getConfInfo_List'
	}
	console.log('getConfInfo_List ==>', tmpInfoData);
	ajaxAsync('/webPage/ecmm/Cmm0300Servlet', tmpInfoData, 'json', successApprovalInfo);
}

function successApprovalInfo(data) {
	$(".loding-div").remove();
	grdSignData = data;
	grdSign.setData(grdSignData);
}

function btnReq_Click() {
	var i = 0;
	var tmpWork = "";
	
	if(getSelectedIndex('cboReqCd') <= 0) {
		dialog.alert('결재종류를 선택하여 주시기 바랍니다.',function(){});
		$('#cboReqCd').focus();
		return;
	}
	
	if(getSelectedIndex('cboSignStep') <= 0) {
		dialog.alert('결재단계를 선택하여 주시기 바랍니다.',function(){});
		$('#cboSignStep').focus();
		return;
	}
	
	if(!$('#chkSys').is(':checked')) {
		if(getSelectedIndex('cboSys') < 1) {
			dialog.alert('시스템을 선택하여 주시기 바랍니다.',function(){});
			$('#cboSys').focus();
			return;
		}
	}
	
	if($('#txtStepName').val().length == 0) {
		dialog.alert('결재단계명칭을 입력하여 주시기 바랍니다.',function(){});
		$('#txtStepName').focus();
		return;
	}
	
	if(getSelectedIndex('cboSgnGbn') <= 0) {
		dialog.alert('결재구분을 선택하여 주시기 바랍니다.',function(){});
		$('#cboSgnGbn').focus();
		return;
	}
	
	if(!$('#chkMember').is(':checked') && !$('#chkOutsourcing').is(':checked')) {
		dialog.alert('직원/외주구분을 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(getSelectedIndex('cboCommon') <= 0) {
		dialog.alert('정상결재 시 처리방법을 선택하여 주시기 바랍니다.',function(){});
		$('#cboCommon').focus();
		return;
	}
	
	if(getSelectedIndex('cboBlank') <= 0) {
		dialog.alert('부재중인 경우 처리방법을 선택하여 주시기 바랍니다.',function(){});
		$('#cboBlank').focus();
		return;
	}
	
	if(getSelectedIndex('cboComAft') <= 0) {
		dialog.alert('업무후[정상] 처리방법을 선택하여 주시기 바랍니다.',function(){});
		$('#cboComAft').focus();
		return;
	}
	
	if(getSelectedIndex('cboEmg') <= 0) {
		dialog.alert('긴급[업무중] 처리방법을 선택하여 주시기 바랍니다.',function(){});
		$('#cboEmg').focus();
		return;
	}
	
	if(getSelectedIndex('cboEmgAft') <= 0) {
		dialog.alert('업무후[긴급] 처리방법을 선택하여 주시기 바랍니다.',function(){});
		$('#cboEmgAft').focus();
		return;
	}
	
	if(getSelectedVal('cboSgnGbn').value == "1") {
		if(getSelectedIndex('cboSysGbn') <= 0) {
			dialog.alert('자동처리구분을 선택하여 주시기 바랍니다.',function(){});
			$('#cboSysGbn').focus();
			return;
		}
	}
	
	if(getSelectedVal('cboSgnGbn').value == "9") {
		if($('#txtDept').val().length == 0) {
			dialog.alert('결재조직을 선택하여 주시기 바랍니다.',function(){});
			$('#txtDept').focus();
			return;
		}
	}
	
	if( getSelectedVal('cboSgnGbn').value == "3" || getSelectedVal('cboSgnGbn').value == "6" ||
		getSelectedVal('cboSgnGbn').value == "7" || getSelectedVal('cboSgnGbn').value == "8" ||
		getSelectedVal('cboSgnGbn').value == "4" || getSelectedVal('cboSgnGbn').value == "5" ||
		getSelectedVal('cboSgnGbn').value == "9" || getSelectedVal('cboSgnGbn').value == "P" || getSelectedVal('cboSgnGbn').value == "B") {	// 20221226 내부결재직원
		
		for(i=0; i<lstRgtData.length; i++) {
			if($('#lstRgt'+lstRgtData[i].cm_micode).is(':checked')) {
				break;
			}
		}
		
		if(i>=lstRgtData.length) {
			dialog.alert('결재직무를 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	if($('#chkGrade').is(':checked')) { //&& lstType.enabled == true
		for(i=0; i<lstGradeData.length; i++) {
			if($('#lstGrade'+lstGradeData[i].cm_micode).is(':checked')) {
				break;
			}
		}
		
		if(i>=lstGradeData.length) {
			dialog.alert('프로그램등급을 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	if($('#chkJawon').is(':checked')) { //&& lstProg.enabled == true
		for(i=0; i<lstJawonData.length; i++) {
			if($('#lstJawon'+lstJawonData[i].cm_micode).is(':checked')) {
				break;
			}
		}
		
		if(i>=lstJawonData.length) {
			dialog.alert('프로그램종류를 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	//Cmm0300.confInfo_Updt(tmpObj);
	tmpInfo = new Object();
	tmpInfo.cm_reqcd = getSelectedVal('cboReqCd').value;
	tmpInfo.cm_step = getSelectedVal('cboSignStep').value;
	tmpInfo.cm_name = $('#txtStepName').val();
	tmpInfo.cm_gubun = getSelectedVal('cboSgnGbn').value;
	
	if($('#chkSys').is(':checked')) {
		tmpInfo.cm_syscd = "99999";
	}else {
		tmpInfo.cm_syscd = getSelectedVal('cboSys').value;
	}
	
	if($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) {
		tmpInfo.cm_manid = "9";
	}else if ($('#chkMember').is(':checked')) {
		tmpInfo.cm_manid = "1";
	}else {
		tmpInfo.cm_manid = "2";
	}
	
	if(getSelectedVal('cboSgnGbn').value == "1") {
		tmpInfo.cm_sysgbn = getSelectedVal('cboSysGbn').value;
	} else{
		tmpInfo.cm_sysgbn = '';
	}
	
	tmpInfo.cm_common = getSelectedVal('cboCommon').value;
	tmpInfo.cm_blank = getSelectedVal('cboBlank').value;
	tmpInfo.cm_holi = getSelectedVal('cboComAft').value;
	tmpInfo.cm_emg = getSelectedVal('cboEmg').value;
	tmpInfo.cm_emg2 = getSelectedVal('cboEmgAft').value;
	
	if($('#chkDelDiv').css('visibility') == "visible" && $('#chkDel').is(':checked')) {
		tmpInfo.delsw = "Y";
	}else {
		tmpInfo.delsw = "N";
	}
	
	if(getSelectedVal('cboSgnGbn').value == "9") {
		tmpInfo.cm_deptcd = tmpDeptCd; //txtDeptName.toolTip;
	} else{
		tmpInfo.cm_deptcd = '';
	}
	
	if( getSelectedVal('cboSgnGbn').value == "3" || getSelectedVal('cboSgnGbn').value == "6" ||
		getSelectedVal('cboSgnGbn').value == "7" || getSelectedVal('cboSgnGbn').value == "8" ||
		getSelectedVal('cboSgnGbn').value == "4" || getSelectedVal('cboSgnGbn').value == "5" ||
		getSelectedVal('cboSgnGbn').value == "9" || getSelectedVal('cboSgnGbn').value == "P" || getSelectedVal('cboSgnGbn').value == "B") {
		
		tmpWork = "";
		
		for(i=0; i<lstRgtData.length; i++) {
			if($('#lstRgt'+lstRgtData[i].cm_micode).is(':checked')) {
				if(tmpWork.length > 0) tmpWork = tmpWork + ",";
				tmpWork = tmpWork + lstRgtData[i].cm_micode;
			}
		}
		tmpInfo.cm_rgtcd = tmpWork;
	} else{
		tmpInfo.cm_rgtcd = '';
	}
	
	if($('#chkGrade').is(':checked')) { //&& lstType.enabled == true
		tmpWork = "";
		for(i=0; i<lstGradeData.length; i++) {
			if($('#lstGrade'+lstGradeData[i].cm_micode).is(':checked')) {
				if(tmpWork.length > 0) tmpWork = tmpWork + ",";
				tmpWork = tmpWork + lstGradeData[i].cm_micode;
			}
		}
		tmpInfo.cm_pgmtype = tmpWork;
	} else{
		tmpInfo.cm_pgmtype = '';
	}
	
	if($('#chkJawon').is(':checked')) { //&& lstProg.enabled == true
		tmpWork = "";
		for(i=0; i<lstJawonData.length; i++) {
			if($('#lstJawon'+lstJawonData[i].cm_micode).is(':checked')) {
				if(tmpWork.length > 0) tmpWork = tmpWork + ",";
				tmpWork = tmpWork + lstJawonData[i].cm_micode;
			}
		}
		tmpInfo.cm_rsrccd = tmpWork;
	} else{
		tmpInfo.cm_rsrccd = '';
	}
//  프로그램종류를 체크하였을 경우, 종류선택이 필수여서 else if문 불필요
//	else if($('#chkJawon').is(':checked')) {
//		tmpInfo.cm_rsrccd = "Y";
//	}
	
	if($('#chkEnd').is(':checked')) {
		tmpInfo.cm_prcsw = "Y";
	}else {
		tmpInfo.cm_prcsw = "N";
	}
	
	tmpInfo.userId = userId;
	
	tmpInfoData = new Object();
	tmpInfoData = {
		etcData		: tmpInfo,
		requestType	: 'confInfo_Updt'
	}
	ajaxAsync('/webPage/ecmm/Cmm0300Servlet', tmpInfoData, 'json', successSetApprovalInfo);
}

function successSetApprovalInfo(data) {
	dialog.alert('결재정보가 등록되었습니다.',function(){
		btnQry_Click();
	});
}

function btnDel_Click() {
	var selectedGridItem = null;
	selectedGridItem = grdSign.list[grdSign.selectedDataIndexs];
	
	if(selectedGridItem == null) {
		dialog.alert('폐기 대상 선택 후 진행해 주시기 바랍니다.',function(){});
		return;
	}
	
	if(getSelectedIndex('cboReqCd') < 1) {
		dialog.alert('결재종류를 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(getSelectedIndex('cboSignStep') < 1) {
		dialog.alert('결재단계를 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if(!$('#chkSys').is(':checked')) {
		if(getSelectedIndex('cboSys') < 1) {
			dialog.alert('시스템을 선택하여 주시기 바랍니다.',function(){});
			return;
		}
	}
	
	if(!$('#chkMember').is(':checked') && !$('#chkOutsourcing').is(':checked')) {
		dialog.alert('직원/외주구분을 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	var tmpSys = "";
	var tmpMem = "";
	
	if(!$('#chkSys').is(':checked')) {
		tmpSys = getSelectedVal('cboSys').value;
	}else {
		tmpSys = "99999";
	}
	
	if($('#chkMember').is(':checked') && $('#chkOutsourcing').is(':checked')) {
		tmpMem = "9";
	}else if ($('#chkMember').is(':checked')) {
		tmpMem = "1";
	}else {
		tmpMem = "2";
	}
	
	//Cmm0300.confInfo_Close(tmpSys,cboReq.selectedItem.cm_micode,tmpMan,cboStep.selectedItem.cm_micode);
	tmpInfo = new Object();

	tmpInfoData = new Object();
	tmpInfoData = {
		SysCd : tmpSys,
		ReqCd : getSelectedVal('cboReqCd').value,
		ManId : tmpMem,
		SeqNo : getSelectedVal('cboSignStep').value,
		userId : userId,
		requestType	: 'confInfo_Close'
	}
	ajaxAsync('/webPage/ecmm/Cmm0300Servlet', tmpInfoData, 'json', successDelApprovalInfo);
}

function successDelApprovalInfo(data) {
	if(data == "OK"){
		dialog.alert('결재정보가 삭제처리되었습니다.',function(){
			btnQry_Click();
		});
	}
	else{
		dialog.alert(data,function(){
			btnQry_Click();
		});
	}
}

function btnAllQry_Click() {
	setTimeout(function() {			
		allApprovalInfoModal.open({
			width: 1024,
			height: 700,
			defaultSize: true,
			iframe: {
				method: "get",
				url: "../modal/approvalInfo/AllApprovalInfoModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
				}
			}
		}, function () {
		});
	}, 200);
}

function btnCopy_Click() {
	setTimeout(function() {			
		copyApprovalInfoModal.open({
			width: 1024,
			height: 700,
			defaultSize: true,
			iframe: {
				method: "get",
				url: "../modal/approvalInfo/CopyApprovalInfoModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
				}
			}
		}, function () {
		});
	}, 200);
}

function btnBlank_Click() {
	setTimeout(function() {			
		rangeApprovalInfoModal.open({
			width: 800,
			height: 750,
			iframe: {
				method: "get",
				url: "../modal/approvalInfo/RangeApprovalInfoModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
				}
			}
		}, function () {
		});
	}, 200);
}