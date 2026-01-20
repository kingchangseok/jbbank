/**
 * 개발영역연결등록 팝업 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2019-06-00
 */

	
var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;

var strUserId = "";
var strSyscd  = "";
var strTmpDir = "";

var grdProgList 	= new ax5.ui.grid(); //프로그램목록그리드

var selOptions		= [];
var cboSystemData 	= [];	//시스템 데이터
var cboDirData		= [];	//기준디렉토리 데이터 (cboDir_dp)
var selHomeDirData	= [];	//홈디렉토리 데이터 (dirCbo_dp)
var cboSvrData		= [];	//서버 데이터
var cboJawonData	= [];	//프로그램종류 데이터
var cboJobData		= [];	//업무 데이터
var grdProgListData = []; 	//프로그램목록그리드 데이터

var treeObj			= [];
var treeObjData		= []; 	//디렉토리트리 데이터

var selectedIndex 	= 0; 	//select 선택 index
var selectedItem 	= [];	//select 선택 item
var fileList 		= [];

var tmpInfo = new Object();
var tmpInfoData = new Object();

var moduleModal 		= new ax5.ui.modal();
var tmpObj1 = {};
var RelatAry = [];
var SecuYn = null;

var devGridColumn = [
	{key: "filename", 		label: "파일명",		width: '19%', 	align: "left"},
    {key: "errmsg", 		label: "등록결과",	  	width: '14%',	align: "left"},
    {key: "cm_dirpath", 	label: "디렉토리",   	width: '25%',	align: "left"},
    {key: "rsrccdname", 	label: "프로그램종류", 	width: '13%',	align: "left"},
    {key: "jobname", 		label: "업무",   		width: '13%',	align: "left"},
    {key: "story", 			label: "프로그램설명", 	width: '15%',	align: "left",	editor: {type: "text"}}
];

var treeSetting = {
	check: {
		enable: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onExpand: myOnExpand,
		onRightClick: OnRightClick
	}
};

grdProgList.setConfig({
    target: $('[data-ax5grid="grdProgList"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    multipleSelect: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	},
    	trStyleClass: function () {
     		if(this.item.error == '1'){
     			return "fontStyle-cncl";
     		}else {
     		}
     	}
    },
    columns: devGridColumn
});

$(document).ready(function(){
	strUserId = $('#UserId').val();
	strSyscd = $('#SysCd').val();
	
	var contentHistory = '';
	contentHistory = "프로그램등록 <strong> &gt; "+ "개발영역연결등록</strong>";
	$('#devBody').contents().find('#history_wrap').html(contentHistory);
	
	//디렉토리조회 클릭
	$("#btnQry").bind('click', function() {
		btnQry_Click();
	});
	
	//등록 클릭
	$("#btnRegist").bind('click', function() {
		btnRegist_Click();
	});
	
	//초기화 클릭
	$("#btnInit").bind('click', function() {
		btnInit_Click();
	});
	
	//엑셀저장 클릭
	$("#btnExcel").bind('click', function() {
		btnExcel_Click();
	});
	
	//시스템
	$("#cboSystem").bind('change', function() {
		cboSystem_Change();
	});
	
	//프로그램종류
	$("#cboJawon").bind('change', function() {
		cboJawon_Change();
	});
	
	//경로입력
	$('#txtDir').bind('keypress', function(event){
		if(event.keyCode==13) {
			btnQry_Click();
		}
	});
	
	getTempDir();
	isAdmin();
});

function isAdmin() {
	tmpInfo = new Object();
	tmpInfo = {
		UserID		: strUserId ,
		requestType	: 'isAdmin',
	}
	ajaxAsync('/webPage/common/UserInfoServlet', tmpInfo, 'json', successIsAdmin);
}

function successIsAdmin(data) {
	if (data) SecuYn = "N";
	else SecuYn = "Y";
	
	getSysInfo();
}

//SystemPath.getTmpDir("99");
function getTempDir() {
	tmpInfo = new Object();
	tmpInfo = {
		requestType	: 'getTmpDir',
		pCode		: '99' 
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfo, 'json', successSystemPath);
}


function successSystemPath(data) {
	if (data != undefined && data != null && typeof(data) == "string") {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	strTmpDir = data;
	if(typeof(strTmpDir) == "object"){
		strTmpDir = data[0].cm_path;
	}
	strTmpDir = strTmpDir + "/";
}

//SysInfo.getSysInfo(strUserId,SecuYn,"SEL",SecuYn,"OPEN");
function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		requestType	: 'getSysInfo'
	}
	tmpInfoData.UserId = strUserId;
	tmpInfoData.SecuYn = SecuYn;
	tmpInfoData.SelMsg = "SEL";
	tmpInfoData.CloseYn = SecuYn;
	tmpInfoData.ReqCd = "OPEN";		
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSysInfo);
}

