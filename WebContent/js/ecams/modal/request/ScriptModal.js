
var reqCd 		= window.parent.reqCd;			// 메뉴코드
var sysCd		= window.parent.SelSysCd; 			// 시스템코드
var ScriptProgData = window.parent.ScriptProgData;

var firstGrid  	= new ax5.ui.grid();

var firstGridData = null; 							//선후행목록 데이타
var data          = null;							//json parameter
var tmpWord = "";
var selIn = null;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
    	trStyleClass: function () {
    		if (this.item.comperr == 'Y' || this.item.deployerr == 'Y' || 
    				this.item.compchg.indexOf("##PARM##") >= 0 || this.item.deploychg.indexOf("##PARM##") >= 0){
    			return "fontStyle-cncl";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        },
        onClick: function () {
        	setScriptData(this.item, this.dindex);
        }
    },
    columns: [
        {key: "cm_dirpath", label: "디렉토리",  width: '20%', align: 'left'},
        {key: "cr_rsrcname", label: "프로그램명",  width: '12%', align: 'left'},
        {key: "execpos", label: "실행시점",  width: '10%', align: 'left'},
        {key: "compchg", label: "컴파일",  width: '26%', align: 'left'},
        {key: "deploychg", label: "배포",  width: '25%', align: 'left'},
        {key: "chguseryn", label: "배포실행",  width: '8%'}
    ]
});


$(document).ready(function() {
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
	//체크인
	if(reqCd == '07'){
		$('#btnCncl').text("체크인취소");
		$('#btnExit').text("체크인");
		tmpWord = "체크인";
	}
	else if (reqCd == '03'){ //테스트배포
		$('#btnCncl').text("적용취소");
		$('#btnExit').text("QA적용");
		tmpWord = "QA적용";
	}
	else{ //운영배포
		$('#btnCncl').text("적용취소");
		$('#btnExit').text("운영적용");
		tmpWord = "운영적용";
	}
	
	$('#btnExit').bind('click',function(){
		OkPopUpClose();
	});
	
	$('#btnCncl').bind('click',function(){
		popClose();
	});
	
	$('#btnReq').bind('click',function(){
		btnReqClick();
	});
	
	getProgScript();
});

function popClose(){
	window.parent.scriptModal.close();
}

function getProgScript(){
	
	var tmpData = {
		sysCD	: 	sysCd,
		reqCD   :   reqCd,
		progList : ScriptProgData,
		requestType	: 	'getProgScript'
	}
	
	ajaxAsync('/webPage/ecmd/Cmd1200Servlet', tmpData, 'json',successGetProgScript);
}

