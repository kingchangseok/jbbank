var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;
var rgtList			= window.top.rgtList;

/* 관리담당자 선택 관련 변수 */
var userModal	 	= new ax5.ui.modal();  
var popSelItem			= [];

var cboRsrcCdData	    = [];	//프로그램종류 데이터
var cboJobData		    = [];	//업무 데이터
var cboTeamData		    = [];
var cboCompileData		= [];
var cboMakeData			= [];
var cboDirData		    = [];	//프로그램경로 데이터
var progInfoData        = [];

var isQA 				= false;
var isVerSync			= false;
var selSw 			    = false;
var rpaBTN				= false;
var myWin 			    = null;
var pUserId             = '';
var strInfo 		    = '';    //프로그램 info
var strSysInfo          = '';
var SecuYn              = '';
var btnVal 				= '';
var callerId			= '';

var completeReadyFunc = false;

var tmpInfo = new Object();
var tmpInfoData = new Object();

$('[data-ax5select="cboRsrcCd"]').ax5select({
    options: []
});
$('[data-ax5select="cboJob"]').ax5select({
    options: []
});
$('[data-ax5select="cboTeam"]').ax5select({
	options: []
});
$('[data-ax5select="cboCompile"]').ax5select({
	options: []
});
$('[data-ax5select="cboMake"]').ax5select({
	options: []
});
$('[data-ax5select="cboEditor"]').ax5select({
	options: []
});
$('input:radio[name=rdoGbn]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name=rdoGbn2]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$(document).ready(function(){
	if (rgtList != null && rgtList != undefined && rgtList != '') {	
		var rgtArray = rgtList.split(',');
		for (var i=0; i<rgtArray.length; i++) {
			if(rgtArray[i] == "A1") adminYN = true;
			if(rgtArray[i] == "QA") isQA = true;
			if(rgtArray[i] == "34") isVerSync = true;
			if(rgtArray[i] == "RP") rpaBTN = true;
		}
	}
	
	$('#btnVersion').hide();
	if(adminYN || isVerSync) {
		$('#btnVersion').show();
	}
	
	$('#btnRPA').hide();
	if(rpaBTN){
		$('#btnRPA').show();
	}

	//프로그램종류
	$('#cboRsrcCd').bind('change', function() {
		cboRsrcCd_Change();
	});

//	//폐기해제
//	$('#btnReOpen').bind('click',function() {
//		btnReOpen_Click();
//	});
	
	//폐기
	$('#btnTblDel').bind('click',function() {
		if(btnVal == "alive") {
			btnReOpen_Click();
		}else {
			btnTblDel_Click();
		}
	});
	
	//소스비교
	$('#btnDiff').bind('click',function() {
		btnDiff_Click();
	});
	
	//소스보기
	$('#btnView').bind('click',function() {
		btnView_Click();
	});
	
	//수정
	$('#btnRegist').bind('click',function() {
		btnRegist_Click();
	});
	
	//버전동기화
	$('#btnVersion').bind('click',function() {
		btnVersion_Click();
	});
	
	// rpa 활성화
	$('#btnRPA').bind('click',function() {
		btnRPA_Click();
	});
	
	//관리담당자
	$('#txtCaller').bind('click',function() {
			setTimeout(function() {
				userModal.open({
					width: 500,
					height: 300,
					iframe: {
						method: "get",
						url: "../../modal/userinfo/UserSelectModal.jsp"
					},
					onStateChanged: function () {
						if (this.state === "open") {
							deptCd = '0091';
							mask.open();
						}
						else if (this.state === "close") {
							mask.close();
							if(popSelItem != null && popSelItem != undefined) {
								$("#txtCaller").val(popSelItem.cm_username);
								$('#txtCallerToolTip').attr('tooltip', popSelItem.cm_userid);
								callerId = popSelItem.cm_userid;
							}
						}
					}
				}, function () {
					
				});
			}, 200);
	});
	
	$("#divCboRsrcCd").hide();
	
	if(adminYN) SecuYn = "Y";
	else SecuYn = "N";
	
	console.log("progBaseTab load complete");
	
	completeReadyFunc = true;
});

