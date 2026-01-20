var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;
var reqCd			= window.top.reqCd;
var attPath			= window.top.attPath;

var picker 			= new ax5.ui.picker();
var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var userGrid		= new ax5.ui.grid();
var fileGrid		= new ax5.ui.grid();

var firstGridData	= [];
var secondGridData	= [];
var userGridData	= [];
var tmpUserGridData = [];
var fileGridData	= [];
var cboGbnData		= [];
var cboStaData		= [];
var cboGbnData2		= [];
var cboStaData2		= [];
var cboJobData		= [];
var lstPMData		= [];

var strReqID		= '';
var wksropen		= '';

/* 관련업무리스트 modal */
var devReqListModal	= new ax5.ui.modal();
var devReqStaList   = []; //to modal

/* 사용자검색 modal */
var devReqUserSelectModal = new ax5.ui.modal();
var devReqUserSelectGbn	  = '';	//to modal
var devReqUserSelectArr	  = []; //to modal
var devReqUserSelectData  = []; //from modal
var devReqUserSelectFlag  = false; //from modal

/* 파일첨부 변수 */
var fileIndex			= 0;
var TotalFileSize 		= 0;
var uploadLen			= 0;

/* 파일업로드 변수 (ComFileUpload에서 사용) */
var fileUploadModal 	= new ax5.ui.modal();
var fileGbn 			= 'U';
var dirGbn 				= '21';
var subDocPath 			= '';
var upFiles 			= [];
var	popCloseFlag 		= false;

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
        /*{key: 'editorName', 	label: '발행자', 		width: '8%',	align: 'left'},*/
        {key: 'CM_DEPTNAME', 	label: '요청팀명',		width: '8%',	align: 'left'},
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
        {key: 'CC_ENDPLAN', 	label: '처리기간', 	width: '6%',	align: 'center'},
        {key: 'editorName', 	label: '발행자', 		width: '6%',	align: 'center'},
        {key: 'CM_DEPTNAME', 	label: '부서',		width: '8%',	align: 'left'},
        {key: 'CC_DOCSUBJ', 	label: '문서제목',   	width: '15%',	align: 'left'},
        {key: 'CC_DETAILJOBN', 	label: '업무상세명',  	width: '15%',	align: 'left'},
        {key: 'CC_DETAILSAYU', 	label: '추가업무내용',	width: '36%',	align: 'left'}
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
    			msg: '담당자를 삭제하시겠습니까?',
    		}, function(){
    			if(this.key === 'ok') {
    				userGrid_dblClick(this.item);
    			}
    		});
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
        {key: 'cm_deptname', 	label: '부서',  	width: '50%',	align: 'left'},
        {key: 'cm_username', 	label: '담당자', 	width: '50%',	align: 'left'}
    ]
});

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

$('[data-ax5select="cboGbn2"]').ax5select({
    options: []
});

$('[data-ax5select="cboSta2"]').ax5select({
	options: []
});

$('[data-ax5select="cboJob"]').ax5select({
	options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));
$('#datStD2').val(getDate('DATE',0));
$('#datEdD2').val(getDate('DATE',0));
$('#datePrcDate').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
picker.bind(defaultPickerInfoLarge('basic2', 'top'));
picker.bind(defaultPickerInfoLarge('basic3', 'bottom'));

