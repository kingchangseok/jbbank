var firstGrid = new ax5.ui.grid();
var endSw = null;
var userId = window.top.pUserId;
var acptNo = window.top.pReqNo;
var srId = window.top.reqInfoData[0].cr_srid;

var beforeClick = null;
var nextRow = null;
var inputTxt = "";
var gridChk = null;
var findTr = 0;
var delay = false;

var isIE	=false;
var agent = navigator.userAgent.toLowerCase();
if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
	isIE = true;
}else{
	isIE = false;
}

var enterEvent = $.Event("keypress", {keyCode: 13});

$(document).ready(function() {
	checkStatus();
	
	firstGrid.setConfig({
	     target: $('[data-ax5grid="firstGrid"]'),
	     showLineNumber : false,
	     showRowSelector : false,
	     multipleSelect : false,
	     virtualScrollY : false,
	     paging : false,
	     rowSelectorColumnWidth : 27,
	     id : "chkListGrid",
	     scroller : {
	    	 size: 0
	     },
	     header: {
	         align: "center"
	     },
	     body: {
	         onClick: function () {
	         	this.self.clearSelect();
	            this.self.select(this.dindex);
	            if(this.column.key == "cm_etc") { //사유란을 클릭하면 바로 input으로 넘어가도록
	            	var d = $('[data-ax5grid-tr-data-index="'+ this.dindex + '"]')[0].childNodes[4];
	            	if(isIE) { //IE일 경우 css조정            		
	            		var find = $(d).find("input").css({
	            			"position" : "relative"
	            		});
	            	}
	            	nextRow = this.dindex + 1;
	            	var txtBox = $("#inputText" + this.dindex);
	            	$(txtBox).focus();
	            	$(txtBox).select();
	            }
	         }
	     },
	     columns: [
	    	 {label: "단계",  width: '20%', align: 'left', columns : [	    		 
	    		 {key: "cm_maingbn", label: "대구분",  width: '10%', align: 'left'},
	    		 {key: "cm_subgbn", label: "소구분",  width: '10%', align: 'left'}
	    	 ]},
	        {key: "cm_contents", label: "점검항목",  width: '40%', align: 'left', multiLine: true, enableFilter: true},
	        {label: "결과",  width: '15%', align: 'left', columns : [	        	
	        	{key: "cm_chkrst", label: "　Y　　　　N 　　해당없음",  width: '15%', align: 'left', formatter: function() {
	        		//row별 라디오버튼 생성
	        		var chkrst = this.item.cm_chkrst;
	        		var chkY, chkN, chkP = "";
	        		if(chkrst == "Y") {
	        			chkY = "checked";
	        		} else if(chkrst == "N") {
	        			chkN = "checked";
	        		} else if(chkrst == "P") {
	        			chkP = "checked";
	        		}
	        		var radioBox = 
	        			'<input id="radioY' + this.dindex + '" name="radioGroup' + this.dindex + '"tabindex="8" type="radio" value="optCkOut" ' + chkY + '/>' +
	        			'<input id="radioN' + this.dindex + '" name="radioGroup' + this.dindex + '"tabindex="8" type="radio" value="optCkOut" ' + chkN + '/>' +
	        			'<input id="radioP' + this.dindex + '" name="radioGroup' + this.dindex + '"tabindex="8" type="radio" value="optCkOut" ' + chkP + '/>';
	        		return radioBox;
	        	}}	 
	        ]},
	        {key: "cm_etc", label: "사유",  width: '25%', align: 'left', multiLine: true, formatter: function() {
	        	var etc = this.item.cm_etc;
	        	etc = etc != undefined ? etc : "";
	        	var inputTag = '<input id="inputText' + this.dindex + '" class="reasonTxt"' +
	        					 'tabindex="8" type="text" value="'+ etc +'"/>';
	        	return inputTag;
	        }}
	     ]
	});
	
	$(".close, #close").bind('click', function() {
		window.top.checkListRegModal.close();
	});
	
	$("#btnQry").bind('click', function() {
		cmdReq();
	})
	
	//상단 row들이 화면을 벗어나는 문제 해결 > 그리드 그리기 완료 시 최상단 row에 이탈방지용 요소 추가
	gridChk = setInterval(function() {
		if(findTr > 0) {		
			var topBody = $('[data-ax5grid-panel-scroll="body"]').css("top").replace('-','');
			if(topBody.substring(0, 1) != "0") {				
				var tempDiv = '<div style="height: ' + topBody + ';"><input type="text" placeholder="임시요소"></div>';
				var bd = $('[data-ax5grid-panel-scroll="body"]').find("tbody");
				$(bd).prepend(tempDiv);
			}
			$(".reasonTxt").bind('keyup', function(e) {
				e.preventDefault();
				if(e.keyCode == 13 && delay == false) {
					delay = true;
					$("#inputText" + nextRow).focus();
					$("#inputText" + nextRow).trigger('click');
					setTimeout(function() { //익스에서 한글입력중 엔터 시 두번 동작하는 문제 때문에 딜레이설정
						delay = false;
					}, 100);
				}
			});
			clearInterval(gridChk);
		}
		findTr = $('[data-ax5grid-panel-scroll="body"]').find("tbody").find('[class="tr-0"]').length;
	}, 50);
	
	
//	$('[data-ax5grid-panel-scroll="body"]').bind('keyup', function(e) {
//		 if(e.keyCode == 13) {
//			 $(nextRow).trigger('dblclick');
//			 $(nextRow).css({"background" : "blue"});
//		 }
//		 console.log("wer");
//	});
	
	$("#btnQry").prop("disabled", true);
	getChkList();
	getChkYN()
	getLastInfo();
	
});

