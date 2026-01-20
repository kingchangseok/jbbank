/** 체크아웃취소 화면 기능
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */
var userName 	    = window.top.userName;
var userId 			= window.top.userId;
var adminYN		    = window.top.adminYN;
var userDeptName    = window.top.userDeptName;
var userDeptCd 	    = window.top.userDeptCd;
var reqCd 			= window.top.reqCd;
var rgtList         = window.top.rgtList;

var approvalModal 	= new ax5.ui.modal();

//grid 생성
var firstGrid 	 	= new ax5.ui.grid();
var secondGrid 		= new ax5.ui.grid();

var firstGridData 	= [];
var sysData 	  	= [];
var fSysData 		= [];
var gridSimpleData 	= [];
var secondGridData 	= [];
var confirmData 	= [];
var confirmInfoData = [];

var loadSw			= false;
var acptNo 			= "";
var myWin       	= null;

firstGrid.setConfig({
    target: $('[data-ax5grid="first-grid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: true, 
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect(); //기존선택된 row deselect 처리 (multipleSelect 할땐 제외해야함)
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	addDataRow();
        },
    	trStyleClass: function () {
    		if(this.item.selected_flag == '1' || this.item.error == 'Y'){
    			return 'fontStyle-cncl';
    		} else {
    			return '';
    		}
    	},
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
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
            {type: 1, label: "추가"}
        ],
        popupFilter: function (item, param) {
         	//firstGridData.clearSelect();
         	//firstGridData.select(Number(param.dindex));
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
        {key: 'acptdate', 	label: '신청일시',  	width: '10%'},
        {key: 'cm_username',label: '신청인',  	width: '5%'},
        {key: 'cm_dirpath', label: '프로그램경로',	width: '30%', align: 'left'},
        {key: 'cr_rsrcname',label: '프로그램명',  	width: '20%', align: 'left'},
        {key: 'cm_jobname', label: '업무명',  	width: '15%', align: 'left'},
        {key: 'jawon', 		label: '프로그램종류',	width: '10%', align: 'left'},
        {key: 'acptno', 	label: '신청번호',  	width: '10%'},
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="second-grid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: true,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect(); //기존선택된 row deselect 처리 (multipleSelect 할땐 제외해야함)
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	deleteDataRow();
        },
    	trStyleClass: function () {
    		if(this.item.baseitemid != this.item.cr_itemid){
    			return 'fontStyle-module';
    		} else {
    			return '';
    		}
    	},
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    		simpleData();
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
            {type: 1, label: "제거"}
        ],
        popupFilter: function (item, param) {
        	//secondGrid.clearSelect();
        	//secondGrid.select(Number(param.dindex));
        	if(secondGrid.getList('selected').length < 1){
        		return false;
        	}
         	return true;
        },
        onClick: function (item, param) {
	        deleteDataRow();
	        secondGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
    	{key: 'cm_username',	label: '신청인',  	width: '5%'},
        {key: 'cm_dirpath', 	label: '프로그램경로', 	width: '35%', align: 'left'},
        {key: 'cr_rsrcname', 	label: '프로그램명',  	width: '30%', align: 'left'},
        {key: 'cm_jobname', 	label: '업무명',  	width: '15%', align: 'left'},
        {key: 'jawon', 			label: '프로그램종류', 	width: '10%', align: 'left'}
    ]
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getSysCbo();
	screenInit();
	
	if (reqCd == "12") {
		$('#btnReq').text('테스트적용취소');
		$('#divDetail').empty();
		$('#divDetail').append('<input type="checkbox" class="checkbox-pie" id="chkDetail" data-label="테스트적용취소항목상세보기"></input>');
 	}
	
	$('#cboSys').bind('change',function(){
		changeSys();
	});
	
	$('#btnSearch').bind('click',function(){
		findProc();
	});
	
	$('#btnDel').bind('click',function(){
		deleteDataRow()
	});
	
	$('#btnAdd').bind('click',function(){
		addDataRow();
	});
	
	$('#btnReq').bind('click',function(){
		checkOutCnlClick();
	});
	
	$('#txtRsrcName').bind('keypress',function(event){
		if(event.keyCode == 13){
			findProc();
		}
	});
	
	$('#chkDetail').bind('click',function(){
		simpleData();
	});
	
	$('#btnExcelLoad').bind('click',function(){
		$('#excelFile').click();
	});
	
	//파일의 change 가 안먹히므로 html file onchange로 빠짐
	$('#excelFile').on('change',function(){
		//fileTypeCheck(this);
	});
});

