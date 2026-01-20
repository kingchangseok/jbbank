var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;

var grdProgList 	= new ax5.ui.grid();   //프로그램그리드

var selectedIndex;		//select 선택 index
var selectedItem;		//select 선택 item
var gridSelectedIndex;  //그리드 선택 index
var selectedGridItem;	//그리드 선택 item

var cboSysData 	   	   = null;	//시스템 데이터
var grdProgListData    = null;  //프로그램그리드 데이터
var progInfoData       = null;
var myWin 			   = null;

var selSw   = false;
var load1Sw = false;
var load2Sw = false;

var tmpInfo 	= new Object();
var tmpInfoData = new Object();

var tmpTab1 = null;
var tmpTab2 = null;
var gridSw  = false;
var likeSw	= false;

var str		= "";
var itemId	= "";

if(adminYN) SecuYn = "Y";
else SecuYn = "N";

$('[data-ax5select="cboSys"]').ax5select({
	options: []
});

grdProgList.setConfig({
    target: $('[data-ax5grid="grdProgList"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            grdProgList_Click();
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	},
     	trStyleClass: function () {
     		/*if (this.item.cr_status != null) {
     			if (this.item.cr_status === '9'){
         			return "fontStyle-cncl";
         		} 
     		}*/
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'cm_jobname', 	label: '업무명',  	width: '10%',	align: 'center'},
        {key: 'cm_dirpath', 	label: '프로그램경로', 	width: '35%',	align: 'left'},
        {key: 'cm_codename', 	label: '프로그램종류', 	width: '10%',	align: 'center'},
        {key: 'cr_rsrcname', 	label: '프로그램명',	width: '20%',	align: 'left'},
        {key: 'cr_lstver', 		label: '버전',   	    width: '5%',	align: 'center'},
        {key: 'sta', 	        label: '상태',  		width: '10%',	align: 'center'}
    ]
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	//tab메뉴
	//$('#tab1Li').width($('#tab1Li').width()+10);
	//$('#tab2Li').width($('#tab2Li').width()+10);
	
	//setTabMenu();
	
	$('#tabProgBase').width($('#tabProgBase').width()+10);
	$('#tabProgHistory').width($('#tabProgHistory').width()+10);
	//$("#tabProgBase").show(); //기본정보
	
	//시스템
	$('#cboSys').bind('change', function() {
		cboSys_Change();
	});
	
	//프로그램명 엔터
	$('#txtRsrcName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			event.keyCode = 0;
			$('#btnQry').trigger('click');
		}
	});

	//프로그램경로 엔터
	$('#txtDir').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			event.keyCode = 0;
			$('#btnQry').trigger('click');
		}
	});

	//조회
	$('#btnQry').bind('click',function() {
		getSql_Qry();
	});
	
	//엑셀저장
	$('#btnExl').bind('click',function() {
		excelExport(grdProgList,"ProgramInfo.xls");
	});
	
	$('#frmProgBase').onload = function() {
		load1Sw = true;
//		screenInit_prog('I');
	}
	
	$('#frmProgHistory').onload = function() {
		load2Sw = true;
//		screenInit_prog('I');
	}
	checkFrameLoad(["frmProgBase","frmProgHistory"], screenInit_prog, 'I');
	clickTabMenu();
	//팝업으로 호출했을 시 파라메터로 넘어온 정보 표시
	setTimeout(function() {
		if($("#syscd").val() > 0) {
			$('[data-ax5select="cboSys"]').ax5select("setValue", $("#syscd").val(), true);
			$("#txtRsrcName").val($("#rsrcname").val());
			$("#btnQry").trigger('click');
			$("#txtRsrcName").prop('disabled', true);
		}
	}, 1000);
	
});

function getProgInfo() {
	var data = new Object();
	data = {
		ItemId : itemId,
		requestType : 'getProgInfo'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', data, 'json', successGetProgInfo);
}

function successGetProgInfo(data) {
	var strWork = data;
	
	if($("#chkLike").is(":checked")) likeSw = true;
	else likeSw = false;
	
	if(strWork.length > 0) {
		sysCd = strWork.substr(0,5);
		dsnCd = strWork.substr(5,7);
		rsrcName = strWork.substr(12);
		$("#txtRsrcName").val(rsrcName);
	}
	
	if(rsrcName != null && rsrcName != "") {
		getSysInfo();
		getSql_Qry();
	}
}

function clickTabMenu(){
	$('ul.tabs li').click(function () {
		if($(this).hasClass('on')) {
			return;
		}
		$('.tab_content').hide();
		var activeTab = $(this).attr('rel');
		
		//tab메뉴 클릭에 따라 색상 변경
		$('ul.tabs li').removeClass('on');
		$(this).addClass('on');
		$('#' + activeTab).fadeIn();
		
		if(this.id === 'tab2') {
			setTimeout(function() {
				tmpTab2 = $('#frmProgHistory').get(0).contentWindow;
				tmpTab2.upFocus();
				
				if (!gridSw) {
					tmpTab2.createViewGrid();
					gridSw = true;
				}
			}, 10);
		}
	});
}

function screenInit() {
	selectedGridItem = [];
	grdProgList.setData([]);
	$('#txtRsrcName').val('');
	
	screenInit_prog('M');
} 

function screenInit_prog(gbn) {
	if (gbn == 'I') {
//		if (load1Sw && load2Sw) {
//			getSysInfo();
//			return;
//		}
		getSysInfo();
		return;
	}
	
	/*$("#tab1").unbind("click");
	$("#tab2").unbind("click");*/
	
	progInfoData = [];
	tmpTab1 = $('#frmProgBase').get(0).contentWindow;
	tmpTab1.screenInit(gbn,userId);
	tmpTab2 = $('#frmProgHistory').get(0).contentWindow;	
	tmpTab2.screenInit(gbn,userId);	
	
	//$('#tab1').trigger('click');
	$("#tabProgHistory").hide(); // 20200514 밑에 중복되는거 제거
	$("#tabProgBase").show(); //기본정보
	$('#tab2').removeClass('on');
	$('#tab1').addClass('on');
	$('#tab1').fadeIn();
}

function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SecuYn : SecuYn,
		WkSys  : "",
		requestType	: 'getCbo_SysCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json', successSystem);
}