$(document).ready(function(){
	setCbo();
	getJobCd();
	getOtherUser();
	getSrOpen();
	
	$('#txtReqNum1').val(getDate('DATE',0).substr(0,4));
	$('#datStD').prop("disabled", true); 
	$('#datEdD').prop("disabled", true);
	$('#datStD2').prop("disabled", true); 
	$('#datEdD2').prop("disabled", true);
	disableCal(true, 'datStD');
	disableCal(true, 'datEdD');
	disableCal(true, 'datStD2');
	disableCal(true, 'datEdD2');
	
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
	
	//조회 (개발요청목록)
	$('#btnQry').bind('click', function() {
		//cmc0500.getREQList
		getREQList2();
	});
	
	//조최 (외주개발요청목록)
	$('#btnQry2').bind('click', function() {
		//cmc0400.getREQList
		getREQList();
	});
	
	//발행자 엔터
	$('#txtEditor').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQry2').trigger('click');
		}
	});
	
	//개발종료
	$('#btnComplete').bind('click', function() {
		btnCompleteClick();
	});
	
	//삭제
	$('#btnDel').bind('click', function() {
		btnDelClick();
	});

	//등록
	$('#btnAdd').bind('click', function() {
		btnAddClick();
	});
	
	//신규등록
	$('#chkNew').bind('click', function() {
		chkNewClick();
	});
	
	//파일첨부
	$('#btnFileAdd').bind('click', function() {
		btnFileAddClick();
	});
	
	//파일삭제
	$('#btnFileDel').bind('click', function() {
		btnFileDelClick();
	});
	
	//추가
	$('#btnAddDevUser').bind('click', function() {
		devReqUserSelectArr = clone(userGridData);
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
						if(devReqUserSelectFlag) {
							userGridData = clone(devReqUserSelectData);
						}
						userGrid.setData(userGridData);
						
						mask.close();
					}
				}
			});
		}, 200);
	});
});

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
	
	cboGbnData2 = [
		{cm_codename : "전체목록", cm_micode : "00"},
		{cm_codename : "개인목록", cm_micode : "01"}
	]
	$('[data-ax5select="cboGbn2"]').ax5select({
		options: injectCboDataToArr(cboGbnData2, 'cm_micode' , 'cm_codename')
	});
	
	cboStaData2 = [
		{cm_codename : "진행중목록", 		cm_micode : "00"},
		{cm_codename : "개발요청종료건",	cm_micode : "01"}
	]
	$('[data-ax5select="cboSta2"]').ax5select({
		options: injectCboDataToArr(cboStaData2, 'cm_micode' , 'cm_codename')
	});
	
	getREQList();
	getREQList2();
}

function getJobCd() {
	var data = new Object();
	data = {
		SelMsg : 'SEL',
		closeYn : 'N',
		requestType	: 'getJobCd'
	}
	ajaxAsync('/webPage/common/CodeInfoServlet', data, 'json',successGetJobCd);
}

function successGetJobCd(data) {
	cboJobData = data;
	$('[data-ax5select="cboJob"]').ax5select({
		options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
	});
}

function getOtherUser() {
	var data = new Object();
	data = {
		requestType	: 'getOtherUser'
	}
	ajaxAsync('/webPage/common/UserInfoServlet', data, 'json',successGetOtherUser);
}

function successGetOtherUser(data) {
	$('#lstPM').empty(); //리스트 초기화
	var addId = null;
	var liStr = null;
	
	lstPMData = data;
	
	if(lstPMData == null || lstPMData.length < 0) return;
	
	liStr  = '';
	lstPMData.forEach(function(lstPMData, Index) {
		addId = lstPMData.cm_userid;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		liStr += '<div class="margin-3-top">';
		liStr += '	<input type="checkbox" class="checkbox-PM" id="chkPM'+addId+'" data-label="'+lstPMData.user+'"  value="'+lstPMData.cm_userid+'" />';
		liStr += '</div>';
		liStr += '</li>';
	});
	$('#lstPM').html(liStr);
	
	$('input.checkbox-PM').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
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

function getREQList() {
	var stDate = '';
	var edDate = '';
	
	if($('#chkReqDate2').is(':checked')) {
		stDate = $('#datStD2').val().trim();
		edDate = $('#datEdD2').val().trim();
	}
	
	var data = new Object();
	data = {
		status : getSelectedVal('cboSta2').cm_micode,
		UserID : userId,
		editorName : $('#txtEditor').val().trim(),
		cboGbn : getSelectedVal('cboGbn2').cm_micode,
		datStD : stDate,
		datEdD : edDate,
		DocType : '03',
		requestType	: 'getREQList'
	}
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div2);
	$(".loding-div2").show();
	ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successGetREQList);
}

