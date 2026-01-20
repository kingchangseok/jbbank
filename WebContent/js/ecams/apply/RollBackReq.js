/**
 * [적용 > 롤백요청] 화면
 */
var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;
var rgtList = window.top.rgtList;

var firstGrid	   = new ax5.ui.grid();
var secondGrid	   = new ax5.ui.grid();

var requestListModal  	= new ax5.ui.modal();
var dirSelectSvrModal	= new ax5.ui.modal();
var ChkOutVerSelModal 	= new ax5.ui.modal();//이전버전선택 모달

var firstGridData  = []; //롤백가능 운영요청건 데이타
var secondGridData = []; //추가대상그리드 데이타
var cboSysCdData   = []; //시스템 데이타

/* 결재절차확정모달 관련 변수 */
var approvalModal 	= new ax5.ui.modal();
var confirmData 	= [];	//from modal
var confirmInfoData = null;	//to modal

var optGbn         = '';
var myWin 		   = null;
var data           = null;
var ajaxReturnData = null;

var acptNo		   = ""; //롤백신청번호
var ingSw          = false;

var modalCloseFlag = false;
var selSysCd       = null;
var selRsrcCd      = null;
var selDsnCd       = null;
var selDirPath     = null;
var selSysMsg      = null;
var selProg        = null;
var befAcptNo      = null;
var befSrNo        = null;
var confirmInfoData= new Object();

$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	
	        if (this.colIndex == 3 && optGbn == 'V' && this.item.selected_flag != '1' ) {//버전선택
	        	this.self.clearSelect();
	        	this.self.select(this.dindex);
	        	
	        	var gridSelIdx = this.dindex;
	        	
	        	pItemId = this.item.cr_itemid;
	        	
	    		setTimeout(function() {

	    			modalCloseFlag = false;
	    			ChkOutVerSelModal.open({
	    				width: 650,
	    				height: 500,
	    				iframe: {
	    					method: "get",
	    					url: "../modal/request/ChkOutVerSelModal.jsp"
	    				},
	    				onStateChanged: function () {
	    					if (this.state === "open") {
	    						mask.open();
	    					}
	    					else if (this.state === "close") {
	    						console.log(gridSelIdx, updateFlag, selectVer, selectAcptno, selectViewVer);
	    						
	    						if (updateFlag) {
	    							firstGrid.setValue(gridSelIdx, 'cr_acptno', selectAcptno);
	    							firstGrid.setValue(gridSelIdx, 'cr_version', selectVer);
	    							firstGrid.setValue(gridSelIdx, 'cr_befver', selectVer);
	    							firstGrid.setValue(gridSelIdx, 'cr_realbefver', selectVer);
	    						}
	    						mask.close();
	    						firstGrid.repaint();
	    					}
	    				}
	    			}, function () {
	    				
	    			});
	    		}, 200);
	        } else {
	        	this.self.select(this.dindex);
	        }
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	addDataRow();
        },
    	trStyleClass: function () {
    		var findSw = false;
    		if(this.item.selected_flag == '1' || this.item.cr_status != '0' || this.item.ermsg != '정상'){
    			return 'fontStyle-cncl';
    		} else return '';
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
            {type: 1, label: '추가'}
        ],
        popupFilter: function (item, param) {
        	if(firstGrid.getList('selected').length < 1){
        		return false;
        	}
         	return true;
       	 
        },
        onClick: function (item, param) {
	        addDataRow();
	        
	        firstGrid.contextMenu.close();//또는 return true;        	
        }
    },
    columns: [
        {key: "cm_dirpath",  label: "프로그램경로",		width: '20%', align: 'left'},
        {key: "cr_rsrcname", label: "프로그램명",		width: '30%', align: 'left'},
        {key: "jawon",       label: "프로그램종류",		width: '12%', align: 'left'},
        {key: 'cr_version',	 label: '버전',  		width: '8%',
        	formatter: function() {
        		if (optGbn != 'V') {
        			return '<label>'+ this.item.cr_version +'</label>';
        		} else {
        			if (this.item.cr_version == 'sel') {
        				return '<button style="width: 98%; height: 98%;">버전선택</button>';
        			} else {
        				return '<button style="width: 98%; height: 98%;">버전: '+ this.value +'</button>';
        			}
        		}
        	}
        },
        {key: "cr_lastdate", label: "수정일", 		width: '6%'},
        {key: "ermsg",       label: "체크결과",		width: '10%', align: 'left'},
        {key: "codename",    label: "상태", 		 	width: '10%', align: 'left'} 
    ]
});


secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    showLineNumber: true,
    header: {
        align: "center",
        selector: false
    },
    body: {
        onClick: function () {
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	deleteDataRow();
        },
    	trStyleClass: function () {
    		if (this.item.cr_itemid != this.item.baseitem){
    			return "fontStyle-module";
    		} else if (this.item.selected_flag == '1'){
    			return "fontStyle-notaccess";
    		} 
    	},
    	onDataChanged: function(){
    		//console.log(this);
    		if (this.item.selected_flag == '1') {
            	this.self.clearSelect(this.dindex);
        		return;
        	}
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
            {type: 1, label: '제거'}
        ],
        popupFilter: function (item, param) {
        	if(secondGrid.getList('selected').length < 1){
        		return false;
        	}
         	return true;
       	 
        },
        onClick: function (item, param) {
        	deleteDataRow();
	        
	        firstGrid.contextMenu.close();//또는 return true;
        	
        }
    },
    columns: [
        {key: "cm_dirpath",    label: "프로그램경로",	width: '22%', 	align: 'left'},
        {key: "cr_rsrcname",   label: "프로그램명",    width: '22%', 	align: 'left'},
        {key: "cr_story",      label: "프로그램한글명",	width: '14%', 	align: 'left'},
        {key: "cr_version",    label: "버전",			width: '5%'},
        {key: "cr_lastdate",   label: "최종수정일",	width: '10%'},
        {key: "jawon",         label: "프로그램종류",	width: '12%', 	align: 'left'},
        {key: "ermsg",         label: "체크결과",		width: '10%', 	align: 'left'},
    ]
});