function screenInit() {
	$('#btnReq').attr('disabled',true);
}

//SysInfo.getSysInfo(strUserId,"y","","n",strReqCD);
function getSysCbo(){
	var tmpData = {
		UserId : userId,
		SecuYn : 'y',
		SelMsg : '',
		CloseYn : 'n',
		ReqCd : reqCd,
		requestType : 'getSysInfo'
	}	
	
	ajaxAsync('/webPage/common/SysInfoServlet', tmpData, 'json',successGetSysCbo);
}

function successGetSysCbo(data){
	sysData = data;
	sysData = sysData.filter(function(item) {
		if(item.cm_sysinfo.substr(0,1) == "1") return false;
		else return true;
	})
	
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(sysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if (sysData.length > 0) {
		for (var i=0;sysData.length>i;i++) {
			if (sysData[i].setyn == "Y") {
				 $('[data-ax5select="cboSys"]').ax5select("setValue", sysData[i].cm_syscd, true);
				break;
			}
		}
	}
	
	 $('[data-ax5select="cboSys"]').trigger('change');
}

function changeSys(){
	var tmpObj = new Object();

	firstGrid.setData([]);
	firstGridaData = [];
	
	if(getSelectedIndex('cboSys') < 0) return;
	if(getSelectedVal('cboSys').cm_sysinfo.substr(3,1) == '0') {
		tmpObj.secuyn = "Y";
		tmpObj.qrygbn = "01";
		tmpObj.userid = userId;
		tmpObj.reqcd = reqCd;
		findProc();
	} else {
		findProc();
	} 
}

function findProc() {
	firstGrid.setData([]);
	firstGridData = [];
	
	if (getSelectedIndex('cboSys') < 0) return;

	var etcData = new Object();
	etcData.UserID = userId;
	etcData.syscd  = getSelectedVal('cboSys').cm_syscd;
	etcData.sysgb  = getSelectedVal('cboSys').cm_sysgb;
	etcData.reqcd  = reqCd;
	etcData.txtProg = $('#txtRsrcName').val().trim();
	
	if($("#chkMySelf").is(":checked")) etcData.Self = "Y";
	else etcData.Self = "N";
	
	var tmpData = {
		etcData : etcData,
		requestType : 'getFileList'
	}	
	$('[data-ax5grid="first-grid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0101Servlet', tmpData, 'json',successFindProc);
}

function successFindProc(data){
	console.log('[ChckOutCnl.js] successFindProc ==>', data);
	$(".loding-div").remove();
	if (data == undefined || data == null) return;
	if(data.length < 1) dialog.alert('검색결과가 없습니다.');
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
	} else {
		firstGridData = data;
		checkSelectedFlag();
	}
}

/* 체크아웃리스트에 추가된 데이터 파일리스트에서 선택표시 */
function checkSelectedFlag(){
	if(firstGridData.length >0){
		var i=0;
		var j=0;
		var fndItem=0;
		for (i=0;i<firstGridData.length;i++){
			fndItem=0;
			for (j=0;j<secondGridData.length;j++){
				if (firstGridData[i].cr_itemid == secondGridData[j].cr_itemid){
					fndItem++;
				}
			}
			if (fndItem > 0){
				firstGridData[i].__disable_selection__ = true;
				firstGridData[i].selected_flag = '1';
			}else{
				firstGridData[i].__disable_selection__ = '';
				firstGridData[i].selected_flag = '0';
			}
		}
		tmpobj1 = null;
		
	}
	
	firstGrid.setData(firstGridData);
	/*firstGrid.focus("HOME");
	secondGrid.focus("HOME");*/
}

