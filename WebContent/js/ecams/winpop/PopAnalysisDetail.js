/**
 * 영향분석확인 상세 (eCmr1010.mxml)
 * 
 * 	작성자: 진가윤
 * 	버전 : 1.0
 *  수정일 : 2022-09-00
 * 
 */

var pUserId 	= null;
var pAcptNo	    = null;

var f = document.getReqData;
pUserId = f.userId.value;
pAcptNo = f.acptno.value;

var astaGrid		= new ax5.ui.grid();

var astaGridData		= [];
var cboStaData			= [];
var cboSta2Data			= [];
var cboDeptData 		= [];
var rgtcd = [];

astaGrid.setConfig({
	target : $('[data-ax5grid="astaGrid"]'),
	showLineNumber : true,
	showRowSelector : true,
	multipleSelect : true,
	lineNumberColumnWidth : 40,
	rowSelectorColumnWidth : 27,
	header : {
		align: "center"
	},
	body : {
		columnHeight: 24,
		onClick : function() {
			/*this.self.clearSelect();*/
			this.self.select(this.dindex);
			selectedItem = this.item;
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
         	openWindow(1, this.item.cr_itemid, this.item.cr_acptno,'');
         },
         trStyleClass: function () {
        	 if(this.item.colorsw === '5'){
      			return "fontStyle-error";
      		} else if (this.item.colorsw === '3'){
      			return "fontStyle-cncl";
      		} else if (this.item.colorsw === '0'){
      			return "fontStyle-ing";
      		}
      	},
 		onDataChanged : function() {
 			this.self.repaint();
 		}
	},
	columns : [
		{key : "modelnm",		label : "모델명",			width: "16%",	align: "left"	}, 
		{key : "cr_rsrcname2",	label : "프로그램명",		width: "10%",	align: "left"	}, 
		{key : "rsrcstat1",		label : "상태",			width: "6%",	align: "center"	},
		{key : "cm_username",	label : "요청자",			width: "6%",	align: "center"	},
		{key : "refmodelnm",	label : "참조모델명",		width: "16%",	align: "left"	},
		{key : "cr_rsrcname",	label : "참조프로그램명",	width: "10%",	align: "left"	},
		{key : "rsrcstat2",		label : "상태",			width: "6%",	align: "center"	},
		{key : "cm_deptname",	label : "확인대상자부서",	width: "6%",	align: "center"	},
		{key : "cr_refuser",	label : "확인대상자",		width: "6%",	align: "center"	},
		{key : "cr_chkflag",	label : "확인구분",		width: "6%",	align: "center"	},
		{key : "cr_chkusser",	label : "확인자",			width: "6%",	align: "center"	}, 
		{key : "cr_chkdate",	label : "확인일시",		width: "6%",	align: "center"	} 
	]
});


$(document).ready(function() {
	screenInit();
	getCodeInfo();
	getUserRgtCd();
	//조회
	$("#btnQry").bind('click', function() {
		getAsta();
	})
	//엑셀저장
	$('#btnExcel').bind('click', function() {
  	    excelExport(astaGrid, "영향분석확인.xls");
	});
	//등록
	$('#btnRegist').bind('click', function() {
		btnRegist_Click();
	});
	//닫기
	$('#btnClose').bind('click', function() {
		close();
	});
	//전체선택
	$('#chkAll').bind('click', function() {
		chkAll_Click();
	});
	
	//시스템 변경 이벤트
	$('#cboDept').bind('change', function() {
		cboDept_Change();
	});
});

function cboDept_Change(){
	 console.log(getSelectedVal('cboDept').value);
	 
	 var tempGridData = astaGridData;

	 if(getSelectedVal('cboDept').value != "****"){
		 tempGridData = tempGridData.filter(function(data) {
				if(data.refproject == getSelectedVal('cboDept').value) return true;
				else return false;
			});
	 }
	 
	astaGrid.setData(tempGridData);
}

function screenInit(){
	$('[data-ax5select="cboSta"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboSta2"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDept"]').ax5select({
		options: []
	});

	$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
}

//사용자 권한 가져오기
function getUserRgtCd() {
	var data = new Object();
	data = {
		UserId 		: pUserId,
		requestType	: 'getUserRGTCD'
	}
	ajaxAsync('/webPage/ecmm/Cmm0400Servlet', data, 'json',successGetUserRgtCd);
}

