/** 작업계획서만 작성
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 정호윤
 * 	버전 : 1.1
 *  수정일 : 2021-05
 */

var userName = window.top.userName;
var adminYN = window.top.adminYN;
var codeList = window.top.codeList;//전체 코드 리스트
var userId = window.top.userId;
var reqCd = window.top.reqCd;

var srData	     = null;	//SR-ID 콤보
var selCboISR    = null;
var cboOptions   = [];
var tab8         = null;

//ISRID
$('[data-ax5select="cboSrId"]').ax5select({
    options: []
});
$('#btnReqInfo').prop('disabled',true);
$('#btnChgInfo').prop('disabled',true);
$('#btnTestInfo').prop('disabled',true);

$(document).ready(function() {
	
	$('#tabWorkPlan').width($('#tabWorkPlan').width()+10);
	$("#tabWorkPlan").show();

	// 요구관리정보
	$('#btnReqInfo').bind('click', function() {
		popOpen("REQ");
	});
	// 변경관리정보
	$('#btnChgInfo').bind('click', function() {
		popOpen("CHG");
	});
	// 테스트관리정보
	$('#btnTestInfo').bind('click', function() {
		popOpen("TEST");
	});
	//ISR선택
	$('#cboSrId').bind('change',function(){
		changeSrId();
	});
	
	getSrIdCbo();
});
//SR정보 조회
function getSrIdCbo() {
	var prjInfo;
	var prjInfoData;
	prjInfo 		= new Object();
	prjInfo.userid 	= userId;
	prjInfo.reqcd 	= reqCd;
	prjInfo.qrygbn 	= '01';
	prjInfo.secuyn 	= 'Y';
	prjInfoData = new Object();
	prjInfoData = {
		prjInfoData		: 	prjInfo,
		requestType	: 	'getPrjList_Chg'
	}
	ajaxAsync('/webPage/common/ComPrjInfo', prjInfoData, 'json', successGetPrjInfoList);
	prjInfo = null;
	prjInfoData = null;
}
//SR-ID정보
function successGetPrjInfoList(data) {
	cboOptions = [];
	if ( data != undefined && data != null ) {
		if (data.indexOf("ERROR")>-1) {
			dialog.alert(data);
		} else {
			srData = data;
			$.each(srData,function(key,value) {
				cboOptions.push({value: value.cc_isrid, text: value.isridtitle, syscd:value.syscd, cc_isrid : value.cc_isrid
					           , isridtitle : value.isridtitle, cc_docno : value.cc_docno , reqdate : value.reqdate, reqdept : value.reqdept
					           , requser : value.requser, reqsta1 : value.reqsta1, reqsta2 : value.reqsta2
					           , cc_isrsub : value.cc_isrsub, maintab : value.maintab});
			});
		}
	}
	$('[data-ax5select="cboSrId"]').ax5select({
		options: cboOptions
	});
	if ( cboOptions.length > 0 ) { 
		changeSrId();
	}
}
//SR-ID 선택
function changeSrId() {
	tab8 = $('#frmWorkPlan').get(0).contentWindow; // 작업계획서
	tab8.strIsrId = "";
	tab8.strIsrSub = "";
	tab8.strUserId = "";
	tab8.popsw = false;
	tab8.strReqCd = reqCd;
	
	tab8.$('#txtISRID').val('');
	tab8.$('#txtDOCNO').val('');
	tab8.$('#txtREQDT').val('');
	tab8.$('#txtREQDEPT').val('');
	tab8.$('#txtEDITOR').val('');
	tab8.$('#txtSTATUS').val('');
	tab8.$('#txtPROSTATUS').val('');

	tab8.$('#txtJOBRECORD').val('');
	tab8.$('#txtJOBDETAIL').val('');
	tab8.$('#txtREFMSG').val('');
//	tab8.$('#grdFile').setData([]);
//	tab8.$('#cmdSave').enabled = false;

	$('#btnReqInfo').prop('disabled',true);
	$('#btnChgInfo').prop('disabled',true);
	$('#btnTestInfo').prop('disabled',true);
	selCboISR = null;
	
	if ( getSelectedVal('cboSrId') == null ) return;			
	selCboISR = getSelectedVal('cboSrId');
	
	tab8 = $('#frmWorkPlan').get(0).contentWindow; // 작업계획서
	tab8.strIsrId = selCboISR.cc_isrid;
	tab8.strIsrSub = selCboISR.cc_isrsub;
	tab8.strUserId = userId;
	tab8.strReqCd = reqCd;
	tab8.$('#txtISRID').val( selCboISR.isridtitle );
	tab8.$('#txtDOCNO').val( selCboISR.cc_docno );
	tab8.$('#txtREQDT').val( selCboISR.reqdate );
	tab8.$('#txtREQDEPT').val( selCboISR.reqdept );
	tab8.$('#txtEDITOR').val( selCboISR.requser );
	tab8.$('#txtSTATUS').val( selCboISR.reqsta1 );
	tab8.$('#txtPROSTATUS').val( selCboISR.reqsta2 );
	tab8.screenInit('M');
	tab8.jobCall();
	
	if ( selCboISR != null && selCboISR.maintab != undefined && selCboISR.maintab.indexOf("R")>=0 ) $('#btnReqInfo').prop('disabled',false);
	if ( selCboISR != null && selCboISR.maintab != undefined && selCboISR.maintab.indexOf("C")>=0 ) $('#btnChgInfo').prop('disabled',false);
	if ( selCboISR != null && selCboISR.maintab != undefined && selCboISR.maintab.indexOf("T")>=0 ) $('#btnTestInfo').prop('disabled',false);
}
//팝업
function popOpen(status){
	var popName = null;
	if( selCboISR == null ) { 
		return;
	}
	if(status === 'REQ') { 
		popName = 'PopReq'
	} else if(status ==='CHG') {
		popName = 'PopChg'
	} else {
		popName = 'PopTest'
	}
	var winName = popName + 'Info';
	var f = document.popPam;   		
    
	f.user.value 	= userId;    	
	f.code.value	= status;  
	f.redcd.value   = "";	
	f.isrId.value   = selCboISR.cc_isrid + '-' + selCboISR.cc_isrsub;
    
	nHeight = 740;
    nWidth  = 1200;

	cURL = "/webPage/winpop/" + popName + "Info.jsp";
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}