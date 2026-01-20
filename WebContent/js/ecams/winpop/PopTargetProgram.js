/**
 * [대상프로그램 등록] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이해성
 * 	버전 		: 1.1
 *  수정일 	: 2021-04-27
 * 
 */
var userId 	= $('#userId').val();  //strUserId
var code 	= $('#code').val();  //code
var reqCd 	= $('#reqCd').val();  //strReqCd
var isrId 	= $('#isrId').val();  //strIsrId + strIsrSub

var grdLst	= new ax5.ui.grid();
var grdProg	= new ax5.ui.grid();

var cboTeam_dp = [];
var grdProg_dp = [];
var grdLst_dp = [];
var strIsrId = "";
var strIsrSub = "";
var strUserId = "";
var strReqCd = "";
var strBaseIsr = "";
var strBaseProg = "";

$('[data-ax5select="cboTeam"]').ax5select({
	options : []
});

$(document).ready(function() {
	// 등록 버튼
	$('#btnReq').bind('click', function() {
		btnCopy_Click();
	});
	// 닫기 버튼
	$('#btnClose').bind('click', function() {
		btnClose_click();
	});
	// 조회 버튼
	$('#btnQry').bind('click', function() {
		btnQry_click();
	});
	// 전체선택 체크
	$('#chkAll').bind('change', function() {
		chkAll_click();
	});				

	strUserId = userId;
	strIsrId = isrId;
	strReqCd = reqCd;

	strIsrSub = strIsrId.substr(12);
	strIsrId = strIsrId.substr(0,12);

	$('#txtIsrId').val(strIsrId + "-" + strIsrSub);

	getTeamInfoGrid2();
	getPrjInfoDetail();
});

grdLst.setConfig({
    target: $('[data-ax5grid="grdLst"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
        columnHeight: 30
    },
    paging : false,
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
			grdLst_click(this.item, this.dindex, false);
        },
        onDBLClick: function () {},
        trStyleClass: function () {},
    	onDataChanged: function(){}
    },
    columns: [
        {key: "cc_isrid", 		label: "ISR ID",  	width: '10%', align: "left"},
        {key: "cc_isrsub",		label: "SUB#",  	width: '5%'},
        {key: "isrtitle",		label: "요청제목",   width: '15%'},
        {key: "reqday",   		label: "요청등록일",	  width: '10%'},
        {key: "reqenddt", 		label: "완료요청일",  width: '12%'},
        {key: "requser", 		label: "요청인",    width: '13%', align: "left"},
		{key: "reqdept", 		label: "요청부서",   width: '13%', align: "left"},
		{key: "recvdept",		label: "변경파트",   width: '13%', align: "left"},
		{key: "status",  		label: "진행현황", 	  width: '13%', align: "left"},
    ]
});

grdProg.setConfig({
    target: $('[data-ax5grid="grdProg"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
        columnHeight: 30
    },
    paging : false,
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {},
    	onDataChanged: function(){}
    },
    columns: [
        {key: "sysmsg", 	label: "시스템",  		width: '15%', align: "left"},
        {key: "dirpath",	label: "프로그램경로", 	width: '20%', align: "left"},
        {key: "rsrcname", 	label: "프로그램명",  	width: '20%', align: "left"},
        {key: "sta", 		label: "상태",  		width: '15%', align: "left"},
        {key: "username", 	label: "최종변경인",  	width: '15%', align: "left"},
		{key: "story", 		label: "프로그램설명",  	width: '20%', align: "left"},
    ]
});

function screenInit() {
	grdLst_dp = [];
	$('#txtISRId').val("");
	$('#txtReqDT').val("");
	$('#txtStatus').val("");
	$('#txtTitle').val("");
	$('#txtReqDept').val("");
	$('#txtProStatus').val("");
	$('#txtEditor').val("");
	$('#txtComEdDt').val("");

	grdProg_dp = [];
}

function getTeamInfoGrid2() {
	var userInfo = {
		selMsg : "All",
		cm_useyn : "Y",
		gubun : "sub",
		itYn : "N",
		requestType : 'getTeamInfo'
	}
	ajaxAsync('/webPage/common/CommonTeamInfo', userInfo, 'json',
			successGetTeamInfoGrid2);
}

function successGetTeamInfoGrid2(data) {
	data[0].cm_deptname = "전체"
	cboTeam_dp = data;

	$('[data-ax5select="cboTeam"]').ax5select({
		options: injectCboDataToArr(cboTeam_dp, 'cm_deptcd' , 'cm_deptname')
	});

	btnQry_click();	
}

function getPrjInfoDetail() {
	var obj = new Object();
	obj.IsrId = strIsrId;
	obj.SubId = strIsrSub;
	obj.UserId = strUserId;
	obj.ReqCd = "XX";
	
	var data = new Object();
	data.objData = obj;
	data.requestType = 'getPrjInfoDetail';
	ajaxAsync('/webPage/winpop/PopCopyTestCase', data, 'json',
			successGetPrjInfoDetail);
}

function successGetPrjInfoDetail(data) {
	var tmpArray = [];
	tmpArray = data;
	if (tmpArray.length > 0) {
		$('#txtIsrId').val("["+$('#txtIsrId').val() + "] " + tmpArray[0].isrtitle);

		var data = {
			IsrId : strIsrId,
			SubId : strIsrSub,
			UserId : strUserId,
			requestType : 'getProgIsr'
		}
		ajaxAsync('/webPage/winpop/PopTargetProgram', data, 'json',
				successGetProgIsr);		
	}	
}

