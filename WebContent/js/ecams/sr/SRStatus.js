var userName 	= window.top.userName;
var userId 		= window.top.userId;
var reqCd 		= window.top.reqCd;
var rgtList		= window.top.rgtList; 
var adminYN		= window.top.adminYN;

var firstGrid 	= new ax5.ui.grid();
var picker 		= new ax5.ui.picker();

var firstGridData 	= [];
var cboDeptData1	= [];
var cboDeptData2	= [];
var cboStaData1		= [];
var cboStaData2		= [];

var myWin 			= null;
var SoJangYn 		= false; 	// 32: 소장
var SilJangYn 		= false;	// 40: 실장
var loadSw			= false;

$('#datStD').val(getDate('DATE',0));
$('#datEdD').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));
 
firstGrid.setConfig({
     target: $('[data-ax5grid="firstGrid"]'),
     sortable: true, 
     multiSort: true,
     header: {
         align: "center"
     },
     frozenColumnIndex: 1,
     body: {
         onClick: function () {
         	this.self.clearSelect();
            this.self.select(this.dindex);
         },
         onDBLClick: function () {
        	 openWindow('SRINFO', this.item.isrid);
         },
     	trStyleClass: function () {
    	   	if (this.item.color == "3" || this.item.color == "A"){ //반려 또는 취소
    	   		return "fontStyle-cncl";
    	   	} else if (this.item.color == "R") {	//접수
    	   		return "fontStyle-rec";
    	   	} else if (this.item.color == "C" ) {	//개발
    	   		return "fontStyle-dev";
    	   	} else if (this.item.color == "T" ) {	//테스트
    	   		return "fontStyle-test";
    	   	} else if (this.item.color == "P") {	//적용
    	   		return "fontStyle-apply";
    	   	} else if (this.item.color == "9") {	//처리완료
    	   		return "fontStyle-end";
    	   	} 
     	},
     	onDataChanged: function(){
     	    this.self.repaint();
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
             {type: 1, label: "SR정보"},
         ],
         popupFilter: function (item, param) {
        	 return true;
         },
         onClick: function (item, param) {
        	 openWindow('SRINFO', param.item.isrid);
             firstGrid.contextMenu.close();
         }
     },
     columns: [
		{key: "isrid",			label: "SR ID", 	width: "10%",align: "center"},
		{key: "genieid",		label: "GENIE번호",	width: "10%",align: "center"},
		{key: "recvdate",		label: "등록일", 		width: "6%", align: "center"},
		{key: "reqdept",		label: "요청부서", 	width: "6%", align: "left"},
		{key: "reqsta1",		label: "SR상태", 		width: "6%", align: "center"},
		{key: "reqtitle",		label: "요청제목", 	width: "15%",align: "left"},
		{key: "reqedday",		label: "완료요청일", 	width: "6%", align: "center"},
		{key: "comdept",		label: "등록부서", 	width: "6%", align: "left"},
		{key: "recvuser",		label: "등록인", 		width: "6%", align: "center"},
		{key: "recvdept",		label: "개발부서", 	width: "6%", align: "left"},
		{key: "devuser",		label: "개발담당자", 	width: "6%", align: "center"},
		{key: "reqsta2",		label: "개발자상태", 	width: "6%", align: "center"},
		{key: "chgdevterm",		label: "개발기간", 	width: "10%",align: "center"},
		{key: "chgdevtime",		label: "투입공수", 	width: "6%", align: "center"},
		{key: "chgpercent",		label: "진행율", 		width: "6%", align: "center"},
		{key: "chgedgbn",		label: "변경종료구분", 	width: "6%", align: "center"},
		{key: "chgeddate",		label: "변경종료일", 	width: "6%", align: "center"},
		{key: "isredgbn",		label: "SR완료구분", 	width: "6%", align: "center"},
		{key: "isreddate",		label: "SR완료일", 	width: "6%", align: "left"}
     ]
});

$('input:radio[name=rdoGbn]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){
	if(reqCd == '' || reqCd == null || reqCd == undefined) reqCd = 'MY';
	if(reqCd == 'MY' || reqCd == '1' || reqCd == 'A') $('#optSub3').wRadio('check', true);
	else $('#optSub1').wRadio('check', false);
	
	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#datEdD').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#datEdD").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
	
	$('#cboSta1').bind('change', function() {
		changeStaInfo();
	});
	
	$('input:radio[name="rdoGbn"]').bind('click', function() {
		if(loadSw) get_SelectList();
	});
	
	// 엑셀저장버튼 클릭
	$('#btnExcel').bind('click', function() {
		firstGrid.exportExcel('SR진행현황목록.xls');
	});
	
	// 초기화 버튼 클릭
	$('#btnReset').bind('click', function() {
		resetScreen();
	});
	
	// 조회 버튼 클릭
	$('#btnQry').bind('click', function() {
		get_SelectList();
	});
	
	// 요청 제목 엔터
	$("#txtTitle").bind("keyup", function(e) {
		if(e.keyCode == 13) get_SelectList();
	})
	
	checkRgtCd();
	getDeptInfo1();
	getDeptInfo2();
});

