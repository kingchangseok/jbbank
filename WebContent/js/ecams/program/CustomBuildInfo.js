/**
 * [프로그램] 사용자 정의 빌드 정보
 */

var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부

var buildGrid		= new ax5.ui.grid();

var cboSysData		= [];
var rsrcBldData 	= [];
var gbnCdData 		= [];
var rsrcModData		= [];
var dirData 		= [];
var buildGridData 	= [];
var selectedGridIndex = buildGrid.selectedDataIndexs;
var selectedGridItem = buildGrid.list[buildGrid.selectedDataIndexs];


buildGrid.setConfig({
    target: $('[data-ax5grid="buildGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center",
        columnHeight: 30
    },
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
//    	trStyleClass: function () {
//    		if(this.item.cr_status === 'Z'){
//    			return "fontStyle-cncl";
//     		} 
//     		if(this.item.cr_status === '0' && this.item.visible === '1'){
//    			return "fontStyle-ing";
//     		} 
//    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "cr_rsrcname", 	label: "프로그램명", 	 	width: '13%', align: "left"},
    	{key: "cr_story", 		label: "프로그램설명",  	width: '20%', align: "left"},
    	{key: "rsrccd", 		label: "프로그램종류",  	width: '13%', align: "left"},
    	{key: "cm_dirpath", 	label: "프로그램경로",  	width: '20%', align: "left"},
        {key: "sta",		 	label: "실행구분", 		width: '10%', align: "left"},
        {key: "job",	 		label: "커맨드",  		width: '13%', align: "left"},
        {key: "cm_username", 	label: "빌드후모듈", 	 	width: '10%', align: "left"},
        {key: "cr_lastdate", 	label: "모듈유형",  		width: '10%', align: "left"},
        {key: "cr_lstver", 		label: "모듈경로", 		width: '25%', align: "left"}
    ]
});


$(document).ready(function() {
	screenInit();

	getSysCbo();
	
	//시스템 변경
	$('#cboSys').bind('change',function(){
		cboSys_Change();
	});

	//상단 프로그램종류 변경
	$('#cboBaseRsrc').bind('change',function(){
		cboBaseRsrc_Change();
	});
	
	//하단 프로그램종류 변경
	$('#cboRsrcCd').bind('change',function(){
		cboRsrcCd_Change();
	});
	
	//실행구분
	$('#cboGbnCd').bind('change',function(){
		cboGbnCd_Click();
	});

	//조회버튼 클릭
	$('#btnQry').bind('click', function() {
		btnQry_Click();
	});

	//등록버튼 클릭
	$('#btnReg').bind('click',function(){
		btnReg_Click();
	});
	
	//삭제버튼 클릭
	$('#btnDel').bind('click',function(){
		btnDel_Click();
	});
	
//	//프로그램명 입력 후 엔터로 조회
//	$('#txtProg').bind('keypress', function(event) {
//		if(event.keyCode === 13) {
//			$('#btnQry').trigger('click');
//		}
//	});
//	
});


function screenInit(){
	
	buildGrid.setData([]);
	
	$('[data-ax5select="cboBaseRsrc"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboGbnCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboRsrcCd"]').ax5select({
		options: []
	});
	$('[data-ax5select="cboDir"]').ax5select({
		options: []
	});
	$('#txtProg').val('');
}


//시스템
function getSysCbo(){

	var tmpInfo = new Object();
	tmpInfo.UserID 	= userId;
	if(adminYN) {
		tmpInfo.SecuYn = 'N';
	}else {
		tmpInfo.SecuYn = 'Y';
	}
	tmpInfo.SelMsg 	= 'SEL';
	
    tmpInfoData = new Object();
    tmpInfoData = {
    	sysData		:	tmpInfo,	
    	requestType	: 	'getSysInfo_bld'
    }
	ajaxAsync('/webPage/program/CustomBuild', tmpInfoData, 'json',successGetSysCd);
}


function successGetSysCd(data){
	console.log("SysCd= ", data);
	
	cboSysData = data;
	
	var options = [];
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if(cboSysData.length == 2){
		var selectVal = $('select[name=cboSys] option:eq(1)').val();
		$('[data-ax5select="cboSys"]').ax5select('setValue',selectVal,true);
	}

	cboSys_Change();
}

//시스템 변경
function cboSys_Change(){

	screenInit();

	if(getSelectedIndex('cboSys') > 0 && getSelectedVal('cboSys') != null){
		var sysCd = getSelectedVal('cboSys').value;
		getRsrcBld(sysCd); //프로그램 종류 조회
	}
}


