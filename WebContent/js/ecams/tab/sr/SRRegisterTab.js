/** SRRegisterTab(eCmc0100_tab.mxml) 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 */

var userName 		= window.top.userName;
var userId			= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 		= window.top.userDeptCd;
var reqCd	 		= window.parent.reqCd;
var winSw			= window.parent.winSw;

var srData 			= new Object(); // from SRRegister
var grdPart 		= new ax5.ui.grid();
var grdFile 		= new ax5.ui.grid();
var picker			= new ax5.ui.picker();

var myWin 			= null;

var cboCatTypeData 	= [];
var cboChgTypeData 	= [];
var cboWorkRankData	= [];
var cboReqSecuData 	= [];
var cboDevUserData 	= [];
var grdPartData 	= [];
var grdFileData 	= [];
var srInfoData		= [];

$('[data-ax5select="cboCatType"]').ax5select({ options: [] });
$('[data-ax5select="cboChgType"]').ax5select({ options: [] });
$('[data-ax5select="cboWorkRank"]').ax5select({ options: [] });
$('[data-ax5select="cboReqSecu"]').ax5select({ options: [] });
$('[data-ax5select="cboDevUser"]').ax5select({ options: [] });

var sel_sw = false;
var ing_sw = false;
var ins_sw = false;
var editSw = false;

var strIsrId 	= '';
var strStatus 	= '';
var strAcptNo	= '';
//var strUserId = '';
var strEditor	= '';
var attPath 	= ''; // 첨부파일경로(ptahcd=21)
var strSel		= '';
var resultMSG	= '';

// 조직도팝업 변수
var organizationModal	= new ax5.ui.modal();
var	selDeptSw			= true;
var subSw  				= false;
var popData  			= new Object();
var txtOrg				= ''; // from organizationModal
var selDeptCd			= ''; // (flex: strDept)
var txtUserName			= ''; 
var txtUserId			= '';
var deptName			= '';
var deptCd				= '';

// 파일첨부 변수
var fileUploadModal 	= new ax5.ui.modal();
var fileIndex			= 0;
var TotalFileSize 		= 0;

// 파일업로드 변수 (ComFileUpload에서 사용)
var fileGbn 			= 'U';
var dirGbn 				= '21';
var subDocPath 			= '';
var upFiles 			= [];
var	popCloseFlag 		= false;
var completeReadyFunc	= false;

// 결재 변수
var approvalModal 		= new ax5.ui.modal();
var confirmInfoData 	= null; // to approvalModal
var confirmData			= []; 	// from approvalModal

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
picker.bind(defaultPickerInfo('basic', 'top'));

grdPart.setConfig({
	target : $('[data-ax5grid="grdPart"]'),
	sortable : true,
	multiSort : true,
	multiselect: false,
	header : {
		align : "center"
	},
	page : false,
	body : {
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		}
	},
	columns : [
		{key: "cm_deptname", 	label: "소속부서",  	width: '33%', align: 'center'},
        {key: "cm_username", 	label: "담당개발자",  	width: '33%', align: 'center'},
        {key: "cm_codename", 	label: "상태",  		width: '33%', align: 'center'}
	]
});

grdFile.setConfig({
	target : $('[data-ax5grid="grdFile"]'),
	sortable : true,
	multiSort : true,
	multiselect: false,
	header : {
		align : "center"
	},
	page : false,
	body : {
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
         	if (this.item.cc_savename == null || this.item.cc_savename == '' || this.item.cc_savename == undefined) {
            	confirmDialog.confirm({
        			title: '삭제확인',
        			msg: '[' + this.item.name +'] 파일을 삭제할까요?',
        		}, function(){
        			if(this.key === 'ok') {
        				grdFile.removeRow(grdFile.selectedDataIndexs[0]);
        				grdFileData = grdFile.getList();
        			}
        		});
         	}else {
         		fileDown(attPath+this.item.cc_savename, this.item.name);
         	}
		},
	},
	contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "파일삭제"}
        ],
        popupFilter: function (item, param) {
        	/** 
        	 * return 값에 따른 context menu filter
        	 * 
        	 * return true; -> 모든 context menu 보기
        	 * return item.type == 1; --> type이 1인 context menu만 보기
        	 * return item.type == 1 | item.type == 2; --> type 이 1,2인 context menu만 보기
        	 * 
        	 * ex)
	            	if(param.item.qrycd2 === '01'){
	            		return item.type == 1 | item.type == 2;
	            	}
        	 */
        	grdFile.clearSelect();
        	grdFile.select(Number(param.dindex));

	       	var selIn = grdFile.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	if(param.item.cc_savename == null || param.item.cc_savename == '' || param.item.cc_savename == undefined) return;
        	
        	if(reqCd == 'XX' || strStatus == '1' || strStatus == '3' || strStatus == '5' || strStatus == 'A') return;  	
				 
	        return true;
        },
        onClick: function (item, param) {
        	confirmDialog.confirm({
    			title: '삭제확인',
    			msg: '[' + param.item.name +'] 파일을 삭제할까요?',
    		}, function(){
    			if(this.key === 'ok') {
    				var tmpObj = new Object();
    				if(param.item.cc_srid == null || param.item.cc_division == null || param.item.cc_seq == null) {
    					tmpObj.cc_srid = '';
    					tmpObj.cc_division = '';
    					tmpObj.cc_seq = '';
    				}else {
    					tmpObj.cc_srid = param.item.cc_srid;
    					tmpObj.cc_division = param.item.cc_division;
    					tmpObj.cc_seq = param.item.cc_seq;
    				}
    				
    				grdFileData = [];
    				grdFile.setData([]);
    				deleteDoc(tmpObj);
    			}
    		});
        	grdFile.contextMenu.close();
		}
	},
	columns : [
		{key: "name", 	label: "파일명",  	width: '100%', align: 'left'}
	]
});

