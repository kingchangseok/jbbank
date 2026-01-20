/**
 * 로컬신청
 * 
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-06-09
 * 
 */

var userId 		= window.top.userId;
var reqCd 		= window.top.reqCd;
var adminYN 	= window.top.adminYN;
var strReqCD 	= '';

var datReqDate 	= new ax5.ui.picker();
var firstGrid	= new ax5.ui.grid();
var secondGrid	= new ax5.ui.grid();

var firstGridData 	= [];
var orifirstGridData= [];
var secondGridData	= [];
var requestData		= [];

var cboReqData 		= [];
var cboDeployData	= [];
var cboSysData		= [];
var cboJobData		= [];
var cboJawonData	= [];
var svrData			= [];

var strAplyDate = ""; 	  //특정배포일시
var acptNo 		= "";    //신청번호

/***** 동기화처리로그확인 모달 관련 변수 *****/
var localSyncResultModal = new ax5.ui.modal();
var localSyncResultData = [];
var localSyncResultFlag = false;

/***** 결재절차확정 모달 관련 변수 *****/
var approvalModal 	= new ax5.ui.modal();
var confirmData 	= [];
var confirmInfoData = null;

/***** 적용대상서버 *****/
var svrList 		= [];
var selSvrModal		= new ax5.ui.modal();
var selSvrModalData = [];
var selSvrModalFlag = false;

/***** 영향분석 *****/
var checkoutData	= [];

/***** 파일첨부 관련 변수 *****/
var fileUploadModal2	= new ax5.ui.modal();
var uploadCk 			= false;
var upFiles 			= [];
var strAcptNo			= '';
var gubunName 			= "";
var fileGb 				= '1';
var testResultFiles 	= [];
var docFiles 			= [];

/***** 선행작업 관련 변수 *****/
var befJobModal 	= new ax5.ui.modal();
var befJobData 		= [];
var befCheck 		= false;

/***** 트리 관련 변수 *****/
var rMenu		= null;
var ztree 		= null;
var treeData 	= [];
var selectedNode= null;
var setting = {
	data: {
		simpleData: {
			enable: true,
		}
	},
	callback:{
		onExpand: myOnExpand,
		onClick : treeDir_nodeClick,
		onDblClick : treeDir_doubleClick,
		onRightClick: OnRightClickTree,
	}
};

var ingSw	= false;
var diffSw	= false;
var dirGbn	= 'N';

$('#txtReqDate').val(getDate('DATE',0));
datReqDate.bind(defaultPickerInfo('txtReqDate','bottom'));

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$("label[for='chkEmg']").css({'color':'#ff0000'});

$('[data-ax5select="cboSys"]').ax5select({
	options : []
});

$('[data-ax5select="cboJob"]').ax5select({
	options : []
});

$('[data-ax5select="cboJawon"]').ax5select({
	options : []
});

$('[data-ax5select="cboReq"]').ax5select({
	options : []
});

$('[data-ax5select="cboDeploy"]').ax5select({
	options : []
});

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
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	addDataRow();
        },
    	trStyleClass: function () {
    		if (this.item.selected_flag == "1") {
    			return 'fontStyle-cncl';
    		}else if (this.item.color == "E" || this.item.color == "S") {
    			return 'fontStyle-ing';
    		}else {
    			return '';
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{key: "view_dirpath",	label: "디렉토리", 		width: "15%",	align: "left"},
		{key: "filename",		label: "파일명", 			width: "10%", 	align: "left"},
		{key: "localSize",		label: "[PC]파일Size", 	width: "6%", 	align: "right", hidden: 'true'},
		{key: "localDate",		label: "[PC]수정한날짜", 	width: "6%", 	align: "center", hidden: 'true'},
		{key: "diffRst", 		label: '로컬비교결과',  	width: '8%', 	hidden: 'true',
        	formatter: function() {
        		if (firstGrid.getList().length > 0) {
        			if(this.value == '불일치[수정]' || this.value == '수정') {
        				return '<button style="width: 98%; height: 98%; text-decoration: underline;">' + this.value +'</button>';
        			}else {
        				return '<button style="width: 98%; height: 98%;">'+ this.value +'</button>';
        			}
        		} else {
        			return '<label></label>';
        		}
        	}
        },
		
		{key: "sayu",		label: "신청사유", 	width: "10%",	align: "left", hidden: 'true'},
		{key: "sta",		label: "형상상태", 	width: "6%", 	align: "left"},
		{key: "cr_lstver",	label: "버전", 		width: "6%", 	align: "center"},
		{key: "cm_jobname",	label: "업무"	, 		width: "6%", 	align: "left"},
		{key: "jawon",		label: "프로그램종류",	width: "6%", 	align: "left"},
		{key: "cr_lstusr",	label: "최종변경인", 	width: "6%", 	align: "center"},
		{key: "lastdate",	label: "변경일시", 	width: "8%", 	align: "center"},
		{key: "cr_story",	label: "프로그램설명",	width: "10%", 	align: "left"}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="second-grid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: true, 
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	deleteDataRow();
        },
    	trStyleClass: function () {
    		if(strReqCD == "SYNC") return '';
    		
    		if (this.item.color == "E") {
    			return 'fontStyle-cncl';
    		}else if (this.item.color == "S") {
    			return 'fontStyle-ing'
    		}else {
    			return '';
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{key: 'selected', 	label: 'Overwrite여부',	width: '10%', align: 'center',
    		editor: {
    			type: "checkbox", config: {height: 17, trueValue: "1", falseValue: "0"}
    		}
    	},
    	{key: "view_dirpath",	label: "디렉토리", 		width: "15%",	align: "left"},
		{key: "filename",		label: "파일명", 			width: "10%", 	align: "left"},
		{key: 'diffRst', 		label: '로컬비교결과',  	width: '8%', 	hidden: 'true',
        	formatter: function() {
        		if (firstGrid.getList().length > 0) {
        			if(this.value == '불일치[수정]' || this.value == '수정') {
        				return '<button style="width: 98%; height: 98%; text-decoration: underline;">' + this.value +'</button>';
        			}else {
        				return '<button style="width: 98%; height: 98%;">'+ this.value +'</button>';
        			}
        		} else {
        			return '<label></label>';
        		}
        	}
        },
        {key: "lastdate",	label: "[형상]변경일시", 	width: "8%", 	align: "center"},
		{key: "localDate",	label: "[PC]수정한날짜", 	width: "8%", 	align: "center"},
		{key: "sta",		label: "형상상태", 		width: "6%", 	align: "left"},
		{key: "localSize",	label: "[PC]파일Size", 	width: "6%", 	align: "right"},
		{key: "cr_lstusr",	label: "최종변경인", 		width: "6%", 	align: "center"},
		{key: "sayu",		label: "신청사유", 		width: "6%", 	align: "left"}
    ]
});

