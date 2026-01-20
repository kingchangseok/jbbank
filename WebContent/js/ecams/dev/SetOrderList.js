var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;
var manYn 	 		= window.top.manYn;
var rgtList			= window.top.rgtList;
var reqCd			= window.top.reqCd;
var attPath			= window.top.attPath;

var picker 			= new ax5.ui.picker();
var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var userGrid		= new ax5.ui.grid();
var thirdUserGrid	= new ax5.ui.grid();
var fileGrid		= new ax5.ui.grid();

var firstGridData		= [];
var secondGridData		= [];
var userGridData		= [];
var thirdUserGridData	= [];
var fileGridData		= [];
var cboGbnData			= [];
var cboStaData			= [];
var cboStaData2			= [];
var cboJobData			= [];
var cboThirdGridData	= [{cm_micode:'1', cm_codename:'업무관련자'},{cm_micode:'0', cm_codename:'전임자'}];

var strReqID 	= '';
var strQ1 		= 'N';	//QA여부
var wksropen		= '';

/* 업무지시서 미처리건 리스트  modal */
var devOrderListModal	= new ax5.ui.modal();
var statusListData   	= []; //to modal

/* 사용자검색 modal */
var devReqUserSelectModal = new ax5.ui.modal();
var devReqUserSelectGbn	  = '';
var devReqUserSelectData  = []; //from modal
var devReqUserSelectFlag  = false; //from modal

/* 파일첨부 변수 */
var fileUploadModal 	= new ax5.ui.modal();
var fileIndex			= 0;
var TotalFileSize 		= 0;

/* 파일업로드 변수 (ComFileUpload에서 사용) */
var fileGbn 			= 'U';
var dirGbn 				= '21';
var subDocPath 			= '';
var upFiles 			= [];
var	popCloseFlag 		= false;
var completeReadyFunc	= false;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            firstGrid_Click(this.item);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	},
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'CC_REQID', 		label: '요청번호',  	width: '10%',	align: 'left'},
        {key: 'CC_ENDPLAN', 	label: '처리기간', 	width: '6%',	align: 'center'},
        {key: 'editorName', 	label: '발행자', 		width: '6%',	align: 'center'},
        {key: 'CM_DEPTNAME', 	label: '부서',		width: '8%',	align: 'left'},
        {key: 'CC_DOCSUBJ', 	label: '문서제목',   	width: '15%',	align: 'left'},
        {key: 'CC_DETAILJOBN', 	label: '업무상세명',  	width: '15%',	align: 'left'},
        {key: 'CC_DETAILSAYU', 	label: '추가업무내용',	width: '36%',	align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            secondGrid_Click(this.item);
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	},
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'CC_REQID', 		label: '요청번호',  	width: '10%',	align: 'left'},
        {key: 'STARTDT', 		label: '요청등록일', 	width: '8%',	align: 'center'},
        {key: 'CC_ORDERID', 	label: '발행번호', 	width: '8%',	align: 'left'},
        {key: 'CC_DOCNUM', 		label: '발행근거',		width: '8%',	align: 'left'},
        {key: 'CC_ENDPLAN', 	label: '처리기한',   	width: '15%',	align: 'center'},
        {key: 'CC_REQSUB', 		label: '요청제목',  	width: '15%',	align: 'left'}
    ]
});

userGrid.setConfig({
    target: $('[data-ax5grid="userGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    page: false,
    header: {
        align: 'center'
    },
    body: {
    	onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        	confirmDialog.confirm({
    			title: '삭제 확인',
    			msg: '개발계획작성 후 단위테스트진행 중 입니다. 삭제를 계속 진행할까요?',
    		}, function(){
    			if(this.key === 'ok') {
    				userGrid_dblClick(this.item);
    			}
    		});
        },
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'cm_deptname', 	label: '부서',  	width: '50%',	align: 'left'},
        {key: 'cm_username', 	label: '담당자', 	width: '50%',	align: 'left'}
    ]
});

thirdUserGrid.setConfig({
    target: $('[data-ax5grid="thirdUserGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    page: false,
    header: {
        align: 'center'
    },
    body: {
    	onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        	confirmDialog.confirm({
    			title: '삭제 확인',
    			msg: '담당자를 삭제하시겠습니까?',
    		}, function(){
    			if(this.key === 'ok') {
    				thirdUserGrid_dblClick(this.item);
    			}
    		});
        },
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'cm_deptname', 	label: '부서',  		width: '33%',	align: 'left'},
        {key: 'cm_username', 	label: '담당자', 		width: '33%',	align: 'left'},
        {key: 'CC_CHECK', 		label: '전임/업무',	width: '33%',	align: 'left',
        	formatter: function(){
    			var selStr = '<div class="select width-100">';
    			selStr += "<select onChange='cboThirdGridChange(this)' class='selBox width-100'>"
    			for(var i=0; i<cboThirdGridData.length; i++) {
    				var selected = "";
    				// 그리드 값이랑 같으면 selected 처리
    				if (this.item.CC_CHECK != null && this.item.CC_CHECK != '') {
    					if(cboThirdGridData[i].cm_micode == this.item.CC_CHECK){
    						selected = "selected='selected'";
    					}
    				}
    				selStr += "<option value='"+cboThirdGridData[i].cm_micode+"' "+ selected +">"+cboThirdGridData[i].cm_codename+"</option>";
    			}
    			selStr += "</select>";
    			selStr +='</div>';
    			return selStr;
        	}
        }
    ]
});

