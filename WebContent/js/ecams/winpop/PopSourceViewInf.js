/** 프로그램정보 > 소스보기-연관프로그램 (eCmr5500.mxml) **/
var pReqNo  = null;
var pItemId = null;
var pUserId = null;
var rgtList = null;
var adminYN = null;

var grdProgHistory = new ax5.ui.grid();

var cboVersionData 		= [];
var grdProgHistoryData 	= [];
var selectedGridItem   	= null;
var srcData       		= null;
var srcArray      		= [];

var pItemId        = null;
var isAdmin 	   = false;
var ingSw          = false;
var findLine       = 0;
var svWord         = null;
var tmpDir         = null;
var downURL        = null;
var outName        = null;
var prettify       = [];
var grdProgHistoryClickIng = false;

var tmpInfo = new Object();
var tmpInfoData = new Object();

var f = document.getSrcData;

pReqNo = f.acptno.value;
pItemId = f.itemid.value;
pUserId = f.user.value;

grdProgHistory.setConfig({
    target: $('[data-ax5grid="first-Grid"]'),
    sortable: true, 
    multiSort: true,
    page: false,
    //showRowSelector: true,
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	if(grdProgHistoryClickIng){
        		return;
        	}
           this.self.clearSelect();
           this.self.select(this.dindex);
           grdProgHistoryClick();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_rsrcname", label: "프로그램명",	width: '35%', align: 'left'},
        {key: "cr_ver",      label: "버전",      width: '10%'},
        {key: "cm_dirpath",  label: "경로",      width: '55%', align: 'left'}
    ]
    
});

$('[data-ax5select="cboVersion"]').ax5select({
    options: []
});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	if (pUserId == null || pUserId.length == 0 || pUserId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;
	}
	if (pItemId == null || pItemId == '' || pItemId == undefined || pItemId.length != 12) {
		dialog.alert('프로그램ID가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	screenInit('M');
		
	//소스다운 클릭
	$('#btnSrcDown').bind('click', function() {
		btnSrcDown_click();
	});

	//소스다운 클릭
	$('#btnSrcCopy').bind('click', function() {
		btnSrcCopy_click();
	});
	
	//찾기클릭
	$('#btnSearch').bind('click', function() {
		btnSearch_click();
	});
	
	//닫기클릭
	$('#btnExit').bind('click', function() {
		window.open('about:blank','_self').self.close();
	});
	
	$('#cboVersion').bind('change', function() {
		changeBaseVer();
	});

	// 검색단어입력 후 엔터
	$('#txtSearch').bind('keypress', function(event) {		
		if(event.keyCode === 13) {
			$('#btnSearch').trigger('click');
		}
	});
	
	getFileVer();
	getTmpDir('99');		
	
});

//활성화 비활성화 초기화로직
function screenInit(gbn){
	
	if (gbn == 'M') {

		cboVersionData = [];
		$('[data-ax5select="cboVersion"]').ax5select({
	        options: []
		});
		$('#txtSysMsg').val('');
		$('#txtProgId').val('');
		$('#txtDir').val('');
		
		$('#btnSrcCopy').hide();
	}
	if (gbn != 'P') {
		grdProgHistory.setData([]);
		grdProgHistory.repaint();	
	}
	$('#btnSrcDown').prop('disabled', true); 
	$('#btnSearch').prop('disabled', true); 
	$('#txtSearch').val('');
	$('#txtSearch').prop("readonly", true);
	
	var htmlObj = document.getElementById("htmlSrc1");
	htmlObj = null;
	prettify = [];
	txtSearch_change();
	
	hljs.initHighlightingOnLoad();
}