function successGetProgScript(data){
	if (data == undefined || data == null) return;
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function setScriptData(item, index){
	selIn = null;
	if (item == null || item == undefined) return;
	selIn = index;
	$('#btnReq').prop('disabled', true);
		
	$('#txtDirPath').val(item.cm_dirpath);
	$('#txtRsrcName').val(item.cr_rsrcname);
	$('#txtCompBase').val(item.compshl);
	$('#txtDeployBase').val(item.deployshl);
	if (item.cm_useryn == "Y") $('#chkExec').prop('disabled', false);
	else $('#chkExec').prop('disabled',true);
	
	if (item.chguseryn == "Y") $('#chkExec').wCheck('check',true);
	else $('#chkExec').wCheck('check',false);
	
	if (item.edityn1 == "Y") {
		$('#txtCompChg').prop('readonly', false);
		$('#txtCompChg').prop('disalbed', false);
		if (item.chgparm1 != null) $('#txtCompChg').val(item.chgparm1);	
		else $('#txtCompChg').val("");
	} else {
		$('#txtCompChg').prop('readonly', true);
		$('#txtCompChg').prop('disabled', true);
	}
	if (item.edityn2 == "Y")  {
		$('#txtDeployChg').prop('readonly', false);
		$('#txtDeployChg').prop('disabled', false);
		if (item.chgparm2 != null) $('#txtDeployChg').val(item.chgparm2);	
		else $('#txtDeployChg').val("");
	} else {
		$('#txtDeployChg').prop('readonly', true);
		$('#txtDeployChg').prop('disabled', true);
	}
	if (item.cm_useryn == "Y" || item.edityn1 == "Y" || item.edityn2 == "Y") {
		$('#btnReq').prop('disabled', false);
	}
	
}

function btnReqClick(){
	var tmpOb = new Object();
	var chgTxt = "";
	
	if (firstGrid.list.length == 0) {
		dialog.alert("["+ firstGridData[selIn].cm_dirpath + "/" + firstGridData[selIn].cr_rsrcname + "에 대한 컴파일방법을 정확히 입력하여 주시기 바랍니다.");
		return;
	}
	if (firstGridData[selIn].edityn1 == "Y") {
		if ($('#txtCompChg').val().trim().length == 0) {
			dialog.alert("["+ firstGridData[selIn].cm_dirpath + "/" + firstGridData[selIn].cr_rsrcname + "에 대한 컴파일방법을 정확히 입력하여 주시기 바랍니다.");
			return;
		}
	}
	if (firstGridData[selIn].edityn2 == "Y") {
		if ( $('#txtDeployChg').val().trim().length == 0) {
			dialog.alert("["+ firstGridData[selIn].cm_dirpath + "/" + firstGridData[selIn].cr_rsrcname + "에 대한 배포방법을 정확히 입력하여 주시기 바랍니다.");
			return;
		}
	}
	tmpObj = {};	
	tmpObj = firstGridData[selIn];
	if (firstGridData[selIn].edityn1 == "Y") {
		if (firstGridData[selIn].chgparm1 != $('#txtCompChg').val().trim()) {
			chgTxt = firstGridData[selIn].compshl;
			tmpObj.compchg = replaceAllString(chgTxt, "##PARM##", $('#txtCompChg').val().trim());
			tmpObj.chgparm1 = $('#txtCompChg').val();
		}
	}
	if (firstGridData[selIn].edityn2 == "Y") {
		if (firstGridData[selIn].chgparm2 != $('#txtDeployChg').val().trim()) {
			chgTxt = firstGridData[selIn].deployshl;
			tmpObj.deploychg = replaceAllString(chgTxt, "##PARM##", $('#txtDeployChg').val().trim());
			tmpObj.chgparm2 = $('#txtDeployChg').val().trim();
		}
	}
	if ($('#chkExec').is(':checked')) tmpObj.chguseryn = "Y";
	else tmpObj.chguseryn = "N";
	firstGridData.splice(selIn,1);
	firstGridData.splice(selIn,0,tmpObj);
	firstGrid.setData(firstGridData);
	tmpObj = new Object();
}

function OkPopUpClose()
{
	if (firstGrid.list.length == 0) {
		dialog.alert("실행 할 스크립트유형 정보가 없습니다.");
		window.parent.scriptModalClose();
	}else{
		confirmDialog.confirm({
			msg: tmpWord+' 완료합니다. 계속진행하시겠습니까?',
			btns :{
				ok: {
                    label:'ok'
                },
                cancel: {
                    label:'cancel'
                }
			}
		}, function(){
			if(this.key === 'ok') {
				for (i = 0;firstGridData.length>i;i++) {
					if (firstGridData[i].comperr == "Y" || firstGridData[i].deployerr == "Y") {
						dialog.alert("["+ firstGridData[i].cm_dirpath + "/" + firstGridData[i].cr_rsrcname + "에 대하여 실행할 스크립트유형이 등록되지 않았습니다.");
						return;
					}
					if (firstGridData[i].edityn1 == "Y" && firstGridData[i].compchg.indexOf("?#PARM#")>=0) {
						dialog.alert("["+ firstGridData[i].cm_dirpath + "/" + firstGridData[i].cr_rsrcname + "에 대한 컴파일방법을 정확히 입력하여 주시기 바랍니다.");
						return;
					}
					if (firstGridData[i].edityn2 == "Y" && firstGridData[i].deploychg.indexOf("?#PARM#")>=0) {
						dialog.alert("["+ firstGridData[i].cm_dirpath + "/" + firstGridData[i].cr_rsrcname + "에 대한  배포방법을 정확히 입력하여 주시기 바랍니다.");
						return;
					}
				}
				window.parent.scriptData = firstGridData;
				window.parent.scriptModalClose();
			}
		});
	}
}