$(document).ready(function(){
	// 요청부서
	$('#txtOrg').bind('dblclick', function() {
		findPerson('0');
	});
	
	// 담당개발자
	$('#txtUser').bind('dblclick', function() {
		findPerson('1');
	});
	
	$('#txtUser').bind('keypress', function(event){
		if(event.keyCode==13) {
			getUserCombo('DEVUSER', '', $('#txtUser').val(), '');
		}
	});
	
	// 신규등록
	$('#chkNew').bind('click',function() {
		if($('#chkNew').is(':checked')) {
			screenInit('NEW');
		}else {
			screenInit('');
		}
	});
	
	// 보안요구사항 변경
	$('#cboReqSecu').bind('change', function() {
		cboReqSecu_Change();
	});
	
	// 담당개발자 추가
	$('#btnAddDevUser').bind('click', function() {
		btnAddDevUser_Click();
	});
	
	// 담당개발자 삭제
	$('#btnDelDevUser').bind('click', function() {
		btnDelDevUser_Click();
	});
	
	// 첨부파일
	$('#btnFileAdd').bind('click', function() {
		btnFileAdd_Click();
	});
	
	// 등록
	$('#btnRegister').bind('click', function() {
		btnRegister_Click('I');
	});	
	
	// 수정
	$('#btnUpdate').bind('click', function() {
		btnRegister_Click('U');
	});	
	
	// 반려
	$('#btnDelete').bind('click', function() {
		btnDelete_Click();
	});	
	
	//결재정보
	$('#btnConf').bind('click', function() {
		conf_Click();
	});	
	
	//결재
	$('#btnOk').bind('click', function() {
		ok_Click();
	});

	//반려
	$('#btnCncl').bind('click', function() {
		cncl_Click();
	});

	getData();
	getePath();
	getCodeInfo();
	getDocList('', '');
	gyulChk();
	
	$('#chkNew').wCheck('check', true);
	screenInit('NEW');
	
	completeReadyFunc	= true;
});

function getData() {
	reqCd = window.parent.reqCd;
	
	if (window.parent.srData != null && window.parent.srData != '' && window.parent.srData != undefined) {
		strIsrId = window.parent.srData.strIsrId;
		strEditor = window.parent.srData.strEditor;
		strStatus = window.parent.srData.strStatus;
		strAcptNo = window.parent.srData.strAcptNo;
	}else{
		strIsrId = window.parent.strIsrId;
		strEditor = window.parent.strEditor;
		strStatus = window.parent.strStatus;
		strAcptNo = window.parent.strAcptNo;
	}
}

function getePath() {
	var data = {
		pCode : '21',
		requestType : 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json', successGetTmpDir);
}

function successGetTmpDir(data) {
	attPath = data;
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('CATTYPE','SEL','N','3',''),
		new CodeInfoOrdercd('CHGTYPE','SEL','N','3',''),
		new CodeInfoOrdercd('WORKRANK','SEL','N','3',''),
		new CodeInfoOrdercd('REQSECU','SEL','N','3','')
	]);

	cboCatTypeData = codeInfos.CATTYPE;
	$('[data-ax5select="cboCatType"]').ax5select({
        options: injectCboDataToArr(cboCatTypeData, "cm_micode", "cm_codename")
	});
	
	cboChgTypeData = codeInfos.CHGTYPE;
	$('[data-ax5select="cboChgType"]').ax5select({
        options: injectCboDataToArr(cboChgTypeData, "cm_micode", "cm_codename")
	});
	
	cboWorkRankData = codeInfos.WORKRANK;
	$('[data-ax5select="cboWorkRank"]').ax5select({
        options: injectCboDataToArr(cboWorkRankData, "cm_micode", "cm_codename")
	});
	
	cboReqSecuData = codeInfos.REQSECU;
	$('[data-ax5select="cboReqSecu"]').ax5select({
        options: injectCboDataToArr(cboReqSecuData, "cm_micode", "cm_codename")
	});
}

