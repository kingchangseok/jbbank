var acptNo	= window.top.pReqNo;

var deptGrid	= new ax5.ui.grid();
var deptGridData   = [];

var isConf 			= false;
var isEnabledComp	= false;

var tmpArr = [];
var tmpInfoData = new Object();

var strTestCd	= "";

var completeReadyFunc = false;

$('input:radio[name=rdoGbn1]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	createViewGrid();

	$("#btnUpdt").bind("click", function(){
		btnUpdt_Click();
	});
	
	completeReadyFunc = true;
});

function upFocus() {
	//deptGrid.focus('HOME');
	getTestData();
	getReqRunners();
}

function createViewGrid() {
	deptGrid.setConfig({
	    target: $('[data-ax5grid="deptGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showLineNumber: true,
	    paging : false,
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
	    		if (this.item.SubItems9 === '3'){
	    			return 'fontStyle-cncl';
	    		} 
	    	},
	    	onDataChanged: function(){
	    	    this.self.repaint();
	    	}
	    },
	    columns: [
	    	{key: 'cm_deptname',	label: '부서',  		width: '50%',	align: 'center'},
			{key: 'cm_username',	label: '담당자', 		width: '50%',	align: 'left'},
	    ]
	});
	
	getTestData();
	getReqRunners();
}

function btnUpdt_Click() {
	var cnt = 0;
	if(!$("#radio0").is(":checked") && !$("#radio1").is(":checked") && !$("#radio2").is(":checked"))
		++cnt;
	
	if(cnt > 0){
		dialog.alert("TEST 결과를 선택하여 주십시오.");
		return;
	}
	
	if(!$("#radio0").is(":checked") && $('#txtCnclSayu').val().length == 0){
		dialog.alert("부적합 사유 및 추가 개선 요구사항을 입력하여 주십시오.");
		return;
	}
	
	var tmpObj = new Object();
	tmpObj.cr_testrslt = strTestCd;
	tmpObj.cr_cnclsayu = $('#txtCnclSayu').val().trim(); 	
	tmpObj.cr_testdate = $('#txtDatTest').val().trim(); 
	tmpObj.cr_etcsayu  = $('#txtEtcSayu').val().trim(); 
	
	var data = new Object();
	data = {
		AcptNo : window.top.pReqNo,
		Data   : tmpObj,
		requestType : 'setTestData'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successSetTestData);
}

function successSetTestData(data) {
	if(!data) dialog.alert("테스트 결과 저장을 실패하였습니다.");
	else dialog.alert("테스트 결과 저장을 완료하였습니다.");
}

function getTestData() {
	/*for(var i = 0; i < 3; i++) {
		if($('#radio'+ i ).is(":checked"))
			return;
	}*/
	var data = new Object();
	data = {
		AcptNo : window.top.pReqNo,
		requestType : 'getTestData'
	}
	console.log('[getTestData] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetTestData);
}

function successGetTestData(data) {
	console.log('[successGetTestData] ==>', data);
	var tmpData = data;
	
	if(tmpData.length > 0) {
		var obj = tmpData[0];
		var str = obj.cr_testdate;
		if(str != null && str != "" && str != undefined) {
			$('#txtDatTest').val(str.substr(0,4)+"/"+str.substr(4,2)+"/"+str.substr(6,2));
		} 
		
		$('#txtEtcSayu').val(obj.cr_etcsayu);
		
		if(obj.cr_testrslt != null && obj.cr_testrslt != "") strTestCd = obj.cr_testrslt;

		for(var i = 0; i < 3; i++) {
			if($('#radio'+ i ) == obj.cr_testrslt)
				$('#radio'+ i ).wRadio("check", true);
		}
		
		if(obj.cr_cnclsayu != null && obj.cr_cnclsayu != "") $('#txtCnclSayu').val(obj.cr_cnclsayu);
	}
	
	for(var i = 0; i < 3; i++) {
		if($('#radio'+ i ).value == "01"){
			$('#radio'+ i ).wRadio("check", true);
			strTestCd = "01";
		}
	}
}

function getReqRunners() {
	var data = new Object();
	data = {
		AcptNo : window.top.pReqNo,
		requestType : 'getReqRunners'
	}
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetReqRunners);
}

function successGetReqRunners(data) {
	deptGrid.setData(data);
	
	if(data.length < 0) {
		$('#btnUpdt').prop("disabled", true);
		$('#txtDatTest').attr("readonly", true);
		$('#txtEtcSayu').attr("readonly", true);
		$('#txtCnclSayu').attr("readonly", true);
	}
}

function radioBtn_Click() {
	if (isEnabledComp){
		if($('#radio0').is(":checked")){
			$('#txtCnclSayu').attr("readonly", true);
			$('#txtCnclSayu').val("");
			isConf = true;
		}
		else {
			isConf = false;
			$('#txtCnclSayu').attr("readonly", false);
		}
		window.parent.setBtnEnabled();
	} else {
		for (var i = 0; i < 3; i++) {
    		 if (strTestCd != "" && !isEnabledComp) {
				if (strTestCd == $('#radio'+ i ).value)
					$('#radio'+ i ).wRadio("check", true);
				else
					$('#radio'+ i ).wRadio("check", false);
    		 } else $('#radio'+ i ).wRadio("check", false);
		}
	}
}

function enabledComp(value) {
	if(value) {
		$('#btnUpdt').prop("disabled", false);
		$('#txtDatTest').attr("readonly", false);
		$('#txtEtcSayu').attr("readonly", false);
		$('#txtCnclSayu').attr("readonly", false);
	}
	
	isEnabledComp = value;
	
	for(var i = 0; i < 3; i++) {
		$('#radio' + i).prop("disabled", !value);
	}
	
	if(value) {
		$('#radio0').wRadio("check", true);
	}
	$('#txtCnclSayu').attr("readonly", false);
	//txtCnclSayu.editable = !radio1.selected;
}