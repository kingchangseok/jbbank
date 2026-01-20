var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;

var grdProgList = new ax5.ui.grid();   //프로그램그리드
var progModal 	= new ax5.ui.modal();
var userModal 	= new ax5.ui.modal();
var autoModal	= new ax5.ui.modal();

var selectedIndex;		//select 선택 index
var selectedItem;		//select 선택 item
var gridSelectedIndex;  //그리드 선택 index
var selectedGridItem;	//그리드 선택 item

var cboSystemData 	= []; //시스템 데이터
var cboDeptData		= []; //관리팀 데이터
var cboJobData		= []; //업무 데이터
var cboDirData		= []; //프로그램경로 데이터
var cboRsrcCdData	= []; //프로그램구분 데이터
var cboRsrcGubunData= []; //프로그램언어 데이터
var cboCompileData	= []; //컴파일모드 데이터
var cboMakeData		= []; //메이크모드 데이터
var grdProgListData = []; //프로그램그리드 데이터

var modalObj 	= [];
var popSelItem  = [];
var tmpInfoData = new Object();

var selSw 	= false;
var strInfo = "";
var strJob  = "";
var id 		= "";
var selSys	= '';
var selJob  = '';
var selSysInfo = '';

if(adminYN) var SecuYn = "N";
else SecuYn = "Y";

$('[data-ax5select="cboCompile"]').ax5select({options: []});
$('[data-ax5select="cboMake"]').ax5select({options: []});
$('[data-ax5select="cboSystem"]').ax5select({options: []});
$('[data-ax5select="cboRsrcGubun"]').ax5select({options: []});
$('input:radio[name=rdoGbn]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

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
            grdProgList_Click();
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
            {type: 1, label: "실행모듈연결정보"}
        ],
        popupFilter: function (item, param) {
        	grdProgList.clearSelect();
        	grdProgList.select(Number(param.dindex));

        	var selIn = grdProgList.selectedDataIndexs;
        	if(selIn.length === 0) return;
        	 
         	if (param.item == undefined) return false;
         	if (param.dindex < 0) return false;
         	
         	var info = param.item.cm_info;
         	
         	if(info.substr(8,1) == "1") {
         		return true;
         	} else {
         		return false;
         	}
        },
        onClick: function (item, param) {
        	grdProgList.contextMenu.close();
	        
	        modalObj = new Object();
	        modalObj.userid = userId;
	    	modalObj.syscd = param.item.cr_syscd;
	    	modalObj.itemid = param.item.cr_itemid;
	    	modalObj.sysmsg = getSelectedVal('cboSystem').cm_sysmsg;
	    	modalObj.rsrcname = param.item.cr_rsrcname;
	    	modalObj.path = param.item.cm_dirpath;
	    	
	    	openProgModal();
        }
    },
    columns: [
    	{key: "jawon_code", 	label: "프로그램구분",  	width: '10%',	align: "left"},
    	{key: "jawon_code", 	label: "프로그램언어",  	width: '10%',	align: "left"},
        {key: "cr_rsrcname", 	label: "프로그램명",	  	width: '20%',	align: "left"},
        {key: "cr_story", 	 	label: "프로그램설명",   	width: '20%',	align: "left"},
        {key: "cm_dirpath", 	label: "프로그램경로",   	width: '30%',	align: "left"},
        {key: "cr_lastdate", 	label: "최종등록일",  		width: '10%',	align: "center"}
    ]
});

$(document).ready(function(){
	getSysInfo();
	//getTeamInfo();
	
	//조회시스템
	$("#cboSystem").bind('change', function() {
		cboSystem_Change();
	});
	
	//업무
	$("#cboJob").bind('change', function() {
		cboJob_Change();
	});
	
	//프로그램언어
	$("#cboRsrcCd").bind('change', function() {
		cboRsrcCd_Change();
	});
	
	//등록
	$('#btnExec').bind('click',function() {
		btnExec_Click();
	});
	
	//조회
	$('#btnQry').bind('click',function() {
		btnQry_Click();
	});
	
	//프로그램명 엔터이벤트
	$('#txtRsrcName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQry').trigger('click');
		}
	});
	
	//삭제
	$('#btnDel').bind('click',function() {
		btnDel_Click();
	});
	
	//초기화
	$('#btnInit').bind('click',function() {
		btnInit_Click();
	});
	
	//자동신규
	$('#btnAuto').bind('click',function() {
		btnAuto_Click();
	});

	//관리담당자
	$('#txtEditor').bind('click',function() {
		txtEditor_Click();
	});
});

