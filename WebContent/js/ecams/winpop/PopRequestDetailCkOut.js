var pReqNo  = null;
var pReqCd  = null;
var pUserId = null;
var reqCd   = null; //선후행작업 reqcd 파라미터
var rgtList = null;

var adminYN = window.top.adminYN;

var reqGrid     = new ax5.ui.grid();
var resultGrid  = new ax5.ui.grid();

var confirmDialog2 		= new ax5.ui.dialog();
var requestSayuModal	= new ax5.ui.modal();

var reqInfoData    		= [];
var reqGridData    		= []; //체크인목록그리드 데이타
var reqGridOrgData		= []; //체크인목록그리드 original 데이타 (변경X)
var reqGridChgData 		= []; //체크인목록그리드 항목상세보기 데이타 
var resultGridData 		= []; //처리결과그리드 데이타
var selGridData			= []; //로컬파일 다운 데이타
var cboEditor			= [];
var cboEditorData		= [];

var data           = null; //json parameter
var myWin 		   = null; //새창id

var isAdmin 	   = false;
var ingSw          = false;

var DocDownPath	   = "";
var strTmpDir	   = "";
var strLIB		   = "N";

//한번만 실행하도록 함
var gridSw1		   = false;
var gridSw2		   = false;

var f = document.getReqData;
var popF = document.setReqData;

var closeCk = false;

var $activeTab = "tab1Li";
var refreshCk = true;

pReqNo = f.acptno.value;
pReqCd = pReqNo.substr(4,2);
pUserId = f.user.value;
rgtList = f.rgtList.value;

reqCd = pReqCd;

confirmDialog.setConfig({
    lang:{
        "ok": "확인", "cancel": "취소"
    },
    width: 500
});
confirmDialog2.setConfig({
	Title: "확인",
    theme: "info",
    width: 500
});

var reqGridColumns = [
	{key: "cr_rsrcname", 	label: "프로그램명",		width: '12%'},
	{key: "cr_story", 		label: "프로그램설명",		width: '14%'},
	{key: "cr_editcon", 	label: "변경사유",			width: '14%'},
	{key: "cm_codename", 	label: "프로그램종류",		width: '9%'},
	{key: "cm_dirpath", 	label: "프로그램경로",		width: '20%'},
	{key: "cr_expday", 		label: "반환예정일",		width: '9%',	align: "center"},
    {key: "cm_jobname", 	label: "업무",  			width: '10%'},
    {key: "cr_version", 	label: "버전",  			width: '6%',	align: "center"},
    {key: "prcrst", 		label: "처리결과",  		width: '10%',	align: "center"},
    {key: "prcdate", 		label: "처리일시",  		width: '10%',	align: "center"},
]
$('#txtAcptNo').val(pReqNo.substr(0,4) + "-" + pReqNo.substr(4,2) + "-" + pReqNo.substr(6,6));

