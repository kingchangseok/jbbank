/**
 * 빌드/릴리즈유형등록 화면 기능정의
 *
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-18
 *
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var editScriptGrid		= new ax5.ui.grid();
var scriptGrid			= new ax5.ui.grid();

var editScriptGridData 	= [];
var scriptGridData		= [];

var cboBldGbnData	= [];
var cboBldCdData	= [];
var fCboBldCdData	= [];
var cboBldGbnBData	= [];

var tmpPath			= '';
var uploadJspFile	= '';
var newBldCdValue	= null;

editScriptGrid.setConfig({
    target: $('[data-ax5grid="editScriptGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: false,
    showLineNumber: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            clickEditGrid(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        /*{key: "cm_seq", 	label: "순서",  		width: '5%' },*/
        {key: "cm_cmdname", label: "수행명령",  	width: '95%', align: 'left'},
    ]
});

scriptGrid.setConfig({
    target: $('[data-ax5grid="scriptGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_codename", 	label: "빌드패턴",  	width: '20%', align: 'center'},
        {key: "cm_seq", 		label: "순서",  		width: '5%',  align: 'center' },
        {key: "cm_cmdname", 	label: "수행명령",  	width: '75%', align: 'left'},
    ]
});

$('[data-ax5select="cboBldGbn"]').ax5select({
    options: []
});

$('[data-ax5select="cboBldCd"]').ax5select({
    options: []
});

$('[data-ax5select="cboBldGbnB"]').ax5select({
    options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {

	getCodeInfo();
	getCAMSDir();

	$('#cboBldGbnB').bind('change', function() {
		$('#btnQry').trigger('click');
	});

	// 등록구분 변경
	$('#cboBldGbn').bind('change', function() {
		cboBldGbn_Change();
	});

	// 유형구분조회
	$('#txtBoldCdFilter').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			var txtBoldCdFilter = $('#txtBoldCdFilter').val().trim();

			for (var i=0; i<fCboBldCdData.length; i++) {
				if(fCboBldCdData[i].cm_micode == txtBoldCdFilter || fCboBldCdData[i].cm_codename.indexOf(txtBoldCdFilter)>=0){
					$('[data-ax5select="cboBldCd"]').ax5select('setValue', fCboBldCdData[i].cm_micode, true);
					cboBldCd_Change();
					break;
				}
			}
		}
	});
	
	// 유형구분 변경
	$('#cboBldCd').bind('change', function() {
		cboBldCd_Change();
	});

	// 유형삭제 클릭
	$('#btnDel').bind('click', function() {
		delScript();
	});

	// 새이름저장 클릭
	$('#btnCopy').bind('click', function() {
		copyScript();
	});
	
	//$('#btnCopy').hide();
	
	// 추가 클릭
	$('#btnScr').bind('click', function() {
		addGrid();
	});
	
	// 수정 클릭
	$('#btnEdit').bind('click', function() {
		addEditGrid();
	});

	// 제거 클릭
	$('#btnDelScr').bind('click', function() {
		deleteEditGrid();
	});

	// 저장 클릭
	$('#btnReq').bind('click', function() {
		insertScript();
	});

	// 조회 클릭
	$('#btnQry').bind('click', function() {
		getExistScript();
	});

	// 수행명령 엔터
	$('#txtOrder').bind('keypress', function(event) {
		if(event.keyCode==13) {
			$('#btnQry').trigger('click');
		}
	});

	// 엑셀불러오기
	$('#btnScriptLoad').bind('click', function() {
		$('#excelFile').trigger('click');
	});
	
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
	
	//엑셀저장
	$('#btnScriptSave').bind('click', function() {
		var excelData = [];
		for(var i=0; i<editScriptGridData.length; i++) {
			var excelObj = new Object();
			excelObj.cm_cmdname = editScriptGridData[i].cm_cmdname;
			excelObj.cm_errword = $('#txtErrMsg').val();
			if($('#chkLocal').is(':checked')) {
				excelObj.cm_runtype = 'L';
			}else {
				excelObj.cm_runtype = 'R';
			}
			excelData.push(excelObj);
		}
		excelData.unshift({cm_cmdname: '수행명령', cm_errword: '오류MSG', cm_runtype: 'RunType(R:L)'});
			
		var headerDef = new  Array();
		headerDef.push("cm_cmdname");
		headerDef.push("cm_errword");
		headerDef.push("cm_runtype");

		var xlsName = '빌드릴리즈정보';
		var filePath = tmpPath + "/" + xlsName + ".xls";
		
		var tmpData = {
			headerDef : headerDef,
			filePath : filePath,
			excelData : excelData,
			requestType : 'setExcel'
		}
		var ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
		if (ajaxReturnData == null || ajaxReturnData == '' || ajaxReturnData == undefined) return;
		if (typeof(ajaxReturnData) == 'string' && ajaxReturnData.indexOf('ERR')>-1) {
			dialog.alert(ajaxReturnData);
			return;
		}
		successExcelExport(ajaxReturnData, xlsName);
	});
	
	editScriptGrid.setConfig();
	scriptGrid.setConfig();
});

