/** 거래제어 화면 기능
 * [인스턴스등록] 에서 등록한 노드를 [거래제어] 화면에서 제어 적용
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-08-00
 */
var userName 	    = window.top.userName;
var userId 			= window.top.userId;
var adminYN		    = window.top.adminYN;
var userDeptName    = window.top.userDeptName;
var userDeptCd 	    = window.top.userDeptCd;
var reqCd 			= window.top.reqCd;
var rgtList         = window.top.rgtList;

//grid 생성
var firstGrid 	 	= new ax5.ui.grid();
var secondGrid 		= new ax5.ui.grid();

var firstGridData 	= [];
var secondGridData 	= [];
var cboInstanceData = [];
var cboDateData		= [];

var chk = 0;
var chkacpt = 0;
var acptDate = '';

firstGrid.setConfig({
    target: $('[data-ax5grid="first-grid"]'),
    sortable: false, 
    multiSort: false,
    multipleSelect: false,
    showRowSelector: false, 
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect(); //기존선택된 row deselect 처리 (multipleSelect 할땐 제외해야함)
        	this.self.select(this.dindex);
        	
        	//리프레스 컬럼 클릭 이벤트 : 리프레시 체크박스 클릭시 인스턴스 기동/종료 체크박스 비활성화
        	//작업구분에 리프레시, 인스턴스가 같이 들어갈 수 없으므로 로직 불필요
        },
        onDBLClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{label: "채널거래제어",  width: '60%', align: 'left',
    	 columns: [
    		 		{key: "instancecd", 	label: "인스턴스구분",	width: '10%', align: 'left'},
    		 		{key: "INSTANCE_YN2", 	label: "인스턴스",  	width: '10%', align: 'center'},
    		 		{key: "INSTANCE_YN1", 	label: "제어/해제",  	width: '10%', align: 'center',
    		 			 formatter: function() {
    		 	  			 if(this.item.visible1 == '1') {
    		 	  				return '<input id="chkChOn'+this.item.CD_INSTANCECD+'" name="cha'+this.item.CD_INSTANCECD+'" class="chkChOn" type="radio" ' + ((this.item.checkbox1>0)?"checked":"") + '/> <label for="chkChOn">제어</label>'
       		 	   			 			+'<input id="chkChOff'+this.item.CD_INSTANCECD+'" name="cha'+this.item.CD_INSTANCECD+'" class="chkChOff" type="radio" ' + ((this.item.checkbox2>0)?"checked":"") + '/> <label for="chkChOff">해제</label>';
    		 	  			 }else return '';
    		 	   			 
    		 	   		 }
    		 		},
    		 		{key: "MCI", label: "MCI",  width: '10%', align: 'left', 
    		 			styleClass: function () {
    		 				if(this.item.colormci == '3') return 'fontStyle-ing';
    		 				else if(this.item.colormci == '5') return 'fontStyle-cncl';
    		 	        }
    		 		},
    		 		{key: "FEP", label: "FEP",  width: '10%', align: 'left', 
    		 			styleClass: function () {
    		 				if(this.item.colormci == '3') return 'fontStyle-ing';
    		 				else if(this.item.colormci == '5') return 'fontStyle-cncl';
    		 	        }
    		 		},
    		 		{key: "EAI", label: "EAI",  width: '10%', align: 'left', 
    		 			styleClass: function () {
    		 				if(this.item.colormci == '3') return 'fontStyle-ing';
    		 				else if(this.item.colormci == '5') return 'fontStyle-cncl';
    		 	        }
    		 		},
			   	  ]
    	},
    	{label: "변경/리프레시",  width: '20%', align: 'left',
       	 columns: [{key: "instancecd", label: "리프레시",  width: '10%', align: 'center',
       			 formatter: function() {
	 	  			 if(Number(this.item.visible4) > 0) {
	 	  				return '<input id="chkRefOn'+this.item.CD_INSTANCECD+'" name="'+this.item.CD_INSTANCECD+'" class="chkRefOn" type="checkbox" ' + ((this.item.selected1>0)?"checked":"") + '/> <label for="chkRefOn">ON</label>';
	 	  			 }else return '';
	 	   		 }
       		 }]
       	},
       	{label: "인스턴스기동/종료",  width: '20%', align: 'left',
      	 columns: [{key: "INSTANCE_YN3", label: "인스턴스",  width: '10%', align: 'center',
	      		formatter: function() {
		  			 if(this.item.visible3 == '1') {
		  				return '<input id="chkInsOn'+this.item.CD_INSTANCECD+'" name="ins'+this.item.CD_INSTANCECD+'" class="chkInsOn" type="radio" ' + ((this.item.checkbox5>0)?"checked":"") + '/> <label for="chkInsOn">기동</label>'
		 	   			 			+'<input id="chkInsOff'+this.item.CD_INSTANCECD+'" name="ins'+this.item.CD_INSTANCECD+'" class="chkInsOff" type="radio" ' + ((this.item.checkbox6>0)?"checked":"") + '/> <label for="chkInsOff">종료</label>';
		  			 }else return '';
		   		 }
	      	 }]
      	}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="second-grid"]'),
    sortable: false, 
    multiSort: false,
    multipleSelect: false,
    showRowSelector: false,
    header: {
        align: 'center'
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        	if(this.item.CR_RESULT != null && this.item.CR_RESULT != undefined && this.item.CR_RESULT != '') {
        		//거래제어결과확인 open
        		openWindow(1,this.item.CR_RESULT);
        	}else {
        		dialog.alert('결과파일이 생성되지 않았습니다.');
        	}
        },
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    columns: [
    	{key: 'node', 		label: '노드구분', 	width: '6%',	align: 'center'},
        {key: 'instancecd', label: '인스턴스구분', 	width: '10%', 	align: 'left'},
        {key: 'instanceid', label: '인스턴스',  	width: '10%', 	align: 'center'},
        {key: 'reqcd', 		label: '처리구분',  	width: '10%', 	align: 'center'},
        {key: 'runcd', 		label: '작업구분', 	width: '10%', 	align: 'left'},
        {key: 'cr_svrip', 	label: 'IP', 		width: '6%', 	align: 'left'},
        {key: 'cr_portno', 	label: 'PORT', 		width: '4%', 	align: 'center'},
        {key: 'acptdate', 	label: '적용일시', 	width: '6%', 	align: 'center'},
        {key: 'cr_prcdate', label: '완료일시', 	width: '6%', 	align: 'center'},
        {key: 'cr_prcrst', 	label: '처리결과', 	width: '16%', 	align: 'center'},
        {key: 'cr_rstmsg', 	label: '처리결과내용', 	width: '16%', 	align: 'left'}
    ]
});

