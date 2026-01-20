var userId 	= window.top.userId;

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

var docSw = false;	//산출물여부(46)
var ingSw = false;

var ingSw          = false;
var findLine       = 0;
var svWord         = null;

var tmpDir         = null;
var downURL        = null;
var outName        = null;

var prettify       = null;
var tmpTab 		   = null;
var tabIndex	   = 0;
var tabSize		   = 0;
var tabSize2	   = 0;
var clickTab	   = null;
var clickTab2	   = null;
var onTab		   = null;
var rMenu		   = null;

var treeObj			= null;
var treeObjData		= null;

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},callback: {
		onExpand: myOnExpand,
		onClick : onClickTree,
	},
	view: {
		dblClickExpand: false
	}
};

$(document).ready(function(){
	rMenu = $("#context-menu");
	
//	var Page_loading_div = "<div class='loding-Frame' style='width: 100%; height: 100%;'><div class='loding-Frame-img'><img alt='' src='/img/loading.gif' style='width: 64px;'></div></div>";
//	$("html").append(Page_loading_div);
	
	//검색
	$('#btnQry').bind('click', function() { 
		btnQry_click();
	});
	
	//프로그램명 Enter키
	$('#txtRsrc').bind('keypress', function(event){
		if(event.keyCode==13) {
			btnQry_click();
		}
	});
	
	//찾기
	$('#btnSearch').bind('click', function() { 
		btnSearch_click();
	});
	
	//검색단어 Enter키
	$('#txtSearch').bind('keypress', function(event){
		if(event.keyCode==13) {
			btnSearch_click();
		}
	});
	
	getCAMSDir();
//	getTopSysPath('',false);
});