function getCAMSDir() {
	var tmpInfoData = {
		pCode : '99,F2',
		requestType : 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpInfoData, 'json', successeCAMSDir);
}

function successeCAMSDir(data) {
	data = data.filter(function(data) {
		if(data.cm_pathcd == '99') tmpPath = data.cm_path;				//엑셀 파일 업로드시 파일 올릴 경로 
		else if(data.cm_pathcd == 'F2') uploadJspFile = data.cm_path;	//업로드할때 사용할 JSP 파일명 가져오기
	});
}


function cboBldGbn_Change() {
	var selGbn = getSelectedVal('cboBldGbn').value;
	fCboBldCdData = [];
	cboBldCdData.forEach(function(bldCdItem, index) {
		//if(bldCdItem.cm_bldgbn == selGbn) fCboBldCdData.push(bldCdItem);
		if(bldCdItem.cm_bldgbn == selGbn) fCboBldCdData.push(bldCdItem);
 	});

	
	if(fCboBldCdData[0].cm_micode == '00') {
		var FIELD = 'cm_micode';
		if(fCboBldCdData[0].cm_micode === undefined && fCboBldCdData.cm_bldcd !== undefined){
			FIELD = 'cm_bldcd';
		}
		//다음 코드 계산
		var FIRST = "0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ";
		var SECOND = "0123456789";
		var max = -1;
		var nextCd = '00';
		
		for (var i=0;fCboBldCdData.length>i;i++){
			var cd=fCboBldCdData[i][FIELD]
			if(!cd || cd ==='00') continue;
			var a = cd.charAt(0);
			var b = cd.charAt(1);
			var idx = FIRST.indexOf(a) * SECOND.length + SECOND.indexOf(b);
			if(idx > max) max = idx;
			
			if(max >= 0){
				var nextIdx = max +1;
				nextCd = FIRST.charAt(Math.floor(nextIdx /SECOND.length))+SECOND.charAt(nextIdx %SECOND.length);
			}
		}

		fCboBldCdData[0][FIELD] = nextCd;
		if(fCboBldCdData[0].cm_codename !== undefined){
			fCboBldCdData[0].cm_codename = '['+nextCd+'] 유형신규등록';
			//fCboBldCdData[0].cm_codename =  '유형신규등록';
		}	
	}
			

	$('[data-ax5select="cboBldCd"]').ax5select({
        options: injectCboDataToArr(fCboBldCdData, 'cm_micode' , 'cm_codename')
   	});
	

	if(fCboBldCdData.length > 0 ){
		if(newBldCdValue !== null) {
			$('[data-ax5select="cboBldCd"]').ax5select('setValue',newBldCd,true);
			newBldCdValue = null;
		}
		 $('#cboBldCd').trigger('change');
	}
}

