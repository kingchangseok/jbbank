/** 체크아웃 화면 기능
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */

var userName 	      = window.top.userName;
var userId 			  = window.top.userId;
var adminYN		      = window.top.adminYN;
var userDeptName      = window.top.userDeptName;
var userDeptCd 	      = window.top.userDeptCd;
var reqCd 			  = window.top.reqCd;
var attPath			  = window.top.attPath;
//grid 생성
var firstGrid		  = new ax5.ui.grid();
var secondGrid		  = new ax5.ui.grid();
var thirdGrid		  = new ax5.ui.grid();

var paraSearchModal   	= new ax5.ui.modal();//파라미터연관조회 모달

var picker 				= new ax5.ui.picker();

var orgFirstGridData    = [];
var firstGridData  		= [];
var gridSimpleData 		= [];
var secondGridData 		= [];
var tmpArr 				= [];
var firstGridColumns 	= null;

var sysData 	   		= [];
var prgData	  	   		= [];
var treeData 	   		= [];
var selectedGridItem 	= [];
var cboReqData 			= [];
var orderAcData			= [];
var cboJobData          = [];

/* 이전버전체크아웃 관련 변수 */
var ChkOutVerSelModal	= new ax5.ui.modal();//이전버전선택 모달
var updateFlag   		= false; //그리드 컬럽 value 업데이트 여부
var pItemId 	 		= null;  //버전선택창 파라미터: 프로그램 ITEMID
var selectVer    		= '';    //선택버전
var selectAcptno 		= '';    //선택한신청버전
var selectViewVer		= 'sel';

/* 결재절차확정모달 관련 변수 */
var approvalModal 	= new ax5.ui.modal();
var confirmData 	= [];	//from modal
var confirmInfoData = null;	//to modal

/* 신청상세>재요청 관련 변수 */
var strAcptNo 	   	= '';
if(window.opener != null && window.opener != undefined) {
	strAcptNo = window.opener.pReqNo;
}
var winSw 		= '';	//신청상세에서 winSw=true로 set
var gitSw       = false;

var excelAry  	= null;
var outpos    	= 'R';
var searchMOD 	= '';
var dirCd     	= '';
var tmpPath  	= '';
var sysCd	 	= '';
var jobCd	  	= '';
var reqSw 		= false;
var gitSw       = false;
var alertFlag 	= 0;
var myWin 		= null;
var acptNo 		= '';
var exlSw 			= false;
var localFileDownYN = false; //로컬로파일다운
var tmpPath			= '';
var uploadJspFile 	= '';
dialog.setConfig({
    width: 400
});

var setting = {
	data: {
		simpleData: {
			enable: true,
		}
	},
	callback:{
		onExpand: myOnExpand,
		onClick : onClickTree,
	}
};

/**
 * $ 사용건 사전 선언
 */

$('#datStD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
var selcboSys = $('[data-ax5select="cboSys"]');

firstGrid.setConfig({
    target: $('[data-ax5grid="first-grid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: false, 
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
	        if (this.colIndex == 4 && reqCd == '02' && this.item.cr_status == '0') {//버전선택
	        	this.self.clearSelect();
	        	this.self.select(this.dindex);
	        	
	        	var gridSelIdx = this.dindex;
	        	
	        	pItemId = this.item.cr_itemid;
	        	
	    		setTimeout(function() {
	    			ChkOutVerSelModal.open({
	    				width: 1000,
	    				height: 500,
	    				iframe: {
	    					method: "get",
	    					url: "../modal/request/ChkOutVerSelModal.jsp"
	    				},
	    				onStateChanged: function () {
	    					if (this.state === "open") {
	    						mask.open();
	    					}
	    					else if (this.state === "close") {
	    						console.log(gridSelIdx, updateFlag, selectVer, selectAcptno, selectViewVer);
	    						
	    						if (updateFlag) {
	    							firstGrid.setValue(gridSelIdx, 'scrver', selectViewVer);
	    							firstGrid.setValue(gridSelIdx, 'selAcptno', selectAcptno);
	    							firstGrid.setValue(gridSelIdx, 'cr_lstver', selectVer);
	    						}
	    						mask.close();
	    						firstGrid.repaint();
	    					}
	    				}
	    			}, function () {
	    				
	    			});
	    		}, 200);
	        } else {
	        	this.self.select(this.dindex);
	        	firstGridClick();
	        }
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	addDataRow();
        },
    	trStyleClass: function () {
    		if (this.item.filterData || this.item.selected_flag == "1" || this.item.cr_status != "0" || this.item.cr_nomodify != "0" || this.item.unyoung == "1"){
    			this.item.__disable_selection__ = '';
    			//this.item.__selected__ = true;
    			return 'fontStyle-cncl';
    		}else {
    			return '';
    		}
    	},
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
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
            {type: 1, label: "추가"},
        ],
        popupFilter: function (item, param) {
        	firstGrid.clearSelect();
          	firstGrid.select(Number(param.dindex));

        	var selIn = firstGrid.selectedDataIndexs;
        	if(selIn.length === 0) return;
        	 
         	if (param.item == undefined) return false;
         	if (param.dindex < 0) return false;
         	
			strConfUser = "";
			
			var retType = ''; 
         	if (param.item.selected_flag == "0" && !param.item.filterData && param.item.cr_status == "0") {
         		retType = '1';
    		}
         	
         	if (retType == '') return false;
         	var retString;
			    
         	if (retType.indexOf('1')>-1){
		    	retString = (item.type == 1);
		    }
         	
		    return retString;
        },
        onClick: function (item, param) {
        	if(item.type == '1') addDataRow();
        	
	        firstGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
        {key: 'cm_dirpath2', 	label: '프로그램경로',	width: '25%', align: 'left'},
        {key: 'cr_rsrcname', 	label: '프로그램명',	width: '25%', align: 'left'},
        {key: 'errmsg',			label: '체크결과',		width: '10%', align: 'left'},
        {key: 'jawon', 			label: '프로그램종류',	width: '8%', align: 'left'},
        {key: 'codename', 		label: '상태',  		width: '6%', align: 'left'},
        {key: 'cr_lstver', 		label: '버전',  		width: '6%',
        	formatter: function() {
        		if (this.value == 'sel') {
        			return '<button style="width: 98%; height: 98%;">버전선택</button>';
        		} else {
        			return '<button style="width: 98%; height: 98%;">버전: '+ this.value +'</button>';
        		}
        	}
        },
        {key: 'cm_username',	label: '수정자',		width: '5%'},
        {key: 'lastdt', 		label: '수정일',		width: '7%'}, 	
        {key: 'cr_orderid',		label: '발행번호',		width: '10%'} 	
    ]
});
var fn_disabled = function (){
	return function () {
		console.log(this);
		console.log(this.value);
		return this.item.chk_selected == "0" ? true : false;
	}
}
secondGrid.setConfig({
    target: $('[data-ax5grid="second-grid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: false, 
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect(); //기존선택된 row deselect 처리 (multipleSelect 할땐 제외해야함)
            //this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	deleteDataRow();
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		simpleData();
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
            {type: 1, label: "제거"},
        ],
        popupFilter: function (item, param) {
        	if(secondGrid.getList('selected').length < 1){
        		return false;
        	}
         	return true;
        },
        onClick: function (item, param) {
	        if(item.type == '1') deleteDataRow();
	        
	        secondGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
    	/*{key: 'selected', 	label: '체크아웃여부',	width: '10%', align: 'center',
    		editor: {
    			type: "checkbox", 
    			config: {height: 17, trueValue: "1", falseValue: "0"},
    			disabled: function() {
					return fn_disabled;
				},
				sortable: false, 
    		}
    	},*/
    	{key: 'cm_dirpath2', 	label: '프로그램경로',	width: '30%', align: 'left'},
    	{key: 'cr_rsrcname', 	label: '프로그램명',  	width: '20%', align: 'left'},
    	{key: 'jobname', 		label: '업무명',  	width: '10%', align: 'left'},
    	{key: 'jawon',			label: '프로그램종류',	width: '10%', align: 'left'},
    	{key: 'cr_lstver', 		label: '신청버전',  	width: '6%'},
    	{key: 'cr_expday', 		label: '반환예정일',  	width: '8%'},
    	{key: 'cm_username',	label: '수정자',  	width: '6%'},
    	{key: 'lastdt', 		label: '수정일',  	width: '10%'}
    ]
});

