var pReqNo  = null;
var pReqCd  = null;
var pUserId = null;
var reqCd   = null;
var rgtList = null;

var adminYN = window.top.adminYN;

var reqGrid     = new ax5.ui.grid();
var resultGrid  = new ax5.ui.grid();
var fileGrid	= new ax5.ui.grid();

var confirmDialog2 		= new ax5.ui.dialog();

var reqInfoData    		= []; //신청정보
var reqGridData    		= []; //체크인목록그리드 데이타
var resultGridData 		= []; //처리결과그리드 데이타
var fileGridData		= []; //첨부파일그리드
var cboPrcSysData  		= []; //배포구분

var attPath			= '';
var data           	= null; //json parameter
var myWin 		   	= null; //새창id
var ingSw          	= false;

//한번만 실행하도록 함
var gridSw1		  	= false;
var gridSw2		   	= false;
var gridSw3		   	= false;

var f = document.getReqData;
pReqNo = f.acptno.value;
pReqCd = pReqNo.substr(4,2);
reqCd = pReqCd;
pUserId = f.user.value;
rgtList = f.rgtList.value;

var $activeTab = "tab1Li";
var refreshCk = true;

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

$('#txtAcptNo').val(pReqNo.substr(0,4) + "-" + pReqNo.substr(4,2) + "-" + pReqNo.substr(6,6));
createViewGrid1();
createViewGrid2();
createViewGrid3();

$('[data-ax5select="cboPrcSys"]').ax5select({
    options: []
});

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
	        	//this.self.clearSelect();
	           this.self.select(this.dindex);
	        },
	        onDBLClick: function () {
	        	if (this.dindex < 0) return;
	
	        	if(this.item == null){
	        		return;
	        	}

		       	openWindow('RESULTVIEW', '', this.item.cr_baseitem);
	        },
	    	trStyleClass: function () {
				if(this.item.ColorSw == '3'){
					return "fontStyle-cncl";
				} else if(this.item.ColorSw == '5'){
					return "fontStyle-error";
				} else {
					if(this.item.cr_itemid != this.item.cr_baseitem) {
						return "fontStyle-module";
					}
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
	            {type: 1, label: "처리결과확인"},
	        ],
	        popupFilter: function (item, param) {
	        	reqGrid.clearSelect();
	        	reqGrid.select(Number(param.dindex));

	        	var selIn = reqGrid.selectedDataIndexs;
	        	if(selIn.length === 0) return;
	        	 
	         	if (param.item == undefined) return false;
	         	if (param.dindex < 0) return false;
	         	if (param.item.cr_baseitem == null || param.item.cr_baseitem == undefined) return false;
	         	
	         	if (param.item.rst == 'Y') return true;
	        },
	        onClick: function (item, param) {
	        	openWindow('RESULTVIEW', '', param.item.cr_baseitem);
	        	reqGrid.contextMenu.close();//또는 return true;
	        }
	    },
	    columns: [
	    	{key: "cr_rsrcname", 	label: "프로그램명",	width: '15%'},
			{key: "cr_editcon", 	label: "변경사유",		width: '20%'},
			{key: "cm_codename", 	label: "프로그램종류",	width: '10%'},
			{key: "cr_dsncd2", 		label: "프로그램경로",	width: '30%'},
			{key: "cm_jobname", 	label: "업무명",  	width: '15%'},
			{key: "priority", 		label: "우선순위",  	width: '10%',	align: "center"},
		]
	});
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
		contextMenu: {
	        iconWidth: 20,
	        acceleratorWidth: 100,
	        itemClickAndClose: false,
	        icons: {
	            'arrow': '<i class="fa fa-caret-right"></i>'
	        },
	        items: [
	            {type: 1, label: "결과확인"},
	        ],
	        popupFilter: function (item, param) {
	        	resultGrid.clearSelect();
	        	resultGrid.select(Number(param.dindex));

	        	var selIn = resultGrid.selectedDataIndexs;
	        	if(selIn.length === 0) return;
	        	 
	         	if (param.item == undefined) return false;
	         	if (param.dindex < 0) return false;
	         	
				return true;
	        },
	        onClick: function (item, param) {
	        	openWindow('RESULTVIEW', '', param.item.cr_seqno);
	        	resultGrid.contextMenu.close();//또는 return true;
	        }
	    },
		columns: [
			{key: "prcsys", 		label: "구분",  		width: '10%'},
			{key: "cr_rsrcname",	label: "프로그램명",  	width: '15%'},
			{key: "jawon", 			label: "프로그램종류", 	width: '15%'},
			{key: "cm_dirpath", 	label: "적용경로",  	width: '20%'},
			{key: "cr_svrname", 	label: "적용서버",  	width: '20%'},
			{key: "prcrst", 		label: "처리결과",  	width: '10%',	align: 'center'},
			{key: "prcdate", 		label: "처리일시",  	width: '10%', 	align: 'center'}
		]
	});
	
	getRstList();
};

