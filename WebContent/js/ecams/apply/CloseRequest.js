/**
 * 체크인,테스트적용,운영적용 신청화면
 * 
 * 	작성자: 정선희
 * 	버전 : 1.0
 *  수정일 : 2019-05-27
 * 
 */
var userId = window.top.userId;
var reqCd = window.top.reqCd;

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var datReqDate 		= new ax5.ui.picker();
var befJobModal 		= new ax5.ui.modal();
var approvalModal 		= new ax5.ui.modal();

var request         =  new Request();

var cboOptions = [];

var ajaxReturnData = "";
var srSw            = false;
var sysData  = null; //시스템콤보
var cboReqGbnData   = null;
var srData	= null;	//SR-ID 콤보
var firstGridData = []; //신청대상그리드
var secondGridData = [];
var gridSimpleData = [];

var firstGridColumns = null;
var secondGridColumns = null;
var qrySw = false;
var ingSw = false;
var closeSw = false;
var confirmData = [];
var rsrccdData = null;
var swEmg = false;
var strAplyDate = '';
var befJobData = [];
var befCheck = false; // 체크박스 변경시 이벤트가 바로 걸려 체크하기위한 변수
var confirmInfoData = null;
var acptNo = "";
var winDevRep        = null; //SR정보 새창
var SelSysCd = null;

var codeList = window.top.codeList;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 	// 그리드 sort 가능 여부(true/false)
    multiSort: true,	// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.select(this.dindex);
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
    		else {
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
            {type: 1, label: "추가"}
        ],
        popupFilter: function (item, param) {
         	//firstGrid.clearSelect();
         	//firstGrid.select(Number(param.dindex));
        	if(firstGrid.getList('selected').length < 1){
        		return false;
        	}
         	return true;
       	 
        },
        onClick: function (item, param) {
	        addDataRow();
	        firstGrid.contextMenu.close();//또는 return true;
        	
        }
    },
    columns: [
        {key: "view_dirpath", label: "프로그램경로",  width: '15%', align: 'left'},
        {key: "cr_rsrcname", label: "프로그램명",  width: '15%', align: 'left'},
        {key: "jobname", label: "업무명",  width: '10%', align: 'left'},
        {key: "jawon", label: "프로그램종류",  width: '10%',align: 'left'},
        {key: "cr_story", label: "프로그램설명",  width: '15%', align: 'left'},
        {key: "codename", label: "상태",  width: '10%'},
        {key: "cr_viewver", label: "현재버전",  width: '8%'},
        {key: "cm_username", label: "수정자",  width: '7%'},
        {key: "cr_lastdate", label: "수정일",  width: '10%'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 	// 그리드 sort 가능 여부(true/false)
    multiSort: true,	// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	deleteDataRow();
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    		simpleData();
    	    this.self.repaint();
    	},
    	trStyleClass: function () {
    		if (this.item.cr_itemid != this.item.baseitem){
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
            {type: 1, label: "제거"}
        ],
        popupFilter: function (item, param) {
        	//secondGrid.clearSelect();
        	//secondGrid.select(Number(param.dindex));
        	if(secondGrid.getList('selected').length < 1){
        		return false;
        	}
         	return true;
       	 
        },
        onClick: function (item, param) {
	        	
	        deleteDataRow();
	        secondGrid.contextMenu.close();//또는 return true;
        	
        }
    },
    columns: [
    	{key: "view_dirpath", label: "프로그램경로",  width: '20%', align: 'left'},
        {key: "cr_rsrcname", label: "프로그램명",  width: '20%', align: 'left'},
        {key: "jobname", label: "업무명",  width: '15%'},
        {key: "jawon", label: "프로그램종류",  width: '15%'},
        {key: "editRow", label: "컴파일순서",  width: '10%', editor: {type: "text"}},
        {key: "cr_story", label: "프로그램설명",  width: '20%', align: 'left'}
    ]
});

$('[data-ax5select="cboSrId"]').ax5select({
    options: []
});

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});


$('[data-ax5select="cboRsrccd"]').ax5select({
    options: []
});

$('[data-ax5select="cboReqGbn"]').ax5select({
	options: []
});