thirdGrid.setConfig({
	target: $('[data-ax5grid="third-grid"]'),
	sortable: true, 
	multiSort: true,
	multipleSelect: false,
	showRowSelector: false,
	header: {
		align: 'center'
	},
	body: {
		onClick: function () {
			//this.self.clearSelect(); //기존선택된 row deselect 처리 (multipleSelect 할땐 제외해야함)
			//this.self.select(this.dindex);
		},
		onDBLClick: function () {
			//this.self.clearSelect();
			//this.self.select(this.dindex);
			
			openWindow('orderlist', '', this.item.CC_ORDERID);
			
		},
		onDataChanged: function(){
			this.self.repaint();
		}
	},
	columns: [
		{key: 'CC_ORDERID', label: '발행번호',	 width: '20%',  align: 'left'},
		{key: 'CC_REQSUB', 	label: '요청제목', width: '80%',  align: 'left'}
	]
});

$('input.checkbox-checkout').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	winSw = $('#winSw').val();
	if(winSw === 'true') {
		userId = $('#userId').val();
		reqCd = window.opener.reqCd;
		adminYN = window.opener.isAdmin;
		attPath = window.opener.attPath;
		$('#divFrame').css('padding','10px');
	}
	
	screenInit();
	getSysCbo();
	getOrderList();
	getTmpDir();
	selcboSys.bind('change',function(){
		changeSys();
	});
	$('#cboJob').bind('change',function(){
		changeJob();
	});
	
	// 검색
	$('#btnSearch').bind('click',function(){
		clickSearchBtn();
	});
	
	// gitlab 검색
	$('#btnGit').bind('click',function(){
		clickGitBtn();
	});
	
	// 파라미터연관조회
	$('#btnParaSearch').bind('click',function(){
		if($('#txtSayu').val().trim().length < 1) {
			dialog.alert("사유를 입력하여 주시기 바랍니다.");
			$('#txtSayu').focus();
			return;
		} else {
			btnParaSearch_Click();
		}
	});
	
	// 제거
	$('#btnDelete').bind('click',function(){
		deleteDataRow()
	});
	
	// 추가
	$('#btnRegist').bind('click',function(){
		addDataRow();
	});
	
	// 체크아웃
	$('#btnReq').bind('click',function(){
		checkOutClick();
	});
	
	//프로그램명조회
	$('#txtRsrcName').bind('keypress',function(event){
		// 20221214 프로그램명 조회 버튼 클릭시 입력체크함 
//		if($('#txtRsrcName').val().trim().length == 0) {
//			setTimeout(function(){
//				dialog.alert('검색단어를 입력한 후 검색하시기 바랍니다.');
//			}, 200);
//			return;
//		}
		
		if(event.keyCode == 13){
			event.keyCode = 0;
			clickSearchBtn();
		}
	});
	
	//체크아웃항목상세보기
	$('#chkDetail').bind('click',function(){
		simpleData();
	});
	
	/*
	$('#btnExcelLoad').bind('click',function(){
		$('#excelFile').click();
	});
	*/
	
	// 20221222
	$('#btnLoadExl').bind('click', function() {
		if(getSelectedIndex('cboSys') < 1) {
			dialog.alert('시스템 선택 후 사용해 주시기 바랍니다.', function() {});
			return;
		}
		
		$('#excelFile').trigger('click');
	});
	
	// 엑셀템플릿
	$('#btnExlTmp').bind('click', function() {
		fileDown(attPath+"/"+"checkout_excel_templet.xlsx","checkout_excel_templet.xlsx");
	});
	
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
});

function screenInit() {
	$('#btnReq').attr('disabled',true);
}

function getSysCbo(){
	var sysInfoData = new Object();
	
	if(strAcptNo != null && strAcptNo != '') {
		var tmpData = {
			UserId : userId,
			AcptNo : strAcptNo,
			requestType : 'getSysInfo',
		}	
		ajaxAsync('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json',successGetSysCbo);
	}
	
	var tmpData = {
		UserId 	: userId,
		SecuYn 	: 'Y',
		SelMsg 	: '',
		CloseYn : 'N',
		ReqCd 	: reqCd,
		requestType : 'getSysInfo',
	}	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpData, 'json',successGetSysCbo);
	
	if(strAcptNo != "") {
		var tmpData = {
			AcptNo : strAcptNo,
			UserId : userId,
			requestType : 'chkRechkOut'
		}
		console.log('chkRechkOut ==>', tmpData);
		ajaxAsync('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json',successChkRechkOut);
	}
}

