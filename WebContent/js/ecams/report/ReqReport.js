/**
 * [보고서 > 형상관리신청현황] 
 */
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();

var options 	= [];
var cboSinData	= null;
var cboStaData	= null;

$('[data-ax5select="cboJob"]').ax5select({
	options: [{text:"전체", value:""}]
});
$('[data-ax5select="reqDeptSel"]').ax5select({
	options: []
});
$('[data-ax5select="statusSel"]').ax5select({
	options: []
});
var columnData = 
	[ 
		{key : "acptno",		label : "신청번호",	align : "left"}, 
		{key : "sysmsg",		label : "시스템",		align : "left"}, 
		{key : "reqcd",			label : "변경구분",	align : "left"},
		{key : "r_acptdate",		label : "RIMS<br/>신청일시",		align : "left"},
		{key : "deptname",		label : "신청부서",		align : "left"},
		{key : "editor",		label : "신청자",		align : "left"},
		{key : "itsm_cmo",		label : "IT업무포털<br/>CMO",		align : "left"},
		{key : "itsm_cmodate",		label : "IT업무포털<br/>CMO결재일시",		align : "left"},
		{key : "r_selfdate",		label : "RIMS<br/>본인확인일시",		align : "left"},
		{key : "r_teamj",		label : "RIMS<br/>팀장",		align : "left"},
		{key : "r_teamjdate",		label : " RIMS<br/>팀장결재일시",		align : "left"},
		{key : "r_cmo",		label : "RIMS<br/>CMO",		align : "left"},
		{key : "r_cmodate",		label : "RIMS<br/>CMO결재일시",		align : "left"},
		{key : "r_depdate",		label : "RIMS<br/>운영배포일시",		align : "left"},
		{key : "r_enddate",		label : "RIMS<br/>최종본인확인일시",		align : "left", width: '10%'},
		{key : "r_cmolateyn",		label : "RIMS<br/>후결여부(CMO)",		align : "left"},
		{key : "r_befdep",		label : "RIMS<br/>운영배포전 최종결재",		align : "left", width: '10%'},
		{key : "r_befdepdate",		label : "RIMS<br/>운영배포전 최종결재일시",		align : "left",width: '10%'},
		{key : "prjno",		label : "변경ID",		align : "left"},
		{key : "prjname",		label : "변경제목",	align : "left",		width: "13%"}
	];

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

$(document).ready(function() {
	
	$('input:radio[name=radioGroup]').wCheck({theme: 'circle-radial blue', selector: 'checkmark', highlightLabel: true});
	$('input.checkbox-user').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});
	$('#chkJungi').wCheck('check', true);
	
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		showLineNumber : true,
		showRowSelector : false,
		multipleSelect : false,
		lineNumberColumnWidth : 40,
		rowSelectorColumnWidth : 27,
		header : {align: "center",columnHeight: 31},
		body : {
			columnHeight: 24,
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	        onDBLClick: function () {
	         	if (this.dindex < 0) return;
	 			openWindow(1, this.item.acptno.substr(5,2), this.item.acptno,'');
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
				{type: 1, label: "변경신청상세"}
			],
			popupFilter: function (item, param) {
				mainGrid.clearSelect();
				mainGrid.select(Number(param.dindex));
	        	 
	        	var selIn = mainGrid.selectedDataIndexs;
	        	if(selIn.length === 0) return;
	        	 
	         	if (param.item == undefined) return false;
	         	if (param.dindex < 0) return false;
	         	
	         	return item.type == 1;
			},
			onClick: function (item, param) {
				openWindow(item.type, param.item.acptno.substr(5,2), param.item.acptno,'');
				mainGrid.contextMenu.close();
			}
		}
	}); 
	
	$('input:radio[name=radioStd]').wRadio({theme: 'circle-classic blue', selector: 'checkmark', highlightLabel: true});
	getDeptInfo();
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#dateEd').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#dateEd").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
})

function getDeptInfo() {
	var ajaxData = {
			UserId : userid,
			SelMsg			: 'All',
			cm_useyn		: 'Y',
			requestType	: 'getTeamInfoGrid'
	};
	ajaxAsync('/webPage/common/TeamInfoServlet', ajaxData, 'json', SuccessGetDeptInfo);
}

function SuccessGetDeptInfo(data) {
	options = [];
	$.each(data, function(i, value) {
		options.push({value : value.cm_deptcd, text : value.cm_deptname});
	});
	options[0].value = '';
	
	$('[data-ax5select="reqDeptSel"]').ax5select({
		options: options
	});	
}

//picker에 오늘 날짜 디폴트로 세팅
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
	$("#dateEd").val(today);
	$("#dateSt").val(today);
})

//조회 클릭 시
$("#btnSearch").bind('click', function() {
	search();
})

$("#optEnd").bind('click', function() {
	$('#chkDepYn').wCheck('check', true);
})

//조회
function search() {
	var inputData = new Object();
	var strAcptOrEnd = "";
	var strjungi = "";
	var strdepyn = "";
	
	if($('#optAcpt').is(':checked')){
		strAcptOrEnd = "A";
	} else {
		strAcptOrEnd = "E";
		$('#chkDepYn').wCheck('check', true);
	}
	
	if($('#chkJungi').is(':checked')){
		strjungi = "1";
	} else {
		strjungi = "0";
	}
	
	if($('#chkDepYn').is(':checked')){
		strdepyn = "1";
	} else {
		strdepyn = "0";
	}
	
	ajaxData = {
			pStartDt : replaceAllString($("#dateSt").val(), '/', ''),
			pEndDt : replaceAllString($("#dateEd").val(), '/', ''),
			strTeamCD : getSelectedVal('reqDeptSel').value,
			pAorE : strAcptOrEnd,
			pJungi : strjungi,
			pDepYn : strdepyn,
			requestType : "getReqList"
	}
		
	var ajaxResult = ajaxCallWithJson('/webPage/ecmp/Cmp0600Servlet', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
}

//엑셀
$("#btnExcel").on('click', function() {
	var st_date = new Date().toLocaleString();
	var today = st_date.substr(0, st_date.indexOf("오"));
	
	mainGrid.exportExcel("전체승인내역 " + today + ".xls");
})

//contextMenu 화면 호출
function openWindow(type,reqCd,reqNo,rsrcName) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userid;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 1) {
		nHeight = 740;
	    nWidth  = 1200;

		cURL = "/webPage/winpop/PopRequestDetail.jsp";
	    
	} 
	
	//console.log('+++++++++++++++++'+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