function successSysInfo(data) {
	
	cboSystemData = data;
	
	cboSystemData = cboSystemData.filter(function(data) {
		if(data.cm_sysinfo.substr(6,1) == "1") return true; //SYSINFO [7] 개발영역등록허용
		else return false;
	});
		
   	$('[data-ax5select="cboSystem"]').ax5select({
        options: injectCboDataToArr(cboSystemData, 'cm_syscd' , 'cm_sysmsg')
   	});
	
	if(strSyscd != "" && strSyscd != null) {
		for(var i=0; i<cboSystemData.length; i++) {
			if(cboSystemData[i].value == strSyscd) {
				$('[data-ax5select="cboSystem"]').ax5select('setValue',cboSystemData[i].value,true); //value값으로
				break;
			}
		}
	}
	
	if(getSelectedIndex('cboSystem') > -1) {
		cboSystem_Change();
	}
}

function cboJawon_Change() {
	selectedIndex = getSelectedIndex('cboJawon');
	selectedItem = getSelectedVal('cboJawon');
	
	$('#txtExeName').val("");
	
	if(selectedIndex < 1) return;
	
//	if(getSelectedVal('cboJawon').cm_info.substr(40,1) == "1") {
//		$('#txtExeName').val("");
//	}else{
		$('#txtExeName').val(selectedItem.cm_exename);
//	}
}

function cboSystem_Change() {
	screenInit();
	
	selectedIndex = getSelectedIndex('cboSystem');
	selectedItem = getSelectedVal('cboSystem');
	
	if(selectedIndex < 0) return;
	
	if(selectedIndex >= 0) {
		tmpInfoData = new Object();
		tmpInfoData = {
			UserID		: strUserId,
			SysCd		: selectedItem.value,
			SecuYn		: "Y",
			CloseYn		: "N",
			SelMsg		: "SEL",
			sortCd		: "NAME",
			requestType	: 'getJobInfo'
		}
		
		ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successJobInfo);
	}
	tmpInfo = new Object();
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserID		: strUserId,
		SysCd		: selectedItem.value,
		SecuYn		: "Y",
		SelMsg		: "",
		requestType	: 'getsvrInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSvrInfo);
}

function screenInit() {
	$('[data-ax5select="cboJawon"]').ax5select({
        options: []
	});
	
	$('[data-ax5select="cboJob"]').ax5select({
        options: []
	});
	
	$('[data-ax5select="cboDir"]').ax5select({
        options: []
	});
	
	$('[data-ax5select="cboSvr"]').ax5select({
        options: []
	});
	
	selHomeDirData = [];
	grdProgList.setData([]);
	$('#txtDir').val("");
}

function successJobInfo(data) {

	if (data != undefined && data != null && typeof(data) == "string") {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	selOptions = data;
	cboJobData = [];
	
	$.each(selOptions,function(key,value) {
		cboJobData.push({value: value.cm_jobcd, text: value.cm_jobname});
	});
	
	$('[data-ax5select="cboJob"]').ax5select({
        options: cboJobData
	});
	
	if(cboJobData.length > 0) {
		if ( cboJobData.length == 2 ){
			$('[data-ax5select="cboJob"]').ax5select('setValue',cboJobData[1].value,true);
		}
		
		tmpInfoData = new Object();
		tmpInfoData = {
			SysCd		: getSelectedVal('cboSystem').value,
			SelMsg		: "SEL",
			requestType	: 'getRsrcOpen'
		}
		
		ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successJawon);
	}
}