function successGetSysCbo(data){
	console.log('[successGetSysCbo] ==>',data);
	sysData = data;
	
	sysData = sysData.filter(function(item) {
		if(item.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	})
	
	selcboSys.ax5select({
        options: injectCboDataToArr(sysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if (sysData.length > 0) {
		if (strAcptNo != null && strAcptNo != "") {
			selcboSys.ax5select("setValue", sysData[0]);
		} else {
			for (var i=0;sysData.length>i;i++) {
				if (sysData[i].setyn == "Y") {
					selcboSys.ax5select("setValue", sysData[i].cm_syscd);
					break;
				}
			}
			if(sysData.length < i) selcboSys.ax5select("setValue", sysData[0]);
		}
	} else {
		/*if (strAcptNo != null && strAcptNo != "") {
			dialog.alert("사용권한이 없는 시스템에 대하여 체크아웃요청을 의뢰하였습니다.");
		}*/
	}
	
	$(selcboSys).trigger('change');
}

function getOrderList() {
	var tmpData = {
		Userid : userId,
		requestType : 'OrderSelect2'
	}
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', tmpData, 'json',successGetOrderList);
}

function successGetOrderList(data) {
	thirdGridData = data;
	thirdGrid.setData(thirdGridData);
}

function successChkRechkOut(data) {
	if(Boolean(data)) {
		var tmpData = {
			UserId : userId,
			AcptNo : strAcptNo,
			requestType : 'getReturnInfo'
		}
		ajaxAsync('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json',successGetReturnInfo);
	}
}

function successGetReturnInfo(data) {
	console.log('[successGetReturnInfo] ==>', data);
	
	secondGridData = data;
	secondGrid.setData(secondGridData);
	
	selectedIndex = getSelectedIndex('cboSys');
	selectedItem = getSelectedVal('cboSys');
	
	for(var i=0; i<secondGridData.length; i++) {
		var tmpObj = secondGridData[i];
		
		$("#cboSys").ax5select('setValue',tmpObj.cr_syscd,true);
		changeSys();
		
		$("#txtSayu").val(tmpObj.cr_sayu);
		
		if(tmpObj.cr_tstchg == "7") $("#chkOverWrite").wCheck("check", true);
		else $("#chkOverWrite").wCheck("check", false);
		
		var strDate = tmpObj.cr_expday;
		$("#datStD").val(strDate);
		
		$('#btnReq').prop('disabled',false);
	}
	
	var getFileData = new Object();
	getFileData.UserId = userId;
	getFileData.SysCd = selectedItem.cm_syscd;
	getFileData.SysGb = selectedItem.cm_sysgb;
	getFileData.DsnCd = "";
	getFileData.RsrcCd = "";
	getFileData.ReqCd = "01";
	getFileData.RsrcName = "*";
	getFileData.JobCd = "0000";
	getFileData.UpLowSw = false;
	getFileData.selfSw = false;
	getFileData.LikeSw = false;
	
	getFileGridData(getFileData);
	getProgOrders();
}

function getProgOrders() {
	var tmpData = {
		AcptNo : strAcptNo,
		requestType : 'getProgOrders'
	}
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json',successGetProgOrders);
}

function successGetProgOrders(data) {
	orderAcData = data;
}

function changeSys(){
	firstGrid.setData([]);
	fristGridData = [];
	treeData = [];
	cboJobData = [];
	gitSw = false;
	$('[data-ax5select="cboJob"]').ax5select({
        options: []
   	});
	ztree = $.fn.zTree.init($('#treeDemo'), setting, treeData);
	
	$('#btnGit').css('display','none');
	$('#divgit').css('display','none');
	
	if(getSelectedIndex('cboSys') < 0) return;
	
	if (getSelectedVal('cboSys').cm_sysinfo.substr(4,1) == "1" && getSelectedVal('cboSys').cm_stopsw == "1") {
		dialog.alert('이관통제를 위하여 일시적으로 형상관리 사용을 중지합니다.');
		$('#btnSearch').prop('disabled',true);
		return;
	}
	
	if(getSelectedVal('cboSys').cm_sysinfo.substr(3,1) == "1") {
		$('#btnSearch').prop('disabled',false);
	}else {
		$('#btnSearch').prop('disabled',false);
	}
	getRsrcPath();
	sysCd = getSelectedVal('cboSys').value;
	
	if(sysCd == '01200') $('#btnParaSearch').show();
	else $('#btnParaSearch').hide();
	
	console.log('changeSys='+getSelectedVal('cboSys').cm_sysinfo.substr(18,1));
	if(getSelectedVal('cboSys').cm_sysinfo.substr(18,1) == "1") {
		gitSw = true;
		$('#btnGit').css('display','inline-block');
		$('#divgit').css('display','inline-block');
		tmpInfoData = new Object();
		tmpInfoData = {
			UserID  : userId,
			SysCd	: sysCd,
			SecuYn  : 'Y',
			CloseYn : 'N',
			SelMsg  : 'SEL',
			sortCd  : 'NAME',
			requestType	: 'getJobInfo'
		}
		
		//JobCd.getJobInfo(strUserId,cboSys.selectedItem.,"Y","N","SEL","NAME");
		ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successJob);
	} 
		
	if(strAcptNo != null && strAcptNo != '' && strAcptNo != undefined) {
		getLinkList();
		strAcptNo = '';
	}
}


function successJob(data) {
	cboJobData = data;
	
	$('[data-ax5select="cboJob"]').ax5select({
        options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
   	});
}

function changeJob(){
	orgFirstGridData = [];
	firstGridData = [];
	firstGrid.setData([]);
}
function getLinkList() {
	var tmpData = {
		UserID : userId,
		AcptNo : strAcptNo,
		requestType: 'getLinkList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json',successGetLinkList);
}

function successGetLinkList(data) {
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function getRsrcPath() {
	var tmpData = {
		UserID : userId,
		SysCd  : getSelectedVal('cboSys').cm_syscd,
		SecuYn : "y",
		SinCd  : reqCd,
		ReqCd  : "",
		requestType: 'getRsrcPath_ztree'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpData, 'json',successGetFileTree);
}

function successGetFileTree(data){
	/*if (data.length < 2) {
		dialog.alert("체크아웃 대상경로가 없습니다.");
		ztree = $.fn.zTree.init($('#treeDemo'), setting, treeData);
		return;
	}*/
	
	treeData = data;
	
	if(treeData !== 'ERR') {
		var obj = treeData;
		
		/*for(var i in treeData){
			if(obj[i].cm_codename =='' ){
				delete obj[i]
				continue;
			}
			obj[i].name = obj[i].cm_codename;
			delete obj[i].cm_codename;
			obj[i].isParent = true;
		}*/
		obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
		obj = JSON.parse(obj);
		treeData = obj;
		
		ztree = $.fn.zTree.init($('#treeDemo'), setting, treeData);
	}
}

// 폴더 펼칠때 실행하는 함수
function myOnExpand(event, treeId, treeNode) {
	if(treeNode.children != undefined) {
		return false;
	}
	
	if(treeNode.cm_upseq != '0'){
		return false;
	};
	
	//로딩중 icon class 추가
	$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){

		var ajaxReturnData = null;
		
		var requestType = "";
		var tmpData = {};
		
		tmpData = {
			UserId : userId,
			SysCd  : getSelectedVal('cboSys').value,
			RsrcCd : treeNode.cr_rsrccd,
			Info   : treeNode.cm_info,
			seqNo  : treeNode.cm_seqno,
			requestType:  "getDirPath3_ztree"
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
		
		console.log('[getDirPath3_ztree] ==>', ajaxReturnData);
		if (ajaxReturnData == undefined || ajaxReturnData == null) return;
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
		} else {
			var obj = ajaxReturnData;
				
			for(var i in ajaxReturnData){
				if(obj[i].cm_dirpath =='' ){
					delete obj[i]
					continue;
				}
				obj[i].name = obj[i].cm_dirpath;
				//delete obj[i].cm_dirpath;
				obj[i].isParent = true;
			}
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			ztree.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
};

function onClickTree(event, treeId, treeNode) {
	var sysCd  		= getSelectedVal('cboSys').value;
	var sysgb  		= getSelectedVal('cboSys').cm_sysgb;
	
	var hasChild 	= treeNode.children != undefined? true : false;
	var fileId 		= treeNode.id;
	var fileInfo 	= treeNode.cm_info;
	var dsncd  		= treeNode.cr_dsncd;
	var rsrccd 		= treeNode.cr_rsrccd;
	var subrsrccd	= treeNode.cr_rsrccd
	var fullpath = null;
	
	if(treeNode.cm_volpath == null){
		fullpath = treeNode.cm_fullpath;
	}else{
		fullpath = treeNode.cm_volpath;
	}
	
	if(fileId != '-1' ) {
		if( rsrccd ===  undefined) rsrccd = subrsrccd;
		//if( root === 'true' && !hasChild ) 					getChildFileTree(fileInfo, rsrccd, fileId);
		if( dsncd !== undefined || fullpath !== undefined) 	makeFileDir(fullpath, dsncd, fileInfo, hasChild, sysgb, rsrccd, sysCd);
	}
	
	searchMOD = 'T';
}

function makeFileDir(fullpath, dsncd, fileinfo, hasChild, sysgb, rsrccd, sysCd){
	var lb6split 	= fullpath.split('/');
	var lb6String1 	= '';
	var toDsnCd 	= fullpath;
	var strDsn 		= dsncd;
	var devToolCon 	= false;
	var LikeSw		= false;
	var UplowSw		= false;
	var getFileData = {};
	var rsrcname 	= $('#txtRsrcName').val().trim();
	var selectedSubnode = $('#chkbox_subnode').is(':checked');
	
	firstGridData = [];
	firstGrid.setData([]);
	
	if(!devToolCon && fileinfo != undefined && fileinfo.substr(26,1) == '1') {
		devToolCon = true;
	}
	
	if (lb6split.length < 2){
		$('#lbPath').text('');
	}
	
	for (var i=0 ; i < lb6split.length ; i++){
		if (lb6split[i].length>0) {
			if (i==0 && lb6split[i].indexOf(':')>0) {
				lb6String1 = lb6split[i];
			} else {
				lb6String1 = lb6String1+ '/'+ lb6split[i];
			}
		}
	}
	
	if (lb6String1.length > 0){
		$('#lbPath').text(lb6String1);
	}
	
	if($("#chkUpLow").is(":checked")) getFileData.UplowSw = true;
	else getFileData.UpLowSw = false;
	
	if (selectedSubnode  && lb6String1.length > 0 ){//하위디렉토리 포함 일때
		toDsnCd = 'F' + lb6String1;
	}else if (!selectedSubnode && lb6String1.length > 0 && devToolCon && !hasChild){
		toDsnCd = 'F' + lb6String1;
	}else if (strDsn != '' && strDsn != null) {
		toDsnCd = strDsn;
	}else {
		toDsnCd = '';
	}
	
	if($("#chkLike").is(":checked")) getFileData.LikeSw = true;
	else getFileData.LikeSw = false;
	
	if (toDsnCd.length > 0 || (toDsnCd.length == 0 && selectedSubnode) ) {
		getFileData.UserId = userId;
		getFileData.SysCd = sysCd;
		getFileData.SysGb = sysgb;
		getFileData.DsnCd = toDsnCd;
		getFileData.RsrcCd = rsrccd;
		getFileData.ReqCd = reqCd;
		if($('#txtRsrcName').val().trim().length === 0) getFileData.RsrcName = '';
		else getFileData.RsrcName = rsrcname;
		getFileData.JobCd = "0000";
		getFileData.selfSw = false;
		
		getFileGridData(getFileData);
	}
	searchMOD = 'T';
}

function getFileGridData(getFileData) {
	var getFileDataInfo = {
		UserId : getFileData.UserId,
		SysCd : getFileData.SysCd,
		SysGb : getFileData.SysGb,
		DsnCd : getFileData.DsnCd,
		RsrcCd : getFileData.RsrcCd,
		RsrcName : getFileData.RsrcName,
		ReqCd : getFileData.ReqCd,
		JobCd : getFileData.JobCd,
		UpLowSw : getFileData.UplowSw,
		selfSw : getFileData.selfSw,
		LikeSw : getFileData.LikeSw,
		requestType	: 'getFileList',
	}
	$('[data-ax5grid="first-grid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', getFileDataInfo, 'json',successGetFileGridData);	
}

function successGetFileGridData(data){
	$(".loding-div").remove();
	
	console.log('[successGetFileGridData] ==>', data);
	if (data == undefined || data == null) return;
	if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
		dialog.alert(data);
	} else {
		firstGridData = data;
		firstGridData.forEach(function(item) {
			var verNum = Number(item.cr_lstver);
			if(verNum >= 0) {
				item.cr_lstver = verNum;
			}
		});

		//컬럼제거하기
		firstGridColumns = firstGrid.columns;
		if(firstGridColumns[2].key == 'errmsg'){
			firstGrid.removeColumn(2);
		}

		if( (null == firstGridData || firstGridData.length == 0) && alertFlag == 1){
			firstGrid.setData([]);
			dialog.alert('검색 결과가 없습니다.');
		} else {
			alertFlag = 0;
			
			$(firstGridData).each(function(i){
	
				/*if(firstGridData[i].cm_info.substr(57,1) == '1' &&  firstGridData[i].cm_info.substr(44,1) == '0'){
					firstGridData[i].selected_flag = '1';
					firstGridData[i].view_dirpath = '이클립스에서만 체크아웃이 가능합니다.';
				}*/
				
				$(secondGridData).each(function(j){
					if(secondGridData[j].cr_itemid == firstGridData[i].cr_itemid){
						firstGridData[i].selected_flag = '1';
					}
				})
				
				if(firstGridData[i].selected_flag == '1' || firstGridData[i].cr_status != '0' || firstGridData[i].cr_nomodify != '0'){
					firstGridData[i].filterData = true;
					firstGridData[i].__disable_selection__ = true;
				}
				else{
					firstGridData[i].filterData = false;
					firstGridData[i].__disable_selection__ = '';
				}
			});
			firstGrid.setData(firstGridData);
			firstGrid.setColumnSort({cr_rsrcname:{seq:0, orderBy:'asc'}});
		}
		//fileGrid.refresh(); //체크아웃후 새로운 데이터 안가져옴
		//changeFileGridStyle(firstGridData);
		//console.log(firstGrid);
	}
}

function clickSearchBtn() {
	alertFlag = 1;
	var getFileData = {};
	var rsrcname 	= null;
	
	if (gitSw && orgFirstGridData.length>0) {
		
		var filterData = orgFirstGridData;
		
		rsrcname = $('#txtRsrcName').val().trim();
		var txtProg = '';
		if(rsrcname.length>0) {	
			filterData = filterData.filter(function(data) {
				txtProg = data.cr_rsrcname;
				if (txtProg.indexOf(rsrcname)<0) return false;
				else return true;
			});
		}
		successGetFileGridData(filterData);
	} else {
		getFileData.UserId 	= userId;
		getFileData.SysCd 	= getSelectedVal('cboSys').value;
		getFileData.SysGb 	= getSelectedVal('cboSys').cm_sysgb;
		getFileData.DsnCd 	= "";
		getFileData.RsrcCd 	= "";
		getFileData.ReqCd 	= reqCd;
		getFileData.JobCd	= "0000";
		if($("#chkUpLow").is(":checked")) getFileData.UpLowSw = true;
		else getFileData.UpLowSw = false;
		if($("#chkSelf").is(":checked")) getFileData.selfSw = true;
		else getFileData.selfSw = false;
		if($("#chkLike").is(":checked")) getFileData.LikeSw = true;
		else getFileData.LikeSw = false;
		rsrcname = $('#txtRsrcName').val().trim();
		if(rsrcname === '' || rsrcname === undefined) {
			dialog.alert('검색단어를 입력한 후 검색하시기 바랍니다.');
			return;
		}else {
			getFileData.RsrcName = rsrcname;
		}
		
		searchMOD = 'B';
		getFileGridData(getFileData);
	}
}

function clickGitBtn() {
	alertFlag = 1;
	var getFileData = {};
	var gitData = new Object();
	
	firstGridData = [];
	firstGrid.setData([]);
	
	if (getSelectedIndex('cboJob')<1) {
		dialog.alert('업무를 선택하여 주시기 바랍니다.');
		return;
	}
	gitData.syscd = getSelectedVal('cboSys').value;
	gitData.sysgb = getSelectedVal('cboSys').cm_sysgb;
	gitData.jobcd = getSelectedVal('cboJob').cm_jobcd;
	gitData.userid = userId;
	gitData.reqcd = reqCd;
	

	var getFileData = {
		    gitData : gitData,
		requestType	: 'Gitlab_Export_ChgFile',
	}
	$('[data-ax5grid="first-grid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', getFileData, 'json',successGitlab_Export_ChgFile);	
	gitData = null;
}
function successGitlab_Export_ChgFile(data){
	
	$(".loding-div").remove();
	if (data == undefined || data == null) return;
	if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
		dialog.alert(data);
	} else {
		if (data.length == 1 && data[0].errmsg != null && data[0].errmsg != '' && data[0].errmsg != undefined) {
			dialog.alert(data[0].errmsg);
		} else {
			orgFirstGridData = data;
			successGetFileGridData(data);
		}
	}
}
function btnParaSearch_Click() {
	selectedGridItem = firstGrid.getList('selected');
	tmpArr = clone(selectedGridItem);
	
	if(selectedGridItem == undefined || selectedGridItem.length == 0) {
		dialog.alert('선택하지 않으셨습니다.');
		return;
	}
	
	//// 파라미터 연관조회 모달
	setTimeout(function() {
		paraSearchModal.open({
	        width: 1200,
	        height: 550,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ParaSearchModal.jsp"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	                mask.close();
	            }
	        }
		});
	}, 200);	
}

function closeLangInfo(flag, data) {
	tmpArr = [];
	var dupleChk = false;
	if(flag) {
		tmpArr = data;
		if(tmpArr != null) {
			data = new Object();
			data = {
				Itemid : tmpArr,
				requestType : 'cmr0100_ParaSearch_Search'
			}
			ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0100Servlet', data, 'json');
			
			for(var i = 0; i < ajaxReturnData.length; i++) {
				var tmpObj = new Object();
				tmpObj = clone(ajaxReturnData[i]);
	            tmpObj.sysgb = getSelectedVal('cboSys').cm_sysgb;
	            tmpObj.baseitemid = ajaxReturnData[i].cr_itemid;
	            tmpObj.cr_editcon = $('#txtSayu').val().trim();
	            tmpObj.cr_sayu = $('#txtSayu').val().trim();
	            tmpObj.cr_expday = $('#datStD').val().trim();
				if ($('#chkOverWrite').is(":checked")) tmpObj.overwrite = "7";
				else tmpObj.overwrite = "0"
					
				for (var j = 0; j < secondGridData.length; j++) {
					if (ajaxReturnData[i].cr_itemid == secondGridData[j].cr_itemid) {
						dupleChk = true;
						break;
					} else {
						dupleChk = false;
					}
				}
				
//				if(!dupleChk) ajaxReturnData[i].push(tmpObj);
				if(!dupleChk) secondGridData.push($.extend({},tmpObj, {__index: undefined}));
//				downFileList.splice(j,1);
				
				tmpObj = null;
			}
			secondGrid.setData(secondGridData);
		}
	}
}

function addDataRow() {
	var calcnt = 0;
	var vercnt = 0;
	var secondGridList = new Array;
	var firstGridSelected = firstGrid.getList("selected");
	var ajaxReturnData;
	
	alertFlag = 0;
	
	localFileDownYN = false; //로컬로 파일 다운 YN 초기화
	var todsnYN = false;
	var todsnNAME = '';
	
	var today	= getDate('DATE',0);
	var strDate	= replaceAllString($('#datStD').val(),'/','');
	if (today > strDate) {
		dialog.alert("반환예정일자가 현재일 이전입니다. 정확히 선택하여 주십시오.");
		return;
	}
	
	$(firstGridSelected).each(function(i){
		this.cr_sayu = $('#txtSayu').val();
		this.cr_expday = $('#datStD').val();
		if ($('#chkOverWrite').is(":checked")) this.overwrite = "7";
		else this.overwrite = "0"

		if (this.selected_flag == "1" || this.codename == "[프로그램정보 없음]") {
			dialog.alert("선택하신 " + this.cr_rsrcname + "는\n" + "프로그램 정보가 없습니다.");
			return false;
		}	
			
		if (this.selected_flag == "1" || this.cr_status != "0" || this.cr_nomodify != "0") {
			//continue;
			dialog.alert("선택하신 " + this.cr_rsrcname + "는\n" + this.cm_username + "님이 대여중입니다.");
			return false;
		}
		if (exlSw && this.errmsg != "정상") {
			//continue;
			return false;
		}
		
		if(this.unyoung == '1') return false;
		
		//RSCHKITEM	[27]-개발툴연계, [04]-동시적용항목CHECK, [47]-디렉토리기준관리
		if (this.cm_info.substr(26,1) == "1" || this.cm_info.substr(3,1) == "1" || this.cm_info.substr(46,1) == "1") {
			calcnt++;
		}	
		
		//RSCHKITEM	[45]-로컬에서개발, [38]-서버/로컬동시체크아웃
		if (this.cm_info.substr(44,1) == "1" || this.cm_info.substr(37,1) == "1") {

			localFileDownYN = true;//로컬로 파일 다운 여부

			if (this.pcdir1 == null || this.pcdir1 == "") {
				dialog.alert("로컬 홈디렉토리를 지정하지 않았습니다. 기본관리>사용자환경설정에서 홈디렉토리지정 후 처리하시기 바랍니다.");
				return;
			}
		}
		
		if (reqCd == "02"){
			if (this.cr_lstver == "sel"){
				vercnt++;
			}
			calcnt++;
		}
		
		if(this.filterData!=true){
			this.filterData = true;
			secondGridList.push($.extend({}, this, {__index: undefined}));
		}
	});
	
	firstGrid.clearSelect();	// 상위 그리드에 있는 데이터가 하단 그리드에 추가되면 상단 그리드에서 선택했던 체크박스 초기화
	
	if(secondGridList.length > 0 ) {
		if($('#txtSayu').val().trim().length < 1) {
			dialog.alert("사유를 입력하여 주시기 바랍니다.");
			$('#txtSayu').focus();
			return;
		}
		
		if (vercnt>0){				
			dialog.alert("버전을 선택하여 주십시요.");
			return;
		}
		
		if ((secondGrid.list.length + secondGridList.length) > 300){
			dialog.alert("300건까지 신청 가능합니다.");
			return;
		}
		
		if (secondGridList.length != 0){
			for(var i=0; i<secondGridList.length; i++) {
				secondGridList[i].baseitemid = secondGridList[i].cr_itemid;
				secondGridList[i].cr_editcon = $('#txtSayu').val();
				secondGridList[i].checked = true;
				secondGridList[i].selected = '1';
				secondGridList[i].chk_selected = "0";
				secondGridList[i].ckboxDisabled = true;
			}
			checkDuplication(secondGridList);
		}
		if (secondGrid.list.length > 0){
			$('#cboSys').ax5select('disable');
		}
	}
}

function successDownFileData(data){
	if (data == undefined || data == null) return;
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}
	if(data.length != 0 && data == 'ERR'){
		dialog.alert($('#btnReq').val+'목록 작성에 실패하였습니다.');
		return;
	}
	else{		
		$(data).each(function(){
			if(this.cr_itemid == 'ERROR'){
				dialog.alert('파일목록 에러 \n파일경로 : '+thiscm_dirpath);
				ingsw = false;
				return;
			}
		});
		
	}
	checkDuplication(data);
}

