/**
 * 실행모듈정보 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 * 
 */


var parentvar = window.parent.mappingObj;

var UserId = parentvar.UserId;
var AcptNo = parentvar.AcptNo;
var SysCd = parentvar.SysCd;
var lstModule_dp = parentvar.lstModule_dp;

var mappingGrid		= new ax5.ui.grid();
var mappingGridData = [];
var cboJobData		= [];
var lstSubprogData 	= [];
var lstModuleData 	= [];

mappingGrid.setConfig({
    target: $('[data-ax5grid="mappingGrid"]'),
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
        	dblClickDirGrid(this.dindex);
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
        {key: "basersrcname", 	label: "기준프로그램명",  	width: '35%', align: "left"},
        {key: "cm_codename",	label: "서브프로그램종류",  	width: '20%', align: "left"},
        {key: "subrsrcname", 	label: "서브프로그램명",  	width: '35%', align: "left"},
    ]
});

$('[data-ax5select="cboJob"]').ax5select({
    options: []
});


$(document).ready(function() {
	
	if (AcptNo != null && AcptNo !="") {
		$('#btnClose').text('닫기');
		$('#txtAcptno').css('display', '');
		$('#txtAcptno').val('신청번호 :' +AcptNo);
	} else {
		$('#btnClose').text('취소');
		$('#txtAcptno').css('display', 'none');
	}
	getJobInfo();
	lstModuleData = lstModule_dp;
	makeModule();
	
	$('#cboJob').bind('change', function() {
		lstSubprogData = [];
		$('#lstSubprog').empty();
		var data = {
			syscd 	: SysCd,			
			jobcd 	: getSelectedVal('cboJob').cm_jobcd,			
			requestType	: 'getSubList'
		}
		ajaxAsync('/webPage/ecmr/Cmr0270Servlet', data, 'json',successGetSubList);
		
	});
	// 등록
	$('#btnReq').bind('click', function() {
		if(mappingGridData.length==0){
			confirmDialog.setConfig({
		        title: "sub프로그램확인",
		        theme: "info"
		    });
			confirmDialog.confirm({
				msg: '등록된 sub프로그램이 없습니다. 진행하시겠습니까?',
			}, function(){
				if(this.key === 'ok') {
					window.parent.updateModFlag = true;
					popClose();
				}
			});
		} else {
			addProgFiles = mappingGridData;
			window.parent.updateModFlag = true;
			popClose();
		}
		
		
	});
	// 닫기
	$('#btnClose').bind('click', function() {
		window.parent.updateModFlag = false;
		popClose();
	});
	
	// 추가
	$('#btnAdd').bind('click', function() {
		var i = 0;
		var j = 0;
		var tmpObj = {};
		var tmpArr = [];
		
		var checkMod = [];
		var checkSub = [];
		
		lstModuleData.forEach(function(item, index) {
			addId = item.cr_itemid;
			if($('#chkMod'+addId).is(':checked')) {
				checkMod.push(item);
			}
		});
		
		if(checkMod.length === 0 ) { 
			dialog.alert('기준프로그램을 선택해 주십시오.', function(){});
			return;
		}
		
		
		lstSubprogData.forEach(function(item, index) {
			addId = item.cr_itemid;
			if($('#chkSub'+addId).is(':checked')) {
				checkSub.push(item);
			}
		});
		
		if(checkSub.length === 0 ) { 
			dialog.alert('Sub프로그램을 선택해 주십시오.', function(){});
			return;
		}
		
		//DB에 저장할 정보 조합
		for(i =0; i<checkMod.length; i++){
  			for(j =0; j<checkSub.length; j++){
				tmpObj = {};
				tmpObj.cr_baseitem = checkMod[i].cr_baseitem;
				tmpObj.basersrcname = checkMod[i].basersrcname;
				tmpObj.cr_itemid = checkSub[j].cr_itemid;
				tmpObj.subrsrcname = checkSub[j].cr_rsrcname;
				tmpObj.cr_dsncd = checkSub[j].cr_dsncd;
				tmpObj.cm_codename = checkSub[j].cm_codename;
				tmpObj.checkbox = true;
				tmpObj.visible = true;
				tmpObj.check = true;
				tmpArr.push(tmpObj);
				tmpObj = null;
			}
		}
		
		//맵핑정보 리스트에 업데이트 할 정보만 추출하기
		if (mappingGridData.length > 0){
	  		for(i =0; i<mappingGridData.length; i++){
	  			for(j =0; j<tmpArr.length; j++){
	  				//기준프로그램과 sub프로그램이 동일하면 추가할 리스트에서 삭제처리
	  				if (mappingGridData[i].cr_baseitem == tmpArr[j].cr_baseitem
	  				    && mappingGridData[i].cr_itemid == tmpArr[j].cr_itemid){
	  						tmpArr.splice(1,j--);
	  						break;
	  				}
	  			}
	  		}
		}
		
		
		//새로운 맵핑 정보를 하단 그리드에 추가 하는 작업
		for(j =0; j<tmpArr.length; j++){
			mappingGridData.push(tmpArr[j]);
		}
		mappingGrid.setData(mappingGridData);
	})
	
});

