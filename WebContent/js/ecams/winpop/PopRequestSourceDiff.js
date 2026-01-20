/** 신청상세 > 소스비교 (eCmr6100.mxml) **/

var pReqNo  = null;
var pItemId = null;
var pUserId = null;

var grdDiffSrc     = new ax5.ui.grid();
var grdProgHistory = new ax5.ui.grid();

var grdProgHistoryData = null; //체크인목록그리드 데이타
var grdDiffSrcData     = null;
var diffSrcData        = null;
var selectedGridItem   = null;

var isAdmin 	   = false;
var ingSw          = false;
var tmpDir         = null;
var downURL        = null;
var diffGbn        = 'A';         
var svIdx          = 0;
var chgLine        = -1;
var befVer         = 0;
var aftVer         = 0;
var cr_qrycd       = 0;
var cr_itemid      = 0;

var tmpInfo = new Object();
var tmpInfoData = new Object();

var f = document.getSrcData;
pReqNo = f.acptno.value;
pItemId = f.itemid.value;
pUserId = f.user.value;

$('#txtAcptno').val(pReqNo.substr(0,4)+'-'+pReqNo.substr(4,2)+'-'+pReqNo.substr(6));

baseColumns = [
	{key: "cr_rsrcname",	label: "프로그램명",	width: '30%', align: 'left'},
    {key: "cr_befver",  	label: "변경전",		width: '10%'},
    {key: "cr_aftver", 		label: "변경후",		width: '10%'},
    {key: "cm_codename", 	label: "구분",		width: '10%'},
    {key: "cm_dirpath",    	label: "프로그램경로",	width: '20%', align: 'left'},
]

grdProgHistory.setConfig({
    target: $('[data-ax5grid="grdProgHistory"]'),
    sortable: true, 
    multiSort: true,
    page: false,
    header: {
        align: "center",
        columnHeight: 28
    },
    body: {
        columnHeight: 26,
        onClick: function () {
           this.self.clearSelect();
           this.self.select(this.dindex);
           grdProgHistory_click(this.item);
        },
        onDBLClick: function () {
        //	btnVerDiff_click();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: baseColumns
});

grdDiffSrc.setConfig({
    target: $('[data-ax5grid="grdDiffSrc"]'),
    sortable: false, 
    multiSort: false,
    showLineNumber: false,
    paging: false,
    page: false,
    lineNumberColumnWidth:46,
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
    	{key: "lineno", label: "NO", 	width: 46,  	align: 'center'},
        {key: "file1", 	label: "변경전", 	width: '48%',  	align: 'left',
        	formatter: function(){
                var htmlStr = '<xmp>'+this.value+'</xmp>';
                return htmlStr;
             }
        },
        {key: "file2", 	label: "변경후",  width: '48%',  	align: 'left',
        	formatter: function(){
                var htmlStr = '<xmp>'+this.value+'</xmp>';
                return htmlStr;
             }
        }
    ]
});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

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
	if (pReqNo == null || pReqNo == '' || pReqNo == undefined || pReqNo.length != 12) {
		dialog.alert('신청번호가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	screenInit('M');
	
	//소스비교클릭
	$('#btnVerDiff').bind('click', function() {
		if (grdProgHistory.selectedDataIndexs == undefined || grdProgHistory.selectedDataIndexs == null || grdProgHistory.selectedDataIndexs == '') return;
		
		grdProgHistory_click(grdProgHistoryData[grdProgHistory.selectedDataIndexs]);
	});

	//변경부분클릭
	$('#btnSrcDiff').bind('click', function() {
		btnSrcDiff_click();
	});
		
	//왼쪽화살표클릭
	$('#btnLeft').bind('click', function() {
		btnLeft_click();
	});

	//오른쪽화살표클릭
	$('#btnRight').bind('click', function() {
		btnRight_click();
	});

	//찾기클릭
	$('#btnSearch').bind('click', function() {
		btnSearch_click();
	});

	// 엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		excelExport(grdDiffSrc,pReqNo+"_RequestSourceDiff.xls");
	});
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		close();
	});
	
	getTmpDir('99');
	
	new ResizeSensor($('#girdDiv'), function() { // div 리사이징 감지 이벤트
		var girdHeight = $('#girdDiv').height();
		grdDiffSrc.setHeight(girdHeight);
	});
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
		pCode: 	dirCd,
		requestType: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}