function screenInit(gbn,userId) {	
	if(gbn == 'M') {
		$('[data-ax5select="cboJob"]').ax5select({
	        options: []
		});
		
		$('[data-ax5select="cboRsrcCd"]').ax5select({
	        options: []
		});
	}
	$('#divPrgCbo').css('display', 'none');
	$('#divPrgTxt').css('display', 'block');
	$('#divDirCbo').css('display', 'none');
	$('#divDirTxt').css('display', 'block');

	$('#txtCallerToolTip').attr('tooltip','');
	$('#txtCaller').val('');
	$('#txtSysMsg').val('');
	$('#txtLanguge').val('');
	$('#txtProgId').val('');
	$('#txtStory').val('');
	$('#txtPath').val('');
	$('#txtCreator').val('');
	$('#txtEditor').val('');
	$('#txtLstUsr').val('');
	$('#txtLstDat').val('');
	$('#txtCreatDt').val('');
	$('#txtLstDt').val('');
	$('#txtSta').val('');
	$('#txtOrderId').val('');
	$('#txtStory').prop("readonly", true);
	
	$('#txtPath').prop('disabled', true);
	$('#btnTblDel').prop('disabled', true);
	$('#btnDiff').prop('disabled', true);
	$('#btnView').prop('disabled', true);
	$('#btnRegist').prop('disabled', true);
	
	$('[data-ax5select="cboEditor"]').ax5select({
        options: []
	});

	if (cboJobData != null && cboJobData.length > 0) {
		$('[data-ax5select="cboJob"]').ax5select('setValue', 'ALL', true); 	// 업무 초기화
	}
	if (cboRsrcCdData != null && cboRsrcCdData.length > 0) {
		$('[data-ax5select="cboRsrcCd"]').ax5select('setValue', 'ALL', true); 	// 프로그램유형 초기화
	}
	
	/*$('[data-ax5select="cboEditor"]').ax5select("disable");
	$('[data-ax5select="cboJob"]').ax5select("disable");
	$('[data-ax5select="cboRsrcCd"]').ax5select("disable");*/
}

//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(data) {
	progInfoData = [];
	$('[data-ax5select="cboJob"]').ax5select({
        options: []
	});
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', data, 'json', successJob);
}

function successJob(data) {
	cboJobData = data;	
	/*var tmpObj = {}
	tmpObj.ID = "Cbo_Job";
	tmpObj.cm_jobcd = "ALL";
	tmpObj.cm_jobname = "전체";
	cboJobData.unshift(tmpObj);*/
	
	if (cboJobData != null && (cboJobData.length > 0)) {
		cboJobData = data.filter(function(item) {
			if (item.ID == "Cbo_Job") return true;
			else return false;
		});
		
		$('[data-ax5select="cboJob"]').ax5select({
	        options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
		});
		try {
			if (window.parent != undefined){
				window.parent.$("#cboJob").ax5select({
			        options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
				});
			}
		} catch(e) {
		}
		
		if (progInfoData != null && progInfoData != '' && progInfoData.length>0) $('[data-ax5select="cboJob"]').ax5select('setValue', progInfoData[0].WkJobCd,	true); 	
		else $('[data-ax5select="cboJob"]').ax5select('setValue', 'ALL', true); 	// 업무 초기화
	}
	
	successJawon(data);
}
function successJawon(data) {
	$('[data-ax5select="cboRsrcCd"]').ax5select({
	      options: []
	});
	
	cboRsrcCdData = data;	
	/*var tmpObj = {}
	tmpObj.ID = "Cbo_RsrcCd";
	tmpObj.cm_micode = "ALL";
	tmpObj.cm_codename = "전체";
	cboRsrcCdData.unshift(tmpObj);*/
	
	if (cboRsrcCdData != null && (cboRsrcCdData.length > 0)) {
		cboRsrcCdData = cboRsrcCdData.filter(function(item) {
			if (item.ID == "Cbo_RsrcCd") return true;
			else return false;
		});
		
		$('[data-ax5select="cboRsrcCd"]').ax5select({
	        options: injectCboDataToArr(cboRsrcCdData, 'cm_micode' , 'cm_codename')
		});
		
		/*try {
			if (window.parent != undefined){
				window.parent.$("#cboRsrcCd").ax5select({
			        options: injectCboDataToArr(cboRsrcCdData, 'cm_micode' , 'cm_codename')
				});
			}
		} catch(e) {
		}*/
		if (progInfoData != null && progInfoData != '' && progInfoData.length>0) $('[data-ax5select="cboRsrcCd"]').ax5select('setValue', progInfoData[0].WkRsrcCd, 	true); 	
		else $('[data-ax5select="cboRsrcCd"]').ax5select('setValue', 'ALL', true); 	// 프로그램유형 초기화
	}
}

