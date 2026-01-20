/**
 * 적용요청등록
 * 
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-08-00
 * 
 */
var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;
var attPath = window.top.attPath;

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var thirdGrid		= new ax5.ui.grid();
var datReqDate 		= new ax5.ui.picker();

var cboSysData			= [];	
var cboDeployData 		= [];
var cboReqData			= [];
var cboGyulCheckData 	= [];
var cboReqSayuData		= [];
var cboTestYNData 		= [];
var cboNewGoodsData 	= [];
var cboImportantData 	= [];
var cboJobData = [];

var firstGridData 		= [];
var secondGridData 		= [];
var thirdGridData		= [];

var ingSw 				= false;
var exlSw 			 	= false; //엑셀첨부 flag (사용안함)
var nowFlag				= false;
var strAcptNo		 	= "";    //신청번호

/* 실행모듈연결정보 모달 관련 변수 */
var progModal 	= new ax5.ui.modal();
var modalObj	= new Object();

/* 신청상세>재요청 관련 변수 */
var reChkInAcptNo 	= "";
if(window.opener != null && window.opener != undefined) {
	reChkInAcptNo = window.opener.pReqNo;
}
var isReChkIn		= false;
var orderAC			= [];
var winSw 			= '';	//신청상세에서 winSw=true로 set

/* 검증사항모달 관련 변수 */
var modalId 				= null;
var reqCheckModal 			= new ax5.ui.modal();
var reqCheckModalVal 		= '';	//from modal
var reqCheckModalFiles 		= [];	//from modal
var reqCheckModalTestInfo 	= [];	//from modal

/* 결재절차확정모달 관련 변수 */
var approvalModal 	= new ax5.ui.modal();
var confirmData 	= [];	//from modal
var confirmInfoData = null;	//to modal

/* 파일 업로드 관련 변수 */
var fileUploadModal = new ax5.ui.modal();
var upFiles 		= [];
var uploadCk 		= false;
var uploadLen 		= 0;
var tmpPath  	= '';
var uploadJspFile = '';

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	if($('#txtSayu').val().trim().length == 0) {
        		$('#txtSayu').val(this.item.cr_sayu);
        		$('#lblSayu').text('총 (' + $('#txtSayu').val().trim().length + ')자');
        	}
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	addDataRow();
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    	trStyleClass: function () {
    		if (this.item.selected_flag === '1'){
    			return "fontStyle-cncl";
    		}
   			if (this.item.errmsg != null && this.item.errmsg != ""){
   				if (this.item.errmsg != "정상") {
   	    			return "fontStyle-cncl";
   				}
   			}
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
            {type: 1, label: "추가"},
            {type: 2, label: "실행모듈연결정보"}
        ],
        popupFilter: function (item, param) {
         	firstGrid.clearSelect();
         	firstGrid.select(Number(param.dindex));
         	
         	var selIn = firstGrid.selectedDataIndexs;
        	if(selIn.length === 0) return;
         	
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	var retType = '1';
        	if(param.item.cm_ifno.substr(8,1) == '1') retType = retType+'2';
        	
        	if (retType == '') return false;
		    var retString;
		    
		    if (retType.indexOf('1')>-1){
		    	retString = (item.type == 1);
		    }
		    if (retType.indexOf('2')>-1){
		    	if (retType == '') retString = (item.type == 2);
		    	else retString = retString | (item.type == 2);
		    }
       	 
        },
        onClick: function (item, param) {
        	if(item.type == '1') {
        		addDataRow();
        	}else if(item.type == '2') {
        		openProgModal(param.item);
        	}
	        firstGrid.contextMenu.close();//또는 return true;
        }
    },
    columns:  [
        {key: "enddate",		label: "수정일자",		width: '8%',	align: 'center'},
    	{key: "cm_username",    label: "신청자",		width: '5%',  	align: 'center'},
    	{key: "cr_lstver",    	label: "버전", 		width: '4%',  	align: 'center'},
    	{key: "cr_rsrcname",    label: "프로그램명", 	width: '35%',  	align: 'left'},
    	{key: "jawon",    		label: "프로그램종류",	width: '10%',  	align: 'left'},
    	{key: "cm_jobname",  	label: "업무",		width: '10%',  	align: 'left'},
    	{key: "cm_dirpath2",   	label: "프로그램경로",	width: '28%',  	align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	deleteDataRow();
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    	trStyleClass: function () {
    		var sameSw = false;
    		
			if (this.item.cr_itemid == null || this.item.cr_itemid == "") {
				sameSw = true;
			} else {
	   			if (this.item.baseitem != this.item.cr_itemid){
	   				sameSw = true;
	   			}
	  		}

	  		if (sameSw) {
    			return "fontStyle-module";
	  		}
	  		
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
            {type: 1, label: "제거"},
            {type: 2, label: "실행모듈연결정보"}
        ],
        popupFilter: function (item, param) {
        	secondGrid.clearSelect();
        	secondGrid.select(Number(param.dindex));
        	var selIn = reqGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	var retType = '1';
        	if(param.item.cm_ifno.substr(8,1) == '1') retType = retType+'2';
        	
        	if (retType == '') return false;
		    var retString;
		    
		    if (retType.indexOf('1')>-1){
		    	retString = (item.type == 1);
		    }
		    if (retType.indexOf('2')>-1){
		    	if (retType == '') retString = (item.type == 2);
		    	else retString = retString | (item.type == 2);
		    }
        },
        onClick: function (item, param) {
        	if(item.type == '1') {
        		deleteDataRow();
        	}else if(item.type == '2') {
        		openProgModal(param.item);
        	}
	        secondGrid.contextMenu.close();//또는 return true;
        }
    },
    columns:  [
        {key: "cr_rsrcname",   			label: "프로그램명",		width: '20%',  	align: 'left'},
    	{key: "dealcode",    			label: "거래코드",			width: '15%',  	align: 'left'},
    	{key: "sayu",    				label: "변경사유",			width: '15%',  	align: 'left'},
    	{key: "importancecodecodename", label: "중요도",			width: '15%',  	align: 'left'},
    	{key: "testynname",    			label: "테스트대상",		width: '10%',  	align: 'left'},
        {key: "newgoodscodecodename",   label: "신상품/공통여부", 	width: '15%',	align: 'left'},
        {key: "prcseq",       			label: "우선순위", 		width: '10%',	align: 'center',   editor:{type:"number"}},
    ]
});

thirdGrid.setConfig({
    target: $('[data-ax5grid="thirdGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    page: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	//this.self.clearSelect();
        	//this.self.select(this.dindex);
        	openWindow('2',this.item.CC_ORDERID)
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    },
    columns: [
    	{key: "CC_ORDERID",   label: "발행번호",		width: '30%',  	align: 'left'},
    	{key: "CC_REQSUB",    label: "요청제목",		width: '70%',  	align: 'left'}
    ]
});

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});
$('[data-ax5select="cboReq"]').ax5select({
    options: []
});
$('[data-ax5select="cboDeploy"]').ax5select({
    options: []
});
$('[data-ax5select="cboGyulCheck"]').ax5select({
    options: []
});
$('[data-ax5select="cboTestYN"]').ax5select({
    options: []
});
$('[data-ax5select="cboNewGoods"]').ax5select({
    options: []
});
$('[data-ax5select="cboImportant"]').ax5select({
    options: []
});
$('[data-ax5select="cboReqSayu"]').ax5select({
    options: []
});