function successJawon(data) {

	if (data != undefined && data != null && typeof(data) == "string") {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	cboJawonData = data;
	
	cboJawonData = cboJawonData.filter(function(data) {
		if(data.cm_micode == "0000") return true;
		else {
			if(data.cm_info.substr(44,1) == "1") return false;
			else return true;
		}
	});
	
	$('[data-ax5select="cboJawon"]').ax5select({
        options: injectCboDataToArr(cboJawonData, 'cm_micode' , 'cm_codename')
   	});
	
	$('[data-ax5select="cboJawon"]').ax5select('setValue',cboJawonData[0].value,true); //value값으로
}

function successSvrInfo(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	cboSvrData = data;
	cboSvrData = cboSvrData.filter(function(data) {
		if(data.cm_svrname != null && (data.cm_svrip == null || data.cm_svrip == "")) return true;
		else {
			if(data.cm_svrinfo.substr(1,1) == '0' && data.cm_svrcd == "01") { 
				return true;
			}else {
				return false;
			}
		}
	});
	
   	$('[data-ax5select="cboSvr"]').ax5select({
        options: injectCboDataToArr(cboSvrData, 'cm_svrip' , 'cm_svrname')
   	});
	
   	$.fn.zTree.init($("#treeDir"), treeSetting, []); //초기화
	
	if(cboSvrData.length > 0) {
		$('[data-ax5select="cboSvr"]').ax5select('setValue',cboSvrData[0].value,true); //value값으로
		cboSvr_click();
	}
}

function cboSvr_click() {
/*	$('[data-ax5select="cboDir"]').ax5select({
        options: []
	});
*/	
	selectedIndex = getSelectedIndex('cboSvr');
	selectedItem = getSelectedVal('cboSvr');
	
	if(selectedIndex < 0) return;
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		svrCd	: getSelectedVal('cboSvr').cm_svrcd,
		svrInfo	: getSelectedVal('cboSvr').cm_svruse,
		svrHome	: getSelectedVal('cboSvr').cm_volpath,
		SysCd	: getSelectedVal('cboSystem').value,
		seqNo	: getSelectedVal('cboSvr').cm_seqno,
		requestType	: 'getHomeDirList'
	}
	
	ajaxAsync('/webPage/ecmd/svrOpenServlet', tmpInfoData, 'json', successHomeDir);
}

function successHomeDir(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
/*	selOptions = data;
	cboDirData = [];
	selHomeDirData = [];
	console.log("successHomeDir!");
	$.each(selOptions,function(key,value) {
		cboDirData.push({value: value.cm_volpath, text: value.cm_volpath});
		selHomeDirData.push({value: value.cm_volpath, text: value.cm_volpath});
	});
	
	$('[data-ax5select="cboDir"]').ax5select({
        options: cboDirData
	});*/
	
	//$('[data-ax5select="cboDir"]').ax5select('setValue',cboDirData[-1].value,true); //value값으로
	//cboDir.text = "기준디렉토리를 선택 또는 입력해주세요.";
	
	$('#txtDirBase').val(data[0].cm_volpath);
}

function btnQry_Click() {
	var i = 0;
	var minLen = 30;
	var minLenStr = "";
	//var baseDir = $('[data-ax5select="cboDir"]').ax5select("getValue")[0].value;
	var baseDir = $('#txtDirBase').val();
	var baseDirSub = $('#txtDir').val();
	
	grdProgList.setData([]);
	selectedIndex = getSelectedIndex('cboSvr');
	selectedItem = getSelectedVal('cboSvr');
	
	console.log('baseDir, volpath ==>', baseDir, selectedItem.cm_volpath);
	if (baseDir.length == 0){
		dialog.alert("기준디렉토리는  ["+selectedItem.cm_volpath+"] 포함하는 디렉토리를 입력하여 주십시오.");
		return;
	} else if (baseDir.length < selectedItem.cm_volpath.length){
		//기준디렉토리에서 홈디렉토리 길이 만큼 짤라서 홈디렉토리랑 비교하는데 틀릴때
		dialog.alert("기준디렉토리는  ["+selectedItem.cm_volpath+"] 포함하는 디렉토리만 가능합니다.");
		return;
	} else if (baseDir.toUpperCase().substr(0,selectedItem.cm_volpath.length) != selectedItem.cm_volpath.toUpperCase()){
		//기준디렉토리에서 홈디렉토리 길이 만큼 짤라서 홈디렉토리랑 비교하는데 틀릴때
		dialog.alert("기준디렉토리는  ["+selectedItem.cm_volpath+"] 포함하는 디렉토리만 가능합니다.");
		return;
	}
	 
	if(selectedIndex < 0) return;
	
	//기준디렉토리 뒤에 슬래쉬 o + 입력디렉토리 앞에 슬래쉬 o > 슬래쉬빼주기
	//기준디렉토리 뒤에 슬래쉬 o + 입력디렉토리 앞에 슬래쉬 x
	//기준디렉토리 뒤에 슬래쉬 x + 입력디렉토리 앞에 슬래쉬 o
	//기준디렉토리 뒤에 슬래쉬 x + 입력디렉토리 앞에 슬래쉬 x > 슬래쉬 붙여주기
	
	//입력디렉토리 앞에 슬래쉬 o
	if($('#txtDir').val() != "") {
		if($('#txtDir').val().substr(0,1) == "/" || $('#txtDir').val().substr(0,1) == "\\") {
			if(baseDir.substr(baseDir.length-1,1) == "/" || baseDir.substr(baseDir.length-1,1) == "\\") {
				$('#txtDir').val($('#txtDir').val().substr(1,$('#txtDir').val().length-1))
			}
		}
		
		//입력디렉토리 앞에 슬래쉬 x
		if($('#txtDir').val().substr(0,1) != "/" && $('#txtDir').val().substr(0,1) != "\\") {
			if(baseDir.substr(baseDir.length-1,1) != "/" || baseDir.substr(baseDir.length-1,1) != "\\") {
				$('#txtDir').val("/" + $('#txtDir').val())
			}
		}
	}
	
	baseDir = baseDir.split("//").join("/");
	baseDir = baseDir.split("\\\\").join("\\");
	
	if ( baseDir.lastIndexOf("\\")==baseDir.length-1 || baseDir.lastIndexOf("/")==baseDir.length-1) {
		baseDir = baseDir.substr(0,baseDir.length-1);
	}
//	if ( baseDir.indexOf("\\") > -1 ){
//		if(baseDir.split("\\").length < 2){
//			dialog.alert("최상위 디렉토리로 조회가 불가능합니다. 기준디렉토리에 서브디렉토리명까지 입력 후 조회하여 주십시오.");
//			return;
//		}
//	} else if(baseDir.split("/").length < 3) {
//		dialog.alert("최상위 디렉토리로 조회가 불가능합니다. 기준디렉토리에 서브디렉토리명까지 입력 후 조회하여 주십시오.");
//		return;
//	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserID : strUserId,
		SysCd : getSelectedVal('cboSystem').value,
		SvrIp : selectedItem.cm_svrip,
		SvrPort : selectedItem.cm_portno,
		BaseDir : baseDir + $('#txtDir').val(),
		AgentDir : selectedItem.cm_dir,
		SysOs : selectedItem.cm_sysos,
		HomeDir : baseDir + $('#txtDir').val(),
		svrName : selectedItem.cm_svrname,
		buffSize : selectedItem.cm_buffsize,
		requestType	: 'getSvrDir_HTML5'
	}
	console.log("getSvrDir Call:",tmpInfoData);
	ajaxAsync('/webPage/ecmd/svrOpenServlet', tmpInfoData, 'json', successSvrDir);
}