//상단 프로그램종류
function getRsrcBld(sysCd) {
	
	var tmpInfoData = new Object();
	tmpInfoData = {
		SysCd		:	sysCd,	
    	requestType	: 	'getRsrcBld'
    }
    ajaxAsync('/webPage/program/CustomBuild', tmpInfoData, 'json',successGetRsrcBld);
}

function successGetRsrcBld(data){
	console.log("RsrcBld= ", data);
	
	rsrcBldData = data;
	var options = [];
	
	for(var i = 0; i < rsrcBldData.length; i++){
		options.push({value: rsrcBldData[i].cm_micode, text: rsrcBldData[i].cm_codename, cm_info: rsrcBldData[i].cm_info});
	};
	
	$('[data-ax5select="cboBaseRsrc"]').ax5select({
		options: options
	});
	
	cboBaseRsrc_Change();
}

function cboBaseRsrc_Change(){
	
	$('#cboRsrcCd').prop('disabled', true);
	$('#txtMod').prop('disabled', true);
	$('#txtMod').val('');
	$('#cboDir').prop('disabled', true);
	
	if (getSelectedIndex('cboBaseRsrc') < 0) return; 
	
	if (getSelectedVal('cboBaseRsrc').cm_info.substr(3,1) == "1") { 
		  var syscd = getSelectedVal('cboSys').value;
		  var cm_micode = getSelectedVal('cboBaseRsrc').value;
		  getRsrcMod(syscd, cm_micode);
		}	
		var tmpObj = new Object();
		
		if (getSelectedVal('cboBaseRsrc').cm_info.substr(0,1) == "1") {
			
			tmpObj.cm_micode = "SYSCB";
			tmpObj.cm_codename = "빌드커맨드";
		}
		if (getSelectedVal('cboBaseRsrc').cm_info.substr(20,1) == "1") {
			tmpObj.cm_micode = "SYSED";
			tmpObj.cm_codename = "릴리즈커맨드";
		}
		
		gbnCdData.push(tmpObj);
		var options = [];
		$('[data-ax5select="cboGbnCd"]').ax5select({
	        options: injectCboDataToArr(gbnCdData, 'cm_micode' , 'cm_codename')
		});
		
		cboGbnCd_Click();
		gbnCdData = [];
}


//하단 프로그램종류
function getRsrcMod(syscd, cm_micode){
	
	var tmpInfo = new Object();
	tmpInfo.SysCd 	= syscd;
	tmpInfo.SelMsg 	= 'SEL';
	tmpInfo.RsrcCd 	= cm_micode;
	
    tmpInfoData = new Object();
    tmpInfoData = {
    	tmpInfo		:	tmpInfo,	
    	requestType	: 	'getRsrcMod'
    }
	ajaxAsync('/webPage/program/CustomBuild', tmpInfoData, 'json',successGetRsrcMod);
}

function successGetRsrcMod(data){
	console.log("RsrcMod= ", data);
	
	rsrcModData = data;
	
	if(rsrcModData.length == 2){
		var selectVal = $('select[name=cboRsrcCd] option:eq(1)').val();
		$('[data-ax5select="cboRsrcCd"]').ax5select('setValue',selectVal,true);
	}
	
	var options = [];
	
	for(var i = 0; i < rsrcModData.length; i++){
		options.push({value: rsrcModData[i].cm_micode, text: rsrcModData[i].cm_codename , cm_exename: rsrcModData[i].cm_exename, cm_samename: rsrcModData[i].cm_samename, cm_samedir: rsrcModData[i].cm_samedir, cm_info: rsrcModData[i].cm_info });
	};
	
	$('[data-ax5select="cboRsrcCd"]').ax5select({
		options: options
	});
	
	cboRsrcCd_Change();
}

function cboRsrcCd_Change(){
	
	$('#txtExeName').val('확장자표시');
	$('#cboDir').prop('disabled', true);
	$('#txtMod').prop('disabled', true);
	
	if (getSelectedIndex('cboRsrcCd') > 0) {
		$('#txtExeName').val(getSelectedVal('cboRsrcCd').cm_exename);
		if (getSelectedVal('cboRsrcCd').cm_samename == "?#USER#") {
			$('#txtMod').prop('disabled', false);
	   	} else {
	   		$('#txtMod').val('');
	   	} 	
		
		if (getSelectedVal('cboRsrcCd').cm_samedir == "/") {
			$('#cboDir').prop('disabled', false);
			
			var syscd = getSelectedVal('cboSys').value;
			var cm_micode = getSelectedVal('cboRsrcCd').value;
			getDir(syscd,cm_micode);
	   	} 	
	 } 
}