function getCAMSDir() {
	var tmpInfoData = {
		pCode : '99',
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}

function successeCAMSDir(data) {
	data = data.filter(function(data) {
		if(data.cm_pathcd == '99') tmpDir = data.cm_path; 
	});
}

function getTopSysPath(rsrc,upsw) {
	var data = new Object();
	data = {
		UserID : userId,
		progName : rsrc,
		upSw : upsw,
		requestType	: 'getTopSysPath_ztree'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTopSysPath);
}

function successGetTopSysPath(data) {
	setTimeout(function(){
		$(".loding-Frame").remove();
	}, 1000);
	
	treeObjData = data;
	$.fn.zTree.init($("#treePath"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("treePath");
}

//트리 오픈
function myOnExpand(event, treeId, treeNode) {
	console.log('treeNode=',treeNode);
	if(treeNode.children != undefined) {
		return false;
	}
	
	if(treeNode.gbncd != 'RSC') {
		return false;
	};
	
	//로딩중 icon class 추가
	$('#'+treeNode.pId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){
		var ajaxReturnData = null;
		
		var tmpData = new Object();
		tmpData = {
			UserId : userId,
			SysCd  : treeNode.cm_syscd,
			RsrcCd : treeNode.cr_rsrccd,
			seqNo  : treeNode.cm_seqno,
			maxCnt  : treeNode.cr_lstver,
			progName  : $('#txtRsrc').val(),
			upSw  :  $('#txtRsrc').val().length > 0 ? $('#chkUpLow').is(':checked') : false,
			requestType:  "getDirPathFile_ztree"
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
				//obj[i].isParent = true;
			}
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			treeObj.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.pId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
};

//트리 클릭
function onClickTree(event, treeId, treeNode) {
	console.log('onClickTree=>',treeNode);
	var hasChild 	= treeNode.children != undefined ? true : false;
	var fullpath    = treeNode.cm_fullpath;
	var gbn 		= treeNode.gbncd;
	var itemid 		= treeNode.cr_itemid;
	var lstver  	= treeNode.cr_lstver;
	var rsrcName 	= treeNode.cm_dirpath;
	var rsrcInfo 	= treeNode.cm_info;
	var tmpFullPath = fullpath + '/' + rsrcName;
	
	docSw = false;
	
	if(gbn == 'SYS') return;
	if(gbn != 'ITM') return;
	
	if ( ingSw ){
		dialog.alert("진행중입니다. 잠시 후 시도해 주시기 바랍니다.");
		return;
	}
	ingSw = true;
	
	if(tabIndex > 0){
		for (i = 0; tabIndex > i; i++) {
			if (document.getElementById("tab"+i) !== null){
				if (document.getElementById("tab"+i).innerHTML === rsrcName){
					if(!$('#tab'+i).hasClass('on')){
						$("ul.tabs li").removeClass('on');
						$("#tab"+i).addClass('on');
					    $('.tab_content').css('display', 'none');
					    $('#tabSourceView'+i).css('display', 'block');
					}
					ingSw = false;
					return;
				}
			}
		}
	}
	
	if ( $("#tabs li").length === 10 ){
		dialog.alert("10개까지만 동시에 확인가능합니다.");
		ingSw = false;
		return;
	}
	
	//if(rsrcInfo.substr(45,1) == '1') docSw = true;
	if($('#chkDev').is(':checked') && (docSw || rsrcInfo.substr(44,1) == '1')) {
		dialog.alert('로컬에서 개발하는 소스 또는 산출물에 대해서는 개발서버의 소스를 확인할 수 없습니다.');
		ingSw = false;
		return;
	}
	
	if ( $("#tabs li").length === 10 ){
		dialog.alert("10개까지만 동시에 확인가능합니다.");
		ingSw = false;
		return;
	}
	
	var data = {
		ItemId : itemid,
		Version : $('#chkDev').is(':checked') ? 'DEV' : lstver,
		AcptNo : '',
		ynDocFile : docSw,
		gubun : 'true',
		requestType : 'getFileText'
	}
	console.log('getFileText',data);
	var sourceData = ajaxCallWithJson('/webPage/ecmr/Cmr5300Servlet', data, 'json');
	
	if(docSw) {
		fileDown(sourceData[0].src, rsrcName);
		ingSw = false;
	}else {
		tabSize = $("#tabs").outerWidth();
		$("#tabs").append("<li rel='tabSourceView" + tabIndex + "'id='tab" + tabIndex + "' class='sourcetab'></li>"); 
	    $("#content").append("<div id='tabSourceView" + tabIndex + "' class='tab_content' style='width:100%; height:100%;'><iframe id='frmSourceView" + tabIndex + "' name='frmSourceView" + tabIndex + "' src='/webPage/tab/sourceview/SourceViewTab.jsp' width='100%' height='100%' frameborder='0'></iframe></div>");
		document.getElementById('frmSourceView'+tabIndex).onload = function() {
			tmpTab = $('#frmSourceView'+tabIndex).get(0).contentWindow;
			//tmpTab.elementInit();
			tmpTab.tmpDir = tmpDir;
			tmpTab.showSource(sourceData);
			document.getElementById("tab"+tabIndex).innerHTML = rsrcName;
			tabSize2 = tabSize2 + $("#tab" + tabIndex).outerWidth();
			if (tabSize < tabSize2){
				var cnt = $("#tabs li").length;
				var tmpWidth = 95 / cnt;
				$(".sourcetab").siblings().css({"width": tmpWidth + "%"});
			} else {
				$(".sourcetab").siblings().css({"width": "auto"});
			}
			$("ul.tabs li").removeClass('on');
			$("#tab"+tabIndex).addClass('on');
		    $('.tab_content').css('display', 'none');
		    $('#tabSourceView'+tabIndex).css('display', 'block');
		    $("ul.tabs li").unbind('click');
			setTabMenu();
			tabIndex++;
			ingSw = false;
		}
	}
}

function setTabMenu(){
	$("ul.tabs li").mousedown(function (e) {
		e.preventDefault();
		if (e.which == 1){
			if($(this).hasClass('on')) {
				return;
			}
			$(".tab_content").css('display', 'none');
			var activeTab = $(this).attr("rel");
			$("ul.tabs li").removeClass('on');
			$(this).addClass("on");
			$("#" + activeTab).css('display', 'block');
		} else if (e.which == 3){
			clickTab = $(this).attr("id");
			clickTab2 = $(this).attr("rel");
			if ($(this).hasClass('on')){
				onTab = "on";
			} else {
				onTab = "off";
			}
			showRMenu(e);
		}
	});
}

function showRMenu(event) {
	var y = 0;
	var x = 0;
	
	$("#context-menu ul").show(); 
	$("#contextmenu1").show();
	//$("#contextmenu2").show();
	$("#contextmenu3").show();
	$(document).on('contextmenu', function() {
		return false;
	});
	
	x = event.clientX;
	y = event.clientY;
    y += document.body.scrollTop; 
    x += document.body.scrollLeft; 
    rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});
  
    $("body").bind("mousedown", onBodyMouseDown); 
} 

function onBodyMouseDown(event){
	if (!(event.target.id == clickTab || $(event.target).parents("#context-menu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
		$("body").unbind("mousedown", onBodyMouseDown); 
	}
} 

function hideRMenu() { 
	if ($("#context-menu")) $("#context-menu").css({"visibility": "hidden"}); 
	$("body").unbind("mousedown", onBodyMouseDown); 
}

function contextmenu_click(gbn) {
	hideRMenu();
	
	if(clickTab === null) return;
	
	var tabid = clickTab;
    var contentname = clickTab2;
	if (gbn === "1"){
		if ($("#tabs li").length === 1){
			dialog.alert("최소한 하나의 탭은 유지해야 합니다. ");
			return;
		}
        tabSize2 = tabSize2 - $("#" + tabid).outerWidth();
        $("#" + contentname).remove();
        $("#" + tabid).remove();
        if (tabSize < tabSize2){
			var cnt = $("#tabs li").length;
			var tmpWidth = 95 / cnt;
			$(".sourcetab").siblings().css({"width": tmpWidth + "%"});
		} else {
			$(".sourcetab").siblings().css({"width": "auto"});
		}
        if (onTab === "on"){
        	$("ul.tabs li").removeClass('on');
			$('ul.tabs li:last').addClass("on");
        	$('.tab_content:last').show();	
        }
	} else if (gbn === "2"){
		tmpTab = $('#'+contentname.replace("tabSourceView","frmSourceView")).contents();
		var source = "";
		/*
		$($(tmpTab).find("code.hljs")).each(function(){
			source += $(this).html();
		});
		*/
		var textarea = document.createElement("textarea");
		$($(tmpTab).find("code.hljs")).each(function(){
			textarea.value += $(this).html();
		});
		textarea.value = replaceAllString(replaceAllString(replaceAllString(replaceAllString(replaceAllString(replaceAllString(textarea.value,"&amp;","&"),"&#39;","\'"),"&gt;",">"),"&quot;","\""),"&#29;","\'"),"&lt;","<");
		document.body.appendChild(textarea);
		textarea.select();
		document.execCommand("copy");
		document.body.removeChild(textarea);
		
		dialog.alert("소스가 복사되었습니다.");
	} else {
		tmpTab = $('#'+contentname.replace("tabSourceView","frmSourceView")).get(0).contentWindow;
		tmpTab.btnSrcDown_click();
	}
}

function optradio_change() {
	for (i=0; tabIndex>i; i++) {
		if($('#tab'+i).hasClass('on')){
			break;
		}
	}
	
	tmpTab = $('#frmSourceView'+i).get(0).contentWindow;
	tmpTab.txtSearch_change();
	
	$('#txtSearch').val('');

	$('#btnSearch').text('찾기');
	if ($('[name="optradio"]:checked').val() == 'W') {
		$('#txtSearch').prop("placeholder", "검색할 단어를 입력하세요.");
	} else {
		$('#txtSearch').prop("placeholder", "검색할 라인번호를 입력하세요.");
	}
}

function btnSearch_click() {
	for (i = 0; tabIndex > i; i++) {
		if($('#tab'+i).hasClass('on')){
			break;
		}
	}
	tmpTab = $('#frmSourceView'+i).get(0).contentWindow;
	tmpTab.btnSearch_click($('#txtSearch').val().trim(),$('[name="optradio"]:checked').val());
}

function btnQry_click() {
	if($('#txtRsrc').val().trim().length == 0) {
		dialog.alert('검색할 단어를 입력한 후 조회하시기 바랍니다.');
		return;
	}
	
	treeObjData = [];
	treeObj = $.fn.zTree.init($('#treePath'), setting, treeObjData);
	
	$('.sourcetab').remove();
	$('.tab_content').remove();
	
	getTopSysPath($('#txtRsrc').val().trim(), $('#chkUpLow').is(':checked') );
}