function successGetREQList(data) {
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

function getREQList2() {
	var stDate = '';
	var edDate = '';
	
	if($('#chkReqDate').is(':checked')) {
		stDate = $('#datStD').val().trim();
		edDate = $('#datEdD').val().trim();
	}
	
	var data = new Object();
	data = {
		status : getSelectedVal('cboSta').cm_micode,
		UserID : userId,
		cboGbn : getSelectedVal('cboGbn').cm_micode,
		datStD : stDate,
		datEdD : edDate,
		DocType : 'def',
		requestType	: 'getREQList'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	console.log('[getREQList2] ==>',data);
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetREQList2);
}

function successGetREQList2(data) {
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
			getREQList();
		}
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
						mask.close();
					}
				}
			});
		}, 200);
	}else {
		getREQList();
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
	
	if(data != 'ok') {
		dialog.alert(data,function(){
			requestEnd();
			return;
		});
	}
	
	uploadLen++;
	if(uploadLen == fileGridData.length) getREQList();
}

function firstGrid_Click(selItem) {
	$('#chkNew').wCheck('check',true);
	chkNewClick();
	
	// SR 오픈시 버튼 비활성화
	var today = getDate('DATE',0);

	console.log("firstGrid_Click() > ", wksropen);
	
	if(today >= wksropen && selItem.CC_ACTTYPE == "01") {
		$('#btnAdd').prop("disabled", true);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
		$('#chkNew').prop("disabled", true);
	}else{
		$('#btnAdd').prop("disabled", false);
		$('#btnComplete').prop("disabled", false);
		$('#btnDel').prop("disabled", false);
		$('#chkNew').prop("disabled", false);
	}
	
	$('#txtTitle').val(selItem.CC_DOCSUBJ);
	$('#txtDetail').val(selItem.CC_DETAILSAYU);
	$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	$('#txtCause').val(selItem.CC_DOCNUM);
	$('#datePrcDate').val(selItem.CC_ENDPLAN);
	
	userGridData = [];
	userGrid.setData([]);
	fileGridData = [];
	fileGrid.setData([]);
	
	var tmpArr = null;
	var tmpArr2 = null;
	var tmpData = null;
	
	//file
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
	
	//주관부서
	tmpArr = null;
	if(selItem.CC_REQUSER3 != '' && selItem.CC_REQUSER3 != null && selItem.CC_REQUSER3 != undefined) {
		tmpArr = selItem.CC_REQUSER3.split(',');
		
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
		userGrid.setData(userGridData);
	}
	
	//외주PM
	var addId = null;
	lstPMData.forEach(function(item, index) {
		addId = item.cm_userid;
		$('#chkPM'+addId).wCheck('check', false);
	});
	
	if(selItem.C_TEAM != null && selItem.C_TEAM != undefined) {
		lstPMData.forEach(function(item, index) {
			if(item.cm_userid === selItem.C_TEAM) {
				$('#chkPM'+item.cm_userid).wCheck('check', true);
			}
		});
	}
}