function createViewGrid1() {
	reqGrid.setConfig({
	    target: $('[data-ax5grid="reqGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    showLineNumber: true,
	    paging: false,
	    header: {
	        align: "center"
	    },
	    columnKeys : {disableSeletion : ""},
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	           this.self.select(this.dindex);
	           reqGrid_Click();
	        },
	        onDBLClick: function () {
	        	if (this.dindex < 0) return;
	
	        	if(this.item == null){
	        		return;
	        	}

		       	openWindow('RESULTVIEW', '', this.item.cr_baseitem);
	        },
	    	trStyleClass: function () {
				if (this.item.cr_itemid != this.item.cr_baseitem && this.item.ColorSw != '5'){
					return "fontStyle-ing";
				} else if(this.item.ColorSw == '3'){
					return "fontStyle-cncl";
				} else if(this.item.ColorSw == '5'){
					return "fontStyle-error";
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
	            {type: 5, label: "삭제"},
	            {type: 1, label: "프로그램정보"},
	            {type: 2, label: "프로그램 상세보기"},
	            {type: 3, label: "소스보기"},
	            {type: 4, label: "소스비교"},
	        ],
	        popupFilter: function (item, param) {
	         	reqGrid.clearSelect();
	         	reqGrid.select(Number(param.dindex));
	         	
		       	var selIn = reqGrid.selectedDataIndexs;
		       	if(selIn.length === 0) return;
	       	 
	        	if (param.item == undefined) return false;
	        	if (param.dindex < 0) return false;
	        	
	        	if (isAdmin || param.item.secusw == 'Y' ||
	        			!$('#btnApproval').is(':disabled') ) { 
	        		
	        		var retType = '';
	        		
        			retType = '1';
	        		
        			if (param.dindex >= 0) {
	        			retType = retType+'2';
	        		}
	        		
	        		/*findSw = false;
					//컴파일(39,61,1), 릴리즈스크립트(51,64,21), 적용스크립트 스크립트 실행(59,67,35), 형상관리저장 스크립트 실행(22), 체크아웃스크립트 실행(14)
	        		if (pReqCd == '01' || pReqCd == '02' || pReqCd == '11') {
	                	if (param.item.cm_info.substr(13,1) == '1') {
	                		findSw = true;
	                	}
	                } else if (pReqCd == '07') {
	                	if (param.item.cm_info.substr(21,1) == '1') {
	                		findSw = true;
	                	}
	                } else if (pReqCd == '08') {
	                	if (param.item.cm_info.substr(38,1) == '1' || param.item.cm_info.substr(50,1) == '1' || param.item.cm_info.substr(58,1) == '1' || param.item.cm_info.substr(21,1) == '1') {
	                		findSw = true;
	                	}
	                } else if (pReqCd == '03') {
	                	if (param.item.cm_info.substr(60,1) == '1' || param.item.cm_info.substr(63,1) == '1' || param.item.cm_info.substr(66,1) == '1' || param.item.cm_info.substr(21,1) == '1') {
	                		findSw = true;
	                	}
	                } else if (pReqCd == '04') {
	                	if (param.item.cm_info.substr(0,1) == '1' || param.item.cm_info.substr(20,1) == '1' || param.item.cm_info.substr(34,1) == '1' || param.item.cm_info.substr(21,1) == '0') {
	                		findSw = true;
	                	}
	                }
				    if (findSw) {
	        			retType = retType+'3';
					}*/
        			
        			if (param.item.cm_info.substr(11, 1) == "1" && (param.item.cm_info.substr(9, 1) == "0" ||
        				param.item.cm_info.substr(26, 1) == "1" || param.item.cm_info.substr(45, 1) == "1")) {
        				retType = retType+'3';
        			}


					if (reqInfoData[0].prcsw == "0" && param.item.ColorSw == "5") {
					    if (isAdmin || pUserId == reqInfoData[0].cr_editor) {
		        			retType = retType+'5';
					    }
					}

				    if (((param.item.cm_info.substr(11,1) == "1" && param.item.cm_info.substr(9,1) == "0") ||
				         param.item.cm_info.substr(26,1) == "1") && param.item.diffacpt == "true") {
	        				retType = retType+'4';
				    }
	
				    if (retType == '') return false;
				    
				    var retString;
				    
				    if (retType.indexOf('1')>-1){
				    	retString = (item.type == 1);
				    }
				    if (retType.indexOf('2')>-1){
				    	if (retType == '') retString = (item.type == 2);
				    	else retString = retString | (item.type == 2);
				    }
				    if (retType.indexOf('3')>-1){
				    	if (retType == '') retString = (item.type == 3);
				    	else retString = retString | (item.type == 3);
				    }
				    if (retType.indexOf('4')>-1){
				    	if (retType == '') retString = (item.type == 4);
				    	else retString = retString | (item.type == 4);
				    }
				    if (retType.indexOf('5')>-1){
				    	if (retType == '') retString = (item.type == 5);
				    	else retString = retString | (item.type == 5);
				    }
				    /*
				    else if (retType == '12') return item.type == 1 | item.type == 2;
				    else if (retType == '123') return item.type == 1 | item.type == 2 | item.type == 3;
				    else if (retType == '124') return item.type == 1 | item.type == 2 | item.type == 4;
				    else if (retType == '125') return item.type == 1 | item.type == 2 | item.type == 5;

				    else if (retType == '1234') return item.type == 1 | item.type == 2 | item.type == 3 | item.type == 4;
				    else if (retType == '1235') return item.type == 1 | item.type == 2 | item.type == 3 | item.type == 5;
				    else if (retType == '1245') return item.type == 1 | item.type == 2 | item.type == 4 | item.type == 5;
				    else if (retType == '12345') return item.type == 1 | item.type == 2 | item.type == 3 | item.type == 4 | item.type == 5;
				    else */
				    return retString;
				    
				} else {
					return false;
				}
	        },
	        onClick: function (item, param) {
	        	console.log(param.item)
	        	if (item.type == '1') openWindow('PROGINFO', '', param.item.cr_baseitem);
	        	else if (item.type == '2')  openWindow('PROGDETAIL', '', param.item.cr_baseitem);
	        	else if (item.type == '3')  openWindow('SRCVIEW', '', param.item.cr_baseitem);	        		
	        	else if (item.type == '4') openWindow('SRCDIFF', '', param.item.cr_baseitem);
	        	else if (item.type == '5') {
	        		if (ingSw) {
	        			confirmDialog2.alert('현재 신청하신 다른 내용을 처리 중입니다.');
	    			} else {
	    				mask.open();
	    		        confirmDialog.confirm({
					        title: '삭제확인',
	    					msg: '['+param.item.cr_rsrcname+']를 삭제처리할까요?',
	    				}, function(){
	    					if(this.key === 'ok') {
	    						delReq(param.item);
	    					}
	    				});
						mask.close();
	    			}
	        	}
	        	
	            reqGrid.contextMenu.close();//또는 return true;
	        }
	    },
	    columns: reqGridColumns
	});

	if (pReqCd == "11") {
		reqGrid.addColumn({key: "cr_confno", label: "체크아웃신청번호",  width: '10%', align: 'center'}, 6);
	}
	else if (pReqCd == "12") {
		reqGrid.addColumn({key: "cr_confno", label: "테스트신청번호",  width: '10%', align: 'center'}, 6);
	}
};

