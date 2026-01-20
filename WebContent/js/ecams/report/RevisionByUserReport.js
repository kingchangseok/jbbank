/**
 * [보고서 > 개인별변경추이] 
 */
var userId 		= window.top.userId;
var codeList    = window.top.codeList;  //전체 코드 리스트
var adminYN 	= window.top.adminYN;	//'N' 관리자여부

var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var options 	= [];
var columnData 	= [];
var titleData	= [];
var tmpInfo 	= {};

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
	},
	columns : columnData
}); 


picker.bind({
    target: $('[data-ax5picker="basic"]'),
    direction: "top",
    content: {
        width: 220,
        margin: 10,
        type: 'date',
        config: {
            control: {
                left: '<i class="fa fa-chevron-left"></i>',
                yearTmpl: '%s',
                monthTmpl: '%s',
                right: '<i class="fa fa-chevron-right"></i>'
            },
            dateFormat: 'yyyy/MM/dd',
            lang: {
                yearTmpl: "%s년",
                months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
                dayTmpl: "%s"
            }
        },
        formatter: {
            pattern: 'date'
        }
    },
    btns: {
        today: {
            label: "Today", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .close();
            }
        },
        thisMonth: {
            label: "This Month", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
                                + '/'
                                + ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
                        .close();
            }
        },
        ok: {label: "Close", theme: "default"}
    }
});

$(document).ready(function() {
	
	screenInit();

	//경로
	getTmpDir('99');

	//요청구분
	getCodeInfo();
	
	//시스템
	getSysInfo();
	
	//조회구분
	setFind();

	//시스템 변경에 따른 프로그램종류
	$("#cboSys").bind("change", function(){
		cboSys_Change();	
	});
	
	//사용자명 입력
	$('#txtUserName').bind('keydown', function(event) {
		if(event.keyCode === 13) {
			userFind();	
		}
	});

	$("#btnSearch").bind("click", function(){
		btnSearch_Click();	
	});
	
	$("#btnExcel").bind('click', function() {
		mainGrid.exportExcel("개인별변경추이" + today + ".xls");
	});
	
});

function screenInit(){
	
	$('input:radio[name=radio]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	$('#btnExcel').prop('disabled', true);
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboSel"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboProg"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboReq"]').ax5select({
		options: []
	});
	$('#lblTeam').val('');
	$('[data-ax5select="cboUser"]').ax5select({
		options: []
	});
	
	//picker에 오늘 날짜 디폴트로 세팅
	$(function() {
		var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
		$("#dateEd").val(today);
		$("#dateSt").val(today);
	})
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
}

function getTmpDir(pCode){
	
	var data = new Object();
	data = {
		pCode 		: pCode,
		requestType	: 'getTmpDir'
	}
	var strTmpDir = ajaxCallWithJson('/webPage/common/SystemPathServlet', data, 'json');
	strTmpDir = strTmpDir + '/';
	
}

function getCodeInfo() {
	
//	var reqCd = fixCodeList(codeList['REQCD'], 'ALL', '', '', 'N');
	var request = fixCodeList(codeList['REQUEST'], 'ALL', '', '', 'N');
	var checkIn = fixCodeList(codeList['CHECKIN'], 'ALL', '', '', 'N');
	
	options = [];
	
	$.each(checkIn, function(i, item) {
		options.push({value : 'C'+item.cm_micode, text : '체크인['+item.cm_codename+']'});
	});
	
	$.each(request, function(i, item) {
		options.push({value : item.cm_micode, text : item.cm_codename});
	});

	$('[data-ax5select="cboReq"]').ax5select({
		options: options
	});
	
}