function screenInit() {
	$('[data-ax5select="cboDir"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboJob"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboRsrcCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboRsrcGubun"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDept"]').ax5select({
		options: []
	});
	grdProgList.setData([]);
	$('#txtExeName').val("");
	$('#txtEditor').val("");
	$('#txtEditorToolTip').attr('tooltip','');
	$('#txtRsrcName').val("");
	$('#txtStory').val("");
	$('#btnDevRep').prop("disabled", true);
	$('#txtDsn').prop("disabled", true);
	$('#txtDsnHome').prop("disabled", true);
} 

function getTeamInfo() {
	var data = new Object();
	data = {
		SelMsg 	 : "SEL",
		cm_useyn : "Y",
		gubun 	 : "sub",
		itYn 	 : "Y",
		requestType : "getTeamInfoGrid2"
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json', successTeam);
}

function successTeam(data) {
	cboDeptData = data;
	options = [];
	
	$.each(cboDeptData,function(key,value) {
	    options.push({value: value.cm_deptcd, text: value.cm_deptname});
	});
	
	$('[data-ax5select="cboDept"]').ax5select({
        options: options
	});
}

//시스템조회 SysInfo.getSysInfo(strUserId,"Y","SEL",SecuYn,"OPEN");
function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId	: userId,
		SecuYn	: 'Y',
		SelMsg	: 'SEL',
		CloseYn	: adminYN ? 'N' : 'Y',
		ReqCd	: 'OPEN',
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successSystem);
}

function successSystem(data) {
	cboSystemData = data;
	
	cboSystemData = cboSystemData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) == '01') return false;
		else return true;
	});
	
	$('[data-ax5select="cboSystem"]').ax5select({
		options: injectCboDataToArr(cboSystemData, 'cm_syscd' , 'cm_sysmsg')
	});

	if(cboSystemData.length > 0) {
		cboSystem_Change();
	}
}

function cboSystem_Change() {
	screenInit();
	
	// 20221221 자동신규 기능 sysinfo 17, 18번 체크시 활성화
	if(getSelectedVal('cboSystem').cm_sysinfo.substr(16,1) == "1" || getSelectedVal('cboSystem').cm_sysinfo.substr(17,1) == "1" || getSelectedVal('cboSystem').cm_sysinfo.substr(18,1) == "1"){
		$("#btnAuto").prop('disabled', false);
	} else {
		$("#btnAuto").prop('disabled', true);
	}
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('ETCTEAM','SEL','N')
	]);
	cboDeptData = codeInfos.ETCTEAM;
	
	$('[data-ax5select="cboDept"]').ax5select({
		options: cboDeptData
	});
	
	var data = new Object();
	data = {
		Syscd : getSelectedVal('cboSystem').value,
		SelMsg : "",
		requestType : "getCompile"
	}
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successCompile);
	
	selectedIndex = getSelectedIndex('cboSystem');
	selectedItem = getSelectedVal('cboSystem');
	
	if(selectedIndex < 0) return;
	
	if(selectedIndex > 0) {
		tmpInfoData = new Object();
		tmpInfoData = {
			UserID  : userId,
			SysCd	: selectedItem.cm_syscd,
			SecuYn  : 'Y',
			CloseYn : 'N',
			SelMsg  : 'SEL',
			sortCd  : 'NAME',
			requestType	: 'getJobInfo'
		}
		
		//JobCd.getJobInfo(strUserId,cboSys.selectedItem.,"Y","N","SEL","NAME");
		ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successJob);
	}
}

function successJob(data) {
	cboJobData = data;
	
	$('[data-ax5select="cboJob"]').ax5select({
        options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
   	});
	
	if(cboJobData.length > 0) {
		getRsrcCd();
	}
}