/* 디렉토리 트리구조 셋팅 */
function successSvrDir(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	treeObjData = data;
	console.log("successSvrDir:",data);
	if(treeObjData.constructor == String) {
		dialog.alert(treeObjData.substr(5));
	}else {
		ztree = $.fn.zTree.init($('#treeDir'), treeSetting, treeObjData); //초기화
		treeObj = $.fn.zTree.getZTreeObj("treeDir");
	}
}

/* 트리구조에서 folder 오픈 */
function myOnExpand(event, treeId, treeNode) {
	//root node 만 비동기 방식으로 뽑아오는 조건
	console.log("### myOnExpand  treeNode: ", treeNode);
	if(treeNode.children != undefined || treeNode.check_Child_State == -1){
		return false;
	}
	
	/*
	//로딩중 icon class 추가
	$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){

		var ajaxReturnData = null;
		var baseDir = "";
		
		if(treeNode.pid == null || treeNode.children != undefined){
			baseDir = $('[data-ax5select="cboDir"]').ax5select("getValue")[0].value;
		}else {
			//baseDir = $('[data-ax5select="cboDir"]').ax5select("getValue")[0].value + "/" + treeNode.name;
			baseDir = treeNode.cm_fullpath;
		}
		
		if($('#txtDir').val() != "") {
			if($('#txtDir').val().substr(0,1) == "/" || $('#txtDir').val().substr(0,1) == "\\") {
				if(baseDir.substr(baseDir.length-1,1) == "/" || baseDir.substr(baseDir.length-1,1) == "\\") {
					$('#txtDir').val($('#txtDir').val().substr(1,$('#txtDir').val().length-1))
				}
			}
			
			//입력디렉토리 앞에 슬래쉬 x
			if($('#txtDir').val().substr(0,1) != "/" && $('#txtDir').val().substr(0,1) != "\\") {
				if(baseDir.substr(baseDir.length-1,1) != "/" || baseDir.substr(baseDir.length-1,1) != "\\") {
					$('#txtDir').val("/" + $('#txtDir').val())
				}
			}
		}
		
		tmpInfo = new Object();
		tmpInfo.userId = strUserId;
		tmpInfo.SysCd = getSelectedVal('cboSystem').value;
		tmpInfo.SvrIp = selectedItem.cm_svrip;
		tmpInfo.SvrPort = selectedItem.cm_portno;
		tmpInfo.BuffSize = selectedItem.cm_buffsize;
		tmpInfo.BaseDir = baseDir;
		tmpInfo.AgentDir = selectedItem.cm_dir;
		tmpInfo.SysOs = selectedItem.cm_sysos;
		//tmpInfo.HomeDir = selectedItem.cm_volpath;
		tmpInfo.HomeDir = baseDir;
		tmpInfo.svrName = selectedItem.cm_svrname;
		tmpInfo.parent = treeNode.id;
		tmpInfo.subNode = treeNode.name;

		tmpInfoData = new Object();
		tmpInfoData = {
			etcData	: tmpInfo,
			requestType	: 'getChildSvrDir_HTML5'
		}
		console.log("### myOnExpand  GETCHILDSVRDIR tmpInfo:",tmpInfo);
		ajaxReturnData = null;
		ajaxReturnData = ajaxCallWithJson('/webPage/winpop/PopDevRepositoryServlet', tmpInfoData, 'json');
		if (ajaxReturnData != undefined && ajaxReturnData != null) {
			if (ajaxReturnData.indexOf('ERROR')>-1) {
				dialog.alert(ajaxReturnData);
				return;
			}
		}
		var obj = null;
		obj = ajaxReturnData;
		
		for(var i in ajaxReturnData){
			if(obj[i].cm_dirpath =='' ){
				delete obj[i]
				continue;
			}
			obj[i].name = obj[i].cm_dirpath;
			delete obj[i].cm_dirpath;
			obj[i].isParent = true;
		}
		
		
		if(obj != null) {
			obj = JSON.stringify(obj).replace(/null,/gi,'');
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			ztree.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
	*/
};