function createViewGrid3() {
	fileGrid.setConfig({
	    target: $('[data-ax5grid="fileGrid"]'),
	    sortable: true, 		// 그리드 sort 가능 여부(true/false)
	    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
	    multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
	    showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
	    page: false,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	this.self.select(this.dindex);
	        },
	        onDBLClick: function () {
	        	if (this.dindex < 0) return;
	        	
	        	if(this.item.savename != null && this.item.savename != undefined && this.item.savename != '') {
	         		fileDown(attPath+'/'+this.item.savename, this.item.orgname);
	         	}
	        },
	    	onDataChanged: function(){
	    	    this.self.repaint();
	    	},
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
					 
	        	return false;
		        //return true;
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
	    	{key: "orgname",   label: "파일명",	width: '100%',  align: 'left'}
	    ]
	});
}

$(document).ready(function(){
	//상위 TITLE TEXT SET
	var reqCodes = getCodeInfoCommon([new CodeInfo('REQUEST','','N')]);
	var reqCodeDatas = reqCodes.REQUEST;
	
	var i=0;
	var contentHistory = '';
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
	if (pReqNo == null) {
		confirmDialog2.alert('신청정보가 없습니다.\n다시 로그인 하시기 바랍니다.');
		return;
	}
	
	setTabMenu();
	createViewGrid3();
	getCAMSDir();
	
	$('#tab1Li').width($('#tab1Li').width()+10);
	$('#tab2Li').width($('#tab2Li').width()+10);

	
	/**
	 * ------------------------------------------------------------------------------------------------------------------------------
	 *                                                        button click event
	 * ------------------------------------------------------------------------------------------------------------------------------
	 */
	
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
			$('#btnQry').trigger('click');
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
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		popClose()
	});
	
	//전체회수 클릭
	$('#btnAllCncl').bind('click', function() {
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
	
	//선택건회수 클릭
	$('#btnSelCncl').bind('click', function() {
		//사용안함
	});
	
	/**
	 * ------------------------------------------------------------------------------------------------------------------------------
	 *                                                     select box change event
	 * ------------------------------------------------------------------------------------------------------------------------------
	 */
	//배포구분 콤보선택
	$('#cboPrcSys').bind('change', function() {
		resultGrid.setData([]);
		
		if (resultGridData == null || resultGridData.length < 1) return;
		
		var selectedIndex = getSelectedIndex('cboPrcSys');
		if (selectedIndex > 0) {
			if (pReqCd == getSelectedVal('cboPrcSys').qrycd) {
				var selValue = getSelectedVal('cboPrcSys').value;
				var tmpResultGridData = [];
				resultGridData.forEach(function(lstData, Index) {
					if (selValue == 'SYSCB') {
						if(lstData.cr_prcsys == 'SYSCB' || lstData.cr_prcsys == 'SYSGB'){
							tmpResultGridData.push(lstData);
						}
					} else if (selValue == lstData.cr_prcsys) {
						tmpResultGridData.push(lstData);
					}
				});
				resultGrid.setData(tmpResultGridData);
			} else {
				resultGrid.setData([]);
			}
		} else {
			resultGrid.setData(resultGridData);
		}
		resultGrid.repaint();
	});
	
	setTimeout(function() {
		//최초 화면로딩 시 조회(새로고침버튼 로직)
		$('#btnQry').trigger('click');
	}, 20);
};

// 새로고침
function refresh() {
	detailCk = false;
	resetScreen();
	getPrcSysInfo();
	getFileList();
}

//활성화 비활성화 초기화로직
function resetScreen(){
    $("#lblErMsg").val('');
    $("#lblErMsg").hide();
    $("#divConf").hide();
    $("#btnSelCncl").hide();
    
	$("#btnRetry").prop("disabled",true);
	$("#btnAllCncl").prop("disabled",true);
	$("#btnLog").prop("disabled",true);
	
	/*$("#btnQry").prop("disabled",true);
	$("#btnApprovalInfo").prop("disabled",true);*/
	$("#btnApproval").prop("disabled",true);
	$("#btnCncl").prop("disabled",true);

	$("#txtSyscd").val("");
	$("#txtEditor").val("");
	$("#txtStatus").val("");
	$("#txtAcptDate").val("");
	$("#txtPrcDate").val("");
    $("#txtDeploy").val("");
    $("#txtDevp").val("");
    
    getReqInfo();
}

function setTabMenu(){
	$(".tab_content:first").show();

	if (!gridSw1) {
		createViewGrid1();
		gridSw1 = true;
	}
	
	if (!gridSw3) {
		createViewGrid3();
		gridSw3 = true;
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

function getFileList() {
	data =  new Object();
	data = {
		AcptNo		: pReqNo,
		GbnCd		: '1',
		requestType	: 'getFileList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0150Servlet', data, 'json', successGetFileList);
}

function successGetFileList(data) {
	fileGridData = data;
	fileGrid.setData(data);
}

//배포구분 가져오기
function getPrcSysInfo() {
	data =  new Object();
	data = {
		AcptNo		: pReqNo,
		requestType	: 'getPrcSys'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json', successGetPrcSys);
}

//배포구분 가져오기 완료
function successGetPrcSys(data) {
	cboPrcSysData = data;
	
	options = [];
	$.each(cboPrcSysData,function(key,value) {
	    options.push({value: value.cm_micode, text: value.cm_codename, qrycd: value.qrycd});
	});
	
	$('[data-ax5select="cboPrcSys"]').ax5select({
        options: options
	});
	
	if (cboPrcSysData.length > 0) {
		$('[data-ax5select="cboPrcSys"]').ax5select('setValue',cboPrcSysData[cboPrcSysData.length - 1].cm_micode,true);
		$('#cboPrcSys').trigger('change');		
	}
}

//경로 가져오기
function getCAMSDir() {
	var tmpInfoData = {
		pCode : '22',
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}

function successeCAMSDir(data) {
	data = data.filter(function(data) {
		if(data.cm_pathcd == '22') attPath = data.cm_path;
	});
}

//신청정보가져오기
function getReqInfo() {
	data =  new Object();
	data = {
		UserId			: pUserId,
		AcptNo			: pReqNo,
		requestType		: 'getReqList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0251Servlet', data, 'json',successGetReqList);
}

//신청정보가져오기 완료
function successGetReqList(data) {
	reqInfoData = data;

	if ( reqInfoData.length > 0 ) {
		data =  new Object();
		data = {
			UserId		: pUserId,
			AcptNo		: pReqNo,
			chkYn		: "0",
			qrySw		: false,
			requestType	: 'getProgList'
		}
		ajaxAsync('/webPage/ecmr/Cmr0251Servlet', data, 'json', successGetProgList);
		
		$('#txtSyscd').val(reqInfoData[0].cm_sysmsg);			//시스템
		$('#txtEditor').val(reqInfoData[0].cm_username);		//신청자
		$('#txtAcptDate').val(reqInfoData[0].acptdate);			//신청일시
		$('#txtDeploy').val(reqInfoData[0].reqpass);			//적용구분
		$('#txtDevp').val(reqInfoData[0].cr_devptime);			//개발기간
		
		if(reqInfoData[0].prcdate != null && reqInfoData[0].prcdate != undefined) {
			$('#txtPrcDate').val(reqInfoData[0].prcdate);		//완료일시
		}
		if(reqInfoData[0].confname != null && reqInfoData[0].confname != undefined) {
			$('#txtStatus').val(reqInfoData[0].confname);		//진행상태
		}
		if (reqInfoData[0].log == '1') {
			$('#btnLog').prop("disabled", false);				//로그확인
		}
		if(reqInfoData[0].ermsg != null && reqInfoData[0].ermsg != undefined) {
			$('#lblErrMsg').text(reqInfoData[0].ermsg);			//에러메시지
		}
		
		//신청미완료건 결재자 여부확인
		if (reqInfoData[0].endsw == '0') {
			data =  new Object();
			data = {
				AcptNo 		: pReqNo,
				UserId 		: pUserId,
				requestType	: 'gyulChk'
			}
			ajaxAsync('/webPage/ecmr/Cmr3100Servlet', data, 'json', successGyulChk);
			
		}else { //신청완료 건
			aftChk();
		}
	}
}

function getProgList(){
	$('[data-ax5grid="reqGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	data =  new Object();
	data = {
		UserId			: pUserId,
		AcptNo			: pReqNo,
		chkYn			: "0",
		qrySw			: detailCk, // 상세 데이터를 한번만 가져오도록
		requestType		: 'getProgList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0251Servlet', data, 'json', successGetProgList);
}

//체크인 목록가져오기 완료
function successGetProgList(data) {
	$(".loding-div").remove();
	reqGridData = data;
	reqGrid.setData(reqGridData);
	
	getRstList();
	
	for (var i=0; reqGridData.length>i ; i++) {
		tmpObj = {};
		tmpObj = reqGridData[i];
		if (tmpObj.check == 'true') {
			$("#btnSelCncl").show();
			$('#btnSelCncl').prop("disabled", false);	//선택건회수 활성화
			break;
		}
	}
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

//결재자 여부확인완료
function successGyulChk(data) {
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}

	console.log('successGyulChk==>',data);
	if (data == "0") {
		$("#btnApproval").prop("disabled",false);
	    $("#btnCncl").prop("disabled",false);
		$("#divConf").show();
	}else if (data == "2") {
		$("#lblErMsg").val('해당 발행번호로 체크인해야 할 프로그램이 동일한 단계까지 진행하지 않아 반려처리만 가능합니다.');
	    $("#lblErMsg").show();
	    $("#btnApproval").prop("disabled",true);
	    $("#btnCncl").prop("disabled",false);
	    $("#divConf").show();
	}else if (data != "1") {
		dialog.alert("결재정보 체크 중 오류가 발생하였습니다.");
	}
	
	aftChk();
}

function aftChk() {
	data =  new Object();
	data = {
		UserId		: pUserId,
		AcptNo		: pReqNo,
		requestType	: 'chkBtnEnabled'
	}
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0251Servlet', data, 'json');
	
	if(ajaxReturnData == null || ajaxReturnData.length < 1) {
		return;
	}
	
	$('#btnAllCncl').prop('disabled',!Boolean(ajaxReturnData[0].isBack));
	$('#btnRetry').prop('disabled',!Boolean(ajaxReturnData[0].isRetry))

	if($activeTab == "tab1Li"){
		getProgList();
	} else {
		getRstList();
	}
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
			popClose();
		});
	} else if (data == '2') {
		confirmDialog2.alert('현재 형상관리서버에서 다른처리를 진행하고 있습니다.\n잠시 후 다시 처리하여 주시기 바랍니다.');
	} else {
		confirmDialog2.alert('체크인 회수 처리가 지연되고 있습니다. 관리자에게 문의하시기 바랍니다.\n\n'+data);
	}
	$('#btnQry').trigger('click');
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
		popClose();
	}else{
		confirmDialog2.alert('처리에 실패했습니다. \n[ERROR='+data+']');
	}
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
	
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successSvrProc);
}

//자동처리 완료
function successSvrProc(data) {
	ingSw = false;

	if (data == '0') {
		if (reqInfoData[0].signteam == "SYSPDN" || reqInfoData[0].signteam == "SYSPUP") {
			//getProgFileList();
		} else {
			confirmDialog2.alert("재처리작업이 신청되었습니다. 잠시 후 다시받기를 하여 확인하여 주시기 바랍니다.");
			return;
		}
	}else if (data == '2') {
		confirmDialog2.alert("현재 서버에서 다른처리를 진행 중입니다. 잠시 후 다시 처리하여 주시기 바랍니다.");
		return;
	} else  {
		confirmDialog2.alert("재처리작업 신청 중 오류가 발생하였습니다.\n\n"+data);
		return;
	}
}

//새창팝업
function openWindow(type,acptNo, etcInfo) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_pop_'+pReqCd) == winName ) {
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
	} else if (type === 'SCRIPTVIEW') {//스크립트확인
		nHeight = 500;
		cURL = "/webPage/winpop/PopScript.jsp";
	} else if (type === 'SRCVIEW') {//소스보기
		nWidth = 1200;
		cURL = "/webPage/winpop/PopRequestSourceView.jsp";
	} else if (type === 'SRCDIFF') {//소스비교
		cURL = "/webPage/winpop/PopRequestSourceDiff.jsp";
	} else if (type === 'LOGVIEW') {//로그확인
		cURL = "/webPage/winpop/PopServerLog.jsp";
	} else if (type === 'APPROVAL') {//결재정보
		nHeight = 828;
		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	} else {
		confirmDialog2.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
	
	var f = document.setReqData;
    f.user.value 	= pUserId;
    f.rgtList.value = rgtList;
    f.adminYN.value = adminYN?"Y":"N";
	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	if (acptNo != '' && acptNo != null) {
		f.acptno.value	= acptNo;
	} else {
		f.acptno.value	= pReqNo;
	}
	
	if (etcInfo != '' && etcInfo != null) {
		if (type === 'RESULTVIEW') { //처리결과확인
			f.seqno.value = etcInfo;
		} else if (type === 'PROGINFO' || type === 'SCRIPTVIEW' || type === 'SRCVIEW' || type === 'SRCDIFF') { //프로그램정보, 스크립트확인, 소스보기, 소스비교
			f.itemid.value = etcInfo;
			f.syscd.value = reqInfoData[0].cr_syscd;
		}else {
			f.etcinfo.value = etcInfo;
		} 
	}
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    
}

function fileDelete() {
	var selItem = fileGrid.list[fileGrid.selectedDataIndexs[0]];
	if(selItem.cr_seqno != null && selItem.cr_seqno != undefined && selItem.cr_seqno != '') {
		fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
		fileGridData = fileGrid.getList();
		
		var data = new Object();
		data = {
			AcptNo : pReqNo,
			GuBun : '1',
			SeqNo : selItem.cr_seqno,
			requestType	: 'delDocFile'
		}
		var ajaxReturnData = ajaxCallWithJson('/webPage/common/DocFileServlet', data, 'json');
		if(ajaxReturnData != null && ajaxReturnData != undefined) {
			if(typeof ajaxReturnData == 'string' && ajaxReturnData.indexOf('ERROR') > -1) {
				dialog.alert(ajaxReturnData.substr(5));
				return;
			}
		}
		
		dialog.alert('첨부파일을 삭제했습니다.');
	}else {
		fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
		fileGridData = fileGrid.getList();
	}
}

function popClose() {
	try {
		if (window.opener.getRequestList != undefined){
			window.opener.getRequestList();
		}
		window.open("about:blank","_self").close();
	}catch(e) {
		window.open("about:blank","_self").close();
	}
}