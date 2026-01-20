/**
 * 개발툴연계 화면
 * 
 * <pre>
 * 	작성자	: 허정규
 * 	버전 		: 1.0
 *  수정일 	: 2019-08-26
 * 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부

var firstGrid		= new ax5.ui.grid();

var cboSysData		= [];
var cboJobData		= [];
var cboRsrcCdData	= [];
var firstGridData 	= [];
var myWin			= null;
 
firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if(this.item.cr_status === 'Z'){
    			return "fontStyle-cncl";
     		} 
     		if(this.item.cr_status === '0' && this.item.visible === '1'){
    			return "fontStyle-ing";
     		} 
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_rsrcname", 	label: "프로그램명",  	width: '20%', align: "left"},
        {key: "cr_story",	 	label: "프로그램설명", 	width: '30%', align: "left", editor: {type: "text"}},
        {key: "rsrctype", 		label: "리소스타입",  	width: '20%'},
        {key: "cm_dirpath", 	label: "프로그램경로", 	width: '30%', align: "left"}
    ]
});

$('[data-ax5select="cboSys"]').ax5select({
    options: []
});
$('[data-ax5select="cboJob"]').ax5select({
	options: []
});

$(document).ready(function() {
	$('#cboSys').bind('change',function(){
		if(getSelectedVal('cboSys').cm_syscd == '00080') {	//애니링크
			$('#lbl1').text('서비스명');
		}else {
			$('#lbl1').text('검색단어');
		}
		getJobCbo();
	});

	$('#btnList').bind('click',function(){
		getPfmList();
	});

	$('#btnReq').bind('click',function(){
		insertPrg();
	});
	
	$('#btnExcel').bind('click',function(){
		excelExport(firstGrid,"DevToolList.xls");
	});
	
	$("#txtSearch").keyup(function(event) { 
		if(event.keyCode === 13) {
			$('#btnList').trigger('click');
		}
	});
	
	getSysCbo();
});

//Cmd0101.getSysCd(strUserId);
function getSysCbo(){
	var tmpData = {
		UserID : userId,
		requestType : 'getSysCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd0101Servlet', tmpData, 'json',successGetSysCd);
}

function successGetSysCd(data){
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, 'cm_syscd' , 'cm_sysmsg')
	});

	if(cboSysData.length > 0){
		if(cboSysData.length == 2) {
			$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[1].cm_syscd,true);
			$('#cboSys').trigger('change');
		}else {
			$('[data-ax5select="cboSys"]').ax5select('setValue',cboSysData[0].cm_syscd,true);
		}
	}
}

//Cmd0101.getJobCd(strUserId, cboSysCd.selectedItem.cm_syscd);
function getJobCbo(){
	var tmpData = {
		UserID : userId,
		Syscd : getSelectedVal('cboSys').cm_syscd,
		requestType : 'getJobCd'
	}
	ajaxAsync('/webPage/ecmd/Cmd0101Servlet', tmpData, 'json',successGetJobCbo);
}

function successGetJobCbo(data){
	cboJobData = data;
	$('[data-ax5select="cboJob"]').ax5select({
        options: injectCboDataToArr(cboJobData, 'cm_jobcd' , 'cm_jobname')
	});
	
	if(cboJobData.length == 2) {
		$('[data-ax5select="cboJob"]').ax5select('setValue',cboJobData[1].cm_jobcd,true);
	}
}

function getPfmList(){
	firstGridData = [];
	firstGrid.setData([]);
	
	if (getSelectedIndex('cboSys') <= 0) {
		dialog.alert("시스템을 선택하여 주시기 바랍니다."); 
		return;
	}
	
	if (getSelectedIndex('cboJob') <= 0 && $('#txtSearch').val().trim().length == 0) {
		dialog.alert("업무 또는 검색단어를 입력하여 주시기 바랍니다."); 
		return;
	}
	
	if(getSelectedVal('cboSys').cm_syscd == "00080") {
		if($('#txtSearch').val().trim().length == 0) {
			dialog.alert("애니링크시스템 추출 시 서비스명을 필수 입력 하시기 바랍니다."); 
			return;
		}
	}
	
	//Cmd0101.getPfmList(strUserId,mx.utils.StringUtil.trim(txtProg.text),cboSysCd.selectedItem.cm_syscd,cboJobCd.selectedItem.cm_jobcd);
	var tmpData = {
		UserId		: userId,
		rsrcName	: $("#txtSearch").val().trim(),
		SysCd		: getSelectedVal('cboSys').cm_syscd,
		JobCd		: getSelectedVal('cboJob').cm_jobcd,
		requestType	: 'getPfmList'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmd/Cmd0101Servlet', tmpData, 'json',successGetPfmList);
}

function successGetPfmList(data){
	$(".loding-div").remove();
	
	if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
		dialog.alert(data);
		return;
	}
	
	firstGridData = data;
	$(firstGridData).each(function(i){
		if(firstGridData[i].visible <= 0) {
			firstGridData[i].filterData = true;
			firstGridData[i].__disable_selection__ = true;
		}else {
			firstGridData[i].filterData = false;
			firstGridData[i].__disable_selection__ = '';
		}
	});
	firstGrid.setData(firstGridData);
}

function insertPrg(){
	if (getSelectedIndex('cboSys') <= 0) {
		dialog.alert("시스템을 선택하여 주시기 바랍니다.");
		return;
	}
	
	var firstGridSel = firstGrid.getList('selected');
	var firstGridSelected = [];
	if(firstGridSel.length < 1){
		dialog.alert("등록할 프로그램을 선택한 후 처리하시기 바랍니다.");
		return;
	}
	
	for (var i=0;firstGridSel.length>i;i++) {				
		if (firstGridSel[i].cr_status != "Z") {
			dialog.alert("미등록상태인 프로그램에 대해서만 가능합니다. ["+ firstGridSel[i].cr_rsrcname + "]");
			return;
		} else if (firstGridSel[i].cr_story == null && firstGridSel[i].cr_story == "") {
			dialog.alert("프로그램설명을 입력하여 주시기 바랍니다. ["+firstGridSel[i].cr_rsrcname+"]");
			return;
		}
		firstGridSelected.push(firstGridSel[i]);
	}
	
	
	var tmpObj = new Object();
	var tmpArray = new Array();
	var mciSw	= false;
	j = 0;
	for (i=0;firstGridSelected.length>i;i++) {
		if (firstGridSelected[i].cr_status == "Z") {
			tmpObj = new Object();
			/*var strExe = firstGridSelected[i].cm_exename;
			if (strExe != null && strExe != "") {
					if (strExe.substr(strExe.length-1) == ",") strExe = strExe.substr(0,strExe.length-1);
			}*/
			tmpObj.cm_dirpath = firstGridSelected[i].cm_dirpath;
			tmpObj.cm_info = firstGridSelected[i].cm_info;
			tmpObj.cm_dsncd = "";
			tmpObj.cr_jobcd = firstGridSelected[i].jobcd;
			tmpObj.cr_rsrccd = firstGridSelected[i].cr_rsrccd;
			tmpObj.cr_rsrcname = firstGridSelected[i].cr_rsrcname;
