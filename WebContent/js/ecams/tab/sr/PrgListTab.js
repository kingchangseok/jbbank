/** 프로그램목록(eCmc0930_tab.mxml) 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이성현
 * 	버전 : 1.1
 *  수정일 : 2019-07-24
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.parent.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var reqCd	 		= window.parent.reqCd; 
var strAcptNo 		= window.parent.strAcptNo;
var strStatus		= window.parent.strStatus;
var strIsrId		= window.parent.strIsrId;

var PrgListGrid 	= new ax5.ui.grid();

var cboEditorData 	= [];
var PrgListGridData = [];
var srData			= {};
var myWin			= null;
var completeReadyFunc = false;

$('[data-ax5select="cboEditor"]').ax5select({
    options: []
});

$(document).ready(function() {
	createViewGrid();
	
	// 개발자 변경
	$('#cboEditor').bind('change', function() {
		cboEditor_Change();
	});
	
	// 소스보기
	$('#btnView').bind('click', function() {
		btnSrc_Click('V');
	});
	
	// 소스비교
	$('#btnDiff').bind('click', function() {
		btnSrc_Click('D');
	});
	
	// 엑셀 저장
	$('#btnExcel').bind('click', function() {
		PrgListGrid.exportExcel('프로그램목록.xls');
	});
	getData();
	
	completeReadyFunc = true;
});

//그리드 생성
function createViewGrid() {
	PrgListGrid.setConfig({
	    target: $('[data-ax5grid="PrgListGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    multipleSelect: false,
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
	
		       	var selIn = PrgListGrid.selectedDataIndexs;
		       	if(selIn.length === 0) return;
		       	//openWindow(1, '', PrgListGridData[selIn].cr_itemid);
		       	openWindow(1, this.item.cr_acptno,'');
	        },
	        onDataChanged: function(){
	    		this.self.repaint();
	    	},
	    },
	     contextMenu: {
	         iconWidth: 20,
	         acceleratorWidth: 100,
	         itemClickAndClose: false,
	         icons: {
	             'arrow': '<i class="fa fa-caret-right"></i>'
	         },
	         items: [
	             {type: 1, label: "변경신청상세"}
	         ],
	         popupFilter: function (item, param) {
	         	/** 
	         	 * return 값에 따른 context menu filter
	         	 * 
	         	 * return true; -> 모든 context menu 보기
	         	 * return item.type == 1; --> type이 1인 context menu만 보기
	         	 * return item.type == 1 | item.type == 2; --> type 이 1,2인 context menu만 보기
	         	 * 
	         	 * ex)
		            	if(param.item.qrycd2 === '01'){
		            		return item.type == 1 | item.type == 2;
		            	}
	         	 */
	        	 PrgListGrid.clearSelect();
	        	 PrgListGrid.select(Number(param.dindex));

	        	 return true;
	         },
	         onClick: function (item, param) {
	        	 openWindow(item.type, param.item.cr_acptno,'');
	        	 PrgListGrid.contextMenu.close();//또는 return true;
			}
		},
	    columns: [
	        {key: "qrycd", 		label: "신청구분",		width: '10%', align: "left"},
	        {key: "cm_sysmsg", 	label: "시스템",		width: '10%', align: "left"},
	        {key: "cm_dirpath", label: "프로그램경로",	width: '20%', align: "left"},
	        {key: "cr_rsrcname",label: "프로그램명",  	width: '15%', align: "left"},
	        {key: "status", 	label: "상태",  		width: '10%', align: "left"},
	        {key: "rsrccd", 	label: "프로그램유형",	width: '10%'},
	        {key: "lastdate", 	label: "최종변경일",  	width: '10%'},
	        {key: "cr_story", 	label: "프로그램설명",	width: '15%', align: "left"},
	    ]
	});
	
	if(PrgListGridData.length > 0){
		PrgListGrid.setData(PrgListGridData);
	}
}

function getData() {
	if (window.parent.srData != null && window.parent.srData != '' && window.parent.srData != undefined) {
		srData = window.parent.srData;
		strIsrId = window.parent.srData.strIsrId;
		strStatus = window.parent.srData.strStatus;
	}
}

function screenInit() {
	getData();
	PrgListGridData = [];
	PrgListGrid.setData([]);
}

function docListCall() {
	var data = {
		cc_srid : strIsrId,
		reqCD : reqCd,
		userID : userId,
		requestType : 'getScmuserList'
	}
	ajaxAsync('/webPage/ecmc/Cmc0300Servlet', data, 'json', successGetScmuserList);
}

function successGetScmuserList(data) {
	cboEditorData = data;
	$('[data-ax5select="cboEditor"]').ax5select({
        options: injectCboDataToArr(cboEditorData, "cc_scmuser", "scmuser")
	});
	
	if(cboEditorData.length > 0) {
		for(var i=0; i<cboEditorData.length; i++) {
			if(cboEditorData[i].cc_scmuser == userId) {
				$('[data-ax5select="cboEditor"]').ax5select('setValue', userId, true);
				break;
			}
		}
		
		if(getSelectedIndex('cboEditor') < 1 && cboEditorData.length == 2) {
			$('[data-ax5select="cboEditor"]').ax5select('setValue', cboEditorData[1].cc_scmuser, true);
		}
		
		if(getSelectedIndex('cboEditor') > 0) cboEditor_Change();
	}
}

function cboEditor_Change() {
	PrgListGridData = [];
	PrgListGrid.setData([]);
	
	$('#btnExcel').prop('disabled', true);
	
	if(getSelectedIndex('cboEditor') < 1) return;
	
	var data = {
		IsrId : strIsrId,
		UserId : getSelectedVal('cboEditor').cc_scmuser,
		qryGbn : 'P',
		requestType : 'getProgHist'
	}
	
	$('[data-ax5grid="PrgListGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/common/PrjInfoServlet', data, 'json', successGetProgHist);
}

function successGetProgHist(data) {
	$(".loding-div").remove();
	
	PrgListGridData = data;
	PrgListGrid.setData(PrgListGridData);
	PrgListGrid.setConfig();
	PrgListGrid.repaint();
	
	if(PrgListGridData.length > 0) $('#btnExcel').prop('disabled', false);
	else $('#btnExcel').prop('disabled', true);
}

function btnSrc_Click(gbn) {
	var selItem = PrgListGrid.getList('selected');
	
	if(selItem == null || selItem == undefined || selItem.length == 0) {
		dialog.alert('선택한 프로그램 정보가 없습니다.\n프로그램을 선택해 주세요.');
		return;
	}
	
	if(selItem[0].cr_itemid != null && selItem[0].cr_itemid != undefined && selItem[0].cr_itemid != '') {
		if(gbn == 'V') {
			openWindow('R53', '', selItem[0].cr_itemid);
		}else if(gbn == 'D') {
			openWindow('R52', '', selItem[0].cr_itemid);
		}
	}
}

function openWindow(type, acptNo, etcInfo) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_pop_'+reqCd;

    if(type == 1) {
    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
    } else if (type === 'R52') {
    	cURL = "/webPage/winpop/PopSourceDiff.jsp";
	} else if(type === 'R53') {
		cURL = "/webPage/winpop/PopSourceView.jsp";
	} else {
		confirmDialog2.alert('window open - popup: invalid type ['+type+'] error', function(){return;});
	}
	
	var f = document.setReqData;
    f.user.value 	= userId;
    
	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	if (acptNo != '' && acptNo != null) {
		f.acptno.value	= acptNo;
	}
	
	if (etcInfo != '' && etcInfo != null) {
		f.itemid.value = etcInfo;
	}
    
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}