var mainGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;		// 관리자여부
var codeList      = window.top.codeList;      //전체코드리스트

var comboData = [];
var cboJobData  = null;	//업무 데이터
var cboPeriodData = null;
var winDevRep = null;

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

//피커세팅
picker.bind({
	target: $('[data-ax5picker="picker"]'),
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
	onStateChanged: function () {
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

//피커에 오늘 날짜 세팅
$(function() {
	var today = new Date().toISOString().substring(0,10).replace(/-/gi, "/");
	$("#date").val(today);
});

$(document).ready(function() {
	mainGrid.setConfig({
		target : $('[data-ax5grid="mainGrid"]'),
		sortable: true, 
		showLineNumber : true,
		showRowSelector : false,
		multipleSelect : false,
		lineNumberColumnWidth : 40,
		rowSelectorColumnWidth : 27,
	    header: {
	        align: "center",
	        columnHeight: 25
	    },
		body : {
			columnHeight: 24,
			onDBLClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	            openSRInfo(this.item.cc_srid);
	        },
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	        trStyleClass: function () {
/*	    		if(this.item.cc_status == "03" || this.item.cc_status == "04" || this.item.cc_status == "47"){ //반환,취소,삭제
	    			return "fontStyle-cncl";
	     		}else if(this.item.cc_status === '09'){//처리완료
	     			return "fontStyle-end";
	     		} else {//진행중
	    			return "fontStyle-ing";
	     		}*/
	        }
		},
	    columns: [
	    	{key: "cm_sysmsg", label: "시스템",  width: 80, align: 'left'},
	    	{key: "cm_srjobname", label: "SR업무",  width: 60, align: 'center'},
	        {key: "cc_upsrid", label: "Main-SR",  width: 100, align: 'center'},
	        {key: "srid", label: "Sub-SR",  width: 100, align: 'center'},
	        {key: "srtype", label: "분류유형",  width: 100, align: 'center'},
	        {key: "cc_title", label: "SR제목",  width: 310, align:'left'},
	        {key: "cm_codename", label: "현재 SR상태",  width: 90, align: 'center'},
	        {key: "enddt", label: "SR완료,취소일",  width: 110, align: 'center'}	,
	        {key: "detailnm", label: "순위",  width: 45, align: 'center'},
	        {key: "deptnm", label: "요청부서",  width: 110, align:'left'},
	        {key: "cm_username", label: "요청자",  width: 60, align: 'center'}, 
	        {key: "addusername", label: "접수자",  width: 60, align: 'center'},
	        //{key: "devname", label: "개발책임자",  width: 85, align: 'center'},
	        {key: "devname", label: "개발담당자",  width: 85, align: 'center'},
	        //{key: "tstusernm", label: "테스트담당자",  width: 85, align: 'center'},
	        {key: "pgmid", label: "PGMID",  width: 65, align: 'center'},
	        {key: "pgmowner", label: "PGM담당",  width: 75, align: 'center'},
	        {key: "bauser", label: "BA",  width: 60, align: 'center'},
	        {key: "period", label: "기간구분",  width: 60, align: 'center'},
	        {key: "creatdt", label: "등록일",  width: 110, align: 'center'}	,
	        {key: "adddate", label: "접수일",  width: 110, align: 'center'}	,
	        {key: "cr_prcdate", label: "최초체크아웃/체크인일자",  width: 110, align: 'center'}	,
	        //{key: "reqeddt", label: "적용완료일시",  width: 110, align: 'center'}	,
	        {key: "cc_emgyn", label: "선처리여부",  width: 100, align: 'center'}	,
	        //{key: "cc_procmsg", label: "처리내용",  width: 120, align: 'center'}	,
	        {key: "dbyn", label: "DB작업",  width: 100, align: 'center'}	,
	        {key: "cc_reqid", label: "문서번호",  width: 80, align:'left'},
	        {key: "wishdate", label: "완료희망일",  width: 85, align: 'center'},
	        {key: "devstdt", label: "착수예정일",  width: 85, align: 'center'},
	        {key: "deveddt", label: "완료예정일",  width: 85, align: 'center'},
	        {key: "timeline", label: "추진일정",  width: 80, align: 'center'}, 
	        //{key: "cnclusr", label: "취소자",  width: 80, align: 'center'}, 
	        //{key: "cncldate", label: "취소일시",  width: 110, align: 'center'}, 
	        {key: "copyusr", label: "등록복사자",  width: 85, align: 'center'},
	        {key: "copydate", label: "복사일시",  width: 110, align: 'center'},
	        {key: "cc_delaytxt", label: "지연사유",  width: 110, align: 'center'}, 
	        {key: "cc_holdyn", label: "SR보류여부",  width: 80, align: 'center'}, 
	        {key: "cc_holdsdt", label: "보류시작일자",  width: 80, align: 'center'}, 
	        {key: "cc_holdedt", label: "보류종료일자",  width: 80, align: 'center'}, 
	        {key: "cc_holdtxt", label: "보류사유",  width: 110, align: 'center'}
	    ]
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
	
	$.each(ajaxResult, function(i, value) {
		comboData.push({ value : value.cm_syscd, text : value.cm_sysmsg });
	});
	
	$('[data-ax5select="cboSys"]').ax5select({
		options: comboData,
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
	
	comboData.shift();
	//cboSysCd_Change();
}

// SR업무 조회조건 사용 안함으로 주석처리
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
	
	if($("#btnExcel").length < 1) {
		$("#btnDiv").append('<button class="btn_basic_s" data-grid-control="excel-export" style="width: 70px;" id="btnExcel">엑셀저장</button>');
	}
	getSRListIncomplete();
});

//그리드 컬럼 세팅
function getSRListIncomplete() {
	var ajaxData = new Object();
	var data = new Object();
	var ajaxResult = new Object();
	
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
	
	data = {
			cc_syscd : cc_syscd,
			cc_period : "",
			searchDate :  $("#date").val().trim()
	}
	
	data.cc_emgyn = "A";
	data.cc_status = "0000";
	data.searchCon = "ALL";
	data.searchText = "";

	if(getSelectedIndex("cboSrJob") > 0){
		data.cc_srJobCd = getSelectedVal("cboSrJob").value;
	}
	
	if(getSelectedIndex("cboPeriod") > 0){
		data.cc_period = getSelectedVal("cboPeriod").value;
	}
	
	ajaxData.requestType = "getSRListIncomplete";
	ajaxData.data = data;

	ajaxAsync('/webPage/report/WeeklySRReport', ajaxData, 'json', successGetSRListIncomplete);	
}

function successGetSRListIncomplete(data){
	mainGrid.setData(data);
}

//엑셀저장
$("#btnDiv").on('click', '#btnExcel', function() {
	var st_date = new Date().toLocaleString();
	var today = st_date.substr(0, st_date.indexOf("오"));
	
	mainGrid.exportExcel("미완료SR현황_" + today + ".xls");
});


/*
 * SR 정보 
 */
function openSRInfo(srid){
	
	//ExternalInterface.call("winopen",userId,"SRINFO",cboIsrId.selectedItem.cc_srid);
	var nHeight, nWidth;
	if(winDevRep != null
			&& !winDevRep.closed) {
		winDevRep.close();
	}	
	
	var form = document.popPam;   						  //폼 name
	form.user.value = userId; 	 						  //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.srid.value = srid;    //POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)	
	form.acptno.value = '';

	nHeight = 510;
	nWidth = 1100;
    
    winDevRep = winOpen(form, 'srInfo', '/webPage/winpop/PopSRInfo.jsp', nHeight, nWidth);
}

//셀렉트 박스 툴팁
$(document).on("mouseenter","[data-ax5select='cboSys']",function(event){
	
	var status = $('[data-ax5select="cboSys"]')[0].children[1].getAttribute("data-select-option-group-opened");
	var selectVal = $('[data-ax5select="cboSys"]').ax5select("getValue");
	var selectTxt = "";
	var iconTop = 11;
	
	if(!status && selectVal[0].value != "00000") {		
		$.each(selectVal, function(i, val) {
			selectTxt += "<div><span class='checkIcon' style='top:"+ iconTop +"px;'/><label class='tipLabel'>" 
						+ val.text + "</label></div>";
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