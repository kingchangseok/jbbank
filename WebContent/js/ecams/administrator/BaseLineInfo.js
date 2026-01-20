var userId 		= window.top.userId;
var adminYN 	= window.top.adminYN;

var gridList	= new ax5.ui.grid();

var cboSysCdData = [];
var gridListData = [];

var selRow = {};

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});
$('input.checkbox-all').wCheck({
	theme: 'square-inset blue', 
	selector: 'checkmark', 
	highlightLabel: true
});

$(document).ready(function() {
	
	setGrid();
	getSysInfo();
	getBaselinelist();
	
	$("#cboSysCd").bind('change', function() {
		changeSysCbo();
	});
	
	$("#chkAll").bind('click', function() {
		checkAllRgtCdList();
	})
	
	$("#btnRegist").bind('click', function() {
		setBaselinelist();
	});
	
	$("#btnUpdate").bind('click', function() {
		updtBaselinelist();
	});
	
	$("#btnDelete").bind('click', function() {
		delBaselinelist();
	});
	
});

function setGrid() {
	gridList.setConfig({
	    target: $('[data-ax5grid="gridList"]'),
	    sortable: true,
	    multiSort: true,
	    showRowSelector: true,
	    header: {
	        align: "center",
	    },
	    body: {
	        onClick: function () {
	            this.self.select(this.dindex);
	            selRow = this.item;
	            $("#txtTitle").val(this.item.cd_basetitle);
	            $("#txtSayu").val(this.item.cd_basesayu);
	        },
	        onDBLClick: function () {},
	        trStyleClass: function () {
	    		if(this.item.errsw === '1'){
	    			return "fontStyle-cncl";
	    		} else if(this.item.errsw === '0'){
	    			return "fontStyle-ing";
	    		}
	    	},
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
			{key: "cm_sysmsg",		label: "시스템명"		, width: "20%", align: "center"},
			{key: "cd_basetitle",	label: "BaseLine제목"	, width: "20%", align: "center"},
			{key: "cd_basesayu",	label: "BaseLine사유"	, width: "25%", align: "center"},
			{key: "lastdt",			label: "등록일시"		, width: "15%", align: "center"},
			{key: "cm_username",	label: "등록사용자"	, width: "20%", align: "center"}
	    ]
	});
}

//시스템 콤보 가져오기
function getSysInfo() {
	var sysData =  new Object();
	var data = new Object();
	sysData = {
			UserId : userId,
			SecuYn : "N",
			SelMsg : "SEL",
			CloseYn : "N",
			ReqCd : ""
	}
	data = {
			requestType	: 'getSysInfo',
			sysData : sysData
	}
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

// 시스템 콤보 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
}

function getBaselinelist() {
	var data = {
		requestType : "getBaselinelist"
	}
	ajaxAsync('/webPage/ecmd/Cmd0400Servlet', data, 'json',successGetBaselinelist);
}

function successGetBaselinelist(data) {
	gridListData = data;
	gridList.setData(gridListData);
}

function setBaselinelist() {
	var etcData = {};
	var rsrccd = "";
	var title = $("#txtTitle").val();
	var sayu = $("#txtSayu").val();
	var syscd = getSelectedVal('cboSysCd').cm_syscd;
	
	rsrcUlInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if($('#chkRsrc'+addId).is(':checked')) {
			if(rsrccd.length > 0) rsrccd += ",";
			rsrccd += item.cm_micode;
		}
	});
	
	if(getSelectedIndex('cboSysCd') == 0) {
		dialog.alert("시스템을 선택바랍니다.");
		return;
	}
	
	if(rsrccd.length == 0) {
		dialog.alert("프로그램 종류를 선택바랍니다.");
		return;
	}
	
	if(title.length == 0) {
		dialog.alert("BaseLine 제목을 입력바랍니다.");
		return;
	}
	
	if(sayu.length == 0) {
		dialog.alert("BaseLine 사유를 입력바랍니다.");
		return;
	}
	
	etcData.title	= title
	etcData.sayu	= sayu
	etcData.syscd	= getSelectedVal('cboSysCd').cm_syscd;
	etcData.userid	= userId;
	etcData.rsrccd	= rsrccd;
	var data = {
			etcData : etcData,
		requestType : "setBaselinelist"
	}
	ajaxAsync('/webPage/ecmd/Cmd0400Servlet', data, 'json',successSetBaselinelist);
}

