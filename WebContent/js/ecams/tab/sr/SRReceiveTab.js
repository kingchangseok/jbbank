/**
 * SR 접수/반환 화면 (공통화면)
 * 
 * <pre>
 * &lt;b&gt;History&lt;/b&gt;
 * 	작성자: 허정규
 * 	버전 : 1.0
 *  수정일 : 2020-04-20
 * 
 */

var userName      = window.top.userName;
var userId        = window.top.userId;
var adminYN       = window.top.adminYN;
var userDeptName  = window.top.userDeptName;
var userDeptCd    = window.top.userDeptCd;
var strReqCd      = window.parent.strReqCd;
var codeList      = window.top.codeList;      //전체코드리스트
var strIsrId 	   = "";
var selectedSRInfo = null;
var createGrid= false;

var SysCd = "";
var SysCdCk = true;
var SRStatus = "";
var SrTitle = "";
var UserName = "";
var AddDate = "";
var GradeCd = "";
var TypeCd = "";
var DetailCd = "";
var tstusr = "";
var tstName = "";
var tstowner = "";
var ownerName = "";
var srgbncd = "";
var tag = "";
var sayu = "";
var mother = "";
var Editor = "";
var devstdt = "";
var deveddt = "";
var tablechg = "N";
var table = "";
var bauser = "";
var chkPgnm = true;
var chkData = false;
var strNow ="";
var devMasterUserId ="";
var pgmUser ="";
var srJobCd = "";
var addusrid = "";
var EditorName = "";

// 현업테스트 결재 여부 (테스트담당자 == 개발담당자)
var confTestUserCk = false;
// 항시 수정 가능 상태인지 체크
var testConfCk = false;
// ICT 감사 결재 확인 (테스트담당자가 현업담당자 권한 여부)
var confITCheck = "N";

var options = [];

var userInfoData = [];
var cboTstData= [];
var cboRequestData= [];
var cboOwnerData= [];
var cboDevData= [];
var cboGradeData = [];
var cboCatTypeSRData = [];
var cboDetailTypeData = [];
var cboSysData = [];
var cboSrJobData = [];
var cboBaUserData = [];
var cboPGMUserData = [];
var Cnchk = window.top.Cnchk;


var devUserGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();
//이 부분 지우면 영어명칭으로 바뀜
//ex) 월 -> MON
ax5.info.weekNames = [ {
							label : "일"
						}, {
							label : "월"
						}, {
							label : "화"
						}, {
							label : "수"
						}, {
							label : "목"
						}, {
							label : "금"
						}, {
							label : "토"
						} 
					];

picker.bind(defaultPickerInfo('basic', 'bottom'));
picker.bind(defaultPickerInfo('basic2', 'bottom'));

$('input.checkbox-pie').wCheck({
	theme : 'square-inset blue',
	selector : 'checkmark',
	highlightLabel : true
});

//요청자
$('[data-ax5select="cboRequest"]').ax5select({
	options: []
});
//개발책임자
$('[data-ax5select="cboOwner"]').ax5select({
	options: []
});
//테스트담당자
$('[data-ax5select="cboTst"]').ax5select({
	options: []
});
//개발담당자
$('[data-ax5select="cboDev"]').ax5select({
	options: []
});
//시스템
$('[data-ax5select="cboSys"]').ax5select({
	options: []
});
//SR업무
$('[data-ax5select="cboSrJob"]').ax5select({
	options: []
});
//BA
$('[data-ax5select="cboBaUser"]').ax5select({
	options: []
});
//PGM담당
$('[data-ax5select="cboPgmUser"]').ax5select({
	options: []
});
//분류유형
$('[data-ax5select="cboCatTypeSR"]').ax5select({
	options: []
});
//우선순위
$('[data-ax5select="cboDetailType"]').ax5select({
	options: []
});

$(document).ready(function(){

	setCboElement();
	
	$("#cboRequest").bind("change",function(){
		cborequest_change();
	});
	
	$("#cmdBefore").bind("click",function(){
		cmdBefore_click();
	});
	
	$("[name='opt']").bind("click",function(){
		if($(this).parent("#opt").hasClass("disabled")){
			$("#opt1").prop("checked", true);
			return;
		}
		opt_click();
	});

	$("#cmdCopy").bind("click",function(){
		cmdCopy_click();
	});
	
	$("#cmdCncl").bind("click",function(){
		cmdCncl_click();
	});
	
	$("#cmdRmv").bind("click",function(){
		cmdRmv_Click();
	});
	
	$("#cmdAdd").bind("click",function(){
		cmdAdd_Click();
	});
	
	$("#chkTable").bind("click",function(){
		chkTable_click();
	});

	
	$("#cmdReq").bind("click",function(){
		cmdReq_click();
	});

	$("#cboCatTypeSR").bind("change",function(){
		TypeCd = getSelectedVal("cboCatTypeSR").value;
	});
	
	$("#cboDetailType").bind("change",function(){
		DetailCd = getSelectedVal("cboDetailType").value;
	});
	
	$("#cboBaUser").bind("change",function(){
		bauser = getSelectedVal("cboBaUser").value;
	});
	
	$("#cboPgmUser").bind("change",function(){
		pgmUser = getSelectedVal("cboPgmUser").value;
		
		//PGM 담당자 선택시 개발담당자 초기세팅
		devUserGrid.setData([]);
		if(getSelectedIndex("cboSys") > 0){ // 시스템을 선택해야 개발책임자, 개발담당자 콤보 불러옴
			$('[data-ax5select="cboDev"]').ax5select("setValue", getSelectedVal("cboPgmUser").cm_userid, true);
			if(getSelectedIndex("cboDev") > 0){
				var addData = cboDevData[getSelectedIndex("cboDev")];
				addData.cc_status = "new";
				addData.cm_codename = "접수대기";
				devUserGrid.addRow(addData);
			}
		}
	});

	$("#cboSys").bind("change",function(){
		SysCd = getSelectedVal("cboSys").value;
		getSrJob();
		getUserSys2();
	});
	
	$("#cboSrJob").bind('change',function(){
		srJobCd = getSelectedVal("cboSrJob").value;
		changeSrJob();
	});
	
	$("#txtTst").bind("keypress",function(e){
		if(e.keyCode === 13) {
			var txtUser = $('#txtTst').val().trim();
			for (i = 1; cboTstData.length > i; i++) {
				if (cboTstData[i].cm_username.indexOf(txtUser) >= 0) {
					$('[data-ax5select="cboTst"]').ax5select("setValue", cboTstData[i].cc_editor, true);
					//$('#txtRequestor').val(cboUserData[i].cc_editor);
					break;
				}
			}
		}
	});
});