function createViewGrid2() {
	resultGrid.setConfig({
		target: $('[data-ax5grid="resultGrid"]'),
		sortable: true, 
		multiSort: true,
		showLineNumber: true,
	    paging: false,
		header: {
			align: "center"
		},
		body: {
			onClick: function () {
				this.self.clearSelect();
				this.self.select(this.dindex);
			},
			onDBLClick: function () {
				if (this.dindex < 0) return;
				
				var selIn = resultGrid.selectedDataIndexs;
				if(selIn.length === 0) return;

				openWindow('RESULTVIEW', '', this.item.cr_seqno);
			},
			trStyleClass: function () {
				if (this.item.ColorSw == '3'){
					return "fontStyle-cncl";
				} else if(this.item.ColorSw == '0'){
					return "fontStyle-ing";
				} else if(this.item.ColorSw == '5'){
					return "fontStyle-error";
				} 
			},
			onDataChanged: function(){
				this.self.repaint();
			}
		},
		columns: [
			{key: "prcsys", 	label: "구분",  		width: '10%'},
			{key: "cr_rsrcname",label: "프로그램명",  	width: '15%'},
			{key: "jawon", 		label: "프로그램종류", 	width: '15%'},
			{key: "cm_dirpath", label: "적용경로",  	width: '20%'},
			{key: "cr_svrname", label: "적용서버",  	width: '20%'},
			{key: "prcrst", 	label: "처리결과",  	width: '10%',	align: 'center'},
			{key: "prcdate", 	label: "처리일시",  	width: '10%', 	align: 'center'}
		]
	});
	
	getRstList();
};

$(document).ready(function(){
	//상위 TITLE TEXT SET
	var reqCodes = getCodeInfoCommon([
		new CodeInfo('REQUEST','','N'),
		new CodeInfo('REQSAYU','SEL','N')
	]);
	var reqCodeDatas = reqCodes.REQUEST;
	cboPassData = reqCodes.REQSAYU;
	
	var i=0;
	var contentHistory = '';
	reqCodeDatas.forEach(function(item) {
		if(item.cm_micode == '01') item.cm_codename = '대여요청';
		else if(item.cm_micode == '11') item.cm_codename = '대여취소요청';
	});
	for (i=0; i<reqCodeDatas.length; i++) {
		if (reqCodeDatas[i].cm_micode == reqCd) {
			contentHistory = "변경신청 <strong> &gt; "+ reqCodeDatas[i].cm_codename+"상세</strong>";
			break;
		}
	}
	
	$('#reqBody').contents().find('#history_wrap').html(contentHistory);
	reqCodeDatas = null;
	
	startFunction();
});