$(document).ready(function() {
	rMenu = $("#rMenu");
	
	//적용대상서버
	$('#chkSvr').wCheck('check', false);
	$('#chkSvr').wCheck('disabled', true);
	$('#divSvr').css('display', 'none');
	
	//신청 컴포넌트(하단)
	//$('#divReq2').css('visibility', 'hidden');
	$('#divReq2').css('display', 'none');
	
	//적용시간
	$('#divApplyDate').css('display', 'none');
	
	$('#btnExl').show();
	$('#btnExl2').hide();
	
	getCodeInfo();
	screenInit();
	
	//엑셀저장
	$("#btnExl").bind('click', function() {
		excelExport(firstGrid,"LocalFileList.xls");
	});
	
	//일괄등록양식으로저장
	$("#btnExl2").bind('click', function() {
		btnExl2_Click();		
	});
	
	$("#cboSys").bind('change', function() {
		cboSys_Change();
	});
	
	//신청구분
	$("#cboReq").bind('change', function() {
		cboReq_Change();
	});
	
	//적용구분
	$("#cboDeploy").bind('change', function() {
		cboDeploy_Change();
	});
	
	$("#cboJawon").bind('change', function() {
		$('#txtExeName').val('');
		if(getSelectedIndex('cboJawon') < 1) return;
		$('#txtExeName').val(getSelectedVal('cboJawon').cm_exename);
	});
	
	//미대상제외
	$("#chkOk").bind('click', function() {
		chkOK_Click();
	});
	
	//적용서버선택
	$("#chkSvr").bind('click', function() {
		if ($('#chkSvr').is(':checked')) {
			$("#btnSvr").show();
		}else {
			$("#btnSvr").hide();
		}
	});
	
	// 적용대상서버
	$("#btnSvr").bind('click', function() {
		btnSvr_Click();
	});
	
	// 선택한 경로 파일추출
	$('#getPrgList').bind('click', function() {
		hideRMenu();
		if (cboReq.selectedIndex == 0) return;
		dirGbn = 'N';
		getProgrameList();
	});
	
	// 하위폴더포함하여 파일추출
	$('#getPrgList2').bind('click', function() {
		hideRMenu();
		if (cboReq.selectedIndex == 0) return;
		dirGbn = 'A';
		getProgrameList();
	});
	
	// 추가
	$('#btnAdd').bind('click',function(){
		addDataRow();
	});
	
	// 제거
	$('#btnDel').bind('click',function(){
		deleteDataRow()
	});
	
	// 등록/실행 (동기화, 신규등록)
	$('#btnCmd').bind('click',function(){
		request();
	});
	
	// 신청 (체크아웃, 체크아웃취소, 테스트적용요청, 운영적용요청)
	$('#btnReq').bind('click',function(){
		request();
	});
	
	// 조회
	$('#btnSearch').bind('click',function(){
		filterPgm();
	});
	
	// 프로그램명 조회
	$('#txtRsrcName').bind('keypress', function(event){
		if(event.keyCode==13) {
			filterPgm();
		}
	});
	
	$('#btnFile').bind('click',function(){
		fileUploadModal2.open({
		    width: 685,
		    height: 420,
		    iframe: {
		        method: "get",
		        url: "../modal/fileupload/FileUpload2.jsp"
			},
		    onStateChanged: function () {
		        if (this.state === "open") {
		            mask.open();
		        } else if (this.state === "close") {
		            mask.close();
		        }
		    }
		});
	});
	
	// 비교 (localDiff2) : 로컬비교 실행
	$('#btnDiff_top').bind('click',function(){
		if(firstGridData == null || firstGridData == undefined || firstGridData.length == 0) return;
		//Cmr4100_3.reqDiff(cboSys.selectedItem.cm_syscd, strUserId, strReqCD, svr_dp.toArray(), qry_dp.toArray());
		var tmpInfoData = new Object();
		tmpInfoData = {
			sysCd : getSelectedVal('cboSys').cm_syscd,
			userId : userId,
			reqgbn : strReqCD,
			svrInfo : svrData,
			reqArr : firstGridData,
			requestType	: 'reqDiff'
		}
		$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpInfoData, 'json', successReqDiff_top);
	});
	
	// 비교 (localDiff) : 로컬비교 실행
	$('#btnDiff_bottom, #btnDiff_bottom2').bind('click',function(){
		if(secondGridData == null || secondGridData == undefined || secondGridData.length == 0) return;
		//Cmr4100.reqDiff(cboSys.selectedItem.cm_syscd, strUserId, strReqCD, svr_dp.toArray(), grdLst2_dp.toArray());
		var tmpInfoData = new Object();
		tmpInfoData = {
			sysCd : getSelectedVal('cboSys').cm_syscd,
			userId : userId,
			reqgbn : strReqCD,
			svrInfo : svrData,
			reqArr : secondGridData,
			requestType	: 'reqDiff'
		}
		$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpInfoData, 'json', successReqDiff);
	});
	
	// 선행작업
	$('#chkBef').bind('change',function(){
		if($("#chkBef").parent(".wCheck").css('display') == "none"){
			return;
		}
		
		if (befCheck){
			befCheck = false;
			return;
		}
		
		if($('#chkBef').is(':checked')){
			setTimeout(function() {
				befJobModal.open({
					width: 915,
					height: 580,
					iframe: {
						method: "get",
						url: "../modal/request/BefJobSetModal.jsp"
					},
					onStateChanged: function () {
						if (this.state === "open") {
							mask.open();
						}
						else if (this.state === "close") {
							if(befJobData.length == 0){
								befCheck = true;
								$('#chkBef').wCheck('check',false);
							}
							else{
								befCheck = true;
								$('#chkBef').wCheck('check',true);
							}
							mask.close();
						}
					}
				});
			}, 200);
		} else {
			if (befJobData.length == 0) return;
			
            mask.open();
            confirmDialog.setConfig({
                title: "확인",
                theme: "info"
            });
			confirmDialog.confirm({
				msg: '기 선택된 선행작업이 있습니다. \n 체크해제 시 선행작업이 무시됩니다. \n계속 진행할까요?',
				btns :{
					ok: {
                        label:'ok'
                    }, cancel: {
                        label:'cancel'
                    }, other: {
                        label:'선행작업확인'
                    }
				}
			}, function(){
				if(this.key === 'ok') {
					befJobData = [];
				} else if (this.key ==='other'){
					openBefJobSetModal();
				} else{
					befCheck = true;
					$('#chkBef').wCheck('check',true);
				}
                mask.close();
			});
		}
	});
});

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('REQUEST','','N','3',''),
		new CodeInfo('REQPASS','','N')
	]);
	cboReqData		= codeInfos.REQUEST;
	cboDeployData	= codeInfos.REQPASS;
	
	cboReqData.unshift({cm_macode: "REQUEST", cm_micode: "NEW", cm_codename: "신규등록"});
	cboReqData.unshift({cm_macode: "REQUEST", cm_micode: "SYNC",cm_codename: "동기화"});
	cboReqData.unshift({cm_macode: "REQUEST", cm_micode: "00", 	cm_codename: "선택하세요"});
	
	cboReqFilter(cboReqData);
	$('[data-ax5select="cboDeploy"]').ax5select({
        options: injectCboDataToArr(cboDeployData, 'cm_micode' , 'cm_codename')
	});
	
	//SysInfo.getSysInfo(strUserId,SecuYn,"SEL","N","04");
	var tmpData = {
		UserId	: userId,
		SecuYn	: adminYN ? 'N' : 'Y',
		SelMsg	: 'SEL',
		CloseYn	: 'N',
		ReqCd	: '04',
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpData, 'json', successGetSysInfo);
}

function cboReqFilter(data) {
	var fcboReqData = [];
	fcboReqData = data.filter(function(item) {
		if(getSelectedIndex('cboSys') > 0) {
			if(item.cm_micode == '00' || item.cm_micode == 'SYNC' || item.cm_micode == 'NEW'
				|| item.cm_micode == '01' || item.cm_micode == '11' || item.cm_micode == '04') {
				return true;
			}else {
				if(getSelectedVal('cboSys').TstSw == '1') {
					if(item.cm_micode == '03') return true;
				}
				if(getSelectedVal('cboSys').DevSw == '1') {
					if(item.cm_micode == '05') return true;
				}
				return false;
			}
		}else {
			if(item.cm_micode == '02' || item.cm_micode == '06' || item.cm_micode == '05') return false;
			else return true;
		}
	});
	
	$('[data-ax5select="cboReq"]').ax5select({
        options: injectCboDataToArr(fcboReqData, 'cm_micode' , 'cm_codename')
	});
}

function cboDeployFilter(data) {
	var fcboDeployData = [];
	fcboDeployData = data.filter(function(item) {
		if(strReqCD != '04') return true;
		
		if(getSelectedVal('cboSys').cm_sysinfo.substr(5,1) == '0' && item.cm_micode == '0') return false;
		else return true;
	});
	
	$('[data-ax5select="cboDeploy"]').ax5select({
        options: injectCboDataToArr(fcboDeployData, 'cm_micode' , 'cm_codename')
	});
	
	if(fcboDeployData.length > 0) {
		if(strReqCD == '04') cboDeploy_Change();
	}
}

function cboReq_Change() {
	$('#chkOk').wCheck('check',false);
	$('#btnCmd').prop('disabled',true);
	$('#btnReq').prop('disabled',true);
	firstGridData = [];
	firstGrid.setData([]);
	secondGridData = [];
	secondGrid.setData([]);
	treeData = [];
	ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
	
	if(cboSysData == null || cboSysData == undefined || cboSysData.length == 0 || getSelectedIndex('cboSys') == 0) return;
	
	if(ingSw) {
		strReqCD = '';
		$('[data-ax5select="cboReq"]').ax5select('setValue',cboReqData[0].cm_micode,true);
		return;
	}
	
	$('#divTree').css('display', 'none');
	$('#divGrid').css('width', '100%');
	
	if(getSelectedIndex('cboReq') == 0) {
		strReqCD = '';
		return;
	}
	
	if(getSelectedVal('cboReq').cm_micode == 'SYNC' || getSelectedVal('cboReq').cm_micode == '01') {
		var paramObj = {
			syscd : getSelectedVal('cboSys').cm_syscd,
			userid : userId,
		}
		var tmpInfoData = new Object();
		tmpInfoData = {
			paramObj : paramObj,
			requestType	: 'getLocalPath_ztree'
		}
		ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successGetLocalPath);
		
		strReqCD = getSelectedVal('cboReq').cm_micode;
		screenInit();
	}else if(getSelectedVal('cboReq').cm_micode == 'NEW') {
		treeData = [];
		ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
		getLocalDirTree();
		
		strReqCD = getSelectedVal('cboReq').cm_micode;
		screenInit();
	}else {
		strReqCD = getSelectedVal('cboReq').cm_micode;
		screenInit();
	}
}

function successGetLocalPath(data) {
	treeData = data;
	if(treeData !== 'ERR') {
		var obj = treeData;
		
		for(var i in treeData){
			if(obj[i].cm_codename =='' ){
				delete obj[i]
				continue;
			}
			obj[i].isParent = true;
		}
		obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
		obj = JSON.parse(obj);
		treeData = obj;
		
		ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
	}
}

