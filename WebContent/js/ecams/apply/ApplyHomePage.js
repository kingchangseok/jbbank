/**
 * 홈페이지적용
 * 
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-08-00
 * 
 */
var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;
var tmplatePath = window.top.attPath;

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var fileGrid		= new ax5.ui.grid();

var cboSysData		= [];
var firstGridData 	= [];
var secondGridData 	= [];
var fileGridData	= [];

var attPath			= '';
var tmpPath			= '';
var uploadJspFile 	= '';
var ingSw 			= false;
var exlSw 			= false; //엑셀첨부 flag
var strAcptNo		= "";    //신청번호

/* 실행모듈연결정보 모달 관련 변수 */
var progModal 	= new ax5.ui.modal();
var modalObj	= new Object();

/* 결재절차확정모달 관련 변수 */
var approvalModal 	= new ax5.ui.modal();
var confirmData 	= [];	//from modal
var confirmInfoData = null;	//to modal

/* 파일첨부 변수 */
var fileUploadModal 	= new ax5.ui.modal();
var fileIndex			= 0;
var TotalFileSize 		= 0;

/* 파일업로드 변수 (ComFileUpload에서 사용) */
var fileGbn 			= 'U';
var dirGbn 				= '22';
var subDocPath 			= '';
var upFiles 			= [];
var	popCloseFlag 		= false;
var completeReadyFunc	= false;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	addDataRow();
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    	trStyleClass: function () {
    		if (this.item.selected_flag === '1'){
    			return "fontStyle-cncl";
    		}
   			if (this.item.errmsg != null && this.item.errmsg != ""){
   				if (this.item.errmsg != "정상") {
   	    			return "fontStyle-cncl";
   				}
   			}
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
            {type: 1, label: "추가"},
            {type: 2, label: "실행모듈연결정보"}
        ],
        popupFilter: function (item, param) {
         	firstGrid.clearSelect();
         	firstGrid.select(Number(param.dindex));
         	
         	var selIn = firstGrid.selectedDataIndexs;
        	if(selIn.length === 0) return;
         	
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	var retType = '1';
        	if(param.item.cm_ifno.substr(8,1) == '1') retType = retType+'2';
        	
        	if (retType == '') return false;
		    var retString;
		    
		    if (retType.indexOf('1')>-1){
		    	retString = (item.type == 1);
		    }
		    if (retType.indexOf('2')>-1){
		    	if (retType == '') retString = (item.type == 2);
		    	else retString = retString | (item.type == 2);
		    }
       	 
        },
        onClick: function (item, param) {
        	if(item.type == '1') {
        		addDataRow();
        	}else if(item.type == '2') {
        		openProgModal(param.item);
        	}
	        firstGrid.contextMenu.close();//또는 return true;
        }
    },
    columns:  [
    	{key: "rsrcname",  	 	label: "프로그램명", 	width: '20%',  	align: 'left'},
        {key: "errmsg",			label: "체크결과",		width: '15%',	align: 'left'},
        {key: "dirpath",   		label: "프로그램경로",	width: '25%',  	align: 'left'},
        {key: "jobcd",   		label: "업무",		width: '20%',  	align: 'left'},
        {key: "jawon",    		label: "프로그램종류",	width: '10%',  	align: 'left'},
    	{key: "userid",    		label: "신청자",		width: '10%',  	align: 'center'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
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
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
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
            {type: 1, label: "제거"},
            {type: 2, label: "실행모듈연결정보"}
        ],
        popupFilter: function (item, param) {
        	secondGrid.clearSelect();
        	secondGrid.select(Number(param.dindex));
        	var selIn = reqGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	var retType = '1';
        	if(param.item.cm_ifno.substr(8,1) == '1') retType = retType+'2';
        	
        	if (retType == '') return false;
		    var retString;
		    
		    if (retType.indexOf('1')>-1){
		    	retString = (item.type == 1);
		    }
		    if (retType.indexOf('2')>-1){
		    	if (retType == '') retString = (item.type == 2);
		    	else retString = retString | (item.type == 2);
		    }
        },
        onClick: function (item, param) {
        	if(item.type == '1') {
        		deleteDataRow();
        	}else if(item.type == '2') {
        		openProgModal(param.item);
        	}
	        secondGrid.contextMenu.close();//또는 return true;
        }
    },
    columns:  [
        {key: "cr_rsrcname",   			label: "프로그램명",		width: '20%',  	align: 'left'},
    	{key: "dealcode",    			label: "거래코드",			width: '15%',  	align: 'left'},
    	{key: "sayu",    				label: "변경사유",			width: '15%',  	align: 'left'},
    	{key: "importancecodecodename", label: "중요도",			width: '15%',  	align: 'left'},
    	{key: "testynname",    			label: "테스트대상",		width: '10%',  	align: 'left'},
        {key: "newgoodscodecodename",   label: "신상품/공통여부", 	width: '15%',	align: 'left'},
        {key: "prcseq",       			label: "우선순위", 		width: '10%',	align: 'center'},
    ]
});

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
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    },
    columns: [
    	{key: "cc_attfile",   label: "파일명",	width: '100%',  align: 'left'}
    ]
});

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	if (reqCd == null || reqCd == '' || reqCd == undefined) {
		dialog.alert('정당하지 않은 접근입니다.');
		return;
	}
	if (userId == null || userId == '' || userId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	
	$('#txtCompDate').prop("disabled", true);
	$('#btnDate').prop("disabled", true);
	$('#txtTime').prop("disabled", true);
	
	// 시스템
	$('#cboSys').bind('change',function(){
		changeSys();
	});
	
	// 추가
	$('#btnAdd').bind('click',function(){
		addDataRow();
	});
	
	// 삭제
	$('#btnDel').bind('click',function(){
		deleteDataRow();
	});
	
	// 신청
	$('#cmdReq').bind('click',function(){
		cmdReqClick();
	});
	
	// 엑셀로드
	$('#btnExcel').bind('click',function(){
		$('#excelFile').trigger('click');
	});
	
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
	
	// 엑셀템플릿
	$('#btnTemp').bind('click',function(){
		fileDown(tmplatePath+'/excelTemplet.xls', 'excelTemplet.xls');
	});
	
	//파일첨부
	$('#btnFileAdd').bind('click', function() {
		btnFileAddClick();
	});
	
	//파일삭제
	$('#btnFileDel').bind('click', function() {
		btnFileDelClick();
	});
	
	getCAMSDir();
	getSysCbo();
});