$(document).ready(function(){
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

	$("#panCal").css('display', 'inline-block');
	
	$('#chkDetail').bind('click',function(){
		simpleData();
	});
	
	$('#btnAdd').bind('click',function(){
		addDataRow();
	});
	
	$('#btnDel').bind('click',function(){
		deleteDataRow();
	});
	
	// 처리구분
	$('#cboReqGbn').bind('change',function(){
		cboReqGbnClick();
	});
		
	$('#cboSrId').bind('change',function(){
		changeSrId();
	});
	
	$('#cboSys').bind('change',function(){
		changeSys();
	});
	
	$('#btnFind').bind('click',function(){
		findProc();
	});

	$('#btnRequest').bind('click',function(){
		btnRequestClick();
	});
	
	$('#txtRsrcName').bind('keypress',function(event){
		if(event.keyCode == 13 && $('#txtRsrcName').val().trim().length > 0){
			findProc();
		}
	});
	
	$("#btnSR").bind('click',function(){
		cmdReqInfo_Click();
	});

	//프로그램 유형
	$('#cboRsrccd').bind('change',function(){
		if(getSelectedIndex('cboRsrccd') > -1 || $('#txtRsrcName').val().trim().length > 0){
			findProc();
		}
	});
	
	//체크인
	if (reqCd == '05'){ //개발폐기
		$('#btnRequest').text('개발폐기신청');
		$('#chkBefJob').parent('div.wCheck').hide();
		$('#chkBefJob').parent('div.wCheck').siblings('label[for="chkBefJob"]').hide();
		$("#btnFileUpload").show();
	}
	else if (reqCd == '09'){ //테스트폐기
		$('#btnRequest').text('테스트폐기신청');
		$('#chkBefJob').parent('div.wCheck').hide();
		$('#chkBefJob').parent('div.wCheck').siblings('label[for="chkBefJob"]').hide();
		$("#btnFileUpload").show();
	}
	else{ //운영폐기
		$('#btnRequest').text('운영폐기신청');
		$('#chkBefJob').show();
		$("#btnFileUpload").show();
	}
	
	$('#chkBefJob').bind('change',function(){
		if($("#chkBefJob").parent(".wCheck").css('display') == "none"){
			return;
		}
		
		if(befCheck){
			befCheck = false;
			return;
		}
		
		if($('#chkBefJob').is(':checked')){
			openBefJobSetModal();
		}
		else{
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
                    },
                    cancel: {
                        label:'cancel'
                    },
                    other: {
                        label:'선행작업확인'
                    }
				}
			}, function(){
				console.log(this.key);
				if(this.key === 'ok') {
					befJobData = [];
				}
				else if (this.key ==='other'){
					openBefJobSetModal();
				}
				else{
					befCheck = true;
					$('#chkBefJob').wCheck('check',true);
				}
                mask.close();
			});
		}
	});
	
	dateInit();
	getCodeInfoList();
	getSrIdCbo();
	
});

function openBefJobSetModal(){
	setTimeout(function() {
		befJobModal.open({
			width: 915,
			height: 580,
			iframe: {
				method: "get",
				url: "../modal/request/BefJobSetModal.jsp",
				param: "callBack=modalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					console.log("befjobclose");
					if(befJobData.length == 0){
						befCheck = true;
						$('#chkBefJob').wCheck('check',false);
					}
					else{
						befCheck = true;
						$('#chkBefJob').wCheck('check',true);
					}
					mask.close();
				}
			}
		});
	}, 200);
}

function dateInit() {
	$('#txtReqDate').val(getDate('DATE',0));
	datReqDate.bind(defaultPickerInfo('txtReqDate'));
	/*
	$('#txtReqTime').timepicker({
	    showMeridian : false,
	    minuteStep: 1
	 });
	 */
}

//처리구분
function getCodeInfoList() {
	
	cboReqGbnData 	= fixCodeList(codeList['REQPASS'], '', 'cm_codename', 'ASC', 'N');
	cboReqData 	= fixCodeList(codeList['CHECKIN'], '', 'cm_codename', 'ASC', 'N');
	codeList = null;
	
	/*var codeInfos = getCodeInfoCommon([ new CodeInfo('REQPASS','','N')]);
	cboReqGbnData = codeInfos.REQPASS;
	cboReqData 	  = codeInfos.CHECKIN;*/
	
	cboOptions = [];
	$.each(cboReqGbnData,function(key,value) {
			cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	$('[data-ax5select="cboReqGbn"]').ax5select({
		options: cboOptions
	});
	if(reqCd == '05' || reqCd == '09'){ // 개발폐기,테스트폐기
		$('[data-ax5select="cboReqGbn"]').ax5select('disable');
	}
	cboReqGbnClick();
}

//시스템 리스트
function getSysCbo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userId,
		SecuYn : "Y",
		SelMsg : "SEL",
		CloseYn : "N",
		ReqCd : ""
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysCbo);
}
//시스템 리스트
function successGetSysCbo(data) {
	sysData = data;
	var findSw = false;
	
	cboOptions = [];
	$.each(sysData,function(key,value) {
		findSw = false;
		if (reqCd == '09' && (value.cm_systype == 'C' || value.cm_systype == 'D' || value.cm_systype == 'F' || value.cm_systype == 'G') ) findSw = true;
		if (reqCd == '10' && (value.cm_systype == 'D' || value.cm_systype == 'E' || value.cm_systype == 'G' || value.cm_systype == 'H')) fidSw = true;
		if (reqCd == '05') findSw = true;
		if (findSw) cboOptions.push({value: value.cm_syscd, text: value.cm_sysmsg, cm_sysgb: value.cm_sysgb, cm_sysinfo: value.cm_sysinfo, tstsw: value.TstSw, cm_systype: value.cm_systype});
	});
	$('[data-ax5select="cboSys"]').ax5select({
        options: cboOptions
	});
	
	changeSrId();
	
	if(reqCd == '10'){  //운영폐기
		cboReqGbnClick();
	}
}