function setCboElement() {
	cboGradeData  = fixCodeList(codeList['REQGRADE'], 'SEL', 'cm_micode', 'ASC', 'N');
	cboCatTypeSRData  = fixCodeList(codeList['SRTYPE'], 'SEL', 'cm_seqno', 'ASC', 'N');
	cboDetailTypeData  = fixCodeList(codeList['DETAILTYPE'], 'SEL', 'cm_micode', 'ASC', 'N');
	
	codeList = null;
	
	options = [];
	// 2020 03 10 '00' 으로 세팅하는 부분 있어서 '**** > 00' 으로 수정
	
	cboGradeData[0].cm_micode = '00';
	$('[data-ax5select="cboGrade"]').ax5select({
		options: injectCboDataToArr(cboGradeData, 'cm_micode' , 'cm_codename')
	});
	
	cboCatTypeSRData[0].cm_micode = '00';
	$('[data-ax5select="cboCatTypeSR"]').ax5select({
        options: injectCboDataToArr(cboCatTypeSRData, 'cm_micode' , 'cm_codename')
   	});

	cboDetailTypeData[0].cm_micode = '00';
	$('[data-ax5select="cboDetailType"]').ax5select({
        options: injectCboDataToArr(cboDetailTypeData, 'cm_micode' , 'cm_codename')
   	});
}

function createViewGrid(){

	devUserGrid.setConfig({
		target : $('[data-ax5grid="devUserGrid"]'),
		sortable : true,
		multiSort : true,
		header : {
			align : "center",
			columnHeight : 30
		},
		body : {
			columnHeight : 28,
			onClick : function() {
				this.self.clearSelect();
				this.self.select(this.dindex);
			}
		},
		columns : [ {
			key : "cm_deptname",
			label : "소속부서",
			width : '25%'
		}, {
			key : "cm_username",
			label : "담당개발자",
			width : '38%'
		}, {
			key : "cm_codename",
			label : "상태",
			width : '30%'
		} ],
		page : {
			display : false
		}
	});
	
	createGrid = true;
}

function initFrame(){
	testConfCk = false;
	confITCheck = "N";
	if(strIsrId == ""){
		return;
	}

	getUserInfo();
	getUserSys();
	devUserGrid.setData([]);
	getSRDevInfo();
	
	$("#opt1").prop("checked", true);
	$("#subSrInsertBox").hide();
	
	$("#txtSRID").val(strIsrId);
	$("#txttitle").val(SrTitle);
	$("#txtcncl").val(sayu);
	
	if(tablechg=="Y"){
		$("#chkTable").wCheck('check', true);
		$("#txtTable").prop("disabled",false);
		$("#txtTable").val(table);
	}else{
		$("#chkTable").wCheck('check', false);
		$("#chkTable").wCheck('disabled', true);
		$("#txtTable").prop("disabled",true);
		$("#txtTable").val("");
	}
	
	if(AddDate != null && AddDate != "" && AddDate != undefined) $("#txtdate").val(AddDate);
	else $("#txtdate").val("");
	
	if(UserName != null && UserName != "" && UserName != undefined) $("#txtuser").val(UserName);
	else $("#txtuser").val("");
	
	//if(tag != null && tag != "") txttag").val(tag;
	if(devstdt != null && devstdt != "" && devstdt != undefined) {
		$("#strdate").val(devstdt);
	}
	else {
		var today = getDate('DATE',0);
		today = today.substring(0,4) + "/" + today.substring(4,6) + "/" + today.substring(6,8);
		$("#strdate").val(today);
	}
	
	if(deveddt != null && deveddt != "" && deveddt != undefined) $("#enddate").val(deveddt);
	else $("#enddate").val("");

	if(GradeCd != null && GradeCd != undefined && GradeCd != ""){
		$('[data-ax5select="cboGrade"]').ax5select("setValue", GradeCd, true);
	} else {
		$('[data-ax5select="cboGrade"]').ax5select("setValue", cboGradeData[1].cm_micode, true);
	}
	
	var i=0;
	if(SysCd != "" && SysCd != null && SysCd != undefined && SysCdCk && pgmUser != null && pgmUser != "" && pgmUser != undefined
			&& bauser != null && bauser != "" && bauser != undefined){
		if(mother == "Y"){
			$("#opt2").prop('disabled', false);
		}else{
			$("#opt1").prop('checked', true);
			$("#opt2").prop('disabled', true);
		}
	}else{
		SysCdCk = false;
		//시스템 코드가 없을경우 시스템 입력 할 수 있게
		//$("#subSrInsertBox").show();
		if(SRStatus != "03" && SRStatus != "04" && SRStatus != "09" && SRStatus != "47"){
			$("#cmdReq").prop("disabled", true);
			window.parent.backSRRegister();
			return;
		}
		
		getSysCd();
		if(TypeCd != null && TypeCd != undefined && TypeCd != ""){
			$('[data-ax5select="cboCatTypeSR"]').ax5select("setValue", TypeCd, true);
		} else {
			$('[data-ax5select="cboCatTypeSR"]').ax5select("setValue", cboCatTypeSRData[0].cm_micode, true);
		}
		$('[data-ax5select="cboDetailType"]').ax5select("setValue", cboDetailTypeData[0].cm_micode, true);
		$("#opt1").prop('checked', true);
		$("#opt2").prop('disabled', true);
	}
	if(srgbncd == "12"){
		chkData = true;
		chkPgnm = true;
	}else if(srgbncd == "13"){
		chkData = true;
		chkPgnm = false;
	}else if(srgbncd == "14"){
		chkData = false;
		chkPgnm = true;
	}else{
		chkData = false;
		chkPgnm = true;
	}
	
	var fbool = false;
	if(strReqCd == "62"){
		//2020.05.06 형상관리 고도화 : 통합테스트단계 전에는 정보 수정 가능하도록
		//	10: 작업완료 	25: 개발적용완료		32: 테스트적용결재진행중		35: 테스트적용완료
		if(SRStatus == "01" || SRStatus == "02" || SRStatus == "11" || SRStatus == "12" || SRStatus == "13" 
			|| SRStatus == "10" || SRStatus == "25" || SRStatus == "32" || (SRStatus == "35" && checkTestConf() == "Y")){
			$("#opt").removeClass("disabled");
			//txttag.enabled = true;
			allTrue();
			
			// 적용신청이 들어간 후 에는 개발 담당자는 수정 불가
			if(SRStatus == "25" || SRStatus == "32" || SRStatus == "35"){
				$("#cmdAdd").prop("disabled", true);
				$("#cmdRmv").prop("disabled", true);
				$('[data-ax5select="cboDev"]').ax5select("disable");
			}
			
			if(SRStatus == "01"){
				$("#cmdReq").text("접수");
			}else{
				$("#cmdReq").text("수정");
				if(mother == "Y"){
					$("#opt2").prop('disabled', false);
				}
			}
			if($("#cmdReq").prop("disabled")) $("#cmdReq").prop("disabled", false);
		}else{
			fbool = true;
		}
	}else{
		fbool = true;
	}
	
	// SR분할 가능하도록
	if(SRStatus != "03" && SRStatus != "04" && SRStatus != "09" && SRStatus != "47"){  // 03: SR반려   04: SR취소  09: SR종료   47: SR삭제
		if(mother == "Y"){
			$("#opt2").prop('disabled', false);
		}
	}
	
	if(fbool){
		allFalse();
		if(SRStatus == "47" || SRStatus == "04"){//SR삭제
			$('[data-ax5select="cboGrade"]').ax5select("setValue",cboGradeData[0].cm_micode,"true")
			$("#strdate").prop("disabled", true);
			$("#btnstrdate").prop("disabled",true);
			$("#enddate").prop("disabled", true);
			$("#btnenddate").prop("disabled",true);
			chkData = false;
			chkPgnm = false;
			devUserGrid.setData([]);
			$("txtuser").val("");
			$("txtdate").val("");
		}
		$("#subSrInsertBox").hide();
		$("#strdate").prop("disabled", true);
		$("#btnstrdate").prop("disabled",true);
		$("#enddate").prop("disabled", true);
		$("#btnenddate").prop("disabled",true);
		$("#cmdReq").prop("disabled", true);
		//txttag.enabled = false;
		if (SRStatus != "03" && SRStatus != "04" && SRStatus != "09" && SRStatus != "47"){

			testConfCk = true;
			$("#cmdReq").text("수정");
			$("#cmdReq").prop("disabled", false);
			$("#enddate").prop("disabled", false);
			$("#btnenddate").prop("disabled",false);
		}
	}
}