function getSysCbo() {
	var sysListInfoData = {
			UserId	: userId,
			SecuYn  : "Y",
			SelMsg  : "",
			CloseYn : "N",
			ReqCd   :  reqCd,
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', sysListInfoData, 'json',successGetSysCbo);
}

function successGetSysCbo(data) {
	if(data.length == 0) {
		dialog.alert('권한이 있는 시스템 중 홈페이지적용 하실 수 있는 시스템이 없습니다. 관리자에게 문의해주세요.');
		$('#btnExcel').prop('disabled', true);
		return;
	}
	
	$('#btnExcel').prop('disabled', false);
	
	cboSysData = data;
	cboSysData = cboSysData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	});
	
   	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
   	});
   	
   	if (cboSysData.length>0) {
   		$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[0].cm_syscd,true);
   		for (var i=0;cboSysData.length>i;i++) {
			if (cboSysData[i].setyn == "Y") {
				$('[data-ax5select="cboSys"]').ax5select("setValue", cboSysData[i].cm_syscd);
				break;
			}
		}
   		
   		changeSys();
   	}
}

function changeSys() {
	firstGrid.setData([]);
	firstGridData = [];
	
	secondGrid.setData([]);
	secondGridData = [];
}

//신청목록추가
function addDataRow() {
	firstGridSelected = firstGrid.getList("selected");
	if (firstGridSelected.length == 0) {
		dialog.alert('추가할 항목을 선택해주십시오.');
		return;
	}
	
	if($('#txtSayu').val().trim().length < 1) {
		dialog.alert('사유를 입력하여 주시기 바랍니다.');
		return;
	}
	
	var secondGridList = new Array;
	var findSw = false;
	var i = 0;
	var j = 0;
	var befckoutcnt = 0;
	var strRsrcName = '';
	
	for (i=0;firstGridSelected.length>i;i++) {
		findSw = false;
		if (firstGridSelected[i].selected_flag == '1') {
			findSw = true;
		} 
		
		if (!findSw) {
			for (j=0;secondGridData.length>j;j++) {
				if (firstGridSelected[i].rsrcname == secondGridData[j].rsrcname) {
					findSw = true;
					break;
				}
			}
		}
		
		if (findSw) {
			firstGridSelected.splice(i,1);
			i--;
		}
	}

	if ((secondGridData.length + firstGridSelected.length) > 1000){
		dialog.alert("1000건 이하로 신청하여 주시기 바랍니다.");
		return;
	}
	
	var modSw = false;
	$(firstGridSelected).each(function(i){
		if(this.selected_flag == "1") return true;

		if (exlSw && this.errmsg != "정상" && alertFlag == 2) {
			return true;
		}
		
		if (exlSw && this.errmsg != "정상" && this.errmsg != "파일중복" && alertFlag != 2) {
			return true;
		}
		
		if (!exlSw && !modSw) {
			if(this.cm_info != null && this.cm_info != undefined && this.cm_info != '') {
				if (this.cm_info.substr(8,1) == "1") modSw = true;
			}
		}
		
		if(this.selected_flag != "1"){
			this.selected_flag = "1";
			this.cr_rsrcname = this.rsrcname;
			this.cr_rsrccd = this._rsrccd;
			this.cm_dirpath = this.dirpath;
			this.cr_jobcd = this._jobcd;
			this.sayu = $('#txtSayu').val();
            this.cr_sayu = $('#txtSayu').val();
            this.prcseq = '201';
            this.cr_syscd = getSelectedVal('cboSys').cm_syscd;
            this.sysgb = getSelectedVal('cboSys').cm_sysgb;
            this.reqcd = reqCd;
            this.AplyDate = '';
            
			secondGridList.push($.extend({}, this, {__index: undefined}));
		}
	});

	if (secondGridList.length != 0){
		if (modSw) {
		} else {
			downList_Set(secondGridList);
		}
	}
}

