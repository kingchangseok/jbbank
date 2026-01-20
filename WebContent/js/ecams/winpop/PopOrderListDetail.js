/**
 * 업무지시서상세 (eCmc0501.mxml)
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
var thirdUserGrid	= new ax5.ui.grid();
var fileGrid		= new ax5.ui.grid();

var userGridData		= [];
var thirdUserGridData	= [];
var fileGridData		= [];
var reqObj				= [];

var cboThirdGridData	= [{cm_micode:'1', cm_codename:'업무관련자'},{cm_micode:'0', cm_codename:'전임자'}];

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

thirdUserGrid.setConfig({
    target: $('[data-ax5grid="thirdUserGrid"]'),
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
        {key: 'cm_deptname', 	label: '부서',  		width: '43%',	align: 'left'},
        {key: 'cm_username', 	label: '담당자', 		width: '23%',	align: 'left'},
        {key: 'CC_CHECK', 		label: '전임/업무',	width: '33%',	align: 'left',
        	formatter: function(){
    			var selStr = '<div class="select width-100">';
    			selStr += "<select onChange='cboThirdGridChange(this)' class='selBox width-100' disabled>"
    			for(var i=0; i<cboThirdGridData.length; i++) {
    				var selected = "";
    				// 그리드 값이랑 같으면 selected 처리
    				if (this.item.cc_check != null && this.item.cc_check != '') {
    					if(cboThirdGridData[i].cm_micode == this.item.cc_check){
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
	
	//개발요청서보기
	$('#btnReqView').bind('click', function() {
		if(reqObj.length == 0) return;
		
		if(reqObj.cc_doctype == '01' || reqObj.cc_doctype == '02' || reqObj.cc_doctype == '04' || reqObj.cc_doctype == '99') openWindow('1'); //개발요청상세(eCmc0401.mxml)
		else if(reqObj.cc_doctype == '03') openWindow('2'); //외주개발요청상세(eCmc0411.mxml)
	});
	
	//닫기
	$('#btnClose').bind('click', function() {
		close();
	});
	
	getFiles();
	getOrderInfo();
	getOrderRunners();
	getOrderThirds();
});

function getFiles() {
	var data = new Object();
	data = {
		Id : pOrederId,
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

function getOrderInfo() {
	var data = new Object();
	data = {
		OrderId : pOrederId,
		requestType : 'getOrderInfo'
	}
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetOrderInfo);
}

function successGetOrderInfo(data) {
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
	
	var tmpObj = data[0];
	$('#txtOrderNum0').val(tmpObj.cc_orderid);
	$('#txtCause').val(tmpObj.cc_docnum);
	
	$('#txtPrcdate').val(tmpObj.cc_endplan);
	$('#txtUsr').val(tmpObj.cm_username);
	if(tmpObj.cc_reqid == null || tmpObj.cc_reqid == undefined) $('#txtTitl0').val('');
	else $('#txtTitl0').val(tmpObj.cc_reqid);
	$('#txtTitle').val(tmpObj.cc_reqsub);
//	$('#txtDetail').val(tmpObj.cc_detailsayu.replace(/ /g,'')); // 20231130 띄어쓰기 나오게 요청 
	$('#txtDetail').val(tmpObj.cc_detailsayu);
	if($('#txtTitl0').val().length == 0) {
		$('#btnReqView').prop('disabled',true);
	}else {
		$('#btnReqView').prop('disabled',false);
		var data = new Object();
		data = {
			OrderId : pOrederId,
			requestType : 'getReqs'
		}
		ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetReqs);
	}
}

function successGetReqs(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data.length > 0) reqObj = data[0]; 
}

function getOrderRunners() {
	var data = new Object();
	data = {
		OrderId : pOrederId,
		requestType : 'getOrderRunners'
	}
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetOrderRunners);
}

function successGetOrderRunners(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	userGridData = data;
	userGrid.setData(userGridData);
}

function getOrderThirds() {
	var data = new Object();
	data = {
		OrderId : pOrederId,
		requestType : 'getOrderThirds'
	}
	ajaxAsync('/webPage/ecmc/Cmc0500Servlet', data, 'json',successGetOrderThirds);
}

function successGetOrderThirds(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	thirdUserGridData = data;
	thirdUserGrid.setData(thirdUserGridData);
}

function openWindow(type) {
	var nHeight, nWidth, cURL, winName;

	if ( ('pop_'+type) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
	
	winName = 'pop_'+type;

    nWidth  = 1046;
	nHeight = 700;
	
	if (type === '1') {//개발요청상세(eCmc0401.mxml)
		cURL = "/webPage/winpop/PopDevRequestDetail.jsp";
	} else if (type === '2') {//외주개발요청상세(eCmc0411.mxml)
		cURL = "/webPage/winpop/PopOutSourcingDetail.jsp";
	} else {
		confirmDialog2.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
	
	var f = document.setReqData;
    f.user.value = pUserId;
    f.orderId.value = pOrederId;
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}