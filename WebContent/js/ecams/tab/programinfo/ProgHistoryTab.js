var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;

var grdProgHistory	= new ax5.ui.grid();   //프로그램그리드

var selOptions 		= [];

var cboReqData		   = null;	//신청구분 데이터
var progInfoData       = null;
var myWin 			   = null;
var pUserId            = null;
var grdProgHistoryData = null;

var selSw = false;

var tmpInfo = new Object();
var tmpInfoData = new Object();

var completeReadyFunc = false;


$(document).ready(function(){
	createViewGrid();
	
	getCodeInfo();
	
	//변경이력조회
	$('#btnQry2').bind('click',function() {
		btnQry2_Click();
	});

	$('#btnExcel').bind('click',function() {
		excelExport(grdProgHistory,"Program.xls");
	});
	
	completeReadyFunc = true;
});

function upFocus() {
	grdProgHistory.focus('HOME');
}

function createViewGrid() {
	
	grdProgHistory	= new ax5.ui.grid();
	grdProgHistory.setConfig({
	    target: $('[data-ax5grid="grdProgHistory"]'),
	    sortable: true, 
	    multiSort: true,
	    showLineNumber: true,
	    paging : false,
	    header: {
	        align: 'center'
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	        onDBLClick: function () {
	        	if (this.dindex < 0) return;
	     		openWindow(this.item.qrycd, this.item.SubItems8,'');
	        },
	    	trStyleClass: function () {
	    		if (this.item.SubItems9 === '3'){
	    			return 'fontStyle-cncl';
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
	             {type: 1, label: "변경신청상세"}
	         ],
	         popupFilter: function (item, param) {
	         	/** 
	         	 * return 값에 따른 context menu filter
	         	 * 
	         	 * return true; -> 모든 context menu 보기
	         	 * return item.type == 1; --> type이 1인 context menu만 보기
	         	 * return item.type == 1 | item.type == 2; --> type 이 1,2인 context menu만 보기
	         	 * 
	         	 * ex)
		            	if(param.item.qrycd2 === '01'){
		            		return item.type == 1 | item.type == 2;
		            	}
	         	 */
	          	grdProgHistory.clearSelect();
	          	grdProgHistory.select(Number(param.dindex));

	        	var selIn = grdProgHistory.selectedDataIndexs;
	        	if(selIn.length === 0) return;
	        	 
	         	if (param.item == undefined) return false;
	         	if (param.dindex < 0) return false;
	         	
	         	return item.type == 1;
	         },
	         onClick: function (item, param) {
				openWindow(item.qrycd, param.item.SubItems8,'');
				grdProgHistory.contextMenu.close();
			}
		},
	    columns: [
	        {key: 'SubItems1', 	    label: '신청일시',  	width: '12%',	align: 'center'},
	        {key: 'SubItems2',      label: '신청인',   	width: '9%',	align: 'center'},
	        {key: 'SubItems3', 	    label: '신청구분',    	width: '13%',	align: 'left'},
	        {key: 'SubItems4', 	    label: '신청번호',	  	width: '11%',	align: 'center'},
	        {key: 'SubItems6',   	label: '완료일시',  	width: '12%',	align: 'center'},
	        {key: 'SubItems7', 	    label: '변경사유',   	width: '20%',	align: 'left'}
	    ]
	});
	
	if (grdProgHistoryData != null && grdProgHistoryData.length > 0) {
		grdProgHistory.setData(grdProgHistoryData);
	}
}

function screenInit(gbn,userId) {
	pUserId = userId;
	
	$('#txtProgId2').val('');
	$('#txtStory2').val('');
	
	$('#btnQry2').prop('disabled', true);	
	
	if (cboReqData != null && cboReqData.length > 0) {
		$('[data-ax5select="cboReq"]').ax5select('setValue', 	   '0', 	true); 	// 신청구분 초기화
	}
}

//신청구분 cbo  가져오기
function getCodeInfo(){
	
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('REQUEST','ALL','N','3',''),
	]);
	selOptions	= codeInfos.REQUEST;
	cboReqData  = [];
	
	selOptions = selOptions.filter(function(data) {
		if(data.cm_micode > '10') return false;
		else return true;
	});
	
	if (selOptions.length > 0) {		
		$.each(selOptions,function(key,value) {
			cboReqData.push({value: value.cm_micode, text: value.cm_codename});
		});
		
		$('[data-ax5select="cboReq"]').ax5select({
	        options: cboReqData
		});
		
		$('[data-ax5select="cboReq"]').ax5select('setValue', 	'00', 	true); 	// 신청구분 초기화
	}
}

function successProgInfo(data) {
	var strInfo = '';
	progInfoData = data;
	
	if (progInfoData != null && progInfoData.length > 0) {
		$('#btnQry2').prop('disabled',false);
		$('#txtProgId2').val(progInfoData[0].cr_rsrcname);
		$('#txtStory2').val(progInfoData[0].Lbl_ProgName);	
		
		getHistoryList(progInfoData[0].cr_syscd,progInfoData[0].WkJobCd,progInfoData[0].cr_itemid,'ALL');
	}
}

function getHistoryList(sysCd,jobCd,itemId,reqCd){
	grdProgHistoryData = [];
	grdProgHistory.setData(grdProgHistoryData);
	grdProgHistory.clearSelect();

	tmpInfoData = new Object();
	tmpInfoData = {
		UserId 	  : pUserId,
		L_SysCd   : sysCd,
		L_ItemId  : itemId,
		L_JobCd   : jobCd,
		Cbo_ReqCd : reqCd,
		requestType	: 'getSql_Qry_Hist'
	}
	//getSql_Qry_Hist(String UserId,String L_SysCd,String L_JobCd,String Cbo_ReqCd, String L_ItemId)
	$('[data-ax5grid="approGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	console.log('getSql_Qry_Hist',tmpInfoData);
	ajaxAsync('/webPage/ecmd/Cmd0500Servlet', tmpInfoData, 'json', successHistory);
}

function successHistory(data) {
	$(".loding-div").remove();
	grdProgHistoryData = data;
	grdProgHistory.setData(grdProgHistoryData);
	if (grdProgHistoryData.length > 0) {
		grdProgHistory.select(0);
	}
}

function btnQry2_Click() {
	if (progInfoData.length > 0) {
		$('#btnQry2').prop('disabled',false);
		
		if (getSelectedIndex('cboReq')<1) {
			getHistoryList(progInfoData[0].cr_syscd,progInfoData[0].WkJobCd,progInfoData[0].cr_itemid,'ALL');
		} else {
			getHistoryList(progInfoData[0].cr_syscd,progInfoData[0].WkJobCd,progInfoData[0].cr_itemid,getSelectedVal('cboReq').value);
		}
	}
}

function openWindow(reqCd,reqNo,itemId) {
	var nHeight, nWidth, cURL, winName;

	if ( ('proginfo_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = 'proginfo_'+reqCd;

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= pUserId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value	= itemId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)

    nHeight = screen.height - 300;
    nWidth  = screen.width - 400;
    
    if(reqNo.substr(4,2) == '01' || reqNo.substr(4,2) == '02' || reqNo.substr(4,2) == '11') {
    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
    }else {
    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
    }
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    
}