function downList_Set(data) {
	firstGrid.clearSelect();
	checkDuplication(data);
}

function checkDuplication(downFileList) {
	var secondGridList = new Array;
	var i = 0;
	var j = 0;
	var findSw = false;
	var chkSw = false;
	var diffSw = false;
	var diffSw2 = false;
	var totCnt = secondGridData.length;
	
	if (totCnt > 0) {
		for(i=0; downFileList.length>i ; i++){
			findSw = false;
			totCnt = secondGridData.length;
			for (j=0;totCnt>j;j++) {
				if (downFileList[i].rsrcname == secondGridData[j].rsrcname) {
					findSw = true;
					break;
				}
			}
			if (!findSw) {
				var copyData = clone(downFileList[i]); //리스트의 주소지를 가져오므로 clone 을 해서 add 해줘야함
				secondGridList.push($.extend({}, copyData, {__index: undefined}));
				copyData.__index = secondGridData.length;
				secondGridData.push(copyData);
			}
		}
	} else {
		secondGridList = clone(downFileList);
		secondGridData = clone(downFileList);
	}
	if(downFileList.length > 0) {
		$(downFileList).each(function(i){
			var currentItem = downFileList[i];
						
			for (j=0;firstGridData.length>j;j++) {
				if(firstGrid.list[j].rsrcname == currentItem.rsrcname) {
					firstGrid.list[j].selected_flag = '1';
					firstGrid.list[j].selected = '1';
					firstGrid.list[j].enabled = '0';
					firstGrid.list[j].__disable_selection__ = true;
					break;
				}
			}
		});
	}

	firstGrid.repaint();
	secondGrid.addRow(secondGridList);
	secondGrid.repaint();
	
	exlSw = false;
	$('#cmdReq').prop('disabled',true);
	if(secondGrid.list.length > 0){
		$('#cmdReq').prop('disabled',false);
		$('[data-ax5select="cboSys"]').ax5select('disable');
	}
}

