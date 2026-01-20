var progGrid	= new ax5.ui.grid();   //프로그램그리드
var progGrid2	= new ax5.ui.grid();   //프로그램그리드

var tmpProgData 	= [];
var progGridData 	= [];
var progGrid2Data 	= [];
var popReqCheckInfo	= [];

var completeReadyFunc = false;

createViewGrid();
createViewGrid2();

$(document).ready(function(){
	
	$('#btnSubPop').bind('click',function() {
		openWindow('1');
	});
	
	completeReadyFunc = true;
});

function upFocus() {
	progGrid.focus('HOME');
}

function setRenderSw(value) {
	if(value) $('#btnSubPop').show();
	getPrivProgLst();
}

function createViewGrid() {
	progGrid	= new ax5.ui.grid();
	progGrid.setConfig({
	    target: $('[data-ax5grid="progGrid"]'),
	    sortable: false, 		// 그리드 sort 가능 여부(true/false)
	    multiSort: false,		// 그리드 모든 컬럼 sort 선언(true/false)
	    multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
	    showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
	    showLineNumber: true,
	    page: false,
	    header: {
	        align: 'center'
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
	    	{key: 'cm_codename',	label: '구분',  		width: '25%',	align: 'center'},
	        {key: 'cc_subject',		label: '검증대상항목', 	width: '25%',	align: 'left',
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
	    					liStr +='<input id="firstSubChk'+this.item.cc_seq+i+'" name="'+this.item.cc_seq+'" class="firstSubChk1" type="checkbox" ' + ((this.item.cc_subsrc.substr(i,1)=='1')?"checked":"") + ' disabled/> <label for="firstSubChk"' + i + '>' + chkBoxKey + '</label>';
	    				}
	    				
	    				return liStr;
	    			}else if(this.item.cc_subcd == '3') {	//text
	    				return '<label>' + this.item.cc_subject + '</label>';
	    			}else {
	    				dialog.alert('잘못된 항목 코드입니다. (cc_subcd : ' + this.item.cc_subcd + ')');
	    			}
	    		},
	        },
	        {key: 'checked',		label: '여부확인',    	width: '25%',	align: 'left',
	        	formatter: function() {
	     			 if(this.item.cc_confsrc == null || this.item.cc_confsrc == undefined) this.item.cc_confsrc = '00000000';
	      			 return '<input id="firstChk1'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="firstChk1" type="checkbox" ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"checked":"") + ' disabled/> <label for="firstChk1">대상여부</label>'
	      			 +'<input id="firstChk2'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="firstChk2" type="checkbox" ' + ((this.item.cc_confsrc.substr(1,1)=='1')?"checked":"") + ' ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"":"disabled") + ' disabled/> <label for="firstChk2">테스트여부</label>'
	      			 +'<input id="firstChk3'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="firstChk3" type="checkbox" ' + ((this.item.cc_confsrc.substr(2,1)=='1')?"checked":"") + ' ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"":"disabled") + ' disabled/> <label for="firstChk3">적정여부</label>';
	      		 }
	        },
	        {key: 'cc_bigo', 	    label: '비고',    	width: '25%',	align: 'left'},
	    ]
	});
	
	if (progGridData != null && progGridData.length > 0) {
		progGrid.setData(progGridData);
	}
}

function createViewGrid2() {
	progGrid2	= new ax5.ui.grid();
	progGrid2.setConfig({
		target: $('[data-ax5grid="progGrid2"]'),
		 sortable: false, 		// 그리드 sort 가능 여부(true/false)
		   multiSort: false,		// 그리드 모든 컬럼 sort 선언(true/false)
		   multipleSelect: false,	// 그리드 ROW 선택 시 다중 선택 가능하게 할지 여부(true/false)
		   showRowSelector: false,	// 그리드에 체크박스 보이게 할지 여부(true/false)
		   showLineNumber: true,
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
		   			 return '<input id="secondChk1'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="secondChk1" type="checkbox" ' + ((this.item.cc_confsrc.substr(0,1)=='1')?"checked":"") + ' disabled/> <label for="firstChk1">대상여부</label>'
		   			 +'<input id="secondChk2'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="secondChk2" type="checkbox" ' + ((this.item.cc_confsrc.substr(1,1)=='1')?"checked":"") + ' ' + disabledSw + ' disabled/> <label for="firstChk2">테스트여부</label>'
		   			 +'<input id="secondChk3'+this.item.cc_seq+'" name="'+this.item.cc_seq+'" class="secondChk3" type="checkbox" ' + ((this.item.cc_confsrc.substr(2,1)=='1')?"checked":"") + ' ' + disabledSw + ' disabled/> <label for="firstChk3">적정여부</label>';
		   		 }
			   },
			   {key: "cc_bigo",    	label: "비고",			width: '10%',  	align: 'left'}
		  ]
	});
	
	if (progGrid2Data != null && progGrid2Data.length > 0) {
		progGrid2.setData(progGrid2Data);
	}
}

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

function getPrivProgLst() {
	var data = new Object();
	data = {
		acptNO : window.parent.pReqNo,
		requestType : 'getprivProgLst'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr0200Servlet', data, 'json',successGetPrivProgLst);
}

function successGetPrivProgLst(data) {
	$(".loding-div").remove();
	tmpProgData = data;
	progGridData = [];
	progGrid2Data = [];
	
	if(tmpProgData.length > 0) {
		for(var i = 0; i < tmpProgData.length; i++) {
			if(tmpProgData[i].cc_gbn == "1")
				progGridData.push(tmpProgData[i]);
			if(tmpProgData[i].cc_gbn == "2")
				progGrid2Data.push(tmpProgData[i]);
		}
	}
	
	progGrid.setData(progGridData);
	progGrid2.setData(progGrid2Data);
}

function openWindow(type) {
	var nHeight, nWidth, cURL, winName;

	if ( ('pop_'+type) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}
	
	winName = 'pop_'+type;

    nWidth  = 1046;
	nHeight = 700;
	
	if (type === '1') {//검증사항(eCmr0230.mxml)
		cURL = "/webPage/winpop/PopReqCheck.jsp";
	} else {
		confirmDialog2.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
	
	var f = document.setReqData;
    f.user.value = window.parent.pUserId;
    f.acptno.value = window.parent.pReqNo;
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}