//20141027 neo. [40]실장  [32]소장 에 대한 화면 셋팅
function checkRgtCd() {
	if (rgtList == undefined || rgtList == null && rgtList == '') return;
	
	if (rgtList != null) {	
		var rgtArray = rgtList.split(',');
		for (var i=0;rgtArray.length>i;i++) {
			if (rgtArray[i] == '32') {
				SoJangYn = true;
			} else if (rgtArray[i] == '40') {
				SilJangYn = true;
			} 
		}
	}
	
	if(SoJangYn) {
		$('#optSub1').wRadio('check', true);
		$('#datStD').val(getDate('MON',-1));
		picker.bind(defaultPickerInfoLarge('basic', 'top'));
	}else if(SilJangYn) {
		$('#optSub2').wRadio('check', true);
	}else {
		$('#optSub3').wRadio('check', true);
	}
	
	getCodeInfo();
}

// 요청부서 cbo 가져오기
function getDeptInfo1(){
	var data =  new Object();
	data = {
		SelMsg 		: 'All',
		cm_useyn 	: 'Y',
		gubun 		: 'req',
		itYn 		: 'N',
		requestType	: 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetDeptInfo1);
}

// 요청부서 cbo 가져오기 완료
function successGetDeptInfo1(data) {
	cboDeptData1 = data;
	
	$('[data-ax5select="cboDept1"]').ax5select({
        options: injectCboDataToArr(cboDeptData1, "cm_deptcd", "cm_deptname")
	});
}

// 등록부서 cbo 가져오기
function getDeptInfo2(){
	var data =  new Object();
	data = {
		SelMsg 		: 'All',
		cm_useyn 	: 'Y',
		gubun 		: 'DEPT',
		itYn 		: 'N',
		requestType	: 'getTeamInfoGrid2'
	}
	ajaxAsync('/webPage/common/TeamInfoServlet', data, 'json',successGetDeptInfo2);
}

// 등록부서 cbo 가져오기 완료
function successGetDeptInfo2(data) {
	cboDeptData2 = data;
	
	$('[data-ax5select="cboDept2"]').ax5select({
        options: injectCboDataToArr(cboDeptData2, "cm_deptcd", "cm_deptname")
	});
}

function changeStaInfo() {
	$("#datStD").prop("disabled",true);
	$("#datEdD").prop("disabled",true);
	disableCal(true, 'datStD');
	disableCal(true, 'datEdD');
	if(getSelectedIndex('cboSta1') < -1) return;
	if(getSelectedIndex('cboSta1') == 0 || getSelectedVal('cboSta1').cm_micode == '3'
		|| getSelectedVal('cboSta1').cm_micode == '8' || getSelectedVal('cboSta1').cm_micode == '9') {
		if(getSelectedVal('cboSta2').cm_micode == '00') {
			$("#datStD").prop("disabled",false);
			$("#datEdD").prop("disabled",false);
			disableCal(false, 'datStD');
			disableCal(false, 'datEdD');
		}else {
			$("#datStD").prop("disabled",true);
			$("#datEdD").prop("disabled",true);
			disableCal(true, 'datStD');
			disableCal(true, 'datEdD');
		}
	}
}

// 진행상태 cbo , 신청종류 cbo 가져오기
function getCodeInfo(){
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('ISRSTA','ALL','N'),
		new CodeInfo('ISRSTAUSR','ALL','N')
	]);
	cboStaData1 	= codeInfos.ISRSTA;
	cboStaData2		= codeInfos.ISRSTAUSR;
	
	var tmpObj = new Object();
	tmpObj.cm_macode = "ISRSTA";
	tmpObj.cm_micode = "XX";
	tmpObj.cm_codename = "미완료전체";
	cboStaData1.push(tmpObj);
	tmpObj = null;
	
	tmpObj = new Object();
	tmpObj.cm_macode = "ISRSTAUSR";
	tmpObj.cm_micode = "XX";
	tmpObj.cm_codename = "미완료전체";
	cboStaData2.push(tmpObj);
	
	$('[data-ax5select="cboSta1"]').ax5select({
        options: injectCboDataToArr(cboStaData1, "cm_micode", "cm_codename")
	});

	$('[data-ax5select="cboSta2"]').ax5select({
        options: injectCboDataToArr(cboStaData2, "cm_micode", "cm_codename")
	});
	
	if(SoJangYn) {
		$('[data-ax5select="cboSta1"]').ax5select('setValue','9', true);
		$('[data-ax5select="cboSta2"]').ax5select('setValue','0', true);
	}else if(SilJangYn) {
		$('[data-ax5select="cboSta2"]').ax5select('setValue','XX', true);
	}else {
		$('[data-ax5select="cboSta2"]').ax5select('setValue','XX', true);
	}
	
	loadSw = true;
	get_SelectList();
}