//그리드 체크박스 클릭 할때마다 유지되도록
$(document).on('click','.chkChOn, .chkChOff', function(){
	var name = this.name;
	
	$(firstGridData).each(function(){
		if('cha'+this.CD_INSTANCECD == name){
			if($("#chkChOn"+this.CD_INSTANCECD).is(":checked")) {
				this.checkbox1 = '1';
				this.checkbox2 = '0';
			}else {
				this.checkbox1 = '0';
				this.checkbox2 = '1';
			}
		}
	});
});

$(document).on('click','.chkRefOn', function(){
	var name = this.name;
	
	$(firstGridData).each(function(){
		if(this.CD_INSTANCECD == name){
			if($("#chkRefOn"+this.CD_INSTANCECD).is(":checked")) this.selected1 = '1';
			else this.selected1 = '0';
		}
	});
});

$(document).on('click','.chkInsOn, .chkInsOff', function(){
	var name = this.name;
	
	$(firstGridData).each(function(){
		if('ins'+this.CD_INSTANCECD == name){
			if($("#chkInsOn"+this.CD_INSTANCECD).is(":checked")) {
				this.checkbox5 = '1';
				this.checkbox6 = '0';
			}else {
				this.checkbox5 = '0';
				this.checkbox6 = '1';
			}
		}
	});
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$('[data-ax5select="cboInstance"]').ax5select({
    options: []
});

$('[data-ax5select="cboDate"]').ax5select({
	options: []
});

$(document).ready(function() {
	getCodeInfo();
	
	//노드
	$('input:radio[name="radio"]').bind('click', function() {
		getCodeInfo();
	});
	
	//인스턴스
	$('#cboInstance').bind('change',function(){
		cboInstanceChange('000', 'N', 'TRUE');
	});
	
	//일시
	$('#cboDate').bind('change',function(){
		cboDateChange();
	});
	
	//적용
	$('#btnReq').bind('click',function(){
		btnReqClick();
	});
	
	//거래제어상태조회
	$('#btnSta').bind('click',function(){
		btnStaClick();
	});
	
	//이전건조회
	$('#chkBef').bind('click',function(){
		chkBefClick();
	});
	
	//리프레쉬
	$('#chkRef').bind('click',function(){
		chkRefClick();
	});
});

function getCodeInfo() {
	var codeInfos = getCodeInfoCommon([ 
		new CodeInfoOrdercd('CMR7000ID','ALL','Y','3','')
 	]);
	
	cboInstanceData = codeInfos.CMR7000ID;
	$('[data-ax5select="cboInstance"]').ax5select({
        options: injectCboDataToArr(cboInstanceData, 'cm_micode' , 'cm_codename')
	});
	
	if(cboInstanceData.length > 0) {
		cboInstanceChange('000', 'N', 'TRUE');
	}
}

function chkRefClick() {
	var checkSw = $('#chkRef').is(':checked');
	
	if(!$('#chkRef').is(':disabled')) {
		if(checkSw) {
			$('#chkStart').wRadio('check', false);
			$('#chkStart').wRadio('disabled', true);
			$('#chkStop').wRadio('check', false);
			$('#chkStop').wRadio('disabled', true);
			
			for(var i=0; i<firstGridData.length; i++) {
				firstGrid.list[i].enabled1 = '0';
				firstGrid.list[i].enabled2 = '0';
				firstGrid.list[i].checkbox5 = '0';
				firstGrid.list[i].checkbox6 = '0';
				firstGrid.list[i].selected1 = '1';
				firstGrid.list[i].selected2 = '1';
				firstGrid.list[i].selected3 = '1';
			}
			firstGrid.repaint();
		}else {
			$('#chkStart').wRadio('disabled', false);
			$('#chkStop').wRadio('disabled', false);
			for(var i=0; i<firstGridData.length; i++) {
				firstGrid.list[i].enabled1 = '1';
				firstGrid.list[i].enabled2 = '1';
				firstGrid.list[i].selected1 = '0';
				firstGrid.list[i].selected2 = '0';
				firstGrid.list[i].selected3 = '0';
			}
			firstGrid.repaint();
		}
	}else {
		$('#chkStart').wRadio('disabled', true);
		$('#chkStop').wRadio('disabled', true);
	}
}

function chkBefClick() {
	var checkSw = $('#chkBef').is(':checked');
	
	if(checkSw) {
		$('[data-ax5select="cboDate"]').ax5select('enable');
		
		//채널거래제어
		$('#chkOn').wRadio('check', false);
		$('#chkOn').wRadio('disabled', true);
		$('#chkOff').wRadio('check', false);
		$('#chkOff').wRadio('disabled', true);
		
		//인스턴스
		$('#chkStart').wRadio('check', false);
		$('#chkStart').wRadio('disabled', true);
		$('#chkStop').wRadio('check', false);
		$('#chkStop').wRadio('disabled', true);
		
		//리프레시
		$('#chkRef').wCheck('check', false);
		$('#chkRef').wCheck('disabled', true);
		
		$('#btnReq').prop('disabled', true);
		cmr7100_search();
	}else {
		$('[data-ax5select="cboDate"]').ax5select('disable');
		
		//채널거래제어
		$('#chkOn').wRadio('disabled', false);
		$('#chkOff').wRadio('disabled', false);
		
		//인스턴스
		$('#chkStart').wRadio('disabled', false);
		$('#chkStop').wRadio('disabled', false);
		
		//리프레시
		$('#chkRef').wCheck('disabled', false);
		
		$('#btnReq').prop('disabled', false);
		
		$('[data-ax5select="cboDate"]').ax5select('setValue',cboDateData[0].value,true);
	}
}

function cmr7100_search() {
	var data =  new Object();
	data = {
		requestType	: 'cmr7100_search'
	}
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successGetCmr7100);
}

function successGetCmr7100(data) {
	cboDateData = data;
	$('[data-ax5select="cboDate"]').ax5select({
		options: injectCboDataToArr(cboDateData, 'acptdate' , 'acptdate')
	});
}

function cboInstanceChange(date, status, Healthck) {
	var data =  new Object();
	data = {
		acptdate : date,
		node : $('input[name="radio"]:checked').val(),
		instance : getSelectedVal('cboInstance').cm_micode,
		status : status,
		Healthck : Healthck,
		requestType	: 'cmd7000_SELECTT'
	}
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successGetCmd7000);
}

