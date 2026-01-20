var userId 		= window.parent.userId;
var reqCd 		= window.parent.reqCd;
var sysCd 		= window.parent.sysCd;
var jobCd		= window.parent.jobCd;
var checkInPrgList = window.parent.secondGridData;

var firstGrid  	= new ax5.ui.grid();
var secondGrid  = new ax5.ui.grid();

var firstGridData 		= [];
var firstGridSimpleData = [];
var secondGridData 		= [];
var cboConfData			= [];

var data          		= null;
var selCd 				= "U";
var beforSelIndex 		= -9;
var clickConfIndex 		= null;
var syscd = "";
$('input:radio[name^="optBase"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$('[data-ax5select="cboConf"]').ax5select({
    options: []
});

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    height : window.parent.confGridHeight,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
        },
    	trStyleClass: function () {
    		if (this.item.colorsw == '3'){
    			return "fontStyle-cncl";
    		} else if(this.item.colorsw == '5'){
    			return "fontStyle-eror";
    		} else if (this.item.colorsw == '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        	confSet(this.item);
        }
    },
    columns: [
        {key: "rgtcd", 			label: "직무",	width: '50%'},
        {key: "cm_username",	label: "성명",	width: '50%'}
    ]
});

secondGrid.setConfig({
    target: $('[data-ax5grid="secondGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    height : window.parent.confGridHeight,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
        	this.self.select(this.dindex);
       		secondGridClick(this.dindex, this.item);
        },
    	trStyleClass: function () {
    		if (this.item.colorsw == '3'){
    			return "fontStyle-cncl";
    		} else if(this.item.colorsw == '5'){
    			return "fontStyle-eror";
    		} else if (this.item.colorsw == '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	},
        onDBLClick: function () {
        	removeSecondGrid(this.item, this.dindex);
        }
    },
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "결재(순차)"},
            {type: 2, label: "후결(순차)"}
        ],
        popupFilter: function (item, param) {
        	if(param.dindex == undefined || param.dindex == null || param.dindex < 0){
        		return false;
        	}
        	
        	if((param.item.cm_gubun == '3' || param.item.cm_gubun == '6') && param.item.userSetable == false){
        		return true;
			}
        	return false;
       	 
        },
        onClick: function (item, param) {
	        contextMenuClick(param.item, item);
	        secondGrid.contextMenu.close();// 또는 return true;
        }
    },
    columns: [
        {key: "cm_name", 	label: "단계명칭",	width: '35%'},
        {key: "arysv_text", label: "결재자",	width: '33%', align: 'left'},
        {key: "cm_sgnname", label: "결재",	width: '32%'}
    ]
});

$(document).ready(function() {
	$('#lblDel').hide();
	
	$('#btnClose').bind('click',function() {
		window.parent.confirmData = [];
		popClose();
	});
	
	$('#btnReq').bind('click',function(){
		register();
	});

	$('#btnSearch').bind('click',function(){
		if($('#txtName').val().trim().length < 1) {
			firstGrid.setData(firstGridData);
			return;
		}
		
		selCd = "U";
		firstGrid.setData([]);
		getSignUser();
	});
	
	$('#txtName').bind('keypress',function(event){
		if(event.keyCode == 13){
			$('#btnSearch').click();
		}
	});
	
//	$('[name = "optBase"]').bind('change',function(){
//		if($(this).val() == "추가"){
//			selCd = "I";
//			$("#AddArea").show();
//			firstGrid.setData(firstGridData);
//		}
//		else{
//			selCd = "U";
//			$("#AddArea").hide();
//			firstGrid.setData(firstGridSimpleData);
//		}
//	});

//	$('#btnDown').bind('click',function(){
//		confChange('2');
//	});
//
//	$('#btnUp').bind('click',function(){
//		confChange('1');
//	});
	
	$('#cboConf').bind('change',function(){
		cboConfChange();
	});
	
	//선택
	$('#btnSel').bind('click',function(){
		if(firstGrid.getList('selected').length == 0) return;
		confSet(firstGrid.getList('selected')[0]);
	});
	
	//결재삭제
	$('#btnDel').bind('click',function(){
		btnDelClick();
	});
	
	getConfirmInfo();
	//getMyConf();
});

function popClose(){
	window.parent.approvalModal.close();
}