//빌드모듈경로
function getDir(syscd,cm_micode){
	
	var tmpInfo = new Object();
	tmpInfo.userId 	= userId;
	tmpInfo.sysCd 	= syscd;
	tmpInfo.secuYn = 'Y';
	tmpInfo.rsrcCd 	= cm_micode;
	tmpInfo.jobCd 	= '';
	tmpInfo.selMsg 	= 'SEL';
	
    tmpInfoData = new Object();
    tmpInfoData = {
    	dirData		:	tmpInfo,	
    	requestType	: 	'GETDIR'
    }
	ajaxAsync('/webPage/dev/ProgRegisterServlet', tmpInfoData, 'json',successGetDir);
}

function successGetDir(data){
	console.log("dir= ", data);
	
	dirData = data;
	var options = [];
	
	$('[data-ax5select="cboDir"]').ax5select({
        options: injectCboDataToArr(dirData, 'cm_dsncd' , 'cm_dirpath')
	});
}


//실행구분
function cboGbnCd_Click(){
	
	$('#cboDir').prop('disabled', true);
	$('#txtMod').prop('disabled', true);
	$('#cboRsrcCd').prop('disabled', true);
	
	if(getSelectedIndex('cboGbnCd') < 0)  return;
	
	var codename = '&nbsp;&nbsp;&nbsp;' + getSelectedVal('cboGbnCd').text;
	$('#lblCmd').html(codename);
		if (getSelectedVal('cboGbnCd').value == "SYSCB") {
			$('#cboRsrcCd').prop('disabled', false);
		} else {
			$('#txtMod').val('');
		}
}


function btnQry_Click(){
	
	if(getSelectedIndex('cboSys') < 1){
		dialog.alert('시스템을 선택하여 주십시오');
		$('#cboSys').focus();
		return;
	}
	if(getSelectedIndex('cboBaseRsrc') < 0){
		dialog.alert('프로그램종류를 선택하여 주십시오');
		$('#cboSys').focus();
		return;
	}
	
	tmpInfo = new Object();
	tmpInfo.UserId = userId;
	tmpInfo.SysCd = getSelectedVal('cboSys').value; //cm_syscd
	tmpInfo.RsrcCd = getSelectedVal('cboBaseRsrc').value; //cm_micode
	tmpInfo.RsrcName = '';
	var txtProg = $('#txtProg').val($('#txtProg').val().trim());
	if($(txtProg.length > 0)) {
		tmpInfo.RsrcName = txtProg.val();
	}
	
	tmpInfoData = new Object();
	tmpInfoData = {
			tmpInfo		: tmpInfo,
			requestType	: 'getFileList' 
	}
	ajaxAsync('/webPage/program/CustomBuild', tmpInfoData, 'json', successProgInfo);
}

function successProgInfo(data){
	console.log("조회= ", data);	
	
	buildGridData = data;
	buildGrid.setData(buildGridData);
	
//  	dataSortField.name = "cr_rsrcname";// "fieldName" 필드명을 할당한다.
}