function cboThirdGridChange(val) {
	var selVal = $(val);
	var selIndex = Number($(val).parent().parent().parent().attr('data-ax5grid-data-index'));
	selItem = thirdUserGridData[selIndex];	// 그리드에서 가져옴
	selVal 	= cboThirdGridData[Number(selVal[0].selectedIndex)]; // 선택된 콤보박스정보
	if(selItem == null || selItem.length == 0) {
		return;
	}
	selItem.CC_CHECK = selVal.cm_micode;
	thirdUserGrid.list[selIndex].CC_CHECK = selVal.cm_micode;	
}

fileGrid.setConfig({
    target: $('[data-ax5grid="fileGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: false,
    showLineNumber: true,
    lineNumberColumnWidth: 40,
    page: false,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
         	if (this.dindex < 0) return;
         	
         	if(this.item.cc_savefile != null && this.item.cc_savefile != undefined && this.item.cc_savefile != '') {
         		fileDown(attPath+'/'+this.item.cc_savefile, this.item.cc_attfile);
         	}
		},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "파일삭제"}
        ],
        popupFilter: function (item, param) {
        	fileGrid.clearSelect();
        	fileGrid.select(Number(param.dindex));

	       	var selIn = fileGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;  	
				 
	        return true;
        },
        onClick: function (item, param) {
        	confirmDialog.confirm({
    			title: '삭제확인',
    			msg: '첨부파일을 삭제하시겠습니까?',
    		}, function(){
    			if(this.key === 'ok') {
    				fileDelete();
    			}
    		});
        	
        	fileGrid.contextMenu.close();
		}
	},
    columns: [
        {key: 'cc_attfile', 	label: '파일명',  	width: '80%',	align: 'left'},
        {key: 'cm_username', 	label: '첨부인', 		width: '20%',	align: 'left'}
    ]
});

$('[data-ax5select="cboGbn"]').ax5select({
    options: []
});

$('[data-ax5select="cboSta"]').ax5select({
	options: []
});

$('[data-ax5select="cboJob"]').ax5select({
	options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$('#datStD').val(getDate('MON',-1));
$('#datEdD').val(getDate('DATE',0));

$('#datePrcDate').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic3', 'top'));

$(document).ready(function(){
	if (rgtList != null && rgtList != undefined && rgtList != '') {	
		var rgtArray = rgtList.split(',');
		for (var i=0; i<rgtArray.length; i++) {
			if (rgtArray[i] == 'Q1') {
				strQ1 = 'Y';
			}
		}
	}
	$('#chkReqDate2').wCheck('check',true);
	
	$('#datStD').prop("disabled", true); 
	$('#datEdD').prop("disabled", true);
	$('#datStD2').prop("disabled", true); 
	$('#datEdD2').prop("disabled", true);
	disableCal(true, 'datStD');
	disableCal(true, 'datEdD');
	disableCal(true, 'datStD2');
	disableCal(true, 'datEdD2');
	$('#btnComplete').prop("disabled", true);
	$('#btnDel').prop("disabled", true);
	
	$('#txtDetail').keyup(function() {
		$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	});
	
	$('#chkReqDate').bind('click', function() {
		var checkSw = $('#chkReqDate').is(':checked'); 
			
		$('#datStD').prop("disabled", !checkSw);
		$('#datEdD').prop("disabled", !checkSw);
		disableCal(!checkSw, 'datStD');
		disableCal(!checkSw, 'datEdD');
	});
	
	$('#chkReqDate2').bind('click', function() {
		var checkSw = $('#chkReqDate2').is(':checked'); 
			
		$('#datStD2').prop("disabled", !checkSw);
		$('#datEdD2').prop("disabled", !checkSw);
		disableCal(!checkSw, 'datStD2');
		disableCal(!checkSw, 'datEdD2');
	});
	
	if(strQ1 == 'Y') {
		$('#chkReqDate2').wCheck('check',true);
		chkReqDate2Click();
		$('#datStD2').val(getDate('MON',-6));
		$('#datEdD2').val(getDate('DATE',0));
		picker.bind(defaultPickerInfoLarge('basic2', 'top'));
	}else {
		$('#chkReqDate2').wCheck('check',true);
		chkReqDate2Click();
		$('#datStD2').val(getDate('MON',-12));
		$('#datEdD2').val(getDate('DATE',0));
		picker.bind(defaultPickerInfoLarge('basic2', 'top'));
	}
	
	$('#txtOrderNum1').val(getDate('DATE',0).substr(0,4));
	
	setCbo();
	getCodeInfo();
	getSrOpen();
	
	//요청등록일 (업무지시서목록)
	$('#chkReqDate2').bind('click', function() {
		chkReqDate2Click();
	});
	
	//조회 (개발요청목록)
	$('#btnQry').bind('click', function() {
		getREQList();
	});
	
	//조최 (업무지시서목록)
	$('#btnQry2').bind('click', function() {
		btnQry2Click();
	});
	
	//업무지시종료
	$('#btnComplete').bind('click', function() {
		btnCompleteClick();
	});
	
	//삭제
	$('#btnDel').bind('click', function() {
		btnDelClick();
	});

	//발행
	$('#btnAdd').bind('click', function() {
		btnAddClick();
	});
	
	//신규등록
	$('#chkNew').bind('click', function() {
		chkNewClick();
	});
	
	//자체발행
	$('#chkSelf').bind('click', function() {
		chkSelfClick();
	});
	
	//파일첨부
	$('#btnFileAdd').bind('click', function() {
		btnFileAddClick();
	});
	
	//파일삭제
	$('#btnFileDel').bind('click', function() {
		btnFileDelClick();
	});
	
	//담당자추가
	$('#btnAddDevUser').bind('click', function() {
		if(manYn) devReqUserSelectGbn = '0091';
		else devReqUserSelectGbn = 'Other';
		openDevReqUserSelectModal('U');
	});
	
	//3자추가
	$('#btnAddThirdUser').bind('click', function() {
		devReqUserSelectGbn = '0091';
		openDevReqUserSelectModal('3');
	});
	
});

function openDevReqUserSelectModal(gbn) {
	if(gbn == 'U') devReqUserSelectArr = clone(userGridData); //담당자추가
	else devReqUserSelectArr = clone(thirdUserGridData); //3자추가
	
	setTimeout(function() {
		devReqUserSelectModal.open({
			width: 500,
			height: 650,
			iframe: {
				method: "get",
				url: "../modal/dev/DevReqUserSelectModal.jsp"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}else if (this.state === "close") {
					console.log('[DevReqUserSelectModal close] ==>', devReqUserSelectFlag, devReqUserSelectData);
					
					if(gbn == 'U') { //담당자추가
						if(devReqUserSelectFlag) {
							userGridData = clone(devReqUserSelectData);
						}
						userGrid.setData(userGridData);
					}else { //3자추가
						if(devReqUserSelectFlag) {
							devReqUserSelectData.forEach(function(item) {
								item.CC_CHECK = '1'; //디폴트:업무관련자(1)
							});
							thirdUserGridData = clone(devReqUserSelectData);
						}
						thirdUserGrid.setData(thirdUserGridData);
					}
					mask.close();
				}
			}
		});
	}, 200);
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('JOBTEAM','SEL','N','3','')
	]);
	
	cboJobData = codeInfos.JOBTEAM;
	$('[data-ax5select="cboJob"]').ax5select({
		options: injectCboDataToArr(cboJobData, 'cm_micode' , 'cm_codename')
	});
}