function successGetProgIsr(data) {
	strBaseIsr = data;	
	if (strBaseIsr != null && strBaseIsr != "") {
		$('#txtIsrId').val($('#txtIsrId').val() + "  --> 기 프로그램 연결된 ISR-ID : " + strBaseIsr.substr(0,12) + "-" + strBaseIsr.substr(12));

		var data = {
			IsrId : strIsrId,
			SubId : strIsrSub,
			UserId : strUserId,
			requestType : 'getRelatList'
		}
		ajaxAsync('/webPage/winpop/PopTargetProgram', data, 'json',
				successGetRelatList);	
	}
}

function successGetRelatList(data) {
	strBaseProg = data;
	if (strBaseProg != null && strBaseProg != "") {
		btnQry_click();
	}
}

//조회
function btnQry_click() {
				
	screenInit();

	var tmpObj = new Object();
	//대상목록추출
	tmpObj.userid = strUserId;
	tmpObj.reqcd = "RP";   //대상프로그램
	if (cboTeam.selectedIndex > 0) { 
		tmpObj.recvdept = getSelectedVal('cboTeam').cm_deptcd;
	}
	tmpObj.secuyn = "N";
	tmpObj.stday = "";
	tmpObj.edday = "";
	if (strBaseIsr != null && strBaseIsr != "") { 
		tmpObj.baseisr = strBaseIsr;
	}
	   
	var tmpInfo = new Object();
	tmpInfo.tmpObjData = tmpObj;
	tmpInfo.requestType = "getPrjList_Chg"

	ajaxAsync('/webPage/winpop/PopTargetProgram', tmpInfo, 'json', successGetPrjList_Chg);

	tmpObj = null;   	   
}

function successGetPrjList_Chg(data) {
	if (data !== 'ERR') {
		grdLst_dp = data;	
		grdLst.setData(grdLst_dp);
	}
	if (grdLst_dp.length > 0 && strBaseIsr != null && strBaseIsr != "") {
		grdLst.select(0);
		grdLst_click();
	}	
}

function grdLst_click() {

	$('#txtISRId').val(grdLst.getList('selected')[0].isrid);
	$('#txtReqDT').val(grdLst.getList('selected')[0].reqdate);
	$('#txtStatus').val(grdLst.getList('selected')[0].reqsta1);
	$('#txtTitle').val(grdLst.getList('selected')[0].isrtitle);
	$('#txtReqDept').val(grdLst.getList('selected')[0].reqdept);
	$('#txtProStatus').val(grdLst.getList('selected')[0].reqsta2);
	$('#txtEditor').val(grdLst.getList('selected')[0].requser);
	$('#txtComEdDt').val(grdLst.getList('selected')[0].reqenddt);

	grdProg_dp = [];

	var data = {
		IsrId : grdLst.getList('selected')[0].cc_isrid,
		SubId : grdLst.getList('selected')[0].cc_isrsub,
		UserId : strUserId,
		requestType : 'getProgList'
	}
	ajaxAsync('/webPage/winpop/PopTargetProgram', data, 'json',
			successGetProgList);

}	

function successGetProgList(data) {
	var tmpObj = new Object();
		    	
	grdProg_dp = data;

	if (grdProg_dp.length > 0 && strBaseProg != null && strBaseProg != "") {
		grdProg_dp.forEach(function(item, index) {
			if (strBaseProg.indexOf(grdProg_dp[index].itemid+",") >= 0) {
				grdProg_dp[index].__selected__ = true;
				grdProg_dp[index].__disable_selection__ = true;
			}
		});		
	}
	grdProg.setData(grdProg_dp);
	grdProg.clearSelect();
}

//전체선택
function chkAll_click() {
	   			
	if (grdProg.getList().length == 0){
		   $('#chkAll').prop('checked', false);
		   return;
	}

	if ($('#chkAll').prop('checked') == true) { 
		grdProg_dp.forEach(function(item, index) {
			grdProg.select(index);
		});
	} else {
		grdProg_dp.forEach(function(item, index) {
			grdProg.clearSelect(index);
		});
	} 
}
//복사
function btnCopy_Click() {
	var tmpObj = new Object();
	var tmpArray = [];
	
	for(var i = 0; grdProg_dp.length > i; i++){
		if (grdProg.getList('selected')[i]) {
			tmpObj = new Object();
			tmpObj.itemid = grdProg.getList('selected')[i].itemid;			
			tmpArray.push(tmpObj);				
		}
	}
	
	if (tmpArray.length == 0) {
		dialog.alert("등록할 프로그램을 선택한 후 처리하시기 바랍니다.");
		return;
	}
	
	tmpObj = new Object();
	tmpObj.UserId = strUserId;
	tmpObj.toisrid = strIsrId;
	tmpObj.toisrsub = strIsrSub;
	tmpObj.fromisrid = grdLst.getList('selected')[0].cc_isrid;
	tmpObj.fromisrsub = grdLst.getList('selected')[0].cc_isrsub;

	var tmpInfo = new Object();
	tmpInfo.objData = tmpObj;
	tmpInfo.copyList = tmpArray;
	tmpInfo.requestType = "setProgList"

	ajaxAsync('/webPage/winpop/PopTargetProgram', tmpInfo, 'json', successSetProgList);

	tmpObj = null; 	
}

function successSetProgList(data) {
	var retMsg = data;
		    	 
	if (retMsg != "0") {
		dialog.alert(retMsg);
	} else {
		dialog.alert({
			msg: "대상프로그램 등록 처리를 완료하였습니다.",
		}, function() {
			if(this.key === 'ok') {
				btnClose_click();
			}
		})			
	}
}
//닫기
function btnClose_click() {
	window.close();
}
