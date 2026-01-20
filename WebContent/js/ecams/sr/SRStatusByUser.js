var userName 	= window.top.userName;
var userid 		= window.top.userId;
var strReqCD 	= window.top.reqCd;
var codeList    = window.top.codeList;          //전체코드리스트

var firstGrid 	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();
var confirmDialog2 = new ax5.ui.dialog();

$('[data-ax5select]').ax5select({
	options : []
});


$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));

picker.bind(defaultPickerInfo('basic', 'top'));

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
           this.self.select(this.dindex);
        },
        onDBLClick: function () {
       	 
        },
    	trStyleClass: function () {
   	   	if (this.item.color == "3"){
   	   		return "fontStyle-cncl";
   	   	} else if (this.item.color == "R"){
   	   		return "fontStyle-rec";
   	   	} else if (this.item.color == "C" ){
   	   		return "fontStyle-dev";
   	   	} else if (this.item.color == "T" ){
   	   		return "fontStyle-apply";
   	   	} else if (this.item.color == "9"){
   	   		return "fontStyle-end";
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
            {type: 1, label: "요구관리정보"},
            {type: 2, label: "변경관리정보"},
            {type: 3, label: "테스트관리정보"}
        ],
        popupFilter: function (item, param) {
       	 if(param.item.maintab.indexOf('R') >= 0) {
       		 if(param.item.maintab.indexOf('C') >= 0) {
       			 if(param.item.maintab.indexOf('T') >= 0) {
       				 return item.type == 1 | item.type == 2 | item.type == 3;
       			 }
       			 return item.type == 1 | item.type == 2;
       		 }
       		 return item.type == 1;
       	 }
        },
        onClick: function (item, param) {
       	 openWindow(item.type, param.item.isrid);
            firstGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
		{key: "isrid",		label: "ISR ID"		, width: "5%", align: "center"},
		{key: "isrsub",		label: "SUB"		, width: "3%", align: "center"},
		{key: "reqdate",	label: "등록일"		, width: "7%", align: "center"},
		{key: "reqtitle",	label: "요청제목"		, width: "15%", align: "center"},
		{key: "recvdept",	label: "접수파트"		, width: "5%", align: "center"},
		{key: "chgsubuser",	label: "변경담당자"	, width: "5%", align: "center"},
		{key: "reqsta1",	label: "진행단계"		, width: "5%", align: "center"},
		{key: "reqsta2",	label: "진행상황"		, width: "10%", align: "center"},
		{key: "devyn",		label: "개발계획"		, width: "5%", align: "center"},
		{key: "chgpercent",	label: "진행율"		, width: "4%", align: "center"},
		{key: "unityn",		label: "단위테스트"	, width: "5%", align: "center"},
		{key: "sysyn",		label: "통합테스트"	, width: "5%", align: "center"},
		{key: "devprog",	label: "프로그램개발"	, width: "5%", align: "center"},
		{key: "testyn",		label: "테스트적용"	, width: "5%", align: "center"},
		{key: "realyn",		label: "운영적용"		, width: "5%", align: "center"},
		{key: "endyn",		label: "변경종료"		, width: "10%", align: "center"}
    ]
});

$('input:radio[name=rdoDate]').wRadio({theme: 'circle-radial red', selector: 'checkmark'});
$('input:checkbox[id=chkSelf]').wCheck({theme: 'square-classic blue', selector: 'checkmark'});


$(document).ready(function() {
	
	$('#cboSta1').bind('change', function() {
		changeStaInfo();
	});
	
	$('#btnQry').bind('click', function() {
		get_SelectList();
	});
	
	getDeptInfo1();
	getDeptInfo2();
	getCodeInfo();
	
})