function successCompile(data) {
	cboCompileData = data;
	cboCompileData = cboCompileData.filter(function(data) {
		if(data.cm_gubun == '01') return true;
		else return false;
	});
	$('[data-ax5select="cboCompile"]').ax5select({
        options: injectCboDataToArr(cboCompileData, 'cm_gubun' , 'cm_compscript')
   	});
	
	cboMakeData = data;
	cboMakeData = cboMakeData.filter(function(data) {
		if(data.cm_gubun == '02') return true;
		else return false;
	});
	$('[data-ax5select="cboMake"]').ax5select({
        options: injectCboDataToArr(cboMakeData, 'cm_gubun' , 'cm_compscript')
   	});
}

function getRsrcCd() {
	if(cboJobData.length > 0) {
		tmpInfoData = new Object();
		tmpInfoData = {
			SysCd  : getSelectedVal('cboSystem').value,
			SelMsg : 'SEL',
			requestType	: 'getRsrcOpen'
		}
		//Cmd0100.getRsrcOpen(cboSys.selectedItem.,"ALL");
		ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successJawon2);
		
		data = new Object();
		data = {
			SelMsg : 'SEL',
			requestType	: 'getRsrcOpen2'
		}
		ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successJawon);
	}
}

function successJawon(data) {
	cboRsrcGubunData = data;
	
	$('[data-ax5select="cboRsrcGubun"]').ax5select({
        options: injectCboDataToArr(cboRsrcGubunData, 'cm_micode' , 'cm_codename')
	});
	
	cboRsrcCd_Change();
}

function successJawon2(data) {
	cboRsrcCdData = data;
	
	$('[data-ax5select="cboRsrcCd"]').ax5select({
		options: injectCboDataToArr(cboRsrcCdData, 'cm_micode' , 'cm_codename')
	});
}

function cboRsrcCd_Change() {
	selectedIndex = getSelectedIndex('cboRsrcCd');
	selectedItem = getSelectedVal('cboRsrcCd');
	
	if(selectedIndex > 0) {
		$('#txtExeName').val(selectedItem.cm_extname);
		$("#cboDir").ax5select("enable");
		var saf = selectedItem.cm_info.substr(25,1);
		if(saf == "0" && selectedItem.cm_info.substr(1,1) == "0") {
			$('#txtDsn').prop("disabled", false);
			$('#txtDsnHome').prop("disabled", false);
		}
		/*if($('#txtExeName').val().length > 1) {
			if($('#txtExeName').val().substr($('#txtExeName').val().length-1, 1) == ",") {
				$('#txtExeName').val($('#txtExeName').val().substring(0, $('#txtExeName').val().length-1));
			}
		}*/
		else {
			$('#txtDsn').prop("disabled", true);
			$('#txtDsnHome').prop("disabled", true);
		}
		Dircheck(getSelectedVal('cboJob').value, getSelectedVal('cboRsrcCd').value);
	} else {
		$('[data-ax5select="cboDir"]').ax5select({
	        options: []
		});
	}
}

function cboJob_Change() {
	if(getSelectedIndex('cboJob') < 0) return;
	
	if(getSelectedIndex('cboRsrcCd') > 0) {
		Dircheck(getSelectedVal('cboJob').cm_jobcd, getSelectedVal('cboRsrcCd').value);
	}
}