function screenInit() {
	var firstGridColumns = firstGrid.columns;
	firstGridColumns.forEach(function(data, i) {
		if(firstGridColumns[i].key == 'localSize1'){
			firstGrid.updateColumn({key: "localSize",		label: "[PC]파일Size", 	width: "6%", 	align: "right", hidden: 'true'}, i);
		}else if(firstGridColumns[i].key == 'localDate1') {
			firstGrid.updateColumn({key: "localDate",		label: "[PC]수정한날짜", 	width: "6%", 	align: "center", hidden: 'true'}, i);
		}else if(firstGridColumns[i].key == 'diffRst1') {
			firstGrid.updateColumn({key: 'diffRst', 		label: '로컬비교결과',  	width: '8%', 	hidden: 'true'}, i);
		}
	});
	
	if(strReqCD == 'NEW') {
		$("#btnDiff_top").hide();
		$("#btnDiff_bottom").hide();
		$("#btnDiff_bottom2").hide();
		
		if(strReqCD == '' || $('#txtLocalDir').val().length == 0) {
			$('#btnCmd').prop('disabled',true);
			$('#btnReq').prop('disabled',true);
		}else if(secondGrid.getList().length > 0) {
			$('#btnCmd').prop('disabled',false);
			$('#btnReq').prop('disabled', false);
		}else {
			$('#btnCmd').prop('disabled',true);
			$('#btnReq').prop('disabled',true);
		}
		
	}else if(strReqCD == 'SYNC') {
		$("#btnDiff_top").show();
		$("#btnDiff_bottom").hide();
		$("#btnDiff_bottom2").hide();
		
		firstGridColumns = firstGrid.columns;
		firstGridColumns.forEach(function(data, i) {
			if(firstGridColumns[i].key == 'localSize'){
				firstGrid.updateColumn({key: "localSize",		label: "[PC]파일Size", 	width: "6%", 	align: "right"}, i);
			}else if(firstGridColumns[i].key == 'localDate') {
				firstGrid.updateColumn({key: "localDate",		label: "[PC]수정한날짜", 	width: "6%", 	align: "center"}, i);
			}else if(firstGridColumns[i].key == 'diffRst') {
				firstGrid.updateColumn({key: 'diffRst', 		label: '로컬비교결과',  	width: '8%'}, i);
			}
		});
		
		if(strReqCD == '' || $('#txtLocalDir').val().length == 0) {
			$('#btnDiff_top').prop('disabled',true);
		}else {
			$('#btnDiff_top').prop('disabled',false);
		}
	}else {
		$("#btnDiff_top").hide();
		$("#btnDiff_bottom").show();
		$("#btnDiff_bottom2").show();
		
		if(strReqCD == '' || $('#txtLocalDir').val().length == 0) {
			$('#btnDiff_bottom').prop('disabled',true);
			$('#btnDiff_bottom2').prop('disabled',true);
		}else {
			$('#btnDiff_bottom').prop('disabled',false);
			$('#btnDiff_bottom2').prop('disabled',false);
		}
	}
	
	var secondGridColumns = secondGrid.columns;
	if (strReqCD == "01" || strReqCD == "11" || strReqCD == "12") {
		secondGridColumns.forEach(function(data, i) {
			if(secondGridColumns[i].key == 'selected'){
				secondGrid.updateColumn({key: 'selected', 	label: 'Overwrite여부',	width: '10%', align: 'center',
		    		editor: {
		    			type: "checkbox", config: {height: 17, trueValue: "1", falseValue: "0"}
		    		}
		    	}, i);
				return;
			}
		});
	} else {
		secondGridColumns.forEach(function(data, i) {
			if(secondGridColumns[i].key == 'selected'){
				secondGrid.updateColumn({key: 'selected', 	label: 'Overwrite여부',	width: '10%', align: 'center', hidden: 'true',
		    		editor: {
		    			type: "checkbox", config: {height: 17, trueValue: "1", falseValue: "0"}
		    		}
		    	}, i);
				return;
			}
		});
	}
	
	firstGridColumns = firstGrid.columns;
	firstGridColumns.forEach(function(data, i) {
		if(firstGridColumns[i].key == 'sayu'){
			firstGrid.updateColumn({key: "sayu",		label: "신청사유", 	width: "10%",	align: "left", hidden: 'true'}, i);
		}
	});
	
	secondGridColumns = secondGrid.columns;
	secondGridColumns.forEach(function(data, i) {
		if(secondGridColumns[i].key == 'sayu'){
			secondGrid.updateColumn({key: "sayu",		label: "신청사유", 	width: "6%", 	align: "left", hidden: 'true'}, i);
		}
	});
	
	if (strReqCD == "NEW") {
		$('#btnExl2').show();
		
		secondGridColumns = secondGrid.columns;
		secondGridColumns.forEach(function(data, i) {
			if(secondGridColumns[i].key == 'localSize'){
				secondGrid.updateColumn({key: "localSize",	label: "[PC]파일Size", 	width: "6%", 	align: "right", hidden: "true"}, i);
			}else if(secondGridColumns[i].key == 'localDate') {
				secondGrid.updateColumn({key: "localDate",	label: "[PC]수정한날짜", 	width: "6%", 	align: "center", hidden: "true"}, i);
			}
		});
	}else {
		$('#btnExl2').hide();
		
		secondGridColumns = secondGrid.columns;
		secondGridColumns.forEach(function(data, i) {
			if(secondGridColumns[i].key == 'localSize'){
				secondGrid.updateColumn({key: "localSize",	label: "[PC]파일Size", 	width: "6%", 	align: "right"}, i);
			}else if(secondGridColumns[i].key == 'localDate') {
				secondGrid.updateColumn({key: "localDate",	label: "[PC]수정한날짜", 	width: "6%", 	align: "center"}, i);
			}
		});
	}
	
	$('#chkSvr').wCheck('check', false);
	$('#chkSvr').wCheck('disabled', true);
	$('#divSvr').css('display', 'none');
	$('#chkBef').wCheck('check', false);
	$('#chkEmg').wCheck('check', false);
	
	if (strReqCD == "" || strReqCD == "SYNC" || strReqCD == "NEW") {
		if (strReqCD != "NEW") {
			$('[data-ax5select="cboJawon"]').ax5select("disable");
			$('[data-ax5select="cboJob"]').ax5select("disable");
		}else {
			$('[data-ax5select="cboJawon"]').ax5select("enable");
			$('[data-ax5select="cboJob"]').ax5select("enable");
		}
	}else {
		if(strReqCD != '01') {
			firstGridColumns = firstGrid.columns;
			firstGridColumns.forEach(function(data, i) {
				if(firstGridColumns[i].key == 'sayu'){
					firstGrid.updateColumn({key: "sayu",		label: "신청사유", 	width: "10%",	align: "left"}, i);
				}
			});
			
			secondGridColumns = secondGrid.columns;
			secondGridColumns.forEach(function(data, i) {
				if(secondGridColumns[i].key == 'sayu'){
					secondGrid.updateColumn({key: "sayu",		label: "신청사유", 	width: "6%", 	align: "left"}, i);
				}
			});
		}
		
		if (strReqCD == "05" || strReqCD == "11" || strReqCD == "12") {
			$('[data-ax5select="cboDeploy"]').ax5select('setValue',cboDeployData[0].cm_micode,true);
			$('[data-ax5select="cboDeploy"]').ax5select("disable");
			$('#chkBef').wCheck('disabled', true);
			$('#chkEmg').wCheck('disabled', true);
			$('#btnPMS').prop('disabled',true);
			$('#btnFile').prop('disabled',true);
		}else if(strReqCD == "01" || strReqCD == "03") {
			$('[data-ax5select="cboDeploy"]').ax5select('setValue',cboDeployData[0].cm_micode,true);
			$('[data-ax5select="cboDeploy"]').ax5select("disable");
			$('#chkBef').wCheck('disabled', true);
			$('#chkEmg').wCheck('disabled', true);
			$('#btnPMS').prop('disabled',false);
			$('#btnFile').prop('disabled',true);
		}else {
			$('[data-ax5select="cboDeploy"]').ax5select("enable");
			$('#chkBef').wCheck('disabled', false);
			$('#chkEmg').wCheck('disabled', false);
			$('#btnPMS').prop('disabled',false);
			$('#btnFile').prop('disabled',false);
			cboDeploy_Change();
			
			if(getSelectedVal('cboSys').cm_sysinfo.substr(1,1) == '1') {
				$('#chkSvr').wCheck('check', false);
				$('#chkSvr').wCheck('disabled', false);
				$('#divSvr').css('display', 'inline-block');
				$("#btnSvr").hide();
			}
		}
	}
	
	if (strReqCD == "" || strReqCD == "SYNC") {
		$('#txtStory').prop('disabled',true);	
	} else {
		$('#txtStory').prop('disabled',false);
	}
	if (strReqCD == "") {
		$('#divTree').css('display', 'inline-block');
		$('#divGrid').css('width', 'calc(100% - 410px)');
		$('#divReq1').css('display', 'block');
		$('#divReq2').css('display', 'none');
		$('#btnCmd').text('실행');
		$('#btnReq').text('실행');
		$('#lblLocalPath').text('');
	} else if (strReqCD=="SYNC"){
		$('#divTree').css('display', 'inline-block');
		$('#divGrid').css('width', 'calc(100% - 410px)');
		$('#divReq1').css('display', 'block');
		$('#divReq2').css('display', 'none');
		$('#btnCmd').text('실행');
		$('#btnReq').text('실행');
		$('#lblLocalPath').text('선택한 폴더/하위디렉토리 파일추출 -> 비교 -> 동기화대상 목록추가 -> 실행(형상관리에 등록되지않은 로컬신규파일은 동기화대상제외)');
	} else if (strReqCD=="NEW"){
		$('#divTree').css('display', 'inline-block');
		$('#divGrid').css('width', 'calc(100% - 410px)');
		$('#divReq1').css('display', 'block');
		$('#divReq2').css('display', 'none');
		$('#btnCmd').text('등록');
		$('#btnReq').text('등록');
		$('#lblLocalPath').text('선택한 폴더 및 입력한 검색단어에 대한 파일추출 -> 신규대상목록추가 -> 등록');
	} else {
		$('#divTree').css('display', 'inline-block');
		$('#divGrid').css('width', 'calc(100% - 410px)');
		$('#divReq1').css('display', 'none');
		$('#divReq2').css('display', 'block');
		$('#btnCmd').text('신청');
		$('#btnReq').text('신청');
		if (strReqCD != "01") {
			$('#divReq1').css('display', 'none');
			$('#divReq2').css('display', 'block');
			$('#divTree').css('display', 'none');
			$('#divGrid').css('width', '100%');
			$('#lblLocalPath').text("["+getSelectedVal('cboReq').cm_codename +"] 신청가능 프로그램 조회  -> 신청대상 목록추가 -> 비교 -> 신청");
			filterPgm();
		} else {
			$('#lblLocalPath').text('선택한 폴더 및 입력한 검색단어로 신청가능 프로그램 조회 -> 신청대상 목록추가 -> 비교 -> 신청');
		}
	} 
}

