/** 
 * [테스트관리] 테스트종료
 */

var userId 		 	= window.parent.userId;
//var strReqCd	 	= window.parent.strReqCd; 

var testGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();

var gridData = [];
var gridIndex = '';
var selectedGridItem = [];

var strReqCd = '59';

var tab1 = '';
var tab2 = '';
var tab3 = '';
var tabBase	= '';
var tmpInfo	= {};
var teamData= [];
var option	= [];

var loadSrReg = false;

ax5.info.weekNames = [ 
	{label : "일"}, 
	{label : "월"}, 
	{label : "화"}, 
	{label : "수"}, 
	{label : "목"}, 
	{label : "금"}, 
	{label : "토"}
 ];

picker.bind(defaultPickerInfo('topDate', 'top'));

//picker 현재 날짜
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, '/');
	$("#dateSt").val(today);
	$("#dateEd").val(today);
});

testGrid.setConfig({
	target : $('[data-ax5grid="testGrid"]'),
	sortable : true,
	multiSort : true,
	showLineNumber : true,
	header : {
		align : "center",
		columnHeight : 30
	},
	body : {
		columnHeight : 28,
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
			grdList_Click();
		}
	},
	columns : 
	  [ 
		{key : "cc_isrid",	label : "ISR ID",	width: '12%',align : 'center'}, 
		{key : "cc_isrsub",	label : "#SUB",		width: '7%', align : 'center'}, 
		{key : "isrtitle",	label : "요청제목",	width: '20%',align : 'center'}, 
		{key : "reqday",	label : "요청등록일",	width: '13%',align : 'center'}, 
	    {key : "reqenddt",	label : "완료요청일",	width: '12%',align : 'center'}, 
	    {key : "requser",	label : "요청인",		width: '10%',align : 'center'}, 
	    {key : "reqdept",	label : "요청부서",	width: '15%',align : 'center'}, 
	    {key : "recvdept",	label : "변경파트",	width: '15%',align : 'center'}, 
	    {key : "status",	label : "진행현황",	width: '20%',align : 'center'} 
	  ]
});

$(document).ready(function() {
	
	var oldVal = '';
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').on('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	screenInit();
	
	getTeamInfo();
	
	// 조회
	$('#btnQry').on('click', function() {
		btnQry_Click(1);
	});
	
	// 대상구분
	$('#cboQryGbn').on('change', function() {
		cboQryGbn_Change();
	});
	
	// 요구관리정보
	$('#btnReqInfo').on('click', function() {
		testwinOpen("REQ");
	});

	// 변경관리정보
	$('#btnChgInfo').on('click', function() {
		testwinOpen("CHG");
	});
	
});

function screenInit(){
	
	$('[data-ax5select="cboQryGbn"]').ax5select({
		options : []
	});
	$('[data-ax5select="cboTeam"]').ax5select({
		options : []
	});
	
	tab1 = $('#frmTestAccept').get(0).contentWindow;
	tab2 = $('#frmIntegrationTesting').get(0).contentWindow;
	tab3 = $('#frmTestComplete').get(0).contentWindow;
	tabBase = $('#frmBaseTestInfo').get(0).contentWindow;
	
	$('#dateSt').prop("disabled", true);
	$('#dateEd').prop("disabled", true);
	$('#btnSt').prop("disabled", true);
	$('#btnEd').prop("disabled", true);
	
	$("#tabTestComplete").show();
	$('#btnReqInfo').prop('disabled', true);
	$('#btnChgInfo').prop('disabled', true);
}

function baseInit(){
	
	$("#test_tab").fadeTo(0, 0.5);
	$('#test_tab').css('pointer-events', 'none');	
	tabBase.$('#txtISRId').val('');
	tabBase.$('#txtReqDT').val('');
	tabBase.$('#txtStatus').val('');
	tabBase.$('#txtTitle').val('');
	tabBase.$('#txtReqDept').val('');
	tabBase.$('#txtProStatus').val('');
	tabBase.$('#txtEditor').val('');
	tabBase.$('#txtComEdDt').val('');
}

