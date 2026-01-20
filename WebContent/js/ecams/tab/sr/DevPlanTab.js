/**
 * 개발계획/실적등록 탭 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2019-06-26
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 

//public
var strStatus		= window.parent.strStatus;
var strIsrId		= window.parent.strIsrId;  

var txtExpStdatePicker	= new ax5.ui.picker(); //예상개발시작일
var txtExpEnddatePicker = new ax5.ui.picker(); //예상개발종료일
var txtDevDatePicker	= new ax5.ui.picker(); //예상개발종료일
var grdWorker 			= new ax5.ui.grid();   //담당자그리드
var grdWorkTime 		= new ax5.ui.grid();   //작업시간내역그리드

var selOptions 				= [];
var selectedIndex			= -1;	//select 선택 index
var gridSelectedIndex		= -1;   //그리드 선택 index
var selectedGridItem		= [];	//그리드 선택 item

var workDay					= "";   //월근무일수
var cboRateData 			= [];	//기능접수등급 데이터
var grdWorkerData 			= []; //담당자그리드 데이터
var grdWorkTimeData			= []; //작업시간내역그리드 데이터
var grdWorkTimeData_filter  = []; //작업시간내역그리드 필터링 데이터
var srData 					= {}; 

var createGrid = false;
var completeReadyFunc = false;

$('#txtExpStdate').val(getDate('DATE',0));
$('#txtExpEnddate').val(getDate('DATE',0));
$('#txtDevDate').val(getDate('DATE',0));

txtExpEnddatePicker.bind(defaultPickerInfo('txtExpEnddate', 'bottom'));
txtExpStdatePicker.bind(defaultPickerInfo('txtExpStdate', 'bottom'));
txtDevDatePicker.bind(defaultPickerInfo('txtDevDate', 'top'));

$('input.radio-pie').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

createGrid = false;
createViewGrid();

$(document).ready(function(){
	
	strReqCd = "42";
//	getWorkDays();
//	getDevrRate();
	
	disableCal(true, 'txtDevDate');
	
	//예상개발시작일, 예살개방종료일, 작업일
	$('#txtExpStdate').val(getDate('DATE',0));
	$('#txtExpEnddate').val(getDate('DATE',0));
	$('#txtDevDate').val(getDate('DATE',0));
	txtExpEnddatePicker.bind(defaultPickerInfo('txtExpEnddate', 'bottom'));
	txtExpStdatePicker.bind(defaultPickerInfo('txtExpStdate', 'bottom'));
	txtDevDatePicker.bind(defaultPickerInfo('txtDevDate', 'top'));
	
	//개발계획 클릭
	$('#rdoPlan').bind('click',function() {
		rdoPlan_click();
	});
	
	//개발식절 클릭
	$('#rdoResult').bind('click',function() {
		rdoPlan_click();
	});
	
	//개발계획 등록
	$('#btnRegPlan').bind('click',function() {
		btnRegPlan_Click();
	});
	
	//개발실적 등록
	$('#btnRegResult').bind('click',function() {
		btnRegResult_Click();
	});
	
	//작업시간내역그리드 전체보기 체크박스 이벤트
	$("#chkAll").change(function(){
		worktimeFiltering();
    });

	//작업시간내역 그리드 height 조정
	//grdWorkTime.setHeight($('#btnRegPlan').offset().top - $('[data-ax5grid="grdWorkTime"]').offset().top + $('#btnRegPlan').outerHeight());
	getData();
	completeReadyFunc = true;
});

function createViewGrid() {
//	if(!createGrid) {
		grdWorker.setConfig({
		    target: $('[data-ax5grid="grdWorker"]'),
		    sortable: true, 
		    multiSort: true,
		    showRowSelector: false,
		    multipleSelect: false,
		    header: {
		        align: "center"
		    },
		    body: {
		        onClick: function () {
		        	this.self.clearSelect();
		            this.self.select(this.dindex);
		            grdWorker_Click();
		        },
		    	onDataChanged: function(){
		    		this.self.repaint();
		    	}
		    },
		    columns: [
		        {key: "cm_username", label: "담당자",			width: '15%', 	align: "center"},
		        {key: "cm_codename", label: "진행상태",	  	width: '30%',	align: "center"},
		        {key: "devtime", 	 label: "예상소요시간",   	width: '15%',	align: "center"},
		        {key: "devstday", 	 label: "예상개발시작일",  	width: '20%',	align: "center"},
		        {key: "devedday", 	 label: "예상개발종료일",  	width: '20%',	align: "center"}
		    ]
		});
	
		grdWorkTime.setConfig({
		    target: $('[data-ax5grid="grdWorkTime"]'),
		    sortable: true, 
		    multiSort: true,
		    showRowSelector: false,
		    header: {
		        align: "center"
		    },
		    body: {
		        columnHeight: 20,
		        onClick: function () {
		        	this.self.clearSelect();
		            this.self.select(this.dindex);
		        },
		        onDBLClick: function () {
		        	grdWorkTime_dbClick();
		        },
		    	onDataChanged: function(){
		    		this.self.repaint();
		    	}
		    },
		    columns: [
		        {key: "cc_workday",  label: "등록일",		width: '33%', 	align: "center"},
		        {key: "cc_worktime", label: "시간",	  	width: '33%', 	align: "center"},
		        {key: "cm_username", label: "담당자명",   	width: '33%', 	align: "center"}
		    ]
		});
		
		getWorkDays();
		getDevrRate();
//	}
	
	createGrid = true;

	//작업시간내역 그리드 height 조정
	//grdWorkTime.setHeight($('#btnRegPlan').offset().top - $('[data-ax5grid="grdWorkTime"]').offset().top + $('#btnRegPlan').outerHeight());
}

function getData() {
	if (window.parent.srData != null && window.parent.srData != '' && window.parent.srData != undefined) {
		srData = window.parent.srData;
		strIsrId = window.parent.srData.strIsrId;
		strStatus = window.parent.srData.strStatus;
	}
}

//연도별월근무일수조회 Cmc0200.getWorkDays(new Date().getFullYear());
function getWorkDays() {
	var tmpData;
	tmpData = new Object();
	tmpData = {
		requestType	: 'getWorkDays',
		strYear		: new Date().getFullYear() 
	}
	ajaxAsync('/webPage/ecmc/Cmc0200Servlet', tmpData, 'json', successWorkdays);
}

function successWorkdays(data) {
	workDay = data;
	if(workDay == "No") {
		dialog.alert('월근무일수가 등록되지 않았습니다.',function(){});
		return;
	}
}
//기능접수등급 조회 Cmc0200.getRatecd();
function getDevrRate() {
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('DEVRATE','SEL','N')
	]);
	
	cboRateData = codeInfos.DEVRATE;
	
	$('[data-ax5select="cboRate"]').ax5select({
        options: injectCboDataToArr(cboRateData, 'cm_micode' , 'cm_codename')
   	});
	
	initDevPlan();
}

function screenInit(gbn) {
	if(gbn == 'M') {
		grdWorker.setData([]);
		grdWorkTime.setData([]);
		$('#rdoPlan').wRadio("check", true);
		$('#rdoResult').wRadio("check", false);
		$('[data-ax5select="cboRate"]').ax5select('setValue','00',true);
		rdoPlan_click();
	}
	
	$('#txtWriteDay').val(''); 	//개발계획작성일
	$('#txtWriter').val(''); 	//개발계획작성인
	$('#txtExpTime').val(''); 	//예상소요시간
	$('#txtDevTime').val(''); 	//작업시간
	
	$('#btnRegPlan').prop("disabled", true);
	$('#btnRegResult').prop("disabled", true);

	//개발계획 비활성화
	$('#txtExpTime').prop("disabled", true);
	disableCal(true, 'txtExpStdate');
	$('#txtExpStdate').prop("disabled", true);
	disableCal(true, 'txtExpEnddate');
	$('#txtExpEnddate').prop("disabled", true);
	$('[data-ax5select="cboRate"]').ax5select("disable");
	
	//개발실적 비활성화
	$('#txtDevTime').prop("disabled", true);
	disableCal(true, 'txtDevDate');
	$('#txtDevDate').prop("disabled", true);
	
	$('#chkAll').prop("checked", false);
	//$('[data-ax5select="cboRate"]').ax5select("enable");
}

function rdoPlan_click() {
	$('#btnRegPlan').prop("disabled", true);
	$('#btnRegResult').prop("disabled", true);
	
	//개발계획 비활성화
	$('#txtExpTime').prop("disabled", true);
	disableCal(true, 'txtExpStdate');
	$('#txtExpStdate').prop("disabled", true);
	disableCal(true, 'txtExpEnddate');
	$('#txtExpEnddate').prop("disabled", true);
	$('[data-ax5select="cboRate"]').ax5select("disable");
	
	//개발실적 비활성화
	$('#txtDevTime').prop("disabled", true);
	disableCal(true, 'txtDevDate');
	$('#txtDevDate').prop("disabled", true);
	
	gridSelectedIndex = grdWorker.selectedDataIndexs;
	selectedGridItem = grdWorker.list[grdWorker.selectedDataIndexs];
	
	if(grdWorkerData == null || gridSelectedIndex < 0 || selectedGridItem == null) {
		return;
	}
	
	//strStatus: SR상태
	if(selectedGridItem.cc_userid == userId) {
		if($('#rdoPlan').is(':checked') && (strStatus == '1' || strStatus == '2' || strStatus == '4' || strStatus == '5')) {
			$('#btnRegPlan').prop("disabled", false);
		}else if($('#rdoResult').is(':checked') && (strStatus == '2' || strStatus == '4' || strStatus == '5') && (selectedGridItem.devmm != null && selectedGridItem.devmm != "")) {
			$('#btnRegResult').prop("disabled", false);
			//grdWork.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, grdWork_DUCLICK);
		}
	}//else {
	//	return;
	//}
	
	if($('#rdoPlan').is(':checked') && $('#btnRegPlan').prop("disabled", false)) {
		$('#txtExpTime').prop("disabled", false);
		
		disableCal(false, 'txtExpStdate');
		$('#txtExpStdate').prop("disabled", false);
		
		disableCal(false, 'txtExpEnddate');
		$('#txtExpEnddate').prop("disabled", false);
		
		$('#txtDevTime').prop("disabled", true);
		
		disableCal(true, 'txtDevDate');
		$('#txtDevDate').prop("disabled", true);
		
		$('[data-ax5select="cboRate"]').ax5select("enable");
	}else if($('#rdoResult').is(':checked') && $('#btnRegResult').prop("disabled", false)) {
		$('#txtExpTime').prop("disabled", true);
		
		disableCal(true, 'txtExpStdate');
		$('#txtExpStdate').prop("disabled", true);
		
		disableCal(true, 'txtExpEnddate');
		$('#txtExpEnddate').prop("disabled", true);
		
		$('#txtDevTime').prop("disabled", false);
		
		disableCal(false, 'txtDevDate');
		$('#txtDevDate').prop("disabled", false);
		
		$('[data-ax5select="cboRate"]').ax5select("disable");
	}
}

//devPlanCall
function initDevPlan() {
	getData();
	//SR클릭되면 개발자 조회 Cmc0200.getSelectList(strIsrId,"",strReqCd);
	getWorker();
	
	//작업시간내역 조회 Cmc0200.get_Worktime(strIsrId);
	getWorktime();
}

function getWorker() {
	var SRInfo = new Object();
	SRInfo.srId = strIsrId;
	SRInfo.userId = null;
	SRInfo.reqCd = strReqCd;
	
	var SRInfoData;
	SRInfoData = new Object();
	SRInfoData = {
			SrId	:strIsrId,
			UserId	: "",
			ReqCd	: strReqCd,
		requestType	: 'getSelectList'
	}
	
	$('[data-ax5grid="grdWorker"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmc/Cmc0200Servlet', SRInfoData, 'json', successWorker);
}

function successWorker(data) {
	$(".loding-div").remove();
	grdWorkerData = data;
	
	grdWorkerData = grdWorkerData.filter(function(data) {
		if(data.cm_username == "전체") return false;
		else return true;
	});
	
	grdWorker.setData(grdWorkerData);
	grdWorker.setConfig();
	grdWorker.repaint();
	
	if(grdWorkerData.length > 0) {
		var clickSw = false;
		
		for(var i=0; i<grdWorkerData.length; i++) {
        	if(grdWorkerData[i].cc_userid == userId) {
        		grdWorker.select(i);
        		clickSw = true;
        		break;
        	}
        }
		
		if(!clickSw) {
			grdWorker.select(0);
		}
		grdWorker_Click();
	}
}
	
function getWorktime() {
	var tmpData;
	tmpData = new Object();
	tmpData = {
		IsrId		: strIsrId,
		requestType	: 'get_Worktime'
	}
	ajaxAsync('/webPage/ecmc/Cmc0200Servlet', tmpData, 'json', successWorktime);
}

function successWorktime(data) {
	grdWorkTimeData = data;
	grdWorkTime.setData(grdWorkTimeData);
	
	worktimeFiltering();
}

//worktimeSetting
function worktimeFiltering() {
	if($('#chkAll').is(':checked')) {
		grdWorkTime.setData(grdWorkTimeData);
	}else {
		gridSelectedIndex = grdWorker.selectedDataIndexs;
		selectedGridItem = grdWorker.list[grdWorker.selectedDataIndexs];
		
		if(grdWorkerData == null || gridSelectedIndex < 0 || grdWorkTimeData == null || selectedGridItem == null) {
			return;
		}
		
		grdWorkTimeData_filter = grdWorkTimeData.filter(function(data) {
			if(grdWorker.selectedDataIndexs < 0) return false;
			if(data.cc_userid == selectedGridItem.cc_userid) return true;
			else return false;
		});
		grdWorkTime.setData(grdWorkTimeData_filter);
	}
	
	grdWorkTime.setConfig();
	grdWorkTime.repaint();
}

function grdWorker_Click() {
	gridSelectedIndex = grdWorker.selectedDataIndexs;
	selectedGridItem = grdWorker.list[grdWorker.selectedDataIndexs];
	
	if(gridSelectedIndex < 0) {
		return;
	}
	
	$('#txtWriteDay').val(selectedGridItem.creatdt);
	$('#txtWriter').val(selectedGridItem.cm_username);
	$('#txtExpTime').val(selectedGridItem.devtime);
	
	if(cboRateData != null) {
		for(var i=0; i<cboRateData.length; i++) {
			if(cboRateData[i].value == selectedGridItem.cc_rate) {
				$('[data-ax5select="cboRate"]').ax5select('setValue',selectedGridItem.cc_rate,true);
				break;
			}
		}
	}
	 
	if(selectedGridItem.cc_devstday != null && selectedGridItem.cc_devstday != "") {
		$('#txtExpStdate').val(selectedGridItem.cc_devstday.substr(0,4) + "/" +
							selectedGridItem.cc_devstday.substr(4,2) + "/" +
							selectedGridItem.cc_devstday.substr(6,2));
	}
	
	if(selectedGridItem.cc_devedday != null && selectedGridItem.cc_devedday != "") {
		$('#txtExpEnddate').val(selectedGridItem.cc_devedday.substr(0,4) + "/" +
							selectedGridItem.cc_devedday.substr(4,2) + "/" +
							selectedGridItem.cc_devedday.substr(6,2));
	}
	
	if(selectedGridItem.cnt == 0) {
		if(selectedGridItem.devmm == null || selectedGridItem.devmm == "") {
			$('#rdoResult').wRadio("disabled", true);
		}else {
			$('#rdoResult').wRadio("disabled", false);
		}
		$('#rdoPlan').wRadio("check", true);
	}else {
		$('#rdoResult').wRadio("disabled", false);
		$('#rdoResult').wRadio("check", true);
	}
	
	if(grdWorkerData.length > 0) {
		worktimeFiltering();
	}
	
	rdoPlan_click();
}

function grdWorkTime_dbClick() {
	gridSelectedIndex = grdWorkTime.selectedDataIndexs;
	
	if(gridSelectedIndex < 0) {
		return;
	}
	
	if ($('#btnRegResult').is(':enabled')){
		confirmDialog.confirm({
			msg: '작업시간을 삭제 하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				
				selectedGridItem = grdWorkTime.list[gridSelectedIndex];
				
				var WorkResultInfo = new Object();
				WorkResultInfo.srid = strIsrId;
				WorkResultInfo.userid = selectedGridItem.cc_userid;
				WorkResultInfo.workday = replaceAllString(selectedGridItem.cc_workday, '/', '');
				
				var WorkResultInfoData;
				WorkResultInfoData = new Object();
				WorkResultInfoData = {
						etcData 	 : WorkResultInfo,
					requestType	 	 : 'setTimeDeleteList'
				}
				ajaxAsync('/webPage/ecmc/Cmc0200Servlet', WorkResultInfoData, 'json', successDeleteWorkResult);
			}
		});
	}
}

function successDeleteWorkResult(data) {
	if(data == "0") {
		dialog.alert('작업시간이 삭제 되었습니다.',function(){
			screenInit("M");
			initDevPlan();
		});
	}else {
		dialog.alert('작업시간 삭제 중 오류가 발생하였습니다.',function(){});
	}
}

//개발계획등록
function btnRegPlan_Click() {
	if($('#txtExpTime').val().length == 0) {
		dialog.alert('예상소요시간을 입력하여 주시기 바랍니다.',function(){});
		return;
	}
	
	if($('#txtExpStdate').val().length <= 0) {
		dialog.alert("예상개발시작일을 입력하여 주시기 바랍니다.");
		return;
	}
	
	if($('#txtExpEnddate').val().length <= 0) {
		dialog.alert("예상개발종료일을 입력하여 주시기 바랍니다.");
		return;
	}
	
	if($('#txtExpTime').val() == 0) {
		dialog.alert('예상소요시간을 숫자(1이상)로 입력해 주시기 바랍니다.',function(){});
		return;
	}
	
	if (getSelectedIndex('cboRate') < 1) {
		dialog.alert('기능점수등급을 선택해주시기 바랍니다.',function(){});
		return;
	} 

	if($('#txtExpStdate').val() > $('#txtExpEnddate').val()) {
		dialog.alert('개발종료일이 개발시작일이전입니다. 개발기간을 정확히 선택하여 주시기 바랍니다.',function(){});
		return;
	}
	
	var PlanInfo = new Object();
	PlanInfo.devstday = replaceAllString($('#txtExpStdate').val(), '/', ''); 
	PlanInfo.devedday = replaceAllString($('#txtExpEnddate').val(), '/', '');
	PlanInfo.srid = strIsrId;
	PlanInfo.devmm = "0";
	PlanInfo.userid = userId;
	PlanInfo.devtime = $('#txtExpTime').val();
	PlanInfo.rate = getSelectedVal('cboRate').value;
	
	var PlanInfoData;
	PlanInfoData = new Object();
	PlanInfoData = {
		etcData 	 : PlanInfo,
		requestType	 : 'setInsertList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0200Servlet', PlanInfoData, 'json', successDevPlan);
}

function successDevPlan(data) {
	if(data == "0") {
		dialog.alert('개발계획등록이 정상적으로 처리되었습니다.',function(){
			initDevPlan();
			screenInit("M");
		});
		
	}else {
		dialog.alert('개발계획등록 중 오류가 발생하였습니다.',function(){});
	}
}

//개발실적등록
function btnRegResult_Click() {
	var NowDate = getDate('DATE',0);
	
	if(replaceAllString($('#txtDevDate').val(), '/', '') > NowDate) {
		dialog.alert('작업일자가 현재일자 이후입니다. 정확히 선택하여 주시기 바랍니다.',function(){});
		$('#txtDevDate').focus();
		return;
	}
	
	if($('#txtDevTime').val().length == 0) {
		dialog.alert('작업시간을 입력하여 주십시오.',function(){});
		return;
	}
	
	if($('#txtDevTime').val() == 0) {
		dialog.alert('작업시간을 숫자(1이상)로 입력해 주시기 바랍니다.',function(){});
		return;
	}
	
	if($('#txtDevTime').val() > 24) {
		dialog.alert('작업시간은 24시간 초과 입력을 할 수 없습니다.',function(){});
		return;
	}
	
	if($('#txtExpStdate').val() > $('#txtDevDate').val()) {
		dialog.alert('작업일은 예상개발시작일보다 커야 합니다.',function(){});
		return;
	}
	
	var WorkResultInfo = new Object();
	WorkResultInfo.srid = strIsrId;
	WorkResultInfo.userid = userId;
	WorkResultInfo.workday = replaceAllString($('#txtDevDate').val(), '/', '');
	WorkResultInfo.worktime = $('#txtDevTime').val();
	
	var WorkResultInfoData;
	WorkResultInfoData = new Object();
	WorkResultInfoData = {
			etcData 	 : WorkResultInfo,
		requestType	 	 : 'setTimeInsertList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0200Servlet', WorkResultInfoData, 'json', successWorkResult);
}

function successWorkResult(data) {
	if(data == "0") {
		dialog.alert('개발실적등록이 정상적으로 처리되었습니다.',function(){
			screenInit("M");
			initDevPlan();
		});
	}else {
		dialog.alert('개발실적등록 중 오류가 발생하였습니다.',function(){});
	}
}