function successGetSysInfo(data) {
	cboSysData = data;
	cboSysData = cboSysData.filter(function(data) {
		if(data.cm_syscd == '00000' || data.localyn == 'A' || data.localyn == 'L') return true;
		else return false;
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if( (cboSysData != null && cboSysData.length > 0) && getSelectedIndex('cboSys') > 0) {
		cboSys_Change();
	}
}

function cboSys_Change() {
	fristGridData = [];
	firstGrid.setData([]);
	
	secondGridData = [];
	secondGrid.setData([]);
	
	treeData = [];
	ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
	
	$('#txtLocalDir').val('');
	
	cboReqFilter(cboReqData);
	cboDeployFilter(cboDeployData);
	
	$('#chkSvr').wCheck('check', false);
	$('#chkSvr').wCheck('disabled', true);
	$('#divSvr').css('display', 'none');
	
	if(cboSysData == null || cboSysData.length == 0 || getSelectedIndex('cboSys') == 0) return;
	
	if(strReqCD == '04' && getSelectedVal('cboSys').cm_sysinfo.substr(1,1) == '1') {
		$('#chkSvr').wCheck('check', false);
		$('#chkSvr').wCheck('disabled', false);
		$('#divSvr').css('display', 'inline-block');
		$('#btnSvr').css('display', 'none');
	}
	
	//SysInfo.getJobInfo(strUserId,cboSys.selectedItem.cm_syscd,"Y","N","SEL","NAME");
	var tmpInfoData = new Object();
	tmpInfoData = {
		UserID : userId,
		SysCd : getSelectedVal('cboSys').cm_syscd,
		SecuYn : 'Y',
		CloseYn : 'N',
		SelMsg : 'SEL',
		sortCd : 'NAME',
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successGetJobInfo);
	
	//Cmr4100.getHomeDirList(cboSys.selectedItem.cm_syscd, strUserId);
	tmpInfoData = new Object();
	tmpInfoData = {
		userid : userId,
		syscd : getSelectedVal('cboSys').cm_syscd,
		requestType	: 'getHomeDirList'
	}
	ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpInfoData, 'json', successGetHomeDirList);
}

function successGetJobInfo(data) {
	cboJobData = data;
	$('[data-ax5select="cboJob"]').ax5select({
		options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
	});
	
	if(cboJobData.length > 0) {
		//Cmd0100.getRsrcOpen(cboSys.selectedItem.cm_syscd,"SEL");
		var tmpInfoData = {
			SysCd : getSelectedVal('cboSys').cm_syscd,
			SelMsg : 'SEL',
			requestType	: 'getRsrcOpen'
		}
		
		ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successJawon);
	}
}

function successGetHomeDirList(data) {
	svrData = data;
	if(svrData != null && svrData.length > 0) {
		if(svrData[0].cd_devhome == null || svrData[0].cd_devhome == '' || svrData[0].cd_devhome == undefined
			|| svrData[0].cm_volpath == null || svrData[0].cm_volpath == '' || svrData[0].cm_volpath == undefined) {
			dialog.alert('개발홈디렉토리 정보가 없습니다.\n[기본관리>사용자환경설정]에서 시스템별 개발홈디렉토리를 재 설정하시기 바랍니다.');
			return;
		}else if(svrData[0].cd_devhome != svrData[0].cm_volpath) {
			dialog.alert('개발홈디렉토리설정이 변경되었습니다.\n[기본관리>사용자환경설정]에서 시스템별 개발홈디렉토리를 재 설정하시기 바랍니다.');
			return;
		}else if(svrData[0].cm_ipaddress != svrData[0].cm_svrip) {
			dialog.alert('사용자IP정보와 개발IP설정이 일치하지않습니다.\n관리자에게 문의하여 IP정보를 확인하시기 바랍니다.');
			return;
		}
		
		$('#txtLocalDir').val(svrData[0].cm_volpath);
		
		if(strReqCD == "NEW") {
			$("#btnDiff_top").hide();
			$("#btnDiff_bottom").hide();
			$("#btnDiff_bottom2").hide();
			
			if(strReqCD == '' || $('#txtLocalDir').val().length == 0) {
				$('#btnCmd').prop('disabled',true);
				$('#btnReq').prop('disabled',true);
			}else if(secondGrid.getList().length > 0) {
				$('#btnCmd').prop('disabled',false);
				$('#btnReq').prop('disabled',false);
			}else {
				$('#btnCmd').prop('disabled',true);
				$('#btnReq').prop('disabled',true);
			}
			
		}else if(strReqCD == "SYNC") {
			$("#btnDiff_top").show();
			$("#btnDiff_bottom").hide();
			$("#btnDiff_bottom2").hide();
			
			if(strReqCD == '' || $('#txtLocalDir').val().length == 0) {
				$('#btnDiff_top').prop('disabled',true);
			}else {
				$('#btnDiff_top').prop('disabled',false);
			}
			
		}else {
			$("#btnDiff_top").hide();
			$("#btnDiff_bottom").show();
			$("#btnDiff_bottom2").show();
			
			if(strReqCD == '' || $('#txtLocalDir').val().length == 0) {
				$('#btnDiff_bottom').prop('disabled',true);
				$('#btnDiff_bottom2').prop('disabled',true);
			}else {
				$('#btnDiff_bottom').prop('disabled',false);
				$('#btnDiff_bottom2').prop('disabled',false);
			}
		}
		
		if(getSelectedIndex('cboReq') == 0) return;
		if(strReqCD == "03" || strReqCD == "04" || strReqCD == "05" || strReqCD == "11" || strReqCD == "12") {
			filterPgm();
		}else {
			if(getSelectedVal('cboReq').cm_micode == "NEW") {
				//서버경로 1depth씩 조회
				getLocalDirTree();
			}
		}
	}else {
		dialog.alert('[기본관리>사용자환경설정]에서 \n시스템별 개발홈디렉토리를 설정하시기 바랍니다.');
		return;
	}
}

function cboDeploy_Change() {
	$('#divApplyDate').css('display', 'none');
	$("#hourTxt").val("18");
	$("#minTxt").val("00");
	
	if(getSelectedIndex('cboDeploy') > -1) {
		if(getSelectedVal('cboDeploy').cm_micode == '4') { //특정일시
			$('#divApplyDate').css('display', 'inline-block');
			
			$('#txtReqDate').val(getDate('DATE',0));
			datReqDate.bind(defaultPickerInfo('txtReqDate','bottom'));
			
			//정기배포 사용 시스템 일때, 특정일시 를 정기적용시간~23시59분까지 설정 가능하도록 툴팁 알림함
			if(getSelectedVal('cboSys').cm_sysinfo.substr(5,1) == '1') {
				$("timePickerDiv").attr("tooltip", 
						getSelectedVal('cboSys').cm_systime.substr(0,2) + ":" + getSelectedVal('cboSys').cm_systime.substr(2)
						+ "~23:59 입력"); 
			}else {
				$("timePickerDiv").attr("tooltip", "18:00~23:59 입력"); 
			}
		}
	}
}

function successJawon(data) {
	cboJawonData = data;
	cboJawonData = cboJawonData.filter(function(item) {
		if(item.cm_micode == '0000') return true;
		else {
			if(item.cm_info.substr(44,1) == '1') return true;
			else return false;
		}
	});
	
	$('[data-ax5select="cboJawon"]').ax5select({
		options: injectCboDataToArr(cboJawonData, 'cm_micode' , 'cm_codename')
	});
}

function filterPgm() {
	if(getSelectedIndex('cboSys') < 1) return;
	if($('#txtLocalDir').val().length == 0) return;
	if(getSelectedIndex('cboReq') == 0) return;
	
	if(strReqCD == '03' || strReqCD == '04' || strReqCD == '05' || strReqCD == '11'|| strReqCD == '12') {
		//체크인, 체크아웃취소 파일추출
		firstGridData = [];
		firstGrid.setData([]);
		getLocalFile($('#txtLocalDir').val(), "C");
	}else {
		if(orifirstGridData == null || orifirstGridData.length == 0) return;
		
		firstGridData = clone(orifirstGridData);
		firstGridFilter(orifirstGridData);
	}
}

function getLocalDirTree() {
	//Cmr4100.getLocalDirTree(strUserId,txtLocalDir.text,svr_dp.toArray(), "R");
	var tmpInfoData = {
		userId : userId,
		baseDir : $('#txtLocalDir').val(),
		svrInfo : svrData,
		parentNo : 'R',
		requestType	: 'getLocalDirTree_ztree'
	}
	
	console.log('[getLocalDirTree_ztree] ==>', tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpInfoData, 'json', successGetLocalDirTree);
}

