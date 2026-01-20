/**
 * 업무등록 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-29
 * 
 */
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var selSysCd 	= window.parent.selSysCd;

var jobGrid 		= new ax5.ui.grid();

var jobGridData = [];

jobGrid.setConfig({
    target: $('[data-ax5grid="jobGrid"]'),
    sortable: true, 
    multiSort: false,
    showRowSelector: false,
    multipleSelect: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.clearSelect();
            this.self.select(this.dindex);
            
            if (this.dindex<0) return;
            $('#txtPrjName').val('');
            $('#txtPrjId').val('');
            $('#txtBranch').val('');
            $('#txtPrjHome').val('');
            $('#txtJobCd').val(this.item.cm_jobcd);
            $('#txtJobName').val(this.item.cm_jobname);
            if (this.item.cm_prjname != null && this.item.cm_prjname != '' && this.item.cm_prjname != undefined) $('#txtPrjName').val(this.item.cm_prjname);
            if (this.item.cm_prjid != null && this.item.cm_prjid != '' && this.item.cm_prjid != undefined) $('#txtPrjId').val(this.item.cm_prjid);
            if (this.item.cm_branch != null && this.item.cm_branch != '' && this.item.cm_branch != undefined) $('#txtBranch').val(this.item.cm_branch);
            if (this.item.cm_prjhome != null && this.item.cm_prjhome != '' && this.item.cm_prjhome != undefined) $('#txtPrjHome').val(this.item.cm_prjhome);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_jobcd", 	label: "업무코드",  	     width: '8%'},
        {key: "cm_jobname", label: "업무명",  	     width: '25%', align: 'left'},
        {key: "cm_prjname", label: "프로젝트명",  	     width: '25%', align: 'left'},
        {key: "cm_prjid",   label: "프로젝트ID",     	 width: '10%'},
        {key: "cm_branch",  label: "Branch",         width: '8%', align: 'left'},
        {key: "cm_prjhome", label: "프로젝트홈",        width: '24%', align: 'left'},
    ]
});


function popClose(){
	window.parent.gitJobModal.close();
}

$(document).ready(function(){
	getJobList();
	
	// 편집
	$('#btnReq').bind('click',function() {
		if ($('#txtJobCd').val().length == 0) {
			dialog.alert('업무를 선택하여 주시기 바랍니다.');
			return;
		}
		$('#txtPrjName').val($('#txtPrjName').val().trim());
		$('#txtPrjId').val($('#txtPrjId').val().trim());
		$('#txtBranch').val($('#txtBranch').val().trim());
		$('#txtPrjHome').val($('#txtPrjHome').val().trim());
		
		if ($('#txtPrjName').val().length == 0) {
			dialog.alert('프로젝트명을 입력하여 주시기 바랍니다.');
			return;
		}
		if ($('#txtPrjId').val().length == 0) {
			dialog.alert('프로젝트ID를 입력하여 주시기 바랍니다.');
			return;
		}
		if ($('#txtBranch').val().length == 0) {
			dialog.alert('Default-Branch를 입력하여 주시기 바랍니다.');
			return;
		}
		if ($('#txtPrjHome').val().length == 0) {
			dialog.alert('프로젝트홈을 입력하여 주시기 바랍니다.');
			return;
		}
		
		var gitData = new Object();
		gitData.syscd = selSysCd;
		gitData.jobcd = $('#txtJobCd').val();
		gitData.prjname = $('#txtPrjName').val();
		gitData.prjid = $('#txtPrjId').val();
		gitData.branch = $('#txtBranch').val();
		gitData.prjhome = $('#txtPrjHome').val();
		
		var jobInfoData = new Object(); 
		jobInfoData = {
			    gitData     : gitData,
			requestType		: 'setGitJobInfo'
		}
		ajaxAsync('/webPage/ecmm/Cmm0200Servlet', jobInfoData, 'json',successSetGitJobInfo);
		gitData = null;
		jobInfoData = null;
	});
	
	// 닫기
	$('#btnClose').bind('click',function() {
		popClose('jobModal');
	});
});


function getJobList() {
	jobGridData = [];
	jobGrid.setData([]);
	
	var jobInfoData = new Object(); 
	jobInfoData = {
			  SysCd     : selSysCd,
		requestType		: 'getSysJobInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', jobInfoData, 'json',successgetJobList);
}

function successgetJobList(data) {
	jobGridData = data;
	jobGrid.setData(jobGridData);
}

function successSetGitJobInfo(data) {
	if (data != 'OK') {
		dialog.alert('업무별 GIT프로젝정보 연결에 실패하였습니다.');
	} else {
		dialog.alert('업무별 GIT프로젝정보를 등록하였습니다.');
		
		getJobList();
	}
}