$('[data-ax5select="cboJob"]').ax5select({
    options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$('#txtCompDate').val(getDate('DATE',0));
$('#txtReqDate').val(getDate('DATE',0));
$('#txtTestDate').val(getDate('DATE',0));
picker.bind(defaultPickerInfo('basic', 'top'));
picker.bind(defaultPickerInfo('basic2', 'top'));
picker.bind(defaultPickerInfo('basic3', 'bottom'));

$(document).ready(function(){
	winSw = $('#winSw').val();
	if(winSw === 'true') {
		userId = $('#userId').val();
		reqCd = window.opener.reqCd;
		adminYN = window.opener.isAdmin;
		attPath = window.opener.attPath;
		$('#divFrame').css('padding','10px');
	}
	
	if (reqCd == null || reqCd == '' || reqCd == undefined) {
		dialog.alert('정당하지 않은 접근입니다.');
		return;
	}
	if (userId == null || userId == '' || userId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	
	$('#txtCompDate').prop("disabled", true);
	$('#btnDate').prop("disabled", true);
	$('#txtTime').prop("disabled", true);
	
	$('#txtSayu').keyup(function() {
		$('#lblSayu').text('총 (' + $('#txtSayu').val().trim().length + ')자');
	});
	getTmpDir();
	// 시스템
	$('#cboSys').bind('change',function(){
		changeSys();
	});

	// 업무
	$('#cboJob').bind('change',function(){
		getReqList();
	});
	// 적용구분
	$('#cboDeploy').bind('change',function(){
		cboDeployChange();
	});
	
	// 신청구분
	$("#cboReq").bind('change',function(){
		cboReqChange();
	});
	
	// 주관부서결재
	$("#cboGyulCheck").bind('change',function(){
		cboGyulCheckChange();
	});
		
	// 추가
	$('#btnAdd').bind('click',function(){
		addDataRow();
	});
	
	// 삭제
	$('#btnDel').bind('click',function(){
		deleteDataRow();
	});
	
	// 검색
	$('#btnFind').bind('click',function(){
		getReqList();
	});
	
	// 신청
	$('#cmdReq').bind('click',function(){
		cmdReqClick();
	});
	
	// 프로그램명
	$('#txtProg').bind('keypress',function(event){
		if(event.keyCode == 13){
			event.keyCode = 0;
			getReqList();
		}
	});
	
	// 엑셀저장
	$('#btnExcel').bind('click',function(){
		firstGrid.exportExcel("RequestList.xls");
	});
	
	// 특정일시컴파일
	$('#chkComp').bind('click', function() {
		var checkSw = $('#chkComp').is(':checked');
		if(checkSw) {
			$('#txtCompDate').prop("disabled", false);
			$('#btnDate').prop("disabled", false);
			$('#txtTime').prop("disabled", false);
		}else {
			$('#txtCompDate').prop("disabled", true);
			$('#btnDate').prop("disabled", true);
			$('#txtTime').prop("disabled", true);
		}
	});
	
	screenInit();
	//chkRechkIn();
	
	// 20230102
	$('#btnLoadExl').bind('click', function() {
		if(getSelectedIndex('cboSys') < 1) {
			dialog.alert('시스템 선택 후 사용해 주시기 바랍니다.', function() {});
			return;
		}
		
		$('#excelFile').trigger('click');
	});
	
	// 엑셀템플릿
	$('#btnExlTmp').bind('click', function() {
		fileDown(attPath+"/"+"checkin_excel_templet.xlsx","checkin_excel_templet.xlsx");
	});
	
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
});

function chkRechkIn() {
	var data = {
			AcptNo	: reChkInAcptNo,
			UserId	: userId,
		requestType	: 'chkRechkIn'
	}
	
	console.log('[chkRechkIn] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successChkRechkIn);
}

function successChkRechkIn(data) {
	isReChkIn = Boolean(data);
	if(isReChkIn) {
		$('#cmdReq').prop('disabled',true);
		
		var data = {
				AcptNo	: reChkInAcptNo,
				UserId	: userId,
			requestType	: 'getReturnInfo'
		}
		
		console.log('[getReturnInfo] ==>', data);
		ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successGetReturnInfo);
	}
}

function successGetReturnInfo(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	var tmpArr = data;
	var tmpObj = data[0];
	if(tmpObj.cr_devptime != null && tmpObj.cr_devptime != undefined && tmpObj.cr_devptime != '') {
		$('#txtDevTime').val(tmpObj.cr_devptime);
	}
	$('[data-ax5select="cboSys"]').ax5select('setValue',tmpObj.cm_syscd,true);
	$('[data-ax5select="cboReqSayu"]').ax5select('setValue',cboReqSayuData[0].cm_micode,true);
	for(var i=0; i<cboReqSayuData.length; i++) {
		if(cboReqSayuData[i].cm_micode == tmpObj.cr_reqsayu) {
			$('[data-ax5select="cboReqSayu"]').ax5select('setValue',tmpObj.cr_reqsayu,true);
			break;
		}
	}
	
	getReqList();
	
	for(var i=0; i<tmpArr.length; i++) {
		if(tmpArr[i].cr_aplydate != null && tmpArr[i].cr_aplydate != undefined && tmpArr[i].cr_aplydate != '') {
			$('[data-ax5select="cboDeploy"]').ax5select('setValue','4',true);
			$("#divApplyDate").css("visibility","visible");
			$("#hourTxt").val(tmpArr[i].cr_aplydate.substr(tmpArr[i].cr_aplydate.length-4,2));
			$("#minTxt").val(tmpArr[i].cr_aplydate.substr(tmpArr[i].cr_aplydate.length-2));
			$('#txtReqDate').val(tmpArr[i].cr_aplydate.substr(0,8));
			picker.bind(defaultPickerInfo('basic2','top'));
			tmpArr[i].Deploy = '4';
			
			if(tmpArr[i].cr_compdate != null && tmpArr[i].cr_compdate != undefined && tmpArr[i].cr_compdate != '') {
				$('#chkComp').wCheck('check',true);
				$('#txtCompDate').prop("disabled", false);
				$('#btnDate').prop("disabled", false);
				$('#txtTime').prop("disabled", false);
				
				$('#txtCompDate').val(tmpArr[i].cr_compdate.substr(0,8));
				$('#txtTime').val(tmpArr[i].cr_compdate.substr(tmpArr[i].cr_compdate.length-4))
				picker.bind(defaultPickerInfo('basic','top'));
			}
		}
		
		tmpArr[i].selected_flag = '1';
		tmpArr[i].selected = '0';
		tmpArr[i].enabled = '0';
		$('#txtSayu').val(tmpArr[i].sayu);
		$('#lblSayu').text('총 (' + $('#txtSayu').val().trim().length + ')자');
		$('#txtCode').val(tmpArr[i].dealcode);
		
		if(tmpArr[i].cr_testdate != null && tmpArr[i].cr_testdate != undefined && tmpArr[i].cr_testdate != '') {
			$('[data-ax5select="cboGyulCheck"]').ax5select('setValue',cboGyulCheckData[1].cm_micode,true);
			cboGyulCheckChange();
			$('#txtTestDate').val(tmpArr[i].cr_testdate);
			picker.bind(defaultPickerInfo('basic3','bottom'));
			if(tmpArr[i].cr_etcsayu != null && tmpArr[i].cr_etcsayu != undefined && tmpArr[i].cr_etcsayu != '') {
				$('#txtTestSayu').val(tmpArr[i].cr_etcsayu);
			}
		}
		
		$('[data-ax5select="cboNewGoods"]').ax5select('setValue',tmpArr[i].newgoodscode,true);
		if(tmpArr[i].testyn == '00') $('[data-ax5select="cboTestYN"]').ax5select('setValue',cboTestYNData[0].cm_micode,true);
		else $('[data-ax5select="cboTestYN"]').ax5select('setValue',cboTestYNData[1].cm_micode,true);
		$('[data-ax5select="cboImportant"]').ax5select('setValue',tmpArr[i].importancecode,true);
	}
	
	console.log('tmpArr=',tmpArr);
	secondGridData = clone(tmpArr);
	secondGrid.setData(secondGridData);
	
	var data = {
			AcptNo	: reChkInAcptNo,
		requestType	: 'getProgOrders'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successGetProgOrders);
}

function successGetProgOrders(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data.length > 0) {
		orderAC = [];
		orderAC = data;
		for(var i = 0; i < thirdGridData.length; i++) {
			for(var j = 0; j < orderAC.length; j++) {
				if(thirdGridData[i].CC_ORDERID == orderAC[j].CC_ORDERID) {
					console.log('thirdGridData==>',thirdGridData[i]);
					thirdGridData[i].__selected__ = true;
					thirdGrid.setData(thirdGridData); //20221214
				}
			}
		}
	}
}

function screenInit() {
	var codeInfos = getCodeInfoCommon([ new CodeInfoOrdercd('REQPASS','','N','3',''),
										new CodeInfoOrdercd('CHECKIN','SEL','N','3',''),
										new CodeInfoOrdercd('REQSAYU','SEL','N','3',''),
										new CodeInfoOrdercd('NEW/GLO','SEL','N','3',''),
										new CodeInfoOrdercd('IMPORTANT','SEL','N','3','')
								 	 ]);
	
	//적용구분
	cboDeployData = codeInfos.REQPASS;
	$('[data-ax5select="cboDeploy"]').ax5select({
        options: injectCboDataToArr(cboDeployData, 'cm_micode' , 'cm_codename')
	});
	
	//신청구분
	cboReqData = codeInfos.CHECKIN;
	cboReqData.unshift({cm_macode: "CHECKIN", cm_micode: "99", cm_codename: "신규+수정"});
	$('[data-ax5select="cboReq"]').ax5select({
        options: injectCboDataToArr(cboReqData, 'cm_micode' , 'cm_codename')
	});
	
	//적용근거
	cboReqSayuData = codeInfos.REQSAYU;
	$('[data-ax5select="cboReqSayu"]').ax5select({
        options: injectCboDataToArr(cboReqSayuData, 'cm_micode' , 'cm_codename')
	});
	
	//신상품/공통여부
	cboNewGoodsData = codeInfos['NEW/GLO'];
	$('[data-ax5select="cboNewGoods"]').ax5select({
        options: injectCboDataToArr(cboNewGoodsData, 'cm_micode' , 'cm_codename')
	});
	
	//중요도
	cboImportantData = codeInfos.IMPORTANT;
	$('[data-ax5select="cboImportant"]').ax5select({
        options: injectCboDataToArr(cboImportantData, 'cm_micode' , 'cm_codename')
	});
	
	cboTestYNData = [
		{cm_codename : "N", cm_micode : "00"},
		{cm_codename : "Y", cm_micode : "01"}
	];
	$('[data-ax5select="cboTestYN"]').ax5select({
        options: injectCboDataToArr(cboTestYNData, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboTestYN"]').ax5select('setValue','01',true);
	
	cboGyulCheckData = clone(cboTestYNData);
	$('[data-ax5select="cboGyulCheck"]').ax5select({
        options: injectCboDataToArr(cboGyulCheckData, 'cm_micode' , 'cm_codename')
	});
	
	getSysCbo();
	getOrderList();
}

function getOrderList() {
	var sysListInfoData = {
			Userid	: userId,
		requestType	: 'OrderSelect2'
	}
	
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', sysListInfoData, 'json',successGerOrderList);
}

function successGerOrderList(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	thirdGridData = data;
	thirdGrid.setData(thirdGridData);
}

function getSysCbo() {
	var sysListInfoData = {
			UserId	: userId,
			SecuYn  : "Y",
			SelMsg  : "",
			CloseYn : "N",
			ReqCd   :  reqCd,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', sysListInfoData, 'json',successGetSysCbo);
}

function successGetSysCbo(data) {
	if(data.length == 0) {
		dialog.alert('권한이 있는 시스템 중 적용요청 하실 수 있는 시스템이 없습니다. 관리자에게 문의해주세요.');
		return;
	}
	
	cboSysData = data;
	cboSysData = cboSysData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	});
	
   	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
   	});
   	
   	if (cboSysData.length>0) {
   		$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[0].cm_syscd,true);
   		for (var i=0;cboSysData.length>i;i++) {
			if (cboSysData[i].setyn == "Y") {
				$('[data-ax5select="cboSys"]').ax5select("setValue", cboSysData[i].cm_syscd);
				break;
			}
		}
   		/* 기존 소스 */
   		// changeSys();
   		// chkRechkIn();
   	
   		/* 2025 변경 소스 */
   		if (reChkInAcptNo != null && reChkInAcptNo != '' && reChkInAcptNo.length > 0) chkRechkIn();
   		else changeSys();
   	}
}

function changeSys() {
	firstGrid.setData([]);
	firstGridData = [];
	cboJobData = [];
	
	secondGrid.setData([]);
	secondGridData = [];
	    
	if (getSelectedVal('cboSys').cm_sysinfo.substr(4,1) == "1") {
		dialog.alert("이관통제를 위하여 일시적으로 형상관리 사용을 중지합니다.");
		$('#btnSearch').prop('disabled',true);
		return;
	} else $('#btnSearch').prop('disabled',false);
	
	
	/* 업무 코드 추가 */
	selectedIndex = getSelectedIndex('cboSys');
	selectedItem = getSelectedVal('cboSys');
	if(selectedIndex < 1) {
		$('[data-ax5select="cboJob"]').ax5select({
			options: []
		});
	} else {
		if( selectedItem == null ) return;
		getJobInfo(selectedItem.cm_syscd);  //업무 리로딩
	}
}
//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(sysCd) {
	var data = new Object();
	data = {
		UserID 		: userId,
		SysCd 		: sysCd,
		SecuYn	 	: 'Y',		
		CloseYn	 	: 'Y',		
		SelMsg 		: 'ALL',
		sortCd 		: 'NAME',
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successJob);
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
	} else {
		options = [];
		options.push({value: "0000", text: "전체"});
		$('[data-ax5select="cboJob"]').ax5select({
	        options: options
		});
	}


	var fcboReqData = cboReqData.filter(function(item) {
		if(item.cm_micode == '00') return false;
		else if(item.cm_micode != '05') return true;
		else if(reqCd == '04') return true;
		else if(getSelectedVal('cboSys').delsw == 'Y') return true;
		else return false;
	});
	
	$('[data-ax5select="cboReq"]').ax5select({
        options: injectCboDataToArr(fcboReqData, 'cm_micode' , 'cm_codename')
	});
	
	if(fcboReqData.length > 0) {
		cboReqChange();
	}

}
function cboReqChange() {
	var selReq = getSelectedVal('cboReq');
	
	if (selReq.cm_micode == "05" && secondGridData.length > 0) {
		if (secondGridData[0].reqcd != "05") {
			dialog.alert("폐기는 다른신청과 함께 신청할 수 없습니다.");
			return;
		}
	}
	
	if (getSelectedIndex('cboReq') > -1 && getSelectedIndex('cboSys') > -1) {
		firstGrid.setData([]);
		firstGridData = [];
		
	 	if ( (reqCd == "04" && getSelectedVal('cboSys').TstSw == "1") 
	 			|| selReq.cm_micode == "00" || selReq.cm_micode == "99" || selReq.cm_micode == "03" || selReq.cm_micode == "04") {			
	 		getReqList();
		} else {
			firstGrid.setData([]);
			firstGridData = [];
		}
	}
}

