var UserId 		= window.parent.userId;			//접속자 ID
var reqCd 		= window.parent.reqCd;			//접속자 ID
var sysCd 		= window.parent.sysCd;
var jobCd		= window.parent.jobCd;

var mainGrid	= new ax5.ui.grid();

var acptNo = "";
var popType = "";
var reqpath = "";
var svrIp = "";
var svrPort = "";
var mainGrid_dp_len = 0;
var trancnt = 0;
var errcnt = 0;
var acptcnt = 0;
var parentvar = null;
var progFile_dp = [];
var mainGrid_dp = [];
var mainGridFilter_dp = [];
var preFileName = "";

var szLocal = "";
var szRemote = "";
var tmpPath = "";

mainGrid.setConfig({
    target: $('[data-ax5grid="mainGrid"]'),
    sortable: false, 
    showLineNumber : false,
    multiSort: false,
    showRowSelector : false,
    header: {
        align: "center",
        columnHeight: 30
    },
    paging : false,
    body: {
        columnHeight: 25,
        onClick: function () {
        //	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {
    		if (this.item.errflag != '0') {
				return "fontStyle-module";
    		}
    	},
    	onDataChanged: function(){}
    },
    columns: [
        {key: "viewpcdir", 		label: "로컬경로",  	width: '50%', align: "left"},
        {key: "cr_rsrcname",	label: "파일명",  	width: '30%'},
        {key: "error", 			label: "결과",  		width: '20%'},
    ]
});

$(document).ready(function() {
	parentvar = window.parent.progFileList;

//	progFile_dp = parentvar.file;
	progFile_dp = parentvar;
	
	if(parentvar == null || progFile_dp.length < 1){
		dialog.alert("파일이 없습니다.", function(){
			popClose();
		});
	}
//	isInstalledActiveX();
	
	// 닫기 클릭
	$('#btnClose').bind('click', function() {
		window.parent.modalCloseFlag = false;
		popClose();
	});

	getTmpDirConf();
});

function popClose() {
	window.parent.fileDownModal.close();
}

function getTmpDirConf() {
	var tmpData = {
		pCode : "99",
		requestType  : 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpData, 'json',successGetTmpDir);
}

function successGetTmpDir(data) {
	
	tmpPath = data;
	
	mainGrid_dp = progFile_dp;
	mainGrid_dp_len = mainGrid_dp.length;
	

	for (var i=0;i<mainGrid_dp_len;i++){
		mainGrid_dp[i].sendflag = "0";
		mainGrid_dp[i].errflag = "0";
	}
	//mainGrid_dp.refresh();
//	mainGrid.setData(mainGrid_dp);
	fileListDown();

}

function fileListDown() {
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	setTimeout(function() {
		var ajaxResult = null;
		var tmpData = {
				userId	: userId,
				sysCd	: sysCd,
				fileList : progFile_dp,
				requestType  : 'fileListDown'
		}
		
		
	ajaxResult = ajaxCallWithJson('/webPage/ecmd/Cmd1500Servlet', tmpData, 'json');
		
		if(ajaxResult == "0") {
			readFilesendResult();
		} else {
			dialog.alert(ajaxResult);
			$(".loding-div").remove();
			return;
		}
		
	}, 100);
} 

function readFilesendResult() {
	var tmpData = {
			userId	: userId,
		requestType  : 'readFilesendResult'
	}
	ajaxAsync('/webPage/ecmd/Cmd1500Servlet', tmpData, 'json', successReadFilesendResult);
}

function successReadFilesendResult(data) {
	$(".loding-div").remove();
	var errFlag = false;
	if(data != null && data.length > 0) {
		for(i in data) {
			for(j in mainGrid_dp) {
				if(data[i].itemId == mainGrid_dp[j].cr_itemid) {
					mainGrid_dp[j].sendflag = "1";
					if(data[i].resultCode != "0000") {
						mainGrid_dp[j].errflag = "1";
						errFlag = true;
					}
					mainGrid_dp[j].error = data[i].resultMsg;
				}
			}
		}
		mainGrid.setData(mainGrid_dp);
		if(errFlag) {
			dialog.alert("다운로드에 실패한 파일이 있습니다.");
		} else {
			dialog.alert("다운로드가 정상적으로 완료되었습니다.");
		}
	} else {
		dialog.alert("다운로드할 파일이 없습니다.");
		return;
	}
}
