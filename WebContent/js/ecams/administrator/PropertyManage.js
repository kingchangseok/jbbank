/**
 * 프로퍼티관리 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-27
 * 
 */

var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드


var proGrid	= new ax5.ui.grid();

var proGridData		= [];
var cboCodeData		= [];
var dbConnData		= [];

proGrid.setConfig({
    target: $('[data-ax5grid="proGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {
    		if(this.item.status === 'ER'){
    			return "text-danger";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "gbn", 	label: "Properties Item",  		width: '20%', align: "left"},
        {key: "value",	label: "Properties Values",  	width: '80%', align: "left", editor: {type: "text"}},
    ]
});

$('[data-ax5select="cboCode"]').ax5select({
    options: []
});


$(document).ready(function() {
	$('#btnSave').prop('disabled', true);
	getCodeInfo();
	getSvrProperties();
	
	// 구분 변경
	$('#cboCode').bind('change', function() {
		getProperties();
	});
	// WEB 프로퍼티 저장
	$('#btnSave').bind('click', function() {
		saveProperties();
	});
	// 서버 프로퍼티 저장
	$('#btnSvrSave').bind('click', function() {
		saveSvrProperties();
	});
});

// 서버 프로퍼티 저장
function saveSvrProperties() {
	var txtConn = $('#txtConn').val().trim();
	var txtUser = $('#txtUser').val().trim();
	var txtPass = $('#txtPass').val().trim();
	if(txtConn.length === 0 || txtUser.length === 0 || txtPass.length === 0) {
		dialog.alert('모든 서버 정보를 입력하여 주시기 바랍니다.', function() {});
		return;
	}
	
	var data = new Object();
	data = {
		dbconn 		: txtConn,
		dbuser 		: txtUser,
		dbpass 		: txtPass,
		UserId		: userId,
		requestType	: 'setSvrProperties'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successSaveSvrProperties);
}

// 서버 프로퍼티 저장 완료
function successSaveSvrProperties(data) {
	dialog.alert('저장을 완료하였습니다.', function() {});
	$('#cboCode').trigger('change');
}

// WEB 프로퍼티 저장
function saveProperties() {
	
	for(var i=0; i<proGridData.length; i++) {
		var item = proGridData[i];
		if(item.value.trim() === '') {
			dialog.alert('모든 속성값을 입력하여 주시기바랍니다.', function() {});
			return;
		}
		
		if(item.value.trim().indexOf('****') >= 0) {
			dialog.alert('속성값을 정확히 입력해 주시기바랍니다.', function() {});
			return;
		}
	}
	
	var data = new Object();
	data = {
		infoList 	: proGridData,
		UserId 		: userId,
		requestType	: 'setProperties'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successSaveProperties);
}

// WEB 프로퍼티 저장 완료
function successSaveProperties(data) {
	dialog.alert('저장을 완료하였습니다.', function() {});
	$('#cboCode').trigger('change');
}

// 서버 프로퍼티 정보 가져오기
function getSvrProperties() {
	var data = new Object();
	data = {
		requestType	: 'getSvrProperties'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetSvrProperties);
}

// 서버 프로퍼티 정보 가져오기 완료
function successGetSvrProperties(data) {
	dbConnData = data;
	
	if(data == undefined || data == null){
		$("#serverProperty").hide();
	}
	
	if(dbConnData != null && dbConnData.length > 0 ) { 
		
		$(dbConnData).each(function(){
			if(this.gbn == "DBCONN"){
				$('#txtConn').val(this.value);
			} else if (this.gbn == "DBUSER"){
				$('#txtUser').val(this.value);
			} else if (this.gbn == "DBPASS"){
				$('#txtPass').val(this.value);
			}
		});
		
	}
	
	/*dbConnData.forEach(function(item,index){
		if(item.gbn === 'DBCONN') {
			$('#txtConn').val(item.value);
		}
		if(item.gbn === 'DBUSER') {
			$('#txtUser').val(item.value);		
		}
		if(item.gbn === 'DBPASS') {
			$('#txtPass').val(item.value);
		}
	});*/
}

// 구분에 따른 프로퍼티정보 가져오기
function getProperties() {
	var data = new Object();
	data = {
		strGbn 		: getSelectedVal('cboCode').value,
		requestType	: 'getProperties'
	}
	ajaxAsync('/webPage/ecmm/Cmm0700Servlet', data, 'json',successGetProperties);
}

// 구분에 따른 프로퍼티정보 가져오기 완료
function successGetProperties(data) {
	proGridData = data;
	proGrid.setData(proGridData);
	if(proGridData.length > 0 ) {
		$('#btnSave').prop('disabled', false);
	} else {
		$('#btnSave').prop('disabled', true);
	}
}

// 구분 가져오기
function getCodeInfo() {
	var codeInfos = getCodeInfoCommon( [new CodeInfo('PROPERTIES','','N')] );
	cboCodeData = codeInfos.PROPERTIES;
	
	$('[data-ax5select="cboCode"]').ax5select({
      options: injectCboDataToArr(cboCodeData, 'cm_micode' , 'cm_codename')
	});
	
	if(cboCodeData.length > 0 ) {
		for(var i=0; i<cboCodeData.length; i++) {
			if(cboCodeData[i].cm_micode == 'O') {
				$('[data-ax5select="cboCode"]').ax5select('setValue', cboCodeData[i].cm_micode, true);
				break;
			}
		}
		getProperties();
	}
}