//SR-ID 선택
function changeSrId() {
	
	$('#txtSayu').val('');
	$('#btnSR').prop('disabled',true);
	firstGrid.setData([]);
	fristGridData = [];
	
	var bSyscd = '';
	
	if (getSelectedIndex('cboSrId') > 0) {
		$('#btnSR').prop('disabled',false);
		$('#txtSayu').val(getSelectedVal('cboSrId').text);
		
		if (reqCd == '10'){ //운영폐기
			if(cboReqGbnData.length>0){
				var selectVal = $('select[name=cboReqGbn] option:eq(0)').val();
				$('[data-ax5select="cboReqGbn"]').ax5select('setValue',selectVal,true);
				cboReqGbnClick();
			}
		}
	}
	
	if(sysData.length > 0){
		var sysLength = sysDataFilter();

		var sysSelectIndex = 0;
		if(sysLength == 1) sysSelectIndex = 0;
		else sysSelectIndex = 1;

		var selectVal = $('select[name=cboSys] option:eq('+sysSelectIndex+')').val();
		$('[data-ax5select="cboSys"]').ax5select('setValue',selectVal,true);
		
	}
	
	changeSys();
}
//프로그램유정보
function getRsrcInfo(syscd) {
	rsrccdData = null;
	ajaxReturnData = null;
	if (syscd === '00000') {
		$('[data-ax5select="cboRsrccd"]').ax5select({
	      options: []
		});
		$('[data-ax5select="cboRsrccd"]').ax5select("disable");
		return;
	}
	else {
		$('[data-ax5select="cboRsrccd"]').ax5select("enable");
	}

	var tmpData = new Object();
	tmpData.sysCd = syscd;
	tmpData.selMsg = 'ALL';
	
	var sysListInfoData;
	sysListInfoData = new Object();
	sysListInfoData = {
		sysData	    : 	tmpData,
		requestType	: 	'getRsrcInfo'
	}
	
	ajaxReturnData = ajaxCallWithJson('/webPage/common/SysInfoServlet', sysListInfoData, 'json');
	
	successGetRsrcInfoList(ajaxReturnData);
}
//프로그램종류리스트
function successGetRsrcInfoList(data) {
	rsrccdData = data;
	cboOptions = [];
	$.each(data,function(key,value) {
		if (value.moduleyn == 'N' || value.cm_micode == '0000') {
			cboOptions.push({value: value.cm_micode, text: value.cm_codename});
		}
	});
	$('[data-ax5select="cboRsrccd"]').ax5select({
      options: cboOptions
	});
}

//SR정보
function getSrIdCbo() {
	var prjInfo;
	var prjInfoData;
	prjInfo 		= new Object();
	prjInfo.userid 	= userId;
	prjInfo.reqcd 	= reqCd;
	prjInfo.secuyn 	= 'Y';
	prjInfo.qrygbn 	= '01';
	
	prjInfoData = new Object();
	prjInfoData = {
		prjInfoData	: 	prjInfo,
		requestType	: 	'getPrjlist'
	}
	ajaxAsync('/webPage/common/ComPrjInfo', prjInfoData, 'json', successGetPrjInfoList);
}
//SR-ID정보
function successGetPrjInfoList(data) {
	cboOptions = [];
	cboOptions.push({value:'SR정보 선택 또는 해당없음',text:'SR정보 선택 또는 해당없음'});
//	
//	if (data != undefined && data != null) {
//		if (data.indexOf("ERROR")>-1) {
//			dialog.alert(data);
//		} else {
//			console.log(data);
//			srData = data;
//			$.each(srData,function(key,value) {
//				cboOptions.push({value: value.cc_srid, text: value.srid, cc_title:value.cc_title, syscd:value.syscd, cc_chgtype: value.cc_chgtype});
//			});
//		}
//	}
//	
//	$('[data-ax5select="cboSrId"]').ax5select({
//		options: cboOptions
//	});
//	
//	if (getSelectedIndex('cboSrId')>0) {
//		var selectVal = $('select[name=cboSrId] option:eq(1)').val();
//		$('[data-ax5select="cboSrId"]').ax5select('setValue',selectVal,true);
//	}
	
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
		} else {
			srData = data;
			$.each(srData,function(key,value) {
				if(value.setyn === 'Y') selectedSrId = value.cc_srid;
				cboOptions.push({value: value.cc_srid, text: value.srid, syscd:value.syscd, cc_title :value.cc_title});
			});
		}
	}
	$('[data-ax5select="cboSrId"]').ax5select({
        options: cboOptions
	});

	getSysCbo();
}

//처리구분선택
function cboReqGbnClick() {
	$("#hourTxt").val('18');
	$("#minTxt").val('30');
	if(getSelectedIndex('cboReqGbn') > -1){
		swEmg = false;
		if(getSelectedVal('cboReqGbn').value == '02'){// 긴급적용
			swEmg = true;
		}
		else	if (getSelectedVal('cboReqGbn').value == '4') {
			$("#panCal").css('display', 'inline-block');
			return;
		}
		$("#panCal").hide();
	}
}


