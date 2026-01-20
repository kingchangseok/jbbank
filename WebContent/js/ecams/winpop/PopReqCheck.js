var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];
var tmpProgData		= [];
var exportData		= [];

var confirmChkCnt	= 0;
var srcCnt 			= 8; //cc_confsrc 자릿수

var f = document.getReqData;
pUserId = f.userId.value;
pAcptNo = f.acptno.value;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: false, 		// 그리드 sort 가능 여부(true/false)
    multiSort: false,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    page: false,
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
    },
    columns: [
    	{key: "cm_codename",   	label: "구분",			width: '15%',  	align: 'left'},
    	{key: "cc_subject",   	label: "검증대상항목",		width: '30%',  	align: 'left',
    		formatter: function() {
    			if(this.item.cc_subcd == '1') {			//체크박스
    				if(this.item.cc_subject == null || this.item.cc_subject == undefined || this.item.cc_subject == '') return '';
    				if(this.item.cc_subsrc == null || this.item.cc_subsrc == undefined) this.item.cc_subsrc = '00000000';
    				
    				var chkBoxData = this.item.cc_subject.split('.');
    				var chkBoxCnt = chkBoxData.length;
    				
    				var liStr = '';
    				var chkBoxKey = '';
    				for(var i=0; i<chkBoxData.length; i++) {
    					chkBoxKey = chkBoxData[i];
    					liStr +='<input id="firstSubChk'+this.item.cc_seq+i+'" name="'+this.item.cc_seq+'" class="firstSubChk1" type="checkbox" ' + ((this.item.cc_subsrc.substr(i,1)=='1')?"checked":"") + '/> <label for="firstSubChk"' + i + '>' + chkBoxKey + '</label>';
    				}
    				
    				return liStr;
    			}else if(this.item.cc_subcd == '3') {	//text
    				return '<label>' + this.item.cc_subject + '</label>';
    			}else {
    				dialog.alert('잘못된 항목 코드입니다. (cc_subcd : ' + this.item.cc_subcd + ')');
    			}
    		}
    	},
    	{key: "checked", 		label: "여부확인",  		width: '45%', 	align: 'center',
   		 formatter: function() {
  			 if(this.item.cc_confsrc == null || this.item.cc_confsrc == undefined) this.item.cc_confsrc = '00000000';
   			 return '<input id="firstChk1'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="firstChk1" type="checkbox" ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"checked":"") + '/> <label for="firstChk1">대상여부</label>'
   			 +'<input id="firstChk2'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="firstChk2" type="checkbox" ' + ((this.item.cc_confsrc.substr(1,1)=='1')?"checked":"") + ' ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"":"disabled") + '/> <label for="firstChk2">테스트여부</label>'
   			 +'<input id="firstChk3'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="firstChk3" type="checkbox" ' + ((this.item.cc_confsrc.substr(2,1)=='1')?"checked":"") + ' ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"":"disabled") + '/> <label for="firstChk3">적정여부</label>';
   		 }
    	},
    	{key: "cc_bigo",    	label: "비고",			width: '10%',  	align: 'left'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: false, 		// 그리드 sort 가능 여부(true/false)
    multiSort: false,		// 그리드 모든 컬럼 sort 선언(true/false)
    multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
    showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
    page: false,
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
        	secondGridDblClick(this.item);
        },
    	onDataChanged: function(){
    		//그리드 새로고침 (스타일 유지)
    	    this.self.repaint();
    	},
    },
    columns: [
    	{key: "cc_subject",    	label: "검증대상항목",		width: '20%',  	align: 'left'},
		{key: "checked", 		label: "여부확인",  		width: '50%', 	align: 'center',
	   		 formatter: function() {
	   			if(this.item.cc_confsrc == null || this.item.cc_confsrc == undefined) this.item.cc_scc_confsrcel = '00000000';
	   			 var disabledSw = 'disabled';
	   			 if(this.item.cc_confsrc.substr(0,1)=='1') disabledSw = '';
	   			 return '<input id="secondChk1'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="secondChk1" type="checkbox" ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"checked":"") + '/> <label for="firstChk1">대상여부</label>'
	   			 +'<input id="secondChk2'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="secondChk2" type="checkbox" ' + ((this.item.cc_confsrc.substr(1,1)=='1')?"checked":"") + ' ' + disabledSw + '/> <label for="firstChk2">테스트여부</label>'
	   			 +'<input id="secondChk3'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="secondChk3" type="checkbox" ' + ((this.item.cc_confsrc.substr(2,1)=='1')?"checked":"") + ' ' + disabledSw + '/> <label for="firstChk3">적정여부</label>';
	   		 }
	    },
   		{key: "cc_bigo",    	label: "비고",			width: '10%',  	align: 'left'}
    ]
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});


