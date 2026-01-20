var firstGrid 		= new ax5.ui.grid();
var firstGridData 	= [];

var strUserId 		= window.parent.localSyncResultData.strUserId;
var strSysCd		= window.parent.localSyncResultData.strSysCd;
var localDir 		= window.parent.localSyncResultData.localDir;

var resultEnd		= false;
var syncCnt 		= '0';
var TimerSw			= false;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    showLineNumber: true,
    showRowSelector: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	if (this.dindex < 0) return;

	       	var selIn = retGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
	    	if (this.item.cr_rstfile == null || this.item.cr_rstfile == "") return;			
	    	
	       	getResultLog(this.item.cr_rstfile);
        },
    	trStyleClass: function () {
    		if (this.item.selected == '1'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    	    this.self.repaint();
    	}
    },
    contextMenu: {},
    columns: [
        {key: "syncRst",      	label: "동기화결과",   width: '25%'},
        {key: "filename",    	label: "파일명",  	width: '25%', align: 'left'},
        {key: "param_dirpath", 	label: "디렉토리",  	width: '40%', align: 'left'}
    ]
});

$(document).ready(function(){
	console.log('window.parent.reqArr ==>',window.parent.reqArr);
	firstGridData = window.parent.reqArr;
	firstGrid.setData(firstGridData);
	firstGrid.clearSelect();
	
	execSync();
	
	//닫기 클릭
	$('#btnClose').bind('click', function() {
		if( $('#btnRefresh').prop('disabled') ) {
			TimerSw = true;
			cmdQry_a_Click();
		}
		
		if (!resultEnd) {
			confirmDialog.confirm({
				msg: '동기화 작업이 종료되지않았습니다.\n작업을 중단하고 창을 닫으시겠습니까?',
			}, function(){
				if(this.key === 'ok') {
					//Cmr4100_2.getLogView(strUserId, strSysCd, "S", grdLst_dp.toArray(), "");
					var tmpData = {
						userId : strUserId,
						sysCd : strSysCd,
						gbn	: 'S',
						syncArr	: firstGrid.getList(),
						syncCnt : '',
						requestType	: 'getLogView'
					}
					console.log('[getLogView] ==>', tmpData);
					var ajaxReturnData = ajaxCallWithJson('/webPage/ecmr/Cmr4100Servlet', tmpData, 'json');
					popClose(resultEnd);
				}
			});
			
		} else {
			popClose(resultEnd);
		}
	});
	
	//새로고침 클릭
	$('#btnRefresh').bind('click', function() {
		refresh_click();
	});
	
	//자동고침 클릭
	$('#cmdQry_a').bind('click', function() {
		cmdQry_a_Click();
	});
});

function popClose(flag){
	window.parent.localSyncResultFlag = flag;
	window.parent.localSyncResultModal.close();
}

//Cmr4100.execSync(strSysCd, strUserId, localDir, grdLst_dp.toArray());
function execSync() {
	var tmpData = {
		sysCd : strSysCd,
		userId : strUserId,
		homeDir : localDir,
		syncArr	: firstGrid.getList(),
		requestType	: 'execSync'
	}
	console.log('[execSync] ==>', tmpData);
	ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpData, 'json', successExecSync);
}

var inter 	 = null;		//inter val
var timer 	 = 0;   		//세션만료 timer
var timerSw1 = 0;

function successExecSync(data) {
	syncCnt = data;
	
	TimerSw = false;
	
	if(firstGrid.getList().length > 5000) {
		timerSw1 = 0;
		$('#cmdQry_a').text('자동고침');
		$('#btnRefresh').prop('disabled',false);
    	$('#cmdQry_a').prop('disabled',true);
		$('#lblCnt').text('');
	}
	else if(firstGrid.getList().length > 3000) {
		timerSw1 = 20000;
		$('#lblCnt').text('(20초)');
	}
	else if(firstGrid.getList().length > 1000) {
		timerSw1 = 15000;
		$('#lblCnt').text('(15초)');
	}
	else if(firstGrid.getList().length > 500) {
		timerSw1 = 10000;
		$('#lblCnt').text('(10초)');
	}
	else if(firstGrid.getList().length > 100) {
		timerSw1 = 5000;
		$('#lblCnt').text('(5초)');
	}
	else {
		timerSw1 = 3000;
		$('#lblCnt').text('(3초)');
	}
	
	cmdQry_a_Click();
}