function getDocList(srid, reqcd) {
	var data = {
		cc_srid : srid,
		reqCd : reqcd,
		requestType : 'getDocList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successGetDocList);
}

function successGetDocList(data) {
	grdFileData = data;
	grdFile.setData(grdFileData);
}

function gyulChk() {
	if(window.parent.srData != null && window.parent.srData != undefined) {
		strAcptNo = window.parent.srData.strAcptNo;
	}
	
	if(strAcptNo != null && strAcptNo != '' && strAcptNo != undefined) {
		var data = {
			AcptNo : strAcptNo,
			UserId : userId,
			requestType : 'gyulChk'
		}
		ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json', successGyulChk);		
	}
}

function successGyulChk(data) {
	console.log('successGyulChk ==>', data);
	if(data == '0') { // 결재자
		$('#divGyul').show();
		$('#divGyulBtn').show();
	}else if (data != '1') {
		dialog.alert('결재정보 체크 중 오류가 발생하였습니다.');
		return;
	} 
}

// SRRegister에서 호출
function screenInit(scrGbn) {
	getData();
	sel_sw = false;
	
	$('#btnFileAdd').prop("disabled", false);
	$('#btnAddDevUser').prop("disabled", false);
	$('#btnDelDevUser').prop("disabled", false);
	
	$('#divGyul').hide();
	$('#divGyulBtn').hide();
	$('#btnConf').hide();
	
	console.log('scrGbn ==> ' + scrGbn + ', strIsrId ==> ' + strIsrId + ', strStatus ==> ' + strStatus);
	if(scrGbn == 'NEW') {
		strIsrId = '';
		
		if(window.parent.reqCd == '41') { // SR접수(41)
			window.parent.clearIdx();
		}
		
		$('#datReqComDate').val('');
		$('#chkNew').wCheck('check', true);
		$('#btnRegister').prop("disabled", false);
		$('#btnUpdate').prop("disabled", true);
		$('#btnDelete').prop("disabled", true);
		$('#txtSRID').val('신규등록');
		$('#txtRegUser').val(userName);
		$('#txtRegDate').val('신규등록');
		
		ins_sw = true;
		
		getUserCombo('REQUSER', '', '', userId);
		getUserCombo('DEVUSER', '', '', userId);
		
	}else if(strIsrId != null && strIsrId != '' && strIsrId != undefined) {
		if(scrGbn == 'M') {
			$('#chkNew').wCheck('check', false);
			$('#btnRegister').prop("disabled", true);
			$('#btnUpdate').prop("disabled", true);
			$('#btnDelete').prop("disabled", true);
		}else {
			// 0: 저장상태(모이라 등록건)
			if(strStatus == '0') {
				$('#btnRegister').prop("disabled", false);
				$('#btnUpdate').prop("disabled", true);
				$('#btnDelete').prop("disabled", false);
			}else if(strStatus == '2' || strStatus == 'C' || strStatus == '4') {
				// 2: 등록완료, C: 진행중, 4: 등록승인중반려
				if(strStatus == '2' || strStatus == 'C') {
					$('#btnDelete').prop("disabled", false);
				}else {
					$('#btnDelete').prop("disabled", true);
				}
				$('#btnRegister').prop("disabled", true);
				$('#btnUpdate').prop("disabled", false);
			}else {
				$('#btnRegister').prop("disabled", true);
				$('#btnUpdate').prop("disabled", true);
				$('#btnDelete').prop("disabled", true);
				
				// 1: 등록승인중, 3: 반려, 5: 적용확인중, A: 완료승인중
				if(strStatus == '1' || strStatus == '3' || strStatus == '5' || strStatus == 'A') {
					$('#btnFileAdd').prop("disabled", true);
					$('#btnAddDevUser').prop("disabled", true);
					$('#btnDelDevUser').prop("disabled", true);
				}
			}
			
			$('#txtSRID').val('');
			$('#txtRegUser').val('');
			$('#txtRegDate').val('');
			ins_sw = false;
		}
	}else {
		$('#txtSRID').val('');
		$('#txtRegUser').val('');
		$('#txtRegDate').val('');
		
		$('#btnRegister').prop("disabled", true);
		$('#btnUpdate').prop("disabled", true);
		$('#btnDelete').prop("disabled", true);
		$('#btnFileAdd').prop("disabled", true);
		$('#btnAddDevUser').prop("disabled", true);
		$('#btnDelDevUser').prop("disabled", true);
	}
	
	$('#txtDocuNum').val('');
	$('#txtOrg').val('');
	$('#txtReqTitle').val('');
	$('#texReqContent').val('');
	$('#txtUser').val('');

	grdFileData = [];
	grdFile.setData([]);
	grdPartData = [];
	grdPart.setData([]);

	if(cboCatTypeData.length > 0) $('[data-ax5select="cboCatType"]').ax5select('setValue', cboCatTypeData[0].value, true);
	if(cboChgTypeData.length > 0) $('[data-ax5select="cboChgType"]').ax5select('setValue', cboChgTypeData[0].value, true);
	if(cboWorkRankData.length > 0) $('[data-ax5select="cboWorkRank"]').ax5select('setValue', cboWorkRankData[0].value, true);
	if(cboReqSecuData.length > 0) $('[data-ax5select="cboReqSecu"]').ax5select('setValue', cboReqSecuData[0].value, true);
	if(cboDevUserData.length > 0) $('[data-ax5select="cboDevUser"]').ax5select('setValue', cboDevUserData[0].value, true);
			
	$('#txtReqSecu').val('');
	$('#txtReqSecu').hide();
	
	$('#chkRealData').wCheck('check', false);
	
	console.log('reqCd ==> ' + reqCd);
	if(reqCd == 'XX') {
		$('#chkNew').wCheck('disabled', true);
		$('#btnRegister').prop("disabled", true);
		$('#btnUpdate').prop("disabled", true);
		$('#btnDelete').prop("disabled", true);
		$('#btnFileAdd').prop("disabled", true);
		$('#btnAddDevUser').prop("disabled", true);
		$('#btnDelDevUser').prop("disabled", true);
	}
	
	if(reqCd == 'XX' || ( $('#btnRegister').is(':disabled') && $('#btnUpdate').is(':disabled') && $('#btnDelete').is(':disabled') ) ) {
		$('[data-ax5select="cboCatType"]').ax5select('disable');
		$('[data-ax5select="cboChgType"]').ax5select('disable');
		$('[data-ax5select="cboWorkRank"]').ax5select('disable');
		$('[data-ax5select="cboReqSecu"]').ax5select('disable');
		
		$('#datReqComDate').prop("disabled", true); 
		disableCal(true, 'datReqComDate');
		$('#chkRealData').wCheck('disabled', true);
		
		$('#txtDocuNum').prop('readonly', true);
		$('#txtReqTitle').prop('readonly', true);
		$('#texReqContent').prop('readonly', true);
		$('#txtReqSecu').prop('readonly', true);
	}else {
		$('[data-ax5select="cboCatType"]').ax5select('enable');
		$('[data-ax5select="cboChgType"]').ax5select('enable');
		$('[data-ax5select="cboWorkRank"]').ax5select('enable');
		$('[data-ax5select="cboReqSecu"]').ax5select('enable');
		
		$('#datReqComDate').prop("disabled", false); 
		disableCal(false, 'datReqComDate');
		$('#chkRealData').wCheck('disabled', false);
		
		$('#txtDocuNum').prop('readonly', false);
		$('#txtReqTitle').prop('readonly', false);
		$('#texReqContent').prop('readonly', false);
		$('#txtReqSecu').prop('readonly', false);
	}
}

function getUserCombo(cbo, sDept, sUser, user) {
	var data = {
		combo_select : cbo,
		search_dept : sDept,
		search_user : sUser,
		UserId : user,
		requestType : 'getUserCombo'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successGetUserCombo);
}

function successGetUserCombo(data) {
	cboDevUserData = data;
	$('[data-ax5select="cboDevUser"]').ax5select({
        options: injectCboDataToArr(cboDevUserData, "cm_userid", "cm_idname")
	});
	
	if(cboDevUserData.length == 2) {
		$('[data-ax5select="cboDevUser"]').ax5select('setValue', cboDevUserData[1].cm_userid, true);
	}
}

function findPerson(gbn) {
	// gbn=0:조직, 1:개발담당자
	
	if( $('#btnRegister').is(':disabled') && $('#btnUpdate').is(':disabled') ) return;
	
	if(gbn == '0') subSw = false;
	else subSw = true;
	
	popData = new Object();
	popData.wingbn = gbn;
	
	setTimeout(function() {
		organizationModal.open({
			width: 400,
			height: 500,
			iframe: {
				method: "get",
				url: "../../modal/OrganizationModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					//mask.open();
				}else if (this.state === "close") {
					//mask.close();
					
					if(popData.wingbn == '0') {
						console.log('조직도팝업 close ==> ', txtOrg, selDeptCd);
					}else {
						cboDevUserData 	= [
							{cm_userid: '0000', cm_username: txtUserName, cm_idname: '선택하세요', cm_deptcd: '0000', cm_deptname: ''},
							{cm_userid: txtUserId, cm_username: txtUserName, cm_idname: txtUserName+'['+txtUserId+']', cm_deptcd: deptCd, cm_deptname: deptName},
						];
						
						$('[data-ax5select="cboDevUser"]').ax5select({
					        options: injectCboDataToArr(cboDevUserData, "cm_userid", "cm_idname")
						});
						$('[data-ax5select="cboDevUser"]').ax5select('setValue', cboDevUserData[1].cm_userid, true);
						btnAddDevUser_Click();
					}
				}
			}
		}, function () {
		});
	}, 200);
}

