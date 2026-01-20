var userId 		 	= window.top.userId;

var instanceGrid = new ax5.ui.grid();  

var selectedIndex;		//select 선택 index
var selectedItem;		//select 선택 item
var gridSelectedIndex;  //그리드 선택 index
var selectedGridItem;	//그리드 선택 item

var cboSystemData 	= []; //시스템 데이터
var cboJobData		= []; //업무 데이터
var cboGbnData		= []; //프로그램경로 데이터
var cboInsGbnData	= []; //프로그램구분 데이터
var cboInsNameData	= []; //프로그램언어 데이터
var tmpInfoData = new Object();

var strChk 	   = "";
var strGridChk = "0";

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

instanceGrid.setConfig({
    target: $('[data-ax5grid="instanceGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            instanceGrid_Click();
        },
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "SYSMSG", 		label: "노드",  			width: '6%',	align: "left"},
    	{key: "runcd", 			label: "구분",  			width: '6%',	align: "left"},
        {key: "cd_svrip", 		label: "IP",	  		width: '6%',	align: "left"},
        {key: "cd_portno", 		label: "PORT",   		width: '6%',	align: "left"},
        {key: "reqcd", 			label: "작업구분",   		width: '7%',	align: "left"},
        {key: "instancecd", 	label: "인스턴스구분", 		width: '7%',	align: "center"},
        {key: "instanceid", 	label: "인스턴스ID",  		width: '7%',	align: "center"},
        {key: "cd_svrstop", 	label: "STOP명령",  		width: '7%',	align: "center"},
        {key: "cd_svrstart",	label: "START명령",  		width: '7%',	align: "center"},
        {key: "CD_STOPCK", 		label: "STOP조회명령", 	width: '7%',	align: "center"},
        {key: "CD_STARTCK", 	label: "START조회명령",  	width: '7%',	align: "center"},
        {key: "cd_healthck", 	label: "Health체크명령",  	width: '6%',	align: "center"},
        {key: "CD_CREATDT", 	label: "등록일",  		width: '6%',	align: "center"},
        {key: "CD_EDITOR", 		label: "등록자",  		width: '6%',	align: "center"},
        {key: "CD_LASTUPDT",	label: "변경일",  		width: '9%',	align: "center"}
    ]
});

$(document).ready(function(){
	getCodeInfo();
	btnQry_Click();
	
	//조회
	$('#btnQry').bind('click',function() {
		btnQry_Click();
	});
	
	//등록
	$('#btnReg').bind('click',function() {
		btnReg_Click();
	});
	
	//삭제
	$('#btnDel').bind('click',function() {
		btnDel_Click();
	});
});

function screenInit() {
	$('[data-ax5select="cboSystem"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboJob"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboGbn"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboInsGbn"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboInsName"]').ax5select({
		options: []
	});
	
	$('#txtRsrcName').val("");
	$('#txtStory').val("");
	$('#btnDevRep').prop("disabled", true);
	$('#txtDsn').prop("disabled", true);
	$('#txtDsnHome').prop("disabled", true);
} 

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('CMR7000JOB','SEL','N'),
		new CodeInfo('CMR7000ID','SEL','N'),
		new CodeInfo('CMR7000GBN','SEL','N'),
		new CodeInfo('CMR7000ND','SEL','N'),
		new CodeInfo('INSTANCE','SEL','N')
	]);
	cboJobData = codeInfos.CMR7000JOB;
	cboInsNameData = codeInfos.CMR7000ID;
	cboGbnData = codeInfos.CMR7000GBN;
	cboSystemData = codeInfos.CMR7000ND;
	cboInsGbnData = codeInfos.INSTANCE;
	
	$('[data-ax5select="cboJob"]').ax5select({
		options: cboJobData
	});
	$('[data-ax5select="cboInsName"]').ax5select({
		options: cboInsNameData
	});
	$('[data-ax5select="cboGbn"]').ax5select({
		options: cboGbnData
	});
	$('[data-ax5select="cboSystem"]').ax5select({
		options: cboSystemData
	});
	$('[data-ax5select="cboInsGbn"]').ax5select({
		options: cboInsGbnData
	});
}