//신청대상목록 조회
function findProc() {
	firstGrid.setData([]);
	firstGridData = [];

	if(qrySw) {
		dialog.alert('검색 또는 신청 진행중 입니다.'); 
		return; //qrySw=true 일때는 검색 또는 신청 진행중일때임
	}
	if (srSw && getSelectedIndex('cboSrId') < 1) return;
	
	
	if (getSelectedIndex('cboSys') < 1) {
		dialog.alert('시스템을 선택하십시오.');
		return;
	}
	
	qrySw = true;
	
	var tmpObj = new Object();
	tmpObj.UserId = userId;
	tmpObj.SysCd = SelSysCd;
	tmpObj.SinCd = reqCd;
	tmpObj.RsrcName = $('#txtRsrcName').val().trim();
	tmpObj.DsnCd = "";
	tmpObj.DirPath = "";
	tmpObj.SysInfo = getSelectedVal('cboSys').cm_sysinfo;
	tmpObj.RsrcCd = "";
	tmpObj.ReqCD = reqCd;
	if(rsrccdData.length > 0){
		tmpObj.RsrcCd = getSelectedVal('cboRsrccd').value;
	}
	
	if (srSw && getSelectedIndex('cboSrId')>0) {		
		tmpObj.srid = getSelectedVal('cboSrId').value;//SR사용여부 체크
	}


	var paramData = new Object();
	paramData = {
		baseInfoData: 	tmpObj,
		requestType	: 	'getCloseList'
	}
	
	ajaxAsync('/webPage/apply/CloseRequest', paramData, 'json', successGetProgramList);

}
//신청목록조회
function successGetProgramList(data) {
	if (data == undefined || data == null) return;
	
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		qrySw = false;
		return;
	}
	firstGridData = data;
	if(firstGridData.length == 0 ){
		//dialog.alert('검색 결과가 없습니다.');
		qrySw = false;
		return;
	}
	else if (firstGridData.length > 0){
		if(firstGridData[0].ID =='MAX'){
			dialog.alert('검색결과가 너무 많으니 검색조건을 입력하여 검색하여 주시기 바랍니다.');
			qrySw = false;
			return;
		}
	}
	
	$(firstGridData).each(function(i){
		$(secondGridData).each(function(j){
			if(firstGridData[i].cr_itemid == secondGridData[j].cr_itemid){
				firstGridData[i].selected_flag = "1";
				return true;
			}
		})
		
		if(firstGridData[i].selected_flag == "1"){
			firstGridData[i].__disable_selection__ = true;
		}
	});
	firstGrid.setData(firstGridData);
	
	if(firstGridData.length > 0 && reqCd == '10'){
		firstGrid.selectAll({selected:true, filter:function(){
			return this['selected_flag'] != '1';
		}});
		//addDataRow();
	}
	qrySw = false;
}

