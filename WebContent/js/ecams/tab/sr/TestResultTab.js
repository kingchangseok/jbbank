/**
 * 개발계획/완료 탭 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 허정규
 * 	버전 : 1.0
 *  수정일 : 2020-04-23
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var codeList        = window.top.codeList;      //전체코드리스트

//public
var strIsrId		= window.parent.strIsrId;  

var grdDoc 			= new ax5.ui.grid();   //담당자그리드
var picker = new ax5.ui.picker();

var SrID = "";
var SysCd = "";
var SrTitle = "";
var SRStatus = "";
var GradeCd = "";
var AcptNo = "";
var notiPath = "";
var fileIndex = 0;
var cbouserData = [];
var confirmData = [];
var TotalFileSize = 0;
var developerList = [];
var testUser = false;
var developer = false;

var upFiles = [];
var upFiles2 = [];
var grdDocContextMenu = null;

ax5.info.weekNames = [
    {label: "일"},
    {label: "월"},
    {label: "화"},
    {label: "수"},
    {label: "목"},
    {label: "금"},
    {label: "토"}
];

//개발담당자
$('[data-ax5select="cbouser"]').ax5select({
	options: []
});

$(document).ready(function(){

	createGrid = false;

	$("#cmdResultTest").bind("click",function(){
		$("#doc").click();
	});
	
	$("#doc").bind("change",function(){
		if($("#doc").get(0).files && $("#doc").get(0).files[0]){
			var fileObj = new Object();
			fileObj = {
				srid : SrID,
				filegb : "22",
				editor : userId,
				testergbn : ""
			}
			window.parent.docList = grdDoc.getList();
			window.parent.upFiles = $("#doc").get(0); 
			window.parent.fileObj = fileObj; 
			window.parent.fileupload();
		}
	});
	
});

function createViewGrid() {
//	if(!createGrid) {
		grdDoc.setConfig({
		    target: $('[data-ax5grid="grdDoc"]'),
		    sortable: true, 
		    multiSort: true,
		    showRowSelector: false,
		    multipleSelect: false,
		    paging: false,
		    page : {
		    	display:false
		    },
		    header: {
		        align: "center"
		    },
		    body: {
		        onClick: function () {
		        	this.self.clearSelect();
		            this.self.select(this.dindex);
		        },
		        onDBLClick: function () {
		        	this.self.clearSelect();
		        	this.self.select(this.dindex);
		        	if(this.item.testDoc == "Y"){
		        		downSelectDoc(this.dindex, this.item);
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
		            {type: 1, label: "제거"}
		        ],
		        popupFilter: function (item, param) {
		        	if(isNaN(param.dindex < 0)){
		        		grdDoc.contextMenu.close();//또는 return true;
		        		return false;
		        	} else if(param.item.testDoc != "Y") {
		        		return false;
		        	}
		         	return true;
		       	 
		        },
		        onClick: function (item, param) {
		        	delSelectDoc(param.item);
		        	grdDoc.contextMenu.close();//또는 return true;
		        }
		    },
		    columns: [
				{key: "gbncd", label: "구분",  width: '10%'},
				{key: "cm_username", label: "요청자",  width: '8%', align: "center"},
				{key: "name", label: "파일명",  width: '60%'},
				{key: "testDoc", label: "테스트결과서첨부",  width: '11%', align: "center"}
			]
		});
	
	createGrid = true;
	grdDocContextMenu = grdDoc.config.contextMenu;
}

function setSRInfo_detail_func() {
	if(SrID == "" || SrID == null) return;
	 getSRInfo();
	 
	 initFrame();
}

function initFrame(){
	//SystemPath.getTmpDir("30");
	getTmpDir();
	
}

//엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pcode 		: '30',
		requestType	: 'getSystemPath'
	}
	ajaxAsync('/webPage/common/CommonSystemPath', data, 'json',successGetTmpDir);
}

function successGetTmpDir(data) {
	notiPath = data;
	if(typeof(tmpPath) == "object"){
		notiPath = data[0].cm_path;
	}
	
	$("#txtsrid").val(SrID);
	$("#txttitle").val(SrTitle);

	clear();
}

function clear(){
	grdDoc.setData([]);
	$("#lblmenu").hide();
	if(strReqCd == "66"){
		
		getDocList("21")
		if(SRStatus == "25"){
			chkMyTest();
		}
	}else{
		grdDoc.config.contextMenu = null;
		getDocList("21,22")
	}
	
}

function chkMyTest(){

	ajaxReturnData = null;
	var data = {
		srId : SrID,
		userId : userId,
		requestType : 'chkMyTest'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/winpop/PopRequestDetailServlet',
			data, 'json');
	if(ajaxReturnData == "OK"){
		$("#cmdResultTest").prop("disabled",false);
		grdDoc.config.contextMenu = grdDocContextMenu;
	}else{
		$("#cmdResultTest").prop("dsabled",true);
		grdDoc.config.contextMenu = null;
	}
	
}


function getDocList(gbnCd){

	ajaxReturnData = null;
	var docSr = {
		srid : SrID,
		gbn : "21,22,23",
		requestType : 'getDocList'
	}

	data = ajaxCallWithJson('/webPage/srcommon/SRRegister', docSr, 'json');
	//#######################################################수정필요
	if (ajaxReturnData !== 'ERR') {
		grdDoc.setData([]);
		
		var docData = [];
		for(var i = 0; i < developerList.length; i++) { //통합테스트 결과서 정보 세팅
			var devCnt = 0;
			for(var j = 0; j < data.length; j++) { //등록된 파일 있으면 해당 데이터로 세팅 후 체크리스트 Y로 변경
				
				//2020.06.25 과거 22로 등록된 현업테스트 결과서 건이 표시가 안돼서 수정
				if(developerList[i].cc_editor == data[j].cc_editor && data[j].cc_gbncd != "23"){
					data[j].testDoc = "Y";
					docData.push(data[j]);
					devCnt++;
				}
			}
			
			if(devCnt == 0) { //등록된 파일 없으면 개발담당자 정보 세팅
				var docObj = {
					gbncd : "통합테스트결과서",
					cm_username : developerList[i].cm_username,
					cc_editor : developerList[i].cc_editor,
					testDoc : "N"
				}
				docData.push(docObj);
			}
		}
		$.each(data, function(i, val) { //현업테스트 결과서 세팅
			if(val.cc_gbncd == "23") {
				val.testDoc = "Y";
				val.cc_chklist = "해당 없음";
				docData.push(val);
			} else if (val.cc_gbncd == "22"){
				var docCk = true;
				$(docData).each(function(i){
					if(this.cc_editor == val.cc_editor ){
						docCk = false;
					}
				});
				
				if(docCk){
					val.testDoc = "Y";
					val.cc_chklist = "해당 없음";
					docData.push(val);
				}
			}
		});
		
		grdDoc.list = docData;
		grdDoc.setData(grdDoc.list);
	
		if(data.length>0) {
			if(!$("#cmdResultTest").prop("disabled")){
				$("#lblmenu").show(); 
			}else{
				$("#lblmenu").hide(); 
			}
		}else{
			$("#lblmenu").hide(); 
		}
	
//		grdDoc.setData(data);
	}
	
}

function getSRInfo(){
	var ajaxReturnData = null;
	var prjData = new Object();
	prjData.deptCd = "ALL";
	prjData.gbnCd = "ALL";
	prjData.strDate = "";
	prjData.endDate = "";
	prjData.selCd = "SEL";
	prjData.selTxt = "";
	prjData.srId = SrID;
	
	prjData.userId 	= userId;
	prjData.reqCd 	= strReqCd;
	
	var prjInfo = new Object();
	prjInfo.prjInfoData =	prjData;
	prjInfo.requestType = 'getSRInfo';
	
	ajaxReturnData = ajaxCallWithJson('/webPage/common/ComPrjInfo', prjInfo, 'json');
	console.log(ajaxReturnData);
	if(ajaxReturnData.length>0){
		SrTitle = ajaxReturnData[0].cc_title;
		SRStatus = ajaxReturnData[0].cc_status;
		GradeCd = ajaxReturnData[0].grade;
	}
	getSRDevInfo();
	getTestUser();
}

function delSelectDoc(data) {
	var ajaxReturnData = null;
	if(data.cc_seqno == null || data.cc_seqno == undefined)  return;
	if(data.cc_editor != userId) {
		dialog.alert("본인이 등록한 단위테스트결과서만 제거할 수 있습니다.");
		return;
	}
	var ajaxData = new Object();
	ajaxData = {
		srid : SrID,
		editor : data.cc_editor,
		seqno : data.cc_seqno,
		gbncd : data.cc_gbncd,
		requestType	: 'deleteMyTest'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', ajaxData, 'json');
	
	if(ajaxReturnData == "ok") {
		dialog.alert("단위테스트결과를 제거했습니다.");
		clear();
	}else {
		dialog.alert("단위테스트결과를 제거에 실패하였습니다.");
	}
}

function downSelectDoc(index,item){
	var fullPath = notiPath+'/'+item.cc_srid+'/'+item.cc_reldoc;
	var fileName = item.name;
	fileDown(fullPath, fileName);
}

function fileClear(){
	$("#doc").val('');

	//window.parent.getPrjList();
	clear();
	
	if(GradeCd == "01" && grdDoc.getList().length > 0){ //일반일 경우에만 개발책임자에게 알림메시지전송

		var tmpData = {
				srid   :   SrID,
			requestType	: 	'sendAlarm'
		}
		
		ajaxAsync('/webPage/sr/SRStatus', tmpData, 'json',successSendAlarm);
	}
}

function successSendAlarm(data){
	if(data != "OK"){
		dialog.alert("알림메세지 전송에 실패하였습니다.");
	}
}

//테스트담당자 아이디 가져오기
function getTestUser() {
	var data = new Object();
	data = {
		srId 		: SrID,
		requestType	: 'getTstUserInfo'
	}
	var returnData = ajaxCallWithJson('/webPage/winpop/PopRequestDetailServlet', data, 'json');
	if(userId == returnData) testUser = true;
}

// 개발담당자리스트 가져오기
function getSRDevInfo() {
	var data = {
		userId	: "",
		strIsrId	: SrID,
		requestType	: 'getSRDevInfo'
	}
	var returnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
	console.log(returnData);
	developerList = returnData;
	$.each(returnData, function(i, val) {
		if(val.cc_editor == userId) developer = true;
	})
}