function secondGrid_Click(selItem) {
	initCbobox('M');
	
	$('#btnAdd').text('수정');
	
	//선택된 아이템 status 값이 9, 3 이면 완료버튼, 수정버튼 비활성화
	if(selItem.CC_STATUS == '9' || selItem.CC_STATUS == '3') {
		$('#btnAdd').prop("disabled", true);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
	}else {
		if(selItem.CC_EDITOR == userId) {
			$('#btnAdd').prop("disabled", false);
			$('#btnComplete').prop("disabled", false);
		}else {
			$('#btnAdd').prop("disabled", true);
			$('#btnComplete').prop("disabled", true);
			$('#btnDel').prop("disabled", true);
		}		
	}
	
	//선택된 아이템 status 값이 0이 아닐때는 삭제버튼 비활성화
	if(selItem.CC_STATUS != '0') {
		$('#btnDel').prop("disabled", true);
	}else {
		$('#btnDel').prop("disabled", false);
	}
	

	// SR 오픈시 버튼 비활성화
	var today = getDate('DATE',0);

	if(today >= wksropen ) {
		if(selItem.CC_SRREQID == "N"){
			$('#btnAdd').prop("disabled", false);
			$('#btnComplete').prop("disabled", false);
			$('#btnDel').prop("disabled", false);
		}else{
			$('#btnAdd').prop("disabled", true);
			$('#btnComplete').prop("disabled", true);
			$('#btnDel').prop("disabled", true);
		}
	}

	if(today >= wksropen){
		$('#chkNew').prop("disabled", true);
	}

	//외주PM
	var addId = null;
	lstPMData.forEach(function(item, index) {
		addId = item.cm_userid;
		$('#chkPM'+addId).wCheck('check', false);
	});
	
	if(selItem.C_TEAM != null && selItem.C_TEAM != undefined) {
		lstPMData.forEach(function(item, index) {
			if(item.cm_userid === selItem.C_TEAM) {
				$('#chkPM'+item.cm_userid).wCheck('check', true);
			}
		});
	}
	
	var tmpArr = null;
	var tmpArr2 = null;
	var tmpData = null;
	
	//주관부서
	userGridData = [];
	userGrid.setData([]);
	if(selItem.CC_REQUSER3 != '' && selItem.CC_REQUSER3 != null && selItem.CC_REQUSER3 != undefined) {
		tmpArr = selItem.CC_REQUSER3.split(',');
		
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
	
	//file
	fileGridData = [];
	fileGrid.setData([]);
	if(selItem.fileinfos != '' && selItem.fileinfos != null && selItem.fileinfos != undefined) {
		tmpArr = selItem.fileinfos.split('$');
		
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
	
	//발행번호 표시하고 기존 발행번호 컴포넌트들 숨기기
	$('#divOrder').show();
	$('#txtOrderHide').val(selItem.CC_REQID); //발행근거
	$('#divReqNum0').hide();
	$('#divJob').hide();
	$('#divReqNum1').hide();
	
	strReqID = selItem.CC_REQID;
	$('#chkNew').wCheck('check',false);
	
	$('#txtCause').val(selItem.CC_DOCNUM); //발행근거
	$('#txtTitle').val(selItem.CC_DOCSUBJ); //요청제목
	$('#txtDetail').val(selItem.CC_DETAILJOBN); //상세내용
	$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	$('#datePrcDate').val(selItem.CC_ENDPLAN); //처리기한
	$('[data-ax5select="cboJob"]').ax5select("setValue", selItem.CC_JOBCD, true); //업무
}

function initCbobox(gbn) {
	fileGridData = [];
	fileGrid.setData([]);
	
	userGridData = [];
	userGrid.setData([]);
	
	if(cboJobData.length > 0) $('[data-ax5select="cboJob"]').ax5select("setValue", cboJobData[0].value, true);
	
	$('#txtDetail').val('');
	$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	$('#txtTitle').val('');
}

function chkNewClick() {
	var checkSw = $('#chkNew').is(':checked');
	
	if(checkSw) {
		initCbobox('M');
		$('#btnAdd').text('등록');
		$('#btnAdd').prop("disabled", false);
		$('#btnComplete').prop("disabled", true);
		$('#btnDel').prop("disabled", true);
		
		//그리드 선택했을때 비활성화됐던 콤포넌트들 다시 셋팅
		$('#divOrder').hide();
		$('#divReqNum0').show();
		$('#divJob').show();
		$('#divReqNum1').show();
		
		$('#txtTitle').val('');
		$('#txtDetail').val('');
		$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
		$('#txtCause').val('');
		
		$('#datStD').val(getDate('DATE',0));
		$('#datEdD').val(getDate('DATE',0));
		$('#datStD2').val(getDate('DATE',0));
		$('#datEdD2').val(getDate('DATE',0));
		$('#datePrcDate').val(getDate('DATE',0));
		picker.bind(defaultPickerInfoLarge('basic', 'top'));
		picker.bind(defaultPickerInfoLarge('basic2', 'top'));
		picker.bind(defaultPickerInfoLarge('basic3', 'bottom'));
		
		var addId = null;
		lstPMData.forEach(function(item, index) {
			addId = item.cm_userid;
			$('#chkPM'+addId).wCheck('check', false);
		});
		
	}else {
		$('#divOrder').hide();
		$('#divReqNum0').show();
		$('#divJob').show();
		$('#divReqNum1').show();
		
		if(cboJobData.length > 0) $('[data-ax5select="cboJob"]').ax5select("setValue", cboJobData[0].value, true);
	}
}

function userGrid_dblClick(selItem) {
	userGrid.removeRow(userGrid.selectedDataIndexs[0]);
	dialog.alert('삭제완료');
	userGridData = userGrid.getList();
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
			btnQryClick();
		}
	}
}