function cboReqSecu_Change() {
	if(getSelectedVal('cboReqSecu').cm_micode == '6') {
		$('#txtReqSecu').show();
	}else {
		$('#txtReqSecu').val('');
		$('#txtReqSecu').hide();
	}
}

// 개발자 추가
function btnAddDevUser_Click() {
	if(getSelectedIndex('cboDevUser') < 1) return;
	
	for(var i=0; i<grdPartData.length; i++) {
		if(grdPartData[i].cc_userid == getSelectedVal('cboDevUser').cm_userid) {
			return;
		}
	}
	
	var addData = new Object();
	addData.cc_userid = getSelectedVal('cboDevUser').cm_userid;
	addData.cm_username = getSelectedVal('cboDevUser').cm_username;
	addData.cm_deptcd = getSelectedVal('cboDevUser').cm_deptcd;
	addData.cm_deptname = getSelectedVal('cboDevUser').cm_deptname;
	addData.delyn = 'OK';
	
	grdPartData = grdPart.getList();
	grdPartData.push(addData);
	grdPart.setData(grdPartData);
	addData = null;
	
}

// 개발자 삭제
function btnDelDevUser_Click() {
	if(grdPart.selectedDataIndexs.length < 1) {
		dialog.alert('삭제 할 대상을 선택한 후 진행하시기 바랍니다.');
		return;
	}
	
	var setGridItem = grdPart.getList("selected")[0];
	if(setGridItem.delyn != 'OK') {
		dialog.alert('['+setGridItem.cm_username+']는 개발진행 중이므로 삭제가 불가합니다.');
		return;
	}
	
	grdPart.removeRow(grdPart.selectedDataIndexs[0]);
	grdPartData = grdPart.getList();
}