function successGetCmd7000(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function btnReqClick() { //미개발
	chkacpt = 1;
	
	var instanceGbn = 'ALL';
	if(getSelectedIndex('cboInstance') == 1) instanceGbn = 'I1';
	else if(getSelectedIndex('cboInstance') == 2) instanceGbn = 'I2';
	else if(getSelectedIndex('cboInstance') == 3) instanceGbn = 'I3';
	
	var tmpObj = new Object();
	var tmpArr = [];
	for(var i=0; i<firstGridData.length; i++) {
		if(firstGridData[i].checkbox1=="1" || firstGridData[i].checkbox2=="1" ||
				firstGridData[i].selected1=="1" || firstGridData[i].checkbox5=="1" || firstGridData[i].checkbox6=="1") {
			
			tmpObj = new Object();
			tmpObj.editor = userId;
			tmpObj.node = firstGridData[i].CD_NODE;
			tmpObj.instancecd = firstGridData[i].CD_INSTANCECD;
			tmpObj.instanceid = firstGridData[i].CD_INSTANCEID;
			
			if(firstGridData[i].checkbox1=="1") tmpObj.trcontrol = "ON";
			else if(firstGridData[i].checkbox2=="1") tmpObj.trcontrol = "OFF";
			else tmpObj.trcontrol = "";
			
			if(firstGridData[i].selected1=="1") tmpObj.refresh = "ON";
			else  tmpObj.refresh = "";
			
			if(firstGridData[i].checkbox5=="1") tmpObj.instance = "ON";
			else if(firstGridData[i].checkbox6=="1") tmpObj.instance = "OFF";
			else  tmpObj.instance = "";
			
			tmpArr.push(tmpObj);
			tmpObj = null;
		}
	}
	
	$('#chkBef').wCheck('check',false);
	$('#chkBef').wCheck('disabled',true);
	$('[data-ax5select="cboDate"]').ax5select('disable');
	
	if(tmpArr.length < 1) {
		dialog.alert('상단그리드에서 거래제어 또는 인스턴스 기동/종료 구분(on/off)을 선택해 주시기바랍니다.');
		return;
	}
	
	var data =  new Object();
	data = {
		getCm7000List : tmpArr,
		requestType	: 'cmr7000_INSERTT'
	}
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successInsertCmr7000);
}