/* 트리 노드 마우스 우클릭 이벤트 */
function OnRightClick(event, treeId, treeNode) { 
	var treeId = treeNode.id;
	
	if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
		treeObj.cancelSelectedNode();
		
		showRMenu("root", event.clientX, event.clientY);
		selectedNode = treeNode;
	} else if (treeNode && !treeNode.noR) {
		treeObj.selectNode(treeNode);
		showRMenu("node", event.clientX, event.clientY);
		selectedNode = treeNode;
	}
} 

/* context menu 설정 */
function showRMenu(type, x, y) { 
	$("#rMenu ul").show(); 
	if (type=="root") { 
		$("#contextmenu1").hide();
		$("#contextmenu2").hide();
	} else { 
		$("#contextmenu1").show();
		$("#contextmenu2").show();
 	} 
  
     y += document.body.scrollTop; 
     x += document.body.scrollLeft; 
     $("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"});
  
     $("body").bind("mousedown", onBodyMouseDown); 
} 

/* 노드 이외 영역 클릭시 context menu 숨기기 */
function onBodyMouseDown(event){ 
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		$("#rMenu").css({"visibility" : "hidden"});
	}
} 

/* context menu 숨기기 */
function hideRMenu() { 
	if ($("#rMenu")) $("#rMenu").css({"visibility": "hidden"}); 
	$("body").unbind("mousedown", onBodyMouseDown); 
}

/* 파일추출 클릭 이벤트 */
function contextmenu_click(gbn) {
	hideRMenu();
	
	if(treeObj.getSelectedNodes()[0].id == null) return;
	
	grdProgList.setData([]);
	
	var fullpath = treeObj.getSelectedNodes()[0].cm_fullpath;
	if(fullpath != "" && fullpath != null) {
		if(fullpath.indexOf(":")<0 && fullpath.substr(0,1) != "/") {
			fullpath = "/" + fullpath;
		}
	}
	
	var tmpStr  = "";
	var tmpExe1 = "";
	var tmpExe2 = "";
	var tmpPrgName = "";
	
	tmpStr = gbn;
	//console.log("tmpStr: " + tmpStr);
	
	if($('#txtExe').val($('#txtExe').val().trim()).length > 0) tmpExe1 = $('#txtExe').val().trim();
	else tmpExe1 = "";
	
	if($('#txtNoExe').val($('#txtNoExe').val().trim()).length > 0) tmpExe2 = $('#txtNoExe').val().trim();
	else tmpExe2 = "";
	
	if($('#txtPrgName').val($('#txtPrgName').val().trim()).length > 0) tmpPrgName = $('#txtPrgName').val().trim();
	else tmpPrgName = "";
	
	selectedItem = getSelectedVal('cboSvr');
	
	tmpInfo = new Object();
	tmpInfo.UserID = strUserId;
	tmpInfo.SysCd = getSelectedVal('cboSystem').value;
	tmpInfo.SvrIp = selectedItem.cm_svrip;
	tmpInfo.SvrPort = selectedItem.cm_portno;
	tmpInfo.HomeDir = fullpath;
	tmpInfo.BaseDir = fullpath;
	tmpInfo.SvrCd = selectedItem.cm_svrcd;
	tmpInfo.GbnCd = tmpStr;

	if(tmpExe1.length > 0){
		if(tmpExe1.indexOf(".") < 0){
			dialog.alert('추출대상 확장자에 "." 을 입력하여 주십시오.',function(){});
			return;
		}
	}
	if(tmpExe2.length > 0){
		if(tmpExe2.indexOf(".") < 0){
			dialog.alert('추출제외 확장자에 "." 을 입력하여 주십시오.',function(){});
			return;
		}
	}
	tmpInfo.exeName1 = tmpExe1;
	tmpInfo.exeName2 = tmpExe2;
	tmpInfo.exeName3 = tmpPrgName;
	tmpInfo.SysInfo = selectedItem.cm_sysinfo;
	tmpInfo.AgentDir = selectedItem.cm_dir;
	tmpInfo.SysOs = selectedItem.cm_sysos;
	tmpInfo.buffSize = selectedItem.cm_buffsize;

	tmpInfoData = new Object();
	tmpInfoData = {
		etcData		: tmpInfo,
		requestType	: 'getFileList_thread'
	}
	
	$('[data-ax5grid="grdProgList"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/svrOpenServlet', tmpInfoData, 'json', successGetFileList);
	
	fullpath = "";
}