// 등록구분 변경
function cboBldGbn_Change_1016() {
	var selGbn = getSelectedVal('cboBldGbn').value;
	fCboBldCdData = [];
	cboBldCdData.forEach(function(bldCdItem, index) {
		//if(bldCdItem.cm_bldgbn == selGbn) fCboBldCdData.push(bldCdItem);
		if(bldCdItem.cm_bldgbn == selGbn) fCboBldCdData.push(bldCdItem);
 	});

	if(fCboBldCdData.length === 0 ) {
		var newBldCd 	= new Object();
		newBldCd.value 	= 0;
		newBldCd.cm_codename = '유형신규등록';
		newBldCd.cm_micode = '00';
		newBldCd.cm_bldgbn = selGbn;
		fCboBldCdData.push(newBldCd);
	}
	
	if(fCboBldCdData[0].cm_micode == '00') {
		var cnt = 0;
		var newBldCd = new Object();
		var i = 0;
		var j = 0;
		var k = 0;
		var bldcd = '';
		var chgbldcd ='';
		var firstbldcd = '';
		var bldcdlist = '0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ;'
		
		var findSw = false;
		
		for (j=0; bldcdlist.length>j; j++) {
			for (k=0;bldcdlist.length>k; k++){
				chgbldcd = bldcdlist.substr(j,1)+bldcdlist.substr(k,1);
				if (chgbldcd == '00') continue;
				findSw = false;
				for (i=1;fCboBldCdData.length>i;i++){
					if (chgbldcd == fCboBldCdData[i].cm_micode) {
						findSw = true;
						break;
					}
				}
				if (!findSw) break;
			}
			if (!findSw) break;
			newBldCd = fCboBldCdData[0];
			newBldCd.cm_micode = chgbldcd;
			newBldCd.cm_codename = '['+chgbldcd+'] 유형신규등록';
			fCboBldCdData.splice(0,1,newBldCd);
		}

		$('[data-ax5select="cboBldCd"]').ax5select({
	        options: injectCboDataToArr(fCboBldCdData, 'cm_micode' , 'cm_codename')
	   	});
		

		if(fCboBldCdData.length > 0 ){
			if(newBldCdValue !== null) {
				$('[data-ax5select="cboBldCd"]').ax5select('setValue',newBldCd,true);
				newBldCdValue = null;
			}
			 $('#cboBldCd').trigger('change');
		}
	}
}
function cboBldGbn_Change_old() {
	var selGbn = getSelectedVal('cboBldGbn').value;
	$('[data-ax5select="cboBldGbnB"]').ax5select('setValue',selGbn,true);
	$('#btnQry').trigger('click');
	
	console.log('[cboBldGbn_Change] selGbn='+selGbn);
	console.log('[cboBldGbn_Change] bldCdItem=',cboBldCdData);
	
	fCboBldCdData = [];
	cboBldCdData.forEach(function(bldCdItem, index) {
//		if(bldCdItem.cm_micode == '00') fCboBldCdData.push(bldCdItem);
//		else {
			if(bldCdItem.cm_bldgbn == selGbn) {
				fCboBldCdData.push(bldCdItem);
			} 
//		}
	});

	if(fCboBldCdData.length === 0 ) {
		var newBldCd 	= new Object();
		newBldCd.value 	= 0;
		newBldCd.cm_codename = '유형신규등록';
		newBldCd.cm_micode = '00';
		newBldCd.cm_bldgbn = selGbn;
		fCboBldCdData.push(newBldCd);
	} else {
		if(fCboBldCdData[0].cm_micode == '00') {
			var cnt = 0;
			var newBldCd = new Object();
			var i = 0;
			for(i=1; i<fCboBldCdData.length; i++) {
				++cnt;
				if(Number(fCboBldCdData[i].cm_micode) != cnt) break;
			}
	
			if(i >= fCboBldCdData.length) ++cnt;
	
			newBldCd = fCboBldCdData[0];
			if(cnt < 10) newBldCd.cm_micode = '0' + String(cnt);
			//else if (cnt < 100) newBldCd.cm_micode = String(cnt);
			else newBldCd.cm_micode = '0' + String(cnt);
	
			fCboBldCdData.splice(0,1,newBldCd);
		}
	}
	$('[data-ax5select="cboBldCd"]').ax5select({
        options: injectCboDataToArr(fCboBldCdData, 'cm_micode' , 'cm_codename')
   	});

	if(fCboBldCdData.length > 0 ) $('#cboBldCd').trigger('change');
}