function getSrOpen() {
	var data = new Object();
	data = {
		SelMsg : 'SEL',
		closeYn : 'N',
		requestType	: 'getSrOpen'
	}
	ajaxAsync('/webPage/common/CodeInfoServlet', data, 'json',successGetSrOpen);
}


function successGetSrOpen(data) {
	console.log("successGetSrOpen > ", data);
	
	wksropen = data;
	
	// SR 오픈시 버튼 비활성화
	var today = getDate('DATE',0);
	if(today >= wksropen) {
		$('#btnAdd').prop("disabled", true);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
	}
}

function setCbo() {
	cboGbnData = [
		{cm_codename : "전체목록", cm_micode : "00"},
		{cm_codename : "개인목록", cm_micode : "01"}
	]
	$('[data-ax5select="cboGbn"]').ax5select({
		options: injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboGbn"]').ax5select("setValue", '01', true);
	
	cboStaData = [
		{cm_codename : "진행중목록", 	cm_micode : "00"},
		{cm_codename : "종료목록", 	cm_micode : "01"},
		{cm_codename : "전체목록", 	cm_micode : "02"}
	]
	$('[data-ax5select="cboSta"]').ax5select({
		options: injectCboDataToArr(cboStaData, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboSta"]').ax5select("setValue", '00', true);
	
	cboStaData2 = [
		{cm_codename : "진행중목록", 	cm_micode : "00"},
		{cm_codename : "종료목록", 	cm_micode : "01"},
		{cm_codename : "전체목록", 	cm_micode : "02"}
	]
	$('[data-ax5select="cboSta2"]').ax5select({
		options: injectCboDataToArr(cboStaData2, 'cm_micode' , 'cm_codename')
	});
	$('[data-ax5select="cboSta2"]').ax5select("setValue", '00', true);
	
	getREQList();
}

function getREQList() {
	var stDate = '';
	var edDate = '';
	
	if($('#chkReqDate').is(':checked')) {
		stDate = $('#datStD').val();
		edDate = $('#datEdD').val();
	}
	
	var data = new Object();
	data = {
		status : getSelectedVal('cboSta').cm_micode,
		UserID : userId,
		cboGbn : getSelectedVal('cboGbn').cm_micode,
		datStD : stDate,
		datEdD : edDate,
		DocType : 'etc',
		requestType	: 'getREQList'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	console.log('[getREQList] ==>',data);
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetREQList);
}

function successGetREQList(data) {
	$(".loding-div").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
	
	if(firstGridData.length == 0) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	var stDate = '';
	var edDate = '';
	if($('#chkReqDate2').is(':checked')) {
		stDate = $('#datStD2').val();
		edDate = $('#datEdD2').val();
	}
	getORDERListSearch('0', stDate, edDate, userId);
}

function getORDERListSearch(sta, stDt, edDt, userId) {
	var data = new Object();
	data = {
		Status : sta,
		Stdt : stDt,
		Eddt : edDt,
		UserId : userId,
		requestType	: 'getORDERListSearch'
	}
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div2);
	$(".loding-div2").show();
	console.log('[getORDERListSearch] ==>',data);
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetORDERListSearch);
}

function successGetORDERListSearch(data) {
	$(".loding-div2").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	secondGridData = data;
	secondGrid.setData(secondGridData);
	
	if(secondGridData.length == 0) {
		dialog.alert('검색결과가 없습니다.');
	}
}

function chkReqDate2Click() {
	if($('#chkReqDate2').is(':checked')) {
		$('#datStD2').prop("disabled", false); 
		$('#datEdD2').prop("disabled", false);
		disableCal(false, 'datStD2');
		disableCal(false, 'datEdD2');
	}else {
		$('#datStD2').prop("disabled", true); 
		$('#datEdD2').prop("disabled", true);
		disableCal(true, 'datStD2');
		disableCal(true, 'datEdD2');
	}
}

function btnQry2Click() {
	var i = getSelectedIndex('cboSta2');
	var sta = '8'; //전체목록
	
	if(i==0) sta = '0';
	else if(i==1) sta = '9';
	
	var stDate = '';
	var edDate = '';
	
	if($('#chkReqDate2').is(':checked')) {
		stDate = $('#datStD2').val();
		edDate = $('#datEdD2').val();
		if(replaceAllString(stDate,'/','') > replaceAllString(edDate,'/','')) {
			dialog.alert('올바른 기간을 선택해주십시오.');
			return;
		}
	}
	
	getORDERListSearch(sta, stDate, edDate, userId);
}

function firstGrid_Click(selItem) {
	$('#chkSelf').wCheck('check',true);
	chkSelfClick();
	
	$('#txtTitl0').show();
	$('#txtTitl1').show();
	
	userGridData = [];
	userGrid.setData([]);
	
	thirdUserGridData = [];
	thirdUserGrid.setData([]);
	
	fileGridData = [];
	fileGrid.setData([]);
	
	$('#chkSelf').wCheck('check',false); //자체발행
	$('#txtTitl0').val('');
	$('#txtTitl0').val(selItem.CC_REQID); //요청번호
	$('#txtTitl1').val(selItem.CM_DEPTNAME);
	$('#txtTitle').val(selItem.CC_DOCSUBJ); //요청제목
	$("#txtDetail").val(selItem.CC_DETAILSAYU); //상세요청내용
	$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	$("#txtCause").val(selItem.CC_DOCNUM2); //발행근거(개발요청에서 문서번호에 해당)
	
	//개발요청 그리드 선택시 요청번호 남기고 신규등록처럼 되게 하기
	$('#btnAdd').text('발행');
	if($('#chkNew').is(':checked')) {
		$('#divOrderNum0').show();
		$('#divJob').show();
		$('#divOrderNum1').show();
		$('#chkSelf').wCheck('check',false); //자체발행
	}

	$('#chkNew').wCheck('check',true);
	
	// SR 오픈시 버튼 비활성화
	var today = getDate('DATE',0);
	if(today >= wksropen  && selItem.CC_ACTTYPE == "01") {
		$('#btnAdd').prop("disabled", true);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
	}else{
		$('#btnAdd').prop("disabled", false);
		$('#btnComplete').prop("disabled", false);
		$('#btnDel').prop("disabled", false);
	}
	
	$('#divOrderNum2').hide();
	
	$('#datePrcDate').val(getDate('DATE',0));
	picker.bind(defaultPickerInfoLarge('basic3', 'top'));
	
	$('#divOrderNum0').show();
	$('#divJob').show();
	$('#divOrderNum1').show();
	

	fileGridData = [];
	fileGrid.setData([]);
		
	getORDERList();
}

function getORDERList() {
	var i = getSelectedIndex('cboSta2');
	var sta = '8'; //전체목록
	
	if(i==0) sta = '0';
	else if(i==1) sta = '9';
	
	var stDate = '';
	var edDate = '';
	
	if($('#chkReqDate2').is(':checked')) {
		stDate = replaceAllString($('#datStD2').val().trim(),'/','')
		edDate = replaceAllString($('#datEdD2').val().trim(),'/','')
		if(stDate > edDate) {
			dialog.alert('올바른 기간을 선택해주십시오.');
			return;
		}
	}
	
	var data = new Object();
	data = {
		Userid : userId,
		Status : sta,
		ReqID : $('#txtTitl0').val(),
		SubID : '',
		Stdt : stDate,
		Eddt : edDate,
		Selfsw : '',
		requestType	: 'getORDERList'
	}
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	console.log('[getORDERListSearch] ==>',data);
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetORDERList);
}

function successGetORDERList(data) {
	$(".loding-div").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	secondGridData = data;
	secondGrid.setData(secondGridData);
}

function secondGrid_Click(selItem) {
	//선택된 아이템 status 값이 9, 3 이면 완료버튼, 수정버튼 비활성화
	if(selItem.CC_STATUS == '9' || selItem.CC_STATUS == '3') {
		$('#btnAdd').prop("disabled", true);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
	}else {
		$('#btnAdd').prop("disabled", false);
		$('#btnComplete').prop("disabled", false);
		$('#btnDel').prop("disabled", false);
	}
	
	if(adminYN) {
		$('#btnAdd').prop("disabled", false);
		$('#txtTitl0').prop("disabled", false);
	}
	
	$('#chkNew').wCheck('check',false);
	$('#chkSelf').wCheck('check',false);
	
	// SR 오픈시 버튼 비활성화
	var today = getDate('DATE',0);
	if(today >= wksropen  && selItem.CC_ACTTYPE == "01") {
		$('#btnAdd').prop("disabled", true);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
		$('#btnAdd').prop("disabled", true);
		$('#txtTitl0').prop("disabled", true);
		$('#chkNew').prop("disabled", true);
		$('#chkSelf').prop("disabled", true);
	}else{
		$('#btnAdd').prop("disabled", false);
		$('#btnComplete').prop("disabled", false);
		$('#btnDel').prop("disabled", false);
		$('#btnAdd').prop("disabled", false);
		$('#txtTitl0').prop("disabled", false);
		$('#chkNew').prop("disabled", false);
		$('#chkSelf').prop("disabled", false);
	}
	
	userGridData = [];
	userGrid.setData([]);
	
	thirdUserGridData = [];
	thirdUserGrid.setData([]);
	
	// 20231130 파일리스트 추가 수정
	fileGridData = [];
	fileGrid.setData([]);
	if(selItem.fileinfos != '' && selItem.fileinfos != null && selItem.fileinfos != undefined) {
		tmpArr = selItem.fileinfos.split('|');
		
		tmpArr2 = null;
		for(var i=0; i<tmpArr.length; i++) {
			tmpArr2 = tmpArr[i].split('^');
			tmpData = new Object();
			tmpData.cc_id = tmpArr2[0];
			tmpData.subid = tmpArr2[1];
			tmpData.cc_subreq = tmpArr2[2];
			tmpData.cc_seqno = tmpArr2[3];
			tmpData.cc_savefile = tmpArr2[4];
			tmpData.cc_attfile = tmpArr2[5];
			tmpData.cc_lastdt = tmpArr2[6];
			tmpData.cc_editor = tmpArr2[7];
			tmpData.cc_reqcd = tmpArr2[8];
			tmpData.cm_username = tmpArr2[9];
			tmpData.isCopy = 'true';
			fileGridData.push(tmpData);
			tmpData = null;
		}
		fileGrid.setData(fileGridData);
	}
	
	$('#btnAdd').text('수정');
	$('#txtTitl0').show();
	$('#txtTitl1').show();
	$('#txtTitl0').val('');
	$('#txtTitl0').val(selItem.CC_REQID); //요청번호
	$('#txtTitl1').val(selItem.CM_DEPTNAME);	
	
	$('#divOrderNum0').hide();
	$('#divJob').hide();
	$('#divOrderNum1').hide();
	$('#divOrderNum2').show();
	
	$('#txtOrderNum2').val(selItem.CC_ORDERID); //발행번호
	$('#txtTitle').val(selItem.CC_REQSUB); //요청제목
	$('#txtDetail').val(selItem.CC_DETAILSAYU); //상세요청내용
	$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	$('#txtCause').val(selItem.CC_DOCNUM); //발행근거
	$('#datePrcDate').val(selItem.CC_ENDPLAN); //처리기한
	
	var tmpArr = null;
	var tmpArr2 = null;
	var tmpData = null;
	
	//3자
	thirdUserGridData = [];
	thirdUserGrid.setData([]);
	if(selItem.thirdusers != '' && selItem.thirdusers != null && selItem.thirdusers != undefined) {
		tmpArr = selItem.thirdusers.split(',');
		
		for(var i=0; i<tmpArr.length; i++) {
			tmpArr2 = tmpArr[i].split('^');
			tmpData = new Object();
			tmpData.cm_project = tmpArr2[0];
			tmpData.cm_deptname = tmpArr2[1];
			tmpData.cm_userid = tmpArr2[2];
			tmpData.cm_username = tmpArr2[3];
			tmpData.CC_CHECK = tmpArr2[4];
			tmpData.cm_posname = tmpArr2[5];
			thirdUserGridData.push(tmpData);
			tmpData = null;
		}
		
		console.log('thirdUserGridData=',thirdUserGridData);
		thirdUserGrid.setData(thirdUserGridData);
	}
	
	//담당자
	userGridData = [];
	userGrid.setData([]);
	if(selItem.runners != '' && selItem.runners != null && selItem.runners != undefined) {
		tmpArr = selItem.runners.split(',');
		
		for(var i=0; i<tmpArr.length; i++) {
			tmpArr2 = tmpArr[i].split('^');
			tmpData = new Object();
			tmpData.cm_project = tmpArr2[0];
			tmpData.cm_deptname = tmpArr2[1];
			tmpData.cm_userid = tmpArr2[2];
			tmpData.cm_username = tmpArr2[3];
			tmpData.cm_posname = tmpArr2[4];
			userGridData.push(tmpData);
			tmpData = null;
		}
		
		console.log('userGridData=',userGridData);
		userGrid.setData(userGridData);
	}
	
}

function userGrid_dblClick(selItem) {
	userGrid.removeRow(userGrid.selectedDataIndexs[0]);
	dialog.alert('삭제완료');
	userGridData = userGrid.getList();
}

function thirdUserGrid_dblClick(selItem) {
	thirdUserGrid.removeRow(thirdUserGrid.selectedDataIndexs[0]);
	dialog.alert('삭제완료');
	thirdUserGridData = thirdUserGrid.getList();
}

function btnCompleteClick() {
	var gridSelectedIndex = secondGrid.selectedDataIndexs;
	var gridselectedItem = secondGrid.list[gridSelectedIndex];
	
	if(gridSelectedIndex < 0) return;
	
	if(!adminYN && gridselectedItem != undefined && gridselectedItem.CC_ORDERUSER != userId) {
		dialog.alert('작성자만 종료 가능합니다.');
		return;
	}
	
	var data = new Object();
	data = {
		userID : userId,
		orderID : gridselectedItem.CC_ORDERID,
		reqID : gridselectedItem.CC_REQID,
		requestType	: 'statusChk'
	}
	console.log('[statusChk] ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successStatusChk);
}

function successStatusChk(data) {
	if(data == 'Y') {
		var gridSelectedIndex = secondGrid.selectedDataIndexs;
		var gridselectedItem = secondGrid.list[gridSelectedIndex];
		
		var data = new Object();
		data = {
			userID : userId,
			orderID : gridselectedItem.CC_ORDERID,
			reqID : gridselectedItem.CC_REQID,
			requestType	: 'statusUpdt'
		}
		console.log('[statusChk] ==>', data);
		ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successStatusUpdt);
	}else {
		dialog.alert('대여/적용건이 없습니다.');
		return;
	}
}

function successStatusUpdt(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	statusListData = data;
	if(statusListData != null && statusListData != undefined) {
		if(statusListData.length == 0) {
			dialog.alert('종료 되었습니다.');
			return;
		}else {
			setTimeout(function() {
				devOrderListModal.open({
					width: 400,
					height: 650,
					iframe: {
						method: "get",
						url: "../modal/dev/DevOrderListModal.jsp"
					},
					onStateChanged: function () {
						if (this.state === "open") {
							mask.open();
						}else if (this.state === "close") {
							mask.close();
						}
					}
				});
			}, 200);
		}
	}
}

function btnDelClick() {
	var gridSelectedIndex = secondGrid.selectedDataIndexs;
	var gridselectedItem = secondGrid.list[gridSelectedIndex];
	
	if(!adminYN && gridselectedItem != undefined && gridselectedItem.CC_ORDERUSER != userId) {
		dialog.alert('작성자만 삭제 가능합니다.');
		return;
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: '업무지시서를 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			
			var delREQ = new Object();
			delREQ.delORDERinfo = gridselectedItem.CC_ORDERID;
			delREQ.delStrUserID = userId;
			
			var data = new Object();
			data = {
				delREQ : delREQ,
				requestType	: 'delREQinfo'
			}
			console.log('[delREQinfo] ==>', data);
			ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successDelREQinfo);
		}
	});
}