function cmdQry_a_Click() {
	if(firstGrid.getList().length > 5000) {
		timerSw1 = 0;
		$('#cmdQry_a').text('자동고침');
		$('#btnRefresh').prop('disabled',false);
    	$('#cmdQry_a').prop('disabled',true);
		$('#lblCnt').text('');
		
		timer = 0;
		clearInterval(inter);//STOP
		inter = null;
	}else {
		if(!TimerSw) {
			$('#cmdQry_a').text('자동고침중');
			TimerSw = true;
			
			if(firstGrid.getList().length > 3000) {
				$('#lblCnt').text('(20초)');
			}
			else if(firstGrid.getList().length > 1000) {
				$('#lblCnt').text('(15초)');
			}
			else if(firstGrid.getList().length > 500) {
				$('#lblCnt').text('(10초)');
			}
			else if(firstGrid.getList().length > 100) {
				$('#lblCnt').text('(5초)');
			}
			else {
				$('#lblCnt').text('(3초)');
			}
			
			inter = setInterval(TimerStart, timerSw1);
			
		}else {
			console.log('자동새로고침 정지');
			
			$('#cmdQry_a').text('자동고침');
			TimerSw = false;
			timer = 0;
			clearInterval(inter);//STOP
			inter = null;
			$('#btnRefresh').prop('disabled',false);
			$('#lblCnt').text('');
		}
	}
}

function TimerStart() {
	$('#btnRefresh').prop('disabled',true);
	refresh_click();
}

function refresh_click() {
	$('#txtLogView').val('');
	
	//Cmr4100.getLogView(strUserId, strSysCd, "V", grdLst_dp.toArray(), syncCnt);
	var tmpData = {
		userId : strUserId,
		sysCd : strSysCd,
		gbn	: 'V',
		syncArr	: firstGrid.getList(),
		syncCnt : syncCnt,
		requestType	: 'getLogView'
	}
	console.log('[getLogView] ==>', tmpData);
	ajaxAsync('/webPage/ecmr/Cmr4100Servlet', tmpData, 'json', successGetLogView);
}

function successGetLogView(data) {
	if (typeof data == 'string' && data.indexOf('ERROR') > -1) {
		$('#txtLogView').val(data.substr(5));
		return;
	}
	
	var endSw = data.EndSw;
	$('#txtLogView').val(data.LogMsg);
	
	var selIdx = 0;
	var cnt = 0;
	
	if(data.SyncResult != null && data.SyncResult != undefined && data.SyncResult != '') {
		var syncList = data.SyncResult;
		
		for(var i=0; i<firstGridData.length; i++) {
			for(var j=0; j<syncList.length; j++) {
				if(firstGridData[i].cr_itemid == syncList[j].cr_itemid) {
					firstGridData[i].selected = syncList[j].selected;
					firstGridData[i].syncRst = syncList[j].syncRst;
				}
				
				if(syncList[j].syncRst != null && syncList[j].syncRst != undefined && syncList[j].syncRst != '') {
					selIdx = i;
				}
				
				if (syncList[j].selected == "1") cnt++;
				break;
			}
		}
	}
	
	firstGrid.setData(firstGridData);
	if (cnt == firstGridData.length) {
		dialog.alert("동기화가 작업이 완료되었습니다.");
		resultEnd = true;
		TimerSw = true;
		cmdQry_a_Click();
		return;
	}
	
	if (endSw == "1") {
		dialog.alert("오류가 발생하여 동기화작업이 중단되었습니다.");
		resultEnd = true;
		TimerSw = true;
		cmdQry_a_Click();
	}
}