/** 모니터링체크(eCmc0300_tab.mxml) 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 방지연
 * 	버전 : 1.0
 *  수정일 : 2022-00-00
 */

var userId 		 	= window.top.userId;

var reqCd	 		= window.parent.reqCd;
var strAcptNo 		= window.parent.strAcptNo;
var strStatus		= window.parent.strStatus;
var strIsrId		= window.parent.strIsrId;  

var firstGrid 		= new ax5.ui.grid();

var firstGridData	= [];
var cboUserData 	= [];
var cboSysData		= [];
var srData 			= {}; 

var strSysCd 		= '';
var completeReadyFunc = false;

$('[data-ax5select="cboUser"]').ax5select({
    options: []
});
$('[data-ax5select="cboSys"]').ax5select({
    options: []
});

firstGrid.setConfig({
	target : $('[data-ax5grid="firstGrid"]'),
	sortable : false,
	multiSort : false,
	multiselect: false,
	header : {
		align : "center"
	},
	page : false,
	body : {
		onClick : function() {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDataChanged: function(){
    		this.self.repaint();
    	}
	},
	columns : [
		{key: "cc_detgubun", 	label: "상세구분",  	width: '25%', 	align: 'left', 
			styleClass: function() { return 'fontStyle-bold'; }
		},
        {key: "cc_item", 		label: "항목",  		width: '37%', 	align: 'left'},
        {key: "cc_reqyn", 		label: "필수여부",  	width: '8%',	align: 'center'},
        {key: "checked", 		label: "평가",  		width: '8%', 	align: 'center',
    		 formatter: function() {
    			 var checked = this.item.sel;
    			 var disabled = this.item.cc_reqyn;
    			 return '<input id="optPass'+this.item.cc_gbncd+'" class="optPass" type="radio" name="'+this.item.cc_gbncd+'" '+((checked == "P")?"checked":"")+' /> <label for="optPass">PASS</label>'
    			 +'<input id="optFail'+this.item.cc_gbncd+'" class="optFail" type="radio" name="'+this.item.cc_gbncd+'" '+((checked == "F")?"checked":"")+'/> <label for="optFail">FAIL</label>'
    			 +'<input id="optNull'+this.item.cc_gbncd+'" class="optNull" type="radio" name="'+this.item.cc_gbncd+'" '+((checked == "N")?"checked":"")+((disabled == "Y")?"disabled":"")+'/> <label for="optNull">N/A</label>';
    		 }
        }
	]
});

//설정, 해제 라디오 버튼 클릭 할때마다 유지되도록
$(document).on('click','.optPass, .optFail, .optNull', function(){
	var name = this.name;
	console.log('this==>',this);
	
	$(firstGridData).each(function(){
		console.log('cc_item==>',this.cc_gbncd);
		if(this.cc_gbncd == name){
			if($("#optPass"+this.cc_gbncd).is(":checked")){
				this.sel = "P";
			} else if($("#optFail"+this.cc_gbncd).is(":checked")){
				this.sel = "F";
			} else{
				this.sel = "N";
			}
		}
	});
});

$(document).ready(function() {
	firstGrid.setConfig();
	
	// 등록/수정
	$('#cmdReq').bind('click', function() {
		cmdReq_Click();
	});
	
	// 조회
	$('#cmdQry').bind('click', function() {
		cmdQry_Click();
	});
	
	// 개발자 변경
	$('#cboUser').bind('change', function() {
		cboUser_Change();
	});
	
	// 시스템 변경
	$('#cboSys').bind('change', function() {
		cboSys_Change();
	});
	
	completeReadyFunc = true;
});

function screenInit(gbn) {
	if(gbn == 'M') {
		firstGridData = [];
		firstGrid.setData([]);
		
		cboUserData = [];
		$('[data-ax5select="cboUser"]').ax5select({
		    options: []
		});
		
		cboSysData = [];
		$('[data-ax5select="cboSys"]').ax5select({
		    options: []
		});
		
		if(reqCd == 'XX' || reqCd == '69' || reqCd == 'MY') {
			$('#cmdReq').prop("disabled", true);
		}
		return;
	}
	
	if(reqCd == 'XX' || reqCd == '69' || reqCd == 'MY') {
		$('#cmdReq').prop("disabled", true);
	}
}

function initCboSystem() {
	var data = {
		cc_srid : strIsrId,
		reqCD : reqCd,
		userID : userId,
		requestType : 'getScmuserList'
	}
	
	console.log('[모니터링] getScmuserList ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json', successGetScmuserList);
}

function successGetScmuserList(data) {
	cboUserData = data;
	$('[data-ax5select="cboUser"]').ax5select({
        options: injectCboDataToArr(cboUserData, "cc_scmuser", "scmuser")
	});
	
	if(cboUserData.length > 1) {
		for(var i=0; i<cboUserData.length; i++) {
			if(cboUserData[i].cc_scmuser == userId) {
				$('[data-ax5select="cboUser"]').ax5select('setValue', userId, true);
				break;
			}
		}
		
		if(getSelectedIndex('cboUser') < 1 && cboUserData.length == 2) {
			$('[data-ax5select="cboUser"]').ax5select('setValue', cboUserData[1].cc_scmuser, true);
		}
		
		cboUser_Change();
	}
}

function cboUser_Change() {
	firstGridData = [];
	firstGrid.setData([]);
	
	cboSysData = [];
	$('[data-ax5select="cboSys"]').ax5select({
	    options: []
	});
	
	$('#cmdReq').prop("disabled", true);
	
	var data = {
		srID : strIsrId,
		scmUser : getSelectedVal('cboUser').cc_scmuser,
		requestType : 'getSyscd'
	}
	
	console.log('[모니터링] getSyscd ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0600Servlet', data, 'json', successGetSyscd);
}

function successGetSyscd(data) {
	cboSysData = data;
	$('[data-ax5select="cboSys"]').ax5select({
        options: injectCboDataToArr(cboSysData, "syscd", "sysmsg")
	});
	
	if(cboSysData.length > 1) {
		$('[data-ax5select="cboSys"]').ax5select('setValue', cboSysData[1].syscd, true);
		cboSys_Change();
	}
}

function cboSys_Change() {
	firstGridData = [];
	firstGrid.setData([]);
	
	if(getSelectedVal('cboSys') == null || getSelectedVal('cboSys') == undefined) return;
	if(getSelectedIndex('cboSys') < 1) return;
	
	strSysCd = getSelectedVal('cboSys').syscd;
	if(getSelectedVal('cboUser').cc_scmuser == userId
			&& getSelectedVal('cboUser').secuyn == 'OK' && reqCd == '63') {
		$('#cmdReq').prop("disabled", false);
	}
	$('#cmdQry').prop("disabled", false);
	
	var data = {
		SysCd : strSysCd,
		SrId : strIsrId,
		strReqCd : reqCd,
		requestType : 'getSecCheckList'
	}
	
	console.log('[모니터링] getSecCheckList ==>', data);
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmc/Cmc0600Servlet', data, 'json', successGetSecCheckList);
}

function successGetSecCheckList(data) {
	$(".loding-div").remove();
	firstGridData = data;
	firstGrid.setData(firstGridData);
	firstGrid.setConfig();
	firstGrid.repaint();
	
	if(firstGridData.length > 0) {
		if(reqCd == '63') {
			//$('#cmdReq').prop("disabled", false);
		}
		getApplyInfo();
	}
}

function getApplyInfo() {
	var data = {
		srID : strIsrId,
		sysCd : strSysCd,
		editorID : getSelectedVal('cboUser').cc_scmuser,
		requestType : 'getApplyInfo'
	}
	
	console.log('[모니터링] getApplyInfo ==>', data);
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmc/Cmc0600Servlet', data, 'json', successGetApplyInfo);
}

// 등록된 체크값 가져오기  결과
function successGetApplyInfo(data) {
	$(".loding-div").remove();
	var tmpData = data;
	if(tmpData.length > 0 && reqCd != 'XX' && reqCd != '69') {
		//flex에 resultHandler없음
		//Cmc0600.getMonitorChkOk(strIsrId);
	}
	
	console.log('[bef] ==>', firstGridData);
	var tmpObj = new Object();
	for(var i=0; i<firstGridData.length; i++) {
		if(tmpData.length > 0) {
			for(var j=0; j<tmpData.length; j++) {
				if(reqCd != '63') {
					tmpObj = firstGridData[i];
					tmpObj.sel = tmpData[j].cc_reqyn;
					firstGridData.splice(i, 1, tmpObj);
					tmpObj = null;
				}
				
				if(firstGridData[i].cc_gbncd == tmpData[j].cc_gbncd) {
					tmpObj = firstGridData[i];
					tmpObj.sel = tmpData[j].sel;
					firstGridData.splice(i, 1, tmpObj);
					tmpObj = null;
					tmpData.splice(j);
					break;
				}
			}
		}
	}
	
	console.log('[aft] ==>', firstGridData);
	firstGrid.setConfig();
	firstGrid.repaint();
}

function cmdQry_Click() {
	getApplyInfo();
}

function cmdReq_Click() {
	if(getSelectedVal('cboUser') == null || getSelectedVal('cboUser') == undefined) return;
	if(getSelectedIndex('cboUser') < 1) return;
	if(getSelectedVal('cboSys') == null || getSelectedVal('cboSys') == undefined) return;
	if(getSelectedIndex('cboSys') < 1) return;
	
	var tmpObj = new Object();
	var tmpArr = [];
	for(var i=0; i<firstGridData.length; i++) {
		if(firstGridData[i].sel == '0') {
			dialog.alert('평가에 대한 값 선택은 필수입니다.\nPASS 또는 FAIL 또는 N/A를 선택해 주시기 바랍니다.');
			return;
		}
		
		tmpObj = new Object();
		tmpObj.cc_syscd = strSysCd;
		tmpObj.cc_srid = strIsrId;
		tmpObj.cc_scmuser = getSelectedVal('cboUser').cc_scmuser;
		tmpObj.cc_editor = userId;
		tmpObj.cc_gbncd  = firstGridData[i].cc_gbncd;
		tmpObj.cc_reqyn  = firstGridData[i].cc_reqyn;
		tmpObj.cc_eval = firstGridData[i].sel;
		if(tmpObj.cc_eval == null || tmpObj.cc_eval == '' || tmpObj.cc_eval == '0' || tmpObj.cc_eval == undefined) {
			dialog.alert('평가항목을 체크해주십시오.');
			return;
		}
		tmpArr.push(tmpObj);
		tmpObj = null;
	}
	
	if(tmpArr.length == 0) {
		dialog.alert('평가항목을 체크해주십시오.');
		return;
	}
	
	var data = {
		tmp_obj : tmpArr,
		requestType : 'setSecChkInsertUpdate'
	}
	
	console.log('[모니터링] setSecChkInsertUpdate ==>', data);
	ajaxAsync('/webPage/ecmc/Cmc0600Servlet', data, 'json', successGetSecChkInsertUpdate);
}

function successGetSecChkInsertUpdate(data) {
	if(data == 'insert') {
		dialog.alert('등록완료 처리되었습니다.');
	}else if(data == 'update') {
		dialog.alert('수정완료 처리되었습니다.');
	}else {
		dialog.alert('등록 혹은 수정이 처리 되지 않았습니다.');
	}
	
	window.parent.loadiFrameEnd();
	firstGridData = [];
	firstGrid.setData([]);
}