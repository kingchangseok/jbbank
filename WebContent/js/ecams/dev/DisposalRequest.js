var reqCd 		= window.top.reqCd;
var userid 		= window.top.userId;
var mainGrid	= new ax5.ui.grid();
var picker		= new ax5.ui.picker();
var approvalModal = new ax5.ui.modal();
var secuYn		= null;
var selectedItem = null;
var rsrcCd		= "";
var confirmInfoData = null;
var confirmData = [];
var systemSelData = [];
var columnData	= 
	[ 
		{key : "cm_jobname",label : "업무",align : "left",width: "10%"}, 
		{key : "cm_dirpath",label : "프로그램경로",align : "left",width: "30%"}, 
		{key : "cr_rsrcname",label : "프로그램명",align : "left",width: "20%"}, 
		{key : "cm_codename",label : "프로그램종류",align : "left",width: "8%"}, 
		{key : "cr_version",label : "버전",align : "left",width: "4%"},
		{key : "cm_username",label : "최종변경인",align : "left",width: "5%"},
		{key : "sta",label : "상태",align : "left",width: "5%"}
	];

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	showLineNumber : true,
	showRowSelector : true,
	multipleSelect : true,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {align: "center"},
	body : {
		columnHeight: 24,
        onClick: function () {
//        	this.self.clearSelect();
            this.self.select(this.dindex);
            selectedItem = this.item;
        },
        trStyleClass: function () {
        	if (this.item.cr_status != '0'){
        		return "font-pink";
        	} 
        }
	},
	columns : columnData,
	contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        }
   }
});

$(document).ready(function() {
	isAdmin();
	getSysInfo();

	//조회
	$("#btnQry").bind('click', function() {
		search();
	});
	
	$("#btnReq").bind('click', function() {
		disposalReq();
	})
	
	//엔터조회
	$("#programPath").bind('keypress', function() {
		if(window.event.keyCode == 13) search();
	})
	
	$("#programName").bind('keypress', function() {
		if(window.event.keyCode == 13) search();
	})
	
});

function search() {
	var inputData = new Object();
	inputData = {
		sysCd : $("[data-ax5select='systemSel']").ax5select("getValue")[0].value,
		userId  : userid,
		dirPath : $("#programPath").val(),
		rsrcName : $("#programName").val(),
		secuYn : secuYn
	}
	ajaxData = {
		data : inputData,
		requestType : "getDeleteList"
	}
	var ajaxResult = ajaxCallWithJson('/webPage/dev/DisposalRequest', ajaxData, 'json');
	mainGrid.setData(ajaxResult);
	console.log(ajaxResult);
}

function disposalReq() {
	var selectedArr = mainGrid.selectedDataIndexs;
	for(var i = 0; i < selectedArr.length; i++) {
		console.log(mainGrid.list[selectedArr[i]]);
	}
	if(selectedArr.length == 0) {
		dialog.alert("폐기할 프로그램을 선택하십시오.");
		return;
	}
	
	if($("#reasonTxt").val().length < 1) {
		dialog.alert("폐기사유를 입력하시기 바랍니다.");
		return;
	}
	
    mask.open();
    confirmDialog.setConfig({
        title: "확인",
        theme: "info"
    });
	confirmDialog.confirm({
		msg: '폐기신청 하시겠습니까?',
		btns :{
			ok: {
                label:'ok'
            },
            cancel: {
                label:'cancel'
            }
		}
	}, function(){
		if(this.key === 'ok') {
			confSelect(selectedArr);
		} else {
			mask.close();
			deleteReq();
		}
	});
	
}

function confSelect(data) {
	for(var i = 0; i < data.length; i++) {
		rsrcCd += mainGrid.list[data[i]].cr_rsrccd + ",";
	}
	rsrcCd = rsrcCd.substr(0, rsrcCd.length -1);
	var confData = {
		SysCd		: $("[data-ax5select='systemSel']").ax5select("getValue")[0].value,
		ReqCd		: reqCd,
		strRsrcCd	: rsrcCd, 
		UserID		: userId,
		strQry		: "04"
	}
	var ajaxData = {
		requestType : "confSelect",
		confirmInfoData : confData 
	}
	ajaxResultData = ajaxCallWithJson('/webPage/request/ConfirmServlet', ajaxData, 'json');
	confChk(ajaxResultData);
}