function successDelREQinfo(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	if(data == 'Y') {
		dialog.alert('폐기를 완료하였습니다.');
		//getORDERListSearch('8', '', '', userId);
		btnQry2Click();
		$('#chkNew').wCheck('check',true);
		chkNewClick();
	}else {
		dialog.alert('업무지시서 미처리건이 있습니다.');
		return;
	}
}

function btnAddClick() {
	var gridSelectedIndex = secondGrid.selectedDataIndexs;
	var gridselectedItem = secondGrid.list[gridSelectedIndex];
	
	if(!adminYN && gridselectedItem != undefined && gridselectedItem.CC_ORDERUSER != userId) {
		if($('#btnAdd').text() == '수정') {
			dialog.alert('작성자만 수정이 가능합니다.');
			return;
		}
	}
	
	if($('#txtDetail').val().length < 200) {
		dialog.alert("상세요청내용은 200자 이상입력하셔야 합니다.");
		return;
	}
	
	if(fileGridData.length == 0) {
		confirmDialog.confirm({
			title: '파일첨부 확인',
			msg: '파일을 첨부하지 않고 등록하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				btnAddClick_sub();
			}
		});
	}else {
		btnAddClick_sub();
	}
}

function btnAddClick_sub() {
	if(getSelectedIndex('cboJob') < 1 && $('#chkNew').is(':checked')) {
		dialog.alert('발행번호를 선택후 발행하십시오.');
		return;
	}
	
	if($('#txtTitle').val().length == 0) {
		dialog.alert("요청제목을 입력 후 등록하십시오.");
		return;
	}
	
	if($('#txtCause').val().length == 0) {
		dialog.alert("발행근거를 입력 후 등록하십시오.");
		return;
	}
	
	if($('#txtDetail').val().length == 0) {
		dialog.alert("상세요청내용을 입력 후 등록하십시오.");
		return;
	}
	
	if(userGridData.length == 0) {
		dialog.alert("담당자를 선택한 후 발행하십시오.");
		return;
	}
	
	if(thirdUserGridData.length == 0) {
		dialog.alert("3자를 지정후 발행하십시오.");
		return;
	}
	
	var tmpObj = new Object();
	if(adminYN) {
		tmpObj.CC_REQID = $('#txtTitl0').val();
	}else {
		if(!$('#chkSelf').is(':checked')) {
			tmpObj.CC_REQID = $('#txtTitl0').val();
		}
	}
	tmpObj.CC_REQSUB = $('#txtTitle').val();
	tmpObj.CC_DETAILSAYU = $('#txtDetail').val();				
	tmpObj.CC_DOCNUM = $('#txtCause').val();
	tmpObj.CC_ENDPLAN = $('#datePrcDate').val();
	tmpObj.CC_ORDERUSER = userId;
	
	var reqType = '';
	var newUserGridData = [];
    if( $('#chkNew').is(':checked') ) {
		reqType = 'setORDERInfo';
		//newUserGridData = clone(userGridData);
		tmpObj.CC_ORDERID = $('#txtOrderNum0').val()+"-"+getSelectedVal('cboJob').cm_codename+"-"+$('#txtOrderNum1').val();
	}else {
		reqType = 'setORDERupdt';
		
		for(var i = 0; i < userGridData.length; i++) {
			if(userGridData[i].newUser == 'true') newUserGridData.push(userGridData[i]);
		}
		
		/*newUserGridData = userGridData.filter(function(item) {
			if(item.newUser == 'true') return true;
			else return false;
		});*/
		
		var gridSelectedIndex = secondGrid.selectedDataIndexs;
		var gridselectedItem = secondGrid.list[gridSelectedIndex];
		tmpObj.CC_ORDERID = gridselectedItem.CC_ORDERID;
	}
    
    var data = new Object();
	data = {
		etcData : tmpObj,
		RunnerList : userGridData,
		tmpRunnerList : reqType == 'setORDERInfo' ? userGridData : newUserGridData,
		ThirdList : thirdUserGridData,
		requestType	: reqType
	}
	console.log('['+reqType+'] ==>', data);
	
	if(reqType == 'setORDERInfo') ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successSetORDERInfo);
	else ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successSetORDERupdt);
}