function cboDeployChange() {
	var deployValue = getSelectedVal('cboDeploy');
	$("#divApplyDate").css("visibility","hidden");
	
	if (getSelectedIndex('cboDeploy') > -1) {
		if (deployValue.cm_micode == "4") { //특정일시
			$("#hourTxt").val("00");
			$("#minTxt").val("00");
			$("#divApplyDate").css("visibility","visible");
			$('#txtReqDate').val(getDate('DATE',0));
			picker.bind(defaultPickerInfo('basic2','top'));
		}
	}
}

function cboGyulCheckChange() {
	if(getSelectedVal('cboGyulCheck').cm_micode == '00') {
		$('#divTest').hide();
	}else {
		$('#divTest').show();
		$('#txtTestDate').val(getDate('DATE',0));
		picker.bind(defaultPickerInfo('basic3', 'bottom'));
	}
}

function getReqList() {
	if (ingSw) return;

	ingSw = true;
	
	firstGrid.setData([]);
	firstGridData = [];
	
	var tmpObj = {};
	tmpObj.UserId = userId;
	tmpObj.SysCd = getSelectedVal('cboSys').cm_syscd;
	if(getSelectedVal('cboJob') != null && getSelectedVal('cboJob') != undefined) {
		tmpObj.JobCd = getSelectedVal('cboJob').value;	
	}else{
		tmpObj.JobCd = '0000'
	}
	tmpObj.SinCd = reqCd;
	tmpObj.ReqCd = getSelectedIndex('cboReq') == 0 ? '00' : getSelectedVal('cboReq').cm_micode;
	tmpObj.TstSw = getSelectedVal('cboSys').TstSw;
	tmpObj.RsrcName = $("#txtProg").val();
	tmpObj.DsnCd = "";
	tmpObj.DirPath = "";
	tmpObj.SysInfo = getSelectedVal('cboSys').cm_sysinfo;
	tmpObj.RsrcCd = "00";
	if($('#chkUpLow').is(':checked')) tmpObj.UpLowSw = 'true';
	else tmpObj.UpLowSw = 'false';
	if($('#chkSelf').is(':checked')) tmpObj.selfSw = 'true';
	else tmpObj.selfSw = 'false';
	if($('#chkLike').is(':checked')) tmpObj.LikeSw = 'true';
	else tmpObj.LikeSw = 'false';
	tmpObj.txtORDERInfo = "";
	tmpObj.itemid = "";
		
	var paramData = new Object();
	paramData = {
		paramMap	: tmpObj,
		requestType :'getReqList'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	$("#btnFind").prop("disabled", true);
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', paramData, 'json', successGetReqList);
}

function successGetReqList(data) {
	ingSw = false;
	$(".loding-div").remove();
	$("#btnFind").prop("disabled", false);
	
	if(isReChkIn) $('#cmdReq').prop('disabled',false);

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
	firstGridData.sort(function(a,b){
		return a.cr_rsrcname < b.cr_rsrcname ? -1 : a.cr_rsrcname > b.cr_rsrcname ? 1 : 0;
	});
	
	if (firstGridData.length == 0 ) {
		dialog.alert('검색 결과가 없습니다.');
		return;		
	} else if (firstGridData.length > 0) {
		if(firstGridData[0].ID =='MAX'){
			dialog.alert('검색결과가 너무 많으니 검색조건을 입력하여 검색하여 주시기 바랍니다.');
			return;
		}
	}
	$(firstGridData).each(function(i){
		$(secondGridData).each(function(j){
			if(firstGridData[i].cr_itemid == secondGridData[j].cr_itemid){
				firstGridData[i].selected_flag = '1';
				firstGridData[i].selected = '0';
				firstGridData[i].enabled = '0';
				return true;
			}
		});
		if(firstGridData[i].selected_flag == "1"){
			firstGridData[i].__disable_selection__ = true;
		}
	});
	firstGrid.setData(firstGridData);
}

//신청목록추가
function addDataRow() {
	firstGridSelected = firstGrid.getList("selected");
	if (firstGridSelected.length == 0) {
		dialog.alert('추가할 항목을 선택해주십시오.');
		return;
	}
	
	if (Number($('#txtDevTime').val()) < 1){
		dialog.alert("개발기간을 확인하십시오.");
		ingSw = false;
		$('#txtDevTime').focus();
		return;
	}
	
	if(getSelectedVal('cboReqSayu').cm_micode == '00') {
		dialog.alert('적용근거를 선택하여 주시기 바랍니다.');
		ingSw = false;
		return;
	}
	
	var orderListSelected = thirdGrid.getList("selected");
	if(getSelectedVal('cboSys').cm_sysinfo.substr(11,1) == '0' && orderListSelected.length == 0) {
		dialog.alert('업무지시서를 선택하여 주시기 바랍니다.');
		return;
	}
	
	if($('#txtSayu').val().trim().length < 10) {
		dialog.alert('공백 제외 10자 이상 변경사유를 입력하여 주시기 바랍니다.');
		return;
	}
	
	if(getSelectedVal('cboSys').cm_sysinfo.substr(11,1) == '0' && $('#txtCode').val().length == 0) {
		dialog.alert('거래코드 입력하여 주시기 바랍니다.');
		return;
	}
	
	if(getSelectedVal('cboSys').cm_sysinfo.substr(11,1) == '0' && getSelectedIndex('cboNewGoods') < 1) {
		dialog.alert('신상품/공통여부 선택하여 주시기 바랍니다.');
		return;
	}

	//12: 작업지시사용안함
	if(getSelectedVal('cboSys').cm_sysinfo.substr(11,1) == '0' && getSelectedIndex('cboImportant') < 1) {
		dialog.alert('중요도 선택하여 주시기 바랍니다.');
		return;
	}
	
	var secondGridList = new Array;
	var findSw = false;
	var i = 0;
	var j = 0;
	var befckoutcnt = 0;
	var strRsrcName = '';
	
	for (i=0;firstGridSelected.length>i;i++) {
		findSw = false;
		if (firstGridSelected[i].selected_flag == '1') {
			findSw = true;
		} 
		
		if (!findSw) {
			for (j=0;secondGridData.length>j;j++) {
				if (firstGridSelected[i].cr_itemid == secondGridData[j].cr_itemid) {
					findSw = true;
					break;
				}
			}
		}
		
		if (findSw) {
			firstGridSelected.splice(i,1);
			i--;
		}
	}

	if ((secondGridData.length + firstGridSelected.length) > 2000){
		dialog.alert("2000건 이하로 신청하여 주시기 바랍니다.");
		return;
	}
	
	if (getSelectedVal('cboReq').cm_micode == "05" && secondGridData.length > 0) {
		if (secondGridData[0].reqcd != "05") {
			dialog.alert("폐기는 다른신청과 함께 신청할 수 없습니다.");
			return;
		}
	}
	
	var today	= getDate('DATE',0);
	var strNow	= '';
	var strTime = '';
	var strDate = '';
	var strNum  = '0123456789';
	
	if (getSelectedVal('cboDeploy').cm_micode == '4') {
		strTime = $("#hourTxt").val() + $("#minTxt").val();
		strDate = replaceAllString($('#txtReqDate').val(),'/','');
		
		if ($('#hourTxt').val() == '' || $('#minTxt').val() == '' || strTime == '' || strDate == '') {
			dialog.alert("특정일시를 입력하여 주시기 바랍니다.");
			ingSw = false;
			return;
		}
		
		if (strTime.length != 4) {
			dialog.alert("4자리 숫자로 입력하여 주시기 바랍니다.");
			ingSw = false;
			return;
		}
		
		for (i=0;strTime.length>i;i++) {
			if (strNum.indexOf(strTime.substr(i,1))<0) {
				dialog.alert("4자리 숫자로 입력하여 주시기 바랍니다.");
				ingSw = false;
				return;
			}
		}
		
		if (today > strDate) {
			dialog.alert("특정일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
			ingSw = false;
			return;
		}else if (today == strDate) {				
			strNow = getTime();
			if (strTime < strNow) {
				dialog.alert("특정일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
				ingSw = false;
				return;
			}
		}
	}
	
	var strCompTime = '';
	var strCompDate = '';
	if($('#chkComp').is(':checked')) {
		strCompTime = $("#txtTime").val();
		strCompDate = replaceAllString($('#txtCompDate').val(),'/','');
		
		if (strCompTime == '' || strCompDate == '') {
			dialog.alert("특정일시를 입력하여 주시기 바랍니다.");
			ingSw = false;
			return;
		}
		
		if (strCompTime.length != 4) {
			dialog.alert("4자리 숫자로 입력하여 주시기 바랍니다.");
			ingSw = false;
			return;
		}
		
		if (today > strCompDate) {
			dialog.alert("컴파일지정일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
			ingSw = false;
			return;
		}else if (today == strCompDate) {				
			strNow = getTime();
			if (strCompTime < strNow) {
				dialog.alert("컴파일지정일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
				ingSw = false;
				return;
			}
		}
	}
	
	var modSw = false;
	$(firstGridSelected).each(function(i){
		if(this.selected_flag == "1") return true;

		if (exlSw && this.errmsg != "정상" && alertFlag == 2) {
			return true;
		}
		
		if (exlSw && this.errmsg != "정상" && this.errmsg != "파일중복" && alertFlag != 2) {
			return true;
		}
		
		if (!exlSw && !modSw) {
			if (this.cm_info.substr(8,1) == "1") modSw = true;
		}
		
		if(this.selected_flag != "1"){
			this.selected_flag = "1";
			this.baseitem = this.cr_itemid;
			this.sayu = $('#txtSayu').val();
			this.importancecode = getSelectedVal('cboImportant').cm_micode;
			this.importancecodecodename = getSelectedVal('cboImportant').cm_codename;
			this.newgoodscode = getSelectedVal('cboNewGoods').cm_micode;
			this.newgoodscodecodename = getSelectedVal('cboNewGoods').cm_codename;
            this.dealcode = $('#txtCode').val();
            this.testyn = getSelectedVal('cboTestYN').cm_micode;
            this.testynname = getSelectedVal('cboTestYN').cm_codename;
            this.cr_sayu = $('#txtSayu').val();
            this.Deploy = getSelectedVal('cboDeploy').cm_micode;
            this.AplyDate = strDate + strTime;
            this.CompDate = strCompDate + strCompTime;
            this.sysgb = getSelectedVal('cboSys').cm_sysgb;
            
            var selItems = thirdGrid.getList('selected');
        	for(var j=0; j<selItems.length; j++) {
        		var tmpObj = new Object();
        		if(selItems[j].CC_NOWDATE == '1') {
        			if(!nowFlag) nowFlag = true;
        		}
        		tmpObj.CC_ORDERID = selItems[j].CC_ORDERID;
        		tmpObj.CC_REQSUB = selItems[j].CC_REQSUB;
        		tmpObj.CR_ITEMID = this.cr_itemid;
        		orderAC.push(tmpObj);
                tmpObj = null;
        	}
        	
			secondGridList.push($.extend({}, this, {__index: undefined}));
		}
	});

	if (secondGridList.length != 0){
//		if (modSw) {
//		} else {
			//downList_Set(secondGridList);
			var paramData = new Object();
			paramData = {
				fileList	: secondGridList,
				requestType :'filechk'
			}
			var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', paramData, 'json');
			
			console.log('orderListSelected=>',orderListSelected);
			var strOrderId1 = '';
			var strOrderId2 = '';
			nowFlag = false;
			for(var i=0; i<orderListSelected.length; i++) {
				if(orderListSelected[i].CC_NOWDATE == '1') {
					strOrderId1 = orderListSelected[i].CC_ORDERID;
				}
			}
			if(strOrderId1 != strOrderId2) nowFlag = false;
			$('txtOrderNum').val(strOrderId1 + '' + strOrderId2);
			for(var i=0; i<orderListSelected.length; i++) {
				if(orderListSelected[i].CC_NOWDATE == '1') {
					strOrderId2 = orderListSelected[i].CC_ORDERID;
					if(!nowFlag) {
						dialog.alert('유효기간이 지난 업무지시서를 선택하셨습니다 !!!');
					}
					nowFlag = true;
                	break;
				}
			}
			
			if(ajaxReturnData != null && ajaxReturnData != undefined && ajaxReturnData != '') {
				secondGridList.forEach(function(item) {
					if(item.cr_rsrccd == '25') {
						item.cr_rsrccd = item.cr_rsrccd + ',' + ajaxReturnData;
					}
				});
			}
			
			downList_Set(secondGridList);
//		}
	}
}

function downList_Set(data) {
	firstGrid.clearSelect();
	checkDuplication(data);
}

function checkDuplication(downFileList) {
	var secondGridList = new Array;
	var i = 0;
	var j = 0;
	var findSw = false;
	var chkSw = false;
	var diffSw = false;
	var diffSw2 = false;
	var totCnt = secondGridData.length;
	
	if (totCnt > 0) {
		for(i=0; downFileList.length>i ; i++){
			findSw = false;
			totCnt = secondGridData.length;
			for (j=0;totCnt>j;j++) {
				if (downFileList[i].cr_itemid == secondGridData[j].cr_itemid) {
					findSw = true;
					break;
				}
			}
			if (!findSw) {
				var copyData = clone(downFileList[i]); //리스트의 주소지를 가져오므로 clone 을 해서 add 해줘야함
				secondGridList.push($.extend({}, copyData, {__index: undefined}));
				copyData.__index = secondGridData.length;
				secondGridData.push(copyData);
			}
		}
	} else {
		secondGridList = clone(downFileList);
		secondGridData = clone(downFileList);
	}
	if(downFileList.length > 0) {
		$(downFileList).each(function(i){
			var currentItem = downFileList[i];
						
			for (j=0;firstGridData.length>j;j++) {
				if(firstGrid.list[j].cr_itemid == currentItem.cr_itemid) {
					firstGrid.list[j].selected_flag = '1';
					firstGrid.list[j].selected = '1';
					firstGrid.list[j].enabled = '0';
					firstGrid.list[j].__disable_selection__ = true;
					break;
				}
			}
		});
	}

	firstGrid.repaint();
	secondGrid.addRow(secondGridList);
	secondGrid.repaint();
	
	exlSw = false;
	$('#cmdReq').prop('disabled',true);
	if(secondGrid.list.length > 0){
		$('#cmdReq').prop('disabled',false);
		$('[data-ax5select="cboSys"]').ax5select('disable');
		if(getSelectedVal('cboReq').cm_micode == '05') $('#cmdReq').prop('disabled',true); 
	}
}

//신청목록에서 제거
function deleteDataRow() {
	var secondGridSeleted = secondGrid.getList("selected");
	var originalData = null;

	$(secondGridSeleted).each(function(i){
		originalData = null;
		if (this.cr_itemid != this.baseitem){
			for(var x=0; x<secondGrid.list.length; x++){
				if(secondGrid.list[x].baseitem == this.baseitem){
					secondGrid.select(x,{selected:true} );
					originalData = secondGrid.list[x].baseitem;
				}
			}
		}

		// 체크인 데이터 삭제
		$(secondGridData).each(function(z){
			if(this.baseitem == secondGridSeleted[i].baseitem){
				secondGridData.splice(z,1);
			}
		});

		// 상위 그리드 표시 해제
		$(firstGrid.list).each(function(j){
			if((firstGrid.list[j].cr_itemid == secondGridSeleted[i].cr_itemid) ||
				(firstGrid.list[j].cr_itemid == originalData && originalData != null)){

				firstGrid.list[j].__disable_selection__ = '';
				firstGrid.list[j].selected_flag = "0";
				return false;
			}
		});

	});
	secondGrid.removeRow("selected");
	firstGrid.repaint();
	
	$(secondGridSeleted).each(function(i){
		for(var j=0; j<orderAC.length; j++) {
			if(orderAC[j].CR_ITEMID == this.cr_itemid) {
				orderAC.splice(j,1);
				j--;
			}
		}
	});
	
	if (secondGrid.list.length == 0){
		$('[data-ax5select="cboSys"]').ax5select("enable");
		$('#cmdReq').prop('disabled',true);
		if(getSelectedVal('cboDeploy').cm_micode != '2' && reqCd == '04') {
			$('[data-ax5select="cboDeploy"]').ax5select("enable");
		}
	} else {		
		$('#cmdReq').prop('disabled',false);
	}
}

function cmdReqClick() {
	var tmpSayu = $('#txtSayu').val().trim();
	var i = 0;
	
	if (ingSw) {
		dialog.alert("현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	
	var orderListSelected = thirdGrid.getList("selected");
	if(getSelectedVal('cboSys').cm_sysinfo.substr(11,1) == '0' && orderListSelected.length == 0) {
		dialog.alert('업무지시서를 선택하여 주시기 바랍니다.');
		return;
	}
	
	if (tmpSayu.length.length < 10){
		dialog.alert("공백 제외 10자 이상 변경사유를 입력하여 주시기 바랍니다.");
		ingSw = false;
		$('#txtSayu').focus();
		return;
	}
	
	/*if (Number($('#txtDevTime').val()) < 1){
		dialog.alert("개발기간을 확인하십시오.");
		ingSw = false;
		$('#txtDevTime').focus();
		return;
	}*/
	
	if (getSelectedVal('cboGyulCheck').cm_micode == '01') {
		if($('#txtTestDate').val().length == 0) {
			dialog.alert('Test일자를 입력하여 주시기 바랍니다.');
			ingSw = false;
			return;
		}
		
		var today = getDate('DATE',0);
		var testDate = replaceAllString($('#txtTestDate').val(),'/','');
		if (today < testDate) {
			dialog.alert("Test일자가 현재일 이후입니다. 정확히 선택하여 주십시오.");
			ingSw = false;
			return;
		}
		
		if($('#txtTestSayu').val().length == 0) {
			dialog.alert('신규 및 변경 요구사항을 입력하여 주시기 바랍니다.');
			$('#txtTestSayu').focus();
			ingSw = false;
			return;
		}
	}
	
	/*if(getSelectedVal('cboReqSayu').cm_micode == '00') {
		dialog.alert('적용근거를 선택하여 주시기 바랍니다.');
		ingSw = false;
		return;
	}*/
	
	if (secondGrid.list.length == 0) {
		dialog.alert('신청 할 파일을 추가하여 주십시오.');
		ingSw = false;
		return;
	}
	
	//testPopUp = eCmr0202_tab(PopUpManager.createPopUp(this, eCmr0202_tab, true));
	reqCheckModalFiles = [];
	reqCheckModalTestInfo = [];
	setTimeout(function() {
		modalId = reqCheckModal.open({
//			width: screen.width - 200,
			width: window.innerWidth - 200,
	        height: window.innerHeight - 150,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ReqCheckModal.jsp"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	mask.close();
	            	if(reqCheckModalVal == '1'){
	            		console.log('reqCheckModalFiles==>',reqCheckModalFiles);
	            		console.log('reqCheckModalTestInfo==>',reqCheckModalTestInfo);
	            		//cmdReqClick2();
	            	} else {
	            		dialog.alert("[검증사항]을 완료하셔야 적용요청 하실 수 있습니다.");
	            		ingSw = false;
	                    return;
	            	}
	            }
	        }
		});
	}, 200);	
}

function cmdReqClick2() {
	var tmpArray = [];
	for (var x=0;x<secondGridData.length;x++) {
		if (secondGridData[x].prcseq == "") {
			dialog.alert("우선순위를 입력한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
			ingSw = false;
			return;
		}
		
		//RSA모델이면서 최종적용일이 2017/03/25일 이전인 경우 별도의 체크로직 추가  (20181022) cis
// 2022.10.11 flex와 다르게 처리돼서 주석 처리
//		if (secondGridData[x].cr_rsrccd == "25" && Number(secondGridData[x].lstdat) < 20170325) {
//			tmpArray.push(secondGridData[x]);
//		}
		
		for (var y=x+1;y<secondGridData.length;y++) {
			if (secondGridData[x].cr_itemid != null && secondGridData[x].cr_itemid != "") {
				if (secondGridData[x].cr_itemid == secondGridData[y].cr_itemid) {
					dialog.alert("동일한 프로그램이 중복으로 요청되었습니다. 조정한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
					ingSw = false;
					return;
				}
			} else {
				if (secondGridData[x].cr_syscd == secondGridData[y].cr_syscd &&
					secondGridData[x].cr_dsncd == secondGridData[y].cr_dsncd &&
					secondGridData[x].cr_rsrcname == secondGridData[y].cr_rsrcname) {
					dialog.alert("동일한 프로그램이 중복으로 요청되었습니다. 조정한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
					ingSw = false;
					return;
				}
			}
		}
	}
	
	if (ingSw == true) {
		Alert.show("현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}

// 2022.10.11 flex와 다르게 처리돼서 주석 처리
//	if(tmpArray.length > 0) {
//		var paramData = new Object();
//		paramData = {
//				rsaList	: tmpArray,
//			requestType :'getRsaCheck_Dev'
//		}
//		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', paramData, 'json');
//		if(ajaxReturnData != 'OK') {
//			confirmDialog.confirm({
//				title: '적용확인',
//				msg: '아래 모델은 운영버전의 모델이 아닐 수 있습니다. 계속 진행할까요?',
//			}, function(){
//				if(this.key === 'ok') {
//					cmdReqSub();
//				}
//			});
//		}
//	}else {
		cmdReqSub();
//	}
}

function cmdReqSub(){
	var strRsrcCd = "";
	var strQry = "";
	
	ingSw = true;
	var x=0;
	for (x=0 ; x<secondGridData.length ; x++) {
		if (strQry.length > 0) {
			if (strQry.indexOf(secondGridData[x].reqcd) < 0) {
				strQry = strQry + ",";
				strQry = strQry + secondGridData[x].reqcd;
			}
		} else strQry = secondGridData[x].reqcd;

		if (strRsrcCd.length > 0) {
			if (strRsrcCd.indexOf(secondGridData[x].cr_rsrccd) < 0) {
				strRsrcCd = strRsrcCd + ",";
				strRsrcCd = strRsrcCd + secondGridData[x].cr_rsrccd;
			}
		} else strRsrcCd = secondGridData[x].cr_rsrccd;
	}
	
	var tmpData = {
			  SysCd : getSelectedVal('cboSys').cm_syscd,
			  ReqCd : reqCd,
		     RsrcCd : strRsrcCd,
			 UserId : userId,
			  QryCd : strQry,
		requestType : 'confSelect'
	}	
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	if (ajaxReturnData != undefined && ajaxReturnData != null) {
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
			ingSw = false;
			return;
		}
	}
	
	if (ajaxReturnData == "C") {
	    confirmDialog.setConfig({
	    	title: "결재지정확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				confCall("Y");
			} else {
				confCall("N");
			}
		});
	} else if (ajaxReturnData == "Y") {
		confCall("Y");
    } else if (ajaxReturnData != "N") {
    	dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
		ingSw = false;
    } else {
		confCall("N");
    }
}

function confCall(GbnCd){
	var strQry = '';
	var emgSw = "N";
	var tmpRsrc = '';
	var tmpJobCd = '';
	var tmpPrjNo = '';
	
	/*
	var tmpArr = [];
	for(var x=0; x<orderAC.length; x++) {
		if(tmpArr.length == 0) {
			var tmpObj = new Object();
			tmpObj.CC_ORDERID = orderAC[x].CC_ORDERID;
			tmpObj.CC_REQSUB = orderAC[x].CC_REQSUB;
			tmpObj.CR_ITEMID = orderAC[x].CR_ITEMID;
            tmpArr.push(tmpObj);
		}else {
			for(var y=0; y<tmpArr.length; y++) {
				if(tmpArr[y].CC_ORDERID == orderAC[x].CC_ORDERID) break;
				else {
					tmpObj.CC_ORDERID = orderAC[x].CC_ORDERID;
					tmpObj.CC_REQSUB = orderAC[x].CC_REQSUB;
					tmpObj.CR_ITEMID = orderAC[x].CR_ITEMID;
		            tmpArr.push(tmpObj);
				}
			}
		}
	}
	
	tmpPrjNo = '';
	for(var y=0; y<tmpArr.length; y++) {
		if (tmpPrjNo.length > 0) {
			tmpPrjNo = tmpPrjNo + "," + tmpArr[y].CC_ORDERID;
		} else tmpPrjNo = tmpArr[y].CC_ORDERID;
	
	}
	*/
	
	for(var i=0 ; i<thirdGridData.length; i++){
		var row = thirdGridData[i];

		if(row.__selected__ == true){
			if(tmpPrjNo.length > 0){
				tmpPrjNo += "," + row.CC_ORDERID;
			}else{
				tmpPrjNo = row.CC_ORDERID;
			}
		}
	}
	
	tmpRsrc = '';
	strQry = '';
	for(var x=0;x<secondGridData.length;x++) {
		if (tmpRsrc.length > 0) {
			if (tmpRsrc.indexOf(secondGridData[x].cr_rsrccd) < 0)
	            tmpRsrc = tmpRsrc + "," + secondGridData[x].cr_rsrccd;
		} else tmpRsrc = secondGridData[x].cr_rsrccd;
	
		if (strQry.length > 0) {
			if (strQry.indexOf(secondGridData[x].reqcd) < 0) {
				strQry = strQry + "," + secondGridData[x].reqcd;
			}
		} else strQry = secondGridData[x].reqcd;
	}
	
	if(getSelectedVal('cboDeploy').cm_micode == '2') {
		emgSw = 'Y';
	}
	
	confirmInfoData = new Object();
	confirmInfoData.UserId = userId;
	confirmInfoData.ReqCd = reqCd;
	confirmInfoData.SysCd = getSelectedVal('cboSys').cm_syscd;
	confirmInfoData.RsrcCd = tmpRsrc;
	confirmInfoData.QryCd = strQry;
	confirmInfoData.EmgSw = emgSw;
	confirmInfoData.JobCd = tmpJobCd;
	confirmInfoData.deployCd = 'N';
	confirmInfoData.PrjNo = tmpPrjNo;
	confirmInfoData.passok = '';
	confirmInfoData.gyulcheck = getSelectedVal('cboGyulCheck').cm_micode;
	
	confirmData = [];	
	if (GbnCd == "Y") {
		setTimeout(function() {
			approvalModal.open({
		        width: 850,
		        height: 550,
		        iframe: {
		            method: "get",
		            url: "../modal/request/ApprovalModal.jsp"
			    },
		        onStateChanged: function () {
		            if (this.state === "open") {
		                mask.open();
		            }
		            else if (this.state === "close") {
		            	if(confirmData.length > 0){
		            		reqQuestConf();
		            	} else {
		            		ingSw = false;
		            		modalId = null;
		                	reqCheckModal.close();
		            	}
		                mask.close();
		            }
		        }
			});
		}, 200);	
	} else if (GbnCd == "N") {
		
		var data = {
			UserId : userId,
			SysCd : getSelectedVal('cboSys').cm_syscd,
			ReqCd : reqCd,
			RsrcCd : tmpRsrc,
			PgmType : '',
			EmgSw : emgSw,
			PrjNo : tmpPrjNo,	
			deployCd : 'N',
			QryCd : strQry,
			passok : '',
			gyulcheck : getSelectedVal('cboGyulCheck').cm_micode
		}
		
		var tmpData = {
			paramMap	: data,
			requestType : 'Confirm_Info'
		}
		
		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json');
		if (ajaxReturnData != undefined && ajaxReturnData != null) {
			if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
				dialog.alert(ajaxReturnData);
			} else {
				confirmData = ajaxReturnData;
				for(var i=0; i<confirmData.length ; i++){
					if (confirmData[i].arysv[0].SvUser == null || confirmData[i].arysv[0].SvUser == "" || confirmData[i].arysv[0].SvUser == undefined) {
						confirmData.splice(i,1);
						i--;
					}
				}
				reqQuestConf();
			}
		}
	}
}

function reqQuestConf( ){
	var requestData = new Object();
	requestData.UserID = userId;
	requestData.ReqCD = reqCd;
	requestData.Sayu = $('#txtSayu').val().trim();
	requestData.PassCd = '';
	requestData.cr_devptime = $('#txtDevTime').val().trim();
	requestData.EmgCd = '0';
	requestData.TstSw = getSelectedVal('cboSys').TstSw;
	if(reqCd == '04' && getSelectedVal('cboSys').cm_sysinfo.substr(3,1) == '0') {
		requestData.CC_SCMUSER = userId;
		requestData.CC_ACPTNO = '';
	}
	requestData.Deploy = getSelectedVal('cboDeploy').cm_micode;
	requestData.upload = "N";
	requestData.reqsayu = getSelectedVal('cboReqSayu').cm_micode;
	if(getSelectedVal('cboGyulCheck').cm_micode == '01') {
		requestData.testdate = replaceAllString($('#txtTestDate').val(),'/','');
		requestData.etcSayu = $('#txtTestSayu').val();
	}
	
	var tmpData = {
		chkInList	: secondGridData,
		etcData 	: requestData,
		befJob		: [],	
		ConfList 	: confirmData,
		TestList 	: reqCheckModalTestInfo,
		OrderList 	: orderAC ,
		confFg 		: 'Y',
		requestType : 'request_Check_In'
	}
	
	console.log('[request_Check_In] ==>', tmpData);
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) {
		dialog.alert('[' + $('#cmdReq').text() + '] 중 오류가 발생하였습니다. [request_Check_In]');
		ingSw = false;
		$('#cmdReq').prop('disabled',true);
		return;
	}
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}
	if(ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR'){
		dialog.alert(ajaxReturnData.substr(5));
		ingSw = false;
		return;
	}
	
	strAcptNo = ajaxReturnData;
	
	upFiles = [];
	uploadLen = 0;
	//upFiles = clone(reqCheckModalFiles);
	console.log('reqCheckModalFiles=',reqCheckModalFiles.length);
	console.log('reqCheckModalFiles=',reqCheckModalFiles);
	if (reqCheckModalFiles.length>0) {
		startFileupload(upFiles);
	} else {
		fileSenderClose_sub();
	}
}

function startFileupload() {
	console.log('[startFileupload] isReChkIn==>',isReChkIn);
	if(isReChkIn) {
		//미개발 (flex에 return함수 없음)
		
		var tmpFileList = reqCheckModalFiles.filter(function(item) {
			if(item.isCopy == 'true') return true;
			else return false;
		});
		
		//Cmr0200.copyDoc2(strAcptNo, gridFile_dp);
		var data = new Object();
		data = {
			AcptNo : strAcptNo,
			FileList : tmpFileList,
			requestType	: 'copyDoc2'
		}
		ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json', successCopyDoc);
	}
	
	var cnt = 0;
	for(var i=0; i<reqCheckModalFiles.length; i++) {
		if(reqCheckModalFiles[i].isCopy == 'true') cnt++;
	}
	
	console.log('[startFileupload] reqCheckModalFiles==>',reqCheckModalFiles,cnt);
	if(cnt == reqCheckModalFiles.length) {
		fileSenderClose_sub();
		return;
	}
	
	var data = new Object();
	data = {
		ReqId : strAcptNo,
		ReqCd : reqCd,
		requestType	: 'maxFileSeq'
	}
	console.log('[maxFileSeq] ==>', data);
	var fileseq = ajaxCallWithJson('/webPage/ecmc/Cmc0010Servlet', data, 'json');
	if(Number(fileseq) < 1) {
		dialog.alert('파일첨부 일련번호 추출에 실패하였습니다.');
		return;
	}
	
	fileGbn = 'U';
	dirGbn = '21';
	popCloseFlag = false;
	
	attPath = attPath + '/' + strAcptNo;
	//attPath = 'C:\\html5\\temp\\'; //테스트중
	subDocPath = strAcptNo;
	
	reqCheckModalFiles = reqCheckModalFiles.filter(function(item) {
		if(item.isCopy == 'true') return false;
		else return true;
	});
	
	for(var i=0; i<reqCheckModalFiles.length; i++) {
		
		var filenm = '';
		filenm = fillZero(3,fileseq+'');
		
		var tmpFile = new Object();
		tmpFile = clone(reqCheckModalFiles[i]);
		tmpFile.savefile = strAcptNo + '/' + reqCd + filenm + '.' +reqCheckModalFiles[i].name.substring(reqCheckModalFiles[i].name.lastIndexOf(".")+1) ;
		tmpFile.saveFileName = reqCd + filenm + '.' +reqCheckModalFiles[i].name.substring(reqCheckModalFiles[i].name.lastIndexOf(".")+1);			//서버파일명
		tmpFile.fileseq = '' + fileseq;
		tmpFile.attfile = reqCheckModalFiles[i].name;			//실제파일명
		tmpFile.fullName = attPath+'/'+strAcptNo;	//첨부경로
		tmpFile.fullpath = attPath;					//첨부경로(pathcd=21)
		
		upFiles.push(tmpFile);
		
		++fileseq;
	}
	
	//upFiles = clone(fileGridData);
	console.log('[ApplyRequest.js] upFiles==>',upFiles);
	
	setTimeout(function() {
		fileUploadModal.open({
			width: 685,
			height: 420,
			iframe: {
				method: "get",
				url: "../modal/fileupload/ComFileUpload.jsp"
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

function successCopyDoc(data) {
	console.log('[successCopyDoc] ==>', data);
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
}

// ComFileUpload 함수에서 호출
function setFile(fileList) {
	console.log('[ApplyRequest.js] setFile ==>', fileList);
	
	fileList.forEach(function(item, Index) {
		var tmpObj = new Object();
		tmpObj.reqid = strAcptNo;
		tmpObj.reqcd = reqCd;
		tmpObj.subreq = '00';
		tmpObj.savefile = item.savefile;	//요청번호/서버파일명
		tmpObj.attfile = item.name;			//실제파일명	
		tmpObj.fileseq = item.fileseq;
		tmpObj.userid = userId;
		
		var data = {
			docFile : tmpObj,
			requestType : 'setDocFile'
		}
		console.log('[ApplyRequest.js] setDocFile ==>', data);
		ajaxAsync('/webPage/ecmc/Cmc0010Servlet', data, 'json', successSetDocFile);
	});
}

function successSetDocFile(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	if(data != 'OK') {
		dialog.alert(data);
		requestEnd();
		return;
	}
	
	uploadLen++;
	if(uploadLen == reqCheckModalFiles.length) requestEnd();
}

function fileSenderClose_sub() {
	var findSw = false;
	if (reqCd == "04" && getSelectedVal('cboSys').TstSw == "1")  {
	}else{
		for (var i=0; secondGridData.length>i ; i++) {
			if (secondGridData[i].cm_info.substr(44,1) == "1" && secondGridData[i].reqcd != "05" && secondGridData[i].reqcd != "09" )  {
				findSw = true;
				break;
			}
		}
	}
	if (findSw) {
		//Cmr0100.getProgFileList(strAcptNo,"F");
	} else {
		requestEnd();
	}
}

function requestEnd(){
	ingSw = false;
    
    dialog.alert('체크인 신청완료!',function(){
    	modalId = null;
    	reqCheckModal.close();
    	if(isReChkIn) {
    		close();
    	}
    });
    
    strAcptNo = '';
	uploadCk = false;
	upFiles = [];
	uploadLen = 0;
	firstGrid.setData([]);
	firstGridData = [];
	secondGrid.setData([]);
	secondGridData = [];
	confirmData = [];
	confirmInfoData = null;
	reqCheckModalFiles = [];
	reqCheckModalTestInfo = [];
	$('#cmdReq').prop('disabled',true);
	
	$('[data-ax5select="cboDeploy"]').ax5select("enable");
	//cboDeployChange();
	
	$('[data-ax5select="cboSys"]').ax5select('enable');
    //changeSys();
    
    $('[data-ax5select="cboReq"]').ax5select('enable');
    //cboReqChange();
}

function openWindow(type,param) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   	//폼 name
    f.user.value = userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 'DETAIL') {		//신청상세
        f.acptno.value = param;
	    cURL = "/webPage/winpop/PopRequestDetail.jsp";
	}else if (type == '2') {	//업무지시서상세(eCmc0501.mxml)    
        f.orderId.value = param;
	    cURL = "/webPage/winpop/PopOrderListDetail.jsp";
	}else {
		dialog.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

function openProgModal(item) {
	if(item.itemid == null || item.itemid == '' || item.itemid == undefined) {
		dialog.alert('프로그램정보가 누락되었습니다. [eCAMS 관리자 문의]');
		return;
	}
	
	modalObj = new Object();
	modalObj.syscd = getSelectedVal('cboSys').cm_syscd;
	modalObj.userid = userId;
	modalObj.sysmsg = getSelectedVal('cboSys').cm_sysmsg;
	modalObj.rsrcname = item.cr_rsrcname;
	modalObj.itemid = item.cr_itemid;
	modalObj.path = item.cm_dirpath;
	
	setTimeout(function() {
		progModal.open({
			width: 1000,
			height: 750,
			iframe: {
				method: "get",
				url: "../modal/request/ProgramInfo_relat.jsp"
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

function showAndHideUploadModal(showAndHide) {
	console.log('[showAndHideUploadModal] ==>' + showAndHide + ',' + modalId.config.id)
	if(showAndHide === 'show') {
		$('#' + modalId.config.id).css('display', 'block');
	}
	if(showAndHide === 'hide') {
		$('#' + modalId.config.id).css('display', 'none');
		mask.close();
		cmdReqClick2();
	}
}


//20221222
//엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir);
}

//엑셀 파일 업로드시 파일 올릴 경로 가져오기 완료 AND 업로드할때 사용할 JSP 파일명 가져오기
function successGetTmpDir(data) {
	tmpPath = data;
	if(typeof(tmpPath) == "object"){
		tmpPath = data[0].cm_path;
	}

	var data = new Object();
	data = {
		pCode 		: 'F2',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetUploadJspName);
}

//업로드할때 사용할 JSP 파일명 가져오기 완료
function successGetUploadJspName(data) {
	uploadJspFile = data;
	if(typeof(uploadJspFile) == "object"){
		uploadJspFile = data[0].cm_path;
	}
}

//엑셀 파일 업로드시 파일타입 체크
function fileTypeCheck(obj) {
	var pathpoint = obj.value.lastIndexOf('.');
	var filepoint = obj.value.substring(pathpoint+1,obj.length);
	filetype = filepoint.toLowerCase();
	if(filetype=='xls' || filetype=='xlsx') {
		startUpload();
	} else {
		dialog.alert('엑셀 파일만 업로드 가능합니다.', function() {});
		parentObj  = obj.parentNode
		node = parentObj.replaceChild(obj.cloneNode(true),obj);
		return false;
	}
}

//엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;

	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	//tmpPath = 'C:\\html5\\temp\\'; //테스트중
	formData.append('fullName',tmpPath+userId+"_excel_checkout.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);

  $.ajax({
		url: homePath+'/webPage/fileupload/'+uploadJspFile,
      type:'POST',
      data:formData,
      async:false,
      cache:false,
      contentType:false,
      processData:false
  }).done(function(response){
  	onUploadCompleteData(response);
  }).fail(function(xhr,status,errorThrown){
  	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
  }).always(function(){
  	// file 초기화
  	var agent = navigator.userAgent.toLowerCase();
  	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
  	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
  	} else {
  	    $("#excelFile").val("");
  	}
  });
}

//엑셀파일 완료 후
function onUploadCompleteData(filePath){
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");

	headerDef.push("dirpath");
	headerDef.push("rsrcname");
	
	tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

//읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}

	for (var i=0;data.length>i;i++) {
		for (var j=i+1;data.length>j;j++) {
			if (data[i].dirpath == data[j].dirpath && data[i].rsrcname == data[j].rsrcname) {
				data.splice(i,1);
				i--;
				break;
			}
		}
	}
	
	firstGrid.setData([]);
	firstGridData = [];
	
	var tmpObj = {};
	tmpObj.UserId = userId;
	tmpObj.SysCd = getSelectedVal('cboSys').cm_syscd;
	tmpObj.SinCd = reqCd;
	tmpObj.ReqCd = getSelectedIndex('cboReq') == 0 ? '00' : getSelectedVal('cboReq').cm_micode;
	tmpObj.TstSw = getSelectedVal('cboSys').TstSw;
	tmpObj.RsrcName = $("#txtProg").val();
	tmpObj.DsnCd = "";
	tmpObj.DirPath = "";
	tmpObj.SysInfo = getSelectedVal('cboSys').cm_sysinfo;
	tmpObj.RsrcCd = "00";
	if($('#chkUpLow').is(':checked')) tmpObj.UpLowSw = 'true';
	else tmpObj.UpLowSw = 'false';
	if($('#chkSelf').is(':checked')) tmpObj.selfSw = 'true';
	else tmpObj.selfSw = 'false';
	if($('#chkLike').is(':checked')) tmpObj.LikeSw = 'true';
	else tmpObj.LikeSw = 'false';
	tmpObj.txtORDERInfo = "";
	tmpObj.itemid = "";
		
	var paramData = new Object();
	paramData = {
		paramMap	: tmpObj,
		fileList : data,
		requestType :'getFileList_excel'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	$("#btnFind").prop("disabled", true);
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', paramData, 'json', successGetFileList_excel);
}

function successGetFileList_excel(data){
	$(".loding-div").remove();
	console.log(data);
	if (data == undefined || data == null) return;
	if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
		dialog.alert(data);
	} else {
		firstGridData = data;
		firstGridData.forEach(function(item) {
			item.cr_lstver = Number(item.cr_lstver);
		});
		firstGridData.sort(function(a,b){
			return a.cr_rsrcname < b.cr_rsrcname ? -1 : a.cr_rsrcname > b.cr_rsrcname ? 1 : 0;
		});
		
		if (firstGridData.length == 0 ) {
			dialog.alert('검색 결과가 없습니다.');
			return;		
		} else if (firstGridData.length > 0) {
			if(firstGridData[0].ID =='MAX'){
				dialog.alert('검색결과가 너무 많으니 검색조건을 입력하여 검색하여 주시기 바랍니다.');
				return;
			}
		}
		$(firstGridData).each(function(i){
			$(secondGridData).each(function(j){
				if(firstGridData[i].cr_itemid == secondGridData[j].cr_itemid){
					firstGridData[i].selected_flag = '1';
					firstGridData[i].selected = '0';
					firstGridData[i].enabled = '0';
					return true;
				}
			});
			if(firstGridData[i].selected_flag == "1"){
				firstGridData[i].__disable_selection__ = true;
			}
		});
		firstGrid.setData(firstGridData);
	}
}