$(document).ready(function(){
	if (reqCd == null || reqCd == '' || reqCd == undefined) {
		dialog.alert('정당하지 않은 접근입니다.');
		return;
	}
	if (userId == null || userId == '' || userId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	screenInit('M');
	
	//조회버튼 클릭
	$('#btnQry').bind('click',function(){
		if (getSelectedIndex('cboSysCd') < 0) {
			dialog.alert('시스템을 선택하시기 바랍니다.');
			return;
		}
		selRollbackModalOpen();
	});
	
	// 시스템콤보 변경
	$('#cboSysCd').bind('change', function() {
		changeSysCd();
	});
	
	// 신청건,이전버전 라디오 클릭
	$('input:radio[name^="radio"]').bind('click', function() {
		changeGbn();
	});
		
	$('#btnAdd').bind('click',function(){
		addDataRow();
	});
	
	$('#btnDel').bind('click',function(){
		deleteDataRow();
	});	
	
	//신청버튼 클릭
	$('#btnReq').bind('click',function() {
		btnReq_click();
	});
	
	//프로그램명 검색
	$('#txtProg').bind('keypress', function(event){
		if(event.keyCode==13) {
			event.keyCode = 0;
			$('#btnSearch').trigger('click');
		}
	});
	
	//프로그램조회버튼 클릭
	$('#btnSearch').bind('click',function(){
		if (getSelectedIndex('cboSysCd') < 0) {
			dialog.alert('시스템을 선택하시기 바랍니다.');
			return;
		}
		selProg = $('#txtProg').val().trim();
		selRsrcCd = '';
		selDsnCd = '';
		selDirPath = '';
		
		getFileList();
	});
	
	//시스템정보 가져오기
	getSysIfo();
	
});
function screenInit(gbn) {
	
	$('#txtDir').val('');
	$('#txtProg').val('');
	$('#txtSayu').val('');
	
	$('#btnReq').prop('disabled',true);
	$('#btnQry').prop('disabled',true);
	$('#optReq').wCheck('check',true);
	
	changeGbn();
	
	if (gbn == 'S') {
		firstGridData = [];
		firstGrid.setData([]);
		
		secondGridData = [];
		secondGrid.setData([]);
		
		$('[data-ax5select="cboSysCd"]').ax5select('enable');
		$('#btnQry').prop('disabled', false);
		$('#btnReq').prop('disabled', true);
		$('#optReq').wCheck('disabled', false);
		$('#optVer').wCheck('disabled', false);
		$('#btnDel').prop('disabled',true);
	}
}
//시스템정보 가져오기
function getSysIfo() {
	data =  new Object();
	data = {
		UserId			: userId,
		ReqCd			: reqCd,
		SecuYn			: 'Y',
		SelMsg			: '',
		CloseYn			: 'N',
		requestType		: 'getSysInfo'			
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}
//시스템정보 가져오기완료
function successGetSysInfo(data) {
	
	if (data == null || data == '' || data == undefined || data.length == 0) {
		dialog.alert("롤백 대상 시스템이 없습니다.");
		return;
	}
	if (typeof(data) == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	
	cboSysCdData = data;
	$('[data-ax5select="cboSysCd"]').ax5select({
	      options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if (cboSysCdData.length > 0) {
		for(var i=0; i<cboSysCdData.length; i++) {
			if(cboSysCdData[i].cm_syscd == '01200') {
				$('[data-ax5select="cboSysCd"]').ax5select('setValue',cboSysCdData[i].cm_syscd,true);
				break;
			}
		}
		
		changeSysCd();
	}
}
function changeSysCd() {
	
	screenInit('S');
	
	if (getSelectedIndex('cboSysCd')<0) return;
	
	if (getSelectedVal('cboSysCd').cm_sysinfo.substr(4,1) == '1' && getSelectedVal('cboSysCd').cm_stopsw == '1') {
		dialog.alert('이관통제을 위하여 일시적으로 형상관리 사용을 중지합니다.');
		$('#btnQry').prop('disabled',true);
	} else $('#btnQry').prop('disabled',false);
	
}
function changeGbn() {
	optGbn = $('input[name="radio"]:checked').val();
	if($('#optReq').is(':checked')) {
		$('#btnQry').text('롤백대상신청건조회');
		$('#txtDir').css('visibility','hidden');
		$('#btnSearch').prop('disabled',true);
		$('#txtProg').prop('disabled',true);
		$('#txtProg').val('');
	} else {
		$('#btnQry').text('디렉토리검색');
		$('#txtDir').css('visibility','visible');
		$('#btnSearch').prop('disabled',false);
		$('#txtProg').prop('disabled',false);
	}
}
function selRollbackModalOpen() {
	
	modalCloseFlag = false;
	
	optGbn = $('input[name="radio"]:checked').val();
	console.log('selRollbackModalOpen='+optGbn);
	
	selSysCd = getSelectedVal('cboSysCd').value;
	selSysMsg = getSelectedVal('cboSysCd').text;
	befAcptNo = '';
	selDsnCd = '';
	selDirPath = '';
	selProg = '';
	selRsrcCd = '';
	
	if (optGbn == 'L') {  //신청건 롤백
		setTimeout(function() {			
			requestListModal.open({
				width: 1100,
				height: 500,
				defaultSize: false,
				iframe: {
					method: "get",
					url: "../modal/rollback/RequestListModal.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						
						if (modalCloseFlag) {
							if (befAcptNo != null && befAcptNo != '' && befAcptNo != undefined) getReqFileList();
						}
					}
				}
			}, function () {
			});
		}, 200);
	} else {              //이전버전 롤백		
		selSysCd = getSelectedVal('cboSysCd').value;
		selSysMsg = getSelectedVal('cboSysCd').text;
		setTimeout(function() {
			dirSelectSvrModal.open({
				width: 800,
				height: 520,
				defaultSize: false,
				iframe: {
					method: "get",
					url: "../modal/rollback/DirSelectSvrModal.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						
						if (modalCloseFlag) {
							$('#txtDir').val(selDirPath);
							getFileList();
						}
					}
				}
			}, function () {});
		}, 200);
	}
}

//운영배포신청목록조회
function getReqFileList(){
	firstGridData = [];
	secondGridData = [];
	
	firstGrid.setData([]);
	secondGrid.setData([]);
	
	ingSw = true;
	data =  new Object();
	data = {
			 UserId	: userId,
			 AcptNo	: befAcptNo,
		       SrNo	: befSrNo,
		requestType	: 'getBefReq_Prog'
	}
	
	console.log('[getBefReq_Prog] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr0600Servlet', data, 'json', successGetBefReq_Prog);
}
//신청프로그램목록조회 완료
function successGetBefReq_Prog(data){
	console.log('[successGetBefReq_Prog] ==>', data);
	
	ingSw = false;
	firstGridData = data;
	firstGrid.setData(firstGridData);
	firstGrid.setColumnSort({cr_rsrcname:{seq:0, orderBy:'desc'}});
	
	if (firstGridData.length == 0) {
		dialog.alert("대상프로그램이 존재하지 않습니다.");
		return;
	}
	for (var i=0;firstGridData.length>i;i++) {
		if (firstGridData[i].version > 0 && firstGridData[i].selected_flag == '1') {
			dialog.alert('체크아웃 중인 프로그램이 있습니다. 체크아웃 취소 후 처리하시기 바랍니다.')
			return;
		}
	}
	
	if (firstGridData.length>0) {
		firstGrid.selectAll({selected:true, filter:function(){			
				return this['selected_flag'] != '1';			
			}
		});
		addDataRow();
	}
}

//프로그램목록조회
function getFileList(){
	firstGridData = [];
	secondGridData = [];
	
	firstGrid.setData([]);
	secondGrid.setData([]);
	
	ingSw = true;
	var tmpInfo = new Object();
	tmpInfo.userid = userId;
	tmpInfo.syscd = getSelectedVal('cboSysCd').value;
	if (selDsnCd == null || selDsnCd == '' || selDsnCd == undefined) tmpInfo.dirpath = selDirPath;
	else tmpInfo.dsncd = selDsnCd;
	tmpInfo.rsrcname = selProg;
	tmpInfo.reqcd = reqCd;
	tmpInfo.rsrccd = selRsrcCd;
	
	data =  new Object();
	data = {
			etcData	: tmpInfo,
		requestType	: 'getFileList'
	}
	console.log('getFileList',data);
	ajaxAsync('/webPage/ecmr/Cmr0600Servlet', data, 'json', successGetFileList);
	
}
function successGetFileList(data) {
	console.log('successGetFileList',data);
	firstGridData = data;
	firstGrid.setData(firstGridData);
	firstGrid.setColumnSort({cr_rsrcname:{seq:0, orderBy:'desc'}});
	if (firstGridData.length == 0) {
		dialog.alert("대상프로그램이 존재하지 않습니다.");
		return;
	}
	
}
function addDataRow() {
	
	optGbn = $('input[name="radio"]:checked').val();
	var firstGridSeleted = firstGrid.getList("selected");
	if (firstGridSeleted.length == 0) {
		//dialog.alert('롤백할 파일을 선택한 후 진행하시기 바랍니다.')
		return;
	}
	
	var calSw = false;
	var shSw = false;
	var pgSw = false;
	var findSw = false;
	var i = 0;
	var j = 0;
	var vercnt = 0;
	var calcnt = 0;
	
	console.log('1.',firstGridSeleted);
	for (i=0;firstGridSeleted.length>i;i++) {
		findSw = false;
		console.log('3.',firstGridSeleted[i]);
		if (firstGridSeleted[i].selected_flag == '1' || firstGridSeleted[i].cr_status != '0') {
			findSw = true;
		} else if (firstGridSeleted[i].cr_version == '0') findSw = true;
		else {
			if (secondGridData.length>0) {
				for (j=0;secondGridData.length>j;j++) {
					if (secondGridData[j].cr_itemid == secondGridData[j].baseitem) {
						if (firstGridSeleted[i].cr_itemid == secondGridData[j].cr_itemid) {
							findSw = true;
							break;
						}
					}
				}
			}
		}
		if (findSw) {
			firstGridSeleted.splice(i,1);
			i--;
		} else {
			if (firstGridSeleted[i].cr_version == 'sel') ++vercnt;
			if (firstGridSeleted[i].cm_info.substr(3,1) == '1' || firstGridSeleted[i].cm_info.substr(8,1) == '1' 
					|| firstGridSeleted[i].cm_info.substr(26,1) == '1' || Number(firstGridSeleted[i].itemCnt) > 0 ) ++calcnt;
		} 
	}
	
	if (firstGridSeleted.length == 0) {
		//dialog.alert('신청 할 목록을 정확히 선택한 후 진행하여 주시기 바랍니다.');
		return;
	}
	
	if (vercnt>0) {
		dialog.alert('버전을 선택하여 주시기 바랍니다.');
		return;
	}
	
	console.log('calcnt ==> ' + calcnt);
	if (firstGridSeleted.length>0 && calcnt>0) {
		var tmpInfo = new Object();
		tmpInfo.ReqCD = reqCd;
		tmpInfo.cm_syscd = getSelectedVal('cboSysCd').value;
		tmpInfo.userid = userId;
		tmpInfo.QryCd = '';
		tmpInfo.QryName = '';
		tmpInfo.qrygbn = optGbn;
		
		data = new Object();
		data = {
			etcData : tmpInfo,
		    fileList : firstGridSeleted,
		    requestType : 'getDownFileList'
		}
		ajaxAsync('/webPage/ecmr/Cmr0600Servlet', data, 'json', successGetDownFileList);
		
	} else if (firstGridSeleted.length>0) {
		chk_duplicate(firstGridSeleted);
	}
}

//상세프로그램항목 가져오기완료
function successGetDownFileList(data){
	console.log('[successGetDownFileList] ==>', data);
	chk_duplicate(data);
}

function chk_duplicate(gridDataList) {
	var i = 0;
	var j = 0;
	var findSw = false;
	
	if (secondGridData.length>0) {
		for (i=0;secondGridData.length>i;i++) {
			if (secondGridData[i].cr_itemid == secondGridData[i].baseitem) {
				for (j=0;gridDataList.length>j;j++) {
					if (secondGridData[i].cr_itemid == gridDataList[j].baseitem) {
						gridDataList.splice(j,1);
						j--;
					}
				}
			}
		}
	}
	
	if (gridDataList.length > 0) {
		secondGrid.addRow(gridDataList);

		for (i=0;gridDataList.length>i;i++) {
			if (gridDataList[i].baseitem == gridDataList[i].cr_itemid) {				
				for (j=0;firstGrid.list.length>j;j++) {
					if (gridDataList[i].cr_itemid == firstGrid.list[j].cr_itemid) {
						firstGrid.list[j].selected_flag = '1';
						firstGrid.list[j].__disable_selection__ = true;
						break;
					}
				}
			}
		}
	}
	firstGrid.clearSelect();	// 상위 그리드에 있는 데이터가 하단 그리드에 추가되면 상단 그리드에서 선택했던 체크박스 초기화
	firstGrid.repaint();
	secondGrid.repaint();
	secondGridData = clone(secondGrid.list);
	
	if (secondGridData.length>0) {
		$('[data-ax5select="cboSysCd"]').ax5select('disable');
		$('#btnQry').prop('disabled', true);
		$('#btnReq').prop('disabled', false);
		$('#optReq').wCheck('disabled', true);
		$('#optVer').wCheck('disabled', true);
		$('#btnDel').prop('disabled',false);
	}
}

function deleteDataRow() {
	var secondGridSeleted = secondGrid.getList("selected");

	secondGridData = clone(secondGrid.list);
	
	if (secondGridSeleted.length == 0) return;
	var selLen = secondGridSeleted.length;
	var grd1Len = firstGridData.length;
	var grd2Len = secondGridData.length;
	var i = 0;
	var j = 0;
	
	//상단그리드에서 제거
	for (i=0;selLen>i;i++) {
		for (j=0;firstGrid.list.length>j;j++) {
			if (secondGridSeleted[i].baseitem == firstGrid.list[j].baseitem) {
				firstGrid.list[j].__disable_selection__ = '';
				firstGrid.list[j].selected_flag = "0";
				break;
			}
		}
	}
	
	//하단그리드에서 제거
	for (i=0;selLen>i;i++) {
		for (j=0;grd2Len>j;j++) {
			if (secondGridSeleted[i].baseitem == secondGridData[j].baseitem) {
				secondGridData.splice(j,1);
				j--;
				grd2Len = secondGridData.length;
			}
		}
	}
	
	firstGrid.repaint();
	secondGrid.setData(secondGridData);
	secondGrid.repaint();
	
	if (secondGridData.length == 0) {
		$('[data-ax5select="cboSysCd"]').ax5select('enable');
		$('#btnQry').prop('disabled', false);
		$('#btnReq').prop('disabled', true);
		$('#optReq').wCheck('disabled', false);
		$('#optVer').wCheck('disabled', false);
		$('#btnDel').prop('disabled',true);
	}
}

//신청가능대상 자동선택
function secondGridDefaultCheck(gridDataList) {
	for(var i=0; i<gridDataList.length; i++) {
		if (gridDataList[i].selected_flag == '0') {
			secondGrid.select(i);
			$('#btnReq').prop('disabled',false);
		}
	}
}

//롤백신청
function btnReq_click() {
	optGbn = $('input[name="radio"]:checked').val();
	
	$('#txtSayu').val($('#txtSayu').val().trim());
	
	if ( $('#txtSayu').val().length == 0 ) {
		dialog.alert('신청사유를 입력하시기 바랍니다.');
		return;
	}
	if (getSelectedIndex('cboSysCd') < 0) {
		dialog.alert('시스템을 선택하여 주시기 바랍니다.');
		return;
	}
	secondGridData = clone(secondGrid.list);
	if (secondGridData.length == 0) {
		dialog.alert('롤백대상 프로그램을 목록에서 선택하시기 바랍니다.');
		return;
	}
	
	var j = 0;
	var tmpRsrc = '';
	var findSw = false;
	for (var i=0;secondGridData.length>i;i++) {
		if (secondGridData[i].ermsg != '정상' && secondGridData[i].cr_version != '0') {
			dialog.alert('체크결과 원복할 수 없는 파일이 있습니다. 제외한 후 신청하시기 바랍니다.');
			findSw = true;
			break;
		}
		if (secondGridData[i].cr_itemid == secondGridData[i].baseitem) {
			for (j=i+1;secondGridData.length>j;j++) {
				if (secondGridData[i].cr_itemid == secondGridData[j].cr_itemid) {
					dialog.alert('동일한 프로그램이 중복으로 요청되었습니다. 조정한 후 다시 신청하시기 바랍니다. ['+ secondGridData[i].cr_rsrcname+']');
					findSw = true;
					break;
				}
			}
		}
		if (findSw) break;
		
		if (tmpRsrc.length > 0) {
			if (tmpRsrc.indexOf(secondGridData[i].cr_rsrccd) < 0)
	            tmpRsrc = tmpRsrc + "," + secondGridData[i].cr_rsrccd;
		} else tmpRsrc = secondGridData[i].cr_rsrccd;
	}
	
	if (findSw) return;
	
	var tmpData = {
			  SysCd : getSelectedVal('cboSysCd').value,
			  ReqCd : reqCd,
		     RsrcCd : tmpRsrc,
			 UserId : userId,
			  QryCd : '',
		requestType : 'confSelect'
	}	
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) {
		dialog.alert('결재정보 확인 중 오류가 발생하였습니다.');
		ingSw = false;
		return;
	}
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>=0) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}
	
	if (ajaxReturnData == 'C') {
		confirmDialog.setConfig({
	    	title: "결재지정확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			if (this.key === 'ok') {
				confCall('Y', tmpRsrc);
			} else {
				confCall('N', tmpRsrc);
			}
		});
	} else if (ajaxReturnData == 'Y') {
		confCall('Y', tmpRsrc);
    } else if (ajaxReturnData != 'N') {
    	dialog.alert('결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.');
    } else {
		confCall('N', tmpRsrc);
    }
}

