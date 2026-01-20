/**
 * 시스템정보 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-05-16
 */

var userName 	= window.top.userName;
var userId 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;

var sysInfoGrid		= new ax5.ui.grid();

var sysInfoGridData = []; // 	시스템 그리드
var sysInfoData 	= [];

var relModal 		= new ax5.ui.modal();
var sysDetailModal 	= new ax5.ui.modal();
var sysCopyModal 	= new ax5.ui.modal();
var sysInfoModal	= new ax5.ui.modal();

var selectedSystem 	= null;
var selectedGbnCd 	= null;

sysInfoGrid.setConfig({
    target: $('[data-ax5grid="sysInfoGrid"]'),
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
         onDBLClick: function () {
         	if (this.dindex < 0) return;
         	
        	sysInfoGrid_DblClick('UPDT');
        },
    	trStyleClass: function () {
    		if(this.item.closeSw === 'Y'){
    			return "fontStyle-cncl";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_syscd", 		label: "시스템코드",	    width: '10%'},
        {key: "cm_sysmsg", 		label: "시스템명",  	    width: '20%', align: 'left'},
        {key: "sysgb", 		    label: "시스템유형",  	    width: '10%', align: 'left'},
        {key: "scmopen",     	label: "형상관리오픈",    	width: '10%'},
        {key: "sysopen",     	label: "시스템오픈",    	width: '10%'},
        {key: "dirbname",     	label: "디렉토리기준",    	width: '10%'},
        {key: "sysinfo",     	label: "시스템속성",    	width: '30%', align: 'left'},
    ]
});

$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
var loading_div = "<div class='loding-div' style='display:none;'><div class='loding-img'><img alt='' src='/img/loading_gird.gif'></div></div>";

$(document).ready(function(){
	if (userId == null || userId == '') {
		dialog.alert('로그인 후 사용하여 주시기 바랍니다.');
		return;
	}
	
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	//폐기포함 클릭시
	$("#chkCls").bind('click', function() {
		sysInfoFilter();
	});
	
	// 시스템 코드/ 시스템명 찾기
	$('#txtFindSys').bind('keypress', function(event) {
		if(event.keyCode === 13 ) {
			sysInfoFilter();
		}
	});
	
	// 조회 클릭시
	$('#btnQry').bind('click', function() {
		getSysInfoList($('#txtFindSys').val().trim());
	});
	
	$("#btnExecl").bind("click", function(){
		sysInfoGrid.exportExcel("시스템정보.xls");
	});
	
	// 처리팩터추가 버튼 클릭시
	$('#btnFact').bind('click',function() {
		var factUpInfoData;
		factUpInfoData = new Object();
		factUpInfoData = {
			requestType	: 'factUpdt'
		}
		ajaxAsync('/webPage/ecmm/Cmm0200Servlet', factUpInfoData, 'json',successFactUpdt);
	});
	
	// 시스템정보복사
	$('#btnCopy').bind('click', function() {
		setTimeout(function() {			
			sysCopyModal.open({
				width: 1024,
				height: 768,
				defaultSize: true,
				iframe: {
					method: "get",
					url: "../modal/sysinfo/SysCopyModal.jsp"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						$('#btnQry').trigger('click');
					}
				}
			}, function () {
			});
		}, 200);
	});	
	
	// 시스템신규버튼 클릭시
	$('#btnReq').bind('click', function() {
		setTimeout(function() {			
			sysInfoGrid_DblClick('OPEN');
		}, 200);
	});
	
    $('#btnQry').trigger('click');
});

//	처리팩터 추가
function successFactUpdt(data) {
	if(data === 'OK') dialog.alert('시스템속성/서버속성/프로그램종류속성 자릿수를 일치시켰습니다.',function(){});
	else dialog.alert('처리팩터추가중 오류가 발생했습니다. 관리자에게 문의하세요.',function(){});
}

// 시스템 리스트
function getSysInfoList(sysCd) {
	var sysClsSw 	= $('#chkCls').is(':checked');
	var sysListInfo	= new Object();
	
	var sysListInfoData = new Object();
	sysListInfoData = {
		clsSw : sysClsSw ? true : false,
		SysCd : '',
		requestType	: 'getSysInfo_List'
	}
	$('[data-ax5grid="sysInfoGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', sysListInfoData, 'json',successGetSysInfoList);
	sysListInfoData = null;
	sysListInfo = null;
}

//시스템 리스트
function successGetSysInfoList(data) {
	$(".loding-div").remove();
	sysInfoData = data;
	sysInfoFilter();
}

function sysInfoFilter() {
	var sysClsSw = $('#chkCls').is(':checked');
	var sysCd = $('#txtFindSys').val().trim();
	
	sysInfoGrid.setData([]);
	sysInfoGridData = [];
	sysInfoGridData = sysInfoData;
	sysInfoGridData = sysInfoGridData.filter(function(sysInfoData) {
		if (sysCd != null && sysCd.length>0) {
			if (sysInfoData.cm_syscd == sysCd || sysInfoData.cm_sysmsg.toUpperCase().indexOf(sysCd.toUpperCase())>=0) {
				if (!sysClsSw && sysInfoData.closeSw == 'Y') return false;
				else return true;
			}
		} else {
			if (!sysClsSw && sysInfoData.closeSw == 'Y') return false;
			else return true;
		}
	});
	sysCd = null;
	sysInfoGrid.setData(sysInfoGridData);
}

function sysInfoGrid_DblClick(gbnCd) {
	
	if (gbnCd != null && gbnCd == 'OPEN') {
		selectedSystem = null;
	} else {
		var gridSelectedIndex 	= sysInfoGrid.selectedDataIndexs;
		if(gridSelectedIndex.length === 0 ) {
			dialog.alert('시스템을 선택한 후 진행하여 주시기 바랍니다.',function(){});
			return;
		}
		selectedSystem = sysInfoGrid.list[gridSelectedIndex];
	}
	selectedGbnCd = gbnCd;
	sysDetailModal.open({
        width: 1100,
        height: 850,
        defaultSize : true,
        iframe: {
            method: "get",
            url: "../modal/sysinfo/SysDetailModal.jsp"
        },
        onStateChanged: function () {
            if (this.state === "open") {
                mask.open();
            }
            else if (this.state === "close") {
            	sysDetailModal.close();
                //mask.close();
                
                _promise(10,mask.close())
    			.then(function() {
    				return _promise(10, $('#btnQry').trigger('click'));
    			});
            }
        }
    }, function () {
    });
}