//			if (firstGridSelected[i].cm_info.substr(43,1) == "1") {
//				if (firstGridSelected[i].cr_rsrcname.indexOf(".")<0) {
//					tmpObj.cr_rsrcname = firstGridSelected[i].cr_rsrcname + strExe;
//				}
//			}
			tmpObj.cr_story = "";
			if (firstGridSelected[i].cr_story != null && firstGridSelected[i].cr_story != "") {
				tmpObj.cr_story = firstGridSelected[i].cr_story
			}
			tmpArray.push(tmpObj);
			tmpObj = null;
		} 
	}
	tmpObj = new Object();
	tmpObj.userid = strUserId;
	tmpObj.cr_syscd = getSelectedVal('cboSys').cm_syscd;
	tmpObj.cr_jobcd = getSelectedVal('cboJob').cm_jobcd;
	tmpObj.cm_jobname = getSelectedVal('cboJob').cm_jobname;
	
	var tmpData = {
		etcData : tmpObj,
		fileList : tmpArray,
		requestType : "insCmr0020"
	}
	
	ajaxAsync('/webPage/ecmd/Cmd0101Servlet', tmpData, 'json',successInsertPrg);
}

function successInsertPrg(data) {
	if (data == "OK") {
		dialog.alert("선택한 건에 대한 신규등록처리가 완료되었습니다. 결과를 확인하시기 바랍니다.");
	
		for (var i=0;i<firstGridData.length;i++){
			if (firstGridData[i].visible == "1" && firstGridData[i].checked == "1") {
				firstGridData[i].visible = "0";
				firstGridData[i].__disable_selection__ = true;
				firstGridData[i].checked = "0";
				firstGridData[i].cr_status = "3";						
			}
		}
		firstGrid.repaint();
	} else {
		dialog.alert(data);
	}
}