/**
 * 영향분석확인 상세 (eCmr1010.mxml)
 * 
 * 	작성자: 진가윤
 * 	버전 : 1.0
 *  수정일 : 2022-09-00
 * 
 */

var pUserId 	= window.parent.pUserId;
var pAcptNo	    = window.parent.pReqNo;

//var f = document.getReqData;
//pUserId = f.userId.value;
//pAcptNo = f.acptno.value;

var astaGrid		= new ax5.ui.grid();

var astaGridData		= [];
var astaGridData2		= [];

astaGrid.setConfig({
	target : $('[data-ax5grid="astaGrid"]'),
	showLineNumber : true,
	showRowSelector : false,
	multipleSelect : false,
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
		{key : "cr_refuser",	label : "확인대상자",		width: "6%",	align: "center"	},
		{key : "cr_chkflag",	label : "확인구분",		width: "6%",	align: "center"	},
		{key : "cr_chkusser",	label : "확인자",			width: "6%",	align: "center"	}, 
		{key : "cr_chkdate",	label : "확인일시",		width: "12%",	align: "center"	} 
	]
});

$('input.checkbox-file').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	console.log(pUserId,pAcptNo);
	//조회
	$("#btnQry").bind('click', function() {
		getAsta();
	})
	//엑셀저장
	$('#btnExcel').bind('click', function() {
  	    excelExport(astaGrid, "영향분석확인.xls");
	});
	//닫기
	$('#btnClose').bind('click', function() {
		btnClose_Click();
	});
	
	//미확인건만 조회
	$('#chkNo').bind('click', function() {
  	    click_chkNo();
	});
	
	getAsta();
});

function click_chkNo(){
	if($("#chkNo").is(":checked")){
		astaGrid.setData(astaGridData2);
	} else {
		astaGrid.setData(astaGridData);
	}
}

function btnClose_Click() {
	window.parent.AstaModal.close();
}

function getAsta() {
	data = new Object();
	data = {
		Acptno : pAcptNo,
		userid : pUserId,
		requestType : 'getAsta2'
	}
	ajaxAsync('/webPage//ecmr/Cmr3200Servlet', data, 'json', successGetAsta);
}

function successGetAsta(data) {
	astaGridData = data;
	astaGrid.setData(astaGridData);
	
	// 미처리건 전용 변수
	astaGridData2.filter(function(data) {
		if(astaGridData2.cr_chkflag == '미확인') 
			return true;
		else 
			return false;
	});
	
	var i = 0;
	if(astaGridData.length < 1) {
		dialog.alert('검색결과가 없습니다.');
	}
	
	if(astaGridData.length > 0) {
		$('#txtSys').val(astaGridData[0].cm_sysmsg);
		$('#txtAcptNo').val(astaGridData[0].cr_acptno);
	}
//	astaGridData = [];
}