function successGetSubList(data) {
	lstSubprogData = data;
	makeSubProg();
}

function makeModule() {
	$('#lstModule').empty();
	var liStr = null;
	var addId = null;
	var dutyT1 = false;
	var dutyT2 = false;
	liStr  = '';
	lstModuleData.forEach(function(item, index) {
		addId = item.cr_itemid;
		liStr += '<li class="list-group-item">';
		if(item.checkbox !== undefined && item.checkbox === 'true') {
			liStr += '	<input type="checkbox" class="checkbox-mod" id="chkMod'+addId+'" data-label="'+item.cr_rsrcname+'"  value="'+addId+'" checked="checked"/>';
		} else {
			liStr += '	<input type="checkbox" class="checkbox-mod" id="chkMod'+addId+'" data-label="'+item.cr_rsrcname+'"  value="'+addId+'" />';
		}
		liStr += '</li>';
	});
	
	$('#lstModule').html(liStr);
	
	$('input.checkbox-mod').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}
function makeSubProg() {
	$('#lstSubprog').empty();
	var liStr = null;
	var addId = null;
	var dutyT1 = false;
	var dutyT2 = false;
	liStr  = '';
	lstSubprogData.forEach(function(item, index) {
		addId = item.cr_itemid;
		liStr += '<li class="list-group-item">';
		if(item.checkbox !== undefined && item.checkbox === 'true') {
			liStr += '	<input type="checkbox" class="checkbox-sub" id="chkSub'+addId+'" data-label="'+item.cr_rsrcname+'"  value="'+addId+'" checked="checked"/>';
		} else {
			liStr += '	<input type="checkbox" class="checkbox-sub" id="chkSub'+addId+'" data-label="'+item.cr_rsrcname+'"  value="'+addId+'" />';
		}
		liStr += '</li>';
	});
	
	$('#lstSubprog').html(liStr);
	
	$('input.checkbox-sub').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}


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
		dialog.alert('선택된 프로그램목로기 없습ㅂ니다. 프로그램을 선택한 후 처리하십시오.', function() {});
		return;
	}
	
	if(selInMod.length === 0 ){
		dialog.alert('선택된 맵핑프로그램목록이 없습니다. 맵핑프로그램을 선택한 후 처리하십시오.', function() {});
		return;
	}
	
	selInPrg.forEach(function(selInP, index) {
		selInMod.forEach(function(selInM, index) {
			var prgItem = prgGrid.list[selInP];
			var modItem = modGrid.list[selInM];
			var tmpItem = new Object();
			tmpItem.cr_itemid 	= prgItem.cr_itemid;
			tmpItem.cr_prcitem 	= modItem.cr_itemid;
			tmpItem.check 		= 'true';
			progList.push(tmpItem);
			tmpItem = null;
			prgItem = null;
			modItem = null;
		});
	});
	
	var data = new Object();
	data = {
		tmpInfo		: {
			UserId 		: userId,
			SysCd 		: getSelectedVal('cboSysCd').value,
		},
		progList 	: progList,			
		requestType	: 'updtRelat'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successUpdtRelat);
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
	
	if(getSelectedIndex('cboRsrc') < 1) {
		dialog.alert('프로그램 종류를 선택하여 주십시오.', function() {});
		return;
	}
	
	var data = new Object();
	data = {
		tmpInfo		: {			
			UserId 		: userId,
			SysCd 		: getSelectedVal('cboSysCd').value,
			ProgId 		: $('#txtPrg').val().trim()
//			subSw 		: false,
//			rsrcCd 		: getSelectedVal('cboRsrc').value
		},
		requestType	: 'getProgList'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successGetProgList);
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
	
	if(getSelectedIndex('cboRsrc') < 1) {
		dialog.alert('프로그램 종류를 선택하여 주십시오.', function() {});
		return;
	}
	
	var data = new Object();
	data = {
		tmpInfo		: {			
			UserId 		: userId,
			SysCd 		: getSelectedVal('cboSysCd').value,
			ProgId 		: $('#txtMod').val().trim()
//			subSw 		: false,
//			rsrcCd 		: getSelectedVal('cboRsrc').samersrc
		},
		requestType	: 'getModList'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successGetModList);
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
		tmpInfo		: {			
			SysCd 		: getSelectedVal('cboSysCd').value,
			SelMsg 		: 'SEL'
		},
		requestType	: 'getRsrcList'
	}
	ajaxAsync('/webPage/program/ModuleInfo', data, 'json',successGetRsrcList);
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

//업무정보 콤보 가져오기
function getJobInfo() {
	
	var data = {
		UserID : UserId,
		SysCd : SysCd,
		SecuYn : "Y",
		CloseYn : "N",
		SelMsg : "",
		sortCd : "",
		requestType	: 'getJobInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetJobInfo);
}

// 업무정보
function successGetJobInfo(data) {
	cboJobData = data;
	$('[data-ax5select="cboJob"]').ax5select({
      options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'jobcdname')
	});
	$('#cboJob').trigger('change');
}


function popClose(){
	window.parent.subPrgModal.close();
}