function startFunction() {
	
	$('input.checkbox-detail').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
	if (pReqNo == null) {
		confirmDialog2.alert('신청정보가 없습니다.\n다시 로그인 하시기 바랍니다.');
		return;
	}
	
	setTabMenu();
	
	$('#tab1Li').width($('#tab1Li').width()+10);
	$('#tab2Li').width($('#tab2Li').width()+10);

	/**
	 * ------------------------------------------------------------------------------------------------------------------------------
	 *                                                     checkbox click event
	 * ------------------------------------------------------------------------------------------------------------------------------
	 */
	//항목상세보기
	$('#chkDetail').bind('click',function(){
		if(!detailCk){
			detailCk = true;
			getProgList();
		}
		
		gridData_Filter();
	});
	
	
	/**
	 * ------------------------------------------------------------------------------------------------------------------------------
	 *                                                        button click event
	 * ------------------------------------------------------------------------------------------------------------------------------
	 */
	//첨부문서확인 클릭
	$('#btnTestDoc').bind('click', function() {
		requestDocModal.open({
	        width: 700,
	        height: 360,
	        iframe: {
	            method: "get",
	            url: "../modal/request/RequestDocModal.jsp",
	            param: "callBack=requestDocModalCallBack"
	        },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	                mask.close();
	            }
	        }
	    }, function () {
	    });
	});
	
	//변경신청자 수정 클릭
	$('#btnUpdt').bind('click', function() {
		updtEditor();
	});
	
	//전체재처리 클릭
	$('#btnRetry').bind('click', function() {
		if (ingSw) {
			confirmDialog2.alert('현재 신청하신 다른 내용을 처리 중입니다.');
			return;
		}
		mask.open();
        confirmDialog.confirm({
			title: '작업확인',
			msg: '전체 재처리를 시작할까요?',
		}, function(){
			if(this.key === 'ok') {
				svrProc('Retry');
			}
			mask.close();
		});
	});
	//오류건 재처리 클릭
	$('#btnErrRetry').bind('click', function() {
		if (ingSw) {
			confirmDialog2.alert('현재 신청하신 다른 내용을 처리 중입니다.');
			return;
		}
		mask.open();
        confirmDialog.confirm({
			title: '작업확인',
			msg: '오류건에 대한 재처리를 시작할까요?',
		}, function(){
			if(this.key === 'ok') {
				svrProc('Errtry');
			}
			mask.close();
		});
	});
	//완료처리 클릭
	$('#btnStepEnd').bind('click', function() {
		if (ingSw) {
			confirmDialog2.alert('현재 신청하신 다른 내용을 처리 중입니다.');
			return;
		}

     	mask.open();
        confirmDialog.confirm({
			title: '완료처리확인',
			msg: '[신청번호 :'+$('#txtAcptNo').val()+']를 완료처리 할까요?',
		}, function(){
			if(this.key === 'ok') {
				nextConfSelf('1', '수기완료처리');
			}
			mask.close();
		});
	});
	//새로고침 클릭
	$('#btnQry').bind('click', function() {
		refresh();
	});
	//결재클릭
	$('#btnApproval').bind('click', function() {
		if (ingSw) {
			confirmDialog2.alert('현재 신청하신 다른 내용을 처리 중입니다.');
			return;
		}
		mask.open();
        confirmDialog.confirm({
			title: '결재확인',
			msg: '결재처리하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
		        nextConf('1',$('#txtApprovalMsg').val());
			}
			mask.close();
			//$('#btnQry').trigger('click');
		});
	});
	//반려클릭
	$('#btnCncl').bind('click', function() {
		if (ingSw) {
			confirmDialog2.alert('현재 신청하신 다른 내용을 처리 중입니다.');
			return;
		}
	    if($('#txtApprovalMsg').val() == ''){
	    	confirmDialog2.alert('반려의견을 입력하여 주십시오.');
	    	return;
	    }
     	mask.open();
        confirmDialog.confirm({
			title: '반려확인',
			msg: '반려처리하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
		        nextConf('3',$('#txtApprovalMsg').val());
			}
			mask.close();
		});
	});
	
	//전체회수
	$("#btnAllCncl").bind("click", function(){
		if (ingSw) {
			confirmDialog2.alert('현재 신청내용 처리 중입니다. 잠시 후 이용해 주세요.');
		} else {
			mask.open();
	        confirmDialog.confirm({
				title: '전체회수',
				msg: '[' + $('#txtAcptNo').val() +'] 를 전체회수 할까요?',
			}, function(){
				if(this.key === 'ok') {
					confirmDialog.prompt({
				        title: "전체회수",
				        msg: '전체회수 사유를 입력하시기 바랍니다.'
				    }, function () {
				        if(this.key === 'ok') {
				        	if (this.input.value.trim() == '' || this.input.value.length == 0) {
				        		confirmDialog2.alert('전체회수 사유를 입력하시기 바랍니다.');
				        	} else {
				        		allCncl(this.input.value);
				        	}
				        }
				    });
				}
				mask.close();
			});
		}
	});
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		/*
		if (window.opener.getRequestList != undefined){
			//window.opener.getRequestList();
		}
		*/
		closeCk = true;
		window.open("about:blank","_self").close();
	});

	$("#btnExcel").bind("click", function(){
		reqGrid.exportExcel(pReqNo+"_체크인목록.xls");
	});
	
	$("#btnExcel2").bind("click", function(){
		resultGrid.exportExcel(pReqNo+"_처리결과확인.xls");
	});
	
	/**
	 * ------------------------------------------------------------------------------------------------------------------------------
	 *                                             button click -> window open event
	 * ------------------------------------------------------------------------------------------------------------------------------
	 */
	//로그확인 클릭
	$('#btnLog').bind('click', function() {
		openWindow('LOGVIEW', '', '');
	});
	//결재정보 클릭
	$('#btnApprovalInfo').bind('click', function() {
		openWindow('APPROVAL', '', '');
	});
	//프로그램 상세
	$("#btnProgDetail").bind("click", function(){
		openWindow('PROGDETAIL', '', reqGrid.list[reqGrid.selectedDataIndexs].cr_itemid);
	});
	//재신청
	$("#btnRechkOut").bind("click", function(){
		openWindow('RECHKOUT', '', '');
	});
	
	setTimeout(function() {
		//최초 화면로딩 시 조회(새로고침버튼 로직)
		$('#btnQry').trigger('click');
	}, 20);
};

// 새로고침
function refresh() {
	detailCk = false;
	$("#chkDetail").wCheck("check", false);
	
	resetScreen();
	checkAdmin();
	getUserRGTCDList();
	chkBtnEnabled();
}

