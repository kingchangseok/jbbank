/**
 * 결재현황 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-05
 * 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드
var rgtList     = window.top.rgtList;      	

var approGrid		= new ax5.ui.grid();

var approGridData 	= [];
var cboTeamData		= [];
var cboSinData		= [];
var cboStaData		= [];

var allCheck = "";
var gyulChk = "";

var myWin = null;
 
approGrid.setConfig({
    target: $('[data-ax5grid="approGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	$('#lblConf').text('');
        	$('#lblConf').hide();
        	$('#txtConMsg').hide();
        	
        	this.self.clearSelect();
            this.self.select(this.dindex);
        	
            if(this.item.endyn == '3' || this.item.endyn == '9') {
            }else {
            	var ajaxData = new Object();
        		var reqType = '';
            	if(getSelectedVal('cboSta').cm_micode == '05') {
            		reqType = 'gyulChk_bujang';
        			ajaxData = {
    					AcptNo : this.item.cr_acptno,
    					UserId : userId,
         				requestType : reqType
             		};
            	}else {
            		reqType = 'gyulChk';
        			ajaxData = {
    					AcptNo : this.item.cr_acptno,
    					UserId : userId,
         				requestType : reqType
             		};
            	}
            	
            	var gyulChk = ajaxCallWithJson('/webPage/ecmr/Cmr3100Servlet', ajaxData, 'json');
        		console.log('gyulChk==>',gyulChk);
        		
        		if(reqType == 'gyulChk_bujang') {
        			if (gyulChk == "Y") {
        				$('#lblConf').text('결재/반려의견');
            			$('#lblConf').show();
            			$('#txtConMsg').show();
            			$('#btnGyul').prop("disabled", false);
    					$('#btnCncl').prop("disabled", false);
        			}else {
        				$('#lblConf').text('');
        				$('#lblConf').hide();
            			$('#txtConMsg').hide();
            			$('#btnGyul').prop("disabled", true);
    					$('#btnCncl').prop("disabled", true);
        			}
        		}else { //gyulChk
        			if (gyulChk == "0") {
        				$('#lblConf').text('결재/반려의견');
            			$('#lblConf').show();
            			$('#txtConMsg').show();
            			if(this.item.enabledyn == 'false') {
            				$('#btnGyul').prop("disabled", true);
        					$('#btnCncl').prop("disabled", true);
            			}else if(this.item.endyn == '0' && this.item.signteamcd != '8') {
            				$('#btnGyul').prop("disabled", false);
        					$('#btnCncl').prop("disabled", false);
            			}else {
            				$('#btnGyul').prop("disabled", false);
        					$('#btnCncl').prop("disabled", false);
            			}
        			}else if(gyulChk == "2") {
        				$('#lblConf').text('반려의견');
        				$('#lblConf').show();
            			$('#txtConMsg').show();
            			dialog.alert('해당 ISR-ID로 진행해야 할 건들이 동일한 단계까지 진행되지 않았습니다. 반려만 가능합니다.');
        			}else if(gyulChk != "1") {
        				dialog.alert('결재정보 체크 중 오류가 발생하였습니다.');
        			}
        		}
            }
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;
 			openWindow(1, this.item.cr_acptno, this.item.cr_qrycd,'');
        },
    	trStyleClass: function () {
     		if(this.item.colorsw === '3'){
    			return "fontStyle-cncl";
     		} 
     		if(this.item.colorsw === '5'){
    			return "fontStyle-error";
     		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "결재요청내용확인"},
            {type: 2, label: "결재정보"},
            {type: 3, label: "결재"},
            {type: 4, label: "반려"},
            {type: 5, label: "Q.A결재시 내용확인 후 결재가능"}
        ],
        popupFilter: function (item, param) {
         	approGrid.clearSelect();
         	approGrid.select(Number(param.dindex));
       	 
	       	/*var selIn = approGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;*/
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	if (param.item.endyn == '3' || param.item.endyn == '9'){
        		return item.type == 1 | item.type == 2;
        	} else {
        		var ajaxData = new Object();
        		var reqType = '';
        		if(getSelectedVal('cboSta').cm_micode == '05') {
        			reqType = 'gyulChk_bujang';
        			ajaxData = {
    					AcptNo : param.item.cr_acptno,
    					UserId : userId,
         				requestType : reqType
             		};
        		}else {
        			reqType = 'gyulChk';
        			ajaxData = {
    					AcptNo : param.item.cr_acptno,
    					UserId : userId,
         				requestType : reqType
             		};
        		}
        		
        		gyulChk = ajaxCallWithJson('/webPage/ecmr/Cmr3100Servlet', ajaxData, 'json');
        		console.log('gyulChk==>',gyulChk);
        		
        		if(reqType == 'gyulChk_bujang') {
        			if (gyulChk == "Y") {
        				$('#lblConf').text('결재/반려의견');
            			$('#lblConf').show();
            			$('#txtConMsg').show();
            			$('#btnGyul').prop("disabled", false);
    					$('#btnCncl').prop("disabled", false);
    					return item.type == 1 | item.type == 2 | item.type == 3 | item.type == 4;
        			}else {
        				$('#lblConf').text('');
        				$('#lblConf').hide();
            			$('#txtConMsg').hide();
            			$('#btnGyul').prop("disabled", true);
    					$('#btnCncl').prop("disabled", true);
        			}
        		}else { //gyulChk
        			if (gyulChk == "0") {
        				$('#lblConf').text('결재/반려의견');
            			$('#lblConf').show();
            			$('#txtConMsg').show();
            			if(param.item.enabledyn == 'false') {
            				return item.type == 1 | item.type == 2 | item.type == 5;
            				$('#btnGyul').prop("disabled", true);
        					$('#btnCncl').prop("disabled", true);
            			}else if(param.item.endyn == '0' && param.item.signteamcd != '8') {
            				return item.type == 1 | item.type == 2 | item.type == 3 | item.type == 4;
            				$('#btnGyul').prop("disabled", false);
        					$('#btnCncl').prop("disabled", false);
            			}else {
            				return item.type == 1 | item.type == 2 | item.type == 3;
            				$('#btnGyul').prop("disabled", false);
        					$('#btnCncl').prop("disabled", false);
            			}
        			}else if(gyulChk == "2") {
        				$('#lblConf').text('반려의견');
        				$('#lblConf').show();
            			$('#txtConMsg').show();
            			dialog.alert('해당 ISR-ID로 진행해야 할 건들이 동일한 단계까지 진행되지 않았습니다. 반려만 가능합니다.');
            			return item.type == 1 | item.type == 2 | item.type == 4;
        			}else if(gyulChk == "1") {
        				return item.type == 1 | item.type == 2;
        			}else if(gyulChk != "1") {
        				dialog.alert('결재정보 체크 중 오류가 발생하였습니다.');
        			}
        		}
        	}
        },
        onClick: function (item, param) {
        	if(item.type == 1 || item.type == 2) {
        		openWindow(item.type, param.item.cr_acptno, param.item.cr_qrycd, '');
            	approGrid.contextMenu.close();
        	} else if (item.type == 3) {
				confirmDialog.setConfig({
    		        title: "결재확인",
    		        theme: "info"
    		    });
    			confirmDialog.confirm({
    				msg: '결재처리하시겠습니까?',
    			}, function(){
    				//Cmr3100.nextConf(param.item.cr_acptno,strUserId, txtConMsg.text, "3",param.item.cr_qrycd);
    				if(this.key === 'ok') {
						nextConf(param.item.cr_acptno, param.item.cr_qrycd,  $('#txtConMsg').val().trim(), "1");
    				}
    			});
        	} else if (item.type == 4) {
        		if ($('#txtConMsg').val().trim() == null || $('#txtConMsg').val().trim() == "") {
        			dialog.alert("반려의견을 입력하여 주십시오.");
        		} else {
        			confirmDialog.setConfig({
        		        title: "반려확인",
        		        theme: "info"
        		    });
        			confirmDialog.confirm({
        				msg: '반려처리하시겠습니까?',
        			}, function(){
        				if(this.key === 'ok') {
        					nextConf(param.item.cr_acptno, param.item.cr_qrycd, $('#txtConMsg').val().trim(), "3");
        				}
        			});
        		}
			}else if(item.type == 5) {
				openWindow(1, param.item.cr_acptno, param.item.cr_qrycd, '');
			}
		}
    },
    columns: [
    	{key: "sayu",		label: "신청내용"		, width: "10%", 	align: "left"},
    	{key: "acptdate",	label: "신청일시"		, width: "9%", 		align: "center"},
    	{key: "prcdate",	label: "적용일시"		, width: "9%", 		align: "center"},
    	{key: "deptname",	label: "요청파트"		, width: "5%", 		align: "left"},
    	{key: "editor",		label: "요청인"		, width: "5%", 		align: "center"},
    	{key: "sta",		label: "상태"			, width: "9%", 		align: "left"},
		{key: "cm_sysmsg",	label: "시스템"		, width: "10%", 	align: "left"},
		{key: "confdate",	label: "결재일시"		, width: "9%", 		align: "center"},
		{key: "qrycd",		label: "결재사유"		, width: "9%", 		align: "center"},
		{key: "acptno",		label: "신청번호"		, width: "9%", 		align: "center"},
    ]
});