function getUserInfo(){
	var data = {
			userId	: userId,
		requestType	: 'getUserInfo'
	}
	ajaxAsync('/webPage/common/CommonUserInfo', data, 'json', successGetUserInfo);
}

function successGetUserInfo(data){
	var now = getDate('DATE',0);
	userInfoData = data;
	if(UserName == null || UserName == undefined || UserName == ""){
		$("#txtuser").val(userInfoData[0].cm_username);
	}
	$("#cmdCncl").prop("disabled",true);
	$("#txtcncl").prop("disabled",true);
	$("#cmdCopy").prop("disabled",true);
					
	if(strReqCd == "62"){
		// 2016.02.16 SR반려권한자만 enable
		if(Cnchk == "Y" && (SRStatus != "47" && SRStatus != "04")){
			$("#cmdCncl").prop("disabled",false);
			$("#txtcncl").prop("disabled",false);
		}else {
			$("#cmdCncl").prop("disabled",true);
			$("#txtcncl").prop("disabled",true);
		}
		$("#cmdCopy").prop("disabled",false);
	}
	
	strNow = now.substr(0,4) + "/" + now.substr(4,2) + "/" + now.substr(6,2);
	$("txtdate").val(strNow);
}

function getSRDevInfo(){
	var data = {
			userId	: "",
			strIsrId	: strIsrId,
		requestType	: 'getSRDevInfo'
	}
	ajaxAsync('/webPage/sr/SRStatus', data, 'json', successGetSRDevInfo);
}

function successGetSRDevInfo(data){
	devUserGrid.setData(data);
	if(data.length > 0){
		if(data[0].pgmCnt == "1"){
			chkPgnm = true;
		}else{
			chkPgnm = false;
		}
		if(data[0].dateCnt == "1"){
			chkData = true;
		}else{
			chkData = false;
		}
	}
	
	confTestUserCk = false;
	$(data).each(function(){
		if (this.cc_editor == tstusr){
			confTestUserCk = true;
		}
	});
}

function allTrue(){
	$("#strdate").prop("disabled",false);
	$("#enddate").prop("disabled",false);
	$('[data-ax5select="cboOwner"]').ax5select("enable");
	$('[data-ax5select="cboDev"]').ax5select("enable");
	$('[data-ax5select="cboTst"]').ax5select("enable");
	$('[data-ax5select="cboRequest"]').ax5select("enable");
	$('[data-ax5select="cboGrade"]').ax5select("enable");
	$("#cmdAdd").prop("disabled",false);
	$("#cmdRmv").prop("disabled",false);
	$("#btrequest").prop("disabled",false);
	$("#cmdReq").prop("disabled",false);
	$("#opt").removeClass("disabled");
	$("#chkTable").wCheck('disabled', false);
	if($("#chkTable").is(":checked") && !$("#txtTable").prop("disabled")) $("#txtTable").prop("disabled",false);
	else $("#txtTable").prop("disabled",true);
	///
	$("#lblrequest").prop("disabled",false);
	$("#txttitle").prop("disabled",false);
	$("#txtTst").prop("disabled",false);
	$("#strdate").prop("disabled",false);
	$("#btnstrdate").prop("disabled",false);
	$("#enddate").prop("disabled",false);
	$("#btnenddate").prop("disabled",false);
	$("#cmdRequest").prop("disabled",false);
	///
}

function allFalse(){
	$('[data-ax5select="cboOwner"]').ax5select("disable");
	$('[data-ax5select="cboDev"]').ax5select("disable");
	$('[data-ax5select="cboTst"]').ax5select("disable");
	$('[data-ax5select="cboRequest"]').ax5select("disable");
	$('[data-ax5select="cboGrade"]').ax5select("disable");
	$("#cmdAdd").prop("disabled",true);
	$("#cmdRmv").prop("disabled",true);
	$("#btrequest").prop("disabled",true);
	$("#opt").addClass("disabled");
	$("#chkTable").wCheck('disabled', true);
	$("#txtTable").prop("disabled",true);
	///
	$("#lblrequest").prop("disabled",true);
	$("#txttitle").prop("disabled",true);
	$("#txtTst").prop("disabled",true);
	$("#strdate").prop("disabled",true);
	$("#btnstrdate").prop("disabled",true);
	$("#enddate").prop("disabled",true);
	$("#btnenddate").prop("disabled",true);
	$("#cmdRequest").prop("disabled",true);
	///
}

function getUserSys(){
	var userInfo = {
		requestType : 'getUserSys'
	}

	ajaxAsync('/webPage/srcommon/SRRegister',
			userInfo, 'json', successGetUserSys);
}
function successGetUserSys(data){
	data.splice(0,0,{"cc_editor":"", "cm_hap": "선택하세요", "cm_username" : ""});
	cboTstData = clone(data);
	cboRequestData = clone(data);
	options = [];

	$('[data-ax5select="cboTst"]').ax5select(
			{
				options : injectCboDataToArr(cboTstData, 'cc_editor',
						'cm_hap')
			});
	
	if(tstusr != null && tstusr != "" && tstusr != undefined){
		// 사용자 비활성화 찾기
		var editorDisabled = true;
		$(cboTstData).each(function(){
			if(this.cc_editor == tstusr){
				editorDisabled = false;
				return true;
			}
		});
		confITCheck = "N";
		if(!editorDisabled){
			$('[data-ax5select="cboTst"]').ax5select("setValue", tstusr, true);
			$("#txtTst").val(cboTstData[getSelectedIndex("cboTst")].cm_username);
			if(getSelectedIndex("cboTst") > 0 && getSelectedVal("cboTst").rgt31Ck == "Y"){
				confITCheck = "Y";
			}
		} else {
			// 비활성화 사용자
			$("#cboTst").find("[data-ax5select-display='label']").text(tstName + " [비활성화]");
			$("#txtTst").val("");
		}
		
	} else {
		$('[data-ax5select="cboTst"]').ax5select("setValue", Editor, true);
		$("#txtTst").val(cboRequestData[getSelectedIndex("cboTst")].cm_username);
	}

	$('[data-ax5select="cboRequest"]').ax5select(
			{
				options : injectCboDataToArr(cboRequestData, 'cc_editor',
						'cm_hap')
			});

	// 사용자 비활성화 찾기
	var editorDisabled = true;
	$(cboRequestData).each(function(){
		if(this.cc_editor == Editor){
			editorDisabled = false;
			return true;
		}
	});
	
	if(!editorDisabled){
		$('[data-ax5select="cboRequest"]').ax5select("setValue", Editor, true);
	} else {
		// 비활성화 사용자
		$("#cboRequest").find("[data-ax5select-display='label']").text(EditorName + " [비활성화]");
	}
	cborequest_change();
	getUserSys2();
}