function getMyConf() {
	var data = new Object();
	var tmpData = window.parent.confirmInfoData;

	var data = {
		UserID	: tmpData.UserId,
		SysCd : tmpData.SysCd,
		JobCd : tmpData.JobCd,
		requestType	: 'getMyConf'
	}
	console.log('getMyConf',data);
	ajaxAsync('/webPage/common/SignInfoServlet', data, 'json',successGetMyConf);
}

function successGetMyConf(data) {
	cboConfData = data;
	$('[data-ax5select="cboConf"]').ax5select({
        options: injectCboDataToArr(cboConfData, 'ID' , 'cm_signnm')
	});
	
	if(cboConfData.length > 0) cboConfChange();
}

function cboConfChange() {
	var tmpRgt = '';
	var tmpPos = '';

	firstGridData = [];
	firstGrid.setData([]);
	
	if(getSelectedIndex('cboConf') > 0) {
		for(var i=0; i<secondGridData.length; i++) {
			if(secondGridData[i].cm_gubun == '3' || secondGridData[i].cm_gubun == '4') {
				if(secondGridData[i].cm_position != null && secondGridData[i].cm_position != undefined && secondGridData[i].cm_position != '') {
					if(tmpRgt.indexOf(secondGridData[i].cm_position) < 0) {
						if(tmpRgt != '') tmpRgt = tmpRgt + ',' + secondGridData[i].cm_position;
						else tmpRgt = secondGridData[i].cm_position;
					}
				}
				
				if(secondGridData[i].cm_duty != null && secondGridData[i].cm_duty != undefined && secondGridData[i].cm_duty != '') {
					if(tmpPos.indexOf(secondGridData[i].cm_duty) < 0) {
						if(tmpPos != '') tmpPos = tmpPos + ',' + secondGridData[i].cm_duty;
						else tmpPos = secondGridData[i].cm_duty;
					}
				}
			}
		}
		
		getSignInfo();
		var data = new Object();

		var data = {
			UserID	: userId,
			SignNm : getSelectedVal('cboConf').cm_signnm,
			RgtCd : tmpRgt,
			PosCd : tmpPos,
			requestType	: 'getSignInfo'
		}
		console.log('getSignInfo',data);
		ajaxAsync('/webPage/common/SignInfoServlet', data, 'json',successGetSignInfo);
	}
}

function successGetSignInfo(data) {
	firstGridData = data;
	firstGridData = firstGridData.filter(function(item) {
		if(item.cm_manid == "Y") return true;
		else return false;
	});
	firstGrid.setData(firstGridData);
}

function getSignUser(){
	var serarchData = [];
	for(var i=0; i<firstGridData.length; i++) {
		var searchName = firstGridData[i].cm_username;
		if(searchName.indexOf($('#txtName').val()) > -1) {
			var userObj = new Object();
			userObj = firstGridData[i];
			serarchData.push(userObj);
		}
	}
	
	firstGrid.setData(serarchData);
} 

function getConfirmInfo(){
	var Confirm_Info = new Object();
	Confirm_Info = window.parent.confirmInfoData;
	syscd = Confirm_Info.SysCd; // 20221223 내부직원 시스템코드 저장용
	var tmpData = {
		paramMap	: Confirm_Info,
		requestType	: 'Confirm_Info'
	}
	console.log('Confirm_Info',tmpData);
	ajaxAsync('/webPage/ecmr/Confirm_selectServlet', tmpData, 'json',successGetConfirmInfo);
}

function successGetConfirmInfo(data){
	console.log('successGetConfirmInfo ==>',data);
	if (data == undefined || data == null) return;
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}
	$(data).each(function(){
		this.arysv_text = this.arysv[0].SvSum;
	});
	
	for(var j=0; j<data.length; j++) {
		for(var q=j+1; q<data.length; q++) {
			if(data[j].cm_name == data[q].cm_name) {
				if(data[j].cm_baseuser == data[q].cm_baseuser) {
					data.splice(j,1);
					j--;
					break;
				}
			}
		}
	}

	var rgtcdList = getMyRgtCd(userId);
	if(rgtcdList.indexOf("DG") > -1){
	} else {
		for(var z=0; z<data.length; z++) {
			if(data[z].cm_gubun == "B") {
				data.splice(z,1);
				z--;
				break;
			}
		}
	}
	console.log(checkInPrgList);
	
	// 20230131 rpa 결재 
	var rpa = "N";
	for(var q=0; q<checkInPrgList.length; q++) {
		if(checkInPrgList[q].cr_rpaflag != null && checkInPrgList[q].cr_rpaflag !='' && checkInPrgList[q].cr_rpaflag == "Y") {
			rpa = "Y";
			break;
		}
	}
	
	if(rpa == "N"){
		for(var z=0; z<data.length; z++) {
			if(data[z].cm_baseuser == "RP") {
				data.splice(z,1);
				z--;
				break;
			}
		}
	}
	
	secondGridData = data;
	secondGrid.setData(secondGridData);
	
	if(secondGridData.length == 0 ){
		dialog.alert('결재정보가 없습니다. 관리자에게 등룍요청한 후 처리하시기 바랍니다.');
		$("#btnReq").prop('disabled',true);
		return;
	}else{
		$("#btnReq").prop('disabled',false);
	}
	
	cmdInit();
}