function grdPrj_click(srid) {
	var data = {
		cc_srid : srid,
		requestType : 'selectSRInfo'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successGetSRInfo);
}

function successGetSRInfo(data) {
	$('#chkNew').wCheck('check', false);
	screenInit('');
	sel_sw = true;
	
	srInfoData = data;
	$('#txtSRID').val(srInfoData[0].cc_srid);
	if(strStatus == '0') $('#txtRegUser').val(userName);
	else $('#txtRegUser').val(srInfoData[0].createuser);
	$('#txtRegDate').val(srInfoData[0].createdate);
	//14.07.14 siruen SR등록 화면에서, SR 클릭시 요청부서/요청인 에 요청부서만 뜨는 문제 해결
	selDeptCd = srInfoData[0].cc_reqdept;
	$('#txtDocuNum').val(srInfoData[0].cc_docid);
	if(srInfoData[0].acptno != null && srInfoData[0].acptno != undefined) {
		$('#btnConf').show();
	}
	if(strStatus == '0' || strStatus == '1' || strStatus == '2' || strStatus == '4') editSw = true;
	else editSw = false;
	
	if(editSw && strEditor == userId) {
	}else {
		$('#txtOrg').prop('readonly', false);
	}
	$('#txtOrg').val(srInfoData[0].reqdept);
	$('#txtReqTitle').val(srInfoData[0].cc_reqtitle);
	
	var tmpDate = srInfoData[0].cc_reqcompdate;
	if(tmpDate == null || tmpDate == '' || tmpDate == undefined) {
		$('#datReqComDate').val('');
	}else {
		$('#datReqComDate').val(srInfoData[0].cc_reqcompdate);
		picker.bind(defaultPickerInfo('basic', 'top'));
	}
	$('#texReqContent').val(srInfoData[0].cc_content);
	$('[data-ax5select="cboCatType"]').ax5select('setValue', srInfoData[0].cc_cattype, true);
	$('[data-ax5select="cboChgType"]').ax5select('setValue', srInfoData[0].cc_chgtype, true);
	$('[data-ax5select="cboWorkRank"]').ax5select('setValue', srInfoData[0].cc_workrank, true);
	$('[data-ax5select="cboReqSecu"]').ax5select('setValue', srInfoData[0].cc_reqsecu, true);
	
	if(getSelectedVal('cboReqSecu').cm_micode == '6') {
		$('#txtReqSecu').show();
		$('#txtReqSecu').val(srInfoData[0].cc_txtreqsecu);
	}
	
	if(srInfoData[0].cc_realdatause == 'Y') {
		$('#chkRealData').wCheck('check', true);
	}else {
		$('#chkRealData').wCheck('check', false);
	}
	
	if(reqCd == 'XX' || ( $('#btnRegister').is(':disabled') && $('#btnUpdate').is(':disabled') && $('#btnDelete').is(':disabled') ) ) {
		$('[data-ax5select="cboCatType"]').ax5select('disable');
		$('[data-ax5select="cboChgType"]').ax5select('disable');
		$('[data-ax5select="cboWorkRank"]').ax5select('disable');
		$('[data-ax5select="cboReqSecu"]').ax5select('disable');
		
		$('#datReqComDate').prop("disabled", true); 
		disableCal(true, 'datReqComDate');
		$('#chkRealData').wCheck('disabled', true);
	}else {
		$('[data-ax5select="cboCatType"]').ax5select('enable');
		$('[data-ax5select="cboChgType"]').ax5select('enable');
		$('[data-ax5select="cboWorkRank"]').ax5select('enable');
		$('[data-ax5select="cboReqSecu"]').ax5select('enable');
		
		$('#datReqComDate').prop("disabled", false); 
		disableCal(false, 'datReqComDate');
		$('#chkRealData').wCheck('disabled', false);
	}
	
	getDocList($('#txtSRID').val(), '41');
	getDevUserList($('#txtSRID').val(), userId);
}

function getDevUserList(srid, user) {
	var data = {
		cc_srid : srid,
		userid : user,
		requestType : 'getDevUserList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successGetDevUserList);
}

function successGetDevUserList(data) {
	grdPartData = data;
	grdPart.setConfig();
	grdPart.setData(grdPartData);
	grdPart.repaint();
	
	var delCnt = 0;
	for(var i=0; i<grdPartData.length; i++) {
		if(grdPartData[i].delyn != 'OK') {
			delCnt++;
			break;
		}
	}
	
	if( (strStatus == '0' || strStatus == '2' || strStatus == '4' || strStatus == 'C') && delCnt == 0 && reqCd != 'XX') {
		$('#btnDelete').prop("disabled", false);
	}else {
		$('#btnDelete').prop("disabled", true);
	}
}

// 첨부파일 삭제
function deleteDoc(data) {
	var data = {
		tmp_obj : data,
		requestType : 'deleteDoc'
	}
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successDeleteDoc);
}

