var firstGrid		= new ax5.ui.grid();
var secondGrid		= new ax5.ui.grid();
var fileGrid		= new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];
var fileGridData 	= [];

var srcCnt 			= 8; //cc_confsrc 자릿수

/* 파일재첨부 관련 변수 */
var reqCheckFileModal 		= new ax5.ui.modal();
var reqCheckFileModalFlag 	= false;	//from modal
var reqCheckFileModalFiles	= [];		//from modal

/* 파일첨부 변수 */
var fileUploadModal 	= new ax5.ui.modal();
var fileIndex			= 0;
var TotalFileSize 		= 0;

/* 신청상세>재요청 관련 변수 */
var reChkInAcptNo 		= window.parent.reChkInAcptNo;
var userId				= window.parent.userId;

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
	   			if(this.item.cc_confsrc == null || this.item.cc_confsrc == undefined) this.item.cc_confsrc = '00000000';
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

fileGrid.setConfig({
    target: $('[data-ax5grid="fileGrid"]'),
    sortable: true, 		// 그리드 sort 가능 여부(true/false)
    multiSort: true,		// 그리드 모든 컬럼 sort 선언(true/false)
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
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "파일삭제"}
        ],
        popupFilter: function (item, param) {
        	fileGrid.clearSelect();
        	fileGrid.select(Number(param.dindex));

	       	var selIn = fileGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;  	
				 
	        return true;
        },
        onClick: function (item, param) {
        	/*confirmDialog.confirm({
    			title: '삭제확인',
    			msg: '첨부파일을 삭제하시겠습니까?',
    		}, function(){
    			if(this.key === 'ok') {
    				fileDelete();
    			}
    		});*/
        	fileDelete();
        	fileGrid.contextMenu.close();
		}
	},
    columns: [
    	{key: "cc_attfile",   	label: "파일명",		width: '50%',  	align: 'left'},
    	{key: "cm_username",    label: "첨부인",		width: '50%',  	align: 'left'}
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
	getComProgLst();
	chkRechkIn();
	
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
	
	//파일첨부
	$('#btnFile').bind('click',function(){
		btnFileAddClick();
	});
	
	//파일재첨부
	$('#btnReFile').bind('click',function(){
		btnReFileClick();
	});
	
	//취소
	$('#btnExit').bind('click',function(){
		popClose('2');
	});
	
	//완료
	$('#btnOk').bind('click',function(){
		btnOkClick();
	});
});

function chkRechkIn() {
	var data = {
			AcptNo	: reChkInAcptNo,
			UserId	: userId,
		requestType	: 'chkRechkIn'
	}
	
	console.log('[chkRechkIn] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successChkRechkIn);
}

function successChkRechkIn(data) {
	if(Boolean(data)) {
		getProgListInfo();
		getFiles();
	}
}

function getProgListInfo() {
	var data = {
			AcptNo	: reChkInAcptNo,
		requestType	: 'getProgListInfo'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successGetProgListInfo);
}

function successGetProgListInfo(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	console.log('successGetProgListInfo ==>', data);
	
	firstGridData = data.filter(function(item) {
		if(item.cc_gbn == '1') return true;
		else return false;
	});
	firstGrid.setData(firstGridData);
	
	secondGridData = data.filter(function(item) {
		if(item.cc_gbn == '2') return true;
		else return false;
	});
	secondGrid.setData(secondGridData);
}

function getFiles() {
	var data = {
				Id	: reChkInAcptNo,
		requestType	: 'getFiles'
	}
	
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json',successGetFiles);
}

function successGetFiles(data) {
	fileGridData = data;
	for(var i=0; i<fileGridData.length; i++) {
		fileGridData[i].isCopy = 'true';
	}
	fileGrid.setData(fileGridData);
}

function getComProgLst() {
	var data = {
		requestType	: 'getComProgLst'
	}
	
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successGetComProgLst);
}

function successGetComProgLst(data) {
	$(".loding-div").remove();
	
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
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
	if(fileGridData.length < 1) {
		dialog.alert('테스트결과서를 첨부하여 주십시오.');
		return;
	}
	
	window.parent.reqCheckModalFiles = clone(fileGridData);
	window.parent.reqCheckModalTestInfo = firstGridData.concat(secondGridData);
	
	popClose('1');
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

function btnFileAddClick() {
	if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
		fileIndex++;
		$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
	}
	$("#file"+fileIndex).click();
}

