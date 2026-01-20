/**
 * 개발계획/완료 탭 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 허정규
 * 	버전 : 1.0
 *  수정일 : 2020-04-23
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var codeList        = window.top.codeList;      //전체코드리스트

//public
var strIsrId		= window.parent.strIsrId;  

var firstGrid 			= new ax5.ui.grid();
var secondGrid 			= new ax5.ui.grid();
var picker = new ax5.ui.picker();

var strReqCD = "";
var cc_srid = "";
var SysCd = "";
var cc_srtitle = "";
var SRStatus = "";
var GradeCd = "";
var AcptNo = "";

ax5.info.weekNames = [
    {label: "일"},
    {label: "월"},
    {label: "화"},
    {label: "수"},
    {label: "목"},
    {label: "금"},
    {label: "토"}
];

//개발담당자
$('[data-ax5select="cbouser"]').ax5select({
	options: []
});

$(document).ready(function(){

	createGrid = false;

	$("#btnSaveExcel1").bind("click",function(){
		firstGrid.exportExcel("eCmc0200_acptList_ " + userId + ".xls");
	});

	$("#btnSaveExcel2").bind("click",function(){
		secondGrid.exportExcel("eCmc0200_rscList_ " + userId + ".xls");
	});
	
	$("[name='rbgResource']").bind("change",function(){
		rbChange();
	});
});

function createViewGrid() {
//	if(!createGrid) {
	firstGrid.setConfig({
		    target: $('[data-ax5grid="grdList1"]'),
		    sortable: true, 
		    multiSort: true,
		    showRowSelector: false,
		    multipleSelect: false,
		    paging: false,
		    page : {
		    	display:false
		    },
		    header: {
		        align: "center"
		    },
		    body: {
		        onClick: function () {
		        	 if (this.colIndex == 7 && (this.item.cc_qrycd == '32' || this.item.cc_qrycd == '04')) {

			     		openWindow(2, this.item.cc_qrycd, this.item.cc_acptno2,'');
		 	        } else { 
			        	this.self.clearSelect();
			            this.self.select(this.dindex);
			            if(this.dindex != undefined && this.dindex >= 0){
			            	$("#rb2").prop("checked", true);
			            	rbChange();
			            }
		 	        }
		        },
		        onDBLClick: function () {
		        	this.self.clearSelect();
		        	this.self.select(this.dindex);
		        	openWindow(1, this.item.cc_qrycd, this.item.cc_acptno2,'');
		        },
		    	onDataChanged: function(){
		    		this.self.repaint();
		    	},
		    	trStyleClass: function () {
		    		if(this.item.cc_statuscd == "3"){
		    			return 'fontStyle-cncl';
		    		} else if (this.item.cc_statuscd == "0") {
		    			return 'fontStyle-ing';
		    		} else {
		    			return '';
		    		}
		    	}
		    },
		     contextMenu: {
		         iconWidth: 20,
		         acceleratorWidth: 100,
		         itemClickAndClose: false,
		         icons: {
		             'arrow': '<i class="fa fa-caret-right"></i>'
		         },
		         items: [
		             {type: 1, label: "변경신청상세"},
		             {type: 2, label: "결재정보"}
		         ],
		         popupFilter: function (item, param) {
			        	if(isNaN(param.dindex)){
			        		firstGrid.contextMenu.close();
			        		return false;
			        	}
			         	return true;
			       	 
			      },
		         onClick: function (item, param) {
		     		/*swal({
		                 title: item.label+"팝업",
		                 text: "신청번호 ["+param.item.acptno2+"]["+param.item.qrycd2+"]"
		             });*/
		     		openWindow(item.type, param.item.cc_qrycd, param.item.cc_acptno2,'');
		             firstGrid.contextMenu.close();//또는 return true;
		         }
		     },
		    columns: [
		        {key: "cc_sysmsg",  label: "시스템",		width: '14%', 	align: "center"},
		        {key: "cc_acptno",  label: "신청번호",		width: '14%', 	align: "center"},
		        {key: "cc_acptdate",  label: "신청일시",		width: '14%', 	align: "center"},
		        {key: "cc_qrygbn",  label: "신청구분",		width: '14%', 	align: "center"},
		        {key: "cm_username",  label: "신청자",		width: '10%', 	align: "center"},
		        {key: "cc_status",  label: "진행상태",		width: '10%', 	align: "center"},
		        {key: "cc_realdate",  label: "완료일시",		width: '14%', 	align: "center"},
		        {key: 'btnApproval', label: '결재정보',  width: '8%',
		        	formatter: function() {
		        		if (this.item.cc_qrycd != '32' && this.item.cc_qrycd != '04') {
		        			return '';
		        		} else {
	        				return '<button style="width: 98%; height: 98%;">결재정보</button>';
		        		}
		        	}
		        }
		    ]
		});
	secondGrid.setConfig({
		    target: $('[data-ax5grid="grdList2"]'),
		    sortable: true, 
		    multiSort: true,
		    showRowSelector: false,
		    multipleSelect: false,
		    paging: false,
		    page : {
		    	display:false
		    },
		    header: {
		        align: "center"
		    },
		    body: {
		        onClick: function () {
		        	this.self.clearSelect();
		            this.self.select(this.dindex);
		        },
		        onDBLClick: function () {
		        	this.self.clearSelect();
		        	this.self.select(this.dindex);
		        	downSelectDoc(this.dindex, this.item);
		        },
		    	onDataChanged: function(){
		    		this.self.repaint();
		    	},
		    	trStyleClass: function () {
		    		if(this.item.cr_itemid != this.item.cr_baseitem){
		    			return 'fontStyle-module';
		    		} else {
		    			return '';
		    		}
		    	}
		    },
		    columns: [
		        //{key: "cc_acptno",  label: "신청번호",		width: 110, 	align: "center"},
		        {key: "cc_rsrcname",  label: "프로그램명",		width: 150, 	align: "center"},
		        {key: "cm_codename",  label: "진행상태",		width: 100, 	align: "center"},
		        {key: "version",  label: "버전",		width: 90, 	align: "center"},
		        {key: "cc_dirpath",  label: "프로그램경로",		width: '10%', 	align: "left"}
		    ]
		});
	
	createGrid = true;
}