function getTmpDir(dirCd){
//	console.log('getTmpDir dirCd:',dirCd);
	var tmpInfoData = {
			  pCode : 	dirCd,
		requestType : 	'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successGetSystemPath);
}

function successGetSystemPath(data) {
	console.log('successGetSystemPath',data);
	tmpDir = data;
}

function getFileVer(){
	var tmpInfoData = {
		ItemID     : pItemId,
		requestType: 'getFileVer'
	}
	ajaxAsync('/webPage/ecmr/Cmr5500Servlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	cboVersionData = data;
	
	if (cboVersionData == null || cboVersionData == '' || cboVersionData == undefined || cboVersionData.length == 0) return;
	
	if (cboVersionData.length == 1) {
		if (cboVersionData[0].cr_itemid == 'ERROR') {
			dialog.alert(cboVersionData[0].cr_sayu);
			return;
		}
	}
	$('#txtSysMsg').val(cboVersionData[0].cm_sysmsg);
	$('#txtProgId').val(cboVersionData[0].cr_rsrcname);
	$('#txtDir').val(cboVersionData[0].cm_dirpath);
	
	var selValue = '';
	
	cboVersionData = cboVersionData.filter(function(data){
		if (data.cr_acptno != 'DEV' && selValue == '') selValue = data.cr_acptno;
		if(data.labelmsg != null && data.labelmsg != "" && data.labelmsg != undefined ){
			return true;
		}
	});
	
	console.log('1.cboVersion='+selValue);
	console.log('2.cboVersion',cboVersionData);
	$('[data-ax5select="cboVersion"]').ax5select({
        options: injectCboDataToArr(cboVersionData, 'cr_acptno' , 'labelmsg')
	});
	
	if (cboVersionData.length>0) {
		if (pReqNo != null && pReqNo != '' && pReqNo != undefined) {
			$('[data-ax5select="cboVersion"]').ax5select('setValue',pReqNo,true);
			if (getSelectedVal('cboVersion').value != pReqNo) $('[data-ax5select="cboVersion"]').ax5select('setValue',selValue,true);
		} else $('[data-ax5select="cboVersion"]').ax5select('setValue',selValue,true);
		
		changeBaseVer();
	}
}

function changeBaseVer() {
	screenInit('S');
	
	if (getSelectedIndex('cboVersion') < 0) return;
	$('#txtSayu').val('');
	
	if (getSelectedVal('cboVersion').cr_sayu != null && getSelectedVal('cboVersion').cr_sayu != '' && getSelectedVal('cboVersion').cr_sayu != undefined) 
		$('#txtSayu').val(getSelectedVal('cboVersion').cr_sayu);
		
	tmpInfoData = new Object();
	tmpInfoData = {
			ItemID	: pItemId,
			AcptNo	: getSelectedVal('cboVersion').cr_acptno,
			QryCd	: getSelectedVal('cboVersion').cr_qrycd,
			chkFg	: 'false',
			Status	: getSelectedVal('cboVersion').cr_status,
		requestType	: 'getRelatFile'
	}
	ajaxAsync('/webPage/ecmr/Cmr5500Servlet', tmpInfoData, 'json', successGetRelatFile);
}

function successGetRelatFile(data) {	
	grdProgHistoryData = data;
	grdProgHistory.setData(grdProgHistoryData);
	
	console.log('successGetRelatFile',grdProgHistoryData);
	if (grdProgHistoryData.length > 0) {
		grdProgHistory.select(0);
		
		grdProgHistoryClick();
	}
}

function grdProgHistoryClick() {
	grdProgHistoryClickIng = true;
	screenInit('P');

	selectedGridItem = grdProgHistory.list[grdProgHistory.selectedDataIndexs];
	
	// 그리드에 loading 이미지 추가 후 보여주기
	$('#sourceDiv').append(loading_div);
	$(".loding-div").show();
//		$("#sourceDiv").css("overflow-y", "hidden");
	
	var parmAcpt = '';
	var parmVer = '';
	if (selectedGridItem.cr_ver == null || selectedGridItem.cr_ver == '' || selectedGridItem.cr_ver == undefined) {
		parmAcpt  = selectedGridItem.cr_acptno;
	} else parmVer = selectedGridItem.cr_ver;
	
	tmpInfoData = {
			 ItemId	: pItemId,
			Version	: parmVer,
			 AcptNo	: parmAcpt,
		  ynDocFile : 'false',
		  	  gubun : 'true',
		requestType	: 'getFileText'
	}
	console.log('getFileText',tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr5300Servlet', tmpInfoData, 'json', successVersion);
}

function successVersion(data) {	
	console.log('successVersion data:',data);
	grdProgHistoryClickIng = false;
//	$(".loding-div").remove();
//	$("#sourceDiv").css("overflow-y", "auto");
//	$('#sourceDiv pre').remove();
//	$('#sourceDiv').append('<pre style="width:100%;height:100%;"><code id="htmlSrc1" class="hljs" style="line-height: 14px; width:fit-content;"></code></pre>');
	
	$("#sourceDiv .loding-div").remove();
	$('#sourceDiv pre').remove();
	$('#sourceDiv').append('<textarea id="htmlSrc1" style="line-height: 14px; width: fit-content; font-size: 13px; padding-bottom: 0px; width: 100%; height: 100%; padding: 0.5em; background: white; color: black; resize: none;" spellcheck="false" readonly></textarea>');
	
	if (data == null || data == '' || data == undefined || data.length == 0) {
		dialog.alert('소스보기 중 오류가 발생하였습니다.');
		return;
	}
	srcArray = data;
	
	if (srcArray.length < 1) {
		dialog.alert('소스보기에 실패하였습니다.');
		return;	
	}
	if (srcArray[0].error == 'Y') {
		dialog.alert(srcArray[0].errmsg);
		return;	
	}
	
	//srcView();
	var totalSource = "";
	for (var i=0; i<srcArray.length; i++){
		totalSource += srcArray[i].line + ' ' + srcArray[i].src;
	}
	$("#htmlSrc1").val(totalSource);
	$('#htmlView').scrollTop(0);

	$('#btnSrcDown').prop('disabled', false);
	$('#btnSearch').prop('disabled', false);
	$('#txtSearch').prop("readonly", false);
}

function srcView() {
	var lineData = '';
	var sourceViewCnt = 0;
	
	srcData = '';
	$('#sourceDiv pre').remove();
	$('#sourceDiv').append('<pre style="width:100%;height:100%;"><code id="htmlSrc1" class="hljs" style="line-height: 14px; width:fit-content;font-size:12px"></code></pre>');
	
	//코드가 너무 길 경우 크롬에서 스크롤이 느려 소스를 100줄씩 나누어 그려줌
	var srcCount = (srcArray.length/100)+1;
	for (var i=0; i<srcCount; i++){
		if( i!=0 ){
			$("#sourceDiv pre").append("<code id='htmlSrc" +(sourceViewCnt+1)+"' class='hljs' style='line-height: 14px; width:fit-content;font-size:12px'></code>");
		}
		srcData = "";
		for(var j=0; j<100; j++){
			var cnt = j+(100*sourceViewCnt);
			if(cnt >= srcArray.length){
				break;
			}
			lineData = srcArray[cnt].line + ' ' + srcArray[cnt].src;
			
			srcData += lineData;
		}
		// IE에서의 속도문제 때문에 highlight 제거
//		var highlightSrc = hljs.highlightAuto(srcData).value;
//		$("#htmlSrc"+(sourceViewCnt+1)).append(highlightSrc);
//		prettify.push(highlightSrc);
		$("#htmlSrc"+(sourceViewCnt+1)).append(htmlFilter(srcData));
		// filter 한 데이터를 배열에 추가
		prettify.push(htmlFilter(srcData));
		$("#htmlSrc"+sourceViewCnt).css("padding-bottom","0px");
		sourceViewCnt++;
		if(i!=0){
			$("#htmlSrc"+sourceViewCnt).css("padding-top","0px");
		}
	}
	var agent = navigator.userAgent.toLowerCase();
	if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
	     // ie일 경우
		$("#sourceDiv pre code").css("display","table");
	}
	$('#htmlView').scrollTop(0);
	
	$('#btnSrcDown').prop('disabled', false); 
	$('#btnSearch').prop('disabled', false); 
	$('#txtSearch').prop("readonly", false);
}

function optradio_change() {
	txtSearch_change();
}

function txtSearch_change() {
	//console.log('txtSearch_change'+$('#txtSearch').val());
	
	findLine = 0;
	svWord = '';
	$('#btnSearch').text('찾기');
	if (prettify.length > 0) {
		for(var i=0; i<prettify.length; i++){
			$("#htmlSrc"+(i+1)).html(prettify[i]);
			$('#htmlView').scrollTop(0);
		}
		/*
		var htmlObj = document.getElementById("htmlSrc");
		htmlObj.innerHTML = prettify;
		$('#htmlView').scrollTop(0);
		*/
	}
}

function btnSearch_click() {
	var strWord = $('#txtSearch').val().trim();
	var findSw = false;
	var lineData = '';
	
	if (strWord != null && strWord.length == 0) {
		dialog.alert('검색 할 단어/라인을 입력하여 주시기 바랍니다.', function(){});
		return;
	}
	var searchGbn = $('[name="optradio"]:checked').val();
	//console.log('radio='+searchGbn);
	if (searchGbn == 'W') {
		if (svWord != strWord) {
			findLine = 0;
			svWord = strWord;
			var chgString = '<span class="hljs-search">' + strWord + '</span>';
			for(var i=0; i<prettify.length; i++){
				var resultString = replaceAllString(prettify[i],strWord,chgString);
				$("#htmlSrc"+(i+1)).html(resultString);
			}
			
			//var htmlObj = document.getElementById("htmlSrc1");
			//htmlObj.innerHTML = resultString;
		}
		for (var i=findLine+1;srcArray.length>i;i++) {
			lineData = srcArray[i].src;
			if (lineData.indexOf(strWord)>=0) {
				findLine = i;
				findSw = true;
				break;
			}
		}
		if (findSw) $('#btnSearch').text('다음');
	} else {
		if (isNaN(strWord)) {
			dialog.alert('검색 할 라인을 숫자로 입력하여 주시기 바랍니다.', function(){});
			return;
		}
		findLine = Number(strWord);
		//console.log('line search='+findLine);
		if (srcArray.length < findLine) {
			dialog.alert('검색 할 라인이 존재하지 않습니다.', function(){});
			return;
		}
		findLine = findLine - 1;
		findSw = true;
	}
	//console.log(findLine);
	
	if (findSw) {
		yPOS = (findLine * 14)+Number(($("#htmlSrc1").css("padding-top")).substring("px",""))+4;
		//console.log(yPOS);
		$('#htmlView').scrollTop(yPOS);
		if (searchGbn == 'L') {
			findLine = 0;
		}
	} else if (findLine == 0) {
		dialog.alert('검색단어가 존재하지 않습니다.', function(){});
		return;
	} else {
		dialog.alert('더 이상 검색단어가 존재하지 않습니다.', function(){});
		findLine = 0;
		$('#btnSearch').text('찾기');
		return;
	}
}

function btnSrcDown_click() {
	var getSelectedItem = grdProgHistory.getList("selected");
	outName = getSelectedItem[0].cr_itemid + "_gensrc.file";
	var fullPath = tmpDir+'/'+outName;
	var fileName = getSelectedItem[0].cr_rsrcname;
	fileDown(fullPath, fileName);
}

function btnSrcCopy_click(){

	var textarea = document.createElement("textarea");
	$($("#sourceDiv").find("code.hljs")).each(function(){
		textarea.value += $(this).html();
	});
	textarea.value = replaceAllString(replaceAllString(replaceAllString(replaceAllString(replaceAllString(replaceAllString(textarea.value,"&amp;","&"),"&#39;","\'"),"&gt;",">"),"&quot;","\""),"&#29;","\'"),"&lt;","<");
	document.body.appendChild(textarea);
	textarea.select();
	document.execCommand("copy");
	document.body.removeChild(textarea);

	dialog.alert('소스가 복사되었습니다.');
}