function fileChange(file) {
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	var spcChar = "{}<>?|~`!@#$%^&*+\"'\\/";
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 
		var fileCk = true;
		
		for(var i=0; i<jqueryFiles.files.length; i++){
			var sizeCount = 0;
			var size = jqueryFiles.files[i].size/1024; // Byte, KB, MB, GB
			while(size > 1024 && sizeCount < 2){
				size = size/1024;
				sizeCount ++;
			}
			size = Math.round(size*10)/10.0 + fileSizeArr[sizeCount];
			var sizeReal = jqueryFiles.files[i].size;
			var name = jqueryFiles.files[i].name
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				fileCk = false;
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				fileCk = false;
				break;
			}
			
			for(var j=0; j<fileGridData.length; j++){

				for (k=0;spcChar.length>k;k++) {
					if (name.indexOf(spcChar.substr(k,1))>=0) {
						dialog.alert("첨부파일명에 특수문자를 입력하실 수 없습니다.\n특수문자를 제외하고 첨부하여 주시기 바랍니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
					
					if (name.substring(name.length-3, name.length).toUpperCase() == 'HWX') {
						dialog.alert("HWX(hwx) 파일은 첨부 할 수 없습니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
				}
				if(!fileCk){
					break;
				}
				
				if(fileGridData[j].name == name){
					dialog.alert("파일명 : " + name +"\n 은 이미 추가 되어 있는 파일명 입니다.\n확인 후 다시 등록해 주세요");
					$("#"+file).remove();
					fileCk = false;
					break;
				}
				fileCk = true;
			}

			if(fileCk){
				var tmpObj = new Object(); // 그리드에 추가할 파일 속성
				tmpObj.name = name;
				tmpObj.size = size;
				tmpObj.sizeReal = sizeReal;
				tmpObj.newFile = true;
				tmpObj.realName = name;
				tmpObj.cc_attfile = name;
				tmpObj.file = jqueryFiles.files[i];
				
				fileGridData.push(tmpObj);
			}
			else{
				break;
			}
		}
		fileGrid.setData(fileGridData);
		console.log('fileGridData==>',fileGridData);
	}
}

function btnReFileClick() {
	setTimeout(function() {
		reqCheckFileModal.open({
	        width: 800,
	        height: 600,
	        iframe: {
	            method: "get",
	            url: "../request/ReqCheckFileModal.jsp"
		    },
	        onStateChanged: function () {
	            if (this.state === "open") {
	                mask.open();
	            }
	            else if (this.state === "close") {
	            	mask.close();
	            	console.log('reqCheckFileModalFlag==>',reqCheckFileModalFiles);
	            	if(reqCheckFileModalFlag){
	            		console.log('reqCheckFileModalFiles==>',reqCheckFileModalFiles);
	            		for(var i=0; i<reqCheckFileModalFiles.length; i++) {
	            			fileGridData.push(reqCheckFileModalFiles[i]);
	            			fileGrid.setData(fileGridData);
	            		}
	            		window.parent.isReChkIn = true;
	            	}
	            }
	        }
		});
	}, 200);	
}

function fileDelete() {
	var selItem = fileGrid.list[fileGrid.selectedDataIndexs[0]];
	if(selItem.newFile) {
		fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
		fileGridData = fileGrid.getList();
	}else {
		fileGrid.removeRow(fileGrid.selectedDataIndexs[0]);
		fileGridData = fileGrid.getList();
		
		var data = new Object();
		data = {
			Id : selItem.cc_id,
			ReqCd : '04',
			seqNo : selItem.cc_seqno,
			requestType	: 'delFile'
		}
		var ajaxReturnData = ajaxCallWithJson('/webPage/ecmc/Cmc0010Servlet', data, 'json');
		if(ajaxReturnData != null && ajaxReturnData != undefined) {
			if(typeof ajaxReturnData == 'string' && ajaxReturnData.indexOf('ERROR') > -1) {
				dialog.alert(ajaxReturnData.substr(5));
				return;
			}
		}
		
		dialog.alert('삭제완료');
	}
}

function popClose(gbn) {
	window.parent.reqCheckModalVal = gbn;
	if(gbn == 1) {
		window.parent.showAndHideUploadModal('hide');
	}else {
		window.parent.reqCheckModal.close();
	}
}