function cmdInit(){
	var tmpRgt = "";
	var tmpRgt2 = "";
	var tmpPos = "";
	var tmpSysCd = "";
	var findSw = false;
	for (var i=0; i<secondGridData.length; i++) {
		console.log('visible=',i,secondGridData[i].visible);
		if (secondGridData[i].visible == "1") {
			$('#btnDel').show();
			$('#lblDel').show();
			$('#chkAll').show();
		}
		// 팀내 책임자일 경우 선택가능한 직무 리스트 작성
		if (secondGridData[i].cm_gubun == "3" || secondGridData[i].cm_gubun == "B") {// 팀내책임자[3]
			if (secondGridData[i].cm_position != null && secondGridData[i].cm_position != "") {
				tmpRgt = tmpRgt + "," + secondGridData[i].cm_position;
			}
			if (secondGridData[i].cm_duty != null && secondGridData[i].cm_duty != "") {
				tmpPos = tmpPos + "," + secondGridData[i].cm_duty;
			}
		}
	}
	
	if ((tmpRgt != null && tmpRgt != "") || tmpSysCd != "") {
		selCd = "U";

		var tmpData = new Object();
		tmpData = {
			UserId : userId,
			RgtCd : tmpRgt,
			PosCd : tmpPos,
			requestType	: 'getSignLst'
		}
		
		console.log('[getSignLst] ==>', tmpData);
		ajaxAsync('/webPage/common/SignInfoServlet', tmpData, 'json',successGetSignList);
	}
}

function successGetSignList(data){
	if (data == undefined || data == null) return;
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
		return;
	}
	console.log('[successGetSignList] ==>', data);
	firstGridData = data;
	firstGridData = firstGridData.filter(function(item) {
		if(item.cm_manid == "Y") return true;
		else return false;
	});
	firstGrid.setData(firstGridData);
	firstGrid.setColumnSort({cr_rsrcname:{seq:0, orderBy:'asc'}});
}

function contextMenuClick(data, item){
	if (data.delyn == false && item.label == "참조") {
		dialog.alert("해당단계는 참조로 변경할 수 없습니다.");
		return;
	}
	
	var intCodeSet = "2";
	if ( item.type == "2" ) {// item.label == "후결(순차)"
		intCodeSet = "4";
	}
	secondGridData[data.__index].cm_blank = intCodeSet;
	secondGridData[data.__index].cm_common = intCodeSet;
	secondGridData[data.__index].cm_congbn = intCodeSet;
	secondGridData[data.__index].cm_emg = intCodeSet;
	secondGridData[data.__index].cm_emg2 = intCodeSet;
	secondGridData[data.__index].cm_holi = intCodeSet;
	secondGridData[data.__index].cm_sgnname = item.label;
	
	// grdLst2_dp.setItemAt(selObj,grdLst2.selectedIndex);
	secondGrid.repaint();
}