function deleteDataRow() {
	var secondGridSeleted = secondGrid.getList("selected");
	var originalData = null;
	
	$(secondGridSeleted).each(function(i){
		originalData = null;
		
		if (this.cr_itemid != this.baseitemid){
			for(var x=0; x<secondGrid.list.length; x++){
				if(secondGrid.list[x].cr_itemid == this.baseitemid || secondGrid.list[x].baseitemid == this.baseitemid){
					secondGrid.select(x,{selected:true} );
					originalData = secondGrid.list[x].baseitemid;
				}
			}
		}
		$(firstGrid.list).each(function(j){
			if( (firstGrid.list[j].cr_itemid == secondGridSeleted[i].cr_itemid && firstGrid.list[j].cr_rsrcname == secondGridSeleted[i].cr_rsrcname)
					|| (firstGrid.list[j].cr_itemid == originalData && originalData != null)){
				firstGrid.list[j].filterData = false;
				firstGrid.list[j].__disable_selection__ = '';
				firstGrid.list[j].selected_flag = "0";
				return false;
			}
		});
	});

	//하단그리드 original에서 선택한 데이터 제거
	$(secondGridSeleted).each(function(i){
		for (var k=0; k<secondGridData.length; k++) {
			if(secondGridData[k].cr_syscd == secondGridSeleted[i].cr_syscd
				&& (secondGridData[k].baseitemid == secondGridSeleted[i].cr_itemid || secondGridData[k].baseitemid == secondGridSeleted[i].baseitemid)){
				secondGridData.splice(k,1);
				k--;
			}
		}
	});

	secondGrid.removeRow("selected");
	firstGrid.repaint();
	//secondGridData = clone(secondGrid.list);
	
	if (secondGrid.list.length == 0){
		$('#cboSys').ax5select('enable');
		$('#btnReq').prop('disabled',true);
	} else {
		$('#btnReq').prop('disabled',false);
	}
}