function successSystem(data) {
	cboSysData = data;
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});

	if(cboSysData.length > 0) {
		for(var i=0; i<cboSysData.length; i++) {
			if(cboSysData[i].cm_syscd == '01200') {
				$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[i].cm_syscd,true);
				break;
			}
		}
		
		cboSys_Change();
	}
}

function cboSys_Change() {
	screenInit();
	selectedIndex = getSelectedIndex('cboSys');
	selectedItem = getSelectedVal('cboSys');
	
	if(selectedIndex < 0) return;
	getJobInfo(selectedItem.cm_syscd);  //업무 리로딩
}

//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(sysCd) {
	console.log('[getJobInfo] ==>'+sysCd);
	if (progInfoData != null && progInfoData != '' && progInfoData != undefined) {
		sysCd = progInfoData[0].cm_syscd;
	}
		
	tmpInfoData = new Object();
	tmpInfoData = {
		L_Syscd : sysCd,
		SecuYn  : adminYN ? 'Y' : 'N',
		UserId  : userId,
		ItemId  : '',
		requestType	: 'Cmd0500_Cbo_Set'
	}
	tmpTab1 = $('#frmProgBase').get(0).contentWindow;
	tmpTab1.getJobInfo(tmpInfoData);
}

function getSql_Qry() {
	
	grdProgListData = [];
	grdProgList.setData(grdProgListData);
	
	screenInit_prog('S');
	
	if (getSelectedIndex('cboSys') < 0) {
		dialog.alert('시스템을 선택하여 주십시오.');
		$('#cboSys').focus();
		return;
	} else if ($("#txtRsrcName").val() == "" && $("#txtDir").val() == "") {
		setTimeout(function() {
			dialog.alert('프로그램명 또는 프로그램경로를 입력하여 주십시오.');
		}, 10);
		return;
	}

	$('#txtRsrcName').val($('#txtRsrcName').val().trim());
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId 		: userId,
		SecuYn 		: SecuYn,
		ViewFg 		: "false",
		L_Syscd 	: getSelectedVal('cboSys').cm_syscd,
		Txt_ProgId  : $("#txtRsrcName").val().trim(),
		DsnCd 		: "",
		DirPath 	: $("#txtDir").val().trim(),
		LikeSw 		: $("#chkLike").is(":checked") ? true : false,
		requestType	: 'getSql_Qry'
	}
	console.log('getSql_Qry',tmpInfoData);
	$('[data-ax5grid="grdProgList"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	$(".loding-div").remove();
	grdProgListData = data;
	grdProgListData.forEach(function(item) {
		item.cr_lstver = Number(item.cr_lstver);
	});
	grdProgList.setData(grdProgListData);
	
	if(grdProgListData.length < 1) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	grdProgList.setColumnSort({cr_rsrcname:{seq:0, orderBy:'asc'}});
}

function grdProgList_Click() {
	gridSelectedIndex = grdProgList.selectedDataIndexs;
	selectedGridItem = grdProgList.list[grdProgList.selectedDataIndexs];
	
	screenInit_prog('S');
	
	var data = new Object();
	data = {
		Syscd : getSelectedVal('cboSys').cm_syscd,
		SelMsg : "",
		requestType : "getCompile"
	}
	tmpTab1 = $('#frmProgBase').get(0).contentWindow;
	tmpTab1.getCodeInfo(data);
	
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId   : userId,
		SecuYn   : SecuYn,
		L_SysCd  : selectedGridItem.cr_syscd,
		L_JobCd  : selectedGridItem.cr_jobcd,
		L_ItemId : selectedGridItem.cr_itemid,
		requestType	: 'Cmd0500_Lv_File_ItemClick'
	}
	progInfoData = ajaxCallWithJson('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json');
	
	if (progInfoData != null && progInfoData != '' && progInfoData != undefined) {
		progInfoData[0].cm_sysmsg = getSelectedVal('cboSys').cm_sysmsg;
		progInfoData[0].cr_syscd = selectedGridItem.cr_syscd;
		progInfoData[0].cr_rsrcname = selectedGridItem.cr_rsrcname;
		progInfoData[0].sta = selectedGridItem.sta;
		progInfoData[0].cm_dirpath = selectedGridItem.cm_dirpath;
		progInfoData[0].cr_dsncd = selectedGridItem.cr_dsncd;
		progInfoData[0].cr_itemid = selectedGridItem.cr_itemid;
		progInfoData[0].cr_status = selectedGridItem.cr_status;
		
		tmpTab1 = $('#frmProgBase').get(0).contentWindow;
		tmpTab1.successProgInfo(progInfoData);
		tmpTab1.pUserId = userId;
		tmpTab1.SecuYn = SecuYn;

		tmpTab2 = $('#frmProgHistory').get(0).contentWindow;
		tmpTab2.successProgInfo(progInfoData);
		tmpTab1.pUserId = userId;
	}
}