// 유형구분 변경
function cboBldCd_Change() {
	var bldCdVal 	= getSelectedVal('cboBldCd').cm_micode;
	var bldGbnVal 	= getSelectedVal('cboBldGbn').value;
	var bldCdSelIn	= getSelectedIndex('cboBldCd');

	$('#btnDel').css("visibility", 'hidden');
	$('#btnCopy').css("visibility", 'hidden');

	if(newBldCdValue != null && newBldCdValue != '') {
		$('[data-ax5select="cboBldCd"]').ax5select('setValue',newBldCdValue ,true);
		bldCdSelIn		= getSelectedIndex('cboBldCd');
		bldCdVal 		= getSelectedVal('cboBldCd').cm_micode;
		newBldCdValue 	= null;
	}

	if(bldCdSelIn === 0) {
		editScriptGridData = [];
		editScriptGrid.setData(editScriptGridData);
		$('#txtBldMsg').val('');
		return;
	} else {
		$('#txtBldMsg').val(getSelectedVal('cboBldCd').cm_codename);
	}

	$('#btnDel').css("visibility", 'visible');
	$('#btnCopy').css("visibility", 'visible');

	var data = new Object();
	data = {
		Cbo_BldGbn_code : bldGbnVal,
		Cbo_BldCd0_code : bldCdVal,
		requestType	: 'getScript'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetScript);
}

// 스크립트 가져오기
function getExistScript() {
	var bldGbn 	= getSelectedVal('cboBldGbnB').value;
	var data = new Object();
	data = {
		Cbo_BldGbn 	: bldGbn,
		requestType	: 'getSql_Qry'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetExistScript);
}

// 스크립트 가져오기 완료
function successGetExistScript(data) {
	scriptGridData = data;
	
	if($('#txtOrder').val().trim().length > 0) {
		scriptGridData = scriptGridData.filter(function(item) {
			if(item.cm_cmdname.toUpperCase().indexOf($('#txtOrder').val().trim().toUpperCase()) > -1) return true;
			else return false;
		});
	}
	
	scriptGrid.setData(scriptGridData);
}

// 스크립트 유형 삭제
function delScript() {
	if(getSelectedIndex('cboBldCd') < 0) {
		dialog.alert('등록구분을 선택하여 주십시오.');
		return;
	}
	
	if(getSelectedIndex('cboBldGbn') < 0) {
		dialog.alert('유형구분을 선택하여 주십시오.');
		return;
	}
	
	var bldCd 		= getSelectedVal('cboBldCd').cm_micode;
	var bldGbn 		= getSelectedVal('cboBldGbn').cm_micode;

	var data = new Object();
	data = {
		Cbo_BldGbn 	: bldGbn,
		Cbo_BldCd0 	: bldCd,
		requestType	: 'getCmm0022_Del'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successDelScript);
}

function successDelScript(data) {
	if (data != 0) {
		dialog.alert("삭제처리를 완료하였습니다.");

	    getBldCd();
	    $('#btnQry').trigger('click');
		$('#txtComp').val('');
		$('#txtSeq').val('');
		$('#txtErrMsg').val('');
		$('#txtBldMsg').val('');
	} else {
		dialog.alert("삭제처리에 실패하였습니다.");
	}
}

// 스크립트 새이름 저장
function copyScript() {
	console.log('fCboBldCdData',fCboBldCdData);
	var bldCd 		= getSelectedVal("cboBldCd").cm_micode;
	var bldGbn 		= getSelectedVal('cboBldGbn').cm_micode;
	var newBld 		= fCboBldCdData[0].cm_micode;
	var txtBldMsg	= $('#txtBldMsg').val();
	newBldCdValue	= newBld;
	
	if(txtBldMsg.length === 0 || getSelectedVal('cboBldCd').text == txtBldMsg) {
		dialog.alert('새로운 유형제목을 입력하여 주십시오.', function(){});
		return;
	}

	var data = new Object();
	data = {
		Cbo_BldGbn 	: bldGbn,
		Cbo_BldCd0 	: bldCd,
		NewBld 		: newBld,
		NewBldMsg   : txtBldMsg,
		requestType	: 'getCmm0022_Copy'
	}
	console.log('getCmm0022_Copy',data);
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successCopyScript);
}

// 스크립트 새이름 저장 완료
function successCopyScript(data) {
	if(data != 'XXX') {
//		newBldCdValue = data;
		dialog.alert('새이름으로 저장처리를 완료하였습니다.', function() {});
		getBldCd();
	} else {
		dialog.alert('새이름으로 저장처리 중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.', function() {});
	}
}