function successGetLocalDirTree(data) {
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data.substr(5));
		return;
	}
	
	console.log('successGetLocalDirTree ==>', data);
	treeData = data;
	
	if(treeData !== 'ERR') {
		var obj = treeData;
		
		for(var i in treeData){
			if(obj[i].cm_codename =='' ){
				delete obj[i]
				continue;
			}
			obj[i].isParent = true;
		}
		obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
		obj = JSON.parse(obj);
		treeData = obj;
		
		ztree = $.fn.zTree.init($('#treeDir'), setting, treeData);
	}
}

function getLocalFile(selPath, dirGbn) {
	var tmpInfoData = {
		sysCd : getSelectedVal('cboSys').cm_syscd,
		sysMsg : getSelectedVal('cboSys').cm_sysmsg,
		userId : userId,
		reqgbn : strReqCD,
		findStr : $('#txtRsrcName').val(),
		selpath : selPath,
		svrInfo : svrData,
		dirGbn : dirGbn,
		requestType	: 'getLocalFile'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	console.log('[getLocalFile] ==>', tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpInfoData, 'json', successGetLocalFile);
}

function successGetLocalFile(data) {
	ingSw = false;
	$(".loding-div").remove();
	
	console.log('[successGetLocalFile] ==>', data);
	
	if (data != undefined && data != null) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data);
			return;
		}
	}
		
	firstGridData = data;
	orifirstGridData = data;
	
	if(firstGridData.length > 0) {
		if(firstGridData[0].errmsg != null && firstGridData[0].errmsg != '' && firstGridData[0].errmsg != undefined) {
			dialog.alert(firstGridData[0].errmsg);
			firstGridData.splice(0,1);
			return;
		}
	}
	
	$(firstGridData).each(function(i){
		$(secondGridData).each(function(j){
			if(firstGridData[i].view_dirpath == secondGridData[j].view_dirpath
					&& firstGridData[i].filename == secondGridData[j].filename){
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
	
	firstGridFilter(firstGridData);
	if(firstGridData.length > 9999) {
		dialog.alert('추출된 파일의 수가 10,000건을 초과하여 \n우선조회하였습니다.\n조회된 대상을 '
				+ getSelectedVal('cboReq').cm_codename + ' 하신 후 \n계속 진행하시기 바랍니다.');
	}
	
	chkOK_Click();
}

function firstGridFilter(data) {
	var rsrcStr = $('#txtRsrcName').val().trim().toUpperCase();
	if(rsrcStr == '' || rsrcStr == null) {
		firstGridData = data.filter(function(data) {
			if ($('#chkOk').is(':checked')) {
				if(data.enabled == '0') return false;
				else return true;
			}else {
				return true;
			}
		});
	}else {
		var findSw = false;
		var rsrcArr = null;
		
		if(rsrcStr.indexOf(',') > -1) {
			rsrcArr = rsrcStr.split(',');
			for(var j=0; j<data.length; j++) {
				for(var i=0; i<rsrcArr.length; i++) {
					if(data[j].cr_rsrcname.toUpperCase().indexOf(rsrcArr[i]) > -1) {
						findSw = true;
						break;
					}
				}
			}
		}else {
			for(var j=0; j<data.length; j++) {
				if(data[j].cr_rsrcname.toUpperCase().indexOf(rsrcStr) > -1) {
					findSw = true;
				}
			}
		}
		
		firstGridData = data.filter(function(data) {
			if (findSw) {
				if ($('#chkOk').is(':checked')) {
					if(data.enabled == '0') return false;
					else return true;
				}else {
					return true;
				}
			}
			return false;
		});
		
	}
	
	firstGrid.setData(firstGridData);
}

function chkOK_Click() {
	if(orifirstGridData == null || orifirstGridData.length == 0) return;
	
	firstGridData = clone(orifirstGridData);
	firstGridFilter(firstGridData);
}


//폴더 펼칠때 실행하는 함수
function myOnExpand(event, treeId, treeNode) {
	//root node 만 비동기 방식으로 뽑아오는 조건
	console.log("### myOnExpand  treeNode[1]: ", treeNode);
//	if(treeNode.pId != null || treeNode.children != undefined){
//		return false;
//	};
//	if(treeNode.children != undefined || treeNode.check_Child_State == -1){
//		return false;
//	}
	if(treeNode.children != undefined){
		return false;
	}
	
	console.log('[myOnExpand] strReqCD:'+strReqCD);
	if(getSelectedIndex('cboSys') < 1) return;
	if(strReqCD != 'NEW') return;

	//로딩중 icon class 추가
	$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){
		var ajaxReturnData = null;

		var tmpInfoData = {
			userId : userId,
			baseDir : treeNode.cm_fullpath,
			svrInfo : svrData,
			parentNo : treeNode.cm_seqno,
			requestType	: 'getLocalDirTree_ztree'
		}
		
		console.log('[getLocalDirTree_ztree] ==>', tmpInfoData);
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr4100Servlet', tmpInfoData, 'json', successGetLocalDirTree);
		
		console.log('[getLocalDirTree_ztree] ==>', ajaxReturnData);
		if (ajaxReturnData == undefined || ajaxReturnData == null) return;
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
		} else {
			var obj = ajaxReturnData;
				
			for(var i in ajaxReturnData){
				if(obj[i].cm_dirpath =='' ){
					delete obj[i]
					continue;
				}
				obj[i].name = obj[i].cm_dirpath;
				delete obj[i].cm_dirpath;
				obj[i].isParent = true;
			}
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			ztree.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
};

//트리 클릭
function treeDir_nodeClick(event, treeId, treeNode) {
	if(getSelectedIndex('cboSys') == 0) return;
	if(strReqCD == '03' || strReqCD == '04' || strReqCD == '05' || strReqCD == '11' || strReqCD == '12') return;
	
	if(ingSw) {
		dialog.alert(getSelectedVal('cboReq').cm_codename + ' 목록추출이 이미 진행중입니다. \n잠시만 기다려 주시기 바랍니다.')
		return;
	}
	
	dirGbn = "C";
}

//트리 오른쪽 클릭 이벤트
function OnRightClickTree(event, treeId, treeNode) {
	var treeId = treeNode.id;
	
	if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
		ztree.cancelSelectedNode();
		showRMenu("root", event.clientX, event.clientY);
		selectedNode = treeNode;
	} else if (treeNode && !treeNode.noR) {
		ztree.selectNode(treeNode);
		showRMenu("node", event.clientX, event.clientY);
		selectedNode = treeNode;
	}
}

//오른쪽(Context menu) 메뉴 보여주기
function showRMenu(type, x, y) {
	$("#rMenu ul").show();
	if (type=="root") {
		$("#getPrgList").hide();
		$("#getPrgList2").hide();
	} else {
		if(strReqCD == 'SYNC' || strReqCD == 'NEW' || strReqCD == '01') {
			$("#getPrgList").show();
			$("#getPrgList2").show();
		}
	}
    y += document.body.scrollTop;
    x += document.body.scrollLeft;
    rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

	$("body").bind("mousedown", onBodyMouseDown);
}

//노드 이외 영역 클릭시 context menu 숨기기
function onBodyMouseDown(event){
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
	}
}

//context menu 숨기기 
function hideRMenu() { 
	if ($("#rMenu")) $("#rMenu").css({"visibility": "hidden"}); 
	$("body").unbind("mousedown", onBodyMouseDown); 
}

function getProgrameList() {
	if(ztree.getSelectedNodes().length == 0) return;
	
	if(strReqCD == '03' || strReqCD == '04' || strReqCD == '05' || strReqCD == '11' || strReqCD == '12') return;
	
	if(ingSw) {
		dialog.alert(getSelectedVal('cboReq').cm_codename + ' 목록추출이 이미 진행중입니다. \n잠시만 기다려 주시기 바랍니다.')
		return;
	}
	
	select_LocalFile();
}

function select_LocalFile() {
	ingSw = true;
	
	firstGridData = [];
	firstGrid.setData([]);
	
	if(ztree.getSelectedNodes().length == 0) return;
	if($('#txtLocalDir').val().length == 0) return;
	
	var cm_volpath = ztree.getSelectedNodes()[0].cm_fullpath;
	
	getLocalFile(cm_volpath, dirGbn);
}

//트리 더블클릭
function treeDir_doubleClick(event, treeId, treeNode) {
	var sysCd  		= getSelectedVal('cboSys').value;
	var sysgb  		= getSelectedVal('cboSys').cm_sysgb;
	
	var hasChild 	= treeNode.children != undefined? true : false;
	var root 		= treeNode.root;
	var fileId 		= treeNode.id;
	var fileInfo 	= treeNode.cm_info;
	var dsncd  		= treeNode.cr_dsncd;
	var fileinfo 	= treeNode.cm_info;
	var rsrccd 		= treeNode.cr_rsrccd;
	var subrsrccd	= treeNode.cr_rsrccd
	var fullpath = null;
	
	if(treeNode.cm_volpath == null){
		fullpath = treeNode.cm_fullpath;
	}else{
		fullpath = treeNode.cm_volpath;
	}
	
	if(fileId != '-1' ) {
		if( rsrccd ===  undefined) rsrccd = subrsrccd;
		//if( root === 'true' && !hasChild ) 					getChildFileTree(fileInfo, rsrccd, fileId);
		if( dsncd !== undefined || fullpath !== undefined) 	makeFileDir(fullpath, dsncd, fileinfo, hasChild, sysgb, rsrccd, sysCd);
	}
	
	searchMOD = 'T';
	
}

//추가
function addDataRow() {
	diffSw = false;
	$('#btnCmd').prop('disabled',true);
	$('#btnReq').prop('disabled',true);
	
	var eachCount = 0;
	var secondGridList = new Array;
	var firstGridSeleted = firstGrid.getList("selected");
	
	$(firstGridSeleted).each(function(i){
		eachCount ++;
		
		if (this.selected_flag == '1' 
			|| ( strReqCD == 'SYNC' && ( this.diffRst == null || this.diffRst == '' || this.diffRst == undefined || this.enabled == '0') ) ) {
			return true;
		}

		if(this.selected_flag != "1"){
			this.selected_flag = "1";
			secondGridList.push($.extend({}, this, {__index: undefined}));
		}
	});
	
	if(firstGridSeleted.length > eachCount) {
		// break 감지
		return;
	}
	
	firstGrid.clearSelect();	// 상위 그리드에 있는 데이터가 하단 그리드에 추가되면 상단 그리드에서 선택했던 체크박스 초기화

	if (secondGridList.length != 0){
		for(var i=0; i<secondGridList.length; i++) {
			if($('#txtStory').val() != '') {
				secondGridList[i].cr_sayu = $('#txtStory').val();
			}
		}
		checkDuplication(secondGridList);
	}
}

function checkDuplication(downFileList) {
	var secondGridList = new Array;

	if(secondGridData.length > 0 ){
		$(secondGridData).each(function(i){
			$(downFileList).each(function(j){
//				if( secondGridData[i].cr_itemid == downFileList[j].cr_itemid ){
				if( (secondGridData[i].view_dirpath == downFileList[j].view_dirpath) &&
					 (secondGridData[i].filename == downFileList[j].filename) ) {
					downFileList.splice(j,1);
					return false;
				}
			});
		});
	}

	if(downFileList.length > 0) {
		$(downFileList).each(function(i){
			var currentItem = downFileList[i];

			if(currentItem.baseitemid == currentItem.cr_itemid){
				$(firstGrid.list).each(function(j){
					//if(firstGrid.list[j].cr_itemid == currentItem.cr_itemid) {
					if( (firstGrid.list[j].view_dirpath == currentItem.view_dirpath) &&
						(firstGrid.list[j].filename == currentItem.filename) ) {
						firstGrid.list[j].__disable_selection__ = true;
						firstGrid.list[j].selected_flag = "1";
						return false;
					}

				});
			}
		});
	}

	firstGrid.repaint();
	secondGrid.addRow(downFileList);
	secondGrid.clearSelect();

	if(secondGrid.list.length > 0 ) {

		$('[data-ax5select="cboSys"]').ax5select("disable");
		$('[data-ax5select="cboReq"]').ax5select("disable");
		if(strReqCD == 'NEW' || strReqCD == 'SYNC') {
			$('#btnCmd').prop('disabled',false);
			$('#btnReq').prop('disabled',false);
		}
	}

	secondGridData = clone(secondGridData.concat(downFileList));
	simpleData();
}

//상세보기
function simpleData(){
	if (secondGrid.list.length < 1) return;

	$(secondGridData).each(function(i){
		this.__index = i;
		this.__original_index = i;
	});

	if(secondGrid.list.length == 0){
		secondGridData = clone(secondGrid.list);
		return;
	}

	secondGrid.list = clone(secondGridData);
	secondGrid.repaint();
}

//제거
function deleteDataRow() {

	var secondGridSeleted = secondGrid.getList("selected");

	$(secondGridSeleted).each(function(i){
		$(firstGrid.list).each(function(j){
			if( (firstGrid.list[j].view_dirpath == secondGridSeleted[i].view_dirpath 
					&& firstGrid.list[j].filename == secondGridSeleted[i].filename) ){

				firstGrid.list[j].__disable_selection__ = '';
				firstGrid.list[j].selected_flag = "0";
				return false;
			}
		});
	});

	//하단그리드 original에서 선택한 데이터 제거
	$(secondGridSeleted).each(function(i){
		for (var k=0; k<secondGridData.length; k++) {
			if( secondGridData[k].view_dirpath == secondGridSeleted[i].view_dirpath
					&& (secondGridData[k].filename == secondGridSeleted[i].filename) ){
				secondGridData.splice(k,1);
				k--;
			}
		}
	});

	secondGrid.removeRow("selected");
	firstGrid.repaint();

	if (secondGrid.list.length == 0){
		$('[data-ax5select="cboSys"]').ax5select("enable");
		$('[data-ax5select="cboReq"]').ax5select("enable");
		$('#btnCmd').prop('disabled',true);
		$('#btnReq').prop('disabled',true);
	} else {
		$('[data-ax5select="cboSys"]').ax5select("disable");
		$('[data-ax5select="cboReq"]').ax5select("disable");
		if(strReqCD == 'NEW' || strReqCD == 'SYNC') {
			$('#btnCmd').prop('disabled',false);
			$('#btnReq').prop('disabled',false);
		}
	}
}

function request() {
	if(secondGrid.getList().length == 0) return;
	if(getSelectedIndex('cboReq') == 0) return;
	if(strReqCD == '') return;
	
	confirmDialog.setConfig({
        title: "확인"
    });
	confirmDialog.confirm({
		msg: getSelectedVal('cboReq').cm_codename + ' 진행하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			requestConfirm();
		}else{
			ingSw = false;
		}
	});
}

function requestConfirm() {
	if (ingSw) {
		dialog.alert("현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	
	if (strReqCD == "SYNC" || strReqCD == "NEW"){
		if (strReqCD == "NEW") {
			if(getSelectedIndex('cboJawon') < 1) {
				dialog.alert("프로그램종류를 선택하세요.");
				return;
			}else {
				var tmpExe = '';
				var rsrcExe = '';
				
				for(var i=0; i<secondGridData.length; i++) {
					if ($('#txtExeName').val() != '') {
						if(secondGridData[i].filename.lastIndexOf('.') > 0) {
							tmpExe = secondGridData[i].filename.substr(secondGridData[i].filename.lastIndexOf("."));
							tmpExe = tmpExe + ",";
							tmpExe = tmpExe.toUpperCase();
							rsrcExe = $('#txtExeName').val().toUpperCase();
							
							if (rsrcExe.indexOf(tmpExe) < 0) {
								dialog.alert("확장자가 일치하지 않습니다.["+secondGridData[i].filename+"]");
								return;
							}
						}else {
							dialog.alert("확장자가 맞지않습니다.");
							return;
						}
					}
				}
			}
			
			if(getSelectedIndex('cboJob') < 1) {
				dialog.alert("업무를 선택하세요.");
				return;
			}
			
			if($('#txtStory').val().trim() == '') {
				dialog.alert("프로그램 설명을 입력하세요.");
				return;
			}
		}
		
		var errsw = false;
		var reqArr = new Array();
		for(var i=0; i<secondGridData.length; i++) {
			if(secondGridData[i].enabled == '1') {
				secondGridData[i].story = $('#txtStory').val();
				secondGridData[i].cr_story = $('#txtStory').val();
				reqArr.push(secondGridData[i]);
			}else {
				errsw = true;
			}
		}
		
		//동기화
		if (strReqCD == "SYNC") {
			if (errsw) {
				if (reqArr.length == 0) {
					dialog.alert($('#btnCmd').text()+" 가능한 대상이 없습니다.");
					return;
				} 
			} else {
				if (reqArr.length == 0) {
					dialog.alert($('#btnCmd').text()+"할 대상을 목록에서 선택하시기 바랍니다.");
					return;
				}
			}
			
			//동기화처리로그확인 modal open
			localSyncResultData = new Object();
			localSyncResultData.strUserId = userId;
			localSyncResultData.strSysCd = getSelectedVal('cboSys').cm_syscd;
			localSyncResultData.localDir = $('#txtLocalDir').val();
			
			setTimeout(function() {
				localSyncResultModal.open({
			        width: 850,
			        height: 550,
			        iframe: {
			            method: "get",
			            url: "../modal/request/LocalSyncResultModal.jsp"
				    },
			        onStateChanged: function () {
			            if (this.state === "open") {
			                mask.open();
			            }
			            else if (this.state === "close") {
			            	ingSw = false;
			            	if(localSyncResultFlag) {
			            		select_LocalFile();
			            	}
			            	secondGridData = [];
			            	secondGrid.setData([]);
			            	$('[data-ax5select="cboSys"]').ax5select("enable");
			            	$('[data-ax5select="cboReq"]').ax5select("enable");
			            	$('#btnCmd').prop('disabled',true);
			            	$('#btnReq').prop('disabled',true);
			                mask.close();
			            }
			        }
				});
	  		}, 200);
		}
		//신규등록
		else {
			if (reqArr.length == 0) {
				dialog.alert($('#btnCmd').text()+"할 대상을 목록에서 선택하시기 바랍니다.");
				return;
			}
			
			var etcData = new Object();
			etcData.userid = userId;
			etcData.sysgb = getSelectedVal('cboSys').cm_sysgb;
			etcData.syscd = getSelectedVal('cboSys').cm_syscd;
			etcData.sysos = getSelectedVal('cboSys').cm_sysos;
			etcData.svrcd = "UA";
			etcData.basesvr = getSelectedVal('cboSys').cm_dirbase;
			etcData.story = $('#txtStory').val();
			etcData.rsrccd = getSelectedVal('cboJawon').cm_micode;
			etcData.cm_info = getSelectedVal('cboJawon').cm_info;
			etcData.seqno = svrData[0].cm_seqno;
			etcData.jobcd = getSelectedVal('cboJob').cm_jobcd;
			
			var tmpData = {
				fileList     : reqArr,
				etcData		 : etcData,
				requestType  : 'cmr0020_Insert_thread'
			}
			
			console.log('[cmr0020_Insert_thread] ==>', tmpData);
			ajaxAsync('/webPage/ecmd/svrOpenServlet', tmpData, 'json',successInsert);
		}
	}else {
		if(strReqCD == '03' || strReqCD == '04') {
			if($('#txtDocNo').val().length == 0) {
				dialog.alert("문서번호를 입력하여 주십시오.");
				return;
			}
		}
		
		if($('#txtSayu').val().length == 0) {
			dialog.alert("신청사유를 입력하세요.");
			return;
		}
		
		var errsw = false;
		request_dp = [];
		
		for(var i=0; i<secondGridData.length; i++) {
			
			if(secondGridData[i].enabled == '1') {
				secondGridData[i].story = $('#txtSayu').val();
				secondGridData[i].cr_story = $('#txtSayu').val();
				
				if ( (strReqCD=="01" || strReqCD=="11" || strReqCD=="12") && secondGridData[i].selected == "1") {
					secondGridData[i].chkrstcd = "X";	//체크아웃처리함, download
				} else {
					secondGridData[i].chkrstcd = "L";	//체크아웃처리안함
				}
				
				request_dp.push(secondGridData[i]);
			}else {
				errsw = true;
			}
		}
		
		if(errsw) {
			if (request_dp.length == 0) {
				dialog.alert($('#btnReq').text()+"이 불가능한 상태입니다.");
				return;
			} else {
				confirmDialog.setConfig({
			        title: "확인"
			    });
				confirmDialog.confirm({
					msg: $('#btnReq').text()+"이 불가능한 프로그램이 있습니다. \n제외하고 진행하시겠습니까?",
				}, function(){
					if(this.key === 'ok') {
						secondGridData = clone(request_dp);
						concinueAction();
					}else{
						ingSw = false;
					}
				});
				return;
			}
		}else {
			if(request_dp.length == 0) {
				dialog.alert( $('#btnReq').text()+"할 대상을 목록에서 선택하시기 바랍니다.");
				return;
			}
		}
		secondGridData = clone(request_dp);
		concinueAction();
	}
}

function successInsert(data) {
	ingSw = false;
	
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data.substr(5));
		return;
	}
	
	var tmpArray = data;
	var errMsg = '';
	
	var i=0;
	var j=0;
	for(i=0; secondGridData.length>i; i++) {
		for (j=0; firstGridData.length>j; j++) {
			if (secondGridData[i].cm_dirpath == firstGridData[j].cm_dirpath &&
					secondGridData[i].filename == firstGridData[j].filename) {
			    if (secondGridData[i].enabled == "1") {
		    		firstGridData.splice(j,1);
		    		j--;
		    	}
			}
		}
	}
	
	firstGrid.setData(firstGridData);
	secondGridData = [];
	secondGrid.setData([]);
	
	for (i=0; tmpArray.length>i; i++) {
		if (tmpArray[i].error == "1") {
			if (errMsg == "") {
				errMsg = tmpArray[i].filename+"("+tmpArray[i].errmsg+")";
			} else {
				errMsg = errMsg+"\n"+tmpArray[i].filename+"("+tmpArray[i].errmsg+")";
			}
		}
	}
	
	if (errMsg != "") {
	    dialog.alert("등록 중 오류가 발생했습니다.\n[Error Message]\n"+errMsg);
	} else {
		dialog.alert("등록처리에 성공하였습니다.");
	}
	
	$('[data-ax5select="cboSys"]').ax5select("enable");
	$('[data-ax5select="cboReq"]').ax5select("enable");
}

function concinueAction() {
	if(strReqCD == '03' || strReqCD == '04') {
		if($('#chkSvr').is(':checked') && svrList.length == 0) {
			dialog.alert('예외적용（적용서버선택)을 선택하셨습니다.\n적용서버를 선택한 후 신청하시기 바랍니다.');
			return;
		}
		
		var today	= getDate('DATE',0);
		var strNow	= '';
		var strTime = '';
		var strDate = '';
		var strNum  = '0123456789';
		
		strAplyDate = ''; //초기화
		
		if (getSelectedVal('cboDeploy').cm_micode == '4') {
			strTime = $("#hourTxt").val() + $("#minTxt").val();
			strDate = replaceAllString($('#txtReqDate').val(),'/','');
			
			if ($('#hourTxt').val() == '' || $('#minTxt').val() == '' || strTime == '' || strDate == '') {
				dialog.alert("특정일시를 입력하여 주시기 바랍니다.");
				return;
			}
			
			if (strTime.length != 4) {
				dialog.alert("4자리 숫자로 입력하여 주시기 바랍니다.");
				return;
			}
			
			for (i=0;strTime.length>i;i++) {
				if (strNum.indexOf(strTime.substr(i,1))<0) {
					dialog.alert("4자리 숫자로 입력하여 주시기 바랍니다.");
					return;
				}
			}
			
			//특정일시 설정 할때 정기배포 사용 여부 체크 후
			//정기적용 시간 ~ 23시59분 사이에 셋팅 가능 하도록...
			if(getSelectedVal('cboSys').cm_sysinfo.substr(5,1) == '1') {
				if(getSelectedVal('cboSys').cm_systime == null || getSelectedVal('cboSys').cm_systime == '' || getSelectedVal('cboSys').cm_systime == undefined || getSelectedVal('cboSys').cm_systime.substr(0,3) == 'ERR') {
					dialog.alert(getSelectedVal('cboSys').cm_systime);
					return;
				}else {
					if(Number(strTime) <= Number(getSelectedVal('cboSys').cm_systime) && Number(strTime) < 2360) {
						dialog.alert("특정일시 시간은 "+getSelectedVal('cboSys').cm_systime.substr(0,2)+":"+getSelectedVal('cboSys').cm_systime.substr(2)+" ~ 23:59 까지 입력 가능합니다.");
						return;
					}
				}
			}
			
			if (today > strDate) {
				dialog.alert("특정일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
				return;
			}else if (today == strDate) {				
				strNow = getTime();
				if (strTime < strNow) {
					dialog.alert("특정일시가 현재일 이전입니다. 정확히 선택하여 주십시오.");
					return;
				}
			}
			
			strAplyDate = strDate + strTime;
		}
		
		
		for (var x=0;x<secondGridData.length;x++) {
			if (secondGridData[x].prcseq == "") {
				dialog.alert("우선순위를 입력한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
				return;
			}
		}
		
		request_analEnd();
	}else {
		request_analEnd();
	}
}

function request_analEnd() {
	var strRsrcCd = "";
	ingSw = true;
	
	for (var x=0 ; x<secondGridData.length ; x++) {

		if (strRsrcCd.length > 0) {
			if (strRsrcCd.indexOf(secondGridData[x].cr_rsrccd) < 0) {
				strRsrcCd = strRsrcCd + ",";
				strRsrcCd = strRsrcCd + secondGridData[x].cr_rsrccd;
			}
		} else strRsrcCd = secondGridData[x].cr_rsrccd;
	}
	
	var tmpData = {
			  SysCd : getSelectedVal('cboSys').cm_syscd,
			  ReqCd : strReqCD,
		     RsrcCd : strRsrcCd,
			 UserId : userId,
			  QryCd : strReqCD,
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
	var strQry = "";
	var emgSw = "0";
	var tmpRsrc = "";
	var tmpJobCd = "";
	var tmpPrjNo = "";
	
	strQry = "";
	for (var x=0;x<secondGridData.length;x++) {
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
	
	if (strReqCD=="04") {
		if ($('#chkEmg').is(':checked')) {
			emgSw = '2';
		}else {
			emgSw = '0';
		}
	}else {
		emgSw = '0';
	}
	
	confirmInfoData = new Object();
	confirmInfoData.userid = userId;
	confirmInfoData.reqcd = strReqCD;
	confirmInfoData.syscd = getSelectedVal('cboSys').cm_syscd;
	confirmInfoData.rsrccd = tmpRsrc;
	confirmInfoData.qrycd = strQry;
	confirmInfoData.deploycd = 'Y';
	confirmInfoData.emgsw = emgSw;
	confirmInfoData.confyn = 'N';
	confirmInfoData.PrjNo = '';
	
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
		            		cmdReq_prjend();
		            	} else ingSw = false;
		                mask.close();
		            }
		        }
			});
		}, 200);	
	} else if (GbnCd == "N") {
		var etcObj = new Object();
		etcObj.userid = userId;
		etcObj.reqcd = strReqCD;
		etcObj.syscd = getSelectedVal('cboSys').cm_syscd;
		etcObj.rsrccd = tmpRsrc;
		etcObj.qrycd = strQry;
		etcObj.deploycd = 'Y';
		etcObj.emgsw = emgSw;	
		etcObj.confyn = "N";
		etcObj.PrjNo = '';
		
		var tmpData = {
			etcObj : etcObj,
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
				cmdReq_prjend();
			}
		}
	}
}

