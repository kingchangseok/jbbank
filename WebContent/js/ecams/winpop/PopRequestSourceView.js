/** 신청상세 > 소스보기 (eCmr5900.mxml) **/

var pReqNo  = null;
var pItemId = null;
var pUserId = null;

var grdProgHistory = new ax5.ui.grid();
var selOptions 	   = [];

var grdProgHistoryData 	= null;
var selectedGridItem  	= null;
var srcData        		= null;
var srcArray       		= [];

var isAdmin 	   = false;
var ingSw          = false;
var findLine       = 0;
var svWord         = null;
var tmpDir         = null;
var downURL        = null;
var outName        = null;
var prettify       = null;
var tmpTab 		   = null;
var tabIndex	   = 0;
var i			   = 0;
var tabSize		   = 0;
var tabSize2	   = 0;
var ingSw2		   = false;
var clickTab	   = null;
var clickTab2	   = null;
var onTab		   = null;
var rMenu		   = null;

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
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
           this.self.clearSelect();
           this.self.select(this.dindex);
           grdProgView_Click(this.item);
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_rsrcname", label: "프로그램명",		width: '50%', align: 'left'},
        {key: "cr_ver",      label: "버전",			width: '20%'},
        {key: "basename",    label: "기준프로그램",		width: '20%', align: 'left'},
        {key: "cm_dirpath",  label: "프로그램경로",		width: '30%', align: 'left'}
    ]
});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	rMenu = $("#context-menu");
	
	if (pUserId == null || pUserId == '' || pUserId == undefined || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pReqNo == null || pReqNo == '' || pReqNo == undefined || pReqNo.length == 0) {
		dialog.alert('신청번호가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	screenInit('M');
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		close();
	});
	$('#btnSearch').bind('click', function() { 
		btnSearch_click();
	});
	
	//검색단어 Enter키
	$('#txtSearch').bind('keypress', function(event){
		if(event.keyCode==13) {
			event.keyCode = 0;
			btnSearch_click();
		}
	});
	
	getTmpDir('99');
	
	getReqList();
	
});
function setTabMenu(){
	$("ul.tabs li").mousedown(function (e) {
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
			showRMenu();
		}
	});
}
function showRMenu() {
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
/* context menu 숨기기 */
function hideRMenu() { 
	if ($("#context-menu")) $("#context-menu").css({"visibility": "hidden"}); 
	$("body").unbind("mousedown", onBodyMouseDown); 
}
function contextmenu_click(gbn) {
	hideRMenu();
	
	if(clickTab === null) return;
	
	var tabid = clickTab;
    var contentname = clickTab2;
//    console.log(clickTab + "/" + clickTab2);
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
//		console.log('#'+contentname.replace("tabSourceView","frmSourceView"));
		tmpTab.btnSrcDown_click();
	}
}
//환성화 비활성화 초기화로직
function screenInit(gbn){
	if (gbn == 'M') {
		$('#Txt_Acptno').val(pReqNo.substr(0,4)+'-'+pReqNo.substr(4,2)+'-'+pReqNo.substr(6));
		grdProgHistory.setData([]);
		grdProgHistory.repaint();
	}
}
function getTmpDir(dirCd){
	var tmpInfoData = {
			pCode  : 	dirCd,
		requestType: 	'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}
function successeCAMSDir(data) {
	tmpDir = data;
}
function getReqList(){
	var tmpInfoData = {
		     AcptNo: 	pReqNo,
		      gbnCd:	'V',
		requestType: 	'getReqList'
	}
	ajaxAsync('/webPage/ecmr/Cmr5300Servlet', tmpInfoData, 'json', successProgList);
}
function successProgList(data) {
	
	if (data != null && data != '' && data != undefined && typeof(data) == 'string') {
		if (data.indexOf('ERR')>=0) {
			dialog.alert(data);
			return;
		}
	}
	
	grdProgHistoryData = data;
	grdProgHistory.setData(grdProgHistoryData);
	
	if (grdProgHistoryData == null || grdProgHistoryData.length == 0) return;
	var findSw = false;
	if (pItemId != null & pItemId != '' && pItemId != undefined) {
		for (var i=0;grdProgHistoryData.length>i;i++) {
			if (grdProgHistoryData[i].cr_itemid == pItemId) {
				findSw = true;
				break;
			}
		}
	}
	if (!findSw) i = 0;
	
	grdProgHistory.select(i);
	grdProgHistory.focus(i);
	
	grdProgView_Click(grdProgHistoryData[i]);
}
function grdProgView_Click(item) {
	if ( ingSw2 ){
		dialog.alert("진행중입니다. 잠시 후 시도해 주시기 바랍니다.");
		return;
	}
	ingSw2 = true;
	
	if(tabIndex > 0){
		for (i = 0; tabIndex > i; i++) {
			if (document.getElementById("tab"+i) !== null){
				tmpTab = $('#frmSourceView'+i).get(0).contentWindow;
				//if (document.getElementById("tab"+i).innerHTML === item.cr_rsrcname){
				if (tmpTab.pItemId === item.cr_itemid){
					if(!$('#tab'+i).hasClass('on')){
						$("ul.tabs li").removeClass('on');
						$("#tab"+i).addClass('on');
					    $('.tab_content').css('display', 'none');
					    $('#tabSourceView'+i).css('display', 'block');
					}
					ingSw2 = false;
					return;
				}
			}
		}
	}
	
	if ( $("#tabs li").length === 10 ){
		dialog.alert("10개까지만 동시에 확인가능합니다.");
		ingSw2 = false;
		return;
	}
//	console.log("tmpDir:",tmpDir);
	tabSize = $("#tabs").outerWidth();
	$("#tabs").append("<li rel='tabSourceView" + tabIndex + "'id='tab" + tabIndex + "' class='sourcetab'></li>"); 
    $("#content").append("<div id='tabSourceView" + tabIndex + "' class='tab_content' style='width:100%; height:100%;'><iframe id='frmSourceView" + tabIndex + "' name='frmSourceView" + tabIndex + "' src='/webPage/tab/sourceview/SourceViewTab.jsp' width='100%' height='100%' frameborder='0'></iframe></div>");
	document.getElementById('frmSourceView'+tabIndex).onload = function() {
		tmpTab = $('#frmSourceView'+tabIndex).get(0).contentWindow;
		tmpTab.pReqNo = pReqNo;
		tmpTab.pItemId = item.cr_itemid;
		tmpTab.pUserId = pUserId;
		tmpTab.rsrcname = item.cr_rsrcname;
		tmpTab.rsrccd = item.cr_rsrccd;
		tmpTab.viewver = item.cr_aftver;
		tmpTab.realver = item.cr_ver;
		tmpTab.basename = item.basename;
		tmpTab.dirpath =  item.cm_dirpath;
		tmpTab.downURL = downURL;
		tmpTab.tmpDir = tmpDir;
		tmpTab.status = item.cr_status;
		console.log('frmSourceView',item);
//		tmpTab.cboCharacterData = cboCharacterData;
//		tmpTab.encoding = "UTF-8";//초기값은 utf-8로 세팅
		tmpTab.elementInit();
		document.getElementById("tab"+tabIndex).innerHTML = item.cr_rsrcname;
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
		ingSw2 = false;
	}
}
//열려있는 ax5select 닫기
function clearSelect(frame){
	// 프레임들을 전부 찾는다
	var frameList = [];
	frameList.push($('#iFrm'));
	if($('#iFrm').contents().find("iframe").length > 0){
		frameList = $.extend({},frameList, $('#iFrm').contents().find("iframe"));
	}
	for(var i=0; i<frameList.length; i++){
		//프레임중 ax5grid 셀렉트가 열려있는 상태인 값을 찾는다
		var frameSelecte = $(frameList[i]).contents().find("[data-select-option-group-opened='true']");
		if(frameSelecte.length > 0){
			// 열려있는 상태가 있다면 그 프레임의 body에 포커스를 주기위해 클릭을 해 준다
			$(frameList[i]).contents().find("body").click();
		}
	}
}
function optradio_change() {
	for (i = 0; tabIndex > i; i++) {
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