function getSysInfo() {
	
	var sysData = {
		UserId 	: userId,
		SecuYn 	: 'N',
		SelMsg 	: 'ALL',
		CloseYn : 'N',
		ReqCd	: '',
	}
	var data = {
		sysData : sysData,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	
	options = [];
	$.each(data, function(i, item) {
		options.push({value : item.cm_syscd, text : item.cm_sysmsg});
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: options
	});	
	
	cboSys_Change();
}

function cboSys_Change(){
	
	if (getSelectedIndex('cboSys') > 0){
		var data = {
				SysCd 		: getSelectedVal('cboSys').value,
				selMsg 		: 'ALL',
				requestType	: 'getJawon'
			}
			ajaxAsync('/webPage/report/RevisionByUserReport', data, 'json', SuccessGetJawon);
    }
}

function SuccessGetJawon(data){
	
	options = [];
	$.each(data, function(i, item) {
		options.push({value : item.cm_micode, text : item.cm_codename});
	});
	
	$('[data-ax5select="cboProg"]').ax5select({
		options: options
	});	
}

function setFind(){
	
	$('[data-ax5select="cboSel"]').ax5select({
		options: [
			{value: "0", text: "전체"},
			{value: "1", text: "시스템별"},
			{value: "2", text: "팀별"},
			{value: "3", text: "프로그램종류별"}
			]
	});
}

function userFind(){

	if($('#txtUserName').val() == '' && $('#txtUserName').val() == null){
		dialog.alert('사용자명을 입력하여 주십시오.');
		return;
	}
	
	var data = {
			clsCd 		: 'N',
			userN 		: $('#txtUserName').val(),
			requestType	: 'getUserList'
	}
	
	ajaxAsync('/webPage/report/RevisionByUserReport', data, 'json', SuccessGetUser);
}

function SuccessGetUser(data){
	
	options = [];
	$.each(data, function(i, item) {
		options.push({value : item.cm_userid, text : item.username});
	});
	
	$('[data-ax5select="cboUser"]').ax5select({
		options: options
	});	
}

function getInfo(){
	
	tmpInfo.qrygbn = getSelectedVal('cboSel').value;
	tmpInfo.syscd = getSelectedVal('cboSys').value;
	tmpInfo.qrycd = getSelectedVal('cboReq').value;
	if(getSelectedIndex('cboProg') > 0) tmpInfo.rsrccd =  getSelectedVal('cboProg').value;
	tmpInfo.stday = replaceAllString($("#dateSt").val(), '/', '');
	tmpInfo.edday = replaceAllString($("#dateEd").val(), '/', '');
	if ($("#optDay").is(":checked")) tmpInfo.term = 'D';
	else if ($("#optWeek").is(":checked")) tmpInfo.term = 'W';
	else tmpInfo.term = 'Y';
}

function btnSearch_Click(){
	
	    if (getSelectedIndex('cboUser') < 0){ 
	    	dialog.alert("사용자를 선택하여 주십시오."); 
		   return;
	    }
	    
	    getInfo(); //getProgList()와 같은 값 사용
		
		var data = {
				tmpInfo 	: tmpInfo,
				requestType	: 'getTitle'
		}
		
		ajaxAsync('/webPage/report/RevisionByUserReport', data, 'json', SuccessGetTitle);
}


function SuccessGetTitle(data){
	console.log("title= ", data);
	titleData = data;
	
	if (titleData.length > 0) {
		if (titleData[0].errmsg != null && titleData[0].errmsg != '') {
			dialog.alert(titleData[0].errmsg);
			return;
		}
	}	

	var lblDaygbn = '';
	if ($("#optDay").is(":checked")) lblDaygbn = '일별';
	else if ($("#optWeek").is(":checked")) lblDaygbn = '주간별';
	else lblDaygbn = '월별';
	columnData.push({key : "daygbn", label : lblDaygbn, align : "center", width: "10%"});
	
	var lblPrccnt = '';
	if(getSelectedVal('cboSel').value == '1') lblPrccnt = '처리건수';
	else lblPrccnt = '합계';
	columnData.push({key : "prccnt", label : lblPrccnt, align : "center", width: "90%", styleClass: "color-red"});

	if(getSelectedVal('cboSel').value != '0'){
		$.each(titleData, function(i, item) {
			columnData.push({key : "gbn" + item.cm_micode , label : item.cm_codename, align: "center", width : 85 / titleData.length + "%"});
		});
	}
	
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		columns : columnData
	});
	
	getProgList(titleData);
}

function getProgList(titleData){
	
    getInfo();
	
    if (getSelectedIndex('cboUser') > -1) tmpInfo.userid = getSelectedVal('cboUser').value;
	
	var progList = {
			tmpInfo		: tmpInfo,
			titleData	: titleData,
			requestType	: 'getSelect'
	}
	
	var data = ajaxCallWithJson('/webPage/report/RevisionByUserReport', progList, 'json');
	
	mainGrid.setData(data);
	
    if (data.length > 0) $('#btnExcel').prop('disabled', false);
}