$(document).ready(function() {
	if($('#userId').val().length > 0) {
		userId = $('#userId').val();
	}
	
	screenInit();
	getUserB();
	getTeamInfo();
	getCodeInfo();
	
	// 진행상태 콤보 변경
	$('#cboSta').bind('change', function() {
		cboSta_change();		
	});
	
	// 조회 버튼 클릭
	$('#btnQry').bind('click', function() {
		getRequestList();
	});
	
	// 신청인 검색
	$('#txtUser').bind('keypress', function(event){
		if(event.keyCode==13) {
			event.keyCode=0;
			$('#btnQry').trigger('click');
		}
	});
	
	// 일괄결재 버튼 클릭
	$('#btnGyul').bind('click', function() {
		btnGyul_Click();
	});
	
	// 일괄반려 버튼 클릭
	$('#btnCncl').bind('click', function() {
		btnCncl_Click();
	});

	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		approGrid.exportExcel('결재현황.xls');
	});

	var oldVal = "";
	$('#datEdD').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#datEdD").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
});

$(document).on('click','[data-ax5grid-column-key="checked"] span', function(){
	$(this).toggleClass("checked");
	
	if(allCheck == true){
		allCheck = "";
	} else {
		allCheck = true;
	}
	
	// 변화 감지 변수
	var repaintCk = false;
	
	//리스트 체크박스 체크 선택or 해제
	$(approGrid.list).each(function(){
		if(this.checkBoxYN){
			this.checked = allCheck;
			repaintCk = true;
		}
	});
	//변화시 repaint
	if(repaintCk){
		approGrid.repaint();
	}
});

