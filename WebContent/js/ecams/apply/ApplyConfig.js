/**
 * 배포관리
 * 
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-08-00
 * 
 */
var userId 	= window.top.userId;
var reqCd 	= window.top.reqCd;
var adminYN = window.top.adminYN;
var rgtList	= window.top.rgtList;

var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];
var cboSysData		= [];	
var cboDeptData 	= [];
var cboGbnData		= [];

var ingSw 			= false;
var swEmg 			= false;
var acptNo 			= '';   //신청번호
var pgAdmin			= 'N';	//프로그램관리자 여부
var btnGbn			= '';	//신청건선택배포:A, 선택배포:S 
var svIdx 			= 0; 
var svWord			= '';

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	//this.self.clearSelect();
        	//this.self.select(this.dindex);
        	getFileList2(this.item.cr_acptno);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        	openWindow(1, '04', this.item.cr_acptno);
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    	trStyleClass: function () {
    		if (this.item.ColorSw === '3'){
    			return "fontStyle-cncl";
    		}else if (this.item.ColorSw === '0'){
    			return "fontStyle-ing";
    		}else if (this.item.ColorSw === '5'){
    			return "fontStyle-error";
    		}
    	}
    },
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "변경신청상세"}
        ],
        popupFilter: function (item, param) {
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
         	return true;
        },
        onClick: function (item, param) {
        	openWindow(1, '04', param.item.cr_acptno);
	        firstGrid.contextMenu.close();//또는 return true;
        }
    },
    columns: [
        {key: "cm_sysmsg",		label: "시스템",		width: '10%',	align: 'left'},
    	{key: "cm_deptname",    label: "신청부서",		width: '12%',  	align: 'left'},
    	{key: "cr_acptno",    	label: "신청번호", 	width: '6%',  	align: 'center'},
    	{key: "passok",       	label: "적용구분", 	width: '8%',  	align: 'center'},
    	{key: "cr_acptdate",    label: "신청일시",		width: '8%',  	align: 'center'},
    	{key: "cr_sayu",   		label: "신청사유",		width: '32%',  	align: 'left'},
    	{key: "mciYN",   		label: "MCI여부",		width: '8%',  	align: 'center'},
    	{key: "cr_editor",    	label: "신청인",    	width: '8%',  	align: 'center'},
    	{key: "prcreq",   		label: "적용완료일시", 	width: '8%',  	align: 'center'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: true,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: true,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    	trStyleClass: function () {
    		if (this.item.ColorSw === '3'){
    			return "fontStyle-cncl";
    		}else if (this.item.ColorSw === '0'){
    			return "fontStyle-ing";
    		}else if (this.item.ColorSw === '5'){
    			return "fontStyle-error";
    		}
    	}
    },
    columns: [
        {key: "cr_deploy",   	label: "적용여부",		width: '10%',  	align: 'center'},
    	{key: "cr_rsrcname2",	label: "프로그램명",	width: '20%',  	align: 'left'},
    	{key: "cr_editcon",    	label: "사유",		width: '20%',  	align: 'left'},
    	{key: "cr_rsrcname",    label: "자원명",		width: '15%',  	align: 'left'},
    	{key: "cr_aplydate",    label: "적용예정일시",	width: '10%',  	align: 'center'},
        {key: "cr_acptno",      label: "신청번호", 	width: '10%',	align: 'center'},
        {key: "cm_codename",    label: "프로그램종류", 	width: '15%',	align: 'center'},
        {key: "cm_dirpath",     label: "디렉토리", 	width: '20%',	align: 'left'},
    ]
});

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});
$('[data-ax5select="cboDept"]').ax5select({
    options: []
});
$('[data-ax5select="cboGbn"]').ax5select({
    options: []
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	if (reqCd == null || reqCd == '' || reqCd == undefined) {
		dialog.alert('정당하지 않은 접근입니다.');
		return;
	}
	if (userId == null || userId == '' || userId == undefined) {
		dialog.alert('로그인 후 사용하시기 바랍니다.');
		return;
	}
	
	if (rgtList != null && rgtList != undefined && rgtList != '') {	
		var rgtArray = rgtList.split(',');
		for (var i=0; i<rgtArray.length; i++) {
			if (rgtArray[i] == 'LIB' || rgtArray[i] == 'A1') { //LIB:프로그램관리자, A1:eCAMS관리자
				pgAdmin = 'Y';
			}
		}
	}
	
	//조회
	$('#btnReq').bind('click',function(){
		btnReqClick();
	});
	
	//신청건 선택배포
	$('#btnAcptApply').bind('click',function(){
		btnGbn = 'A';
		btnAcptApplyClick();
	});
	
	//선택배포
	$('#btnApply').bind('click',function(){
		btnGbn = 'S';
		btnApplyClick();
	});
	
	$('#cboGbn').bind('change',function(){
//		if(reqCd == 'OD') cboGbnChange();	//배포구분
//		else btnReqClick();
		btnReqClick();
	});
	
	//찾기클릭
	$('#btnSearch').bind('click', function() {
		btnSearch_click();
	});
	
	//신청번호,신청인 검색
	$('#txtSearch').bind('keypress', function(event){
		if(event.keyCode==13) {
			event.keyCode = 0;
			
			if ($('#txtSearch').val().trim() == null || $('#txtSearch').val().trim().length == 0) {
				dialog.alert('검색할 신청번호/신청인을 입력하여 주시기 바랍니다.', function(){});
				return;
			}
			
			$('#btnSearch').trigger('click');
		}
	});
	
	getCodeInfo();
	getTeamInfo();	
});

