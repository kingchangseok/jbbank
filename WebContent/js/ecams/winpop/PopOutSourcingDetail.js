/**
 * 외주개발요청상세 (eCmc0411.mxml)
 * 
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-08-00
 * 
 */

var pUserId 	= null;
var pOrederId   = null;
var attPath		= getDocPath();

var f = document.getReqData;
pUserId = f.user.value;
pOrederId = f.orderId.value;

var userGrid		= new ax5.ui.grid();
var fileGrid		= new ax5.ui.grid();

var userGridData	= [];
var fileGridData	= [];
var lstPMData		= [];

userGrid.setConfig({
    target: $('[data-ax5grid="userGrid"]'),
    sortable: true, 
    multiSort: true,
    multiselect: false,
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
    multiselect: false,
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
    	},
     	trStyleClass: function () {
     		if (this.item.baseitem != this.item.cr_itemid) {
     			return "fontStyle-module";
     		}
     	}
    },
    columns: [
        {key: 'cc_attfile', 	label: '파일명',  	width: '80%',	align: 'left'},
        {key: 'cm_username', 	label: '첨부인', 		width: '20%',	align: 'left'}
    ]
});

$(document).ready(function(){
	if (pOrederId == null) {
		confirmDialog2.alert('발행번호가 없습니다. \n다시 시도 하시기 바랍니다.');
		return;
	}
	
	if (pUserId == null) {
		confirmDialog2.alert('로그인정보가 없습니다.\n다시 로그인 하시기 바랍니다.');
		return;
	}
	
	//닫기
	$('#btnClose').bind('click', function() {
		close();
	});
	
	getOtherUser();
	getRequest();
});

function getRequest() {
	var data = new Object();
	data = {
		OrderId : pOrederId,
		requestType : 'getRequest'
	}
	ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successGetRequest);
}

function successGetRequest(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	var dataObj = data[0];
	$('#txtReqNum0').val(dataObj.cc_reqid);
	$('#txtTitle').val(dataObj.cc_docsubj);
	//20240307 수정
	//$('#txtDetail').val(dataObj.cc_detailjobn.replace(/ /g,''));
	$('#txtDetail').val(dataObj.cc_detailjobn);
	$('#txtTime').val(dataObj.cc_endplan);
	$('#txtCause').val(dataObj.cc_docnum);
	$('#txtUsr').val(dataObj.editornm);

	getReqRunners(dataObj.cc_reqid);
	getFiles(dataObj.cc_reqid);
	setPMUser(dataObj.RECVPART);
}

function getReqRunners(reqid) {
	var data = new Object();
	data = {
		ReqId : reqid,
		requestType : 'getReqRunners'
	}
	ajaxAsync('/webPage/ecmc/Cmc0400Servlet', data, 'json',successGetReqRunners);
}

function successGetReqRunners(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	userGridData = data;
	userGrid.setData(userGridData);
}

function getFiles(reqid) {
	var data = new Object();
	data = {
		Id : reqid,
		requestType : 'getFiles'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetFiles);
}

function successGetFiles(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	fileGridData = data;
	fileGrid.setData(fileGridData);
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
		liStr += '	<input type="checkbox" class="checkbox-PM" id="chkPM'+addId+'" data-label="'+lstPMData.cm_username+'"  value="'+lstPMData.cm_userid+'" />';
		liStr += '</div>';
		liStr += '</li>';
	});
	$('#lstPM').html(liStr);
	
	$('input.checkbox-PM').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

function setPMUser(data) {
	var tmpArr = data.split(",");
	
	if(tmpArr != null && tmpArr != undefined) {
		lstPMData.forEach(function(item, index) {
			tmpArr.forEach(function(item2, index2) {
				if(item.cm_userid === item2) {
					$('#chkPM'+item.cm_userid).wCheck('check', true);
				}
			});
		});
	}
}