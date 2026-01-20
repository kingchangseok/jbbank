var userId 		= window.parent.userId;
var userName 	= window.top.userName;

var firstGrid  		= new ax5.ui.grid();
var firstGridData 	= [];
var lstStaCdData 	= [];

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    page : false,
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        	
	       	var selIn = firstGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	       	
	       	window.parent.PMSListModalFlag = true;
	       	window.parent.gwDocId = this.item.DocNo;
	       	window.parent.gwTitle = this.item.Title;
	       	popClose();
        },
        onClick: function () {
        }
    },
    columns: [
        {key: "rnum", 		label: "NO",	width: '40px', align: 'left'},
        {key: "DevUsr", 	label: "개발자",	width: '15%', align: 'left'},
        {key: "DocNo", 		label: "문서번호",	width: '15%', align: 'left'},
        {key: "ReqUsr", 	label: "의뢰자",	width: '15%', align: 'left'},
        {key: "ReqNo", 		label: "의뢰번호",	width: '15%', align: 'left'},
        {key: "Title", 		label: "제목",  	width: '20%'},
        {key: "PmsStaNm", 	label: "상태",	width: '10%'}
    ]
});


$(document).ready(function() {
	$('#txtDevUsr').val(userName);
	
	// 전체선택
	$('#chkAll').bind('click',function() {
		var checkSw = $('#chkAll').is(':checked');
		var addId = null;
		
		lstStaCdData.forEach(function(item,index) {
			 addId = item.cm_micode;
			 if(checkSw) $('#chkSta'+addId).wCheck('check',checkSw);
			 else $('#chkSta'+addId).wCheck('check',checkSw);
		});
	});
	
	// 검색
	$('#cmdFind').bind('click', function() {
		cmdFind_Click('maxpage');
	});
	
	getCodeInfo();
});

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ new CodeInfoOrdercd('PMSSTA','','N','3','') ]);
	lstStaCdData = codeInfos.PMSSTA;
	
	$('#lstStaCd').empty();
	var liStr = '';
	var addId = null;
	lstStaCdData.forEach(function(item) {
		addId = item.cm_micode;
		liStr += '<li class="list-group-item dib width-20">';
		liStr += '	<input type="checkbox" class="checkbox-sta" id="chkSta'+addId+'" data-label="'+item.cm_codename+'"  value="'+addId+'" />';
		liStr += '</li>';
	});
	$('#lstStaCd').html(liStr);
	
	$('input.checkbox-sta').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	
	//PMS 상태가 접수반려, 강제종료가 아닌 것들만 checkbox=true
	//20180705 33,34 종료, 종료중 인것도 체크 선택 안되도록
	var tmpSta = '';
	for(var i=0; i<lstStaCdData.length; i++) {
		tmpSta = lstStaCdData[i].cm_micode;
		if(tmpSta=='26' || tmpSta=='33' || tmpSta=='34' || tmpSta=='35') {
			$('#chkSta'+ (tmpSta) ).wCheck('check',false);
		}else {
			$('#chkSta'+ (tmpSta) ).wCheck('check',true);
		}
	}
	
	cmdFind_Click('maxpage');
}

function cmdFind_Click(flag) {
	var selSw = false;
	var addId = '';	
	for (var i=0; i<lstStaCdData.length; i++) {
		 addId = lstStaCdData[i].cm_micode;
		 if($('#chkSta'+addId).is(':checked')) {
			 selSw = true;
			 break;
		 }
	}
	
	if(!selSw) {
		dialog.alert('진행상태를 선택해주시기 바랍니다.', function(){});
		return;
	}
	
	var tmpSta = ''
	var tmpObj = new Object();
	tmpObj.DocNo = $('#txtDocNo').val();
	tmpObj.DevUsr = $('#txtDevUsr').val();
	tmpObj.ReqNo = $('#txtReqNo').val();
	tmpObj.ReqUsr = $('#txtReqUsr').val();
	tmpObj.Title = $('#txtTitle').val();
	
	addId = '';	
	lstStaCdData.forEach(function(item,index) {
		 addId = item.cm_micode;
		 if($('#chkSta'+addId).is(':checked')) {
			 if(tmpSta.length > 0 ) tmpSta += ',';
			 tmpSta += item.cm_micode;
		 }
	});
	tmpObj.PmsSta = tmpSta;
	
	var data = new Object();
	if(flag == 'maxpage') {
		data = {
			etcObj 	: tmpObj, 
			value 	: '0',
			requestType	: 'getPmsMaxPage'
		}
		$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/common/PrjInfoServlet', data, 'json',successGetPmsMaxPage);
	}else {
		data = {
			etcObj 	: tmpObj, 
			value 	: '0',
			limit 	: '20',
			requestType	: 'getPmsInfo'
		}
		$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
		$(".loding-div").show();
		ajaxAsync('/webPage/common/PrjInfoServlet', data, 'json',successGetPmsInfo);
	}
	
	tmpObj = null;
}

function successGetPmsMaxPage(data) {
	$(".loding-div").remove();
	
	if (typeof data == 'string' && data.indexOf('ERROR')>=0) {
		dialog.alert(data);
		return;
	}
}

function successGetPmsInfo(data) {
	$(".loding-div").remove();
	
	if (typeof data == 'string' && data.indexOf('ERROR')>=0) {
		dialog.alert(data);
		return;
	}
	
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function popClose() {
	window.parent.PMSListModal.close();
}