function successDeleteDoc(data) {
	if(data == 'OK') {
		getDocList($('#txtSRID').val(), '41');
	}
}

//첨부파일
function btnFileAdd_Click() {
	if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
		fileIndex++;
		$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
	}
	$("#file"+fileIndex).click();
}

function fileChange(file) {
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	var spcChar = "{}<>?|~`!@#$%^&*+\"'\\/";
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 
		var fileCk = true;
		
		for(var i=0; i<jqueryFiles.files.length; i++){
			var sizeCount = 0;
			var size = jqueryFiles.files[i].size/1024; // Byte, KB, MB, GB
			while(size > 1024 && sizeCount < 2){
				size = size/1024;
				sizeCount ++;
			}
			size = Math.round(size*10)/10.0 + fileSizeArr[sizeCount];
			var sizeReal = jqueryFiles.files[i].size;
			var name = jqueryFiles.files[i].name
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				fileCk = false;
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				fileCk = false;
				break;
			}
			
			for(var j=0; j<grdFileData.length; j++){

				for (k=0;spcChar.length>k;k++) {
					if (name.indexOf(spcChar.substr(k,1))>=0) {
						dialog.alert("첨부파일명에 특수문자를 입력하실 수 없습니다.\n특수문자를 제외하고 첨부하여 주시기 바랍니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
				}
				if(!fileCk){
					break;
				}
				
				if(grdFileData[j].name == name){
					dialog.alert("파일명 : " + name +"\n 은 이미 추가 되어 있는 파일명 입니다.\n확인 후 다시 등록해 주세요");
					$("#"+file).remove();
					fileCk = false;
					break;
				}
				fileCk = true;
			}

			if(fileCk){
				var tmpObj = new Object(); // 그리드에 추가할 파일 속성
				tmpObj.name = name;
				tmpObj.size = size;
				tmpObj.sizeReal = sizeReal;
				tmpObj.newFile = true;
				tmpObj.realName = name;
				tmpObj.file = jqueryFiles.files[i];
				
				grdFileData.push(tmpObj);
			}
			else{
				break;
			}
		}
		grdFile.setData(grdFileData);
		if(fileCk){
			dialog.alert("파일첨부 시 등록/수정 버튼을 클릭해야 파일이 저장됩니다.");
		}
	}
}

// 등록
function btnRegister_Click(gbn) {
	// gbn I:등록, U: 수정
	if($('#txtOrg').val().trim().length == 0) {
		dialog.alert("요청부서를 선택하여 주시기 바랍니다.");
		return;
	}
	
	if($('#txtReqTitle').val().trim().length == 0) {
		dialog.alert("요청제목을 입력하여 주시기 바랍니다.");
		return;
	}
	
	if($('#texReqContent').val().trim().length == 0) {
		dialog.alert("상세내용을 입력하여 주시기 바랍니다.");
		return;
	}
	
	if(getSelectedIndex('cboCatType') < 1) {
		dialog.alert("분류유형을 선택하여 주시기 바랍니다.");
		return;
	}
	
	if(getSelectedIndex('cboChgType') < 1) {
		dialog.alert("변경종류를 선택하여 주시기 바랍니다.");
		return;
	}
	
	if(getSelectedIndex('cboWorkRank') < 1) {
		dialog.alert("작업순위를 선택하여 주시기 바랍니다..");
		return;
	}
	
	if(getSelectedVal('cboReqSecu').cm_micode == '6') {
		if($('#txtReqSecu').val().trim().length == 0) {
			dialog.alert("보안요구사항을 입력하여 주시기 바랍니다.");
			return;
		}
	}
	
	if($('#datReqComDate').val().trim().length == 0) {
		dialog.alert("완료요청일을 선택하여 주시기 바랍니다.");
		return;
	}
	
	var nowDate = getDate('DATE',0);
	var strDate = replaceAllString($("#datReqComDate").val(), '/', '');
	
	if(gbn == 'I' && nowDate > strDate) {
		dialog.alert("완료요청일이 현재일 이전입니다. 정확히 선택하여 주십시오.");
		return;
	}
	
	grdPartData = grdPart.getList();
	if(grdPartData.length < 1) {
		dialog.alert("담당개발자를 추가하여 주시기 바랍니다.");
		return;
	}
	
	if(gbn == 'I') {
		strSel = '등록';
		if(strStatus == '0') ins_sw = false;
		else ins_sw = true;
	}else {
		strSel = '수정';
		ins_sw = false;
	}		
	
	if(ing_sw) {
		dialog.alert("현재 작업이 진행 중 입니다. 잠시 후 다시 시도해 주세요");
		return;
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: '현재 내용으로 ' + strSel +' 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			confirmInfoData = null;
			if(strStatus == '0' || strStatus == 'C' || $('#chkNew').is(':checked')) {
				confSelect();
			} else {
				confirmEnd();
			}
		}
	});
}