function checkDuplication(downFileList) {
	var secondGridList = new Array;
	var tmpobj1 = {};
	var findSw = false;
	if(secondGridData.length > 0 ){
		$(secondGridData).each(function(i){
			if( secondGridData[i].cr_itemid == secondGridData[i].baseitemid){
				$(downFileList).each(function(j){
					if( secondGridData[i].cr_itemid == downFileList[j].cr_itemid){
						downFileList.splice(j,1);
						return false;
					}
				});
			}
		});
	}
	
	//firstGridData = firstGrid.getList();
	if(downFileList.length > 0) {
		$(downFileList).each(function(i){
			var currentItem = downFileList[i];
			
			if(currentItem.baseitemid == currentItem.cr_itemid){
				$(firstGrid.list).each(function(j){
					if(firstGrid.list[j].cr_itemid == currentItem.cr_itemid) {
						if (exlSw == true) {
						} else {
							secondGridList.push($.extend({}, downFileList[i], {__index: undefined}));
							firstGrid.list[j].__disable_selection__ = true;
							firstGrid.list[j].filterData = true;
						}
						return false;
					}
				});
			}
		});
	}
	exlSw = false;
	
//	if(secondGridData.length > 0) {
//		if ((secondGridData.length+downFileList.length) > 300 && localFileDownYN) {
//			dialog.alert("300건 이하로 선택 가능합니다.[추가가능건수:"+(300-secondGridData.length)+"]");
//			return;
//		}else{
//			firstGrid.setData(firstGridData);
//			firstGrid.repaint();
//			secondGrid.addRow(secondGridList);	
//		}
//	}else if (downFileList.length > 300 && localFileDownYN) {
//		dialog.alert("300건 이하로 선택 가능합니다.[추가가능건수:"+(300-downFileList.length)+"]");
//		return;
//	}else {
//		firstGrid.setData(firstGridData);
		firstGrid.repaint();
		secondGrid.addRow(secondGridList);	
//	}
	
	if(secondGrid.list.length > 0 ) {
		$('#cboSys').ax5select('disable');
		$('#btnReq').show();
		$('#btnReq').prop('disabled',false);
		
		secondGrid.list.forEach(function(requestFileGridData, requestFileGridDataIndex) {
			requestFileGridData.seq = requestFileGridDataIndex + 1; // grid row header 달기
		});
		
		tmpobj1 = null;
	}
	
	secondGridData = clone(secondGrid.list);
	simpleData();
}