function updtBaselinelist() {
	var etcData = {};
	var title = $("#txtTitle").val();
	var sayu = $("#txtSayu").val();
	
	if(title.length == 0) {
		dialog.alert("BaseLine 제목을 입력바랍니다.");
		return;
	}
	
	if(sayu.length == 0) {
		dialog.alert("BaseLine 사유를 입력바랍니다.");
		return;
	}
	
	etcData.title	= title;
	etcData.sayu	= sayu;
	etcData.baseseq	= selRow.cd_baseseq;
	etcData.userid	= userId;
	var data = {
			etcData : etcData,
		requestType : "updtBaselinelist"
	}
	ajaxAsync('/webPage/ecmd/Cmd0400Servlet', data, 'json',successSetBaselinelist);
}

function successUpdtBaselinelist(data) {
	if(data != "OK") {
		dialog.alert("제목/사유 업데이트 작업중 오류가 발생하였습니다.");
	} else {
		dialog.alert("제목/사유 업데이트 작업이 정상적으로 완료되었습니다.");
		getBaselinelist();
	}
}

function successSetBaselinelist(data) {
	if(data != "OK") {
		dialog.alert("BaseLine 등록처리 중 오류가 발생하였습니다.");
	} else {
		dialog.alert("BaseLine 등록이 정상적으로 완료되었습니다.");
		getBaselinelist();
	}
}

function delBaselinelist() {
	var baselist = gridList.getList("selected");
	if(baselist.length < 1) return;
	$.each(baselist, function(i, val) {
		val.baseseq = val.cd_baseseq;
	});
	var data = {
		userid	 : userId,
		baselist : baselist,
		requestType : "delBaselinelist"
	}
	ajaxAsync('/webPage/ecmd/Cmd0400Servlet', data, 'json',successDelBaselinelist);
}

function successDelBaselinelist(data) {
	if(data != "OK") {
		dialog.alert("BaseLine 삭제처리 중 오류가 발생하였습니다.");
	} else {
		dialog.alert("BaseLine 삭제가 정상적으로 완료되었습니다.");
		getBaselinelist();
	}
}

function changeSysCbo() {
	if(getSelectedIndex('cboSysCd') > 0) {
		getProgInfo();
	}
}

function getProgInfo() {
	var data = {
			SysCd	: getSelectedVal('cboSysCd').cm_syscd,
		requestType : "getProgInfo"
	}
	ajaxAsync('/webPage/ecmd/Cmd0400Servlet', data, 'json',successGetProgInfo);
}

function successGetProgInfo(data) {
	rsrcUlInfoData = data;
	if(rsrcUlInfoData.length > 0) {
		makeRsrcUlList();
	}
}

function makeRsrcUlList() {
	$('#rsrcUlInfo').empty();
	var liStr = null;
	var addId = null;
	liStr  = '';
	rsrcUlInfoData.forEach(function(item, index) {
		if(index !== 0) {
			addId = item.cm_micode;
			liStr += '<li class="list-group-item">';
			liStr += '<div class="maring-3-top" style="padding: 5px 0;">';
			liStr += '	<input type="checkbox" class="checkbox-rsrc" id="chkRsrc'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
			liStr += '</div>';
			liStr += '</li>';
		}
	});
	$('#rsrcUlInfo').html(liStr);
	$('input.checkbox-rsrc').wCheck({
		theme: 'square-inset blue', 
		selector: 'checkmark', 
		highlightLabel: true
	});
}

function checkAllRgtCdList() {
	var checkSw = $('#chkAll').is(':checked');
	rsrcUlInfoData.forEach(function(item, index) {
		addId = item.cm_micode;
		if(checkSw) {
			$('#chkRsrc'+addId).wCheck('check', true);
		} else {
			$('#chkRsrc'+addId).wCheck('check', false);
		}
	});
}