function confSelect() {
	var tmpData = {
		SysCd : '99999',
		ReqCd : reqCd,
		RsrcCd : '',
		UserId : userId,
		QryCd : '',
		requestType : 'confSelect'
	}	
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	if (ajaxReturnData != undefined && ajaxReturnData != null) {
		if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData.substr(5));
			ing_sw = false;
			return;
		}
	}
	
	if(ajaxReturnData == 'Y') {
		confirmInfoData = new Object();
		confirmInfoData.UserId = userId;
		confirmInfoData.ReqCd = reqCd;
		confirmInfoData.SysCd = '99999';
		confirmInfoData.Rsrccd = '';
		confirmInfoData.QryCd = '';
		confirmInfoData.EmgSw = '0';
		confirmInfoData.JobCd = '';
		confirmInfoData.deployCd = '';
		confirmInfoData.PrjNo = '';
		confirmInfoData.svryn = '';
		confirmInfoData.OutPos = '';
		console.log('[SRRegisterTab.js] confirmInfoData ==>', confirmInfoData);
		
		confirmData = [];
		setTimeout(function() {
			approvalModal.open({
		        width: 850,
		        height: 550,
		        iframe: {
		            method: "get",
		            url: "../../modal/request/ApprovalModal.jsp"
			    },
		        onStateChanged: function () {
		            if (this.state === "open") {
		                mask.open();
		            }
		            else if (this.state === "close") {
		            	if(confirmData.length > 0) {
		            		confirmEnd();
		            	}else {
		            		ing_sw = false;
		            	}
		                mask.close();
		            }
		        }
			});
		}, 200);
		
	}else {
		confirmEnd();
	}
}

function confirmEnd() {
	var tmpObj = new Object();
	tmpObj.reqcd = reqCd;		
	tmpObj.cc_createuser = userId;
	tmpObj.cc_lastupuser = userId;
	tmpObj.cc_docid = $('#txtDocuNum').val().trim();
	tmpObj.cc_reqdept = selDeptCd;
	tmpObj.cc_requser = $('#txtOrg').val();
	tmpObj.cc_requser = null;
	tmpObj.cc_reqtitle = $('#txtReqTitle').val().trim();
	tmpObj.cc_reqcompdate = $('#datReqComDate').val();
	tmpObj.cc_content = $('#texReqContent').val().trim();
	tmpObj.cc_cattype = getSelectedVal('cboCatType').cm_micode;
	tmpObj.cc_chgtype = getSelectedVal('cboChgType').cm_micode;
	tmpObj.cc_excdate = null;
	tmpObj.cc_exptime = null;
	tmpObj.cc_workrank = getSelectedVal('cboWorkRank').cm_micode;
	tmpObj.cc_devdept = '';
	if(getSelectedVal('cboReqSecu').cm_micode == '00') {
		tmpObj.cc_reqsecu = '0';
	}else {
		tmpObj.cc_reqsecu = getSelectedVal('cboReqSecu').cm_micode;
	}
	if(getSelectedVal('cboReqSecu').cm_micode != '6') {
		tmpObj.cc_reqsecutxt = '';
	}else {
		tmpObj.cc_reqsecutxt = $('#txtReqSecu').val().trim();
	}
	if($('#chkRealData').is(':checked')) {
		tmpObj.cc_realdatause = 'Y';
	}else {
		tmpObj.cc_realdatause = 'N';
	}
	
	if(ins_sw) {
		ins_sw = true;
		insertSRInfo(tmpObj, grdPartData, confirmData);
	}else {
		ins_sw = true;
		tmpObj.cc_srid = $('#txtSRID').val();
		tmpObj.cc_status = strStatus;
		var data = {
			tmp_obj : tmpObj,
			devuser : grdPartData,
			ConfList : confirmData,
			requestType : 'updateSRInfo'
		}
		console.log('[SRRegisterTab.js] updateSRInfo ==>', data);
		ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successSetSRInfo);
	}
	tmpObj = null;
}

