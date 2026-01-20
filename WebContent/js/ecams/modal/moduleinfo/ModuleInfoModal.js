/**
 * 실행모듈정보 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 * 
 */

var userId 		= window.parent.userId;		// 접속자 ID
var adminYN 	= window.parent.adminYN;	// 관리자여부
var sysCD       = window.parent.sysCD;      //시스템코드

var prgGrid		= new ax5.ui.grid();
var modGrid		= new ax5.ui.grid();

var RelatAry 	= window.parent.RelatAry;
var tmpObj1 	= window.parent.tmpObj1;

var prgGridData 	= [];
var fPrgGridData 	= [];
var modGridData	 	= [];
var fModGridData	= [];

var cboSysCdData	= [];
//var cboRsrcData		= [];

prgGrid.setConfig({
    target: $('[data-ax5grid="prgGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        },
        trStyleClass: function () {
    		if(this.item.status === 'ER'){
    			return "text-danger";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_rsrcname", 	label: "프로그램명",  		width: '35%', align: "left"},
        {key: "jawon",			label: "프로그램종류",  	width: '20%', align: "left"},
        {key: "cm_dirpath", 	label: "프로그램경로",  	width: '35%', align: "left"},
        {key: "cr_viewver", 	label: "버전",  			width: '10%'},
    ]
});

modGrid.setConfig({
    target: $('[data-ax5grid="modGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        },
        trStyleClass: function () {
    		if(this.item.status === 'ER'){
    			return "text-danger";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_rsrcname", 	label: "프로그램명",  		width: '35%', align: "left"},
        {key: "jawon",			label: "프로그램종류",  	width: '20%', align: "left"},
        {key: "cm_dirpath", 	label: "프로그램경로",  	width: '35%', align: "left"},
        {key: "cr_viewver", 	label: "버전",  			width: '10%'},
    ]
});

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});

//$('[data-ax5select="cboRsrc"]').ax5select({
//	options: []
//});

$('input.checkbox-module').wCheck({theme: 'square-classic blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	$('#chkNoPrg').wCheck('check', true);
	$('#chkNoMod').wCheck('check', true);
	
	getSysInfo();
	
	$('#cboSysCd').bind('change', function() {
		prgGridData	= [];
		prgGrid.setData([]);
		
		modGridData	= [];
		modGrid.setData([]);

		fPrgGridData = [];
		fModGridData = [];
//		cboRsrcData = [];
		
//		$('[data-ax5select="cboRsrc"]').ax5select({
//	        options: []
//		});
		if(getSelectedIndex('cboSysCd') < 1) {
			return;
		}
//		getRsrcList();
	});
	
//	$('#cboRsrc').bind('change', function() {
//		if(getSelectedIndex('cboRsrc') < 1) {
//			dialog.alert('프로그램종류를 선택하여 주시기 바랍니다.', function() {});
//			return;
//		}
//		
//		$('#btnQryPrg').trigger('click');
//		$('#btnQryMod').trigger('click');
//	});
	// 프로그램목록 미연결건 클릭
	$('#chkNoPrg').bind('click', function() {
		progFilter();
	});
	// 맵핑프로그램목록 미연결건 클릭
	$('#chkNoMod').bind('click', function() {
		modFilter();
	});
	// 프로그램 목록 엔터
	$('#txtPrg').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQryPrg').trigger('click');
		}
	});
	// 맵핑 프로그램 목록 엔터
	$('#txtMod').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('#btnQryMod').trigger('click');
		}
	});
	
	// 프로그램 목록 검색
	$('#btnQryPrg').bind('click', function() {
		getProgList();
	});
	// 맵핑 프로그램 검색
	$('#btnQryMod').bind('click', function() {
		getModList();
	});
	// 등록
	$('#btnReq').bind('click', function() {
		updtRelat();
	});
	// 닫기
	$('#btnClose').bind('click', function() {
		popClose();
	});
	
});

// 등록
function updtRelat() {
	var selInPrg = prgGrid.selectedDataIndexs;
	var selInMod = modGrid.selectedDataIndexs;
	var progList = [];
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}
	
	if(selInPrg.length === 0 ){
		dialog.alert('선택된 프로그램목록이 없습니다.\n프로그램을 선택한 후 처리하십시오.', function() {});
		return;
	}
	
	if(selInMod.length === 0 ){
		dialog.alert('선택된 맵핑프로그램목록이 없습니다.\n맵핑프로그램을 선택한 후 처리하십시오.', function() {});
		return;
	}
	
	selInPrg.forEach(function(selInP, index) {
		selInMod.forEach(function(selInM, index) {
			var prgItem = prgGrid.list[selInP];
			var modItem = modGrid.list[selInM];
			var tmpItem = new Object();
			tmpItem.cr_itemid = prgItem.cr_itemid;
			tmpItem.cr_prcitem = modItem.cr_itemid;
			tmpItem.check = 'true';
			progList.push(tmpItem);
			tmpItem = null;
			prgItem = null;
			modItem = null;
		});
	});
	
	var data = new Object();
	data = {
		UserId 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').value,
		progList 	: progList,			
		requestType	: 'updtRelat'
	}
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successUpdtRelat);
}
// 등록 완료
function successUpdtRelat(data) {
	if(data === '0' ) {
		dialog.alert('프로그램과 맵핑프로그램의 연결등록을 완료하였습니다.', function(){});
	} else {
		dialog.alert('프로그램과 맵핑프로그램의  연결등록처리에 실패하였습니다.', function(){});
	}
	
	$('#btnQryPrg').trigger('click');
	$('#btnQryMod').trigger('click');
}