function cborequest_change(){
	$("#txtdept").val("");
	if(cboRequestData.length > 0 && getSelectedIndex("cboRequest") > 0){
		$("#txtdept").val(getSelectedVal("cboRequest").cm_deptname);
	}
}

function getSRInfo(){
	var ajaxReturnData = null;
	var prjData = new Object();
	prjData.deptCd = "ALL";
	prjData.gbnCd = "ALL";
	prjData.strDate = "";
	prjData.endDate = "";
	prjData.selCd = "SEL";
	prjData.selTxt = "";
	prjData.srId = strIsrId;
	
	prjData.userId 	= userId;
	prjData.reqCd 	= strReqCd;
	
	var prjInfo = new Object();
	prjInfo.prjInfoData =	prjData;
	prjInfo.requestType = 'getSRInfo';
	
	ajaxReturnData = ajaxCallWithJson('/webPage/common/ComPrjInfo', prjInfo, 'json');
	if(ajaxReturnData.length != 0){
		SysCdCk = true;
		SysCd = ajaxReturnData[0].cc_syscd;
		SrTitle = ajaxReturnData[0].cc_title;
		SRStatus = ajaxReturnData[0].cc_status;
		Editor = ajaxReturnData[0].cc_editor;
		UserName = ajaxReturnData[0].addusr;
		AddDate = ajaxReturnData[0].adddate;
		GradeCd = ajaxReturnData[0].grade;
		TypeCd = ajaxReturnData[0].type;
		DetailCd = ajaxReturnData[0].detail;
		tstusr = ajaxReturnData[0].cc_tstusr;
		tstName = ajaxReturnData[0].tstname;
		tstowner = ajaxReturnData[0].cc_owner;
		ownerName = ajaxReturnData[0].ownername;
		srgbncd = ajaxReturnData[0].cc_gbncd;
		tag = ajaxReturnData[0].cc_tag;
		sayu = ajaxReturnData[0].CC_CNCLSAYU;
		mother = ajaxReturnData[0].mother;
		devstdt = ajaxReturnData[0].devstdt;
		deveddt = ajaxReturnData[0].deveddt;
		tablechg = ajaxReturnData[0].CC_TABLECHG;
		table = ajaxReturnData[0].CC_TABLE;
		bauser = ajaxReturnData[0].cc_bauserid;
		addusrid = ajaxReturnData[0].addusrid;
		//devMasterUserid = ajaxReturnData[0].cm_devuserid;
		pgmUser = ajaxReturnData[0].pgmowner;
		EditorName = ajaxReturnData[0].creator;
		if(pgmUser){
			
		}
		srJobCd = ajaxReturnData[0].cc_srjobcd;
		initFrame();
	}
}

function getUserSys2(){
	if(SysCd == null || SysCd == undefined || SysCd == ""){
		$('[data-ax5select="cboOwner"]').ax5select({
			options : []
		});

		$('[data-ax5select="cboDev"]').ax5select({
			options : []
		});
		return;
	}
	var userInfo = {
			sysCd : SysCd,
		requestType : 'getUserSys2'
	}

	ajaxAsync('/webPage/srcommon/SRRegister',
			userInfo, 'json', successGetUserSys2);
}

function successGetUserSys2(data){
	data.splice(0,0,{"cc_editor":"", "cm_hap": "선택하세요"});
	cboOwnerData = clone(data);
	for (var i=1; i<cboOwnerData.length; i++){
		if(cboOwnerData[i].cm_rgtcd != "91"){
			cboOwnerData.splice(i,1);
			i--;
		}
	} 
	cboDevData = clone(data);
	for (var i=1; i<cboDevData.length; i++){
		if(cboDevData[i].cm_rgtcd != "90"){
			cboDevData.splice(i,1);
			i--;
		}
	} 
	options = [];

	$('[data-ax5select="cboOwner"]').ax5select(
			{
				options : injectCboDataToArr(cboOwnerData, 'cc_editor',
						'cm_hap')
			});

		
	$('[data-ax5select="cboDev"]').ax5select(
			{
				options : injectCboDataToArr(cboDevData, 'cc_editor',
				'cm_hap')
			});
	
	grade_Change();

	if($("#opt1").is(":checked")){ // MAIN-SR 접수
			
		if(tstowner != null && tstowner != "" && tstowner != undefined){
			// 사용자 비활성화 찾기
			var editorDisabled = true;
			$(cboOwnerData).each(function(){
				if(this.cc_editor == tstowner){
					editorDisabled = false;
					return true;
				}
			});
			
			if(!editorDisabled){
				$('[data-ax5select="cboOwner"]').ax5select("setValue", tstowner, true);
			} else {
				// 비활성화 사용자
				$("#cboOwner").find("[data-ax5select-display='label']").text(ownerName + " [비활성화]");
			}
		} else {
			//$('[data-ax5select="cboOwner"]').ax5select("setValue", devMasterUserid, true);
		}
			
		if(devUserGrid.getList().length > 0){
			$('[data-ax5select="cboDev"]').ax5select("setValue", devUserGrid.getList()[0].cc_editor, true);
		} else {
			if(pgmUser != null && pgmUser != "" && pgmUser != undefined && !$("#cmdReq").prop("disabled")){
				var pgmUserId = "";
				for(var i=0; i<cboRequestData.length; i++){
					if(cboRequestData[i].cm_username == pgmUser){
						pgmUserId = cboRequestData[i].cc_editor;
						break;
					}
					
				}
				$('[data-ax5select="cboDev"]').ax5select("setValue", pgmUserId, true);
				if(getSelectedIndex("cboDev") > 0){
					var addData = cboDevData[getSelectedIndex("cboDev")];
					addData.cc_status = "new";
					addData.cm_codename = "접수대기";
					devUserGrid.addRow(addData);
				}
			}
		}
	}
}


