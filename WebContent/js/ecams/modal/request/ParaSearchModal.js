var userId 		= window.parent.userId;
var sysCd 		= window.parent.sysCd;
var itemId 		= window.parent.tmpArr;

var firstGrid  	= new ax5.ui.grid();

var firstGridData = [];

var i = 0;
var closeFlag = false;
var data = null;
var tmpArr = [];
var tmpArr2 = [];
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : true,
    showLineNumber: true,
    showRowSelector: true, 
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
    },
    columns: [
        {key: "column11", 	label: "상태",		width: '7%'},
        {key: "column0",	label: "업무코드",		width: '8%'},
        {key: "column5",	label: "프로그램종류",	width: '13%'},
        {key: "column7",	label: "자원구분코드",	width: '12%'},
        {key: "column1",	label: "시번",		width: '12%'},
        {key: "column2",	label: "프로그램명",	width: '17%'},
        {key: "column3",	label: "프로그램설명",	width: '17%'},
        {key: "column10",	label: "이름",		width: '7%'},
        {key: "column4",	label: "디렉토리",		width: '19%'},
        {key: "column6",	label: "언어명칭",		width: '12%'},
        {key: "column8",	label: "언어코드",		width: '12%'},
        {key: "column9",	label: "디렉토리코드",	width: '12%'},
        {key: "column12",	label: "ItemID",	width: '12%'},
        {key: "column13",	label: "status",	width: '12%'}
    ]
});

$(document).ready(function() {
	$('#btnSubmit').prop("disabled", true);
//	$('#chkAll').wCheck("disabled", true);
	
	paraSearch();
	
//	$('#chkAll').bind('click',function() {
//		chkAll();
//	});
	
	$('#btnClose').bind('click',function() {
		popClose();
	});
	
	//대여요청
	$('#btnSubmit').bind('click',function(){
		btnSubmit_Click();
	});
	
	columns = firstGrid.config.columns;
	columns[3].hidden = true;
	columns[4].hidden = true;
	columns[10].hidden = true;
	columns[11].hidden = true;
	columns[12].hidden = true;
	columns[13].hidden = true;
	firstGrid.config.columns = columns;
	firstGrid.setConfig();
});

function popClose(flag){
	window.parent.paraSearchModal.close();
}

function paraSearch() {
	data = new Object();
	data = {
		Itemid : itemId,
		userid : userId,
		syscod : sysCd,
		requestType : 'Cmr0100_Parasearch'
	}
	ajaxAsync('/webPage/ecmr/Cmr0100Servlet', data, 'json',successParaSearch);
}

function successParaSearch(data) {
	if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}
	
	if (data == null) {
		dialog.alert("신규대상목록 추출을 위한 작업에 실패하였습니다. [작성된 파일 없음]");
	} else {
		firstGridData = data;
		firstGrid.setData(firstGridData);
		
		if (firstGridData.length == 0) {
			dialog.alert("목록이 없습니다.");
			popClose();
		}
	}
	
	for (i = 0; i < firstGridData.length; i++) {
		if (firstGridData[i].column13 == "0" || firstGridData[i].column13 == "-") {
			firstGridData[i].enable = "1";
		} else {
			firstGridData[i].enable = "0";
		}
		
		if (firstGridData[i].enable = "1") {
			$('#btnSubmit').prop("disabled", false);
//			$('#chkAll').wCheck("disabled", false);
		}
	}
}

function btnSubmit_Click() {
	var k = 0;
	var c = 0;
	var a = 0;
	var temb = true;
	var firstGridSelected = firstGrid.getList("selected");
	
//	for (i = 0; i < firstGridData.length; i++) {
//		if(firstGridData[i].selected == "1") {
//			a++;
//		}
//	}
	tmpArr2 = [];
	tmpArr = [];
	
	if (firstGridSelected.length == 0) {
		dialog.alert("등록 대상을 선택하십시오.");
		return;
	}
	
	for (var i = 0; i < firstGridSelected.length; i++) {
		tmpObj = new Object;
		tmpObj.UserId = userId;
		tmpObj.syscd = sysCd;
		tmpObj.jobcd = firstGridSelected[i].column0;
		tmpObj.RsrcName = firstGridSelected[i].column2;
		tmpObj.Story = firstGridSelected[i].column3;
		tmpObj.DirPath = firstGridSelected[i].column4;
		tmpObj.ExtName = firstGridSelected[i].column6;
		tmpObj.Rsrccd = firstGridSelected[i].column7;
		tmpObj.LangCd = firstGridSelected[i].column8;
		tmpObj.Dsncd = firstGridSelected[i].column9;
		tmpObj.ProgramType = firstGridSelected[i].column5;
		tmpObj.Last = firstGridSelected[i].column10;
		tmpObj.state = firstGridSelected[i].column11;
		tmpObj.ItemID = firstGridSelected[i].column12;
		tmpObj.status1 = firstGridSelected[i].column13;
		
		if (firstGridSelected[i].column13 == "-") {
			tmpArr2.push(tmpObj);
			temb = false;
		} else {
			tmpArr.push(tmpObj);
		}
		tmpObj = null;
		
		if (!temb) {
			cmd0100_Insert(tmpArr2);
		}
		tmpArr2 = null;
	}
	closeFlag = true;
	window.parent.closeLangInfo(closeFlag, tmpArr);
}

function cmd0100_Insert(tmpData) {
	data = new Object();
	data = {
		getCmd0100List : tmpData,
		requestType : 'cmd0100_Insert'
	}
	ajaxAsync('/webPage/ecmd/Cmd0100Servlet', data, 'json',successInsert);
}

function successInsert(data) {
	
}

//function chkAll() {
//	for (i = 0; i < firstGridData.length; i++) {
//		if ($('#chkAll').is(":checked")) {
//			if (firstGridData[i].enabled == "0") {
//				firstGridData[i].selected == "0"
//			} else {
//				firstGridData[i].selected == "1"
//			}
//		}
//	}
//}