//사용자 권한 가져오기 완료
function successGetUserRgtCd(data) {
	rgtcd = data.filter(function(data) {
		if(data.checkbox == "true") return true;
		else return false;
	});
}

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ 
		new CodeInfoOrdercd('ASTA2', 'ALL','N'),
		new CodeInfoOrdercd('ASTA', 'SEL','N'),
	]);
	
	cboStaData   = codeInfos.ASTA2;
	cboSta2Data  = codeInfos.ASTA;
	
	$('[data-ax5select="cboSta"]').ax5select({
		options: cboStaData
	});
	$('[data-ax5select="cboSta2"]').ax5select({
		options: cboSta2Data
	});
	
	getAsta();
}

function getAsta() {
	astaGridData = [];
	data = new Object();
	data = {
		Acptno : pAcptNo,
		userid : pUserId,
		gubun  : getSelectedVal('cboSta').cm_micode,
		requestType : 'getAsta'
	}
	ajaxAsync('/webPage//ecmr/Cmr3200Servlet', data, 'json', successGetAsta);
}

function successGetAsta(data) {
	astaGridData = data;
	$(astaGridData).each(function(i){
		if(astaGridData[i].enabled == "0"){
			astaGridData[i].__disable_selection__ = true;
		}
	});
	
	astaGridData = astaGridData.filter(function(data) {
		if(data.enabled == "1") return true;
		else if(data.cr_chkflag != "미확인")  return true;
		else return false;
	});
	
	astaGrid.setData(astaGridData);
	
	var i = 0;
	if(astaGridData.length < 1) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	if(astaGridData.length > 0) {
		$('#txtSys').val(astaGridData[0].cm_sysmsg);
		$('#txtAcptNo').val(astaGridData[0].cr_acptno);
	}
	
	cboDeptData = data;
	
	for(var j=0; j<cboDeptData.length; j++) {
		for(var q=j+1; q<cboDeptData.length; q++) {
			if(cboDeptData[j].refproject == cboDeptData[q].refproject) {
				cboDeptData.splice(j,1);
				j--;
				break;
			}
		}
	}
	
	cboDeptData.sort(function(a,b){
		return a.cm_deptname < b.cm_deptname ? -1 : a.cm_deptname > b.cm_deptname ? 1: 0;
	});
	
	cboDeptData.unshift({refproject: '****', cm_deptname: '전체'});
	
	$('[data-ax5select="cboDept"]').ax5select({
        options: injectCboDataToArr(cboDeptData, 'refproject' , 'cm_deptname')
	});
	console.log(astaGridData);
//	astaGridData = [];
}

function btnRegist_Click() {
	if(getSelectedIndex('cboSta2') == 0) {
		dialog.alert('조치여부를 선택하세요');
		return;
	}
	var astaGridSelected = astaGrid.getList("selected");
	
	if(astaGridSelected.length == 0) {
		dialog.alert('선택된 목록이 없습니다. 목록에서 삭제 할 대상을 선택한 후 처리하십시오.');
		return;
	}
	// 20230102 담당직무 체크로직 추가
	var gyulYN = "N";
	var editorYN = "Y";
	
	for(var i=0;i<rgtcd.length;i++){
		if(rgtcd[i].cm_micode == "UC"){
			gyulYN = "Y";
			break;
		}
	}
	
	// 20230126 신청자인경우 결재 진행되도록 추가
	for(var z=0 ; z < astaGridSelected.length;z++){
		if(astaGridSelected[z].cr_editor != pUserId){
			editorYN = "N";
			break;
		}
	}
	
	if(gyulYN == "N" && editorYN == "N"){
		dialog.alert('영향분석확인 직무자, 신청자만 등록할수있습니다.');
		return;
	}
	
	var strSta2 = getSelectedVal('cboSta2').cm_micode;
	var etcObj = [];
	var i = 0;
	
	data = new Object();
	data = {
		etcData : astaGridSelected,
		chkflag : strSta2,
		userid 	: pUserId,
		requestType : 'Cmr1015Insert'
	}
	ajaxAsync('/webPage//ecmr/Cmr3200Servlet', data, 'json', successInsert);
}

function successInsert(data) {
	dialog.alert('정상적으로 등록되었습니다.');
	getAsta();
}

function chkAll_Click() {
	for(var i = 0 ; i < astaGridData.length ; i++) {
		if($('#chkAll').is(':checked')) {
			if(astaGridData[i].enabled == true) {
				astaGrid.select(i, {selected: true});
			}else {
				astaGrid.select(i, {selected: false});
			}
		}else {
			astaGrid.select(i, {selected: false});
		}
	}
	
	astaGridData = [];
}

function openWindow(type,reqCd,reqNo,rsrcName) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   		//폼 name
    
	f.itemid.value  = reqCd;
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= pUserId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	if (type == 1) {
		nHeight = 700;
	    nWidth  = 1000;
	    cURL = "/webPage/winpop/PopSourceDiff.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	console.log('openWindow reqNo='+reqNo+', cURL='+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}