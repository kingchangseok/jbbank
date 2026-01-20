var pReqNo  = null;
var pReqCd  = null;
var pItemId = null;
var pUserId = null;

var scriptGrid  	   = new ax5.ui.grid();

var options 	   = [];

var data           = null; //json parameter
var cboScriptData  = null; //처리구분콤보 데이타
var scriptGridData = null; //처리결과목록 데이타
var baseData       = null; //기본정보 데이타

var f = document.getReqData;
pReqNo = f.acptno.value;
pReqCd = pReqNo.substr(4,2);
pItemId = f.itemid.value;
pUserId = f.user.value;

scriptGrid.setConfig({
    target: $('[data-ax5grid="scriptGrid"]'),
    //sortable: true, 
    //multiSort: true,
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {},
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    contextMenu: {},
    columns: [
        {key: "cm_cmdname", label: "실행명령",  width: '100%', align: 'left'}
    ]
});

$(document).ready(function(){
	if (pReqNo == null) {
		return;
	}
	
	//처리구분 콤보선택
	$('#cboScript').bind('change', function() {
		if (getSelectedIndex('cboScript') >= 0){
			/*
			 사용안함
			 
			if (getSelectedVal('cboScript').cm_runpos == 'L') {
				$('#txtScriptName').val('실행위치 : Local');
			}
			if (getSelectedVal('cboScript').cm_totyn == 'Y') {
				if ($('#txtScriptName').val().length >0) {
					$('#txtScriptName').val($('#txtScriptName').val()+',');
				}
				$('#txtScriptName').val($('#txtScriptName').val()+'일괄쉘실행');
			}
			if (getSelectedVal('cboScript').cm_seqyn == 'Y') {
				if ($('#txtScriptName').val().length >0) {
					$('#txtScriptName').val($('#txtScriptName').val()+',');
				}
				$('#txtScriptName').val($('#txtScriptName').val()+'순차쉘실행');
			}
			if (getSelectedVal('cboScript').cm_execyn == 'N') {
				if ($('#txtScriptName').val().length >0) {
					$('#txtScriptName').val($('#txtScriptName').val()+',');
				}
				$('#txtScriptName').val($('#txtScriptName').val()+'실행안함[사용자선택]');
			}
			*/
			getBldScript();
		}
	});
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		close();
	});
	
	getProgInfo();
});

//최초 스크립트 데이타 조회
function getProgInfo() {
	$('#txtSysMsg').val('');
	$('#txtRsrcnName').val('');
	$('#txtDirPath').val('');
	
	data = new Object();
	data = {
		ItemId		: pItemId,
		requestType : 'getProgInfo'
	}
	ajaxAsync('/webPage/ecmd/Cmd1400Servlet', data, 'json',successGetProgInfo);
}
//최초 스크립트 데이타 조회완료
function successGetProgInfo(data) {
	baseData = data;
	if (baseData.length > 0) {
		$('#txtSysMsg').val(baseData[0].cm_sysmsg);
		$('#txtRsrcnName').val(baseData[0].cr_rsrcname);
		$('#txtDirPath').val(baseData[0].cm_dirpath);
		
		//실행구분콤보 가져오기
		data = new Object();
		data = {
			SysCd		: baseData[0].cr_syscd,
			RsrcCd		: baseData[0].cr_rsrccd,
			JobCd		: baseData[0].cr_jobcd,
			QryCd		: pReqCd,
			ItemId		: pItemId,
			AcptNo		: pReqNo,
			requestType : 'getBldList'
		}
		ajaxAsync('/webPage/ecmd/Cmd1400Servlet', data, 'json',successGetBldList);
	}
}

//실행구분콤보 가져오기 완료
function successGetBldList(data){
	cboScriptData = data;
	
	options = [];
	var i = 0;
	$.each(cboScriptData,function(key,value) {
	    options.push({ value: i, text: value.cm_codename, 
	    	 		   cm_runpos: value.cm_runpos, cm_totyn: value.cm_totyn, cm_seqyn:value.cm_seqyn, 
	    	 		   cm_execyn:value.cm_execyn,  cm_bldcd:value.cm_bldcd,  cm_prcsys:value.cm_prcsys, cm_micode: value.cm_micode });
	    i++;
	});
	$('[data-ax5select="cboScript"]').ax5select({
		options: options
	});
	
	if (cboScriptData.length > 0) {
		$('#cboScript').trigger('change');
	}
}

//스크립트 정보 가져오기
function getBldScript() {
	data = new Object();
	data = {
		AcptNo		: pReqNo,
		SysCd		: baseData[0].cr_syscd,
		RsrcCd		: baseData[0].cr_rsrccd,
		JobCd		: baseData[0].cr_jobcd,
		RsrcName	: baseData[0].cr_rsrcname,
		DirPath		: baseData[0].cm_dirpath,
		BldGbn		: getSelectedVal('cboScript').cm_micode,
		BldCd		: getSelectedVal('cboScript').cm_bldcd,
		ItemId		: pItemId,
		PrcSys		: getSelectedVal('cboScript').cm_prcsys,
		requestType : 'getBldScript'
	}
	ajaxAsync('/webPage/ecmd/Cmd1400Servlet', data, 'json',successGetBldScript);
}

//스크립트리스트 정보 가져오기완료
function successGetBldScript(data) {
	scriptGridData = data;
	scriptGrid.setData(scriptGridData);
}