function confChk(retMsg) {
	if (retMsg == "X") {
		dialog.alert("로컬PC에서 파일을 전송하는 결재단계가 지정되지 않았습니다. 형상관리시스템 담당자에게 연락하여 주시기 바랍니다.");
	} else if (retMsg == "C") {
	    confirmDialog.setConfig({
	        title: "확인",
	        theme: "info"
	    });
		confirmDialog.confirm({
			msg: '결재자를 지정하시겠습니까?',
			btns :{
				ok: {
	                label:'ok'
	            },
	            cancel: {
	                label:'cancel'
	            }
			}
		}, function(){
			if(this.key === 'ok') {
				confCall("Y");
			} else {
				confCall("N");
			}
		});
	} else if (retMsg == "Y") {
		confCall("Y");
    } else if (retMsg != "N") {
		dialog.alert("결재정보가 등록되지 않았습니다. 형상관리시스템담당자에게 연락하여 주시기 바랍니다.");
    } else {
		confCall("N");
    }
}

function confCall(GbnCd) {
	confirmInfoData = {		
			UserId : userid,
			ReqCd  : reqCd,
			SysCd  : getSelectedVal('systemSel').value,
			RsrcCd : [rsrcCd],
			svChkDev : "false",
			EmgSw : "0",
			PrjNo : "",
			JobCd : "0",
			QryCd : ["05"],
			dbaSW : "N"
	}
	
  	if (GbnCd == "Y") {//결재팝업		
		approvalModal.open({
	        width: 820,
	        height: 600,
	        iframe: {
	            method: "get",
	            url: "../modal/request/ApprovalModal.jsp",
	            param: "callBack=modalCallBack"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	if(confirmData.length > 0){
	            		deleteReq();
	            	}
	                mask.close();
	            }
	        }
		});
       
	} else if (GbnCd == "N") {

		var tmpData = {
			confirmInfoData: 	confirmInfoData,
			requestType: 	'Confirm_Info'
		}
		
		ajaxReturnData = ajaxCallWithJson('/webPage/request/ConfirmServlet', tmpData, 'json');
		confirmData = ajaxReturnData;
		deleteReq();
	}
}

function deleteReq() {
	var selectedIndex = mainGrid.selectedDataIndexs;
	var confirmArr = confirmData;
	var tmpArr = [];
	for (var i = 0; i < selectedIndex.length; i++) {
		tmpArr.push(mainGrid.list[selectedIndex[i]]);
	}
	var tmpInfo = {			
		syscd	: $("[data-ax5select='systemSel']").ax5select("getValue")[0].value,
		userid	: userid,
		reqCd	: "04",
		txtSayu	: $("#reasonTxt").val()
	}
	
	$.each(confirmArr, function(i, val) {
		console.log(val);
	})
	
	var ajaxData = {
		tmpInfo : tmpInfo,
		tmpArr	: tmpArr,
		confirmArr : confirmArr,
		requestType	: "deleteReq"
	}
	
	ajaxResultData = ajaxCallWithJson('/webPage/dev/DisposalRequest', ajaxData, 'json');
	dialog.alert("폐기신청이 정상적으로 완료되었습니다.");
	search();
	confirmData = [];
}

function isAdmin() {
	var ajaxData = {
		requestType : 'checkAdmin',
		userId : userid
	}
	ajaxResultData = ajaxCallWithJson('/webPage/common/CommonUserInfo', ajaxData, 'json');
	console.log("isAdmin Result : " + ajaxResultData);
	secuYn = ajaxResultData ? "Y" : "N"; 
}

function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
		UserId : userid,
		SecuYn : "Y",
		SelMsg : "ALL",
		CloseYn : "N",
		ReqCd : "",
	}
	data = {
		requestType	: 'getSysInfo',
		sysData : sysData
	}
		ajaxAsync('/webPage/common/SysInfoServlet', data, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	systemSelData = data;
	
	$('[data-ax5select="systemSel"]').ax5select({
        options: injectCboDataToArr(systemSelData, 'cm_syscd' , 'cm_sysmsg')
	});
}
