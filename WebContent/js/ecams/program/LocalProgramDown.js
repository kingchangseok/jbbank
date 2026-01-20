var userName 	      = window.top.userName;
var userId 			  = window.top.userId;
var adminYN		      = window.top.adminYN;
var userDeptName      = window.top.userDeptName;
var userDeptCd 	      = window.top.userDeptCd;
//var reqCd 			  = window.top.reqCd;

var pItemId	= "";		//프로그램id: itemid
var pUserId	= userId;		//접속자 ID
var reqCd	= "02";			//신청구분
var sysCd = "";


var firstGrid = new ax5.ui.grid();
var picker	= new ax5.ui.picker();
var ChkOutVerSelModal = new ax5.ui.modal();
var fileDownModal	= new ax5.ui.modal();

var ztree = null;
var treeData = [];
var firstGridData = [];
var progFileList = [];

var alertFlag = "1";
var searchMOD = "";

$("#cboSys").ax5select({
	options : []
});

$(document).ready(function() {
	picker.bind(defaultPickerInfo('basic', 'top'));
	$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});
	getDownSys();
	createGrid();
	getRsrcPath();
	
	$("#cboSys").bind('change', function() {
		getRsrcPath();
	});
	
	$("#btnSearch").bind('click', function() {
		clickSearchBtn();
	});
	
	$("#btnDown").bind('click', function() {
		btnDownClick();
	});
	
	$("#txtPrg").bind('keyup', function(e) {
		if(e.keyCode == 13) {
			clickSearchBtn();
		}
	})
});

function createGrid() {
	firstGrid.setConfig({
	    target: $('[data-ax5grid="first-grid"]'),
	    sortable: true, 
	    multiSort: true,
	    multipleSelect: true,
	    showRowSelector: true, //checkbox option
	    //rowSelectorColumnWidth: 26 
	    header: {
	        align: 'center'
	    },
	    body: {
	        onClick: function () {
	            // console.log(this);
	        	//this.self.clearSelect(); //기존선택된 row deselect 처리 (multipleSelect 할땐 제외해야함)
	            
		        if (this.colIndex == 5) {//버전선택
		        	this.self.clearSelect();
		        	this.self.select(this.dindex);
		        	
		        	var gridSelIdx = this.dindex;
		        	
		        	pItemId = this.item.cr_itemid;
		        	
		    		setTimeout(function() {
		    			ChkOutVerSelModal.open({
		    				width: 1000,
		    				height: 500,
		    				iframe: {
		    					method: "get",
		    					url: "../modal/request/ChkOutVerSelModal.jsp",
		    					param: "callBack=chkOutVerSelModalCallBack"
		    				},
		    				onStateChanged: function () {
		    					if (this.state === "open") {
		    						mask.open();
		    					}
		    					else if (this.state === "close") {
		    						console.log(gridSelIdx, updateFlag, selectVer, selectAcptno, selectViewVer);
		    						
		    						if (updateFlag) {
//			        	        	if (selectViewVer == 'sel') {
//				        	        	firstGrid.setValue(gridSelIdx, 'cr_viewver', 'sel');
//				        	        	firstGrid.setValue(gridSelIdx, 'selAcptno', '');
//				        	        	firstGrid.setValue(gridSelIdx, 'cr_lstver', '');
//			        	        	} else {
		    							firstGrid.setValue(gridSelIdx, 'scrver', selectViewVer);
		    							firstGrid.setValue(gridSelIdx, 'selAcptno', selectAcptno);
		    							firstGrid.setValue(gridSelIdx, 'cr_lstver', selectVer);
//			        	        	}
		    						}
		    						mask.close();
		    						firstGrid.repaint();
		    					}
		    				}
		    			}, function () {
		    				
		    			});
		    		}, 200);
		        } else {
		        	this.self.select(this.dindex);
//		        	firstGridClick();
		        }
	        },
	        onDBLClick: function () {
	        	this.self.clearSelect();
	        	this.self.select(this.dindex);
	        },
	    	trStyleClass: function () {
	    	},
	    	onDataChanged: function(){
	    		//그리드 새로고침 (스타일 유지)
	    	    this.self.repaint();
	    	}
	    },
	    columns: [
			{key: "viewpcdir",		label: "프로그램경로"		, width: "30%", align: "center"},
			{key: "cr_rsrcname",		label: "프로그램명"		, width: "10%", align: "center"},
			{key: "rsrccd",		label: "프로그램종류"		, width: "10%", align: "center"},
			{key: "sta",		label: "상태"		, width: "5%", align: "center"},
			{key: "cr_lstver",		label: "형상관리버전"		, width: "8%", align: "center"},
	        {key: 'cr_lstver', label: '버전선택',  width: '10%',
	        	formatter: function() {
        			if (this.value == 'sel') {
        				return '<button style="width: 98%; height: 98%;">버전선택</button>';
        			} else {
        				return '<button style="width: 98%; height: 98%;">버전: '+ this.value +'</button>';
        			}
	        	}
	        },
			{key: "cm_username",		label: "수정자"		, width: "8%", align: "center"},
			{key: "outdate",		label: "수정일"		, width: "10%", align: "center"},
	    ]
	});
}

