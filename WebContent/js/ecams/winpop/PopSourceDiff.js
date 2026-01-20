/** 프로그램정보 > 소스비교 (eCmr5200.mxml) **/
var pReqNo  = null;
var pItemId = null;
var pUserId = null;

var grdDiffSrc     = new ax5.ui.grid();
var grdProgHistory = new ax5.ui.grid();

var selOptions 	   = [];

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
var befVer         = 0;
var aftVer         = 0;
var encoding       = null;

var tmpInfo 		= new Object();
var tmpInfoData 	= new Object();

var f = document.getSrcData;

pReqNo = f.acptno.value;
pItemId = f.itemid.value;
pUserId = f.user.value;

grdProgHistory.setConfig({
    target: $('[data-ax5grid="grdProgHistory"]'),
    sortable: true, 
    multiSort: true,
    page: false,
    //showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
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
        {key: "isChecked",    label: "",        width: '3%', sortable: false, editor:{type:'checkbox',config: {trueValue: "Y", falseValue: "N"}}},
        {key: "version",      label: "버전",     	width: '7%'},
        {key: "cm_username",  label: "신청인",	width: '7%'},
        {key: "acptno",       label: "신청번호",  	width: '10%'},
        {key: "cr_sayu",      label: "신청사유",  	width: '50%',  align: 'left'},
        {key: "DATE1",        label: "신청일시", 	width: '11%'},
        {key: "DATE2",        label: "적용일시",  	width: '11%'}
    ]
});

grdDiffSrc.setConfig({
    target: $('[data-ax5grid="grdDiffSrc"]'),
    sortable: false, 
    multiSort: false,
    paging: false,
    page: false,
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
        {key: "file1",  label: "변경전",	width: '48%',  	align: 'left',
        	formatter: function(){
                var htmlStr = '<xmp>'+this.value+'</xmp>';
                return htmlStr;
             }
        },
        {key: "file2",  label: "변경후",	width: '48%',  align: 'left',
        	formatter: function(){
                var htmlStr = '<xmp>'+this.value+'</xmp>';
                return htmlStr;
             }}
    ]
});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	if (pUserId == null || pUserId == '' || pUserId == undefined || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pItemId == null || pItemId.length != 12) {
		dialog.alert('프로그램ID가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	screenInit('M');
	
	//비교클릭
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

	//변경라인클릭
	$('#btnChgLine').bind('click', function() {
		btnChgLine_click();
	});

	//찾기클릭
	$('#btnSearch').bind('click', function() {
		btnSearch_click();
	});

	//닫기클릭
	$('#btnExit').bind('click', function() {
		close();
	});
	
	//단어검색,라인검색 enter
	$('#txtSearch').bind('keypress', function(event){
		if(event.keyCode==13) {
			$('#btnSearch').trigger('click');
		}
	});
	
	// 엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		//grdDiffSrc.exportExcel($('#txtProgId').val()+"_SourceDiff.xls");
		excelExport(grdDiffSrc,$('#txtProgId').val()+"_SourceDiff.xls");
	});
	getTmpDir('99');	
	
});
//환성화 비활성화 초기화로직
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
		requestType: 	'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}