function cmdReq_prjend() {
	ingSw = true;
	
	var requestData = new Object();
	var ajaxReturnData = null;
	
	if(strReqCD == "03" || strReqCD == "04" || strReqCD == "05") {
		requestData.UserID 	= userId;
		requestData.ReqCD  	= strReqCD;
		requestData.Sayu   	= $('#txtSayu').val().trim();
		requestData.sysinfo = getSelectedVal('cboSys').cm_sysinfo;
		requestData.Deploy 	= getSelectedVal('cboDeploy').cm_micode;
		if (getSelectedVal('cboDeploy').cm_micode == '4') {
			requestData.AplyDate = strAplyDate;
		}else {
			requestData.AplyDate = '';
		}
		requestData.TstSw 	= getSelectedVal('cboSys').TstSw;
		
		//AC재기동 여부 체크
		requestData.acReStart = "Y";
		
		requestData.emrgb  = "0";
		requestData.emrcd  = "";
		requestData.emrmsg = "";
		requestData.PassCd = "";

		if($("#chkEmg").is(":checked")) {
			requestData.EmgCd = "2";
		}else {
			requestData.EmgCd = "0";
		}
		
		requestData.reqday 	= "";
		requestData.reqdept = "";
		requestData.reqdoc 	= "";
		requestData.reqtit 	= "";
		requestData.txtSayu = $('#txtSayu').val().trim();
		requestData.prjno 	= $('#txtDocNo').val().trim();
		requestData.reqetc 	= "";
		
		if(!$("#chkSvr").is(":checked")) {
			svrList = [];
		}
		
		var tmpData = {
			chkInList	: secondGridData,
			etcData 	: requestData,
			befJob		: befJobData,	
			ConfList 	: confirmData,
			confFg 		: 'Y',
			svrList 	: svrList,
			AnalList 	: checkoutData,
			requestType : 'request_Check_In_New'
		}
		console.log('[request_Check_In_New] ==>', tmpData);
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
		
	}else if(strReqCD == "01") {
		requestData.UserID 	 = userId;
		requestData.ReqCD  	 = strReqCD;
		requestData.Sayu   	 = $('#txtSayu').val().trim();
		requestData.cm_syscd = getSelectedVal('cboSys').cm_syscd;
		requestData.cm_sysgb = getSelectedVal('cboSys').cm_sysgb;
		requestData.EmgSw	 = "0";
		requestData.prjno	 = $('#txtDocNo').val().trim();
		
		var tmpData = {
			chkOutList	: secondGridData,
			etcData 	: requestData,
			ConfList 	: confirmData,
			requestType : 'request_Check_Out'
		}
		console.log('[request_Check_Out] ==>', tmpData);
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json');
	}else {
		//strReqCD=11, 12 
		requestData.UserID  = userId;
		requestData.ReqCD   = strReqCD;
		requestData.Sayu    = $('#txtSayu').val().trim();
		requestData.sayu    = $('#txtSayu').val().trim();
		requestData.TstSw 	= getSelectedVal('cboSys').TstSw;
		requestData.syscd 	= getSelectedVal('cboSys').cm_syscd;
		requestData.EmgSw	= "0";
		requestData.isrid  	= "";
		requestData.isrsub  = "";
		
		var tmpData = {
			chkOutList	: secondGridData,
			etcData 	: requestData,
			ConfList 	: confirmData,
			requestType : 'request_Check_Out_Cancel'
		}
		console.log('[request_Check_Out_Cancel] ==>', tmpData);
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0101Servlet', tmpData, 'json');
	}
	
	console.log('ajaxReturnData=',ajaxReturnData);
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}
	if(typeof(ajaxReturnData) == 'string' && (ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR')){
		dialog.alert('에러가 발생하였습니다. 다시 신청해주세요.');
		ingSw = false;
		return;
	}
	acptNo = ajaxReturnData;
	strAcptNo = ajaxReturnData;
	
	if(strReqCD == '04') {
		upFiles = [];
		upFiles = clone(docFiles);
		if (upFiles.length>0) {
			addFile();
		} else {
			fileSenderClose_sub();
		}
	}else {
		fileSenderClose_sub();
	}
}