function successInsertCmr7000(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data == 'ERR'){
		dialog.alert('적용처리 중 오류가 발생하였습니다.');
		return;
	}else {
		acptDate = data;
		cmd7000_SELECT2(data);
		cboInstanceChange(data, 'N', 'TRUE');
	}
}

function btnStaClick() {
	var data =  new Object();
	data = {
		node : $('input[name="radio"]:checked').val(),
		instance : getSelectedIndex('cboInstance')==0?'':getSelectedVal('cboInstance').cm_micode,
		userid : userId,
		requestType	: 'trcontrol_call'
	}
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successTrcontrolcall);
}

function successTrcontrolcall(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data == 'OK'){
		cboInstanceChange('000', 'Y', 'FALSE');
	}else {
		dialog.alert('거래제어 상태조회 중 오류가 발생했습니다.');
		return;
	}
}

function cboDateChange() {
	chkacpt = 0;
	cboInstanceChange(getSelectedVal('cboDate').cr_acptdate, 'N', 'TRUE');
	cmd7000_SELECT2(getSelectedVal('cboDate').cr_acptdate);
}

function cmd7000_SELECT2(acptdate) {
	var data =  new Object();
	data = {
		acptdate : acptdate,
		requestType	: 'cmd7000_SELECT2'
	}
	$('[data-ax5grid="secondGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successGetCmr7010);
}

function successGetCmr7010(data) {
	$(".loding-div").remove();
	secondGridData = data;
	secondGrid.setData(secondGridData);
	
	if(chkacpt != 0){
		conTimer();
	}
	
	if($('#chkBef').is(':checked')) {
		if(getSelectedIndex('cboDate') == 0) return;
		
		if(secondGridData.length > 0) {
			if(secondGridData[0].cr_node == '01') {
				$('#rdo1').wRadio('checked', true);
			}
		}
		
		$('[data-ax5select="cboInstance"]').ax5select('setValue',cboInstanceData[0].value,true);
		cboInstanceChange(getSelectedVal('cboDate').cr_acptdate, 'N', 'TRUE');
	}
}

function conTimer() {
	var data =  new Object();
	data = {
		acptdate : acptdate,
		requestType	: 'cmd7000_CHK'
	}
	ajaxAsync('/webPage/ecmr/Cmr3800Servlet', data, 'json',successCmd7000Chk);
}

var inter 	 = null;
var timerSw1 = 0;

function Cmd7000Chk(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	var selectchk = Number(data);
	
	if(selectchk > 0 && chk == 0) {
		timerSw1 = 5000; //5초
		inter = setInterval(timerStart, timerSw1);
		
		chk = 1;
	}else if(selectchk > 0 && chk == 1){
		timerSw1 = 5000; //5초
		inter = setInterval(timerStart, timerSw1);
		
	}else {
		clearInterval(inter);//STOP
		inter = null;
		
		dialog.alert('적용처리 완료되었습니다.');
		chk = 0;
		$('#chkBef').wCheck('disabled',false);
	}
}

function timerStart() {
	cmd7000_SELECT2(acptdate);
	cmd7000_SELECTT(acptdate, 'N', 'TRUE');
}

function openWindow(type,reqNo) {
	var nHeight, nWidth, cURL, winName;

	if ( ('pop_'+type) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = 'pop_'+type;

	var f = document.popPam;
	f.user.value 	= userId;
    f.acptno.value	= replaceAllString(reqNo, "-", "");
    
    if (type == 1) {
    	nWidth  = 1046;
    	nHeight = 700;
	    cURL = "/webPage/winpop/PopTransactionResult.jsp";
	} else {
		dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
		return;
	}
	
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}