//항목상세보기
function simpleData() {
	if (secondGrid.list.length < 1) return;
	gridSimpleData = clone(secondGrid.list);
	if(secondGrid.list.length == 0){
		secondGridData = clone(secondGrid.list);
		return;
	}
	
	if (!$('#chkDetail').is(':checked')){
		for(var i =0; i < gridSimpleData.length; i++){
			if(gridSimpleData[i].baseitem != gridSimpleData[i].cr_itemid || 
				gridSimpleData[i].cr_itemid == null || gridSimpleData[i].cr_itemid == ''){
				gridSimpleData.splice(i,1);
				i--;
			}
			if(gridSimpleData[i] != null && gridSimpleData[i] != undefined){
				gridSimpleData[i].__index = i;
			}
		};
		secondGrid.list = clone(gridSimpleData);
		secondGrid.repaint();
	}
	else{
		for(var i =0; i < secondGridData.length; i++){
			secondGridData[i].__index = i;
		};
		
		secondGrid.list = clone(secondGridData);
		secondGrid.repaint();
	}
}
//신청목록추가
function addDataRow() {

	var j = 0;
	var secondGridList = new Array;
	var firstGridSeleted = firstGrid.getList("selected");
	ajaxReturnData = null;
	var strRsrcName = '';
	var calSw = false;
	
	$(firstGridSeleted).each(function(i){
		if(this.selected_flag == '1' && reqCd != '10'){
			return true;
		}
		if(this.selected_flag != '1') {
			this.selected_flag = '1';
			var copyData = this;
			secondGridList.push($.extend({}, copyData, {__index: undefined}));
			if (!calSw) {
				if(this.cm_info.substr(3,1) =='1'){ //4 동시모듈
					calSw = true;
				} else if (this.cm_info.substr(8,1) == '1'){ //4 실행모듈
					calSw = true;
				}
			}
		}
	});

	if (secondGridList.length == 0) {
		dialog.alert('배포 할 파일을 선택한 후 진행하시기 바랍니다.');
		return;
	}
	
	firstGrid.clearSelect();	// 상위 그리드에 있는 데이터가 하단 그리드에 추가되면 상단 그리드에서 선택했던 체크박스 초기화
	
	if(reqCd == '10' && getSelectedIndex('cboSrId') > 0 && firstGridData.length != secondGridList.length){
		dialog.alert('운영폐기 할 준비가 완료되지 않은 건이 있습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	
	if ((secondGrid.list.length + secondGridList.length) > 300){
		dialog.alert("300건까지 신청 가능합니다.");
		return;
	}
	
	if(secondGridList.length == 0 ){
		return;
	}
	
	if (calSw) cmdReqSubAnal(secondGridList);
	else checkDuplication(secondGridList);
	
}

//신청목록에서 제거
function deleteDataRow() {

	outpos = "";
	var secondGridSeleted = secondGrid.getList("selected");
	var originalData = null;
	
	if(reqCd == '10' && secondGridSeleted.length > 0){ // 운영신청일때는 한방에 전부 제거가 되어야 함.
		secondGrid.selectAll({selected: true});
		secondGridSeleted = secondGrid.getList("selected");
	}
	$(secondGridSeleted).each(function(i){
		originalData = null;
		
		if( this.cm_info.substr(3,1) == '1' || this.cm_info.substr(8,1) == '1'){
			if($('#chkDetail').is(':checked')){
				for(var x=0; x<secondGrid.list.length; x++){
					if(secondGrid.list[x].baseitem == this.cr_itemid){
						secondGrid.select(x,{selected:true} );
					}
				}
			}
		}
		else if (this.cr_itemid != this.baseitem){
			for(var x=0; x<secondGrid.list.length; x++){
				if(secondGrid.list[x].cr_itemid == this.baseitem){
					secondGrid.select(x,{selected:true} );
					originalData = secondGrid.list[x].baseitem;
				}
			}
		}
		$(firstGridData).each(function(j){
			if((firstGridData[j].cr_itemid == secondGridSeleted[i].baseitem) || 
				firstGridData[j].cr_itemid == originalData && originalData != null){

				firstGridData[j].__disable_selection__ = false;
				firstGridData[j].selected_flag = "0";
				return false;
			}
		});
		
	});
	// 동시적용항목 secondGridData에서 빼주는 작업
	$(secondGrid.getList("selected")).each(function(i){
		for(var j =0; j < secondGridData.length; j++){
			if(this.baseitem == secondGridData[j].baseitem){
				secondGridData.splice(j,1);
				j--
			}
		}
	});

	secondGrid.removeRow("selected");
	firstGrid.repaint();
	
	if (secondGrid.list.length == 0){

		$('[data-ax5select="cboSys"]').ax5select("enable");
		if(srSw) $('[data-ax5select="cboSrId"]').ax5select('enable');
		
		$('[data-ax5select="cboReqGbn"]').ax5select("enable");
		
		$('#btnRequest').prop('disabled',true);
	}
	
	

}

function checkDuplication(downFileList){
	

	var secondGridList = new Array;
	var i = 0;
	var j = 0;
	var findSw = false;
	var totCnt = secondGridData.length;
	
	//console.log(data);
	for(i=0; downFileList.length>i ; i++){
		findSw = false;
		totCnt = secondGridData.length;
		for (j=0;totCnt>j;j++) {
			if (downFileList[i].cr_itemid == secondGridData[j].cr_itemid &&
				downFileList[i].baseitem == secondGridData[j].baseitem) {
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
	
	if(downFileList.length > 0) {
		$(downFileList).each(function(i){
			var currentItem = downFileList[i];
			$(firstGridData).each(function(j){
				if(firstGridData[j].cr_itemid == currentItem.cr_itemid) {
					firstGridData[j].selected_flag = '1';
					firstGridData[j].__disable_selection__ = true;
					return false;
				}
				
			});
		});
	}

	firstGrid.repaint();
	secondGrid.addRow(secondGridList);
	secondGrid.repaint();	
	
	if(secondGrid.list.length > 0){
		$('#btnRequest').prop('disabled', false);
		$('[data-ax5select="cboSys"]').ax5select('disable');
		if(srSw) $('[data-ax5select="cboSrId"]').ax5select('disable');
		simpleData();
	}
	
}

function cmdChkClick(){
	var tmpSayu = $('#txtSayu').val().trim();
	
	if(ingSw){
		dialog.alert('현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.');
		return;
	}
	
	if (getSelectedIndex('cboSys') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
	
	if (srSw && getSelectedIndex('cboSrId') < 1) {
		dialog.alert('SR-ID를 선택하여 주십시오.');
		return;
	}
	
	if (srSw && tmpSayu.length == 0) {
		dialog.alert('신청사유를 입력하여 주십시오.');
		return;
	}
	
	if (secondGrid.list.length == 0) {
		dialog.alert('신청 할 파일을 추가하여 주십시오.');
		return;
	}
	
	$(secondGridData).each(function(i){
		if(this.cr_itemid != this.baseitem){
			secondGridData.splice(i,1);
		}
	});
	
	if(secondGridOverlap()){
		return;
	};
	
	ingSw = true;
	cmdReqSubAnal(secondGridData);
	
}

//secondGridData 중복체크
function secondGridOverlap(){
	var retrunData = false;
	$(secondGridData).each(function(i){
		var data = this;
		for(var j = i+1; j<secondGridData.length; j++){
			if(data.cr_itemid != null && data.cr_itemid != ''){
				if(data.cr_itemid == secondGridData[j].cr_itemid && data.baseitem == secondGridData[j].baseitem){
					dialog.alert('동일한 프로그램이 중복으로 신청되었습니다. 조정한 후 다시 신청하십시오. ['+secondGridData[j].cr_rsrcname+']');
					retrunData = false;
					return true;
				}
			}
			else {
				if(data.cr_syscd == secondGridData[j].cr_syscd &&
				   data.cr_dsncd == secondGridData[j].cr_dsncd &&
				   data.cr_rsrcname == secondGridData[j].cr_rsrcname) {
					
					dialog.alert('동일한 프로그램이 중복으로 신청되었습니다. 조정한 후 다시 신청하십시오. ['+secondGridData[j].cr_rsrcname+']');
					retrunData = false;
					return true;
					
				}
			}
		}
	});
	return retrunData;
}

function cmdReqSubAnal(data){
	if(data.length > 0){
		ajaxReturnData = null;
		var downFileData = new Object();
		downFileData.sayu = $('#txtSayu').val().trim();
		downFileData.ReqCD = reqCd;
		downFileData.SinCd = reqCd;
		downFileData.TstSw = getSelectedVal('cboSys').TstSw;
		downFileData.syscd = SelSysCd;
		downFileData.sysgb = getSelectedVal('cboSys').cm_sysgb;
		downFileData.userid = userId;
		downFileData.closeyn = 'N'; 
		downFileData.veryn = 'Y';
		//if (chkVer.selected) downFileData.veryn = "N"; 개발배포만 적용
		var tmpData = {
			baseInfoData : downFileData,
			fileList     : 	   data,
			requestType  : 'getDownFileList'
		}
		ajaxAsync('/webPage/apply/CloseRequest', tmpData, 'json',successDownFileData);
		return;
	}
	//cmdReqSubSame(data);
}

function successDownFileData(data){
	if (data == undefined || data == null) {
		ingsw = false;
		return;
	}
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		ingsw = false;
	} else {
		var analSw = false;
		
		if(data.length != 0 && data == 'ERR'){
			dialog.alert($('#btnRequest').val+'목록 작성에 실패하였습니다.');
			return;
		}
		else{
			
			$(data).each(function(){
				if(this.cr_itemid == 'ERROR'){
					dialog.alert('파일목록 에러 \n파일경로 : '+thiscm_dirpath);
					ingsw = false;
					return;
				}
			});
			
		}
		
		checkDuplication(data);
	}
}

function sysDataFilter(){
	var sysDataLength = sysData.length;
	options = [];
	for(var i=0; i<sysDataLength ; i++){
		var data = sysData[i];
		if(data.cm_sysinfo.substr(0,1) == '1'){
			continue;
		}
		else if (data.cm_syscd =='00000'){
			options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo : data.cm_sysinfo, cm_systype: data.cm_systype});
		}
		else if(data.cm_sysinfo.substr(9,1) == '1'){
			if(getSelectedIndex('cboSrId') > 0){
				continue;
			} else {
				options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo : data.cm_sysinfo, cm_systype: data.cm_systype});
			}
		}
		else{
			if(getSelectedIndex('cboSrId') > 0){
				if (reqCd == '05') {
					options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo : data.cm_sysinfo, cm_systype: data.cm_systype});
				} else {
					var syscd = getSelectedVal('cboSrId').syscd;
					if (syscd == undefined || null == syscd) continue;
					var arySyscd = syscd.split(",");
					for(var j=0; j<arySyscd.length; j++){
						if(arySyscd[j] == data.cm_syscd){
							options.push({value: data.cm_syscd, text: data.cm_sysmsg, cm_sysgb: data.cm_sysgb, cm_sysinfo : data.cm_sysinfo, cm_systype: data.cm_systype});
							break;
						}
					}
				}
				continue;
			}
			else{
				continue;
			}
		}
		
	}

	$('[data-ax5select="cboSys"]').ax5select({
        options: options
	});
	return options.length;
}

