var userName 	 	= window.parent.userName;
var userId 		 	= window.parent.userId;
var adminYN 		= window.parent.adminYN;
var rgtList         = window.parent.rgtList;
var pItemId         = window.parent.itemId;

var firstGrid 		= new ax5.ui.grid();
var secondGrid 		= new ax5.ui.grid();
var thirdGrid 		= new ax5.ui.grid();

var firstGridData = [];
var secondGridData = [];
var thirdGridData = [];

var tmpInfo = null;
var tmpInfoData = null;

var cboSysCdData = [];
var strSysCd = '';
var strItemId = '';


$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});

$('input:radio[name=radio]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

createFirstGrid();
createSecondGrid();
createThirdGrid();

$(document).ready(function() {
	if(pItemId != null && pItemId != '') {
		$('#btnExit').css('visibility', 'visible');
	}else {
		$('#btnExit').css('visibility', 'hidden');
	}
	
	$('#btnDel').prop('disabled', true);
	getSysInfo();
	
	$('#cboSysCd').bind('change',function() {
		cboSysCd_change();
	});
	
	//검색1
	$('#btnFind').bind('click',function() {
		btnFind_Click();
	});
	
	$('#txtPrgName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnFind').trigger('click');
		}
	});
	
	//검색2
	$('#btnFind2').bind('click',function() {
		btnFind2_Click();
	});
	
	$('#txtPrgName2').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnFind2').trigger('click');
		}
	});
	
	//검색3
	$('#btnFind3').bind('click',function() {
		btnFind3_Click();
	});
	
	$('#txtPrgName3').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnFind3').trigger('click');
		}
	});
	
	//등록
	$('#btnReg').bind('click',function() {
		btnReg_Click();
	});
	
	//폐기
	$('#btnDel').bind('click',function() {
		btnDel_Click();
	});
	
	//엑셀등록
	$('#btnExl').bind('click',function() {
		thirdGrid.exportExcel('실행모듈정보.xls');
	});
	
	//닫기
	$('#btnExit').bind('click',function() {
		btnExit_Click();
	});
});

function createFirstGrid() {
	firstGrid.setConfig({
	    target: $('[data-ax5grid="firstGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: true,
	    multipleSelect: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	//this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	    	{key: "cr_rsrcname", 	label: "프로그램명",		width: '35%', 	align: "left"},
	        {key: "jawon", 			label: "프로그램종류",	  	width: '15%',	align: "left"},
	        {key: "cm_dirpath", 	label: "프로그램경로",   	width: '40%',	align: "left"},
	        {key: "cr_lstver", 		label: "버전",  			width: '10%',	align: "left"}
	    ]
	});
}

function createSecondGrid() {
	secondGrid.setConfig({
	    target: $('[data-ax5grid="secondGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: true,
	    multipleSelect: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	//this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
    		{key: "cr_rsrcname", 	label: "프로그램명",		width: '35%', 	align: "left"},
	        {key: "jawon", 			label: "프로그램종류",	  	width: '15%',	align: "left"},
	        {key: "cm_dirpath", 	label: "프로그램경로",   	width: '40%',	align: "left"},
	        {key: "cr_lstver", 		label: "버전",  			width: '10%',	align: "left"}
	    ]
	});
}

function createThirdGrid() {
	thirdGrid.setConfig({
	    target: $('[data-ax5grid="thirdGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: true,
	    multipleSelect: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	//this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	    	onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	    	{key: "cr_rsrcname", 	label: "프로그램명",		width: '15%', 	align: "left"},
	        {key: "jawon", 			label: "프로그램종류",	  	width: '10%',	align: "left"},
	        {key: "cm_dirpath", 	label: "프로그램경로",   	width: '25%',	align: "left"},
	        {key: "cr_rsrcname2", 	label: "모듈명",  		width: '15%',	align: "left"},
	        {key: "jawon2", 		label: "모듈종류",  		width: '10%',	align: "left"},
	        {key: "cm_dirpath2", 	label: "모듈경로",  		width: '25%',	align: "left"}
	    ]
	});
}

function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId : userId,
		SecuYn : "Y",
		SelMsg : "SEL",
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpInfoData, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysCdData = data;
	cboSysCdData = cboSysCdData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) === '1') return false;
		else return true;
	});
	
	if(cboSysCdData.length > 0) {
		$('#btnDel').prop('disabled', false);
	}
	
	$('[data-ax5select="cboSysCd"]').ax5select({
		options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
	if(getSelectedIndex('cboSysCd') > -1) {
		cboSysCd_change();
	}
}

function cboSysCd_change() {
	firstGridData = [];
	secondGridData = [];
	thirdGridData = [];

	firstGrid.setData([]);
	secondGrid.setData([]);
	thirdGrid.setData([]);
	if(getSelectedIndex('cboSysCd') < 1) {
		return;
	}
	var checkVal = $(':input:radio[name=radio]:checked').val(); //P, M, A
	
	var tmpData = new Object();
	tmpData = {
		UserId : userId,
		SysCd :  getSelectedVal('cboSysCd').cm_syscd,
		GbnCd : checkVal,
		ProgId :  $('#txtPrgName3').val(),
		requestType : 'getRelatList'
	} 
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpData, 'json', successGetRelatList);
}

