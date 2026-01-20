var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부
var codeList    = window.top.codeList;          //전체 코드 리스트
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var cboSinData	= null;
var ajaxResult2 = null;


$('[data-ax5select="cboUser"]').ax5select({
	options: []
});

var columnData	= [];

$('[data-ax5select="systemSel"]').ax5select({
    options: []
});

picker.bind({
    target: $('[data-ax5picker="basic"]'),
    direction: "top",
    content: {
        width: 220,
        margin: 10,
        type: 'date',
        config: {
            control: {
                left: '<i class="fa fa-chevron-left"></i>',
                yearTmpl: '%s',
                monthTmpl: '%s',
                right: '<i class="fa fa-chevron-right"></i>'
            },
            dateFormat: 'yyyy/MM/dd',
            lang: {
                yearTmpl: "%s년",
                months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
                dayTmpl: "%s"
            }
        },
        formatter: {
            pattern: 'date'
        }
    },
    btns: {
        today: {
            label: "Today", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
                        .close();
            }
        },
        thisMonth: {
            label: "This Month", onClick: function () {
                var today = new Date();
                this.self
                        .setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
                        .setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
                                + '/'
                                + ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
                        .close();
            }
        },
        ok: {label: "Close", theme: "default"}
    }
});

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            selectedItem = this.item;
        },
	},
	columns : columnData,
	contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
        	{type: 1, label: "프로그램정보"}
        ],
        popupFilter: function (item, param) {
        	mainGrid.clearSelect();
        	mainGrid.select(Number(param.dindex));
            return true;
        },
        onClick: function (item, param) {
        	mainGrid.contextMenu.close();
        	openWindow(item.type, 'win', '', param.item);
        }
   }
});


$(document).ready(function() {
	//체크박스
	$('input:radio[name=radioGroup]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	$('input.checkbox-user').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
	$(function() {
		var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
		$("#dateEd").val(today);
		$("#dateSt").val(today);
	})
	
	//콤보박스 세팅
	$('[data-ax5select="cboSel"]').ax5select({
	options: [
		{value: "0", text: "전체"},
		{value: "1", text: "시스템별"},
		{value: "3", text: "프로그램종류별"},
		{value: "5", text: "변경구분별"}
		]
	});
	
	$('[data-ax5select="cboReq"]').ax5select({
		options: [
			{value: "1", text: "리소스기준"},
			{value: "0", text: "소스기준"}
			]
	});
	
	getCodeInfo();
	setDateEnable();
	getSysInfo();
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	//조회
	$("#btnSearch").bind('click', function() {
		search();
	});
	//엑셀
	$("#btnExcel").on('click', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		mainGrid.exportExcel("장기체크아웃현황 " + today + ".xls");
	});

	$('#chkDay').bind('click', function() {
		setDateEnable();
	});
	
	$("#txtUser").bind('keypress', function() {
		if(window.event.keyCode == 13){
			getUserData();
		};
	});
});

function getCodeInfo() {
	/*
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('REQPASS','ALL','N'),
		new CodeInfo('CHECKIN','ALL','N'),
		]);
	cboSinData 		= codeInfos.REQPASS;
	cboStaData		= codeInfos.CHECKIN;
	*/
	cboSinData = fixCodeList(codeList['REQCD'], 'ALL', 'cm_codename', '', 'N');
	
	options = [];
	$.each(cboSinData, function(i, value) {
		options.push({value : value.cm_micode, text : value.cm_codename});
	});
	options[0].value = '';
	$('[data-ax5select="cboReqCd"]').ax5select({
		options: options
	});
	
}

function search() {
	var inputData = new Object();
	var stDt;
	var edDt;
	var chkDay;
	
	stDt = replaceAllString($("#dateSt").val(), '/', '');
	edDt = replaceAllString($("#dateEd").val(), '/', '');
	
	var sysCd = "";
	var reqcd = "";
	var term = "";
	var baseitem = "";
	var user = "";
	if(getSelectedIndex("systemSel") > 0){
		sysCd = getSelectedVal("systemSel").value
	}
	
	if(getSelectedIndex("cboReqCd") > 0){
		reqcd = getSelectedVal("cboReqCd").value
	}
	
	if(getSelectedIndex("cboUser") > 0){
		user = getSelectedVal("cboUser").value
	}
	
	
	if($('#optDay').is(':checked')){
		term = "D";
	} else {
		term = "Y";
	}
	
	inputData = {
		qrygbn : getSelectedVal("cboSel").value,
		syscd : sysCd,
		userid : user,
		reqcd : reqcd,
		rsrccd : "",
		qrycd : "04",
		stday : stDt,
		edday : edDt,
		term : term,
		baseitem : getSelectedVal("cboReq").value
	}

	ajaxData = {
	data : inputData,
	requestType : "getTitle_sub"
	}
	
	ajaxResult2 = ajaxCallWithJson('/webPage/report/ChkOutListReport', ajaxData, 'json');
	
	search2();
}