function insertSRInfo(obj, usrData, confData) {
	var data = {
		tmp_obj : obj,
		devuser_ary : usrData,
		ConfList : confData,
		requestType : 'insertSRInfo'
	}
	console.log('[SRRegisterTab.js] insertSRInfo ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successSetSRInfo);
}

function successSetSRInfo(data) {
	if(data != null && data != "") {
		strIsrId = data;
		if(grdFileData.length > 0) {
			startFileupload();
		}else {
			dialog.alert(strSel + '이 완료되었습니다.');
			screenInit('NEW');
			ing_sw = false;
			window.parent.subCmdQry_Click();
		}
	} else {
		dialog.alert('SR등록에 실패하였습니다.');
	}
}

function startFileupload() {
	var fileseq = 0;
	
	fileGbn = 'U';
	dirGbn = '21';
	popCloseFlag = false;
	
	//attPath = 'C:\\html5\\temp\\';
	
	for(var i=0; i<grdFileData.length; i++) {
		grdFileData[i].sendflag = false;
		
		if(grdFileData[i].cc_seq == null || grdFileData[i].cc_seq == '' || grdFileData[i].cc_seq == undefined) {
			if(fileseq < 10) {
				subDocPath = '/SR/' + strIsrId.substr(1,6);
				grdFileData[i].savefile = subDocPath + '/' + strIsrId + '_' + reqCd + '_0' + fileseq;
				grdFileData[i].saveFileName = strIsrId + '_' + reqCd + '_0' + fileseq;
				grdFileData[i].fileseq = '0' + fileseq;
			}else {
				subDocPath = '/SR/' + strIsrId.substr(1,6);
				grdFileData[i].savefile = subDocPath + '/' + strIsrId + '_' + reqCd + '_' + fileseq;
				grdFileData[i].saveFileName = strIsrId + '_' + reqCd + '_' + fileseq;
				grdFileData[i].fileseq = '' + fileseq;
			}
		}else {
			fileseq = grdFileData[i].cc_seq;
			grdFileData[i].fileseq = grdFileData[i].cc_seq;
		}
		
		grdFileData[i].attfile = grdFileData[i].name;
		grdFileData[i].fullName = attPath+subDocPath;
		grdFileData[i].fullpath = attPath;
		++fileseq;
	}
	
	upFiles = clone(grdFileData);
	console.log('[SRRegisterTab.js] upFiles==>',upFiles);
	
	setTimeout(function() {
		fileUploadModal.open({
			width: 685,
			height: 420,
			iframe: {
				method: "get",
				url: "../../modal/fileupload/ComFileUpload.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
				}
			}
		});
	}, 200);
}


function setFile(fileList) {
	var data = {
		srid : strIsrId,
		reqcd : reqCd,
		userid : userId,
		doc_list: fileList,
		requestType : 'insertDocList'
	}
	console.log('[SRRegisterTab.js] insertDocList ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successInsertDocList);
}

function successInsertDocList(data) {
	dialog.alert(strSel + '이 완료되었습니다.');
	screenInit('NEW');
	ing_sw = false;
	window.parent.subCmdQry_Click();
}

// 반려
function btnDelete_Click() {
	if($('#txtSRID').val() == '' || $('#txtSRID').val() == null || $('#chkNew').is(':checked')) {
		dialog.alert('삭제 할 SR을 선택 해 주세요.');
		return;
	}
	
	confirmDialog.confirm({
		title: '삭제확인',
		msg: '선택한 SR [' + $('#txtSRID').val()+']을 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			ing_sw = true;
			var data = {
				userid : userId,
				srid : $('#txtSRID').val(),
				requestType : 'deleteSRInfo'
			}
			ajaxAsync('/webPage/ecmc/Cmc0100Servlet', data, 'json', successDeleteSRInfo);
		}
	});
}

function successDeleteSRInfo(data) {
	dialog.alert('삭제가 완료되었습니다.');
	screenInit('NEW');
	ing_sw = false;
	window.parent.subCmdQry_Click();
}

// 결재버튼 클릭
function ok_Click() {
	if(ing_sw) {
		dialog.alert('현재 신청하신 다른 내용을 처리 중입니다.');
		return;
	}
	
	ing_sw = false;
	
	confirmDialog.confirm({
		title: '결재확인',
		msg: '결재처리 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			ing_sw = true;
			
			$('#txtConMsg').val($('#txtConMsg').val().trim());
			resultMSG = '결재';
			nextConf('1');
		}
	});
}

// 반려버튼 클릭
function cncl_Click() {
	if(ing_sw) {
		dialog.alert('현재 신청하신 다른 내용을 처리 중입니다.');
		return;
	}
	
	ing_sw = false;
	
	$('#txtConMsg').val($('#txtConMsg').val().trim());
	if($('#txtConMsg').val().length == 0) {
		dialog.alert('반려의견을 입력하여 주십시오.');
		return;
	}
	
	confirmDialog.confirm({
		title: '반려확인',
		msg: '반려처리 하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			ing_sw = true;
			
			resultMSG = '반려';
			nextConf('3');
		}
	});
}

function nextConf(cd) {
	var data = {
		AcptNo : strAcptNo,
		UserId : userId,
		conMsg : $('#txtConMsg').val(),
		Cd : cd,
		ReqCd : reqCd,
		requestType : 'nextConf'
	}
	console.log('[nextConf] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json', successNextConf);
}

function successNextConf(data) {
	console.log('[successNextConf] ==>', data);
	ing_sw = false;
	
	if(data == '0') {
		popClose();
	}else {
		dialog.alert('[' + data + ']처리에 실패하였습니다.');
		screenInit('S');
		grdPrj_click(strIsrId);
	}
}

function popClose() {
	if(winSw) {
		try {
			window.parent.popClose();
		}catch (e) {
		}
	}
}

// 결재정보
function conf_Click() {
	var nHeight, nWidth, cURL, winName;

	if ( ('pop_'+srInfoData[0].acptno) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = 'pop_'+srInfoData[0].acptno;

    nWidth  = 1046;
	nHeight = 480;
	cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	
	var f = document.popPam;
    f.user.value 	= userId;
    f.acptno.value	= srInfoData[0].acptno;
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}