function grade_Change(){
	if(strReqCd != "62" || SRStatus =="03" || SRStatus =="04" || SRStatus =="09" || SRStatus == "47") return;
	
	if(getSelectedIndex("cboGrade") > 0 && getSelectedVal("cboGrade").cm_micode == "03"){  //차세대
		var i = 0;

		$("#cmdAdd").prop("disabled", true);
		$("#cmdRmv").prop("disabled", true);
		$('[data-ax5select="cboDev"]').ax5select("disable");

		//개발책임자 본인으로 셋팅
		$('[data-ax5select="cboOwner"]').ax5select("setValue", userId, true);
		//테스트담당자 본인으로 셋팅
		$('[data-ax5select="cboTst"]').ax5select("setValue", userId, true);
		//개발담당자 본인으로 셋팅 -> grdList에 추가
		if(SRStatus == "01"){
			$('[data-ax5select="cboDev"]').ax5select("setValue", userId, true);
			if(getSelectedVal("cboDev").cc_editor == userId){
				var addData = cboDevData[getSelectedIndex("cboDev")];
				devUserGrid.setData([]);
				addData.cc_status = "new";
				addData.cm_codename = "접수대기";
				devUserGrid.addRow(addData);
			}
		}

		//2020.05.06 형상관리 고도화 : 통합테스트단계 전에는 정보 수정 가능하도록
		//	10: 작업완료 , 개발적용 이후에는 수정 불가
		if(SRStatus == "02" || SRStatus == "11" || SRStatus == "12" || SRStatus == "13"
			|| SRStatus == "10"){
			$("#cmdAdd").prop("disabled", false);
			$("#cmdRmv").prop("disabled", false);
			$('[data-ax5select="cboDev"]').ax5select("enable");
		}
	}else{
		if(SRStatus == "01" || SRStatus == "02" || opt2.selected){
			$('[data-ax5select="cboOwner"]').ax5select("enable");
			$('[data-ax5select="cboTst"]').ax5select("enable");
			$('[data-ax5select="cboDev"]').ax5select("enable");
			
			if(SRStatus == "01"){
				if(TypeCd == "02" || TypeCd == "06"){
					//개발책임자 
					
					//cboowner.selectedIndex = 0;

					/*for(i=0; i<cboowner_dp.length; i++){
						if(cboowner_dp.getItemAt(i).cc_editor == UserId){
							cboowner.selectedIndex = i;
							break;
						}
					}*/							
					
					//테스트담당자 본인으로 셋팅
					//cbotst.selectedIndex = 0;
					/*for(i=0; i<cbotst_dp.length; i++){
						if(cbotst_dp.getItemAt(i).cc_editor == Editor){
							cbotst.selectedIndex = i;
							//cbotst.enabled = false;
							break;
						}
					}*/
					
					//개발담당자
					//cbodev.selectedIndex = 0;
					devUserGrid.setData([]);
				}else{
					//개발책임자 본인으로 셋팅
					/*for(i=0; i<cboowner_dp.length; i++){
						if(cboowner_dp.getItemAt(i).cc_editor == UserId){
							cboowner.selectedIndex = i;
							//cboowner.enabled = false;
							break;
						}
					}*/
					//테스트담당자 본인으로 셋팅
					/*for(i=0; i<cbotst_dp.length; i++){
						if(cbotst_dp.getItemAt(i).cc_editor == UserId){
							cbotst.selectedIndex = i;
							//cbotst.enabled = false;
							break;
						}
					}*/
					//개발담당자
					/*for(i=0; i<cbodev_dp.length; i++){
						if(cbodev_dp.getItemAt(i).cc_editor == UserId){
							cbodev.selectedIndex = i;
							//cbodev.enabled = false;
							grdList_dp.removeAll();
							grdList_dp.addItemAt(cbodev_dp.getItemAt(cbodev.selectedIndex), 0);
							grdList_dp.refresh();
							break;
						}
					}*/
				}
			}
		}
		
		if(SRStatus != "01" && devUserGrid.getList().length > 0){
			$('[data-ax5select="cboDev"]').ax5select("setValue", devUserGrid.getList()[0].cc_editor, true);
		}
	}
}

function cmdBefore_click(){
	if(TypeCd == undefined || TypeCd == "" || TypeCd == null){
		dialog.alert("분류유형이 등록되어 있지 않습니다.");
		return;
	}
	
	if(DetailCd == undefined || DetailCd == "" || DetailCd == null){
		dialog.alert("우선순위가 등록되어 있지 않습니다.");
		return;
	}
	confirmDialog.confirm({
		title: '확인',
		msg: 'SR-ID('+strIsrId+')에 대하여 접수전 사전검토를 등록하시겠습니까?',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			copyBefore();
		}
	});
}

function copyBefore(){
	var ajaxReturnData = null;
	
	var data = {
			srId	: strIsrId,
			srType	: TypeCd,
			srDetail	: DetailCd,
			srCncl	:  $("#txtcncl").val().trim(),
			title : $("#txttitle").val().trim(),
			requestType	: 'setBefore'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');

	if(ajaxReturnData == "OK"){
		dialog.alert("SR 접수전 사점검토 등록을 완료 하였습니다.");
	}else{
		dialog.alert(ajaxReturnData);
	}
	if(strReqCd == "62"){
		window.parent.getPrjList();
	}
}

function opt_click(){
	if($("#opt2").prop("disabled") == true) return;
	
	if($("#opt2").is(":checked")){ //Sub-SR등록 라디오버튼 클릭
		getSysCd();
		$("#cmdReq").text("접수");
		$('[data-ax5select="cboCatTypeSR"]').ax5select("setValue", cboCatTypeSRData[0].cm_micode, true);
		$('[data-ax5select="cboDetailType"]').ax5select("setValue", cboDetailTypeData[0].cm_micode, true);
		$('[data-ax5select="cboGrade"]').ax5select("setValue", cboGradeData[1].cm_micode, true);
		$('[data-ax5select="cboOwner"]').ax5select({option : []});
		$('[data-ax5select="cboDev"]').ax5select({option : []});
		
		chkData = false;
		chkPgnm = false;
		devUserGrid.setData([]);
		if(UserName == null || UserName == undefined || UserName == ""){
			$("#txtuser").val(userInfoData[0].cm_username);
		}
		$("#txtdate").val(strNow);
		$("#txttitle").prop("disabled",false);
		$("#chkTable").wCheck('check',false);
		$("#txtTable").val("");
		$("#txtTable").prop("disabled", true);
		
		if(Editor != undefined && Editor != null && Editor != ""){
			$('[data-ax5select="cboTst"]').ax5select("setValue", Editor, true);
			if(cboRequestData.length > 0 && getSelectedIndex("cboRequest") > 0){
				$("#txtdept").val(getSelectedVal("cboRequest").cm_deptname);
			}
		} else {
			$("#txtTst").val("");
			$('[data-ax5select="cboTst"]').ax5select("setValue", cboTstData[0].cc_editor, true);
		}
		
		allTrue();
		$("#subSrInsertBox").show();
	}else{
		$("#subSrInsertBox").hide();
		initFrame();
	}
}

function clear(){
	strIsrId = "";
	$("#txtSRID").val("");
	$("#txttitle").val("");
	$('[data-ax5select="cboGrade"]').ax5select("setValue", cboGradeData[0].cm_micode, true);
	$('[data-ax5select="cboTst"]').ax5select("setValue", cboTstData[0].cc_editor, true);
	$('[data-ax5select="cboOwner"]').ax5select("setValue", cboOwnerData[0].cc_editor, true);
	$('[data-ax5select="cboDev"]').ax5select("setValue", cboDevData[0].cc_editor, true);
	$('[data-ax5select="cboCatTypeSR"]').ax5select("setValue", cboCatTypeSRData[0].cm_micode, true);
	$('[data-ax5select="cboDetailType"]').ax5select("setValue", cboDetailTypeData[0].cm_micode, true);
	$('[data-ax5select="cboRequest"]').ax5select("setValue", userInfoData[0].cm_username, true);
	if(cboSysData.length > 0){
		$('[data-ax5select="cboSys"]').ax5select("setValue", cboSysData[0].cm_syscd, true);
	}
	$('[data-ax5select="cboSrJob"]').ax5select({option : []});
	if(cboBaUserData.length > 0){
		$('[data-ax5select="cboBaUser"]').ax5select("setValue", cboBaUserData[0].cm_userid, true);
	}
	if(cboPGMUserData.length > 0){
		$('[data-ax5select="cboPgmUser"]').ax5select("setValue", cboPGMUserData[0].cm_username, true);
	}
	$("#chkTable").wCheck('check',false);
	$("#txtTable").val("");
	$("#txtTable").prop("disabled", true);
	chkData = false;
	//chkPgnm.selected = false;
	$("#txtTst").val("");
	$("#strdate").val("");
	$("#enddate").val("");
	$("#opt1").prop("checked", true);
	//opt2.selected = false;
	$("#opt2").prop("disabled", false);
	devUserGrid.setData([]);
	if(UserName == null || UserName == undefined || UserName == ""){
		$("#txtuser").val(userInfoData[0].cm_username);
	}
	$("#txtdate").val(strNow);
	SysCdCk = true;
	$("#subSrInsertBox").hide();
	allTrue();
}

function cmdCncl_click(){
	if(strIsrId == "" || strIsrId == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert("취소할 SR을 선택해 주시기 바랍니다.");
		return;
	}
	if($("#txtcncl").val().length == 0){
		dialog.alert("검토의견을 입력해 주시기 바랍니다."); 
		$("#txtcncl").focus();
		return;
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: 'SR-ID('+strIsrId+')을 회수하시겠습니까?',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			cnclFun();
		}
	});
}

