var mainGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부
var codeList      = window.top.codeList;      //전체코드리스트

var comboData = [];
var columnData = [];
var cboJobData  = null;	//업무 데이터
var cboPeriodData = null;

var beforeSelected = false;
var theme = {
	    series: {
	        series: {
	            colors: [
	            ]
	        },
	        label: {
	            color: '#fff',
	            fontFamily: 'sans-serif'
	        }
	    }
	};

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});
$('[data-ax5select="cboSrJob"]').ax5select({
    options: []
});
$('[data-ax5select="cboPeriod"]').ax5select({
	options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

//이 부분 지우면 영어명칭으로 바뀜
//ex) 월 -> MON
ax5.info.weekNames = [
{label: "일"},
{label: "월"},
{label: "화"},
{label: "수"},
{label: "목"},
{label: "금"},
{label: "토"}
];

$(document).ready(function() {
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		sortable: true, 
		showLineNumber : false,
		showRowSelector : false,
		multipleSelect : false,
		paging: false,
		lineNumberColumnWidth : 40,
		rowSelectorColumnWidth : 27,
	    header: {
	        align: "center",
	        columnHeight: 25
	    },
		body : {
			columnHeight: 24,
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	        trStyleClass: function () {
	        	//console.log(this.item[mainGrid.columns[this.index].key]);
	        },
	        mergeCells:["cm_sysmsg"]
		},
		columns: [
//			{key: "cm_sysmsg", 	label: "시스템",  			width: 120, align: 'center'},
			{key: "cm_username", 	label: "담당자",  			width: 60, align: 'center'},
			/*{key: "cm_jobname", 	label: "SR등록",  			width: "6%", align: 'center'},*/
			{key: "SR1", 	label: "SR등록",  			width: "4%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "DEVPLAN", 	label: "SR접수",  			width: "4%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "DEVPLANCONF", 	label: "개발계획서결재중",  			width: "8%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
//			{key: "DEVMASTERCONFIRMM", 	label: "1차개발책임자승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "DEVBACONFIRMM", 	label: "1차BA승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "DEVITCONFIRMM", 	label: "1차팀장승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
			{key: "DEVSTAY", 	label: "개발계획서완료",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "DEVING", 	label: "개발중",  			width: "4%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "DEPLOY", 	label: "개발완료",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
//			{key: "SYSDCB", 	label: "개발빌드",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "SYSFT", 	label: "취약점점검",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
			{key: "SYSDED", 	label: "개발적용중",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
//			{key: "STEPTEST", 	label: "단위테스트",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
			{key: "OWNERD", 	label: "개발적용완료",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
//			{key: "TC57", 	label: "배포담당자승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "SYSTCB", 	label: "테스트빌드",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "DBAT", 	label: "DBA작업및승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "TE57", 	label: "배포담당자승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
			{key: "SYSTED", 	label: "테스트적용중",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "TOTTEST", 	label: "통합테스트",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "FLDTEST", 	label: "현업테스트",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "TOTTEST2", 	label: "현업이행검증승인",  			width: "8%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "REQREAL", 	label: "운영적용요청대기",  			width: "8%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			{key: "REALCONF", 	label: "운영적용요청결재중",  			width: "8%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
//			{key: "OWNERT", 	label: "개발책임자승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "BA", 	label: "BA확인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "QA", 	label: "QA승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "ITQA", 	label: "ICT감사승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "IT", 	label: "개발/인사부서장승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "RC57", 	label: "배포담당자승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "SYSCB", 	label: "운영빌드",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "DBAR", 	label: "DBA작업및승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
//			{key: "RE57", 	label: "배포담당자승인",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
			{key: "SYSED", 	label: "운영적용중",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
//			{key: "SR2", 	label: "SR완료",  			width: "6%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){return "color-red";}}},
			{key: "TOTAL", 	label: "합계",  			width: "4%", align: 'center', styleClass: function(){if(this.item[this.column.key] > 0){this.item[this.column.key] = this.item[this.column.key]*1; return "color-red";}}},
			],
			footSum : [
				[{label: "전체", colspan:1, align: "center"},
			/*{key: "cm_jobname", 	label: "SR등록",  			width: 80, align: 'center'},*/
			{key: "SR1", 	collector:"sum", align: 'center'},
			{key: "DEVPLAN", 	collector:"sum", align: 'center'},
			{key: "DEVPLANCONF", 	collector:"sum", align: 'center'},
//			{key: "DEVMASTERCONFIRMM", 	collector:"sum", align: 'center'},
//			{key: "DEVBACONFIRMM", 	collector:"sum", align: 'center'},
//			{key: "DEVITCONFIRMM", 	collector:"sum", align: 'center'},
			{key: "DEVSTAY", 	collector:"sum", align: 'center'},
			{key: "DEVING", 	collector:"sum", align: 'center'},
			{key: "DEPLOY", 	collector:"sum", align: 'center'},
//			{key: "SYSDCB", 	collector:"sum", align: 'center'},
//			{key: "SYSFT", 	collector:"sum", align: 'center'},
			{key: "SYSDED", 	collector:"sum", align: 'center'},
//			{key: "STEPTEST", 	collector:"sum", align: 'center'},
			{key: "OWNERD", collector:"sum", align: 'center'},
//			{key: "TC57", collector:"sum", align: 'center'},
//			{key: "SYSTCB", 	collector:"sum", align: 'center'},
//			{key: "DBAT", 	collector:"sum", align: 'center'},
//			{key: "TE57", 	collector:"sum", align: 'center'},
			{key: "SYSTED", collector:"sum", align: 'center'},
			{key: "TOTTEST", 	collector:"sum", align: 'center'},
			{key: "FLDTEST", 	collector:"sum", align: 'center'},
			{key: "TOTTEST2", 	collector:"sum", align: 'center'},
			{key: "REQREAL", 	collector:"sum", align: 'center'},
			{key: "REALCONF", 	collector:"sum", align: 'center'},
//			{key: "OWNERT", 	collector:"sum", align: 'center'},
//			{key: "BA", 	collector:"sum", align: 'center'},
//			{key: "QA", 	collector:"sum", align: 'center'},
//			{key: "ITQA", 	collector:"sum", align: 'center'},
//			{key: "IT", 	collector:"sum", align: 'center'},
//			{key: "RC57", 	collector:"sum", align: 'center'},
//			{key: "SYSCB", 	collector:"sum", align: 'center'},
//			{key: "DBAR", 	collector:"sum", align: 'center'},
//			{key: "RE57", 	collector:"sum", align: 'center'},
			{key: "SYSED", 	collector:"sum", align: 'center'},
			{key: "TOTAL", 	collector:"sum", align: 'center'},
//			{key: "SR2", 	collector:"sum", align: 'center'}
			]]
	});

	//시스템
	$('#cboSys').bind('change', function() {
		//cboSysCd_Change();
	});
	setCboElement();
	getSysinfo();
})

function setCboElement() {
	cboPeriodData  = fixCodeList(codeList['PERIOD'], 'ALL', 'cm_micode', 'ASC', 'N');
	codeList = null;
	
	options = [];
	
	cboPeriodData[0].cm_micode = '00';
	$('[data-ax5select="cboPeriod"]').ax5select({
		options: injectCboDataToArr(cboPeriodData, 'cm_micode' , 'cm_codename')
	});
}

//시스템 콤보박스 세팅
function getSysinfo() {
	
	var ajaxData = new Object();
	var ajaxResult = new Object();
	var sysData = new Object();
	sysData = {
		UserId : userid,
		SelMsg : "ALL"
	}
	ajaxData.requestType = "getSysInfoDept";
	ajaxData.sysData = sysData;
	
	ajaxResult = ajaxCallWithJson('/webPage/common/SysInfoServlet', ajaxData, 'json');

	var options = injectCboDataToArr(ajaxResult, 'cm_syscd', 'cm_sysmsg');
	$('[data-ax5select="cboSys"]').ax5select({
        options: options,
        multiple: true,
		reset:'<i class=\'fa fa-trash\'></i>',
        onChange: function () {
        	//멀티 셀렉트박스 선택된 값 없을 때 자동으로 "전체"가 선택되도록하는 코드
            if(this.value.length == 0) {
            	$('[data-option-index="0"]')[0].dataset.optionSelected = "true";
            	$('[data-ax5select="cboSys"]').ax5select("setValue", '00000', true);
            	beforeSelected = false;
        	}else if(this.value[0].value == "00000" && beforeSelected) {
            	//$(".addon-icon-reset").trigger("click");
        		$(this.value).each(function(){
        			$('[data-ax5select="cboSys"]').ax5select("setValue", this.value, false);
        			var index = this["@index"];
                	$('[data-option-index="'+ index +'"]')[0].dataset.optionSelected = "false";
        		});
            	$('[data-option-index="0"]')[0].dataset.optionSelected = "true";
            	$('[data-ax5select="cboSys"]').ax5select("setValue", '00000', true);           	
            	beforeSelected = false;
            } else if(this.value.length > 1 && this.value[0].value == "00000") {
            	$('[data-option-index="0"]')[0].dataset.optionSelected = "false";
            	$('[data-ax5select="cboSys"]').ax5select("setValue", '00000', false);           	
            	beforeSelected = true;
            }
        }
	});

	$('[data-ax5select="cboSys"]').ax5select("setValue", '00000', true);
//	comboData.shift();
	//cboSysCd_Change();
}

function cboSysCd_Change() {

	var srJobData = new Object();
	if(getSelectedIndex("cboSys") < 1){
		$('[data-ax5select="cboSrJob"]').ax5select(
				{
					options : []
				});
		return;
	} else {
		srJobData.sysCd = getSelectedVal("cboSys").value;
	}
	var data = {
			srJobData : srJobData,
		requestType : 'getSrJobList'
	}

	ajaxAsync('/webPage/administrator/SysInfoServlet',data, 'json', successGetSrJob);	

}

function successGetSrJob(data){
	if (data !== 'ERR') {
		cboSrJobData = data;
		options = [];
		cboSrJobData.splice(0,0,{"cm_srjobcd": "", "cm_srjobname" : "전체"})
		$('[data-ax5select="cboSrJob"]').ax5select(
				{
					options : injectCboDataToArr(cboSrJobData, 'cm_srjobcd',
							'cm_srjobname')
				});
	}
}

//조회 클릭 시
$("#btnSearch").bind('click', function() {
	mainGrid.setData([]);
	
	columnData = [];
	if($("#btnExcel").length < 1) {
		$("#btnDiv").append('<button class="btn_basic_s" data-grid-control="excel-export" style="width: 70px;" id="btnExcel">엑셀저장</button>');
	}
	getWeeklySRList();
});

//그리드 컬럼 세팅
function getWeeklySRList() {
	var ajaxData = new Object();
	var ajaxResult = new Object();
	
	ajaxData.requestType = "getNormalPrccnt";
	

	var cc_syscd = "";
	if(getSelectedIndex('cboSys') > 0){
		var lst = $('[data-ax5select="cboSys"]').ax5select("getValue");
		$.each(lst, function(i, val) {
			cc_syscd += val.value + ",";
		});
		cc_syscd = cc_syscd.substr(0, cc_syscd.length - 1);
	} else {
		cc_syscd = "00000";
	}
	
	ajaxData.syscd = cc_syscd;
	
	ajaxData.srJobCd = "";
	if(getSelectedIndex("cboSrJob") > 0){
		ajaxData.srJobCd = getSelectedVal("cboSrJob").value;
	}
	
	ajaxData.period = "";
	if(getSelectedIndex("cboPeriod") > 0){
		ajaxData.period = getSelectedVal("cboPeriod").value;
	}

	ajaxAsync('/webPage/report/WeeklySRReport', ajaxData, 'json', successGetWeeklySRList);	
	
}
function successGetWeeklySRList(data){
	mainGrid.setData(data);
}
//엑셀저장
$("#btnDiv").on('click', '#btnExcel', function() {
	var st_date = new Date().toLocaleString();
	var today = st_date.substr(0, st_date.indexOf("오"));
	
	mainGrid.exportExcel("SR통계_" + today + ".xls");
});

//셀렉트 박스 툴팁
$(document).on("mouseenter","[data-ax5select='cboSys']",function(event){
	
	var status = $('[data-ax5select="cboSys"]')[0].children[1].getAttribute("data-select-option-group-opened");
	var selectVal = $('[data-ax5select="cboSys"]').ax5select("getValue");
	var selectTxt = "";
	var iconTop = 11;
	
	if(!status && selectVal[0].value != "00000") {		
		$.each(selectVal, function(i, val) {
			selectTxt += "<div><span class='checkIcon' style='top:"+ iconTop +"px;'/><label class='tipLabel'>" 
						+ htmlFilter(val.text) + "</label></div>";
			iconTop += 20;
		});
		
		$(this).attr("title",selectTxt);
		
		// title을 변수에 저장
		title_ = $(this).attr("title");
		// class를 변수에 저장
		class_ = $(this).attr("class");
		// title 속성 삭제 ( 기본 툴팁 기능 방지 )
		$(this).attr("title","");
		
		$("body").append("<div id='tip'></div>");
		if (class_ == "img") {
			$("#tip").html(imgTag);
			$("#tip").css("width","100px");
		} else {
			$("#tip").css("display","inline-block");
			$("#tip").html('<pre>'+title_+'</pre>');
		}
		
		var pageX = $(this).offset().left + $(this).width();
		var pageY = $(this).offset().top;
		
		$("#tip").css({left : pageX + "px", top : pageY + "px", "z-index" : "10"}).fadeIn(100);
		return;
	}
}).on('mouseleave',"[data-ax5select='cboSys']",function(){
	$(this).attr("title", '');
	$("#tip").remove();	
});