function btnAuto_Click() {
	if(getSelectedVal('cboSystem').value == null || getSelectedVal('cboSystem').value == "00000") {
		dialog.alert("시스템을 선택하여 주세요.",function(){});
	} else {
		selSys = getSelectedVal('cboSystem').cm_syscd;
		selSysInfo = getSelectedVal('cboSystem').cm_sysinfo;
		selSysData = '['+getSelectedVal('cboSystem').cm_syscd+'] '+ getSelectedVal('cboSystem').cm_sysmsg;
		
		if (getSelectedIndex('cboJob')>=0) selJob = getSelectedVal('cboJob').cm_jobcd;
		else selJob = '';
		console.log(selSys);
		setTimeout(function() {
			autoModal.open({
				width: 1200,
				height: 600,
				iframe: {
					method: "get",
					url: "../modal/dev/NewAutoRegisterModal.jsp"
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
		}, 200);
	}
}

//Cmd0100.getDir(strUserId,cboSys.selectedItem.,"Y",cboRsrcCd.selectedItem.cm_micode,JobCd,"");
function Dircheck(jobcd, rsrccd) {
	if(getSelectedVal('cboSystem').value == "00089" && getSelectedVal('cboRsrcCd').value == "86") {
		data = new Object();
		data = {
			UserID : userId,
			SysCd  : getSelectedVal('cboSystem').value,
			RsrcCd : getSelectedVal('cboRsrcGubun').cm_micode,
			requestType : 'getDir_Tmax'
		}
		ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successDir);
	}else {
		tmpInfoData = new Object();
		tmpInfoData = {
				UserID	: userId,
				SysCd	: getSelectedVal('cboSystem').value,
				SecuYn	: "Y",
				RsrcCd	: rsrccd,
				JobCd	: jobcd,
				SelMsg	: "",
				requestType	: 'getDir'
		}
	}
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successDir);
}

function successDir(data) {
	cboDirData = data;
	
	if(cboDirData.length > 0) {
		$('[data-ax5select="cboDir"]').ax5select({
	        options: injectCboDataToArr(cboDirData, 'cm_dsncd' , 'cm_dirpath')
		});
	}
	
	gridSelectedIndex = grdProgList.selectedDataIndexs;
	selectedGridItem = grdProgList.list[grdProgList.selectedDataIndexs];
	
	if(selSw) {
		if($("#cboDir").ax5select("enable")) {
			for(var i = 0; i < cboDirData.length; i++) {
				if(getSelectedVal('cboSystem').value == "00089" && getSelectedVal('cboRsrcGubun').cm_micode == "86") {
					if(selectedGridItem.cm_dirpath.indexOf(cboDirData[i].cm_dirpath) >= 0) {
						$("#cboDir").ax5select('setValue',cboDirData[i].value,true);
						break;
					} 
				} else {
					if(gridSelectedIndex >= 0) {
						if(cboDirData[i].cm_dsncd == selectedGridItem.cr_dsncd) {
							$("#cboDir").ax5select('setValue',cboDirData[i].value,true);
							break;
						}
					}
				}
			}
		}
	}
}

function btnQry_Click() {
	if(getSelectedIndex('cboSystem') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.',function(){});
		$('#cboSystem').focus();
		return;
	}
	
	$('#txtRsrcName').val($('#txtRsrcName').val().trim());
	
	if(SecuYn == "N") {
		if($('#txtRsrcName').val().trim().length == 0) {
			if(getSelectedIndex('cboRsrcCd') == 0) {
				dialog.alert('관리자는 조회시간지연 및 대량의 결과가 예상되므로 프로그램구분을 선택한 후 조회하셔야 합니다.');
				return;
			}
		}
	}
	
	var strRsrc = "";
	if(getSelectedIndex('cboRsrcCd') > 0) strRsrc = getSelectedVal('cboRsrcCd').value;
	
	//Cmd0100.getOpenList(strUserId,cboSys.selectedItem.,strRsrc,txtProg.text,"true");
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId 	 : userId,
		SysCd  	 : getSelectedVal('cboSystem').value,
		RsrcCd 	 : strRsrc,
		Isrid	 : "",
		RsrcName : $('#txtRsrcName').val(),
		SecuSw 	 : "true",
		requestType	: 'getOpenList'
	}
	
	$('[data-ax5grid="grdProgList"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	$(".loding-div").remove();
	grdProgListData = data;
	grdProgList.setData(grdProgListData);
	
	if(grdProgListData.length < 1) dialog.alert('검색결과가 없습니다.');
	
	grdProgList.setColumnSort({cr_rsrcname:{seq:0, orderBy:"asc"}});
}

function grdProgList_Click() {
	gridSelectedIndex = grdProgList.selectedDataIndexs;
	selectedGridItem = grdProgList.list[grdProgList.selectedDataIndexs];
	
	var i = 0;
	var strJob = null;
	var docCd = selectedGridItem.cr_document;
	var strEditor = selectedGridItem.cr_master;
	
	selSw = true;
	
	if(selectedGridItem != null && selectedGridItem.cr_rsrcname != "") {
		$('#txtRsrcName').val(selectedGridItem.cr_exename);
		$('#txtStory').val(selectedGridItem.cr_story);
		$('#txtDsn').val(selectedGridItem.cr_dsncd2);
		$('#txtDsnHome').val(selectedGridItem.cr_dsncd2home);
		$('#txtEditor').val(selectedGridItem.cm_username);
		$('#txtEditorToolTip').attr('tooltip', selectedGridItem.cr_master);
		id = selectedGridItem.cr_master;
		
		if(docCd == "01") {
			$("#rdoYes").is(":checked");
		} else if(docCd == "02") {
			$("#rdoNo").is(":checked");
		}
		
		strJob = selectedGridItem.cr_jobcd;
		for(i=0; i<cboJobData.length; i++) {
			if(cboJobData[i].cm_jobcd == selectedGridItem.cr_jobcd) {
				$("#cboJob").ax5select('setValue',cboJobData[i].value,true);
				cboJob_Change();
				break;
			}
		}
		if(cboRsrcGubunData.length > 0){
			for(i=0; i<cboRsrcGubunData.length; i++){
				if(cboRsrcGubunData[i].cm_micode == selectedGridItem.cr_langcd){
					$('[data-ax5select="cboRsrcGubun"]').ax5select('setValue',cboRsrcGubunData[i].value,true);
					break;
				}
		  	}
		}
		for(i=1; i<cboRsrcCdData.length; i++) {
			if(cboRsrcCdData[i].cm_micode == selectedGridItem.cr_rsrccd) {
				$('[data-ax5select="cboRsrcCd"]').ax5select('setValue',cboRsrcCdData[i].cm_micode,true);
				selectedItem = cboRsrcCdData[i];
				strInfo = getSelectedVal('cboRsrcCd').cm_info;
				cboRsrcCd_Change();
				break;
			}
		}
		
		for(i=1; i<cboCompileData.length; i++) {
			if(cboCompileData[i].cm_compcd == selectedGridItem.cr_compile) {
				$('[data-ax5select="cboCompile"]').ax5select('setValue',cboCompileData[i].cm_micode,true);
				selectedItem = cboCompileData[i];
				break;
			}
		}
		
		for(i=1; i<cboMakeData.length; i++) {
			if(cboMakeData[i].cm_compcd == selectedGridItem.cr_makecompile) {
				$('[data-ax5select="cboMake"]').ax5select('setValue',cboMakeData[i].cm_micode,true);
				selectedItem = cboMakeData[i];
				break;
			}
		}
		
		if(cboDeptData.length > 0){
	    	  for(i=0; i<cboDeptData.length; i++){
	    		  if(cboDeptData[i].cm_micode == selectedGridItem.cr_teamcd){
	    			  $('[data-ax5select="cboDept"]').ax5select('setValue',selectedGridItem.cr_teamcd,true);
	    			  break;
	    		  }
	      	  }
	    }
		$('#txtExeName').val(selectedGridItem.cr_extname);
		$('#txtExeName').prop('disabled', true);
	}else {
		$('#txtRsrcName').val("");
		$('#txtStory').val("");
	}
}

function btnInit_Click() {
	$('#txtExeName').prop('disabled', false);
	$('#txtRsrcName').val("");
	$('#txtStory').val("");
	$('#txtEditor').val("");
	//$('#txtEditor').toolTip = "";
	$("#cboSystem").ax5select('setValue','00000',true);
	$("#cboRsrcGubun").ax5select('setValue','0000',true);
	if(getSelectedVal('cboSystem').value == '00000') {
		cboSystem_Change();
//		getTeamInfo();
	}
	return;
}

function btnExec_Click() {
	if(getSelectedIndex('cboSystem') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.',function(){});
		$('#cboSystem').focus();
		return;
	}
	
	if(cboDirData == null || cboDirData.length == 0 || getSelectedIndex('cboDir') < 0) {
		dialog.alert('경로를 선택하여 주십시오.',function(){});
		$('#cboDir').focus();
		return;
	}
	
	if(getSelectedIndex('cboRsrcCd') < 1) {
		dialog.alert('프로그램구분을 선택하여 주십시오.',function(){});
		$('#cboRsrcCd').focus();
		return;
	}
	
	if(getSelectedIndex('cboJob') < 0) {
		dialog.alert('업무를 선택하여 주십시오.',function(){});
		$('#cboJob').focus();
		return;
	}
	
	if(getSelectedIndex('cboRsrcGubun') < 1) {
		dialog.alert('프로그램언어를 선택하여 주십시오.',function(){});
		$('#cboRsrcGubun').focus();
		return;
	}
	
	$('#txtRsrcName').val($('#txtRsrcName').val().trim());
	if($('#txtRsrcName').val().length < 1) {
		dialog.alert('프로그램명을 입력하여 주십시오.',function(){});
		return;
	}
	
	if($('#txtRsrcName').val().indexOf(",") >= 0) {
		dialog.alert('프로그램명에 컴마(,)가 포함되었습니다. 제외하고 등록하여 주십시오.',function(){});
		return;
	}
	
	if($('#txtStory').val().length < 1) {
		dialog.alert('프로그램설명을 입력하여 주십시오.',function(){});
		$('#txtStory').focus();
		return;
	}
	
	if($("#txtExeName").val().length < 1) {
		if(getSelectedVal('cboRsrcGubun').cm_exeno == "N") {
			dialog.alert('프로그램확장자를 입력하여 주십시오.',function(){});
			$('#txtExeName').focus();
			return;
		}
	}
	
	if(getSelectedVal('cboRsrcCd').cm_info.substr(26,1) == "1") { //프로프레임연계(27)
		if($('#txtRsrcName').val().indexOf(".") > 0) {
			dialog.alert('확장자없이 신규하여야 합니다.',function(){});
		}
	}
	
	if(getSelectedIndex('cboCompile') < 0) {
		dialog.alert('컴파일모드를 선택하여 주십시오.',function(){});
		$('#cboCompile').focus();
		return;
	}

	if(getSelectedIndex('cboMake') < 0) {
		dialog.alert('메이크모드를 선택하여 주십시오.',function(){});
		$('#cboMake').focus();
		return;
	}

	if(getSelectedIndex('cboDept') < 0) {
		dialog.alert('관리팀을 선택하여 주십시오.',function(){});
		$('#cboDept').focus();
		return;
	}
	$('#txtEditor').val($('#txtEditor').val().trim());
	if($('#txtEditor').val().length < 1) {
		dialog.alert('관리담당자를 선택하여 주십시오.',function(){});
	}
	
	//id = popSelItem.cm_userid;
	
	var docCd = "";
	if($("#rdoYes").is(":checked")) docCd = "01";
	else if($("#rdoNo").is(":checked")) docCd = "02";
	
	
	var tmpInfoData = {
		UserId		: userId,
		SysCd		: getSelectedVal('cboSystem').value,
		OrderId 	: "",
		DsnCd		: getSelectedVal('cboDir').value,
		RsrcName	: $('#txtRsrcName').val(),
		RsrcCd		: getSelectedVal('cboRsrcCd').value,
		JobCd		: getSelectedVal('cboJob').value,
		ProgTit		: $('#txtStory').val().trim(),
		DirPath		: getSelectedVal('cboDir').text,
		CM_info		: getSelectedVal('cboRsrcCd').cm_info,
		dirSw		: false,
		RsRcGuBun 	: getSelectedVal('cboRsrcGubun').value,
		ComPile 	: getSelectedVal('cboCompile').cm_compcd,
		MakeComPile : getSelectedVal('cboMake').cm_compcd,
		Team 		: getSelectedVal('cboDept').value,
		DOCCD 		: docCd,
		ExeName 	: $('#txtExeName').val(),
		MasterID 	: id,
		EtcDsn 		: $('#txtDsn').val(),
		EtcDsnHome 	: $('#txtDsnHome').val(),
		requestType	: 'pgmCheck'
	}
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successCheckProg);
}
	