function btnReg_Click(){
	
	if (getSelectedIndex('cboGbnCd') < 0) {
		dialog.alert("커맨드 실행구분을 선택하여 주시기 바랍니다.");
		$('#cboGbnCd').focus();
		return;
	}	
	
	var txtCmd = $('#txtCmd').val($('#txtCmd').val().trim());
	
    if (txtCmd.val() == '' || txtCmd.val() == null){			
    	dialog.alert("["+$('#lblCmd').text()+"     ]를 입력하여 주십시오.");
    	$('#txtCmd').focus();
	    return;
    }

	if (selectedGridIndex == [] || selectedGridItem == undefined){
		dialog.alert('선택된 목록이 없습니다. 목록에서 삭제 할 대상을 선택한 후 처리하세요.');
		return;
	}
     
    if (getSelectedVal('cboGbnCd').cm_micode == "SYSCB") {
	     if (getSelectedVal('cboBaseRsrc').value(3,1) == "1") {
			 if (getSelecteIndex('cboRsrcCd') < 1) {
			    dialog.alert("빌드모듈에 대한 [프로그램종류]을 선택하여 주십시오.");
			    $('#cboRsrcCd').focus();
			    return;					
	       	 }
		     if (getSelecteVal('cboRsrcCd').cm_samename == "?#USER#") {
		    	 var txtMod = $('#txtMod').val($('#txtMod').val().trim());
			     if (txtMod.val() == '' || txtMod.val() == null){
			    	dialog.alert("[빌드후모듈명]를 입력하여 주십시오.");
			    	$('#txtMod').focus();
				    return;
			     }	
		     }
		     if (getSelecteVal('cboRsrcCd').cm_samedir == "?#USER#") {
			     if (getSelecteIndex('cboDir') < 0){
			    	 dialog.alert("[빌드모듈경로]를 선택하여 주십시오.");
			    	 $('#cboDir').focus();
				     return;
			     }
		     }		
		     if ($('#txtExeName').val() != '' && $('#txtExeName').val() != null) {
			     if (getSelecteVal('cboRsrcCd').value.substr(43,1) == "1") {
			       	 var strExe = $('#txtExeName').val();
			       	 var txtProg = $('#txtProg').val();
			       	 //////////////////////////////////////////////////////////////////////////////////////
				     if (strExe.substr(strExe.length-1) == ",") strExe = strExe.substr(0,strExe.length-1);
				     if (txtProg.substr(txtProg.length - strExe.length) != strExe) {
					     if (txtProg.indexOf(".")>0) {
						     Alert.show("확장자를 정확하게 입력하여 주십시오.  ["+strExe + "]");
						     $('#txtProg').focus();
						     return;
					     }
					     $('#txtProg').val($('#txtProg').val()+strExe);
				     }		 				
			      } else if (txtProg.indexOf(".")>0) {
				       var strWork1 = txtProg.substr(txtProg.indexOf("."))+",";
				       var strWork2 = txtExeName.val().toUpperCase();
					   strWork1 = strWork1.toUpperCase();
				       if (strWork2.indexOf(strWork1) < 0) {
				    	   dialog.alert("확장자를 정확하게 입력하여 주십시오. ["+txtExeName.val() + "]");
						   return;
					    }
				   }  else {
					   dialog.alert("확장자를 정확하게 입력하여 주십시오. ["+txtExeName.val() + "]");
					    return;
				   }
		      } 
	       }
      }
     
	var tmpInfo = new Object();
	tmpInfo.cr_itemid = selectedGridItem.cr_itemid;
	tmpInfo.cmdname = $('#txtCmd').val($('#txtCmd').val().trim()); 
	tmpInfo.samersrc = "";
	tmpInfo.samedir = "";
	tmpInfo.modname = "";
	tmpInfo.prcsys = getSelectedItem('cboGbnCd').value;
	if (getSelectedVal('cboBaseRsrc').value.substr(3,1) == "1" && getSelectedVal('cboGbnCd').value == "SYSCB") {
		tmpInfo.samersrc = getSelectedVal('cboRsrcCd').value;
	    if (getSelectedVal('cboRsrcCd').cm_samename == "?#USER#") {
	    	tmpInfo.modname = $('#txtMod').val($('#txtMod').val().trim());
		}
		if (getSelectedVal('cboRsrcCd').cm_samedir == "?#USER#") {
			tmpInfo.samedir = getSelectedVal('cboDir').value;	
		}
	}
		        
	var tmpInfoData = new Object();
	tmpInfoData = {
		UserId		: userId,
		dataList	: [tmpInfo],
		requestType	: 'updateProg'
	}

	ajaxAsync('/webPage/program/CustomBuild', tmpInfo, 'json', successUpdate);
}

function successUpdate(data) {
	console.log("등록= ", data);
	
	if(Number(data) > 0) {
		dialog.alert('등록처리를 완료하였습니다.');
		btnQry_Click();
	}else {
		dialog.alert('등록처리 중 오류가 발생하였습니다.');
	} 
}


function btnDel_Click() {
	
	if (selectedGridIndex == [] || selectedGridItem == undefined){
		dialog.alert('선택된 목록이 없습니다. 목록에서 삭제 할 대상을 선택한 후 처리하세요.');
		return;
	}
	
	confirmDialog.confirm({
		msg: '선택된 프로그램을 형상관리DB에서 삭제하시겠습니까?',
	}, function(){
		if(this.key === 'ok') {
			var tmpInfo = new Object();
			    tmpInfo = {
			    UserId		: userId,
			    dataList	: [selectedGridItem], //서블릿에서 ArrayList로 받고 있음
				requestType	: 'deleteProg'
			}
			ajaxAsync('/webPage/program/CustomBuild', tmpInfo, 'json', successDELETE);
		}
	});
}

function successDELETE(data) {
	console.log("삭제= ", data);
	
	if(Number(data) > 0) {
		dialog.alert('삭제처리를 완료하였습니다.');
		btnQry_Click();
	}else {
		dialog.alert('삭제처리 중 오류가 발생하였습니다.');
	} 
}