function successSetORDERInfo(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	strReqID = data;
	if(strReqID != null && strReqID != '' && strReqID != undefined && strReqID.length > 0) {
		dialog.alert('발행되었습니다.');
		
		btnQry2Click();
		
		if(fileGridData.length > 0) {
			//파일업로드
			startFileupload();
		}else {
			$('#chkNew').wCheck('check',true);
			chkNewClick();
		}
	}else {
		dialog.alert('발행이 실패되었습니다.');
		return;
	}
}

function successSetORDERupdt(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	strReqID = data;
	if(strReqID != null && strReqID != '' && strReqID != undefined && strReqID.length > 0) {
		dialog.alert('수정되었습니다.');
		
		getORDERListSearch('8', '', '', userId);
		
		if(fileGridData.length > 0) {
			//파일업로드
			startFileupload();
		}else {
			$('#chkNew').wCheck('check',true);
			chkNewClick();
			btnQry2Click();
		}
	}else {
		dialog.alert('수정이 실패되었습니다.');
		return;
	}
}

function chkNewClick() {
	var checkSw = $('#chkNew').is(':checked');
	
	if(checkSw) {
		$('#btnAdd').text('발행');
		$('#txtTitl0').show();
		$('#txtTitl1').show();
		
		if(cboJobData.length > 0) $('[data-ax5select="cboJob"]').ax5select("setValue", cboJobData[0].value, true);
		
		$('#txtTitl0').val('');
		$('#txtTitle').val('');
		$('#txtDetail').val('');
		$('#txtCause').val('');
		
		$('#datePrcDate').val(getDate('DATE',0));
		picker.bind(defaultPickerInfoLarge('basic3', 'top'));
		
		$('#divOrderNum0').show();
		$('#divJob').show();
		$('#divOrderNum1').show();
		$('#divOrderNum2').hide();
		
		userGridData = [];
		userGrid.setData([]);
		
		thirdUserGridData = [];
		thirdUserGrid.setData([]);
		
		fileGridData = [];
		fileGrid.setData([]);
	}
}