function changeSys(){

	if(getSelectedIndex('cboSys') > 0){
		SelSysCd = getSelectedVal('cboSys').value;
		$('[data-ax5select="cboRsrccd"]').ax5select({
	        options: []
		});
		getRsrcInfo(SelSysCd);
	}
	else{
		SelSysCd = null;
		$('[data-ax5select="cboRsrccd"]').ax5select({
	        options: []
		});
	}
	
	firstGrid.setData([]);
	firstGridData = [];
	
	if (getSelectedVal('cboSys').cm_stopsw == "1") {
		dialog.alert("이관통제를 위하여 일시적으로 형상관리 사용을 중지합니다.");
		$('#btnSearch').prop('disabled',true);
		return;
	} 
	else $('#btnSearch').prop('disabled',false);
	
	if (getSelectedVal('cboSys').cm_sysinfo.substr(9,1) == "1") srSw = false;
	else srSw = true;
	
	if (srSw) {
		//$('[data-ax5select="cboSrId"]').ax5select("enable");
		if(srData.lenght == 2){
			var selectVal = $('select[name=cboSrId] option:eq(1)').val();
			$('[data-ax5select="cboSrId"]').ax5select('setValue',selectVal,true);
		}
	} 
	else { 
		var selectVal = $('select[name=cboSrId] option:eq(0)').val();
		$('[data-ax5select="cboSrId"]').ax5select('setValue',selectVal,true);
		//$('[data-ax5select="cboSrId"]').ax5select("disable");
	}
	

	if(getSelectedIndex('cboSrId') < 1){
		$('#cboReqGbn').prop('disabled',false);
		return;
	}
	
	if(getSelectedIndex('cboSys') > 0 && rsrccdData != null) findProc();
	
}