// 스크립트 저장
function insertScript() {
	if(editScriptGridData.length === 0 ) {
		dialog.alert('빌드용 수행명령을 입력한 후 등록하십시오.', function(){});
		return;
	}

	var txtBldMsg = $('#txtBldMsg').val();

	if(txtBldMsg.length === 0 || txtBldMsg == '유형신규등록') {
		dialog.alert('새로운 유형제목을 입력하여 주십시오.', function(){});
		return;
	}

	var bldCd		= getSelectedVal('cboBldCd').cm_micode;
	var bldGbn 		= getSelectedVal('cboBldGbn').value;
	
	var txtErrMsg	= $('#txtErrMsg').val();
	newBldCdValue = bldCd;
	
	var strRunType = 'R';
	if($('#chkLocal').is(':checked')) strRunType = 'L';
	
	for(var i=0; i<editScriptGridData.length; i++) {
		editScriptGridData[i].cm_seq = i+1;
	}

	var data = new Object();
	data = {
		Cbo_BldGbn 	: bldGbn,
		Cbo_BldCd0 	: bldCd,
		codename 	: txtBldMsg,
		Txt_Comp2  	: txtErrMsg,
		runType    	: strRunType,
		Lv_File0_dp	: editScriptGridData,
		requestType	: 'getCmm0022_DBProc'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successInsertScript);
}

// 스크립트 저장 완료
function successInsertScript(data) {
	if (Number(data) > 0) {
//		newBldCdValue = data;
		dialog.alert("등록처리를 완료하였습니다.");

    	getBldCd();
    	$('#btnQry').trigger('click');
		$('#txtComp').val('');
		$('#txtSeq').val('');
		$('#txtErrMsg').val('');
		$('#txtBldMsg').val('');
 	}else{
 		dialog.alert("등록처리를 실패하였습니다.");
 	}
}

// 스크립트 그리드에 추가하기
function addGrid() {
	var txtComp = $('#txtComp').val();
	var txtSeq = $('#txtSeq').val();
	
	if(getSelectedIndex('cboBldCd') < 0) {
		dialog.alert('등록구분을 선택하여 주십시오.');
		return;
	}
	
	if(getSelectedIndex('cboBldGbn') < 0) {
		dialog.alert('유형구분을 선택하여 주십시오.');
		return;
	}
	
	if(txtComp.length === 0 ) {
		dialog.alert('수행명령을 입력하여 주십시오.', function(){});
		return;
	}
	
	var newGridItem 		= new Object();
	newGridItem.ID 			= 'grdScr0';
	newGridItem.cm_cmdname 	= txtComp;
	
	if(editScriptGridData.length > Number(txtSeq) && Number(txtSeq) > 0) {
		editScriptGridData.splice(Number(txtSeq)-1,0,newGridItem);
	}else {
		editScriptGridData.push(newGridItem);
	}
	editScriptGrid.setData(editScriptGridData);
	editScriptGrid.clearSelect();
}

// 수정 스크립트 그리드에 추가하기
function addEditGrid() {
	var txtComp = $('#txtComp').val();
	var txtSeq = $('#txtSeq').val();

	if(getSelectedIndex('cboBldCd') < 0) {
		dialog.alert('등록구분을 선택하여 주십시오.');
		return;
	}
	
	if(getSelectedIndex('cboBldGbn') < 0) {
		dialog.alert('유형구분을 선택하여 주십시오.');
		return;
	}
	
	if(txtComp.length === 0 ) {
		dialog.alert('수행명령을 입력하여 주십시오.', function(){});
		return;
	}
	
	if(editScriptGridData.length >= Number(txtSeq) && Number(txtSeq) > 0) {
		editScriptGridData[Number(txtSeq)-1].cm_cmdname = txtComp;
	}else {
		dialog.alert('잘못된 수행순서입니다.', function(){});
		return;
	}
	
	editScriptGrid.setData(editScriptGridData);
	editScriptGrid.select(Number(txtSeq)-1);
}

// 수정 스크립트 그리드 삭제하기
function deleteEditGrid() {
	var selIn = editScriptGrid.selectedDataIndexs;
	var selItem = null;
	if(selIn.length === 0 ) {
		dialog.alert('삭제 할 정보를 그리드에서 선택 후 눌러주세요.', function(){});
		return;
	}

	editScriptGridData.splice(selIn,1);
	editScriptGrid.setData(editScriptGridData);
}