function chkSelfClick() {
	var checkSw = $('#chkSelf').is(':checked');
	
	if(checkSw) {
		$('#btnAdd').text('발행');
		$('#btnAdd').prop("disabled", false);
		$('#txtTitl0').show();
		$('#txtTitl1').show();
		$('#txtTitl0').val('');
		$('#txtTitl1').val('');
		$('#txtTitle').val('');
		$('#txtDetail').val('');
		$('#txtCause').val('자체발행');
		
		userGridData = [];
		userGrid.setData([]);
		
		thirdUserGridData = [];
		thirdUserGrid.setData([]);
		
		fileGridData = [];
		fileGrid.setData([]);
		
		$('#divOrderNum0').show();
		$('#divJob').show();
		$('#divOrderNum1').show();
		$('#divOrderNum2').hide();
	}else {
		$('#txtCause').val('');
	}
}

function btnFileAddClick() {
	if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
		fileIndex++;
		$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
	}
	$("#file"+fileIndex).click();
}

function fileChange(file) {
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	var spcChar = "{}<>?|~`!@#$%^&*+\"'\\/";
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 
		var fileCk = true;
		
		for(var i=0; i<jqueryFiles.files.length; i++){
			var sizeCount = 0;
			var size = jqueryFiles.files[i].size/1024; // Byte, KB, MB, GB
			while(size > 1024 && sizeCount < 2){
				size = size/1024;
				sizeCount ++;
			}
			size = Math.round(size*10)/10.0 + fileSizeArr[sizeCount];
			var sizeReal = jqueryFiles.files[i].size;
			var name = jqueryFiles.files[i].name
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				fileCk = false;
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				fileCk = false;
				break;
			}
			
			for(var j=0; j<fileGridData.length; j++){

				for (k=0;spcChar.length>k;k++) {
					if (name.indexOf(spcChar.substr(k,1))>=0) {
						dialog.alert("첨부파일명에 특수문자를 입력하실 수 없습니다.\n특수문자를 제외하고 첨부하여 주시기 바랍니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
					
					if (name.substring(name.length-3, name.length).toUpperCase() == 'HWX') {
						dialog.alert("HWX(hwx) 파일은 첨부 할 수 없습니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
				}
				if(!fileCk){
					break;
				}
				
				if(fileGridData[j].name == name){
					dialog.alert("파일명 : " + name +"\n 은 이미 추가 되어 있는 파일명 입니다.\n확인 후 다시 등록해 주세요");
					$("#"+file).remove();
					fileCk = false;
					break;
				}
				fileCk = true;
			}

			if(fileCk){
				var tmpObj = new Object(); // 그리드에 추가할 파일 속성
				tmpObj.name = name;
				tmpObj.size = size;
				tmpObj.sizeReal = sizeReal;
				tmpObj.newFile = true;
				tmpObj.realName = name;
				tmpObj.cc_attfile = name;
				tmpObj.file = jqueryFiles.files[i];
				
				fileGridData.push(tmpObj);
			}
			else{
				break;
			}
		}
		fileGrid.setData(fileGridData);
		if(fileCk && $('#btnAdd').text() == '수정'){
			dialog.alert("파일첨부 시 수정 버튼을 클릭해야 파일이 저장됩니다.");
		}
	}
}

function startFileupload() {
	var data = new Object();
	data = {
		ReqId : strReqID,
		ReqCd : reqCd,
		requestType	: 'maxFileSeq'
	}
	var fileseq = ajaxCallWithJson('/webPage/ecmc/Cmc0010Servlet', data, 'json');
	if(Number(fileseq) < 1) {
		dialog.alert('파일첨부 일련번호 추출에 실패하였습니다.');
		return;
	}
	
	var strSubReq = 0;
	
	fileGbn = 'U';
	dirGbn = '21';
	popCloseFlag = false;
	
	attPath = attPath + '/' + strReqID;
	//attPath = 'C:\\html5\\temp\\'; //테스트중
	subDocPath = strReqID;
	
	for(var i=0; i<fileGridData.length; i++) {
		if(fileGridData[i].newFile) {
			var tmpFile = new Object();
			
			if($('#chkNew').is(':checked')) {
				strSubReq = '01';
			}else {
				strSubReq = '00';
			}
			
			tmpFile = clone(fileGridData[i]);
			tmpFile.savefile = strReqID + '/' + reqCd + '' + strSubReq + '' + fileseq + fileGridData[i].name.substr(fileGridData[i].name.lastIndexOf('.'));
			tmpFile.saveFileName = reqCd + '' + strSubReq + '' + fileseq + fileGridData[i].name.substr(fileGridData[i].name.lastIndexOf('.'));	//서버파일명
			tmpFile.fileseq = '' + fileseq;
			tmpFile.attfile = fileGridData[i].name;		//실제파일명
			tmpFile.fullName = attPath+'/'+strReqID;	//첨부경로
			tmpFile.fullpath = attPath;					//첨부경로(pathcd=21)
			
			upFiles.push(tmpFile);
			
			++fileseq;
		}
	}
	
	//upFiles = clone(fileGridData);
	console.log('[SRRegisterTab.js] upFiles==>',upFiles);
	
	if(upFiles.length > 0) {
		setTimeout(function() {
			fileUploadModal.open({
				width: 685,
				height: 420,
				iframe: {
					method: "get",
					url: "../modal/fileupload/ComFileUpload.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						fileGridData = [];	// 20231130
						fileGrid.setData([]); // 20231130
						upFiles = [];	// 20231130
						mask.close();
					}
				}
			});
		}, 200);
	}else {
		btnQry2Click();
		$('#chkNew').wCheck('check',true);
		chkNewClick();
	}
}

// ComFileUpload 함수에서 호출
function setFile(fileList) {
	console.log('[RegistDevRequest.js] setFile ==>', fileList);
	
	fileList.forEach(function(item, Index) {
		var tmpObj = new Object();
		tmpObj.reqid = strReqID;
		tmpObj.reqcd = reqCd;
		if($('#chkNew').is(':checked')) {
			tmpObj.subreq = '01';
		}else {
			tmpObj.subreq = '00';
		}
		tmpObj.savefile = item.savefile;	//요청번호/서버파일명
		tmpObj.attfile = item.name;			//실제파일명	
		tmpObj.fileseq = item.fileseq;
		tmpObj.userid = userId;
		
		var data = {
			docFile : tmpObj,
			requestType : 'setDocFile'
		}
		console.log('[RegistDevRequest.js] setDocFile ==>', data);
		ajaxAsync('/webPage/ecmc/Cmc0010Servlet', data, 'json', successSetDocFile);
	});
}

function successSetDocFile(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	if(data != 'OK') {
		dialog.alert(data);
		return;
	}
	
	btnQry2Click();
	$('#chkNew').wCheck('check',true);
	chkNewClick();
}

function btnFileDelClick() {
	var gridSelectedIndex = fileGrid.selectedDataIndexs;
	var gridselectedItem = fileGrid.list[gridSelectedIndex];
	
	if(gridselectedItem == null || gridselectedItem == undefined || gridselectedItem.length == 0) {
		dialog.alert('선택된 첨부파일이 없습니다.');
		return;
	}

	confirmDialog.confirm({
		title: '삭제확인',
		msg: '첨부파일을 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			fileDelete();
		}
	});
}

function fileDelete() {
	var selItem = fileGrid.list[fileGrid.selectedDataIndexs[0]];
	if(selItem.newFile) {
		fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
		fileGridData = fileGrid.getList();
	}else {
		if(selItem.cc_editor != userId) {
			dialog.alert('작성자만 삭제 가능합니다.');
			return;
		}else {
			fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
			fileGridData = fileGrid.getList();
			
			var data = new Object();
			data = {
				Id : selItem.cc_id,
				ReqCd : reqCd,
				seqNo : selItem.cc_seqno,
				requestType	: 'delFile'
			}
			var ajaxReturnData = ajaxCallWithJson('/webPage/ecmc/Cmc0010Servlet', data, 'json');
			if(ajaxReturnData != null && ajaxReturnData != undefined) {
				if(typeof ajaxReturnData == 'string' && ajaxReturnData.indexOf('ERROR') > -1) {
					dialog.alert(ajaxReturnData.substr(5));
					return;
				}
			}
			
			dialog.alert('삭제완료');
			getORDERListSearch('8', '', '', userId);
			$('#chkNew').wCheck('check',true);
			chkNewClick();
			btnQry2Click();
		}
	}
}