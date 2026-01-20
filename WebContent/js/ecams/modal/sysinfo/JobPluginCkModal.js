/**
 * 예외업무등록 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 방지연
 * 	버전 		: 1.0
 *  수정일 	: 2022-08-00
 * 
 */
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var selSysCd 	= window.parent.selSysCd

var firstGrid 		= new ax5.ui.grid();
var firstGridData 	= [];

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: false, 
    multiSort: false,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_jobcd", 	label: "업무코드",  	width: '50%', align: 'left'},
        {key: "cm_jobname", label: "업무명", 		width: '50%', align: 'left'}
    ]
});

$(document).ready(function(){
	
	getJobInfo();
	
	// 조회
	$('#btnSearch').bind('click',function() {
		getJobInfo();
	});
	
	// 등록
	$('#btnReq').bind('click',function() {
		setJobPlugin();
	});
	
	// 닫기
	$('#btnClose').bind('click',function() {
		window.parent.jobPluginCkModal.close();
	});
});

function popClose(){
	window.parent.jobPluginCkModal.close();
}

function getJobInfo() {
	$('[data-ax5grid="firstGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	var systemInfoDta = new Object(); 
	systemInfoDta = {
		SysCd : selSysCd,
		requestType	: 'getJobInfo2'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', systemInfoDta, 'json',successGetJobInfo);
}

function successGetJobInfo(data) {
	$(".loding-div").remove();
	
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			firstGridData = [];
			firstGrid.setData([]);
			return;
		}
	}
	
	firstGridData = data;
	$(firstGridData).each(function(){
		if(this.cm_prjnmck == "1"){
			this.__selected__ = true;
		}
	});
	
	firstGrid.setData(firstGridData);
}

function setJobPlugin() {
	var firstGridData = firstGrid.getList();
	
	$(firstGridData).each(function(){
		this.jobcd = this.cm_jobcd;
		this.syscd = selSysCd;
		if(this.__selected__){
			this.prjnmck = "true";
		} else {
			this.prjnmck = "false";
		}
	});
	
	var systemInfoDta = new Object(); 
	systemInfoDta = {
		getCmd0200List : firstGridData,
		requestType	: 'cmd0200_Excption'
	}
	console.log('[cmd0200_Excption] ==>', systemInfoDta);
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', systemInfoDta, 'json',successSetJobPlugin);
}

function successSetJobPlugin(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			firstGridData = [];
			firstGrid.setData([]);
			return;
		}
	}
	
	if(data == '0') {
		dialog.alert('예외업무 등록/해제 완료',function(){
			getJobInfo();
		});
	}else {
		dialog.alert('등록 실패.',function(){});
	}
}