function btnQry_Click() {
	tmpInfoData = new Object();
	tmpInfoData = {
		node 	 	: getSelectedVal('cboSystem').cm_micode,
		instancecd  : getSelectedVal('cboInsGbn').cm_micode,
		instanceid 	: getSelectedVal('cboInsName').cm_micode,
		reqcd 		: getSelectedVal('cboJob').cm_micode,
		runcd 	 	: getSelectedVal('cboGbn').cm_micode,
		requestType	: 'cmd7000_SELECT'
	}
	
	console.log('tmp',tmpInfoData );
	$('[data-ax5grid="instanceGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', tmpInfoData, 'json', successQry);
}

function successQry(data) {
	$(".loding-div").remove();
	strGridChk = "0";
	if(data.length > 0) {
		instanceGrid.setData(data);
	} else {
		dialog.alert('검색결과가 없습니다.');
	}
}

function btnReg_Click() {
	var id = "";
	var dirSw = false;
	
	if(getSelectedIndex('cboSystem') < 1) {
		dialog.alert('노드명을 선택하세요.');
		return;
	} else if(getSelectedIndex('cboInsGbn') < 1) {
		dialog.alert('인스턴스구분을 선택하세요.');
		return;
	} else if(getSelectedIndex('cboInsName') < 1) {
		dialog.alert('인스턴스명을 선택하세요.');
		return;
	} else if($("#txtIp").val().length == 0) {
		dialog.alert('IP를 입력하세요.');
		$('#txtIp').focus();
		return;
	} else if($("#txtStart").val().length == 0) {
		dialog.alert('START 명령을 입력하세요.');
		$('#txtStart').focus();
		return;
	} else if($("#txtStatus").val().length == 0) {
		dialog.alert('상태조회 명령을 입력하세요.');
		$('#txtStatus').focus();
		return;
	} else if(getSelectedIndex('cboGbn') < 1) {
		dialog.alert('구분을 선택하세요.');
		return;
	} else if(getSelectedIndex('cboJob') < 1) {
		dialog.alert('작업구분을 선택하세요.');
		return;
	} else {
		var i = 0;
		if($("#chkRun").is(":checked")) strChk = "L";
		else strChk = "R";
		
		tmpInfoData = new Object();
		tmpInfoData = {
				node		: getSelectedVal('cboSystem').cm_micode,
				svrip		: $("#txtIp").val().trim(),
				portno 		: $("#txtPort").val().trim(),
				svrstart	: $("#txtStart").val().trim(),
				svrstop		: $("#txtStop").val().trim(),
				stopck		: $("#txtStatus").val().trim(),
				startck		: $("#txtStatus2").val().trim(),
				instancecd	: getSelectedVal('cboInsGbn').cm_micode,
				instanceid	: getSelectedVal('cboInsName').cm_micode,
				reqcd		: getSelectedVal('cboJob').cm_micode,
				runcd		: getSelectedVal('cboGbn').cm_micode,
				userid		: userId,
				strchk 		: strChk,
				health 		: $("#txtHealth").val().trim(),
				requestType	: 'cmd7000_INSERT'
		}
		ajaxAsync('/webPage/ecmr/Cmr3800Servlet', tmpInfoData, 'json', successInsert);
	}
}

function successInsert(data) {
	instanceGrid.setData(data);
	tmpInfoData = new Object();
	tmpInfoData = {
		node 	 	: getSelectedVal('cboSystem').cm_micode,
		instancecd  : getSelectedVal('cboInsGbn').cm_micode,
		instanceid 	: getSelectedVal('cboInsName').cm_micode,
		reqcd 		: getSelectedVal('cboJob').cm_micode,
		runcd 	 	: getSelectedVal('cboGbn').cm_micode,
		requestType	: 'cmd7000_SELECT'
	}
	$('[data-ax5grid="instanceGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', tmpInfoData, 'json', successQry);
}