// 탭메뉴
$('.tabs li').on('click', function() {
	if($(this).hasClass("on")) {
		tabnavi_on();
		return;
	}

	$(".tab_content").hide();
	var activeTab = $(this).attr("rel");
	
	$(".tabs li").removeClass('on');
	$(this).addClass("on");
	
	$("#" + activeTab).fadeIn();
	tabnavi_on();
});

function getTeamInfo(){
	
	tmpInfo = {
		selMsg		: 'All',
		cm_useyn	: 'Y',
		gubun		: 'sub',
		itYn		: 'N',
		requestType : 'getTeamInfo'
	}
	
	ajaxAsync('/webPage/common/CommonTeamInfo', tmpInfo, 'json', successGetTeamInfo);
}

function successGetTeamInfo(data) {
	teamData = data;

	$('[data-ax5select="cboTeam"]').ax5select({
		options: injectCboDataToArr(teamData, 'cm_deptcd' , 'cm_deptname')
	});	

	cboSet();
}

function cboSet(){
	
	$('[data-ax5select="cboQryGbn"]').ax5select({
		options: [
			{value: "00", text: "전체"},
			{value: "01", text: "종료승인대상"},
			{value: "02", text: "테스트종료대상"},
			{value: "03", text: "테스트종료요청"},
			{value: "04", text: "테스트종료승인"},
			{value: "05", text: "테스트종료승인(변경관리)"},
			{value: "06", text: "테스트종료반려"}
			]
	}); 
	
	$('[data-ax5select="cboQryGbn"]').ax5select("setValue", "01", true);
	
	cboQryGbn_Change();
	btnQry_Click();
}

function cboQryGbn_Change(){
	
	if(getSelectedVal('cboQryGbn').value == '00'){
		$('#dateSt').prop("disabled", false);
		$('#dateEd').prop("disabled", false);
		$('#btnSt').prop("disabled", false);
		$('#btnEd').prop("disabled", false);
	} else return;
}

function btnQry_Click(num){

	testGrid.setData([]);
    var qryGbn = getSelectedVal('cboQryGbn').value;	
    
    tmpInfo = {};
	tmpInfo.userid	= userId;
	tmpInfo.reqcd	= strReqCd;
	tmpInfo.qrygbn	= qryGbn;
	tmpInfo.secuyn	= 'Y';
	if(qryGbn == '00'){
		tmpInfo.stday = replaceAllString($("#dateSt").val(), '/', '');
		tmpInfo.edday = replaceAllString($("#dateEd").val(), '/', '');
	}
	var ajaxData = {
		tmpInfo		: tmpInfo,
		requestType	: 'getPrjList_Test'
	}
	
	gridData = ajaxCallWithJson('/webpage/test/TestAdministration', ajaxData, 'json');  
	
	testGrid.setData(gridData);
	
	baseInit();
	if(num == 1) tabnavi_on();
}

function grdList_Click(){
	
	gridIndex = testGrid.selectedDataIndexs;
	selectedGridItem = testGrid.list[gridIndex];
	tab1 = $('#frmTestAccept').get(0).contentWindow;
	tab2 = $('#frmIntegrationTesting').get(0).contentWindow;
	tab3 = $('#frmTestComplete').get(0).contentWindow;
	
	tabBase.$('#txtISRId').val(selectedGridItem.isrid);
	tabBase.$('#txtReqDT').val(selectedGridItem.reqdate);
	tabBase.$('#txtStatus').val(selectedGridItem.reqsta1);//
	tabBase.$('#txtTitle').val(selectedGridItem.isrtitle);
	tabBase.$('#txtReqDept').val(selectedGridItem.reqdept);
	tabBase.$('#txtProStatus').val(selectedGridItem.reqsta2);//
	tabBase.$('#txtEditor').val(selectedGridItem.requser);
	tabBase.$('#txtComEdDt').val(selectedGridItem.reqenddt);
			
	tab1.strIsrId = '';
	tab1.strIsrSub = '';
	tab2.strIsrId = '';
	tab2.strIsrSub = '';
	tab3.strIsrId = '';
	tab3.strIsrSub = '';
	
	$('#btnReqInfo').prop('disabled', true);
	$('#btnChgInfo').prop('disabled', true);
	$("#test_tab").fadeTo(0, 0.5);
	$('#test_tab').css('pointer-events', 'none');	

	if(selectedGridItem.maintab.indexOf("R") >= 0) {
		$('#btnReqInfo').prop('disabled', false);
	}
	if(selectedGridItem.maintab.indexOf("C") >= 0) {
		$('#btnChgInfo').prop('disabled', false);
	}
	
	tmpInfo = {
		IsrId		: selectedGridItem.cc_isrid,
		IsrSub		: selectedGridItem.cc_isrsub,
		ReqCd		: 'TST',
		UserId		: userId,
		requestType	: 'getSubTab'
	}
   		
	ajaxAsync('/webpage/test/TestAdministration', tmpInfo, 'json', successGetSubTab);
}