//상세보기
function simpleData(){
	gridSimpleData = clone(secondGrid.list);
	if(secondGrid.list.length == 0){
		secondGridData = clone(secondGrid.list);
		return;
	}
	for(var i =0; i < gridSimpleData.length; i++){
		if(gridSimpleData[i].baseitemid != gridSimpleData[i].cr_itemid){
			gridSimpleData.splice(i,1);
			i--;
		}
	};
	if (!$('#chkDetail').is(':checked')){
		secondGrid.list = clone(gridSimpleData);
		secondGrid.repaint();
	}
	else{
		secondGrid.list = clone(secondGridData);
		secondGrid.repaint();
	}
}

function addDataRow() {
	var calcnt = 0;
	var secondGridList = new Array;
	var firstGridSeleted = firstGrid.getList("selected");
	var ajaxReturnData;
	
	$(firstGridSeleted).each(function(i){
		if(this.selected_flag == '1'){
			return true;
		}
		
		//RSCHKITEM	[27]-개발툴연계, [04]-동시적용항목CHECK, [47]-디렉토리기준관리, [09] 실행모듈Check
		if ((this.cm_info.substr(26,1) == "1" || this.cm_info.substr(3,1) == "1" || 
			 this.cm_info.substr(46,1) == "1" || this.cm_info.substr(8,1) == "1")){
			calcnt++;
		}
				
		if(this.selected_flag != '1'){
			this.selected_flag = '1';
			var copyData = this;
			secondGridList.push($.extend({}, copyData, {__index: undefined}));
		}
	});
	
	firstGrid.clearSelect();	// 상위 그리드에 있는 데이터가 하단 그리드에 추가되면 상단 그리드에서 선택했던 체크박스 초기화	
	
	if (calcnt > 0){
		if (secondGridList.length > 0){
			var tmpData = {
				fileList : secondGridList,
				requestType : 'getDownFileList'
			}
			console.log('[CheckOutCnl.js] getDownFileList==>', tmpData);
			ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0101Servlet', tmpData, 'json');
			
			if (ajaxReturnData == undefined || ajaxReturnData == null) return;

			var tmp = 0;
			//2022.09.22 flex 소스는 dbChk가 있지만 swf에는 없어서 주석처리
			/*for(var i = 0; i < ajaxReturnData.legnth; i++) {
				if(ajaxReturnData[i].status != "5") {
					dialog.alert("개발 형상관리상태가 체크아웃이 아닙니다[1]. ["+ajaxReturnData[i].cr_rsrcname+"]");
					tmp++;
					break;
				}
			}*/
			
			if(tmp == 0) {
				if (ajaxReturnData.indexOf('ERROR')>-1) {
					dialog.alert(ajaxReturnData);
				} else {
					checkDuplication(ajaxReturnData);
				}
			}
		}
	} else {
		for(var i=0;i<secondGridList.length; i++) {
			secondGridList[i].baseitemid = secondGridList[i].cr_itemid;
		}
		//dbChk(secondGridList);
		//2022.09.22 flex 소스는 dbChk가 있지만 swf에는 없어서 주석처리 
		checkDuplication(secondGridList);
		//checkSelectedFlag();
		if (secondGridData.length > 0){
			$('[data-ax5select="cboSys"]').ax5select("disable");
			$('#btnReq').attr('disabled',false);
		}
	}
}