function cnclFun(){
	var ajaxReturnData = null;
	
	var data = {
			srId	: strIsrId,
			UserId	: userId,
			Status	: "03",
			srCncl	:  $("#txtcncl").val().trim(),
			requestType	: 'setSrCncl'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
	
	if(ajaxReturnData == "OK"){
		dialog.alert("SR-ID("+strIsrId+") 회수를 완료했습니다.");
		clear();
	}else{
		dialog.alert("관리자회수가 불가능한 상태입니다.\nSR-ID("+strIsrId+")은 "+ ajaxReturnData);
	}
	window.parent.getPrjList();
} 

function cmdCopy_click(){
	if(strIsrId == "" || strIsrId == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert("복사할 SR을 선택해 주시기 바랍니다.");
		return;
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: 'SR-ID('+strIsrId+')을 복사하시겠습니까?',
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			copyFun();
		}
	});
}

function copyFun(){
	var ajaxReturnData = null;
	
	var data = {
			srId	: strIsrId,
			UserId	: userId,
			requestType	: 'srCopy'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
	
	dialog.alert("SR 복사를 완료했습니다.");
	clear();
	window.parent.getPrjList(ajaxReturnData);
}

function chkTable_click(){
	if($("#chkTable").is(":checked")) {
		$("#txtTable").prop("disabled", false);
	}else{
		$("#txtTable").val("");
		$("#txtTable").prop("disabled", true);
	}
}

function cmdAdd_Click(){
	if(strIsrId == "" || strIsrId == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert("SR을 선택하십시오.");
		return;
	}else if(cboDevData.length == 0 || getSelectedIndex("cboDev") == 0) {
		dialog.alert("추가할 개발담당자를 선택하십시오.");
		return;
	}
	
	var grdList_dp = devUserGrid.getList(); 
	//추가하는 id랑 그리드 안에 있는 id가 같은경우 return;
	for(var i=0; i<grdList_dp.length; i++){
		if(grdList_dp[i].cc_editor == getSelectedVal("cboDev").cc_editor){
			dialog.alert("이미 추가된 개발담당자입니다.");
			return;
		}
	}
	
	var addData = cboDevData[getSelectedIndex("cboDev")];
	addData.cc_status = "new";
	addData.cm_codename = "접수대기";
	grdList_dp = new Array();
	grdList_dp.push(addData);
	//2020.05.06 형상관리 고도화 : 통합테스트단계 전에는 정보 수정 가능하도록
	//	10: 작업완료	
	if((SRStatus == "02" || SRStatus == "11" || SRStatus == "12" || SRStatus == "13" || SRStatus == "10") && $("#opt1").is(":checked")){
		var tmpObj = new Object();
		tmpObj.srid = strIsrId;
		tmpObj.userid = userId;
		tmpObj.pgmUser = pgmUser;
		tmpObj.title = $("#txttitle").val();
		tmpObj.addusrid = addusrid;
		if(getSelectedVal("cboGrade").cm_micode != "04"){
			var gbnCd = "";
			if(chkData && chkPgnm){
				gbnCd = "12";
			}else if(chkData && !chkPgnm){
				gbnCd = "13";
			}else if(!chkData && chkPgnm){
				gbnCd = "14";
			}
			tmpObj.gbncd = gbnCd;
		}else{
			tmpObj.gbncd = "";
		}
		setSRDevUsrInfo(tmpObj, grdList_dp);
		return;
	}else{
		devUserGrid.addRow(addData);
		dialog.alert("접수버튼을 클릭해야 추가됩니다.");
	}
}

function cmdRmv_Click(){
	var grdList_dp = devUserGrid.getList(); 
	if(strIsrId == "" || strIsrId == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert("SR을 선택하십시오.");
		return;
	}else if(devUserGrid.selectedDataIndexs.length < 1) {
		dialog.alert("삭제할 개발담당자를 선택하십시오.");
		return;
	}
	if(getSelectedVal("cboGrade").cm_micode == "03"){
		if(userId == devUserGrid.list[devUserGrid.selectedDataIndexs[0]].cc_editor){
			return;
		}
	}
	
	grdList_dp = devUserGrid.getList("selected");
	for(var i=0; i<grdList_dp.length; i++){
		if(grdList_dp[i].cc_status != "0" && grdList_dp[i].cc_status != "4" && grdList_dp[i].cc_status != "new"){
			dialog.alert("접수, 개발계획서작성완료 상태인\n 개발 담당자만 삭제 가능합니다.");
			return;
		}
	}
	
	//devUserGrid.removeRow(devUserGrid.selectedDataIndexs[0]);
	//2020.05.06 형상관리 고도화 : 통합테스트단계 전에는 정보 수정 가능하도록
	//	10: 작업완료	
	if((SRStatus == "02" || SRStatus == "11" || SRStatus == "12" || SRStatus == "13" || SRStatus == "10") && $("#opt1").is(":checked")){
		if(devUserGrid.getList().length != 1){
			var tmpObj = new Object();
			tmpObj.srid = strIsrId;
			tmpObj.userid = userId;
			tmpObj.title = $("#txttitle").val();
			if(getSelectedVal("cboGrade").cm_micode != "04"){
				var gbnCd = "";
				if(chkData && chkPgnm){
					gbnCd = "12";
				}else if(chkData && !chkPgnm){
					gbnCd = "13";
				}else if(!chkData && chkPgnm){
					gbnCd = "14";
				}
				tmpObj.gbncd = gbnCd;
			}else{
				tmpObj.gbncd = "";
			}
			setSRDevUsrInfo(tmpObj, grdList_dp);
			return;
		}else{
			dialog.alert("개발담당자는 한명이상 등록되어야 합니다.");
			return;
		}
	} else{
		devUserGrid.removeRow(devUserGrid.selectedDataIndexs[0]);
	}
}

function setSRDevUsrInfo(tmpObj, fileArr){
	var ajaxReturnData = null;
	
	var data = {
			tmpObj	: tmpObj,
			fileArr	: fileArr,
			requestType	: 'setSRDevUsrInfo'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');

	if(ajaxReturnData == "OK"){
		dialog.alert("개발담당자를 수정했습니다.");
		devUserGrid.setData([]);
		getSRDevInfo();
	}else{
		dialog.alert(ajaxReturnData);
		devUserGrid.setData([]);
		getSRDevInfo();
	}
}

function cmdReq_click(){
	if(strIsrId == "" || strIsrId == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert("접수할 SR을 선택해 주시기 바랍니다.");
		return;
	}
	
	if(testConfCk){
		if($("#enddate").val().length == 0){
			dialog.alert("완료예정일을 선택하십시오.");
			return;
		}
		confirmDialog.confirm({
			title: '확인',
			msg: "선택하신 SR을 수정 하시겠습니까? "
		}, function(){
			confirmDialog.close();
			if(this.key === 'ok') {
				var tmpObj = new Object();
				tmpObj.userid = userId;
				tmpObj.srid = strIsrId;
				tmpObj.endDate = replaceAllString($("#enddate").val(),"/","");
				
				setSRDevInfoAlways(tmpObj);
			}
		});
		return;
	}
	
	if($("#cmdReq").text() == "수정" && getSelectedIndex("cboTst") > 0 && (SRStatus == "25" || SRStatus == "32" || SRStatus == "35")){
		var devList = devUserGrid.getList();
		var confTestUserCk2 = false;
		// confTestUserCk == true : 현업테스트 결재 있음
		for(var i=0; i< devList.length; i++){
			if (devList[i].cc_editor == getSelectedVal("cboTst").cc_editor){
				confTestUserCk2 = true;
			}
		}
		if(confTestUserCk != confTestUserCk2 || confITCheck != getSelectedVal("cboTst").rgt31Ck){
			dialog.alert("적용신청 회수 후 테스트담당자를 변경하십시오.");
			if(confTestUserCk != confTestUserCk2){
				console.log("테스트 담당자 변경 불가 : 현업테스트결재 추가,삭제 ");
			}else {
				console.log("테스트 담당자 변경 불가 : ICT감사결재 추가,삭제 ");
			}
			return;
		}
		
	}
	
	if($("#subSrInsertBox").css("display") != "none"){
		if(getSelectedIndex("cboCatTypeSR") < 1){
			dialog.alert("분류유형을 선택하십시오.");
			return;
		}else if(getSelectedIndex("cboDetailType") < 1){
			dialog.alert("우선순위를 선택하십시오.");
			return;
		}else if(getSelectedIndex("cboSys") < 1){
			dialog.alert("시스템을 선택하십시오.");
			return;
		}else if(getSelectedIndex("cboBaUser") < 1){
			dialog.alert("BA를 선택하십시오.");
			return;
		}
		/* 필수 아님
		else if(getSelectedIndex("cboPgmUser") < 1){
			dialog.alert("PGM담당을 선택하십시오.");
			return;						
		}
		*/
	}
	
	if(getSelectedIndex("cboOwner") < 1){
		dialog.alert("개발책임자를 선택하십시오.");
		return;
	}else if(getSelectedIndex("cboTst") < 1){
		dialog.alert("테스트담당자를 선택하십시오.");
		return;
	}else if(getSelectedIndex("cboRequest") < 1){
		dialog.alert("요청자를 선택하십시오.");
		return;
	}
	
	var devUser = "";
	var grdList_dp = devUserGrid.getList(); 
	for(var i=0; i<grdList_dp.length; i++){
		devUser = devUser + grdList_dp[i].cm_username + " "
	}
	var tmpObj = {};
	var grdList_dp = devUserGrid.getList();
	if(!$("#opt1").is(":checked")){//등록, 수정
		if(SRStatus == "01"){
			dialog.alert("Main-SR이 접수하여야 Sub-SR등록을 하실수 있습니다. Main-SR을 먼저 접수하시기 바랍니다.");
			$("#opt1").prop("checked", true);
			return;
		}
		
		tmpObj.reqcd = "01";//id새로땀							
		if(getSelectedVal("cboGrade").cm_micode == "03"){
			tmpObj.reqcd = "01";//id새로땀
			dialog.alert("반영프로세스가 차세대인 경우 Sub-SR분할이 불가합니다.");
			return;
		}
	}else{
		tmpObj.reqcd = "02";//id 가지고감
	}
	
	if(strIsrId == "" || strIsrId == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert($("#cmdReq").text()+"할 SR을 선택하십시오.");
		return;
	}else{
		if(getSelectedVal("cboGrade").cm_micode != "04"){
			if(SysCd == "" || SysCd == null || SysCd == undefined){
				dialog.alert("시스템이 등록되어 있지 않습니다.");
				return;
			}else if(TypeCd == undefined || TypeCd == "" || TypeCd == null){
				dialog.alert("분류유형이 등록되어 있지 않습니다.");
				return;
			}else if(DetailCd == undefined || DetailCd == "" || DetailCd == null){
				dialog.alert("우선순위가 등록되어 있지 않습니다.");
				return;
			}else if(grdList_dp.length == 0){
				dialog.alert("개발담당자를 추가하십시오.");
				return;						
			}
		}
	}
	
	var endDate = replaceAllString($("#enddate").val(),"/","");
	var strDate = replaceAllString($("#strdate").val(),"/","");
	var today = replaceAllString(getDate('DATE',0), "/", "");
	
	if($("#strdate").val().length == 0){
		dialog.alert("착수예정일을 선택하십시오.");
		return;
	}else if($("#enddate").val().length == 0){
		dialog.alert("완료예정일을 선택하십시오.");
		return;
	}else if(endDate < strDate){
		dialog.alert("착수예정일이 완료예정일 보다 큽니다.");
		return;
	}else if ((endDate < today || strDate < today) && $("#cmdReq").text() == "접수"){
		dialog.alert("착수예정일과 완료예정일은 \nSR 접수일["+today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2)+"] 이후로 입력하십시오.");
		return;
	}
	
	if($("#chkTable").is(":checked")){
		if($("#txtTable").val().trim().length == 0){
			dialog.alert("DB오브젝트 변경을 선택하셨습니다.\n변경할 DB오브젝트명(테이블,인덱스등)을 입력하시기 바랍니다.");
			$("#txtTable").focus();
			return;
		}else if($("#txtTable").val().trim().length > 500){
			dialog.alert("DB오브젝트명(테이블,인덱스등)을  500자 이내로 입력해 주시기바랍니다.");
			$("#txtTable").focus();
			return;
		}
		tmpObj.CC_TABLECHG = "Y";
		tmpObj.CC_TABLE = $("#txtTable").val().trim();
	}else{
		tmpObj.CC_TABLECHG = "N";
		tmpObj.CC_TABLE = "";
	}
	
	tmpObj.userid = userId;
	tmpObj.srid = strIsrId;
	
	tmpObj.srStatus = SRStatus;
	tmpObj.grade = getSelectedVal("cboGrade").cm_micode;
	tmpObj.title = $("#txttitle").val();
	tmpObj.cnclsayu = $("#txtcncl").val();
	if(getSelectedVal("cboGrade").cm_micode != "04"){
		tmpObj.syscd = SysCd;
		tmpObj.srtype = TypeCd;
		tmpObj.detail = DetailCd;
		tmpObj.owner = getSelectedVal("cboOwner").cc_editor;
		tmpObj.tstusr = getSelectedVal("cboTst").cc_editor;
		
		var gbnCd = "";
		if(chkData && chkPgnm){
			gbnCd = "12";
		}else if(chkData && !chkPgnm){
			gbnCd = "13";
		}else if(!chkData && chkPgnm){
			gbnCd = "14";
		}
		tmpObj.gbncd = gbnCd;
	}else{
		tmpObj.syscd = "";
		tmpObj.srtype = "";
		tmpObj.detail = "";
		tmpObj.owner = "";
		tmpObj.tstusr = "";
		tmpObj.gbncd = "";
	}
	//tmpObj.tag = txttag.text;
	tmpObj.tag = "";
	tmpObj.strDate = $("#strdate").val().substr(0,4)+$("#strdate").val().substr(5,2)+$("#strdate").val().substr(8,2);
	tmpObj.endDate = $("#enddate").val().substr(0,4)+$("#enddate").val().substr(5,2)+$("#enddate").val().substr(8,2);
	tmpObj.cmc0100editor = getSelectedVal("cboRequest").cc_editor;				
	tmpObj.bauser = bauser;
	tmpObj.pgmUser = pgmUser;
	tmpObj.srJobCd = srJobCd;
	
	confirmDialog.confirm({
		title: '확인',
		msg: "선택하신 대로 접수 하시겠습니까? " + "\n" +
			   "SR 요청자          : "+getSelectedVal("cboRequest").cm_username + "\n" +
	           "SR 개발책임자    : "+getSelectedVal("cboOwner").cm_username + "\n" +
	           "SR 테스트담당자 : "+getSelectedVal("cboTst").cm_username + "\n" +
	           "SR 개발담당자 :  "+devUser,
	}, function(){
		confirmDialog.close();
		if(this.key === 'ok') {
			if(SRStatus == "01" || $("#opt2").is(":checked")){
				setSRDevInfo(tmpObj, grdList_dp);
			}else{
				setSRDevInfo(tmpObj, null);
			}
		}
	});
}

function setSRDevInfo(tmpObj, gridList){

	var ajaxReturnData = null;
	
	var data = {
			tmpObj	: tmpObj,
			gridList	: gridList,
			requestType	: 'setSRDevInfo'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');

	if(ajaxReturnData.indexOf("OK") > -1){
		dialog.alert("SR "+$("#cmdReq").text()+"을 완료했습니다.");
	}else{
		dialog.alert(ajaxReturnData);
	}
	if($("#opt2").is(":checked")){
		clear();
		window.parent.getPrjList(ajaxReturnData.split(",")[1]);
	} else {
		clear();
		window.parent.getPrjList();
	}
	
}

// 항시 수정 가능한 목록 업데이트
function setSRDevInfoAlways(tmpObj){

	var ajaxReturnData = null;
	
	var data = {
			tmpObj	: tmpObj,
			requestType	: 'setSRDevInfoAlways'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');

	if(ajaxReturnData.indexOf("OK") > -1){
		dialog.alert("SR "+$("#cmdReq").text()+"을 완료했습니다.");
	}else{
		dialog.alert(ajaxReturnData);
	}
	
	clear();
	window.parent.getPrjList();
	
}

function getSysCd(){

	var ajaxReturnData = null;
	var sysData = new Object();
	
	sysData.UserId = userId;
	sysData.SecuYn = "Y";
	sysData.SelMsg = "SEL";
	sysData.CloseYn = "N";
	sysData.ReqCd = "";
	
	var data = {
			sysData : sysData,
		requestType : 'getSysInfo'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/common/CommonSysInfo',data, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboSysData = ajaxReturnData;
		options = [];

		$('[data-ax5select="cboSys"]').ax5select(
				{
					options : injectCboDataToArr(cboSysData, 'cm_syscd',
							'cm_sysmsg')
				});
	}
	getSrJob();
	getPGMUserInfo();
	getBaUserInfo();
}

function getSrJob(){

	var srJobData = new Object();
	if(getSelectedIndex("cboSys") < 1){
		$('[data-ax5select="cboSrJob"]').ax5select(
				{
					options : []
				});
		return;
	} else {
		srJobData.sysCd = getSelectedVal("cboSys").value;
	}
	var data = {
			srJobData : srJobData,
		requestType : 'getSrJobList'
	}

	ajaxAsync('/webPage/administrator/SysInfoServlet',data, 'json', successGetSrJob);	

}

function successGetSrJob(data){
	if (data !== 'ERR') {
		cboSrJobData = data;
		options = [];
		cboSrJobData.splice(0,0,{"cm_srjobcd": "", "cm_srjobname" : "선택하세요"})
		$('[data-ax5select="cboSrJob"]').ax5select(
				{
					options : injectCboDataToArr(cboSrJobData, 'cm_srjobcd',
							'cm_srjobname')
				});
	}
}

function getBaUserInfo(){
	var ajaxReturnData = null;
	var userInfo = {
		requestType : 'getBaUserInfo'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/srcommon/SRRegister',
			userInfo, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboBaUserData = ajaxReturnData;
		options = [];

		$('[data-ax5select="cboBaUser"]').ax5select(
				{
					options : injectCboDataToArr(cboBaUserData, 'cm_userid', 'cm_hap')
				});
	}
}

function getPGMUserInfo(){
	var ajaxReturnData = null;
	var userInfo = {
		requestType : 'getPGMUserInfo'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/srcommon/SRRegister',
			userInfo, 'json');
	if (ajaxReturnData !== 'ERR') {
		cboPGMUserData = ajaxReturnData;
		options = [];

		$('[data-ax5select="cboPgmUser"]').ax5select(
				{
					options : injectCboDataToArr(cboPGMUserData, 'cm_username', 'cm_hap')
				});
	}
}

function changeSrJob(){ //SR업무 선택시 기본값 자동세팅
	devUserGrid.setData([]);
	if(getSelectedIndex("cboSrJob") < 1){
		return;
	}
	$('[data-ax5select="cboBaUser"]').ax5select("setValue",getSelectedVal("cboSrJob").cm_bauserid,"true");
	//$('[data-ax5select="cboPgmUser"]').ax5select("setValue",getSelectedVal("cboSrJob").pgmUserName,"true");

	bauser = getSelectedVal("cboBaUser").value;
	//pgmUser = getSelectedVal("cboPgmUser").value;
	
	//$('[data-ax5select="cboOwner"]').ax5select("setValue",getSelectedVal("cboSrJob").cm_devuserid,"true");
	//$('[data-ax5select="cboTst"]').ax5select("setValue", Editor, true);
	//$("#txtTst").val(cboRequestData[getSelectedIndex("cboTst")].cm_username);
	/*
	$('[data-ax5select="cboDev"]').ax5select("setValue", getSelectedVal("cboPgmUser").cm_userid, true);
	if(getSelectedIndex("cboDev") > 0){
		devUserGrid.addRow(clone(cboDevData[getSelectedIndex("cboDev")]));
	}
	*/
}

function checkTestConf(){

	if(SRStatus != "35"){
		return "N";
	}
	
	var ajaxReturnData = null;
	var userInfo = {
		srId : strIsrId,
		requestType : 'checkTestConf'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus',
			userInfo, 'json');
	
	return ajaxReturnData;
}