// 신청현황 리스트 가져오기
function get_SelectList(){
	var strStD = $("#datStD").val();
	var strEdD = $("#datEdD").val();
	var tmpObj = {};
	
	if (strStD > strEdD) {
	   dialog.alert("조회기간을 정확하게 선택하여 주십시오."); 
	   return;
    }
	
	tmpObj.userid = userId;
	if (getSelectedVal("cboSta1").cm_micode != "XX") {
		tmpObj.stday = setDateFormat(strStD);
		tmpObj.edday = setDateFormat(strEdD); 
	}
	if (getSelectedIndex("cboDept1") > 0) tmpObj.reqdept = getSelectedVal("cboDept1").cm_deptcd;
	if (getSelectedIndex("cboDept2") > 0) tmpObj.recvdept = getSelectedVal("cboDept2").cm_deptcd;
	if (getSelectedIndex("cboSta1") > 0 && getSelectedVal("cboSta1").cm_micode != "XX") {
		tmpObj.reqsta1 = getSelectedVal("cboSta1").cm_micode;
		if (getSelectedIndex("cboSta2") > 0) tmpObj.reqsta2 = getSelectedVal("cboSta2").cm_micode;
	} else {
		if (getSelectedIndex("cboSta1") > 0) tmpObj.reqsta1 = getSelectedVal("cboSta1").cm_micode;	
	}
	if ($("#txtTitle").val().trim().length > 0) tmpObj.reqtit = $("#txtTitle").val().trim();
	
	if ($("#optSub1").is(":checked") == true) tmpObj.daygbn = "0";
	else if ($("#optSub2").is(":checked") == true) tmpObj.daygbn = "1";
	else tmpObj.daygbn = "2";
	if ($("#chkSelf").is(":checked") == true) tmpObj.selfsw = "Y";
	else tmpObj.selfsw = "N";
	
	tmpObj.scmuser = $("#txtChgUser").val();
	tmpObj.isrid = $("#txtSrId").val();
	
	var data =  new Object();
	data = {
		requestType : 'get_SelectList_scm',
		tmpObj : tmpObj
	}
	ajaxAsync('/webPage/sr/SRStatus', data, 'json',successGet_SelectList);
}

//신청 리스트 가져오기 완료
function successGet_SelectList(data) {
	console.log(data);
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

// 신청부서 cbo 가져오기
function getDeptInfo1(){
	var data =  new Object();
	data = {
			selMsg 		: 'All',
			cm_useyn 	: 'Y',
			gubun 		: 'DEPT',
			itYn 		: 'N',
			requestType	: 'getTeamInfo'
	}
	ajaxAsync('/webPage/common/CommonTeamInfo', data, 'json',successGetDeptInfo1);
}

//신청부서 cbo 가져오기 완료
function successGetDeptInfo1(data) {
	cboDeptData1 = data;
	
	$('[data-ax5select="cboDept1"]').ax5select({
        options: injectCboDataToArr(cboDeptData1, "cm_deptcd", "cm_deptname")
	});
}

// 신청부서 cbo 가져오기
function getDeptInfo2(){
	var data =  new Object();
	data = {
			selMsg 		: 'All',
			cm_useyn 	: 'Y',
			gubun 		: 'sub',
			itYn 		: 'Y',
			requestType	: 'getTeamInfo'
	}
	ajaxAsync('/webPage/common/CommonTeamInfo', data, 'json',successGetDeptInfo2);
}

//신청부서 cbo 가져오기 완료
function successGetDeptInfo2(data) {
	cboDeptData2 = data;
	
	$('[data-ax5select="cboDept2"]').ax5select({
        options: injectCboDataToArr(cboDeptData2, "cm_deptcd", "cm_deptname")
	});
}

function changeStaInfo() {
	var filteredStaData2 = [];
	selectedSta1 = getSelectedVal('cboSta1').value;
	$.each(cboStaData2, function(i, val) {
		if(val.cm_micode.substr(0,1) == selectedSta1.substr(1,1)) {
			filteredStaData2.push(val);
		}
	});
	
	$('[data-ax5select="cboSta2"]').ax5select({
		options : filteredStaData2
	});
}

// 진행상태 cbo , 신청종류 cbo 가져오기
function getCodeInfo(){
	
	cboStaData1 = fixCodeList(codeList['ISRSTAMAIN'], 'ALL', 'cm_micode', 'ASC', 'N');
	cboStaData2 = fixCodeList(codeList['ISRSTASUB'], 'ALL', 'cm_micode', 'ASC', 'N');
	codeList = null;
	
	$('[data-ax5select="cboSta1"]').ax5select({
        options: injectCboDataToArr(cboStaData1, "cm_micode", "cm_codename")
	});

	$('[data-ax5select="cboSta2"]').ax5select({
        options: injectCboDataToArr(cboStaData2, "cm_micode", "cm_codename")
	});
}