// 모듈 미연결건 필터
function modFilter() {
	var checkSw = $('#chkNoMod').is(':checked');
	fModGridData = [];
	if(checkSw) {
		modGridData.forEach(function(item, index) {
			if(item.srccnt === '0') {
				fModGridData.push(item);
			}
		});
	} else {
		fModGridData = modGridData;
	}
	
	$(".loding-div").remove();
	modGrid.setData(fModGridData);
}

// 프로그램 미연결건 필터
function progFilter() {
	var checkSw = $('#chkNoPrg').is(':checked');
	fPrgGridData = [];
	
	if(checkSw) {
		prgGridData.forEach(function(item, index) {
			if(item.modcnt === '0') {
				fPrgGridData.push(item);
			}
		});
	} else {
		fPrgGridData = prgGridData;
	}
	
	$(".loding-div").remove();
	prgGrid.setData(fPrgGridData);
}

// 프로그램 목록 가져오기
function getProgList() {

	prgGridData	= [];
	prgGrid.setData([]);	
	fPrgGridData 	= [];
	
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}
	
//	if(getSelectedIndex('cboRsrc') < 1) {
//		dialog.alert('프로그램 종류를 선택하여 주십시오.', function() {});
//		return;
//	}
	
	var data = new Object();
	data = {
		UserId 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').value,
		ProgId 		: $('#txtPrg').val().trim(),
		subSw 		: false,
//		rsrcCd 		: getSelectedVal('cboRsrc').value,
		requestType	: 'getProgList'
	}
	
	$('[data-ax5grid="prgGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetProgList);
}

// 모듈 목록 가져오기
function getModList() {
	
	modGridData	= [];
	modGrid.setData([]);

	fModGridData	= [];
	
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주십시오.', function() {});
		return;
	}
	
//	if(getSelectedIndex('cboRsrc') < 1) {
//		dialog.alert('프로그램 종류를 선택하여 주십시오.', function() {});
//		return;
//	}
	
	var data = new Object();
	data = {
		UserId 		: userId,
		SysCd 		: getSelectedVal('cboSysCd').value,
		ProgId 		: $('#txtMod').val().trim(),
		subSw 		: false,
//		rsrcCd 		: getSelectedVal('cboRsrc').samersrc,
		requestType	: 'getModList'
	}
	$('[data-ax5grid="modGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetModList);
}
// 모듈 목록 가져오기 완료
function successGetModList(data) {
	modGridData = data;
	modFilter();
}

// 프로그램 목록 가져오기 완료
function successGetProgList(data) {
	prgGridData =data;
	progFilter();
}

// 프로그램 종류 리스트 가져오기
function getRsrcList() {
	var data = new Object();
	data = {
		SysCd 		: getSelectedVal('cboSysCd').value,
		SelMsg 		: 'SEL',
		requestType	: 'getRsrcList'
	}
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetRsrcList);
}
// 프로그램 종류 리스트 가져오기 완료
function successGetRsrcList(data) {
	cboRsrcData = data;
	
	$('[data-ax5select="cboRsrc"]').ax5select({
      options: injectCboDataToArr(cboRsrcData, 'cm_micode' , 'cm_codename')
	});
	
	if(cboRsrcData.length === 2) {
		$('[data-ax5select="cboRsrc"]').ax5select('setValue', cboRsrcData[1].cm_micode, true);
		$('#cboRsrc').trigger('change');
	}
}

//시스템 콤보 가져오기
function getSysInfo() {
	var data = new Object();
	data = {
		UserId 		: userId,
		SecuYn 		: 'Y',
		SelMsg 		: 'SEL',
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetSysInfo);
}
// 시스템 콤보 가져오기 완료
function successGetSysInfo(data) {
	cboSysCdData = data;
	
	cboSysCdData = cboSysCdData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) === '1') return false;
		else return true;
	});
	$('[data-ax5select="cboSysCd"]').ax5select({
      options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if (sysCD != null && sysCD != '') {
		$('[data-ax5select="cboSysCd"]').ax5select('setValue', sysCD, true);
		$('#cboSysCd').trigger('change');
	}
}

function popClose(){
	window.parent.moduleInfoModal.close();
}