function btnFind_Click() {
	$('#txtPrgName').val($('#txtPrgName').val().trim());
	if(getSelectedIndex('cboSysCd') > 0) {
		var tmpData = new Object();
		tmpData = {
			UserId : userId,
			SysCd :  getSelectedVal('cboSysCd').cm_syscd,
			ProgId : $('#txtPrgName').val(),
			requestType : 'getProgList'
		} 
		$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpData, 'json', successGetProgList);
	}else {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
}

function successGetProgList(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
	if (data.length < 1){
		dialog.alert("검색결과가 없습니다.");
		return;
	}
}
function btnFind2_Click() {
	$('#txtPrgName2').val($('#txtPrgName2').val().trim());
	if(getSelectedIndex('cboSysCd') > 0) {
		var tmpData = new Object();
		tmpData = {
			UserId : userId,
			SysCd : getSelectedVal('cboSysCd').cm_syscd,
			ProgId : $('#txtPrgName2').val(),
			requestType : 'getModList'
		} 
		
		$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpData, 'json', successGetModList);
	}else {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
}

function successGetModList(data) {
	$(".loding-div").remove();
	secondGridData = data;
	secondGrid.setData(secondGridData);
	if (data.length < 1){
		dialog.alert("검색결과가 없습니다.");
		return;
	}
}

function btnFind3_Click() {
	$('#txtPrgName3').val($('#txtPrgName3').val().trim());
	if(getSelectedIndex('cboSysCd') > 0) {
		var checkVal = $(':input:radio[name=radio]:checked').val(); //P, M, A
		var tmpData = new Object();
		tmpData = {
			UserId : userId,
			SysCd :  getSelectedVal('cboSysCd').cm_syscd,
			GbnCd : checkVal,
			ProgId :  $('#txtPrgName3').val(),
			requestType : 'getRelatList'
		} 
		$('[data-ax5grid="thirdGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpData, 'json', successGetRelatList);
	}else {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
}

function successGetRelatList(data) {
	$(".loding-div").remove();
	thirdGridData = data;
	thirdGrid.setData(thirdGridData);
	if (data.length < 1){
		dialog.alert("검색결과가 없습니다.");
		return;
	}
}

function btnReg_Click() {
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
	
	
	var checkedPrgItem = firstGrid.getList("selected");
	if(checkedPrgItem.length == 0) {
		dialog.alert('선택된 프로그램목록이 없습니다. \n프로그램을 선택하신 후 처리하십시오.');
		return;
	}
	
	var checkedModItem = secondGrid.getList("selected");
	if(checkedModItem.length == 0) {
		dialog.alert('선택된 모듈목록이 없습니다. 모듈을 선택한 후 처리하십시오.');
		return;
	}
	
	tmpInfoData = [];
	for(var i=0; i<checkedPrgItem.length; i++) {
		for(var j=0; j<checkedModItem.length; j++) {
			tmpInfo = new Object();
			tmpInfo.cr_itemid = checkedPrgItem[i].cr_itemid;
			tmpInfo.cr_prcitem = checkedModItem[j].cr_itemid;
			tmpInfo.check = 'true';
			tmpInfoData.push(tmpInfo);
		}
	}
	
	//Cmd0900.updtRelat(strUserId,cboSysCd.selectedItem.cm_syscd,tmpArr.toArray());
	var tmpData = new Object();
	tmpData = {
		UserId : userId,
		SysCd : getSelectedVal('cboSysCd').cm_syscd,
		progList : tmpInfoData,
		requestType : 'updtRelat'
	} 
	console.log('updtRelat=',tmpData);
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpData, 'json', successUpdtRelat);
}

function successUpdtRelat(data) {
	console.log('successUpdtRelat=',data);
	if(data == '0') {
		dialog.alert('프로그램과 모듈의 연결등록을 완료하였습니다.');
	}else {
		dialog.alert('프로그램과 모듈의 연결등록처리에 실패하였습니다.');
	}
	
	btnFind_Click();
	btnFind2_Click();
	btnFind3_Click();
}

function btnDel_Click() {
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.');
		return;
	}
	
	var checkedItem = thirdGrid.getList("selected");
	if(checkedItem.length == 0) {
		dialog.alert('선택된 프로그램목록이 없습니다. \n목록에서 해제 할 대상을 선택한 후 처리하십시오.');
		return;
	}
	
	tmpInfoData = [];
	for (var i=0; i<checkedItem.length; i++) {
		tmpInfo = new Object();
		tmpInfo.cr_itemid = checkedItem[i].cd_itemid;
		tmpInfo.cr_prcitem = checkedItem[i].cd_prcitem;
		tmpInfoData.push(tmpInfo);
	}
	
	//Cmd0900.delRelat(strUserId,tmpArr.toArray());
	var tmpData = new Object();
	tmpData = {
		UserId : userId,
		progList : tmpInfoData,
		requestType : 'delRelat'
	} 
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', tmpData, 'json', successDelRelat);
}

function successDelRelat(data) {
	if(data == '0') {
		dialog.alert('프로그램과 모듈의 연결 해제를 완료하였습니다.');
	}else {
		dialog.alert('프로그램과 모듈의 연결해제처리에 실패하였습니다.');
	}
	
	btnFind_Click();
	btnFind2_Click();
	btnFind3_Click();
}

function btnExit_Click() {
	window.parent.moduleInfoModal.close();
}