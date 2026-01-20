/**
 * 언어정보 모달 화면 기능 정의
 *
 * <pre>
 * 	작성자	: 정선희
 * 	버전 		: 1.0
 *  수정일 	: 2021-01-04
 *
 */

var langGrid 	 	= new ax5.ui.grid();
var langGridData	= [];
var cboLangData		= [];

langGrid.setConfig({
    target: $('[data-ax5grid="langGrid"]'),
    sortable: true,
    multiSort: false,
    showRowSelector: false,
    multipleSelect: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            langGridClick(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "CM_MICODE", 		label: "언어코드",  		width: '15%'},
        {key: "CM_CODENAME", 	label: "언어명",  		width: '20%', align: 'left'},
        {key: "CM_EXENAME", 	label: "확장자",  		width: '40%', align: 'left'},
        {key: "CM_EXENO", 		label: "확장자없이 사용가능",	width: '20%'}
    ]
});

$('[data-ax5select="cboLang"]').ax5select({
    options: []
});

$(document).ready(function(){
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

	getCboRsrc();
	getLangList();

	//등록
	$('#btnRegist').bind('click', function() {
		btnRegistClick();
	});
	
	//폐기
	$('#btnDelete').bind('click', function() {
		btnDeleteClick();
	});
	
	//조회
	$('#btnSearch').bind('click', function() {
		getLangList();
	});

	$('#cboLang').bind('change', function() {
		cboLangChange();
	});
});

function getCboRsrc() {
	var data = new Object();
	data = {
		SelMsg : 'SEL',
		requestType : 'getCboRsrc'
	}
	ajaxAsync('/webPage/ecmm/Cmm0023Servlet', data, 'json', successGetCboRsrc);
	data = null;
}

function successGetCboRsrc(data) {
	cboLangData = data;
	cboLangData.forEach(function(item,index) {
		if(item.cm_micode == '0000') item.text = item.cm_codename;
		else item.text = item.cm_codename + '[' + item.cm_micode + ']';
	});
	$('[data-ax5select="cboLang"]').ax5select({
	    options: injectCboDataToArr(cboLangData, 'cm_micode' , 'text')
	});
}

function cboLangChange() {
	var selLangItem = getSelectedVal('cboLang');
	
	$('#txtLangcd').val(selLangItem.cm_micode);
	$('#txtLangname').val(selLangItem.cm_codename);
	$('#txtExename').val(selLangItem.cm_exename);
	if(selLangItem.cm_exeno == 'Y') $('#chkExename').wCheck("check", true);
	else $('#chkExename').wCheck("check", false);
	
	for(var i=0; i<langGridData.length; i++) {
		if(langGridData[i].cm_micode == selLangItem.cm_micode) {
			$('#txtExename').val(langGridData[i].cm_exename);
			break;
		}
	}
}

//조회
function getLangList() {
	$('[data-ax5grid="langGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();

	var rsrcList = new Object();
	rsrcList.CM_LANGCD = $('#txtLangcd').val();
	rsrcList.CM_EXENAME = $('#txtExename').val();
	
	var data = {
		rsrcList : rsrcList,
		requestType	: 'getRsrcList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0023Servlet', data, 'json', successGetRsrcList);
}

function successGetRsrcList(data) {
	$(".loding-div").remove();
	langGridData = data;
	langGrid.setData(langGridData);
}

function langGridClick(index) {
	var selItem = langGrid.list[index];

	$('[data-ax5select="cboLang"]').ax5select('setValue',selItem.CM_MICODE,true);

	$('#txtLangcd').val(selItem.CM_MICODE);
	$('#txtLangname').val(selItem.CM_CODENAME);
	$('#txtExename').val(selItem.CM_EXENAME);

	if (selItem.CM_EXENO == 'Y') $('#chkExename').wCheck("check", true);
	else $('#chkExename').wCheck("check", false);
}

//등록
function btnRegistClick() {
	var langcd = $('#txtLangcd').val().trim();
	var langname = $('#txtLangname').val().trim();
	var exename = $('#txtExename').val().trim();
	
	if (langcd.length == 0) {
		dialog.alert('언어코드를 입력후 등록하십시오.');
		return;
	}
	if (langname.length == 0) {
		dialog.alert('언어명을 입력후 등록하십시오.');
		return;
	}
	
	var tmpObj = new Object();
	tmpObj.CM_LANGCD = langcd;
	tmpObj.CM_CODENAME = langname;
	
	if(exename.length == 0) tmpObj.CM_EXENAME = '';
	else tmpObj.CM_EXENAME = exename;
	
	if($('#chkExename').is(':checked')) tmpObj.CM_EXENO = 'Y';
	else tmpObj.CM_EXENO = 'N';
	
	var data = {
		rsrc : tmpObj,
		requestType	: 'setRsrc'
	}
	ajaxAsync('/webPage/ecmm/Cmm0023Servlet', data, 'json', successSetRsrc);
}

function successSetRsrc(data) {
	if(data == 'Y') {
		dialog.alert('등록되었습니다.');
	}else {
		dialog.alert('등록이 실패되었습니다.');
	}
	
	getLangList();
}

//폐기
function btnDeleteClick() {
	var langcd = $('#txtLangcd').val().trim();
	var langname = $('#txtLangname').val().trim();
	var exename = $('#txtExename').val().trim();
	
	confirmDialog.confirm({
		title: "폐기 확인",
		msg: "언어명 ["+ langname +"]을(를) 폐기하시겠습니까?"
	}, function() {
		if(this.key == "ok") {
			var tmpObj = new Object();
			tmpObj.CM_LANGCD = langcd;
			tmpObj.CM_CODENAME = langname;
			
			var data = {
				rsrcDel : tmpObj,
				requestType	: 'delRsrc'
			}
			ajaxAsync('/webPage/ecmm/Cmm0023Servlet', data, 'json', successDelRsrc);
		}
	});
}

function successDelRsrc(data) {
	if(data == 'Y') {
		dialog.alert('삭제되었습니다.');
	}else {
		dialog.alert('삭제가 실패되었습니다.');
	}
	
	getLangList();
}

function popClose(){
	window.parent.langModal.close();
}