// 결재자 추가
function confSet(data){
	var i = 0;
	var swFind = false;
	if (data == null || data == "") {
		dialog.alert("결재자를 선택한 후 처리하시기 바랍니다.");
		return;
	}
	
	if (data.length > 1) {
		dialog.alert("결재자를 다수로 선택할 수 없습니다. 한사람씩 선택하여 주시기 바랍니다.");
		return;
	}
	
	for (i=0; i<secondGridData.length; i++) {
		swFind = false;
		if (selCd == "U") {
			if (secondGridData[i].cm_gubun == "3" || secondGridData[i].cm_gubun == "B") {
				if(secondGridData[i].cm_position != null && secondGridData[i].cm_position != undefined && secondGridData[i].cm_position != '') {
					if (secondGridData[i].cm_position.indexOf(data.cm_rgtcd)>=0) {
			   			swFind = true;
			   			break;
					}
				}
				if(secondGridData[i].cm_duty != null && secondGridData[i].cm_duty != undefined && secondGridData[i].cm_duty != '') {
					if (secondGridData[i].cm_duty.indexOf(data.cm_rgtcd)>=0) {
			   			swFind = true;
			   			break;
					}
				}
							
			} 
		} else if (selCd == "I") {
			swFind = true;
			for (i = 0;i < secondGridData.length;i++) {
			  	if (secondGridData[i].cm_gubun == "3") {
			  		if (secondGridData[i].cm_baseuser != null) {
			  			if (secondGridData[i].cm_baseuser == data.cm_signuser) {
			  				dialog.alert("이미 결재단계에 등록된 결재자입니다. 확인 후 처리하시기 바랍니다.");
		  					return;
		  				}
			  		}
			  	}
			}
		}
	}

	if ( swFind ) {
		var etcObj = new Object();
		var etcsubObj = new Object();
		var etcsubarc = [];
		if (selCd == "U") {
			etcObj = secondGridData[i];
			etcObj.cm_baseuser = data.cm_signuser;
	
			etcsubObj.SvTag  = data.cm_username;
			etcsubObj.SvSum  = data.rgtcd + ' ' + data.cm_username;
			etcsubObj.SvUser = data.cm_daegyul;
			etcsubarc.push(etcsubObj);
			etcObj.arysv = etcsubarc;
			etcObj.arysv_text = etcsubarc[0].SvSum;
			etcObj.delyn = false;
			// etcObj.cm_duty = secondGridData[i].cm_position;
  			secondGridData[i] = etcObj;
		} else {
	  		etcObj.cm_name = data.rgtcd;		
	  		etcObj.cm_sgnname = "결재(순차)";
	  		etcObj.cm_baseuser = data.cm_signuser;
	  		etcObj.cm_congbn = "2";
			etcObj.cm_emg2 = "2";
			etcObj.cm_prcsw = "N";
			etcObj.cm_common = "2";
			etcObj.cm_blank = "2";
			etcObj.cm_holi = "2";
			etcObj.cm_emg = "2";
			etcObj.cm_duty = data.cm_rgtcd;
			etcObj.cm_position = data.cm_rgtcd;
			etcObj.cm_gubun = "3";
			etcObj.cm_seqno = "0";
			etcObj.delyn = "Y";
			
			etcsubObj.SvTag  = data.cm_username;
			etcsubObj.SvUser = data.cm_daegyul;
			etcsubarc.push(etcsubObj);
			etcObj.arysv = etcsubarc;
			etcObj.arysv_text = etcsubarc[0].SvSum;
			
			etcObj.delyn = true;
			etcObj.userSetable = false;
			
			if (secondGridData.length == 0) secondGridData.push(etcObj);
	  		// else grdLst2_dp.addItemAt(etcObj,grdLst2.selectedIndex);
	  		// 결재절차 그리드[grdLst2]에서 선택한 index값이 없을때는 그리드의 len 값으로 셋팅
	  		else if ( beforSelIndex == -9 ){
	  			secondGridData.push(etcObj);
	  		} else{
	  			secondGridData.splice(beforSelIndex,0,etcObj);
	  		}
			secondGrid.clearSelect();
			beforSelIndex = -9;
		}
		secondGrid.setData(secondGridData);
	}
}

function register(){
	if(secondGridData.length == 0){
		dialog.alert('결재정보가 없습니다. 관리자에게 등록요청한 후 처리하시기 바랍니다.');
		return;
	}
	var Msg = '결재절차를 등록하고, 계속 진행하시겠습니까?';

	mask.open();
	confirmDialog.confirm({
		title: '확인',
		msg: Msg,
	}, function(){
		if(this.key === 'ok') {
			registerAfter();
		}
		mask.close();
	});
}

function registerAfter(){
	var findSw = false;
	var i = 0;
	var j = 0;
	// var gubun3Cnt:int = 0;
	var tmpAry = null;
	var tmpNAry = null;
	var tmpNObj = null;
	
	for (i = 0;secondGridData.length>i;i++) {
		if (secondGridData[i].cm_gubun != "8") {
			if (secondGridData[i].arysv[0].SvUser == null || secondGridData[i].arysv[0].SvUser == "") {
				   dialog.alert("[" + secondGridData[i].cm_name + "]에 대한 결재자를 지정한 후 처리하십시오.");
				   return;
			}
		}
	}
	for (i=0;secondGridData.length>i;i++){
		tmpAry = secondGridData[i].arysv;
		if (tmpAry.length < 2){
			continue;
		}
		tmpNAry =[];
		for (j=0;j<tmpAry.length;j++){
			if (tmpAry[j].selectedFlag == "1"){
				tmpNAry.push(tmpAry[j]);
				break;
			}
		}
		tmpNObj = new Object;
		tmpNObj = secondGridData[i];
		tmpNObj.arysv = tmpNAry;
		secondGridData[i] = tmpNObj;
	}
	secondGrid.repaint();
	closeFlag = true;
	window.parent.confirmData = secondGridData;
	popClose();
}