function getCodeInfo() {
	if(reqCd == 'OD') {	//배포관리
		cboGbnData = [
			{cm_codename : "검증", cm_micode : "00"},
			{cm_codename : "적용", cm_micode : "01"},
		];
	} else {
		cboGbnData = [
			{cm_codename : "검증", cm_micode : "00"},
			{cm_codename : "적용", cm_micode : "01"},
			{cm_codename : "MCI 적용", cm_micode : "02"},
		];
	}
	
	$('[data-ax5select="cboGbn"]').ax5select({
        options: injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
   	});
	
	getSysInfo();
}

function getTeamInfo() {
	var data = {
		SelMsg : 'ALL',
		cm_useyn : 'Y',
		gubun : 'sub',
		itYn : 'Y',
		requestType	: 'getTeamInfoGrid2'
	}
	
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetTeamInfo);
}

function successGetTeamInfo(data) {
	cboDeptData = data;
	$('[data-ax5select="cboDept"]').ax5select({
        options: injectCboDataToArr(cboDeptData, 'cm_deptcd' , 'cm_deptname')
   	});
}

function getSysInfo() {
	var data = {
		UserId : userId,
		SecuYn : adminYN ? 'N':'Y',
		SelMsg : 'ALL',
		CloseYn : adminYN ? 'N':'Y',
		ReqCd : adminYN ? '':'OPEN',
		requestType	: 'getSysInfo'
	}
	
	ajaxAsync('/webPage/common/SysInfoServlet', data, 'json',successGetSysInfo);
}

function successGetSysInfo(data) {
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
   	});
	
	if(reqCd == 'OD') {	//배포관리
//		cboGbnChange();
	}else {	//배포관리_NEW
		$('[data-ax5select="cboSys"]').ax5select('enable');
		btnReqClick();
	}
}

//function cboGbnChange() {
//	if(getSelectedIndex('cboGbn') == 0) {
//		for(var i=0; i<cboSysData.length; i++) {
//			if(cboSysData[i].cm_syscd == '01200') {
//				$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[i].cm_syscd,true);
//				break;
//			}
//		}
////		$('[data-ax5select="cboSys"]').ax5select('disable');
//	}else {
//		$('[data-ax5select="cboSys"]').ax5select('enable');
//	}
//}

function getFileList2(acptno) {
	var data = new Object();
	data = {
		acptno : acptno,
		cboGbn : getSelectedIndex('cboGbn') > -1 ? getSelectedVal('cboGbn').cm_micode : '',
		requestType :'getFileList2'
	}
	
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json', successGetFileList2);
}

function successGetFileList2(data) {
	$(".loding-div").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	secondGridData = data;
	secondGrid.setData(secondGridData);
}