function getCodeInfo(data) {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('ETCTEAM','SEL','N')
	]);
	cboTeamData = codeInfos.ETCTEAM;

	$('[data-ax5select="cboTeam"]').ax5select({
		options: cboTeamData
	});
	
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successCompile);
}

function successCompile(data) {
	cboCompileData = data;
	cboCompileData = cboCompileData.filter(function(data) {
		if(data.cm_gubun == '01') return true;
		else return false;
	});
	$('[data-ax5select="cboCompile"]').ax5select({
        options: injectCboDataToArr(cboCompileData, 'cm_syscd' , 'cm_compscript')
   	});
	
	cboMakeData = data;
	cboMakeData = cboMakeData.filter(function(data) {
		if(data.cm_gubun == '02') return true;
		else return false;
	});
	$('[data-ax5select="cboMake"]').ax5select({
        options: injectCboDataToArr(cboMakeData, 'cm_syscd' , 'cm_compscript')
   	});
}

function successProgInfo(data) {
	progInfoData = data;
	var i = 0;
	
	$('#btnReOpen').prop('disabled',true);
	$('#btnTblDel').prop('disabled',true);
	$('#btnDel').prop('disabled',true);
	
	console.log('successProgInfo',progInfoData);
	if (progInfoData.length > 0) {
		strInfo = progInfoData[0].cm_info;
		$('#txtSysMsg').val(progInfoData[0].cm_sysmsg);
		$('#txtRsrcCd').val(progInfoData[0].RsrcName);
		$('#txtProgId').val(progInfoData[0].cr_rsrcname);
		$('#txtSta').val(progInfoData[0].sta);
		$('#txtLanguage').val(progInfoData[0].fname);
		$('#txtStory').val(progInfoData[0].Lbl_ProgName);
		$('#txtPath').val(progInfoData[0].cm_dirpath);
		$('#txtCreator').val(progInfoData[0].Lbl_Creator);
		$('#txtEditor').val(progInfoData[0].Lbl_Editor);
		$('#txtCaller').val(progInfoData[0].cr_masname);
		$('#txtCallerToolTip').attr('tooltip',progInfoData[0].cr_master);
		$('#txtLstUsr').val(progInfoData[0].Lbl_LstUsr);
		$('#txtCreatDt').val(progInfoData[0].Lbl_CreatDt);
		$('#txtLastDt').val(progInfoData[0].Lbl_LastDt);
		$('#txtLstDat').val(progInfoData[0].Lbl_LstDat);
						
		if(progInfoData[0].cr_orderid != null && progInfoData[0].cr_orderid != "") {
			$('#txtOrderId').val(progInfoData[0].cr_orderid + ":" + progInfoData[0].cc_reqsub);
		}
		
		for(i = 0; i < cboCompileData.length; i++) {
			if(cboCompileData[i].cm_compcd == progInfoData[0].cr_compile) {
				$("#cboCompile").ax5select('setValue',cboCompileData[i].value,true);
				break;
			}
		}
		
		for(i = 0; i < cboMakeData.length; i++) {
			if(cboMakeData[i].cm_compcd == progInfoData[0].cr_makecompile) {
				$("#cboMake").ax5select('setValue',cboMakeData[i].value,true);
				break;
			}
		}
		
		for(i = 0; i < cboTeamData.length; i++) {
			if(cboTeamData[i].cm_micode == progInfoData[0].cr_teamcd) {
				$("#cboTeam").ax5select('setValue',cboTeamData[i].value,true);
				break;
			}
		}
		
		if(progInfoData[0].cr_document == "01") {
			$("#rdoYes").wRadio("check", true);
		} else if(progInfoData[0].cr_document == "02") {
			$("#rdoNo").wRadio("check", true);
		}
		
		if(progInfoData[0].cr_rpaflag == "Y") {
			$("#rdoRpaYes").wRadio("check", true);
		} else if(progInfoData[0].cr_rpaflag == "N") {
			$("#rdoRpaNo").wRadio("check", true);
		}
		
		strInfo = progInfoData[0].cm_info;
						
		if (cboJobData != null && cboJobData.length > 0) $('[data-ax5select="cboJob"]').ax5select('setValue',progInfoData[0].WkJobCd,true);
		if (cboDirData != null && cboDirData.length > 0) $('[data-ax5select="cboDir"]').ax5select('setValue',progInfoData[0].cr_dsncd,true);
		
		if (progInfoData[0].WkSecu == 'true') {
			$("#divTxtRsrcCd").css('display', 'none');
			$("#divCboRsrcCd").css('display', 'block');
			$('[data-ax5select="cboRsrcCd"]').ax5select("enable");
			$('[data-ax5select="cboEditor"]').ax5select("enable");
			$('[data-ax5select="cboJob"]').ax5select("enable");
			$('[data-ax5select="cboOrderId"]').ax5select("disable");
			$('#txtStory').prop("readonly", false);
			
			$('[data-ax5select="cboRsrcCd"]').ax5select('setValue',progInfoData[0].WkRsrcCd,true);
			
			getDirList(progInfoData[0].WkRsrcCd);
			getEditorList(progInfoData[0].cr_editor);
			
			if(progInfoData[0].cr_status == "3" || progInfoData[0].cr_status == "0") {
				$("#btnTblDel").text("폐기");
				
				if(isQA || adminYN || SecuYn == "Y") {
					//if (Number(progInfoData[0].WkVer) >= 1 && progInfoData[0].WkSta == "0") 
					$("#btnTblDel").prop('disabled', false); 
				} else $("#btnTblDel").prop('disabled', true); 
			} else if(progInfoData[0].cr_status == "9") {
				$("#btnTblDel").text("복원");
				$("#btnTblDel").val("alive");
				btnVal = $("#btnTblDel").val();
				if(SecuYn == "Y") $("#btnTblDel").prop('disabled', false);
			} else {
				$("#btnTblDel").prop('disabled', true);
				$("#btnTblDel").text("폐기");
			}
			
			if(progInfoData[0].cr_status == "3" || progInfoData[0].cr_status == "0" || progInfoData[0].cr_status == "7") {
				$("#btnRegist").prop('disabled', false); //수정
				
				if(progInfoData[0].cr_status == "3" || progInfoData[0].cr_status == "0") {
					if(progInfoData[0].cr_orderid != null && progInfoData[0].cr_orderid != "") {
						$('[data-ax5select="cboOrderId"]').ax5select('enable');
						$("#divTxtOrderId").css('display', 'none');
						$('[data-ax5select="cboOrderId"]').ax5select('setValue',-1,true);
						for(i = 0; i < cboOrderIdData.length; i++) {
							if(cboOrderIdData[i].cc_orderid == progInfoData[0].cr_orderid) {
								$('[data-ax5select="cboOrderId"]').ax5select('setValue',cboOrderIdData[i].value,true);
								break;
							}
						}
						
						if(getSelectedIndex('cboOrderId') > 0) {
							var tmpObj = {};
							tmpObj.orderid = "[" + progInfoData[0].cr_orderid + "]" + progInfoData[0].cc_reqsub;
							tmpObj.cc_reqsub = progInfoData[0].cc_reqsub;
							tmpObj.cc_orderid = progInfoData[0].cr_orderid;
							$('[data-ax5select="cboOrderId"]').ax5select({
						        options: injectCboDataToArr(tmpObj, 'cc_reqsub' , 'cc_orderid')
							});
							
							$('[data-ax5select="cboOrderId"]').ax5select('setValue',-1,true);
							tmpObj = null;
						}
					}
				}
			}
		}
			
		if ((strInfo.substr(9,1) == "0" && strInfo.substr(11,1) == "1") || strInfo.substr(26,1) == "1" || strInfo.substr(3,1) == "1") {
			if(Number(progInfoData[0].WkVer) < 1) {
				if(strInfo.substr(44,2) == "00" && strInfo.substr(26,1) == "0") $("#btnView").prop('disabled', false);
			} else {
				$("#btnView").prop('disabled', false); //소스보기
				if(Number(progInfoData[0].vercnt) < 2) {
					if(strInfo.substr(44,2) == "00" && strInfo.substr(26,1) == "0") $("#btnDiff").prop('disabled', false);
				} else {
					 $("#btnDiff").prop('disabled', false); //소스비교
				}
			}
			$("#btnView").prop('disabled', false);
			
			if(Number(progInfoData[0].WkVer) > 1 && strInfo.substr(45,1) == "0") {
				$("#btnDiff").prop('disabled', false);
			}
		}
		
		if(Number(progInfoData[0].vercnt) > 0 && strInfo.substr(45,1) == "1") {
			$("#btnView").prop('disabled', false); //소스보기
		}
		 
		/*if (progInfoData[0].WkSta == "9"){
		  	$("#btnReOpen").prop('disabled', false);//폐기복구
		}*/
		} 
}

