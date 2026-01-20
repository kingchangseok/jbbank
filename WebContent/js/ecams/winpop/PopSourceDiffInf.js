/** 프로그램정보 > 소스보기-연관프로그램 (eCmr5400.mxml) **/
var pReqNo  = null;
var pItemId = null;
var pUserId = null;

var grdDiffSrc     = new ax5.ui.grid();
var grdProgHistory = new ax5.ui.grid();
var grdProgHistory_Acptno = new ax5.ui.grid();

var grdProgHistoryData_Acptno = null; //버전비교 목록 
var grdProgHistoryData = []; //체크인목록그리드 데이타
var grdDiffSrcData     = [];
var diffSrcData        = [];
var selectedGridItem   = [];

var isAdmin 	   = false;
var ingSw          = false;
var tmpDir         = null;
var downURL        = null;
var diffGbn        = 'A';         
var svIdx          = 0;
var befVer         = 0;
var aftVer         = 0;
var cr_qrycd       = 0;
var cr_itemid      = 0;
var strLocal       = '';
var svrPort        = '';
var svrIp          = '';

var tmpInfo = new Object();
var tmpInfoData = new Object();

var f = document.getSrcData;

pReqNo = f.acptno.value;
pItemId = f.itemid.value;
pUserId = f.user.value;

