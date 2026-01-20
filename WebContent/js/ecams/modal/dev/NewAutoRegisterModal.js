var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var popSelItem 	= window.parent.popSelItem;

var regGrid		 = new ax5.ui.grid();
//var regGrid2	 = new ax5.ui.grid();
var exportObject = new Object();

var regGridData   = [];
var regGrid2Data  = [];
var orgRegGridData = [];

var sysCd = window.parent.selSys;
var jobCd = window.parent.selJob;
var sysinfo = window.parent.selSysInfo;
var cboJobData = window.parent.cboJobData;
var i = 0;

regGrid.setConfig({
    target: $('[data-ax5grid="regGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
            var selItem = regGrid.list[this.dindex];
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "column0", 	label: "업무코드",  	width: '7%'},
        {key: "column1",	label: "사번",  		width: '6%'},
        {key: "column2",	label: "프로그램명",  	width: '27%', align: 'left'},
        {key: "column3",	label: "프로그램설명", 	width: '18%', align: 'left', editor: {type: "text", disabled: false}},
        {key: "column4",	label: "디렉토리",  	width: '23%', align: 'left'},
        {key: "column5",	label: "자원종류명칭", 	width: '10%', align: 'left'},
        {key: "column6",	label: "언어명칭",  	width: '8%'}
        /*{key: "column7",	label: "자원구분코드", 	width: '25%'},
        {key: "column8",	label: "언어코드",  	width: '25%'},
        {key: "column9",	label: "디렉토리코드",	width: '25%'},*/
    ]
});

/*regGrid2.setConfig({
	target: $('[data-ax5grid="regGrid2"]'),
	sortable: true, 
	multiSort: true,
	showRowSelector: true,
	showLineNumber: true,
	header: {
		align: "center",
	},
	body: {
		onClick: function () {
			this.self.select(this.dindex);
			var selItem = regGrid2.list[param.dindex];
		},
		onDBLClick: function () {
			if (this.dindex < 0) return;
			btnConfirm_Click(this.dindex);
		},
		trStyleClass: function () {},
		onDataChanged: function(){
			this.self.repaint();
		}
	},
	columns: [
		{key: "col0", 	label: "업무코드",  	width: '14%'},
		{key: "col1",	label: "사번",  		width: '8%'},
		{key: "col2",	label: "프로그램명",  	width: '17%'},
		{key: "col3",	label: "프로그램설명", 	width: '17%'},
		{key: "col4",	label: "디렉토리",  	width: '16%'},
		{key: "col5",	label: "자원종류명칭", 	width: '14%'},
		{key: "col6",	label: "언어명칭",  	width: '14%'},
		{key: "col7",	label: "자원구분코드", 	width: '25%'},
		{key: "col8",	label: "언어코드",  	width: '25%'},
		{key: "col9",	label: "디렉토리코드",	width: '25%'},
		{key: "col10",	label: "BaseDir",	width: '25%'},
		{key: "col11",	label: "자원명",		width: '25%'},
	]
});*/
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$(document).ready(function() {
	screenInit();
	
	// 엑셀저장
	$('#btnExcel').bind('click', function() {
		regGrid.exportExcel("자동신규.xls");
	});
	// 조회
	$('#btnQry').bind('click', function() {
		search();
	});
	// 등록
	$('#btnConfirm').bind('click', function() {
		insert();
	});
	// 취소
	$('#btnClose').bind('click', function() {
		popClose();
	});
	
	$('#chkFind').bind('click',function(){
		changeChkFind(); 
	});
});

function screenInit() {
	$('#txtSysMsg').val(window.parent.selSysData);
	if (sysinfo.substr(18,1) == '0') {
		$('#divjob').css('display','none');
		$('#divprog').css('display','none');
	} else {
		$('#divjob').css('display','inline-block');
		$('#divprog').css('display','inline-block');
		$('[data-ax5select="cboJob"]').ax5select({
			options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
		});
		if (jobCd != null && jobCd != '' && jobCd != undefined) {
			$("#cboJob").ax5select('setValue',jobCd,true);
		}
	}
	columns = regGrid.config.columns;
	/*columns[7].hidden = true;
	columns[8].hidden = true;
	columns[9].hidden = true;*/
	regGrid.config.columns = columns;
	regGrid.setConfig();
}

function search() {
	
	var selJobCd = 'XXX';
	var progName = '';
	
	if (sysinfo.substr(18,1) == "1") {
		if (getSelectedIndex('cboJob')<1) {
			dialog.alert('업무를 선택하여 주시기 바랍니다.');
			return;
		} else {
			selJobCd = getSelectedVal('cboJob').cm_jobcd;
			progName = $('#txtProg').val().trim();
		}
	}
	var data = new Object();
	data = {
		userid : userId,
		syscod : sysCd,
		jobcd  : selJobCd,
	    progname : progName,
		requestType : 'Cmd0100_search2'
	}
	$('[data-ax5grid="regGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successSearch);
}

function successSearch(data) {
	$(".loding-div").remove();
	
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	console.log(data);
	regGrid2Data = data;
	orgRegGridData = data;
	
	if(regGrid2Data == null) {
		dialog.alert("1.신규대상목록 추출을 위한 작업에 실패하였습니다.\n[작성된 파일 없음]",function(){});
	} else {
		if($("#chkFind").is(":checked")) {
			changeChkFind();
		} else regGrid.setData(regGrid2Data);
	}
}

function search2() {
	var data = new Object();
	data = {
		userid : userId,
		syscod : sysCd,
		requestType : 'Cmd0100_search'
	}
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successSearch2);
}