function successGetSubTab(data){
	
	if(data.indexOf('51') >= 0 || data.indexOf('52') >= 0 || data.indexOf('59') >= 0){
		$("#test_tab").fadeTo(0, 1);
		$('#test_tab').css('pointer-events', 'auto');	
		tabnavi_on();
	}
}

function tabnavi_on(){
	
	gridIndex = testGrid.selectedDataIndexs;
	selectedGridItem = testGrid.list[gridIndex];
	
	if($('.tabs li#tab1').hasClass('on')){
		if (selectedGridItem == undefined) {
			tab1.screenInit('M'); 
			return;
		}
		if (tab1.strIsrId == selectedGridItem.cc_isrid && tab1.strIsrSub == selectedGridItem.cc_isrsub) return;
		
		tab1.screenInit('M');    
		tab1.strIsrId = selectedGridItem.cc_isrid;
		tab1.strIsrSub = selectedGridItem.cc_isrsub;
		tab1.strSubStatus = selectedGridItem.cc_substatus;
		tab1.strReqCd = '51';
		tab1.strUserId = userId;
		
		tab1.$('#txtTestYn').val('YES'); 
		
		tab1.$('#btnReg').prop('disabled', true);
		
		tab1.testcntCall();
	} else if($('.tabs li#tab2').hasClass('on')){
		if (selectedGridItem == undefined) {
			tab2.screenInit('M'); 
			return;
		}
		if (tab2.strIsrId == selectedGridItem.cc_isrid && tab2.strIsrSub == selectedGridItem.cc_isrsub) return;
		
		tab2.screenInit('M');    
		tab2.strIsrId = selectedGridItem.cc_isrid;
		tab2.strIsrSub = selectedGridItem.cc_isrsub;
		tab2.strSubStatus = selectedGridItem.cc_substatus;
		tab2.strReqCd = '52';
		tab2.strUserId = userId;
		
		tab2.testcntCall();
	} else if($('.tabs li#tab3').hasClass('on')){
		if (selectedGridItem == undefined) {
			tab3.screenInit('M'); 
			return;
		}
		if (tab3.strIsrId == selectedGridItem.cc_isrid && tab3.strIsrSub == selectedGridItem.cc_isrsub) return;
		
		tab3.screenInit('M');    
		tab3.strIsrId = selectedGridItem.cc_isrid;
		tab3.strIsrSub = selectedGridItem.cc_isrsub;
		tab3.strSubStatus = selectedGridItem.cc_substatus;
		tab3.strReqCd = strReqCd;
		tab3.strUserId = userId;
		
		tab3.testcntCall();
	}
	
}

function testwinOpen(status){
	
	var cURL = '';
	var nHeight = 740;
    var nWidth  = 1500;
    var isr = selectedGridItem.cc_isrid + '-' + selectedGridItem.cc_isrsub;
    var winName = 'popUp';
    
	if (status == 'REQ') {
		cURL = "/webPage/winpop/PopReqInfo.jsp";
	} else if (status == 'CHG') {
		cURL = "/webPage/winpop/PopChgInfo.jsp";
	}
	
	var f = document.popPam1;   		
    
    f.isrId.value	= isr;    	
    f.user.value 	= userId;    	

    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}