function successCheckProg(data) {
	var tmpArr = data;
	var tmpObj = [];

	if(tmpArr.length > 0 && tmpArr[0].ID == "ADD") {
		uptItemId = tmpArr[0].cr_itemid;
		dialog.alert('등록처리가 완료되었습니다.',function(){
		});
		// 실행모듈 체크일시 실행모듈 연결창 띄워주기
		var tmpInfo = getSelectedVal('cboRsrcCd').cm_info;
		if (tmpInfo.substr(8, 1) == "1" && uptItemId != "") {
			modalObj = [];
			modalObj.userid = userId;
			modalObj.syscd = data[0].cr_syscd;
			modalObj.itemid = data[0].cr_itemid;
			modalObj.sysmsg = getSelectedVal('cboSystem').cm_sysmsg;
			modalObj.rsrcname = data[0].cr_rsrcname;
			modalObj.path = data[0].cm_dirpath;

			openProgModal();
		}
	}else {
		dialog.alert(tmpArr[0].ID,function(){});
    }   
}

function txtEditor_Click() {
	setTimeout(function() {
		userModal.open({
			width: 550,
			height: 375,
			iframe: {
				method: "get",
				url: "../modal/userinfo/UserSelectModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					deptCd = '0091';
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
					if(popSelItem != null && popSelItem != undefined) {
						$("#txtEditor").val(popSelItem.cm_username);
						$('#txtEditorToolTip').attr('tooltip', popSelItem.cm_userid);
						$("#txtCaller").val(popSelItem.cm_userid);
						id = popSelItem.cm_userid;
					}
				}
			}
		}, function () {
			
		});
	}, 200);
}