//신청목록에서 제거
function deleteDataRow() {
	var secondGridSeleted = secondGrid.getList("selected");

	$(secondGridSeleted).each(function(i){
		// 체크인 데이터 삭제
		$(secondGridData).each(function(z){
			if(this.rsrcname == secondGridSeleted[i].rsrcname){
				secondGridData.splice(z,1);
			}
		});

		// 상위 그리드 표시 해제
		$(firstGrid.list).each(function(j){
			if((firstGrid.list[j].rsrcname == secondGridSeleted[i].rsrcname)){

				firstGrid.list[j].__disable_selection__ = '';
				firstGrid.list[j].selected_flag = "0";
				return false;
			}
		});

	});
	secondGrid.removeRow("selected");
	firstGrid.repaint();
	
	if (secondGrid.list.length == 0){
		$('[data-ax5select="cboSys"]').ax5select("enable");
		$('#cmdReq').prop('disabled',true);
	} else {		
		$('#cmdReq').prop('disabled',false);
	}
}

function cmdReqClick() {
	if (ingSw) {
		dialog.alert("현재 신청하신 건을 처리 중입니다. 잠시 기다려 주시기 바랍니다.");
		return;
	}
	
	if (Number($('#txtDevTime').val()) < 1){
		dialog.alert("개발기간을 확인하십시오.");
		ingSw = false;
		$('#txtDevTime').focus();
		return;
	}

	if (secondGrid.list.length == 0) {
		dialog.alert('신청 할 파일을 추가하여 주십시오.');
		ingSw = false;
		return;
	}
	
	for (var x=0;x<secondGridData.length;x++) {
		if (secondGridData[x].prcseq == "") {
			dialog.alert("우선순위를 입력한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
			ingSw = false;
			return;
		}
		
		for (var y=x+1;y<secondGridData.length;y++) {
			if (secondGridData[x].cr_itemid != null && secondGridData[x].cr_itemid != "") {
				if (secondGridData[x].cr_itemid == secondGridData[y].cr_itemid) {
					dialog.alert("동일한 프로그램이 중복으로 요청되었습니다. 조정한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
					ingSw = false;
					return;
				}
			} else {
				if (secondGridData[x].cr_syscd == secondGridData[y].cr_syscd &&
					secondGridData[x].cr_dsncd == secondGridData[y].cr_dsncd &&
					secondGridData[x].cr_rsrcname == secondGridData[y].cr_rsrcname) {
					dialog.alert("동일한 프로그램이 중복으로 요청되었습니다. 조정한 후 다시 신청하십시오. ["+secondGridData[x].cr_rsrcname+"]");
					ingSw = false;
					return;
				}
			}
		}
	}
	
	cmdReqSub();
}

function cmdReqSub(){
	
	ingSw = true;
	var tmpData = {
			  SysCd : getSelectedVal('cboSys').cm_syscd,
			  ReqCd : reqCd,
		     RsrcCd : '',
			 UserId : userId,
			  QryCd : '',
		requestType : 'confSelect'
	}	
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	if (ajaxReturnData != undefined && ajaxReturnData != null) {
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
			ingSw = false;
			return;
		}
	}
	
	if (ajaxReturnData == "C") {
	    confirmDialog.setConfig({
	    	title: "결재지정확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				confCall("Y");
			} else {
				confCall("N");
			}
		});
	} else if (ajaxReturnData == "Y") {
		confCall("Y");
    } else if (ajaxReturnData != "N") {
    	dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
		ingSw = false;
    } else {
		confCall("N");
    }
}