// 초기화 버튼 클릭
function resetScreen(){
	$("#txtTitle").val('');
	$('#optSub3').wRadio('check', true);
	$('#datStD').val(getDate('DATE',0));
	$('#datEdD').val(getDate('DATE',0));
	picker.bind(defaultPickerInfoLarge('basic', 'top'));
	
	if(cboDeptData1.length > 0) $('[data-ax5select="cboDept1"]').ax5select("setValue", cboDeptData1[0].cm_dpetcd, true);
	if(cboDeptData2.length > 0) $('[data-ax5select="cboDept2"]').ax5select("setValue", cboDeptData2[0].cm_dpetcd, true);
	if(cboStaData1.length > 0) $('[data-ax5select="cboSta1"]').ax5select("setValue", cboStaData1[0].cm_micode, true);
	if(cboStaData2.length > 0) $('[data-ax5select="cboSta2"]').ax5select("setValue", cboStaData2[0].cm_micode, true);
	
	if(cboStaData2.length > 0 && getSelectedIndex('cboSta1') < 1) {
		$('[data-ax5select="cboSta2"]').ax5select("setValue", 'XX', true);
	}
	
	changeStaInfo();
}

// 신청현황 리스트 가져오기
function get_SelectList() {
	var strStD	= $("#datStD").val();
	var strEdD 	= $("#datEdD").val();
	var errSw	= false;
	var tmpObj 	= {};
	
	if(getSelectedIndex('cboSta1') > 0 && getSelectedIndex('cboSta2') > 0) {
		if(getSelectedVal('cboSta2').cm_micode != '3'			//제외
			&& getSelectedVal('cboSta2').cm_micode != '8'		//진행중단
			&& getSelectedVal('cboSta2').cm_micode != '9') {	//완료
			if(getSelectedVal('cboSta1').cm_micode == '0') errSw = true;
			else if(getSelectedVal('cboSta1').cm_micode == '0') errSw = true;
			else if(getSelectedVal('cboSta1').cm_micode == '3') errSw = true;
			else if(getSelectedVal('cboSta1').cm_micode == '8') errSw = true;
			else if(getSelectedVal('cboSta1').cm_micode == '9') errSw = true;
		}
	}
	
	if(errSw) {
		dialog.alert("상태를 정확하게 선택하여 주시기 바랍니다."); 
		return;
	}
	
	tmpObj.userid = userId;
	if (!$("#datStD").is(':disabled') && !$("#datEdD").is(':disabled')) {
		if (strStD > strEdD) {
		   dialog.alert("조회기간을 정확하게 선택하여 주십시오."); 
		   return;
	    }
		
		tmpObj.stday = replaceAllString(strStD, "/", "");
		tmpObj.edday = replaceAllString(strEdD, "/", ""); 
	}
	if (getSelectedIndex("cboDept1") > 0) tmpObj.reqdept = getSelectedVal("cboDept1").cm_deptcd;
	if (getSelectedIndex("cboDept2") > 0) tmpObj.recvdept = getSelectedVal("cboDept2").cm_deptcd;
	if (getSelectedIndex("cboSta1") > 0) {
		tmpObj.reqsta1 = getSelectedVal("cboSta1").cm_micode;
	}
	if (getSelectedIndex("cboSta2") > 0) {
		tmpObj.reqsta2 = getSelectedVal("cboSta2").cm_micode;
	}
	if ($("#txtTitle").val().trim().length > 0) tmpObj.reqtit = $("#txtTitle").val().trim();
	
	var checkVal = $('input[name="rdoGbn"]:checked').val();
	if(checkVal == '2') tmpObj.selfsw = "M";
	else if(checkVal == '1') tmpObj.selfsw = "T";
	else tmpObj.selfsw = "N";
	
	var data =  new Object();
	data = {
		etcData : tmpObj,
		requestType : 'get_SelectList'		
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/common/PrjInfoServlet', data, 'json',successGet_SelectList);
}

// 신청 리스트 가져오기 완료
function successGet_SelectList(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function openWindow(type, isrid) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+isrid) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+isrid;

	var f = document.popPam;   		//폼 name
    f.isrid.value	= isrid;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.rgtList.value = rgtList;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.adminYN.value = adminYN;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 'SRINFO') {
    	cURL = "/webPage/winpop/PopSRInfo.jsp";
	} 
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}