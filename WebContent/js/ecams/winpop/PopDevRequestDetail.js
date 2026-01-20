/**
 * 개발요청상세 (eCmc0401.mxml)
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

var treeObj			= [];
var treeDeptData	= []; //조직트리 데이터

var treeSetting = {
	check: {
		enable: true,
		chkboxType: {
	        "Y": "s",
	        "N": "s"
	    }
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};

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
        {key: 'cm_username', 	label: '첨부인', 		width: '20%',	align: 'center'}
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
	
	getTeamInfoTree();
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
	if(data == null || data == undefined || data == '') {
		dialog.alert('조회 결과가 없습니다.\n[요청번호:' + pOrederId +']');
		return;
	}
	
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	var dataObj = data[0];
	$('#cboReqNum1').val(dataObj.cc_reqid);
	$('#txtDocNum').val(dataObj.cc_docnum);
	$('#txtUsr').val(dataObj.editornm);
	$('#txtDocType').val(dataObj.cc_doctype);
	$('#txtDomestic').val(dataObj.cc_dept1);
	$('#txtForeign').val(dataObj.cc_dept2);
	$('#txtCaller1').val(dataObj.cc_requser1);
	$('#txtCaller2').val(dataObj.cc_requser2);
	$('#txtPrcDt').val(dataObj.cc_endplan);
	$('#txtReqtype').val(dataObj.cc_reqtype);
	$('#txtJob').val(dataObj.cc_jobname);
	$('#txtContype').val(dataObj.cc_acttype);
	$('#txtJobDiff').val(dataObj.cc_jobdif);
	$('#txtStDt').val(dataObj.cc_devstdt);
	$('#txtEndDt').val(dataObj.cc_deveddt);
	$('#txtHandlerDocNum').val(dataObj.cc_docnum2);
	$('#txtTitleDetail').val(dataObj.cc_detailjobn);
	$('#txtTitle').val(dataObj.cc_docsubj);
	$('#txtDetail').val(dataObj.cc_detailsayu.replace(/ /g,''));
	if(dataObj.cc_devperiod != null && dataObj.cc_devperiod != undefined) $('#txtDays').val(dataObj.cc_devperiod);
	
	getReqRunners(dataObj.cc_reqid);
	getFiles(dataObj.cc_reqid);
	setTree(dataObj.RECVPART);
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

function setTree(data) {
	var tmpArr = data.split(",");
	
	console.log('treeObj=>',treeObj,tmpArr);
	treeObj.checkAllNodes(false);
	tmpArr.forEach(function(arrItem, indexP) {
		var node = treeObj.getNodeByParam('id', arrItem);
		if(node !== null && !node.isParen) {
			treeObj.selectNode(node);
			treeObj.checkNode(node, true, true);
		}
	});
}

function getTeamInfoTree() {
	var data = new Object();
	data = {
		chkcd : true,
		itsw : true,
		requestType	: 'getTeamInfoTree_zTree'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetTreeInfo);
}

function successGetTreeInfo(data) {
	/*$('#lstDept').empty(); //리스트 초기화
	var addId = null;
	var liStr = null;
	
	treeDeptData = data;
	
	if(treeDeptData == null || treeDeptData.length < 0) return;
	
	liStr  = '';
	treeDeptData.forEach(function(treeDeptData, Index) {
		addId = treeDeptData.cm_deptcd;
		liStr += '<li class="list-group-item dib width-50" style="min-width: 200px;">';
		liStr += '<div class="margin-3-top">';
		liStr += '	<input type="checkbox" class="checkbox-dept" id="chkDept'+addId+'" data-label="'+treeDeptData.cm_deptname+'"  value="'+treeDeptData.cm_deptcd+'" disabled/>';
		liStr += '</div>';
		liStr += '</li>';
	});
	$('#lstDept').html(liStr);
	
	$('input.checkbox-dept').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});*/
	
	treeDeptData = data;
	$.fn.zTree.init($("#treeDept"), treeSetting, data); //초기화
	treeObj = $.fn.zTree.getZTreeObj("treeDept");
	
	getRequest();
}