function successGetFileList(data) {
	$(".loding-div").remove();
	
	var findSw = false;
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	grdProgListData = data;
	console.log('###  successGetFileList:',data);
	
	if(grdProgListData.length > 0) {
		if(grdProgListData[0].error == "W") {
			dialog.alert(grdProgListData[0].cm_dirpath,function(){});
			grdProgListData.splice(0,1);
		}
		
		var j = 0;
		for(var i=0; i<grdProgListData.length; i++) {
			if(grdProgListData[i].enable1 == "1") {
				if(grdProgListData[i].rsrccd != null && grdProgListData[i].rsrccd != "") {
					findSw = false;
					for(j=0; j<cboJawonData.length; j++) {
						if(cboJawonData[j].cm_micode == grdProgListData[i].rsrccd) {
							grdProgListData[i].rsrccdname = cboJawonData[j].cm_codename;
							grdProgListData[i].cm_info = cboJawonData[j].cm_info;
							findSw = true;
							break;
						}
					}
					if(!findSw) {
						grdProgListData[i].errmsg = "프로그램유형없음";
						grdProgListData[i].error = "1";
						grdProgListData[i].enable1 = "0";
					}
				}
				if(findSw) {
					findSw = false;
					if(grdProgListData[i].jobcd != null && grdProgListData[i].jobcd != "") {
						findSw = false;
						for(j=0; j<cboJobData.length; j++) {
							if(cboJawonData[j].cm_jobcd == grdProgListData[i].jobcd) {
								grdProgListData[i].jobname = cboJawonData[j].cm_jobname;
								findSw = true;
								break;
							}
						}
						if(!findSw) {
							grdProgListData[i].errmsg = "업무코드없음";
							grdProgListData[i].error = "1";
							grdProgListData[i].enable1 = "0";
						}
					}
				}
			}
		}
	}
	
	grdProgList.setData(grdProgListData);
	//grdProgList.setColumnSort({filename:{seq:0, orderBy:"asc"}});
}