//그리드 체크박스 클릭 할때마다 유지되도록
$(document).on('click','.firstChk1, .firstChk2, .firstChk3', function(){
	var name = this.name;
	
	$(firstGridData).each(function(){
		if(this.cc_seq == name){
			//체크
			if($("#firstChk1"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(0,'1');
				
				$("#firstChk2"+this.cc_seq).prop('disabled',false);
				$("#firstChk3"+this.cc_seq).prop('disabled',false);
			}
			
			if($("#firstChk2"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(1,'1');
			}
			
			if($("#firstChk3"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(2,'1');
			}
			
			//체크해제
			if(!$("#firstChk1"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(0,'0');
				$("#firstChk2"+this.cc_seq).prop('checked',false);
				$("#firstChk3"+this.cc_seq).prop('checked',false);
				$("#firstChk2"+this.cc_seq).prop('disabled',true);
				$("#firstChk3"+this.cc_seq).prop('disabled',true);
			}
			
			if(!$("#firstChk2"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(1,'0');
			}
			
			if(!$("#firstChk3"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(2,'0');
			}
		}
	});
});

$(document).on('click','.firstSubChk1', function(){
	var name = this.name;
	
	$(firstGridData).each(function(){
		if(this.cc_seq == name){
			var chkBoxData = this.cc_subject.split('.');
			var chkBoxCnt = chkBoxData.length;
			
			for(var i=0; i<chkBoxData.length; i++) {
				//체크
				if($("#firstSubChk"+name+i).is(":checked")){
					this.cc_subsrc = this.cc_subsrc.replaceAt(i,'1');
				}
				
				//체크해제
				if(!$("#firstSubChk"+name+i).is(":checked")){
					this.cc_subsrc = this.cc_subsrc.replaceAt(i,'0');
				}
			}
		}
	});
});

$(document).on('click','.secondChk1, .secondChk2, .secondChk3', function(){
	var name = this.name;
	
	$(secondGridData).each(function(){
		if(this.cc_seq == name){
			//체크
			if($("#secondChk1"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(0,'1');
				$("#secondChk2"+this.cc_seq).prop('disabled',false);
				$("#secondChk3"+this.cc_seq).prop('disabled',false);
			}
			
			if($("#secondChk2"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(1,'1');
			}
			
			if($("#secondChk3"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(2,'1');
			}
			
			//체크해제
			if(!$("#secondChk1"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(0,'0');
				$("#secondChk2"+this.cc_seq).prop('checked',false);
				$("#secondChk3"+this.cc_seq).prop('checked',false);
				$("#secondChk2"+this.cc_seq).prop('disabled',true);
				$("#secondChk3"+this.cc_seq).prop('disabled',true);
			}
			
			if(!$("#secondChk2"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(1,'0');
			}
			
			if(!$("#secondChk3"+this.cc_seq).is(":checked")){
				this.cc_confsrc = this.cc_confsrc.replaceAt(2,'0');
			}
		}
	});
});

$(document).ready(function(){
	getPrivProgLst();
	
	//대상여부
	$('#chk1').bind('click',function(){
		chk1Click();
	});
	
	$('#chk1').bind('change',function(){
		if($("#chk1").is(":checked")) {
			$("#chk2").wCheck('disabled',false);
			$("#chk3").wCheck('disabled',false);
		}else {
			$("#chk2").wCheck('disabled',true);
			$("#chk3").wCheck('disabled',true);
		}
	});
	
	//추가
	$('#btnAdd').bind('click',function(){
		btnAddClick();
	});
	
	//삭제
	$('#btnDel').bind('click',function(){
		btnDelClick();
	});
	
	//취소
	$('#btnExit').bind('click',function(){
		close();
	});
	
	//완료
	$('#btnOk').bind('click',function(){
		btnOkClick();
	});
});

function getPrivProgLst() {
	var data = {
		acptNO : pAcptNo,
		requestType	: 'getprivProgLst'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successGetPrivProgLst);
}

function successGetPrivProgLst(data) {
	$(".loding-div").remove();

	tmpProgData = data;
	
	if(tmpProgData.length > 0) {
		for(var i = 0; i < tmpProgData.length; i++) {
			if(tmpProgData[i].cc_gbn == "1")
				firstGridData.push(tmpProgData[i]);
			if(tmpProgData[i].cc_gbn == "2")
				secondGridData.push(tmpProgData[i]);
		}
	}
	
	firstGrid.setData(firstGridData);
	secondGrid.setData(secondGridData);
}

function btnAddClick() {
	var tmpSrc = '000';
	var tmpSel = '';
	
	if($("#chk1").is(":checked")) tmpSrc = tmpSrc.replaceAt(0,'1');
	else tmpSrc = tmpSrc.replaceAt(0,'0');
	
	if($("#chk2").is(":checked")) tmpSrc = tmpSrc.replaceAt(1,'1');
	else tmpSrc = tmpSrc.replaceAt(1,'0');
	
	if($("#chk3").is(":checked")) tmpSrc = tmpSrc.replaceAt(2,'1');
	else tmpSrc = tmpSrc.replaceAt(2,'0');
	
	for(var i=0; i<srcCnt-3; i++) tmpSrc += '0';
	
	var tmpObj = new Object();
	tmpObj.cc_gbn = '2';
	tmpObj.cc_proggbn = '0';
	tmpObj.cc_subject = $('#txtSubject').val();
	tmpObj.cc_subsrc = '00000000';
	tmpObj.cc_confsrc = tmpSrc;
	tmpObj.cc_subcd = '3';
	tmpObj.cc_bigo = $('#txtBigo').val();
	tmpObj.cc_seq = secondGridData.length+1;
	
	secondGridData.push(tmpObj);
	secondGrid.setData(secondGridData);
	
	clearText();
}

function btnDelClick() {
	if(secondGrid.getList('selected').length < 1) return;
	secondGrid.removeRow("selected");
	secondGridData = secondGrid.getList();
	
	clearText();
}

function btnOkClick() {
	var tmpData = firstGridData.concat(secondGridData);
	window.parent.popReqCheckInfo = tmpData;

	var data = {
		AcptNo 	 : pAcptNo,
		ConfList : tmpData,
		requestType : 'setConfList'
	}
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successSetConfList);
}

function successSetConfList(data) {
	try {
		if(window.opener.getPrivProgLst != null && window.opener.getPrivProgLst != undefined) {
			window.opener.getPrivProgLst();
		}
	}catch(e) {
	}
	
	if(!data) {
		dialog.alert("검증 항목 수정 실패");
	}else {
		dialog.alert("검증 항목 수정 완료");
	}
	
	close();
}

function clearText() {
	$("#chk1").wCheck('check',false);
	$("#chk2").wCheck('check',false);
	$("#chk3").wCheck('check',false);
	$('#txtSubject').val('');
	$('#txtBigo').val('');
	chk1Click();
}

function chk1Click() {
	if($("#chk1").is(":checked")) {
		$("#chk2").wCheck('disabled',false);
		$("#chk3").wCheck('disabled',false);
	}else {
		$("#chk2").wCheck('check',false);
		$("#chk3").wCheck('check',false);
		$("#chk2").wCheck('disabled',true);
		$("#chk3").wCheck('disabled',true);
	}
}

function secondGridDblClick(selItem) {
	$('#txtSubject').val(selItem.cc_subject);
	$('#txtBigo').val(selItem.cc_bigo);
	
	if(selItem.cc_confsrc.substr(0,1) == '1') $("#chk1").wCheck('check',true);
	else $("#chk1").wCheck('check',false);
	
	if(selItem.cc_confsrc.substr(1,1) == '1') $("#chk2").wCheck('check',true);
	else $("#chk2").wCheck('check',false);
	
	if(selItem.cc_confsrc.substr(2,1) == '1') $("#chk3").wCheck('check',true);
	else $("#chk3").wCheck('check',false);
}

function addToExportData() {
	addToArray(firstGridData, exportData);
	addToArray(secondGridData, exportData);
}

function addToArray(data1, data2) {
	for(var i = 0; i < data1.length; i++) {
		data2.push(data1[i]);
	}
}
