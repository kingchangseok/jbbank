var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var selectedItem = null;
var systemSelData = [];
var columnData	= 
	[ 
//		{key : "srid",label : "SR정보",align : "center",width: "14%"}, 
		{key : "cm_sysmsg",label : "시스템",align : "left",width: "10%"}, 
		{key : "cm_jobname",label : "업무명",align : "left",width: "10%"},
		{key : "cm_dirpath",label : "프로그램경로",align : "left",width: "20%"}, 
		{key : "cr_rsrcname",label : "프로그램명",align : "left",width: "14%"}, 
		{key : "jawon",label : "프로그램종류",align : "left",width: "7%"}, 
//		{key : "statusnm",label : "프로그램상태",align : "left",width: "6%"}, 
		{key : "cm_deptname",label : "신청팀",align : "center",width: "5%"},
		{key : "cm_username",label : "신청자",align : "left",width: "5%"}, 
		{key : "acptdate",label : "신청일시",align : "left",width: "7%"}, 
		{key : "dayTerm",label : "경과일수",align : "left",width: "5%"}, 
		{key : "acptno",label : "신청번호",align : "center",width: "7%"}
	];

$('[data-ax5select="systemSel"]').ax5select({
    options: []
});

$('[data-ax5select="cboJob"]').ax5select({
    options: [{text:"전체", value:"0000"}]
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

//체크박스
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
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
	
	//엔터조회
	$("#txtUser, #txtPath, #dayTerm").bind('keypress', function() {
		if(window.event.keyCode == 13) search();
	});
	
	$("#systemSel").bind("change",function(){
		getJobInfo(getSelectedVal("systemSel").value);
	});
});

function search() {
	var inputData = new Object();
	var stDt;
	var edDt;
	var chkDay;
	
//	if($("#txtUser").val().trim().length == 0){
//		dialog.alert("신청자를 입력해 주세요.")
//		return;
//	}
	
	if($('#chkDay').is(':checked')) {
		stDt = replaceAllString($("#dateSt").val(), '/', '');
		edDt = replaceAllString($("#dateEd").val(), '/', '');
		chkDay = "1";
	} else {
		stDt = "";
		edDt = "";
		chkDay = "0";
	}
	
	var sysCd = "";
	if(getSelectedIndex("systemSel") > 0){
		sysCd = getSelectedVal("systemSel").value
	}
	var jobCd = "";
	if(getSelectedIndex("cboJob") > 0){
		jobCd = getSelectedVal("cboJob").value
	}
	inputData = {
		strSys : sysCd,
		stDt : stDt,
		edDt : edDt,
		jobCd : jobCd,
		chkDay : chkDay,
		txtUser : $("#txtUser").val(),
		txtPath : $("#txtPath").val(),
		dayTerm : $("#dayTerm").val(),
		userId  : userid
	}

//플렉스에 없는 옵션
//	if($('#chkNonActive').is(':checked')) { //비활성자
//		inputData.chkActive = "N";
//	} else {
//		inputData.chkActive = "Y";
//	}
	
	ajaxData = {
			data : inputData,
			requestType : "getReqList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/report/ChkOutListReport', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
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
		ajaxAsync('/webPage/common/CommonSysInfo', data, 'json', SuccessGetSysInfo);
}

function SuccessGetSysInfo(data) {
	systemSelData = data;
	console.log("selData= ",data);
	
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
	if($('#chkDay').is(':checked')) {
		$('#dateSt').prop("disabled", false);
		$('#dateEd').prop("disabled", false);	
		$('#btnSt').prop("disabled", false);
		$('#btnEd').prop("disabled", false);	
		$('#dateSt').val(today);
		//$('#dateEd').val(lastMonth);
		$('#dateEd').val(today);
	} else {
		$('#dateSt').prop("disabled", true);
		$('#dateEd').prop("disabled", true);
		$('#btnSt').prop("disabled", true);
		$('#btnEd').prop("disabled", true);	
	}
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
//선택한 시스템에 대한 업무조회 SysInfo.getJobInfo()
function getJobInfo(sysCd) {
	var tmpInfo = new Object();
	tmpInfo.UserID = userid;
	tmpInfo.SelMsg = 'ALL';
	tmpInfo.CloseYn = 'N';		
	tmpInfo.SysCd = sysCd;
	tmpInfo.SecuYn = adminYN ? 'N' : 'Y';
	tmpInfo.sortCd = 'NAME';
	var data = new Object();
	data = {
		jobData		: tmpInfo,
		requestType	: 'getJobInfo'
	}
	ajaxAsync('/webPage/common/CommonSysInfo', data, 'json', successJob);
	data = null;
	tmpInfo = null;
}

function successJob(data) {
	cboJobData = data;	
	if (cboJobData != null && (cboJobData.length > 0)) {
		options = [];
		$.each(cboJobData,function(key,value) {
			options.push({value: value.cm_jobcd, text: value.cm_jobname});
		});
		
		$('[data-ax5select="cboJob"]').ax5select({
	        options: options
		});
	}
}