function successeCAMSDir(data) {
	tmpDir = data;
	getProgHistory();
}
function getProgHistory(){
	
	var tmpInfoData = {
		      gbnCd: "D",
		     AcptNo: pReqNo,
		requestType: 'getReqList'
	}
//	console.log('getProgHistory tmpInfoData:',tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr5300Servlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	var sameSw = false;
	
	if (data == null || data == '' || data == undefined) return;
	if (typeof(data) == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
//	console.log('grdProgHistory successProgList data:',data);
	grdProgHistoryData = data;
	
	if (grdProgHistoryData.length == 0) {
		dialog.alert('프로그램변경이력이 존재하지 않습니다.');
		return;
	}
	
	grdProgHistory.config.columns = baseColumns;
	grdProgHistory.setConfig();
	
	grdProgHistory.setData(grdProgHistoryData);
	if (grdProgHistoryData.length>0) {
		grdProgHistory.select(0);
		grdProgHistory.focus(0);
	}
	
	var gridSelectedIndex = grdProgHistory.selectedDataIndexs;
	var selectedGridItem = grdProgHistory.list[grdProgHistory.selectedDataIndexs];
	
	grdProgHistory_click(grdProgHistoryData[0]);
}

function btnVerDiff_click() {
	diffGbn = 'A';
	grdDiffSrc.setData(grdDiffSrcData);
	grdDiffSrc.repaint();

	svIdx = -1;
	svWord = '';
	chgLine = -1;
	$('#btnSearch').text('찾기');
	if (diffSrcData.length == 0) {
		$('#btnLeft').prop('disabled',true);
		$('#btnRight').prop('disabled',true);
		$('#btnSrcDiff').prop('disabled',true);
	} else {
		$('#btnLeft').prop('disabled',false);
		$('#btnRight').prop('disabled',false);
		$('#btnSrcDiff').prop('disabled',false);
	}
}

function grdProgHistory_click(selGridHistory) {
	diffSrcData = [];
	grdDiffSrcData = [];
	grdDiffSrc.setData(grdDiffSrcData);
	
	svIdx = -1;
	svWord = '';
	
	if(selGridHistory == null){
		return;
	}
	
	var tmpObj = new Object();
	var befVer = '';
	var aftVer = '';
	var gbnCd = 'A';
	if (selGridHistory.cr_qrycd == '03' || selGridHistory.cr_qrycd == '05' || selGridHistory.cr_qrycd == '09') {
		if (selGridHistory.cr_qrycd == '03') {
			if (selGridHistory.cr_status == '0') aftVer = pReqNo;
			else befVer = selGridHistory.cr_ver;
		} else {
			befVer = selGridHistory.cr_befver;
			gbnCd = 'B';
		}
		tmpInfoData = {
			ItemId		: selGridHistory.cr_itemid,
			Version		: befVer,
			AcptNo		: aftVer,
			GbnCd		: gbnCd,
			requestType	: 'getDiffSingle'
		}
		$('[data-ax5grid="grdDiffSrc"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmr/Cmr5400Servlet', tmpInfoData, 'json', successDiffList);
	} else {
		if (selGridHistory.cr_status == '0') aftVer = pReqNo;
		else aftVer = selGridHistory.cr_ver;
		tmpInfoData = {
				 UserID : pUserId,
				ItemID1	: selGridHistory.cr_itemid,
				ItemID2	: selGridHistory.cr_itemid,
			       ver1	: selGridHistory.cr_befver,
			       ver2	: aftVer,
			requestType	: 'getDiffAry'
		}
		$('[data-ax5grid="grdDiffSrc"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmr/Cmr5200Servlet', tmpInfoData, 'json', successDiffList);
	}
}

function successDiffList(data) {
	$(".loding-div").remove();
	var delCnt = 0;
	var befCnt = 0;
	var addCnt = 0;
	var aftCnt = 0;
	var startLine = -1;

	if (typeof(data) == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	
	data.forEach(function(item, index) {
		if(item.file1 == undefined) item.file1 = '';
		if(item.file2 == undefined) item.file2 = '';
	});	
	
	grdDiffSrcData = data;
	diffSrcData = data;
	diffGbn = 'A';
	for(var i=0; i<grdDiffSrcData.length; i++) {
		if (grdDiffSrcData[i].file1diff != null) {
			if (grdDiffSrcData[i].file1diff == 'D ') {
				++delCnt;
				if (startLine<0) startLine = i;
			} else if (grdDiffSrcData[i].file1diff == 'RO') {
				if (startLine<0) startLine = i;
				++befCnt;
			} 
		}
		if (grdDiffSrcData[i].file2diff != null) {
		    if (grdDiffSrcData[i].file2diff == 'I ') {
				if (startLine<0) startLine = i;
				++addCnt;
			} else if (grdDiffSrcData[i].file2diff == 'RN') {
				if (startLine<0) startLine = i;
				++aftCnt;
			}
		}
	}	
	diffSrcData = diffSrcData.filter(function(data) {
		if(data.file1diff != null && data.file1diff != '') return true;
		if(data.file2diff != null && data.file2diff != '') return true;
		return false;
	});
	
//	console.log("6 " + grdDiffSrcData.length);
	
	$('#txtDelLine').val(String(delCnt));
	$('#txtBefLine').val(String(befCnt));
	$('#txtAftLine').val(String(aftCnt));
	$('#txtAddLine').val(String(addCnt));
	
	grdDiffSrc.setData(grdDiffSrcData);
	
	if (diffSrcData.length == 0) {
		$('#btnSrcDiff').prop('disabled', true); 
		$('#btnLeft').prop('disabled', true); 
		$('#btnRight').prop('disabled', true); 
	} else {
		$('#btnSrcDiff').prop('disabled', false); 
		$('#btnLeft').prop('disabled', false); 
		$('#btnRight').prop('disabled', false); 
		
		$('#optLine').wCheck('check',true);
		$('#txtSearch').val(startLine+1);
		
		txtSearch_change();
	}

	$('#txtBefSrc').val('');
	$('#txtAftSrc').val('');
	grdSrcDiff_Click();
}

function grdSrcDiff_Click() {
	$('#txtBefSrc').val('');
	$('#txtAftSrc').val('');
	
	var selectedGridRow = grdDiffSrc.list[grdDiffSrc.selectedDataIndexs];
	if (selectedGridRow == null) return;
	
	$('#txtBefSrc').val(selectedGridRow.file1);
	$('#txtAftSrc').val(selectedGridRow.file2);
}

function btnSrcDiff_click() {	
	
	diffGbn = 'D';
	grdDiffSrc.setData(diffSrcData);
	grdDiffSrc.repaint();

	svIdx = -1;
	svWord = '';
	$('#btnLeft').prop('disabled',true);
	$('#btnRight').prop('disabled',true);
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
}

function btnChgPart_click() {	
	var i = 0;
	
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
	svIdx = -1;
	svWord = '';
	if (diffGbn == 'A') {
		for(i=0; i<grdDiffSrcData.length; i++) {
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
			if(i == 0) svIdx = diffSrcData[diffSrcData.length-1].lineno;
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
			if(i == grdDiffSrcData.length-1) svIdx = diffSrcData[0].lineno-1;
		}	
	}
	grdDiffSrc.select(svIdx);
	grdDiffSrc.focus(svIdx);
}

function optradio_change() {
	$('#txtSearch').val('');
	txtSearch_change();
	
	if ($('[name="optradio"]:checked').val() == 'W') {
		$('#txtSearch').prop("placeholder", "검색할 단어를 입력하세요.");
	} else {
		$('#txtSearch').prop("placeholder", "검색할 라인번호를 입력하세요.");
	}
}

function txtSearch_change() {
	
	$('#btnSearch').text('찾기');
	svIdx = -1;
	svWord = '';
	chgLine = -1;
	
	$('#txtSearch').val($('#txtSearch').val().trim());
	
	if ($('#txtSearch').val().length == 0) return;
	
	btnSearch_click();
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
					if (rowMsg.toUpperCase().indexOf(svWord.toUpperCase())>=0) {
						svIdx = i;
						findSw = true;
						break;
					}
				}
				rowMsg = grdDiffSrcData[i].file2;
				if (rowMsg.length > 0) {
					if (rowMsg.toUpperCase().indexOf(svWord.toUpperCase())>=0) {
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
					if (rowMsg.toUpperCase().indexOf(svWord.toUpperCase())>=0) {
						svIdx = i;
						findSw = true;
						break;
					}
				}
				rowMsg = diffSrcData[i].file2;
				if (rowMsg.toUpperCase().length > 0) {
					if (rowMsg.indexOf(svWord.toUpperCase())>=0) {
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
		
		if (searchGbn == 'W') $('#btnSearch').text('찾기');
		return;
	}
	grdDiffSrc.select(svIdx);
	grdDiffSrc.focus(svIdx);
	grdSrcDiff_Click();
	
	if(searchGbn == 'L') {
		svIdx = -1;
		svWord = '';
	}
}