function confCall(GbnCd, tmpRsrc){
	confirmInfoData = new Object();
	confirmInfoData.UserId = userId;
	confirmInfoData.ReqCd  = reqCd;
	confirmInfoData.SysCd  = getSelectedVal('cboSysCd').value;
	confirmInfoData.RsrcCd = tmpRsrc;
	confirmInfoData.QryCd = '';
	confirmInfoData.EmgSw = 'Y';
	confirmInfoData.JobCd = '';
	confirmInfoData.PrjNo = '';
	confirmInfoData.deployCd = 'N';
	confirmInfoData.passok = '';
	confirmInfoData.gyulcheck = '00';
	
	confirmData = [];
	if (GbnCd == 'Y') {
		setTimeout(function() {
			approvalModal.open({
				width: 850,
		        height: 550,
		        iframe: {
		            method: "get",
		            url: "../modal/request/ApprovalModal.jsp"
			    },
		        onStateChanged: function () {
		            if (this.state === 'open') {
		                mask.open();
		            }
		            else if (this.state === 'close') {
		            	if(confirmData.length > 0){
		            		reqQuestConf();
		            	}else ingSw = false;
		                mask.close();
		            }
		        }
			});
		}, 200);
	} else if (GbnCd == 'N') {
		ajaxReturnData = null;
		
		var data = {
			UserId : userId,
			SysCd : getSelectedVal('cboSysCd').cm_syscd,
			ReqCd : reqCd,
			RsrcCd : tmpRsrc,
			PgmType : '',
			EmgSw : '0',
			PrjNo : '',	
			deployCd : '',
			QryCd : '',
			passok : '',
			gyulcheck : '00'
		}
		
		var tmpData = {
			paramMap	: data,
			requestType : 'Confirm_Info'
		}
		
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json');
		if (ajaxReturnData != undefined && ajaxReturnData != null) {
			if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
				dialog.alert(ajaxReturnData);
			} else {
				confirmData = ajaxReturnData;
				for(var i=0; i<confirmData.length ; i++){
					if (confirmData[i].arysv[0].SvUser == null || confirmData[i].arysv[0].SvUser == "" || confirmData[i].arysv[0].SvUser == undefined) {
						confirmData.splice(i,1);
						i--;
					}
				}
				reqQuestConf();
			}
		}
	}
}