function confCall(GbnCd){
	var strQry = '';
	var emgSw = "N";
	var tmpRsrc = '';
	var tmpJobCd = '';
	var tmpPrjNo = '';
	
	tmpRsrc = '';
	strQry = '';
	for(var x=0;x<secondGridData.length;x++) {
		if (tmpRsrc.length > 0) {
			if (tmpRsrc.indexOf(secondGridData[x].cr_rsrccd) < 0)
	            tmpRsrc = tmpRsrc + "," + secondGridData[x].cr_rsrccd;
		} else tmpRsrc = secondGridData[x].cr_rsrccd;
	
		if (strQry.length > 0) {
			if (strQry.indexOf(secondGridData[x].reqcd) < 0) {
				strQry = strQry + "," + secondGridData[x].reqcd;
			}
		} else strQry = secondGridData[x].reqcd;
	}
	
	confirmInfoData = new Object();
	confirmInfoData.UserId = userId;
	confirmInfoData.ReqCd = reqCd;
	confirmInfoData.SysCd = getSelectedVal('cboSys').cm_syscd;
	confirmInfoData.RsrcCd = tmpRsrc;
	confirmInfoData.QryCd = strQry;
	confirmInfoData.EmgSw = emgSw;
	confirmInfoData.JobCd = tmpJobCd;
	confirmInfoData.deployCd = 'N';
	confirmInfoData.PrjNo = tmpPrjNo;
	if($('#chkEmg').is(':checked')) confirmInfoData.passok = 'pass';
	else confirmInfoData.passok = '';
	confirmInfoData.gyulcheck = '';
	
	confirmData = [];	
	if (GbnCd == "Y") {
		setTimeout(function() {
			approvalModal.open({
		        width: 850,
		        height: 550,
		        iframe: {
		            method: "get",
		            url: "../modal/request/ApprovalModal.jsp"
			    },
		        onStateChanged: function () {
		            if (this.state === "open") {
		                mask.open();
		            }
		            else if (this.state === "close") {
		            	if(confirmData.length > 0){
		            		reqQuestConf();
		            	} else ingSw = false;
		                mask.close();
		            }
		        }
			});
		}, 200);	
	} else if (GbnCd == "N") {
		var data = {
			UserId : userId,
			SysCd : getSelectedVal('cboSys').cm_syscd,
			ReqCd : reqCd,
			RsrcCd : tmpRsrc,
			PgmType : '',
			EmgSw : emgSw,
			PrjNo : tmpPrjNo,	
			deployCd : 'N',
			QryCd : strQry,
			passok : '',
			gyulcheck : getSelectedVal('cboGyulCheck').cm_micode,
		}
		
		var tmpData = {
			paramMap	: data,
			requestType : 'Confirm_Info'
		}
		
		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json');
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

function reqQuestConf( ){
	var requestData = new Object();
	requestData.UserID = userId;
	requestData.ReqCD = reqCd;
	requestData.Sayu = $('#txtSayu').val().trim();
	requestData.PassCd = '';
	requestData.cr_devptime  = $('#txtDevTime').val().trim();
	requestData.EmgCd = '0';
	requestData.TstSw = getSelectedVal('cboSys').TstSw;
	if($('#chkEmg').is(':checked')) requestData.Deploy = '1';
	else requestData.Deploy = '0';
	requestData.upload = "N";
	
	var tmpData = {
		chkInList	: secondGridData,
		etcData 	: requestData,
		befJob		: [],	
		ConfList 	: confirmData,
		TestList 	: [],
		OrderList 	: [] ,
		confFg 		: 'Y',
		requestType : 'request_homep_Check_In'
	}
	
	console.log('[request_homep_Check_In] ==>', tmpData);
	var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
	
	if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) {
		dialog.alert('[체크인] 중 오류가 발생하였습니다. [request_homep_Check_In]');
		ingSw = false;
		return;
	}
	if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
		dialog.alert(ajaxReturnData);
		ingSw = false;
		return;
	}
	if(ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR'){
		dialog.alert(ajaxReturnData.substr(5));
		ingSw = false;
		return;
	}
	
	strAcptNo = ajaxReturnData;
	
	if(fileGridData.length > 0){
		startFileupload();
	} else{
		requestEnd();
	}
}