function addFile() {
	if (upFiles.length > 0){
		uploadCk = true; 	// 파일 업로드 체크 변수
		gubunName = "";		//upFiles로 업로드하게 gubunName 값 초기화
		
		fileUploadModal2.open({
		    width: 685,
		    height: 420,
		    iframe: {
		        method: "get",
		        url: "../modal/fileupload/FileUpload2.jsp"
			},
		    onStateChanged: function () {
		        if (this.state === "open") {
		            mask.open();
		        } else if (this.state === "close") {
		            mask.close();
	                fileSenderClose_sub();
		        }
		    }
		});
		return;
	} else{
		fileSenderClose_sub();
	}
}

function fileSenderClose_sub() {
	firstGrid.setData([]);
	firstGridData = [];
	secondGrid.setData([]);
	secondGridData = [];
	
	$('#txtSayu').val();
	$('#txtDocNo').val();
	$('#txtStory').val();
	
	ingSw = false;
	
	if(strReqCD == '11') {
		dialog.alert('신청완료');
	}else {
		confirmDialog.confirm({
			msg: $('#btnReq').text()+' 신청완료! 상세 정보를 확인하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				openWindow('DETAIL',acptNo);
			}
		}); 
	}
	
	filterPgm();
	$('[data-ax5select="cboSys"]').ax5select("enable");
	$('[data-ax5select="cboReq"]').ax5select("enable");
	
	if(strReqCD != 'NEW' && strReqCD != 'SYNC') {
		$('#btnDiff_bottom2').prop('disabled',false);
	}
	$('#btnCmd').prop('disabled',true);
	$('#btnReq').prop('disabled',true);
	$('#chkOk').wCheck('check',false);
}
	