/* 등록 버튼을 누른 경우 처리 이벤트 헨들러 */
function checkOutClick() {
	var tmpSayu = $('#txtSayu').val().trim();
	if (tmpSayu.length == 0){
		dialog.alert("신청할 파일을 입력하여 주십시오.");
		return;
	}

	if (reqSw) {
		dialog.alert("현재 처리중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	
	ckoConfirm();
}

function ckoConfirm(){
	var strQry = "";
	var strRsrcCd = "";
	var ajaxReturnData;
	strQry = reqCd;
	
	for(var x = 0; x < secondGridData.length; x++) {
		if(strRsrcCd != null && strRsrcCd != "") {
			if(strRsrcCd.indexOf(secondGridData[x].cr_rsrccd) < 0) {
				strRsrcCd = strRsrcCd + "," + secondGridData[x].cr_rsrccd;
			} else strRsrcCd = secondGridData[x].cr_rsrccd;
		}
	}

	var tmpData = {
		SysCd 	: getSelectedVal('cboSys').cm_syscd,
		ReqCd 	: reqCd,
		RsrcCd 	: strRsrcCd,
		UserId 	: userId,
		QryCd 	: strQry,
		requestType : 'confSelect'
	}	
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	
	if (ajaxReturnData == "X") {
		dialog.alert("로컬PC에서 파일을 전송하는 결재단계가 지정되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
	} else	if (ajaxReturnData == "C") {
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				confCall("Y");
			}
			else{
				confCall('N');
			}
		});
	} else if (ajaxReturnData == "Y") {
		confCall("Y");
    } else if (ajaxReturnData != "N") {
    	dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
    } else {
		confCall("N");
    }
}

function confCall(GbnCd){
	var ajaxReturnData;
	var etcRsrccd = [];
	var etcQryCd = [];
	var etcPgmType = [];
	var emgSw = "N";
	var tmpRsrc = "";
	
	strQry = reqCd;
	for (var x=0; x<secondGridData.length; x++) {
		if (tmpRsrc != null && tmpRsrc != "") {
			if (tmpRsrc.indexOf(secondGridData[x].cr_rsrccd) < 0)
                tmpRsrc = tmpRsrc + "," + secondGridData[x].cr_rsrccd;
		} else tmpRsrc = secondGridData[x].cr_rsrccd;
	}
	
	etcQryCd = strQry.split(",");
	etcRsrcCd = tmpRsrc.split(",");
	
	confirmInfoData = new Object();
	confirmInfoData.UserId = userId;
	confirmInfoData.ReqCd  = reqCd;
	confirmInfoData.SysCd  = getSelectedVal('cboSys').cm_syscd;
	confirmInfoData.RsrcCd = tmpRsrc;
	confirmInfoData.QryCd = reqCd;
	confirmInfoData.EmgSw = emgSw;
	confirmInfoData.PrjNo = '';
	if($("#chkEmg").is(":checked")){
		confirmInfoData.passok = "pass";
	} else {
		confirmInfoData.passok = "";
	}
	confirmInfoData.gyulcheck = '';
	
	confirmData = [];	
  	if (GbnCd == "Y") {//결재팝업
  		setTimeout(function() {
			approvalModal.open({
		        width: 850,
		        height: 550,
		        iframe: {
		            method: "get",
		            url: "../modal/request/ApprovalModal.jsp"
			    },
		        onStateChanged: function () {
		            if (this.state === "open") {
		                mask.open();
		            }
		            else if (this.state === "close") {
		            	if(confirmData.length > 0){
		            		requestCheckOut();
		            	}
		                mask.close();
		            }
		        }
			});
  		}, 200);
       
	} else if (GbnCd == "N") {
		var data = {
			UserId 	  : userId,
			SysCd 	  : getSelectedVal('cboSys').cm_syscd,
			ReqCd 	  : reqCd,
			RsrcCd 	  : tmpRsrc,
			PgmType   : '',
			EmgSw 	  : emgSw,
			PrjNo 	  : "",
			deployCd  : "0",
			QryCd 	  : reqCd,
			passok 	  : $("#chkEmg").is(":checked") ? "pass" : "",
			gyulcheck : ""
		}
		
		var tmpData = {
			paramMap	: data,
			requestType : 'Confirm_Info'
		}
		console.log('[Confirm_Info] ==>', tmpData);
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json');
		confirmData = ajaxReturnData;
		requestCheckOut();
	}
}

function requestCheckOut(){
	reqSw = true;

	var etcObj = {};
	etcObj.UserID = userId;
	etcObj.ReqCD  = reqCd;
	etcObj.ReqSayu = "";
	etcObj.cm_syscd = getSelectedVal('cboSys').cm_syscd;
	etcObj.cm_sysgb = getSelectedVal('cboSys').cm_sysgb;
	if(!$("#chkTool").is(":checked")) {
		etcObj.chktool = "Y";
	} else {
		etcObj.chktool = "N";		
	}
	
	var orderAC = [];
	var selItems = thirdGrid.getList('selected');
	for(var j=0; j<secondGridData.length; j++) {
		for(var i=0; i<selItems.length; i++) {
			var tmpObj = new Object();
			tmpObj.CC_ORDERID = selItems[i].CC_ORDERID;
			tmpObj.CC_REQSUB = selItems[i].CC_REQSUB;
			
				tmpObj.CR_ITEMID = secondGridData[j].cr_itemid;
			
			orderAC.push(tmpObj);
			tmpObj = null;
		}
	}
	
	var tmpData = {
		chkOutList  : secondGridData,
		etcData 	: etcObj,
		ConfList 	: confirmData,
		OrderList 	: orderAC,
		requestType : 'request_Check_Out'
	}
	console.log('[request_Check_Out] ==>', tmpData);
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json');

	reqSw = false;
	if (ajaxReturnData == undefined || ajaxReturnData == null) return;
	if (ajaxReturnData.indexOf('ERROR')>-1) {
		dialog.alert(ajaxReturnData);
		return;
	}
	if(ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR'){
		dialog.alert('에러가 발생하였습니다. 다시 신청해주세요.');
		return;
	}
	
	acptNo = ajaxReturnData;
	 
	//운영영역과 형상관리 소스 버전 불일치 (53:운영서버파일비교) 
	if(acptNo.length>12) {
		dialog.alert(acptNo);
		return;
	}
	fileSenderClose_sub();
}

