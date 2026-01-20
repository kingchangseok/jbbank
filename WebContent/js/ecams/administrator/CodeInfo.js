/**
 * 코드정보 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-25
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var codeGrid		= new ax5.ui.grid();
var jobModal 		= new ax5.ui.modal();

var codeGridData	= [];
var cboQryData		= [];
var cboCloseYnData	= [];

codeGrid.setConfig({
    target: $('[data-ax5grid="codeGrid"]'),
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
        onDBLClick: function () {
        	clickCodeGrid(this.dindex);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_macode", 	 label: "대구분",  		width: '10%', align: "left"},
        {key: "cm_micode",	 label: "소구분",  		width: '10%'},
        {key: "cm_codename", label: "코드명칭",  		width: '30%', align: "left"},
        {key: "cm_seqno", 	 label: "소구분순서",  	width: '10%'},
        {key: "cm_creatdt",  label: "등록일",  		width: '10%'},
        {key: "cm_lastupdt", label: "최종등록일",  	width: '10%'},
        {key: "closeYN", 	 label: "사용여부",  		width: '10%'},
        {key: "cm_closedt",  label: "폐기일",  		width: '10%'},
    ]
});

$('[data-ax5select="cboQry"]').ax5select({
    options: []
});
$('[data-ax5select="cboCloseYn"]').ax5select({
	options: []
});

$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function() {
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	getCodeInfo();
	
	// 대구분 코드값 엔터
	$('#txtMaCode').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('[data-ax5select="cboQry"]').ax5select('setValue', '00', true);
			$('#btnQry').trigger('click');
		}
	});
	
	// 대구분 코드값 한글입력 막기
	$("#txtMaCode").keyup(function(event) { 
		if (!(event.keyCode >=37 && event.keyCode<=40)) {
			var v = $(this).val();
			$(this).val(v.replace(/[^a-z0-9]/gi,''));
		}
	});
	
	// 대구분 코드설명 엔터
	$('#txtMaName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('[data-ax5select="cboQry"]').ax5select('setValue', '01', true);
			$('#btnQry').trigger('click');
		}
	});
	// 소구분 코드값 엔터
	$('#txtMiCode').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('[data-ax5select="cboQry"]').ax5select('setValue', '02', true);
			$('#btnQry').trigger('click');
		}
	});
	// 소구분 코드설명 엔터
	$('#txtMiName').bind('keypress', function(event) {
		if(event.keyCode === 13) {
			$('[data-ax5select="cboQry"]').ax5select('setValue', '03', true);
			$('#btnQry').trigger('click');
		}
	});
	
	// 업무정보
	$('#btnJob').bind('click', function() {
		setTimeout(function() {			
			openJobModal();
		}, 200);
	});
	// 조회
	$('#btnQry').bind('click', function() {
		getCodeList();
	});
	// 적용
	$('#btnReq').bind('click', function() {
		setCodeValue();
	});
	// 엑셀저장
	$('#btnExl').bind('click', function() {
		excelExport(codeGrid,"CodeInfo.xls");
	});
	//Clear
	$('#btnClear').bind('click', function() {
		$('#txtMaCode').val('');
		$('#txtMaName').val('');
		$('#txtMiCode').val('');
		$('#txtMiName').val('');
		$('[data-ax5select="cboQry"]').ax5select('setValue', '00', true);
		$('#btnQry').trigger('click');
	});
});

// 업무정보 모달 오픈
function openJobModal() {
	setTimeout(function() {
		jobModal.open({
	        width: 800,
	        height: 700,
	        iframe: {
	            method: "get",
	            url: "../modal/sysinfo/JobModal.jsp"
	        },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	                mask.close();
	            }
	        }
	    }, function () {
	    });
	}, 200);
}

// 코드정보 적용
function setCodeValue() {
	var txtMaCode = $('#txtMaCode').val().trim();
	var txtMaName = $('#txtMaName').val().trim();
	var txtMiCode = $('#txtMiCode').val().trim();
	var txtMiName = $('#txtMiName').val().trim();
	var txtSeq 	  = $('#txtSeq').val().trim();
	
	if(txtMaName.length === 0 ) {
		dialog.alert('대구분 코드값을 입력하여 주십시오.', function() {});
		return;
	}
	if(txtMaCode.length === 0 ) {
		dialog.alert('대구분 코드설명을 입력하여 주십시오.', function() {});
		return;
	}
	if(txtMiCode.length === 0 ) {
		dialog.alert('소구분 코드값을 입력하여 주십시오.', function() {});
		return;
	}
	if(txtMiName.length === 0 ) {
		dialog.alert('소구분 코드설명을 입력하여 주십시오.', function() {});
		return;
	}
	
	$('#txtMaCode').val($('#txtMaCode').val().trim().toUpperCase());
	txtMaCode = $('#txtMaCode').val();
	
	var dataObj = new Object();
	dataObj.UserId	= userId;
	dataObj.CboMicode = getSelectedVal('cboQry').value;
	dataObj.Txt_Code0 = txtMaCode;
	dataObj.Txt_Code1 = txtMaName;
	dataObj.Txt_Code2 = txtMiCode;
	dataObj.Txt_Code3 = txtMiName;
	dataObj.Txt_Code4 = txtSeq;
	dataObj.closeYN = $('#optUse').is(':checked') ? '사용' : '미사용';
	var data = new Object();
	data = {
		dataObj 	: dataObj,
		requestType	: 'setCodeValue'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successgSetCodeValue);
}

// 코드정보 적용 완료
function successgSetCodeValue(data) {
	dialog.alert('코드설명 [' + data +'] 이(가) 적용 되었습니다.', function() {
		var selIn 	= codeGrid.selectedDataIndexs;
		var selItem = null;
		if(selIn.length !== 0 ) {
			selItem = codeGrid.list[selIn];
			$('#txtMaCode').val(selItem.cm_macode);
		}
		$('#txtMiCode').val('****');
		$('#txtMiName').val('');
		$('[data-ax5select="cboQry"]').ax5select('setValue', '02', true);
		$('#btnQry').trigger('click');
	});
}

//코드 리스트 클릭
function clickCodeGrid(index) {
	var selItem = codeGrid.list[index];
	
	if(selItem.cm_micode === '****') {
		$('#txtMaCode').val(selItem.cm_macode);
		$('#txtMaName').val(selItem.cm_codename);
		$('#txtMiCode').val(selItem.cm_micode);
		$('#txtMiName').val('');
		$('[data-ax5select="cboQry"]').ax5select('setValue', '02', true);
		
		if(selItem.closeYN === '사용') {
			$('#optUse').wRadio('check', true);
		} else {
			$('#optNotUse').wRadio('check', true);
		}
		
		$('#btnQry').trigger('click');
	} else {
		getCodeName();
	}
}

// 소구분명 가져오기
function getCodeName() {
	var data = new Object();
	data = {
		txtCode0 	: $('#txtMaCode').val(),
		requestType	: 'getCodeName'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successgGetCodeName);
}

// 소구분명 가져오기 완료
function successgGetCodeName(data) {
	var selItem = codeGrid.list[codeGrid.selectedDataIndexs[0]];
	$('#txtMaCode').val(selItem.cm_macode);
	$('#txtMaName').val(data);
	$('#txtMiCode').val(selItem.cm_micode);
	$('#txtMiName').val(selItem.cm_codename);
	$('#txtSeq').val(selItem.cm_seqno);
	
	if(selItem.closeYN === '사용') {
		$('#optUse').wRadio('check', true);
	} else {
		$('#optNotUse').wRadio('check', true);
	}
}

// 코드 리스트 조회
function getCodeList() {
	var selVal = getSelectedVal('cboQry').value;
	var txtMaCode = $('#txtMaCode').val().trim();
	var txtMaName = $('#txtMaName').val().trim();
	var txtMiCode = $('#txtMiCode').val().trim();
	var txtMiName = $('#txtMiName').val().trim();
	
	if(selVal !== '00') {
		if(selVal === '01' && txtMaName.length === 0 ) {
			dialog.alert('조회할 대구분 코드설명을 입력하여 주십시오.', function() {});
			return;
		}
		if(selVal !== '01' && txtMaCode.length === 0 ) {
			dialog.alert('대구분 코드값을 입력하여 주십시오.', function() {});
			return;
		}
		if(selVal === '02' && txtMiCode.length === 0 ) {
			dialog.alert('조회할 소구분 코드값을 입력하여 주십시오.', function() {});
			return;
		}
		if(selVal === '03' && txtMiName.length === 0 ) {
			dialog.alert('조회할 소구분 코드설명을 입력하여 주십시오.', function() {});
			return;
		}
	}
	$('#txtMaCode').val($('#txtMaCode').val().trim().toUpperCase());
	txtMaCode = $('#txtMaCode').val();
	
	
	var dataObj = new Object();
	dataObj.UserId		= userId;
	dataObj.CboMicode 	= selVal;
	dataObj.Txt_Code0 	= txtMaCode;
	dataObj.Txt_Code1 	= txtMaName;
	dataObj.Txt_Code2 	= txtMiCode;
	dataObj.Txt_Code3 	= txtMiName;
	
	var data = new Object();
	data = {
		dataObj 	: dataObj,
		requestType	: 'getCodeList'
	}
	$('[data-ax5grid="codeGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successGetCodeList);
}

// 조회 완료
function successGetCodeList(data) {
	$(".loding-div").remove();
	codeGridData = data;
	codeGrid.setData(codeGridData);
}

// 검색조건 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([
		new CodeInfo('CODEINFO','','N'),
	]);
	cboQryData 	= codeInfos.CODEINFO;
	
	$('[data-ax5select="cboQry"]').ax5select({
		options: injectCboDataToArr(cboQryData, 'cm_micode' , 'cm_codename')
	});
}