function cboRsrcCd_Change() {
	if (getSelectedIndex('cboRsrcCd') < 1) return;
	if (progInfoData == null || progInfoData.length == 0) return;
	
	var selRsrcCd = getSelectedVal('cboRsrcCd').value;
	var progRsrcCd = progInfoData[0].WkRsrcCd;
	
	if (cboDirData.length == 0 && selRsrcCd == progRsrcCd) return;
	getDirList(selRsrcCd);
}

function getDirList(rsrcCd){
	cboDirData = [];
	$('[data-ax5select="cboDir"]').ax5select({
        options: cboDirData
	});
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId 	 : userId,
		SecuYn 	 : SecuYn,
		L_Syscd  : progInfoData[0].cr_syscd,
		L_ItemId : progInfoData[0].cr_itemid,
		RsrcCd 	 : rsrcCd,
		L_DsnCd  : progInfoData[0].cr_dsncd,
		FindFg 	 : 'false',
		requestType	: 'getDir_Check'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json', successDirList);
}

function successDirList(data) {
	cboDirData = clone(data);
	if(cboDirData != null && cboDirData.length > 0) {
		
		$('[data-ax5select="cboDir"]').ax5select({
	        options: injectCboDataToArr(cboDirData, 'cm_dsncd' , 'cm_dirpath')
		});
		if (null != progInfoData && progInfoData[0].cr_dsncd != null && progInfoData[0].cr_dsncd != undefined) $('[data-ax5select="cboDir"]').ax5select('setValue',progInfoData[0].cr_dsncd,true);
	}
}