function btnAddClick() {
	var gridSelectedIndex = secondGrid.selectedDataIndexs;
	var gridselectedItem = secondGrid.list[gridSelectedIndex];
	
	if(gridselectedItem != undefined && gridselectedItem.CC_EDITOR != userId) {
		if($('#btnAdd').text() == '수정') {
			dialog.alert('작성자만 수정이 가능합니다.');
			return;
		}
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

function btnAddClick_sub(reqid) {
	if(getSelectedIndex('cboJob') < 1) {
		dialog.alert('요청부서를 선택 후 등록하십시오.');
		return;
	}
	
	if($('#txtTitle').val().length == 0) {
		dialog.alert("요청제목을 입력 후 등록하십시오.");
		return;
	}
	
	if($('#txtDetail').val().length == 0) {
		dialog.alert("상세요청내용을 입력 후 등록하십시오.");
		return;
	}
	
	if($('#txtDetail').val().length < 200) {
		dialog.alert("상세요청내용은 200자 이상입력하셔야 합니다.");
		return;
	}
	
	if($('#txtCause').val().length == 0) {
		dialog.alert("발행근거를 입력 후 등록하십시오.");
		return;
	}
	
	var findSw = false;
	lstPMData.forEach(function(ulItem, index) {
		if($('#chkPM'+ulItem.cm_userid).is(':checked')) findSw = true;
	});
	
	if(!findSw) {
		dialog.alert("외주PM을 선택 후 등록하십시오.");
		return;
	}
	
	var teamList = [];
	var tmpData = null;
	lstPMData.forEach(function(item, index) {
		if($('#chkPM'+item.cm_userid).is(':checked')) {
			tmpData = new Object();
			tmpData.cm_userid = item.cm_userid;
			tmpData.cm_project = item.cm_project;
			teamList.push(tmpData);
			tmpData = null;
		}
	});
	
	var tmpObj = new Object();
	tmpObj.CC_DOCTYPE = '03' 							//외주문서 - 외주문서 micode는 03
    tmpObj.CC_DOCNUM = $('#txtCause').val(); 			//문서번호 - 발행근거 들어가면됌
    tmpObj.CC_DOCSUBJ = $('#txtTitle').val();
    tmpObj.CC_DEPT1 = ''; 								//대내부점 콤보
    tmpObj.CC_REQUSER1 = ''; 							//대내부점요청자 아이디 
    tmpObj.CC_DEPT2 = ''; 								//대외기관 요청자 콤보 
    tmpObj.CC_REQUSER2 = ''; 							//대외기관 요청자
    tmpObj.CC_DOCNUM2 = $('#txtCause').val(); 			//주관부서 문서번호 - 발행근거랑 똑같은거 들어가면됌
    tmpObj.CC_DETAILJOBN = $('#txtDetail').val();		//업무상세
    tmpObj.CC_REQTYPE = '';								//요청유형
    tmpObj.CC_ENDPLAN = $('#datePrcDate').val(); 		//처리기한
    tmpObj.CC_JOBCD = getSelectedVal('cboJob').cm_jobcd;//업무콤보
    tmpObj.CC_ACTTYPE = '';								//조치유형
    tmpObj.CC_JOBDIF = '';								//업무난이도
    tmpObj.CC_DEVSTDT = '';								//개발기간                               
    tmpObj.CC_DEVEDDT = '';								//개발기간                              
   	tmpObj.CC_DETAILSAYU = $('#txtDetail').val();		//추가업무내용
    tmpObj.CC_EDITOR = userId;     
    tmpObj.CC_ETTEAM = userDeptCd;

    var reqType = '';
    if( $('#chkNew').is(':checked') ) {
		reqType = 'setREQInfo';
		tmpObj.CC_REQID = $('#txtReqNum0').val()+"-"+getSelectedVal('cboJob').cm_jobname+"-"+getDate('DATE',0).substr(0,4);
	}else {
		var gridSelectedIndex = secondGrid.selectedDataIndexs;
		var gridselectedItem = secondGrid.list[gridSelectedIndex];
		
		reqType = 'setREQupdt';
		tmpObj.CC_REQID = gridselectedItem.CC_REQID;
	}
	
    var data = new Object();
	data = {
		etcData : tmpObj,
		TeamList : lstPMData,
		UserList : userGridData,
		tmpRunnerList : lstPMData,
		requestType	: reqType
	}
	console.log('['+reqType+'] ==>', data);
	
	if(reqType == 'setREQInfo') ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successSetREQInfo);
	else ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successSetREQupdt);
}