//환성화 비활성화 초기화로직
function resetScreen(){
	$("#chkDetail").wCheck("check", false);
    
	$('#btnTestDoc').hide();					//첨부문서확인
	$("#btnRetry").prop("disabled",true);
	$("#btnErrRetry").prop("disabled",true);
	$("#btnAllCncl").prop("disabled",true);
	$("#btnStepEnd").prop("disabled",true);
	$("#btnLog").prop("disabled",true);
	$("#btnQry").prop("disabled",true);
	$("#btnApproval").prop("disabled",true);
	$("#btnCncl").prop("disabled",true);
	$("#btnApprovalInfo").prop("disabled",true);
	$("#btnProgDetail").prop("disabled",true);
	$("#btnRechkOut").hide();
	$("#divConf").hide();
	
	// 변경신청자
	$("#lbUpdt").hide();
	$("#cboEditor").hide();
	$("#txtUpdt").hide();
	$("#btnUpdt").hide();

	$("#cboEditor").ax5select("disable");
	$("#txtSayu").removeAttr("readonly");

	$("#txtSyscd").val("");
	$("#txtEditor").val("");
	$("#txtAcptDate").val("");
	$("#txtPrcDate").val("");
	$("#txtStatus").val("");
	$("#txtSayu").val("");
    
    chkRechkOut();
}
var befJobModalCallBack = function() {
	befJobListModal.close();
}
//항목상세보기
function gridData_Filter(){
	if (reqGridOrgData.length < 1) return;
	
	reqGridChgData = clone(reqGridOrgData);
	
	if(reqGridChgData.length == 0) {
		reqGridData = clone(reqGridOrgData);
		return;
	}
	
	for(var i =0; i < reqGridChgData.length; i++){
		if(reqGridChgData[i].cr_baseitem != reqGridChgData[i].cr_itemid){
			reqGridChgData.splice(i,1);
			i--;
		}
	};

	if (!$('#chkDetail').prop('checked')){
		reqGridData = clone(reqGridChgData);
		reqGrid.setData(reqGridData);
		//reqGrid.repaint();
		reqGrid.align();
	} else {
		reqGridData = clone(reqGridOrgData);
		for(var i =0; i < reqGridData.length; i++) {
			if(reqGridData[i].cr_baseitem != reqGridData[i].cr_itemid){
				reqGridData[i].filterData = true;
				reqGridData[i].__disable_selection__ = true;
			} else {
				reqGridData[i].filterData = false;
				reqGridData[i].__disable_selection__ = false;
			}
		}
		reqGrid.setData(reqGridData);
		//reqGrid.repaint();
		reqGrid.align();
	}
	// 처음에 sort 가 제대로 안먹혀 sort 한번 실행
	if(reqGrid.sortInfo["cr_rsrcname"] == undefined){
		var sortInfo = {
				cr_rsrcname : {seq : 0, orderBy: "desc"}
		}
		reqGrid.setColumnSort(sortInfo);
	}
	gridSw1 = false;
}

function reqGrid_Click() {
	var selIndex = reqGrid.selectedDataIndexs;
	if(selIndex < 0) {
		$('#btnProgDetail').prop("disabled", true);
		return;
	}
	$('#btnProgDetail').prop("disabled", false);
	
}

function chkRechkOut() {
	data = new Object();
	data = {
		AcptNo : pReqNo,
		UserId : pUserId,
		requestType : 'chkRechkOut'
	}
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', data, 'json',successRechkOut);
}

function successRechkOut(data) {
	if(data) $('#btnRechkOut').show();
	else $('#btnRechkOut').hide();
}

//전체회수 처리시작
function allCncl(inputMsg) {
	ingSw = true;
	
	data =  new Object();
	data = {
		AcptNo			: pReqNo,
		UserId			: pUserId,
		conMsg			: inputMsg,
		ConfUsr			: reqInfoData[0].confusr,
		requestType		: 'reqCncl'
	}
	ajaxAsync('/webPage/ecmr/Cmr3200Servlet', data, 'json',successReqCncl);
}
//전체회수 처리완료
function successReqCncl(data) {
	ingSw = false;
	
	if (data == '0') {
		confirmDialog2.alert('전체회수 처리가 완료되었습니다.', function(){
			window.open("about:blank","_self").close();
			/*
			if (window.opener.getRequestList != undefined){
				//window.opener.getRequestList();
			}
			*/
		});
	} else if (data == '2') {
		confirmDialog2.alert('현재 형상관리서버에서 다른처리를 진행하고 있습니다.\n잠시 후 다시 처리하여 주시기 바랍니다.');
	} else {
		confirmDialog2.alert('체크인 회수 처리가 지연되고 있습니다. 관리자에게 문의하시기 바랍니다.\n\n'+data);
	}
	$('#btnQry').trigger('click');
}

//단계완료
function nextConfSelf(gyulGbn, conMsg) {
	ingSw = true;
	
	if (null == conMsg || undefined  == conMsg || 'undefined ' == conMsg) {
		conMsg = '';
	}
	
	
	data =  new Object();
	data = {
		AcptNo 		: pReqNo,
		UserId 		: reqInfoData[0].signteam,
		conMsg 		: conMsg,
		Cd  		: gyulGbn,
		ReqCd		: reqInfoData[0].cr_qrycd,
		requestType	: 'nextConf'
	}
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json', successNextConf);
}

//결재, 반려 실행
function nextConf(gyulGbn, conMsg) {
	ingSw = true;
	
	if (null == conMsg || undefined  == conMsg || 'undefined ' == conMsg) {
		conMsg = '';
	}
	
	data =  new Object();
	data = {
		AcptNo 		: pReqNo,
		UserId 		: pUserId,
		conMsg 		: conMsg,
		Cd  		: gyulGbn,
		ReqCd		: reqInfoData[0].cr_qrycd,
		requestType	: 'nextConf'
	}
	ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json',successNextConf);
}