function getEditorList(editor){
	cboEditorData = [];
	$('[data-ax5select="cboEditor"]').ax5select({
        options: cboEditorData
	});
	
	tmpInfoData = new Object();
	tmpInfoData = {
		ItemId  : progInfoData[0].cr_itemid,
		Editor  : editor,
		requestType	: 'getCbo_Editor_Add'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json', successEditorList);
}

function successEditorList(data) {
	cboEditorData = data;
	
	if(cboEditorData.length > 0) {
		options = [];
		$.each(cboEditorData,function(key,value) {
			options.push({value: value.cm_userid, text: value.userid});
		});
		
		$('[data-ax5select="cboEditor"]').ax5select({
	        options: options
		});
		$('[data-ax5select="cboEditor"]').ax5select('setValue',progInfoData[0].cr_editor,true);
	}
}

function successUserSelect(name, id) {
	$('#txtCaller').val(name);
	callerId = id;
}

function Check_Yo() {
	if($('#txtStory').val().trim() == "" || $('#txtStory').val().trim().length == 0) {
		dialog.alert('프로그램 기능을 입력하여 주십시오.');
		$('#txtStory').focus();
		return true;
	}
	
	if (getSelectedIndex('cboJob') < 0) {
		dialog.alert('업무를 선택하여 주십시오.');
		return true;
	}
	
	if (getSelectedIndex('cboRsrcCd') < 0) {
		dialog.alert('프로그램종류를 선택하여 주십시오.');
		return true;
	}
	
	if (getSelectedIndex('cboEditor') < 1) {
		dialog.alert('최종변경인을 정확하게 입력하여 주십시오.');
		return true;
	}
	
	if ($('#txtPath').val().trim() == "" || $('#txtPath').val().trim().length == 0) {
		dialog.alert('프로그램경로를 입력하여 주십시오.');
		$('#txtPath').focus();
		return true;
	}
	
	if($("#cboOrderId").attr("disabled") != "disabled") {
		if(getSelectedIndex('cboOrderId') < 0) {
			dialog.alert('발행번호를 선택하여 주십시오.');
			return true;
		}
		if(getSelectedVal('cboOrderId').cc_detcate.substr(0,1) == "9" && getSelectedVal('cboSys').TstSw == "1") {
			dialog.alert('['+ getSelectedVal('cboOrderId').cc_orderid +']은(는) 테스트진행이 없는 요청입니다. \n시스템 또는 ISR정보를 변경하여 주십시오.');
			return true;
		}
	}
	return false;
}

function btnRegist_Click() {
	var findSw = false;
	var strSr = "N";
	var docChk = "";
	var rpaChk = "";
	if (Check_Yo()) return;
	
	if ($('#txtProgId').val().length == 0) return;

	var codeInfos = getCodeInfoCommon([
		new CodeInfo('PGMTYPE','','N')
	]);
	var lstTypeData = codeInfos.PGMTYPE;
	var tmpType = '';
	for(var i=0; i<lstTypeData.length; i++) {
		tmpType + tmpType + '0';
	}
	
	var tmpObj = new Object();
	
	if($("#cboOrderId").attr("disabled") != "disabled" && getSelectedVal('cboOrderId').cc_orderid != progInfoData[0].cr_orderid) {
		tmpObj.orderid = getSelectedVal('cboOrderId').cc_orderid;
		findSw = true;
	}
	
	if(progInfoData[0].WkJobCd != getSelectedVal('cboJob').cm_jobcd) {
		tmpObj.jobcd = getSelectedVal('cboJob').cm_jobcd;
		findSw = true;
	}
	
	if(progInfoData[0].WkRsrcCd != getSelectedVal('cboRsrcCd').cm_micode) {
		tmpObj.rsrccd = getSelectedVal('cboRsrcCd').cm_micode;
		findSw = true;
	}
	
	if(progInfoData[0].Lbl_ProgName != $('#txtStory').val().trim()) {
		tmpObj.story = $('#txtStory').val().trim();
		findSw = true;
	}
	
	if(progInfoData[0].cr_editor != getSelectedVal('cboEditor').value) {
		tmpObj.editor = getSelectedVal('cboEditor').value;
		findSw = true;
	}
	
	if(progInfoData[0].cr_master != callerId) {
		tmpObj.master = callerId;
		findSw = true;
	}
	
	if(progInfoData[0].cr_compile != getSelectedVal('cboCompile').cm_compcd) {
		tmpObj.Compile = getSelectedVal('cboCompile').cm_compcd;
		findSw = true;
	}
	
	if(progInfoData[0].cr_makecompile != getSelectedVal('cboMake').cm_compcd) {
		tmpObj.CompileMake = getSelectedVal('cboMake').cm_compcd;
		findSw = true;
	}
	
	if(progInfoData[0].cr_teamcd != getSelectedVal('cboTeam').cm_micode) {
		tmpObj.Team = getSelectedVal('cboTeam').cm_micode;
		findSw = true;
	}
	
	if($("#rdoYes").is(":checked")) docChk = "01";
	else if($("#rdoNo").is(":checked")) docChk = "02";
	
	if($("#rdoRpaYes").is(":checked")) rpaChk = "Y";
	else if($("#rdoRpaNo").is(":checked")) rpaChk = "N";
	
	if(progInfoData[0].cr_document != docChk) {
		tmpObj.document = docChk;
		findSw = true;
	}
	
	if(progInfoData[0].cr_rpaflag != rpaChk) {
		tmpObj.cr_rpaflag = rpaChk;
		findSw = true;
	}
	
	if(findSw == false) {
		dialog.alert('수정한 내용이 없습니다.');
		return;
	}
	
	tmpObj.itemid = progInfoData[0].cr_itemid;
	tmpObj.userid = userId;
	
	var data = new Object();
	data = {
		etcData : tmpObj,	
		requestType	: 'getTbl_Update'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', data, 'json', successUpdateProg);
}

function successUpdateProg(data) {
	if(Number(data) > 0) {
		dialog.alert('저장 되었습니다.', function(){});
		try {
			if (window.parent != undefined && window.parent.getSql_Qry != undefined){
				window.parent.getSql_Qry();
			} else if (window.parent != undefined && window.parent.getProgInfo != undefined) {
				window.parent.getProgInfo();
			}
		} catch(e) {
		}
	}else {
		dialog.alert('수정처리 중 오류가 발생하였습니다.', function(){});
	}
}

function successGetItem_delete(data) {
	if(Number(data) > 0) {
		dialog.alert('['+$('#txtProgId').val()+'] 삭제처리를 완료하였습니다.', function(){});
	} else {
		dialog.alert('['+$('#txtProgId').val()+'] 삭제처리 중 오류가 발생하였습니다.', function(){});
	}
	try {
		if (window.parent != undefined && window.parent.getSql_Qry != undefined){
			window.parent.getSql_Qry();
		} else if (window.parent != undefined && window.parent.getProgInfo != undefined) {
			window.parent.getProgInfo();
		}
	} catch(e) {
	}
}

function btnTblDel_Click() {
	if ($('#txtProgId').val().length == 0) {
		dialog.alert('선택된 프로그램이 없습니다.', function(){});
		return;	
	}

	confirmDialog.setConfig({
	    title: "상태변경",
	    theme: "info",
	    width: 420	
	});
	confirmDialog.confirm({
		msg: '선택된 프로그램[' + $('#txtProgId').val() + '] 폐기할까요?',
	}, function(){
		if(this.key === 'ok') {	
			var tmpInfo = {
				L_ItemId : progInfoData[0].cr_itemid,
				requestType	: 'getTbl_Delete'
			} 
			ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfo, 'json', successGetTbl_Delete);
		} else {
			dialog.alert('취소되었습니다.');
		}
	});
}
function successGetTbl_Delete(data) {
	if(Number(data) > 0) {
		dialog.alert('['+$('#txtProgId').val()+'] 폐기 완료하였습니다.', function(){});
	} else {
		dialog.alert('['+$('#txtProgId').val()+'] 폐기 중 오류가 발생하였습니다.', function(){});
	}
	try {
		if (window.parent != undefined && window.parent.getSql_Qry != undefined){
			window.parent.getSql_Qry();
		} else if (window.parent != undefined && window.parent.getProgInfo != undefined) {
			window.parent.getProgInfo();
		}
	} catch(e) {
	}
}
//폐기해제
function btnReOpen_Click() {
	if ($('#txtProgId').val().length == 0) return;	

	confirmDialog.setConfig({
	    title: "상태변경",
	    theme: "info",
	    width: 420	
	});
	confirmDialog.confirm({
		msg: '선택된 프로그램[' + $('#txtProgId').val() + '] 복원할까요?',
	}, function(){
		if(this.key === 'ok') {	
			var tmpInfo = {
					L_ItemId : progInfoData[0].cr_itemid,
					editor 	 : userId,
					requestType	: 'getTbl_alive'
				} 
				ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfo, 'json', successGetAlive);
		} else {
			dialog.alert('취소되었습니다.');
		}
	});
}