function dbChk(data) {
	var tmpData = new Object();
	tmpData = {
		fileList : data,
		requestType : 'dbchk'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0101Servlet', tmpData, 'json');
	if(ajaxReturnData != "5") {
		dialog.alert("개발 형상관리상태가 체크아웃이 아닙니다[2].");
	}else {
		checkDuplication(data);
		//checkSelectedFlag();
		if (secondGridData.length > 0){
			$('[data-ax5select="cboSys"]').ax5select("disable");
			$('#btnReq').attr('disabled',false);
		}
	}
}

function checkDuplication(downFileList) {
	var secondGridList = new Array;
	var i = 0;
	var j = 0;
	var findSw = false;
	var totCnt = secondGridData.length;
	
	if(typeof downFileList == 'string' && downFileList.indexOf('ERROR') > -1) {
		dialog.alert("에러가 발생했습니다.");
		return;
	}
	
	for(i=0; downFileList.length>i ; i++){
		findSw = false;
		totCnt = secondGridData.length;
		for (j=0; totCnt>j; j++) {
			if (downFileList[i].cr_itemid == secondGridData[j].cr_itemid) {
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
	
	if(downFileList.length > 0) {
		$(downFileList).each(function(i){
			var currentItem = downFileList[i];
			$(firstGrid.list).each(function(j){
				if(firstGrid.list[j].cr_itemid == currentItem.cr_itemid) {
					firstGrid.list[j].selected_flag = '1';
					firstGrid.list[j].__disable_selection__ = true;
					return false;
				}
				
			});
		});
	}

	firstGrid.repaint();
	secondGrid.addRow(secondGridList);
	secondGrid.repaint();
	
	if(secondGrid.list.length > 0 ) {
		$('[data-ax5select="cboSys"]').ax5select("disable");
		$('#btnReq').prop('disabled',false);
	}
	simpleData();
}

function deleteDataRow() {
	var secondGridSeleted = secondGrid.getList("selected");
	var originalData = null;
	
	$(secondGridSeleted).each(function(i){
		for(var x=0; x<secondGrid.list.length; x++){
			if(secondGrid.list[x].baseitemid == this.baseitemid){
				secondGrid.select(x,{selected:true} );
			}
		}
		
		$(firstGrid.list).each(function(j){
			if(firstGrid.list[j].baseitemid == secondGridSeleted[i].baseitemid) {
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
		$('#btnReq').prop('disabled',true);
	}
	secondGridData = clone(secondGrid.list);
}

function checkOutCnlClick(){
	if (secondGridData.length == 0){
		dialog.alert("신청할 파일을 입력하여 주십시오.");
		return;
	}
	if ($('#txtSayu').val().trim().length == 0){
		dialog.alert("신청사유를 입력하여 주십시오.");
		$('#txtSayu').focus();
		return;
	}
	
	cnclConfirm();
}

function cnclConfirm(){
	var strRsrcCd = "";
	var x=0;
	for (x=0 ; x<secondGridData.length ; x++) {
		if (strRsrcCd.length > 0) {
			if (strRsrcCd.indexOf(secondGridData[x].cr_rsrccd) < 0) {
				strRsrcCd = strRsrcCd + ",";
				strRsrcCd = strRsrcCd + secondGridData[x].cr_rsrccd;
			}
		} else strRsrcCd = secondGridData[x].cr_rsrccd;
	}
	var tmpData = {
			  SysCd : getSelectedVal('cboSys').value,
			  ReqCd : reqCd,
		     RsrcCd : strRsrcCd,
			 UserId : userId,
			  QryCd : reqCd,
		requestType : 'confSelect'
	}	
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0200Servlet', tmpData, 'json');
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
			}
			else{
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
	var tmpRsrc = "";
	var tmpJobCd = "";
	var etcPgmType = new Array();
	
	for (var x=0;x<secondGridData.length;x++) {
		if (tmpRsrc.length > 0) {
			if (tmpRsrc.indexOf(secondGridData[x].cr_rsrccd) < 0)
	            tmpRsrc = tmpRsrc + "," + secondGridData[x].cr_rsrccd;
		} else tmpRsrc = secondGridData[x].cr_rsrccd;
	
		if (tmpJobCd.length > 0) {
			if (tmpJobCd.indexOf(secondGridData[x].cr_jobcd) < 0)
	            tmpJobCd = tmpJobCd + "," + secondGridData[x].cr_jobcd;
		} else tmpJobCd = secondGridData[x].cr_jobcd;
	}
	
	confirmInfoData = new Object();
	confirmInfoData.UserId = userId;
	confirmInfoData.ReqCd = reqCd;
	confirmInfoData.PrjNo = '';
	confirmInfoData.SysCd = getSelectedVal('cboSys').cm_syscd;
	confirmInfoData.RsrcCd = tmpRsrc;
	confirmInfoData.QryCd = reqCd;
	confirmInfoData.EmgSw = 'N';
	confirmInfoData.JobCd = tmpJobCd;
	confirmInfoData.deployCd = '0';
	confirmInfoData.passok = '';
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
			UserId    : userId,
			SysCd 	  : getSelectedVal('cboSys').cm_syscd,
			ReqCd 	  : reqCd,
			RsrcCd    : tmpRsrc,
			PgmType   : '',
			EmgSw 	  : 'N',
			PrjNo 	  : '',
			delpoyCd  : '0',
			QryCd 	  : reqCd,
			passok 	  : '',
			qyulcheck : ''
		}
		
		var tmpData = {
			paramMap	: data,
			requestType : 'Confirm_Info'
		}
		
		ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json');
		console.log('CheckOutCnl.js] Confirm_Info ==> ', ajaxReturnData);
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
	var etcObj = {};
	etcObj.UserID 	= userId;
	etcObj.ReqCD  	= reqCd;
	etcObj.Sayu	  	= $("#txtSayu").val().trim();
	etcObj.TstSw	= getSelectedVal('cboSys').TstSw;
	etcObj.Orderid 	= "";
	etcObj.syscd 	= getSelectedVal('cboSys').cm_syscd;
	etcObj.sayu 	= $("#txtSayu").val().trim();
	etcObj.EmgCd 	= "0";
	
	var tmpData = {
		chkOutList : secondGridData,
		etcData : etcObj,
		ConfList : confirmData,
		requestType : 'request_Check_Out_Cancel'
	}	
	
	ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr0101Servlet', tmpData, 'json');
	console.log('CheckOutCnl.js] request_Check_Out_Cancel ==> ', ajaxReturnData);
	successRequest(ajaxReturnData);
}

function successRequest(data){
	console.log('data ==>',data);
	if (typeof(data) == 'string' && data.indexOf('ERR') >= 0) {
		if (reqCd == "11")  dialog.alert("체크아웃취소 신청실패.");
	    else dialog.alert("테스트적용취소 신청실패.");
	} else if (data == 0) {
		firstGrid.setData([]);
		firstGridData = [];
		secondGrid.setData([]);
		secondGridData = [];

		if (reqCd == "11") {
			dialog.alert("체크아웃취소 신청완료.");
			/*confirmDialog.confirm({
				msg: '체크아웃취소 신청완료!\n상세 정보를 확인하시겠습니까?',
			}, function(){
				if(this.key === 'ok') {
					openWindow('DETAIL','');
				}
				else{
					findRefresh();
				}
			});*/
		}else {
			dialog.alert("테스트적용취소 신청완료.");
			/*confirmDialog.confirm({
				msg: '테스트적용취소 신청완료!\n상세 정보를 확인하시겠습니까?',
			}, function(){
				if(this.key === 'ok') {
					openWindow('DETAIL','');
				}
				else{
					findRefresh();
				}
			});*/
		}
		$('[data-ax5select="cboSys"]').ax5select("enable");
		$('#btnReq').attr('disabled',true);
	}else{
		if (reqCd == "11") dialog.alert("체크아웃취소 신청실패.");
	    else dialog.alert("테스트적용취소 신청실패.");
	}
}

function findRefresh(){
	firstGrid.setData([]);
	firstGridaData = [];

	$('[data-ax5select="cboSys"]').ax5select("enable");
	$('#btnReq').attr('disabled',true);
	findProc();
}

function openWindow(type,param) {
	var nHeight, nWidth, cURL, winName;

	if ( ('pop_'+type) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = 'pop_'+type;

	var f = document.popPam;   	//폼 name
    f.user.value = userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 'DETAIL') {    
        f.acptno.value = acptNo;
	    cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	} else {
		dialog.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
    
    if(type == 'DETAIL') findRefresh();
}