function fileSenderClose_sub() {
	var findSw = false;
	
	for(var i = 0; i < secondGridData.length; i++) {
		if(secondGridData[i].cm_info.substr(44,1) == "1" || secondGridData[i].cm_info.substr(37,1) == "1") {
			findSw = true;
			break;
		}
	}
	
	if(findSw) {
		/*var ajaxReturnData = null;
		var tmpData = {
			AcptNo	: acptNo,
			requestType : 'svrFileMake'
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json');
		
		if (ajaxReturnData == "0") {
			var tmpData = {
				AcptNo:		acptNo,
				fileGb:		"G",
				requestType: 	'getProgFileList'
			}
			ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json');
			// active x 사용하여 로컬로 다운로드하는 로직.. 서버에서 처리 필요할듯?
			// 물어보고 진행..
			
		} else {
			dialog.alert(ajaxReturnData + "[조치 후 체크아웃 상세화면에서 재처리하시기 바랍니다.]");
			ckout_end();
		}*/
		
		ckout_end();
	} else {
		ckout_end();
	}
}

function ckout_end(){
	secondGrid.setData([]);
	secondGridData = [];
	
	firstGridData = [];
	firstGrid.setData(firstGridData);

	$('#cboSys').ax5select('enable');
	$('#txtRsrcName').val('');
	$('#btnReq').prop('disabled',true);
	outpos = "";
	
	dialog.alert('체크아웃 신청완료.');
	/*confirmDialog.confirm({
		msg: '체크아웃 신청완료! 상세 정보를 확인하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			openWindow('detail',acptNo, '');
		}
		findRefresh();
	});*/
}

function findRefresh(){
	/*if(searchMOD == "B"){//B:조회버튼 클릭시,  T:트리구조 클릭시
		clickSearchBtn();
	}else {
		if (ztree.getSelectedNodes(true).length == 0){
			return;
		}
		$('#'+ztree.getNodesByParam("id", ztree.getSelectedNodes(true)[0].id)[0].tId+'_a').click();
	}*/
	
	firstGridData = [];
	firstGrid.setData([]);
	secondGridData = [];
	secondGrid.setData([]);
	$('#cboSys').ax5select('enable');	
}

//그리드 리스트 클릭 퉅팁 미개발
function firstGridClick(){
	/*
	console.log(firstGrid.getList("selected"));
	if (firstGrid.getList("selected").selectedIndex < 0) {
		list1_grid = "";
		return;
	}
	list1_grid.toolTip = list1_grid.selectedItem.view_dirpath;
*/
}

//체크아웃상세보기
function simpleData() {
	if (secondGrid.list.length < 1) return;
	
	gridSimpleData = clone(secondGridData);
	if(secondGrid.list.length == 0){
		secondGridData = clone(secondGrid.list);
		return;
	}
	for(var i =0; i < gridSimpleData.length; i++){
		if(gridSimpleData[i].baseitemid != gridSimpleData[i].cr_itemid){
			gridSimpleData.splice(i,1);
			i--;
		}
	};
	if (!$('#chkDetail').is(':checked')){
		secondGrid.list = clone(gridSimpleData);
		secondGrid.repaint();
	}
	else{
		secondGrid.list = clone(secondGridData);
		secondGrid.repaint();
	}
}

//function fileTypeCheck(obj) {
//	pathpoint = obj.value.lastIndexOf('.');
//	filepoint = obj.value.substring(pathpoint+1,obj.length);
//	filetype = filepoint.toLowerCase();
//	if(filetype=='xls' || filetype=='xlsx') {
//		getTmpDir('99');
//	} else {
//		dialog.alert('엑셀(*.xls,*.xlsx) 파일만 업로드 가능합니다.');
//		parentObj  = obj.parentNode
//		node = parentObj.replaceChild(obj.cloneNode(true),obj);
//		return false;
//	}
//}
//
///*일괄체크아웃엑셀가져오기*/
//function getTmpDir(dirCdPram){
//	var ajaxReturnData = null;
//	dirCd = dirCdPram;
//	var tmpData = {
//		pCode :	dirCd,
//		requestType : 'getTmpDir'
//	}
//	ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
//	if(typeof(ajaxReturnData) == "object"){
//		ajaxReturnData = ajaxReturnData[0].cm_path;
//	}
//	getTmpPath(ajaxReturnData);
//}
//
//function getTmpPath(result){
//	if (dirCd == "99") {
//		tmpPath = result ;
//		dirCd = "F2";
//		
//		getTmpDir(dirCd);
//		
//	} else if (dirCd == "F2") {
//		startUpload(result);
//	}
//}
//
//function startUpload(strURL) {
//	var excelFileSub = $('#excelFile')[0].files[0];
//	var excelFile = null;
//	
//	//tmpPath = 'C:\\eCAMS\\webTmp\\'; // 테스트 임시경로
//	
//	// 파일 업로드 jsp 를 호출해야함
//	var formData = new FormData();
//	formData.append('fullName',tmpPath);
//	formData.append('fullpath',tmpPath+"/"+userId+"_excel.tmp");
//	formData.append('file',excelFileSub);
//	
//	// ajax
//    $.ajax({
//        url: homePath+'/webPage/fileupload/'+strURL,
//        type:'POST',
//        data:formData,
//        async:false,
//        cache:false,
//        contentType:false,
//        processData:false
//    }).done(function(response){
//    	onUploadCompleteData(response);
//    	
//    }).fail(function(xhr,status,errorThrown){
//    	dialog.alert('오류가 발생했습니다.\r 오류명 : '+errorThrown + '\r상태 : '+status);
//    }).always(function(){
//    	// file 초기화
//    	var agent = navigator.userAgent.toLowerCase();
//    	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
//    	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
//    	} else {
//    	    $("#excelFile").val("");
//    	}
//    });
//}
//
//function onUploadCompleteData(filePath){
//	var headerDef = new  Array();
//	headerDef.push("cr_rsrcname");
//	filePath = replaceAllString(filePath,"\n","");
//	console.log(filePath);
//	var tmpData = {
//		pathData : filePath,
//		headerDef: headerDef,
//		requestType: 'getArrayCollection'
//	}
//	ajaxAsync('/webPage/dev/CheckOutServlet', tmpData, 'json',successGetArrayCollection);	
//}
//
//function successGetArrayCollection(data){
//	if (data == undefined || data == null) return;
//	if (data.indexOf('ERROR')>-1) {
//		dialog.alert(data);
//		return;
//	}
//	excelAry = data;
//	for (var i=0;excelAry.length>i;i++) {
//		for (var j=i+1;excelAry.length>j;j++) {
//			if (excelAry[i].cr_rsrcname == excelAry[j].cr_rsrcname) {
//				excelAry.splice(i,1);
//				i--;
//				break;
//			}
//		}
//	}
//	alertFlag = 1;
//	
//	// ax5grid 컬럼추가하기 
//	firstGridColumns = firstGrid.columns;
//	if(firstGridColumns[2].key != 'errmsg'){
//		var excelDataColums = {key: 'errmsg', label: '체크결과',  width: '15%'};
//		firstGridColumns.splice(2, 0,excelDataColums);
//		firstGrid.config.columns = firstGridColumns;
//	}
//	firstGrid.setConfig();
//	secondGrid.setData([]);
//	secondGridData = [];
//	
//	var fileData = {};
//	fileData.SysCd = getSelectedVal('cboSys').value;
//	fileData.SysGb = getSelectedVal('cboSys').cm_sysgb;
//	fileData.ReqCd = reqCd;
//	var tmpData = {
//		fileList : excelAry,
//		baseInfoData: fileData,
//		requestType: 'getFileList_excel'
//	}
//	ajaxAsync('/webPage/dev/CheckOutServlet', tmpData, 'json',successGetFileList_excel);
//}