function successGetAlive(data) {
	if(Number(data) > 0) {
		dialog.alert('['+$('#txtProgId').val()+'] 복원 완료하였습니다.', function(){});
	} else {
		dialog.alert('['+$('#txtProgId').val()+'] 복원 중 오류가 발생하였습니다.', function(){});
	}
	try {
		if (window.parent != undefined && window.parent.getSql_Qry != undefined){
			window.parent.getSql_Qry();
		} else if (window.parent != undefined && window.parent.getProgInfo != undefined) {
			window.parent.getProgInfo();
		}
	} catch(e) {
	}
}

// 20230131 RPA 버튼 활성화
function btnRPA_Click(){
	var data = new Object(); 
	data = {
		itemID : progInfoData[0].cr_itemid,
		requestType : 'btnRPA_Click'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', data, 'json', successbtnRPA_Click);
}

function successbtnRPA_Click(data){
	if(data != null && data != "") {
		if(data > 0) dialog.alert('RPA변경 완료되었습니다.');
		else dialog.alert('RPA변경이 실패되었습니다.');
	}
}

function btnVersion_Click() {
	var data = new Object(); 
	data = {
		itemID : progInfoData[0].cr_itemid,
		userID : isVerSync ? progInfoData[0].cr_editor : getSelectedVal('cboEditor').value,
		Syscd  : progInfoData[0].cr_syscd,
		requestType : 'verSync'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', data, 'json', successVerSync);
}

function successVerSync(data) {
	var resultVer = "";
	resultVer = data;
	
	if(resultVer != null && resultVer != "") {
		if(resultVer == "Y") dialog.alert('버전 동기화가 완료되었습니다.');
		else dialog.alert('버전 동기화가 실패되었습니다.');
	}
}

function btnDiff_Click() {
	if ($('#txtProgId').val().length == 0) return;

	if (progInfoData[0].cm_info.substr(26,1) == '1' || progInfoData[0].cm_info.substr(46,1) == '1') openWindow('R54', '', progInfoData[0].cr_itemid);
	else openWindow('R52', '', progInfoData[0].cr_itemid);
}

function btnView_Click() {
	
	if ($('#txtProgId').val().length == 0) return;
	
	if (progInfoData[0].cm_info.substr(26,1) == '1' || progInfoData[0].cm_info.substr(46,1) == '1') openWindow('R55', '', progInfoData[0].cr_itemid);
	else openWindow('R53', '', progInfoData[0].cr_itemid);
}

function openWindow(reqCd,reqNo,itemId) {
	var nHeight, nWidth, cURL, winName;

	if ( ('proginfo_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = 'proginfo_'+reqCd; 

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value	= itemId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	nHeight = screen.height - 100;
    nWidth  = screen.width - 200;
	if (reqCd == 'R53') {
		cURL = "/webPage/winpop/PopSourceView.jsp";	    
	} else if (reqCd == 'R52') {
		cURL = "/webPage/winpop/PopSourceDiff.jsp";
	} else if (reqCd == 'R55') {
		cURL = "/webPage/winpop/PopSourceViewInf.jsp";
	} else if (reqCd == 'R54') {
		cURL = "/webPage/winpop/PopSourceDiffInf.jsp";
	}
	console.log('openWindow f',f);
	console.log('openWindow cURL='+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}