$('[data-ax5grid-container="header"]').on('DOMNodeInserted', function(e){
	if(allCheck == true){
		$('[data-ax5grid-column-key="checked"] span').addClass("checked");
	}
});

//화면 초기화
function screenInit(){
	$('#datStD').val(getDate('DATE', 0));
	$('#datEdD').val(getDate('DATE', 0));	
	picker.bind(defaultPickerInfoLarge('basic', 'top'));
	picker.bind(defaultPickerInfoLarge('basic2', 'top'));
	
	$('[data-ax5select="cboSin"]').ax5select({
	    options: []
	});
	$('[data-ax5select="cboTeam"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboSta"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboProc"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboGbn"]').ax5select({
		options: []
	});
	
	$('[data-ax5select="cboTeam"]').ax5select('disable');
}

//부장, 부부장 여부 확인
function getUserB() {
	var data = {
		UserID : userId,
		requestType : 'getUserB'
	}
	ajaxAsync('/webPage/common/UserInfoServlet', data, 'json', successGetUserB);
}

function successGetUserB(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data == "Y") {
		$('#btnGyul').prop("disabled", false);
		$('#btnGyul').show();
		$('#btnCncl').show();
		approGrid.config.showRowSelector = true;
		approGrid.config.multipleSelect = true;
		approGrid.setConfig();
	} else {
		$('#btnGyul').prop("disabled", true);
		$('#btnGyul').hide();
		$('#btnCncl').hide();
	}
}

//부서 정보 가져오기
function getTeamInfo() {
	var data = new Object();
	data = {
		SelMsg 		: "ALL",
		cm_useyn 	: "Y",
		gubun 		: "sub",
		itYn 		: "Y",
		requestType	: 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetTeamInfo);
}

//부서 정보 가져오기 완료
function successGetTeamInfo(data) {
	cboTeamData = data;
	$('[data-ax5select="cboTeam"]').ax5select({
		options: injectCboDataToArr(cboTeamData, 'cm_deptcd', 'cm_deptname')
	});	
	$('[data-ax5select="cboTeam"]').ax5select('enable');
}