function removeSecondGrid(data, index){
	if(data == null){
		return;
	}

//	if (data.delyn == null || data.delyn == "") return;
//	if (data.delyn != "Y") return;
	
	if (data.cm_gubun == "3" || data.cm_gubun == "C" || data.cm_gubun == "R") {		
		confirmDialog.confirm({
			title: '확인',
			msg: "결재단계 ["+data.cm_name+"]를 취소할까요?",
		}, function(){
			if(this.key === 'ok') {
				cnclProc(data, index);
			}
		});
	}
}

function cnclProc(data, index){
	var tmpObj = new Object();

	if (data.delyn == true) {
		secondGrid.removeRow(index);
		secondGridData.splice(index,1);
	} else {
		tmpObj = data;
		tmpObj.userSetable = true;
		// tmpObj.arysv = etcsubarc;
		tmpObj.arysv[0].SvTag = "";
		tmpObj.arysv[0].SvUser = "";
		tmpObj.arysv[0].SvSum = "";
		tmpObj.cm_baseuser = "";
		if (tmpObj.cm_position == null || tmpObj.cm_position == undefined || tmpObj.cm_position == "") {
			tmpObj.cm_gubun = "C";
		}
		secondGridData[index]=tmpObj;
	}
	secondGrid.setData(secondGridData);
	secondGrid.clearSelect();
	beforSelIndex = -9;
}

function secondGridClick(index, data){
	if(index == null || index == undefined){
		return;
	}
	if(index == beforSelIndex){
		beforSelIndex = -9;
		secondGrid.clearSelect();
		return;
	}
	
	beforSelIndex = index;
}

function btnSelClick() {
	
}

function btnDelClick() {
	var findSw = false;
	for(var i=0; i<secondGridData.length; i++) {
		if(secondGridData[i].visible == '1') {
			if(secondGridData[i].selected == '1') {
				secondGridData.splice(i,1)
				i--;
				findSw = true;
			}
		}
	}
	
	if(!findSw) {
		dialog.alert("삭제대상 파트협조를 선택한 후 처리하시기 바랍니다.");
		return;
	}
}

//function confChange(change){
//	
//	if(clickConfIndex == null){
//		dialog.alert("자동처리는 순서를 변경할 수 없습니다.");
//		return;
//	}
//	
//	var tmpData = secondGridData[clickConfIndex];
//	if(change == '1'){
//		if(clickConfIndex < 1){
//			return;
//		}
//		// 그리드 포커스 이동시 index 0이면 가장아래로 가는 오류? 발생 하므로 HOME으로 이동
//		var focusIn = clickConfIndex -1 === 0 ? 'HOME' : clickConfIndex -1; 
//		
//		secondGrid.addRow(clone(secondGridData[clickConfIndex]), clickConfIndex-1, {focus: focusIn});
//		secondGrid.deleteRow(clickConfIndex+1);
//		secondGridData = clone(secondGrid.list);
//		clickConfIndex = clickConfIndex-1;
//	}else{
//		if(clickConfIndex+1 >= secondGridData.length){
//			return;
//		}
//		
//		secondGrid.addRow(clone(secondGridData[clickConfIndex]), clickConfIndex+2, {focus: clickConfIndex+1});
//		secondGrid.removeRow(clickConfIndex);
//		secondGridData = clone(secondGrid.list);
//		clickConfIndex = clickConfIndex+1;
//	}
//}

//function deleteRow(){
//	var secondGridSelected = second.getList('selected');
//	
//	var findSw = false;
//	
//	for(var i=0 ;  i>secondGridSelected.length ; i++ ){
//		if(secondGridSelected[i].visible == '1'){
//			secondGrid.removeRow(secondGridSelected.dindex);
//			findSw = true;
//		}
//	}
//	if(findSw){
//		dialog.alert('삭제대상을 선택한 후 처리하시기 바랍니다.');
//		return;
//	}
//}