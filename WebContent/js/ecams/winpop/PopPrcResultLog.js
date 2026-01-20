var pReqNo  = null;
var pUserId = null;
var pSeqNo 	= null;

var retGrid    		= new ax5.ui.grid();

var data           	= null; //json parameter
var cboReqPassData 	= []; //처리구분콤보 데이타
var retGridData    	= []; //처리결과목록 데이타

var lastPrcSys 		= '';
var firstSw    		= true;

var f = document.getReqData;
pReqNo = f.acptno.value;
pUserId = f.user.value;
pSeqNo = f.seqno.value;

$('#txtAcptNo').val(pReqNo.substr(0,4)+'-'+pReqNo.substr(4,2)+'-'+pReqNo.substr(6));

retGrid.setConfig({
    target: $('[data-ax5grid="retGrid"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
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

	       	var selIn = retGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	    	if (this.item.cr_rstfile == null || this.item.cr_rstfile == "") return;			
	    	
	       	getResultLog(this.item.cr_rstfile);
        },
    	trStyleClass: function () {
    		if (this.item.cr_prcrst != null && this.item.cr_prcrst != '' && this.item.cr_prcrst != '0000'){
    			return "fontStyle-error";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
        {key: "SYSGBN",      label: "구분",        width: '12%'},
        {key: "basersrc",    label: "기준프로그램",  width: '19%', align: 'left'},
        {key: "cr_rsrcname", label: "대상프로그램",  width: '19%', align: 'left'},
        {key: "JAWON",       label: "프로그램종류",  width: '10%', align: 'left'},
        {key: "resultsvr",   label: "적용서버",     width: '15%', align: 'left'},
        {key: "prcrstname",  label: "처리결과",     width: '10%'},
        {key: "prcdt",       label: "처리시간",     width: '15%'},
    ]
});

$(document).ready(function(){
	if (pUserId == null || pUserId == undefined || pUserId == '' || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pReqNo == null || pUserId == undefined || pUserId == '' || pReqNo.length != 12) {
		dialog.alert('신청번호가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
	//처리구분 콤보선택
	$('#cboReqPass').bind('change', function() {
		retGrid.setData([]);
		
		if (retGridData == null || retGridData.length < 1) return;
		
		var retGridDataFilterData = [];
		retGridData.forEach(function(lstData, Index) {
			if ( getSelectedIndex('cboReqPass') == 0 ) {
				retGridDataFilterData.push(lstData);
			} else {
				if (lstData.cr_prcsys == getSelectedVal('cboReqPass').value) {
					retGridDataFilterData.push(lstData);
				}
			}
		});
		retGrid.setData(retGridDataFilterData);
		retGrid.repaint();
		var i = 0;
		var findSw = false;
		if (retGridDataFilterData.length>0) {
			if ( getSelectedIndex('cboReqPass') == 0 ) {
				if (lastPrcSys != null && lastPrcSys != '' && lastPrcSys != undefined) {
					for (var i=0;retGridDataFilterData.length>i;i++) {
			        	if (retGridDataFilterData[i].cr_prcsys == lastPrcSys) {
			        		findSw = true;
			        		break;
			        	}
					}
				}
			} 
			
			if (!findSw) i = 0;
			retGrid.select(i);
			retGrid.focus(i);
			
	    	if (retGridDataFilterData[i].cr_rstfile == null || retGridDataFilterData[i].cr_rstfile == "") return;			
	    	
	       	getResultLog(retGridDataFilterData[i].cr_rstfile);
		}
	});
	
	//조회 클릭
	$('#btnSearCh').bind('click', function() {
		if (pReqNo == null) {
			return;
		}
		getPrcGbnInfo();
	});
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		close();
	});
	
	$('#btnSearCh').trigger('click');
	
});

//처리구분정보 가져오기
function getPrcGbnInfo(){
	data = new Object();
	data = {
			acptNo	: pReqNo,
		requestType : 'getResultGbn'
	}
	ajaxAsync('/webPage/ecmr/Cmr5100Servlet', data, 'json',successGetResultGbn);
}

//처리구분정보 가져오기 완료
function successGetResultGbn(data){
	cboReqPassData = data;
	$('[data-ax5select="cboReqPass"]').ax5select({
		options: injectCboDataToArr(cboReqPassData, 'cm_micode' , 'cm_codename')
	});

	getPrcResultList();
}

//처리결과 목록조회하기
function getPrcResultList() {
	data = new Object();
	data = {
		acptNo 		: pReqNo,
		UserId 		: pUserId,
		requestType : 'getResultList'
	}
	ajaxAsync('/webPage/ecmr/Cmr5100Servlet', data, 'json',successGetResultList);
}

//로그가져오기  완료
function successGetResultList(data) {
	lastPrcSys = '';
	if (typeof data == 'string' && data.indexOf('ERR')>=0) {
		dialog.alert(data);
		return;
	}
	if (data.length>0) {
		lastPrcSys = data[data.length-1].lstprc;
		var findSw = false;
		if (lastPrcSys != null && lastPrcSys != '' && lastPrcSys != undefined) {
			data.splice(data.length-1,1);
		}

		retGridData = data;
		retGrid.setData(retGridData);
		if (retGridData.length>0) {
			if (firstSw) {
				if (pSeqNo != null && pSeqNo != '' && pSeqNo != undefined) {
					for (var i=0;retGridData.length>i;i++) {
			        	if (retGridData[i].cr_seqno == pSeqNo) {
			        		retGrid.select(i);
			        		retGrid.focus(i);
			        		findSw = true;
			        		break;
			        	}
					}
				}
				firstSw = false;
			}
		    if (!findSw && lastPrcSys != null && lastPrcSys != '' && lastPrcSys != undefined) {
				for (var i=0;retGridData.length>i;i++) {
		        	if (retGridData[i].cr_prcsys == lastPrcSys) {
		        		retGrid.select(i);
		        		retGrid.focus(i);
		        		findSw = true;
		        		break;
		        	}
				}
			}
		    if (!findSw) i = 0;
		    
			if (retGridData[i].cr_rstfile == null || retGridData[i].cr_rstfile == "" || retGridData[i].cr_rstfile == undefined) return;			
	    	
	       	getResultLog(retGridData[i].cr_rstfile);
		}
	}
}

//로그정보 가져오기
function getResultLog(rstfile) {
	$('#txtRetLog').val('');
	
	data = new Object();
	data = {
			rstfile	: rstfile,
		requestType : 'getFileText'
	}
	ajaxAsync('/webPage/ecmr/Cmr5100Servlet', data, 'json',successGetFileText);
}

//로그정보 가져오기 완료
function successGetFileText(data){
	$('#txtRetLog').val(data);
}