//function successGetFileList_excel(data){
//	if (data == undefined || data == null) return;
//	if (data.indexOf('ERROR')>-1) {
//		dialog.alert(data);
//		alertFlag = 0;
//		return;
//	}
//	
//	var tmpAry = data;
//	var tmpAry2 = new Array;
//	var tmpObj = {};
//	var tmpObj2 = {};
//	var errcnt = 0;
//	/*
//	tmpObj2 = excelAry[0];
//	tmpObj2.result = "처리결과";
//	excelAry.splice(0,0,tmpObj2);
//	*/
//	tmpObj2 = null;
//	for (var i=0;i<tmpAry.length;i++){
//		tmpObj = {};
//		tmpObj = tmpAry[i];
//		tmpObj2 = {};
//		tmpObj2 = excelAry[Number(tmpObj.linenum)];
//		tmpObj2.result = tmpObj.errmsg;
//		//excelAry.splice(Number(tmpObj.linenum),0,tmpObj2);
//		tmpObj2 = null;
//		tmpAry2.push(tmpObj);
//		tmpObj = null;
//	}
//	$(tmpAry2).each(function(i){
//
//		if(tmpAry2[i].errmsg == '정상'){
//			
//			tmpAry2[i].filterData = false;
//			tmpAry2[i].__disable_selection__ = '';
//		}
//		else{
//			tmpAry2[i].filterData = true;
//			tmpAry2[i].__disable_selection__ = true;
//		}
//	});
//
//	firstGrid.setData(tmpAry2);
//	firstGridData = tmpAry2;
//	tmpObj = null;
//	tmpObj2 = null;
//	if (tmpAry2.length == 0){
//		dialog.alert("정상으로 체크아웃 할 파일이 없습니다.");
//		alertFlag = 0;
//		return;
//	} else {
//		alertFlag = 2;
//		//selectall_checkbox.selected = true;
//		//select_grid1();
//	}
//}

function openWindow(type,acptNo,etcInfo) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_pop_'+acptNo) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_pop_'+acptNo;

    nWidth  = 1046;
	nHeight = 700;
    if (type === 'prog') {//프로그램정보;
		cURL = "/webPage/winpop/PopProgramInfo.jsp";
	} else if (type === 'detail') {//체크아웃상세
		cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	}  else if (type == 'R52') {
		cURL = "/webPage/winpop/PopSourceDiff.jsp";
	} else if (type == 'R54') {
		cURL = "/webPage/winpop/PopSourceDiffInf.jsp";
	} else if (type == 'orderlist') {
		cURL = "/webPage/winpop/PopOrderListDetail.jsp";
	} else {
		dialog.alert('window open - popup: invalid type ['+type+'] error');
		return;
	}
	
	var f = document.setReqData;
    f.user.value = userId;
    
	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	if (acptNo != '' && acptNo != null) {
		f.acptno.value	= acptNo;
	}
	if (etcInfo != '' && etcInfo != null) {
		if (type === 'prog' || type == 'R52' || type == 'R54') {
			f.itemid.value = etcInfo;
			if(type == 1) {
				f.syscd.value = firstGrid.getList("selected")[0].cr_syscd;
				f.jobcd.value = firstGrid.getList("selected")[0].cr_jobcd;
			}
		} else if (type == 'orderlist') {
			f.orderId.value = etcInfo;
		} else {
			f.etcinfo.value = etcInfo;
		} 
	}
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    if(type === 'detail') {
    	findRefresh();
    }
}


// 20221222
//엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir);
}

// 엑셀 파일 업로드시 파일 올릴 경로 가져오기 완료 AND 업로드할때 사용할 JSP 파일명 가져오기
function successGetTmpDir(data) {
	tmpPath = data;
	if(typeof(tmpPath) == "object"){
		tmpPath = data[0].cm_path;
	}

	var data = new Object();
	data = {
		pCode 		: 'F2',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetUploadJspName);
}

// 업로드할때 사용할 JSP 파일명 가져오기 완료
function successGetUploadJspName(data) {
	uploadJspFile = data;
	if(typeof(uploadJspFile) == "object"){
		uploadJspFile = data[0].cm_path;
	}
}

// 엑셀 파일 업로드시 파일타입 체크
function fileTypeCheck(obj) {
	var pathpoint = obj.value.lastIndexOf('.');
	var filepoint = obj.value.substring(pathpoint+1,obj.length);
	filetype = filepoint.toLowerCase();
	if(filetype=='xls' || filetype=='xlsx') {
		startUpload();
	} else {
		dialog.alert('엑셀 파일만 업로드 가능합니다.', function() {});
		parentObj  = obj.parentNode
		node = parentObj.replaceChild(obj.cloneNode(true),obj);
		return false;
	}
}

// 엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;

	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();

	//tmpPath = 'C:\\html5\\temp\\'; //테스트중
	formData.append('fullName',tmpPath+userId+"_excel_checkout.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);

    $.ajax({
		url: homePath+'/webPage/fileupload/'+uploadJspFile,
        type:'POST',
        data:formData,
        async:false,
        cache:false,
        contentType:false,
        processData:false
    }).done(function(response){
    	onUploadCompleteData(response);
    }).fail(function(xhr,status,errorThrown){
    	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
    }).always(function(){
    	// file 초기화
    	var agent = navigator.userAgent.toLowerCase();
    	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
    	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
    	} else {
    	    $("#excelFile").val("");
    	}
    });
}

//엑셀파일 완료 후
function onUploadCompleteData(filePath){
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");

	headerDef.push("dirpath");
	headerDef.push("rsrcname");
	
	tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

//읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}

	for (var i=0;data.length>i;i++) {
		for (var j=i+1;data.length>j;j++) {
			if (data[i].dirpath == data[j].dirpath && data[i].rsrcname == data[j].rsrcname) {
				data.splice(i,1);
				i--;
				break;
			}
		}
	}
	
	var tmpData = {
		fileList : data,
		SysCd : getSelectedVal('cboSys').cm_syscd,
		SysGb : getSelectedVal('cboSys').cm_sysgb,
		ReqCd : reqCd,
		UserId : userId,
		UplowSw : $("#chkUpLow").is(":checked") ? true : false,
		selfSw : false,
		requestType: 'getFileList_excel'
	}
	$('[data-ax5grid="first-grid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', tmpData, 'json',successGetFileList_excel);
}

function successGetFileList_excel(data){
	$(".loding-div").remove();
	console.log(data);
	
	if (data == undefined || data == null) return;
	if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
		dialog.alert(data);
	} else {
		firstGridData = data;
		firstGridData.forEach(function(item) {
			var verNum = Number(item.cr_lstver);
			if(verNum >= 0) {
				item.cr_lstver = verNum;
			}
		});

		//컬럼제거하기
		firstGridColumns = firstGrid.columns;
		if(firstGridColumns[2].key == 'errmsg'){
			firstGrid.removeColumn(2);
		}

		if( (null == firstGridData || firstGridData.length == 0) && alertFlag == 1){
			firstGrid.setData([]);
			dialog.alert('검색 결과가 없습니다.');
		} else {
			alertFlag = 0;
			
			$(firstGridData).each(function(i){
				$(secondGridData).each(function(j){
					if(secondGridData[j].cr_itemid == firstGridData[i].cr_itemid){
						firstGridData[i].selected_flag = '1';
					}
				})
				
				if(firstGridData[i].selected_flag == '1' || firstGridData[i].cr_status != '0' || firstGridData[i].cr_nomodify != '0'){
					firstGridData[i].filterData = true;
					firstGridData[i].__disable_selection__ = true;
				}
				else{
					firstGridData[i].filterData = false;
					firstGridData[i].__disable_selection__ = '';
				}
			});
			firstGrid.setData(firstGridData);
			firstGrid.setColumnSort({cr_rsrcname:{seq:0, orderBy:'asc'}});
		}
	}
}
