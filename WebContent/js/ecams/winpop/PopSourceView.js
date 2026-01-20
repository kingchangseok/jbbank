/** 프로그램정보 > 소스보기 (eCmr5300.mxml) **/
var pReqNo  = null;
var pItemId = null;
var pUserId = null;

var grdProgHistory = new ax5.ui.grid();

var grdProgHistoryData	= null;
var selectedGridItem	= null;
var srcData        		= null;
var srcArray       		= [];

var isAdmin 	   = false;
var ingSw          = false;
var findLine       = 0;
var svWord         = null;
var tmpDir         = null;
var outName        = null;
var prettify       = [];

var tmpInfo = new Object();
var tmpInfoData = new Object();

var f = document.getSrcData;
pReqNo = f.acptno.value;
pItemId = f.itemid.value;
pUserId = f.user.value;

grdProgHistory.setConfig({
    target: $('[data-ax5grid="grdProgHistory"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    page: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
           this.self.clearSelect();
           this.self.select(this.dindex);
           grdProgHistory_Click();
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_ver",  	label: "버전",		width: '10%'},
        {key: "filedt",   	label: "신청일시",		width: '10%'},
        {key: "cr_acptno",  label: "신청번호",  	width: '10%'},
        {key: "cr_sayu",  	label: "신청사유",		width: '50%', align: 'left'}
    ]
});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

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
		close();
	});

	//검색단어입력 후 엔터
	$('#txtSearch').bind('keypress', function(event) {		
		if(event.keyCode === 13) {
			event.keyCode = 0;
			$('#btnSearch').trigger('click');
		}
	});
	
	//Line보기
	$('#chkLine').bind('click', function() {
		grdProgHistory_Click();
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
	var tmpInfoData = {
			  pCode : 	dirCd,
		requestType : 	'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}

function successeCAMSDir(data) {
	tmpDir = data;
	getProgHistory(pItemId);
}

function getProgHistory(itemid){
	var tmpInfoData = {
			ItemID  : 	itemid,
		requestType : 	'getFileVer'
	}
	ajaxAsync('/webPage/ecmr/Cmr5300Servlet', tmpInfoData, 'json', successProgList);
}

function successProgList(data) {
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data.substr(5));
		return;
	}
	
	data.forEach(function(item){
		if(item.cr_ver == "DEV" || item.cr_ver == "D") item.filedt = '';
	});
	
	grdProgHistoryData = data;
	grdProgHistory.setData(grdProgHistoryData);
	var findSw = false;
	var i = 0;
	if (grdProgHistoryData == null || grdProgHistoryData.length == 0) return;
	
	$('#txtSysMsg').val(grdProgHistoryData[0].cm_sysmsg);
	$('#txtProgId').val(grdProgHistoryData[0].cr_rsrcname);
	$('#txtDir').val(grdProgHistoryData[0].cm_dirpath);
	
	if (pReqNo != null && pReqNo.length > 0) {
		for(i=0; i<grdProgHistoryData.length; i++) {
			if(grdProgHistoryData[i].cr_acptno == pReqNo) {
				findSw = true;
				grdProgHistory.select(i);
				grdProgHistory.focus(i);
				break;
			}
		}
	} 
	
	if (!findSw) {
		for(var i=0; i<grdProgHistoryData.length; i++) {
			if(grdProgHistoryData.length == 1 || grdProgHistoryData.length == 0) break;
			else {
				if(grdProgHistoryData[i].cr_ver == 'DEV') {
					continue;
				}else if(grdProgHistoryData[i].cr_ver == '') {
					findSw = true;
					break;
				}else {
					findSw = true;
					break;
				}
			}
		}
		//grdProgHistory.clearSelect();
		grdProgHistory.select(i);
		grdProgHistory.focus(i);
		//selectedGridItem = grdProgHistory.list[0];
	}
	grdProgHistory_Click();
}

function grdProgHistory_Click() {
	screenInit('S');
	srcArray = [];
	selectedGridItem = grdProgHistory.list[grdProgHistory.selectedDataIndexs];
	console.log('grdProgHistory_Click',selectedGridItem);
	if (selectedGridItem.errsw != null && selectedGridItem.errsw != '' && selectedGridItem.errsw != undefined && selectedGridItem.errsw == 'Y') {
		dialog.alert(selectedGridItem.rstmsg);
		return;
	}
	
	$('#htmlSrc1').remove();
	$('#lblLine').text('라인검색');
	$('#sourceDiv').append(loading_div);
	$(".loding-div").show();
	
	var strVer = '';
	var strAcpt = '';
	var ynDocFile = 'false';
	if (selectedGridItem.cr_ver != null && selectedGridItem.cr_ver !== '' && selectedGridItem.cr_ver != undefined) strVer = selectedGridItem.cr_ver;
	if (selectedGridItem.cr_acptno != null && selectedGridItem.cr_acptno !== '' && selectedGridItem.cr_acptno != undefined) strAcpt = selectedGridItem.cr_acptno;
	if (selectedGridItem.cm_info.substr(45,1) == '1') ynDocFile = 'true';
					
	tmpInfoData = {
			 ItemId  : pItemId,
			Version  : strVer,
			 AcptNo  : strAcpt,
		  ynDocFile  : ynDocFile,
		  	  gubun	 : $('#chkLine').is(':checked'),
		requestType	 : 'getFileText'
	}
	console.log('getFileText',tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr5300Servlet', tmpInfoData, 'json', successVersion);
}
function successVersion(data) {
	console.log('successVersion data:',data);
	$("#sourceDiv .loding-div").remove();
	$('#sourceDiv pre').remove();
	$('#sourceDiv').append('<pre style="width:100%;height:100%;"><code id="htmlSrc1" class="hljs" style="line-height: 14px; width:fit-content;font-size:12px"></code></pre>');
	//$('#sourceDiv').append('<textarea id="htmlSrc1" style="line-height: 14px; width: fit-content; font-size: 13px; padding-bottom: 0px; width: 100%; height: 100%; padding: 0.5em; background: white; color: black; resize: none;" spellcheck="false" readonly></textarea>');

	if (typeof data == 'string' && data != null && data != undefined && data.indexOf('ERROR')>-1) {
		dialog.alert(data.substr(5));
		return;
	}
	
	if (data == null || data.length == 0) {
		dialog.alert("소스보기 중 오류가 발생하였습니다.");
		return;
	}
	srcArray = data;
	
	if (srcArray.length < 1) {
		dialog.alert("소스보기에 실패하였습니다.");
		return;	
	}
	if (srcArray[0].error == 'Y') {
		dialog.alert(srcArray[0].errmsg);
		return;	
	}
	    
	if (selectedGridItem.cm_info.substr(45,1) == '1') {
		$('#btnSrcDown').prop('disabled', true); 
		$('#btnSearch').prop('disabled', true); 
		$('#txtSearch').prop("readonly", true);
		if (srcArray[0].src != null && srcArray[0].src != '' && srcArray[0].src != undefined) {
			fileDown(srcArray[0].src, selectedGridItem.cr_rsrcname);
		}
		return;
	}
	
	srcView();
	/*var totalSource = "";
	for (var i=0; i<srcArray.length; i++){
		if($('#chkLine').is(':checked')) totalSource += srcArray[i].line + ' ' + srcArray[i].src;
		else totalSource += srcArray[i].src;
	}
	$("#htmlSrc1").val(totalSource);*/
	$('#htmlView').scrollTop(0);
	$('#lblLine').text('라인검색(1~' + srcArray[srcArray.length-1].line.trim()+')');

	$('#btnSrcDown').prop('disabled', false);
	$('#btnSearch').prop('disabled', false);
	$('#txtSearch').prop("readonly", false);
}

function srcView() {
	var lineData = '';
	var sourceViewCnt = 0;
	
	//console.log(data);
	$('#sourceDiv pre').remove();
	$('#sourceDiv').append('<pre style="width:100%;height:100%;"><code id="htmlSrc1" class="hljs" style="line-height: 14px; width:fit-content;font-size:12px"></code></pre>');
	
	srcData = '';
    
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
			if($('#chkLine').is(':checked')) lineData = srcArray[cnt].line + ' ' + srcArray[cnt].src;
			else lineData = srcArray[cnt].src;
			srcData += lineData;
		}
		// IE에서의 속도문제 때문에 highlight 제거
//		var highlightSrc = hljs.highlightAuto(srcData).value;
//		$("#htmlSrc"+(sourceViewCnt+1)).append(highlightSrc);
//		prettify.push(highlightSrc);
		$("#htmlSrc"+(sourceViewCnt+1)).append(htmlFilter(srcData));
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
	//console.log('optradio_change');
	//var htmlObj = $('[name="optradio"]:checked').val();
	//console.log(htmlObj);
	
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
	$('#txtSearch').val($('#txtSearch').val().trim());
	
}
function btnSearch_click() {
	
	var strWord = $('#txtSearch').val().trim();
	var findSw = false;
	var lineData = '';
	
	if (strWord != null && strWord.length == 0) {
		setTimeout(function() {
			dialog.alert('검색 할 단어/라인을 입력하여 주시기 바랍니다.', function(){});
		}, 10);
		return;
	}
	var searchGbn = $('[name="optradio"]:checked').val();
	//console.log('radio='+searchGbn);
	if (searchGbn == 'W') {
		if (svWord != strWord) {
			findLine = -1;
			svWord = strWord;
			var chgString = '<span class="hljs-search">' + strWord + '</span>';
			console.log('prettify==>', prettify);
			for(var i=0; i<prettify.length; i++){
				var resultString = replaceAllString(prettify[i],strWord,chgString);
				$("#htmlSrc"+(i+1)).html(resultString);
			}
			
			//var htmlObj = document.getElementById("htmlSrc1");
			//htmlObj.innerHTML = resultString;
		}
		for (var i=findLine+1;srcArray.length>i;i++) {
			lineData = srcArray[i].src;
			//console.log('lineData='+lineData + ",strWord="+strWord);
			if (lineData.indexOf(strWord)>=0) {
				findLine = i;
				findSw = true;
				break;
			}
		}
		console.log('findLine='+findLine);
		if (findSw) $('#btnSearch').text('다음');
	} else {
		if (isNaN(strWord)) {
			setTimeout(function() {
				dialog.alert('검색 할 라인을 숫자로 입력하여 주시기 바랍니다.', function(){});
			}, 10);
			return;
		}
		findLine = Number(strWord);
		//console.log('line search='+findLine);
		if (srcArray.length < findLine) {
			setTimeout(function() {
				dialog.alert('검색 할 라인이 존재하지 않습니다.', function(){});
			}, 10);
			return;
		}
		findLine = findLine - 1;
		findSw = true;
	}
	//console.log(findLine);
	
	if (findSw) {
		yPOS = (findLine * 14)+Number(($("#htmlSrc1").css("padding-top")).substring("px",""))+4;
		$('#htmlView').scrollTop(yPOS);
		$('#htmlSrc1').scrollTop(yPOS);
		if (searchGbn == 'L') {
			findLine = 0;
		}
	} else if (findLine == -1) {
		setTimeout(function() {
			dialog.alert('검색단어가 존재하지 않습니다.', function(){});
		}, 10);
		return;
	} else {
		setTimeout(function() {
			dialog.alert('더 이상 검색단어가 존재하지 않습니다.', function(){});
		}, 10);
		findLine = -1;
		if (searchGbn == 'W') $('#btnSearch').text('찾기');
		return;
	}
}

function btnSrcDown_click() {
	var fullPath = tmpDir+'/'+ pItemId +"_gensrc.file";
	var fileName = grdProgHistoryData[0].cr_rsrcname;
	fileDown(fullPath, fileName);
}

function btnSrcCopy_click() {
	var textarea = document.createElement("textarea");
	$($("#sourceDiv").find("code.hljs")).each(function(){
		textarea.value += $(this).html();
	});
	textarea.value = replaceAllString(replaceAllString(replaceAllString(replaceAllString(replaceAllString(replaceAllString(textarea.value,"&amp;","&"),"&#39;","\'"),"&gt;",">"),"&quot;","\""),"&#29;","\'"),"&lt;","<");
	document.body.appendChild(textarea);
	textarea.select();
	document.execCommand("copy");
	document.body.removeChild(textarea);

	dialog.alert("소스가 복사되었습니다.");
}