function editRowBlank() {
	var iCnt = gridSimpleData.length; //항목상세보기를 제외한 모듈단위의 목록
	for(var i=0; i<iCnt; i++) {
		if(gridSimpleData[i].prcseq == "") { //컴파일순서에 값이 없으면
			var jCnt = firstGridData.length; //전체프로그램 목록
			for(var j=0; j<jCnt; j++) {
				if(gridSimpleData[i].cr_itemid == firstGridData[j].cr_itemid) { //프로그램경로와 프로그램명이 같으면
					gridSimpleData[i].prcseq = firstGridData[j].prcseq; //상단 그리드에 값을 가져옴
					break;
				}
			}
		}
	}			
}

function cmdReqSub(){
	var strRsrcCd = "";
	var strQry = reqCd;
	ingSw = true;
	var x=0;
	for (x=0 ; x<secondGridData.length ; x++) {
		if (strRsrcCd.length > 0) {
			if (strRsrcCd.indexOf(secondGridData[x].cr_rsrccd) < 0) {
				strRsrcCd = strRsrcCd + ",";
				strRsrcCd = strRsrcCd + secondGridData[x].cr_rsrccd;
			}
		} else strRsrcCd = secondGridData[x].cr_rsrccd;
	}
	
	var confirmInfoData = new Object();
	confirmInfoData.SysCd = SelSysCd;
	confirmInfoData.strRsrcCd = strRsrcCd;
	confirmInfoData.ReqCd = reqCd;
	confirmInfoData.UserID = userId;
	confirmInfoData.strQry = strQry;
	
	var tmpData = {
			requestType : 'confSelect',
			confirmInfoData : confirmInfoData
	}	
	ajaxReturnData = ajaxCallWithJson('/webPage/request/ConfirmServlet', tmpData, 'json');

	if (ajaxReturnData == undefined || ajaxReturnData == null) {
		ingSw = false;
		return;
	}
	if (ajaxReturnData.indexOf('ERROR')>-1) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}	
	
	
	if (ajaxReturnData == "C") {
	    confirmDialog.setConfig({
	        title: "확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				confCall("Y");
			}
			else{
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
	var strQry = reqCd;
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
	
		if (tmpJobCd.length > 0) {
			if (tmpJobCd.indexOf(secondGridData[x].cr_jobcd) < 0)
	            tmpJobCd = tmpJobCd + "," + secondGridData[x].cr_jobcd;
		} else tmpJobCd = secondGridData[x].cr_jobcd;
	}
	
	if (swEmg) {
		emgSw = "2"
	} else {
		emgSw = "0";
	}
	if (getSelectedIndex('cboSrId')>0) tmpPrjNo = getSelectedVal('cboSrId').value;
	confirmInfoData = new Object();
	confirmInfoData.UserID = userId;
	confirmInfoData.ReqCd  = reqCd;
	confirmInfoData.SysCd  = SelSysCd;
	confirmInfoData.Rsrccd = tmpRsrc;
	confirmInfoData.QryCd = strQry;
	confirmInfoData.EmgSw = emgSw;
	confirmInfoData.JobCd = tmpJobCd;
	confirmInfoData.PrjNo = tmpPrjNo;
	
	if (GbnCd == "Y") {
		approvalModal.open({
	        width: 850,
	        height: 600,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ApprovalModal.jsp",
	            param: "callBack=modalCallBack"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	if(confirmData.length > 0){
	            		reqQuestConf();
	            	}
	            	ingSw = false;
	                mask.close();
	            }
	        }
		});
	} else if (GbnCd == "N") {

		var tmpData = {
			confirmInfoData: 	confirmInfoData,
			requestType: 	'Confirm_Info'
		}
		
		ajaxReturnData = ajaxCallWithJson('/webPage/request/ConfirmServlet', tmpData, 'json');

		if (ajaxReturnData == undefined || ajaxReturnData == null) return;
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
		} else {
			confirmData = ajaxReturnData;
			for(var i=0; i<confirmData.length ; i++){
				if (confirmData[i].arysv[0].SvUser == null || confirmData[i].arysv[0].SvUser == "") {
					confirmData.splice(i,1);
					i--;
				}
			}
			reqQuestConf();
		}
	}
}