function search2() {
	var inputData = new Object();
	var stDt;
	var edDt;
	var chkDay;
	
	stDt = replaceAllString($("#dateSt").val(), '/', '');
	edDt = replaceAllString($("#dateEd").val(), '/', '');
	
	var sysCd = "";
	var reqcd = "";
	var term = "";
	var baseitem = "";
	var user = "";
	if(getSelectedIndex("systemSel") > 0){
		sysCd = getSelectedVal("systemSel").value
	}
	
	if(getSelectedIndex("cboReqCd") > 0){
		reqcd = getSelectedVal("cboReqCd").value
	}
	
	if(getSelectedIndex("cboUser") > 0){
		user = getSelectedVal("cboUser").value
	}
	
	
	if($('#optDay').is(':checked')){
		term = "D";
	} else if($('#optWeek').is(':checked')){
		term = "W";
	} else {
		term = "Y";
	}
	
	
	var jobCd = "";
	inputData = {
		qrygbn : getSelectedVal("cboSel").value,
		syscd : sysCd,
		userid : user,
		reqcd : reqcd,
		rsrccd : "",
		stday : stDt,
		edday : edDt,
		term : term,
		baseitem : getSelectedVal("cboReq").value
	}

	ajaxData = {
	data : inputData,
	ajaxResult2 : ajaxResult2,
	requestType : "getSelect_List"
	}
	
	var ajaxResult = ajaxCallWithJson('/webPage/report/ChkOutListReport', ajaxData, 'json');
	
	var dayText = "";
	var prcText = "";
	if(term == "D"){
		dayText = "일별";
	} else if(term == "W"){
		dayText = "주간별";
	} else {
		dayText = "월별";
	}
	
	if(getSelectedVal("cboSel").value == "1"){
		prcText = "처리건수"
	} else {
		prcText = "합계"
	}
	
	columnData = [];
	columnData.push({key : "daygbn", label : dayText, align : "center", width: "10%"});
	columnData.push({key : "prccnt", label : prcText, align : "center", width: "10%"});
	
	if(ajaxResult2[0].errmsg == undefined){
		$.each(ajaxResult2, function(i, value) {
			columnData.push({key : "gbn" + value.cm_micode , label : value.cm_codename, align: "center", width : "10%"});
		});
	}
	ajaxResult2 = null;
	
	mainGrid.setConfig({columns: null});
	mainGrid.setConfig({
		columns : columnData
	});
	
	mainGrid.setData(ajaxResult);
}

function getUserData(){
	inputData = {			
		clsCd : "N",
		userN :  $("#txtUser").val()
	}
	
	ajaxData = {
			prjData : inputData,
			requestType : "getUser"
	}
		
	ajaxAsync('/webPage/report/ConfigReqReport', ajaxData, 'json', SuccessgetUser);
}

function SuccessgetUser(data){
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_userid, text : value.username});
	});
	options[0].value = '';
	
	$('[data-ax5select="cboUser"]').ax5select({
		options: options
	});	
}

function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userid,
		SecuYn : "N",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
		ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	systemSelData = data;
	
	systemSelData = systemSelData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	});
	
	$('[data-ax5select="systemSel"]').ax5select({
        options: injectCboDataToArr(systemSelData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function setDateEnable() {
	var today = getDate('DATE',0);
	var lastMonth = getDate('DATE', -30);
	today = today.substr(0,4) + '/' + today.substr(4,2) + '/' + today.substr(6,2);
	lastMonth = lastMonth.substr(0,4) + '/' + lastMonth.substr(4,2) + '/' + lastMonth.substr(6,2);
}

function openWindow(type,reqCd,reqNo,item) {
	var nHeight, nWidth, nTop, nLeft, cURL, cFeatures, winName;
	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
    winName = type+'_'+reqCd;
	nHeight = 375;//screen.height - 300;
    nWidth  = 1200;//screen.width - 400;
    cURL = "/webPage/winpop/PopProgramInfo.jsp";
	var winWidth  = document.body.clientWidth;  // 현재창의 너비
	var winHeight = document.body.clientHeight; // 현재창의 높이
	var winX      = window.screenX;// 현재창의 x좌표
	var winY      = window.screenY; // 현재창의 y좌표
	nLeft = winX + (winWidth - nWidth) / 2;
	nTop = winY + (winHeight - nHeight) / 2;

	//cFeatures = "top=" + nTop + ",left=" + nLeft + ",height=" + nHeight + ",width=" + nWidth + ",help=no,menubar=no,status=yes,resizable=yes,scroll=no";
	
	var f = document.popPam;   		//폼 name
	f.acptno.value	= '';    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userid;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.itemid.value	= item.cr_itemid;
	f.syscd.value = item.cm_syscd;
	f.rsrccd.value = item.cr_rsrccd;
	f.rsrcname.value = item.cr_rsrcname.trim();

	/*
	var jsonString 	= JSON.stringify(window.top.codeList);
	var codeListInput  = document.createElement('input');
	if(f.querySelector('[name="codeList"]') == null) {
		// 2020 03 10 새창 띄울시 ecamsBase의 전체 코드 리스트 폼에 넣어주기 (" > % 로 공백 > $ 로 변환시켜 넘겨주기) 
	    jsonString = replaceAllString(replaceAllString(jsonString,'"','%'),' ','$');
	    codeListInput.type   = 'hidden';
	    codeListInput.name  = 'codeList';
	    codeListInput.value  = jsonString;
	    f.appendChild(codeListInput);
	}
	*/

    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    /*
    f.action		= cURL; 		//이동할 페이지
    f.target		= winName;    	//폼의 타겟 지정(위의 새창을 지정함)
    f.method		= "post"; 		//POST방식
    f.submit();
    */
}