function reqQuestConf(){
	ajaxReturnData = null;
	
	var etcObj = new Object();
	etcObj.UserID 	= userId;
	etcObj.ReqCD  	= reqCd;
	etcObj.Sayu	  	= $('#txtSayu').val().trim();
	etcObj.cm_syscd = getSelectedVal('cboSysCd').value;
	etcObj.cm_sysgb = getSelectedVal('cboSysCd').cm_sysgb;
	etcObj.passok	= '2';
	etcObj.passcd 	= $('#txtSayu').val().trim();
	etcObj.EmgCd  	= '0';
	etcObj.AplyDate = '';
	etcObj.Deploy 	= '';
	
	data = {
		  chkInList	: secondGridData,
		   ConfList : confirmData,
	        etcData	: etcObj,
	         confFg : 'Y',
		requestType	: 'request_Check_In'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0600Servlet', data, 'json');
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) {
		dialog.alert('Roll-Back신청 중 오류가 발생하였습니다.');
		ingSw = false;
		return;
	}
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>=0) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}
	acptNo = ajaxReturnData;
	
	requestEnd(acptNo);
}
function requestEnd(acptNo){
	mask.open();
	confirmDialog.confirm({
		msg: '롤백신청완료! 상세 정보를 확인하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			openWindow(acptNo);
		}
	});
	mask.close();
	
	screenInit('S');
}
function openWindow(acptNo) {
	var nHeight, nWidth, cURL, winName;
	
	if (myWin != null) {
        if (!myWin.closed) {
        	myWin.close();
        }
	}

    winName = 'rollBackrequestDetail';

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= acptNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.adminYN.value = adminYN;
    f.rgtList.value = rgtList;
    
	nHeight = 740;
    nWidth  = 1200;
	cURL = "/webPage/winpop/PopRequestDetail.jsp";

	myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}