function openWindow(type,param) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+param) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+param;

	var f = document.popPam;   	//폼 name
    f.user.value = userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 'DETAIL') {    
        f.acptno.value = param;
        if(param.substr(4,2) == '01' || param.substr(4,2) == '02' || param.substr(4,2) == '11') {
        	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
        }else {
        	cURL = "/webPage/winpop/PopRequestDetail.jsp";
        }
	} else {
		dialog.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

function successReqDiff_top(data) {
	$(".loding-div").remove();
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data.substr(5));
		return;
	}
	
	firstGridData = clone(data);
	orifirstGridData = clone(data);
	
	if(data.length > 0) {
		if(data[0].errmsg != null && data[0].errmsg != '') {
			dialog.alert(data[0].errmsg);
			data.splice(0,1);
			
			$('#btnCmd').prop('disabled',true);
			$('#btnReq').prop('disabled',true);
		}
	}
	firstGridFilter(orifirstGridData);
}

function successReqDiff(data) {
	$(".loding-div").remove();
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data.substr(5));
		return;
	}
	
	$('#btnCmd').prop('disabled',false);
	$('#btnReq').prop('disabled',false);
	
	secondGridData = clone(data);
	if(data.length > 0) {
		if(data[0].errmsg != null && data[0].errmsg != '') {
			dialog.alert(data[0].errmsg);
			data.splice(0,1);
			
			$('#btnCmd').prop('disabled',true);
			$('#btnReq').prop('disabled',true);
		}
	}
	secondGrid.setData(secondGridData);
}

function btnSvr_Click() {
	selSvrModalData = [];
	selSvrModalData.syscd = getSelectedVal('cboSys').cm_syscd;
	selSvrModalData.sysmsg = getSelectedVal('cboSys').sysmsg;
	selSvrModalData.ReqCd = strReqCD;
	
	setTimeout(function() {
		selSvrModal.open({
			width: 915,
			height: 580,
			iframe: {
				method: "get",
				url: "../modal/request/selSvrModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {  
					if(befJobData.length == 0){
						befCheck = true;
						$('#chkBef').wCheck('check',false);
					}
					else{
						befCheck = true;
						$('#chkBef').wCheck('check',true);
					}
					mask.close();
				}
			}
		});
	}, 200);
}

function btnExl2_Click() {
	var headerDef = [];
	var excelData = new Object();
	var excelDataArray = new Array();
	var path = '';
	
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	var ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) {
		dialog.alert('Temp경로 추출 실패');
		return;
	}
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		return;
	}
	path = ajaxReturnData;
	
	headerDef.push('sysmsg');
	headerDef.push('jobname');
	headerDef.push('userid');
	headerDef.push('rsrcname');
	headerDef.push('story');
	headerDef.push('dirpath');
	headerDef.push('rsrccd');
	
	excelData = new Object();
	for(var i=0; i<firstGridData.length; i++) {
		excelData = new Object();
		excelData.sysmsg = getSelectedVal('cboSys').cm_sysmsg;
		if(getSelectedIndex('cboJob') == 0) {
			excelData.jobname = '';
		}else {
			excelData.jobname = getSelectedVal('cboJob').cm_jobname;
		}
		excelData.userid = userId;
		excelData.rsrcname = firstGridData[i].filename;
		excelData.story = $('#txtStory').val();
		excelData.dirpath = firstGridData[i].basePath + firstGridData[i].view_dirpath;
		if(getSelectedIndex('cboJawon') == 0) {
			excelData.rsrccd = '';
		}else {
			excelData.rsrccd = getSelectedVal('cboJawon').cm_codename;
		}
		excelDataArray.push(excelData);
	}
	
	excelDataArray.unshift({
		"sysmsg":"sysmsg","jobname":"jobname","userid":"userid","rsrcname":"rsrcname","story":"story","dirpath":"dirpath","rsrccd":"rsrccd",
	});
	
	data = {
		headerDef	: headerDef,
		filePath	: path + '/LocalFileList_regist.xls',
		excelData	: excelDataArray,
		requestType	: 'setExcel'
	}
	ajaxReturnData = null;
	ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) return;
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		return;
	}
	successExcelExport(ajaxReturnData, 'LocalFileList_regist');
}