//결재, 반려 처리완료
function successNextConf(data) {
	ingSw = false;
	
	if (data == '1' || data == '3') {
		confirmDialog2.alert('처리가 완료되었습니다.', function(){
			try {
				if(window.opener.getRequestList != null && window.opener.getRequestList != undefined) {
					window.opener.getRequestList();
				}
			}catch(e) {
			}
			close();
		});
	}else{
		confirmDialog2.alert('처리에 실패했습니다. \n[ERROR='+data+']');
	}
//	$('#btnQry').trigger('click');
}

//자동처리 실행
function svrProc(prcSysGbn) {
	ingSw = true;
	
	data =  new Object();
	data = {
		AcptNo		: pReqNo,
		UserId		: pUserId,
		prcCd		: prcSysGbn,
		prcSys		: reqInfoData[0].signteam,
		requestType	: 'svrProc'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json',successSvrProc);
}
//자동처리 완료
function successSvrProc(data) {
	ingSw = false;

	if (data == '0') {
		if (reqInfoData[0].signteam == "SYSPDN" || reqInfoData[0].signteam == "SYSPUP") {
			getProgFileList();
		}else if (reqInfoData[0].signteam == "SYSFMK") {
			svrFileMake();
		}else {
			confirmDialog2.alert("재처리작업이 신청되었습니다. 잠시 후 다시받기를 하여 확인하여 주시기 바랍니다.");
		}
	}else if (data == '2') {
		confirmDialog2.alert("현재 서버에서 다른처리를 진행 중입니다. 잠시 후 다시 처리하여 주시기 바랍니다.");
	} else  {
		confirmDialog2.alert("재처리작업 신청 중 오류가 발생하였습니다.\n\n"+data);
	}
}

//어드민 여부 확인
function checkAdmin(){
	data =  new Object();
	data = {
		UserID		: pUserId,
		requestType	: 'isAdmin'
	}
	ajaxAsync('/webPage/common/UserInfoServlet', data, 'json',successGetUserInfo);
}

//어드민 여부 확인 완료
function successGetUserInfo(data) {
	isAdmin = data;
}

function getUserRGTCDList() {
	data =  new Object();
	data = {
		UserID		: pUserId,
		closeYn		: 'N',
		requestType	: 'getUserRGTCDList'
	}
	ajaxAsync('/webPage/common/UserInfoServlet', data, 'json',successGetUserRGTCD);	
}

function successGetUserRGTCD(data) {
	var tmp = data;
	var tmpArray = tmp.split(",", tmp.length);
	
	for(var i = 0; i < tmpArray.length; i++) {
		if(tmpArray[i] == "LIB") strLIB = "Y";
	}
	
	getReqInfo();
}

function chkBtnEnabled() {
	data =  new Object();
	data = {
		UserId		: pUserId,
		AcptNo		: pReqNo,
		requestType	: 'chkBtnEnabled'
	}
	ajaxAsync('/webPage/ecmr/Cmr0251Servlet', data, 'json',successChkBtnEnabled);	
}

function successChkBtnEnabled(data) {
	if(data.length < 1) return;
	
	//완료건은 전체회수 버튼 비활성화
	if ($("#txtPrcDate").val().length > 0) {
		$('#btnAllCncl').prop("disabled", true);
	} else {
		$('#btnAllCncl').prop("disabled", !Boolean(data[0].isBack));
	}	
	$('#btnRetry').prop("disabled", !Boolean(data[0].isRetry));
}

function setTabMenu(){
	$(".tab_content:first").show();

	if (!gridSw1) {
		createViewGrid1();
		gridSw1 = true;
	}
	
	$("ul.tabs li").click(function () {
		$(".tab_content").hide();
		var activeTab = $(this).attr("rel");
		$("ul.tabs li").removeClass('on');
		$(this).addClass("on");
		$activeTab = $("ul.tabs li.on").prop("id");
		$("#" + activeTab).show(0,function(){
			$(window).trigger("resize");
			if(refreshCk){
				refreshCk = false;
				if($activeTab == "tab1Li"){
					getProgList();
				} else {
					if (!gridSw2) {
						createViewGrid2();
						gridSw2 = true;
					}
					//getRstList();
				}
			}

			if($activeTab == "tab1Li"){
				reqGrid.align();
			}else {
				resultGrid.align();
			}
		});

	});
}
//신청정보가져오기
function getReqInfo() {
	data =  new Object();
	data = {
		UserId	: pUserId,
		AcptNo	: pReqNo,
		requestType	: 'getReqList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json',successGetReqList);
}
 
//체크인 목록가져오기 완료
function successGetProgList(data) {
	console.log(data);

	$(".loding-div").remove();
	reqGridData = data;
	reqGridOrgData = data;
	
	if (reqGridData.length > 0 ) {
	 	$("#btnApprovalInfo").prop("disabled",false);
	 	$("#btnQry").prop("disabled",false);
	}
	//항목상세보기 옵션
	gridData_Filter();
}

//처리결과 가져오기
function getRstList() {
	data =  new Object();
	data = {
		UserId			: pUserId,
		AcptNo			: pReqNo,
		prcSys			: '',
		requestType		: 'getRstList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetRstList);
}

//처리결과확인 목록 가져오기 완료
function successGetRstList(data) {
	resultGridData = data;
	resultGrid.setData(resultGridData);
	resultGrid.repaint();
	
	if(resultGridData.length > 0) {
		$('#cboPrcSys').trigger('change');
	}
}

function getProgList(){

	$('[data-ax5grid="reqGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	data =  new Object();
	data = {
		AcptNo			: pReqNo,
		UserId			: pUserId,
		chkYn			: "0",
		requestType		: 'getProgList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json', successGetProgList);
}

//신청정보가져오기 완료
function successGetReqList(data) {
	reqInfoData = data;
//	console.log(reqInfoData[0]);
	if ( reqInfoData.length > 0 ) {
		
		$("#txtAcptNo").val(reqInfoData[0].acptno);
		$("#txtSyscd").val(reqInfoData[0].cm_sysmsg);
		$("#txtEditor").val(reqInfoData[0].cm_username);
		$("#txtUpdt").val(reqInfoData[0].cm_username);
		$("#txtAcptDate").val(reqInfoData[0].acptdate);
		$("#txtStatus").val(reqInfoData[0].cr_confname);
		
		if (reqInfoData[0].ermsg != null && reqInfoData[0].ermsg != "") {
			//$("#lblErMsg").text("오류메시지 : " + reqInfoData[0].ermsg);
			//$("#lblErMsg").show();
		}
		if (reqInfoData[0].file == "1") $("#btnTestDoc").show();
		if (reqInfoData[0].prcdate != null && reqInfoData[0].prcdate != "") {
			$("#txtPrcDate").val(reqInfoData[0].prcdate);
			$('#btnAllCncl').prop("disabled", true);
		}   
		if (reqInfoData[0].cr_passcd != null && reqInfoData[0].cr_passcd != "")
		   $("#txtSayu").val(reqInfoData[0].cr_passcd);

		if (reqInfoData[0].log == "1") $("#btnLog").prop("disabled",false);
		
		//신청미완료건 결재자 여부확인
		if (reqInfoData[0].endsw == '0') {
			data =  new Object();
			data = {
				AcptNo     	: pReqNo,
				UserId     	: pUserId,
				requestType	: 'gyulChk'
			}
			ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json', successGyulChk);
		} else { //신청완료 건
			aftChk();
		}
	}
}

//결재자 여부확인완료
function successGyulChk(data) {
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}

	if (data == "0") {
		$("#btnApproval").prop("disabled",false);
		if (reqInfoData[0].prcsw == "0") {
		   $("#btnCncl").prop("disabled",false);
		} else {
		   $("#btnCncl").prop("disabled",true);
		}
		$("#divConf").show();
	}else if (data != "1") {
		dialog.alert("결재정보 체크 중 오류가 발생하였습니다.");
	}
	
	aftChk();
}
function aftChk() {
	if (reqInfoData[0].signteamcd == "1") {
   		if (reqInfoData[0].prcsw == "0" && reqInfoData[0].signteam.substr(0,3) == "SYS") {
   		   if (isAdmin || reqInfoData[0].cr_editor == pUserId) {
   		      //$("#btnRetry").prop("disabled",false);
   		      if (reqInfoData[0].errtry == "1") $("#btnErrRetry").prop("disabled",false);
   		      //else if (reqInfoData[0].sttry == "1") $("#btnSttry").prop("disabled",false);
   		   }
   		   
   		   if(isAdmin) $("#btnStepEnd").prop("disabled",false);
   		}
	}

   	if (reqInfoData[0].cr_status != "3" &&
	   (reqInfoData[0].cr_editor = pUserId || isAdmin) &&
	    reqInfoData[0].prcdate != null && reqInfoData[0].prcdate != "") {
   			//updtYn();
	}

	if($activeTab == "tab1Li"){
		getProgList();
	} else {
		getRstList();
	}
}

function updtYn() {
	data = new Object();
	data = {
		AcptNo : pReqNo,
		requestType : 'updtYn'
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json',successUpdtYn);	
}

function successUpdtYn(data) {
	cboEditorData = data;
	if (cboEditorData.length > 0) {
		$("#lbUpdt").show();
		$("#cboEditor").show();
		$("#cboEditor").ax5select("enable");
		$("#btnUpdt").show();
		
		$('[data-ax5select="cboEditor"]').ax5select({
			options: injectCboDataToArr(cboEditorData, 'cm_userid' , 'cm_username')
		});
	} else {
		$("#cboEditor").ax5select("disable");
		$("#btnUpdt").prop("disabled", true);
	}
}

function updtEditor() {
	if(getSelectedIndex('cboEditor') < 1) {
		dialog.alert("변경신청자를 선택하여 주십시오.");
		return;
	}
	
	data = new Object();
	data = {
		AcptNo 	 : pReqNo,
		editUser : getSelectedVal('cboEditor').cm_userid,
		UserId 	 : pUserId,
		requestType : 'updtEditor'
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json',successUpdtEditor);	
}

function successUpdtEditor(data) {
	if (data == "0") {
		dialog.alert("신청자 수정을 완료하였습니다.");
		return;
	} else {
		dialog.alert("신청자 수정 중 오류가 발생하였습니다.");
	}
}

//새창팝업
function openWindow(type,acptNo, etcInfo) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+pReqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_pop_'+pReqCd;

    nWidth  = 1046;
	nHeight = 700;
    if (type === 'PROGINFO') {//프로그램정보
	    nHeight = 750;
		cURL = "/webPage/winpop/PopProgramInfo.jsp";
	} else if (type === 'RESULTVIEW') {//처리결과확인
		cURL = "/webPage/winpop/PopPrcResultLog.jsp";
	} else if (type === 'PROGDETAIL') {//프로그램 상세보기
		nHeight = 500;
		cURL = "/webPage/winpop/PopProgDetailCkOut.jsp";
	} else if (type === 'SRCVIEW') {//소스보기
		nWidth = 1200;
		cURL = "/webPage/winpop/PopSourceView.jsp";
	} else if (type === 'SRCDIFF') {//소스비교
		cURL = "/webPage/winpop/PopSourceDiff.jsp";
	} else if (type === 'LOGVIEW') {//로그확인
		cURL = "/webPage/winpop/PopServerLog.jsp";
	} else if (type === 'APPROVAL') {//결재정보
		nHeight = 828;
		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	} else if (type === 'RECHKOUT') {//재신청
		cURL = "/webPage/dev/CheckOut.jsp";
	} else {
		confirmDialog2.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
	
	var f = document.setReqData;
    f.user.value 	= pUserId;
    f.rgtList.value = rgtList;
    f.adminYN.value = isAdmin?"Y":"N";
	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	if (acptNo != '' && acptNo != null) {
		f.acptno.value	= acptNo;
	} else {
		f.acptno.value	= pReqNo;
	}
	
	if (type === 'RECHKOUT') { //재신청
		f.winSw.value = 'true';
	}
	
	if (etcInfo != '' && etcInfo != null) {
		if (type === 'RESULTVIEW') { //처리결과확인
			f.seqno.value = etcInfo;
		} else if (type === 'PROGINFO' || type === 'SRCVIEW' || type === 'SRCDIFF' || type === 'PROGDETAIL') { //프로그램정보, 스크립트확인, 소스보기, 소스비교
			f.itemid.value = etcInfo;
			f.syscd.value = reqInfoData[0].cr_syscd;
			if(type === 'PROGINFO') {
				f.sysmsg.value = reqInfoData[0].cm_sysmsg;
				f.jobcd.value = reqGrid.getList("selected")[0].cr_jobcd;
				f.rsrcname.value = reqGrid.getList("selected")[0].cr_rsrcname;
				f.rsrccd.value = reqGrid.getList("selected")[0].cr_rsrccd;
				f.dirpath.value = reqGrid.getList("selected")[0].cm_dirpath;
			}
			//f.orderId.value = pReqNo;
		}else {
			f.etcinfo.value = etcInfo;
		} 
	}
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    
}

function svrFileMake() {
	data = new Object();
	data = {
		AcptNo : pReqNo,
		requestType : 'svrFileMake'
	}
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', ajaxData, 'json', successSvrFileMake);
}

function successSvrFileMake(data) {
	if (data == "0") {
		var ajaxData = {
			inAcptNo	: pReqNo,
			fileGb		: "G",
			requestType : "getProgFileList"
		}
		ajaxAsync('/webPage/ecmr/Cmr0100Servlet', ajaxData, 'json', successGetProgFileList);
	} else {
		dialog.alert(data + " [조치 후 체크아웃 상세화면에서 재처리하시기 바랍니다.]");
	}
}

function getProgFileList(){
	var ajaxData = {
		inAcptNo	: pReqNo,
		fileGb		: "F",
		requestType : "getProgFileList"
	}
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', ajaxData, 'json', successGetProgFileList);
}

function successGetProgFileList(data){
	if (data.length == 0) {
		dialog.alert("대상파일이 없습니다.");
		$('#btnQry').trigger('click');
	} else {
		if (data[0].cr_rsrcname == "ERROR") {
			dialog.alert("결재처리 중 오류가 발생하였습니다.");
			return;
		} else {
			selGridData = data;
			setTimeout(function() {
				localFileSendRecvModal.open({
					width: 1000,
					height: 482,
					iframe: {
						method: "get",
						url: "../modal/request/LocalFileSendRecv.jsp",
						param: "callBack=requestSayuModal"
					},
					onStateChanged: function () {
						if (this.state === "open") {
							mask.open();
						}
						else if (this.state === "close") {
							mask.close();
						}
					}
				}, function () {
					
					
				});
			}, 200);
		}
	}
}

function delReq(item){
	var ajaxData = {
		AcptNo		: pReqNo,
		ItemId		: item.cr_baseitem,
		SignTeam	: reqInfoData[0].signteam,
		ReqCd		: pReqNo.substr(4,2),
		requestType : "delReq"
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', ajaxData, 'json', successDelReq);
}

function successDelReq(data){

	if (data == "0") {
		dialog.alert("삭제처리가 완료되었습니다.");
		return
	}else if (data == "2") {
		dialog.alert("현재 서버에서 다른처리를 진행 중입니다. 잠시 후 다시 처리하여 주시기 바랍니다.");
	}else  {
		dialog.alert("삭제처리 중 오류가 발생하였습니다.");
	}
}