function setSRInfo_detail_func() {
	if(cc_srid == "" || cc_srid == null) return;
	$("#txtSRID").val(cc_srid);
	$("#txtSRTitle").val(cc_srtitle);
	
	var SRInfo = {
			srId: 	cc_srid,
			requestType: 	'getSRList_detail'
		}
		
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', SRInfo, 'json');
	if(ajaxReturnData <= 0) {
		firstGrid.setData([]);
		$("#btnSaveExcel1").prop("disabled", true);
		
	}else {
		firstGrid.setData(ajaxReturnData);
		$("#btnSaveExcel1").prop("disabled", false);
	}
	
	$("#rb1").prop("checked", true);
	
	rbChange();
	 
}
function rbChange() {
	if( $("#rb1").is(":checked") ){
		getSRList_resource("",cc_srid);
	} 
	else if ( $("#rb2").is(":checked") ) {
		if(firstGrid.getList().length == 0) {
			secondGrid.setData([]);
			$("#btnSaveExcel2").prop("disabled",true);
		}else{
			if(firstGrid.getList("selected").length < 1){
				return;
			}
			var selectedItem = firstGrid.getList("selected")[0];
			getSRList_resource(selectedItem.cc_acptno2,selectedItem.cc_srid);
		}
	} 		
}

function getSRList_resource(acptno, srid){

	var SRInfo = {
			acptno: 	acptno,
			srId: 	srid,
			requestType: 	'getSRList_resource'
		}
		
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', SRInfo, 'json');
	
	if(ajaxReturnData.length <= 0){ 
		secondGrid.setData([]);
		$("#btnSaveExcel2").prop("disabled",true);
	}
	else {
		secondGrid.setData(ajaxReturnData);
		$("#btnSaveExcel2").prop("disabled",false);
	}
}

function openWindow(type,reqCd,reqNo,rsrcName) {
	var nHeight, nWidth, cURL, winName;

	if ( (type+'_'+reqCd) == winName ) {
		if (myWin != null) {
	        if (!myWin.closed) {
	        	myWin.close();
	        }
		}
	}

    winName = type+'_'+reqCd;

	var f = document.popPam;   		//폼 name
    
    f.acptno.value	= reqNo;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userId;    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    
	if (type == 1) {
	    if(reqCd == "32"){
	    	nHeight = 515;
	    	nWidth  = 1300;
	    	cURL = "/webPage/winpop/PopSRDevPlanInfo.jsp";
		} else {
			nHeight = 740;
			nWidth  = 1300;
		    cURL = "/webPage/winpop/PopRequestDetail.jsp";
		}
			
	    
	} else if (type == 2) {
		nHeight = 828;
	    nWidth  = 1046;

		cURL = "/webPage/winpop/PopApprovalInfo.jsp";
	}
	
	//console.log('+++++++++++++++++'+cURL);
    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
/*
 * 사용하지 않는 기능
function chkdetail_click(){
	if (chkdetail.selected){
		grdList2.dataProvider = grdList2_dp;
	}else{
		var grid_simple_dp:ArrayCollection = new ArrayCollection();
		grid_simple_dp.source = grdList2_dp.source;
		grid_simple_dp.filterFunction = simple_data_Filter;
		grid_simple_dp.refresh();
		grdList2.dataProvider = grid_simple_dp;
	}
}

private function simple_data_Filter(item:Object):Boolean{
	if (item.cr_baseitem != item.cr_itemid){
		return false;
	}
	else{
		return true;
	}
}
*/