function successeCAMSDir(data) {
	tmpDir = data;
	getProgHistory(pItemId);
}
function getProgHistory(itemid){
	var tmpInfoData = {
			ItemID : 	itemid,
			AcptNo : 	pReqNo,
		requestType: 	'getFileVer'
	}
	ajaxAsync('/webPage/ecmr/Cmr5200Servlet', tmpInfoData, 'json', successProgList);
}
function successProgList(data) {
	console.log('successProgList data',data);
	var firstSw = false;
	var secondSw = false;
	var firstVer = '';
	grdProgHistoryData = data;
	//grdProgHistory.setData(grdProgHistoryData);
	
	if (grdProgHistoryData.length == 0) {
		dialog.alert('프로그램변경이력이 존재하지 않습니다.');
		return;
	}
	$('#txtSysMsg').val(grdProgHistoryData[0].cm_sysmsg);
	$('#txtProgId').val(grdProgHistoryData[0].cr_rsrcname);
	$('#txtDir').val(grdProgHistoryData[0].cm_dirpath);
	
	for(var i=0; i<grdProgHistoryData.length; i++) {
		if (grdProgHistoryData[i].checked == 'true') {
			if (!firstSw) {
				grdProgHistoryData[i].isChecked = 'Y';
				firstVer = grdProgHistoryData[i].cr_ver;
				firstSw = true;
			} else {
				grdProgHistoryData[i].isChecked = 'Y';
				secondSw = true;
			}
		}
		if (firstSw && secondSw) break;
	}	
	grdProgHistory.setData(grdProgHistoryData);
	
	if (!firstSw || !secondSw) {
		dialog.alert('비교대상이 존재하지 않습니다.');
		return;
	}
	
	btnVerDiff_click();

}
function btnVerDiff_click() {
	var selCnt = 0;
	var firstLine = 0;
	var secondLine = 0;
	
	diffGbn = 'A';
	svIdx = -1;
	svWord = '';
	$('#btnSearch').text('찾기');
	$('#lblLine').text('라인검색');
	for(var i=0; i<grdProgHistoryData.length; i++) {
		if(grdProgHistoryData[i].isChecked == 'Y'){
			if(selCnt == 0){
				firstLine = i;
				++selCnt;
			} else if(selCnt > 0){
				secondLine = i;
				++selCnt;
			}
		}
	}
	
	if (selCnt != 2) {
		dialog.alert('2개의 변경이력을 선택한 후 진행하시기 바랍니다.');
		return;
	}
	
	//console.log('befVer:',befVer,',aftVer:',aftVer);
	if (befVer > 0 && aftVer > 0) {
		//if ( grdDiffSrcData.length > 0 && befVer == grdProgHistoryData[secondLine].cr_ver && aftVer == grdProgHistoryData[firstLine].cr_ver && encoding == getSelectedVal("cboCharacter").text ) {
		if ( grdDiffSrcData.length > 0 && befVer == grdProgHistoryData[secondLine].cr_ver && aftVer == grdProgHistoryData[firstLine].cr_ver ) {
			grdDiffSrc.setData(grdDiffSrcData);
			grdDiffSrc.repaint();
			
			$('#btnSrcDiff').prop('disabled',true);
			if (diffSrcData.length == 0) {
				$('#btnChgPart').prop('disabled', true); 
				$('#btnChgLine').prop('disabled', true); 
			} else {
				$('#btnChgPart').prop('disabled', false); 
				$('#btnChgLine').prop('disabled', false); 
			}
			return;
		}
	}
	//grdProgHistory.select(1);

	tmpInfoData = new Object();
	tmpInfoData = {
			 UserID	: pUserId,
			ItemID1	: pItemId,
			   ver1	: grdProgHistoryData[secondLine].cr_ver,
			ItemID2	: pItemId,
			   ver2 : grdProgHistoryData[firstLine].cr_ver,
		requestType	: 'getDiffAry'
	}
	
	$('[data-ax5grid="grdDiffSrc"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr5200Servlet', tmpInfoData, 'json', successDiffList);
}

function successDiffList(data) {
	$(".loding-div").remove();
	var delCnt = 0;
	var befCnt = 0;
	var addCnt = 0;
	var aftCnt = 0;
	
	grdDiffSrcData = data;
	diffSrcData = data;
	
	if (grdDiffSrcData[0].errmsg != null && grdDiffSrcData[0].errmsg != '') {
		dialog.alert(grdDiffSrcData[0].errmsg);
		return;
	}
	
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
	if (diffSrcData.length == 0) {
		$('#btnSrcDiff').prop('disabled', true); 
		$('#btnChgPart').prop('disabled', true); 
		$('#btnChgLIne').prop('disabled', true); 
	} else {
		$('#btnChgPart').prop('disabled', false); 
		$('#btnChgLine').prop('disabled', false); 
	}

	$('#txtBefSrc').val('');
	$('#txtAftSrc').val('');
	$('#lblLine').text('라인검색(1~'+grdDiffSrcData.length+')');
	
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

function btnSrcDiff_click() {	
	
	$('#btnSrcDiff').prop('disabled', true); 
	diffGbn = 'A';
	grdDiffSrc.setData(grdDiffSrcData);
	grdDiffSrc.repaint();

	svIdx = -1;
	svWord = '';
	$('#btnSearch').text('찾기');
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
	$('#btnChgPart').prop('disabled', false); 
	$('#btnChgLine').prop('disabled', false); 
	
}

function btnChgPart_click() {
	
	$('#btnSrcDiff').prop('disabled', false); 
	$('#btnChgLine').prop('disabled', true); 
	diffGbn = 'D';
	grdDiffSrc.setData(diffSrcData);
	grdDiffSrc.repaint();

	svIdx = -1;
	svWord = '';
	$('#btnSearch').text('찾기');
	if (diffSrcData.length == 0) {
		dialog.alert('변경된 내용이 없습니다.');
		return;
	}
	$('#btnChgPart').prop('disabled', true); 
	
}
function btnChgLine_click() {	
	

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
	$('#btnSearch').text('찾기');
	
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

	if (searchGbn == 'L') {
		if (isNaN(strWord)) {
			dialog.alert('검색 할 라인을 숫자로 입력하여 주시기 바랍니다.', function(){});
			return;
		}
		
		if(Number(strWord) < 1 || Number(strWord) > grdDiffSrcData.length) {
			dialog.alert('라인을 범위 내로 입력하시기 바랍니다. (1~'+grdDiffSrcData.length+')', function(){});
			return;
		}
	}
	
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
			if (findSw) {
				$('#btnSearch').text('다음');
			}
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
				if (rowMsg.length > 0) {
					if (rowMsg.toUpperCase().indexOf(svWord.toUpperCase())>=0) {
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