function btnRegist_Click() {
	var checkedGridItem = grdProgList.getList("selected");
	var tmpHomedir 		= $('#txtDirBase').val().trim();
	var i 				= 0;
	var tmpExe 			= "";
	var rsrcExe 		= "";
	var errSw 			= false;
	
	for(i=0; i<checkedGridItem.length; i++) {
		checkedGridItem[i].errmsg = "";
	}
	if(checkedGridItem.length == 0) {
		dialog.alert('등록할 파일을 선택하여 주십시오.',function(){});
		$('#cboSystem').focus();
		return;
	}
	if(getSelectedIndex('cboJawon') < 1) {
		dialog.alert('프로그램종류를 선택하여 주십시오.',function(){});
		$('#cboJawon').focus();
		return;
	}
	if(getSelectedIndex('cboJob') < 1) {
		dialog.alert('업무를 선택하여 주십시오.',function(){});
		$('#cboJob').focus();
		return;
	}
	$('#txtStory').val($('#txtStory').val().trim());
	
	if (getSelectedVal('cboJawon').cm_info.substr(26,1) == "1" || getSelectedVal('cboJawon').cm_info.substr(32,1) == "1") {
		dialog.alert("개발툴연계등록에서 신규등록하여 주십시오.");
		return;		
	}
	
	if(getSelectedIndex('cboSystem') < 0) {
		dialog.alert('시스템을 선택하여 주십시오.',function(){});
		$('#cboSystem').focus();
		return;
	}
		
	errSw = false;
	if(checkedGridItem.length == 0) return;
	
	//console.log("checked: " , checkedGridItem[0].__index);
	
	for(i=0; i<checkedGridItem.length; i++) {
		tmpExe = "";
		
		if(checkedGridItem[i].errmsg != null && checkedGridItem[i].errmsg != "") {
			checkedGridItem[i].errmsg = "";
			checkedGridItem[i].error = "";
		}
		
		if(getSelectedVal('cboSvr').cm_sysos == "03") {
			var tmp = checkedGridItem[i].cm_dirpath;			// /IBOSv2.0/...
			var home = getSelectedVal('cboJawon').cm_volpath;	// D:\IBOSv2.0
			var z = 0;
			
			tmp = home.substr(0,2) + tmp;						// D:/IBOSv2.0/
			
			for(z=0; z<tmp.length; z++) {
				tmp = tmp.replace("/", "\\");
			}
			
			grdProgList.setValue(checkedGridItem[i].__index, "cm_dirpath", tmp);	//	D:\\IBOSv2.0\\
		}
		
		if(checkedGridItem[i].cm_dirpath.length > 499) {
			grdProgList.setValue(checkedGridItem[i].__index, "error", "1");
			grdProgList.setValue(checkedGridItem[i].__index, "errmsg", "프로그램경로가 너무 큼(최대 500)");
			errSw = true;
		}else if(checkedGridItem[i].cm_dirpath.substr(0, tmpHomedir.length) != tmpHomedir) {
			console.log(checkedGridItem[i].cm_dirpath.substr(0, tmpHomedir.length));
			console.log(tmpHomedir.length);
			console.log(checkedGridItem[i].cm_dirpath);
			//windows 경로 \로 통일
			if(getSelectedVal('cboSvr').cm_sysos == "03") {
				//g: 발생한 모든 문자열 패턴 replace, i: 대소문자 구분X
				console.log("1="+checkedGridItem[i].cm_dirpath.substr(0, tmpHomedir.length).replace(/\\/gi,'/'));
				console.log("2="+tmpHomedir.replace(/\\/gi,'/'));
				if(checkedGridItem[i].cm_dirpath.substr(0, tmpHomedir.length).replace(/\\/gi,'/') != tmpHomedir.replace(/\\/gi,'/')) {
					grdProgList.setValue(checkedGridItem[i].__index, "error", "1");
					grdProgList.setValue(checkedGridItem[i].__index, "errmsg", "홈디렉토리불일치");
					errSw = true;
				}
			}else {
				grdProgList.setValue(checkedGridItem[i].__index, "error", "1");
				grdProgList.setValue(checkedGridItem[i].__index, "errmsg", "홈디렉토리불일치");
				errSw = true;
			}
		}//else {
		
		//if(!errSw) {
			if (checkedGridItem[i].cm_dirpath.substr(0,getSelectedVal('cboJawon').cm_volpath.length) != getSelectedVal('cboJawon').cm_volpath) {
				checkedGridItem[i].errmsg = "홈디렉토리불일치";
				errSw = true;
			} else {
				if(checkedGridItem[i].filename.lastIndexOf(",") >= 0) {
					grdProgList.setValue(checkedGridItem[i].__index, "error", "1");
					grdProgList.setValue(checkedGridItem[i].__index, "errmsg", "프로그램명컴마제외");
					errSw = true;
				}else {
					//if(getSelectedVal('cboJawon').cm_info.substr(48,1) == "0") { //확장자체크안함
						if($('#txtExeName').val() != "" && $('#txtExeName').val() != null) {
							if(checkedGridItem[i].filename.lastIndexOf(".") > 0) {
								tmpExe = checkedGridItem[i].filename.substr(checkedGridItem[i].filename.lastIndexOf("."));
								tmpExe = tmpExe + ",";
								tmpExe = tmpExe.toUpperCase();
								rsrcExe = $('#txtExeName').val().toUpperCase();
								
								if(rsrcExe.indexOf(tmpExe) < 0) {
									grdProgList.setValue(checkedGridItem[i].__index, "error", "1");
									grdProgList.setValue(checkedGridItem[i].__index, "errmsg", "확장자불일치");
									errSw = true;
								}
							}else {
								grdProgList.setValue(checkedGridItem[i].__index, "error", "1");
								grdProgList.setValue(checkedGridItem[i].__index, "errmsg", "확장자불일치");
								errSw = true;
							}
						}
					//}
				}
			}
		//}
		
		if(!errSw) {
			if(checkedGridItem[i].story == null || checkedGridItem[i].story == "") {
				if($('#txtStory').val().length == 0) {
					dialog.alert('프로그램설명을 입력하여 주십시오.',function(){});
					return;
				}else {
					checkedGridItem[i].story = $('#txtStory').val(); 
				}
			}
		}
	}
	
	grdProgList.selectAll({selected: false});
	
	if(errSw) {
		dialog.alert('등록대상 파일 중 요건에 맞지않는 파일이 있습니다. [등록결과] 을 확인 후 다시 등록하시기 바랍니다.',function(){});
		return;
	}
	
	etcData = new Object();
	etcData.userid = strUserId;
	etcData.sysgb = getSelectedVal('cboSystem').cm_sysgb;
	etcData.syscd = getSelectedVal('cboSystem').cm_syscd;
	etcData.story = $('#txtStory').val(); 
	etcData.rsrccd = getSelectedVal('cboJawon').cm_micode;
	etcData.cm_info = getSelectedVal('cboJawon').cm_info;
	etcData.jobcd = getSelectedVal('cboJob').value;
	etcData.sysos = getSelectedVal('cboSvr').cm_sysos;
	
	tmpInfoData = new Object();
	tmpInfoData = {
		etcData		: etcData,
		fileList	: checkedGridItem,
		requestType	: 'cmr0020_Insert_thread'
	}
	
	tmpInfoData.requestType = 'cmr0020_Insert_thread';
	ajaxAsync('/webPage/ecmd/svrOpenServlet', tmpInfoData, 'json', successRegistProg);
	console.log('### PrpDevRepository   etcData:',etcData);
	console.log('### PrpDevRepository   checkedGridItem:',checkedGridItem);
}