function instanceGrid_Click() {
	gridSelectedIndex = instanceGrid.selectedDataIndexs;
	selectedGridItem = instanceGrid.list[instanceGrid.selectedDataIndexs];

	if(gridSelectedIndex < 0) return;
	strGridChk = "1";
	var i = 0;
	
	$("#txtIp").val(selectedGridItem.cd_svrip);
	$("#txtPort").val(selectedGridItem.cd_portno);
	$("#txtStop").val(selectedGridItem.cd_svrstop);
	$("#txtStart").val(selectedGridItem.cd_svrstart);
	$("#txtStatus").val(selectedGridItem.CD_STOPCK);
	$("#txtStatus2").val(selectedGridItem.CD_STARTCK);
	$("#txtHealth").val(selectedGridItem.cd_healthck);
	
	for(i = 0; i < cboSystemData.length; i++) {
		if(selectedGridItem.cd_node == cboSystemData[i].cm_micode) {
			$("#cboSystem").ax5select('setValue',cboSystemData[i].value,true);
		}
	}
	for(i = 0; i < cboJobData.length; i++) {
		if(selectedGridItem.cd_reqcd == cboJobData[i].cm_micode) {
			$("#cboJob").ax5select('setValue',cboJobData[i].value,true);
		}
	}
	for(i = 0; i < cboGbnData.length; i++) {
		if(selectedGridItem.cd_runcd == cboGbnData[i].cm_micode) {
			$("#cboGbn").ax5select('setValue',cboGbnData[i].value,true);
		}
	}
	for(i = 0; i < cboInsGbnData.length; i++) {
		if(selectedGridItem.cd_instancecd == cboInsGbnData[i].cm_micode) {
			$("#cboInsGbn").ax5select('setValue',cboInsGbnData[i].value,true);
		}
	}
	for(i = 0; i < cboInsNameData.length; i++) {
		if(selectedGridItem.cd_instanceid == cboInsNameData[i].cm_micode) {
			$("#cboInsName").ax5select('setValue',cboInsNameData[i].value,true);
		}
		if(selectedGridItem.cd_runtype == "R") {
			$("#chkRun").prop("checked", false);
		} else {
			$("#chkRun").prop("checked", true);
		}
	}
}

function btnDel_Click() {
	selectedGridItem = instanceGrid.list[instanceGrid.selectedDataIndexs];
	if(strGridChk == "1") {
		tmpInfoData = new Object();
		tmpInfoData = {
			node 	 	: selectedGridItem.cd_node,
			reqcd 		: selectedGridItem.cd_reqcd,
			runcd 	 	: selectedGridItem.cd_runcd,
			instancecd  : selectedGridItem.cd_instancecd,
			instanceid 	: selectedGridItem.cd_instanceid,
			requestType	: 'cmd7000_DELETE'
		}
		$('[data-ax5grid="instanceGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmr/Cmr3800Servlet', tmpInfoData, 'json', successDelete);
	} else {
		dialog.alert('화면에서 삭제할 대상을 선택해주세요.');
	}
}

function successDelete(data) {
	$(".loding-div").remove();
	tmpInfoData = new Object();
	tmpInfoData = {
		node 	 	: getSelectedVal('cboSystem').cm_micode,
		instancecd  : getSelectedVal('cboInsGbn').cm_micode,
		instanceid 	: getSelectedVal('cboInsName').cm_micode,
		reqcd 		: getSelectedVal('cboJob').cm_micode,
		runcd 	 	: getSelectedVal('cboGbn').cm_micode,
		requestType	: 'cmd7000_SELECT'
	}
	$('[data-ax5grid="instanceGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', tmpInfoData, 'json', successQry);
}
