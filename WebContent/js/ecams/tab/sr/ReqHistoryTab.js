/** ReqHistoryTab(eCmc0920_tab.mxml) 화면 정의
 * 
 * <pre>
 * <b>History</b>
 * 	작성자: 이성현
 * 	버전 : 1.1
 *  수정일 : 2019-07-25
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.parent.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var strStatus		= window.parent.strStatus;
var strIsrId		= window.parent.strIsrId;  
var strReqCd 		= "";

var ReqListGrid 	= new ax5.ui.grid();

var cboEditorData 	= [];
var ReqListGridData = [];

var ReqListGridColumns	= null;
var completeReadyFunc	= false;

$('[data-ax5select="cboEditor"]').ax5select({
    options: []
});

$('input:radio[name^="radioAppli"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function() {
	createViewGrid();
	
	// 개발자 변경
	$('#cboEditor').bind('change', function() {
		cboEditor_Change();
	});
	
	$('input:radio[name="radioAppli"]').bind('click', function() {
		getAcptHist();
	});
	
	// 엑셀 저장
	$('#btnExcel').bind('click', function() {
		ReqListGrid.exportExcel('변경요청이력.xls');
	});
	
	completeReadyFunc = true;
});

// 그리드 생성
function createViewGrid() {
	ReqListGrid.setConfig({
	    target: $('[data-ax5grid="ReqListGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    header: {
	        align: "center"
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	        onDBLClick: function () {
	        	if(ReqListGrid.selectedDataIndexs.length < 1 ) {
	        		return;
	        	}
	        	var item = ReqListGrid.list[ReqListGrid.selectedDataIndexs];
	        	openApprovalInfo(1, item.cr_acptno, item.cr_qrycd, item.cc_srid);
	        }, 
	        onDataChanged: function(){
	    		this.self.repaint();
	    	}
	    },
	    columns: [
	        {key: "cm_sysmsg", 	label: "시스템",  	width: '10%', align: "left"},
	        {key: "qrycd", 		label: "신청구분",  	width: '10%', align: "left"},
	        {key: "acptno", 	label: "신청번호",  	width: '10%', align: "left"},
	        {key: "acptdate", 	label: "신청일",  	width: '10%', align: "center"},
	        {key: "cm_dirpath", label: "경로",  		width: '15%', align: "left"},
	        {key: "cr_rsrcname",label: "파일명",  	width: '10%', align: "left"},
	        {key: "status", 	label: "상태",  		width: '10%', align: "left"},
	        {key: "prcdate", 	label: "완료일",  	width: '10%', align: "center"},
	        {key: "passok", 	label: "처리구분",  	width: '10%', align: "left"},
	        {key: "prcreq", 	label: "적용예정일시",	width: '10%', align: "center"}
	    ]
	});
	
	if(ReqListGridData.length > 0){
		ReqListGrid.setData(ReqListGridData);
	}
}

function screenInit() {
	ReqListGridData = [];
	ReqListGrid.setData([]);
}

function docListCall() {
	var data = {
		cc_srid : strIsrId,
		reqCD : 'XX',
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
	ReqListGridData = [];
	ReqListGrid.setData([]);
	
	if(getSelectedIndex('cboEditor') < 1) return;
	
	getAcptHist();
}

function getAcptHist() {
	var data = {
		IsrId : strIsrId,
		UserId : getSelectedVal('cboEditor').cc_scmuser,
		qryGbn : $('input[name="radioAppli"]:checked').val(),
		requestType : 'getAcptHist'
	}
	
	$('[data-ax5grid="ReqListGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/common/PrjInfoServlet', data, 'json', successGetAcptHist);
}

function successGetAcptHist(data) {
	$(".loding-div").remove();
	
	ReqListGridData = data;
	ReqListGrid.setData(ReqListGridData);
	
	var checkVal = $('input[name="radioAppli"]:checked').val();
	if(checkVal == 'A') {
		ReqListGrid.updateColumn({key: "cm_dirpath",	label: "경로",  	width: '10%', align: "left", hidden: true}, 4);
		ReqListGrid.updateColumn({key: "cr_rsrcname", 	label: "파일명",	width: '10%', align: "left", hidden: true}, 5);
	}else {
		ReqListGrid.updateColumn({key: "cm_dirpath",	label: "경로",  	width: '10%', align: "left", hidden: false}, 4);
		ReqListGrid.updateColumn({key: "cr_rsrcname", 	label: "파일명",	width: '10%', align: "left", hidden: false}, 5);
	}
	
	ReqListGrid.setConfig();
	ReqListGrid.repaint();
	
	if(ReqListGridData.length > 0) $('#btnExcel').prop('disabled', false);
	else $('#btnExcel').prop('disabled', true);
}


//신청 상세내역 팝업창 띄우기
function openApprovalInfo(type, acptNo, reqCd, srId) {
	var nHeight, nWidth, cURL, winName;
	
	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;
    
	var form = document.popPam;   		//폼 name
    
	form.acptno.value	= acptNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	form.srid.value 	= srId;    		//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
	
    if(type === 1) {
    	nHeight = screen.height - 300;
	    nWidth  = screen.width - 400;
	    
	    cURL = "/webPage/winpop/PopRequestDetail.jsp";
    }
      
	myWin = winOpen(form, winName, cURL, nHeight, nWidth);
}