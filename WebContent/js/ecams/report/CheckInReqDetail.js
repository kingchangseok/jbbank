var userid = window.top.userId;

var mainGrid = new ax5.ui.grid();
var picker = new ax5.ui.picker();

$('#dateSt').val(getDate('DATE',0));
$('#dateEd').val(getDate('DATE',0));
picker.bind(defaultPickerInfoLarge('basic', 'top'));

$('[data-ax5select]').ax5select({
	option : []
});
$('[data-ax5select="cboSys"]').ax5select({
    options: []
});

mainGrid.setConfig({
	target : $('[data-ax5grid="mainGrid"]'),
	sortable : true,
	showLineNumber : false,
	showRowSelector : false,
	LineNumberColumWidth : 40,
	rowSelectorColumWidth : 27,
	header : {align: "center"},
	body : {
		onClick: function(){
			this.self.select(this.dindex); // 클릭한 row 선택
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
            {type: 1, label: "변경신청상세"},
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
	            	if(param.item.qrycd === '01'){
	            		return item.type == 1 | item.type == 2;
	            	}
        	 */
         	firstGrid.clearSelect();
         	firstGrid.select(Number(param.dindex));
       	 
       	var selIn = firstGrid.selectedDataIndexs;
       	if(selIn.length === 0) return;
       	 
        	if (param.item == undefined) return false;
        	if (param.dindex < 0) return false;
        	
        	
        	if (param.item.colorsw === '9') return item.type == 1 | item.type == 2;
        	else if ( (param.item.qrycd === '07' || param.item.qrycd === '03' || param.item.qrycd === '04' 
        			|| param.item.qrycd === '06' || param.item.qrycd === '16') 
        			&& (userid === param.item.editor2 || isAdmin) ) return true; 
        	else return item.type == 1 | item.type == 2;
        },
        onClick: function (item, param) {
    		openWindow(item.type, param.item.cr_qrycd, param.item.cr_acptno,'');
           firstGrid.contextMenu.close();//또는 return true;
        }
    },
	columns : [	
			{key : "cm_sysmsg",		label : "시스템",		align : "left",		width: "12%"}, 
			{key : "acptno",      	label : "신청번호",	align : "center",	width: "10%"}, 
			{key : "cr_prjno",     	label : "관련문서",	align : "left",		width: "12%"},
			{key : "cr_rsrcname",   label : "프로그램명",	align : "left",		width: "14%"}, 
			{key : "status",     	label : "상태",		align : "center",	width: "6%"}, 
			{key : "acptday",     	label : "신청일",		align : "center",	width: "12%"}, 
			{key : "acpttime",     	label : "신청시간",	align : "center",	width: "12%"}, 
			{key : "cm_deptname",   label : "신청팀",		align : "center",	width: "8%"}, 
			{key : "cm_username",   label : "신청자",		align : "center",	width: "6%"}, 
			{key : "passsub",     	label : "긴급여부",	align : "center",	width: "6%"}
		]
});

$(document).ready(function() {
	getSysInfo();

	//엑셀저장
	$("#btnExcel").on('click', function() {
		var today = getDate('DATE',0);
		mainGrid.exportExcel("체크인내역 세부조회_ " + today + ".xls");
		});
	
	//조회 클릭 시
	$("#btnSearch").bind('click', function() {
		search();
	});
})

function search(){
	var strStd = "";
	var strEdd = "";
	var strSys = "";
	
	tmpInfoData = new Object();
	
	strStD = $("#dateSt").val().substr(0,4) + $("#dateSt").val().substr(5,2) + $("#dateSt").val().substr(8,2);
	strEdD = $("#dateEd").val().substr(0,4) + $("#dateEd").val().substr(5,2) + $("#dateEd").val().substr(8,2);
	if(getSelectedIndex('cboSys') > 0) strSys = getSelectedVal('cboSys').value;
	
	tmpInfoData = {
		UserId : userId,
		SysCd : strSys,
		StDate : strStD,	
		EdDate : strEdD,
		PrjNo : $('#txtPrjno').val().trim(),
		requestType	: 'getReqList'
	}
	
	console.log(tmpInfoData);
	ajaxAsync('/webPage/ecmp/Cmp1700Servlet', tmpInfoData, 'json', successGetReqList);
	
	$('[data-ax5grid="mainGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
}

function successGetReqList(data){
	console.log(data);
	$(".loding-div").remove();
	mainGrid.setData(data);
}

//시스템 콤보박스 세팅
function getSysInfo() {
	tmpInfoData = new Object();
	tmpInfoData = {
		UserId  : userid,
		SecuYn  : "Y",
		SelMsg  : "ALL",
		CloseYn : "N",
		ReqCd 	: "",
		requestType : 'getSysInfo'
	}
	ajaxAsync('/webPage/common/SysInfoServlet', tmpInfoData, 'json', successGetSysInfo);
}

function successGetSysInfo(data) {
	var sysInfoData = [];
	for (var i = 0; i < data.length; i++) {
		if(data[i].cm_sysinfo.substr(0,1) != '1') {
			sysInfoData.push(data[i]);
		}
	}
	$('#cboSys').ax5select({
	    options: injectCboDataToArr(sysInfoData, 'cm_syscd', 'cm_sysmsg')
	});
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
    
    f.acptno.value	= replaceAllString(reqNo, "-", "");    	//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.user.value 	= userid;    							//POST방식으로 넘기고 싶은 값(hidden 변수에 값을 넣음)
    f.adminYN.value = adminYN;
    f.rgtList.value = rgtList;
    
	if (type == 1) {
		nHeight = 740;
	    nWidth  = 1300;
	    
	    if(reqCd == "01" || reqCd == "02" || reqCd == "11"){
	    	cURL = "/webPage/winpop/PopRequestDetailCkOut.jsp";
		    }else if(reqCd != "32") {
		    	cURL = "/webPage/winpop/PopRequestDetail.jsp";
			} else {
				nHeight = 515;
			    nWidth  = 1300;
				cURL = "/webPage/winpop/PopSRDevPlanInfo.jsp";
			}
		} else if (type == 2) {
			nHeight = 828;
		    nWidth  = 1046;

			cURL = "/webPage/winpop/PopApprovalInfo.jsp";
		} else {
			dialog.alert('화면링크정보가 없습니다. type='+type+', reqCd='+reqCd);
			return;
		}
		console.log('openWindow reqNo='+reqNo+', cURL='+cURL);
	    myWin = winOpen(f, winName, cURL, nHeight, nWidth);
}