function checkStatus() {
	if(window.top.reqInfoData[0].prcdate != null && window.top.reqInfoData[0].prcdate != "") {
		endSw = true
	} else {
		endSw = false;
	}
}

function getChkList() {
	var ajaxData = {
		gbnCd : "A",
		acptNo : acptNo,
		requestType : endSw ? "getChkListEnd" : "getChkList_new"
	}
	ajaxAsync('/webPage/modal/requestDetail/CheckListRegModal', ajaxData, 'json', successGetChkList);
}

function successGetChkList(data) {
	var chkListData = data; 
	firstGrid.setData(chkListData);
}

function getChkYN() {
	var ajaxData = {
		userId : userId,
		srId : srId,
		requestType : "getChkYN"
	}
	ajaxAsync('/webPage/modal/requestDetail/CheckListRegModal', ajaxData, 'json', successGetChkYN);
}

function successGetChkYN(data) {
	console.log(data);
	if(data == "OK") {
		$("#btnQry").prop('disabled', false);
	} else {
		$("#btnQry").prop('disabled', true);		
	}
}

function getLastInfo() {
	var ajaxData = {
		acptNo : acptNo,
		requestType : "getLastInfo"
	}
	ajaxAsync('/webPage/modal/requestDetail/CheckListRegModal', ajaxData, 'json', successGetLastInfo);
}

function successGetLastInfo(data) {
	if(data != "") $("#infoText").text(data);
}

function cmdReq() {
	var gridData = firstGrid.list;
	var tmpCmt;
	var tmpLength;
	var tmpArr = [];
	var tmp_obj;
	var ajaxData;
//	$.each(gridData, function(i, val) {
	for(var i = 0; i < gridData.length; i++) {
		if($("#radioY" + i).is(":checked")) gridData[i].cm_chkrst = "Y";
		else if($("#radioN" + i).is(":checked")) gridData[i].cm_chkrst = "N";
		else if($("#radioP" + i).is(":checked")) gridData[i].cm_chkrst = "P";
		
		var etcData = $("#inputText" + i).val();
		
		console.log(etcData);
		if(etcData != null || etcData != undefined) {
			tmpCmt = etcData.trim();
			tmpLength = fnChkByte(etcData);
		} else {
			tmpCmt = "";
			tmpLength = 0;
		}
		if(gridData[i].cm_chkrst != "P" && (tmpCmt == null || tmpCmt == "")) {
			dialog.alert("[" + gridData[i].cm_contents + "]\n항목에 대한 사유를 입력하여 주십시오.");
			tmp_obj = null;
			return;
		}
		
		if(tmpLength >= 300) {
			dialog.alert("사유는 300byte 이하로 입력하여 주십시오")
		}
		
		tmp_obj = new Object();
		tmp_obj.cm_editor = userId;
		tmp_obj.cm_acptno = acptNo;
		tmp_obj.cm_gbncd = gridData[i].cm_gbncd;
		tmp_obj.cm_chkrst = gridData[i].cm_chkrst;
		tmp_obj.cm_etc = tmpCmt;
		tmp_obj.cc_srid = srId;
		tmpArr.push(tmp_obj);
		tmp_obj = null;
	};
	
	if(tmpArr.length > 0) {
		ajaxData = {
			tmpArr : tmpArr,
			requestType : "setChkResult"
		}
		ajaxAsync('/webPage/modal/requestDetail/CheckListRegModal', ajaxData, 'json', successSetChkResult);
	}
}

function fnChkByte(str){
//	var str = $('#txtInfonotiMsg').val().trim();
	var str_len = str.length;//현재 입력된 byte값
	
	var rbyte = 0;
	var rlen = 0;
	var one_char = "";
	var str2 = "";
	var i = 0;
	
	for ( i=0 ; i<str_len ; i++ ) {
		one_char = str.charAt(i);
		if ( escape(one_char).length > 4 ) rbyte += 2 //한글입력 2byte
		else rbyte++; //나머지는 1byte		
		rlen = i+1; //return 할 문자열 개수
	}
//	console.log('rbyte:',rbyte);
	return rlen;
}

function successSetChkResult(data) {
	if(data == "OK") {
		var fileList = window.parent.grid_fileList.getList();
		$.each(fileList, function(i, val) {
			if(val.cc_editor == userId) {
				fileList[i].chkList = "O";
			}
		})
		window.parent.grid_fileList.setData(fileList);
		dialog.alert("등록이 완료되었습니다.", function() {window.parent.checkListRegModal.close();});
	} else {
		dialog.alert("등록에 실패하였습니다.");
	}
}