//콤보정보 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ 
		new CodeInfoOrdercd('REQUEST','ALL','N'),  
		new CodeInfoOrdercd('APPROVAL','ALL','N')
	]);
	cboSinData 	= codeInfos.REQUEST;
	cboStaData  = codeInfos.APPROVAL;
	codeInfos = null;

	$('[data-ax5select="cboSin"]').ax5select({
		options: injectCboDataToArr(cboSinData, 'cm_micode', 'cm_codename')
	});	
	$('[data-ax5select="cboSta"]').ax5select({
		options: injectCboDataToArr(cboStaData, 'cm_micode', 'cm_codename')
	});	
	$('[data-ax5select="cboSta"]').ax5select("setValue", '01', true);

	cboSta_change();
	getRequestList();
}

function cboSta_change() {
	if (getSelectedIndex("cboSta") < 0) return;
	
	if(getSelectedVal("cboSta").cm_micode == '01') {
		$('#datStD').prop("disabled", true);
		$('#datEdD').prop("disabled", true);
		disableCal(true, 'datStD');
		disableCal(true, 'datEdD');
	} else {
		$('#datStD').prop("disabled", false);
		$('#datEdD').prop("disabled", false);
		disableCal(false, 'datStD');
		disableCal(false, 'datEdD');
	}
	
	approGridData = [];
	approGrid.setData([]);
}

//결재 현황 리스트 가져오기
function getRequestList() {
	var strQry = "0";
	var strSta = "0";
	var strTeam ="0"

	$('#lblConf').hide();
	$('#txtConMsg').hide();

	var dateSt = replaceAllString($('#datStD').val().trim(),'/','');
	var dateEd = replaceAllString($('#datEdD').val().trim(),'/','');

	strSta = getSelectedIndex('cboSta') > 0 ?  getSelectedVal('cboSta').value : strSta;
 	if (strSta != "01") {
		if(dateSt > dateEd) {
			dialog.alert('조회기간을 정확하게 선택하여 주십시오.', function() {});
			return;
		}
 	}
	
	strQry  = getSelectedIndex('cboSin') > 0 ?  getSelectedVal('cboSin').value : strQry;
	strTeam = getSelectedIndex('cboTeam') > 0 ?  getSelectedVal('cboTeam').value : strTeam;
	
	var data = new Object();
	data = {
		pReqCd  	: strQry,
		pTeamCd		: strTeam,
		pStateCd 	: strSta,
		pReqUser 	: $('#txtUser').val().trim(),
		pStartDt 	: dateSt,
		pEndDt  	: dateEd,
		pUserId 	: userId,
		pDeptManager: getSelectedVal('cboSta').cm_micode == "05" ? true : false,
		requestType	: 'get_SelectList'
	}
	
	$('[data-ax5grid="approGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json',successGetSelectList);
}

//결재 현황 리스트 가져오기 완료
function successGetSelectList(data) {
	$(".loding-div").remove();
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	approGridData = data;
	if(approGridData.length < 1) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	$(approGridData).each(function(i){
		if(approGridData[i].enabledyn == 'false') {
			approGridData[i].__disable_selection__ = true;
		}else {
			approGridData[i].__disable_selection__ = '';
		}
	});
	
	if(approGrid.sortInfo["acptdate"] == undefined){
		var sortInfo = {
			acptdate : {seq : 0, orderBy: "desc"}
		}
		approGrid.setColumnSort(sortInfo);
	}
	
	approGrid.setData(approGridData);
}

function btnGyul_Click() {
	var cnt = 0;
//	for(var i = 0; i < approGridData.length; i++) {
//		if(approGridData[i].__selected__ == true)
//			++cnt;
//	}
	
	cnt = approGrid.getList('selected');	// 20221214
	console.log(cnt);
	
	if(cnt <= 0) {
		dialog.alert("일괄결재 건이 없습니다.")
	} else {
		$("#lblConf").show();
		$("#txtConMsg").show();
		confirmDialog.setConfig({
	        title: "결재확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '일괄결재 처리하시겠습니까?',
		}, function(){
			var tmpAc = [];
			if(this.key === 'ok') {
				for(var i = 0; i < approGridData.length; i++) {
					if(approGridData[i].__selected__ == true) {
						var tmpObject = new Object();
						tmpObject.cr_qrycd = approGridData[i].cr_qrycd;
						tmpObject.cr_acptno = approGridData[i].cr_acptno;
						tmpAc.push(tmpObject);
						tmpObject = null;
					}
				}
				confGyul(tmpAc, userId, $('#txtConMsg').val().trim(), "1");
			}
		});
	}
}