grdProgHistory_Acptno.setConfig({
    target: $('[data-ax5grid="grdProgHistory_Acptno"]'),
    sortable: true, 
    multiSort: true,
    //showRowSelector: true,
    header: {
        align: "center",
        columnHeight: 28
    },
    body: {
        columnHeight: 26,
        onClick: function () {
           this.self.clearSelect();
           this.self.select(this.dindex);
           
           //grdProgHistory_Click();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{key: "isChecked",    label: "",     	width: '6%',   editor:{type:'checkbox',config: {trueValue: "Y", falseValue: "N"}},  align: 'center'},
		{key: "cr_ver",       label: "버전",     	width: '7%',   align: 'center'},
        {key: "cm_username",  label: "신청자",	width: '10%',  align: 'center'},
        {key: "acptno",       label: "신청번호",  	width: '13%',  align: 'center'},
        {key: "DATE1",        label: "신청일시",  	width: '13%',  align: 'center'},
        {key: "DATE2",        label: "적용일시",  	width: '13%',  align: 'center'},
        {key: "cr_sayu",      label: "신청사유",  	width: '40%',  align: 'left'}
    ]
});

grdProgHistory.setConfig({
    target: $('[data-ax5grid="grdProgHistory"]'),
    sortable: true, 
    multiSort: true,
    paging: false,
    page: false,
    //showRowSelector: true,
    header: {
        align: "center",
        columnHeight: 28
    },
    body: {
        columnHeight: 26,
        onClick: function () {
           this.self.clearSelect();
           this.self.select(this.dindex);
           
           btnVerDiff_click();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
		{key: "cr_rsrcname", label: "프로그램명",	width: '25%'},
        {key: "diff",        label: "비교",      width: '10%'},
        {key: "cr_aftver",   label: "후버전",		width: '12%'},
        {key: "cr_befver",   label: "전버전",		width: '12%'},
        {key: "cm_dirpath",  label: "경로",		width: '40%'}
    ]
});

grdDiffSrc.setConfig({
    target: $('[data-ax5grid="grdDiffSrc"]'),
    sortable: false, 
    multiSort: false,
    header: {
        align: "center",
        columnHeight: 28
    },
    body: {
        columnHeight: 25,
        onClick: function () {
           this.self.clearSelect();
           this.self.select(this.dindex);
           grdSrcDiff_Click();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
     	trStyleClass: function () {
     		if (this.item.file1diff != null) {
     			if (this.item.file1diff === 'RO' || this.item.file1diff === 'RN'){
         			return "fontStyle-error";
         		} else if (this.item.file1diff === 'RO'){
         			return "fontStyle-error";
         		} else if (this.item.file1diff === 'D '){
         			return "fontStyle-cncl";
         		} else if (this.item.file1diff === 'I '){
         			return "fontStyle-ing";
         		} 
     		}
     		if (this.item.file2diff != null) {
     			if (this.item.file2diff === 'RO' || this.item.file2diff === 'RN'){
         			return "fontStyle-error";
         		} else if (this.item.file2diff === 'RO'){
         			return "fontStyle-error";
         		} else if (this.item.file2diff === 'D '){
         			return "fontStyle-cncl";
         		} else if (this.item.file2diff === 'I '){
         			return "fontStyle-ing";
         		} 
     		}
     	},
    },
    columns: [
        {key: "file1", label: "변경전",  width: '50%',  align: 'left'
        	,
        	formatter: function(){
                var htmlStr = '<xmp>'+this.value+'</xmp>';
                return htmlStr;
             }
        },
        {key: "file2", label: "변경후",  width: '50%',  align: 'left'
        	,
        	formatter: function(){
                var htmlStr = '<xmp>'+this.value+'</xmp>';
                return htmlStr;
             }
        }
    ]
});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name^="optSysGbn"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$('#txtSearch').bind('keypress', function(event){
	if(event.keyCode==13) {
		$('#btnSearch').trigger('click');
	}
});

$(document).ready(function(){
	if (pUserId == null || pUserId == '' || pUserId == undefined || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	
	screenInit('M');
	
	//소스비교클릭
	$('#btnVerDiff').bind('click', function() {
		btnVerDiff_click();
	});

	//전체비교클릭
	$('#btnSrcDiff').bind('click', function() {
		btnSrcDiff_click();
	});
	
	//변경부분클릭
	$('#btnChgPart').bind('click', function() {
		btnChgPart_click();
	});

	//찾기클릭
	$('#btnSearch').bind('click', function() {
		btnSearch_click();
	});

	// 엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		excelExport(grdDiffSrc,$('#txtProgId').val()+"_SourceDiffInf.xls");
	});

	//닫기클릭
	$('#btnClose').bind('click', function() {
		window.close();
	});
	getTmpDir('99');
	
});

//활성화 비활성화 초기화로직
function screenInit(gbn){
	if (gbn == 'M') {
		grdProgHistory.setData([]);
		$('#txtProgId').val('');
		$('#txtDir').val('');
		grdProgHistory.repaint();	
	}
	grdDiffSrc.setData([]);
	grdDiffSrc.repaint();	
}

function getTmpDir(dirCd){
	var tmpInfoData = {
		      pCode : dirCd,
		requestType : 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}

function successeCAMSDir(data) {
	tmpDir = data;
	getProgHistory_Acptno();
}

function getProgHistory_Acptno(){
	var tmpInfoData = {
			ItemID  : pItemId,
			AcptNo  : pReqNo,
		requestType : 'getFileVer'
	}
	ajaxAsync('/webPage/ecmr/Cmr5400Servlet', tmpInfoData, 'json', successProgList_Acptno);
}

function successProgList_Acptno(data) {
	var firstSw = false;
	var secondSw = false;
	
	grdProgHistoryData_Acptno = data;
	grdProgHistory_Acptno.setData(grdProgHistoryData_Acptno);
	
	if (grdProgHistoryData_Acptno.length == 0) {
		dialog.alert('프로그램변경이력이 존재하지 않습니다.');
		return;
	}
	
	$('#txtSysMsg').val(grdProgHistoryData_Acptno[0].cm_sysmsg);
	$('#txtProgId').val(grdProgHistoryData_Acptno[0].cr_rsrcname);
	$('#txtDir').val(grdProgHistoryData_Acptno[0].cm_dirpath);
	
	for(var i=0; i<grdProgHistoryData_Acptno.length; i++) {
		if (grdProgHistoryData_Acptno[i].checked == 'true') {
			if (!firstSw) {
				firstSw = true;
				grdProgHistoryData_Acptno[i].isChecked = 'Y';
			} else {
				grdProgHistoryData_Acptno[i].isChecked = 'Y';
				secondSw = true;
			}
			if (firstSw && secondSw) break;
		}

		if (firstSw && secondSw) break;
	}	
	grdProgHistory_Acptno.setData(grdProgHistoryData_Acptno);
	getProgHistory();
}

function getProgHistory(){
	var selCnt = 0;
	var firstLine = 0;
	var secondLine = 0;
	var befVer = 0;
	var aftVer = 0;
	
	diffGbn = 'A';
	svIdx = -1;
	svWord = '';
	for(var i=0; i<grdProgHistoryData_Acptno.length; i++) {
		if (grdProgHistoryData_Acptno[i].isChecked == 'Y') ++selCnt;
		if (selCnt == 1) {
			firstLine = i;
		} else if (selCnt == 2) {
			secondLine = i;
		}
		if (selCnt > 2) break;
	}	
	
	if (selCnt != 2) {
		dialog.alert('2개의 변경이력을 선택한 후 진행하시기 바랍니다.');
		return;
	}
	
	if (befVer > 0 && aftVer > 0) {
		if (grdDiffSrcData.length > 0 && befVer == grdProgHistoryData_Acptno[secondLine].cr_version && aftVer == grdProgHistoryData_Acptno[firstLine].cr_version) {
			grdDiffSrc.setData(grdDiffSrcData);
			grdDiffSrc.repaint();
			return;
		}
	}
	befVer = grdProgHistoryData_Acptno[secondLine].cr_version;
	aftVer = grdProgHistoryData_Acptno[firstLine].cr_version;

	var aftAcpt = grdProgHistoryData_Acptno[firstLine].cr_acptno;
	var chkFg = 'false';
	
	if (grdProgHistoryData_Acptno[firstLine].cr_ver == null || grdProgHistoryData_Acptno[firstLine].cr_ver == '' || grdProgHistoryData_Acptno[firstLine].cr_ver == undefined) chkFg = 'true';
    
	tmpInfoData = new Object();
	tmpInfoData = {
			 ItemID	: grdProgHistoryData_Acptno[0].cr_itemid,
			befAcpt	: grdProgHistoryData_Acptno[secondLine].cr_acptno,
			 befQry	: grdProgHistoryData_Acptno[secondLine].cr_qrycd,
			aftAcpt	: grdProgHistoryData_Acptno[firstLine].cr_acptno,
			 aftQry	: grdProgHistoryData_Acptno[firstLine].cr_qrycd,
			  chkFg	: chkFg,
		requestType	: 'fileDiffInf'
	}
	console.log('fileDiffInf',tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr5400Servlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	var firstSw = false;
	var secondSw = false;
	
	grdProgHistoryData = data;
	grdProgHistory.setData(grdProgHistoryData);
	
//	if (grdProgHistoryData.length == 0) {
//		dialog.alert('프로그램변경이력이 존재하지 않습니다.');
//		return;
//	}
	
	grdProgHistory.setData(grdProgHistoryData);
	
	if (grdProgHistoryData.length>0) {
		grdProgHistory.focus(0);
		btnVerDiff_click();
	}
}

function btnVerDiff_click() {
	var selCnt = 0;
	var firstLine = 0;
	var secondLine = 0;
	var befVer = 0;
	var aftVer = 0;
	var selIdx = 0;
	
	if (grdProgHistoryData.length == 0) return;	
	
	
	if (befVer > 0 && aftVer > 0) {
		grdDiffSrc.setData(grdDiffSrcData);
		grdDiffSrc.repaint();
		return;
	}

	$('#btnSrcDiff').prop('disabled',true);
	$('#btnChgPart').prop('disabled',true);
	
	if(grdProgHistory.selectedDataIndexs != null && grdProgHistory.selectedDataIndexs != "" && grdProgHistory.selectedDataIndexs != undefined){
		selIdx = grdProgHistory.selectedDataIndexs;
	}

	var parmVer = '';
	var parmAcpt = '';
	var parmGbn = '';
	
	if (grdProgHistoryData[selIdx].ColorSw == 'A' || grdProgHistoryData[selIdx].ColorSw == 'D') {
		if (grdProgHistoryData[selIdx].ColorSw == 'A') {
			parmGbn = grdProgHistoryData[selIdx].ColorSw;
			if (grdProgHistoryData[selIdx].cr_aftver != null && grdProgHistoryData[selIdx].cr_aftver != '' && grdProgHistoryData[selIdx].cr_aftver != undefined) {
				parmVer = grdProgHistoryData[selIdx].cr_aftver;
			} else {
				parmAcpt = grdProgHistoryData[selIdx].cr_acptno;
			}
		} else {
			parmGbn = 'B';
			parmVer = grdProgHistoryData[selIdx].cr_befver;
		}
		tmpInfoData = new Object();
		tmpInfoData = {
				 ItemId	: grdProgHistoryData[selIdx].cr_itemid,
				Version	: parmVer,
				 AcptNo	: parmAcpt,
				  GbnCd	: parmGbn,
			requestType	: 'getDiffSingle'
		}
		ajaxAsync('/webPage/ecmr/Cmr5400Servlet', tmpInfoData, 'json', successDiffSingle);
	} else {
		diffProc(grdProgHistoryData[selIdx]);
	}
}

function diffProc(selData) {
	var parmVer1 = '';
	var parmVer2 = '';
	
	parmVer1 = selData.cr_befver;
	
	if(selData.cr_aftver == '' || selData.cr_aftver == null || selData.cr_aftver == undefined) {
		paramVer2 = selData.cr_acptno;
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
			UserID		: pUserId,
			ItemID1		: selData.cr_itemid,
			ver1		: parmVer1,
			ItemID2		: selData.cr_itemid,
			ver2		: parmVer2,
		requestType		: 'getDiffAry'
	}
	ajaxAsync('/webPage/ecmr/Cmr5200Servlet', tmpInfoData, 'json', successDiffList);
}

function successDiffSingle(data) {
	var delCnt = 0;
	var befCnt = 0;
	var addCnt = 0;
	var aftCnt = 0;
	$('#txtDelLine').val(String(delCnt));
	$('#txtBefLine').val(String(befCnt));
	$('#txtAftLine').val(String(aftCnt));
	$('#txtAddLine').val(String(addCnt));
	
	grdDiffSrc.setData(grdDiffSrcData);
	
	$('#btnSrcDiff').prop('disabled', true); 
	$('#btnChgPart').prop('disabled', true); 
	$('#btnLeft').prop('disabled', true); 
	$('#btnRight').prop('disabled', true); 
}

function successDiffList(data) {
	var delCnt = 0;
	var befCnt = 0;
	var addCnt = 0;
	var aftCnt = 0;
	
	grdDiffSrcData = data;
	diffSrcData = data;
	
	var startLine = '';
	for(var i=0; i<grdDiffSrcData.length; i++) {
		if (grdDiffSrcData[i].file1diff != null) {
			if (grdDiffSrcData[i].file1diff == 'D ') {
				if(startLine.length == 0) startLine = grdDiffSrcData[i].lineno;
				++delCnt;
			} else if (grdDiffSrcData[i].file1diff == 'RO') {
				if(startLine.length == 0) startLine = grdDiffSrcData[i].lineno;
				++befCnt;
			} 
		}
		if (grdDiffSrcData[i].file2diff != null) {
		    if (grdDiffSrcData[i].file2diff == 'I ') {
				if(startLine.length == 0) startLine = grdDiffSrcData[i].lineno;
				++addCnt;
			} else if (grdDiffSrcData[i].file2diff == 'RN') {
				if(startLine.length == 0) startLine = grdDiffSrcData[i].lineno;
				++aftCnt;
			}
		}
	}	
	
	diffSrcData = diffSrcData.filter(function(data) {
		if(data.file1diff != null && data.file1diff != '') return true;
		if(data.file2diff != null && data.file2diff != '') return true;
		return false;
	});
	
	$('#txtDelLine').val(String(delCnt));
	$('#txtBefLine').val(String(befCnt));
	$('#txtAftLine').val(String(aftCnt));
	$('#txtAddLine').val(String(addCnt));
	
	grdDiffSrc.setData(grdDiffSrcData);
	
	svIdx = -1;
	svWord = '';
	$('#btnSearch').text('찾기');
	
	if (diffSrcData.length == 0) {
		$('#btnSrcDiff').prop('disabled', true); 
		$('#btnChgPart').prop('disabled', true); 
	} else {
		$('#btnChgPart').prop('disabled',false);
	}
	
	$('#txtBefSrc').val('');
	$('#txtAftSrc').val('');
	
	if(startLine.length > 0 && $('[name="optradio"]:checked').val() == 'L') {
		$('#txtSearch').val(startLine);
		btnSearch_click();
	}
}

function grdSrcDiff_Click() {
	$('#txtBefSrc').val('');
	$('#txtAftSrc').val('');
	
	var selectedGridRow = grdDiffSrc.list[grdDiffSrc.selectedDataIndexs];
	if (selectedGridRow == null) return;
	
	$('#txtBefSrc').val(selectedGridRow.file1);
	$('#txtAftSrc').val(selectedGridRow.file2);
}

//전체비교
function btnSrcDiff_click() {	
	
	$('#btnSrcDiff').prop('disabled',true);
	if (diffSrcData.length > 0) $('#btnChgPart').prop('disabled',false);
	
	diffGbn = 'A';
	grdDiffSrc.setData(grdDiffSrcData);
	grdDiffSrc.repaint();

	svIdx = -1;
	svWord = '';
	
}

//변경부분
function btnChgPart_click() {
	
	$('#btnChgPart').prop('disabled',true);
	$('#btnSrcDiff').prop('disabled',false);
	diffGbn = 'D';
	grdDiffSrc.setData(diffSrcData);
	grdDiffSrc.repaint();

	svIdx = -1;
	svWord = '';
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
}

function btnLeft_click() {	
	
	var i = 0;
	
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
	grdDiffSrc.clearSelect();

	svWord = '';
	if (diffGbn == 'A') {
		if (svIdx <= 0) svIdx = grdDiffSrcData.length;
		--svIdx;

		for(i=svIdx;i>=0; i--) {
			if (grdDiffSrcData[i].file1diff != null && grdDiffSrcData[i].file1diff != '') {
				svIdx = i;
				break;
			}
			if (grdDiffSrcData[i].file2diff != null && grdDiffSrcData[i].file2diff != '') {
				svIdx = i;
				break;
			}
		}	
	}
	grdDiffSrc.select(svIdx);
	grdDiffSrc.focus(svIdx);
	
}

function btnRight_click() {	
	
	var i = 0;
	
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
	svWord = '';
	grdDiffSrc.clearSelect();
	if (diffGbn == 'A') {
		++svIdx;
		if (grdDiffSrcData.length<=svIdx) svIdx = 0;
		for(i=svIdx; i<grdDiffSrcData.length; i++) {
			if (grdDiffSrcData[i].file1diff != null && grdDiffSrcData[i].file1diff != '') {
				svIdx = i;
				break;
			}
			if (grdDiffSrcData[i].file2diff != null && grdDiffSrcData[i].file2diff != '') {
				svIdx = i;
				break;
			}
		}	
	}
	grdDiffSrc.select(svIdx);
	grdDiffSrc.focus(svIdx);
	
}

function optradio_change() {
	txtSearch_change();
}

function txtSearch_change() {
	svIdx = -1;
	svWord = '';

	$('#btnSearch').text('찾기');
}

function btnSearch_click() {
	//console.log('txtSearch_change'+$('#txtSearch').val());
	var strWord = $('#txtSearch').val().trim();
	var findSw = false;
	var rowMsg = '';
	
	if (strWord == null || strWord.length == 0) {
		dialog.alert('검색 할 단어/라인을 입력하여 주시기 바랍니다.', function(){});
		return;
	}
	if (svWord == '' || svWord != strWord) svIdx = -1;
	++svIdx;

	grdDiffSrc.clearSelect();
	
	//console.log('svidx='+svIdx);
	svWord = strWord;
	var searchGbn = $('[name="optradio"]:checked').val();
	if (diffGbn == 'A') {
		if (searchGbn == 'W') {
			for(i=svIdx; i<grdDiffSrcData.length; i++) {
				rowMsg = grdDiffSrcData[i].file1;
				if (rowMsg.length > 0) {
					if (rowMsg.indexOf(svWord)>=0) {
						svIdx = i;
						findSw = true;
						break;
					}
				}
				rowMsg = grdDiffSrcData[i].file2;
				if (rowMsg.length > 0) {
					if (rowMsg.indexOf(svWord)>=0) {
						svIdx = i;
						findSw = true;
						break;
					}
				}
			}
			if (findSw) $('#btnSearch').text('다음');
		} else {
			svIdx = Number(svWord);
			if (svIdx <= grdDiffSrcData.length) {
				--svIdx;
				findSw = true;
			}
		}
	} else {
		if (diffSrcData.length<=svIdx) svIdx = 0;

		if (searchGbn == 'W') {
			for(i=svIdx; i<diffSrcData.length; i++) {
				rowMsg = diffSrcData[i].file1;
				if (rowMsg.length > 0) {
					if (rowMsg.indexOf(svWord)>=0) {
						svIdx = i;
						findSw = true;
						break;
					}
				}
				rowMsg = diffSrcData[i].file2;
				if (rowMsg.length > 0) {
					if (rowMsg.indexOf(svWord)>=0) {
						svIdx = i;
						findSw = true;
						break;
					}
				}
			}
		} else {
			svIdx = Number(svWord);
			if (svIdx <= diffSrcData.length) {
				--svIdx;
				findSw = true;
			}
		}
	}
	if (!findSw) {
		svIdx = -1;
		svWord = '';
		dialog.alert('더 이상 검색내용이 없습니다.');
		if(searchGbn == 'W') $('#btnSearch').text('찾기');
		return;
	}
	grdDiffSrc.select(svIdx);
	grdDiffSrc.focus(svIdx);
	
	if(searchGbn == 'L') {
		svIdx = -1;
		svWord = '';
	}
}