function btnReqClick() {
	secondGridData = [];
	secondGrid.setData([]);
	
	var strSys 	= '';
	var strDept	= '';
	var strGbn	= '';
	var emgSw 	= '0';
	
//	if(getSelectedVal('cboSys').cm_syscd != '01200') {
//		if(pgAdmin == 'N') {
//			dialog.alert('배포권한이 없습니다.');
//			return;
//		}
//	}
	
	if(getSelectedVal('cboGbn').cm_micode == '01') {
		if(pgAdmin == 'N') {
			dialog.alert('운영배포권한이 없습니다.');
			return;
		}
	}
	
	if(getSelectedIndex('cboSys') > 0) strSys = getSelectedVal('cboSys').cm_syscd;
	if(getSelectedIndex('cboDept') > 0) strDept = getSelectedVal('cboDept').cm_deptcd;
	if(getSelectedIndex('cboGbn') > -1) strGbn = getSelectedVal('cboGbn').cm_micode;
	if($('#chkEmg').is(':checked')) emgSw = '2';
	
	var data = new Object();
	data = {
		strUserId : userId,
		strSys : strSys,
		strDept	: strDept,
		cboGbn : strGbn,
		Admin : pgAdmin,
		emgSw : emgSw,
		requestType :'getFileList_Emg'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json', successGetFileList);
}

function successGetFileList(data) {
	$(".loding-div").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
	
	if(firstGridData.length < 1) {
		dialog.alert('검색 결과가 없습니다.');
		return;
	}
}

function btnAcptApplyClick() {
	var selItems = firstGrid.getList('selected');
	if(selItems.length < 1) {
		dialog.alert('선택한 배포건이 없습니다. 선택 후  배포하여 주십시오.');
		return;
	}
	
	var data = new Object();
	data = {
		chkInList : selItems,
		cboGbn : getSelectedIndex('cboGbn') > -1 ? getSelectedVal('cboGbn').cm_micode : '',
		requestType :'BePoplay1'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json', successApply);
}

function btnApplyClick() {
	var selItems = secondGrid.getList('selected');
	if(selItems.length < 1) {
		dialog.alert('선택한 배포건이 없습니다. 선택 후  배포하여 주십시오.');
		return;
	}
	
	var data = new Object();
	data = {
		chkInList : selItems,
		cboGbn : getSelectedIndex('cboGbn') > -1 ? getSelectedVal('cboGbn').cm_micode : '',
		requestType :'BePoplay'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json', successApply);
}

function successApply(data) {
	if(btnGbn == 'A') dialog.alert('해당 신청건이 배포되었습니다.');
	else dialog.alert('해당 자원이 배포되었습니다.');
	
	btnReqClick();
}

function btnSearch_click() {
	var findSw = false;
	var strWord = $('#txtSearch').val().trim();
	strWord = replaceAllString(strWord, "-", "");

	if (svWord == '' || svWord != strWord) svIdx = -1;
	++svIdx;
	
	svWord = strWord;
	
	for(i=svIdx; i<firstGrid.list.length; i++) {
		if (firstGrid.list[i].cr_acptno.indexOf(svWord)>=0 || firstGrid.list[i].cr_editor.indexOf(svWord)>=0) {
			svIdx = i;
			if(!firstGrid.list[i].__selected__) firstGrid.select(svIdx,{selectedClear:false});
			findSw = true;
			break;
		}
	}
	
	if (findSw) {
		$('#btnSearch').text('다음');
	}
	
	if (!findSw) {
		setTimeout(function() {
			dialog.alert('더 이상 검색내용이 없습니다.');
		}, 100);
		$('#btnSearch').text('찾기');
		svIdx = -1;
		svWord = '';
		return;
	}
	//firstGrid.focus(svIdx);
	//if(!firstGridData[svIdx].__selected__) firstGrid.select(svIdx,{selectedClear:false});
	//firstGrid.clickBody(svIdx);
	//firstGrid.clearSelect();
}

function openWindow(type,reqcd,param) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;
    f.user.value = userId;
    f.acptno.value = param;
    
    if (type == 1) {
		nHeight = 740;
	    nWidth  = 1300;

	    if(reqCd == "01" || reqCd == "02" || reqCd == "11") {
	    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
	    }else if(reqCd == "60") {
	    	cURL = "/webPage/winpop/PopRequestDetailHomePg.jsp";
		}else {
	    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
		}
	} else {
		dialog.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}