function btnCncl_Click() {
	var cnt = 0;
//	for(var i = 0; i < approGridData.length; i++) {
//		if(approGridData[i].__selected__ == true)
//			++cnt;
//	}

	cnt = approGrid.getList('selected');	// 20221214
	console.log(cnt);
	
	if(cnt <= 0) {
		dialog.alert("일괄반려 건이 없습니다.")
	}else {
		$("#lblConf").show();
		$("#txtConMsg").show();
		
		if($("#txtConMsg").val().length == 0) {
			dialog.alert("반려의견을 입력하여 주십시오.");
			$("#txtConMsg").focus();
			return;
		}
		
		confirmDialog.setConfig({
	        title: "반려확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '일괄반려 처리하시겠습니까?',
		}, function(){
			var tmpAc = [];
			if(this.key === 'ok') {
				for(var i = 0; i < approGridData.length; i++) {
					if(approGridData[i].__selected__ == true) {
						var tmpObject = new Object();
						tmpObject.cr_qrycd = approGridData[i].cr_qrycd;
						tmpObject.cr_acptno = approGridData[i].cr_acptno;
						tmpAc.push(tmpObject);
						tmpObject = null;
					}
				}
				packageConf(tmpAc, userId, $('#txtConMsg').val().trim(), "3");
			}
		});
	}
}

function confGyul(value, UserId, conMsg, Cd) {
	var data = new Object();
	data = {
		value  : value,
		UserId : UserId,
		conMsg : conMsg,
		Cd 	   : Cd,
		requestType : getSelectedVal('cboSta').cm_micode == "05" ? 'bujangConf' : 'packageConf'
	}
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json',successPackageConf);
}

function nextConf(acptno, qrycd, conmsg, cd) {
	var data = new Object();
	data = {
		AcptNo	:	acptno,
		UserId	:	userId,
		conMsg	:	conmsg,
		Cd		:	cd,
		ReqCd	:	qrycd,
		requestType	: getSelectedVal('cboSta').cm_micode == "05" ? 'nextConf3' : 'nextConf'
	}
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json',successNextConf);
}

function successNextConf(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data == '1') {
		$("#txtConMsg").val("");
		getRequestList();
	}else if(data == '3') {
		$("#txtConMsg").val("");
		getRequestList();
	}else {
		dialog.alert("결재/반송처리에 실패하였습니다.");
	}
}

function packageConf(value, userId, conmsg, cd) {
	var data = new Object();
	data = {
		value 	: value,
		UserId 	: userId,
		conMsg 	: conmsg,
		Cd 		: cd,
		requestType : 'packageConf'
	}
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json',successPackageConf);
}

function successPackageConf(data) {
	if (data == "0") {
		$("#txtConMsg").val("");
		getRequestList();
	} else {
		var msg = data;
		msg = data.replace("ERROR", "");
		dialog.alert(msg + "\n\n결재/반송처리에 실패하였습니다.");
	}
}


//결재 정보 창 띄우기
function openWindow(type, acptNo, reqCd, srId) {
	var nHeight, nWidth, cURL, winName;
	
	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
	
	winName = type+'_'+reqCd;
 
	var form = document.popPam;   		//폼 name
	if (type === 1){
		form.acptno.value	= acptNo;
	} else {
	 	form.acptno.value	= replaceAllString(acptNo,'-','');
	}
	
	form.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	
	if(type === 1) {
		nHeight = screen.height - 300;
	    nWidth  = screen.width - 400;
	    
	    if(reqCd == "01" || reqCd == "02" || reqCd == "11") {
	    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	    }else if(reqCd == "60") {
	    	cURL = "/webPage/winpop/PopRequestDetailHomePg.jsp";
		}else {
			if(getSelectedVal('cboSta').cm_micode == "05") {
				form.chkmanager.value = "05";
			}
	    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
		}
	}else if(type === 2) {
		nHeight = 828;
		nWidth  = 1046;
		cURL	= '/webPage/winpop/PopApprovalInfo.jsp';
	}

	myWin = winOpen(form, winName, cURL, nHeight, nWidth);
}