function openProgModal(itemId, path) {
	if(modalObj.itemid == null || modalObj.itemid == '' || modalObj.itemid == undefined) {
		dialog.alert('프로그램정보가 누락되었습니다. [eCAMS 관리자 문의]');
		return;
	}
	
	setTimeout(function() {
		progModal.open({
			width: 1000,
			height: 750,
			iframe: {
				method: "get",
				url: "../modal/request/ProgramInfo_relat.jsp"
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
	}, 200);
}

function btnDel_Click() {
	var findSW = false;
	var checkedGridItem = grdProgList.getList("selected");
	
	if(getSelectedIndex('cboSystem') < 1) return;
	
	if(checkedGridItem.length > 0) {
		findSW = true;
	}
	
	if(!findSW) {
		dialog.alert('선택된 목록이 없습니다. 목록에서 삭제 할 대상을 선택한 후 처리하십시오.',function(){});
		return;
	}
	
	confirmDialog.confirm({
		title: '삭제확인',
		msg: '선택된 프로그램을 정말로 삭제하시겠습니까?'
	}, function(){
		if(this.key === 'ok') {
			var i = 0;
			findSW = false;
			checkedGridItem = grdProgList.getList("selected");
			
			if(getSelectedIndex('cboSystem') < 0) {
				dialog.alert('시스템을 선택하여 주십시오.',function(){});
				return;
			}
			
			tmpInfoData = new Object();
			tmpInfoData = {
				UserId		: userId,
				dataList	: checkedGridItem,
				requestType	: 'cmr0020_Delete'
			}
			ajaxAsync('/webPage/ecmd/Cmd0100Servlet', tmpInfoData, 'json', successDELETE);
		}
	});
}

function successDELETE(data) {
	if(data == "0") {
		dialog.alert('삭제처리가 완료되었습니다.',function(){});
		grdProgList.removeRow("selected");
	}else { 
		dialog.alert('삭제처리가 실패되었습니다.',function(){});
	}
}

/*function openRegPop(gbn) {
	if(getSelectedIndex('cboSystem') < 0) {
		dialog.alert('시스템을 선택하여 주십시오.',function(){});
		$('#cboSystem').focus();
		return;
	}
	
	var nHeight, nWidth;
	
	var form = document.popPam; 							//폼 name
	form.UserId.value = userId;   							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.SysCd.value  = getSelectedVal('cboSystem').value;  //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	
	if(winDevRep != null && !winDevRep.closed) {
		winDevRep.close();
	}	
	
	nHeight = 880;
	nWidth  = 1200;
	
	winDevRep = winOpen(form, 'devRep', '/webPage/winpop/PopDevRepository.jsp', nHeight, nWidth, 'no');
}*/