function successSearch2(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	regGridData = data;
	
	if(regGridData == null) {
		dialog.alert("2.신규대상목록 추출을 위한 작업에 실패하였습니다.\n[작성된 파일 없음]",function(){});
	} 
}
function changeChkFind() {
	regGrid2Data = orgRegGridData;
	
	var findProg = $('#txtProg').val().trim();
	var txtProg = '';
	if($("#chkFind").is(":checked") && findProg.length>0) {	
		regGrid2Data = regGrid2Data.filter(function(data) {
			txtProg = data.column2;
			if (txtProg.indexOf(findProg)<0) return false;
			else return true;
		});
	}
	regGrid.setData(regGrid2Data);
}
function insert() {
	var tmpObj = new Object();
	var tmpArr = [];
	var tmpObj2 = new Object();
	var tmpArr2 = [];
	var k = 0;
	var z = 0;
	var a = 0;
	
	var selItems = regGrid.getList('selected');
	if(selItems.length == 0) {
		dialog.alert("등록 대상을 선택하십시오.");
		return;
	}
	
	for(i=0; i<selItems.length; i++) {
		tmpObj = new Object();
		tmpObj.UserId	= userId;					//유저아이디
		tmpObj.syscd 	= sysCd;					//시스템코드
		tmpObj.jobcd 	= selItems[i].column0;		//업무코드
		tmpObj.RsrcName = selItems[i].column2;		//프로그램명
		tmpObj.Story 	= selItems[i].column3;		//프로그램설명
		tmpObj.DirPath 	= selItems[i].column4;		//디렉토리
		tmpObj.ExtName 	= selItems[i].column6;		//언어명칭
		tmpObj.Rsrccd 	= selItems[i].column7;		//자원구분코드
		tmpObj.LangCd 	= selItems[i].column8;		//언어코트
		tmpObj.Dsncd 	= selItems[i].column9;		//디렉토리코드
		tmpArr.push(tmpObj);
		tmpObj = null;								
	}
	
	/*for(i=0; i<regGrid2Data.length; i++) {
		tmpObj2 = new Object();
		tmpObj2.UserId	 = userId;					//유저아이디
		tmpObj2.syscd 	 = sysCd;					//시스템코드
		tmpObj2.jobcd 	 = regGrid2Data[i].col0;	//업무코드
		tmpObj2.RsrcName = regGrid2Data[i].col2;	//프로그램명
		tmpObj2.Story 	 = regGrid2Data[i].col3;	//프로그램설명
		tmpObj2.DirPath  = regGrid2Data[i].col4;	//디렉토리
		tmpObj2.ExtName  = regGrid2Data[i].col6;	//언어명칭
		tmpObj2.Rsrccd 	 = regGrid2Data[i].col7;	//자원구분코드
		tmpObj2.LangCd 	 = regGrid2Data[i].col8;	//언어코트
		tmpObj2.Dsncd 	 = regGrid2Data[i].col9;	//디렉토리코드
		tmpObj2.Basedir  = regGrid2Data[i].col10;	//디렉토리코드
		tmpObj2.Jawon 	 = regGrid2Data[i].col11;	//디렉토리코드
		tmpArr2.push(tmpObj2);
		tmpObj2 = null;			
	}*/
	var data = new Object();
	data = {
		getCmd0100List : tmpArr,
		requestType : 'cmd0100_Insert'
	}
	
	console.log('[NewAutoRegistModal.js] cmd0100_Insert ==> ', data);
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successInsert);
	tmpArr = null;
	tmpArr2 = null;
}

function successInsert(data) {
	var tmpObj = new Object();
	var tmpArr = new Array();
	var tmpObj2 = new Object();
	var tmpArr2 = new Array();
	var k = 0;
	var z = 0;
	var a = 0;
	
	for(i=0; i<regGrid2Data.length; i++) {
		tmpObj2 = new Object();
		tmpObj2.UserId	 = userId;					//유저아이디
		tmpObj2.syscd 	 = sysCd;					//시스템코드
		tmpObj2.jobcd 	 = regGrid2Data[i].col0;	//업무코드
		tmpObj2.RsrcName = regGrid2Data[i].col2;	//프로그램명
		tmpObj2.Story 	 = regGrid2Data[i].col3;	//프로그램설명
		tmpObj2.DirPath  = regGrid2Data[i].col4;	//디렉토리
		tmpObj2.ExtName  = regGrid2Data[i].col6;	//언어명칭
		tmpObj2.Rsrccd 	 = regGrid2Data[i].col7;	//자원구분코드
		tmpObj2.LangCd 	 = regGrid2Data[i].col8;	//언어코트
		tmpObj2.Dsncd 	 = regGrid2Data[i].col9;	//디렉토리코드
		tmpObj2.Basedir  = regGrid2Data[i].col10;	//베이스디렉토리코드
		tmpObj2.Jawon 	 = regGrid2Data[i].col11;	//자원명
		
		tmpArr2.push(tmpObj2);
		
		tmpObj2 = null;			
	}
	var data = new Object();
	data = {
		getCmd0100List  : tmpArr,
		getCmd0100List2 : tmpArr2,
		requestType : 'cmd0100_Insert2'
	}
	
	console.log('[NewAutoRegistModal.js] cmd0100_Insert2 ==> ', data);
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json', successInsert2);

	tmpArr2 = null;
}

function successInsert2(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	dialog.alert('등록 완료');
}

// 모달 닫기
function popClose() {
	window.parent.autoModal.close();
}