function getRsrcPath() {
	var tmpData = {
		UserID: userId,
		SysCd: 	getSelectedVal('cboSys').cm_syscd,
		SecuYn: 'y',
		SinCd: 	"01",
		ReqCd: 	'',
		requestType: 'getRsrcPath_ztree'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', tmpData, 'json',successGetFileTree);
}

function successGetFileTree(data){
	/*if (data.length < 2) {
		dialog.alert("체크아웃 대상경로가 없습니다.");
		ztree = $.fn.zTree.init($('#treeDemo'), setting, treeData);
		return;
	}*/
	
	treeData = data;
	var setting = {
			data: {
				simpleData: {
					enable: true,
				}
			},
			callback:{
				onExpand: myOnExpand,
				onClick : onClickTree,
			}
	};
	
	if(treeData !== 'ERR') {
		var obj = treeData;
		
		for(var i in treeData){
			if(obj[i].cm_codename =='' ){
				delete obj[i]
				continue;
			}
			obj[i].name = obj[i].cm_codename;
			delete obj[i].cm_codename;
			obj[i].isParent = true;
		}
		obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
		obj = JSON.parse(obj);
		treeData = obj;
		
		ztree = $.fn.zTree.init($('#treeDemo'), setting, treeData);
	}
}


// 폴더 펼칠때 실행하는 함수
function myOnExpand(event, treeId, treeNode) {
	//root node 만 비동기 방식으로 뽑아오는 조건
	if(treeNode.pId != null || treeNode.children != undefined){ //treeNode.pid != -1 ||
		return false;
	};
	//로딩중 icon class 추가
	$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_loading');
	setTimeout(function(){

		var ajaxReturnData = null;
		
		var reqyestType = "";
		var tmpData = {};
		
		tmpData = {
			UserId : userId,
			SysCd : getSelectedVal('cboSys').value,
			RsrcCd : treeNode.cr_rsrccd,
			Info : treeNode.cm_info,
			seqNo : treeNode.cm_upseq,
			requestType:  "getDirPath3_ztree"
		}
		
		ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
		
		if (ajaxReturnData == undefined || ajaxReturnData == null) return;
		if (ajaxReturnData.indexOf('ERROR')>-1) {
			dialog.alert(ajaxReturnData);
		} else {
			var obj = ajaxReturnData;
				
			for(var i in ajaxReturnData){
				if(obj[i].cm_dirpath =='' ){
					delete obj[i]
					continue;
				}
				obj[i].name = obj[i].cm_dirpath;
				delete obj[i].cm_dirpath;
				obj[i].isParent = true;
			}
			obj = JSON.stringify(obj).replace(/null,/gi,''); // delete 를 해도 empty 값으로 남아있어서 지워줌
			obj = JSON.parse(obj);
			ajaxReturnData = obj;
			
			ztree.addNodes(treeNode,ajaxReturnData)
			$('#'+treeNode.tId+'_ico').removeClass().addClass('button ico_open');
		}
	}, 200);
};

function onClickTree(event, treeId, treeNode) {
	var sysCd  		= getSelectedVal('cboSys').value;
	var sysgb  		= getSelectedVal('cboSys').cm_sysgb;
	
	var hasChild 	= treeNode.children != undefined? true : false;
	var root 		= treeNode.root;
	var fileId 		= treeNode.id;
	var fileInfo 	= treeNode.cm_info;
	var dsncd  		= treeNode.cr_dsncd;
	var fileinfo 	= treeNode.cm_info;
	var rsrccd 		= treeNode.cr_rsrccd;
	var subrsrccd	= treeNode.cr_rsrccd
	var fullpath = null;
	
	if(treeNode.cm_volpath == null){
		fullpath = treeNode.cm_fullpath;
	}else{
		fullpath = treeNode.cm_volpath;
	}
	
	if(fileId != '-1' ) {
		if( rsrccd ===  undefined) rsrccd = subrsrccd;
		//if( root === 'true' && !hasChild ) 					getChildFileTree(fileInfo, rsrccd, fileId);
		if( dsncd !== undefined || fullpath !== undefined) 	makeFileDir(fullpath, dsncd, fileinfo, hasChild, sysgb, rsrccd, sysCd);
	}
	
	searchMOD = 'T';
	
}

function getDownSys() {
	var etcData;
	var returnData;
	etcData = {
		UserID : userId,
		requestType : "getDownSys"
	};
	returnData = ajaxCallWithJson('/webPage/common/SysInfoServlet', etcData, 'json');
	returnData = returnData.filter(function(item) {
		if( (item.cm_sysinfo.substr(6,1) == "1") && (item.localyn == "A" || item.localyn == "L") ) return true;
			else return false;
	});
	
	$("#cboSys").ax5select({
		options : injectCboDataToArr(returnData, "cm_syscd", "cm_sysmsg")
	});
	
	for(i in returnData) {
		if(returnData[i].cm_setyn == "Y") {
			$("#cboSys").ax5select("setValue", i);
			break;
		}
	}
}

function btnDownClick() {
	progFileList = firstGrid.getList("selected");
	sysCd = getSelectedVal("cboSys").cm_syscd;
	setTimeout(function() {
		fileDownModal.open({
			width: 800,
			height: 500,
			iframe: {
				method: "get",
				url: "../modal/request/ProgFileDownModal.jsp",
				param: "callBack=fileDownModalCallBack"
			},
			onStateChanged: function () {
				if (this.state === "open") {
					mask.open();
				}
				else if (this.state === "close") {
					mask.close();
					firstGrid.repaint();
				}
			}
		});
	}, 200);
}


function makeFileDir(fullpath, dsncd, fileinfo, hasChild, sysgb, rsrccd, sysCd){
	var lb6split 	= fullpath.split('/');
	var lb6String1 	= '';
	var lb6		 	= '';
	var toDsnCd 	= fullpath;
	var strDsn 		= dsncd;
	var devToolCon 	= false;
	var getFileData = {};
	var rsrcname = $('#txtPrg').val().trim();
	var selectedSubnode = $('#chkbox_subnode').is(':checked');
	
	if(!devToolCon && fileinfo != undefined && fileinfo.substr(26,1) == '1') {
		devToolCon = true;
	}
	
	if (lb6split.length < 2){
		$('#idx_lbl_path').text('');
	}
	
	for (var i=0 ; i < lb6split.length ; i++){
		if (lb6split[i].length>0) {
			if (i==0 && lb6split[i].indexOf(':')>0) {
				lb6String1 = lb6split[i];
			} else {
				lb6String1 = lb6String1+ '/'+ lb6split[i];
			}
		}
	}
	
	if (lb6String1.length > 0){
		$('#idx_lbl_path').text(lb6String1);
	}
	
	if (selectedSubnode  && lb6String1.length > 0 ){//하위디렉토리 포함 일때
		toDsnCd = 'F' + lb6String1 + '/';
	}else if (!selectedSubnode && lb6String1.length > 0 && devToolCon && !hasChild){
		toDsnCd = 'F' + lb6String1;
	}else if (!selectedSubnode && lb6String1.length > 0){
		toDsnCd = 'G' + lb6String1 + '/';
	}else if (strDsn != '' && strDsn != null) toDsnCd = strDsn;
	else toDsnCd = '';
	
	if (toDsnCd.length > 0 || (toDsnCd.length == 0 && selectedSubnode) ) {
		getFileData.UserId = userId;
		getFileData.SysCd = sysCd;
		getFileData.SysGb = sysgb;
		if (toDsnCd.length > 0){
			getFileData.DsnCd = toDsnCd;
		} 
		getFileData.RsrcCd = rsrccd;
		getFileData.ReqCd = reqCd;
		if(rsrcname === undefined) getFileData.RsrcName = '';
		else getFileData.RsrcName = rsrcname;
		getFileData.JobCd = "0000";
		getFileData.selfSw = false;
		getFileData.UpLowSw = false;
		
		getFileGridData(getFileData);
	}
	searchMOD = 'T';
}
function getFileGridData(getFileData) {
	var outDate = "";
	var dsnCd = "";
	if($("#chkStrdt").is(":checked")) {
		outDate = $("#dateSt").val().substr(0,4) + $("#dateSt").val().substr(5,2) + $("#dateSt").val().substr(8,2);
	}
	if(getFileData.DsnCd != null && getFileData.DsnCd.length > 0) {
		dsnCd = getFileData.DsnCd;
	}
	var getFileDataInfo = {
		userid: userId,
		syscd: getSelectedVal('cboSys').cm_syscd,
		svrcd: getSelectedVal('cboSys').cm_svrcd,
		svrseq: getSelectedVal('cboSys').cm_seqno,
		rsrcname: $("#txtPrg").val().trim(),
		DsnCd: "",
		strDate: outDate,
		rsrccd: getFileData.RsrcCd,
		requestType	: 'getFileList',
	}
	ajaxAsync('/webPage/ecmd/Cmd1500Servlet', getFileDataInfo, 'json',successGetFileGridData);	
}

function successGetFileGridData(data){
	if (data == undefined || data == null) return;
	if (data.indexOf('ERROR')>-1) {
		dialog.alert(data);
	} else {
		firstGridData = data;

		//컬럼제거하기
		firstGridColumns = firstGrid.columns;
		if(firstGridColumns[2].key == 'errmsg'){
			firstGrid.removeColumn(2);
		}

		if(null == firstGridData || firstGridData.length == 0){
			dialog.alert('검색 결과가 없습니다.');
			firstGrid.setData([]);
		} else {
			alertFlag = 0;
			
			if(firstGridData[0].cr_rsrcname == null || firstGridData[0].cr_rsrcname == "" ) {
				dialog.alert("로컬 홈디렉토리를 지정하지 않았습니다. 기본관리>사용자환경설정에서 홈디렉토리지정 후 처리하시기 바랍니다.");
				$("#btnDown").prop("disalbed", true);
				return;
			} else {
				$("#btnDown").prop("disalbed", false);
			}
			firstGrid.setData(firstGridData);
		}
	}
}

function clickSearchBtn() {
	alertFlag = 1;
	var getFileData = {};
//	var rsrcname 	= null;
//	
//	getFileData.UserId 	= userId;
//	getFileData.SysCd 	= getSelectedVal('cboSys').value;
//	getFileData.SysGb 	= getSelectedVal('cboSys').cm_sysgb;
//	getFileData.DsnCd 	= "";
//	getFileData.RsrcCd 	= "";
////	getFileData.JobCd 	= getSelectedVal('cboJob').cm_jobcd;
//	getFileData.ReqCd 	= reqCd;
//	getFileData.UpLowSw 	= false;
//	getFileData.selfSw 	= false;
//	rsrcname = $('#txtRsrcName').val().trim();
//	if(rsrcname === '' || rsrcname === undefined) {
//		dialog.alert('검색단어를 입력한 후 검색하시기 바랍니다.');
//		return;
//	}
//	else getFileData.RsrcName = rsrcname;
	
	searchMOD = 'B';
	getFileGridData(getFileData);
}

//이전버전 선택 모달 창 닫기
var chkOutVerSelModalCallBack = function() {
	ChkOutVerSelModal.close();
}