function requestEnd() {
	ingSw = false;
    
    dialog.alert('체크인 신청완료!',function(){});
    
    strAcptNo = '';
	uploadCk = false;
	upFiles = [];
	firstGrid.setData([]);
	firstGridData = [];
	secondGrid.setData([]);
	secondGridData = [];
	fileGridData = [];
	fileGrid.setData([]);
	confirmData = [];
	confirmInfoData = null;
	$('txtDevTime').val('0');
	$('txtSayu').val('');
	$('#cmdReq').prop('disabled',true);
	
	$('[data-ax5select="cboSys"]').ax5select('enable');
    changeSys();
}

function openWindow(type,param) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   	//폼 name
    f.user.value = userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 'DETAIL') {		//신청상세
        f.acptno.value = param;
	    cURL = "/webPage/winpop/PopRequestDetail.jsp";
	}else {
		dialog.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}

function openProgModal(item) {
	if(item.itemid == null || item.itemid == '' || item.itemid == undefined) {
		dialog.alert('프로그램정보가 누락되었습니다. [eCAMS 관리자 문의]');
		return;
	}
	
	modalObj = new Object();
	modalObj.syscd = getSelectedVal('cboSys').cm_syscd;
	modalObj.userid = userId;
	modalObj.sysmsg = getSelectedVal('cboSys').cm_sysmsg;
	modalObj.rsrcname = item.cr_rsrcname;
	modalObj.itemid = item.cr_itemid;
	modalObj.path = item.cm_dirpath;
	
	setTimeout(function() {
		progModal.open({
			width: 1000,
			height: 750,
			iframe: {
				method: "get",
				url: "../modal/request/ProgramInfo_relat.jsp"
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

//경로 가져오기
function getCAMSDir() {
	var tmpInfoData = {
		pCode : '99,F2,22',
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}

function successeCAMSDir(data) {
	data = data.filter(function(data) {
		if(data.cm_pathcd == '99') tmpPath = data.cm_path;				//엑셀 파일 업로드시 파일 올릴 경로 
		else if(data.cm_pathcd == 'F2') uploadJspFile = data.cm_path;	//업로드할때 사용할 JSP 파일명 가져오기
		else if(data.cm_pathcd == '22') attPath = data.cm_path;			//첨부파일 올릴 경로
		else downURL = data.cm_path;
	});
}

//엑셀 파일 업로드시 파일타입 체크
function fileTypeCheck(obj) {
	var pathpoint = obj.value.lastIndexOf('.');
	var filepoint = obj.value.substring(pathpoint+1,obj.length);
	filetype = filepoint.toLowerCase();
	if(filetype=='xls' || filetype=='xlsx') {
		startUpload();
	} else {
		dialog.alert('엑셀 파일만 업로드 가능합니다.', function() {});
		parentObj  = obj.parentNode
		node = parentObj.replaceChild(obj.cloneNode(true),obj);
		return false;
	}
}

//엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;

	//tmpPath = 'C:\\html5\\temp\\'; //테스트중
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();
	formData.append('fullName',tmpPath+userId+"_excel_ApplyHomePage.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);

    $.ajax({
		url: homePath+'/webPage/fileupload/'+uploadJspFile,
        type:'POST',
        data:formData,
        async:false,
        cache:false,
        contentType:false,
        processData:false
    }).done(function(response){
    	onUploadCompleteData(response);
    }).fail(function(xhr,status,errorThrown){
    	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
    }).always(function(){
    	// file 초기화
    	var agent = navigator.userAgent.toLowerCase();
    	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
    	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
    	} else {
    	    $("#excelFile").val("");
    	}
    });
}

//엑셀파일 완료 후
function onUploadCompleteData(filePath){
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");

	headerDef.push("sysmsg");
	headerDef.push("jobcd");
	headerDef.push("userid");
	headerDef.push("rsrcname");
	headerDef.push("dirpath");
	headerDef.push("jawon");
	
	tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

//읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			$(".loding-div").remove();
			dialog.alert(data);
			return;
		}
	}

	var findSw = false;
	firstGridData = data;
	//firstGridData.splice(0,1);
	firstGrid.setData(firstGridData);

	if (firstGridData.length == 0){
		$(".loding-div").remove();
		$('#cmdReq').prop('disabled', true);
		return;
	}
	if (firstGridData[0].sysmsg != getSelectedVal('cboSys').cm_sysmsg) {
		$(".loding-div").remove();
		dialog.alert("시스템명이 선택한 시스템명과 일치하지 않습니다.");
		return;
	}

	firstGrid.setData(firstGridData);
	getFileListExcel();
}

//세팅한 정보 유효성 검사
function getFileListExcel() {
	var dataObj = new Object();
	dataObj.cm_syscd = getSelectedVal('cboSys').cm_syscd;
	dataObj.cm_sysmsg = getSelectedVal('cboSys').cm_sysmsg;

	var tmpData = {
		fileList 	: firstGridData,
		dataObj		: dataObj,
		requestType	: 'getFileListIn_excel'
	}
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json',successGetFileListExcel);
}

//세팅한 정보 유효성 검사 완료
function successGetFileListExcel(data) {
	$(".loding-div").remove();
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	firstGridData = data;
	
	var errSw = false;
	for(var i=0; i<firstGridData.length; i++) {
		if(firstGridData[i].errsw === '1') {
			dialog.alert('입력체크 중 오류가 발생한 항목이 있습니다. 확인하여 조치 후 다시 처리하시기 바랍니다.',  function() {});
			errSw = true;
			break;
		}
	}
	
	$('#cmdReq').prop('disabled', errSw);
	firstGrid.setData(firstGridData);
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
	}
}

function startFileupload() {
	var data = new Object();
	data = {
		ReqId : strAcptNo,
		ReqCd : reqCd,
		requestType	: 'maxFileSeq'
	}
	var fileseq = ajaxCallWithJson('/webPage/ecmc/Cmc0010Servlet', data, 'json');
	if(Number(fileseq) < 1) {
		dialog.alert('파일첨부 일련번호 추출에 실패하였습니다.');
		return;
	}
	
	fileGbn = 'U';
	dirGbn = '22';
	popCloseFlag = false;
	
	attPath = attPath + '/' + strAcptNo;
	//attPath = 'C:\\html5\\temp\\'; //테스트중
	subDocPath = strAcptNo.substr(0,4) + '/' + strAcptNo.substr(4,2);
	
	for(var i=0; i<fileGridData.length; i++) {
		var filenm = '';
		filenm = fillZero(4,fileseq+'');
		
		var tmpFile = new Object();
		tmpFile = clone(fileGridData[i]);
		tmpFile.savefile = strAcptNo.substr(0,4) + '/' + strAcptNo.substr(4,2) + '/1' +strAcptNo.substr(6)+filenm;
		tmpFile.saveFileName = '1'+strAcptNo.substr(6)+filenm;								//서버파일명
		tmpFile.fileseq = '' + fileseq;
		tmpFile.attfile = fileGridData[i].name;												//실제파일명
		tmpFile.fullName = attPath+'/'+strAcptNo.substr(0,4) + '/' + strAcptNo.substr(4,2);	//첨부경로
		tmpFile.fullpath = attPath;															//첨부경로(pathcd=22)
		
		upFiles.push(tmpFile);
		
		++fileseq;
	}
	
	//upFiles = clone(fileGridData);
	console.log('[ApplyHomePage.js] upFiles==>',upFiles);
	
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
}

// ComFileUpload 함수에서 호출
function setFile(fileList) {
	console.log('[RegistDevRequest.js] setFile ==>', fileList);
	
	fileList.forEach(function(item, Index) {
		item.acptno = strAcptNo;
		item.filegb = '1';
		item.realName = item.name;			//실제파일명
		item.saveName = item.savefile;		//YYYY/REQCD/1+신청번호.substr(6)+seqno(4자리)
		item.seq = item.fileseq;
	});
	
	var data = {
		fileList : fileList,
		requestType : 'setDocFile'
	}
	console.log('[ApplyHomePage.js] setDocFile ==>', data);
	ajaxAsync('/webPage/common/DocFileServlet', data, 'json', successSetDocFile);
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
	}else {
		requestEnd();
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
	fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
	fileGridData = fileGrid.getList();
}