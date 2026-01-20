/**
 * [환경설정 > 운영시간관리 TAB] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-24
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var operTimeGrid		= new ax5.ui.grid();

var operTimeGridData 	= [];
var cboTimeDivData		= [];

$('[data-ax5select="cboTimeDiv"]').ax5select({
    options: []
});

$('#txtTimeSt').timepicker({
	direction:'top',
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: true,
    scrollbar: true
 });


$('#txtTimeEd').timepicker({
	direction:'top',
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: true,
    scrollbar: true
 });

$(document).ready(function() {
	setGrid();
	getCodeInfo();
	getOperTimeList();
	// 등록 
	$('#btnReq').bind('click', function() {
		insertOperTime();
	});
	
	// 등록 
	$('#btnDel').bind('click', function() {
		deleteOperList();
	});
});

function setGrid() {
	operTimeGrid.setConfig({
	    target: $('[data-ax5grid="operTimeGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    showLineNumber: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	            clickOperGrid(this.dindex);
	        },
	        onDBLClick: function () {},
	    	trStyleClass: function () {},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_codename", 	label: "운영시간구분",  	width: '46%', align: 'left' },
	        {key: "stedtime", 		label: "운영시간",  		width: '46%' },
	    ]
	});
}

// 등록
function insertOperTime() {
	
	var txtTimeSt = $('#txtTimeSt').val().trim();
	var txtTimeEd = $('#txtTimeEd').val().trim();
	
	txtTimeSt = replaceAllString(txtTimeSt, ':', '');
	txtTimeEd = replaceAllString(txtTimeEd, ':', '');
	
	txtTimeSt = txtTimeSt.length === 3 ? '0' +txtTimeSt : txtTimeSt;
	txtTimeEd = txtTimeEd.length === 3 ? '0' +txtTimeEd : txtTimeEd;
	
	if(getSelectedIndex('cboTimeDiv') < 1) {
		dialog.alert('시간구분을 선택하시고 등록하시기 바랍니다.', function(){});
		return;
	}
	
	if(txtTimeSt.length === 0 || txtTimeEd.length === 0) {
		dialog.alert('운영시간을 정확히 입력해 주시기 바랍니다.', function(){});
		return;
	}
	
	var data = new Object();
	data = {
		timegb 		: getSelectedVal('cboTimeDiv').value,
		stTime 		: txtTimeSt,
		edTime 		: txtTimeEd,
		requestType	: 'addTab1Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successInsertOperTime);
}

// 등록 완료
function successInsertOperTime(data) {
	dialog.alert(data.retmsg, function(){});
	getOperTimeList();
}

// 폐기
function deleteOperList() {
	var selIn = operTimeGrid.selectedDataIndexs;
	if(selIn.length === 0 ) {
		dialog.alert('폐기 대상을 체크해 주시기 바랍니다.');
		return;
	}
	
	var data = new Object();
	data = {
		timegb 		: getSelectedVal('cboTimeDiv').value,
		requestType	: 'delTab1Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successDeleteOperList);
}

// 폐기 완료
function successDeleteOperList(data) {
	dialog.alert(data.retmsg, function(){});
	getOperTimeList();
}

// 운영시간관리 리스트 클릭
function clickOperGrid(index) {
	var selItem = operTimeGrid.list[operTimeGrid.selectedDataIndexs[0]];
	
	var txtTimeSt = selItem.cm_sttime.substr(0,2) + ':' + selItem.cm_sttime.substr(2,2);
	var txtTimeEd = selItem.cm_edtime.substr(0,2) + ':' + selItem.cm_edtime.substr(2,2);
	
	$('#txtTimeSt').val(txtTimeSt);
	$('#txtTimeEd').val(txtTimeEd);
	$('[data-ax5select="cboTimeDiv"]').ax5select('setValue', selItem.cm_timecd, true);
}

// 운영시간관리 리스트 가져오기
function getOperTimeList() {
	var data = new Object();
	data = {
		requestType	: 'getTab1Info'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetOperTimeList);
}

// 운영시간관리 리스트 가져오기 완료
function successGetOperTimeList(data) {
	operTimeGridData = data;
	operTimeGrid.setData(operTimeGridData);
}

// 시간구분가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([new CodeInfo('ECAMSTIME','SEL','N')]);
	cboTimeDivData 	= codeInfos.ECAMSTIME;
	
	$('[data-ax5select="cboTimeDiv"]').ax5select({
        options: injectCboDataToArr(cboTimeDivData, 'cm_micode' , 'cm_codename')
	});
}