function successSetREQInfo(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	strReqID = data;
	if(strReqID != null && strReqID != '' && strReqID != undefined && strReqID.length > 0) {
		dialog.alert('등록되었습니다.');
		
		if(fileGridData.length > 0) {
			//파일업로드
			startFileupload();
		}else {
			$('#chkNew').wCheck('check',true);
			getREQList();
			screenInit();
		}
	}else {
		dialog.alert('등록이 실패되었습니다.');
		return;
	}
}

function successSetREQupdt(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	strReqID = data;
	if(strReqID != null && strReqID != '' && strReqID != undefined && strReqID.length > 0) {
		dialog.alert('수정되었습니다.');
		
		// status가 0이 아닌 아이템(그리드에서 선택된)선택했을때 비활성화됐던 삭제버튼 다시 활성화시키기
		$('#btnDel').prop("disabled", false);
		
		if(fileGridData.length > 0) {
			//파일업로드
			startFileupload();
		}else {
			$('#chkNew').wCheck('check',true);
			chkNewClick();
			btnQryClick();
		}
	}else {
		dialog.alert('수정이 실패되었습니다.');
		return;
	}
}

function btnDelClick() {
	var gridSelectedIndex = secondGrid.selectedDataIndexs;
	var gridselectedItem = secondGrid.list[gridSelectedIndex];
	
	if(gridselectedItem != undefined && gridselectedItem.CC_EDITOR != userId) {
		dialog.alert('작성자만 삭제 가능합니다.');
		return;
	}
	
	confirmDialog.confirm({
		title: '확인',
		msg: '개발요청서를 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var delObj = new Object();
			delObj.delREQinfo = gridselectedItem.CC_REQID;
			delObj.delStrUserID = userId;
			
			var data = new Object();
			data = {
				delREQ : delObj,
				requestType	: 'delREQinfo'
			}
			console.log('[delREQinfo] ==>', data);
			ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successDelREQinfo);
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
		$('#chkNew').wCheck('check',true);
		chkNewClick();
		getREQList();
	}else {
		dialog.alert('관련된 업무지시서가 모두 종료되지 않았습니다.');
		return;
	}
}

function btnCompleteClick() {
	var gridSelectedIndex = secondGrid.selectedDataIndexs;
	var gridselectedItem = secondGrid.list[gridSelectedIndex];
	
	if(gridSelectedIndex < 0) return;
	
	if(gridselectedItem != undefined && gridselectedItem.CC_EDITOR != userId) {
		dialog.alert('작성자만 종료 가능합니다.');
		return;
	}
	
	var data = new Object();
	data = {
		userID : userId,
		reqID : gridselectedItem.CC_REQID,
		requestType	: 'statusUpdt'
	}
	console.log('[statusUpdt] ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successStatusUpdt);
}

function successStatusUpdt(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	devReqStaList = data;
	if(devReqStaList != null && devReqStaList != undefined) {
		if(devReqStaList.length == 0) {
			dialog.alert('종료 되었습니다.');
			return;
		}else {
			setTimeout(function() {
				devReqListModal.open({
					width: 400,
					height: 650,
					iframe: {
						method: "get",
						url: "../modal/dev/DevReqListModal.jsp"
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

function screenInit() {
	$('#txtTitle').val('');
	$('#txtDetail').val('');
	$('#lblDetail').text('총 (' + $('#txtDetail').val().length + ')자');
	$('#txtCause').val('');
	
	userGridData = [];
	userGrid.setData([]);
	fileGridData = [];
	fileGrid.setData([]);
}