function reqQuestConf(){

	ajaxReturnData = null;
	var ajaxReturnStr = null;
	var requestData = new Object();
	var deploy = '0';
	var emgCd = '0';
	var aplyData = '';
	
	deploy = getSelectedVal('cboReqGbn').value;
	if(swEmg) emgCd = '2';
	if(getSelectedVal('cboReqGbn').value =='4') {
		aplyData = strAplyDate;
	}
	requestData.closeyn = 'Y';
		
	requestData.UserID = userId;
	requestData.ReqCD  = reqCd;
	requestData.Sayu	  = $('#txtSayu').val().trim();
	requestData.Deploy = deploy;
	requestData.EmgCd = emgCd;
	requestData.AplyDate = aplyData;
	requestData.TstSw = getSelectedVal('cboSys').TstSw;
	requestData.emrgb = "0";
	requestData.emrcd = "";
	requestData.emrmsg = "";
	requestData.reqday = "";
	requestData.reqdept = "";
	requestData.reqdoc = "";
	requestData.reqtit = "";
	requestData.ReqSayu = "9";
	requestData.txtSayu = $('#txtSayu').val().trim();
	requestData.reqetc = "";
	if (getSelectedIndex('cboSrId')>0){
		requestData.srid = getSelectedVal('cboSrId').value;
		requestData.cc_title = getSelectedVal('cboSrId').cc_title;
	} else {
		requestData.srid = "";
		requestData.cc_title = "";
	}

	var tmpData = {
		baseInfoData: 	requestData,
		fileList : 		secondGridData,
		requestConfirmList:	confirmData,
		befJobList 	: 	befJobData,
		requestType: 	'requestConf'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/apply/CloseRequest', tmpData, 'json');

	if (ajaxReturnData == undefined || ajaxReturnData == null) {
		ingSw = false;
		return;
	}
	if (ajaxReturnData.indexOf('ERROR')>-1) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}	
	
	if(ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR'){
		console.log(ajaxReturnData);
		dialog.alert('에러가 발생하였습니다. 다시 신청해주세요.');
		ingSw = false;
		return;
	}
	acptNo = ajaxReturnData;
		
	ingSw = false;
    confirmDialog.setConfig({
        title: "확인",
        theme: "info"
    });
	confirmDialog.confirm({
		msg: $('#btnRequest').val()+' 신청완료! 상세 정보를 확인하시겠습니까?',
	}, function(){
		upFiles = [];

		if(this.key === 'ok') {
			cmdDetail();
		}
		else{
			getSysCbo();
		}
	});
	
	secondGrid.setData([]);
	secondGridData = [];
	$('#btnRequest').prop('disabled',true);
	$('[data-ax5select="cboSys"]').ax5select("enable");
	if(srSw) $('[data-ax5select="cboSrId"]').ax5select('enable');
}

function cmdDetail(){

	var winName = "checkInEnd";
	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= acptNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	nHeight = 740;
    nWidth  = 1200;

	cURL = "/webPage/winpop/PopRequestDetail.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);

	getSysCbo();
}

function btnRequestClick(){
	var tmpSayu = $('#txtSayu').val().trim();
	
	if (ingSw) {
		dialog.alert("현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	
	if (getSelectedIndex('cboSys') < 1) {
		dialog.alert('시스템을 선택하여 주시기 바랍니다.');
		return;
	}
	
	if(getSelectedIndex('cboSys') == null ){
		dialog.alert('시스템정보가 없습니다. 확인 후 다시 신청해 주세요.');
		return;		
	}
	
	if (srSw && tmpSayu.length == 0) {
		dialog.alert('신청사유를 입력하여 주십시오.');
		return;
	}
	
	if (secondGrid.list.length == 0) {
		dialog.alert('신청 할 파일을 추가하여 주십시오.');
		return;
	}
	mask.open();
    confirmDialog.setConfig({
        title: "확인",
        theme: "info"
    });
	confirmDialog.confirm({
		msg : $('#btnRequest').text()+"을 하시겠습니까?",
	}, function(){
        mask.close();
		if(this.key === 'ok') {
				baepoConfirm();
		}
	});
}


function baepoConfirm(){
	var strNow = "";
	var strDate = getDate('DATE',0);
	var strDate2 = "";
	var strTime = "";
	
	timeSw = true;

	strAplyDate = "";

	if ( getSelectedVal('cboReqGbn').value == "4" ) {//4:특정일시배포  0:일반  2:긴급
		strTime = $("#hourTxt").val() + ":"+ $("#minTxt").val();
		if ($('#hourTxt').val() == '' || $('#minTxt').val() == '' || strTime == "") {
			dialog.alert("적용일시를 입력하여 주시기 바랍니다.");
			return;
		}
		strTime = strTime.replace(":","");
		if (strTime.length != 4) {
			dialog.alert("4자리 숫자로 입력하여 주시기 바랍니다.");
			return;
		}
		
		var strDate2 = replaceAllString($('#txtReqDate').val(),'/','');
		
		
		if (strDate > strDate2) {
			dialog.alert("적용일시가 현재일 이전입니다. 정확히 선택하여 주십시오");
			return;
		} else if (strDate == strDate2) {
			
			strNow = getTime();
			if (strTime < strNow) {
				dialog.alert("적용일시가 현재일 이전입니다. 정확히 선택하여 주십시오");
				return;
			}
		}
		strAplyDate = strDate2 + strTime + "00";
		now = null;
	}

	if(secondGridOverlap()){
		return;
	};
		
	if ( ingSw ) {
		dialog.alert("현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	cmdReqSub();
	//RequestScript();
	
}
/*
 * SR 정보 
 */
function cmdReqInfo_Click(){
	if (getSelectedIndex('cboSrId') < 1) {
		dialog.alert('SR정보를 확인 할 SR-ID를 선택하십시오.',function(){});
		return;
	}
	
	//ExternalInterface.call("winopen",userId,"SRINFO",cboIsrId.selectedItem.cc_srid);
	var nHeight, nWidth;
	if(winDevRep != null
			&& !winDevRep.closed) {
		winDevRep.close();
	}	
	
	var form = document.popPam;   						  //폼 name
	form.user.value = userId; 	 						  //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.srid.value = getSelectedVal('cboSrId').value;    //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)	
	form.acptno.value = '';

	nHeight = 510;
	nWidth = 1100;
    
    winDevRep = winOpen(form, 'devRep', '/webPage/winpop/PopSRInfo.jsp', nHeight, nWidth);
}