// 스크립트 수정 그리드 선택
function clickEditGrid(index) {
	var selItem = editScriptGrid.list[index];

	$('#chkLocal').wCheck('check',false);
	$('#txtComp').val(selItem.cm_cmdname);
	$('#txtSeq').val(index+1);
	$('#txtErrMsg').val(selItem.cm_errword);
}

// 유형구분 선택시 스크립트 가져오기
function successGetScript(data) {
	$('#chkLocal').wCheck('check',false);
	editScriptGridData = data;
	editScriptGrid.setData(editScriptGridData);
	if(editScriptGridData.length > 0 ) {
		$('#txtErrMsg').val(editScriptGridData[0].cm_errword);
		if(editScriptGridData[0].cm_runtype == 'L') {
			$('#chkLocal').wCheck('check',true);
		}
	}
}

// 유형구분 가져오기
function getBldCd(){
	var data = new Object();
	data = {
		requestType	: 'getBldCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', data, 'json',successGetBldCd, changeCboBldGbn);
}

// 트리거 콜백으로 쓰기위해서 function으로 뺌
function changeCboBldGbn() {
	$('#cboBldGbn').trigger('change');
}

// 유형구분 가져오기 완료
function successGetBldCd(data) {
	cboBldCdData = data;
}

// 등록구분 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('BLDGBN','SEL','N', '3', '')
	]);
	cboBldGbnData 	= codeInfos.BLDGBN;
	cboBldGbnBData 	= codeInfos.BLDGBN;

	codeInfos = null;
	
	$('[data-ax5select="cboBldGbn"]').ax5select({
        options: injectCboDataToArr(cboBldGbnData, 'cm_micode' , 'cm_codename')
	});
	
	$('[data-ax5select="cboBldGbnB"]').ax5select({
        options: injectCboDataToArr(cboBldGbnData, 'cm_micode' , 'cm_codename')
	});

	getBldCd();
	getExistScript();
};

//엑셀 파일 업로드시 파일타입 체크
function fileTypeCheck(obj) {
	var pathpoint = obj.value.lastIndexOf('.');
	var filepoint = obj.value.substring(pathpoint+1,obj.length);
	filetype = filepoint.toLowerCase();
	if(filetype=='xls' || filetype=='xlsx') {
		startUpload();
	} else {
		dialog.alert('엑셀 파일만 업로드 가능합니다.', function() {});
		parentObj  = obj.parentNode
		node = parentObj.replaceChild(obj.cloneNode(true),obj);
		return false;
	}
}

//엑셀 파일을 임시 경로에 업로드
function startUpload() {
	editScriptGridData = [];
	editScriptGrid.setData(editScriptGridData);

	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;

	//tmpPath = 'C:\\html5\\temp\\'; //테스트중
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();
	formData.append('fullName',tmpPath+userId+"_excel_eCmd1200.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);

	$.ajax({
	     url:homePath+'/webPage/fileupload/'+uploadJspFile,
	     type:'POST',
	     data:formData,
	     async:false,
	     cache:false,
	     contentType:false,
	     processData:false
	}).done(function(response){
	 	onUploadCompleteData(response);
	}).fail(function(xhr,status,errorThrown){
	 	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
	}).always(function(){
	 	// file 초기화
	 	var agent = navigator.userAgent.toLowerCase();
	 	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
	 	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
	 	} else {
	 	    $("#excelFile").val("");
	 	}
	});
}

//엑셀파일 완료 후
function onUploadCompleteData(filePath){
	filePath = replaceAllString(filePath,"\n","");

	var headerDef = new  Array();
	headerDef.push("cm_cmdname");
	headerDef.push("cm_errword");
	headerDef.push("cm_runtype");

	var tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

//읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}

	editScriptGridData = data;
	editScriptGridData.splice(0,1);
	editScriptGrid.setData(editScriptGridData);
	
	if(editScriptGridData[1].cm_runtype == 'L') {
		$('#chkLocal').wCheck('check',true);
	}else {
		$('#chkLocal').wCheck('check',false);
	}
}