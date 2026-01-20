/**
 * 소스보기탭
 *  1) 신청상세 > 소스보기
 *  2) 조회 > Source View
 * 
 */

var pReqNo  = null;
var pItemId = null;
var pUserId = null;
var tmpDir  = null;

var selOptions 	   = [];
var srcData        = null;
var srcArray       = [];

var isAdmin 	   = false;
var ingSw          = false;
var findLine       = 0;
var svWord         = null;

var downURL        = null;
var outName        = null;
var prettify       = [];
var status         = null;
var realver		   = null;
var pUserId		   = null;
var viewver        = null;
var cr_devver      = null;
var rsrcname	   = null;
var rsrccd   	   = null;
var basename	   = null;

var tmpInfo = new Object();
var tmpInfoData = new Object();

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	var htmlObj = document.getElementById("htmlSrc1");
	htmlObj = null;
	prettify = [];
	txtSearch_change();
	
	hljs.initHighlightingOnLoad();
});

//SrcViewer.js
function showSource(data) {
	$('#divInfo').hide();
	$('#htmlView').css('height','97%');
	$("body").bind("mousedown", onBodyMouseDown); 
	
	successFileText(data);
}

//PopRequestSourceView.js
//활성화 비활성화 초기화로직
function elementInit() {
	$('#txtProgId').val(rsrcname);
	$('#txtBaseId').val(basename);
	$('#txtVer').val(viewver);
	$('#txtDirPath').val(dirpath);
	
	$("body").bind("mousedown", onBodyMouseDown); 
	
	getFileText(pItemId,pReqNo,realver,cr_devver,status);
}

function onBodyMouseDown(event){
	window.parent.$("#context-menu").css({"visibility" : "hidden"});
} 

function getFileText(itemid, acptno, realver, cr_devver, status){
	var parmVer = realver;
	var parmAcpt = acptno;
	
	$('#sourceDiv').append(loading_div);
	$(".loding-div").show();
	
	if (realver != null && realver != '') {
		if (realver.length == 12) {
			parmVer = '';
			parmAcpt = realver;
		} else {
			parmVer = realver;
		}
	} 
	var tmpInfoData = {
			 ItemId	: itemid,
			Version	: parmVer,
			 AcptNo	: parmAcpt,
		  ynDocFile	: 'false',
		  	  gubun : true,
		requestType : 'getFileText'
	}
	console.log('getFileText',tmpInfoData);
	ajaxAsync('/webPage/ecmr/Cmr5300Servlet', tmpInfoData, 'json', successFileText);
}

function successFileText(data) {
	console.log('successFileText',data);
	$("#sourceDiv .loding-div").remove();
	$('#sourceDiv pre').remove();
	$('#sourceDiv').append('<pre style="width:100%;height:100%;"><code id="htmlSrc1" class="hljs" style="line-height: 14px; width:fit-content;font-size:12px"></code></pre>');
	//$('#sourceDiv').append('<textarea id="htmlSrc1" style="line-height: 14px; width: fit-content; font-size: 13px; padding-bottom: 0px; width: 100%; height: 100%; padding: 0.5em; background: white; color: black; resize: none;" spellcheck="false" readonly></textarea>');
	
	if (typeof data == 'string' && data != null && data != undefined && data.indexOf('ERROR')>-1) {
		window.parent.dialog.alert(data.substr(5));
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
	
	srcView();
	var totalSource = "";
	for (var i=0; i<srcArray.length; i++){
		totalSource += srcArray[i].line + ' ' + srcArray[i].src;
	}
	$("#htmlSrc1").val(totalSource);
	$('#htmlView').scrollTop(0);
}

function srcView() {
	/*var htmlObj = document.getElementById("htmlSrc");
	htmlObj = null;
	prettify = [];
	hljs.initHighlightingOnLoad();*/
	
	var lineData = '';
	var sourceViewCnt = 0;
	
	$('#sourceDiv pre').remove();
	$('#sourceDiv').append('<pre style="width:100%;height:100%;"><code id="htmlSrc1" class="hljs" style="line-height: 14px; width:fit-content;font-size:12px"></code></pre>');
	//srcArray = data;
	
	srcData = '';
	
	//코드가 너무 길 경우 크롬에서 스크롤이 느려 소스를 100줄씩 나누어 그려줌
	var srcCount = (srcArray.length/100)+1;
	for (var i=0; i<srcCount; i++){
		if(i!=0){
			$("#sourceDiv pre").append("<code id='htmlSrc" +(sourceViewCnt+1)+"' class='hljs' style='line-height: 14px; width:fit-content;font-size:12px'></code>");
		}
		
		srcData = "";
		for(var j=0; j<100; j++){
			var cnt = j+(100*sourceViewCnt);
			if(cnt >= srcArray.length) {
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
	/*
	$.each(srcArray,function(key,value) {
		lineData = value.line + ' ' + value.src;
		srcData += lineData;
	});
	
	prettify = hljs.highlightAuto(srcData).value;
	var htmlObj = document.getElementById("htmlSrc");
	htmlObj.innerHTML = prettify;
	*/
}

function txtSearch_change() {
	findLine = 0;
	svWord = "";
	
	$('#htmlView').scrollTop(0);
	
	/*if (prettify.length > 0) {
		for(var i=0; i<prettify.length; i++){
			$("#htmlSrc"+(i+1)).html(prettify[i]);
			$('#htmlView').scrollTop(0);
		}
	}*/
}

function btnSearch_click(word,gbn) {
	var strWord = word;
	var findSw = false;
	var lineData = '';
	
	if (strWord != null && strWord.length == 0) {
		setTimeout(function() {
			window.parent.dialog.alert('검색 할 단어/라인을 입력하여 주시기 바랍니다.', function(){});
		}, 10);
		return;
	}
	var searchGbn = gbn;
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
			//var resultString = replaceAllString(prettify,strWord,chgString);
			//var htmlObj = document.getElementById("htmlSrc");
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
		if (findSw) {
			$('#btnSearch', window.parent.document).text('다음');
		}
	} else {
		if (isNaN(strWord)) {
			setTimeout(function() {
				window.parent.dialog.alert('검색 할 라인을 숫자로 입력하여 주시기 바랍니다.', function(){});
			}, 10);
			return;
		}
		
		findLine = Number(strWord);
		console.log('line search='+findLine);
		if (srcArray.length < findLine) {
			setTimeout(function() {
				window.parent.dialog.alert('검색 할 라인이 존재하지 않습니다.', function(){});
			}, 10);
			return;
		}
		findLine = findLine - 1;
		findSw = true;
	}
	console.log(findLine);
	
	if (findSw) {
		yPOS = (findLine * 13.5)+Number(($("#htmlSrc1").css("padding-top")).substring("px",""))+4;
		$('#htmlView').scrollTop(yPOS);
		$('#htmlSrc1').scrollTop(yPOS);
		if (searchGbn == 'L') {
			findLine = 0;
		}
	} else if (findLine == -1) {
		setTimeout(function() {
			window.parent.dialog.alert('검색단어가 존재하지 않습니다.', function(){});
		}, 10);
		return;
	} else {
		setTimeout(function() {
			window.parent.dialog.alert('더 이상 검색단어가 존재하지 않습니다.', function(){});
		}, 10);
		findLine = 0;
		
		if (searchGbn == 'W') $('#btnSearch', window.parent.document).text('찾기');
		return;
	}
}

function btnSrcDown_click() {
	var fullPath = tmpDir+'/'+pItemId+'_gensrc.file';
	console.log('btnSrcDown_click='+fullPath+','+rsrcname);
	fileDown(fullPath, rsrcname);
}