function successRegistProg(data) {
	var tmpArray = null;
	var tmpObj = new Object();
	var j = 0;
	var findSw = false;
	var okSw = false;
	var itemId = "";
	
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	tmpArray = data;
	console.log('###    successRegistProg   data:',data);
	
	for(var i=0; i<tmpArray.length; i++) {
		for(j=0; j<grdProgListData.length; j++) {
			if(tmpArray[i].cm_dirpath == grdProgListData[j].cm_dirpath &&
					tmpArray[i].filename == grdProgListData[j].filename) {
				console.log(tmpArray[i].error + " / " + i +" / " + j);
				if(tmpArray[i].error == "1") {
					findSw = true;
					grdProgList.setValue(grdProgListData[j].__index, "errmsg", tmpArray[i].errmsg);
					grdProgList.setValue(grdProgListData[j].__index, "error", tmpArray[i].error);
					grdProgList.select(j, {selected: false});
				}else {
					//row 삭제이후 해당 목록 data 넣고 다시 grid에 넣기
					grdProgList.deleteRow(j);
					grdProgListData = clone(grdProgList.list);
					grdProgList.setData(grdProgListData);
					okSw = true;
					break;
				}
			}
		}
	} 
	
	// getSelectedVal('cboJawon').modsw 값이 무조건 N이기 때문에 팝업창 사용안함
	if(getSelectedVal('cboJawon').cm_info.substr(8,1) == "1" &&
			okSw && getSelectedVal('cboJawon').modsw == "Y") {
		/*tmpObj1 = {};
		tmpObj1.syscd = getSelectedVal('cboSystem').cm_syscd;
		tmpObj1.userid = userId;
		tmpObj1.sysmsg = getSelectedVal('cboSystem').cm_sysmsg;
		tmpObj1.jawon = getSelectedVal('cboJawon').cm_codename;
		tmpObj1.cm_info = getSelectedVal('cboJawon').cm_info;
		
		var tmpObj = {};
		for (i=0;tmpArray.length>i;i++) {
			if (tmpArray[i].error != "1") {
				tmpObj = {};
				tmpObj = tmpArray[i];
				//Alert.show("$$$$$$$$$$   : "+tmpObj.itemid);
				tmpObj.ID = i + 1 ;
				tmpObj.cr_lstver = "0";
				tmpObj.checked = "true";
				tmpObj.jawon = getSelectedVal('cboJawon').cm_codename;
				RelatAry.push(tmpObj);
				tmpObj = null;
			}
		}*/
		
		//실행모듈 연결정보 팝업 svrOpen_Relat
		/*setTimeout(function() {
			moduleModal.open({
				width: 1000,
				height: 750,
				iframe: {
					method: "get",
					url: "../modal/moduleinfo/ModuleInfoModal.jsp",
					param: "callBack=moduleInfoModalCallBack"
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
		}, 200);*/
	}

	if(findSw) {
		dialog.alert('등록 중 오류가 발생한 건이 있으니 확인하시기 바랍니다.',function(){});
	}else {
		dialog.alert('등록처리에 성공하였습니다.',function(){});
	}
}

var moduleInfoModalCallBack = function() {
	moduleModal.close();
}

function btnInit_Click() {
	cboSystem_Change();
	$('#txtExeName').val("");
	$('#txtExe').val("");
	$('#txtNoExe').val("");
	$('#txtStory').val("");
}

function btnExcel_Click() {
	grdProgList.exportExcel("파일추출목록.xls");
}